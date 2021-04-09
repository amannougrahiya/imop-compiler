/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.deprecated;

import imop.baseVisitor.DepthFirstVisitor;

@Deprecated
public class Deprecated_TemporaryDataAttChecker extends DepthFirstVisitor {
	// public boolean foundStatic = false;
	//
	// private SymbolList printedAlready = new SymbolList();
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * | Constant()
	// * | ExpressionClosed()
	// */
	// @Override
	// public void visit(PrimaryExpression n) {
	// if (n.getF0().getChoice() instanceof NodeToken) {
	// Symbol sym = Misc.getSymbolEntry(((NodeToken)
	// n.getF0().getChoice()).getTokenImage(), n);
	// if (sym != null) {
	// if (!this.printedAlready.contains(sym)) {
	// this.printedAlready.add(sym);
	// System.out.println(sym.getName() + " is " +
	// n.getInfo().getSharingAttribute(sym) + "; ");
	// }
	// }
	// }
	// n.getF0().accept(this);
	// }
	//
}
