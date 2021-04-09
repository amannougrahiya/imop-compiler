/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.builder;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstProcess;

/**
 * TODO: WRITE SOME CODE IN THIS FILE!! :D
 * This class sets the declCodeString of the declaration for a new variable
 * in String, as per the given Expression
 * IMPORTANT: This should be called only over an Expression (or its sub-type)
 * Caller should populate varName with name of the new variable
 */
public class DeclarationStringFromExpressionGetter extends DepthFirstProcess {
	private String varName;
	public String declCodeString; // String holding the declaration to be used

	public DeclarationStringFromExpressionGetter(String varName) {
		this.varName = varName;
		declCodeString = "unsigned int " + varName + ";";
		// TODO: Change the initializer above
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(Expression n) {
		n.getExpF0().accept(this);
		n.getExpF1().accept(this);
	}

	/**
	 * f0 ::= NonConditionalExpression()
	 * | ConditionalExpression()
	 */
	@Override
	public void visit(AssignmentExpression n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= UnaryExpression()
	 * f1 ::= AssignmentOperator()
	 * f2 ::= AssignmentExpression()
	 */
	@Override
	public void visit(NonConditionalExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
	}

	/**
	 * f0 ::= "="
	 * | "*="
	 * | "/="
	 * | "%="
	 * | "+="
	 * | "-="
	 * | "<<="
	 * | ">>="
	 * | "&="
	 * | "^="
	 * | "|="
	 */
	@Override
	public void visit(AssignmentOperator n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= LogicalORExpression()
	 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
	 */
	@Override
	public void visit(ConditionalExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ConditionalExpression()
	 */
	@Override
	public void visit(ConstantExpression n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= LogicalANDExpression()
	 * f1 ::= ( "||" LogicalORExpression() )?
	 */
	@Override
	public void visit(LogicalORExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= InclusiveORExpression()
	 * f1 ::= ( "&&" LogicalANDExpression() )?
	 */
	@Override
	public void visit(LogicalANDExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ExclusiveORExpression()
	 * f1 ::= ( "|" InclusiveORExpression() )?
	 */
	@Override
	public void visit(InclusiveORExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ANDExpression()
	 * f1 ::= ( "^" ExclusiveORExpression() )?
	 */
	@Override
	public void visit(ExclusiveORExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= EqualityExpression()
	 * f1 ::= ( "&" ANDExpression() )?
	 */
	@Override
	public void visit(ANDExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= RelationalExpression()
	 * f1 ::= ( EqualOptionalExpression() )?
	 */
	@Override
	public void visit(EqualityExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= EqualExpression()
	 * | NonEqualExpression()
	 */
	@Override
	public void visit(EqualOptionalExpression n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "=="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public void visit(EqualExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= "!="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public void visit(NonEqualExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ShiftExpression()
	 * f1 ::= ( RelationalOptionalExpression() )?
	 */
	@Override
	public void visit(RelationalExpression n) {
		n.getRelExpF0().accept(this);
		n.getRelExpF1().accept(this);
	}

	/**
	 * f0 ::= RelationalLTExpression()
	 * | RelationalGTExpression()
	 * | RelationalLEExpression()
	 * | RelationalGEExpression()
	 */
	@Override
	public void visit(RelationalOptionalExpression n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "<"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalLTExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ">"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalGTExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= "<="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalLEExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ">="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalGEExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= AdditiveExpression()
	 * f1 ::= ( ShiftOptionalExpression() )?
	 */
	@Override
	public void visit(ShiftExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ShiftLeftExpression()
	 * | ShiftRightExpression()
	 */
	@Override
	public void visit(ShiftOptionalExpression n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= ">>"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public void visit(ShiftLeftExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= "<<"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public void visit(ShiftRightExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= MultiplicativeExpression()
	 * f1 ::= ( AdditiveOptionalExpression() )?
	 */
	@Override
	public void visit(AdditiveExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= AdditivePlusExpression()
	 * | AdditiveMinusExpression()
	 */
	@Override
	public void visit(AdditiveOptionalExpression n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "+"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public void visit(AdditivePlusExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= "-"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public void visit(AdditiveMinusExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= CastExpression()
	 * f1 ::= ( MultiplicativeOptionalExpression() )?
	 */
	@Override
	public void visit(MultiplicativeExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= MultiplicativeMultiExpression()
	 * | MultiplicativeDivExpression()
	 * | MultiplicativeModExpression()
	 */
	@Override
	public void visit(MultiplicativeOptionalExpression n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "*"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeMultiExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= "/"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeDivExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= "%"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeModExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= CastExpressionTyped()
	 * | UnaryExpression()
	 */
	@Override
	public void visit(CastExpression n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= TypeName()
	 * f2 ::= ")"
	 * f3 ::= CastExpression()
	 */
	@Override
	public void visit(CastExpressionTyped n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
	}

	/**
	 * f0 ::= UnaryExpressionPreIncrement()
	 * | UnaryExpressionPreDecrement()
	 * | UnarySizeofExpression()
	 * | UnaryCastExpression()
	 * | PostfixExpression()
	 */
	@Override
	public void visit(UnaryExpression n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(UnaryExpressionPreIncrement n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(UnaryExpressionPreDecrement n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= UnaryOperator()
	 * f1 ::= CastExpression()
	 */
	@Override
	public void visit(UnaryCastExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= SizeofTypeName()
	 * | SizeofUnaryExpression()
	 */
	@Override
	public void visit(UnarySizeofExpression n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(SizeofUnaryExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= "("
	 * f2 ::= TypeName()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(SizeofTypeName n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
	}

	/**
	 * f0 ::= "&"
	 * | "*"
	 * | "+"
	 * | "-"
	 * | "~"
	 * | "!"
	 */
	@Override
	public void visit(UnaryOperator n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public void visit(PostfixExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ( APostfixOperation() )*
	 */
	@Override
	public void visit(PostfixOperationsList n) {
		n.getF0().accept(this);
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
	public void visit(APostfixOperation n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "++"
	 */
	@Override
	public void visit(PlusPlus n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "--"
	 */
	@Override
	public void visit(MinusMinus n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "["
	 * f1 ::= Expression()
	 * f2 ::= "]"
	 */
	@Override
	public void visit(BracketExpression n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ExpressionList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ArgumentList n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
	}

	/**
	 * f0 ::= "."
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(DotId n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= "->"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(ArrowId n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | Constant()
	 * | ExpressionClosed()
	 */
	@Override
	public void visit(PrimaryExpression n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= Expression()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ExpressionClosed n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(ExpressionList n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= <INTEGER_LITERAL>
	 * | <FLOATING_POINT_LITERAL>
	 * | <CHARACTER_LITERAL>
	 * | ( <STRING_LITERAL> )+
	 */
	@Override
	public void visit(Constant n) {
		n.getF0().accept(this);
	}

	@Override
	public void visit(SimplePrimaryExpression n) {
		if (n.isAConstant()) {
			n.getConstant().accept(this);
		} else {
			n.getIdentifier().accept(this);
		}
	}

}
