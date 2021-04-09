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
import imop.lib.analysis.solver.Accessible;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.analysis.typeSystem.StructType;
import imop.lib.analysis.typeSystem.UnionType;
import imop.lib.util.CellSet;
import imop.lib.util.Immutable;
import imop.lib.util.ImmutableCellSet;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an abstraction of a memory cell.
 * Each object of this class is either a stack element (represented by the
 * corresponding {@link Symbol}), a heap-element (represented by the
 * corresponding {@link HeapCell}), a reference to the address of a
 * stack-element (represented by {@link AddressCell}),
 * or an abstraction of aggregate elements {@link FieldCell} (e.g.,
 * {@code arr.f} for an array {@code arr}).
 * <p>
 * Note that a Cell may also refer to a {@link FreeVariable}.
 * However, as of Tue Jul 11 23:38:05 IST 2017, free-variables are quite
 * immature.
 * <p>
 * NOTE: Although this class implements Immutable, the same needs to be tested
 * once.
 * 
 * @author Aman Nougrahiya
 *
 */
public class Cell implements Accessible, Immutable, Comparable<Cell> {

	public static int idCounter = 0;
	public final int id;

	protected Cell() {
		id = idCounter++;
	}

	public final static Set<Cell> allCells = new HashSet<>();
	public static Set<Cell> deletedCells = new HashSet<>();
	public static Set<Cell> allFunctionCells = new HashSet<>();
	public final static Cell genericCell = new Cell();
	public static boolean dontSave = false;
	private static Cell nullCell = new Cell();

	public static Cell getNullCell() {
		return Cell.nullCell;
	}

	public static void resetStaticFields() {
		Cell.allCells.clear();
		Cell.deletedCells = new HashSet<>();
		Cell.allFunctionCells = new HashSet<>();
	}

	/**
	 * Checks if this cell is a summary node (one that abstractly represents
	 * more than one memory elements.)
	 * 
	 * @return
	 *         true, if this cell is a summary node.
	 */
	public boolean isSummaryNode() {
		if (this instanceof Symbol) {
			Symbol sym = (Symbol) this;
			if (sym.getType() instanceof ArrayType) {
				return true;
			} else if (sym.getType() instanceof StructType || sym.getType() instanceof UnionType) {
				return true;
			}
		} else if (this instanceof HeapCell) {
			return true;
		} else if (this instanceof FreeVariable) {
			return false;
		} else if (this instanceof AddressCell) {
			return false;
		} else if (this instanceof FieldCell) {
			return true;
		}
		return false;
	}

	@Deprecated
	public CellSet deprecated_getPointsTo(Node node) {
		return null;
	}

	public ImmutableCellSet getPointsTo(Node node) {
		CellSet retSet = new CellSet();
		if (this == Cell.genericCell) {
			retSet.add(Cell.genericCell);
		} else if (this == Cell.getNullCell()) {
			retSet.add(Cell.getNullCell());
		}
		return new ImmutableCellSet(retSet);
	}

	@Override
	public String toString() {
		if (this == Cell.genericCell) {
			return "globalCell";
		} else if (this == Cell.getNullCell()) {
			return "nullCell";
		}
		assert (false);
		return "";
	}

	/**
	 * Removes the cell {@code cell} from the set of {@code deletedCells}, which
	 * is
	 * used to convert Symbols to FreeVariables while accessing any elements of
	 * Cell collections.
	 * 
	 * @param cell
	 *             cell that has to be removed from the set of deleted cells.
	 */
	public static void addCell(Cell cell) {
		deletedCells.remove(cell);
	}

	/**
	 * Adds the cell {@code cell} to the set of {@code deletedCells}, which is
	 * used to convert Symbols to FreeVariables while accessing any elements of
	 * Cell collections.
	 * 
	 * @param cell
	 *             cell that has to be added to the set of deleted cells.
	 */
	public static void removeCell(Cell cell) {
		deletedCells.add(cell);
	}

	@Override
	public int compareTo(Cell o) {
		return this.id - o.id;
	}
}
