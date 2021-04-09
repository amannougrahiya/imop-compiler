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
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.util.CellSet;
import imop.lib.util.ImmutableCellSet;

/**
 * Represents {@code &sym} for some stack-element {@code sym}.
 * <br>
 * Note that this cell can only be read from, and not written to.
 * 
 * @author Aman Nougrahiya
 */
public class AddressCell extends Cell {
	/**
	 * Symbol whose address this address-cell represents.
	 */
	private final Symbol sym;
	private final HeapCell heapCell;
	private int cachedHashCode = -1;

	public AddressCell(HeapCell heapCell) {
		this.heapCell = heapCell;
		this.sym = null;
		if (!Cell.dontSave) {
			allCells.add(this);
		}
	}

	public AddressCell(Symbol sym) {
		assert (sym != null && !(sym.getType() instanceof ArrayType));
		this.heapCell = null;
		this.sym = sym;
		if (!Cell.dontSave) {
			allCells.add(this);
			// System.err.println("&" + sym.getName());
			// allCells.add(this);
			// if (sym.isAFunction()) {
			// Symbol.allFunctionCells.add(this);
			// } else {
			// allCells.add(this);
			// }
		}
	}

	public Cell getPointedElement() {
		return (sym == null) ? heapCell : sym;
	}

	@Override
	public ImmutableCellSet getPointsTo(Node node) {
		CellSet pointsToSet = new CellSet();
		if (this.sym != null) {
			pointsToSet.add(sym);
		} else if (this.heapCell != null) {
			pointsToSet.add(heapCell);
		}
		return new ImmutableCellSet(pointsToSet);
	}

	@Override
	public int hashCode() {
		if (cachedHashCode == -1) {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((heapCell == null) ? 0 : heapCell.hashCode());
			result = prime * result + ((sym == null) ? 0 : sym.hashCode());
			cachedHashCode = result;
		}
		return cachedHashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AddressCell other = (AddressCell) obj;
		if (cachedHashCode != other.cachedHashCode) {
			return false;
		}
		if (heapCell == null) {
			if (other.heapCell != null) {
				return false;
			}
		} else if (!heapCell.equals(other.heapCell)) {
			return false;
		}
		if (sym == null) {
			if (other.sym != null) {
				return false;
			}
		} else if (!sym.equals(other.sym)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String ret = "&";
		if (this.sym != null) {
			ret += sym.getName();
		} else if (this.heapCell != null) {
			ret += this.heapCell.toString();
		}
		return ret;
	}
}
