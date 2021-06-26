/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.controlflow;

import imop.ast.node.external.BarrierDirective;
import imop.ast.node.external.Node;
import imop.ast.node.internal.PostCallNode;
import imop.lib.analysis.flowanalysis.BranchEdge;
import imop.lib.util.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ReversePath {
	/**
	 * Represents a point where any intra-phase intra-procedural path starts.
	 * This can either be a BarrierDirective, a PostCallNode, BeginNode of a
	 * ParallelConstruct, or BeginNode of a FunctionDefinition.
	 */
	public static interface PathStartable {
		public default Node getNode() {
			return (Node) this;
		}
	}

	public final PathStartable startPoint;
	public final ImmutableList<BranchEdge> edgesOnPath;
	private static int maxSizeOfPath = 0;

	public ReversePath(PathStartable startPoint, ImmutableList<BranchEdge> edgesOnPath) {
		this.startPoint = startPoint;
		this.edgesOnPath = edgesOnPath;
		if (edgesOnPath.size() > maxSizeOfPath) {
			maxSizeOfPath = edgesOnPath.size();
			// System.err.println("Maximum edges on path: " + maxSizeOfPath);
		}
	}

	List<BranchEdge> getNewList(BranchEdge be) {
		int predicateIndex = this.getPredicateIndexOf(be);
		List<BranchEdge> newList;
		newList = new ArrayList<>(this.edgesOnPath);
		if (predicateIndex != -1) {
			for (int i = predicateIndex; i >= 0; i--) {
				newList.remove(i);
			}
		}
		newList.add(0, be);
		return newList;
	}

	private int getPredicateIndexOf(BranchEdge be) {
		int i = -1;
		for (BranchEdge b : this.edgesOnPath) {
			i++;
			if (b.predicate == be.predicate) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Checks if the receiver path is subsumed in the argument path as a suffix.
	 *
	 * @param other
	 *
	 * @return
	 */
	public boolean isStrictlySubsumedAsSuffixOf(ReversePath other) {
		if (this.startPoint == null) {
			if (other.startPoint == null) {
				// Do nothing.
			} else {
				return false;
			}
		} else {
			if (other.startPoint == null) {
				return false;
			} else {
				if (this.startPoint != other.startPoint) {
					return false;
				}
			}
		}
		if (this.edgesOnPath.size() >= other.edgesOnPath.size()) {
			return false;
		}
		ListIterator<BranchEdge> thisIterator = this.edgesOnPath.listIterator(this.edgesOnPath.size());
		ListIterator<BranchEdge> thatIterator = other.edgesOnPath.listIterator(other.edgesOnPath.size());
		while (thisIterator.hasPrevious()) {
			BranchEdge thisEdge = thisIterator.previous();
			BranchEdge thatEdge = thatIterator.previous();
			if (!thisEdge.equals(thatEdge)) {
				return false;
			}
		}
		// System.err.println("The path " + this + " is subsumed by the bigger path " +
		// other);
		return true;
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
		ReversePath other = (ReversePath) obj;
		if (startPoint == null) {
			if (other.startPoint != null) {
				return false;
			}
		} else if (startPoint != other.startPoint) {
			return false;
		}
		if (edgesOnPath == null) {
			if (other.edgesOnPath != null) {
				return false;
			}
		} else if (!edgesOnPath.equals(other.edgesOnPath)) {
			return false;
		}
		return true;
	}

	private int hashCode = -1;

	@Override
	public int hashCode() {
		if (hashCode == -1) {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((edgesOnPath == null) ? 0 : edgesOnPath.hashCode());
			hashCode = result;
		}
		return hashCode;
	}

	@Override
	public String toString() {
		String str = "\nReverse<";
		if (startPoint != null) {
			str += startPoint.getNode().getNodeId() + ",";
		} else {
			str += "--,";
		}
		for (BranchEdge edge : edgesOnPath) {
			str += " " + edge.toString() + ";";
		}
		str += ">";
		return str;
	}
}
