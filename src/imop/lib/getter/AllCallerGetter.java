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
import imop.baseVisitor.GJVoidDepthFirst;
import imop.lib.cg.CallSite;

import java.util.ArrayList;
import java.util.List;

/**
 * Populates allCallerList with all the callSites from where @param argu has
 * been called.
 */
@Deprecated
public class AllCallerGetter extends GJVoidDepthFirst<FunctionDefinition> {
	public List<CallSite> allCallerList = new ArrayList<>();

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public void visit(PostfixExpression n, FunctionDefinition argu) {
		String functionName = argu.getInfo().getFunctionName();
		List<Node> opList = n.getF1().getF0().getNodes();
		if ((!opList.isEmpty()) && ((APostfixOperation) opList.get(0)).getF0().getChoice() instanceof ArgumentList) { // If
																														// a
																														// call
																														// site
																														// found
			if (n.getF0().getF0().getChoice() instanceof NodeToken) { // If call site doesn't use a function pointer (?)
				// Get the Callee's name and body
				String name = ((NodeToken) n.getF0().getF0().getChoice()).getTokenImage();
				if (name.equals(functionName)) {
					// Add the callSite
					allCallerList.add(new CallSite(name, argu, n));
				}
			}
		}
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(SizeofUnaryExpression n, FunctionDefinition argu) {
		n.getF0().accept(this, argu);
		// n.f1.accept(this, argu);
		// Expression is not evaluated when it is present as an argument to the sizeof
		// operator
	}
}
