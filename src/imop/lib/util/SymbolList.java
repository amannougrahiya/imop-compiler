/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import java.util.ArrayList;

public class SymbolList extends SymbolCollection {

	public SymbolList() {
		this.internalRepresentation = new ArrayList<>();
	}

	public SymbolList(SymbolList otherList) {
		this.internalRepresentation = new ArrayList<>(otherList.internalRepresentation);
	}

	// public int indexOf(Symbol s) {
	// return ((List<Symbol>) internalRepresentation).indexOf(s);
	// }
	//
	// public int lastIndexOf(Symbol s) {
	// return ((List<Symbol>) internalRepresentation).lastIndexOf(s);
	// }
	//
	// @Override
	// public Object clone() {
	// SymbolList newSet = new SymbolList(this);
	// return newSet;
	// }
	//
	// public Symbol get(int index) {
	// if (((List<Symbol>)
	// internalRepresentation).contains(Symbol.getGenericCell())) {
	// Thread.dumpStack();
	// Misc.warnDueToLackOfFeature("Trying to fetch from a universal SymbolList.",
	// Main.root);
	// return Symbol.getGenericCell();
	// } else {
	// return ((List<Symbol>) internalRepresentation).get(index);
	// }
	// }
	//
	// public Symbol set(int index, Symbol s) {
	// return ((List<Symbol>) internalRepresentation).set(index, s);
	// }
	//
	// public boolean add(Symbol s) {
	// if (s == null) {
	// Thread.dumpStack();
	// Misc.warnDueToLackOfFeature("Cannot add a null symbol to a SymbolList.",
	// Main.root);
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
	// return ((List<Symbol>) internalRepresentation).add(s);
	// }
	// }
	// }
	//
	// public void add(int index, Symbol s) {
	// if (s == null) {
	// Thread.dumpStack();
	// Misc.warnDueToLackOfFeature("Cannot add a null symbol to a SymbolList.",
	// Main.root);
	// }
	// ((List<Symbol>) internalRepresentation).add(index, s);
	// }
	//
	// public Symbol remove(int index) {
	// return ((List<Symbol>) internalRepresentation).remove(index);
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
	// internalRepresentation = new ArrayList<>(Symbol.allSymbols);
	// internalRepresentation.remove(s);
	// return true;
	// } else {
	// return ((List<Symbol>) internalRepresentation).remove(s);
	// }
	// }
	// }
	//
	// public boolean removeAll(SymbolList c) {
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
	// return ((List<Symbol>)
	// internalRepresentation).removeAll(c.internalRepresentation);
	// }
	// }
	// }
	//
	// public boolean containsAll(SymbolList c) {
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
	// return ((List<Symbol>)
	// internalRepresentation).containsAll(c.internalRepresentation);
	// }
	// }
	// }
	//
	// public boolean addAll(SymbolList c) {
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
	// public boolean retainAll(SymbolList c) {
	// if (c.contains(Symbol.getGenericCell())) {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// return false;
	// } else {
	// return false;
	// }
	// } else {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// internalRepresentation = new ArrayList<>(c.internalRepresentation);
	// return true;
	// } else {
	// return ((List<Symbol>)
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
	// if (!(obj instanceof SymbolList))
	// return false;
	// SymbolList other = (SymbolList) obj;
	// if (internalRepresentation == null) {
	// if (other.internalRepresentation != null)
	// return false;
	// } else if (!internalRepresentation.equals(other.internalRepresentation))
	// return false;
	// return true;
	// }
	//
}
