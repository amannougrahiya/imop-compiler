/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis;

import imop.lib.util.Immutable;

import java.util.*;
import java.util.function.Predicate;

public class ImmutableDefinitionSet extends AbstractSet<Definition> implements Immutable {
	private Set<Definition> defSet;

	@Override
	public int hashCode() {
		return this.defSet.size();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		ImmutableDefinitionSet other = (ImmutableDefinitionSet) obj;
		if (defSet == null) {
			if (other.defSet != null) {
				return false;
			}
		} else {
			if (defSet == other.defSet) {
				return true;
			} else if (!defSet.equals(other.defSet)) {
				return false;
			}
		}
		return true;
	}

	public ImmutableDefinitionSet(Set<Definition> defSet) {
		this.defSet = defSet;
	}

	public ImmutableDefinitionSet() {
		this.defSet = new HashSet<>();
	}

	@Override
	public Iterator<Definition> iterator() {
		return defSet.iterator();
	}

	public Set<Definition> getInternalSetCopy() {
		return new HashSet<>(defSet);
	}

	@Override
	public int size() {
		return defSet.size();
	}

	@Override
	public boolean add(Definition v) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method add() from RestrictedSet.");
	}

	@Override
	public boolean remove(Object o) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method remove() from RestrictedSet.");
	}

	@Override
	public boolean addAll(Collection<? extends Definition> c) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method addAll() from RestrictedSet.");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method removeAll() from RestrictedSet.");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method retainAll() from RestrictedSet.");
	}

	@Override
	public void clear() {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method clear() from RestrictedSet.");
	}

	@Override
	public boolean removeIf(Predicate<? super Definition> filter) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method removeIf() from RestrictedSet.");
	}

}
