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
import imop.lib.analysis.flowanalysis.HeapCell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterThreadForwardNonCellularAnalysis;
import imop.lib.util.CellList;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;

public class HeapValidityAnalysis extends InterThreadForwardNonCellularAnalysis<HeapValidityAnalysis.ValidityFlowFact> {

	public static class ValidityFlowFact extends FlowAnalysis.FlowFact {
		public CellSet validHeapCells;

		public ValidityFlowFact(CellSet validHeapCells) {
			this.validHeapCells = validHeapCells;
		}

		@Override
		public boolean isEqualTo(FlowFact other) {
			if (other == null || !(other instanceof ValidityFlowFact)) {
				return false;
			}
			ValidityFlowFact that = (ValidityFlowFact) other;
			if (this == that) {
				return true;
			}
			if (this.validHeapCells == null) {
				if (that.validHeapCells == null) {
					return true;
				} else {
					return false;
				}
			} else {
				if (that.validHeapCells == null) {
					return false;
				} else {
					if (this.validHeapCells.isUniversal()) {
						if (that.validHeapCells.isUniversal()) {
							return true;
						} else {
							return false;
						}
					} else {
						if (that.validHeapCells.isUniversal()) {
							return false;
						} else {
							return this.validHeapCells.equals(that.validHeapCells);
						}
					}
				}
			}
		}

		@Override
		public String getString() {
			String retString = "";
			if (!this.validHeapCells.isEmpty()) {
				retString = "[";
				if (validHeapCells.isUniversal()) {
					retString += Cell.genericCell.toString() + ";";
				} else {
					for (Cell cell : validHeapCells) {
						if (cell instanceof HeapCell) {
							retString += cell.toString() + "; ";
						}
					}
				}
				retString += "]";
			}
			return retString;
		}

		@Override
		public boolean merge(FlowFact other, CellSet cellSet) {
			if (other == null || !(other instanceof ValidityFlowFact)) {
				return false;
			}
			ValidityFlowFact that = (ValidityFlowFact) other;
			if (this == that) {
				return false;
			}
			CellSet thisValidSet = this.validHeapCells;
			CellSet thatValidSet = that.validHeapCells;
			if (thatValidSet == null || thatValidSet.isUniversal()) {
				// Intersection with Universal Set would not change anything.
				return false;
			} else if (thisValidSet == null || thisValidSet.isUniversal()) {
				// Intersection would yield thatDomSet.
				this.validHeapCells = new CellSet(thatValidSet);
				return true;
			} else {
				return thisValidSet.retainAll(thatValidSet);
			}
		}

	}

	public HeapValidityAnalysis() {
		super(AnalysisName.HEAP_VALIDITY, new AnalysisDimension(SVEDimension.SVE_INSENSITIVE));
	}

	@Override
	/**
	 * @return
	 *         a global set of cells, containing all the elements.
	 */
	public ValidityFlowFact getTop() {
		CellSet entrySet = new CellSet();
		entrySet.add(Cell.genericCell);
		ValidityFlowFact entryFF = new ValidityFlowFact(entrySet);
		return entryFF;
	}

	@Override
	/**
	 * @return
	 *         a global set of cells, containing all the elements.
	 */
	public ValidityFlowFact getEntryFact() {
		return this.getTop();
	}

	@Override
	public ValidityFlowFact initProcess(Node n, ValidityFlowFact flowFactIN) {
		if (n instanceof PostCallNode) {
			CallStatement callStmt = ((PostCallNode) n).getParent();
			String funcName = callStmt.getFunctionDesignatorNode().toString();
			if (funcName.equals("free")) {
				/*
				 * Remove all those heap elements that are present in the
				 * points-to set of the variable being free'd.
				 */
				ValidityFlowFact flowFactOUT = flowFactIN;
				SimplePrimaryExpression ptrArg = callStmt.getPreCallNode().getArgumentList().get(0);
				if (ptrArg.isAConstant()) {
					return flowFactOUT;
				} else {
					Cell ptrCell = Misc.getSymbolOrFreeEntry(ptrArg.getIdentifier());
					if (!(ptrCell instanceof Symbol)) {
						return flowFactOUT;
					} else {
						Symbol ptrSymbol = (Symbol) ptrCell;
						CellSet heapSet = new CellSet();
						for (Object obj : ptrSymbol.getPointsTo(n).getReadOnlyInternal().stream()
								.filter(c -> c instanceof HeapCell).toArray()) {
							HeapCell heapCell = (HeapCell) obj;
							heapSet.add(heapCell);
						}
						if (!Misc.doIntersect(flowFactIN.validHeapCells, heapSet)) {
							return flowFactOUT;
						} else {
							CellSet newSet = Misc.setMinus(flowFactIN.validHeapCells, heapSet);
							flowFactOUT = new ValidityFlowFact(newSet);
							return flowFactOUT;
						}
					}
				}
			} else if (funcName.equals("malloc") || funcName.equals("calloc") || funcName.equals("realloc")) {
				CellList validList = HeapCell.getHeapCells(callStmt);
				if (flowFactIN.validHeapCells.isUniversal()) {
					return flowFactIN;
				} else {
					// If the set needs an update, create a new flow fact and return.
					if (Misc.subsumes(flowFactIN.validHeapCells.getReadOnlyInternal(),
							validList.getReadOnlyInternal())) {
						return flowFactIN;
					} else {
						CellSet newSet = new CellSet(flowFactIN.validHeapCells);
						newSet.addAll(validList);
						ValidityFlowFact flowFactOUT = new ValidityFlowFact(newSet);
						return flowFactOUT;
					}
				}
			} else {
				return flowFactIN;
			}
		} else {
			return flowFactIN;
		}
	}

	@Override
	public ValidityFlowFact writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument,
			ValidityFlowFact flowFactIN) {
		return flowFactIN;
	}

}
