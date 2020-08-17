/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.annotation;

import imop.ast.node.external.*;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

public class CaseLabel extends Label {
	private ConstantExpression caseExpression;

	public CaseLabel(Statement labeledCFGNode, ConstantExpression caseExpression) {
		super(labeledCFGNode);
		this.caseExpression = caseExpression;
	}

	public CaseLabel(CaseLabel other) {
		super(other.getLabeledCFGNode());
		this.caseExpression = FrontEnd.parseAndNormalize(other.caseExpression.toString(), ConstantExpression.class);
	}

	public ConstantExpression getCaseExpression() {
		return this.caseExpression;
	}

	public SwitchStatement getParentSwitch() {
		return Misc.getEnclosingSwitch(this.getLabeledCFGNode());
	}

	@Override
	public String getString() {
		return "case " + caseExpression.getInfo().getString() + ": ";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CaseLabel)) {
			return false;
		}
		CaseLabel other = (CaseLabel) obj;
		if (other.getParentSwitch() != this.getParentSwitch()) {
			return false;
		}
		if (other.getCaseExpression().toString().equals(this.getCaseExpression().toString())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		SwitchStatement switchStmt = this.getParentSwitch();
		if (this.getCaseExpression() == null) {
			result = prime * result + ((switchStmt == null) ? 0 : switchStmt.hashCode());
			return result;
		}
		String caseStr = this.getCaseExpression().toString();
		result = prime * result + ((caseStr == null) ? 0 : caseStr.hashCode());
		result = prime * result + ((switchStmt == null) ? 0 : switchStmt.hashCode());
		return result;
	}
}
