/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

import imop.ast.node.external.*;

public class NodeStatePair {
	private int hashCode = -1;

	@Override
	public int hashCode() {
		if (hashCode == -1) {
			final int prime = 31;
			hashCode = 1;
			hashCode = prime * hashCode + ((node == null) ? 0 : node.hashCode());
			hashCode = prime * hashCode + (stateINChanged ? 1231 : 1237);
			return hashCode;
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
		if (!(obj instanceof NodeStatePair)) {
			return false;
		}
		NodeStatePair other = (NodeStatePair) obj;
		if (node == null) {
			if (other.node != null) {
				return false;
			}
		} else if (!node.equals(other.node)) {
			return false;
		}
		if (stateINChanged != other.stateINChanged) {
			return false;
		}
		return true;
	}

	public final Node node;
	public final boolean stateINChanged;

	public NodeStatePair(Node node, boolean stateINChanged) {
		this.node = node;
		this.stateINChanged = stateINChanged;
	}
}
