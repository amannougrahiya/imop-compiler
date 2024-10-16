/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.controlflow;

import imop.ast.info.cfgNodeInfo.ExpressionInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.BranchEdge;
import imop.lib.analysis.flowanalysis.SCC;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.InterProceduralControlFlowAnalysis;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.ImmutableList;
import imop.lib.util.ImmutableSet;
import imop.lib.util.Misc;

import java.util.*;

public class CrossCallPredicateAnalysis extends InterProceduralControlFlowAnalysis<PredicateAnalysis.PredicateFlowFact> {

	public CrossCallPredicateAnalysis() {
		super(AnalysisName.CROSSCALL_PREDICATE_ANALYSIS, new AnalysisDimension(SVEDimension.SVE_INSENSITIVE));
	}

	@Override
	public PredicateAnalysis.PredicateFlowFact getTop() {
		return new PredicateAnalysis.PredicateFlowFact(new ImmutableSet<>(new HashSet<>()));
	}

	@Override
	public PredicateAnalysis.PredicateFlowFact getEntryFact() {
		Set<ReversePath> newPathSet = new HashSet<>();
		List<BranchEdge> newList = new LinkedList<>();
		newPathSet.add(new ReversePath(null, new ImmutableList<>(newList)));
		return new PredicateAnalysis.PredicateFlowFact(new ImmutableSet<>(newPathSet));
	}

	@Override
	protected PredicateAnalysis.PredicateFlowFact edgeTransferFunction(PredicateAnalysis.PredicateFlowFact edgeIN, Node predecessor, Node successor) {
		if (predecessor instanceof Expression && Misc.isAPredicate(predecessor)) {
			Set<ReversePath> newPathSet = new HashSet<>();
			BranchEdge be = ((ExpressionInfo) predecessor.getInfo()).getBranchEdge(successor);
			for (ReversePath path : edgeIN.controlPredicatePaths) {
				if (path.edgesOnPath.contains(be)) {
					newPathSet.add(path);
					continue;
				}
				List<BranchEdge> newList = path.getNewList(be);
				ReversePath newPath = new ReversePath(path.startPoint, new ImmutableList<>(newList));
				newPathSet.add(newPath);
			}
			return new PredicateAnalysis.PredicateFlowFact(new ImmutableSet<>(newPathSet));
			// OLD CODE: Now, this is shifted to the visits of BeginNode and
			// BarrierDirective.
			// } else if (predecessor instanceof BarrierDirective
			// || (predecessor instanceof BeginNode && predecessor.getParent() instanceof
			// ParallelConstruct)) {
			// /*
			// * The predecessor corresponds to a BeginPhasePoint.
			// * We need to create a new flow-fact with a single path containing
			// * this BeginPhasePoint.
			// */
			// ReversePath newPath = new ReversePath(
			// BeginPhasePoint.getBeginPhasePoint(new NodeWithStack(predecessor, new
			// CallStack())),
			// new ImmutableList<>(new ArrayList<>()));
			// Set<ReversePath> newPathSet = new HashSet<>();
			// newPathSet.add(newPath);
			// return new PredicateFlowFact(new ImmutableSet<>(newPathSet));
		}
		return edgeIN;
	}

	@Override
	protected boolean processPostCallNodesIN(PostCallNode node, PredicateAnalysis.PredicateFlowFact incompleteIN) {
		return false;
	}

	public void restartAnalysisFromStoredNodes() {
		assert (!SCC.processingTarjan);
		this.autoUpdateTriggerCounter++;
		long localTimer = System.nanoTime();

		/*
		 * From the set of nodes to be updated, we obtain the workList to be
		 * processed.
		 * We add all the entry points of the SCC of each node.
		 */
		this.globalWorkList.recreate();
		for (Node n : nodesToBeUpdated) {
			if (n.getInfo().isConnectedToProgram()) {
				this.globalWorkList.add(n);
			}
		}
		// OLD CODE: Now, if we ever find that a node is unconnected to the program, we
		// remove it from processing.
		this.nodesToBeUpdated.clear();
		// this.nodesToBeUpdated.clear();

		this.safeCurrentSCCNodes = new HashSet<>();
		this.underApproximated = new HashSet<>();
		while (!globalWorkList.isEmpty()) {
			Node nodeToBeAnalyzed = globalWorkList.removeFirstElement();
			CFGInfo nInfo = nodeToBeAnalyzed.getInfo().getCFGInfo();
			if (nInfo.getSCC() == null) {
				// Here, node itself is an SCC. We do not require two rounds.
				this.nodesProcessedDuringUpdate++;
				this.debugRecursion(nodeToBeAnalyzed);
				this.processWhenNotUpdated(nodeToBeAnalyzed); // Directly invoke the second round processing.
				continue;
			} else {
				/*
				 * This node belongs to an SCC. We should process the whole of
				 * SCC in the first phase, followed by its processing in the
				 * second phase, and only then shall we move on to the next SCC.
				 */
				stabilizeSCCOf(nodeToBeAnalyzed);
			}
		}

		localTimer = System.nanoTime() - localTimer;
		this.flowAnalysisUpdateTimer += localTimer;
	}

	@Override
	public PredicateAnalysis.PredicateFlowFact writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument,
			PredicateAnalysis.PredicateFlowFact flowFactOne) {
		return flowFactOne;
	}

	@Override
	public PredicateAnalysis.PredicateFlowFact visit(BeginNode n, PredicateAnalysis.PredicateFlowFact flowFactOne) {
		if (n.getParent() instanceof ParallelConstruct) {
			ReversePath newPath = new ReversePath(n, new ImmutableList<>(new LinkedList<>()));
			Set<ReversePath> newPathSet = new HashSet<>();
			newPathSet.add(newPath);
			this.globalWorkList.add(n.getParent().getInfo().getCFGInfo().getNestedCFG().getEnd()); // TODO: Verify.
			return new PredicateAnalysis.PredicateFlowFact(new ImmutableSet<>(newPathSet));
		} else {
			return flowFactOne;
		}
	}

	@Override
	public PredicateAnalysis.PredicateFlowFact visit(BarrierDirective n, PredicateAnalysis.PredicateFlowFact flowFactOne) {
		ReversePath newPath = new ReversePath(n, new ImmutableList<>(new LinkedList<>()));
		Set<ReversePath> newPathSet = new HashSet<>();
		newPathSet.add(newPath);
		return new PredicateAnalysis.PredicateFlowFact(new ImmutableSet<>(newPathSet));
	}

	@Override
	public PredicateAnalysis.PredicateFlowFact visit(EndNode n, PredicateAnalysis.PredicateFlowFact flowFactIN) {
		if (n.getParent() instanceof ParallelConstruct) {
			BeginNode begin = n.getParent().getInfo().getCFGInfo().getNestedCFG().getBegin();
			PredicateAnalysis.PredicateFlowFact beginIN = (PredicateAnalysis.PredicateFlowFact) begin.getInfo().getIN(AnalysisName.CROSSCALL_PREDICATE_ANALYSIS);
			if (beginIN == null) {
				return new PredicateAnalysis.PredicateFlowFact(new ImmutableSet<>());
			} else {
				return new PredicateAnalysis.PredicateFlowFact(beginIN);
			}
		} else {
			return flowFactIN;
		}

	}
}
