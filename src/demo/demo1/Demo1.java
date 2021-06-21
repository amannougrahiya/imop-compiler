/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo1;

import java.util.HashSet;

import imop.ast.node.external.*;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo1 {

	/**
	 * Driver method for Demo #1: Working with the AST and Info Objects.
	 *
	 * (A) Parse a sample program using IMOP.
	 * (B) From the generated AST, print the program to a file/terminal.
	 * (C) Invoke loop unrolling on the given while-statement, and print.
	 *
	 * *** TODO OPTIONS ***
	 * (For all these demos, we need to use one of these options for each "TODO".)
	 *
	 * I. whileStmt.getInfo().unrollLoop(1);
	 *
	 * II. DumpSnapshot.dumpRoot("final-1"); System.out.println(Program.getRoot());
	 *
	 * III. Program.parseNormalizeInput(args);
	 *
	 * IV. Misc.getExactEnclosee(Program.getRoot(), WhileStatement.class))
	 *
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/pldi-eg/example.i", "-nru" };
		/*
		 * TODO T1: Parse the program.
		 * (One line code, to be selected from the given TODO options in the comment of
		 * main.)
		 */

		/*
		 * TODO T2: Print the program from its AST to a file/terminal.
		 * (Two lines, one for printing to a file, and another to print to the
		 * terminal.)
		 */

		demo1();
	}

	public static void demo1() {
		/*
		 * TODO T3: Iterate over all while statements in the program.
		 * (Replace the "new" expression below.)
		 */
		for (WhileStatement whileStmt : new HashSet<WhileStatement>()) {
			// TODO T4: Invoke the loop unrolling code for while statement.

			System.out.println(whileStmt);
		}
	}

}
