/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.dataflow;

import imop.ast.info.cfgNodeInfo.ParameterDeclarationInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.Assignment;
import imop.lib.analysis.AssignmentGetter;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.CellularDataFlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterThreadForwardCellularAnalysis;
import imop.lib.util.CellSet;
import imop.lib.util.ExtensibleCellMap;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.List;

public class CopyPropagationAnalysis
		extends InterThreadForwardCellularAnalysis<CopyPropagationAnalysis.CopyPropagationFlowMap> {

	public CopyPropagationAnalysis() {
		super(AnalysisName.COPYPROPAGATION, new AnalysisDimension(Program.sveSensitivityOfIDFAEdges));
	}

	public static class CopyPropagationFlowMap extends CellularDataFlowAnalysis.CellularFlowMap<Cell> {

		public CopyPropagationFlowMap(ExtensibleCellMap<Cell> flowMap) {
			super(flowMap);
		}

		public CopyPropagationFlowMap(CopyPropagationFlowMap thatFlowFact) {
			super(thatFlowFact);
		}

		@Override
		public String getAnalysisNameKey() {
			return "copy";
		}

		@Override
		public Cell meet(Cell c1, Cell c2) {
			if (c1 == null) {
				return c2;
			} else if (c2 == null) {
				return c1;
			}
			if (c1 == c2) {
				return c1;
			} else {
				return Cell.genericCell;
			}
		}

	}

	@Override
	public CopyPropagationFlowMap getTop() {
		ExtensibleCellMap<Cell> copyMap = new ExtensibleCellMap<>();
		return new CopyPropagationFlowMap(copyMap);
	}

	@Override
	public CopyPropagationFlowMap getEntryFact() {
		ExtensibleCellMap<Cell> copyMap = new ExtensibleCellMap<>();
		TranslationUnit root = Program.getRoot();
		for (Node elementChoice : root.getF0().getNodes()) {
			ElementsOfTranslation elemTransChoice = ((ElementsOfTranslation) elementChoice);
			Node extDeclChoice = elemTransChoice.getF0().getChoice();
			if (extDeclChoice instanceof ExternalDeclaration) {
				ExternalDeclaration extDecl = ((ExternalDeclaration) extDeclChoice);
				Node declChoice = extDecl.getF0().getChoice();
				if (declChoice instanceof Declaration) {
					// Note: This declaration may be that of a Function or a Variable
					Declaration declaration = (Declaration) declChoice;
					List<String> declaratorNameList = declaration.getInfo().getIDNameList();
					if (declaration.getInfo().isTypedef()) {
						continue;
					}
					if (declaratorNameList.size() != 1) {
						continue;
					}
					if (!declaration.getInfo().hasInitializer()) {
						continue;
					}
					addAssignmentToMap(declaration, copyMap);
				}
			}
		}

		return new CopyPropagationFlowMap(copyMap);
	}

	/**
	 * Adds the assignment, if any, from {@code node} to the {@code copyMap}.
	 * 
	 * @param declaration
	 * @param copyMap
	 * @return
	 *         true, if any changes were made to the copyMap.
	 */
	private void addAssignmentToMap(Node declaration, ExtensibleCellMap<Cell> copyMap) {
		List<Assignment> assignmentList = AssignmentGetter.getInterProceduralAssignments(declaration);
		if (assignmentList.size() != 1) {
			return;
		}
		Assignment assign = assignmentList.get(0);
		CellSet lhsSet = assign.getLHSLocations();
		if (lhsSet.size() != 1) {
			return;
		}
		CellSet rhsSet = assign.getRHSLocations();
		if (rhsSet.size() != 1) {
			return;
		}
		Cell lhsCell = lhsSet.getAnyElement();
		Cell rhsCell = rhsSet.getAnyElement();
		if (lhsCell instanceof Symbol && rhsCell instanceof Symbol) {
			copyMap.put(lhsCell, rhsCell);
		}
		return;

	}

	@Override
	public CopyPropagationFlowMap initProcess(Node n, CopyPropagationFlowMap flowFactIN) {
		if (n instanceof BeginNode) {
			Node parent = n.getParent();
			if (parent instanceof FunctionDefinition) {
				return new CopyPropagationFlowMap(flowFactIN);
			}
		}
		if (n instanceof EndNode) {
			Node parent = n.getParent();
			if (parent instanceof FunctionDefinition || parent instanceof CompoundStatement) {
				return new CopyPropagationFlowMap(flowFactIN);
			}
		}
		CopyPropagationFlowMap flowFactOUT = flowFactIN;
		CellSet writes = new CellSet(n.getInfo().getWrites());
		if (writes.isEmpty()) {
			if (n instanceof PostCallNode) {
				return new CopyPropagationFlowMap(flowFactIN);
			} else {
				return flowFactIN;
			}
		}
		flowFactOUT = new CopyPropagationFlowMap(flowFactIN);

		/*
		 * Step 1: Check if this node is a copy instruction.
		 */
		List<Assignment> assignList = AssignmentGetter.getInterProceduralAssignments(n);
		Cell lhsCell = null;
		Cell rhsCell = null;
		Assignment assign = null;
		boolean isCopyInstruction = true;
		if (assignList.size() != 1) {
			isCopyInstruction = false;
		} else {
			assign = assignList.get(0);
			if (!assign.isCopyInstruction()) {
				isCopyInstruction = false;

			}
		}

		if (isCopyInstruction) {
			lhsCell = assign.getLHSLocations().getAnyElement();
			rhsCell = assign.getRHSLocations().getAnyElement();
		}

		if (!isCopyInstruction) {
			/*
			 * Step 2.A : In this scenario, all variables that are written here
			 * should kill the existing copy mappings related to them.
			 * Note that in "(IN-KILL) U GEN", the GEN is empty for this case.
			 */
			if (writes.isUniversal()) {
				flowFactOUT.flowMap = new ExtensibleCellMap<>();
				return flowFactOUT;
			} else {
				ExtensibleCellMap<Cell> map = flowFactOUT.flowMap;
				for (Cell w : writes) {
					if (!(w instanceof Symbol)) {
						continue;
					}
					// Change all mappings a->w to a->G;
					for (Cell a : map.nonGenericKeySet()) {
						if (map.get(a) == w) {
							map.put(a, Cell.genericCell);
						}
					}

					// Override the existing mapping of w with w->G, or add if there is none;
					map.put(w, Cell.genericCell);
				}
				return flowFactOUT;
			}
		} else {
			/*
			 * Step 2.B: Given this copy instruction, let's update the copy map
			 * now. As per the data-flow equation "OUT = (IN-KILL) U GEN", let's
			 * first kill and then generate.
			 */

			// Change all mappings a->lhsCell to a->G;
			boolean foundMirror = false;
			ExtensibleCellMap<Cell> map = flowFactOUT.flowMap;
			for (Cell a : map.nonGenericKeySet()) {
				/*
				 * UPDATE: Now, we ensure that "b=a" does not kill a flow-fact
				 * "a:b".
				 * In fact, in this case, we do not add "b:a".
				 */
				if (a == rhsCell && map.get(a) == lhsCell) {
					foundMirror = true;
					continue;
				}
				if (map.get(a) == lhsCell) {
					map.put(a, Cell.genericCell);
				}
			}

			// Add the new/existing mapping lhsCell->rhsCell;
			if (!foundMirror) {
				map.put(lhsCell, rhsCell);
			}
			return flowFactOUT;
		}
	}

	@Override
	public CopyPropagationFlowMap writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument,
			CopyPropagationFlowMap flowFactIN) {
		if (argument.isAConstant()) {
			return flowFactIN;
		}
		NodeToken rootParamNodeToken = ParameterDeclarationInfo.getRootParamNodeToken(parameter);
		Cell lhsCell = Misc.getSymbolOrFreeEntry(rootParamNodeToken);
		if (lhsCell == null || !(lhsCell instanceof Symbol)) {
			return flowFactIN;
		}
		Cell rhsCell = Misc.getSymbolOrFreeEntry(argument.getIdentifier());
		if (rhsCell == null || !(rhsCell instanceof Symbol)) {
			return flowFactIN;
		}
		Cell oldCell = flowFactIN.flowMap.get(lhsCell);
		if (oldCell == rhsCell) {
			return flowFactIN;
		} else {
			CopyPropagationFlowMap flowFactOUT = new CopyPropagationFlowMap(flowFactIN);
			flowFactOUT.flowMap.put(lhsCell, rhsCell);
			return flowFactOUT;
		}
	}
}
