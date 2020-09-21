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
import imop.parser.Program;

import java.util.HashSet;
import java.util.Set;

public class DominanceAnalysis extends InterProceduralControlFlowAnalysis<DominanceAnalysis.DominatorFlowFact> {

	public DominanceAnalysis() {
		super(AnalysisName.DOMINANCE, new AnalysisDimension(SVEDimension.SVE_INSENSITIVE));
	}

	public static class DominatorFlowFact extends FlowAnalysis.FlowFact {
		public Set<Node> dominators;

		public DominatorFlowFact(Set<Node> dominators) {
			this.dominators = dominators;
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
			Set<Node> thisDomSet = this.dominators;
			Set<Node> thatDomSet = that.dominators;
			if (thatDomSet == null) {
				// Intersection with Universal Set would not change anything.
				return false;
			} else if (thisDomSet == null) {
				// Intersection would yield thatDomSet.
				this.dominators = new HashSet<>(thatDomSet);
				return true;
			} else {
				return thisDomSet.retainAll(thatDomSet);
			}
		}
	} // End of DominatorFlowFact

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
		return new DominatorFlowFact(domSet);
	}

	@Override
	public DominatorFlowFact initProcess(Node n, DominatorFlowFact flowFactIN) {
		if (flowFactIN.dominators == null) {
			return flowFactIN;
		}
		if (flowFactIN.dominators.contains(n)) {
			return flowFactIN;
		}
		DominatorFlowFact flowFactOUT = new DominatorFlowFact(new HashSet<>(flowFactIN.dominators));
		flowFactOUT.dominators.add(n);
		return flowFactOUT;
	}

	@Override
	protected boolean processPostCallNodesIN(PostCallNode node, DominatorFlowFact incompleteIN) {
		return false;
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
		DominatorFlowFact flowFactOUT = new DominatorFlowFact(new HashSet<>(flowFactIN.dominators));
		flowFactOUT.dominators.add(parameter);
		return flowFactOUT;
	}
}
