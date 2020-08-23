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

import java.util.List;

/**
 * For an array dereference of the syntactic form {@code e1[e2]} (and NOT of the
 * form {@code *(e1 + e2)}), this class represents the base expression
 * {@code e1} as a {@code BaseSyntax} (which is just a combination of a
 * {@link PrimaryExpression} with a list of post-fix operators
 * ({@link APostfixOperation})), and index-expression {@code e2} as a list of
 * tokens ({@link Tokenizable}) in prefix form.
 * <br>
 * This class is intended to be used while modeling field-sensitivity using Z3
 * solver.
 * 
 * @author Aman Nougrahiya
 *
 */
public class SyntacticAccessExpression extends AccessExpression {
	/**
	 * The syntactic expression which is used to denote the base ({@code e1} in
	 * {@code e1[e2]}).
	 * <br>
	 */
	private BaseSyntax syntacticBase = null;

	public SyntacticAccessExpression(Node leafNode, BaseSyntax syntacticBase, List<Tokenizable> indexExpression) {
		super(leafNode, indexExpression);
		this.syntacticBase = syntacticBase;
	}

	public BaseSyntax getBaseSyntax() {
		return syntacticBase;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((syntacticBase == null) ? 0 : syntacticBase.hashCode());
		result = prime * result + ((getIndexExpression() == null) ? 0 : getIndexExpression().hashCode());
		result = prime * result + ((getLeafNode() == null) ? 0 : getLeafNode().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SyntacticAccessExpression other = (SyntacticAccessExpression) obj;
		if (!this.getLeafNode().equals(other.getLeafNode())) {
			return false;
		}
		if (!this.getIndexExpression().equals(other.getIndexExpression())) {
			return false;
		}
		if (syntacticBase == null) {
			if (other.syntacticBase != null) {
				return false;
			}
		} else if (!syntacticBase.equals(other.syntacticBase)) {
			return false;
		}
		if (getIndexExpression() == null) {
			if (other.getIndexExpression() != null) {
				return false;
			}
		} else if (!getIndexExpression().equals(other.getIndexExpression())) {
			return false;
		}
		if (getLeafNode() == null) {
			if (other.getLeafNode() != null) {
				return false;
			}
		} else if (!getLeafNode().equals(other.getLeafNode())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "S-AE[" + syntacticBase + "::" + getIndexExpression() + "]";
	}
}
