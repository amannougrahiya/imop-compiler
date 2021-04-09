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
import imop.ast.node.external.*;
import imop.baseVisitor.GJDepthFirstProcess;
import imop.lib.util.Misc;

import java.io.BufferedWriter;

/**
 * This class writes the dot edges' information.
 * This text is written in the file sent as an argument in argu (Type:
 * BufferedWriter)
 * 
 * @author aman
 *
 */
public class DotEdgeWriter extends GJDepthFirstProcess<Object, BufferedWriter> {

	/**
	 * In this overriding of initProcess(), we will be writing dot edges.
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
				// Problem: May induce cycles across clusters, which our DOT doesn't handle
				// well.
				// if (n instanceof DummyFlushDirective) {
				// DummyFlushDirective df = (DummyFlushDirective) n;
				// for (InterTaskEdge interTaskEdge : df.getInfo().getOutgoingInterTaskEdges())
				// {
				// Node base = interTaskEdge.getSourceNode();
				// assert(base == n);
				// Node succ = interTaskEdge.getDestinationNode();
				// bw.write("\n\tnode" + base.getInfo().hashCode() + " ::= node" +
				// succ.getInfo().hashCode()
				// + "[weight=8, color=red, style=dotted, tailport=s, headport=n];"); // Add an
				// edge between n and succ
				// }
				// }
				for (Node succ : info.getCFGInfo().getSuccBlocks()) { // for all succ of n
					if (Misc.isCFGLeafNode(succ)) { // if succ is a leaf node
						bw.write("\n\tnode" + info.hashCode() + " -> node" + succ.getInfo().hashCode()
								+ "[weight=8, tailport=s, headport=n];"); // Add an edge between n and succ
					} else if (Misc.isCFGNonLeafNode(succ)) { // if succ is a non-leaf node
						bw.write("\n\tnode" + info.hashCode() + " -> node"
								+ succ.getInfo().getCFGInfo().getNestedCFG().getBegin().getInfo().hashCode()
								+ "[lhead=cluster" + succ.getInfo().hashCode()
								+ ", weight=8, tailport=s, headport=n];"); // Add an edge between n and succ using
																			// succ's begin
					}
				}
				return null;
			} else { // If n is a non-leaf CFG node
						// Create links from beginNode to its succ (Needs to be done here, as there are
						// no AST visitors for beginNode)
				for (Node succ : ncfg.getBegin().getInfo().getCFGInfo().getSuccBlocks()) { // for all succ of begin
					NestedCFG succNcfg = succ.getInfo().getCFGInfo().getNestedCFG();
					if (Misc.isCFGLeafNode(succ)) { // succ is a leaf node
						bw.write("\n\tnode" + ncfg.getBegin().getInfo().hashCode() + " -> node"
								+ succ.getInfo().hashCode() + "[tailport=s, headport=n, weight=8];"); // Add edge
																										// between begin
																										// and leaf succ
					} else if (Misc.isCFGNonLeafNode(succ)) { // succ is a non-leaf node
						bw.write("\n\tnode" + ncfg.getBegin().getInfo().hashCode() + " -> node"
								+ succNcfg.getBegin().getInfo().hashCode()
								+ "[tailport=s, headport=n, weight=8, lhead=cluster" + succ.getInfo().hashCode()
								+ "];"); // Add edge between begin and non-leaf succ using succ's begin
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This overridden method creates edges between the non-leaf node n, and its
	 * successors.
	 */
	@Override
	public Object endProcess(Node n, BufferedWriter bw) {
		try {
			if (!Misc.isCFGNode(n)) {
				return null;
			}
			NodeInfo info = n.getInfo();
			NestedCFG ncfg = info.getCFGInfo().getNestedCFG();
			if (Misc.isCFGLeafNode(n)) { // No exit-work for leaf nodes.
				return null;
			} else if (Misc.isCFGNonLeafNode(n)) {// performs following exit-works for non-leaf nodes

				// Create links of this non-leaf node with its successors
				for (Node succ : info.getCFGInfo().getSuccBlocks()) { // for all succ of n
					NestedCFG ncfgSucc = succ.getInfo().getCFGInfo().getNestedCFG();
					if (Misc.isCFGLeafNode(succ)) { // succ of n is a leaf node
						bw.write("\n\tnode" + ncfg.getEnd().getInfo().hashCode() + " -> node"
								+ succ.getInfo().hashCode() + "[tailport=s, headport=n, weight=8, ltail=cluster"
								+ n.getInfo().hashCode() + "];"); // Add edge from n to its succ using n's begin node
					} else if (Misc.isCFGNonLeafNode(succ)) {
						// Succ of this cluster is a cluster
						bw.write("\n\tnode" + ncfg.getEnd().getInfo().hashCode() + " -> node"
								+ ncfgSucc.getBegin().getInfo().hashCode() + "[tailport=s, headport=n, ltail=cluster"
								+ n.getInfo().hashCode() + ", lhead=cluster" + succ.getInfo().hashCode() + "];"); // Add
																													// edge
																													// from
																													// n
																													// to
																													// its
																													// succ,
																													// using
																													// begin's
																													// of
																													// both.
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
