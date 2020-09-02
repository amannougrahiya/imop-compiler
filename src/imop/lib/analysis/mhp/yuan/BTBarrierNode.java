/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp.yuan;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;

/**
 * Represents a barrier node in a Barrier Tree.
 * Note that a barrier may be a {@link BarrierDirective}, or a {@link BeginNode}
 * or {@link EndNode} of a {@link ParallelConstruct}
 * 
 * @author Aman Nougrahiya
 *
 */
public class BTBarrierNode extends BTNode {
	public final Node barrierNode;

	public BTBarrierNode(Node barrierNode) {
		assert (barrierNode instanceof BarrierDirective
				|| ((barrierNode instanceof BeginNode || barrierNode instanceof EndNode)
						&& barrierNode.getParent() instanceof ParallelConstruct));
		this.barrierNode = barrierNode;
	}

	public Node getASTNode() {
		return this.barrierNode;
	}

	@Override
	public String getString(int tab) {
		String str = this.printTabs(tab);
		str += "barrier\n";
		return str;
	}

	@Override
	public Integer getFixedLength() {
		return 1;
	}

	@Override
	public boolean isWellMatched() {
		return true;
	}

	@Override
	public void populateBTPairs(ParallelConstruct parCons) {
		// Do nothing.
	}

}
