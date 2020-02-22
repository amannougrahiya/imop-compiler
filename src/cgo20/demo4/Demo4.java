/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package cgo20.demo4;

import imop.ast.node.external.FunctionDefinition;
import imop.ast.node.internal.CallStatement;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo4 {

	public static void main(String[] args) {
		Program.parseNormalizeInput(args);
		for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			/*
			 * TODO: Print the names and types of symbols declared in func.
			 */
			for (CallStatement callStmt : Misc.getInheritedEnclosee(func, CallStatement.class)) {
				/*
				 * TODO: Print the scopes for each argument passed to callStmt.
				 */
			}
		}
	}

}
