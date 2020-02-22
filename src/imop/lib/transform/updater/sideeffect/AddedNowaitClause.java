/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.updater.sideeffect;

import imop.parser.Program;
import imop.ast.node.external.Node;
import imop.ast.node.external.OmpConstruct;

public class AddedNowaitClause extends NodeUpdated {

	public AddedNowaitClause(Node affectedNode) {
		super(affectedNode);
		assert (affectedNode instanceof OmpConstruct);
		if (!this.toString().equals("")) {
			this.affectedNode.getInfo().getComments().add(this.toString());
		}
	}

	@Override
	public String toString() {
		if (Program.printSideEffects) {
			return "A nowait clause was added to this construct to make its barrier explicit.";
		} else {
			return "";
		}
	}

}
