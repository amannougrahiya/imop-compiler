/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.generic.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.cfg.parallel.InterTaskEdge;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.lib.util.ProfileSS;
import imop.parser.Program;
import imop.parser.Program.UpdateCategory;

import java.util.HashSet;
import java.util.Set;

public class DummyFlushDirectiveInfo extends FlushDirectiveInfo {

	private Set<InterTaskEdge> incomingInterTaskEdges;
	private Set<InterTaskEdge> outgoingInterTaskEdges;

	public DummyFlushDirectiveInfo(Node owner) {
		super(owner);
	}

	public void flushALLMHPData() {
		this.incomingInterTaskEdges = null;
		this.outgoingInterTaskEdges = null;
	}

	/**
	 * If this node does not share any other common phase with its neighbour DFD,
	 * then remove the link with the
	 * neighbour.
	 *
	 * @param phase
	 *              phase that has to be removed.
	 */
	public void flushMHPData(AbstractPhase<?, ?> phase) {
		Set<? extends AbstractPhase<?, ?>> thisPhaseSet = this.getNodePhaseInfo().getStalePhaseSet();
		if (this.incomingInterTaskEdges == null) {
			assert (this.outgoingInterTaskEdges == null);
			return;
		}
		Set<DummyFlushDirective> disconnectedDFD = new HashSet<>();
		for (InterTaskEdge ite : new HashSet<>(this.incomingInterTaskEdges)) {
			DummyFlushDirective thatDFD = ite.getSourceNode();
			DummyFlushDirective thisDFD = (DummyFlushDirective) this.getNode();
			assert (ite.getDestinationNode() == thisDFD);
			boolean foundACommonPhase;
			Set<? extends AbstractPhase<?, ?>> thatPhaseSet = thatDFD.getInfo().getNodePhaseInfo().getStalePhaseSet();
			foundACommonPhase = thisPhaseSet.stream().filter(thisPh -> thisPh != phase)
					.anyMatch(thatPhaseSet::contains);

			if (!foundACommonPhase) {
				disconnectedDFD.add(thatDFD);
				this.incomingInterTaskEdges.remove(ite);
				thatDFD.getInfo().outgoingInterTaskEdges.remove(ite);
			}
		}
		for (InterTaskEdge ite : new HashSet<>(this.outgoingInterTaskEdges)) {
			// NOTE: THIS NEW CODE IS APPLICABLE ONLY IF ITE IS A SYMMETRIC RELATION.
			if (disconnectedDFD.contains(ite.getDestinationNode())) {
				this.outgoingInterTaskEdges.remove(ite);
				ite.getDestinationNode().getInfo().incomingInterTaskEdges.remove(ite);
			}
			// OLD CODE:
			// DummyFlushDirective thatDFD = ite.getDestinationNode();
			// DummyFlushDirective thisDFD = (DummyFlushDirective) this.getNode();
			// assert (ite.getSourceNode() == thisDFD);
			// boolean foundACommonPhase = false;
			// Set<Phase> thatPhaseSet = thatDFD.getInfo().getNodePhaseInfo().getPhaseSet();
			// for (Phase thisPh : thisPhaseSet) {
			// if (thisPh == phase) {
			// continue;
			// }
			// if (thatPhaseSet.contains(thisPh)) {
			// foundACommonPhase = true;
			// break;
			// }
			// }
			// if (!foundACommonPhase) {
			// this.outgoingInterTaskEdges.remove(ite);
			// thatDFD.getInfo().incomingInterTaskEdges.remove(ite);
			// }
		}

	}

	public Set<InterTaskEdge> getStaleIncomingInterTaskEdges() {
		ProfileSS.addRelevantChangePoint(ProfileSS.iteSet);
		if (incomingInterTaskEdges == null) {
			incomingInterTaskEdges = new HashSet<>();
		}
		return incomingInterTaskEdges;
	}

	public Set<InterTaskEdge> getIncomingInterTaskEdges() {
		ProfileSS.addRelevantChangePoint(ProfileSS.iteSet);
		if (incomingInterTaskEdges == null) {
			incomingInterTaskEdges = new HashSet<>();
		}
		if (Program.mhpUpdateCategory == UpdateCategory.EGINV || Program.mhpUpdateCategory == UpdateCategory.EGUPD) {
			return this.incomingInterTaskEdges;
		} else if (Program.mhpUpdateCategory == UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			if (incomingInterTaskEdges == null) {
				incomingInterTaskEdges = new HashSet<>();
			}
			return this.incomingInterTaskEdges;
		}
		BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		return this.incomingInterTaskEdges;
	}

	public Set<InterTaskEdge> getStaleOutgoingInterTaskEdges() {
		ProfileSS.addRelevantChangePoint(ProfileSS.iteSet);
		if (outgoingInterTaskEdges == null) {
			outgoingInterTaskEdges = new HashSet<>();
		}
		return this.outgoingInterTaskEdges;
	}

	public Set<InterTaskEdge> getOutgoingInterTaskEdges() {
		ProfileSS.addRelevantChangePoint(ProfileSS.iteSet);
		if (outgoingInterTaskEdges == null) {
			outgoingInterTaskEdges = new HashSet<>();
		}
		if (Program.mhpUpdateCategory == UpdateCategory.EGINV || Program.mhpUpdateCategory == UpdateCategory.EGUPD) {
			return this.outgoingInterTaskEdges;
		} else if (Program.mhpUpdateCategory == UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (AbstractPhase.globalMHPStale) {
				AbstractPhase.globalMHPStale = false;
				AutomatedUpdater.reinitMHP();
			}
			if (outgoingInterTaskEdges == null) {
				outgoingInterTaskEdges = new HashSet<>();
			}
			return this.outgoingInterTaskEdges;
		}
		BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		return this.outgoingInterTaskEdges;
	}

	public void addIncomingInterTaskEdge(InterTaskEdge edgeToThis) {
		if (!this.getStaleIncomingInterTaskEdges().contains(edgeToThis)) {
			this.getStaleIncomingInterTaskEdges().add(edgeToThis);
			if (BeginPhasePoint.stabilizationInProgress) {
				Set<Node> nodeSet = new HashSet<>();
				for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
					if (analysis instanceof InterThreadForwardCellularAnalysis
							|| analysis instanceof InterThreadForwardNonCellularAnalysis) {
						nodeSet.clear();
						nodeSet.add(edgeToThis.getDestinationNode());
						analysis.storeNodesToBeUpdated(nodeSet);
					}
					if (analysis instanceof InterThreadBackwardCellularAnalysis
							|| analysis instanceof InterThreadBackwardNonCellularAnalysis) {
						nodeSet.clear();
						nodeSet.add(edgeToThis.getSourceNode());
						analysis.storeNodesToBeUpdated(nodeSet);
					}
				}
			}
		}
	}

	public void addOutgoingInterTaskEdge(InterTaskEdge edgeFromThis) {
		if (!this.getStaleOutgoingInterTaskEdges().contains(edgeFromThis)) {
			this.getStaleOutgoingInterTaskEdges().add(edgeFromThis);
			if (BeginPhasePoint.stabilizationInProgress) {
				Set<Node> nodeSet = new HashSet<>();
				for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
					if (analysis instanceof InterThreadForwardCellularAnalysis
							|| analysis instanceof InterThreadForwardNonCellularAnalysis) {
						nodeSet.clear();
						nodeSet.add(edgeFromThis.getDestinationNode());
						analysis.storeNodesToBeUpdated(nodeSet);
					}
					if (analysis instanceof InterThreadBackwardCellularAnalysis
							|| analysis instanceof InterThreadBackwardNonCellularAnalysis) {
						nodeSet.clear();
						nodeSet.add(edgeFromThis.getSourceNode());
						analysis.storeNodesToBeUpdated(nodeSet);
					}
				}
			}
		}
	}

	/**
	 * Returns a set of only those dummy flush directives to which communication is
	 * made via at least one symbol.
	 *
	 * @return a set of only those dummy flush directives to which communication is
	 *         made via at least one symbol.
	 */
	public Set<DummyFlushDirective> getInterTaskDummyPredecessors() {
		Node node = this.getNode();
		assert (node instanceof DummyFlushDirective);
		Set<DummyFlushDirective> predecessors = new HashSet<>();
		if (node instanceof DummyFlushDirective) {
			DummyFlushDirective destinationDummyFlush = (DummyFlushDirective) node;
			for (InterTaskEdge interTaskEdge : destinationDummyFlush.getInfo().getIncomingInterTaskEdges()) {
				DummyFlushDirective sourceDummyFlush = interTaskEdge.getSourceNode();
				if (Program.preciseDFDEdges) {
					CellSet writtenBefore = sourceDummyFlush.getWrittenBeforeCells();
					CellSet readAfter = destinationDummyFlush.getReadAfterCells();
					CellSet communicationCells = Misc.setIntersection(writtenBefore, readAfter);
					if (communicationCells.isEmpty()) {
						continue;
					} else {
						predecessors.add(sourceDummyFlush);
					}
				} else {
					predecessors.add(sourceDummyFlush);
				}
			}
		}
		return predecessors;
	}

	/**
	 * Returns a set of only those dummy flush directives to which communication is
	 * made via at least one symbol.
	 *
	 * @return a set of only those dummy flush directives to which communication is
	 *         made via at least one symbol.
	 */
	public Set<DummyFlushDirective> getInterTaskDummySuccessors() {
		Node node = this.getNode();
		assert (node instanceof DummyFlushDirective);
		Set<DummyFlushDirective> successors = new HashSet<>();
		if (node instanceof DummyFlushDirective) {
			DummyFlushDirective sourceDummyFlush = (DummyFlushDirective) node;
			for (InterTaskEdge interTaskEdge : sourceDummyFlush.getInfo().getOutgoingInterTaskEdges()) {
				DummyFlushDirective destinationDummyFlush = interTaskEdge.getDestinationNode();
				if (Program.preciseDFDEdges) {
					CellSet writtenBefore = sourceDummyFlush.getWrittenBeforeCells();
					CellSet readAfter = destinationDummyFlush.getReadAfterCells();
					CellSet communicationCells = Misc.setIntersection(writtenBefore, readAfter);
					if (communicationCells.isEmpty()) {
						continue;
					} else {
						successors.add(destinationDummyFlush);
					}
				} else {
					successors.add(destinationDummyFlush);
				}
			}
		}
		return successors;
	}

}
