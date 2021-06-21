/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo6;

import imop.ast.node.external.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.cfg.info.CompoundStatementCFGInfo;
import imop.lib.util.CellSet;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Demo6 {

	/**
	 * Driver method for Demo #6: Removing Redundant Barriers.
	 *
	 * Check if a barrier-directive is required to preserve dependencies among
	 * phases across it. If not, then delete the barrier. Steps involved are:
	 * (a) For any given barrier, obtain the set of phases that it may end, and the
	 * set of phases that may start after it.
	 * (b) For each pair of phases from the sets in the last step, see if the pair
	 * conflicts, i.e. see if there exists any conflicting accesses between two
	 * phases of the pair.
	 * (c) If no conflicts are found across a barrier, remove it from the program.
	 *
	 * *** TODO OPTIONS ***
	 *
	 * I. reads2.overlapsWith(writes1) || writes1.overlapsWith(writes2)
	 *
	 * II. csCFGInfo.removeElement(barrier);
	 *
	 * III. parConsNode.getInfo().getConnectedPhases()
	 *
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/pldi-eg/example.c", "-nru" };
		Program.parseNormalizeInput(args);
		demo6();
		DumpSnapshot.dumpRoot("final-6");
		System.out.println(Program.getRoot());
	}

	public static void demo6() {
		for (BarrierDirective barrier : Misc.getInheritedEncloseeList(Program.getRoot(), BarrierDirective.class)) {
			Set<Phase> allPhaseSet = new HashSet<>();
			for (ParallelConstruct parConsNode : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
				/*
				 * TODO T1: Replace the "new" expression with code to get all phases in
				 * "parConsNode".
				 */
				allPhaseSet.addAll((Collection<? extends Phase>) new HashSet<Phase>());
			}
			Set<Phase> phasesAbove = (Set<Phase>) barrier.getInfo().getNodePhaseInfo().getPhaseSet();
			Set<Phase> phasesBelow = new HashSet<>();
			for (Phase ph : allPhaseSet) {
				for (AbstractPhasePointable bpp : ph.getBeginPoints()) {
					if (bpp.getNodeFromInterface() == barrier) {
						phasesBelow.add(ph);
					}
				}
			}
			boolean removable = true;
			if (phasesAbove.isEmpty() || phasesBelow.isEmpty()) {
				removable = false;
			}
			outer: for (Phase phAbove : phasesAbove) {
				for (Phase phBelow : phasesBelow) {
					if (phAbove == phBelow) {
						continue;
					}
					for (Node stmt1 : phAbove.getNodeSet()) {
						for (Node stmt2 : phBelow.getNodeSet()) {
							CellSet reads1 = new CellSet(stmt1.getInfo().getSharedReads());
							CellSet reads2 = new CellSet(stmt2.getInfo().getSharedReads());
							CellSet writes1 = new CellSet(stmt1.getInfo().getSharedWrites());
							CellSet writes2 = new CellSet(stmt2.getInfo().getSharedWrites());
							/*
							 * TODO T2: Replace "false" with code to check for dependencies between "stmt1"
							 * and "stmt2".
							 */
							if (reads1.overlapsWith(writes2) || true) {
								removable = false;
								break outer;
							}
						}
					}
				}
			}
			if (removable) {
				CompoundStatement enclosingCS = (CompoundStatement) Misc.getEnclosingBlock(barrier);
				CompoundStatementCFGInfo csCFGInfo = enclosingCS.getInfo().getCFGInfo();
				System.out.println("Removing a barrier.");
				/*
				 * TODO T3: Add a one-line code to remove the "barrier" from its enclosing
				 * compound-statement.
				 */
				demo6();
			}
		}
	}

}
