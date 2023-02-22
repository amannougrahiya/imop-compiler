/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.projects.datarace;

import imop.ast.node.CompoundStatement;
import imop.baseVisitor.DepthFirstVisitor;
import imop.lib.util.Misc;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order. Your visitors may extend this class.
 */
public class CompoundStatementNormalizer extends DepthFirstVisitor {

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public void visit(CompoundStatement n) {
		// Normalize the CS
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		Misc.normalizeNode(n);
	}

}
