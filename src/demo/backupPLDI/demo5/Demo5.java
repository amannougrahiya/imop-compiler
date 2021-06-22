/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.backupPLDI.demo5;

import imop.ast.node.external.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Demo5 {

	/**
	 * Driver method for Demo #5: Analyzing Concurrency in OpenMP Programs.
	 *
	 * (A) Print the number of static phases in every parallel-construct.
	 * (B) Print the highest number of statements in any static phase in the system.
	 * (C) Print the set of all those CFG leaf nodes that may run in parallel with
	 * the given expression statement.
	 *
	 * *** TODO OPTIONS ***
	 * I. inParallel.addAll(ph.getNodeSet());
	 *
	 * II. ph.getNodeSet().size()
	 *
	 * III. new HashSet<>(parCons.getInfo().getConnectedPhases()).size()
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/pldi-eg/example.i", "-nru" };
		Program.parseNormalizeInput(args);
		demo5();
	}

	public static void demo5() {
		int max = 0;
		for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			/*
			 * TODO T1: Replace "0" with code to obtain the number of static phases in
			 * "parCons".
			 */
			int count = 0;
			System.out.println("Number of static phases: " + count);
			for (AbstractPhase<?, ?> absPh : new HashSet<>(parCons.getInfo().getConnectedPhases())) {
				Phase ph = (Phase) absPh;
				/*
				 * TODO T2: Replace "0" with code to obtain the number of statements in a phase.
				 */
				int stmtCount = 0;
				if (max < stmtCount) {
					max = stmtCount;
				}
			}
		}
		System.out.println("Highest number of statements within a phase has been: " + max);

		for (ExpressionStatement expStmt : Misc.getInheritedEnclosee(Program.getRoot(), ExpressionStatement.class)) {
			Set<Node> inParallel = new HashSet<>();
			for (AbstractPhase<?, ?> absPh : expStmt.getInfo().getNodePhaseInfo().getPhaseSet()) {
				Phase ph = (Phase) absPh;
				/*
				 * TODO T3: Add a line of code to collect all nodes that may run in parallel
				 * with "expStmt" in phase "ph".
				 */
			}
			if (!inParallel.isEmpty()) {
				System.out.println("Statements that may run in parallel with the statement " + expStmt + ": ");
				System.out.println(inParallel);
			}
		}
		System.exit(0);
	}

}
