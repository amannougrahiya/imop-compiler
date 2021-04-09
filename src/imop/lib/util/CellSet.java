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
import imop.lib.analysis.flowanalysis.FreeVariable;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.parser.Program;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CellSet extends CellCollection {

	public CellSet() {
		super(false);
		internalRepresentation = new HashSet<>();
	}

	public CellSet(CellSet c) {
		super(c.isDerivedFromUniversal);
		this.freeVariableCount = c.freeVariableCount;
		internalRepresentation = new HashSet<>(c.internalRepresentation);
		if (c.containsUniversal) {
			this.containsUniversal = true;
		}
	}

	public CellSet(CellList c) {
		super(c.isDerivedFromUniversal);
		this.freeVariableCount = c.freeVariableCount;
		internalRepresentation = new HashSet<>(c.internalRepresentation);
		if (c.containsUniversal) {
			this.containsUniversal = true;
		}
	}

	/**
	 * Create a wrapper CellSet object for the given set. Note that the
	 * {@code cellSet} is not copied, and is used as it
	 * is.
	 *
	 * @param cellSet
	 */
	public CellSet(Set<Cell> cellSet) {
		super((cellSet.contains(Cell.genericCell)) ? true : false);
		this.internalRepresentation = cellSet;
	}

	@Override
	public int size() {
		if (this.isUniversal()) {
			return getAllCells().size();
		} else {
			return this.internalRepresentation.size();
		}
	}

	@Override
	public boolean add(Cell s) {
		if (s == null) {
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature("Cannot add a null symbol.", Program.getRoot());
			return false;
		}
		if (s == Cell.genericCell) {
			if (isUniversal()) {
				return false;
			} else {
				internalRepresentation.clear();
				internalRepresentation.add(s);
				this.freeVariableCount = Integer.MAX_VALUE;
				this.containsUniversal = true;
				// (int) getAllCells().stream().filter((x) -> {
				// return x instanceof FreeVariable;
				// }).count();
				CellCollection.addCollectionToDerived(this);
				return true;
			}
		} else {
			if (isUniversal()) {
				return false;
			} else {
				if (s instanceof FreeVariable) {
					freeVariableCount++;
				}
				return ((Set<Cell>) internalRepresentation).add(s);
			}
		}
	}

	@Override
	public boolean remove(Object o) {
		if (!(o instanceof Cell)) {
			return false;
		}
		Cell s = (Cell) o;
		if (s == Cell.genericCell) {
			this.containsUniversal = false;
			if (isUniversal()) {
				CellCollection.removeCollectionFromDerived(this);
				internalRepresentation.clear();
				freeVariableCount = 0;
				return true;
			} else {
				CellCollection.removeCollectionFromDerived(this);
				internalRepresentation.clear();
				freeVariableCount = 0;
				return true;
			}
		} else {
			if (isUniversal()) {
				internalRepresentation = new HashSet<>(getAllCells());
				this.freeVariableCount = Integer.MAX_VALUE;
				this.containsUniversal = false;
				// getAllCells().stream().filter((x) -> {
				// return x instanceof FreeVariable;
				// }).toArray().length;
				internalRepresentation.remove(s);
				if (s instanceof FreeVariable) {
					freeVariableCount--;
				}
				CellCollection.addCollectionToDerived(this);
				return true;
			} else {
				boolean removed = ((Set<Cell>) internalRepresentation).remove(s);
				if (removed) {
					if (s instanceof FreeVariable) {
						freeVariableCount--;
					}
				}
				return removed;
			}
		}
	}

	@Override
	public Object clone() {
		CellSet newSet = new CellSet(this);
		return newSet;
	}

	@Override
	public boolean removeAll(Collection<?> coll) {
		if (!(coll instanceof CellSet)) {
			return false;
		}
		CellSet c = (CellSet) coll;
		if (c.isUniversal()) {
			this.containsUniversal = false;
			if (isUniversal()) {
				CellCollection.removeCollectionFromDerived(this);
				internalRepresentation.clear();
				this.freeVariableCount = 0;
				return true;
			} else {
				if (internalRepresentation.isEmpty()) {
					return false;
				} else {
					CellCollection.removeCollectionFromDerived(this);
					internalRepresentation.clear();
					this.freeVariableCount = 0;
					return true;
				}
			}
		} else {
			if (isUniversal()) {
				internalRepresentation.clear();
				this.containsUniversal = false;
				for (Cell s : getAllCells()) {
					if (!c.contains(s)) {
						internalRepresentation.add(s);
					}
				}
				CellCollection.addCollectionToDerived(this);
				return true;
			} else {
				return ((Set<Cell>) internalRepresentation).removeAll(c.internalRepresentation);
			}
		}
	}

	public boolean containsAll(CellSet c) {
		if (c.isUniversal()) {
			if (isUniversal()) {
				return true;
			} else {
				return false;
			}
		} else {
			if (isUniversal()) {
				return true;
			} else {
				this.testAndConvert();
				c.testAndConvert();
				return ((Set<Cell>) internalRepresentation).containsAll(c.internalRepresentation);
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((internalRepresentation == null) ? 0 : internalRepresentation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CellSet)) {
			return false;
		}
		CellSet other = (CellSet) obj;
		if (internalRepresentation == null) {
			if (other.internalRepresentation != null) {
				return false;
			}
		} else if (!internalRepresentation.equals(other.internalRepresentation)) {
			return false;
		}
		return true;
	}

	public boolean overlapsWith(CellSet c) {
		if (c.isUniversal()) {
			if (isUniversal()) {
				return true;
			} else {
				return true;
			}
		} else {
			if (isUniversal()) {
				return true;
			} else {
				this.testAndConvert();
				c.testAndConvert();
				return Misc.doIntersect(this.internalRepresentation, c.internalRepresentation);
			}
		}
	}

	@Override
	public void testAndConvert() {
		if (this.isUniversal()) {
			return;
		}
		if (!this.hasFreeVariables()) {
			return;
		}

		Object[] cellList = ((Set<Cell>) this.internalRepresentation).toArray();
		for (int i = 0; i < cellList.length; i++) {
			Cell c = (Cell) cellList[i];
			if (c instanceof FreeVariable) {
				Symbol sym = Misc.getSymbolEntry(((FreeVariable) c).getFreeVariableName(),
						((FreeVariable) c).getNodeToken());
				if (sym != null) {
					CellCollection.removeCellFromDerivedCollections(c);
					this.internalRepresentation.remove(c);
					this.internalRepresentation.add(sym);
				}
			} else if (c instanceof Symbol) {
				Symbol sym = (Symbol) c;
				if (Cell.deletedCells.contains(sym)) {
					// TODO: Check why should the code reach here in clomp.
					// assert (false);
				}
			}
		}
	}

	public boolean testAndConvert(Cell c) {
		if (c == Cell.genericCell) {
			return false;
		}
		if (!this.hasFreeVariables()) {
			return false;
		}
		if (!(c instanceof FreeVariable)) {
			return false;
		}
		boolean changed = false;
		if (!this.internalRepresentation.contains(c)) {
			return false;
		}
		Symbol sym = Misc.getSymbolEntry(((FreeVariable) c).getFreeVariableName(), ((FreeVariable) c).getNodeToken());
		if (sym != null) {
			CellCollection.removeCellFromDerivedCollections(c);
			this.internalRepresentation.remove(c);
			this.internalRepresentation.add(sym);
			changed = true;
		}
		return changed;
	}

	@Override
	public boolean hasDeletedSymbols() {
		if (Cell.deletedCells.isEmpty()) {
			return false;
		} else {
			if (Misc.doIntersect(Cell.deletedCells, this.internalRepresentation)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public Cell getAnyElement() {
		if (isUniversal()) {
			return Cell.genericCell;
		}
		for (Cell c : this) {
			return c;
		}
		return null;
	}

}
