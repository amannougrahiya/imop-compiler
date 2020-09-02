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
import imop.lib.util.Misc;

public class ParallelRegionDumper extends DepthFirstProcess {

	/**
	 * @Override
	 *           Dumps Parallel Region Info for all the Leaf CFG Nodes.
	 */
	@Override
	public void initProcess(Node n) {
		if (Misc.isCFGLeafNode(n)) {
			if (!(n instanceof BeginNode || n instanceof EndNode)) {
				if (n.getInfo() != null && n.getInfo().readRegionInfo() != null) {
					System.out.print(Misc.getLineNum(n) + ": ");
					System.out.println(n.getInfo().getString());
					System.out.print("\t\t");
					for (ParallelConstruct par : n.getInfo().readRegionInfo()) {
						System.out.print(par.hashCode() + "; ");
					}
					System.out.println();
				}
			}
		}
	}
}
