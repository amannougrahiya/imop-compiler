/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package cgo20.demo8;

import imop.ast.node.external.ParallelConstruct;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo8 {

	public static void main(String[] args) {
		args = new String[]{"-f", "runner/cgo-eg/example.c", "-nru"}; 
		Program.parseNormalizeInput(args);
		for (ParallelConstruct parCons : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			/*
			 * TODO: Print the number of static phases in every
			 * parallel-construct.
			 */
		}
		/*
		 * TODO: Print the highest number of statements in any static phase in the
		 * system.
		 */

		/*
		 * TODO: Print the set of all those statements that may run in parallel
		 * with the given statement.
		 */
	}

}
