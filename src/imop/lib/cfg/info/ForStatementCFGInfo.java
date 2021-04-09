/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
// TODO: Verify.
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

public class ForStatementCFGInfo extends CFGInfo {

	public ForStatementCFGInfo(Node owner) {
		super(owner);
	}

	public void setInitExpression(Expression e1) {
		if (e1 == this.getInitExpression()) {
			return;
		}
		PointsToAnalysis.disableHeuristic();
		removeInitExpression();
		NodeRemover.removeNodeIfConnected(e1);
		ForStatement owner = (ForStatement) this.getOwner();
		NodeOptional nodeOptional = new NodeOptional(e1);
		nodeOptional.setParent((owner));

		owner.setF2(nodeOptional);
		AutomatedUpdater.invalidateSymbolsInNode(nodeOptional);
		updateCFGForInitExpressionAddition(e1);

		e1 = Normalization.normalizeLeafNodes(e1, new ArrayList<>());

		Program.invalidColumnNum = true;
		AutomatedUpdater.updateInformationForAddition(e1);
		// AutomatedUpdater.invalidateSymbolsInNode(nodeOptional);// Added, so that any
		// changes from points-to may be reflected here.
	}

	public Expression getInitExpression() {
		return (Expression) ((ForStatement) getOwner()).getF2().getNode();
	}

	public boolean hasInitExpression() {
		return ((ForStatement) getOwner()).getF2().present();
	}

	public void removeInitExpression() {
		ForStatement owner = (ForStatement) this.getOwner();
		if (owner.getF2().present()) {
			PointsToAnalysis.disableHeuristic();
			AutomatedUpdater.flushCaches();
			Set<Node> rerunNodesForward = AutomatedUpdater.nodesForForwardRerunOnRemoval(owner.getF2().getNode());
			Set<Node> rerunNodesBackward = AutomatedUpdater.nodesForBackwardRerunOnRemoval(owner.getF2().getNode());
			Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
					.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF2().getNode());
			AutomatedUpdater.updateInformationForRemoval(owner.getF2().getNode());
			updateCFGForInitExpressionRemoval((Expression) owner.getF2().getNode());
			Program.invalidColumnNum = true;
			Expression oldInit = (Expression) owner.getF2().getNode();
			owner.getF2().setNode(null);
			AutomatedUpdater.invalidateSymbolsInNode(oldInit);
			AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
			AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after removal is successful.
			// AutomatedUpdater.invalidateSymbolsInNode(oldInit);// Added, so that any
			// changes from points-to may be reflected here.
			AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
		}
	}

	public void setTerminationExpression(Expression e2) {
		if (e2 == this.getTerminationExpression()) {
			return;
		}
		PointsToAnalysis.disableHeuristic();
		removeTerminationExpression();
		NodeRemover.removeNodeIfConnected(e2);
		ForStatement owner = (ForStatement) this.getOwner();
		NodeOptional nodeOptional = new NodeOptional(e2);
		nodeOptional.setParent((owner));

		owner.setF4(nodeOptional);
		AutomatedUpdater.invalidateSymbolsInNode(nodeOptional);
		updateCFGForTerminationExpressionAddition(e2);

		e2 = Normalization.normalizeLeafNodes(e2, new ArrayList<>());

		Program.invalidColumnNum = true;
		AutomatedUpdater.updateInformationForAddition(e2);
		// AutomatedUpdater.invalidateSymbolsInNode(nodeOptional);// Added, so that any
		// changes from points-to may be reflected here.
	}

	public Expression getTerminationExpression() {
		return (Expression) ((ForStatement) getOwner()).getF4().getNode();
	}

	public boolean hasTerminationExpression() {
		return ((ForStatement) getOwner()).getF4().present();
	}

	public void removeTerminationExpression() {
		ForStatement owner = (ForStatement) this.getOwner();
		if (owner.getF4().present()) {
			PointsToAnalysis.disableHeuristic();
			AutomatedUpdater.flushCaches();
			Set<Node> rerunNodesForward = AutomatedUpdater.nodesForForwardRerunOnRemoval(owner.getF4().getNode());
			Set<Node> rerunNodesBackward = AutomatedUpdater.nodesForBackwardRerunOnRemoval(owner.getF4().getNode());
			Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
					.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF4().getNode());
			AutomatedUpdater.updateInformationForRemoval(owner.getF4().getNode());
			updateCFGForTerminationExpressionRemoval((Expression) owner.getF4().getNode());
			Program.invalidColumnNum = true;
			Expression oldTermExp = (Expression) owner.getF4().getNode();
			owner.getF4().setNode(null);
			AutomatedUpdater.invalidateSymbolsInNode(oldTermExp);
			AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
			AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after removal is successful.
			// AutomatedUpdater.invalidateSymbolsInNode(oldTermExp);// Added, so that any
			// changes from points-to may be reflected here.
			AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
		}
	}

	public void setStepExpression(Expression e3) {
		if (e3 == this.getStepExpression()) {
			return;
		}
		PointsToAnalysis.disableHeuristic();
		removeStepExpression();
		NodeRemover.removeNodeIfConnected(e3);
		ForStatement owner = (ForStatement) this.getOwner();
		NodeOptional nodeOptional = new NodeOptional(e3);
		nodeOptional.setParent((owner));

		owner.setF6(nodeOptional);
		AutomatedUpdater.invalidateSymbolsInNode(nodeOptional);
		updateCFGForStepExpressionAddition(e3);

		e3 = Normalization.normalizeLeafNodes(e3, new ArrayList<>());

		Program.invalidColumnNum = true;
		AutomatedUpdater.updateInformationForAddition(e3);
		// AutomatedUpdater.invalidateSymbolsInNode(nodeOptional);// Added, so that any
		// changes from points-to may be reflected here.
	}

	public Expression getStepExpression() {
		return (Expression) ((ForStatement) getOwner()).getF6().getNode();
	}

	public boolean hasStepExpression() {
		return ((ForStatement) getOwner()).getF6().present();
	}

	public void removeStepExpression() {
		PointsToAnalysis.disableHeuristic();
		ForStatement owner = (ForStatement) this.getOwner();
		if (owner.getF6().present()) {
			AutomatedUpdater.flushCaches();
			Set<Node> rerunNodesForward = AutomatedUpdater.nodesForForwardRerunOnRemoval(owner.getF6().getNode());
			Set<Node> rerunNodesBackward = AutomatedUpdater.nodesForBackwardRerunOnRemoval(owner.getF6().getNode());
			Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
					.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF6().getNode());
			AutomatedUpdater.updateInformationForRemoval(owner.getF6().getNode());
			updateCFGForStepExpressionRemoval((Expression) owner.getF6().getNode());
			Program.invalidColumnNum = true;
			Expression oldNode = (Expression) owner.getF6().getNode();
			owner.getF6().setNode(null);
			AutomatedUpdater.invalidateSymbolsInNode(oldNode);
			AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
			AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after removal is successful.
			// AutomatedUpdater.invalidateSymbolsInNode(oldNode);// Added, so that any
			// changes from points-to may be reflected here.
			AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
		}
	}

	public List<SideEffect> setBody(Statement stmt) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		ForStatement owner = (ForStatement) this.getOwner();
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
		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(owner.getF8());
		stmt.setParent(owner);

		Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(owner.getF8());
		Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(owner.getF8());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF8());
		AutomatedUpdater.updateInformationForRemoval(owner.getF8());
		updateCFGForBodyRemoval(owner.getF8());
		owner.setF8(stmt);
		AutomatedUpdater.invalidateSymbolsInNode(owner.getF8());
		AutomatedUpdater.invalidateSymbolsInNode(stmt);
		updateCFGForBodyAddition(stmt);

		stmt = Normalization.normalizeLeafNodes(stmt, sideEffectList);

		// this.getOwner().accept(new CompoundStatementEnforcer());// COMMENTED
		// RECENTLY.
		Program.invalidColumnNum = Program.invalidLineNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateInformationForAddition(stmt);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
		// AutomatedUpdater.invalidateSymbolsInNode(owner.getF8());// Added, so that any
		// changes from points-to may be reflected here.
		// AutomatedUpdater.invalidateSymbolsInNode(stmt);// Added, so that any changes
		// from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
		return sideEffectList;
	}

	@Override
	public Statement getBody() {
		return (Statement) Misc.getInternalFirstCFGNode(((ForStatement) getOwner()).getF8());
	}

	private void updateCFGForInitExpressionRemoval(Expression removed) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// Remove stale edges.
		if (this.hasTerminationExpression()) {
			connectAndAdjustEndReachability(ncfg.getBegin(), this.getTerminationExpression());
		} else {
			connectAndAdjustEndReachability(ncfg.getBegin(), this.getBody());
		}
		removed.getInfo().getCFGInfo().clearAllEdges();
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForInitExpressionAddition(Expression added) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// Add new edges.
		connectAndAdjustEndReachability(ncfg.getBegin(), added);
		if (this.hasTerminationExpression()) {
			connectAndAdjustEndReachability(added, this.getTerminationExpression());
			disconnectAndAdjustEndReachability(ncfg.getBegin(), this.getTerminationExpression());
		} else {
			connectAndAdjustEndReachability(added, this.getBody());
			disconnectAndAdjustEndReachability(ncfg.getBegin(), this.getBody());
		}
	}

	private void updateCFGForTerminationExpressionRemoval(Expression removed) {
		// Remove stale edges.
		List<Node> prevNodeList = new ArrayList<>(removed.getInfo().getCFGInfo().getPredBlocks());
		List<Node> nextNodeList = new ArrayList<>(removed.getInfo().getCFGInfo().getSuccBlocks());
		for (Node prevNode : prevNodeList) {
			for (Node nextNode : nextNodeList) {
				if (nextNode instanceof EndNode) {
					continue;
				}
				connectAndAdjustEndReachability(prevNode, nextNode);
			}
		}
		removed.getInfo().getCFGInfo().clearAllEdges();
	}

	private void updateCFGForTerminationExpressionAddition(Expression added) {
		added = (Expression) Misc.getInternalFirstCFGNode(added);
		CompoundStatement loopBody = (CompoundStatement) this.getBody();
		connectAndAdjustEndReachability(added, loopBody);
		connectAndAdjustEndReachability(added, this.getNestedCFG().getEnd());
		if (this.hasInitExpression()) {
			Expression initExpression = this.getInitExpression();
			connectAndAdjustEndReachability(initExpression, added);
			disconnectAndAdjustEndReachability(initExpression, loopBody);
		} else {
			BeginNode beginNode = this.getNestedCFG().getBegin();
			connectAndAdjustEndReachability(beginNode, added);
			disconnectAndAdjustEndReachability(beginNode, loopBody);
		}
		if (this.hasStepExpression()) {
			Expression stepExpression = this.getStepExpression();
			connectAndAdjustEndReachability(stepExpression, added);
			disconnectAndAdjustEndReachability(stepExpression, loopBody);
			return;
		} else {
			Set<Node> bodyNodes = loopBody.getInfo().getCFGInfo().getLexicalCFGContents();
			Statement outerLoop = (Statement) this.getOwner();
			for (Node bodyNode : bodyNodes) {
				if (!(bodyNode instanceof ContinueStatement)) {
					continue;
				}
				ContinueStatement contStmt = (ContinueStatement) bodyNode;
				Statement loopStmt = (Statement) Misc.getEnclosingLoopOrForConstruct(contStmt);
				if (loopStmt != outerLoop) {
					continue;
				}
				connectAndAdjustEndReachability(contStmt, added);
				disconnectAndAdjustEndReachability(contStmt, loopBody);
			}
			if (loopBody.getInfo().getCFGInfo().isEndReachable()) {
				connectAndAdjustEndReachability(loopBody, added);
				disconnectAndAdjustEndReachability(loopBody, loopBody);
			}
			return;
		}
	}

	private void updateCFGForStepExpressionRemoval(Expression removed) {
		// Remove stale edges.
		List<Node> prevNodeList = new ArrayList<>(removed.getInfo().getCFGInfo().getPredBlocks());
		List<Node> nextNodeList = new ArrayList<>(removed.getInfo().getCFGInfo().getSuccBlocks());
		for (Node prevNode : prevNodeList) {
			for (Node nextNode : nextNodeList) {
				connectAndAdjustEndReachability(prevNode, nextNode);
				// disconnectAndAdjustEndReachability(prevNode, removed);
				// disconnectAndAdjustEndReachability(removed, nextNode);
			}
		}
		removed.getInfo().getCFGInfo().clearAllEdges();
	}

	private void updateCFGForStepExpressionAddition(Expression added) {
		// Add new edges.
		added = (Expression) Misc.getInternalFirstCFGNode(added);
		if (this.hasTerminationExpression()) {
			Expression termExpression = this.getTerminationExpression();
			List<Node> termPredsList = new ArrayList<>(termExpression.getInfo().getCFGInfo().getPredBlocks());
			connectAndAdjustEndReachability(added, termExpression);
			for (Node termPred : termPredsList) {
				if ((this.hasInitExpression() && termPred == this.getInitExpression())
						|| termPred instanceof BeginNode) {
					continue;
				}
				connectAndAdjustEndReachability(termPred, added);
				disconnectAndAdjustEndReachability(termPred, termExpression);
			}
			return;
		} else {
			Statement loopBody = this.getBody();
			List<Node> bodyPredsList = new ArrayList<>(loopBody.getInfo().getCFGInfo().getPredBlocks());
			connectAndAdjustEndReachability(added, loopBody);
			for (Node bodyPred : bodyPredsList) {
				if ((this.hasInitExpression() && bodyPred == this.getInitExpression())
						|| bodyPred instanceof BeginNode) {
					continue;
				}
				connectAndAdjustEndReachability(bodyPred, added);
				disconnectAndAdjustEndReachability(bodyPred, loopBody);
			}
			return;
		}

		// Old code:
		// Statement body = this.getBody();
		// Node prevNode;
		// Node nextNode;
		// if (body.getInfo().getCFGInfo().isEndReachable()) {
		// prevNode = body;
		// } else {
		// prevNode = null;
		// }
		// if (this.hasTerminationExpression()) {
		// nextNode = this.getTerminationExpression();
		// } else {
		// nextNode = body;
		// }
		//
		// disconnect(prevNode, nextNode);
		// connect(prevNode, added);
		// connect(added, nextNode);
	}

	private void updateCFGForBodyRemoval(Statement removed) {
		removed = (Statement) Misc.getInternalFirstCFGNode(removed);
		// 1. Adjust incompleteness.
		removed.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerRemoval();
		// 2. Remove stale edges.
		removed.getInfo().getCFGInfo().clearAllEdges();
		// Note that this is an intermediate state; we don't need to connect successors
		// to predecessors.
		// for (Node components : this.getAllComponents()) {
		// disconnectAndAdjustEndReachability(removed, components);
		// disconnectAndAdjustEndReachability(components, removed);
		// }
	}

	private void updateCFGForBodyAddition(Statement added) {
		added = (Statement) Misc.getInternalFirstCFGNode(added);
		Statement body = added;
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// 1. Add new edges.
		if (this.hasInitExpression()) {
			if (this.hasTerminationExpression()) {
				connectAndAdjustEndReachability(this.getTerminationExpression(), body);
				if (this.hasStepExpression()) { // E1 E2 E3
					if (body.getInfo().getCFGInfo().isEndReachable()) {
						connectAndAdjustEndReachability(body, this.getStepExpression());
					}
				} else { // E1 E2 -
					if (body.getInfo().getCFGInfo().isEndReachable()) {
						connectAndAdjustEndReachability(body, this.getTerminationExpression());
					}
				}
			} else {
				connectAndAdjustEndReachability(this.getInitExpression(), body);
				if (this.hasStepExpression()) { // E1 - E3
					if (body.getInfo().getCFGInfo().isEndReachable()) {
						connectAndAdjustEndReachability(body, this.getStepExpression());
					}
					connectAndAdjustEndReachability(this.getStepExpression(), body);
				} else { // E1 - -
					// Note: Here, we need to alter the continue-edges as well.
					if (body.getInfo().getCFGInfo().isEndReachable()) {
						connectAndAdjustEndReachability(body, body);
					}
					body.getInfo().getIncompleteSemantics().adjustContinueSemanticsForLoopExpressionAddition();
				}
			}
		} else {
			if (this.hasTerminationExpression()) {
				connectAndAdjustEndReachability(this.getTerminationExpression(), body);
				if (this.hasStepExpression()) { // - E2 E3
					if (body.getInfo().getCFGInfo().isEndReachable()) {
						connectAndAdjustEndReachability(body, this.getStepExpression());
					}
				} else { // - E2 -
					if (body.getInfo().getCFGInfo().isEndReachable()) {
						connectAndAdjustEndReachability(body, this.getTerminationExpression());
					}
				}
			} else {
				connectAndAdjustEndReachability(ncfg.getBegin(), body);
				if (this.hasStepExpression()) { // - - E3
					if (body.getInfo().getCFGInfo().isEndReachable()) {
						connectAndAdjustEndReachability(body, this.getStepExpression());
						connectAndAdjustEndReachability(this.getStepExpression(), body);
					}
				} else { // - - -
					if (body.getInfo().getCFGInfo().isEndReachable()) {
						connectAndAdjustEndReachability(body, body);
						// Note: Here, we need to alter the continue-edges as well.
						body.getInfo().getIncompleteSemantics().adjustContinueSemanticsForLoopExpressionAddition();
					}
				}
			}
		}
		// Handle the incomplete edges.
		added.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerAddition();

		// OLD CODE:
		// if (this.hasTerminationExpression()) {
		// prevNode = this.getTerminationExpression();
		// } else {
		// if (this.hasInitExpression()) {
		// prevNode = this.getInitExpression();
		// } else {
		// prevNode = ncfg.getBegin();
		// }
		// }
		// if (this.hasStepExpression()) {
		// nextNode = this.getStepExpression();
		// } else {
		// if (this.hasTerminationExpression()) {
		// nextNode = this.getTerminationExpression();
		// } else {
		// nextNode = added;
		// }
		// }
		// connectAndAdjustEndReachability(prevNode, added);
		// if (added.getInfo().getCFGInfo().isEndReachable()) {
		// connectAndAdjustEndReachability(added, nextNode);
		// }
		// connectAndAdjustEndReachability(nextNode, added);
		// disconnectAndAdjustEndReachability(prevNode, nextNode);

		// 2. Adjust incompleteness
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
		if (this.hasInitExpression()) {
			retList.add(this.getInitExpression());
		}
		if (this.hasTerminationExpression()) {
			retList.add(this.getTerminationExpression());
		}
		if (this.hasStepExpression()) {
			retList.add(this.getStepExpression());
		}
		retList.add(this.getBody());
		retList.add(this.getNestedCFG().getEnd());
		return retList;
	}

}
