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
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterProceduralControlFlowAnalysis;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.CellSet;
import imop.lib.util.ImmutableList;
import imop.lib.util.ImmutableSet;
import imop.lib.util.Misc;

import java.util.*;

public class PredicateAnalysis extends InterProceduralControlFlowAnalysis<PredicateAnalysis.PredicateFlowFact> {

	public PredicateAnalysis() {
		super(AnalysisName.PREDICATE_ANALYSIS, new AnalysisDimension(SVEDimension.SVE_INSENSITIVE));
	}

	public static class PredicateFlowFact extends FlowAnalysis.FlowFact {

		public ImmutableSet<ReversePath> controlPredicatePaths;
		private static int maxNumberOfPaths = 0;

		public PredicateFlowFact(PredicateFlowFact other) {
			this.controlPredicatePaths = other.controlPredicatePaths;
		}

		public PredicateFlowFact(ImmutableSet<ReversePath> controlPredicatePaths) {
			this.controlPredicatePaths = controlPredicatePaths;
			if (controlPredicatePaths.size() > maxNumberOfPaths) {
				maxNumberOfPaths = controlPredicatePaths.size();
				// System.err.println("New max number of paths: " + maxNumberOfPaths);
			}
		}

		@Override
		public boolean isEqualTo(FlowFact other) {
			if (other == null || !(other instanceof PredicateFlowFact)) {
				return false;
			}
			PredicateFlowFact that = (PredicateFlowFact) other;
			if (this == that) {
				return true;
			}
			if (this.controlPredicatePaths == null) {
				if (that.controlPredicatePaths == null) {
					return true;
				} else {
					return false;
				}
			} else {
				if (that.controlPredicatePaths == null) {
					return false;
				} else {
					return this.controlPredicatePaths.equals(that.controlPredicatePaths);
				}
			}
		}

		@Override
		public String getString() {
			if (this.controlPredicatePaths.isEmpty()) {
				return "";
			}
			String retString = "predFlowFact";
			retString = "{";
			for (ReversePath path : this.controlPredicatePaths) {
				retString += path.toString();
			}
			retString += "}";
			return retString;
		}

		@Override
		public boolean merge(FlowFact other, CellSet cellSet) {
			if (other == null || !(other instanceof PredicateFlowFact)) {
				return false;
			}
			PredicateFlowFact that = (PredicateFlowFact) other;
			if (this == that) {
				return false;
			}
			ImmutableSet<ReversePath> thisPathSet = this.controlPredicatePaths;
			ImmutableSet<ReversePath> thatPathSet = that.controlPredicatePaths;
			if (thatPathSet == null) {
				return false;
			} else if (thisPathSet == null) {
				this.controlPredicatePaths = that.controlPredicatePaths;
				return true;
			} else {
				Set<ReversePath> newPathSet = new HashSet<>();
				newPathSet.addAll(thisPathSet);
				newPathSet.addAll(thatPathSet);
				Set<ReversePath> simplifiedPathSet = PredicateFlowFact.simplifyPaths(newPathSet);
				if (!simplifiedPathSet.equals(thisPathSet)) {
					this.controlPredicatePaths = new ImmutableSet<>(simplifiedPathSet);
					return true;
				}
				return false;
			}
		}

		private static Set<ReversePath> simplifyPaths(Set<ReversePath> pathSet) {
			pathSet = PredicateFlowFact.fusePredicateBranches(pathSet);
			pathSet = PredicateFlowFact.obtainPrefixPaths(pathSet);
			return pathSet;
		}

		/**
		 * Given a set of {@link ReversePath}, this method creates the simplified * set
		 * of paths by removing all those
		 * paths whose suffix exists as any other path in the set.
		 *
		 * @param pathSet
		 *
		 * @return
		 */
		private static Set<ReversePath> obtainPrefixPaths(Set<ReversePath> pathSet) {
			Set<ReversePath> pathsToBeRemoved = new HashSet<>();
			for (ReversePath shorterPath : pathSet) {
				if (pathsToBeRemoved.contains(shorterPath)) {
					continue;
				}
				for (ReversePath longerPath : pathSet) {
					if (pathsToBeRemoved.contains(longerPath)) {
						continue;
					}
					if (shorterPath.isStrictlySubsumedAsSuffixOf(longerPath)) {
						// Remove longer path.
						pathsToBeRemoved.add(longerPath);
					}
				}
			}
			if (pathsToBeRemoved.isEmpty()) {
				return pathSet;
			}
			Set<ReversePath> returnSet = new HashSet<>(pathSet);
			returnSet.removeAll(pathsToBeRemoved);
			return returnSet;
		}

		/**
		 * Given a set of {@link ReversePath}, this method creates the simplified paths
		 * as follows:
		 * <ul>
		 * <li>If there exists any predicate whose all possible branches are
		 * present as the first elements of any of the paths, then from all
		 * those paths that contain any of these branches as their first
		 * elements, the entry for such branches as first elements are removed,
		 * while generating the set of simplified paths.
		 * </li>
		 * <li>Note that this process is applied recursively, until a
		 * fixed-point is reached.
		 * </ul>
		 *
		 * @param pathSet
		 *
		 * @return
		 */
		private static Set<ReversePath> fusePredicateBranches(Set<ReversePath> pathSet) {
			Set<Expression> predicateToRemove = new HashSet<>();
			Set<BranchEdge> tempBuffer = new HashSet<>();
			for (ReversePath path : pathSet) {
				if (path.edgesOnPath.isEmpty()) {
					continue;
				}
				BranchEdge firstBE = path.edgesOnPath.get(0);
				Collection<?> otherBranches = firstBE.getOthers();
				if (otherBranches.isEmpty() || tempBuffer.containsAll(otherBranches)) {
					predicateToRemove.add(firstBE.predicate);
					tempBuffer.removeAll(otherBranches);
				} else {
					tempBuffer.add(firstBE);
				}
			}
			if (predicateToRemove.isEmpty()) {
				return pathSet;
			}
			Set<ReversePath> returnSet = new HashSet<>();
			for (ReversePath path : pathSet) {
				if (path.edgesOnPath.isEmpty()) {
					returnSet.add(path);
					continue;
				}
				BranchEdge firstBE = path.edgesOnPath.get(0);
				if (!predicateToRemove.contains(firstBE.predicate)) {
					returnSet.add(path);
					continue;
				}
				List<BranchEdge> newList = new LinkedList<>(path.edgesOnPath);
				newList.remove(0);
				ImmutableList<BranchEdge> newEdgePath = new ImmutableList<>(newList);
				ReversePath newPath = new ReversePath(path.bPP, newEdgePath);
				returnSet.add(newPath);
			}
			return PredicateFlowFact.fusePredicateBranches(returnSet);
		}
	} // End of PredicateFlowFact

	@Override
	public PredicateFlowFact getTop() {
		return new PredicateFlowFact(new ImmutableSet<>(new HashSet<>()));
	}

	@Override
	public PredicateFlowFact getEntryFact() {
		Set<ReversePath> newPathSet = new HashSet<>();
		List<BranchEdge> newList = new LinkedList<>();
		newPathSet.add(new ReversePath(null, new ImmutableList<>(newList)));
		return new PredicateFlowFact(new ImmutableSet<>(newPathSet));
	}

	@Override
	protected PredicateFlowFact edgeTransferFunction(PredicateFlowFact edgeIN, Node predecessor, Node successor) {
		if (predecessor instanceof Expression && Misc.isAPredicate(predecessor)) {
			Set<ReversePath> newPathSet = new HashSet<>();
			BranchEdge be = ((ExpressionInfo) predecessor.getInfo()).getBranchEdge(successor);
			for (ReversePath path : edgeIN.controlPredicatePaths) {
				if (path.edgesOnPath.contains(be)) {
					newPathSet.add(path);
					continue;
				}
				List<BranchEdge> newList = path.getNewList(be);
				ReversePath newPath = new ReversePath(path.bPP, new ImmutableList<>(newList));
				newPathSet.add(newPath);
			}
			return new PredicateFlowFact(new ImmutableSet<>(newPathSet));
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
	protected boolean processPostCallNodesIN(PostCallNode node, PredicateFlowFact incompleteIN) {
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
		this.workList.recreate();
		for (Node n : nodesToBeUpdated) {
			if (n.getInfo().isConnectedToProgram()) {
				boolean added = this.workList.add(n);
			}
		}
		// OLD CODE: Now, if we ever find that a node is unconnected to the program, we
		// remove it from processing.
		this.nodesToBeUpdated.clear();
		// this.nodesToBeUpdated.clear();

		this.processedInThisUpdate = new HashSet<>();
		this.yetToBeFinalized = new HashSet<>();
		while (!workList.isEmpty()) {
			Node nodeToBeAnalyzed = workList.removeFirstElement();
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
	public PredicateFlowFact writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument,
			PredicateFlowFact flowFactOne) {
		return flowFactOne;
	}

	@Override
	public PredicateFlowFact visit(BeginNode n, PredicateFlowFact flowFactOne) {
		if (n.getParent() instanceof ParallelConstruct) {
			ReversePath newPath = new ReversePath(
					BeginPhasePoint.getBeginPhasePoint(new NodeWithStack(n, new CallStack())),
					new ImmutableList<>(new LinkedList<>()));
			Set<ReversePath> newPathSet = new HashSet<>();
			newPathSet.add(newPath);
			this.workList.add(n.getParent().getInfo().getCFGInfo().getNestedCFG().getEnd()); // TODO: Verify.
			return new PredicateFlowFact(new ImmutableSet<>(newPathSet));
		} else {
			return flowFactOne;
		}
	}

	@Override
	public PredicateFlowFact visit(BarrierDirective n, PredicateFlowFact flowFactOne) {
		ReversePath newPath = new ReversePath(BeginPhasePoint.getBeginPhasePoint(new NodeWithStack(n, new CallStack())),
				new ImmutableList<>(new LinkedList<>()));
		Set<ReversePath> newPathSet = new HashSet<>();
		newPathSet.add(newPath);
		return new PredicateFlowFact(new ImmutableSet<>(newPathSet));
	}

	@Override
	public PredicateFlowFact visit(EndNode n, PredicateFlowFact flowFactIN) {
		if (n.getParent() instanceof ParallelConstruct) {
			BeginNode begin = n.getParent().getInfo().getCFGInfo().getNestedCFG().getBegin();
			PredicateFlowFact beginIN = (PredicateFlowFact) begin.getInfo().getIN(AnalysisName.PREDICATE_ANALYSIS);
			if (beginIN == null) {
				return new PredicateFlowFact(new ImmutableSet<>());
			} else {
				return new PredicateFlowFact(beginIN);
			}
		} else {
			return flowFactIN;
		}

	}
}
