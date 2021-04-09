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
import imop.lib.util.Misc.TraverseExpressions;

import java.util.LinkedList;
import java.util.List;

/**
 * Populates the list astContents with all the internal AST nodes of the type
 * corresponding to {@code searchCode}.
 * Note: This version is costlier than the one that captures nodes directly in
 * the appropriate visitors.
 * In case if a separate AST-node specific getter exists for this use, prefer it
 * over this.
 * 
 * @param <T>
 */

public class InfiExactASTNodeListGetter<T extends Node> extends DepthFirstProcess {
	public List<T> astContents = new LinkedList<>();
	private final int searchCode;
	private TraverseExpressions traverseExpressions;

	public InfiExactASTNodeListGetter(int searchCode, TraverseExpressions traverseExpressions) {
		this.searchCode = searchCode;
		this.traverseExpressions = traverseExpressions;
		if (searchCode % 3 == 0) {
			this.traverseExpressions = TraverseExpressions.DONT_TRAVERSE_EXPRESSIONS;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void endProcess(Node n) {
		if (n.getClassId() == searchCode) {
			astContents.add((T) n);
		}
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(Expression n) {
		if (traverseExpressions == TraverseExpressions.DONT_TRAVERSE_EXPRESSIONS) {
			return;
		}
		initProcess(n);
		n.getExpF0().accept(this);
		n.getExpF1().accept(this);
		endProcess(n);
	}

}
