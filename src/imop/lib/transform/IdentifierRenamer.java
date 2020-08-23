/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform;

import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstVisitor;
import imop.lib.analysis.flowanalysis.Symbol;

/**
 * This class renames the occurrences of symbol oldSymbol with that of
 * newSymbol.
 * <br>
 * NOTE: This method is unsafe. It does not update the semantic data structures
 * automatically.
 */
public class IdentifierRenamer extends DepthFirstVisitor {
	public Symbol oldSymbol;
	public Symbol newSymbol;

	public IdentifierRenamer(Symbol oldSymbol, Symbol newSymbol) {
		this.oldSymbol = oldSymbol;
		this.newSymbol = newSymbol;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | Constant()
	 * | ExpressionClosed()
	 */
	@Override
	public void visit(PrimaryExpression n) {
		if (n.getF0().getChoice() instanceof NodeToken) {
			String symbolName = ((NodeToken) n.getF0().getChoice()).getTokenImage();
			if (!oldSymbol.getName().equals(symbolName)) {
				return;
			}
			((NodeToken) n.getF0().getChoice()).setTokenImage(newSymbol.getName());
		}
	}

}
