/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis;

import imop.ast.info.DataSharingAttribute;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.*;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.analysis.typeSystem.FunctionType;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.util.CellList;
import imop.lib.util.Misc;
import imop.lib.util.NodePair;
import imop.parser.Program;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SVEChecker {

	/**
	 * Names of functions that may return different values for same arguments.
	 * (These functions are not functions,
	 * mathematically speaking.)
	 */
	public static final Set<String> variableFunctions = new HashSet<>();

	static {
		/*
		 * TODO: Fill up this list.
		 */
		variableFunctions.add("omp_get_thread_num");
		variableFunctions.add("gettimeofday");
		// variableFunctions.add("malloc"); // Add this and other variants.
	}

	private static Set<Node> singleValuedExpressions = new HashSet<>();
	private static Set<Node> multiValuedExpressions = new HashSet<>();

	private static boolean disable = true;

	public static long sveTimer = 0;

	public static void resetStaticFields() {
		SVEChecker.sveTimer = 0;
		SVEChecker.singleValuedExpressions.clear();
		SVEChecker.multiValuedExpressions.clear();
		CoExistenceChecker.resetStaticFields();
	}

	/**
	 * Checks if the given expression is single-valued in all its phases.
	 *
	 * @param exp
	 *            the given expression
	 *
	 * @return true, if the given expression is single-valued.
	 */
	public static boolean isSingleValuedPredicate(Expression exp) {
		if (Program.sveNoCheck) {
			return true;
		}
		long timer = System.nanoTime();
		if (!Misc.isAPredicate(exp)) {
			SVEChecker.sveTimer += (System.nanoTime() - timer);
			return false;
		} else {
			boolean retVal = SVEChecker.isSingleValuedPredicate(exp, new HashSet<>(), new HashSet<>());
			SVEChecker.sveTimer += (System.nanoTime() - timer);
			return retVal;
		}
	}

	static boolean isSingleValuedPredicate(Expression exp, Set<Expression> expSet, Set<NodePair> nodePairs) {
		if (disable) {
			Node parent = exp.getParent();
			if (parent instanceof IfStatement || parent instanceof SwitchStatement) {
				// Consider all conditional predicates to be an SVE.
				return true;
			} else if (parent instanceof WhileStatement || parent instanceof DoStatement) {
				// Implicit valuation of parent.
			} else if (parent instanceof NodeOptional) {
				Node grandParent = parent.getParent();
				if (grandParent instanceof ForStatement) {
					ForStatement forStmt = (ForStatement) grandParent;
					if (forStmt.getInfo().getCFGInfo().getTerminationExpression() == exp) {
						parent = forStmt;
					}
				}
			}
			if (parent != null) {
				if (exp.getInfo().getNodePhaseInfo().getPhaseSet().size() == 1) {
					return false;
				}
				if (!Misc.getExactEnclosee(parent, BarrierDirective.class).isEmpty()) {
					return true;
				} else {
					return false;
				}
			}
		}
		if (exp == null) {
			return false;
		}
		if (expSet.contains(exp)) {
			// System.out.println(exp + " is recursively an SVE.");
			// SVEChecker.singleValuedExpressions.add(exp);
			return true;
			// System.out.println(exp + " is recursively an MVE.");
			// SVEChecker.multiValuedExpressions.add(exp);
			// return false;
		} else if (SVEChecker.singleValuedExpressions.contains(exp)) {
			return true;
		} else if (SVEChecker.multiValuedExpressions.contains(exp)) {
			return false;
		} else {
			expSet.add(exp);
		}

		/*
		 * If this expression does not read from any variable, all threads
		 * will evaluate it to same value.
		 * (Clearly, all writes, if any, would take values from constants.)
		 */
		CellList readList = exp.getInfo().getReads();
		if (readList.isEmpty()) {
			expSet.remove(exp);
			SVEChecker.singleValuedExpressions.add(exp);
			return true;
		}

		/*
		 * If this expression reads and writes to the same variable, OR writes
		 * to more than one variable, then, conservatively, we assume it to be
		 * an MVE.
		 */
		CellList writeList = exp.getInfo().getWrites();
		if (writeList.size() > 1) {
			expSet.remove(exp);
			SVEChecker.multiValuedExpressions.add(exp);
			return false;
		}
		// Old code: This rule is too imprecise and unnecessary.
		// if (Misc.doIntersect(readList, writeList)) {
		// expSet.remove(exp);
		// SVEChecker.multiValuedExpressions.add(exp);
		// return false;
		// }

		for (Cell r : readList) {
			if (r == Cell.getNullCell()) {
				continue;
			} else if (r instanceof AddressCell) {
				/*
				 * All threads will read same values for address of shared
				 * variables, and not for private variables.
				 */
				Cell pointedElem = ((AddressCell) r).getPointedElement();
				if (exp.getInfo().getSharingAttribute(pointedElem) == DataSharingAttribute.SHARED) {
					continue;
				} else {
					expSet.remove(exp);
					SVEChecker.multiValuedExpressions.add(exp);
					return false;
				}
			} else if (r instanceof FieldCell) {
				/*
				 * Since "r" is a summary node, let's assume that different
				 * threads may read different locations within "r".
				 */
				expSet.remove(exp);
				SVEChecker.multiValuedExpressions.add(exp);
				return false;
			} else if (r instanceof HeapCell) {
				/*
				 * Since "r" is a summary node, let's assume that different
				 * threads may read different locations within "r".
				 */
				expSet.remove(exp);
				SVEChecker.multiValuedExpressions.add(exp);
				return false;
			} else if (r instanceof FreeVariable) {
				expSet.remove(exp);
				SVEChecker.multiValuedExpressions.add(exp);
				return false;
			}
			Symbol variable = (Symbol) r;
			Type varType = variable.getType();
			if (varType != null && varType instanceof FunctionType) {
				continue;
			}
			/*
			 * If the variable is an array, then note that its value (i.e., the
			 * address of its first element)
			 * would remain same for all threads if it is a shared variable.
			 * Otherwise, if the variable is private, then its address would be
			 * different for different threads.
			 */
			if (exp.getInfo().getSharingAttribute(variable) == DataSharingAttribute.SHARED) {
				if (variable.getType() instanceof ArrayType) {
					continue;
				}
				/*
				 * If there exists any write to this variable in any statements
				 * in the phases in which $exp$ may execute, then different
				 * threads may see different values of exp (old or new).
				 */
				if (CoExistenceChecker.isWrittenInPhase(exp, variable, expSet, nodePairs)) {
					expSet.remove(exp);
					// System.out.println(exp + " is an MVE since it is a shared variable written in
					// the current phase.");
					SVEChecker.multiValuedExpressions.add(exp);
					return false;
				} else {
					continue;
				}
			} else {
				// When the variable is private at the predicate.
				if (variable.getType() instanceof ArrayType) {
					expSet.remove(exp);
					SVEChecker.multiValuedExpressions.add(exp);
					return false;
				}
				if (!ensureSameValue(variable, exp, expSet, nodePairs)) {
					expSet.remove(exp);
					// System.out.println(exp + " is an MVE since the reaching definition(s) of " +
					// variable
					// + " do(es) not write single-value for all threads.");
					SVEChecker.multiValuedExpressions.add(exp);
					return false;
				} else {
					continue;
				}
			}
		}

		expSet.remove(exp);
		// System.out.println(exp + " is an SVE.");
		SVEChecker.singleValuedExpressions.add(exp);
		return true;
	}

	/**
	 * Checks if all threads will read the same value of private variable
	 * {@code sym} at the node {@code expression}.
	 *
	 * @param sym
	 * @param expression
	 * @param expSet
	 * @param nodePairs
	 *
	 * @return true, if all threads will read same value for the private variable
	 *         {@code sym} at the node {@code
	 * expression}; false otherwise.
	 */
	public static boolean ensureSameValue(Symbol sym, Node exp, Set<Expression> expSet, Set<NodePair> nodePairs) {
		Node cfgNode = Misc.getCFGNodeFor(exp);
		Set<Definition> reachingDefinitions = cfgNode.getInfo().getReachingDefinitions(sym);
		if (reachingDefinitions == null || reachingDefinitions.isEmpty()) {
			// System.err.println("No definition of " + sym + " at " +
			// sym.getDeclaringNode() + " reaches " + exp
			// + ", a(n) " + exp.getClass().getSimpleName());
			// Thread.dumpStack();
			/*
			 * Note that this situation would occur when reaching-definition
			 * IDFA hasn't yet been settled down and a co-existence query has
			 * been asked in finding sibling barriers.
			 * Conservatively, we return false.
			 */
			return false;
		}
		if (reachingDefinitions.size() == 1) {
			Node rd = Misc.getAnyElement(reachingDefinitions).getDefiningNode();
			if (SVEChecker.writesSingleValue(rd, expSet, nodePairs)) {
				return true;
			} else {
				return false;
			}
		}

		/*
		 * When exp has more than one reaching-definitions for sym, all RDs must
		 * be SVEs, and all must be executed by either all threads or none of
		 * them.
		 */
		for (Definition rdDef : reachingDefinitions) {
			Node rd = rdDef.getDefiningNode();
			// /*
			// * Debug code.
			// */
			//
			// if (rd instanceof Declaration) {
			// Declaration decl = (Declaration) rd;
			// if (!decl.getInfo().hasInitializer()) {
			// System.err.println(exp);
			// Main.dumpReachingDefinitions();
			// }
			// }
			// /*
			// * Debug code ends here.
			// */
			if (!SVEChecker.writesSingleValue(rd, expSet, nodePairs)) {
				return false;
			}
			if (!CoExistenceChecker.existsForAll(rd, expSet, nodePairs)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if node writes single-value for all the threads in any given phase.
	 *
	 * @param node
	 *             node that has to be tested.
	 *
	 * @return true if {@code node} would write same value to a symbol, for any
	 *         thread that may execute this node.
	 */
	public static boolean writesSingleValue(Node node) {
		return SVEChecker.writesSingleValue(node, new HashSet<>(), new HashSet<>());
	}

	/**
	 * Checks if node writes single-value for all the threads in any given phase.
	 *
	 * @param node
	 *                  node that has to be tested.
	 * @param expSet
	 *                  nodes for which SVE check is under process (on call-stack).
	 * @param nodePairs
	 *                  nodes for which co-existence check is under process (on
	 *                  call-stack).
	 *
	 * @return true if {@code node} would write same value to a symbol, for any
	 *         thread that may execute this node.
	 */
	private static boolean writesSingleValue(Node node, Set<Expression> expSet, Set<NodePair> nodePairs) {
		if (node instanceof Declaration) {
			/*
			 * Initializer should be an SVE.
			 * If there is no initializer, then return false, if declaration is
			 * that of a private variable; true if this declaration is that of a
			 * shared variable. For now, we conservatively return false for both
			 * the cases.
			 */
			Declaration decl = (Declaration) node;
			if (!decl.getInfo().hasInitializer()) {
				return false;
			}
			return SVEChecker.isSingleValuedPredicate(decl.getInfo().getInitializer(), expSet, nodePairs);
		} else if (node instanceof ParameterDeclaration) {
			return false;
		} else if (node instanceof OmpForInitExpression) {
			return false;
		} else if (node instanceof OmpForCondition) {
			assert (false);
		} else if (node instanceof OmpForReinitExpression) {
			return false;
		} else if (node instanceof ExpressionStatement) {
			ExpressionStatement exp = (ExpressionStatement) node;
			boolean ret = SVEChecker.isSingleValuedPredicate((Expression) exp.getF0().getNode(), expSet, nodePairs);
			return ret;
		} else if (node instanceof PostCallNode) {
			PostCallNode post = (PostCallNode) node;
			CallStatement callStmt = post.getParent();
			if (!callStmt.getInfo().hasKnownCalledFunctionSymbol()) {
				return false;
			}
			Symbol calledFunction = (Symbol) callStmt.getInfo().getCalledSymbols().get(0);
			if (SVEChecker.variableFunctions.contains(calledFunction.getName())) {
				return false;
			} else {
				/*
				 * If there are no arguments, then the return value is
				 * single-valued.
				 * If the arguments are all single-valued variables, then the
				 * return value is single-valued.
				 */
				List<SimplePrimaryExpression> argumentList = callStmt.getPreCallNode().getArgumentList();
				if (argumentList.isEmpty()) {
					return true;
				}
				for (SimplePrimaryExpression spe : argumentList) {
					if (!SVEChecker.isSingleValuedPredicate(spe, expSet, nodePairs)) {
						return false;
					}
				}
				// System.out.println(node + " performs a single-write since all arguments are
				// single-valued.");
				return true;
			}
		} else if (node instanceof PreCallNode) {
			/*
			 * Update: 27-Apr-18
			 * Note that in this scenario, the called methods might have been
			 * unavailable.
			 * In such a case, we should conservatively assume that different
			 * values might be written to different reachable cells.
			 */
			return false;
			// OLD CODE: Redundant if-else.
			// if (!node.getInfo().mayWrite()) {
			// return true;
			// } else {
			// return false;
			// }
		} else if (node instanceof Expression) {
			boolean ret = SVEChecker.isSingleValuedPredicate((Expression) node, expSet, nodePairs);
			// System.out.println(node + " performs a" + (ret ? " single-write" : "
			// multi-write") + " as an Expression.");
			return ret;
		}
		assert (false) : node.getClass().getSimpleName() + " " + node + " type of node is invalid here.";
		return false;
	}

	public static void extractSVEPragmas() {
		for (CompoundStatement compStmt : Misc.getInheritedPostOrderEnclosee(Program.getRoot(),
				CompoundStatement.class)) {
			/*
			 * For all unknown pragma of form "imop sve", annotate the
			 * succeeding predicate with SVE tag.
			 */
			List<Node> elements = compStmt.getInfo().getCFGInfo().getElementList();
			for (Object elementObj : elements.stream().filter(n -> n instanceof UnknownPragma).toArray()) {
				Node element = (Node) elementObj;
				if (!element.toString().contains("imop predicate sve")) {
					continue;
				}
				int indexAnnotated = elements.indexOf(element) + 1;
				if (indexAnnotated >= elements.size()) {
					Misc.exitDueToError("Incorrect sve annotation at line #" + Misc.getLineNum(element));
				}

				Node annotatedElem = elements.get(indexAnnotated);
				if (annotatedElem instanceof IfStatement) {
					((IfStatement) annotatedElem).getInfo().getCFGInfo().getPredicate().getInfo().setSVEAnnotated();
					compStmt.getInfo().getCFGInfo().removeElement(element);
				} else if (annotatedElem instanceof SwitchStatement) {
					((SwitchStatement) annotatedElem).getInfo().getCFGInfo().getPredicate().getInfo().setSVEAnnotated();
					compStmt.getInfo().getCFGInfo().removeElement(element);
				} else if (annotatedElem instanceof WhileStatement) {
					((WhileStatement) annotatedElem).getInfo().getCFGInfo().getPredicate().getInfo().setSVEAnnotated();
					compStmt.getInfo().getCFGInfo().removeElement(element);
				} else if (annotatedElem instanceof DoStatement) {
					((DoStatement) annotatedElem).getInfo().getCFGInfo().getPredicate().getInfo().setSVEAnnotated();
					compStmt.getInfo().getCFGInfo().removeElement(element);
				} else {
					Misc.warnDueToLackOfFeature("Ignoring the sve annotation at line #" + Misc.getLineNum(element),
							null);
				}
			}
		}

	}
}
