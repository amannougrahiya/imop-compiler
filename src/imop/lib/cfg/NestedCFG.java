/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;

/**
 * Corresponds to an internal CFG's begin and end nodes for a given AST node.
 * 
 * @author aman
 */

public class NestedCFG {
	private final BeginNode begin;
	private final EndNode end;
	private final Node node;

	public NestedCFG(Node node) {
		this.node = node;
		this.begin = new BeginNode(this);
		this.end = new EndNode(this);
	}

	public NestedCFG getCopy(Node newNode) {
		NestedCFG nCFG = new NestedCFG(newNode);
		return nCFG;
	}

	public BeginNode getBegin() {
		return begin;
	}

	public EndNode getEnd() {
		return end;
	}

	public Node getNode() {
		return node;
	}
}
