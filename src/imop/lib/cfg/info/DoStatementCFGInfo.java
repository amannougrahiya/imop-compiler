/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import imop.ast.node.external.CompoundStatement;
import imop.ast.node.external.DoStatement;
import imop.ast.node.external.Expression;
import imop.ast.node.external.Node;
import imop.ast.node.external.ParallelConstruct;
import imop.ast.node.external.Statement;
import imop.lib.analysis.mhp.BeginPhasePoint;
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

public class DoStatementCFGInfo extends CFGInfo {

	public DoStatementCFGInfo(Node owner) {
		super(owner);
	}

	public void setPredicate(Expression predicate) {
		if (predicate == this.getPredicate()) {
			return;
		}
		AutomatedUpdater.flushCaches();
		NodeRemover.removeNodeIfConnected(predicate);
		DoStatement owner = (DoStatement) this.getOwner();
		predicate.setParent(owner);

		Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(owner.getF4());
		Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(owner.getF4());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF4());
		AutomatedUpdater.updateInformationForRemoval(owner.getF4());
		updateCFGForPredicateRemoval(owner.getF4());
		owner.setF4(predicate);
		AutomatedUpdater.invalidateSymbolsInNode(owner.getF4());
		AutomatedUpdater.invalidateSymbolsInNode(predicate);
		updateCFGForPredicateAddition(predicate);
		
		predicate = Normalization.normalizeLeafNodes(predicate, new ArrayList<>());
		
		Program.invalidColumnNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateInformationForAddition(predicate);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
		//		AutomatedUpdater.invalidateSymbolsInNode(owner.getF4());// Added, so that any changes from points-to may be reflected here.
		//		AutomatedUpdater.invalidateSymbolsInNode(predicate);// Added, so that any changes from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
	}

	public Expression getPredicate() {
		return ((DoStatement) getOwner()).getF4();
	}

	public List<SideEffect> setBody(Statement stmt) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		DoStatement owner = (DoStatement) this.getOwner();
		stmt = Misc.getStatementWrapper(stmt);
		if (stmt == this.getBody()) {
			return sideEffectList;
		}
		AutomatedUpdater.flushCaches();

		List<SideEffect> splitSE = SplitCombinedConstructs.splitCombinedConstructForTheStatement(stmt);
		if (!splitSE.isEmpty()) {
			NodeUpdated nodeUpdatedSE = (NodeUpdated) splitSE.get(0);
			// Note: Here we reparse the parallel construct so that we can perform other normalizations within it.
			ParallelConstruct splitParCons = FrontEnd.parseAndNormalize(nodeUpdatedSE.affectedNode.toString(), ParallelConstruct.class);
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

		stmt.setParent(owner);

		Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(owner.getF1());
		Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(owner.getF1());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF1());
		AutomatedUpdater.updateInformationForRemoval(owner.getF1());
		updateCFGForBodyRemoval(owner.getF1());
		owner.setF1(stmt);
		AutomatedUpdater.invalidateSymbolsInNode(owner.getF1());
		AutomatedUpdater.invalidateSymbolsInNode(stmt);
		updateCFGForBodyAddition(stmt);

		stmt = Normalization.normalizeLeafNodes(stmt, sideEffectList);

		//		this.getOwner().accept(new CompoundStatementEnforcer()); // COMMENTED RECENTLY.
		Program.invalidColumnNum = Program.invalidLineNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateInformationForAddition(stmt);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
		//		AutomatedUpdater.invalidateSymbolsInNode(owner.getF1());// Added, so that any changes from points-to may be reflected here.
		//		AutomatedUpdater.invalidateSymbolsInNode(stmt);// Added, so that any changes from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
		return sideEffectList;
	}

	@Override
	public Statement getBody() {
		return (Statement) Misc.getInternalFirstCFGNode(((DoStatement) getOwner()).getF1());
	}

	private void updateCFGForBodyRemoval(Statement removed) {
		removed = (Statement) Misc.getInternalFirstCFGNode(removed);
		// 1. Adjust incompleteness.
		removed.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerRemoval();
		removed.getInfo().getCFGInfo().clearAllEdges();
		//		// 2. Remove stale edges.
		//		for (Node components : this.getAllComponents()) {
		//			disconnectAndAdjustEndReachability(removed, components);
		//			disconnectAndAdjustEndReachability(components, removed);
		//		}
	}

	private void updateCFGForBodyAddition(Statement added) {
		added = (Statement) Misc.getInternalFirstCFGNode(added);
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// 1. Add new edges.
		Statement body = (Statement) Misc.getInternalFirstCFGNode(added);
		connectAndAdjustEndReachability(ncfg.getBegin(), body);
		if (body.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(body, this.getPredicate());
		}
		connectAndAdjustEndReachability(this.getPredicate(), body);

		// 2. Adjust incompleteness
		added.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerAddition();
	}

	private void updateCFGForPredicateRemoval(Expression removed) {
		// Remove stale edges.
		this.getBody().getInfo().getIncompleteSemantics().adjustContinueSemanticsForLoopExpressionRemoval();
		removed.getInfo().getCFGInfo().clearAllEdges();
		//		for (Node components : this.getAllComponents()) {
		//			disconnectAndAdjustEndReachability(removed, components);
		//			disconnectAndAdjustEndReachability(components, removed);
		//		}
	}

	private void updateCFGForPredicateAddition(Expression added) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// Add new edges.
		Statement body = this.getBody(); //(Statement) Misc.getInternalFirstCFGNode(added);
		Expression predicate = added;
		connectAndAdjustEndReachability(predicate, body);
		if (body.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(body, predicate);
		}
		connectAndAdjustEndReachability(predicate, ncfg.getEnd());
		this.getBody().getInfo().getIncompleteSemantics().adjustContinueSemanticsForLoopExpressionAddition();
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
		retList.add(this.getPredicate());
		retList.add(this.getNestedCFG().getEnd());
		return retList;
	}
}
