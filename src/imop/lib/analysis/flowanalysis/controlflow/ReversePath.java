/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.controlflow;

import imop.lib.analysis.flowanalysis.BranchEdge;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.util.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ReversePath {
	public final BeginPhasePoint bPP;
	public final ImmutableList<BranchEdge> edgesOnPath;
	private static int maxSizeOfPath = 0;

	public ReversePath(BeginPhasePoint startingBPP, ImmutableList<BranchEdge> edgesOnPath) {
		this.bPP = startingBPP;
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
		if (this.bPP == null) {
			if (other.bPP == null) {
				// Do nothing.
			} else {
				return false;
			}
		} else {
			if (other.bPP == null) {
				return false;
			} else {
				if (!this.bPP.equals(other.bPP)) {
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
		if (bPP == null) {
			if (other.bPP != null) {
				return false;
			}
		} else if (!bPP.equals(other.bPP)) {
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
		if (bPP != null) {
			str += bPP.getNode().getNodeId() + ",";
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
