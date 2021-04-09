/*
 *   Copyright (c) 2020 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 *   This file is a part of the project IMOP, licensed under the MIT license.
 *   See LICENSE.md for the full text of the license.
 *
 *   The above notice shall be included in all copies or substantial
 *   portions of this file.
 */
package imop.lib.analysis.mhp.incMHP;

import imop.ast.info.cfgNodeInfo.BarrierDirectiveInfo;
import imop.ast.info.cfgNodeInfo.ParallelConstructInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.solver.FieldSensitivity;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class corresponds to the information related to one parallel phase.
 *
 * @author Aman
 */
public class Phase extends AbstractPhase<BeginPhasePoint, EndPhasePoint> {
	private static Set<Phase> stalePhases = new HashSet<>();

	/**
	 * This constructor populates the following sets of this node:
	 * <ul>
	 * <li>beginPoints</li>
	 * <li>nodeSet</li>
	 * <li>endPoints</li>
	 * </ul>
	 * Note that this constructor does not make changes to the edges of PFG.
	 * However, it does ensure that updates to phase and inter-task information
	 * of nodes that get added to this phase are done.
	 *
	 * @param owner
	 * @param endPPSet
	 */
	public Phase(ParallelConstruct owner, Set<EndPhasePoint> endPPSet) {
		this(owner.getInfo());
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return;
		}
		for (EndPhasePoint endPP : endPPSet) {
			if (!(endPP.getNode() instanceof EndNode)) {
				assert (endPP.getNode().getInfo().isConnectedToProgram());
				this.beginPoints.add(BeginPhasePoint.getBeginPhasePoint(endPP, this));
			}
		}
		/*
		 * Old Code: (when the constructor of new phase was not followed by call
		 * to settleThisPhase on the newly constructed phase.)
		 */
		// for (BeginPhasePoint bpp : this.beginPoints) {
		// bpp.setInvalidFlag();
		// bpp.populateReachablesAndBarriersWithoutCleaning();
		// for (EndPhasePoint endPP : bpp.getNextBarriersCopy()) {
		// this.endPoints.add(endPP);
		// this.nodeSet.add(endPP.getNode());
		// endPP.getNode().getInfo().getNodePhaseInfo().addPhase(this);
		// }
		// for (Node reachable : bpp.getReachableNodesCopy()) {
		// this.nodeSet.add(reachable);
		// reachable.getInfo().getNodePhaseInfo().addPhase(this);
		// }
		// }
	}

	public Phase(ParallelConstructInfo owner) {
		super((ParallelConstruct) owner.getNode());
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return;
		}
		assert (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON);
		Collection<Phase> allPhaseList = (Collection<Phase>) owner.getAllPhaseList();
		this.succPhases = new HashSet<Phase>();
		this.predPhases = new HashSet<Phase>();
		allPhaseList.add(this);
	}

	/**
	 * Resets the list of stale phases. Should be called before parsing of the
	 * program.
	 */
	public static void resetStaticFields() {
		Phase.stalePhases.clear();
		AbstractPhase.stabilizationTime = 0;
		AbstractPhase.counter = 0;
	}

	/**
	 * Tests if node {@code n} (or its BeginNode, if {@code n} is a non-leaf node)
	 * is allowed to co-exist with other
	 * statements in phases that end at the {@code rootBarrier}. <br>
	 * A node can be shifted to a phase if any of these
	 * conditions are true:
	 * <ul>
	 * <li>A duplicate of the node is already present in the phase.</li>
	 * <li>Bernstein conditions are satisfied for accesses in the node with
	 * accesses in those statements which may get executed in the phase (and
	 * that hadn't shared a phase with this node in the input program).</li>
	 * <li>UPDATE: The conflicting node in the phases cannot exist in parallel
	 * with the {@code rootBarrier}.</li>
	 * <ul>
	 *
	 * @param n
	 *                    node for which code motion has to be tested.
	 * @param rootBarrier
	 *                    base barrier above which we intend to move the node
	 *                    {@code n}.
	 *
	 * @return true if node {@code n} can be moved to the phases that end at
	 *         {@code rootBarrier}.
	 */
	public static boolean isNodeAllowedInNewPhasesAbove(Node n, BarrierDirective rootBarrier) {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return false;
		}
		CellSet readsInSource = n.getInfo().getSharedReads();
		CellSet writesInSource = n.getInfo().getSharedWrites();
		CellSet nonFieldReadsInSource = null;
		CellSet nonFieldWritesInSource = null;
		if (readsInSource.isEmpty() && writesInSource.isEmpty()) {
			return true;
		}
		if (Program.fieldSensitive) {
			nonFieldReadsInSource = n.getInfo().getNonFieldSharedReads();
			nonFieldWritesInSource = n.getInfo().getNonFieldSharedWrites();
		}

		Set<AbstractPhase<?, ?>> inputPhasesOfN = n.getInfo().getNodePhaseInfo().getInputPhasesOfOldTimes();
		for (AbstractPhase<?, ?> ph : rootBarrier.getInfo().getNodePhaseInfo().getPhaseSet()) {
			for (Node otherNode : ph.getNodeSet()) {
				// Ignore those nodes that had shared at least one phase with {@code n} in the
				// input code.
				if (Misc.doIntersect(inputPhasesOfN,
						otherNode.getInfo().getNodePhaseInfo().getInputPhasesOfOldTimes())) {
					continue;
				}
				boolean mayConflict = false;
				if (Program.fieldSensitive) {
					/*
					 * In field-sensitive mode, we need to split checks in two
					 * parts:
					 * ..1. Check for conflicts in non-field accesses.
					 * ..2. Use field-sensitivity modules to check for conflicts
					 * .....among field accesses.
					 */
					CellSet nonFieldReadsInDestination = otherNode.getInfo().getNonFieldSharedReads();
					CellSet nonFieldWritesInDestination = otherNode.getInfo().getNonFieldSharedWrites();
					if (nonFieldWritesInSource.overlapsWith(nonFieldWritesInDestination)
							|| nonFieldWritesInSource.overlapsWith(nonFieldReadsInDestination)
							|| nonFieldReadsInSource.overlapsWith(nonFieldWritesInDestination)) {
						mayConflict = true;
					} else {
						if (FieldSensitivity.canConflictWithTwoThreads(n, otherNode)) {
							mayConflict = true;
						}
					}
				} else {
					/*
					 * In field-insensitive mode, nothing changes.
					 */
					CellSet readsInDestination = otherNode.getInfo().getSharedReads();
					CellSet writesInDestination = otherNode.getInfo().getSharedWrites();
					if (writesInSource.overlapsWith(writesInDestination)
							|| writesInSource.overlapsWith(readsInDestination)
							|| readsInSource.overlapsWith(writesInDestination)) {
						mayConflict = true;
					}
				}
				if (mayConflict) {
					/*
					 * Now, we should verify whether rootBarrier can co-exist
					 * with otherNode.
					 * If that's true, then the node n will co-exist with
					 * otherNode if we move it above the barrier -- may lead to
					 * a race-condition.
					 */
					if (CoExistenceChecker.canCoExistInPhase(otherNode, rootBarrier, ph)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Tests if node {@code n} (or its BeginNode, if {@code n} is a non-leaf node)
	 * is allowed to co-exist with other
	 * statements in phases that start after the {@code rootBarrier}. <br>
	 * A node can be shifted to a phase if any of
	 * these conditions are true:
	 * <ul>
	 * <li>A duplicate of the node is already present in the phase.</li>
	 * <li>Bernstein conditions are satisfied for accesses in the node, and
	 * those in other statements that may get executed in the phase (except for
	 * those that had shared some phase with the given node in the input
	 * code).</li>
	 * <li>UPDATE: The conflicting node in the phases cannot exist in parallel
	 * with the {@code rootBarrier}.</li>
	 * <ul>
	 *
	 * @param n
	 *                    node for which code motion has to be tested.
	 * @param rootBarrier
	 *                    base barrier below which we intend to move the node
	 *                    {@code n}.
	 *
	 * @return true if node {@code n} can be moved to the phases that end at
	 *         {@code rootBarrier}.
	 */
	public static boolean isNodeAllowedInNewPhasesBelow(Node n, BarrierDirective rootBarrier) {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			Misc.exitDueToError("Unexpected path.");
			return false;
		}
		CellSet readsInSource = n.getInfo().getSharedReads();
		CellSet writesInSource = n.getInfo().getSharedWrites();
		CellSet nonFieldReadsInSource = null;
		CellSet nonFieldWritesInSource = null;
		if (readsInSource.isEmpty() && writesInSource.isEmpty()) {
			return true;
		}
		if (Program.fieldSensitive) {
			nonFieldReadsInSource = n.getInfo().getNonFieldSharedReads();
			nonFieldWritesInSource = n.getInfo().getNonFieldSharedWrites();
		}

		Set<AbstractPhase<?, ?>> inputPhasesOfN = n.getInfo().getNodePhaseInfo().getInputPhasesOfOldTimes();
		for (AbstractPhase<?, ?> absPhAbove : rootBarrier.getInfo().getNodePhaseInfo().getPhaseSet()) {
			Phase phAbove = (Phase) absPhAbove;
			Phase phBelow = phAbove.getSuccPhase();

			Set<Node> coExistBelow = new HashSet<>();
			Set<BeginPhasePoint> belowBPP = phBelow.getBeginPoints();
			Set<Node> nodesBelow = phBelow.getNodeSet();
			for (Node nodeBelow : nodesBelow) {
				if (belowBPP.stream().filter(bpp -> bpp.getNode() == rootBarrier)
						.anyMatch(bpp -> bpp.getReachableNodes().contains(nodeBelow))) {
					// Refer to rule #3 in "Synchronization elimination --> Modeling
					// SVE-sensitivity."
					coExistBelow.add(nodeBelow);
					continue;
				}
				if (belowBPP.stream()
						.filter(bpp -> bpp.getReachableNodes().contains(nodeBelow)
								&& bpp.getNode().getInfo().getNodePhaseInfo().getPhaseSet().contains(phAbove))
						.anyMatch(bpp -> CoExistenceChecker.canCoExistInPhase(bpp.getNode(), rootBarrier, phAbove))) {
					// Refer to rule #3 in "Synchronization elimination --> Modeling
					// SVE-sensitivity."
					coExistBelow.add(nodeBelow);
					continue;
				}
			}

			for (Node nBelow : nodesBelow) {
				if (n == nBelow || !coExistBelow.contains(nBelow)) {
					continue;
				}

				/*
				 * If both n1 and n2 had shared a phase in common in the input,
				 * then it is safe (?) to ignore the pair.
				 * Note: This might not be "safe" if in the original-phases n1
				 * and n2 hadn't co-existed.
				 */
				Set<AbstractPhase<?, ?>> n2OldPhases = nBelow.getInfo().getNodePhaseInfo().getInputPhasesOfOldTimes();
				if (Misc.doIntersect(inputPhasesOfN, n2OldPhases)) {
					continue;
				}
				if (Program.fieldSensitive) {
					/*
					 * In field-sensitive mode, we need to split checks in two
					 * parts:
					 * ..1. Check for conflicts in non-field accesses.
					 * ..2. Use field-sensitivity modules to check for conflicts
					 * .....among field accesses.
					 */
					CellSet nonFieldReadsInDestination = nBelow.getInfo().getNonFieldSharedReads();
					CellSet nonFieldWritesInDestination = nBelow.getInfo().getNonFieldSharedWrites();
					if (nonFieldWritesInSource.overlapsWith(nonFieldWritesInDestination)
							|| nonFieldWritesInSource.overlapsWith(nonFieldReadsInDestination)
							|| nonFieldReadsInSource.overlapsWith(nonFieldWritesInDestination)) {
						return false;
					} else {
						if (FieldSensitivity.canConflictWithTwoThreads(n, nBelow)) {
							return false;
						}
					}
				} else {
					/*
					 * In field-insensitive mode, nothing changes.
					 */
					CellSet readsInDestination = nBelow.getInfo().getSharedReads();
					CellSet writesInDestination = nBelow.getInfo().getSharedWrites();
					if (writesInSource.overlapsWith(writesInDestination)
							|| writesInSource.overlapsWith(readsInDestination)
							|| readsInSource.overlapsWith(writesInDestination)) {
						return false;
					}
				}

				// Old code:
				// CellSet readsInDestination = nBelow.getInfo().getSharedReads();
				// CellSet writesInDestination = nBelow.getInfo().getSharedWrites();
				// if (Misc.doIntersect(readsInSource, writesInDestination)
				// || Misc.doIntersect(readsInDestination, writesInSource)
				// || Misc.doIntersect(writesInSource, writesInDestination)) {
				// return false;
				// }
			}

		}
		return true;
	}

	public Phase getSuccPhase() {
		return (Phase) Misc.getAnyElement(this.getSuccPhases());
	}

	public static void addStalePhases(Set<Phase> set) {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return;
		}
		Phase.stalePhases.addAll(set);
	}

	public void stabilizeThisPhase(Set<Phase> visitedPhases, Set<BeginPhasePoint> staleBeginPhasePoints,
			Set<BeginPhasePoint> stabilizedBPPsDuringStabilization, Phase oldImage) {
		/*
		 * Do not visit a phase recursively.
		 */
		if (visitedPhases.contains(this)) {
			return;
		} else {
			visitedPhases.add(this);
		}
		/*
		 * Step 1: Collect the set of BPPs for which the sets of this phase need
		 * to be recomputed.
		 */
		Set<BeginPhasePoint> bppToProcess = new HashSet<>();
		if (oldImage == null || (this.phaseId != oldImage.phaseId)) {
			// Process all BPPs
			bppToProcess = this.beginPoints; // Don't write to bppToProcess!!
		} else {
			for (BeginPhasePoint bpp : this.beginPoints) {
				if (staleBeginPhasePoints.contains(bpp)) {
					bppToProcess.add(bpp);
				}
			}
		}
		if (bppToProcess.isEmpty()) {
			/*
			 * Process the successor phase, and return.
			 */
			Phase succPhase = (this.succPhases == null || this.succPhases.isEmpty()) ? null
					: (Phase) Misc.getAnyElement(this.succPhases);
			if (succPhase != null) {
				succPhase.stabilizeThisPhase(visitedPhases, staleBeginPhasePoints, stabilizedBPPsDuringStabilization,
						succPhase);
			}
			return;
		}
		/*
		 * Stabilize the phase now.
		 */
		boolean isANewPhase = this.endPoints.isEmpty();
		boolean ePPChanged = false;
		for (BeginPhasePoint bpp : bppToProcess) {
			if (!stabilizedBPPsDuringStabilization.contains(bpp) || bpp.isNewBPP()) {
				// Stabilize the oldBPP now.
				bpp.recomputeSets();
				stabilizedBPPsDuringStabilization.add(bpp);
			}
			/*
			 * Now, inculcate additions of newly obtained information in phase.
			 */
			for (Node rN : bpp.getReachableNodes()) {
				this.addNode(rN); // Note: Do NOT directly use "this.nodeSet".
			}
			for (EndPhasePoint epp : bpp.getNextBarriers()) {
				this.addNode(epp.getNode());
				ePPChanged |= this.endPoints.add(epp); // No triggered effects yet.
			}
		}
		/*
		 * Remove extra nodes and extra EPPs from the node.
		 * Ensure that BeginNode of PC is not removed from the first phase.
		 */
		Set<Node> remNodeSet = new HashSet<>();
		for (Node currN : this.nodeSet) {
			if (currN == this.parConstruct.getInfo().getCFGInfo().getNestedCFG().getBegin()) {
				continue;
			}
			boolean foundInBPPReachOrNextBarr = false;
			outer: for (BeginPhasePoint bpp : this.beginPoints) {
				if (currN == bpp.getNode() && bpp.getNode() instanceof BeginNode
						&& bpp.getNode().getParent() instanceof ParallelConstruct) { // For BeginNode of PC.
					foundInBPPReachOrNextBarr = true;
					break outer;
				}
				if (bpp.getReachableNodes().contains(currN)) {
					foundInBPPReachOrNextBarr = true;
					break outer;
				}
				for (EndPhasePoint epp : bpp.getNextBarriers()) {
					if (epp.getNode() == currN) {
						foundInBPPReachOrNextBarr = true;
						break outer;
					}
				}
			}
			if (!foundInBPPReachOrNextBarr) {
				remNodeSet.add(currN);
			}
		}
		for (Node remN : remNodeSet) {
			this.removeNode(remN); // Note: Do NOT directly use "this.nodeSet"
		}
		Set<EndPhasePoint> remEPPSet = new HashSet<>();
		for (EndPhasePoint currEPP : this.endPoints) {
			boolean foundInBPPNextBarriers = false;
			for (BeginPhasePoint bpp : this.beginPoints) {
				if (bpp.getNextBarriers().contains(currEPP)) {
					foundInBPPNextBarriers = true;
					break;
				}
			}
			if (!foundInBPPNextBarriers) {
				remEPPSet.add(currEPP);
			}
		}
		ePPChanged |= this.endPoints.removeAll(remEPPSet);

		Phase succPhase = (this.succPhases == null || this.succPhases.isEmpty()) ? null
				: (Phase) Misc.getAnyElement(this.succPhases);
		Phase oldSuccessor = succPhase;
		if (ePPChanged || isANewPhase) {
			/*
			 * Obtain the new successor phase, based on update in
			 * this.endPoints.
			 */
			Phase newSucc = this.getNextPhase(visitedPhases);
			if (succPhase != null) {
				Phase.disconnectPhases(this, succPhase);
			}
			Phase.connectPhases(this, newSucc);
		}
		/*
		 * Process the successor phase, and return.
		 */
		succPhase = (this.succPhases == null || this.succPhases.isEmpty()) ? null
				: (Phase) Misc.getAnyElement(this.succPhases);
		if (succPhase != null) {
			succPhase.stabilizeThisPhase(visitedPhases, staleBeginPhasePoints, stabilizedBPPsDuringStabilization,
					oldSuccessor);
		}
		return;
	}

	@SuppressWarnings("unlikely-arg-type")
	private Phase getNextPhase(Set<Phase> visitedPhases) {
		Set<EndPhasePoint> thisEndPoints = new HashSet<>();
		for (EndPhasePoint thisEndPoint : this.endPoints) {
			if (thisEndPoint.getNode() instanceof EndNode) {
				continue;
			}
			thisEndPoints.add(thisEndPoint);
		}

		/*
		 * If entries in the last phase are subset of the entries in the
		 * beginPoints of some phase (may be itself), then we should stop.
		 */
		Phase superPhase = null;
		outer: for (Phase otherPhase : visitedPhases) {
			superPhase = otherPhase;
			for (EndPhasePoint endPhasePoint : thisEndPoints) {
				if (!otherPhase.getBeginPoints().contains(endPhasePoint)) {
					superPhase = null;
					continue outer;
				}
			}
			break;
		}
		if (superPhase != null) {
			return superPhase;
		} else {
			return new Phase(this.parConstruct, this.endPoints);
		}
	}

	/**
	 * Recomputes the set of nodes contained in this phase, and set of end-points
	 * for this phase.
	 *
	 * @return true, if the set of end-points may have changed as a result of this
	 *         call.
	 */
	private boolean recomputeSets() {
		Set<EndPhasePoint> newEndPoints = new HashSet<>();
		Set<Node> newNodeSet = new HashSet<>();
		for (BeginPhasePoint bpp : this.beginPoints) {
			newEndPoints.addAll(bpp.getNextBarriers());
			newNodeSet.addAll(bpp.getReachableNodes());
			if (bpp.getNode() instanceof BeginNode) {
				BeginNode bn = (BeginNode) bpp.getNode();
				if (bn.getParent() instanceof ParallelConstruct) {
					ParallelConstruct parCons = (ParallelConstruct) bn.getParent();
					if (parCons == this.parConstruct) {
						// Here, we add the BeginNode of parCons to the nodeSet.
						newNodeSet.add(bn);
					}
				}
			}
		}

		// Remove nodes.
		Set<Node> nodesToBeRemoved = new HashSet<>();
		for (Node oldNode : this.nodeSet) {
			if (!newNodeSet.contains(oldNode)) {
				nodesToBeRemoved.add(oldNode);
			}
		}
		for (Node nodeToBeRemoved : nodesToBeRemoved) {
			this.removeNode(nodeToBeRemoved);
		}

		// Add nodes.
		for (Node newNode : newNodeSet) {
			if (!this.nodeSet.contains(newNode)) {
				this.addNode(newNode);
			}
		}

		// Remove end-points.
		boolean changed = false;
		Set<EndPhasePoint> endPPsToBeRemoved = new HashSet<>();
		for (EndPhasePoint oldEndPP : this.endPoints) {
			if (!newEndPoints.contains(oldEndPP)) {
				endPPsToBeRemoved.add(oldEndPP);
			}
		}
		for (EndPhasePoint endPPToBeRemoved : endPPsToBeRemoved) {
			this.removeNode(endPPToBeRemoved.getNode());
			this.endPoints.remove(endPPToBeRemoved);
			if (!(endPPToBeRemoved.getNode() instanceof EndNode)) {
				changed = true;
			}

		}

		// Add end-points.
		for (EndPhasePoint newEndPP : newEndPoints) {
			if (!this.endPoints.contains(newEndPP)) {
				this.addNode(newEndPP.getNode());
				this.endPoints.add(newEndPP);
				if (!(newEndPP.getNode() instanceof EndNode)) {
					changed = true;
				}
			}
		}
		return changed;
	}

	/**
	 * To find whether this phase starts with begin-phase points that correspond to
	 * the {@code endPPset}, a set of
	 * end-phase points.
	 *
	 * @param endPPSet
	 *                 a set of end-phase points.
	 *
	 * @return true, if this phase starts with begin-phase points corresponding to
	 *         end-phase points set {@code
	 * endPPSet}.
	 */
	@SuppressWarnings("unlikely-arg-type")
	private boolean startsWithBeginPoints(Iterable<EndPhasePoint> endPPSet) {
		// EndPhasePoint toRemove = null;
		// for (EndPhasePoint epp : endPPSet) {
		// if (epp.getNode() instanceof EndNode) {
		// toRemove = epp;
		// }
		// }
		// if (toRemove != null) {
		// endPPSet = new HashSet<>(endPPSet);
		// endPPSet.remove(toRemove);
		// }
		// return this.beginPoints.equals(endPPSet);
		int matchedCounter = 0;
		for (EndPhasePoint endPP : endPPSet) {
			if (endPP.getNode() instanceof EndNode) {
				continue;
			}
			if (!beginPoints.contains(endPP)) {
				return false;
			} else {
				matchedCounter++;
			}
		}
		if (matchedCounter != beginPoints.size()) {
			return false;
		}
		return true;
	}

	public boolean percolateCodeUpwardsInLoop(IterationStatement itStmt) {
		boolean codeBroughtIn = false;
		Set<EndPhasePoint> traversedPoints = new HashSet<>();
		int safetyCount = 0;
		do {
			if (safetyCount++ > 1e5) {
				assert (false);
			}
			for (EndPhasePoint endPP : new HashSet<>(this.getEndPoints())) {
				if (!this.getEndPoints().contains(endPP)) {
					continue;
				}
				traversedPoints.add(endPP);
				Node syncNode = endPP.getNode();
				if (!(syncNode instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective barrDir = (BarrierDirective) syncNode;
				codeBroughtIn |= barrDir.getInfo().percolateCodeUpwards(itStmt);
			}
		} while (!Misc.setMinus(new HashSet<>(this.getEndPoints()), traversedPoints).isEmpty());

		return codeBroughtIn;
	}

	/**
	 * Adds {@code endPP} to {@link #endPoints} of this phase. This method also
	 * ensures that the PFG is made consistent
	 * upon this change.
	 *
	 * @param endPP
	 *              an end point to be added to {@link #endPoints}.
	 */
	@Deprecated
	public void deprecated_addEndPoint(EndPhasePoint endPP) {
		if (this.endPoints.contains(endPP)) {
			return;
		}
		this.addNode(endPP.getNode());
		this.endPoints.add(endPP);
		if (!(endPP.getNode() instanceof EndNode)) {
			this.deprecated_settleNextPhase();
		}
	}

	/**
	 * @return
	 *
	 * @deprecated
	 */
	@Deprecated
	public Set<Node> deprecated_getStableNodeSet() {
		BeginPhasePoint.deprecated_stabilizeStaleBeginPhasePoints();
		return this.getNodeSet();
	}

	/**
	 * Percolate code upwards into this phase from the phases that succeed this
	 * phase.
	 *
	 * @return true, if any code has been moved up.
	 */
	@Deprecated
	public boolean deprecated_old_PercolateCodeUpwards() {
		boolean codeBroughtIn = false;

		/*
		 * Step I: Mark statements that can be percolated up the barriers where
		 * this phase ends.
		 */
		for (PhasePoint endPP : this.endPoints) {
			Node endPointNode = endPP.getNode();
			if (endPointNode instanceof BarrierDirective) {
				BarrierDirectiveInfo barrierInfo = (((BarrierDirective) endPointNode).getInfo());
				barrierInfo.updateMovableList(getParConstruct());
				// we will call percolateCodeUpwards only after the movable code has been
				// marked for all the barriers.
			} else if (endPointNode instanceof ParallelConstruct) {
				if (((EndNode) endPointNode).getParent() == getParConstruct()) {
					continue;
				}
				// else, this is a case of nested parallelism, which we do not handle, as of
				// now.
				// TODO: Handle nested parallelism.
			} else {
				/*
				 * We assume that there are no implicit barriers in the input
				 * code,
				 * except at the end of the parallel constructs, to simplify
				 * coding.
				 */
				System.out.println(
						"An implicit barrier found at line #" + Misc.getLineNum(endPointNode) + ". Exiting...");
				System.exit(1);
			}
		} // ending iteration over all ending phase-points.

		/*
		 * Step II: Percolate the movable statements up into this phase.
		 */
		for (PhasePoint endPP : this.endPoints) {
			Node endPointNode = endPP.getNode();
			if (endPointNode instanceof BarrierDirective) {
				BarrierDirectiveInfo barrierInfo = (((BarrierDirective) endPointNode).getInfo());
				// TODO: CHANGE THIS BACK TO percolateCodeUpwards later.
				codeBroughtIn = codeBroughtIn || barrierInfo.percolateCodeUpwards();
			} else if (endPointNode instanceof ParallelConstruct) {
				if (endPointNode == getParConstruct()) {
					continue;
				}
				// else, this is a case of nested parallelism, which we do not handle, as of
				// now.
				// TODO: Handle nested parallelism.
			} else {
				/*
				 * We assume that there are no implicit barriers in the input
				 * code,
				 * except at the end of the parallel constructs, to simplify
				 * coding.
				 */
				System.out.println(
						"An implicit barrier found at line #" + Misc.getLineNum(endPointNode) + ". Exiting...");
				System.exit(1);
			}
		}

		// parConstruct.getInfo().root.getInfo().printNode();
		// System.exit(0);
		return codeBroughtIn;
	}

	@Deprecated
	public boolean deprecated_newPercolateCodeUpwards() {
		boolean codeBroughtIn = false;

		/*
		 * Step I: Mark statements that can be percolated up the barriers where
		 * this phase ends.
		 */
		for (PhasePoint endPP : this.endPoints) {
			Node endPointNode = endPP.getNode();
			if (endPointNode instanceof BarrierDirective) {
				BarrierDirectiveInfo barrierInfo = (((BarrierDirective) endPointNode).getInfo());
				barrierInfo.updateMovableList(getParConstruct());
				// we will call percolateCodeUpwards only after the movable code has been
				// marked for all the barriers.
			} else if (endPointNode instanceof EndNode) {
				if (((EndNode) endPointNode).getParent() == getParConstruct()) {
					continue;
				}
				// else, this is a case of nested parallelism, which we do not handle, as of
				// now.
				// TODO: Handle nested parallelism.
			} else {
				/*
				 * We assume that there are no implicit barriers in the input
				 * code,
				 * except at the end of the parallel constructs, to simplify
				 * coding.
				 */
				System.out.println(
						"An implicit barrier found at line #" + Misc.getLineNum(endPointNode) + ". Exiting...");
				System.exit(1);
			}
		} // ending iteration over all ending phase-points.

		/*
		 * Step II: Percolate the movable statements up into this phase.
		 */
		for (PhasePoint endPP : this.endPoints) {
			Node endPointNode = endPP.getNode();
			if (endPointNode instanceof BarrierDirective) {
				BarrierDirectiveInfo barrierInfo = (((BarrierDirective) endPointNode).getInfo());
				// TODO: CHANGE THIS BACK TO percolateCodeUpwards later.
				codeBroughtIn = codeBroughtIn || barrierInfo.percolateCodeUpwards();
			} else if (endPointNode instanceof ParallelConstruct) {
				if (endPointNode == getParConstruct()) {
					continue;
				}
				// else, this is a case of nested parallelism, which we do not handle, as of
				// now.
				// TODO: Handle nested parallelism.
			} else {
				/*
				 * We assume that there are no implicit barriers in the input
				 * code,
				 * except at the end of the parallel constructs, to simplify
				 * coding.
				 */
				System.out.println(
						"An implicit barrier found at line #" + Misc.getLineNum(endPointNode) + ". Exiting...");
				System.exit(1);
			}
		}

		// parConstruct.getInfo().root.getInfo().printNode();
		// System.exit(0);
		return codeBroughtIn;
	}

	/**
	 * Removes {@code endPP} from {@link #endPoints} of this phase. This method also
	 * ensures that the PFG is made
	 * consistent upon this change.
	 *
	 * @param endPP
	 *              an end point to be removed from {@link #endPoints}.
	 */
	@Deprecated
	public void deprecated_removeEndPoint(EndPhasePoint endPP) {
		if (!this.endPoints.contains(endPP)) {
			return;
		}
		this.removeNode(endPP.getNode());
		this.endPoints.remove(endPP);
		if (!(endPP.getNode() instanceof EndNode)) {
			this.deprecated_settleNextPhase();
		}
	}

	/**
	 * This method belongs to the "lazy update" module for stabilizing phase
	 * information only when required since the
	 * last change to the program. It will ensure that the phase flow graph is
	 * correct as per the current status of the
	 * program.
	 */
	@Deprecated
	public static void deprecated_stabilizePhases() {
		if (stalePhases.isEmpty()) {
			return;
		}
		long timer = System.nanoTime();
		// System.err.println("Starting stabilization of PHASES.");
		Set<ParallelConstruct> affectedParCons = new HashSet<>();
		Set<Phase> stalePhaseCopy = new HashSet<>(stalePhases);
		stalePhases.clear();
		;
		for (Phase ph : stalePhaseCopy) {
			// Ignore those that are disconnected.
			boolean found = false;
			for (BeginPhasePoint bpp : ph.beginPoints) {
				if (bpp.getNode().getInfo().isConnectedToProgram()) {
					found = true;
					break;
				}
			}
			if (!found) {
				continue;
			}
			// Stabilize the phase information, starting with ph.
			boolean updated = ph.deprecated_settleThisPhase();
			if (updated) {
				affectedParCons.add(ph.getParConstruct());
			}
		}
		Phase.removeUnreachablePhases();
		long diff = System.nanoTime() - timer;
		stabilizationTime += diff;
		// System.err.println("\tStabilization of phase completed in " + diff / (1.0 *
		// 1e9) + "s.");
		// for (ParallelConstruct parCons : affectedParCons) {
		// Phase.removeUnreachablePhases(parCons);
		// }

	}

	/**
	 * This method ensures that the current set of end-phase points for this phase
	 * gets reflected in the next phase.
	 * This method also makes appropriate changes to the PFG, when needed.
	 *
	 * @return true, if cleaning of the unreachable phases is required.
	 */
	@Deprecated
	private boolean deprecated_settleNextPhase() {
		if (this.endPoints.size() == 1 && Misc.getSingleton(this.endPoints).getNode() instanceof EndNode) {
			return false;
		}
		if (getSuccPhase() != null && getSuccPhase().startsWithBeginPoints(this.endPoints)) {
			return false;
		}
		List<Phase> allPhaseList = (List<Phase>) this.parConstruct.getInfo().getAllPhaseList();
		boolean cleanPFG = false;
		boolean found = false;
		for (Phase ph : allPhaseList) {
			if (ph.startsWithBeginPoints(this.endPoints)) {
				found = true;
				if (getSuccPhase() != null) {
					Phase oldSuccPhase = getSuccPhase();
					Phase.disconnectPhases(this, getSuccPhase());
					if (oldSuccPhase.predPhases == null || oldSuccPhase.predPhases.isEmpty()) {
						cleanPFG = true;
					}
					Phase.connectPhases(this, ph);
					break;
				} else {
					Phase.connectPhases(this, ph);
					return false;
				}
			}
		}
		if (cleanPFG) {
			return true;
		} else if (found) {
			return false;
		}

		// If we reach this point, then there exists no phase which has appropriate
		// begin-phase points to be made as a successor of this phase.
		if (getSuccPhase() != null) {
			Phase oldSuccPhase = getSuccPhase();
			Phase.disconnectPhases(this, getSuccPhase());
			if (oldSuccPhase.predPhases == null || oldSuccPhase.predPhases.isEmpty()) {
				cleanPFG = true;
			}
		}
		Phase newSuccPhase = new Phase(this.parConstruct, this.endPoints);
		cleanPFG |= newSuccPhase.deprecated_settleThisPhase();
		// cleanPFG |= newSuccPhase.settleNextPhase();
		Phase.connectPhases(this, newSuccPhase);
		return cleanPFG;

		// Old code:
		// if (succPhase == null) {
		// Phase newSuccPhase = new Phase(this.parConstruct, this.endPoints);
		// newSuccPhase.settleNextPhase();
		// Phase.connectPhases(this, newSuccPhase);
		// } else if (succPhase.getPredPhases().size() != 1) {
		// Phase.disconnectPhases(this, succPhase);
		// Phase.removeUnreachablePhases(this.parConstruct);
		// Phase newSuccPhase = new Phase(this.parConstruct, this.endPoints);
		// newSuccPhase.settleNextPhase();
		// Phase.connectPhases(this, newSuccPhase);
		// } else {
		// for (BeginPhasePoint bpp : succPhase.beginPoints) {
		// bpp.getPhaseSet().remove(succPhase);
		// }
		// succPhase.beginPoints = new HashSet<>();
		// for (EndPhasePoint endPP : this.endPoints) {
		// BeginPhasePoint bpp = BeginPhasePoint.getBeginPhasePoint(endPP);
		// bpp.setInvalidFlag();
		// bpp.populateReachablesAndBarriers();
		// succPhase.beginPoints.add(bpp);
		// }
		// succPhase.settleThisPhase();
		// }

	}

	/**
	 * This method ensures that the current set of end-points and node-set are
	 * consistent with the ones that are
	 * attainable from the current set of begin-points of this node. This method
	 * performs minimal updates to existing
	 * sets, while adjusting them. Since it calls update-enabled methods for
	 * adding/removing endPhasePoints, and nodes,
	 * the future path in PFG, and phase/inter-task information of affected nodes,
	 * get updated automatically. The
	 * changes are propagated further in the PFG, as and when needed.
	 */
	@Deprecated
	public boolean deprecated_settleThisPhase() {
		boolean changed = this.recomputeSets();
		if (changed) {
			return this.deprecated_settleNextPhase();
		}
		return false;
	}

	/**
	 * Removes all those phases that are not reachable from the start phase of
	 * {@code parConstruct} anymore.
	 *
	 * @param parCons
	 *                parallelConstruct for which the PFG needs to be updated.
	 *
	 * @deprecated
	 */
	@Deprecated
	public static void removeUnreachablePhases(ParallelConstruct parCons) {
		List<Phase> allPhaseList = (List<Phase>) parCons.getInfo().getAllPhaseList();
		if (allPhaseList.isEmpty()) {
			return;
		}
		Phase startPhase = allPhaseList.get(0);
		boolean hasBeginNode = false;
		for (BeginPhasePoint bpp : startPhase.getBeginPoints()) {
			if (bpp.getNode() instanceof BeginNode) {
				hasBeginNode = true;
				break;
			}
		}
		assert (hasBeginNode);

		Set<Phase> reachablePhases = new HashSet<>();
		reachablePhases.add(startPhase);
		Phase tempPhase = startPhase;
		while (tempPhase.getSuccPhase() != null) {
			tempPhase = tempPhase.getSuccPhase();
			if (reachablePhases.contains(tempPhase)) {
				break;
			}
			reachablePhases.add(tempPhase);
		}

		Set<Phase> phasesToBeRemoved = Misc.setMinus(new HashSet<>(allPhaseList), reachablePhases);
		for (Phase phaseToBeRemoved : phasesToBeRemoved) {
			parCons.getInfo().removePhase(phaseToBeRemoved);
		}
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public static void removeUnreachablePhases() {
		for (ParallelConstruct parCons : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			Phase.removeUnreachablePhases(parCons);
		}
	}

}
