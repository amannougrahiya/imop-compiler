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
import imop.baseVisitor.DepthFirstProcess;

/**
 * Populates longFound with true if long is found as a type specifier
 */
public class HasLongType extends DepthFirstProcess {
	public boolean longFound = false;

	/**
	 * f0 ::= ( <VOID> | <CHAR> | <SHORT> | <INT> | <LONG> | <FLOAT> | <DOUBLE>
	 * |
	 * <SIGNED> | <UNSIGNED> | StructOrUnionSpecifier() | EnumSpecifier() |
	 * TypedefName() )
	 */
	@Override
	public void visit(TypeSpecifier n) {
		initProcess(n);
		if (n.getF0().getChoice() instanceof NodeToken) {
			if (((NodeToken) n.getF0().getChoice()).getTokenImage().equals("long")) {
				longFound = true;
			}
		}
		endProcess(n);
	}

}
