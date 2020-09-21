/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.controlflow;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterProceduralControlFlowAnalysis;
import imop.lib.util.CellSet;
import imop.lib.util.ImmutableSet;
import imop.parser.Program;

import java.util.HashSet;
import java.util.Set;

public class ImmutableDominanceAnalysis
		extends InterProceduralControlFlowAnalysis<ImmutableDominanceAnalysis.DominatorFlowFact> {

	public ImmutableDominanceAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
		super(analysisName, analysisDimension);
	}

	@Override
	protected boolean processPostCallNodesIN(PostCallNode node, DominatorFlowFact incompleteIN) {
		return false;
	}

	public static class DominatorFlowFact extends FlowAnalysis.FlowFact {
		public ImmutableSet<Node> dominators;

		public DominatorFlowFact(ImmutableSet<Node> dominators) {
			if (dominators == null) {
				this.dominators = null;
			} else {
				this.dominators = new ImmutableSet<>(dominators);
			}
		}

		@Override
		public boolean isEqualTo(FlowFact other) {
			if (other == null || !(other instanceof DominatorFlowFact)) {
				return false;
			}
			DominatorFlowFact that = (DominatorFlowFact) other;
			if (this == that) {
				return true;
			}
			if (this.dominators == null) {
				if (that.dominators == null) {
					return true;
				} else {
					return false;
				}
			} else {
				if (that.dominators == null) {
					return false;
				} else {
					return this.dominators.equals(that.dominators);
				}
			}
		}

		@Override
		public String getString() {
			String retString = "";
			for (Node def : this.dominators) {
				retString += def + "\n";
			}
			return retString;
		}

		@Override
		public boolean merge(FlowFact other, CellSet cellSet) {
			if (other == null || !(other instanceof DominatorFlowFact)) {
				return false;
			}
			DominatorFlowFact that = (DominatorFlowFact) other;
			if (this == that) {
				return false;
			}
			ImmutableSet<Node> thisDomSet = this.dominators;
			ImmutableSet<Node> thatDomSet = that.dominators;
			if (thatDomSet == null) {
				// Intersection with Universal Set would not change anything.
				return false;
			} else if (thisDomSet == null) {
				// Intersection would yield thatDomSet.
				this.dominators = new ImmutableSet<>(thatDomSet);
				return true;
			} else {
				Set<Node> newSet = new HashSet<>(thisDomSet);
				boolean retVal = newSet.retainAll(thatDomSet);
				this.dominators = new ImmutableSet<>(newSet);
				return retVal;
			}
		}
	} // End of DominatorFlowFact

	public ImmutableDominanceAnalysis() {
		super(AnalysisName.DOMINANCE, new AnalysisDimension(SVEDimension.SVE_INSENSITIVE));
	}

	@Override
	public DominatorFlowFact getTop() {
		return new DominatorFlowFact(null); // TOP is universal set of all nodes; we represent that with a null set.
	}

	@Override
	public DominatorFlowFact getEntryFact() {
		Set<Node> domSet = new HashSet<>();
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main != null) {
			domSet.add(main.getInfo().getCFGInfo().getNestedCFG().getBegin());
		}
		return new DominatorFlowFact(new ImmutableSet<>(domSet));
	}

	@Override
	public DominatorFlowFact initProcess(Node n, DominatorFlowFact flowFactIN) {
		if (flowFactIN.dominators == null) {
			return flowFactIN;
		}
		if (flowFactIN.dominators.contains(n)) {
			return flowFactIN;
		}
		Set<Node> newSet = new HashSet<>(flowFactIN.dominators);
		newSet.add(n);
		DominatorFlowFact flowFactOUT = new DominatorFlowFact(new ImmutableSet<>(newSet));
		return flowFactOUT;
	}

	@Override
	public DominatorFlowFact writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument,
			DominatorFlowFact flowFactIN) {
		if (flowFactIN.dominators == null) {
			return flowFactIN;
		}
		if (flowFactIN.dominators.contains(parameter)) {
			return flowFactIN;
		}
		Set<Node> newSet = new HashSet<>(flowFactIN.dominators);
		newSet.add(parameter);
		DominatorFlowFact flowFactOUT = new DominatorFlowFact(new ImmutableSet<>(newSet));
		return flowFactOUT;
	}
}
