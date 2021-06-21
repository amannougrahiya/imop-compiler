/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo4;

import java.util.HashSet;

import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.transform.updater.InsertImmediatePredecessor;
import imop.lib.util.DumpSnapshot;
import imop.parser.FrontEnd;
import imop.parser.Program;

public class Demo4 {

	/**
	 * Driver method for Demo #4: Using Higher-Level CFG Transformations.
	 *
	 * Write barrier: Write a pass that instruments a program such that immediately
	 * before write to a scalar variable thisVar at runtime, a notification is
	 * displayed. Steps involved would be:
	 * (a) Detect all those leaf CFG nodes that may write to thisVar.
	 * (b) Create a notification message as a printf() statement.
	 * (c) Insert the newly created statement immediately before the detected nodes.
	 *
	 *
	 * *** TODO OPTIONS ***
	 *
	 * I. FrontEnd.parseAndNormalize(newStr, Statement.class)
	 *
	 * II. InsertImmediatePredecessor.insert(node, newStmt);
	 *
	 * III. func.getInfo().getCFGInfo().getLexicalCFGLeafContents()
	 *
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/pldi-eg/example.i", "-nru" };
		Program.parseNormalizeInput(args);
		demo4();
		DumpSnapshot.dumpRoot("final-4");
		System.out.println(Program.getRoot());
	}

	public static void demo4() {
		for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			/*
			 * TODO T1: Replace the "new" expression with code to obtain all leaf CFG nodes
			 * in "func".
			 */
			for (Node node : new HashSet<Node>()) {
				for (Cell c : node.getInfo().getWrites()) {
					if (!(c instanceof Symbol)) {
						continue;
					}
					Symbol sym = (Symbol) c;
					if (!sym.getName().equals("thisVar")) {
						continue;
					}
					String newStr = "printf(\"About to write to thisVar.\");";
					/*
					 * TODO T2: Replace "null" with code that parses "newStr" as a statement.
					 */
					Statement newStmt = null;
					// TODO T3: Add a line of code to insert "newStmt" immediately before "node".
				}
			}
		}
	}

}
