/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis;

import imop.ast.node.external.*;

public class Definition {
	private final Node definingNode;
	private final Cell cell;
	public int cachedHashCode = -1;

	public Definition(Node definingNode, Cell cell) {
		this.definingNode = definingNode;
		this.cell = cell;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Definition)) {
			return false;
		}
		Definition other = (Definition) o;
		if ((other.definingNode == this.definingNode) && (other.cell == this.cell)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		if (cachedHashCode == -1) {
			cachedHashCode = definingNode.getNodeId();
		}
		return cachedHashCode;
		// if (hashCode == -1) {
		// final int prime = 31;
		// hashCode = 1;
		// hashCode = prime * hashCode + ((this.getDefiningNode() == null) ? 0 :
		// this.getDefiningNode().hashCode());
		// hashCode = prime * hashCode + ((this.getCell() == null) ? 0 :
		// this.getCell().id);
		// }
		// return hashCode;
	}

	@Override
	public String toString() {
		if (this.cell instanceof Symbol) {
			return this.definingNode + " for symbol " + ((Symbol) this.cell).getName();
		} else if (this.cell instanceof HeapCell) {
			return this.definingNode + " for heap location at " + ((HeapCell) this.cell).getLocation();
		} else {
			return "";
		}
	}

	public Node getDefiningNode() {
		return definingNode;
	}

	public Cell getCell() {
		return cell;
	}

}
