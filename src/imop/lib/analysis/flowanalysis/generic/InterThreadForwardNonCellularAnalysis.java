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

public abstract class InterThreadForwardNonCellularAnalysis<F extends FlowAnalysis.FlowFact>
		extends NonCellularDataFlowAnalysis<F> {

	public InterThreadForwardNonCellularAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
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
	@SuppressWarnings("unchecked")
	@Override
	public final void cloneFlowFactsToAllWithin(Node affectedNode) {
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
		nodeInfo.setOUT(analysisName, newOUT);

		// Step 3: Process the successors, if needed.
		propagateFurther |= inChanged;
		if (node instanceof BarrierDirective) {
			if (newOUT == newIN && inChanged) {
				assert (false);
				propagateFurther = true; // Redundant; kept for readability.
			} else if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
				propagateFurther = true;
			}
		}
		if (propagateFurther) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				Node n = idfaEdge.getNode();
				this.workList.add(n);
			}
		}
		return;
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
		}
		nodeInfo.setOUT(analysisName, newOUT);

		// Step 3: Process the successors.
		if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				this.workList.add(idfaEdge.getNode());
			}
		}
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <BARRIER> f2 ::= OmpEol()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final F visit(BarrierDirective n, F flowFactIN) {
		// OLD CODE: boolean changed = false;
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
					// OLD CODE: changed = changed | flowFactOUT.merge(siblingFlowFactIN, null);
					flowFactOUT.merge(siblingFlowFactIN, null);
				} else {
					if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON
							&& this.analysisDimension.getSVEDimension() == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInPhase(n, siblingBarrier, ph)) {
						continue;
					}
					// OLD CODE: changed = changed | flowFactOUT.merge(siblingFlowFactIN,
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

	@SuppressWarnings("unchecked")
	public final F visitChanged(BarrierDirective n, F flowFactIN, final boolean first) {
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
}
