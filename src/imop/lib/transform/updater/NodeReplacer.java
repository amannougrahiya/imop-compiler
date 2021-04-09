/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.updater;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.info.CompoundStatementCFGInfo;
import imop.lib.cfg.info.ParallelConstructCFGInfo;
import imop.lib.cfg.info.SectionsConstructCFGInfo;
import imop.lib.cfg.info.TaskConstructCFGInfo;
import imop.lib.cfg.link.baseVisitor.GJNoArguCFGLinkVisitor;
import imop.lib.cfg.link.node.*;
import imop.lib.transform.updater.NodeRemover.LabelShiftingMode;
import imop.lib.transform.updater.sideeffect.*;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class NodeReplacer {
	public static List<SideEffect> replaceNodes(Node oldNode, Node newNode) {
		List<SideEffect> sideEffects = new ArrayList<>();
		oldNode = Misc.getCFGNodeFor(oldNode);
		assert (oldNode != null);
		if (oldNode instanceof BeginNode || oldNode instanceof EndNode) {
			String str = "Cannot replace a BeginNode or an EndNode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffects.add(new SyntacticConstraint(oldNode, str));
			return sideEffects;
		}
		CFGLink link = CFGLinkFinder.getCFGLinkFor(oldNode);
		if (link == null) {
			Misc.warnDueToLackOfFeature("Cannot replace a node whose parent is missing.", null);
			sideEffects.add(new MissingCFGParent(oldNode));
			return sideEffects;
		}
		newNode = Misc.getCFGNodeFor(newNode);
		List<SideEffect> tempSE = link.accept(new NodeReplacementVisitor(newNode));
		if (tempSE != null) {
			sideEffects.addAll(tempSE);
		}
		return sideEffects;
	}

	public static class NodeReplacementVisitor extends GJNoArguCFGLinkVisitor<List<SideEffect>> {
		Node newNode;

		public NodeReplacementVisitor(Node newNode) {
			this.newNode = newNode;
		}

		@Override
		public List<SideEffect> visit(FunctionBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(FunctionParameterLink link) {
			List<SideEffect> sideEffects = new ArrayList<>();
			String str = "Cannot replace the ParameterDeclaration of a FunctionDefinition. This is a TODO.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffects.add(new SyntacticConstraint(link.childNode, str));
			return sideEffects;
		}

		@Override
		public List<SideEffect> visit(FunctionBodyLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((CompoundStatement) newNode);
			return null;
		}

		@Override
		public List<SideEffect> visit(FunctionEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ParallelBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ParallelClauseLink link) {
			ParallelConstructCFGInfo parConstructCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (link.childNode instanceof IfClause) {
				assert (newNode instanceof IfClause);
				parConstructCFGInfo.setIfClause((IfClause) newNode);
			} else if (link.childNode instanceof NumThreadsClause) {
				assert (newNode instanceof NumThreadsClause);
				parConstructCFGInfo.setNumThreadsClause((NumThreadsClause) newNode);
			}

			return null;
		}

		@Override
		public List<SideEffect> visit(ParallelBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(ParallelEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OmpForBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OmpForInitLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setInitExpression((OmpForInitExpression) newNode);
			return null;

		}

		@Override
		public List<SideEffect> visit(OmpForTermLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setForConditionExpression((OmpForCondition) newNode);
			return null;
		}

		@Override
		public List<SideEffect> visit(OmpForStepLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setReinitExpression((OmpForReinitExpression) newNode);
			return null;
		}

		@Override
		public List<SideEffect> visit(OmpForBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(OmpForEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SectionsBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SectionsSectionBodyLink link) {
			SectionsConstructCFGInfo sectionsCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			int index = sectionsCFGInfo.getSectionList().indexOf(link.childNode);
			sectionsCFGInfo.removeSection(link.childNode);
			return sectionsCFGInfo.addSection(index, (Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(SectionsEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SingleBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SingleBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(SingleEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(TaskBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(TaskClauseLink link) {
			TaskConstructCFGInfo taskCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			if (link.childNode instanceof FinalClause) {
				assert (newNode instanceof FinalClause);
				taskCFGInfo.setFinalClause((FinalClause) newNode);
			} else if (link.childNode instanceof IfClause) {
				assert (newNode instanceof IfClause);
				taskCFGInfo.setIfClause((IfClause) newNode);
			}
			return null;
		}

		@Override
		public List<SideEffect> visit(TaskBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(TaskEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(MasterBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(MasterBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(MasterEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CriticalBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CriticalBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(CriticalEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(AtomicBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(AtomicStatementLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((Statement) newNode);
			return null;
		}

		@Override
		public List<SideEffect> visit(AtomicEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OrderedBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OrderedBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(OrderedEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CompoundBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CompoundElementLink link) {
			CompoundStatementCFGInfo compStmtCFGInfo = link.enclosingNonLeafNode.getInfo().getCFGInfo();
			int index = compStmtCFGInfo.getElementList().indexOf(Misc.getCFGNodeFor(link.childNode));
			List<SideEffect> sideEffects = NodeRemover.removeNode(link.childNode, LabelShiftingMode.LABELS_WITH_GRAPH);
			if (!Misc.changePerformed(sideEffects)) {
				Misc.warnDueToLackOfFeature("Could not remove " + link.childNode, null);
				return sideEffects;
			}
			for (SideEffect sideEffect : sideEffects) {
				if (sideEffect instanceof IndexIncremented) {
					index++;
				} else if (sideEffect instanceof IndexDecremented) {
					index--;
				}
			}
			return compStmtCFGInfo.addElement(index, newNode);
		}

		@Override
		public List<SideEffect> visit(CompoundEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(IfBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(IfPredicateLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setPredicate((Expression) newNode);
			return null;
		}

		@Override
		public List<SideEffect> visit(IfThenBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setThenBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(IfElseBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setElseBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(IfEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SwitchBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SwitchPredicateLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setPredicate((Expression) newNode);
			return null;
		}

		@Override
		public List<SideEffect> visit(SwitchBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(SwitchEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(WhileBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(WhilePredicateLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setPredicate((Expression) newNode);
			return null;
		}

		@Override
		public List<SideEffect> visit(WhileBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(WhileEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(DoBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(DoPredicateLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setPredicate((Expression) newNode);
			return null;
		}

		@Override
		public List<SideEffect> visit(DoBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(DoEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ForBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ForInitLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setInitExpression((Expression) newNode);
			return null;
		}

		@Override
		public List<SideEffect> visit(ForTermLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setTerminationExpression((Expression) newNode);
			return null;
		}

		@Override
		public List<SideEffect> visit(ForStepLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().setStepExpression((Expression) newNode);
			return null;
		}

		@Override
		public List<SideEffect> visit(ForBodyLink link) {
			return link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody((Statement) newNode);
		}

		@Override
		public List<SideEffect> visit(ForEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CallBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CallPreLink link) {
			List<SideEffect> sideEffects = new ArrayList<>();
			String str = "Cannot replace the PreCallNode of a CallStatement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffects.add(new SyntacticConstraint(link.childNode, str));
			return sideEffects;
		}

		@Override
		public List<SideEffect> visit(CallPostLink link) {
			List<SideEffect> sideEffects = new ArrayList<>();
			String str = "Cannot replace the PostCallNode of a CallStatement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffects.add(new SyntacticConstraint(link.childNode, str));
			return sideEffects;
		}

		@Override
		public List<SideEffect> visit(CallEndLink link) {
			assert (false);
			return null;
		}
		//
		// public List<UpdateSideEffects> visit(GotoLink link) {
		// initProcess(link);
		// endProcess(link);
		// }
		//
		// public List<UpdateSideEffects> visit(BreakLink link) {
		// initProcess(link);
		// endProcess(link);
		// }
		//
		// public List<UpdateSideEffects> visit(ContinueLink link) {
		// initProcess(link);
		// endProcess(link);
		// }
		//
		// public List<UpdateSideEffects> visit(ReturnLink link) {
		// initProcess(link);
		// endProcess(link);
		// }
	}
}
