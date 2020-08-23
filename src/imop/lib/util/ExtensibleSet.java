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

/**
 * NOTE: This implementation is not complete. Do NOT use this class.
 * 
 * @author Aman Nougrahiya
 *
 * @param <V>
 */
public class ExtensibleSet<V> extends AbstractSet<V> implements Immutable {

	private Set<V> internalSet = new HashSet<>();
	private ExtensibleSet<V> fallBackSet;
	private Set<V> negationSet;
	private Set<ExtensibleSet<V>> extensionSets = new HashSet<>(); // Those maps which extend upon this map.

	public ExtensibleSet() {
	}

	public ExtensibleSet(Collection<? extends V> coll) {
		this.internalSet.addAll(coll);
	}

	public ExtensibleSet(ExtensibleSet<V> other) {
		if (other != null) {
			this.fallBackSet = other;
			other.extensionSets.add(this);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ExtensibleSet<?>)) {
			return false;
		}
		ExtensibleSet<?> other = (ExtensibleSet<?>) o;
		if (this.size() != other.size()) {
			return false;
		}

		for (V elem : this) {
			if (!other.contains(elem)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int code = 0;
		for (V elem : this) {
			code += elem.hashCode();
		}
		return code;
	}

	@Override
	public int size() {
		int count = 0;
		for (Iterator<V> iterator = this.iterator(); iterator.hasNext();) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		if (this.internalSet.isEmpty()) {
			if (fallBackSet == null) {
				return true;
			} else {
				return fallBackSet.isEmpty();
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean contains(Object o) {
		for (V elem : this) {
			if (elem.equals(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<V> iterator() {
		// TODO: Main code here.
		return null;
	}

	@Override
	public void forEach(Consumer<? super V> action) {
		if (action == null) {
			throw new NullPointerException();
		}
		for (V elem : this) {
			action.accept(elem);
		}
		internalSet.forEach(action);
	}

	@Override
	public Object[] toArray() {
		return super.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return super.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return super.containsAll(c);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public boolean add(V v) {
		if (this.contains(v)) {
			return false;
		}
		this.internalSet.add(v);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		if (!this.contains(o)) {
			return false;
		}
		Set<V> newSet = new HashSet<>();
		for (V elem : this) {
			if (elem != o) {
				newSet.add(elem);
			}
		}
		this.internalSet = newSet;
		if (this.fallBackSet != null) {
			this.fallBackSet.extensionSets.remove(this);
			this.fallBackSet = null;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends V> c) {
		boolean added = false;
		for (V elem : c) {
			added |= this.add(elem);
		}
		return added;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean removed = false;
		Set<V> newSet = new HashSet<>();
		for (V elem : this) {
			if (!c.contains(elem)) {
				removed |= newSet.add(elem);
			}
		}
		this.internalSet = newSet;
		if (this.fallBackSet != null) {
			this.fallBackSet.extensionSets.remove(this);
			this.fallBackSet = null;
		}
		return removed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		super.retainAll(c);
		throw new UnsupportedOperationException("Invoked retainAll() from ExtensibleSet.");
	}

	@Override
	public void clear() {
		this.internalSet.clear();
		if (this.fallBackSet != null) {
			this.fallBackSet.extensionSets.remove(this);
			this.fallBackSet = null;
		}
	}

	@Override
	public boolean removeIf(Predicate<? super V> filter) {
		Set<V> toBeRemoved = new HashSet<>();
		for (V elem : this) {
			if (filter.test(elem)) {
				toBeRemoved.add(elem);
			}
		}
		return this.removeAll(toBeRemoved);
	}

	@Override
	public Spliterator<V> spliterator() {
		return super.spliterator();
	}

	@Override
	public Stream<V> stream() {
		return super.stream();
	}

	@Override
	public Stream<V> parallelStream() {
		return super.parallelStream();
	}

}
