/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.IterationStatementInfo;
import imop.ast.node.external.*;
import imop.lib.analysis.typeSystem.FloatingType;
import imop.lib.analysis.typeSystem.IntegerType;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.cfg.info.ForStatementCFGInfo;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

public class ForStatementInfo extends IterationStatementInfo {

	public ForStatementInfo(Node owner) {
		super(owner);
	}

	@Override
	public ForStatementCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new ForStatementCFGInfo(getNode());
		}
		return (ForStatementCFGInfo) cfgInfo;
	}

	@Override
	public Node getLoopEntryPoint() {
		return this.getCFGInfo().getTerminationExpression();
	}

	/**
	 * Unroll this owner loop to achieve the given {@code unrollingFactor}.
	 *
	 * @param unrollingFactor
	 *                        number of copies of iteration-body in the final state.
	 *                        <p>
	 *                        Note: Unrolling factor of 1 would mean no unrolling.
	 */
	@Override
	public void unrollLoop(int unrollingFactor) {
		ForStatement loop = (ForStatement) this.getNode();
		Statement newBody = null;
		String newBodyString = "";
		ForStatementCFGInfo cfgInfo = loop.getInfo().getCFGInfo();
		newBodyString += "{";
		newBodyString += cfgInfo.getBody();
		for (int i = 1; i < unrollingFactor; i++) {
			if (cfgInfo.hasStepExpression()) {
				newBodyString += cfgInfo.getStepExpression() + ";";
			}
			if (cfgInfo.hasTerminationExpression()) {
				if (this.hasConstantPredicate()) {
					if (predicateIsConstantFalse()) {
						newBodyString += "break;";
					} else if (predicateIsConstantTrue()) {
						newBodyString += "";
					}
				} else {
					newBodyString += "if (!(" + loop.getInfo().getCFGInfo().getTerminationExpression() + ")) {break;}";
				}
			}
			newBodyString += ((CompoundStatement) loop.getInfo().getCFGInfo().getBody()).getInfo()
					.getCopyForUnrolling();
		}
		newBodyString += "}";
		newBody = FrontEnd.parseAndNormalize(newBodyString, Statement.class);
		loop.getInfo().getCFGInfo().setBody(newBody);
	}

	public boolean hasConstantPredicate() {
		if (!this.getCFGInfo().hasTerminationExpression()) {
			return false;
		}
		Expression predicate = this.getCFGInfo().getTerminationExpression();
		return predicate.getInfo().evaluatesToConstant();
	}

	public boolean predicateIsConstantTrue() {
		if (!this.hasConstantPredicate()) {
			return false;
		}
		Expression predicate = this.getCFGInfo().getTerminationExpression();
		Type predType = Type.getType(predicate);
		if (predType instanceof IntegerType) {
			Integer predVal = Misc.evaluateInteger(predicate);
			if (predVal == null) {
				return false;
			} else {
				return (predVal == 0) ? false : true;
			}
		} else if (predType instanceof FloatingType) {
			Float predVal = Misc.evaluateFloat(predicate);
			if (predVal == null) {
				return false;
			} else {
				return (predVal == +0.0f || predVal == -0.0f) ? false : true;
			}
		} else {
			assert (false);
			return false;
		}
	}

	public boolean predicateIsConstantFalse() {
		if (!this.hasConstantPredicate()) {
			return false;
		}
		Expression predicate = this.getCFGInfo().getTerminationExpression();
		Type predType = Type.getType(predicate);
		if (predType instanceof IntegerType) {
			Integer predVal = Misc.evaluateInteger(predicate);
			if (predVal == null) {
				return false;
			} else {
				return (predVal == 0) ? true : false;
			}
		} else if (predType instanceof FloatingType) {
			Float predVal = Misc.evaluateFloat(predicate);
			if (predVal == null) {
				return false;
			} else {
				return (predVal == +0.0f || predVal == -0.0f) ? true : false;
			}
		} else {
			assert (false);
			return false;
		}
	}
}
