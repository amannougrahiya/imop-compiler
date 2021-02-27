/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.backup.demo8;

import imop.ast.node.external.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.List;

public class Demo8 {

	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.c", "-nru" };
		Program.parseNormalizeInput(args);
		int max = 0;
		for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			/*
			 * Print the number of static phases in every
			 * parallel-construct.
			 */
			List<Phase> phaseList = (List<Phase>) parCons.getInfo().getConnectedPhases();
			System.out.println(phaseList.size());
			/*
			 * Print the highest number of statements in any static phase in the
			 * system.
			 */
			for (Phase ph : phaseList) {
				if (ph.getNodeSet().size() > max) {
					max = ph.getNodeSet().size();
				}
			}
		}
		System.out.println("Highest number of statements within a phase has been: " + max);

		/*
		 * Print the set of all those statements that may run in parallel
		 * with the given statement.
		 */
		for (ExpressionStatement expStmt : Misc.getInheritedEnclosee(Program.getRoot(), ExpressionStatement.class)) {
			System.out.println("Statements that may run in parallel with the statement " + expStmt + ": ");
			for (AbstractPhase<?, ?> ph : expStmt.getInfo().getNodePhaseInfo().getPhaseSet()) {
				System.out.println(ph.getNodeSet());
			}
		}
		System.exit(0);
	}

}
