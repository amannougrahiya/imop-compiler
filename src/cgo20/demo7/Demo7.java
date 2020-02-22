/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package cgo20.demo7;

import imop.parser.Program;

public class Demo7 {

	public static void main(String[] args) {
		args = new String[]{"-f", "runner/cgo-eg/example.c", "-nru"}; 
		Program.parseNormalizeInput(args);
		/*
		 * Write barrier: Write a pass that instruments a program such that
		 * immediately before write to a scalar variable thisVar at runtime, a
		 * notification is displayed.
		 */
		
		/*
		 * TODO: Iterate over, and detect all those leaf CFG nodes that may write to thisVar.
		 * TODO: Create a notification message as a printf() statement.
		 * TODO: Insert the newly created statement immediately before the detected
		 * node.
		 */
	}

}
