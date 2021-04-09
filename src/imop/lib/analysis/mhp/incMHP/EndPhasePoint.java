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
import imop.ast.node.internal.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.Misc;

import java.util.Set;

public class EndPhasePoint extends PhasePoint {

	public EndPhasePoint(Node sink, CallStack callStack) {
		super(sink, callStack);
		assert (sink instanceof BarrierDirective || sink instanceof EndNode);
	}

	/**
	 * Note that equality of BeginPhasePoint and EndPhasePoint is well-defined.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof NodeWithStack)) {
			return false;
		}
		NodeWithStack that = (NodeWithStack) other;
		if (this.node == that.getNode() && this.callStack.equals(that.getCallStack())) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getNode() == null) ? 0 : this.getNode().hashCode());
		result = prime * result + this.callStack.size();
		return result;
	}

	@Override
	public String toString() {
		String retString = "";
		if (node instanceof BarrierDirective) {
			retString += "line#" + Misc.getLineNum(node);
		} else {
			EndNode endNode = (EndNode) node;
			Node parent = endNode.getParent();
			retString += "end_line#" + Misc.getLineNum(parent);
		}
		return retString;
	}

	@Override
	public Set<Node> getReachableNodes() {
		Thread.dumpStack();
		Misc.exitDueToError("We don't require reachable nodes for an EndPhasePoint!");
		return null;
	}

	@Override
	public Set<? extends AbstractPhase<?, ?>> getPhaseSet() {
		Thread.dumpStack();
		Misc.exitDueToError("We don't require phase information for an EndPhasePoint!");
		return null;
	}

	@Override
	public Set<? extends AbstractPhasePointable> getNextBarriers() {
		Thread.dumpStack();
		Misc.exitDueToError("We don't require next set of barriers for an EndPhasePoint!");
		return null;
	}

	@Override
	public void flushMHPData(AbstractPhase<?, ?> phase) {
		assert (false);
	}

	@Override
	public void flushData() {
		assert (false);
	}

	@Override
	public <T extends AbstractPhase<?, ?>> void removePhase(T phaseToBeRemoved) {
		assert (false) : "Unexpected path";
	}

}
