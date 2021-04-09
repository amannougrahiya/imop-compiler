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
import imop.lib.util.Misc;

import java.util.Collection;

/**
 * Superclass for all intra-procedural control-flow analyses.
 *
 * @author Aman Nougrahiya
 */
public abstract class IntraProceduralControlFlowAnalysis<F extends FlowAnalysis.FlowFact>
		extends ControlFlowAnalysis<F> {

	public IntraProceduralControlFlowAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
		super(analysisName, analysisDimension);
	}

	@Override
	public final void run(FunctionDefinition startingFunc) {
		for (FunctionDefinition reachableFuncDef : startingFunc.getInfo().getReachableCallGraphNodes()) {
			BeginNode beginNode = reachableFuncDef.getInfo().getCFGInfo().getNestedCFG().getBegin();
			this.workList.recreate();
			this.workList.add(beginNode);
			do {
				Node nodeToBeAnalysed = this.workList.removeFirstElement();
				this.debugRecursion(nodeToBeAnalysed);
				this.processWhenNotUpdated(nodeToBeAnalysed);
			} while (!workList.isEmpty());
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
	@Override
	public final void cloneFlowFactsToAllWithin(Node affectedNode) {
		Node node;
		if (Misc.isCFGLeafNode(affectedNode)) {
			node = affectedNode;
		} else {
			node = affectedNode.getInfo().getCFGInfo().getNestedCFG().getBegin();
		}

		NodeInfo nodeInfo = node.getInfo();
		Collection<Node> predecessors = nodeInfo.getCFGInfo().getLeafPredecessors();
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
		for (Node internalLeaf : affectedNode.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
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
		Collection<Node> predecessors = nodeInfo.getCFGInfo().getLeafPredecessors();
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
		F newOUT;
		if (node instanceof PreCallNode) {
			newOUT = modelCallEffect((CallStatement) node.getParent(), newIN);
		} else {
			newOUT = node.accept(this, newIN);
		}
		nodeInfo.setOUT(analysisName, newOUT);

		// Step 3: Process the successors, if needed.
		propagateFurther |= inChanged;
		if (propagateFurther) {
			workList.addAll(nodeInfo.getCFGInfo().getLeafSuccessors());
		}
		return;
	}

	/**
	 * This method should model the effect of the given call statement on the given
	 * flow fact, and return the result.
	 *
	 * @param callStmt
	 * @param newIN
	 *
	 * @return
	 */
	protected F modelCallEffect(CallStatement callStmt, F newIN) {
		return newIN;
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
	protected void processWhenUpdated(Node node) {
		boolean first = false;
		if (!this.processedInThisUpdate.contains(node)) {
			first = true;
			// Don't add the node to this set unless its OUT has been populated.
		}
		NodeInfo nodeInfo = node.getInfo();
		Collection<Node> predecessors;
		predecessors = nodeInfo.getCFGInfo().getLeafPredecessors();
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

		if (anyPredMissed) {
			this.yetToBeFinalized.add(node);
		} else {
			this.yetToBeFinalized.remove(node);
		}
		nodeInfo.setIN(analysisName, newIN);

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		F oldOUT = (F) nodeInfo.getOUT(analysisName);
		F newOUT;
		newOUT = node.accept(this, newIN);
		nodeInfo.setOUT(analysisName, newOUT);
		this.processedInThisUpdate.add(node); // Mark a node as processed only after its OUT has been "purified".

		// Step 3: Process the successors.
		propagateFurther |= inChanged;
		if (propagateFurther) {
			this.workList.addAll(nodeInfo.getCFGInfo().getLeafSuccessors());
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
		F flowFactTwo = this.getTop();
		F tempFlowFact = assignBottomToParameter(n, flowFactOne);
		flowFactTwo.merge(tempFlowFact, null);
		return flowFactTwo;
	}

	/**
	 * Given a parameter declaration, this method models the affect of assigning
	 * BOTTOM element lattice to the
	 * parameter, and returns the resulting updated {@code flowFactOne}.
	 *
	 * @param parameter
	 *                    a parameter declaration.
	 * @param flowFactOne
	 *                    a flow-fact on which the affect of parameter's write to
	 *                    BOTTOM has to be modeled.
	 *
	 * @return a flow-fact that is obtained upon applying the affect of parameter's
	 *         write to BOTTOM, on {@code
	 * flowFactOne}.
	 */
	public abstract F assignBottomToParameter(ParameterDeclaration parameter, F flowFactOne);

}
