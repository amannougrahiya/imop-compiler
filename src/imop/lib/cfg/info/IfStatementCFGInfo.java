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

public class IfStatementCFGInfo extends CFGInfo {

	public IfStatementCFGInfo(Node owner) {
		super(owner);
	}

	public void setPredicate(Expression predicate) {
		if (predicate == this.getPredicate()) {
			return;
		}
		PointsToAnalysis.disableHeuristic();
		AutomatedUpdater.flushCaches();
		IfStatement owner = (IfStatement) this.getOwner();
		NodeRemover.removeNodeIfConnected(predicate);
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

		predicate = Normalization.normalizeLeafNodes(predicate, new ArrayList<>());

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
		return ((IfStatement) getOwner()).getF2();
	}

	public List<SideEffect> setThenBody(Statement stmt) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		IfStatement owner = (IfStatement) this.getOwner();
		stmt = Misc.getStatementWrapper(stmt);
		if (stmt == this.getThenBody()) {
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
			sideEffectList.addAll(this.setThenBody(splitParCons));
			return sideEffectList;
		}

		if (!(stmt.getStmtF0().getChoice() instanceof CompoundStatement)) {
			Statement outSt = FrontEnd.parseAlone("{}", Statement.class);
			CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(outSt);
			sideEffectList = this.setThenBody(compStmt);
			sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(stmt));
			sideEffectList.add(new AddedEnclosingBlock(compStmt));
			return sideEffectList;
		}
		NodeRemover.removeNodeIfConnected(stmt);
		Statement newStmt = ImplicitBarrierRemover.makeBarrierExplicitForNode(stmt, sideEffectList);
		if (newStmt != stmt) {
			sideEffectList.addAll(this.setThenBody(newStmt));
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
		updateCFGForThenBodyRemoval(owner.getF4());
		owner.setF4(stmt);
		AutomatedUpdater.invalidateSymbolsInNode(owner.getF4());
		AutomatedUpdater.invalidateSymbolsInNode(stmt);
		updateCFGForThenBodyAddition(stmt);

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

	public Statement getThenBody() {
		return (Statement) Misc.getInternalFirstCFGNode(((IfStatement) getOwner()).getF4());
	}

	public List<SideEffect> setElseBody(Statement stmt) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		IfStatement owner = (IfStatement) this.getOwner();
		stmt = Misc.getStatementWrapper(stmt);
		if (stmt == this.getElseBody()) {
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
			sideEffectList.addAll(this.setElseBody(splitParCons));
			return sideEffectList;
		}

		if (!(stmt.getStmtF0().getChoice() instanceof CompoundStatement)) {
			Statement outSt = FrontEnd.parseAlone("{}", Statement.class);
			CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(outSt);
			sideEffectList = this.setElseBody(compStmt);
			sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(stmt));
			sideEffectList.add(new AddedEnclosingBlock(compStmt));
			return sideEffectList;
		}
		NodeRemover.removeNodeIfConnected(stmt);
		Statement newStmt = ImplicitBarrierRemover.makeBarrierExplicitForNode(stmt, sideEffectList);
		if (newStmt != stmt) {
			sideEffectList.addAll(this.setElseBody(newStmt));
			return sideEffectList;
		}
		if (owner.getF5().present()) {
			NodeSequence nodeSeq = (NodeSequence) owner.getF5().getNode();
			Statement oldElseBody = (Statement) nodeSeq.getNodes().get(1);

			PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(stmt);
			PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(oldElseBody);
			stmt.setParent(nodeSeq);

			Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(oldElseBody);
			Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(oldElseBody);
			Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
					.updateBPPOrGetAffectedBPPSetUponRemoval(oldElseBody);
			AutomatedUpdater.updateInformationForRemoval(oldElseBody);
			updateCFGForElseBodyRemoval(oldElseBody);
			nodeSeq.removeNode(oldElseBody);
			nodeSeq.addNode(stmt);
			AutomatedUpdater.invalidateSymbolsInNode(stmt);
			updateCFGForElseBodyAddition(stmt);

			stmt = Normalization.normalizeLeafNodes(stmt, sideEffectList);

			// this.getOwner().accept(new CompoundStatementEnforcer());// COMMENTED
			// RECENTLY.
			Program.invalidColumnNum = Program.invalidLineNum = true;
			AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
			AutomatedUpdater.updateInformationForAddition(stmt);
			AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
			// AutomatedUpdater.invalidateSymbolsInNode(stmt);// Added, so that any changes
			// from points-to may be reflected here.
			AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
			return sideEffectList;
		} else {
			PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(stmt);
			NodeSequence nodeSeq = new NodeSequence(2);
			nodeSeq.addNode(new NodeToken("else"));
			nodeSeq.addNode(stmt);
			AutomatedUpdater.invalidateSymbolsInNode(stmt);
			nodeSeq.setParent(owner.getF5()); // Not needed; harmless, though.
			owner.getF5().setNode(nodeSeq);
			updateCFGForElseBodyAddition(stmt);

			stmt = Normalization.normalizeLeafNodes(stmt, sideEffectList);

			// this.getOwner().accept(new CompoundStatementEnforcer());// COMMENTED
			// RECENTLY.
			Program.invalidColumnNum = Program.invalidLineNum = true;
			AutomatedUpdater.updateInformationForAddition(stmt);
			AutomatedUpdater.invalidateSymbolsInNode(stmt);// Added, so that any changes from points-to may be reflected
															// here.
			return sideEffectList;
		}
	}

	public Statement getElseBody() {
		if (((IfStatement) getOwner()).getF5().present()) {
			NodeSequence nodeSeq = (NodeSequence) ((IfStatement) getOwner()).getF5().getNode();
			return (Statement) Misc.getInternalFirstCFGNode(nodeSeq.getNodes().get(1));
		} else {
			return null;
		}
	}

	public boolean hasElseBody() {
		return ((IfStatement) getOwner()).getF5().present();
	}

	public void removeElseBody() {
		AutomatedUpdater.flushCaches();
		IfStatement owner = (IfStatement) this.getOwner();
		// TODO: Why no CFG updates here?!!! The next four lines have been added
		// temporarily. Kindly verify.
		NodeSequence nodeSeq = (NodeSequence) owner.getF5().getNode();
		if (nodeSeq == null) {
			// This implies that there was no else-body.
			return;
		}
		Statement oldElseBody = (Statement) nodeSeq.getNodes().get(1);
		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(oldElseBody);
		Set<Node> rerunNodesForward = AutomatedUpdater.nodesForForwardRerunOnRemoval(oldElseBody);
		Set<Node> rerunNodesBackward = AutomatedUpdater.nodesForBackwardRerunOnRemoval(oldElseBody);
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(oldElseBody);
		AutomatedUpdater.updateInformationForRemoval(oldElseBody);
		updateCFGForElseBodyRemoval(oldElseBody);

		((IfStatement) getOwner()).getF5().setNode(null);
		AutomatedUpdater.invalidateSymbolsInNode(oldElseBody);
		Program.invalidColumnNum = Program.invalidLineNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after removal is successful.
		// AutomatedUpdater.invalidateSymbolsInNode(oldElseBody);// Added, so that any
		// changes from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
	}

	private void updateCFGForPredicateRemoval(Expression removed) {
		// Remove stale edges.
		removed.getInfo().getCFGInfo().clearAllEdges();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForPredicateAddition(Expression added) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// Add new edges.
		Expression predicate = added;
		connectAndAdjustEndReachability(ncfg.getBegin(), predicate);
		connectAndAdjustEndReachability(predicate, this.getThenBody());
		if (!this.hasElseBody()) {
			connectAndAdjustEndReachability(predicate, ncfg.getEnd());
		} else {
			connectAndAdjustEndReachability(predicate, this.getElseBody());
		}
	}

	private void updateCFGForThenBodyRemoval(Statement removed) {
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

	private void updateCFGForThenBodyAddition(Statement added) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// 1. Add new edges.
		Statement body = (Statement) Misc.getInternalFirstCFGNode(added);
		connectAndAdjustEndReachability(this.getPredicate(), body);
		if (body.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(body, ncfg.getEnd());
		}

		// 2. Adjust incompleteness
		body.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerAddition();
	}

	private void updateCFGForElseBodyRemoval(Statement removed) {
		removed = (Statement) Misc.getInternalFirstCFGNode(removed);
		// 1. Adjust incompleteness.
		removed.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerRemoval();

		// 2. Remove stale edges.
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		connectAndAdjustEndReachability(this.getPredicate(), ncfg.getEnd());
		removed.getInfo().getCFGInfo().clearAllEdges();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForElseBodyAddition(Statement added) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// 1. Add new edges.
		Statement body = (Statement) Misc.getInternalFirstCFGNode(added);
		Expression predicate = this.getPredicate();
		connectAndAdjustEndReachability(predicate, body);
		if (body.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(body, ncfg.getEnd());
		}
		disconnectAndAdjustEndReachability(predicate, ncfg.getEnd());

		// 2. Adjust incompleteness
		body.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerAddition();
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
		retList.add(this.getThenBody());
		if (this.hasElseBody()) {
			retList.add(this.getElseBody());
		}
		retList.add(this.getNestedCFG().getEnd());
		return retList;
	}
}
