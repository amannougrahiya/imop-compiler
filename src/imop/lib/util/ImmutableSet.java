/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ImmutableSet<V> extends AbstractSet<V> implements Immutable {

	private Set<V> internalSet = new HashSet<>();

	public ImmutableSet() {
	}

	public ImmutableSet(Collection<? extends V> coll) {
		this.internalSet.addAll(coll);
	}

	public ImmutableSet(ImmutableSet<V> other) {
		this.internalSet = other.internalSet;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ImmutableSet<?>)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		ImmutableSet<V> otherSet = (ImmutableSet<V>) o;
		if (otherSet.internalSet == this.internalSet) {
			return true;
		} else {
			return this.internalSet.equals(otherSet.internalSet);
		}
	}

	@Override
	public int hashCode() {
		return internalSet.hashCode();
	}

	@Override
	public int size() {
		return internalSet.size();
	}

	@Override
	public boolean isEmpty() {
		return internalSet.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return internalSet.contains(o);
	}

	@Override
	public Iterator<V> iterator() {
		return internalSet.iterator();
	}

	@Override
	public void forEach(Consumer<? super V> action) {
		internalSet.forEach(action);
	}

	@Override
	public Object[] toArray() {
		return internalSet.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return internalSet.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return internalSet.containsAll(c);
	}

	@Override
	public String toString() {
		return internalSet.toString();
	}

	@Override
	public boolean add(V v) {
		throw new UnsupportedOperationException("Invoked add() from ImmutableSet.");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Invoked remove() from ImmutableSet.");
	}

	@Override
	public boolean addAll(Collection<? extends V> c) {
		throw new UnsupportedOperationException("Invoked addAll() from ImmutableSet.");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Invoked removeAll() from ImmutableSet.");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Invoked retainAll() from ImmutableSet.");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Invoked clear() from ImmutableSet.");
	}

	@Override
	public boolean removeIf(Predicate<? super V> filter) {
		throw new UnsupportedOperationException("Invoked removeIf() from ImmutableSet.");
	}

	@Override
	public Spliterator<V> spliterator() {
		return this.internalSet.spliterator();
	}

	@Override
	public Stream<V> stream() {
		return this.internalSet.stream();
	}

	@Override
	public Stream<V> parallelStream() {
		return internalSet.parallelStream();
	}

}
