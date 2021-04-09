/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.solver.tokens;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Definition;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.solver.ConstraintsGenerator;
import imop.lib.util.Misc;
import imop.parser.CParserConstants;

import java.util.*;

/**
 * This class is used to obtain the list of tokens that represent the visited
 * Expression.
 * 
 * @author Aman Nougrahiya
 */
public class ExpressionTokenizer {
	/**
	 * Returns a list of {@link Tokenizable} tokens for the given expressions.
	 * 
	 * @param expression
	 *                   input expression for which a list of tokens have to be
	 *                   found.
	 * @
	 */
	public static List<Tokenizable> getInfixTokens(Node expression) {
		List<Tokenizable> retList = new ArrayList<>();
		if (!(expression instanceof Expression) && !(expression instanceof OmpForInitExpression)
				&& !(expression instanceof OmpForReinitExpression) && !(expression instanceof OmpForCondition)) {
			// Such nodes cannot be represented by a list of Tokenizable tokens.
			// Misc.warnDueToLackOfFeature("Cannot express " + expression + " as a list of
			// tokens.", null);
			return new ArrayList<>();
		}
		Node cfgLeaf = Misc.getCFGNodeFor(expression);
		TokenListGetter tokenListGetter = new TokenListGetter(cfgLeaf);
		expression.accept(tokenListGetter);
		retList = tokenListGetter.tokenList;
		if (retList.isEmpty() || tokenListGetter.inexpressible) {
			// Misc.warnDueToLackOfFeature("Cannot express " + expression + " as a list of
			// tokens.", null);
			return new ArrayList<>();
		}
		return retList;
	}

	public static List<Tokenizable> getPrefixTokens(Node expression) {
		return ExpressionTokenizer.getPrefixTokens(ExpressionTokenizer.getInfixTokens(expression));
	}

	public static List<Tokenizable> getPrefixTokens(List<Tokenizable> inList) {
		List<Tokenizable> preList = new ArrayList<>();
		if (inList.isEmpty()) {
			return preList;
		}
		List<Tokenizable> reverseList = new ArrayList<>();
		for (Tokenizable token : inList) {
			Tokenizable toInsert = token;
			if (token instanceof OperatorToken) {
				OperatorToken op = (OperatorToken) token;
				if (op == OperatorToken.OP) {
					toInsert = OperatorToken.CP;
				} else if (op == OperatorToken.CP) {
					toInsert = OperatorToken.OP;
				}
			}
			reverseList.add(0, toInsert);
		}
		preList = ExpressionTokenizer.getPostfixTokens(reverseList);
		Collections.reverse(preList);
		return preList;
	}

	public static List<Tokenizable> getPostfixTokens(Node expression) {
		return ExpressionTokenizer.getPostfixTokens(ExpressionTokenizer.getInfixTokens(expression));
	}

	public static List<Tokenizable> getPostfixTokens(List<Tokenizable> inList) {
		List<Tokenizable> postList = new ArrayList<>();
		if (inList.isEmpty()) {
			return postList;
		}
		Stack<OperatorToken> stack = new Stack<>();
		for (Tokenizable token : inList) {
			if (token instanceof IdOrConstToken) {
				postList.add(token);
				continue;
			}
			OperatorToken op = (OperatorToken) token;
			if (op == OperatorToken.CP) {
				// Empty the stack until OP, including OP.
				while (!stack.isEmpty() && stack.peek() != OperatorToken.OP) {
					postList.add(stack.pop());
				}
				assert (!stack.isEmpty()) : "Found unmatched parentheses in " + inList;
				Tokenizable top = stack.pop(); // to remove OP.
				assert (top == OperatorToken.OP) : "Found unmatched parentheses in " + inList;
				continue;
			} else if (op == OperatorToken.OP) {
				stack.push(op);
				continue;
			} else {
				do {
					if (stack.isEmpty()) {
						stack.push(op);
						break;
					}
					OperatorToken top = stack.peek();
					int topPrecedence = top.precedence();
					int opPrecedence = op.precedence();
					if (opPrecedence > topPrecedence) {
						stack.push(op);
						break;
					} else if (opPrecedence <= topPrecedence) {
						top = stack.pop();
						postList.add(top);
						continue;
					}
				} while (true);
				continue;
			}
		}
		while (!stack.isEmpty()) {
			postList.add(stack.pop());
		}
		return postList;
	}

	/**
	 * Given an expression in prefix form, this method returns the first operand
	 * (equivalent to the left subtree of the root in the expression tree of the
	 * given list).
	 * 
	 * @param prefixList
	 *                   an input list representing an expression in prefix form.
	 * @return
	 *         first operand at the top-most level.
	 */
	public static List<Tokenizable> getFirstOperand(List<Tokenizable> prefixList) {
		List<Tokenizable> firstOperand = new ArrayList<>();
		if (prefixList == null || prefixList.isEmpty()) {
			return firstOperand;
		}
		Tokenizable first = prefixList.get(0);
		if (first instanceof IdOrConstToken) {
			firstOperand.add(first);
			return firstOperand;
		} else {
			int collector = 0;
			int totalArity = 1;
			while (collector < totalArity) {
				collector++;
				Tokenizable next = prefixList.get(collector);
				firstOperand.add(next);
				if (next instanceof IdOrConstToken) {
					continue;
				} else {
					OperatorToken nextOp = (OperatorToken) next;
					totalArity += nextOp.arity();
					continue;
				}
			}
		}
		return firstOperand;
	}

	/**
	 * Given an expression in prefix form, this method returns the second
	 * operand (equivalent to the right subtree of the root in the expression
	 * tree of the given list).
	 * 
	 * @param prefixList
	 *                   an input list representing an expression in prefix form.
	 * @return
	 *         second operand at the top-most level; an empty list if the query
	 *         is not valid.
	 */
	public static List<Tokenizable> getSecondOperand(List<Tokenizable> prefixList) {
		List<Tokenizable> secondOperand = new ArrayList<>();
		Tokenizable first = prefixList.get(0);
		if (first instanceof IdOrConstToken) {
			return secondOperand;
		}
		OperatorToken op = (OperatorToken) first;
		if (op.arity() == 1) {
			return secondOperand;
		} else if (op.arity() == 2) {
			int firstOpSize = ExpressionTokenizer.getFirstOperand(prefixList).size();
			List<Tokenizable> dummyList = new ArrayList<>(prefixList.subList(firstOpSize + 1, prefixList.size()));
			dummyList.add(0, first);
			return ExpressionTokenizer.getFirstOperand(dummyList);
		} else if (op.arity() == 3) {
			assert (false) : "Currently, we do not handle ?:";
		}
		return secondOperand;
	}

	/**
	 * Obtain a new list of tokens where variables are replaced by their
	 * reaching definitions whenever there is exactly one reaching definition,
	 * which doesn't depend upon itself, cyclically.
	 * 
	 * @param inTokens
	 *                    input list of tokens, representing an expression that
	 *                    needs to
	 *                    be expanded.
	 * @param readSymbols
	 *                    set of symbols that have already been seen.
	 * @return
	 *         a new list of tokens where variables are replaced by their
	 *         reaching definitions whenever there is exactly one reaching
	 *         definition, which doesn't depend upon itself, cyclically.
	 * 
	 */
	public static List<Tokenizable> getUniqueExpansion(List<Tokenizable> inTokens, Set<Symbol> readSymbols) {
		if (readSymbols == null) {
			readSymbols = new HashSet<>();
		}
		List<Tokenizable> outTokens = new ArrayList<>();
		if (!inTokens.stream().anyMatch(t -> t instanceof IdOrConstToken && ((IdOrConstToken) t).isAnIdentifier())) {
			outTokens.addAll(inTokens);
			return outTokens;
		}
		Set<Symbol> symSet = ExpressionTokenizer.getSymbolSet(inTokens);
		Node cfgNode = null;
		for (Tokenizable t : inTokens) {
			if (t instanceof IdOrConstToken) {
				IdOrConstToken idOrC = (IdOrConstToken) t;
				if (idOrC.isAnIdentifier()) {
					cfgNode = idOrC.getCfgLeaf();
					break;
				}
			}
		}
		// Set<Node> reachingDefs = cfgNode.getInfo().getReachingDefinitions();
		HashMap<String, List<Tokenizable>> replacementMap = new HashMap<>();
		for (Symbol read : symSet) {
			if (readSymbols.contains(read)) {
				// Do not perform recursive replacements.
				continue;
			}
			// TODO: What if cfgNode is null?
			Set<Definition> readDefs = cfgNode.getInfo().getReachingDefinitions(read);
			// for (Node rd : reachingDefs) {
			// if (rd.getInfo().getWrites().contains(read)) {
			// readDefs.add(rd);
			// }
			// }
			if (readDefs == null || readDefs.isEmpty() || readDefs.size() != 1) {
				continue;
			} else {
				Node def = Misc.getAnyElement(readDefs).getDefiningNode();
				List<Tokenizable> defInPrefixForm = ExpressionTokenizer.getAssigningExpression(def);
				HashMap<IdOrConstToken, List<Tokenizable>> normalizedForm = ExpressionTokenizer
						.getNormalizedForm(defInPrefixForm);
				for (IdOrConstToken tempIdToken : normalizedForm.keySet()) {
					if (!tempIdToken.toString().trim().equals(read.toString().trim())) {
						continue;
					}
					List<Tokenizable> rhsExpression = normalizedForm.get(tempIdToken);
					Set<Symbol> newReadSet = new HashSet<>(readSymbols);
					newReadSet.add(read);
					List<Tokenizable> expandedForm = ExpressionTokenizer.getUniqueExpansion(rhsExpression, newReadSet);
					replacementMap.put(tempIdToken.toString(), expandedForm);
				}
			}
		}
		for (Tokenizable token : inTokens) {
			if (token instanceof OperatorToken) {
				outTokens.add(token);
			} else {
				if (replacementMap.containsKey(token.toString())) {
					List<Tokenizable> replacementList = replacementMap.get(token.toString());
					outTokens.addAll(replacementList);
				} else {
					outTokens.add(token);
				}
			}
		}
		return outTokens;
	}

	public static List<Tokenizable> getAssertionFor(String variable, List<List<Tokenizable>> assertions) {
		for (List<Tokenizable> assertion : assertions) {
			List<List<Tokenizable>> assignAsserts = ConstraintsGenerator.getAssignConstraints(assertion);
			if (assignAsserts == null || assignAsserts.isEmpty()) {
				continue;
			} else {
				List<Tokenizable> thisAssert = assignAsserts.get(0);
				List<Tokenizable> firstOperand = ExpressionTokenizer.getFirstOperand(thisAssert);
				if (firstOperand != null && firstOperand.size() == 1) {
					if (firstOperand.get(0).toString().equals(variable)) {
						return assertion;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Given a C definition in its prefix form as {@code defTokens}, this method
	 * provides a definition map with all the writes that happen in the
	 * definition, in normalized form.
	 * <br>
	 * Note that the values in map will not contain any <i>side-effect</i>
	 * operators.
	 * 
	 * @param defTokens
	 *                  a C definition represented in prefix form.
	 * @return
	 *         output map, for getting normalized form, free from
	 *         side-effects, for various writes that may happen in
	 *         {@code defTokens}.
	 */
	public static HashMap<IdOrConstToken, List<Tokenizable>> getNormalizedForm(List<Tokenizable> defTokens) {
		final HashMap<IdOrConstToken, List<Tokenizable>> definitionMap = new HashMap<>();
		/*
		 * Step 1: Collect all the side-effect operators in defTokens.
		 */
		List<OperatorToken> sideEffects = new ArrayList<>();
		List<Integer> sideEffectIndices = new ArrayList<>(); // Required due to repetitions in list.
		int i = -1;
		for (Tokenizable t : defTokens) {
			i++;
			if (t == OperatorToken.ASSIGN || t == OperatorToken.PRE_DEC || t == OperatorToken.PRE_INC
					|| t == OperatorToken.POST_DEC || t == OperatorToken.POST_INC) {
				sideEffects.add((OperatorToken) t);
				sideEffectIndices.add(i);
			}
		}
		/*
		 * Step 2: Handle each side-effect separately.
		 */
		i = -1;
		for (OperatorToken op : sideEffects) {
			i++;
			int indexOfOp = sideEffectIndices.get(i);
			if (op == OperatorToken.ASSIGN) {
				IdOrConstToken id = (IdOrConstToken) defTokens.get(indexOfOp + 1);
				if (!id.isAnIdentifier()) {
					// Ignore expressions of the form "= c x".
					continue;
				}
				// Obtain the read part without side-effects.
				List<Tokenizable> readPartWithSE = new ArrayList<>();
				for (int j = indexOfOp + 2; j < defTokens.size(); j++) {
					readPartWithSE.add(defTokens.get(j));
				}
				List<Tokenizable> readPartWithoutSE = ExpressionTokenizer.removeSideEffects(readPartWithSE);
				if (readPartWithoutSE == null) {
					// Ignore.
					continue;
				} else {
					definitionMap.put(id, readPartWithoutSE);
					continue;
				}
			} else if (op == OperatorToken.PRE_DEC || op == OperatorToken.POST_DEC) {
				IdOrConstToken id = (IdOrConstToken) defTokens.get(indexOfOp + 1);
				IdOrConstToken one = IdOrConstToken.getNewConstantToken("1");
				List<Tokenizable> readPartWithoutSE = new ArrayList<>();
				readPartWithoutSE.add(OperatorToken.MINUS);
				readPartWithoutSE.add(id);
				readPartWithoutSE.add(one);
				definitionMap.put(id, readPartWithoutSE);
			} else if (op == OperatorToken.PRE_INC || op == OperatorToken.POST_INC) {
				IdOrConstToken id = (IdOrConstToken) defTokens.get(indexOfOp + 1);
				IdOrConstToken one = IdOrConstToken.getNewConstantToken("1");
				List<Tokenizable> readPartWithoutSE = new ArrayList<>();
				readPartWithoutSE.add(OperatorToken.PLUS);
				readPartWithoutSE.add(id);
				readPartWithoutSE.add(one);
				definitionMap.put(id, readPartWithoutSE);
			} else {
				assert (false);
			}
		}
		return definitionMap;
	}

	/**
	 * Checks whether this list has any side-effects.
	 * 
	 * @param inTokens
	 *                 list of tokens, representing an expression.
	 * @return
	 *         {@code true}, if the list has any side-effects.
	 */
	public static boolean hasSideEffects(List<Tokenizable> inTokens) {
		return inTokens.stream().anyMatch(p -> p == OperatorToken.ASSIGN || p == OperatorToken.PRE_DEC
				|| p == OperatorToken.PRE_INC || p == OperatorToken.POST_DEC || p == OperatorToken.POST_INC);
	}

	/**
	 * A naive implementation to check if {@code inTokens} is a positive
	 * constant.
	 * 
	 * @param inTokens
	 *                 list of tokens to be inspected.
	 * @return
	 *         true, if {@code inTokens} is a positive constant; false, if
	 *         information cannot be obtained with a simple check.
	 */
	public static boolean isKnownPositive(List<Tokenizable> inTokens) {
		if (inTokens.isEmpty()) {
			return false;
		}
		List<Tokenizable> expandedForm = ExpressionTokenizer.getUniqueExpansion(inTokens, new HashSet<>());
		if (expandedForm.isEmpty()) {
			expandedForm = inTokens;
		}
		Tokenizable token = expandedForm.get(0);
		if (token instanceof IdOrConstToken) {
			IdOrConstToken idOrC = (IdOrConstToken) token;
			if (idOrC.isAConstant()) {
				String constStr = idOrC.toString();
				if (constStr.startsWith("-")) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * A naive implementation to check if {@code inTokens} is a negative
	 * constant.
	 * 
	 * @param inTokens
	 *                 list of tokens to be inspected.
	 * @return
	 *         true, if {@code inTokens} is a negative constant; false, if
	 *         information cannot be obtained with a simple check.
	 */
	public static boolean isKnownNegative(List<Tokenizable> inTokens) {
		if (inTokens.isEmpty()) {
			return false;
		}
		List<Tokenizable> expandedForm = ExpressionTokenizer.getUniqueExpansion(inTokens, new HashSet<>());
		if (expandedForm.isEmpty()) {
			expandedForm = inTokens;
		}
		Tokenizable token = expandedForm.get(0);
		if (token instanceof IdOrConstToken) {
			IdOrConstToken idOrC = (IdOrConstToken) token;
			if (idOrC.isAConstant()) {
				String constStr = idOrC.toString();
				if (constStr.startsWith("-")) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isZero(List<Tokenizable> tokens) {
		if (tokens.size() != 1) {
			return false;
		}
		if (tokens.get(0).toString().equals("0")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isOne(List<Tokenizable> tokens) {
		if (tokens.size() != 1) {
			return false;
		}
		if (tokens.get(0).toString().equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Remove side-effect operators from {@code inTokens}.
	 * 
	 * @param inTokens
	 *                 a C expression in prefix form.
	 * @return
	 *         an equivalent C expression with removal of ++, and -- operators;
	 *         if {@code inTokens} contain {@link OperatorToken#ASSIGN}
	 *         operator, then this method returns null.
	 */
	public static List<Tokenizable> removeSideEffects(List<Tokenizable> inTokens) {
		List<Tokenizable> outTokens = new ArrayList<>();
		int i = -1;
		boolean skipNext = false;
		for (Tokenizable t : inTokens) {
			i++;
			if (skipNext) {
				skipNext = false;
				continue;
			}
			if (t == OperatorToken.ASSIGN) {
				return null;
			} else if (t == OperatorToken.PRE_DEC || t == OperatorToken.PRE_INC) {
				if (t == OperatorToken.PRE_DEC) {
					outTokens.add(OperatorToken.MINUS);
				} else {
					outTokens.add(OperatorToken.PLUS);
				}
				IdOrConstToken next = (IdOrConstToken) inTokens.get(i + 1);
				outTokens.add(next);
				IdOrConstToken one = IdOrConstToken.getNewConstantToken("1");
				outTokens.add(one);
				skipNext = true;
				continue;
			} else if (t == OperatorToken.POST_DEC || t == OperatorToken.POST_INC) {
				// Skip.
				continue;
			} else {
				outTokens.add(t);
			}
		}
		return outTokens;
	}

	/**
	 * From a C definition, obtain its equivalent prefix form.
	 * 
	 * @param node
	 *             a C definition.
	 * @return
	 *         equivalent prefix form of the provided C definition.
	 *         <br>
	 *         Obtain normalized form of this result.
	 */
	public static List<Tokenizable> getAssigningExpression(Node node) {
		List<Tokenizable> retList = new ArrayList<>();
		node = Misc.getCFGNodeFor(node);
		if (!Misc.isCFGLeafNode(node)) {
			return retList;
		}
		if (node instanceof Declaration) {
			Declaration decl = (Declaration) node;
			if (!decl.getInfo().hasInitializer()) {
				return retList;
			}
			Symbol sym = decl.getInfo().getDeclaredSymbol();
			Declarator declarator = decl.getInfo().getDeclarator(sym.getName());
			NodeToken idNode = null;
			for (NodeToken temp : Misc.getInheritedEnclosee(declarator, NodeToken.class)) {
				if (temp.getKind() == CParserConstants.IDENTIFIER) {
					idNode = temp;
					break;
				}
			}
			if (idNode == null) {
				return retList;
			}
			Initializer init = decl.getInfo().getInitializer();
			retList = getPrefixTokens(init);
			retList.add(0, new IdOrConstToken(idNode, node));
			retList.add(0, OperatorToken.ASSIGN);
			return retList;
		} else if (node instanceof ExpressionStatement) {
			Expression exp = (Expression) ((ExpressionStatement) node).getF0().getNode();
			if (exp == null) {
				return retList;
			}
			return getPrefixTokens(exp);
		} else if (node instanceof ReturnStatement) {
			return retList;
		} else if (node instanceof PostCallNode) {
			return retList;
		} else if (node instanceof OmpForInitExpression) {
			return getPrefixTokens(node);
		} else if (node instanceof OmpForReinitExpression) {
			return getPrefixTokens(node);
		} else if (node instanceof OmpForCondition) {
			return getPrefixTokens(node);
		} else if (node instanceof Expression) {
			return getPrefixTokens(node);
		} else {
			return retList;
		}
	}

	/**
	 * Given a list of tokens, and a renaming map, this method returns a new
	 * list with renaming performed as per the map.
	 * 
	 * @param inTokens
	 *                 a list of tokens, representing an expression.
	 * @param map
	 *                 a map from set of old names to set of new names for various
	 *                 identifiers in the {@code inTokens}.
	 * @return
	 *         a new list with renaming performed.
	 */
	public static List<Tokenizable> getRenamedList(List<Tokenizable> inTokens, HashMap<String, String> map) {
		if (map.isEmpty()) {
			return new ArrayList<>(inTokens);
		}
		List<Tokenizable> outTokens = new ArrayList<>();
		for (Tokenizable token : inTokens) {
			if (token instanceof OperatorToken) {
				outTokens.add(token);
			} else {
				IdOrConstToken idOrC = (IdOrConstToken) token;
				if (idOrC.isAConstant()) {
					outTokens.add(token);
				} else {
					NodeToken old = idOrC.getIdentifier();
					if (!map.containsKey(old.toString())) {
						outTokens.add(token);
					} else {
						NodeToken newToken = new NodeToken(map.get(old.toString()));
						newToken.setKind(CParserConstants.IDENTIFIER);
						outTokens.add(new IdOrConstToken(newToken, idOrC.getCfgLeaf()));
					}
				}
			}
		}
		return outTokens;
	}

	/**
	 * Obtain the CFG node associated with the provided expression in its prefix
	 * form.
	 * <br>
	 * Note that this information should not be relied upon for expanded-forms.
	 * 
	 * @param tokens
	 *               list of tokens representing a C expression in prefix form.
	 * @return
	 *         CFG node corresponding to the provided (non-expanded) list.
	 */
	public static Node getCFGNode(List<Tokenizable> tokens) {
		for (Tokenizable token : tokens) {
			if (token instanceof IdOrConstToken) {
				IdOrConstToken idOrC = (IdOrConstToken) token;
				return idOrC.getCfgLeaf();
			}
		}
		return null;
	}

	/**
	 * Returns the prefix form min of the given two expressions.
	 * 
	 * @param first
	 *               an expression in prefix form.
	 * @param second
	 *               an expression in prefix form.
	 * @return
	 *         a prefix form expression which would represent the max of
	 *         {@code first} and {@code second}.
	 */
	public static List<Tokenizable> getMin(List<Tokenizable> first, List<Tokenizable> second) {
		List<Tokenizable> maxValue = new ArrayList<>();
		maxValue.add(OperatorToken.ITE);
		maxValue.add(OperatorToken.GT);
		maxValue.add(OperatorToken.MINUS);
		maxValue.addAll(first);
		maxValue.addAll(second);
		maxValue.add(IdOrConstToken.getNewConstantToken("0"));
		maxValue.addAll(second);
		maxValue.addAll(first);
		// Old code:
		// maxValue.add(OperatorToken.DIV);
		// maxValue.add(OperatorToken.MINUS);
		// maxValue.add(OperatorToken.PLUS);
		// maxValue.addAll(first);
		// maxValue.addAll(second);
		// maxValue.add(OperatorToken.MUL);
		// maxValue.add(OperatorToken.MINUS);
		// maxValue.addAll(first);
		// maxValue.addAll(second);
		// maxValue.add(OperatorToken.MOD);
		// maxValue.add(OperatorToken.PLUS);
		// maxValue.add(OperatorToken.MUL);
		// maxValue.add(IdOrConstToken.getNewConstantToken("2"));
		// maxValue.add(OperatorToken.MINUS);
		// maxValue.addAll(first);
		// maxValue.addAll(second);
		// maxValue.add(IdOrConstToken.getNewConstantToken("1"));
		// maxValue.add(IdOrConstToken.getNewConstantToken("2"));
		// maxValue.add(IdOrConstToken.getNewConstantToken("2"));
		return maxValue;
	}

	/**
	 * Returns the prefix form max of the given two expressions.
	 * 
	 * @param first
	 *               an expression in prefix form.
	 * @param second
	 *               an expression in prefix form.
	 * @return
	 *         a prefix form expression which would represent the max of
	 *         {@code first} and {@code second}.
	 */
	public static List<Tokenizable> getMax(List<Tokenizable> first, List<Tokenizable> second) {
		List<Tokenizable> maxValue = new ArrayList<>();
		maxValue.add(OperatorToken.ITE);
		maxValue.add(OperatorToken.GT);
		maxValue.add(OperatorToken.MINUS);
		maxValue.addAll(first);
		maxValue.addAll(second);
		maxValue.add(IdOrConstToken.getNewConstantToken("0"));
		maxValue.addAll(first);
		maxValue.addAll(second);
		// Old code:
		// maxValue.add(OperatorToken.DIV);
		// maxValue.add(OperatorToken.PLUS);
		// maxValue.add(OperatorToken.PLUS);
		// maxValue.addAll(first);
		// maxValue.addAll(second);
		// maxValue.add(OperatorToken.MUL);
		// maxValue.add(OperatorToken.MINUS);
		// maxValue.addAll(first);
		// maxValue.addAll(second);
		// maxValue.add(OperatorToken.MOD);
		// maxValue.add(OperatorToken.PLUS);
		// maxValue.add(OperatorToken.MUL);
		// maxValue.add(IdOrConstToken.getNewConstantToken("2"));
		// maxValue.add(OperatorToken.MINUS);
		// maxValue.addAll(first);
		// maxValue.addAll(second);
		// maxValue.add(IdOrConstToken.getNewConstantToken("1"));
		// maxValue.add(IdOrConstToken.getNewConstantToken("2"));
		// maxValue.add(IdOrConstToken.getNewConstantToken("2"));
		return maxValue;
	}

	/**
	 * Returns the prefix form max of the given two expressions.
	 * 
	 * @param first
	 *              an expression in prefix form.
	 * @return
	 *         a prefix form expression which would represent the max of
	 *         {@code first} and {@code second}.
	 */
	public static List<Tokenizable> getAbs(List<Tokenizable> operand) {
		List<Tokenizable> maxValue = new ArrayList<>();
		maxValue.add(OperatorToken.ITE);
		maxValue.add(OperatorToken.GE);
		maxValue.addAll(operand);
		maxValue.add(IdOrConstToken.getNewConstantToken("0"));
		maxValue.addAll(operand);
		maxValue.add(OperatorToken.UNARY_MINUS);
		maxValue.addAll(operand);
		return maxValue;
	}

	/**
	 * Given an expression in prefix form ({@code tokens}), obtain the set of
	 * all those Symbols that have been read in the expression.
	 * <br>
	 * Note that in an assertion {@code "a = b"}, both {@code a} and {@code b}
	 * are being read.
	 * 
	 * @param tokens
	 *               an expression in its prefix form.
	 * @return
	 *         a set of symbols that have been read in the expression.
	 */
	public static Set<Symbol> getSymbolSet(List<Tokenizable> tokens) {
		Set<Symbol> symbolList = new HashSet<>();
		for (Tokenizable token : tokens) {
			if (token instanceof IdOrConstToken) {
				IdOrConstToken spe = (IdOrConstToken) token;
				if (spe.isAnIdentifier()) {
					Node leafNode = spe.getCfgLeaf();
					Cell cell = Misc.getSymbolEntry(spe.getIdentifier().toString(), leafNode);
					if (cell == null) {
						continue;
					}
					symbolList.add((Symbol) cell);
				}
			}
		}
		return symbolList;
	}

	/**
	 * Obtain the list of identifiers read in the given token list.
	 * 
	 * @param tokens
	 *               given token list, representing a C expression in prefix form.
	 * @return
	 *         a set of identifier strings accessed in the given list of
	 *         {@code tokens}.
	 */
	public static Set<String> getIdSet(List<Tokenizable> tokens) {
		Set<String> stringSet = new HashSet<>();
		for (Tokenizable token : tokens) {
			if (token instanceof IdOrConstToken) {
				IdOrConstToken spe = (IdOrConstToken) token;
				if (spe.isAnIdentifier()) {
					stringSet.add(spe.getIdentifier().toString());
				}
			}
		}
		return stringSet;

	}

	private static class TokenListGetter extends DepthFirstProcess {
		public List<Tokenizable> tokenList = new ArrayList<>();
		public boolean inexpressible = false;
		public final Node cfgLeaf;

		public TokenListGetter(Node cfgLeaf) {
			this.cfgLeaf = cfgLeaf;
		}

		@Override
		public void visit(NodeToken n) {
			if (n.getKind() == CParserConstants.IDENTIFIER) {
				tokenList.add(new IdOrConstToken(n, cfgLeaf));
			}
		}

		/**
		 * f0 ::= AssignmentExpression() | ArrayInitializer()
		 */
		@Override
		public void visit(Initializer n) {
			if (n.getF0().getChoice() instanceof AssignmentExpression) {
				n.getF0().getChoice().accept(this);
			} else {
				this.inexpressible = true;
			}
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForInitExpression n) {
			n.getF0().accept(this);
			tokenList.add(OperatorToken.ASSIGN);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= OmpForLTCondition()
		 * | OmpForLECondition()
		 * | OmpForGTCondition()
		 * | OmpForGECondition()
		 */
		@Override
		public void visit(OmpForCondition n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<"
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForLTCondition n) {
			n.getF0().accept(this);
			tokenList.add(OperatorToken.LT);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForLECondition n) {
			n.getF0().accept(this);
			tokenList.add(OperatorToken.LE);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">"
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForGTCondition n) {
			n.getF0().accept(this);
			tokenList.add(OperatorToken.GT);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForGECondition n) {
			n.getF0().accept(this);
			tokenList.add(OperatorToken.GE);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= PostIncrementId()
		 * | PostDecrementId()
		 * | PreIncrementId()
		 * | PreDecrementId()
		 * | ShortAssignPlus()
		 * | ShortAssignMinus()
		 * | OmpForAdditive()
		 * | OmpForSubtractive()
		 * | OmpForMultiplicative()
		 */
		@Override
		public void visit(OmpForReinitExpression n) {
			n.getOmpForReinitF0().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "++"
		 */
		@Override
		public void visit(PostIncrementId n) {
			n.getF0().accept(this);
			tokenList.add(OperatorToken.POST_INC);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "--"
		 */
		@Override
		public void visit(PostDecrementId n) {
			n.getF0().accept(this);
			tokenList.add(OperatorToken.POST_DEC);
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(PreIncrementId n) {
			tokenList.add(OperatorToken.PRE_INC);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(PreDecrementId n) {
			tokenList.add(OperatorToken.PRE_DEC);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "+="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(ShortAssignPlus n) {
			n.getF0().accept(this);
			tokenList.add(OperatorToken.ASSIGN);
			n.getF0().accept(this);
			tokenList.add(OperatorToken.PLUS);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "-="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(ShortAssignMinus n) {
			n.getF0().accept(this);
			tokenList.add(OperatorToken.ASSIGN);
			n.getF0().accept(this);
			tokenList.add(OperatorToken.MINUS);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "+"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public void visit(OmpForAdditive n) {
			n.getF0().accept(this);
			tokenList.add(OperatorToken.ASSIGN);
			n.getF2().accept(this);
			tokenList.add(OperatorToken.PLUS);
			n.getF4().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "-"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public void visit(OmpForSubtractive n) {
			n.getF0().accept(this);
			tokenList.add(OperatorToken.ASSIGN);
			n.getF2().accept(this);
			tokenList.add(OperatorToken.MINUS);
			n.getF4().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= MultiplicativeExpression()
		 * f3 ::= "+"
		 * f4 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(OmpForMultiplicative n) {
			n.getF0().accept(this);
			tokenList.add(OperatorToken.ASSIGN);
			n.getF2().accept(this);
			tokenList.add(OperatorToken.PLUS);
			n.getF4().accept(this);
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public void visit(Expression n) {
			if (n.getExpF1().getNodes().isEmpty()) {
				n.getExpF0().accept(this);
			} else {
				assert (false); // ExpressionSimplification should have removed the comma operator.
				inexpressible = true;
				return;
			}
		}

		/**
		 * f0 ::= NonConditionalExpression() | ConditionalExpression()
		 */
		@Override
		public void visit(AssignmentExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public void visit(NonConditionalExpression n) {
			n.getF0().accept(this);
			switch (n.getF1().toString().trim()) {
			case "=":
				tokenList.add(OperatorToken.ASSIGN);
				break;
			case "*=":
				tokenList.add(OperatorToken.ASSIGN);
				tokenList.add(OperatorToken.OP);
				n.getF0().accept(this);
				tokenList.add(OperatorToken.CP);
				tokenList.add(OperatorToken.MUL);
				break;
			case "/=":
				tokenList.add(OperatorToken.ASSIGN);
				tokenList.add(OperatorToken.OP);
				n.getF0().accept(this);
				tokenList.add(OperatorToken.CP);
				tokenList.add(OperatorToken.DIV);
				break;
			case "%=":
				tokenList.add(OperatorToken.ASSIGN);
				tokenList.add(OperatorToken.OP);
				n.getF0().accept(this);
				tokenList.add(OperatorToken.CP);
				tokenList.add(OperatorToken.MOD);
				break;
			case "+=":
				tokenList.add(OperatorToken.ASSIGN);
				tokenList.add(OperatorToken.OP);
				n.getF0().accept(this);
				tokenList.add(OperatorToken.CP);
				tokenList.add(OperatorToken.PLUS);
				break;
			case "-=":
				tokenList.add(OperatorToken.ASSIGN);
				tokenList.add(OperatorToken.OP);
				n.getF0().accept(this);
				tokenList.add(OperatorToken.CP);
				tokenList.add(OperatorToken.MINUS);
				break;
			case "<<=":
				tokenList.add(OperatorToken.ASSIGN);
				tokenList.add(OperatorToken.OP);
				n.getF0().accept(this);
				tokenList.add(OperatorToken.CP);
				tokenList.add(OperatorToken.SL);
				break;
			case ">>=":
				tokenList.add(OperatorToken.ASSIGN);
				tokenList.add(OperatorToken.OP);
				n.getF0().accept(this);
				tokenList.add(OperatorToken.CP);
				tokenList.add(OperatorToken.SR);
				break;
			case "&=":
				tokenList.add(OperatorToken.ASSIGN);
				tokenList.add(OperatorToken.OP);
				n.getF0().accept(this);
				tokenList.add(OperatorToken.CP);
				tokenList.add(OperatorToken.BIT_AND);
				break;
			case "^=":
				tokenList.add(OperatorToken.ASSIGN);
				tokenList.add(OperatorToken.OP);
				n.getF0().accept(this);
				tokenList.add(OperatorToken.CP);
				tokenList.add(OperatorToken.BIT_XOR);
				break;
			case "|=":
				tokenList.add(OperatorToken.ASSIGN);
				tokenList.add(OperatorToken.OP);
				n.getF0().accept(this);
				tokenList.add(OperatorToken.CP);
				tokenList.add(OperatorToken.BIT_OR);
				break;
			default:
				assert (false);
			}
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= LogicalORExpression()
		 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
		 */
		@Override
		public void visit(ConditionalExpression n) {
			if (n.getF1().present()) {
				tokenList.add(OperatorToken.ITE);
				n.getF0().accept(this);
				NodeSequence nodeSeq = (NodeSequence) n.getF1().getNode();
				nodeSeq.getNodes().get(1).accept(this);
				nodeSeq.getNodes().get(3).accept(this);
				// assert (false);
				// inexpressible = true;
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= LogicalANDExpression()
		 * f1 ::= ( "||" LogicalORExpression() )?
		 */
		@Override
		public void visit(LogicalORExpression n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				// assert (false);
				// inexpressible = true;
				tokenList.add(OperatorToken.LOGIC_OR);
				n.getF1().accept(this);
			}
		}

		/**
		 * f0 ::= InclusiveORExpression()
		 * f1 ::= ( "&&" LogicalANDExpression())?
		 */
		@Override
		public void visit(LogicalANDExpression n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				assert (false);
				inexpressible = true;
			}
		}

		/**
		 * f0 ::= ExclusiveORExpression()
		 * f1 ::= ( "|" InclusiveORExpression())?
		 */
		@Override
		public void visit(InclusiveORExpression n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				tokenList.add(OperatorToken.BIT_OR);
				n.getF1().accept(this);
			}
		}

		/**
		 * f0 ::= ANDExpression()
		 * f1 ::= ( "^" ExclusiveORExpression() )?
		 */
		@Override
		public void visit(ExclusiveORExpression n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				tokenList.add(OperatorToken.BIT_XOR);
				n.getF1().accept(this);
			}
		}

		/**
		 * f0 ::= EqualityExpression()
		 * f1 ::= ( "&" ANDExpression() )?
		 */
		@Override
		public void visit(ANDExpression n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				tokenList.add(OperatorToken.BIT_AND);
				n.getF1().accept(this);
			}
		}

		/**
		 * f0 ::= RelationalExpression()
		 * f1 ::= ( EqualOptionalExpression() )?
		 */
		@Override
		public void visit(EqualityExpression n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= EqualExpression() | NonEqualExpression()
		 */
		@Override
		public void visit(EqualOptionalExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "=="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public void visit(EqualExpression n) {
			tokenList.add(OperatorToken.ISEQ);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "!="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public void visit(NonEqualExpression n) {
			tokenList.add(OperatorToken.ISNEQ);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= ShiftExpression()
		 * f1 ::= ( RelationalOptionalExpression() )?
		 */
		@Override
		public void visit(RelationalExpression n) {
			n.getRelExpF0().accept(this);
			n.getRelExpF1().accept(this);
		}

		/**
		 * f0 ::= RelationalLTExpression() | RelationalGTExpression() |
		 * RelationalLEExpression() | RelationalGEExpression()
		 */
		@Override
		public void visit(RelationalOptionalExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "<"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public void visit(RelationalLTExpression n) {
			tokenList.add(OperatorToken.LT);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= ">"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public void visit(RelationalGTExpression n) {
			tokenList.add(OperatorToken.GT);
			n.getF1().accept(this);

		}

		/**
		 * f0 ::= "<="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public void visit(RelationalLEExpression n) {
			tokenList.add(OperatorToken.LE);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= ">="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public void visit(RelationalGEExpression n) {
			tokenList.add(OperatorToken.GE);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= AdditiveExpression()
		 * f1 ::= ( ShiftOptionalExpression() )?
		 */
		@Override
		public void visit(ShiftExpression n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= ShiftLeftExpression() | ShiftRightExpression()
		 */
		@Override
		public void visit(ShiftOptionalExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= ">>"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public void visit(ShiftLeftExpression n) {
			tokenList.add(OperatorToken.SL);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "<<"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public void visit(ShiftRightExpression n) {
			tokenList.add(OperatorToken.SR);
			n.getF1().accept(this);

		}

		/**
		 * f0 ::= MultiplicativeExpression()
		 * f1 ::= ( AdditiveOptionalExpression() )?
		 */
		@Override
		public void visit(AdditiveExpression n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= AdditivePlusExpression() | AdditiveMinusExpression()
		 */
		@Override
		public void visit(AdditiveOptionalExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "+"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public void visit(AdditivePlusExpression n) {
			tokenList.add(OperatorToken.PLUS);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "-"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public void visit(AdditiveMinusExpression n) {
			tokenList.add(OperatorToken.MINUS);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= CastExpression()
		 * f1 ::= ( MultiplicativeOptionalExpression() )?
		 */
		@Override
		public void visit(MultiplicativeExpression n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= MultiplicativeMultiExpression() |
		 * MultiplicativeDivExpression() |
		 * MultiplicativeModExpression()
		 */
		@Override
		public void visit(MultiplicativeOptionalExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "*"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public void visit(MultiplicativeMultiExpression n) {
			tokenList.add(OperatorToken.MUL);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "/"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public void visit(MultiplicativeDivExpression n) {
			tokenList.add(OperatorToken.DIV);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "%"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public void visit(MultiplicativeModExpression n) {
			tokenList.add(OperatorToken.MOD);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= CastExpressionTypedCastExpressionTyped() | UnaryExpression()
		 */
		@Override
		public void visit(CastExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "("
		 * f1 ::= TypeName()
		 * f2 ::= ")"
		 * f3 ::= CastExpression()
		 */
		@Override
		public void visit(CastExpressionTyped n) {
			inexpressible = true;
		}

		/**
		 * f0 ::= UnaryExpressionPreIncrement() | UnaryExpressionPreDecrement()
		 * | UnarySizeofExpression() | UnaryCastExpression() |
		 * PostfixExpression()
		 */
		@Override
		public void visit(UnaryExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(UnaryExpressionPreIncrement n) {
			tokenList.add(OperatorToken.PRE_INC);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(UnaryExpressionPreDecrement n) {
			tokenList.add(OperatorToken.PRE_DEC);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= UnaryOperator()
		 * f1 ::= CastExpression()
		 */
		@Override
		public void visit(UnaryCastExpression n) {
			switch (n.getF0().toString().trim()) {
			case "&":
				tokenList.add(OperatorToken.AMP);
				break;
			case "*":
				tokenList.add(OperatorToken.PTR);
				break;
			case "+":
				tokenList.add(OperatorToken.UNARY_PLUS);
				break;
			case "-":
				tokenList.add(OperatorToken.UNARY_MINUS);
				break;
			case "~":
				tokenList.add(OperatorToken.BIT_NEGATION);
				break;
			case "!":
				tokenList.add(OperatorToken.NOT);
				break;
			default:
			}
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= SizeofTypeName() | SizeofUnaryExpression()
		 */
		@Override
		public void visit(UnarySizeofExpression n) {
			inexpressible = true;
		}

		/**
		 * f0 ::= PrimaryExpression()
		 * f1 ::= PostfixOperationsList()
		 */
		@Override
		public void visit(PostfixExpression n) {
			n.getF0().accept(this);
			NodeListOptional nodeList = n.getF1().getF0();
			if (nodeList.getNodes().isEmpty()) {
				return;
			} else {
				int size = nodeList.getNodes().size();
				int i = 0;
				while (i < size) {
					Node opNode = ((APostfixOperation) nodeList.getNodes().get(i)).getF0().getChoice();
					if (opNode instanceof BracketExpression) {
						inexpressible = true;
					} else if (opNode instanceof ArgumentList) {
						inexpressible = true;
					} else if (opNode instanceof DotId) {
						inexpressible = true;
					} else if (opNode instanceof ArrowId) {
						inexpressible = true;
					} else if (opNode instanceof PlusPlus) {
						tokenList.add(OperatorToken.POST_INC);
					} else if (opNode instanceof MinusMinus) {
						tokenList.add(OperatorToken.POST_DEC);
					} else {
						assert (false);
					}
					i++;
				}
			}
		}

		/**
		 * f0 ::= <IDENTIFIER> | Constant() | ExpressionClosed()
		 */
		@Override
		public void visit(PrimaryExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "("
		 * f1 ::= Expression()
		 * f2 ::= ")"
		 */
		@Override
		public void visit(ExpressionClosed n) {
			tokenList.add(OperatorToken.OP);
			n.getF1().accept(this);
			tokenList.add(OperatorToken.CP);
		}

		/**
		 * f0 ::= <INTEGER_LITERAL> | <FLOATING_POINT_LITERAL> |
		 * <CHARACTER_LITERAL> | (<STRING_LITERAL> )+
		 */
		@Override
		public void visit(Constant n) {
			tokenList.add(new IdOrConstToken(n));
		}

		@Override
		public void visit(SimplePrimaryExpression n) {
			if (n.isAConstant()) {
				tokenList.add(new IdOrConstToken(n.getConstant()));
			} else {
				tokenList.add(new IdOrConstToken(n.getIdentifier(), cfgLeaf));
			}
		}
	}
}
