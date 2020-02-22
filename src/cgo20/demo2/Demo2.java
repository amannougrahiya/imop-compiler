/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package cgo20.demo2;

import imop.parser.Program;

public class Demo2 {

	public static void main(String[] args) {
		args = new String[]{"-f", "runner/cgo-eg/example.c", "-nru"}; 
		Program.parseNormalizeInput(args);
		/*
		 * TODO: Code here for 2.B. Print body and predicate of each
		 * while-statement in the program.
		 */

		/*
		 * TODO: Code here for 2.C. Print successors of those if-statements
		 * which do not have an else-body.
		 */

	}

}
