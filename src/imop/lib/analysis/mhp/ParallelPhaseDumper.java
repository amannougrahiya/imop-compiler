/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp;

import imop.ast.node.external.Node;
import imop.ast.node.internal.BeginNode;
import imop.ast.node.internal.EndNode;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.util.Misc;

public class ParallelPhaseDumper extends DepthFirstProcess {

	/**
	 * @Override
	 *           Dumps Parallel Phase Info for all the Leaf CFG Nodes.
	 */
	@Override
	public void initProcess(Node n) {
		if (Misc.isCFGLeafNode(n)) {
			if (!(n instanceof BeginNode || n instanceof EndNode)) {
				if (!n.getInfo().getNodePhaseInfo().getPhaseSet().isEmpty()) {
					System.out.println(n.getInfo().getString());
					System.out.print(" [");
					for (Phase ph : n.getInfo().getNodePhaseInfo().getPhaseSet()) {
						System.out.print(ph.getPhaseId() + ";");
					}
					System.out.println("]");
				}
			}
		}
	}
}
