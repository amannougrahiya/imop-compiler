/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package cgo20.demo9;

import imop.parser.Program;

public class Demo9 {

	public static void main(String[] args) {
		args = new String[]{"-f", "runner/cgo-eg/example.c", "-nru"}; 
		Program.parseNormalizeInput(args);
		/*
		 * Check if a barrier-directive is required to preserve
		 * dependences among
		 * phases across it. If not, then delete the barrier.
		 * For any given barrier, get the set of phases that it may end, and the
		 * set of phases that may start after it.
		 * For each pair of phases from the sets in the last step, see if the
		 * pair conflicts, i.e. see if there exists any conflicting accesses
		 * between two phases of the pair.
		 * If no conflicts are found across a barrier, remove it from the
		 * program.
		 */
		demo9();
	}

	public static void demo9() {
		// TODO: Code here.
	}

}
