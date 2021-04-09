/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.util.CellSet;
import imop.lib.util.ExtensibleCellMap;
import imop.lib.util.Immutable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class CellularDataFlowAnalysis<F extends CellularDataFlowAnalysis.CellularFlowMap<?>>
		extends DataFlowAnalysis<F> {

	public CellularDataFlowAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
		super(analysisName, analysisDimension);
	}

	public abstract static class CellularFlowMap<V extends Immutable> extends FlowAnalysis.FlowFact {
		public ExtensibleCellMap<V> flowMap;

		public CellularFlowMap(ExtensibleCellMap<V> flowMap) {
			this.flowMap = flowMap;
		}

		public CellularFlowMap(CellularFlowMap<V> thatFlowFact) {
			this.flowMap = new ExtensibleCellMap<>(thatFlowFact.flowMap);
		}

		@Override
		public final boolean isEqualTo(FlowFact other) {
			if (other == null || !(other instanceof CellularFlowMap<?>)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			CellularFlowMap<V> that = (CellularFlowMap<V>) other;
			if (this == that) {
				return true;
			}
			return this.flowMap.equals(that.flowMap);
		}

		@Override
		public final boolean merge(FlowFact other, CellSet cellSet) {
			if (other == null || !(other instanceof CellularFlowMap<?>)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			CellularFlowMap<V> that = (CellularFlowMap<V>) other;
			return this.flowMap.mergeWith(that.flowMap, (s1, s2) -> this.meet(s1, s2), cellSet);
		}

		@Override
		public final String getString() {
			String retString = "";
			String analysisName = this.getAnalysisNameKey();
			retString += "[";
			if (flowMap.isUniversal()) {
				retString += analysisName + "(globalCell) := " + flowMap.get(Cell.genericCell);
			}
			Set<Cell> cellSet = flowMap.nonGenericKeySet();
			List<Cell> cellList = new ArrayList<>(cellSet);
			Collections.sort(cellList);
			for (Cell c : cellList) {
				retString += analysisName + "(" + c.toString() + ") := ";
				V value = flowMap.get(c);
				if (value != null) {
					retString += value.toString();
				} else {
					retString += "NULL";
				}
				retString += ";\n";
			}
			retString += "]";
			return retString;
		}

		/**
		 * Obtain a one-word string that represents the name of this data-flow
		 * fact,
		 * for debugging purposes.
		 * </br>
		 * For example, {@code ptsTo} is the one-word string that represents
		 * the flow-fact for points-to analysis.
		 * 
		 * @return
		 *         one-word name for this flow-fact.
		 */
		public abstract String getAnalysisNameKey();

		/**
		 * Obtain meet of the two elements of the data-flow lattice --
		 * {@code v1}
		 * and {@code v2}.
		 * 
		 * @param v1
		 *           an element of the data-flow lattice.
		 * @param v2
		 *           an element of the data-flow lattice.
		 * @return
		 *         meet of {@code v1} and {@code v2}; an element of the
		 *         data-flow
		 *         lattice.
		 */
		public abstract V meet(V v1, V v2);
	}
}
