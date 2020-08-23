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
import imop.baseVisitor.GJNoArguDepthFirst;

/**
 * Stores the functionName field of FunctionDefinitionInfo for
 * FunctionDefinition nodes
 */
public class InitFunctionName extends GJNoArguDepthFirst<String> {

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public String visit(FunctionDefinition n) {
		String str = n.getF1().accept(this);
		n.getInfo().setFunctionName(str);
		return null;
	}

	/**
	 * f0 ::= ( Pointer() )?
	 * f1 ::= DirectDeclarator()
	 */
	@Override
	public String visit(Declarator n) {
		return n.getF1().accept(this);
	}

	/**
	 * f0 ::= IdentifierOrDeclarator()
	 */
	@Override
	public String visit(DirectDeclarator n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | "(" Declarator() ")"
	 */
	@Override
	public String visit(IdentifierOrDeclarator n) {
		if (n.getF0().getChoice() instanceof NodeToken) {
			return ((NodeToken) n.getF0().getChoice()).toString();
		} else {
			return ((NodeSequence) n.getF0().getChoice()).getNodes().get(1).accept(this);
		}
	}
}
