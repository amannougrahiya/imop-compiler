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
import imop.ast.annotation.Label;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.typeSystem.FunctionType;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.analysis.typeSystem.VoidType;
import imop.lib.builder.Builder;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.info.CompoundStatementCFGInfo;
import imop.lib.cfg.info.ForStatementCFGInfo;
import imop.lib.cfg.info.ParallelConstructCFGInfo;
import imop.lib.cfg.info.SectionsConstructCFGInfo;
import imop.lib.cfg.link.baseVisitor.GJNoArguCFGLinkVisitor;
import imop.lib.cfg.link.node.*;
import imop.lib.transform.updater.InsertOnTheEdge.PivotPoint;
import imop.lib.transform.updater.NodeRemover.LabelShiftingMode;
import imop.lib.transform.updater.sideeffect.*;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InsertImmediatePredecessor {
	public static List<SideEffect> insertSimple(Node baseNode, Node targetNode) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		baseNode = Misc.getCFGNodeFor(baseNode);
		targetNode = Misc.getCFGNodeFor(targetNode);

		CFGLink cfgLink = CFGLinkFinder.getCFGLinkFor(baseNode);
		if (cfgLink == null) {
			Misc.warnDueToLackOfFeature(
					"Cannot insert a node immediately before some other node, due to lack of an appropriate enclosing node.",
					baseNode);
			sideEffectList.add(new MissingCFGParent(baseNode));
			return sideEffectList;
		}
		return cfgLink.accept(new SimpleInsertImmediatePredecessor(targetNode));
	}

	public static List<SideEffect> insert(Node baseNode, Node targetNode) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		baseNode = Misc.getCFGNodeFor(baseNode);
		CFGLink cfgLink = CFGLinkFinder.getCFGLinkFor(baseNode);
		if (cfgLink == null) {
			Misc.warnDueToLackOfFeature(
					"Cannot insert a node immediately before some basenode, due to lack of an appropriate enclosing node for the basenode.",
					baseNode);
			sideEffectList.add(new MissingCFGParent(baseNode));
			return sideEffectList;
		}

		targetNode = Misc.getCFGNodeFor(targetNode);
		if (!(targetNode instanceof Statement) && !(targetNode instanceof Declaration)) {
			String str = "Can only insert statements and declarations in aggressive mode. "
					+ "Use elementary methods instead, for other types of insertions.";
			Misc.warnDueToLackOfFeature(str, baseNode);
			sideEffectList.add(new SyntacticConstraint(baseNode, str));
			return sideEffectList;
		}

		List<Node> basePredecessors = baseNode.getInfo().getCFGInfo().getPredecessors();

		Node nonBeginBase = (baseNode instanceof BeginNode) ? baseNode.getParent() : baseNode;
		if (basePredecessors.isEmpty() && !(nonBeginBase instanceof CompoundStatement)) {
			String str = "Cannot insert a node before a disconnected node " + baseNode;
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(baseNode, str));
			return sideEffectList;
		}

		// To avoid creating multiple copies of the target, instead of simply shifting
		// the labels,
		// we have added a logical OR with a new second operand here.
		if (basePredecessors.size() <= 1 || cfgLink instanceof CompoundElementLink || cfgLink instanceof DoBodyLink
				|| cfgLink instanceof ForBodyLink || cfgLink instanceof ForTermLink || cfgLink instanceof ForEndLink
				|| cfgLink instanceof WhileEndLink || cfgLink instanceof DoEndLink || cfgLink instanceof SwitchEndLink
				|| cfgLink instanceof OmpForTermLink || cfgLink instanceof IfEndLink) {
			sideEffectList = cfgLink.accept(new AggressiveInsertImmediatePredecessor(targetNode));
			return sideEffectList;
		} else {
			Node copiedTarget = Builder.getCopiedTarget(targetNode);
			List<Node> unrollTargets = new ArrayList<>();
			basePredecessors = new ArrayList<>(baseNode.getInfo().getCFGInfo().getPredecessors());
			boolean first = true;
			for (Node basePredecessor : basePredecessors) {
				if (first) {
					first = false;
					sideEffectList.addAll(InsertOnTheEdge.insertAggressive(basePredecessor, baseNode, targetNode,
							PivotPoint.DESTINATION));
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
				sideEffectList.addAll(InsertOnTheEdge.insertAggressive(basePredecessor, baseNode, copiedTarget,
						PivotPoint.DESTINATION));
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

	private static class AggressiveInsertImmediatePredecessor extends SimpleInsertImmediatePredecessor {
		private AggressiveInsertImmediatePredecessor(Node targetNode) {
			super(targetNode);
		}

		@Override
		public List<SideEffect> visit(FunctionBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node before the beginNode of a FunctionDefinition.";
			Misc.warnDueToLackOfFeature(str, link.enclosingNonLeafNode);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(FunctionParameterLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node before the parameters of a FunctionDefinition.";
			Misc.warnDueToLackOfFeature(str, link.enclosingNonLeafNode);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(FunctionBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			FunctionDefinition functionDef = link.enclosingNonLeafNode;
			CompoundStatement body = functionDef.getInfo().getCFGInfo().getBody();
			sideEffectList.addAll(body.getInfo().getCFGInfo().addElement(0, targetNode));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(FunctionEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			FunctionDefinition functionDef = link.enclosingNonLeafNode;
			EndNode endNode = functionDef.getInfo().getCFGInfo().getNestedCFG().getEnd();
			List<Node> endPredList = endNode.getInfo().getCFGInfo().getPredBlocks();

			assert (endPredList.size() == 1); // Since insertAggressive() would not call this method if this is not the
												// case.
			Node predecessor = endPredList.get(0);
			if (predecessor instanceof CompoundStatement) {
				EndNode bodyEnd = ((CompoundStatement) predecessor).getInfo().getCFGInfo().getNestedCFG().getEnd();
				return InsertImmediatePredecessor.insert(bodyEnd, targetNode);
			} else {
				assert (predecessor instanceof ReturnStatement);
				ReturnStatement retStmt = (ReturnStatement) predecessor;
				if (retStmt.getInfo().getReturnExpression() == null) {
					return InsertImmediatePredecessor.insert(retStmt, targetNode);
				} else {
					String str = "Cannot insert a node after a returnStatement that returns a non-void expression.";
					Misc.warnDueToLackOfFeature(str, null);
					sideEffectList.add(new SyntacticConstraint(link.childNode, str));
					return sideEffectList;
				}
			}
		}

		@Override
		public List<SideEffect> visit(ParallelBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(ParallelClauseLink link) {
			// if (targetNode instanceof IfClause) {
			// parCFGInfo.setIfClause((IfClause) targetNode);
			// return sideEffectList;
			// } else if (targetNode instanceof NumThreadsClause) {
			// parCFGInfo.setNumThreadsClause((NumThreadsClause) targetNode);
			// return sideEffectList;
			// } else {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
			// }
		}

		@Override
		public List<SideEffect> visit(ParallelBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			// ParallelConstructCFGInfo parCFGInfo =
			// link.enclosingNonLeafNode.getInfo().getCFGInfo();
			// if (targetNode instanceof IfClause) {
			// parCFGInfo.setIfClause((IfClause) targetNode);
			// return sideEffectList;
			// } else if (targetNode instanceof NumThreadsClause) {
			// parCFGInfo.setNumThreadsClause((NumThreadsClause) targetNode);
			// return sideEffectList;
			// }
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
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
		public List<SideEffect> visit(ParallelEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ParallelConstructCFGInfo parCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (!(targetNode instanceof Statement)) {
				return sideEffectList;
			}
			Node rawBody = parCFGInfo.getBody();
			assert (rawBody instanceof CompoundStatement);
			CompoundStatement body = (CompoundStatement) rawBody;
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
		public List<SideEffect> visit(OmpForBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(OmpForInitLink link) {
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
		public List<SideEffect> visit(OmpForTermLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert any node immediately before the termination condition of a parallel for loop.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForStepLink link) {
			// Note that this method would be called only if we have one predecessor.
			List<Node> leafPredList = link.childNode.getInfo().getCFGInfo().getLeafPredecessors();
			assert (leafPredList.size() == 1);
			Node pred = leafPredList.get(0);
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (pred instanceof EndNode) {
				CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
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
					return sideEffectList;
				} else {
					sideEffectList.addAll(InsertImmediatePredecessor
							.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
					return sideEffectList;
				}
			} else {
				assert (pred instanceof ContinueStatement);
				CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
				if (Misc.collidesFreelyWithinScopesOf(targetNode, body, pred)) {
					sideEffectList.add(new NoUpdateDueToNameCollision(targetNode));
					return sideEffectList;
				} else {
					return InsertImmediatePredecessor.insert(leafPredList.get(0), targetNode);
				}
			}
		}

		@Override
		public List<SideEffect> visit(OmpForBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
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
		public List<SideEffect> visit(OmpForEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert any node immediately before the end node of an omp for loop. Who would handle the clauses?";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(SectionsBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SectionsSectionBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
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
		public List<SideEffect> visit(SectionsEndLink link) {
			// Note that this method would never be called, unless we have exactly one
			// predecessor.
			assert (link.childNode.getInfo().getCFGInfo().getLeafPredecessors().size() == 1);
			List<SideEffect> sideEffectList = new ArrayList<>();
			SectionsConstructCFGInfo cfgInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (cfgInfo.getSectionList().isEmpty()) {
				Node pred = link.childNode.getInfo().getCFGInfo().getLeafPredecessors().get(0);
				assert (pred instanceof BeginNode);
				/*
				 * Add a new section, and insert the targetNode in it.
				 */
				Statement stmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(stmt);
				sideEffectList.addAll(cfgInfo.addSection(stmt));
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
				return sideEffectList;
			} else {
				Statement sectionBody = cfgInfo.getSectionList().get(0);
				assert (sectionBody instanceof CompoundStatement);
				CompoundStatement body = (CompoundStatement) sectionBody;
				if (Misc.collidesFreelyWith(targetNode, body)) {
					Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
					CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
					link.enclosingNonLeafNode.getInfo().getCFGInfo().removeSection(body); // Note that this line looks
																							// same, but is different.
					link.enclosingNonLeafNode.getInfo().getCFGInfo().addSection(compStmt);
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
		}

		@Override
		public List<SideEffect> visit(SingleBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SingleBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
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
		public List<SideEffect> visit(SingleEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			SingleConstruct singleConstruct = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) singleConstruct.getInfo().getCFGInfo().getBody();
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
		public List<SideEffect> visit(TaskBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(TaskClauseLink link) {
			// if (targetNode instanceof IfClause) {
			// taskConstruct.getInfo().getCFGInfo().setIfClause((IfClause) targetNode);
			// return sideEffectList;
			// } else if (targetNode instanceof FinalClause) {
			// taskConstruct.getInfo().getCFGInfo().setFinalClause((FinalClause)
			// targetNode);
			// return sideEffectList;
			// } else {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
			// }
		}

		@Override
		public List<SideEffect> visit(TaskBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
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
		public List<SideEffect> visit(TaskEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			TaskConstruct taskConstruct = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) taskConstruct.getInfo().getCFGInfo().getBody();
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
		public List<SideEffect> visit(MasterBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(MasterBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
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
		public List<SideEffect> visit(MasterEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			MasterConstruct masterConstruct = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) masterConstruct.getInfo().getCFGInfo().getBody();
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
		public List<SideEffect> visit(CriticalBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CriticalBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
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
		public List<SideEffect> visit(CriticalEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CriticalConstruct criticalConstruct = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) criticalConstruct.getInfo().getCFGInfo().getBody();
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
		public List<SideEffect> visit(AtomicBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(AtomicStatementLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the expression-statement of an atomic construct.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(AtomicEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node inside the body of an atomic construct.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OrderedBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(OrderedBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
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
		public List<SideEffect> visit(OrderedEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			OrderedConstruct orderedConstruct = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) orderedConstruct.getInfo().getCFGInfo().getBody();
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
		public List<SideEffect> visit(CompoundBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = link.enclosingNonLeafNode;
			CompoundStatementCFGInfo bodyInfo = body.getInfo().getCFGInfo();
			sideEffectList.addAll(bodyInfo.addElement(0, targetNode));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CompoundElementLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = link.enclosingNonLeafNode;

			Node baseNode = link.childNode;
			List<Node> elementList = compStmt.getInfo().getCFGInfo().getElementList();
			int baseIndex = elementList.indexOf(baseNode);

			if (baseNode instanceof Declaration) {
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(baseIndex, targetNode));
				return sideEffectList;
			} else if (targetNode instanceof Declaration) {
				Declaration decl = (Declaration) targetNode;
				// New code in next two lines.
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(baseIndex, targetNode));
				return sideEffectList;
				// OLD CODE: Now, normalization of the initializer is done automatically within
				// addElement().
				// Initializer init = decl.getInfo().getInitializer();
				// if (init == null) {
				// sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(baseIndex,
				// targetNode));
				// return sideEffectList;
				// } else {
				// // Too bad! Now, we need to simplify the declaration, and adjust the indices.
				// Type typeOfInit = Type.getType(init);
				// String tempName = Builder.getNewTempName(decl.getInfo().getDeclaredName() +
				// "Init");
				// Declaration simplifyingTemp = typeOfInit.getDeclaration(tempName);
				// sideEffectList.addAll(InsertImmediatePredecessor.insertAggressive(link.childNode,
				// simplifyingTemp));
				// sideEffectList.add(new InitializationSimplified(simplifyingTemp));
				//
				// Statement simplifyingExp = FrontEnd.parseAndNormalize(tempName + " = " +
				// init.toString() + ";",
				// Statement.class);
				// sideEffectList.addAll(InsertImmediatePredecessor.insertAggressive(link.childNode,
				// simplifyingExp));
				// sideEffectList.add(new InitializationSimplified(simplifyingExp));
				//
				// Declarator declarator =
				// decl.getInfo().getDeclarator(decl.getInfo().getDeclaredName());
				// String newDeclString = decl.getF0().toString() + " " + declarator + " = " +
				// tempName + ";";
				// Declaration newDecl = FrontEnd.parseAndNormalize(newDeclString,
				// Declaration.class);
				// sideEffectList.add(new NodeUpdated(newDecl, "Initializer of this declaration
				// was simplified."));
				// baseIndex =
				// compStmt.getInfo().getCFGInfo().getElementList().indexOf(link.childNode);
				// sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(baseIndex,
				// newDecl));
				// return sideEffectList;
				// }
			} else {
				Statement baseStmt = (Statement) baseNode;
				Statement targetStmt = (Statement) targetNode;
				List<Label> baseLabels = new ArrayList<>(baseStmt.getInfo().getLabelAnnotations());
				baseStmt.getInfo().clearLabelAnnotations();
				sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(baseIndex, targetNode));
				for (Label lab : baseLabels) {
					Label newLabel = Label.getCopy(lab);
					targetStmt.getInfo().addLabelAnnotation(newLabel);
				}
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(CompoundEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = link.enclosingNonLeafNode;
			CompoundStatementCFGInfo bodyInfo = compStmt.getInfo().getCFGInfo();
			sideEffectList.addAll(bodyInfo.addElement(bodyInfo.getElementList().size(), targetNode));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(IfBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(IfPredicateLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(IfThenBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getThenBody();
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (Misc.collidesFreelyWith(targetNode, body)) {
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
		public List<SideEffect> visit(IfElseBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getElseBody();
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (Misc.collidesFreelyWith(targetNode, body)) {
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
		public List<SideEffect> visit(IfEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SwitchBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SwitchPredicateLink link) {
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
		public List<SideEffect> visit(SwitchBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
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
		public List<SideEffect> visit(SwitchEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);

			if (edges.stream()
					.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CASE_SOURCE
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_DEFAULT_SOURCE))) {
				sideEffectList.add(new JumpEdgeConstraint(targetNode));
				return sideEffectList;
			}
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
			// List<UpdateSideEffects> sideEffectList = new ArrayList<>();
			// SwitchStatement switchStmt = link.enclosingNonLeafNode;
			// EndNode switchEnd = link.childNode;
			// List<Node> endPreds = switchEnd.getInfo().getCFGInfo().getPredBlocks();
			// if (endPreds.isEmpty()) {
			// Misc.warnDueToLackOfFeature(
			// "Cannot insert a node immediately before the endNode of a SwitchStatement,
			// since it is not reachable.",
			// null);
			// sideEffectList.add(UpdateSideEffects.SYNTACTIC_CONSTRAINT);
			// return sideEffectList;
			// }
			// assert (endPreds.size() == 1);
			// Node endPred = endPreds.get(0);
			// if (endPred instanceof Expression) {
			// assert (!switchStmt.getInfo().hasDefaultLabel());
			// if (targetNode instanceof Declaration) {
			// Misc.warnDueToLackOfFeature("Cannot insert a declaration before the endNode
			// of a SwitchStatement.",
			// null);
			// sideEffectList.add(UpdateSideEffects.SYNTACTIC_CONSTRAINT);
			// return sideEffectList;
			// } else {
			// Statement targetStmt = (Statement) targetNode;
			// DefaultLabel defLabel = new DefaultLabel(targetStmt, switchStmt);
			// targetStmt.getInfo().addLabelAnnotation(defLabel);
			// CompoundStatement body = (CompoundStatement)
			// link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
			// CompoundStatementCFGInfo bodyInfo = body.getInfo().getCFGInfo();
			// sideEffectList.addAll(bodyInfo.addElement(bodyInfo.getElementList().size(),
			// targetStmt));
			// return sideEffectList;
			// }
			// } else if (endPred instanceof BreakStatement) {
			// return InsertImmediatePredecessor.insertAggressive(endPred, targetNode);
			// } else {
			// assert (endPred instanceof CompoundStatement);
			// CompoundStatement body = (CompoundStatement) endPred;
			// assert (body.getInfo().getCFGInfo().isEndReachable());
			// CompoundStatementCFGInfo bodyInfo = body.getInfo().getCFGInfo();
			// sideEffectList.addAll(bodyInfo.addElement(bodyInfo.getElementList().size(),
			// targetNode));
			// return sideEffectList;
			// }
		}

		@Override
		public List<SideEffect> visit(WhileBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(WhilePredicateLink link) {
			// Note that this method would be called only if we have one predecessor.
			List<SideEffect> sideEffectList = new ArrayList<>();
			Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);

			if (edges.stream()
					.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
				sideEffectList.add(new JumpEdgeConstraint(targetNode));
				return sideEffectList;
			}
			List<Node> leafPredList = link.childNode.getInfo().getCFGInfo().getLeafPredecessors();
			assert (leafPredList.size() == 1);
			Node pred = leafPredList.get(0);
			assert (pred instanceof BeginNode);
			return InsertImmediatePredecessor.insert(pred, targetNode);
			//
			// List<UpdateSideEffects> sideEffectList = new ArrayList<>();
			// if (pred instanceof EndNode) {
			// assert (false);
			// CompoundStatement body = (CompoundStatement)
			// link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
			// if (Misc.collidesFreelyWith(targetNode, body)) {
			// Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
			// CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
			// sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt));
			// // Note that this line looks same, but is different.
			// sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(body));
			// sideEffectList.addAll(InsertImmediateSuccessor.insertAggressive(body,
			// targetNode));
			// sideEffectList.add(UpdateSideEffects.ADDED_ENCLOSING_BLOCK);
			// return sideEffectList;
			// } else {
			// sideEffectList.addAll(InsertImmediatePredecessor
			// .insertAggressive(body.getInfo().getCFGInfo().getNestedCFG().getEnd(),
			// targetNode));
			// return sideEffectList;
			// }
			// } else if (pred instanceof ContinueStatement) {
			// assert (false);
			// CompoundStatement body = (CompoundStatement)
			// link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
			// if (Misc.collidesFreelyWithinScopesOf(targetNode, body, pred)) {
			// sideEffectList.add(UpdateSideEffects.NO_UPDATE_DUE_TO_NAME_COLLISION);
			// return sideEffectList;
			// } else {
			// return InsertImmediatePredecessor.insertAggressive(leafPredList.get(0),
			// targetNode);
			// }
			// } else {
			// }
		}

		@Override
		public List<SideEffect> visit(WhileBodyLink link) {
			CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
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
		public List<SideEffect> visit(WhileEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);

			if (edges.stream()
					.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
				sideEffectList.add(new JumpEdgeConstraint(targetNode));
				return sideEffectList;
			}
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(DoBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(DoPredicateLink link) {
			// Note that this method would be called only if we have one predecessor.
			List<Node> leafPredList = link.childNode.getInfo().getCFGInfo().getLeafPredecessors();
			assert (leafPredList.size() == 1);
			Node pred = leafPredList.get(0);
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (pred instanceof EndNode) {
				CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
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
					return sideEffectList;
				} else {
					sideEffectList.addAll(InsertImmediatePredecessor
							.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
					return sideEffectList;
				}
			} else {
				assert (pred instanceof ContinueStatement);
				CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
				if (Misc.collidesFreelyWithinScopesOf(targetNode, body, pred)) {
					sideEffectList.add(new NoUpdateDueToNameCollision(targetNode));
					return sideEffectList;
				} else {
					return InsertImmediatePredecessor.insert(leafPredList.get(0), targetNode);
				}
			}
		}

		@Override
		public List<SideEffect> visit(DoBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
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
		public List<SideEffect> visit(DoEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);

			if (edges.stream()
					.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
				sideEffectList.add(new JumpEdgeConstraint(targetNode));
				return sideEffectList;
			}
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(ForBeginLink link) {
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
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(ForTermLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);

			if (edges.stream()
					.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
				sideEffectList.add(new JumpEdgeConstraint(targetNode));
				return sideEffectList;
			}
			ForStatement forStmt = link.enclosingNonLeafNode;

			ForStatementCFGInfo forInfo = forStmt.getInfo().getCFGInfo();
			CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();

			/*
			 * See if the transformation can be completed atomically.
			 */
			Expression initExpression = null;
			Expression stepExpression = null;
			List<Node> unrollTargets = new ArrayList<>();
			if (forInfo.hasInitExpression()) {
				initExpression = forInfo.getInitExpression();
				forInfo.removeInitExpression();
				Statement initStmt = FrontEnd.parseAndNormalize(initExpression + ";", Statement.class);
				sideEffectList.addAll(InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, initStmt));
				if (!Misc.changePerformed(sideEffectList)) {
					forInfo.setInitExpression(initExpression);
					return sideEffectList;
				}
				unrollTargets.add(initStmt);
			}
			assert (!forInfo.hasInitExpression());

			if (forInfo.hasStepExpression()) {
				/*
				 * Test if step expression can be inserted at all predecessors.
				 */
				stepExpression = forInfo.getStepExpression();
				List<Node> stepPreds = stepExpression.getInfo().getCFGInfo().getLeafPredecessors();
				forInfo.removeStepExpression();

				if (stepPreds.isEmpty()) {
					// This means that the path is not reachable.
					// We should complete the transformation and return.
					sideEffectList.add(new RemovedDeadCode(stepExpression));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode));
					if (!Misc.changePerformed(sideEffectList)) {
						for (Node n : unrollTargets) {
							NodeRemover.removeNode(n, LabelShiftingMode.LABELS_WITH_NODE);
						}
						if (initExpression != null) {
							forInfo.setInitExpression(initExpression);
						}
						return sideEffectList;
					}
					return sideEffectList;
				}
				for (Node stepPred : stepPreds) {
					Statement stepStmt = FrontEnd.parseAndNormalize(stepExpression + ";", Statement.class);
					if (stepPred instanceof EndNode) {
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
							sideEffectList.add(new AddedEnclosingBlock(compStmt));
						} else {
							sideEffectList.addAll(InsertImmediatePredecessor
									.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), stepStmt));
						}
						unrollTargets.add(stepStmt);
					} else if (stepPred instanceof ContinueStatement) {
						if (Misc.collidesFreelyWithinScopesOf(stepStmt, body, stepPred)) {
							sideEffectList.add(new NoUpdateDueToNameCollision(stepStmt));
							for (Node n : unrollTargets) {
								NodeRemover.removeNode(n, LabelShiftingMode.LABELS_WITH_GRAPH);
							}
							forInfo.setStepExpression(stepExpression);
							if (initExpression != null) {
								forInfo.setInitExpression(initExpression);
							}
							return sideEffectList;
						} else {
							sideEffectList.addAll(InsertImmediatePredecessor.insert(stepPred, stepStmt));
							unrollTargets.add(stepStmt);
						}
					}
				}
			}
			assert (!forInfo.hasStepExpression());

			/*
			 * Now, take care of only the last case: for (;e2;) {...}
			 */
			Node copiedTarget = Builder.getCopiedTarget(targetNode);
			List<Node> termPreds = link.childNode.getInfo().getCFGInfo().getLeafPredecessors();
			sideEffectList.addAll(InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode));
			if (!Misc.changePerformed(sideEffectList)) {
				for (Node n : unrollTargets) {
					NodeRemover.removeNode(n, LabelShiftingMode.LABELS_WITH_GRAPH);
				}
				if (stepExpression != null) {
					forInfo.setStepExpression(stepExpression);
				}
				if (initExpression != null) {
					forInfo.setInitExpression(initExpression);
				}
				return sideEffectList;
			}
			unrollTargets.add(targetNode);

			for (Node termPred : termPreds) {
				if (termPred == link.enclosingNonLeafNode.getInfo().getCFGInfo().getNestedCFG().getBegin()) {
					continue;
				}
				if (termPred instanceof EndNode) {
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
						sideEffectList.addAll(InsertImmediatePredecessor
								.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), copiedTarget));
					}
					unrollTargets.add(copiedTarget);
					copiedTarget = Builder.getCopiedTarget(copiedTarget);
				} else {
					assert (termPred instanceof ContinueStatement) : termPred.getClass().getSimpleName() + " "
							+ termPred + " is a predecessor of " + link.childNode;
					if (Misc.collidesFreelyWithinScopesOf(copiedTarget, body, termPred)) {
						sideEffectList.add(new NoUpdateDueToNameCollision(copiedTarget));
						for (Node n : unrollTargets) {
							NodeRemover.removeNode(n, LabelShiftingMode.LABELS_WITH_GRAPH);
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
						if (stepExpression != null) {
							forInfo.setStepExpression(stepExpression);
						}
						if (initExpression != null) {
							forInfo.setInitExpression(initExpression);
						}
						return sideEffectList;
					} else {
						sideEffectList.addAll(InsertImmediatePredecessor.insert(termPred, copiedTarget));
						unrollTargets.add(copiedTarget);
						copiedTarget = Builder.getCopiedTarget(copiedTarget);
					}
				}
				sideEffectList.add(new AddedCopy(copiedTarget));
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForStepLink link) {
			// Note that this method would be called only if we have one predecessor.
			List<Node> leafPredList = link.childNode.getInfo().getCFGInfo().getLeafPredecessors();
			assert (leafPredList.size() == 1);
			Node pred = leafPredList.get(0);
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (pred instanceof EndNode) {
				CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
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
					return sideEffectList;
				} else {
					sideEffectList.addAll(InsertImmediatePredecessor
							.insert(body.getInfo().getCFGInfo().getNestedCFG().getEnd(), targetNode));
					return sideEffectList;
				}
			} else {
				assert (pred instanceof ContinueStatement);
				CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
				if (Misc.collidesFreelyWithinScopesOf(targetNode, body, pred)) {
					sideEffectList.add(new NoUpdateDueToNameCollision(targetNode));
					return sideEffectList;
				} else {
					return InsertImmediatePredecessor.insert(leafPredList.get(0), targetNode);
				}
			}
		}

		@Override
		public List<SideEffect> visit(ForBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement body = (CompoundStatement) link.childNode;
			if (Misc.collidesFreelyWith(targetNode, body)) {
				Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
				CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
				sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt)); // Note that
																											// this line
																											// looks
																											// same, but
																											// is
																											// different.
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
		public List<SideEffect> visit(ForEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);

			if (edges.stream()
					.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
							|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
				sideEffectList.add(new JumpEdgeConstraint(targetNode));
				return sideEffectList;
			}
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CallBeginLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CallPreLink link) {
			return InsertImmediatePredecessor.insert(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CallPostLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the postCallNode of a callStatement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CallEndLink link) {
			return InsertImmediateSuccessor.insert(link.enclosingNonLeafNode, targetNode);
		}
	}

	private static class SimpleInsertImmediatePredecessor extends GJNoArguCFGLinkVisitor<List<SideEffect>> {

		protected Node targetNode;

		private SimpleInsertImmediatePredecessor(Node targetNode) {
			this.targetNode = targetNode;
		}

		/**
		 * Copies labels from baseNode to targetNode.
		 * 
		 * @param baseNode
		 * @param targetNode
		 */
		private void copyLabels(Statement baseNode, Statement targetNode) {
			List<Label> labelCopy = new ArrayList<>(baseNode.getInfo().getLabelAnnotations());
			for (Label baseLabel : labelCopy) {
				targetNode.getInfo().addLabelAnnotation(0, baseLabel);
			}
		}

		@Override
		public List<SideEffect> visit(FunctionBeginLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert nodes at the beginning of a FunctionDefinition.";
			Misc.warnDueToLackOfFeature(str, link.enclosingNonLeafNode);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(FunctionParameterLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (!(targetNode instanceof ParameterDeclaration)) {
				sideEffectList.add(new SyntacticConstraint(link.childNode,
						"Cannot insert an immediate predecessor for a parameter."));
				return sideEffectList;
			}
			FunctionDefinition functionDef = link.enclosingNonLeafNode;
			List<ParameterDeclaration> paramList = functionDef.getInfo().getCFGInfo().getParameterDeclarationList();
			int index = paramList.indexOf(link.childNode);
			functionDef.getInfo().getCFGInfo().addParameterDeclaration(index, (ParameterDeclaration) targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(FunctionBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert nodes at the beginning of a FunctionDefinition.";
			Misc.warnDueToLackOfFeature(str, link.enclosingNonLeafNode);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
			// FunctionDefinition functionDef = link.enclosingNonLeafNode;
			// if (targetNode instanceof ParameterDeclaration) {
			// functionDef.getInfo().getCFGInfo().addParameterDeclaration((ParameterDeclaration)
			// targetNode);
			// return sideEffectList;
			// } else if (targetNode instanceof Statement) {
			// CompoundStatement body = functionDef.getInfo().getCFGInfo().getBody();
			// body.getInfo().getCFGInfo().addElement(0, targetNode);
			// return sideEffectList; // Since we were not able to insert the targetNode
			// outside the body.
			// } else {
			// return sideEffectList;
			// }
		}

		@Override
		public List<SideEffect> visit(FunctionEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			FunctionDefinition functionDef = link.enclosingNonLeafNode;
			EndNode endNode = functionDef.getInfo().getCFGInfo().getNestedCFG().getEnd();
			List<Node> endPredList = endNode.getInfo().getCFGInfo().getPredBlocks();

			FunctionType fType = Type.getTypeTree(functionDef, Program.getRoot());
			boolean returnsVoid = (fType.getReturnType() instanceof VoidType) ? true : false;
			if (!returnsVoid) {
				if (endPredList.size() > 1
						|| (endPredList.size() == 1 && !(endPredList.get(0) instanceof CompoundStatement))) {
					String str = "Cannot insert a node immediately before the end-node of a function definition ("
							+ functionDef.getInfo().getFunctionName() + ") that returns a non-void expression.";
					Misc.warnDueToLackOfFeature(str, null);
					sideEffectList.add(new SyntacticConstraint(link.childNode, str));
					return sideEffectList;
				}
			}

			for (Node predNode : endPredList) {
				if (predNode instanceof CompoundStatement) {
					((CompoundStatement) predNode).getInfo().getCFGInfo().addElement(targetNode);
				} else {
					if (predNode instanceof ReturnStatement) {
						Node copiedTarget = Builder.getCopiedTarget(targetNode);
						InsertImmediatePredecessor.insertSimple(predNode, copiedTarget);
						this.copyLabels((Statement) predNode, (Statement) copiedTarget);
						((Statement) predNode).getInfo().clearLabelAnnotations();
					}
				}
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ParallelBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(ParallelClauseLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ParallelConstructCFGInfo parCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (targetNode instanceof IfClause) {
				parCFGInfo.setIfClause((IfClause) targetNode);
				return sideEffectList;
			} else if (targetNode instanceof NumThreadsClause) {
				parCFGInfo.setNumThreadsClause((NumThreadsClause) targetNode);
				return sideEffectList;
			} else {
				return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
			}
		}

		@Override
		public List<SideEffect> visit(ParallelBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ParallelConstructCFGInfo parCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (targetNode instanceof IfClause) {
				parCFGInfo.setIfClause((IfClause) targetNode);
				return sideEffectList;
			} else if (targetNode instanceof NumThreadsClause) {
				parCFGInfo.setNumThreadsClause((NumThreadsClause) targetNode);
				return sideEffectList;
			} else if (targetNode instanceof Statement) {
				String str = "Cannot insert a statement as a predecessor of the body of a ParallelConstruct.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList; // Since we were not able to insert the targetNode outside the body.
			} else {
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(ParallelEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ParallelConstructCFGInfo parCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (!(targetNode instanceof Statement)) {
				return sideEffectList;
			}
			Node rawBody = parCFGInfo.getBody();
			assert (rawBody instanceof CompoundStatement);
			CompoundStatement body = (CompoundStatement) rawBody;
			body.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(OmpForInitLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(OmpForTermLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert any node immediately before the termination condition"
					+ " of a parallel for loop.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForStepLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ForConstruct forConstruct = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) forConstruct.getInfo().getCFGInfo().getBody();
			body.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert any node immediately before the body of a parallel for loop.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert any node immediately before the end node of a parallel for loop.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(SectionsBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SectionsSectionBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert any node immediately before the body of a section.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(SectionsEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			SectionsConstructCFGInfo cfgInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (!(targetNode instanceof Statement)) {
				return sideEffectList;
			}
			for (Statement sectionBody : cfgInfo.getSectionList()) {
				assert (sectionBody instanceof CompoundStatement);
				CompoundStatement body = (CompoundStatement) sectionBody;
				Node copiedTarget = Builder.getCopiedTarget(targetNode);
				body.getInfo().getCFGInfo().addElement(copiedTarget);
			}
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(SingleBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SingleBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert any node immediately before the body of a single construct.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(SingleEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			SingleConstruct singleConstruct = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) singleConstruct.getInfo().getCFGInfo().getBody();
			body.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(TaskBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(TaskClauseLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			TaskConstruct taskConstruct = link.enclosingNonLeafNode;
			if (targetNode instanceof IfClause) {
				taskConstruct.getInfo().getCFGInfo().setIfClause((IfClause) targetNode);
				return sideEffectList;
			} else if (targetNode instanceof FinalClause) {
				taskConstruct.getInfo().getCFGInfo().setFinalClause((FinalClause) targetNode);
				return sideEffectList;
			} else {
				return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
			}
		}

		@Override
		public List<SideEffect> visit(TaskBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			TaskConstruct taskConstruct = link.enclosingNonLeafNode;
			if (targetNode instanceof IfClause) {
				taskConstruct.getInfo().getCFGInfo().setIfClause((IfClause) targetNode);
				return sideEffectList;
			} else if (targetNode instanceof FinalClause) {
				taskConstruct.getInfo().getCFGInfo().setFinalClause((FinalClause) targetNode);
				return sideEffectList;
			} else {
				String str = "Cannot insert any node immediately before the body of a task construct.";
				Misc.warnDueToLackOfFeature(str, null);
				sideEffectList.add(new SyntacticConstraint(link.childNode, str));
				return sideEffectList;
			}
		}

		@Override
		public List<SideEffect> visit(TaskEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			TaskConstruct taskConstruct = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) taskConstruct.getInfo().getCFGInfo().getBody();
			body.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(MasterBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(MasterBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the body of a master construct.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(MasterEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			MasterConstruct masterConstruct = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) masterConstruct.getInfo().getCFGInfo().getBody();
			body.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CriticalBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CriticalBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before a critical construct.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CriticalEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CriticalConstruct criticalConstruct = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) criticalConstruct.getInfo().getCFGInfo().getBody();
			body.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(AtomicBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(AtomicStatementLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the expression-statement of an atomic construct.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(AtomicEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node inside the body of an atomic construct.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OrderedBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(OrderedBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the body of an ordered construct.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OrderedEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			OrderedConstruct orderedConstruct = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) orderedConstruct.getInfo().getCFGInfo().getBody();
			body.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CompoundBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CompoundElementLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = link.enclosingNonLeafNode;
			Node baseNode = link.childNode;
			if (baseNode instanceof Declaration) {
				return sideEffectList;
			}

			List<Node> elementList = compStmt.getInfo().getCFGInfo().getElementList();
			int baseIndex = elementList.indexOf(Misc.getCFGNodeFor(baseNode));
			sideEffectList = compStmt.getInfo().getCFGInfo().addElement(baseIndex, targetNode);
			this.copyLabels((Statement) baseNode, (Statement) targetNode);
			((Statement) baseNode).getInfo().clearLabelAnnotations();
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CompoundEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = link.enclosingNonLeafNode;
			sideEffectList = compStmt.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(IfBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(IfPredicateLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(IfThenBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the then-body of an If statement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(IfElseBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the else-body of an If statement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(IfEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			assert (targetNode instanceof Statement);
			IfStatement ifStmt = link.enclosingNonLeafNode;
			CompoundStatement elseBody = (CompoundStatement) ifStmt.getInfo().getCFGInfo().getElseBody();
			Node copiedTarget = Builder.getCopiedTarget(targetNode);
			if (elseBody != null) {
				elseBody.getInfo().getCFGInfo().addElement(copiedTarget);
			} else {
				sideEffectList = ifStmt.getInfo().getCFGInfo().setElseBody((Statement) copiedTarget);
			}
			CompoundStatement thenBody = (CompoundStatement) ifStmt.getInfo().getCFGInfo().getThenBody();
			thenBody.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(SwitchBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SwitchPredicateLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(SwitchBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the body of a switch statement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(SwitchEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			SwitchStatement switchStmt = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) switchStmt.getInfo().getCFGInfo().getBody();
			body.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(WhileBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(WhilePredicateLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			Node copiedNode = Builder.getCopiedTarget(targetNode);
			sideEffectList = InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, copiedNode);
			if (sideEffectList.stream().anyMatch(s -> s instanceof SyntacticConstraint)) {
				return sideEffectList;
			}
			WhileStatement whileStmt = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) whileStmt.getInfo().getCFGInfo().getBody();
			body.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(WhileBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the body of a while statement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(WhileEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the end node of a while statement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(DoBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(DoPredicateLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			DoStatement doStmt = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) doStmt.getInfo().getCFGInfo().getBody();
			body.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(DoBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the body of a do statement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(DoEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the end node of a do statement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(ForInitLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(ForTermLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the termination condition of a for loop.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForStepLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			ForStatement forStmt = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) forStmt.getInfo().getCFGInfo().getBody();
			body.getInfo().getCFGInfo().addElement(targetNode);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForBodyLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the body of a for loop.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(ForEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the end node of a for loop.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CallBeginLink link) {
			return InsertImmediatePredecessor.insertSimple(link.enclosingNonLeafNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(CallPreLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the preCallNode of a call statement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CallPostLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the postCallNode of a call statement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CallEndLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot insert a node immediately before the end node of a call statement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}
	}
}
