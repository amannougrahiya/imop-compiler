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
 * <p>
 * Note: This version uses Reflections.
 * In case if a separate AST-node specific getter exists for this use, prefer it
 * over this.
 */

public class InfiInheritedASTNodesGetter extends DepthFirstProcess {
	public Set<Node> astContents = new HashSet<>();
	Class astType;

	public InfiInheritedASTNodesGetter(Class astType) {
		this.astType = astType;
	}

	@Override
	public void initProcess(Node n) {
		if (astType.isInstance(n)) {
			astContents.add(n);
		}
	}
}
