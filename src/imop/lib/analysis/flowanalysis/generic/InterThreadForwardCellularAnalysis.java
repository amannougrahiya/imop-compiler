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
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis.PointsToFlowMap;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis.PointsToGlobalState;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.typesystem.ArrayType;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.util.CellSet;
import imop.lib.util.ExtensibleCellMap;
import imop.lib.util.Immutable;
import imop.lib.util.ImmutableCellSet;
import imop.lib.util.ImmutableSet;
import imop.lib.util.Misc;
import imop.parser.Program;
import imop.parser.Program.StabilizationIDFAMode;

import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	public List<Long> counterList = new ArrayList<>();

	/**
	 * Perform the analysis, starting at the begin node of the function
	 * {@code funcDef}.<br>
	 * <i>Note that this method does not clear the analysis information, if already
	 * present.</i>
	 *
	 * @param funcDef function-definition on which this analysis has to be
	 *                performed.
	 */
	@Override
	public void run(FunctionDefinition funcDef) {
		// String typeList = "";
		BeginNode beginNode = funcDef.getInfo().getCFGInfo().getNestedCFG().getBegin();
		this.globalWorkList.recreate();
		this.globalWorkList.add(beginNode);
		do {
			Node nodeToBeAnalysed = this.globalWorkList.removeFirstElement();
			/*
			 * Old code below: This code is internally taken care of in
			 * ReversePostOrderWorklist.
			 */
			// if (nodeToBeAnalysed instanceof BarrierDirective || (nodeToBeAnalysed
			// instanceof EndNode
			// && nodeToBeAnalysed.getParent() instanceof ParallelConstruct)) {
			// // Process all barriers from the work-list.
			// Set<Node> barrList = new HashSet<>();
			// barrList.add(nodeToBeAnalysed);
			// while (true) {
			// Node nextNode = this.workList.peekFirstElement();
			// if (nextNode instanceof BarrierDirective
			// || (nextNode instanceof EndNode && nextNode.getParent() instanceof
			// ParallelConstruct)) {
			// nextNode = this.workList.removeFirstElement();
			// barrList.add(nextNode);
			// } else {
			// break;
			// }
			// }
			// for (Node barr : barrList) {
			// // typeList += barr.getClass().getSimpleName() + foo(barr) + ";\n ";
			// this.debugRecursion(barr);
			// this.processBarrierINWhenNotUpdated(barr);
			// }
			// for (Node barr : barrList) {
			// this.processBarrierOUTWhenNotUpdated(barr);
			// }
			// } else {
			/*
			 * Old code above.
			 */
			this.debugRecursion(nodeToBeAnalysed);
			this.processWhenNotUpdated(nodeToBeAnalysed);
			// } // Part of the old code.
		} while (!globalWorkList.isEmpty());
		if (this.analysisName == AnalysisName.POINTSTO) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.CORRECT;
		}
	}

	/**
	 * Processing that is performed when no updates have been done to the program.
	 * Note that IN objects will never change -- their value may change
	 * monotonically.
	 *
	 * @param node
	 * @param stateINChanged
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected final void processWhenNotUpdated(Node node) {
		boolean inOrOUTChanged = false;
		boolean proceedAhead = false; // To indicate whether the transfer function should be applied.
		NodeInfo nodeInfo = node.getInfo();
		Set<IDFAEdge> predecessors = nodeInfo.getCFGInfo()
			.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension());
		F newIN = (F) nodeInfo.getIN(analysisName);
		if (newIN == null) {
			/*
			 * In this scenario, we must set the propagation flag, so that all successors
			 * are processed at least once.
			 */
			inOrOUTChanged = true;
			if (predecessors.isEmpty()) {
				newIN = this.getEntryFact();
			} else {
				newIN = this.getTop();
			}
			proceedAhead = true;
		}

		CellSet accessedCells = null;
		if (Program.useAccessedCellsWithExhaustive) {
			accessedCells = node.getInfo().getAccessedCellSets(this);
		}
		CellularDataFlowAnalysis.accessedCellValueChanged = false;

		boolean inChanged = false;
		CellSet nonLocalCells = null;
		for (IDFAEdge idfaEdge : predecessors) {
			F predOUT = (F) idfaEdge.getNode().getInfo().getOUT(analysisName);
			if (predOUT == null) {
				continue;
			}
			F edgeOUT = this.edgeTransferFunction(predOUT, idfaEdge.getNode(), node);
			if (Program.useAccessedCellsWithExhaustive) {
				inChanged |= newIN.mergeWithAccessed(edgeOUT, idfaEdge.getCells(), accessedCells);
			} else {
				inChanged |= newIN.merge(edgeOUT, idfaEdge.getCells());
			}
			if (node instanceof PostCallNode) {
				if (nonLocalCells == null) {
					nonLocalCells = new CellSet();
				}
				// Capture nonLocalCells.
				nonLocalCells.addAll(predOUT.flowMap.keySet());
			}
		}

		if (node instanceof PostCallNode) {
			inChanged |= processPostCallNodes((PostCallNode) node, newIN, nonLocalCells);
		}

		if (Program.useAccessedCellsWithExhaustive) {
			if (CellularDataFlowAnalysis.accessedCellValueChanged) {
				proceedAhead = true;
			}
			if (node instanceof ParameterDeclaration) {
				proceedAhead = true; // Note that the value of arguments won't be present in the IN flow-map. Hence,
									 // conservatively, we set this flag.
			} else if (node instanceof PostCallNode) {
				proceedAhead = true;
				}
		}

		/**
		 * If the IN of a BarrierDirective has changed, we should add all its sibling
		 * barriers to the workList.
		 */
		if (inChanged && node instanceof BarrierDirective) {
			this.addAllSiblingBarriersToGlobalWorkList((BarrierDirective) node);
		}

		nodeInfo.setIN(analysisName, newIN);

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		CellularDataFlowAnalysis.nodeAnalysisStack.add(new NodeAnalysisPair(node, this));
		F oldOUT = (F) nodeInfo.getOUT(analysisName);
		F newOUT;
		if (Program.useAccessedCellsWithExhaustive) {
			if (node instanceof BarrierDirective) {
				newOUT = node.accept(this, newIN);
			} else {
				if (proceedAhead || (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition)) {
					newOUT = node.accept(this, newIN);
				} else {
					this.transferFunctionsSkipped++;
					if (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition) {
						newOUT = this.copyFlowMap(newIN);
					} else if (node instanceof EndNode && node.getParent() instanceof FunctionDefinition) {
						newOUT = this.copyFlowMap(newIN);
					} else if (node instanceof EndNode && node.getParent() instanceof CompoundStatement) {
						newOUT = this.copyFlowMap(newIN);
					} else if (node instanceof PostCallNode) {
						newOUT = this.copyFlowMap(newIN);
					} else {
						if (accessedCells.isEmpty()) {
							newOUT = newIN;
						} else {
							newOUT = this.copyFlowMap(newIN);
							for (Cell c : accessedCells) {
								if (oldOUT.flowMap.containsKey(c)) {
									newOUT.flowMap.putSpecial(c, oldOUT.flowMap, c);
								}
							}
						}
					}
				}
			}
		} else {
			newOUT = node.accept(this, newIN);
		}
		if (node instanceof EndNode) {
			processEndNodes((EndNode) node, newOUT);
		} else if (node instanceof BeginNode) {
			processBeginNodes((BeginNode) node, newOUT);
		}
		CellularDataFlowAnalysis.nodeAnalysisStack.pop();
		nodeInfo.setOUT(analysisName, newOUT);

		// if (Program.maintainImpactedCells) {
		// this.updateImpactedCells(node, newIN, newOUT);
		// }

		// Step 3: Process the successors, if needed.
		inOrOUTChanged |= inChanged;
		if (node instanceof BarrierDirective) {
			if (newOUT == newIN && inChanged) {
				assert (false); // OUT of a barrier can never be same object as its IN.
				inOrOUTChanged = true; // Redundant; kept for readability.
			} else if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
				inOrOUTChanged = true;
			}
		}
		if (node instanceof ParameterDeclaration) {
			// Added this code because finding whether the OUT of ParameterDeclaration has
			// changed, is complex.
			// Note that if the IN of a ParameterDeclaration does not change, the OUT may
			// still change (due to changes at call-sites).
			inOrOUTChanged = true;
		}
		if (inOrOUTChanged) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				Node n = idfaEdge.getNode();
				this.globalWorkList.add(n);
					}
			if (node instanceof PreCallNode) {
				PreCallNode pre = (PreCallNode) node;
				this.globalWorkList.add(pre.getParent().getPostCallNode());
			}
			// If we are adding successors of a BeginNode of a FunctionDefinition, we should
			// add all the ParameterDeclarations.
			if (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition) {
				FunctionDefinition func = (FunctionDefinition) node.getParent();
				for (ParameterDeclaration paramDecl : func.getInfo().getCFGInfo().getParameterDeclarationList()) {
					this.globalWorkList.add(paramDecl);
				}
			}
		}
	}

	// protected Set<Node> copyOfSeedNodesForFirstPhase = new HashSet<>();
	// protected Set<Node> safeNodesForFirstPhase = new HashSet<>();

	/**
	 * Processing that is performed when no updates have been done to the program.
	 * Note that IN objects will never change -- their value may change
	 * monotonically.
	 *
	 * @param barr
	 * @param stateINChanged
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	protected final void processBarrierINWhenNotUpdated(Node barr) {
		boolean propagateFurther = false;
		NodeInfo nodeInfo = barr.getInfo();
		Set<IDFAEdge> predecessors = nodeInfo.getCFGInfo()
			.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension());
		F newIN = (F) nodeInfo.getIN(analysisName);
		if (newIN == null) {
			/*
			 * In this scenario, we must set the propagation flag, so that all successors
			 * are processed at least once.
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
		 * If the IN of a BarrierDirective has changed, we should add all its sibling
		 * barriers to the workList.
		 */
		if (inChanged && barr instanceof BarrierDirective) {
			this.addAllSiblingBarriersToGlobalWorkList((BarrierDirective) barr);
		}
		nodeInfo.setIN(analysisName, newIN);

		propagateFurther |= inChanged;
		if (propagateFurther) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				Node n = idfaEdge.getNode();
				this.globalWorkList.add(n);
					}
		}
		return;
	}

	@SuppressWarnings("unchecked")
	@Deprecated
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
				this.globalWorkList.add(n);
					}
		}
		return;
	}

	/**
	 * Adds missing information from the OUT of the corresponding PreCallNode to the
	 * given {@code incompleteFF}, which is the final IN state of the given
	 * post-call node, {@code node}.
	 *
	 * @param node         a postCallNode.
	 * @param incompleteFF
	 */
	@SuppressWarnings("unchecked")
	private <H extends Immutable> boolean processPostCallNodes(PostCallNode node, F incompleteFF,
			CellSet nonLocalCells) {
		PreCallNode preNode = node.getParent().getPreCallNode();
		CellularFlowMap<H> postIN = (CellularFlowMap<H>) incompleteFF;
		CellularFlowMap<H> preOUT = (CellularFlowMap<H>) preNode.getInfo().getOUT(analysisName);
		if (preOUT == null || preOUT.getFlowMap() == null) {
			return false;
		}

		Set<Cell> keysInPost = postIN.getFlowMap().nonGenericKeySet();
		boolean changed = false;
		for (Cell preKey : preOUT.getFlowMap().nonGenericKeySet()) {
			// /*
			// * Debugging code starts.
			// */
			// if (node.getParent().toString().contains("norm2u3(_imopVarPre306, ") &&
			// preKey instanceof Symbol) {
			// Symbol sym = (Symbol) preKey;
			// if (sym.getName().contains("245")) {
			// System.err.println("Found 245.");
			// }
			// }
			// /*
			// * Debugging code ends.
			// */
			if (nonLocalCells != null && !nonLocalCells.contains(preKey)) {
				// i.e., if preKey is a local cell.
				H val = preOUT.getFlowMap().get(preKey);
				if (val == null) {
					continue;
				}
				changed = true;
				postIN.getFlowMap().put(preKey, val);
			} // TODO: Do we need an else branch here for meet?
		}
		return changed;
	}

	/**
	 * This method removes those entries from the flow-map {@code fullFF} that do
	 * not correspond to accessible symbols at the function which the given
	 * begin-node, {@code node}, begins.
	 *
	 * @param node   a begin-node
	 * @param fullFF state of the OUT of the begin-node; note that this object's
	 *               values may get modified.
	 */
	private void processBeginNodes(BeginNode node, F fullFF) {
		CellularFlowMap<?> cellFullFF = fullFF;
		if (node.getParent() instanceof FunctionDefinition) {
			Set<Cell> removalSet = new HashSet<>();
			FunctionDefinition funcDef = (FunctionDefinition) node.getParent();
			Set<Symbol> symHere = new HashSet<>(funcDef.getInfo().getSymbolTable().values());
			symHere.addAll(Program.getRoot().getInfo().getSymbolTable().values());
			for (Cell c : cellFullFF.getFlowMap().nonGenericKeySet()) {
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
				cellFullFF.getFlowMap().remove(removeCell);
			}
			// CellSet cellsHere = node.getInfo().getAllCellsAtNode();
			// removalSet.addAll(cellFullFF.flowMap.nonGenericKeySet().stream().filter(c ->
			// !cellsHere.contains(c))
			// .collect(Collectors.toList()));
		}
	}

	/**
	 * This method removes those entries from the flow-map {@code fullFF} which
	 * correspond to local keys of the scope that the given end-node, {@code node},
	 * ends.
	 *
	 * @param node   an end-node.
	 * @param fullFF state of OUT of the end-node; note that this object's values
	 *               may get modified.
	 */
	private void processEndNodes(EndNode node, F fullFF) {
		CellularFlowMap<?> cellFullFF = fullFF;
		if (node.getParent() instanceof CompoundStatement) {
			CompoundStatement parentCS = (CompoundStatement) node.getParent();
			for (Symbol key : parentCS.getInfo().getSymbolTable().values()) {
				if (!key.isStatic()) {
					cellFullFF.getFlowMap().remove(key);
					cellFullFF.getFlowMap().remove(key.getAddressCell());
					if (key.getType() instanceof ArrayType) {
						cellFullFF.getFlowMap().remove(key.getFieldCell());
					}
				}
			}
		} else if (node.getParent() instanceof FunctionDefinition) {
			FunctionDefinition funcDef = (FunctionDefinition) node.getParent();
			for (Symbol key : funcDef.getInfo().getSymbolTable().values()) {
				if (!key.isStatic()) {
					cellFullFF.getFlowMap().remove(key);
					cellFullFF.getFlowMap().remove(key.getAddressCell());
					if (key.getType() instanceof ArrayType) {
						cellFullFF.getFlowMap().remove(key.getFieldCell());
					}
				}
			}
		} else {
			return;
		}
	}

	@Override
	/*
	 * NOTE: FOR PTA, SEE THE OVERRIDDEN VERSION.
	 */
	public void restartAnalysisFromStoredNodes() {
		assert (!SCC.processingTarjan);
		if (this.analysisName == AnalysisName.POINTSTO
				&& PointsToAnalysis.stateOfPointsTo != PointsToGlobalState.STALE) {
			nodesToBeUpdated.clear();
			return;
				}
		if (Program.stabilizationIDFAMode == StabilizationIDFAMode.INCIDFA) {
			this.newStabilizationTriggerHandler();
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
		// this.safeNodesForFirstPhase.clear();
		// this.copyOfSeedNodesForFirstPhase.clear();
		// this.copyOfSeedNodesForFirstPhase.addAll(this.nodesToBeUpdated);
		long localTimer = System.nanoTime();

		/*
		 * From the set of nodes to be updated, we obtain the workList to be processed.
		 * We add all the entry points of the SCC of each node.
		 */
		this.globalWorkList.recreate();
		Set<Node> remSet = new HashSet<>();
		for (Node n : nodesToBeUpdated) {
			boolean added = this.globalWorkList.add(n);
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

		this.safeCurrentSCCNodes = new HashSet<>();
		this.underApproximated = new HashSet<>();
		while (!globalWorkList.isEmpty()) {
			Node nodeToBeAnalyzed = globalWorkList.removeFirstElement();
			CFGInfo nInfo = nodeToBeAnalyzed.getInfo().getCFGInfo();
			if (nInfo.getSCC() == null) {
				// Here, node itself is an SCC. We do not require two rounds.
				// Program.tempString += (nodeToBeAnalyzed.toString());
				this.nodesProcessedDuringUpdate++;
				this.debugRecursion(nodeToBeAnalyzed);
				this.processWhenNotUpdated(nodeToBeAnalyzed); // Directly invoke the second round processing.
				continue;
			} else {
				/*
				 * This node belongs to an SCC. We should process the whole of SCC in the first
				 * phase, followed by its processing in the second phase, and only then shall we
				 * move on to the next SCC.
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

	/**
	 * This method starts with the given node, and stabilizes the SCC corresponding
	 * to it before returning back.
	 *
	 * @param node node whose SCC has to be stabilized. Note that the node has
	 *             already been removed from the workList.
	 */
	@Override
	protected final void stabilizeSCCOf(Node node) {
		this.safeCurrentSCCNodes.clear();
		this.underApproximated.clear();

		SCC thisSCC = node.getInfo().getCFGInfo().getSCC();
		int thisSCCNum = node.getInfo().getCFGInfo().getSCCRPOIndex();
		// System.err.println("*** PROCESSING SCC #" + thisSCCNum);

		assert (thisSCC != null);

		if (Program.checkForGhostValues) {
			checkForGhostValues(node);
		}

		long localCounterPerPhase = this.nodesProcessedDuringUpdate;
		do {
			/*
			 * This conditional check is used to minimize the extra work that we perform
			 * during the first phase.
			 */
			// if (!this.safeCurrentSCCNodes.contains(node)) {
			// Program.tempString += (node.toString());
			this.nodesProcessedDuringUpdate++;
			this.debugRecursion(node);
			this.processWhenUpdated(node);
			// } else {
			// outer: for (IDFAEdge idfaEdge : node.getInfo().getCFGInfo()
			// .getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension()))
			// {
			// Node predNode = idfaEdge.getNode();
			// SCC predSCC = predNode.getInfo().getCFGInfo().getSCC();
			// if (predSCC == node.getInfo().getCFGInfo().getSCC()) {
			// if (!this.safeCurrentSCCNodes.contains(predNode)) {
			// this.underApproximated.add(node);
			// break outer;
			// }
			// }
			// }
			// // OR:
			// // this.underApproximated.add(node);
			// }
			node = this.globalWorkList.peekFirstElementOfSameSCC(thisSCCNum);
			if (node == null) {
				break;
			}
			SCC nextSCC = node.getInfo().getCFGInfo().getSCC();
			if (nextSCC == null || nextSCC != thisSCC) {
				// The next node belongs to a different SCC. Break from the first phase.
				break;
			} else {
				// Extract the next node and process it next.
				node = this.globalWorkList.removeFirstElementOfSameSCC(thisSCCNum);
			}
		} while (true);
		firstPhaseCount.add(this.nodesProcessedDuringUpdate - localCounterPerPhase);
		/*
		 * NEW CODE: To check the percent of nodes processed in an SCC in the first
		 * phase.
		 */
		if (Program.profileSCC) {
			int SCCSize = thisSCC.getNodeCount();
			double percentInFirst = this.safeCurrentSCCNodes.size() / (SCCSize * 1.0) * 100;
			if (SCCSize != 1) {
				System.out.println("Total " + Program.df2.format(percentInFirst) + "% of nodes processed out of "
						+ SCCSize + " in this SCC.");
			}
		}
		/*
		 * <-- NEW CODE Ends.
		 */

		// Now, the set processedInThisUpdate is not required anymore. Clear it.
		this.safeCurrentSCCNodes.clear();
		/*
		 * Next, let's start with the second phase, with all elements of
		 * yetToBeFinalized in the workList, where we proceed as usual.
		 */
		this.globalWorkList.addAll(this.underApproximated);
		this.underApproximated.clear();
		localCounterPerPhase = this.nodesProcessedDuringUpdate;
		do {
			node = this.globalWorkList.peekFirstElementOfSameSCC(thisSCCNum);
			if (node == null) {
				break;
			}
			SCC nextSCC = node.getInfo().getCFGInfo().getSCC();
			if (nextSCC == null || nextSCC != thisSCC) {
				// The next node belongs to a different SCC. Break from the second phase.
				break;
			} else {
				// Extract the next node and process it in the second phase.
				node = this.globalWorkList.removeFirstElementOfSameSCC(thisSCCNum);
			}
			// Program.tempString += (node.toString());
			this.nodesProcessedDuringUpdate++;
			this.debugRecursion(node);
			this.processWhenNotUpdated(node); // Note that this is a call to normal processing.
		} while (true);
		secondPhaseCount.add(this.nodesProcessedDuringUpdate - localCounterPerPhase);

		// if (node != null) {
		// int nextSCCNum = node.getInfo().getCFGInfo().getSCCRPOIndex();
		// System.err.println("*** ENCOUNTERED NEXT SCC with ID #" + nextSCCNum);
		// }
	}

	private void checkForGhostValues(Node node) {
		SCC thisSCC = node.getInfo().getCFGInfo().getSCC();
		assert (thisSCC != null);
		/*
		 * Find the node with least size of the cellmap.
		 */
		for (Node n : thisSCC.getNodes()) {
			@SuppressWarnings("unchecked")
			F ff = (F) n.getInfo().getIN(this.getAnalysisName());
			if (ff == null) {
				continue;
			}
			ExtensibleCellMap<?> flowMap = ff.getFlowMap();
			String retString = "";
			retString += "[";
			if (flowMap.isUniversal()) {
				retString += analysisName + "(globalCell) := " + flowMap.get(Cell.genericCell);
			}
			Set<Cell> cellSet = flowMap.nonGenericKeySet();
			List<Cell> cellList = new ArrayList<>(cellSet);
			Collections.sort(cellList);
			for (Cell c : cellList) {
				retString += analysisName + "(" + c.toString() + ") := ";
				Object value = flowMap.get(c);
				if (value != null && value != Cell.getNullCell()) {
					retString += System.identityHashCode(value);
				} else {
					retString += "NULL";
				}
				retString += ";\n";
			}
			retString += "]";
			System.out.println(retString);
		}
		System.out.println("***************");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected final void processWhenUpdated(Node node) {
		boolean first = false;
		if (!this.safeCurrentSCCNodes.contains(node)) {
			first = true;
			// Don't add the node to this set unless its OUT has been populated.
		}
		NodeInfo nodeInfo = node.getInfo();
		Set<IDFAEdge> predecessors = nodeInfo.getCFGInfo()
			.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension());
		F oldIN = (F) nodeInfo.getIN(analysisName);
		boolean inOrOUTChanged = false;
		F newIN;
		if (oldIN == null) {
			// First processing ever of this node!
			inOrOUTChanged = true;
			newIN = (predecessors.isEmpty()) ? this.getEntryFact() : this.getTop();
		} else if (first) {
			// First processing in the first round of update.
			newIN = (predecessors.isEmpty()) ? this.getEntryFact() : this.getTop();
		} else {
			newIN = oldIN; // Use the same object.
		}

		boolean inChanged = false;
		boolean anyPredMissed = false;
		CellSet nonLocalCells = null;
		for (IDFAEdge idfaEdge : predecessors) {
			Node predNode = idfaEdge.getNode();
			/*
			 * Ignore a predecessor if: (i) it lies within the SCC, AND (ii) it has not been
			 * processed yet.
			 */
			SCC predSCC = predNode.getInfo().getCFGInfo().getSCC();
			if (predSCC != null) {
				// If null, then pred clearly belongs to some other SCC.
				if (node.getInfo().getCFGInfo().getSCC() == predSCC) {
					// Predecessor lies within the SCC.
					if (!this.safeCurrentSCCNodes.contains(predNode)) {
						anyPredMissed = true;
						continue;
					}
				}
			}
			F predOUT = (F) predNode.getInfo().getOUT(analysisName);
			if (predOUT == null) {
				/*
				 * Here, we do not mark anyPredMissed, as this node would be marked for
				 * processing whenever the predecessor has been processed for the first time.
				 * Check the setting of inOrOUTChanged above.
				 */
				continue;
			}
			F edgeOUT = this.edgeTransferFunction(predOUT, idfaEdge.getNode(), node);
			inChanged |= newIN.merge(edgeOUT, idfaEdge.getCells());
			if (node instanceof PostCallNode) {
				if (nonLocalCells == null) {
					nonLocalCells = new CellSet();
				}
				// Capture nonLocalCells.
				nonLocalCells.addAll(predOUT.flowMap.keySet());
			}
		}

		if (node instanceof PostCallNode) {
			PreCallNode preNode = ((CallStatement) node.getParent()).getPreCallNode();
			SCC preSCC = preNode.getInfo().getCFGInfo().getSCC();
			boolean doNotProcess = false;
			if (preSCC != null) {
				// If null, then pred clearly belongs to some other SCC.
				if (node.getInfo().getCFGInfo().getSCC() == preSCC) {
					// Predecessor lies within the SCC.
					if (!this.safeCurrentSCCNodes.contains(preNode)) {
						anyPredMissed = true;
						doNotProcess = true;
						// this.underApproximated.add(node); // New code.
					}
				}
			}
			if (!doNotProcess) {
				inChanged |= processPostCallNodes((PostCallNode) node, newIN, nonLocalCells);
			}
		}
		if (anyPredMissed) {
			this.underApproximated.add(node);
		} else {
			this.underApproximated.remove(node); // MG: 13275 vs 13307 with this line.
		}
		/**
		 * If the IN of a BarrierDirective has changed, we should add all its sibling
		 * barriers to the workList.
		 */
		if (node instanceof BarrierDirective) {
			if (oldIN == null) {
				// This was the first processing of the barrier, ever. Others would get affected
				// only if the in was changed.
				if (inChanged) {
					this.addAllSiblingBarriersToGlobalWorkList((BarrierDirective) node);
				}
			} else if (oldIN != newIN) {
				// This was the first processing of the barrier in this round. If the objects
				// differ semantically, others would get affected.
				if (!oldIN.isEqualTo(newIN)) {
					this.addAllSiblingBarriersToGlobalWorkList((BarrierDirective) node);
				}
			} else {
				// This is any-but-first processing of the barrier in this round. Others would
				// be impacted only if anything changed in IN.
				if (inChanged) {
					this.addAllSiblingBarriersToGlobalWorkList((BarrierDirective) node);
				}
			}
		}

		nodeInfo.setIN(analysisName, newIN);

		// if (oldIN != null && oldIN != newIN && !oldIN.isEqualTo(newIN)) {
		// inOrOUTChanged = true;
		// }
		// NEW, RECTIFIED CODE:
		if (oldIN != null && oldIN != newIN) {
			inChanged = !oldIN.isEqualTo(newIN);
		}

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		long localTimer = System.nanoTime();
		InterThreadForwardCellularAnalysis.innerCount++;
		F oldOUT = (F) nodeInfo.getOUT(analysisName);
		F newOUT;
		if (node instanceof BarrierDirective) {
			newOUT = this.visitChanged((BarrierDirective) node, newIN, first);
		} else {
			localTimer = System.nanoTime();
			newOUT = node.accept(this, newIN);
			InterThreadForwardCellularAnalysis.innerTimer += (System.nanoTime() - localTimer);
			if (node instanceof EndNode) {
				processEndNodes((EndNode) node, newOUT);
			} else if (node instanceof BeginNode) {
				processBeginNodes((BeginNode) node, newOUT);
			}
		}
		nodeInfo.setOUT(analysisName, newOUT);
		this.safeCurrentSCCNodes.add(node); // Mark a node as processed only after its OUT has been "purified".

		// if (Program.maintainImpactedCells) {
		// this.updateImpactedCells(node, newIN, newOUT);
		// }

		// Step 3: Process the successors, if needed.
		inOrOUTChanged |= inChanged;
		if (node instanceof BarrierDirective) {
			if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
				inOrOUTChanged = true;
			}
		}
		if (node instanceof ParameterDeclaration) {
			// Added this code because finding whether the OUT of ParameterDeclaration has
			// changed, is complex.
			// Note that if the IN of a ParameterDeclaration does not change, the OUT may
			// still change (due to changes at call-sites).
			inOrOUTChanged = true;
		}
		if (inOrOUTChanged) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				Node n = idfaEdge.getNode();
				this.globalWorkList.add(n);
					}
			if (node instanceof PreCallNode) {
				PreCallNode pre = (PreCallNode) node;
				this.globalWorkList.add(pre.getParent().getPostCallNode());
			}
			// If we are adding successors of a BeginNode of a FunctionDefinition, we should
			// add all the ParameterDeclarations.
			if (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition) {
				FunctionDefinition func = (FunctionDefinition) node.getParent();
				for (ParameterDeclaration paramDecl : func.getInfo().getCFGInfo().getParameterDeclarationList()) {
					this.globalWorkList.add(paramDecl);
				}
			}
		} else if (Program.testSafeMarkingHeuristic) {
			/*
			 * Mon Nov 1 20:35:26 IST 2021 TODO: TEST THIS CODE; ADDED AS A HEURISTIC FOR
			 * EARLY TERMINATION OF FIRST PASS. This code was thought about while writing
			 * the paper on HIDFAp (or IncIDFA). If the current node has "stabilized"
			 * without requiring any changes on its initial state and if we aren't planning
			 * on processing its successors, we should perform the following when, the
			 * worklist contains at least one other element to be processed in the same SCC:
			 * Mark all those nodes as "processed" for which this node is a dominator.
			 *
			 */
			// if
			// (this.workList.peekFirstElementOfSameSCC(node.getInfo().getCFGInfo().getSCCRPOIndex())
			// != null) {
			// SCC thisSCC = node.getInfo().getCFGInfo().getSCC();
			// Set<Node> graySet = new HashSet<>();
			// for (IDFAEdge idfaEdge : node.getInfo().getCFGInfo()
			// .getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
			// Node child = idfaEdge.getNode();
			// if (!this.processedInThisUpdate.contains(child) &&
			// !this.safeNodesForFirstPhase.contains(child)) {
			// graySet.add(child);
			// }
			// }
			// while (!graySet.isEmpty()) {
			// Node currNode = Misc.getAnyElement(graySet);
			// graySet.remove(currNode);
			// SCC childSCC = currNode.getInfo().getCFGInfo().getSCC();
			// if (childSCC == null || childSCC != thisSCC) {
			// continue;
			// }
			// if (this.isNodeSafe(currNode)) {
			// /*
			// * Add currNode to the processed set, and put its children that aren't already
			// * processed in the workset, and which belong to this set.
			// */
			// this.safeNodesForFirstPhase.add(currNode);
			// for (IDFAEdge idfaEdge : currNode.getInfo().getCFGInfo()
			// .getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
			// Node child = idfaEdge.getNode();
			// if (!this.processedInThisUpdate.contains(child)
			// && !this.safeNodesForFirstPhase.contains(child)) {
			// graySet.add(child);
			// }
			// }
			// } else {
			// // Stop traversal here, for this path. Do nothing.
			// }
			// } // End of the marking loop.
			// }
		}
	}

	public F copyFlowMap(F newIN) {
		F top = this.getTop();
		top.initFallBackForExtensibleCellMapWith(newIN.getFlowMap());
		return top;
		// long timer = System.nanoTime();
		// for (long temp = 0; temp < 1e7; temp++) {
		// top = this.getTop();
		// top.initExtensibleCellMapWith(newIN.getFlowMap()); // A cheaper alternative
		// // top.merge(newIN, null); // A much costlier alternative
		// }
		// DecimalFormat df = Program.df2;
		// System.err.println("Time: " + df.format((System.nanoTime() - timer) / 1e9) +
		// "s.");
		// return top;
	}

	@SuppressWarnings("unchecked")
	protected F getCopyOfFlowMap(F newIN) {
		Constructor<?> copyConstructor = null;
		try {
			Class<?> classOfFlowMap = newIN.getClass();
			copyConstructor = classOfFlowMap.getConstructor(CellularFlowMap.class);
			return (F) copyConstructor.newInstance(newIN);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public List<Long> firstPhaseCount = new ArrayList<>();
	public List<Long> secondPhaseCount = new ArrayList<>();

	protected final void stabilizeSCCOfInOnePassAfterInit(Node node) {
		SCC thisSCC = node.getInfo().getCFGInfo().getSCC();
		int thisSCCNum = node.getInfo().getCFGInfo().getSCCRPOIndex();
		assert (thisSCC != null);
		long localCounterPerPhase = this.nodesProcessedDuringUpdate;
		do {
			this.nodesProcessedDuringUpdate++;
			this.debugRecursion(node);
			this.processWhenNotUpdated(node); // Note that this is a call to normal processing.
			node = this.globalWorkList.peekFirstElementOfSameSCC(thisSCCNum);
			if (node == null) {
				break;
			}
			SCC nextSCC = node.getInfo().getCFGInfo().getSCC();
			if (nextSCC == null || nextSCC != thisSCC) {
				// The next node belongs to a different SCC. Break from the second phase.
				break;
			} else {
				// Extract the next node and process it in the second phase.
				node = this.globalWorkList.removeFirstElementOfSameSCC(thisSCCNum);
			}
		} while (true);
		secondPhaseCount.add(this.nodesProcessedDuringUpdate - localCounterPerPhase);
	}

	protected final void stabilizeSCCOfInOnePassWithoutInit(Node node, Set<Node> visited) {
		SCC thisSCC = node.getInfo().getCFGInfo().getSCC();
		int thisSCCNum = node.getInfo().getCFGInfo().getSCCRPOIndex();
		assert (thisSCC != null);
		long localCounterPerPhase = this.nodesProcessedDuringUpdate;
		do {
			this.nodesProcessedDuringUpdate++;
			this.debugRecursion(node);
			this.processWhenNotUpdated(node); // Note that this is a call to normal processing.
			node = this.globalWorkList.peekFirstElementOfSameSCC(thisSCCNum);
			if (node == null) {
				break;
			}
			SCC nextSCC = node.getInfo().getCFGInfo().getSCC();
			if (nextSCC == null || nextSCC != thisSCC) {
				// The next node belongs to a different SCC. Break from the second phase.
				break;
			} else {
				// Extract the next node and process it in the second phase.
				node = this.globalWorkList.removeFirstElementOfSameSCC(thisSCCNum);
			}
			if (!visited.contains(node)) {
				node.getInfo().removeAnalysisInformation(AnalysisName.POINTSTO);
				visited.add(node);
			}
		} while (true);
		secondPhaseCount.add(this.nodesProcessedDuringUpdate - localCounterPerPhase);
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

	// protected Set<Node> copyOfSeedNodesForFirstPhase = new HashSet<>();
	// protected Set<Node> safeNodesForFirstPhase = new HashSet<>();

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
					if (siblingBarrier != n && !this.safeCurrentSCCNodes.contains(siblingBarrier)) {
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
			this.underApproximated.add(n);
		} else {
			this.underApproximated.remove(n);
		}
		return newOUT;
	}

	private boolean isNodeSafe(Node node) {
		if (this.globalWorkList.contains(node)) {
			return false;
		}
		if (node.getInfo().getCFGInfo().getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension())
				.stream().anyMatch(e -> !this.safeCurrentSCCNodes.contains(e.getNode()))) {
			return false;
				}
		return true;
	}

	/**
	 * Capture all those cells that are (i) present in OUT but not IN, or (ii)
	 * contain possibly-different values for IN and OUT.
	 *
	 * @param n
	 * @param IN
	 * @param OUT
	 */
	@Deprecated
	protected void updateImpactedCells(Node n, F IN, F OUT) {
		Set<Cell> outCellSet = OUT.getFlowMap().keySet();
		CellSet impacted = n.getInfo().getImpactedSet();
		for (Cell c : outCellSet) {
			Object inVal = IN.getFlowMap().get(c);
			if (inVal == null) {
				continue;
			}
			Object outVal = OUT.getFlowMap().get(c);
			if (inVal != outVal) {
				/*
				 * Note that we only do a reference comparison here, hoping that the immutable
				 * values are being reused. (There's nothing incorrect, but inefficient, with
				 * marking more cells as impacted.)
				 */
				impacted.add(c);
			}
		}
	}

	/**
	 * Given a node connected to the program, this method ensures that the IN and
	 * OUT of all the leaf nodes within the given node are set to be the meet of OUT
	 * of all the predecessors of the entry point of the node (there must be just
	 * one entry point, and no internal barriers/flushes). Note that since the
	 * {@code affectedNode} has been checked to not affect the flow-analysis, we
	 * should use the same object for IN as well as OUT of each node.<br>
	 * Important: If the OUT of predecessors of the entry node is not stable, we
	 * will still use the unstable values for now; not that the stabilization
	 * process, whenever triggered, would take care of these nodes as well.
	 *
	 * @param affectedNode node which does not seem to affect the analysis (and
	 *                     hence, for which we just copy same IN and OUT to all its
	 *                     internal nodes).
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
				if (!safeCurrentSCCNodes.contains(siblingBarrier)) {
					if (reachablePredecessorsOfSeeds.containsKey(n)) {
						if (reachablePredecessorsOfSeeds.get(n).contains(siblingBarrier)) {
							underApproximated.add(n);
							anyOUTignored = true;
							continue;
						}
					} else {
						underApproximated.add(n);
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
			this.underApproximated.remove(n);
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
					this.globalWorkList.add(siblingBarrier);
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
					this.globalWorkList.add(siblingBarrier);
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
		 * Step 0: Collect information about reachable predecessor for seed nodes.
		 */
		this.populateReachablePredecessorMap();
		/*
		 * Step 1: Start processing from the leaf elements of updateSeedSet in "update"
		 * mode.
		 */
		this.globalWorkList.recreate();
		for (Node n : nodesToBeUpdated) {
			if (n.getInfo().isConnectedToProgram()) {
				this.globalWorkList.add(n);
			}
		}
		// this.workList.addAll(nodesToBeUpdated);
		this.nodesToBeUpdated.clear();
		this.safeCurrentSCCNodes = new HashSet<>();
		this.underApproximated = new HashSet<>();
		while (!globalWorkList.isEmpty()) {
			this.nodesProcessedDuringUpdate++;
			Node nodeToBeAnalysed = this.globalWorkList.removeFirstElement();
			this.debugRecursion(nodeToBeAnalysed);
			this.processWhenUpdated(nodeToBeAnalysed);
		}

		/****************************/
		/*
		 * Step 2: Reprocess those nodes whose IDFA is not yet finalized, in "normal"
		 * mode.
		 */
		this.globalWorkList.addAll(underApproximated);
		while (!globalWorkList.isEmpty()) {
			this.nodesProcessedDuringUpdate++;
			Node nodeToBeAnalysed = this.globalWorkList.removeFirstElement();
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
	 * objects would be reinitialized for all encountered nodes.
	 *
	 * @param node
	 * @param stateINChanged
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	protected final void deprecated_processWhenUpdated(Node node) {
		boolean first = false;
		if (!this.safeCurrentSCCNodes.contains(node)) {
			this.safeCurrentSCCNodes.add(node);
			first = true;
		}
		NodeInfo nodeInfo = node.getInfo();
		Set<IDFAEdge> predecessors = nodeInfo.getCFGInfo()
			.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension());
		F oldIN = (F) nodeInfo.getIN(analysisName);
		F newIN = predecessors.isEmpty() ? this.getEntryFact() : this.getTop();

		boolean anyOUTignored = false;
		for (IDFAEdge idfaEdge : predecessors) {
			if (!safeCurrentSCCNodes.contains(idfaEdge.getNode())) {
				if (reachablePredecessorsOfSeeds.containsKey(node)) {
					if (reachablePredecessorsOfSeeds.get(node).contains(idfaEdge.getNode())) {
						underApproximated.add(node);
						anyOUTignored = true;
						continue;
					}
				} else {
					underApproximated.add(node);
					anyOUTignored = true;
					continue;
				}
			}
			F predOUT = (F) idfaEdge.getNode().getInfo().getOUT(analysisName);
			if (predOUT == null) {
				continue;
			}
			/*
			 * Ignore the "may be imprecise" OUT of all those predecessors that have not
			 * been processed since this update yet. There are two scenarios now: (i) we
			 * will encounter this node again, after updating the OUT of this predecessor
			 * upon processing it, OR (ii) the IDFA flow-facts will stabilize well before we
			 * get a chance to process this node again. In order to ensure that the final
			 * value of this flow-fact is correct even if we encounter scenario "(ii)"
			 * above, we should add this node to the list of nodes that should be processed
			 * once more, in NO-PROGRAM-UPDATE mode.
			 */
			F edgeOUT = this.edgeTransferFunction(predOUT, idfaEdge.getNode(), node);
			newIN.merge(edgeOUT, idfaEdge.getCells());
		}
		if (node instanceof PostCallNode) {
			processPostCallNodes((PostCallNode) node, newIN, new CellSet());
		}
		if (!anyOUTignored) {
			this.underApproximated.remove(node);
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
				this.globalWorkList.add(idfaEdge.getNode());
					}
			if (node instanceof PreCallNode) {
				PreCallNode pre = (PreCallNode) node;
				this.globalWorkList.add(pre.getParent().getPostCallNode());
			}

		}
	}

	/**
	 * Perform the analysis, starting at the begin node of the function
	 * {@code funcDef}.<br>
	 * <i>Note that this method does not clear the analysis information, if already
	 * present.</i>
	 *
	 * @param funcDef function-definition on which this analysis has to be
	 *                performed.
	 */
	@Deprecated
	public void deprecated_run(FunctionDefinition funcDef) {
		// String typeList = "";
		BeginNode beginNode = funcDef.getInfo().getCFGInfo().getNestedCFG().getBegin();
		this.globalWorkList.recreate();
		this.globalWorkList.add(beginNode);
		do {
			Node nodeToBeAnalysed = this.globalWorkList.removeFirstElement();
			// typeList += nodeToBeAnalysed.getClass().getSimpleName() +
			// foo(nodeToBeAnalysed) + ";\n ";
			this.debugRecursion(nodeToBeAnalysed);
			this.processWhenNotUpdated(nodeToBeAnalysed);
		} while (!globalWorkList.isEmpty());
		if (this.analysisName == AnalysisName.POINTSTO) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.CORRECT;
		}
		// DumpSnapshot.printToFile(typeList, "a.a");
	}

	@Deprecated
	public void deprecated_processKeysInSCC() {
		for (SCC scc : SCC.allSCCs) {
			scc.processKeyDependenceGraphForAnalysis(this, scc.getNodes());
		}
	}

	@Deprecated
	private static enum PhaseOfFirstPass {
		PHASEONE, PHASETWO
	}

	/**
	 * Add all the entry nodes of the SCC of the given node, to the workList.
	 *
	 * @param seed
	 * @param workList
	 */
	protected void addEntryNodesForSCCOf(Node seed, ReversePostOrderWorkList workList) {
		SCC nSCC = seed.getInfo().getCFGInfo().getSCC();
		if (nSCC == null) {
			return;
		}
		workList.addAll(nSCC.getEntryNodes());
	}

	private static boolean seen = false;

	/**
	 * Latest algorithm for achieving incremental version of any generic (forward)
	 * IDFA. Tue Feb 22 16:58:01 IST 2022
	 */
	protected void newStabilizationTriggerHandler() {
		this.autoUpdateTriggerCounter++;
		/*
		 * [Homeostasis-specific] Mark start of next epoch of PTA stabilization.
		 */
		if (this.analysisName == AnalysisName.POINTSTO) {
			PointsToAnalysis.enableHeuristic();
		}
		long localTimer = System.nanoTime();

		// Capture the number of nodes "processed".
		long thisUpdateNodeCounter = this.nodesProcessedDuringUpdate;
		/*
		 * Step 1: Recreate the global-worklist. Add seed nodes to the global-worklist,
		 * along with BeginNode and EndNode of enclosing block of any seed node that is
		 * a Declaration.
		 */
		this.globalWorkList.recreate();
		Set<Node> remSet = new HashSet<>();
		for (Node n : nodesToBeUpdated) {
			boolean added = this.globalWorkList.add(n);
			if (added) {
				remSet.add(n);
				/*
				 * New code: If n is a Declaration, add the BeginNode and EndNode of its scope
				 * to the worklist.
				 */
				if (n instanceof Declaration) {
					Scopeable definingScope = Misc.getEnclosingBlock(n);
					if (definingScope != null && definingScope instanceof CompoundStatement) {
						this.globalWorkList.add(
								((CompoundStatement) definingScope).getInfo().getCFGInfo().getNestedCFG().getBegin());
						this.globalWorkList.add(
								((CompoundStatement) definingScope).getInfo().getCFGInfo().getNestedCFG().getEnd());
					}
				}
			}
		}
		this.nodesToBeUpdated.removeAll(remSet);
		if (Program.profileSCC) {
			System.out.println("Size of the seed set for trigger #" + autoUpdateTriggerCounter + ": " + remSet.size());
		}

		/*
		 * A new piece of code starts: Count the number of SCCs (with cycles) that have
		 * seed nodes.
		 */
		if (Program.countSeededSCCs && Program.profileSCC) {
			Set<SCC> seededSCCs = new HashSet<>();
			for (Node n : globalWorkList.getIteratorForNonBarrierNodes()) {
				SCC thisSCC = n.getInfo().getCFGInfo().getSCC();
				if (thisSCC != null) {
					seededSCCs.add(thisSCC);
				}
			}
			System.out.println("Seeded SCCs:");
			for (SCC scc : seededSCCs) {
				System.out.print("ID: " + scc.id);
			}
			System.out.println("Seeded SCC: " + seededSCCs.size());
		}
		/*
		 * .. New code for counter ends.
		 */

		/**
		 * Main driver loop for the incremental IDFA algorithm. This loop picks one SCC
		 * at a time.
		 */
		int processedSCCCount = 0;
		if (this.analysisName == AnalysisName.POINTSTO) {
			Program.basePointsTo = false; // Not needed for second and further runs.
			Program.memoizeAccesses++;
		}
		if (Program.checkForCyclesInKeyDependenceGraph) {
			System.out.println("Printing information for a trigger.");
		}
		while (!this.globalWorkList.isEmpty()) {
			Node nodeToBeAnalyzed = this.globalWorkList.removeFirstElement();
			CFGInfo nInfo = nodeToBeAnalyzed.getInfo().getCFGInfo();
			if (nInfo.getSCC() == null) {
				// Here, node itself is an SCC. We do not require two rounds.
				this.nodesProcessedDuringUpdate++;
				this.debugRecursion(nodeToBeAnalyzed);
				this.processNonSCCNodeOptimized(nodeToBeAnalyzed); // Directly invoke the second round processing.
			} else {
				/*
				 * This node belongs to an SCC. We should process the whole of till fixed-point
				 * in various phases, and only then shall we move on to the next SCC.
				 */
				processedSCCCount++;
				this.stabilizeSCCOfOptimized(nodeToBeAnalyzed);
			}
		}
		localTimer = System.nanoTime() - localTimer;
		if (Program.profileSCC) {
			System.out.println("Total SCCs processed: " + processedSCCCount + " out of " + SCC.getAllSCCSize());
		}
		if (this.analysisName == AnalysisName.POINTSTO) {
			Program.memoizeAccesses--;
		}

		counterList.add(this.nodesProcessedDuringUpdate - thisUpdateNodeCounter); // Total number of nodes processed in
																				  // this stabilization trigger.
		if (Program.checkForCyclesInKeyDependenceGraph) {
			System.out.println("Time spent in this trigger: " + localTimer / 1e9 + "s.");
		}
		this.flowAnalysisUpdateTimer += localTimer;
		if (this.analysisName == AnalysisName.POINTSTO
				&& PointsToAnalysis.stateOfPointsTo == PointsToGlobalState.STALE) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.CORRECT;
				}
		/*
		 * New Code: To check the key dependencies.
		 */
		// this.processKeysInSCC();
	}

	/**
	 * This method starts with the given node, and stabilizes the SCC corresponding
	 * to it before returning back.
	 *
	 * @param node node whose SCC has to be stabilized. Note that the node has
	 *             already been removed from the workList.
	 */
	protected final void stabilizeSCCOfOptimized(Node node) {
		// System.err.println("PROCESSING SCC ID#" +
		// node.getInfo().getCFGInfo().getSCCRPOIndex());
		// Node copyNode = node;
		SCC thisSCC = node.getInfo().getCFGInfo().getSCC();
		assert (thisSCC != null);
		int thisSCCNum = node.getInfo().getCFGInfo().getSCCRPOIndex();

		/*
		 * Step: Initialize safeCurrentSCCNodes, underApproximated, and thisSeeds, as
		 * per the SCC of the current node. Note that the node has already been removed
		 * from the SCC.
		 */
		this.safeCurrentSCCNodes.clear();
		this.underApproximated.clear();
		this.thisSeeds.clear();
		this.thisSeeds.add(node);
		do {
			Node tempNode = this.globalWorkList.removeFirstElementOfSameSCC(thisSCCNum);
			if (tempNode == null) {
				break;
			}
			this.thisSeeds.add(tempNode);
		} while (true);
		// Now, all seed nodes of this SCC are present in this.thisSeeds.
		boolean foundACycle = true;
		if (Program.checkForCyclesInKeyDependenceGraph) {
			boolean cycleInSCC = thisSCC.processKeyDependenceGraphForAnalysis(this, this.thisSeeds);
			if (!cycleInSCC) {
				System.out.println("\t NO cycle found in " + thisSCC.id);
				foundACycle = false;
			} else {
				System.out.println("\t Cycle found in " + thisSCC.id);
			}
		}

		/*
		 * Step: Take snapshot of OUT of all exit nodes of thisSCC.
		 *
		 * NOTE: This step relies on the property that there will be no in-place changes
		 * in the OUT object of any node during stabilization of its SCC. Otherwise we
		 * will have to store a copy of these OUT values. TODO: Verify this property.
		 */
		Map<Node, CellularFlowMap<?>> snapShotOfOut = new HashMap<>();
		for (Node exit : thisSCC.getExitNodes()) {
			CellularFlowMap<?> oldOUT = (CellularFlowMap<?>) exit.getInfo().getCurrentOUT(this.analysisName);
			if (oldOUT == null) {
				continue;
			}
			snapShotOfOut.put(exit, oldOUT);
		}

		/*
		 * Profiling code below. Ignore.
		 */
		if (Program.checkForGhostValues) {
			checkForGhostValues(node);
		}
		/*
		 * Profiling code ends.
		 */

		/**
		 * FIRST PASS: Contains two phases -- PHASE-I and PHASE-II. In PHASE-I, we start
		 * from the entry-nodes and move till seed-nodes or exit within this SCC. We
		 * ensure that each reachable node on those paths is processed at least once. In
		 * PHASE-II, we start with the seed-nodes and proceed until there is not change
		 * within the SCC nodes.
		 */
		long localCounterPerPhase = this.nodesProcessedDuringUpdate;
		// long totalThisSCC = 0;
		if (!Program.checkForCyclesInKeyDependenceGraph || foundACycle) {
			/*
			 * FIRST PASS, PHASE-I. We start only with the entry-nodes. Not all nodes are
			 * considered safe. May write to underApproximated.
			 */
			this.intraSCCWorkList.recreate();
			if (Program.runFirstPartOfFirstPass) {
				this.addEntryNodesForSCCOf(node, this.intraSCCWorkList);
				do {
					Node tempNode = this.intraSCCWorkList.removeFirstElementOfSameSCC(thisSCCNum);
					if (tempNode == null) {
						break;
					}
					// if (this.safeCurrentSCCNodes.contains(tempNode)) {
					// this.underApproximated.add(tempNode);
					// continue;
					// }
					// Program.tempString += (tempNode.toString());
					this.nodesProcessedDuringUpdate++;
					this.debugRecursion(tempNode);
					this.processWhenUpdatedOptimized(tempNode, thisSCC, true);
				} while (true);

				FlowAnalysis.nodes += "***\n";
				assert (this.intraSCCWorkList.isEmpty());
			}
			/*
			 * FIRST PASS, PHASE-II. All seed-nodes contain at least partial (non-empty)
			 * information. Not all nodes are considered safe. May write to
			 * underApproximated. The phase ends when all nodes are guaranteed to contain
			 * safe values.
			 */
			this.intraSCCWorkList.addAll(this.thisSeeds);
			do {
				Node tempNode = this.intraSCCWorkList.removeFirstElementOfSameSCC(thisSCCNum);
				if (tempNode == null) {
					break;
				}
				if (this.safeCurrentSCCNodes.contains(tempNode)) {
					this.underApproximated.add(tempNode);
					continue;
				}
				// Program.tempString += (tempNode.toString());
				this.nodesProcessedDuringUpdate++;
				this.debugRecursion(tempNode);
				this.processWhenUpdatedOptimized(tempNode, thisSCC, false);
			} while (true);

			firstPhaseCount.add(this.nodesProcessedDuringUpdate - localCounterPerPhase);
			// totalThisSCC += (this.nodesProcessedDuringUpdate - localCounterPerPhase);
			/*
			 * NEW CODE: To check the percent of nodes processed in an SCC in the first
			 * phase.
			 */
			if (Program.profileSCC) {
				int SCCSize = thisSCC.getNodeCount();
				double percentInFirst = this.safeCurrentSCCNodes.size() / (SCCSize * 1.0) * 100;
				if (SCCSize != 1) {
					System.out.println("Total " + Program.df2.format(percentInFirst)
							+ "% of unique nodes processed out of " + SCCSize + " in this SCC.");
				}
			}
			/*
			 * <-- NEW CODE Ends.
			 */
		}

		/**
		 * SECOND PASS. Note that at the end of FIRST PASS, we will have a set of nodes
		 * that are "underApproximated" -- they are processed in the SECOND PASS. All
		 * nodes are considered safe. Here we start with the underApproximated nodes and
		 * reach the fixed-point for the complete SCC.
		 */
		this.safeCurrentSCCNodes.clear();
		this.intraSCCWorkList.addAll(this.underApproximated);
		this.underApproximated.clear();
		if (Program.checkForCyclesInKeyDependenceGraph && !foundACycle) {
			this.intraSCCWorkList.addAll(this.thisSeeds);
		}
		localCounterPerPhase = this.nodesProcessedDuringUpdate;
		do {
			node = this.intraSCCWorkList.removeFirstElementOfSameSCC(thisSCCNum);
			if (node == null) {
				break;
			}
			SCC nextSCC = node.getInfo().getCFGInfo().getSCC();
			assert (thisSCC == nextSCC);
			// Program.tempString += (node.toString());
			this.nodesProcessedDuringUpdate++;
			this.debugRecursion(node);
			this.processWhenNotUpdatedOptimized(node, thisSCC); // Note that this is a call to normal processing.
		} while (true);
		secondPhaseCount.add(this.nodesProcessedDuringUpdate - localCounterPerPhase);
		// totalThisSCC += (this.nodesProcessedDuringUpdate - localCounterPerPhase);

		/*
		 * Step: Add nodes of successor SCCs, if the OUT of exit nodes has changed.
		 */
		Set<Node> exitsToBePropagated = new HashSet<>();
		for (Node exit : snapShotOfOut.keySet()) {
			CellularFlowMap<?> oldOUT = snapShotOfOut.get(exit);
			CellularFlowMap<?> newOUT = (CellularFlowMap<?>) exit.getInfo().getCurrentOUT(this.analysisName);
			if (newOUT != oldOUT && !newOUT.equals(oldOUT)) {
				exitsToBePropagated.add(exit);
			}
		}
		// All those nodes that are exit nodes but for which there was no oldOUT.
		exitsToBePropagated.addAll(Misc.setMinus(thisSCC.getExitNodes(), snapShotOfOut.keySet()));

		// Add successors of the selected exit node, from other SCCs, to the global
		// worklist.
		for (Node exit : exitsToBePropagated) {
			for (IDFAEdge idfaEdge : exit.getInfo().getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				Node n = idfaEdge.getNode();
				SCC otherSCC = n.getInfo().getCFGInfo().getSCC();
				if (otherSCC != thisSCC) {
					this.globalWorkList.add(n);
				}
					}
		}
		// System.err.println("Finished processing SCC ID#" +
		// copyNode.getInfo().getCFGInfo().getSCCRPOIndex() + " having "
		// + copyNode.getInfo().getCFGInfo().getSCC().getNodeCount() + " nodes, " + "by
		// processing a total of "
		// + totalThisSCC + " nodes.");
	}

	public static long innerTimer = 0;
	public static int innerCount = 0;

	/**
	 * Process the path from entry nodes to the seed nodes of an SCC (available in
	 * this.thisSeeds). This method takes one such node at a time (excluding the
	 * seeds). This method does not add nodes of other SCCs in the worklist. The
	 * worklist being used is intraSCCWorklist. This worklist was initialized with
	 * the entry nodes of "thisSCC". The successors are always added to the
	 * worklist, as long as they belong to the same SCC, and are not the seed nodes.
	 * When a node is processed for the first time in this stabilization trigger, a
	 * fresh object is always created for the IN object.
	 *
	 * @param node
	 * @param thisSCC
	 * @param phaseOne when set to {@code true}, traversal is performed
	 *                 unconditionally till the current set of seed nodes.
	 */
	@SuppressWarnings("unchecked")
	protected final void processWhenUpdatedOptimized(Node node, SCC thisSCC, boolean phaseOne) {
		boolean first = false;
		if (!this.safeCurrentSCCNodes.contains(node)) {
			first = true;
			// Don't add the node to this set unless its OUT has been populated.
		}
		NodeInfo nodeInfo = node.getInfo();
		Set<IDFAEdge> predecessors = nodeInfo.getCFGInfo()
			.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension());
		F oldIN = (F) nodeInfo.getIN(analysisName);
		F newIN;
		boolean inChanged = false; // To indicate whether the IN has changed.
		boolean proceedAhead = false; // To indicate whether the transfer function should be applied.
		boolean firstProcessingOfExistingNode = false;
		if (oldIN == null) {
			// First processing ever of this node!
			inChanged = true;
			newIN = (predecessors.isEmpty()) ? this.getEntryFact() : this.getTop();
			proceedAhead = true; // Apply the transfer function.
		} else if (first) {
			if (phaseOne) {
				inChanged = true; // This is done to ensure that each node is accessed at least once.
			}
			firstProcessingOfExistingNode = true;
			// First processing in the first round of update.
			newIN = (predecessors.isEmpty()) ? this.getEntryFact() : this.getTop();
		} else {
			newIN = oldIN; // Use the same object.
		}

		CellSet accessedCells = node.getInfo().getAccessedCellSets(this);
		CellularDataFlowAnalysis.accessedCellValueChanged = false;
		boolean anyPredMissed = false;
		CellSet nonLocalCells = null;
		for (IDFAEdge idfaEdge : predecessors) {
			Node predNode = idfaEdge.getNode();
			/*
			 * Ignore a predecessor if: (i) it lies within the SCC, AND (ii) it has not been
			 * processed yet.
			 */
			SCC predSCC = predNode.getInfo().getCFGInfo().getSCC();
			if (predSCC != null) {
				// If null, then pred clearly belongs to some other SCC.
				if (thisSCC == predSCC) {
					// Predecessor lies within the SCC.
					if (!this.safeCurrentSCCNodes.contains(predNode)) {
						anyPredMissed = true;
						continue;
					}
				}
			}
			F predOUT = (F) predNode.getInfo().getOUT(analysisName);
			if (predOUT == null) {
				/*
				 * Here, we do not mark anyPredMissed, as this node would be marked for
				 * processing whenever the predecessor has been processed for the first time.
				 * Check the setting of inOrOUTChanged above.
				 */
				continue;
			}
			F edgeOUT = this.edgeTransferFunction(predOUT, idfaEdge.getNode(), node);
			inChanged |= newIN.mergeWithAccessed(edgeOUT, idfaEdge.getCells(), accessedCells);
			if (node instanceof PostCallNode) {
				if (nonLocalCells == null) {
					nonLocalCells = new CellSet();
				}
				// Capture nonLocalCells.
				nonLocalCells.addAll(predOUT.flowMap.keySet());
			}
		}
		boolean anyChangesDueToPre = inChanged;
		if (node instanceof PostCallNode) {
			PreCallNode preNode = ((CallStatement) node.getParent()).getPreCallNode();
			SCC preSCC = preNode.getInfo().getCFGInfo().getSCC();
			boolean doNotProcess = false;
			if (preSCC != null) {
				// If null, then pred clearly belongs to some other SCC.
				if (thisSCC == preSCC) {
					// Predecessor lies within the SCC.
					if (!this.safeCurrentSCCNodes.contains(preNode)) {
						anyPredMissed = true;
						doNotProcess = true;
					}
				}
			}
			if (!doNotProcess) {
				inChanged |= processPostCallNodes((PostCallNode) node, newIN, nonLocalCells);
			}
		}
		if (anyChangesDueToPre != inChanged) {
			proceedAhead = true;
		}

		if (firstProcessingOfExistingNode) {
			assert (oldIN != null);
			if (!proceedAhead) {
				if (oldIN.flowMap != null && newIN.flowMap != null) {
					if (accessedCells.isUniversal()) {
						proceedAhead = true;
					} else {
						for (Cell c : accessedCells) {
							boolean oldContains = oldIN.flowMap.containsKey(c);
							boolean newContains = newIN.flowMap.containsKey(c);
							// If the entry is present in OLD, but not NEW, then we should re-apply the
							// transfer function.
							if (oldContains && !newContains) {
								proceedAhead = true;
								break;
							}
							if (newContains && !newIN.flowMap.get(c).equals(oldIN.flowMap.get(c))) {
								proceedAhead = true;
								break;
							}
						}
					}
				}
			}
		} else {
			if (CellularDataFlowAnalysis.accessedCellValueChanged) {
				proceedAhead = true;
			}
		}

		if (anyPredMissed) {
			this.underApproximated.add(node);
		} else {
			this.underApproximated.remove(node);
		}
		if (node instanceof ParameterDeclaration) {
			proceedAhead = true; // Note that the value of arguments won't be present in the IN flow-map. Hence,
								 // conservatively, we set this flag.
		} else if (node instanceof PostCallNode) {
			proceedAhead = true;
			}
		/**
		 * If the IN of a BarrierDirective has changed, we should add all its sibling
		 * barriers to the workList.
		 */
		if (node instanceof BarrierDirective) {
			if (oldIN == null) {
				// This was the first processing of the barrier, ever. Others would get affected
				// only if the in was changed.
				if (inChanged) {
					this.addAllSiblingBarriersToWorkList((BarrierDirective) node, thisSCC);
				}
			} else if (oldIN != newIN) {
				// This was the first processing of the barrier in this round. If the objects
				// differ semantically, others would get affected.
				if (!oldIN.isEqualTo(newIN)) {
					this.addAllSiblingBarriersToWorkList((BarrierDirective) node, thisSCC);
				}
			} else {
				// This is any-but-first processing of the barrier in this round. Others would
				// be impacted only if anything changed in IN.
				if (inChanged) {
					this.addAllSiblingBarriersToWorkList((BarrierDirective) node, thisSCC);
				}
			}
		}

		nodeInfo.setIN(analysisName, newIN);

		// if (oldIN != null && oldIN != newIN && !oldIN.isEqualTo(newIN)) {
		// inOrOUTChanged = true;
		// }
		// NEW, RECTIFIED CODE:
		if (oldIN != null && oldIN != newIN) {
			inChanged = !oldIN.isEqualTo(newIN);
		}

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		long localTimer = System.nanoTime();
		InterThreadForwardCellularAnalysis.innerCount++;
		CellularDataFlowAnalysis.nodeAnalysisStack.add(new NodeAnalysisPair(node, this));
		F oldOUT = (F) nodeInfo.getOUT(analysisName);
		F newOUT;
		if (node instanceof BarrierDirective) {
			newOUT = this.visitChanged((BarrierDirective) node, newIN, first);
		} else {
			/*
			 * Refer to the last point in Note 21.0.1 from IMOP-CR. TODO: Verify the second
			 * operand of || below. Taken from Phase II.
			 */
			if (accessedCells.isUniversal() || proceedAhead || (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition)) {
				newOUT = node.accept(this, newIN);
			} else {
				this.transferFunctionsSkipped++;
				localTimer = System.nanoTime();
				if (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition) {
					newOUT = this.copyFlowMap(newIN);
				} else if (node instanceof EndNode && node.getParent() instanceof FunctionDefinition) {
					newOUT = this.copyFlowMap(newIN);
				} else if (node instanceof EndNode && node.getParent() instanceof CompoundStatement) {
					newOUT = this.copyFlowMap(newIN);
				} else if (node instanceof PostCallNode) {
					newOUT = this.copyFlowMap(newIN);
				} else {
					if (accessedCells.isEmpty()) {
						newOUT = newIN;
					} else {
						newOUT = this.copyFlowMap(newIN);
						for (Cell c : accessedCells) {
							if (oldOUT.flowMap.containsKey(c)) {
								newOUT.flowMap.putSpecial(c, oldOUT.flowMap, c);
							}
						}
					}
				}
				InterThreadForwardCellularAnalysis.innerTimer += (System.nanoTime() - localTimer);
			}
			if (node instanceof EndNode) {
				processEndNodes((EndNode) node, newOUT);
			} else if (node instanceof BeginNode) {
				processBeginNodes((BeginNode) node, newOUT);
			}
		}
		CellularDataFlowAnalysis.nodeAnalysisStack.pop();
		nodeInfo.setOUT(analysisName, newOUT);
		this.safeCurrentSCCNodes.add(node); // Mark a node as processed only after its OUT has been "purified".

		// Step 3: Process the successors, if needed.
		boolean inOrOUTChanged = false;
		inOrOUTChanged |= inChanged;
		if (node instanceof BarrierDirective) {
			if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
				inOrOUTChanged = true;
			}
		}
		if (node instanceof ParameterDeclaration) {
			// Added this code because finding whether the OUT of ParameterDeclaration has
			// changed, is complex.
			// Note that if the IN of a ParameterDeclaration does not change, the OUT may
			// still change (due to changes at call-sites).
			inOrOUTChanged = true;
		}
		if (inOrOUTChanged) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				Node n = idfaEdge.getNode();
				if (phaseOne && this.thisSeeds.contains(n)) {
					continue;
				}
				SCC otherSCC = n.getInfo().getCFGInfo().getSCC();
				if (otherSCC == thisSCC) {
					this.intraSCCWorkList.add(n);
				} else if (node instanceof ParameterDeclaration) {
					// ReversePostOrderWorkList.checkNode(n, this.globalWorkList);
					this.globalWorkList.add(n);
				}
					}
			if (node instanceof PreCallNode) {
				PreCallNode pre = (PreCallNode) node;
				PostCallNode post = pre.getParent().getPostCallNode();
				SCC otherSCC = post.getInfo().getCFGInfo().getSCC();
				if (otherSCC == thisSCC) {
					this.intraSCCWorkList.add(post);
				} else {
					// ReversePostOrderWorkList.checkNode(post, this.globalWorkList);
					this.globalWorkList.add(post);
				}
			}
			// If we are adding successors of a BeginNode of a FunctionDefinition, we should
			// add all the ParameterDeclarations.
			if (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition) {
				FunctionDefinition func = (FunctionDefinition) node.getParent();
				for (ParameterDeclaration paramDecl : func.getInfo().getCFGInfo().getParameterDeclarationList()) {
					SCC otherSCC = paramDecl.getInfo().getCFGInfo().getSCC();
					if (otherSCC == thisSCC) {
						this.intraSCCWorkList.add(paramDecl);
					} else {
						// ReversePostOrderWorkList.checkNode(paramDecl, this.globalWorkList);
						this.globalWorkList.add(paramDecl);
					}
				}
			}
		}
	}

	/**
	 * Processing that is performed as per standard IDFA for nodes to be processed
	 * in the second pass of processing an SCC. Note that in this method, no nodes
	 * of other SCC should be inserted in the worklist.
	 *
	 * @param node
	 * @param stateINChanged
	 */
	@SuppressWarnings("unchecked")
	protected final void processWhenNotUpdatedOptimized(Node node, SCC thisSCC) {
		boolean inOrOUTChanged = false;
		NodeInfo nodeInfo = node.getInfo();
		Set<IDFAEdge> predecessors = nodeInfo.getCFGInfo()
			.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension());
		F newIN = (F) nodeInfo.getIN(analysisName);
		if (newIN == null) {
			/*
			 * In this scenario, we must set the propagation flag, so that all successors
			 * are processed at least once.
			 */
			inOrOUTChanged = true;
			if (predecessors.isEmpty()) {
				newIN = this.getEntryFact();
			} else {
				newIN = this.getTop();
			}
		}

		CellSet nonLocalCells = null;
		boolean inChanged = false;
		for (IDFAEdge idfaEdge : predecessors) {
			F predOUT = (F) idfaEdge.getNode().getInfo().getOUT(analysisName);
			if (predOUT == null) {
				continue;
			}
			F edgeOUT = this.edgeTransferFunction(predOUT, idfaEdge.getNode(), node);
			inChanged |= newIN.merge(edgeOUT, idfaEdge.getCells());
			if (node instanceof PostCallNode) {
				if (nonLocalCells == null) {
					nonLocalCells = new CellSet();
				}
				// Capture nonLocalCells.
				nonLocalCells.addAll(predOUT.flowMap.keySet());
			}
		}

		if (node instanceof PostCallNode) {
			inChanged |= processPostCallNodes((PostCallNode) node, newIN, nonLocalCells);
		}

		/**
		 * If the IN of a BarrierDirective has changed, we should add all its sibling
		 * barriers to the workList.
		 */
		if (inChanged && node instanceof BarrierDirective) {
			this.addAllSiblingBarriersToWorkList((BarrierDirective) node, thisSCC);
		}
		nodeInfo.setIN(analysisName, newIN);

		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		CellularDataFlowAnalysis.nodeAnalysisStack.add(new NodeAnalysisPair(node, this));
		F oldOUT = (F) nodeInfo.getOUT(analysisName);
		F newOUT = node.accept(this, newIN);
		if (node instanceof EndNode) {
			processEndNodes((EndNode) node, newOUT);
		} else if (node instanceof BeginNode) {
			processBeginNodes((BeginNode) node, newOUT);
		}
		CellularDataFlowAnalysis.nodeAnalysisStack.pop();
		nodeInfo.setOUT(analysisName, newOUT);

		// if (Program.maintainImpactedCells) {
		// this.updateImpactedCells(node, newIN, newOUT);
		// }

		// Step 3: Process the successors, if needed.
		inOrOUTChanged |= inChanged;
		if (node instanceof BarrierDirective) {
			if (newOUT == newIN && inChanged) {
				assert (false); // OUT of a barrier can never be same object as its IN.
				inOrOUTChanged = true; // Redundant; kept for readability.
			} else if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
				inOrOUTChanged = true;
			}
		}
		if (node instanceof ParameterDeclaration) {
			// Added this code because finding whether the OUT of ParameterDeclaration has
			// changed, is complex.
			// Note that if the IN of a ParameterDeclaration does not change, the OUT may
			// still change (due to changes at call-sites).
			inOrOUTChanged = true;
		}
		if (inOrOUTChanged) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				Node n = idfaEdge.getNode();
				SCC otherSCC = n.getInfo().getCFGInfo().getSCC();
				if (otherSCC == thisSCC) {
					this.intraSCCWorkList.add(n);
				}
					}
			if (node instanceof PreCallNode) {
				PreCallNode pre = (PreCallNode) node;
				PostCallNode post = pre.getParent().getPostCallNode();
				SCC otherSCC = post.getInfo().getCFGInfo().getSCC();
				if (otherSCC == thisSCC) {
					this.intraSCCWorkList.add(post);
				} else {
					this.globalWorkList.add(post);
				}
			}
			// If we are adding successors of a BeginNode of a FunctionDefinition, we should
			// add all the ParameterDeclarations.
			if (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition) {
				FunctionDefinition func = (FunctionDefinition) node.getParent();
				for (ParameterDeclaration paramDecl : func.getInfo().getCFGInfo().getParameterDeclarationList()) {
					SCC otherSCC = paramDecl.getInfo().getCFGInfo().getSCC();
					if (otherSCC == thisSCC) {
						this.intraSCCWorkList.add(paramDecl);
					} else {
						this.globalWorkList.add(paramDecl);
					}
				}
			}
		}
	}

	/**
	 *
	 * @param node
	 * @param stateINChanged
	 */
	@SuppressWarnings("unchecked")
	protected final void processNonSCCNodeOptimized(Node node) {
		boolean first = true; // We just assume that each non-SCC node is processed exactly once in RPO.
		NodeInfo nodeInfo = node.getInfo();
		Set<IDFAEdge> predecessors = nodeInfo.getCFGInfo()
			.getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension());
		F oldIN = (F) nodeInfo.getIN(analysisName);
		F newIN;
		boolean inChanged = false;
		boolean proceedAhead = false; // To indicate whether the transfer function should be applied.
		boolean firstProcessingOfExistingNode = false;
		if (oldIN == null) {
			inChanged = true;
			newIN = (predecessors.isEmpty()) ? this.getEntryFact() : this.getTop();
			proceedAhead = true;
		} else if (first) {
			firstProcessingOfExistingNode = true;
			// First processing in the first round of update.
			newIN = (predecessors.isEmpty()) ? this.getEntryFact() : this.getTop();
		} else {
			newIN = oldIN; // Use the same object
		}

		CellSet accessedCells = node.getInfo().getAccessedCellSets(this);
		CellularDataFlowAnalysis.accessedCellValueChanged = false;

		CellSet nonLocalCells = null;
		for (IDFAEdge idfaEdge : predecessors) {
			F predOUT = (F) idfaEdge.getNode().getInfo().getOUT(analysisName);
			if (predOUT == null) {
				continue;
			}
			F edgeOUT = this.edgeTransferFunction(predOUT, idfaEdge.getNode(), node);
			inChanged |= newIN.mergeWithAccessed(edgeOUT, idfaEdge.getCells(), accessedCells);
			if (node instanceof PostCallNode) {
				if (nonLocalCells == null) {
					nonLocalCells = new CellSet();
				}
				// Capture nonLocalCells.
				nonLocalCells.addAll(predOUT.flowMap.keySet());
			}
		}

		boolean anyChangesDueToPre = inChanged;
		if (node instanceof PostCallNode) {
			inChanged |= processPostCallNodes((PostCallNode) node, newIN, nonLocalCells);
		}
		if (anyChangesDueToPre != inChanged) {
			proceedAhead = true;
		}

		if (firstProcessingOfExistingNode) {
			assert (oldIN != null);
			if (!proceedAhead) {
				if (oldIN.flowMap != null && newIN.flowMap != null) {
					for (Cell c : accessedCells) {
						boolean oldContains = oldIN.flowMap.containsKey(c);
						boolean newContains = newIN.flowMap.containsKey(c);
						// If the entry is present in OLD, but not NEW, then we should re-apply the
						// transfer function.
						if (oldContains && !newContains) {
							proceedAhead = true;
							break;
						}
						if (newContains && !newIN.flowMap.get(c).equals(oldIN.flowMap.get(c))) {
							proceedAhead = true;
							break;
						}
					}
				}
			}
		} else {
			if (CellularDataFlowAnalysis.accessedCellValueChanged) {
				proceedAhead = true;
			}
		}

		if (node instanceof ParameterDeclaration) {
			proceedAhead = true; // Note that the value of arguments won't be present in the IN flow-map. Hence,
								 // conservatively, we set this flag.
		} else if (node instanceof PostCallNode) {
			proceedAhead = true;
			}

		/**
		 * If the IN of a BarrierDirective has changed, we should add all its sibling
		 * barriers to the workList.
		 */
		if (node instanceof BarrierDirective) {
			if (oldIN == null) {
				// This was the first processing of the barrier, ever. Others would get affected
				// only if the in was changed.
				if (inChanged) {
					this.addAllSiblingBarriersToGlobalWorkList((BarrierDirective) node);
				}
			} else if (oldIN != newIN) {
				// This was the first processing of the barrier in this round. If the objects
				// differ semantically, others would get affected.
				if (!oldIN.isEqualTo(newIN)) {
					this.addAllSiblingBarriersToGlobalWorkList((BarrierDirective) node);
				}
			} else {
				// This is any-but-first processing of the barrier in this round. Others would
				// be impacted only if anything changed in IN.
				if (inChanged) {
					this.addAllSiblingBarriersToGlobalWorkList((BarrierDirective) node);
				}
			}
		}
		// if (inChanged && node instanceof BarrierDirective) {
		// this.addAllSiblingBarriersToGlobalWorkList((BarrierDirective) node);
		// }

		nodeInfo.setIN(analysisName, newIN);

		// OLD CODE:
		// if (oldIN != null && oldIN != newIN && !oldIN.isEqualTo(newIN)) {
		// inChanged = true;
		// }
		// NEW, RECTIFIED CODE:
		if (oldIN != null && oldIN != newIN) {
			inChanged = !oldIN.isEqualTo(newIN);
		}
		// Step 2: Apply the flow-function on IN, to obtain the OUT.
		CellularDataFlowAnalysis.nodeAnalysisStack.add(new NodeAnalysisPair(node, this));
		F oldOUT = (F) nodeInfo.getOUT(analysisName);
		F newOUT;
		if (node instanceof BarrierDirective) {
			newOUT = this.visitChanged((BarrierDirective) node, newIN, first);
		} else {
			if (proceedAhead || (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition)) {
				newOUT = node.accept(this, newIN);
			} else {
				this.transferFunctionsSkipped++;
				if (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition) {
					newOUT = this.copyFlowMap(newIN);
				} else if (node instanceof EndNode && node.getParent() instanceof FunctionDefinition) {
					newOUT = this.copyFlowMap(newIN);
				} else if (node instanceof EndNode && node.getParent() instanceof CompoundStatement) {
					newOUT = this.copyFlowMap(newIN);
				} else if (node instanceof PostCallNode) {
					newOUT = this.copyFlowMap(newIN);
				} else {
					if (accessedCells.isEmpty()) {
						newOUT = newIN;
					} else {
						newOUT = this.copyFlowMap(newIN);
						for (Cell c : accessedCells) {
							if (oldOUT.flowMap.containsKey(c)) {
								newOUT.flowMap.putSpecial(c, oldOUT.flowMap, c);
							}
						}
					}
				}
			}
		}
		if (node instanceof EndNode) {
			processEndNodes((EndNode) node, newOUT);
		} else if (node instanceof BeginNode) {
			processBeginNodes((BeginNode) node, newOUT);
		}
		CellularDataFlowAnalysis.nodeAnalysisStack.pop();
		nodeInfo.setOUT(analysisName, newOUT);

		// if (Program.maintainImpactedCells) {
		// this.updateImpactedCells(node, newIN, newOUT);
		// }

		// Step 3: Process the successors, if needed.
		boolean inOrOUTChanged = false;
		inOrOUTChanged |= inChanged;
		if (node instanceof BarrierDirective) {
			if (oldOUT == null || !newOUT.isEqualTo(oldOUT)) {
				inOrOUTChanged = true;
			}
		}
		if (node instanceof ParameterDeclaration) {
			// Added this code because finding whether the OUT of ParameterDeclaration has
			// changed, is complex.
			// Note that if the IN of a ParameterDeclaration does not change, the OUT may
			// still change (due to changes at call-sites).
			inOrOUTChanged = true;
		}
		if (inOrOUTChanged) {
			for (IDFAEdge idfaEdge : nodeInfo.getCFGInfo()
					.getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension())) {
				Node n = idfaEdge.getNode();
				this.globalWorkList.add(n);
					}
			if (node instanceof PreCallNode) {
				PreCallNode pre = (PreCallNode) node;
				this.globalWorkList.add(pre.getParent().getPostCallNode());
			}
			// If we are adding successors of a BeginNode of a FunctionDefinition, we should
			// add all the ParameterDeclarations.
			if (node instanceof BeginNode && node.getParent() instanceof FunctionDefinition) {
				FunctionDefinition func = (FunctionDefinition) node.getParent();
				for (ParameterDeclaration paramDecl : func.getInfo().getCFGInfo().getParameterDeclarationList()) {
					this.globalWorkList.add(paramDecl);
				}
			}
		}
	}

}
