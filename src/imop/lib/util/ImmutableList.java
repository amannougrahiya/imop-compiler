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
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ImmutableList<V> extends AbstractList<V> {
	private final List<V> internalList;
	final int size;
	private final int id;
	private static int internalCounter = 0;

	public ImmutableList(Collection<? extends V> coll) {
		this.internalList = new ArrayList<>();
		this.internalList.addAll(coll);
		this.size = coll.size();
		this.id = internalCounter++;
	}

	public ImmutableList(ImmutableList<V> other) {
		this.internalList = other.internalList;
		this.size = other.internalList.size();
		this.id = other.id;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public V get(int index) {
		return this.internalList.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return this.internalList.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.internalList.lastIndexOf(o);
	}

	@Override
	public boolean addAll(Collection<? extends V> c) {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	public boolean contains(Object o) {
		return this.internalList.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.internalList.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return this.internalList.isEmpty();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	public Object[] toArray() {
		return this.internalList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.internalList.toArray(a);
	}

	@Override
	public void add(int index, V element) {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	public boolean add(V e) {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	public boolean addAll(int index, Collection<? extends V> c) {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new ImmutableList<>(this);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof ImmutableList)) {
			return false;
		}
		ImmutableList<?> that = (ImmutableList<?>) o;
		if (this == that || this.id == that.id) {
			return true;
		}
		return this.internalList.equals(that.internalList);
	}

	@Override
	public void forEach(Consumer<? super V> action) {
		this.internalList.forEach(action);
	}

	private int hashCode = -1;

	@Override
	public int hashCode() {
		if (hashCode == -1) {
			hashCode = this.internalList.hashCode();
		}
		return hashCode;
	}

	@Override
	public Iterator<V> iterator() {
		return this.internalList.iterator();
	}

	@Override
	public ListIterator<V> listIterator() {
		return this.internalList.listIterator();
	}

	@Override
	public ListIterator<V> listIterator(int index) {
		return this.internalList.listIterator(index);
	}

	@Override
	public Stream<V> parallelStream() {
		return this.internalList.parallelStream();
	}

	@Override
	public V remove(int index) {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	public boolean removeIf(Predicate<? super V> filter) {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	public void replaceAll(UnaryOperator<V> operator) {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	public V set(int index, V element) {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	public void sort(Comparator<? super V> c) {
		throw new UnsupportedOperationException("Invoked an update method from ImmutableList.");
	}

	@Override
	public Spliterator<V> spliterator() {
		return this.internalList.spliterator();
	}

	@Override
	public Stream<V> stream() {
		return this.internalList.stream();
	}

	@Override
	public List<V> subList(int fromIndex, int toIndex) {
		return this.internalList.subList(fromIndex, toIndex);
	}

	@Override
	public String toString() {
		return this.internalList.toString();
	}

}
