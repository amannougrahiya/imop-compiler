/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package cgo20.demo5;

import imop.ast.node.external.ExpressionStatement;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo5 {

	public static void main(String[] args) {
		Program.parseNormalizeInput(args);
		for (ExpressionStatement expStmt : Misc.getInheritedEnclosee(Program.getRoot(), ExpressionStatement.class)) {
			/*
			 * TODO: Write a pass that prints the set of cells (read and/or
			 * written) in expStmt.
			 */
		}
		/*
		 * TODO: Write a pass that prints all those expression-statements which
		 * may
		 * write to a variable with given name.
		 */

		for (ExpressionStatement expStmt1 : Misc.getInheritedEnclosee(Program.getRoot(), ExpressionStatement.class)) {
			for (ExpressionStatement expStmt2 : Misc.getInheritedEnclosee(Program.getRoot(),
					ExpressionStatement.class)) {
				/*
				 * TODO: Write a pass that prints the kind of data-dependences (RAW,
				 * WAR, or WAW), if any, that expStmt1 may have with expStmt2.
				 */
			}
		}
	}

}
