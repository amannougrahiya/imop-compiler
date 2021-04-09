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
import imop.lib.analysis.flowanalysis.SCC;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class InterProceduralControlFlowAnalysis<F extends FlowAnalysis.FlowFact>
		extends ControlFlowAnalysis<F> {

	public InterProceduralControlFlowAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
		super(analysisName, analysisDimension);
	}

	protected Set<FunctionDefinition> functionWithBarrier = new HashSet<>();

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
	@Override
	public final void run(FunctionDefinition funcDef) {
		if (this.analysisName == AnalysisName.PREDICATE_ANALYSIS) {
			functionWithBarrier.clear();
			for (FunctionDefinition foo : Misc.getExactEnclosee(Program.getRoot(), FunctionDefinition.class)) {
				if (foo.getInfo().hasBarrierInCFG()) {
					functionWithBarrier.add(foo);
				}
			}
		}
		BeginNode beginNode = funcDef.getInfo().getCFGInfo().getNestedCFG().getBegin();
		this.workList.recreate();
		this.workList.add(beginNode);
		do {
			Node nodeToBeAnalysed = this.workList.removeFirstElement();
			this.debugRecursion(nodeToBeAnalysed);
			this.processWhenNotUpdated(nodeToBeAnalysed);
		} while (!workList.isEmpty());
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
	@Override
	public final void cloneFlowFactsToAllWithin(Node affectedNode) {
		Node node;
		if (Misc.isCFGLeafNode(affectedNode)) {
			node = affectedNode;
		} else {
			node = affectedNode.getInfo().getCFGInfo().getNestedCFG().getBegin();
		}

		NodeInfo nodeInfo = node.getInfo();
		Collection<Node> predecessors = nodeInfo.getCFGInfo().getInterProceduralLeafPredecessors();
		F ffForAll = predecessors.isEmpty() ? this.getEntryFact() : this.getTop();

		for (Node predNode : predecessors) {
			@SuppressWarnings("unchecked")
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
	 * Processing that is performed when no updates have been done to the program.
	 * Note that IN objects will never
	 * change -- their value may change monotonically.
	 *
	 * @param node
	 * @param stateINChanged
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected final void processWhenNotUpdated(Node node) {
		boolean propagateFurther = false;
		NodeInfo nodeInfo = node.getInfo();
		Collection<Node> predecessors = nodeInfo.getCFGInfo().getInterProceduralLeafPredecessors();
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
		if (this.analysisName == AnalysisName.PREDICATE_ANALYSIS && node instanceof PostCallNode) {
			// A small hack here.
			PostCallNode postNode = (PostCallNode) node;
			List<FunctionDefinition> funcDef = postNode.getParent().getInfo().getCalledDefinitions();
			if (funcDef.isEmpty() || funcDef.stream().noneMatch(f -> this.functionWithBarrier.contains(f))) {
				predecessors = new HashSet<>();
				predecessors.add(postNode.getParent().getPreCallNode());
			}
		}
		for (Node predNode : predecessors) {
			F predOUT = (F) predNode.getInfo().getOUT(analysisName);
			if (predOUT == null) {
				continue;
			}
			F edgeOUT = this.edgeTransferFunction(predOUT, predNode, node);
			inChanged |= newIN.merge(edgeOUT, null);
		}

		if (node instanceof PostCallNode) {
			inChanged |= processPostCallNodesIN((PostCallNode) node, newIN);
		}

		nodeInfo.setIN(analysisName, newIN);

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		F newOUT = node.accept(this, newIN);
		nodeInfo.setOUT(analysisName, newOUT);

		// Step 3: Process the successors, if needed.
		propagateFurther |= inChanged;
		if (propagateFurther) {
			workList.addAll(nodeInfo.getCFGInfo().getInterProceduralLeafSuccessors());
			if (node instanceof PreCallNode) {
				PreCallNode pre = (PreCallNode) node;
				this.workList.add(pre.getParent().getPostCallNode());
			}
			// If we are adding successors of a BeginNode of a FunctionDefinition, we should
			// add all the ParameterDeclarations.
			if (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition) {
				FunctionDefinition func = (FunctionDefinition) node.getParent();
				for (ParameterDeclaration paramDecl : func.getInfo().getCFGInfo().getParameterDeclarationList()) {
					this.workList.add(paramDecl);
				}
			}
		}
	}

	/**
	 * This method can be overridden to perform any extra merge operations, if
	 * needed, on the IN of a {@link
	 * PostCallNode}, beyond the meet of OUT of {@link EndNode} of all its targets.
	 *
	 * @param node
	 * @param incompleteIN
	 *
	 * @return
	 */
	protected abstract boolean processPostCallNodesIN(PostCallNode node, F incompleteIN);

	public void restartAnalysisFromStoredNodes() {
		assert (!SCC.processingTarjan);
		this.autoUpdateTriggerCounter++;
		if (this.analysisName == AnalysisName.PREDICATE_ANALYSIS) {
			functionWithBarrier.clear();
			for (FunctionDefinition foo : Misc.getExactEnclosee(Program.getRoot(), FunctionDefinition.class)) {
				if (foo.getInfo().hasBarrierInCFG()) {
					functionWithBarrier.add(foo);
				}
			}
		}
		long localTimer = System.nanoTime();

		/*
		 * From the set of nodes to be updated, we obtain the workList to be
		 * processed.
		 * We add all the entry points of the SCC of each node.
		 */
		this.workList.recreate();
		for (Node n : nodesToBeUpdated) {
			if (n.getInfo().isConnectedToProgram()) {
				boolean added = this.workList.add(n);
			}
		}
		// OLD CODE: Now, if we ever find that a node is unconnected to the program, we
		// remove it from processing.
		this.nodesToBeUpdated.clear();
		// this.nodesToBeUpdated.clear();

		this.processedInThisUpdate = new HashSet<>();
		this.yetToBeFinalized = new HashSet<>();
		while (!workList.isEmpty()) {
			Node nodeToBeAnalyzed = workList.removeFirstElement();
			CFGInfo nInfo = nodeToBeAnalyzed.getInfo().getCFGInfo();
			if (nInfo.getSCC() == null) {
				// Here, node itself is an SCC. We do not require two rounds.
				this.nodesProcessedDuringUpdate++;
				this.debugRecursion(nodeToBeAnalyzed);
				this.processWhenNotUpdated(nodeToBeAnalyzed); // Directly invoke the second round processing.
				continue;
			} else {
				/*
				 * This node belongs to an SCC. We should process the whole of
				 * SCC in the first phase, followed by its processing in the
				 * second phase, and only then shall we move on to the next SCC.
				 */
				stabilizeSCCOf(nodeToBeAnalyzed);
			}
		}

		localTimer = System.nanoTime() - localTimer;
		this.flowAnalysisUpdateTimer += localTimer;
	}

	/**
	 * Processing that is performed when the program has been updated. Note that IN
	 * objects would be reinitialized for
	 * all encountered nodes.
	 *
	 * @param node
	 * @param stateINChanged
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected final void processWhenUpdated(Node node) {
		boolean first = false;
		if (!this.processedInThisUpdate.contains(node)) {
			first = true;
			// Don't add the node to this set unless its OUT has been populated.
		}
		NodeInfo nodeInfo = node.getInfo();
		Collection<Node> predecessors;
		predecessors = nodeInfo.getCFGInfo().getInterProceduralLeafPredecessors();
		F oldIN = (F) nodeInfo.getIN(analysisName);
		boolean propagateFurther = false;
		F newIN;
		if (oldIN == null) {
			// First processing ever of this node!
			propagateFurther = true;
			newIN = (predecessors.isEmpty()) ? newIN = this.getEntryFact() : this.getTop();
		} else if (first) {
			// First processing in the first round of update.
			newIN = (predecessors.isEmpty()) ? newIN = this.getEntryFact() : this.getTop();
		} else {
			newIN = oldIN; // Use the same object.
		}

		boolean inChanged = false;
		boolean anyPredMissed = false;
		for (Node predNode : predecessors) {
			/*
			 * Ignore a predecessor if:
			 * (i) it lies within the SCC, AND
			 * (ii) it has not been processed yet.
			 */
			SCC predSCC = predNode.getInfo().getCFGInfo().getSCC();
			if (predSCC != null) {
				// If null, then pred clearly belongs to some other SCC.
				if (node.getInfo().getCFGInfo().getSCC() == predSCC) {
					// Predecessor lies within the SCC.
					if (!this.processedInThisUpdate.contains(predNode)) {
						anyPredMissed = true;
						continue;
					}
				}
			}
			F predOUT = (F) predNode.getInfo().getOUT(analysisName);
			if (predOUT == null) {
				/*
				 * Here, we do not mark anyPredMissed, as this node would be
				 * marked for processing whenever the predecessor has been
				 * processed for the first time. Check the setting of
				 * propagateFurther above.
				 */
				continue;
			}
			F edgeOUT = this.edgeTransferFunction(predOUT, predNode, node);
			inChanged |= newIN.merge(edgeOUT, null);
		}

		if (node instanceof PostCallNode) {
			PreCallNode preNode = ((CallStatement) node.getParent()).getPreCallNode();
			SCC preSCC = preNode.getInfo().getCFGInfo().getSCC();
			boolean doNotProcess = false;
			if (preSCC != null) {
				// If null, then pred clearly belongs to some other SCC.
				if (node.getInfo().getCFGInfo().getSCC() == preSCC) {
					// Predecessor lies within the SCC.
					if (!this.processedInThisUpdate.contains(preNode)) {
						anyPredMissed = true;
						doNotProcess = true;
					}
				}
			}
			if (!doNotProcess) {
				inChanged |= processPostCallNodesIN((PostCallNode) node, newIN);
			}

		}
		if (anyPredMissed) {
			this.yetToBeFinalized.add(node);
		} else {
			this.yetToBeFinalized.remove(node);
		}
		nodeInfo.setIN(analysisName, newIN);

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		F newOUT;
		newOUT = node.accept(this, newIN);
		nodeInfo.setOUT(analysisName, newOUT);
		this.processedInThisUpdate.add(node); // Mark a node as processed only after its OUT has been "purified".

		// Step 3: Process the successors.
		propagateFurther |= inChanged;
		if (propagateFurther) {
			this.workList.addAll(nodeInfo.getCFGInfo().getInterProceduralLeafSuccessors());
			if (node instanceof PreCallNode) {
				PreCallNode pre = (PreCallNode) node;
				this.workList.add(pre.getParent().getPostCallNode());
			}
			// If we are adding successors of a BeginNode of a FunctionDefinition, we should
			// add all the ParameterDeclarations.
			if (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition) {
				FunctionDefinition func = (FunctionDefinition) node.getParent();
				for (ParameterDeclaration paramDecl : func.getInfo().getCFGInfo().getParameterDeclarationList()) {
					this.workList.add(paramDecl);
				}
			}
		}
	}

	/**
	 * f0 ::= DeclarationSpecifiers() f1 ::= ParameterAbstraction()
	 */
	@Override
	public final F visit(ParameterDeclaration n, F flowFactOne) {
		if (n.toString().equals("void ")) {
			return flowFactOne;
		}
		FunctionDefinition calledFunction = Misc.getEnclosingFunction(n);
		if (calledFunction.getInfo().getFunctionName().equals("main")) {
			return flowFactOne;
		}
		int index = calledFunction.getInfo().getCFGInfo().getParameterDeclarationList().indexOf(n);
		Set<CallStatement> callers = calledFunction.getInfo().getCallersOfThis();
		if (callers.isEmpty()) {
			return flowFactOne;
		}
		F flowFactTwo = this.getTop();
		for (CallStatement callStmt : callers) {
			SimplePrimaryExpression argExp;
			try {
				argExp = callStmt.getPreCallNode().getArgumentList().get(index);
			} catch (Exception e) {
				continue;
			}
			F tempFlowFact = writeToParameter(n, argExp, flowFactOne);
			flowFactTwo.merge(tempFlowFact, null);
		}
		return flowFactTwo;
	}

	/**
	 * Given a parameter-declaration {@code parameter}, and a
	 * simple-primary-expression {@code argument} that represents
	 * the argument for this parameter from some call-site, this method should model
	 * the flow-function of the write to
	 * the parameter that happens implicitly.
	 *
	 * @param parameter
	 *                    a {@code ParameterDeclaration} which needs to be assigned
	 *                    with the {@code argument}.
	 * @param argument
	 *                    a {@code SimplePrimaryExpression} which is assigned to the
	 *                    {@code parameter}.
	 * @param flowFactOne
	 *                    the IN/OUT flow-fact for the implicit assignment of the
	 *                    {@code argument} to the {@code parameter}.
	 *
	 * @return the OUT/IN flow-fact, as a result of the implicit assignment of the
	 *         {@code argument} to the {@code
	 * parameter}.
	 */
	public abstract F writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument, F flowFactOne);
}
