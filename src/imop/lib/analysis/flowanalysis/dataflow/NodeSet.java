/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.dataflow;

import imop.ast.node.external.*;

import java.util.HashSet;
import java.util.Set;

public class NodeSet {
	private Set<Node> internalSet;

	public NodeSet(NodeSet that) {
		this.internalSet = that.internalSet;
	}

	public NodeSet(Set<Node> nodeSet) {
		this.internalSet = nodeSet;
	}

	public boolean contains(Node n) {
		return this.internalSet.contains(n);
	}

	public void addNode(Node n) {
		if (this.internalSet.contains(n)) {
			return;
		}
		this.internalSet = new HashSet<>(this.internalSet);
		this.internalSet.add(n);
	}

	public boolean union(NodeSet that) {
		if (this.internalSet == that.internalSet) {
			return false;
		}
		if (this.internalSet.containsAll(that.internalSet)) {
			return false;
		}
		this.internalSet = new HashSet<>(this.internalSet);
		this.internalSet.addAll(that.internalSet);
		return true;
	}

	public void clear() {
		this.internalSet = new HashSet<>();
	}

	public Set<Node> getReadOnlySet() {
		Set<Node> retSet = new HashSet<>(this.internalSet);
		return retSet;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof NodeSet)) {
			return false;
		}
		NodeSet that = (NodeSet) obj;
		return this.internalSet.equals(that.internalSet);
	}

	@Override
	public int hashCode() {
		return this.internalSet.size();
	}

	public int getHash() {
		return this.internalSet.hashCode();
	}

	@Override
	public String toString() {
		return this.internalSet.toString();
	}
}
