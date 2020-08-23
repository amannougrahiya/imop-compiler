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
import imop.baseVisitor.DepthFirstVisitor;

/**
 * Counter for getting an approximate number of expression nodes
 * that are just wrappers.
 * <p>
 * This class also counts the number of objects that are wasted in
 * wrappings of a NodeChoice.
 */
public class DummyExpressionNodeCounter extends DepthFirstVisitor {

	public int totalExpressionObjects = 0;
	public int totalNumberofWrapperExpressions = 0;
	public int totalNumberofNodes = 0;
	public int totalNumberofChoiceObjects = 0;

	public void initProcess(Node n) {
		if (n instanceof Expression) {
			totalExpressionObjects++;
		}
		if (n instanceof NodeChoice) {
			totalNumberofChoiceObjects++;
		}
		totalNumberofNodes++;
	}

	public void endProcess(Node n) {
	}

	@Override
	public void visit(NodeList n) {
		initProcess(n);
		for (Node e : n.getNodes()) {
			e.accept(this);
		}
		endProcess(n);
	}

	@Override
	public void visit(NodeListOptional n) {
		initProcess(n);
		if (n.present()) {
			for (Node e : n.getNodes()) {
				e.accept(this);
			}
		}
		endProcess(n);
	}

	@Override
	public void visit(NodeOptional n) {
		initProcess(n);
		if (n.present()) {
			n.getNode().accept(this);
		}
		endProcess(n);

	}

	@Override
	public void visit(NodeSequence n) {
		initProcess(n);
		for (Node e : n.getNodes()) {
			e.accept(this);
		}
		endProcess(n);
	}

	@Override
	public void visit(NodeChoice n) {
		initProcess(n);
		n.getChoice().accept(this);
		endProcess(n);
	}

	@Override
	public void visit(NodeToken n) {
		initProcess(n);
		endProcess(n);
	}

	//
	// User-generated visitor methods below
	//

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(Expression n) {
		initProcess(n);
		totalNumberofWrapperExpressions++;
		n.getExpF0().accept(this);
		n.getExpF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= NonConditionalExpression()
	 * | ConditionalExpression()
	 */
	@Override
	public void visit(AssignmentExpression n) {
		initProcess(n);
		totalNumberofWrapperExpressions++;
		n.getF0().accept(this);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= UnaryExpression()
	 * f1 ::= AssignmentOperator()
	 * f2 ::= AssignmentExpression()
	 */
	@Override
	public void visit(NonConditionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
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
		initProcess(n);

		if (!n.getF1().present()) {
			totalNumberofWrapperExpressions++;
		}
		n.getF0().accept(this);
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
		initProcess(n);
		if (!n.getF1().present()) {
			totalNumberofWrapperExpressions++;
		}
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
		initProcess(n);
		if (!n.getF1().present()) {
			totalNumberofWrapperExpressions++;
		}
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
		initProcess(n);
		if (!n.getF1().present()) {
			totalNumberofWrapperExpressions++;
		}
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
		initProcess(n);
		if (!n.getF1().present()) {
			totalNumberofWrapperExpressions++;
		}
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
		if (!n.getF1().present()) {
			totalNumberofWrapperExpressions++;
		}
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
		if (!n.getRelExpF1().present()) {
			totalNumberofWrapperExpressions++;
		}
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
		if (!n.getF1().present()) {
			totalNumberofWrapperExpressions++;
		}
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
		if (!n.getF1().present()) {
			totalNumberofWrapperExpressions++;
		}
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
		if (!n.getF1().present()) {
			totalNumberofWrapperExpressions++;
		}
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
		totalNumberofWrapperExpressions++;
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(UnaryExpressionPreIncrement n) {
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
		totalNumberofWrapperExpressions++;
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
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "--"
	 */
	@Override
	public void visit(MinusMinus n) {
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
