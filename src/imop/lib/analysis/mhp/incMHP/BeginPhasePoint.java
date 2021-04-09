/*
 *   Copyright (c) 2020 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 *   This file is a part of the project IMOP, licensed under the MIT license.
 *   See LICENSE.md for the full text of the license.
 *
 *   The above notice shall be included in all copies or substantial
 *   portions of this file.
 */
package imop.lib.analysis.mhp.incMHP;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.CollectorVisitor;
import imop.lib.util.Misc;
import imop.parser.Program;
import imop.parser.Program.UpdateCategory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BeginPhasePoint extends PhasePoint {
	// Note that the node can be a BeginNode of a ParallelConstruct, or it can be a
	// BarrierDirective.
	private Set<Node> reachableNodeSet = null;
	private Set<EndPhasePoint> nextBarrierSet = null;
	private Set<Phase> phaseSet = new HashSet<>(); // set of phases of which this object is a begin point.
	public static boolean stabilizationInProgress = false;
	private static Set<BeginPhasePoint> staleBeginPhasePoints = new HashSet<>(); // Used only during INC modes.
	public static long stabilizationTime = 0;
	// private boolean setsInvalid = true;

	/**
	 * Stabilizes the phase information, starting with all those begin-phase points
	 * that have been marked as stale.
	 */
	public static void stabilizeStaleBeginPhasePoints() {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return;
		}
		/*
		 * Check whether stabilization is needed.
		 */
		if (BeginPhasePoint.staleBeginPhasePoints.isEmpty()) {
			return;
		}
		/*
		 * Ensure that this method doesn't call itself recursively.
		 */
		if (BeginPhasePoint.stabilizationInProgress) {
			return;
		} else {
			/*
			 * Initialize the stabilization process.
			 */
			BeginPhasePoint.stabilizationInProgress = true;
		}
		/********* Start the stabilization process ***********/
		long timer = System.nanoTime();
		AutomatedUpdater.reinitMHPCounter++;
		Set<BeginPhasePoint> stabilizedBPPs = new HashSet<>();
		/*
		 * Step 1: Collect all affected parallel constructs (PCs).
		 * Ignore all those PCs that are disconnected.
		 */
		Set<ParallelConstruct> affectedParCons = new HashSet<>();
		for (BeginPhasePoint staleBPP : staleBeginPhasePoints) {
			for (Phase ph : staleBPP.phaseSet) {
				ParallelConstruct pc = ph.getParConstruct();
				if (affectedParCons.contains(pc)) {
					continue;
				}
				if (pc.getInfo().isConnectedToProgram()) {
					if (pc.getInfo().getFirstPhase() != null) {
						affectedParCons.add(pc);
					}
				}
			}
		}

		// Stabilize MHP of each affected PC.
		for (ParallelConstruct pc : affectedParCons) {
			/*
			 * Step 2: Cache the list of connected phases of pc.
			 */
			Set<Phase> staleConnectedSet = new HashSet<Phase>((Collection<Phase>) pc.getInfo().getConnectedPhases());
			/*
			 * Stabilize each phase, starting with the first phase of PC.
			 */
			Phase firstPhase = (Phase) pc.getInfo().getFirstPhase();
			assert (firstPhase != null) : "Why could we not find MHP info for : " + pc + " \n that is present as "
					+ pc.getInfo().getDebugString() + "\n?";
			firstPhase.stabilizeThisPhase(new HashSet<>(), BeginPhasePoint.staleBeginPhasePoints, stabilizedBPPs,
					firstPhase);

			/*
			 * Obtain the list of new connected phases of pc.
			 */
			Set<Phase> freshConnectedSet = new HashSet<Phase>((Collection<Phase>) pc.getInfo().getConnectedPhases());

			/*
			 * Remove the effects of all the stale phases from
			 * staleConnectedSet.
			 */
			for (Phase oldPh : staleConnectedSet) {
				if (!freshConnectedSet.contains(oldPh)) {
					oldPh.flushMHPData();
				}
			}
		}

		/********* Wrap-up the stabilization process ***********/
		/*
		 * As the last step, clear the set of stale BPPs, and processed BPPs.
		 * Reset the stabilizationInProgress flag.
		 */
		// BeginPhasePoint.staleBeginPhasePoints.removeAll(stabilizedBPPs);
		BeginPhasePoint.staleBeginPhasePoints.clear();
		BeginPhasePoint.stabilizationInProgress = false;
		BeginPhasePoint.stabilizationTime += (System.nanoTime() - timer);
		// DumpSnapshot.dumpPhases("stable" + Program.mhpUpdateCategory +
		// AutomatedUpdater.reinitMHPCounter);
	}

	private BeginPhasePoint(Node sink, CallStack callStack) {
		super(sink, callStack);
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
		}
		assert (sink instanceof BarrierDirective || sink instanceof BeginNode || sink instanceof EndNode);
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			AbstractPhasePointable.allBeginPhasePoints.add(this);
		} else {
			assert (false) : "Unexpected path encountered";
		}
	}

	private BeginPhasePoint(NodeWithStack nodeWithStack) {
		super(nodeWithStack.getNode(), nodeWithStack.getCallStack());
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
		}
		assert (nodeWithStack.getNode() instanceof BarrierDirective || nodeWithStack.getNode() instanceof BeginNode
				|| nodeWithStack.getNode() instanceof EndNode);
		AbstractPhasePointable.allBeginPhasePoints.add(this);
	}

	public static BeginPhasePoint getBeginPhasePoint(Node sink, CallStack callStack, Phase ph) {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return null;
		}
		BeginPhasePoint returnBPP = null;
		for (AbstractPhasePointable bppAbs : AbstractPhasePointable.allBeginPhasePoints) {
			BeginPhasePoint bpp = (BeginPhasePoint) bppAbs;
			if (bpp.callStack == callStack && bpp.node == sink) {
				returnBPP = bpp;
				break;
			}
		}
		if (returnBPP == null) {
			returnBPP = new BeginPhasePoint(sink, callStack);
		}
		returnBPP.getPhaseSet().add(ph);
		return returnBPP;
	}

	public static BeginPhasePoint getBeginPhasePoint(NodeWithStack nodeWithStack) {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return null;
		}
		BeginPhasePoint returnBPP = null;
		for (AbstractPhasePointable bppAbs : AbstractPhasePointable.allBeginPhasePoints) {
			BeginPhasePoint bpp = (BeginPhasePoint) bppAbs;
			if (bpp.equals(nodeWithStack)) {
				returnBPP = bpp;
				break;
			}
		}
		if (returnBPP == null) {
			returnBPP = new BeginPhasePoint(nodeWithStack);
		}
		return returnBPP;
	}

	public static BeginPhasePoint getBeginPhasePoint(NodeWithStack barrierNodeWithStack, Phase ph) {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return null;
		}
		BeginPhasePoint returnBPP = null;
		for (AbstractPhasePointable bppAbs : AbstractPhasePointable.allBeginPhasePoints) {
			BeginPhasePoint bpp = (BeginPhasePoint) bppAbs;
			if (bpp.equals(barrierNodeWithStack)) {
				returnBPP = bpp;
				break;
			}
		}
		if (returnBPP == null) {
			returnBPP = new BeginPhasePoint(barrierNodeWithStack);
		}
		returnBPP.phaseSet.add(ph);
		return returnBPP;
	}

	public void flushData() {
		this.reachableNodeSet = null;
		this.nextBarrierSet = null;
		this.phaseSet.clear();
	}

	@Override
	public void flushMHPData(AbstractPhase<?, ?> phase) {
		this.phaseSet.remove(phase);
	}

	@Override
	public <T extends AbstractPhase<?, ?>> void removePhase(T phaseToBeRemoved) {
		this.getPhaseSet().remove(phaseToBeRemoved);
	}

	/**
	 * Obtain set of all those BeginPhasePoints from which {@code node} may be
	 * reachable, on barrier-free path
	 * (inclusive).
	 * <br>
	 * Note that this method would also trigger resetting of {@link #setsInvalid} of
	 * all the {@link BeginPhasePoint}
	 * nodes, after refreshing the corresponding lists of reachable nodes and
	 * barriers.
	 * <br>
	 * Furthermore, if {@code node} is same as the node field of a BeginPhasePoint,
	 * that BeginPhasePoint is also
	 * considered as a valid entry to be returned.
	 *
	 * @param node
	 *             node for which we obtain all those BeginPhasePoints from which
	 *             the node may be reachable.
	 *
	 * @return set of all those BeginPhasePoints from which {@code node} may be
	 *         reachable on barrier-free path of same
	 *         parallelism-nesting level.
	 */
	public static Set<BeginPhasePoint> getRelatedBPPs(Node node) {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return null;
		}
		Set<BeginPhasePoint> bppSet = new HashSet<>();
		if (node instanceof BarrierDirective
				|| (node instanceof BeginNode && node.getParent() instanceof ParallelConstruct)) {
			for (AbstractPhasePointable bppAbs : AbstractPhasePointable.allBeginPhasePoints) {
				BeginPhasePoint bpp = (BeginPhasePoint) bppAbs;
				if (BeginPhasePoint.staleBeginPhasePoints.contains(bpp)) {
					continue; // Ignore this BPP; it's already marked as stale.
				}
				if (bpp.getNode() == node) {
					bppSet.add(bpp);
				}
			}
		} else if (node instanceof EndNode && node.getParent() instanceof ParallelConstruct) {
			/*
			 * New code: This code now handles the scenario
			 * where the given node is the EndNode of a ParallelConstruct.
			 * This could be the case only when the node after a PC is going to
			 * be removed. In such a case, we don't need to update any phase
			 * information.
			 * The loop does not take care of this situation because
			 * allBeginPhasePoints does not save EndNode of a ParallelConstruct
			 * as a barrier, even though it is.
			 */
			return bppSet; // Return an empty set.
		} else {
			for (AbstractPhasePointable bppAbs : AbstractPhasePointable.allBeginPhasePoints) {
				BeginPhasePoint bpp = (BeginPhasePoint) bppAbs;
				if (BeginPhasePoint.staleBeginPhasePoints.contains(bpp)) {
					continue; // Ignore this BPP; it's already marked as stale.
				}
				assert (bpp.getNode() != node);
				if (bpp.getReachableNodes().contains(node)) {
					bppSet.add(bpp);
				}
			}
		}
		return bppSet;
	}

	public static Set<BeginPhasePoint> getRelatedBPPsNoStaleRemoval(Node node) {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return null;
		}
		Set<BeginPhasePoint> bppSet = new HashSet<>();
		assert (!(node instanceof BarrierDirective)
				&& (!(node instanceof BeginNode && node.getParent() instanceof ParallelConstruct)));
		if (node instanceof EndNode && node.getParent() instanceof ParallelConstruct) {
			/*
			 * New code: This code now handles the scenario
			 * where the given node is the EndNode of a ParallelConstruct.
			 * This could be the case only when the node after a PC is going to
			 * be removed. In such a case, we don't need to update any phase
			 * information.
			 * The loop does not take care of this situation because
			 * allBeginPhasePoints does not save EndNode of a ParallelConstruct
			 * as a barrier, even though it is.
			 */
			return bppSet; // Return an empty set.
		} else {
			for (AbstractPhase absPh : node.getInfo().getNodePhaseInfo().getStalePhaseSet()) {
				Phase ph = (Phase) absPh;
				for (BeginPhasePoint bpp : ph.getStaleBeginPoints()) {
					if (bpp.getInternalReachables().contains(node)) {
						bppSet.add(bpp);
					}
				}
			}
		}
		return bppSet;
	}

	public static Set<BeginPhasePoint> getStaleBeginPhasePoints() {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return null;
		}
		return staleBeginPhasePoints;
	}

	public static void addStaleBeginPhasePoints(Set<BeginPhasePoint> staleBPPs) {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			Thread.dumpStack();
			assert (false) : "Unexpected path.";
			return;
		}
		BeginPhasePoint.staleBeginPhasePoints.addAll(staleBPPs);
	}

	/**
	 * Stabilizes the phase information, starting with all those begin-phase points
	 * that have been marked as stale.
	 *
	 * @deprecated
	 */
	@Deprecated
	public static void deprecated_stabilizeStaleBeginPhasePoints() {
		assert (false);
		if (BeginPhasePoint.getStaleBeginPhasePoints().isEmpty()) {
			return;
		}
		if (BeginPhasePoint.stabilizationInProgress) {
			return;
		}

		BeginPhasePoint.stabilizationInProgress = true;
		long timer = System.nanoTime();
		// System.err.println("Starting stabilization of BPPs.");
		Set<BeginPhasePoint> changedBPPs = new HashSet<>();
		Set<BeginPhasePoint> toRemove = new HashSet<>();
		for (BeginPhasePoint bpp : BeginPhasePoint.getStaleBeginPhasePoints()) {
			if (!bpp.getNode().getInfo().isConnectedToProgram()) {
				continue;
			}
			toRemove.add(bpp);
			boolean changed = bpp.recomputeSets();
			if (changed) {
				changedBPPs.add(bpp);
			}
		}
		BeginPhasePoint.stabilizationInProgress = false;
		BeginPhasePoint.getStaleBeginPhasePoints().removeAll(toRemove);
		if (changedBPPs.isEmpty()) {
			long diff = System.nanoTime() - timer;
			stabilizationTime += diff;
			return;
		}
		for (BeginPhasePoint bpp : changedBPPs) {
			Phase.addStalePhases(bpp.getPhaseSet());
		}
		BeginPhasePoint.getStaleBeginPhasePoints().clear();
		long diff = System.nanoTime() - timer;
		stabilizationTime += diff;
		// System.err.println("\tStabilization of BPPs completed in " + diff / (1.0 *
		// 1e9) + "s.");
	}

	/**
	 * Adds {@code nodeToBeAdded} to the reachability set of this phase point.
	 * <br>
	 * Note that this method does not attempt addition of other nodes to the
	 * reachability set of this phase point.
	 *
	 * @param nodeToBeAdded
	 *                      node that has to be added to the reachability set of
	 *                      this phase point.
	 */
	public void addNode(Node nodeToBeAdded) {
		if (this.reachableNodeSet == null) {
			this.recomputeSets();
		}
		if (this.reachableNodeSet.contains(nodeToBeAdded)) {
			return;
		}
		this.reachableNodeSet.add(nodeToBeAdded);
		for (Phase ph : this.getPhaseSet()) {
			ph.addNode(nodeToBeAdded);
		}
	}

	public boolean isNewBPP() {
		return this.reachableNodeSet == null;
	}

	public Set<EndPhasePoint> getNextBarriers() {
		if (Program.mhpUpdateCategory == UpdateCategory.EGINV || Program.mhpUpdateCategory == UpdateCategory.EGUPD) {
			return this.getInternalNextBarriers();
		} else if (Program.mhpUpdateCategory == UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.getInternalNextBarriers();
		} else { // Mode: LZINC
			if (!BeginPhasePoint.getStaleBeginPhasePoints().contains(this)) {
				// This BPP has correct set of reachable nodes.
				return this.getInternalNextBarriers();
			}
			// Otherwise, this BPP is stale.
			BeginPhasePoint.stabilizeStaleBeginPhasePoints();
			return this.getInternalNextBarriers();
		}
	}

	public Set<Phase> getPhaseSet() {
		if (Program.mhpUpdateCategory == UpdateCategory.EGINV || Program.mhpUpdateCategory == UpdateCategory.EGUPD) {
			return this.phaseSet;
		} else if (Program.mhpUpdateCategory == UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.phaseSet;
		} else { // Mode: LZINC
			// Otherwise, this BPP is stale.
			BeginPhasePoint.stabilizeStaleBeginPhasePoints();
			return this.phaseSet;
		}
	}

	@Override
	public Set<Node> getReachableNodes() {
		if (Program.mhpUpdateCategory == UpdateCategory.EGINV || Program.mhpUpdateCategory == UpdateCategory.EGUPD) {
			return this.getInternalReachables();
		} else if (Program.mhpUpdateCategory == UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.getInternalReachables();
		} else { // Mode: LZINC
			if (!BeginPhasePoint.getStaleBeginPhasePoints().contains(this)) {
				// This BPP has correct set of reachable nodes.
				return this.getInternalReachables();
			}
			// Otherwise, this BPP is stale.
			BeginPhasePoint.stabilizeStaleBeginPhasePoints();
			return this.getInternalReachables();
		}
	}

	private Set<EndPhasePoint> getInternalNextBarriers() {
		if (nextBarrierSet == null) {
			this.recomputeSets();
		}
		return this.nextBarrierSet;
	}

	public Set<Node> getInternalReachables() {
		if (reachableNodeSet == null) {
			this.recomputeSets();
		}
		return this.reachableNodeSet;
	}

	/**
	 * Populate the reachable nodes, and next set of barriers, for this node. This
	 * method also triggers updates to the
	 * PFG, when needed.
	 */
	@Deprecated
	public void populateReachablesAndBarriers() {
		boolean changed = this.recomputeSets();

		// Settle this phase, and the PFG.
		if (changed) {
			Set<Phase> affectedPhases = new HashSet<>(this.getPhaseSet());
			boolean update = false;
			for (Phase ph : affectedPhases) {
				update |= ph.deprecated_settleThisPhase();
			}
			if (update) {
				for (Phase ph : affectedPhases) {
					Phase.removeUnreachablePhases(ph.getParConstruct());
				}
			}
		}
	}

	/**
	 * This method recomputes the set of reachable nodes and next end-points,
	 * without reflecting this change in the
	 * corresponding phases that may start at this BeginPhasePoint.
	 *
	 * @return true, if the state of existing sets changed.
	 */
	public boolean recomputeSets() {
		Set<NodeWithStack> endPoints = new HashSet<>();
		Set<NodeWithStack> reachablesWithStack = new HashSet<>();
		assert (!(this.getNode() instanceof EndNode && this.getNode().getParent() instanceof ParallelConstruct));
		for (NodeWithStack succ : this.getNode().getInfo().getCFGInfo()
				.getInterProceduralLeafSuccessors(this.getCallStack())) {
			Set<NodeWithStack> someEndPoints = new HashSet<>();
			reachablesWithStack
					.addAll(CollectorVisitor.collectNodesIntraTaskForwardBarrierFreePath(succ, someEndPoints));
			reachablesWithStack.add(succ);
			reachablesWithStack.addAll(someEndPoints);
			endPoints.addAll(someEndPoints);
		}

		Set<Node> newReachableNodeSet;
		Set<EndPhasePoint> newNextBarrierSet;
		newReachableNodeSet = new HashSet<>();
		for (NodeWithStack nodeWithStack : reachablesWithStack) {
			newReachableNodeSet.add(nodeWithStack.getNode());
		}
		newNextBarrierSet = new HashSet<>();
		for (NodeWithStack nodeWithStack : endPoints) {
			newNextBarrierSet.add(new EndPhasePoint(nodeWithStack.getNode(), nodeWithStack.getCallStack()));
		}
		// Equate reachableNodeSet to newReachableNodeSet.
		if (reachableNodeSet == null) {
			reachableNodeSet = new HashSet<>();
		}
		boolean changed = this.reachableNodeSet.retainAll(newReachableNodeSet);
		changed |= this.reachableNodeSet.addAll(newReachableNodeSet);

		// Equate nextBarrierSet to newNextBarrierSet.
		if (this.nextBarrierSet == null) {
			this.nextBarrierSet = new HashSet<>();
		}
		changed |= this.nextBarrierSet.retainAll(newNextBarrierSet);
		changed |= this.nextBarrierSet.addAll(newNextBarrierSet);
		// this.setsInvalid = false;
		return changed;
	}

	public void overwriteWithNewSets() {
		if (reachableNodeSet == null) {
			reachableNodeSet = new HashSet<>();
		}
		if (this.nextBarrierSet == null) {
			this.nextBarrierSet = new HashSet<>();
		}
		Set<NodeWithStack> endPoints = new HashSet<>();
		Set<NodeWithStack> reachablesWithStack = new HashSet<>();
		assert (!(this.getNode() instanceof EndNode && this.getNode().getParent() instanceof ParallelConstruct));
		for (NodeWithStack succ : this.getNode().getInfo().getCFGInfo()
				.getInterProceduralLeafSuccessors(this.getCallStack())) {
			Set<NodeWithStack> someEndPoints = new HashSet<>();
			reachablesWithStack
					.addAll(CollectorVisitor.collectNodesIntraTaskForwardBarrierFreePath(succ, someEndPoints));
			reachablesWithStack.add(succ);
			reachablesWithStack.addAll(someEndPoints);
			endPoints.addAll(someEndPoints);
		}

		for (NodeWithStack nodeWithStack : reachablesWithStack) {
			this.reachableNodeSet.add(nodeWithStack.getNode());
		}
		for (NodeWithStack nodeWithStack : endPoints) {
			this.reachableNodeSet.add(nodeWithStack.getNode());
			this.nextBarrierSet.add(new EndPhasePoint(nodeWithStack.getNode(), nodeWithStack.getCallStack()));
		}
	}

	/**
	 * Remove the node {@code node} from the reachability set of this phase point.
	 * <br>
	 * Note that this method does not attempt removal of any other nodes from the
	 * reachability set of this phase point.
	 *
	 * @param node
	 *             node that has to be removed from the reachability set of this
	 *             phase point.
	 *
	 * @return true, if {@code leafNode} was found in the reachability set and
	 *         removed.
	 */
	public void removeNode(Node leafNode) {
		assert (Misc.isCFGLeafNode(leafNode));
		if (this.reachableNodeSet == null) {
			this.recomputeSets();
		}
		if (!this.reachableNodeSet.contains(leafNode)) {
			return;
		}
		this.reachableNodeSet.remove(leafNode);

		for (Phase ph : this.getPhaseSet()) {
			boolean nodeExists = false;
			for (BeginPhasePoint bpp : ph.getBeginPoints()) {
				if (bpp.getReachableNodes().contains(leafNode)) {
					nodeExists = true;
					break;
				}
			}
			if (!nodeExists) {
				ph.removeNode(leafNode);
			}
		}
	}

	/**
	 * Note that equality of BeginPhasePoint and EndPhasePoint is well-defined.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof NodeWithStack)) {
			return false;
		}
		NodeWithStack that = (NodeWithStack) other;
		if (this.node == that.getNode() && this.callStack.equals(that.getCallStack())) {
			return true;
		}
		return false;
	}

	/**
	 * Invalidates the reachability and nextBarrier sets for this BeginPhasePoint.
	 */
	public void setInvalidFlag() {
		return;
		// this.setsInvalid = true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getNode() == null) ? 0 : this.getNode().hashCode());
		result = prime * result + this.callStack.size();
		return result;
	}

	@Override
	public String toString() {
		String retString = "";
		if (node instanceof BarrierDirective) {
			retString += "line#" + Misc.getLineNum(node);
		} else {
			BeginNode beginNode = (BeginNode) node;
			Node parent = beginNode.getParent();
			retString += "begin_line#" + Misc.getLineNum(parent);
		}
		return retString;
	}

	/**
	 * Obtain set of all those BeginPhasePoints from which one or more elements of
	 * {@code nodeSet} might be reachable.
	 * <br>
	 * Note that this method would also trigger resetting of {@link #setsInvalid} of
	 * all the {@link BeginPhasePoint}
	 * nodes, after refreshing the corresponding lists of reachable nodes and
	 * barriers.
	 *
	 * @param nodeSet
	 *                set of elements for which we wish to obtain the set of
	 *                BeginPhasePoints from which one or more elements
	 *                of the set are reachable.
	 *
	 * @return set of BeginPhasePoints from which one or more elements of
	 *         {@code nodeSet} are reachable on barrier-free
	 *         path of same nesting level of parallelism.
	 */
	@Deprecated
	public static Set<BeginPhasePoint> getRelatedBPPs(Set<Node> nodeSet) {
		Set<BeginPhasePoint> bppSet = new HashSet<>();
		for (AbstractPhasePointable bppAbs : AbstractPhasePointable.getAllBeginPhasePoints()) {
			BeginPhasePoint bpp = (BeginPhasePoint) bppAbs;
			if (Misc.doIntersect(bpp.getReachableNodes(), nodeSet)) {
				bppSet.add(bpp);
			}
		}
		return bppSet;
	}

	/**
	 * Adds {@code endPP} to the {@link #nextBarrierSet nextBarrierSet} of this
	 * phase point.
	 * <br>
	 * Note that this method would not have any side-effects on sets of other phase
	 * points.
	 *
	 * @param endPP
	 *              a phase point that has to be added in the {@link #nextBarrierSet
	 *              nextBarrierSet} of this node.
	 */
	@Deprecated
	public void deprecated_addNextBarrier(EndPhasePoint endPP) {
		if (this.nextBarrierSet == null) {
			this.populateReachablesAndBarriers();
		}
		if (this.nextBarrierSet.contains(endPP)) {
			return;
		}
		this.nextBarrierSet.add(endPP);

		for (Phase ph : this.getPhaseSet()) {
			ph.deprecated_addEndPoint(endPP);
		}
	}

	/**
	 * @return
	 *
	 * @deprecated
	 */
	@Deprecated
	public Set<EndPhasePoint> deprecated_getStableNextBarriers() {
		BeginPhasePoint.deprecated_stabilizeStaleBeginPhasePoints();
		return this.getInternalNextBarriers();
	}

	/**
	 * @return
	 *
	 * @deprecated
	 */
	@Deprecated
	public Set<Phase> deprecated_getStablePhases() {
		BeginPhasePoint.deprecated_stabilizeStaleBeginPhasePoints();
		Phase.deprecated_stabilizePhases();
		return this.getPhaseSet();
	}

	/**
	 * @return
	 *
	 * @deprecated
	 */
	@Deprecated
	public Set<Node> deprecated_getStableReachables() {
		BeginPhasePoint.deprecated_stabilizeStaleBeginPhasePoints();
		Set<Node> reachables = this.getInternalReachables();
		/*
		 * Test code below.
		 */
		// for (Node n : reachables) {
		// if (!n.getInfo().isConnectedToProgram()) {
		// DumpSnapshot.dumpRoot("temp");
		// Thread.dumpStack();
		// assert (false) : "ERROR: " + n.getInfo().getDebugString() + " IS DISCONNECTED
		// TO THE BARRIER "
		// + this.getNode().getInfo().getDebugString();
		// }
		// }

		/*
		 * Test code above.
		 */
		return reachables;
	}

	/**
	 * Populate the reachable nodes, and next set of barriers, for this node. This
	 * method also triggers updates to the
	 * PFG, when needed, but doesn't remove any unconnected nodes.
	 */
	@Deprecated
	public void deprecated_populateReachablesAndBarriersWithoutCleaning() {
		boolean changed = this.recomputeSets();
		// Settle this phase, and the PFG.
		if (changed) {
			Set<Phase> affectedPhases = new HashSet<>(this.phaseSet);
			for (Phase ph : affectedPhases) {
				ph.deprecated_settleThisPhase();
			}
		}
	}

	/**
	 * Removes {@code endPP} from {@link #nextBarrierSet} of this phase point.
	 * <br>
	 * Note that this method does not have side-effects on the set of other phase
	 * points.
	 *
	 * @param endPP
	 *              an EndPhasePoint that has to be added to the
	 *              {@link #nextBarrierSet} of this phase point.
	 */
	@Deprecated
	public void deprecated_removeNextBarrier(EndPhasePoint endPP) {
		if (this.nextBarrierSet == null) {
			this.populateReachablesAndBarriers();
		}
		if (!this.nextBarrierSet.contains(endPP)) {
			return;
		}
		this.nextBarrierSet.remove(endPP);

		for (Phase ph : this.phaseSet) {
			boolean exists = false;
			for (BeginPhasePoint bpp : ph.getBeginPoints()) {
				if (bpp.getInternalNextBarriers().contains(endPP)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				ph.deprecated_removeEndPoint(endPP);
			}
		}
	}

	/**
	 * Given a new set of next end-phase points (barriers), this method makes
	 * appropriate changes to the PFG.
	 *
	 * @param newNextBarriers
	 *                        a new set of next end-phase points (barriers), for
	 *                        this begin-phase points.
	 */
	@Deprecated
	public void deprecated_setNewNextBarrierSet(Set<EndPhasePoint> newNextBarriers) {
		if (this.nextBarrierSet == null) {
			this.nextBarrierSet = new HashSet<>();
		}
		// Remove end-phase points.
		Set<EndPhasePoint> endPhasePointsForRemoval = new HashSet<>();
		for (EndPhasePoint oldEndPP : this.nextBarrierSet) {
			if (!newNextBarriers.contains(oldEndPP)) {
				endPhasePointsForRemoval.add(oldEndPP);
			}
		}
		boolean changed = false;
		for (EndPhasePoint oldEndPP : endPhasePointsForRemoval) {
			changed = true;
			this.nextBarrierSet.remove(oldEndPP);
		}

		// Add end-phase points.
		for (EndPhasePoint newEndPP : newNextBarriers) {
			if (!this.nextBarrierSet.contains(newEndPP)) {
				changed = true;
				this.nextBarrierSet.add(newEndPP);
			}
		}

		// Settle this phase, and the PFG.
		if (changed) {
			Set<Phase> affectedPhases = this.phaseSet;
			for (Phase ph : affectedPhases) {
				ph.deprecated_settleThisPhase();
			}
		}
	}

	/**
	 * Given a new set of reachable nodes {@code newNodes}, this method makes
	 * appropriate changes to the PFG.
	 *
	 * @param newNodes
	 *                 a new set of reachable nodes for this begin-phase point.
	 */
	@Deprecated
	public void deprecated_setNewReachabilitySet(Set<Node> newNodes) {
		if (this.reachableNodeSet == null) {
			this.reachableNodeSet = new HashSet<>();
		}
		// Remove nodes.
		Set<Node> nodesToBeRemoved = new HashSet<>();
		for (Node oldNode : this.reachableNodeSet) {
			if (!newNodes.contains(oldNode)) {
				nodesToBeRemoved.add(oldNode);
			}
		}
		boolean changed = false;
		for (Node oldNode : nodesToBeRemoved) {
			changed = true;
			this.reachableNodeSet.remove(oldNode);
		}

		// Add nodes.
		for (Node newNode : newNodes) {
			if (!this.reachableNodeSet.contains(newNode)) {
				changed = true;
				this.reachableNodeSet.add(newNode);
			}
		}

		// Settle this phase, and the PFG.
		if (changed) {
			Set<Phase> affectedPhases = this.phaseSet;
			for (Phase ph : affectedPhases) {
				ph.deprecated_settleThisPhase();
			}
		}
	}

	// private static void setStaleBeginPhasePoints(Set<BeginPhasePoint>
	// staleBeginPhasePoints) {
	// BeginPhasePoint.staleBeginPhasePoints = staleBeginPhasePoints;
	// }
	//
}
