/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo3;

import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.util.CellSet;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo3 {

	/**
	 * Driver method for Demo #3: Detecting Data-Dependencies
	 *
	 * Write a pass that prints the kind of data-dependencies (RAW, WAR, or WAW), if
	 * any, that given two statements (with labels “l1” and “l2”) may have.
	 *
	 * *** TODO OPTIONS ***
	 *
	 * I. reads2.overlapsWith(writes1) || writes1.overlapsWith(writes2)
	 *
	 * II. new CellSet(stmt1.getInfo().getReads())
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/pldi-eg/example.i", "-nru" };
		Program.parseNormalizeInput(args);
		for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			Statement stmt1 = func.getInfo().getStatementWithLabel("l1");
			Statement stmt2 = func.getInfo().getStatementWithLabel("l2");
			if (stmt1 != null && stmt2 != null) {
				Demo3.demo3(stmt1, stmt2);
			}
		}
	}

	public static boolean demo3(Statement stmt1, Statement stmt2) {
		/*
		 * TODO T1: Replace "null" with code to obtain the set of cells that may be read
		 * by stmt1.
		 */
		CellSet reads1 = null;
		CellSet reads2 = new CellSet(stmt2.getInfo().getReads());
		CellSet writes1 = new CellSet(stmt1.getInfo().getWrites());
		CellSet writes2 = new CellSet(stmt2.getInfo().getWrites());
		/*
		 * TODO T2: Replace the "false" expression with code to check for conflicting
		 * accesses.
		 */
		if (reads1.overlapsWith(writes2) || false) {
			System.out.println("Conflict detected between " + stmt1 + " and " + stmt2);
			return true;
		}
		return false;
	}
}
