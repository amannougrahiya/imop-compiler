/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.updater;

import imop.ast.annotation.IncompleteEdge;
import imop.ast.annotation.IncompleteEdge.TypeOfIncompleteness;
import imop.ast.info.cfgNodeInfo.ReturnStatementInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.builder.Builder;
import imop.lib.builder.Copier;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.info.*;
import imop.lib.cfg.link.baseVisitor.GJNoArguCFGLinkVisitor;
import imop.lib.cfg.link.node.*;
import imop.lib.transform.updater.InsertOnTheEdge.PivotPoint;
import imop.lib.transform.updater.NodeRemover.LabelShiftingMode;
import imop.lib.transform.updater.sideeffect.*;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Insert the given {@code targetNode} before the {@code baseNode} on all paths
 * in the CFG.
 * 
 * @author Aman Nougrahiya
 *
 */
public class InsertImmediateSuccessor {
	/**
	 * Insert a given {@code targetNode} before a {@code baseNode}
	 * on all the paths, without any other major changes to the existing CFG.
	 * 
	 * @param baseNode
	 * @param targetNode
	 * @return
	 */
	public static List<SideEffect> insertSimple(Node baseNode, Node targetNode) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		baseNode = Misc.getCFGNodeFor(baseNode);
		targetNode = Misc.getCFGNodeFor(targetNode);

		CFGLink cfgLink = CFGLinkFinder.getCFGLinkFor(baseNode);
		if (cfgLink == null) {
			Misc.warnDueToLackOfFeature(
					"Cannot insert a node immediately after some other node, due to lack of an appropriate enclosing node.",
					baseNode);
			sideEffectList.add(new MissingCFGParent(baseNode));
			return sideEffectList;
		}
		return cfgLink.accept(new SimpleInsertImmediateSuccessor(targetNode));
	}

	/**
	 * Insert a given {@code targetNode} before a {@code baseNode}
	 * on all the paths, with higher probability of insertion and side-effects.
	 * 
	 * @param baseNode
	 * @param targetNode
	 * @return
	 */
	public static List<SideEffect> insert(Node baseNode, Node targetNode) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		baseNode = Misc.getCFGNodeFor(baseNode);
		targetNode = Misc.getCFGNodeFor(targetNode);

		if (!(targetNode instanceof Statement) && !(targetNode instanceof Declaration)) {
			String str = "Cannot insert a non-statement or a non-declaration in aggressive mode. Use elementary methods instead.";
			Misc.warnDueToLackOfFeature(str, baseNode);
			sideEffectList.add(new SyntacticConstraint(baseNode, str));
			return sideEffectList;
		}

		CFGLink cfgLink = CFGLinkFinder.getCFGLinkFor(baseNode);
		if (cfgLink == null) {
			Misc.warnDueToLackOfFeature(
					"Cannot insert a node immediately after some other node, due to lack of an appropriate enclosing node for the base node.",
					baseNode);
			sideEffectList.add(new MissingCFGParent(baseNode));
			return sideEffectList;
		}

		List<Node> baseSuccessors = baseNode.getInfo().getCFGInfo().getSuccessors();

		if (!(baseNode instanceof EndNode) && baseSuccessors.isEmpty()) {
			String str = "Cannot insert a node after a dangling node.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(baseNode, str));
			return sideEffectList;
		}

		if (baseSuccessors.size() <= 1 || cfgLink instanceof DoPredicateLink) {
			sideEffectList = cfgLink.accept(new AggressiveInsertImmediateSuccessor(targetNode));
			return sideEffectList;
		} else {
			Node copiedTarget = Builder.getCopiedTarget(targetNode);
			List<Node> unrollTargets = new ArrayList<>();
			baseSuccessors = new ArrayList<>(baseNode.getInfo().getCFGInfo().getSuccessors());
			boolean first = true;
			for (Node baseSuccessor : baseSuccessors) {
				if (first) {
					first = false;
					sideEffectList.addAll(
							InsertOnTheEdge.insertAggressive(baseNode, baseSuccessor, targetNode, PivotPoint.SOURCE));
					if (!Misc.changePerformed(sideEffectList)) {
						for (Node n : unrollTargets) {
							NodeRemover.removeNode(n, LabelShiftingMode.LABELS_WITH_NODE);
						}
						for (SideEffect se : new ArrayList<>(sideEffectList)) {
							if (!(se instanceof AddedCopy)) {
								continue;
							}
							AddedCopy ac = (AddedCopy) se;
							if (unrollTargets.contains(ac.affectedNode)) {
								sideEffectList.remove(se);
							}
						}
						return sideEffectList;
					}
					unrollTargets.add(targetNode);
					continue;
				}
				sideEffectList.addAll(
						InsertOnTheEdge.insertAggressive(baseNode, baseSuccessor, copiedTarget, PivotPoint.SOURCE));
				if (!Misc.changePerformed(sideEffectList)) {
					for (Node n : unrollTargets) {
						NodeRemover.removeNode(n, LabelShiftingMode.LABELS_WITH_NODE);
					}
					for (SideEffect se : new ArrayList<>(sideEffectList)) {
						if (!(se instanceof AddedCopy)) {
							continue;
						}
						AddedCopy ac = (AddedCopy) se;
						if (unrollTargets.contains(ac.affectedNode)) {
							sideEffectList.remove(se);
						}
					}
					return sideEffectList;
				}
				unrollTargets.add(copiedTarget);
				copiedTarget = Builder.getCopiedTarget(copiedTarget);
				sideEffectList.add(new AddedCopy(copiedTarget));
			}
			return sideEffectList;
		}
	}

	private static class AggressiveInsertImmediateSuccessor extends SimpleInsertImmediateSuccessor {
		public AggressiveInsertImmediateSuccessor(Node targetNode) {
			super(targetNode);
		}

		@Override
		public List<SideEffect> visit(FunctionBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			FunctionDefinition parentFunc = link.enclosingNonLeafNode;

			if (parentFunc.getInfo().getCFGInfo().getParameterDeclarationList().size() != 0) {
				String str = "Cannot insert a node immediately after the BeginNode of a FunctionDefinition with non-empty parameters.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}

			CompoundStatement body = parentFunc.getInfo().getCFGInfo().getBody();
			sideEffectList.addAll(body.getInfo().getCFGInfo().addElement(0, targetNode));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(FunctionParameterLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			FunctionDefinition parentFunc = link.enclosingNonLeafNode;

			List<ParameterDeclaration> paramList = parentFunc.getInfo().getCFGInfo().getParameterDeclarationList();
			if (link.childNode != paramList.get(paramList.size() - 1)) {
				String str = "Cannot insert a node immediately after the non-last parameter of a FunctionDefinition.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}

			CompoundStatement body = parentFunc.getInfo().getCFGInfo().getBody();
			sideEffectList.addAll(body.getInfo().getCFGInfo().addElement(0, targetNode));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(FunctionBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			FunctionDefinition funcDef = link.enclosingNonLeafNode;
			CompoundStatement body = funcDef.getInfo().getCFGInfo().getBody();
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the body of a FunctionDefinition, since the end of the body is not reachable."
						+ "Suggestion: Try inserting the node immediately before the endNode of the FunctionDefinition, if that's semantically valid.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			EndNode bodyEndNode = body.getInfo().getCFGInfo().getNestedCFG().getEnd();
			return InsertImmediatePredecessor.insert(bodyEndNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(FunctionEndLink link) {
			return InsertImmediatePredecessor.insert(link.childNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(ParallelBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ParallelConstruct parConstruct = link.enclosingNonLeafNode;
			ParallelConstructCFGInfo parConsInfo = parConstruct.getInfo().getCFGInfo();
			if (parConsInfo.hasIfClause() || parConsInfo.hasNumThreadsClause()) {
				String str = "Cannot insert a node immediately after the BeginNode of a ParallelConstruct since it has some executable clauses.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			CompoundStatement body = (CompoundStatement) parConsInfo.getBody();
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt));
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, body));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediateSuccessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(ParallelClauseLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ParallelConstruct parConstruct = link.enclosingNonLeafNode;
			ParallelConstructCFGInfo parConsInfo = parConstruct.getInfo().getCFGInfo();

			List<OmpClause> clauses = parConsInfo.getCFGClauseList();
			if (link.childNode != clauses.get(clauses.size() - 1)) {
				String str = "Cannot insert a node immediately after the non-last clause of a parallelConstruct.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}

			CompoundStatement body = (CompoundStatement) parConsInfo.getBody();
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt));
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, body));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediateSuccessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(ParallelBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ParallelConstructCFGInfo parConsStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) parConsStmtInfo.getBody();
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the body of a ParallelConstruct, since the end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(ParallelEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(OmpForBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(OmpForInitLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the initialization expression of an omp forConstruct.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForTermLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OmpForStepLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the update expression of a omp for construct.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ForConstructCFGInfo ompForStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) ompForStmtInfo.getBody();
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the body of an OmpFor, since the end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(OmpForEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SectionsBeginLink link) {
			BeginNode sectionBegin = link.childNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			List<Node> succList = sectionBegin.getInfo().getCFGInfo().getSuccBlocks();
			if (succList.size() != 1) {
				assert (false);
				return null;
			}
			Node succ = succList.get(0);
			if (succ instanceof EndNode) {
				// Add the CS-encapsulated code as a new and only section.
				Node cfgNode = Misc.getCFGNodeFor(targetNode);
				if (cfgNode instanceof CompoundStatement) {
					return link.enclosingNonLeafNode.getInfo().getCFGInfo().addSection((Statement) targetNode);
				} else {
					Statement stmt = FrontEnd.parseAndNormalize("{}", Statement.class);
					CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(stmt);
					sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().addSection(stmt));
					sideEffectList.add(new AddedEnclosingBlock(compStmt));
					sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
					return sideEffectList;
				}
			} else {
				CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo()
						.getSectionList().get(0);
				if (Misc.collidesFreelyWith(targetNode, body)) {
					Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
					CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
					link.enclosingNonLeafNode.getInfo().getCFGInfo().removeSection(body);
					sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().addSection(compStmt));
					sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
					sideEffectList.add(new AddedEnclosingBlock(compStmt));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, body));
					return sideEffectList;
				} else {
					sideEffectList.addAll(InsertImmediateSuccessor
							.insert(body.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode));
					return sideEffectList;
				}
			}
		}

		@Override
		public List<SideEffect> visit(SectionsSectionBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the body of a SectionsConstruct, since the end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				link.enclosingNonLeafNode.getInfo().getCFGInfo().removeSection(body);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().addSection(compStmt)); // Note
																												// that
																												// this
																												// line
																												// looks
																												// same,
																												// but
																												// is
																												// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(SectionsEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SingleBeginLink link) {
			SingleConstructCFGInfo singleConsInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) singleConsInfo.getBody();
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt));
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, body));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediateSuccessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(SingleBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			SingleConstructCFGInfo singleStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) singleStmtInfo.getBody();
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the body of a SingleConstruct, since the end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(SingleEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(TaskBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			TaskConstructCFGInfo taskConsInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (taskConsInfo.hasFinalClause() || taskConsInfo.hasIfClause()) {
				String str = "Cannot insert a node after the beginNode of a TaskConstruct since it has some executable clauses.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			CompoundStatement body = (CompoundStatement) taskConsInfo.getBody();
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt));
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, body));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediateSuccessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(TaskClauseLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			TaskConstructCFGInfo taskConsInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();

			List<OmpClause> clauseList = taskConsInfo.getCFGClauseList();
			if (link.childNode != clauseList.get(clauseList.size() - 1)) {
				String str = "Cannot insert a node after the non-last clause of a TaskConstruct";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}

			CompoundStatement body = (CompoundStatement) taskConsInfo.getBody();
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt));
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, body));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediateSuccessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(TaskBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			TaskConstructCFGInfo taskConsStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) taskConsStmtInfo.getBody();
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the body of a TaskConstruct, since the end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(TaskEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(MasterBeginLink link) {
			MasterConstructCFGInfo masterConsInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) masterConsInfo.getBody();
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt));
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, body));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediateSuccessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(MasterBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			MasterConstructCFGInfo masterConsStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) masterConsStmtInfo.getBody();
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the body of a MasterConstruct, since the end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(MasterEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CriticalBeginLink link) {
			CriticalConstructCFGInfo criticalConsInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) criticalConsInfo.getBody();
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt));
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, body));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediateSuccessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(CriticalBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CriticalConstructCFGInfo criticalConsStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) criticalConsStmtInfo.getBody();
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the body of a CriticalConstruct, since the end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(CriticalEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(AtomicBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node inside the atomic construct.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(AtomicStatementLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node inside the atomic construct.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(AtomicEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(OrderedBeginLink link) {
			OrderedConstructCFGInfo orderedCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) orderedCFGInfo.getBody();
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt));
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, body));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediateSuccessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(OrderedBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			OrderedConstructCFGInfo orderedConsStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) orderedConsStmtInfo.getBody();
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the body of an OrderedConstruct, since the end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(OrderedEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CompoundBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatementCFGInfo compStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			sideEffectList.addAll(compStmtInfo.addElement(0, targetNode));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CompoundElementLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatementCFGInfo compStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			Node baseNode = link.childNode;
			if (!(baseNode instanceof JumpStatement)) {
				// TODO: Handle the insertion after the _implicit flush_ of the TaskConstruct
				// elements.
				int index = compStmtInfo.getElementList().indexOf(baseNode);
				sideEffectList.addAll(compStmtInfo.addElement(index + 1, targetNode));
				return sideEffectList;
			} else {
				if (baseNode instanceof ReturnStatement) {
					ReturnStatementInfo retStmtInfo = ((ReturnStatement) baseNode).getInfo();
					Expression retExp = retStmtInfo.getReturnExpression();
					if (retExp != null) {
						String str = "Cannot insert a node immediately after a return with expresison.";
						Misc.warnDueToLackOfFeature(str, null);
						sideEffectList.add(new SyntacticConstraint(link.childNode, str));
						return sideEffectList;
					}
					return InsertImmediatePredecessor.insert(baseNode, targetNode);
				} else if (baseNode instanceof BreakStatement) {
					return InsertImmediatePredecessor.insert(baseNode, targetNode);
				} else if (baseNode instanceof GotoStatement) {
					return InsertImmediatePredecessor.insert(baseNode, targetNode);
				} else if (baseNode instanceof ContinueStatement) {
					return InsertImmediatePredecessor.insert(baseNode, targetNode);
				} else {
					assert (false);
					return null;
				}
			}

		}

		@Override
		public List<SideEffect> visit(CompoundEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = link.enclosingNonLeafNode;
			if (!compStmt.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node before the end of a CompoundStatement, since its end node is not reachable. ";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;

			}
			CompoundStatementCFGInfo compInfo = compStmt.getInfo().getCFGInfo();
			sideEffectList.addAll(compInfo.addElement(compInfo.getElementList().size(), targetNode));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(IfBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(IfPredicateLink link) {
			/*
			 * Here, the predicate has exactly one successor.
			 */
			List<Node> predSuccessors = link.childNode.getInfo().getCFGInfo().getSuccBlocks();
			assert (predSuccessors.size() == 1);
			List<SideEffect> sideEffectList = new ArrayList<>();
			IfStatement ifStmt = link.enclosingNonLeafNode;
			IfStatementCFGInfo ifCFGInfo = ifStmt.getInfo().getCFGInfo();
			Node predSuccessor = predSuccessors.get(0);
			if (predSuccessor instanceof EndNode) {
				/*
				 * i.e., the ifStmt has no elseBody, and the predicate is false.
				 * Here, we add the else-body, and insert the targetNode in the
				 * else-body.
				 * If targetNode is a compound-statement, we do not need to
				 * create a new one.
				 */
				if (targetNode instanceof CompoundStatement) {
					sideEffectList.addAll(ifCFGInfo.setElseBody((Statement) targetNode));
					if (!Misc.changePerformed(sideEffectList)) {
						ifCFGInfo.removeElseBody();
						sideEffectList.add(new SyntacticConstraint(link.childNode, ""));
						return sideEffectList;
					}
					return sideEffectList;
				} else {
					CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
					sideEffectList.addAll(ifCFGInfo.setElseBody(compStmt));
					sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
					if (!Misc.changePerformed(sideEffectList)) {
						ifCFGInfo.removeElseBody();
						sideEffectList.add(new SyntacticConstraint(link.childNode, ""));
						return sideEffectList;
					}
					return sideEffectList;
				}
			} else {
				// i.e., the ifStmt has an elseBody, and the predicate can be false or true (but
				// a constant).
				assert (predSuccessor instanceof CompoundStatement)
						: "Following if-statement does not enclose its bodies: " + link.enclosingNonLeafNode.toString();

				CompoundStatement successorBody = (CompoundStatement) predSuccessor;
				boolean isTrue = (ifCFGInfo.getThenBody() == successorBody) ? true : false;
				if (Misc.collidesFreelyWith(targetNode, successorBody)) {
					Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
					CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
					if (isTrue) {
						sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setThenBody(compStmt));
					} else {
						sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setElseBody(compStmt));
					}
					sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
					sideEffectList.add(new AddedEnclosingBlock(compStmt));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, successorBody));
					if (!Misc.changePerformed(sideEffectList)) {
						sideEffectList.add(new SyntacticConstraint(link.childNode, ""));
						return sideEffectList;
					}
					return sideEffectList;
				} else {
					sideEffectList.addAll(InsertImmediateSuccessor
							.insert(successorBody.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode));
					if (!Misc.changePerformed(sideEffectList)) {
						sideEffectList.add(new SyntacticConstraint(link.childNode, ""));
						return sideEffectList;
					}
					return sideEffectList;
				}
			}
			// IfStatementCFGInfo ifStmtInfo =
			// link.enclosingNonLeafNode.getInfo().getCFGInfo();
			// CompoundStatement thenBody = (CompoundStatement) ifStmtInfo.getThenBody();
			// BeginNode thenBeginNode =
			// thenBody.getInfo().getCFGInfo().getNestedCFG().getBegin();
			// sideEffectList = InsertImmediateSuccessor.insertAggressive(thenBeginNode,
			// targetNode);
			//
			// Node copiedTarget = this.getCopiedTarget();
			// if (ifStmtInfo.hasElseBody()) {
			// CompoundStatement elseBody = (CompoundStatement) ifStmtInfo.getElseBody();
			// BeginNode elseBeginNode =
			// elseBody.getInfo().getCFGInfo().getNestedCFG().getBegin();
			// sideEffectList.addAll(InsertImmediateSuccessor.insertAggressive(elseBeginNode,
			// copiedTarget));
			// sideEffectList.add(UpdateSideEffects.ADDED_COPY);
			// return sideEffectList;
			// } else {
			// if (copiedTarget instanceof Declaration) {
			// CompoundStatement newElseBody = FrontEnd.parseAndNormalize("{}",
			// CompoundStatement.class);
			// sideEffectList.add(UpdateSideEffects.ADDED_ENCLOSING_BLOCK);
			// sideEffectList.addAll(ifStmtInfo.setElseBody(newElseBody));
			// sideEffectList.addAll(newElseBody.getInfo().getCFGInfo().addElement(0,
			// copiedTarget));
			// sideEffectList.add(UpdateSideEffects.ADDED_COPY);
			// return sideEffectList;
			// } else {
			// Statement targetStmt = (Statement) copiedTarget;
			// sideEffectList.addAll(ifStmtInfo.setElseBody(targetStmt));
			// return sideEffectList;
			// }
			// }
		}

		@Override
		public List<SideEffect> visit(IfThenBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			IfStatementCFGInfo ifStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement thenBody = (CompoundStatement) ifStmtInfo.getThenBody();
			if (!thenBody.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the then-body of an IfStatement, since the end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, thenBody)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setThenBody(compStmt)); // Note
																												// that
																												// this
																												// line
																												// looks
																												// same,
																												// but
																												// is
																												// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(thenBody));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(thenBody, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(thenBody.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(IfElseBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			IfStatementCFGInfo ifStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement elseBody = (CompoundStatement) ifStmtInfo.getElseBody();
			assert (elseBody != null);
			if (!elseBody.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the else-body of an IfStatement, since the end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, elseBody)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setElseBody(compStmt)); // Note
																												// that
																												// this
																												// line
																												// looks
																												// same,
																												// but
																												// is
																												// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(elseBody));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(elseBody, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(elseBody.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(IfEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SwitchBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);
			if (edges.stream()
					.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CASE_SOURCE
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_DEFAULT_SOURCE))) {
				sideEffectList.add(new JumpEdgeConstraint(targetNode));
				return sideEffectList;
			}
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SwitchPredicateLink link) {
			// Note that we will enter this method only when the number of successors is
			// one.
			assert (link.childNode.getInfo().getCFGInfo().getSuccessors().size() == 1);
			Node successorNode = link.childNode.getInfo().getCFGInfo().getSuccessors().get(0);
			if (successorNode instanceof EndNode) {
				// This implies that there is no default case in this switch-statement.
				// We will handle this case by adding a default statement.
				List<SideEffect> sideEffectList = new ArrayList<>();
				CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
				if (body.getInfo().getCFGInfo().isEndReachable()) {
					BreakStatement breakStmt = FrontEnd.parseAndNormalize("break;", BreakStatement.class);
					sideEffectList.addAll(body.getInfo().getCFGInfo().addAtLast(breakStmt));
				}
				Statement defaultStmt = FrontEnd.parseAndNormalize("default: ;", Statement.class);
				sideEffectList.addAll(body.getInfo().getCFGInfo().addAtLast(defaultStmt));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(defaultStmt, targetNode));
				return sideEffectList;

				// Old code:
				// Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);
				// if (edges.stream()
				// .anyMatch((e) -> (e.getTypeOfIncompleteness() ==
				// TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
				// || e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CASE_SOURCE
				// || e.getTypeOfIncompleteness() ==
				// TypeOfIncompleteness.UNKNOWN_DEFAULT_SOURCE))) {
				// sideEffectList.add(UpdateSideEffects.JUMPEDGE_CONSTRAINT);
				// sideEffectList.add(UpdateSideEffects.SYNTACTIC_CONSTRAINT);
				// return sideEffectList;
				// }
				// return InsertImmediateSuccessor.insertAggressive(link.enclosingNonLeafNode,
				// targetNode);
			} else {
				return InsertImmediatePredecessor.insert(successorNode, targetNode);
			}
		}

		@Override
		public List<SideEffect> visit(SwitchBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			SwitchStatementCFGInfo switchStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) switchStmtInfo.getBody();
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the body of a SwitchStatement since its end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(SwitchEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(WhileBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);

			if (edges.stream()
					.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
				sideEffectList.add(new JumpEdgeConstraint(targetNode));
				return sideEffectList;
			}
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(WhilePredicateLink link) {
			Integer predVal = Misc.evaluateInteger(link.childNode);
			if (predVal == null) {
				// Note that a WhilePredicate will have a single successor only if it is a
				// compile-time constant.
				// This visit shouldn't be entered if the predicate is not a compile-time
				// constant.
				assert (false);
				return null;
			} else {
				if (predVal == 0) {
					return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
				} else {
					WhileStatementCFGInfo whileCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
					CompoundStatement body = (CompoundStatement) whileCFGInfo.getBody();
					return InsertImmediateSuccessor.insert(body.getInfo().getCFGInfo().getNestedCFG().getBegin(),
							targetNode);
				}
			}
		}

		@Override
		public List<SideEffect> visit(WhileBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the body of a "
						+ "WhileStatement since its end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(WhileEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(DoBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);

			if (edges.stream()
					.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
				sideEffectList.add(new JumpEdgeConstraint(targetNode));
				return sideEffectList;
			}
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(DoPredicateLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			/*
			 * Shift up the predicate after all predecessors, with targetNode
			 * copied at appropriate places.
			 */
			Type predType = Type.getType(link.childNode);
			String tempName = Builder.getNewTempName("doPred");
			Declaration predDecl = predType.getDeclaration(tempName);
			sideEffectList.addAll(InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, predDecl));

			List<Node> predicatePredList = link.childNode.getInfo().getCFGInfo().getLeafPredecessors();
			Expression oldPredicate = link.childNode;
			Expression newPredicate = FrontEnd.parseAndNormalize("1", Expression.class);
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setPredicate(newPredicate);
			boolean first = true;
			for (Node predicatePred : predicatePredList) {
				if (first) {
					first = false;
					Statement predEval = FrontEnd.parseAndNormalize(tempName + " = " + oldPredicate + ";",
							Statement.class);
					sideEffectList.addAll(InsertImmediateSuccessor.insert(predicatePred, predEval));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(predEval, targetNode));
					Statement breakingIf = FrontEnd.parseAndNormalize("if (!" + tempName + ") {break;}",
							Statement.class);
					sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, breakingIf));
					continue;
				}
				Statement predEval = FrontEnd.parseAndNormalize(tempName + " = " + oldPredicate + ";", Statement.class);
				sideEffectList.addAll(InsertImmediateSuccessor.insert(predicatePred, predEval));
				Node copiedTarget = Builder.getCopiedTarget(targetNode);
				sideEffectList.add(new AddedCopy(copiedTarget));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(predEval, copiedTarget));
				Statement breakingIf = FrontEnd.parseAndNormalize("if (!" + tempName + ") {break;}", Statement.class);
				sideEffectList.addAll(InsertImmediateSuccessor.insert(copiedTarget, breakingIf));
			}

			return sideEffectList;

			// List<UpdateSideEffects> sideEffectList = new ArrayList<>();
			// DoStatementCFGInfo doStmtInfo =
			// link.enclosingNonLeafNode.getInfo().getCFGInfo();
			// CompoundStatement body = (CompoundStatement) doStmtInfo.getBody();
			// Node copiedTarget = Copier.getDeepCopy(targetNode);
			// sideEffectList = InsertImmediatePredecessor
			// .insertSimple(body.getInfo().getCFGInfo().getNestedCFG().getBegin(),
			// targetNode);
			// if (!sideEffectList.contains(UpdateSideEffects.SYNTACTIC_CONSTRAINT)) {
			// return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode,
			// copiedTarget);
			// }
			// return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(DoBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the body of a "
						+ "DoStatement since its end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(DoEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(ForBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);
			if (edges.stream()
					.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
				sideEffectList.add(new JumpEdgeConstraint(targetNode));
				return sideEffectList;
			}
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(ForInitLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);
			if (edges.stream()
					.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
				sideEffectList.add(new JumpEdgeConstraint(targetNode));
				return sideEffectList;
			}
			Statement initStmt = FrontEnd.parseAndNormalize(link.childNode + ";", Statement.class);
			ForStatementCFGInfo forInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			forInfo.removeInitExpression();
			sideEffectList.addAll(InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, initStmt));
			sideEffectList.addAll(InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForTermLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			List<Node> termSuccessors = link.childNode.getInfo().getCFGInfo().getSuccBlocks();
			assert (termSuccessors.size() == 1);
			Node termSuccessor = termSuccessors.get(0);
			CFGLink termSuccLink = CFGLinkFinder.getCFGLinkFor(termSuccessor);
			if (termSuccLink instanceof ForEndLink) {
				Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);
				if (edges.stream()
						.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
								|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
					sideEffectList.add(new JumpEdgeConstraint(targetNode));
					return sideEffectList;
				}
				ForStatement forStmt = link.enclosingNonLeafNode;
				return InsertImmediateSuccessor.insert(forStmt, targetNode);
			} else {
				assert (termSuccLink instanceof ForBodyLink);
				CompoundStatement body = (CompoundStatement) ((ForBodyLink) termSuccLink).childNode;
				if (Misc.collidesFreelyWith(targetNode, body)) {
					Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
					CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
					sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt));
					sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
					sideEffectList.add(new AddedEnclosingBlock(compStmt));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, body));
					return sideEffectList;
				} else {
					sideEffectList.addAll(InsertImmediateSuccessor
							.insert(body.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode));
					return sideEffectList;
				}
			}
			// ForStatementCFGInfo forStmtInfo =
			// link.enclosingNonLeafNode.getInfo().getCFGInfo();
			// CompoundStatement compStmt = (CompoundStatement) forStmtInfo.getBody();
			// Node copiedTarget = Copier.getDeepCopy(targetNode);
			// sideEffectList = InsertImmediateSuccessor
			// .insertAggressive(compStmt.getInfo().getCFGInfo().getNestedCFG().getBegin(),
			// targetNode);
			// if (!sideEffectList.contains(UpdateSideEffects.SYNTACTIC_CONSTRAINT)) {
			// return InsertImmediateSuccessor.insertAggressive(link.enclosingNonLeafNode,
			// copiedTarget);
			// }
			// return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForStepLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();

			List<Node> predList = new ArrayList<>(link.childNode.getInfo().getCFGInfo().getLeafPredecessors());
			CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();

			/*
			 * Before attempting to perform aggressive insertion, verify that
			 * the insertion is allowed.
			 */
			boolean allowed = true;
			for (Node pred : predList) {
				if (pred instanceof ContinueStatement) {
					if (Misc.collidesFreelyWithinScopesOf(targetNode, body, pred)) {
						allowed = false;
					}
					if (Misc.collidesFreelyWithinScopesOf(link.childNode, body, pred)) {
						allowed = false;
					}
				}
			}
			if (!allowed) {
				sideEffectList.add(new NoUpdateDueToNameCollision(targetNode));
				return sideEffectList;
			}

			ForStatementCFGInfo forInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			forInfo.removeStepExpression();
			Node copiedTarget = Builder.getCopiedTarget(targetNode);
			Statement stepStmt = FrontEnd.parseAndNormalize(link.childNode + ";", Statement.class);
			Node firstPred = predList.get(0);
			if (firstPred instanceof ContinueStatement) {
				link.enclosingNonLeafNode.getInfo().getCFGInfo().removeStepExpression();
				sideEffectList.addAll(InsertImmediatePredecessor.insert(firstPred, stepStmt));
				sideEffectList.addAll(InsertImmediatePredecessor.insert(firstPred, targetNode));
			} else {
				assert (firstPred.getParent() == body);
				if (Misc.collidesFreelyWith(stepStmt, body)) {
					Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
					CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
					sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note
																												// that
																												// this
																												// line
																												// looks
																												// same,
																												// but
																												// is
																												// different.
					sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(body, stepStmt));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(stepStmt, targetNode));
					sideEffectList.add(new AddedEnclosingBlock(compStmt));
				} else {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(firstPred, stepStmt));
					if (Misc.collidesFreelyWith(targetNode, body)) {
						Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
						CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
						sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note
																													// that
																													// this
																													// line
																													// looks
																													// same,
																													// but
																													// is
																													// different.
						sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
						sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
						sideEffectList.add(new AddedEnclosingBlock(compStmt));
					} else {
						sideEffectList.addAll(InsertImmediatePredecessor.insert(firstPred, targetNode));
					}
				}
			}
			int i = 0;
			for (Node pred : predList) {
				if (i++ == 0) {
					continue;
				}
				copiedTarget = Builder.getCopiedTarget(copiedTarget);
				sideEffectList.add(new AddedCopy(copiedTarget));
				if (pred instanceof ContinueStatement) {
					stepStmt = FrontEnd.parseAndNormalize(link.childNode + ";", Statement.class);
					sideEffectList.addAll(InsertImmediatePredecessor.insert(pred, stepStmt));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(pred, copiedTarget));
				} else {
					assert (pred.getParent() == body);
					stepStmt = FrontEnd.parseAndNormalize(link.childNode + ";", Statement.class);
					if (Misc.collidesFreelyWith(stepStmt, body)) {
						Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
						CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
						sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note
																													// that
																													// this
																													// line
																													// looks
																													// same,
																													// but
																													// is
																													// different.
						sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
						sideEffectList.addAll(InsertImmediateSuccessor.insert(body, stepStmt));
						sideEffectList.addAll(InsertImmediateSuccessor.insert(stepStmt, copiedTarget));
						sideEffectList.add(new AddedEnclosingBlock(compStmt));
					} else {
						sideEffectList.addAll(InsertImmediatePredecessor.insert(pred, stepStmt));
						if (Misc.collidesFreelyWith(copiedTarget, body)) {
							Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
							CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
							sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note
																														// that
																														// this
																														// line
																														// looks
																														// same,
																														// but
																														// is
																														// different.
							sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
							sideEffectList.addAll(InsertImmediateSuccessor.insert(body, copiedTarget));
							sideEffectList.add(new AddedEnclosingBlock(compStmt));
						} else {
							sideEffectList.addAll(InsertImmediatePredecessor.insert(pred, copiedTarget));
						}
					}
				}
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (!body.getInfo().getCFGInfo().isEndReachable()) {
				String str = "Cannot insert a node immediately after the boyd of a "
						+ "ForStatement, as its end is not reachable.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(body, targetNode));
				sideEffectList.add(new AddedEnclosingBlock(compStmt));
				return sideEffectList;
			} else {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(ForEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CallBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CallPreLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the preCallNode of a CallStatement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CallPostLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CallEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

	}

	private static class SimpleInsertImmediateSuccessor extends GJNoArguCFGLinkVisitor<List<SideEffect>> {
		protected Node targetNode;

		public SimpleInsertImmediateSuccessor(Node targetNode) {
			this.targetNode = targetNode;
		}

		@Override
		public List<SideEffect> visit(FunctionBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (!(targetNode instanceof ParameterDeclaration)) {
				String str = "Cannot insert a parameter declaration.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
			FunctionDefinition funcDef = link.enclosingNonLeafNode;
			funcDef.getInfo().getCFGInfo().addParameterDeclaration(0, (ParameterDeclaration) targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(FunctionParameterLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			FunctionDefinition funcDef = link.enclosingNonLeafNode;
			if (targetNode instanceof ParameterDeclaration) {
				int index = funcDef.getInfo().getCFGInfo().getParameterDeclarationList().indexOf(link.childNode);
				funcDef.getInfo().getCFGInfo().addParameterDeclaration(index + 1, (ParameterDeclaration) targetNode);
				return sideEffectList;
			} else if (targetNode instanceof Statement) {
				CompoundStatement funcBody = funcDef.getInfo().getCFGInfo().getBody();
				return InsertImmediateSuccessor.insertSimple(funcBody.getInfo().getCFGInfo().getNestedCFG().getBegin(),
						targetNode);
			} else {
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(FunctionBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the function body, in Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(FunctionEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the function body, in Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ParallelBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ParallelConstruct parConstruct = link.enclosingNonLeafNode;
			ParallelConstructCFGInfo parConsInfo = parConstruct.getInfo().getCFGInfo();
			if (targetNode instanceof IfClause) {
				parConsInfo.setIfClause((IfClause) targetNode);
				return sideEffectList;
			} else if (targetNode instanceof NumThreadsClause) {
				parConsInfo.setNumThreadsClause((NumThreadsClause) targetNode);
			} else if (targetNode instanceof Statement) {
				if (parConsInfo.hasIfClause() || parConsInfo.hasNumThreadsClause()) {
					return sideEffectList;
				} else {
					Statement body = parConsInfo.getBody();
					return InsertImmediateSuccessor.insertSimple(body.getInfo().getCFGInfo().getNestedCFG().getBegin(),
							targetNode);
				}
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ParallelClauseLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ParallelConstruct parConstruct = link.enclosingNonLeafNode;
			ParallelConstructCFGInfo parConsInfo = parConstruct.getInfo().getCFGInfo();
			if (targetNode instanceof IfClause) {
				parConsInfo.setIfClause((IfClause) targetNode);
				return sideEffectList;
			} else if (targetNode instanceof NumThreadsClause) {
				parConsInfo.setNumThreadsClause((NumThreadsClause) targetNode);
			} else if (targetNode instanceof Statement) {
				if (parConsInfo.hasIfClause() || parConsInfo.hasNumThreadsClause()) {
					return sideEffectList;
				} else {
					Statement body = parConsInfo.getBody();
					return InsertImmediateSuccessor.insertSimple(body.getInfo().getCFGInfo().getNestedCFG().getBegin(),
							targetNode);
				}
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ParallelBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the body of a parallel construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ParallelEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(OmpForBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the BeginNode of a parallel for construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForInitLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the initialization expression of a parallel for construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForTermLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ForConstructCFGInfo forConsInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			Node copiedTarget = Copier.getDeepCopy(targetNode);
			sideEffectList = InsertImmediateSuccessor
					.insertSimple(forConsInfo.getBody().getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode);
			if (!sideEffectList.stream().anyMatch(s -> s instanceof SyntacticConstraint)) {
				return sideEffectList;
			} else {
				return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, copiedTarget);
			}
		}

		@Override
		public List<SideEffect> visit(OmpForStepLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the update expression of a parallel for construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the body of a parallel for construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SectionsBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			SectionsConstructCFGInfo secConsInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			for (Statement sectionCons : secConsInfo.getSectionList()) {
				assert (sectionCons instanceof CompoundStatement);
				Node copiedTarget = Copier.getDeepCopy(targetNode);
				((CompoundStatementCFGInfo) sectionCons.getInfo().getCFGInfo()).addElement(0, copiedTarget);
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(SectionsSectionBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after a Section of a Sections construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(SectionsEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SingleBeginLink link) {
			SingleConstructCFGInfo singleConsInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			return InsertImmediateSuccessor.insertSimple(
					singleConsInfo.getBody().getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode);
		}

		@Override
		public List<SideEffect> visit(SingleBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the body of a Single construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(SingleEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(TaskBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			TaskConstructCFGInfo taskConsInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (targetNode instanceof IfClause) {
				taskConsInfo.setIfClause((IfClause) targetNode);
			} else if (targetNode instanceof FinalClause) {
				taskConsInfo.setFinalClause((FinalClause) targetNode);
			} else if (targetNode instanceof Statement) {
				return InsertImmediateSuccessor.insertSimple(
						taskConsInfo.getBody().getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode);
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(TaskClauseLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			TaskConstructCFGInfo taskConsInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (targetNode instanceof IfClause) {
				taskConsInfo.setIfClause((IfClause) targetNode);
			} else if (targetNode instanceof FinalClause) {
				taskConsInfo.setFinalClause((FinalClause) targetNode);
			} else if (targetNode instanceof Statement) {
				return InsertImmediateSuccessor.insertSimple(
						taskConsInfo.getBody().getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode);
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(TaskBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the body of a task construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(TaskEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(MasterBeginLink link) {
			MasterConstructCFGInfo masterConsInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			return InsertImmediateSuccessor.insertSimple(
					masterConsInfo.getBody().getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode);
		}

		@Override
		public List<SideEffect> visit(MasterBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the body of a master construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(MasterEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CriticalBeginLink link) {
			CriticalConstructCFGInfo criticalConsInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			return InsertImmediateSuccessor.insertSimple(
					criticalConsInfo.getBody().getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode);
		}

		@Override
		public List<SideEffect> visit(CriticalBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the body of a critical construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CriticalEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(AtomicBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node inside the atomic construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(AtomicStatementLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node inside the atomic construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(AtomicEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(OrderedBeginLink link) {
			OrderedConstructCFGInfo orderedCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			return InsertImmediateSuccessor.insertSimple(
					orderedCFGInfo.getBody().getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode);
		}

		@Override
		public List<SideEffect> visit(OrderedBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the body of an ordered construct, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OrderedEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CompoundBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatementCFGInfo compStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			compStmtInfo.addElement(0, targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CompoundElementLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatementCFGInfo compStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			Node baseNode = link.childNode;
			if (!(baseNode instanceof JumpStatement)) {
				int index = compStmtInfo.getElementList().indexOf(baseNode);
				sideEffectList.addAll(compStmtInfo.addElement(index + 1, targetNode));
				return sideEffectList;
			} else {
				if (baseNode instanceof ReturnStatement) {
					ReturnStatementInfo retStmtInfo = ((ReturnStatement) baseNode).getInfo();
					Expression retExp = retStmtInfo.getReturnExpression();
					if (retExp != null) {
						String str = "Cannot insert a node immediately after a return with expresison.";
						Misc.warnDueToLackOfFeature(str, null);
						sideEffectList.add(new SyntacticConstraint(link.childNode, str));
						return sideEffectList;
					}
					return InsertImmediatePredecessor.insertSimple(baseNode, targetNode);
				} else if (baseNode instanceof BreakStatement) {
					return InsertImmediatePredecessor.insertSimple(baseNode, targetNode);
				} else if (baseNode instanceof GotoStatement) {
					return InsertImmediatePredecessor.insertSimple(baseNode, targetNode);
				} else if (baseNode instanceof ContinueStatement) {
					return InsertImmediatePredecessor.insertSimple(baseNode, targetNode);
				} else {
					assert (false);
					return null;
				}
			}
		}

		@Override
		public List<SideEffect> visit(CompoundEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(IfBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the BeginNode of an if-statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(IfPredicateLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			IfStatementCFGInfo ifStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			Node copiedTarget = Copier.getDeepCopy(targetNode);
			sideEffectList = InsertImmediateSuccessor.insertSimple(
					ifStmtInfo.getThenBody().getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode);
			if (!sideEffectList.stream().anyMatch(s -> s instanceof SyntacticConstraint)) {
				return InsertImmediateSuccessor.insertSimple(
						ifStmtInfo.getElseBody().getInfo().getCFGInfo().getNestedCFG().getBegin(), copiedTarget);
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(IfThenBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the then-body of an IfStatement";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(IfElseBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the else-body of an IfStatement";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(IfEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SwitchBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the BeginNode of a SwitchStatement";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(SwitchPredicateLink link) {
			SwitchStatementCFGInfo switchCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			return InsertImmediateSuccessor
					.insertSimple(switchCFGInfo.getBody().getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode);
		}

		@Override
		public List<SideEffect> visit(SwitchBodyLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SwitchEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(WhileBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the BeginNode of a while statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(WhilePredicateLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			WhileStatementCFGInfo whileCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			Node copiedTarget = Copier.getDeepCopy(targetNode);
			sideEffectList = InsertImmediateSuccessor
					.insertSimple(whileCFGInfo.getBody().getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode);
			if (!sideEffectList.stream().anyMatch(s -> s instanceof SyntacticConstraint)) {
				return insertSimple(link.enclosingNonLeafNode, copiedTarget);
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(WhileBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the body of a while statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(WhileEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(DoBeginLink link) {
			DoStatementCFGInfo doStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) doStmtInfo.getBody();
			return InsertImmediateSuccessor.insertSimple(body.getInfo().getCFGInfo().getNestedCFG().getBegin(),
					targetNode);
		}

		@Override
		public List<SideEffect> visit(DoPredicateLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			DoStatementCFGInfo doStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) doStmtInfo.getBody();
			Node copiedTarget = Copier.getDeepCopy(targetNode);
			sideEffectList = InsertImmediatePredecessor
					.insertSimple(body.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode);
			if (!sideEffectList.stream().anyMatch(s -> s instanceof SyntacticConstraint)) {
				return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, copiedTarget);
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(DoBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the body of a do-statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(DoEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(ForBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the BeginNode of a for statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForInitLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the init-expression of a for statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForTermLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ForStatementCFGInfo forStmtInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			CompoundStatement compStmt = (CompoundStatement) forStmtInfo.getBody();
			Node copiedTarget = Copier.getDeepCopy(targetNode);
			sideEffectList = InsertImmediateSuccessor
					.insertSimple(compStmt.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode);
			if (!sideEffectList.stream().anyMatch(s -> s instanceof SyntacticConstraint)) {
				return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, copiedTarget);
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForStepLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the step-expression of a for statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the body of a for statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CallBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the begin node of a call statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CallPreLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the preCallNode of a call statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CallPostLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after the postCallNode of a call statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CallEndLink link) {
			return InsertImmediateSuccessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(GotoLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after a goto statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(BreakLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after a break statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ContinueLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after a continue statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ReturnLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately after a return statement, in the Simple mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}
	}
}
