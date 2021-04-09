/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.dataflow;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Definition;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterThreadForwardNonCellularAnalysis;
import imop.lib.getter.AllDefinitionGetter;
import imop.lib.util.CellSet;
import imop.parser.Program;

import java.util.HashSet;
import java.util.Set;

@Deprecated
public class Old_ReachingDefinitionAnalysis
		extends InterThreadForwardNonCellularAnalysis<Old_ReachingDefinitionAnalysis.Old_ReachingDefinitionFlowFact> {

	public static class Old_ReachingDefinitionFlowFact extends FlowAnalysis.FlowFact {
		public Set<Definition> definitions;

		public Old_ReachingDefinitionFlowFact(Set<Definition> definitions) {
			this.definitions = definitions;
		}

		@Override
		public boolean isEqualTo(FlowFact other) {
			if (other == null || !(other instanceof Old_ReachingDefinitionFlowFact)) {
				return false;
			}
			Old_ReachingDefinitionFlowFact that = (Old_ReachingDefinitionFlowFact) other;
			if (this == that) {
				return true;
			}
			return this.definitions.equals(that.definitions);
		}

		@Override
		public String getString() {
			String retString = "";
			for (Definition def : this.definitions) {
				retString += def + "\n";
			}
			return retString;
		}

		@Override
		public boolean merge(FlowFact other, CellSet cellSet) {
			if (other == null || !(other instanceof Old_ReachingDefinitionFlowFact)) {
				return false;
			}
			Old_ReachingDefinitionFlowFact that = (Old_ReachingDefinitionFlowFact) other;
			if (this == that) {
				return false;
			}
			boolean changed = false;
			Set<Definition> thisDefSet = this.definitions;
			if (cellSet != null) {
				for (Definition def : that.definitions) {
					if (cellSet.contains(def.getCell())) {
						changed |= thisDefSet.add(def);
					}
				}
			} else {
				changed |= thisDefSet.addAll(that.definitions);
			}
			return changed;
		}
	} // End of ReachingDefinitionFlowFact.

	public Old_ReachingDefinitionAnalysis() {
		super(AnalysisName.OLD_REACHING_DEFINITION, new AnalysisDimension(SVEDimension.SVE_INSENSITIVE));
	}

	@Override
	public Old_ReachingDefinitionFlowFact getTop() {
		return new Old_ReachingDefinitionFlowFact(new HashSet<>());
	}

	@Override
	public Old_ReachingDefinitionFlowFact getEntryFact() {
		Set<Definition> defSet = new HashSet<>();
		for (Symbol sym : Program.getRoot().getInfo().getSymbolTable().values()) {
			if (sym.isAVariable()) {
				defSet.add(new Definition(sym.getDeclaringNode(), sym));
			}
		}
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main != null) {
			for (ParameterDeclaration param : main.getInfo().getCFGInfo().getParameterDeclarationList()) {
				Cell declaredParam = param.getInfo().getDeclaredSymbol();
				defSet.add(new Definition(param, declaredParam));
			}
		}
		return new Old_ReachingDefinitionFlowFact(defSet);
	}

	// @Override
	// public ReachingDefinitionFlowFact visit(EndNode n, ReachingDefinitionFlowFact
	// flowFactIN) {
	// Node parent = n.getParent();
	// if (parent instanceof CompoundStatement) {
	// CompoundStatement compStmt = (CompoundStatement) parent;
	// Set<Definition> newDefSet = new HashSet<>(flowFactIN.definitions);
	// ReachingDefinitionFlowFact flowFactOUT = new
	// ReachingDefinitionFlowFact(newDefSet);
	//
	// HashMap<String, Symbol> symTable = compStmt.getInfo().getSymbolTable();
	// for (Definition def : newDefSet) {
	// if (symTable.containsValue(def.getCell())) {
	// Symbol sym = (Symbol) def.getCell();
	// symTable.remove(sym.getName());
	// }
	// }
	// return flowFactOUT;
	// } else {
	// return flowFactIN;
	// }
	// }

	@Override
	public Old_ReachingDefinitionFlowFact initProcess(Node n, Old_ReachingDefinitionFlowFact flowFactIN) {
		AllDefinitionGetter defGetter = new AllDefinitionGetter();
		n.accept(defGetter);
		if (defGetter.definitionList.isEmpty()) {
			return flowFactIN;
		}

		CellSet redefinedCells = new CellSet();
		for (Definition defined : defGetter.definitionList) {
			if (defined.getCell() == Cell.genericCell) {
				redefinedCells.add(Cell.genericCell);
				break;
			}
			redefinedCells.add(defined.getCell());
		}

		Set<Definition> outDefSet = defGetter.definitionList;
		if (redefinedCells.isUniversal()) {
			outDefSet.addAll(flowFactIN.definitions);
		} else if (redefinedCells.size() > 1) {
			/*
			 * Conservatively, we assume that neither of the writes are
			 * guaranteed to happen.
			 */
			outDefSet.addAll(flowFactIN.definitions);
		} else {
			Cell killedCell = redefinedCells.getAnyElement();
			for (Definition inDef : flowFactIN.definitions) {
				if (killedCell != inDef.getCell()) {
					outDefSet.add(inDef);
				}
			}
		}
		return new Old_ReachingDefinitionFlowFact(outDefSet);
	}

	@Override
	public Old_ReachingDefinitionFlowFact writeToParameter(ParameterDeclaration parameter,
			SimplePrimaryExpression argument, Old_ReachingDefinitionFlowFact flowFactIN) {
		Cell paramCell = parameter.getInfo().getDeclaredSymbol();
		Definition newDef = new Definition(parameter, paramCell);
		CellSet redefinedCells = new CellSet();
		redefinedCells.add(newDef.getCell());

		Set<Definition> outDefSet = new HashSet<>();
		outDefSet.add(newDef);
		if (redefinedCells.isUniversal()) {
			outDefSet.addAll(flowFactIN.definitions);
		} else if (redefinedCells.size() > 1) {
			/*
			 * Conservatively, we assume that neither of the writes are
			 * guaranteed to happen.
			 */
			outDefSet.addAll(flowFactIN.definitions);
		} else {
			Cell killedCell = redefinedCells.getAnyElement();
			for (Definition inDef : flowFactIN.definitions) {
				if (killedCell != inDef.getCell()) {
					outDefSet.add(inDef);
				}
			}
		}
		return new Old_ReachingDefinitionFlowFact(outDefSet);
	}
}
