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

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * A variant of CellMap, with notion of keysNotPresent and fallBackMaps.
 * IMPORTANT: NEEDS TO BE REVIEWED.
 * 
 * @author Aman Nougrahiya
 *
 * @param <V>
 */
public class ExtensibleCellMap<V extends Immutable> extends CellMap<V> {
	protected CellSet keysNotPresent; // Set of keys which are assumed to be not present; shouldn't accept a universal
										// element.
	protected ExtensibleCellMap<V> fallBackMap;// Backup map which this map extends; used when keys are not found in
												// this map.
	private List<ExtensibleCellMap<V>> extensionMaps = new ArrayList<>(); // Those maps which extend upon this map.
	private final int maxLinkLength;
	private static int LINKLENGTH = 2;

	private static int counterLinkLength = 0;

	public ExtensibleCellMap() {
		this.internalRepresentation = new HashMap<>();
		this.keysNotPresent = null;
		this.fallBackMap = null;
		this.freeVariableCount = 0;
		this.containsUniversal = false;
		this.extensionMaps = new ArrayList<>();
		this.maxLinkLength = LINKLENGTH;
	}

	/**
	 * 
	 * @param fallBackMap
	 *                      map, which this map would extend.
	 * @param maxLinkLength
	 *                      maximum permissible number of elements in any extension
	 *                      chain.
	 */
	public ExtensibleCellMap(ExtensibleCellMap<V> fallBackMap, int maxLinkLength) {
		this.maxLinkLength = maxLinkLength;
		this.commonConstruction(fallBackMap);
	}

	public ExtensibleCellMap(ExtensibleCellMap<V> fallBackMap) {
		this.maxLinkLength = LINKLENGTH;
		this.commonConstruction(fallBackMap);
	}

	private void commonConstruction(ExtensibleCellMap<V> fallBackMap) {
		int linkLength = ExtensibleCellMap.getLinkLength(fallBackMap);
		if (linkLength > counterLinkLength) {
			counterLinkLength = linkLength;
			System.err.println("New highest value of link length for ExtensibleCellMaps: " + counterLinkLength);
		}
		if (linkLength < this.maxLinkLength) {
			// Link
			this.internalRepresentation = new HashMap<>();
			this.keysNotPresent = null;
			ExtensibleCellMap.setFallBackMap(this, fallBackMap);
			this.freeVariableCount = 0;
			this.containsUniversal = false;
			this.extensionMaps = new ArrayList<>();
		} else {
			// Copy
			this.internalRepresentation = new HashMap<>(fallBackMap.internalRepresentation);
			if (fallBackMap.keysNotPresent != null) {
				this.keysNotPresent = new CellSet(fallBackMap.keysNotPresent);
			}
			ExtensibleCellMap.setFallBackMap(this, fallBackMap.fallBackMap);
			this.freeVariableCount = fallBackMap.freeVariableCount;
			this.containsUniversal = fallBackMap.containsUniversal;
			this.extensionMaps = new ArrayList<>();
		}
	}

	/**
	 * Sets the map {@code fallBackMap} as a fall back map for
	 * {@code extendedMap}, and adds {@code extendedMap} to the set
	 * {@code extensionMaps} of {@code fallBackMap}.
	 * 
	 * @param <H>
	 * @param extendedMap
	 * @param fallBackMap
	 */
	private static <H extends Immutable> void setFallBackMap(ExtensibleCellMap<H> extendedMap,
			ExtensibleCellMap<H> fallBackMap) {
		if (extendedMap != null) {
			extendedMap.fallBackMap = fallBackMap;
			if (fallBackMap != null) {
				/*
				 * OLD CODE: The implicit equality checks were very costly.
				 * Furthermore, here we wish to treat two structurally different
				 * maps as different, regardless of whether they are logically
				 * same.
				 * // fallBackMap.extensionMaps.add(extendedMap);
				 */

				/*
				 * NEW CODE: Here, we simply check if the list extensionMaps
				 * (initially it was
				 * a set), contains the extendedMap object already.
				 */
				boolean foundSameObject = false;
				for (ExtensibleCellMap<?> tempMap : fallBackMap.extensionMaps) {
					if (tempMap == extendedMap) {
						foundSameObject = true;
					}
				}
				if (!foundSameObject) {
					fallBackMap.extensionMaps.add(extendedMap);
				}
			}
		}
	}

	/**
	 * Provides the number of elements in the extension chain of this map
	 * (including this map).
	 * 
	 * @param map
	 * @return
	 */
	public static int getLinkLength(ExtensibleCellMap<?> map) {
		int count = 0;
		ExtensibleCellMap<?> temp = map;
		while (temp != null) {
			count++;
			temp = temp.fallBackMap;
		}
		return count;
	}

	@Override
	public boolean isUniversal() {
		if (this.containsUniversal) {
			return true;
		}
		if (this.fallBackMap == null) {
			return false;
		} else {
			return this.fallBackMap.isUniversal();
		}
	}

	@Override
	public boolean hasFreeVariables() {
		if (this.freeVariableCount > 0) {
			return true;
		}
		if (this.fallBackMap == null) {
			return false;
		} else {
			return fallBackMap.hasFreeVariables();
		}
	}

	@Override
	public boolean hasDeletedSymbols() {
		if (Cell.deletedCells.isEmpty()) {
			return false;
		} else {
			if (Misc.doIntersect(Cell.deletedCells, this.nonGenericKeySet(ConvertMode.OFF))) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Checks if the provided {@code key}'s value will be read from the
	 * {@code fallBackMap}, for the receiver map.
	 * 
	 * @param key
	 *            a key to be tested.
	 * @return
	 *         {@code true}, if the {@code fallBackMap} would be referenced for
	 *         getting the value corresponding to the given {@code key}.
	 */
	private boolean readsKeyFromTheFallBackMap(Cell key) {
		if (this.fallBackMap == null) {
			return false;
		}
		if (this.keysNotPresent != null && this.keysNotPresent.contains(key)) {
			return false;
		}
		if (this.internalRepresentation.containsKey(key) || this.internalRepresentation.containsKey(Cell.genericCell)) {
			return false;
		}
		return true;
	}

	@Override
	public int size() {
		if (this.isUniversal()) {
			int notPresent = (this.keysNotPresent == null) ? 0 : this.keysNotPresent.size();
			return Cell.allCells.size() - notPresent;
		}
		if (this.fallBackMap == null) {
			if (this.keysNotPresent == null) {
				return this.internalRepresentation.size();
			} else {
				return (int) this.internalRepresentation.keySet().parallelStream()
						.filter(c -> !keysNotPresent.contains(c)).count();
			}
		} else {
			int size = 0;
			for (@SuppressWarnings("unused")
			Cell key : this.keySetExpanded(ConvertMode.OFF)) {
				size++;
			}
			return size;
		}
	}

	@Override
	public boolean isEmpty() {
		if (this.internalRepresentation.isEmpty()) {
			if (this.fallBackMap == null) {
				return true;
			} else {
				if (this.keysNotPresent == null || this.keysNotPresent.isEmpty()) {
					return this.fallBackMap.isEmpty();
				}
			}
		}
		return this.size() == 0;
	}

	@Override
	public boolean containsKey(Cell key) {
		this.testAndConvert();
		if (key instanceof FreeVariable) {
			key = testAndConvert(key);
		}
		return this.containsKey(key, ConvertMode.ON);
	}

	protected boolean containsKey(Cell key, ConvertMode convertMode) {
		if (keysNotPresent != null && keysNotPresent.contains(key)) {
			return false;
		}
		if (key == Cell.genericCell) {
			if (this.isUniversal()) {
				return true;
			} else {
				return false;
			}
		}
		if (this.internalRepresentation.containsKey(key) || this.internalRepresentation.containsKey(Cell.genericCell)) {
			return true;
		}
		if (this.fallBackMap == null) {
			return false;
		} else {
			return this.fallBackMap.containsKey(key, convertMode);
		}
	}

	@Override
	public boolean containsValue(Object value) {
		for (Cell key : this.keySetExpanded(ConvertMode.OFF)) {
			Object keyVal = this.get(key, ConvertMode.OFF);
			if (keyVal == value || keyVal.equals(value)) {
				return true;
			}
		}
		return false;
	}

	private static enum ConvertMode {
		ON, OFF
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
	@Override
	public V get(Cell key) {
		this.testAndConvert();
		if (key instanceof FreeVariable) {
			key = this.testAndConvert(key);
		}
		return this.get(key, ConvertMode.ON);
	}

	protected V get(Cell key, ConvertMode convertMode) {
		if (key == Cell.genericCell) {
			if (keysNotPresent != null && !keysNotPresent.isEmpty()) {
				return null;
			}
			if (this.internalRepresentation.containsKey(key)) {
				if (this.internalRepresentation.size() != 1) {
					return null;
				}
				return this.internalRepresentation.get(key);
			}
		} else {
			if (keysNotPresent != null && keysNotPresent.contains(key)) {
				return null;
			}
			if (this.internalRepresentation.containsKey(key)) {
				return this.internalRepresentation.get(key);
			}
			if (this.internalRepresentation.containsKey(Cell.genericCell)) {
				return this.internalRepresentation.get(Cell.genericCell);
			}
		}
		if (fallBackMap == null) {
			return null;
		} else {
			return this.fallBackMap.get(key, convertMode);
		}
	}

	/**
	 * If a mapping already exists for {@code key}, it is changed to
	 * {@code value}. Otherwise, a new mapping is added for ({@code key},
	 * {@code value}). However, if {@code key} is {@link getGenericCell()},
	 * then the internal map is cleared and the new mapping is added to the map.
	 * 
	 * @param key
	 *                 key for the mapping to be added.
	 * @param newValue
	 *                 value for the mapping to be added.
	 * @return
	 *         the value that is mapped to {@code key}. If no such mapping
	 *         exists, then the value mapped to {@link getGenericCell()},
	 *         if any, is returned, else {@code null} is returned.
	 * 
	 */
	@Override
	public V put(Cell key, V newValue) {
		assert (newValue != null);
		if (key instanceof FreeVariable) {
			key = this.testAndConvert(key);
		}
		return this.put(key, newValue, ConvertMode.ON);
	}

	protected V put(Cell key, V newValue, ConvertMode convertMode) {
		assert (newValue != null);
		V oldVal = this.get(key, convertMode);
		if (oldVal != null && (oldVal == newValue || oldVal.equals(newValue))) {
			return oldVal;
		}

		if (key == Cell.genericCell) {
			if (keysNotPresent != null) {
				for (Cell tempKey : new CellSet(keysNotPresent)) {
					this.removeKeyFromKeysNotPresent(tempKey);
				}
			}
			for (ExtensibleCellMap<V> ext : new HashSet<>(this.extensionMaps)) {
				for (Cell tempKey : new HashSet<>(this.internalRepresentation.keySet())) {
					// This should work even when tempKey is a Universal element.
					if (ext.readsKeyFromTheFallBackMap(tempKey)) {
						ext.internalRepresentation.put(tempKey, this.internalRepresentation.get(tempKey));
						if (tempKey == Cell.genericCell) {
							ext.containsUniversal = true;
						}
					}
				}
				if (this.fallBackMap != null) {
					ExtensibleCellMap.setFallBackMap(ext, this.fallBackMap);
				} else {
					ext.fallBackMap = null;
				}
				this.extensionMaps.remove(ext);
			}

			this.internalRepresentation.clear();
			this.internalRepresentation.put(key, newValue);
			this.containsUniversal = true;
			this.freeVariableCount = 0;
		} else {
			this.removeKeyFromKeysNotPresent(key);
			for (ExtensibleCellMap<V> ext : this.extensionMaps) {
				if (ext.readsKeyFromTheFallBackMap(key)) {
					ext.internalRepresentation.put(key, oldVal);
				}
			}
			this.internalRepresentation.put(key, newValue);
			if (key instanceof FreeVariable) {
				this.freeVariableCount++;
			}
		}
		return oldVal;
	}

	private void removeKeyFromKeysNotPresent(Cell key) {
		if (this.keysNotPresent == null) {
			return;
		}
		if (!this.keysNotPresent.contains(key)) {
			return;
		}
		for (ExtensibleCellMap<V> ext : extensionMaps) {
			if (ext.readsKeyFromTheFallBackMap(key)) {
				if (ext.keysNotPresent == null) {
					ext.keysNotPresent = new CellSet();
				}
				ext.keysNotPresent.add(key);
			}
		}
		keysNotPresent.remove(key);
	}

	@Override
	public V remove(Cell key) {
		if (key instanceof FreeVariable) {
			key = this.testAndConvert(key);
		}
		return this.remove(key, ConvertMode.ON);
	}

	protected V remove(Cell key, ConvertMode convertMode) {
		if (!this.containsKey(key, convertMode)) {
			return null;
		}
		V retVal = this.get(key, convertMode);

		if (key != Cell.genericCell) {
			this.addKeyToKeysNotPresent(key, convertMode);
		} else {
			if (this.keysNotPresent != null) {
				CellSet kNPCopy = new CellSet(keysNotPresent);
				for (Cell kNP : kNPCopy) {
					this.removeKeyFromKeysNotPresent(kNP);
					this.internalRepresentation.remove(kNP);
				}
				assert (this.keysNotPresent.isEmpty());
			}

			for (ExtensibleCellMap<V> ext : this.extensionMaps) {
				if (ext.internalRepresentation.containsKey(Cell.genericCell)) {
					continue;
				}
				for (Cell nonGenericKey : this.nonGenericKeySet()) {
					V value = this.get(nonGenericKey, convertMode);
					if (ext.readsKeyFromTheFallBackMap(nonGenericKey)) {
						ext.internalRepresentation.put(nonGenericKey, value);
					}
				}
				ext.internalRepresentation.put(Cell.genericCell, retVal);
				ext.containsUniversal = true;
				/*
				 * We won't have to set the fallBackMap of the extension map, as
				 * it already contains a universal element now.
				 */
			}

			this.internalRepresentation.clear();
			this.freeVariableCount = 0;
			this.containsUniversal = false;
			this.fallBackMap.extensionMaps.remove(this);
			this.fallBackMap = null;
		}
		return retVal;
		// OLD CODE BELOW:
		// if (this.isUniversal()) {
		// this.containsUniversal = false;
		// V globalVal = this.internalRepresentation.remove(Cell.getGenericCell());
		// this.freeVariableCount =
		// this.nonGenericKeySet(convertMode).stream().filter((x) -> {
		// return x instanceof FreeVariable;
		// }).toArray().length;
		// Set<Cell> existingKeys = this.internalRepresentation.keySet();
		// for (Cell otherCell : Cell.allCells) {
		// if (!(otherCell.equals(key) || existingKeys.contains(otherCell))) {
		// if (!this.internalRepresentation.containsKey(otherCell)) {
		// if (otherCell instanceof FreeVariable) {
		// this.freeVariableCount++;
		// }
		// }
		// this.internalRepresentation.put(otherCell, globalVal);
		// }
		// }
		// if (convertMode == ConvertMode.ON) {
		// key = this.testAndConvert(key);
		// }
		// V old = this.internalRepresentation.remove(key);
		// if (old != null) {
		// if (key instanceof FreeVariable) {
		// freeVariableCount--;
		// }
		// }
		// } else {
		// if (convertMode == ConvertMode.ON) {
		// key = this.testAndConvert(key);
		// }
		// V old = this.internalRepresentation.remove(key);
		// if (old != null) {
		// if (key instanceof FreeVariable) {
		// freeVariableCount--;
		// }
		// }
		// }
	}

	private void addKeyToKeysNotPresent(Cell key, ConvertMode convertMode) {
		if (!this.containsKey(key, convertMode)) {
			return;
		}
		V value = this.get(key, convertMode);
		for (ExtensibleCellMap<V> ext : extensionMaps) {
			if (ext.readsKeyFromTheFallBackMap(key)) {
				ext.internalRepresentation.put(key, value);
			}
		}
		if (keysNotPresent == null) {
			keysNotPresent = new CellSet();
		}
		this.keysNotPresent.add(key);

	}

	@Override
	public void clear() {
		CellSet tempSet = new CellSet();
		for (Cell key : keySetExpanded(ConvertMode.OFF)) {
			tempSet.add(key);
		}
		for (Cell key : tempSet) {
			this.remove(key, ConvertMode.OFF);
		}
	}

	@Override
	public String toString() {
		String tempStr = "[";
		for (Cell c : this.keySetExpanded()) {
			tempStr += "\n\t" + c + ":" + this.get(c);
		}
		tempStr += "]";
		return tempStr;
	}

	@Override
	public Object clone() {
		ExtensibleCellMap<V> newMap = new ExtensibleCellMap<>(this);
		return newMap;
	}

	@Override
	public int hashCode() {
		// final int prime = 31;
		int result = this.size();
		// for (Cell key : this.keySetExpanded()) {
		// result += key.hashCode();
		// }
		// result = prime * result;
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
		ExtensibleCellMap<V> that = (ExtensibleCellMap<V>) obj;

		if (this.keysNotPresent == null && that.keysNotPresent == null) {
			if (this.internalRepresentation.equals(that.internalRepresentation)) {
				if (this.fallBackMap == that.fallBackMap) {
					return true;
				}
			}
		}

		int thisSize = 0;
		for (Cell thisKey : this.keySetExpanded(ConvertMode.OFF)) {
			thisSize++;
			if (!that.containsKey(thisKey, ConvertMode.OFF)) {
				return false;
			}
			V thatValue = that.get(thisKey, ConvertMode.OFF);
			V thisValue = this.get(thisKey, ConvertMode.OFF);
			if (thisValue == thatValue) {
				continue;
			} else if (thisValue == null) {
				// Clearly, thatValue is not null.
				return false;
			} else if (thatValue == null) {
				// Clearly, thisValue is not null.
				return false;
			} else if (!thisValue.equals(thatValue)) {
				return false;
			}
		}
		int thatSize = 0;
		for (@SuppressWarnings("unused")
		Cell thatKey : that.keySetExpanded(ConvertMode.OFF)) {
			thatSize++;
		}
		if (thisSize != thatSize) {
			return false;
		}
		return true;
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
	@Override
	public boolean mergeWith(CellMap<V> thatMapExt, BinaryOperator<V> mergeMethod, CellSet selectedCells) {
		boolean changed = false;
		if (thatMapExt == null) {
			return changed;
		}
		if (!(thatMapExt instanceof ExtensibleCellMap<?>)) {
			Misc.warnDueToLackOfFeature(
					"The behaviour upon merging a non-ExtensibleCellMap into an ExtensibleCellMap has not been tested yet.",
					null);
			return super.mergeWith(thatMapExt, mergeMethod, selectedCells);
		}
		ExtensibleCellMap<V> thatMap = (ExtensibleCellMap<V>) thatMapExt;
		ExtensibleCellMap<V> thisMap = this;
		Set<Cell> thatNonGenericSet = thatMap.nonGenericKeySet(ConvertMode.OFF);
		// Handle all explicit cells of thatMap.
		for (Cell thatCell : thatNonGenericSet) {
			if (selectedCells != null && !selectedCells.contains(thatCell)) {
				continue;
			}
			V thatValue = thatMap.get(thatCell, ConvertMode.OFF);
			V thisValue = thisMap.get(thatCell, ConvertMode.OFF);
			// Note that thisValue may be null. We assume that mergeMethod takes care of
			// that.
			V newValue = mergeMethod.apply(thisValue, thatValue);
			if (newValue != null && !newValue.equals(thisValue)) {
				changed = true;
				thisMap.put(thatCell, newValue, ConvertMode.OFF);
			}
		}
		if (!thatMap.isUniversal()) {
			return changed;
		}
		// Handle all implicit cells of thatMap.
		V thatGenericValue = thatMap.get(Cell.genericCell);
		Set<Cell> thisNonGenericSet = new HashSet<>(thisMap.nonGenericKeySet(ConvertMode.OFF));
		// Handle those explicit cells of this map which are not yet taken care of.
		thisNonGenericSet.removeAll(thatNonGenericSet);
		for (Cell thisCell : thisNonGenericSet) {
			if (selectedCells != null && !selectedCells.contains(thisCell)) {
				continue;
			}
			V thisValue = thisMap.get(thisCell);
			V newValue = mergeMethod.apply(thisValue, thatGenericValue);
			if (newValue != null && !newValue.equals(thisValue)) {
				changed = true;
				thisMap.put(thisCell, newValue);
			}
		}

		Set<Cell> cellsToUpdate = (selectedCells == null) ? Cell.allCells
				: (Set<Cell>) selectedCells.getReadOnlyInternal();
		V thisGenericValue = thisMap.get(Cell.genericCell);
		V newGenericValue;
		if (this.containsUniversal) {
			newGenericValue = mergeMethod.apply(thisGenericValue, thatGenericValue);
		} else {
			newGenericValue = null;
		}
		for (Cell key : cellsToUpdate) {
			if (thatNonGenericSet.contains(key)) {
				continue;
			}
			if (thisNonGenericSet.contains(key)) {
				continue;
			}
			if (this.containsUniversal) {
				changed = true;
				thisMap.put(key, newGenericValue);
			} else {
				changed = true;
				thisMap.put(key, thatGenericValue);
			}
		}

		// OLD CODE: This code below relied upon updateGenericMap(), which was too
		// clumsy.
		// if (selectedCells != null) {
		// // Handle other cells of thisMap.
		// for (Cell otherCell : selectedCells) {
		// if (thatNonGenericSet.contains(otherCell)) {
		// continue;
		// }
		// if (thisNonGenericSet.contains(otherCell)) {
		// continue;
		// }
		// changed = true;
		// thisMap.put(otherCell, thatGenericValue);
		// }
		// } else {
		// // Handle implicit cells of thisMap.
		// V thisGenericValue = thisMap.get(Cell.genericCell);
		// // Note that thisGenericValye may be null. We assume that mergeMethod takes
		// care of this issue.
		// V newGenericValue = mergeMethod.apply(thisGenericValue, thatGenericValue);
		// if (!newGenericValue.equals(thisGenericValue)) {
		// changed = true;
		// thisMap.updateGenericMap(newGenericValue);
		// }
		// }
		this.containsUniversal = true;
		return changed;
	}

	private static enum STATE {
		UNIVERSAL, INTERNAL, FALLBACK
	}

	@Override
	public Set<Cell> keySetExpanded() {
		this.testAndConvert();
		return this.keySetExpanded(ConvertMode.OFF);
	}

	protected Set<Cell> keySetExpanded(ConvertMode convertMode) {
		return this.new KeySetExpanded();
	}

	final class KeySetExpanded extends RestrictedSet<Cell> implements Iterable<Cell> {
		@Override
		public int size() {
			return ExtensibleCellMap.this.size();
		}

		@Override
		public boolean isEmpty() {
			return ExtensibleCellMap.this.isEmpty();
		}

		@Override
		public boolean contains(Object obj) {
			return ExtensibleCellMap.this.containsKey(obj);
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			for (Object obj : c) {
				if (!this.contains(obj)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public Iterator<Cell> iterator() {
			return new KeySetExpandedIterator();
		}

		final class KeySetExpandedIterator implements Iterator<Cell> {
			private STATE traversalState;
			private Iterator<Cell> internalIterator;
			private Iterator<Cell> fallBackMapIterator;
			private Iterator<Cell> universalIterator;
			private Cell nextCell = null;

			public KeySetExpandedIterator() {
				if (internalRepresentation.containsKey(Cell.genericCell)) {
					traversalState = STATE.UNIVERSAL;
					universalIterator = Cell.allCells.iterator();
				} else {
					traversalState = STATE.INTERNAL;
					internalIterator = internalRepresentation.keySet().iterator();
				}
			}

			@Override
			public boolean hasNext() {
				if (nextCell != null) {
					return true;
				}
				switch (traversalState) {
				case UNIVERSAL:
					while (universalIterator.hasNext()) {
						Cell tempRet = universalIterator.next();
						if (keysNotPresent == null || !keysNotPresent.contains(tempRet)) {
							nextCell = tempRet;
							return true;
						}
					}
					// Reaching here would imply that all cells are exhausted.
					return false;
				case INTERNAL:
					while (internalIterator.hasNext()) {
						Cell tempRet = internalIterator.next();
						if (keysNotPresent == null || !keysNotPresent.contains(tempRet)) {
							nextCell = tempRet;
							return true;
						}
					}
					if (fallBackMap == null) {
						return false;
					}
					fallBackMapIterator = fallBackMap.keySetExpanded(ConvertMode.OFF).iterator();
					traversalState = STATE.FALLBACK;
					// Don't break. Fall down to FALLBACK
				case FALLBACK:
					while (fallBackMapIterator.hasNext()) {
						Cell tempRet = fallBackMapIterator.next();
						if ((keysNotPresent == null || !keysNotPresent.contains(tempRet))
								&& !internalRepresentation.keySet().contains(tempRet)) {
							nextCell = tempRet;
							return true;
						}
					}
					return false;
				}
				return false;
			}

			@Override
			public Cell next() {
				this.hasNext();
				if (nextCell != null) {
					Cell tempCell = nextCell;
					nextCell = null;
					return tempCell;
				}
				throw new NoSuchElementException();
			}

		}
	}

	@Override
	public Set<Cell> nonGenericKeySet() {
		this.testAndConvert();
		return this.nonGenericKeySet(ConvertMode.OFF);
	}

	protected Set<Cell> nonGenericKeySet(ConvertMode convertMode) {
		return this.new NonGenericKeySet();
	}

	final class NonGenericKeySet extends RestrictedSet<Cell> implements Iterable<Cell> {
		@Override
		public int size() {
			int count = 0;
			for (@SuppressWarnings("unused")
			Cell key : nonGenericKeySet(ConvertMode.OFF)) {
				count++;
			}
			return count;
		}

		@Override
		public boolean isEmpty() {
			return this.size() == 0;
		}

		@Override
		public boolean contains(Object obj) {
			for (Cell c : ExtensibleCellMap.this.nonGenericKeySet()) {
				if (c == obj) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			for (Object obj : c) {
				if (!this.contains(obj)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public Iterator<Cell> iterator() {
			return new NonGenericKeySetIterator();
		}

		final class NonGenericKeySetIterator implements Iterator<Cell> {
			private STATE traversalState;
			private Iterator<Cell> internalIterator;
			private Iterator<Cell> fallBackMapIterator;
			private Cell nextCell = null;

			public NonGenericKeySetIterator() {
				traversalState = STATE.INTERNAL;
				internalIterator = internalRepresentation.keySet().iterator();
			}

			@SuppressWarnings("incomplete-switch")
			@Override
			public boolean hasNext() {
				if (nextCell != null) {
					return true;
				}
				switch (traversalState) {
				case INTERNAL:
					while (internalIterator.hasNext()) {
						Cell tempRet = internalIterator.next();
						if (tempRet == Cell.genericCell) {
							continue;
						}
						if (keysNotPresent == null || !keysNotPresent.contains(tempRet)) {
							nextCell = tempRet;
							return true;
						}
					}
					if (internalRepresentation.containsKey(Cell.genericCell)) {
						return false;
					}
					if (fallBackMap == null) {
						return false;
					}
					fallBackMapIterator = fallBackMap.nonGenericKeySet(ConvertMode.OFF).iterator();
					traversalState = STATE.FALLBACK;
					// Don't break. Fall down to FALLBACK
				case FALLBACK:
					while (fallBackMapIterator.hasNext()) {
						Cell tempRet = fallBackMapIterator.next();
						if (tempRet == Cell.genericCell) {
							continue;
						}
						if ((keysNotPresent == null || !keysNotPresent.contains(tempRet))
								&& !internalRepresentation.keySet().contains(tempRet)) {
							nextCell = tempRet;
							return true;
						}
					}
					return false;
				}
				return false;
			}

			@Override
			public Cell next() {
				this.hasNext();
				if (nextCell != null) {
					Cell tempCell = nextCell;
					nextCell = null;
					return tempCell;
				}
				throw new NoSuchElementException();
			}

		}
	}

	@Override
	protected void testAndConvert() {
		// if (true) {
		// return;
		// }
		if (this.fallBackMap != null) {
			this.fallBackMap.testAndConvert();
		}

		if (!this.hasFreeVariables()) {
			return;
		}

		Set<FreeVariable> freeCells = new HashSet<>();
		for (Cell cell : this.nonGenericKeySet(ConvertMode.OFF)) {
			if (cell instanceof FreeVariable) {
				freeCells.add((FreeVariable) cell);
			}
		}

		for (FreeVariable f : freeCells) {
			Symbol sym = Misc.getSymbolEntry(f.getFreeVariableName(), f.getNodeToken());
			if (sym != null) {
				// CellCollection.removeCellFromDerivedCollections(f);
				this.put(sym, this.get(f, ConvertMode.OFF), ConvertMode.OFF);
				this.remove(f, ConvertMode.OFF);
				freeVariableCount--;
			}
		}
	}

	@Override
	protected Cell testAndConvert(Cell f) {
		if (this.fallBackMap != null) {
			this.fallBackMap.testAndConvert(f);
		}
		if (!(f instanceof FreeVariable)) {
			return f;
		}
		if (!this.containsKey(f, ConvertMode.OFF)) {
			return f;
		}
		Symbol sym = Misc.getSymbolEntry(((FreeVariable) f).getFreeVariableName(), ((FreeVariable) f).getNodeToken());
		if (sym != null) {
			// CellCollection.removeCellFromDerivedCollections(f);
			this.put(sym, this.get(f, ConvertMode.OFF), ConvertMode.OFF);
			this.remove(f, ConvertMode.OFF);
			freeVariableCount--;
			return sym;
		}
		return f;
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
	 * @deprecated this method is too clumsy to reason about; it's better to use
	 *             any other alternative.
	 */
	@Deprecated
	@Override
	public V updateGenericMap(V value) {
		V oldVal = this.get(Cell.genericCell);
		if (this.internalRepresentation.containsKey(Cell.genericCell)) {
			/*
			 * Step 1: To each extension, push all those non-generic keys that
			 * this map was holding.
			 * Step 2: Push all the keysNotPresent to the extensions, if
			 * applicable. Clear the kNP set of this map.
			 * Step 3: Now, push the old value for the generic key to the
			 * extension.
			 * Step 4: After handling all the extension maps, change the value
			 * for generic key's entry in this map.
			 */
			Set<Cell> thisNonGenericKeySet = this.nonGenericKeySet();
			for (ExtensibleCellMap<V> ext : this.extensionMaps) {
				// Step 1:
				if (ext.internalRepresentation.containsKey(Cell.genericCell)) {
					continue;
				}
				for (Cell ngK : thisNonGenericKeySet) {
					if (ext.keysNotPresent != null && ext.keysNotPresent.contains(ngK)) {
						continue;
					}
					if (ext.internalRepresentation.containsKey(ngK)) {
						continue;
					}
					ext.internalRepresentation.put(ngK, this.get(ngK));
				}
				// Step 2:
				for (Cell kNP : this.keysNotPresent) {
					if (ext.keysNotPresent != null && ext.keysNotPresent.contains(kNP)) {
						continue;
					}
					if (ext.internalRepresentation.containsKey(kNP)) {
						continue;
					}
					ext.keysNotPresent.add(kNP);
				}
				this.keysNotPresent.clear();
				// Step 3:
				ext.internalRepresentation.put(Cell.genericCell, oldVal);
			}
			// Step 4:
			this.internalRepresentation.put(Cell.genericCell, value);
			return oldVal;
		} else {
			/*
			 * Step 1: Bring all non-generic keys of this map to its internal
			 * map, and seal the exit to flowBack maps by adding the generic
			 * entry (with OLD value). These changes don't change the
			 * interpretation of this map.
			 * Step 2: Now, recursively call this method to go to the if-branch
			 * of this if-else.
			 */
			// Step 1:
			Set<Cell> tempSet = new HashSet<>();
			for (Cell nGK : this.nonGenericKeySet()) {
				if (!this.internalRepresentation.containsKey(nGK)) {
					tempSet.add(nGK);
				}
			}
			for (Cell tempKey : tempSet) {
				this.internalRepresentation.put(tempKey, this.get(tempKey));
			}
			if (this.isUniversal()) {
				this.internalRepresentation.put(Cell.genericCell, oldVal);
				return this.updateGenericMap(value);
			} else {
				for (ExtensibleCellMap<V> ext : this.extensionMaps) {
					// Step 1:
					if (ext.internalRepresentation.containsKey(Cell.genericCell)) {
						continue;
					}
					Set<Cell> thisNonGenericKeySet = this.nonGenericKeySet();
					for (Cell ngK : thisNonGenericKeySet) {
						if (ext.keysNotPresent != null && ext.keysNotPresent.contains(ngK)) {
							continue;
						}
						if (ext.internalRepresentation.containsKey(ngK)) {
							continue;
						}
						ext.internalRepresentation.put(ngK, this.get(ngK));
					}
				}
				this.internalRepresentation.put(Cell.genericCell, value);
				return null;
			}
		}
		// OLD CODE BELOW:
		// if (this.isUniversal()) {
		// this.containsUniversal = true;
		// V oldVal = this.get(Cell.getGenericCell());
		// // DO NOT CHANGE THIS TO PUT().
		// this.internalRepresentation.put(Cell.getGenericCell(), value);
		// return oldVal;
		// } else {
		// this.internalRepresentation.put(Cell.getGenericCell(), value);
		// return null;
		// }
	}

	@Deprecated
	@Override
	public void forEachExpanded(BiConsumer<Cell, V> action) {
		throw new UnsupportedOperationException();
		// OLD CODE BELOW.
		// if (this.isUniversal()) {
		// Thread.dumpStack();
		// Misc.warnDueToLackOfFeature("Applying an action on all cells due to expansion
		// of keys.", Program.getRoot());
		// this.testAndConvert();
		// V globalVal = this.internalRepresentation.get(Cell.getGenericCell());
		// Set<Cell> existingKeys = this.internalRepresentation.keySet();
		// for (Cell otherCell : Cell.allCells) {
		// if (!existingKeys.contains(otherCell)) {
		// action.accept(otherCell, globalVal);
		// }
		// }
		// for (Cell existingKey : existingKeys) {
		// action.accept(existingKey, this.internalRepresentation.get(existingKey));
		// }
		// } else {
		// this.testAndConvert();
		// this.internalRepresentation.forEach(action);
		// }
	}

	@Deprecated
	@Override
	public void replaceAllExpanded(BiFunction<Cell, V, V> function) {
		throw new UnsupportedOperationException();
		// OLD CODE BELOW.
		// if (this.isUniversal()) {
		// Thread.dumpStack();
		// Misc.warnDueToLackOfFeature("Replacing values for all cells due to expansion
		// of keys.", Program.getRoot());
		// this.testAndConvert();
		// V globalVal = this.internalRepresentation.get(Cell.getGenericCell());
		// Set<Cell> existingKeys = this.internalRepresentation.keySet();
		// for (Cell otherCell : Cell.allCells) {
		// if (!existingKeys.contains(otherCell)) {
		// this.internalRepresentation.put(otherCell, function.apply(otherCell,
		// globalVal));
		// }
		// }
		// for (Cell existingKey : existingKeys) {
		// this.internalRepresentation.put(existingKey,
		// function.apply(existingKey, this.internalRepresentation.get(existingKey)));
		// }
		// this.remove(Cell.getGenericCell());
		// this.containsUniversal = false;
		// } else {
		// this.testAndConvert();
		// this.internalRepresentation.replaceAll(function);
		// }
	}
}
