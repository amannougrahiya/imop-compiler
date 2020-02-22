/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import java.util.ArrayList;
import java.util.List;

import imop.ast.node.external.APostfixOperation;
import imop.ast.node.external.ArgumentList;
import imop.ast.node.external.FunctionDefinition;
import imop.ast.node.external.Node;
import imop.ast.node.external.NodeToken;
import imop.ast.node.external.PostfixExpression;
import imop.ast.node.external.SizeofUnaryExpression;
import imop.baseVisitor.DepthFirstVisitor;
import imop.lib.cg.CallSite;
import imop.parser.Program;

/**
 * Populates callSiteList with the call sites within the visited node's AST
 * subtree.
 */
public class CallSiteGetter extends DepthFirstVisitor {
	public List<CallSite> callSiteList = new ArrayList<>();

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public void visit(PostfixExpression n) {
		List<Node> opList = n.getF1().getF0().getNodes();
		if ((!opList.isEmpty()) && ((APostfixOperation) opList.get(0)).getF0().getChoice() instanceof ArgumentList) { // If a call site found
			if (n.getF0().getF0().getChoice() instanceof NodeToken) { // If call site doesn't use a function pointer (?)
				// Get the Callee's name and body
				String name = ((NodeToken) n.getF0().getF0().getChoice()).getTokenImage();
				FunctionDefinitionGetter funcGetter = new FunctionDefinitionGetter();
				Program.getRoot().accept(funcGetter, name);

				// Add the callSite
				if (funcGetter.funcDefNodes.size() != 0) { // If callee's body is available
					FunctionDefinition calledFunction = funcGetter.funcDefNodes.get(0);
					callSiteList.add(new CallSite(name, calledFunction, n));
				} else {
					callSiteList.add(new CallSite(name, null, n));
				}
			}
		}
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(SizeofUnaryExpression n) {
		n.getF0().accept(this);
		//n.f1.accept(this);
		// Expression is not evaluated when it is present as an argument to the sizeof operator
	}
}
