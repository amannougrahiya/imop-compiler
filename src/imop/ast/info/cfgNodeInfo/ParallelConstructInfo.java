/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.OmpConstructInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.analysis.mhp.yuan.BTBarrierNode;
import imop.lib.analysis.mhp.yuan.BTNode;
import imop.lib.analysis.mhp.yuan.YPhase;
import imop.lib.cfg.info.ParallelConstructCFGInfo;
import imop.lib.util.CellSet;
import imop.lib.util.CollectorVisitor;
import imop.lib.util.Misc;
import imop.lib.util.ProfileSS;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParallelConstructInfo extends OmpConstructInfo {
	private List<? extends AbstractPhase<?, ?>> allPhaseList;

	public static class BTPair {
		public final BTNode first;
		public final BTNode second;

		public BTPair(BTNode first, BTNode second) {
			this.first = first;
			this.second = second;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			} else if (!(obj instanceof BTPair)) {
				return false;
			}
			BTPair that = (BTPair) obj;
			if ((this.first == that.first && this.second == that.second)
					|| (this.first == that.second && this.second == that.first)) {
				return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return this.first.hashCode() + this.second.hashCode();
		}

		@Override
		public String toString() {
			return "{" + first.getString(0) + ", " + second.getString(0) + "}";
		}
	}

	private Set<BTPair> btPairs = new HashSet<>();

	public void addBTPair(BTBarrierNode first, BTBarrierNode second) {
		this.btPairs.add(new BTPair(first, second));
	}

	public Set<BTPair> getBTPairs() {
		return btPairs;
	}

	private ParallelConstructInfo(Node owner) {
		super(owner);
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			this.allPhaseList = new ArrayList<Phase>();
		} else {
			this.allPhaseList = new ArrayList<YPhase>();
		}
	}

	/**
	 * A factory method that creates a parallel construct info, and also initializes
	 * its entry {@link BeginPhasePoint}
	 * and entry {@link Phase}.
	 *
	 * @param owner
	 *              a parallel construct whose info object has to be created.
	 *
	 * @return an info object of the parallel-construct.
	 */
	public static ParallelConstructInfo createParallelConstructInfo(ParallelConstruct owner) {
		ParallelConstructInfo parConsInfo;
		parConsInfo = new ParallelConstructInfo(owner);
		// OLD CODE: We do not maintain entryBPP or entryPhase now.
		// parConsInfo.entryPhase = new Phase(parConsInfo);
		// NodeWithStack entryNWS = new
		// NodeWithStack(parConsInfo.getCFGInfo().getNestedCFG().getBegin(), new
		// CallStack());
		// parConsInfo.entryBPP = BeginPhasePoint.getBeginPhasePoint(entryNWS,
		// parConsInfo.entryPhase);
		// List<BeginPhasePoint> bppSet = new ArrayList<>();
		// bppSet.add(parConsInfo.entryBPP);
		// parConsInfo.entryPhase.setBeginPointsNoUpdate(bppSet);
		//
		// // Mark entry BPP as stale.
		// Set<BeginPhasePoint> staleBPPs = new HashSet<>();
		// staleBPPs.add(parConsInfo.entryBPP);
		// parConsInfo.entryBPP.setInvalidFlag();
		// BeginPhasePoint.addStaleBeginPhasePoints(staleBPPs);
		//
		// // Mark entry phase as stale.
		// Set<Phase> stalePhases = new HashSet<>();
		// stalePhases.add(parConsInfo.entryPhase);
		// Phase.addStalePhases(stalePhases);
		return parConsInfo;
	}

	public void flushMHPData() {
		for (AbstractPhase<?, ?> ph : new ArrayList<>(this.allPhaseList)) {
			ph.flushMHPData();
		}
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (btPairs == null) {
				btPairs = new HashSet<>();
			}
			this.btPairs.clear();
		}
		this.allPhaseList.clear();
	}

	public void flushALLMHPData() {
		for (AbstractPhase<?, ?> ph : this.allPhaseList) {
			ph.flushALLMHPData();
		}
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (btPairs == null) {
				btPairs = new HashSet<>();
			}
			this.btPairs.clear();
		}
		this.allPhaseList.clear();
	}

	@Override
	public ParallelConstructCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new ParallelConstructCFGInfo(getNode());
		}
		return (ParallelConstructCFGInfo) cfgInfo;
	}

	public List<? extends AbstractPhase<?, ?>> getAllPhaseList() {
		ProfileSS.addRelevantChangePoint(ProfileSS.phSet);
		return allPhaseList;
	}

	public List<? extends AbstractPhase<?, ?>> getConnectedPhases() {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			List<Phase> reachablePhases = new ArrayList<>();
			Phase tempPhase = (Phase) this.getFirstPhase();
			if (tempPhase == null) {
				assert (false) : "Could not find the first phase of the parallel construct at line #"
						+ Misc.getLineNum(this.getNode());
				return null;
			}
			reachablePhases.add(tempPhase);
			while (tempPhase.getSuccPhase() != null) {
				tempPhase = tempPhase.getSuccPhase();
				if (reachablePhases.contains(tempPhase)) {
					break;
				}
				reachablePhases.add(tempPhase);
			}
			return reachablePhases;
		} else {
			List<YPhase> reachablePhases = new ArrayList<>();
			boolean found = false;
			for (AbstractPhase<?, ?> tempPhaseAbs : this.getFirstPhaseSet()) {
				found = true;
				YPhase tempPhase = (YPhase) tempPhaseAbs;
				reachablePhases.add(tempPhase);
				Set<YPhase> endPoints = new HashSet<>();
				reachablePhases.addAll(CollectorVisitor.collectNodeSetInGenericGraph(tempPhase, endPoints, n -> false,
						p -> (Set<YPhase>) p.getSuccPhases()));
				reachablePhases.addAll(endPoints);
			}
			if (!found) {
				assert (false) : "Could not find the first phase of the parallel construct at line #"
						+ Misc.getLineNum(this.getNode());
				return null;
			}
			return reachablePhases;
		}
	}

	public Set<AbstractPhase<?, ?>> getFirstPhaseSet() {
		return (Set<AbstractPhase<?, ?>>) this.getCFGInfo().getNestedCFG().getBegin().getInfo().getNodePhaseInfo()
				.getPhaseSet();
	}

	public AbstractPhase<?, ?> getFirstPhase() {
		assert (Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YUANMHP) : "Unexpected path.";
		for (AbstractPhase<?, ?> ph : this.getCFGInfo().getNestedCFG().getBegin().getInfo().getNodePhaseInfo()
				.getPhaseSet()) {
			if (ph.getParConstruct() == this.getNode()) {
				return ph;
			}
		}
		return null;
	}

	@Deprecated
	public void reinitAllPhaseList() {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			this.allPhaseList = new ArrayList<Phase>();
		} else {
			this.allPhaseList = new ArrayList<YPhase>();
		}
	}

	public void removePhase(AbstractPhase<?, ?> phaseToBeRemoved) {
		if (!this.allPhaseList.contains(phaseToBeRemoved)) {
			return;
		}
		while (this.allPhaseList.remove(phaseToBeRemoved)) {
			;
		}
		for (Node entry : new HashSet<>(phaseToBeRemoved.getNodeSet())) {
			entry.getInfo().getNodePhaseInfo().removePhase(phaseToBeRemoved);
		}
		for (AbstractPhasePointable bpp : new HashSet<>(phaseToBeRemoved.getBeginPoints())) {
			bpp.removePhase(phaseToBeRemoved);
		}

	}

	public Set<String> getListedPrivateNames() {
		List<OmpClause> clauseList = this.getOmpClauseList();
		Set<String> privateIdNames = new HashSet<>();
		for (OmpClause clause : clauseList) {
			if (clause instanceof OmpPrivateClause || clause instanceof OmpLastPrivateClause
					|| clause instanceof OmpFirstPrivateClause || clause instanceof OmpCopyinClause
					|| clause instanceof OmpReductionClause) {
				privateIdNames.addAll(clause.getInfo().getListedNames());
			}
		}
		return privateIdNames;
	}

	public CellSet getListedPrivateSymbols() {
		List<OmpClause> clauseList = this.getOmpClauseList();
		CellSet privateCells = new CellSet();
		for (OmpClause clause : clauseList) {
			if (clause instanceof OmpPrivateClause || clause instanceof OmpLastPrivateClause
					|| clause instanceof OmpFirstPrivateClause || clause instanceof OmpCopyinClause
					|| clause instanceof OmpReductionClause) {
				privateCells.addAll(clause.getInfo().getListedSymbols());
			}
		}
		return privateCells;
	}

	/**
	 * A temporary method. DO NOT USE IT, except at known places! This method does
	 * not guarantee automated updates.
	 */
	public void addClause(OmpClause newClause) {
		if (newClause instanceof IfClause || newClause instanceof NumThreadsClause) {
			ParallelConstruct parCons = (ParallelConstruct) this.getNode();
			UniqueParallelOrDataClauseList clauseList = parCons.getParConsF1().getF1();
			NodeChoice nC = new NodeChoice(newClause);
			UniqueParallelClause upC = new UniqueParallelClause(nC);
			nC = new NodeChoice(upC);
			AUniqueParallelOrDataClause updC = new AUniqueParallelOrDataClause(nC);
			clauseList.getF0().addNode(updC);
		} else if (newClause instanceof OmpPrivateClause || newClause instanceof OmpSharedClause
				|| newClause instanceof OmpFirstPrivateClause || newClause instanceof OmpCopyinClause
				|| newClause instanceof OmpReductionClause) {
			ParallelConstruct parCons = (ParallelConstruct) this.getNode();
			UniqueParallelOrDataClauseList clauseList = parCons.getParConsF1().getF1();
			NodeChoice nC = new NodeChoice(newClause);
			DataClause dC = new DataClause(nC);
			nC = new NodeChoice(dC);
			AUniqueParallelOrDataClause updC = new AUniqueParallelOrDataClause(nC);
			clauseList.getF0().addNode(updC);

		}
	}

	/**
	 * A temporary method. DO NOT USE IT, except at known places! This method does
	 * not guarantee automated updates.
	 *
	 * @param clause
	 *
	 * @return true, if the clause was found and removed.
	 */
	public boolean removeClause(OmpClause clause) {
		assert (this.getOmpClauseList().contains(clause));
		ParallelConstruct parCons = (ParallelConstruct) this.getNode();
		UniqueParallelOrDataClauseList updL = parCons.getParConsF1().getF1();
		Node nodeToBeRemoved = null;
		for (Node aupdc : updL.getF0().getNodes()) {
			List<OmpClause> tempList = Misc.getInheritedEncloseeList(aupdc, OmpClause.class);
			assert (tempList.size() == 1);
			if (tempList.get(0) == clause) {
				nodeToBeRemoved = aupdc;
			}
		}
		if (nodeToBeRemoved == null) {
			return false;
		}
		updL.getF0().removeNode(nodeToBeRemoved);
		return true;
	}

	@Deprecated
	private Phase entryPhase;
	@Deprecated
	private BeginPhasePoint entryBPP;

	@Deprecated
	public void setAllPhaseList(List<Phase> allPhaseList) {
		this.allPhaseList = allPhaseList;
	}

	@Deprecated
	public void setEntryPhase(Phase entryPhase) {
		this.entryPhase = entryPhase;
	}

	@Deprecated
	public void setEntryBeginPhasePoint(BeginPhasePoint entryBPP) {
		this.entryBPP = entryBPP;
	}

	/**
	 * Singleton entry phase corresponding to this parallel construct. Note that
	 * this phase's begin-phase point set
	 * would consist of a single element, namely the {@link BeginNode} of the
	 * parallel construct (which would also be
	 * part of the {@code nodeSet} of this phase).
	 *
	 * @return entry phase of this parallel construct.
	 */
	@Deprecated
	public Phase getEntryPhase() {
		return this.entryPhase;
	}

	/**
	 * A {@link BeginPhasePoint} encapsulating the {@code BeginNode} of this
	 * parallel construct, with an empty
	 * call-stack.
	 *
	 * @return the starting BPP of this parallel construct.
	 */
	@Deprecated
	public BeginPhasePoint getEntryBPP() {
		return this.entryBPP;
	}
}
