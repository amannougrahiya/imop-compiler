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

import java.util.*;
import java.util.function.Consumer;

public abstract class CellCollection extends AbstractCollection<Cell> implements Iterable<Cell> {
	protected Collection<Cell> internalRepresentation;
	public boolean isDerivedFromUniversal;
	protected boolean containsUniversal = false;
	public int freeVariableCount;
	public static Set<CellCollection> allDerivedCollections = Collections
			.newSetFromMap(new WeakHashMap<CellCollection, Boolean>());

	public static void addCollectionToDerived(CellCollection c) {
		if (!c.isDerivedFromUniversal) {
			c.isDerivedFromUniversal = true;
			CellCollection.allDerivedCollections.add(c);
		}
	}

	public static void removeCollectionFromDerived(CellCollection c) {
		if (c.isDerivedFromUniversal) {
			c.isDerivedFromUniversal = false;
			CellCollection.allDerivedCollections.remove(c);
		}
	}

	public static void removeCellFromDerivedCollections(Cell cell) {
		Set<Cell> removeSet = new HashSet<>();
		removeSet.add(cell);
		if (cell instanceof Symbol) {
			Symbol sym = (Symbol) cell;
			if (sym.hasAddressCell()) {
				removeSet.add(sym.getAddressCell());
			}
			if (sym.hasFieldCell()) {
				removeSet.add(sym.getFieldCell());
			}
		}
		for (CellCollection c : CellCollection.allDerivedCollections) {
			if (c.isUniversal()) {
				continue;
			}
			c.internalRepresentation.removeAll(removeSet);
		}
		getAllCells().removeAll(removeSet);
	}

	public Collection<Cell> getReadOnlyInternal() {
		return Collections.unmodifiableCollection(this.internalRepresentation);
	}

	public CellCollection(boolean isDerivedFromUniversal) {
		this.isDerivedFromUniversal = isDerivedFromUniversal;
		this.freeVariableCount = 0;
		if (isDerivedFromUniversal) {
			allDerivedCollections.add(this);
		}
	}

	public boolean isUniversal() {
		return this.containsUniversal;
		// return this.internalRepresentation.contains(getGenericCell());
	}

	@Override
	public Iterator<Cell> iterator() {
		if (this.isUniversal()) {
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature("Iteration over a set with generic element.", Program.getRoot());
		}
		if (this.hasFreeVariables()) {
			testAndConvert();
		}
		return internalRepresentation.iterator();
	}

	@Override
	public int size() {
		if (this.isUniversal()) {
			return getAllCells().size();
		} else {
			return internalRepresentation.size();
		}
	}

	@Override
	public boolean isEmpty() {
		if (this.isUniversal()) {
			return false;
		} else {
			return internalRepresentation.isEmpty();
		}
	}

	public boolean contains(Cell s) {
		if (s == Cell.genericCell) {
			if (this.isUniversal()) {
				return true;
			} else {
				return false;
			}
		} else {
			if (this.isUniversal()) {
				return true;
			} else {
				if (this.hasFreeVariables()) {
					testAndConvert();
				}
				return internalRepresentation.contains(s);
			}
		}
	}

	@Override
	public void clear() {
		if (this.isDerivedFromUniversal) {
			CellCollection.allDerivedCollections.remove(this);
			this.isDerivedFromUniversal = false;
		}
		this.freeVariableCount = 0;
		this.containsUniversal = false;
		internalRepresentation.clear();
	}

	public boolean addAll(CellSet c) {
		if (c.isUniversal()) {
			if (this.isUniversal()) {
				return false;
			} else {
				internalRepresentation.clear();
				internalRepresentation.add(Cell.genericCell);
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
				boolean changed = false;
				for (Cell s : c) {
					changed |= this.add(s);
				}
				return changed;
			}
		}
	}

	public boolean addAll(CellList c) {
		if (c.isUniversal()) {
			if (this.isUniversal()) {
				return false;
			} else {
				internalRepresentation.clear();
				internalRepresentation.add(Cell.genericCell);
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
				boolean changed = false;
				for (Cell s : c) {
					changed |= this.add(s);
				}
				return changed;
			}
		}
	}

	@Override
	public boolean retainAll(Collection<?> coll) {
		if (!(coll instanceof CellSet)) {
			return false;
		}
		CellSet c = (CellSet) coll;
		if (c.isUniversal()) {
			if (this.isUniversal()) {
				return false;
			} else {
				return false;
			}
		} else {
			if (this.isUniversal()) {
				internalRepresentation = new HashSet<>(c.internalRepresentation);
				this.freeVariableCount = c.freeVariableCount;
				if (!c.isDerivedFromUniversal) {
					CellCollection.removeCollectionFromDerived(this);
				}
				this.containsUniversal = false;
				return true;
			} else {
				this.testAndConvert();
				c.testAndConvert();
				return internalRepresentation.retainAll(c.internalRepresentation);
			}
		}
	}

	public void applyAllExpanded(Consumer<Cell> c) {
		if (this.isUniversal()) {
			// Thread.dumpStack();
			Misc.warnDueToLackOfFeature(
					"Not efficient in handling a generic cell while applying a function on all elements.",
					Program.getRoot());
			for (Cell s : getAllCells()) {
				c.accept(s);
			}
		} else {
			if (this.hasFreeVariables()) {
				testAndConvert();
			}
			for (Cell s : internalRepresentation) {
				c.accept(s);
			}
		}
	}

	@Override
	public abstract boolean add(Cell s);

	public boolean hasFreeVariables() {
		return this.freeVariableCount > 0;
	}

	public abstract boolean hasDeletedSymbols();

	/**
	 * Tests each element and convert it from {@link FreeVariable} to
	 * {@link Symbol}, if possible.
	 * 
	 * @return
	 *         true, if any element was converted from {@link FreeVariable} to
	 *         {@link Symbol}.
	 */
	public abstract void testAndConvert();

	public static Set<Cell> getAllCells() {
		return Cell.allCells;
	}

	@Override
	public String toString() {
		return internalRepresentation.toString();
	}

}
