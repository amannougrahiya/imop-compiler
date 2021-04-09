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
import imop.lib.builder.Builder;
import imop.lib.cfg.info.WhileStatementCFGInfo;
import imop.lib.transform.simplify.CompoundStatementNormalizer;
import imop.lib.transform.updater.InsertImmediatePredecessor;
import imop.lib.transform.updater.InsertOnTheEdge;
import imop.lib.transform.updater.NodeRemover;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

import java.util.ArrayList;
import java.util.List;

public class WhileStatementInfo extends IterationStatementInfo {

	public WhileStatementInfo(Node owner) {
		super(owner);
	}

	@Override
	public WhileStatementCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new WhileStatementCFGInfo(getNode());
		}
		return (WhileStatementCFGInfo) cfgInfo;
	}

	@Override
	public Node getLoopEntryPoint() {
		return this.getCFGInfo().getPredicate();
	}

	public void unrollLoop() {
		this.unrollLoop(1);
	}

	/**
	 * Unroll this owner loop to achieve the given {@code unrollingFactor}.
	 *
	 * @param unrollingFactor
	 *                        number of <i>new</i> copies of iteration-body in the
	 *                        final state.
	 */
	@Override
	public void unrollLoop(int unrollingFactor) {
		WhileStatement loop = (WhileStatement) this.getNode();
		Statement newBody = null;
		String newBodyString = "";
		newBodyString += "{";
		newBodyString += loop.getInfo().getCFGInfo().getBody();
		for (int i = 0; i < unrollingFactor; i++) {
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
		CompoundStatementNormalizer.removeExtraScopes(loop);
	}

	public List<SideEffect> changePredicateToConstantTrue() {
		List<SideEffect> sideEffectList = new ArrayList<>();
		if (this.predicateIsConstantTrue()) {
			return sideEffectList;
		}
		WhileStatement whileStmt = (WhileStatement) this.getNode();
		Expression predicate = this.getCFGInfo().getPredicate();

		/*
		 * Step 1: Insert the declaration for temp.
		 */
		Type predType = Type.getType(predicate);
		String tempName = Builder.getNewTempName("whilePred");
		Declaration tempDecl = predType.getDeclaration(tempName);
		sideEffectList.addAll(InsertImmediatePredecessor.insert(whileStmt, tempDecl));

		// Old Code. Now, we insert the break point "before" the predicate.
		// Statement predBreak;
		// for (Node predicateSucc :
		// predicate.getInfo().getCFGInfo().getLeafSuccessors()) {
		// if (this.hasConstantPredicate()) {
		// assert (this.predicateIsConstantFalse()); // Else we would have returned
		// already.
		// predBreak = FrontEnd.parseAndNormalize("break;", Statement.class);
		// } else {
		// predBreak = FrontEnd.parseAndNormalize("if (!" + tempName + ") {break;}",
		// Statement.class);
		// }
		// if (predicateSucc instanceof EndNode) {
		// continue;
		// }
		// sideEffectList.addAll(InsertOnTheEdge.insertAggressive(predicate,
		// predicateSucc, predBreak));
		// }

		/*
		 * Step 2: Insert the evaluation of temp at all predecessors.
		 */
		Statement predEval = FrontEnd.parseAndNormalize(tempName + " = " + predicate + ";", Statement.class);
		sideEffectList.addAll(InsertImmediatePredecessor.insert(predicate, predEval));

		/*
		 * Step 3: Execute the while loop conditionally.
		 */
		Statement ifStmtStatement = FrontEnd.parseAndNormalize("if (" + tempName + ") {}", Statement.class);
		sideEffectList.addAll(InsertImmediatePredecessor.insert(whileStmt, ifStmtStatement));
		sideEffectList.addAll(NodeRemover.removeNode(whileStmt));
		IfStatement ifStmt = (IfStatement) Misc.getCFGNodeFor(ifStmtStatement);
		CompoundStatement thenBody = (CompoundStatement) ifStmt.getInfo().getCFGInfo().getThenBody();
		sideEffectList.addAll(thenBody.getInfo().getCFGInfo().addElement(whileStmt));

		/*
		 * Step 4: At all predecessors of the predicate, excluding the BeginNode
		 * of the whileStmt, insert the break.
		 */
		for (Node predicatePred : predicate.getInfo().getCFGInfo().getLeafPredecessors()) {
			if (predicatePred == whileStmt.getInfo().getCFGInfo().getNestedCFG().getBegin()) {
				continue;
			}
			// Create a breaking block.
			Statement predBreak;
			predBreak = FrontEnd.parseAndNormalize("if (!" + tempName + ") {break;}", Statement.class);
			InsertOnTheEdge.insert(predicatePred, predicate, predBreak);
		}

		/*
		 * Finally, replace the predicate with 1.
		 */
		Expression newPred = FrontEnd.parseAndNormalize("1", Expression.class);
		this.getCFGInfo().setPredicate(newPred);

		return sideEffectList;
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
