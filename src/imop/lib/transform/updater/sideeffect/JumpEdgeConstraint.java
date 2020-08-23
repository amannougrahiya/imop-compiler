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

public class JumpEdgeConstraint extends SyntacticConstraint {

	public JumpEdgeConstraint(Node affectedNode) {
		super(affectedNode);
		if (!this.toString().equals("")) {
			this.affectedNode.getInfo().getComments().add(this.toString());
		}
	}

	@Override
	public String toString() {
		if (Program.printSideEffects) {
			return "This node could not be inserted at its destined location due to possible "
					+ "mismatch of sources and destinations of jump edges that may have resulted upon insertion.";
		} else {
			return "";
		}
	}

}
