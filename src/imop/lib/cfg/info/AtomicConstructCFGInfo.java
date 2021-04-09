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
import imop.lib.transform.updater.NodeRemover;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AtomicConstructCFGInfo extends CFGInfo {

	public AtomicConstructCFGInfo(Node owner) {
		super(owner);
	}

	public void setBody(Statement expStmt) {
		expStmt = Misc.getStatementWrapper(expStmt);
		if (expStmt == this.getStatement()) {
			return;
		}
		AutomatedUpdater.flushCaches();
		NodeRemover.removeNodeIfConnected(expStmt);
		AtomicConstruct atomicOwner = (AtomicConstruct) getOwner();

		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(expStmt);
		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(atomicOwner.getF4());
		expStmt.setParent(atomicOwner);

		Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(atomicOwner.getF4());
		Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(atomicOwner.getF4());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(atomicOwner.getF4());
		AutomatedUpdater.updateInformationForRemoval(atomicOwner.getF4());
		updateCFGForBodyRemoval(atomicOwner.getF4());
		atomicOwner.setF4(expStmt);
		AutomatedUpdater.invalidateSymbolsInNode(atomicOwner.getF4());
		AutomatedUpdater.invalidateSymbolsInNode(expStmt);
		updateCFGForBodyAddition(expStmt);

		// NOTE: For this non-leaf node, no modifications are required in internal leaf
		// nodes.
		// expStmt = Normalization.normalizeLeafNodes(expStmt, new ArrayList<>());

		invalidatePositionForStatementAddition(expStmt);
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateInformationForAddition(expStmt);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
		// AutomatedUpdater.invalidateSymbolsInNode(atomicOwner.getF4());// Added, so
		// that any changes from points-to may be reflected here.
		// AutomatedUpdater.invalidateSymbolsInNode(expStmt);// Added, so that any
		// changes from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
	}

	public Statement getStatement() {
		return (Statement) Misc.getInternalFirstCFGNode(((AtomicConstruct) getOwner()).getF4());
	}

	private void updateCFGForBodyRemoval(Statement removed) {
		removed = (Statement) Misc.getInternalFirstCFGNode(removed);
		// // 1. Adjust incompleteness.
		// removed.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerRemoval();

		// 2. Remove stale edges.
		removed.getInfo().getCFGInfo().clearAllEdges();
	}

	private void updateCFGForBodyAddition(Statement added) {
		added = (Statement) Misc.getInternalFirstCFGNode(added);
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();

		// 1. Add new CFG edges.
		connectAndAdjustEndReachability(ncfg.getBegin(), added);
		if (added.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(added, ncfg.getEnd());
		}

		// // 2. Adjust incompleteness.
		// added.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerAddition();
	}

	public void invalidatePositionForStatementAddition(Statement expStmt) {
		if (expStmt.toString().contains("\n")) {
			Program.invalidLineNum = true;
		}
		Program.invalidColumnNum = true;
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
		retList.add(this.getStatement());
		retList.add(this.getNestedCFG().getEnd());
		return retList;
	}
}
