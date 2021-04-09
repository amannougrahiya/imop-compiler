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
import imop.lib.analysis.flowanalysis.Symbol;
import imop.parser.Program;

import java.util.Collection;
import java.util.Iterator;

public abstract class SymbolCollection implements Iterable<Symbol> {
	Collection<Symbol> internalRepresentation;
	//
	// public boolean isUniversal() {
	// if (this.contains(Symbol.getGenericCell())) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	@Override
	public Iterator<Symbol> iterator() {
		if (internalRepresentation.contains(Cell.genericCell)) {
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature("Iteration over a set with generic element.", Program.getRoot());
		}
		return internalRepresentation.iterator();
	}

	// public int size() {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// return Symbol.allSymbols.size();
	// } else {
	// return internalRepresentation.size();
	// }
	// }
	//
	// public boolean isEmpty() {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// return false;
	// } else {
	// return internalRepresentation.isEmpty();
	// }
	// }
	//
	// public boolean contains(Symbol s) {
	// if (s == Symbol.getGenericCell()) {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// return true;
	// } else {
	// return false;
	// }
	// } else {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// return true;
	// } else {
	// return internalRepresentation.contains(s);
	// }
	// }
	// }
	//
	// public void clear() {
	// internalRepresentation.clear();
	// }
	//
	// public void applyAll(Consumer<Symbol> c) {
	// if (internalRepresentation.contains(Symbol.getGenericCell())) {
	// for (Symbol s : Symbol.allSymbols) {
	// c.accept(s);
	// }
	// } else {
	// for (Symbol s : internalRepresentation) {
	// c.accept(s);
	// }
	// }
	// }
	//
	// @Override
	// public String toString() {
	// return internalRepresentation.toString();
	// }
	//
}
