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

public class TaskConstructCFGInfo extends CFGInfo {

	public TaskConstructCFGInfo(Node owner) {
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
		TaskConstruct owner = (TaskConstruct) this.getOwner();
		TaskClause taskClause = TaskConstructCFGInfo.getTaskClauseWrapper(ifClause);
		NodeListOptional clauseList = owner.getF2();
		clauseList.addNode(taskClause);
		AutomatedUpdater.invalidateSymbolsInNode(taskClause);
		updateCFGForIfClauseAddition(ifClause);

		ifClause = Normalization.normalizeLeafNodes(ifClause, new ArrayList<>());

		Program.invalidColumnNum = true;
		AutomatedUpdater.updateInformationForAddition(ifClause);// Added, so that any changes from points-to may be
																// reflected here.
		// AutomatedUpdater.invalidateSymbolsInNode(taskClause);// Added, so that any
		// changes from points-to may be reflected here.
	}

	public IfClause getIfClause() {
		TaskConstruct node = (TaskConstruct) this.getOwner();
		NodeListOptional clauseList = node.getF2();
		for (Node clauseElement : clauseList.getNodes()) {
			TaskClause clauseChoice = (TaskClause) clauseElement;
			if (clauseChoice.getF0().getChoice() instanceof UniqueTaskClause) {
				UniqueTaskClause utc = (UniqueTaskClause) clauseChoice.getF0().getChoice();
				OmpClause clause = (OmpClause) utc.getF0().getChoice();
				if (clause instanceof IfClause) {
					return (IfClause) clause;
				}
			}
		}
		return null;
	}

	public boolean hasIfClause() {
		TaskConstruct node = (TaskConstruct) this.getOwner();
		NodeListOptional clauseList = node.getF2();
		for (Node clauseElement : clauseList.getNodes()) {
			TaskClause clauseChoice = (TaskClause) clauseElement;
			if (clauseChoice.getF0().getChoice() instanceof UniqueTaskClause) {
				UniqueTaskClause utc = (UniqueTaskClause) clauseChoice.getF0().getChoice();
				OmpClause clause = (OmpClause) utc.getF0().getChoice();
				if (clause instanceof IfClause) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean removeIfClause() {
		TaskConstruct node = (TaskConstruct) this.getOwner();
		NodeListOptional clauseList = node.getF2();
		int index = 0;
		boolean found = false;
		for (Node clause : clauseList.getNodes()) {
			clause = Misc.getInternalFirstCFGNode(clause);
			if (clause == null) {
				index++;
				continue;
			}
			if (clause instanceof IfClause) {
				AutomatedUpdater.flushCaches();
				found = true;
				Set<Node> rerunNodesForward = AutomatedUpdater.nodesForForwardRerunOnRemoval(clause);
				Set<Node> rerunNodesBackward = AutomatedUpdater.nodesForBackwardRerunOnRemoval(clause);
				Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
						.updateBPPOrGetAffectedBPPSetUponRemoval(clause);
				AutomatedUpdater.updateInformationForRemoval(clause);
				updateCFGForIfClauseRemoval((IfClause) clause);
				Program.invalidColumnNum = true;
				AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
				AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after removal is successful.
				AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
				break;
			}
			index++;
		}
		if (found) {
			Node clause = clauseList.getNodes().get(index);
			clauseList.removeNode(clause);
			AutomatedUpdater.invalidateSymbolsInNode(clause);
			return true;
		} else {
			return false;
		}
	}

	public void setFinalClause(FinalClause finalClause) {
		FinalClause oldClause = this.getFinalClause();
		if (finalClause == oldClause) {
			return;
		}
		if (oldClause != null) {
			removeFinalClause();
		}
		NodeRemover.removeNodeIfConnected(finalClause);
		TaskConstruct owner = (TaskConstruct) this.getOwner();
		TaskClause taskClause = TaskConstructCFGInfo.getTaskClauseWrapper(finalClause);
		NodeListOptional clauseList = owner.getF2();
		clauseList.addNode(taskClause);
		AutomatedUpdater.invalidateSymbolsInNode(taskClause);
		updateCFGForFinalClauseAddition(finalClause);

		finalClause = Normalization.normalizeLeafNodes(finalClause, new ArrayList<>());

		Program.invalidColumnNum = true;
		AutomatedUpdater.updateInformationForAddition(taskClause);
		// AutomatedUpdater.invalidateSymbolsInNode(taskClause);// Added, so that any
		// changes from points-to may be reflected here.
	}

	public FinalClause getFinalClause() {
		TaskConstruct node = (TaskConstruct) this.getOwner();
		NodeListOptional clauseList = node.getF2();
		for (Node clauseElement : clauseList.getNodes()) {
			TaskClause clauseChoice = (TaskClause) clauseElement;
			if (clauseChoice.getF0().getChoice() instanceof UniqueTaskClause) {
				UniqueTaskClause utc = (UniqueTaskClause) clauseChoice.getF0().getChoice();
				OmpClause clause = (OmpClause) utc.getF0().getChoice();
				if (clause instanceof FinalClause) {
					return (FinalClause) clause;
				}
			}
		}
		return null;
	}

	public boolean hasFinalClause() {
		TaskConstruct node = (TaskConstruct) this.getOwner();
		NodeListOptional clauseList = node.getF2();
		for (Node clauseElement : clauseList.getNodes()) {
			TaskClause clauseChoice = (TaskClause) clauseElement;
			if (clauseChoice.getF0().getChoice() instanceof UniqueTaskClause) {
				UniqueTaskClause utc = (UniqueTaskClause) clauseChoice.getF0().getChoice();
				OmpClause clause = (OmpClause) utc.getF0().getChoice();
				if (clause instanceof FinalClause) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean removeFinalClause() {
		TaskConstruct owner = (TaskConstruct) this.getOwner();
		NodeListOptional clauseList = owner.getF2();
		int index = 0;
		boolean found = false;
		for (Node clause : clauseList.getNodes()) {
			clause = Misc.getInternalFirstCFGNode(clause);
			if (clause == null) {
				index++;
				continue;
			}
			if (clause instanceof FinalClause) {
				found = true;
				AutomatedUpdater.flushCaches();
				Set<Node> rerunNodesForward = AutomatedUpdater.nodesForForwardRerunOnRemoval(clause);
				Set<Node> rerunNodesBackward = AutomatedUpdater.nodesForBackwardRerunOnRemoval(clause);
				Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
						.updateBPPOrGetAffectedBPPSetUponRemoval(clause);
				AutomatedUpdater.updateInformationForRemoval(clause);
				updateCFGForFinalClauseRemoval((FinalClause) clause);
				Program.invalidColumnNum = true;
				AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
				AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after removal is successful.
				AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
				break;
			}
			index++;
		}
		if (found) {
			Node clause = clauseList.getNodes().get(index);
			clauseList.removeNode(clause);
			AutomatedUpdater.invalidateSymbolsInNode(clause);
			return true;
		} else {
			return false;
		}
	}

	public List<OmpClause> getCFGClauseList() {
		List<OmpClause> retList = new ArrayList<>();
		if (this.hasIfClause()) {
			retList.add(this.getIfClause());
		}
		if (this.hasFinalClause()) {
			retList.add(this.getFinalClause());
		}

		return retList;
	}

	public List<SideEffect> setBody(Statement stmt) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		TaskConstruct owner = (TaskConstruct) this.getOwner();
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
		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(owner.getF4());
		stmt.setParent(owner);

		Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(owner.getF4());
		Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(owner.getF4());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF4());
		AutomatedUpdater.updateInformationForRemoval(owner.getF4());
		updateCFGForBodyRemoval(owner.getF4());
		owner.setF4(stmt);
		AutomatedUpdater.invalidateSymbolsInNode(owner.getF4());
		AutomatedUpdater.invalidateSymbolsInNode(stmt);
		updateCFGForBodyAddition(stmt);

		stmt = Normalization.normalizeLeafNodes(stmt, sideEffectList);

		// this.getOwner().accept(new CompoundStatementEnforcer());// COMMENTED
		// RECENTLY.
		Program.invalidColumnNum = Program.invalidLineNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateInformationForAddition(stmt);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
		// AutomatedUpdater.invalidateSymbolsInNode(owner.getF4());// Added, so that any
		// changes from points-to may be reflected here.
		// AutomatedUpdater.invalidateSymbolsInNode(stmt);// Added, so that any changes
		// from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
		return sideEffectList;
	}

	@Override
	public Statement getBody() {
		return (Statement) Misc.getInternalFirstCFGNode(((TaskConstruct) getOwner()).getF4());
	}

	private void updateCFGForBodyRemoval(Statement removed) {
		removed = (Statement) Misc.getInternalFirstCFGNode(removed);
		// // 1. Adjust incompleteness.
		// removed.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerRemoval();

		// 2. Remove stale edges.
		removed.getInfo().getCFGInfo().clearAllEdges();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(components, removed);
		// disconnectAndAdjustEndReachability(removed, components);
		// }
	}

	private void updateCFGForBodyAddition(Statement added) {
		added = (Statement) Misc.getInternalFirstCFGNode(added);
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// 1. Add new edges.
		List<OmpClause> clauseList = this.getCFGClauseList();
		Statement body = added;
		if (clauseList.isEmpty()) {
			connectAndAdjustEndReachability(ncfg.getBegin(), body);
		} else {
			OmpClause lastClause = clauseList.get(clauseList.size() - 1);
			connectAndAdjustEndReachability(lastClause, body);
		}
		if (body.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(body, ncfg.getEnd());
		}

		// // 2. Adjust incompleteness
		// added.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerAddition();
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
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// Add new edges.
		IfClause ifClause = added;
		List<OmpClause> clauseList = this.getCFGClauseList();
		Statement body = this.getBody();
		assert (!clauseList.isEmpty());
		if (clauseList.size() == 1) {
			disconnectAndAdjustEndReachability(ncfg.getBegin(), body);
			connectAndAdjustEndReachability(ncfg.getBegin(), ifClause);
		} else {
			OmpClause lastClause = clauseList.get(clauseList.size() - 2);
			disconnectAndAdjustEndReachability(lastClause, body);
			connectAndAdjustEndReachability(lastClause, ifClause);
		}
		connectAndAdjustEndReachability(ifClause, body);
	}

	private void updateCFGForFinalClauseRemoval(FinalClause removed) {
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

	private void updateCFGForFinalClauseAddition(FinalClause added) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// Add new edges.
		FinalClause finalClause = added;
		List<OmpClause> clauseList = this.getCFGClauseList();
		Statement body = this.getBody();
		assert (!clauseList.isEmpty());
		if (clauseList.size() == 1) {
			disconnectAndAdjustEndReachability(ncfg.getBegin(), body);
			connectAndAdjustEndReachability(ncfg.getBegin(), finalClause);
		} else {
			OmpClause lastClause = clauseList.get(clauseList.size() - 2);
			disconnectAndAdjustEndReachability(lastClause, body);
			connectAndAdjustEndReachability(lastClause, finalClause);
		}
		connectAndAdjustEndReachability(finalClause, body);
	}

	public static TaskClause getTaskClauseWrapper(OmpClause ompClause) {
		TaskClause taskClause = Misc.getEnclosingNode(ompClause, TaskClause.class);
		if (taskClause == null) {
			Node chosenNode = null;
			if (ompClause instanceof IfClause || ompClause instanceof FinalClause || ompClause instanceof UntiedClause
					|| ompClause instanceof MergeableClause) {
				Node chosenNodeWrapper = Misc.getEnclosingNode(ompClause, UniqueTaskClause.class);
				if (chosenNodeWrapper != null) {
					chosenNode = chosenNodeWrapper;
				} else {
					chosenNode = new UniqueTaskClause(new NodeChoice(ompClause));
				}
			} else if (ompClause instanceof OmpPrivateClause || ompClause instanceof OmpFirstPrivateClause
					|| ompClause instanceof OmpLastPrivateClause || ompClause instanceof OmpSharedClause
					|| ompClause instanceof OmpCopyinClause || ompClause instanceof OmpDfltSharedClause
					|| ompClause instanceof OmpDfltNoneClause || ompClause instanceof OmpReductionClause) {
				Node chosenNodeWrapper = Misc.getEnclosingNode(ompClause, DataClause.class);
				if (chosenNodeWrapper != null) {
					chosenNode = chosenNodeWrapper;
				} else {
					chosenNode = new DataClause(new NodeChoice(ompClause));
				}
			}
			assert (chosenNode instanceof DataClause || chosenNode instanceof UniqueTaskClause);
			taskClause = new TaskClause(new NodeChoice(chosenNode));
			return taskClause;
		} else {
			return taskClause;
		}

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
		if (this.hasFinalClause()) {
			retList.add(this.getFinalClause());
		}
		retList.add(this.getBody());
		retList.add(this.getNestedCFG().getEnd());
		return retList;
	}
}
