/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp;

import imop.ast.node.external.*;
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.analysis.mhp.yuan.YPhase;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.lib.util.NodePair;
import imop.parser.Program;
import imop.parser.Program.ConcurrencyAlgorithm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DependenceCounter {
	private static int counter = 0;

	public static void printCoExistenceCount(Node root) {
		Set<NodePair> allPairs = new HashSet<>();
		int sum = 0;
		Set<AbstractPhase<?, ?>> allPhaseSet = new HashSet<>();
		for (ParallelConstruct parConsNode : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			allPhaseSet.addAll(parConsNode.getInfo().getConnectedPhases());
		}
		for (Node n1 : Program.getRoot().getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
			CellSet reads = n1.getInfo().getSharedReads();
			CellSet writes = n1.getInfo().getSharedWrites();
			if (reads.isEmpty() && writes.isEmpty()) {
				continue;
			}
			for (Node n2 : Program.getRoot().getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
				reads = n2.getInfo().getSharedReads();
				writes = n2.getInfo().getSharedWrites();
				if (reads.isEmpty() && writes.isEmpty()) {
					continue;
				}
				if (Program.concurrencyAlgorithm == ConcurrencyAlgorithm.ICON) {
					sum += CoExistenceChecker.canCoExistInAnyPhase(n1, n2) ? 1 : 0;
				} else { // YUAN:
					if (allPhaseSet.stream()
							.anyMatch(ph -> ph.getNodeSet().contains(n1) && ph.getNodeSet().contains(n2))) {
						sum++;
					}
				}
			}
		}
		System.err.println("Total coexistence count with " + Program.concurrencyAlgorithm + ": " + sum);
	}

	public static void printBarrierDependents(Node root) {
		int id = 0;
		Set<NodePair> allPairs = new HashSet<>();
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YCON) {
			for (Node barrierNode : Misc.getInheritedPostOrderEnclosee(root, BarrierDirective.class)) {
				Set<NodePair> nodePairs = new HashSet<>();
				BarrierDirective barrier = (BarrierDirective) barrierNode;
				Set<AbstractPhase<?, ?>> allPhaseSet = new HashSet<>();
				for (ParallelConstruct parConsNode : Misc.getExactEnclosee(Program.getRoot(),
						ParallelConstruct.class)) {
					allPhaseSet.addAll(parConsNode.getInfo().getConnectedPhases());
				}
				Set<YPhase> phasesAbove = (Set<YPhase>) barrier.getInfo().getNodePhaseInfo().getPhaseSet();
				Set<YPhase> phasesBelow = new HashSet<>();
				for (AbstractPhase<?, ?> absPh : allPhaseSet) {
					for (AbstractPhasePointable bpp : absPh.getBeginPoints()) {
						if (bpp.getNodeFromInterface() == barrier) {
							phasesBelow.add((YPhase) absPh);
						}
					}
				}

				for (YPhase phAbove : phasesAbove) {
					for (YPhase phBelow : phasesBelow) {
						if (phAbove == phBelow) {
							continue;
						}
						Set<Node> nodesInP1 = new HashSet<>(phAbove.getNodeSet());
						Set<Node> nodesInP2 = new HashSet<>(phBelow.getNodeSet());
						for (Node nAbove : nodesInP1) {
							CellSet readAbove = nAbove.getInfo().getSharedReads();
							CellSet writtenAbove = nAbove.getInfo().getSharedWrites();
							if (readAbove.isEmpty() && writtenAbove.isEmpty()) {
								continue;
							}
							for (Node nBelow : nodesInP2) {
								CellSet readBelow = nBelow.getInfo().getSharedReads(); // Note: These shared-reads would
																						// always be from cached values.
								CellSet writtenBelow = nBelow.getInfo().getSharedWrites(); // Note: These shared-writes
																							// would always be from
																							// cached values.
								if (readBelow.isEmpty() && writtenBelow.isEmpty()) {
									continue;
								}
								nodePairs.add(new NodePair(nAbove, nBelow));
							}
						}
					}
				}
				System.out.println("Barr#" + id++ + ": " + nodePairs.size());
				allPairs.addAll(nodePairs);
			}
			System.out.println("Total: " + allPairs.size());
		} else {
			for (Node barrierNode : Misc.getInheritedPostOrderEnclosee(root, BarrierDirective.class)) {
				Set<NodePair> nodePairs = new HashSet<>();
				BarrierDirective barrier = (BarrierDirective) barrierNode;
				Set<Phase> allPhaseSet = new HashSet<>();
				for (ParallelConstruct parConsNode : Misc.getExactEnclosee(Program.getRoot(),
						ParallelConstruct.class)) {
					allPhaseSet.addAll((Collection<? extends Phase>) parConsNode.getInfo().getConnectedPhases());
				}
				Set<Phase> phasesAbove = (Set<Phase>) barrier.getInfo().getNodePhaseInfo().getPhaseSet();
				Set<Phase> phasesBelow = new HashSet<>();
				for (Phase ph : allPhaseSet) {
					for (BeginPhasePoint bpp : ph.getBeginPoints()) {
						if (bpp.getNode() == barrier) {
							phasesBelow.add(ph);
						}
					}
				}

				for (Phase phAbove : phasesAbove) {
					for (Phase phBelow : phasesBelow) {
						if (phAbove == phBelow) {
							continue;
						}
						Set<Node> nodesInP1 = new HashSet<>(phAbove.getNodeSet());
						Set<Node> nodesInP2 = new HashSet<>(phBelow.getNodeSet());
						/*
						 * Extract only those nodes from nodesBelow that may co-exist with the
						 * barrier in phBelow.
						 */
						Set<Node> coExistBelow = new HashSet<>(nodesInP2);
						Set<BeginPhasePoint> belowBPP = phBelow.getBeginPoints();
						for (Node nBelow : nodesInP2) {
							CellSet readBelow = nBelow.getInfo().getSharedReads();
							CellSet writtenBelow = nBelow.getInfo().getSharedWrites();
							if (readBelow.isEmpty() && writtenBelow.isEmpty()) {
								continue;
							}
							if (belowBPP.stream().filter(bpp -> bpp.getNode() == barrier)
									.anyMatch(bpp -> bpp.getReachableNodes().contains(nBelow))) {
								coExistBelow.add(nBelow);
								continue;
							}
							if (belowBPP.stream()
									.filter(bpp -> bpp.getReachableNodes().contains(nBelow) && bpp.getNode().getInfo()
											.getNodePhaseInfo().getPhaseSet().contains(phAbove))
									.anyMatch(bpp -> CoExistenceChecker.canCoExistInPhase(bpp.getNode(), barrier,
											phAbove))) {
								coExistBelow.add(nBelow);
							}
						}
						for (Node nAbove : nodesInP1) {
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
							for (Node nBelow : coExistBelow) {
								CellSet readBelow = nBelow.getInfo().getSharedReads();
								CellSet writtenBelow = nBelow.getInfo().getSharedWrites();
								if (readBelow.isEmpty() && writtenBelow.isEmpty()) {
									continue;
								}
								nodePairs.add(new NodePair(nAbove, nBelow));
							}
						}
					}
				}
				System.out.println("Barr#" + id++ + ": " + nodePairs.size());
				allPairs.addAll(nodePairs);
			}
			System.out.println("Total: " + allPairs.size());
		}
	}
}
