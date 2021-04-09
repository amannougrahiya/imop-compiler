/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.info;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.cfg.NestedCFG;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.transform.simplify.ImplicitBarrierRemover;
import imop.lib.transform.simplify.Normalization;
import imop.lib.transform.simplify.SplitCombinedConstructs;
import imop.lib.transform.updater.NodeRemover;
import imop.lib.transform.updater.sideeffect.AddedEnclosingBlock;
import imop.lib.transform.updater.sideeffect.NodeUpdated;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParallelConstructCFGInfo extends CFGInfo {

	public ParallelConstructCFGInfo(Node owner) {
		super(owner);
	}

	public void setIfClause(IfClause ifClause) {
		IfClause oldClause = this.getIfClause();
		if (ifClause == oldClause) {
			return;
		}
		if (oldClause != null) {
			removeIfClause();
		}

		NodeRemover.removeNodeIfConnected(ifClause);
		ParallelConstruct owner = (ParallelConstruct) this.getOwner();
		UniqueParallelOrDataClauseList clauseList = owner.getParConsF1().getF1();
		AUniqueParallelOrDataClause newClauseWrapper = ParallelConstructCFGInfo
				.getAUniqueParallelOrDataClauseWrapper(ifClause);
		clauseList.getF0().addNode(newClauseWrapper);
		AutomatedUpdater.invalidateSymbolsInNode(newClauseWrapper);
		updateCFGForIfClauseAddition(ifClause);

		ifClause = Normalization.normalizeLeafNodes(ifClause, new ArrayList<>());

		Program.invalidColumnNum = true;
		AutomatedUpdater.updateInformationForAddition(ifClause);
		// AutomatedUpdater.invalidateSymbolsInNode(newClauseWrapper);// Added, so that
		// any changes from points-to may be reflected here.
	}

	public IfClause getIfClause() {
		UniqueParallelOrDataClauseList clauseList = ((ParallelConstruct) getOwner()).getParConsF1().getF1();
		for (Node aUPDCnode : clauseList.getF0().getNodes()) {
			OmpClause existingClause = (OmpClause) Misc.getInternalFirstCFGNode(aUPDCnode);
			if (existingClause instanceof IfClause) {
				return (IfClause) existingClause;
			}
		}
		return null;
	}

	public boolean hasIfClause() {
		return (this.getIfClause() != null);
	}

	public boolean removeIfClause() {
		UniqueParallelOrDataClauseList clauseList = ((ParallelConstruct) getOwner()).getParConsF1().getF1();
		int index = 0;
		boolean found = false;
		for (Node aUPDCnode : clauseList.getF0().getNodes()) {
			OmpClause existingClause = (OmpClause) Misc.getInternalFirstCFGNode(aUPDCnode);
			if (existingClause instanceof IfClause) {
				AutomatedUpdater.flushCaches();
				found = true;
				Set<Node> rerunNodesForward = AutomatedUpdater.nodesForForwardRerunOnRemoval(existingClause);
				Set<Node> rerunNodesBackward = AutomatedUpdater.nodesForBackwardRerunOnRemoval(existingClause);
				Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
						.updateBPPOrGetAffectedBPPSetUponRemoval(existingClause);
				AutomatedUpdater.updateInformationForRemoval(existingClause);
				updateCFGForIfClauseRemoval((IfClause) existingClause);
				Program.invalidColumnNum = true;
				AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
				AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after removal is successful.
				AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
				break;
			}
			index++;
		}
		if (found) {
			Node clause = clauseList.getF0().getNodes().get(index);
			clauseList.getF0().removeNode(clause);
			AutomatedUpdater.invalidateSymbolsInNode(clause);
			return true;
		} else {
			return false;
		}
	}

	public void setNumThreadsClause(NumThreadsClause numThreadsClause) {
		NumThreadsClause oldNT = this.getNumThreadsClause();
		if (numThreadsClause == oldNT) {
			return;
		}
		if (oldNT != null) {
			removeNumThreadsClause();
		}
		NodeRemover.removeNodeIfConnected(numThreadsClause);
		ParallelConstruct owner = (ParallelConstruct) this.getOwner();
		UniqueParallelOrDataClauseList clauseList = owner.getParConsF1().getF1();
		AUniqueParallelOrDataClause newClauseWrapper = ParallelConstructCFGInfo
				.getAUniqueParallelOrDataClauseWrapper(numThreadsClause);
		clauseList.getF0().addNode(newClauseWrapper);
		AutomatedUpdater.invalidateSymbolsInNode(newClauseWrapper);
		updateCFGForNumThreadsClauseAddition(numThreadsClause);

		numThreadsClause = Normalization.normalizeLeafNodes(numThreadsClause, new ArrayList<>());

		Program.invalidColumnNum = true;
		AutomatedUpdater.updateInformationForAddition(numThreadsClause);
		// AutomatedUpdater.invalidateSymbolsInNode(newClauseWrapper);// Added, so that
		// any changes from points-to may be reflected here.
	}

	public NumThreadsClause getNumThreadsClause() {
		UniqueParallelOrDataClauseList clauseList = ((ParallelConstruct) getOwner()).getParConsF1().getF1();
		for (Node aUPDCnode : clauseList.getF0().getNodes()) {
			OmpClause existingClause = (OmpClause) Misc.getInternalFirstCFGNode(aUPDCnode);
			if (existingClause instanceof NumThreadsClause) {
				return (NumThreadsClause) existingClause;
			}
		}
		return null;
	}

	public boolean hasNumThreadsClause() {
		return (this.getNumThreadsClause() != null);
	}

	public boolean removeNumThreadsClause() {
		UniqueParallelOrDataClauseList clauseList = ((ParallelConstruct) getOwner()).getParConsF1().getF1();
		int index = 0;
		boolean found = false;
		for (Node aUPDCnode : clauseList.getF0().getNodes()) {
			OmpClause existingClause = (OmpClause) Misc.getInternalFirstCFGNode(aUPDCnode);
			if (existingClause instanceof NumThreadsClause) {
				found = true;
				AutomatedUpdater.flushCaches();
				Set<Node> rerunNodesForward = AutomatedUpdater.nodesForForwardRerunOnRemoval(existingClause);
				Set<Node> rerunNodesBackward = AutomatedUpdater.nodesForBackwardRerunOnRemoval(existingClause);
				Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
						.updateBPPOrGetAffectedBPPSetUponRemoval(existingClause);
				AutomatedUpdater.updateInformationForRemoval(existingClause);
				updateCFGForNumThreadsClauseRemoval((NumThreadsClause) existingClause);
				Program.invalidColumnNum = true;
				AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
				AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after removal is successful.
				AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
				break;
			}
			index++;
		}
		if (found) {
			Node clause = clauseList.getF0().getNodes().get(index);
			clauseList.getF0().removeNode(clause);
			AutomatedUpdater.invalidateSymbolsInNode(clause);
			return true;
		} else {
			return false;
		}
	}

	public List<OmpClause> getCFGClauseList() {
		List<OmpClause> retList = new ArrayList<>();
		UniqueParallelOrDataClauseList clauseList = ((ParallelConstruct) getOwner()).getParConsF1().getF1();
		for (Node clause : clauseList.getF0().getNodes()) {
			clause = Misc.getInternalFirstCFGNode(clause);
			if (clause instanceof IfClause) {
				retList.add((OmpClause) clause);
			}
			if (clause instanceof NumThreadsClause) {
				retList.add((OmpClause) clause);
			}
		}

		return retList;
	}

	public List<SideEffect> setBody(Statement stmt) {
		// assert (false) : "This method is unsafe!";
		List<SideEffect> sideEffectList = new ArrayList<>();
		ParallelConstruct owner = (ParallelConstruct) this.getOwner();
		stmt = Misc.getStatementWrapper(stmt);
		if (stmt == this.getBody()) {
			return sideEffectList;
		}
		AutomatedUpdater.flushCaches();

		List<SideEffect> splitSE = SplitCombinedConstructs.splitCombinedConstructForTheStatement(stmt);
		if (!splitSE.isEmpty()) {
			NodeUpdated nodeUpdatedSE = (NodeUpdated) splitSE.get(0);
			// Note: Here we reparse the parallel construct so that we can perform other
			// normalizations within it.
			ParallelConstruct splitParCons = FrontEnd.parseAndNormalize(nodeUpdatedSE.affectedNode.toString(),
					ParallelConstruct.class);
			sideEffectList.add(new NodeUpdated(splitParCons, nodeUpdatedSE.getUpdateMessage()));
			sideEffectList.addAll(this.setBody(splitParCons));
			return sideEffectList;
		}

		if (!(stmt.getStmtF0().getChoice() instanceof CompoundStatement)) {
			Statement outSt = FrontEnd.parseAlone("{}", Statement.class);
			CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(outSt);
			sideEffectList = this.setBody(compStmt);
			sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(stmt));
			sideEffectList.add(new AddedEnclosingBlock(compStmt));
			return sideEffectList;
		}
		NodeRemover.removeNodeIfConnected(stmt);
		Statement newStmt = ImplicitBarrierRemover.makeBarrierExplicitForNode(stmt, sideEffectList);
		if (newStmt != stmt) {
			sideEffectList.addAll(this.setBody(newStmt));
			return sideEffectList;
		}

		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(stmt);
		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(owner.getParConsF2());
		stmt.setParent(owner);

		Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(owner.getParConsF2());
		Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(owner.getParConsF2());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getParConsF2());
		AutomatedUpdater.updateInformationForRemoval(owner.getParConsF2());
		updateCFGForBodyRemoval(owner.getParConsF2());
		owner.setParConsF2(stmt);
		AutomatedUpdater.invalidateSymbolsInNode(owner.getParConsF2());
		AutomatedUpdater.invalidateSymbolsInNode(stmt);
		updateCFGForBodyAddition(stmt);

		stmt = Normalization.normalizeLeafNodes(stmt, sideEffectList);

		// this.getOwner().accept(new CompoundStatementEnforcer());// COMMENTED
		// RECENTLY.
		Program.invalidColumnNum = Program.invalidLineNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateInformationForAddition(stmt);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
		// AutomatedUpdater.invalidateSymbolsInNode(owner.getParConsF2());// Added, so
		// that any changes from points-to may be reflected here.
		// AutomatedUpdater.invalidateSymbolsInNode(stmt);// Added, so that any changes
		// from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
		return sideEffectList;
	}

	@Override
	public Statement getBody() {
		return (Statement) Misc.getInternalFirstCFGNode(((ParallelConstruct) getOwner()).getParConsF2());
	}

	/**
	 * @param targetNode
	 * @return
	 */
	public static AUniqueParallelOrDataClause getAUniqueParallelOrDataClauseWrapper(OmpClause targetNode) {
		AUniqueParallelOrDataClause targetWrapper = Misc.getEnclosingNode(targetNode,
				AUniqueParallelOrDataClause.class);
		if (targetWrapper == null) {
			Node chosenNode = null;
			if (targetNode instanceof IfClause || targetNode instanceof NumThreadsClause) {
				Node chosenNodeWrapper = Misc.getEnclosingNode(targetNode, UniqueParallelClause.class);
				if (chosenNodeWrapper == null) {
					chosenNode = new UniqueParallelClause(new NodeChoice(targetNode));
				} else {
					chosenNode = chosenNodeWrapper;
				}
			} else if (targetNode instanceof OmpPrivateClause || targetNode instanceof OmpFirstPrivateClause
					|| targetNode instanceof OmpLastPrivateClause || targetNode instanceof OmpSharedClause
					|| targetNode instanceof OmpCopyinClause || targetNode instanceof OmpDfltSharedClause
					|| targetNode instanceof OmpDfltNoneClause || targetNode instanceof OmpReductionClause) {
				Node chosenNodeWrapper = Misc.getEnclosingNode(targetNode, DataClause.class);
				if (chosenNodeWrapper == null) {
					chosenNode = new DataClause(new NodeChoice(targetNode));
				} else {
					chosenNode = chosenNodeWrapper;
				}
			}
			assert (chosenNode instanceof UniqueParallelClause || chosenNode instanceof DataClause);
			targetWrapper = new AUniqueParallelOrDataClause(new NodeChoice(chosenNode));
		}
		return targetWrapper;
	}

	private void updateCFGForIfClauseRemoval(IfClause removed) {
		List<Node> predList = removed.getInfo().getCFGInfo().getPredBlocks();
		List<Node> succList = removed.getInfo().getCFGInfo().getSuccBlocks();

		for (Node pred : predList) {
			for (Node succ : succList) {
				connectAndAdjustEndReachability(pred, succ);
			}
		}

		// Remove stale edges.
		removed.getInfo().getCFGInfo().clearAllEdges();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForIfClauseAddition(IfClause added) {
		// Add new edges.
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		List<OmpClause> clauseList = this.getCFGClauseList();
		Statement body = this.getBody();
		assert (!clauseList.isEmpty());
		if (clauseList.size() == 1) {
			connectAndAdjustEndReachability(ncfg.getBegin(), added);
			disconnectAndAdjustEndReachability(ncfg.getBegin(), body);
		} else {
			OmpClause lastClause = clauseList.get(clauseList.size() - 2);
			connectAndAdjustEndReachability(lastClause, added);
			disconnectAndAdjustEndReachability(lastClause, body);
		}
		connectAndAdjustEndReachability(added, body);
	}

	private void updateCFGForNumThreadsClauseRemoval(NumThreadsClause removed) {
		List<Node> predList = removed.getInfo().getCFGInfo().getPredBlocks();
		List<Node> succList = removed.getInfo().getCFGInfo().getSuccBlocks();

		for (Node pred : predList) {
			for (Node succ : succList) {
				connectAndAdjustEndReachability(pred, succ);
			}
		}

		// Remove stale edges.
		removed.getInfo().getCFGInfo().clearAllEdges();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForNumThreadsClauseAddition(NumThreadsClause added) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// Add new edges.
		// Add new edges.
		List<OmpClause> clauseList = this.getCFGClauseList();
		Statement body = this.getBody();
		assert (!clauseList.isEmpty());
		if (clauseList.size() == 1) {
			connectAndAdjustEndReachability(ncfg.getBegin(), added);
			disconnectAndAdjustEndReachability(ncfg.getBegin(), body);
		} else {
			OmpClause lastClause = clauseList.get(clauseList.size() - 2);
			connectAndAdjustEndReachability(lastClause, added);
			disconnectAndAdjustEndReachability(lastClause, body);
		}
		connectAndAdjustEndReachability(added, body);
	}

	private void updateCFGForBodyRemoval(Statement removed) {
		removed = (Statement) Misc.getInternalFirstCFGNode(removed);
		// // 1. Adjust incompleteness.
		// removed.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerRemoval();

		// 2. Remove stale edges.
		removed.getInfo().getCFGInfo().clearAllEdges();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForBodyAddition(Statement added) {
		added = (Statement) Misc.getInternalFirstCFGNode(added);
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// 1. Add new edges.
		Statement body = (Statement) Misc.getInternalFirstCFGNode(added);
		List<OmpClause> clauseList = this.getCFGClauseList();
		if (clauseList.isEmpty()) {
			connectAndAdjustEndReachability(ncfg.getBegin(), body);
		} else {
			OmpClause lastClause = clauseList.get(clauseList.size() - 1);
			connectAndAdjustEndReachability(lastClause, body);
		}
		if (body.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(body, ncfg.getEnd());
		}

		// 2. Adjust incompleteness
		// OLD CODE: Note that body of a ParallelContruct must be a structured block.
		// added.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerAddition();
	}

	/**
	 * Obtain the various CFG components of the {@code owner} node.
	 * 
	 * @return
	 *         CFG components of the {@code owner} node.
	 */
	@Override
	public List<Node> getAllComponents() {
		List<Node> retList = new ArrayList<>();
		retList.add(this.getNestedCFG().getBegin());
		if (this.hasIfClause()) {
			retList.add(this.getIfClause());
		}
		if (this.hasNumThreadsClause()) {
			retList.add(this.getNumThreadsClause());
		}
		retList.add(this.getBody());
		retList.add(this.getNestedCFG().getEnd());
		return retList;
	}
}
