/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop;

import imop.ast.node.external.*;
import imop.parser.FrontEnd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ParseCheckMain {

	public static TranslationUnit root;

	public static void Main(String[] args) throws FileNotFoundException {
		List<File> notParsedList = new ArrayList<>();
		File dir = new File("/Users/aman/Documents/gcc.i");
		File[] progRootList = dir.listFiles();
		int total = 0, parsed = 0;
		if (progRootList != null) {
			for (File prog : progRootList) {
				String name = prog.getName();
				if (!name.endsWith(".i")) {
					continue;
				}
				System.out.println("Name: " + prog.getName());
				root = FrontEnd.parseAlone(new FileInputStream(prog.getAbsolutePath()), TranslationUnit.class);
				if (root != null) {
					System.out.println("Success.");
					parsed++;
				} else {
					notParsedList.add(prog);
				}
				total++;

			}
		}
		System.out.println("List of files not parsed: ");
		for (File f : notParsedList) {
			System.out.println("\t" + f.getAbsolutePath());
		}
		System.out.println("Successfully parsed " + parsed + " out of " + total + " testcases.");
	}

}
