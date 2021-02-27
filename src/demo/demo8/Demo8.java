/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo8;

import imop.ast.node.external.*;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo8 {

    public static void main(String[] args) {
        args = new String[]{"-f", "runner/cgo-eg/example.c", "-nru"};
        Program.parseNormalizeInput(args);
        int max = 0;
        for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
            //			/*
            //			 * Print the number of static phases in every
            //			 * parallel-construct.
            //			 */
            //			TODO: List<Phase> phaseList = (List<Phase>) parCons.getInfo().CONNECTED-PH();
            //			System.out.println(phaseList.size());
            //			/*
            //			 * Print the highest number of statements in any static phase in the
            //			 * system.
            //			 */
            //			for (Phase ph : phaseList) {
            //				TODO: if (ph.NODES.size() > max) {
            //					TODO: max = ph.NODES.size();
            //				}
            //			}
        }
        System.out.println("Highest number of statements within a phase has been: " + max);

        /*
         * Print the set of all those statements that may run in parallel
         * with the given statement.
         */
        for (ExpressionStatement expStmt : Misc.getInheritedEnclosee(Program.getRoot(), ExpressionStatement.class)) {
            System.out.println("Statements that may run in parallel with the statement " + expStmt + ": ");
            //            for (AbstractPhase<?, ?> ph : expStmt.getInfo().TODO.TODO) {
            //                System.out.println(ph.NODES());
            //            }
        }
        System.exit(0);
    }

}
