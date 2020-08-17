/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.StatementInfo;
import imop.ast.node.external.*;
import imop.lib.analysis.Assignment;
import imop.lib.analysis.AssignmentGetter;

import java.util.List;

public class ExpressionStatementInfo extends StatementInfo {

	public ExpressionStatementInfo(Node owner) {
		super(owner);
	}

	public boolean isCopyInstruction() {
		List<Assignment> list = AssignmentGetter.getLexicalAssignments(this.getNode());
		if (list.size() != 1) {
			return false;
		} else {
			Assignment firstAssign = list.get(0);
			return firstAssign.isCopyInstruction();
		}
	}

}
