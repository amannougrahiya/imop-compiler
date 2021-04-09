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
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.solver.tokens.ExpressionTokenizer;
import imop.lib.analysis.solver.tokens.IdOrConstToken;
import imop.lib.analysis.solver.tokens.OperatorToken;
import imop.lib.analysis.solver.tokens.Tokenizable;
import imop.parser.CParserConstants;

import java.util.*;

/**
 * Represents a row of a table which is used to ensure that there are no
 * inductive (or recursive) definitions, as well as no name-conflicts in the
 * Z3 system of (in)equations.
 * <br>
 * Each entry represents either a unique set of reaching-definitions that a
 * symbol might see at a program-point, or a unique induction-range for a
 * symbol at a program point.
 * 
 * @author Aman Nougrahiya
 *
 */
public class DependenceEntry {
	private final Symbol symbol;
	private final String z3Name;
	private final IdOrConstToken tid;
	private final Set<Node> reachingDefinitions;
	public InductionRange inductionRange;
	public HashMap<String, String> renamingMap;
	private List<List<Tokenizable>> possibleRenamedAssertions;

	public DependenceEntry(Symbol symbol, String z3Name, IdOrConstToken tid, Set<Node> reachingDefs) {
		this.symbol = symbol;
		this.z3Name = z3Name;
		this.tid = tid;
		this.reachingDefinitions = reachingDefs;
		this.renamingMap = new HashMap<>();
	}

	/**
	 * Obtain a list of possible equations, as per the given set of reaching
	 * definitions or induction ranges, with suggested renaming applied.
	 * <br>
	 * Note that each entry of return would be a conjunction of one or more
	 * "LHS=RHS", or it would be some inequality for induction ranges. These
	 * assertions may still be <i>inductive</i>.
	 * 
	 * @param numThreads
	 *                   number of threads (symbolically).
	 * @return
	 *         list of possible equations as per this object's
	 *         reaching-definitions/induction-ranges.
	 */
	public List<List<Tokenizable>> getRenamedAssertions(IdOrConstToken numThreads) {
		if (possibleRenamedAssertions == null) {
			possibleRenamedAssertions = new ArrayList<>();
			if (inductionRange != null) {
				// Fill assertions with ranges.
				for (List<Tokenizable> range : inductionRange.getRanges(tid, numThreads)) {
					if (range.isEmpty()) {
						continue;
					}
					HashMap<String, String> completeMap = new HashMap<>(renamingMap);
					completeMap.put(symbol.getName(), z3Name);
					possibleRenamedAssertions.add(ExpressionTokenizer.getRenamedList(range, completeMap));
				}
			} else {
				// Fill assertions with expanded reaching definitions.
				List<Tokenizable> allExpandedRDs = new ArrayList<>();
				int counter = 0;
				for (Node rd : this.getReachingDefinitions()) {
					List<Tokenizable> prefixForm = ExpressionTokenizer.getAssigningExpression(rd);
					HashMap<IdOrConstToken, List<Tokenizable>> normalizedMap = ExpressionTokenizer
							.getNormalizedForm(prefixForm);
					for (IdOrConstToken id : normalizedMap.keySet()) {
						if (!id.toString().equals(symbol.toString())) {
							continue;
						}
						List<Tokenizable> normalizedDefinitionRHS = normalizedMap.get(id);
						if (normalizedDefinitionRHS.isEmpty()) {
							continue;
						}
						normalizedDefinitionRHS.add(0, OperatorToken.ASSIGN);
						NodeToken newToken = new NodeToken(symbol.getName());
						newToken.setKind(CParserConstants.IDENTIFIER);
						normalizedDefinitionRHS.add(1, new IdOrConstToken(newToken, rd));
						HashMap<String, String> completeMap = new HashMap<>(renamingMap);
						completeMap.put(symbol.getName(), z3Name);
						counter++;
						allExpandedRDs.addAll(ExpressionTokenizer.getRenamedList(normalizedDefinitionRHS, completeMap));
					}
				}
				for (int i = 0; i < counter - 1; i++) {
					allExpandedRDs.add(0, OperatorToken.LOGIC_OR);
				}
				if (!allExpandedRDs.isEmpty()) {
					possibleRenamedAssertions.add(allExpandedRDs);
				}
			}
		}
		return possibleRenamedAssertions;
	}

	@Override
	public String toString() {
		return "DependenceEntry [" + this.symbol + "(" + this.tid + "):" + this.z3Name + ", " + this.reachingDefinitions
				+ ", " + inductionRange + ", RenamingMap" + renamingMap + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getReachingDefinitions() == null) ? 0 : getReachingDefinitions().hashCode());
		result = prime * result + ((getSymbol() == null) ? 0 : getSymbol().hashCode());
		result = prime * result + ((getZ3Name() == null) ? 0 : getZ3Name().hashCode());
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
		DependenceEntry other = (DependenceEntry) obj;
		if (getSymbol() == null) {
			if (other.getSymbol() != null) {
				return false;
			}
		} else if (!getSymbol().equals(other.getSymbol())) {
			return false;
		}
		if (getZ3Name() == null) {
			if (other.getZ3Name() != null) {
				return false;
			}
		} else if (!getZ3Name().equals(other.getZ3Name())) {
			return false;
		}
		if (getReachingDefinitions() == null) {
			if (other.getReachingDefinitions() != null) {
				return false;
			}
		} else if (!getReachingDefinitions().equals(other.getReachingDefinitions())) {
			return false;
		}
		return true;
	}

	public Set<Node> getReachingDefinitions() {
		return Collections.unmodifiableSet(reachingDefinitions);
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public String getZ3Name() {
		return z3Name;
	}

	public IdOrConstToken getTid() {
		return tid;
	}
}
