/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo2;

import imop.ast.node.external.*;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo2 {

	/**
	 * Driver method for Demo #2.
	 * TODO OPTIONS:
	 * 1. System.out.println(whileStmt.getInfo().getCFGInfo().getBody());
	 * 2. Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)
	 * 3. System.out.println(ifStmt.getInfo().getCFGInfo().getSuccessors());
	 * 4. !ifStmt.getInfo().getCFGInfo().hasElseBody()
	 * 5. System.out.println(whileStmt.getInfo().getCFGInfo().getPredicate());
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.c", "-nru" };
		Program.parseNormalizeInput(args);

		// for (WhileStatement whileStmt :
		// TODO T1
		// ) {
		// TODO T2
		// TODO T3
		// }
		// for (IfStatement ifStmt : Misc.getInheritedEnclosee(Program.getRoot(),
		// IfStatement.class)) {
		// if (
		// TODO T4
		// ) {
		// TODO T5
		// }
		// }
	}

}
