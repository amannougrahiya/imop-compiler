/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.link.autoupdater;

import java.util.List;

import imop.ast.node.external.CompoundStatement;
import imop.ast.node.external.Expression;
import imop.ast.node.external.GotoStatement;
import imop.ast.node.external.Node;
import imop.ast.node.external.OmpForReinitExpression;
import imop.ast.node.internal.EndNode;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cfg.info.ForStatementCFGInfo;
import imop.lib.cfg.link.baseVisitor.CFGLinkVisitor;
import imop.lib.cfg.link.node.AtomicBeginLink;
import imop.lib.cfg.link.node.AtomicEndLink;
import imop.lib.cfg.link.node.AtomicStatementLink;
import imop.lib.cfg.link.node.CFGLink;
import imop.lib.cfg.link.node.CallBeginLink;
import imop.lib.cfg.link.node.CallEndLink;
import imop.lib.cfg.link.node.CallPostLink;
import imop.lib.cfg.link.node.CallPreLink;
import imop.lib.cfg.link.node.CompoundBeginLink;
import imop.lib.cfg.link.node.CompoundElementLink;
import imop.lib.cfg.link.node.CompoundEndLink;
import imop.lib.cfg.link.node.CriticalBeginLink;
import imop.lib.cfg.link.node.CriticalBodyLink;
import imop.lib.cfg.link.node.CriticalEndLink;
import imop.lib.cfg.link.node.DoBeginLink;
import imop.lib.cfg.link.node.DoBodyLink;
import imop.lib.cfg.link.node.DoEndLink;
import imop.lib.cfg.link.node.DoPredicateLink;
import imop.lib.cfg.link.node.ForBeginLink;
import imop.lib.cfg.link.node.ForBodyLink;
import imop.lib.cfg.link.node.ForEndLink;
import imop.lib.cfg.link.node.ForInitLink;
import imop.lib.cfg.link.node.ForStepLink;
import imop.lib.cfg.link.node.ForTermLink;
import imop.lib.cfg.link.node.FunctionBeginLink;
import imop.lib.cfg.link.node.FunctionBodyLink;
import imop.lib.cfg.link.node.FunctionEndLink;
import imop.lib.cfg.link.node.FunctionParameterLink;
import imop.lib.cfg.link.node.IfBeginLink;
import imop.lib.cfg.link.node.IfElseBodyLink;
import imop.lib.cfg.link.node.IfEndLink;
import imop.lib.cfg.link.node.IfPredicateLink;
import imop.lib.cfg.link.node.IfThenBodyLink;
import imop.lib.cfg.link.node.MasterBeginLink;
import imop.lib.cfg.link.node.MasterBodyLink;
import imop.lib.cfg.link.node.MasterEndLink;
import imop.lib.cfg.link.node.OmpForBeginLink;
import imop.lib.cfg.link.node.OmpForBodyLink;
import imop.lib.cfg.link.node.OmpForEndLink;
import imop.lib.cfg.link.node.OmpForInitLink;
import imop.lib.cfg.link.node.OmpForStepLink;
import imop.lib.cfg.link.node.OmpForTermLink;
import imop.lib.cfg.link.node.OrderedBeginLink;
import imop.lib.cfg.link.node.OrderedBodyLink;
import imop.lib.cfg.link.node.OrderedEndLink;
import imop.lib.cfg.link.node.ParallelBeginLink;
import imop.lib.cfg.link.node.ParallelBodyLink;
import imop.lib.cfg.link.node.ParallelClauseLink;
import imop.lib.cfg.link.node.ParallelEndLink;
import imop.lib.cfg.link.node.SectionsBeginLink;
import imop.lib.cfg.link.node.SectionsEndLink;
import imop.lib.cfg.link.node.SectionsSectionBodyLink;
import imop.lib.cfg.link.node.SingleBeginLink;
import imop.lib.cfg.link.node.SingleBodyLink;
import imop.lib.cfg.link.node.SingleEndLink;
import imop.lib.cfg.link.node.SwitchBeginLink;
import imop.lib.cfg.link.node.SwitchBodyLink;
import imop.lib.cfg.link.node.SwitchEndLink;
import imop.lib.cfg.link.node.SwitchPredicateLink;
import imop.lib.cfg.link.node.TaskBeginLink;
import imop.lib.cfg.link.node.TaskBodyLink;
import imop.lib.cfg.link.node.TaskClauseLink;
import imop.lib.cfg.link.node.TaskEndLink;
import imop.lib.cfg.link.node.WhileBeginLink;
import imop.lib.cfg.link.node.WhileBodyLink;
import imop.lib.cfg.link.node.WhileEndLink;
import imop.lib.cfg.link.node.WhilePredicateLink;
import imop.lib.util.Misc;

public class EndReachabilityAdjuster {
	/**
	 * Performs changes to the required CFG edges, when the {@link EndNode} of
	 * {@code node} becomes reachable.
	 * 
	 * @param node
	 *            node whose {@link EndNode} has become reachable.
	 */
	public static void updateEndReachabilityAddition(Node node) {
		if (!Misc.isCFGNonLeafNode(node)) {
			return;
		}
		if (!node.getInfo().getCFGInfo().isEndReachable()) {
			return;
		}
		CFGLink link = CFGLinkFinder.getCFGLinkFor(node);
		if (link == null) {
			return;
		}
		link.accept(new EndReachabilityAdder());
	}

	/**
	 * Performs changes to the required CFG edges, when the {@link EndNode} of
	 * {@code node} becomes unreachable.
	 * 
	 * @param node
	 *            node whose {@link EndNode} has become unreachable.
	 */
	public static void updateEndReachabilityRemoval(Node node) {
		if (!Misc.isCFGNonLeafNode(node)) {
			return;
		}
		if (node.getInfo().getCFGInfo().isEndReachable()) {
			return;
		}
		CFGLink link = CFGLinkFinder.getCFGLinkFor(node);
		if (link == null) {
			return;
		}
		link.accept(new EndReachabilityRemover());
	}

	private static class EndReachabilityAdder extends CFGLinkVisitor {
		@Override
		public void visit(FunctionBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(FunctionParameterLink link) {
			assert (false);
		}

		@Override
		public void visit(FunctionBodyLink link) {
			CompoundStatement body = link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(FunctionEndLink link) {
			assert (false);
		}

		@Override
		public void visit(ParallelBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(ParallelClauseLink link) {
			assert (false);
		}

		@Override
		public void visit(ParallelBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(ParallelEndLink link) {
			assert (false);
		}

		@Override
		public void visit(OmpForBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(OmpForInitLink link) {
			assert (false);
		}

		@Override
		public void visit(OmpForTermLink link) {
			assert (false);
		}

		@Override
		public void visit(OmpForStepLink link) {
			assert (false);
		}

		@Override
		public void visit(OmpForBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			OmpForReinitExpression reinitExp = link.enclosingNonLeafNode.getInfo().getCFGInfo().getReinitExpression();
			CFGInfo.connectAndAdjustEndReachability(body, reinitExp);
		}

		@Override
		public void visit(OmpForEndLink link) {
			assert (false);
		}

		@Override
		public void visit(SectionsBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(SectionsSectionBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(SectionsEndLink link) {
			assert (false);
		}

		@Override
		public void visit(SingleBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(SingleBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(SingleEndLink link) {
			assert (false);
		}

		@Override
		public void visit(TaskBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(TaskClauseLink link) {
			assert (false);
		}

		@Override
		public void visit(TaskBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(TaskEndLink link) {
			assert (false);
		}

		@Override
		public void visit(MasterBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(MasterBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(MasterEndLink link) {
			assert (false);
		}

		@Override
		public void visit(CriticalBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(CriticalBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(CriticalEndLink link) {
			assert (false);
		}

		@Override
		public void visit(AtomicBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(AtomicStatementLink link) {
			assert (false);
		}

		@Override
		public void visit(AtomicEndLink link) {
			assert (false);
		}

		@Override
		public void visit(OrderedBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(OrderedBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(OrderedEndLink link) {
			assert (false);
		}

		@Override
		public void visit(CompoundBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(CompoundElementLink link) {
			List<Node> elementList = link.enclosingNonLeafNode.getInfo().getCFGInfo().getElementList();
			boolean isLastElement = (elementList.indexOf(link.childNode) == (elementList.size() - 1));
			if (isLastElement) {
				EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
				CFGInfo.connectAndAdjustEndReachability(link.childNode, endNode);
			} else {
				/*
				 * Add connection to the next node. Note that we do not
				 * change the reachability of the next node.
				 */
				Node nextNode = elementList.get(elementList.indexOf(link.childNode) + 1);
				CFGInfo.connectAndAdjustEndReachability(link.childNode, nextNode);
			}
		}

		@Override
		public void visit(CompoundEndLink link) {
			assert (false);
		}

		@Override
		public void visit(IfBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(IfPredicateLink link) {
			assert (false);
		}

		@Override
		public void visit(IfThenBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(IfElseBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(IfEndLink link) {
			assert (false);
		}

		@Override
		public void visit(SwitchBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(SwitchPredicateLink link) {
			assert (false);
		}

		@Override
		public void visit(SwitchBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(SwitchEndLink link) {
			assert (false);
		}

		@Override
		public void visit(WhileBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(WhilePredicateLink link) {
			assert (false);
		}

		@Override
		public void visit(WhileBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			Expression predicate = link.enclosingNonLeafNode.getInfo().getCFGInfo().getPredicate();
			CFGInfo.connectAndAdjustEndReachability(body, predicate);
		}

		@Override
		public void visit(WhileEndLink link) {
			assert (false);
		}

		@Override
		public void visit(DoBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(DoPredicateLink link) {
			assert (false);
		}

		@Override
		public void visit(DoBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			Expression predicate = link.enclosingNonLeafNode.getInfo().getCFGInfo().getPredicate();
			CFGInfo.connectAndAdjustEndReachability(body, predicate);
		}

		@Override
		public void visit(DoEndLink link) {
			assert (false);
		}

		@Override
		public void visit(ForBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(ForInitLink link) {
			assert (false);
		}

		@Override
		public void visit(ForTermLink link) {
			assert (false);
		}

		@Override
		public void visit(ForStepLink link) {
			assert (false);
		}

		@Override
		public void visit(ForBodyLink link) {
			ForStatementCFGInfo forInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (forInfo.hasStepExpression()) {
				Expression stepExpression = forInfo.getStepExpression();
				CFGInfo.connectAndAdjustEndReachability(link.childNode, stepExpression);
			} else if (forInfo.hasTerminationExpression()) {
				Expression termExpression = forInfo.getTerminationExpression();
				CFGInfo.connectAndAdjustEndReachability(link.childNode, termExpression);
			} else {
				CFGInfo.connectAndAdjustEndReachability(link.childNode, link.childNode);
			}
		}

		@Override
		public void visit(ForEndLink link) {
			assert (false);
		}

		@Override
		public void visit(CallBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(CallPreLink link) {
			assert (false);
		}

		@Override
		public void visit(CallPostLink link) {
			assert (false);
		}

		@Override
		public void visit(CallEndLink link) {
			assert (false);
		}
	}

	private static class EndReachabilityRemover extends CFGLinkVisitor {
		@Override
		public void visit(FunctionBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(FunctionParameterLink link) {
			assert (false);
		}

		@Override
		public void visit(FunctionBodyLink link) {
			CompoundStatement body = link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.disconnectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(FunctionEndLink link) {
			assert (false);
		}

		@Override
		public void visit(ParallelBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(ParallelClauseLink link) {
			assert (false);
		}

		@Override
		public void visit(ParallelBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.disconnectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(ParallelEndLink link) {
			assert (false);
		}

		@Override
		public void visit(OmpForBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(OmpForInitLink link) {
			assert (false);
		}

		@Override
		public void visit(OmpForTermLink link) {
			assert (false);
		}

		@Override
		public void visit(OmpForStepLink link) {
			assert (false);
		}

		@Override
		public void visit(OmpForBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			OmpForReinitExpression reinitExp = link.enclosingNonLeafNode.getInfo().getCFGInfo().getReinitExpression();
			CFGInfo.disconnectAndAdjustEndReachability(body, reinitExp);
		}

		@Override
		public void visit(OmpForEndLink link) {
			assert (false);
		}

		@Override
		public void visit(SectionsBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(SectionsSectionBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.disconnectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(SectionsEndLink link) {
			assert (false);
		}

		@Override
		public void visit(SingleBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(SingleBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.disconnectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(SingleEndLink link) {
			assert (false);
		}

		@Override
		public void visit(TaskBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(TaskClauseLink link) {
			assert (false);
		}

		@Override
		public void visit(TaskBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.disconnectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(TaskEndLink link) {
			assert (false);
		}

		@Override
		public void visit(MasterBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(MasterBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.disconnectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(MasterEndLink link) {
			assert (false);
		}

		@Override
		public void visit(CriticalBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(CriticalBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.disconnectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(CriticalEndLink link) {
			assert (false);
		}

		@Override
		public void visit(AtomicBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(AtomicStatementLink link) {
			assert (false);
		}

		@Override
		public void visit(AtomicEndLink link) {
			assert (false);
		}

		@Override
		public void visit(OrderedBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(OrderedBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.disconnectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(OrderedEndLink link) {
			assert (false);
		}

		@Override
		public void visit(CompoundBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(CompoundElementLink link) {
			List<Node> elementList = link.enclosingNonLeafNode.getInfo().getCFGInfo().getElementList();
			boolean isLastElement = (elementList.indexOf(link.childNode) == (elementList.size() - 1));
			if (isLastElement) {
				EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
				CFGInfo.disconnectAndAdjustEndReachability(link.childNode, endNode);
			} else {
				/*
				 * UPDATE: Do not remove the edge, if link.childNode is a
				 * GotoStatement, whose label is the one present in nextNode.
				 * Oh, but then we are only processing those elements in this
				 * visitor which were earlier end-reachable, but are not so now
				 * -- that implies the link.childNode can never be a
				 * GotoStatement.
				 * Let's assert this.
				 */
				assert (!(link.childNode instanceof GotoStatement));
				/*
				 * Remove connection with the next node. Note that we do not
				 * change the reachability of the next node.
				 */
				Node nextNode = elementList.get(elementList.indexOf(link.childNode));
				CFGInfo.disconnectAndAdjustEndReachability(link.childNode, nextNode);
			}
		}

		@Override
		public void visit(CompoundEndLink link) {
			assert (false);
		}

		@Override
		public void visit(IfBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(IfPredicateLink link) {
			assert (false);
		}

		@Override
		public void visit(IfThenBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.disconnectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(IfElseBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.disconnectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(IfEndLink link) {
			assert (false);
		}

		@Override
		public void visit(SwitchBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(SwitchPredicateLink link) {
			assert (false);
		}

		@Override
		public void visit(SwitchBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			EndNode endNode = link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.disconnectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(SwitchEndLink link) {
			assert (false);
		}

		@Override
		public void visit(WhileBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(WhilePredicateLink link) {
			assert (false);
		}

		@Override
		public void visit(WhileBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			Expression predicate = link.enclosingNonLeafNode.getInfo().getCFGInfo().getPredicate();
			CFGInfo.disconnectAndAdjustEndReachability(body, predicate);
		}

		@Override
		public void visit(WhileEndLink link) {
			assert (false);
		}

		@Override
		public void visit(DoBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(DoPredicateLink link) {
			assert (false);
		}

		@Override
		public void visit(DoBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.childNode;
			Expression predicate = link.enclosingNonLeafNode.getInfo().getCFGInfo().getPredicate();
			CFGInfo.disconnectAndAdjustEndReachability(body, predicate);
		}

		@Override
		public void visit(DoEndLink link) {
			assert (false);
		}

		@Override
		public void visit(ForBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(ForInitLink link) {
			assert (false);
		}

		@Override
		public void visit(ForTermLink link) {
			assert (false);
		}

		@Override
		public void visit(ForStepLink link) {
			assert (false);
		}

		@Override
		public void visit(ForBodyLink link) {
			ForStatementCFGInfo forInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (forInfo.hasStepExpression()) {
				Expression stepExpression = forInfo.getStepExpression();
				CFGInfo.disconnectAndAdjustEndReachability(link.childNode, stepExpression);
			} else if (forInfo.hasTerminationExpression()) {
				Expression termExpression = forInfo.getTerminationExpression();
				CFGInfo.disconnectAndAdjustEndReachability(link.childNode, termExpression);
			} else {
				CFGInfo.disconnectAndAdjustEndReachability(link.childNode, link.childNode);
			}
		}

		@Override
		public void visit(ForEndLink link) {
			assert (false);
		}

		@Override
		public void visit(CallBeginLink link) {
			assert (false);
		}

		@Override
		public void visit(CallPreLink link) {
			assert (false);
		}

		@Override
		public void visit(CallPostLink link) {
			assert (false);
		}

		@Override
		public void visit(CallEndLink link) {
			assert (false);
		}
	}

}
