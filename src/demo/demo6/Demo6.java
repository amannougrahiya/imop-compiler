/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo6;

import imop.parser.Program;

public class Demo6 {

    public static void main(String[] args) {
        args = new String[]{"-f", "runner/cgo-eg/example.c", "-nru"};
        Program.parseNormalizeInput(args);
        demo6A();
        demo6B();
    }

    public static void demo6A() {
        //		TODO: for (WhileStatement whileStmt : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
        //			TODO: String whileBody = TODO;
        //			System.out.println(newWhile);
        //			TODO: Statement newWhileStmt = FrontEnd.TODO(newWhile, Statement.class);
        //			TODO: REP.REPLACE(whileStmt, newWhileStmt);
        //		}
        //		Program.getRoot().getInfo().removeExtraScopes();
        //		System.out.println(Program.getRoot());
    }

    public static void demo6B() {
        //		for (DoStatement doStmt : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
        //			TODO: String doBody = TODO;
        //			System.out.println(whileString);
        //			TODO: Statement whileStmt = TODO;
        //			TODO: REP.REPLACE(doStmt, whileStmt);
        //		}
        //		Program.getRoot().getInfo().removeExtraScopes();
        //		DumpSnapshot.dumpRoot("doToWhile");
        //		System.out.println(Program.getRoot());
    }

}
