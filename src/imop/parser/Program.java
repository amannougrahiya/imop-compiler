/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.parser;

import imop.ast.info.NodeInfo;
import imop.ast.info.RootInfo;
import imop.ast.node.external.*;
import imop.lib.analysis.Assignment;
import imop.lib.analysis.SVEChecker;
import imop.lib.analysis.flowanalysis.*;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis.PointsToGlobalState;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.analysis.mhp.lock.AbstractLock;
import imop.lib.analysis.solver.ConstraintsGenerator;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.transform.percolate.LoopInstructionsRescheduler;
import imop.lib.transform.simplify.FunctionInliner;
import imop.lib.util.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Program {

	// static {
	// Program.loadZ3LibrariesInMac();
	// }

	public enum UpdateCategory {
		EGINV, // eager invalidation, upon each elementary transformation, with rerun of the
		// analysis.
		EGUPD, // eager update, upon each elementary transformation, with incremental update to
		// the analysis data.
		CPINV, // CP-based invalidation
		CPUPD, // CP-based update
		LZINV, // lazy invalidation, involving rerun of the analysis, whenever first read is
		// performed after transformation.
		LZUPD, // lazy update, with incremental update to the analysis data, whenever first
		// read is performed after transformation.
	}

	/**
	 * Various fine-grained stabilization-modes for IDFA stabilization. Note that
	 * all these modes employ lazy triggers; one should ensure that by using "LZUPD"
	 * mode of stabilization. (Further note that the {@link UpdateCategory#LZUPD}
	 * mode of stabilization with ALL_* is similar to the earlier LZINV mode.
	 *
	 * @author aman
	 *
	 */
	public enum StabilizationIDFAMode {
		RESTART, // restarting iterations, SCC-wise; imprecise and fast
		INIT_RESTART, // restarting iterations preceded with initialization, SCC-wise; precise and
		// relatively slower than RETST
		INCIDFA, // our new proposed approach, which is cell-aware.
		ALL_SCC, // reinitializes and recomputes all, SCC-wise
		ALL_NOSCC, // reinitializes and recomputes all, without taking SCCs into consideration
		OLD_INCIDFA // old, cell-unaware IncIDFA; obsolete now.
	}

	public enum CPredAMode {
		H1, // keeping both H2 and H3 disabled in CPredA
		H1H2, // enabling H3 (known to be quite slow when used in absence of H2)
		H1H3, // enabling H2
		H1H2H3 // the default mode for CPredA
	}

	public enum ConcurrencyAlgorithm {
		ICON, YCON
	}

	private static TranslationUnit root;
	public static boolean invalidLineNum;
	public static boolean invalidColumnNum;

	public static boolean isPrePassPhase;
	public static boolean proceedBeyond;
	public static boolean removeUnused = true;
	public static String fileName;
	public static boolean enableUnmodifiability;
	/**
	 * Enables the heuristic that helps in early termination of first phase of
	 * HIDFAp, by marking those nodes as safe that are reachable on a straight path
	 * from a safe node (without any other incoming paths to the path).
	 */
	public static boolean testSafeMarkingHeuristic = false;
	public static boolean countSeededSCCs = false;
	/**
	 * When enabled, SCC-specific parameters will be printed.
	 */
	public static boolean profileSCC = false;
	public static boolean checkForGhostValues = false;
	public static boolean maintainImpactedCells = false;
	public static boolean useNoSCCs = false;
	public static StabilizationIDFAMode stabilizationIDFAMode = StabilizationIDFAMode.INCIDFA;
	public static long timerForMarking = 0;
	/**
	 * Checks whether at least one symbol is required for communication.
	 */
	public static boolean preciseDFDEdges = false;
	/**
	 * Prints side-effects as comments on annotated nodes.
	 */
	public static boolean printSideEffects = false;
	public static boolean printDFDs = true;
	public static boolean printRWinDFDs = false;
	/**
	 * Decides whether intermediate states of the program should be dumped.
	 */
	public static boolean dumpIntermediateStates = false;
	public static boolean printNoFiles = true;
	public static boolean printNoPredicates = true;
	/*
	 * When set, certain checks are performed regarding phase-set of a barrier while
	 * it is being tested for removal.
	 */
	public static boolean phaseEmptySetChecker = false;
	/**
	 * Decided whether the PTA-heuristic that looks at the cell being dereferenced,
	 * is enabled.
	 */
	public static boolean ptaHeuristicEnabled = false;
	public static UpdateCategory idfaUpdateCategory = UpdateCategory.LZUPD;
	public static UpdateCategory mhpUpdateCategory = UpdateCategory.LZUPD;
	/*
	 * Decides whether the local-stabilization heuristic is used with Yuan (where we
	 * simply assume that no changes impact the SVEness of any of the predicates.)
	 * Note that in the absence of any special checks validating our assumption,
	 * this heuristic would not be correct for Yuan's concurrency analysis.
	 */
	public static boolean useHeuristicWithYuan = false;
	public static boolean useHeuristicWithIcon = true;
	public static boolean useInterProceduralPredicateAnalysis = false; // false; the queries have been modified
	// accordingly.
	public static boolean useTimerForIncEPARuns = false;
	public static int secondsForIncEPARuns = 900;

	public static boolean useContextSensitiveQueryResolver = true;

	/**
	 * Set the default SVE sensitivity for the whole program.<br>
	 * Note that IDFA's <i>may</i> have their fixed SVE sensitivity values, defined
	 * in their constructors.
	 */
	public static SVEDimension sveSensitive = SVEDimension.SVE_INSENSITIVE;
	public static SVEDimension sveSensitivityOfIDFAEdges = SVEDimension.SVE_INSENSITIVE;

	public static final StringBuilder stabilizationStackDump = null; // new StringBuilder();

	public static boolean fieldSensitive = false;
	public static int maxThreads;
	public static String z3TimeoutInMilliSecs = "5000";
	private static CellSet cellsThatMayPointToSymbols;
	private static boolean postOrderValid = false;
	private static List<Node> reversePostOrderOfLeaves = new ArrayList<>();
	public static ConcurrencyAlgorithm concurrencyAlgorithm = ConcurrencyAlgorithm.ICON;
	/*
	 * Decides whether all predicates are assumed to be SVE; note that this flag
	 * does not impact the way that the co-existence checks are handled.
	 */
	public static boolean sveNoCheck = true;
	private static Set<Symbol> addressTakenSymbols;
	/*
	 * When set to true, the call-stacks can never contain two call-statements that
	 * have same function-designator node (string-wise).
	 */
	public static boolean oneEntryPerFDInCallStack = true;
	/**
	 * When this flag is set, points-to information is not obtained from the
	 * corresponding flow-facts. Just before running the points-to analysis, this
	 * flag is reset to false, so that the analysis does not get polluted by the
	 * conservative approximations of the base points-to sets.
	 */
	public static boolean basePointsTo = true;
	/**
	 * Just before running any points-to analysis, we should increment this field by
	 * 1, and should decrement it back after the points-to analysis. That way, the
	 * points-to sets will read from the IN flow-fact unless the fixed point is
	 * reached.
	 */
	public static int memoizeAccesses = 0;
	private static boolean initialPhasesRemembered = false;
	public static final long thresholdIDFAProcessingCount = (long) 1e8;
	public static DecimalFormat df2 = new DecimalFormat("#.##");
	public static boolean disableLineNumbers = false;
	public static int numExpansionAllowed = 100; // Default, applicable for the command-line arguments. This is set
	// again in the method {@link defaultCommandLineArguments()}.
	public static final boolean addRelCPs = false; // Default: false; when true, we profile to obtain and print the set
	// of relevant CPs.

	public static CPredAMode cpaMode = CPredAMode.H1H2H3; // The default mode is H1H2H3.
	public static boolean temporaryFlagToDisableSymbolsAtDummyFlushes = false;

	public static void rememberInitialPhasesIfRequired() {
		if (Program.getRoot() == null) {
			return;
		}
		if (!Program.initialPhasesRemembered) {
			Program.initialPhasesRemembered = true;
			for (Node cfgNode : Program.getRoot().getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
				cfgNode.getInfo().getNodePhaseInfo().rememberCurrentPhases();
			}
			// for (ParallelConstruct parConstruct :
			// Misc.getInheritedPostOrderEnclosee(newNode,
			// ParallelConstruct.class)) {
			// for (Phase ph : parConstruct.getInfo().getStableAllPhaseList()) {
			// DataFlowGraph.populateInterTaskEdges(newNode, ph);
			// }
			// }
		}
		return;
	}

	public static boolean checkForCyclesInKeyDependenceGraph = false; // A profiling flag. Disable for normal
	// runs.
	public static boolean useAccessedCellsWithExhaustive = false;
	public static boolean runFirstPartOfFirstPass = true; // Default: true

	public static boolean retainNullCellsInPTAMaps = false; // Decides whether PTA maps of the form {nullCell, a, b} are
	// possible.

	/**
	 * Sets the various defaults for global flags (and filenames), which will be
	 * used in the absence of any command-line arguments. Also, this method
	 * sets/resets some other flags to their initial state.
	 *
	 * @return a string representing the path of the input file.
	 */
	private static String defaultCommandLineArguments() {
		/*
		 * For IncIDFA -->
		 *
		 */
		Program.isPrePassPhase = false; // Leave as false when working with IMOP-preprocessed files.
		Program.removeUnused = true; // Leave as true, to weed out dead definitions and declarations.
		Program.idfaUpdateCategory = UpdateCategory.LZUPD; // Leave as LZUPD. Now we use stabilizationIDFAMode as the
		// flag for IncIDFA results.
		Program.stabilizationIDFAMode = StabilizationIDFAMode.INCIDFA; // The actual mode to be used.
		Program.testSafeMarkingHeuristic = false; // Leave as false. Not needed in the current algorithm.
		Program.profileSCC = false; // To calculate print SCC-processing specific information. Leave as false.
		Program.countSeededSCCs = false; // To calculate and print part of SCC-processing specific information. Leave as
		// false.
		Program.checkForGhostValues = false; // Temporary flag. Leave as false.
		Program.maintainImpactedCells = true; // To calculate and maintain impactedSet.
		Program.useNoSCCs = false; // When false, SCCs are used.
		/*
		 * <-- for IncIDFA.
		 */

		Program.invalidLineNum = false;
		Program.invalidColumnNum = false;
		Program.enableUnmodifiability = false;
		Program.proceedBeyond = true;
		Program.fieldSensitive = false;
		Program.maxThreads = 13;
		Program.z3TimeoutInMilliSecs = "5000"; // 5s timeout for each z3 query.
		Program.oneEntryPerFDInCallStack = true;
		Program.disableLineNumbers = true; // Unless we need line numbers, keep this as true.
		Program.basePointsTo = true;
		Program.memoizeAccesses = 0;
		Program.preciseDFDEdges = false;
		Program.testSafeMarkingHeuristic = false;
		Program.concurrencyAlgorithm = ConcurrencyAlgorithm.YCON;
		Program.mhpUpdateCategory = UpdateCategory.LZUPD; // Default is LZUPD.
		Program.sveSensitive = SVEDimension.SVE_SENSITIVE;
		Program.useContextSensitiveQueryResolver = false;
		Program.cpaMode = CPredAMode.H1H2H3;
		Program.useInterProceduralPredicateAnalysis = false;
		Program.useTimerForIncEPARuns = false;
		Program.useHeuristicWithIcon = true;
		Program.sveSensitivityOfIDFAEdges = SVEDimension.SVE_INSENSITIVE;
		Program.sveNoCheck = true;
		Program.numExpansionAllowed = 100;
		Program.useHeuristicWithYuan = false;
		Program.phaseEmptySetChecker = false;
		Program.ptaHeuristicEnabled = false;

		// Just a safety check below.
		if (Program.concurrencyAlgorithm == ConcurrencyAlgorithm.YCON) {
			Program.mhpUpdateCategory = UpdateCategory.LZINV;
			Program.sveNoCheck = true;
			// Program.idfaUpdateCategory = UpdateCategory.LZUPD;
		}

		String filePath = "";
		// filePath = ("../tests/classB-preproc/bt-b.i");
		// filePath = ("../tests/classB-preproc/cg-b.i");
		// filePath = ("../tests/classB-preproc/ep-b.i");
		// filePath = ("../tests/classB-preproc/ft-b.i");
		// filePath = ("../tests/classB-preproc/is-b.i");
		// filePath = ("../tests/classB-preproc/lu-b.i");
		// filePath = ("../tests/large-preproc/mg-c.i");
		// filePath = ("../tests/classB-preproc/sp-b.i");
		//
		// filePath = ("../output-dump/ft-bimop_output_LZINC.i");
		filePath = ("../tests/npb-post/bt3-0.i");
		// filePath = ("../tests/npb-post/cg3-0.i");
		filePath = ("../tests/npb-post/ep3-0.i");
		// filePath = ("../tests/npb-post/ft3-0.i");
		// filePath = ("../tests/npb-post/is3-0.i");
		// filePath = ("../tests/is-small.i");
		filePath = ("../tests/npb-post/lu3-0.i");
		filePath = ("../tests/npb-post/mg3-0.i");
		// filePath = ("../tests/npb-post/sp3-0.i");
		//
		// filePath = ("../tests/quake-postpass.i");
		// filePath = ("../tests/amgmk-postp
		// filePath = ("../tests/amgmk-small.i");
		// filePath = ("../tests/clomp-postpass.i");
		// filePath = ("../tests/stream-postpass.i");
		// filePath = ("../tests/scanner-postpass.i");
		//
		// filePath = "../output-dump/imop_useful.i";
		// filePath = ("../src/imop/lib/testcases/cfgTests/singleLooping.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/criticalOne.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/forStatement.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/allCFG.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/allCFG.i");
		// filePath = ("../src/imop/lib/testcases/traversals/traversalIntraTask.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/functionCalls.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/definitions.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/switchStatement.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/continueStatement.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/livenessCheck.c");
		// filePath = ("../src/imop/lib/testcases/reachingDefsTest/loopMore.c");
		// filePath = ("../src/imop/lib/testcases/reachingDefsTest/rdTest1.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/small1.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/whileStatement.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/atomicConstructSimple.c");
		// filePath = ("../src/imop/lib/testcases/cfgTests/proc.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/whilePeeler1.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/barr-check.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/silo-check.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/barrDownFork.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/sveTest1.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/simplifyPredicate.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/simplifyPredicate.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/mhpTest1.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/mhpTest10.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/mhpTest9.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/mhpTest11.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/test1.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/mhpTest8.c");
		// filePath = ("../src/imop/lib/testcases/rwTest/mhpTest9.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/mhpTest3.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/fenceIssue.c");
		// filePath = ("../src/imop/lib/testcases/mhpTests/wrongPercolation.c");
		// filePath = ("../src/imop/lib/testcases/pointers/pointerTest1.c");
		// filePath = ("../src/imop/lib/testcases/test83.c");
		// filePath = ("../src/imop/lib/testcases/test33.i");
		// filePath = ("../src/imop/lib/testcases/test84.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/copyCheck1.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/copysplit.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/copy-meet.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/postCallFlowIssue.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/voidParamTest.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/ampersandCopy.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/idfaUpdate.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/swapCheck.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/swapCheck2.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/singleFlowConflict.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/updateSwap.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/multiRDSwap.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/flowIssueSwap.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/test14.c");
		// filePath = ("../src/imop/lib/testcases/dataTest/test13.c");
		// filePath = ("../src/imop/lib/testcases/parsing/test74.c");
		// filePath = ("../src/imop/lib/testcases/parsing/test14.c");
		// filePath = ("../src/imop/lib/testcases/parsing/test81.c");
		// filePath = ("../src/imop/lib/testcases/parsing/headerMac.i");
		// filePath = ("../src/imop/lib/testcases/parsing/headerK2.i");
		// filePath = ("../src/imop/lib/testcases/parsing/test81.c");
		// filePath = ("../src/imop/lib/testcases/parsing/temporarySnippets.i");
		// filePath = ("../src/imop/lib/testcases/simplification/scoping.c");
		// filePath = ("../src/imop/lib/testcases/simplification/cs-enforcer.c");
		// filePath = ("../src/imop/lib/testcases/simplification/inline1.c");
		// filePath = ("../src/imop/lib/testcases/simplification/expand1.c");
		// filePath =
		// ("../src/imop/lib/testcases/simplification/implicitBarrierIssue.c");
		// filePath = ("../src/imop/lib/testcases/simplification/token-test1.c");
		// filePath = ("../src/imop/lib/testcases/simplification/token-test2.c");
		// filePath = ("../src/imop/lib/testcases/simplification/token-test3.c");
		// filePath = ("../src/imop/lib/testcases/simplification/type-conversion.c");
		// filePath = ("../src/imop/lib/testcases/simplification/selectionSwap.c");
		// filePath = ("../src/imop/lib/testcases/simplification/or.c");
		// filePath = ("../src/imop/lib/testcases/simplification/test7.c");
		// filePath = ("../src/imop/lib/testcases/simplification/test8.c");
		// filePath = ("../src/imop/lib/testcases/simplification/test9.c");
		// filePath = ("../src/imop/lib/testcases/simplification/test10.c");
		// filePath = ("../src/imop/lib/testcases/simplification/test11.c");
		// filePath = ("../src/imop/lib/testcases/traversals/test12.c");
		// filePath = ("../src/imop/lib/testcases/traversals/test13.c");
		// filePath = ("../src/imop/lib/testcases/simplification/fptr2.c");
		// filePath = ("../src/imop/lib/testcases/higher/succ/SwitchPredicateLink.c");
		// filePath = ("../src/imop/lib/testcases/simplification/test12.c");
		// filePath = ("../src/imop/lib/testcases/simplification/test0.c");
		// filePath = ("../src/imop/lib/testcases/simplification/fptr.c");
		// filePath = ("../src/imop/lib/testcases/simplification/morePtr.c");
		// filePath = ("../src/imop/lib/testcases/simplification/manyPtr.c");
		// filePath = ("../src/imop/lib/testcases/simplification/parentheses.c");
		// filePath = ("../src/imop/lib/testcases/simplification/struct.c");
		// filePath = ("../src/imop/lib/testcases/simplification/nestedType.c");
		// filePath = ("../src/imop/lib/testcases/simplification/test1.c");
		// filePath = ("../src/imop/lib/testcases/simplification/test2.c");
		// filePath = ("../src/imop/lib/testcases/rwTest/simple.c");
		// filePath = ("../src/imop/lib/testcases/test83.c");
		// filePath = ("../src/imop/lib/testcases/array_addition.c");
		// filePath = ("../src/imop/lib/testcases/solverTests/test1.c");
		// filePath = ("../src/imop/lib/testcases/solverTests/test2.c");
		// filePath = ("../src/imop/lib/testcases/solverTests/test4.c");
		// filePath = ("../src/imop/lib/testcases/solverTests/test5.c");
		//
		// filePath = ("../tests/parboil/histo.i");
		// filePath = ("../tests/parboil/stencil.i");
		// filePath = ("../tests/parboil/tpacf.i");
		// filePath = ("../tests/test-mhp.c");
		// filePath = ("../tests/testZ3.c");
		// filePath = ("../tests/ocean/input.i");
		// filePath = ("../tests/cs6868-ocean/ocean6868.i");
		// filePath = ("../tests/sea.i");
		// filePath = ("../tests/testocean.c");
		// filePath = ("../tests/auto-normalize.c");
		// filePath = ("../tests/simplify.c");
		// filePath = ("../tests/enforcer.c");
		// filePath = ("../tests/en.c") i;
		// filePath = ("../tests/internal.c");
		// filePath = ("../tests/cfg-rem.c");
		// filePath = ("../tests/predicate.c");
		// filePath = ("../tests/strassen.i");
		// filePath = ("../tests/ocean6868-k2.i");
		// filePath = ("../tests/ocean6868-mini.i");
		// filePath = ("../tests/ocean6868-virgo.i");
		// filePath = ("../tests/mdljcell.i");
		// filePath = ("../tests/barr-opt-tests/heated_plate_openmp.i");
		// filePath = ("../tests/heated_plate_pointer.i");
		// filePath = ("../tests/heated_plate_pointer-merged-swapped.i");
		// filePath = ("../tests/temp.i");
		// filePath = ("../tests/temp2.i");
		// filePath = ("../tests/temp3.i");
		// filePath = ("../tests/temp4.i");
		// filePath = ("../tests/simple.c");
		// filePath = ("../tests/foo.c");
		// filePath = ("../tests/foo1.c");
		// filePath = ("../tests/bt-long.c");
		// filePath = ("../tests/poisson_openmp-while.i");
		// filePath = ("../tests/poisson-temp.i");
		// filePath = ("../tests/poisson_openmp-while-merged.i");
		// filePath = ("../tests/output.i");
		// filePath = ("../tests/ocean/k2Input.i");
		// filePath = ("../tests/for-func.i");
		// filePath = ("../tests/null-final.i");
		// filePath = ("../tests/if-func.c");
		// filePath = ("../tests/type-long.c");
		// filePath = ("../tests/structDef.c");
		// filePath = ("../tests/scanner.i");
		// filePath = ("../tests/struct-issue.c");
		// filePath = ("../tests/atomic-test.c");
		// filePath = ("../tests/ocean/kinst-k2input.i");
		// filePath = ("../tests/dijkstra_openmp.i");
		// filePath = ("../tests/barr-opt-tests/jacobi-1d-imper.i");
		// filePath = ("../tests/jacobi-1d-imper-postpass.i");
		// filePath = ("../tests/dijkstra_while_openmp.i");
		// filePath = ("../tests/jacobi-while.i");
		// filePath = ("../runner/cgo-eg/example.c");
		// filePath = ("../tests/ziggurat_openmp.i");
		// filePath = ("../tests/sgefa_openmp.i");
		// filePath = ("../tests/fft_openmp.i");
		// filePath = ("../tests/quake.i");
		// filePath = ("../tests/sequoia/clomp.i");
		// filePath = ("../tests/tt.i");
		// filePath = ("../tests/fsu/md_openmp.i");
		// filePath = ("../tests/alternate.i");
		// filePath = ("../tests/parboil/lbm.i");
		// filePath = ("../tests/insert.i");
		// filePath = ("../tests/minebench/kmeans.i");
		// filePath = ("../tests/barr-opt-tests/c_jacobi01.i");
		// filePath = ("../tests/barr-opt-tests/c_jacobi03.i");
		// filePath = ("../tests/barr-opt-tests/c_md.i");
		// filePath = ("../tests/barr-opt-tests/histo.i");
		// filePath = ("../tests/barr-opt-tests/dijkstra_openmp.i");
		// filePath = ("../tests/c_jacobi03-postpass.i");
		// filePath = ("../tests/c_jacobi01-postpass.i");
		// filePath = ("../tests/barr-opt-tests/adi.i");
		// filePath = ("../tests/barr-opt-tests/amgmk.i");
		// filePath = ("../tests/icon-tests/smithwa.i");
		// filePath = ("../tests/barr-opt-tests/kmeans.i");
		// filePath = ("../tests/kmeans-small.i");
		// filePath = ("../tests/barr-opt-tests/clomp.i");
		// filePath = ("../tests/barr-opt-tests/stream.i");
		// filePath = ("../tests/stream-postpass.i");
		// filePath = ("../tests/barr-opt-tests/quake.i");
		// filePath = ("../src/imop/lib/testcases/allKnown.c");
		// filePath = ("../output-dump/imop_output.i");

		/* New Benchmarks */
		// filePath = ("../tests/icon-tests/smithwa.i"); // Full length benchmark code;
		// usable.
		// filePath = ("../tests/icon-tests/nab.i"); // Full length benchmark code;
		// filePath = ("../tests/icon-tests/ammp.i"); // Full length benchmark code
		// filePath = ("../tests/ammp-useful.i"); // Full length benchmark code
		// filePath = ("../tests/icon-tests/hop.i"); // Full length benchmark code
		// filePath = ("../tests/hop-useful.i");
		// filePath = ("../tests/icon-tests/scalparc.i"); // Full length benchmark code
		return filePath;
	}

	/**
	 * Given the string of command-line arguments as input, this method, firstly,
	 * sets the default values for various global flags. These default values are
	 * then overridden by the flags specified in the command-line arguments. After
	 * setting of the flags, this method transfers the control the
	 * {@link FrontEnd#parseAndNormalize(java.io.InputStream)} which parses the
	 * input file.
	 *
	 * @param args
	 */
	public static void parseNormalizeInput(String[] args) {
		String filePath = Program.defaultCommandLineArguments();
		// Next loop overrides the defaults, if any command-line arguments are provided.
		int index = 0;
		for (String str : args) {
			if (str.equals("--prepass") || str.equals("-p")) {
				Program.isPrePassPhase = true;
				Program.proceedBeyond = false;
			} else if (str.equals("--noPrepass") || str.equals("-np")) {
				Program.isPrePassPhase = false;
			}
			if (str.equals("--removeUnused") || str.equals("-ru")) {
				Program.removeUnused = true;
			} else if (str.equals("--noRemoveUnused") || str.equals("-nru")) {
				Program.removeUnused = false;
			}
			if (str.equals("--printNoFiles") || str.equals("-pnf")) {
				Program.printNoFiles = true;
			}
			if (str.equals("--sveSensitive") || str.equals("-sve")) {
				Program.sveSensitive = SVEDimension.SVE_SENSITIVE;
			} else if (str.equals("--noSveSensitive") || str.equals("-nsve")) {
				Program.sveSensitive = SVEDimension.SVE_INSENSITIVE;
			}
			if (str.equals("--fieldSensitive") || str.equals("-fs")) {
				Program.fieldSensitive = true;
			} else if (str.equals("--noFieldSensitive") || str.equals("-nfs")) {
				Program.fieldSensitive = false;
			}
			if (str.equals("--file") || str.equals("-f")) {
				filePath = args[index + 1];
			}
			if (str.equals("--disableLineNumbering") || str.equals("-dln")) {
				Program.disableLineNumbers = true;
			}
			if (str.equals("--yuan") || str.equals("-yuan")) {
				Program.concurrencyAlgorithm = ConcurrencyAlgorithm.YCON;
				Program.sveSensitive = SVEDimension.SVE_SENSITIVE;
				Program.sveSensitivityOfIDFAEdges = SVEDimension.SVE_SENSITIVE;
			}
			if (str.equals("--accessedExhaustive") || str.equals("-ae")) {
				Program.useAccessedCellsWithExhaustive = true;
			}
			if (str.equals("--noAccessedExhaustive") || str.equals("-nae")) {
				Program.useAccessedCellsWithExhaustive = false;
			}
			if (str.equals("--icon") || str.equals("-icon")) {
				Program.concurrencyAlgorithm = ConcurrencyAlgorithm.ICON;
			}
			if (str.equals("--intra-cp") || str.equals("-icp")) {
				Program.useInterProceduralPredicateAnalysis = false;
			}
			if (str.equals("-h1h2h3")) {
				Program.cpaMode = CPredAMode.H1H2H3;
			} else if (str.equals("-h1h2")) {
				Program.cpaMode = CPredAMode.H1H2;
			} else if (str.equals("-h1h3")) {
				Program.cpaMode = CPredAMode.H1H3;
			} else if (str.equals("-h1")) {
				Program.cpaMode = CPredAMode.H1;
			}
			if (str.equals("--idfaMode") || str.equals("-im")) {
				String next = args[index + 1];
				Program.stabilizationIDFAMode = StabilizationIDFAMode.valueOf(next);
				if (Program.stabilizationIDFAMode == null) {
					System.out.println("Please use a valid identifier for IDFA stabilization mode: "
							+ StabilizationIDFAMode.values());
					System.out.println("Read the documentation for more details. Exiting..");
					System.exit(0);
				}
			}
			if (str.equals("--category") || str.equals("-c")) {
				String next = args[index + 1];
				try {
					Program.idfaUpdateCategory = UpdateCategory.valueOf(next);
				} catch (Exception e) {
					System.out.println("Please use a valid identifier for IDFA update category: "
							+ Arrays.asList(UpdateCategory.values()));
					System.out.println("Read the documentation for more details. Exiting..");
					System.exit(0);
				}
			}
			if (str.equals("--categoryMHP") || str.equals("-cm")) {
				String next = args[index + 1];
				try {
					Program.mhpUpdateCategory = UpdateCategory.valueOf(next);
				} catch (Exception e) {
					System.out.println(
							"Please use a valid identifier for MHP update category: EGINV, LZINV, EGUPD, or LZUPD.");
					System.out.println("Read the documentation for more details. Exiting..");
					System.exit(0);
				}
			}
			if (str.equals("--version") || str.equals("-v")) {
				System.out.println("IMOP Pre-Release, Version 0.0");
				System.exit(0);
			}
			index++;
		}

		// Attempt to open the input stream for the specified file.
		try {
			if (filePath.isEmpty()) {
				throw new FileNotFoundException();
			}
			if (filePath.contains("/")) {
				String fullFileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
				Program.fileName = fullFileName.substring(0, fullFileName.lastIndexOf("."));
			} else {
				Program.fileName = filePath.substring(0, filePath.lastIndexOf("."));
			}
			System.setIn(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			if (filePath.isEmpty()) {
				System.out.println("Please specify the input file.");
				System.out.println("usage: java <jvmoptions> imop.Main [--prepass | -p | --noPrePass | --npe] "
						+ "[--removeUnused | -ru | --noRemoveUnused | --nru] "
						+ "[--sveSensitive | -sve | --noSveSensitive | --nsve] "
						+ "[--fieldSensitive | -fs | --noFieldSensitive | -- nfs] " + "--file|-f <input-file>");
			}
			System.out.println(filePath + " is not a valid file path.");
			e.printStackTrace();
			System.exit(0);
		}

		// Ensure that the output folder exists.
		File folder = new File("output-dump");
		if (!folder.exists()) {
			System.err.println("Could not find the output folder imop-compiler/output-dump. Creating one..");
			if (!folder.mkdirs()) {
				System.err.println("Could not create an empty folder imop-compiler/output-dump. \n"
						+ "Please create it manually and retry.");
				System.exit(0);
			}
		}

		// Finally, call the parser and normalization pass for the input program.
		System.err.println("===== Processing the file " + Program.fileName + " =====");
		FrontEnd.parseAndNormalize(System.in);
		System.err.flush();
	}

	/**
	 * Reset static fields of all the classes while changing the program's root.
	 */
	public static void resetGlobalStaticFields() {
		System.err.println("\n*** NOTE: Resetting (almost) all the static fields...\n");
		Program.invalidLineNum = Program.invalidColumnNum = true;
		Program.cellsThatMayPointToSymbols = null;
		Program.postOrderValid = false;
		Program.addressTakenSymbols = null;
		Program.basePointsTo = true;
		Program.initialPhasesRemembered = false;
		NodeInfo.readWriteDestinationsSet = false;
		NodeInfo.resetFirstRunFlags();
		RootInfo.functionListQueryTimer = 0;
		CFGInfo.resetStaticFields();
		Symbol.allSymbols = new ArrayList<>();
		HeapCell.resetStaticFields();
		Cell.resetStaticFields();
		CellCollection.allDerivedCollections.clear();
		FlowAnalysis.resetStaticFields();
		PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.NOT_INIT;
		AbstractLock.resetStaticFields();
		AbstractPhasePointable.resetStaticFields();
		Phase.resetStaticFields();
		SVEChecker.resetStaticFields();
		ConstraintsGenerator.resetStaticFields();
		FunctionInliner.resetStaticFields();
		LoopInstructionsRescheduler.resetStaticFields();
		TraversalOrderObtainer.resetStaticFields();
		Misc.resetStaticFields();
	}

	private static void loadZ3LibrariesInMac() {
		String osName = System.getProperty("os.name");
		if (!osName.startsWith("Mac")) {
			String javaLibPath = System.getProperty("java.library.path");
			String suggestion = "Ensure that java.library.path contains the " + "path to native libraries of Z3.";
			if (javaLibPath.isEmpty()) {
				Misc.exitDueToError(suggestion);
			} else {
				System.err.println("Note: " + suggestion);
			}
			return;
		}
		String[] command = { "bash", "-c",
		"source ~/.bash_profile; source ~/.bashrc; source ~/.profile; echo $Z3HOME" };
		try {
			Process proc = Runtime.getRuntime().exec(command);
			proc.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while (reader.ready()) {
				String z3Home = reader.readLine();
				if (z3Home.isEmpty()) {
					Misc.exitDueToError("Could not read the environment variable $Z3HOME.");
				}
				String z3LibPath = z3Home + "build/";
				System.err.println("Expecting z3 libraries to be at " + z3LibPath + " ...");
				System.load(z3LibPath + "libz3java.dylib");
				System.load(z3LibPath + "libz3.dylib");
				break;
			}
			System.err.println("Successfully loaded Z3 native libraries.");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static TranslationUnit getRoot() {
		if (root == null) {
			Misc.warnDueToLackOfFeature(
					"Could not find any program to work with! You may get a NullPointerException sometime soon...",
					null);
		}
		return root;
	}

	public static void setRoot(TranslationUnit root) {
		Program.root = root;
	}

	public static void invalidateCellsThatMayPointToSymbols() {
		Program.cellsThatMayPointToSymbols = null;
	}

	public static void invalidateReversePostorder() {
		Program.postOrderValid = false;
	}

	public static void invalidateAddressTakenSet() {
		Program.addressTakenSymbols = null;
	}

	public static void stabilizeReversePostOrderOfLeaves() {
		if (Program.postOrderValid) {
			return;
		}
		Program.postOrderValid = true;
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main == null) {
			Misc.warnDueToLackOfFeature("Cannot obtain reverse postorder due to missing main method.", null);
			return;
		} else {
			Node beginNode = main.getInfo().getCFGInfo().getNestedCFG().getBegin();
			List<DFable> reversePostOrder = TraversalOrderObtainer.obtainReversePostOrder(beginNode,
					n -> n.getDataFlowSuccessors());
			int i = 0;
			for (DFable dfN : reversePostOrder) {
				dfN.setReversePostOrder(i++);
			}
		}
	}

	/**
	 * Obtain a read-only list which represents a reverse postordering on the leaves
	 * of the program.
	 *
	 * @return list representing reverse postordering on the leaves of the program.
	 */
	@Deprecated
	public static void deprecated_stabilizeReversePostOrderOfLeaves() {
		if (Program.postOrderValid) {
			return;
		}
		for (Node n : Program.reversePostOrderOfLeaves) {
			n.getInfo().setReversePostOrderId(-1);
		}
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main == null) {
			Misc.warnDueToLackOfFeature("Cannot obtain reverse postorder due to missing main method.", null);
			return;
		} else {
			Node beginNode = main.getInfo().getCFGInfo().getNestedCFG().getBegin();
			List<Node> reversePostOrderOfLeaves = TraversalOrderObtainer.obtainReversePostOrder(beginNode,
					// n -> n.getInfo().getCFGInfo().getInterProceduralLeafSuccessors());
					n -> n.getInfo().getCFGInfo().getInterTaskLeafSuccessorNodes());
			Program.reversePostOrderOfLeaves = reversePostOrderOfLeaves;
			int i = 0;
			for (Node n : reversePostOrderOfLeaves) {
				n.getInfo().setReversePostOrderId(i++);
			}
			Program.postOrderValid = true;
		}
	}

	/**
	 * Obtain a read-only list which represents a reverse postordering on the leaves
	 * of the program.
	 *
	 * @return list representing reverse postordering on the leaves of the program.
	 */
	@Deprecated
	public static List<Node> getUnmodifiableReversePostOrderOfLeaves() {
		if (Program.reversePostOrderOfLeaves == null) {
			FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
			if (main == null) {
				Misc.warnDueToLackOfFeature("Cannot obtain reverse postorder due to missing main method.", null);
				return new ArrayList<>();
			}
			Node beginNode = main.getInfo().getCFGInfo().getNestedCFG().getBegin();
			Program.reversePostOrderOfLeaves = TraversalOrderObtainer.obtainReversePostOrder(beginNode,
					n -> n.getInfo().getCFGInfo().getInterTaskLeafSuccessorNodes());
			int i = 0;
			for (Node n : Program.reversePostOrderOfLeaves) {
				n.getInfo().setReversePostOrderId(i++);
			}
			Program.postOrderValid = true;
		}
		if (Program.enableUnmodifiability) {
			return Collections.unmodifiableList(Program.reversePostOrderOfLeaves);
		} else {
			return Program.reversePostOrderOfLeaves;
		}
	}

	public static CellSet getCellsThatMayPointToSymbols() {
		if (Program.cellsThatMayPointToSymbols == null) {
			cellsThatMayPointToSymbols = Program.getCellsWithSymbolPointees();
		}
		return cellsThatMayPointToSymbols;
	}

	public static Set<Symbol> getAddressTakenSymbols() {
		if (Program.addressTakenSymbols == null) {
			// Populate.
			Set<Symbol> retSet = new HashSet<>();
			TranslationUnit root = getRoot();
			FunctionDefinition mainFunc = root.getInfo().getMainFunction();
			if (mainFunc == null) {
				return retSet;
			}
			// List<Assignment> assignList =
			// mainFunc.getInfo().getInterProceduralAssignments();
			// for (Assignment assign : assignList) {
			// CellSet rhsLocs = assign.getRHSLocations();
			// CellSet lhsLocs = assign.getLHSLocations();
			// for (Cell rhs : rhsLocs) {
			// if (rhs instanceof AddressCell) {
			// changed |= retSet.addAll(lhsLocs);
			// } else if (rhs instanceof Symbol) {
			// if (retSet.contains(rhs)) {
			// changed |= retSet.addAll(lhsLocs);
			// }
			// }
			// }
			// }
			Program.addressTakenSymbols = retSet;
		}
		return Program.addressTakenSymbols;
	}

	private static boolean disableCellPointees = true;

	/**
	 * Obtain a set of all those cells that may point to a symbol.
	 *
	 * @return set of all those cells that may point to a symbol.
	 */
	private static CellSet getCellsWithSymbolPointees() {
		CellSet retSet = new CellSet();
		if (disableCellPointees) {
			retSet.add(Cell.genericCell);
			return retSet;
		}
		TranslationUnit root = getRoot();
		FunctionDefinition mainFunc = root.getInfo().getMainFunction();
		if (mainFunc == null) {
			return retSet;
		}
		List<Assignment> assignList = mainFunc.getInfo().getInterProceduralAssignments();
		boolean changed;
		do {
			changed = false;
			for (Assignment assign : assignList) {
				CellSet rhsLocs = assign.getRHSLocations();
				CellSet lhsLocs = assign.getLHSLocations();
				for (Cell rhs : rhsLocs) {
					if (rhs instanceof AddressCell) {
						changed |= retSet.addAll(lhsLocs);
					} else if (rhs instanceof Symbol) {
						if (retSet.contains(rhs)) {
							changed |= retSet.addAll(lhsLocs);
						}
					}
				}
			}
		} while (changed);
		return retSet;
	}

	@Deprecated
	public static String getAllIncludes() {
		String retString = "";
		boolean foundEnd = false;
		for (Node elemNode : Program.getRoot().getF0().getNodes()) {
			ElementsOfTranslation elemTr = (ElementsOfTranslation) elemNode;
			Node internal = elemTr.getF0().getChoice();
			if (internal instanceof UnknownCpp && internal.toString().startsWith("# include")
					|| internal.toString().startsWith("#include")) {
				if (foundEnd) {
					return null;
				}
				retString += internal;
			} else {
				System.err.println("Code begins at: ");
				System.err.println(internal.toString().substring(0, 15));
				foundEnd = true;
				return retString;
			}
		}
		return retString;
	}
}
