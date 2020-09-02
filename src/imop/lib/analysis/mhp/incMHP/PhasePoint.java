/*
 *   Copyright (c) 2020 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 *   This file is a part of the project IMOP, licensed under the MIT license.
 *   See LICENSE.md for the full text of the license.
 *
 *   The above notice shall be included in all copies or substantial
 *   portions of this file.
 */
package imop.lib.analysis.mhp.incMHP;

import imop.ast.node.external.*;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.cg.CallSite;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;

import java.util.Stack;

public abstract class PhasePoint extends NodeWithStack implements AbstractPhasePointable {
	public PhasePoint(Node sink, CallStack callStack) {
		super(sink, callStack);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof PhasePoint)) {
			return false;
		}
		PhasePoint other = (PhasePoint) obj;
		if (this.getNode() == other.getNode() && this.getCallStack().equals(other.getCallStack())) {
			return true;
		}
		return false;
	}

	@Override
	public abstract int hashCode();

	/**
	 * @deprecated
	 */
	@Deprecated
	private Stack<CallSite> callSiteStack;

	@Deprecated
	public PhasePoint(Node sink, Stack<CallSite> contextStack) {
		super(sink, null);
		this.setCallSiteStack(contextStack);
	}

	@Deprecated
	public Stack<CallSite> getCallSiteStack() {
		return callSiteStack;
	}

	@Deprecated
	public void setCallSiteStack(Stack<CallSite> callSiteStack) {
		this.callSiteStack = callSiteStack;
	}

}
