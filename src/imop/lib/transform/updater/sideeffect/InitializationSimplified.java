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
import imop.ast.node.external.Declaration;
import imop.ast.node.external.Node;

public class InitializationSimplified extends IndexIncremented {

	public InitializationSimplified(Node affectedNode, Node baseNode) {
		super(affectedNode, baseNode);
		if (!this.toString().equals("")) {
			this.affectedNode.getInfo().getComments().add(this.toString());
		}
	}

	@Override
	public String toString() {
		if (Program.printSideEffects) {
			//			return "The index of this node was modified due to its normalization.";
			return "";
		} else {
			return "";
		}
	}

}
