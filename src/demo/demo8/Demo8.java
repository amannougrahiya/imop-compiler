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
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.List;

public class Demo8 {

	/**
	 * Driver method for Demo #8.
	 * TODO OPTIONS:
	 * 1. System.out.println(ph.getNodeSet());
	 * 2. expStmt.getInfo().getNodePhaseInfo().getPhaseSet()
	 * 3. max = ph.getNodeSet().size();
	 * 4. List<Phase> phaseList = (List<Phase>)
	 * parCons.getInfo().getConnectedPhases();
	 * 5. ph.getNodeSet().size() > max
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.c", "-nru" };
		Program.parseNormalizeInput(args);
		int max = 0;
		for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			// /*
			// * Print the number of static phases in every
			// * parallel-construct.
			// */
			// TODO T1
			// System.out.println(phaseList.size());
			// /*
			// * Print the highest number of statements in any static phase in the
			// * system.
			// */
			// for (Phase ph : phaseList) {
			// if (
			// TODO T2
			// ) {
			// TODO T3
			// }
			// }
		}
		System.out.println("Highest number of statements within a phase has been: " + max);

		/*
		 * Print the set of all those statements that may run in parallel
		 * with the given statement.
		 */
		for (ExpressionStatement expStmt : Misc.getInheritedEnclosee(Program.getRoot(), ExpressionStatement.class)) {
			System.out.println("Statements that may run in parallel with the statement " + expStmt + ": ");
			// for (AbstractPhase<?, ?> ph :
			// TODO T4
			// ) {
			// TODO T5
			// }
		}
		System.exit(0);
	}

}
