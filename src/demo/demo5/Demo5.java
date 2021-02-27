/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo5;

import imop.ast.node.external.*;
import imop.parser.Program;

public class Demo5 {

    public static void main(String[] args) {
        args = new String[]{"-f", "runner/cgo-eg/example.c", "-nru"};
        Program.parseNormalizeInput(args);
        demo5ab();
    }

    public static void demo5ab() {
        //        for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
        //            TODO: Statement stmt = func.getInfo().LAB("thisStmt");
        //            if (stmt == null) {
        //                continue;
        //            }
        //            TODO: for (Cell c : stmt.getInfo().ACCESS()) {
        //            TODO: if (!(c instanceof SYM)) {
        //                    continue;
        //                }
        //            TODO: System.out.println(((Symbol) c).NAME());
        //            }
        //        }
        //        for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
        //            for (ExpressionStatement node : Misc.getInheritedEnclosee(func, ExpressionStatement.class)) {
        //                TODO: for (Cell c : node.getInfo().WRITE()) {
        //                    if (!(c instanceof Symbol)) {
        //                        continue;
        //                    }
        //                    Symbol sym = (Symbol) c;
        //                    TODO: if (sym.NAME().equals("thisVar")) {
        //                        System.out.println(node);
        //                    }
        //                }
        //            }
        //        }
        //        for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
        //            TODO: Statement stmt1 = func.getInfo().LABEL("l1");
        //            TODO: Statement stmt2 = func.getInfo().LABEL("l2");
        //            if (stmt1 != null && stmt2 != null) {
        //                Demo5.demo5c(stmt1, stmt2);
        //            }
        //        }
        //
    }

    public static boolean demo5c(Statement stmt1, Statement stmt2) {
        //        TODO: CellSet reads1 = new CellSet(stmt1.getInfo().TOOBY());
        //        TODO: CellSet reads2 = new CellSet(stmt2.getInfo().DOOBY());
        //        TODO: CellSet writes1 = new CellSet(stmt1.getInfo().DOO());
        //        TODO: CellSet writes2 = new CellSet(stmt2.getInfo().TODO());
        //        if (reads1.overlapsWith(writes2) || TODO || TODO-AGAIN!) {
        //            System.out.println("Conflict detected between " + stmt1 + " and " + stmt2);
        //            return true;
        //        }
        return false;
    }
}
