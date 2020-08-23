/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

import imop.ast.node.external.*;
import imop.lib.util.CellSet;

/**
 * Denotes an edge between nodes in the domain of this IDFA.<br>
 * Note that these edges need not contain a reference to either source or
 * destination, but only one of them.
 * 
 * @author aman
 *
 */
public class IDFAEdge {
	private Node node;
	private CellSet cells;

	public IDFAEdge(Node node, CellSet cells) {
		this.node = node;
		this.cells = cells;
	}

	public Node getNode() {
		return node;
	}

	public CellSet getCells() {
		return cells;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof IDFAEdge)) {
			return false;
		}
		IDFAEdge other = (IDFAEdge) obj;
		if (this.node != other.node) {
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
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime * result + ((cells == null) ? 0 : cells.size());
		return result;
	}
}
