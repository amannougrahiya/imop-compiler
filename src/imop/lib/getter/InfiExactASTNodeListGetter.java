/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import java.util.LinkedList;
import java.util.List;

import imop.ast.node.external.Node;
import imop.baseVisitor.DepthFirstProcess;

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

	public InfiExactASTNodeListGetter(int searchCode) {
		this.searchCode = searchCode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void endProcess(Node n) {
		if (n.getClassId() == searchCode) {
			astContents.add((T) n);
		}
	}
}