/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.backup.demo2;

import imop.ast.node.external.*;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo2 {

	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.c", "-nru" };
		Program.parseNormalizeInput(args);

		for (WhileStatement whileStmt : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			System.out.println(whileStmt.getInfo().getCFGInfo().getPredicate());
			System.out.println(whileStmt.getInfo().getCFGInfo().getBody());
		}
		for (IfStatement ifStmt : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			if (!ifStmt.getInfo().getCFGInfo().hasElseBody()) {
				System.out.println(ifStmt.getInfo().getCFGInfo().getSuccessors());
			}
		}
		System.exit(0);
	}

}
