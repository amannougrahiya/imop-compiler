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
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstVisitor;

/**
 * Stores the line number of the first token found in the visited node, in
 * lineNum.
 */
public class LineNumGetter extends DepthFirstVisitor {
	public int lineNum = 0;
	public int columnNum = 0;
	boolean foundFirst = false;

	@Override
	public void visit(NodeToken n) {
		if (!foundFirst) {
			lineNum = n.getLineNum();
			columnNum = n.getColumnNum();
			foundFirst = true;
		}
		return;
	}

	@Override
	public void visit(CallStatement n) {
		lineNum = n.getLineNum();
		columnNum = n.getColumnName();
		return;
	}

	@Override
	public void visit(PreCallNode n) {
		n.getParent().accept(this);
	}

	@Override
	public void visit(PostCallNode n) {
		n.getParent().accept(this);
	}

	@Override
	public void visit(SimplePrimaryExpression n) {
		if (n.isAConstant()) {
			n.getConstant().accept(this);
		} else if (n.isAnIdentifier()) {
			n.getIdentifier().accept(this);
		}
	}

	@Override
	public void visit(BeginNode n) {
	}

	@Override
	public void visit(EndNode n) {
	}
}
