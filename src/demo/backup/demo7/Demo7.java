/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.backup.demo7;

import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.transform.updater.InsertImmediatePredecessor;
import imop.parser.FrontEnd;
import imop.parser.Program;

public class Demo7 {

	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.c", "-nru" };
		Program.parseNormalizeInput(args);
		/*
		 * Write barrier: Write a pass that instruments a program such that
		 * immediately before write to a scalar variable thisVar at runtime, a
		 * notification is displayed.
		 */
		for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			for (Node node : func.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
				for (Cell c : node.getInfo().getWrites()) {
					if (!(c instanceof Symbol)) {
						continue;
					}
					Symbol sym = (Symbol) c;
					if (!sym.getName().equals("thisVar")) {
						continue;
					}
					String newStr = "printf(\"About to write to thisVar.\");";
					Statement newStmt = FrontEnd.parseAndNormalize(newStr, Statement.class);
					InsertImmediatePredecessor.insert(node, newStmt);
				}
			}
		}
		System.out.println(Program.getRoot());
	}

}
