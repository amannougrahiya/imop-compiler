/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

import imop.ast.node.external.Node;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.util.CellSet;
import imop.lib.util.ExtensibleCellMap;
import imop.lib.util.Immutable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public abstract class CellularDataFlowAnalysis<F extends CellularDataFlowAnalysis.CellularFlowMap<?>>
		extends DataFlowAnalysis<F> {

	/**
	 * A simple (node, IDFA) pair, to be used as element-type of stack of nodes for
	 * which transfer-function applications are underway.
	 *
	 * @author aman
	 *
	 */
	public static class NodeAnalysisPair {
		public final Node node;
		public final CellularDataFlowAnalysis<?> analysis;

		public NodeAnalysisPair(Node node, CellularDataFlowAnalysis<?> analysis) {
			this.node = node;
			this.analysis = analysis;
		}
	}

	public static final Stack<NodeAnalysisPair> nodeAnalysisStack = new Stack<>();

	public static boolean accessedCellValueChanged;

	public CellularDataFlowAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
		super(analysisName, analysisDimension);
	}

	public abstract static class CellularFlowMap<V extends Immutable> extends FlowAnalysis.FlowFact {
		protected ExtensibleCellMap<V> flowMap;

		public CellularFlowMap(ExtensibleCellMap<V> flowMap) {
			this.flowMap = flowMap;
			// Here, we need to consider all entries of flowMap as "accessed" for the
			// current (node, IDFA), if any.
			// Note that this is a special case scenario. Nowhere else can we set the
			// flowMap manually.
			// Hence, it's only here that we need to manually mark the cells as accessed.
			// Note: For PTA, this constructor is being invoked only with an empty flow-map.
			if (flowMap != null && !CellularDataFlowAnalysis.nodeAnalysisStack.isEmpty()) {
				NodeAnalysisPair nap = CellularDataFlowAnalysis.nodeAnalysisStack.peek();
				if (nap != null && !flowMap.nonGenericKeySet().isEmpty()) {
					if (flowMap.isUniversal()) {
						nap.node.getInfo().getAccessedCellSets(nap.analysis).add(Cell.genericCell);
					} else {
						nap.node.getInfo().getAccessedCellSets(nap.analysis).addAll(flowMap.nonGenericKeySet());
					}
				}
			}
		}

		public CellularFlowMap(CellularFlowMap<V> thatFlowFact) {
			this.flowMap = new ExtensibleCellMap<>(thatFlowFact); // By not passing thatFlowFact.flowMap directly, we
																	// are ensuring that we do not consider the cells in
																	// the argument as "accessed".
		}

		@SuppressWarnings("unchecked")
		public void initFallBackForExtensibleCellMapWith(ExtensibleCellMap<?> ext) {
			this.flowMap.initFallBackMapWith((ExtensibleCellMap<V>) ext);
		}

		public ExtensibleCellMap<V> getFlowMap() {
			return flowMap;
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
			return this.getFlowMap().equals(that.getFlowMap());
		}

		public final boolean mergeWithAccessed(FlowFact other, CellSet cellSet, CellSet accessedSet) {
			if (other == null || !(other instanceof CellularFlowMap<?>)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			CellularFlowMap<V> that = (CellularFlowMap<V>) other;
			return this.getFlowMap().mergeWithAccessed(that.getFlowMap(), (s1, s2) -> this.meet(s1, s2), cellSet,
					accessedSet);
		}

		@Override
		public final boolean merge(FlowFact other, CellSet cellSet) {
			if (other == null || !(other instanceof CellularFlowMap<?>)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			CellularFlowMap<V> that = (CellularFlowMap<V>) other;
			return this.getFlowMap().mergeWith(that.getFlowMap(), (s1, s2) -> this.meet(s1, s2), cellSet);
		}

		@Override
		/**
		 * Test that this innocent-looking addition doesn't break anything.
		 * This addition was done carelessly without looking at the semantics of
		 * RestrictedSets.
		 */
		public String toString() {
			return this.getString();
		}

		@Override
		public final String getString() {
			String retString = "";
			String analysisName = this.getAnalysisNameKey();
			retString += "[";
			if (getFlowMap().isUniversal()) {
				retString += analysisName + "(globalCell) := " + getFlowMap().get(Cell.genericCell);
			}
			Set<Cell> cellSet = getFlowMap().nonGenericKeySet();
			List<Cell> cellList = new ArrayList<>(cellSet);
			Collections.sort(cellList, new Comparator<Cell>() {
				@Override
				public int compare(Cell o1, Cell o2) {
					int val = o1.toString().compareTo(o2.toString());
					if (val == 0) {
						val = o1.compareTo(o2);
					}
					return val;
				}
			});
			for (Cell c : cellList) {
				retString += analysisName + "(" + c.toString() + ") := ";
				V value = getFlowMap().get(c);
				if (value instanceof CellSet) {
					Set<Cell> rhsCells = new HashSet<>(((CellSet) value).getReadOnlyInternal());
					List<Cell> rhsCellList = new ArrayList<>(rhsCells);
					Collections.sort(rhsCellList, new Comparator<Cell>() {
						@Override
						public int compare(Cell o1, Cell o2) {
							int val = o1.toString().compareTo(o2.toString());
							if (val == 0) {
								val = o1.compareTo(o2);
							}
							return val;
						}
					});
					retString += rhsCellList.toString();
				} else {
					if (value != null) {
						retString += value.toString();
					} else {
						retString += "NULL";
					}
				}
				retString += ";\n";
			}
			retString += "]";
			return retString;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof FlowFact)) {
				return false;
			}
			FlowFact other = (FlowFact) obj;
			return this.isEqualTo(other);
		}

		@Override
		public int hashCode() {
			return this.getFlowMap().hashCode();
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
