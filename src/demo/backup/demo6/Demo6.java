/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.backup.demo6;

import imop.ast.node.external.*;
import imop.lib.transform.updater.NodeReplacer;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;
import imop.parser.Program;

public class Demo6 {

	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.c", "-nru" };
		Program.parseNormalizeInput(args);
		demo6A();
		demo6B();
	}

	public static void demo6A() {
		for (WhileStatement whileStmt : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			String whileBody = whileStmt.getInfo().getCFGInfo().getBody().toString();
			String whilePredicate = whileStmt.getInfo().getCFGInfo().getPredicate().toString();
			String newWhile = "while (" + whilePredicate + ") { " + whileBody + " if (! (" + whilePredicate
					+ ")) {break;}" + whileBody + "}";
			System.out.println(newWhile);
			Statement newWhileStmt = FrontEnd.parseAndNormalize(newWhile, Statement.class);
			NodeReplacer.replaceNodes(whileStmt, newWhileStmt);
		}
		Program.getRoot().getInfo().removeExtraScopes();
		System.out.println(Program.getRoot());
	}

	public static void demo6B() {
		DumpSnapshot.dumpRoot("unrolledWhile");
		for (DoStatement doStmt : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			String doBody = doStmt.getInfo().getCFGInfo().getBody().toString();
			String doPredicate = doStmt.getInfo().getCFGInfo().getPredicate().toString();
			String whileString = "{" + doBody + "while (" + doPredicate + ") " + doBody + "}";
			System.out.println(whileString);
			Statement whileStmt = FrontEnd.parseAndNormalize(whileString, Statement.class);
			NodeReplacer.replaceNodes(doStmt, whileStmt);
		}
		Program.getRoot().getInfo().removeExtraScopes();
		DumpSnapshot.dumpRoot("doToWhile");
		System.out.println(Program.getRoot());
	}

}
