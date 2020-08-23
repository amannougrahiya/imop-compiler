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

import java.util.LinkedList;
import java.util.List;

/**
 * Populates the field astContents with all the internal AST nodes of any of the
 * types
 * present in {@code astTypeList} within the visited node, inclusively.
 * Note: This version uses Reflections.
 * In case if a separate AST-node specific getter exists for this use, prefer it
 * over this.
 * 
 * @param <T>
 */

public class InfiInheritedSingleASTNodeTypeListGetter<T extends Node> extends DepthFirstProcess {
	public List<T> astContents = new LinkedList<>();
	private Class<T> astType;

	public InfiInheritedSingleASTNodeTypeListGetter(Class<T> astType) {
		this.astType = astType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initProcess(Node n) {
		if (astType.isInstance(n)) {
			astContents.add((T) n);
		}
	}
}
