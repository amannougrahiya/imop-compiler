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
import imop.lib.util.CellSet;
import imop.lib.util.ImmutableCellSet;

public class FieldCell extends Cell {
	public final Symbol sym;
	public int cachedHashCode = -1;

	FieldCell(Symbol sym) {
		assert (sym != null);
		this.sym = sym;
		if (!Cell.dontSave) {
			allCells.add(this);
		}
	}

	public Symbol getAggregateElement() {
		return sym;
	}

	@Override
	public CellSet deprecated_getPointsTo(Node node) {
		CellSet pointsToSet = new CellSet();
		pointsToSet.add(this); // Note the difference here, as compared to an AddressCell.
		return pointsToSet;
	}

	@Override
	public ImmutableCellSet getPointsTo(Node node) {
		CellSet pointsToSet = new CellSet();
		pointsToSet.add(this); // Note the difference here, as compared to an AddressCell.
		return new ImmutableCellSet(pointsToSet);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FieldCell)) {
			return false;
		}
		FieldCell that = (FieldCell) obj;
		if (this.sym.equals(that.sym)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		if (cachedHashCode == -1) {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((sym == null) ? 0 : sym.hashCode());
			cachedHashCode = result;
		}
		return cachedHashCode;
	}

	@Override
	public String toString() {
		String ret = "";
		ret += sym.getName() + ".f";
		return ret;
	}
}
