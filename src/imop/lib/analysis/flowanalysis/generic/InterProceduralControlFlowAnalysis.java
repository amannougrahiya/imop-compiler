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
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.ContextDimension;
import imop.lib.util.Misc;

import java.util.Collection;
import java.util.Set;

public abstract class InterProceduralControlFlowAnalysis<F extends FlowAnalysis.FlowFact>
		extends ControlFlowAnalysis<F> {

	public InterProceduralControlFlowAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
		super(analysisName, analysisDimension);
	}

	/**
	 * Perform the analysis, starting at the begin node of the function
	 * {@code funcDef}.<br>
	 * <i>Note that this method does not clear the analysis information, if
	 * already present.</i>
	 * 
	 * @param funcDef
	 *            function-definition on which this analysis has to be
	 *            performed.
	 */
	@Override
	public final void run(FunctionDefinition funcDef) {
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
	 * Given a node connected to the program, this method ensures that the IN
	 * and OUT of all the leaf nodes within the given node are set to be the
	 * meet of OUT of all the predecessors of the entry point of the node (there
	 * must be just one entry point, and no internal barriers/flushes).
	 * Note that since the {@code affectedNode} has been checked to not affect
	 * the flow-analysis, we should use the same object for IN as well as OUT of
	 * each node.<br>
	 * Important: If the OUT of predecessors of the entry node is not stable, we
	 * will still use the unstable values for now; not that the stabilization
	 * process, whenever triggered, would take care of these nodes as well.
	 * 
	 * @param affectedNode
	 *            node which does not seem to affect the analysis (and hence,
	 *            for which we just copy same IN and OUT to all its internal
	 *            nodes).
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
			//			F predOUT = (F) idfaEdge.getNode().getInfo().getOUT(analysisName);
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
	 * Processing that is performed when no updates have been done to the
	 * program.
	 * Note that IN objects will never change -- their value may change
	 * monotonically.
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
		for (Node predNode : predecessors) {
			F predOUT = (F) predNode.getInfo().getOUT(analysisName);
			if (predOUT == null) {
				continue;
			}
			F edgeOUT = this.edgeTransferFunction(predOUT, predNode, node);
			inChanged |= newIN.merge(edgeOUT, null);
		}

		nodeInfo.setIN(analysisName, newIN);

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		F newOUT = node.accept(this, newIN);
		nodeInfo.setOUT(analysisName, newOUT);

		// Step 3: Process the successors, if needed.
		propagateFurther |= inChanged;
		if (propagateFurther) {
			workList.addAll(nodeInfo.getCFGInfo().getInterProceduralLeafSuccessors());
		}
	}

	/**
	 * Processing that is performed when the program has been updated.
	 * Note that IN objects would be reinitialized for all encountered nodes.
	 * 
	 * @param node
	 * @param stateINChanged
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected final void processWhenUpdated(Node node) {
		boolean first = false;
		if (!this.processedInThisUpdate.contains(node)) {
			this.processedInThisUpdate.add(node);
			first = true;
		}
		NodeInfo nodeInfo = node.getInfo();
		Collection<Node> predecessors;
		predecessors = nodeInfo.getCFGInfo().getLeafPredecessors();
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
		newOUT = node.accept(this, newIN);
		nodeInfo.setOUT(analysisName, newOUT);

		// Step 3: Process the successors.
		if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
			this.workList.addAll(nodeInfo.getCFGInfo().getLeafSuccessors());
		}
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ParameterAbstraction()
	 */
	@Override
	public final F visit(ParameterDeclaration n, F flowFactOne) {
		if (this.analysisDimension.getContextDimension() == ContextDimension.CONTEXT_SENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available for context-sensitive analysis.", null);
		}
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
	 * simple-primary-expression {@code argument} that represents the argument
	 * for this parameter from some call-site, this method should model the
	 * flow-function of the write to the parameter that happens implicitly.
	 * 
	 * @param parameter
	 *            a {@code ParameterDeclaration} which needs to be assigned with
	 *            the {@code argument}.
	 * @param argument
	 *            a {@code SimplePrimaryExpression} which is assigned to the
	 *            {@code parameter}.
	 * @param flowFactOne
	 *            the IN/OUT flow-fact for the implicit assignment of the
	 *            {@code argument} to the {@code parameter}.
	 * @return
	 *         the OUT/IN flow-fact, as a result of the implicit assignment of
	 *         the
	 *         {@code argument} to the {@code parameter}.
	 */
	public abstract F writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument, F flowFactOne);
}
