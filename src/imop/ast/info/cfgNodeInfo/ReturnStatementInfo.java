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
import imop.lib.cfg.info.ReturnStatementCFGInfo;

public class ReturnStatementInfo extends JumpStatementInfo {

	private ReturnStatementCFGInfo returnCFGInfo = null;

	public ReturnStatementInfo(Node owner) {
		super(owner);
	}

	public Expression getReturnExpression() {
		ReturnStatement retStmt = (ReturnStatement) this.getNode();
		if (retStmt.getF1().present()) {
			return (Expression) retStmt.getF1().getNode();
		} else {
			return null;
		}
	}

	@Override
	public ReturnStatementCFGInfo getCFGInfo() {
		if (returnCFGInfo == null) {
			returnCFGInfo = new ReturnStatementCFGInfo(this.getNode());
		}
		return returnCFGInfo;
	}
}
