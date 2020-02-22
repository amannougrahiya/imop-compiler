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
import java.util.Set;

import imop.ast.node.external.Node;
import imop.baseVisitor.DepthFirstProcess;

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

public class InfiInheritedASTNodeListGetter<T extends Node> extends DepthFirstProcess {
	public List<Node> astContents = new LinkedList<>();
	private Set<Class<T>> astTypeList;

	public InfiInheritedASTNodeListGetter(Set<Class<T>> astType) {
		this.astTypeList = astType;
	}

	@Override
	public void initProcess(Node n) {
		for (Class<T> cls : astTypeList) {
			if (cls.isInstance(n)) {
				astContents.add(n);
			}
		}
	}
}
