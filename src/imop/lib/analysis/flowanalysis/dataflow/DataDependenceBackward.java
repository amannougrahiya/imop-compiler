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
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterThreadBackwardNonCellularAnalysis;
import imop.lib.util.CellList;
import imop.lib.util.CellMap;
import imop.lib.util.CellSet;
import imop.parser.Program;

import java.util.HashSet;
import java.util.Set;

public class DataDependenceBackward
		extends InterThreadBackwardNonCellularAnalysis<DataDependenceBackward.DataDependenceBackwardFF> {

	public static class DataDependenceBackwardFF extends FlowAnalysis.FlowFact {
		public CellMap<NodeSet> writeDestinations;
		public CellMap<NodeSet> readDestinations;

		public DataDependenceBackwardFF(CellMap<NodeSet> writeDestinations, CellMap<NodeSet> readDestinations) {
			this.writeDestinations = writeDestinations;
			this.readDestinations = readDestinations;
		}

		@Override
		public boolean isEqualTo(FlowFact other) {
			if (other == null || !(other instanceof DataDependenceBackwardFF)) {
				return false;
			}
			DataDependenceBackwardFF that = (DataDependenceBackwardFF) other;
			if (this == that) {
				return true;
			}
			if (this.writeDestinations.equals(that.writeDestinations)
					&& this.readDestinations.equals(that.readDestinations)) {
				return true;
			}
			return false;
		}

		@Override
		public String getString() {
			String retString = "";
			retString += this.getStringOfField(this.writeDestinations, "write-destinations");
			retString += this.getStringOfField(this.readDestinations, "read-destinations");
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
			if (other == null || !(other instanceof DataDependenceBackwardFF)) {
				return false;
			}
			boolean changed = false;
			DataDependenceBackwardFF that = (DataDependenceBackwardFF) other;
			if (this == that) {
				return false;
			}
			changed = changed | getMergedFromThat(this.writeDestinations, that.writeDestinations, cellSet);
			changed = changed | getMergedFromThat(this.readDestinations, that.readDestinations, cellSet);
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
			// if (thisMap == thatMap) {
			// return false;
			// }
			// boolean changed = false;
			//
			// if (cellSet == null) {
			// if (thatMap.isUniversal()) {
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
			//
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

	public DataDependenceBackward() {
		super(AnalysisName.DATA_DEPENDENCE_BACKWARD, new AnalysisDimension(Program.sveSensitive));
	}

	@Override
	public DataDependenceBackwardFF getTop() {
		return new DataDependenceBackwardFF(new CellMap<>(), new CellMap<>());
	}

	@Override
	public DataDependenceBackwardFF getEntryFact() {
		return this.getTop();
	}

	@Override
	public DataDependenceBackwardFF visit(BeginNode n, DataDependenceBackwardFF flowFactOUT) {
		Node parent = n.getParent();
		if (parent instanceof CompoundStatement) {
			CompoundStatement compStmt = (CompoundStatement) parent;
			CellMap<NodeSet> writeDestinations = getCopyOf(flowFactOUT.writeDestinations);
			CellMap<NodeSet> readDestinations = getCopyOf(flowFactOUT.readDestinations);
			DataDependenceBackwardFF flowFactIN = new DataDependenceBackwardFF(writeDestinations, readDestinations);

			if (!writeDestinations.isUniversal()) {
				for (Symbol sym : compStmt.getInfo().getSymbolTable().values()) {
					writeDestinations.remove(sym);
				}
			}
			if (!readDestinations.isUniversal()) {
				for (Symbol sym : compStmt.getInfo().getSymbolTable().values()) {
					readDestinations.remove(sym);
				}
			}
			return flowFactIN;
		} else {
			return flowFactOUT;
		}
	}

	@Override
	public DataDependenceBackwardFF initProcess(Node n, DataDependenceBackwardFF flowFactOUT) {
		CellList writes = n.getInfo().getWrites();
		CellList reads = n.getInfo().getReads();

		if (writes.isEmpty() && reads.isEmpty()) {
			return flowFactOUT;
		}

		CellMap<NodeSet> writeDestinations = getCopyOf(flowFactOUT.writeDestinations);
		CellMap<NodeSet> readDestinations = getCopyOf(flowFactOUT.readDestinations);
		DataDependenceBackwardFF flowFactIN = new DataDependenceBackwardFF(writeDestinations, readDestinations);

		// Update writeDestinations.
		if (writes.size() == 1) {
			Cell sym = writes.get(0);
			Set<Node> destinationSet = new HashSet<>(2);
			destinationSet.add(n);
			writeDestinations.put(sym, new NodeSet(destinationSet));
		} else {
			if (writes.isUniversal()) {
				NodeSet existingWriteDestinations = writeDestinations.get(Cell.genericCell);
				if (existingWriteDestinations != null) {
					existingWriteDestinations.addNode(n);
				} else {
					Set<Node> destinationSet = new HashSet<>(2);
					destinationSet.add(n);
					writeDestinations.put(Cell.genericCell, new NodeSet(destinationSet));
				}
			} else {
				writes.applyAllExpanded(sym -> {
					NodeSet existingWriteDestinations = writeDestinations.get(sym);
					if (existingWriteDestinations != null) {
						existingWriteDestinations.addNode(n);
					} else {
						Set<Node> destinationSet = new HashSet<>(2);
						destinationSet.add(n);
						writeDestinations.put(sym, new NodeSet(destinationSet));
					}
				});
			}
		}

		// Update readDestinations.
		if (writes.size() == 1) {
			Cell sym = writes.get(0);
			if (readDestinations.containsKey(sym)) {
				readDestinations.get(sym).clear();
			}
		}
		if (reads.isUniversal()) {
			NodeSet existingReadDestinations = readDestinations.get(Cell.genericCell);
			if (existingReadDestinations != null) {
				existingReadDestinations.addNode(n);
			} else {
				Set<Node> destinationSet = new HashSet<>(2);
				destinationSet.add(n);
				readDestinations.put(Cell.genericCell, new NodeSet(destinationSet));
			}
		} else {
			reads.applyAllExpanded(sym -> {
				NodeSet existingReadDestinations = readDestinations.get(sym);
				if (existingReadDestinations != null) {
					existingReadDestinations.addNode(n);
				} else {
					Set<Node> destinationSet = new HashSet<>(2);
					destinationSet.add(n);
					readDestinations.put(sym, new NodeSet(destinationSet));
				}
			});
		}

		return flowFactIN;
	}

	@Override
	public DataDependenceBackwardFF writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument,
			DataDependenceBackwardFF flowFactOUT) {
		return this.initProcess(parameter, flowFactOUT);
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
