/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import java.util.Collection;

import imop.ast.node.external.*;
import imop.ast.node.internal.BeginNode;
import imop.ast.node.internal.CallStatement;
import imop.ast.node.internal.DummyFlushDirective;
import imop.ast.node.internal.EndNode;
import imop.ast.node.internal.PostCallNode;
import imop.ast.node.internal.PreCallNode;
import imop.ast.node.internal.SimplePrimaryExpression;
import imop.baseVisitor.DepthFirstProcess;
import imop.baseVisitor.DepthFirstVisitor;

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
