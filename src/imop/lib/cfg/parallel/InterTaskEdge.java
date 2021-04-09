/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.parallel;

import imop.ast.node.internal.*;

public class InterTaskEdge {
	private final DummyFlushDirective sourceNode;
	private final DummyFlushDirective destinationNode;

	/**
	 * 
	 * @param n1
	 * @param n2
	 */
	public InterTaskEdge(DummyFlushDirective sourceNode, DummyFlushDirective destinationNode) {
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
	}

	public DummyFlushDirective getSourceNode() {
		return sourceNode;
	}

	public DummyFlushDirective getDestinationNode() {
		return destinationNode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof InterTaskEdge)) {
			return false;
		}
		InterTaskEdge other = (InterTaskEdge) obj;
		if (this.sourceNode == other.sourceNode && this.destinationNode == other.destinationNode) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceNode == null) ? 0 : sourceNode.hashCode());
		result = prime * result + ((destinationNode == null) ? 0 : destinationNode.hashCode());
		return result;
		// return Arrays.hashCode(new Object[] {sourceNode, destinationNode});
	}

}
