/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo4;

import imop.parser.Program;

public class Demo4 {

    public static void main(String[] args) {
        args = new String[]{"-f", "runner/cgo-eg/example.c", "-nru"};
        Program.parseNormalizeInput(args);

        //		for (FunctionDefinition func : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
        //			TODO: for (Symbol sym : func.getInfo().TABLE.values()) {
        //			TODO:	System.out.println(sym.NAME() + " has type " + sym.TYPE());
        //			}
        //		}
        //		for (CallStatement callStmt : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
        //			TODO: for (SimplePrimaryExpression spe : callStmt.PRE.ARG) {
        //			TODO:	if (!spe.isAnIdentifier()) {
        //					continue;
        //				}
        //			TODO: System.out.println(Misc.SYM(X, Y).SCOPE());
        //			}
        //		}
    }

}
