/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis;

import imop.Main;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.BranchEdge;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.controlflow.PredicateAnalysis;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ContextSensitiveCoExistenceChecker {
	public static Set<NodePair> knownCoExistingNodes = new HashSet<>();
	public static Set<NodePair> knownNonCoExistingNodes = new HashSet<>();
	static Set<NodePhasePair> knownCoExistingNodesInPhase = new HashSet<>();
	static Set<NodePhasePair> knownNonCoExistingNodesInPhase = new HashSet<>();
	static Set<Node> existForAll = new HashSet<>();
	static Set<Node> doesNotExistForAll = new HashSet<>();

	public static int queryCounter = 0;

	public static void resetStaticFields() {
		ContextSensitiveCoExistenceChecker.knownCoExistingNodesInPhase.clear();
		ContextSensitiveCoExistenceChecker.knownNonCoExistingNodesInPhase.clear();
		ContextSensitiveCoExistenceChecker.knownCoExistingNodes.clear();
		ContextSensitiveCoExistenceChecker.knownNonCoExistingNodes.clear();
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
		ContextSensitiveCoExistenceChecker.knownCoExistingNodesInPhase.clear();
		ContextSensitiveCoExistenceChecker.knownNonCoExistingNodesInPhase.clear();
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
		assert Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YCON : "Unexpected path";
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
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YCON) {
			commonPhases = Misc.setIntersection((Set<YPhase>) n1Phases, (Set<YPhase>) n2Phases);
		} else {
			commonPhases = Misc.setIntersection((Set<Phase>) n1Phases, (Set<Phase>) n2Phases);
		}
		for (AbstractPhase<?, ?> ph : commonPhases) {
			if (ContextSensitiveCoExistenceChecker.canCoExistInPhase(n1, n2, ph, new CallStack(), new CallStack())) {
				knownCoExistingNodes.add(nP);
				return true;
			}
		}
		knownNonCoExistingNodes.add(nP);
		return false;
	}

	public static boolean canCoExistInPhase(Node n1, Node n2, AbstractPhase<?, ?> ph, CallStack c1, CallStack c2) {
		assert Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YCON : "Unexpected path";
		if (Program.sveSensitive == SVEDimension.SVE_INSENSITIVE) {
			return true;
		}
		if (n1 == n2) {
			return true;
		}
		// TODO: Check the following heuristic.
		if (Program.sveNoCheck && n1 instanceof BarrierDirective && n2 instanceof BarrierDirective) {
			return false;
		}
		long sveTimerThis = System.nanoTime();
		queryCounter++;
		assert (!Main.timerOn) : "Conflicting timers";
		Main.timerOn = true;
		boolean retVal = ContextSensitiveCoExistenceChecker.canCoExistInPhase(n1, n2, ph, new HashSet<>(),
				new HashSet<>(), c1, c2);
		Main.timerOn = false;
		sveTimerThis = System.nanoTime() - sveTimerThis;
		SVEChecker.sveQueryTimer += sveTimerThis;
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
			Set<Expression> expSet, CallStack c1, CallStack c2) {
		if (Program.sveSensitive == SVEDimension.SVE_INSENSITIVE) {
			return true;
		}
		if (Program.sveNoCheck && n1 instanceof BarrierDirective && n2 instanceof BarrierDirective) {
			return false;
		}
		// TODO: This is newly added if statement to take care of queries during phase
		// updates.
		// if (!n1.getInfo().getNodePhaseInfo().getPhaseSet().contains(ph)) {
		// Misc.warnDueToLackOfFeature(
		// "Could not find a node in the given phase, while invoking
		// ContextSensitiveCoExistenceChecker.canCoExistInPhase()",
		// null);
		// return true;
		// } else if (!n2.getInfo().getNodePhaseInfo().getPhaseSet().contains(ph)) {
		// Misc.warnDueToLackOfFeature(
		// "Could not find a node in the given phase, while invoking
		// ContextSensitiveCoExistenceChecker.canCoExistInPhase()",
		// null);
		// return true;
		// }
		// assert (n1.getInfo().getNodePhaseInfo().getPhaseSet().contains(ph)) : n1 + "
		// does not belong to phase #"
		// + ph.getPhaseId() + ". It\'s phases are " +
		// n1.getInfo().getNodePhaseInfo().getPhaseString()
		// + " as compared to " + n2 + " whose phases are " +
		// n2.getInfo().getNodePhaseInfo().getPhaseString();
		// assert (n2.getInfo().getNodePhaseInfo().getPhaseSet().contains(ph));

		NodePhasePair nodePhasePair = new NodePhasePair(n1, n2, ph);
		NodePair nodePair = new NodePair(n1, n2);
		if (ContextSensitiveCoExistenceChecker.knownCoExistingNodesInPhase.contains(nodePhasePair)) {
			return true;
		} else if (ContextSensitiveCoExistenceChecker.knownNonCoExistingNodes.contains(nodePair)
				|| ContextSensitiveCoExistenceChecker.knownNonCoExistingNodesInPhase.contains(nodePhasePair)) {
			return false;
		}

		if (nodePairs.contains(nodePair)) {
			return false;
			/*
			 * Conservatively, we return true.
			 * NOTE: Since this is a recursive call, DO NOT assume this pair to
			 * co-exist just yet.
			 */
			// TODO: tests/btsmall won't give precise answer if we return true, as compared
			// to YCON.
			// return true;
		} else {
			nodePairs.add(nodePair);
		}

		PredicateAnalysis.PredicateFlowFact n1Paths = (PredicateAnalysis.PredicateFlowFact) n1.getInfo()
				.getIN(Program.useInterProceduralPredicateAnalysis ? AnalysisName.CROSSCALL_PREDICATE_ANALYSIS
						: AnalysisName.PSEUDO_INTER_PREDICATE_ANALYSIS);
		PredicateAnalysis.PredicateFlowFact n2Paths = (PredicateAnalysis.PredicateFlowFact) n2.getInfo()
				.getIN(Program.useInterProceduralPredicateAnalysis ? AnalysisName.CROSSCALL_PREDICATE_ANALYSIS
						: AnalysisName.PSEUDO_INTER_PREDICATE_ANALYSIS);

		if (n1Paths == null || n2Paths == null) {
			// TODO: Uncomment the following code, after re-inspecting the logic here.
			// System.err.println("FOUND AN INTERESTING CASE, where the following nodes do
			// not have any paths:\n"
			// + n1.getInfo().getDebugString() + "\n and \n" +
			// n2.getInfo().getDebugString());
			// DumpSnapshot.dumpPredicates("erroneous");
			// Thread.dumpStack();
			// System.exit(0);
			knownCoExistingNodesInPhase.add(nodePhasePair);
			knownCoExistingNodes.add(nodePair);
			nodePairs.remove(nodePair);
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
			if (rp.startPoint == null // i.e., the path starts at the entry point of a function.
					|| ph.getBeginPoints().stream().anyMatch(bpp -> bpp.getNodeFromInterface() == rp.startPoint)
					// i.e., the BPP corresponding to the path starts the phase ph.
					|| rp.startPoint.getNode().getInfo().getNodePhaseInfo().getPhaseSet().contains(ph)) { // i.e., the
																											// BPP of
				// this path is
				// present
				// in some internal parallel construct of the phase ph.
				pathsOfN1.add(rp);
			}
		}

		Set<ReversePath> pathsOfN2 = new HashSet<>();
		for (ReversePath rp : n2Paths.controlPredicatePaths) {
			if (rp.startPoint == null // i.e., the path starts at the entry point of a function.
					|| ph.getBeginPoints().stream().anyMatch(bpp -> bpp.getNodeFromInterface() == rp.startPoint)
					// i.e., the BPP corresponding to the path starts the phase ph.
					|| rp.startPoint.getNode().getInfo().getNodePhaseInfo().getPhaseSet().contains(ph)) { // i.e., the
																											// BPP of
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
				knownNonCoExistingNodesInPhase.add(nodePhasePair);
				nodePairs.remove(nodePair);
				return false;
			}
		}

		/*
		 * Now, use the following method to check if any valid pair of paths may
		 * exist such that n1 and n2 may co-exist.
		 */
		if (ContextSensitiveCoExistenceChecker.haveAnyValidPathPairs(n1, n2, pathsOfN1, pathsOfN2, ph, nodePairs,
				expSet, c1, c2)) {
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

	private static boolean haveAnyValidPathPairs(Node n1, Node n2, Set<ReversePath> pathsOfN1,
			Set<ReversePath> pathsOfN2, AbstractPhase<?, ?> ph, Set<NodePair> nodePairs, Set<Expression> expSet,
			CallStack c1, CallStack c2) {
		if (pathsOfN1 == null || pathsOfN2 == null) {
			return true;
		}
		if (pathsOfN1.isEmpty() && pathsOfN2.isEmpty()) {
			return true;
		}
		for (ReversePath path1 : pathsOfN1) {
			for (ReversePath path2 : pathsOfN2) {
				/*
				 * In this loop, we attempt to return true from this method if
				 * the pair (path1, path2) can be consistent.
				 */
				if (ContextSensitiveCoExistenceChecker.isPathPairValid(n1, n2, path1, path2, ph, nodePairs, expSet, c1,
						c2)) {
					return true;
				}
			}
		}
		/*
		 * If we were not able to find a consistent pair, then n1 and n2 cannot
		 * co-exist.
		 */
		return false;
	}

	private static boolean isPathPairValid(Node n1, Node n2, ReversePath path1, ReversePath path2,
			AbstractPhase<?, ?> ph, Set<NodePair> nodePairs, Set<Expression> expSet, CallStack c1, CallStack c2) {
		boolean isP1Barrier = (path1.startPoint instanceof BarrierDirective || (path1.startPoint instanceof BeginNode
				&& ((Node) path1.startPoint).getParent() instanceof ParallelConstruct));
		boolean isP2Barrier = (path2.startPoint instanceof BarrierDirective || (path2.startPoint instanceof BeginNode
				&& ((Node) path2.startPoint).getParent() instanceof ParallelConstruct));

		boolean isP1FuncBegin = path1.startPoint instanceof BeginNode
				&& ((BeginNode) path1.startPoint).getParent() instanceof FunctionDefinition;
		boolean isP2FuncBegin = path2.startPoint instanceof BeginNode
				&& ((BeginNode) path2.startPoint).getParent() instanceof FunctionDefinition;

		boolean isP1Call = path1.startPoint instanceof PostCallNode;
		boolean isP2Call = path2.startPoint instanceof PostCallNode;

		/*
		 * If the paths are inconsistent due to co-existence checks
		 * on their start points, then ignore these paths by returning false. -->
		 */
		if (path1.startPoint != null && path2.startPoint != null && path1.startPoint != path2.startPoint) {
			/*
			 * <-- If any of the start points are not available, then we simply
			 * assume that the paths are not inconsistent due to them.
			 */
			if (isP1Barrier && isP2Barrier) {
				if (path1.startPoint != path2.startPoint) {
					if (!ContextSensitiveCoExistenceChecker.canBarriersCoExistInPhase((Node) path1.startPoint,
							(Node) path2.startPoint, ph, nodePairs, expSet, c1, c2)) {
						return false;
					}
				}
			} else if (isP1Barrier || isP2Barrier) {
				Node base;
				Set<NodeWithStack> options;
				if (isP1Barrier) {
					base = n1;
					if (isP2Call) {
						CallStatement p2Call = ((PostCallNode) path2.startPoint).getParent();
						List<FunctionDefinition> calledDefs = p2Call.getInfo().getCalledDefinitions();
						if (calledDefs.isEmpty()) {
							options = new HashSet<>(1);
							options.add(new NodeWithStack(p2Call.getPreCallNode(), c2));
						} else {
							options = new HashSet<>(calledDefs.size());
							for (FunctionDefinition func : calledDefs) {
								CallStack newStack = CallStack.pushUnique(c2, p2Call);
								options.add(new NodeWithStack(func.getInfo().getCFGInfo().getNestedCFG().getEnd(),
										newStack));
							}
						}
					} else if (isP2FuncBegin) {
						FunctionDefinition func = (FunctionDefinition) ((BeginNode) path2.startPoint).getParent();
						List<CallStatement> callSites = new ArrayList<>();
						if (c2.isContextSensitiveStack()) {
							CallStatement caller = c2.peek();
							callSites.add(caller);
						} else {
							callSites.addAll(func.getInfo().getCallersOfThis());
						}
						options = new HashSet<>(callSites.size());
						for (CallStatement callSt : callSites) {
							CallStack newCallStack = CallStack.pop(c2);
							options.add(new NodeWithStack(callSt.getPreCallNode(), newCallStack));
						}
					} else {
						options = new HashSet<>();
					}
					if (!options.stream()
							.anyMatch(other -> ph.getNodeSet().contains(other)
									&& ContextSensitiveCoExistenceChecker.canCoExistInPhase(base, other.getNode(), ph,
											nodePairs, expSet, c1, other.getCallStack()))) {
						return false;
					}
				} else {
					base = n2;
					if (isP1Call) {
						CallStatement p1Call = ((PostCallNode) path1.startPoint).getParent();
						List<FunctionDefinition> calledDefs = p1Call.getInfo().getCalledDefinitions();
						if (calledDefs.isEmpty()) {
							options = new HashSet<>(1);
							options.add(new NodeWithStack(p1Call.getPreCallNode(), c1));
						} else {
							options = new HashSet<>(calledDefs.size());
							for (FunctionDefinition func : calledDefs) {
								CallStack newStack = CallStack.pushUnique(c1, p1Call);
								options.add(new NodeWithStack(func.getInfo().getCFGInfo().getNestedCFG().getEnd(),
										newStack));
							}
						}
					} else if (isP1FuncBegin) {
						FunctionDefinition func = (FunctionDefinition) ((BeginNode) path1.startPoint).getParent();
						List<CallStatement> callSites = new ArrayList<>();
						if (c1.isContextSensitiveStack()) {
							CallStatement caller = c1.peek();
							callSites.add(caller);
						} else {
							callSites.addAll(func.getInfo().getCallersOfThis());
						}
						options = new HashSet<>(callSites.size());
						for (CallStatement callSt : callSites) {
							CallStack newCallStack = CallStack.pop(c1);
							options.add(new NodeWithStack(callSt.getPreCallNode(), newCallStack));
						}
					} else {
						options = new HashSet<>();
					}
					if (!options.stream()
							.anyMatch(other -> ph.getNodeSet().contains(other)
									&& ContextSensitiveCoExistenceChecker.canCoExistInPhase(other.getNode(), base, ph,
											nodePairs, expSet, other.getCallStack(), c2))) {
						return false;
					}
				}
			} else {
				Node base = n1;
				Set<NodeWithStack> options = new HashSet<>();
				if (isP2Call) {
					CallStatement p2Call = ((PostCallNode) path2.startPoint).getParent();
					List<FunctionDefinition> calledDefs = p2Call.getInfo().getCalledDefinitions();
					if (calledDefs.isEmpty()) {
						options = new HashSet<>(1);
						options.add(new NodeWithStack(p2Call.getPreCallNode(), c2));
					} else {
						options = new HashSet<>(calledDefs.size());
						for (FunctionDefinition func : calledDefs) {
							CallStack newStack = CallStack.pushUnique(c2, p2Call);
							options.add(
									new NodeWithStack(func.getInfo().getCFGInfo().getNestedCFG().getEnd(), newStack));
						}
					}
				} else if (isP2FuncBegin) {
					FunctionDefinition func = (FunctionDefinition) ((BeginNode) path2.startPoint).getParent();
					List<CallStatement> callSites = new ArrayList<>();
					if (c2.isContextSensitiveStack()) {
						CallStatement caller = c2.peek();
						callSites.add(caller);
					} else {
						callSites.addAll(func.getInfo().getCallersOfThis());
					}
					options = new HashSet<>(callSites.size());
					for (CallStatement callSt : callSites) {
						CallStack newCallStack = CallStack.pop(c2);
						options.add(new NodeWithStack(callSt.getPreCallNode(), newCallStack));
					}
				} else {
					options = new HashSet<>();
				}
				if (!options.stream()
						.anyMatch(other -> ph.getNodeSet().contains(other)
								&& ContextSensitiveCoExistenceChecker.canCoExistInPhase(base, other.getNode(), ph,
										nodePairs, expSet, c1, other.getCallStack()))) {
					return false;
				}
			}
		} /* End of code to check the feasibility of start points. */

		/*
		 * If there exists any branch in path1 that has a contradicting
		 * branch in path2, AND if the predicate corresponding to the
		 * branch is an SVE, then we should ignore this pair of paths,
		 * as the pair cannot be consistent.
		 */
		for (BranchEdge be1 : path1.edgesOnPath) {
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
				 * the SVEness of this expression.
				 */
				isSVE = SVEChecker.isSingleValuedPredicate(exp, expSet, nodePairs);
			}
			if (!isSVE) {
				continue;
			}
			for (BranchEdge be2 : path2.edgesOnPath) {
				if (be1.predicate == be2.predicate && !be1.equals(be2)) {
					// be2 is contradicting to be1, as the predicate is an SVE.
					return false;
				}
			}
		}
		/*
		 * If we reach this point, then it implies that (path1, path2)
		 * is a consistent/valid pair.
		 */
		// /*
		// * Well, we can try to gain more precision if either of the paths may start at
		// * the BeginNode
		// * of a FunctionDefinition.
		// * Note that getting a contradiction should help us skip returning true (by
		// * just letting us continue
		// * to the next pair, if any.)
		// */
		return true;
	}

	private static boolean canBarriersCoExistInPhase(Node b1, Node b2, AbstractPhase<?, ?> absPh,
			Set<NodePair> nodePairs, Set<Expression> expSet, CallStack c1, CallStack c2) {
		assert (Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YCON);
		if (b1 == null || b2 == null) {
			return true; // Conservatively.
		}
		if (b1 == b2) {
			return true;
		}
		if (Program.sveNoCheck
				&& (b1 instanceof BarrierDirective
						|| (b1 instanceof BeginNode && b1.getParent() instanceof ParallelConstruct))
				&& (b2 instanceof BarrierDirective
						|| (b2 instanceof BeginNode && b2.getParent() instanceof ParallelConstruct))) {
			return false;
		}
		boolean oneIsEntry = absPh.getBeginPoints().stream().anyMatch(b -> b.getNodeFromInterface() == b1);// OLD
																											// Code:
																											// bPP1.getPhaseSet().contains(absPh);
		boolean twoIsEntry = absPh.getBeginPoints().stream().anyMatch(b -> b.getNodeFromInterface() == b2);// OLD
																											// Code:
																											// bPP2.getPhaseSet().contains(absPh);
		if (oneIsEntry && twoIsEntry) {
			if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YCON) {
				Thread.dumpStack();
				assert (false) : "Unexpected path.";
				return true;
			}
			Phase ph = (Phase) absPh;
			for (AbstractPhase<?, ?> predPhaseAbs : ph.getPredPhases()) {
				Phase predPhase = (Phase) predPhaseAbs;
				Set<Node> nodeSet = predPhase.getNodeSet();
				if (nodeSet.contains(b1) && nodeSet.contains(b2)) {
					if (ContextSensitiveCoExistenceChecker.canCoExistInPhase(b1, b2, predPhase, nodePairs, expSet, c1,
							c2)) {
						return true;
					}
				}
			}
			return false; // There does not exist any pred-phase in which barriers may co-exist.
		} else if (oneIsEntry) {
			// Check if a successor of the BPP1 can co-exist with BPP2 in ph.
			return ContextSensitiveCoExistenceChecker.canCoExistInPhase(b2,
					b1.getInfo().getCFGInfo().getLeafSuccessors().get(0), absPh, nodePairs, expSet, c1, c2);
		} else if (twoIsEntry) {
			// Check if a successor of the BPP2 can co-exist with BPP3 in ph.
			return ContextSensitiveCoExistenceChecker.canCoExistInPhase(b1,
					b2.getInfo().getCFGInfo().getLeafSuccessors().get(0), absPh, nodePairs, expSet, c1, c2);
		} else {
			Set<Node> phNodeSet = absPh.getNodeSet();
			if (phNodeSet.contains(b1) && phNodeSet.contains(b2)) {
				// Here, both BPP1 and BPP2 belong to some internal parallel construct within
				// ph.
				return ContextSensitiveCoExistenceChecker.canCoExistInPhase(b1, b2, absPh, nodePairs, expSet, c1, c2);
			} else {
				return true; // Conservatively.
			}
		}

	}

}
