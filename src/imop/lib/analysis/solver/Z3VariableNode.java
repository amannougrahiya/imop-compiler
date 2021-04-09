/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.solver;

import imop.lib.analysis.solver.tokens.ExpressionTokenizer;
import imop.lib.analysis.solver.tokens.OperatorToken;
import imop.lib.analysis.solver.tokens.Tokenizable;

import java.util.*;

public class Z3VariableNode {
	private final String z3VarName;
	private Set<Z3VariableNode> readsFrom;

	public Z3VariableNode(String z3VarName, Set<Z3VariableNode> readsFrom) {
		this.z3VarName = z3VarName;
		this.readsFrom = readsFrom;
	}

	String getZ3VarName() {
		return z3VarName;
	}

	Set<Z3VariableNode> getReadsFrom() {
		if (readsFrom == null) {
			readsFrom = new HashSet<>();
		}
		return readsFrom;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((z3VarName == null) ? 0 : z3VarName.hashCode());
		return result;
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
		Z3VariableNode other = (Z3VariableNode) obj;
		if (readsFrom == null) {
			if (other.readsFrom != null) {
				return false;
			}
		} else if (!readsFrom.equals(other.readsFrom)) {
			return false;
		}
		if (z3VarName == null) {
			if (other.z3VarName != null) {
				return false;
			}
		} else if (!z3VarName.equals(other.z3VarName)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String str = "[" + z3VarName + ": ";
		for (Z3VariableNode read : this.readsFrom) {
			str += read.z3VarName + ";";
		}
		str += " ]";
		return str;
	}

	/**
	 * Given a list of assertions in conjunction form
	 * ({@link ConstraintsGenerator#hasConjunctiveEqualityOrRelationalForm(List)}),
	 * this
	 * method returns all Z3 variable dependences as a graph.
	 * <br>
	 * Note that we do not create dependence edges arising out of symbolic range
	 * inequalities (since they can never be a part of a cycle anyway).
	 * 
	 * 
	 * @param assertions
	 *                   a set of assertions, where each entity is a conjunction of
	 *                   lists of the form {@code op x e}, where {@code op} is an
	 *                   equality or relational operator.
	 * @return
	 *         a dependence graph.
	 */
	public static HashMap<String, Z3VariableNode> getZ3VariableGraph(List<List<Tokenizable>> assertions) {
		HashMap<String, Z3VariableNode> allZ3Nodes = new HashMap<>();
		for (List<Tokenizable> assertion : assertions) {
			int index = -1;
			for (Tokenizable token : assertion) {
				index++;
				if (token == OperatorToken.ASSIGN) {
					List<Tokenizable> subList = assertion.subList(index, assertion.size());
					List<Tokenizable> firstOperand = ExpressionTokenizer.getFirstOperand(subList);
					List<Tokenizable> secondOperand = ExpressionTokenizer.getSecondOperand(subList);
					Set<String> writeSet = ExpressionTokenizer.getIdSet(firstOperand);
					Set<String> readSet = ExpressionTokenizer.getIdSet(secondOperand);
					for (String writeStr : writeSet) {
						Z3VariableNode writeVar;
						if (allZ3Nodes.containsKey(writeStr)) {
							writeVar = allZ3Nodes.get(writeStr);
						} else {
							writeVar = new Z3VariableNode(writeStr, new HashSet<>());
							allZ3Nodes.put(writeStr, writeVar);
						}
						for (String readStr : readSet) {
							Z3VariableNode readVar;
							if (allZ3Nodes.containsKey(readStr)) {
								readVar = allZ3Nodes.get(readStr);
							} else {
								readVar = new Z3VariableNode(readStr, new HashSet<>());
								allZ3Nodes.put(readStr, readVar);
							}
							Set<Z3VariableNode> alreadyReadVars = writeVar.getReadsFrom();
							if (!alreadyReadVars.contains(readVar)) {
								alreadyReadVars.add(readVar);
							}
						}
					}
				}
			}
		}
		return allZ3Nodes;
	}

	/**
	 * Given a Z3 variable dependence graph, this method returns all cyclic
	 * paths in the graph.
	 * 
	 * @param dependenceGraph
	 *                        a Z3 variable dependence graph.
	 * @return
	 *         set of all the cycles in the provided graph.
	 */
	public static Set<List<String>> getAllCycles(Collection<Z3VariableNode> z3Nodes) {
		Set<List<String>> setOfCycles = new HashSet<>();
		for (Z3VariableNode node : z3Nodes) {
			List<Z3VariableNode> cyclicPath = Z3VariableNode.getCyclicPath(node, new ArrayList<>());
			if (cyclicPath.isEmpty()) {
				continue;
			}
			List<String> cycle = new ArrayList<>();
			for (Z3VariableNode c : cyclicPath) {
				cycle.add(c.getZ3VarName());
			}
			/*
			 * Check if the cycle already exists in the set.
			 */
			boolean found = false;
			for (List<String> existing : setOfCycles) {
				String firstNew = cycle.get(0);
				if (!existing.contains(firstNew)) {
					continue;
				}
				if (existing.size() != cycle.size()) {
					continue;
				}
				int startingIndex = existing.indexOf(firstNew);
				boolean same = true;
				for (int i = 0, existingIndex = startingIndex; i < cycle
						.size(); i++, existingIndex = ((existingIndex + 1) % existing.size())) {
					if (!existing.get(existingIndex).equals(cycle.get(i))) {
						same = false;
						break;
					}
				}
				if (!same) {
					continue;
				}
				// Existing is same as cycle.
				found = true;
				break;
			}
			if (!found) {
				setOfCycles.add(cycle);
			}
		}
		return setOfCycles;
	}

	/**
	 * Given a node in Z3 variable dependence graph, this method detects (any
	 * one) cycle of which the node is a part.
	 * 
	 * @param node
	 *                     a node in Z3 variable graph.
	 * @param visitedNodes
	 *                     list of nodes visited so far.
	 * @return
	 *         a cyclic path of which {@code node} is a part; otherwise, if no
	 *         cycle exists, then an empty list is returned.
	 */
	private static List<Z3VariableNode> getCyclicPath(Z3VariableNode node, List<Z3VariableNode> visitedNodes) {
		if (visitedNodes.contains(node)) {
			return visitedNodes.subList(visitedNodes.indexOf(node), visitedNodes.size());
		} else {
			visitedNodes.add(node);
		}
		for (Z3VariableNode read : node.getReadsFrom()) {
			List<Z3VariableNode> returnList = Z3VariableNode.getCyclicPath(read, visitedNodes);
			if (!returnList.isEmpty()) {
				// First cycle detected; return.
				return returnList;
			}
		}
		// No cycle detected.
		visitedNodes.remove(node);
		return new ArrayList<>();
	}
}
