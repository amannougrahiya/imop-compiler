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

public class SectionsConstructCFGInfo extends CFGInfo {

	public SectionsConstructCFGInfo(Node owner) {
		super(owner);
	}

	public void setSectionList(List<Statement> newSectionBodies) {
		clearSectionList();
		for (Statement stmt : newSectionBodies) {
			this.addSection(stmt);
		}
	}

	public List<Statement> getSectionList() {
		List<Statement> stmtList = new ArrayList<>();
		for (ASection aSection : Misc.getInheritedEnclosee(((SectionsConstruct) getOwner()).getF4(), ASection.class)) {
			stmtList.add((Statement) Misc.getInternalFirstCFGNode(aSection.getF3()));
		}
		return stmtList;
	}

	public void clearSectionList() {
		NodeListOptional sectionList = ((SectionsConstruct) getOwner()).getF4().getF2();
		while (!sectionList.getNodes().isEmpty()) {
			this.removeSection(((ASection) sectionList.getNodes().get(0)).getF3());
		}
	}

	public boolean removeSection(Statement sectionBody) {
		sectionBody = Misc.getStatementWrapper(sectionBody);
		ASection aSection = Misc.getEnclosingNode(sectionBody, ASection.class);
		if (aSection == null) {
			return false;
		}
		SectionsConstruct sectionsConstruct = Misc.getEnclosingNode(aSection, SectionsConstruct.class);
		if (sectionsConstruct == null || sectionsConstruct != getOwner()) {
			return false;
		}
		AutomatedUpdater.flushCaches();

		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(aSection.getF3());
		NodeListOptional sectionList = ((SectionsConstruct) getOwner()).getF4().getF2();
		Set<Node> rerunNodesForward = AutomatedUpdater.nodesForForwardRerunOnRemoval(aSection.getF3());
		Set<Node> rerunNodesBackward = AutomatedUpdater.nodesForBackwardRerunOnRemoval(aSection.getF3());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(aSection.getF3());
		AutomatedUpdater.updateInformationForRemoval(aSection.getF3());
		this.updateCFGForSectionRemoval(aSection.getF3());
		sectionList.removeNode(aSection);
		AutomatedUpdater.invalidateSymbolsInNode(aSection);
		Program.invalidColumnNum = Program.invalidLineNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after removal is successful.
		// AutomatedUpdater.invalidateSymbolsInNode(aSection);// Added, so that any
		// changes from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
		return true;
	}

	public List<SideEffect> addSection(int index, ASection aSection) {
		return this.addSection(index, aSection.getF3());
	}

	public List<SideEffect> addSection(int index, Statement sectionBody) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		NodeListOptional sectionList = ((SectionsConstruct) getOwner()).getF4().getF2();
		Statement stmt = Misc.getStatementWrapper(sectionBody);
		AutomatedUpdater.flushCaches();

		List<SideEffect> splitSE = SplitCombinedConstructs.splitCombinedConstructForTheStatement(stmt);
		if (!splitSE.isEmpty()) {
			NodeUpdated nodeUpdatedSE = (NodeUpdated) splitSE.get(0);
			// Note: Here we reparse the parallel construct so that we can perform other
			// normalizations within it.
			ParallelConstruct splitParCons = FrontEnd.parseAndNormalize(nodeUpdatedSE.affectedNode.toString(),
					ParallelConstruct.class);
			sideEffectList.add(new NodeUpdated(splitParCons, nodeUpdatedSE.getUpdateMessage()));
			sideEffectList.addAll(this.addSection(index, splitParCons));
			return sideEffectList;
		}

		if (!(stmt.getStmtF0().getChoice() instanceof CompoundStatement)) {
			Statement outSt = FrontEnd.parseAlone("{}", Statement.class);
			CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(outSt);
			sideEffectList = this.addSection(index, compStmt);
			sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(stmt));
			sideEffectList.add(new AddedEnclosingBlock(compStmt));
			return sideEffectList;
		}

		NodeRemover.removeNodeIfConnected(stmt);
		Statement newStmt = ImplicitBarrierRemover.makeBarrierExplicitForNode(stmt, sideEffectList);
		if (newStmt != stmt) {
			sideEffectList.addAll(this.addSection(index, newStmt));
			return sideEffectList;
		}

		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(sectionBody);
		ASection newSection;
		if (stmt.getParent() instanceof ASection) {
			newSection = (ASection) stmt.getParent();
		} else {
			newSection = FrontEnd.parseAlone("#pragma omp section \n {}", ASection.class);
			newSection.setF3(stmt);
		}
		sectionList.addNode(index, newSection);
		AutomatedUpdater.invalidateSymbolsInNode(newSection);
		this.updateCFGForSectionAddition(stmt);

		stmt = Normalization.normalizeLeafNodes(stmt, sideEffectList);

		// this.getOwner().accept(new CompoundStatementEnforcer());// COMMENTED
		// RECENTLY.
		Program.invalidColumnNum = Program.invalidLineNum = true;
		AutomatedUpdater.updateInformationForAddition(stmt);
		// AutomatedUpdater.invalidateSymbolsInNode(newSection);// Added, so that any
		// changes from points-to may be reflected here.
		return sideEffectList;
	}

	public List<SideEffect> addSection(ASection aSection) {
		return this.addSection(aSection.getF3());
	}

	public List<SideEffect> addSection(Statement sectionBody) {
		return addSection(this.getSectionList().size(), sectionBody);
	}

	private void updateCFGForSectionRemoval(Statement removed) {
		removed = (Statement) Misc.getInternalFirstCFGNode(removed);
		// // 1. Adjust incompleteness.
		// removed.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerRemoval();

		// 2. Remove stale edges.
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		NodeListOptional sectionList = ((SectionsConstruct) getOwner()).getF4().getF2();
		if (sectionList.size() == 1) {
			connectAndAdjustEndReachability(ncfg.getBegin(), ncfg.getEnd());
		}
		removed.getInfo().getCFGInfo().clearAllEdges();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForSectionAddition(Statement added) {
		added = (Statement) Misc.getInternalFirstCFGNode(added);
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();

		// 1. Add new edges.
		Statement body = (Statement) Misc.getInternalFirstCFGNode(added);
		connectAndAdjustEndReachability(ncfg.getBegin(), body);
		if (body.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(body, ncfg.getEnd());
		}

		// // 2. Adjust incompleteness
		// added.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerAddition();

		NodeListOptional sectionList = ((SectionsConstruct) getOwner()).getF4().getF2();
		if (sectionList.size() == 1) {
			disconnectAndAdjustEndReachability(ncfg.getBegin(), ncfg.getEnd());
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
		retList.addAll(this.getSectionList());
		retList.add(this.getNestedCFG().getEnd());
		return retList;
	}
}
