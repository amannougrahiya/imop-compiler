/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg;

import imop.ast.node.external.Node;

public class CFGEdge {
	public final Node src;
	public final Node dest;
	private int hashCode = -1;

	public CFGEdge(Node src, Node dest) {
		assert (src != null && dest != null);
		this.src = src;
		this.dest = dest;
	}

	@Override
	public int hashCode() {
		if (hashCode == -1) {
			hashCode = 31 * src.getNodeId() + dest.getNodeId();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CFGEdge)) {
			return false;
		}
		CFGEdge other = (CFGEdge) obj;
		if (dest != other.dest || src != other.src) {
			return false;
		}
		return true;
	}

}
