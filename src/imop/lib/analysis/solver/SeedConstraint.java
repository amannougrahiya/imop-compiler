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
import imop.lib.analysis.solver.tokens.IdOrConstToken;
import imop.lib.analysis.solver.tokens.OperatorToken;
import imop.lib.analysis.solver.tokens.Tokenizable;

import java.util.Collections;
import java.util.List;

/**
 * Intermediate representation of the constraints of the form
 * {@code e1 = e2} in Z3 system of (in)equations, represented in terms of
 * their C counterparts.
 * 
 * @author Aman Nougrahiya
 *
 */
public class SeedConstraint {
	private final List<Tokenizable> lhsExp;
	private final Node lhsLeafNode;
	public final IdOrConstToken tidLHS;
	private final List<Tokenizable> rhsExp;
	private final Node rhsLeafNode;
	public final IdOrConstToken tidRHS;
	private final OperatorToken operator;

	public SeedConstraint(List<Tokenizable> lhsExp, Node lhsLeafNode, IdOrConstToken tidLHS, List<Tokenizable> rhsExp,
			Node rhsLeafNode, IdOrConstToken tidRHS, OperatorToken opToken) {
		this.lhsExp = lhsExp;
		this.rhsExp = rhsExp;
		this.lhsLeafNode = lhsLeafNode;
		this.rhsLeafNode = rhsLeafNode;
		this.tidLHS = tidLHS;
		this.tidRHS = tidRHS;
		if (opToken == null) {
			this.operator = OperatorToken.ASSIGN;
		} else {
			this.operator = opToken;
		}
		assert (!lhsExp.isEmpty() && !rhsExp.isEmpty());
		assert (lhsLeafNode != null && rhsLeafNode != null);
	}

	public List<Tokenizable> getLhsExp() {
		return Collections.unmodifiableList(lhsExp);
	}

	public List<Tokenizable> getRhsExp() {
		return Collections.unmodifiableList(rhsExp);
	}

	public Node getLhsLeafNode() {
		return lhsLeafNode;
	}

	public Node getRhsLeafNode() {
		return rhsLeafNode;
	}

	@Override
	public String toString() {
		return lhsExp + " (" + tidLHS + ") = " + rhsExp + " (" + tidRHS + ")";
	}

	public OperatorToken getOperator() {
		return operator;
	}

}
