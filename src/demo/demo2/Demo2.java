/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo2;

import imop.parser.Program;

public class Demo2 {

    public static void main(String[] args) {
        args = new String[]{"-f", "runner/cgo-eg/example.c", "-nru"};
        Program.parseNormalizeInput(args);

        //		TODO: for (WhileStatement whileStmt : Misc.ENCL(Program.getRoot(), WhileStatement.class)) {
        //			TODO: System.out.println(whileStmt.getInfo().CFG.PRED());
        //			TODO: System.out.println(whileStmt.getInfo().CFG.BODY());
        //		}
        //		for (IfStatement ifStmt : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
        //			TODO: if (!ifStmt.getInfo().CFG.ELSE?()) {
        //			TODO: System.out.println(ifStmt.getInfo().CFG.SUCC());
        //			}
        //		}
    }

}
