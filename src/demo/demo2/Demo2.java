/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo2;

import java.util.HashSet;

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
	 * I. callStmt.getInfo().getCalledDefinitions()
	 *
	 * II. System.out.println(func.getInfo().getFunctionName() + " is" +
	 * (func.getInfo().isRecursive() ? "" :" not" + " recursive.");
	 *
	 * III. Misc.getExactEnclosee(func, CallStatement.class)
	 *
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/pldi-eg/example.i", "-nru" };
		Program.parseNormalizeInput(args);
		demo2();
	}

	public static void demo2() {
		for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			/*
			 * TODO T1: Replace the "new" expression with code to obtain all
			 * call-statements in "func".
			 */
			for (CallStatement callStmt : new HashSet<CallStatement>()) {
				System.out.println(callStmt);

				/*
				 * TODO T2: Replace the "new" expression with code to obtain target functions of
				 * "callStmt".
				 */
				for (FunctionDefinition targetFunc : new HashSet<FunctionDefinition>()) {
					System.out.println(targetFunc);
				}

			}
			/*
			 * TODO T3: Check if "func" is a recursive method.
			 */
		}
	}

}
