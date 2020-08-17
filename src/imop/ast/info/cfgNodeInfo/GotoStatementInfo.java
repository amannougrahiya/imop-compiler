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
import imop.lib.cfg.info.GotoStatementCFGInfo;

public class GotoStatementInfo extends JumpStatementInfo {
	private GotoStatementCFGInfo gotoCFGInfo = null;

	public GotoStatementInfo(Node owner) {
		super(owner);
	}

	public String getLabelName() {
		return ((GotoStatement) getNode()).getF1().getTokenImage();
	}

	@Override
	public GotoStatementCFGInfo getCFGInfo() {
		if (gotoCFGInfo == null) {
			gotoCFGInfo = new GotoStatementCFGInfo(this.getNode());
		}
		return gotoCFGInfo;
	}

}
