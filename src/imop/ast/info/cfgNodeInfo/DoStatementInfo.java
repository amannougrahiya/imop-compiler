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
import imop.lib.cfg.info.DoStatementCFGInfo;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

public class DoStatementInfo extends IterationStatementInfo {

	public DoStatementInfo(Node owner) {
		super(owner);
	}

	@Override
	public DoStatementCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new DoStatementCFGInfo(getNode());
		}
		return (DoStatementCFGInfo) cfgInfo;
	}

	@Override
	public Node getLoopEntryPoint() {
		return this.getCFGInfo().getNestedCFG().getBegin();
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
		DoStatement loop = (DoStatement) this.getNode();
		Statement newBody = null;
		String newBodyString = "";
		newBodyString += "{";
		newBodyString += loop.getInfo().getCFGInfo().getBody();
		for (int i = 1; i < unrollingFactor; i++) {
			if (this.hasConstantPredicate()) {
				if (predicateIsConstantFalse()) {
					newBodyString += "break;";
				} else if (predicateIsConstantTrue()) {
					newBodyString += "";
				}
			} else {
				newBodyString += "if (!(" + loop.getInfo().getCFGInfo().getPredicate() + ")) {break;}";
			}
			newBodyString += ((CompoundStatement) loop.getInfo().getCFGInfo().getBody()).getInfo()
					.getCopyForUnrolling();
		}
		newBodyString += "}";
		newBody = FrontEnd.parseAndNormalize(newBodyString, Statement.class);
		loop.getInfo().getCFGInfo().setBody(newBody);
	}

	public boolean hasConstantPredicate() {
		Expression predicate = this.getCFGInfo().getPredicate();
		return predicate.getInfo().evaluatesToConstant();
	}

	public boolean predicateIsConstantTrue() {
		if (!this.hasConstantPredicate()) {
			return false;
		}
		Expression predicate = this.getCFGInfo().getPredicate();
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
		Expression predicate = this.getCFGInfo().getPredicate();
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
