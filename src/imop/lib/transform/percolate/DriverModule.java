/*
// * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.percolate;

import imop.Main;
import imop.ast.info.DataSharingAttribute;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.analysis.SVEChecker;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.SCC;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.analysis.mhp.incMHP.EndPhasePoint;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.analysis.solver.ConstraintsGenerator;
import imop.lib.analysis.solver.FieldSensitivity;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.builder.Builder;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cfg.info.WhileStatementCFGInfo;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.transform.BasicTransform;
import imop.lib.transform.CopyEliminator;
import imop.lib.transform.simplify.*;
import imop.lib.transform.updater.InsertImmediatePredecessor;
import imop.lib.transform.updater.InsertOnTheEdge;
import imop.lib.util.*;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.text.DecimalFormat;
import java.util.*;

public class DriverModule {
	public static int counter = 0;

	public static void clientAutoUpdateHomeostasis() {
		// DumpSnapshot.dumpVisibleSharedReadWrittenCells("first" +
		// Program.updateCategory);
		boolean dumpIntermediate = Program.dumpIntermediateStates;
		Program.sveSensitive = SVEDimension.SVE_INSENSITIVE; // Note: We keep SVE-sensitivity disabled for this client.
		ParallelConstructExpander.mergeParallelRegions(Program.getRoot());
		Program.getRoot().getInfo().removeUnusedElements();
		ProfileSS.insertCP(); // RCP
		AutomatedUpdater.stabilizePTAInCPModes();
		if (dumpIntermediate) {
			DumpSnapshot.dumpRoot("merged" + Program.updateCategory);
			DumpSnapshot.dumpPointsTo("merged" + Program.updateCategory);
			DumpSnapshot.dumpPhases("merged" + Program.mhpUpdateCategory);
		}
		// OLD CODE: BasicTransform.removeUnusedFunctions(Program.getRoot());
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
		} else {
			RedundantSynchronizationRemovalForYA.removeBarriers(Program.getRoot());
		}
		if (dumpIntermediate) {
			DumpSnapshot.dumpRoot("merged-rem" + Program.updateCategory);
			DumpSnapshot.dumpPointsTo("merged-rem" + Program.updateCategory);
			DumpSnapshot.dumpPhases("merged-rem" + Program.mhpUpdateCategory);
		}
		// TODO: Uncomment starting this.
		// FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		// FunctionInliner.inline(mainFunc);
		// if (dumpIntermediate) {
		// DumpSnapshot.dumpRoot("merged-rem-inlined" + Program.mhpUpdateCategory);
		// DumpSnapshot.dumpPointsTo("merged-rem-inlined" + Program.updateCategory);
		// DumpSnapshot.dumpPhases("merged-rem-inlined" + Program.mhpUpdateCategory);
		// }
		// ParallelConstructExpander.mergeParallelRegions(Program.getRoot());
		// Program.getRoot().getInfo().removeUnusedElements();
		// ProfileSS.insertCP(); // RCP
		// AutomatedUpdater.stabilizePTAInCPModes();
		// if (dumpIntermediate) {
		// DumpSnapshot.dumpRoot("merged-rem-inlined-merged" +
		// Program.mhpUpdateCategory);
		// DumpSnapshot.dumpPointsTo("merged-rem-inlined-merged" +
		// Program.updateCategory);
		// DumpSnapshot.dumpPhases("merged-rem-inlined-merged" +
		// Program.mhpUpdateCategory);
		// }
		// // Program.getRoot().getInfo().removeExtraScopes();
		// if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
		// RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
		// } else {
		// RedundantSynchronizationRemovalForYA.removeBarriers(Program.getRoot());
		// }
		// if (dumpIntermediate) {
		// DumpSnapshot.dumpRoot("merged-rem-inlined-merged-rem" +
		// Program.mhpUpdateCategory);
		// }
		// TODO: Uncomment till here.
		double totTime = (System.nanoTime() - Main.totalTime) / (1.0 * 1e9);
		double incMHPTime = 0.0;
		double incIDFATime = 0.0;
		long incMHPTriggers = 0;
		long incIDFATriggers = 0;
		long finalIncNodes = 0;
		long tarjanCount = 0;
		double sccTime = 0.0;
		System.err.println("Number of times IDFA update were triggered -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println("\t For " + analysis.getAnalysisName() + ": " + analysis.autoUpdateTriggerCounter);
			if (analysis.getAnalysisName() == AnalysisName.POINTSTO) {
				incIDFATriggers = analysis.autoUpdateTriggerCounter;
			}
		}
		System.err.println("Total number of times nodes were processed during automated IDFA update -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println("\t For " + analysis.getAnalysisName() + ": " + analysis.nodesProcessedDuringUpdate);
			if (analysis.getAnalysisName() == AnalysisName.POINTSTO) {
				finalIncNodes = analysis.nodesProcessed;
			}
		}
		System.err.println("Time spent in forward IDFA updates -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println(
					"\t For " + analysis.getAnalysisName() + ": " + analysis.flowAnalysisUpdateTimer / (1e9) + "s.");
			if (analysis.getAnalysisName() == AnalysisName.POINTSTO) {
				incIDFATime = analysis.flowAnalysisUpdateTimer / (1e9 * 1.0);
			}
		}
		// System.err.println("Time spent in SVE queries: " + SVEChecker.sveTimer / (1e9
		// * 1.0) + "s.");
		System.err.println("Time spent in phase update: " + BeginPhasePoint.stabilizationTime / (1e9 * 1.0) + "s.");
		incMHPTime = BeginPhasePoint.stabilizationTime / (1e9 * 1.0);
		System.err.println("Number of stabilizations of phase analysis: " + AutomatedUpdater.reinitMHPCounter);
		incMHPTriggers = AutomatedUpdater.reinitMHPCounter;
		System.err.println("Time spent generating SCCs: " + SCC.SCCTimer / (1e9 * 1.0) + "s.");
		sccTime = SCC.SCCTimer / (1e9 * 1.0);
		System.err.println("Total invocations of Tarjan's algorithm: " + SCC.tarjanCount);
		tarjanCount = SCC.tarjanCount;
		if (Program.fieldSensitive) {
			System.err.println("Number of field-sensitive queries: " + FieldSensitivity.counter);
			System.err.println("Time spent in field-sensitive queries: " + FieldSensitivity.timer / (1e9 * 1.0) + "s.");
		}
		// System.err.println("Time spent in inlining: " + FunctionInliner.inliningTimer
		// / (1e9 * 1.0) + "s.");
		// System.err.println("Time spent in having uni-task precision in IDFA edge
		// creation: "
		// + CFGInfo.uniPrecisionTimer / (1e9 * 1.0) + "s.");
		// System.err.println("Number of field-sensitive queries: " +
		// FieldSensitivity.counter);
		// System.err.println("Time spent in field-sensitive queries: " +
		// FieldSensitivity.timer / (1e9 * 1.0) + "s.");
		// System.err.println("Time spent in generating reverse postordering of the
		// program nodes: "
		// + TraversalOrderObtainer.orderGenerationTime / (1e9 * 1.0) + "s.");
		// if (Program.fieldSensitive) {
		// Misc.printToFile(ConstraintsGenerator.allConstraintString, Program.fileName +
		// "_z3_queries.txt");
		// }
		System.err.println("TOTAL TIME (including disk I/O time): " + totTime + "s.");
		System.err.println("This execution ran in " + Program.updateCategory + " mode for IDFA update.");
		System.err.println("Optimized a total of " + AutomatedUpdater.hasBeenOtimized + " stale markings.");
		System.err
				.println("Number of times PTA would have had to run in semi-eager mode: " + ProfileSS.flagSwitchCount);
		DumpSnapshot.printToFile(Program.getRoot(),
				(Program.fileName + "imop_output_" + Program.mhpUpdateCategory + ".i").trim());
		DumpSnapshot.dumpPointsTo("final" + Program.updateCategory);
		DumpSnapshot.dumpPhases("final" + Program.mhpUpdateCategory);
		if (dumpIntermediate) {
			DumpSnapshot.dumpNestedCFG(Program.getRoot(), "optimized" + Program.mhpUpdateCategory);
		}
		DecimalFormat df2 = Program.df2;
		System.out.println(Program.fileName + " " + Program.updateCategory + " " + df2.format(totTime) + " "
				+ df2.format(incMHPTime) + " " + df2.format(incIDFATime) + " " + incMHPTriggers + " " + incIDFATriggers
				+ " " + finalIncNodes + " " + tarjanCount + " " + df2.format(sccTime));
		/*
		 * NOTE: This commented portion is used to print all those traces from where the
		 * flushing of the abstractions has been invoked. To enable this, we also need
		 * to uncomment the call to Misc.isCalledFromMethod() in
		 * AutomatedUpdate.flushCaches().
		 *
		 * String finalStrTrace = "";
		 * for (String str : Misc.uniqueTraces) {
		 * finalStrTrace += str + "\n";
		 * }
		 * System.out.println(finalStrTrace);
		 */

		if (Program.addRelCPs) {
			System.out.println("Active change-points for points-to analysis: " + ProfileSS.ptaSet);
			System.out.println("Active change-points for CFG: " + ProfileSS.cfgSet);
			System.out.println("Active change-points for CG: " + ProfileSS.cgSet);
			System.out.println("Active change-points for inter-task edges: " + ProfileSS.iteSet);
			System.out.println("Active change-points for symbol tables: " + ProfileSS.symSet);
			System.out.println("Active change-points for label-lookups: " + ProfileSS.labSet);
			System.out.println("Active change-points for phase-queries: " + ProfileSS.phSet);
		}
		System.exit(0);
	}

	public static void clientAutoUpdateIncEPA() {
		// DumpSnapshot.dumpVisibleSharedReadWrittenCells("first" +
		// Program.updateCategory);
		// DumpSnapshot.dumpPhases("first_" + Program.concurrencyAlgorithm + "_" +
		// Program.updateCategory);
		// boolean dumpIntermediate = Program.dumpIntermediateStates;
		boolean dumpIntermediate = false;
		ParallelConstructExpander.mergeParallelRegions(Program.getRoot());
		// DriverModule.searchExample();
		// System.exit(0);

		Program.getRoot().getInfo().removeUnusedElements();
		ProfileSS.insertCP();
		if (dumpIntermediate) {
			DumpSnapshot.dumpRoot("merged" + Program.updateCategory);
			DumpSnapshot.dumpPointsTo("merged" + Program.updateCategory);
			DumpSnapshot.dumpPhases("merged" + Program.mhpUpdateCategory);
		}
		// OLD CODE: BasicTransform.removeUnusedFunctions(Program.getRoot());
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
		} else {
			RedundantSynchronizationRemovalForYA.removeBarriers(Program.getRoot());
		}
		// RedundantSynchronizationRemoval.removeBarriersFromAllParConsWithin(Program.getRoot());
		if (dumpIntermediate) {
			DumpSnapshot.dumpRoot("merged-rem" + Program.updateCategory);
			DumpSnapshot.dumpPointsTo("merged-rem" + Program.updateCategory);
			DumpSnapshot.dumpPhases("merged-rem" + Program.mhpUpdateCategory);
		}
		// TODO: Uncomment starting this.
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		FunctionInliner.inline(mainFunc);
		if (dumpIntermediate) {
			DumpSnapshot.dumpRoot("merged-rem-inlined" + Program.mhpUpdateCategory);
			DumpSnapshot.dumpPointsTo("merged-rem-inlined" + Program.updateCategory);
			DumpSnapshot.dumpPhases("merged-rem-inlined" + Program.mhpUpdateCategory);
		}
		ParallelConstructExpander.mergeParallelRegions(Program.getRoot());
		Program.getRoot().getInfo().removeUnusedElements();
		ProfileSS.insertCP();
		if (dumpIntermediate) {
			DumpSnapshot.dumpRoot("merged-rem-inlined-merged" + Program.mhpUpdateCategory);
			DumpSnapshot.dumpPointsTo("merged-rem-inlined-merged" + Program.updateCategory);
			DumpSnapshot.dumpPhases("merged-rem-inlined-merged" + Program.mhpUpdateCategory);
		}
		// Program.getRoot().getInfo().removeExtraScopes();
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
		} else {
			RedundantSynchronizationRemovalForYA.removeBarriers(Program.getRoot());
		}
		// RedundantSynchronizationRemoval.removeBarriersFromAllParConsWithin(Program.getRoot());
		// if (dumpIntermediate) {
		// DumpSnapshot.dumpRoot("merged-rem-inlined-merged-rem" +
		// Program.mhpUpdateCategory);
		// }
		// TODO: Uncomment till here.
		double totTime = (System.nanoTime() - Main.totalTime) / (1.0 * 1e9);
		double incMHPTime = 0.0;
		double incIDFATime = 0.0;
		long incMHPTriggers = 0;
		long incIDFATriggers = 0;
		long finalIncNodes = 0;
		long tarjanCount = 0;
		double sccTime = 0.0;
		// List<Long> triggerSizeCountList = null;
		System.err.println("Number of times IDFA update were triggered -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println("\t For " + analysis.getAnalysisName() + ": " + analysis.autoUpdateTriggerCounter);
			if (analysis.getAnalysisName() == AnalysisName.POINTSTO) {
				incIDFATriggers = analysis.autoUpdateTriggerCounter;
				// triggerSizeCountList = analysis.localList;
			}
		}
		System.err.println("Total number of times nodes were processed during automated IDFA update -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println("\t For " + analysis.getAnalysisName() + ": " + analysis.nodesProcessedDuringUpdate);
			if (analysis.getAnalysisName() == AnalysisName.POINTSTO) {
				finalIncNodes = analysis.nodesProcessed;
			}
		}
		System.err.println("Time spent in forward IDFA updates -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println(
					"\t For " + analysis.getAnalysisName() + ": " + analysis.flowAnalysisUpdateTimer / (1e9) + "s.");
			if (analysis.getAnalysisName() == AnalysisName.POINTSTO) {
				incIDFATime = analysis.flowAnalysisUpdateTimer / (1e9 * 1.0);
			}
		}
		// System.err.println("Time spent in SVE queries: " + SVEChecker.sveTimer / (1e9
		// * 1.0) + "s.");
		System.err.println("Time spent in phase update: " + BeginPhasePoint.stabilizationTime / (1e9 * 1.0) + "s.");
		incMHPTime = BeginPhasePoint.stabilizationTime / (1e9 * 1.0);
		System.err.println("Number of stabilizations of phase analysis: " + AutomatedUpdater.reinitMHPCounter);
		incMHPTriggers = AutomatedUpdater.reinitMHPCounter;
		System.err.println("Time spent generating SCCs: " + SCC.SCCTimer / (1e9 * 1.0) + "s.");
		sccTime = SCC.SCCTimer / (1e9 * 1.0);
		System.err.println("Total invocations of Tarjan's algorithm: " + SCC.tarjanCount);
		tarjanCount = SCC.tarjanCount;
		if (Program.fieldSensitive) {
			System.err.println("Number of field-sensitive queries: " + FieldSensitivity.counter);
			System.err.println("Time spent in field-sensitive queries: " + FieldSensitivity.timer / (1e9 * 1.0) + "s.");
		}
		// System.err.println("Time spent in inlining: " + FunctionInliner.inliningTimer
		// / (1e9 * 1.0) + "s.");
		// System.err.println("Time spent in having uni-task precision in IDFA edge
		// creation: "
		// + CFGInfo.uniPrecisionTimer / (1e9 * 1.0) + "s.");
		// System.err.println("Number of field-sensitive queries: " +
		// FieldSensitivity.counter);
		// System.err.println("Time spent in field-sensitive queries: " +
		// FieldSensitivity.timer / (1e9 * 1.0) + "s.");
		// System.err.println("Time spent in generating reverse postordering of the
		// program nodes: "
		// + TraversalOrderObtainer.orderGenerationTime / (1e9 * 1.0) + "s.");
		// if (Program.fieldSensitive) {
		// Misc.printToFile(ConstraintsGenerator.allConstraintString, Program.fileName +
		// "_z3_queries.txt");
		// }
		System.err.println("TOTAL TIME (including disk I/O time): " + totTime + "s.");
		System.err.println("This execution ran in " + Program.updateCategory + " mode for IDFA update, and in "
				+ Program.mhpUpdateCategory + " mode for MHP update.");
		System.err.println("Optimized a total of " + AutomatedUpdater.hasBeenOtimized + " stale markings.");
		System.err
				.println("Number of times PTA would have had to run in semi-eager mode: " + ProfileSS.flagSwitchCount);
		String s = (Program.sveSensitive == SVEDimension.SVE_SENSITIVE) ? "S" : "U";
		DumpSnapshot.printToFile(Program.getRoot(), (Program.fileName + "imop_output_" + Program.concurrencyAlgorithm
				+ "_" + Program.mhpUpdateCategory + s + ".i").trim());
		DumpSnapshot.dumpPointsTo("final" + Program.updateCategory);
		DumpSnapshot.dumpPhases("final" + Program.concurrencyAlgorithm + "_" + Program.mhpUpdateCategory + s);
		DumpSnapshot.dumpPredicates("final" + Program.concurrencyAlgorithm + "_" + Program.mhpUpdateCategory + s);
		if (dumpIntermediate) {
			DumpSnapshot.dumpNestedCFG(Program.getRoot(), "optimized" + Program.mhpUpdateCategory + s);
		}
		DecimalFormat df2 = Program.df2;
		// Count the number of aggregate phases
		int numPhases = 0;
		for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			numPhases += parCons.getInfo().getConnectedPhases().size();
		}

		// Count the number of explicit barriers.
		int numExplicitBarriers = Misc.getExactEnclosee(Program.getRoot(), BarrierDirective.class).size();
		StringBuilder resultString = new StringBuilder(Program.fileName + " "
				+ ((Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) ? "YUAN" : "ICON") + " "
				+ Program.mhpUpdateCategory + " "
				+ ((Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) ? "SVE-sensitive"
						: ((Program.sveSensitive == SVEDimension.SVE_SENSITIVE)
								? ("SVE-sensitive (" + df2.format(SVEChecker.sveTimer * 1.0 / 1e9) + ")")
								: "SVE-insensitive (0)"))
				+ " " + df2.format(totTime) + " " + df2.format(incMHPTime) + " " + df2.format(incIDFATime) + " "
				+ incMHPTriggers + " " + incIDFATriggers + " " + finalIncNodes + " " + tarjanCount + " "
				+ df2.format(sccTime) + " " + numPhases + " " + numExplicitBarriers);
		System.out.println(resultString);
		System.err.println(resultString);
		// DumpSnapshot.printToFile(Program.stabilizationStackDump.toString(),
		// "stabilization-dump" + Program.concurrencyAlgorithm +
		// Program.mhpUpdateCategory + s + ".txt");
		// System.err.println("Trigger count: " + triggerSizeCountList);
		System.exit(0);
	}

	public static void resetStaticFields() {
		DriverModule.counter = 0;
	}

	public static void mergeParRegs() {
		SVEDimension wasSVE = Program.sveSensitive;
		if (wasSVE == SVEDimension.SVE_SENSITIVE) {
			Program.sveSensitive = SVEDimension.SVE_INSENSITIVE;
		}
		boolean dumpIntermediate = Program.dumpIntermediateStates;

		double mprTime = System.nanoTime();
		ParallelConstructExpander.mergeParallelRegions(Program.getRoot());
		double totTime = (System.nanoTime() - Main.totalTime) / (1.0 * 1e9);
		Program.getRoot().getInfo().removeUnusedElements();

		ProfileSS.insertCP();
		if (dumpIntermediate) {
			DumpSnapshot.dumpRoot("merged" + Program.updateCategory);
			DumpSnapshot.dumpPointsTo("merged" + Program.updateCategory);
			DumpSnapshot.dumpPhases("merged" + Program.mhpUpdateCategory);
		}
		RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
		// RedundantSynchronizationRemoval.removeBarriersFromAllParConsWithin(Program.getRoot());
		if (dumpIntermediate) {
			DumpSnapshot.dumpRoot("merged-rem" + Program.updateCategory);
			DumpSnapshot.dumpPointsTo("merged-rem" + Program.updateCategory);
			DumpSnapshot.dumpPhases("merged-rem" + Program.mhpUpdateCategory);
		}
		double timerLocal = System.nanoTime();
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		System.err.println("Starting with function inlining..");
		FunctionInliner.inline(mainFunc);
		System.err.println("Inlining successful.");
		System.err.println("Time spent in function inlining: " + (System.nanoTime() - timerLocal) / 1e9);
		Program.getRoot().getInfo().removeUnusedElements();

		if (dumpIntermediate) {
			DumpSnapshot.dumpRoot("merged-rem-inlined" + Program.mhpUpdateCategory);
			DumpSnapshot.dumpPointsTo("merged-rem-inlined" + Program.updateCategory);
			DumpSnapshot.dumpPhases("merged-rem-inlined" + Program.mhpUpdateCategory);
		}

		timerLocal = System.nanoTime();
		ParallelConstructExpander.mergeParallelRegions(Program.getRoot());
		if (dumpIntermediate) {
			DumpSnapshot.dumpRoot("merged-rem-inlined-merged" + Program.mhpUpdateCategory);
			DumpSnapshot.dumpPointsTo("merged-rem-inlined-merged" + Program.updateCategory);
			DumpSnapshot.dumpPhases("merged-rem-inlined-merged" + Program.mhpUpdateCategory);
		}
		// Program.getRoot().getInfo().removeExtraScopes();
		RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());

		System.err.println("Time spent in expansion and barrier removal: " + (System.nanoTime() - timerLocal) / 1e9);
		DumpSnapshot.dumpRoot("merged-rem-inlined-merged-rem" + Program.mhpUpdateCategory);
		totTime = (System.nanoTime() - Main.totalTime) / (1.0 * 1e9);
		// System.err.println("MPR Time: " + (System.nanoTime() - mprTime) / 1e9);
		// System.err.println("TOTAL TIME (including disk I/O time): " + totTime +
		// "s.");
		// System.exit(0);
		// RedundantSynchronizationRemoval.removeBarriersFromAllParConsWithin(Program.getRoot());
		// if (dumpIntermediate) {
		// double
		totTime = (System.nanoTime() - Main.totalTime) / (1.0 * 1e9);
		System.err.println("TOTAL TIME (including disk I/O time): " + totTime + "s.");
		DumpSnapshot.dumpRoot("merged-rem-inlined-merged-rem" + Program.mhpUpdateCategory);
		// }
		if (wasSVE == SVEDimension.SVE_SENSITIVE) {
			Program.sveSensitive = SVEDimension.SVE_SENSITIVE;
		}
		// change();
		DriverModule.optimizeBarriers(); // This method does not return;
		System.exit(0);
	}

	public static void askQueries() {
		int counter = 0;
		long tim = 0;
		for (int i = 0; i < 100; i++) {
			for (DummyFlushDirective dfd1 : Misc.getExactEncloseeList(Program.getRoot(), DummyFlushDirective.class)) {
				for (DummyFlushDirective dfd2 : Misc.getExactEncloseeList(Program.getRoot(),
						DummyFlushDirective.class)) {
					long timLoc = System.nanoTime();
					if (Misc.doIntersect(new HashSet<>(dfd1.getInfo().getNodePhaseInfo().getPhaseSet()),
							new HashSet<>(dfd2.getInfo().getNodePhaseInfo().getPhaseSet()))
							&& Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YUANMHP) {
						CoExistenceChecker.canCoExistInAnyPhase(dfd1, dfd2);
					}
					tim += System.nanoTime() - timLoc;
					counter++;
				}
			}
		}
		double totTime = (System.nanoTime() - Main.totalTime) / (1.0 * 1e9);
		double incMHPTime = 0.0;
		double incIDFATime = 0.0;
		long incMHPTriggers = 0;
		long incIDFATriggers = 0;
		long finalIncNodes = 0;
		long tarjanCount = 0;
		double sccTime = 0.0;
		// List<Long> triggerSizeCountList = null;
		System.err.println("Number of times IDFA update were triggered -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println("\t For " + analysis.getAnalysisName() + ": " + analysis.autoUpdateTriggerCounter);
			if (analysis.getAnalysisName() == AnalysisName.POINTSTO) {
				incIDFATriggers = analysis.autoUpdateTriggerCounter;
				// triggerSizeCountList = analysis.localList;
			}
		}
		System.err.println("Total number of times nodes were processed during automated IDFA update -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println("\t For " + analysis.getAnalysisName() + ": " + analysis.nodesProcessedDuringUpdate);
			if (analysis.getAnalysisName() == AnalysisName.POINTSTO) {
				finalIncNodes = analysis.nodesProcessed;
			}
		}
		System.err.println("Time spent in forward IDFA updates -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println(
					"\t For " + analysis.getAnalysisName() + ": " + analysis.flowAnalysisUpdateTimer / (1e9) + "s.");
			if (analysis.getAnalysisName() == AnalysisName.POINTSTO) {
				incIDFATime = analysis.flowAnalysisUpdateTimer / (1e9 * 1.0);
			}
		}
		// System.err.println("Time spent in SVE queries: " + SVEChecker.sveTimer / (1e9
		// * 1.0) + "s.");
		System.err.println("Time spent in phase update: " + BeginPhasePoint.stabilizationTime / (1e9 * 1.0) + "s.");
		incMHPTime = BeginPhasePoint.stabilizationTime / (1e9 * 1.0);
		System.err.println("Number of stabilizations of phase analysis: " + AutomatedUpdater.reinitMHPCounter);
		incMHPTriggers = AutomatedUpdater.reinitMHPCounter;
		System.err.println("Time spent generating SCCs: " + SCC.SCCTimer / (1e9 * 1.0) + "s.");
		sccTime = SCC.SCCTimer / (1e9 * 1.0);
		System.err.println("Total invocations of Tarjan's algorithm: " + SCC.tarjanCount);
		tarjanCount = SCC.tarjanCount;
		if (Program.fieldSensitive) {
			System.err.println("Number of field-sensitive queries: " + FieldSensitivity.counter);
			System.err.println("Time spent in field-sensitive queries: " + FieldSensitivity.timer / (1e9 * 1.0) + "s.");
		}
		// System.err.println("Time spent in inlining: " + FunctionInliner.inliningTimer
		// / (1e9 * 1.0) + "s.");
		// System.err.println("Time spent in having uni-task precision in IDFA edge
		// creation: "
		// + CFGInfo.uniPrecisionTimer / (1e9 * 1.0) + "s.");
		// System.err.println("Number of field-sensitive queries: " +
		// FieldSensitivity.counter);
		// System.err.println("Time spent in field-sensitive queries: " +
		// FieldSensitivity.timer / (1e9 * 1.0) + "s.");
		// System.err.println("Time spent in generating reverse postordering of the
		// program nodes: "
		// + TraversalOrderObtainer.orderGenerationTime / (1e9 * 1.0) + "s.");
		// if (Program.fieldSensitive) {
		// Misc.printToFile(ConstraintsGenerator.allConstraintString, Program.fileName +
		// "_z3_queries.txt");
		// }
		System.err.println("TOTAL TIME (including disk I/O time): " + totTime + "s.");
		System.err.println("This execution ran in " + Program.updateCategory + " mode for IDFA update, and in "
				+ Program.mhpUpdateCategory + " mode for MHP update.");
		System.err.println("Optimized a total of " + AutomatedUpdater.hasBeenOtimized + " stale markings.");
		System.err
				.println("Number of times PTA would have had to run in semi-eager mode: " + ProfileSS.flagSwitchCount);
		String s = (Program.sveSensitive == SVEDimension.SVE_SENSITIVE) ? "S" : "U";
		DumpSnapshot.printToFile(Program.getRoot(), (Program.fileName + "imop_output_" + Program.concurrencyAlgorithm
				+ "_" + Program.mhpUpdateCategory + s + ".i").trim());
		DumpSnapshot.dumpPointsTo("final" + Program.updateCategory);
		DumpSnapshot.dumpPhases("final" + Program.concurrencyAlgorithm + "_" + Program.mhpUpdateCategory + s);
		DumpSnapshot.dumpPredicates("final" + Program.concurrencyAlgorithm + "_" + Program.mhpUpdateCategory + s);
		DecimalFormat df2 = Program.df2;
		// Count the number of aggregate phases
		int numPhases = 0;
		for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			numPhases += parCons.getInfo().getConnectedPhases().size();
		}

		// Count the number of explicit barriers.
		int numExplicitBarriers = Misc.getExactEnclosee(Program.getRoot(), BarrierDirective.class).size();
		StringBuilder resultString = new StringBuilder(Program.fileName + " "
				+ ((Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) ? "YUAN" : "ICON") + " "
				+ Program.mhpUpdateCategory + " "
				+ ((Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) ? "SVE-sensitive"
						: ((Program.sveSensitive == SVEDimension.SVE_SENSITIVE)
								? ("SVE-sensitive (" + df2.format(SVEChecker.sveTimer * 1.0 / 1e9) + ")")
								: "SVE-insensitive (0)"))
				+ " " + df2.format(totTime) + " " + df2.format(incMHPTime) + " " + df2.format(incIDFATime) + " "
				+ incMHPTriggers + " " + incIDFATriggers + " " + finalIncNodes + " " + tarjanCount + " "
				+ df2.format(sccTime) + " " + numPhases + " " + numExplicitBarriers + " " + counter + " " + tim + "s.");
		System.out.println(resultString);
		System.err.println(resultString);
		// DumpSnapshot.printToFile(Program.stabilizationStackDump.toString(),
		// "stabilization-dump" + Program.concurrencyAlgorithm +
		// Program.mhpUpdateCategory + s + ".txt");
		// System.err.println("Trigger count: " + triggerSizeCountList);
		System.exit(0);
	}

	public static void searchExample() {
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			return;
		}
		for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			Set<BeginPhasePoint> bppSet = new HashSet<>();
			for (AbstractPhase<?, ?> absPh : parCons.getInfo().getConnectedPhases()) {
				Phase ph = (Phase) absPh;
				bppSet.addAll(ph.getBeginPoints());
			}
			for (BeginPhasePoint bpp1 : bppSet) {
				for (BeginPhasePoint bpp2 : bppSet) {
					if (bpp1.getNode() == bpp2.getNode()) {
						continue;
					}
					for (EndPhasePoint epp : Misc.setIntersection(bpp1.getNextBarriers(), bpp2.getNextBarriers())) {
						Node n = epp.getNode();
						if (n instanceof BarrierDirective
								&& Misc.getEnclosingFunction(n) != Misc.getEnclosingFunction(bpp1.getNode())) {
							System.out.println("Found n!");
						}
					}
				}
			}
		}
	}

	private static void change() {
		System.err.println("Number of times IDFA update were triggered -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println("\t For " + analysis.getAnalysisName() + ": " + analysis.autoUpdateTriggerCounter);
		}
		System.err.println("Total number of times nodes were processed during automated IDFA update -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println("\t For " + analysis.getAnalysisName() + ": " + analysis.nodesProcessedDuringUpdate);
		}
		System.err.println("Time spent in forward IDFA updates -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println(
					"\t For " + analysis.getAnalysisName() + ": " + analysis.flowAnalysisUpdateTimer / (1e9) + "s.");
		}
		System.err.println("Time spent in SVE queries: " + SVEChecker.sveTimer / (1e9 * 1.0) + "s.");
		System.err.println("Time spent in phase update: "
				+ (AbstractPhase.stabilizationTime + BeginPhasePoint.stabilizationTime) / (1e9 * 1.0) + "s.");
		System.err.println("Time spent in inlining: " + FunctionInliner.inliningTimer / (1e9 * 1.0) + "s.");
		System.err.println("Time spent in having uni-task precision in IDFA edge creation: "
				+ CFGInfo.uniPrecisionTimer / (1e9 * 1.0) + "s.");
		System.err.println("Number of field-sensitive queries: " + FieldSensitivity.counter);
		System.err.println("Time spent in field-sensitive queries: " + FieldSensitivity.timer / (1e9 * 1.0) + "s.");
		System.err.println("Time spent in generating reverse postordering of the program nodes: "
				+ TraversalOrderObtainer.orderGenerationTime / (1e9 * 1.0) + "s.");
		if (Program.fieldSensitive) {
			DumpSnapshot.printToFile(ConstraintsGenerator.allConstraintString, Program.fileName + "_z3_queries.txt");
		}
		System.err.println(
				"TOTAL TIME (including disk I/O time): " + (System.nanoTime() - Main.totalTime) / (1.0 * 1e9) + "s.");
		DumpSnapshot.printToFile(Program.getRoot(), "imop_output.i");
		DumpSnapshot.dumpPhases("final");
		DumpSnapshot.dumpNestedCFG(Program.getRoot(), "optimized");
		System.exit(0);
	}

	public static void optimizeBarriers() {
		RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
		DumpSnapshot.dumpPhases("initial");
		long timer = System.nanoTime();
		DriverModule.run(Program.getRoot());
		timer = System.nanoTime() - timer;
		DumpSnapshot.dumpPhases("after");
		RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
		BasicTransform.removeEmptyConstructs(Program.getRoot());
		Program.getRoot().getInfo().removeUnusedElements();
		// OLD CODE: BasicTransform.removeDeadDeclarations(Program.getRoot());
		RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
		System.err.println("Time spent in the driver module: " + timer / (1e9 * 1.0) + "s.");
		System.err.println("Number of times IDFA update were triggered -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println("\t For " + analysis.getAnalysisName() + ": " + analysis.autoUpdateTriggerCounter);
		}
		System.err.println("Total number of times nodes were processed during automated IDFA update -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println("\t For " + analysis.getAnalysisName() + ": " + analysis.nodesProcessedDuringUpdate);
		}
		System.err.println("Time spent in forward IDFA updates -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println(
					"\t For " + analysis.getAnalysisName() + ": " + analysis.flowAnalysisUpdateTimer / (1e9) + "s.");
		}
		System.err.println("Time spent in SVE queries: " + SVEChecker.sveTimer / (1e9 * 1.0) + "s.");
		System.err.println("Time spent in phase update: "
				+ (AbstractPhase.stabilizationTime + BeginPhasePoint.stabilizationTime) / (1e9 * 1.0) + "s.");
		System.err.println("Time spent in inlining: " + FunctionInliner.inliningTimer / (1e9 * 1.0) + "s.");
		System.err.println("Time spent in having uni-task precision in IDFA edge creation: "
				+ CFGInfo.uniPrecisionTimer / (1e9 * 1.0) + "s.");
		System.err.println("Number of field-sensitive queries: " + FieldSensitivity.counter);
		System.err.println("Time spent in field-sensitive queries: " + FieldSensitivity.timer / (1e9 * 1.0) + "s.");
		System.err.println("Time spent in generating reverse postordering of the program nodes: "
				+ TraversalOrderObtainer.orderGenerationTime / (1e9 * 1.0) + "s.");
		if (Program.fieldSensitive) {
			DumpSnapshot.printToFile(ConstraintsGenerator.allConstraintString, Program.fileName + "_z3_queries.txt");
		}
		System.err.println(
				"TOTAL TIME (including disk I/O time): " + (System.nanoTime() - Main.totalTime) / (1.0 * 1e9) + "s.");
		System.err.println("This execution ran in " + Program.updateCategory + " mode for IDFA update, and in "
				+ Program.mhpUpdateCategory + " mode for MHP update.");
		DumpSnapshot.printToFile(Program.getRoot(), "imop_output.i");
		DumpSnapshot.dumpNestedCFG(Program.getRoot(), "optimized");
		DumpSnapshot.dumpPhases("final");
		// Main.dumpReachingDefinitions("final");
		// Main.dumpVisibleSharedReadWrittenCells("final");
		// Main.dumpCopyInfo("final");
		// Main.dumpPhaseAndCopyInfo("final");
		System.exit(0);
	}

	/**
	 * Performs synchronization optimizations on various loop statements in the
	 * input program, if required.
	 *
	 * @param root
	 *             root of the AST of the input program.
	 */
	public static void run(TranslationUnit root) {
		System.err.println("Pass: Performing synchronization optimizations on the program.");
		Set<Class<? extends Node>> iterationClasses = new HashSet<>(16);
		iterationClasses.add(DoStatement.class);
		iterationClasses.add(WhileStatement.class);
		iterationClasses.add(ForStatement.class);
		for (Node itStmtNode : Misc.getExactPostOrderEnclosee(root, iterationClasses)) {
			assert (!itStmtNode.getClass().getSimpleName().equals("IterationStatement"));
			IterationStatement itStmt = (IterationStatement) itStmtNode;
			int minPhaseCount = DriverModule.phaseCountPerExecution(itStmt);
			boolean proceed = DriverModule.run(itStmt);
			if (proceed) {
				for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
					UpwardPercolater.initPercolate(parCons);
				}
				/*
				 * Now we will try different scheduling options for shared
				 * accesses in various phases of the loop.
				 */
				// Program.sveSensitive = true;
				LoopInstructionsRescheduler.tryDifferentSchedulings(itStmt, minPhaseCount);
				DriverModule.performCopyElimination(itStmt);
				// Program.sveSensitive = false;
			}
		}
	}

	/**
	 * Performs in-place synchronization optimizations in {@code itStmt}, by
	 * applying code-percolation, loop unrolling
	 * and memory renaming. These translations are performed in a way that
	 * guarantees consistency of various
	 * data-structures that represent the semantics of the input program.
	 *
	 * @param itStmt
	 *               iteration-statement (a {@link WhileStatement}, a
	 *               {@link ForStatement}, or a {@link DoStatement}), on
	 *               which we need to apply synchronization elimination
	 *               optimizations.
	 *
	 * @return true, if the number of phases per execution is more than one.
	 */
	private static boolean run(IterationStatement itStmt) {
		/*
		 * Step 1a: Obtain the number of abstract phases encountered in one
		 * execution of this loop.
		 * Save it as maximum unrolling factor.
		 */
		int swapLength = getSwapLength(itStmt);
		int maxUnrollFactor = phaseCountPerExecution(itStmt);
		if (maxUnrollFactor == 1) {
			System.err.println("\t Not procesing the iteration at line #" + Misc.getLineNum(itStmt)
					+ " since it contains only one abstract phase within.");
			return false;
		}

		/*
		 * Step 1b: Convert for-statement and do-while--statement to a while-statement.
		 */
		itStmt = BasicTransform.convertToWhile(itStmt);

		// Step 2: Remove unnecessary barriers from the original body of the loop.
		System.err.println("\t Processing the iteration at line #" + Misc.getLineNum(itStmt)
				+ " for maximum unrolling factor of " + maxUnrollFactor);
		BasicTransform.simplifyPredicate(itStmt);
		if (itStmt instanceof WhileStatement) {
			WhileStatement whileStmt = (WhileStatement) itStmt;
			/*
			 * Let's move all declarations from the level of this
			 * whileStatement's body to outside the loop,
			 * so that they do not hamper code percolation.
			 * This method takes care of possible name collision.
			 */
			DeclarationEscalator
					.pushAllDeclarationsUpFromLevel((CompoundStatement) whileStmt.getInfo().getCFGInfo().getBody());
		}
		DriverModule.intraIterationCodePercolation(itStmt);
		RedundantSynchronizationRemoval.removeBarriers(itStmt);
		System.err.println("\t Initial phase count per iteration: " + DriverModule.phaseCountPerExecution(itStmt)
				+ " for loop at line#" + Misc.getLineNum(itStmt));
		// System.err.println("\t Initial phase count per iteration: " +
		// NewDriverModule.getBarrierCountInNode(itStmt)
		// + " for loop at line#" + Misc.getLineNum(itStmt));

		// Step 3: Capture the original body to be used later.
		Statement origBody = null;
		if (itStmt instanceof ForStatement) {
			origBody = ((ForStatement) itStmt).getInfo().getCFGInfo().getBody();
		} else if (itStmt instanceof DoStatement) {
			origBody = ((DoStatement) itStmt).getInfo().getCFGInfo().getBody();
		} else if (itStmt instanceof WhileStatement) {
			origBody = ((WhileStatement) itStmt).getInfo().getCFGInfo().getBody();
		}

		// Step 4: Process each phase that may have at least one starting barrier in
		// itStmt, until fixed point.
		boolean iterate = false;
		int unrollFactor = 1;
		double oldPhaseCountPerIteration = DriverModule.phaseCountPerExecution(itStmt);
		DumpSnapshot.dumpPhases("beforeUnroll");
		boolean lastStep = false;
		do {
			iterate = false;
			System.out.println("Increasing the generation.");
			// Step a. Perform loop unrolling, by incrementing the unroll factor by 1.
			DriverModule.unrollLoopWithOriginalBodyAndEnvironmentUpdate(itStmt, origBody, new HashMap<>());
			unrollFactor++;
			DumpSnapshot.dumpPhases("afterUnrolling-" + unrollFactor);

			// Step b. Extract the border node, and remove the associated border node
			// marker.
			// Also obtain the cleanWrites.
			Statement borderNode = itStmt.getInfo().getStatementWithLabel("__imopLoopBorder");
			borderNode.getInfo().removeSimpleLabelAnnotation("__imopLoopBorder");
			Set<Node> renamingCandidates = DriverModule.getRenamingCandidates(itStmt, borderNode);
			Node newBorderNode = DriverModule.getNewBorderNode(itStmt);

			// Step c. Remove anti-dependence across the old and new portion of the loop
			// body.
			DriverModule.performNoUpdateRenaming(itStmt, renamingCandidates, newBorderNode);
			DumpSnapshot.dumpPhases("afterRenaming-" + unrollFactor);

			// Step d. Perform code motion, and barrier removal.
			DriverModule.intraIterationCodePercolation(itStmt);
			RedundantSynchronizationRemoval.removeBarriers(itStmt);
			// Main.dumpPhases("afterUnrolledMotion-" + unrollFactor);
			DumpSnapshot.dumpPhases("afterRenamedMotion-" + unrollFactor);
			DumpSnapshot.dumpRoot("afterRenamedMotion-" + unrollFactor);

			// Step e. Decide whether we want to unroll again.
			double newPhaseCountPerIteration = DriverModule.phaseCountPerExecution(itStmt) / (1.0 * unrollFactor);
			System.out.println("New phase count per iteration: " + newPhaseCountPerIteration);
			if (!lastStep) {
				// if (newPhaseCountPerIteration < oldPhaseCountPerIteration || unrollFactor <
				// maxUnrollFactor) {
				if (unrollFactor < maxUnrollFactor) {
					iterate = true;
				} else {
					System.out.println("Swap length is: " + swapLength);
					if (swapLength != 0) {
						lastStep = true;
						iterate = true;
					} else {
						iterate = false;
					}
				}
			} else {
				if (unrollFactor % swapLength != 0) {
					System.out.println(
							"Unrolling once again, such that unrolling-factor can be a multiple of swap-length.");
					iterate = true;
				} else {
					System.out.println("Now, we stop with unrolling factor " + unrollFactor + ", which is divisible by "
							+ swapLength + ".");
					iterate = false;
				}
			}
			oldPhaseCountPerIteration = newPhaseCountPerIteration;
			if (oldPhaseCountPerIteration == 0) {
				break;
			}
		} while (iterate);
		return true;

		// Old Code:
		// // Step 4: Check with increasing number of unrolling factors.
		// double oldPhasesPerIteration = DriverModule.getBarrierCountInNode(itStmt);
		// IterationStatement oldStmt = itStmt;
		// for (int i = 1; i < 8; i++) {
		// IterationStatement copyStmt = FrontEnd.parseAndNormalize(itStmt.toString(),
		// IterationStatement.class);
		// copyStmt = (IterationStatement) Misc.getCFGNodeFor(copyStmt);
		// copyStmt.getInfo().unrollLoop(i);
		// NodeReplacer.replaceNodes(oldStmt, copyStmt);
		// UpwardPercolater.intraIterationPercolate(copyStmt);
		// RedundantSynchronizationRemoval.removeBarriers(copyStmt);
		// double newPhasesPerIteration = DriverModule.getBarrierCountInNode(copyStmt);
		// // System.out.println(oldPhasesPerIteration + " " + newPhasesPerIteration);
		// if (oldPhasesPerIteration <= newPhasesPerIteration / i) {
		// NodeReplacer.replaceNodes(copyStmt, oldStmt);
		// break;
		// }
		// oldPhasesPerIteration = newPhasesPerIteration / i;
		// oldStmt = copyStmt;
		// }

		// Older Code:
		// float oldPhasesPerIteration = 0.0f;
		// float newPhasePerIteration = 0.0f;
		// UpwardPercolater.intraIterationPercolate(itStmt);
		// RedundantSynchronizationRemoval.removeBarriers(itStmt);
		// newPhasePerIteration = Misc.getPhasesPerIteration(itStmt);
		// do {
		// oldPhasesPerIteration = newPhasePerIteration;
		// if (oldPhasesPerIteration == 0) {
		// break;
		// }
		//
		// if (LoopTransformations.isUnrollingFeasible(itStmt)) {
		// itStmt.getInfo().unrollLoop(2);
		// } else {
		// break;
		// }
		//
		// RedundantSynchronizationRemoval.removeBarriers(itStmt);
		// newPhasePerIteration = Misc.getPhasesPerIteration(itStmt);
		// } while (newPhasePerIteration < oldPhasesPerIteration);
	}

	/**
	 * Obtain the maximum size of swap-chain present anywhere within encloser.
	 *
	 * @param encloser
	 *                 enclosing node, within which the length of swap-chain, if
	 *                 any, has to be found.
	 *
	 * @return maximum length of swap-chain within the node.
	 */
	private static int getSwapLength(Node encloser) {
		int maxLength = 0;
		for (CompoundStatement compStmt : Misc.getInheritedPostOrderEnclosee(encloser, CompoundStatement.class)) {
			for (Node elem : compStmt.getInfo().getCFGInfo().getElementList()) {
				if (elem instanceof ExpressionStatement) {
					ExpressionStatement expStmt = (ExpressionStatement) elem;
					if (!expStmt.getInfo().isCopyInstruction()) {
						continue;
					}
					int thisLength = CopyEliminator.swapLength(expStmt, compStmt);
					if (maxLength < thisLength) {
						maxLength = thisLength;
					}
				}
			}
		}
		return maxLength;
	}

	private static Set<Node> getRenamingCandidates(IterationStatement itStmt, Node borderNode) {
		borderNode = Misc.getFirstLeaves(borderNode).get(0);
		Set<Node> itContents = itStmt.getInfo().getCFGInfo().getLexicalCFGLeafContents();

		Set<Node> cleanWrites = new HashSet<>();
		/*
		 * Step 1: Collect the clean writes that are reachable from BPPs that
		 * belong to itStmt, and that start a phase of which borderNode is a
		 * part.
		 */
		assert (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON);
		Set<Phase> borderPhases = (Set<Phase>) borderNode.getInfo().getNodePhaseInfo().getPhaseSet();
		cleanWrites.addAll(extractCleanWrites(itContents, borderPhases));

		/*
		 * Step 2: Collect the clean writes that are reachable from BPPs that
		 * belong to itStmt, and that start a phase which is a successor of
		 * those of the borderNode.
		 */
		Set<Phase> nextPhases = new HashSet<>();
		for (Phase ph : borderPhases) {
			nextPhases.add(ph.getSuccPhase());
		}
		cleanWrites.addAll(extractCleanWrites(itContents, nextPhases));

		return cleanWrites;
	}

	/**
	 * Obtain clean writes of shared variables in phases present in
	 * {@code phaseSet}, within the loop whose contents are
	 * {@code itContents}.
	 * <br>
	 * Note: We skip those clean writes which write to a symbol of complicated type
	 * that does not overload "=" operator
	 * (e.g., arrays, functions, etc.).
	 *
	 * @param itContents
	 *                     contents of a loop.
	 * @param borderPhases
	 *                     set of phases in which clean-writes have to be found.
	 *
	 * @return set of clean-writes (mostly {@link ExpressionStatement} nodes)
	 *         present in {@code itContents}, occurring
	 *         in {@code phaseSet}.
	 */
	private static Set<Node> extractCleanWrites(Set<Node> itContents, Set<Phase> borderPhases) {
		Set<Node> cleanWrites = new HashSet<>();
		for (Phase ph : borderPhases) {
			for (BeginPhasePoint bpp : ph.getBeginPoints()) {
				Node bppNode = bpp.getNode();
				if (bppNode instanceof BeginNode) {
					continue;
				}
				if (!itContents.contains(bppNode)) {
					continue;
				}
				// Step a. Obtain the set of cleanWrites.
				outer: for (Node reachable : bpp.getReachableNodes()) {
					Cell cleanCell = reachable.getInfo().getCleanWrite();
					if ((cleanCell == null) || !(cleanCell instanceof Symbol)) {
						continue;
					}
					// Consider clean writes to only shared variables.
					if (reachable.getInfo().getSharingAttribute(cleanCell) != DataSharingAttribute.SHARED) {
						continue;
					}
					// Ignore arrays.
					Symbol sym = (Symbol) cleanCell;
					if (sym.getType() instanceof ArrayType) {
						continue;
					}
					// Ignore those nodes which are outside the loop.
					if (!itContents.contains(reachable)) {
						continue;
					}
					// Ignore all those nodes which may have successors or predecessors outside the
					// loop.
					for (Node succ : reachable.getInfo().getCFGInfo().getInterProceduralLeafSuccessors()) {
						if (!itContents.contains(succ)) {
							continue outer;
						}
					}
					for (Node pred : reachable.getInfo().getCFGInfo().getInterProceduralLeafPredecessors()) {
						if (!itContents.contains(pred)) {
							continue outer;
						}
					}
					cleanWrites.add(reachable);
				}
			}
		}
		return cleanWrites;
	}

	/**
	 * This method starts at the {@code borderNode}, and tries to remove an
	 * anti-edge across it, by renaming shared
	 * variables, if required, up till exit points from this new copy of the
	 * original iteration body.
	 *
	 * @param itStmt
	 *                      iteration-statement that has recently been added with a
	 *                      new copy of original iteration.
	 * @param cleanWrites
	 *                      set of those nodes which are reachable from BPPs that
	 *                      belong to the itSmt, and that start a phase of
	 *                      which the first node in the newly added iteration is a
	 *                      part.
	 * @param newBorderNode
	 *                      borderNode that separates the new copy with the next.
	 *
	 * @return true, if any code change was performed.
	 */
	private static boolean performNoUpdateRenaming(IterationStatement itStmt, Set<Node> cleanWrites,
			Node newBorderNode) {
		boolean changed = false;
		Set<Node> itContents = itStmt.getInfo().getCFGInfo().getLexicalCFGLeafContents();

		/*
		 * Step 1: For each clean write node, obtain the set of inside and
		 * frontier nodes.
		 */
		Map<Node, Set<Node>> insideNodeMap = new HashMap<>();
		Map<Node, Set<Node>> allInsideNodeMap = new HashMap<>();
		Map<Node, Set<Node>> frontierNodeMap = new HashMap<>();
		for (Node cleanNode : cleanWrites) {
			System.err.println("\t Looking into the nodes of interest for " + cleanNode);
			Set<Node> insideNodes;
			Set<Node> allInsideNodes;
			Set<Node> frontierNodes = new HashSet<>();

			insideNodes = CollectorVisitor.collectNodeSetInGenericGraph(cleanNode, frontierNodes, (n) -> {
				if (n instanceof BreakStatement
						&& Misc.getEnclosingLoopOrForConstruct(n) == Misc.getCFGNodeFor(itStmt)) {
					return true;
				}
				if (n == newBorderNode) {
					return true;
				}
				for (Node succ : n.getInfo().getCFGInfo().getInterProceduralLeafSuccessors()) {
					if (!itContents.contains(succ)) {
						return true;
					}
				}
				for (Node pred : n.getInfo().getCFGInfo().getInterProceduralLeafPredecessors()) {
					if (!itContents.contains(pred)) {
						return true;
					}
				}
				return false;
			}, (n) -> n.getInfo().getCFGInfo().getInterProceduralLeafSuccessors());

			// Make sure that only relevant nodes are taken into account.
			Set<Node> relevantNodes = new HashSet<>();
			Cell cleanCell = cleanNode.getInfo().getWrites().get(0);
			for (Node n : insideNodes) {
				if (n.getInfo().getReads().contains(cleanCell) || n.getInfo().getWrites().contains(cleanCell)) {
					relevantNodes.add(n);
				}
			}
			allInsideNodes = insideNodes;
			insideNodes = relevantNodes;

			// Make sure that the cleanNode itself is treated as an insideNode, if there
			// exists any other insideNode.
			if (insideNodes.size() != 0) {
				insideNodes.add(cleanNode);
			} else {
				// If there are no nodes of interest for a given cell, make both maps empty for
				// that cell.
				frontierNodes.clear();
			}
			System.err.println("\t\tInside nodes: " + insideNodes);
			System.err.println("\t\tFrontier nodes: " + frontierNodes);
			// Finally, add the node-sets to the map.
			insideNodeMap.put(cleanNode, insideNodes);
			allInsideNodeMap.put(cleanNode, allInsideNodes);
			frontierNodeMap.put(cleanNode, frontierNodes);
		}

		/*
		 * Step 2: Make changes to the code, if required, to perform memory
		 * renaming,
		 * back and forth.
		 */
		// Step 2.a Set to obtain a set of renamings on each node.
		HashMap<Node, HashMap<String, String>> renamingsOnNodes = new HashMap<>();

		// Step 2.b Set to obtain a set of identifiers that must be swapped back before
		// any given node.
		HashMap<Node, HashMap<String, String>> swapBackMap = new HashMap<>();

		// Step 2.c Populate sets of Step 3.a and Step 3.b.
		for (Node cleanWrite : cleanWrites) {
			Set<Node> insideNodes = insideNodeMap.get(cleanWrite);
			Set<Node> frontierNodes = frontierNodeMap.get(cleanWrite);
			if (insideNodes.isEmpty()) {
				continue;
			}
			changed = true;

			Cell cleanCell = cleanWrite.getInfo().getWrites().get(0);
			Symbol sym = (Symbol) cleanCell;

			// Add the declaration for new symbol at appropriate scope.
			String newSymbolName = Builder.getNewTempName(sym.getName());
			Type type = sym.getType();
			Declaration newSymbolDecl = type.getDeclaration(newSymbolName);
			System.err.println("\t\tNew declaration: " + newSymbolDecl);
			Scopeable scope = sym.getDefiningScope();
			if (scope instanceof CompoundStatement) {
				CompoundStatement compStmt = (CompoundStatement) scope;
				compStmt.getInfo().getCFGInfo().addDeclaration(newSymbolDecl);
			} else if (scope instanceof FunctionDefinition) {
				FunctionDefinition funcDef = (FunctionDefinition) scope;
				CompoundStatement body = funcDef.getInfo().getCFGInfo().getBody();
				body.getInfo().getCFGInfo().addDeclaration(newSymbolDecl);
			} else if (scope instanceof TranslationUnit) {
				Builder.addDeclarationToGlobals(newSymbolDecl);
			}

			// Update for inside nodes.
			for (Node inside : insideNodes) {
				HashMap<String, String> renamingMap;
				if (renamingsOnNodes.containsKey(inside)) {
					renamingMap = renamingsOnNodes.get(inside);
				} else {
					renamingMap = new HashMap<>();
					renamingsOnNodes.put(inside, renamingMap);
				}
				System.err.println("\t We will replace " + sym.getName() + " with " + newSymbolName + " in " + inside);
				renamingMap.put(sym.getName(), newSymbolName);
			}

			// Update for frontier nodes.
			for (Node frontier : frontierNodes) {
				HashMap<String, String> swapMap;
				if (swapBackMap.containsKey(frontier)) {
					swapMap = swapBackMap.get(frontier);
				} else {
					swapMap = new HashMap<>();
					swapBackMap.put(frontier, swapMap);
				}
				System.err.println("\t We might swap " + sym.getName() + " with " + newSymbolName
						+ " immediately before " + frontier + " if required.");
				swapMap.put(sym.getName(), newSymbolName);
			}
		}
		// Perform renaming on the inside nodes of all cleanWrites together
		for (Node inside : renamingsOnNodes.keySet()) {
			BasicTransform.renameIdsInNodes(inside, renamingsOnNodes.get(inside));
		}

		// Obtain the swapNode for each frontier.
		HashMap<Node, Node> swapNodeForFrontier = new HashMap<>();
		for (Node frontier : swapBackMap.keySet()) {
			/*
			 * Step 1: Ignore those frontier which do not require any
			 * swap-backs.
			 */
			if (swapBackMap.get(frontier).isEmpty()) {
				continue;
			}

			// THIRD option of the new code.
			// If the swap is required on all successors of the frontier,
			// add the node before the frontier (if there is no data clash). Otherwise,
			// proceed down.
			/*
			 * Step 2: If the original variable is not used before being
			 * redefined anywhere after the frontier, then skip the swap of that
			 * variable in this frontier.
			 */
			Set<String> keys = new HashSet<>(swapBackMap.get(frontier).keySet());
			Set<String> needSwapping = new HashSet<>();
			for (String symName : keys) {
				Symbol swapOldSym = Misc.getSymbolEntry(symName, frontier);
				if (symName == null) {
					continue;
				}

				for (Node succ : frontier.getInfo().getCFGInfo().getInterProceduralLeafSuccessors()) {
					/*
					 * Check if swapOldSym is used anywhere after succ without
					 * being redefined.
					 */
					if (succ.getInfo().getReads().contains(swapOldSym)) {
						needSwapping.add(symName);
						continue;
					}

					NodeWithStack startPoint = new NodeWithStack(succ, new CallStack());
					Set<NodeWithStack> endPoints = new HashSet<>();
					CollectorVisitor.collectNodeSetInGenericGraph(startPoint, endPoints, (n) -> {
						CellSet readsOfN = new CellSet(n.getNode().getInfo().getReads());
						if (readsOfN.contains(swapOldSym)) {
							return true;
						} else {
							return false;
						}
					}, (n) -> {
						Set<NodeWithStack> retSet = new HashSet<>();
						for (NodeWithStack succNode : n.getNode().getInfo().getCFGInfo()
								.getInterProceduralLeafSuccessors(n.getCallStack())) {
							// TODO: Check if we should check n's or succNode's writes in the line below.
							CellSet writesOfN = new CellSet(n.getNode().getInfo().getWrites());
							if (writesOfN.size() == 1 && writesOfN.contains(swapOldSym)) {
								// Skip.
							} else {
								retSet.add(succNode);
							}
						}
						return retSet;
					});
					if (endPoints.size() != 0) {
						needSwapping.add(symName);
						continue;
					}
				}
			}

			/*
			 * Step 3: Ignore all those frontiers that do not require any
			 * swap-backs.
			 */
			if (needSwapping.isEmpty()) {
				continue;
			}

			/*
			 * Step 4: Obtain the swapping node.
			 */
			String swapperStr;
			if (frontier.getInfo().getAllLexicalNonLeafEnclosersExclusive().stream()
					.anyMatch(non -> non instanceof SingleConstruct || non instanceof MasterConstruct)) {
				swapperStr = "{";
				for (String old : needSwapping) {
					swapperStr += old + " = " + swapBackMap.get(frontier).get(old) + ";\n";
				}
				swapperStr += "}";
			} else {
				swapperStr = "#pragma omp single \n{";
				for (String old : needSwapping) {
					swapperStr += old + " = " + swapBackMap.get(frontier).get(old) + ";\n";
				}
				swapperStr += "}";
			}
			Statement swapperNode = FrontEnd.parseAndNormalize(swapperStr, Statement.class);
			swapNodeForFrontier.put(frontier, swapperNode);

		}

		Set<Node> allInsideNodes = new HashSet<>();
		for (Node cleanWrite : cleanWrites) {
			allInsideNodes.addAll(allInsideNodeMap.get(cleanWrite));
		}

		/*
		 * Perform actual addition of all swapNode of a given frontier
		 * immediately before that frontier, within the loop.
		 */
		for (Node frontier : swapNodeForFrontier.keySet()) {
			Node swapNode = swapNodeForFrontier.get(frontier);
			if (swapNode == null) {
				continue;
			}
			System.err.println("\t Adding the swap node " + swapNode + " for " + frontier + ".");
			Set<Node> frontierPreds = frontier.getInfo().getCFGInfo().getInterProceduralLeafPredecessors();
			/*
			 * If all predecessors of a frontier are part of allInsideNodes,
			 * then add a single copy of the swapNode before the frontier.
			 */
			if (frontierPreds.stream().allMatch((p) -> allInsideNodes.contains(p))) {
				InsertImmediatePredecessor.insert(frontier, swapNode);
			} else {
				/*
				 * Otherwise, add a copy of swapNode on only those edges
				 * that originate within internal nodes and terminate at the
				 * frontier.
				 */
				boolean first = true;
				for (Node frontierPred : frontierPreds) {
					if (allInsideNodes.contains(frontierPred)) {
						if (first) {
							first = false;
							InsertOnTheEdge.insert(frontierPred, frontier, swapNode);
							continue;
						}
						Node copiedSwapNode = Builder.getCopiedTarget(swapNode);
						InsertOnTheEdge.insert(frontierPred, frontier, copiedSwapNode);
					}
				}
			}
		}

		itStmt.getInfo().removeExtraScopes();
		return changed;
	}

	/**
	 * Obtain the current border node for the given iteration-statement.
	 *
	 * @param itStmt
	 *
	 * @return
	 */
	private static Node getNewBorderNode(IterationStatement itStmt) {
		if (itStmt instanceof ForStatement) {
			ForStatement forStmt = (ForStatement) itStmt;
			return forStmt.getInfo().getCFGInfo().getStepExpression();
		} else if (itStmt instanceof DoStatement) {
			DoStatement doStmt = (DoStatement) itStmt;
			return doStmt.getInfo().getCFGInfo().getPredicate();
		} else if (itStmt instanceof WhileStatement) {
			WhileStatement whileStmt = (WhileStatement) itStmt;
			return whileStmt.getInfo().getCFGInfo().getPredicate();
		}
		return null;
	}

	/**
	 * Modifies the iteration-statement {@code itStmt}, by incrementing its
	 * unrolling factor by one, using {@code
	 * origBody}.
	 * <br>
	 * Note that this method places a special label marker: "__imopLoopBorder",
	 * which can be used to extract the loop
	 * border. Care should be taken to remove this label after border extraction.
	 *
	 * @param itStmt
	 * @param origBody
	 * @param environmentUpdate
	 */
	private static void unrollLoopWithOriginalBodyAndEnvironmentUpdate(IterationStatement itStmt, Statement origBody,
			HashMap<Symbol, Symbol> environmentUpdate) {
		// NOTE: It so turns out that environmentalUpdate is not required in this
		// method.
		String oneStr = Builder.getRepeatableCopyForSameScope(origBody);
		if (itStmt instanceof ForStatement) {
			ForStatement forStmt = (ForStatement) itStmt;
			Statement oldBody = forStmt.getInfo().getCFGInfo().getBody();
			Expression e3 = forStmt.getInfo().getCFGInfo().getStepExpression();
			Expression e2 = forStmt.getInfo().getCFGInfo().getTerminationExpression();
			String newBodyStr = "{";
			CompoundStatement oldCompStmt = (CompoundStatement) Misc.getCFGNodeFor(oldBody);
			for (Node oldElement : oldCompStmt.getInfo().getCFGInfo().getElementList()) {
				newBodyStr += oldElement;
			}
			newBodyStr += "__imopLoopBorder: " + e3 + ";";
			if (forStmt.getInfo().hasConstantPredicate()) {
				if (forStmt.getInfo().predicateIsConstantTrue()) {
					newBodyStr += "";
				} else {
					newBodyStr += "break;";
				}
			} else {
				newBodyStr += "if (!(" + e2 + ")) {break;}";
			}
			newBodyStr += oneStr;
			newBodyStr += "}";
			Statement newBody = FrontEnd.parseAndNormalize(newBodyStr, Statement.class);
			forStmt.getInfo().getCFGInfo().setBody(newBody);

		} else if (itStmt instanceof DoStatement) {
			DoStatement doStmt = (DoStatement) itStmt;
			Statement oldBody = doStmt.getInfo().getCFGInfo().getBody();
			Expression cond = doStmt.getInfo().getCFGInfo().getPredicate();
			String newBodyStr = "{";
			CompoundStatement oldCompStmt = (CompoundStatement) Misc.getCFGNodeFor(oldBody);
			for (Node oldElement : oldCompStmt.getInfo().getCFGInfo().getElementList()) {
				newBodyStr += oldElement;
			}
			if (doStmt.getInfo().hasConstantPredicate()) {
				if (doStmt.getInfo().predicateIsConstantTrue()) {
					newBodyStr += "__imopLoopBorder: ;";
				} else {
					newBodyStr += "__imopLoopBorder: break;";
				}
			} else {
				newBodyStr += "__imopLoopBorder: if (!(" + cond + ")) {break;}";
			}
			newBodyStr += oneStr;
			newBodyStr += "}";
			Statement newBody = FrontEnd.parseAndNormalize(newBodyStr, Statement.class);
			doStmt.getInfo().getCFGInfo().setBody(newBody);
		} else if (itStmt instanceof WhileStatement) {
			WhileStatement whileStmt = (WhileStatement) itStmt;
			Statement oldBody = whileStmt.getInfo().getCFGInfo().getBody();
			Expression cond = whileStmt.getInfo().getCFGInfo().getPredicate();
			String newBodyStr = "{";
			CompoundStatement oldCompStmt = (CompoundStatement) Misc.getCFGNodeFor(oldBody);
			for (Node oldElement : oldCompStmt.getInfo().getCFGInfo().getElementList()) {
				newBodyStr += oldElement + "\n";
			}
			if (whileStmt.getInfo().hasConstantPredicate()) {
				if (whileStmt.getInfo().predicateIsConstantTrue()) {
					newBodyStr += "__imopLoopBorder: ;";
				} else {
					newBodyStr += "__imopLoopBorder: break;";
				}
			} else {
				newBodyStr += "__imopLoopBorder: if (!(" + cond + ")) {break;}";
			}
			newBodyStr += oneStr;
			newBodyStr += "}";
			Statement newBody = FrontEnd.parseAndNormalize(newBodyStr, Statement.class);
			whileStmt.getInfo().getCFGInfo().setBody(newBody);
		}
		itStmt.getInfo().removeExtraScopes();
	}

	private static boolean intraIterationCodePercolation(IterationStatement itStmt) {
		BeginNode beginOfLoop = itStmt.getInfo().getCFGInfo().getNestedCFG().getBegin();

		List<Phase> entryPhases = new ArrayList<>();
		entryPhases.addAll((Collection<? extends Phase>) beginOfLoop.getInfo().getNodePhaseInfo().getPhaseSet());

		Set<Node> cfgContents = new HashSet<>();
		for (NodeWithStack nodeWithStack : itStmt.getInfo().getCFGInfo()
				.getIntraTaskCFGLeafContentsOfSameParLevel(new CallStack())) {
			cfgContents.add(nodeWithStack.getNode());
		}

		List<Phase> lastPhases = new ArrayList<>();
		List<Phase> phaseList = new ArrayList<>();
		phaseList.addAll(entryPhases);
		phaseList
				.addAll(CollectorVisitor.collectNodeListInGenericGraph(entryPhases, lastPhases, (ph) -> false, (ph) -> {
					List<Phase> nextPhaseList = new LinkedList<>();
					Phase succPhase = ph.getSuccPhase();
					if (succPhase != null) {
						for (EndPhasePoint endPP : succPhase.getEndPoints()) {
							if (cfgContents.contains(endPP.getNode())) {
								nextPhaseList.add(succPhase);
								break;
							}
						}
					}
					return nextPhaseList;
				}));
		phaseList.addAll(lastPhases);

		boolean anyChange = false;
		boolean anyPhaseUpdated;
		do {
			anyPhaseUpdated = false;
			for (Phase ph : phaseList) {
				anyPhaseUpdated |= ph.percolateCodeUpwardsInLoop(itStmt);
			}
			anyChange |= anyPhaseUpdated;
		} while (anyPhaseUpdated);
		return anyChange;
	}

	/**
	 * This method performs copy elimination on each node in the parent block of
	 * {@code itStmt}, followed by dead code
	 * removal, in an attempt to reduce the shared dependences.
	 *
	 * @param itStmt
	 *               loop from within whose parent copy elimination has to be
	 *               performed.
	 *
	 * @return true, if any copy was eliminated.
	 */
	public static boolean performCopyElimination(IterationStatement itStmt) {
		CompoundStatement enclosingCS = (CompoundStatement) Misc.getEnclosingBlock(itStmt);
		if (enclosingCS == null) {
			return false;
		}
		boolean changed = false;
		while (true) {
			DumpSnapshot.dumpPhaseAndCopyInfo(counter++ + "");
			boolean internalChanged;
			do {
				internalChanged = false;
				while (CopyEliminator.removeAllCopyKillers(enclosingCS)) {
					internalChanged = true;
					changed = true;
				}
				while (CopyEliminator.eliminateDeadCopies(enclosingCS)) {
					internalChanged = true;
					changed = true;
				}
			} while (internalChanged);
			Set<Node> copySources = CopyEliminator.replaceCopiesIn(enclosingCS);
			if (copySources.isEmpty()) {
				break;
			}
		}
		return changed;
		// Old code:
		// do {
		// Main.dumpCopyInfo(counter++ + "");
		// Set<Node> allSources = new HashSet<>();
		// do {
		// Set<Node> copySources = CopyEliminator.replaceCopiesIn(enclosingCS);
		// if (copySources.isEmpty()) {
		// break;
		// } else {
		// allSources.addAll(copySources);
		// boolean removedKillers = CopyEliminator.removeAllCopyKillers(copySources);
		// if (!removedKillers) {
		// break;
		// }
		// }
		// } while (true);
		// CellSet symbols = new CellSet();
		// for (Node n : allSources) {
		// symbols.addAll(n.getInfo().getWrites());
		// }
		// boolean changed = CopyEliminator.eliminateDeadCopies(enclosingCS);
		// if (!changed) {
		// break;
		// }
		// } while (true);

	}

	/**
	 * To obtain the maximum number of abstract phases that one execution of this
	 * loop may observe.
	 *
	 * @param itStmt
	 *               loop to be tested.
	 *
	 * @return maximum number of abstract phases that can be encountered in one
	 *         execution of this loop.
	 */
	public static int phaseCountPerExecution(IterationStatement itStmt) {
		int maxLength = 1;
		Set<NodeWithStack> internalNodes = itStmt.getInfo().getCFGInfo()
				.getIntraTaskCFGLeafContentsOfSameParLevel(new CallStack());
		Node loopEntryPoint = itStmt.getInfo().getLoopEntryPoint();
		// TODO: Add code here for the case where loopEntryPoint is null.
		int thisOuterLength = 1;
		for (AbstractPhase<?, ?> ph : loopEntryPoint.getInfo().getNodePhaseInfo().getPhaseSet()) {
			/*
			 * If this phase does not have any end point that lies within
			 * the loop, then continue.
			 */
			if (!ph.getEndPoints().parallelStream().anyMatch(
					n -> internalNodes.parallelStream().anyMatch(iN -> iN.getNode() == n.getNodeFromInterface()))) {
				continue;
			}

			/*
			 * For this phase, let's try and count the number of reachable
			 * phases that have at least one unique ending barrier in this
			 * loop.
			 */
			int thisLength = 1;
			Phase nextPhase = ((Phase) ph).getSuccPhase();
			Set<Phase> visitedPhases = new HashSet<>();
			while (nextPhase != null) {
				if (visitedPhases.contains(nextPhase)) {
					break;
				} else {
					visitedPhases.add(nextPhase);
				}
				if (nextPhase.getNodeSet().parallelStream().anyMatch(n -> n == loopEntryPoint)) {
					break;
				}
				if (!nextPhase.getSuccPhase().getEndPoints().parallelStream()
						.anyMatch(n -> internalNodes.parallelStream().anyMatch(iN -> iN.getNode() == n.getNode()))) {
					break;
				}
				thisLength++;
				nextPhase = nextPhase.getSuccPhase();
			}

			if (thisLength > thisOuterLength) {
				thisOuterLength = thisLength;
			}
		}
		if (thisOuterLength > maxLength) {
			maxLength = thisOuterLength;
		}
		return maxLength;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private static int getBarrierCountInNode(IterationStatement itStmt) {
		itStmt = (IterationStatement) Misc.getCFGNodeFor(itStmt);
		int count = 0;
		Set<NodeWithStack> internalNodes = itStmt.getInfo().getCFGInfo()
				.getIntraTaskCFGLeafContentsOfSameParLevel(new CallStack());
		Set<NodeWithStack> internalBarriers = new HashSet<>();
		for (NodeWithStack nodeWithStack : internalNodes) {
			if (nodeWithStack.getNode() instanceof BarrierDirective) {
				internalBarriers.add(nodeWithStack);
				count++;
			}
		}
		/*
		 * Remove all those barriers which move out of the itStmt without moving
		 * over the back-edge.
		 */
		if (itStmt instanceof WhileStatement) {
			Expression predicate;
			WhileStatementCFGInfo info = (WhileStatementCFGInfo) itStmt.getInfo().getCFGInfo();
			predicate = info.getPredicate();
			for (NodeWithStack barrierWithStack : internalBarriers) {
				Set<NodeWithStack> endingElements = new HashSet<>();
				CollectorVisitor.collectNodeSetInGenericGraph(barrierWithStack, endingElements, n -> {
					if (!internalNodes.stream().anyMatch(nS -> nS.getNode() == n.getNode())) {
						return true;
					}
					if (predicate == n.getNode()) {
						return true;
					}
					return false;
				}, nS -> nS.getNode().getInfo().getCFGInfo().getInterProceduralLeafSuccessors(nS.getCallStack()));
				if (!endingElements.stream().anyMatch(nS -> nS.getNode() == predicate)) {
					count--;
				}
			}
		}

		return count;
	}

	/**
	 * Obtains all those phases that either start in this loop, or enter through the
	 * BeginNode and end in this loop.
	 *
	 * @param itStmt
	 *               loop to be processed.
	 *
	 * @return set of all those phases that either start in this loop, or enter
	 *         through the BeginNode and end in this
	 *         loop.
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private static Set<Phase> getPhasesOfIteration(IterationStatement itStmt) {
		Set<Phase> returningSet = new HashSet<>();
		Set<NodeWithStack> internalNodes = itStmt.getInfo().getCFGInfo()
				.getIntraTaskCFGLeafContentsOfSameParLevel(new CallStack());
		if (itStmt instanceof WhileStatement) {
			WhileStatement whileStmt = (WhileStatement) itStmt;
			Expression predicate = whileStmt.getInfo().getCFGInfo().getPredicate();
			for (AbstractPhase<?, ?> absPh : predicate.getInfo().getNodePhaseInfo().getPhaseSet()) {
				Phase ph = (Phase) absPh;
				returningSet.add(ph);
				/*
				 * If this phase does not have any end point that lies within
				 * the loop, then continue.
				 */
				if (!ph.getSuccPhase().getEndPoints().parallelStream()
						.anyMatch(n -> internalNodes.parallelStream().anyMatch(iN -> iN.getNode() == n.getNode()))) {
					continue;
				}

				/*
				 * For this phase, let's try and count the number of reachable
				 * phases that have at least one unique ending barrier in this
				 * loop.
				 */
				Phase nextPhase = ph.getSuccPhase();
				while (nextPhase != null) {
					returningSet.add(nextPhase);
					if (nextPhase.getNodeSet().parallelStream().anyMatch(n -> n == predicate)) {
						break;
					}
					if (!nextPhase.getSuccPhase().getEndPoints().parallelStream().anyMatch(
							n -> internalNodes.parallelStream().anyMatch(iN -> iN.getNode() == n.getNode()))) {
						break;
					}
					nextPhase = nextPhase.getSuccPhase();
				}

			}
		}
		return returningSet;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private static boolean mayDistortEnvironment(CellSet cells, Set<Node> frontiers, IterationStatement itStmt,
			Node aboveNode, Node belowNode) {
		// Set<Node> itContents =
		// itStmt.getInfo().getCFGInfo().getIntraTaskCFGLeafContents();
		//
		// aboveNode = Misc.getCFGNodeFor(aboveNode);
		// belowNode = Misc.getCFGNodeFor(belowNode);
		//
		// if (aboveNode.getInfo().isControlConfined()) {
		// return false;
		// }
		// if (aboveNode.getInfo().getInJumpSources().size() != 0) {
		// return false;
		// }
		// if (aboveNode.getInfo().getOutJumpSources().isEmpty()) {
		// return true;
		// }
		// CellSet belowWrites = new CellSet(belowNode.getInfo().getWrites());
		// for (Node outJumpSrc : aboveNode.getInfo().getOutJumpSources()) {
		// NodeWithStack startPoint = new NodeWithStack(outJumpSrc, new CallStack());
		// Set<NodeWithStack> endPoints = new HashSet<>();
		// for (Cell w : belowWrites) {
		// CollectorVisitor.collectNodeSetInGenericGraph(startPoint, endPoints, (n) -> {
		// CellSet readsOfN = new CellSet(n.getNode().getInfo().getReads());
		// if (readsOfN.contains(w)) {
		// return true;
		// } else {
		// return false;
		// }
		// }, (n) -> {
		// Set<NodeWithStack> retSet = new HashSet<>();
		// for (NodeWithStack succNode :
		// n.getNode().getInfo().getCFGInfo().getInterProceduralLeafSuccessors(n.getCallStack()))
		// {
		// CellSet writesOfN = new CellSet(n.getNode().getInfo().getWrites());
		// if (writesOfN.size() == 1 && writesOfN.contains(w)) {
		// // Skip.
		// } else {
		// retSet.add(succNode);
		// }
		// }
		// return retSet;
		// });
		// if (endPoints.size() != 0) {
		// return true;
		// }
		// }
		// }
		return true;
	}

	/**
	 * Checks if there is any anti-dependence across the back-edge of
	 * {@code itStmt}, for a shared variable.
	 *
	 * @param itStmt
	 *               loop that needs to be tested.
	 *
	 * @return true if there exists an anti-dependence on a shared variable across
	 *         the back-edge of {@code itStmt}.
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private static boolean hasAntiAcrossBackEdge(IterationStatement itStmt) {
		Set<Node> contents = itStmt.getInfo().getCFGInfo().getLexicalCFGLeafContents();
		for (Node node : contents) {
			for (Node antiDest : node.getInfo().getAntiDestinations()) {
				if (!contents.contains(antiDest)) {
					continue;
				}
				if (Misc.getLineNum(node) > Misc.getLineNum(antiDest)) {
					return true;
				} else {
					if (node instanceof Expression) {
						System.err.println(Misc.getLineNum(node) + " " + Misc.getLineNum(antiDest) + " for " + node
								+ " and " + antiDest);
					}
				}
			}
		}
		return false;
	}
}
