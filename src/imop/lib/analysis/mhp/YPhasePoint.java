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
import imop.ast.node.internal.*;
import imop.lib.analysis.mhp.yuan.YPhase;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.CollectorVisitor;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class YPhasePoint implements AbstractPhasePointable {
	private final Node node;
	private Set<Node> reachableNodeSet = null;
	private Set<YPhasePoint> nextBarrierSet = null;
	private Set<YPhase> phaseSet = new HashSet<>(); // set of phases of which this object is a begin point.

	public YPhasePoint(Node node) {
		this.node = node;
		// Note that we do not add to AbstractPhasePointable.allBeginPhasePoints here as
		// this
		// constructor is also invoked for generation of a logical EndPhasePoint.
		// Instead, we perform this addition in getYPhasePoint().
	}

	public static YPhasePoint getYPhasePoint(Node node, YPhase ph) {
		YPhasePoint returnYPP = null;
		for (AbstractPhasePointable bppAbs : AbstractPhasePointable.allBeginPhasePoints) {
			YPhasePoint ypp = (YPhasePoint) bppAbs;
			if (ypp.node == node) {
				returnYPP = ypp;
				break;
			}
		}
		if (returnYPP == null) {
			returnYPP = new YPhasePoint(node);
			AbstractPhasePointable.allBeginPhasePoints.add(returnYPP);

		}
		returnYPP.phaseSet.add(ph);
		return returnYPP;
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
		assert (false) : "Unexpected path";
	}

	@Override
	public Set<Node> getReachableNodes() {
		if (Program.mhpUpdateCategory == Program.UpdateCategory.EGINV
				|| Program.mhpUpdateCategory == Program.UpdateCategory.EGUPD) {
			return this.getInternalReachables();
		} else if (Program.mhpUpdateCategory == Program.UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.getInternalReachables();
		} else { // Mode: LZINC
			assert (false) : "Unexpected path.";
			return null;
		}
	}

	@Override
	public Set<? extends AbstractPhase<?, ?>> getPhaseSet() {
		if (Program.mhpUpdateCategory == Program.UpdateCategory.EGINV
				|| Program.mhpUpdateCategory == Program.UpdateCategory.EGUPD) {
			return this.phaseSet;
		} else if (Program.mhpUpdateCategory == Program.UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.phaseSet;
		} else { // Mode: LZINC
			assert (false) : "Unexpected path.";
			return null;
		}
	}

	@Override
	public Set<? extends AbstractPhasePointable> getNextBarriers() {
		if (Program.mhpUpdateCategory == Program.UpdateCategory.EGINV
				|| Program.mhpUpdateCategory == Program.UpdateCategory.EGUPD) {
			return this.getInternalNextBarriers();
		} else if (Program.mhpUpdateCategory == Program.UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.getInternalNextBarriers();
		} else { // Mode: LZINC
			assert (false) : "Unexpected path.";
			return null;
		}
	}

	private Set<YPhasePoint> getInternalNextBarriers() {
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
	 * This method recomputes the set of reachable nodes and next end-points,
	 * without reflecting this change in the
	 * corresponding phases that may start at this BeginPhasePoint.
	 *
	 * @return true, if the state of existing sets changed.
	 */
	public void recomputeSets() {
		Set<NodeWithStack> endPoints = new HashSet<>();
		Set<NodeWithStack> reachablesWithStack = new HashSet<>();
		for (NodeWithStack succ : this.getNode().getInfo().getCFGInfo()
				.getInterProceduralLeafSuccessors(new CallStack())) {
			Set<NodeWithStack> someEndPoints = new HashSet<>();
			reachablesWithStack
					.addAll(CollectorVisitor.collectNodesIntraTaskForwardBarrierFreePath(succ, someEndPoints));
			reachablesWithStack.add(succ);
			reachablesWithStack.addAll(someEndPoints);
			endPoints.addAll(someEndPoints);
		}
		this.reachableNodeSet = new HashSet<>();
		for (NodeWithStack nws : reachablesWithStack) {
			this.reachableNodeSet.add(nws.getNode());
		}
		this.nextBarrierSet = new HashSet<>();
		for (NodeWithStack nws : endPoints) {
			this.nextBarrierSet.add(new YPhasePoint(nws.getNode()));
		}
	}

	public Node getNode() {
		return node;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		YPhasePoint that = (YPhasePoint) o;
		return getNode() == that.getNode();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getNode());
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
}
