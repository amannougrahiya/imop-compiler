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
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterThreadBackwardNonCellularAnalysis;
import imop.lib.util.CellCollection;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.HashMap;

public class LivenessAnalysis extends InterThreadBackwardNonCellularAnalysis<LivenessAnalysis.LivenessFlowFact> {

	public static class LivenessFlowFact extends FlowAnalysis.FlowFact {
		public HashMap<Node, CellSet> livenessInfo = new HashMap<>();

		@Override
		public boolean isEqualTo(FlowFact other) {
			if (other == null || !(other instanceof LivenessFlowFact)) {
				return false;
			}
			LivenessFlowFact that = (LivenessFlowFact) other;
			if (this == that) {
				return true;
			}
			return this.livenessInfo.equals(that.livenessInfo);
			// Old Code:
			// if (this.livenessInfo.size() != that.livenessInfo.size()) {
			// return false;
			// }
			// for (Node successor : this.livenessInfo.keySet()) {
			// if (!that.livenessInfo.containsKey(successor)) {
			// return false;
			// }
			// CellSet thisCellSet = this.livenessInfo.get(successor);
			// CellSet thatCellSet = that.livenessInfo.get(successor);
			// if (thisCellSet.size() != thatCellSet.size()) {
			// return false;
			// }
			// for (Cell cell : thisCellSet) {
			// if (!thatCellSet.contains(cell)) {
			// // assert(!thisCellSet.toString().equals(thatCellSet.toString()));
			// return false;
			// }
			// }
			// }
			// return true;
		}

		@Override
		public String getString() {
			String retString = "";
			for (Node successor : this.livenessInfo.keySet()) {
				retString += "Live symbols on the successor node " + successor + " are "
						+ this.livenessInfo.get(successor) + "\n";
			}
			return retString;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean merge(FlowFact other, CellSet cellSet) {
			assert (other instanceof LivenessFlowFact);
			LivenessFlowFact that = (LivenessFlowFact) other;
			if (this == that) {
				return false;
			}
			if (that.livenessInfo.isEmpty()) {
				return false;
			}

			if (this.livenessInfo.isEmpty()) {
				for (Node key : that.livenessInfo.keySet()) {
					this.livenessInfo.put(key, (CellSet) that.livenessInfo.get(key).clone());
				}
				return true;
			}

			boolean changed = false;
			for (Node successor : that.livenessInfo.keySet()) {
				if (!this.livenessInfo.containsKey(successor)) {
					changed = true;
					if (cellSet != null) {
						CellSet newThisSet = new CellSet(that.livenessInfo.get(successor));
						newThisSet.retainAll(cellSet);
						this.livenessInfo.put(successor, newThisSet);
					} else {
						this.livenessInfo.put(successor, new CellSet(that.livenessInfo.get(successor)));
					}
				} else {
					CellSet thisCellSet = this.livenessInfo.get(successor);
					CellSet thatCellSet = that.livenessInfo.get(successor);
					CellSet minusSet = new CellSet();
					if (thisCellSet.isUniversal()) {
						for (Cell thisCell : Cell.allCells) {
							if (!thatCellSet.contains(thisCell)) {
								changed = true;
								minusSet.add(thisCell);
							}
						}
						CellCollection.addCollectionToDerived(minusSet);
					} else {
						for (Cell thisCell : thisCellSet) {
							if (!thatCellSet.contains(thisCell)) {
								changed = true;
								minusSet.add(thisCell);
							}
						}
					}
					thisCellSet.removeAll(minusSet);
				}
			}
			return changed;
		}
	}

	public LivenessAnalysis() {
		super(AnalysisName.LIVENESS, new AnalysisDimension(Program.sveSensitive));
	}

	@Override
	public LivenessFlowFact getTop() {
		// We represent TOP by an empty map.
		LivenessFlowFact flowFact = new LivenessFlowFact();
		return flowFact;
	}

	@Override
	public LivenessFlowFact getEntryFact() {
		return getTop();
	}

	@Override
	public LivenessFlowFact initProcess(Node n, LivenessFlowFact flowFactOUT) {
		LivenessFlowFact flowFactIN = new LivenessFlowFact();
		CellSet genSet = new CellSet(n.getInfo().getReads());
		CellSet killSet = new CellSet(n.getInfo().getWrites());
		CellSet outSet = new CellSet();
		for (Node successor : flowFactOUT.livenessInfo.keySet()) {
			outSet.addAll(flowFactOUT.livenessInfo.get(successor));
		}
		CellSet inSet = new CellSet(genSet);
		if (killSet.isEmpty()) {
			inSet.addAll(outSet);
		} else {
			inSet.addAll(Misc.setMinus(outSet, killSet));
		}

		flowFactIN.livenessInfo.put(n, inSet);
		return flowFactIN;
	}

	@Override
	public LivenessFlowFact writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument,
			LivenessFlowFact flowFactOUT) {
		LivenessFlowFact flowFactIN = new LivenessFlowFact();
		CellSet genSet = new CellSet();
		if (argument.isAnIdentifier()) {
			genSet.add(Misc.getSymbolOrFreeEntry(argument.getIdentifier()));
		}
		CellSet killSet = new CellSet();
		killSet.add(Misc.getSymbolOrFreeEntry(ParameterDeclarationInfo.getRootParamNodeToken(parameter)));

		CellSet outSet = new CellSet();
		for (Node successor : flowFactOUT.livenessInfo.keySet()) {
			outSet.addAll(flowFactOUT.livenessInfo.get(successor));
		}

		CellSet inSet = new CellSet(genSet);
		inSet.addAll(Misc.setMinus(outSet, killSet));
		flowFactIN.livenessInfo.put(parameter, inSet);
		return flowFactIN;
	}

}
