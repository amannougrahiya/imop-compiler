/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.projects.operatorCounter;

import java.util.HashMap;

import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstProcess;

/**
 * Populates the hash map with counts for different operations.
 */
public class OperatorCounter extends DepthFirstProcess {
	public HashMap<Operator, Integer> operatorCounter = new HashMap<>();

	public OperatorCounter() {
		for (Operator op : Operator.values()) {
			operatorCounter.put(op, new Integer(0));
		}
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
		switch (((NodeToken) n.getF0().getChoice()).getTokenImage()) {
		case "*=":
			operatorCounter.put(Operator.MUL, operatorCounter.get(Operator.MUL) + 1);
			break;
		case "/=":
			operatorCounter.put(Operator.DIV, operatorCounter.get(Operator.DIV) + 1);
			break;
		case "%=":
			operatorCounter.put(Operator.REM, operatorCounter.get(Operator.REM) + 1);
			break;
		case "+=":
			operatorCounter.put(Operator.ADD, operatorCounter.get(Operator.ADD) + 1);
			break;
		case "-=":
			operatorCounter.put(Operator.SUB, operatorCounter.get(Operator.SUB) + 1);
			break;
		case "<<=":
			operatorCounter.put(Operator.LSHIFT, operatorCounter.get(Operator.LSHIFT) + 1);
			break;
		case ">>=":
			operatorCounter.put(Operator.RSHIFT, operatorCounter.get(Operator.RSHIFT) + 1);
			break;
		case "&=":
			operatorCounter.put(Operator.BITWISE_AND, operatorCounter.get(Operator.BITWISE_AND) + 1);
			break;
		case "^=":
			operatorCounter.put(Operator.BITWISE_XOR, operatorCounter.get(Operator.BITWISE_XOR) + 1);
			break;
		case "|=":
			operatorCounter.put(Operator.BITWISE_OR, operatorCounter.get(Operator.BITWISE_OR) + 1);
			break;
		}
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= LogicalORExpression()
	 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
	 */
	@Override
	public void visit(ConditionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ConditionalExpression()
	 */
	@Override
	public void visit(ConstantExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= LogicalANDExpression()
	 * f1 ::= ( "||" LogicalORExpression() )?
	 */
	@Override
	public void visit(LogicalORExpression n) {
		if (n.getF1().present()) {
			operatorCounter.put(Operator.OR, operatorCounter.get(Operator.OR) + 1);
		}
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= InclusiveORExpression()
	 * f1 ::= ( "&&" LogicalANDExpression() )?
	 */
	@Override
	public void visit(LogicalANDExpression n) {
		if (n.getF1().present()) {
			operatorCounter.put(Operator.AND, operatorCounter.get(Operator.AND) + 1);
		}
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ExclusiveORExpression()
	 * f1 ::= ( "|" InclusiveORExpression() )?
	 */
	@Override
	public void visit(InclusiveORExpression n) {
		if (n.getF1().present()) {
			operatorCounter.put(Operator.BITWISE_OR, operatorCounter.get(Operator.BITWISE_OR) + 1);
		}
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ANDExpression()
	 * f1 ::= ( "^" ExclusiveORExpression() )?
	 */
	@Override
	public void visit(ExclusiveORExpression n) {
		if (n.getF1().present()) {
			operatorCounter.put(Operator.BITWISE_XOR, operatorCounter.get(Operator.BITWISE_XOR) + 1);
		}
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= EqualityExpression()
	 * f1 ::= ( "&" ANDExpression() )?
	 */
	@Override
	public void visit(ANDExpression n) {
		if (n.getF1().present()) {
			operatorCounter.put(Operator.BITWISE_AND, operatorCounter.get(Operator.BITWISE_AND) + 1);
		}
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= RelationalExpression()
	 * f1 ::= ( EqualOptionalExpression() )?
	 */
	@Override
	public void visit(EqualityExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= EqualExpression()
	 * | NonEqualExpression()
	 */
	@Override
	public void visit(EqualOptionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "=="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public void visit(EqualExpression n) {
		operatorCounter.put(Operator.EQUALS, operatorCounter.get(Operator.EQUALS) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "!="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public void visit(NonEqualExpression n) {
		operatorCounter.put(Operator.NOT_EQUALS, operatorCounter.get(Operator.NOT_EQUALS) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ShiftExpression()
	 * f1 ::= ( RelationalOptionalExpression() )?
	 */
	@Override
	public void visit(RelationalExpression n) {
		initProcess(n);
		n.getRelExpF0().accept(this);
		n.getRelExpF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= RelationalLTExpression()
	 * | RelationalGTExpression()
	 * | RelationalLEExpression()
	 * | RelationalGEExpression()
	 */
	@Override
	public void visit(RelationalOptionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "<"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalLTExpression n) {
		operatorCounter.put(Operator.LT, operatorCounter.get(Operator.LT) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ">"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalGTExpression n) {
		operatorCounter.put(Operator.GT, operatorCounter.get(Operator.GT) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "<="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalLEExpression n) {
		operatorCounter.put(Operator.LEQ, operatorCounter.get(Operator.LEQ) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ">="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalGEExpression n) {
		operatorCounter.put(Operator.GEQ, operatorCounter.get(Operator.GEQ) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= AdditiveExpression()
	 * f1 ::= ( ShiftOptionalExpression() )?
	 */
	@Override
	public void visit(ShiftExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ShiftLeftExpression()
	 * | ShiftRightExpression()
	 */
	@Override
	public void visit(ShiftOptionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ">>"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public void visit(ShiftLeftExpression n) {
		operatorCounter.put(Operator.RSHIFT, operatorCounter.get(Operator.RSHIFT) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "<<"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public void visit(ShiftRightExpression n) {
		operatorCounter.put(Operator.LSHIFT, operatorCounter.get(Operator.LSHIFT) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= MultiplicativeExpression()
	 * f1 ::= ( AdditiveOptionalExpression() )?
	 */
	@Override
	public void visit(AdditiveExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= AdditivePlusExpression()
	 * | AdditiveMinusExpression()
	 */
	@Override
	public void visit(AdditiveOptionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "+"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public void visit(AdditivePlusExpression n) {
		operatorCounter.put(Operator.ADD, operatorCounter.get(Operator.ADD) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "-"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public void visit(AdditiveMinusExpression n) {
		operatorCounter.put(Operator.SUB, operatorCounter.get(Operator.SUB) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= CastExpression()
	 * f1 ::= ( MultiplicativeOptionalExpression() )?
	 */
	@Override
	public void visit(MultiplicativeExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= MultiplicativeMultiExpression()
	 * | MultiplicativeDivExpression()
	 * | MultiplicativeModExpression()
	 */
	@Override
	public void visit(MultiplicativeOptionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "*"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeMultiExpression n) {
		operatorCounter.put(Operator.MUL, operatorCounter.get(Operator.MUL) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "/"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeDivExpression n) {
		operatorCounter.put(Operator.DIV, operatorCounter.get(Operator.DIV) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "%"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeModExpression n) {
		operatorCounter.put(Operator.REM, operatorCounter.get(Operator.REM) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= CastExpressionTyped()
	 * | UnaryExpression()
	 */
	@Override
	public void visit(CastExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= TypeName()
	 * f2 ::= ")"
	 * f3 ::= CastExpression()
	 */
	@Override
	public void visit(CastExpressionTyped n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
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
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(UnaryExpressionPreIncrement n) {
		operatorCounter.put(Operator.PREINC, operatorCounter.get(Operator.PREINC) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(UnaryExpressionPreDecrement n) {
		operatorCounter.put(Operator.PREDEC, operatorCounter.get(Operator.PREDEC) + 1);
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= UnaryOperator()
	 * f1 ::= CastExpression()
	 */
	@Override
	public void visit(UnaryCastExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= SizeofTypeName()
	 * | SizeofUnaryExpression()
	 */
	@Override
	public void visit(UnarySizeofExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(SizeofUnaryExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= "("
	 * f2 ::= TypeName()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(SizeofTypeName n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
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
		switch (((NodeToken) n.getF0().getChoice()).getTokenImage()) {
		case "-":
			operatorCounter.put(Operator.UNARY_NEGATION, operatorCounter.get(Operator.UNARY_NEGATION) + 1);
			break;
		case "~":
			operatorCounter.put(Operator.LOGICAL_NEGATION, operatorCounter.get(Operator.LOGICAL_NEGATION) + 1);
			break;
		case "!":
			operatorCounter.put(Operator.BITWISE_NEGATION, operatorCounter.get(Operator.BITWISE_NEGATION) + 1);
			break;
		}
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public void visit(PostfixExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( APostfixOperation() )*
	 */
	@Override
	public void visit(PostfixOperationsList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
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
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "++"
	 */
	@Override
	public void visit(PlusPlus n) {
		operatorCounter.put(Operator.POSTINC, operatorCounter.get(Operator.POSTINC) + 1);
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "--"
	 */
	@Override
	public void visit(MinusMinus n) {
		operatorCounter.put(Operator.POSTDEC, operatorCounter.get(Operator.POSTDEC) + 1);
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "["
	 * f1 ::= Expression()
	 * f2 ::= "]"
	 */
	@Override
	public void visit(BracketExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ExpressionList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ArgumentList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "."
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(DotId n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "->"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(ArrowId n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | Constant()
	 * | ExpressionClosed()
	 */
	@Override
	public void visit(PrimaryExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= Expression()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ExpressionClosed n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(ExpressionList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <INTEGER_LITERAL>
	 * | <FLOATING_POINT_LITERAL>
	 * | <CHARACTER_LITERAL>
	 * | ( <STRING_LITERAL> )+
	 */
	@Override
	public void visit(Constant n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

}
