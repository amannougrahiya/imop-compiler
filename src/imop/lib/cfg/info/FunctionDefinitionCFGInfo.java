/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
// TODO: Verify
package imop.lib.cfg.info;

import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.cfg.NestedCFG;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.transform.simplify.Normalization;
import imop.lib.transform.updater.NodeRemover;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FunctionDefinitionCFGInfo extends CFGInfo {

	public FunctionDefinitionCFGInfo(Node owner) {
		super(owner);
	}

	@Deprecated
	public void setParameterDeclarationList(List<ParameterDeclaration> parameterDeclarations) {
		AutomatedUpdater.flushCaches();
		this.clearParameterDeclarationList();
		for (ParameterDeclaration paramDecl : parameterDeclarations) {
			this.addParameterDeclaration(paramDecl);
		}

		// OLD Code
		// FunctionDefinition owner = (FunctionDefinition) this.owner;
		// ParameterTypeListClosed paramTypeListClosed = (ParameterTypeListClosed) Misc
		// .getSingleton(Misc.getInheritedEnclosee(owner.f1,
		// ParameterTypeListClosed.class));
		//
		// NodeListOptional newNodeListOptional = new NodeListOptional();
		// for (ParameterDeclaration newParam : parameterDeclarations) {
		// int index = parameterDeclarations.indexOf(newParam);
		// if (index != 0) {
		// NodeSequence nodeSeq = new NodeSequence(2);
		// nodeSeq.addNode(new NodeToken(","));
		// nodeSeq.addNode(newParam);
		// newNodeListOptional.addNode(nodeSeq);
		// }
		// }
		// ParameterList paramList = new ParameterList(parameterDeclarations.get(0),
		// newNodeListOptional);
		// ParameterTypeList newParameterTypeList = new ParameterTypeList(paramList, new
		// NodeOptional());
		// newParameterTypeList.parent = paramTypeListClosed.f1;
		// paramTypeListClosed.f1.node = newParameterTypeList;
	}

	public List<ParameterDeclaration> getParameterDeclarationList() {
		List<ParameterDeclaration> paramDeclList = new ArrayList<>();
		ParameterList paramList = Misc.getSingleton(
				Misc.getInheritedEnclosee(((FunctionDefinition) getOwner()).getF1(), ParameterList.class));
		if (paramList != null) {
			paramDeclList.add(paramList.getF0());
			for (Node paramDeclNodeSeq : paramList.getF1().getNodes()) {
				paramDeclList.add((ParameterDeclaration) ((NodeSequence) paramDeclNodeSeq).getNodes().get(1));
			}
		}
		return paramDeclList;
	}

	@Deprecated
	public void clearParameterDeclarationList() {
		FunctionDefinition node = (FunctionDefinition) this.getOwner();
		ParameterTypeListClosed paramTypeListClosed = Misc
				.getSingleton(Misc.getInheritedEnclosee(node.getF1(), ParameterTypeListClosed.class));
		ParameterTypeList paramTypeList = (ParameterTypeList) paramTypeListClosed.getF1().getNode();
		ParameterList paramList = paramTypeList.getF0();
		while (paramList.getF1().size() != 0) {
			this.removeParameterDeclaration(
					(ParameterDeclaration) ((NodeSequence) paramList.getF1().getNodes().get(0)).getNodes().get(1));
		}
		this.removeParameterDeclaration(paramList.getF0());
	}

	/**
	 * Removes {@code paramDecl} from the list of parameters of the
	 * corresponding FunctionDefinition.
	 * 
	 * @param paramDecl
	 *                  parameter that has to be removed from the corresponding
	 *                  FunctionDefinition.
	 * @return
	 *         true, if the parameter could have been removed from the list of
	 *         parameters.
	 */
	@Deprecated
	public boolean removeParameterDeclaration(ParameterDeclaration paramDecl) {
		AutomatedUpdater.flushCaches();
		FunctionDefinition node = (FunctionDefinition) this.getOwner();
		ParameterTypeListClosed paramTypeListClosed = Misc
				.getSingleton(Misc.getInheritedEnclosee(node.getF1(), ParameterTypeListClosed.class));
		if (paramTypeListClosed.getF1().present()) {
			ParameterList paramList = ((ParameterTypeList) paramTypeListClosed.getF1().getNode()).getF0();
			if (paramDecl == paramList.getF0()) {
				// If paramDecl is the first parameter of this function-definition.

				/*
				 * Update CFG edges.
				 */
				this.updateCFGForParameterRemoval(paramDecl);
				Program.invalidColumnNum = true;

				/*
				 * Updated AST information.
				 */
				if (paramList.getF1().present()) {
					// If there are more parameters in this function-definition.
					NodeSequence nodeSeq = (NodeSequence) paramList.getF1().removeNode(0);
					ParameterDeclaration shiftedParam = (ParameterDeclaration) nodeSeq.getNodes().get(1);
					shiftedParam.setParent(paramList);
					paramList.setF0(shiftedParam);
				} else {
					paramTypeListClosed.getF1().setNode(null);
				}
				return true;
			} else {
				int index = 0;
				boolean found = false;
				for (Node nodeSeqNode : paramList.getF1().getNodes()) {
					NodeSequence nodeSeq = (NodeSequence) nodeSeqNode;
					if (paramDecl == nodeSeq.getNodes().get(1)) {
						found = true;
						/*
						 * Update CFG edges.
						 */
						this.updateCFGForParameterRemoval(paramDecl);
						Program.invalidColumnNum = true;
						break;
					}
					index++;
				}
				if (!found) {
					return false;
				} else {
					/*
					 * Update AST information.
					 */
					paramList.getF1().removeNode(index);
					return true;
				}
			}
		} else {
			// If there are no parameters in this function-definition.
			return false;
		}
	}

	@Deprecated
	public void addParameterDeclaration(ParameterDeclaration targetNode) {
		this.addParameterDeclaration(this.getParameterDeclarationList().size(), targetNode);
	}

	@Deprecated
	public void addParameterDeclaration(int index, ParameterDeclaration targetNode) {
		AutomatedUpdater.flushCaches();
		ParameterList paramList = Misc.getSingleton(
				Misc.getInheritedEnclosee(((FunctionDefinition) getOwner()).getF1(), ParameterList.class));
		if (paramList == null) {
			// Create a parameterList.
			paramList = FrontEnd.parseAndNormalize(targetNode.toString(), ParameterList.class);

			// Add the newly created parameterList to the existing ParameterTypeListClosed.
			ParameterTypeListClosed paramTypeListClosed = Misc.getSingleton(Misc
					.getInheritedEnclosee(((FunctionDefinition) getOwner()).getF1(), ParameterTypeListClosed.class));
			ParameterTypeList paramTypeList = FrontEnd.parseAndNormalize(targetNode.toString(),
					ParameterTypeList.class);
			paramTypeList.setF0(paramList);
			NodeOptional nodeOptional = new NodeOptional(paramTypeList);
			paramTypeListClosed.setF1(nodeOptional);
		}
		if (index != 0) {
			NodeListOptional paramDeclList = paramList.getF1();
			NodeSequence nodeSeq = new NodeSequence(2);
			nodeSeq.addNode(new NodeToken(","));
			nodeSeq.addNode(targetNode);
			paramDeclList.addNode(index - 1, nodeSeq);
		} else {
			ParameterDeclaration oldParamDecl = paramList.getF0();
			targetNode.setParent(paramList);
			paramList.setF0(targetNode);
			NodeListOptional paramDeclList = paramList.getF1();
			NodeSequence nodeSeq = new NodeSequence(2);
			nodeSeq.addNode(new NodeToken(","));
			nodeSeq.addNode(oldParamDecl);
			paramDeclList.addNode(0, nodeSeq);
		}
		this.updateCFGForParameterAddition(targetNode);
		Program.invalidColumnNum = true;
	}

	public void setBody(CompoundStatement compStmt) {
		if (compStmt == this.getBody()) {
			return;
		}
		AutomatedUpdater.flushCaches();
		NodeRemover.removeNodeIfConnected(compStmt);
		FunctionDefinition owner = (FunctionDefinition) this.getOwner();

		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(compStmt);
		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(owner.getF3());
		compStmt.setParent(owner);

		Set<Node> rerunNodesForward = AutomatedUpdater.unreachableAfterRemovalForward(owner.getF3());
		Set<Node> rerunNodesBackward = AutomatedUpdater.unreachableAfterRemovalBackward(owner.getF3());
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(owner.getF3());
		AutomatedUpdater.updateInformationForRemoval(owner.getF3());
		updateCFGForBodyRemoval(owner.getF3());
		owner.setF3(compStmt);
		AutomatedUpdater.invalidateSymbolsInNode(owner.getF3());
		AutomatedUpdater.invalidateSymbolsInNode(compStmt);
		updateCFGForBodyAddition(compStmt);

		compStmt = Normalization.normalizeLeafNodes(compStmt, new ArrayList<>());

		Program.invalidColumnNum = Program.invalidLineNum = true;
		AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
		AutomatedUpdater.updateInformationForAddition(compStmt);
		AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after replacement is successful.
		// AutomatedUpdater.invalidateSymbolsInNode(owner.getF3());// Added, so that any
		// changes from points-to may be reflected here.
		// AutomatedUpdater.invalidateSymbolsInNode(compStmt);// Added, so that any
		// changes from points-to may be reflected here.
		AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
	}

	@Override
	public CompoundStatement getBody() {
		return ((FunctionDefinition) getOwner()).getF3();
	}

	private void updateCFGForBodyRemoval(CompoundStatement removed) {
		removed.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerRemoval();

		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		Statement body = removed;
		List<ParameterDeclaration> paramList = this.getParameterDeclarationList();
		if (paramList.isEmpty()) {
			disconnectAndAdjustEndReachability(ncfg.getBegin(), body);
		} else {
			ParameterDeclaration lastParam = paramList.get(paramList.size() - 1);
			disconnectAndAdjustEndReachability(lastParam, body);
		}
		disconnectAndAdjustEndReachability(body, ncfg.getEnd());
	}

	private void updateCFGForBodyAddition(CompoundStatement added) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		Statement body = added;
		List<ParameterDeclaration> paramList = this.getParameterDeclarationList();
		if (paramList.isEmpty()) {
			connectAndAdjustEndReachability(ncfg.getBegin(), body);
		} else {
			ParameterDeclaration lastParam = paramList.get(paramList.size() - 1);
			connectAndAdjustEndReachability(lastParam, body);
		}
		if (body.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(body, ncfg.getEnd());
		}

		added.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerAddition();
	}

	/**
	 * Updates the CFG edges upon removal of a parameter from this
	 * function-definition. <br>
	 * Note that this method should be called before changing the AST fields.
	 * 
	 * @param removed
	 */
	private void updateCFGForParameterRemoval(ParameterDeclaration removed) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// Remove stale edges.
		List<ParameterDeclaration> paramList = this.getParameterDeclarationList();
		int index = paramList.indexOf(removed);
		Node prevNode;
		Node nextNode;
		if (index == 0) {
			prevNode = ncfg.getBegin();
			if (paramList.size() > 1) {
				nextNode = paramList.get(1);
			} else {
				nextNode = this.getBody();
			}
		} else if (index < paramList.size() - 1) {
			prevNode = paramList.get(index - 1);
			nextNode = paramList.get(index + 1);
		} else if (index == paramList.size() - 1) {
			prevNode = paramList.get(index - 1);
			nextNode = this.getBody();
		} else {
			nextNode = prevNode = null;
			assert (false);
		}
		connectAndAdjustEndReachability(prevNode, nextNode);
		disconnectAndAdjustEndReachability(prevNode, removed);
		disconnectAndAdjustEndReachability(removed, nextNode);
	}

	private void updateCFGForParameterAddition(ParameterDeclaration added) {
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// Add new edges.
		List<ParameterDeclaration> paramList = this.getParameterDeclarationList();
		int index = paramList.indexOf(added);
		Node prevNode;
		Node nextNode;
		if (index == 0) {
			prevNode = ncfg.getBegin();
			if (paramList.size() > 1) {
				nextNode = paramList.get(1);
			} else {
				nextNode = this.getBody();
			}
		} else if (index < paramList.size() - 1) {
			prevNode = paramList.get(index - 1);
			nextNode = paramList.get(index + 1);
		} else if (index == paramList.size() - 1) {
			prevNode = paramList.get(index - 1);
			nextNode = this.getBody();
		} else {
			nextNode = prevNode = null;
			assert (false);
		}
		connectAndAdjustEndReachability(prevNode, added);
		connectAndAdjustEndReachability(added, nextNode);
		disconnectAndAdjustEndReachability(prevNode, nextNode);
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
		retList.addAll(this.getParameterDeclarationList());
		retList.add(this.getBody());
		retList.add(this.getNestedCFG().getEnd());
		return retList;
	}
}
