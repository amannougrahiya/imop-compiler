/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.dataflow.HeapValidityAnalysis;
import imop.lib.analysis.flowanalysis.dataflow.HeapValidityAnalysis.ValidityFlowFact;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis.PointsToFlowMap;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.analysis.typeSystem.PointerType;
import imop.lib.util.CellList;
import imop.lib.util.CellSet;
import imop.lib.util.ImmutableCellSet;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.HashMap;

public class HeapCell extends Cell {
	public static enum Allocator {
		MALLOC, CALLOC, REALLOC, NONE
	}

	public final Allocator allocatorType;
	public final CallStatement allocatorNode;
	public final ParameterDeclaration pointingParam;
	public final int heapId;
	private AddressCell addressCell;
	public static int counter = 0;
	public int cachedHashCode = -1;

	private static HashMap<ParameterDeclaration, HeapCell> unknownParamCells = new HashMap<>();

	public HeapCell() {
		this.allocatorNode = null;
		this.allocatorType = null;
		this.pointingParam = null;
		this.heapId = counter++;
		if (!dontSave) {
			allCells.add(this);
		}
	}

	public HeapCell(Allocator allocatorType, ParameterDeclaration param) {
		this.allocatorType = allocatorType;
		this.allocatorNode = null;
		this.pointingParam = param;
		this.heapId = counter++;
		if (!dontSave) {
			allCells.add(this);
		}
	}

	public HeapCell(Allocator allocatorType, CallStatement allocatorNode) {
		this.allocatorType = allocatorType;
		this.allocatorNode = allocatorNode;
		this.pointingParam = null;
		this.heapId = counter++;
		if (!dontSave) {
			allCells.add(this);
			// System.err.println(this.allocatorNode);
		}
	}

	private static HashMap<CallStatement, CellList> heapElements = new HashMap<>();

	public static CellList getHeapCells(CallStatement callStmt) {
		CellList cellList = new CellList();
		if (heapElements.containsKey(callStmt)) {
			cellList.addAll(heapElements.get(callStmt));
		} else {
			if (callStmt.getInfo().getCalledDefinitions().isEmpty()) {
				for (Cell calledCell : callStmt.getInfo().getCalledSymbols()) {
					if (calledCell instanceof Symbol) {
						Symbol calledSymbol = (Symbol) calledCell;
						String funcName = calledSymbol.getName();
						if (funcName.equals("malloc")) {
							cellList.add(new HeapCell(Allocator.MALLOC, callStmt));
						} else if (funcName.equals("calloc")) {
							cellList.add(new HeapCell(Allocator.CALLOC, callStmt));
						} else if (funcName.equals("realloc")) {
							// Change this later.
							cellList.add(new HeapCell(Allocator.REALLOC, callStmt));
						} else if (funcName.equals("polybench_alloc_data")) {
							cellList.add(new HeapCell(Allocator.MALLOC, callStmt));
						}
					}
				}
			}
			heapElements.put(callStmt, cellList);
		}
		return cellList;
	}

	public static void resetStaticFields() {
		HeapCell.unknownParamCells = new HashMap<>();
		HeapCell.heapElements = new HashMap<>();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof HeapCell)) {
			return false;
		}
		HeapCell that = (HeapCell) obj;
		if (this == that) {
			return true;
		}
		if (this.allocatorNode == that.allocatorNode && this.pointingParam == that.pointingParam) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (cachedHashCode == -1) {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((allocatorNode == null) ? 0 : allocatorNode.hashCode());
			result = prime * result + ((pointingParam == null) ? 0 : pointingParam.hashCode());
			cachedHashCode = result;
		}
		return cachedHashCode;
	}

	public int getLocation() {
		if (this == Cell.genericCell) {
			return -1;
		} else if (this.allocatorNode == null) {
			if (this.pointingParam == null) {
				return -1;
			} else {
				return Misc.getLineNum(this.pointingParam);
			}
		} else {
			return Misc.getLineNum(this.allocatorNode);
		}
	}

	@Override
	public CellSet deprecated_getPointsTo(Node node) {
		node = Misc.getCFGNodeFor(node);
		if (Program.basePointsTo) {
			return this.getDefaultPointsTo(node);
		} else {
			CellSet retSet = new CellSet();
			PointsToFlowMap pff = (PointsToFlowMap) node.getInfo().getIN(AnalysisName.POINTSTO);
			if (pff == null) {
				return retSet; // return an empty points-to set.
			} else {
				retSet = pff.flowMap.get(this);
				if (retSet == null) {
					retSet = new CellSet();
				}
				return retSet;
			}
		}
	}

	@Override
	public ImmutableCellSet getPointsTo(Node node) {
		node = Misc.getCFGNodeFor(node);
		if (Program.basePointsTo) {
			return new ImmutableCellSet(this.getDefaultPointsTo(node));
		} else {
			ImmutableCellSet retSet;
			PointsToFlowMap pff = (PointsToFlowMap) node.getInfo().getIN(AnalysisName.POINTSTO, this);
			if (pff == null) {
				return new ImmutableCellSet(); // return an empty points-to set.
			} else {
				retSet = pff.flowMap.get(this);
				if (retSet == null) {
					return new ImmutableCellSet();
				}
				return retSet;
			}
		}
	}

	public CellSet getDefaultPointsTo(Node node) {
		CellSet pointsToSet = new CellSet();
		pointsToSet.add(Cell.genericCell);
		return pointsToSet;
	}

	public Cell getAddressCell() {
		if (addressCell == null) {
			addressCell = new AddressCell(this);
		}
		return addressCell;
	}

	@Override
	public String toString() {
		return "heapCell#" + this.heapId;
	}

	public static HeapCell getUnknownParamPointee(ParameterDeclaration paramDecl) {
		if (paramDecl.toString().trim().equals("void")) {
			return null;
		}
		Cell declaredSym = paramDecl.getInfo().getDeclaredSymbol();
		assert (declaredSym instanceof Symbol);
		Symbol sym = (Symbol) declaredSym;
		if (!(sym.getType() instanceof ArrayType) && !(sym.getType() instanceof PointerType)) {
			return null;
		}
		if (unknownParamCells.containsKey(paramDecl)) {
			return unknownParamCells.get(paramDecl);
		} else {
			HeapCell newCell = new HeapCell(Allocator.NONE, paramDecl);
			unknownParamCells.put(paramDecl, newCell);
			return newCell;
		}
	}

	public boolean isValidAt(Node n) {
		n = Misc.getCFGNodeFor(n);
		HeapValidityAnalysis.ValidityFlowFact ff = (ValidityFlowFact) n.getInfo().getIN(AnalysisName.HEAP_VALIDITY);
		if (ff == null) {
			return false;
		}
		if (ff.validHeapCells.contains(this)) {
			return true;
		} else {
			return false;
		}
	}

}
