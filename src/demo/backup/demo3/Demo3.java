/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.backup.demo3;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo3 {

	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.c", "-nru" };
		Program.parseNormalizeInput(args);

		for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			for (CallStatement callStmt : Misc.getInheritedEnclosee(func, CallStatement.class)) {
				System.out.println(callStmt);
			}
			System.out.println(func.getInfo().getCallersOfThis());
			for (CallStatement callStmt : Misc.getInheritedEnclosee(func, CallStatement.class)) {
				System.out.println(callStmt.getInfo().getCalledDefinitions());
				System.out.print(callStmt.getPreCallNode().getArgumentList());
			}
			System.out.println("Recursive?" + (func.getInfo().isRecursive() ? "Yes" : "No"));
		}
		System.exit(0);

	}

}
