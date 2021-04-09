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
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.*;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis;
import imop.lib.analysis.mhp.incMHP.MHPAnalyzer;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.builder.Builder;
import imop.lib.cfg.CFGGenerator;
import imop.lib.cfg.parallel.InterTaskEdge;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.getter.DummyExpressionNodeCounter;
import imop.lib.transform.simplify.CompoundStatementEnforcer;
import imop.lib.transform.simplify.ExpressionSimplifier;
import imop.lib.transform.simplify.ExpressionSimplifier.SimplificationString;
import imop.lib.transform.simplify.ImplicitBarrierRemover;
import imop.lib.transform.simplify.SplitCombinedConstructs;
import imop.lib.util.CellSet;
import imop.lib.util.CollectorVisitor;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Represents all the methods which help in the front-end setup, including
 * parsing of the input, expression simplification, etc.
 * 
 * @author aman
 *
 */
public class FrontEnd {

	/**
	 * This field is used as root for prototype declarations of built-in
	 * functions.
	 * It reads from "imop/parser/builtin.h".
	 */
	private static TranslationUnit otherBuiltins;
	/**
	 * This field is used as root for prototype declarations of library
	 * functions in
	 * MacPort GCC 6.2. It reads from "imop/parser/headerMac.h".
	 */
	private static TranslationUnit macBuiltins;
	/**
	 * This field is used as root for prototype declarations of library
	 * functions in
	 * GCC 4.4.7. It reads from "imop/parser/headerK2.h".
	 */
	private static TranslationUnit k2Builtins;

	/**
	 * Creates the input AST tree of the complete translation-unit represented
	 * by
	 * the string <code>parseText</code>. <br>
	 * This method does not perform any AST normalizations or initializations,
	 * except those below, which are part of the parsing process itself:
	 * <ol>
	 * <li>Creates the back link to the parent node for each node.</li>
	 * <li>Sets the <code>root</code> field of <code>Main</code>, if we are
	 * parsing
	 * a TranslationUnit.</li>
	 * <li>Removes all the labeled-statements, and represents labels as metadata
	 * instead.</li>
	 * <li>Ensures that the old-style of parameter declaration is replaced with
	 * new,
	 * for function prototypes.</li>
	 * <li>Ensures that unnamed structs/unions/enums are given a name.</li>
	 * </ol>
	 * 
	 * @param parseText
	 *                  string that holds the text of a complete translation-unit.
	 * @return root node of the input AST corresponding to the string
	 *         <code>parseText</code>.
	 */
	public static TranslationUnit parseCrude(String parseText) {
		Cell.allCells.clear();
		return FrontEnd.parseCrude(new ByteArrayInputStream(parseText.getBytes()));
	}

	/**
	 * Creates the input AST tree of the complete translation-unit represented
	 * by
	 * the stream <code>inputTextStream</code>. <br>
	 * This method does not perform any AST normalizations or initializations,
	 * except those below, which are part of the parsing process itself.
	 * <ol>
	 * <li>Creates the back link to the parent node for each node.</li>
	 * <li>Sets the <code>root</code> field of <code>Main</code>, if we are
	 * parsing
	 * a TranslationUnit.</li>
	 * <li>Removes all the labeled-statements, and represents labels as metadata
	 * instead.</li>
	 * <li>Ensures that the old-style of parameter declaration is replaced with
	 * new,
	 * for function prototypes.</li>
	 * <li>Ensures that unnamed structs/unions/enums are given a name.</li>
	 * </ol>
	 * 
	 * @param inputTextStream
	 *                        stream that holds the text of a complete
	 *                        translation-unit.
	 * @return root node of the input AST corresponding to the stream
	 *         <code>inputTextStream</code>.
	 */
	public static TranslationUnit parseCrude(InputStream inputTextStream) {
		return FrontEnd.parseCrude(inputTextStream, TranslationUnit.class);
	}

	public static <T extends Node> T parseCrude(String input, Class<T> nodeType) {
		return parseCrude(new ByteArrayInputStream(input.getBytes()), nodeType);
	}

	/**
	 * Simply parses the input, and performs the following steps:
	 * <ol>
	 * <li>Creates the back link to the parent node for each node.</li>
	 * <li>Sets the <code>root</code> field of <code>Main</code>, if we are
	 * parsing
	 * a TranslationUnit.</li>
	 * <li>Removes all the labeled-statements, and represents labels as metadata
	 * instead.</li>
	 * <li>Ensures that the old-style of parameter declaration is replaced with
	 * new,
	 * for function prototypes.</li>
	 * <li>Ensures that unnamed structs/unions/enums are given a name.</li>
	 * </ol>
	 * 
	 * @param input
	 *                 stream that holds the text to be parsed.
	 * @param nodeType
	 *                 non-terminal type with which the input stream has to be
	 *                 parsed.
	 * @return a reference to the root node of the AST obtained upon parsing
	 *         {@code input} as a {@code nodeType}.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Node> T parseCrude(InputStream input, Class<T> nodeType) {
		if (nodeType == TranslationUnit.class) {
			CParser.types.clear();
			CParser.types.put("__builtin_va_list", Boolean.TRUE);
			CParser.types.put("_Bool", Boolean.TRUE);
			CParser.types.put("bool", Boolean.TRUE);
		}

		input = FrontEnd.addNewLineAtEndIfNeeded(input, nodeType);

		T newNode = null;
		if (OmpClause.class.isAssignableFrom(nodeType)) {
			newNode = parseOmpClause(input, nodeType);
		} else if (nodeType == NodeToken.class) {
			return (T) new NodeToken(Misc.getStringFromInputStream(input));
		} else {
			// Before parsing a program, reset all global fields.
			if (nodeType == TranslationUnit.class) {
				Program.resetGlobalStaticFields();
			}
			CParser parser = new CParser(input);
			/** TEST CODE, suggested by Anshuman **/
			// for (Method tempMethod : CParser.class.getMethods()) {
			// try {
			// tempMethod = CParser.class.getDeclaredMethod(nodeType.getSimpleName(),
			// (Class<?>[]) null);
			// newNode = (T) tempMethod.invoke(parser);
			// } catch (NoSuchMethodException | SecurityException e1) {
			// continue;
			// } catch (IllegalAccessException e) {
			// continue;
			// } catch (IllegalArgumentException e) {
			// continue;
			// } catch (InvocationTargetException e) {
			// continue;
			// }
			// break;
			// }
			/** TEST CODE ends here. **/
			// if (newNode == null) {
			Method m = null;
			try {
				m = CParser.class.getDeclaredMethod(nodeType.getSimpleName(), (Class<?>[]) null);
				newNode = (T) m.invoke(parser);
			} catch (NoSuchMethodException | SecurityException e1) {
				System.err.println("Could not find method " + nodeType.getSimpleName() + "() in CParser.");
				// e1.printStackTrace();
				System.exit(1);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		if (nodeType == TranslationUnit.class) {
			Program.setRoot((TranslationUnit) newNode);
		}
		return newNode;
	}

	/**
	 * Creates the input AST tree of the complete translation-unit represented
	 * by
	 * the string <code>parseText</code>. <br>
	 * This method does not perform any AST normalizations or initializations,
	 * except those below, which are part of the parsing process itself:
	 * <ol>
	 * <li>Creates the back link to the parent node for each node.</li>
	 * <li>Sets the <code>root</code> field of <code>Main</code>, if we are
	 * parsing
	 * a TranslationUnit.</li>
	 * <li>Removes all the labeled-statements, and represents labels as metadata
	 * instead.</li>
	 * <li>Ensures that the old-style of parameter declaration is replaced with
	 * new,
	 * for function prototypes.</li>
	 * <li>Ensures that unnamed structs/unions/enums are given a name.</li>
	 * <li>Creates the CFG of the input. (Note that this step might fail if the
	 * input is not simplified.)</li>
	 * </ol>
	 * 
	 * @param parseText
	 *                  string that holds the text of a complete translation-unit.
	 * @return root node of the input AST corresponding to the string
	 *         <code>parseText</code>.
	 */
	public static TranslationUnit parseAlone(String parseText) {
		Cell.allCells.clear();
		return FrontEnd.parseAlone(new ByteArrayInputStream(parseText.getBytes()));
	}

	/**
	 * Creates the input AST tree of the complete translation-unit represented
	 * by
	 * the stream <code>inputTextStream</code>. <br>
	 * This method does not perform any AST normalizations or initializations,
	 * except those below, which are part of the parsing process itself.
	 * <ol>
	 * <li>Creates the back link to the parent node for each node.</li>
	 * <li>Sets the <code>root</code> field of <code>Main</code>, if we are
	 * parsing
	 * a TranslationUnit.</li>
	 * <li>Removes all the labeled-statements, and represents labels as metadata
	 * instead.</li>
	 * <li>Ensures that the old-style of parameter declaration is replaced with
	 * new,
	 * for function prototypes.</li>
	 * <li>Ensures that unnamed structs/unions/enums are given a name.</li>
	 * <br>
	 * CREATED/UPDATED: 11-Dec-2017 7:32:02 PM (Edited by: Aman) <br>
	 * <li>Creates the CFG of the input. (Note that this step might fail if the
	 * input is not simplified.)</li>
	 * </ol>
	 * 
	 * @param inputTextStream
	 *                        stream that holds the text of a complete
	 *                        translation-unit.
	 * @return root node of the input AST corresponding to the stream
	 *         <code>inputTextStream</code>.
	 */
	public static TranslationUnit parseAlone(InputStream inputTextStream) {
		return FrontEnd.parseAlone(inputTextStream, TranslationUnit.class);
	}

	public static <T extends Node> T parseAlone(String input, Class<T> nodeType) {
		return parseAlone(new ByteArrayInputStream(input.getBytes()), nodeType);
	}

	/**
	 * Simply parses the input, and performs the following steps:
	 * <ol>
	 * <li>Creates the back link to the parent node for each node.</li>
	 * <li>Sets the <code>root</code> field of <code>Main</code>, if we are
	 * parsing
	 * a TranslationUnit.</li>
	 * <li>Removes all the labeled-statements, and represents labels as metadata
	 * instead.</li>
	 * <li>Ensures that the old-style of parameter declaration is replaced with
	 * new,
	 * for function prototypes.</li>
	 * <li>Ensures that unnamed structs/unions/enums are given a name.</li>
	 * <br>
	 * CREATED/UPDATED: 11-Dec-2017 7:31:48 PM (Edited by: Aman) <br>
	 * <li>Creates the CFG of the input. (Note that this step might fail if the
	 * input is not simplified.)</li>
	 * </ol>
	 * 
	 * @param input
	 *                 stream that holds the text to be parsed.
	 * @param nodeType
	 *                 non-terminal type with which the input stream has to be
	 *                 parsed.
	 * @return a reference to the root node of the AST obtained upon parsing
	 *         {@code input} as a {@code nodeType}.
	 */
	public static <T extends Node> T parseAlone(InputStream input, Class<T> nodeType) {
		T newNode = FrontEnd.parseCrude(input, nodeType);
		CFGGenerator.createCFGEdgesIn(newNode);
		return newNode;
	}

	public static boolean isConstant(String str) {
		CParser parser = new CParser(new ByteArrayInputStream(str.getBytes()));
		try {
			Method m = CParser.class.getDeclaredMethod("Constant", (Class<?>[]) null);
			m.invoke(parser);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			return false;
		}
		return true;
	}

	/**
	 * Parses the {@code codeString} stream as a C program (and hence performs
	 * various passes that are implicit in
	 * {@link #parseAlone(InputStream, Class)}),
	 * and performs the following normalizations:
	 * <ol>
	 * <li>Makes all the implicit barriers explicit.</li>
	 * <li>Simplifies expressions, by carrying out the following:
	 * <ul>
	 * <li>Remove all usages of the logical AND (&&) and logical OR
	 * operators.</li>
	 * <li>Remove all usages of the conditional operator (?:).</li>
	 * <li>Remove all usages of the comma operator (,).</li>
	 * <li>Simplify function calls, to be of either of the following forms:
	 * <ul>
	 * <li><code>t = foo(t1, t2, ..., tn);</code></li>
	 * <li><code>foo(t1, t2, ..., tn);</code></li>
	 * </ul>
	 * where, <code>t, t1, t2, ... tn</code> are all either identifiers or
	 * constants.</li>
	 * <li>Those parentheses that directly wrap up a simple-primary expression,
	 * i.e., identifiers and constants, are removed.</li>
	 * <li>Split all the ParallelFor constructs into Parallel and For
	 * constructs.</li>
	 * <li>Split all the ParallelSection constructs into Parallel and Sections
	 * constructs.</li>
	 * <li>Splits declarators across multiple declarations (one declarator per
	 * declaration).
	 * </li>
	 * <li>Ensures that user-defined types (structs/unions/enums) are declared
	 * and used in different declarations.
	 * </li>
	 * </ul>
	 * <li>Populates all the symbol, type, and typedef tables.</li>
	 * <li>Removes extra level of compound-statement scopes.</li>
	 * <li>Populates CFG edges.</li>
	 * <li>Performs various other passes related to parallel programs, by
	 * invoking
	 * {@link #processParallelism(Node)}.</li>
	 * <li>Runs the following inter-task iterative data-flow analysis passes:
	 * <ul>
	 * <li>reaching-definition analysis pass (forward),</li>
	 * <li>liveness analysis pass (backward),</li>
	 * <li>data-dependence source analysis pass (forward), and</li>
	 * <li>data-dependence destination analysis pass (backward).</li>
	 * </ul>
	 * </li>
	 * </ol>
	 * 
	 * @param codeString
	 *                   string that contains text to be parsed as a C program.
	 * @return reference to the root node of the normalized AST of
	 *         {@code codeString} when parsed as a C program.
	 */
	public static TranslationUnit parseAndNormalize(String codeString) {
		return FrontEnd.parseAndNormalize(new ByteArrayInputStream(codeString.getBytes()), TranslationUnit.class);
	}

	/**
	 * Parses the {@code input} stream as a C program (and hence performs
	 * various
	 * passes that are implicit in {@link #parseAlone(InputStream, Class)}), and
	 * performs the following normalizations:
	 * <ol>
	 * <li>Makes all the implicit barriers explicit.</li>
	 * <li>Simplifies expressions, by carrying out the following:
	 * <ul>
	 * <li>Remove all usages of the logical AND (&&) and logical OR
	 * operators.</li>
	 * <li>Remove all usages of the conditional operator (?:).</li>
	 * <li>Remove all usages of the comma operator (,).</li>
	 * <li>Simplify function calls, to be of either of the following forms:
	 * <ul>
	 * <li><code>t = foo(t1, t2, ..., tn);</code></li>
	 * <li><code>foo(t1, t2, ..., tn);</code></li>
	 * </ul>
	 * where, <code>t, t1, t2, ... tn</code> are all either identifiers or
	 * constants.</li>
	 * <li>Those parentheses that directly wrap up a simple-primary expression,
	 * i.e., identifiers and constants, are removed.</li>
	 * <li>Split all the ParallelFor constructs into Parallel and For
	 * constructs.</li>
	 * <li>Split all the ParallelSection constructs into Parallel and Sections
	 * constructs.</li>
	 * <li>Splits declarators across multiple declarations (one declarator per
	 * declaration).
	 * </li>
	 * <li>Ensures that typedefs do not contain any implicit definitions of
	 * user-defined
	 * types.
	 * </li>
	 * <li>Ensures that user-defined types (structs/unions/enums) are declared
	 * and used in different declarations.
	 * </li>
	 * <li>Replace {@code foo(void)} with {@code foo()} in all
	 * FunctionDefinition nodes.</li>
	 * </ul>
	 * <li>Populates all the symbol, type, and typedef tables.</li>
	 * <li>Removes extra level of compound-statement scopes.</li>
	 * <li>Populates CFG edges.</li>
	 * <li>Performs various other passes related to parallel programs, by
	 * invoking
	 * {@link #processParallelism(Node)}.</li>
	 * <li>Runs the following inter-task iterative data-flow analysis passes:
	 * <ul>
	 * <li>reaching-definition analysis pass (forward),</li>
	 * <li>liveness analysis pass (backward),</li>
	 * <li>data-dependence source analysis pass (forward), and</li>
	 * <li>data-dependence destination analysis pass (backward).</li>
	 * </ul>
	 * </li>
	 * </ol>
	 * 
	 * @param input
	 *              stream that contains text to be parsed as a C program.
	 * @return reference to the root node of the normalized AST of {@code input}
	 *         when parsed as a C program.
	 */
	public static TranslationUnit parseAndNormalize(InputStream input) {
		long wholeTime = System.nanoTime();
		TranslationUnit newNode = parseCrude(input, TranslationUnit.class);
		long timeStart, timeTaken;

		if (Program.isPrePassPhase) {
			newNode = FrontEnd.prePass(newNode, wholeTime);
			if (!Program.proceedBeyond) {
				System.exit(0);
			} else {
				Misc.warnDueToLackOfFeature(
						"Note that the pre-pass phase does not guarantee automated consistency of various data structures, "
								+ "for efficiency reasons. "
								+ "Use pre-pass and following passes in two different executions of the framework.",
						Program.getRoot());
			}
		}

		System.err.println("Pass: Creating the control-flow graph.");
		timeStart = System.nanoTime();
		CFGGenerator.createCFGEdgesIn(newNode);
		timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
		DumpSnapshot.dumpNestedCFG(newNode, Program.fileName);

		System.err.println("\tStats: Number of leaf nodes: "
				+ Program.getRoot().getInfo().getCFGInfo().getIntraTaskCFGLeafContents().size() + ".");

		// Testing various traversals.
		// FrontEnd.testTraversals(newNode);

		/*
		 * Process/initialize parallelism related data structures.
		 */
		FrontEnd.processParallelism(newNode);

		System.err.println("\n** Number of cells encountered so far: " + Cell.allCells.size() + "\n");
		System.err.flush();
		// Testing various parallel traversals
		// FrontEnd.testParallelTraversals(newNode);

		/*
		 * If any node has incompatible type case, disable fieldSensitivity.
		 */
		FrontEnd.testIncompatibleTypeCasts(newNode);

		/*
		 * Run various data-flow analyses.
		 */
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		if (mainFunc != null) {

			// System.err.println("Pass: Performing points-to analysis.");
			// timeStart = System.nanoTime();
			// PointsToAnalysis pta = new PointsToAnalysis();
			// Program.basePointsTo = false;
			// Program.memoizeAccesses++;
			// pta.run(mainFunc);
			// Program.memoizeAccesses--;
			// timeTaken = System.nanoTime() - timeStart;
			// System.err.println("\tNodes processed " + pta.nodesProcessed + " times.");
			// System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
			DumpSnapshot.dumpPredicates("");

			System.err.println("Pass: Performing optimized points-to analysis.");
			timeStart = System.nanoTime();
			PointsToAnalysis pta = new PointsToAnalysis();
			Program.basePointsTo = false;
			Program.memoizeAccesses++;
			pta.run(mainFunc);
			Program.memoizeAccesses--;
			timeTaken = System.nanoTime() - timeStart;
			System.err.println("\tNodes processed " + pta.nodesProcessed + " times.");
			System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
			DumpSnapshot.dumpPointsTo("");
			// FrontEnd.dumpStatsOfIDFA(pta.tempMap);

			// Old code: This is executed in getIN() now, on demand.
			// System.err.println("Pass: Performing reaching-definition analysis.");
			// timeStart = System.nanoTime();
			// NodeInfo.rdDone = true;
			// ReachingDefinitionAnalysis rda = new ReachingDefinitionAnalysis();
			// rda.run(mainFunc);
			// timeTaken = System.nanoTime() - timeStart;
			// System.err.println("\tNodes processed " + rda.nodesProcessed + " times.");
			// System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
			// FrontEnd.dumpStatsOfIDFA(rda.tempMap);

			// NOTE: Following line is here only for testing purpose. It's safe to remove
			// it.
			// mainFunc.getInfo().getCFGInfo().getNestedCFG().getBegin().getInfo().getIN(AnalysisName.DOMINANCE);

			// System.err.println("Pass: Performing optimized reaching-definition
			// analysis.");
			// timeStart = System.nanoTime();
			// ReachingDefinitionAnalysis rda = new ReachingDefinitionAnalysis();
			// rda.run(mainFunc);
			// timeTaken = System.nanoTime() - timeStart;
			// System.err.println("\tNodes processed " + rda.nodesProcessed + " times.");
			// System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
			// FrontEnd.dumpStatsOfIDFA(rda.tempMap);
			// Old code: This is executed in getIN() now, on demand.
			// System.err.println("Pass: Performing liveness analysis.");
			// timeStart = System.nanoTime();
			// LivenessAnalysis lva = new LivenessAnalysis();
			// lva.run(mainFunc);
			// timeTaken = System.nanoTime() - timeStart;
			// System.err.println("\tNodes processed " + lva.nodesProcessed + " times.");
			// System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
			// FrontEnd.dumpStatsOfIDFA(lva.tempMap);

			// Old code: This is executed in getIN() now, on demand.
			// System.err.println("Pass: Performing data-dependence analysis.");
			// System.err.println("\tPerforming forward R/W analysis.");
			// timeStart = System.nanoTime();
			// DataDependenceForward ddf = new DataDependenceForward();
			// ddf.run(mainFunc);
			// timeTaken = System.nanoTime() - timeStart;
			// System.err.println("\tNodes processed (forward) " + ddf.nodesProcessed + "
			// times.");
			// System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
			// Main.dumpWriteSources();
			// FrontEnd.dumpStatsOfIDFA(ddf.tempMap);

			// System.err.println("Pass: Performing backward R/W analysis.");
			// timeStart = System.nanoTime();
			// NodeInfo.setReadWriteDestinations();
			// timeTaken = System.nanoTime() - timeStart;
			// System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");

			// NOTE: Calls to backward data-flow analysis have been disabled for efficiency
			// concerns.
			// Now, the required information about data-flow edge destinations is obtained
			// by inverting the source map.
			// System.err.println("\tPerforming backward R/W analysis.");
			// DataDependenceBackward ddb = new DataDependenceBackward();
			// timeStart = System.nanoTime();
			// ddb.run(mainFunc);
			// timeTaken = System.nanoTime() - timeStart;
			// System.err.println("\tNodes processed (backward) " + ddb.nodesProcessed + "
			// times.");
			// System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
			// System.out.println("For backward analysis: ");
			// FrontEnd.dumpStatsOfIDFA(ddb.tempMap);
		} else {
			System.err.println("Parsed not a main.");
		}
		System.err.println("Done with the basic front-end setup.");
		timeTaken = System.nanoTime() - wholeTime;
		System.err.println(
				"\tTotal time taken by the front-end (including diagnostics): " + timeTaken / 1000000000.0 + "s.\n");
		return newNode;
	}

	/**
	 * Parses the string {@code codeString} as a {@code nodeType} (and hence
	 * performs various passes that are implicit in
	 * {@link #parseAlone(InputStream, Class)}), and performs the following
	 * normalizations:
	 * <ol>
	 * <li>Makes all the implicit barriers explicit.</li>
	 * <li>In case when {@code nodeType} is a {@link TranslationUnit}, this
	 * method
	 * simplifies the expressions, by carrying out the following:
	 * <ul>
	 * <li>Remove all usages of the logical AND (&&) and logical OR
	 * operators.</li>
	 * <li>Remove all usages of the conditional operator (?:).</li>
	 * <li>Remove all usages of the comma operator (,).</li>
	 * <li>Simplify function calls, to be of either of the following forms:
	 * <ul>
	 * <li><code>t = foo(t1, t2, ..., tn);</code></li>
	 * <li><code>foo(t1, t2, ..., tn);</code></li>
	 * </ul>
	 * where, <code>t, t1, t2, ... tn</code> are all either identifiers or
	 * constants.</li>
	 * <li>Those parentheses that directly wrap up a simple-primary expression,
	 * i.e., identifiers and constants, are removed.</li>
	 * <li>Split all the ParallelFor constructs into Parallel and For
	 * constructs.</li>
	 * <li>Split all the ParallelSection constructs into Parallel and Sections
	 * constructs.</li>
	 * </ul>
	 * <li>Populates all the symbol, type, and typedef tables.</li>
	 * <li>Removes extra level of compound-statement scopes.</li>
	 * <li>Populates CFG edges.</li>
	 * </ol>
	 * 
	 * @param codeString
	 *                   string that contains text to be parsed as a
	 *                   {@code nodeType}.
	 * @param nodeType
	 *                   type as which {@code codeString} has to be parsed.
	 * 
	 * @return reference to the root node of the normalized AST of
	 *         {@code codeString} when parsed as a {@code nodeType}.
	 */
	public static <T extends Node> T parseAndNormalize(String codeString, Class<T> nodeType) {
		return FrontEnd.parseAndNormalize(new ByteArrayInputStream(codeString.getBytes()), nodeType);
	}

	/**
	 * Parses the {@code input} stream as a {@code nodeType} (and hence performs
	 * various passes that are implicit in
	 * {@link #parseAlone(InputStream, Class)}),
	 * and performs the following normalizations:
	 * <ol>
	 * <li>Makes all the implicit barriers explicit.</li>
	 * <li>In case when {@code nodeType} is a {@link TranslationUnit}, this
	 * method
	 * simplifies the expressions, by carrying out the following:
	 * <ul>
	 * <li>Remove all usages of the logical AND (&&) and logical OR
	 * operators.</li>
	 * <li>Remove all usages of the conditional operator (?:).</li>
	 * <li>Remove all usages of the comma operator (,).</li>
	 * <li>Simplify function calls, to be of either of the following forms:
	 * <ul>
	 * <li><code>t = foo(t1, t2, ..., tn);</code></li>
	 * <li><code>foo(t1, t2, ..., tn);</code></li>
	 * </ul>
	 * where, <code>t, t1, t2, ... tn</code> are all either identifiers or
	 * constants.</li>
	 * <li>Those parentheses that directly wrap up a simple-primary expression,
	 * i.e., identifiers and constants, are removed.</li>
	 * <li>Split all the ParallelFor constructs into Parallel and For
	 * constructs.</li>
	 * <li>Split all the ParallelSection constructs into Parallel and Sections
	 * constructs.</li>
	 * </ul>
	 * <li>Populates all the symbol, type, and typedef tables.</li>
	 * <li>Removes extra level of compound-statement scopes.</li>
	 * <li>Populates CFG edges.</li>
	 * </ol>
	 * 
	 * @param input
	 *                 stream that contains text to be parsed as a {@code nodeType}.
	 * @param nodeType
	 *                 type as which {@code input} has to be parsed.
	 * 
	 * @return reference to the root node of the normalized AST of {@code input}
	 *         when parsed as a {@code nodeType}.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Node> T parseAndNormalize(InputStream input, Class<T> nodeType) {
		if (nodeType.equals(TranslationUnit.class)) {
			return (T) FrontEnd.parseAndNormalize(input);
		}
		assert (!nodeType.equals(TranslationUnit.class));

		T newNode = parseCrude(input, nodeType);

		// newNode.accept(new ImplicitBarrierRemover());
		SplitCombinedConstructs.splitCombinedConstructsWithin(newNode);
		newNode.accept(new CompoundStatementEnforcer()); // This must be called before removal of implicit barriers.
		CFGGenerator.createCFGEdgesIn(newNode);

		// No normalizations are required if this is an empty CompoundStatement.
		if (Misc.getCFGNodeFor(newNode) instanceof CompoundStatement) {
			CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newNode);
			if (compStmt.getInfo().getCFGInfo().getElementList().isEmpty()) {
				return newNode;
			}
		}
		ImplicitBarrierRemover.removeImplicitBarrierDuringParsing(newNode); // This must be called only after
																			// enforcement of compound-statements.
		/*
		 * TODO: Uncomment the next line, after handling removal of scopes for
		 * disconnected snippets of code.
		 */
		// newNode.getInfo().removeExtraScopes();

		for (CompoundStatement compStmt : Misc.getInheritedEnclosee(newNode, CompoundStatement.class)) {
			compStmt.getInfo().getCFGInfo().initializeDummyFlushes();
		}

		// OLD CODE: Now, in order to remove spurious MHP relations, we do not invoke
		// initMHP on new/disconnected snippetss
		// for (ParallelConstruct parCons : Misc.getInheritedPostOrderEnclosee(newNode,
		// ParallelConstruct.class)) { // for all parCons
		// MHPAnalyzer mhp = new MHPAnalyzer(parCons);
		// mhp.initMHP(); // perform MHP Analysis
		// }
		// for (Node cfgNode :
		// newNode.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
		// cfgNode.getInfo().getNodePhaseInfo().rememberCurrentPhases();
		// }
		// Misc.createDataFlowGraph(newNode);
		/*
		 * If any node has incompatible type case, disable fieldSensitivity.
		 */
		FrontEnd.testIncompatibleTypeCasts(newNode);
		return newNode;
	}

	/**
	 * Parses {@code string} as a simplified function call, if possible.
	 * Otherwise,
	 * returns {@code null}.
	 * 
	 * @param string
	 *               input string that needs to be checked for representing a
	 *               simplified function call.
	 * @return root of the parse tree for the simplified function call
	 *         representing
	 *         {@code string}; {@code null}, if {@code string} does not
	 *         represent a
	 *         simplified function call.
	 */
	public static Expression parseACall(String string) {
		try {
			return new CParser(new ByteArrayInputStream(string.getBytes()), true).Expression();
		} catch (ParseException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static <T extends Node> T parseOmpClause(InputStream input, Class<T> nodeType) {
		assert (OmpClause.class.isAssignableFrom(nodeType));
		T newNode = null;
		if (nodeType == IfClause.class || nodeType == NumThreadsClause.class || nodeType == OmpCopyinClause.class
				|| nodeType == OmpDfltSharedClause.class || nodeType == OmpDfltNoneClause.class
				|| nodeType == OmpFirstPrivateClause.class || nodeType == OmpPrivateClause.class
				|| nodeType == OmpReductionClause.class || nodeType == OmpSharedClause.class) {
			Scanner scan1 = new Scanner(input);
			Scanner scanner = scan1.useDelimiter("\\A");
			String clauseText = scanner.next();
			scan1.close();
			scanner.close();
			ParallelConstruct parCons = FrontEnd.parseAndNormalize("#pragma omp parallel " + clauseText + " \n{}",
					ParallelConstruct.class);
			for (OmpClause clause : parCons.getInfo().getOmpClauseList()) {
				newNode = (T) clause;
				break;
			}
		} else if (nodeType == NowaitClause.class || nodeType == OmpLastPrivateClause.class) {
			Scanner scan1 = new Scanner(input);
			Scanner scanner = scan1.useDelimiter("\\A");
			String clauseText = scanner.next();
			scan1.close();
			scanner.close();
			SectionsConstruct secCons = FrontEnd.parseAndNormalize("#pragma omp sections " + clauseText + "\n {}",
					SectionsConstruct.class);
			for (OmpClause clause : secCons.getInfo().getOmpClauseList()) {
				newNode = (T) clause;
				break;
			}
		} else if (nodeType == FinalClause.class || nodeType == MergeableClause.class) {
			Scanner scan1 = new Scanner(input);
			Scanner scanner = scan1.useDelimiter("\\A");
			String clauseText = scanner.next();
			scan1.close();
			scanner.close();
			TaskConstruct taskCons = FrontEnd.parseAndNormalize("#pragma omp task " + clauseText + "\n {}",
					TaskConstruct.class);
			for (OmpClause clause : taskCons.getInfo().getOmpClauseList()) {
				newNode = (T) clause;
				break;
			}
		}
		return newNode;
	}

	/**
	 * Used by the static block of {#link {@link FrontEnd}} to parse various
	 * library
	 * and built-in methods.
	 * 
	 * @param input
	 *              a stream that contains prototype declarations for various
	 *              library
	 *              and built-in methods.
	 * @return a TranslationUnit which is used to obtain type information of
	 *         various
	 *         library and built-in methods.
	 */
	private static TranslationUnit parseBuiltin(InputStream input) {
		@SuppressWarnings("unchecked")
		HashMap<String, Boolean> oldTypeMap = (HashMap<String, Boolean>) CParser.types.clone();
		CParser.types.clear();
		CParser.types.put("__builtin_va_list", Boolean.TRUE);
		CParser.types.put("_Bool", Boolean.TRUE);
		CParser.types.put("bool", Boolean.TRUE);

		input = FrontEnd.addNewLineAtEndIfNeeded(input, TranslationUnit.class);

		TranslationUnit newNode = null;
		CParser parser = new CParser(input);
		Method m = null;
		try {
			m = CParser.class.getDeclaredMethod("TranslationUnit", (Class<?>[]) null);
			newNode = (TranslationUnit) m.invoke(parser);
		} catch (NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
			System.exit(1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			System.exit(1);
		}
		CParser.types = oldTypeMap;
		return newNode;
	}

	/**
	 * Adds a new line character at the end of the input stream, if there isn't
	 * one
	 * already, in case if the {@code nodeType} demands it.
	 * 
	 * @param input
	 *                 input stream representing some piece of code.
	 * @param nodeType
	 *                 type of the code represented by {@code input}
	 * @return an input stream (same as {@code input} if unchanged), that
	 *         contains a
	 *         line separator at the end of the code represented by
	 *         {@code input}.
	 */
	private static <T> InputStream addNewLineAtEndIfNeeded(InputStream input, Class<T> nodeType) {
		InputStream retStream = input;
		if (nodeType.equals(UnknownCpp.class) || nodeType.equals(UnknownPragma.class)
				|| nodeType.equals(BarrierDirective.class) || nodeType.equals(TaskyieldDirective.class)
				|| nodeType.equals(TaskwaitDirective.class) || nodeType.equals(FlushDirective.class)) {
			Scanner scan1 = new Scanner(input);
			Scanner s1 = scan1.useDelimiter("\\A");
			String codeString = s1.hasNext() ? s1.next() : "";
			scan1.close();
			s1.close();
			if (!codeString.endsWith(System.lineSeparator())) {
				codeString += System.lineSeparator();
			}
			retStream = new ByteArrayInputStream(codeString.getBytes());
		}
		return retStream;
	}

	/**
	 * Performs a simplification prepass on the input and exits.
	 * The simplified code is stored in {@code imop_useful.i}.
	 * <br>
	 * IMPORTANT NOTE: For efficiency reasons, automated updates are disabled in
	 * the pre-pass phase.
	 * 
	 * @param newNode
	 *                  root of the AST to be simplified.
	 * @param wholeTime
	 *                  start of the clock.
	 * @return
	 *         root of the simplified AST node.
	 */
	private static TranslationUnit prePass(TranslationUnit newNode, long wholeTime) {
		long timeStart, timeTaken;
		// AST constraints below
		/*
		 * OLD:
		 */
		Set<CompoundStatement> originalCS = Misc.getInheritedEnclosee(newNode, CompoundStatement.class);
		System.err.println("Pass: Simplifying the expressions.");
		timeStart = System.nanoTime();
		SimplificationString ret = newNode.accept(new ExpressionSimplifier(originalCS));
		timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
		Builder.counterVar = 0;
		newNode = FrontEnd.parseCrude(ret.getReplacementString().toString());
		ret = null;
		if (Program.fileName == null) {
			Program.fileName = "gen";
		}

		DumpSnapshot.printToFile(newNode, Program.fileName + "-simplified.i");
		/*
		 * : OLD.
		 */
		/*
		 * NEW:
		 */
		// SplitCombinedConstructs.splitCombinedConstructsWithin(newNode);
		/*
		 * : NEW.
		 */

		System.err.println("Pass: Enforcing scoping for bodies with single non-block statement.");
		timeStart = System.nanoTime();
		newNode.accept(new CompoundStatementEnforcer());
		timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
		DumpSnapshot.printToFile(newNode, Program.fileName + "-enforcer.i");

		System.err.println("Pass: Creating the control-flow graph.");
		timeStart = System.nanoTime();
		CFGGenerator.createCFGEdgesIn(newNode);
		timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
		DumpSnapshot.dumpNestedCFG(newNode, Program.fileName);

		/*
		 * NEW:
		 */
		// Normalization.normalizeLeafNodes(newNode, new ArrayList<>());
		// Misc.printToFile(newNode, Program.fileName + "-simplified.i");
		/*
		 * :NEW.
		 */

		System.err.println("Pass: Making implicit barriers explicit.");
		timeStart = System.nanoTime();
		ImplicitBarrierRemover.removeImplicitBarrierDuringParsing(newNode);
		timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
		DumpSnapshot.printToFile(newNode, Program.fileName + "-explicitBarriers.i");

		System.err.println("Pass: Removing extra scoping of compound statements.");
		timeStart = System.nanoTime();
		newNode.getInfo().removeExtraScopes();
		timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
		DumpSnapshot.printToFile(newNode, Program.fileName + "-scoped.i");

		// System.err.println("Pass: Creating the control-flow graph.");
		// timeStart = System.nanoTime();
		// newNode.accept(new CFGGenerator());
		// timeTaken = System.nanoTime() - timeStart;
		// System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
		// Misc.generateDotGraph(newNode, "base");
		//
		if (Program.removeUnused) {
			System.err.println("Pass: Removing declarations for unused elements.");
			timeStart = System.nanoTime();
			newNode.getInfo().removeUnusedElements();
			timeTaken = System.nanoTime() - timeStart;
			System.err.println("\tTime taken: " + timeTaken * 1.0 / 1e9 + "s.");
		}
		timeTaken = System.nanoTime() - wholeTime;
		DumpSnapshot.printToFile(newNode, Program.fileName + "-useful.i");
		System.err.println("Done with the basic pre-pass.");
		System.err.println("\tTotal time taken by the pre-pass: " + timeTaken * 1.0 / 1e9 + "s.\n");
		return newNode;
	}

	/**
	 * Runs the following parallelism-related passes on {@code root}, starting
	 * with
	 * the {@code main()} function, if any.
	 * <ol>
	 * <li>Insertion of dummy-flushes.</li>
	 * <li>Phase analysis.</li>
	 * <li>Population of inter-task data-flow edges.</li>
	 * <li>Lock-set analysis. (DISABLED)</li>
	 * </ol>
	 * 
	 * @param root
	 *             reference to the root of AST on which various parallelism
	 *             passes
	 *             have to be run.
	 */
	private static void processParallelism(TranslationUnit root) {
		System.err.println("Pass: Inserting dummy-flushes.");
		long timeStart = System.nanoTime();
		for (CompoundStatement compStmt : Misc.getInheritedEnclosee(root, CompoundStatement.class)) {
			compStmt.getInfo().getCFGInfo().initializeDummyFlushes();
		}
		long timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");

		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main == null) {
			return;
		}

		System.err.println("Pass: Marking the phases.");
		MHPAnalyzer.performMHPAnalysis(root);
		timeStart = System.nanoTime();
		timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
		DumpSnapshot.dumpPhases(Program.mhpUpdateCategory + "first");
		DumpSnapshot.dumpPhaseListOfAllParCons("first");

		System.err.println("Pass: Creating the inter-task data-flow graph.");
		timeStart = System.nanoTime();
		Misc.createDataFlowGraph(root);
		timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");

		System.err.println("DISABLED Pass: Performing the lock-set analysis.");
		// timeStart = System.nanoTime();
		// LockSetAnalysis lockSetAnalysis = new LockSetAnalysis();
		// lockSetAnalysis.run(Program.root.getInfo().getMainFunction());
		// timeTaken = System.nanoTime() - timeStart;
		// System.err.println("\tNodes processed " + lockSetAnalysis.nodesProcessed + "
		// times.");
		// System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");

	}

	/**
	 * Performs various statistics collecting routines.
	 * 
	 * @param root
	 */
	public static void checkStats(TranslationUnit root) {
		DummyExpressionNodeCounter nodeCounter = new DummyExpressionNodeCounter();
		root.accept(nodeCounter);
		System.out.println("There were a total of " + nodeCounter.totalNumberofNodes + " objects out of which "
				+ (nodeCounter.totalNumberofChoiceObjects * 100.0) / nodeCounter.totalNumberofNodes
				+ "% were uselss choice objects.");
		System.out.println("There were a total of " + nodeCounter.totalExpressionObjects + " expressions out of which "
				+ (nodeCounter.totalNumberofWrapperExpressions * 100.0) / nodeCounter.totalExpressionObjects
				+ "% were uselss wrapper expressions.");
	}

	/**
	 * @param root
	 */
	public static void dumpDataFlowGraph() {
		for (DummyFlushDirective df : Misc.getInheritedEnclosee(Program.getRoot(), DummyFlushDirective.class)) {
			System.out.println("FOR: " + df);
			System.out.println("PREDs: ");
			for (InterTaskEdge itE : df.getInfo().getIncomingInterTaskEdges()) {
				System.out.println(itE.getSourceNode());
			}
			System.out.println("SUCCs: ");
			for (InterTaskEdge itE : df.getInfo().getOutgoingInterTaskEdges()) {
				System.out.println(itE.getDestinationNode());
			}
			System.out.println();
			System.out.println();

		}
	}

	public static String getAccessString(Node n) {
		CellSet readList = new CellSet(n.getInfo().getReads());
		CellSet writeList = new CellSet(n.getInfo().getWrites());
		String retString = "";
		if (readList.size() != 0) {
			retString = "reads(";
			if (readList.isUniversal()) {
				retString += "__generic_stack/__generic_heap;";
			} else {
				for (Cell cell : readList) {
					if (cell instanceof HeapCell) {
						HeapCell heapCell = (HeapCell) cell;
						if (heapCell == Cell.genericCell) {
							retString += "__generic_heap; ";
						} else {
							retString += "heap: #" + heapCell.heapId + "; ";
						}
					} else if (cell instanceof Symbol) {
						Symbol sym = (Symbol) cell;
						if (sym == Cell.genericCell) {
							retString += "__generic_stack; ";
						} else {
							retString += sym.getName() + "; ";
						}
					} else if (cell instanceof FreeVariable) {
						retString += "(f):" + ((FreeVariable) cell).getNodeToken().getTokenImage() + ": ";
					} else if (cell instanceof AddressCell) {
						Cell pointee = ((AddressCell) cell).getPointedElement();
						if (pointee instanceof Symbol) {
							retString += "(&):" + ((Member) pointee).getName() + "; ";
						} else if (pointee instanceof HeapCell) {
							retString += "(&):heap#" + ((HeapCell) pointee).getLocation() + "; ";
						}
					} else if (cell instanceof FieldCell) {
						retString += ((FieldCell) cell).getAggregateElement().getName() + ".f;";
					}
				}
			}
			retString += "); ";
		}
		if (writeList.size() != 0) {
			retString += "writes(";
			if (writeList.isUniversal()) {
				retString += "__generic_stack/__generic_heap;";
			} else {
				for (Cell cell : writeList) {
					if (cell instanceof HeapCell) {
						HeapCell heapCell = (HeapCell) cell;
						if (heapCell == Cell.genericCell) {
							retString += "__generic_heap; ";
						} else {
							retString += "heap: #" + heapCell.heapId + "; ";
						}
					} else if (cell instanceof Symbol) {
						Symbol sym = (Symbol) cell;
						if (sym == Cell.genericCell) {
							retString += "__generic_stack; ";
						} else {
							retString += sym.getName() + "; ";
						}
					} else if (cell instanceof FreeVariable) {
						retString += "(f):" + ((FreeVariable) cell).getNodeToken().getTokenImage() + ": ";
					} else if (cell instanceof AddressCell) {
						Cell pointee = ((AddressCell) cell).getPointedElement();
						if (pointee instanceof Symbol) {
							retString += "(&):" + ((Member) pointee).getName() + "; ";
						} else if (pointee instanceof HeapCell) {
							retString += "(&):heap#" + ((HeapCell) pointee).getLocation() + "; ";
						}
					} else if (cell instanceof FieldCell) {
						retString += ((FieldCell) cell).getAggregateElement().getName() + ".f;";
					}
				}
			}
			retString += ");";
		}
		return retString;
	}

	public static TranslationUnit getOtherBuiltins() {
		if (otherBuiltins == null) {
			try {
				String filePath = Program.class.getProtectionDomain().getCodeSource().getLocation().getFile();
				String fs = System.getProperty("file.separator");
				System.setIn(new FileInputStream(
						filePath + ".." + fs + "src" + fs + "imop" + fs + "parser" + fs + "builtin.h"));
				FrontEnd.otherBuiltins = FrontEnd.parseBuiltin(System.in);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		return otherBuiltins;
	}

	public static TranslationUnit getMacBuiltins() {
		if (macBuiltins == null) {
			try {
				String filePath = Program.class.getProtectionDomain().getCodeSource().getLocation().getFile();
				String fs = System.getProperty("file.separator");
				System.setIn(new FileInputStream(
						filePath + ".." + fs + "src" + fs + "imop" + fs + "parser" + fs + "headerMac.h"));
				FrontEnd.macBuiltins = FrontEnd.parseBuiltin(System.in);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(0);
			}

		}
		return macBuiltins;
	}

	public static TranslationUnit getK2Builtins() {
		if (k2Builtins == null) {
			try {
				String filePath = Program.class.getProtectionDomain().getCodeSource().getLocation().getFile();
				String fs = System.getProperty("file.separator");
				System.setIn(new FileInputStream(
						filePath + ".." + fs + "src" + fs + "imop" + fs + "parser" + fs + "headerK2.h"));
				FrontEnd.k2Builtins = FrontEnd.parseBuiltin(System.in);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		return k2Builtins;
	}

	public static void testParallelTraversals(Node root) {
		for (Node aCFGNode : root.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
			if (aCFGNode instanceof DummyFlushDirective) {
				DummyFlushDirective dF = (DummyFlushDirective) aCFGNode;
				if (!dF.getInfo().containsLabel("testThis")) {
					continue;
				}
				for (Node encloser : dF.getInfo().getCFGInfo().getInterTaskLeafPredecessorNodes()) {
					if (encloser instanceof EndNode) {
						continue;
					}
					if (encloser instanceof BeginNode) {
						continue;
					}
					System.out.println(encloser.getClass().getSimpleName() + ">>>>>>>\n\t" + encloser);
					System.out.println();
				}
				for (Node encloser : dF.getInfo().getCFGInfo().getInterTaskLeafSuccessorNodes()) {
					if (encloser instanceof EndNode) {
						continue;
					}
					if (encloser instanceof BeginNode) {
						continue;
					}
					System.out.println(encloser.getClass().getSimpleName() + ">>>>>>>\n\t" + encloser);
					System.out.println();
				}
				break;
			}
		}
	}

	/**
	 * A temporary testing method that tries out some traversal mechanisms.
	 * 
	 * @param root
	 *             root of an AST.
	 */
	public static void testTraversals(Node root) {
		// for (Node n : Main.root.getInfo().getAllLeafNodesInTheProgram()) {
		// if (n.getInfo().getReads().isUniversal()) {
		// System.out.println("***\n Universal reads: " + n);
		// }
		// if (n.getInfo().getWrites().isUniversal()) {
		// System.out.println("***\n Universal writes: " + n);
		// }
		// }
		//

		if (NodeInfo.class.hashCode() > 0) {
			return;
		}
		for (Node aCFGNode : root.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
			if (aCFGNode instanceof ExpressionStatement) {
				ExpressionStatement expStmt = (ExpressionStatement) aCFGNode;
				Statement labeledStmt = expStmt.getInfo().getStatementWithLabel("testThis");
				if (labeledStmt == null) {
					continue;
				}
				// for (Node encloser :
				// labeledStmt.getInfo().getAllLexicalNonLeafEnclosersExclusive()) {
				// System.out.println(encloser.getClass().getSimpleName() + ">>>>>>>\n\t" +
				// encloser);
				// System.out.println();
				// }
				// for (Node encloser : labeledStmt.getInfo().getNonLeafNestingPathExclusive())
				// {
				// System.out.println(encloser.getClass().getSimpleName() + ">>>>>>>\n\t" +
				// encloser);
				// System.out.println();
				// }
				// for (Node encloser :
				// CollectorVisitor.collectNodesIntraTaskForward(labeledStmt, new HashSet<>(),
				// (n) -> false)) {
				// if (encloser instanceof EndNode) {
				// continue;
				// }
				// if (encloser instanceof BeginNode) {
				// continue;
				// }
				// System.out.println(encloser.getClass().getSimpleName() + ">>>>>>>\n\t" +
				// encloser);
				// System.out.println();
				// }
				// for (Node encloser :
				// CollectorVisitor.collectNodesIntraTaskBackward(labeledStmt, new HashSet<>(),
				// (n) -> false)) {
				// if (encloser instanceof EndNode) {
				// continue;
				// }
				// if (encloser instanceof BeginNode) {
				// continue;
				// }
				// System.out.println(encloser.getClass().getSimpleName() + ">>>>>>>\n\t" +
				// encloser);
				// System.out.println();
				// }
				// for (Node encloser :
				// labeledStmt.getInfo().getCFGInfo().getIntraTaskCFGLeafReachablesBackward()) {
				// if (encloser instanceof EndNode) {
				// continue;
				// }
				// if (encloser instanceof BeginNode) {
				// continue;
				// }
				// System.out.println(encloser.getClass().getSimpleName() + ">>>>>>>\n\t" +
				// encloser);
				// System.out.println();
				// }
				break;
			}
		}
		for (FunctionDefinition func : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			Statement stmt = func.getInfo().getStatementWithLabel("testThis");
			if (stmt == null) {
				continue;
			}
			// for (Node encloser :
			// stmt.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			// if (encloser instanceof EndNode) {
			// continue;
			// }
			// if (encloser instanceof BeginNode) {
			// continue;
			// }
			// System.out.println(encloser.getClass().getSimpleName() + ">>>>>>>\n\t" +
			// encloser);
			// System.out.println();
			// }
			NodeWithStack nWS = new NodeWithStack(stmt, new CallStack());
			for (NodeWithStack encloserWithStack : CollectorVisitor.collectNodesIntraTaskForwardBarrierFreePath(nWS,
					new HashSet<>())) {
				Node encloser = encloserWithStack.getNode();
				if (encloser instanceof EndNode) {
					continue;
				}
				if (encloser instanceof BeginNode) {
					continue;
				}
				System.out.println(encloser.getClass().getSimpleName() + ">>>>>>>\n\t" + encloser + " with "
						+ encloserWithStack.getCallStack());
				System.out.println();
			}
		}
	}

	/**
	 * Ensures that field-sensitivity is disabled if there are any incompatible
	 * type-casts to pointers, present in the program.
	 * 
	 * @param rootNode
	 *                 node within which incompatible type-casts have to be checked
	 *                 for.
	 */
	private static void testIncompatibleTypeCasts(Node rootNode) {
		if (!Program.fieldSensitive) {
			return;
		}
		for (CastExpressionTyped leaf : Misc.getInheritedEnclosee(rootNode, CastExpressionTyped.class)) {
			if (Type.hasIncompatibleTypeCastOfPointers(leaf)) {
				Misc.warnDueToLackOfFeature(
						"Disabling field-sensitivity, due to the presence of following incompatible type cast: " + leaf,
						leaf);
				Program.fieldSensitive = false;
				return;
			}
		}
	}

	@SuppressWarnings("unused")
	private static void dumpStatsOfIDFA(HashMap<Node, Integer> tempMap) {
		String str = "";
		for (Node n : tempMap.keySet()) {
			str += Misc.getLineNum(n) + ": " + n + ": " + tempMap.get(n) + "\n";
		}
		DumpSnapshot.printToFile(str, "ddfOut.i");
	}

	/**
	 * The C Parser class, generated by JavaCC/JTB.
	 */
	private static class CParser implements CParserConstants {

		/**
		 * Used to throw a parsing error when we encounter a non-function string
		 * while
		 * trying to parse a function.
		 */
		public boolean parseACall = false;
		/**
		 * This bit has meaning only when parseACall is set.
		 * When this bit is reset, we accept only call-statements; when this is
		 * set, we accept only SimplePrimaryExpressions. Initially, this bit is
		 * reset.
		 */
		public boolean parseAnSPE = false;

		public static HashMap<String, Boolean> types;
		static {
			types = new HashMap<>();
			types.put("__builtin_va_list", Boolean.TRUE);
			types.put("bool", Boolean.TRUE);
			types.put("_Bool", Boolean.TRUE);
		}
		public boolean flag = true;

		public Stack<Boolean> typedefParsingStack = new Stack<>();

		public boolean isType(String type) {
			if (types.get(type) != null) {
				return true;
			}
			return false;
		}

		public void addType(String type) {
			types.put(type, Boolean.TRUE);
		}

		@SuppressWarnings("unused")
		final public TranslationUnit TranslationUnit() throws ParseException {
			NodeList n0 = new NodeList();
			ElementsOfTranslation n1;
			label_1: while (true) {
				n1 = ElementsOfTranslation();
				n0.addNode(n1);
				if (jj_2_1(1)) {
					;
				} else {
					break label_1;
				}
			}
			n0.trimToSize();
			{

				return new TranslationUnit(n0);
			}

		}

		final public ElementsOfTranslation ElementsOfTranslation() throws ParseException {
			NodeChoice n0;
			ExternalDeclaration n1;
			UnknownCpp n2;
			UnknownPragma n3;
			if (jj_2_2(3)) {
				n1 = ExternalDeclaration();
				n0 = new NodeChoice(n1, 0);
			} else if (jj_2_3(3)) {
				n2 = UnknownCpp();
				n0 = new NodeChoice(n2, 1);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case CROSSBAR:
					n3 = UnknownPragma();
					n0 = new NodeChoice(n3, 2);
					break;
				default:
					jj_la1[0] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new ElementsOfTranslation(n0);
			}

		}

		final public ExternalDeclaration ExternalDeclaration() throws ParseException {
			NodeChoice n0;
			Declaration n1;
			FunctionDefinition n2;
			DeclareReductionDirective n3;
			ThreadPrivateDirective n4;
			if (jj_2_4(2147483647)) {
				n1 = Declaration();
				n0 = new NodeChoice(n1, 0);
			} else if (jj_2_5(1)) {
				n2 = FunctionDefinition();
				n0 = new NodeChoice(n2, 1);
			} else if (jj_2_6(4)) {
				n3 = DeclareReductionDirective();
				n0 = new NodeChoice(n3, 2);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case CROSSBAR:
					n4 = ThreadPrivateDirective();
					n0 = new NodeChoice(n4, 3);
					break;
				default:
					jj_la1[1] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new ExternalDeclaration(n0);
			}

		}

		final public FunctionDefinition FunctionDefinition() throws ParseException {
			NodeOptional n0 = new NodeOptional();
			DeclarationSpecifiers n1;
			Declarator n2;
			NodeOptional n3 = new NodeOptional();
			DeclarationList n4;
			CompoundStatement n5;
			if (jj_2_7(2147483647)) {
				n1 = DeclarationSpecifiers();
				n0.addNode(n1);
			} else {
				;
			}
			n2 = Declarator();
			if (jj_2_8(1)) {
				n4 = DeclarationList();
				n3.addNode(n4);
			} else {
				;
			}
			n5 = CompoundStatement();
			{

				return new FunctionDefinition(n0, n2, n3, n5);
			}

		}

		final public Declaration Declaration() throws ParseException {
			DeclarationSpecifiers n0;
			NodeOptional n1 = new NodeOptional();
			InitDeclaratorList n2;
			NodeToken n3;
			Token n4;
			n0 = DeclarationSpecifiers();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_MUL:
			case OP_BITXOR:
			case LEFTPAREN:
			case IDENTIFIER:
				n2 = InitDeclaratorList();
				n1.addNode(n2);
				break;
			default:
				jj_la1[2] = jj_gen;
				;
			}
			n4 = jj_consume_token(SEMICOLON);
			n3 = JTBToolkit.makeNodeToken(n4);
			{

				return new Declaration(n0, n1, n3);
			}

		}

		final public DeclarationList DeclarationList() throws ParseException {
			NodeList n0 = new NodeList();
			Declaration n1;
			label_2: while (true) {
				n1 = Declaration();
				n0.addNode(n1);
				if (jj_2_9(2147483647)) {
					;
				} else {
					break label_2;
				}
			}
			n0.trimToSize();
			{

				return new DeclarationList(n0);
			}

		}

		final public DeclarationSpecifiers DeclarationSpecifiers() throws ParseException {
			NodeList n0 = new NodeList();
			ADeclarationSpecifier n1;
			label_3: while (true) {
				n1 = ADeclarationSpecifier();
				n0.addNode(n1);
				if (jj_2_10(1)) {
					;
				} else {
					break label_3;
				}
			}
			n0.trimToSize();
			{

				return new DeclarationSpecifiers(n0);
			}

		}

		final public ADeclarationSpecifier ADeclarationSpecifier() throws ParseException {
			NodeChoice n0;
			StorageClassSpecifier n1;
			TypeSpecifier n2;
			TypeQualifier n3;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case REGISTER:
			case TYPEDEF:
			case EXTERN:
			case STATIC:
			case AUTO:
				n1 = StorageClassSpecifier();
				n0 = new NodeChoice(n1, 0);
				break;
			default:
				jj_la1[3] = jj_gen;
				if (jj_2_11(1)) {
					n2 = TypeSpecifier();
					n0 = new NodeChoice(n2, 1);
				} else {
					switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
					case RESTRICT:
					case VOLATILE:
					case CCONST:
					case INLINE:
					case CINLINED:
					case CINLINED2:
					case CSIGNED:
					case CSIGNED2:
					case CONST:
					case EXTENSION:
					case CATOMIC:
					case COMPLEX:
						n3 = TypeQualifier();
						n0 = new NodeChoice(n3, 2);
						break;
					default:
						jj_la1[4] = jj_gen;
						jj_consume_token(-1);
						throw new ParseException();
					}
				}
			}
			{

				return new ADeclarationSpecifier(n0);
			}

		}

		final public StorageClassSpecifier StorageClassSpecifier() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			NodeToken n7;
			Token n8;
			NodeToken n9;
			Token n10;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case AUTO:
				n2 = jj_consume_token(AUTO);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case REGISTER:
				n4 = jj_consume_token(REGISTER);
				n3 = JTBToolkit.makeNodeToken(n4);
				n0 = new NodeChoice(n3, 1);
				break;
			case STATIC:
				n6 = jj_consume_token(STATIC);
				n5 = JTBToolkit.makeNodeToken(n6);
				n0 = new NodeChoice(n5, 2);
				break;
			case EXTERN:
				n8 = jj_consume_token(EXTERN);
				n7 = JTBToolkit.makeNodeToken(n8);
				n0 = new NodeChoice(n7, 3);
				break;
			case TYPEDEF:
				n10 = jj_consume_token(TYPEDEF);
				n9 = JTBToolkit.makeNodeToken(n10);
				flag = true;
				typedefParsingStack.push(Boolean.TRUE);
				n0 = new NodeChoice(n9, 4);
				break;
			default:
				jj_la1[5] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new StorageClassSpecifier(n0);
			}

		}

		final public TypeSpecifier TypeSpecifier() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			NodeToken n7;
			Token n8;
			NodeToken n9;
			Token n10;
			NodeToken n11;
			Token n12;
			NodeToken n13;
			Token n14;
			NodeToken n15;
			Token n16;
			NodeToken n17;
			Token n18;
			StructOrUnionSpecifier n19;
			EnumSpecifier n20;
			TypedefName n21;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case VOID:
				n2 = jj_consume_token(VOID);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case CHAR:
				n4 = jj_consume_token(CHAR);
				n3 = JTBToolkit.makeNodeToken(n4);
				n0 = new NodeChoice(n3, 1);
				break;
			case SHORT:
				n6 = jj_consume_token(SHORT);
				n5 = JTBToolkit.makeNodeToken(n6);
				n0 = new NodeChoice(n5, 2);
				break;
			case INT:
				n8 = jj_consume_token(INT);
				n7 = JTBToolkit.makeNodeToken(n8);
				n0 = new NodeChoice(n7, 3);
				break;
			case LONG:
				n10 = jj_consume_token(LONG);
				n9 = JTBToolkit.makeNodeToken(n10);
				n0 = new NodeChoice(n9, 4);
				break;
			case FLOAT:
				n12 = jj_consume_token(FLOAT);
				n11 = JTBToolkit.makeNodeToken(n12);
				n0 = new NodeChoice(n11, 5);
				break;
			case DOUBLE:
				n14 = jj_consume_token(DOUBLE);
				n13 = JTBToolkit.makeNodeToken(n14);
				n0 = new NodeChoice(n13, 6);
				break;
			case SIGNED:
				n16 = jj_consume_token(SIGNED);
				n15 = JTBToolkit.makeNodeToken(n16);
				n0 = new NodeChoice(n15, 7);
				break;
			case UNSIGNED:
				n18 = jj_consume_token(UNSIGNED);
				n17 = JTBToolkit.makeNodeToken(n18);
				n0 = new NodeChoice(n17, 8);
				break;
			case STRUCT:
			case UNION:
				n19 = StructOrUnionSpecifier();
				n0 = new NodeChoice(n19, 9);
				break;
			case ENUM:
				n20 = EnumSpecifier();
				n0 = new NodeChoice(n20, 10);
				break;
			default:
				jj_la1[6] = jj_gen;
				if ((!typedefParsingStack.empty() && typedefParsingStack.peek().booleanValue() && flag == true)
						|| ((typedefParsingStack.empty() || !typedefParsingStack.peek().booleanValue())
								&& isType(getToken(1).image))) {
					n21 = TypedefName();
					n0 = new NodeChoice(n21, 11);
				} else {
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			if ((!typedefParsingStack.empty()) && typedefParsingStack.peek().booleanValue()) {
				flag = false;
			}
			{

				return new TypeSpecifier(n0);
			}

		}

		final public TypeQualifier TypeQualifier() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			NodeToken n7;
			Token n8;
			NodeToken n9;
			Token n10;
			NodeToken n11;
			Token n12;
			NodeToken n13;
			Token n14;
			NodeToken n15;
			Token n16;
			NodeToken n17;
			Token n18;
			NodeToken n19;
			Token n20;
			NodeToken n21;
			Token n22;
			NodeToken n23;
			Token n24;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case RESTRICT:
				n2 = jj_consume_token(RESTRICT);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case CONST:
				n4 = jj_consume_token(CONST);
				n3 = JTBToolkit.makeNodeToken(n4);
				n0 = new NodeChoice(n3, 1);
				break;
			case VOLATILE:
				n6 = jj_consume_token(VOLATILE);
				n5 = JTBToolkit.makeNodeToken(n6);
				n0 = new NodeChoice(n5, 2);
				break;
			case INLINE:
				n8 = jj_consume_token(INLINE);
				n7 = JTBToolkit.makeNodeToken(n8);
				n0 = new NodeChoice(n7, 3);
				break;
			case CCONST:
				n10 = jj_consume_token(CCONST);
				n9 = JTBToolkit.makeNodeToken(n10);
				n0 = new NodeChoice(n9, 4);
				break;
			case CINLINED:
				n12 = jj_consume_token(CINLINED);
				n11 = JTBToolkit.makeNodeToken(n12);
				n0 = new NodeChoice(n11, 5);
				break;
			case CINLINED2:
				n14 = jj_consume_token(CINLINED2);
				n13 = JTBToolkit.makeNodeToken(n14);
				n0 = new NodeChoice(n13, 6);
				break;
			case CSIGNED:
				n16 = jj_consume_token(CSIGNED);
				n15 = JTBToolkit.makeNodeToken(n16);
				n0 = new NodeChoice(n15, 7);
				break;
			case CSIGNED2:
				n18 = jj_consume_token(CSIGNED2);
				n17 = JTBToolkit.makeNodeToken(n18);
				n0 = new NodeChoice(n17, 8);
				break;
			case CATOMIC:
				n20 = jj_consume_token(CATOMIC);
				n19 = JTBToolkit.makeNodeToken(n20);
				n0 = new NodeChoice(n19, 9);
				break;
			case EXTENSION:
				n22 = jj_consume_token(EXTENSION);
				n21 = JTBToolkit.makeNodeToken(n22);
				n0 = new NodeChoice(n21, 10);
				break;
			case COMPLEX:
				n24 = jj_consume_token(COMPLEX);
				n23 = JTBToolkit.makeNodeToken(n24);
				n0 = new NodeChoice(n23, 11);
				break;
			default:
				jj_la1[7] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new TypeQualifier(n0);
			}

		}

		final public StructOrUnionSpecifier StructOrUnionSpecifier() throws ParseException {
			NodeChoice n0;
			StructOrUnionSpecifierWithList n1;
			StructOrUnionSpecifierWithId n2;
			typedefParsingStack.push(Boolean.FALSE);
			if (jj_2_12(4)) {
				n1 = StructOrUnionSpecifierWithList();
				n0 = new NodeChoice(n1, 0);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case STRUCT:
				case UNION:
					n2 = StructOrUnionSpecifierWithId();
					n0 = new NodeChoice(n2, 1);
					break;
				default:
					jj_la1[8] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			typedefParsingStack.pop();
			{

				return new StructOrUnionSpecifier(n0);
			}

		}

		final public StructOrUnionSpecifierWithList StructOrUnionSpecifierWithList() throws ParseException {
			StructOrUnion n0;
			NodeOptional n1 = new NodeOptional();
			NodeToken n2;
			Token n3;
			NodeToken n4;
			Token n5;
			StructDeclarationList n6;
			NodeToken n7;
			Token n8;
			n0 = StructOrUnion();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case IDENTIFIER:
				n3 = jj_consume_token(IDENTIFIER);
				n2 = JTBToolkit.makeNodeToken(n3);
				n1.addNode(n2);
				break;
			default:
				jj_la1[9] = jj_gen;
				;
			}
			n5 = jj_consume_token(LEFTBRACE);
			n4 = JTBToolkit.makeNodeToken(n5);
			n6 = StructDeclarationList();
			n8 = jj_consume_token(RIGHTBRACE);
			n7 = JTBToolkit.makeNodeToken(n8);
			{

				return new StructOrUnionSpecifierWithList(n0, n1, n4, n6, n7);
			}

		}

		final public StructOrUnionSpecifierWithId StructOrUnionSpecifierWithId() throws ParseException {
			StructOrUnion n0;
			NodeToken n1;
			Token n2;
			n0 = StructOrUnion();
			n2 = jj_consume_token(IDENTIFIER);
			n1 = JTBToolkit.makeNodeToken(n2);
			{

				return new StructOrUnionSpecifierWithId(n0, n1);
			}

		}

		final public StructOrUnion StructOrUnion() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case STRUCT:
				n2 = jj_consume_token(STRUCT);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case UNION:
				n4 = jj_consume_token(UNION);
				n3 = JTBToolkit.makeNodeToken(n4);
				n0 = new NodeChoice(n3, 1);
				break;
			default:
				jj_la1[10] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new StructOrUnion(n0);
			}

		}

		final public StructDeclarationList StructDeclarationList() throws ParseException {
			NodeList n0 = new NodeList();
			StructDeclaration n1;
			label_4: while (true) {
				n1 = StructDeclaration();
				n0.addNode(n1);
				if (jj_2_13(1)) {
					;
				} else {
					break label_4;
				}
			}
			n0.trimToSize();
			{

				return new StructDeclarationList(n0);
			}

		}

		final public InitDeclaratorList InitDeclaratorList() throws ParseException {
			InitDeclarator n0;
			NodeListOptional n1 = new NodeListOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			InitDeclarator n5;
			n0 = InitDeclarator();
			label_5: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case COMMA:
					;
					break;
				default:
					jj_la1[11] = jj_gen;
					break label_5;
				}
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(COMMA);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = InitDeclarator();
				n2.addNode(n5);
				n1.addNode(n2);
			}
			n1.trimToSize();
			if (!(typedefParsingStack.empty()) && typedefParsingStack.peek().booleanValue()) {
				typedefParsingStack.pop();
			}
			{

				return new InitDeclaratorList(n0, n1);
			}

		}

		final public InitDeclarator InitDeclarator() throws ParseException {
			Declarator n0;
			NodeOptional n1 = new NodeOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			Initializer n5;
			n0 = Declarator();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_ASS:
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(OP_ASS);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = Initializer();
				n2.addNode(n5);
				n1.addNode(n2);
				break;
			default:
				jj_la1[12] = jj_gen;
				;
			}
			{

				return new InitDeclarator(n0, n1);
			}

		}

		final public StructDeclaration StructDeclaration() throws ParseException {
			SpecifierQualifierList n0;
			StructDeclaratorList n1;
			NodeToken n2;
			Token n3;
			n0 = SpecifierQualifierList();
			n1 = StructDeclaratorList();
			n3 = jj_consume_token(SEMICOLON);
			n2 = JTBToolkit.makeNodeToken(n3);
			{

				return new StructDeclaration(n0, n1, n2);
			}

		}

		final public SpecifierQualifierList SpecifierQualifierList() throws ParseException {
			NodeList n0 = new NodeList();
			ASpecifierQualifier n1;
			label_6: while (true) {
				n1 = ASpecifierQualifier();
				n0.addNode(n1);
				if (jj_2_14(1)) {
					;
				} else {
					break label_6;
				}
			}
			n0.trimToSize();
			{

				return new SpecifierQualifierList(n0);
			}

		}

		final public ASpecifierQualifier ASpecifierQualifier() throws ParseException {
			NodeChoice n0;
			TypeSpecifier n1;
			TypeQualifier n2;
			if (jj_2_15(1)) {
				n1 = TypeSpecifier();
				n0 = new NodeChoice(n1, 0);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case RESTRICT:
				case VOLATILE:
				case CCONST:
				case INLINE:
				case CINLINED:
				case CINLINED2:
				case CSIGNED:
				case CSIGNED2:
				case CONST:
				case EXTENSION:
				case CATOMIC:
				case COMPLEX:
					n2 = TypeQualifier();
					n0 = new NodeChoice(n2, 1);
					break;
				default:
					jj_la1[13] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new ASpecifierQualifier(n0);
			}

		}

		final public StructDeclaratorList StructDeclaratorList() throws ParseException {
			StructDeclarator n0;
			NodeListOptional n1 = new NodeListOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			StructDeclarator n5;
			n0 = StructDeclarator();
			label_7: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case COMMA:
					;
					break;
				default:
					jj_la1[14] = jj_gen;
					break label_7;
				}
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(COMMA);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = StructDeclarator();
				n2.addNode(n5);
				n1.addNode(n2);
			}
			n1.trimToSize();
			{

				return new StructDeclaratorList(n0, n1);
			}

		}

		final public StructDeclarator StructDeclarator() throws ParseException {
			NodeChoice n0;
			StructDeclaratorWithDeclarator n1;
			StructDeclaratorWithBitField n2;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_MUL:
			case OP_BITXOR:
			case LEFTPAREN:
			case IDENTIFIER:
				n1 = StructDeclaratorWithDeclarator();
				n0 = new NodeChoice(n1, 0);
				break;
			case COLON:
				n2 = StructDeclaratorWithBitField();
				n0 = new NodeChoice(n2, 1);
				break;
			default:
				jj_la1[15] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new StructDeclarator(n0);
			}

		}

		final public StructDeclaratorWithDeclarator StructDeclaratorWithDeclarator() throws ParseException {
			Declarator n0;
			NodeOptional n1 = new NodeOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			ConstantExpression n5;
			n0 = Declarator();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case COLON:
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(COLON);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = ConstantExpression();
				n2.addNode(n5);
				n1.addNode(n2);
				break;
			default:
				jj_la1[16] = jj_gen;
				;
			}
			{

				return new StructDeclaratorWithDeclarator(n0, n1);
			}

		}

		final public StructDeclaratorWithBitField StructDeclaratorWithBitField() throws ParseException {
			NodeToken n0;
			Token n1;
			ConstantExpression n2;
			n1 = jj_consume_token(COLON);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = ConstantExpression();
			{

				return new StructDeclaratorWithBitField(n0, n2);
			}

		}

		final public EnumSpecifier EnumSpecifier() throws ParseException {
			NodeChoice n0;
			EnumSpecifierWithList n1;
			EnumSpecifierWithId n2;
			if (jj_2_16(3)) {
				n1 = EnumSpecifierWithList();
				n0 = new NodeChoice(n1, 0);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case ENUM:
					n2 = EnumSpecifierWithId();
					n0 = new NodeChoice(n2, 1);
					break;
				default:
					jj_la1[17] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new EnumSpecifier(n0);
			}

		}

		final public EnumSpecifierWithList EnumSpecifierWithList() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeOptional n2 = new NodeOptional();
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			EnumeratorList n7;
			NodeToken n8;
			Token n9;
			n1 = jj_consume_token(ENUM);
			n0 = JTBToolkit.makeNodeToken(n1);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case IDENTIFIER:
				n4 = jj_consume_token(IDENTIFIER);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				break;
			default:
				jj_la1[18] = jj_gen;
				;
			}
			n6 = jj_consume_token(LEFTBRACE);
			n5 = JTBToolkit.makeNodeToken(n6);
			n7 = EnumeratorList();
			n9 = jj_consume_token(RIGHTBRACE);
			n8 = JTBToolkit.makeNodeToken(n9);
			{

				return new EnumSpecifierWithList(n0, n2, n5, n7, n8);
			}

		}

		final public EnumSpecifierWithId EnumSpecifierWithId() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			n1 = jj_consume_token(ENUM);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(IDENTIFIER);
			n2 = JTBToolkit.makeNodeToken(n3);
			{

				return new EnumSpecifierWithId(n0, n2);
			}

		}

		final public EnumeratorList EnumeratorList() throws ParseException {
			Enumerator n0;
			NodeListOptional n1 = new NodeListOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			Enumerator n5;
			n0 = Enumerator();
			label_8: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case COMMA:
					;
					break;
				default:
					jj_la1[19] = jj_gen;
					break label_8;
				}
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(COMMA);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = Enumerator();
				n2.addNode(n5);
				n1.addNode(n2);
			}
			n1.trimToSize();
			{

				return new EnumeratorList(n0, n1);
			}

		}

		final public Enumerator Enumerator() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeOptional n2 = new NodeOptional();
			NodeSequence n3;
			NodeToken n4;
			Token n5;
			ConstantExpression n6;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_ASS:
				n3 = new NodeSequence(2);
				n5 = jj_consume_token(OP_ASS);
				n4 = JTBToolkit.makeNodeToken(n5);
				n3.addNode(n4);
				n6 = ConstantExpression();
				n3.addNode(n6);
				n2.addNode(n3);
				break;
			default:
				jj_la1[20] = jj_gen;
				;
			}
			{

				return new Enumerator(n0, n2);
			}

		}

		final public Declarator Declarator() throws ParseException {
			NodeOptional n0 = new NodeOptional();
			Pointer n1;
			DirectDeclarator n2;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_MUL:
			case OP_BITXOR:
				n1 = Pointer();
				n0.addNode(n1);
				break;
			default:
				jj_la1[21] = jj_gen;
				;
			}
			n2 = DirectDeclarator();
			{

				return new Declarator(n0, n2);
			}

		}

		final public DirectDeclarator DirectDeclarator() throws ParseException {
			IdentifierOrDeclarator n0;
			DeclaratorOpList n1;
			n0 = IdentifierOrDeclarator();
			n1 = DeclaratorOpList();
			{

				return new DirectDeclarator(n0, n1);
			}

		}

		final public DeclaratorOpList DeclaratorOpList() throws ParseException {
			NodeListOptional n0 = new NodeListOptional();
			ADeclaratorOp n1;
			typedefParsingStack.push(Boolean.FALSE);
			label_9: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case LEFTPAREN:
				case LEFTBRACKET:
					;
					break;
				default:
					jj_la1[22] = jj_gen;
					break label_9;
				}
				n1 = ADeclaratorOp();
				n0.addNode(n1);
			}
			n0.trimToSize();
			typedefParsingStack.pop();
			{

				return new DeclaratorOpList(n0);
			}

		}

		final public ADeclaratorOp ADeclaratorOp() throws ParseException {
			NodeChoice n0;
			DimensionSize n1;
			ParameterTypeListClosed n2;
			OldParameterListClosed n3;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case LEFTBRACKET:
				n1 = DimensionSize();
				n0 = new NodeChoice(n1, 0);
				break;
			default:
				jj_la1[23] = jj_gen;
				if (jj_2_17(3)) {
					n2 = ParameterTypeListClosed();
					n0 = new NodeChoice(n2, 1);
				} else {
					switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
					case LEFTPAREN:
						n3 = OldParameterListClosed();
						n0 = new NodeChoice(n3, 2);
						break;
					default:
						jj_la1[24] = jj_gen;
						jj_consume_token(-1);
						throw new ParseException();
					}
				}
			}
			{

				return new ADeclaratorOp(n0);
			}

		}

		final public DimensionSize DimensionSize() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeOptional n2 = new NodeOptional();
			ConstantExpression n3;
			NodeToken n4;
			Token n5;
			n1 = jj_consume_token(LEFTBRACKET);
			n0 = JTBToolkit.makeNodeToken(n1);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case INTEGER_LITERAL:
			case FLOATING_POINT_LITERAL:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
			case SIZEOF:
			case OP_INCR:
			case OP_DECR:
			case OP_ADD:
			case OP_SUB:
			case OP_MUL:
			case OP_BITAND:
			case OP_NOT:
			case OP_BITNOT:
			case LEFTPAREN:
			case IDENTIFIER:
				n3 = ConstantExpression();
				n2.addNode(n3);
				break;
			default:
				jj_la1[25] = jj_gen;
				;
			}
			n5 = jj_consume_token(RIGHTBRACKET);
			n4 = JTBToolkit.makeNodeToken(n5);
			{

				return new DimensionSize(n0, n2, n4);
			}

		}

		final public ParameterTypeListClosed ParameterTypeListClosed() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeOptional n2 = new NodeOptional();
			ParameterTypeList n3;
			NodeToken n4;
			Token n5;
			n1 = jj_consume_token(LEFTPAREN);
			n0 = JTBToolkit.makeNodeToken(n1);
			if (jj_2_18(1)) {
				n3 = ParameterTypeList();
				n2.addNode(n3);
			} else {
				;
			}
			n5 = jj_consume_token(RIGHTPAREN);
			n4 = JTBToolkit.makeNodeToken(n5);
			{

				return new ParameterTypeListClosed(n0, n2, n4);
			}

		}

		final public OldParameterListClosed OldParameterListClosed() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeOptional n2 = new NodeOptional();
			OldParameterList n3;
			NodeToken n4;
			Token n5;
			n1 = jj_consume_token(LEFTPAREN);
			n0 = JTBToolkit.makeNodeToken(n1);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case IDENTIFIER:
				n3 = OldParameterList();
				n2.addNode(n3);
				break;
			default:
				jj_la1[26] = jj_gen;
				;
			}
			n5 = jj_consume_token(RIGHTPAREN);
			n4 = JTBToolkit.makeNodeToken(n5);
			{

				return new OldParameterListClosed(n0, n2, n4);
			}

		}

		final public IdentifierOrDeclarator IdentifierOrDeclarator() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeSequence n3;
			NodeToken n4;
			Token n5;
			Declarator n6;
			NodeToken n7;
			Token n8;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case IDENTIFIER:
				n2 = jj_consume_token(IDENTIFIER);
				n1 = JTBToolkit.makeNodeToken(n2);
				if (!(typedefParsingStack.empty()) && typedefParsingStack.peek().booleanValue()) {
					addType(n2.image);
				}
				n0 = new NodeChoice(n1, 0);
				break;
			case LEFTPAREN:
				n3 = new NodeSequence(3);
				n5 = jj_consume_token(LEFTPAREN);
				n4 = JTBToolkit.makeNodeToken(n5);
				n3.addNode(n4);
				n6 = Declarator();
				n3.addNode(n6);
				n8 = jj_consume_token(RIGHTPAREN);
				n7 = JTBToolkit.makeNodeToken(n8);
				n3.addNode(n7);
				n0 = new NodeChoice(n3, 1);
				break;
			default:
				jj_la1[27] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new IdentifierOrDeclarator(n0);
			}

		}

		final public Pointer Pointer() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			NodeOptional n5 = new NodeOptional();
			TypeQualifierList n6;
			NodeOptional n7 = new NodeOptional();
			Pointer n8;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_MUL:
				n2 = jj_consume_token(OP_MUL);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case OP_BITXOR:
				n4 = jj_consume_token(OP_BITXOR);
				n3 = JTBToolkit.makeNodeToken(n4);
				n0 = new NodeChoice(n3, 1);
				break;
			default:
				jj_la1[28] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case RESTRICT:
			case VOLATILE:
			case CCONST:
			case INLINE:
			case CINLINED:
			case CINLINED2:
			case CSIGNED:
			case CSIGNED2:
			case CONST:
			case EXTENSION:
			case CATOMIC:
			case COMPLEX:
				n6 = TypeQualifierList();
				n5.addNode(n6);
				break;
			default:
				jj_la1[29] = jj_gen;
				;
			}
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_MUL:
			case OP_BITXOR:
				n8 = Pointer();
				n7.addNode(n8);
				break;
			default:
				jj_la1[30] = jj_gen;
				;
			}
			{

				return new Pointer(n0, n5, n7);
			}

		}

		final public TypeQualifierList TypeQualifierList() throws ParseException {
			NodeList n0 = new NodeList();
			TypeQualifier n1;
			label_10: while (true) {
				n1 = TypeQualifier();
				n0.addNode(n1);
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case RESTRICT:
				case VOLATILE:
				case CCONST:
				case INLINE:
				case CINLINED:
				case CINLINED2:
				case CSIGNED:
				case CSIGNED2:
				case CONST:
				case EXTENSION:
				case CATOMIC:
				case COMPLEX:
					;
					break;
				default:
					jj_la1[31] = jj_gen;
					break label_10;
				}
			}
			n0.trimToSize();
			{

				return new TypeQualifierList(n0);
			}

		}

		final public ParameterTypeList ParameterTypeList() throws ParseException {
			ParameterList n0;
			NodeOptional n1 = new NodeOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			n0 = ParameterList();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case COMMA:
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(COMMA);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n6 = jj_consume_token(ELLIPSIS);
				n5 = JTBToolkit.makeNodeToken(n6);
				n2.addNode(n5);
				n1.addNode(n2);
				break;
			default:
				jj_la1[32] = jj_gen;
				;
			}
			{

				return new ParameterTypeList(n0, n1);
			}

		}

		final public ParameterList ParameterList() throws ParseException {
			ParameterDeclaration n0;
			NodeListOptional n1 = new NodeListOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			ParameterDeclaration n5;
			n0 = ParameterDeclaration();
			label_11: while (true) {
				if (jj_2_19(2)) {
					;
				} else {
					break label_11;
				}
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(COMMA);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = ParameterDeclaration();
				n2.addNode(n5);
				n1.addNode(n2);
			}
			n1.trimToSize();
			{

				return new ParameterList(n0, n1);
			}

		}

		final public ParameterDeclaration ParameterDeclaration() throws ParseException {
			DeclarationSpecifiers n0;
			ParameterAbstraction n1;
			n0 = DeclarationSpecifiers();
			n1 = ParameterAbstraction();
			{

				return new ParameterDeclaration(n0, n1);
			}

		}

		final public ParameterAbstraction ParameterAbstraction() throws ParseException {
			NodeChoice n0;
			Declarator n1;
			AbstractOptionalDeclarator n2;
			if (jj_2_20(2147483647)) {
				n1 = Declarator();
				n0 = new NodeChoice(n1, 0);
			} else {
				n2 = AbstractOptionalDeclarator();
				n0 = new NodeChoice(n2, 1);
			}
			{

				return new ParameterAbstraction(n0);
			}

		}

		final public AbstractOptionalDeclarator AbstractOptionalDeclarator() throws ParseException {
			NodeOptional n0 = new NodeOptional();
			AbstractDeclarator n1;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_MUL:
			case OP_BITXOR:
			case LEFTPAREN:
			case LEFTBRACKET:
				n1 = AbstractDeclarator();
				n0.addNode(n1);
				break;
			default:
				jj_la1[33] = jj_gen;
				;
			}
			{

				return new AbstractOptionalDeclarator(n0);
			}

		}

		final public OldParameterList OldParameterList() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeListOptional n2 = new NodeListOptional();
			NodeSequence n3;
			NodeToken n4;
			Token n5;
			NodeToken n6;
			Token n7;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			label_12: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case COMMA:
					;
					break;
				default:
					jj_la1[34] = jj_gen;
					break label_12;
				}
				n3 = new NodeSequence(2);
				n5 = jj_consume_token(COMMA);
				n4 = JTBToolkit.makeNodeToken(n5);
				n3.addNode(n4);
				n7 = jj_consume_token(IDENTIFIER);
				n6 = JTBToolkit.makeNodeToken(n7);
				n3.addNode(n6);
				n2.addNode(n3);
			}
			n2.trimToSize();
			{

				return new OldParameterList(n0, n2);
			}

		}

		final public Initializer Initializer() throws ParseException {
			NodeChoice n0;
			AssignmentExpression n1;
			ArrayInitializer n2;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case INTEGER_LITERAL:
			case FLOATING_POINT_LITERAL:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
			case SIZEOF:
			case OP_INCR:
			case OP_DECR:
			case OP_ADD:
			case OP_SUB:
			case OP_MUL:
			case OP_BITAND:
			case OP_NOT:
			case OP_BITNOT:
			case LEFTPAREN:
			case IDENTIFIER:
				n1 = AssignmentExpression();
				n0 = new NodeChoice(n1, 0);
				break;
			case LEFTBRACE:
				n2 = ArrayInitializer();
				n0 = new NodeChoice(n2, 1);
				break;
			default:
				jj_la1[35] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new Initializer(n0);
			}

		}

		final public ArrayInitializer ArrayInitializer() throws ParseException {
			NodeToken n0;
			Token n1;
			InitializerList n2;
			NodeOptional n3 = new NodeOptional();
			NodeToken n4;
			Token n5;
			NodeToken n6;
			Token n7;
			n1 = jj_consume_token(LEFTBRACE);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = InitializerList();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case COMMA:
				n5 = jj_consume_token(COMMA);
				n4 = JTBToolkit.makeNodeToken(n5);
				n3.addNode(n4);
				break;
			default:
				jj_la1[36] = jj_gen;
				;
			}
			n7 = jj_consume_token(RIGHTBRACE);
			n6 = JTBToolkit.makeNodeToken(n7);
			{

				return new ArrayInitializer(n0, n2, n3, n6);
			}

		}

		final public InitializerList InitializerList() throws ParseException {
			Initializer n0;
			NodeListOptional n1 = new NodeListOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			Initializer n5;
			n0 = Initializer();
			label_13: while (true) {
				if (jj_2_21(2)) {
					;
				} else {
					break label_13;
				}
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(COMMA);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = Initializer();
				n2.addNode(n5);
				n1.addNode(n2);
			}
			n1.trimToSize();
			{

				return new InitializerList(n0, n1);
			}

		}

		final public TypeName TypeName() throws ParseException {
			SpecifierQualifierList n0;
			NodeOptional n1 = new NodeOptional();
			AbstractDeclarator n2;
			n0 = SpecifierQualifierList();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_MUL:
			case OP_BITXOR:
			case LEFTPAREN:
			case LEFTBRACKET:
				n2 = AbstractDeclarator();
				n1.addNode(n2);
				break;
			default:
				jj_la1[37] = jj_gen;
				;
			}
			{

				return new TypeName(n0, n1);
			}

		}

		final public AbstractDeclarator AbstractDeclarator() throws ParseException {
			NodeChoice n0;
			AbstractDeclaratorWithPointer n1;
			DirectAbstractDeclarator n2;
			if (jj_2_22(3)) {
				n1 = AbstractDeclaratorWithPointer();
				n0 = new NodeChoice(n1, 0);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case LEFTPAREN:
				case LEFTBRACKET:
					n2 = DirectAbstractDeclarator();
					n0 = new NodeChoice(n2, 1);
					break;
				default:
					jj_la1[38] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new AbstractDeclarator(n0);
			}

		}

		final public AbstractDeclaratorWithPointer AbstractDeclaratorWithPointer() throws ParseException {
			Pointer n0;
			NodeOptional n1 = new NodeOptional();
			DirectAbstractDeclarator n2;
			n0 = Pointer();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case LEFTPAREN:
			case LEFTBRACKET:
				n2 = DirectAbstractDeclarator();
				n1.addNode(n2);
				break;
			default:
				jj_la1[39] = jj_gen;
				;
			}
			{

				return new AbstractDeclaratorWithPointer(n0, n1);
			}

		}

		final public DirectAbstractDeclarator DirectAbstractDeclarator() throws ParseException {
			AbstractDimensionOrParameter n0;
			DimensionOrParameterList n1;
			n0 = AbstractDimensionOrParameter();
			n1 = DimensionOrParameterList();
			{

				return new DirectAbstractDeclarator(n0, n1);
			}

		}

		final public AbstractDimensionOrParameter AbstractDimensionOrParameter() throws ParseException {
			NodeChoice n0;
			AbstractDeclaratorClosed n1;
			DimensionSize n2;
			ParameterTypeListClosed n3;
			if (jj_2_23(3)) {
				n1 = AbstractDeclaratorClosed();
				n0 = new NodeChoice(n1, 0);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case LEFTBRACKET:
					n2 = DimensionSize();
					n0 = new NodeChoice(n2, 1);
					break;
				case LEFTPAREN:
					n3 = ParameterTypeListClosed();
					n0 = new NodeChoice(n3, 2);
					break;
				default:
					jj_la1[40] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new AbstractDimensionOrParameter(n0);
			}

		}

		final public AbstractDeclaratorClosed AbstractDeclaratorClosed() throws ParseException {
			NodeToken n0;
			Token n1;
			AbstractDeclarator n2;
			NodeToken n3;
			Token n4;
			n1 = jj_consume_token(LEFTPAREN);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = AbstractDeclarator();
			n4 = jj_consume_token(RIGHTPAREN);
			n3 = JTBToolkit.makeNodeToken(n4);
			{

				return new AbstractDeclaratorClosed(n0, n2, n3);
			}

		}

		final public DimensionOrParameterList DimensionOrParameterList() throws ParseException {
			NodeListOptional n0 = new NodeListOptional();
			ADimensionOrParameter n1;
			label_14: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case LEFTPAREN:
				case LEFTBRACKET:
					;
					break;
				default:
					jj_la1[41] = jj_gen;
					break label_14;
				}
				n1 = ADimensionOrParameter();
				n0.addNode(n1);
			}
			n0.trimToSize();
			{

				return new DimensionOrParameterList(n0);
			}

		}

		final public ADimensionOrParameter ADimensionOrParameter() throws ParseException {
			NodeChoice n0;
			DimensionSize n1;
			ParameterTypeListClosed n2;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case LEFTBRACKET:
				n1 = DimensionSize();
				n0 = new NodeChoice(n1, 0);
				break;
			case LEFTPAREN:
				n2 = ParameterTypeListClosed();
				n0 = new NodeChoice(n2, 1);
				break;
			default:
				jj_la1[42] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new ADimensionOrParameter(n0);
			}

		}

		final public TypedefName TypedefName() throws ParseException {
			NodeToken n0;
			Token n1;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			{

				return new TypedefName(n0);
			}

		}

		final public Statement Statement() throws ParseException {
			NodeChoice n0;
			LabeledStatement n1;
			Statement n2;
			CompoundStatement n3;
			SelectionStatement n4;
			IterationStatement n5;
			JumpStatement n6;
			UnknownPragma n7;
			OmpConstruct n8;
			OmpDirective n9;
			UnknownCpp n10;
			if (jj_2_24(2)) {
				n1 = LabeledStatement();
				n0 = new NodeChoice(n1, 0);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case INTEGER_LITERAL:
				case FLOATING_POINT_LITERAL:
				case CHARACTER_LITERAL:
				case STRING_LITERAL:
				case SIZEOF:
				case OP_INCR:
				case OP_DECR:
				case OP_ADD:
				case OP_SUB:
				case OP_MUL:
				case OP_BITAND:
				case OP_NOT:
				case OP_BITNOT:
				case SEMICOLON:
				case LEFTPAREN:
				case IDENTIFIER:
					n2 = ExpressionStatement();
					n0 = new NodeChoice(n2, 1);
					break;
				case LEFTBRACE:
					n3 = CompoundStatement();
					n0 = new NodeChoice(n3, 2);
					break;
				case SWITCH:
				case IF:
					n4 = SelectionStatement();
					n0 = new NodeChoice(n4, 3);
					break;
				case WHILE:
				case FOR:
				case DO:
					n5 = IterationStatement();
					n0 = new NodeChoice(n5, 4);
					break;
				case CONTINUE:
				case RETURN:
				case BREAK:
				case GOTO:
					n6 = JumpStatement();
					n0 = new NodeChoice(n6, 5);
					break;
				default:
					jj_la1[43] = jj_gen;
					if (jj_2_25(4)) {
						n7 = UnknownPragma();
						n0 = new NodeChoice(n7, 6);
					} else if (jj_2_26(4)) {
						n8 = OmpConstruct();
						n0 = new NodeChoice(n8, 7);
					} else if (jj_2_27(2)) {
						n9 = OmpDirective();
						n0 = new NodeChoice(n9, 8);
					} else {
						switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
						case CROSSBAR:
							n10 = UnknownCpp();
							n0 = new NodeChoice(n10, 9);
							break;
						default:
							jj_la1[44] = jj_gen;
							jj_consume_token(-1);
							throw new ParseException();
						}
					}
				}
			}
			{

				return new Statement(n0);
			}

		}

		final public UnknownCpp UnknownCpp() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			n1 = jj_consume_token(CROSSBAR);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(UNKNOWN_CPP);
			n2 = JTBToolkit.makeNodeToken(n3);
			{

				return new UnknownCpp(n0, n2);
			}

		}

		final public OmpEol OmpEol() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OMP_CR:
				n2 = jj_consume_token(OMP_CR);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case OMP_NL:
				n4 = jj_consume_token(OMP_NL);
				n3 = JTBToolkit.makeNodeToken(n4);
				n0 = new NodeChoice(n3, 1);
				break;
			default:
				jj_la1[45] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new OmpEol(n0);
			}

		}

		final public OmpConstruct OmpConstruct() throws ParseException {
			NodeChoice n0;
			ParallelConstruct n1;
			ForConstruct n2;
			SectionsConstruct n3;
			SingleConstruct n4;
			ParallelForConstruct n5;
			ParallelSectionsConstruct n6;
			TaskConstruct n7;
			MasterConstruct n8;
			CriticalConstruct n9;
			AtomicConstruct n10;
			OrderedConstruct n11;
			if (jj_2_28(6)) {
				n1 = ParallelConstruct();
				n0 = new NodeChoice(n1, 0);
			} else if (jj_2_29(4)) {
				n2 = ForConstruct();
				n0 = new NodeChoice(n2, 1);
			} else if (jj_2_30(4)) {
				n3 = SectionsConstruct();
				n0 = new NodeChoice(n3, 2);
			} else if (jj_2_31(4)) {
				n4 = SingleConstruct();
				n0 = new NodeChoice(n4, 3);
			} else if (jj_2_32(6)) {
				n5 = ParallelForConstruct();
				n0 = new NodeChoice(n5, 4);
			} else if (jj_2_33(6)) {
				n6 = ParallelSectionsConstruct();
				n0 = new NodeChoice(n6, 5);
			} else if (jj_2_34(4)) {
				n7 = TaskConstruct();
				n0 = new NodeChoice(n7, 6);
			} else if (jj_2_35(4)) {
				n8 = MasterConstruct();
				n0 = new NodeChoice(n8, 7);
			} else if (jj_2_36(4)) {
				n9 = CriticalConstruct();
				n0 = new NodeChoice(n9, 8);
			} else if (jj_2_37(4)) {
				n10 = AtomicConstruct();
				n0 = new NodeChoice(n10, 9);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case CROSSBAR:
					n11 = OrderedConstruct();
					n0 = new NodeChoice(n11, 10);
					break;
				default:
					jj_la1[46] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new OmpConstruct(n0);
			}

		}

		final public OmpDirective OmpDirective() throws ParseException {
			NodeChoice n0;
			BarrierDirective n1;
			TaskwaitDirective n2;
			TaskyieldDirective n3;
			FlushDirective n4;
			if (jj_2_38(4)) {
				n1 = BarrierDirective();
				n0 = new NodeChoice(n1, 0);
			} else if (jj_2_39(4)) {
				n2 = TaskwaitDirective();
				n0 = new NodeChoice(n2, 1);
			} else if (jj_2_40(4)) {
				n3 = TaskyieldDirective();
				n0 = new NodeChoice(n3, 2);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case CROSSBAR:
					n4 = FlushDirective();
					n0 = new NodeChoice(n4, 3);
					break;
				default:
					jj_la1[47] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new OmpDirective(n0);
			}

		}

		final public ParallelConstruct ParallelConstruct() throws ParseException {
			OmpPragma n0;
			ParallelDirective n1;
			Statement n2;
			n0 = OmpPragma();
			n1 = ParallelDirective();
			n2 = Statement();
			{

				return new ParallelConstruct(n0, n1, n2);
			}

		}

		final public OmpPragma OmpPragma() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			NodeToken n4;
			Token n5;
			n1 = jj_consume_token(CROSSBAR);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(PRAGMA);
			n2 = JTBToolkit.makeNodeToken(n3);
			n5 = jj_consume_token(OMP);
			n4 = JTBToolkit.makeNodeToken(n5);
			{

				return new OmpPragma(n0, n2, n4);
			}

		}

		final public UnknownPragma UnknownPragma() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			NodeToken n4;
			Token n5;
			n1 = jj_consume_token(CROSSBAR);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(PRAGMA);
			n2 = JTBToolkit.makeNodeToken(n3);
			n5 = jj_consume_token(UNKNOWN_CPP);
			n4 = JTBToolkit.makeNodeToken(n5);
			{

				return new UnknownPragma(n0, n2, n4);
			}

		}

		final public ParallelDirective ParallelDirective() throws ParseException {
			NodeToken n0;
			Token n1;
			UniqueParallelOrDataClauseList n2;
			OmpEol n3;
			n1 = jj_consume_token(PARALLEL);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = UniqueParallelOrDataClauseList();
			n3 = OmpEol();
			{

				return new ParallelDirective(n0, n2, n3);
			}

		}

		final public UniqueParallelOrDataClauseList UniqueParallelOrDataClauseList() throws ParseException {
			NodeListOptional n0 = new NodeListOptional();
			AUniqueParallelOrDataClause n1;
			label_15: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case DFLT:
				case IF:
				case REDUCTION:
				case PRIVATE:
				case FIRSTPRIVATE:
				case LASTPRIVATE:
				case SHARED:
				case COPYIN:
				case NUM_THREADS:
					;
					break;
				default:
					jj_la1[48] = jj_gen;
					break label_15;
				}
				n1 = AUniqueParallelOrDataClause();
				n0.addNode(n1);
			}
			n0.trimToSize();
			{

				return new UniqueParallelOrDataClauseList(n0);
			}

		}

		final public AUniqueParallelOrDataClause AUniqueParallelOrDataClause() throws ParseException {
			NodeChoice n0;
			UniqueParallelClause n1;
			DataClause n2;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case IF:
			case NUM_THREADS:
				n1 = UniqueParallelClause();
				n0 = new NodeChoice(n1, 0);
				break;
			case DFLT:
			case REDUCTION:
			case PRIVATE:
			case FIRSTPRIVATE:
			case LASTPRIVATE:
			case SHARED:
			case COPYIN:
				n2 = DataClause();
				n0 = new NodeChoice(n2, 1);
				break;
			default:
				jj_la1[49] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new AUniqueParallelOrDataClause(n0);
			}

		}

		final public UniqueParallelClause UniqueParallelClause() throws ParseException {
			NodeChoice n0;
			IfClause n1;
			NumThreadsClause n2;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case IF:
				n1 = IfClause();
				n0 = new NodeChoice(n1, 0);
				break;
			case NUM_THREADS:
				n2 = NumThreadsClause();
				n0 = new NodeChoice(n2, 1);
				break;
			default:
				jj_la1[50] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new UniqueParallelClause(n0);
			}

		}

		final public IfClause IfClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			NodeToken n5;
			Token n6;
			n1 = jj_consume_token(IF);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			{

				return new IfClause(n0, n2, n4, n5);
			}

		}

		final public NumThreadsClause NumThreadsClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			NodeToken n5;
			Token n6;
			n1 = jj_consume_token(NUM_THREADS);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			{

				return new NumThreadsClause(n0, n2, n4, n5);
			}

		}

		final public DataClause DataClause() throws ParseException {
			NodeChoice n0;
			OmpPrivateClause n1;
			OmpFirstPrivateClause n2;
			OmpLastPrivateClause n3;
			OmpSharedClause n4;
			OmpCopyinClause n5;
			OmpDfltSharedClause n6;
			OmpDfltNoneClause n7;
			OmpReductionClause n8;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case PRIVATE:
				n1 = OmpPrivateClause();
				n0 = new NodeChoice(n1, 0);
				break;
			case FIRSTPRIVATE:
				n2 = OmpFirstPrivateClause();
				n0 = new NodeChoice(n2, 1);
				break;
			case LASTPRIVATE:
				n3 = OmpLastPrivateClause();
				n0 = new NodeChoice(n3, 2);
				break;
			case SHARED:
				n4 = OmpSharedClause();
				n0 = new NodeChoice(n4, 3);
				break;
			case COPYIN:
				n5 = OmpCopyinClause();
				n0 = new NodeChoice(n5, 4);
				break;
			default:
				jj_la1[51] = jj_gen;
				if (jj_2_41(2147483647)) {
					n6 = OmpDfltSharedClause();
					n0 = new NodeChoice(n6, 5);
				} else {
					switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
					case DFLT:
						n7 = OmpDfltNoneClause();
						n0 = new NodeChoice(n7, 6);
						break;
					case REDUCTION:
						n8 = OmpReductionClause();
						n0 = new NodeChoice(n8, 7);
						break;
					default:
						jj_la1[52] = jj_gen;
						jj_consume_token(-1);
						throw new ParseException();
					}
				}
			}
			{

				return new DataClause(n0);
			}

		}

		final public OmpPrivateClause OmpPrivateClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			VariableList n4;
			NodeToken n5;
			Token n6;
			n1 = jj_consume_token(PRIVATE);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = VariableList();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			{

				return new OmpPrivateClause(n0, n2, n4, n5);
			}

		}

		final public OmpFirstPrivateClause OmpFirstPrivateClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			VariableList n4;
			NodeToken n5;
			Token n6;
			n1 = jj_consume_token(FIRSTPRIVATE);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = VariableList();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			{

				return new OmpFirstPrivateClause(n0, n2, n4, n5);
			}

		}

		final public OmpLastPrivateClause OmpLastPrivateClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			VariableList n4;
			NodeToken n5;
			Token n6;
			n1 = jj_consume_token(LASTPRIVATE);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = VariableList();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			{

				return new OmpLastPrivateClause(n0, n2, n4, n5);
			}

		}

		final public OmpSharedClause OmpSharedClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			VariableList n4;
			NodeToken n5;
			Token n6;
			n1 = jj_consume_token(SHARED);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = VariableList();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			{

				return new OmpSharedClause(n0, n2, n4, n5);
			}

		}

		final public OmpCopyinClause OmpCopyinClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			VariableList n4;
			NodeToken n5;
			Token n6;
			n1 = jj_consume_token(COPYIN);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = VariableList();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			{

				return new OmpCopyinClause(n0, n2, n4, n5);
			}

		}

		final public OmpDfltSharedClause OmpDfltSharedClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			NodeToken n4;
			Token n5;
			NodeToken n6;
			Token n7;
			n1 = jj_consume_token(DFLT);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n5 = jj_consume_token(SHARED);
			n4 = JTBToolkit.makeNodeToken(n5);
			n7 = jj_consume_token(RIGHTPAREN);
			n6 = JTBToolkit.makeNodeToken(n7);
			{

				return new OmpDfltSharedClause(n0, n2, n4, n6);
			}

		}

		final public OmpDfltNoneClause OmpDfltNoneClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			NodeToken n4;
			Token n5;
			NodeToken n6;
			Token n7;
			n1 = jj_consume_token(DFLT);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n5 = jj_consume_token(NONE);
			n4 = JTBToolkit.makeNodeToken(n5);
			n7 = jj_consume_token(RIGHTPAREN);
			n6 = JTBToolkit.makeNodeToken(n7);
			{

				return new OmpDfltNoneClause(n0, n2, n4, n6);
			}

		}

		final public OmpReductionClause OmpReductionClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			ReductionOp n4;
			NodeToken n5;
			Token n6;
			VariableList n7;
			NodeToken n8;
			Token n9;
			n1 = jj_consume_token(REDUCTION);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = ReductionOp();
			n6 = jj_consume_token(COLON);
			n5 = JTBToolkit.makeNodeToken(n6);
			n7 = VariableList();
			n9 = jj_consume_token(RIGHTPAREN);
			n8 = JTBToolkit.makeNodeToken(n9);
			{

				return new OmpReductionClause(n0, n2, n4, n5, n7, n8);
			}

		}

		final public ForConstruct ForConstruct() throws ParseException {
			OmpPragma n0;
			ForDirective n1;
			OmpForHeader n2;
			Statement n3;
			n0 = OmpPragma();
			n1 = ForDirective();
			n2 = OmpForHeader();
			n3 = Statement();
			{

				return new ForConstruct(n0, n1, n2, n3);
			}

		}

		final public ForDirective ForDirective() throws ParseException {
			NodeToken n0;
			Token n1;
			UniqueForOrDataOrNowaitClauseList n2;
			OmpEol n3;
			n1 = jj_consume_token(FOR);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = UniqueForOrDataOrNowaitClauseList();
			n3 = OmpEol();
			{

				return new ForDirective(n0, n2, n3);
			}

		}

		final public UniqueForOrDataOrNowaitClauseList UniqueForOrDataOrNowaitClauseList() throws ParseException {
			NodeListOptional n0 = new NodeListOptional();
			AUniqueForOrDataOrNowaitClause n1;
			label_16: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case DFLT:
				case ORDERED:
				case NOWAIT:
				case SCHEDULE:
				case REDUCTION:
				case PRIVATE:
				case FIRSTPRIVATE:
				case LASTPRIVATE:
				case SHARED:
				case COPYIN:
				case COLLAPSE:
					;
					break;
				default:
					jj_la1[53] = jj_gen;
					break label_16;
				}
				n1 = AUniqueForOrDataOrNowaitClause();
				n0.addNode(n1);
			}
			n0.trimToSize();
			{

				return new UniqueForOrDataOrNowaitClauseList(n0);
			}

		}

		final public AUniqueForOrDataOrNowaitClause AUniqueForOrDataOrNowaitClause() throws ParseException {
			NodeChoice n0;
			UniqueForClause n1;
			DataClause n2;
			NowaitClause n3;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case ORDERED:
			case SCHEDULE:
			case COLLAPSE:
				n1 = UniqueForClause();
				n0 = new NodeChoice(n1, 0);
				break;
			case DFLT:
			case REDUCTION:
			case PRIVATE:
			case FIRSTPRIVATE:
			case LASTPRIVATE:
			case SHARED:
			case COPYIN:
				n2 = DataClause();
				n0 = new NodeChoice(n2, 1);
				break;
			case NOWAIT:
				n3 = NowaitClause();
				n0 = new NodeChoice(n3, 2);
				break;
			default:
				jj_la1[54] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new AUniqueForOrDataOrNowaitClause(n0);
			}

		}

		final public NowaitClause NowaitClause() throws ParseException {
			NodeToken n0;
			Token n1;
			n1 = jj_consume_token(NOWAIT);
			n0 = JTBToolkit.makeNodeToken(n1);
			{

				return new NowaitClause(n0);
			}

		}

		final public UniqueForClause UniqueForClause() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			UniqueForClauseSchedule n3;
			UniqueForCollapse n4;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case ORDERED:
				n2 = jj_consume_token(ORDERED);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case SCHEDULE:
				n3 = UniqueForClauseSchedule();
				n0 = new NodeChoice(n3, 1);
				break;
			case COLLAPSE:
				n4 = UniqueForCollapse();
				n0 = new NodeChoice(n4, 2);
				break;
			default:
				jj_la1[55] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new UniqueForClause(n0);
			}

		}

		final public UniqueForCollapse UniqueForCollapse() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			NodeToken n5;
			Token n6;
			n1 = jj_consume_token(COLLAPSE);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			{

				return new UniqueForCollapse(n0, n2, n4, n5);
			}

		}

		final public UniqueForClauseSchedule UniqueForClauseSchedule() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			ScheduleKind n4;
			NodeOptional n5 = new NodeOptional();
			NodeSequence n6;
			NodeToken n7;
			Token n8;
			Expression n9;
			NodeToken n10;
			Token n11;
			n1 = jj_consume_token(SCHEDULE);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = ScheduleKind();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case COMMA:
				n6 = new NodeSequence(2);
				n8 = jj_consume_token(COMMA);
				n7 = JTBToolkit.makeNodeToken(n8);
				n6.addNode(n7);
				n9 = Expression();
				n6.addNode(n9);
				n5.addNode(n6);
				break;
			default:
				jj_la1[56] = jj_gen;
				;
			}
			n11 = jj_consume_token(RIGHTPAREN);
			n10 = JTBToolkit.makeNodeToken(n11);
			{

				return new UniqueForClauseSchedule(n0, n2, n4, n5, n10);
			}

		}

		final public ScheduleKind ScheduleKind() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			NodeToken n7;
			Token n8;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case STATIC:
				n2 = jj_consume_token(STATIC);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case DYNAMIC:
				n4 = jj_consume_token(DYNAMIC);
				n3 = JTBToolkit.makeNodeToken(n4);
				n0 = new NodeChoice(n3, 1);
				break;
			case GUIDED:
				n6 = jj_consume_token(GUIDED);
				n5 = JTBToolkit.makeNodeToken(n6);
				n0 = new NodeChoice(n5, 2);
				break;
			case RUNTIME:
				n8 = jj_consume_token(RUNTIME);
				n7 = JTBToolkit.makeNodeToken(n8);
				n0 = new NodeChoice(n7, 3);
				break;
			default:
				jj_la1[57] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new ScheduleKind(n0);
			}

		}

		final public OmpForHeader OmpForHeader() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			OmpForInitExpression n4;
			NodeToken n5;
			Token n6;
			OmpForCondition n7;
			NodeToken n8;
			Token n9;
			OmpForReinitExpression n10;
			NodeToken n11;
			Token n12;
			n1 = jj_consume_token(FOR);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = OmpForInitExpression();
			n6 = jj_consume_token(SEMICOLON);
			n5 = JTBToolkit.makeNodeToken(n6);
			n7 = OmpForCondition();
			n9 = jj_consume_token(SEMICOLON);
			n8 = JTBToolkit.makeNodeToken(n9);
			n10 = OmpForReinitExpression();
			n12 = jj_consume_token(RIGHTPAREN);
			n11 = JTBToolkit.makeNodeToken(n12);
			{

				return new OmpForHeader(n0, n2, n4, n5, n7, n8, n10, n11);
			}

		}

		final public OmpForInitExpression OmpForInitExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(OP_ASS);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			{

				return new OmpForInitExpression(n0, n2, n4);
			}

		}

		final public OmpForCondition OmpForCondition() throws ParseException {
			NodeChoice n0;
			OmpForLTCondition n1;
			OmpForLECondition n2;
			OmpForGTCondition n3;
			OmpForGECondition n4;
			if (jj_2_42(2)) {
				n1 = OmpForLTCondition();
				n0 = new NodeChoice(n1, 0);
			} else if (jj_2_43(2)) {
				n2 = OmpForLECondition();
				n0 = new NodeChoice(n2, 1);
			} else if (jj_2_44(2)) {
				n3 = OmpForGTCondition();
				n0 = new NodeChoice(n3, 2);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case IDENTIFIER:
					n4 = OmpForGECondition();
					n0 = new NodeChoice(n4, 3);
					break;
				default:
					jj_la1[58] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new OmpForCondition(n0);
			}

		}

		final public OmpForLTCondition OmpForLTCondition() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(OP_LT);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			{

				return new OmpForLTCondition(n0, n2, n4);
			}

		}

		final public OmpForLECondition OmpForLECondition() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(OP_LE);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			{

				return new OmpForLECondition(n0, n2, n4);
			}

		}

		final public OmpForGTCondition OmpForGTCondition() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(OP_GT);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			{

				return new OmpForGTCondition(n0, n2, n4);
			}

		}

		final public OmpForGECondition OmpForGECondition() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(OP_GE);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			{

				return new OmpForGECondition(n0, n2, n4);
			}

		}

		final public OmpForReinitExpression OmpForReinitExpression() throws ParseException {
			NodeChoice n0;
			PostIncrementId n1;
			PostDecrementId n2;
			PreIncrementId n3;
			PreDecrementId n4;
			ShortAssignPlus n5;
			ShortAssignMinus n6;
			OmpForAdditive n7;
			OmpForSubtractive n8;
			OmpForMultiplicative n9;
			if (jj_2_45(2147483647)) {
				n1 = PostIncrementId();
				n0 = new NodeChoice(n1, 0);
			} else if (jj_2_46(2147483647)) {
				n2 = PostDecrementId();
				n0 = new NodeChoice(n2, 1);
			} else if (jj_2_47(2147483647)) {
				n3 = PreIncrementId();
				n0 = new NodeChoice(n3, 2);
			} else if (jj_2_48(2147483647)) {
				n4 = PreDecrementId();
				n0 = new NodeChoice(n4, 3);
			} else if (jj_2_49(2)) {
				n5 = ShortAssignPlus();
				n0 = new NodeChoice(n5, 4);
			} else if (jj_2_50(2)) {
				n6 = ShortAssignMinus();
				n0 = new NodeChoice(n6, 5);
			} else if (jj_2_51(4)) {
				n7 = OmpForAdditive();
				n0 = new NodeChoice(n7, 6);
			} else if (jj_2_52(4)) {
				n8 = OmpForSubtractive();
				n0 = new NodeChoice(n8, 7);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case IDENTIFIER:
					n9 = OmpForMultiplicative();
					n0 = new NodeChoice(n9, 8);
					break;
				default:
					jj_la1[59] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new OmpForReinitExpression(n0);
			}

		}

		final public PostIncrementId PostIncrementId() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(OP_INCR);
			n2 = JTBToolkit.makeNodeToken(n3);
			{

				return new PostIncrementId(n0, n2);
			}

		}

		final public PostDecrementId PostDecrementId() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(OP_DECR);
			n2 = JTBToolkit.makeNodeToken(n3);
			{

				return new PostDecrementId(n0, n2);
			}

		}

		final public PreIncrementId PreIncrementId() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			n1 = jj_consume_token(OP_INCR);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(IDENTIFIER);
			n2 = JTBToolkit.makeNodeToken(n3);
			{

				return new PreIncrementId(n0, n2);
			}

		}

		final public PreDecrementId PreDecrementId() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			n1 = jj_consume_token(OP_DECR);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(IDENTIFIER);
			n2 = JTBToolkit.makeNodeToken(n3);
			{

				return new PreDecrementId(n0, n2);
			}

		}

		final public ShortAssignPlus ShortAssignPlus() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(OP_ADDASS);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			{

				return new ShortAssignPlus(n0, n2, n4);
			}

		}

		final public ShortAssignMinus ShortAssignMinus() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(OP_SUBASS);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			{

				return new ShortAssignMinus(n0, n2, n4);
			}

		}

		final public OmpForAdditive OmpForAdditive() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			NodeToken n4;
			Token n5;
			NodeToken n6;
			Token n7;
			AdditiveExpression n8;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(OP_ASS);
			n2 = JTBToolkit.makeNodeToken(n3);
			n5 = jj_consume_token(IDENTIFIER);
			n4 = JTBToolkit.makeNodeToken(n5);
			n7 = jj_consume_token(OP_ADD);
			n6 = JTBToolkit.makeNodeToken(n7);
			n8 = AdditiveExpression();
			{

				return new OmpForAdditive(n0, n2, n4, n6, n8);
			}

		}

		final public OmpForSubtractive OmpForSubtractive() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			NodeToken n4;
			Token n5;
			NodeToken n6;
			Token n7;
			AdditiveExpression n8;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(OP_ASS);
			n2 = JTBToolkit.makeNodeToken(n3);
			n5 = jj_consume_token(IDENTIFIER);
			n4 = JTBToolkit.makeNodeToken(n5);
			n7 = jj_consume_token(OP_SUB);
			n6 = JTBToolkit.makeNodeToken(n7);
			n8 = AdditiveExpression();
			{

				return new OmpForSubtractive(n0, n2, n4, n6, n8);
			}

		}

		final public OmpForMultiplicative OmpForMultiplicative() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			MultiplicativeExpression n4;
			NodeToken n5;
			Token n6;
			NodeToken n7;
			Token n8;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(OP_ASS);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = MultiplicativeExpression();
			n6 = jj_consume_token(OP_ADD);
			n5 = JTBToolkit.makeNodeToken(n6);
			n8 = jj_consume_token(IDENTIFIER);
			n7 = JTBToolkit.makeNodeToken(n8);
			{

				return new OmpForMultiplicative(n0, n2, n4, n5, n7);
			}

		}

		final public SectionsConstruct SectionsConstruct() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			NowaitDataClauseList n3;
			OmpEol n4;
			SectionsScope n5;
			n0 = OmpPragma();
			n2 = jj_consume_token(SECTIONS);
			n1 = JTBToolkit.makeNodeToken(n2);
			n3 = NowaitDataClauseList();
			n4 = OmpEol();
			n5 = SectionsScope();
			{

				return new SectionsConstruct(n0, n1, n3, n4, n5);
			}

		}

		final public NowaitDataClauseList NowaitDataClauseList() throws ParseException {
			NodeListOptional n0 = new NodeListOptional();
			ANowaitDataClause n1;
			label_17: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case DFLT:
				case NOWAIT:
				case REDUCTION:
				case PRIVATE:
				case FIRSTPRIVATE:
				case LASTPRIVATE:
				case SHARED:
				case COPYIN:
					;
					break;
				default:
					jj_la1[60] = jj_gen;
					break label_17;
				}
				n1 = ANowaitDataClause();
				n0.addNode(n1);
			}
			n0.trimToSize();
			{

				return new NowaitDataClauseList(n0);
			}

		}

		final public ANowaitDataClause ANowaitDataClause() throws ParseException {
			NodeChoice n0;
			NowaitClause n1;
			DataClause n2;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case NOWAIT:
				n1 = NowaitClause();
				n0 = new NodeChoice(n1, 0);
				break;
			case DFLT:
			case REDUCTION:
			case PRIVATE:
			case FIRSTPRIVATE:
			case LASTPRIVATE:
			case SHARED:
			case COPYIN:
				n2 = DataClause();
				n0 = new NodeChoice(n2, 1);
				break;
			default:
				jj_la1[61] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new ANowaitDataClause(n0);
			}

		}

		final public SectionsScope SectionsScope() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeOptional n2 = new NodeOptional();
			Statement n3;
			NodeListOptional n4 = new NodeListOptional();
			ASection n5;
			NodeToken n6;
			Token n7;
			n1 = jj_consume_token(LEFTBRACE);
			n0 = JTBToolkit.makeNodeToken(n1);
			if (jj_2_53(4)) {
				n3 = Statement();
				n2.addNode(n3);
			} else {
				;
			}
			label_18: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case CROSSBAR:
					;
					break;
				default:
					jj_la1[62] = jj_gen;
					break label_18;
				}
				n5 = ASection();
				n4.addNode(n5);
			}
			n4.trimToSize();
			n7 = jj_consume_token(RIGHTBRACE);
			n6 = JTBToolkit.makeNodeToken(n7);
			{

				return new SectionsScope(n0, n2, n4, n6);
			}

		}

		final public ASection ASection() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			OmpEol n3;
			Statement n4;
			n0 = OmpPragma();
			n2 = jj_consume_token(SECTION);
			n1 = JTBToolkit.makeNodeToken(n2);
			n3 = OmpEol();
			n4 = Statement();
			{

				return new ASection(n0, n1, n3, n4);
			}

		}

		final public SingleConstruct SingleConstruct() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			SingleClauseList n3;
			OmpEol n4;
			Statement n5;
			n0 = OmpPragma();
			n2 = jj_consume_token(SINGLE);
			n1 = JTBToolkit.makeNodeToken(n2);
			n3 = SingleClauseList();
			n4 = OmpEol();
			n5 = Statement();
			{

				return new SingleConstruct(n0, n1, n3, n4, n5);
			}

		}

		final public SingleClauseList SingleClauseList() throws ParseException {
			NodeListOptional n0 = new NodeListOptional();
			ASingleClause n1;
			label_19: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case DFLT:
				case NOWAIT:
				case REDUCTION:
				case PRIVATE:
				case FIRSTPRIVATE:
				case LASTPRIVATE:
				case COPYPRIVATE:
				case SHARED:
				case COPYIN:
					;
					break;
				default:
					jj_la1[63] = jj_gen;
					break label_19;
				}
				n1 = ASingleClause();
				n0.addNode(n1);
			}
			n0.trimToSize();
			{

				return new SingleClauseList(n0);
			}

		}

		final public ASingleClause ASingleClause() throws ParseException {
			NodeChoice n0;
			NowaitClause n1;
			DataClause n2;
			OmpCopyPrivateClause n3;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case NOWAIT:
				n1 = NowaitClause();
				n0 = new NodeChoice(n1, 0);
				break;
			case DFLT:
			case REDUCTION:
			case PRIVATE:
			case FIRSTPRIVATE:
			case LASTPRIVATE:
			case SHARED:
			case COPYIN:
				n2 = DataClause();
				n0 = new NodeChoice(n2, 1);
				break;
			case COPYPRIVATE:
				n3 = OmpCopyPrivateClause();
				n0 = new NodeChoice(n3, 2);
				break;
			default:
				jj_la1[64] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new ASingleClause(n0);
			}

		}

		final public OmpCopyPrivateClause OmpCopyPrivateClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			VariableList n4;
			NodeToken n5;
			Token n6;
			n1 = jj_consume_token(COPYPRIVATE);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = VariableList();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			{

				return new OmpCopyPrivateClause(n0, n2, n4, n5);
			}

		}

		final public TaskConstruct TaskConstruct() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			NodeListOptional n3 = new NodeListOptional();
			TaskClause n4;
			OmpEol n5;
			Statement n6;
			n0 = OmpPragma();
			n2 = jj_consume_token(TASK);
			n1 = JTBToolkit.makeNodeToken(n2);
			label_20: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case DFLT:
				case IF:
				case REDUCTION:
				case PRIVATE:
				case FIRSTPRIVATE:
				case LASTPRIVATE:
				case SHARED:
				case COPYIN:
				case UNTIED:
				case MERGEABLE:
				case FINAL:
					;
					break;
				default:
					jj_la1[65] = jj_gen;
					break label_20;
				}
				n4 = TaskClause();
				n3.addNode(n4);
			}
			n3.trimToSize();
			n5 = OmpEol();
			n6 = Statement();
			{

				return new TaskConstruct(n0, n1, n3, n5, n6);
			}

		}

		final public TaskClause TaskClause() throws ParseException {
			NodeChoice n0;
			DataClause n1;
			UniqueTaskClause n2;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case DFLT:
			case REDUCTION:
			case PRIVATE:
			case FIRSTPRIVATE:
			case LASTPRIVATE:
			case SHARED:
			case COPYIN:
				n1 = DataClause();
				n0 = new NodeChoice(n1, 0);
				break;
			case IF:
			case UNTIED:
			case MERGEABLE:
			case FINAL:
				n2 = UniqueTaskClause();
				n0 = new NodeChoice(n2, 1);
				break;
			default:
				jj_la1[66] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new TaskClause(n0);
			}

		}

		final public UniqueTaskClause UniqueTaskClause() throws ParseException {
			NodeChoice n0;
			IfClause n1;
			FinalClause n2;
			UntiedClause n3;
			MergeableClause n4;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case IF:
				n1 = IfClause();
				n0 = new NodeChoice(n1, 0);
				break;
			case FINAL:
				n2 = FinalClause();
				n0 = new NodeChoice(n2, 1);
				break;
			case UNTIED:
				n3 = UntiedClause();
				n0 = new NodeChoice(n3, 2);
				break;
			case MERGEABLE:
				n4 = MergeableClause();
				n0 = new NodeChoice(n4, 3);
				break;
			default:
				jj_la1[67] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new UniqueTaskClause(n0);
			}

		}

		final public FinalClause FinalClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			NodeToken n5;
			Token n6;
			n1 = jj_consume_token(FINAL);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			{

				return new FinalClause(n0, n2, n4, n5);
			}

		}

		final public UntiedClause UntiedClause() throws ParseException {
			NodeToken n0;
			Token n1;
			n1 = jj_consume_token(UNTIED);
			n0 = JTBToolkit.makeNodeToken(n1);
			{

				return new UntiedClause(n0);
			}

		}

		final public MergeableClause MergeableClause() throws ParseException {
			NodeToken n0;
			Token n1;
			n1 = jj_consume_token(MERGEABLE);
			n0 = JTBToolkit.makeNodeToken(n1);
			{

				return new MergeableClause(n0);
			}

		}

		final public ParallelForConstruct ParallelForConstruct() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			UniqueParallelOrUniqueForOrDataClauseList n5;
			OmpEol n6;
			OmpForHeader n7;
			Statement n8;
			n0 = OmpPragma();
			n2 = jj_consume_token(PARALLEL);
			n1 = JTBToolkit.makeNodeToken(n2);
			n4 = jj_consume_token(FOR);
			n3 = JTBToolkit.makeNodeToken(n4);
			n5 = UniqueParallelOrUniqueForOrDataClauseList();
			n6 = OmpEol();
			n7 = OmpForHeader();
			n8 = Statement();
			{

				return new ParallelForConstruct(n0, n1, n3, n5, n6, n7, n8);
			}

		}

		final public UniqueParallelOrUniqueForOrDataClauseList UniqueParallelOrUniqueForOrDataClauseList()
				throws ParseException {
			NodeListOptional n0 = new NodeListOptional();
			AUniqueParallelOrUniqueForOrDataClause n1;
			label_21: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case DFLT:
				case IF:
				case ORDERED:
				case SCHEDULE:
				case REDUCTION:
				case PRIVATE:
				case FIRSTPRIVATE:
				case LASTPRIVATE:
				case SHARED:
				case COPYIN:
				case NUM_THREADS:
				case COLLAPSE:
					;
					break;
				default:
					jj_la1[68] = jj_gen;
					break label_21;
				}
				n1 = AUniqueParallelOrUniqueForOrDataClause();
				n0.addNode(n1);
			}
			n0.trimToSize();
			{

				return new UniqueParallelOrUniqueForOrDataClauseList(n0);
			}

		}

		final public AUniqueParallelOrUniqueForOrDataClause AUniqueParallelOrUniqueForOrDataClause()
				throws ParseException {
			NodeChoice n0;
			UniqueParallelClause n1;
			UniqueForClause n2;
			DataClause n3;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case IF:
			case NUM_THREADS:
				n1 = UniqueParallelClause();
				n0 = new NodeChoice(n1, 0);
				break;
			case ORDERED:
			case SCHEDULE:
			case COLLAPSE:
				n2 = UniqueForClause();
				n0 = new NodeChoice(n2, 1);
				break;
			case DFLT:
			case REDUCTION:
			case PRIVATE:
			case FIRSTPRIVATE:
			case LASTPRIVATE:
			case SHARED:
			case COPYIN:
				n3 = DataClause();
				n0 = new NodeChoice(n3, 2);
				break;
			default:
				jj_la1[69] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new AUniqueParallelOrUniqueForOrDataClause(n0);
			}

		}

		final public ParallelSectionsConstruct ParallelSectionsConstruct() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			UniqueParallelOrDataClauseList n5;
			OmpEol n6;
			SectionsScope n7;
			n0 = OmpPragma();
			n2 = jj_consume_token(PARALLEL);
			n1 = JTBToolkit.makeNodeToken(n2);
			n4 = jj_consume_token(SECTIONS);
			n3 = JTBToolkit.makeNodeToken(n4);
			n5 = UniqueParallelOrDataClauseList();
			n6 = OmpEol();
			n7 = SectionsScope();
			{

				return new ParallelSectionsConstruct(n0, n1, n3, n5, n6, n7);
			}

		}

		final public MasterConstruct MasterConstruct() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			OmpEol n3;
			Statement n4;
			n0 = OmpPragma();
			n2 = jj_consume_token(MASTER);
			n1 = JTBToolkit.makeNodeToken(n2);
			n3 = OmpEol();
			n4 = Statement();
			{

				return new MasterConstruct(n0, n1, n3, n4);
			}

		}

		final public CriticalConstruct CriticalConstruct() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			NodeOptional n3 = new NodeOptional();
			RegionPhrase n4;
			OmpEol n5;
			Statement n6;
			n0 = OmpPragma();
			n2 = jj_consume_token(CRITICAL);
			n1 = JTBToolkit.makeNodeToken(n2);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case LEFTPAREN:
				n4 = RegionPhrase();
				n3.addNode(n4);
				break;
			default:
				jj_la1[70] = jj_gen;
				;
			}
			n5 = OmpEol();
			n6 = Statement();
			{

				return new CriticalConstruct(n0, n1, n3, n5, n6);
			}

		}

		final public RegionPhrase RegionPhrase() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			NodeToken n4;
			Token n5;
			n1 = jj_consume_token(LEFTPAREN);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(IDENTIFIER);
			n2 = JTBToolkit.makeNodeToken(n3);
			n5 = jj_consume_token(RIGHTPAREN);
			n4 = JTBToolkit.makeNodeToken(n5);
			{

				return new RegionPhrase(n0, n2, n4);
			}

		}

		final public AtomicConstruct AtomicConstruct() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			NodeOptional n3 = new NodeOptional();
			AtomicClause n4;
			OmpEol n5;
			Statement n6;
			n0 = OmpPragma();
			n2 = jj_consume_token(ATOMIC);
			n1 = JTBToolkit.makeNodeToken(n2);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case READ:
			case WRITE:
			case UPDATE:
			case CAPTURE:
				n4 = AtomicClause();
				n3.addNode(n4);
				break;
			default:
				jj_la1[71] = jj_gen;
				;
			}
			n5 = OmpEol();
			n6 = ExpressionStatement();
			{

				return new AtomicConstruct(n0, n1, n3, n5, n6);
			}

		}

		final public AtomicClause AtomicClause() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			NodeToken n7;
			Token n8;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case READ:
				n2 = jj_consume_token(READ);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case WRITE:
				n4 = jj_consume_token(WRITE);
				n3 = JTBToolkit.makeNodeToken(n4);
				n0 = new NodeChoice(n3, 1);
				break;
			case UPDATE:
				n6 = jj_consume_token(UPDATE);
				n5 = JTBToolkit.makeNodeToken(n6);
				n0 = new NodeChoice(n5, 2);
				break;
			case CAPTURE:
				n8 = jj_consume_token(CAPTURE);
				n7 = JTBToolkit.makeNodeToken(n8);
				n0 = new NodeChoice(n7, 3);
				break;
			default:
				jj_la1[72] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new AtomicClause(n0);
			}

		}

		final public FlushDirective FlushDirective() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			NodeOptional n3 = new NodeOptional();
			FlushVars n4;
			OmpEol n5;
			n0 = OmpPragma();
			n2 = jj_consume_token(FLUSH);
			n1 = JTBToolkit.makeNodeToken(n2);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case LEFTPAREN:
				n4 = FlushVars();
				n3.addNode(n4);
				break;
			default:
				jj_la1[73] = jj_gen;
				;
			}
			n5 = OmpEol();
			{

				return new FlushDirective(n0, n1, n3, n5);
			}

		}

		final public FlushVars FlushVars() throws ParseException {
			NodeToken n0;
			Token n1;
			VariableList n2;
			NodeToken n3;
			Token n4;
			n1 = jj_consume_token(LEFTPAREN);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = VariableList();
			n4 = jj_consume_token(RIGHTPAREN);
			n3 = JTBToolkit.makeNodeToken(n4);
			{

				return new FlushVars(n0, n2, n3);
			}

		}

		final public OrderedConstruct OrderedConstruct() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			OmpEol n3;
			Statement n4;
			n0 = OmpPragma();
			n2 = jj_consume_token(ORDERED);
			n1 = JTBToolkit.makeNodeToken(n2);
			n3 = OmpEol();
			n4 = Statement();
			{

				return new OrderedConstruct(n0, n1, n3, n4);
			}

		}

		final public BarrierDirective BarrierDirective() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			OmpEol n3;
			n0 = OmpPragma();
			n2 = jj_consume_token(BARRIER);
			n1 = JTBToolkit.makeNodeToken(n2);
			n3 = OmpEol();
			{

				return new BarrierDirective(n0, n1, n3);
			}

		}

		final public TaskwaitDirective TaskwaitDirective() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			OmpEol n3;
			n0 = OmpPragma();
			n2 = jj_consume_token(TASKWAIT);
			n1 = JTBToolkit.makeNodeToken(n2);
			n3 = OmpEol();
			{

				return new TaskwaitDirective(n0, n1, n3);
			}

		}

		final public TaskyieldDirective TaskyieldDirective() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			OmpEol n3;
			n0 = OmpPragma();
			n2 = jj_consume_token(TASKYIELD);
			n1 = JTBToolkit.makeNodeToken(n2);
			n3 = OmpEol();
			{

				return new TaskyieldDirective(n0, n1, n3);
			}

		}

		final public ThreadPrivateDirective ThreadPrivateDirective() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			VariableList n5;
			NodeToken n6;
			Token n7;
			OmpEol n8;
			n0 = OmpPragma();
			n2 = jj_consume_token(THREADPRIVATE);
			n1 = JTBToolkit.makeNodeToken(n2);
			n4 = jj_consume_token(LEFTPAREN);
			n3 = JTBToolkit.makeNodeToken(n4);
			n5 = VariableList();
			n7 = jj_consume_token(RIGHTPAREN);
			n6 = JTBToolkit.makeNodeToken(n7);
			n8 = OmpEol();
			{

				return new ThreadPrivateDirective(n0, n1, n3, n5, n6, n8);
			}

		}

		final public DeclareReductionDirective DeclareReductionDirective() throws ParseException {
			OmpPragma n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			ReductionOp n7;
			NodeToken n8;
			Token n9;
			ReductionTypeList n10;
			NodeToken n11;
			Token n12;
			Expression n13;
			NodeToken n14;
			Token n15;
			NodeOptional n16 = new NodeOptional();
			InitializerClause n17;
			OmpEol n18;
			n0 = OmpPragma();
			n2 = jj_consume_token(DECLARE);
			n1 = JTBToolkit.makeNodeToken(n2);
			n4 = jj_consume_token(REDUCTION);
			n3 = JTBToolkit.makeNodeToken(n4);
			n6 = jj_consume_token(LEFTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			n7 = ReductionOp();
			n9 = jj_consume_token(COLON);
			n8 = JTBToolkit.makeNodeToken(n9);
			n10 = ReductionTypeList();
			n12 = jj_consume_token(COLON);
			n11 = JTBToolkit.makeNodeToken(n12);
			n13 = Expression();
			n15 = jj_consume_token(RIGHTPAREN);
			n14 = JTBToolkit.makeNodeToken(n15);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case INITIALIZER:
				n17 = InitializerClause();
				n16.addNode(n17);
				break;
			default:
				jj_la1[74] = jj_gen;
				;
			}
			n18 = OmpEol();
			{

				return new DeclareReductionDirective(n0, n1, n3, n5, n7, n8, n10, n11, n13, n14, n16, n18);
			}

		}

		final public ReductionTypeList ReductionTypeList() throws ParseException {
			NodeListOptional n0 = new NodeListOptional();
			TypeSpecifier n1;
			label_22: while (true) {
				if (jj_2_54(1)) {
					;
				} else {
					break label_22;
				}
				n1 = TypeSpecifier();
				n0.addNode(n1);
			}
			n0.trimToSize();
			{

				return new ReductionTypeList(n0);
			}

		}

		final public InitializerClause InitializerClause() throws ParseException {
			NodeChoice n0;
			AssignInitializerClause n1;
			ArgumentInitializerClause n2;
			if (jj_2_55(5)) {
				n1 = AssignInitializerClause();
				n0 = new NodeChoice(n1, 0);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case INITIALIZER:
					n2 = ArgumentInitializerClause();
					n0 = new NodeChoice(n2, 1);
					break;
				default:
					jj_la1[75] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new InitializerClause(n0);
			}

		}

		final public AssignInitializerClause AssignInitializerClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			NodeToken n4;
			Token n5;
			NodeToken n6;
			Token n7;
			Initializer n8;
			NodeToken n9;
			Token n10;
			n1 = jj_consume_token(INITIALIZER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n5 = jj_consume_token(IDENTIFIER);
			n4 = JTBToolkit.makeNodeToken(n5);
			n7 = jj_consume_token(OP_ASS);
			n6 = JTBToolkit.makeNodeToken(n7);
			n8 = Initializer();
			n10 = jj_consume_token(RIGHTPAREN);
			n9 = JTBToolkit.makeNodeToken(n10);
			{

				return new AssignInitializerClause(n0, n2, n4, n6, n8, n9);
			}

		}

		final public ArgumentInitializerClause ArgumentInitializerClause() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			NodeToken n4;
			Token n5;
			NodeToken n6;
			Token n7;
			ExpressionList n8;
			NodeToken n9;
			Token n10;
			NodeToken n11;
			Token n12;
			n1 = jj_consume_token(INITIALIZER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n5 = jj_consume_token(IDENTIFIER);
			n4 = JTBToolkit.makeNodeToken(n5);
			n7 = jj_consume_token(LEFTPAREN);
			n6 = JTBToolkit.makeNodeToken(n7);
			n8 = ExpressionList();
			n10 = jj_consume_token(RIGHTPAREN);
			n9 = JTBToolkit.makeNodeToken(n10);
			n12 = jj_consume_token(RIGHTPAREN);
			n11 = JTBToolkit.makeNodeToken(n12);
			{

				return new ArgumentInitializerClause(n0, n2, n4, n6, n8, n9, n11);
			}

		}

		final public ReductionOp ReductionOp() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			NodeToken n7;
			Token n8;
			NodeToken n9;
			Token n10;
			NodeToken n11;
			Token n12;
			NodeToken n13;
			Token n14;
			NodeToken n15;
			Token n16;
			NodeToken n17;
			Token n18;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case IDENTIFIER:
				n2 = jj_consume_token(IDENTIFIER);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case OP_ADD:
				n4 = jj_consume_token(OP_ADD);
				n3 = JTBToolkit.makeNodeToken(n4);
				n0 = new NodeChoice(n3, 1);
				break;
			case OP_MUL:
				n6 = jj_consume_token(OP_MUL);
				n5 = JTBToolkit.makeNodeToken(n6);
				n0 = new NodeChoice(n5, 2);
				break;
			case OP_SUB:
				n8 = jj_consume_token(OP_SUB);
				n7 = JTBToolkit.makeNodeToken(n8);
				n0 = new NodeChoice(n7, 3);
				break;
			case OP_BITAND:
				n10 = jj_consume_token(OP_BITAND);
				n9 = JTBToolkit.makeNodeToken(n10);
				n0 = new NodeChoice(n9, 4);
				break;
			case OP_BITXOR:
				n12 = jj_consume_token(OP_BITXOR);
				n11 = JTBToolkit.makeNodeToken(n12);
				n0 = new NodeChoice(n11, 5);
				break;
			case OP_BITOR:
				n14 = jj_consume_token(OP_BITOR);
				n13 = JTBToolkit.makeNodeToken(n14);
				n0 = new NodeChoice(n13, 6);
				break;
			case OP_OR:
				n16 = jj_consume_token(OP_OR);
				n15 = JTBToolkit.makeNodeToken(n16);
				n0 = new NodeChoice(n15, 7);
				break;
			case OP_AND:
				n18 = jj_consume_token(OP_AND);
				n17 = JTBToolkit.makeNodeToken(n18);
				n0 = new NodeChoice(n17, 8);
				break;
			default:
				jj_la1[76] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new ReductionOp(n0);
			}

		}

		final public VariableList VariableList() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeListOptional n2 = new NodeListOptional();
			NodeSequence n3;
			NodeToken n4;
			Token n5;
			NodeToken n6;
			Token n7;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			label_23: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case COMMA:
					;
					break;
				default:
					jj_la1[77] = jj_gen;
					break label_23;
				}
				n3 = new NodeSequence(2);
				n5 = jj_consume_token(COMMA);
				n4 = JTBToolkit.makeNodeToken(n5);
				n3.addNode(n4);
				n7 = jj_consume_token(IDENTIFIER);
				n6 = JTBToolkit.makeNodeToken(n7);
				n3.addNode(n6);
				n2.addNode(n3);
			}
			n2.trimToSize();
			{

				return new VariableList(n0, n2);
			}

		}

		final public LabeledStatement LabeledStatement() throws ParseException {
			NodeChoice n0;
			SimpleLabeledStatement n1;
			CaseLabeledStatement n2;
			DefaultLabeledStatement n3;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case IDENTIFIER:
				n1 = SimpleLabeledStatement();
				n0 = new NodeChoice(n1, 0);
				break;
			case CASE:
				n2 = CaseLabeledStatement();
				n0 = new NodeChoice(n2, 1);
				break;
			case DFLT:
				n3 = DefaultLabeledStatement();
				n0 = new NodeChoice(n3, 2);
				break;
			default:
				jj_la1[78] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new LabeledStatement(n0);
			}

		}

		final public SimpleLabeledStatement SimpleLabeledStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Statement n4;
			n1 = jj_consume_token(IDENTIFIER);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(COLON);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Statement();
			{

				return new SimpleLabeledStatement(n0, n2, n4);
			}

		}

		final public CaseLabeledStatement CaseLabeledStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			ConstantExpression n2;
			NodeToken n3;
			Token n4;
			Statement n5;
			n1 = jj_consume_token(CASE);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = ConstantExpression();
			n4 = jj_consume_token(COLON);
			n3 = JTBToolkit.makeNodeToken(n4);
			n5 = Statement();
			{

				return new CaseLabeledStatement(n0, n2, n3, n5);
			}

		}

		final public DefaultLabeledStatement DefaultLabeledStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Statement n4;
			n1 = jj_consume_token(DFLT);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(COLON);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Statement();
			{

				return new DefaultLabeledStatement(n0, n2, n4);
			}

		}

		final public Statement ExpressionStatement() throws ParseException {
			NodeOptional n0 = new NodeOptional();
			Expression n1;
			NodeToken n2;
			Token n3;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case INTEGER_LITERAL:
			case FLOATING_POINT_LITERAL:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
			case SIZEOF:
			case OP_INCR:
			case OP_DECR:
			case OP_ADD:
			case OP_SUB:
			case OP_MUL:
			case OP_BITAND:
			case OP_NOT:
			case OP_BITNOT:
			case LEFTPAREN:
			case IDENTIFIER:
				n1 = Expression();
				n0.addNode(n1);
				break;
			default:
				jj_la1[79] = jj_gen;
				;
			}
			n3 = jj_consume_token(SEMICOLON);
			n2 = JTBToolkit.makeNodeToken(n3);
			{

				return ExpressionStatement.getExpressionStatementOrCallStatement(n0, n2);
			}

		}

		final public CompoundStatement CompoundStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeListOptional n2 = new NodeListOptional();
			CompoundStatementElement n3;
			NodeToken n4;
			Token n5;
			n1 = jj_consume_token(LEFTBRACE);
			n0 = JTBToolkit.makeNodeToken(n1);
			label_24: while (true) {
				if (jj_2_56(1)) {
					;
				} else {
					break label_24;
				}
				n3 = CompoundStatementElement();
				n2.addNode(n3);
			}
			n2.trimToSize();
			n5 = jj_consume_token(RIGHTBRACE);
			n4 = JTBToolkit.makeNodeToken(n5);
			{

				return new CompoundStatement(n0, n2, n4);
			}

		}

		final public CompoundStatementElement CompoundStatementElement() throws ParseException {
			NodeChoice n0;
			Declaration n1;
			Statement n2;
			if (jj_2_57(2147483647)) {
				n1 = Declaration();
				n0 = new NodeChoice(n1, 0);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case INTEGER_LITERAL:
				case FLOATING_POINT_LITERAL:
				case CHARACTER_LITERAL:
				case STRING_LITERAL:
				case CONTINUE:
				case DFLT:
				case SWITCH:
				case RETURN:
				case WHILE:
				case BREAK:
				case CASE:
				case GOTO:
				case FOR:
				case IF:
				case DO:
				case SIZEOF:
				case OP_INCR:
				case OP_DECR:
				case OP_ADD:
				case OP_SUB:
				case OP_MUL:
				case OP_BITAND:
				case OP_NOT:
				case OP_BITNOT:
				case SEMICOLON:
				case LEFTPAREN:
				case LEFTBRACE:
				case CROSSBAR:
				case IDENTIFIER:
					n2 = Statement();
					n0 = new NodeChoice(n2, 1);
					break;
				default:
					jj_la1[80] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new CompoundStatementElement(n0);
			}

		}

		final public SelectionStatement SelectionStatement() throws ParseException {
			NodeChoice n0;
			IfStatement n1;
			SwitchStatement n2;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case IF:
				n1 = IfStatement();
				n0 = new NodeChoice(n1, 0);
				break;
			case SWITCH:
				n2 = SwitchStatement();
				n0 = new NodeChoice(n2, 1);
				break;
			default:
				jj_la1[81] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new SelectionStatement(n0);
			}

		}

		final public IfStatement IfStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			NodeToken n5;
			Token n6;
			Statement n7;
			NodeOptional n8 = new NodeOptional();
			NodeSequence n9;
			NodeToken n10;
			Token n11;
			Statement n12;
			n1 = jj_consume_token(IF);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			n7 = Statement();
			if (jj_2_58(2)) {
				n9 = new NodeSequence(2);
				n11 = jj_consume_token(ELSE);
				n10 = JTBToolkit.makeNodeToken(n11);
				n9.addNode(n10);
				n12 = Statement();
				n9.addNode(n12);
				n8.addNode(n9);
			} else {
				;
			}
			{

				return new IfStatement(n0, n2, n4, n5, n7, n8);
			}

		}

		final public SwitchStatement SwitchStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			NodeToken n5;
			Token n6;
			Statement n7;
			n1 = jj_consume_token(SWITCH);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			n7 = Statement();
			{

				return new SwitchStatement(n0, n2, n4, n5, n7);
			}

		}

		final public IterationStatement IterationStatement() throws ParseException {
			NodeChoice n0;
			WhileStatement n1;
			DoStatement n2;
			ForStatement n3;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case WHILE:
				n1 = WhileStatement();
				n0 = new NodeChoice(n1, 0);
				break;
			case DO:
				n2 = DoStatement();
				n0 = new NodeChoice(n2, 1);
				break;
			case FOR:
				n3 = ForStatement();
				n0 = new NodeChoice(n3, 2);
				break;
			default:
				jj_la1[82] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new IterationStatement(n0);
			}

		}

		final public WhileStatement WhileStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			Expression n4;
			NodeToken n5;
			Token n6;
			Statement n7;
			n1 = jj_consume_token(WHILE);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = Expression();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			n7 = Statement();
			{

				return new WhileStatement(n0, n2, n4, n5, n7);
			}

		}

		final public DoStatement DoStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			Statement n2;
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			Expression n7;
			NodeToken n8;
			Token n9;
			NodeToken n10;
			Token n11;
			n1 = jj_consume_token(DO);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = Statement();
			n4 = jj_consume_token(WHILE);
			n3 = JTBToolkit.makeNodeToken(n4);
			n6 = jj_consume_token(LEFTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			n7 = Expression();
			n9 = jj_consume_token(RIGHTPAREN);
			n8 = JTBToolkit.makeNodeToken(n9);
			n11 = jj_consume_token(SEMICOLON);
			n10 = JTBToolkit.makeNodeToken(n11);
			{

				return new DoStatement(n0, n2, n3, n5, n7, n8, n10);
			}

		}

		final public ForStatement ForStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			NodeOptional n4 = new NodeOptional();
			Expression n5;
			NodeToken n6;
			Token n7;
			NodeOptional n8 = new NodeOptional();
			Expression n9;
			NodeToken n10;
			Token n11;
			NodeOptional n12 = new NodeOptional();
			Expression n13;
			NodeToken n14;
			Token n15;
			Statement n16;
			n1 = jj_consume_token(FOR);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case INTEGER_LITERAL:
			case FLOATING_POINT_LITERAL:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
			case SIZEOF:
			case OP_INCR:
			case OP_DECR:
			case OP_ADD:
			case OP_SUB:
			case OP_MUL:
			case OP_BITAND:
			case OP_NOT:
			case OP_BITNOT:
			case LEFTPAREN:
			case IDENTIFIER:
				n5 = Expression();
				n4.addNode(n5);
				break;
			default:
				jj_la1[83] = jj_gen;
				;
			}
			n7 = jj_consume_token(SEMICOLON);
			n6 = JTBToolkit.makeNodeToken(n7);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case INTEGER_LITERAL:
			case FLOATING_POINT_LITERAL:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
			case SIZEOF:
			case OP_INCR:
			case OP_DECR:
			case OP_ADD:
			case OP_SUB:
			case OP_MUL:
			case OP_BITAND:
			case OP_NOT:
			case OP_BITNOT:
			case LEFTPAREN:
			case IDENTIFIER:
				n9 = Expression();
				n8.addNode(n9);
				break;
			default:
				jj_la1[84] = jj_gen;
				;
			}
			n11 = jj_consume_token(SEMICOLON);
			n10 = JTBToolkit.makeNodeToken(n11);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case INTEGER_LITERAL:
			case FLOATING_POINT_LITERAL:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
			case SIZEOF:
			case OP_INCR:
			case OP_DECR:
			case OP_ADD:
			case OP_SUB:
			case OP_MUL:
			case OP_BITAND:
			case OP_NOT:
			case OP_BITNOT:
			case LEFTPAREN:
			case IDENTIFIER:
				n13 = Expression();
				n12.addNode(n13);
				break;
			default:
				jj_la1[85] = jj_gen;
				;
			}
			n15 = jj_consume_token(RIGHTPAREN);
			n14 = JTBToolkit.makeNodeToken(n15);
			n16 = Statement();
			{

				return new ForStatement(n0, n2, n4, n6, n8, n10, n12, n14, n16);
			}

		}

		final public JumpStatement JumpStatement() throws ParseException {
			NodeChoice n0;
			GotoStatement n1;
			ContinueStatement n2;
			BreakStatement n3;
			ReturnStatement n4;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case GOTO:
				n1 = GotoStatement();
				n0 = new NodeChoice(n1, 0);
				break;
			case CONTINUE:
				n2 = ContinueStatement();
				n0 = new NodeChoice(n2, 1);
				break;
			case BREAK:
				n3 = BreakStatement();
				n0 = new NodeChoice(n3, 2);
				break;
			case RETURN:
				n4 = ReturnStatement();
				n0 = new NodeChoice(n4, 3);
				break;
			default:
				jj_la1[86] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new JumpStatement(n0);
			}

		}

		final public GotoStatement GotoStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			NodeToken n4;
			Token n5;
			n1 = jj_consume_token(GOTO);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(IDENTIFIER);
			n2 = JTBToolkit.makeNodeToken(n3);
			n5 = jj_consume_token(SEMICOLON);
			n4 = JTBToolkit.makeNodeToken(n5);
			{

				return new GotoStatement(n0, n2, n4);
			}

		}

		final public ContinueStatement ContinueStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			n1 = jj_consume_token(CONTINUE);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(SEMICOLON);
			n2 = JTBToolkit.makeNodeToken(n3);
			{

				return new ContinueStatement(n0, n2);
			}

		}

		final public BreakStatement BreakStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			n1 = jj_consume_token(BREAK);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(SEMICOLON);
			n2 = JTBToolkit.makeNodeToken(n3);
			{

				return new BreakStatement(n0, n2);
			}

		}

		final public ReturnStatement ReturnStatement() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeOptional n2 = new NodeOptional();
			Expression n3;
			NodeToken n4;
			Token n5;
			n1 = jj_consume_token(RETURN);
			n0 = JTBToolkit.makeNodeToken(n1);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case INTEGER_LITERAL:
			case FLOATING_POINT_LITERAL:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
			case SIZEOF:
			case OP_INCR:
			case OP_DECR:
			case OP_ADD:
			case OP_SUB:
			case OP_MUL:
			case OP_BITAND:
			case OP_NOT:
			case OP_BITNOT:
			case LEFTPAREN:
			case IDENTIFIER:
				n3 = Expression();
				n2.addNode(n3);
				break;
			default:
				jj_la1[87] = jj_gen;
				;
			}
			n5 = jj_consume_token(SEMICOLON);
			n4 = JTBToolkit.makeNodeToken(n5);
			{

				return new ReturnStatement(n0, n2, n4);
			}

		}

		final public Expression Expression() throws ParseException {
			AssignmentExpression n0;
			NodeListOptional n1 = new NodeListOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			AssignmentExpression n5;
			n0 = AssignmentExpression();
			label_25: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case COMMA:
					;
					break;
				default:
					jj_la1[88] = jj_gen;
					break label_25;
				}
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(COMMA);
				n3 = JTBToolkit.makeNodeToken(n4);
				if (parseACall) {
					throw new ParseException();
				}
				n2.addNode(n3);
				n5 = AssignmentExpression();
				n2.addNode(n5);
				n1.addNode(n2);
			}
			n1.trimToSize();
			{

				return new Expression(n0, n1);
			}

		}

		final public AssignmentExpression AssignmentExpression() throws ParseException {
			NodeChoice n0;
			NonConditionalExpression n1;
			ConditionalExpression n2;
			if (jj_2_59(2147483647)) {
				n1 = NonConditionalExpression();
				n0 = new NodeChoice(n1, 0);
			} else if (jj_2_60(3)) {
				n2 = ConditionalExpression();
				n0 = new NodeChoice(n2, 1);
			} else {
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new AssignmentExpression(n0);
			}

		}

		final public NonConditionalExpression NonConditionalExpression() throws ParseException {
			UnaryExpression n0;
			AssignmentOperator n1;
			AssignmentExpression n2;
			if (parseACall) {
				throw new ParseException();
			}
			n0 = UnaryExpression();
			n1 = AssignmentOperator();
			n2 = AssignmentExpression();
			{

				return new NonConditionalExpression(n0, n1, n2);
			}

		}

		final public AssignmentOperator AssignmentOperator() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			NodeToken n7;
			Token n8;
			NodeToken n9;
			Token n10;
			NodeToken n11;
			Token n12;
			NodeToken n13;
			Token n14;
			NodeToken n15;
			Token n16;
			NodeToken n17;
			Token n18;
			NodeToken n19;
			Token n20;
			NodeToken n21;
			Token n22;
			if (parseACall) {
				throw new ParseException();
			}
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_ASS:
				n2 = jj_consume_token(OP_ASS);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case OP_MULASS:
				n4 = jj_consume_token(OP_MULASS);
				n3 = JTBToolkit.makeNodeToken(n4);
				n0 = new NodeChoice(n3, 1);
				break;
			case OP_DIVASS:
				n6 = jj_consume_token(OP_DIVASS);
				n5 = JTBToolkit.makeNodeToken(n6);
				n0 = new NodeChoice(n5, 2);
				break;
			case OP_MODASS:
				n8 = jj_consume_token(OP_MODASS);
				n7 = JTBToolkit.makeNodeToken(n8);
				n0 = new NodeChoice(n7, 3);
				break;
			case OP_ADDASS:
				n10 = jj_consume_token(OP_ADDASS);
				n9 = JTBToolkit.makeNodeToken(n10);
				n0 = new NodeChoice(n9, 4);
				break;
			case OP_SUBASS:
				n12 = jj_consume_token(OP_SUBASS);
				n11 = JTBToolkit.makeNodeToken(n12);
				n0 = new NodeChoice(n11, 5);
				break;
			case OP_SLASS:
				n14 = jj_consume_token(OP_SLASS);
				n13 = JTBToolkit.makeNodeToken(n14);
				n0 = new NodeChoice(n13, 6);
				break;
			case OP_SRASS:
				n16 = jj_consume_token(OP_SRASS);
				n15 = JTBToolkit.makeNodeToken(n16);
				n0 = new NodeChoice(n15, 7);
				break;
			case OP_ANDASS:
				n18 = jj_consume_token(OP_ANDASS);
				n17 = JTBToolkit.makeNodeToken(n18);
				n0 = new NodeChoice(n17, 8);
				break;
			case OP_XORASS:
				n20 = jj_consume_token(OP_XORASS);
				n19 = JTBToolkit.makeNodeToken(n20);
				n0 = new NodeChoice(n19, 9);
				break;
			case OP_ORASS:
				n22 = jj_consume_token(OP_ORASS);
				n21 = JTBToolkit.makeNodeToken(n22);
				n0 = new NodeChoice(n21, 10);
				break;
			default:
				jj_la1[89] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new AssignmentOperator(n0);
			}

		}

		final public ConditionalExpression ConditionalExpression() throws ParseException {
			LogicalORExpression n0;
			NodeOptional n1 = new NodeOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			Expression n5;
			NodeToken n6;
			Token n7;
			ConditionalExpression n8;
			n0 = LogicalORExpression();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case QUESTION:
				if (parseACall) {
					throw new ParseException();
				}
				n2 = new NodeSequence(4);
				n4 = jj_consume_token(QUESTION);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = Expression();
				n2.addNode(n5);
				n7 = jj_consume_token(COLON);
				n6 = JTBToolkit.makeNodeToken(n7);
				n2.addNode(n6);
				n8 = ConditionalExpression();
				n2.addNode(n8);
				n1.addNode(n2);
				break;
			default:
				jj_la1[90] = jj_gen;
				;
			}
			{

				return new ConditionalExpression(n0, n1);
			}

		}

		final public ConstantExpression ConstantExpression() throws ParseException {
			ConditionalExpression n0;
			n0 = ConditionalExpression();
			{

				return new ConstantExpression(n0);
			}

		}

		final public LogicalORExpression LogicalORExpression() throws ParseException {
			LogicalANDExpression n0;
			NodeOptional n1 = new NodeOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			LogicalORExpression n5;
			n0 = LogicalANDExpression();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_OR:
				if (parseACall) {
					throw new ParseException();
				}
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(OP_OR);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = LogicalORExpression();
				n2.addNode(n5);
				n1.addNode(n2);
				break;
			default:
				jj_la1[91] = jj_gen;
				;
			}
			{

				return new LogicalORExpression(n0, n1);
			}

		}

		final public LogicalANDExpression LogicalANDExpression() throws ParseException {
			InclusiveORExpression n0;
			NodeOptional n1 = new NodeOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			LogicalANDExpression n5;
			n0 = InclusiveORExpression();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_AND:
				if (parseACall) {
					throw new ParseException();
				}
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(OP_AND);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = LogicalANDExpression();
				n2.addNode(n5);
				n1.addNode(n2);
				break;
			default:
				jj_la1[92] = jj_gen;
				;
			}
			{

				return new LogicalANDExpression(n0, n1);
			}

		}

		final public InclusiveORExpression InclusiveORExpression() throws ParseException {
			ExclusiveORExpression n0;
			NodeOptional n1 = new NodeOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			InclusiveORExpression n5;
			n0 = ExclusiveORExpression();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_BITOR:
				if (parseACall) {
					throw new ParseException();
				}
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(OP_BITOR);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = InclusiveORExpression();
				n2.addNode(n5);
				n1.addNode(n2);
				break;
			default:
				jj_la1[93] = jj_gen;
				;
			}
			{

				return new InclusiveORExpression(n0, n1);
			}

		}

		final public ExclusiveORExpression ExclusiveORExpression() throws ParseException {
			ANDExpression n0;
			NodeOptional n1 = new NodeOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			ExclusiveORExpression n5;
			n0 = ANDExpression();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_BITXOR:
				if (parseACall) {
					throw new ParseException();
				}
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(OP_BITXOR);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = ExclusiveORExpression();
				n2.addNode(n5);
				n1.addNode(n2);
				break;
			default:
				jj_la1[94] = jj_gen;
				;
			}
			{

				return new ExclusiveORExpression(n0, n1);
			}

		}

		final public ANDExpression ANDExpression() throws ParseException {
			EqualityExpression n0;
			NodeOptional n1 = new NodeOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			ANDExpression n5;
			n0 = EqualityExpression();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_BITAND:
				if (parseACall) {
					throw new ParseException();
				}
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(OP_BITAND);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = ANDExpression();
				n2.addNode(n5);
				n1.addNode(n2);
				break;
			default:
				jj_la1[95] = jj_gen;
				;
			}
			{

				return new ANDExpression(n0, n1);
			}

		}

		final public EqualityExpression EqualityExpression() throws ParseException {
			RelationalExpression n0;
			NodeOptional n1 = new NodeOptional();
			EqualOptionalExpression n2;
			n0 = RelationalExpression();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_EQ:
			case OP_NEQ:
				if (parseACall) {
					throw new ParseException();
				}
				n2 = EqualOptionalExpression();
				n1.addNode(n2);
				break;
			default:
				jj_la1[96] = jj_gen;
				;
			}
			{

				return new EqualityExpression(n0, n1);
			}

		}

		final public EqualOptionalExpression EqualOptionalExpression() throws ParseException {
			NodeChoice n0;
			EqualExpression n1;
			NonEqualExpression n2;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_EQ:
				n1 = EqualExpression();
				n0 = new NodeChoice(n1, 0);
				break;
			case OP_NEQ:
				n2 = NonEqualExpression();
				n0 = new NodeChoice(n2, 1);
				break;
			default:
				jj_la1[97] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new EqualOptionalExpression(n0);
			}

		}

		final public EqualExpression EqualExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			EqualityExpression n2;
			n1 = jj_consume_token(OP_EQ);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = EqualityExpression();
			{

				return new EqualExpression(n0, n2);
			}

		}

		final public NonEqualExpression NonEqualExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			EqualityExpression n2;
			n1 = jj_consume_token(OP_NEQ);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = EqualityExpression();
			{

				return new NonEqualExpression(n0, n2);
			}

		}

		final public RelationalExpression RelationalExpression() throws ParseException {
			ShiftExpression n0;
			NodeOptional n1 = new NodeOptional();
			RelationalOptionalExpression n2;
			n0 = ShiftExpression();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_GE:
			case OP_LE:
			case OP_GT:
			case OP_LT:
				if (parseACall) {
					throw new ParseException();
				}
				n2 = RelationalOptionalExpression();
				n1.addNode(n2);
				break;
			default:
				jj_la1[98] = jj_gen;
				;
			}
			{

				return new RelationalExpression(n0, n1);
			}

		}

		final public RelationalOptionalExpression RelationalOptionalExpression() throws ParseException {
			NodeChoice n0;
			RelationalLTExpression n1;
			RelationalGTExpression n2;
			RelationalLEExpression n3;
			RelationalGEExpression n4;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_LT:
				n1 = RelationalLTExpression();
				n0 = new NodeChoice(n1, 0);
				break;
			case OP_GT:
				n2 = RelationalGTExpression();
				n0 = new NodeChoice(n2, 1);
				break;
			case OP_LE:
				n3 = RelationalLEExpression();
				n0 = new NodeChoice(n3, 2);
				break;
			case OP_GE:
				n4 = RelationalGEExpression();
				n0 = new NodeChoice(n4, 3);
				break;
			default:
				jj_la1[99] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new RelationalOptionalExpression(n0);
			}

		}

		final public RelationalLTExpression RelationalLTExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			RelationalExpression n2;
			n1 = jj_consume_token(OP_LT);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = RelationalExpression();
			{

				return new RelationalLTExpression(n0, n2);
			}

		}

		final public RelationalGTExpression RelationalGTExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			RelationalExpression n2;
			n1 = jj_consume_token(OP_GT);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = RelationalExpression();
			{

				return new RelationalGTExpression(n0, n2);
			}

		}

		final public RelationalLEExpression RelationalLEExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			RelationalExpression n2;
			n1 = jj_consume_token(OP_LE);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = RelationalExpression();
			{

				return new RelationalLEExpression(n0, n2);
			}

		}

		final public RelationalGEExpression RelationalGEExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			RelationalExpression n2;
			n1 = jj_consume_token(OP_GE);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = RelationalExpression();
			{

				return new RelationalGEExpression(n0, n2);
			}

		}

		final public ShiftExpression ShiftExpression() throws ParseException {
			AdditiveExpression n0;
			NodeOptional n1 = new NodeOptional();
			ShiftOptionalExpression n2;
			n0 = AdditiveExpression();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_SL:
			case OP_SR:
				if (parseACall) {
					throw new ParseException();
				}
				n2 = ShiftOptionalExpression();
				n1.addNode(n2);
				break;
			default:
				jj_la1[100] = jj_gen;
				;
			}
			{

				return new ShiftExpression(n0, n1);
			}

		}

		final public ShiftOptionalExpression ShiftOptionalExpression() throws ParseException {
			NodeChoice n0;
			ShiftLeftExpression n1;
			ShiftRightExpression n2;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_SR:
				n1 = ShiftLeftExpression();
				n0 = new NodeChoice(n1, 0);
				break;
			case OP_SL:
				n2 = ShiftRightExpression();
				n0 = new NodeChoice(n2, 1);
				break;
			default:
				jj_la1[101] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new ShiftOptionalExpression(n0);
			}

		}

		final public ShiftLeftExpression ShiftLeftExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			ShiftExpression n2;
			n1 = jj_consume_token(OP_SR);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = ShiftExpression();
			{

				return new ShiftLeftExpression(n0, n2);
			}

		}

		final public ShiftRightExpression ShiftRightExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			ShiftExpression n2;
			n1 = jj_consume_token(OP_SL);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = ShiftExpression();
			{

				return new ShiftRightExpression(n0, n2);
			}

		}

		final public AdditiveExpression AdditiveExpression() throws ParseException {
			MultiplicativeExpression n0;
			NodeOptional n1 = new NodeOptional();
			AdditiveOptionalExpression n2;
			n0 = MultiplicativeExpression();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_ADD:
			case OP_SUB:
				if (parseACall) {
					throw new ParseException();
				}
				n2 = AdditiveOptionalExpression();
				n1.addNode(n2);
				break;
			default:
				jj_la1[102] = jj_gen;
				;
			}
			{

				return new AdditiveExpression(n0, n1);
			}

		}

		final public AdditiveOptionalExpression AdditiveOptionalExpression() throws ParseException {
			NodeChoice n0;
			AdditivePlusExpression n1;
			AdditiveMinusExpression n2;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_ADD:
				n1 = AdditivePlusExpression();
				n0 = new NodeChoice(n1, 0);
				break;
			case OP_SUB:
				n2 = AdditiveMinusExpression();
				n0 = new NodeChoice(n2, 1);
				break;
			default:
				jj_la1[103] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new AdditiveOptionalExpression(n0);
			}

		}

		final public AdditivePlusExpression AdditivePlusExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			AdditiveExpression n2;
			n1 = jj_consume_token(OP_ADD);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = AdditiveExpression();
			{

				return new AdditivePlusExpression(n0, n2);
			}

		}

		final public AdditiveMinusExpression AdditiveMinusExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			AdditiveExpression n2;
			n1 = jj_consume_token(OP_SUB);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = AdditiveExpression();
			{

				return new AdditiveMinusExpression(n0, n2);
			}

		}

		final public MultiplicativeExpression MultiplicativeExpression() throws ParseException {
			CastExpression n0;
			NodeOptional n1 = new NodeOptional();
			MultiplicativeOptionalExpression n2;
			n0 = CastExpression();
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_MUL:
			case OP_DIV:
			case OP_MOD:
				if (parseACall) {
					throw new ParseException();
				}
				n2 = MultiplicativeOptionalExpression();
				n1.addNode(n2);
				break;
			default:
				jj_la1[104] = jj_gen;
				;
			}
			{

				return new MultiplicativeExpression(n0, n1);
			}

		}

		final public MultiplicativeOptionalExpression MultiplicativeOptionalExpression() throws ParseException {
			NodeChoice n0;
			MultiplicativeMultiExpression n1;
			MultiplicativeDivExpression n2;
			MultiplicativeModExpression n3;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_MUL:
				n1 = MultiplicativeMultiExpression();
				n0 = new NodeChoice(n1, 0);
				break;
			case OP_DIV:
				n2 = MultiplicativeDivExpression();
				n0 = new NodeChoice(n2, 1);
				break;
			case OP_MOD:
				n3 = MultiplicativeModExpression();
				n0 = new NodeChoice(n3, 2);
				break;
			default:
				jj_la1[105] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new MultiplicativeOptionalExpression(n0);
			}

		}

		final public MultiplicativeMultiExpression MultiplicativeMultiExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			MultiplicativeExpression n2;
			n1 = jj_consume_token(OP_MUL);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = MultiplicativeExpression();
			{

				return new MultiplicativeMultiExpression(n0, n2);
			}

		}

		final public MultiplicativeDivExpression MultiplicativeDivExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			MultiplicativeExpression n2;
			n1 = jj_consume_token(OP_DIV);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = MultiplicativeExpression();
			{

				return new MultiplicativeDivExpression(n0, n2);
			}

		}

		final public MultiplicativeModExpression MultiplicativeModExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			MultiplicativeExpression n2;
			n1 = jj_consume_token(OP_MOD);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = MultiplicativeExpression();
			{

				return new MultiplicativeModExpression(n0, n2);
			}

		}

		final public CastExpression CastExpression() throws ParseException {
			NodeChoice n0;
			CastExpressionTyped n1;
			UnaryExpression n2;
			if (jj_2_61(2147483647)) {
				n1 = CastExpressionTyped();
				n0 = new NodeChoice(n1, 0);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case INTEGER_LITERAL:
				case FLOATING_POINT_LITERAL:
				case CHARACTER_LITERAL:
				case STRING_LITERAL:
				case SIZEOF:
				case OP_INCR:
				case OP_DECR:
				case OP_ADD:
				case OP_SUB:
				case OP_MUL:
				case OP_BITAND:
				case OP_NOT:
				case OP_BITNOT:
				case LEFTPAREN:
				case IDENTIFIER:
					n2 = UnaryExpression();
					n0 = new NodeChoice(n2, 1);
					break;
				default:
					jj_la1[106] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new CastExpression(n0);
			}

		}

		final public CastExpressionTyped CastExpressionTyped() throws ParseException {
			NodeToken n0;
			Token n1;
			TypeName n2;
			NodeToken n3;
			Token n4;
			CastExpression n5;
			if (parseACall) {
				throw new ParseException();
			}
			n1 = jj_consume_token(LEFTPAREN);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = TypeName();
			n4 = jj_consume_token(RIGHTPAREN);
			n3 = JTBToolkit.makeNodeToken(n4);
			n5 = CastExpression();
			{

				return new CastExpressionTyped(n0, n2, n3, n5);
			}

		}

		final public UnaryExpression UnaryExpression() throws ParseException {
			NodeChoice n0;
			UnaryExpressionPreIncrement n1;
			UnaryExpressionPreDecrement n2;
			UnarySizeofExpression n3;
			UnaryCastExpression n4;
			PostfixExpression n5;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_INCR:
				n1 = UnaryExpressionPreIncrement();
				n0 = new NodeChoice(n1, 0);
				break;
			case OP_DECR:
				n2 = UnaryExpressionPreDecrement();
				n0 = new NodeChoice(n2, 1);
				break;
			case SIZEOF:
				n3 = UnarySizeofExpression();
				n0 = new NodeChoice(n3, 2);
				break;
			case OP_ADD:
			case OP_SUB:
			case OP_MUL:
			case OP_BITAND:
			case OP_NOT:
			case OP_BITNOT:
				n4 = UnaryCastExpression();
				n0 = new NodeChoice(n4, 3);
				break;
			case INTEGER_LITERAL:
			case FLOATING_POINT_LITERAL:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
			case LEFTPAREN:
			case IDENTIFIER:
				n5 = PostfixExpression();
				n0 = new NodeChoice(n5, 4);
				break;
			default:
				jj_la1[107] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new UnaryExpression(n0);
			}

		}

		final public UnaryExpressionPreIncrement UnaryExpressionPreIncrement() throws ParseException {
			NodeToken n0;
			Token n1;
			UnaryExpression n2;
			if (parseACall) {
				throw new ParseException();
			}
			n1 = jj_consume_token(OP_INCR);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = UnaryExpression();
			{

				return new UnaryExpressionPreIncrement(n0, n2);
			}

		}

		final public UnaryExpressionPreDecrement UnaryExpressionPreDecrement() throws ParseException {
			NodeToken n0;
			Token n1;
			UnaryExpression n2;
			if (parseACall) {
				throw new ParseException();
			}
			n1 = jj_consume_token(OP_DECR);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = UnaryExpression();
			{

				return new UnaryExpressionPreDecrement(n0, n2);
			}

		}

		final public UnaryCastExpression UnaryCastExpression() throws ParseException {
			UnaryOperator n0;
			CastExpression n1;
			if (parseACall) {
				throw new ParseException();
			}
			n0 = UnaryOperator();
			n1 = CastExpression();
			{

				return new UnaryCastExpression(n0, n1);
			}

		}

		final public UnarySizeofExpression UnarySizeofExpression() throws ParseException {
			NodeChoice n0;
			SizeofTypeName n1;
			SizeofUnaryExpression n2;
			if (parseACall) {
				throw new ParseException();
			}
			if (jj_2_62(2147483647)) {
				n1 = SizeofTypeName();
				n0 = new NodeChoice(n1, 0);
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case SIZEOF:
					n2 = SizeofUnaryExpression();
					n0 = new NodeChoice(n2, 1);
					break;
				default:
					jj_la1[108] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			{

				return new UnarySizeofExpression(n0);
			}

		}

		final public SizeofUnaryExpression SizeofUnaryExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			UnaryExpression n2;
			n1 = jj_consume_token(SIZEOF);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = UnaryExpression();
			{

				return new SizeofUnaryExpression(n0, n2);
			}

		}

		final public SizeofTypeName SizeofTypeName() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			TypeName n4;
			NodeToken n5;
			Token n6;
			n1 = jj_consume_token(SIZEOF);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(LEFTPAREN);
			n2 = JTBToolkit.makeNodeToken(n3);
			n4 = TypeName();
			n6 = jj_consume_token(RIGHTPAREN);
			n5 = JTBToolkit.makeNodeToken(n6);
			{

				return new SizeofTypeName(n0, n2, n4, n5);
			}

		}

		final public UnaryOperator UnaryOperator() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			NodeToken n7;
			Token n8;
			NodeToken n9;
			Token n10;
			NodeToken n11;
			Token n12;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case OP_BITAND:
				n2 = jj_consume_token(OP_BITAND);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case OP_MUL:
				n4 = jj_consume_token(OP_MUL);
				n3 = JTBToolkit.makeNodeToken(n4);
				n0 = new NodeChoice(n3, 1);
				break;
			case OP_ADD:
				n6 = jj_consume_token(OP_ADD);
				n5 = JTBToolkit.makeNodeToken(n6);
				n0 = new NodeChoice(n5, 2);
				break;
			case OP_SUB:
				n8 = jj_consume_token(OP_SUB);
				n7 = JTBToolkit.makeNodeToken(n8);
				n0 = new NodeChoice(n7, 3);
				break;
			case OP_BITNOT:
				n10 = jj_consume_token(OP_BITNOT);
				n9 = JTBToolkit.makeNodeToken(n10);
				n0 = new NodeChoice(n9, 4);
				break;
			case OP_NOT:
				n12 = jj_consume_token(OP_NOT);
				n11 = JTBToolkit.makeNodeToken(n12);
				n0 = new NodeChoice(n11, 5);
				break;
			default:
				jj_la1[109] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new UnaryOperator(n0);
			}

		}

		final public PostfixExpression PostfixExpression() throws ParseException {
			PrimaryExpression n0;
			PostfixOperationsList n1;
			n0 = PrimaryExpression();
			if (parseACall) {
				if (!Misc.isSimplePrimaryExpression(n0)) {
					throw new ParseException();
				}
			}
			boolean iChanged = false;
			if (parseACall && !parseAnSPE) {
				parseAnSPE = true;
				iChanged = true;
			}
			n1 = PostfixOperationsList();
			if (parseACall && iChanged) {
				parseAnSPE = false;
			}
			if (parseACall) {
				if (parseAnSPE) {
					if (n1.getF0().getNodes().size() != 0) {
						throw new ParseException();
					}
				} else {
					if (n1.getF0().getNodes().size() != 1) {
						throw new ParseException();
					} else {
						APostfixOperation postFixOpNode = (APostfixOperation) n1.getF0().getNodes().get(0);
						Node postFixOp = postFixOpNode.getF0().getChoice();
						if (!(postFixOp instanceof ArgumentList)) {
							throw new ParseException();
						}
					}
				}
			}
			{

				return new PostfixExpression(n0, n1);
			}

		}

		final public PostfixOperationsList PostfixOperationsList() throws ParseException {
			NodeListOptional n0 = new NodeListOptional();
			APostfixOperation n1;
			label_26: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case OP_DEREF:
				case OP_INCR:
				case OP_DECR:
				case DOT:
				case LEFTPAREN:
				case LEFTBRACKET:
					;
					break;
				default:
					jj_la1[110] = jj_gen;
					break label_26;
				}
				n1 = APostfixOperation();
				n0.addNode(n1);
			}
			n0.trimToSize();
			{

				return new PostfixOperationsList(n0);
			}

		}

		final public APostfixOperation APostfixOperation() throws ParseException {
			NodeChoice n0;
			BracketExpression n1;
			ArgumentList n2;
			DotId n3;
			ArrowId n4;
			PlusPlus n5;
			MinusMinus n6;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case LEFTBRACKET:
				n1 = BracketExpression();
				n0 = new NodeChoice(n1, 0);
				break;
			case LEFTPAREN:
				n2 = ArgumentList();
				n0 = new NodeChoice(n2, 1);
				break;
			case DOT:
				n3 = DotId();
				n0 = new NodeChoice(n3, 2);
				break;
			case OP_DEREF:
				n4 = ArrowId();
				n0 = new NodeChoice(n4, 3);
				break;
			case OP_INCR:
				n5 = PlusPlus();
				n0 = new NodeChoice(n5, 4);
				break;
			case OP_DECR:
				n6 = MinusMinus();
				n0 = new NodeChoice(n6, 5);
				break;
			default:
				jj_la1[111] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new APostfixOperation(n0);
			}

		}

		final public PlusPlus PlusPlus() throws ParseException {
			NodeToken n0;
			Token n1;
			n1 = jj_consume_token(OP_INCR);
			n0 = JTBToolkit.makeNodeToken(n1);
			{

				return new PlusPlus(n0);
			}

		}

		final public MinusMinus MinusMinus() throws ParseException {
			NodeToken n0;
			Token n1;
			n1 = jj_consume_token(OP_DECR);
			n0 = JTBToolkit.makeNodeToken(n1);
			{

				return new MinusMinus(n0);
			}

		}

		final public BracketExpression BracketExpression() throws ParseException {
			NodeToken n0;
			Token n1;
			Expression n2;
			NodeToken n3;
			Token n4;
			n1 = jj_consume_token(LEFTBRACKET);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = Expression();
			n4 = jj_consume_token(RIGHTBRACKET);
			n3 = JTBToolkit.makeNodeToken(n4);
			{

				return new BracketExpression(n0, n2, n3);
			}

		}

		final public ArgumentList ArgumentList() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeOptional n2 = new NodeOptional();
			ExpressionList n3;
			NodeToken n4;
			Token n5;
			n1 = jj_consume_token(LEFTPAREN);
			n0 = JTBToolkit.makeNodeToken(n1);
			if (jj_2_63(2147483647)) {
				n3 = ExpressionList();
				n2.addNode(n3);
			} else {
				;
			}
			n5 = jj_consume_token(RIGHTPAREN);
			n4 = JTBToolkit.makeNodeToken(n5);
			{

				return new ArgumentList(n0, n2, n4);
			}

		}

		final public DotId DotId() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			n1 = jj_consume_token(DOT);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(IDENTIFIER);
			n2 = JTBToolkit.makeNodeToken(n3);
			{

				return new DotId(n0, n2);
			}

		}

		final public ArrowId ArrowId() throws ParseException {
			NodeToken n0;
			Token n1;
			NodeToken n2;
			Token n3;
			n1 = jj_consume_token(OP_DEREF);
			n0 = JTBToolkit.makeNodeToken(n1);
			n3 = jj_consume_token(IDENTIFIER);
			n2 = JTBToolkit.makeNodeToken(n3);
			{

				return new ArrowId(n0, n2);
			}

		}

		final public PrimaryExpression PrimaryExpression() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			Constant n3;
			ExpressionClosed n4;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case IDENTIFIER:
				n2 = jj_consume_token(IDENTIFIER);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case INTEGER_LITERAL:
			case FLOATING_POINT_LITERAL:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
				n3 = Constant();
				n0 = new NodeChoice(n3, 1);
				break;
			case LEFTPAREN:
				n4 = ExpressionClosed();
				n0 = new NodeChoice(n4, 2);
				break;
			default:
				jj_la1[112] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new PrimaryExpression(n0);
			}

		}

		final public ExpressionClosed ExpressionClosed() throws ParseException {
			NodeToken n0;
			Token n1;
			Expression n2;
			NodeToken n3;
			Token n4;
			n1 = jj_consume_token(LEFTPAREN);
			n0 = JTBToolkit.makeNodeToken(n1);
			n2 = Expression();
			n4 = jj_consume_token(RIGHTPAREN);
			n3 = JTBToolkit.makeNodeToken(n4);
			{

				return new ExpressionClosed(n0, n2, n3);
			}

		}

		final public ExpressionList ExpressionList() throws ParseException {
			AssignmentExpression n0;
			NodeListOptional n1 = new NodeListOptional();
			NodeSequence n2;
			NodeToken n3;
			Token n4;
			AssignmentExpression n5;
			n0 = AssignmentExpression();
			label_27: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case COMMA:
					;
					break;
				default:
					jj_la1[113] = jj_gen;
					break label_27;
				}
				n2 = new NodeSequence(2);
				n4 = jj_consume_token(COMMA);
				n3 = JTBToolkit.makeNodeToken(n4);
				n2.addNode(n3);
				n5 = AssignmentExpression();
				n2.addNode(n5);
				n1.addNode(n2);
			}
			n1.trimToSize();
			{

				return new ExpressionList(n0, n1);
			}

		}

		final public Constant Constant() throws ParseException {
			NodeChoice n0;
			NodeToken n1;
			Token n2;
			NodeToken n3;
			Token n4;
			NodeToken n5;
			Token n6;
			NodeList n7 = new NodeList();
			NodeToken n8;
			Token n9;
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case INTEGER_LITERAL:
				n2 = jj_consume_token(INTEGER_LITERAL);
				n1 = JTBToolkit.makeNodeToken(n2);
				n0 = new NodeChoice(n1, 0);
				break;
			case FLOATING_POINT_LITERAL:
				n4 = jj_consume_token(FLOATING_POINT_LITERAL);
				n3 = JTBToolkit.makeNodeToken(n4);
				n0 = new NodeChoice(n3, 1);
				break;
			case CHARACTER_LITERAL:
				n6 = jj_consume_token(CHARACTER_LITERAL);
				n5 = JTBToolkit.makeNodeToken(n6);
				n0 = new NodeChoice(n5, 2);
				break;
			case STRING_LITERAL:
				label_28: while (true) {
					n9 = jj_consume_token(STRING_LITERAL);
					n8 = JTBToolkit.makeNodeToken(n9);
					n7.addNode(n8);
					switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
					case STRING_LITERAL:
						;
						break;
					default:
						jj_la1[114] = jj_gen;
						break label_28;
					}
				}
				n7.trimToSize();
				n0 = new NodeChoice(n7, 3);
				break;
			default:
				jj_la1[115] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			{

				return new Constant(n0);
			}

		}

		private boolean jj_2_1(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_1();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(0, xla);
			}
		}

		private boolean jj_2_2(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_2();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(1, xla);
			}
		}

		private boolean jj_2_3(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_3();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(2, xla);
			}
		}

		private boolean jj_2_4(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_4();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(3, xla);
			}
		}

		private boolean jj_2_5(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_5();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(4, xla);
			}
		}

		private boolean jj_2_6(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_6();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(5, xla);
			}
		}

		private boolean jj_2_7(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_7();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(6, xla);
			}
		}

		private boolean jj_2_8(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_8();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(7, xla);
			}
		}

		private boolean jj_2_9(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_9();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(8, xla);
			}
		}

		private boolean jj_2_10(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_10();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(9, xla);
			}
		}

		private boolean jj_2_11(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_11();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(10, xla);
			}
		}

		private boolean jj_2_12(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_12();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(11, xla);
			}
		}

		private boolean jj_2_13(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_13();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(12, xla);
			}
		}

		private boolean jj_2_14(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_14();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(13, xla);
			}
		}

		private boolean jj_2_15(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_15();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(14, xla);
			}
		}

		private boolean jj_2_16(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_16();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(15, xla);
			}
		}

		private boolean jj_2_17(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_17();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(16, xla);
			}
		}

		private boolean jj_2_18(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_18();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(17, xla);
			}
		}

		private boolean jj_2_19(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_19();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(18, xla);
			}
		}

		private boolean jj_2_20(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_20();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(19, xla);
			}
		}

		private boolean jj_2_21(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_21();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(20, xla);
			}
		}

		private boolean jj_2_22(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_22();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(21, xla);
			}
		}

		private boolean jj_2_23(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_23();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(22, xla);
			}
		}

		private boolean jj_2_24(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_24();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(23, xla);
			}
		}

		private boolean jj_2_25(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_25();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(24, xla);
			}
		}

		private boolean jj_2_26(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_26();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(25, xla);
			}
		}

		private boolean jj_2_27(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_27();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(26, xla);
			}
		}

		private boolean jj_2_28(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_28();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(27, xla);
			}
		}

		private boolean jj_2_29(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_29();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(28, xla);
			}
		}

		private boolean jj_2_30(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_30();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(29, xla);
			}
		}

		private boolean jj_2_31(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_31();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(30, xla);
			}
		}

		private boolean jj_2_32(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_32();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(31, xla);
			}
		}

		private boolean jj_2_33(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_33();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(32, xla);
			}
		}

		private boolean jj_2_34(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_34();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(33, xla);
			}
		}

		private boolean jj_2_35(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_35();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(34, xla);
			}
		}

		private boolean jj_2_36(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_36();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(35, xla);
			}
		}

		private boolean jj_2_37(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_37();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(36, xla);
			}
		}

		private boolean jj_2_38(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_38();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(37, xla);
			}
		}

		private boolean jj_2_39(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_39();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(38, xla);
			}
		}

		private boolean jj_2_40(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_40();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(39, xla);
			}
		}

		private boolean jj_2_41(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_41();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(40, xla);
			}
		}

		private boolean jj_2_42(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_42();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(41, xla);
			}
		}

		private boolean jj_2_43(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_43();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(42, xla);
			}
		}

		private boolean jj_2_44(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_44();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(43, xla);
			}
		}

		private boolean jj_2_45(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_45();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(44, xla);
			}
		}

		private boolean jj_2_46(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_46();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(45, xla);
			}
		}

		private boolean jj_2_47(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_47();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(46, xla);
			}
		}

		private boolean jj_2_48(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_48();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(47, xla);
			}
		}

		private boolean jj_2_49(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_49();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(48, xla);
			}
		}

		private boolean jj_2_50(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_50();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(49, xla);
			}
		}

		private boolean jj_2_51(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_51();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(50, xla);
			}
		}

		private boolean jj_2_52(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_52();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(51, xla);
			}
		}

		private boolean jj_2_53(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_53();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(52, xla);
			}
		}

		private boolean jj_2_54(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_54();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(53, xla);
			}
		}

		private boolean jj_2_55(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_55();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(54, xla);
			}
		}

		private boolean jj_2_56(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_56();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(55, xla);
			}
		}

		private boolean jj_2_57(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_57();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(56, xla);
			}
		}

		private boolean jj_2_58(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_58();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(57, xla);
			}
		}

		private boolean jj_2_59(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_59();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(58, xla);
			}
		}

		private boolean jj_2_60(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_60();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(59, xla);
			}
		}

		private boolean jj_2_61(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_61();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(60, xla);
			}
		}

		private boolean jj_2_62(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_62();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(61, xla);
			}
		}

		private boolean jj_2_63(int xla) {
			jj_la = xla;
			jj_lastpos = jj_scanpos = token;
			try {
				return !jj_3_63();
			} catch (LookaheadSuccess ls) {
				return true;
			} finally {
				jj_save(62, xla);
			}
		}

		private boolean jj_3R_127() {
			if (jj_3R_187()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_126() {
			if (jj_3R_186()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_67() {
			if (jj_scan_token(DFLT)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			if (jj_scan_token(SHARED)) {
				return true;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_125() {
			if (jj_3R_185()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_382() {
			if (jj_3R_392()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_377() {
			if (jj_scan_token(COMMA)) {
				return true;
			}
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_287() {
			if (jj_3R_304()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_50() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_125()) {
				jj_scanpos = xsp;
				if (jj_3R_126()) {
					jj_scanpos = xsp;
					if (jj_3R_127()) {
						return true;
					}
				}
			}
			return false;
		}

		private boolean jj_3R_381() {
			if (jj_3R_391()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_286() {
			if (jj_3R_303()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_369() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_377()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_228() {
			if (jj_3R_257()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_373() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_381()) {
				jj_scanpos = xsp;
				if (jj_3R_382()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_285() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_57() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(SINGLE)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_12() {
			if (jj_3R_39()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_251() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_285()) {
				jj_scanpos = xsp;
				if (jj_3R_286()) {
					jj_scanpos = xsp;
					if (jj_3R_287()) {
						return true;
					}
				}
			}
			return false;
		}

		private boolean jj_3R_353() {
			if (jj_scan_token(COPYIN)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_364() {
			if (jj_3R_373()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_170() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3_12()) {
				jj_scanpos = xsp;
				if (jj_3R_228()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_368() {
			if (jj_3R_124()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_338() {
			if (jj_3R_363()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_364()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_343() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_368()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_360() {
			if (jj_scan_token(OP_DEREF)) {
				return true;
			}
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_20() {
			if (jj_3R_46()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_227() {
			if (jj_scan_token(COMPLEX)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_226() {
			if (jj_scan_token(EXTENSION)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_312() {
			if (jj_3R_343()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_352() {
			if (jj_scan_token(SHARED)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_225() {
			if (jj_scan_token(CATOMIC)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_339() {
			if (jj_scan_token(OP_BITAND)) {
				return true;
			}
			if (jj_3R_306()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_311() {
			if (jj_3R_46()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_359() {
			if (jj_scan_token(DOT)) {
				return true;
			}
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_224() {
			if (jj_scan_token(CSIGNED2)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_306() {
			if (jj_3R_338()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_339()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_292() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_311()) {
				jj_scanpos = xsp;
				if (jj_3R_312()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_223() {
			if (jj_scan_token(CSIGNED)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_222() {
			if (jj_scan_token(CINLINED2)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_53() {
			if (jj_3R_79()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_63() {
			if (jj_3R_88()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_221() {
			if (jj_scan_token(CINLINED)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_220() {
			if (jj_scan_token(CCONST)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_219() {
			if (jj_scan_token(INLINE)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_370() {
			if (jj_3R_88()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_351() {
			if (jj_scan_token(LASTPRIVATE)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_218() {
			if (jj_scan_token(VOLATILE)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_45() {
			if (jj_3R_35()) {
				return true;
			}
			if (jj_3R_292()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_358() {
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_370()) {
				jj_scanpos = xsp;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_217() {
			if (jj_scan_token(CONST)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_307() {
			if (jj_scan_token(OP_BITXOR)) {
				return true;
			}
			if (jj_3R_289()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_216() {
			if (jj_scan_token(RESTRICT)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_289() {
			if (jj_3R_306()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_307()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_169() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_216()) {
				jj_scanpos = xsp;
				if (jj_3R_217()) {
					jj_scanpos = xsp;
					if (jj_3R_218()) {
						jj_scanpos = xsp;
						if (jj_3R_219()) {
							jj_scanpos = xsp;
							if (jj_3R_220()) {
								jj_scanpos = xsp;
								if (jj_3R_221()) {
									jj_scanpos = xsp;
									if (jj_3R_222()) {
										jj_scanpos = xsp;
										if (jj_3R_223()) {
											jj_scanpos = xsp;
											if (jj_3R_224()) {
												jj_scanpos = xsp;
												if (jj_3R_225()) {
													jj_scanpos = xsp;
													if (jj_3R_226()) {
														jj_scanpos = xsp;
														if (jj_3R_227()) {
															return true;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3_19() {
			if (jj_scan_token(COMMA)) {
				return true;
			}
			if (jj_3R_45()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_350() {
			if (jj_scan_token(FIRSTPRIVATE)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_357() {
			if (jj_scan_token(LEFTBRACKET)) {
				return true;
			}
			if (jj_3R_206()) {
				return true;
			}
			if (jj_scan_token(RIGHTBRACKET)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_117() {
			if (jj_3R_45()) {
				return true;
			}
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3_19()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_290() {
			if (jj_scan_token(OP_BITOR)) {
				return true;
			}
			if (jj_3R_253()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_253() {
			if (jj_3R_289()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_290()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_362() {
			if (jj_scan_token(OP_DECR)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_349() {
			if (jj_scan_token(PRIVATE)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_376() {
			if (jj_scan_token(COMMA)) {
				return true;
			}
			if (jj_scan_token(ELLIPSIS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_44() {
			if (jj_3R_117()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_376()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_361() {
			if (jj_scan_token(OP_INCR)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_109() {
			if (jj_3R_172()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_56() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(SECTIONS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_108() {
			if (jj_3R_171()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_254() {
			if (jj_scan_token(OP_AND)) {
				return true;
			}
			if (jj_3R_204()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_41() {
			if (jj_3R_67()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_107() {
			if (jj_3R_170()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_327() {
			if (jj_3R_355()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_106() {
			if (jj_scan_token(UNSIGNED)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_204() {
			if (jj_3R_253()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_254()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_326() {
			if (jj_3R_354()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_337() {
			if (jj_3R_362()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_105() {
			if (jj_scan_token(SIGNED)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_325() {
			if (jj_3R_67()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_336() {
			if (jj_3R_361()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_261() {
			if (jj_3R_169()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_104() {
			if (jj_scan_token(DOUBLE)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_324() {
			if (jj_3R_353()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_335() {
			if (jj_3R_360()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_103() {
			if (jj_scan_token(FLOAT)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_233() {
			Token xsp;
			if (jj_3R_261()) {
				return true;
			}
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_261()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_323() {
			if (jj_3R_352()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_334() {
			if (jj_3R_359()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_102() {
			if (jj_scan_token(LONG)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_322() {
			if (jj_3R_351()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_333() {
			if (jj_3R_358()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_101() {
			if (jj_scan_token(INT)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_80() {
			if (jj_scan_token(INITIALIZER)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(OP_ASS)) {
				return true;
			}
			if (jj_3R_47()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_321() {
			if (jj_3R_350()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_332() {
			if (jj_3R_357()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_100() {
			if (jj_scan_token(SHORT)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_320() {
			if (jj_3R_349()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_205() {
			if (jj_scan_token(OP_OR)) {
				return true;
			}
			if (jj_3R_159()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_99() {
			if (jj_scan_token(CHAR)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_305() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_332()) {
				jj_scanpos = xsp;
				if (jj_3R_333()) {
					jj_scanpos = xsp;
					if (jj_3R_334()) {
						jj_scanpos = xsp;
						if (jj_3R_335()) {
							jj_scanpos = xsp;
							if (jj_3R_336()) {
								jj_scanpos = xsp;
								if (jj_3R_337()) {
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_182() {
			if (jj_3R_122()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_298() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_320()) {
				jj_scanpos = xsp;
				if (jj_3R_321()) {
					jj_scanpos = xsp;
					if (jj_3R_322()) {
						jj_scanpos = xsp;
						if (jj_3R_323()) {
							jj_scanpos = xsp;
							if (jj_3R_324()) {
								jj_scanpos = xsp;
								if (jj_3R_325()) {
									jj_scanpos = xsp;
									if (jj_3R_326()) {
										jj_scanpos = xsp;
										if (jj_3R_327()) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_98() {
			if (jj_scan_token(VOID)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_159() {
			if (jj_3R_204()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_205()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_181() {
			if (jj_3R_233()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_180() {
			if (jj_scan_token(OP_BITXOR)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_38() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_98()) {
				jj_scanpos = xsp;
				if (jj_3R_99()) {
					jj_scanpos = xsp;
					if (jj_3R_100()) {
						jj_scanpos = xsp;
						if (jj_3R_101()) {
							jj_scanpos = xsp;
							if (jj_3R_102()) {
								jj_scanpos = xsp;
								if (jj_3R_103()) {
									jj_scanpos = xsp;
									if (jj_3R_104()) {
										jj_scanpos = xsp;
										if (jj_3R_105()) {
											jj_scanpos = xsp;
											if (jj_3R_106()) {
												jj_scanpos = xsp;
												if (jj_3R_107()) {
													jj_scanpos = xsp;
													if (jj_3R_108()) {
														jj_scanpos = xsp;
														jj_lookingAhead = true;
														jj_semLA = (!typedefParsingStack.empty()
																&& typedefParsingStack.peek().booleanValue()
																&& flag == true)
																|| ((typedefParsingStack.empty()
																		|| !typedefParsingStack.peek().booleanValue())
																		&& isType(getToken(1).image));
														jj_lookingAhead = false;
														if (!jj_semLA || jj_3R_109()) {
															return true;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_179() {
			if (jj_scan_token(OP_MUL)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_78() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(OP_ASS)) {
				return true;
			}
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(OP_SUB)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_288() {
			if (jj_3R_305()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_122() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_179()) {
				jj_scanpos = xsp;
				if (jj_3R_180()) {
					return true;
				}
			}
			xsp = jj_scanpos;
			if (jj_3R_181()) {
				jj_scanpos = xsp;
			}
			xsp = jj_scanpos;
			if (jj_3R_182()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3_55() {
			if (jj_3R_80()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_235() {
			if (jj_3R_84()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_252() {
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_288()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_346() {
			if (jj_scan_token(NUM_THREADS)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_54() {
			if (jj_3R_38()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_203() {
			if (jj_3R_251()) {
				return true;
			}
			if (jj_3R_252()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_160() {
			if (jj_scan_token(QUESTION)) {
				return true;
			}
			if (jj_3R_206()) {
				return true;
			}
			if (jj_scan_token(COLON)) {
				return true;
			}
			if (jj_3R_84()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_77() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(OP_ASS)) {
				return true;
			}
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(OP_ADD)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_231() {
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			if (jj_3R_46()) {
				return true;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_84() {
			if (jj_3R_159()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_160()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_345() {
			if (jj_scan_token(IF)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_284() {
			if (jj_scan_token(OP_NOT)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_215() {
			if (jj_scan_token(TYPEDEF)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_283() {
			if (jj_scan_token(OP_BITNOT)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_214() {
			if (jj_scan_token(EXTERN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_282() {
			if (jj_scan_token(OP_SUB)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_213() {
			if (jj_scan_token(STATIC)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_230() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_281() {
			if (jj_scan_token(OP_ADD)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_212() {
			if (jj_scan_token(REGISTER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_176() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_230()) {
				jj_scanpos = xsp;
				if (jj_3R_231()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_280() {
			if (jj_scan_token(OP_MUL)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_211() {
			if (jj_scan_token(AUTO)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_76() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(OP_SUBASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_316() {
			if (jj_3R_346()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_279() {
			if (jj_scan_token(OP_BITAND)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_158() {
			if (jj_scan_token(OP_ORASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_168() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_211()) {
				jj_scanpos = xsp;
				if (jj_3R_212()) {
					jj_scanpos = xsp;
					if (jj_3R_213()) {
						jj_scanpos = xsp;
						if (jj_3R_214()) {
							jj_scanpos = xsp;
							if (jj_3R_215()) {
								return true;
							}
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_315() {
			if (jj_3R_345()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_157() {
			if (jj_scan_token(OP_XORASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_34() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(DECLARE)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_250() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_279()) {
				jj_scanpos = xsp;
				if (jj_3R_280()) {
					jj_scanpos = xsp;
					if (jj_3R_281()) {
						jj_scanpos = xsp;
						if (jj_3R_282()) {
							jj_scanpos = xsp;
							if (jj_3R_283()) {
								jj_scanpos = xsp;
								if (jj_3R_284()) {
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_156() {
			if (jj_scan_token(OP_ANDASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_296() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_315()) {
				jj_scanpos = xsp;
				if (jj_3R_316()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_155() {
			if (jj_scan_token(OP_SRASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_154() {
			if (jj_scan_token(OP_SLASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_344() {
			if (jj_3R_369()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_153() {
			if (jj_scan_token(OP_SUBASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_75() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(OP_ADDASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_152() {
			if (jj_scan_token(OP_ADDASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_313() {
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_344()) {
				jj_scanpos = xsp;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_268() {
			if (jj_3R_298()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_151() {
			if (jj_scan_token(OP_MODASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_97() {
			if (jj_3R_169()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_267() {
			if (jj_3R_296()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_150() {
			if (jj_scan_token(OP_DIVASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_11() {
			if (jj_3R_38()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_237() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_267()) {
				jj_scanpos = xsp;
				if (jj_3R_268()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_149() {
			if (jj_scan_token(OP_MULASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_96() {
			if (jj_3R_168()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_148() {
			if (jj_scan_token(OP_ASS)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_87() {
			if (jj_scan_token(SIZEOF)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			if (jj_3R_85()) {
				return true;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_37() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_96()) {
				jj_scanpos = xsp;
				if (jj_3_11()) {
					jj_scanpos = xsp;
					if (jj_3R_97()) {
						return true;
					}
				}
			}
			return false;
		}

		private boolean jj_3R_74() {
			if (jj_scan_token(OP_DECR)) {
				return true;
			}
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_83() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_148()) {
				jj_scanpos = xsp;
				if (jj_3R_149()) {
					jj_scanpos = xsp;
					if (jj_3R_150()) {
						jj_scanpos = xsp;
						if (jj_3R_151()) {
							jj_scanpos = xsp;
							if (jj_3R_152()) {
								jj_scanpos = xsp;
								if (jj_3R_153()) {
									jj_scanpos = xsp;
									if (jj_3R_154()) {
										jj_scanpos = xsp;
										if (jj_3R_155()) {
											jj_scanpos = xsp;
											if (jj_3R_156()) {
												jj_scanpos = xsp;
												if (jj_3R_157()) {
													jj_scanpos = xsp;
													if (jj_3R_158()) {
														return true;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3_18() {
			if (jj_3R_44()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_166() {
			if (jj_3R_94()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_193() {
			if (jj_3R_237()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_43() {
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3_18()) {
				jj_scanpos = xsp;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_134() {
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_193()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3_10() {
			if (jj_3R_37()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_278() {
			if (jj_scan_token(SIZEOF)) {
				return true;
			}
			if (jj_3R_82()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_73() {
			if (jj_scan_token(OP_INCR)) {
				return true;
			}
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_35() {
			Token xsp;
			if (jj_3_10()) {
				return true;
			}
			while (true) {
				xsp = jj_scanpos;
				if (jj_3_10()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3_9() {
			if (jj_3R_32()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_62() {
			if (jj_3R_87()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_314() {
			if (jj_3R_235()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_130() {
			if (jj_scan_token(PARALLEL)) {
				return true;
			}
			if (jj_3R_134()) {
				return true;
			}
			if (jj_3R_133()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_66() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(TASKYIELD)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_249() {
			if (jj_3R_278()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_295() {
			if (jj_scan_token(LEFTBRACKET)) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_314()) {
				jj_scanpos = xsp;
			}
			if (jj_scan_token(RIGHTBRACKET)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_95() {
			if (jj_3R_32()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_72() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(OP_DECR)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_248() {
			if (jj_3R_87()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_36() {
			Token xsp;
			if (jj_3R_95()) {
				return true;
			}
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_95()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_255() {
			if (jj_3R_82()) {
				return true;
			}
			if (jj_3R_83()) {
				return true;
			}
			if (jj_3R_164()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_201() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_248()) {
				jj_scanpos = xsp;
				if (jj_3R_249()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_51() {
			if (jj_scan_token(CROSSBAR)) {
				return true;
			}
			if (jj_scan_token(PRAGMA)) {
				return true;
			}
			if (jj_scan_token(UNKNOWN_CPP)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_65() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(TASKWAIT)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_59() {
			if (jj_3R_82()) {
				return true;
			}
			if (jj_3R_83()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_294() {
			if (jj_3R_313()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_92() {
			if (jj_3R_167()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_71() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(OP_INCR)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_17() {
			if (jj_3R_43()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_60() {
			if (jj_3R_84()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_202() {
			if (jj_3R_250()) {
				return true;
			}
			if (jj_3R_86()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_293() {
			if (jj_3R_295()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_32() {
			if (jj_3R_35()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_92()) {
				jj_scanpos = xsp;
			}
			if (jj_scan_token(SEMICOLON)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_208() {
			if (jj_3R_255()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_260() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_293()) {
				jj_scanpos = xsp;
				if (jj_3_17()) {
					jj_scanpos = xsp;
					if (jj_3R_294()) {
						return true;
					}
				}
			}
			return false;
		}

		private boolean jj_3R_164() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_208()) {
				jj_scanpos = xsp;
				if (jj_3_60()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_64() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(BARRIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_94() {
			if (jj_scan_token(CROSSBAR)) {
				return true;
			}
			if (jj_scan_token(PRAGMA)) {
				return true;
			}
			if (jj_scan_token(OMP)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_7() {
			if (jj_3R_35()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_52() {
			if (jj_3R_78()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_200() {
			if (jj_scan_token(OP_DECR)) {
				return true;
			}
			if (jj_3R_82()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_8() {
			if (jj_3R_36()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_51() {
			if (jj_3R_77()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_48() {
			if (jj_3R_74()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_50() {
			if (jj_3R_76()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_93() {
			if (jj_3R_35()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_299() {
			if (jj_scan_token(COMMA)) {
				return true;
			}
			if (jj_3R_164()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_47() {
			if (jj_3R_73()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_232() {
			if (jj_3R_260()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_49() {
			if (jj_3R_75()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_33() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_93()) {
				jj_scanpos = xsp;
			}
			if (jj_3R_46()) {
				return true;
			}
			xsp = jj_scanpos;
			if (jj_3_8()) {
				jj_scanpos = xsp;
			}
			if (jj_3R_195()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_188() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(ORDERED)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_46() {
			if (jj_3R_72()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_54() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_3R_130()) {
				return true;
			}
			if (jj_3R_79()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_206() {
			if (jj_3R_164()) {
				return true;
			}
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_299()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_199() {
			if (jj_scan_token(OP_INCR)) {
				return true;
			}
			if (jj_3R_82()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_45() {
			if (jj_3R_71()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_177() {
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_232()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_129() {
			if (jj_3R_189()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_147() {
			if (jj_3R_203()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_91() {
			if (jj_3R_166()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_4() {
			if (jj_3R_32()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_146() {
			if (jj_3R_202()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_302() {
			if (jj_3R_206()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_40() {
			if (jj_3R_66()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_6() {
			if (jj_3R_34()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_119() {
			if (jj_3R_176()) {
				return true;
			}
			if (jj_3R_177()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_145() {
			if (jj_3R_201()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_5() {
			if (jj_3R_33()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_39() {
			if (jj_3R_65()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_277() {
			if (jj_scan_token(RETURN)) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_302()) {
				jj_scanpos = xsp;
			}
			if (jj_scan_token(SEMICOLON)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_144() {
			if (jj_3R_200()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_90() {
			if (jj_3R_32()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_38() {
			if (jj_3R_64()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_143() {
			if (jj_3R_199()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_30() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_90()) {
				jj_scanpos = xsp;
				if (jj_3_5()) {
					jj_scanpos = xsp;
					if (jj_3_6()) {
						jj_scanpos = xsp;
						if (jj_3R_91()) {
							return true;
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_53() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3_38()) {
				jj_scanpos = xsp;
				if (jj_3_39()) {
					jj_scanpos = xsp;
					if (jj_3_40()) {
						jj_scanpos = xsp;
						if (jj_3R_129()) {
							return true;
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_82() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_143()) {
				jj_scanpos = xsp;
				if (jj_3R_144()) {
					jj_scanpos = xsp;
					if (jj_3R_145()) {
						jj_scanpos = xsp;
						if (jj_3R_146()) {
							jj_scanpos = xsp;
							if (jj_3R_147()) {
								return true;
							}
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_118() {
			if (jj_3R_122()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_46() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_118()) {
				jj_scanpos = xsp;
			}
			if (jj_3R_119()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_189() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(FLUSH)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_276() {
			if (jj_scan_token(BREAK)) {
				return true;
			}
			if (jj_scan_token(SEMICOLON)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_89() {
			if (jj_3R_51()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_128() {
			if (jj_3R_188()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_3() {
			if (jj_3R_31()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_37() {
			if (jj_3R_63()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_207() {
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			if (jj_3R_85()) {
				return true;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			if (jj_3R_86()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_36() {
			if (jj_3R_62()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_2() {
			if (jj_3R_30()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_393() {
			if (jj_scan_token(OP_ASS)) {
				return true;
			}
			if (jj_3R_235()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_275() {
			if (jj_scan_token(CONTINUE)) {
				return true;
			}
			if (jj_scan_token(SEMICOLON)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_29() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3_2()) {
				jj_scanpos = xsp;
				if (jj_3_3()) {
					jj_scanpos = xsp;
					if (jj_3R_89()) {
						return true;
					}
				}
			}
			return false;
		}

		private boolean jj_3_35() {
			if (jj_3R_61()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_175() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_393()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_70() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(OP_GT)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_61() {
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			if (jj_3R_85()) {
				return true;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			if (jj_3R_86()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_34() {
			if (jj_3R_60()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_33() {
			if (jj_3R_59()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_163() {
			if (jj_3R_82()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_32() {
			if (jj_3R_58()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_162() {
			if (jj_3R_207()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_1() {
			if (jj_3R_29()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_31() {
			if (jj_3R_57()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_274() {
			if (jj_scan_token(GOTO)) {
				return true;
			}
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(SEMICOLON)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_86() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_162()) {
				jj_scanpos = xsp;
				if (jj_3R_163()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3_30() {
			if (jj_3R_56()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_69() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(OP_LE)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_383() {
			if (jj_scan_token(COMMA)) {
				return true;
			}
			if (jj_3R_175()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_29() {
			if (jj_3R_55()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_28() {
			if (jj_3R_54()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_116() {
			if (jj_3R_175()) {
				return true;
			}
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_383()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_415() {
			if (jj_scan_token(OP_MOD)) {
				return true;
			}
			if (jj_3R_384()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_52() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3_28()) {
				jj_scanpos = xsp;
				if (jj_3_29()) {
					jj_scanpos = xsp;
					if (jj_3_30()) {
						jj_scanpos = xsp;
						if (jj_3_31()) {
							jj_scanpos = xsp;
							if (jj_3_32()) {
								jj_scanpos = xsp;
								if (jj_3_33()) {
									jj_scanpos = xsp;
									if (jj_3_34()) {
										jj_scanpos = xsp;
										if (jj_3_35()) {
											jj_scanpos = xsp;
											if (jj_3_36()) {
												jj_scanpos = xsp;
												if (jj_3_37()) {
													jj_scanpos = xsp;
													if (jj_3R_128()) {
														return true;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_247() {
			if (jj_3R_277()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_246() {
			if (jj_3R_276()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_68() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(OP_LT)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_63() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(ATOMIC)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_245() {
			if (jj_3R_275()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_244() {
			if (jj_3R_274()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_198() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_244()) {
				jj_scanpos = xsp;
				if (jj_3R_245()) {
					jj_scanpos = xsp;
					if (jj_3R_246()) {
						jj_scanpos = xsp;
						if (jj_3R_247()) {
							return true;
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_414() {
			if (jj_scan_token(OP_DIV)) {
				return true;
			}
			if (jj_3R_384()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_258() {
			if (jj_scan_token(ENUM)) {
				return true;
			}
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_192() {
			if (jj_scan_token(OMP_NL)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_191() {
			if (jj_scan_token(OMP_CR)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_44() {
			if (jj_3R_70()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_133() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_191()) {
				jj_scanpos = xsp;
				if (jj_3R_192()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3_43() {
			if (jj_3R_69()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_413() {
			if (jj_scan_token(OP_MUL)) {
				return true;
			}
			if (jj_3R_384()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_115() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_42() {
			if (jj_3R_68()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_301() {
			if (jj_3R_206()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_42() {
			if (jj_scan_token(ENUM)) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_115()) {
				jj_scanpos = xsp;
			}
			if (jj_scan_token(LEFTBRACE)) {
				return true;
			}
			if (jj_3R_116()) {
				return true;
			}
			if (jj_scan_token(RIGHTBRACE)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_300() {
			if (jj_3R_206()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_410() {
			if (jj_3R_415()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_409() {
			if (jj_3R_414()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_31() {
			if (jj_scan_token(CROSSBAR)) {
				return true;
			}
			if (jj_scan_token(UNKNOWN_CPP)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_273() {
			if (jj_scan_token(FOR)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_300()) {
				jj_scanpos = xsp;
			}
			if (jj_scan_token(SEMICOLON)) {
				return true;
			}
			xsp = jj_scanpos;
			if (jj_3R_301()) {
				jj_scanpos = xsp;
			}
			if (jj_scan_token(SEMICOLON)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_408() {
			if (jj_3R_413()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_62() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(CRITICAL)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_402() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_408()) {
				jj_scanpos = xsp;
				if (jj_3R_409()) {
					jj_scanpos = xsp;
					if (jj_3R_410()) {
						return true;
					}
				}
			}
			return false;
		}

		private boolean jj_3R_229() {
			if (jj_3R_258()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_140() {
			if (jj_3R_31()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_16() {
			if (jj_3R_42()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_27() {
			if (jj_3R_53()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_171() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3_16()) {
				jj_scanpos = xsp;
				if (jj_3R_229()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_394() {
			if (jj_3R_402()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_26() {
			if (jj_3R_52()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_61() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(MASTER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_384() {
			if (jj_3R_86()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_394()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3_25() {
			if (jj_3R_51()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_139() {
			if (jj_3R_198()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_138() {
			if (jj_3R_197()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_342() {
			if (jj_scan_token(COLON)) {
				return true;
			}
			if (jj_3R_235()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_272() {
			if (jj_scan_token(DO)) {
				return true;
			}
			if (jj_3R_79()) {
				return true;
			}
			if (jj_scan_token(WHILE)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_137() {
			if (jj_3R_196()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_136() {
			if (jj_3R_195()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_412() {
			if (jj_scan_token(OP_SUB)) {
				return true;
			}
			if (jj_3R_378()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_135() {
			if (jj_3R_194()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_24() {
			if (jj_3R_50()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_59() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(PARALLEL)) {
				return true;
			}
			if (jj_scan_token(SECTIONS)) {
				return true;
			}
			if (jj_3R_134()) {
				return true;
			}
			if (jj_3R_133()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_79() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3_24()) {
				jj_scanpos = xsp;
				if (jj_3R_135()) {
					jj_scanpos = xsp;
					if (jj_3R_136()) {
						jj_scanpos = xsp;
						if (jj_3R_137()) {
							jj_scanpos = xsp;
							if (jj_3R_138()) {
								jj_scanpos = xsp;
								if (jj_3R_139()) {
									jj_scanpos = xsp;
									if (jj_3_25()) {
										jj_scanpos = xsp;
										if (jj_3_26()) {
											jj_scanpos = xsp;
											if (jj_3_27()) {
												jj_scanpos = xsp;
												if (jj_3R_140()) {
													return true;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_416() {
			if (jj_scan_token(COLON)) {
				return true;
			}
			if (jj_3R_235()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_411() {
			if (jj_scan_token(OP_ADD)) {
				return true;
			}
			if (jj_3R_378()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_341() {
			if (jj_3R_46()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_416()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_271() {
			if (jj_scan_token(WHILE)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			if (jj_3R_206()) {
				return true;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_266() {
			if (jj_3R_298()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_265() {
			if (jj_3R_297()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_404() {
			if (jj_3R_412()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_264() {
			if (jj_3R_296()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_172() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_403() {
			if (jj_3R_411()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_310() {
			if (jj_3R_342()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_236() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_264()) {
				jj_scanpos = xsp;
				if (jj_3R_265()) {
					jj_scanpos = xsp;
					if (jj_3R_266()) {
						return true;
					}
				}
			}
			return false;
		}

		private boolean jj_3R_309() {
			if (jj_3R_341()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_395() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_403()) {
				jj_scanpos = xsp;
				if (jj_3R_404()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_291() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_309()) {
				jj_scanpos = xsp;
				if (jj_3R_310()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_243() {
			if (jj_3R_273()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_375() {
			if (jj_3R_43()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_242() {
			if (jj_3R_272()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_374() {
			if (jj_3R_295()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_241() {
			if (jj_3R_271()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_385() {
			if (jj_3R_395()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_190() {
			if (jj_3R_236()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_365() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_374()) {
				jj_scanpos = xsp;
				if (jj_3R_375()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_197() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_241()) {
				jj_scanpos = xsp;
				if (jj_3R_242()) {
					jj_scanpos = xsp;
					if (jj_3R_243()) {
						return true;
					}
				}
			}
			return false;
		}

		private boolean jj_3R_132() {
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_190()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_378() {
			if (jj_3R_384()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_385()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_407() {
			if (jj_scan_token(COMMA)) {
				return true;
			}
			if (jj_3R_291()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_347() {
			if (jj_scan_token(SCHEDULE)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_259() {
			if (jj_3R_291()) {
				return true;
			}
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_407()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_340() {
			if (jj_3R_365()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_308() {
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_340()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_406() {
			if (jj_scan_token(OP_SL)) {
				return true;
			}
			if (jj_3R_371()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_270() {
			if (jj_scan_token(SWITCH)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			if (jj_3R_206()) {
				return true;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_58() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(PARALLEL)) {
				return true;
			}
			if (jj_scan_token(FOR)) {
				return true;
			}
			if (jj_3R_132()) {
				return true;
			}
			if (jj_3R_133()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_114() {
			if (jj_3R_169()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_15() {
			if (jj_3R_38()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_49() {
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			if (jj_3R_124()) {
				return true;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_405() {
			if (jj_scan_token(OP_SR)) {
				return true;
			}
			if (jj_3R_371()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_348() {
			if (jj_scan_token(COLLAPSE)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_41() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3_15()) {
				jj_scanpos = xsp;
				if (jj_3R_114()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3_58() {
			if (jj_scan_token(ELSE)) {
				return true;
			}
			if (jj_3R_79()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_397() {
			if (jj_3R_406()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_263() {
			if (jj_3R_43()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_14() {
			if (jj_3R_41()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_396() {
			if (jj_3R_405()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_269() {
			if (jj_scan_token(IF)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			if (jj_3R_206()) {
				return true;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_262() {
			if (jj_3R_295()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_319() {
			if (jj_3R_348()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_113() {
			Token xsp;
			if (jj_3_14()) {
				return true;
			}
			while (true) {
				xsp = jj_scanpos;
				if (jj_3_14()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_386() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_396()) {
				jj_scanpos = xsp;
				if (jj_3R_397()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_318() {
			if (jj_3R_347()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_23() {
			if (jj_3R_49()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_317() {
			if (jj_scan_token(ORDERED)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_234() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3_23()) {
				jj_scanpos = xsp;
				if (jj_3R_262()) {
					jj_scanpos = xsp;
					if (jj_3R_263()) {
						return true;
					}
				}
			}
			return false;
		}

		private boolean jj_3R_297() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_317()) {
				jj_scanpos = xsp;
				if (jj_3R_318()) {
					jj_scanpos = xsp;
					if (jj_3R_319()) {
						return true;
					}
				}
			}
			return false;
		}

		private boolean jj_3R_379() {
			if (jj_3R_386()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_40() {
			if (jj_3R_113()) {
				return true;
			}
			if (jj_3R_259()) {
				return true;
			}
			if (jj_scan_token(SEMICOLON)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_371() {
			if (jj_3R_378()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_379()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_240() {
			if (jj_3R_270()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_183() {
			if (jj_3R_234()) {
				return true;
			}
			if (jj_3R_308()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_239() {
			if (jj_3R_269()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_196() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_239()) {
				jj_scanpos = xsp;
				if (jj_3R_240()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_256() {
			if (jj_scan_token(OP_ASS)) {
				return true;
			}
			if (jj_3R_47()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_401() {
			if (jj_scan_token(OP_GE)) {
				return true;
			}
			if (jj_3R_363()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_123() {
			if (jj_3R_183()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_57() {
			if (jj_3R_32()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_209() {
			if (jj_3R_46()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_256()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_48() {
			if (jj_3R_122()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_123()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_142() {
			if (jj_3R_79()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_141() {
			if (jj_3R_32()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_400() {
			if (jj_scan_token(OP_LE)) {
				return true;
			}
			if (jj_3R_363()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_81() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_141()) {
				jj_scanpos = xsp;
				if (jj_3R_142()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_184() {
			if (jj_3R_183()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_22() {
			if (jj_3R_48()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_124() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3_22()) {
				jj_scanpos = xsp;
				if (jj_3R_184()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_399() {
			if (jj_scan_token(OP_GT)) {
				return true;
			}
			if (jj_3R_363()) {
				return true;
			}
			return false;
		}

		private boolean jj_3_56() {
			if (jj_3R_81()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_210() {
			if (jj_scan_token(COMMA)) {
				return true;
			}
			if (jj_3R_209()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_195() {
			if (jj_scan_token(LEFTBRACE)) {
				return true;
			}
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3_56()) {
					jj_scanpos = xsp;
					break;
				}
			}
			if (jj_scan_token(RIGHTBRACE)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_167() {
			if (jj_3R_209()) {
				return true;
			}
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_210()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_161() {
			if (jj_3R_124()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_85() {
			if (jj_3R_113()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_161()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_398() {
			if (jj_scan_token(OP_LT)) {
				return true;
			}
			if (jj_3R_363()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_131() {
			if (jj_scan_token(FOR)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_238() {
			if (jj_3R_206()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_356() {
			if (jj_scan_token(STRING_LITERAL)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_13() {
			if (jj_3R_40()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_194() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_238()) {
				jj_scanpos = xsp;
			}
			if (jj_scan_token(SEMICOLON)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_390() {
			if (jj_3R_401()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_331() {
			Token xsp;
			if (jj_3R_356()) {
				return true;
			}
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_356()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_60() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_scan_token(TASK)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_112() {
			Token xsp;
			if (jj_3_13()) {
				return true;
			}
			while (true) {
				xsp = jj_scanpos;
				if (jj_3_13()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_389() {
			if (jj_3R_400()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_330() {
			if (jj_scan_token(CHARACTER_LITERAL)) {
				return true;
			}
			return false;
		}

		private boolean jj_3_21() {
			if (jj_scan_token(COMMA)) {
				return true;
			}
			if (jj_3R_47()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_388() {
			if (jj_3R_399()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_329() {
			if (jj_scan_token(FLOATING_POINT_LITERAL)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_55() {
			if (jj_3R_94()) {
				return true;
			}
			if (jj_3R_131()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_387() {
			if (jj_3R_398()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_328() {
			if (jj_scan_token(INTEGER_LITERAL)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_366() {
			if (jj_3R_47()) {
				return true;
			}
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3_21()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_380() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_387()) {
				jj_scanpos = xsp;
				if (jj_3R_388()) {
					jj_scanpos = xsp;
					if (jj_3R_389()) {
						jj_scanpos = xsp;
						if (jj_3R_390()) {
							return true;
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_303() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_328()) {
				jj_scanpos = xsp;
				if (jj_3R_329()) {
					jj_scanpos = xsp;
					if (jj_3R_330()) {
						jj_scanpos = xsp;
						if (jj_3R_331()) {
							return true;
						}
					}
				}
			}
			return false;
		}

		private boolean jj_3R_174() {
			if (jj_scan_token(UNION)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_187() {
			if (jj_scan_token(DFLT)) {
				return true;
			}
			if (jj_scan_token(COLON)) {
				return true;
			}
			if (jj_3R_79()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_173() {
			if (jj_scan_token(STRUCT)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_110() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_173()) {
				jj_scanpos = xsp;
				if (jj_3R_174()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_367() {
			if (jj_scan_token(COMMA)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_355() {
			if (jj_scan_token(REDUCTION)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_372() {
			if (jj_3R_380()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_363() {
			if (jj_3R_371()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_372()) {
				jj_scanpos = xsp;
			}
			return false;
		}

		private boolean jj_3R_178() {
			if (jj_scan_token(LEFTBRACE)) {
				return true;
			}
			if (jj_3R_366()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_367()) {
				jj_scanpos = xsp;
			}
			if (jj_scan_token(RIGHTBRACE)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_186() {
			if (jj_scan_token(CASE)) {
				return true;
			}
			if (jj_3R_235()) {
				return true;
			}
			if (jj_scan_token(COLON)) {
				return true;
			}
			if (jj_3R_79()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_257() {
			if (jj_3R_110()) {
				return true;
			}
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_165() {
			if (jj_scan_token(COMMA)) {
				return true;
			}
			if (jj_3R_164()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_88() {
			if (jj_3R_164()) {
				return true;
			}
			Token xsp;
			while (true) {
				xsp = jj_scanpos;
				if (jj_3R_165()) {
					jj_scanpos = xsp;
					break;
				}
			}
			return false;
		}

		private boolean jj_3R_392() {
			if (jj_scan_token(OP_NEQ)) {
				return true;
			}
			if (jj_3R_338()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_354() {
			if (jj_scan_token(DFLT)) {
				return true;
			}
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_121() {
			if (jj_3R_178()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_111() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_185() {
			if (jj_scan_token(IDENTIFIER)) {
				return true;
			}
			if (jj_scan_token(COLON)) {
				return true;
			}
			if (jj_3R_79()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_120() {
			if (jj_3R_164()) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_39() {
			if (jj_3R_110()) {
				return true;
			}
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_111()) {
				jj_scanpos = xsp;
			}
			if (jj_scan_token(LEFTBRACE)) {
				return true;
			}
			if (jj_3R_112()) {
				return true;
			}
			if (jj_scan_token(RIGHTBRACE)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_47() {
			Token xsp;
			xsp = jj_scanpos;
			if (jj_3R_120()) {
				jj_scanpos = xsp;
				if (jj_3R_121()) {
					return true;
				}
			}
			return false;
		}

		private boolean jj_3R_304() {
			if (jj_scan_token(LEFTPAREN)) {
				return true;
			}
			if (jj_3R_206()) {
				return true;
			}
			if (jj_scan_token(RIGHTPAREN)) {
				return true;
			}
			return false;
		}

		private boolean jj_3R_391() {
			if (jj_scan_token(OP_EQ)) {
				return true;
			}
			if (jj_3R_338()) {
				return true;
			}
			return false;
		}

		/** Generated Token Manager. */
		public CParserTokenManager token_source;
		SimpleCharStream jj_input_stream;
		/** Current token. */
		public Token token;
		/** Next token. */
		public Token jj_nt;
		private int jj_ntk;
		private Token jj_scanpos, jj_lastpos;
		private int jj_la;
		/** Whether we are looking ahead. */
		private boolean jj_lookingAhead = false;
		private boolean jj_semLA;
		private int jj_gen;
		final private int[] jj_la1 = new int[116];
		static private int[] jj_la1_0;
		static private int[] jj_la1_1;
		static private int[] jj_la1_2;
		static private int[] jj_la1_3;
		static private int[] jj_la1_4;
		static private int[] jj_la1_5;
		static {
			jj_la1_init_0();
			jj_la1_init_1();
			jj_la1_init_2();
			jj_la1_init_3();
			jj_la1_init_4();
			jj_la1_init_5();
		}

		private static void jj_la1_init_0() {
			jj_la1_0 = new int[] { 0x0, 0x0, 0x0, 0x4000000, 0xfa800000, 0x4000000, 0x0, 0xfa800000, 0x0, 0x0, 0x0, 0x0,
					0x0, 0xfa800000, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xd100, 0x0, 0x0, 0x0,
					0xfa800000, 0x0, 0xfa800000, 0x0, 0x0, 0x0, 0xd100, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x100d100,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xd100, 0x100d100, 0x0,
					0x0, 0xd100, 0xd100, 0xd100, 0x1000000, 0xd100, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xd100, 0xd100, 0x0, 0x0, 0x0, 0x0, 0xd100, 0x0, 0x8000,
					0xd100, };
		}

		private static void jj_la1_init_1() {
			jj_la1_1 = new int[] { 0x0, 0x0, 0x0, 0x200284, 0xc0004001, 0x200284, 0x4d9a512, 0xc0004001, 0x2100, 0x0,
					0x2100, 0x0, 0x0, 0xc0004001, 0x0, 0x0, 0x0, 0x100000, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
					0x20000000, 0x0, 0x0, 0x0, 0xc0004001, 0x0, 0xc0004001, 0x0, 0x0, 0x0, 0x20000000, 0x0, 0x0, 0x0,
					0x0, 0x0, 0x0, 0x0, 0x3b001860, 0x0, 0x0, 0x0, 0x0, 0x8000008, 0x8000008, 0x8000000, 0x0, 0x8, 0x8,
					0x8, 0x0, 0x0, 0x200, 0x0, 0x0, 0x8, 0x8, 0x0, 0x8, 0x8, 0x8000008, 0x8000008, 0x8000000, 0x8000008,
					0x8000008, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x40008, 0x20000000, 0x3b041868, 0x8000020,
					0x12000800, 0x20000000, 0x20000000, 0x20000000, 0x1001040, 0x20000000, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x20000000, 0x20000000, 0x20000000, 0x0,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x0, };
		}

		private static void jj_la1_init_2() {
			jj_la1_2 = new int[] { 0x0, 0x0, 0x8000000, 0x0, 0x1, 0x0, 0x0, 0x1, 0x0, 0x0, 0x0, 0x0, 0x40000000, 0x1,
					0x0, 0x8000000, 0x0, 0x0, 0x0, 0x0, 0x40000000, 0x8000000, 0x0, 0x0, 0x0, 0x8e600000, 0x0, 0x0,
					0x8000000, 0x1, 0x8000000, 0x1, 0x0, 0x8000000, 0x0, 0x8e600000, 0x0, 0x8000000, 0x0, 0x0, 0x0, 0x0,
					0x0, 0x8e600000, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x8e000060, 0x0,
					0x0, 0x8e600000, 0x8e600000, 0x0, 0x0, 0x8e600000, 0x8e600000, 0x8e600000, 0x0, 0x8e600000, 0x0,
					0x40007f8c, 0x0, 0x40, 0x20, 0x0, 0x0, 0x80000000, 0x20010, 0x20010, 0x18c0000, 0x18c0000, 0x18000,
					0x18000, 0x6000000, 0x6000000, 0x38000000, 0x38000000, 0x8e600000, 0x8e600000, 0x0, 0x8e000000,
					0x700000, 0x700000, 0x0, 0x0, 0x0, 0x0, };
		}

		private static void jj_la1_init_3() {
			jj_la1_3 = new int[] { 0x8000, 0x8000, 0x102, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x4000, 0x0, 0x0,
					0x4000, 0x112, 0x10, 0x0, 0x0, 0x4000, 0x0, 0x2, 0x500, 0x400, 0x100, 0x10c, 0x0, 0x100, 0x2, 0x0,
					0x2, 0x0, 0x4000, 0x502, 0x4000, 0x110c, 0x4000, 0x502, 0x500, 0x500, 0x500, 0x500, 0x500, 0x112c,
					0x8000, 0x0, 0x8000, 0x8000, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x4000, 0x0, 0x0, 0x0, 0x0,
					0x0, 0x8000, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x100, 0x0, 0x0, 0x100, 0x0, 0x0, 0x3, 0x4000, 0x0,
					0x10c, 0x912c, 0x0, 0x0, 0x10c, 0x10c, 0x10c, 0x0, 0x10c, 0x4000, 0x0, 0x40, 0x0, 0x0, 0x1, 0x2,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x10c, 0x10c, 0x0, 0xc, 0x580, 0x580, 0x100,
					0x4000, 0x0, 0x0, };
		}

		private static void jj_la1_init_4() {
			jj_la1_4 = new int[] { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0x0, 0x0, 0xc0000000, 0xc0000000, 0x0, 0x80000000,
					0x40000000, 0xc3040000, 0xc3040000, 0x2040000, 0x0, 0x1c000000, 0x0, 0x0, 0xc1000000, 0xc1000000,
					0x0, 0xc1000000, 0xc1000000, 0xc0000000, 0xc0000000, 0x0, 0xc2040000, 0xc2040000, 0x0, 0x0, 0x0,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
					0x0, 0x0, 0x0, };
		}

		private static void jj_la1_init_5() {
			jj_la1_5 = new int[] { 0x0, 0x0, 0x100000, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x100000, 0x0, 0x0, 0x0, 0x0, 0x0,
					0x100000, 0x0, 0x0, 0x100000, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x100000, 0x100000, 0x100000, 0x0, 0x0,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x100000, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x100000, 0x0, 0x0, 0x0, 0x0,
					0x5b, 0x5b, 0x40, 0x1b, 0x0, 0x9b, 0x9b, 0x80, 0x0, 0x0, 0x100000, 0x100000, 0x1b, 0x1b, 0x0, 0x1f,
					0x1f, 0xb001b, 0xb001b, 0xb0000, 0xdb, 0xdb, 0x0, 0xf00, 0xf00, 0x0, 0x40000, 0x40000, 0x100000,
					0x0, 0x100000, 0x100000, 0x100000, 0x0, 0x0, 0x100000, 0x100000, 0x100000, 0x0, 0x100000, 0x0, 0x0,
					0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x100000, 0x100000,
					0x0, 0x0, 0x0, 0x0, 0x100000, 0x0, 0x0, 0x0, };
		}

		final private JJCalls[] jj_2_rtns = new JJCalls[63];
		private boolean jj_rescan = false;
		private int jj_gc = 0;

		/**
		 * Added later.
		 * 
		 * @param stream
		 * @param parseACall
		 * @author Aman Nougrahiya
		 */
		public CParser(java.io.InputStream stream, boolean parseACall) {
			this(stream, null);
			this.parseACall = parseACall;
			this.parseAnSPE = false;
		}

		/** Constructor with InputStream. */
		public CParser(java.io.InputStream stream) {
			this(stream, null);
		}

		/** Constructor with InputStream and supplied encoding */
		public CParser(java.io.InputStream stream, String encoding) {
			try {
				jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
			} catch (java.io.UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			token_source = new CParserTokenManager(jj_input_stream);
			token = new Token();
			jj_ntk = -1;
			jj_gen = 0;
			for (int i = 0; i < 116; i++) {
				jj_la1[i] = -1;
			}
			for (int i = 0; i < jj_2_rtns.length; i++) {
				jj_2_rtns[i] = new JJCalls();
			}
		}

		/** Reinitialise. */
		@SuppressWarnings("unused")
		public void ReInit(java.io.InputStream stream) {
			ReInit(stream, null);
		}

		/** Reinitialise. */
		public void ReInit(java.io.InputStream stream, String encoding) {
			try {
				jj_input_stream.ReInit(stream, encoding, 1, 1);
			} catch (java.io.UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			token_source.ReInit(jj_input_stream);
			token = new Token();
			jj_ntk = -1;
			jj_gen = 0;
			for (int i = 0; i < 116; i++) {
				jj_la1[i] = -1;
			}
			for (int i = 0; i < jj_2_rtns.length; i++) {
				jj_2_rtns[i] = new JJCalls();
			}
		}

		/** Constructor. */
		@SuppressWarnings("unused")
		public CParser(java.io.Reader stream) {
			jj_input_stream = new SimpleCharStream(stream, 1, 1);
			token_source = new CParserTokenManager(jj_input_stream);
			token = new Token();
			jj_ntk = -1;
			jj_gen = 0;
			for (int i = 0; i < 116; i++) {
				jj_la1[i] = -1;
			}
			for (int i = 0; i < jj_2_rtns.length; i++) {
				jj_2_rtns[i] = new JJCalls();
			}
		}

		/** Reinitialise. */
		@SuppressWarnings("unused")
		public void ReInit(java.io.Reader stream) {
			jj_input_stream.ReInit(stream, 1, 1);
			token_source.ReInit(jj_input_stream);
			token = new Token();
			jj_ntk = -1;
			jj_gen = 0;
			for (int i = 0; i < 116; i++) {
				jj_la1[i] = -1;
			}
			for (int i = 0; i < jj_2_rtns.length; i++) {
				jj_2_rtns[i] = new JJCalls();
			}
		}

		/** Constructor with generated Token Manager. */
		@SuppressWarnings("unused")
		public CParser(CParserTokenManager tm) {
			token_source = tm;
			token = new Token();
			jj_ntk = -1;
			jj_gen = 0;
			for (int i = 0; i < 116; i++) {
				jj_la1[i] = -1;
			}
			for (int i = 0; i < jj_2_rtns.length; i++) {
				jj_2_rtns[i] = new JJCalls();
			}
		}

		/** Reinitialise. */
		@SuppressWarnings("unused")
		public void ReInit(CParserTokenManager tm) {
			token_source = tm;
			token = new Token();
			jj_ntk = -1;
			jj_gen = 0;
			for (int i = 0; i < 116; i++) {
				jj_la1[i] = -1;
			}
			for (int i = 0; i < jj_2_rtns.length; i++) {
				jj_2_rtns[i] = new JJCalls();
			}
		}

		private Token jj_consume_token(int kind) throws ParseException {
			Token oldToken;
			if ((oldToken = token).next != null) {
				token = token.next;
			} else {
				token = token.next = token_source.getNextToken();
			}
			jj_ntk = -1;
			if (token.kind == kind) {
				jj_gen++;
				if (++jj_gc > 100) {
					jj_gc = 0;
					for (int i = 0; i < jj_2_rtns.length; i++) {
						JJCalls c = jj_2_rtns[i];
						while (c != null) {
							if (c.gen < jj_gen) {
								c.first = null;
							}
							c = c.next;
						}
					}
				}
				return token;
			}
			token = oldToken;
			jj_kind = kind;
			throw generateParseException();
		}

		static private final class LookaheadSuccess extends java.lang.Error {

			/**
			 * 
			 */
			private static final long serialVersionUID = -4370572257618084435L;
		}

		final private LookaheadSuccess jj_ls = new LookaheadSuccess();

		private boolean jj_scan_token(int kind) {
			if (jj_scanpos == jj_lastpos) {
				jj_la--;
				if (jj_scanpos.next == null) {
					jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
				} else {
					jj_lastpos = jj_scanpos = jj_scanpos.next;
				}
			} else {
				jj_scanpos = jj_scanpos.next;
			}
			if (jj_rescan) {
				int i = 0;
				Token tok = token;
				while (tok != null && tok != jj_scanpos) {
					i++;
					tok = tok.next;
				}
				if (tok != null) {
					jj_add_error_token(kind, i);
				}
			}
			if (jj_scanpos.kind != kind) {
				return true;
			}
			if (jj_la == 0 && jj_scanpos == jj_lastpos) {
				throw jj_ls;
			}
			return false;
		}

		/** Get the next Token. */
		@SuppressWarnings("unused")
		final public Token getNextToken() {
			if (token.next != null) {
				token = token.next;
			} else {
				token = token.next = token_source.getNextToken();
			}
			jj_ntk = -1;
			jj_gen++;
			return token;
		}

		/** Get the specific Token. */
		final public Token getToken(int index) {
			Token t = jj_lookingAhead ? jj_scanpos : token;
			for (int i = 0; i < index; i++) {
				if (t.next != null) {
					t = t.next;
				} else {
					t = t.next = token_source.getNextToken();
				}
			}
			return t;
		}

		private int jj_ntk() {
			if ((jj_nt = token.next) == null) {
				return (jj_ntk = (token.next = token_source.getNextToken()).kind);
			} else {
				return (jj_ntk = jj_nt.kind);
			}
		}

		private java.util.List<int[]> jj_expentries = new java.util.ArrayList<>();
		private int[] jj_expentry;
		private int jj_kind = -1;
		private int[] jj_lasttokens = new int[100];
		private int jj_endpos;

		private void jj_add_error_token(int kind, int pos) {
			if (pos >= 100) {
				return;
			}
			if (pos == jj_endpos + 1) {
				jj_lasttokens[jj_endpos++] = kind;
			} else if (jj_endpos != 0) {
				jj_expentry = new int[jj_endpos];
				for (int i = 0; i < jj_endpos; i++) {
					jj_expentry[i] = jj_lasttokens[i];
				}
				jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
					int[] oldentry = (int[]) (it.next());
					if (oldentry.length == jj_expentry.length) {
						for (int i = 0; i < jj_expentry.length; i++) {
							if (oldentry[i] != jj_expentry[i]) {
								continue jj_entries_loop;
							}
						}
						jj_expentries.add(jj_expentry);
						break jj_entries_loop;
					}
				}
				if (pos != 0) {
					jj_lasttokens[(jj_endpos = pos) - 1] = kind;
				}
			}
		}

		/** Generate ParseException. */
		public ParseException generateParseException() {
			jj_expentries.clear();
			boolean[] la1tokens = new boolean[183];
			if (jj_kind >= 0) {
				la1tokens[jj_kind] = true;
				jj_kind = -1;
			}
			for (int i = 0; i < 116; i++) {
				if (jj_la1[i] == jj_gen) {
					for (int j = 0; j < 32; j++) {
						if ((jj_la1_0[i] & (1 << j)) != 0) {
							la1tokens[j] = true;
						}
						if ((jj_la1_1[i] & (1 << j)) != 0) {
							la1tokens[32 + j] = true;
						}
						if ((jj_la1_2[i] & (1 << j)) != 0) {
							la1tokens[64 + j] = true;
						}
						if ((jj_la1_3[i] & (1 << j)) != 0) {
							la1tokens[96 + j] = true;
						}
						if ((jj_la1_4[i] & (1 << j)) != 0) {
							la1tokens[128 + j] = true;
						}
						if ((jj_la1_5[i] & (1 << j)) != 0) {
							la1tokens[160 + j] = true;
						}
					}
				}
			}
			for (int i = 0; i < 183; i++) {
				if (la1tokens[i]) {
					jj_expentry = new int[1];
					jj_expentry[0] = i;
					jj_expentries.add(jj_expentry);
				}
			}
			jj_endpos = 0;
			jj_rescan_token();
			jj_add_error_token(0, 0);
			int[][] exptokseq = new int[jj_expentries.size()][];
			for (int i = 0; i < jj_expentries.size(); i++) {
				exptokseq[i] = jj_expentries.get(i);
			}
			return new ParseException(token, exptokseq, tokenImage);
		}

		/** Enable tracing. */
		@SuppressWarnings("unused")
		final public void enable_tracing() {
		}

		/** Disable tracing. */
		@SuppressWarnings("unused")
		final public void disable_tracing() {
		}

		private void jj_rescan_token() {
			jj_rescan = true;
			for (int i = 0; i < 63; i++) {
				try {
					JJCalls p = jj_2_rtns[i];
					do {
						if (p.gen > jj_gen) {
							jj_la = p.arg;
							jj_lastpos = jj_scanpos = p.first;
							switch (i) {
							case 0:
								jj_3_1();
								break;
							case 1:
								jj_3_2();
								break;
							case 2:
								jj_3_3();
								break;
							case 3:
								jj_3_4();
								break;
							case 4:
								jj_3_5();
								break;
							case 5:
								jj_3_6();
								break;
							case 6:
								jj_3_7();
								break;
							case 7:
								jj_3_8();
								break;
							case 8:
								jj_3_9();
								break;
							case 9:
								jj_3_10();
								break;
							case 10:
								jj_3_11();
								break;
							case 11:
								jj_3_12();
								break;
							case 12:
								jj_3_13();
								break;
							case 13:
								jj_3_14();
								break;
							case 14:
								jj_3_15();
								break;
							case 15:
								jj_3_16();
								break;
							case 16:
								jj_3_17();
								break;
							case 17:
								jj_3_18();
								break;
							case 18:
								jj_3_19();
								break;
							case 19:
								jj_3_20();
								break;
							case 20:
								jj_3_21();
								break;
							case 21:
								jj_3_22();
								break;
							case 22:
								jj_3_23();
								break;
							case 23:
								jj_3_24();
								break;
							case 24:
								jj_3_25();
								break;
							case 25:
								jj_3_26();
								break;
							case 26:
								jj_3_27();
								break;
							case 27:
								jj_3_28();
								break;
							case 28:
								jj_3_29();
								break;
							case 29:
								jj_3_30();
								break;
							case 30:
								jj_3_31();
								break;
							case 31:
								jj_3_32();
								break;
							case 32:
								jj_3_33();
								break;
							case 33:
								jj_3_34();
								break;
							case 34:
								jj_3_35();
								break;
							case 35:
								jj_3_36();
								break;
							case 36:
								jj_3_37();
								break;
							case 37:
								jj_3_38();
								break;
							case 38:
								jj_3_39();
								break;
							case 39:
								jj_3_40();
								break;
							case 40:
								jj_3_41();
								break;
							case 41:
								jj_3_42();
								break;
							case 42:
								jj_3_43();
								break;
							case 43:
								jj_3_44();
								break;
							case 44:
								jj_3_45();
								break;
							case 45:
								jj_3_46();
								break;
							case 46:
								jj_3_47();
								break;
							case 47:
								jj_3_48();
								break;
							case 48:
								jj_3_49();
								break;
							case 49:
								jj_3_50();
								break;
							case 50:
								jj_3_51();
								break;
							case 51:
								jj_3_52();
								break;
							case 52:
								jj_3_53();
								break;
							case 53:
								jj_3_54();
								break;
							case 54:
								jj_3_55();
								break;
							case 55:
								jj_3_56();
								break;
							case 56:
								jj_3_57();
								break;
							case 57:
								jj_3_58();
								break;
							case 58:
								jj_3_59();
								break;
							case 59:
								jj_3_60();
								break;
							case 60:
								jj_3_61();
								break;
							case 61:
								jj_3_62();
								break;
							case 62:
								jj_3_63();
								break;
							}
						}
						p = p.next;
					} while (p != null);
				} catch (LookaheadSuccess ls) {
				}
			}
			jj_rescan = false;
		}

		private void jj_save(int index, int xla) {
			JJCalls p = jj_2_rtns[index];
			while (p.gen > jj_gen) {
				if (p.next == null) {
					p = p.next = new JJCalls();
					break;
				}
				p = p.next;
			}
			p.gen = jj_gen + xla - jj_la;
			p.first = token;
			p.arg = xla;
		}

		static final class JJCalls {
			int gen;
			Token first;
			int arg;
			JJCalls next;
		}

	}

	public static class JTBToolkit {
		static NodeToken makeNodeToken(Token t) {
			return new NodeToken(t.image.intern(), t.kind, t.beginLine, t.beginColumn, t.endLine, t.endColumn);
		}
	}
}
