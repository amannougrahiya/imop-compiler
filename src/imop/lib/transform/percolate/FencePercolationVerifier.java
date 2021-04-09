/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.percolate;

import imop.ast.node.external.*;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.CellSet;
import imop.lib.util.CollectorVisitor;
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

public class FencePercolationVerifier {

	/**
	 * Given a phase {@code ph}, this method performs a very basic,
	 * <i>conservative</i>, check for whether it is okay to percolate code
	 * across
	 * fences in {@code ph}.
	 * 
	 * @param ph
	 *           a phase to be tested.
	 * @return
	 *         true if we do not need to perform any validation checks while
	 *         moving code around fences in the given phase {@code ph}.
	 */
	public static boolean isPhaseSafeForFencePercolations(Phase ph) {
		/*
		 * ** Checks to be performed: **
		 * Step A: Collect all pairs of statements (S1, S2) in the phase, such
		 * that S1 and S2 may write to some shared variable(s), and there exists
		 * at least one intra-task path from S1 to S2, such that S2 and S1 can
		 * be interchanged on that path.
		 * Q: Do we need to consider the case when S1 == S2?
		 * A: No. Since we are not planning to split any leaf nodes, we will
		 * never be reordering the writes of variables in S1 (=S2).
		 * ***
		 * Step B: For each pair of shared variables (v1, v2), written by the
		 * statements (S1, S2) in Step A above, check if there exists any pair
		 * of statements (S3, S4) in the phase, such that (S3, S4) reads from
		 * (v2, v1), and there exists a path from S3 to S4. If so, return false.
		 * ***
		 * Step C: Collect all pairs of statements (S1, S2) in the phase, such
		 * that S1 may read from a shared variable, and S2 may write to a shared
		 * variable, and there exits at least one intra-task path from S1 to S2,
		 * such that S2 and S1 can be interchanged on that path.
		 * Q: Do we need to consider the case when S1 == S2?
		 * A: No. Since we are not planning to split any leaf nodes, we will
		 * never be reordering the read and write of variables in S1 (=S2).
		 * ***
		 * Step D: For each pair of shared variables (v1, v2), read and written
		 * by the statements (S1, S2) in Step C above, check if there exits any
		 * pair of statements (S3, S4) in the phase, such that (S3, S4) reads
		 * and writes the variables (v2, v1), and there exists a path from S3 to
		 * S4. If so, return false.
		 * .
		 * .
		 * An inefficient code (O(n^5), for n statements) follows.
		 * Note that this solution can be simplified by using some more maps,
		 * and lesser traversals.
		 */

		Set<Node> cfgNodesInPh = new HashSet<>();
		Set<Node> sharedReadNodes = new HashSet<>();
		Set<Node> sharedWriteNodes = new HashSet<>();
		for (Object cfgObj : ph.getNodeSet().stream().filter(s -> Misc.isCFGLeafNode(s)).toArray()) {
			Node leafNode = (Node) cfgObj;
			cfgNodesInPh.add(leafNode);
			if (!leafNode.getInfo().getSharedReads().isEmpty()) {
				sharedReadNodes.add(leafNode);
			}
			if (!leafNode.getInfo().getSharedWrites().isEmpty()) {
				sharedWriteNodes.add(leafNode);
			}
		}

		System.out.println("\t Performing percolation safety checks on total of " + sharedReadNodes.size()
				+ " read(s) and " + sharedWriteNodes.size() + " write(s).");
		// First check.
		for (Node read1 : sharedReadNodes) {
			for (Node write1 : sharedWriteNodes) {
				if (read1 == write1) {
					continue;
				}
				// Continue if there does not exist a path from read1 to write1.
				if (!FencePercolationVerifier.areSwappableOnPath(read1, write1)) {
					continue;
				}

				CellSet readSet = read1.getInfo().getSharedReads();
				CellSet writeSet = write1.getInfo().getSharedWrites();
				// Check if any criss-crossed path exists in the phase.
				for (Node read2 : sharedReadNodes) {
					if (read2 == read1 || read2 == write1) {
						continue;
					}
					for (Node write2 : sharedWriteNodes) {
						if (write2 == read1 || write2 == write1 || write2 == read2) {
							continue;
						}
						if (!FencePercolationVerifier.anIntraPhasePathExists(read2, write2)) {
							continue;
						}
						CellSet secondReadSet = read2.getInfo().getSharedReads();
						CellSet secondWriteSet = write2.getInfo().getSharedWrites();
						if (readSet.overlapsWith(secondWriteSet) && writeSet.overlapsWith(secondReadSet)) {
							System.out.println(
									"Encountered following possible fence swap issue of form (RY,WX) --> (RX, WY):");
							System.out.println("(" + read1 + ", " + write1 + ") with (" + read2 + ", " + write2 + ")");
							return false;
						}
					}
				}
			}
		}

		// Second check.
		for (Node write1 : sharedWriteNodes) {
			for (Node write2 : sharedWriteNodes) {
				// Continue if there does not exist a path from write1 to write2.
				if (!FencePercolationVerifier.areSwappableOnPath(write1, write2)) {
					continue;
				}

				CellSet write1Set = write1.getInfo().getSharedWrites();
				CellSet write2Set = write2.getInfo().getSharedWrites();
				// Check if any criss-crossed path exists in the phase.
				for (Node read1 : sharedReadNodes) {
					for (Node read2 : sharedReadNodes) {
						if (!FencePercolationVerifier.anIntraPhasePathExists(read2, read1)) {
							continue;
						}
						CellSet read1Set = read1.getInfo().getSharedReads();
						CellSet read2Set = read2.getInfo().getSharedReads();
						if (write1Set.overlapsWith(read1Set) && write2Set.overlapsWith(read2Set)) {
							System.out.println(
									"Encountered following possible fence swap issue of the form (WX, WY) --> (RY, RX)");
							System.out.println("(" + write1 + ", " + write2 + ") with (" + read2 + ", " + read1 + ")");
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * Checks whether there exists any intra-task intra-phase path between
	 * {@code n1} and {@code n2}.
	 * 
	 * @param n1
	 *           a CFG node.
	 * @param n2
	 *           a CFG node.
	 * @return
	 *         true, if an intra-task intra-phase path may exits between n1 and
	 *         n2.
	 */
	public static boolean anIntraPhasePathExists(Node n1, Node n2) {
		NodeWithStack startPoint = new NodeWithStack(n1, new CallStack());
		Set<NodeWithStack> endPoints = new HashSet<>();
		CollectorVisitor.collectNodeSetInGenericGraph(startPoint, endPoints, nS -> {
			if (nS.getNode() instanceof BarrierDirective) {
				return true;
			} else if (nS.getNode() == n2) {
				return true;
			} else {
				return false;
			}
		}, nS -> nS.getNode().getInfo().getCFGInfo().getInterProceduralLeafSuccessors(nS.getCallStack()));
		if (endPoints.stream().anyMatch(nS -> nS.getNode() == n2)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks whether there exists any intra-task intra-phase path between
	 * {@code n1} and {@code n2}, along which {@code n1} and {@code n2} can be
	 * swapped.
	 * 
	 * @param n1
	 *           a CFG node.
	 * @param n2
	 *           a CFG node.
	 * @return
	 *         true, if an intra-task intra-phase path may exits between n1 and
	 *         n2, such that {@code n1} and {@code n2} can be swapped along this
	 *         path.
	 */
	public static boolean areSwappableOnPath(Node n1, Node n2) {
		if (Misc.haveDataDependences(n1, n2)) {
			return false;
		}
		/*
		 * TODO: Check for dependences on the path itself!
		 * For now, we use a conservative approximation.
		 */
		return anIntraPhasePathExists(n1, n2);
	}

}
