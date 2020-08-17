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
import imop.lib.cfg.info.BreakStatementCFGInfo;
import imop.lib.util.Misc;

public class BreakStatementInfo extends JumpStatementInfo {
	private BreakStatementCFGInfo breakCFGInfo = null;

	public BreakStatementInfo(Node owner) {
		super(owner);
	}

	public Node getCorrespondingLoopOrSwitch() {
		return Misc.getEnclosingLoopOrSwitch(this.getNode());
	}

	@Override
	public BreakStatementCFGInfo getCFGInfo() {
		if (breakCFGInfo == null) {
			breakCFGInfo = new BreakStatementCFGInfo(this.getNode());
		}
		return breakCFGInfo;
	}
}
