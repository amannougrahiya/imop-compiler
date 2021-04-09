/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

import imop.ast.info.NodeInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.cfgTraversals.GJDepthFirstCFG;
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.analysis.flowanalysis.AddressCell;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.FieldCell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis.PointsToGlobalState;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.*;
import imop.lib.analysis.flowanalysis.generic.CellularDataFlowAnalysis.CellularFlowMap;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis.FlowFact;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.CellSet;
import imop.lib.util.CollectorVisitor;
import imop.lib.util.Immutable;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.*;

/**
 * A generic class to implement an inter-thread interprocedural forward
 * iterative data-flow analysis.
 */
@Deprecated
public abstract class InterThreadForwardIDFA<F extends FlowFact> extends GJDepthFirstCFG<F, F> {

	@SuppressWarnings("unused")
	private static class TempList extends ArrayList<Node> {
		private static final long serialVersionUID = 1L;

		public void recreate() {
			this.clear();
		}

		public Node removeAnyElement() {
			if (!this.isEmpty()) {
				return this.remove(0);
			}
			return null;
		}
	}

	public static enum AnalysisFlowNature {
		DATAFLOW, CONTROLFLOW
	}

	/**
	 * Name (unique-ID) to denote this analysis.
	 */
	private AnalysisName analysisName;
	/**
	 * Specifies whether this analysis is:
	 * <ul>
	 * <li>flow-sensitive/insensitive</li>
	 * <li>intraprocedural/interprocedural</li>
	 * <li>context-sensitive/insensitive</li>
	 * <li>field-sensitive/insensitive</li>
	 * <li>path-sensitive/insensitive</li>
	 * <ul>
	 */
	private AnalysisDimension analysisDimension;
	public AnalysisFlowNature flowNature = AnalysisFlowNature.DATAFLOW;

	/**
	 * A set of nodes with which we need to perform this analysis. <br>
	 * <i>Note that the processing of these nodes may recursively process the
	 * successor nodes as well.</i>
	 */
	ReversePostOrderWorkList workList = new ReversePostOrderWorkList();
	/**
	 * A set of analyses that require forward IDFA.
	 */
	private static HashMap<AnalysisName, InterThreadForwardIDFA<?>> analysisSet = new HashMap<>();
	private boolean isInvalid = false;
	// TempList workList = new TempList();
	public long nodesProcessed = 0;
	public long nodesProcessedDuringUpdate = 0;
	public int autoUpdateTriggerCounter = 0;
	public long forwardIDFAUpdateTimer = 0;
	private Set<Node> nodesToBeUpdated = new HashSet<>();

	private Set<Node> processedInThisUpdate = new HashSet<>();
	private Set<Node> yetToBeFinalized = new HashSet<>();

	private HashMap<Node, Set<Node>> reachablePredecessorsOfSeeds = new HashMap<>();

	public HashMap<Node, Integer> tempMap = new HashMap<>();

	public static void resetStaticFields() {
		InterThreadForwardIDFA.analysisSet = new HashMap<>();
	}

	/**
	 * Set of analyses that require forward IDFA.
	 *
	 * @return set of analyses that require forward IDFA.
	 */
	public static HashMap<AnalysisName, InterThreadForwardIDFA<?>> getForwardAnalyses() {
		return analysisSet;
	}

	public InterThreadForwardIDFA(AnalysisName analysisName, AnalysisDimension analysisDimension,
			AnalysisFlowNature flowNature) {
		this(analysisName, analysisDimension);
		this.flowNature = flowNature;
	}

	public InterThreadForwardIDFA(AnalysisName analysisName, AnalysisDimension analysisDimension) {
		this.analysisName = analysisName;
		analysisSet.put(analysisName, this);
		if (analysisDimension == null) {
			analysisDimension = new AnalysisDimension(Program.sveSensitive);
		}

		this.analysisDimension = analysisDimension;

		if (analysisDimension.getFlowDimension() == FlowDimension.FLOW_INSENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for flow-insensitive analyses.", null);
		}
		if (analysisDimension.getContextDimension() == ContextDimension.CONTEXT_SENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for context-sensitive analyses.", null);
		}
		if (analysisDimension.getFieldDimension() == FieldDimension.FIELD_SENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for field-sensitive analysis.", null);
		}
		if (analysisDimension.getPathDimension() == PathDimension.PATH_SENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for path-sensitive analysis.", null);
		}
		if (analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for intra-procedural analysis.", null);
		}
	}

	/**
	 * Obtain the Top element of the associated lattice for this IDFA.
	 *
	 * @return Top element of the associated lattice for this IDFA.
	 */
	public abstract F getTop();

	/**
	 * Obtain flowFact to be used when a node has no predecessors. Note that usually
	 * this flow-fact should reflect the
	 * state that should be the IN of the main's body.
	 *
	 * @return FlowFact to be used when a node has no predecessors.
	 */
	public abstract F getEntryFact();

	/**
	 * Perform the analysis, starting at the begin node of the function
	 * {@code funcDef}.<br>
	 * <i>Note that this method does not clear the analysis information, if
	 * already present.</i>
	 *
	 * @param funcDef
	 *                function-definition on which this analysis has to be
	 *                performed.
	 */
	public void run(FunctionDefinition funcDef) {
		if (this.analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL) {
			// Process all other reachable function definitions.
			for (FunctionDefinition reachableFuncDef : funcDef.getInfo().getReachableCallGraphNodes()) {
				this.runOne(reachableFuncDef);
			}
		} else {
			this.runOne(funcDef);
		}
	}

	private void runOne(FunctionDefinition funcDef) {
		BeginNode beginNode = funcDef.getInfo().getCFGInfo().getNestedCFG().getBegin();
		this.workList.recreate();
		this.workList.add(beginNode);
		do {
			Node nodeToBeAnalysed = this.workList.removeFirstElement();
			this.debugRecursion(nodeToBeAnalysed);
			if (this.flowNature == AnalysisFlowNature.DATAFLOW) {
				this.processWhenNotUpdatedForDataFlow(nodeToBeAnalysed);
			} else {
				this.processWhenNotUpdatedForControlFlow(nodeToBeAnalysed);
			}
		} while (!workList.isEmpty());
		if (this.analysisName == AnalysisName.POINTSTO) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.CORRECT;
		}

	}

	/**
	 * This method is usually called by getIN and getOUT methods to check if
	 * flow-facts of various nodes for this
	 * analysis reflect the state of the program.
	 *
	 * @return true, if the state maybe invalid.
	 */
	public boolean stateIsInvalid() {
		return isInvalid;
	}

	/**
	 * After deciding to run the stabilizing pass (by calling
	 * {@link #restartAnalysisFromStoredNodes()}), this method is
	 * called first, so that the stabilizing pass itself shouldn't go into a
	 * recursion were it to use the getIN and
	 * getOUT on the same analysis (e.g., in case of points-to analysis).
	 */
	public void markStateToBeValid() {
		this.isInvalid = false;
	}

	/**
	 * Given a set of nodes starting which this IDFA pass should be re-run, this
	 * method stores these nodes in an
	 * internal structure for later use.
	 *
	 * @param updateSeedSet
	 *                      set of nodes starting which this IDFA pass should be
	 *                      re-run.
	 */
	public void storeNodesToBeUpdated(Set<Node> updateSeedSet) {
		if (!this.isInvalid) {
			/*
			 * The state so far was correct. Set the invalid bit, clear the
			 * nodesToBeUpdated set, and add updateSeedSet to them.
			 */
			this.isInvalid = true;
			nodesToBeUpdated.clear();
		}
		for (Node n : updateSeedSet) {
			if (Misc.isCFGLeafNode(n)) {
				this.nodesToBeUpdated.add(n);
			} else {
				this.nodesToBeUpdated.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
			}
		}
		return;
	}

	/**
	 * This method re-runs this analysis starting with all (still connected) nodes
	 * from nodesToBeUpdated, and resets the
	 * isInvalid bit. As a result of this method invocation, the corresponding IDFA
	 * flow-fact of this pass should match
	 * the expected value.<br>
	 * IMPORTANT: For points-to analysis, note that this method will not trigger any
	 * update
	 * unless the state of points-to analysis is marked as
	 * {@link PointsToGlobalState#STALE}.
	 */
	public void restartAnalysisFromStoredNodes() {
		if (this.analysisName == AnalysisName.POINTSTO
				&& PointsToAnalysis.stateOfPointsTo != PointsToGlobalState.STALE) {
			nodesToBeUpdated.clear();
			return;
		}
		this.autoUpdateTriggerCounter++;
		long localTimer = System.nanoTime();
		/*
		 * Step 0: Collect information about reachable predecessor for seed
		 * nodes.
		 */
		this.populateReachablePredecessorMap();
		/*
		 * Step 1: Start processing from the leaf elements of updateSeedSet in
		 * "update" mode.
		 */
		this.workList.recreate();
		this.workList.addAll(nodesToBeUpdated);
		this.nodesToBeUpdated.clear();
		this.processedInThisUpdate = new HashSet<>();
		this.yetToBeFinalized = new HashSet<>();
		while (!workList.isEmpty()) {
			this.nodesProcessedDuringUpdate++;
			Node nodeToBeAnalysed = this.workList.removeFirstElement();
			this.debugRecursion(nodeToBeAnalysed);
			if (this.flowNature == AnalysisFlowNature.DATAFLOW) {
				this.processWhenUpdatedForDataFlow(nodeToBeAnalysed);
			} else {
				this.processWhenUpdatedForControlFlow(nodeToBeAnalysed);
			}
		}
		/****************************/
		/*
		 * Step 2: Reprocess those nodes whose IDFA is not yet finalized, in
		 * "normal" mode.
		 */
		this.workList.addAll(yetToBeFinalized);
		while (!workList.isEmpty()) {
			this.nodesProcessedDuringUpdate++;
			Node nodeToBeAnalysed = this.workList.removeFirstElement();
			this.debugRecursion(nodeToBeAnalysed);
			if (this.flowNature == AnalysisFlowNature.DATAFLOW) {
				this.processWhenNotUpdatedForDataFlow(nodeToBeAnalysed);
			} else {
				this.processWhenNotUpdatedForControlFlow(nodeToBeAnalysed);
			}
		}
		localTimer = System.nanoTime() - localTimer;
		this.forwardIDFAUpdateTimer += localTimer;
		if (this.analysisName == AnalysisName.POINTSTO
				&& PointsToAnalysis.stateOfPointsTo == PointsToGlobalState.STALE) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.CORRECT;
		}
	}

	/**
	 * Given a node connected to the program, this method ensures that the IN and
	 * OUT of all the leaf nodes within the
	 * given node are set to be the meet of OUT of all the predecessors of the entry
	 * point of the node (there must be
	 * just one entry point, and no internal barriers/flushes). Note that since the
	 * {@code affectedNode} has been
	 * checked to not affect the flow-analysis, we should use the same object for IN
	 * as well as OUT of each node.<br>
	 * Important: If the OUT of predecessors of the entry node is not stable, we
	 * will still use the unstable values for
	 * now; not that the stabilization process, whenever triggered, would take care
	 * of these nodes as well.
	 *
	 * @param affectedNode
	 *                     node which does not seem to affect the analysis (and
	 *                     hence, for which we just copy same IN and OUT to all
	 *                     its internal nodes).
	 */
	@SuppressWarnings("unchecked")
	public void cloneFlowFactsToAllWithinForDataFlow(Node affectedNode) {
		Node node;
		if (Misc.isCFGLeafNode(affectedNode)) {
			node = affectedNode;
		} else {
			node = affectedNode.getInfo().getCFGInfo().getNestedCFG().getBegin();
		}

		NodeInfo nodeInfo = node.getInfo();
		Set<IDFAEdge> predecessors = nodeInfo.getCFGInfo()
				.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension());
		F ffForAll = predecessors.isEmpty() ? this.getEntryFact() : this.getTop();

		for (IDFAEdge idfaEdge : predecessors) {
			F predOUT = (F) idfaEdge.getNode().getInfo().getCurrentOUT(analysisName);
			// F predOUT = (F) idfaEdge.getNode().getInfo().getOUT(analysisName);
			if (predOUT == null) {
				continue;
			}
			F edgeOUT = this.edgeTransferFunction(predOUT, idfaEdge.getNode(), node);
			ffForAll.merge(edgeOUT, idfaEdge.getCells());
		}
		nodeInfo.setIN(analysisName, ffForAll);
		nodeInfo.setOUT(analysisName, ffForAll);
		for (Node internalLeaf : affectedNode.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			if (internalLeaf == affectedNode) {
				continue;
			}
			F newObj = this.getTop();
			newObj.merge(ffForAll, null);
			internalLeaf.getInfo().setIN(analysisName, newObj);
			internalLeaf.getInfo().setOUT(analysisName, newObj);
		}
	}

	/**
	 * Given a node connected to the program, this method ensures that the IN and
	 * OUT of all the leaf nodes within the
	 * given node are set to be the meet of OUT of all the predecessors of the entry
	 * point of the node (there must be
	 * just one entry point, and no internal barriers/flushes). Note that since the
	 * {@code affectedNode} has been
	 * checked to not affect the flow-analysis, we should use the same object for IN
	 * as well as OUT of each node.<br>
	 * Important: If the OUT of predecessors of the entry node is not stable, we
	 * will still use the unstable values for
	 * now; not that the stabilization process, whenever triggered, would take care
	 * of these nodes as well.
	 *
	 * @param affectedNode
	 *                     node which does not seem to affect the analysis (and
	 *                     hence, for which we just copy same IN and OUT to all
	 *                     its internal nodes).
	 */
	@SuppressWarnings("unchecked")
	public void cloneFlowFactsToAllWithinForControlFlow(Node affectedNode) {
		Node node;
		if (Misc.isCFGLeafNode(affectedNode)) {
			node = affectedNode;
		} else {
			node = affectedNode.getInfo().getCFGInfo().getNestedCFG().getBegin();
		}

		NodeInfo nodeInfo = node.getInfo();
		Set<Node> predecessors = nodeInfo.getCFGInfo().getInterProceduralLeafPredecessors();
		F ffForAll = predecessors.isEmpty() ? this.getEntryFact() : this.getTop();

		for (Node predNode : predecessors) {
			F predOUT = (F) predNode.getInfo().getCurrentOUT(analysisName);
			// F predOUT = (F) idfaEdge.getNode().getInfo().getOUT(analysisName);
			if (predOUT == null) {
				continue;
			}
			F edgeOUT = this.edgeTransferFunction(predOUT, predNode, node);
			ffForAll.merge(edgeOUT, null);
		}
		nodeInfo.setIN(analysisName, ffForAll);
		nodeInfo.setOUT(analysisName, ffForAll);
		for (Node internalLeaf : affectedNode.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			if (internalLeaf == affectedNode) {
				continue;
			}
			F newObj = this.getTop();
			newObj.merge(ffForAll, null);
			internalLeaf.getInfo().setIN(analysisName, newObj);
			internalLeaf.getInfo().setOUT(analysisName, newObj);
		}
	}

	/**
	 * Given an edge from {@code predecessor} to {@code successor}, this method
	 * models the effect of the edge on {@code
	 * edgeIN} to return {@code edgeOUT}.
	 *
	 * @param edgeIN
	 * @param predecessor
	 * @param successor
	 *
	 * @return
	 */
	protected F edgeTransferFunction(F edgeIN, Node predecessor, Node successor) {
		return edgeIN;
	}

	/**
	 * Called before starting of the {@link #restartAnalysisFromStoredNodes()}, this
	 * method clears and populates the map
	 * {@link #reachablePredecessorsOfSeeds}, where each entry maps a seed-node
	 * (from {@link #nodesToBeUpdated}) to a
	 * set of predecessor nodes that are reachable from the seed-node. UPDATE: This
	 * method has been intentionally made
	 * intra-task, for efficiency purposes.
	 */
	private void populateReachablePredecessorMap() {
		reachablePredecessorsOfSeeds.clear();
		for (Node node : this.nodesToBeUpdated) {
			/*
			 * Collect all those predecessors of node that are reachable from
			 * the node.
			 */
			NodeWithStack startPoint = new NodeWithStack(node, new CallStack());
			Set<Node> reachablePredecessors = new HashSet<>();
			CollectorVisitor.collectNodeSetInGenericGraph(startPoint, null, n -> {
				if (n.getNode().getInfo().getCFGInfo().getInterProceduralLeafSuccessors(n.getCallStack()).stream()
						.anyMatch(n2 -> n2.getNode() == node)) {
					// We have reached a reachable predecessor of node.
					reachablePredecessors.add(n.getNode());
					return true;
				}
				return false;
			}, n -> n.getNode().getInfo().getCFGInfo().getInterProceduralLeafSuccessors(n.getCallStack()));
			// CollectorVisitor.collectNodeSetInGenericGraph(startPoint, null, n -> {
			// if (n.getNode().getInfo().getCFGInfo()
			// .getInterTaskLeafSuccessorNodes(n.getCallStack(),
			// this.analysisDimension.getSVEDimension())
			// .stream().anyMatch(n2 -> n2.getNode() == node)) {
			// // We have reached a reachable predecessor of node.
			// reachablePredecessors.add(n.getNode());
			// return true;
			// }
			// return false;
			// }, n ->
			// n.getNode().getInfo().getCFGInfo().getInterTaskLeafSuccessorNodes(n.getCallStack(),
			// this.analysisDimension.getSVEDimension()));
			reachablePredecessorsOfSeeds.put(node, reachablePredecessors);
		}
	}

	/**
	 * Processing that is performed when no updates have been done to the program.
	 * Note that IN objects will never
	 * change -- their value may change monotonically.
	 *
	 * @param node
	 * @param stateINChanged
	 */
	@SuppressWarnings("unchecked")
	protected void processWhenNotUpdatedForDataFlow(Node node) {
		boolean propagateFurther = false;
		NodeInfo nodeInfo = node.getInfo();
		Set<IDFAEdge> predecessors = nodeInfo.getCFGInfo()
				.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension());
		F newIN = (F) nodeInfo.getIN(analysisName);
		if (newIN == null) {
			/*
			 * In this scenario, we must set the propagation flag, so that
			 * all successors are processed at least once.
			 */
			propagateFurther = true;
			if (predecessors.isEmpty()) {
				newIN = this.getEntryFact();
			} else {
				newIN = this.getTop();
			}
		}

		boolean inChanged = false;
		for (IDFAEdge idfaEdge : predecessors) {
			F predOUT = (F) idfaEdge.getNode().getInfo().getOUT(analysisName);
			if (predOUT == null) {
				continue;
			}
			F edgeOUT = this.edgeTransferFunction(predOUT, idfaEdge.getNode(), node);
			inChanged |= newIN.merge(edgeOUT, idfaEdge.getCells());
		}

		if (node instanceof PostCallNode) {
			inChanged |= processPostCallNodes((PostCallNode) node, newIN);
		}

		/**
		 * If the IN of a BarrierDirective has changed,
		 * we should add all its sibling barriers to the workList.
		 */
		if (inChanged && node instanceof BarrierDirective) {
			this.addAllSiblingBarriersToWorkList((BarrierDirective) node);
		}
		nodeInfo.setIN(analysisName, newIN);

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		F oldOUT = (F) nodeInfo.getOUT(analysisName);
		F newOUT = node.accept(this, newIN);
		if (node instanceof EndNode) {
			processEndNodes((EndNode) node, newOUT);
		} else if (node instanceof BeginNode) {
			processBeginNodes((BeginNode) node, newOUT);
		}
		nodeInfo.setOUT(analysisName, newOUT);

		// Step 3: Process the successors, if needed.
		propagateFurther |= inChanged;
		if (node instanceof BarrierDirective) {
			if (newOUT == newIN && inChanged) {
				propagateFurther = true; // Redundant; kept for readability.
			} else if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
				propagateFurther = true;
			}
		}
		if (propagateFurther) {
			if (this.analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL) {
				for (Node successor : nodeInfo.getCFGInfo().getLeafSuccessors()) {
					this.workList.add(successor);
				}
			} else {
				for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
						.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
					Node n = idfaEdge.getNode();
					this.workList.add(n);
				}
			}
		}
		return;
	}

	/**
	 * Processing that is performed when no updates have been done to the program.
	 * Note that IN objects will never
	 * change -- their value may change monotonically.
	 *
	 * @param node
	 * @param stateINChanged
	 */
	@SuppressWarnings("unchecked")
	protected void processWhenNotUpdatedForControlFlow(Node node) {
		boolean propagateFurther = false;
		NodeInfo nodeInfo = node.getInfo();
		Collection<Node> predecessors;
		if (this.analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL) {
			predecessors = nodeInfo.getCFGInfo().getLeafPredecessors();
		} else {
			predecessors = nodeInfo.getCFGInfo().getInterProceduralLeafPredecessors();
		}
		F newIN = (F) nodeInfo.getIN(analysisName);
		if (newIN == null) {
			/*
			 * In this scenario, we must set the propagation flag, so that
			 * all successors are processed at least once.
			 */
			propagateFurther = true;
			if (predecessors.isEmpty()) {
				newIN = this.getEntryFact();
			} else {
				newIN = this.getTop();
			}
		}

		boolean inChanged = false;
		for (Node predNode : predecessors) {
			F predOUT = (F) predNode.getInfo().getOUT(analysisName);
			if (predOUT == null) {
				continue;
			}
			F edgeOUT = this.edgeTransferFunction(predOUT, predNode, node);
			inChanged |= newIN.merge(edgeOUT, null);
		}

		if (node instanceof PostCallNode) {
			inChanged |= processPostCallNodes((PostCallNode) node, newIN);
		}

		/**
		 * If the IN of a BarrierDirective has changed,
		 * we should add all its sibling barriers to the workList.
		 */
		if (inChanged && node instanceof BarrierDirective) {
			this.addAllSiblingBarriersToWorkList((BarrierDirective) node);
		}
		nodeInfo.setIN(analysisName, newIN);

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		F oldOUT = (F) nodeInfo.getOUT(analysisName);
		F newOUT;
		if (this.analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL
				&& node instanceof PreCallNode) {
			newOUT = modelCallEffect((CallStatement) node.getParent(), newIN);
		} else {
			newOUT = node.accept(this, newIN);
		}
		if (node instanceof EndNode) {
			processEndNodes((EndNode) node, newOUT);
		} else if (node instanceof BeginNode) {
			processBeginNodes((BeginNode) node, newOUT);
		}
		nodeInfo.setOUT(analysisName, newOUT);

		// Step 3: Process the successors, if needed.
		propagateFurther |= inChanged;
		if (node instanceof BarrierDirective) {
			if (newOUT == newIN && inChanged) {
				propagateFurther = true; // Redundant; kept for readability.
			} else if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
				propagateFurther = true;
			}
		}
		if (propagateFurther) {
			workList.addAll(nodeInfo.getCFGInfo().getInterProceduralLeafSuccessors());
		}
		return;
	}

	protected F modelCallEffect(CallStatement callStmt, F newIN) {
		return newIN;
	}

	private void addAllSiblingBarriersToWorkList(BarrierDirective barrier) {
		for (AbstractPhase<?, ?> ph : new HashSet<>(barrier.getInfo().getNodePhaseInfo().getPhaseSet())) {
			for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
				if (!(endingPhasePoint.getNodeFromInterface() instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNodeFromInterface();
				if (siblingBarrier == barrier) {
					continue;
				}
				if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON
						&& this.analysisDimension.getSVEDimension() == SVEDimension.SVE_SENSITIVE
						&& !CoExistenceChecker.canCoExistInPhase(barrier, siblingBarrier, ph)) {
					continue;
				}
				this.workList.add(siblingBarrier);
			}
		}
	}

	/**
	 * adds missing information from the out of the corresponding precallnode to the
	 * given {@code incompleteff}, which
	 * is the final in state of the given post-call node, {@code node}.
	 *
	 * @param node
	 *                     a postCallNode.
	 * @param incompleteFF
	 */
	@SuppressWarnings("unchecked")
	private <H extends Immutable> boolean processPostCallNodes(PostCallNode node, F incompleteFF) {
		if (!(incompleteFF instanceof CellularFlowMap<?>)) {
			return false;
		}
		PreCallNode preNode = node.getParent().getPreCallNode();
		CellularFlowMap<H> postIN = (CellularFlowMap<H>) incompleteFF;
		CellularFlowMap<H> preOUT = (CellularFlowMap<H>) preNode.getInfo().getOUT(analysisName);
		if (preOUT == null || preOUT.flowMap == null) {
			return false;
		}

		Set<Cell> keysInPost = postIN.flowMap.nonGenericKeySet();
		// ExtensibleCellMap<H> tempMap = new ExtensibleCellMap<>(preOUT.flowMap);
		// for (Cell postKey : keysInPost) {
		// H val = postIN.flowMap.get(postKey);
		// assert (val != null);
		// tempMap.put(postKey, val);
		// }
		// int initialSize = (postIN.flowMap.isUniversal()) ? Integer.MAX_VALUE :
		// postIN.flowMap.size();
		// int finalSize = tempMap.isUniversal() ? Integer.MAX_VALUE : tempMap.size();
		// postIN.flowMap = tempMap;
		// if (initialSize != Integer.MAX_VALUE) {
		// if (initialSize != finalSize) {
		// return true;
		// } else {
		// return false;
		// }
		// } else {
		// return true;
		// }
		// OLD CODE BELOW: Here, we traversed over bigger keyset (assuming that
		// the number of globals is less than the number of locals).
		boolean changed = false;
		for (Cell preKey : preOUT.flowMap.nonGenericKeySet()) {
			if (!keysInPost.contains(preKey)) {
				changed = true;
				H val = preOUT.flowMap.get(preKey);
				assert (val != null);
				postIN.flowMap.put(preKey, val);
			}
		}
		return changed;
	}

	/**
	 * This method removes those entries from the flow-map {@code fullFF} that do
	 * not correspond to accessible symbols
	 * at the function which the given begin-node, {@code node}, begins.
	 *
	 * @param node
	 *               a begin-node
	 * @param fullFF
	 *               state of the OUT of the begin-node; note that this object's
	 *               values may get modified.
	 */
	private void processBeginNodes(BeginNode node, F fullFF) {
		if (!(fullFF instanceof CellularFlowMap<?>)) {
			return;
		} else {
			CellularFlowMap<?> cellFullFF = (CellularFlowMap<?>) fullFF;
			if (node.getParent() instanceof FunctionDefinition) {
				Set<Cell> removalSet = new HashSet<>();
				FunctionDefinition funcDef = (FunctionDefinition) node.getParent();
				Set<Symbol> symHere = new HashSet<>(funcDef.getInfo().getSymbolTable().values());
				symHere.addAll(Program.getRoot().getInfo().getSymbolTable().values());
				for (Cell c : cellFullFF.flowMap.nonGenericKeySet()) {
					Symbol sym = null;
					if (c instanceof Symbol) {
						sym = (Symbol) c;
					} else if (c instanceof FieldCell) {
						sym = ((FieldCell) c).getAggregateElement();
					} else if (c instanceof AddressCell) {
						Cell pointedElem = ((AddressCell) c).getPointedElement();
						if (pointedElem instanceof Symbol) {
							sym = (Symbol) pointedElem;
						}
					}
					if (sym != null && !symHere.contains(sym)) {
						removalSet.add(sym);
					}
				}
				for (Cell removeCell : removalSet) {
					cellFullFF.flowMap.remove(removeCell);
				}
				// CellSet cellsHere = node.getInfo().getAllCellsAtNode();
				// removalSet.addAll(cellFullFF.flowMap.nonGenericKeySet().stream().filter(c ->
				// !cellsHere.contains(c))
				// .collect(Collectors.toList()));
			}
		}
	}

	/**
	 * This method removes those entries from the flow-map {@code fullFF} which
	 * correspond to local keys of the scope
	 * that the given end-node, {@code node}, ends.
	 *
	 * @param node
	 *               an end-node.
	 * @param fullFF
	 *               state of OUT of the end-node; note that this object's values
	 *               may get modified.
	 */
	private void processEndNodes(EndNode node, F fullFF) {
		if (!(fullFF instanceof CellularFlowMap<?>)) {
			return;
		} else {
			CellularFlowMap<?> cellFullFF = (CellularFlowMap<?>) fullFF;
			if (node.getParent() instanceof CompoundStatement) {
				CompoundStatement parentCS = (CompoundStatement) node.getParent();
				for (Symbol key : parentCS.getInfo().getSymbolTable().values()) {
					if (!key.isStatic()) {
						cellFullFF.flowMap.remove(key);
						cellFullFF.flowMap.remove(key.getAddressCell());
						if (key.getType() instanceof ArrayType) {
							cellFullFF.flowMap.remove(key.getFieldCell());
						}
					}
				}
			} else if (node.getParent() instanceof FunctionDefinition) {
				FunctionDefinition funcDef = (FunctionDefinition) node.getParent();
				for (Symbol key : funcDef.getInfo().getSymbolTable().values()) {
					if (!key.isStatic()) {
						cellFullFF.flowMap.remove(key);
						cellFullFF.flowMap.remove(key.getAddressCell());
						if (key.getType() instanceof ArrayType) {
							cellFullFF.flowMap.remove(key.getFieldCell());
						}
					}
				}
			} else {
				return;
			}
		}
	}

	/**
	 * Processing that is performed when the program has been updated. Note that IN
	 * objects would be reinitialized for
	 * all encountered nodes.
	 *
	 * @param node
	 * @param stateINChanged
	 */
	@SuppressWarnings("unchecked")
	protected void processWhenUpdatedForDataFlow(Node node) {
		boolean first = false;
		if (!this.processedInThisUpdate.contains(node)) {
			this.processedInThisUpdate.add(node);
			first = true;
		}
		NodeInfo nodeInfo = node.getInfo();
		Set<IDFAEdge> predecessors = nodeInfo.getCFGInfo()
				.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension());
		F oldIN = (F) nodeInfo.getIN(analysisName);
		F newIN = predecessors.isEmpty() ? this.getEntryFact() : this.getTop();

		boolean anyOUTignored = false;
		for (IDFAEdge idfaEdge : predecessors) {
			if (!processedInThisUpdate.contains(idfaEdge.getNode())) {
				if (reachablePredecessorsOfSeeds.containsKey(node)) {
					if (reachablePredecessorsOfSeeds.get(node).contains(idfaEdge.getNode())) {
						yetToBeFinalized.add(node);
						anyOUTignored = true;
						continue;
					}
				} else {
					yetToBeFinalized.add(node);
					anyOUTignored = true;
					continue;
				}
			}
			F predOUT = (F) idfaEdge.getNode().getInfo().getOUT(analysisName);
			if (predOUT == null) {
				continue;
			}
			/*
			 * Ignore the "may be imprecise" OUT of all those predecessors that
			 * have not been processed since this update yet.
			 * There are two scenarios now: (i) we will encounter this node
			 * again, after updating the OUT of this predecessor upon processing
			 * it, OR (ii) the IDFA flow-facts will stabilize well before we get
			 * a chance to process this node again.
			 * In order to ensure that the final value of this flow-fact is
			 * correct even if we encounter scenario "(ii)" above, we should add
			 * this node to the list of nodes that should be processed once
			 * more, in NO-PROGRAM-UPDATE mode.
			 */
			F edgeOUT = this.edgeTransferFunction(predOUT, idfaEdge.getNode(), node);
			newIN.merge(edgeOUT, idfaEdge.getCells());
		}
		if (node instanceof PostCallNode) {
			processPostCallNodes((PostCallNode) node, newIN);
		}
		if (!anyOUTignored) {
			this.yetToBeFinalized.remove(node);
		}
		F oldOUT = (F) nodeInfo.getOUT(analysisName);
		if (newIN.isEqualTo(oldIN) && oldOUT != null && !(node instanceof BarrierDirective) && !first) {
			return;
		}
		nodeInfo.setIN(analysisName, newIN);

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		F newOUT;
		if (node instanceof BarrierDirective) {
			newOUT = this.visitChanged((BarrierDirective) node, newIN, first);
		} else {
			newOUT = node.accept(this, newIN);
			if (node instanceof EndNode) {
				processEndNodes((EndNode) node, newOUT);
			} else if (node instanceof BeginNode) {
				processBeginNodes((BeginNode) node, newOUT);
			}
		}
		nodeInfo.setOUT(analysisName, newOUT);

		// Step 3: Process the successors.
		if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
			if (this.analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL) {
				for (Node successor : nodeInfo.getCFGInfo().getLeafSuccessors()) {
					this.workList.add(successor);
				}
			} else {
				for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
						.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
					this.workList.add(idfaEdge.getNode());
				}
			}
		}
	}

	/**
	 * Processing that is performed when the program has been updated. Note that IN
	 * objects would be reinitialized for
	 * all encountered nodes.
	 *
	 * @param node
	 * @param stateINChanged
	 */
	@SuppressWarnings("unchecked")
	protected void processWhenUpdatedForControlFlow(Node node) {
		boolean first = false;
		if (!this.processedInThisUpdate.contains(node)) {
			this.processedInThisUpdate.add(node);
			first = true;
		}
		NodeInfo nodeInfo = node.getInfo();
		Collection<Node> predecessors;
		if (this.analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL) {
			predecessors = nodeInfo.getCFGInfo().getLeafPredecessors();
		} else {
			predecessors = nodeInfo.getCFGInfo().getInterProceduralLeafPredecessors();

		}
		F oldIN = (F) nodeInfo.getIN(analysisName);
		F newIN = predecessors.isEmpty() ? this.getEntryFact() : this.getTop();

		boolean anyOUTignored = false;
		for (Node predNode : predecessors) {
			if (!processedInThisUpdate.contains(predNode)) {
				if (reachablePredecessorsOfSeeds.containsKey(node)) {
					if (reachablePredecessorsOfSeeds.get(node).contains(predNode)) {
						yetToBeFinalized.add(node);
						anyOUTignored = true;
						continue;
					}
				} else {
					yetToBeFinalized.add(node);
					anyOUTignored = true;
					continue;
				}
			}
			F predOUT = (F) predNode.getInfo().getOUT(analysisName);
			if (predOUT == null) {
				continue;
			}
			/*
			 * Ignore the "may be imprecise" OUT of all those predecessors that
			 * have not been processed since this update yet.
			 * There are two scenarios now: (i) we will encounter this node
			 * again, after updating the OUT of this predecessor upon processing
			 * it, OR (ii) the IDFA flow-facts will stabilize well before we get
			 * a chance to process this node again.
			 * In order to ensure that the final value of this flow-fact is
			 * correct even if we encounter scenario "(ii)" above, we should add
			 * this node to the list of nodes that should be processed once
			 * more, in NO-PROGRAM-UPDATE mode.
			 */
			F edgeOUT = this.edgeTransferFunction(predOUT, predNode, node);
			newIN.merge(edgeOUT, null);
		}
		if (node instanceof PostCallNode) {
			processPostCallNodes((PostCallNode) node, newIN);
		}
		if (!anyOUTignored) {
			this.yetToBeFinalized.remove(node);
		}
		F oldOUT = (F) nodeInfo.getOUT(analysisName);
		if (newIN.isEqualTo(oldIN) && oldOUT != null && !(node instanceof BarrierDirective) && !first) {
			return;
		}
		nodeInfo.setIN(analysisName, newIN);

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		F newOUT;
		if (node instanceof BarrierDirective) {
			newOUT = this.visitChanged((BarrierDirective) node, newIN, first);
		} else {
			newOUT = node.accept(this, newIN);
			if (node instanceof EndNode) {
				processEndNodes((EndNode) node, newOUT);
			} else if (node instanceof BeginNode) {
				processBeginNodes((BeginNode) node, newOUT);
			}
		}
		nodeInfo.setOUT(analysisName, newOUT);

		// Step 3: Process the successors.
		if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
			this.workList.addAll(nodeInfo.getCFGInfo().getInterProceduralLeafSuccessors());
		}
	}

	/**
	 * @param node
	 */
	private void debugRecursion(Node node) {
		nodesProcessed++;
		Integer nodeProcessCount = tempMap.get(node);
		if (nodeProcessCount == null) {
			tempMap.put(node, 1);
		} else {
			tempMap.put(node, ++nodeProcessCount);
			if (nodeProcessCount > Program.thresholdIDFAProcessingCount) {
				Misc.exitDueToError(
						"At " + node + ", we exceeeded 700k iterations for IDFA analysis " + analysisName + ".");
			}
		}
	}

	/**
	 * Obtain a unique name that is used to denote this analysis.
	 *
	 * @return a unique name that is used to denote this analysis.
	 */
	public AnalysisName getAnalysisName() {
		return analysisName;
	}

	/////////////////////////////////////////////////
	// Default implementation of flow-functions below
	@Override
	public F initProcess(Node n, F flowFactIN) {
		return flowFactIN;
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )? f1 ::= Declarator() f2 ::= (
	 * DeclarationList() )? f3 ::= CompoundStatement()
	 */
	@Override
	public final F visit(FunctionDefinition n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= ParallelDirective() f2 ::= Statement()
	 */
	@Override
	public final F visit(ParallelConstruct n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= ForDirective() f2 ::= OmpForHeader() f3 ::=
	 * Statement()
	 */
	@Override
	public final F visit(ForConstruct n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <SECTIONS> f2 ::= NowaitDataClauseList() f3 ::=
	 * OmpEol() f4 ::= SectionsScope()
	 */
	@Override
	public final F visit(SectionsConstruct n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <SINGLE> f2 ::= SingleClauseList() f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public final F visit(SingleConstruct n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <TASK> f2 ::= ( TaskClause() )* f3 ::= OmpEol() f4
	 * ::= Statement()
	 */
	@Override
	public final F visit(TaskConstruct n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <PARALLEL> f2 ::= <FOR> f3 ::=
	 * UniqueParallelOrUniqueForOrDataClauseList() f4 ::=
	 * OmpEol() f5 ::= OmpForHeader() f6 ::= Statement()
	 */
	@Override
	public final F visit(ParallelForConstruct n, F flowFactIN) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <PARALLEL> f2 ::= <SECTIONS> f3 ::=
	 * UniqueParallelOrDataClauseList() f4 ::= OmpEol() f5
	 * ::= SectionsScope()
	 */
	@Override
	public final F visit(ParallelSectionsConstruct n, F flowFactIN) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <MASTER> f2 ::= OmpEol() f3 ::= Statement()
	 */
	@Override
	public final F visit(MasterConstruct n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <CRITICAL> f2 ::= ( RegionPhrase() )? f3 ::=
	 * OmpEol() f4 ::= Statement()
	 */
	@Override
	public final F visit(CriticalConstruct n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <ATOMIC> f2 ::= ( AtomicClause() )? f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public final F visit(AtomicConstruct n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <ORDERED> f2 ::= OmpEol() f3 ::= Statement()
	 */
	@Override
	public final F visit(OrderedConstruct n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= "{" f1 ::= ( CompoundStatementElement() )* f2 ::= "}"
	 */
	@Override
	public final F visit(CompoundStatement n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= <IF> f1 ::= "(" f2 ::= Expression() f3 ::= ")" f4 ::= Statement() f5
	 * ::= ( <ELSE> Statement() )?
	 */
	@Override
	public final F visit(IfStatement n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= <SWITCH> f1 ::= "(" f2 ::= Expression() f3 ::= ")" f4 ::= Statement()
	 */
	@Override
	public final F visit(SwitchStatement n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= <WHILE> f1 ::= "(" f2 ::= Expression() f3 ::= ")" f4 ::= Statement()
	 */
	@Override
	public final F visit(WhileStatement n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= <DO> f1 ::= Statement() f2 ::= <WHILE> f3 ::= "(" f4 ::= Expression()
	 * f5 ::= ")" f6 ::= ";"
	 */
	@Override
	public final F visit(DoStatement n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= <FOR> f1 ::= "(" f2 ::= ( Expression() )? f3 ::= ";" f4 ::= (
	 * Expression() )? f5 ::= ";" f6 ::= (
	 * Expression() )? f7 ::= ")" f8 ::= Statement()
	 */
	@Override
	public final F visit(ForStatement n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	@Override
	public final F visit(CallStatement n, F flowFactIN) {
		assert (false);
		return flowFactIN;
	}

	/**
	 * f0 ::= DeclarationSpecifiers() f1 ::= ( InitDeclaratorList() )? f2 ::= ";"
	 */
	@Override
	public F visit(Declaration n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= DeclarationSpecifiers() f1 ::= ParameterAbstraction()
	 */
	@Override
	public final F visit(ParameterDeclaration n, F flowFactIN) {
		if (this.analysisDimension.getContextDimension() == ContextDimension.CONTEXT_SENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available for context-sensitive analysis.", null);
		}
		if (n.toString().equals("void ")) {
			return flowFactIN;
		}
		FunctionDefinition calledFunction = Misc.getEnclosingFunction(n);
		if (calledFunction.getInfo().getFunctionName().equals("main")) {
			return flowFactIN;
		}
		int index = calledFunction.getInfo().getCFGInfo().getParameterDeclarationList().indexOf(n);
		Set<CallStatement> callers = calledFunction.getInfo().getCallersOfThis();
		if (callers.isEmpty()) {
			return flowFactIN;
		}
		F flowFactOUT = this.getTop();
		for (CallStatement callStmt : callers) {
			SimplePrimaryExpression argExp;
			try {
				argExp = callStmt.getPreCallNode().getArgumentList().get(index);
			} catch (Exception e) {
				continue;
			}
			F tempFlowFact = writeToParameter(n, argExp, flowFactIN);
			flowFactOUT.merge(tempFlowFact, null);
		}
		return flowFactOUT;
	}

	/**
	 * Given a parameter-declaration {@code parameter}, and a
	 * simple-primary-expression {@code argument} that represents
	 * the argument for this parameter from some call-site, this method should model
	 * the flow-function of the write to
	 * the parameter that happens implicitly.
	 *
	 * @param parameter
	 *                   a {@code ParameterDeclaration} which needs to be assigned
	 *                   with the {@code argument}.
	 * @param argument
	 *                   a {@code SimplePrimaryExpression} which is assigned to the
	 *                   {@code parameter}.
	 * @param flowFactIN
	 *                   the IN flow-fact for the implicit assignment of the
	 *                   {@code argument} to the {@code parameter}.
	 *
	 * @return the OUT flow-fact, as a result of the implicit assignment of the
	 *         {@code argument} to the {@code
	 * parameter}.
	 */
	public abstract F writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument, F flowFactIN);

	/**
	 * f0 ::= "#" f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public F visit(UnknownCpp n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= "#" f1 ::= <PRAGMA> f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public F visit(UnknownPragma n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <IDENTIFIER> f1 ::= "=" f2 ::= Expression()
	 */
	@Override
	public F visit(OmpForInitExpression n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpForLTCondition() | OmpForLECondition() | OmpForGTCondition() |
	 * OmpForGECondition()
	 */
	@Override
	public F visit(OmpForCondition n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= PostIncrementId() | PostDecrementId() | PreIncrementId() |
	 * PreDecrementId() | ShortAssignPlus() |
	 * ShortAssignMinus() | OmpForAdditive() | OmpForSubtractive() |
	 * OmpForMultiplicative()
	 */
	@Override
	public F visit(OmpForReinitExpression n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <FLUSH> f2 ::= ( FlushVars() )? f3 ::= OmpEol()
	 */
	@Override
	public F visit(FlushDirective n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	@Override
	public F visit(DummyFlushDirective n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	@SuppressWarnings("unchecked")
	public final F visitChanged(BarrierDirective n, F flowFactIN, final boolean first) {
		if (this.flowNature == AnalysisFlowNature.CONTROLFLOW) {
			return flowFactIN;
		}
		boolean changed = false;
		F flowFactOUT = this.getTop();
		if (!first) {
			F oldFactOUT = (F) n.getInfo().getOUT(analysisName);
			if (oldFactOUT != null) {
				flowFactOUT.merge(oldFactOUT, null);
			}
		}
		boolean anyOUTignored = false;
		for (AbstractPhase<?, ?> ph : new HashSet<>(n.getInfo().getNodePhaseInfo().getPhaseSet())) {
			// for (PhasePoint endingPhasePoint : ph.getUnmodifiableEndPoints()) {}
			for (AbstractPhasePointable endingPhasePoint : new HashSet<>(ph.getEndPoints())) {
				if (!(endingPhasePoint.getNodeFromInterface() instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNodeFromInterface();
				// if (!processedInThisUpdate.contains(siblingBarrier)) {
				// yetToBeFinalized.add(n);
				// continue;
				// }
				if (!processedInThisUpdate.contains(siblingBarrier)) {
					if (reachablePredecessorsOfSeeds.containsKey(n)) {
						if (reachablePredecessorsOfSeeds.get(n).contains(siblingBarrier)) {
							yetToBeFinalized.add(n);
							anyOUTignored = true;
							continue;
						}
					} else {
						yetToBeFinalized.add(n);
						anyOUTignored = true;
						continue;
					}
				}
				F siblingFlowFactIN = (F) siblingBarrier.getInfo().getIN(analysisName);
				if (siblingFlowFactIN == null) {
					continue;
				}
				if (siblingBarrier == n) {
					siblingFlowFactIN = flowFactIN;
					changed = changed | flowFactOUT.merge(siblingFlowFactIN, null);
				} else {
					if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON
							&& this.analysisDimension.getSVEDimension() == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInPhase(n, siblingBarrier, ph)) {
						continue;
					}
					changed = changed | flowFactOUT.merge(siblingFlowFactIN, n.getInfo().getSharedCellsAtNode());
				}
			}
		}
		if (!anyOUTignored) {
			this.yetToBeFinalized.remove(n);
		}
		if (changed && first) {
			CellSet myShared = n.getInfo().getSharedCellsAtNode();
			for (AbstractPhase<?, ?> ph : new HashSet<>(n.getInfo().getNodePhaseInfo().getPhaseSet())) {
				for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
					if (!(endingPhasePoint.getNodeFromInterface() instanceof BarrierDirective)) {
						continue;
					}
					BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNodeFromInterface();
					if (siblingBarrier == n) {
						continue;
					}
					if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON
							&& this.analysisDimension.getSVEDimension() == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInPhase(n, siblingBarrier, ph)) {
						continue;
					}
					F siblingFlowOUT = (F) siblingBarrier.getInfo().getOUT(analysisName);
					if (siblingFlowOUT == null) {
						continue;
					}
					CellSet siblingShared = siblingBarrier.getInfo().getSharedCellsAtNode();
					CellSet sharedShared = Misc.setIntersection(myShared, siblingShared);
					F myTemp = this.getTop();
					myTemp.merge(flowFactOUT, sharedShared);
					F tempOUT = this.getTop();
					tempOUT.merge(siblingFlowOUT, sharedShared);
					if (myTemp.isEqualTo(tempOUT)) {
						continue;
					}
					this.workList.add(siblingBarrier);
				}
			}
		}
		if (changed && !first) {
			for (AbstractPhase<?, ?> ph : new HashSet<>(n.getInfo().getNodePhaseInfo().getPhaseSet())) {
				for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
					if (!(endingPhasePoint.getNodeFromInterface() instanceof BarrierDirective)) {
						continue;
					}
					BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNodeFromInterface();
					if (siblingBarrier == n) {
						continue;
					}
					if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON
							&& this.analysisDimension.getSVEDimension() == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInPhase(n, siblingBarrier, ph)) {
						continue;
					}
					this.workList.add(siblingBarrier);
				}
			}
		}
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <BARRIER> f2 ::= OmpEol()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final F visit(BarrierDirective n, F flowFactIN) {
		if (this.flowNature == AnalysisFlowNature.CONTROLFLOW) {
			return flowFactIN;
		}
		// boolean changed = false;
		F flowFactOUT = this.getTop();
		////

		F oldFactOUT = (F) n.getInfo().getOUT(analysisName);
		if (oldFactOUT != null) {
			flowFactOUT.merge(oldFactOUT, null);
		}

		///
		for (AbstractPhase<?, ?> ph : new HashSet<>(n.getInfo().getNodePhaseInfo().getPhaseSet())) {
			for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
				if (!(endingPhasePoint.getNodeFromInterface() instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNodeFromInterface();
				F siblingFlowFactIN = (F) siblingBarrier.getInfo().getIN(analysisName);
				if (siblingFlowFactIN == null) {
					continue;
				}
				if (siblingBarrier == n) {
					siblingFlowFactIN = flowFactIN;
					// changed = changed | flowFactOUT.merge(siblingFlowFactIN, null);
					flowFactOUT.merge(siblingFlowFactIN, null);
				} else {
					if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON
							&& this.analysisDimension.getSVEDimension() == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInPhase(n, siblingBarrier, ph)) {
						continue;
					}
					// changed = changed | flowFactOUT.merge(siblingFlowFactIN,
					// n.getInfo().getSharedCellsAtNode());
					flowFactOUT.merge(siblingFlowFactIN, n.getInfo().getSharedCellsAtNode());
				}
			}
		}
		// OLD CODE: Now, instead of adding sibling barriers upon a changing OUT,
		// we do so when the IN of a BarrierDirective changes.
		// if (changed) {
		// for (Phase ph : new
		// HashSet<>(n.getInfo().getNodePhaseInfo().getStablePhaseSet())) {
		// for (PhasePoint endingPhasePoint : ph.getStableEndPoints()) {
		// if (!(endingPhasePoint.getNode() instanceof BarrierDirective)) {
		// continue;
		// }
		// BarrierDirective siblingBarrier = (BarrierDirective)
		// endingPhasePoint.getNode();
		// if (siblingBarrier == n) {
		// continue;
		// }
		// if (this.analysisDimension.getSVEDimension() == SVEDimension.SVE_SENSITIVE
		// && !SVEChecker.canCoExistInPhase(n, siblingBarrier, ph)) {
		// continue;
		// }
		// this.workList.add(siblingBarrier);
		// }
		// }
		// }
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <TASKWAIT> f2 ::= OmpEol()
	 */
	@Override
	public F visit(TaskwaitDirective n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <TASKYIELD> f2 ::= OmpEol()
	 */
	@Override
	public F visit(TaskyieldDirective n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= ( Expression() )? f1 ::= ";"
	 */
	@Override
	public F visit(ExpressionStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <GOTO> f1 ::= <IDENTIFIER> f2 ::= ";"
	 */
	@Override
	public F visit(GotoStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <CONTINUE> f1 ::= ";"
	 */
	@Override
	public F visit(ContinueStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <BREAK> f1 ::= ";"
	 */
	@Override
	public F visit(BreakStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <RETURN> f1 ::= ( Expression() )? f2 ::= ";"
	 */
	@Override
	public F visit(ReturnStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= AssignmentExpression() f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public F visit(Expression n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <IF> f1 ::= "(" f2 ::= Expression() f3 ::= ")"
	 */
	@Override
	public F visit(IfClause n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <NUM_THREADS> f1 ::= "(" f2 ::= Expression() f3 ::= ")"
	 */
	@Override
	public F visit(NumThreadsClause n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <FINAL> f1 ::= "(" f2 ::= Expression() f3 ::= ")"
	 */
	@Override
	public F visit(FinalClause n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(BeginNode n, F flowFactIN) {
		// return flowFactIN;
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(EndNode n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(PreCallNode n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(PostCallNode n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * Obtain a set of successor nodes for this {@code node}, along with symbols via
	 * which information may flow on the
	 * edge connecting the successors to this {@code node}.
	 *
	 * @param node
	 *             node whose successors have to be found.
	 *
	 * @return a set of successor nodes for this {@code node}, along with symbols
	 *         via which information may flow on the
	 *         edge connecting the successors to this {@code node}.
	 *
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("unused")
	private Set<IDFAEdge> getInterTaskLeafSuccessors(Node node) {
		assert (Misc.isCFGLeafNode(node));
		if (this.analysisDimension.getFlowDimension() == FlowDimension.FLOW_INSENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for flow-insensitive analyses.", null);
			return null;
		}
		if (this.analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for intra-procedural analyses.", null);
			return null;
		} else {
			if (this.analysisDimension.getContextDimension() == ContextDimension.CONTEXT_SENSITIVE) {
				Misc.warnDueToLackOfFeature("No generic pass available yet for context-sensitive analyses.", null);
				return null;
			}
		}
		return node.getInfo().getCFGInfo().getInterTaskLeafSuccessorEdges(analysisDimension.getSVEDimension());
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Deprecated
	private boolean isHarmful(Set<Node> computeSetParam) {
		for (Node n : computeSetParam) {
			/*
			 * Step 1: Obtain the meet of OUT of all current predecessors of n.
			 * Step 2: Compare it with current IN of n.
			 * Step 3: If OLD-IN(n) = MEET(OLD-OUT(p)), for all predecessors of
			 * n, then, n will not get polluted value.
			 */
			F predOUTMeet = this.getTop();
			for (IDFAEdge predEdge : this.getInterTaskLeafPredecessors(n)) {
				F predOUT = (F) predEdge.getNode().getInfo().getOUT(analysisName);
				predOUTMeet.merge(predOUT, predEdge.getCells());
			}
			F nOldIN = (F) n.getInfo().getIN(analysisName);
			predOUTMeet.merge(nOldIN, null);
			if (!predOUTMeet.isEqualTo(nOldIN)) {
				return true;
			}

		}
		return false;
	}

	/**
	 * Obtain a set of predecessor nodes for this {@code node}, along with symbols
	 * via which information may flow on the
	 * edge connecting the predecessors to this {@code node}.
	 *
	 * @param node
	 *             node whose predecessors have to be found.
	 *
	 * @return a set of predecessor nodes for this {@code node}, along with symbols
	 *         via which information may flow on
	 *         the edge connecting the predecessors to this {@code node}.
	 *
	 * @deprecated
	 */
	@Deprecated
	private Set<IDFAEdge> getInterTaskLeafPredecessors(Node node) {
		// if (!Misc.isCFGLeafNode(node)) {
		// System.err.println(node.getClass().getSimpleName());
		// System.err.println(node);
		// }
		assert (Misc.isCFGLeafNode(node));
		if (this.analysisDimension.getFlowDimension() == FlowDimension.FLOW_INSENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for flow-insensitive analyses.", null);
			return null;
		}
		if (this.analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for intra-procedural analyses.", null);
			return null;
		} else {
			if (this.analysisDimension.getContextDimension() == ContextDimension.CONTEXT_SENSITIVE) {
				Misc.warnDueToLackOfFeature("No generic pass available yet for context-sensitive analyses.", null);
				return null;
			}
		}
		return node.getInfo().getCFGInfo().getInterTaskLeafPredecessorEdges(analysisDimension.getSVEDimension());
	}

	//
	// /**
	// * Perform the analysis, starting at the begin node of the function
	// * {@code funcDef}.<br>
	// * <i>Note that this method does not clear the analysis information, if
	// * already present.</i>
	// *
	// * @param funcDef
	// * function-definition on which this analysis has to be
	// * performed.
	// * @param fuseSet
	// * set of nodes for which flow-function has to be considered to
	// * be an identity function.
	// */
	// @Deprecated
	// public void runButFuse(FunctionDefinition funcDef, Set<Node> fuseSet) {
	// this.fuseSet = fuseSet;
	// BeginNode beginNode =
	// funcDef.getInfo().getCFGInfo().getNestedCFG().getBegin();
	// this.computeSet.clear();
	// this.computeSet.add(beginNode);
	// this.processForwardIteratively();
	// // this.processNodeForward(beginNode);
	// }
	//
	// /**
	// * Start this analysis from all the nodes of {@code computeSet}.
	// * Computation of these nodes may trigger computations of other nodes as
	// * well.<br>
	// * <i>Note that this method does not clear the analysis information, if
	// * already present.</i>
	// *
	// * @param computeSet
	// * a set of nodes which must be (re)analyzed.
	// * @param fuseSet
	// * set of nodes for which flow-function has to be considered to
	// * be an identity function.
	// */
	// @Deprecated
	// public void runButFuse(Set<Node> computeSet, Set<Node> fuseSet) {
	// this.fuseSet = fuseSet;
	// this.computeSet = computeSet;
	// this.processForwardIteratively();
	// // for (Node node : computeSet) {
	// // this.processNodeForward(node);
	// // }
	// }
	//
	// /**
	// * Perform this analysis on all the functions.<br>
	// * <i>Note that this method does not clear the analysis information, if
	// * already present.</i>
	// *
	// * @param fuseSet
	// * set of nodes for which flow-function has to be considered to
	// * be an identity function.
	// */
	// @Deprecated
	// public void runButFuse(Set<Node> fuseSet) {
	// if (this.analysisDimension.getProceduralDimension() ==
	// ProceduralDimension.INTRA_PROCEDURAL) {
	// for (Node funcDefNode : Misc.getInheritedEnclosee(Main.root,
	// FunctionDefinition.class)) {
	// this.runButFuse((FunctionDefinition) funcDefNode, fuseSet);
	// }
	// } else {
	// this.computeSet.clear();
	// this.computeSet.add(Main.root.getInfo().getMainFunction().getInfo().getCFGInfo().getNestedCFG().getBegin());
	// this.runButFuse(computeSet, fuseSet);
	// }
	// }
}
