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
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo5 {

    /**
     * Driver method for Demo #5.
     * TODO OPTIONS:
     *      1. CellSet writes1 = new CellSet(stmt1.getInfo().getWrites());
     *      2. Statement stmt = func.getInfo().getStatementWithLabel("thisStmt");
     *      3. stmt.getInfo().getAccesses()
     *      4. sym.getName().equals("thisVar")
     *      5. Statement stmt1 = func.getInfo().getStatementWithLabel("l1"); Statement stmt2 = func.getInfo().getStatementWithLabel("l2");
     *      6. node.getInfo().getWrites())
     *      7. System.out.println(((Symbol) c).getName());
     *      8. CellSet reads1 = new CellSet(stmt1.getInfo().getReads());
     *      9. reads2.overlapsWith(writes1) || writes1.overlapsWith(writes2)
     *      10. !(c instanceof Symbol)
     */
    public static void main(String[] args) {
        args = new String[]{"-f", "runner/cgo-eg/example.c", "-nru"};
        Program.parseNormalizeInput(args);
        demo5ab();
    }

    public static void demo5ab() {
        //        for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
        //  TODO T1
        //            if (stmt == null) {
        //                continue;
        //            }
        //            for (Cell c :
        //  TODO T2
        //            ) {
        //              if (
        //  TODO T3
        //              ) {
        //                    continue;
        //                }
        //  TODO T4
        //            }
        //        }
        //        for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
        //            for (ExpressionStatement node : Misc.getInheritedEnclosee(func, ExpressionStatement.class)) {
        //                for (Cell c :
        //  TODO T5
        //                ) {
        //                    if (!(c instanceof Symbol)) {
        //                        continue;
        //                    }
        //                    Symbol sym = (Symbol) c;
        //                    if (
        //  TODO T6
        //                    ) {
        //                        System.out.println(node);
        //                    }
        //                }
        //            }
        //        }
        //        for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
        // TODO T7
        //            if (stmt1 != null && stmt2 != null) {
        //                Demo5.demo5c(stmt1, stmt2);
        //            }
        //        }
        //
    }

    public static boolean demo5c(Statement stmt1, Statement stmt2) {
        // TODO T8
        //        CellSet reads2 = new CellSet(stmt2.getInfo().getReads());
        // TODO T9
        //        CellSet writes2 = new CellSet(stmt2.getInfo().getWrites());
        //        if (reads1.overlapsWith(writes2) ||
        // TODO T10
        //        ) {
        //            System.out.println("Conflict detected between " + stmt1 + " and " + stmt2);
        //            return true;
        //        }
        return false;
    }
}
