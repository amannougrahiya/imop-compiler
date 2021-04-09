/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.simplify;

import imop.ast.annotation.CaseLabel;
import imop.ast.annotation.DefaultLabel;
import imop.ast.annotation.Label;
import imop.ast.annotation.SimpleLabel;
import imop.ast.info.StatementInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstVisitor;
import imop.lib.util.Misc;

public class LabelRemover {
	/**
	 * Populates the CFG statements within {@code baseNode} with the labels
	 * that annotate them.
	 * Furthermore, to ensure easy processing, all the wrapping
	 * labeled-statements are removed.
	 * <br>
	 * This method, via {@link StatementInfo#initAddLabelAnnotation(Label)},
	 * also adds Incompleteness for missing SwitchStatement parent, wherever
	 * appropriate.
	 * 
	 * @param baseNode
	 *                 root of the tree under which, the CFG statements
	 *                 that have label annotations are to be marked with the same,
	 *                 and for whom the wrapping label-statements have to be
	 *                 removed.
	 */
	public static void populateLabelAnnotations(Statement baseNode) {
		for (Node labelNode : Misc.getInheritedEncloseeList(baseNode, LabeledStatement.class)) {
			if (labelNode == baseNode) {
				continue;
			}
			if (!(labelNode instanceof SimpleLabeledStatement || labelNode instanceof DefaultLabeledStatement
					|| labelNode instanceof CaseLabeledStatement)) {
				// So that we do not process the wrapper object of runtime-type
				// LabeledStatement.
				continue;
			}
			Node labeledCFGNode = Misc.getInternalFirstCFGNode(labelNode);
			assert (labeledCFGNode instanceof Statement);
			Label newLabel = null;
			if (labelNode instanceof SimpleLabeledStatement) {
				newLabel = new SimpleLabel((Statement) labeledCFGNode,
						((SimpleLabeledStatement) labelNode).getF0().getTokenImage());
			} else if (labelNode instanceof CaseLabeledStatement) {
				newLabel = new CaseLabel((Statement) labeledCFGNode, ((CaseLabeledStatement) labelNode).getF1());
			} else if (labelNode instanceof DefaultLabeledStatement) {
				newLabel = new DefaultLabel((Statement) labeledCFGNode);
			} else {
				assert (false);
			}
			// System.out.println(
			// "Adding label " + newLabel.getString() + " to " + labeledCFGNode);
			((StatementInfo) labeledCFGNode.getInfo()).initAddLabelAnnotation(newLabel);
		}

		// Now, we will remove all the LabeledStatements from within baseNode
		baseNode.accept(new LabelDeleter());
		return;
	}

	/**
	 * This class is used to delete LabeledStatements from the visited node,
	 * and replace them with statements that have those labels as annotations.
	 * 
	 * @author aman
	 *
	 */
	private static class LabelDeleter extends DepthFirstVisitor {
		/**
		 * f0 ::= ( LabeledStatement() | ExpressionStatement() |
		 * CompoundStatement()
		 * | SelectionStatement() | IterationStatement() | JumpStatement() |
		 * UnknownPragma() | OmpConstruct() | OmpDirective() | UnknownCpp() )
		 */
		@Override
		public void visit(Statement n) {
			/*
			 * Delete the LabeledStatement, and replace it with an appropriate
			 * replacement.
			 */
			if (n.getStmtF0().getChoice().getClass() == LabeledStatement.class) {
				LabelReplacementGetter replacementGetter = new LabelReplacementGetter();
				n.accept(replacementGetter);
				n.getStmtF0().setChoice(replacementGetter.replacementStatement);
			}
			n.getStmtF0().accept(this);
		}
	}

	/**
	 * This class is used to obtain that AST node which can replace the visited
	 * LabeledStatement with itself.
	 * 
	 * @author aman
	 *
	 */
	private static class LabelReplacementGetter extends DepthFirstVisitor {
		public Node replacementStatement = null;

		/**
		 * f0 ::= ( LabeledStatement() | ExpressionStatement() |
		 * CompoundStatement()
		 * | SelectionStatement() | IterationStatement() | JumpStatement() |
		 * UnknownPragma() | OmpConstruct() | OmpDirective() | UnknownCpp() )
		 */
		@Override
		public void visit(Statement n) {
			n.getStmtF0().accept(this);
		}

		/**
		 * f0 ::= "#"
		 * f1 ::= <UNKNOWN_CPP>
		 */
		@Override
		public void visit(UnknownCpp n) {
			replacementStatement = n;
		}

		/**
		 * f0 ::= ParallelConstruct()
		 * | ForConstruct()
		 * | SectionsConstruct()
		 * | SingleConstruct()
		 * | ParallelForConstruct()
		 * | ParallelSectionsConstruct()
		 * | TaskConstruct()
		 * | MasterConstruct()
		 * | CriticalConstruct()
		 * | AtomicConstruct()
		 * | OrderedConstruct()
		 */
		@Override
		public void visit(OmpConstruct n) {
			replacementStatement = n;
		}

		/**
		 * f0 ::= BarrierDirective()
		 * | TaskwaitDirective()
		 * | TaskyieldDirective()
		 * | FlushDirective()
		 */
		@Override
		public void visit(OmpDirective n) {
			replacementStatement = n;
		}

		/**
		 * f0 ::= "#"
		 * f1 ::= <PRAGMA>
		 * f2 ::= <UNKNOWN_CPP>
		 */
		@Override
		public void visit(UnknownPragma n) {
			replacementStatement = n;
		}

		/**
		 * f0 ::= SimpleLabeledStatement()
		 * | CaseLabeledStatement()
		 * | DefaultLabeledStatement()
		 */
		@Override
		public void visit(LabeledStatement n) {
			n.getLabStmtF0().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ":"
		 * f2 ::= Statement()
		 */
		@Override
		public void visit(SimpleLabeledStatement n) {
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <CASE>
		 * f1 ::= ConstantExpression()
		 * f2 ::= ":"
		 * f3 ::= Statement()
		 */
		@Override
		public void visit(CaseLabeledStatement n) {
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= <DFLT>
		 * f1 ::= ":"
		 * f2 ::= Statement()
		 */
		@Override
		public void visit(DefaultLabeledStatement n) {
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= ( Expression() )?
		 * f1 ::= ";"
		 */
		@Override
		public void visit(ExpressionStatement n) {
			replacementStatement = n;
		}

		/**
		 * f0 ::= "{"
		 * f1 ::= ( CompoundStatementElement() )*
		 * f2 ::= "}"
		 */
		@Override
		public void visit(CompoundStatement n) {
			replacementStatement = n;
		}

		/**
		 * f0 ::= IfStatement()
		 * | SwitchStatement()
		 */
		@Override
		public void visit(SelectionStatement n) {
			replacementStatement = n;
		}

		/**
		 * f0 ::= WhileStatement()
		 * | DoStatement()
		 * | ForStatement()
		 */
		@Override
		public void visit(IterationStatement n) {
			replacementStatement = n;
		}

		/**
		 * f0 ::= GotoStatement()
		 * | ContinueStatement()
		 * | BreakStatement()
		 * | ReturnStatement()
		 */
		@Override
		public void visit(JumpStatement n) {
			replacementStatement = n;
		}

		@Override
		public void visit(CallStatement n) {
			replacementStatement = n;
		}
	}
}
