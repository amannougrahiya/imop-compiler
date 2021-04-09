/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.annotation.CaseLabel;
import imop.ast.annotation.DefaultLabel;
import imop.ast.annotation.Label;
import imop.ast.info.SelectionStatementInfo;
import imop.ast.info.StatementInfo;
import imop.ast.node.external.*;
import imop.lib.cfg.info.SwitchStatementCFGInfo;
import imop.lib.getter.SwitchRelevantStatementsGetter;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SwitchStatementInfo extends SelectionStatementInfo {

	public SwitchStatementInfo(Node owner) {
		super(owner);
	}

	@Override
	public SwitchStatementCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new SwitchStatementCFGInfo(getNode());
		}
		return (SwitchStatementCFGInfo) cfgInfo;
	}

	/**
	 * Obtain a list of Case and Default labels for this switch statement.
	 * 
	 * @return
	 *         list of Case/Default labels of this switch statement.
	 */
	public List<Label> getCaseDefaultLabels() {
		List<Label> enclosedLabels = new ArrayList<>();
		for (Statement stmtNode : this.getRelevantCFGStatements()) {
			List<Label> tempLabels = stmtNode.getInfo().getLabelAnnotations();
			for (Label tempLabel : tempLabels) {
				if (tempLabel instanceof CaseLabel || tempLabel instanceof DefaultLabel) {
					enclosedLabels.add(tempLabel);
				}
			}
		}
		return enclosedLabels;
	}

	/**
	 * 
	 * @return
	 * @deprecated Use {@link #hasDefaultLabel()} instead.
	 */
	@Deprecated
	public boolean hasEnclosedDefaultStatement() {
		for (Statement stmtNode : this.getRelevantCFGStatements()) {
			List<Label> tempLabels = stmtNode.getInfo().getLabelAnnotations();
			for (Label tempLabel : tempLabels) {
				if (tempLabel instanceof DefaultLabel) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Obtain a list of all those statements that contain a relevant
	 * case/default label annotation for this switch statement.
	 * 
	 * @return
	 * @deprecated
	 *             Use {@link #getRelevantCFGStatements()} instead.
	 */
	@Deprecated
	public List<Statement> getCaseDefaultLabelStatementList() {
		List<Statement> enclosedCaseStatements = new ArrayList<>();
		for (Statement stmt : this.getRelevantCFGStatements()) {
			List<Label> tempLabels = stmt.getInfo().getLabelAnnotations();
			for (Label tempLabel : tempLabels) {
				if (tempLabel instanceof DefaultLabel || tempLabel instanceof CaseLabel) {
					enclosedCaseStatements.add(stmt);
					break;
				}
			}
		}
		return enclosedCaseStatements;
	}

	/**
	 * This method returns true if the owner contains a CFG statement that has
	 * been annotated with a default label that corresponds to
	 * {@code switchStmt}.
	 * 
	 * @return
	 *         true, if there exist any default label switch statement
	 */
	public boolean hasDefaultLabel() {
		for (Statement tempNode : this.getRelevantCFGStatements()) {
			StatementInfo stmtInfo = tempNode.getInfo();
			List<Label> labelList = stmtInfo.getLabelAnnotations();
			for (Label l : labelList) {
				if (l instanceof DefaultLabel) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Obtain the CFG node with default-label annotation for ((SwitchStatement)
	 * node)
	 * SwitchStatement.
	 * 
	 * @return
	 *         an enclosed CFG node that is annotated with the default-label
	 *         linked to this switch.
	 *         null, otherwise.
	 */
	public Statement getDefaultStatement() {
		for (Statement stmtNode : this.getRelevantCFGStatements()) {
			List<Label> tempLabels = stmtNode.getInfo().getLabelAnnotations();
			for (Label tempLabel : tempLabels) {
				if (tempLabel instanceof DefaultLabel) {
					return stmtNode;
				}
			}
		}
		return null;
	}

	/**
	 * Obtain the statement, if any, which is the destination for the case
	 * {@code caseVal}.
	 * 
	 * @param caseVal
	 *                an integer case constant.
	 * @return
	 *         statement that will be the target of this predicate, if the
	 *         predicate evaluates to {@code caseVal}; null, if no such
	 *         statement exists.
	 */
	public Statement getDestinationStatement(int caseVal) {
		/*
		 * If any case matches caseVal, return that statement.
		 */
		for (Statement stmt : this.getRelevantCFGStatements()) {
			for (Label lab : stmt.getInfo().getLabelAnnotations()) {
				if (lab instanceof CaseLabel) {
					CaseLabel caseLabel = (CaseLabel) lab;
					Integer stmtCaseVal = Misc.evaluateInteger(caseLabel.getCaseExpression());
					if (stmtCaseVal != null && stmtCaseVal == caseVal) {
						return stmt;
					}
				}
			}
		}
		/*
		 * Otherwise, return the default-statement, if any; else null.
		 */
		if (this.hasDefaultLabel()) {
			return this.getDefaultStatement();
		} else {
			return null;
		}
	}

	/**
	 * Obtain the statement, if any, which is the destination for the case
	 * {@code caseVal}.
	 * 
	 * @param caseVal
	 *                an character case constant.
	 * @return
	 *         statement that will be the target of this predicate, if the
	 *         predicate evaluates to {@code caseVal}; null, if no such
	 *         statement exists.
	 */
	public Statement getDestinationStatement(char caseVal) {
		/*
		 * If any case matches caseVal, return that statement.
		 */
		for (Statement stmt : this.getRelevantCFGStatements()) {
			if (!Misc.isCFGNode(stmt)) {
				continue;
			}
			for (Label lab : stmt.getInfo().getLabelAnnotations()) {
				if (lab instanceof CaseLabel) {
					CaseLabel caseLabel = (CaseLabel) lab;
					Character stmtCaseVal = Misc.evaluateCharacter(caseLabel.getCaseExpression());
					if (stmtCaseVal != null && stmtCaseVal == caseVal) {
						return stmt;
					}
				}
			}
		}
		/*
		 * Otherwise, return the default-statement, if any; else null.
		 */
		if (this.hasDefaultLabel()) {
			return this.getDefaultStatement();
		} else {
			return null;
		}
	}

	/**
	 * Obtain a list of those nested CFG statements that are not nested inside
	 * any other {@code SwitchStatement}, and that can be the potential targets
	 * of the predicate of {@code SwitchStatement}.
	 * 
	 * @return
	 */
	public Set<Statement> getRelevantCFGStatements() {
		SwitchRelevantStatementsGetter getter = new SwitchRelevantStatementsGetter();
		Node body = this.getCFGInfo().getBody();
		if (body != null) {
			body.accept(getter); // This visit must be called on the body.
		}
		return getter.relevantCFGStmts;
	}

}
