/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.link.autoupdater;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cfg.info.ForStatementCFGInfo;
import imop.lib.cfg.link.baseVisitor.CFGLinkVisitor;
import imop.lib.cfg.link.node.*;
import imop.lib.util.Misc;

import java.util.Collection;
import java.util.List;

public class NextNodeJoiner {
	private static NextNodeJoinVisitor visitor = new NextNodeJoinVisitor();

	/**
	 * This method adds the edges, if any, between the given node and all its
	 * immediate successors, as per OpenMP C semantics.
	 * Such changes may trigger further addition of edges, until a fixed point
	 * is reached.
	 * 
	 * @param node
	 *             node that needs to be connected to all its immediate
	 *             successors.
	 */
	public static void joinToNextNode(Node node) {
		assert (!(node instanceof EndNode));
		for (Node n : Misc.getFirstLeaves(node)) {
			CFGLink link = CFGLinkFinder.getCFGLinkFor(n);
			link.accept(visitor);
		}
	}

	private static class NextNodeJoinVisitor extends CFGLinkVisitor {

		@Override
		public void visit(FunctionBeginLink link) {
			BeginNode beginNode = link.childNode;
			FunctionDefinition funcDef = link.enclosingNonLeafNode;
			List<ParameterDeclaration> paramList = funcDef.getInfo().getCFGInfo().getParameterDeclarationList();
			if (paramList == null || paramList.isEmpty()) {
				CFGInfo.connectAndAdjustEndReachability(beginNode, funcDef.getInfo().getCFGInfo().getBody());
			} else {
				CFGInfo.connectAndAdjustEndReachability(beginNode, paramList.get(0));
			}
		}

		@Override
		public void visit(FunctionParameterLink link) {
			FunctionDefinition funcDef = link.enclosingNonLeafNode;
			ParameterDeclaration param = link.childNode;
			List<ParameterDeclaration> paramList = funcDef.getInfo().getCFGInfo().getParameterDeclarationList();
			assert (paramList != null && paramList.contains(param));
			int thisIndex = paramList.indexOf(param);
			if (thisIndex < paramList.size() - 1) {
				CFGInfo.connectAndAdjustEndReachability(param, paramList.get(thisIndex + 1));
			} else {
				CFGInfo.connectAndAdjustEndReachability(param, funcDef.getInfo().getCFGInfo().getBody());
			}
		}

		@Override
		public void visit(FunctionBodyLink link) {
			FunctionDefinition funcDef = link.enclosingNonLeafNode;
			CompoundStatement body = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(body, funcDef.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(FunctionEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(ParallelBeginLink link) {
			ParallelConstruct parCons = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			List<OmpClause> clauseList = parCons.getInfo().getCFGInfo().getCFGClauseList();
			if (clauseList == null || clauseList.isEmpty()) {
				CFGInfo.connectAndAdjustEndReachability(beginNode, parCons.getInfo().getCFGInfo().getBody());
			} else {
				CFGInfo.connectAndAdjustEndReachability(beginNode, clauseList.get(0));
			}
		}

		@Override
		public void visit(ParallelClauseLink link) {
			ParallelConstruct parCons = link.enclosingNonLeafNode;
			OmpClause clause = link.childNode;
			List<OmpClause> clauseList = parCons.getInfo().getCFGInfo().getCFGClauseList();
			assert (clauseList != null && clauseList.contains(clause));
			int thisIndex = clauseList.indexOf(clause);
			if (thisIndex < clauseList.size() - 1) {
				CFGInfo.connectAndAdjustEndReachability(clause, clauseList.get(thisIndex + 1));
			} else {
				CFGInfo.connectAndAdjustEndReachability(clause, parCons.getInfo().getCFGInfo().getBody());
			}
		}

		@Override
		public void visit(ParallelBodyLink link) {
			ParallelConstruct parCons = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(body, parCons.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(ParallelEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(OmpForBeginLink link) {
			ForConstruct forCons = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			OmpForInitExpression init = forCons.getInfo().getCFGInfo().getInitExpression();
			CFGInfo.connectAndAdjustEndReachability(beginNode, init);
		}

		@Override
		public void visit(OmpForInitLink link) {
			ForConstruct forCons = link.enclosingNonLeafNode;
			OmpForInitExpression init = link.childNode;
			OmpForCondition cond = forCons.getInfo().getCFGInfo().getForConditionExpression();
			CFGInfo.connectAndAdjustEndReachability(init, cond);

		}

		@Override
		public void visit(OmpForTermLink link) {
			ForConstruct forCons = link.enclosingNonLeafNode;
			OmpForCondition cond = link.childNode;
			Statement body = forCons.getInfo().getCFGInfo().getBody();
			EndNode endNode = forCons.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(cond, body);
			CFGInfo.connectAndAdjustEndReachability(cond, endNode);
		}

		@Override
		public void visit(OmpForStepLink link) {
			ForConstruct forCons = link.enclosingNonLeafNode;
			OmpForReinitExpression step = link.childNode;
			OmpForCondition cond = forCons.getInfo().getCFGInfo().getForConditionExpression();
			CFGInfo.connectAndAdjustEndReachability(step, cond);
		}

		@Override
		public void visit(OmpForBodyLink link) {
			ForConstruct forCons = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			OmpForReinitExpression step = forCons.getInfo().getCFGInfo().getReinitExpression();
			CFGInfo.connectAndAdjustEndReachability(body, step);
		}

		@Override
		public void visit(OmpForEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(SectionsBeginLink link) {
			SectionsConstruct sectionsCons = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			Collection<Statement> sectionList = sectionsCons.getInfo().getCFGInfo().getSectionList();
			if (sectionList.isEmpty()) {
				EndNode endNode = sectionsCons.getInfo().getCFGInfo().getNestedCFG().getEnd();
				CFGInfo.connectAndAdjustEndReachability(beginNode, endNode);
			} else {
				for (Statement section : sectionList) {
					CFGInfo.connectAndAdjustEndReachability(beginNode, section);
				}
			}
		}

		@Override
		public void visit(SectionsSectionBodyLink link) {
			SectionsConstruct sectionsCons = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			EndNode endNode = sectionsCons.getInfo().getCFGInfo().getNestedCFG().getEnd();
			CFGInfo.connectAndAdjustEndReachability(body, endNode);
		}

		@Override
		public void visit(SectionsEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(SingleBeginLink link) {
			SingleConstruct singleCons = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(beginNode, singleCons.getInfo().getCFGInfo().getBody());
		}

		@Override
		public void visit(SingleBodyLink link) {
			SingleConstruct singleCons = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(body, singleCons.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(SingleEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(TaskBeginLink link) {
			TaskConstruct taskCons = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			List<OmpClause> clauseList = taskCons.getInfo().getCFGInfo().getCFGClauseList();
			if (clauseList == null || clauseList.isEmpty()) {
				CFGInfo.connectAndAdjustEndReachability(beginNode, taskCons.getInfo().getCFGInfo().getBody());
			} else {
				CFGInfo.connectAndAdjustEndReachability(beginNode, clauseList.get(0));
			}
		}

		@Override
		public void visit(TaskClauseLink link) {
			TaskConstruct taskCons = link.enclosingNonLeafNode;
			OmpClause clause = link.childNode;
			List<OmpClause> clauseList = taskCons.getInfo().getCFGInfo().getCFGClauseList();
			assert (clauseList != null && clauseList.contains(clause));
			int thisIndex = clauseList.indexOf(clause);
			if (thisIndex < clauseList.size() - 1) {
				CFGInfo.connectAndAdjustEndReachability(clause, clauseList.get(thisIndex + 1));
			} else {
				CFGInfo.connectAndAdjustEndReachability(clause, taskCons.getInfo().getCFGInfo().getBody());
			}
		}

		@Override
		public void visit(TaskBodyLink link) {
			TaskConstruct taskCons = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(body, taskCons.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(TaskEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(MasterBeginLink link) {
			MasterConstruct masterCons = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(beginNode, masterCons.getInfo().getCFGInfo().getBody());
		}

		@Override
		public void visit(MasterBodyLink link) {
			MasterConstruct masterCons = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(body, masterCons.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(MasterEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(CriticalBeginLink link) {
			CriticalConstruct criticalCons = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(beginNode, criticalCons.getInfo().getCFGInfo().getBody());
		}

		@Override
		public void visit(CriticalBodyLink link) {
			CriticalConstruct criticalCons = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(body, criticalCons.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(CriticalEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(AtomicBeginLink link) {
			AtomicConstruct atomicCons = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(beginNode, atomicCons.getInfo().getCFGInfo().getBody());
		}

		@Override
		public void visit(AtomicStatementLink link) {
			AtomicConstruct atomicCons = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(body, atomicCons.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(AtomicEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(OrderedBeginLink link) {
			OrderedConstruct orderedCons = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(beginNode, orderedCons.getInfo().getCFGInfo().getBody());
		}

		@Override
		public void visit(OrderedBodyLink link) {
			OrderedConstruct orderedCons = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(body, orderedCons.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(OrderedEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(CompoundBeginLink link) {
			CompoundStatement compStmt = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			List<Node> elemList = compStmt.getInfo().getCFGInfo().getElementList();
			if (elemList.isEmpty()) {
				CFGInfo.connectAndAdjustEndReachability(beginNode,
						compStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());
			} else {
				CFGInfo.connectAndAdjustEndReachability(beginNode, elemList.get(0));
			}
		}

		@Override
		public void visit(CompoundElementLink link) {
			CompoundStatement compStmt = link.enclosingNonLeafNode;
			/*
			 * Note that this element could also be a jump statement.
			 */
			Node elem = link.childNode;
			if (!(elem instanceof JumpStatement)) {
				List<Node> elemList = compStmt.getInfo().getCFGInfo().getElementList();
				assert (elemList != null && elemList.contains(elem));
				int thisIndex = elemList.indexOf(elem);
				if (thisIndex < elemList.size() - 1) {
					CFGInfo.connectAndAdjustEndReachability(elem, elemList.get(thisIndex + 1));
				} else {
					CFGInfo.connectAndAdjustEndReachability(elem,
							compStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());
				}
			} else {
				if (elem instanceof GotoStatement) {
					GotoStatement gotoStmt = (GotoStatement) elem;
					Node target = gotoStmt.getInfo().getCFGInfo().getTarget();
					if (target != null) {
						CFGInfo.connectAndAdjustEndReachability(gotoStmt, target);
					}
				} else if (elem instanceof BreakStatement) {
					BreakStatement breakStmt = (BreakStatement) elem;
					Node target = breakStmt.getInfo().getCFGInfo().getTarget();
					if (target != null) {
						CFGInfo.connectAndAdjustEndReachability(breakStmt, target);
					}
				} else if (elem instanceof ContinueStatement) {
					ContinueStatement continueStmt = (ContinueStatement) elem;
					Node target = continueStmt.getInfo().getCFGInfo().getTarget();
					if (target != null) {
						CFGInfo.connectAndAdjustEndReachability(continueStmt, target);
					}
				} else if (elem instanceof ReturnStatement) {
					ReturnStatement returnStmt = (ReturnStatement) elem;
					Node target = returnStmt.getInfo().getCFGInfo().getTarget();
					if (target != null) {
						CFGInfo.connectAndAdjustEndReachability(returnStmt, target);
					}
				} else {
					assert (false);
				}
			}
		}

		@Override
		public void visit(CompoundEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(IfBeginLink link) {
			IfStatement ifStmt = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(beginNode, ifStmt.getInfo().getCFGInfo().getPredicate());
		}

		@Override
		public void visit(IfPredicateLink link) {
			IfStatement ifStmt = link.enclosingNonLeafNode;
			Expression pred = link.childNode;
			// We defer the check of precise edges to CFGInfo.
			CFGInfo.connectAndAdjustEndReachability(pred, ifStmt.getInfo().getCFGInfo().getThenBody());
			if (ifStmt.getInfo().getCFGInfo().hasElseBody()) {
				CFGInfo.connectAndAdjustEndReachability(pred, ifStmt.getInfo().getCFGInfo().getElseBody());
			}
		}

		@Override
		public void visit(IfThenBodyLink link) {
			IfStatement ifStmt = link.enclosingNonLeafNode;
			Statement thenBody = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(thenBody, ifStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(IfElseBodyLink link) {
			IfStatement ifStmt = link.enclosingNonLeafNode;
			Statement elseBody = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(elseBody, ifStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(IfEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(SwitchBeginLink link) {
			SwitchStatement switchStmt = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(beginNode, switchStmt.getInfo().getCFGInfo().getPredicate());
		}

		@Override
		public void visit(SwitchPredicateLink link) {
			SwitchStatement switchStmt = link.enclosingNonLeafNode;
			Expression pred = link.childNode;
			Collection<Statement> targetStmtList = switchStmt.getInfo().getRelevantCFGStatements();
			for (Statement target : targetStmtList) {
				CFGInfo.connectAndAdjustEndReachability(pred, target);
			}
			if (!switchStmt.getInfo().hasDefaultLabel()) {
				CFGInfo.connectAndAdjustEndReachability(pred,
						switchStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());
			}

		}

		@Override
		public void visit(SwitchBodyLink link) {
			SwitchStatement switchStmt = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(body, switchStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(SwitchEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(WhileBeginLink link) {
			WhileStatement whileStmt = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(beginNode, whileStmt.getInfo().getCFGInfo().getPredicate());

		}

		@Override
		public void visit(WhilePredicateLink link) {
			WhileStatement whileStmt = link.enclosingNonLeafNode;
			Expression pred = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(pred, whileStmt.getInfo().getCFGInfo().getBody());
			CFGInfo.connectAndAdjustEndReachability(pred, whileStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(WhileBodyLink link) {
			WhileStatement whileStmt = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(body, whileStmt.getInfo().getCFGInfo().getPredicate());
		}

		@Override
		public void visit(WhileEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(DoBeginLink link) {
			DoStatement doStmt = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(beginNode, doStmt.getInfo().getCFGInfo().getBody());
		}

		@Override
		public void visit(DoPredicateLink link) {
			DoStatement doStmt = link.enclosingNonLeafNode;
			Expression pred = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(pred, doStmt.getInfo().getCFGInfo().getBody());
			CFGInfo.connectAndAdjustEndReachability(pred, doStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(DoBodyLink link) {
			DoStatement doStmt = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(body, doStmt.getInfo().getCFGInfo().getPredicate());
		}

		@Override
		public void visit(DoEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(ForBeginLink link) {
			ForStatement forStmt = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			ForStatementCFGInfo forInfo = forStmt.getInfo().getCFGInfo();
			if (forInfo.hasInitExpression()) {
				CFGInfo.connectAndAdjustEndReachability(beginNode, forInfo.getInitExpression());
			} else if (forInfo.hasTerminationExpression()) {
				CFGInfo.connectAndAdjustEndReachability(beginNode, forInfo.getTerminationExpression());
			} else {
				CFGInfo.connectAndAdjustEndReachability(beginNode, forInfo.getBody());
			}
		}

		@Override
		public void visit(ForInitLink link) {
			ForStatement forStmt = link.enclosingNonLeafNode;
			Expression init = link.childNode;
			ForStatementCFGInfo forInfo = forStmt.getInfo().getCFGInfo();
			if (forInfo.hasTerminationExpression()) {
				CFGInfo.connectAndAdjustEndReachability(init, forInfo.getTerminationExpression());
			} else {
				CFGInfo.connectAndAdjustEndReachability(init, forInfo.getBody());
			}
		}

		@Override
		public void visit(ForTermLink link) {
			ForStatement forStmt = link.enclosingNonLeafNode;
			Expression term = link.childNode;
			ForStatementCFGInfo forInfo = forStmt.getInfo().getCFGInfo();
			CFGInfo.connectAndAdjustEndReachability(term, forInfo.getBody());
			CFGInfo.connectAndAdjustEndReachability(term, forInfo.getNestedCFG().getEnd());
		}

		@Override
		public void visit(ForStepLink link) {
			ForStatement forStmt = link.enclosingNonLeafNode;
			Expression step = link.childNode;
			ForStatementCFGInfo forInfo = forStmt.getInfo().getCFGInfo();
			if (forInfo.hasTerminationExpression()) {
				CFGInfo.connectAndAdjustEndReachability(step, forInfo.getTerminationExpression());
			} else {
				CFGInfo.connectAndAdjustEndReachability(step, forInfo.getBody());
			}
		}

		@Override
		public void visit(ForBodyLink link) {
			ForStatement forStmt = link.enclosingNonLeafNode;
			Statement body = link.childNode;
			ForStatementCFGInfo forInfo = forStmt.getInfo().getCFGInfo();
			if (forInfo.hasStepExpression()) {
				CFGInfo.connectAndAdjustEndReachability(body, forInfo.getStepExpression());
			} else if (forInfo.hasTerminationExpression()) {
				CFGInfo.connectAndAdjustEndReachability(body, forInfo.getTerminationExpression());
			} else {
				CFGInfo.connectAndAdjustEndReachability(body, body);
			}
		}

		@Override
		public void visit(ForEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}

		@Override
		public void visit(CallBeginLink link) {
			CallStatement callStmt = link.enclosingNonLeafNode;
			BeginNode beginNode = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(beginNode, callStmt.getPreCallNode());
		}

		@Override
		public void visit(CallPreLink link) {
			CallStatement callStmt = link.enclosingNonLeafNode;
			PreCallNode preCallNode = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(preCallNode, callStmt.getPostCallNode());
		}

		@Override
		public void visit(CallPostLink link) {
			CallStatement callStmt = link.enclosingNonLeafNode;
			PostCallNode postCallNode = link.childNode;
			CFGInfo.connectAndAdjustEndReachability(postCallNode,
					callStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());
		}

		@Override
		public void visit(CallEndLink link) {
			Node parent = link.enclosingNonLeafNode;
			EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
		}
	}

}
