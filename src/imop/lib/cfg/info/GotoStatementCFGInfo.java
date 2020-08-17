/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.info;

import imop.ast.node.external.*;
import imop.lib.util.Misc;

public class GotoStatementCFGInfo extends CFGInfo {

	public GotoStatementCFGInfo(Node node) {
		super(node);
	}

	public Statement getTarget() {
		Node outerMostEncloser = this.getOwner().getInfo().getOuterMostNonLeafEncloser();
		if (outerMostEncloser == null) {
			return null;
		}
		Statement labeledNode = outerMostEncloser.getInfo()
				.getStatementWithLabel(((GotoStatement) this.getOwner()).getF1().getTokenImage());
		if (labeledNode == null) {
			return null;
		}
		return (Statement) Misc.getInternalFirstCFGNode(labeledNode);
	}

}
