/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.JumpStatementInfo;
import imop.ast.node.external.*;
import imop.lib.cfg.info.ContinueStatementCFGInfo;

public class ContinueStatementInfo extends JumpStatementInfo {

	private ContinueStatementCFGInfo continueCFGInfo = null;

	public ContinueStatementInfo(Node owner) {
		super(owner);
	}

	public Expression getTargetPredicate() {
		ContinueStatement cont = (ContinueStatement) this.getNode();
		for (Node succ : this.getCFGInfo().getSuccBlocks()) {
			if (succ instanceof Expression) {
				return (Expression) succ;
			}
		}
		return null;
	}

	@Override
	public ContinueStatementCFGInfo getCFGInfo() {
		if (continueCFGInfo == null) {
			continueCFGInfo = new ContinueStatementCFGInfo(this.getNode());
		}
		return continueCFGInfo;
	}

}
