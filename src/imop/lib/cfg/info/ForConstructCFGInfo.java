/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
//TODO: Verify.
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

public class ForConstructCFGInfo extends CFGInfo {

	public ForConstructCFGInfo(Node owner) {
		super(owner);
	}

	public void setInitExpression(OmpForInitExpression initExpression) {
		if (initExpression == this.getInitExpression()) {
			return;
		}
		PointsToAnalysis.disableHeuristic();
		AutomatedUpdater.flushCaches();
		NodeRemover.removeNodeIfConnected(initExpression);
		ForConstruct owner = (ForConstruct) this.getOwner();
		initExpression.setParent(owner.getF2());

		Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(owner.getF2().getF2());
		Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(owner.getF2().getF2());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF2().getF2());
		AutomatedUpdater.updateInformationForRemoval(owner.getF2().getF2());
		updateCFGForOmpForInitExpressionRemoval(owner.getF2().getF2());
		owner.getF2().setF2(initExpression);
		AutomatedUpdater.invalidateSymbolsInNode(owner.getF2().getF2());
		AutomatedUpdater.invalidateSymbolsInNode(initExpression);
		updateCFGForOmpForInitExpressionAddition(initExpression);

		initExpression = Normalization.normalizeLeafNodes(initExpression, new ArrayList<>());

		Program.invalidColumnNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateInformationForAddition(initExpression);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
		// AutomatedUpdater.invalidateSymbolsInNode(owner.getF2().getF2());// Added, so
		// that any changes from points-to may be reflected here.
		// AutomatedUpdater.invalidateSymbolsInNode(initExpression);// Added, so that
		// any changes from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
	}

	public OmpForInitExpression getInitExpression() {
		return ((ForConstruct) getOwner()).getF2().getF2();
	}

	public void setForConditionExpression(OmpForCondition forCondition) {
		if (forCondition == this.getForConditionExpression()) {
			return;
		}
		PointsToAnalysis.disableHeuristic();
		AutomatedUpdater.flushCaches();
		NodeRemover.removeNodeIfConnected(forCondition);
		ForConstruct owner = (ForConstruct) this.getOwner();
		forCondition.setParent(owner.getF2());

		Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(owner.getF2().getF4());
		Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(owner.getF2().getF4());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF2().getF4());
		AutomatedUpdater.updateInformationForRemoval(owner.getF2().getF4());
		updateCFGForOmpForConditionRemoval(owner.getF2().getF4());
		owner.getF2().setF4(forCondition);
		AutomatedUpdater.invalidateSymbolsInNode(owner.getF2().getF4());
		AutomatedUpdater.invalidateSymbolsInNode(forCondition);
		updateCFGForOmpForConditionAddition(forCondition);

		forCondition = Normalization.normalizeLeafNodes(forCondition, new ArrayList<>());

		Program.invalidColumnNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateInformationForAddition(forCondition);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
		// AutomatedUpdater.invalidateSymbolsInNode(owner.getF2().getF4());// Added, so
		// that any changes from points-to may be reflected here.
		// AutomatedUpdater.invalidateSymbolsInNode(forCondition);// Added, so that any
		// changes from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
	}

	public OmpForCondition getForConditionExpression() {
		return ((ForConstruct) getOwner()).getF2().getF4();
	}

	public void setReinitExpression(OmpForReinitExpression reinitExpression) {
		if (reinitExpression == this.getReinitExpression()) {
			return;
		}
		PointsToAnalysis.disableHeuristic();
		AutomatedUpdater.flushCaches();
		NodeRemover.removeNodeIfConnected(reinitExpression);
		ForConstruct owner = (ForConstruct) this.getOwner();
		reinitExpression.setParent(owner.getF2());

		Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(owner.getF2().getF6());
		Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(owner.getF2().getF6());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF2().getF6());
		AutomatedUpdater.updateInformationForRemoval(owner.getF2().getF6());
		updateCFGForOmpForReinitExpressionRemoval(owner.getF2().getF6());
		owner.getF2().setF6(reinitExpression);
		AutomatedUpdater.invalidateSymbolsInNode(owner.getF2().getF6());
		AutomatedUpdater.invalidateSymbolsInNode(reinitExpression);
		updateCFGForOmpForReinitExpressionAddition(reinitExpression);

		reinitExpression = Normalization.normalizeLeafNodes(reinitExpression, new ArrayList<>());

		Program.invalidColumnNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateInformationForAddition(reinitExpression);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
		// AutomatedUpdater.invalidateSymbolsInNode(owner.getF2().getF6());// Added, so
		// that any changes from points-to may be reflected here.
		// AutomatedUpdater.invalidateSymbolsInNode(reinitExpression);// Added, so that
		// any changes from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
	}

	public OmpForReinitExpression getReinitExpression() {
		return ((ForConstruct) getOwner()).getF2().getF6();
	}

	public List<SideEffect> setBody(Statement stmt) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		ForConstruct owner = (ForConstruct) this.getOwner();
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
		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(owner.getF3());
		stmt.setParent(owner);

		Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(owner.getF3());
		Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(owner.getF3());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF3());
		AutomatedUpdater.updateInformationForRemoval(owner.getF3());
		updateCFGForBodyRemoval(owner.getF3());
		owner.setF3(stmt);
		AutomatedUpdater.invalidateSymbolsInNode(owner.getF3());
		AutomatedUpdater.invalidateSymbolsInNode(stmt);
		updateCFGForBodyAddition(stmt);

		stmt = Normalization.normalizeLeafNodes(stmt, sideEffectList);

		// this.getOwner().accept(new CompoundStatementEnforcer()); // COMMENTED
		// RECENTLY.
		Program.invalidColumnNum = Program.invalidLineNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateInformationForAddition(stmt);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
		// AutomatedUpdater.invalidateSymbolsInNode(owner.getF3());// Added, so that any
		// changes from points-to may be reflected here.
		// AutomatedUpdater.invalidateSymbolsInNode(stmt);// Added, so that any changes
		// from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
		return sideEffectList;
	}

	@Override
	public Statement getBody() {
		return (Statement) Misc.getInternalFirstCFGNode(((ForConstruct) getOwner()).getF3());
	}

	private void updateCFGForOmpForInitExpressionRemoval(OmpForInitExpression removed) {
		// Remove stale edges.
		removed.getInfo().getCFGInfo().clearAllEdges();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForOmpForInitExpressionAddition(OmpForInitExpression added) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// 1. Add new edges.
		connectAndAdjustEndReachability(ncfg.getBegin(), added);
		connectAndAdjustEndReachability(added, this.getForConditionExpression());
	}

	private void updateCFGForOmpForConditionRemoval(OmpForCondition removed) {
		// Remove stale edges.
		removed.getInfo().getCFGInfo().clearAllEdges();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForOmpForConditionAddition(OmpForCondition added) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// 1. Add new edges.
		connectAndAdjustEndReachability(this.getInitExpression(), added);
		connectAndAdjustEndReachability(this.getReinitExpression(), added);
		connectAndAdjustEndReachability(added, ncfg.getEnd());
		connectAndAdjustEndReachability(added, this.getBody());
	}

	private void updateCFGForOmpForReinitExpressionRemoval(OmpForReinitExpression removed) {
		removed.getInfo().getCFGInfo().clearAllEdges();
		// this.getBody().getInfo().getIncompleteSemantics().adjustContinueSemanticsForLoopExpressionRemoval();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForOmpForReinitExpressionAddition(OmpForReinitExpression added) {
		// 1. Add new edges.
		connectAndAdjustEndReachability(added, this.getForConditionExpression());
		Statement body = this.getBody();
		if (body.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(body, added);
		}
		this.getBody().getInfo().getIncompleteSemantics().adjustContinueSemanticsForLoopExpressionAddition();
	}

	private void updateCFGForBodyRemoval(Statement removed) {
		removed = (Statement) Misc.getInternalFirstCFGNode(removed);
		// 1. Adjust incompleteness.
		removed.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerRemoval();
		// 2. Remove stale edges.
		removed.getInfo().getCFGInfo().clearAllEdges();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForBodyAddition(Statement added) {
		added = (Statement) Misc.getInternalFirstCFGNode(added);
		// 1. Add new edges.
		connectAndAdjustEndReachability(this.getForConditionExpression(), added);
		if (added.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(added, this.getReinitExpression());
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
		retList.add(this.getBody());
		retList.add(this.getInitExpression());
		retList.add(this.getForConditionExpression());
		retList.add(this.getReinitExpression());
		retList.add(this.getNestedCFG().getEnd());
		return retList;
	}
}
