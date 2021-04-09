/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo1;

import imop.ast.node.external.*;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo1 {

	/**
	 * Driver method for Demo #1.
	 * TODO OPTIONS:
	 * 1. Program.parseNormalizeInput(args); System.out.println(Program.getRoot());
	 * 2. whileStmt.getInfo().unrollLoop(1);
	 * 3. Program.getRoot().getInfo().getAllFunctionDefinitions()
	 * 4. Statement stmt = func.getInfo().getStatementWithLabel("test");
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.c", "-nru" };
		// TODO T1

		// Get statement with label "test"
		// for (FunctionDefinition func :
		// TODO T2
		// ) {
		// TODO T3
		// if (stmt == null) {
		// System.out.println("Not found!");
		// } else {
		// System.out.println(stmt);
		// }
		// for (WhileStatement whileStmt : Misc.getInheritedEnclosee(func,
		// WhileStatement.class)) {
		// TODO T4
		// System.out.println(whileStmt);
		// }
		// }
	}

}
