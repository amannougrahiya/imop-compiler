/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.solver;

import imop.ast.node.external.*;
import imop.lib.analysis.solver.tokens.Tokenizable;
import imop.lib.util.Misc;

import java.util.List;

/**
 * This is a superclass for {@link SyntacticAccessExpression} and
 * {@link ResolvedDereference} that logically represent static abstractions
 * of array dereferences.
 * <br>
 * These classes have been created with an intention to support modeling of
 * field-sensitivity using Z3 solver.
 * 
 * @author Aman Nougrahiya
 *
 */
public abstract class AccessExpression implements Accessible {

	/**
	 * The CFG leaf node of which this array dereference is a part.
	 */
	private final Node leafNode;

	/**
	 * List of tokens that represent the index-expression in prefix form.
	 * Note that if this list is empty, we assume that the index expression
	 * refers to a variable that may take any positive integer value.
	 */
	private List<Tokenizable> indexExpression;

	protected AccessExpression(Node leafNode, List<Tokenizable> indexExpression2) {
		assert (Misc.isCFGLeafNode(leafNode));
		this.leafNode = leafNode;
		this.indexExpression = indexExpression2;
	}

	/**
	 * List of tokens that represent the index-expression in prefix form.
	 * Note that if this list is empty, we assume that the index expression
	 * refers to a variable that may take any positive integer value.
	 * 
	 * @return
	 *         list of tokens that represent the index-expression in prefix
	 *         form.
	 */
	public List<Tokenizable> getIndexExpression() {
		return indexExpression;
	}

	/**
	 * CFG node within which this access-expression occurs.
	 * 
	 * @return
	 *         CFG node within which this access-expression occurs.
	 */
	public Node getLeafNode() {
		return leafNode;
	}

	/**
	 * Checks if this object represents an access whose index-expression is
	 * empty; in such a case, we assume that the index expression has a single
	 * variable that may take any positive integer value.
	 * 
	 * @return
	 *         true if the index-expression of this object is empty (i.e., it
	 *         logically evaluates to all possible values for this index).
	 */
	public boolean hasUnknownRange() {
		return this.getIndexExpression().isEmpty();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((indexExpression == null) ? 0 : indexExpression.hashCode());
		result = prime * result + ((leafNode == null) ? 0 : leafNode.hashCode());
		return result;
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
		AccessExpression other = (AccessExpression) obj;
		if (indexExpression == null) {
			if (other.indexExpression != null) {
				return false;
			}
		} else if (!indexExpression.equals(other.indexExpression)) {
			return false;
		}
		if (leafNode == null) {
			if (other.leafNode != null) {
				return false;
			}
		} else if (!leafNode.equals(other.leafNode)) {
			return false;
		}
		return true;
	}

}
