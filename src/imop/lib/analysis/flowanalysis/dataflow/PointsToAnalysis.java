/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.dataflow;

import imop.Main;
import imop.ast.info.PTAStabilizationHeuristic;
import imop.ast.info.cfgNodeInfo.ParameterDeclarationInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.analysis.flowanalysis.*;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis.PointsToGlobalState;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.CellularDataFlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterThreadForwardCellularAnalysis;
import imop.lib.analysis.typesystem.*;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.*;
import imop.lib.util.CollectorVisitor.NeighbourSetGetter;
import imop.parser.Program;
import imop.parser.Program.StabilizationIDFAMode;

import java.util.function.Predicate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PointsToAnalysis extends InterThreadForwardCellularAnalysis<PointsToAnalysis.PointsToFlowMap> {

	public static boolean isHeuristicEnabled = false; // Also enable Program.ptaHeuristicEnabled
	public static CellSet affectedCellsInThisEpoch = new CellSet();

	public static void handleNodeAdditionOrRemovalForHeuristic(Node affectedNode) {
		if (!isHeuristicEnabled) {
			return;
		} else if (!Program.ptaHeuristicEnabled) {
			isHeuristicEnabled = false;
			return;
		}
		CellSet affectedCells = PTAStabilizationHeuristic.getAffectedCells(affectedNode);
		if (affectedCells == null) {
			PointsToAnalysis.disableHeuristic();
			return;
		} else {
			affectedCellsInThisEpoch.addAll(affectedCells);
		}
	}

	public static void disableHeuristic() {
		PointsToAnalysis.isHeuristicEnabled = false;
		affectedCellsInThisEpoch.clear();
	}

	public static void enableHeuristic() {
		if (Program.ptaHeuristicEnabled) {
			PointsToAnalysis.isHeuristicEnabled = true;
		}
		affectedCellsInThisEpoch.clear();
	}

	/**
	 * Old implementation of IncIDFA, which was cell unaware.
	 */
	public void restartAnalysisFromStoredNodes() {
		assert (!SCC.processingTarjan);
		if (Program.stabilizationIDFAMode == StabilizationIDFAMode.INCIDFA) {
			this.newStabilizationTriggerHandler(); // NOTE: THIS METHOD IS PRESENT IN
													// INTERTHREADFORWARDCELLULARANALYSIS.
			return;
		}
		// if (this.analysisName == AnalysisName.POINTSTO
		// && PointsToAnalysis.stateOfPointsTo != PointsToGlobalState.STALE) {
		// nodesToBeUpdated.clear();
		// return;
		// }
		// String localStr = "";
		// for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
		// localStr += ste.getFileName() + ":" + ste.getLineNumber() + "\n";
		// }
		// Main.globalString += localStr + "\n\n\n";
		this.autoUpdateTriggerCounter++;
		// if (Program.stabilizationStackDump != null) {
		// StringBuilder tempStr = new StringBuilder();
		// for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
		// Program.stabilizationStackDump.append(ste.toString() + "\n");
		// }
		// Program.stabilizationStackDump.append("#######\n\n");
		// }
		PointsToAnalysis.enableHeuristic(); // Mark start of next epoch.
		/*
		 * Maintain a copy of nodes-to-be-updated for certain heuristic later.
		 */
		// this.copyOfSeedNodesForFirstPhase.clear();
		// this.copyOfSeedNodesForFirstPhase.addAll(this.nodesToBeUpdated);
		long localTimer = System.nanoTime();

		/*
		 * From the set of nodes to be updated, we obtain the workList to be
		 * processed.
		 * OLD: We add all the entry points of the SCC of each node.
		 */
		long thisUpdateNodeCounter = this.nodesProcessedDuringUpdate;
		this.workList.recreate();
		Set<Node> remSet = new HashSet<>();
		// TODO: Check why on Earth are we getting a ConcurrentModificationException
		// here for EGUPD mode of IDFA-stabilization! This might mostly be due to some
		// assert in the add().
		for (Node n : new HashSet<>(nodesToBeUpdated)) {
			boolean added = this.workList.add(n);
			if (added) {
				remSet.add(n);
				/*
				 * New code: If n is a Declaration, add the EndNode of its scope to the
				 * worklist.
				 */
				if (n instanceof Declaration) {
					Scopeable definingScope = Misc.getEnclosingBlock(n);
					if (definingScope instanceof CompoundStatement) {
						this.workList.add(
								((CompoundStatement) definingScope).getInfo().getCFGInfo().getNestedCFG().getBegin());
						this.workList.add(
								((CompoundStatement) definingScope).getInfo().getCFGInfo().getNestedCFG().getEnd());
					}
				}
			}
			// OLD CODE: Now we do not add the entry points of SCCs to be processed.
			// NEW CODE: And now we do. Thu Jan 27 11:47:06 IST 2022
			SCC nSCC = n.getInfo().getCFGInfo().getSCC();
			if (nSCC != null) {
				this.workList.addAll(nSCC.getEntryNodes());
			}
		}
		if (Program.profileSCC) {
			System.out.println("Size of the seed set for trigger #" + autoUpdateTriggerCounter + ": " + remSet.size());
		}
		// OLD CODE: Now, if we ever find that a node is unconnected to the program, we
		// remove it from processing.
		this.nodesToBeUpdated.removeAll(remSet);
		// this.nodesToBeUpdated.clear();

		/*
		 * A new piece of code starts: Count the number of SCCs (with cycles) that have
		 * seed nodes.
		 */
		if (Program.countSeededSCCs && Program.profileSCC) {
			Set<SCC> seededSCCs = new HashSet<>();
			for (Node n : workList.getIteratorForNonBarrierNodes()) {
				SCC thisSCC = n.getInfo().getCFGInfo().getSCC();
				if (thisSCC != null) {
					seededSCCs.add(thisSCC);
				}
			}
			System.out.println("Seeded SCC: " + seededSCCs.size());
		}
		/*
		 * .. New code for counter ends.
		 */

		switch (Program.stabilizationIDFAMode) {
		case ALL_NOSCC:
			this.reinitAllAndRunNonSCC();
			break;
		case ALL_SCC:
			this.reinitAllAndRunSCC();
			break;
		case RESTART:
			restartingIterationsAfterInit();
			break;
		case INIT_RESTART:
			markFlowFactsOfReachableNodesAsNull();
			Program.timerForMarking += System.nanoTime() - localTimer;
			localTimer = System.nanoTime();
			restartingIterationsAfterInit();
			break;
		case INCIDFA:
			Misc.exitDueToError(
					"Use the alternative for restartAnalaysisFromStoredNodes given in PTA, with name newStabilizationTriggerHandler");
		case OLD_INCIDFA:
			obsolete_twoPassPerSCC();
			break;
		default:
			break;
		}

		localTimer = System.nanoTime() - localTimer;
		counterList.add(this.nodesProcessedDuringUpdate - thisUpdateNodeCounter);
		this.flowAnalysisUpdateTimer += localTimer;
		if (this.analysisName == AnalysisName.POINTSTO
				&& PointsToAnalysis.stateOfPointsTo == PointsToGlobalState.STALE) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.CORRECT;
		}
	}

	private void reinitAllAndRunSCC() {
		for (Node node : Program.getRoot().getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
			node.getInfo().removeAnalysisInformation(this.getAnalysisName());
		}
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		Program.basePointsTo = false; // Unneeded for second and further runs.
		Program.memoizeAccesses++;
		BeginNode beginNode = mainFunc.getInfo().getCFGInfo().getNestedCFG().getBegin();
		this.workList.recreate();
		this.workList.add(beginNode);
		int processedSCCCount = 0;
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
				stabilizeSCCOfInOnePassAfterInit(nodeToBeAnalyzed);
				processedSCCCount++;
			}
		}
		if (Program.profileSCC) {
			System.out.println("Total SCCs processed: " + processedSCCCount + " out of " + SCC.getAllSCCSize());
		}
		Program.memoizeAccesses--;
	}

	private void reinitAllAndRunNonSCC() {
		for (Node node : Program.getRoot().getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
			node.getInfo().removeAnalysisInformation(this.getAnalysisName());
		}
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		Program.basePointsTo = false; // Unneeded for second and further runs.
		Program.memoizeAccesses++;
		// String typeList = "";
		BeginNode beginNode = mainFunc.getInfo().getCFGInfo().getNestedCFG().getBegin();
		this.workList.recreate();
		this.workList.add(beginNode);
		do {
			Node nodeToBeAnalysed = this.workList.removeFirstElement();
			this.debugRecursion(nodeToBeAnalysed);
			this.processWhenNotUpdated(nodeToBeAnalysed);
		} while (!this.workList.isEmpty());
		Program.memoizeAccesses--;
	}

	private void markFlowFactsOfReachableNodesAsNull() {
		Set<Node> seedNodes = new HashSet<>();
		for (Node n : workList.getIteratorForNonBarrierNodes()) {
			seedNodes.add(n);
		}
		for (Node n : workList.getIteratorForBarrierNodes()) {
			seedNodes.add(n);
		}
		Set<NodeWithStack> workSet = new HashSet<>();
		for (Node n : seedNodes) {
			workSet.add(new NodeWithStack(n, new CallStack()));
		}

		Set<NodeWithStack> collectedNodeSet = new HashSet<>();
		while (!workSet.isEmpty()) {
			NodeWithStack currentNode = Misc.getAnyElement(workSet);
			workSet.remove(currentNode);
			for (NodeWithStack succNode : currentNode.getNode().getInfo().getCFGInfo()
					.getInterProceduralLeafSuccessors(currentNode.getCallStack())) {
				if (!collectedNodeSet.contains(succNode)) {
					collectedNodeSet.add(succNode);
					workSet.add(succNode);
				}
			}
		}
		for (NodeWithStack nodeWithStack : collectedNodeSet) {
			nodeWithStack.getNode().getInfo().removeAnalysisInformation(this.getAnalysisName());
		}
	}

	private void restartingIterationsAfterInit() {
		int processedSCCCount = 0;
		Program.basePointsTo = false; // Unneeded for second and further runs.
		Program.memoizeAccesses++;
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
				stabilizeSCCOfInOnePassAfterInit(nodeToBeAnalyzed);
				processedSCCCount++;
			}
		}
		if (Program.profileSCC) {
			System.out.println("Total SCCs processed: " + processedSCCCount + " out of " + SCC.getAllSCCSize());
		}
		Program.memoizeAccesses--;
	}

	@SuppressWarnings("unused")
	private void restartingIterationsWithoutInit() {
		int processedSCCCount = 0;
		Program.basePointsTo = false; // Unneeded for second and further runs.
		Program.memoizeAccesses++;
		Set<Node> visited = new HashSet<>();
		while (!workList.isEmpty()) {
			Node nodeToBeAnalyzed = workList.removeFirstElement();
			if (!visited.contains(nodeToBeAnalyzed)) {
				nodeToBeAnalyzed.getInfo().removeAnalysisInformation(AnalysisName.POINTSTO);
				visited.add(nodeToBeAnalyzed);
			}
			CFGInfo nInfo = nodeToBeAnalyzed.getInfo().getCFGInfo();
			if (nInfo.getSCC() == null) {
				// Here, node itself is an SCC. We do not require two rounds.
				this.nodesProcessedDuringUpdate++;
				this.debugRecursion(nodeToBeAnalyzed);
				this.processWhenNotUpdated(nodeToBeAnalyzed); // Directly invoke the second round processing.
				continue;
			} else {
				stabilizeSCCOfInOnePassWithoutInit(nodeToBeAnalyzed, visited);
				processedSCCCount++;
			}
		}
		if (Program.profileSCC) {
			System.out.println("Total SCCs processed: " + processedSCCCount + " out of " + SCC.getAllSCCSize());
		}
		Program.memoizeAccesses--;
	}

	private void obsolete_twoPassPerSCC() {
		int processedSCCCount = 0;
		Program.basePointsTo = false; // Not needed for second and further runs.
		Program.memoizeAccesses++;
		this.safeCurrentSCCNodes = new HashSet<>();
		this.underApproximated = new HashSet<>();
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
				processedSCCCount++;
			}
		}
		if (Program.profileSCC) {
			System.out.println("Total SCCs processed: " + processedSCCCount + " out of " + SCC.getAllSCCSize());
		}
		Program.memoizeAccesses--;
	}

	public PointsToAnalysis() {
		super(AnalysisName.POINTSTO, new AnalysisDimension(Program.sveSensitivityOfIDFAEdges));
	}

	public static enum PointsToGlobalState {
		NOT_INIT, CORRECT, STALE
	}

	public static PointsToGlobalState stateOfPointsTo = PointsToGlobalState.NOT_INIT;

	public static class PointsToFlowMap extends CellularDataFlowAnalysis.CellularFlowMap<ImmutableCellSet> {

		public PointsToFlowMap(ExtensibleCellMap<ImmutableCellSet> flowMap) {
			super(flowMap);
		}

		public PointsToFlowMap(CellularFlowMap<ImmutableCellSet> thatFlowFact) {
			super(thatFlowFact);
		}

		@Override
		public String getAnalysisNameKey() {
			return "ptsTo";
		}

		@Override
		public ImmutableCellSet meet(ImmutableCellSet s1, ImmutableCellSet s2) {
			ImmutableCellSet s3;
			if (s1 == null) {
				if (s2 == null) {
					s3 = null;
				} else {
					s3 = s2;
				}
			} else {
				if (s2 == null) {
					s3 = s1;
				} else if (s1 == s2 || s1.equals(s2)) {
					s3 = s1;
				} else {
					CellSet newSet = new CellSet(s1);
					newSet.addAll(s2);
					s3 = new ImmutableCellSet(newSet);
				}
			}
			return s3;
		}

	}

	@Override
	public PointsToFlowMap getTop() {
		ExtensibleCellMap<ImmutableCellSet> pointsToMap = new ExtensibleCellMap<>();
		return new PointsToFlowMap(pointsToMap);
	}

	@Override
	public PointsToFlowMap getEntryFact() {
		PointsToFlowMap entryFact = this.getTop();
		TranslationUnit root = Program.getRoot();
		for (Node elementChoice : root.getF0().getNodes()) {
			ElementsOfTranslation elemTransChoice = ((ElementsOfTranslation) elementChoice);
			Node extDeclChoice = elemTransChoice.getF0().getChoice();
			if (extDeclChoice instanceof ExternalDeclaration) {
				ExternalDeclaration extDecl = ((ExternalDeclaration) extDeclChoice);
				Node declChoice = extDecl.getF0().getChoice();
				if (declChoice instanceof Declaration) {
					// Note: This declaration may be that of a Function or a Variable
					Declaration declaration = (Declaration) declChoice;
					List<String> declaratorNameList = declaration.getInfo().getIDNameList();
					if (declaration.getInfo().isTypedef()) {
						continue;
					}
					if (declaratorNameList.size() != 1) {
						continue;
					}
					Symbol sym = declaration.getInfo().getDeclaredSymbol();
					if (sym == null) {
						continue;
					}
					Type symType = sym.getType();
					if (!(symType instanceof PointerType)) {
						continue;
					}
					Initializer init = declaration.getInfo().getInitializer();
					CellSet rhsPtsToSet = new CellSet();
					if (init != null) {
						CellList locationsOfInit = init.getInfo().getLocationsOf();
						if (locationsOfInit.isUniversal()) {
							rhsPtsToSet.add(Cell.genericCell);
							// System.err.println("Gen at " + n);
						} else {
							for (Cell c : locationsOfInit) {
								CellSet ptsTo = new CellSet();
								if (c instanceof AddressCell) {
									ptsTo.add(((AddressCell) c).getPointedElement());
								} else if (c instanceof FieldCell) {
									ptsTo.add(c);
								} else {
									ptsTo = entryFact.getFlowMap().get(c);
								}
								if (ptsTo != null && !ptsTo.isEmpty()) {
									rhsPtsToSet.addAll(ptsTo);
								}
							}
						}
					} else {
						rhsPtsToSet.add(Cell.getNullCell());
					}

					if (!rhsPtsToSet.isEmpty()) {
						entryFact.getFlowMap().put(sym, new ImmutableCellSet(rhsPtsToSet));
					}
				}
			}
		}

		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main != null) {
			for (ParameterDeclaration param : main.getInfo().getCFGInfo().getParameterDeclarationList()) {
				Symbol declaredSym = param.getInfo().getDeclaredSymbol();
				if (!(declaredSym.getType() instanceof PointerType)) {
					continue;
				}
				CellSet pointedSet = new CellSet();
				Cell pointedTo = HeapCell.getUnknownParamPointee(param);
				if (pointedTo != null) {
					pointedSet.add(pointedTo);
					entryFact.getFlowMap().put(declaredSym, new ImmutableCellSet(pointedSet));
				}
			}
		}

		return entryFact;
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public PointsToFlowMap visit(Declaration n, PointsToFlowMap flowFactIN) {
		PointsToFlowMap flowFactOUT = flowFactIN;
		Symbol sym = n.getInfo().getDeclaredSymbol();
		if (sym == null || (!(sym.getType() instanceof PointerType))) {
			/*
			 * This is a type definition -- struct/union/enum/typedef, etc,
			 * or definition of some non pointer type.
			 */
			return flowFactIN;
		}
		Initializer init = n.getInfo().getInitializer();
		Set<Cell> tempLHSSet = new HashSet<>();
		tempLHSSet.add(sym);
		ImmutableCellSet lhsSet = new ImmutableCellSet(tempLHSSet);
		CellSet rhsPtsToSet = new CellSet();
		if (init != null) {
			CellList locationsOfInit = init.getInfo().getLocationsOf();
			if (locationsOfInit.isUniversal()) {
				rhsPtsToSet.add(Cell.genericCell);
			} else {
				for (Cell c : init.getInfo().getLocationsOf()) {
					rhsPtsToSet.addAll(c.getPointsTo(n));
				}
			}
		} else {
			rhsPtsToSet.add(Cell.getNullCell());
		}

		if (!rhsPtsToSet.isEmpty()) {
			CellMap<ImmutableCellSet> affectedMap = OptimizedPointsToUpdateGetter.generateUpdateMap(n, lhsSet,
					new ImmutableCellSet(rhsPtsToSet));
			if (!affectedMap.isEmpty()) {
				flowFactOUT = new PointsToFlowMap(flowFactIN);
				flowFactOUT.getFlowMap().mergeWith(affectedMap, (s1, s2) -> {
					ImmutableCellSet s3;
					if (s2 != null) {
						s3 = s2; // We use same CellSet objects as present in affectedMap.
					} else {
						s3 = s1; // Implies no change.
					}
					return s3;
				}, null);
			}
		}

		return flowFactOUT;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public PointsToFlowMap visit(UnknownCpp n, PointsToFlowMap flowFactIN) {
		return flowFactIN;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public PointsToFlowMap visit(UnknownPragma n, PointsToFlowMap flowFactIN) {
		return flowFactIN;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	@Override
	public PointsToFlowMap visit(OmpForInitExpression n, PointsToFlowMap flowFactIN) {
		PointsToFlowMap flowFactOUT = flowFactIN;
		/*
		 * Note that in a well-defined OpenMP program, no side-effects should
		 * exist in the
		 * computation of the RHS expression.
		 * Next, we model the effect of f0 = f2, if f0 is a pointer-type.
		 */
		Symbol lhs = Misc.getSymbolEntry(n.getF0().toString(), n);
		if (lhs == null || !(lhs.getType() instanceof PointerType)) {
			return flowFactIN;
		}
		Set<Cell> tempLHSSEt = new HashSet<>();
		tempLHSSEt.add(lhs);
		ImmutableCellSet lhsSet = new ImmutableCellSet(tempLHSSEt);
		CellSet rhsPtsToSet = new CellSet();
		CellList locationsOfF2 = n.getF2().getInfo().getLocationsOf();
		if (locationsOfF2.isUniversal()) {
			rhsPtsToSet.add(Cell.genericCell);
		} else {
			for (Cell c : locationsOfF2) {
				rhsPtsToSet.addAll(c.getPointsTo(n));
			}
		}
		if (!rhsPtsToSet.isEmpty()) {
			CellMap<ImmutableCellSet> affectedMap = OptimizedPointsToUpdateGetter.generateUpdateMap(n, lhsSet,
					new ImmutableCellSet(rhsPtsToSet));
			if (!affectedMap.isEmpty()) {
				flowFactOUT = new PointsToFlowMap(flowFactIN);
				flowFactOUT.getFlowMap().mergeWith(affectedMap, (s1, s2) -> {
					ImmutableCellSet s3;
					if (s2 != null) {
						s3 = s2; // We use same CellSet objects as present in affectedMap.
					} else {
						s3 = s1; // Implies no change.
					}
					return s3;
				}, null);
			}
		}
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	@Override
	public PointsToFlowMap visit(OmpForCondition n, PointsToFlowMap flowFactIN) {
		/*
		 * Note that in a well-defined OpenMP program, no side-effects should
		 * exist in the
		 * computation of the RHS operator.
		 */
		return flowFactIN;
	}

	/**
	 * f0 ::= PostIncrementId()
	 * | PostDecrementId()
	 * | PreIncrementId()
	 * | PreDecrementId()
	 * | ShortAssignPlus()
	 * | ShortAssignMinus()
	 * | OmpForAdditive()
	 * | OmpForSubtractive()
	 * | OmpForMultiplicative()
	 */
	@Override
	public PointsToFlowMap visit(OmpForReinitExpression n, PointsToFlowMap flowFactIN) {
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public PointsToFlowMap visit(FlushDirective n, PointsToFlowMap flowFactIN) {
		return flowFactIN;
	}

	@Override
	public PointsToFlowMap visit(DummyFlushDirective n, PointsToFlowMap flowFactIN) {
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public PointsToFlowMap visit(TaskwaitDirective n, PointsToFlowMap flowFactIN) {
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public PointsToFlowMap visit(TaskyieldDirective n, PointsToFlowMap flowFactIN) {
		return flowFactIN;
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public PointsToFlowMap visit(ExpressionStatement n, PointsToFlowMap flowFactIN) {
		PointsToFlowMap flowFactOUT = flowFactIN;
		if (n.getF0().present()) {
			Expression e = (Expression) n.getF0().getNode();
			flowFactOUT = updateOptimizedPointsTo(e, flowFactIN);
		}
		return flowFactOUT;
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public PointsToFlowMap visit(GotoStatement n, PointsToFlowMap flowFactIN) {
		return flowFactIN;
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public PointsToFlowMap visit(ContinueStatement n, PointsToFlowMap flowFactIN) {
		return flowFactIN;
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public PointsToFlowMap visit(BreakStatement n, PointsToFlowMap flowFactIN) {
		return flowFactIN;
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public PointsToFlowMap visit(ReturnStatement n, PointsToFlowMap flowFactIN) {
		PointsToFlowMap flowFactOUT = flowFactIN;
		if (n.getF1().present()) {
			Expression e = (Expression) n.getF1().getNode();
			flowFactOUT = updateOptimizedPointsTo(e, flowFactIN);
		}
		return flowFactOUT;
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public PointsToFlowMap visit(Expression n, PointsToFlowMap flowFactIN) {
		PointsToFlowMap flowFactOUT;
		flowFactOUT = updateOptimizedPointsTo(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public PointsToFlowMap visit(IfClause n, PointsToFlowMap flowFactIN) {
		PointsToFlowMap flowFactOUT;
		flowFactOUT = updateOptimizedPointsTo(n.getF2(), flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public PointsToFlowMap visit(NumThreadsClause n, PointsToFlowMap flowFactIN) {
		PointsToFlowMap flowFactOUT;
		flowFactOUT = updateOptimizedPointsTo(n.getF2(), flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public PointsToFlowMap visit(FinalClause n, PointsToFlowMap flowFactIN) {
		PointsToFlowMap flowFactOUT;
		flowFactOUT = updateOptimizedPointsTo(n.getF2(), flowFactIN);
		return flowFactOUT;
	}

	/**
	 * Special Node
	 */
	@Override
	public PointsToFlowMap visit(BeginNode n, PointsToFlowMap flowFactIN) {
		Node parent = n.getParent();
		if (parent instanceof FunctionDefinition) {
			return new PointsToFlowMap(flowFactIN);
		}
		return flowFactIN;
	}

	/**
	 * Special Node
	 */
	@Override
	public PointsToFlowMap visit(EndNode n, PointsToFlowMap flowFactIN) {
		Node parent = n.getParent();
		if (parent instanceof FunctionDefinition || parent instanceof CompoundStatement) {
			return new PointsToFlowMap(flowFactIN);
		}
		return flowFactIN;
	}

	/**
	 * Special Node
	 */
	@Override
	public PointsToFlowMap visit(PreCallNode n, PointsToFlowMap flowFactIN) {
		return flowFactIN;
		// OLD CODE: This code is incomplete; anyway, a more logical alternative is
		// HeapValidityAnalysis.
		// PointsToFlowMap flowFactOUT = flowFactIN;
		// CallStatement callStmt = n.getParent();
		// Symbol funcSymbol = (Symbol)
		// callStmt.getInfo().getCalledSymbols().getReadOnlyInternal().stream().findFirst()
		// .get();
		// String funcName = funcSymbol.getName();
		// if (funcName.equals("free")) {
		// /*
		// * Now, we should get the pointer that is being freed, and emulate
		// * "ptr = NULL;"
		// */
		// CellSet lhsSet = new CellSet();
		// SimplePrimaryExpression firstArgSPE = n.getArgumentList().get(0);
		// if (firstArgSPE.isAConstant()) {
		// return flowFactOUT;
		// }
		// NodeToken firstArg = firstArgSPE.getIdentifier();
		// Symbol argSymbol = Misc.getSymbolEntry(firstArg.getTokenImage(), n);
		// lhsSet.add(argSymbol);
		//
		// CellSet rhsPtsToSet = new CellSet();
		// rhsPtsToSet.add(Symbol.getNullCell());
		//
		// CellMap<CellSet> affectedMap =
		// OptimizedPointsToUpdateGetter.generateUpdateMap(n, lhsSet, rhsPtsToSet);
		// if (!affectedMap.isEmpty()) {
		// flowFactOUT = new OptimizedPointsToFlowFact(flowFactIN);
		// flowFactOUT.flowMap.mergeWith(affectedMap, (s1, s2) -> {
		// CellSet s3;
		// if (s2 != null) {
		// s3 = s2; // We use same CellSet objects as present in affectedMap.
		// } else {
		// s3 = s1; // Implies no change.
		// }
		// return s3;
		// }, null);
		// }
		// return flowFactOUT;
		// }
		// return flowFactOUT;
	}

	/**
	 * Special Node
	 */
	@Override
	public PointsToFlowMap visit(PostCallNode n, PointsToFlowMap flowFactIN) {
		PointsToFlowMap flowFactOUT = new PointsToFlowMap(flowFactIN);
		if (!n.hasReturnReceiver()) {
			return flowFactOUT;
		}
		SimplePrimaryExpression retSPE = n.getReturnReceiver();
		assert (retSPE.isAnIdentifier());
		Cell capturingSym = Misc.getSymbolOrFreeEntry(retSPE.getIdentifier());
		assert (capturingSym != null);
		if (capturingSym instanceof Symbol) {
			Symbol sym = (Symbol) capturingSym;
			if (!(sym.getType() instanceof PointerType)) {
				return flowFactOUT;
			}
		}
		Set<Cell> tempLHSSet = new HashSet<>();
		tempLHSSet.add(capturingSym);
		ImmutableCellSet lhsSet = new ImmutableCellSet(tempLHSSet);

		CellSet rhsPtsToSet = new CellSet();
		CallStatement callStmt = n.getParent();
		for (FunctionDefinition funcDef : callStmt.getInfo().getCalledDefinitions()) {
			for (ReturnStatement ret : Misc.getInheritedEnclosee(funcDef, ReturnStatement.class)) {
				Expression e = ret.getInfo().getReturnExpression();
				if (e == null) {
					continue;
				}
				CellList locationsOfE = e.getInfo().getLocationsOf();
				if (locationsOfE.isUniversal()) {
					rhsPtsToSet.add(Cell.genericCell);
				} else {
					for (Cell c : locationsOfE) {
						rhsPtsToSet.addAll(c.getPointsTo(ret));
					}
				}
			}
		}

		if (callStmt.getInfo().getCalledDefinitions().isEmpty()) {
			rhsPtsToSet.addAll(HeapCell.getHeapCells(callStmt));
		}

		CellMap<ImmutableCellSet> affectedMap = OptimizedPointsToUpdateGetter.generateUpdateMap(n, lhsSet,
				new ImmutableCellSet(rhsPtsToSet));
		if (!affectedMap.isEmpty()) {
			flowFactOUT.getFlowMap().mergeWith(affectedMap, (s1, s2) -> {
				ImmutableCellSet s3;
				if (s2 != null) {
					s3 = s2; // We use same CellSet objects as present in affectedMap.
				} else {
					s3 = s1; // Implies no change.
				}
				return s3;
			}, null);
		}
		return flowFactOUT;
	}

	/**
	 * Given a parameter-declaration {@code parameter}, and a
	 * simple-primary-expression {@code argument} that represents the argument
	 * for this parameter from some call-site, this method should model the
	 * flow-function of the write to the parameter that happens implicitly.
	 *
	 * @param parameter
	 *                   a {@code ParameterDeclaration} which needs to be assigned
	 *                   with
	 *                   the {@code argument}.
	 * @param argument
	 *                   a {@code SimplePrimaryExpression} which is assigned to the
	 *                   {@code parameter}.
	 * @param flowFactIN
	 *                   the IN flow-fact for the implicit assignment of the
	 *                   {@code argument} to the {@code parameter}.
	 * @return
	 *         the OUT flow-fact, as a result of the implicit assignment of the
	 *         {@code argument} to the {@code parameter}.
	 */
	@Override
	public PointsToFlowMap writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument,
			PointsToFlowMap flowFactIN) {
		PointsToFlowMap flowFactOUT = flowFactIN;
		if (argument.isAConstant()) {
			return flowFactOUT;
		}
		NodeToken paramNodeToken = ParameterDeclarationInfo.getRootParamNodeToken(parameter);
		if (paramNodeToken == null) {
			// For "void" parameter.
			return flowFactOUT;
		}
		Cell paramCell = Misc.getSymbolOrFreeEntry(paramNodeToken);
		Cell argCell = Misc.getSymbolOrFreeEntry(argument.getIdentifier());
		assert (paramCell != null && argCell != null);
		Set<Cell> tempSet = new HashSet<>();
		tempSet.add(paramCell);
		if (paramCell instanceof Symbol) {
			Symbol param = (Symbol) paramCell;
			if (!(param.getType() instanceof PointerType)) {
				return flowFactOUT;
			}
		}
		ImmutableCellSet lhsSet = new ImmutableCellSet(tempSet);
		CellSet rhsPtsToSet = new CellSet();
		if (argCell == Cell.genericCell) {
			rhsPtsToSet.add(Cell.genericCell);
		} else {
			rhsPtsToSet.addAll(argCell.getPointsTo(argument));
		}
		CellMap<ImmutableCellSet> affectedMap = OptimizedPointsToUpdateGetter.generateUpdateMap(parameter, lhsSet,
				new ImmutableCellSet(rhsPtsToSet));
		if (!affectedMap.isEmpty()) {
			flowFactOUT = new PointsToFlowMap(flowFactIN);
			flowFactOUT.getFlowMap().mergeWith(affectedMap, (s1, s2) -> {
				ImmutableCellSet s3;
				if (s2 != null) {
					s3 = s2; // We use same CellSet objects as present in affectedMap.
				} else {
					s3 = s1; // Implies no change.
				}
				return s3;
			}, null);
		}

		return flowFactOUT;
	}

	private static PointsToFlowMap updateOptimizedPointsTo(Expression n, PointsToFlowMap flowFactIN) {
		PointsToFlowMap flowFactOUT = flowFactIN;
		OptimizedPointsToUpdateGetter ptsToUpdateGetter = new OptimizedPointsToUpdateGetter();
		n.accept(ptsToUpdateGetter);
		CellMap<ImmutableCellSet> affectedMap = ptsToUpdateGetter.updateMap;
		if (!affectedMap.isEmpty()) {
			flowFactOUT = new PointsToFlowMap(flowFactIN);
			flowFactOUT.getFlowMap().mergeWith(affectedMap, (s1, s2) -> {
				ImmutableCellSet s3;
				if (s2 != null) {
					s3 = s2; // We use same CellSet objects as present in affectedMap.
				} else {
					s3 = s1; // Implies no change.
				}
				return s3;
			}, null);
		}
		return flowFactOUT;
	}

	/**
	 *
	 * This visitor is used to obtain the "modifications" in the points-to sets
	 * of various symbols, as a result of the symbolic execution of the visited
	 * expression.
	 *
	 * @author Aman Nougrahiya
	 *
	 */
	private static class OptimizedPointsToUpdateGetter extends DepthFirstProcess {
		public CellMap<ImmutableCellSet> updateMap = new ExtensibleCellMap<>();

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public void visit(NonConditionalExpression n) {
			ImmutableCellSet lhsSet = new ImmutableCellSet(n.getF0().getInfo().getLocationsOf());
			ImmutableCellSet rhsSet = new ImmutableCellSet(n.getF2().getInfo().getLocationsOf());
			if (rhsSet.isEmpty() || lhsSet.isEmpty()) {
				return;
			}
			for (Cell lhsCell : lhsSet) {
				if (lhsCell instanceof Symbol) {
					Symbol lhsSym = (Symbol) lhsCell;
					if (lhsSym != Cell.genericCell && !(lhsSym.getType() instanceof PointerType)) {
						return;
					}
				}
			}
			CellSet rhsPtsToSet = new CellSet();
			if (rhsSet.isUniversal()) {
				rhsPtsToSet.add(Cell.genericCell);
			} else {
				for (Cell rhsCell : rhsSet) {
					rhsPtsToSet.addAll(rhsCell.getPointsTo(n));
				}
			}
			if (!rhsPtsToSet.isEmpty()) {
				CellMap<ImmutableCellSet> tempUpdateMap = OptimizedPointsToUpdateGetter.generateUpdateMap(n, lhsSet,
						new ImmutableCellSet(rhsPtsToSet));
				updateMap.mergeWith(tempUpdateMap, (s1, s2) -> {
					ImmutableCellSet s3;
					if (s2 != null) {
						s3 = s2; // We use same CellSet objects as present in affectedMap.
					} else {
						s3 = s1; // Implies no change.
					}
					return s3;
				}, null);
			}
		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public void visit(UnarySizeofExpression n) {
			// Expression is not evaluated when present as the argument
			// to sizeof operator.
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(SizeofUnaryExpression n) {
			// Expression is not evaluated when present as the argument to sizeof expression
		}

		public static CellMap<ImmutableCellSet> generateUpdateMap(Node n, ImmutableCellSet lhsSet,
				ImmutableCellSet rhsPtsToSet) {
			CellMap<ImmutableCellSet> updateMap = new ExtensibleCellMap<>();
			if (lhsSet == null || lhsSet.isEmpty() || rhsPtsToSet == null || rhsPtsToSet.isEmpty()) {
				return updateMap;
			}
			boolean strongUpdate = true;
			if (lhsSet.size() > 1) {
				strongUpdate = false;
			} else {
				Cell cell = lhsSet.getAnyElement();
				if (cell == Cell.genericCell) {
					strongUpdate = false;
				}
				if (cell instanceof HeapCell) {
					strongUpdate = false; // See if we need to optimize this.
				} else if (cell instanceof Symbol) {
					Type t = ((Symbol) cell).getType();
					if (t instanceof ArrayType || t instanceof StructType || t instanceof UnionType) {
						strongUpdate = false;
					}
				}
			}

			if (strongUpdate) {
				Cell lhsCell = lhsSet.getAnyElement();
				if (lhsCell == Cell.getNullCell()) {
					return updateMap;
				}
				updateMap.put(lhsCell, rhsPtsToSet);
				return updateMap;
			} else {
				if (lhsSet.isUniversal()) {
					// TODO: Think about this further.
					Set<Cell> tempUniversal = new HashSet<>();
					tempUniversal.add(Cell.genericCell);
					ImmutableCellSet universalSet = new ImmutableCellSet(tempUniversal);
					updateMap.put(Cell.genericCell, universalSet);
					return updateMap;
				} else {
					for (Cell lhsCell : lhsSet) {
						if (lhsCell == Cell.getNullCell()) {
							continue;
						}
						ImmutableCellSet lhsOldPtsToSet = new ImmutableCellSet(lhsCell.getPointsTo(n));
						CellSet newPtsToSet = new CellSet(lhsOldPtsToSet);
						newPtsToSet.addAll(rhsPtsToSet);
						if (!lhsOldPtsToSet.equals(newPtsToSet)) {
							updateMap.put(lhsCell, new ImmutableCellSet(newPtsToSet));
						}
					}
					return updateMap;
				}
			}
		}

	}
}
