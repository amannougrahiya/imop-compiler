/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import java.util.Set;

import imop.lib.analysis.flowanalysis.Cell;

public abstract class FunctionCellCollection extends CellCollection {

	public FunctionCellCollection(boolean isDerivedFromUniversal) {
		super(isDerivedFromUniversal);
	}

	public static Set<Cell> getAllCells() {
		return Cell.allFunctionCells;
	}

}
