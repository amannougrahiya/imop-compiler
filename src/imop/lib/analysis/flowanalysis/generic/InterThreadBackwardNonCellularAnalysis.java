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
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.HashSet;
import java.util.Set;

public abstract class InterThreadBackwardNonCellularAnalysis<F extends FlowAnalysis.FlowFact>
		extends NonCellularDataFlowAnalysis<F> {

	public InterThreadBackwardNonCellularAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
		super(analysisName, analysisDimension);
	}

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
	public void run(FunctionDefinition funcDef) {
		EndNode endNode = funcDef.getInfo().getCFGInfo().getNestedCFG().getEnd();
		this.workList.recreate();
		this.workList.add(endNode);
		do {
			Node nodeToBeAnalysed = this.workList.removeLastElement();
			this.debugRecursion(nodeToBeAnalysed);
			this.processWhenNotUpdated(nodeToBeAnalysed);
		} while (!workList.isEmpty());
	}

	/**
	 * Given a node connected to the program, this method ensures that the IN and
	 * OUT of all the leaf nodes within the
	 * given node are set to be the meet of IN of the successors of the exit point
	 * of the node (there must be just one
	 * exit point) . Note that since the {@code affectedNode} has been checked to
	 * not affect the flow-analysis, we
	 * should use the same object for IN as well as OUT of each node.<br>
	 * Important: If the IN of successors of the exit
	 * node is not stable, we will still use the unstable values for now; not that
	 * the stabilization process, whenever
	 * triggered, would take care of these nodes as well.
	 *
	 * @param affectedNode
	 *                     node which does not seem to affect the analysis (and
	 *                     hence, for which we just copy same IN and OUT to all
	 *                     its internal nodes).
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final void cloneFlowFactsToAllWithin(Node affectedNode) {
		Node node;
		if (Misc.isCFGLeafNode(affectedNode)) {
			node = affectedNode;
		} else {
			node = affectedNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
		}

		NodeInfo nodeInfo = node.getInfo();
		Set<IDFAEdge> successors = nodeInfo.getCFGInfo()
				.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension());
		F ffForAll = successors.isEmpty() ? this.getEntryFact() : this.getTop();

		for (IDFAEdge idfaEdge : successors) {
			F succIN = (F) idfaEdge.getNode().getInfo().getCurrentIN(analysisName);
			// F predOUT = (F) idfaEdge.getNode().getInfo().getOUT(analysisName);
			if (succIN == null) {
				continue;
			}
			F edgeIN = this.edgeTransferFunction(succIN, idfaEdge.getNode(), node);
			ffForAll.merge(edgeIN, idfaEdge.getCells());
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
		Set<IDFAEdge> successors = nodeInfo.getCFGInfo()
				.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension());
		F newOUT = (F) nodeInfo.getOUT(analysisName);
		if (newOUT == null) {
			/*
			 * In this scenario, we must set the propagation flag, so that
			 * all successors are processed at least once.
			 */
			propagateFurther = true;
			if (successors.isEmpty()) {
				newOUT = this.getEntryFact();
			} else {
				newOUT = this.getTop();
			}
		}

		boolean outChanged = false;
		for (IDFAEdge idfaEdge : successors) {
			F succIN = (F) idfaEdge.getNode().getInfo().getIN(analysisName);
			if (succIN == null) {
				continue;
			}
			F edgeIN = this.edgeTransferFunction(succIN, idfaEdge.getNode(), node);
			outChanged |= newOUT.merge(edgeIN, idfaEdge.getCells());
		}

		/**
		 * If the OUT of a BarrierDirective has changed,
		 * we should add all its sibling barriers to the workList.
		 */
		if (outChanged && node instanceof BarrierDirective) {
			this.addAllSiblingBarriersToWorkList((BarrierDirective) node);
		}
		nodeInfo.setOUT(analysisName, newOUT);

		// Step 2: Apply the flow-function on OUT, to obtain the IN.
		F oldIN = (F) nodeInfo.getIN(analysisName);
		F newIN = node.accept(this, newOUT);
		nodeInfo.setIN(analysisName, newIN);

		// Step 3: Process the successors, if needed.
		propagateFurther |= outChanged;
		if (node instanceof BarrierDirective) {
			if (newIN == newOUT && outChanged) {
				assert (false);
				propagateFurther = true; // Redundant; kept for readability.
			} else if (oldIN == null || !newIN.isEqualTo(oldIN)) {
				propagateFurther = true;
			}
		}
		if (propagateFurther) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension())) {
				Node n = idfaEdge.getNode();
				this.workList.add(n);
			}
		}
		return;
	}

	/**
	 * Processing that is performed when the program has been updated. Note that OUT
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
			this.processedInThisUpdate.add(node);
			first = true;
		}
		NodeInfo nodeInfo = node.getInfo();
		Set<IDFAEdge> successors = nodeInfo.getCFGInfo()
				.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension());
		F oldOUT = (F) nodeInfo.getOUT(analysisName);
		F newOUT = successors.isEmpty() ? this.getEntryFact() : this.getTop();

		boolean anyINignored = false;
		for (IDFAEdge idfaEdge : successors) {
			if (!processedInThisUpdate.contains(idfaEdge.getNode())) {
				if (reachablePredecessorsOfSeeds.containsKey(node)) {
					if (reachablePredecessorsOfSeeds.get(node).contains(idfaEdge.getNode())) {
						yetToBeFinalized.add(node);
						anyINignored = true;
						continue;
					}
				} else {
					yetToBeFinalized.add(node);
					anyINignored = true;
					continue;
				}
			}
			F succIN = (F) idfaEdge.getNode().getInfo().getIN(analysisName);
			if (succIN == null) {
				continue;
			}
			/*
			 * Ignore the "may be imprecise" IN of all those predecessors that
			 * have not been processed since this update yet.
			 * There are two scenarios now: (i) we will encounter this node
			 * again, after updating the IN of this predecessor upon processing
			 * it, OR (ii) the IDFA flow-facts will stabilize well before we get
			 * a chance to process this node again.
			 * In order to ensure that the final value of this flow-fact is
			 * correct even if we encounter scenario "(ii)" above, we should add
			 * this node to the list of nodes that should be processed once
			 * more, in NO-PROGRAM-UPDATE mode.
			 */
			F edgeIN = this.edgeTransferFunction(succIN, idfaEdge.getNode(), node);
			newOUT.merge(edgeIN, idfaEdge.getCells());
		}
		if (!anyINignored) {
			this.yetToBeFinalized.remove(node);
		}
		F oldIN = (F) nodeInfo.getIN(analysisName);
		if (newOUT.isEqualTo(oldOUT) && oldIN != null && !(node instanceof BarrierDirective) && !first) {
			return;
		}
		nodeInfo.setOUT(analysisName, newOUT);

		// Step 2: Apply the flow-function on OUT, to obtain the IN.
		F newIN;
		if (node instanceof BarrierDirective) {
			newIN = this.visitChanged((BarrierDirective) node, newOUT, first);
		} else {
			newIN = node.accept(this, newOUT);
		}
		nodeInfo.setIN(analysisName, newIN);

		// Step 3: Process the successors.
		if (oldIN == null || !newIN.isEqualTo(oldIN)) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension())) {
				this.workList.add(idfaEdge.getNode());
			}
		}
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <BARRIER> f2 ::= OmpEol()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final F visit(BarrierDirective n, F flowFactOUT) {
		// OLD CODE: boolean changed = false;
		F flowFactIN = this.getTop();
		////

		F oldFactIN = (F) n.getInfo().getIN(analysisName);
		if (oldFactIN != null) {
			flowFactIN.merge(oldFactIN, null);
		}

		///
		for (AbstractPhase<?, ?> ph : new HashSet<>(n.getInfo().getNodePhaseInfo().getPhaseSet())) {
			for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
				if (!(endingPhasePoint.getNodeFromInterface() instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNodeFromInterface();
				F siblingFlowFactOUT = (F) siblingBarrier.getInfo().getOUT(analysisName);
				if (siblingFlowFactOUT == null) {
					continue;
				}
				if (siblingBarrier == n) {
					siblingFlowFactOUT = flowFactOUT;
					// OLC CODE: changed = changed | flowFactIN.merge(siblingFlowFactOUT, null);
					flowFactIN.merge(siblingFlowFactOUT, null);
				} else {
					if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON
							&& this.analysisDimension.getSVEDimension() == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInPhase(n, siblingBarrier, ph)) {
						continue;
					}
					// OLD CODE: changed = changed | flowFactIN.merge(siblingFlowFactOUT,
					// n.getInfo().getSharedCellsAtNode());
					flowFactIN.merge(siblingFlowFactOUT, n.getInfo().getSharedCellsAtNode());
				}
			}
		}
		// OLD CODE: Now, instead of adding sibling barriers upon a changing IN,
		// we do so when the OUT of a BarrierDirective changes.
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
		return flowFactIN;
	}

	@SuppressWarnings("unchecked")
	public final F visitChanged(BarrierDirective n, F flowFactOUT, final boolean first) {
		boolean changed = false;
		F flowFactIN = this.getTop();
		if (!first) {
			F oldFactIN = (F) n.getInfo().getIN(analysisName);
			if (oldFactIN != null) {
				flowFactIN.merge(oldFactIN, null);
			}
		}
		boolean anyINignored = false;
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
							anyINignored = true;
							continue;
						}
					} else {
						yetToBeFinalized.add(n);
						anyINignored = true;
						continue;
					}
				}
				F siblingFlowFactOUT = (F) siblingBarrier.getInfo().getOUT(analysisName);
				if (siblingFlowFactOUT == null) {
					continue;
				}
				if (siblingBarrier == n) {
					siblingFlowFactOUT = flowFactOUT;
					changed = changed | flowFactIN.merge(siblingFlowFactOUT, null);
				} else {
					if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON
							&& this.analysisDimension.getSVEDimension() == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInPhase(n, siblingBarrier, ph)) {
						continue;
					}
					changed = changed | flowFactIN.merge(siblingFlowFactOUT, n.getInfo().getSharedCellsAtNode());
				}
			}
		}
		if (!anyINignored) {
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
					F siblingFlowIN = (F) siblingBarrier.getInfo().getIN(analysisName);
					if (siblingFlowIN == null) {
						continue;
					}
					CellSet siblingShared = siblingBarrier.getInfo().getSharedCellsAtNode();
					CellSet sharedShared = Misc.setIntersection(myShared, siblingShared);
					F myTemp = this.getTop();
					myTemp.merge(flowFactIN, sharedShared);
					F tempIN = this.getTop();
					tempIN.merge(siblingFlowIN, sharedShared);
					if (myTemp.isEqualTo(tempIN)) {
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
		return flowFactIN;
	}
}
