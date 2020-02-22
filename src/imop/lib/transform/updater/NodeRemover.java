/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.updater;

import java.util.ArrayList;
import java.util.List;

import imop.ast.annotation.Label;
import imop.ast.node.external.CompoundStatement;
import imop.ast.node.external.CriticalConstruct;
import imop.ast.node.external.Declaration;
import imop.ast.node.external.DoStatement;
import imop.ast.node.external.Expression;
import imop.ast.node.external.ExpressionStatement;
import imop.ast.node.external.FinalClause;
import imop.ast.node.external.ForConstruct;
import imop.ast.node.external.ForStatement;
import imop.ast.node.external.FunctionDefinition;
import imop.ast.node.external.IfClause;
import imop.ast.node.external.IfStatement;
import imop.ast.node.external.MasterConstruct;
import imop.ast.node.external.Node;
import imop.ast.node.external.NumThreadsClause;
import imop.ast.node.external.OmpConstruct;
import imop.ast.node.external.OmpDirective;
import imop.ast.node.external.OrderedConstruct;
import imop.ast.node.external.ParallelConstruct;
import imop.ast.node.external.SingleConstruct;
import imop.ast.node.external.Statement;
import imop.ast.node.external.SwitchStatement;
import imop.ast.node.external.TaskConstruct;
import imop.ast.node.external.WhileStatement;
import imop.ast.node.internal.BeginNode;
import imop.ast.node.internal.EndNode;
import imop.ast.node.internal.OmpClause;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.link.baseVisitor.GJNoArguCFGLinkVisitor;
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
import imop.lib.transform.updater.sideeffect.IndexIncremented;
import imop.lib.transform.updater.sideeffect.MissingCFGParent;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.lib.transform.updater.sideeffect.SyntacticConstraint;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

public class NodeRemover {

	public enum LabelShiftingMode {
		LABELS_WITH_GRAPH, LABELS_WITH_NODE
	}

	/**
	 * Checks if this node is connected to any other node in the control-flow
	 * graph.
	 * If so, then this node is removed from its location in the AST and CFG.
	 * <br>
	 * This method is generally used by elementary transformations.
	 * 
	 * @param node
	 *            node to be removed from its location in the program/snippet.
	 * @return
	 *         list of side effects resulting from removal of the node.
	 */
	public static List<SideEffect> removeNodeIfConnected(Node node) {
		node = Misc.getCFGNodeFor(node);
		if (!node.getInfo().getCFGInfo().getPredBlocks().isEmpty()) {
			return NodeRemover.removeNode(node);
		}
		return new ArrayList<>();
	}

	public static List<SideEffect> removeNode(Node node) {
		return NodeRemover.removeNode(node, LabelShiftingMode.LABELS_WITH_GRAPH);
	}

	public static List<SideEffect> removeNode(Node node, LabelShiftingMode labelShiftingMode) {
		node = Misc.getCFGNodeFor(node);
		List<SideEffect> sideEffectList = new ArrayList<>();
		if (node instanceof BeginNode || node instanceof EndNode) {
			String str = "Cannot remove a BeginNode/EndNode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(node, str));
			return sideEffectList;
		}
		CFGLink cfgLink = CFGLinkFinder.getCFGLinkFor(node);
		if (cfgLink == null) {
			Misc.warnDueToLackOfFeature("Cannot remove a node, due to lack of an appropriate enclosing node.", node);
			sideEffectList.add(new MissingCFGParent(node));
			return sideEffectList;

		}
		return cfgLink.accept(new AggressiveNodeRemovingVisitor(labelShiftingMode));
	}

	private static class AggressiveNodeRemovingVisitor extends GJNoArguCFGLinkVisitor<List<SideEffect>> {
		@Override
		public List<SideEffect> visit(FunctionBeginLink link) {
			assert (false);
			return null;
		}

		private final LabelShiftingMode labelShiftingMode;

		public AggressiveNodeRemovingVisitor(LabelShiftingMode labelShiftingMode) {
			this.labelShiftingMode = labelShiftingMode;
		}

		@Override
		public List<SideEffect> visit(FunctionParameterLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot remove a parameter in this mode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(FunctionBodyLink link) {
			FunctionDefinition parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			parent.getInfo().getCFGInfo().setBody(compStmt);
			return sideEffectList;
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
			ParallelConstruct parCons = link.enclosingNonLeafNode;
			OmpClause clause = link.childNode;
			if (clause instanceof IfClause) {
				parCons.getInfo().getCFGInfo().removeIfClause();
			} else if (clause instanceof NumThreadsClause) {
				parCons.getInfo().getCFGInfo().removeNumThreadsClause();
			} else {
				assert (false);
				return null;
			}
			return new ArrayList<>();
		}

		@Override
		public List<SideEffect> visit(ParallelBodyLink link) {
			ParallelConstruct parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setBody(compStmt));
			return sideEffectList;
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
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot remove an OmpForInit expression.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForTermLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot remove an OmpForTerm expression.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForStepLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot remove an OmpForStep expression.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(OmpForBodyLink link) {
			ForConstruct parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setBody(compStmt));
			return sideEffectList;
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
			link.enclosingNonLeafNode.getInfo().getCFGInfo().removeSection(link.childNode);
			return new ArrayList<>();
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
			SingleConstruct parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setBody(compStmt));
			return sideEffectList;
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
			if (link.childNode instanceof FinalClause) {
				link.enclosingNonLeafNode.getInfo().getCFGInfo().removeFinalClause();
			} else {
				link.enclosingNonLeafNode.getInfo().getCFGInfo().removeIfClause();
			}
			return new ArrayList<>();
		}

		@Override
		public List<SideEffect> visit(TaskBodyLink link) {
			TaskConstruct parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setBody(compStmt));
			return sideEffectList;
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
			MasterConstruct parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setBody(compStmt));
			return sideEffectList;
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
			CriticalConstruct parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setBody(compStmt));
			return sideEffectList;
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
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot remove the body of an atomic statement.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
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
			OrderedConstruct parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setBody(compStmt));
			return sideEffectList;
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
			List<SideEffect> sideEffects = new ArrayList<>();
			if (this.labelShiftingMode == LabelShiftingMode.LABELS_WITH_GRAPH) {
				sideEffects.addAll(separateOutLabels(link));
			}
			sideEffects.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().removeElement(link.childNode));
			return sideEffects;
		}

		/**
		 * Separates out the label from the element to on an empty statement.
		 * 
		 * @param link
		 *            a compound-element link.
		 */
		private List<SideEffect> separateOutLabels(CompoundElementLink link) {
			List<SideEffect> sideEffects = new ArrayList<>();
			assert (this.labelShiftingMode == LabelShiftingMode.LABELS_WITH_GRAPH);
			Node element = link.childNode;
			if (element instanceof Declaration || element instanceof OmpConstruct || element instanceof OmpDirective) {
				return sideEffects; // A Declaration cannot have any labels.
			}
			Statement stmt = (Statement) element;
			if (!stmt.getInfo().hasLabelAnnotations()) { //We do not require getLabels() here.
				return sideEffects;
			}
			List<Label> labels = new ArrayList<>(stmt.getInfo().getLabelAnnotations());
			stmt.getInfo().clearLabelAnnotations();
			Statement emptyStmt = FrontEnd.parseAndNormalize(";", Statement.class);
			ExpressionStatement emptyExpStmt = (ExpressionStatement) Misc.getCFGNodeFor(emptyStmt);
			List<Node> elementList = link.enclosingNonLeafNode.getInfo().getCFGInfo().getElementList();
			int indexOfStmt = elementList.indexOf(stmt);
			assert (indexOfStmt != -1);
			sideEffects.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().addElement(indexOfStmt, emptyStmt));
			sideEffects.add(new IndexIncremented(emptyStmt, element));
			for (Label l : labels) {
				emptyExpStmt.getInfo().addLabelAnnotation(l);
			}
			return sideEffects;
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
			IfStatement parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			Expression newPredicate = FrontEnd.parseAndNormalize("1", Expression.class);
			parent.getInfo().getCFGInfo().setPredicate(newPredicate);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(IfThenBodyLink link) {
			IfStatement parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setThenBody(compStmt));
			return sideEffectList;

		}

		@Override
		public List<SideEffect> visit(IfElseBodyLink link) {
			IfStatement parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setElseBody(compStmt));
			return sideEffectList;

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
			SwitchStatement parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			Expression newPredicate = FrontEnd.parseAndNormalize("1", Expression.class);
			parent.getInfo().getCFGInfo().setPredicate(newPredicate);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(SwitchBodyLink link) {
			SwitchStatement parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setBody(compStmt));
			return sideEffectList;

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
			WhileStatement parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			Expression newPredicate = FrontEnd.parseAndNormalize("1", Expression.class);
			parent.getInfo().getCFGInfo().setPredicate(newPredicate);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(WhileBodyLink link) {
			WhileStatement parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setBody(compStmt));
			return sideEffectList;

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
			DoStatement parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			Expression newPredicate = FrontEnd.parseAndNormalize("1", Expression.class);
			parent.getInfo().getCFGInfo().setPredicate(newPredicate);
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(DoBodyLink link) {
			DoStatement parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setBody(compStmt));
			return sideEffectList;

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
			link.enclosingNonLeafNode.getInfo().getCFGInfo().removeInitExpression();
			return new ArrayList<>();
		}

		@Override
		public List<SideEffect> visit(ForTermLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().removeTerminationExpression();
			return new ArrayList<>();
		}

		@Override
		public List<SideEffect> visit(ForStepLink link) {
			link.enclosingNonLeafNode.getInfo().getCFGInfo().removeStepExpression();
			return new ArrayList<>();
		}

		@Override
		public List<SideEffect> visit(ForBodyLink link) {
			ForStatement parent = link.enclosingNonLeafNode;
			List<SideEffect> sideEffectList = new ArrayList<>();
			CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
			sideEffectList.addAll(parent.getInfo().getCFGInfo().setBody(compStmt));
			return sideEffectList;

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
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot remove a PreCallNode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CallPostLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			String str = "Cannot remove a PostCallNode.";
			Misc.warnDueToLackOfFeature(str, null);
			sideEffectList.add(new SyntacticConstraint(link.childNode, str));
			return sideEffectList;
		}

		@Override
		public List<SideEffect> visit(CallEndLink link) {
			assert (false);
			return null;
		}
	}
}
