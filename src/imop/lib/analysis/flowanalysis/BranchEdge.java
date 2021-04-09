/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis;

import imop.ast.node.external.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents one of the many branches that a predicate may take.
 * NOTE: We intentionally do not keep the
 * successor here, so that no update is required.
 *
 * @author Aman Nougrahiya
 */
public class BranchEdge {
	public final Expression predicate;
	public final int succNmber;

	public BranchEdge(Expression predicate, int succNmber) {
		this.predicate = predicate;
		this.succNmber = succNmber;
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
		BranchEdge other = (BranchEdge) obj;
		if (predicate == null) {
			if (other.predicate != null) {
				return false;
			}
		} else if (!predicate.equals(other.predicate)) {
			return false;
		}
		if (succNmber != other.succNmber) {
			return false;
		}
		return true;
	}

	private int hashCode = -1;

	@Override
	public int hashCode() {
		if (this.hashCode == -1) {
			final int prime = 31;
			hashCode = 1;
			hashCode = prime * hashCode + ((predicate == null) ? 0 : predicate.hashCode());
			hashCode = prime * hashCode + succNmber;
		}
		return this.hashCode;
	}

	/**
	 * Get all other sibling branches for this branch.
	 *
	 * @return
	 */
	public Collection<?> getOthers() {
		Set<BranchEdge> otherBranches = new HashSet<>();
		for (BranchEdge be : this.predicate.getInfo().getBranchEdges()) {
			if (!this.equals(be)) {
				otherBranches.add(be);
			}
		}
		return otherBranches;
	}

	@Override
	public String toString() {
		return predicate.getNodeId() + "_" + succNmber;
	}

}
