/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo2;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo2 {

	/**
	 * Driver method for Demo #2: Working with Call Graphs.
	 *
	 * (A) Print all call-sites present lexically within a given function.
	 * (B) For a given call-statement, print its target function(s).
	 * (C) Test whether a given method is recursive.
	 *
	 * *** TODO OPTIONS ***
	 *
	 * System.out.println(callStmt.getInfo().getCalledDefinitions());
	 * System.out.print(callStmt.getPreCallNode().getArgumentList());
	 *
	 * Misc.getInheritedEnclosee(func, CallStatement.class)
	 * System.out.println("Recursive?" + (func.getInfo().isRecursive() ? "Yes" : *
	 * "No"));
	 *
	 * System.out.println(func.getInfo().getCallersOfThis());
	 *
	 * Program.getRoot().getInfo().getAllFunctionDefinitions()
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/pldi-eg/example.i", "-nru" };
		/*
		 * Print all call-sites present lexically within a given function.
		 * For a given call-statement, print its target function(s).
		 * Test whether a given method is recursive.
		 */
		Program.parseNormalizeInput(args);

		// for (FunctionDefinition func :
		// TODO T1
		// ) {
		// for (CallStatement callStmt :
		// TODO T2
		// ) {
		// System.out.println(callStmt);
		// }
		// TODO T3
		// for (CallStatement callStmt : Misc.getInheritedEnclosee(func,
		// CallStatement.class)) {
		// TODO T4
		// }
		// TODO T5
		// }
		DumpSnapshot.dumpRoot("final-2");
	}

}
