/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.solver;

import com.microsoft.z3.*;
import imop.ast.node.external.*;
import imop.lib.analysis.SVEChecker;
import imop.lib.analysis.flowanalysis.Definition;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.solver.tokens.ExpressionTokenizer;
import imop.lib.analysis.solver.tokens.IdOrConstToken;
import imop.lib.analysis.solver.tokens.OperatorToken;
import imop.lib.analysis.solver.tokens.Tokenizable;
import imop.lib.analysis.typeSystem.FloatingType;
import imop.lib.analysis.typeSystem.IntegerType;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.builder.Builder;
import imop.lib.util.Misc;
import imop.parser.CParserConstants;
import imop.parser.Program;

import java.util.*;

/**
 * This class contains various static methods used to generate and solve Z3
 * constraints for various analysis purposes.
 * 
 * @author Aman Nougrahiya
 *
 */
public class ConstraintsGenerator {

	public static String allConstraintString = "";
	/**
	 * When this flag is set, we consider two sets of reaching definitions for
	 * different threads to be unique only if each entry in the set writes same
	 * value for the symbols.
	 */
	private static boolean isSVESensitive = true;

	/**
	 * When set, no debug messages are printed to STDOUT.
	 */
	private static boolean debugMode = true;

	/**
	 * This represents the set of induction ranges from program, when
	 * reinitInductionSet() was called.
	 */
	private static Set<InductionRange> inductionSet;
	public static long initTimer = 0;

	private static Set<InductionRange> getInductionSet() {
		if (inductionSet == null) {
			ConstraintsGenerator.reinitInductionSet();
		}
		return inductionSet;
	}

	public static void resetStaticFields() {
		ConstraintsGenerator.inductionSet = null;
		ConstraintsGenerator.initTimer = 0;
	}

	/**
	 * This method is used to reinitialize various global data structures, that
	 * are used in generation of constraints.
	 * <br>
	 * Note that this method must be recalled whenever induction variables, or
	 * their symbolic value ranges might have changed.
	 */
	public static void reinitInductionSet() {
		if (!Program.fieldSensitive || !FieldSensitivity.neededAtLeastOnce) {
			return;
		}
		long localTimer = System.nanoTime();
		inductionSet = new HashSet<>();
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main == null) {
			return;
		}
		Set<Node> loopHeaders = NaturalLoopAnalysis.getLoopHeadersFromCFG(main);
		for (Node loopHeader : loopHeaders) {
			for (Node loopTail : NaturalLoopAnalysis.getLoopTails(loopHeader)) {
				Set<Node> loopContents = NaturalLoopAnalysis.getLoopContentsOfThisLoopHeader(loopHeader, loopTail);
				HashMap<Symbol, InductionRange> map = NaturalLoopAnalysis.getBasicInductionValueRange(loopHeader,
						loopTail, loopContents);
				inductionSet.addAll(map.values());
			}
		}
		if (ConstraintsGenerator.debugMode) {
			System.err.println("Reinitialized a total of " + inductionSet.size() + " symbols.");
		}
		long totalTime = System.nanoTime() - localTimer;
		ConstraintsGenerator.initTimer += totalTime;
		System.err.println("Total time taken for initializing the induction sets: " + totalTime / 1.0e9 + "s.");
	}

	/**
	 * Given a set of seed constraints in terms of C expressions, this
	 * method adds these constraints, and constraints on the values of the
	 * identifiers involved in these expressions (inductively), to a newly
	 * created solver object, to check if the system is satisfiable.
	 * 
	 * @param seedConstraints
	 *                        set of seed constraints in terms of C expressions.
	 * @return
	 *         true, if a solution may exist.
	 */
	public static boolean mayBeSatisfiable(Set<SeedConstraint> seedConstraints) {
		if (!Program.fieldSensitive) {
			return true;
		}
		FieldSensitivity.counter++;
		HashMap<String, String> cfg = new HashMap<>();
		cfg.put("model_validate", "true");
		cfg.put("timeout", Program.z3TimeoutInMilliSecs);
		Context context = new Context(cfg);
		Solver solver = context.mkSimpleSolver();
		long timer = System.nanoTime();
		HashMap<Symbol, Set<DependenceEntry>> mapFromSymbolsToDependenceEntrySets = new HashMap<>();
		/*
		 * Step 1: Obtain properly renamed constraints on values of identifiers
		 * in e1 and e2, in mapFromSymbolsToDependenceEntrySets. Also, save the
		 * properly renamed constraint "e1=e2" in finalizedSeeds.
		 */
		List<List<Tokenizable>> finalizedSeeds = new ArrayList<>();
		HashMap<String, String> renamingMap;
		for (SeedConstraint constraint : seedConstraints) {
			List<Tokenizable> seedRenamed = new ArrayList<>();
			seedRenamed.add(constraint.getOperator());

			renamingMap = ConstraintsGenerator.getValueConstraintsAndRenamingMap(constraint.getLhsExp(),
					constraint.tidLHS, constraint.getLhsLeafNode(), mapFromSymbolsToDependenceEntrySets);
			seedRenamed.addAll(ExpressionTokenizer.getRenamedList(constraint.getLhsExp(), renamingMap));

			renamingMap = ConstraintsGenerator.getValueConstraintsAndRenamingMap(constraint.getRhsExp(),
					constraint.tidRHS, constraint.getRhsLeafNode(), mapFromSymbolsToDependenceEntrySets);
			seedRenamed.addAll(ExpressionTokenizer.getRenamedList(constraint.getRhsExp(), renamingMap));
			finalizedSeeds.add(seedRenamed);
		}

		/*
		 * Step 2: Replace all induction variable entries with their induction
		 * ranges.
		 */
		ConstraintsGenerator.utilizeInductionRanges(mapFromSymbolsToDependenceEntrySets);

		/*
		 * Step 3: Obtain a map from identifiers used in the finalized set of
		 * dependence entries to ArithExpr objects for Z3.
		 */
		IdOrConstToken numThreads = IdOrConstToken.getNewIdToken("numTh");
		HashMap<String, ArithExpr> cVarsToZ3Vars = ConstraintsGenerator
				.getArithExprFromAssertions(mapFromSymbolsToDependenceEntrySets, seedConstraints, numThreads, context);
		allConstraintString += "Generated the following Z3 variables: \n";
		for (ArithExpr arithExpr : cVarsToZ3Vars.values()) {
			allConstraintString += "\t" + arithExpr + ": " + arithExpr.getSort() + "\n";
		}

		/*
		 * Step 4: Create a final set of (in)equations to be fed to Z3 solver,
		 * using the mapFromSymbolsToDependenceEntrySets, thread-id constraints,
		 * and finalized seeds. Also, validate all dependence assertions,
		 * after breaking cycles, if any.
		 */
		List<List<Tokenizable>> finalAssertions = ConstraintsGenerator
				.generateDependenceAndInductionAssertions(mapFromSymbolsToDependenceEntrySets, numThreads);
		Collection<String> knownInductionNames = ConstraintsGenerator
				.getKnownInductionRanges(mapFromSymbolsToDependenceEntrySets).keySet();
		Set<List<Tokenizable>> newReplacementAssertions = ConstraintsGenerator.breakCycles(finalAssertions,
				knownInductionNames);
		// Add newly added identifiers to the Z3 variable map.
		for (List<Tokenizable> replacementAssertion : newReplacementAssertions) {
			for (Tokenizable token : replacementAssertion) {
				if (token instanceof IdOrConstToken) {
					IdOrConstToken idOrC = (IdOrConstToken) token;
					if (idOrC.isAnIdentifier()) {
						if (!cVarsToZ3Vars.containsKey(idOrC.getIdentifier().toString())) {
							cVarsToZ3Vars.put(idOrC.toString(), context.mkIntConst(idOrC.toString()));
						}
					}
				}
			}
		}
		boolean isValidSystem = ConstraintsGenerator.validateSystemOfEquations(finalAssertions);
		if (!isValidSystem) {
			Misc.warnDueToLackOfFeature("WARNING: Generated invalid system of (in)equations: " + finalAssertions
					+ "\n\t Returning true, conservatively.", null);
			context.close();
			return true;
		}
		finalAssertions.addAll(ConstraintsGenerator.generateSeedAndThreadsAssertions(finalizedSeeds,
				mapFromSymbolsToDependenceEntrySets, numThreads));
		allConstraintString += "Final set of inequations are: \n";
		for (List<Tokenizable> assertion : finalAssertions) {
			allConstraintString += "\t" + assertion + "\n";
		}

		/*
		 * Step 5: Use Z3 solver on the final set of constraints.
		 */
		for (List<Tokenizable> assertion : finalAssertions) {
			Expr finalExpr = ConstraintsGenerator.getZ3Expression(assertion, cVarsToZ3Vars, context);
			if (finalExpr == null) {
				// Ignore the assertion
				continue;
			}
			if (finalExpr instanceof BoolExpr) {
				solver.add((BoolExpr) finalExpr);
			}
			if (debugMode) {
				System.err.println(finalExpr);
			}
		}
		Status st = solver.check();
		timer = System.nanoTime() - timer;
		if (debugMode) {
			System.err.println("\n\tTime taken by the solver: " + timer * 1.0 / 1e9 + "s.");
			System.err.println("****");
		}
		if (st == Status.SATISFIABLE) {
			allConstraintString += "\nTime taken: " + timer * 1.0 / 1e9 + "s for SATISFIABLE state.";
			Model returnModel = solver.getModel();
			allConstraintString += "\nSolution: \n";
			for (ArithExpr var : cVarsToZ3Vars.values()) {
				allConstraintString += ("\t" + var + ": " + returnModel.getConstInterp(var) + "\n");
			}

			return true;
		} else if (st == Status.UNKNOWN) {
			allConstraintString += "\nTime taken: " + timer * 1.0 / 1e9 + "s for UNKNOWN state.";
			System.err.println("Found an unknown state!");
			return true;
		} else {
			assert (st == Status.UNSATISFIABLE);
			allConstraintString += "\nTime taken: " + timer * 1.0 / 1e9 + "s for UNSATISFIABLE state.";
			return false;
		}
	}

	/**
	 * Given an expression {@code tokens}, and a set of
	 * {@code mapFromSymbolsToDependenceEntrySets} that will already be modeled
	 * in the system
	 * of (in)equations, this method adds to the
	 * {@code mapFromSymbolsToDependenceEntrySets}
	 * various constraints on the values of identifiers read in the expression.
	 * <br>
	 * Corresponding to the {@code tokens}, this method also returns a list of
	 * tokens with some of its identifiers renamed to ensure that there are no
	 * naming conflicts.
	 * <br>
	 * <b>Note that these entries may still contain inductive and recursive
	 * definitions. Those are supposed to be removed in the next step.</b>
	 * 
	 * @param tokens
	 *                                            an expression (without
	 *                                            side-effects) in its prefix form.
	 * @param tid
	 *                                            thread-id associated with the
	 *                                            expression.
	 * @param cfgNode
	 *                                            CFG node at which the expression
	 *                                            appears.
	 * @param mapFromSymbolsToDependenceEntrySets
	 *                                            set of entries that will be used
	 *                                            to generate the system of
	 *                                            (in)equations.
	 * @return
	 *         renaming map to be applied in {@code tokens} to obtain correct
	 *         set of (in)equations, without any naming conflicts/mismatches.
	 */
	private static HashMap<String, String> getValueConstraintsAndRenamingMap(List<Tokenizable> tokens,
			IdOrConstToken tid, Node cfgNode,
			HashMap<Symbol, Set<DependenceEntry>> mapFromSymbolsToDependenceEntrySets) {
		cfgNode = Misc.getCFGNodeFor(cfgNode);
		HashMap<String, String> finalRenamingMap = new HashMap<>();
		Set<Symbol> symbolSet = ExpressionTokenizer.getSymbolSet(tokens);
		Set<Definition> rdSet = cfgNode.getInfo().getReachingDefinitions();
		symLoop: for (Symbol symbol : symbolSet) {
			Set<Node> reachingDefs = new HashSet<>();
			for (Definition rdDef : rdSet) {
				Node rd = rdDef.getDefiningNode();
				if (rd.getInfo().getWrites().contains(symbol)) {
					reachingDefs.add(rd);
				}
			}
			// System.err.println("Reaching definitions of " + symbol.getName() + " are: " +
			// reachingDefs);
			/********
			 * Skimming through all the entries in
			 * mapFromSymbolsToDependenceEntrySets, following two cases may
			 * occur:
			 * ..1. In the set associated with this symbol (empty if new), there
			 * .....is an entry that refers to the same set of reaching
			 * .....definitions, and the thread-id is same as tid.
			 * .....In this case, we should skip processing this cell.
			 * .
			 * ..2. Otherwise,
			 * .....We should create a dependence entry for this
			 * .....set of reaching definitions, with a new z3 variable name.
			 * .
			 * .....
			 * In the above cases, whenever we are adding the set of
			 * reaching-definitions, then AFTER adding the set, we should call
			 * this * same method on the "RHS" expression of the reaching
			 * definitions. (Note that this would not lead to infinite recursion
			 * due to case 1 above.)
			 * .....
			 * Furthermore, note that we do not attempt to detect/remove any
			 * recursive/inductive definitions; nor do we attempt to use
			 * InductionRange objects.
			 * .....
			 * It is important to save all renaming added here to a temporary
			 * map that can be used to create the correct return list.
			 **********/
			/*
			 * Get handle of the set of dependence entries for this symbol;
			 * let's call it depEntrySet.
			 */
			Set<DependenceEntry> depEntrySet = null;
			if (mapFromSymbolsToDependenceEntrySets.containsKey(symbol)) {
				depEntrySet = mapFromSymbolsToDependenceEntrySets.get(symbol);
			} else {
				depEntrySet = new HashSet<>();
				mapFromSymbolsToDependenceEntrySets.put(symbol, depEntrySet);
			}

			/*
			 * Check if an entry corresponding to reachingDefs already exists in
			 * depEntrySet, for same thread-id.
			 */
			DependenceEntry foundDep = null;
			depEntryLoop: for (DependenceEntry depEntry : depEntrySet) {
				if (depEntry.getReachingDefinitions().equals(reachingDefs)) {
					if (depEntry.getTid().toString().equals(tid.toString())) {
						// Note that here single-valuedness of reaching-definition entries does not
						// matter.
						foundDep = depEntry;
						break;
					} else {
						if (ConstraintsGenerator.isSVESensitive) {
							// Ensure that all reaching-definitions are single-valued.
							boolean allSame = true;
							for (Node rd : reachingDefs) {
								if (!SVEChecker.writesSingleValue(rd)) {
									allSame = false;
									break;
								}
							}
							if (allSame) {
								foundDep = depEntry;
								break depEntryLoop;
							}
						} else {
							// We cannot use this dependence entry.
						}
					}
				}
			}
			if (foundDep != null) {
				/*
				 * This is case 1 from above; continue to the next symbol, while
				 * reusing the mapping of LHS.
				 */
				String baseName = foundDep.getSymbol().toString();
				String z3Name = foundDep.getZ3Name();
				if (!baseName.equals(z3Name)) {
					finalRenamingMap.put(baseName, z3Name);
				}
				continue symLoop;
			} else {
				/*
				 * This is case 2; create a new dependence entry.
				 * To settle the renaming-map for this newly created entry, we
				 * should do the following:
				 * .. To avoid infinite recursion, add the dependence entry to
				 * .. the set;
				 * .. Then, for the RHS of each reaching-definition, add value
				 * .. constraints, while collecting renaming info.
				 */
				String newTempName = Builder.getNewTempName(symbol.getName());
				DependenceEntry newDepEntry = new DependenceEntry(symbol, newTempName, tid, reachingDefs);
				depEntrySet.add(newDepEntry);
				for (Node rd : reachingDefs) {
					// Obtain the assigning expression, e.g. "x = 3" from "int x = 3;"
					List<Tokenizable> rdTokens = ExpressionTokenizer.getAssigningExpression(rd);
					if (rdTokens == null || rdTokens.isEmpty()) {
						// This we will handle later; skip for now.
						continue;
					}
					HashMap<IdOrConstToken, List<Tokenizable>> nextRDMap = ExpressionTokenizer
							.getNormalizedForm(rdTokens);
					for (IdOrConstToken id : nextRDMap.keySet()) {
						List<Tokenizable> prevReachingDefinition = nextRDMap.get(id);
						HashMap<String, String> rhsRenamingMap = ConstraintsGenerator.getValueConstraintsAndRenamingMap(
								prevReachingDefinition, tid, rd, mapFromSymbolsToDependenceEntrySets);
						newDepEntry.renamingMap.putAll(rhsRenamingMap);
					}
				}
				finalRenamingMap.put(symbol.toString(), newTempName);
			}
		} // End of loop on all identifiers read in the given token list.
		return finalRenamingMap;
	}

	/**
	 * This method populates induction ranges of induction variables in the
	 * provided map.
	 * <br>
	 * Note that an inductionRange of a Symbol is <i>applicable</i> for a
	 * dependence entry of that Symbol only if one of the reaching definitions
	 * from dependence entry is also the stepCFGNode of the induction range.
	 * <br>
	 * Since the formula for induction ranges may contain new variables, we
	 * update the {@code mapFromSymbolsToDependenceEntrySets} accordingly.
	 * 
	 * @param mapFromSymbolsToDependenceEntrySets
	 *                                            a map from set of symbols to power
	 *                                            set of dependence entries,
	 *                                            in which we need to utilize the
	 *                                            induction ranges.
	 */
	private static void utilizeInductionRanges(
			HashMap<Symbol, Set<DependenceEntry>> mapFromSymbolsToDependenceEntrySets) {
		Set<InductionRange> inductionSet = ConstraintsGenerator.getInductionSet();
		Set<InductionRange> addedInductionSet = new HashSet<>();
		for (InductionRange inductionRange : inductionSet) {
			Symbol inductionVariable = inductionRange.inductionVariable;
			if (!mapFromSymbolsToDependenceEntrySets.containsKey(inductionVariable)) {
				continue;
			}
			Set<DependenceEntry> dependenceEntries = mapFromSymbolsToDependenceEntrySets.get(inductionVariable);
			for (DependenceEntry depEntry : dependenceEntries) {
				if (!depEntry.getReachingDefinitions().contains(inductionRange.stepCFGNode)) {
					// This induction range is not applicable for the dependenceEntry.
					continue;
				} else {
					// Now, we set the induction-range to the dependence entry.
					if (depEntry.inductionRange != null) {
						assert (depEntry.inductionRange == inductionRange);
					}
					depEntry.inductionRange = inductionRange;
					addedInductionSet.add(inductionRange);
					/*
					 * Finally, we update the renaming-map of the dependence
					 * entry, if required after the processing of the ranges.
					 */
					for (List<Tokenizable> rangeList : inductionRange.getRanges(depEntry.getTid(),
							IdOrConstToken.getNewConstantToken("179"))) {
						HashMap<String, String> rhsRenamingMap = ConstraintsGenerator.getValueConstraintsAndRenamingMap(
								rangeList, depEntry.getTid(), inductionRange.stepCFGNode,
								mapFromSymbolsToDependenceEntrySets);
						depEntry.renamingMap.putAll(rhsRenamingMap);
					}
				}
			}
		}
	}

	/**
	 * This method detects and breaks cycles of dependences from the given map.
	 * 
	 * @param finalAssertions
	 *                        list of final assertions, from which cycles have to be
	 *                        removed.
	 * @param
	 * list
	 *                        of strings that are names of known induction ranges.
	 * @return
	 *         set of new replacement-assertions.
	 */
	private static Set<List<Tokenizable>> breakCycles(List<List<Tokenizable>> finalAssertions,
			Collection<String> knownInductionNames) {
		HashMap<String, Z3VariableNode> dependenceGraph = Z3VariableNode.getZ3VariableGraph(finalAssertions);
		Set<List<String>> setOfCycles = Z3VariableNode.getAllCycles(dependenceGraph.values());
		Set<List<Tokenizable>> newIdSet = new HashSet<>();
		if (setOfCycles.isEmpty()) {
			return newIdSet;
		} else {
			for (List<String> cycle : setOfCycles) {
				if (debugMode) {
					System.err.println("Break the cycle: " + cycle);
				}
				/*
				 * Step 1: Find a string in cycle for which the assertion
				 * contains at least one base equation (that does not form the
				 * part of the cycle). Note that if there are more than one base
				 * equations, we should simply remove the corresponding
				 * assertion.
				 */
				List<Tokenizable> baseAssertion = null;
				List<Tokenizable> firstRelatedAssertion = null;
				symLoop: for (List<Tokenizable> assertion : finalAssertions) {
					List<List<Tokenizable>> assignConstraints = ConstraintsGenerator.getAssignConstraints(assertion);
					List<List<Tokenizable>> assignConstraintRHS = new ArrayList<>();
					for (List<Tokenizable> assignConstraint : assignConstraints) {
						List<Tokenizable> firstOperand = ExpressionTokenizer.getFirstOperand(assignConstraint);
						Tokenizable idToken = firstOperand.get(0);
						if (!cycle.contains(idToken.toString())) {
							continue symLoop;
						}
						assignConstraintRHS.add(ExpressionTokenizer.getSecondOperand(assignConstraint));
					}
					if (firstRelatedAssertion == null) {
						firstRelatedAssertion = assertion;
					}
					if (assignConstraints.size() != 2) {
						continue symLoop;
					}
					/*
					 * If there exists even a single assignConstraintRHS that
					 * does not contain any reads from elements in "cycle", then
					 * use this assertion as a base assertion.
					 */
					rhsLoop: for (List<Tokenizable> listRHS : assignConstraintRHS) {
						for (String readStr : ExpressionTokenizer.getIdSet(listRHS)) {
							if (cycle.contains(readStr)) {
								continue rhsLoop;
							}
						}
						// This implies that listRHS does not read from the id's involved in this cycle.
						baseAssertion = assertion;
						break symLoop;
					}
				}
				if (baseAssertion == null) {
					System.err.println(
							"WARNING: Unable to find any base assertion. Removing this first assertion that is involved in cycle: "
									+ firstRelatedAssertion);
					finalAssertions.remove(firstRelatedAssertion);
					return newIdSet;
				}
				/*
				 * Step 2: Get the inductive and initVal lists from the base
				 * assertion.
				 */
				if (debugMode) {
					System.err.println("Base assertion is : " + baseAssertion);
				}
				List<Tokenizable> initValList = ConstraintsGenerator.getInitValueList(baseAssertion, finalAssertions,
						cycle, knownInductionNames);
				if (debugMode) {
					System.err.println("Init list is: " + initValList);
				}
				List<Tokenizable> inductiveList = ConstraintsGenerator.getInductiveExpandedList(baseAssertion,
						finalAssertions, cycle, knownInductionNames);
				if (debugMode) {
					System.err.println("Inductive list is: " + inductiveList);
				}
				List<Tokenizable> replacementList = ConstraintsGenerator.getClosedForm(initValList, inductiveList,
						knownInductionNames);
				if (replacementList.isEmpty()) {
					System.err.println(
							"WARNING: Unable to find replacement for the base assertion. \nRemoving the base assertion instead.");
					finalAssertions.remove(baseAssertion);
				} else {
					finalAssertions.remove(baseAssertion);
					replacementList.add(0, OperatorToken.LOGIC_OR);
					replacementList.addAll(1, initValList);
					if (debugMode) {
						System.err.println("Replacement: " + baseAssertion + " with " + replacementList);
					}
					finalAssertions.add(replacementList);
					newIdSet.add(replacementList);
				}
			}
			return newIdSet;
		}
	}

	/**
	 * Given an initialization and inductive definition of a name, this method
	 * returns the closed form definition, if possible.
	 * 
	 * @param initValueList
	 * @param inductiveList
	 * @param knownInductionNames
	 * @return
	 */
	private static List<Tokenizable> getClosedForm(List<Tokenizable> initValueList, List<Tokenizable> inductiveList,
			Collection<String> knownInductionNames) {
		List<Tokenizable> closedForm = new ArrayList<>();
		if (initValueList.isEmpty() || inductiveList.isEmpty()) {
			return closedForm;
		}
		/*
		 * Obtain the form "j = k;"
		 */
		IdOrConstToken j = (IdOrConstToken) ExpressionTokenizer.getFirstOperand(initValueList).get(0);
		List<Tokenizable> k = ExpressionTokenizer.getSecondOperand(initValueList);
		List<Tokenizable> inductiveRHS = ExpressionTokenizer.getSecondOperand(inductiveList);
		/*
		 * If inductiveRHS does not contain any operation on "j", then this is
		 * not an inductiveList; return it as it is.
		 */
		if (!inductiveRHS.stream().anyMatch(t -> t.toString().equals(j.toString()))) {
			return inductiveRHS;
		}
		/*
		 * Obtain the form "j = j.c1 +/- i.c2", for some i in
		 * knownInductionNames.
		 */
		int termJLength = 0;
		int termILength = 0;
		List<Tokenizable> c1 = ConstraintsGenerator.getCoefficient(inductiveRHS, j);
		if (c1.isEmpty()) {
			c1 = new ArrayList<>();
			c1.add(IdOrConstToken.getNewConstantToken("1"));
			termJLength = 1;
		} else {
			termJLength = c1.size() + 2;
		}

		// Check which induction variable is being used
		IdOrConstToken i = null;
		List<Tokenizable> c2 = new ArrayList<>();
		bivLoop: for (String biv : knownInductionNames) {
			for (Tokenizable token : inductiveRHS) {
				if (token.toString().equals(biv)) {
					i = (IdOrConstToken) token;
					break bivLoop;
				}
			}
		}
		if (i == null) {
			termILength = 0;
			c2.add(IdOrConstToken.getNewConstantToken("0"));
		} else {
			c2 = ConstraintsGenerator.getCoefficient(inductiveRHS, i);
			if (c2.isEmpty()) {
				c2 = new ArrayList<>();
				c2.add(IdOrConstToken.getNewConstantToken("1"));
				termILength = 1;
			} else {
				termILength = c2.size() + 2;
			}
		}

		if (debugMode) {
			System.err.println(c1 + "." + j + " + " + c2 + "." + i);
		}
		Tokenizable firstInRHS = inductiveRHS.get(0);
		int totalKnownLength = 0;
		OperatorToken midOp = null;
		if (firstInRHS == OperatorToken.PLUS || firstInRHS == OperatorToken.MINUS) {
			totalKnownLength++;
			midOp = (OperatorToken) firstInRHS;
			if (i == null) {
				List<Tokenizable> forConstFirst = ExpressionTokenizer.getFirstOperand(inductiveRHS);
				List<Tokenizable> forConstSecond = ExpressionTokenizer.getSecondOperand(inductiveRHS);
				if (forConstFirst.toString().contains(j.toString())) {
					/*
					 * Now, if second term does not contain any references to j,
					 * assume it to be c2, with i set to "1".
					 * Adjust the size accordingly.
					 */
					boolean found = false;
					for (Tokenizable tempToken : forConstSecond) {
						if (tempToken.toString().equals(j.toString())) {
							found = true;
							break;
						}
					}
					if (!found) {
						i = IdOrConstToken.getNewConstantToken("1");
						c2 = forConstSecond;
					}
				} else if (forConstSecond.toString().contains(j.toString())) {
					/*
					 * Now, if first term does not contain any references to j,
					 * assume it to be c2, with i set to "1".
					 * Adjust the size accordingly.
					 */
					boolean found = false;
					for (Tokenizable tempToken : forConstFirst) {
						if (tempToken.toString().equals(j.toString())) {
							found = true;
							break;
						}
					}
					if (!found) {
						i = IdOrConstToken.getNewConstantToken("1");
						c2 = forConstFirst;
					}
				}
			}
			if (c2 != null && !c2.isEmpty()) {
				termILength = c2.size();
			}
		}
		totalKnownLength += termILength + termJLength;
		IdOrConstToken unknownBIV = IdOrConstToken.getNewIdToken(Builder.getNewTempName("IV"));
		if (totalKnownLength == inductiveRHS.size()) {
			// Now, we know that the form is is realizable.
			if (ExpressionTokenizer.isOne(c1) && !ExpressionTokenizer.isZero(c2) && midOp != null) {
				if (i != null) {
					if (i.toString().equals("1")) {
						// C
						closedForm.add(OperatorToken.ASSIGN);
						closedForm.add(j);
						closedForm.add(midOp);
						closedForm.addAll(k);
						closedForm.add(OperatorToken.MUL);
						closedForm.addAll(c2);
						closedForm.add(OperatorToken.MINUS);
						closedForm.add(i);
						closedForm.add(IdOrConstToken.getNewConstantToken("1"));
						return closedForm;
					} else if (!i.toString().equals("0")) {
						// B
						closedForm.add(OperatorToken.ASSIGN);
						closedForm.add(j);
						closedForm.add(midOp);
						closedForm.addAll(k);
						closedForm.add(OperatorToken.DIV);
						closedForm.add(OperatorToken.MUL);
						closedForm.addAll(c2);
						closedForm.add(OperatorToken.MUL);
						closedForm.add(i);
						closedForm.add(OperatorToken.PLUS);
						closedForm.add(i);
						closedForm.add(IdOrConstToken.getNewConstantToken("1"));
						closedForm.add(IdOrConstToken.getNewConstantToken("2"));
						return closedForm;
					}
				}
			} else if (!ExpressionTokenizer.isZero(c1)) {
				if (ExpressionTokenizer.isZero(c2)) {
					// A
					closedForm.add(OperatorToken.ASSIGN);
					closedForm.add(j);
					closedForm.add(OperatorToken.MUL);
					closedForm.add(OperatorToken.POW);
					closedForm.addAll(c1);
					closedForm.add(unknownBIV);
					closedForm.addAll(k);
				} else if (i.toString().equals("1") && midOp != null) {
					// D
					closedForm.add(OperatorToken.ASSIGN);
					closedForm.add(j);
					closedForm.add(midOp);
					closedForm.add(OperatorToken.MUL);
					closedForm.add(OperatorToken.POW);
					closedForm.addAll(c1);
					closedForm.add(unknownBIV);
					closedForm.addAll(k);
					closedForm.add(OperatorToken.MUL);
					closedForm.addAll(c2);
					closedForm.add(OperatorToken.DIV);
					closedForm.add(OperatorToken.MINUS);
					closedForm.add(OperatorToken.POW);
					closedForm.addAll(c1);
					closedForm.add(unknownBIV);
					closedForm.add(IdOrConstToken.getNewConstantToken("1"));
					closedForm.add(OperatorToken.MINUS);
					closedForm.addAll(c1);
					closedForm.add(IdOrConstToken.getNewConstantToken("1"));
				}
			}
		}
		return closedForm;
	}

	/**
	 * Given a {@code baseAssertion} for a variable that is involved in a
	 * dependence {@code cycle}, this method returns the corresponding initial
	 * value.
	 * 
	 * @param baseAssertion
	 * @param finalAssertions
	 * @param cycle
	 * @param knownInductionNames
	 * @return
	 */
	private static List<Tokenizable> getInitValueList(List<Tokenizable> baseAssertion,
			List<List<Tokenizable>> finalAssertions, List<String> cycle, Collection<String> knownInductionNames) {
		List<Tokenizable> initValueList = new ArrayList<>();
		List<List<Tokenizable>> assignConstraints = ConstraintsGenerator.getAssignConstraints(baseAssertion);
		defLoop: for (List<Tokenizable> assignConstraint : assignConstraints) {
			List<Tokenizable> secondOperand = ExpressionTokenizer.getSecondOperand(assignConstraint);
			for (String readStr : ExpressionTokenizer.getIdSet(secondOperand)) {
				if (cycle.contains(readStr)) {
					continue defLoop;
				}
			}
			// This implies that listRHS does not read from the id's involved in this cycle.
			return assignConstraint;
		}
		return initValueList;
	}

	/**
	 * Given a {@code baseAssertion} for a variable that is involved in a
	 * dependence {@code cycle}, this method returns the corresponding expanded
	 * inductive definition.
	 * 
	 * @param baseAssertion
	 * @param finalAssertions
	 * @param cycle
	 * @param knownInductionNames
	 * @return
	 */
	private static List<Tokenizable> getInductiveExpandedList(List<Tokenizable> baseAssertion,
			List<List<Tokenizable>> finalAssertions, List<String> cycle, Collection<String> knownInductionNames) {
		List<Tokenizable> inductiveList;
		/*
		 * Step 1: Find the initial induction list.
		 */
		IdOrConstToken baseVariable = null;
		List<List<Tokenizable>> assignConstraints = ConstraintsGenerator.getAssignConstraints(baseAssertion);
		List<Tokenizable> initialInductionList = null;
		defLoop: for (List<Tokenizable> assignConstraint : assignConstraints) {
			List<Tokenizable> secondOperand = ExpressionTokenizer.getSecondOperand(assignConstraint);
			for (String readStr : ExpressionTokenizer.getIdSet(secondOperand)) {
				if (cycle.contains(readStr)) {
					// Found the inductive definition.
					baseVariable = (IdOrConstToken) ExpressionTokenizer.getFirstOperand(assignConstraint).get(0);
					initialInductionList = assignConstraint;
					break defLoop;
				}
			}
		}
		if (initialInductionList == null) {
			// Could not find the inductive definition.
			return new ArrayList<>();
		}

		/*
		 * Step 2: Find the expanded induction list.
		 */
		HashMap<String, List<Tokenizable>> replacementMap = new HashMap<>();
		for (String cyclicVar : cycle) {
			// Find the assertion for cyclicVar, except for the baseVariable.
			if (cyclicVar.equals(baseVariable.toString())) {
				continue;
			}
			List<Tokenizable> varAssert = ExpressionTokenizer.getAssertionFor(cyclicVar, finalAssertions);
			List<List<Tokenizable>> varAssignAssertList = ConstraintsGenerator.getAssignConstraints(varAssert);
			if (varAssignAssertList.size() != 1) {
				return new ArrayList<>();
			}
			List<Tokenizable> varAssignRHS = ExpressionTokenizer.getSecondOperand(varAssignAssertList.get(0));
			replacementMap.put(cyclicVar, varAssignRHS);
		}
		boolean done = false;
		inductiveList = initialInductionList;
		do {
			List<Tokenizable> inductionLHS = ExpressionTokenizer.getFirstOperand(inductiveList);
			List<Tokenizable> inductionRHS = ExpressionTokenizer.getSecondOperand(inductiveList);
			inductiveList = new ArrayList<>(inductionLHS);
			inductiveList.add(0, OperatorToken.ASSIGN);
			Set<String> replacedVars = new HashSet<>();
			boolean changed = false;
			for (Tokenizable rhsToken : inductionRHS) {
				if (rhsToken instanceof OperatorToken) {
					inductiveList.add(rhsToken);
				} else {
					IdOrConstToken idToken = (IdOrConstToken) rhsToken;
					if (idToken.isAConstant()) {
						inductiveList.add(rhsToken);
					} else {
						// If replacement exists, perform it.
						String idStr = idToken.getIdentifier().toString();
						if (replacementMap.containsKey(idStr)) {
							inductiveList.addAll(replacementMap.get(idStr));
							replacedVars.add(idStr);
							changed = true;
						} else {
							inductiveList.add(idToken);
						}
					}
				}
			}
			/*
			 * Remove the mapping for those strings which have been replaced at
			 * least once. This would ensure termination.
			 */
			for (String idStr : replacedVars) {
				replacementMap.remove(idStr);
			}

			/*
			 * Check if replacement is complete (or if no more change is done).
			 * We should not have read of any cyclic symbol other than
			 * baseVariable in inductiveList.
			 */
			if (!changed) {
				break;
			}
			done = true;
			for (Tokenizable token : inductiveList) {
				if (token.toString().equals(baseVariable.getIdentifier().toString())) {
					continue;
				}
				if (cycle.contains(token.toString())) {
					done = false;
					break;
				}
			}
		} while (!done);
		return inductiveList;
	}

	public static List<Tokenizable> getCoefficient(List<Tokenizable> tokens, IdOrConstToken id) {
		List<Tokenizable> coefficient = new ArrayList<>();
		int index = -1;
		for (Tokenizable token : tokens) {
			index++;
			if (token == OperatorToken.MUL) {
				List<Tokenizable> subList = tokens.subList(index, tokens.size());
				List<Tokenizable> firstOperand = ExpressionTokenizer.getFirstOperand(subList);
				List<Tokenizable> secondOperand = ExpressionTokenizer.getSecondOperand(subList);
				if (firstOperand.size() == 1) {
					Tokenizable idToken = firstOperand.get(0);
					if (idToken.toString().equals(id.toString())) {
						return secondOperand;
					}
				}
				if (secondOperand.size() == 1) {
					Tokenizable idToken = secondOperand.get(0);
					if (idToken.toString().equals(id.toString())) {
						return firstOperand;
					}
				}
			}
		}
		return coefficient;
	}

	/**
	 * Obtain list of <i>assign</i> constraints in the assertion, of the form
	 * {@code = x e}, where x is an identifier.
	 * 
	 * @param assertion
	 *                  an assertion, in prefix form.
	 * @return
	 *         list of all the <i>assign</i> constraints in the assertion.
	 */
	public static List<List<Tokenizable>> getAssignConstraints(List<Tokenizable> assertion) {
		List<List<Tokenizable>> assignConstraintSet = new ArrayList<>();
		int index = -1;
		for (Tokenizable token : assertion) {
			index++;
			if (token == OperatorToken.ASSIGN) {
				List<Tokenizable> subList = assertion.subList(index, assertion.size());
				List<Tokenizable> firstOperand = ExpressionTokenizer.getFirstOperand(subList);
				List<Tokenizable> secondOperand = ExpressionTokenizer.getSecondOperand(subList);
				if (firstOperand.size() != 1) {
					continue;
				}
				List<Tokenizable> assignConstraint = new ArrayList<>();
				assignConstraint.add(OperatorToken.ASSIGN);
				assignConstraint.addAll(firstOperand);
				assignConstraint.addAll(secondOperand);
				assignConstraintSet.add(assignConstraint);
			}
		}
		return assignConstraintSet;
	}

	/**
	 * Obtain all known induction ranges in the given map of dependences.
	 * 
	 * @param mapFromSymbolsToDependenceEntrySets
	 *                                            dependence information.
	 * @return
	 *         a map that, given a symbol name, returns its dependence-entry
	 *         that represents the induction range, if any.
	 */
	public static HashMap<String, DependenceEntry> getKnownInductionRanges(
			HashMap<Symbol, Set<DependenceEntry>> mapFromSymbolsToDependenceEntrySets) {
		HashMap<String, DependenceEntry> knownInductionRanges = new HashMap<>();
		for (Symbol sym : mapFromSymbolsToDependenceEntrySets.keySet()) {
			Set<DependenceEntry> depEntrySet = mapFromSymbolsToDependenceEntrySets.get(sym);
			for (DependenceEntry depEntry : depEntrySet) {
				if (depEntry.inductionRange != null) {
					knownInductionRanges.put(depEntry.getZ3Name(), depEntry);
				}
			}
		}
		if (debugMode) {
			System.err.println("\tKnown induction ranges exist for: " + knownInductionRanges.keySet());
		}
		return knownInductionRanges;
	}

	/**
	 * Given a set of seed constraints, and their generated reaching
	 * definitions, this method creates Z3 identifiers required to express the
	 * dependences using constraints in Z3 solver.
	 * <br>
	 * Note that these Z3 variables are created and added to the given context.
	 * 
	 * @param mapFromSymbolsToDependenceEntrySets
	 *                                            dependence information.
	 * @param seedConstraints
	 *                                            set of seed constraints, from
	 *                                            which identifiers
	 *                                            corresponding to thread-id's are
	 *                                            created.
	 * @param context
	 *                                            a Z3 context.
	 * @return
	 *         a map from identifier names to Z3 variables.
	 */
	private static HashMap<String, ArithExpr> getArithExprFromAssertions(
			HashMap<Symbol, Set<DependenceEntry>> mapFromSymbolsToDependenceEntrySets,
			Set<SeedConstraint> seedConstraints, IdOrConstToken numThreads, Context context) {
		HashMap<String, ArithExpr> cVarsToZ3Vars = new HashMap<>();
		/*
		 * Add Z3 variables for various non-constant thread identifiers.
		 */
		if (numThreads.isAnIdentifier()) {
			String numStr = numThreads.getIdentifier().toString();
			if (!cVarsToZ3Vars.containsKey(numStr)) {
				cVarsToZ3Vars.put(numStr, context.mkIntConst(numStr));
			}
		}
		for (SeedConstraint eqConst : seedConstraints) {
			IdOrConstToken tidLHS = eqConst.tidLHS;
			if (tidLHS.isAnIdentifier()) {
				String tidLHSStr = tidLHS.getIdentifier().toString();
				if (!cVarsToZ3Vars.containsKey(tidLHSStr)) {
					cVarsToZ3Vars.put(tidLHSStr, context.mkIntConst(tidLHSStr));
				}
			}
			IdOrConstToken tidRHS = eqConst.tidRHS;
			if (tidRHS.isAnIdentifier()) {
				String tidRHSStr = tidRHS.getIdentifier().toString();
				if (!cVarsToZ3Vars.containsKey(tidRHSStr)) {
					cVarsToZ3Vars.put(tidRHSStr, context.mkIntConst(tidRHSStr));
				}
			}
		}
		/*
		 * Add Z3 variables for various identifiers in the dependence set.
		 */
		for (Symbol sym : mapFromSymbolsToDependenceEntrySets.keySet()) {
			Set<DependenceEntry> depSet = mapFromSymbolsToDependenceEntrySets.get(sym);
			Type symTep = sym.getType();
			for (DependenceEntry depEntry : depSet) {
				/*
				 * Get symbols for LHS, from z3Name.
				 */
				String idStr = depEntry.getZ3Name();
				if (!cVarsToZ3Vars.containsKey(idStr)) {
					if (symTep instanceof FloatingType) {
						cVarsToZ3Vars.put(idStr, context.mkRealConst(idStr));
					} else if (symTep instanceof IntegerType) {
						cVarsToZ3Vars.put(idStr, context.mkIntConst(idStr));
					} else {
						cVarsToZ3Vars.put(idStr, context.mkIntConst(idStr));
					}
				}
				/*
				 * Get symbols for RHS, from renamingMap.
				 */
				for (String rhsIdStr : depEntry.renamingMap.values()) {
					if (!cVarsToZ3Vars.containsKey(rhsIdStr)) {
						if (symTep instanceof FloatingType) {
							cVarsToZ3Vars.put(rhsIdStr, context.mkRealConst(rhsIdStr));
						} else if (symTep instanceof IntegerType) {
							cVarsToZ3Vars.put(rhsIdStr, context.mkIntConst(rhsIdStr));
						} else {
							cVarsToZ3Vars.put(rhsIdStr, context.mkIntConst(rhsIdStr));
						}
					}
				}
				/*
				 * Note that not all symbols would be available in the
				 * renamingMap. Specially those which do not have any
				 * definitions.
				 * Hence, in this updated code, we do not use the renaming map
				 * but the reaching-definition/induction range itself.
				 */
			}
		}
		return cVarsToZ3Vars;
	}

	private static List<List<Tokenizable>> generateDependenceAndInductionAssertions(
			HashMap<Symbol, Set<DependenceEntry>> mapFromSymbolsToDependenceEntrySets, IdOrConstToken numThreads) {
		List<List<Tokenizable>> finalAssertions = new ArrayList<>();
		for (Symbol sym : mapFromSymbolsToDependenceEntrySets.keySet()) {
			for (DependenceEntry depEntry : mapFromSymbolsToDependenceEntrySets.get(sym)) {
				finalAssertions.addAll(depEntry.getRenamedAssertions(numThreads));
			}
		}
		return finalAssertions;
	}

	private static List<List<Tokenizable>> generateSeedAndThreadsAssertions(List<List<Tokenizable>> finalizedSeeds,
			HashMap<Symbol, Set<DependenceEntry>> mapFromSymbolsToDependenceEntrySets, IdOrConstToken numThreads) {
		List<List<Tokenizable>> finalAssertions = new ArrayList<>();
		assert (!finalizedSeeds.stream().anyMatch(fe -> fe.isEmpty()));
		finalAssertions.addAll(finalizedSeeds);
		Set<IdOrConstToken> tidSet = new HashSet<>();
		for (Symbol sym : mapFromSymbolsToDependenceEntrySets.keySet()) {
			for (DependenceEntry depEntry : mapFromSymbolsToDependenceEntrySets.get(sym)) {
				tidSet.add(depEntry.getTid());
			}
		}
		/*
		 * numThreads > 0
		 */
		IdOrConstToken zeroToken = IdOrConstToken.getNewConstantToken("0");
		List<Tokenizable> tidConstraint = new ArrayList<>();
		tidConstraint.add(OperatorToken.GT);
		tidConstraint.add(numThreads);
		tidConstraint.add(zeroToken);
		finalAssertions.add(tidConstraint);

		/*
		 * NOTE: Next assertion is unsound (for unbounded number of threads).
		 * TODO: Think about this constraint below.
		 * When added alongside the imprecise-yet-efficient assertion for the
		 * size of the last chunk, the solver time gets reduced from 17s to
		 * 2.7s, for 64 threads.
		 * For 11s, the time gets reduced from 17s to 0.35s.
		 */
		// IdOrConstToken eightToken = IdOrConstToken.getNewConstantToken("13");
		IdOrConstToken eightToken = IdOrConstToken.getNewConstantToken(Program.maxThreads + "");
		tidConstraint = new ArrayList<>();
		tidConstraint.add(OperatorToken.LE);
		tidConstraint.add(numThreads);
		tidConstraint.add(eightToken);
		finalAssertions.add(tidConstraint);

		/*
		 * Add tid >= 0, and tid < numThreads, for all tids.
		 */
		for (IdOrConstToken thread : tidSet) {
			if (thread.isAConstant()) {
				continue;
			}
			tidConstraint = new ArrayList<>();
			tidConstraint.add(OperatorToken.GE);
			tidConstraint.add(thread);
			tidConstraint.add(zeroToken);
			finalAssertions.add(tidConstraint);

			tidConstraint = new ArrayList<>();
			tidConstraint.add(OperatorToken.LT);
			tidConstraint.add(thread);
			tidConstraint.add(numThreads);
			finalAssertions.add(tidConstraint);
		}
		/*
		 * For all i, j, tid_i != tid_j
		 */
		List<IdOrConstToken> tidList = new ArrayList<>(tidSet);
		for (int i = 0; i < tidList.size() - 1; i++) {
			IdOrConstToken tid1 = tidList.get(i);
			if (tid1.isAConstant()) {
				continue;
			}
			for (int j = i + 1; j < tidList.size(); j++) {
				IdOrConstToken tid2 = tidList.get(j);
				if (tid2.isAConstant()) {
					continue;
				}
				tidConstraint = new ArrayList<>();
				tidConstraint.add(OperatorToken.ISNEQ);
				tidConstraint.add(tid1);
				tidConstraint.add(tid2);
				finalAssertions.add(tidConstraint);
			}
		}
		return finalAssertions;
	}

	/**
	 * Checks whether the provided list of assertions (in prefix form) can form
	 * a valid system of (in)equations for Z3 solver.
	 * We consider a system of (in)equations to be valid, if:
	 * .. 1. Each equation is conjunction of the form "= x e", where "x" is an
	 * ......identifier, and "e" does not contain any side-effects.
	 * .. 2. There are no cyclic dependences among the variables in the system.
	 * 
	 * @param finalAssertions
	 *                        a list of assertions in prefix form.
	 * 
	 * @return
	 *         {@code true}, if {@code assertionList} represents a valid system
	 *         of (in)equations for Z3 solver.
	 */
	public static boolean validateSystemOfEquations(List<List<Tokenizable>> finalAssertions) {
		if (finalAssertions == null || finalAssertions.isEmpty()) {
			return true;
		}
		if (!ConstraintsGenerator.hasConjunctiveEqualityOrRelationalForm(finalAssertions)) {
			return false;
		}
		HashMap<String, Z3VariableNode> allZ3Nodes = Z3VariableNode.getZ3VariableGraph(finalAssertions);
		Set<List<String>> cycleList = Z3VariableNode.getAllCycles(allZ3Nodes.values());
		if (!cycleList.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Checks whether all entries in {@code assertions} are conjunctions of
	 * equality expressions (in prefix form), of the form {@code op x e}, where
	 * neither {@code x}, nor {@code e} contain any side-effects, and {@code op}
	 * is either equality or a relational operator.
	 * 
	 * @param assertions
	 *                   a list of assertions.
	 * @return
	 *         true, if all entries in {@code assertions} are conjunctions of
	 *         equality expressions (in prefix form), of the form {@code = x e},
	 *         where neither {@code x}, nor {@code e} contain any side-effects,
	 *         and {@code op} is either equality or a relational operator.
	 */
	public static boolean hasConjunctiveEqualityOrRelationalForm(List<List<Tokenizable>> assertions) {
		for (List<Tokenizable> assertion : assertions) {
			int index = -1;
			for (Tokenizable token : assertion) {
				index++;
				if (token == OperatorToken.ASSIGN) {
					List<Tokenizable> subList = assertion.subList(index, assertion.size());
					List<Tokenizable> firstOperand = ExpressionTokenizer.getFirstOperand(subList);
					List<Tokenizable> secondOperand = ExpressionTokenizer.getSecondOperand(subList);
					if (firstOperand == null || firstOperand.isEmpty()
							|| (firstOperand.size() != 1 && firstOperand.get(0) != OperatorToken.MOD)) {
						return false;
					}
					if (ExpressionTokenizer.hasSideEffects(firstOperand)) {
						return false;
					}
					if (ExpressionTokenizer.hasSideEffects(secondOperand)) {
						return false;
					}
				} else if (token == OperatorToken.PRE_DEC || token == OperatorToken.PRE_INC
						|| token == OperatorToken.POST_DEC || token == OperatorToken.POST_INC) {
					return false;
				}
			}
		}
		return true;
	}

	public static Expr getZ3Expression(List<Tokenizable> assertion, HashMap<String, ArithExpr> idMap, Context c) {
		if (assertion.isEmpty()) {
			return null;
		}
		Tokenizable first = assertion.get(0);
		assertion.remove(first);
		if (first instanceof IdOrConstToken) {
			IdOrConstToken spe = (IdOrConstToken) first;
			if (spe.isAnIdentifier()) {
				String idString = spe.toString().trim();
				ArithExpr ret = idMap.get(idString);
				if (ret == null) {
					Misc.exitDueToError("No mapping found for identifier " + idString
							+ " while trying to generate a Z3 expression.");
				}
				return ret;
			} else if (spe.isAConstant()) {
				Constant constant = spe.getConstant();
				Node constNode = constant.getF0().getChoice();
				if (constNode instanceof NodeToken) {
					NodeToken constToken = (NodeToken) constNode;
					if (constToken.getKind() == CParserConstants.INTEGER_LITERAL) {
						Integer integer = Integer.parseInt(constToken.toString().trim());
						return c.mkInt(integer);
					} else if (constToken.getKind() == CParserConstants.FLOATING_POINT_LITERAL) {
						return c.mkReal(constToken.toString().trim());
					} else if (constToken.getKind() == CParserConstants.CHARACTER_LITERAL) {
						return null; // No Z3 expression for character constants.
					} else {
						assert (false);
						return null;
					}
				} else {
					return null; // No Z3 expression for strings.
				}
			} else {
				assert (false);
				return null;
			}
		} else {
			// Token "first" is an operator.
			OperatorToken op = (OperatorToken) first;
			Expr operand1 = null;
			Expr operand2 = null;
			Expr operand3 = null;
			try {
				switch (op) {
				case AMP:
					return null;
				case ASSIGN:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					Sort op1Sort = operand1.getSort();
					Sort op2Sort = operand2.getSort();
					if (!op1Sort.equals(op2Sort)) {
						if (op1Sort.equals(c.getBoolSort()) && op2Sort.equals(c.mkIntSort())) {
							// Convert op2 to bool.
							operand2 = c.mkITE(c.mkNot(c.mkEq(operand2, c.mkIntConst("0"))), c.mkTrue(), c.mkFalse());
						} else if (op2Sort.equals(c.getBoolSort()) && op1Sort.equals(c.mkIntSort())) {
							// Convert op1 to bool.
							operand1 = c.mkITE(c.mkNot(c.mkEq(operand1, c.mkIntConst("0"))), c.mkTrue(), c.mkFalse());
						}
					}
					return c.mkEq(operand1, operand2);
				case ITE:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand3 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null || operand3 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null && operand3 != null);
					return c.mkITE((BoolExpr) operand1, operand2, operand3);
				case LOGIC_OR:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkOr((BoolExpr) operand1, (BoolExpr) operand2);
				case BIT_AND:
					return null;
				case BIT_NEGATION:
					return null;
				case BIT_OR:
					return null;
				case BIT_XOR:
					return null;
				case CP:
					if (operand1 == null || operand2 == null) {
						return null;
					}
				case DIV:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkDiv((ArithExpr) operand1, (ArithExpr) operand2);
				case GE:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkGe((ArithExpr) operand1, (ArithExpr) operand2);
				case GT:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkGt((ArithExpr) operand1, (ArithExpr) operand2);
				case ISEQ:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkEq(operand1, operand2);
				case ISNEQ:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					BoolExpr eqExpr = c.mkEq(operand1, operand2);
					return c.mkNot(eqExpr);
				case LE:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkLe((ArithExpr) operand1, (ArithExpr) operand2);
				case LT:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkLt((ArithExpr) operand1, (ArithExpr) operand2);
				case MINUS:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkSub((ArithExpr) operand1, (ArithExpr) operand2);
				case MOD:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkMod((IntExpr) operand1, (IntExpr) operand2);
				case MUL:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkMul((ArithExpr) operand1, (ArithExpr) operand2);
				case NOT:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null) {
						return null;
					}
					assert (operand1 != null);
					return c.mkNot((BoolExpr) operand1);
				case OP:
					assert (false);
					return null;
				case PLUS:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkAdd((ArithExpr) operand1, (ArithExpr) operand2);
				case POST_DEC:
					return null;
				case POST_INC:
					return null;
				case PRE_DEC:
					return null;
				case PRE_INC:
					return null;
				case PTR:
					return null;
				case SL:
					return null;
				case SR:
					return null;
				case UNARY_MINUS:
					return ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
				case UNARY_PLUS:
					operand1 = c.mkIntConst("0");
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkSub((ArithExpr) operand1, (ArithExpr) operand2);
				case POW:
					operand1 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					operand2 = ConstraintsGenerator.getZ3Expression(assertion, idMap, c);
					if (operand1 == null || operand2 == null) {
						return null;
					}
					assert (operand1 != null && operand2 != null);
					return c.mkPower((ArithExpr) operand1, (ArithExpr) operand2);
				default:
					assert (false);
					return null;
				}
			} catch (ClassCastException e) {
				Misc.exitDueToError("Type mismatch error during Z3 expression creation.");
			} catch (NullPointerException e) {
				e.printStackTrace();
				Misc.exitDueToError("Input form for Z3 expression creation is incorrect.");
			}
		}
		return null;
	}
}
