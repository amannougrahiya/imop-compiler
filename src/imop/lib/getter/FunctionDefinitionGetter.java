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
import imop.baseVisitor.GJDepthFirst;

import java.util.Vector;

/**
 * Populates funcDefNodes with the FunctionDefinition nodes corresponding to
 * the function name passed via argu.
 */
public class FunctionDefinitionGetter extends GJDepthFirst<String, String> {
	public Vector<FunctionDefinition> funcDefNodes = new Vector<>();

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public String visit(FunctionDefinition n, String argu) {
		String _ret = null;
		String str = n.getF1().accept(this, argu);
		if (str.equals(argu)) {
			funcDefNodes.add(n);
		}
		return _ret;
	}

	/**
	 * f0 ::= ( Pointer() )?
	 * f1 ::= DirectDeclarator()
	 */
	@Override
	public String visit(Declarator n, String argu) {
		return n.getF1().accept(this, argu);
	}

	/**
	 * f0 ::= IdentifierOrDeclarator()
	 */
	@Override
	public String visit(DirectDeclarator n, String argu) {
		return n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | "(" Declarator() ")"
	 */
	@Override
	public String visit(IdentifierOrDeclarator n, String argu) {
		if (n.getF0().getChoice() instanceof NodeToken) {
			return ((NodeToken) n.getF0().getChoice()).toString();
		} else {
			return ((NodeSequence) n.getF0().getChoice()).getNodes().get(1).accept(this, argu);
		}
	}
}
