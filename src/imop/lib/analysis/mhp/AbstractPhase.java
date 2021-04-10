/*
 *   Copyright (c) 2020 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 *   This file is a part of the project IMOP, licensed under the MIT license.
 *   See LICENSE.md for the full text of the license.
 *
 *   The above notice shall be included in all copies or substantial
 *   portions of this file.
 */

package imop.lib.analysis.mhp;

import imop.ast.node.external.*;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.util.CellSet;
import imop.lib.util.ProfileSS;
import imop.parser.Program;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the notion of a compile-time abstraction of a runtime phase.
 * <br>
 * Each phase starts at a set of barriers, and ends at the next set of reachable
 * barriers. A phase may be SVE-sensitive
 * or -insensitive.
 *
 * @param <B>
 *            type of elements of abstraction where a phase may start.
 * @param <E>
 *            type of elements of abstraction where a phase may end.
 */
public abstract class AbstractPhase<B extends AbstractPhasePointable, E extends AbstractPhasePointable> {
	public static long stabilizationTime = 0;
	public static boolean globalMHPStale = false; // Used only during LZFF mode.
	protected static int counter = 0;

	protected final ParallelConstruct parConstruct;
	protected Set<Node> nodeSet = new HashSet<>(); // This is the set of nodes which are a part of this phase.
	protected Set<B> beginPoints = new HashSet<B>();
	protected Set<E> endPoints = new HashSet<E>();
	protected Set<? extends AbstractPhase<B, E>> succPhases;
	protected Set<? extends AbstractPhase<B, E>> predPhases;
	protected final int phaseId;

	public Set<Node> getNodeSet() {
		ProfileSS.addRelevantChangePoint(ProfileSS.phSet);
		if (Program.mhpUpdateCategory == Program.UpdateCategory.EGINV
				|| Program.mhpUpdateCategory == Program.UpdateCategory.EGUPD) {
			return this.nodeSet;
		} else if (Program.mhpUpdateCategory == Program.UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.nodeSet;
		}
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		} else {
			assert (false) : "Unexpected path.";
		}
		return this.nodeSet;
	}

	public Set<B> getBeginPoints() {
		if (Program.mhpUpdateCategory == Program.UpdateCategory.EGINV
				|| Program.mhpUpdateCategory == Program.UpdateCategory.EGUPD) {
			return this.beginPoints;
		} else if (Program.mhpUpdateCategory == Program.UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.beginPoints;
		}
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		} else {
			assert (false) : "Unexpected path.";
		}
		return this.beginPoints;
	}

	public Set<B> getStaleBeginPoints() {
		return this.beginPoints;
	}

	public Set<E> getEndPoints() {
		if (Program.mhpUpdateCategory == Program.UpdateCategory.EGINV
				|| Program.mhpUpdateCategory == Program.UpdateCategory.EGUPD) {
			return this.endPoints;
		} else if (Program.mhpUpdateCategory == Program.UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.endPoints;
		}
		BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		return this.endPoints;
	}

	public Set<E> getStaleEndPoints() {
		return this.endPoints;
	}

	public Set<? extends AbstractPhase<?, ?>> getSuccPhases() {
		ProfileSS.addRelevantChangePoint(ProfileSS.phSet);
		if (Program.mhpUpdateCategory == Program.UpdateCategory.EGINV
				|| Program.mhpUpdateCategory == Program.UpdateCategory.EGUPD) {
			return this.succPhases;
		} else if (Program.mhpUpdateCategory == Program.UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.succPhases;
		}
		BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		return succPhases;
	}

	public Set<? extends AbstractPhase<?, ?>> getPredPhases() {
		ProfileSS.addRelevantChangePoint(ProfileSS.phSet);
		if (Program.mhpUpdateCategory == Program.UpdateCategory.EGINV
				|| Program.mhpUpdateCategory == Program.UpdateCategory.EGUPD) {
			return this.predPhases;
		} else if (Program.mhpUpdateCategory == Program.UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.predPhases;
		}
		BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		return this.predPhases;
	}

	public AbstractPhase(ParallelConstruct owner) {
		this.parConstruct = owner;
		this.phaseId = counter++;
	}

	public void flushALLMHPData() {
		for (Node n : this.nodeSet) {
			n.getInfo().getNodePhaseInfo().flushALLMHPData();
		}
		this.nodeSet.clear();
		this.beginPoints.clear();
		this.endPoints.clear();
		this.succPhases.clear();
		this.predPhases.clear();
	}

	public void flushMHPData() {
		for (Node n : new HashSet<>(this.nodeSet)) {
			n.getInfo().getNodePhaseInfo().flushMHPData(this);
			this.nodeSet.remove(n);
		}
		for (AbstractPhasePointable bpp : this.beginPoints) {
			bpp.flushMHPData(this);
		}
		this.beginPoints.clear();
		this.endPoints.clear();
		this.succPhases.clear();
		this.predPhases.clear();
	}

	public final ParallelConstruct getParConstruct() {
		return this.parConstruct;
	}

	public final int getPhaseId() {
		return phaseId;
	}

	/**
	 * Create a new edge in the phase-flow graph from {@code source} phase to the
	 * {@code destination} phase.
	 *
	 * @param source
	 *                    source of the new edge to be created in the PFG.
	 * @param destination
	 *                    destination of the new edge to be created in the PFG.
	 */
	@SuppressWarnings("unchecked")
	public static <P extends AbstractPhasePointable, Q extends AbstractPhasePointable, R extends AbstractPhase<P, Q>> void connectPhases(
			R source, R destination) {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			source.succPhases.clear(); // ICON supports only one successor phase per phase.
		}
		((Set<R>) source.succPhases).add(destination);
		((Set<R>) destination.predPhases).add(source);
	}

	/**
	 * Remove an existing edge in the phase-flow graph from {@code source} phase to
	 * {@code destination} phase.
	 *
	 * @param source
	 *                    source phase of an existing edge that has to be removed.
	 * @param destination
	 *                    destination phase of an existing edge that has to be
	 *                    removed.
	 */
	public static void disconnectPhases(AbstractPhase<?, ?> source, AbstractPhase<?, ?> destination) {
		if (source != null) {
			source.succPhases.remove(destination);
		}
		if (destination != null) {
			destination.predPhases.remove(source);
		}
	}

	/**
	 * Removes {@code nodeToBeRemoved} from this phase, and this phase from the
	 * node.
	 * <br>
	 * Note that updates to inter-task edges are implicit in this method.
	 *
	 * @param beginPoints
	 *                    node that has to be removed.
	 */
	public void setBeginPointsNoUpdate(List<B> beginPoints) {
		this.beginPoints = new HashSet<B>();
		this.beginPoints.addAll(beginPoints);
	}

	/**
	 * Add the specified end-point to the end-points of this phase. This method does
	 * not support automated updates.
	 *
	 * @return
	 */
	public boolean addEndPointNoUdpate(E newEndPP) {
		if (this.endPoints == null) {
			this.endPoints = new HashSet<>();
		}
		return this.endPoints.add(newEndPP);
	}

	public final Set<Node> getStaleNodeSet() {
		ProfileSS.addRelevantChangePoint(ProfileSS.phSet);
		return this.nodeSet;
	}

	/**
	 * Adds {@code nodeToBeAdded} to {@link #nodeSet} of this phase.
	 *
	 * @param nodeToBeAdded
	 *                      node to be added in this phase.
	 */
	public void addNode(Node nodeToBeAdded) {
		if (this.nodeSet.contains(nodeToBeAdded)) {
			return;
		}
		this.nodeSet.add(nodeToBeAdded);
		if (!nodeToBeAdded.getInfo().getNodePhaseInfo().getStalePhaseSet().contains(this)) {
			nodeToBeAdded.getInfo().getNodePhaseInfo().addPhase(this);
		}
	}

	public void removeNode(Node nodeToBeRemoved) {
		if (!this.nodeSet.contains(nodeToBeRemoved)) {
			return;
		}
		this.nodeSet.remove(nodeToBeRemoved);
		if (nodeToBeRemoved.getInfo().getNodePhaseInfo().getPhaseSet().contains(this)) {
			nodeToBeRemoved.getInfo().getNodePhaseInfo().removePhase(this);
		}
	}

	public final CellSet getSharedReads() {
		CellSet readSet = new CellSet();
		for (Node n : getNodeSet()) {
			readSet.addAll(n.getInfo().getSharedReads());
		}
		return readSet;
	}

	public final CellSet getSharedWrites() {
		CellSet writeSet = new CellSet();
		for (Node n : getNodeSet()) {
			writeSet.addAll(n.getInfo().getSharedWrites());
		}
		return writeSet;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractPhase)) {
			return false;
		}
		AbstractPhase<?, ?> other = (AbstractPhase<?, ?>) obj;
		return other.phaseId == this.phaseId;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.getPhaseId();
		return result;
	}

	public String toString() {
		return "phase#" + this.phaseId;
	}

	/**
	 * @param removedNode
	 *
	 * @deprecated
	 */
	@Deprecated
	public void deprecated_crudeRemoveNode(Node removedNode) {
		this.nodeSet.remove(removedNode);
	}

	/**
	 * @param addedNode
	 *
	 * @deprecated
	 */
	@Deprecated
	public void deprecated_crudeAddNode(Node addedNode) {
		this.nodeSet.add(addedNode);
	}

}
