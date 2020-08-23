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
import imop.lib.util.Misc;

public abstract class SideEffect {
	public final Node affectedNode;

	protected SideEffect(Node affectedNode) {
		this.affectedNode = Misc.getCFGNodeFor(affectedNode);
	}

	@Override
	public String toString() {
		return "";
	}
}
