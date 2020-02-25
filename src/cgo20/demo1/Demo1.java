/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package cgo20.demo1;

import imop.ast.node.external.FunctionDefinition;
import imop.ast.node.external.WhileStatement;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo1 {

	/**
	 * Driver method for Demo #1.
	 */
	public static void main(String[] args) {
		args = new String[]{"-f", "runner/cgo-eg/example.c", "-nru"}; 
		/*
		 * (A) Invoke the correct function here for parsing the file provided
		 * via the parameter args.
		 */
		Program.parseNormalizeInput(args);

		/*
		 * (B) From the generated AST, print the program to a file/terminal.
		 */
		System.out.println(Program.getRoot());
		// TODO: Code here for (B)

		/*
		 * (C) Find and print all those statements that have a given label, say "test".
		 */
		for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			System.out.println(func.getInfo().getStatementWithLabel("test"));
		}

		/*
		 * (D) Invoke loop unrolling on the given while-statement, and print.
		 */
		for (WhileStatement whileStmt : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			demo1d(whileStmt);
			System.out.println(whileStmt);
			break;
		}
		DumpSnapshot.dumpRoot("-demo0");
	}

	public static void demo1d(WhileStatement whileStmt) {
		whileStmt.getInfo().unrollLoop(2);
		// TODO: Code here for (D)
	}

}
