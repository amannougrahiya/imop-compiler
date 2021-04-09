/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import java.util.HashSet;

public class SymbolSet extends SymbolCollection {

	public SymbolSet() {
		internalRepresentation = new HashSet<>();
	}

	public SymbolSet(SymbolSet c) {
		internalRepresentation = new HashSet<>(c.internalRepresentation);
	}
	//
	// public boolean add(Symbol s) {
	// if (s == null) {
	// Thread.dumpStack();
	// Misc.warnDueToLackOfFeature("Cannot add a null symbol.", Main.root);
	// return false;
	// }
	// if (s == Symbol.getGenericCell()) {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// return false;
	// } else {
	// internalRepresentation.clear();
	// internalRepresentation.add(s);
	// return true;
	// }
	// } else {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// return false;
	// } else {
	// return ((Set<Symbol>) internalRepresentation).add(s);
	// }
	// }
	// }
	//
	// public boolean remove(Symbol s) {
	// if (s == Symbol.getGenericCell()) {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// internalRepresentation.clear();
	// return true;
	// } else {
	// internalRepresentation.clear();
	// return true;
	// }
	// } else {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// internalRepresentation = new HashSet<>(Symbol.allSymbols);
	// internalRepresentation.remove(s);
	// return true;
	// } else {
	// return ((Set<Symbol>) internalRepresentation).remove(s);
	// }
	// }
	// }
	//
	// @Override
	// public Object clone() {
	// SymbolSet newSet = new SymbolSet(this);
	// return newSet;
	// }
	//
	// public boolean removeAll(SymbolSet c) {
	// if (c.contains(Symbol.getGenericCell())) {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// internalRepresentation.clear();
	// return true;
	// } else {
	// if (internalRepresentation.isEmpty()) {
	// return false;
	// } else {
	// internalRepresentation.clear();
	// return true;
	// }
	// }
	// } else {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// internalRepresentation.clear();
	// for (Symbol s : Symbol.allSymbols) {
	// if (!c.contains(s)) {
	// internalRepresentation.add(s);
	// }
	// }
	// return true;
	// } else {
	// return ((Set<Symbol>)
	// internalRepresentation).removeAll(c.internalRepresentation);
	// }
	// }
	// }
	//
	// public boolean containsAll(SymbolSet c) {
	// if (c.contains(Symbol.getGenericCell())) {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// return true;
	// } else {
	// return false;
	// }
	// } else {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// return true;
	// } else {
	// return ((Set<Symbol>)
	// internalRepresentation).containsAll(c.internalRepresentation);
	// }
	// }
	// }
	//
	// public boolean addAll(SymbolSet c) {
	// if (c.contains(Symbol.getGenericCell())) {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// return false;
	// } else {
	// internalRepresentation.clear();
	// internalRepresentation.add(Symbol.getGenericCell());
	// return true;
	// }
	// } else {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// return false;
	// } else {
	// boolean changed = false;
	// for (Symbol s : c) {
	// changed |= this.add(s);
	// }
	// return changed;
	// }
	// }
	// }
	//
	// public boolean retainAll(SymbolSet c) {
	// if (c.contains(Symbol.getGenericCell())) {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// return false;
	// } else {
	// return false;
	// }
	// } else {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// internalRepresentation = new HashSet<>(c.internalRepresentation);
	// return true;
	// } else {
	// return ((Set<Symbol>)
	// internalRepresentation).retainAll(c.internalRepresentation);
	// }
	// }
	// }
	//
	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = prime * result + ((internalRepresentation == null) ? 0 :
	// internalRepresentation.hashCode());
	// return result;
	// }
	//
	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (obj == null)
	// return false;
	// if (!(obj instanceof SymbolSet))
	// return false;
	// SymbolSet other = (SymbolSet) obj;
	// if (internalRepresentation == null) {
	// if (other.internalRepresentation != null)
	// return false;
	// } else if (!internalRepresentation.equals(other.internalRepresentation))
	// return false;
	// return true;
	// }
	//
}
