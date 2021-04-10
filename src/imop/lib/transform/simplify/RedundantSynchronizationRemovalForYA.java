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
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.mhp.YPhasePoint;
import imop.lib.analysis.mhp.yuan.YPhase;
import imop.lib.cfg.info.CompoundStatementCFGInfo;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.lib.util.ProfileSS;
import imop.parser.Program;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedundantSynchronizationRemovalForYA {
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
	 * UPDATE: 07-Dec-2017 11:27:10 PM (Edited by: aman) <br>
	 * We replace barriers with a flush, to be safe. Note that
	 * this replacement may not be required.
	 *
	 * @param root
	 *             Root AST node under which redundant barriers have to be removed.
	 */
	public static void removeBarriers(Node root) {
		for (Node barrierNode : Misc.getInheritedEncloseeList(root, BarrierDirective.class)) {
			BarrierDirective barrier = (BarrierDirective) barrierNode;
			Set<YPhase> allPhaseSet = new HashSet<>();
			for (ParallelConstruct parConsNode : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
				allPhaseSet.addAll((Collection<? extends YPhase>) parConsNode.getInfo().getConnectedPhases());
			}
			Set<YPhase> phasesAbove = (Set<YPhase>) barrier.getInfo().getNodePhaseInfo().getPhaseSet();
			Set<YPhase> phasesBelow = new HashSet<>();
			for (YPhase ph : allPhaseSet) {
				for (AbstractPhasePointable bpp : ph.getBeginPoints()) {
					if (bpp.getNodeFromInterface() == barrier) {
						phasesBelow.add(ph);
					}
				}
			}

			boolean removable = true;
			outer: for (YPhase phAbove : phasesAbove) {
				for (YPhase phBelow : phasesBelow) {
					if (phAbove == phBelow) {
						continue;
					}
					if (RedundantSynchronizationRemovalForYA.phasesConflictForBarrier(phAbove, phBelow, barrier)) {
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
				ProfileSS.insertCP();
				// Phase.removeUnreachablePhases(); // Newly added code.
				removeBarriers(root);
				ProfileSS.insertCP();
				return;
			}
		}
	}

	public static boolean phasesConflictForBarrier(YPhase ph1, YPhase ph2, BarrierDirective barrier) {
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
			if (RedundantSynchronizationRemovalForYA.setsConflictForBarrier(ph1, masterNodesInP1, barrier, ph2,
					nodesInP2)) {
				return true;
			}
		}
		if (!masterNodesInP2.isEmpty()) {
			if (RedundantSynchronizationRemovalForYA.setsConflictForBarrier(ph1, nodesInP1, barrier, ph2,
					masterNodesInP2)) {
				return true;
			}
		}
		if (RedundantSynchronizationRemovalForYA.setsConflictForBarrier(ph1, nodesInP1, barrier, ph2, nodesInP2)) {
			return true;
		}

		return false;
	}

	public static boolean setsConflictForBarrier(YPhase phAbove, Set<Node> nodesAbove, BarrierDirective barrier,
			YPhase phBelow, Set<Node> nodesBelow) {
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
		Set<YPhasePoint> belowBPP = (Set<YPhasePoint>) phBelow.getBeginPoints();
		for (Node nodeBelow : nodesBelow) {
			if (belowBPP.stream().filter(bpp -> bpp.getNode() == barrier)
					.anyMatch(bpp -> bpp.getReachableNodes().contains(nodeBelow))) {
				// Refer to rule #3 in "Synchronization elimination --> Modeling
				// SVE-sensitivity."
				coExistBelow.add(nodeBelow);
				continue;
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
			for (Node nBelow : nodesBelow) {
				if (!coExistBelow.contains(nBelow)) {
					continue;
				}
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

	public static boolean canBarrierBeRemoved(BarrierDirective barrier) {
		Set<YPhase> phasesAbove = (Set<YPhase>) barrier.getInfo().getNodePhaseInfo().getPhaseSet();
		for (YPhase phAbove : phasesAbove) {
			for (AbstractPhase<?, ?> phBelowAbs : phAbove.getSuccPhases()) {
				YPhase phBelow = (YPhase) phBelowAbs;
				if (phAbove == phBelow) {
					continue;
				}
				if (RedundantSynchronizationRemovalForYA.phasesConflict(phAbove, phBelow)) {
					return false;
				}
			}
			// if (RedundantSynchronizationRemoval.phasesConflictForBarrier(phAbove,
			// phBelow, barrier)) {
			// return false;
			// }
		}
		return true;
	}

	public static boolean phasesConflict(YPhase ph1, YPhase ph2) {
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
			if (RedundantSynchronizationRemovalForYA.setsConflict(masterNodesInP1, nodesInP2)) {
				return true;
			}
		}
		if (!masterNodesInP2.isEmpty()) {
			if (RedundantSynchronizationRemovalForYA.setsConflict(nodesInP1, masterNodesInP2)) {
				return true;
			}
		}
		if (RedundantSynchronizationRemovalForYA.setsConflict(nodesInP1, nodesInP2)) {
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
