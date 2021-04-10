/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.GJDepthFirstProcess;
import imop.lib.analysis.flowanalysis.SCC;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis.PointsToGlobalState;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.ContextDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.FieldDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.FlowDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.PathDimension;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.CellSet;
import imop.lib.util.CollectorVisitor;
import imop.lib.util.Misc;
import imop.parser.Program;
import imop.parser.Program.UpdateCategory;

import java.util.*;

/**
 * Superclass of all fixed-point based data/control flow analyses.
 *
 * @author Aman Nougrahiya
 */
public abstract class FlowAnalysis<F extends FlowAnalysis.FlowFact> extends GJDepthFirstProcess<F, F> {

	public FlowAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
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
	}

	List<Node> last20Nodes;

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

	public static void resetStaticFields() {
		FlowAnalysis.analysisSet = new HashMap<>();
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
	public final void storeNodesToBeUpdated(Set<Node> updateSeedSet) {
		if (!this.isInvalid) {
			/*
			 * The state so far was correct. Set the invalid bit, clear the
			 * nodesToBeUpdated set, and add updateSeedSet to them.
			 */
			if (Program.updateCategory == UpdateCategory.LZINV || Program.updateCategory == UpdateCategory.LZUPD
					|| Program.updateCategory == UpdateCategory.CPINV
					|| Program.updateCategory == UpdateCategory.CPUPD) {
				this.isInvalid = true;
			}
			// nodesToBeUpdated.clear();
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
		assert (!SCC.processingTarjan);
		this.autoUpdateTriggerCounter++;
		long localTimer = System.nanoTime();

		// /*
		// * Step 0: Collect information about reachable predecessor for seed
		// * nodes.
		// */
		// this.populateReachablePredecessorMap();
		/*
		 * Step 1: Start processing from the leaf elements of updateSeedSet in
		 * "update" mode.
		 */
		this.workList.recreate();
		for (Node n : nodesToBeUpdated) {
			if (n.getInfo().isConnectedToProgram()) {
				this.workList.add(n);
			}
		}
		// this.workList.addAll(nodesToBeUpdated);
		this.nodesToBeUpdated.clear();
		this.processedInThisUpdate = new HashSet<>();
		this.yetToBeFinalized = new HashSet<>();
		while (!workList.isEmpty()) {
			this.nodesProcessedDuringUpdate++;
			Node nodeToBeAnalysed = this.workList.removeFirstElement();
			this.debugRecursion(nodeToBeAnalysed);
			this.processWhenUpdated(nodeToBeAnalysed);
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
			this.processWhenNotUpdated(nodeToBeAnalysed);
		}
		localTimer = System.nanoTime() - localTimer;
		this.flowAnalysisUpdateTimer += localTimer;
	}

	/**
	 * This method starts with the given node, and stabilizes the SCC corresponding
	 * to it before returning back.
	 *
	 * @param node
	 *             node whose SCC has to be stabilized. Note that the node has
	 *             already been removed from the workList.
	 */
	protected void stabilizeSCCOf(Node node) {
		this.processedInThisUpdate.clear();
		this.yetToBeFinalized.clear();

		SCC thisSCC = node.getInfo().getCFGInfo().getSCC();
		int thisSCCNum = node.getInfo().getCFGInfo().getSCCRPOIndex();
		// System.err.println("*** PROCESSING SCC #" + thisSCCNum);

		assert (thisSCC != null);
		do {
			this.nodesProcessedDuringUpdate++;
			this.debugRecursion(node);
			this.processWhenUpdated(node);
			node = this.workList.peekFirstElementOfSameSCC(thisSCCNum);
			if (node == null) {
				break;
			}
			SCC nextSCC = node.getInfo().getCFGInfo().getSCC();
			if (nextSCC == null || nextSCC != thisSCC) {
				// The next node belongs to a different SCC. Break from the first phase.
				break;
			} else {
				// Extract the next node and process it next.
				node = this.workList.removeFirstElementOfSameSCC(thisSCCNum);
			}
		} while (true);
		// Now, the set processedInThisUpdate is not required anymore. Clear it.
		this.processedInThisUpdate.clear();
		/*
		 * Next, let's start with the second phase, with all elements of
		 * yetToBeFinalized in the workList, where we proceed as usual.
		 */
		this.workList.addAll(this.yetToBeFinalized);
		this.yetToBeFinalized.clear();
		do {
			node = this.workList.peekFirstElementOfSameSCC(thisSCCNum);
			if (node == null) {
				break;
			}
			SCC nextSCC = node.getInfo().getCFGInfo().getSCC();
			if (nextSCC == null || nextSCC != thisSCC) {
				// The next node belongs to a different SCC. Break from the second phase.
				break;
			} else {
				// Extract the next node and process it in the second phase.
				node = this.workList.removeFirstElementOfSameSCC(thisSCCNum);
			}
			this.nodesProcessedDuringUpdate++;
			this.debugRecursion(node);
			this.processWhenNotUpdated(node); // Note that this is a call to normal processing.
		} while (true);

		// if (node != null) {
		// int nextSCCNum = node.getInfo().getCFGInfo().getSCCRPOIndex();
		// System.err.println("*** ENCOUNTERED NEXT SCC with ID #" + nextSCCNum);
		// }
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
	public abstract void cloneFlowFactsToAllWithin(Node affectedNode);

	public boolean hasUnprocessedNodes() {
		return !this.nodesToBeUpdated.isEmpty() || !this.workList.isEmpty();
	}

	protected abstract void processWhenNotUpdated(Node node);

	protected abstract void processWhenUpdated(Node node);

	/**
	 * Super-class for all the specializations of {@code FlowFact}s, which are used
	 * to represent the class of data-flow
	 * facts obtained at the end of a given IDFA.
	 *
	 * @author aman
	 */
	public static abstract class FlowFact {
		/**
		 * Returns true if {@code other} is semantically same as this object.
		 *
		 * <i>Note that we do not use equals() methods, since we want to enforce
		 * the subclasses of {@code FlowFact} to define this equality.</i>
		 *
		 * @param other
		 *              the object to be checked for equality with this object.
		 *
		 * @return true, if <code>other</code> is same as this object.
		 */
		public abstract boolean isEqualTo(final FlowFact other);

		/**
		 * Prints the information represented by this object in a human-readable form.
		 *
		 * @return a String that represents the human-readable form of this FlowFact.
		 */
		public abstract String getString();

		/**
		 * Given an instance of a FlowFact {@link other}, this method should be
		 * overridden to update the receiver such
		 * that it is equal to the meet of the initial state of the receiver with
		 * {@link other}.
		 * <p>
		 * An implementation can use the set of cells, {@link cellSet}, to make the meet
		 * more precise. The set {@link
		 * cellSet} should contain all those cells which may contribute to the update in
		 * data-flow fact of a node.
		 * <p>
		 * <i>Note: Make sure that this method should not change the internal
		 * state of {@link other}.</i>
		 *
		 * @param other
		 *                one of the flowFacts. Can be {@code null}.
		 * @param cellSet
		 *                set of cells that may affect the data-flow fact. <br>
		 *                <ul>
		 *                <li><i>Note 1: An empty set should be treated as a
		 *                universal
		 *                set of all the cells available in this context.</i></li>
		 *                <li><i>Note 2: A set with just a singleton object
		 *                {@link Symbol.getPhantomSharedSymbol()} should be treated
		 *                as
		 *                the set of all the shared objects available in this
		 *                context.</i></li>
		 *                </ul>
		 *
		 * @return true if this flow-fact has been updated as a result of this meet
		 *         operation.
		 */
		public abstract boolean merge(final FlowFact other, CellSet cellSet);
		//
		// /**
		// * Obtain a copy for this flow-fact, which can be updated independently of
		// * the receiver.
		// *
		// * @return
		// * a copy of the this flow-fact.
		// */
		// public abstract FlowFact getCopy();
	}

	/**
	 * Name (unique-ID) to denote this analysis.
	 */
	protected AnalysisName analysisName;

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
	protected AnalysisDimension analysisDimension;

	/**
	 * A set of nodes with which we need to perform this analysis. <br>
	 * <i>Note that the processing of these nodes may recursively process the
	 * successor nodes as well.</i>
	 */
	protected ReversePostOrderWorkList workList = new ReversePostOrderWorkList();

	public long nodesProcessed = 0;
	// public long localCount = -1;
	// public List<Long> localList = new ArrayList<>();

	public HashMap<Node, Integer> tempMap = new HashMap<>();

	/**
	 * A map from an analysis name to the analysis object.
	 */
	protected static Map<AnalysisName, FlowAnalysis<?>> analysisSet = new HashMap<>();

	protected boolean isInvalid = false;
	public long nodesProcessedDuringUpdate = 0;
	public int autoUpdateTriggerCounter = 0;
	public long flowAnalysisUpdateTimer = 0;
	public Set<Node> nodesToBeUpdated = new HashSet<>();
	protected Set<Node> processedInThisUpdate = new HashSet<>();
	protected Set<Node> yetToBeFinalized = new HashSet<>();
	protected HashMap<Node, Set<Node>> reachablePredecessorsOfSeeds = new HashMap<>();

	/**
	 * Depending upon the nature of the analysis, this method should implement the
	 * code that invokes appropriate methods
	 * to run the flow analysis, given a function definition. NOTE: The definitions
	 * of this method would already be
	 * provided in specific subclasses. Client analyses should not override this
	 * method.
	 *
	 * @param funcDef
	 */
	public abstract void run(FunctionDefinition funcDef);

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
	 * Obtain a unique name that is used to denote this analysis.
	 *
	 * @return a unique name that is used to denote this analysis.
	 */
	public final AnalysisName getAnalysisName() {
		return analysisName;
	}

	/**
	 * Set of all the analyses run so far.
	 *
	 * @return Set of all the analyses run so far.
	 */
	public static Map<AnalysisName, FlowAnalysis<?>> getAllAnalyses() {
		return analysisSet;
	}

	/**
	 * This method is usually called by getIN and getOUT methods to check if
	 * flow-facts of various nodes for this
	 * analysis reflect the state of the program.
	 *
	 * @return true, if the state maybe invalid.
	 */
	public final boolean stateIsInvalid() {
		return isInvalid;
	}

	/**
	 * After deciding to run the stabilizing pass (by calling
	 * {@link #restartAnalysisFromStoredNodes()}), this method is
	 * called first, so that the stabilizing pass itself shouldn't go into a
	 * recursion were it to use the getIN and
	 * getOUT on the same analysis (e.g., in case of points-to analysis).
	 */
	public final void markStateToBeValid() {
		this.isInvalid = false;
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
	protected F edgeTransferFunction(F edgeOne, Node predecessor, Node successor) {
		return edgeOne;
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
	@Deprecated
	protected final void populateReachablePredecessorMap() {
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
	 * @param node
	 */
	protected final void debugRecursion(Node node) {
		nodesProcessed++;
		// localCount++;
		Integer nodeProcessCount = tempMap.get(node);
		if (nodeProcessCount == null) {
			tempMap.put(node, 1);
		} else {
			tempMap.put(node, ++nodeProcessCount);
			if (nodeProcessCount > Program.thresholdIDFAProcessingCount) {
				if (last20Nodes == null) {
					last20Nodes = new ArrayList<>();
					last20Nodes.add(node);
				} else if (last20Nodes.size() < 20) {
					last20Nodes.add(node);
				} else {
					int i = 0;
					for (Node n : last20Nodes) {
						i++;
						System.out.println(
								"Node #" + i + n + " with phases: " + n.getInfo().getNodePhaseInfo().getPhaseSet()
										+ n.getInfo().getIN(this.analysisName).getString());
					}
					Misc.exitDueToError("At " + node + ", we exceeeded " + Program.thresholdIDFAProcessingCount
							+ " iterations for IDFA analysis " + analysisName + ".");
				}
			}
		}
	}

	/////////////////////////////////////////////////
	// Default implementation of flow-functions below
	@Override
	public F initProcess(Node n, F flowFactOne) {
		return flowFactOne;
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )? f1 ::= Declarator() f2 ::= (
	 * DeclarationList() )? f3 ::= CompoundStatement()
	 */
	@Override
	public final F visit(FunctionDefinition n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= ParallelDirective() f2 ::= Statement()
	 */
	@Override
	public final F visit(ParallelConstruct n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= ForDirective() f2 ::= OmpForHeader() f3 ::=
	 * Statement()
	 */
	@Override
	public final F visit(ForConstruct n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <SECTIONS> f2 ::= NowaitDataClauseList() f3 ::=
	 * OmpEol() f4 ::= SectionsScope()
	 */
	@Override
	public final F visit(SectionsConstruct n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <SOneGLE> f2 ::= SingleClauseList() f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public final F visit(SingleConstruct n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <TASK> f2 ::= ( TaskClause() )* f3 ::= OmpEol() f4
	 * ::= Statement()
	 */
	@Override
	public final F visit(TaskConstruct n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <PARALLEL> f2 ::= <FOR> f3 ::=
	 * UniqueParallelOrUniqueForOrDataClauseList() f4 ::=
	 * OmpEol() f5 ::= OmpForHeader() f6 ::= Statement()
	 */
	@Override
	public final F visit(ParallelForConstruct n, F flowFactOne) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <PARALLEL> f2 ::= <SECTIONS> f3 ::=
	 * UniqueParallelOrDataClauseList() f4 ::= OmpEol() f5
	 * ::= SectionsScope()
	 */
	@Override
	public final F visit(ParallelSectionsConstruct n, F flowFactOne) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <MASTER> f2 ::= OmpEol() f3 ::= Statement()
	 */
	@Override
	public final F visit(MasterConstruct n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <CRITICAL> f2 ::= ( RegionPhrase() )? f3 ::=
	 * OmpEol() f4 ::= Statement()
	 */
	@Override
	public final F visit(CriticalConstruct n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <ATOMIC> f2 ::= ( AtomicClause() )? f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public final F visit(AtomicConstruct n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <ORDERED> f2 ::= OmpEol() f3 ::= Statement()
	 */
	@Override
	public final F visit(OrderedConstruct n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= "{" f1 ::= ( CompoundStatementElement() )* f2 ::= "}"
	 */
	@Override
	public final F visit(CompoundStatement n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= <IF> f1 ::= "(" f2 ::= Expression() f3 ::= ")" f4 ::= Statement() f5
	 * ::= ( <ELSE> Statement() )?
	 */
	@Override
	public final F visit(IfStatement n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= <SWITCH> f1 ::= "(" f2 ::= Expression() f3 ::= ")" f4 ::= Statement()
	 */
	@Override
	public final F visit(SwitchStatement n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= <WHILE> f1 ::= "(" f2 ::= Expression() f3 ::= ")" f4 ::= Statement()
	 */
	@Override
	public final F visit(WhileStatement n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= <DO> f1 ::= Statement() f2 ::= <WHILE> f3 ::= "(" f4 ::= Expression()
	 * f5 ::= ")" f6 ::= ";"
	 */
	@Override
	public final F visit(DoStatement n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= <FOR> f1 ::= "(" f2 ::= ( Expression() )? f3 ::= ";" f4 ::= (
	 * Expression() )? f5 ::= ";" f6 ::= (
	 * Expression() )? f7 ::= ")" f8 ::= Statement()
	 */
	@Override
	public final F visit(ForStatement n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	@Override
	public final F visit(CallStatement n, F flowFactOne) {
		assert (false);
		return flowFactOne;
	}

	/**
	 * f0 ::= DeclarationSpecifiers() f1 ::= ( InitDeclaratorList() )? f2 ::= ";"
	 */
	@Override
	public F visit(Declaration n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= DeclarationSpecifiers() f1 ::= ParameterAbstraction()
	 */
	@Override
	public F visit(ParameterDeclaration n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= "#" f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public F visit(UnknownCpp n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= "#" f1 ::= <PRAGMA> f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public F visit(UnknownPragma n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= <IDENTIFIER> f1 ::= "=" f2 ::= Expression()
	 */
	@Override
	public F visit(OmpForInitExpression n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= OmpForLTCondition() | OmpForLECondition() | OmpForGTCondition() |
	 * OmpForGECondition()
	 */
	@Override
	public F visit(OmpForCondition n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= PostIncrementId() | PostDecrementId() | PreIncrementId() |
	 * PreDecrementId() | ShortAssignPlus() |
	 * ShortAssignMinus() | OmpForAdditive() | OmpForSubtractive() |
	 * OmpForMultiplicative()
	 */
	@Override
	public F visit(OmpForReinitExpression n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <FLUSH> f2 ::= ( FlushVars() )? f3 ::= OmpEol()
	 */
	@Override
	public F visit(FlushDirective n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	@Override
	public F visit(DummyFlushDirective n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <BARRIER> f2 ::= OmpEol()
	 */
	@Override
	public F visit(BarrierDirective n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <TASKWAIT> f2 ::= OmpEol()
	 */
	@Override
	public F visit(TaskwaitDirective n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <TASKYIELD> f2 ::= OmpEol()
	 */
	@Override
	public F visit(TaskyieldDirective n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= ( Expression() )? f1 ::= ";"
	 */
	@Override
	public F visit(ExpressionStatement n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= <GOTO> f1 ::= <IDENTIFIER> f2 ::= ";"
	 */
	@Override
	public F visit(GotoStatement n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= <CONTOneUE> f1 ::= ";"
	 */
	@Override
	public F visit(ContinueStatement n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= <BREAK> f1 ::= ";"
	 */
	@Override
	public F visit(BreakStatement n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= <RETURN> f1 ::= ( Expression() )? f2 ::= ";"
	 */
	@Override
	public F visit(ReturnStatement n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= AssignmentExpression() f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public F visit(Expression n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= <IF> f1 ::= "(" f2 ::= Expression() f3 ::= ")"
	 */
	@Override
	public F visit(IfClause n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= <NUM_THREADS> f1 ::= "(" f2 ::= Expression() f3 ::= ")"
	 */
	@Override
	public F visit(NumThreadsClause n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * f0 ::= <FOneAL> f1 ::= "(" f2 ::= Expression() f3 ::= ")"
	 */
	@Override
	public F visit(FinalClause n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(BeginNode n, F flowFactOne) {
		// return flowFactOne;
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(EndNode n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(PreCallNode n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(PostCallNode n, F flowFactOne) {
		F flowFactTwo;
		flowFactTwo = initProcess(n, flowFactOne);
		return flowFactTwo;
	}

}
