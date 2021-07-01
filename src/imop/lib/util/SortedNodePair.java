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

public class SortedNodePair {
	public final Node one;
	public final Node two;

	public SortedNodePair(Node one, Node two) {
		if (one.getNodeId() <= two.getNodeId()) {
			this.one = one;
			this.two = two;
		} else {
			this.one = two;
			this.two = one;
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof SortedNodePair)) {
			return false;
		}
		SortedNodePair that = (SortedNodePair) other;
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
