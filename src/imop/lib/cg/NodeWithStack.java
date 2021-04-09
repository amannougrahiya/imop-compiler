/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cg;

import imop.ast.node.external.*;

public class NodeWithStack {
	protected final Node node;
	protected final CallStack callStack;

	public NodeWithStack(Node node, CallStack callStack) {
		this.node = node;
		this.callStack = callStack;
	}

	public Node getNode() {
		return node;
	}

	public CallStack getCallStack() {
		return callStack;
	}

	// @Override
	// public boolean equals(Object obj) {
	// if (!(obj instanceof NodeWithStack)) {
	// return false;
	// }
	// NodeWithStack other = (NodeWithStack) obj;
	// if (this.node == other.node && this.callStack.equals(other.callStack)) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	//
	// @Override
	// public int hashCode() {
	// return Arrays.hashCode(new Object[]{node});
	// }

	@Override
	public String toString() {
		String retString = "";
		retString += node;
		return retString;
	}

	private int hashCode = -1;

	@Override
	public int hashCode() {
		if (this.hashCode == -1) {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((node == null) ? 0 : node.hashCode());
			hashCode = result;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		NodeWithStack other = (NodeWithStack) obj;
		if (node == null) {
			if (other.node != null) {
				return false;
			}
		} else if (!node.equals(other.node)) {
			return false;
		}
		if (callStack == null) {
			if (other.callStack != null) {
				return false;
			}
		} else if (!callStack.equals(other.callStack)) {
			return false;
		}
		return true;
	}
}
