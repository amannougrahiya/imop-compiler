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
import imop.lib.analysis.solver.tokens.ExpressionTokenizer;
import imop.lib.analysis.solver.tokens.Tokenizable;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps those nodes of the AST which logically represent {@code e1} for
 * some dereference {@code e1[e2]}.
 * 
 * @author Aman Nougrahiya
 *
 */
public class BaseSyntax {
	public final PrimaryExpression primExp;
	public final List<APostfixOperation> postFixOps;

	public BaseSyntax(PrimaryExpression primExp, List<APostfixOperation> postFixOps2) {
		this.primExp = primExp;
		this.postFixOps = postFixOps2;
	}

	/**
	 * 
	 * @return
	 */
	public ResolvedDereference getLowerLevelDereference() {
		ResolvedDereference returnDef = null;
		if (this.postFixOps == null || this.postFixOps.isEmpty()) {
			List<CastExpression> lowerDerefCast = PointerDereferenceGetter
					.getPointerDereferencedCastExpressionLocationsOf(primExp);
			if (lowerDerefCast == null || lowerDerefCast.isEmpty()) {
				return null;
			} else {
				// Pick the first lowerDerefCast option.
				return ResolvedDereference.getResolvedAccess(lowerDerefCast.get(0));
			}
		} else {
			Node lastOp = postFixOps.get(postFixOps.size() - 1).getF0().getChoice();
			if (lastOp instanceof BracketExpression) {
				BracketExpression bracketExp = (BracketExpression) lastOp;
				List<Tokenizable> indexExpression = ExpressionTokenizer.getPrefixTokens(bracketExp.getF1());
				Node enclosingLeaf = Misc.getCFGNodeFor(primExp);
				List<APostfixOperation> newPostList = new ArrayList<>(this.postFixOps);
				newPostList.remove(newPostList.size() - 1);
				BaseSyntax lowerBase = new BaseSyntax(primExp, newPostList);
				SyntacticAccessExpression lowerSAE = new SyntacticAccessExpression(enclosingLeaf, lowerBase,
						indexExpression);
				return ResolvedDereference.getResolvedAccess(lowerSAE);
			}
		}
		return returnDef;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((postFixOps == null) ? 0 : postFixOps.hashCode());
		result = prime * result + ((primExp == null) ? 0 : primExp.hashCode());
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
		BaseSyntax other = (BaseSyntax) obj;
		if (postFixOps == null) {
			if (other.postFixOps != null) {
				return false;
			}
		} else if (!postFixOps.equals(other.postFixOps)) {
			return false;
		}
		if (primExp == null) {
			if (other.primExp != null) {
				return false;
			}
		} else if (!primExp.equals(other.primExp)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Base(" + primExp + ":" + postFixOps + ")";
	}
}
