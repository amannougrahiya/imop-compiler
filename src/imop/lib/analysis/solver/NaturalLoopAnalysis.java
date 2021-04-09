/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.solver;

import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Definition;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.solver.tokens.ExpressionTokenizer;
import imop.lib.analysis.solver.tokens.IdOrConstToken;
import imop.lib.analysis.solver.tokens.OperatorToken;
import imop.lib.analysis.solver.tokens.Tokenizable;
import imop.lib.analysis.typeSystem.ArithmeticType;
import imop.lib.util.CellList;
import imop.lib.util.CollectorVisitor;
import imop.lib.util.Misc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class contains static methods that are used to analyze natural loops.
 * 
 * @author Aman Nougrahiya
 *
 */
public class NaturalLoopAnalysis {

	public static boolean debugMode = false; // For prints to STDOUT.

	/**
	 * Given a natural loop, obtain the induction range associated with basic
	 * induction variables of the loop.
	 * 
	 * @param loopHeader
	 *                     head of the back-edge.
	 * @param loopTail
	 *                     tail of the back-edge.
	 * @param loopContents
	 *                     contents of the loop.
	 * @return
	 *         a map from symbols (that are basic induction variables of the
	 *         loop), to their symbolic value ranges.
	 */
	public static HashMap<Symbol, InductionRange> getBasicInductionValueRange(Node loopHeader, Node loopTail,
			Set<Node> loopContents) {
		HashMap<Symbol, InductionRange> indRange = new HashMap<>();
		HashMap<Symbol, List<Tokenizable>> basicInductionVariables = NaturalLoopAnalysis
				.getBasicInductionVariablesWithStepDefinition(loopHeader, loopTail, loopContents);
		/*
		 * Step 1: Find all exit predicates of the loop.
		 */
		Set<Node> exitPredicates = NaturalLoopAnalysis.getExitPredicates(loopHeader, loopTail, loopContents);
		if (NaturalLoopAnalysis.debugMode) {
			System.out.println("\tExit predicates: " + exitPredicates);
		}
		/*
		 * Step 2: Expand the exit predicates, and see if they are a value check
		 * for any of the induction variables. If so, then use them to create
		 * the induction range for the variable.
		 */
		if (exitPredicates.size() == 1) {
			Node predicate = Misc.getAnyElement(exitPredicates);
			List<Tokenizable> predicateList = ExpressionTokenizer.getPrefixTokens(predicate);
			if (predicateList != null && !predicateList.isEmpty()) {
				Set<Symbol> dontExpandSymbols = new HashSet<>();
				dontExpandSymbols.addAll(basicInductionVariables.keySet());
				List<Tokenizable> expandedPredicate = ExpressionTokenizer.getUniqueExpansion(predicateList,
						dontExpandSymbols);
				/*
				 * Check if expandedPredicate is of the form
				 * "</>/<=/>=/!= biv e" (or with biv and e interchanged), for
				 * some expression "e" which is a loop constant, and some basic
				 * induction variable "biv".
				 */
				Tokenizable finalOperatorToken = expandedPredicate.get(0);
				if (finalOperatorToken == OperatorToken.LE || finalOperatorToken == OperatorToken.GE
						|| finalOperatorToken == OperatorToken.LT || finalOperatorToken == OperatorToken.GT
						|| finalOperatorToken == OperatorToken.ISNEQ) {
					OperatorToken finalOperator = (OperatorToken) finalOperatorToken;
					List<Tokenizable> firstOperand = ExpressionTokenizer.getFirstOperand(expandedPredicate);
					List<Tokenizable> secondOperand = ExpressionTokenizer.getSecondOperand(expandedPredicate);
					IdOrConstToken id = null;
					List<Tokenizable> finalValue = null;
					if (firstOperand.size() == 1) {
						Tokenizable firstOp = firstOperand.get(0);
						if (basicInductionVariables.keySet().stream()
								.anyMatch(biv -> firstOp.toString().trim().equals(biv.toString().trim()))) {
							id = (IdOrConstToken) firstOp;
							finalValue = secondOperand;
						}
					}
					if (id == null && secondOperand.size() == 1) {
						Tokenizable secondOp = secondOperand.get(0);
						if (basicInductionVariables.keySet().stream()
								.anyMatch(biv -> secondOp.toString().trim().equals(biv.toString().trim()))) {
							id = (IdOrConstToken) secondOp;
							finalValue = firstOperand;
						}
					}
					if (id != null && NaturalLoopAnalysis.isLoopConstant(finalValue, loopContents)) {
						Symbol sym = Misc.getSymbolEntry(id.toString(), id.getCfgLeaf());
						/*
						 * Now, finalVal is the one of the bounds on the
						 * induction variable "sym".
						 * We need to obtain the another bound as well.
						 */
						Node cfgLeaf = id.getCfgLeaf();
						Set<Definition> reachingDefs = cfgLeaf.getInfo().getReachingDefinitions(sym);
						if (reachingDefs.size() == 2) {
							/*
							 * Obtain that reaching definition which is not
							 * recursive.
							 */
							List<Tokenizable> initValue = null;
							rdLoop: for (Definition rdDef : reachingDefs) {
								Node rd = rdDef.getDefiningNode();
								if (loopContents.contains(rd)) {
									// Ignore the definition within this loop.
									continue;
								}
								List<Tokenizable> prefixDef = ExpressionTokenizer.getAssigningExpression(rd);
								HashMap<IdOrConstToken, List<Tokenizable>> normalizedMap = ExpressionTokenizer
										.getNormalizedForm(prefixDef);
								for (IdOrConstToken tempId : normalizedMap.keySet()) {
									if (tempId.toString().trim().equals(sym.toString().trim())) {
										initValue = normalizedMap.get(tempId);
										break rdLoop;
									}
								}
							}
							/*
							 * Finally, create and save the induction range.
							 */
							if (initValue != null) {
								// && NaturalLoopAnalysis.isLoopConstant(initPrefixRHS, loopContents)) {}
								/*
								 * Expand the initRHS, to get better precision,
								 * if possible.
								 */
								List<Tokenizable> tempInit = ExpressionTokenizer.getUniqueExpansion(initValue,
										dontExpandSymbols);
								if (!tempInit.isEmpty()) {
									initValue = tempInit;
								}
								/*
								 * Obtain stepOperator and stepValue.
								 */
								List<Tokenizable> updateValExpression = basicInductionVariables.get(sym);
								Tokenizable stepOperatorToken = updateValExpression.get(0);
								OperatorToken stepOperator = null;
								if (stepOperatorToken == OperatorToken.PLUS
										|| stepOperatorToken == OperatorToken.MINUS) {
									stepOperator = (OperatorToken) stepOperatorToken;
								}
								if (stepOperatorToken != null) {
									List<Tokenizable> stepFirstOperand = ExpressionTokenizer
											.getFirstOperand(updateValExpression);
									List<Tokenizable> stepSecondOperand = ExpressionTokenizer
											.getSecondOperand(updateValExpression);
									IdOrConstToken stepId = null;
									List<Tokenizable> stepValue = null;
									if (stepFirstOperand.size() == 1) {
										Tokenizable firstOp = stepFirstOperand.get(0);
										if (firstOp.toString().trim().equals(sym.toString().trim())) {
											stepId = (IdOrConstToken) firstOp;
											stepValue = stepSecondOperand;
										}
									}
									if (stepId == null && stepSecondOperand.size() == 1) {
										Tokenizable secondOp = stepSecondOperand.get(0);
										if (secondOp.toString().trim().equals(sym.toString().trim())) {
											stepId = (IdOrConstToken) secondOp;
											stepValue = stepFirstOperand;
										}
									}
									if (stepValue != null && !stepValue.isEmpty()) {
										if (NaturalLoopAnalysis.debugMode) {
											System.out.println("\tSo far: \n\t\tTermCFGLeaf: " + cfgLeaf + "\n\t\tBIV: "
													+ id + "\n\t\tTERMLIST: " + stepValue + "\n\t\tUPLIST: "
													+ updateValExpression + "\n\t\tINITLIST: " + initValue);
										}
										/*
										 * Create and save the induction range.
										 */
										indRange.put(sym, new InductionRange(sym, initValue, stepOperator, stepValue,
												finalOperator, finalValue, loopHeader, loopTail, stepId.getCfgLeaf()));
									}
								}
							}
						}
					}
				}
			}
		} else {
			/*
			 * For now, we do nothing. If we have more than one exit-predicates,
			 * we should consider them only if they give same induction range.
			 * Note: Add this logic later, if and when required. (Simply
			 * modularize the above branch, and reuse.)
			 */
		}
		return indRange;
	}

	/**
	 * Given a loop header, this method collects all basic induction variables
	 * of the associated loop.
	 * A Symbol is a basic induction variable of the loop iff there exists only
	 * one definition of that variable in the loop, and
	 * that definition is executed exactly once in each iteration of the loop,
	 * and when expanded, it is of the form "i = i + e", where e is a loop
	 * constant.
	 * 
	 * @param loopHeader
	 *                     header of the loop under inspection.
	 * @param loopTail
	 *                     a predecessor of the loop header.
	 * @param loopContents
	 *                     set of all the nodes in the loop under inspection.
	 * @return
	 *         set of symbols that form the basic induction variables for the
	 *         loop associated with {@code loopHeader}, along with information
	 *         about its definition.
	 */
	public static HashMap<Symbol, List<Tokenizable>> getBasicInductionVariablesWithStepDefinition(Node loopHeader,
			Node loopTail, Set<Node> loopContents) {
		/*
		 * Step 1: Collect all the symbols that have exactly one definition in
		 * the loop.
		 */
		Set<Symbol> exactlyOnce = new HashSet<>();
		Set<Symbol> removed = new HashSet<>();
		HashMap<Symbol, Node> internalDefinitions = new HashMap<>();
		for (Node n : loopContents) {
			CellList writesOfN = n.getInfo().getWrites();
			for (Cell c : writesOfN) {
				if (c instanceof Symbol) {
					Symbol sym = (Symbol) c;
					if (exactlyOnce.contains(sym)) {
						exactlyOnce.remove(sym);
						removed.add(sym);
						internalDefinitions.remove(sym);
					} else if (!removed.contains(sym)) {
						internalDefinitions.put(sym, n);
						exactlyOnce.add(sym);
					}
				}
			}
		}
		if (NaturalLoopAnalysis.debugMode) {
			System.out.println("\tSymbols that are written exactly once inside the node: " + internalDefinitions);
		}
		/*
		 * Step 2: Remove all those symbols that are not of integer type.
		 */
		Set<Symbol> toBeRemoved = new HashSet<>();
		for (Symbol sym : internalDefinitions.keySet()) {
			if (!(sym.getType() instanceof ArithmeticType)) {
				toBeRemoved.add(sym);
			}
		}
		for (Symbol sym : toBeRemoved) {
			internalDefinitions.remove(sym);
		}

		/*
		 * Step 3: Collect all those definitions are are executed exactly
		 * once in each iteration of the loop.
		 * Given the back-edge loopTail -> loopHeader, and a definition def, the
		 * def would be executed:
		 * -- at least once in each iteration, if there does not exist any path
		 * from loopHeader to loopTail that doesn't pass through def; for this,
		 * we need to ensure that def is a dominator of loopTail.
		 * -- not more than once in each iteration, if there does not exist any
		 * path from def to itself without passing through the back-edge.
		 */
		HashMap<Symbol, Node> singleInstances = new HashMap<>();
		for (Symbol sym : internalDefinitions.keySet()) {
			Node def = internalDefinitions.get(sym);
			boolean atLeastOnce = loopTail.getInfo().getDominators().contains(def);
			if (!atLeastOnce) {
				/*
				 * There is no guarantee that def would be executed in all the
				 * iterations.
				 */
				continue;
			}
			Set<Node> endPoints = new HashSet<>();
			CollectorVisitor.collectNodesIntraTaskBackward(def, endPoints, n -> (n == def || n == loopHeader));
			boolean moreThanOnce = endPoints.contains(def);
			if (moreThanOnce) {
				/*
				 * It may happen that multiple copies of def may get executed
				 * within one iteration.
				 */
				continue;
			} else {
				singleInstances.put(sym, def);
			}
		}
		internalDefinitions = null;
		if (NaturalLoopAnalysis.debugMode) {
			System.out.println("\tSymbols whose sole definition within the loop would be exeucted exactly once: "
					+ singleInstances.keySet());
		}

		/*
		 * Step 4: Remove side-effects from all the definitions, and express
		 * them in prefix form; simplify defs of the form "a = b++ + --c;"
		 */
		HashMap<Symbol, List<Tokenizable>> normalizedMap = new HashMap<>();
		for (Symbol sym : singleInstances.keySet()) {
			Node def = singleInstances.get(sym);
			List<Tokenizable> prefixForm = ExpressionTokenizer.getAssigningExpression(def);
			HashMap<IdOrConstToken, List<Tokenizable>> defMap = ExpressionTokenizer.getNormalizedForm(prefixForm);
			for (IdOrConstToken idToken : defMap.keySet()) {
				Symbol finalSym = Misc.getSymbolEntry(idToken.getIdentifier().toString(), idToken.getCfgLeaf());
				normalizedMap.put(finalSym, defMap.get(idToken));
			}
		}

		/*
		 * Step 5: Expand the definitions; delete all those definitions which
		 * are not linear inductions of the form "i = i +/- e" or
		 * "i = e +/- i", where e is a "loop constant";
		 */
		Set<Symbol> removeMapping = new HashSet<>();
		for (Symbol sym : normalizedMap.keySet()) {
			Set<Symbol> readSymbols = new HashSet<>();
			readSymbols.add(sym);
			List<Tokenizable> expandedForm = ExpressionTokenizer.getUniqueExpansion(normalizedMap.get(sym),
					readSymbols);

			if (expandedForm.size() < 3) {
				removeMapping.add(sym);
				continue;
			}
			Tokenizable firstToken = expandedForm.get(0);
			if (firstToken != OperatorToken.PLUS && firstToken != OperatorToken.MINUS) {
				removeMapping.add(sym);
				continue;
			}
			List<Tokenizable> firstOperand = ExpressionTokenizer.getFirstOperand(expandedForm);
			List<Tokenizable> secondOperand = ExpressionTokenizer.getSecondOperand(expandedForm);
			IdOrConstToken id = null;
			List<Tokenizable> eList = null;
			if (firstOperand.size() == 1) {
				Tokenizable firstOp = firstOperand.get(0);
				if (firstOp.toString().trim().equals(sym.toString().trim())) {
					id = (IdOrConstToken) firstOp;
					eList = secondOperand;
				}
			}
			if (id == null && secondOperand.size() == 1) {
				Tokenizable secondOp = secondOperand.get(0);
				if (secondOp.toString().trim().equals(sym.toString().trim())) {
					id = (IdOrConstToken) secondOp;
					eList = firstOperand;
				}
			}
			if (id == null || eList == null || !NaturalLoopAnalysis.isLoopConstant(eList, loopContents)) {
				removeMapping.add(sym);
			}

			// Old code.
			// Tokenizable secondToken = expandedForm.get(1);
			// if (!(secondToken instanceof IdOrConstToken)) {
			// removeMapping.add(sym);
			// continue;
			// } else {
			// IdOrConstToken idOrC = (IdOrConstToken) secondToken;
			// if (idOrC.isAConstant()) {
			// // Now, third token must be same as sym.
			// Tokenizable thirdToken = expandedForm.get(2);
			// if (!thirdToken.toString().trim().equals(sym.getName())) {
			// removeMapping.add(sym);
			// continue;
			// }
			// } else {
			// if (!secondToken.toString().trim().equals(sym.getName())) {
			// removeMapping.add(sym);
			// continue;
			// } else {
			// // Now, third token must be some constant.
			// Tokenizable thirdToken = expandedForm.get(2);
			// if (!(thirdToken instanceof IdOrConstToken)) {
			// removeMapping.add(sym);
			// continue;
			// } else {
			// IdOrConstToken thirdIdOrC = (IdOrConstToken) thirdToken;
			// if (!thirdIdOrC.isAConstant()) {
			// removeMapping.add(sym);
			// continue;
			// }
			// }
			// }
			// }
			// }
			if (NaturalLoopAnalysis.debugMode) {
				System.out.println("\tExpanded form of rhs-exp for def of basic induction variable " + sym + " is: "
						+ expandedForm);
			}
		}
		for (Symbol remSym : removeMapping) {
			normalizedMap.remove(remSym);
		}

		return normalizedMap;
	}

	/**
	 * Given a list of tokens in prefix form, this method checks whether the
	 * list is "loop-invariant".
	 * 
	 * @param loopContents
	 *                     contents of the loop to be tested.
	 * @return
	 *         {@code true}, if the given list is a loop-invariant.
	 */
	public static boolean isLoopConstant(List<Tokenizable> tokens, Set<Node> loopContents) {
		if (ExpressionTokenizer.hasSideEffects(tokens)) {
			return false;
		}
		Set<Symbol> symList = ExpressionTokenizer.getSymbolSet(tokens);
		for (Node content : loopContents) {
			for (Cell write : content.getInfo().getWrites()) {
				if (symList.contains(write)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Obtain all the exit predicates of the natural loop associated with the
	 * given back-edge.
	 * <br>
	 * Note that due to the nicety in the strucutre of a natural loop, we just
	 * need to obtain all those {@code Expression} (or similar) leaf nodes whose
	 * one of the
	 * successors is not present in the {@code loopContents}.
	 * 
	 * @param loopHeader
	 *                     head of the back-edge.
	 * @param loopTail
	 *                     tail of the back-edge.
	 * @param loopContents
	 *                     leaf contents of the loop.
	 * @return
	 *         a set of all the exit predicates of the natural loop associated
	 *         with the given back-edge.
	 */
	public static Set<Node> getExitPredicates(Node loopHeader, Node loopTail, Set<Node> loopContents) {
		Set<Node> exitPredicates = new HashSet<>();
		for (Node node : loopContents) {
			if (!Misc.isAPredicate(node)) {
				continue;
			}
			if (node.getInfo().getCFGInfo().getInterProceduralLeafSuccessors().stream()
					.anyMatch(n -> !loopContents.contains(n))) {
				exitPredicates.add(node);
			}
		}
		return exitPredicates;
	}

	/**
	 * Obtain all loop-headers that are present within the given node.
	 * 
	 * @param node
	 *             node that has to be inspected
	 * @return
	 *         loop-headers of natural loops within this node.
	 */
	public static Set<Node> getLoopHeadersFromCFG(Node node) {
		Node cfgNode = Misc.getCFGNodeFor(node);
		Set<Node> loopHeaders = new HashSet<>();
		for (Node leaf : cfgNode.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			for (Node pred : leaf.getInfo().getCFGInfo().getInterProceduralLeafPredecessors()) {
				if (pred.getInfo().getDominators().contains(leaf)) {
					loopHeaders.add(leaf);
					break;
				}
			}
		}
		return loopHeaders;
	}

	/**
	 * Assuming that the given {@code node} is a loop header, this method
	 * returns the set of all those nodes that may be contained in its natural
	 * loop.
	 * 
	 * @param loopTail
	 *                 tail of the loop's back-edge.
	 * @return
	 *         leaf nodes present within the natural loop of which {@code node}
	 *         is the loop header.
	 */
	public static Set<Node> getLoopContentsOfThisLoopHeader(Node node, Node loopTail) {
		Node cfgNode = Misc.getCFGNodeFor(node);
		if (!loopTail.getInfo().getDominators().contains(cfgNode)) {
			assert (false);
			return null;
		}
		Node startPoint = loopTail;
		Set<Node> endPoints = new HashSet<>();
		Set<Node> collectedNodes = CollectorVisitor.collectNodesIntraTaskBackward(startPoint, endPoints,
				n -> n == cfgNode);
		collectedNodes.add(startPoint);
		collectedNodes.addAll(endPoints);
		return collectedNodes;
	}

	/**
	 * Obtain set of all those predecessors of {@code loopHeader} that form the
	 * tail of a back-edge that terminates at the {@code loopHeader}.
	 * 
	 * @param loopHeader
	 *                   head of a back-edge.
	 * @return
	 *         set of all tails of the back-edges of various loops associated
	 *         with the given {@code loopHeader}.
	 */
	public static Set<Node> getLoopTails(Node loopHeader) {
		Set<Node> loopTails = new HashSet<>();
		for (Node pred : loopHeader.getInfo().getCFGInfo().getInterProceduralLeafPredecessors()) {
			if (pred.getInfo().getDominators().contains(loopHeader)) {
				loopTails.add(pred);
			}
		}
		return loopTails;
	}

}
