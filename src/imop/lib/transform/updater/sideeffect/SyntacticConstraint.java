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

public class SyntacticConstraint extends SideEffect {

	private String str;

	protected SyntacticConstraint(Node affectedNode) {
		super(affectedNode);
		if (!this.toString().equals("")) {
			this.affectedNode.getInfo().getComments().add(this.toString());
		}
	}

	public SyntacticConstraint(Node affectedNode, String str) {
		super(affectedNode);
		this.str = str;
		this.affectedNode.getInfo().getComments().add(str);
	}

	@Override
	public String toString() {
		if (str != null) {
			return str;
		} else {
			if (Program.printSideEffects) {
				return "Some transformation was not possible at this node as a result of one or more syntactic constraints.";
			} else {
				return "";
			}
		}
	}

}
