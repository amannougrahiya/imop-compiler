/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.solver;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.GJNoArguDepthFirstProcess;
import imop.lib.analysis.solver.tokens.ExpressionTokenizer;
import imop.lib.analysis.solver.tokens.Tokenizable;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to obtain the list of syntactic access-expressions that
 * are read or written inside a given node (any CFG, or Expression node).
 * 
 * @author Aman Nougrahiya
 */
public class SyntacticAccessExpressionGetter {

	/**
	 * Obtain a list of syntactic AccessExpressions that may be read anywhere
	 * within the node {@code node} (inter-procedurally).
	 * 
	 * @param node
	 *             a node from within which syntactic AccessExpression that are
	 *             read have to be found.
	 * @return
	 *         list of syntactic AccessExpressions that may be read within the
	 *         provided node {@code node}.
	 */
	public static List<SyntacticAccessExpression> getBaseAccessExpressionReads(Node node) {
		List<SyntacticAccessExpression> retList = new ArrayList<>();
		if (node instanceof Expression) {
			BaseGetter visitor = new BaseGetter();
			List<SyntacticAccessExpression> representedList = node.accept(visitor);
			assert (!visitor.readList.contains(null));
			retList.addAll(visitor.readList);
			if (representedList != null) {
				retList.addAll(representedList);
			}
			return retList;
		} else {
			node = Misc.getCFGNodeFor(node);
			for (Node leaf : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				BaseGetter visitor = new BaseGetter();
				List<SyntacticAccessExpression> representedList = leaf.accept(visitor);
				assert (!visitor.readList.contains(null));
				retList.addAll(visitor.readList);
				if (representedList != null) {
					retList.addAll(representedList);
				}
			}
			return retList;
		}
	}

	/**
	 * Obtain a list of syntactic AccessExpressions that may be written anywhere
	 * within the node {@code node} (inter-procedurally).
	 * 
	 * @param node
	 *             a node from within which syntactic AccessExpression that are
	 *             written have to be found.
	 * @return
	 *         list of syntactic AccessExpressions that may be written within
	 *         the
	 *         provided node {@code node}.
	 */
	public static List<SyntacticAccessExpression> getBaseAccessExpressionWrites(Node node) {
		if (node instanceof Expression) {
			BaseGetter visitor = new BaseGetter();
			node.accept(visitor);
			assert (!visitor.writeList.contains(null));
			return visitor.writeList;
		} else {
			List<SyntacticAccessExpression> retList = new ArrayList<>();
			node = Misc.getCFGNodeFor(node);
			for (Node leaf : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				BaseGetter visitor = new BaseGetter();
				leaf.accept(visitor);
				assert (!visitor.writeList.contains(null));
				retList.addAll(visitor.writeList);
			}
			return retList;
		}
	}

	/**
	 * Obtain a list of syntactic AccessExpressions that are represented by this
	 * node.
	 * 
	 * @param node
	 *             a node for whose location the corresponding syntactic
	 *             AccessExpressions have to be found.
	 * @return
	 *         list of syntactic AccessExpressions that may be represented by
	 *         the provided node {@code node}.
	 */
	public static List<SyntacticAccessExpression> getBaseAccessExpressionLocationsOf(Node node) {
		if (node instanceof Expression) {
			BaseGetter visitor = new BaseGetter();
			return node.accept(visitor);
		} else {
			List<SyntacticAccessExpression> retList = new ArrayList<>();
			node = Misc.getCFGNodeFor(node);
			for (Node leaf : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				BaseGetter visitor = new BaseGetter();
				List<SyntacticAccessExpression> tempList = leaf.accept(visitor);
				if (tempList != null) {
					retList.addAll(tempList);
				}
			}
			return retList;
		}
	}

	private static class BaseGetter extends GJNoArguDepthFirstProcess<List<SyntacticAccessExpression>> {
		public List<SyntacticAccessExpression> readList = new ArrayList<>();
		public List<SyntacticAccessExpression> writeList = new ArrayList<>();

		private void addReads(List<SyntacticAccessExpression> list) {
			if (list == null) {
				return;
			}
			readList.addAll(list);
		}

		private void addWrites(List<SyntacticAccessExpression> list) {
			if (list == null) {
				return;
			}
			writeList.addAll(list);
		}

		/**
		 * Obtain access-expressions from within a given (Expression) node.
		 * 
		 * @param node
		 *             an Expression or similar node from within which we need to
		 *             obtain the list of accessed access-expressions.
		 * @return
		 *         list of access-expressions within {@code node}.
		 */
		public static List<SyntacticAccessExpression> getInternalAccesses(Node node) {
			List<SyntacticAccessExpression> retList = new ArrayList<>();
			Node leaf = Misc.getCFGNodeFor(node);
			for (BracketExpression bracketExp : Misc.getInheritedEnclosee(node, BracketExpression.class)) {
				Expression index = bracketExp.getF1();
				List<Tokenizable> preList = ExpressionTokenizer.getPrefixTokens(index);
				PostfixExpression postFixExp = Misc.getEnclosingNode(bracketExp, PostfixExpression.class);
				PrimaryExpression primExp = postFixExp.getF0();
				List<APostfixOperation> postFixOps = new ArrayList<>();
				for (Node opNode : postFixExp.getF1().getF0().getNodes()) {
					APostfixOperation op = (APostfixOperation) opNode;
					if (op.getF0().getChoice() == bracketExp) {
						break;
					}
					postFixOps.add(op);
				}
				BaseSyntax baseSyntax = new BaseSyntax(primExp, postFixOps);
				retList.add(new SyntacticAccessExpression(leaf, baseSyntax, preList));
			}
			return retList;
		}

		@Override
		public List<SyntacticAccessExpression> visit(NodeToken n) {
			return null;
		}

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ( InitDeclaratorList() )?
		 * f2 ::= ";"
		 */
		@Override
		public List<SyntacticAccessExpression> visit(Declaration n) {
			if (n.getInfo().isTypedef()) {
				return null;
			}
			n.getF1().accept(this);
			return null;
		}

		/**
		 * f0 ::= Declarator()
		 * f1 ::= ( "=" Initializer() )?
		 */
		@Override
		public List<SyntacticAccessExpression> visit(InitDeclarator n) {
			if (n.getF1().getNode() != null) {
				addReads(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
			}
			// This return null is correct -- the expression does not represent LHS
			// location.
			return null;
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * | ArrayInitializer()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(Initializer n) {
			if (n.getF0().getChoice() instanceof AssignmentExpression) {
				AssignmentExpression assignExp = (AssignmentExpression) n.getF0().getChoice();
				List<SyntacticAccessExpression> retList = new ArrayList<>();
				if (assignExp.getF0().getChoice() instanceof ConditionalExpression) {
					ConditionalExpression condExp = (ConditionalExpression) assignExp.getF0().getChoice();
					List<SyntacticAccessExpression> condRetSet = condExp.accept(this);
					if (condRetSet != null) {
						retList.addAll(condRetSet);
					}
				} else {
					// in case of a NonConditionalExpression.
					NonConditionalExpression condExp = (NonConditionalExpression) assignExp.getF0().getChoice();
					List<SyntacticAccessExpression> condRetSet = condExp.accept(this);
					if (condRetSet != null) {
						retList.addAll(condRetSet); // Wouldn't this mostly be empty?
					}
				}
				return retList;
			} else {
				return null;
			}
		}

		/**
		 * f0 ::= <IF>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public List<SyntacticAccessExpression> visit(IfClause n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <NUM_THREADS>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public List<SyntacticAccessExpression> visit(NumThreadsClause n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= Expression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(OmpForInitExpression n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<"
		 * f2 ::= Expression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(OmpForLTCondition n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<="
		 * f2 ::= Expression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(OmpForLECondition n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">"
		 * f2 ::= Expression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(OmpForGTCondition n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">="
		 * f2 ::= Expression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(OmpForGECondition n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "++"
		 */
		@Override
		public List<SyntacticAccessExpression> visit(PostIncrementId n) {
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "--"
		 */
		@Override
		public List<SyntacticAccessExpression> visit(PostDecrementId n) {
			return null;
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public List<SyntacticAccessExpression> visit(PreIncrementId n) {
			return null;
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public List<SyntacticAccessExpression> visit(PreDecrementId n) {
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "+="
		 * f2 ::= Expression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ShortAssignPlus n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "-="
		 * f2 ::= Expression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ShortAssignMinus n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "+"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(OmpForAdditive n) {
			addReads(n.getF4().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "-"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(OmpForSubtractive n) {
			addReads(n.getF4().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= MultiplicativeExpression()
		 * f3 ::= "+"
		 * f4 ::= <IDENTIFIER>
		 */
		@Override
		public List<SyntacticAccessExpression> visit(OmpForMultiplicative n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <FINAL>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public List<SyntacticAccessExpression> visit(FinalClause n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= ( Expression() )?
		 * f1 ::= ";"
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ExpressionStatement n) {
			if (n.getF0().present()) {
				addReads(n.getF0().getNode().accept(this));
			}
			return null;
		}

		/**
		 * f0 ::= <RETURN>
		 * f1 ::= ( Expression() )?
		 * f2 ::= ";"
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ReturnStatement n) {
			if (n.getF1().present()) {
				addReads(n.getF1().getNode().accept(this));
			}
			return null;
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public List<SyntacticAccessExpression> visit(Expression n) {
			if (n.getExpF1().getNodes().isEmpty()) {
				return n.getExpF0().accept(this);
			} else {
				assert (false); // ExpressionSimplification should have removed the comma operator.
				return null;
			}
		}

		/**
		 * f0 ::= NonConditionalExpression()
		 * | ConditionalExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(AssignmentExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(NonConditionalExpression n) {
			addWrites(n.getF0().accept(this));
			String operator = ((NodeToken) n.getF1().getF0().getChoice()).getTokenImage();
			if (!operator.equals("=")) {
				addReads(n.getF0().accept(this));
			}
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= LogicalORExpression()
		 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ConditionalExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				NodeSequence seq = (NodeSequence) n.getF1().getNode();
				Expression exp = (Expression) seq.getNodes().get(1);
				ConditionalExpression condExp = (ConditionalExpression) seq.getNodes().get(3);

				List<SyntacticAccessExpression> retList = new ArrayList<>();

				addReads(n.getF0().accept(this));
				retList.addAll(exp.accept(this));
				retList.addAll(condExp.accept(this));
				return retList;
			}
		}

		/**
		 * f0 ::= LogicalANDExpression()
		 * f1 ::= ( "||" LogicalORExpression() )?
		 */
		@Override
		public List<SyntacticAccessExpression> visit(LogicalORExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n.getF0().accept(this));
				addReads(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= InclusiveORExpression()
		 * f1 ::= ( "&&" LogicalANDExpression() )?
		 */
		@Override
		public List<SyntacticAccessExpression> visit(LogicalANDExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n.getF0().accept(this));
				addReads(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= ExclusiveORExpression()
		 * f1 ::= ( "|" InclusiveORExpression() )?
		 */
		@Override
		public List<SyntacticAccessExpression> visit(InclusiveORExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n.getF0().accept(this));
				addReads(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= ANDExpression()
		 * f1 ::= ( "^" ExclusiveORExpression() )?
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ExclusiveORExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n.getF0().accept(this));
				addReads(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= EqualityExpression()
		 * f1 ::= ( "&" ANDExpression() )?
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ANDExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n.getF0().accept(this));
				addReads(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= RelationalExpression()
		 * f1 ::= ( EqualOptionalExpression() )?
		 */
		@Override
		public List<SyntacticAccessExpression> visit(EqualityExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n.getF0().accept(this));
				n.getF1().getNode().accept(this);
				return null;
			}
		}

		/**
		 * f0 ::= EqualExpression()
		 * | NonEqualExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(EqualOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "=="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(EqualExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "!="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(NonEqualExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= ShiftExpression()
		 * f1 ::= ( RelationalOptionalExpression() )?
		 */
		@Override
		public List<SyntacticAccessExpression> visit(RelationalExpression n) {
			if (n.getRelExpF1().getNode() == null) {
				return n.getRelExpF0().accept(this);
			} else {
				addReads(n.getRelExpF0().accept(this));
				n.getRelExpF1().getNode().accept(this);
				return null;
			}
		}

		/**
		 * f0 ::= RelationalLTExpression()
		 * | RelationalGTExpression()
		 * | RelationalLEExpression()
		 * | RelationalGEExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(RelationalOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "<"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(RelationalLTExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= ">"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(RelationalGTExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "<="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(RelationalLEExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= ">="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(RelationalGEExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= AdditiveExpression()
		 * f1 ::= ( ShiftOptionalExpression() )?
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ShiftExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n.getF0().accept(this));
				n.getF1().getNode().accept(this);
				return null;
			}
		}

		/**
		 * f0 ::= ShiftLeftExpression()
		 * | ShiftRightExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ShiftOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= ">>"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ShiftLeftExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "<<"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ShiftRightExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= MultiplicativeExpression()
		 * f1 ::= ( AdditiveOptionalExpression() )?
		 */
		@Override
		public List<SyntacticAccessExpression> visit(AdditiveExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n.getF0().accept(this));
				n.getF1().getNode().accept(this);
				return null;
			}
		}

		/**
		 * f0 ::= AdditivePlusExpression()
		 * | AdditiveMinusExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(AdditiveOptionalExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "+"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(AdditivePlusExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "-"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(AdditiveMinusExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= CastExpression()
		 * f1 ::= ( MultiplicativeOptionalExpression() )?
		 */
		@Override
		public List<SyntacticAccessExpression> visit(MultiplicativeExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n.getF0().accept(this));
				n.getF1().getNode().accept(this);
				return null;
			}
		}

		/**
		 * f0 ::= MultiplicativeMultiExpression()
		 * | MultiplicativeDivExpression()
		 * | MultiplicativeModExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(MultiplicativeOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "*"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(MultiplicativeMultiExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "/"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(MultiplicativeDivExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "%"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(MultiplicativeModExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= CastExpressionTypedCastExpressionTyped()
		 * | UnaryExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(CastExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "("
		 * f1 ::= TypeName()
		 * f2 ::= ")"
		 * f3 ::= CastExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(CastExpressionTyped n) {
			return n.getF3().accept(this);
		}

		/**
		 * f0 ::= UnaryExpressionPreIncrement()
		 * | UnaryExpressionPreDecrement()
		 * | UnarySizeofExpression()
		 * | UnaryCastExpression()
		 * | PostfixExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(UnaryExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(UnaryExpressionPreIncrement n) {
			List<SyntacticAccessExpression> symList = n.getF1().accept(this);
			addReads(symList);
			addWrites(symList);
			return null;
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(UnaryExpressionPreDecrement n) {
			List<SyntacticAccessExpression> symList = n.getF1().accept(this);
			addReads(symList);
			addWrites(symList);
			return null;
		}

		/**
		 * f0 ::= UnaryOperator()
		 * f1 ::= CastExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(UnaryCastExpression n) {
			List<SyntacticAccessExpression> symList = n.getF1().accept(this);
			if (symList == null) {
				return new ArrayList<>();
			} else {
				addReads(symList);
			}
			return null;
		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(UnarySizeofExpression n) {
			// Expression is not evaluated when present as the argument
			// to sizeof operator.
			return null;
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(SizeofUnaryExpression n) {
			// Expression is not evaluated when present as the argument to sizeof expression
			return null;
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= "("
		 * f2 ::= TypeName()
		 * f3 ::= ")"
		 */
		@Override
		public List<SyntacticAccessExpression> visit(SizeofTypeName n) {
			return null;
		}

		/**
		 * f0 ::= PrimaryExpression()
		 * f1 ::= PostfixOperationsList()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(PostfixExpression n) {
			List<SyntacticAccessExpression> retList = new ArrayList<>();
			/*
			 * Collect the access-expressions, if any, from the
			 * primary-expression.
			 */
			retList = n.getF0().accept(this);
			if (retList == null) {
				retList = new ArrayList<>();
			}
			if (n.getF1().getF0().getNodes().isEmpty()) {
				// Return the collected set as it is.
				return retList;
			} else {
				Node leaf = Misc.getCFGNodeFor(n);
				for (Node postFixOpNode : n.getF1().getF0().getNodes()) {
					APostfixOperation postFixOp = (APostfixOperation) postFixOpNode;
					/*
					 * Let internal access-expressions, if any (such as those
					 * within e2 in [e2]), be collected.
					 */
					postFixOp.accept(this);
					addReads(retList);
					/*
					 * If postFixOp wraps a BracketExpression, we should create
					 * an SyntacticAccessExpression here.
					 */
					if (postFixOp.getF0().getChoice() instanceof BracketExpression) {
						BracketExpression bracketExp = (BracketExpression) postFixOp.getF0().getChoice();
						/*
						 * Obtain the prefix form of index expression.
						 */
						Expression index = bracketExp.getF1();
						List<Tokenizable> preList = ExpressionTokenizer.getPrefixTokens(index);
						/*
						 * Obtain a BaseSyntax representing the base expression
						 * for this bracketExp.
						 */
						List<APostfixOperation> postFixOps = new ArrayList<>();
						for (Node opNode : n.getF1().getF0().getNodes()) {
							APostfixOperation op = (APostfixOperation) opNode;
							if (op.getF0().getChoice() == bracketExp) {
								break;
							}
							postFixOps.add(op);
						}
						BaseSyntax baseSyntax = new BaseSyntax(n.getF0(), postFixOps);
						retList.clear();
						retList.add(new SyntacticAccessExpression(leaf, baseSyntax, preList));
					} else if (postFixOp.getF0().getChoice() instanceof PlusPlus
							|| postFixOp.getF0().getChoice() instanceof MinusMinus) {
						addWrites(retList);
						retList.clear();
					} else {
						// For ".id", and "->id"
						retList.clear();
					}
				}
				return retList;
			}
		}

		/**
		 * f0 ::= "["
		 * f1 ::= Expression()
		 * f2 ::= "]"
		 */
		@Override
		public List<SyntacticAccessExpression> visit(BracketExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( ExpressionList() )?
		 * f2 ::= ")"
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ArgumentList n) {
			assert (false);
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * | Constant()
		 * | ExpressionClosed()
		 */
		@Override
		public List<SyntacticAccessExpression> visit(PrimaryExpression n) {
			if (n.getF0().getChoice() instanceof NodeToken) {
				return null;
			} else if (n.getF0().getChoice() instanceof Constant) {
				return null;
			} else if (n.getF0().getChoice() instanceof ExpressionClosed) {
				return n.getF0().getChoice().accept(this);
			} else {
				assert (false);
				return null;
			}
		}

		/**
		 * f0 ::= "("
		 * f1 ::= Expression()
		 * f2 ::= ")"
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ExpressionClosed n) {
			return n.getF1().accept(this);
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public List<SyntacticAccessExpression> visit(ExpressionList n) {
			assert (false);
			return null;
		}

		/**
		 * f0 ::= <INTEGER_LITERAL>
		 * | <FLOATING_POINT_LITERAL>
		 * | <CHARACTER_LITERAL>
		 * | ( <STRING_LITERAL> )+
		 */
		@Override
		public List<SyntacticAccessExpression> visit(Constant n) {
			return null;
		}

		@Override
		public List<SyntacticAccessExpression> visit(CallStatement n) {
			// AccessGetter should be called only on leaf nodes or their parts.
			// CallStatement is NOT a leaf node now.
			assert (false);
			return null;
		}

		@Override
		public List<SyntacticAccessExpression> visit(PreCallNode n) {
			return null;
		}

		@Override
		public List<SyntacticAccessExpression> visit(PostCallNode n) {
			return null;
		}

		@Override
		public List<SyntacticAccessExpression> visit(SimplePrimaryExpression n) {
			return null;
		}
	}

}
