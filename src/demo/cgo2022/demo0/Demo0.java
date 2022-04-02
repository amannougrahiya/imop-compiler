/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.cgo2022.demo0;

import imop.parser.Program;

public class Demo0 {

	/**
	 * Driver method for Demo #0: Testing the setup.
	 *
	 * Note 1: Simply run this program, and see if you get the "Setup successful!"
	 * message in STDOUT. This message will be preceded by other compilation
	 * messages printed to STDERR, by Program.parseNormalizeInput().
	 *
	 * Note 2: For these demo programs, we hard-code the example input
	 * (runner/cgo-eg/example.i), using the "-f" switch.
	 * We also use another switch, "-nru" to instruct IMOP to not remove unused
	 * variables/functions.
	 *
	 * Here, we provide these switches by manually writing to the array "args".
	 * In practice, such details are given as command-line arguments.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.i", "-nru" };
		Program.parseNormalizeInput(args);
		System.out.println("Setup successful!");
	}
}
