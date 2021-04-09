/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.BranchEdge;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.controlflow.PredicateAnalysis.PredicateFlowFact;
import imop.lib.analysis.flowanalysis.controlflow.ReversePath;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.analysis.mhp.yuan.YPhase;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.CollectorVisitor;
import imop.lib.util.Misc;
import imop.lib.util.NodePair;
import imop.lib.util.NodePhasePair;
import imop.parser.Program;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CoExistenceChecker {
	public static Set<NodePair> knownCoExistingNodes = new HashSet<>();
	public static Set<NodePair> knownNonCoExistingNodes = new HashSet<>();
	static Set<NodePhasePair> knownCoExistingNodesInPhase = new HashSet<>();
	static Set<NodePhasePair> knownNonCoExistingNodesInPhase = new HashSet<>();
	static Set<Node> existForAll = new HashSet<>();
	static Set<Node> doesNotExistForAll = new HashSet<>();

	public static void resetStaticFields() {
		CoExistenceChecker.knownCoExistingNodesInPhase.clear();
		CoExistenceChecker.knownNonCoExistingNodesInPhase.clear();
		CoExistenceChecker.knownCoExistingNodes.clear();
		CoExistenceChecker.knownNonCoExistingNodes.clear();
	}

	public static void flushCoExistenceCaches() {
		knownCoExistingNodes.clear();
		knownNonCoExistingNodes.clear();
		knownCoExistingNodesInPhase.clear();
		knownNonCoExistingNodesInPhase.clear();
		existForAll.clear();
		doesNotExistForAll.clear();
		// clearCache();
	}

	public static void clearCache() {
		/*
		 * TODO: Check why this method has not been invoked in
		 * flushCoExistenceCaches().
		 */
		CoExistenceChecker.knownCoExistingNodesInPhase.clear();
		CoExistenceChecker.knownNonCoExistingNodesInPhase.clear();
	}

	/**
	 * Removes all caching related to {@code node} from
	 * {@link CoExistenceChecker#knownCoExistingNodesInPhase} and
	 * {@link CoExistenceChecker#knownNonCoExistingNodesInPhase}.
	 *
	 * @param node
	 *             a node that has to be removed from its position in the AST.
	 */
	public static void removeNodeCache(Node node) {
		Set<NodePhasePair> toRemove = new HashSet<>();
		for (NodePhasePair nPair : knownCoExistingNodesInPhase) {
			if (nPair.one == node || nPair.two == node) {
				toRemove.add(nPair);
			}
		}
		knownCoExistingNodesInPhase.removeAll(toRemove);
		toRemove = new HashSet<>();
		for (NodePhasePair nPair : knownNonCoExistingNodesInPhase) {
			if (nPair.one == node || nPair.two == node) {
				toRemove.add(nPair);
			}
		}
		knownNonCoExistingNodesInPhase.removeAll(toRemove);
	}

	/**
	 * Checks if there exists any phase in which n1 and n2 may co-exist with each
	 * other.
	 *
	 * @param n1
	 * @param n2
	 *
	 * @return true if there exists any common phase for n1 and n2, where both may
	 *         co-exist with each other; false
	 *         otherwise.
	 */
	public static boolean canCoExistInAnyPhase(Node n1, Node n2) {
		if (Program.sveSensitive == SVEDimension.SVE_INSENSITIVE) {
			return true;
		}
		assert Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YUANMHP : "Unexpected path";
		NodePair nP = new NodePair(n1, n2);
		if (knownCoExistingNodes.contains(nP)) {
			return true;
		}
		if (knownNonCoExistingNodes.contains(nP)) {
			return false;
		}
		Set<? extends AbstractPhase<?, ?>> n1Phases = n1.getInfo().getNodePhaseInfo().getPhaseSet();
		Set<? extends AbstractPhase<?, ?>> n2Phases = n2.getInfo().getNodePhaseInfo().getPhaseSet();
		Set<? extends AbstractPhase<?, ?>> commonPhases;
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			commonPhases = Misc.setIntersection((Set<YPhase>) n1Phases, (Set<YPhase>) n2Phases);
		} else {
			commonPhases = Misc.setIntersection((Set<Phase>) n1Phases, (Set<Phase>) n2Phases);
		}
		for (AbstractPhase<?, ?> ph : commonPhases) {
			if (CoExistenceChecker.canCoExistInPhase(n1, n2, ph)) {
				knownCoExistingNodes.add(nP);
				return true;
			}
		}
		knownNonCoExistingNodes.add(nP);
		return false;
	}

	public static boolean canCoExistInPhase(Node n1, Node n2, AbstractPhase<?, ?> ph) {
		assert Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YUANMHP : "Unexpected path";
		// TODO: Check the following heuristic.
		if (Program.sveNoCheck && n1 instanceof BarrierDirective && n2 instanceof BarrierDirective) {
			return false;
		}
		long sveTimerThis = System.nanoTime();
		boolean retVal = CoExistenceChecker.canCoExistInPhase(n1, n2, ph, new HashSet<>(), new HashSet<>());
		sveTimerThis = System.nanoTime() - sveTimerThis;
		SVEChecker.sveTimer += sveTimerThis;
		return retVal;
	}

	/**
	 * Assuming that nodes {@code n1} and {@code n2} may happen in parallel, this
	 * method conservatively checks if it is
	 * possible for different threads to encounter n1 and n2, using the SVE
	 * (single-valued expression) analysis on the
	 * common control predicates of both the nodes.
	 *
	 * @param n1
	 *                  a CFG node.
	 * @param n2
	 *                  a CFG node.
	 * @param ph
	 *                  an abstract phase.
	 * @param nodePairs
	 *                  pair of nodes for which the query is being asked
	 *                  recursively.
	 *
	 * @return true, if both the nodes may be executed by different threads together
	 *         in any runtime instance of phase
	 *         {@code ph}.
	 */
	private static boolean canCoExistInPhase(Node n1, Node n2, AbstractPhase<?, ?> ph, Set<NodePair> nodePairs,
			Set<Expression> expSet) {
		if (Program.sveSensitive == SVEDimension.SVE_INSENSITIVE) {
			return true;
		}
		if (Program.sveNoCheck && n1 instanceof BarrierDirective && n2 instanceof BarrierDirective) {
			return false;
		}
		// TODO: This is newly added if statement to take care of queries during phase
		// updates.
		if (!n1.getInfo().getNodePhaseInfo().getPhaseSet().contains(ph)) {
			Misc.warnDueToLackOfFeature(
					"Could not find a node in the given phase, while invoking CoExistenceChecker.canCoExistInPhase()",
					null);
			return true;
		} else if (!n2.getInfo().getNodePhaseInfo().getPhaseSet().contains(ph)) {
			Misc.warnDueToLackOfFeature(
					"Could not find a node in the given phase, while invoking CoExistenceChecker.canCoExistInPhase()",
					null);
			return true;
		}
		assert (n1.getInfo().getNodePhaseInfo().getPhaseSet().contains(ph)) : n1 + " does not belong to phase #"
				+ ph.getPhaseId() + ". It\'s phases are " + n1.getInfo().getNodePhaseInfo().getPhaseString()
				+ " as compared to " + n2 + " whose phases are " + n2.getInfo().getNodePhaseInfo().getPhaseString();
		assert (n2.getInfo().getNodePhaseInfo().getPhaseSet().contains(ph));

		NodePhasePair nodePhasePair = new NodePhasePair(n1, n2, ph);
		NodePair nodePair = new NodePair(n1, n2);
		if (CoExistenceChecker.knownCoExistingNodesInPhase.contains(nodePhasePair)) {
			return true;
		} else if (CoExistenceChecker.knownNonCoExistingNodes.contains(nodePair)
				|| CoExistenceChecker.knownNonCoExistingNodesInPhase.contains(nodePhasePair)) {
			return false;
		}

		if (nodePairs.contains(nodePair)) {
			/*
			 * Conservatively, we return true.
			 * NOTE: Since this is a recursive call, DO NOT assume this pair to
			 * co-exist just yet.
			 */
			return true;
		} else {
			nodePairs.add(nodePair);
		}

		PredicateFlowFact n1Paths = (PredicateFlowFact) n1.getInfo()
				.getIN(Program.useInterProceduralPredicateAnalysis ? AnalysisName.PREDICATE_ANALYSIS
						: AnalysisName.INTRA_PREDICATE_ANALYSIS);
		PredicateFlowFact n2Paths = (PredicateFlowFact) n2.getInfo()
				.getIN(Program.useInterProceduralPredicateAnalysis ? AnalysisName.PREDICATE_ANALYSIS
						: AnalysisName.INTRA_PREDICATE_ANALYSIS);

		if (n1Paths == null || n2Paths == null) {
			// TODO: Uncomment the following code, after re-inspecting the logic here.
			// System.err.println("FOUND AN INTERESTING CASE, where the following nodes do
			// not have any paths:\n"
			// + n1.getInfo().getDebugString() + "\n and \n" +
			// n2.getInfo().getDebugString());
			// DumpSnapshot.dumpPredicates("erroneous");
			// Thread.dumpStack();
			// System.exit(0);
			return true; // conservatively.
		}

		/*
		 * Consider only those paths for both n1 and n2, which correspond to the
		 * code within the phase "ph".
		 * In other words, the BPP of any selected path should either start the
		 * phase ph, or it should be present within the phase ph.
		 */
		Set<ReversePath> pathsOfN1 = new HashSet<>();
		for (ReversePath rp : n1Paths.controlPredicatePaths) {
			if (rp.bPP == null // i.e., the path starts at the entry point of a function.
					|| ph.getBeginPoints().contains(rp.bPP)
					// i.e., the BPP corresponding to the path starts the phase ph.
					|| rp.bPP.getNode().getInfo().getNodePhaseInfo().getPhaseSet().contains(ph)) { // i.e., the BPP of
																									// this path is
																									// present
				// in some internal parallel construct of the phase ph.
				pathsOfN1.add(rp);
			}
		}

		Set<ReversePath> pathsOfN2 = new HashSet<>();
		for (ReversePath rp : n2Paths.controlPredicatePaths) {
			if (rp.bPP == null // i.e., the path starts at the entry point of a function.
					|| ph.getBeginPoints().contains(rp.bPP)
					// i.e., the BPP corresponding to the path starts the phase ph.
					|| rp.bPP.getNode().getInfo().getNodePhaseInfo().getPhaseSet().contains(ph)) { // i.e., the BPP of
																									// this path is
																									// present
				// in some internal parallel construct of the phase ph.
				pathsOfN2.add(rp);
			}
		}

		if (Program.sveNoCheck) {
			// Here, we rely on the assumption that there are no matching barriers (which is
			// indeed the case for all benchmarks under study.)
			BeginPhasePoint commonBPP = null;
			for (AbstractPhasePointable bppAbs : ph.getBeginPoints()) {
				BeginPhasePoint bpp = (BeginPhasePoint) bppAbs;
				if (bpp.getReachableNodes().contains(n1) && bpp.getReachableNodes().contains(n2)) {
					commonBPP = bpp;
				}
			}
			if (commonBPP == null) {
				return false;
			}
		}

		/*
		 * Now, use the following method to check if any valid pair of paths may
		 * exist such that n1 and n2 may co-exist.
		 */
		if (CoExistenceChecker.haveAnyValidPathPairs(pathsOfN1, pathsOfN2, ph, nodePairs, expSet)) {
			knownCoExistingNodesInPhase.add(nodePhasePair);
			knownCoExistingNodes.add(nodePair);
			nodePairs.remove(nodePair);
			return true;
		} else {
			knownNonCoExistingNodesInPhase.add(nodePhasePair);
			nodePairs.remove(nodePair);
			return false;
		}
	}

	private static boolean haveAnyValidPathPairs(Set<ReversePath> pathsOfN1, Set<ReversePath> pathsOfN2,
			AbstractPhase<?, ?> ph, Set<NodePair> nodePairs, Set<Expression> expSet) {
		if (pathsOfN1 == null || pathsOfN2 == null) {
			return true;
		}
		if (pathsOfN1.isEmpty() && pathsOfN2.isEmpty()) {
			return true;
		}
		for (ReversePath path1 : pathsOfN1) {
			inner: for (ReversePath path2 : pathsOfN2) {
				/*
				 * In this loop, we attempt to return true from this method if
				 * the pair (path1, path2) can be consistent.
				 */
				if (path1.bPP != null && path2.bPP != null) {
					/*
					 * If the paths are inconsistent due to co-existence checks
					 * on their BPPs, then ignore these paths.
					 * If any of the BPPs are not available, then we can simply
					 * assume that the paths are not inconsistent due to BPPs.
					 */
					if (!CoExistenceChecker.canBarriersCoExistInPhase(path1.bPP, path2.bPP, ph, nodePairs, expSet)) {
						continue inner;
					}
				}

				/*
				 * If there exists any branch in path1 that has a contradicting
				 * branch in path2, AND if the predicate corresponding to the
				 * branch is an SVE, then we should ignore this pair of paths,
				 * as the pair cannot be consistent.
				 */
				for (BranchEdge be1 : path1.edgesOnPath) {
					for (BranchEdge be2 : path2.edgesOnPath) {
						if (be1.predicate == be2.predicate && !be1.equals(be2)) {
							// be2 is contradicting to be1, if predicate is an SVE.
							Expression exp = be1.predicate;
							boolean isSVE = false;
							if (exp.getInfo().isSVEAnnotated()) {
								isSVE = true;
							} else if (exp.getInfo().getCFGInfo().getSuccBlocks().size() <= 1) {
								/*
								 * If this expression has exactly one successor,
								 * consider it to be an SVE.
								 */
								isSVE = true;
							} else {
								/*
								 * Else, apply some simple heuristics to check
								 * the SVEness of
								 * this expression.
								 */
								isSVE = SVEChecker.isSingleValuedPredicate(exp, expSet, nodePairs);
							}
							if (isSVE) {
								/*
								 * Here, we have found a proof that (path1,
								 * path2) is an inconsistent pair. Ignore this
								 * pair.
								 */
								continue inner;
							}
						}
					}
				}
				/*
				 * If we reach this point, then it implies that (path1, path2)
				 * is a consistent/valid pair.
				 */
				// TODO: Test where this code should be at.
				// * OR is it???
				// * Well, we can try to gain more precision if either of the paths may start at
				// the BeginNode
				// * of a FunctionDefinition.
				// * Note that getting a contradiction should help us skip returning true (by
				// just letting us continue
				// * to the next pair, if any.)
				// */
				// if (CoExistenceChecker.extendedChecksOfCoExistence(path1, path2, ph,
				// nodePairs, expSet)) {
				// continue inner;
				// }
				return true;
			}
		}
		/*
		 * If we were not able to find a consistent pair, then n1 and n2 cannot
		 * co-exist.
		 */
		return false;
	}

	private static boolean extendedChecksOfCoExistence(ReversePath path1, ReversePath path2, AbstractPhase<?, ?> ph,
			Set<NodePair> nodePairs, Set<Expression> expSet) {
		return false;
	}

	private static boolean canBarriersCoExistInPhase(BeginPhasePoint bPP1, BeginPhasePoint bPP2,
			AbstractPhase<?, ?> absPh, Set<NodePair> nodePairs, Set<Expression> expSet) {
		assert (Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YUANMHP);
		if (bPP1 == null || bPP2 == null) {
			return true; // Conservatively.
		}
		if (bPP1.getNode() == bPP2.getNode()) {
			return true;
		}
		if (Program.sveNoCheck && (bPP1.getNode() instanceof BarrierDirective
				|| (bPP1.getNode() instanceof BeginNode && bPP1.getNode().getParent() instanceof ParallelConstruct))
				&& (bPP2.getNode() instanceof BarrierDirective || (bPP2.getNode() instanceof BeginNode
						&& bPP2.getNode().getParent() instanceof ParallelConstruct))) {
			return false;
		}
		boolean oneIsEntry = absPh.getBeginPoints().stream().anyMatch(b -> b.getNodeFromInterface() == bPP1.getNode());// OLD
																														// Code:
																														// bPP1.getPhaseSet().contains(absPh);
		boolean twoIsEntry = absPh.getBeginPoints().stream().anyMatch(b -> b.getNodeFromInterface() == bPP2.getNode());// OLD
																														// Code:
																														// bPP2.getPhaseSet().contains(absPh);
		if (oneIsEntry && twoIsEntry) {
			if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
				Thread.dumpStack();
				assert (false) : "Unexpected path.";
				return true;
			}
			Phase ph = (Phase) absPh;
			for (AbstractPhase<?, ?> predPhaseAbs : ph.getPredPhases()) {
				Phase predPhase = (Phase) predPhaseAbs;
				Set<Node> nodeSet = predPhase.getNodeSet();
				if (nodeSet.contains(bPP1.getNode()) && nodeSet.contains(bPP2.getNode())) {
					if (CoExistenceChecker.canCoExistInPhase(bPP1.getNode(), bPP2.getNode(), predPhase, nodePairs,
							expSet)) {
						return true;
					}
				}
			}
			return false; // There does not exist any pred-phase in which barriers may co-exist.
		} else if (oneIsEntry) {
			// Check if a successor of the BPP1 can co-exist with BPP2 in ph.
			return CoExistenceChecker.canCoExistInPhase(bPP2.getNode(),
					bPP1.getNode().getInfo().getCFGInfo().getLeafSuccessors().get(0), absPh, nodePairs, expSet);
		} else if (twoIsEntry) {
			// Check if a successor of the BPP2 can co-exist with BPP3 in ph.
			return CoExistenceChecker.canCoExistInPhase(bPP1.getNode(),
					bPP2.getNode().getInfo().getCFGInfo().getLeafSuccessors().get(0), absPh, nodePairs, expSet);
		} else {
			Set<Node> phNodeSet = absPh.getNodeSet();
			if (phNodeSet.contains(bPP1.getNode()) && phNodeSet.contains(bPP2.getNode())) {
				// Here, both BPP1 and BPP2 belong to some internal parallel construct within
				// ph.
				return CoExistenceChecker.canCoExistInPhase(bPP1.getNode(), bPP2.getNode(), absPh, nodePairs, expSet);
			} else {
				return true; // Conservatively.
			}
		}

	}

	/**
	 * Checks whether {@code variable} has been written anywhere in the phases in
	 * which {@code node} may execute.
	 *
	 * @param node
	 *                  a CFG node.
	 * @param variable
	 *                  a Symbol.
	 * @param expSet
	 *                  nodes for which SVE check is under process (on call-stack).
	 * @param nodePairs
	 *                  nodes for which co-existence check is under process (on
	 *                  call-stack).
	 *
	 * @return true if there may exist a write to
	 */
	public static boolean isWrittenInPhase(Node node, Symbol variable, Set<Expression> expSet,
			Set<NodePair> nodePairs) {
		node = Misc.getCFGNodeFor(node);
		for (AbstractPhase<?, ?> ph : node.getInfo().getNodePhaseInfo().getPhaseSet()) {
			for (Node other : ph.getNodeSet()) {
				// Here, we will not skip the case other == node.
				if (other.getInfo().getWrites().contains(variable)) {
					if (CoExistenceChecker.canCoExistInPhase(node, other, ph, nodePairs, expSet)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Tests whether the node {@code rd} has all its control-predicates in all its
	 * phases as SVEs or not.
	 * <br>
	 * When this method returns true, it implies that {@code rd} will either be
	 * executed by all the threads or by none
	 * of them.
	 *
	 * @param rd
	 *                  node that has to be tested for existence-for-all.
	 * @param expSet
	 *                  nodes for which SVE check is under process (on call-stack).
	 * @param nodePairs
	 *                  nodes for which co-existence check is under process (on
	 *                  call-stack).
	 *
	 * @return true if either all threads will execute {@code rd} in its any given
	 *         phase, or none.
	 */
	static boolean existsForAll(Node rd, Set<Expression> expSet, Set<NodePair> nodePairs) {
		if (CoExistenceChecker.existForAll.contains(rd)) {
			return true;
		} else if (CoExistenceChecker.doesNotExistForAll.contains(rd)) {
			return false;
		}
		final Node cfgNode = Misc.getCFGNodeFor(rd);
		for (AbstractPhase<?, ?> ph : cfgNode.getInfo().getNodePhaseInfo().getPhaseSet()) {
			/*
			 * If there exists any BPP of ph from which cfgNode is unreachable,
			 * but whose successor may co-exist with the cfgNode, then return
			 * false.
			 */
			if (ph.getBeginPoints().stream().filter(bpp -> !bpp.getReachableNodes().contains(cfgNode))
					.anyMatch(bpp -> canCoExistInPhase(
							bpp.getNodeFromInterface().getInfo().getCFGInfo().getLeafSuccessors().get(0), cfgNode, ph,
							nodePairs, expSet))) {

				doesNotExistForAll.add(rd);
				return false;
			}

			/*
			 * Ensure that all non-SCOPPed predicates of cfgNode are SVEs.
			 */
			Set<Node> predicatesToBeChecked = new HashSet<>();
			NodeWithStack nWS = new NodeWithStack(cfgNode, new CallStack());
			CollectorVisitor.collectNodeSetInGenericGraph(nWS, new HashSet<>(),
					n -> (n.getNode() instanceof BarrierDirective || (n.getNode() instanceof BeginNode
							&& ((BeginNode) n.getNode()).getParent() instanceof ParallelConstruct)),
					n -> {
						Set<NodeWithStack> preds = n.getNode().getInfo().getCFGInfo()
								.getParallelConstructFreeInterProceduralLeafPredecessors(n.getCallStack());
						/*
						 * Collect successors of non-SCOPPed predicates.
						 */
						for (NodeWithStack pS : preds) {
							Node predecessor = pS.getNode();
							if (Misc.isAPredicate(predecessor) && !predecessor.getInfo().isSCOPPed(cfgNode)) {
								predicatesToBeChecked.add(predecessor);
							}
						}
						return preds;
					});

			for (Node expNode : predicatesToBeChecked) {
				if (expNode instanceof OmpForCondition) {
					doesNotExistForAll.add(rd);
					return false;
				}
				Expression exp = (Expression) expNode;
				boolean isSVE = false;
				if (exp.getInfo().isSVEAnnotated()) {
					isSVE = true;
				} else if (exp.getInfo().getCFGInfo().getSuccBlocks().size() <= 1) {
					/*
					 * If this expression has exactly one successor, mark it to
					 * be an SVE.
					 */
					isSVE = true;
				} else {
					isSVE = SVEChecker.isSingleValuedPredicate(exp, expSet, nodePairs);
				}

				if (!isSVE) {
					doesNotExistForAll.add(rd);
					return false;
				}
			}
		}
		existForAll.add(rd);
		return true;
	}

	/**
	 * Assuming that nodes {@code n1} and {@code n2} may happen in parallel, this
	 * method conservatively checks if it is
	 * possible for different threads to encounter n1 and n2, using the SVE
	 * (single-valued expression) analysis on the
	 * common control predicates of both the nodes.
	 *
	 * @param n1
	 *                  a CFG node.
	 * @param n2
	 *                  a CFG node.
	 * @param ph
	 *                  an abstract phase.
	 * @param nodePairs
	 *                  pair of nodes for which the query is being asked
	 *                  recursively.
	 *
	 * @return true, if both the nodes may be executed by different threads together
	 *         in any runtime instance of phase
	 *         {@code ph}.
	 */
	@Deprecated
	static boolean deprecated_canCoExistInPhase(Node n1, Node n2, Phase ph, Set<NodePair> nodePairs,
			Set<Expression> expSet) {
		assert (Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YUANMHP);
		if (Program.sveSensitive == SVEDimension.SVE_INSENSITIVE) {
			return true;
		}
		NodePair pair = new NodePair(n1, n2);
		if (nodePairs.contains(pair)) {
			/*
			 * Conservatively, we return true.
			 * NOTE: Since this is a recursive call, DO NOT assume this pair to
			 * co-exist just yet.
			 * TODO: Check false.
			 */
			return false;
		} else {
			nodePairs.add(pair);
		}

		n1 = Misc.getCFGNodeFor(n1);
		n2 = Misc.getCFGNodeFor(n2);
		/*
		 * If n1 and n2 belong to the same basic block, then return true.
		 */
		final Node n1F = n1;
		final Node n2F = n2;
		final NodeWithStack n1SF = new NodeWithStack(n1, new CallStack());
		final NodeWithStack n2SF = new NodeWithStack(n2, new CallStack());
		final Set<Node> bothNodes = new HashSet<>();
		bothNodes.add(n1);
		bothNodes.add(n2);
		Set<NodeWithStack> endPointN1 = new HashSet<>();
		CollectorVisitor.collectNodeSetInGenericGraph(n1SF, endPointN1,
				n -> (n.getNode() instanceof BarrierDirective
						|| (n.getNode() instanceof BeginNode
								&& ((BeginNode) n.getNode()).getParent() instanceof ParallelConstruct)
						|| (Misc.isAPredicate(n.getNode()) && !n.getNode().getInfo().isSCOPPed(bothNodes))
						|| n.getNode() == n2SF.getNode()),
				n -> n.getNode().getInfo().getCFGInfo()
						.getParallelConstructFreeInterProceduralLeafPredecessors(n.getCallStack()));
		if (endPointN1.stream().anyMatch(nWS -> nWS.getNode() == n2F)) {
			nodePairs.remove(pair);
			return true;
		}

		Set<NodeWithStack> endPointN2 = new HashSet<>();
		CollectorVisitor.collectNodeSetInGenericGraph(n2SF, endPointN2,
				n -> (n.getNode() instanceof BarrierDirective
						|| (n.getNode() instanceof BeginNode
								&& ((BeginNode) n.getNode()).getParent() instanceof ParallelConstruct)
						|| (Misc.isAPredicate(n.getNode()) && !n.getNode().getInfo().isSCOPPed(bothNodes))
						|| n.getNode() == n1SF.getNode()),
				n -> n.getNode().getInfo().getCFGInfo()
						.getParallelConstructFreeInterProceduralLeafPredecessors(n.getCallStack()));
		if (endPointN2.stream().anyMatch(nWS -> nWS.getNode() == n1F)) {
			nodePairs.remove(pair);
			return true;
		}

		/*
		 * Obtain BPP's in ph, from which n1 and n2 are reachable.
		 */
		Set<BeginPhasePoint> pBPPSet = ph.getBeginPoints();
		Set<BeginPhasePoint> n1BPP = new HashSet<>();
		Set<BeginPhasePoint> n2BPP = new HashSet<>();
		for (BeginPhasePoint pBPP : pBPPSet) {
			if (pBPP.getNode() == n1 || pBPP.getReachableNodes().contains(n1)) {
				n1BPP.add(pBPP);
			}
			if (pBPP.getNode() == n2 || pBPP.getReachableNodes().contains(n2)) {
				n2BPP.add(pBPP);
			}
		}

		/*
		 * If there exists any BPP pair from the set of pairs ((BPPN1-BPPN2),
		 * (BPPN2-BPPN1)), that may co-exist in any previous phase, then return
		 * true.
		 */
		for (BeginPhasePoint n1Mn2BPP : Misc.setMinus(n1BPP, n2BPP)) {
			Node prevB1 = n1Mn2BPP.getNode();
			for (BeginPhasePoint n2Mn1BPP : Misc.setMinus(n2BPP, n1BPP)) {
				Node prevB2 = n2Mn1BPP.getNode();
				for (AbstractPhase<?, ?> prevPhaseAbs : ph.getPredPhases()) {
					Phase prevPhase = (Phase) prevPhaseAbs;
					if (prevPhase.getNodeSet().contains(prevB1) && prevPhase.getNodeSet().contains(prevB2)) {
						if (CoExistenceChecker.deprecated_canCoExistInPhase(prevB1, prevB2, prevPhase, nodePairs,
								expSet)) {
							nodePairs.remove(pair);
							return true;
						}
					}
				}
			}
		}

		/*
		 * Otherwise, if the intersection of BPPN1 and BPPN2 is empty, then
		 * return false.
		 */
		if (!Misc.doIntersect(n1BPP, n2BPP)) {
			nodePairs.remove(pair);
			return false; // The nodes can NOT co-exist in "ph".
		}

		/*
		 * If there exists any pair of barriers from ((BPPN1-BPPN2), (BPPN1
		 * \intersection BPPN2)) OR ((BPPN2-BPPN1), (BPPN1 \intersection
		 * BPPN2)), such that the pair may co-exist in any previous phase, then
		 * return true.
		 */
		Set<BeginPhasePoint> intersection = Misc.setIntersection(n1BPP, n2BPP);
		for (BeginPhasePoint n1Mn2BPP : Misc.setMinus(n1BPP, n2BPP)) {
			Node prevB1 = n1Mn2BPP.getNode();
			for (BeginPhasePoint interBPP : intersection) {
				Node prevB2 = interBPP.getNode();
				for (AbstractPhase<?, ?> prevPhaseAbs : ph.getPredPhases()) {
					Phase prevPhase = (Phase) prevPhaseAbs;
					if (prevPhase.getNodeSet().contains(prevB1) && prevPhase.getNodeSet().contains(prevB2)) {
						if (CoExistenceChecker.deprecated_canCoExistInPhase(prevB1, prevB2, prevPhase, nodePairs,
								expSet)) {
							nodePairs.remove(pair);
							return true;
						}
					}
				}
			}
		}

		for (BeginPhasePoint n2Mn1BPP : Misc.setMinus(n2BPP, n1BPP)) {
			Node prevB1 = n2Mn1BPP.getNode();
			for (BeginPhasePoint interBPP : intersection) {
				Node prevB2 = interBPP.getNode();
				for (AbstractPhase<?, ?> prevPhaseAbs : ph.getPredPhases()) {
					Phase prevPhase = (Phase) prevPhaseAbs;
					if (prevPhase.getNodeSet().contains(prevB1) && prevPhase.getNodeSet().contains(prevB2)) {
						if (CoExistenceChecker.deprecated_canCoExistInPhase(prevB1, prevB2, prevPhase, nodePairs,
								expSet)) {
							nodePairs.remove(pair);
							return true;
						}
					}
				}
			}
		}

		/*
		 * Obtain all predicates and predicate-successors that are reachable
		 * while moving backwards from N1 in the current phase.
		 */
		Set<Node> predicateSuccessorsForN1 = new HashSet<>();
		Set<Node> predicatesOfN1 = new HashSet<>();
		CollectorVisitor.collectNodeSetInGenericGraph(n1SF, new HashSet<>(),
				n -> (n.getNode() instanceof BarrierDirective || (n.getNode() instanceof BeginNode
						&& ((BeginNode) n.getNode()).getParent() instanceof ParallelConstruct)),
				n -> {
					Set<NodeWithStack> preds = n.getNode().getInfo().getCFGInfo()
							.getParallelConstructFreeInterProceduralLeafPredecessors(n.getCallStack());
					/*
					 * Collect successors of non-SCOPPed predicates.
					 */
					for (NodeWithStack pS : preds) {
						Node predecessor = pS.getNode();
						if (Misc.isAPredicate(predecessor) && !predecessor.getInfo().isSCOPPed(bothNodes)) {
							predicatesOfN1.add(predecessor);
							predicateSuccessorsForN1.add(n.getNode());
						}
					}

					return preds;
				});

		/*
		 * Obtain all predicates and predicate-successors that are reachable
		 * while moving backwards from N2 in the current phase.
		 */
		Set<Node> predicateSuccessorsForN2 = new HashSet<>();
		Set<Node> predicatesOfN2 = new HashSet<>();
		CollectorVisitor.collectNodeSetInGenericGraph(n2SF, new HashSet<>(),
				n -> (n.getNode() instanceof BarrierDirective || (n.getNode() instanceof BeginNode
						&& ((BeginNode) n.getNode()).getParent() instanceof ParallelConstruct)),
				n -> {
					Set<NodeWithStack> preds = n.getNode().getInfo().getCFGInfo()
							.getParallelConstructFreeInterProceduralLeafPredecessors(n.getCallStack());
					/*
					 * Collect successors of non-SCOPPed predicates.
					 */
					for (NodeWithStack pS : preds) {
						Node predecessor = pS.getNode();
						if (Misc.isAPredicate(predecessor) && !predecessor.getInfo().isSCOPPed(bothNodes)) {
							predicatesOfN2.add(predecessor);
							predicateSuccessorsForN2.add(n.getNode());
						}
					}

					return preds;
				});

		/*
		 * If for each element in the common set of predicates, all immediate
		 * successors of the predicate can reach both N1 and N2, then return
		 * true (since none of these predicates can split the phase in two or
		 * more parts).
		 */
		Set<Node> commonSet = Misc.setIntersection(predicatesOfN1, predicatesOfN2);
		boolean foundBreaker = false;
		predLoop: for (Node predicate : commonSet) {
			for (Node predSuccessor : predicate.getInfo().getCFGInfo().getInterProceduralLeafSuccessors()) {
				if (predicateSuccessorsForN1.contains(predSuccessor)
						&& !predicateSuccessorsForN2.contains(predSuccessor)) {
					foundBreaker = true;
					break predLoop;
				}
				if (predicateSuccessorsForN2.contains(predSuccessor)
						&& !predicateSuccessorsForN1.contains(predSuccessor)) {
					foundBreaker = true;
					break predLoop;
				}
			}
		}
		if (!foundBreaker) {
			nodePairs.remove(pair);
			return true;
		}

		// Set<NodeWithStack> finalSet = commonSet.stream().map(c -> new
		// NodeWithStack(c, new CallStack()))
		// .collect(Collectors.toCollection(HashSet::new));
		Set<NodeWithStack> finalSet = new HashSet<>();
		Set<NodeWithStack> frontierSet = new HashSet<>();
		frontierSet.add(n1SF);
		frontierSet.add(n2SF);
		@SuppressWarnings("unused")
		int i = 0;
		while (!commonSet.isEmpty()) {
			i++;
			Set<NodeWithStack> nextFrontier = new HashSet<>();
			CollectorVisitor.collectNodeSetInGenericGraph(frontierSet, nextFrontier,
					n -> commonSet.contains(n.getNode()) || finalSet.stream().anyMatch(f -> f.getNode() == n.getNode()),
					n -> {
						Set<NodeWithStack> retSet = n.getNode().getInfo().getCFGInfo()
								.getParallelConstructFreeInterProceduralLeafPredecessors(n.getCallStack());
						retSet.removeAll(retSet.stream().filter(
								p -> (p.getNode() instanceof BarrierDirective) || (p.getNode() instanceof BeginNode
										&& (((BeginNode) p.getNode()).getParent() instanceof ParallelConstruct)))
								.collect(Collectors.toSet()));
						return retSet;
					});
			nextFrontier.removeAll(finalSet);
			finalSet.addAll(nextFrontier);
			if (nextFrontier.size() == 1) {
				break;
			} else {
				for (NodeWithStack nS : nextFrontier) {
					commonSet.remove(nS.getNode());
				}
				frontierSet = nextFrontier;
			}
		}
		// System.out.println("After " + i + " level(s) of backward BFS, remaining
		// predicate count is " + finalSet.size()
		// + " out of " + commonSize + ".");

		/*
		 * If "intersection" is empty, then conservatively, we should return
		 * true.
		 * Otherwise, if any expression in "intersection" is a multi-valued
		 * expression, then return true.
		 * Else, return false;
		 */
		if (finalSet.isEmpty()) {
			nodePairs.remove(pair);
			return true;
		}
		for (NodeWithStack expNodeWS : finalSet) {
			Node expNode = expNodeWS.getNode();
			if (expNode instanceof OmpForCondition) {
				nodePairs.remove(pair);
				return true;
			}
			Expression exp = (Expression) expNode;
			boolean isSVE = false;
			if (exp.getInfo().isSVEAnnotated()) {
				isSVE = true;
			} else if (exp.getInfo().getCFGInfo().getSuccBlocks().size() <= 1) {
				/*
				 * If this expression has exactly one successor, mark it to be
				 * an SVE.
				 */
				isSVE = true;
			} else {
				/*
				 * Else, apply some simple heuristics to check the SVEness of
				 * this expression.
				 */
				isSVE = SVEChecker.isSingleValuedPredicate(exp, expSet, nodePairs);
			}

			if (!isSVE) {
				nodePairs.remove(pair);
				return true;
			}
		}
		nodePairs.remove(pair);
		return false;
	}

}
