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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class ImmutableCellSet extends CellSet implements Immutable {

	public int cachedHashCode = -1;

	public ImmutableCellSet() {
	}

	public ImmutableCellSet(Set<Cell> cellSet) {
		super(cellSet);
	}

	/**
	 * Note that this constructor does not copy the internalRepresentation of
	 * the argument, and uses it as it is (except when it is a list).
	 * 
	 * @param c
	 */
	public ImmutableCellSet(CellCollection c) {
		this.isDerivedFromUniversal = c.isDerivedFromUniversal;
		this.freeVariableCount = c.freeVariableCount;
		if (c.internalRepresentation instanceof CellSet) {
			this.internalRepresentation = c.internalRepresentation;
		} else {
			this.internalRepresentation = new HashSet<>(c.internalRepresentation);
		}
		this.containsUniversal = c.containsUniversal;
		if (this.isDerivedFromUniversal) {
			allDerivedCollections.add(this);
		}
	}

	@Override
	public int hashCode() {
		if (cachedHashCode == -1) {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((internalRepresentation == null) ? 0 : internalRepresentation.size());
			cachedHashCode = result;
		}
		return cachedHashCode;
	}

	@Override
	public boolean add(Cell s) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(CellList c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(CellSet c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends Cell> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> coll) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeIf(Predicate<? super Cell> filter) {
		throw new UnsupportedOperationException();
	}

}
