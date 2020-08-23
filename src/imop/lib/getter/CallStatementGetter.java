/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.GJNoArguDepthFirstProcess;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class CallStatementGetter {
	/**
	 * Used to obtain a {@link CallStatement}, if {@code exp} represents one.
	 * Otherwise, {@code null} is returned.
	 * 
	 * @param exp
	 *            an expression from which a call-statement, if any, has to be
	 *            obtained. Note that this expression need not be in simplified
	 *            form.
	 * @return
	 *         a {@link CallStatement}, if {@code exp} represents one;
	 *         otherwise, {@code null}.
	 * 
	 */
	public static CallStatement getCallIfAny(Expression exp) {
		return exp.accept(new InternalGetter());
	}

	/**
	 * Returns a {@link CallStatement}, if the visited expression represents
	 * one.
	 * Note that the visited expression need not be in simplified form, since
	 * this visitor may be called from the parser itself.
	 */
	private static class InternalGetter extends GJNoArguDepthFirstProcess<CallStatement> {

		@Override
		public CallStatement visit(NodeList n) {
			assert (false);
			return null;
		}

		@Override
		public CallStatement visit(NodeListOptional n) {
			assert (false);
			return null;
		}

		@Override
		public CallStatement visit(NodeOptional n) {
			if (n.present()) {
				return n.getNode().accept(this);
			} else {
				return null;
			}
		}

		@Override
		public CallStatement visit(NodeSequence n) {
			assert (false);
			return null;
		}

		@Override
		public CallStatement visit(NodeChoice n) {
			return n.getChoice().accept(this);
		}

		@Override
		public CallStatement visit(NodeToken n) {
			return null;
		}

		//
		// User-generated visitor methods below
		//

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public CallStatement visit(Expression n) {
			if (n.getExpF1().getNodes().size() != 0) {
				return null;
			}
			return n.getExpF0().accept(this);
		}

		/**
		 * f0 ::= NonConditionalExpression()
		 * | ConditionalExpression()
		 */
		@Override
		public CallStatement visit(AssignmentExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public CallStatement visit(NonConditionalExpression n) {
			// Return null, when operator is not "=".
			String operatorString = n.getF1().toString();
			if (!operatorString.equals(" = ")) {
				return null;
			}

			// Return null, when LHS is not a simple identifier.
			SimplePrimaryExpression retReceiver = Misc.getSimplePrimaryExpression(n.getF0());
			if (retReceiver == null) {
				return null;
			}

			CallStatement callStatement = n.getF2().accept(this);
			if (callStatement == null) {
				// Returning null, since RHS is not of form "functionName (i1, c2, i3, ...)"
				return null;
			} else {
				// Else, add the return receiver to the callStatement, and return it.
				PostCallNode postCallNode = new PostCallNode(retReceiver);
				callStatement.addAReturnReceiver(postCallNode);
				return callStatement;
			}
		}

		/**
		 * f0 ::= LogicalORExpression()
		 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
		 */
		@Override
		public CallStatement visit(ConditionalExpression n) {
			if (n.getF1().present()) {
				return null;
			}
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= ConditionalExpression()
		 */
		@Override
		public CallStatement visit(ConstantExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= LogicalANDExpression()
		 * f1 ::= ( "||" LogicalORExpression() )?
		 */
		@Override
		public CallStatement visit(LogicalORExpression n) {
			if (n.getF1().present()) {
				return null;
			}
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= InclusiveORExpression()
		 * f1 ::= ( "&&" LogicalANDExpression() )?
		 */
		@Override
		public CallStatement visit(LogicalANDExpression n) {
			if (n.getF1().present()) {
				return null;
			}
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= ExclusiveORExpression()
		 * f1 ::= ( "|" InclusiveORExpression() )?
		 */
		@Override
		public CallStatement visit(InclusiveORExpression n) {
			if (n.getF1().present()) {
				return null;
			}
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= ANDExpression()
		 * f1 ::= ( "^" ExclusiveORExpression() )?
		 */
		@Override
		public CallStatement visit(ExclusiveORExpression n) {
			if (n.getF1().present()) {
				return null;
			}
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= EqualityExpression()
		 * f1 ::= ( "&" ANDExpression() )?
		 */
		@Override
		public CallStatement visit(ANDExpression n) {
			if (n.getF1().present()) {
				return null;
			}
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= RelationalExpression()
		 * f1 ::= ( EqualOptionalExpression() )?
		 */
		@Override
		public CallStatement visit(EqualityExpression n) {
			if (n.getF1().present()) {
				return null;
			}
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= ShiftExpression()
		 * f1 ::= ( RelationalOptionalExpression() )?
		 */
		@Override
		public CallStatement visit(RelationalExpression n) {
			if (n.getRelExpF1().present()) {
				return null;
			}
			return n.getRelExpF0().accept(this);
		}

		/**
		 * f0 ::= AdditiveExpression()
		 * f1 ::= ( ShiftOptionalExpression() )?
		 */
		@Override
		public CallStatement visit(ShiftExpression n) {
			if (n.getF1().present()) {
				return null;
			}
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= MultiplicativeExpression()
		 * f1 ::= ( AdditiveOptionalExpression() )?
		 */
		@Override
		public CallStatement visit(AdditiveExpression n) {
			if (n.getF1().present()) {
				return null;
			}
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= CastExpression()
		 * f1 ::= ( MultiplicativeOptionalExpression() )?
		 */
		@Override
		public CallStatement visit(MultiplicativeExpression n) {
			if (n.getF1().present()) {
				return null;
			}
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= CastExpressionTyped()
		 * | UnaryExpression()
		 */
		@Override
		public CallStatement visit(CastExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "("
		 * f1 ::= TypeName()
		 * f2 ::= ")"
		 * f3 ::= CastExpression()
		 */
		@Override
		public CallStatement visit(CastExpressionTyped n) {
			return null;
		}

		/**
		 * f0 ::= UnaryExpressionPreIncrement()
		 * | UnaryExpressionPreDecrement()
		 * | UnarySizeofExpression()
		 * | UnaryCastExpression()
		 * | PostfixExpression()
		 */
		@Override
		public CallStatement visit(UnaryExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public CallStatement visit(UnaryExpressionPreIncrement n) {
			return null;
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public CallStatement visit(UnaryExpressionPreDecrement n) {
			return null;
		}

		/**
		 * f0 ::= UnaryOperator()
		 * f1 ::= CastExpression()
		 */
		@Override
		public CallStatement visit(UnaryCastExpression n) {
			return null;
		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public CallStatement visit(UnarySizeofExpression n) {
			return null;
		}

		/**
		 * f0 ::= PrimaryExpression()
		 * f1 ::= PostfixOperationsList()
		 */
		@Override
		public CallStatement visit(PostfixExpression n) {
			Node primaryChoice = n.getF0().getF0().getChoice();
			if (!(primaryChoice instanceof NodeToken)) {
				return null;
			}
			NodeToken functionDesignatorNode = (NodeToken) primaryChoice;
			List<Node> postOpList = n.getF1().getF0().getNodes();
			if (postOpList.size() != 1) {
				return null;
			}

			APostfixOperation postOp = (APostfixOperation) postOpList.get(0);
			if (!(postOp.getF0().getChoice() instanceof ArgumentList)) {
				return null;
			}

			ArgumentList argList = (ArgumentList) postOp.getF0().getChoice();
			List<SimplePrimaryExpression> argumentSPEList = new ArrayList<>();
			ExpressionList expList = (ExpressionList) argList.getF1().getNode();
			if (expList != null) {
				SimplePrimaryExpression tempExp = Misc.getSimplePrimaryExpression(expList.getF0());
				if (tempExp == null) {
					return null;
				}
				argumentSPEList.add(tempExp);
				for (Node nodeSeqNode : expList.getF1().getNodes()) {
					AssignmentExpression assignExp = (AssignmentExpression) ((NodeSequence) nodeSeqNode).getNodes()
							.get(1);
					tempExp = Misc.getSimplePrimaryExpression(assignExp);
					if (tempExp == null) {
						return null;
					}
					argumentSPEList.add(tempExp);
				}
			}

			PreCallNode preCallNode = new PreCallNode(argumentSPEList);
			return new CallStatement(functionDesignatorNode, preCallNode, new PostCallNode());

		}

		/**
		 * f0 ::= ( APostfixOperation() )*
		 */
		@Override
		public CallStatement visit(PostfixOperationsList n) {
			assert (false);
			return null;
		}

		/**
		 * f0 ::= BracketExpression()
		 * | ArgumentList()
		 * | DotId()
		 * | ArrowId()
		 * | PlusPlus()
		 * | MinusMinus()
		 */
		@Override
		public CallStatement visit(APostfixOperation n) {
			assert (false);
			return null;
		}

		/**
		 * f0 ::= "++"
		 */
		@Override
		public CallStatement visit(PlusPlus n) {
			assert (false);
			return null;
		}

		/**
		 * f0 ::= "--"
		 */
		@Override
		public CallStatement visit(MinusMinus n) {
			assert (false);
			return null;
		}

		/**
		 * f0 ::= "["
		 * f1 ::= Expression()
		 * f2 ::= "]"
		 */
		@Override
		public CallStatement visit(BracketExpression n) {
			assert (false);
			return null;
		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( ExpressionList() )?
		 * f2 ::= ")"
		 */
		@Override
		public CallStatement visit(ArgumentList n) {
			assert (false);
			return null;
		}

		/**
		 * f0 ::= "."
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public CallStatement visit(DotId n) {
			assert (false);
			return null;
		}

		/**
		 * f0 ::= "->"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public CallStatement visit(ArrowId n) {
			assert (false);
			return null;
		}

		/**
		 * f0 ::= "("
		 * f1 ::= Expression()
		 * f2 ::= ")"
		 */
		@Override
		public CallStatement visit(ExpressionClosed n) {
			assert (false);
			return null;
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public CallStatement visit(ExpressionList n) {
			assert (false);
			return null;
		}

		@Override
		public CallStatement visit(SimplePrimaryExpression n) {
			return null;
		}
	}

}
