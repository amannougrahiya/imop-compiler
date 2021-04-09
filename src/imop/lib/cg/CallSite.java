/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cg;

import imop.ast.node.external.*;
import imop.lib.util.Misc;

import java.util.Vector;

/**
 * @deprecated
 * @author aman
 */
@Deprecated
public class CallSite {
	public String calleeName;
	public FunctionDefinition calleeDefinition; // is NULL if calleeDef not available
	public PostfixExpression callerExp; // The actual PostfixExpression having a call within it.
	public Node callerCFGNode; // The actual CFG node which contains this PostfixExpression.

	public CallSite(String calleeName, FunctionDefinition calleeDef, PostfixExpression callerExp) {
		this.calleeName = calleeName;
		this.calleeDefinition = calleeDef;
		this.callerExp = callerExp;
		if (callerExp != null) {
			this.callerCFGNode = Misc.getEnclosingCFGNodeInclusive(callerExp);
		}
	}

	public boolean isEqual(CallSite callS) {
		if (callS.calleeDefinition == calleeDefinition && callS.calleeName.equals(calleeName)
				&& callS.callerExp == callerExp) {
			return true;
		} else {
			return false;
		}
	}

	public boolean presentIn(Vector<CallSite> callSiteList) {
		for (CallSite callS : callSiteList) {
			if (this.isEqual(callS)) {
				// TODO: Check if this equality check is semantically correct or not.
				return true;
			}
		}
		return false;
	}

	public boolean presentKTimesIn(Vector<CallSite> callSiteList, int k) {
		int found = 0;
		for (CallSite callS : callSiteList) {
			if (this.isEqual(callS)) {
				found++;
			}
		}
		if (found >= k) {
			System.out.println("REACHED THE LIMIT K HERE!");
			// Thread.dumpStack();
			// System.exit(1);
			return true;
		}
		return false;
	}

	public boolean isEmptyCallSite() {
		if (calleeName == null || callerExp == null) {
			return true;
		} else {
			return false;
		}
	}
}
