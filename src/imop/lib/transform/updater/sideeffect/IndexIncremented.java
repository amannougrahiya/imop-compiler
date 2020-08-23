/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.updater.sideeffect;

import imop.ast.node.external.*;

public class IndexIncremented extends PredecessorUpdated {
	protected Node baseNode;

	public IndexIncremented(Node affectedNode, Node baseNode) {
		super(affectedNode);
		this.baseNode = baseNode;
	}

	/**
	 * Returns the node relative to which the index has been incremented.
	 * <br>
	 * Note that the node could as well have been intended to be removed.
	 * 
	 * @return
	 */
	public Node getBaseNode() {
		return baseNode;
	}

}
