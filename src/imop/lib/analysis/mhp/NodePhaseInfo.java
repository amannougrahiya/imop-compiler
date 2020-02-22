/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import imop.ast.node.external.Node;
import imop.ast.node.internal.DummyFlushDirective;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterThreadBackwardCellularAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterThreadBackwardNonCellularAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterThreadForwardCellularAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterThreadForwardNonCellularAnalysis;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.cfg.parallel.DataFlowGraph;
import imop.lib.cfg.parallel.InterTaskEdge;
import imop.lib.util.Misc;
import imop.parser.Program;
import imop.parser.Program.UpdateCategory;

/**
 * This class contains the parallel phase information corresponding to the node.
 * 
 * @author aman
 *
 */
public class NodePhaseInfo {
	private final Node node;
	/**
	 * Set of phases in which this node may get executed.
	 */
	private Set<Phase> phaseSet = new HashSet<>();

	private Set<Phase> inputPhaseSet;

	public void flushMHPData(Phase phase) {
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
		assert (Misc.isCFGLeafNode(node));
		this.node = node;
	}

	public Set<Phase> getStalePhaseSet() {
		return this.phaseSet;
	}

	public Set<Phase> getPhaseSet() {
		if (Program.mhpUpdateCategory == UpdateCategory.EGFF || Program.mhpUpdateCategory == UpdateCategory.EGINC) {
			return this.phaseSet;
		} else if (Program.mhpUpdateCategory == UpdateCategory.LZFF) {
			if (BeginPhasePoint.globalMHPStale) {
				BeginPhasePoint.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			return this.phaseSet;
		}
		BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		return this.phaseSet;
	}

	Node getNode() {
		return node;
	}

	/**
	 * This method adds {@code phase} to the set of phases in which the owner
	 * node may get executed. It also triggers an automated update for
	 * inter-task edges.
	 * 
	 * @param phase
	 */
	public void addPhase(Phase phase) {
		if (this.phaseSet.contains(phase)) {
			return;
		}
		this.phaseSet.add(phase);
		if (this.getNode() instanceof DummyFlushDirective) {
			DummyFlushDirective thisNode = (DummyFlushDirective) this.getNode();
			for (Node n : phase.getStaleNodeSet()) {
				if (n instanceof DummyFlushDirective) {
					DataFlowGraph.createEdgeBetween(thisNode, (DummyFlushDirective) n);
				}
			}
		}
		// In case if this method has been called from some context other than Phase.addNode()
		if (!phase.getStaleNodeSet().contains(this.getNode())) {
			phase.addNode(this.getNode());
		}
	}

	/**
	 * Removes {@code phase} from the set of phases in which the owner node may
	 * get executed. It also triggers automated updates for inter-task edges, if
	 * the owner node is a DummyFlushDirective.
	 * 
	 * @param phase
	 *            phase that has to be removed from {@code phaseSet} of this
	 *            object.
	 */
	public void removePhase(Phase phase) {
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
				if (!Misc.doIntersect(this.phaseSet, thatNode.getInfo().getNodePhaseInfo().phaseSet)) {
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
				if (!Misc.doIntersect(this.phaseSet, thatNode.getInfo().getNodePhaseInfo().phaseSet)) {
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
		// In case if this method has been called from some context other than Phase.removeNode()
		if (phase.getNodeSet().contains(this.getNode())) {
			phase.removeNode(this.getNode());
		}
	}

	public void rememberCurrentPhases() {
		//		BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		//		Phase.stabilizePhases();
		if (this.phaseSet != null) {
			inputPhaseSet = new HashSet<>(this.phaseSet);
		} else {
			inputPhaseSet = new HashSet<>();
		}
	}

	public Set<Phase> getInputPhasesOfOldTimes() {
		if (inputPhaseSet == null) {
			inputPhaseSet = new HashSet<>();
		}
		return Collections.unmodifiableSet(inputPhaseSet);
	}

	public String getPhaseString() {
		String ret = "[";
		for (Phase ph : this.getPhaseSet()) {
			ret += ph.getPhaseId() + ";";
		}
		ret += "]";
		return ret;
	}

	@Deprecated
	public void crudeSetPhaseSet(Set<Phase> phaseSet) {
		this.phaseSet = phaseSet;
	}

	@Deprecated
	public NodePhaseInfo getCopy(Node copyOwner) {
		NodePhaseInfo newPhaseInfo = new NodePhaseInfo(copyOwner);
		newPhaseInfo.crudeSetPhaseSet(new HashSet<>(phaseSet));
		return newPhaseInfo;
	}
}
