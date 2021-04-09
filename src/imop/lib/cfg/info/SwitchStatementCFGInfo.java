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

public class SwitchStatementCFGInfo extends CFGInfo {

	public SwitchStatementCFGInfo(Node owner) {
		super(owner);
	}

	public void setPredicate(Expression predicate) {
		if (predicate == this.getPredicate()) {
			return;
		}
		PointsToAnalysis.disableHeuristic();
		AutomatedUpdater.flushCaches();

		NodeRemover.removeNodeIfConnected(predicate);

		SwitchStatement owner = (SwitchStatement) this.getOwner();
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
		return ((SwitchStatement) getOwner()).getF2();
	}

	public List<SideEffect> setBody(Statement stmt) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		SwitchStatement owner = (SwitchStatement) this.getOwner();
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
		SwitchStatement node = (SwitchStatement) this.getOwner();
		return (Statement) Misc.getInternalFirstCFGNode(node.getF4());
	}
	//
	// public void setCaseDefaultLabelStatementList(List<Statement>
	// caseDefaultLabelList) {
	// this.clearCaseDefaultLabelStatementList();
	// for (Statement stmt : caseDefaultLabelList) {
	// this.addCaseDefaultLabelStatement(stmt);
	// }
	// }

	// public void clearCaseDefaultLabelStatementList() {
	// for (Statement stmt : this.getCaseDefaultLabelStatementList()) {
	// stmt = (Statement) Misc.getInternalFirstCFGNode(stmt);
	// this.removeCaseDefaultLabelStatement(stmt);
	// }
	// }
	//
	// public boolean addCaseDefaultLabelStatement(Statement stmt) {
	// return this.addCaseDefaultLabelStatement(-1, stmt);
	// }
	//
	// /**
	// * Add a new case/default label for this switch statement.
	// * Note: This method ensures that there exists a break statement at the end
	// * of the compound-statement,
	// * before appending it with {@code stmt}
	// *
	// * @param index
	// * @param stmt
	// * @return
	// */
	// public boolean addCaseDefaultLabelStatement(int index, Statement stmt) {
	// /*
	// * Step 1: Ensure that the label for ((SwitchStatement) node) {@code
	// * caseStmt} should not be
	// * present already
	// * in the SwitchStatement.
	// */
	// stmt = (Statement) Misc.getInternalFirstCFGNode(stmt);
	// List<Label> oldLabels = ((SwitchStatement)
	// owner).getInfo().getEnclosedLabels();
	// for (Label label : stmt.getInfo().getLabelAnnotations()) {
	// if (oldLabels.contains(label)) {
	// return false;
	// }
	// }
	// /*
	// * Step 2: Add the element.
	// */
	// CompoundStatement body = (CompoundStatement) this.getBody();
	// List<Node> cfgElements = body.getInfo().getCFGInfo().getElementList();
	// if (!(cfgElements.get(cfgElements.size() - 1) instanceof BreakStatement)) {
	// body.getInfo().getCFGInfo().addElement(CParser.justParse("break;",
	// BreakStatement.class));
	// }
	// body.getInfo().getCFGInfo().addElement(stmt);
	// return true;
	// }
	//
	// public boolean removeCaseDefaultLabelStatement(Statement stmt) {
	// for (Statement existingStmt : this.getCaseDefaultLabelStatementList()) {
	// if (existingStmt == stmt) {
	// CompoundStatement body = (CompoundStatement) this.getBody();
	// body.getInfo().getCFGInfo().removeElement(stmt);
	// return true;
	// }
	// }
	// return false;
	// }
	//
	public boolean hasDefaultStatement() {
		return ((SwitchStatement) this.getOwner()).getInfo().hasDefaultLabel();
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
		// for (Node components : ((SwitchStatementInfo)
		// this.getOwner().getInfo()).getRelevantCFGStatements()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForBodyAddition(Statement added) {
		added = (Statement) Misc.getInternalFirstCFGNode(added);
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// 1. Add new edges.
		Statement body = added;
		if (body.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(body, ncfg.getEnd());
		}

		// 2. Adjust incompleteness
		added.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerAddition();
	}

	private void updateCFGForPredicateRemoval(Expression removed) {
		// Remove stale edges.
		this.getBody().getInfo().getIncompleteSemantics().adjustSemanticsForSwitchPredicateRemoval();
		removed.getInfo().getCFGInfo().clearAllEdges();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
		// for (Node components : ((SwitchStatementInfo)
		// this.getOwner().getInfo()).getRelevantCFGStatements()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForPredicateAddition(Expression added) {
		SwitchStatement owner = (SwitchStatement) this.getOwner();
		NestedCFG ncfg = owner.getInfo().getCFGInfo().getNestedCFG();
		Expression predicate = added;
		connectAndAdjustEndReachability(ncfg.getBegin(), predicate);
		// OLD CODE: This is now taken care of by
		// adjustSemanticsForSwitchPredicateAddition().
		// for (Node caseStmt : owner.getInfo().getRelevantCFGStatements()) {
		// connectAndAdjustEndReachability(predicate, caseStmt);
		// }
		if (!this.hasDefaultStatement()) {
			connectAndAdjustEndReachability(predicate, ncfg.getEnd());
		}
		this.getBody().getInfo().getIncompleteSemantics().adjustSemanticsForSwitchPredicateAddition();
	}
	//
	// private void updateCFGForCaseDefaultLabelStatementRemoval(Statement removed)
	// {
	// NestedCFG ncfg = owner.getInfo().getCFGInfo().getNestedCFG();
	// // 1. Adjust incompleteness.
	// IncompleteSemantics incompleteness =
	// removed.getInfo().getIncompleteSemantics();
	// incompleteness.adjustSemanticsForRemoval((FunctionDefinition)
	// Misc.getEnclosingFunction(removed));
	// // 2. Remove stale edges.
	// for (Node components : this.getAllComponents()) {
	// disconnect(removed, components);
	// disconnect(components, removed);
	// }
	// }
	//
	// private void updateCFGForCaseDefaultLabelStatementInsertion(Statement added)
	// {
	// NestedCFG ncfg = owner.getInfo().getCFGInfo().getNestedCFG();
	// // 1. Add new edges.
	// Expression predicate = this.getPredicate();
	// connect(predicate, added);
	//
	// // 2. Adjust incompleteness
	// IncompleteSemantics incompleteness =
	// added.getInfo().getIncompleteSemantics();
	// if (incompleteness.getIncompleteEdges().size() != 0) {
	// incompleteness.adjustSemanticsForAddition((FunctionDefinition)
	// Misc.getEnclosingFunction(added));
	// }
	// }
	//

	/**
	 * Obtain the various CFG components of the {@code owner} node.
	 * 
	 * @return
	 *         CFG components of the {@code owner} node.
	 */
	@Override
	public List<Node> getAllComponents() {
		// SwitchStatement owner = (SwitchStatement) this.getOwner();
		List<Node> retList = new ArrayList<>();
		retList.add(this.getNestedCFG().getBegin());
		retList.add(this.getPredicate());
		retList.add(this.getBody());
		// retList.addAll(owner.getInfo().getCaseDefaultLabelStatementList());
		retList.add(this.getNestedCFG().getEnd());
		return retList;
	}
}
