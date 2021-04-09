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
import imop.lib.util.CellMap;
import imop.lib.util.CellSet;

import java.util.HashSet;
import java.util.Set;

public class DataDependenceForward
		extends InterThreadForwardNonCellularAnalysis<DataDependenceForward.DataDependenceForwardFF> {

	public static class DataDependenceForwardFF extends FlowAnalysis.FlowFact {
		public CellMap<NodeSet> writeSources;
		public CellMap<NodeSet> readSources;

		public DataDependenceForwardFF(CellMap<NodeSet> writeSources, CellMap<NodeSet> readSources) {
			this.writeSources = writeSources;
			this.readSources = readSources;
		}

		@Override
		public boolean isEqualTo(FlowFact other) {
			if (other == null || !(other instanceof DataDependenceForwardFF)) {
				return false;
			}
			DataDependenceForwardFF that = (DataDependenceForwardFF) other;
			if (this == that) {
				return true;
			}
			if (this.writeSources.equals(that.writeSources) && this.readSources.equals(that.readSources)) {
				return true;
			}
			return false;
		}

		@Override
		public String getString() {
			String retString = "";
			retString += this.getStringOfField(this.writeSources, "write-sources");
			retString += this.getStringOfField(this.readSources, "read-sources");
			return retString;
		}

		private String getStringOfField(CellMap<NodeSet> map, String fieldName) {
			String retString = "\n";
			if (map.isUniversal()) {
				retString += "\nFor the generic cell, " + fieldName + " are:";
				for (Node node : map.get(Cell.genericCell).getReadOnlySet()) {
					retString += "\n\t" + node;
				}
			} else {
				for (Cell cell : map.keySetExpanded()) {
					if (cell instanceof Symbol) {
						Symbol sym = (Symbol) cell;
						retString += "\nFor " + sym.getName() + ", " + fieldName + " are:";
						for (Node node : map.get(sym).getReadOnlySet()) {
							retString += "\n\t" + node;
						}
					} else if (cell instanceof HeapCell) {
						HeapCell heapCell = (HeapCell) cell;
						retString += "\nFor " + heapCell.getLocation() + ", " + fieldName + " are:";
						for (Node node : map.get(heapCell).getReadOnlySet()) {
							retString += "\n\t" + node;
						}
					}
				}
			}
			retString += "\n";
			return retString;
		}

		@Override
		public boolean merge(FlowFact other, CellSet cellSet) {
			if (other == null || !(other instanceof DataDependenceForwardFF)) {
				return false;
			}
			boolean changed = false;
			DataDependenceForwardFF that = (DataDependenceForwardFF) other;
			if (this == that) {
				return false;
			}
			changed = changed | getMergedFromThat(this.writeSources, that.writeSources, cellSet);
			changed = changed | getMergedFromThat(this.readSources, that.readSources, cellSet);
			return changed;
		}

		private static boolean getMergedFromThat(CellMap<NodeSet> thisMap, CellMap<NodeSet> thatMap, CellSet cellSet) {
			if (thisMap == thatMap) {
				return false;
			}
			boolean changed = false;

			if (cellSet == null || cellSet.isUniversal()) {
				Set<Cell> thisNGCells = thisMap.nonGenericKeySet();
				Set<Cell> thatNGCells = thatMap.nonGenericKeySet();
				for (Cell thatNGCell : thatNGCells) {
					if (thisNGCells.contains(thatNGCell)) {
						NodeSet thisVal = thisMap.get(thatNGCell);
						changed |= thisVal.union(thatMap.get(thatNGCell));
					} else {
						NodeSet newSet;
						if (thisMap.isUniversal()) {
							newSet = new NodeSet(thisMap.get(Cell.genericCell));
						} else {
							newSet = new NodeSet(new HashSet<>(2));
						}
						changed |= newSet.union(thatMap.get(thatNGCell));
						thisMap.put(thatNGCell, newSet);
					}
				}
				if (thatMap.isUniversal()) {
					NodeSet thatGlobalVal = thatMap.get(Cell.genericCell);
					if (thisMap.isUniversal()) {
						NodeSet thisGlobalVal = thisMap.get(Cell.genericCell);
						changed |= thisGlobalVal.union(thatGlobalVal);
					}
					for (Cell c : thisNGCells) {
						if (!thatNGCells.contains(c)) {
							NodeSet thisVal = thisMap.get(c);
							changed |= thisVal.union(thatGlobalVal);
						}
					}
				}
			} else {
				Set<Cell> thisNGCells = thisMap.nonGenericKeySet();
				Set<Cell> thatNGCells = thatMap.nonGenericKeySet();
				for (Cell thatNGCell : thatNGCells) {
					if (cellSet.contains(thatNGCell)) {
						if (thisNGCells.contains(thatNGCell)) {
							NodeSet thisVal = thisMap.get(thatNGCell);
							changed |= thisVal.union(thatMap.get(thatNGCell));
						} else {
							NodeSet newSet;
							if (thisMap.isUniversal()) {
								newSet = new NodeSet(thisMap.get(Cell.genericCell));
							} else {
								newSet = new NodeSet(new HashSet<>(2));
							}
							changed |= newSet.union(thatMap.get(thatNGCell));
							thisMap.put(thatNGCell, newSet);
						}
					}
				}
				if (thatMap.isUniversal()) {
					NodeSet thatGlobalVal = thatMap.get(Cell.genericCell);
					for (Cell c : cellSet) {
						if (!(thisNGCells.contains(c) || thatNGCells.contains(c))) {
							thisMap.put(c, new NodeSet(thatGlobalVal));
						}
					}
					for (Cell c : thisNGCells) {
						if (cellSet.contains(c)) {
							if (!thatNGCells.contains(c)) {
								NodeSet thisVal = thisMap.get(c);
								changed |= thisVal.union(thatGlobalVal);
							}
						}
					}
				}
			}
			// Old Code:
			// if (cellSet == null) {
			// if (thatMap.isUniversal()) {
			//
			// } else {
			// for (Cell sym : thatMap.keySetExpanded()) {
			// NodeSet thatFlowSet = thatMap.get(sym);
			// if (thatFlowSet != null) {
			// NodeSet thisFlowSet = thisMap.get(sym);
			// if (thisFlowSet != null) {
			// changed = changed | thisFlowSet.union(thatFlowSet);
			// } else {
			// changed = true;
			// thisMap.put(sym, new NodeSet(thatFlowSet));
			// }
			// }
			// }
			// }
			// } else {
			// if (cellSet.isUniversal()) {
			// for (Cell sym : Cell.allCells) {
			// NodeSet thatFlowSet = thatMap.get(sym);
			// if (thatFlowSet != null) {
			// NodeSet thisFlowSet = thisMap.get(sym);
			// if (thisFlowSet != null) {
			// changed = changed | thisFlowSet.union(thatFlowSet);
			// } else {
			// changed = true;
			// thisMap.put(sym, new NodeSet(thatFlowSet));
			// }
			// }
			// }
			// } else {
			// for (Cell sym : cellSet) {
			// NodeSet thatFlowSet = thatMap.get(sym);
			// if (thatFlowSet != null) {
			// NodeSet thisFlowSet = thisMap.get(sym);
			// if (thisFlowSet != null) {
			// changed = changed | thisFlowSet.union(thatFlowSet);
			// } else {
			// changed = true;
			// thisMap.put(sym, new NodeSet(thatFlowSet));
			// }
			// }
			// }
			// }
			// }
			return changed;
		}

	}

	public DataDependenceForward() {
		super(AnalysisName.DATA_DEPENDENCE_FORWARD, new AnalysisDimension(SVEDimension.SVE_INSENSITIVE));
	}

	@Override
	public DataDependenceForwardFF getTop() {
		return new DataDependenceForwardFF(new CellMap<>(), new CellMap<>());
	}

	@Override
	public DataDependenceForwardFF getEntryFact() {
		return this.getTop();
	}

	@Override
	public DataDependenceForwardFF visit(EndNode n, DataDependenceForwardFF flowFactIN) {
		Node parent = n.getParent();
		if (parent instanceof CompoundStatement) {
			CompoundStatement compStmt = (CompoundStatement) parent;
			CellMap<NodeSet> writeSources = getCopyOf(flowFactIN.writeSources);
			CellMap<NodeSet> readSources = getCopyOf(flowFactIN.readSources);
			DataDependenceForwardFF flowFactOUT = new DataDependenceForwardFF(writeSources, readSources);

			if (!writeSources.isUniversal()) {
				for (Symbol sym : compStmt.getInfo().getSymbolTable().values()) {
					writeSources.remove(sym);
				}
			}
			if (!readSources.isUniversal()) {
				for (Symbol sym : compStmt.getInfo().getSymbolTable().values()) {
					readSources.remove(sym);
				}
			}
			return flowFactOUT;
		} else {
			return flowFactIN;
		}
	}

	@Override
	public DataDependenceForwardFF initProcess(Node n, DataDependenceForwardFF flowFactIN) {
		CellSet writes = new CellSet(n.getInfo().getWrites());
		CellSet reads = new CellSet(n.getInfo().getReads());

		if (writes.isEmpty() && reads.isEmpty()) {
			return flowFactIN;
		}

		CellMap<NodeSet> writeSources = getCopyOf(flowFactIN.writeSources);
		CellMap<NodeSet> readSources = getCopyOf(flowFactIN.readSources);
		DataDependenceForwardFF flowFactOUT = new DataDependenceForwardFF(writeSources, readSources);

		// Update writeSources.
		if (writes.size() == 1) {
			Cell sym = writes.getAnyElement();
			Set<Node> sourceSet = new HashSet<>(1);
			sourceSet.add(n);
			writeSources.put(sym, new NodeSet(sourceSet));
		} else {
			if (writes.isUniversal()) {
				NodeSet existingWriteSources = writeSources.get(Cell.genericCell);
				if (existingWriteSources != null) {
					existingWriteSources.addNode(n);
				} else {
					Set<Node> sourceSet = new HashSet<>(2);
					sourceSet.add(n);
					writeSources.put(Cell.genericCell, new NodeSet(sourceSet));
				}
			} else {
				writes.applyAllExpanded(sym -> {
					NodeSet existingWriteSources = writeSources.get(sym);
					if (existingWriteSources != null) {
						existingWriteSources.addNode(n);
					} else {
						Set<Node> sourceSet = new HashSet<>(2);
						sourceSet.add(n);
						writeSources.put(sym, new NodeSet(sourceSet));
					}
				});
			}
		}

		// Update readSources.
		if (writes.size() == 1) {
			Cell sym = writes.getAnyElement();
			if (readSources.containsKey(sym)) {
				readSources.get(sym).clear();
			}
		}

		if (reads.isUniversal()) {
			NodeSet existingReadSources = readSources.get(Cell.genericCell);
			if (existingReadSources != null) {
				existingReadSources.addNode(n);
			} else {
				Set<Node> sourceSet = new HashSet<>(2);
				sourceSet.add(n);
				readSources.put(Cell.genericCell, new NodeSet(sourceSet));
			}
		} else {
			reads.applyAllExpanded(sym -> {
				if (writes.size() != 1 || !writes.contains(sym)) {
					NodeSet existingReadSources = readSources.get(sym);
					if (existingReadSources != null) {
						existingReadSources.addNode(n);
					} else {
						Set<Node> sourceSet = new HashSet<>(2);
						sourceSet.add(n);
						readSources.put(sym, new NodeSet(sourceSet));
					}
				}
			});

		}
		return flowFactOUT;
	}

	@Override
	public DataDependenceForwardFF writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument,
			DataDependenceForwardFF flowFactIN) {
		return this.initProcess(parameter, flowFactIN);
	}

	private static CellMap<NodeSet> getCopyOf(CellMap<NodeSet> in) {
		CellMap<NodeSet> out = new CellMap<>();
		if (in.isUniversal()) {
			out.put(Cell.genericCell, new NodeSet(in.get(Cell.genericCell)));
			for (Cell sym : in.nonGenericKeySet()) {
				out.put(sym, new NodeSet(in.get(sym)));
			}
		} else {
			for (Cell sym : in.keySetExpanded()) {
				out.put(sym, new NodeSet(in.get(sym)));
			}
		}
		return out;
	}

}
