/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import imop.ast.node.external.*;

import java.util.Collection;

/**
 * Collects statements of the given kind in post-order.
 */
public class InfiExactStatementGetter<T extends Node> extends SuperStatementVisitor<T> {
	private final int searchCode;

	public InfiExactStatementGetter(int searchCode, Collection<T> collectIn) {
		this.searchCode = searchCode;
		this.astContents = collectIn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void endProcess(Node n) {
		if (n.getClassId() == searchCode) {
			astContents.add((T) n);
		}
	}
}
