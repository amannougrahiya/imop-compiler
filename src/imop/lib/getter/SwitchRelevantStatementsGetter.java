/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import imop.ast.annotation.CaseLabel;
import imop.ast.annotation.DefaultLabel;
import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

/**
 * This visitor populates "cases" with the case (and default) labeled statements
 * of a given switch statement's body "n".
 * <br>
 * <b>NOTE</b>: This visitor should be called on a switch statement's body, and
 * not the switch statement itself.
 * 
 */
public class SwitchRelevantStatementsGetter extends DepthFirstProcess {

	public boolean foundDefaultLabel = false;
	public Set<Statement> relevantCFGStmts = new HashSet<>();

	@Override
	public void initProcess(Node n) {
		if (n instanceof Statement && Misc.isCFGNode(n)) {
			Statement stmt = (Statement) n;
			if (stmt.getInfo().getLabelAnnotations().stream().anyMatch(l -> {
				if (l instanceof CaseLabel) {
					return true;
				} else if (l instanceof DefaultLabel) {
					foundDefaultLabel = true;
					return true;
				} else {
					return false;
				}
			})) {
				relevantCFGStmts.add((Statement) n);
			}

		}
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(DefaultLabeledStatement n) {
		assert (false) : "The visitor SwitchRelevantStatementsGetter shouldn't have been called "
				+ "unless all labels have been extracted already.";
		// OLD CODE BELOW:
		initProcess(n);
		foundDefaultLabel = true;
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
	}

	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SwitchStatement n) {
		initProcess(n);
		// No accepts here ensure that case statements of the internal switches are not
		// added to "cases".
		// NOTE: This visitor is called with Switch statement's body, and not the switch
		// statement itself.
	}

}
