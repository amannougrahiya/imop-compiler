/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import imop.ast.node.external.*;

public class NodePair {
	public final Node one;
	public final Node two;

	public NodePair(Node one, Node two) {
		this.one = one;
		this.two = two;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof NodePair)) {
			return false;
		}
		NodePair that = (NodePair) other;
		if ((this.one == that.one && this.two == that.two) || (this.one == that.two && this.two == that.one)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return one.hashCode() + two.hashCode();
	}
}
