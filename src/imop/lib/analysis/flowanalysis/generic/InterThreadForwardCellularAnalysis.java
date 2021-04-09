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
import imop.lib.analysis.flowanalysis.*;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis.PointsToGlobalState;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.util.CellSet;
import imop.lib.util.Immutable;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.HashSet;
import java.util.Set;

/**
 * A generic class to implement an inter-thread interprocedural forward
 * iterative data-flow analysis.
 */
public abstract class InterThreadForwardCellularAnalysis<F extends CellularDataFlowAnalysis.CellularFlowMap<?>>
		extends CellularDataFlowAnalysis<F> {

	public InterThreadForwardCellularAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
		super(analysisName, analysisDimension);
	}

	// private String foo(Node n) {
	// if (n.getInfo().getCFGInfo().getSCC() == null) {
	// return n.getReversePostOrder() + "" + n;
	// } else {
	// return n.getInfo().getCFGInfo().getSCC().getReversePostOrder() + "-" +
	// n.getReversePostOrder() + n;
	// }
	// }
	//

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
		// String typeList = "";
		BeginNode beginNode = funcDef.getInfo().getCFGInfo().getNestedCFG().getBegin();
		this.workList.recreate();
		this.workList.add(beginNode);
		do {
			Node nodeToBeAnalysed = this.workList.removeFirstElement();
			/*
			 * New code below.
			 */
			if (nodeToBeAnalysed instanceof BarrierDirective || (nodeToBeAnalysed instanceof EndNode
					&& nodeToBeAnalysed.getParent() instanceof ParallelConstruct)) {
				// Process all barriers from the work-list.
				Set<Node> barrList = new HashSet<>();
				barrList.add(nodeToBeAnalysed);
				while (true) {
					Node nextNode = this.workList.peekFirstElement();
					if (nextNode instanceof BarrierDirective
							|| (nextNode instanceof EndNode && nextNode.getParent() instanceof ParallelConstruct)) {
						nextNode = this.workList.removeFirstElement();
						barrList.add(nextNode);
					} else {
						break;
					}
				}
				for (Node barr : barrList) {
					// typeList += barr.getClass().getSimpleName() + foo(barr) + ";\n ";
					this.debugRecursion(barr);
					this.processBarrierINWhenNotUpdated(barr);
				}
				for (Node barr : barrList) {
					this.processBarrierOUTWhenNotUpdated(barr);
				}
			} else {
				/*
				 * New code above.
				 */
				// typeList += nodeToBeAnalysed.getClass().getSimpleName() +
				// foo(nodeToBeAnalysed) + ";\n ";
				this.debugRecursion(nodeToBeAnalysed);
				this.processWhenNotUpdated(nodeToBeAnalysed);
			}
		} while (!workList.isEmpty());
		if (this.analysisName == AnalysisName.POINTSTO) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.CORRECT;
		}
		// DumpSnapshot.printToFile(typeList, "b.b");
	}

	/**
	 * Processing that is performed when no updates have been done to the program.
	 * Note that IN objects will never
	 * change -- their value may change monotonically.
	 *
	 * @param barr
	 * @param stateINChanged
	 */
	@SuppressWarnings("unchecked")
	protected final void processBarrierINWhenNotUpdated(Node barr) {
		boolean propagateFurther = false;
		NodeInfo nodeInfo = barr.getInfo();
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
			F edgeOUT = this.edgeTransferFunction(predOUT, idfaEdge.getNode(), barr);
			inChanged |= newIN.merge(edgeOUT, idfaEdge.getCells());
		}
		/**
		 * If the IN of a BarrierDirective has changed,
		 * we should add all its sibling barriers to the workList.
		 */
		if (inChanged && barr instanceof BarrierDirective) {
			this.addAllSiblingBarriersToWorkList((BarrierDirective) barr);
		}
		nodeInfo.setIN(analysisName, newIN);

		propagateFurther |= inChanged;
		if (propagateFurther) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				Node n = idfaEdge.getNode();
				this.workList.add(n);
			}
		}
		return;
	}

	@SuppressWarnings("unchecked")
	protected final void processBarrierOUTWhenNotUpdated(Node barr) {
		NodeInfo nodeInfo = barr.getInfo();
		F newIN = (F) nodeInfo.getIN(analysisName);
		assert (newIN != null);

		F oldOUT = (F) nodeInfo.getOUT(analysisName);
		F newOUT = barr.accept(this, newIN);
		nodeInfo.setOUT(analysisName, newOUT);

		if (barr instanceof BarrierDirective && (oldOUT == null || !newOUT.isEqualTo(oldOUT))) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				Node n = idfaEdge.getNode();
				this.workList.add(n);
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
				assert (false); // OUT of a barrier can never be same object as its IN.
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
	 * This method starts with the given node, and stabilizes the SCC corresponding
	 * to it before returning back.
	 *
	 * @param node
	 *             node whose SCC has to be stabilized. Note that the node has
	 *             already been removed from the workList.
	 */
	protected final void stabilizeSCCOf(Node node) {
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
	 * Adds missing information from the OUT of the corresponding PreCallNode to the
	 * given {@code incompleteFF}, which
	 * is the final IN state of the given post-call node, {@code node}.
	 *
	 * @param node
	 *                     a postCallNode.
	 * @param incompleteFF
	 */
	@SuppressWarnings("unchecked")
	private <H extends Immutable> boolean processPostCallNodes(PostCallNode node, F incompleteFF) {
		PreCallNode preNode = node.getParent().getPreCallNode();
		CellularFlowMap<H> postIN = (CellularFlowMap<H>) incompleteFF;
		CellularFlowMap<H> preOUT = (CellularFlowMap<H>) preNode.getInfo().getOUT(analysisName);
		if (preOUT == null || preOUT.flowMap == null) {
			return false;
		}

		Set<Cell> keysInPost = postIN.flowMap.nonGenericKeySet();
		boolean changed = false;
		for (Cell preKey : preOUT.flowMap.nonGenericKeySet()) {
			if (!keysInPost.contains(preKey)) {
				H val = preOUT.flowMap.get(preKey);
				if (val == null) {
					continue;
				}
				changed = true;
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
		CellularFlowMap<?> cellFullFF = fullFF;
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
	 * f0 ::= OmpPragma() f1 ::= <BARRIER> f2 ::= OmpEol()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final F visit(BarrierDirective n, F flowFactIN) {
		// OLD CODE: boolean changed = false;
		F flowFactOUT = this.getTop();

		F oldFactOUT = (F) n.getInfo().getOUT(analysisName);
		if (oldFactOUT != null) {
			flowFactOUT.merge(oldFactOUT, null);
		}

		for (AbstractPhase<?, ?> ph : new HashSet<>(n.getInfo().getNodePhaseInfo().getPhaseSet())) {
			// Check whether this phase is actually ended by n.
			boolean found = false;
			for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
				if (endingPhasePoint.getNodeFromInterface() == n) {
					found = true;
					break;
				}
			}
			if (!found) {
				continue;
			}
			// Apply the transfer function.
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

	@Override
	public void restartAnalysisFromStoredNodes() {
		assert (!SCC.processingTarjan);
		if (this.analysisName == AnalysisName.POINTSTO
				&& PointsToAnalysis.stateOfPointsTo != PointsToGlobalState.STALE) {
			nodesToBeUpdated.clear();
			return;
		}
		this.autoUpdateTriggerCounter++;
		// Use the commented code below when attempting to count the list per trigger.
		// We also need to uncomment the increment to localCount in debugRecursion, when
		// counting.
		// if (localCount != -1) {
		// localList.add(localCount);
		// }
		// localCount = 0;
		long localTimer = System.nanoTime();

		/*
		 * From the set of nodes to be updated, we obtain the workList to be
		 * processed.
		 * We add all the entry points of the SCC of each node.
		 */
		this.workList.recreate();
		Set<Node> remSet = new HashSet<>();
		for (Node n : nodesToBeUpdated) {
			boolean added = this.workList.add(n);
			if (added) {
				remSet.add(n);
			}
			// OLD CODE: Now we do not add the entry points of SCCs to be processed.
			// SCC nSCC = n.getInfo().getCFGInfo().getSCC();
			// if (nSCC != null) {
			// this.workList.addAll(nSCC.getEntryNodes());
			// }
		}
		// OLD CODE: Now, if we ever find that a node is unconnected to the program, we
		// remove it from processing.
		this.nodesToBeUpdated.removeAll(remSet);
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
		if (this.analysisName == AnalysisName.POINTSTO
				&& PointsToAnalysis.stateOfPointsTo == PointsToGlobalState.STALE) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.CORRECT;
			// DumpSnapshot.dumpPointsTo("stable" + Program.updateCategory +
			// this.autoUpdateTriggerCounter);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected final void processWhenUpdated(Node node) {
		boolean first = false;
		if (!this.processedInThisUpdate.contains(node)) {
			first = true;
			// Don't add the node to this set unless its OUT has been populated.
		}
		NodeInfo nodeInfo = node.getInfo();
		Set<IDFAEdge> predecessors = nodeInfo.getCFGInfo()
				.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension());
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
		for (IDFAEdge idfaEdge : predecessors) {
			Node predNode = idfaEdge.getNode();
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
			F edgeOUT = this.edgeTransferFunction(predOUT, idfaEdge.getNode(), node);
			inChanged |= newIN.merge(edgeOUT, idfaEdge.getCells());
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
				inChanged |= processPostCallNodes((PostCallNode) node, newIN);
			}
		}
		if (anyPredMissed) {
			this.yetToBeFinalized.add(node);
		} else {
			this.yetToBeFinalized.remove(node);
		}
		/**
		 * If the IN of a BarrierDirective has changed,
		 * we should add all its sibling barriers to the workList.
		 */
		if (node instanceof BarrierDirective) {
			if (oldIN == null) {
				// This was the first processing of the barrier, ever. Others would get affected
				// only if the in was changed.
				if (inChanged) {
					this.addAllSiblingBarriersToWorkList((BarrierDirective) node);
				}
			} else if (oldIN != newIN) {
				// This was the first processing of the barrier in this round. If the objects
				// differ semantically, others would get affected.
				if (!oldIN.equals(newIN)) {
					this.addAllSiblingBarriersToWorkList((BarrierDirective) node);
				}
			} else {
				// This is any-but-first processing of the barrier in this round. Others would
				// be impacted only if anything changed in IN.
				if (inChanged) {
					this.addAllSiblingBarriersToWorkList((BarrierDirective) node);
				}
			}
		}
		nodeInfo.setIN(analysisName, newIN);

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		F oldOUT = (F) nodeInfo.getOUT(analysisName);
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
		this.processedInThisUpdate.add(node); // Mark a node as processed only after its OUT has been "purified".

		// Step 3: Process the successors, if needed.
		propagateFurther |= inChanged;
		if (node instanceof BarrierDirective) {
			if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
				propagateFurther = true;
			}
		}
		if (propagateFurther) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				Node n = idfaEdge.getNode();
				this.workList.add(n);
			}
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

	@SuppressWarnings("unchecked")
	public final F visitChanged(BarrierDirective n, F flowFactIN, boolean first) {
		F newOUT = this.getTop();

		F oldOUT = (F) n.getInfo().getOUT(analysisName);
		if (first) {
			// Here, we must ignore the oldOUT, whether null or not.
		} else {
			// Here, we can safely start with the value of oldOUT, if not null.
			if (oldOUT != null) {
				newOUT.merge(oldOUT, null);
			}
		}

		// Capture the SCC id for this barrier.
		int thisBarrNum = n.getInfo().getCFGInfo().getSCCRPOIndex();
		// Now, take the meet of IN of sibling barriers.
		boolean anyPredMissed = false;
		for (AbstractPhase<?, ?> ph : new HashSet<>(n.getInfo().getNodePhaseInfo().getPhaseSet())) {
			// Check whether this phase is actually ended by n.
			boolean found = false;
			for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
				if (endingPhasePoint.getNodeFromInterface() == n) {
					found = true;
					break;
				}
			}
			if (!found) {
				continue;
			}
			// Apply the transfer function.
			for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
				if (!(endingPhasePoint.getNodeFromInterface() instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNodeFromInterface();
				int sibSCCNum = siblingBarrier.getInfo().getCFGInfo().getSCCRPOIndex();
				if (sibSCCNum < thisBarrNum) {
					// For barriers of previous SCCs, assume them to be stable and process the node.
					assert (false) : "Found a sibling barrier with different SCC id!";
				} else if (sibSCCNum == thisBarrNum) {
					if (!this.processedInThisUpdate.contains(siblingBarrier)) {
						// Ignore this sibling of same SCC for now.
						anyPredMissed = true;
						continue;
					}
				} else {
					// The future SCC barrier would add us whenever its IN changes. For now, ignore
					// it.
					// Since the future SCC has not been stabilized, let's ignore that barrier, but
					// without
					// marking this barrier for processing in the second round.
					assert (false) : "Found a sibling barrier with different SCC id!";
					continue;
				}
				// Ignore this sibling if if it has not been processed yet.
				F siblingFlowFactIN = (F) siblingBarrier.getInfo().getIN(analysisName);
				if (siblingFlowFactIN == null) {
					continue;
				}
				if (siblingBarrier == n) {
					siblingFlowFactIN = flowFactIN;
					newOUT.merge(siblingFlowFactIN, null);
				} else {
					if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON
							&& this.analysisDimension.getSVEDimension() == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInPhase(n, siblingBarrier, ph)) {
						continue;
					}
					newOUT.merge(siblingFlowFactIN, n.getInfo().getSharedCellsAtNode());
				}
			}
		}
		if (anyPredMissed) {
			this.yetToBeFinalized.add(n);
		} else {
			this.yetToBeFinalized.remove(n);
		}
		return newOUT;
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

	@SuppressWarnings("unchecked")
	@Deprecated
	public final F deprecated_visitChanged(BarrierDirective n, F flowFactIN, final boolean first) {
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

	@Deprecated
	public final void deprecated_restartAnalysisFromStoredNodes() {
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
		if (this.analysisName == AnalysisName.POINTSTO
				&& PointsToAnalysis.stateOfPointsTo == PointsToGlobalState.STALE) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.CORRECT;
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
	@Deprecated
	@SuppressWarnings("unchecked")
	protected final void deprecated_processWhenUpdated(Node node) {
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
			newOUT = this.deprecated_visitChanged((BarrierDirective) node, newIN, first);
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
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				this.workList.add(idfaEdge.getNode());
			}
			if (node instanceof PreCallNode) {
				PreCallNode pre = (PreCallNode) node;
				this.workList.add(pre.getParent().getPostCallNode());
			}

		}
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
	@Deprecated
	public void deprecated_run(FunctionDefinition funcDef) {
		// String typeList = "";
		BeginNode beginNode = funcDef.getInfo().getCFGInfo().getNestedCFG().getBegin();
		this.workList.recreate();
		this.workList.add(beginNode);
		do {
			Node nodeToBeAnalysed = this.workList.removeFirstElement();
			// typeList += nodeToBeAnalysed.getClass().getSimpleName() +
			// foo(nodeToBeAnalysed) + ";\n ";
			this.debugRecursion(nodeToBeAnalysed);
			this.processWhenNotUpdated(nodeToBeAnalysed);
		} while (!workList.isEmpty());
		if (this.analysisName == AnalysisName.POINTSTO) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.CORRECT;
		}
		// DumpSnapshot.printToFile(typeList, "a.a");
	}

}
