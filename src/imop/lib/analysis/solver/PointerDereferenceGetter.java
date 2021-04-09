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
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to obtain syntactic dereferences made via asterisk
 * operator ({@code *}), where the dereferenced location is read/written.
 * 
 * @author Aman Nougrahiya
 */
public class PointerDereferenceGetter {

	/**
	 * Obtain a list of syntactic dereferences made via {@code *} for which the
	 * dereferenced location may have been read anywhere within the given
	 * {@code node}.
	 * <br>
	 * For example, in {@code ... = *(e1);}, this method returns a set
	 * containing {@code e1}.
	 * 
	 * @param node
	 *             a node from within which syntactic pointer dereferences where
	 *             the dereferenced location may have been read have to be found.
	 * @return
	 *         a list of syntactic dereferences made via {@code *} for
	 *         which the dereferenced location may have been read anywhere
	 *         within the given {@code node}.
	 */
	public static List<CastExpression> getPointerDereferencedCastExpressionReads(Node node) {
		List<CastExpression> retList = new ArrayList<>();
		if (node instanceof Expression) {
			BaseGetter visitor = new BaseGetter();
			List<CastExpression> representedList = node.accept(visitor);
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
				List<CastExpression> representedList = leaf.accept(visitor);
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
	 * Obtain a list of syntactic dereferences made via {@code *} for which the
	 * dereferenced location may have been written anywhere within the given
	 * {@code node}.
	 * <br>
	 * For example, in {@code *(e1) = ...;}, this method returns a set
	 * containing {@code e1}.
	 * 
	 * @param node
	 *             a node from within which syntactic pointer dereferences where
	 *             the dereferenced location may have been written have to be
	 *             found.
	 * @return
	 *         a list of syntactic dereferences made via {@code *} for
	 *         which the dereferenced location may have been written anywhere
	 *         within the given {@code node}.
	 */
	public static List<CastExpression> getPointerDereferencedCastExpressionWrites(Node node) {
		if (node instanceof Expression) {
			BaseGetter visitor = new BaseGetter();
			node.accept(visitor);
			assert (!visitor.writeList.contains(null));
			return visitor.writeList;
		} else {
			List<CastExpression> retList = new ArrayList<>();
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
	 * Assuming that {@code node} will be dereferenced using the {@code *}
	 * operator, this method returns the location that will be dereferenced.
	 * 
	 * @param node
	 *             any C expression node.
	 * @return
	 *         the location on which dereference via {@code *} opertator would
	 *         be done, when applied on {@code node}.
	 */
	public static List<CastExpression> getPointerDereferencedCastExpressionLocationsOf(Node node) {
		if (node instanceof Expression) {
			BaseGetter visitor = new BaseGetter();
			return node.accept(visitor);
		} else {
			List<CastExpression> retList = new ArrayList<>();
			node = Misc.getCFGNodeFor(node);
			for (Node leaf : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				BaseGetter visitor = new BaseGetter();
				List<CastExpression> tempExp = leaf.accept(visitor);
				if (tempExp != null) {
					retList.addAll(tempExp);
				}
			}
			return retList;
		}
	}

	private static class BaseGetter extends GJNoArguDepthFirstProcess<List<CastExpression>> {
		public List<CastExpression> readList = new ArrayList<>();
		public List<CastExpression> writeList = new ArrayList<>();

		private void addReads(List<CastExpression> list) {
			if (list == null) {
				return;
			}
			readList.addAll(list);
		}

		private void addWrites(List<CastExpression> list) {
			if (list == null) {
				return;
			}
			writeList.addAll(list);
		}

		@Override
		public List<CastExpression> visit(NodeToken n) {
			return null;
		}

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ( InitDeclaratorList() )?
		 * f2 ::= ";"
		 */
		@Override
		public List<CastExpression> visit(Declaration n) {
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
		public List<CastExpression> visit(InitDeclarator n) {
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
		public List<CastExpression> visit(Initializer n) {
			if (n.getF0().getChoice() instanceof AssignmentExpression) {
				AssignmentExpression assignExp = (AssignmentExpression) n.getF0().getChoice();
				List<CastExpression> retList = new ArrayList<>();
				if (assignExp.getF0().getChoice() instanceof ConditionalExpression) {
					ConditionalExpression condExp = (ConditionalExpression) assignExp.getF0().getChoice();
					List<CastExpression> condRetSet = condExp.accept(this);
					if (condRetSet != null) {
						retList.addAll(condRetSet);
					}
				} else {
					// in case of a NonConditionalExpression.
					NonConditionalExpression condExp = (NonConditionalExpression) assignExp.getF0().getChoice();
					List<CastExpression> condRetSet = condExp.accept(this);
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
		public List<CastExpression> visit(IfClause n) {
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
		public List<CastExpression> visit(NumThreadsClause n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= Expression()
		 */
		@Override
		public List<CastExpression> visit(OmpForInitExpression n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<"
		 * f2 ::= Expression()
		 */
		@Override
		public List<CastExpression> visit(OmpForLTCondition n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<="
		 * f2 ::= Expression()
		 */
		@Override
		public List<CastExpression> visit(OmpForLECondition n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">"
		 * f2 ::= Expression()
		 */
		@Override
		public List<CastExpression> visit(OmpForGTCondition n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">="
		 * f2 ::= Expression()
		 */
		@Override
		public List<CastExpression> visit(OmpForGECondition n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "++"
		 */
		@Override
		public List<CastExpression> visit(PostIncrementId n) {
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "--"
		 */
		@Override
		public List<CastExpression> visit(PostDecrementId n) {
			return null;
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public List<CastExpression> visit(PreIncrementId n) {
			return null;
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public List<CastExpression> visit(PreDecrementId n) {
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "+="
		 * f2 ::= Expression()
		 */
		@Override
		public List<CastExpression> visit(ShortAssignPlus n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "-="
		 * f2 ::= Expression()
		 */
		@Override
		public List<CastExpression> visit(ShortAssignMinus n) {
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
		public List<CastExpression> visit(OmpForAdditive n) {
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
		public List<CastExpression> visit(OmpForSubtractive n) {
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
		public List<CastExpression> visit(OmpForMultiplicative n) {
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
		public List<CastExpression> visit(FinalClause n) {
			addReads(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= ( Expression() )?
		 * f1 ::= ";"
		 */
		@Override
		public List<CastExpression> visit(ExpressionStatement n) {
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
		public List<CastExpression> visit(ReturnStatement n) {
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
		public List<CastExpression> visit(Expression n) {
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
		public List<CastExpression> visit(AssignmentExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public List<CastExpression> visit(NonConditionalExpression n) {
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
		public List<CastExpression> visit(ConditionalExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				NodeSequence seq = (NodeSequence) n.getF1().getNode();
				Expression exp = (Expression) seq.getNodes().get(1);
				ConditionalExpression condExp = (ConditionalExpression) seq.getNodes().get(3);

				List<CastExpression> retList = new ArrayList<>();

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
		public List<CastExpression> visit(LogicalORExpression n) {
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
		public List<CastExpression> visit(LogicalANDExpression n) {
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
		public List<CastExpression> visit(InclusiveORExpression n) {
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
		public List<CastExpression> visit(ExclusiveORExpression n) {
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
		public List<CastExpression> visit(ANDExpression n) {
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
		public List<CastExpression> visit(EqualityExpression n) {
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
		public List<CastExpression> visit(EqualOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "=="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public List<CastExpression> visit(EqualExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "!="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public List<CastExpression> visit(NonEqualExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= ShiftExpression()
		 * f1 ::= ( RelationalOptionalExpression() )?
		 */
		@Override
		public List<CastExpression> visit(RelationalExpression n) {
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
		public List<CastExpression> visit(RelationalOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "<"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public List<CastExpression> visit(RelationalLTExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= ">"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public List<CastExpression> visit(RelationalGTExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "<="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public List<CastExpression> visit(RelationalLEExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= ">="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public List<CastExpression> visit(RelationalGEExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= AdditiveExpression()
		 * f1 ::= ( ShiftOptionalExpression() )?
		 */
		@Override
		public List<CastExpression> visit(ShiftExpression n) {
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
		public List<CastExpression> visit(ShiftOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= ">>"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public List<CastExpression> visit(ShiftLeftExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "<<"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public List<CastExpression> visit(ShiftRightExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= MultiplicativeExpression()
		 * f1 ::= ( AdditiveOptionalExpression() )?
		 */
		@Override
		public List<CastExpression> visit(AdditiveExpression n) {
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
		public List<CastExpression> visit(AdditiveOptionalExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "+"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public List<CastExpression> visit(AdditivePlusExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "-"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public List<CastExpression> visit(AdditiveMinusExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= CastExpression()
		 * f1 ::= ( MultiplicativeOptionalExpression() )?
		 */
		@Override
		public List<CastExpression> visit(MultiplicativeExpression n) {
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
		public List<CastExpression> visit(MultiplicativeOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "*"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public List<CastExpression> visit(MultiplicativeMultiExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "/"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public List<CastExpression> visit(MultiplicativeDivExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "%"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public List<CastExpression> visit(MultiplicativeModExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= CastExpressionTypedCastExpressionTyped()
		 * | UnaryExpression()
		 */
		@Override
		public List<CastExpression> visit(CastExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "("
		 * f1 ::= TypeName()
		 * f2 ::= ")"
		 * f3 ::= CastExpression()
		 */
		@Override
		public List<CastExpression> visit(CastExpressionTyped n) {
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
		public List<CastExpression> visit(UnaryExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public List<CastExpression> visit(UnaryExpressionPreIncrement n) {
			List<CastExpression> symList = n.getF1().accept(this);
			addReads(symList);
			addWrites(symList);
			return null;
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public List<CastExpression> visit(UnaryExpressionPreDecrement n) {
			List<CastExpression> symList = n.getF1().accept(this);
			addReads(symList);
			addWrites(symList);
			return null;
		}

		/**
		 * f0 ::= UnaryOperator()
		 * f1 ::= CastExpression()
		 */
		@Override
		public List<CastExpression> visit(UnaryCastExpression n) {
			List<CastExpression> retList = new ArrayList<>();

			String operator = ((NodeToken) n.getF0().getF0().getChoice()).getTokenImage();
			if (operator.trim().equals("*")) {
				retList.add(n.getF1());
			}
			List<CastExpression> symList = n.getF1().accept(this);
			if (symList != null) {
				addReads(symList);
			}
			return retList;
		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public List<CastExpression> visit(UnarySizeofExpression n) {
			// Expression is not evaluated when present as the argument
			// to sizeof operator.
			return null;
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public List<CastExpression> visit(SizeofUnaryExpression n) {
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
		public List<CastExpression> visit(SizeofTypeName n) {
			return null;
		}

		/**
		 * f0 ::= PrimaryExpression()
		 * f1 ::= PostfixOperationsList()
		 */
		@Override
		public List<CastExpression> visit(PostfixExpression n) {
			List<CastExpression> retList;
			/*
			 * Collect the pointer-dereferences, if any, from the
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
				for (Node postFixOpNode : n.getF1().getF0().getNodes()) {
					APostfixOperation postFixOp = (APostfixOperation) postFixOpNode;
					/*
					 * Let internal pointer-dereference reads/writes, if any
					 * (such as those
					 * within e2 in [e2]), be collected.
					 */
					postFixOp.accept(this);
					addReads(retList);
					if (postFixOp.getF0().getChoice() instanceof PlusPlus
							|| postFixOp.getF0().getChoice() instanceof MinusMinus) {
						addWrites(retList);
					}
					retList.clear();
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
		public List<CastExpression> visit(BracketExpression n) {
			addReads(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( ExpressionList() )?
		 * f2 ::= ")"
		 */
		@Override
		public List<CastExpression> visit(ArgumentList n) {
			assert (false);
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * | Constant()
		 * | ExpressionClosed()
		 */
		@Override
		public List<CastExpression> visit(PrimaryExpression n) {
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
		public List<CastExpression> visit(ExpressionClosed n) {
			return n.getF1().accept(this);
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public List<CastExpression> visit(ExpressionList n) {
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
		public List<CastExpression> visit(Constant n) {
			return null;
		}

		@Override
		public List<CastExpression> visit(CallStatement n) {
			// AccessGetter should be called only on leaf nodes or their parts.
			// CallStatement is NOT a leaf node now.
			assert (false);
			return null;
		}

		@Override
		public List<CastExpression> visit(PreCallNode n) {
			return null;
		}

		@Override
		public List<CastExpression> visit(PostCallNode n) {
			return null;
		}

		@Override
		public List<CastExpression> visit(SimplePrimaryExpression n) {
			return null;
		}
	}

}
