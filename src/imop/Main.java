/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop;

import com.microsoft.z3.*;
import imop.ast.annotation.Label;
import imop.ast.info.cfgNodeInfo.ForConstructInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.Assignment;
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.analysis.SVEChecker;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Definition;
import imop.lib.analysis.flowanalysis.HeapCell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.analysis.solver.ConstraintsGenerator;
import imop.lib.analysis.solver.FieldSensitivity;
import imop.lib.analysis.solver.SeedConstraint;
import imop.lib.analysis.solver.SyntacticAccessExpression;
import imop.lib.analysis.solver.tokens.ExpressionTokenizer;
import imop.lib.analysis.solver.tokens.IdOrConstToken;
import imop.lib.analysis.solver.tokens.OperatorToken;
import imop.lib.analysis.solver.tokens.Tokenizable;
import imop.lib.analysis.typeSystem.SignedIntType;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.getter.LoopAndOmpProfiler;
import imop.lib.transform.BarrierCounterInstrumentation;
import imop.lib.transform.BasicTransform;
import imop.lib.transform.CopyEliminator;
import imop.lib.transform.percolate.DriverModule;
import imop.lib.transform.percolate.FencePercolationVerifier;
import imop.lib.transform.percolate.LoopInstructionsRescheduler;
import imop.lib.transform.percolate.UpwardPercolater;
import imop.lib.transform.simplify.DeclarationEscalator;
import imop.lib.transform.simplify.FunctionInliner;
import imop.lib.transform.simplify.ParallelConstructExpander;
import imop.lib.transform.simplify.RedundantSynchronizationRemoval;
import imop.lib.transform.updater.*;
import imop.lib.util.*;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.io.FileNotFoundException;
import java.util.*;

@SuppressWarnings("unused")
public class Main {

	public static long totalTime;

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		// Thread.sleep(7000);
		totalTime = System.nanoTime();
		Program.parseNormalizeInput(args);

		// demo1("L1");
		// demo2();
		// demo3();
		// demo4();
		// demo5ab();
		// demo6();
		// demo7();
		// demo8();
		// demo9();
		// demo11();
		// System.exit(0);
		// DumpSnapshot.dumpRoot("Hello");
		// ConcurrencyAnalysis.printBarrierTrees();
		// ConcurrencyAnalysis.verifyForMVEs();
		// YuanConcurrencyAnalysis.performMHPByYuan();
		// System.exit(0);

		// FunctionDefinition funcDef = Program.getRoot().getInfo().getMainFunction();
		// Statement oldStmt = funcDef.getInfo().getStatementWithLabel("j");
		// Statement newStmt =
		// Program.getRoot().getInfo().getFunctionWithName("foo").getInfo().getStatementWithLabel("w");
		// NodeReplacer.replaceNodes(oldStmt, newStmt);
		// DumpSnapshot.dumpPhases("tester");
		// System.exit(0);

		// Main.dumpReachingDefinitions("");
		// Main.dumpCopyInfo("");
		// dumpReadSources();
		// dumpWriteSources();
		// dumpReadDestinations();
		// dumpWriteDestinations();
		// dumpLockSet();
		// dumpDataDependence();
		// dumpDominatorInfo("");
		// dumpPhases("");
		// dumpHeapValidity();
		// dumpLiveness();
		// dumpPointsTo("");
		// dumpVisibleSharedReadWrittenCells("");
		// dumpPhaseAndCopyInfo("");
		// dumpAccessibleCells("");
		// dumpLoopHeadersOnSTDOUT();
		// ********* Those methods that never return.*********
		// DriverModule.askQueries();
		DriverModule.clientAutoUpdateHomeostasis();
		// DriverModule.mergeParRegs();
		// DriverModule.optimizeBarriers();
		// Main.testNodeShifting();
		// Main.testNormalization();
		// Misc.extractSVEPragmas();
		// testReversePostOrder();
		// Z3Solver.test3();
		// testPA_Sample();
		// testPA_A4();
		// testLabels();
		// testDriverAndHeuristic();
		// testFieldSensitivity();
		// testExpressionTokenizer();
		// temporaryChecker1();
		// temporaryChecker2();
		// temporaryChecker3();
		// testValueConstraintsGenerator();
		// testPointerDereferenceGetter();
		// testBaseExpressionGetter();
		// testZ3ExpressionCreator();
		// testSwapLength();
		// testIDFAUpdate();
		// checkSVEAnnotations();
		// testNoNullParent();
		// testReachableUses();
		// testCopyElimination();
		// testCopyEliminationFromLoops();
		// testUnreachables();
		// testCopyDetector();
		// debugDeclarationEscalator(Misc.getAnyElement(Misc.getInheritedEnclosee(Program.root,
		// WhileStatement.class)));
		// testSVEness();
		// testCoExistence();
		// testNodeReplacedString();
		// checkHighLevelCFGTransformations();
		// testLCACS();
		// testClauses();
		// profileStructure1();
		// profileStructure2();
		// testPhaseInWhile();
		// testPhaseSwaps();
		// testPhaseAdditions();
		// testPhaseRemovals();
		// testInlining();
		// testParConsExpansionAndFusion();
		// testBarrierPercolation();
		// testBarrierCounterInstrumentation();
		// testFencePercolation();
		// removeBarriers();

		// ********* End of those methods that never return. *********
		System.exit(0);

		/*
		 * Testing the variable-lengthed arrays.
		 */
		// for (FunctionDefinition funcDef :
		// Program.root.getInfo().getAllFunctionDefinitions()) {
		// if (funcDef.getInfo().getFunctionName().contains("aman")) {
		// System.out.println("Found.");
		// for (Scopeable scope : funcDef.getInfo().getEnclosedScopesInclusive()) {
		// if (scope instanceof CompoundStatement) {
		// HashMap<String, Symbol> symbolTable = ((CompoundStatement)
		// scope).getInfo().getSymbolTable();
		// for (String s : symbolTable.keySet()) {
		// Symbol sym = symbolTable.get(s);
		// System.out.println(sym);
		// if (sym.getType() instanceof ArrayType) {
		// ArrayType t = (ArrayType) sym.getType();
		// System.out.println(sym.getDeclaringNode() + " is " +
		// t.isKnownConstantSized());
		// }
		// }
		// }
		// }
		// }
		// }
		// System.exit(0);

		/*
		 * Testing ends, for variable-lengthed arrays.
		 */

		/*
		 * Testing the collector visitor
		 */
		// Set<Node> startSet = new HashSet<>();
		// Node beginNode =
		// Main.root.getInfo().getMainFunction().getInfo().getCFGInfo().getNestedCFG().getBegin();
		// Node endNode =
		// Main.root.getInfo().getMainFunction().getInfo().getCFGInfo().getNestedCFG().getEnd();
		// startSet.add(beginNode);
		// System.out.println(CollectorVisitor.collectNodesForward(startSet, null,
		// doesntMatter ::= false).stream()
		// .filter((node) ::= !(node instanceof BeginNode || node instanceof
		// EndNode)).count());
		// System.out.println(
		// CollectorVisitor.collectNodesForward(beginNode, new HashSet<>(), doesntMatter
		// ::= false).size());
		// System.out.println(
		// CollectorVisitor.collectNodesBackward(endNode, new HashSet<>(), doesntMatter
		// ::= false).size());
		// System.out.println(CollectorVisitor.collectNodesForwardContextSensitive(
		// new NodeWithStack(beginNode, new CallStack()), new HashSet<>(), doesntMatter
		// ::= false).size());
		// System.out.println(CollectorVisitor.collectNodesBackwardContextSensitive(
		// new NodeWithStack(endNode, new CallStack()), new HashSet<>(), doesntMatter
		// ::= false).size());
		/*
		 * End of test for the collector visitor.
		 */

		// String inFileName = "tests/ocean/imopInputFirst.i";
		// Misc.printToFile(root, inFileName);
		// Setup.processParallelism(root);
		// changed = SyncOptimizationDriver.optimizeProgram();
		// String outFileName = "tests/ocean/imopOutput.i";
		// Misc.printToFile(root, outFileName);
		// inputName = outFileName;
		// boolean changed;
		// int max = 0;
		// do {
		// changed = false;
		// System.setIn(new FileInputStream(inputName));
		// root = Setup.newFrontEnd(System.in);
		//// root = Setup.frontEnd(System.in);
		// Setup.newProcessParallelism(root);
		// changed = SyncOptimizationDriver.optimizeProgram();
		// outFileName = "tests/ocean/imopOutput.i";
		// Misc.printToFile(root, outFileName);
		// inputName = outFileName;
		// max++;
		// } while (changed && max < 100);
		// System.exit(0);
		//
		// System.setIn(new FileInputStream("tests/ocean/inputWithPrints.i"));
		// root = Setup.frontEnd(System.in);
		// Misc.printToFile(root, "imop_input.i");
		// Uncomment till here.
		// /////////////////////////////////////////////////////////////////////////////
		// for (Node node : root.getInfo().getCFGNodes()) {
		// if (Misc.isCFGLeafNode(node)) {
		// SymbolList readSymbols = node.getInfo().getReads();
		// if (readSymbols.size() != 0) {
		// System.out.println("For node " + node.getClass().getSimpleName() + ": <<" +
		// node
		// + ">>, the read variables are: ");
		// for (Symbol sym : readSymbols) {
		// System.out.print(sym.name + ": " + node.getInfo().getSharingAttribute(sym) +
		// "; ");
		// }
		// System.out.println();
		// }
		// }
		// }
		// System.exit(0);
		// Setup.checkStats(root);
		// long timeStart = System.nanoTime();
		// for (int i = 0; i < 28; i++) {
		// root = (TranslationUnit) Copier.getDeepCopy(root);
		// }
		// long timeTaken = System.nanoTime() - timeStart;
		// System.out.println("Time taken to create one copy (in milliseconds) : " +
		// timeTaken / (1000000.0 * 28));

		/*
		 * Testing CFG insertion...
		 * //
		 */
		// for (Node node : root.getInfo().getCFGNodes()) {
		// ExpressionStatement expStmtTarget = (ExpressionStatement)
		// CParser.justParse("x = 123;",
		// ExpressionStatement.class);
		// if (node instanceof FunctionDefinition) {
		// continue;
		// }
		// CFGLink link = CFGLinkFinder.getCFGLinkFor(node);
		// if (link == null) {
		// System.out.println(node);
		// }
		// if (link instanceof CompoundElementLink) {
		// // Node tempNode = CParser.justParse("final(x>1)", FinalClause.class);
		// // tempNode.isCFGNode = true;
		// InsertImmediatePredecessor.insertImmediatePredecessor(node, expStmtTarget);
		//
		// // IfStatement ifStmt = ((IfEndLink) link).enclosingNonLeafNode;
		// // EndNode endNode = ifStmt.getInfo().getCFGInfo().getNestedCFG().end;
		// // IfStatement copiedIfStmt = Copier.getDeepCopy(ifStmt);
		// // System.out.println("Copied ==== " + ifStmt + " \n==== to ^^^^" +
		// copiedIfStmt + "\n^^^^");
		// }
		// }
		// Misc.generateDotGraph(root, "");
		// Misc.printToFile(root, "imop_before_dummy.i");
		/*
		 * .. done with testing of CFG insertion.
		 */

		/*
		 * Testing insertion of dummy flushses.
		 */
		// InsertDummyFlushDirectives.insertDummyFlushDirective(root);
		// for (Node dummyFlushNode : root.getInfo().getCFGNodes()) {
		// if (dummyFlushNode instanceof DummyFlushDirective) {
		// DummyFlushDirective dummyFlush = (DummyFlushDirective) dummyFlushNode;
		// dummyFlush.setWrittenBeforeFlush();
		// dummyFlush.setReadAfterFlush();
		// }
		// }
		/*
		 * .. test for insertion of dummy flushes.
		 */

		// for (Node cfgNode : root.getInfo().getCFGNodes()) {
		// System.out.println("Context-insensitive inter-procedural successors of a(n) "
		// + cfgNode.getClass().getSimpleName() + " " + cfgNode + " are --");
		// int i = 0;
		// for (Node succ:
		// cfgNode.getInfo().getCFGInfo().getInterProceduralSuccessors()) {
		// System.out.println(i++ + " : " + succ);
		// }
		// System.out.println("And predecessors are --");
		// i = 0;
		// for (Node pred:
		// cfgNode.getInfo().getCFGInfo().getInterProceduralPredecessors()) {
		// System.out.println(i++ + " : " + pred);
		// }

		/*
		 * Testing ReachingDefinitionAnalysis (RDA).
		 */
		// new ReachingDefinitionAnalysis().run(root.getInfo().getMainFunction());

		// for (Node node : root.getInfo().getCFGNodes()) {
		// if (!Misc.isCFGLeafNode(node)) {
		// continue;
		// }
		// if (node instanceof BeginNode || node instanceof EndNode) {
		// continue;
		// }
		// FlowFact inFF = node.getInfo().getIN(AnalysisName.REACHING_DEFINITION);
		// if (inFF != null) {
		// System.out.println("*** Reaching Definitions for " + node);
		// System.out.println(inFF.getString());
		// }
		// }
		/*
		 * Testing of RDA done.
		 */

		/*
		 * Testing of LivenessAnalysis (LA).
		 */
		// new LivenessAnalysis().run(root.getInfo().getMainFunction());
		// for (Node node : root.getInfo().getCFGNodes()) {
		// if (!Misc.isCFGLeafNode(node)) {
		// continue;
		// }
		// if (node instanceof BeginNode || node instanceof EndNode) {
		// continue;
		// }
		// FlowFact outFF = node.getInfo().getOUT(AnalysisName.LIVENESS);
		// if (outFF != null) {
		// System.out.println("\n*** Liveness information for " + node);
		// System.out.println(outFF.getString());
		// }
		// }
		/*
		 * Testing of LA done.
		 */
		// SyncOptimizationDriver.optimizeProgram();

		/*
		 * Testing updates so far.
		 */
		// System.out.println("Initially: \n" + root + " at " + Misc.getLineNum(root));
		// FunctionDefinition fooDef = root.getInfo().getFunctionWithName("foo");
		// CompoundStatement newStmt = (CompoundStatement) CParser.justParse("{bar(x,
		// y);}", CompoundStatement.class);
		// for (Node cfgNode : fooDef.getInfo().getCFGIntraProceduralNodes()) {
		// if (cfgNode instanceof IfStatement) {
		// IfStatement ifStmt = (IfStatement) cfgNode;
		// ifStmt.getInfo().printNode();
		// ifStmt.getInfo().getCFGInfo().setThenBody(newStmt);
		// }
		// }
		// System.out.println("Finally: \n" + root + " at " + Misc.getLineNum(root));
		//
		// for (Node scope : Misc.getInheritedEnclosee(root, CompoundStatement.class)) {
		// CompoundStatement compStmt = (CompoundStatement) scope;
		// if (compStmt.getInfo().getSymbolTable().isEmpty()) {
		// continue;
		// }
		// for (String sym : compStmt.getInfo().getSymbolTable().keySet()) {
		// System.out.println(sym);
		// }
		// Declaration toBeRemoved = null;
		// for (Node cfgNode : compStmt.getInfo().getCFGInfo().getAllComponents()) {
		// if (cfgNode instanceof Declaration) {
		// toBeRemoved = (Declaration) cfgNode;
		// break;
		// }
		// }
		// if (toBeRemoved != null) {
		// compStmt.getInfo().getCFGInfo().removeElement(toBeRemoved);
		// }
		// // System.out.println(compStmt.getInfo().getSymbolTable().size());
		// }
		/*
		 * Testing done for updates.
		 */
		/*
		 * Testing RoutineLocks
		 */
		// FunctionDefinition mainDef = root.getInfo().getMainFunction();
		// for (CallStatement callStmt : mainDef.getInfo().getCallStatements()) {
		// RoutineLock routineLock = RoutineLock.getRoutineLock(callStmt);
		// if (routineLock != null) {
		// System.out.println("Yay!" + routineLock.getAllLockSetCopy());
		// }
		// }
		/*
		 * Testing complete for routine locks.
		 */

		// for (Node parConsNode : Misc.getInheritedEnclosee(root,
		// ParallelConstruct.class)) {
		// ParallelConstruct parCons = (ParallelConstruct) parConsNode;
		// System.err.println("Number of phases encountered in the parallel-construct at
		// line #"
		// + Misc.getLineNum(parCons) + ":" +
		// parCons.getInfo().getAllPhaseList().size());
		// for (Phase ph : parCons.getInfo().getAllPhaseList()) {
		// System.err.println(ph.getBeginPointsCopy());
		// System.err.println(ph.getNodeSetCopy());
		// System.err.println(ph.getEndPointsCopy());
		// System.err.println();
		// }
		//
		// }

		// Set<BeginPhasePoint> allBeginPhasePoints =
		// BeginPhasePoint.getAllBeginPhasePointsCopy();
		// System.err.println("Number of phases encountered in the program: " +
		// allBeginPhasePoints.size());
		// for (BeginPhasePoint ph : allBeginPhasePoints) {
		// // System.err.println("\t" + ph.);
		// System.err.println("\t" + ph.getReachableNodesCopy());
		// System.err.println("\t" + ph.getNextBarriersCopy());
		// System.err.println();
		// }

		/*
		 * Testing getNonLeafNestingPathExclusive.
		 */
		// OmpConstruct ompCons = (OmpConstruct) CParser.justParse("#pragma omp parallel
		// \n {if (0) printf(\"abc\"); for (x = 0; x < 10; x++) i = i + 1; #pragma omp
		// flush\n}", OmpConstruct.class);
		// System.out.println(ompCons);
		// for (Node expStmtNode : Misc.getInheritedEnclosee(ompCons,
		// ExpressionStatement.class)) {
		// ExpressionStatement expStmt = (ExpressionStatement) expStmtNode;
		// System.out.println("For " + expStmt);
		// for (Node pathNode : Misc.getNonLeafNestingPathExclusive(expStmt)) {
		// System.out.println(pathNode.getClass().getSimpleName());
		// }
		// }
		/*
		 * Testing of getNonLeafNestingPathExclusive done.
		 */

		/*
		 * root = (TranslationUnit)
		 * CParser.createRefinedASTNode(root.getInfo().getString(),
		 * TranslationUnit.class);
		 * // for (Node whileNode : Misc.getExactEnclosee(root,
		 * WhileStatement.class)) {
		 * // WhileStatement whileStmt = (WhileStatement) whileNode;
		 * // WhileStatement newWhileStmt =
		 * LoopTransformations.unrollWhile(whileStmt, 2);
		 * // BasicTransform.crudeReplaceOldWithNew(whileStmt,
		 * newWhileStmt);
		 * // }
		 * // root = (TranslationUnit)
		 * CParser.createRefinedASTNode(root.getInfo().getString(),
		 * TranslationUnit.class);
		 * //
		 * // System.err.println("==== Performing MHP Analysis ====");
		 * // Misc.performMHPAnalysis(root);
		 * //
		 * // System.err.
		 * println("==== Performing Intra-thread Reaching Definition Analysis ===="
		 * );
		 * // Misc.performReachingDefinitionsAnalysis(root);
		 * //
		 * // System.err.
		 * println("==== Performing Intra-thread DU and UD chains Analysis ===="
		 * );
		 * // root.accept(new DUChainGenerator());
		 * //
		 * // System.err.
		 * println("==== Performing Data Dependence Analyses ====");
		 * // Misc.performDataDependenceAnalysis(root);
		 * //
		 * // System.err.
		 * println("==== Performing Memory Renaming to remove Anti-dependence on the shared variables ===="
		 * );
		 * for (ParallelConstruct parCons : Misc.getParConstructs(root)) {
		 * for (Node whileNode : Misc.getExactEnclosee(parCons,
		 * WhileStatement.class)) {
		 * int i = 2;
		 * while (i < 4) {
		 * WhileStatement whileStmt = (WhileStatement) whileNode;
		 * WhileStatement newWhileStmt =
		 * LoopTransformations.unrollWhile(whileStmt, i++);
		 * BasicTransform.crudeReplaceOldWithNew(whileStmt, newWhileStmt);
		 * setupRoot();
		 * RemoveAnti.removeAntiDependence(parCons);
		 * Misc.printToFile(root, "output.i" + i);
		 * BasicTransform.crudeReplaceOldWithNew(newWhileStmt, whileStmt);
		 * }
		 * }
		 * }
		 * System.err.flush();
		 */
		// System.exit(0);
		/*
		 * Temporary Code ends here.
		 */

		// Node rootScope = new CParser(new
		// FileInputStream("/Users/aman/Dropbox/openmp/prog/compareInputOutput/inputWithPrints.i")).TranslationUnit();
		// root = (TranslationUnit) CParser.createRefinedASTNode(System.in,
		// TranslationUnit.class);
		// Builder.crudeCreateASTNode("char x[] = \"hello\";",
		// "Declaration").getInfo().printNode();
		// Builder.crudeDuplicateASTNode(Builder.crudeCreateASTNode("char
		// x[] = \"hello\";", "Declaration")).getInfo().printNode();

		// Misc.printOpCounts(root);

		/*
		 * Initializations.
		 */
		// Remove old-style parameter lists. Put "int" for empty return
		// types.

		// Here, we need to insert a visitor that can ensure that all
		// the structures and unions are
		// defined in their own lines and these lines do not contain any
		// declarations of their objects.

		/*
		 * CFG Creation
		 * Testing: Done on NPB (not semantically)
		 */
		// Done in createRefinedASTNode: root.accept(new
		// CFGGenerator(root));
		// Done in createRefinedASTNode: root.accept(new
		// NonNestedCFGGenerator());
		// DotGraphGenerator dotGen = new DotGraphGenerator(root);
		// dotGen.create(TypeOfCFG.NESTED);
		// System.out.println();
		// dotGen.create(TypeOfCFG.SIMPLE);

		/*
		 * Parallel Region Analysis
		 * Testing: Done on NPB (not semantically)
		 */
		// Not-needed Code:
		// System.out.println("==== Finding Parallel Regions ====");
		// Misc.markParallelRegions(root);
		// Misc.dumpParallelRegion(root);

		/*
		 * Parallel Phase Analysis (MHP Analysis)
		 */
		// System.err.println("==== Performing MHP Analysis ====");
		// Misc.performMHPAnalysis(root);
		// System.err.flush();
		// Misc.dumpMHPInformation(root);
		// System.out.println("<--- Done");

		/*
		 * Checking LvalueAccessGetter
		 */
		// LvalueAccessGetter lvalGetter = new LvalueAccessGetter();
		// root.accept(lvalGetter);
		// System.out.println("Lvalues read: ");
		// Vector<Symbol> valuesRead = lvalGetter.lvalueReadList;
		// for(Symbol s: valuesRead) {
		// System.out.println("\t" + s.name);
		// }
		// System.out.println("\nLvalues written: ");
		// Vector<Symbol> valuesWritten = lvalGetter.lvalueWriteList;
		// for(Symbol s: valuesWritten) {
		// System.out.println("\t" + s.name);
		// }
		// System.out.println("<--- Done");
		////////////////////////
		////////////////////////

		// System.out.println("==== Dumping Data Sharing Attributes ====");
		// for (ParallelConstruct parCons : Misc.getParConstructs(root)) {
		// parCons.accept(new TemporaryDataAttChecker());
		// }

		// /*
		// * IntraThread Reaching Definitions Analysis
		// */
		// System.err.println("==== Performing Intra-thread Reaching Definition Analysis
		// ====");
		// Misc.performReachingDefinitionsAnalysis(root);
		// root.accept(new TemporaryDataflowChecker());

		/*
		 * IntraThread DU (and UD) chains Analysis
		 */
		// System.err.println("==== Performing Intra-thread DU and UD chains Analysis
		// ====");
		// root.accept(new DUChainGenerator());
		// root.accept(new TemporaryDataflowChecker());
		// System.out.println("Def-Use (and Use-Def) Chains Analysis
		// complete.");

		// /*
		// * Liveness Analysis
		// */
		// System.err.println("==== Performing Liveness Analysis ====");
		// Misc.performLivenessAnalysis(root);
		// // root.accept(new TemporaryDataflowChecker());

		/*
		 * Perform Intra-Thread Data Dependence Analysis
		 */
		// System.err.println("==== Performing Data Dependence Analyses ====");
		// Misc.performDataDependenceAnalysis(root);
		// root.accept(new TemporaryDataflowChecker());

		/*
		 * Perform Lockset Analysis
		 */
		// System.err.println("==== Performing Lock Identification Analysis ====");
		// Misc.performLocksetAnalysis(root);

		// // Testing target variables of atomic regions
		// System.err.println("======= Target Variable Checks =========");
		// for (Node n : Misc.getExactEnclosee(root, AtomicConstruct.class))
		// {
		// AtomicConstructInfo atomInfo = ((AtomicConstructInfo)
		// n.getInfo());
		// System.err.println("Target variable: " +
		// atomInfo.getAtomicClauseType() + " of " +
		// atomInfo.getTargetSymbol().name);
		// }

		// Naive testing of atomicity races
		// for (Node n1 : root.getInfo().getCFGNodes()) {
		// for (Node n2 : root.getInfo().getCFGNodes()) {
		// if (n1 == n2) {
		// continue;
		// }
		// if (!(Misc.isCFGLeafNode(n1) && Misc.isCFGLeafNode(n2))) {
		// continue;
		// }
		// if (Misc.isAtomicityRacePair(n1, n2)) {
		// System.err.println("Found a race between " +
		// n1.getInfo().getString() + " and " + n2.getInfo().getString());
		// }
		// }
		// }
		/*
		 * TEST starts
		 */

		// for (Node csNode : Misc.getInheritedEnclosee(root,
		// CompoundStatement.class)) {
		// BasicTransform.splitCompoundStatement((CompoundStatement)csNode);
		// }
		// root.getInfo().printNode();

		/*
		 * TEST ends
		 */

		/*
		 * Perform code percolation on all the parallel constructs of root
		 */
		// System.err.println("==== Performing Code Percolation on all the
		// Parallel Constructs ====");
		// for (ParallelConstruct parCons : Misc.getParConstructs(root)) {
		// UpwardPercolater.initPercolate(parCons);
		// }

		/*
		 * Node firstNode = ((RootInfo)
		 * root.getInfo()).getMainFunction().getInfo().getCFGInfo().
		 * getNestedCFG().begin;
		 * ReachingDefinitionAnalysis rda = new
		 * ReachingDefinitionAnalysis(firstNode);
		 * rda.run();
		 * rda.printFacts();
		 */
		// System.err.println("==== Performing Memory Renaming to remove Anti-dependence
		// on the shared variables ====");
		// for (ParallelConstruct parCons : Misc.getParConstructs(root)) {
		// RemoveAnti.removeAntiDependence(parCons);
		// }

		// System.err.println(root.getInfo().getPhaseInfo().phaseList.size());
	}

	public static void checkHighLevelCFGTransformations() {
		// testInsertOnTheEdge();
		testInsertImmediatePredecessor();
		// testInsertImmediateSuccessor();
		// testNodeReplacer();
		// testNodeRemover();
		DumpSnapshot.dumpRoot("test");
		System.exit(0);
	}

	private static void checkSVEAnnotations() {
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main == null) {
			return;
		}
		int count = 0;
		for (Node leaf : main.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			if (!(leaf instanceof Expression)) {
				continue;
			}
			Expression exp = (Expression) leaf;
			if (!Misc.isAPredicate(exp)) {
				continue;
			}
			count++;
			boolean isSVE = SVEChecker.isSingleValuedPredicate(exp);
			System.out.println(leaf + " is " + (isSVE ? "" : "not") + " a single-valued expression.");
		}
		System.out.println("Total number of predicates: " + count);
		System.out.println("Time spent in SVE queries: " + SVEChecker.sveTimer / (1e9 * 1.0) + "s.");
		// System.out.println("Time spent in call-stmt queries: " +
		// NodeInfo.callQueryTimer / (1e9 * 1.0) + "s.");
		System.exit(0);
	}

	private static void criticalAnalyzer() {
		for (ParallelConstruct parCons : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (AbstractPhase<?, ?> absPh : parCons.getInfo().getConnectedPhases()) {
				Phase ph = (Phase) absPh;
				CellSet allCells = new CellSet();
				Set<CriticalNode> nodes = new HashSet<>();
				for (Node node : ph.getNodeSet()) {
					if (node instanceof BeginNode) {
						BeginNode begin = (BeginNode) node;
						if (begin.getParent() instanceof CriticalConstruct) {
							CriticalConstruct cc = (CriticalConstruct) begin.getParent();
							CellSet cellSet = cc.getInfo().getSharedAccesses();
							allCells.addAll(cellSet);
							nodes.add(new CriticalNode(cc, cellSet));
						}
					}
				}
				// All leaf nodes are "allCells".
				for (CriticalNode cn1 : nodes) {
					for (CriticalNode cn2 : nodes) {
						if (cn1.cc == cn2.cc) {
							continue;
						}
						if (cn1.getAccessedCells().containsAll(cn2.getAccessedCells())) {
							System.out.println(cn1.getAccessedCells() + " with " + cn2.getAccessedCells());
							CriticalNode.connect(cn1, cn2);
						}
					}
				}
			}
		}
		System.exit(0);
	}

	private static void debugDeclarationEscalator(WhileStatement whileStmt) {
		DeclarationEscalator
				.pushAllDeclarationsUpFromLevel((CompoundStatement) whileStmt.getInfo().getCFGInfo().getBody());
		DumpSnapshot.dumpVisibleSharedReadWrittenCells("");
		System.exit(0);
	}

	private static void profileStructure1() {
		LoopAndOmpProfiler profiler = new LoopAndOmpProfiler();
		Program.getRoot().accept(profiler);
		DumpSnapshot.printToFile(profiler.str.toString(), Program.fileName + "_sample.txt");
		System.exit(0);
	}

	/**
	 * Checks if the input program contains any loops which:
	 * <ul>
	 * <li>contain more than two barriers,</li> OR
	 * <li>contain parallel regions</li>
	 * </ul>
	 * Furthermore, we try and obtain relevant information about such parallel
	 * regions, if any.
	 */
	private static void profileStructure2() {
		for (IterationStatement itStmt : Misc.getInheritedEnclosee(Program.getRoot(), IterationStatement.class)) {
			if (itStmt.getClass() == IterationStatement.class) {
				continue;
			}
			itStmt = (IterationStatement) Misc.getCFGNodeFor(itStmt);

			int externalBarriersInLoops = 0;
			int internalBarriersInLoops = 0;
			boolean foundExternalRegion = false;
			for (NodeWithStack nws : itStmt.getInfo().getCFGInfo()
					.getIntraTaskCFGLeafContentsOfSameParLevel(new CallStack())) {
				Node internalNode = nws.getNode();
				if (internalNode instanceof BarrierDirective) {
					externalBarriersInLoops++;
					foundExternalRegion = true;
				}
				if (!foundExternalRegion) {
					if (internalNode instanceof BeginNode) {
						BeginNode beginNode = (BeginNode) internalNode;
						if (beginNode.getParent() instanceof ParallelConstruct) {
							internalBarriersInLoops += 2;
						}
					}
				}
			}

			if (foundExternalRegion) {
				if (externalBarriersInLoops > 1) {
					System.out.println(
							"Loop#" + Misc.getLineNum(itStmt) + ": " + externalBarriersInLoops + "B of outer regions.");
				}
			} else {
				if (internalBarriersInLoops > 1) {
					System.out.println(
							"Loop#" + Misc.getLineNum(itStmt) + ": " + internalBarriersInLoops + "B of inner regions.");
				}
			}
		}
	}

	private static void removeBarriers() {
		RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
		System.err.println("Time spent in SVE queries: " + SVEChecker.sveTimer / (1e9 * 1.0) + "s.");
		System.err.println("Time spent in forward IDFA updates -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println(
					"\t For " + analysis.getAnalysisName() + ": " + analysis.flowAnalysisUpdateTimer / (1e9) + "s.");
		}
		System.err.println("Time spent in having uni-task precision in IDFA edge creation: "
				+ CFGInfo.uniPrecisionTimer / (1e9 * 1.0) + "s.");
		System.err.println("Time spent in field-sensitive queries: " + FieldSensitivity.timer / (1e9 * 1.0) + "s.");
		System.err.println("Total number of times nodes were processed for automated updates -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println("\t For " + analysis.getAnalysisName() + ": " + analysis.nodesProcessedDuringUpdate);
		}
		System.err.println("Number of field-sensitive queries: " + FieldSensitivity.counter);
		BasicTransform.removeEmptyConstructs(Program.getRoot());
		Program.getRoot().getInfo().removeUnusedElements();
		// OLD CODE: BasicTransform.removeDeadDeclarations(Program.getRoot());
		// FrontEnd.parseAndNormalize(Program.root.toString());
		RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
		DumpSnapshot.printToFile(Program.getRoot(), "imop_output.i");
		DumpSnapshot.dumpPhases("final");
		DumpSnapshot.dumpVisibleSharedReadWrittenCells("final");
		DumpSnapshot.dumpCopyInfo("final");
		DumpSnapshot.dumpPhaseAndCopyInfo("final");
		System.exit(0);
	}

	public static boolean tempChecker() {
		for (ParallelConstruct parCons : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (AbstractPhase<?, ?> absPh : parCons.getInfo().getConnectedPhases()) {
				Phase ph = (Phase) absPh;
				Set<Node> stmtList = ph.getNodeSet();
				Node stmt1 = null;
				Node stmt2 = null;
				for (Node s : stmtList) {
					if (s instanceof DummyFlushDirective) {
						continue;
					}
					if (s.toString().contains("/ 4.0")) {
						stmt1 = s;
					} else if (s.toString().contains("fabs")) {
						stmt2 = s;
					}
				}
				if (stmt1 != null && stmt2 != null) {
					System.out.println(
							"Phase id #" + ph.getPhaseId() + " has these statements: " + stmt1 + " and " + stmt2);
					return true;
				}
			}
		}
		return false;
	}

	private static void temporaryChecker1() {
		Statement s1 = null, s2 = null;
		for (Statement stmt : Misc.getInheritedEnclosee(Program.getRoot(), Statement.class)) {
			Node cfgNode = Misc.getCFGNodeFor(stmt);
			if (cfgNode == null || !(cfgNode instanceof Statement)) {
				continue;
			}
			Statement cfgStmt = (Statement) cfgNode;
			if (!cfgStmt.getInfo().hasLabelAnnotations()) {
				continue;
			}
			List<Label> labels = cfgStmt.getInfo().getLabelAnnotations();
			if (labels.stream().anyMatch(l -> l.toString().contains("tc11"))) {
				s1 = cfgStmt;
			} else if (labels.stream().anyMatch(l -> l.toString().contains("tc12"))) {
				s2 = cfgStmt;
			}
		}
		if (s1 == null || s2 == null) {
			Misc.exitDueToError("Could not find labels tc11 and tc12 to run this check.");
		}
		System.out.println(FieldSensitivity.canConflictWithTwoThreads(s1, s2));
		SyntacticAccessExpression sae1 = s1.getInfo().getBaseAccessExpressionWrites().get(0);
		SyntacticAccessExpression sae2 = s2.getInfo().getBaseAccessExpressionReads().get(1);
		System.out.println(sae1);
		System.out.println(sae2);
		SeedConstraint tempEquality = new SeedConstraint(sae1.getIndexExpression(), s1,
				IdOrConstToken.getNewIdToken("tid1"), sae2.getIndexExpression(), s2,
				IdOrConstToken.getNewIdToken("tid2"), OperatorToken.ASSIGN);
		Set<SeedConstraint> allEqualityConstraints = new HashSet<>();
		allEqualityConstraints.add(tempEquality);
		long timer = System.nanoTime();
		ConstraintsGenerator.reinitInductionSet();
		boolean satisfiable = ConstraintsGenerator.mayBeSatisfiable(allEqualityConstraints);
		if (satisfiable) {
			System.out.println("System has a solution.");
		} else {
			System.out.println("System is UNSATISFIABLE.");
		}
		System.exit(0);
	}

	private static void temporaryChecker2() {
		Statement s1 = null;
		for (Statement stmt : Misc.getInheritedEnclosee(Program.getRoot(), Statement.class)) {
			Node cfgNode = Misc.getCFGNodeFor(stmt);
			if (cfgNode == null || !(cfgNode instanceof Statement)) {
				continue;
			}
			Statement cfgStmt = (Statement) cfgNode;
			if (!cfgStmt.getInfo().hasLabelAnnotations()) {
				continue;
			}
			List<Label> labels = cfgStmt.getInfo().getLabelAnnotations();
			if (labels.stream().anyMatch(l -> l.toString().contains("tc11"))) {
				s1 = cfgStmt;
			}
		}
		if (s1 == null) {
			Misc.exitDueToError("Could not find label  tc11 to run this check.");
		}
		HashMap<String, String> cfg = new HashMap<>();
		cfg.put("model_validate", "true");
		Context c = new Context(cfg);
		Solver solver = c.mkSimpleSolver();
		System.out.println(ExpressionTokenizer.getNormalizedForm(ExpressionTokenizer.getAssigningExpression(s1)));
		// System.out.println(ExpressionTokenizer.getFirstOperand(ExpressionTokenizer.getAssigningExpression(s1)));
		// System.out.println(ExpressionTokenizer.getSecondOperand(ExpressionTokenizer.getAssigningExpression(s1)));
		c.close();
		System.exit(0);
	}

	private static void temporaryChecker3() {
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main == null) {
			System.exit(0);
		}
		for (Node leaf1 : main.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			if (!Misc.isAPredicate(leaf1)) {
				continue;
			}
			System.err.println("##### Attempting eqaulity comparison of " + leaf1 + " with 0 #####");
			List<Tokenizable> e1 = ExpressionTokenizer.getAssigningExpression(leaf1);
			List<Tokenizable> e2 = new ArrayList<>();
			e2.add(IdOrConstToken.getNewConstantToken("13"));
			SeedConstraint tempEquality = new SeedConstraint(e1, leaf1, IdOrConstToken.getNewIdToken("tid1"), e2, leaf1,
					IdOrConstToken.getNewIdToken("tid2"), OperatorToken.ASSIGN);
			Set<SeedConstraint> allEqualityConstraints = new HashSet<>();
			allEqualityConstraints.add(tempEquality);
			long timer = System.nanoTime();
			ConstraintsGenerator.reinitInductionSet();
			boolean satisfiable = ConstraintsGenerator.mayBeSatisfiable(allEqualityConstraints);
			if (satisfiable) {
				System.err.println("System has a solution.");
			}
		}
		DumpSnapshot.printToFile(ConstraintsGenerator.allConstraintString, Program.fileName + "_z3_queries.txt");
	}

	public static void testBarrierCounterInstrumentation() {
		BarrierCounterInstrumentation.insertBarrierCounters();
		DumpSnapshot.dumpRoot("counted");
		System.exit(0);
	}

	private static void testBarrierPercolation() {
		RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
		System.err.println(
				"Skipping par-cons expansion and merge...\nMoving on to upward percolation and barrier removal.");
		// ParallelConstructExpander.mergeParallelRegions(Program.root);
		// Main.dumpPhases("merged");
		for (ParallelConstruct parCons : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			UpwardPercolater.initPercolate(parCons);
		}
		DumpSnapshot.dumpPhases("trial");
		RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
		DumpSnapshot.dumpRoot("optimized");
		System.err.println("Time spent in SVE queries: " + SVEChecker.sveTimer / (1e9 * 1.0) + "s.");
		DumpSnapshot.dumpPhaseAndCopyInfo("final");
		System.exit(0);
	}

	public static void testBaseExpressionGetter() {
		for (ExpressionStatement expStmt : Misc.getInheritedEnclosee(Program.getRoot(), ExpressionStatement.class)) {
			if (expStmt.getF0().present()) {
				Expression exp = (Expression) expStmt.getF0().getNode();
				List<SyntacticAccessExpression> accessReads = exp.getInfo().getBaseAccessExpressionReads();
				List<SyntacticAccessExpression> accessWrites = exp.getInfo().getBaseAccessExpressionWrites();
				if (!accessReads.isEmpty() || !accessWrites.isEmpty()) {
					System.err.println("For expression " + exp);
				}
				for (SyntacticAccessExpression ae : accessReads) {
					System.err.println("\tREAD:" + ae + ".");
				}
				for (SyntacticAccessExpression ae : accessWrites) {
					System.err.println("\tWRITE:" + ae + ".");
				}
			}
		}
		System.exit(0);
	}

	public static void testClauses() {
		OmpClause newClause;
		newClause = FrontEnd.parseAndNormalize("final(x)", FinalClause.class);
		System.out.println(newClause);
		newClause = FrontEnd.parseAndNormalize("if (x < 2)", IfClause.class);
		System.out.println(newClause);
		newClause = FrontEnd.parseAndNormalize("mergeable", MergeableClause.class);
		System.out.println(newClause);
		newClause = FrontEnd.parseAndNormalize("nowait", NowaitClause.class);
		System.out.println(newClause);
		newClause = FrontEnd.parseAndNormalize("num_threads(2)", NumThreadsClause.class);
		System.out.println(newClause);
		newClause = FrontEnd.parseAndNormalize("copyin(x)", OmpCopyinClause.class);
		System.out.println(newClause);
		newClause = FrontEnd.parseAndNormalize("default(shared)", OmpDfltSharedClause.class);
		System.out.println(newClause);
		newClause = FrontEnd.parseAndNormalize("default(none)", OmpDfltNoneClause.class);
		System.out.println(newClause);
		newClause = FrontEnd.parseAndNormalize("firstprivate(x)", OmpFirstPrivateClause.class);
		System.out.println(newClause);
		newClause = FrontEnd.parseAndNormalize("lastprivate(x)", OmpLastPrivateClause.class);
		System.out.println(newClause);
		newClause = FrontEnd.parseAndNormalize("private(x, y)", OmpPrivateClause.class);
		System.out.println(newClause);
		newClause = FrontEnd.parseAndNormalize("reduction(+:x)", OmpReductionClause.class);
		System.out.println(newClause);
		newClause = FrontEnd.parseAndNormalize("shared(x)", OmpSharedClause.class);
		System.out.println(newClause);
		System.exit(0);
	}

	public static void testCoExistence() {
		Node n1 = Program.getRoot().getInfo().getStatementWithLabel("l1");
		Node n2 = Program.getRoot().getInfo().getStatementWithLabel("l2");
		if (n1 == null || n2 == null) {
			Misc.warnDueToLackOfFeature("Input should contain two statements labeled l1 and l2.", null);
		} else {
			System.out.println(CoExistenceChecker.canCoExistInAnyPhase(n1, n2));
		}
		System.exit(0);
	}

	private static void testCopyDetector() {
		for (IterationStatement itStmt : Misc.getInheritedEnclosee(Program.getRoot(), IterationStatement.class)) {
			if (itStmt.getClass() == IterationStatement.class) {
				continue;
			}
			CopyEliminator.detectSwapCode(itStmt);
		}
		// for (ExpressionStatement exp : Misc.getInheritedEnclosee(Program.root,
		// ExpressionStatement.class)) {
		// for (Assignment assign : AssignmentGetter.getLexicalAssignments(exp)) {
		// assign.isCopyInstruction();
		// }
		// }
		System.exit(0);
	}

	private static void testCopyElimination() {
		DumpSnapshot.dumpReachingDefinitions("final");
		long timer = System.nanoTime();
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		int counter = 0;

		// for (SingleConstruct singleCons : Misc.getInheritedEnclosee(main,
		// SingleConstruct.class)) {
		// CompoundStatement compStmt = (CompoundStatement)
		// singleCons.getInfo().getCFGInfo().getBody();
		// for (Node stmt : compStmt.getInfo().getCFGInfo().getElementList()) {
		// NodeRemover.removeNode(stmt);
		// }
		// }
		// Main.dumpReachingDefinitions("tex");
		// System.exit(0);
		// Statement tempStmt = main.getInfo().getStatementWithLabel("l2");
		// Cell w = tempStmt.getInfo().getWrites().get(0);
		// Statement stmt = main.getInfo().getStatementWithLabel("L1");
		// NodeRemover.removeNode(stmt);
		// System.out.println("Statement has followers: " +
		// stmt.getInfo().getAllUsesForwardsExclusively(w));
		// CopyEliminator.eliminateDeadCopies(main);
		// System.out.println(main);
		// System.exit(0);

		while (true) {
			DumpSnapshot.dumpPhaseAndCopyInfo(counter++ + "");
			boolean internalChanged;
			do {
				internalChanged = false;
				while (CopyEliminator.removeAllCopyKillers(main)) {
					internalChanged = true;
				}
				while (CopyEliminator.eliminateDeadCopies(main)) {
					internalChanged = true;
				}
			} while (internalChanged);
			Set<Node> copySources = CopyEliminator.replaceCopiesIn(main);
			if (copySources.isEmpty()) {
				break;
			}
		}
		DumpSnapshot.dumpCopyInfo("finalCopy");
		timer = System.nanoTime() - timer;
		BasicTransform.removeEmptyConstructs(main);
		main.getInfo().removeUnusedElements();
		// OLD CODE: BasicTransform.removeDeadDeclarations(main);
		RedundantSynchronizationRemoval.removeBarriers(main);
		DumpSnapshot.dumpRoot("final");
		System.out.println("Time spent in removing copies: " + timer / (1e9 * 1.0) + "s.");
		System.err.println("Time spent in forward IDFA updates -- ");
		for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
			System.err.println(
					"\t For " + analysis.getAnalysisName() + ": " + analysis.flowAnalysisUpdateTimer / (1e9) + "s.");
		}
		System.exit(0);
	}

	private static void testCopyEliminationFromLoops() {
		for (IterationStatement itStmt : Misc.getInheritedEnclosee(Program.getRoot(), IterationStatement.class)) {
			if (itStmt instanceof WhileStatement) {
				DriverModule.performCopyElimination(itStmt);
			}
		}
		BasicTransform.removeEmptyConstructs(Program.getRoot());
		Program.getRoot().getInfo().removeUnusedElements();
		// OLD CODE: BasicTransform.removeDeadDeclarations(Program.getRoot());
		DumpSnapshot.dumpRoot("final");
		DumpSnapshot.dumpPhaseAndCopyInfo("final");
		System.exit(0);
	}

	private static void testDriverAndHeuristic() {
		int counter = 0;
		for (WhileStatement whileStmt : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			LoopInstructionsRescheduler.peelFirstBarrier(whileStmt);
			DumpSnapshot.dumpPhases("cheker" + counter++);
		}
		System.exit(0);
	}

	public static void testExpressionTokenizer() {
		for (BracketExpression brack : Misc.getInheritedEnclosee(Program.getRoot(), BracketExpression.class)) {
			Expression exp = brack.getF1();
			List<Tokenizable> tokens = ExpressionTokenizer.getInfixTokens(exp);
			if (tokens.isEmpty()) {
				continue;
			}
			System.err.println(exp + ": " + tokens + "\n Postfix: " + ExpressionTokenizer.getPostfixTokens(exp)
					+ "\n Prefix: " + ExpressionTokenizer.getPrefixTokens(exp));
		}
		System.exit(0);
	}

	public static void testFencePercolation() {
		for (ParallelConstruct parConsNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (AbstractPhase<?, ?> absPh : parConsNode.getInfo().getConnectedPhases()) {
				Phase ph = (Phase) absPh;
				System.out.println("Verifying fence percolation in phase #" + ph.getPhaseId());
				boolean valid = FencePercolationVerifier.isPhaseSafeForFencePercolations(ph);
				System.out.println(
						"\t Percolation across fences " + (valid ? "is" : "is NOT") + " safe in this phase.\n");
			}
		}
		System.exit(0);
	}

	private static void testFieldSensitivity() {
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		for (Node leaf1 : main.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			for (Node leaf2 : main.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				if (leaf1 == leaf2) {
					continue;
				}
				if (FieldSensitivity.canConflictWithTwoThreads(leaf1, leaf2)) {
					System.out.println("Following pair may conflict: (" + leaf1 + ", " + leaf2 + ")");
				}

			}

		}
		System.exit(0);
	}

	private static void testIDFAUpdate() {
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		Statement stmt = main.getInfo().getStatementWithLabel("l1");
		NodeRemover.removeNode(stmt);
		System.out.println(Program.getRoot());
		System.exit(0);
	}

	private static void testInlining() {
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		FunctionInliner.inline(mainFunc);
		// Main.dumpPhases("inlined");
		DumpSnapshot.dumpRoot("inlined");
		// Main.dumpVisibleSharedReadWrittenCells("");
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
		System.err.println("Time spent in having uni-task precision in IDFA edge creation: "
				+ CFGInfo.uniPrecisionTimer / (1e9 * 1.0) + "s.");
		System.err.println("Number of field-sensitive queries: " + FieldSensitivity.counter);
		System.err.println("Time spent in field-sensitive queries: " + FieldSensitivity.timer / (1e9 * 1.0) + "s.");
		System.err.println("Time spent in inlining: " + FunctionInliner.inliningTimer / (1e9 * 1.0) + "s.");
		System.exit(0);
	}

	public static void testInsertImmediatePredecessor() {
		/*
		 * Test InsertImmediatePredecessor.
		 */
		ExpressionStatement expStmt;
		int i = 0;
		for (FunctionDefinition function : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			for (Node n : function.getInfo().getCFGInfo().getLexicalCFGContents()) {
				// FunctionDefinition
				// if (n instanceof FunctionDefinition) {
				// FunctionDefinition func = (FunctionDefinition) n;
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// //
				// System.out.println(InsertImmediatePredecessor.insertAggressive(func.getInfo().getCFGInfo().getNestedCFG().getBegin(),
				// expStmt));
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// //
				// System.out.println(InsertImmediatePredecessor.insertAggressive(func.getInfo().getCFGInfo().getNestedCFG().getEnd(),
				// expStmt));
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// //
				// System.out.println(InsertImmediatePredecessor.insertAggressive(func.getInfo().getCFGInfo().getBody(),
				// expStmt));
				//
				// for (Node comp : func.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// }
				// }

				// ParallelConstruct
				// if (n instanceof ParallelConstruct) {
				// ParallelConstruct parCons = (ParallelConstruct) n;
				// // expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getNestedCFG().getEnd(),
				// expStmt));
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// // }
				// }

				// OmpFor
				// if (n instanceof ForConstruct) {
				// ForConstruct parCons = (ForConstruct) n;
				// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getReinitExpression(),
				// expStmt));
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// // expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// //// expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// // }
				// }

				// OmpSections
				// if (n instanceof SectionsConstruct) {
				// SectionsConstruct parCons = (SectionsConstruct) n;
				// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getNestedCFG().getEnd(),
				// expStmt));
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				//// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				//// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				//// System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				//// }
				// }

				// // OmpSingle
				// if (n instanceof SingleConstruct) {
				// SingleConstruct parCons = (SingleConstruct) n;
				// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getBody(), expStmt));
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// // // expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// // }
				// }
				//
				// // OmpTask
				// if (n instanceof TaskConstruct) {
				// TaskConstruct parCons = (TaskConstruct) n;
				// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getBody(), expStmt));
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// // // expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// // }
				// }
				//
				// // OmpMaster
				// if (n instanceof MasterConstruct) {
				// MasterConstruct parCons = (MasterConstruct) n;
				// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getBody(), expStmt));
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// // // expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize("i = " + i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// // }
				// }
				//
				// // OmpCritical
				// if (n instanceof CriticalConstruct) {
				// CriticalConstruct parCons = (CriticalConstruct) n;
				// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getBody(), expStmt));
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// // // expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// // }
				// }
				//
				// // OmpAtomic
				// if (n instanceof AtomicConstruct) {
				// AtomicConstruct parCons = (AtomicConstruct) n;
				// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getStatement(), expStmt));
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// // // expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// // }
				// }
				//
				// // OmpOrdered
				// if (n instanceof OrderedConstruct) {
				// OrderedConstruct parCons = (OrderedConstruct) n;
				// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getBody(), expStmt));
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// // // expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// // }
				// }
				//
				// IfStatement
				// if (n instanceof IfStatement) {
				// IfStatement parCons = (IfStatement) n;
				// // expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediatePredecessor
				// // .insertAggressive(parCons.getInfo().getCFGInfo().getNestedCFG().getEnd(),
				// expStmt));
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				//// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// }
				// }
				//
				// WhileStatement
				// if (n instanceof WhileStatement) {
				// WhileStatement parCons = (WhileStatement) n;
				// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// Statement stmt = FrontEnd.parseAndNormalize("while(1) {continue;}",
				// Statement.class);
				// System.out.println(InsertImmediatePredecessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getNestedCFG().getEnd(),
				// stmt));
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// // expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// // }
				// }
				//
				// DoStatement
				// if (n instanceof DoStatement) {
				// DoStatement parCons = (DoStatement) n;
				// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // Statement stmt = FrontEnd.parseAndNormalize("continue;", Statement.class);
				// System.out.println(InsertImmediatePredecessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getNestedCFG().getEnd(),
				// expStmt));
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// // expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// // }
				// }
				//
				// // CallStatement
				// if (n instanceof CallStatement) {
				// CallStatement parCons = (CallStatement) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// }
				// }
				//
				// CompoundStatement
				if (n instanceof CompoundStatement) {
					CompoundStatement parCons = (CompoundStatement) n;
					for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
						Declaration decl = FrontEnd.parseAndNormalize("int x" + i++ + " = 333;", Declaration.class);
						expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
						System.out.println(InsertImmediatePredecessor.insert(comp, decl));
						System.out.println(InsertImmediatePredecessor.insert(comp, expStmt));
					}
				}

				// ForStatement
				if (n instanceof ForStatement) {
					ForStatement parCons = (ForStatement) n;
					expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";", ExpressionStatement.class);
					// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
					Statement stmt = FrontEnd.parseAndNormalize("continue;", Statement.class);
					System.out.println(InsertImmediatePredecessor
							.insert(parCons.getInfo().getCFGInfo().getTerminationExpression(), stmt));
					if (parCons.getInfo().getCFGInfo().hasTerminationExpression()) {
						System.out.println(InsertImmediatePredecessor
								.insert(parCons.getInfo().getCFGInfo().getTerminationExpression(), expStmt));
					}

					// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
					// // if (parCons.getInfo().getCFGInfo().getTerminationExpression() == comp) {
					// // continue;
					// // }
					// if (!parCons.getInfo().getCFGInfo().getAllComponents().contains(comp)) {
					// continue;
					// }
					// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
					// System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
					// expStmt));
					// }
				}

				// SwitchStatement
				// if (n instanceof SwitchStatement) {
				// SwitchStatement parCons = (SwitchStatement) n;
				// expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// Statement stmt = FrontEnd.parseAndNormalize("break;", Statement.class);
				// System.out.println(InsertImmediatePredecessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getNestedCFG().getEnd(),
				// stmt));
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// // if (!parCons.getInfo().getCFGInfo().getAllComponents().contains(comp)) {
				// // continue;
				// // }
				// // expStmt = FrontEnd.parseAndNormalize("x = " + i++ + ";",
				// ExpressionStatement.class);
				// // // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediatePredecessor.insertAggressive(comp,
				// expStmt));
				// // }
				// }
				//
				// GotoStatement
				// if (n instanceof Statement) {
				// Statement stmt = (Statement) n;
				// if (stmt.getInfo().hasLabelAnnotations()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediatePredecessor.insertAggressive(stmt,
				// expStmt));
				// }
				// }
				//
				/////////////////////////////////////////////////////////////
				// // CompoundStatement -- begin, end, and somewhere.
				// if (n instanceof CompoundStatement) {
				// CompoundStatement body = (CompoundStatement) n;
				// BeginNode b = body.getInfo().getCFGInfo().getNestedCFG().getBegin();
				// EndNode e = body.getInfo().getCFGInfo().getNestedCFG().getEnd();
				// // BeginNode
				// System.err.println(InsertOnTheEdge.insertOnTheEdge(b, succ, expStmt));
				// // EndNode
				// System.err.println(InsertOnTheEdge.insertOnTheEdge(pred, e, expStmt));
				// // Elsewhere.
				// List<Node> elements = body.getInfo().getCFGInfo().getElementList();
				// if (elements.size() > 2) {
				// Node one = elements.get(0);
				// Node two = elements.get(1);
				// expStmt = FrontEnd.parseAndNormalize((0.5 + i++) + ";",
				// ExpressionStatement.class);
				// System.err.println(InsertOnTheEdge.insertOnTheEdge(one, two, expStmt));
				// }
				// }
				//
				// // Predicates.
				// if (n instanceof Expression) {
				// int i = 10;
				// if (!Misc.isCFGLeafNode(n)) {
				// continue;
				// }
				// for (Node succ : new ArrayList<>(n.getInfo().getCFGInfo().getSuccessors())) {
				// expStmt = FrontEnd.parseAndNormalize((10 + i++) + ";",
				// ExpressionStatement.class);
				// System.err.println(InsertOnTheEdge.insertOnTheEdge(n, succ, expStmt));
				// }
				// }
			}
		}

	}

	public static void testInsertImmediateSuccessor() {
		/*
		 * Test InsertImmediateSuccessor.
		 */
		ExpressionStatement expStmt;
		int i = 0;
		for (FunctionDefinition function : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			for (Node n : function.getInfo().getCFGInfo().getLexicalCFGContents()) {
				// BeginNode of all types.
				// if (n instanceof BeginNode) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(n, expStmt));
				// }

				// FunctionDefinition
				if (n instanceof FunctionDefinition) {
					FunctionDefinition func = (FunctionDefinition) n;
					for (Node comp : func.getInfo().getCFGInfo().getAllComponents()) {
						expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
						System.out.println(InsertImmediateSuccessor.insert(comp, expStmt));
					}
				}

				// ParallelConstruct
				// if (n instanceof ParallelConstruct) {
				// ParallelConstruct parCons = (ParallelConstruct) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// OmpFor
				// if (n instanceof ForConstruct) {
				// ForConstruct parCons = (ForConstruct) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// OmpSections
				// if (n instanceof SectionsConstruct) {
				// SectionsConstruct parCons = (SectionsConstruct) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// OmpSingle
				// if (n instanceof SingleConstruct) {
				// SingleConstruct parCons = (SingleConstruct) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// OmpTask
				// if (n instanceof TaskConstruct) {
				// TaskConstruct parCons = (TaskConstruct) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// OmpMaster
				// if (n instanceof MasterConstruct) {
				// MasterConstruct parCons = (MasterConstruct) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// OmpCritical
				// if (n instanceof CriticalConstruct) {
				// CriticalConstruct parCons = (CriticalConstruct) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// OmpAtomic
				// if (n instanceof AtomicConstruct) {
				// AtomicConstruct parCons = (AtomicConstruct) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// OmpOrdered
				// if (n instanceof OrderedConstruct) {
				// OrderedConstruct parCons = (OrderedConstruct) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// IfStatement
				// if (n instanceof IfStatement) {
				// IfStatement parCons = (IfStatement) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// WhileStatement
				// if (n instanceof WhileStatement) {
				// WhileStatement parCons = (WhileStatement) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// DoStatement
				// if (n instanceof DoStatement) {
				// DoStatement parCons = (DoStatement) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// CallStatement
				// if (n instanceof CallStatement) {
				// CallStatement parCons = (CallStatement) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// CompoundStatement
				// if (n instanceof CompoundStatement) {
				// CompoundStatement parCons = (CompoundStatement) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// Declaration decl = FrontEnd.parseAndNormalize("int x" + i++ + " = 333;",
				// Declaration.class);
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, decl));
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// ForStatement
				// if (n instanceof ForStatement) {
				// ForStatement parCons = (ForStatement) n;
				//
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor
				// .insertAggressive(parCons.getInfo().getCFGInfo().getTerminationExpression(),
				// expStmt));
				//
				// // for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// // // if (parCons.getInfo().getCFGInfo().getTerminationExpression() == comp)
				// {
				// // // continue;
				// // // }
				// // if (!parCons.getInfo().getCFGInfo().getAllComponents().contains(comp)) {
				// // continue;
				// // }
				// // expStmt = FrontEnd.parseAndNormalize(i++ + ";",
				// ExpressionStatement.class);
				// // System.out.println(InsertImmediateSuccessor.insertAggressive(comp,
				// expStmt));
				// // }
				// }

				// SwitchStatement
				// if (n instanceof SwitchStatement) {
				// SwitchStatement parCons = (SwitchStatement) n;
				// for (Node comp : parCons.getInfo().getCFGInfo().getAllComponents()) {
				// if (!parCons.getInfo().getCFGInfo().getAllComponents().contains(comp)) {
				// continue;
				// }
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(comp, expStmt));
				// }
				// }

				// GotoStatement
				// if (n instanceof Statement) {
				// Statement stmt = (Statement) n;
				// if (stmt.getInfo().hasLabelAnnotations()) {
				// expStmt = FrontEnd.parseAndNormalize(i++ + ";", ExpressionStatement.class);
				// System.out.println(InsertImmediateSuccessor.insertAggressive(stmt, expStmt));
				// }
				// }

				/////////////////////////////////////////////////////////////
				// // CompoundStatement -- begin, end, and somewhere.
				// if (n instanceof CompoundStatement) {
				// CompoundStatement body = (CompoundStatement) n;
				// BeginNode b = body.getInfo().getCFGInfo().getNestedCFG().getBegin();
				// EndNode e = body.getInfo().getCFGInfo().getNestedCFG().getEnd();
				// // BeginNode
				// System.err.println(InsertOnTheEdge.insertOnTheEdge(b, succ, expStmt));
				// // EndNode
				// System.err.println(InsertOnTheEdge.insertOnTheEdge(pred, e, expStmt));
				// // Elsewhere.
				// List<Node> elements = body.getInfo().getCFGInfo().getElementList();
				// if (elements.size() > 2) {
				// Node one = elements.get(0);
				// Node two = elements.get(1);
				// expStmt = FrontEnd.parseAndNormalize((0.5 + i++) + ";",
				///////////////////////////////////////////////////////////// ExpressionStatement.class);
				// System.err.println(InsertOnTheEdge.insertOnTheEdge(one, two, expStmt));
				// }
				// }
				//
				// // Predicates.
				// if (n instanceof Expression) {
				// int i = 10;
				// if (!Misc.isCFGLeafNode(n)) {
				// continue;
				// }
				// for (Node succ : new ArrayList<>(n.getInfo().getCFGInfo().getSuccessors())) {
				// expStmt = FrontEnd.parseAndNormalize((10 + i++) + ";",
				///////////////////////////////////////////////////////////// ExpressionStatement.class);
				// System.err.println(InsertOnTheEdge.insertOnTheEdge(n, succ, expStmt));
				// }
				// }
			}
		}
	}

	public static void testInsertOnTheEdge() {
		/*
		 * Test InsertOnTheEdge.
		 */
		ExpressionStatement expStmt;
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main == null) {
			System.out.println("Found no main. Exiting.");
			System.exit(0);
		}
		for (Node n : main.getInfo().getCFGInfo().getLexicalCFGContents()) {
			// ParameterDeclaration
			// if (n instanceof ParameterDeclaration) {
			// expStmt = FrontEnd.parseAndNormalize("235;", ExpressionStatement.class);
			// for (Node succ : n.getInfo().getCFGInfo().getLeafSuccessors()) {
			// if (succ instanceof ParameterDeclaration) {
			// continue;
			// }
			// System.err.println(InsertOnTheEdge.insertOnTheEdge(n, succ, expStmt));
			// }
			// }
			//

			// CompoundStatement -- begin, end, and somewhere.
			if (n instanceof CompoundStatement) {
				CompoundStatement body = (CompoundStatement) n;
				BeginNode b = body.getInfo().getCFGInfo().getNestedCFG().getBegin();
				EndNode e = body.getInfo().getCFGInfo().getNestedCFG().getEnd();
				int i = 0;
				// BeginNode
				// for (Node succ : b.getInfo().getCFGInfo().getSuccessors()) {
				// expStmt = FrontEnd.parseAndNormalize((200 + i++) + ";",
				// ExpressionStatement.class);
				// System.err.println(InsertOnTheEdge.insertOnTheEdge(b, succ, expStmt));
				// }
				// EndNode
				// for (Node pred : e.getInfo().getCFGInfo().getPredecessors()) {
				// expStmt = FrontEnd.parseAndNormalize((100 + i++) + ";",
				// ExpressionStatement.class);
				// System.err.println(InsertOnTheEdge.insertOnTheEdge(pred, e, expStmt));
				// }
				// Elsewhere.
				// List<Node> elements = body.getInfo().getCFGInfo().getElementList();
				// if (elements.size() > 2) {
				// Node one = elements.get(0);
				// Node two = elements.get(1);
				// expStmt = FrontEnd.parseAndNormalize((0.5 + i++) + ";",
				// ExpressionStatement.class);
				// System.err.println(InsertOnTheEdge.insertOnTheEdge(one, two, expStmt));
				// }
			}

			// Predicates.
			if (n instanceof Expression) {
				int i = 10;
				if (!Misc.isCFGLeafNode(n)) {
					continue;
				}
				for (Node succ : new ArrayList<>(n.getInfo().getCFGInfo().getSuccessors())) {
					expStmt = FrontEnd.parseAndNormalize((10 + i++) + ";", ExpressionStatement.class);
					System.err.println(InsertOnTheEdge.insert(n, succ, expStmt));
				}
			}
		}
	}

	public static void testLabels() {
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		if (mainFunc == null) {
			System.exit(0);
		}
		Statement stmt = mainFunc.getInfo().getStatementWithLabel("l1");
		Statement newStmt = FrontEnd.parseAndNormalize("l2: X;", Statement.class);
		NodeReplacer.replaceNodes(stmt, newStmt);
		System.out.println(Program.getRoot());
		System.exit(0);
	}

	public static void testLCACS() {
		for (ExpressionStatement e1 : Misc.getInheritedEnclosee(Program.getRoot(), ExpressionStatement.class)) {
			for (ExpressionStatement e2 : Misc.getInheritedEnclosee(Program.getRoot(), ExpressionStatement.class)) {
				if (e1 == e2) {
					continue;
				}
				System.out.println(e1 + " and " + e2 + " in " + Misc.getLCAScope(e1, e2));
			}
		}

	}

	private static void testLoopingSingle() {
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main == null) {
			System.exit(0);
		}
		for (Node leafNode : main.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			if (leafNode instanceof BeginNode) {
				BeginNode beginNode = (BeginNode) leafNode;
				if (beginNode.getParent() instanceof SingleConstruct) {
					SingleConstruct singleCons = (SingleConstruct) beginNode.getParent();
					System.out.println("Single at line #" + Misc.getLineNum(singleCons) + " is "
							+ (singleCons.getInfo().isLoopedInPhase() ? "within" : "not within") + " a loop.");
				}
			}
		}
		System.exit(0);
	}

	public static void testMapMerge() {
		CellMap<String> firstMap = new CellMap<>();
		CellMap<String> secondMap = new CellMap<>();
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		CellSet allCells = new CellSet();
		for (Cell c : mainFunc.getInfo().getAccesses()) {
			allCells.add(c);
		}

		CellSet selected = new CellSet();
		for (Cell c : allCells) {
			if (c.toString().contains("y")) {
				firstMap.put(c, c.toString());
			}
			if (c.toString().contains("x")) {
				secondMap.put(c, "Meow");
			}
			if (c.toString().contains("z") || c.toString().contains("y")) {
				selected.add(c);
			}
		}
		// firstMap.updateGenericMap("G");

		System.out.println(firstMap);
		System.out.println(secondMap);
		System.out.println("===");
		firstMap.mergeWith(secondMap, (s1, s2) -> {
			String s3 = "";
			if (s1 != null) {
				s3 += s1;
			}
			s3 += s2;
			return s3;
		}, selected);
		System.out.println(firstMap);

		System.exit(0);

	}

	private static void testNewCellMap() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
		class MyString implements Immutable {
			public String s1;

			public MyString(String name) {
				s1 = name;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}
				if (obj == null) {
					return false;
				}
				if (getClass() != obj.getClass()) {
					return false;
				}
				MyString other = (MyString) obj;
				if (s1 == null) {
					if (other.s1 != null) {
						return false;
					}
				} else if (!s1.equals(other.s1)) {
					return false;
				}
				return true;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((s1 == null) ? 0 : s1.hashCode());
				return result;
			}
		}
		List<Symbol> symList = new ArrayList<>();
		// Alter comments for next 3 declarations;
		// CellMap<String> testMap;
		// List<CellMap<String>> cellMapList = new ArrayList<>();
		// CellMap<String> newMap;
		List<ExtensibleCellMap<MyString>> cellMapList = new ArrayList<>();
		ExtensibleCellMap<MyString> testMap;
		ExtensibleCellMap<MyString> newMap;
		int COUNT = 1000;

		for (int i = 0; i < COUNT; i++) {
			symList.add(new Symbol("s" + i, SignedIntType.type(), null, null));
		}
		int counter = 0;
		Random rand = new Random();
		int index = -1;
		long constTimer = System.nanoTime();
		while (counter < COUNT) {
			index++;
			int size = Math.max(1, Math.abs((rand.nextInt()) % (COUNT / 200)));
			size = Math.min(COUNT - counter, size);
			if (size < 1) {
				break;
			}
			counter += size;
			if (index == 0) {
				// newMap = new CellMap<>();
				newMap = new ExtensibleCellMap<>();
			} else {
				// newMap = new CellMap<>(cellMapList.get(cellMapList.size() - 1));
				newMap = new ExtensibleCellMap<>(cellMapList.get(cellMapList.size() - 1), 3);
			}
			cellMapList.add(newMap);
			System.out.println("Link Length: " + ExtensibleCellMap.getLinkLength(newMap));
			for (int i = 0; i < size; i++) {
				int symRandIndex = Math.abs(rand.nextInt() % COUNT);
				Symbol tempSym = symList.get(symRandIndex);
				// newMap.put(tempSym, tempSym.getName());
				newMap.put(tempSym, new MyString(tempSym.getName()));
			}
		}
		System.out.println("Total number of maps produced: " + cellMapList.size() + "; Time: "
				+ (System.nanoTime() - constTimer) / 1e9);

		int random = Math.max(0, Math.abs(rand.nextInt() % cellMapList.size()));
		testMap = cellMapList.get(random);

		// Perform random checks.
		long timer = System.nanoTime();
		for (int i = 0; i < 1e6; i++) {
			int randomSym = Math.max(0, Math.abs(rand.nextInt() % symList.size()));
			testMap.get(symList.get(randomSym));
			testMap.containsKey(symList.get(randomSym));
		}
		System.out.println("Time taken to perform operations: " + (System.nanoTime() - timer) / 1e9);

		System.exit(0);
	}

	public static void testNodeReplacedString() {
		for (WhileStatement whileStmt : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			System.out.println(
					whileStmt.getInfo().getNodeReplacedString(whileStmt.getInfo().getCFGInfo().getPredicate(), "33"));
		}
		System.exit(0);
	}

	private static void testNodeShifting() {
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		Statement stmt1 = main.getInfo().getStatementWithLabel("l1");
		Statement stmt2 = main.getInfo().getStatementWithLabel("l2");
		System.out.println(stmt1);
		System.out.println(stmt2);
		DumpSnapshot.dumpNestedCFG(Program.getRoot(), "first");
		InsertImmediatePredecessor.insert(stmt2, stmt1);
		DumpSnapshot.dumpNestedCFG(Program.getRoot(), "second");
		System.out.println(Program.getRoot());
		System.exit(0);
	}

	private static void testNoNullParent() {
		for (Node n : Misc.getInheritedEnclosee(Program.getRoot(), Node.class)) {
			if (n instanceof TranslationUnit) {
				continue;
			}
			if (n.getParent() == null) {
				String str = n.getInfo().getString();
				if (n instanceof BeginNode) {
					str = "BeginNode";
				} else if (n instanceof EndNode) {
					str = "EndNode";
				}
				System.out
						.println("Following node has some issue: " + str + " of type " + n.getClass().getSimpleName());
			}
		}

		for (FunctionDefinition func : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node cfgNode : func.getInfo().getCFGInfo().getLexicalCFGContents()) {
				if (cfgNode instanceof FunctionDefinition) {
					continue;
				}
				if (Misc.getEnclosingFunction(cfgNode) != func) {
					String str = cfgNode.getInfo().getString();
					if (cfgNode instanceof BeginNode) {
						str = "BeginNode";
					} else if (cfgNode instanceof EndNode) {
						str = "EndNode";
					}
					System.out.println(
							"Following node has some issue: " + str + " of type " + cfgNode.getClass().getSimpleName());
				}
				for (Node n : cfgNode.getInfo().getCFGInfo().getSuccBlocks()) {
					if (n instanceof ForConstruct) {
						ForConstructInfo forInfo = (ForConstructInfo) n.getInfo();
						if (!forInfo.getOmpClauseList().stream().anyMatch(c -> c instanceof NowaitClause)) {
							System.out.println("Following node has some issue: " + cfgNode + " of type "
									+ cfgNode.getClass().getSimpleName());

						}
					}
				}
				for (Node pred : cfgNode.getInfo().getCFGInfo().getPredBlocks()) {
					if (!pred.getInfo().getCFGInfo().getSuccBlocks().contains(cfgNode)) {
						System.out.println("Following pair are not connected bothways: " + pred + " with " + cfgNode);
					}
				}
				for (Node succ : cfgNode.getInfo().getCFGInfo().getSuccBlocks()) {
					if (!succ.getInfo().getCFGInfo().getPredBlocks().contains(cfgNode)) {
						System.out.println("Following pair are not connected bothways: " + cfgNode + " with " + succ);
					}
				}
			}
		}

		System.exit(0);

	}

	private static void testNormalization() {
		// CompoundStatement compStmt1 = FrontEnd.parseAndNormalize(
		// "{int y = 11; \n#pragma omp parallel for\nfor (i=0;i<10;i++){} int x = 10;}",
		// CompoundStatement.class);
		// ParallelForConstruct parForCons = FrontEnd.parseAndNormalize("\n#pragma omp
		// parallel for\nfor (i=0;i<10;i++){}",
		// ParallelForConstruct.class);
		// CompoundStatement compStmt2 = FrontEnd.parseAndNormalize(
		// "{int y = 12; y = 13; \n#pragma omp parallel sections\n{\n#pragma omp
		// section\n {int y = 0; y++;}} \n#pragma omp flush\nint x = 13;}",
		// CompoundStatement.class);
		ForConstruct forCons = FrontEnd.parseAndNormalize("\n#pragma omp for\nfor (i=0;i<10;i++){}",
				ForConstruct.class);
		// System.out.println(compStmt1);
		// System.out.println(parForCons);
		// compStmt2.getInfo().getCFGInfo().addAtLast(compStmt3);
		// System.out.println(compStmt2);
		for (DoStatement doStmt : Misc.getInheritedEnclosee(Program.getRoot().getInfo().getMainFunction(),
				DoStatement.class)) {
			doStmt.getInfo().getCFGInfo().setBody(forCons);
			System.out.println(doStmt);
			Expression exp = FrontEnd.parseAndNormalize("2 && foo()", Expression.class);
			doStmt.getInfo().getCFGInfo().setPredicate(exp);
		}
		// for (ForStatement forStmt :
		// Misc.getInheritedEnclosee(Program.getRoot().getInfo().getMainFunction(),
		// ForStatement.class)) {
		// Expression exp = FrontEnd.parseAndNormalize("4 || 5 + foo()",
		// Expression.class);
		// forStmt.getInfo().getCFGInfo().setTerminationExpression(exp);
		// ParallelForConstruct parForCons2 =
		// FrontEnd.parseAndNormalize(parForCons.toString(),
		// ParallelForConstruct.class);
		// forStmt.getInfo().getCFGInfo().setBody(parForCons2);
		// }
		// for (WhileStatement whileStmt :
		// Misc.getInheritedEnclosee(Program.getRoot().getInfo().getMainFunction(),
		// WhileStatement.class)) {
		// Expression exp = FrontEnd.parseAndNormalize("2, 3, 4", Expression.class);
		// whileStmt.getInfo().getCFGInfo().setPredicate(exp);
		// whileStmt.getInfo().getCFGInfo()
		// .setBody((Statement)
		// compStmt2.getInfo().getCFGInfo().getElementList().get(1));
		// }
		System.out.println(Program.getRoot());
		DumpSnapshot.dumpRoot("final");
		DumpSnapshot.dumpNestedCFG(Program.getRoot(), Program.fileName);
		System.exit(0);
	}

	private static void testPA_A4() {
		/*
		 * Step 1: Find all null-pointer or dangling-pointer dereferences, and
		 * the corresponding temporaries in which we try to save the value of
		 * these dereferences.
		 * Also, remove all such assignment statements.
		 */
		CellList riskyDerefs = new CellList();
		for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			for (CastExpression castExp : func.getInfo().getPointerDereferencedExpressionReads()) {
				Node cfgNode = Misc.getCFGNodeFor(castExp);
				for (Cell accessed : castExp.getInfo().getLocationsOf()) {
					boolean risky = false; // Set if this dereferenced access is either null or a dangling pointer.
					if (accessed.getPointsTo(cfgNode).contains(Cell.getNullCell())) {
						// This location is a null pointer.
						risky = true;
					} else {
						/*
						 * Now, if any element in points-to set of accessed
						 * location is invalid, then this location is a dangling
						 * pointer.
						 */
						for (Cell cell : accessed.getPointsTo(cfgNode)) {
							if (cell instanceof HeapCell) {
								HeapCell heapCell = (HeapCell) cell;
								if (!heapCell.isValidAt(cfgNode)) {
									risky = true;
									break;
								}
							}
						}
					}
					if (risky) {
						if (cfgNode instanceof ExpressionStatement) {
							Cell temporaryCopy;
							ExpressionStatement expStmt = (ExpressionStatement) cfgNode;
							for (Assignment assign : expStmt.getInfo().getLexicalAssignments()) {
								if (assign.rhs.toString().contains("*" + accessed)) {
									riskyDerefs.addAll(assign.getLHSLocations());
								}
							}
							System.out.println("Deleting the following assignment: " + expStmt);
							NodeRemover.removeNode(expStmt);
						}
					}
				}
			}
		}

		/*
		 * Step 2: Find and delete all print statements that may be reading from
		 * any riskyDerefs.
		 */
		for (CallStatement callStmt : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			if (callStmt.getFunctionDesignatorNode().toString().equals("printf")) {
				CellList reads = callStmt.getInfo().getReads();
				if (Misc.doIntersect(reads, riskyDerefs)) {
					System.out.println("Deleting the following print statement: " + callStmt);
					NodeRemover.removeNode(callStmt);
				}
			}
		}
		DumpSnapshot.dumpRoot("output");
		System.exit(0);
	}

	/**
	 * This sample pass adds a {@code printf} statement that prints {@code true},
	 * before all pointer-dereference writes
	 * (i.e., before stores, such as {@code *p = ...;}.
	 * <p>
	 * NOTE: To use this method, paste it in the {@code Main} class, and invoke it
	 * after {@code
	 * Program.parseAndNormalize(args);} in {@code Main.main()}.
	 */
	private static void testPA_Sample() {
		for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			for (CastExpression castExp : func.getInfo().getPointerDereferencedExpressionWrites()) {
				Node cfgNode = Misc.getCFGNodeFor(castExp);
				String printStr = "printf(\"true\");";
				Statement callStmt = FrontEnd.parseAndNormalize(printStr, Statement.class);
				InsertImmediatePredecessor.insert(cfgNode, callStmt);
			}
		}
		DumpSnapshot.dumpRoot("sample");
		System.exit(0);
	}

	private static void testParConsExpansionAndFusion() {
		ParallelConstructExpander.mergeParallelRegions(Program.getRoot());
		DumpSnapshot.dumpRoot("expanded");
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
		// Main.dumpReachingDefinitions("final");
		// Main.dumpVisibleSharedReadWrittenCells("final");
		// Main.dumpCopyInfo("final");
		// Main.dumpPhaseAndCopyInfo("final");
		System.exit(0);
	}

	public static void testPhaseAdditions() {
		/*
		 * BEGIN: Testing Automated Updates of Phases.
		 */
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		assert (main != null);
		for (WhileStatement itStmt : Misc.getInheritedEnclosee(main, WhileStatement.class)) {
			List<CriticalConstruct> critNode = Misc.getInheritedEncloseeList(itStmt, CriticalConstruct.class);
			CriticalConstruct crit = critNode.get(0);
			CompoundStatement compStmt = (CompoundStatement) Misc.getEnclosingBlock(crit);
			int index = compStmt.getInfo().getCFGInfo().getElementList().indexOf(crit);
			compStmt.getInfo().getCFGInfo().addElement(index,
					// FrontEnd.parseAndNormalize(
					// "{if (1) {#pragma omp barrier\n i=i; i;} else {#pragma omp barrier\n i; i;}
					// i;}", Statement.class));
					// FrontEnd.parseAndNormalize("foo();", Statement.class));
					// FrontEnd.parseAndNormalize("printf(\"10\");", Statement.class));
					// FrontEnd.parseAndNormalize("{i = i;}", Statement.class));
					// FrontEnd.parseAndNormalize("#pragma omp barrier\n", Statement.class));
					// FrontEnd.parseAndNormalize("i;", Statement.class));
					// FrontEnd.parseAndNormalize("int k;", Declaration.class));
					FrontEnd.parseAndNormalize("if (1) {break;}", Statement.class));
			// FrontEnd.parseAndNormalize("if (1) {goto lab;}", Statement.class));
			// FrontEnd.parseAndNormalize("if (1) {return;}", Statement.class));
			// FrontEnd.parseAndNormalize("if (1) {continue;}", Statement.class));
		}

		Program.getRoot().getInfo().removeExtraScopes();

		/*
		 * END: Testing Automated Updates of Phases.
		 */
		DumpSnapshot.dumpPhases("after");
		// dumpLiveness();
		System.exit(0);
	}

	public static void testPhaseInWhile() {
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		assert (main != null);
		for (WhileStatement itStmt : Misc.getInheritedEnclosee(main, WhileStatement.class)) {
			CompoundStatement oldStmt = (CompoundStatement) itStmt.getInfo().getCFGInfo().getBody();
			CompoundStatement copyStmt = FrontEnd.parseAlone(
					"{if (1) {foo(); #pragma omp barrier\n} else {#pragma omp barrier\nfoo();}}",
					CompoundStatement.class);
			itStmt.getInfo().getCFGInfo().setBody(copyStmt);
		}
		DumpSnapshot.dumpPhases("after");
		System.exit(0);
	}

	public static void testPhaseRemovals() {
		/*
		 * BEGIN: Testing Automated Updates of Phases.
		 */
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		assert (main != null);
		for (WhileStatement itStmt : Misc.getInheritedEnclosee(main, WhileStatement.class)) {
			List<CriticalConstruct> critNode = Misc.getInheritedEncloseeList(itStmt, CriticalConstruct.class);
			CriticalConstruct crit = critNode.get(0);
			CompoundStatement compStmt = (CompoundStatement) Misc.getEnclosingBlock(crit);
			int index = compStmt.getInfo().getCFGInfo().getElementList().indexOf(crit);
			compStmt.getInfo().getCFGInfo().removeElement(index + 2);
		}
		/*
		 * END: Testing Automated Updates of Phases.
		 */
		DumpSnapshot.dumpPhases("after");
		System.exit(0);
	}

	public static void testPhaseSwaps() {
		/*
		 * BEGIN: Testing Automated Updates of Phases.
		 */
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		assert (main != null);
		for (WhileStatement itStmt : Misc.getInheritedEnclosee(main, WhileStatement.class)) {
			List<CriticalConstruct> critNode = Misc.getInheritedEncloseeList(itStmt, CriticalConstruct.class);
			CriticalConstruct crit = critNode.get(0);
			CompoundStatement compStmt = (CompoundStatement) Misc.getEnclosingBlock(crit);
			int index = compStmt.getInfo().getCFGInfo().getElementList().indexOf(crit);

			BarrierDirective barrNode = Misc.getInheritedEncloseeList(itStmt, BarrierDirective.class).get(2);
			int newIndex = compStmt.getInfo().getCFGInfo().getElementList().indexOf(barrNode);

			Node element = compStmt.getInfo().getCFGInfo().getElementList().get(index + 2);
			compStmt.getInfo().getCFGInfo().removeElement(element);
			compStmt.getInfo().getCFGInfo().addElement(newIndex + 1, element);
		}
		/*
		 * END: Testing Automated Updates of Phases.
		 */
		DumpSnapshot.dumpPhases("after");
		System.exit(0);
	}

	public static void testPointerDereferenceGetter() {
		for (ExpressionStatement expStmt : Misc.getInheritedEnclosee(Program.getRoot(), ExpressionStatement.class)) {
			if (expStmt.getF0().present()) {
				Expression exp = (Expression) expStmt.getF0().getNode();
				List<CastExpression> accessReads = exp.getInfo().getPointerDereferencedExpressionReads();
				List<CastExpression> accessWrites = exp.getInfo().getPointerDereferencedExpressionWrites();
				if (!accessReads.isEmpty() || !accessWrites.isEmpty()) {
					System.err.println("For expression " + exp);
				}
				for (CastExpression ae : accessReads) {
					System.err.println("\tREAD:" + ae + ".");
				}
				for (CastExpression ae : accessWrites) {
					System.err.println("\tWRITE:" + ae + ".");
				}
			}
		}
		System.exit(0);
	}

	private static void testReachableUses() {
		FrontEnd.parseAndNormalize(
				"int main() {int x; int y; x = 10; y = x; l1: x = y + 10; y = ++x + 11; x = x + 3; y = x; x = x + 1;}");
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		ExpressionStatement stmt = (ExpressionStatement) main.getInfo().getStatementWithLabel("l1");
		Cell c = stmt.getInfo().getWrites().get(0);
		System.out.println(stmt.getInfo().getAllUsesForwardsExclusively(c));
		CellSet set = new CellSet();
		set.add(c);
		System.out.println(stmt.getInfo().getFirstPossibleKillersForwardExclusively(set));
		System.exit(0);

	}

	private static void testReversePostOrder() {
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main == null) {
			System.exit(0);
		}
		Node beginNode = main.getInfo().getCFGInfo().getNestedCFG().getBegin();

		long timer = System.nanoTime();
		List<Node> reversePostOrder = TraversalOrderObtainer.obtainReversePostOrder(beginNode,
				n -> n.getInfo().getCFGInfo().getInterProceduralLeafSuccessors());
		long finalTime = System.nanoTime() - timer;
		for (Node node : reversePostOrder) {
			if (node instanceof BeginNode) {
				System.err.println("B (" + node.getParent().getClass().getSimpleName() + ")");
			} else if (node instanceof EndNode) {
				System.err.println("E (" + node.getParent().getClass().getSimpleName() + ")");
			} else {
				System.err.println(node);
			}
		}
		for (int i = 0; i < reversePostOrder.size(); i++) {
			Node n1 = reversePostOrder.get(i);
			for (int j = i + 1; j < reversePostOrder.size(); j++) {
				Node n2 = reversePostOrder.get(j);
				if (n1 == n2) {
					System.err.println("WHY: " + n1 + "?");
				}
			}
		}
		System.err.println("Time spent in generating reverse postordering of the program nodes: "
				+ TraversalOrderObtainer.orderGenerationTime / (1e9 * 1.0) + "s.");
		System.exit(0);
	}

	private static void testSVEness() {
		for (WhileStatement whileStmt : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Expression exp : Misc.getExactEnclosee(whileStmt, Expression.class)) {
				if (Misc.isAPredicate(exp)) {
					System.out.println("\n*** Testing SVEness of " + exp);
					SVEChecker.isSingleValuedPredicate(exp);
				}
			}
			for (ExpressionStatement expStmt : Misc.getInheritedEnclosee(whileStmt, ExpressionStatement.class)) {
				SVEChecker.writesSingleValue(expStmt);
			}
		}
		System.exit(0);
	}

	private static void testSwapLength() {
		FrontEnd.parseAndNormalize(
				"int main () {int A; int B; int C; int D; \n#pragma omp parallel\n{A=B;B=C;C=D;D=A;}}");
		FunctionDefinition funcDef = Program.getRoot().getInfo().getMainFunction();
		assert (funcDef != null);
		for (ParallelConstruct parCons : Misc.getInheritedEnclosee(funcDef, ParallelConstruct.class)) {
			CompoundStatement parBody = (CompoundStatement) parCons.getInfo().getCFGInfo().getBody();
			for (Node elem : parBody.getInfo().getCFGInfo().getElementList()) {
				if (elem instanceof ExpressionStatement) {
					ExpressionStatement expStmt = (ExpressionStatement) elem;
					if (expStmt.getInfo().isCopyInstruction()) {
						System.out.println("Length of swap starting at " + expStmt + " is "
								+ CopyEliminator.swapLength(expStmt, parBody));
					}
				}
			}
		}
		System.exit(0);
	}

	private static void testLists() {
		totalTime = System.nanoTime();
		List<Integer> list = Arrays.asList(new Integer[(int) 5e7]);
		System.out.println("Time taken to construct: " + (System.nanoTime() - totalTime) / 1e9);
		totalTime = System.nanoTime();
		for (int i = 0; i < 5e7; i++) {
			list.set(i, 100);
		}
		System.out.println("Time taken to insert: " + (System.nanoTime() - totalTime) / 1e9);
		totalTime = System.nanoTime();
		list = new ArrayList<>();
		for (int i = 0; i < 5e7; i++) {
			list.add(i);
		}
		System.out.println("Time taken to construct and add: " + (System.nanoTime() - totalTime) / 1e9);
		System.exit(0);
	}

	private static void testUnreachables() {
		FrontEnd.parseAndNormalize(
				"int foo() {int p; p = 10; return p;} int main() {int x; x = 2; if (x > 3) {goto l2;}l1:while(x > 4){x = 11; foo();if(x>1){x = 10;goto l3;l2:;} x = 12; } l3: x = 14;}",
				TranslationUnit.class);
		WhileStatement whileStmt = (WhileStatement) Program.getRoot().getInfo().getStatementWithLabel("l1");
		whileStmt.getInfo().getCFGInfo().getEndUnreachableLeafHeaders().stream().forEach(e -> {
			System.out.println("E: " + e.getClass().getSimpleName());
		});
		whileStmt.getInfo().getCFGInfo().getBeginUnreachableLeafHeaders().stream().forEach(e -> {
			System.out.println("B: " + e.getClass().getSimpleName());
		});
		whileStmt.getInfo().getCFGInfo().getBothBeginAndEndReachableIntraTaskLeafNodes().stream().forEach(e -> {
			System.out.println(e);
		});
		System.exit(0);
	}

	public static void testValueConstraintsGenerator() {
		for (ExpressionStatement expStmt : Misc.getInheritedEnclosee(Program.getRoot(), ExpressionStatement.class)) {
			if (expStmt.getF0().present()) {
				Expression exp = (Expression) expStmt.getF0().getNode();
			}
		}
		System.exit(0);
	}

	public static void testZ3ExpressionCreator() {
		List<Tokenizable> tokenList = new ArrayList<>();
		// Constant constant = FrontEnd.parseAndNormalize("3.325127", Constant.class);
		Constant const1 = FrontEnd.parseAndNormalize("299.5", Constant.class);
		Constant const2 = FrontEnd.parseAndNormalize("4", Constant.class);
		NodeToken id1 = new NodeToken("id1");
		IdOrConstToken spe1 = new IdOrConstToken(id1, null);
		NodeToken id2 = new NodeToken("id2");
		IdOrConstToken spe2 = new IdOrConstToken(id2, null);
		IdOrConstToken spe3 = new IdOrConstToken(const1);
		IdOrConstToken spe4 = new IdOrConstToken(const2);
		tokenList.addAll(Arrays.asList(OperatorToken.ASSIGN, OperatorToken.MINUS, OperatorToken.MUL, spe4,
				OperatorToken.PLUS, spe3, spe1, spe2, spe2));
		System.out.println(tokenList);
		Context c = new Context();
		HashMap<String, ArithExpr> idMap = new HashMap<>();
		idMap.put(id1.toString(), c.mkIntConst(id1.toString()));
		idMap.put(id2.toString(), c.mkIntConst(id2.toString()));
		Expr finalExpr = ConstraintsGenerator.getZ3Expression(tokenList, idMap, c);
		System.out.println(finalExpr);
		Solver solver = c.mkSimpleSolver();
		if (finalExpr instanceof BoolExpr) {
			solver.add((BoolExpr) finalExpr);
		}
		Status st = solver.check();
		if (st == Status.SATISFIABLE) {
			Model m = solver.getModel();
			for (ArithExpr e : idMap.values()) {
				System.out.println(e + ": " + m.getConstInterp(e));
			}
		} else {
			System.out.println("No solution found for the system.");
		}
		System.exit(0);
	}

	public static void validateRDs() {
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main == null) {
			return;
		}
		for (Node leaf : main.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			for (Definition rdDef : leaf.getInfo().getReachingDefinitions()) {
				Node rd = rdDef.getDefiningNode();
				if (rd instanceof Declaration) {
					continue;
				}
				if (!rd.getInfo().isConnectedToProgram()) {
					Misc.exitDueToError("Found an unstable reaching-definition state: " + rd
							+ " is not connected to the program, and is yet considered to be a reaching definition for "
							+ leaf);
				}
			}
		}
	}

	public static class CriticalNode {
		public static void connect(CriticalNode cn1, CriticalNode cn2) {
			if (cn1.getSuccNodes().contains(cn2)) {
				return;
			}
			cn1.getSuccNodes().add(cn2);
			cn2.getPredNodes().add(cn1);
		}

		private final CriticalConstruct cc;
		private CellSet accessedCells;
		private List<CriticalNode> succNodes = new ArrayList<>();

		private List<CriticalNode> predNodes = new ArrayList<>();

		public CriticalNode(CriticalConstruct cc, CellSet accessedCells) {
			this.cc = cc;
			this.accessedCells = accessedCells;
			System.out.println("Creating node with " + accessedCells.size() + " entries.");
		}

		CellSet getAccessedCells() {
			return accessedCells;
		}

		CriticalConstruct getCriticalConstruct() {
			return cc;
		}

		List<CriticalNode> getPredNodes() {
			return predNodes;
		}

		List<CriticalNode> getSuccNodes() {
			return succNodes;
		}
	}

}
