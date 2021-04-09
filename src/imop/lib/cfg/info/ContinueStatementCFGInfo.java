/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.info;

import imop.ast.node.external.*;
import imop.lib.util.Misc;

public class ContinueStatementCFGInfo extends CFGInfo {

	public ContinueStatementCFGInfo(Node node) {
		super(node);
	}

	public Node getTarget() {
		Node targetOwner = Misc.getEnclosingLoopOrForConstruct(this.getOwner()); // Obtain the owner loop of this
																					// continue statement

		if (targetOwner == null) {
			return null;
		}

		Node target = null; // target represents the target statement of this continue
		if (targetOwner instanceof WhileStatement) {
			target = ((WhileStatementCFGInfo) targetOwner.getInfo().getCFGInfo()).getPredicate();
			// target = Misc.getInternalFirstCFGNode(((WhileStatement)
			// targetOwner).getF2()); // Target is the termination condition
		} else if (targetOwner instanceof DoStatement) {
			target = ((DoStatementCFGInfo) targetOwner.getInfo().getCFGInfo()).getPredicate();
			// target = Misc.getInternalFirstCFGNode(((DoStatement) targetOwner).getF4());
			// // Target is the termination condition

		} else if (targetOwner instanceof ForStatement) {
			ForStatement forS = ((ForStatement) targetOwner);
			ForStatementCFGInfo forInfo = forS.getInfo().getCFGInfo();
			if (forInfo.hasStepExpression()) {
				target = forInfo.getStepExpression();
			} else if (forInfo.hasTerminationExpression()) {
				target = forInfo.getTerminationExpression();
			} else {
				target = forInfo.getBody();
			}
			// if (forS.getF6().present()) { // If step is present
			// target = Misc.getInternalFirstCFGNode(forS.getF6());
			// } else if (forS.getF4().present()) { // else if termination condition is
			// present
			// target = Misc.getInternalFirstCFGNode(forS.getF4());
			// } else {
			// target = Misc.getInternalFirstCFGNode(forS.getF8());
			// }

		} else if (targetOwner instanceof ForConstruct) {
			ForConstructCFGInfo cfgInfo = (ForConstructCFGInfo) targetOwner.getInfo().getCFGInfo();
			target = cfgInfo.getReinitExpression(); // Target is the step-expression.
		}
		return target;
	}

}
