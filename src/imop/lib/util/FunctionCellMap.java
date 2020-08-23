/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import imop.lib.analysis.flowanalysis.Cell;

import java.util.HashMap;
import java.util.Set;

public class FunctionCellMap<T> extends CellMap<T> {

	public FunctionCellMap() {
		this.internalRepresentation = new HashMap<>();
	}

	public FunctionCellMap(CellMap<T> other) {
		super(other);
	}

	public static Set<Cell> getAllCell() {
		return Cell.allFunctionCells;
	}
}
