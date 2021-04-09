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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.UnaryOperator;

public class CellList extends CellCollection {

	public CellList() {
		super(false);
		this.internalRepresentation = new LinkedList<>();
		this.freeVariableCount = 0;
	}

	public CellList(CellList otherList) {
		super(otherList.isDerivedFromUniversal);
		this.freeVariableCount = otherList.freeVariableCount;
		this.internalRepresentation = new LinkedList<>(otherList.internalRepresentation);
		if (otherList.containsUniversal) {
			this.containsUniversal = true;
		}
	}

	public CellList(CellSet otherSet) {
		super(otherSet.isDerivedFromUniversal);
		this.freeVariableCount = otherSet.freeVariableCount;
		this.internalRepresentation = new LinkedList<>(otherSet.internalRepresentation);
		if (otherSet.containsUniversal) {
			this.containsUniversal = true;
		}
	}

	@Override
	public int size() {
		if (this.isUniversal()) {
			return getAllCells().size();
		} else {
			return this.internalRepresentation.size();
		}
	}

	public int indexOf(Cell s) {
		this.testAndConvert();
		return ((LinkedList<Cell>) internalRepresentation).indexOf(s);
	}

	public int lastIndexOf(Cell s) {
		this.testAndConvert();
		return ((LinkedList<Cell>) internalRepresentation).lastIndexOf(s);
	}

	@Override
	public Object clone() {
		CellList newSet = new CellList(this);
		if (this.isUniversal()) {
			newSet.containsUniversal = true;
		}
		return newSet;
	}

	public Cell get(int index) {
		if (this.isUniversal()) {
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature("Trying to fetch from a universal CellList.", Program.getRoot());
			return Cell.genericCell;
		} else {
			this.testAndConvert(index);
			return ((LinkedList<Cell>) internalRepresentation).get(index);
		}
	}

	public Cell set(int index, Cell s) {
		if (s == null) {
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature("Cannot add a null symbol to a CellList.", Program.getRoot());
			return this.get(index);
		}
		if (s == Cell.genericCell) {
			if (this.isUniversal()) {
				return s;
			} else {
				internalRepresentation.clear();
				internalRepresentation.add(s);
				this.freeVariableCount = Integer.MAX_VALUE;
				this.containsUniversal = true;
				// this.freeVariableCount = getAllCells().stream().filter((x) -> {
				// return x instanceof FreeVariable;
				// }).toArray().length;
				CellCollection.addCollectionToDerived(this);
				return s;
			}
		} else {
			if (this.isUniversal()) {
				internalRepresentation.clear();
				this.freeVariableCount = 0;
				this.add(s);
				CellCollection.removeCollectionFromDerived(this);
				return Cell.genericCell;
			} else {
				Cell old = ((LinkedList<Cell>) this.internalRepresentation).get(index);
				if (old instanceof FreeVariable) {
					this.freeVariableCount--;
				}
				((LinkedList<Cell>) this.internalRepresentation).set(index, s);
				if (s instanceof FreeVariable) {
					this.freeVariableCount++;
				}
				return old;
			}
		}
	}

	@Override
	public boolean add(Cell s) {
		if (s == null) {
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature("Cannot add a null symbol to a CellList.", Program.getRoot());
			return false;
		}
		if (s == Cell.genericCell) {
			if (this.isUniversal()) {
				return false;
			} else {
				internalRepresentation.clear();
				internalRepresentation.add(s);
				this.freeVariableCount = Integer.MAX_VALUE;
				this.containsUniversal = true;
				// this.freeVariableCount = getAllCells().stream().filter((x) -> {
				// return x instanceof FreeVariable;
				// }).toArray().length;
				CellCollection.addCollectionToDerived(this);
				return true;
			}
		} else {
			if (this.isUniversal()) {
				return false;
			} else {
				if (s instanceof FreeVariable) {
					freeVariableCount++;
				}
				return ((LinkedList<Cell>) internalRepresentation).add(s);
			}
		}
	}

	public void add(int index, Cell s) {
		if (s == null) {
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature("Cannot add a null symbol to a CellList.", Program.getRoot());
			return;
		} else if (s == Cell.genericCell) {
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature("The index of universal element should not be specified in a CellList.",
					Program.getRoot());
			internalRepresentation.clear();
			internalRepresentation.add(s);
			this.freeVariableCount = Integer.MAX_VALUE;
			this.containsUniversal = true;
			// this.freeVariableCount = getAllCells().stream().filter((x) -> {
			// return x instanceof FreeVariable;
			// }).toArray().length;
			CellCollection.addCollectionToDerived(this);
			return;
		} else {
			if (this.isUniversal()) {
				return;
			} else {
				if (s instanceof FreeVariable) {
					freeVariableCount++;
				}
				((LinkedList<Cell>) internalRepresentation).add(index, s);
			}
		}
	}

	public Cell remove(int index) {
		if (this.isUniversal()) {
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature("The index of universal element should not be specified in a CellList.",
					Program.getRoot());
			this.internalRepresentation.clear();
			this.freeVariableCount = 0;
			this.containsUniversal = false;
			return Cell.genericCell;
		}
		Cell old = ((LinkedList<Cell>) internalRepresentation).remove(index);
		if (old instanceof FreeVariable) {
			freeVariableCount--;
		}
		return old;
	}

	public boolean remove(Cell s) {
		if (s == Cell.genericCell) {
			if (this.isUniversal()) {
				CellCollection.removeCollectionFromDerived(this);
				internalRepresentation.clear();
				freeVariableCount = 0;
				this.containsUniversal = false;
				return true;
			} else {
				CellCollection.removeCollectionFromDerived(this);
				internalRepresentation.clear();
				freeVariableCount = 0;
				return true;
			}
		} else {
			if (this.isUniversal()) {
				internalRepresentation = new LinkedList<>(getAllCells());
				this.freeVariableCount = Integer.MAX_VALUE;
				this.containsUniversal = false;
				// this.freeVariableCount = getAllCells().stream().filter((x) -> {
				// return x instanceof FreeVariable;
				// }).toArray().length;
				internalRepresentation.remove(s);
				if (s instanceof FreeVariable) {
					freeVariableCount--;
				}
				CellCollection.addCollectionToDerived(this);
				return true;
			} else {
				boolean removed = ((LinkedList<Cell>) internalRepresentation).remove(s);
				if (removed) {
					if (s instanceof FreeVariable) {
						freeVariableCount--;
					}
				}
				return removed;
			}
		}
	}

	public boolean removeAll(CellList c) {
		if (c.isUniversal()) {
			if (this.isUniversal()) {
				CellCollection.removeCollectionFromDerived(this);
				internalRepresentation.clear();
				this.freeVariableCount = 0;
				this.containsUniversal = false;
				return true;
			} else {
				if (internalRepresentation.isEmpty()) {
					return false;
				} else {
					CellCollection.removeCollectionFromDerived(this);
					internalRepresentation.clear();
					this.freeVariableCount = 0;
					this.containsUniversal = false;
					return true;
				}
			}
		} else {
			if (this.isUniversal()) {
				internalRepresentation.clear();
				this.freeVariableCount = 0;
				this.containsUniversal = false;
				for (Cell s : getAllCells()) {
					if (!c.contains(s)) {
						if (s instanceof FreeVariable) {
							this.freeVariableCount++;
						}
						internalRepresentation.add(s);
					}
				}
				CellCollection.addCollectionToDerived(this);
				return true;
			} else {
				this.testAndConvert();
				c.testAndConvert();
				return ((LinkedList<Cell>) internalRepresentation).removeAll(c.internalRepresentation);
			}
		}
	}

	public boolean containsAll(CellList c) {
		if (c.isUniversal()) {
			if (this.isUniversal()) {
				return true;
			} else {
				return false;
			}
		} else {
			if (this.isUniversal()) {
				return true;
			} else {
				this.testAndConvert();
				c.testAndConvert();
				return ((LinkedList<Cell>) internalRepresentation).containsAll(c.internalRepresentation);
			}
		}
	}

	public void replaceAll(UnaryOperator<Cell> operator) {
		if (this.isUniversal()) {
			internalRepresentation = new LinkedList<>(getAllCells());
			this.freeVariableCount = Integer.MAX_VALUE;
			this.containsUniversal = false;
			// this.freeVariableCount = getAllCells().stream().filter((x) -> {
			// return x instanceof FreeVariable;
			// }).toArray().length;
			((LinkedList<Cell>) internalRepresentation).replaceAll(operator);
			CellCollection.addCollectionToDerived(this);
		} else {
			this.testAndConvert();
			((LinkedList<Cell>) internalRepresentation).replaceAll(operator);
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
		if (!(obj instanceof CellList)) {
			return false;
		}
		CellList other = (CellList) obj;
		if (internalRepresentation == null) {
			if (other.internalRepresentation != null) {
				return false;
			}
		} else if (!internalRepresentation.equals(other.internalRepresentation)) {
			return false;
		}
		return true;
	}

	public boolean isOverlapping(CellList c) {
		if (c.isUniversal()) {
			if (this.isUniversal()) {
				return true;
			} else {
				return true;
			}
		} else {
			if (this.isUniversal()) {
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
		if (this.hasFreeVariables()) {
			int index = 0;
			for (int i = 0; i < this.internalRepresentation.size(); i++) {
				Cell c = ((LinkedList<Cell>) this.internalRepresentation).get(i);
				if (c instanceof FreeVariable) {
					Symbol sym = Misc.getSymbolEntry(((FreeVariable) c).getFreeVariableName(),
							((FreeVariable) c).getNodeToken());
					if (sym != null) {
						CellCollection.removeCellFromDerivedCollections(c);
						((LinkedList<Cell>) this.internalRepresentation).set(index, sym);
					}
				} else if (c instanceof Symbol) {
					Symbol sym = (Symbol) c;
					if (Cell.deletedCells.contains(sym)) {
						assert (false);
					}
				}
				index++;
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
		int index = this.indexOf(c);
		if (index == -1) {
			return false;
		}
		Symbol sym = Misc.getSymbolEntry(((FreeVariable) c).getFreeVariableName(), ((FreeVariable) c).getNodeToken());
		if (sym != null) {
			CellCollection.removeCellFromDerivedCollections(c);
			((LinkedList<Cell>) this.internalRepresentation).set(index, sym);
			changed = true;
		}
		return changed;
	}

	private void testAndConvert(int index) {
		if (!this.hasFreeVariables()) {
			return;
		}
		Cell c = ((LinkedList<Cell>) this.internalRepresentation).get(index);
		if (c == Cell.genericCell) {
			return;
		}
		if (!(c instanceof FreeVariable)) {
			return;
		}
		Symbol sym = Misc.getSymbolEntry(((FreeVariable) c).getFreeVariableName(), ((FreeVariable) c).getNodeToken());
		if (sym != null) {
			CellCollection.removeCellFromDerivedCollections(c);
			((LinkedList<Cell>) this.internalRepresentation).set(index, sym);
		}
	}

	@Override
	public boolean hasDeletedSymbols() {
		if (Cell.deletedCells.isEmpty()) {
			return false;
		} else {
			if (Misc.doIntersect(Cell.deletedCells, new HashSet<>(this.internalRepresentation))) {
				return true;
			} else {
				return false;
			}
		}
	}
}
