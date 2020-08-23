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
 * Sets <code>paramListFound</code> to true, if a parameter-list is found in the
 * traversed
 * sub-tree.
 */
public class HasParameterList extends DepthFirstProcess {
	public boolean paramListFound = false;

	/**
	 * f0 ::= "("
	 * f1 ::= ( ParameterTypeList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ParameterTypeListClosed n) {
		paramListFound = true;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( OldParameterList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(OldParameterListClosed n) {
		paramListFound = true;
	}

}
