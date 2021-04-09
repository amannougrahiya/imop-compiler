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
import imop.baseVisitor.GJVoidDepthFirstProcess;
import imop.lib.util.Misc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 */
public class SimpleNodeEdgeWriter extends GJVoidDepthFirstProcess<BufferedWriter> {

	@Override
	public void initProcess(Node n, BufferedWriter bw) {
		if (n.getInfo() == null || n.getInfo().getCFGInfo() == null) {
			return;
		}
		if (!Misc.isCFGLeafNode(n)) {
			return;
		}

		try {
			// Print the dot format of node
			bw.write("\n\tnode" + n.getInfo().hashCode() + "[label=\"" + Misc.putEscapes(n.getInfo().getString())
					+ "\"];");
			// Get successors of node n
			List<Node> succList = n.getInfo().getCFGInfo().deprecated_getSucc();
			if (succList != null) {
				for (Node succ : succList) {
					// Add an edge between n and succ
					bw.write("\n\tnode" + n.getInfo().hashCode() + " ::= node" + succ.getInfo().hashCode()
							+ "[weight=8, tailport=s, headport=n];");

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

}
