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
import imop.lib.analysis.flowanalysis.generic.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.yuan.YPhase;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.cfg.parallel.DataFlowGraph;
import imop.lib.cfg.parallel.InterTaskEdge;
import imop.lib.util.Misc;
import imop.lib.util.ProfileSS;
import imop.parser.Program;
import imop.parser.Program.UpdateCategory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains the parallel phase information corresponding to the node.
 *
 * @author aman
 */
public class NodePhaseInfo {
	private final Node node;
	/**
	 * Set of phases in which this node may get executed.
	 */
	private Set<? extends AbstractPhase<?, ?>> phaseSet;

	private Set<? extends AbstractPhase<?, ?>> inputPhaseSet;

	public void flushMHPData(AbstractPhase<?, ?> phase) {
		if (this.inputPhaseSet != null) {
			this.inputPhaseSet.remove(phase);
		}
		if (this.getNode() instanceof DummyFlushDirective) {
			DummyFlushDirective thisNode = (DummyFlushDirective) this.getNode();
			thisNode.getInfo().flushMHPData(phase);
		}
		this.phaseSet.remove(phase);
	}

	public void flushALLMHPData() {
		this.phaseSet.clear();
		this.inputPhaseSet = null;
		if (this.getNode() instanceof DummyFlushDirective) {
			DummyFlushDirective thisNode = (DummyFlushDirective) this.getNode();
			thisNode.getInfo().flushALLMHPData();
		}
	}

	public NodePhaseInfo(Node node) {
		ProfileSS.addRelevantChangePoint(ProfileSS.phSet);
		assert (Misc.isCFGLeafNode(node));
		this.node = node;
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			this.phaseSet = new HashSet<YPhase>();
		} else {
			this.phaseSet = new HashSet<Phase>();
		}
	}

	public Set<? extends AbstractPhase<?, ?>> getStalePhaseSet() {
		ProfileSS.addRelevantChangePoint(ProfileSS.phSet);
		return this.phaseSet;
	}

	public Set<? extends AbstractPhase<?, ?>> getPhaseSet() {
		ProfileSS.addRelevantChangePoint(ProfileSS.phSet);
		if (Program.mhpUpdateCategory == UpdateCategory.EGINV || Program.mhpUpdateCategory == UpdateCategory.EGUPD) {
			return this.phaseSet;
		} else if (Program.mhpUpdateCategory == UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.phaseSet;
		}
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		} else {
			assert (false) : "Unexpected path: LZUPD mode is not supported in Yuan's algorithm.";
		}
		return this.phaseSet;
	}

	Node getNode() {
		return node;
	}

	/**
	 * This method adds {@code phase} to the set of phases in which the owner node
	 * may get executed. It also triggers an
	 * automated update for inter-task edges.
	 *
	 * @param phase
	 */
	public void addPhase(AbstractPhase<?, ?> phase) {
		if (this.phaseSet.contains(phase)) {
			return;
		}
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			Collection<Phase> thisPhaseSet = (Collection<Phase>) this.phaseSet;
			thisPhaseSet.add((Phase) phase);
		} else {
			Collection<YPhase> thisPhaseSet = (Collection<YPhase>) this.phaseSet;
			thisPhaseSet.add((YPhase) phase);
		}

		if (this.getNode() instanceof DummyFlushDirective) {
			DummyFlushDirective thisNode = (DummyFlushDirective) this.getNode();
			for (Node n : phase.getStaleNodeSet()) {
				if (n instanceof DummyFlushDirective) {
					DataFlowGraph.createEdgeBetween(thisNode, (DummyFlushDirective) n);
				}
			}
		}
		// In case if this method has been called from some context other than
		// Phase.addNode()
		if (!phase.getStaleNodeSet().contains(this.getNode())) {
			phase.addNode(this.getNode());
		}
	}

	/**
	 * Removes {@code phase} from the set of phases in which the owner node may get
	 * executed. It also triggers automated
	 * updates for inter-task edges, if the owner node is a DummyFlushDirective.
	 *
	 * @param phase
	 *              phase that has to be removed from {@code phaseSet} of this
	 *              object.
	 */
	public void removePhase(AbstractPhase<?, ?> phase) {
		if (!this.phaseSet.contains(phase)) {
			return;
		}
		this.phaseSet.remove(phase);
		if (this.getNode() instanceof DummyFlushDirective) {
			DummyFlushDirective thisNode = (DummyFlushDirective) this.getNode();
			Set<InterTaskEdge> originalIncoming = thisNode.getInfo().getStaleIncomingInterTaskEdges();
			Set<InterTaskEdge> duplicateIncoming = new HashSet<>(originalIncoming);
			for (InterTaskEdge incomingITE : duplicateIncoming) {
				assert (incomingITE.getDestinationNode() == this.getNode());
				DummyFlushDirective thatNode = incomingITE.getSourceNode();
				boolean haveCommonPhase;
				if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
					haveCommonPhase = Misc.doIntersect((Set<YPhase>) this.phaseSet,
							(Set<YPhase>) thatNode.getInfo().getNodePhaseInfo().phaseSet);
				} else {
					haveCommonPhase = Misc.doIntersect((Set<Phase>) this.phaseSet,
							(Set<Phase>) thatNode.getInfo().getNodePhaseInfo().phaseSet);
				}
				if (!haveCommonPhase) {
					originalIncoming.remove(incomingITE);
					thatNode.getInfo().getStaleOutgoingInterTaskEdges().remove(incomingITE);
					if (BeginPhasePoint.stabilizationInProgress) {
						Set<Node> nodeSet = new HashSet<>();
						for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
							if (analysis instanceof InterThreadForwardCellularAnalysis
									|| analysis instanceof InterThreadForwardNonCellularAnalysis) {
								nodeSet.clear();
								nodeSet.add(incomingITE.getDestinationNode());
								analysis.storeNodesToBeUpdated(nodeSet);
							}
							if (analysis instanceof InterThreadBackwardCellularAnalysis
									|| analysis instanceof InterThreadBackwardNonCellularAnalysis) {
								nodeSet.clear();
								nodeSet.add(incomingITE.getSourceNode());
								analysis.storeNodesToBeUpdated(nodeSet);
							}
						}
					}
				}

			}

			Set<InterTaskEdge> originalOutgoing = thisNode.getInfo().getStaleOutgoingInterTaskEdges();
			Set<InterTaskEdge> duplicateOutgoing = new HashSet<>(originalOutgoing);
			for (InterTaskEdge outgoingITE : duplicateOutgoing) {
				assert (outgoingITE.getSourceNode() == this.getNode());
				DummyFlushDirective thatNode = outgoingITE.getDestinationNode();
				boolean haveCommonPhase;
				if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
					haveCommonPhase = Misc.doIntersect((Set<YPhase>) this.phaseSet,
							(Set<YPhase>) thatNode.getInfo().getNodePhaseInfo().phaseSet);
				} else {
					haveCommonPhase = Misc.doIntersect((Set<Phase>) this.phaseSet,
							(Set<Phase>) thatNode.getInfo().getNodePhaseInfo().phaseSet);
				}
				if (!haveCommonPhase) {
					originalOutgoing.remove(outgoingITE);
					thatNode.getInfo().getStaleIncomingInterTaskEdges().remove(outgoingITE);
					if (BeginPhasePoint.stabilizationInProgress) {
						Set<Node> nodeSet = new HashSet<>();
						for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
							if (analysis instanceof InterThreadForwardCellularAnalysis
									|| analysis instanceof InterThreadForwardNonCellularAnalysis) {
								nodeSet.clear();
								nodeSet.add(outgoingITE.getDestinationNode());
								analysis.storeNodesToBeUpdated(nodeSet);
							}
							if (analysis instanceof InterThreadBackwardCellularAnalysis
									|| analysis instanceof InterThreadBackwardNonCellularAnalysis) {
								nodeSet.clear();
								nodeSet.add(outgoingITE.getSourceNode());
								analysis.storeNodesToBeUpdated(nodeSet);
							}
						}
					}
				}
			}
		}
		// In case if this method has been called from some context other than
		// Phase.removeNode()
		if (phase.getNodeSet().contains(this.getNode())) {
			phase.removeNode(this.getNode());
		}
	}

	public void rememberCurrentPhases() {
		// BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		// Phase.stabilizePhases();
		if (this.phaseSet != null) {
			if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
				inputPhaseSet = new HashSet<YPhase>((Collection<? extends YPhase>) this.phaseSet);
			} else {
				inputPhaseSet = new HashSet<Phase>((Collection<? extends Phase>) this.phaseSet);
			}
		} else {
			if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
				inputPhaseSet = new HashSet<YPhase>();
			} else {
				inputPhaseSet = new HashSet<Phase>();
			}
		}
	}

	public Set<AbstractPhase<?, ?>> getInputPhasesOfOldTimes() {
		if (inputPhaseSet == null) {
			inputPhaseSet = new HashSet<>();
		}
		return Collections.unmodifiableSet(inputPhaseSet);
	}

	public String getPhaseString() {
		String ret = "[";
		for (AbstractPhase<?, ?> ph : this.getPhaseSet()) {
			ret += ph.getPhaseId() + ";";
		}
		ret += "]";
		return ret;
	}

	@Deprecated
	public void crudeSetPhaseSet(Set<? extends AbstractPhase<?, ?>> phaseSet) {
		this.phaseSet = phaseSet;
	}

	@Deprecated
	public NodePhaseInfo getCopy(Node copyOwner) {
		NodePhaseInfo newPhaseInfo = new NodePhaseInfo(copyOwner);
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			newPhaseInfo.crudeSetPhaseSet(new HashSet<YPhase>((Collection<? extends YPhase>) phaseSet));
		} else {
			newPhaseInfo.crudeSetPhaseSet(new HashSet<Phase>((Collection<? extends Phase>) phaseSet));
		}
		return newPhaseInfo;
	}
}
