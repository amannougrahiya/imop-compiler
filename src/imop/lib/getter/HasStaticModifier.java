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

public class HasStaticModifier extends DepthFirstVisitor {
	public boolean foundStatic = false;

	/**
	 * f0 ::= <AUTO>
	 * | <REGISTER>
	 * | <STATIC>
	 * | <EXTERN>
	 * | <TYPEDEF>
	 */
	@Override
	public void visit(StorageClassSpecifier n) {
		if (((NodeToken) n.getF0().getChoice()).getTokenImage().equals("static")) {
			foundStatic = true;
		}
	}

}
