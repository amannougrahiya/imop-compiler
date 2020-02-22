/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

import imop.lib.cg.NodeWithStack;
import imop.lib.util.CellSet;

/**
 * Denotes an edge between nodes in the domain of this IDFA.<br>
 * Note that these edges need not contain a reference to either source or
 * destination, but only one of them.
 * 
 * @author aman
 *
 */
public class IDFAEdgeWithStack {
	private final NodeWithStack nodeWithStack;
	private final CellSet cells;

	public IDFAEdgeWithStack(NodeWithStack node, CellSet cells) {
		this.nodeWithStack = node;
		this.cells = cells;
	}

	public NodeWithStack getNodeWithStack() {
		return nodeWithStack;
	}

	public CellSet getCells() {
		return cells;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof IDFAEdgeWithStack)) {
			return false;
		}
		IDFAEdgeWithStack other = (IDFAEdgeWithStack) obj;
		if (this.nodeWithStack != other.nodeWithStack) {
			return false;
		}
		if (this.cells == null && other.cells == null) {
			return true;
		} else if (this.cells == null || other.cells == null) {
			return false;
		}
		if (this.cells.equals(other.cells)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeWithStack == null) ? 0 : nodeWithStack.hashCode());
		result = prime * result + ((cells == null) ? 0 : cells.size());
		return result;
	}
}
