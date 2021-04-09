/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg;

import imop.ast.info.NodeInfo;
import imop.ast.info.cfgNodeInfo.FunctionDefinitionInfo;
import imop.ast.node.external.*;
import imop.baseVisitor.GJDepthFirstProcess;
import imop.lib.util.Misc;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * This class writes the dot node and subgraph information.
 * This text is written in the file sent as an argument in argu (Type:
 * BufferedWriter)
 * 
 * @author aman
 *
 */
public class DotNodeWriter extends GJDepthFirstProcess<Object, BufferedWriter> {
	int tabSpace = 1;

	/**
	 * This method prints <tab> number of tabs, without any new lines.
	 * 
	 * @param tab
	 * @throws IOException
	 */
	public void printTabs(int tab, BufferedWriter bw) throws IOException {
		bw.write("\n");
		for (int i = 0; i < tab; i++) {
			bw.write("\t");
		}
	}

	/**
	 * In this overriding of initProcess(), we will be writing dot node
	 * information.
	 * Format of a graph node's name is: "node" + node.getInfo().hashCode()
	 * Format of a cluster (subgraph's name is) : "cluster" +
	 * node.getInfo().hashCode()
	 */
	@Override
	public Object initProcess(Node n, BufferedWriter bw) {
		try {
			if (!Misc.isCFGNode(n)) {
				return null;
			}
			NodeInfo info = n.getInfo();
			NestedCFG ncfg = info.getCFGInfo().getNestedCFG();
			if (Misc.isCFGLeafNode(n)) { // n is a Leaf Node
				printTabs(tabSpace, bw);
				bw.write("node" + info.hashCode() + "[label=\"" + Misc.putEscapes(n.getInfo().getString()) + "\"];");
				return null;
			} else if (Misc.isCFGNonLeafNode(n)) { // If n is a non-leaf CFG node
				// Create the subgraph for this CFG node
				printTabs(tabSpace, bw);
				bw.write("subgraph cluster" + info.hashCode() + " {"); // Start a new cluster
				printTabs(tabSpace + 1, bw);
				bw.write("compound=true;");
				printTabs(tabSpace + 1, bw);
				bw.write("style=dotted;");
				printTabs(tabSpace + 1, bw);
				if (n instanceof FunctionDefinition) {
					FunctionDefinitionInfo funcInfo = (FunctionDefinitionInfo) n.getInfo();
					bw.write("label=\"" + funcInfo.getFunctionName() + "()\";");
				} else {
					bw.write("label=\"" + n.getClass().getSimpleName() + "\";");
				}
				printTabs(tabSpace + 1, bw);
				// bw.write("node" + ncfg.getBegin().getInfo().hashCode() + "[label=\"begin" +
				// Misc.getShortHand(n.getClass().getSimpleName())
				bw.write("node" + ncfg.getBegin().getInfo().hashCode() + "[label=\"B_"
						+ Misc.getShortHand(n.getClass().getSimpleName()) + "\", shape=\"triangle\"];"); // Add begin
																											// node
				printTabs(tabSpace + 1, bw);
				// bw.write("node" + ncfg.getEnd().getInfo().hashCode() + "[label=\"end" +
				// Misc.getShortHand(n.getClass().getSimpleName())
				bw.write("node" + ncfg.getEnd().getInfo().hashCode() + "[label=\"E_"
						+ Misc.getShortHand(n.getClass().getSimpleName()) + "\", shape=\"invtriangle\"];"); // Add end
																											// node
				tabSpace++;
			}
		} catch (Exception e) {
			System.err.println("Type: " + n.getClass().getSimpleName());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This overridden method puts closing braces for non-leaf nodes, once all
	 * the children have
	 * been traversed.
	 */
	@Override
	public Object endProcess(Node n, BufferedWriter bw) {
		try {
			if (!Misc.isCFGNode(n)) {
				return null;
			}
			if (Misc.isCFGLeafNode(n)) { // No exit-work for leaf nodes.
				return null;
			} else if (Misc.isCFGNonLeafNode(n)) {// performs following exit-works for non-leaf nodes
				tabSpace--;
				printTabs(tabSpace, bw);
				bw.write("}"); // this completes the definition of the non-leaf node.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
