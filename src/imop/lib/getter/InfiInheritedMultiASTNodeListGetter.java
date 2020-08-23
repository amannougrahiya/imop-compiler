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
import java.util.Set;

/**
 * Populates the list astContents with all the internal AST nodes of any of the
 * types whose classId is present in {@code srarchCodeSet}, or its subclasses,
 * within the visited node (inclusively).
 * 
 * Note: This version is costlier than the one that captures nodes directly in
 * the appropriate visitors.
 * In case if a separate AST-node specific getter exists for this use, prefer it
 * over this.
 * 
 * @param <T>
 */

public class InfiInheritedMultiASTNodeListGetter extends DepthFirstProcess {
	public List<Node> astContents = new LinkedList<>();
	private Set<Integer> searchCodeSet;
	private TraverseExpressions traverseExpressions;

	public InfiInheritedMultiASTNodeListGetter(Set<Integer> searchCodeSet, TraverseExpressions traverseExpressions) {
		this.searchCodeSet = searchCodeSet;
		this.traverseExpressions = traverseExpressions;
		boolean found = false;
		for (int i : searchCodeSet) {
			if (i % 3 != 0) {
				found = true;
				break;
			}
		}
		if (!found) {
			this.traverseExpressions = TraverseExpressions.DONT_TRAVERSE_EXPRESSIONS;
		}
	}

	@Override
	public void endProcess(Node n) {
		for (int i : searchCodeSet) {
			if (n.getClassId() % i == 0) {
				astContents.add(n);
				return;
			}
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
