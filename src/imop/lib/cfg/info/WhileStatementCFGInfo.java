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

public class WhileStatementCFGInfo extends CFGInfo {

	public WhileStatementCFGInfo(Node owner) {
		super(owner);
	}

	public void setPredicate(Expression predicate) {
		PointsToAnalysis.disableHeuristic();
		if (predicate == this.getPredicate()) {
			return;
		}
		AutomatedUpdater.flushCaches();

		NodeRemover.removeNodeIfConnected(predicate);

		WhileStatement owner = (WhileStatement) this.getOwner();
		predicate.setParent(owner);

		Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(owner.getF2());
		Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(owner.getF2());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF2());
		AutomatedUpdater.updateInformationForRemoval(owner.getF2());
		updateCFGForPredicateRemoval(owner.getF2());
		owner.setF2(predicate);
		AutomatedUpdater.invalidateSymbolsInNode(owner.getF2());
		AutomatedUpdater.invalidateSymbolsInNode(predicate);
		updateCFGForPredicateAddition(predicate);
		/*
		 * New code below.
		 */
		predicate = Normalization.normalizeLeafNodes(predicate, new ArrayList<>());
		/*
		 * New code above.
		 */
		Program.invalidColumnNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateInformationForAddition(predicate);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
		// AutomatedUpdater.invalidateSymbolsInNode(owner.getF2());// Added, so that any
		// changes from points-to may be reflected here.
		// AutomatedUpdater.invalidateSymbolsInNode(predicate);// Added, so that any
		// changes from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
	}

	public Expression getPredicate() {
		return ((WhileStatement) getOwner()).getF2();
	}

	public List<SideEffect> setBody(Statement stmt) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		WhileStatement owner = (WhileStatement) this.getOwner();
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
		return (Statement) Misc.getInternalFirstCFGNode(((WhileStatement) getOwner()).getF4());
	}

	private void updateCFGForPredicateRemoval(Expression removed) {
		this.getBody().getInfo().getIncompleteSemantics().adjustContinueSemanticsForLoopExpressionRemoval();
		// Remove stale edges.
		removed.getInfo().getCFGInfo().clearAllEdges();
		// NestedCFG ncfg = owner.getInfo().getCFGInfo().getNestedCFG();
		// Node elementCond = removed;
		// disconnectAndAdjustEndReachability(ncfg.getBegin(), elementCond);
		// disconnectAndAdjustEndReachability(elementCond, owner.getF4());
		// disconnectAndAdjustEndReachability(elementCond, ncfg.getEnd());
		// disconnectAndAdjustEndReachability(owner.getF4(), elementCond);
	}

	private void updateCFGForPredicateAddition(Expression added) {
		WhileStatement owner = (WhileStatement) this.getOwner();
		NestedCFG ncfg = owner.getInfo().getCFGInfo().getNestedCFG();
		// 1. Add new edges.
		Expression elementCond = added;
		connectAndAdjustEndReachability(ncfg.getBegin(), elementCond);
		connectAndAdjustEndReachability(elementCond, owner.getF4());
		connectAndAdjustEndReachability(elementCond, ncfg.getEnd());
		if (this.getBody().getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(owner.getF4(), elementCond);
		}
		this.getBody().getInfo().getIncompleteSemantics().adjustContinueSemanticsForLoopExpressionAddition();
	}

	private void updateCFGForBodyRemoval(Statement removed) {
		removed = (Statement) Misc.getInternalFirstCFGNode(removed);
		// 1. Adjust incompleteness.
		removed.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerRemoval();
		removed.getInfo().getCFGInfo().clearAllEdges();
		// 2. Remove stale edges.
		// Node elementBody = removed;
		// Node elementCond = this.getPredicate();
		// disconnectAndAdjustEndReachability(elementCond, elementBody);
		// disconnectAndAdjustEndReachability(elementBody, elementCond);
	}

	private void updateCFGForBodyAddition(Statement added) {
		added = (Statement) Misc.getInternalFirstCFGNode(added);
		// 1. Add new edges.
		Node elementBody = added;
		Node elementCond = this.getPredicate();
		connectAndAdjustEndReachability(elementCond, elementBody);
		if (elementBody.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(elementBody, elementCond);
		}

		// 2. Adjust incompleteness
		added.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerAddition();
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
		retList.add(this.getPredicate());
		retList.add(this.getBody());
		retList.add(this.getNestedCFG().getEnd());
		return retList;
	}
}
