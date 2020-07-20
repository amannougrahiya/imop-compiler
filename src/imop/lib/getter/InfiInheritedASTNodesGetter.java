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
import java.util.Set;

import imop.ast.node.external.Node;
import imop.baseVisitor.DepthFirstProcess;

/**
 * Populates the set astContents with all the internal AST nodes of type
 * corresponding to {@code searchCode}, or its subclasses.
 * Note: This version is costlier than the one that captures nodes directly in
 * the appropriate visitors.
 * In case if a separate AST-node specific getter exists for this use, prefer it
 * over this.
 */

public class InfiInheritedASTNodesGetter<T extends Node> extends DepthFirstProcess {
	public Set<T> astContents = new HashSet<>();
	private final int searchCode;

	public InfiInheritedASTNodesGetter(int searchCode) {
		this.searchCode = searchCode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void endProcess(Node n) {
		// Note that in this version, unlike exactEnclosee, we check for remainder.
		if (n.getClassId() % searchCode == 0) {
			astContents.add((T) n);
		}
	}
}
