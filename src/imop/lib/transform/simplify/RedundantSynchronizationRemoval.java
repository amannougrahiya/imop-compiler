/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.simplify;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.analysis.mhp.incMHP.EndPhasePoint;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.analysis.solver.FieldSensitivity;
import imop.lib.cfg.info.CompoundStatementCFGInfo;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.transform.updater.NodeRemover;
import imop.lib.util.CellSet;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;
import imop.lib.util.ProfileSS;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedundantSynchronizationRemoval {
	public static void mergePhasesOf(ParallelConstruct parCons) {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return;
		}
		phaseLoop: for (AbstractPhase<?, ?> absPh : new ArrayList<>(parCons.getInfo().getConnectedPhases())) {
			Phase ph = (Phase) absPh;
			/*
			 * Step 1: Check if there exists any barrier between ph and its
			 * succ, which can NOT be removed.
			 * If so, then continue over to the next phase.
			 */
			for (EndPhasePoint epp : ph.getEndPoints()) {
				Node epNode = epp.getNode();
				if (epNode instanceof EndNode) {
					// Ignore this barrier.
					continue;
				}
				BarrierDirective endingBarr = (BarrierDirective) epNode;
				if (!canBarrierBeRemoved(endingBarr)) {
					continue phaseLoop;
				}
			}
			/*
			 * Step 2: If we have reached till here, then it implies that there
			 * is no barrier at the border of this phase (with its successor
			 * phase) that cannot be removed.
			 * In such a case, let's simply remove all the barriers using
			 * NodeRemover.
			 */
			boolean anyBarrierRemoved = false;
			for (EndPhasePoint epp : ph.getEndPoints()) {
				Node epNode = epp.getNode();
				if (epNode instanceof EndNode) {
					// Ignore this barrier.
					continue;
				}
				System.err.println("** Removing the barrier at line #" + Misc.getLineNum(epp.getNode()));
				NodeRemover.removeNode(epp.getNode());
				anyBarrierRemoved = true;
			}
			if (anyBarrierRemoved) {
				/*
				 * Step 3: If any barrier was removed, then allStablePhaseList
				 * would have been updated.
				 * Hence we visit this method recursively.
				 */
				RedundantSynchronizationRemoval.mergePhasesOf(parCons);
			}
		}
	}

	private static int counter = 0;

	/**
	 * This method removes redundant barriers from the root. A barrier <i>b</i> is
	 * redundant, if there exists no pair of
	 * phases,
	 * <i>(p1, p2)</i>, and a shared variable <i>s</i>, such that <i>p1</i> ends
	 * at <i>b</i>,
	 * <i>p2</i> starts at <i>b</i>, and there exists an anti-, true- or output-
	 * dependence on <i>s</i> between <i>p1</i> and <i>p2</i>.
	 *
	 * <br>
	 * UPDATE: 07-Dec-2017 11:27:10 PM (Edited by: Aman) <br>
	 * We replace barriers with a flush, to be safe. Note that
	 * this replacement may not be required.
	 *
	 * @param root
	 *             Root AST node under which redundant barriers have to be removed.
	 */
	public static void removeBarriers(Node root) {
		if (1 == 2) {
			DumpSnapshot.dumpPhases("rem" + Program.mhpUpdateCategory + counter);
			DumpSnapshot.dumpPointsTo("rem" + Program.mhpUpdateCategory + counter);
			DumpSnapshot.dumpPredicates("rem" + Program.mhpUpdateCategory + counter);
			DumpSnapshot.dumpVisibleSharedReadWrittenCells("rem" + Program.mhpUpdateCategory + counter);
			DumpSnapshot.dumpRoot("root" + Program.updateCategory + counter++);
		}
		for (Node barrierNode : Misc.getInheritedEncloseeList(root, BarrierDirective.class)) {
			BarrierDirective barrier = (BarrierDirective) barrierNode;
			Set<Phase> allPhaseSet = new HashSet<>();
			for (ParallelConstruct parConsNode : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
				allPhaseSet.addAll((Collection<? extends Phase>) parConsNode.getInfo().getConnectedPhases());
			}
			Set<Phase> phasesAbove = (Set<Phase>) barrier.getInfo().getNodePhaseInfo().getPhaseSet();
			if (Program.phaseEmptySetChecker && phasesAbove.isEmpty()) {
				if (!Misc.getEnclosingFunction(barrier).getInfo().getCallersOfThis().isEmpty()) {
					Misc.exitDueToError("Found a barrier that does not possess any phase information.");
				}
			}
			Set<Phase> phasesBelow = new HashSet<>();
			for (Phase ph : allPhaseSet) {
				for (BeginPhasePoint bpp : ph.getBeginPoints()) {
					if (bpp.getNode() == barrier) {
						phasesBelow.add(ph);
					}
				}
			}
			if (Program.phaseEmptySetChecker && phasesBelow.isEmpty()) {
				if (!Misc.getEnclosingFunction(barrier).getInfo().getCallersOfThis().isEmpty()) {
					Misc.exitDueToError("Found a barrier that does not start any phase!");
				}
			}

			boolean removable = true;
			outer: for (Phase phAbove : phasesAbove) {
				for (Phase phBelow : phasesBelow) {
					if (phAbove == phBelow) {
						continue;
					}
					// TODO: Try adding a check where which stops processing of two phases unless
					// one is the predecessor of another.
					if (RedundantSynchronizationRemoval.phasesConflictForBarrier(phAbove, phBelow, barrier)) {
						removable = false;
						break outer;
					}
				}
			}
			if (removable) {
				// Update: Not replacing the barrier with a flush now.
				System.err.println("** Removing the barrier at line #" + Misc.getLineNum(barrier));
				// System.err.println("** Replacing the barrier at line #" +
				// Misc.getLineNum(barrier) + " with a flush.");
				CompoundStatement enclosingCS = (CompoundStatement) Misc.getEnclosingBlock(barrier);
				CompoundStatementCFGInfo csCFGInfo = enclosingCS.getInfo().getCFGInfo();
				// int index = csCFGInfo.getElementList().indexOf(barrier);
				// FlushDirective flush = FrontEnd.parseAlone("#pragma omp flush",
				// FlushDirective.class);
				// csCFGInfo.addElement(index, flush);
				csCFGInfo.removeElement(barrier);
				ProfileSS.insertCP(); // RCP
				AutomatedUpdater.stabilizePTAInCPModes();
				// Phase.removeUnreachablePhases(); // Newly added code.
				removeBarriers(root);
				ProfileSS.insertCP(); // RCP
				AutomatedUpdater.stabilizePTAInCPModes();
				return;
			}
		}
	}

	public static boolean phasesConflictForBarrier(Phase ph1, Phase ph2, BarrierDirective barrier) {
		Set<Node> nodesInP1 = new HashSet<>(ph1.getNodeSet());
		Set<Node> nodesInP2 = new HashSet<>(ph2.getNodeSet());

		Set<MasterConstruct> allMasterConstructs = new HashSet<>();
		for (Node n1 : nodesInP1) {
			if (n1 instanceof BeginNode) {
				BeginNode beginNode = (BeginNode) n1;
				if (beginNode.getParent() instanceof MasterConstruct) {
					MasterConstruct master = (MasterConstruct) beginNode.getParent();
					// Conservatively check that this master construct applies to the
					// parallel-construct of ph1.
					ParallelConstruct parentPar = Misc.getEnclosingNode(master, ParallelConstruct.class);
					if (parentPar == null || !parentPar.getInfo().getConnectedPhases().contains(ph1)) {
						continue;
					}
					allMasterConstructs.add(master);
				}
			}
		}
		Set<Node> masterNodesInP1 = new HashSet<>();
		for (MasterConstruct masterCons : allMasterConstructs) {
			masterNodesInP1.addAll(masterCons.getInfo().getCFGInfo().getIntraTaskCFGLeafContents());
		}
		nodesInP1.removeAll(masterNodesInP1);

		allMasterConstructs = new HashSet<>();
		for (Node n2 : nodesInP2) {
			if (n2 instanceof BeginNode) {
				BeginNode beginNode = (BeginNode) n2;
				if (beginNode.getParent() instanceof MasterConstruct) {
					MasterConstruct master = (MasterConstruct) beginNode.getParent();
					// Conservatively check that this master construct applies to the
					// parallel-construct of ph2.
					ParallelConstruct parentPar = Misc.getEnclosingNode(master, ParallelConstruct.class);
					if (parentPar == null || !parentPar.getInfo().getAllPhaseList().contains(ph2)) {
						continue;
					}
					allMasterConstructs.add(master);
				}
			}
		}
		Set<Node> masterNodesInP2 = new HashSet<>();
		for (MasterConstruct masterCons : allMasterConstructs) {
			masterNodesInP2.addAll(masterCons.getInfo().getCFGInfo().getIntraTaskCFGLeafContents());
		}
		nodesInP2.removeAll(masterNodesInP2);

		/*
		 * Return true, if any of the following collisions exist:
		 * - masterNodesInP1 with nodesinP2
		 * - masterNodesInP2 with nodesInP1
		 * - nodesInP1 with nodesInP2
		 */
		if (!masterNodesInP1.isEmpty()) {
			if (RedundantSynchronizationRemoval.setsConflictForBarrier(ph1, masterNodesInP1, barrier, ph2, nodesInP2)) {
				return true;
			}
		}
		if (!masterNodesInP2.isEmpty()) {
			if (RedundantSynchronizationRemoval.setsConflictForBarrier(ph1, nodesInP1, barrier, ph2, masterNodesInP2)) {
				return true;
			}
		}
		if (RedundantSynchronizationRemoval.setsConflictForBarrier(ph1, nodesInP1, barrier, ph2, nodesInP2)) {
			return true;
		}

		return false;
	}

	public static boolean setsConflictForBarrier(Phase phAbove, Set<Node> nodesAbove, BarrierDirective barrier,
			Phase phBelow, Set<Node> nodesBelow) {
		// /*
		// * Debug code starts.
		// */
		// boolean DF = false;
		// if (nodesAbove.stream().anyMatch(n ->
		// n.toString().contains("diff_imopVarPre77 += mydiff;"))
		// && nodesBelow.stream().anyMatch(n -> n.toString().contains("(float)
		// diff_imopVarPre77"))) {
		// DF = true;
		// System.out.println("Yes");
		// }
		//
		// /*
		// * Debug code ends.
		// */
		/*
		 * Extract only those nodes from nodesBelow that may co-exist with the
		 * barrier in phBelow.
		 */
		Set<Node> coExistBelow = new HashSet<>();
		Set<BeginPhasePoint> belowBPP = phBelow.getBeginPoints();
		for (Node nBelow : nodesBelow) {
			CellSet readBelow = nBelow.getInfo().getSharedReads();
			CellSet writtenBelow = nBelow.getInfo().getSharedWrites();
			if (readBelow.isEmpty() && writtenBelow.isEmpty()) {
				continue;
			}
			if (belowBPP.stream().filter(bpp -> bpp.getNode() == barrier)
					.anyMatch(bpp -> bpp.getReachableNodes().contains(nBelow))) {
				// Refer to rule #3 in "Synchronization elimination --> Modeling
				// SVE-sensitivity."
				coExistBelow.add(nBelow);
				continue;
			}
			if (belowBPP.stream()
					.filter(bpp -> bpp.getReachableNodes().contains(nBelow)
							&& bpp.getNode().getInfo().getNodePhaseInfo().getPhaseSet().contains(phAbove))
					.anyMatch(bpp -> CoExistenceChecker.canCoExistInPhase(bpp.getNode(), barrier, phAbove))) {
				// Refer to rule #3 in "Synchronization elimination --> Modeling
				// SVE-sensitivity."
				coExistBelow.add(nBelow);
			}
		}
		for (Node nAbove : nodesAbove) {
			CellSet readAbove = nAbove.getInfo().getSharedReads();
			CellSet writtenAbove = nAbove.getInfo().getSharedWrites();
			CellSet nonFieldReadsInSource = null;
			CellSet nonFieldWritesInSource = null;
			if (readAbove.isEmpty() && writtenAbove.isEmpty()) {
				continue;
			}
			if (!CoExistenceChecker.canCoExistInPhase(nAbove, barrier, phAbove)) {
				continue;
			}
			if (Program.fieldSensitive) {
				nonFieldReadsInSource = nAbove.getInfo().getNonFieldSharedReads();
				nonFieldWritesInSource = nAbove.getInfo().getNonFieldSharedWrites();
			}

			for (Node nBelow : coExistBelow) {
				if (Program.fieldSensitive) {
					CellSet nonFieldReadsInDestination = nBelow.getInfo().getNonFieldSharedReads();
					CellSet nonFieldWritesInDestination = nBelow.getInfo().getNonFieldSharedWrites();
					if (nonFieldWritesInSource.overlapsWith(nonFieldWritesInDestination)
							|| nonFieldWritesInSource.overlapsWith(nonFieldReadsInDestination)
							|| nonFieldReadsInSource.overlapsWith(nonFieldWritesInDestination)) {
						return true;
					} else {
						if (FieldSensitivity.canConflictWithTwoThreads(nAbove, nBelow)) {
							return true;
						}
					}
				} else {
					CellSet readBelow = nBelow.getInfo().getSharedReads(); // Note: These shared-reads would always be
																			// from cached values.
					CellSet writtenBelow = nBelow.getInfo().getSharedWrites(); // Note: These shared-writes would always
																				// be from cached values.
					if (Misc.doIntersect(readAbove, writtenBelow) || Misc.doIntersect(readBelow, writtenAbove)
							|| Misc.doIntersect(writtenAbove, writtenBelow)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void removeBarriersFromAllParConsWithin(Node root) {
		for (ParallelConstruct parCons : Misc.getExactEnclosee(root, ParallelConstruct.class)) {
			RedundantSynchronizationRemoval.mergePhasesOf(parCons);
		}
	}

	public static boolean canBarrierBeRemoved(BarrierDirective barrier) {
		Set<Phase> phasesAbove = (Set<Phase>) barrier.getInfo().getNodePhaseInfo().getPhaseSet();
		for (Phase phAbove : phasesAbove) {
			Phase phBelow = phAbove.getSuccPhase();
			if (phAbove == phBelow) {
				continue;
			}
			if (RedundantSynchronizationRemoval.phasesConflict(phAbove, phBelow)) {
				return false;
			}
			// if (RedundantSynchronizationRemoval.phasesConflictForBarrier(phAbove,
			// phBelow, barrier)) {
			// return false;
			// }
		}
		return true;
	}

	public static boolean phasesConflict(Phase ph1, Phase ph2) {
		if (ph1 == ph2) {
			return false;
		}
		Set<Node> nodesInP1 = new HashSet<>(ph1.getNodeSet());
		Set<Node> nodesInP2 = new HashSet<>(ph2.getNodeSet());

		Set<MasterConstruct> allMasterConstructs = new HashSet<>();
		for (Node n1 : nodesInP1) {
			if (n1 instanceof BeginNode) {
				BeginNode beginNode = (BeginNode) n1;
				if (beginNode.getParent() instanceof MasterConstruct) {
					MasterConstruct master = (MasterConstruct) beginNode.getParent();
					// Conservatively check that this master construct applies to the
					// parallel-construct of ph1.
					ParallelConstruct parentPar = Misc.getEnclosingNode(master, ParallelConstruct.class);
					if (parentPar == null || !parentPar.getInfo().getConnectedPhases().contains(ph1)) {
						continue;
					}
					allMasterConstructs.add(master);
				}
			}
		}
		Set<Node> masterNodesInP1 = new HashSet<>();
		for (MasterConstruct masterCons : allMasterConstructs) {
			masterNodesInP1.addAll(masterCons.getInfo().getCFGInfo().getIntraTaskCFGLeafContents());
		}
		nodesInP1.removeAll(masterNodesInP1);

		allMasterConstructs = new HashSet<>();
		for (Node n2 : nodesInP2) {
			if (n2 instanceof BeginNode) {
				BeginNode beginNode = (BeginNode) n2;
				if (beginNode.getParent() instanceof MasterConstruct) {
					MasterConstruct master = (MasterConstruct) beginNode.getParent();
					// Conservatively check that this master construct applies to the
					// parallel-construct of ph2.
					ParallelConstruct parentPar = Misc.getEnclosingNode(master, ParallelConstruct.class);
					if (parentPar == null || !parentPar.getInfo().getAllPhaseList().contains(ph2)) {
						continue;
					}
					allMasterConstructs.add(master);
				}
			}
		}
		Set<Node> masterNodesInP2 = new HashSet<>();
		for (MasterConstruct masterCons : allMasterConstructs) {
			masterNodesInP2.addAll(masterCons.getInfo().getCFGInfo().getIntraTaskCFGLeafContents());
		}
		nodesInP2.removeAll(masterNodesInP2);

		/*
		 * Return true, if any of the following collisions exist:
		 * - masterNodesInP1 with nodesinP2
		 * - masterNodesInP2 with nodesInP1
		 * - nodesInP1 with nodesInP2
		 */
		if (!masterNodesInP1.isEmpty()) {
			if (RedundantSynchronizationRemoval.setsConflict(masterNodesInP1, nodesInP2)) {
				return true;
			}
		}
		if (!masterNodesInP2.isEmpty()) {
			if (RedundantSynchronizationRemoval.setsConflict(nodesInP1, masterNodesInP2)) {
				return true;
			}
		}
		if (RedundantSynchronizationRemoval.setsConflict(nodesInP1, nodesInP2)) {
			return true;
		}

		return false;
	}

	public static boolean setsConflict(Set<Node> nodesAbove, Set<Node> nodesBelow) {
		for (Node nAbove : nodesAbove) {
			CellSet readAbove = nAbove.getInfo().getSharedReads();
			CellSet writtenAbove = nAbove.getInfo().getSharedWrites();
			if (readAbove.isEmpty() && writtenAbove.isEmpty()) {
				continue;
			}
			for (Node nBelow : nodesBelow) {
				CellSet readBelow = nBelow.getInfo().getSharedReads();
				CellSet writtenBelow = nBelow.getInfo().getSharedWrites();
				if (Misc.doIntersect(readAbove, writtenBelow) || Misc.doIntersect(readBelow, writtenAbove)
						|| Misc.doIntersect(writtenAbove, writtenBelow)) {
					return true;
				}
			}
		}
		return false;
	}
}
