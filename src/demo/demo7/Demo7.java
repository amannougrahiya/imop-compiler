/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo7;

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
        //		for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
        //			TODO: for (Node node : func.getInfo().getCFGInfo().LEXICAL-LEAF()) {
        //				for (Cell c : node.getInfo().getWrites()) {
        //					if (!(c instanceof Symbol)) {
        //						continue;
        //					}
        //					Symbol sym = (Symbol) c;
        //					if (!sym.getName().equals("thisVar")) {
        //						continue;
        //					}
        //					String newStr = "printf(\"About to write to thisVar.\");";
        //					TODO: Statement newStmt = TODO;
        //					TODO: INSERTPRED.INSERT();
        //				}
        //			}
        //		}
        System.out.println(Program.getRoot());
    }

}
