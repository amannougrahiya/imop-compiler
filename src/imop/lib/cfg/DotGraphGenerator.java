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
import imop.lib.util.DumpSnapshot;

import java.io.BufferedWriter;

/**
 * This class generates the dot graph text for the CFGs created.
 * This text is written in the file returned by Misc::getGraphFileWriter()
 * 
 * @author aman
 *
 */
public class DotGraphGenerator {

	private String fileName;

	BufferedWriter bw;
	TypeOfCFG nested;
	Node n;

	public DotGraphGenerator(Node at, String fileName) {
		this.n = at;
		this.fileName = fileName;
	}

	public void create(TypeOfCFG nested) {
		bw = DumpSnapshot.getGraphFileWriter(nested, fileName); // Obtain the BufferedWriter which will be used to write
																// the dot data.
		switch (nested) {
		case NESTED:
			generateNestedDotGraph();
			break;
		case SIMPLE:
			generateSimpleDotGraph();
			break;
		}
	}

	/**
	 * This method generates a simple non-nested CFG starting from the given
	 * input.
	 * 
	 * @param n
	 */
	private void generateSimpleDotGraph() {
		try {
			bw.write("\ndigraph graph" + n.hashCode() + " {");
			bw.write("\n\tedge[color=blue];");
			n.accept(new SimpleNodeEdgeWriter(), bw);
			bw.write("\n}");
			bw.close();
			System.err.println("\tTo create png of the non-nested graph, run: dot -Tpng " + fileName
					+ "nestedDotGraph.gv -o " + fileName + "nestedDotGraph.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method generates the dot graph for all the functions and nodes
	 * present
	 * within the Node n.
	 * Name of the graph is "graph" + node.hashCode()
	 * 
	 * @param n
	 */
	private void generateNestedDotGraph() {
		try {
			bw.write("digraph graph" + n.hashCode() + " {");
			// bw.write("\tratio=1.0;");
			bw.write("\n\tcompound=true;");
			bw.write("\n\tedge[color=blue];");

			// Every node executes initProcess(), calls visits on the AST children, and then
			// executes endProcess().
			n.accept(new DotNodeWriter(), bw); // Write the information about all the nodes and subgraphs.
			n.accept(new DotEdgeWriter(), bw); // Writer the information about all the edges.

			bw.write("\n}\n");
			bw.close();
			System.err.println("\tTo create png of the nested graph, run: dot -Tpng " + fileName
					+ "nestedDotGraph.gv -o " + fileName + "nestedDotGraph.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
