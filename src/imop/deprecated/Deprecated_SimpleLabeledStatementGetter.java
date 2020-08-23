/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.deprecated;

import imop.ast.node.external.*;
import imop.baseVisitor.GJVoidDepthFirst;

/**
 * This visitor visits over the given AST and populates its field labelNode
 * with the statement having argu as the label.
 */
@Deprecated
public class Deprecated_SimpleLabeledStatementGetter extends GJVoidDepthFirst<String> {
	public SimpleLabeledStatement labelNode = null;

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(SimpleLabeledStatement n, String argu) {
		if (n.getF0().getTokenImage().equals(argu)) {
			labelNode = n;
			return;
		}
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		return;
	}

}
