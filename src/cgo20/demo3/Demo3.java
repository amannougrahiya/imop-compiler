/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package cgo20.demo3;

import imop.ast.node.external.FunctionDefinition;
import imop.ast.node.internal.CallStatement;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo3 {

	public static void main(String[] args) {
		Program.parseNormalizeInput(args);
		for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			/*
			 * TODO: Print all call-sites present lexically present within
			 * "func".
			 */
			/*
			 * TODO: Print all call-sites in the program that may have "func"
			 */
			for (CallStatement callStmt : Misc.getInheritedEnclosee(func, CallStatement.class)) {
				/*
				 * TODO: For callStmt:
				 * (i) print its target function(s), and
				 * (ii) print all its arguments.
				 */
			}
			/*
			 * TODO: Check if "func" is a recursive method.
			 */
		}
	}

}
