/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info;

import imop.ast.node.external.*;
import imop.baseVisitor.GJVoidDepthFirst;
import imop.lib.getter.FunctionDefinitionGetter;

import java.util.List;

/**
 * Needs to be verified (AND modified).
 * This visitor populates the runInParallel field of FunctionDefinitionInfo.
 * This field is set to true, if the function may run in parallel with other
 * functions.
 */
public class InitRunInParallel extends GJVoidDepthFirst<Node> {

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public void visit(PostfixExpression n, Node argu) {
		List<Node> opList = n.getF1().getF0().getNodes();
		if ((!opList.isEmpty()) && ((APostfixOperation) opList.get(0)).getF0().getChoice() instanceof ArgumentList) {
			if (n.getF0().getF0().getChoice() instanceof NodeToken) {
				String name = ((NodeToken) n.getF0().getF0().getChoice()).getTokenImage();
				FunctionDefinitionGetter funcGetter = new FunctionDefinitionGetter();
				argu.accept(funcGetter, name);
				if (funcGetter.funcDefNodes != null) {
					funcGetter.funcDefNodes.get(0).getInfo().setRunInParallel(true);
				}
			}
		}
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
	}

	/**
	 * f0 ::= ( APostfixOperation() )*
	 */
	@Override
	public void visit(PostfixOperationsList n, Node argu) {
		n.getF0().accept(this, argu);
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
	public void visit(APostfixOperation n, Node argu) {
		n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | Constant()
	 * | ExpressionClosed()
	 */
	@Override
	public void visit(PrimaryExpression n, Node argu) {
		n.getF0().accept(this, argu);
	}

}
