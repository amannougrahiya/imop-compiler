/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package cgo20.demo6;

import imop.ast.node.external.DoStatement;
import imop.ast.node.external.WhileStatement;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo6 {

	public static void main(String[] args) {
		Program.parseNormalizeInput(args);
		for (WhileStatement whileStmt : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			/*
			 * TODO: Write a pass to perform loop unrolling for while loops.
			 */
		}
		for (DoStatement doStmt : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			/*
			 * Write a pass that translates a do-while loop to a while loop.
			 * (Assume that no jump statements exist in the body.)
			 */
		}
	}

}
