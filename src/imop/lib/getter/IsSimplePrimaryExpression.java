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
import imop.baseVisitor.GJNoArguDepthFirst;
import imop.parser.FrontEnd;

import java.util.List;

/**
 * Each visit in this class returns true if the visited expression is an
 * identifier/constant or bracketed form of identifier/constant.
 */
public class IsSimplePrimaryExpression extends GJNoArguDepthFirst<Boolean> {
	private NodeToken anIdentifier;
	private Constant aConstant;
	public SimplePrimaryExpression aSimplePE;
	//
	// User-generated visitor methods below
	//

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public Boolean visit(Expression n) {
		if (n.getExpF1().present()) {
			return false;
		}
		return n.getExpF0().accept(this);
	}

	/**
	 * f0 ::= NonConditionalExpression()
	 * | ConditionalExpression()
	 */
	@Override
	public Boolean visit(AssignmentExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= UnaryExpression()
	 * f1 ::= AssignmentOperator()
	 * f2 ::= AssignmentExpression()
	 */
	@Override
	public Boolean visit(NonConditionalExpression n) {
		return false;
	}

	/**
	 * f0 ::= LogicalORExpression()
	 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
	 */
	@Override
	public Boolean visit(ConditionalExpression n) {
		if (n.getF1().present()) {
			return false;
		}
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= ConditionalExpression()
	 */
	@Override
	public Boolean visit(ConstantExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= LogicalANDExpression()
	 * f1 ::= ( "||" LogicalORExpression() )?
	 */
	@Override
	public Boolean visit(LogicalORExpression n) {
		if (n.getF1().present()) {
			return false;
		}
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= InclusiveORExpression()
	 * f1 ::= ( "&&" LogicalANDExpression() )?
	 */
	@Override
	public Boolean visit(LogicalANDExpression n) {
		if (n.getF1().present()) {
			return false;
		}
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= ExclusiveORExpression()
	 * f1 ::= ( "|" InclusiveORExpression() )?
	 */
	@Override
	public Boolean visit(InclusiveORExpression n) {
		if (n.getF1().present()) {
			return false;
		}
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= ANDExpression()
	 * f1 ::= ( "^" ExclusiveORExpression() )?
	 */
	@Override
	public Boolean visit(ExclusiveORExpression n) {
		if (n.getF1().present()) {
			return false;
		}
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= EqualityExpression()
	 * f1 ::= ( "&" ANDExpression() )?
	 */
	@Override
	public Boolean visit(ANDExpression n) {
		if (n.getF1().present()) {
			return false;
		}
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= RelationalExpression()
	 * f1 ::= ( EqualOptionalExpression() )?
	 */
	@Override
	public Boolean visit(EqualityExpression n) {
		if (n.getF1().present()) {
			return false;
		}
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= ShiftExpression()
	 * f1 ::= ( RelationalOptionalExpression() )?
	 */
	@Override
	public Boolean visit(RelationalExpression n) {
		if (n.getRelExpF1().present()) {
			return false;
		}
		return n.getRelExpF0().accept(this);
	}

	/**
	 * f0 ::= AdditiveExpression()
	 * f1 ::= ( ShiftOptionalExpression() )?
	 */
	@Override
	public Boolean visit(ShiftExpression n) {
		if (n.getF1().present()) {
			return false;
		}
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= MultiplicativeExpression()
	 * f1 ::= ( AdditiveOptionalExpression() )?
	 */
	@Override
	public Boolean visit(AdditiveExpression n) {
		if (n.getF1().present()) {
			return false;
		}
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= CastExpression()
	 * f1 ::= ( MultiplicativeOptionalExpression() )?
	 */
	@Override
	public Boolean visit(MultiplicativeExpression n) {
		if (n.getF1().present()) {
			return false;
		}
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= CastExpressionTyped()
	 * | UnaryExpression()
	 */
	@Override
	public Boolean visit(CastExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= TypeName()
	 * f2 ::= ")"
	 * f3 ::= CastExpression()
	 */
	@Override
	public Boolean visit(CastExpressionTyped n) {
		return false;
	}

	/**
	 * f0 ::= UnaryExpressionPreIncrement()
	 * | UnaryExpressionPreDecrement()
	 * | UnarySizeofExpression()
	 * | UnaryCastExpression()
	 * | PostfixExpression()
	 */
	@Override
	public Boolean visit(UnaryExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public Boolean visit(UnaryExpressionPreIncrement n) {
		return false;
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public Boolean visit(UnaryExpressionPreDecrement n) {
		return false;
	}

	/**
	 * f0 ::= UnaryOperator()
	 * f1 ::= CastExpression()
	 */
	@Override
	public Boolean visit(UnaryCastExpression n) {
		return false;
	}

	/**
	 * f0 ::= SizeofTypeName()
	 * | SizeofUnaryExpression()
	 */
	@Override
	public Boolean visit(UnarySizeofExpression n) {
		return false;
	}

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public Boolean visit(PostfixExpression n) {
		Boolean primVal = n.getF0().accept(this);
		Boolean opVal = n.getF1().accept(this);
		if (primVal && opVal) {
			return true;
		}
		return false;
	}

	/**
	 * f0 ::= ( APostfixOperation() )*
	 */
	@Override
	public Boolean visit(PostfixOperationsList n) {
		List<Node> opList = n.getF0().getNodes();
		if (opList.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | Constant()
	 * | ExpressionClosed()
	 */
	@Override
	public Boolean visit(PrimaryExpression n) {
		Node choice = n.getF0().getChoice();
		if (choice instanceof ExpressionClosed) {
			return choice.accept(this);
		} else if (choice instanceof NodeToken) {
			anIdentifier = new NodeToken(((NodeToken) choice).getTokenImage());
			aSimplePE = new SimplePrimaryExpression(anIdentifier);
		} else {
			aConstant = FrontEnd.parseAlone(choice.toString(), Constant.class);
			aSimplePE = new SimplePrimaryExpression(aConstant);
		}
		return true;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= Expression()
	 * f2 ::= ")"
	 */
	@Override
	public Boolean visit(ExpressionClosed n) {
		return n.getF1().accept(this);
	}

	@Override
	public Boolean visit(SimplePrimaryExpression n) {
		return true;
	}
}
