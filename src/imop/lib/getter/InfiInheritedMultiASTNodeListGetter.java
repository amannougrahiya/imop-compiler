/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import imop.ast.node.external.Node;
import imop.baseVisitor.DepthFirstProcess;

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

	public InfiInheritedMultiASTNodeListGetter(Set<Integer> searchCodeSet) {
		this.searchCodeSet = searchCodeSet;
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
}
