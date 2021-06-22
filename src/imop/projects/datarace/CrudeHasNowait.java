/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.projects.datarace;

import imop.ast.node.NowaitClause;
import imop.baseVisitor.DepthFirstVisitor;

/**
 */
public class CrudeHasNowait extends DepthFirstVisitor {
	boolean foundNowait = false;

	/**
	 * f0 ::= <NOWAIT>
	 */
	@Override
	public void visit(NowaitClause n) {
		foundNowait = true;
		n.getF0().accept(this);
	}

}
