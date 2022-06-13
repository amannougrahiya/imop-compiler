/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.projects.datarace;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Vector;

import imop.ast.info.InitFunctionName;
import imop.ast.info.InitRunInParallel;
import imop.ast.node.CompoundStatement;
import imop.ast.node.FunctionDefinition;
import imop.ast.node.Node;
import imop.ast.node.OmpConstruct;
import imop.ast.node.TranslationUnit;
import imop.lib.getter.AllFunctionDefinitionGetter;
import imop.lib.getter.FunctionDefinitionGetter;
import imop.lib.getter.OmpConstructsGetter;
import imop.lib.getter.StringGetter;
import imop.lib.util.Misc;
import imop.parser.CParser;
import imop.parser.ParseException;

/**
 * This class implements the main() for
 * "Dynamic Data Race Detection using Instrumentation Techniques for OpenMP
 * programs written in C"
 * It takes a file as an argument, and dumps the Instrumented Code to the
 * stdout,
 * which is then later run and its output file "dataRaceOut.txt" is analyzed by
 * another program (DataRaceAnalyzer)
 * which prints the data races detected in the program.
 * 
 * @author aman
 *
 */
public class DataRaceDetector {

	/**
	 * 
	 */
	public static boolean RELEASE = true;
	/**
	 * 
	 */

	public static DRCommonUtils commUtil = new DRCommonUtils();
	public static Node root;
	public static int numThreads = 4;

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		if (RELEASE == true) {
			/**
			 * CODE TO BE USED WHEN IN RELEASE MODE
			 */
			if (args.length != 1) {
				System.out.println("usage: java imop/lib/datarace/DataRaceDetector <InputFileName>");
				System.exit(0);
			}
			root = CParser.createCrudeASTNode(new FileInputStream(args[0]), TranslationUnit.class);
		} else {
			/**
			 * CODE TO BE USED WHEN IN DEBUG MODE
			 */
			root = CParser.createCrudeASTNode(
					new FileInputStream("/Users/aman/Dropbox/openmp/prog/compareInputOutput/inputWithPrints.i"),
					TranslationUnit.class);
			// rootScope = new CParser(new FileInputStream("/Users/aman/Dropbox/openmp/Frame/sample.i")).TranslationUnit();
			// rootScope = new CParser(new FileInputStream("/Users/aman/Dropbox/openmp/prog/diff1.i")).TranslationUnit();
		}

		// Populate functionName in Info of FunctionDefinition
		root.accept(new InitFunctionName());

		// Get a reference to all the function definitions
		AllFunctionDefinitionGetter functionFinder = new AllFunctionDefinitionGetter();
		root.accept(functionFinder);
		Vector<FunctionDefinition> allFunc = functionFinder.funcList;

		// Find those functions which can runInParallel
		OmpConstructsGetter ompConstructVisitor = new OmpConstructsGetter();
		root.accept(ompConstructVisitor);
		Vector<OmpConstruct> ompList = ompConstructVisitor.ompList;
		for (OmpConstruct ompCon : ompList) {
			ompCon.accept(new InitRunInParallel(), root);
		}

		//System.err.println("Phase 1 complete...");

		// Add the appropriate print logs in func
		for (FunctionDefinition func : allFunc) {
			if (func.getInfo().runInParallel) { // Add the appropriate print logs in func, 
				//if func can be called from a parallel context.
				func.accept(new Instrumentor(), null);
				//System.err.println("Phase 2 complete for " + func.getInfo().functionName + "...");
				func.accept(new PhaseIncrementer(), null);
				//System.err.println("Phase 3 complete for " + func.getInfo().functionName + "...");
			} else { // In case if a func can't be called from a parallel context, 
						// instrument the code selectively. No difference between main and other functions.
					// Get a list of all the Omp Constructs and instrument the statements within them.
				ompConstructVisitor = new OmpConstructsGetter();
				func.accept(ompConstructVisitor);
				ompList = ompConstructVisitor.ompList;
				for (OmpConstruct ompCons : ompList) {
					ompCons.accept(new Instrumentor(), null);
					ompCons.accept(new PhaseIncrementer(), null);
				}
				//System.err.println("Phase X complete for " + func.getInfo().functionName + "...");
			}
		}

		// Add the initialization data to the header, and starting and ending of the main function
		addHeader();
		addToMain();
		Misc.normalizeNode(root);
		System.out.println(root.getInfo().getString());
	}

	/**
	 * Adds the preprocessed form of following header files' includes: omp.h,
	 * stdio.h,
	 * and, adds the global declarations of "FILE *fp" and "int
	 * phaseCounter=0;".
	 * 
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
	public static void addHeader() throws ParseException, FileNotFoundException {
		// Obtain a new TranslationUnit (group of ElementsOfTranslation)
		String add = commUtil.getHeader(numThreads);
		TranslationUnit newTU = (TranslationUnit) CParser.createCrudeASTNode(new ByteArrayInputStream(add.getBytes()),
				TranslationUnit.class);

		// Now, extract all the ElementsOfTranslation from newTU, and add them to the ElementsOfTranslation of original TU
		List<Node> oldList = ((TranslationUnit) root).getF0().nodes;
		List<Node> newList = newTU.getF0().nodes;
		for (Node src : oldList) {
			newList.add(src);
		} // Now, newList contains all the external declarations appended with the new declarations from commUtil.getHeader()
			// Replace oldList with newList
		((TranslationUnit) root).getF0().nodes = newList;

		// Now, we need to delete the duplicates in translationUnit (added due to extra preprocessing)
		removeDuplicateExternals((TranslationUnit) root);
	}

	public static void removeDuplicateExternals(TranslationUnit n) {
		List<Node> list = n.getF0().nodes;
		int i = 0;
		while (i < list.size()) {
			Node currElement = list.get(i);
			boolean found = false;
			int index = 0;
			for (Node tempElement : list) {
				index = list.indexOf(tempElement);
				if (i >= index) {
					continue;
				}
				StringGetter str1 = new StringGetter();
				StringGetter str2 = new StringGetter();
				currElement.accept(str1);
				tempElement.accept(str2);
				if (currElement.getInfo().getString().equals(tempElement.getInfo().getString())) {
					found = true;
					break;
				}
			}
			// If duplicate found in future, delete that
			if (found) {
				list.remove(index);
			}
			i++;
		}
	}

	/**
	 * Adds the statements required to open the file dataRaceOut.txt in read
	 * mode.
	 * 
	 * @throws ParseException
	 */
	public static void addToMain() throws ParseException {
		// Find the FunctionDefinition node for main()
		// (this is written crudely in absence of Symbol Table)
		FunctionDefinitionGetter mainGetter = new FunctionDefinitionGetter();
		root.accept(mainGetter, "main");
		if (mainGetter.funcDefNodes == null) {
			System.out.println("No main function found in the code! Exiting...");
			System.exit(0);
		}
		FunctionDefinition mainDef = mainGetter.funcDefNodes.get(0);

		// Obtain a CompoundStatement to add to the beginning of main
		String add = commUtil.getStartMain(numThreads);
		add = "{" + add + "}";
		CompoundStatement newComp = (CompoundStatement) CParser
				.createCrudeASTNode(new ByteArrayInputStream(add.getBytes()), CompoundStatement.class);

		// Add the elements from the actual CompoundStatement in main to newComp
		for (Node element : mainDef.getF3().getF1().nodes) {
			newComp.getF1().nodes.add(element);
		}

		// Obtain a CompoundStatement to add to the end of main
		add = commUtil.getEndMain(numThreads);
		add = "{" + add + "}";

		CompoundStatement endComp = (CompoundStatement) CParser
				.createCrudeASTNode(new ByteArrayInputStream(add.getBytes()), CompoundStatement.class);

		// Add the elements from the this CompoundStatement to newComp
		for (Node element : endComp.getF1().nodes) {
			newComp.getF1().nodes.add(element);
		}

		// Replace the old CompoundStatement with the new one
		mainDef.setF3(newComp);
	}

}
