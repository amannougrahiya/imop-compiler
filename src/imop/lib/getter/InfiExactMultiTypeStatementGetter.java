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
import java.util.Set;

/**
 * Collects statements of the given kind in post-order.
 */
public class InfiExactMultiTypeStatementGetter<T extends Node> extends SuperStatementVisitor<T> {
	private final Set<Integer> stmtTypeId;

	public InfiExactMultiTypeStatementGetter(Set<Integer> stmtTypeId, Collection<T> collectIn) {
		this.stmtTypeId = stmtTypeId;
		this.astContents = collectIn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void endProcess(Node n) {
		if (stmtTypeId.contains(n.getClassId())) {
			astContents.add((T) n);
		}
	}
}
