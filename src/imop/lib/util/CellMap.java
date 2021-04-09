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
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class CellMap<V> extends AbstractMap<Cell, V> {
	protected Map<Cell, V> internalRepresentation;
	public int freeVariableCount = 0;
	protected boolean containsUniversal = false;

	public boolean hasFreeVariables() {
		return this.freeVariableCount > 0;
	}

	public CellMap() {
		this.internalRepresentation = new HashMap<>();
		this.freeVariableCount = 0;
	}

	public CellMap(CellMap<V> other) {
		this.internalRepresentation = new HashMap<>(other.internalRepresentation);
		this.freeVariableCount = other.freeVariableCount;
		if (other.containsUniversal) {
			this.containsUniversal = true;
		}
	}

	public boolean isUniversal() {
		return this.containsUniversal;
	}

	@Override
	public int size() {
		if (this.isUniversal()) {
			return Cell.allCells.size();
		} else {
			return this.internalRepresentation.size();
		}
	}

	@Override
	public Set<Cell> keySet() {
		return this.keySetExpanded();
	}

	public Set<Cell> keySetExpanded() {
		if (this.isUniversal()) {
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature("Returning all cells due to expansion of keys.", Program.getRoot());
			return new HashSet<>(Cell.allCells);
		} else {
			this.testAndConvert();
			return new HashSet<>(this.internalRepresentation.keySet());
		}
	}

	public Set<Cell> nonGenericKeySet() {
		if (this.isUniversal()) {
			Set<Cell> retSet = new HashSet<>();
			for (Object o : this.internalRepresentation.keySet().stream().filter((key) -> (key != Cell.genericCell))
					.toArray()) {
				o = this.testAndConvert((Cell) o);
				retSet.add((Cell) o);
			}
			return retSet;
		} else {
			this.testAndConvert();
			return new HashSet<>(this.internalRepresentation.keySet());
		}
	}

	/**
	 * Returns the value that is mapped to {@code key}. If no such mapping
	 * exists, then the value mapped to {@link getGenericCell()}, if any,
	 * is returned, else {@code null} is returned.
	 * 
	 * @param key
	 *            key for which corresponding value is required.
	 * @return
	 *         the value that is mapped to {@code key}. If no such mapping
	 *         exists, then the value mapped to {@link getGenericCell()},
	 *         if any,
	 *         is returned, else {@code null} is returned.
	 * 
	 */
	public V get(Cell key) {
		if (key instanceof FreeVariable) {
			key = this.testAndConvert(key);
		}
		if (key == Cell.genericCell) {
			if (this.isUniversal()) {
				return this.internalRepresentation.get(key);
			} else {
				return null;
			}
		} else {
			if (this.isUniversal()) {
				if (this.internalRepresentation.containsKey(key)) {
					return this.internalRepresentation.get(key);
				} else {
					return this.internalRepresentation.get(Cell.genericCell);
				}
			} else {
				return this.internalRepresentation.get(key);
			}
		}
	}

	public boolean containsKey(Cell key) {
		if (key instanceof FreeVariable) {
			key = testAndConvert(key);
		}
		if (key == Cell.genericCell) {
			if (this.isUniversal()) {
				return true;
			} else {
				return false;
			}
		} else {
			if (this.isUniversal()) {
				return true;
			} else {
				return this.internalRepresentation.containsKey(key);
			}
		}
	}

	/**
	 * If a mapping already exists for {@code key}, it is changed to
	 * {@code value}. Otherwise, a new mapping is added for ({@code key},
	 * {@code value}). However, if {@code key} is {@link getGenericCell()},
	 * then the internal map is cleared and the new mapping is added to the map.
	 * 
	 * @param key
	 *              key for the mapping to be added.
	 * @param value
	 *              value for the mapping to be added.
	 * @return
	 *         the value that is mapped to {@code key}. If no such mapping
	 *         exists, then the value mapped to {@link getGenericCell()},
	 *         if any, is returned, else {@code null} is returned.
	 * 
	 */
	@Override
	public V put(Cell key, V value) {
		if (key instanceof FreeVariable) {
			key = this.testAndConvert(key);
		}
		V retVal = this.get(key);
		if (key == Cell.genericCell) {
			this.containsUniversal = true;
			if (this.isUniversal()) {
				this.internalRepresentation.clear();
				this.internalRepresentation.put(key, value);
				this.freeVariableCount = Integer.MAX_VALUE;
			} else {
				this.internalRepresentation.clear();
				this.internalRepresentation.put(key, value);
				this.freeVariableCount = Integer.MAX_VALUE;
			}
		} else {
			if (this.isUniversal()) {
				this.internalRepresentation.put(key, value);
			} else {
				this.internalRepresentation.put(key, value);
				if (key instanceof FreeVariable) {
					freeVariableCount++;
				}
			}
		}
		return retVal;
	}

	/**
	 * If a mapping already exists for the generic cell, then the map is updated
	 * and old value is returned. Otherwise, an entry is added, without removing
	 * existing explicit cell mappings.
	 * 
	 * @param value
	 *              value which a generic cell should be mapped to.
	 * @return
	 *         the old value that was mapped to the generic cell, else
	 *         {@code null}.
	 */
	public V updateGenericMap(V value) {
		if (this.isUniversal()) {
			this.containsUniversal = true;
			V oldVal = this.get(Cell.genericCell);
			// DO NOT CHANGE THIS TO PUT().
			this.internalRepresentation.put(Cell.genericCell, value);
			return oldVal;
		} else {
			this.internalRepresentation.put(Cell.genericCell, value);
			return null;
		}
	}

	public V remove(Cell key) {
		if (key instanceof FreeVariable) {
			key = this.testAndConvert(key);
		}
		V retVal = this.get(key);
		if (key == Cell.genericCell) {
			this.containsUniversal = false;
			if (this.isUniversal()) {
				this.internalRepresentation.clear();
				this.freeVariableCount = 0;
			} else {
				this.internalRepresentation.clear();
				this.freeVariableCount = 0;
			}
		} else {
			if (this.isUniversal()) {
				this.containsUniversal = false;
				V globalVal = this.internalRepresentation.remove(Cell.genericCell);
				this.freeVariableCount = this.nonGenericKeySet().stream().filter((x) -> {
					return x instanceof FreeVariable;
				}).toArray().length;
				Set<Cell> existingKeys = this.internalRepresentation.keySet();
				for (Cell otherCell : Cell.allCells) {
					if (!(otherCell.equals(key) || existingKeys.contains(otherCell))) {
						if (!this.internalRepresentation.containsKey(otherCell)) {
							if (otherCell instanceof FreeVariable) {
								this.freeVariableCount++;
							}
						}
						this.internalRepresentation.put(otherCell, globalVal);
					}
				}
				key = this.testAndConvert(key);
				V old = this.internalRepresentation.remove(key);
				if (old != null) {
					if (key instanceof FreeVariable) {
						freeVariableCount--;
					}
				}
			} else {
				key = this.testAndConvert(key);
				V old = this.internalRepresentation.remove(key);
				if (old != null) {
					if (key instanceof FreeVariable) {
						freeVariableCount--;
					}
				}
			}
		}
		return retVal;
	}

	@Override
	public boolean containsValue(Object value) {
		return this.internalRepresentation.containsValue(value);
	}

	public void forEachExpanded(BiConsumer<Cell, V> action) {
		if (this.isUniversal()) {
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature("Applying an action on all cells due to expansion of keys.", Program.getRoot());
			this.testAndConvert();
			V globalVal = this.internalRepresentation.get(Cell.genericCell);
			Set<Cell> existingKeys = this.internalRepresentation.keySet();
			for (Cell otherCell : Cell.allCells) {
				if (!existingKeys.contains(otherCell)) {
					action.accept(otherCell, globalVal);
				}
			}
			for (Cell existingKey : existingKeys) {
				action.accept(existingKey, this.internalRepresentation.get(existingKey));
			}
		} else {
			this.testAndConvert();
			this.internalRepresentation.forEach(action);
		}
	}

	public void replaceAllExpanded(BiFunction<Cell, V, V> function) {
		if (this.isUniversal()) {
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature("Replacing values for all cells due to expansion of keys.", Program.getRoot());
			this.testAndConvert();
			V globalVal = this.internalRepresentation.get(Cell.genericCell);
			Set<Cell> existingKeys = this.internalRepresentation.keySet();
			for (Cell otherCell : Cell.allCells) {
				if (!existingKeys.contains(otherCell)) {
					this.internalRepresentation.put(otherCell, function.apply(otherCell, globalVal));
				}
			}
			for (Cell existingKey : existingKeys) {
				this.internalRepresentation.put(existingKey,
						function.apply(existingKey, this.internalRepresentation.get(existingKey)));
			}
			this.remove(Cell.genericCell);
			this.containsUniversal = false;
		} else {
			this.testAndConvert();
			this.internalRepresentation.replaceAll(function);
		}
	}

	@Override
	public boolean isEmpty() {
		return this.internalRepresentation.isEmpty();
	}

	@Override
	public void clear() {
		this.internalRepresentation.clear();
		this.freeVariableCount = 0;
		this.containsUniversal = false;
	}

	@Override
	public String toString() {
		String tempStr = "[";
		for (Cell c : this.internalRepresentation.keySet()) {
			tempStr += "\n\t" + c + ":" + this.internalRepresentation.get(c);
		}
		tempStr += "]";
		return tempStr;
	}

	@Override
	public Object clone() {
		CellMap<V> newMap = new CellMap<>(this);
		return newMap;
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		CellMap<V> other = (CellMap<V>) obj;
		if (internalRepresentation == null) {
			if (other.internalRepresentation != null) {
				return false;
			}
		} else if (!internalRepresentation.equals(other.internalRepresentation)) {
			return false;
		}
		return true;
	}

	protected void testAndConvert() {
		if (!this.hasFreeVariables()) {
			return;
		}

		Set<FreeVariable> freeCells = new HashSet<>();
		for (Cell cell : this.internalRepresentation.keySet()) {
			if (cell instanceof FreeVariable) {
				freeCells.add((FreeVariable) cell);
			}
		}
		for (FreeVariable f : freeCells) {
			Symbol sym = Misc.getSymbolEntry(f.getFreeVariableName(), f.getNodeToken());
			if (sym != null) {
				// CellCollection.removeCellFromDerivedCollections(f);
				this.put(sym, this.internalRepresentation.get(f));
				this.remove(f);
			}
		}
	}

	protected Cell testAndConvert(Cell o) {
		if (!(o instanceof FreeVariable)) {
			return o;
		}
		if (!this.internalRepresentation.containsKey(o)) {
			return o;
		}
		Symbol sym = Misc.getSymbolEntry(((FreeVariable) o).getFreeVariableName(), ((FreeVariable) o).getNodeToken());
		if (sym != null) {
			// CellCollection.removeCellFromDerivedCollections(o);
			V value = this.internalRepresentation.get(o);
			this.internalRepresentation.remove(o);
			freeVariableCount--;
			this.put(sym, value);
			return sym;
		}
		return o;
	}

	/**
	 * Merges this object with {@code thatMap}, for the cells specified in
	 * {@code selectedCells} (all, if {@code null}), as per the merge operation
	 * provided by {@code mergeMethod}.
	 * 
	 * @param thatMap
	 *                      map, of same value type, which needs to be merged into
	 *                      this
	 *                      map.
	 * @param mergeMethod
	 *                      a binary operator, that takes two arguments and returns
	 *                      a
	 *                      value of the same type. This lambda is used to specify
	 *                      the
	 *                      merge operation given two elements from the co-domain of
	 *                      this
	 *                      map. Note that this method should take care of
	 *                      {@code null}
	 *                      values as well (for the first argument).
	 * @param selectedCells
	 *                      set of cells for which merge has to be performed; value
	 *                      {@code null} represents all cells.
	 * @return
	 *         true, if this method changed the state (except for the internal
	 *         free variable to symbol conversions) of the receiver object.
	 */
	public boolean mergeWith(CellMap<V> thatMap, BinaryOperator<V> mergeMethod, CellSet selectedCells) {
		boolean changed = false;
		if (thatMap == null) {
			return changed;
		}
		CellMap<V> thisMap = this;
		Set<Cell> thatNonGenericSet = thatMap.nonGenericKeySet();
		// Handle all explicit cells of thatMap.
		for (Cell thatCell : thatNonGenericSet) {
			if (selectedCells != null && !selectedCells.contains(thatCell)) {
				continue;
			}
			V thatValue = thatMap.get(thatCell);
			V thisValue = thisMap.get(thatCell);
			// Note that thisValue may be null. We assume that mergeMethod takes care of
			// that.
			V newValue = mergeMethod.apply(thisValue, thatValue);
			if (!newValue.equals(thisValue)) {
				changed = true;
				thisMap.put(thatCell, newValue);
			}
		}
		if (thatMap.isUniversal()) {
			// Handle all implicit cells of thatMap.
			this.containsUniversal = true;
			V thatGenericValue = thatMap.get(Cell.genericCell);
			Set<Cell> thisNonGenericSet = thisMap.nonGenericKeySet();
			// Handle those explicit cells of this map which are not yet taken care of.
			thisNonGenericSet.removeAll(thatNonGenericSet);
			for (Cell thisCell : thisNonGenericSet) {
				if (selectedCells != null && !selectedCells.contains(thisCell)) {
					continue;
				}
				V thisValue = thisMap.get(thisCell);
				V newValue = mergeMethod.apply(thisValue, thatGenericValue);
				if (!newValue.equals(thisValue)) {
					changed = true;
					thisMap.put(thisCell, newValue);
				}
			}

			if (selectedCells != null) {
				// Handle other cells of thisMap.
				for (Cell otherCell : selectedCells) {
					if (thatNonGenericSet.contains(otherCell)) {
						continue;
					}
					if (thisNonGenericSet.contains(otherCell)) {
						continue;
					}
					V newValue = mergeMethod.apply(null, thatGenericValue);
					changed = true;
					thisMap.put(otherCell, newValue);
				}
			} else {
				// Handle implicit cells of thisMap.
				V thisGenericValue = thisMap.get(Cell.genericCell);
				// Note that thisGenericValye may be null. We assume that mergeMethod takes care
				// of this issue.
				V newGenericValue = mergeMethod.apply(thisGenericValue, thatGenericValue);
				if (!newGenericValue.equals(thisGenericValue)) {
					changed = true;
					thisMap.updateGenericMap(newGenericValue);
				}
			}
		}
		return changed;
	}

	public boolean hasDeletedSymbols() {
		if (Cell.deletedCells.isEmpty()) {
			return false;
		} else {
			if (Misc.doIntersect(Cell.deletedCells, new HashSet<>(this.internalRepresentation.keySet()))) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public Set<Entry<Cell, V>> entrySet() {
		this.testAndConvert();
		return this.internalRepresentation.entrySet();
	}
}
