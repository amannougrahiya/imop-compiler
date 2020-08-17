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

public class ReturnStatementCFGInfo extends CFGInfo {

	public ReturnStatementCFGInfo(Node node) {
		super(node);
	}

	public Node getTarget() {
		Node targetOwn = Misc.getEnclosingFunction(this.getOwner());
		if (targetOwn == null) {
			return null;
		}
		return targetOwn.getInfo().getCFGInfo().getNestedCFG().getEnd(); // get endNode of the enclosing function
	}

}
