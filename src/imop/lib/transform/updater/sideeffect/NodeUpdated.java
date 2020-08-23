/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.updater.sideeffect;

import imop.ast.node.external.*;
import imop.parser.Program;

public class NodeUpdated extends SideEffect {

	private String updateMessage;

	protected NodeUpdated(Node affectedNode) {
		super(affectedNode);
	}

	public NodeUpdated(Node affectedNode, String updateMessage) {
		super(affectedNode);
		this.updateMessage = updateMessage;
		if (Program.printSideEffects && updateMessage != null && !updateMessage.isEmpty()) {
			this.affectedNode.getInfo().getComments().add(updateMessage);
		}
	}

	@Override
	public String toString() {
		if (Program.printSideEffects) {
			return getUpdateMessage();
		} else {
			return "";
		}
	}

	public String getUpdateMessage() {
		return updateMessage;
	}

}
