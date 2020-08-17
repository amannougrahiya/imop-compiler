/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.SelectionStatementInfo;
import imop.ast.node.external.*;
import imop.lib.cfg.info.IfStatementCFGInfo;
import imop.parser.FrontEnd;

public class IfStatementInfo extends SelectionStatementInfo {

	public IfStatementInfo(Node owner) {
		super(owner);
	}

	@Override
	public IfStatementCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new IfStatementCFGInfo(getNode());
		}
		return (IfStatementCFGInfo) cfgInfo;
	}

	/**
	 * Add an empty else-body to this if-statement it does not have one.
	 * 
	 * @param ifStmt
	 */
	public void addEmptyElse() {
		IfStatement ifStmt = (IfStatement) this.getNode();
		IfStatementCFGInfo cfgInfo = ifStmt.getInfo().getCFGInfo();
		if (cfgInfo.hasElseBody()) {
			return;
		}
		Statement emptyElse = FrontEnd.parseAndNormalize("{}", Statement.class);
		cfgInfo.setElseBody(emptyElse);
	}

}
