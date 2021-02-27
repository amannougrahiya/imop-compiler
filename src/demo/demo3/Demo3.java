/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo3;

import imop.parser.Program;

public class Demo3 {

    public static void main(String[] args) {
        args = new String[]{"-f", "runner/cgo-eg/example.c", "-nru"};
        Program.parseNormalizeInput(args);

        //		TODO: for (FunctionDefinition func : Program.getRoot().getInfo().ALLFUNC()) {
        //			TODO: for (CallStatement callStmt : Misc.ENCLOSEE(func, CallStatement.class)) {
        //				System.out.println(callStmt);
        //			}
        //			TODO: System.out.println(func.getInfo().CALLERS());
        //			for (CallStatement callStmt : Misc.getInheritedEnclosee(func, CallStatement.class)) {
        //				TODO: System.out.println(callStmt.getInfo().DEFS());
        //				TODO: System.out.print(callStmt.PRE.ARGS());
        //			}
        //			TODO: System.out.println("Recursive?" + (func.getInfo().REC() ? "Yes" : "No"));
        //		}
    }

}
