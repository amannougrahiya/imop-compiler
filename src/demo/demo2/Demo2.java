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
import imop.ast.node.internal.*;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo3 {

	/**
	 * Driver method for Demo #3.
	 * TODO OPTIONS:
	 * 1. System.out.println(callStmt.getInfo().getCalledDefinitions());
	 * System.out.print(callStmt.getPreCallNode().getArgumentList());
	 * 2. Misc.getInheritedEnclosee(func, CallStatement.class)
	 * 3. System.out.println("Recursive?" + (func.getInfo().isRecursive() ? "Yes" :
	 * "No"));
	 * 4. System.out.println(func.getInfo().getCallersOfThis());
	 * 5. Program.getRoot().getInfo().getAllFunctionDefinitions()
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.c", "-nru" };
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
	}

}
