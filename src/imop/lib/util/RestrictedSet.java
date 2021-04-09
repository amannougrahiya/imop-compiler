/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Provides a superclass that can be used to extend all specialized subclasses
 * of a set in such a way that any invocation of a method that has not been
 * overridden by the specialized subclass would throw an
 * {@link UnsupportedOperationException}.
 * 
 * @author Aman Nougrahiya
 *
 * @param <E>
 *            type of elements in the set.
 */
public class RestrictedSet<E> extends AbstractSet<E> {

	public RestrictedSet() {
	}

	@Override
	public boolean equals(Object o) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method equals() from RestrictedSet.");
	}

	@Override
	public int hashCode() {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method hashCode() from RestrictedSet.");
	}

	@Override
	public int size() {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method size() from RestrictedSet.");
	}

	@Override
	public boolean isEmpty() {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method isEmpty() from RestrictedSet.");
	}

	@Override
	public boolean contains(Object o) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method contains() from RestrictedSet.");
	}

	@Override
	public Iterator<E> iterator() {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method iterator() from RestrictedSet.");
	}

	@Override
	public void forEach(Consumer<? super E> action) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method forEach() from RestrictedSet.");
	}

	@Override
	public boolean add(E v) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method add() from RestrictedSet.");
	}

	@Override
	public boolean remove(Object o) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method remove() from RestrictedSet.");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method containsAll() from RestrictedSet.");
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
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
	public String toString() {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method toString() from RestrictedSet.");
	}

	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		Thread.dumpStack();
		throw new UnsupportedOperationException("Invoked unimplemented method removeIf() from RestrictedSet.");
	}

	// @Override
	// public Spliterator<E> spliterator() {
	// Thread.dumpStack();
	// throw new UnsupportedOperationException("Invoked unimplemented method
	// spliterator() from RestrictedSet.");
	// }
	//
	// @Override
	// public Stream<E> stream() {
	// Thread.dumpStack();
	// throw new UnsupportedOperationException("Invoked unimplemented method
	// stream() from RestrictedSet.");
	// }
	//
	// @Override
	// public Stream<E> parallelStream() {
	// Thread.dumpStack();
	// throw new UnsupportedOperationException("Invoked unimplemented method
	// parallelStream() from RestrictedSet.");
	// }
}
