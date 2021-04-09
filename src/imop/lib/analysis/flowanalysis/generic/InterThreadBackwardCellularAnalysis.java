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
import imop.lib.analysis.flowanalysis.AddressCell;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.FieldCell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.util.CellSet;
import imop.lib.util.Immutable;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.HashSet;
import java.util.Set;

public abstract class InterThreadBackwardCellularAnalysis<F extends CellularDataFlowAnalysis.CellularFlowMap<?>>
		extends CellularDataFlowAnalysis<F> {

	public InterThreadBackwardCellularAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
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

		if (node instanceof PreCallNode) {
			outChanged |= this.processPreCallNodes((PreCallNode) node, newOUT);
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
		if (node instanceof BeginNode) {
			this.processBeginNodes((BeginNode) node, newIN);
		} else if (node instanceof EndNode) {
			this.processEndNodes((EndNode) node, newIN);
		}
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
			// TODO: Write code for handling CallStatement scoping edges. While doing so,
			// also take care of the extra if-conditional for
			// BeginNode of a FunctionDefinition that is present in the equivalent code for
			// forward analyses.
		}
		return;
	}

	/**
	 * Adds missing information from the OUT of the corresponding PreCallNode to the
	 * given {@code incompleteFF}, which
	 * is the final IN state of the given post-call node, {@code node}.
	 *
	 * @param node
	 *                     a postCallNode.
	 * @param incompleteFF
	 */
	@SuppressWarnings("unchecked")
	private <H extends Immutable> boolean processPreCallNodes(PreCallNode node, F incompleteFF) {
		PostCallNode postNode = node.getParent().getPostCallNode();
		CellularFlowMap<H> preOUT = (CellularFlowMap<H>) incompleteFF;
		CellularFlowMap<H> postIN = (CellularFlowMap<H>) postNode.getInfo().getIN(analysisName);
		if (postIN == null || postIN.flowMap == null) {
			return false;
		}

		Set<Cell> keysInPre = preOUT.flowMap.nonGenericKeySet();
		boolean changed = false;
		for (Cell postKey : postIN.flowMap.nonGenericKeySet()) {
			if (!keysInPre.contains(postKey)) {
				changed = true;
				H val = postIN.flowMap.get(postKey);
				assert (val != null);
				preOUT.flowMap.put(postKey, val);
			}
		}
		return changed;
	}

	/**
	 * This method removes those entries from the flow-map {@code fullFF} that do
	 * not correspond to accessible symbols
	 * at the function which the given end-node, {@code node}, ends.
	 *
	 * @param node
	 *               a begin-node
	 * @param fullFF
	 *               state of the OUT of the begin-node; note that this object's
	 *               values may get modified.
	 */
	private void processEndNodes(EndNode node, F fullFF) {
		CellularFlowMap<?> cellFullFF = fullFF;
		if (!(node.getParent() instanceof FunctionDefinition)) {
			return;
		}
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

	/**
	 * This method removes those entries from the flow-map {@code fullFF} which
	 * correspond to local keys of the scope
	 * that the given begin node, {@code node}, begins.
	 *
	 * @param node
	 *               an end-node.
	 * @param fullFF
	 *               state of OUT of the end-node; note that this object's values
	 *               may get modified.
	 */
	private void processBeginNodes(BeginNode node, F fullFF) {
		CellularFlowMap<?> cellFullFF = fullFF;
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
	protected final void processWhenUpdated(Node node) {
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
		if (node instanceof PreCallNode) {
			processPreCallNodes((PreCallNode) node, newOUT);
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
			if (node instanceof EndNode) {
				processEndNodes((EndNode) node, newIN);
			} else if (node instanceof BeginNode) {
				processBeginNodes((BeginNode) node, newIN);
			}
		}
		nodeInfo.setIN(analysisName, newIN);

		// Step 3: Process the successors.
		if (oldIN == null || !newIN.isEqualTo(oldIN)) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension())) {
				this.workList.add(idfaEdge.getNode());
				// TODO: Write code for handling CallStatement scoping edges. While doing so,
				// also take care of the extra if-conditional for
				// BeginNode of a FunctionDefinition that is present in the equivalent code for
				// forward analyses.
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
