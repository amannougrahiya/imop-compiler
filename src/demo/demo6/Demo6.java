/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo6;

import imop.ast.node.external.*;
import imop.lib.transform.updater.NodeReplacer;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;
import imop.parser.Program;

public class Demo6 {

	/**
	 * Driver method for Demo #6.
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.c", "-nru" };
		Program.parseNormalizeInput(args);
		demo6A();
		demo6B();
	}

	/**
	 * TODO OPTIONS:
	 * 1. NodeReplacer.replaceNodes(whileStmt, newWhileStmt);
	 * 2. Statement newWhileStmt = FrontEnd.parseAndNormalize(newWhile,
	 * Statement.class);
	 * 3. Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)
	 * 4. String whileBody = whileStmt.getInfo().getCFGInfo().getBody().toString();
	 * 5. String whilePredicate =
	 * whileStmt.getInfo().getCFGInfo().getPredicate().toString();
	 * 6. String newWhile = "while (" + whilePredicate + ") { " + whileBody + " if
	 * (! (" + whilePredicate + ")) {break;}" + whileBody + "}";
	 */
	public static void demo6A() {
		// for (WhileStatement whileStmt :
		// TODO T1
		// ) {
		// TODO T2
		// TODO T3
		// TODO T4
		// System.out.println(newWhile);
		// TODO T5
		// TODO T6
		// }
		// Program.getRoot().getInfo().removeExtraScopes();
		// System.out.println(Program.getRoot());
	}

	/**
	 * TODO OPTIONS:
	 * 1. NodeReplacer.replaceNodes(doStmt, whileStmt);
	 * 2. Statement whileStmt = FrontEnd.parseAndNormalize(whileString,
	 * Statement.class);
	 * 3. String doPredicate =
	 * doStmt.getInfo().getCFGInfo().getPredicate().toString();
	 * 4. String whileString = "{" + doBody + "while (" + doPredicate + ") " +
	 * doBody + "}";
	 * 5. String doBody = doStmt.getInfo().getCFGInfo().getBody().toString();
	 */
	public static void demo6B() {
		// for (DoStatement doStmt : Misc.getInheritedEnclosee(Program.getRoot(),
		// DoStatement.class)) {
		// TODO T1
		// TODO T2
		// TODO T3
		// System.out.println(whileString);
		// TODO T4
		// TODO T5
		// }
		// Program.getRoot().getInfo().removeExtraScopes();
		// DumpSnapshot.dumpRoot("doToWhile");
		// System.out.println(Program.getRoot());
	}

}
