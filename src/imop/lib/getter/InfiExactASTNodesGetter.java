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
 * Populates the field astContents with all the internal AST nodes of type T
 * within the visited node.
 * Note: This version uses Reflections.
 * In case if a separate AST-node specific getter exists for this use, prefer it
 * over this.
 */

public class InfiExactASTNodesGetter extends DepthFirstProcess {
	public Set<Node> astContents = new HashSet<>();
	Set<Class<?>> astTypeSet;

	public InfiExactASTNodesGetter(Class<?> astType) {
		this.astTypeSet = new HashSet<>();
		this.astTypeSet.add(astType);
	}

	public InfiExactASTNodesGetter(Set<Class<?>> astTypeSet) {
		this.astTypeSet = astTypeSet;
	}

	@Override
	public void initProcess(Node n) {
		if (astTypeSet.contains(n.getClass())) {
			astContents.add(n);
		}
	}
}
