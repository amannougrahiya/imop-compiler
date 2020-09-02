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
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.analysis.mhp.AbstractPhase;
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
					for (AbstractPhase<?, ?> ph : n.getInfo().getNodePhaseInfo().getPhaseSet()) {
						System.out.print(ph.getPhaseId() + ";");
					}
					System.out.println("]");
				}
			}
		}
	}
}
