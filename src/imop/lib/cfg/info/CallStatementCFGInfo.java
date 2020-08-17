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
import imop.ast.node.internal.*;

import java.util.ArrayList;
import java.util.List;

public class CallStatementCFGInfo extends CFGInfo {

	public CallStatementCFGInfo(Node node) {
		super(node);
	}

	public PreCallNode getPreCallNode() {
		return ((CallStatement) this.getOwner()).getPreCallNode();
	}

	public PostCallNode getPostCallNode() {
		return ((CallStatement) this.getOwner()).getPostCallNode();
	}

	@Override
	public List<Node> getAllComponents() {
		CallStatement callStmt = (CallStatement) this.getOwner();
		List<Node> retList = new ArrayList<>();
		retList.add(callStmt.getInfo().getCFGInfo().getNestedCFG().getBegin());
		retList.add(callStmt.getPreCallNode());
		retList.add(callStmt.getPostCallNode());
		retList.add(callStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());
		return retList;
	}

}
