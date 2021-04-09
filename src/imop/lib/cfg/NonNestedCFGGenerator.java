/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg;

import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.util.Misc;

import java.util.List;

/**
 * This visitor calls createCFG() on all the leaf CFG nodes.
 * createCFG() adds the succ links from the given node to its leaf successors.
 */
public class NonNestedCFGGenerator extends DepthFirstProcess {

	/**
	 * Create link between pred and succ
	 * 
	 * @param pred
	 * @param succ
	 */
	public void connect(Node pred, Node succ) {
		// If the edges are not already present, add them.
		boolean edgeFound = false;
		List<Node> succList = pred.getInfo().getCFGInfo().deprecated_getSucc();
		for (Node edge : succList) {
			if (edge.hashCode() == succ.hashCode()) {
				edgeFound = true;
				break;
			}
		}
		if (!edgeFound) {
			pred.getInfo().getCFGInfo().deprecated_getSucc().add(succ);
			succ.getInfo().getCFGInfo().deprecated_getPred().add(pred);
		}
	}

	/**
	 * Overridden to process all the CFG leaf nodes.
	 */
	@Override
	public void initProcess(Node n) {
		if (Misc.isCFGLeafNode(n)) {
			List<Node> nestedSuccessors = n.getInfo().getCFGInfo().getSuccBlocks();
			for (Node nestedSucc : nestedSuccessors) { // For all the nestedSuccessors of the node n
				List<Node> leavesOnThisPath = Misc.getFirstLeaves(nestedSucc); // Get the first leaf nodes
				for (Node nextLeaf : leavesOnThisPath) {
					connect(n, nextLeaf); // Connect n to the first leaf nodes
				}
			}
		}
	}
}
