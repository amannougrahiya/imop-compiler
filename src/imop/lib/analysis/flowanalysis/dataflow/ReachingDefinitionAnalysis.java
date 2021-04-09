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
import imop.lib.analysis.flowanalysis.ImmutableDefinitionSet;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.CellularDataFlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterThreadForwardCellularAnalysis;
import imop.lib.getter.AllDefinitionGetter;
import imop.lib.util.CellSet;
import imop.lib.util.ExtensibleCellMap;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.HashSet;
import java.util.Set;

public class ReachingDefinitionAnalysis
		extends InterThreadForwardCellularAnalysis<ReachingDefinitionAnalysis.ReachingDefinitionFlowMap> {

	public ReachingDefinitionAnalysis() {
		super(AnalysisName.REACHING_DEFINITION, new AnalysisDimension(Program.sveSensitivityOfIDFAEdges));
	}

	public static class ReachingDefinitionFlowMap
			extends CellularDataFlowAnalysis.CellularFlowMap<ImmutableDefinitionSet> {

		public ReachingDefinitionFlowMap(ExtensibleCellMap<ImmutableDefinitionSet> flowMap) {
			super(flowMap);
		}

		public ReachingDefinitionFlowMap(CellularFlowMap<ImmutableDefinitionSet> thatFlowFact) {
			super(thatFlowFact);
		}

		@Override
		public String getAnalysisNameKey() {
			return "rd";
		}

		// @Override
		// public ImmutableCellSet meet(ImmutableCellSet s1, ImmutableCellSet s2) {
		// ImmutableCellSet s3;
		// if (s1 == null) {
		// if (s2 == null) {
		// s3 = new ImmutableCellSet();
		// } else {
		// s3 = s2;
		// }
		// } else if (s1 == s2) {
		// s3 = s1;
		// } else {
		// CellSet newSet = new CellSet(s1);
		// newSet.addAll(s2);
		// s3 = new ImmutableCellSet(newSet);
		// }
		// return s3;
		// }

		@Override
		public ImmutableDefinitionSet meet(ImmutableDefinitionSet s1, ImmutableDefinitionSet s2) {
			ImmutableDefinitionSet s3;
			if (s1 == null) {
				if (s2 == null) {
					s3 = new ImmutableDefinitionSet();
				} else {
					s3 = s2;
				}
			} else if (s1 == s2 || s1.equals(s2)) {
				s3 = s1;
			} else {
				Set<Definition> newSet = new HashSet<>(s1);
				newSet.addAll(s2);
				s3 = new ImmutableDefinitionSet(newSet);
			}
			return s3;
		}
	} // End of ReachingDefinitionFlowFact.

	@Override
	public ReachingDefinitionFlowMap getTop() {
		ExtensibleCellMap<ImmutableDefinitionSet> defMap = new ExtensibleCellMap<>();
		return new ReachingDefinitionFlowMap(defMap);
	}

	@Override
	public ReachingDefinitionFlowMap getEntryFact() {
		ExtensibleCellMap<ImmutableDefinitionSet> defMap = new ExtensibleCellMap<>();
		Set<Definition> defSet = new HashSet<>();
		for (Symbol sym : Program.getRoot().getInfo().getSymbolTable().values()) {
			if (sym.isAVariable()) {
				defSet = new HashSet<>();
				defSet.add(new Definition(sym.getDeclaringNode(), sym));
				ImmutableDefinitionSet ids = new ImmutableDefinitionSet(defSet);
				defMap.put(sym, ids);
			}
		}
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main != null) {
			for (ParameterDeclaration param : main.getInfo().getCFGInfo().getParameterDeclarationList()) {
				Cell declaredParam = param.getInfo().getDeclaredSymbol();
				defSet = new HashSet<>();
				defSet.add(new Definition(param, declaredParam));
				ImmutableDefinitionSet ids = new ImmutableDefinitionSet(defSet);
				defMap.put(declaredParam, ids);
			}
		}
		return new ReachingDefinitionFlowMap(defMap);
	}

	@Override
	public ReachingDefinitionFlowMap initProcess(Node n, ReachingDefinitionFlowMap flowFactIN) {
		if (n instanceof BeginNode) {
			Node parent = n.getParent();
			if (parent instanceof FunctionDefinition) {
				return new ReachingDefinitionFlowMap(flowFactIN);
			}
		} else if (n instanceof EndNode) {
			Node parent = n.getParent();
			if (parent instanceof FunctionDefinition || parent instanceof CompoundStatement) {
				return new ReachingDefinitionFlowMap(flowFactIN);
			}
		}
		AllDefinitionGetter defGetter = new AllDefinitionGetter();
		n.accept(defGetter);
		if (defGetter.definitionList.isEmpty()) {
			if (n instanceof PostCallNode) {
				return new ReachingDefinitionFlowMap(flowFactIN);
			} else {
				return flowFactIN;
			}
		}

		CellSet redefinedCells = new CellSet();
		for (Definition defined : defGetter.definitionList) {
			if (defined.getCell() == Cell.genericCell) {
				redefinedCells.add(Cell.genericCell);
				break;
			}
			redefinedCells.add(defined.getCell());
		}

		Set<Definition> definedList = defGetter.definitionList;
		ReachingDefinitionFlowMap flowFactOUT = new ReachingDefinitionFlowMap(flowFactIN);
		ExtensibleCellMap<ImmutableDefinitionSet> outDefMap = flowFactOUT.flowMap;
		if (redefinedCells.isUniversal()) {
			for (Cell key : flowFactIN.flowMap.nonGenericKeySet()) {
				ImmutableDefinitionSet inSet = flowFactIN.flowMap.get(key);
				Set<Definition> newDefSet = new HashSet<>();
				// assert (redefinedCells.size() == 1);
				Definition def = Misc.getAnyElement(definedList);
				if (inSet == null || !inSet.contains(def)) {
					newDefSet.add(def);
					if (inSet != null) {
						newDefSet.addAll(inSet);
					}
				}
				if (!newDefSet.isEmpty()) {
					outDefMap.put(key, new ImmutableDefinitionSet(newDefSet));
				}
			}
			if (outDefMap.isUniversal()) {
				ImmutableDefinitionSet inSet = flowFactIN.flowMap.get(Cell.genericCell);
				Set<Definition> newDefSet = new HashSet<>();
				assert (redefinedCells.size() == 1);
				Definition def = Misc.getAnyElement(definedList);
				if (inSet == null || !inSet.contains(def)) {
					newDefSet.add(def);
					if (inSet != null) {
						newDefSet.addAll(inSet);
					}
					outDefMap.put(Cell.genericCell, new ImmutableDefinitionSet(newDefSet));
				}
			}
		} else if (redefinedCells.size() > 1) {
			/*
			 * Conservatively, we assume that none of the writes are
			 * guaranteed to happen. Hence, we do not kill any definitions.
			 */
			for (Cell key : redefinedCells) {
				Set<Definition> inSet = flowFactIN.flowMap.get(key);
				Set<Definition> newDefSet = new HashSet<>();
				for (Definition def : definedList) {
					if (def.getCell() == key) {
						if (inSet == null || !inSet.contains(def)) {
							newDefSet.add(def);
							if (inSet != null) {
								newDefSet.addAll(inSet);
							}
						}
					}
				}
				if (!newDefSet.isEmpty()) {
					outDefMap.put(key, new ImmutableDefinitionSet(newDefSet));
				}
			}
		} else {
			Set<Definition> newDefSet = new HashSet<>();
			assert (redefinedCells.size() == 1 && definedList.size() == 1);
			Cell redefinedCell = redefinedCells.getAnyElement();
			Definition def = Misc.getAnyElement(definedList);
			Set<Definition> inSet = flowFactIN.flowMap.get(redefinedCell);
			assert (def.getCell() == redefinedCell);
			if (inSet == null || inSet.size() != 1 || !inSet.contains(def)) {
				newDefSet.add(def);
				outDefMap.put(redefinedCell, new ImmutableDefinitionSet(newDefSet));
			}
		}
		return flowFactOUT;
	}

	@Override
	public ReachingDefinitionFlowMap writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument,
			ReachingDefinitionFlowMap flowFactIN) {
		Cell paramCell = parameter.getInfo().getDeclaredSymbol();
		Definition newDef = new Definition(parameter, paramCell);
		Set<Definition> newDefSet = new HashSet<>();
		Set<Definition> inSet = flowFactIN.flowMap.get(paramCell);
		if (inSet == null || inSet.size() != 1 || !inSet.contains(newDef)) {
			newDefSet.add(newDef);
			ExtensibleCellMap<ImmutableDefinitionSet> defMap = new ExtensibleCellMap<>(flowFactIN.flowMap);
			defMap.put(newDef.getCell(), new ImmutableDefinitionSet(newDefSet));
			return new ReachingDefinitionFlowMap(defMap);
		}
		return flowFactIN;
	}
}
