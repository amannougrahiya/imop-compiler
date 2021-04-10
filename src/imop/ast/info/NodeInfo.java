/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info;

import imop.ast.annotation.IncompleteSemantics;
import imop.ast.annotation.Label;
import imop.ast.annotation.PragmaImop;
import imop.ast.annotation.SimpleLabel;
import imop.ast.info.cfgNodeInfo.CompoundStatementInfo;
import imop.ast.info.cfgNodeInfo.FunctionDefinitionInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.deprecated.Deprecated_FlowFact;
import imop.deprecated.Deprecated_InterProceduralCFGPass;
import imop.lib.analysis.Assignment;
import imop.lib.analysis.AssignmentGetter;
import imop.lib.analysis.flowanalysis.*;
import imop.lib.analysis.flowanalysis.controlflow.DominanceAnalysis;
import imop.lib.analysis.flowanalysis.controlflow.DominanceAnalysis.DominatorFlowFact;
import imop.lib.analysis.flowanalysis.controlflow.IntraProceduralPredicateAnalysis;
import imop.lib.analysis.flowanalysis.controlflow.PredicateAnalysis;
import imop.lib.analysis.flowanalysis.dataflow.*;
import imop.lib.analysis.flowanalysis.dataflow.CopyPropagationAnalysis.CopyPropagationFlowMap;
import imop.lib.analysis.flowanalysis.dataflow.DataDependenceForward.DataDependenceForwardFF;
import imop.lib.analysis.flowanalysis.dataflow.ReachingDefinitionAnalysis.ReachingDefinitionFlowMap;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.ControlFlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis.FlowFact;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.analysis.mhp.incMHP.NodePhaseInfo;
import imop.lib.analysis.mhp.OldLock;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.analysis.mhp.yuan.BTNode;
import imop.lib.analysis.mhp.yuan.BarrierTreeConstructor;
import imop.lib.analysis.solver.BaseSyntax;
import imop.lib.analysis.solver.PointerDereferenceGetter;
import imop.lib.analysis.solver.SyntacticAccessExpression;
import imop.lib.analysis.solver.SyntacticAccessExpressionGetter;
import imop.lib.analysis.solver.tokens.Tokenizable;
import imop.lib.analysis.typeSystem.*;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.cg.CallSite;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.getter.*;
import imop.lib.getter.StringGetter.Commentor;
import imop.lib.transform.simplify.CompoundStatementNormalizer;
import imop.lib.util.*;
import imop.parser.FrontEnd;
import imop.parser.Program;
import imop.parser.Program.UpdateCategory;

import java.util.*;

/**
 * Corresponds to the information related to an AST node.
 *
 * @author Aman Nougrahiya
 */

public class NodeInfo implements Cloneable {
	public static boolean readWriteDestinationsSet = false;

	private Node node;
	private Node copySourceNode;

	/**
	 * Saves hashCode of the original copy of the related node. This field is used
	 * to find the duplicates.
	 */
	private int idNumber; // Saves hashCode of the original copy of the related
	// node.
	// This field is used to find duplicates.

	/**
	 * A map from analysis-name to IN flow-fact for this node.
	 */
	protected HashMap<AnalysisName, FlowFact> flowFactsIN;

	/**
	 * A map from analysis-name to OUT flow-fact for this node.
	 */
	protected HashMap<AnalysisName, FlowFact> flowFactsOUT;

	protected CFGInfo cfgInfo;
	private NodePhaseInfo phaseInfo;
	// private List<InterTaskEdge> interTaskEdgeList; // Not needed, since we will
	// be creating such edges only between dummy-flush directives.
	private List<ParallelConstruct> regionInfo;
	private LinkedList<CallStatement> callStatements = null;
	private IncompleteSemantics incomplete;

	private static enum TRISTATE {
		YES, NO, UNKNOWN
	}

	private TRISTATE reliesOnPtsTo = TRISTATE.UNKNOWN;
	private TRISTATE mayWriteToPointerType = TRISTATE.UNKNOWN;
	private int reversePostOrderId = -1;
	/**
	 * Set of all the cells that may be read in the execution of this node (and the
	 * called methods, if any).
	 * <br>
	 * Note that this field is used only for CFG leaf nodes. Once we add
	 * {@link Predicate} to appropriate places in the
	 * production rules, we can create appropriate interfaces for CFG leaf and
	 * non-leaf nodes, and add this field in the
	 * corresponding CFG nodes.
	 */
	private CellList readList;

	/**
	 * Set of all the cells that may be written in the execution of this node (and
	 * the called methods, if any).
	 * <br>
	 * Note that this field is used only for CFG leaf nodes. Once we add
	 * {@link Predicate} to appropriate places in the
	 * production rules, we can create appropriate interfaces for CFG leaf and
	 * non-leaf nodes, and add this field in the
	 * corresponding CFG nodes.
	 */
	private CellList writeList;

	/**
	 * Set of all the shared cells that may be read in the execution of this node
	 * (and the called methods, if any).
	 * <br>
	 * Note that this field is used only for CFG leaf nodes. Once we add
	 * {@link Predicate} to appropriate places in the
	 * production rules, we can create appropriate interfaces for CFG leaf and
	 * non-leaf nodes, and add this field in the
	 * corresponding CFG nodes.
	 */
	private CellSet sharedReadSet;

	/**
	 * Set of all the shared cells that may be written in the execution of this node
	 * (and the called methods, if any).
	 * <br>
	 * Note that this field is used only for CFG leaf nodes. Once we add
	 * {@link Predicate} to appropriate places in the
	 * production rules, we can create appropriate interfaces for CFG leaf and
	 * non-leaf nodes, and add this field in the
	 * corresponding CFG nodes.
	 */
	private CellSet sharedWriteSet;

	/**
	 * Set of all the shared cells that may be written in the execution of this node
	 * (and the called methods, if any),
	 * excluding all field accesses.
	 * <br>
	 * Note that this field is used only for CFG leaf nodes. Once we add
	 * {@link Predicate} to appropriate places in the
	 * production rules, we can create appropriate interfaces for CFG leaf and
	 * non-leaf nodes, and add this field in the
	 * corresponding CFG nodes.
	 */
	private CellSet nonFieldReadSet;

	/**
	 * Set of all the shared cells that may be written in the execution of this node
	 * (and the called methods, if any),
	 * excluding all field accesses.
	 * <br>
	 * Note that this field is used only for CFG leaf nodes. Once we add
	 * {@link Predicate} to appropriate places in the
	 * production rules, we can create appropriate interfaces for CFG leaf and
	 * non-leaf nodes, and add this field in the
	 * corresponding CFG nodes.
	 */
	private CellSet nonFieldWriteSet;

	/**
	 * Information about destinations of various nodes that can read a write
	 * happening at this node.
	 */
	private CellMap<NodeSet> readDestinations;

	/**
	 * Information about destinations of various nodes that can read a write
	 * happening at this node.
	 */
	private CellMap<NodeSet> writeDestinations;

	/**
	 * List of access-expressions (composed up of {@link BaseSyntax} and
	 * List{@literal <}{@link Tokenizable}{@literal
	 * >}) representing array dereferences that may be read anywhere in this node.
	 */
	private List<SyntacticAccessExpression> baseAccessExpressionReads;

	/**
	 * List of access-expressions (composed up of {@link BaseSyntax} and
	 * List{@literal <}{@link Tokenizable}{@literal
	 * >}) representing array dereferences that may be written anywhere in this
	 * node.
	 */
	private List<SyntacticAccessExpression> baseAccessExpressionWrites;

	/**
	 * List of all the {@link CastExpression} expressions on which dereference may
	 * have been made using an asterisk
	 * ({@code *}), and whose dereferenced location may have been read within this
	 * node.
	 */
	private List<CastExpression> pointerDereferencedExpressionReads;
	/**
	 * List of all the {@link CastExpression} expressions on which dereference may
	 * have been made using an asterisk
	 * ({@code *}), and whose dereferenced location may have been written within
	 * this node.
	 */
	private List<CastExpression> pointerDereferencedExpressionWrites;

	private List<String> comments;

	private List<PragmaImop> pragmaAnnotations;

	private static Commentor defaultCommentor;

	private static boolean livenessDone = false;
	private static boolean ddfDone = false;
	public static boolean rdDone = false;
	private static boolean daDone = false;
	private static boolean hvDone = false;
	private static boolean paDone = false;

	private BTNode btNode = null;
	public static long callQueryTimer = 0;

	/*
	 * For MHP of Yuan et al.
	 */
	private Set<Node> forwardReachableNodes = null;
	private Set<Node> backwardReachableNodes = null;
	private Set<Node> forwardReachableBarriers = null;
	private Set<Node> backwardReachableBarriers = null;

	public Set<Node> getFWRNodes() {
		if (this.node instanceof EndNode && this.node.getParent() instanceof ParallelConstruct) {
			return new HashSet<>();
		}
		populateForward();
		// if (this.forwardReachableNodes == null) {
		// }
		return this.forwardReachableNodes;
	}

	public Set<Node> getFWRBarriers() {
		if (this.node instanceof EndNode && this.node.getParent() instanceof ParallelConstruct) {
			return new HashSet<>();
		}
		populateForward();
		// if (this.forwardReachableBarriers == null) {
		// }
		return this.forwardReachableBarriers;
	}

	public Set<Node> getBWRNodes() {
		if (this.node instanceof BeginNode && this.node.getParent() instanceof ParallelConstruct) {
			return new HashSet<>();
		}
		populateBackward();
		// if (this.backwardReachableNodes == null) {
		// }
		return this.backwardReachableNodes;
	}

	public Set<Node> getBWRBarriers() {
		if (this.node instanceof BeginNode && this.node.getParent() instanceof ParallelConstruct) {
			return new HashSet<>();
		}
		populateBackward();
		// if (this.backwardReachableBarriers == null) {
		// }
		return this.backwardReachableBarriers;
	}
	/*
	 * MHP info above.
	 */

	private void populateForward() {
		Set<NodeWithStack> endPoints = new HashSet<>();
		Set<NodeWithStack> reachablesWithStack = new HashSet<>();
		for (NodeWithStack succ : this.getNode().getInfo().getCFGInfo()
				.getInterProceduralLeafSuccessors(new CallStack())) {
			Set<NodeWithStack> someEndPoints = new HashSet<>();
			reachablesWithStack
					.addAll(CollectorVisitor.collectNodesIntraTaskForwardBarrierFreePath(succ, someEndPoints));
			reachablesWithStack.add(succ);
			reachablesWithStack.addAll(someEndPoints);
			endPoints.addAll(someEndPoints);
		}
		this.forwardReachableNodes = new HashSet<>();
		for (NodeWithStack nws : reachablesWithStack) {
			this.forwardReachableNodes.add(nws.getNode());
		}
		this.forwardReachableBarriers = new HashSet<>();
		for (NodeWithStack nws : endPoints) {
			this.forwardReachableBarriers.add(nws.getNode());
		}
	}

	private void populateBackward() {
		Set<NodeWithStack> endPoints = new HashSet<>();
		Set<NodeWithStack> reachablesWithStack = new HashSet<>();
		for (NodeWithStack pred : this.getNode().getInfo().getCFGInfo()
				.getInterProceduralLeafPredecessors(new CallStack())) {
			Set<NodeWithStack> someEndPoints = new HashSet<>();
			reachablesWithStack
					.addAll(CollectorVisitor.collectNodesIntraTaskBackwardBarrierFreePath(pred, someEndPoints));
			reachablesWithStack.add(pred);
			reachablesWithStack.addAll(someEndPoints);
			endPoints.addAll(someEndPoints);
		}
		this.backwardReachableNodes = new HashSet<>();
		for (NodeWithStack nws : reachablesWithStack) {
			this.backwardReachableNodes.add(nws.getNode());
		}
		this.backwardReachableBarriers = new HashSet<>();
		for (NodeWithStack nws : endPoints) {
			this.backwardReachableBarriers.add(nws.getNode());
		}
	}

	private Set<Expression> jumpedPredicates;

	public Set<Expression> getJumpedPredicates() {
		if (jumpedPredicates == null) {
			jumpedPredicates = new HashSet<>();
		}
		return jumpedPredicates;
	}

	public int getReversePostOrder() {
		// Program.stabilizeReversePostOrderOfLeaves();
		if (CFGInfo.isSCCStale) {
			SCC.initializeSCC();
		}
		SCC scc = this.getCFGInfo().getSCC();
		if (scc == null) {
			// This itself is an SCC; return the ID as it is.
		} else {
			/*
			 * In this case, we would assume that the request is made for the
			 * reverse-postorder id of the node within its scc. We trigger this
			 * stabilization, if not done already.
			 */
			scc.stabilizeInternalRPO();
		}
		return this.reversePostOrderId;
	}

	public void setReversePostOrderId(int i) {
		this.reversePostOrderId = i;
	}

	/**
	 * This method ensures that the points-to flow facts for all nodes have been
	 * stabilized, if required. Note that
	 * memoization of all access lists is disabled when running the stabilization of
	 * points-to.
	 * <br>
	 * IMPORTANT: This method must always be called before attempting to read any
	 * (of the six) memoized access list.
	 *
	 * @return true, if any change might have been made.
	 */
	@Deprecated
	private boolean stabilizePointsToIfRequired() {
		FlowAnalysis<?> pointsTo = FlowAnalysis.getAllAnalyses().get(AnalysisName.POINTSTO);
		if (pointsTo == null || !pointsTo.stateIsInvalid()) {
			return false;
		}
		pointsTo.markStateToBeValid();

		Program.memoizeAccesses++;
		pointsTo.restartAnalysisFromStoredNodes();
		Program.memoizeAccesses--;
		return true;
	}

	/**
	 * Checks whether the points-to flow-facts might be stale, without resetting the
	 * invalid flag. When this flag is
	 * set, none of the memoized access lists are used as they are.
	 *
	 * @return
	 */
	private boolean pointsToMayBeStale() {
		FlowAnalysis<?> pointsTo = FlowAnalysis.getAllAnalyses().get(AnalysisName.POINTSTO);
		if (pointsTo != null && !pointsTo.stateIsInvalid()) {
			return false;
		} else {
			return true;
		}
	}

	public static void resetFirstRunFlags() {
		NodeInfo.ddfDone = false;
		NodeInfo.daDone = false;
		NodeInfo.livenessDone = false;
		NodeInfo.rdDone = false;
		NodeInfo.callQueryTimer = 0;
		NodeInfo.defaultCommentor = null;
	}

	/**
	 * For some special cases, if the analysis has not yet been run even once, this
	 * method runs the analysis.
	 *
	 * @param analysisName
	 *                     an analysis name; check the method body to add more
	 *                     analyses to the list of allowed analyses here.
	 */
	private static void checkFirstRun(AnalysisName analysisName) {
		if (analysisName == AnalysisName.INTRA_PREDICATE_ANALYSIS || analysisName == AnalysisName.PREDICATE_ANALYSIS) {
			if (!NodeInfo.paDone) {
				NodeInfo.paDone = true;
				performPredicateAnalysis();
			}
		} else if (analysisName == AnalysisName.LIVENESS) {
			if (!NodeInfo.livenessDone) {
				NodeInfo.livenessDone = true;
				performLivenessAnalysis();
			}
		} else if (analysisName == AnalysisName.DATA_DEPENDENCE_FORWARD) {
			if (!NodeInfo.ddfDone) {
				NodeInfo.ddfDone = true;
				performDDF();
			}
		} else if (analysisName == AnalysisName.REACHING_DEFINITION) {
			if (!NodeInfo.rdDone) {
				NodeInfo.rdDone = true;
				performRDA();
				// Main.dumpReachingDefinitions("");
			}
		} else if (analysisName == AnalysisName.DOMINANCE) {
			if (!NodeInfo.daDone) {
				NodeInfo.daDone = true;
				performDominanceAnalysis();
			}
		} else if (analysisName == AnalysisName.HEAP_VALIDITY) {
			if (!NodeInfo.hvDone) {
				NodeInfo.hvDone = true;
				performHeapValidityAnalysis();
			}
		}
	}

	private static void performPredicateAnalysis() {
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		if (mainFunc == null) {
			return;
		}
		System.err.println("Pass: Performing predicate analysis.");
		long timeStart = System.nanoTime();
		ControlFlowAnalysis<?> pa;
		if (Program.useInterProceduralPredicateAnalysis) {
			pa = new PredicateAnalysis();
			pa.run(mainFunc);
		} else {
			pa = new IntraProceduralPredicateAnalysis();
			pa.run(mainFunc);
		}
		long timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tNodes processed " + pa.nodesProcessed + " times.");
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
	}

	private static void performLivenessAnalysis() {
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		if (mainFunc == null) {
			return;
		}
		System.err.println("Pass: Performing liveness analysis.");
		long timeStart = System.nanoTime();
		LivenessAnalysis lva = new LivenessAnalysis();
		lva.run(mainFunc);
		long timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tNodes processed " + lva.nodesProcessed + " times.");
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
	}

	private static void performRDA() {
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		if (mainFunc == null) {
			return;
		}
		System.err.println("Pass: Performing reaching-definition analysis.");
		long timeStart = System.nanoTime();
		ReachingDefinitionAnalysis rda = new ReachingDefinitionAnalysis();
		rda.run(mainFunc);
		long timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tNodes processed " + rda.nodesProcessed + " times.");
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
	}

	private static void performDDF() {
		// Thread.dumpStack();
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		if (mainFunc == null) {
			return;
		}
		System.err.println("Pass: Performing data-dependence analysis.");
		System.err.println("\tPerforming forward R/W analysis.");
		long timeStart = System.nanoTime();
		DataDependenceForward ddf = new DataDependenceForward();
		ddf.run(mainFunc);
		long timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tNodes processed (forward) " + ddf.nodesProcessed + " times.");
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
		DumpSnapshot.dumpWriteSources();
	}

	private static void performDominanceAnalysis() {
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		if (mainFunc == null) {
			return;
		}
		System.err.println("Pass: Performing dominator analysis.");
		long timeStart = System.nanoTime();
		DominanceAnalysis da = new DominanceAnalysis();
		da.run(mainFunc);
		long timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tNodes processed " + da.nodesProcessed + " times.");
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
	}

	private static void performHeapValidityAnalysis() {
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		if (mainFunc == null) {
			return;
		}
		System.err.println("Pass: Performing heap validity analysis.");
		long timeStart = System.nanoTime();
		HeapValidityAnalysis hva = new HeapValidityAnalysis();
		hva.run(mainFunc);
		long timeTaken = System.nanoTime() - timeStart;
		System.err.println("\tNodes processed " + hva.nodesProcessed + " times.");
		System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
	}

	/**
	 * Obtain the IN flow-fact for this node, for {@code analysisName}. <br>
	 * Note that this function may return {@code
	 * null}, which implies the following:
	 * <ul>
	 * <li>during forward analysis: this node has not yet been processed for
	 * {@code analysisName}, even once.</li>
	 * <li>during backward analysis: this node has not been initialized for
	 * {@code analysisName}.</li>
	 * </ul>
	 *
	 * @param analysisName
	 *                     name (ID) of the analysis for which the IN flow-fact of
	 *                     this node has to be obtained.
	 *
	 * @return
	 */
	public FlowFact getIN(AnalysisName analysisName) {
		return this.getIN(analysisName, null);
	}

	public FlowFact getIN(AnalysisName analysisName, Cell thisCell) {
		ProfileSS.addRelevantChangePoint(ProfileSS.ptaSet);
		checkFirstRun(analysisName);
		/*
		 * Step 1: Get the analysis. If the analysis is INVALID, then mark it to
		 * be valid, after running the analysis.
		 */
		FlowAnalysis<?> analysisHandle = FlowAnalysis.getAllAnalyses().get(analysisName);
		if (analysisHandle != null) {
			/*
			 * For this forward analysis, let's make the state consistent first,
			 * if required.
			 */
			if (Program.updateCategory == UpdateCategory.EGINV || Program.updateCategory == UpdateCategory.EGUPD
					|| Program.updateCategory == UpdateCategory.CPINV
					|| Program.updateCategory == UpdateCategory.CPUPD) {
				assert (!analysisHandle.stateIsInvalid());
			} else if (Program.updateCategory == UpdateCategory.LZINV) {
				if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP
						|| Program.mhpUpdateCategory == UpdateCategory.LZINV) {
					if (AbstractPhase.globalMHPStale) {
						AbstractPhase.globalMHPStale = false;
						AutomatedUpdater.reinitMHP();
					}
				} else {
					BeginPhasePoint.stabilizeStaleBeginPhasePoints();
				}
				if (analysisHandle.stateIsInvalid()) {
					analysisHandle.markStateToBeValid();
					AutomatedUpdater.reinitIDFA(analysisHandle);
				}
			} else {
				assert (Program.updateCategory == UpdateCategory.LZUPD);
				if (thisCell == null) {
					if (analysisHandle.stateIsInvalid()) {
						if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP
								|| Program.mhpUpdateCategory == UpdateCategory.LZINV) {
							if (AbstractPhase.globalMHPStale) {
								AbstractPhase.globalMHPStale = false;
								AutomatedUpdater.reinitMHP();
							}
						} else {
							BeginPhasePoint.stabilizeStaleBeginPhasePoints();
						}
						analysisHandle.markStateToBeValid();
						if (analysisName == AnalysisName.POINTSTO) {
							Program.memoizeAccesses++;
							// System.out.println("Triggering PTA stabilization in response to a query of
							// points-to on " + Symbol.tempNow.getName());
							analysisHandle.restartAnalysisFromStoredNodes();
							Program.memoizeAccesses--;
						} else {
							analysisHandle.restartAnalysisFromStoredNodes();
						}
					}
				} else {
					// if (analysisHandle.stateIsInvalid()) {
					if (analysisHandle.stateIsInvalid()
							&& (analysisName != AnalysisName.POINTSTO || !PointsToAnalysis.isHeuristicEnabled
									|| PointsToAnalysis.affectedCellsInThisEpoch.contains(thisCell))) {
						if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP
								|| Program.mhpUpdateCategory == UpdateCategory.LZINV) {
							if (AbstractPhase.globalMHPStale) {
								AbstractPhase.globalMHPStale = false;
								AutomatedUpdater.reinitMHP();
							}
						} else {
							BeginPhasePoint.stabilizeStaleBeginPhasePoints();
						}
						analysisHandle.markStateToBeValid();
						if (analysisName == AnalysisName.POINTSTO) {
							Program.memoizeAccesses++;
							// System.out.println("Triggering PTA stabilization in response to a query of
							// points-to on " + Symbol.tempNow.getName());
							analysisHandle.restartAnalysisFromStoredNodes();
							Program.memoizeAccesses--;
						} else {
							analysisHandle.restartAnalysisFromStoredNodes();
						}
					}
				}
			}
		}
		/*
		 * Step 2: Now, return the stabilized IN fact.
		 */
		if (flowFactsIN == null) {
			flowFactsIN = new HashMap<>();
		}
		return flowFactsIN.get(analysisName);
	}

	/**
	 * Get the current IN object for give data-flow analysis, without running
	 * stabilization pass.
	 *
	 * @param analysisName
	 *                     analysis whose IN object has to be obtained.
	 *
	 * @return (unstable) IN flow-fact for {@code analysisName}.
	 */
	public FlowFact getCurrentIN(AnalysisName analysisName) {
		checkFirstRun(analysisName);
		if (flowFactsIN == null) {
			flowFactsIN = new HashMap<>();
		}
		return flowFactsIN.get(analysisName);

	}

	/**
	 * Get the current OUT object for give data-flow analysis, without running
	 * stabilization pass.
	 *
	 * @param analysisName
	 *                     analysis whose OUT object has to be obtained.
	 *
	 * @return (unstable) OUT flow-fact for {@code analysisName}.
	 */
	public FlowFact getCurrentOUT(AnalysisName analysisName) {
		checkFirstRun(analysisName);
		if (flowFactsOUT == null) {
			flowFactsOUT = new HashMap<>();
		}
		return flowFactsOUT.get(analysisName);

	}

	/**
	 * Sets {@code flowFact} as the IN flow-fact for {@code analysisName} for this
	 * node.
	 *
	 * @param analysisName
	 *                     name (ID) of the analysis for which the OUT flow-fact of
	 *                     this node has to be obtained.
	 * @param flowFact
	 *                     new flow-fact to be set.
	 */
	public void setIN(AnalysisName analysisName, FlowFact flowFact) {
		if (flowFactsIN == null) {
			flowFactsIN = new HashMap<>();
		}
		FlowFact oldIN = flowFactsIN.get(analysisName);
		if (oldIN == flowFact) {
			return;
		}
		if (analysisName == AnalysisName.POINTSTO) {
			if (this.mayRelyOnPtsTo()) {
				if (oldIN == null || !oldIN.isEqualTo(flowFact)) {
					this.invalidateAccessLists();
				}
			}
		}
		flowFactsIN.put(analysisName, flowFact);

		return;
	}

	/**
	 * Obtain the OUT flow-fact for this node, for {@code analysisName}. <br>
	 * Note that this function may return {@code
	 * null}, which implies the following:
	 * <ul>
	 * <li>during forward analysis: this node has not been initialized for
	 * {@code analysisName}.</li>
	 * <li>during backward analysis: this node has not yet been processed for
	 * {@code analysisName}, even once.</li>
	 * </ul>
	 *
	 * @param analysisName
	 *                     name (ID) of the analysis for which the OUT flow-fact of
	 *                     this node has to be obtained.
	 *
	 * @return
	 */
	public FlowFact getOUT(AnalysisName analysisName) {
		ProfileSS.addRelevantChangePoint(ProfileSS.ptaSet);
		checkFirstRun(analysisName);
		/*
		 * Step 1: Get the analysis. If the analysis is INVALID, then mark it to
		 * be valid, after running the analysis.
		 */
		FlowAnalysis<?> analysisHandle = FlowAnalysis.getAllAnalyses().get(analysisName);
		if (analysisHandle != null) {
			/*
			 * For this forward analysis, let's make the state consistent first,
			 * if required.
			 */
			if (Program.updateCategory == UpdateCategory.EGINV || Program.updateCategory == UpdateCategory.EGUPD
					|| Program.updateCategory == UpdateCategory.CPINV
					|| Program.updateCategory == UpdateCategory.CPUPD) {
				assert (!analysisHandle.stateIsInvalid());
			} else if (Program.updateCategory == UpdateCategory.LZINV) {
				if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP
						|| Program.mhpUpdateCategory == UpdateCategory.LZINV) {
					if (AbstractPhase.globalMHPStale) {
						AbstractPhase.globalMHPStale = false;
						AutomatedUpdater.reinitMHP();
					}
				} else {
					BeginPhasePoint.stabilizeStaleBeginPhasePoints();
				}
				if (analysisHandle.stateIsInvalid()) {
					analysisHandle.markStateToBeValid();
					AutomatedUpdater.reinitIDFA(analysisHandle);
				}
			} else {
				assert (Program.updateCategory == UpdateCategory.LZUPD);
				if (analysisHandle.stateIsInvalid()) {
					if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP
							|| Program.mhpUpdateCategory == UpdateCategory.LZINV) {
						if (AbstractPhase.globalMHPStale) {
							AbstractPhase.globalMHPStale = false;
							AutomatedUpdater.reinitMHP();
						}
					} else {
						BeginPhasePoint.stabilizeStaleBeginPhasePoints();
					}
					analysisHandle.markStateToBeValid();
					if (analysisName == AnalysisName.POINTSTO) {
						Program.memoizeAccesses++;
						analysisHandle.restartAnalysisFromStoredNodes();
						Program.memoizeAccesses--;
					} else {
						analysisHandle.restartAnalysisFromStoredNodes();
					}
				}
			}
		}
		if (flowFactsOUT == null) {
			flowFactsOUT = new HashMap<>();
		}
		return flowFactsOUT.get(analysisName);
	}

	public FlowFact getStaleOUT(AnalysisName analysisName) {
		if (flowFactsOUT == null) {
			flowFactsOUT = new HashMap<>();
		}
		return flowFactsOUT.get(analysisName);
	}

	/**
	 * Sets {@code flowFact} as the OUT flow-fact for {@code analysisName} for this
	 * node.
	 *
	 * @param analysisName
	 *                     name (ID) of the analysis for which the OUT flow-fact of
	 *                     this node has to be obtained.
	 * @param flowFact
	 *                     new flow-fact to be set.
	 */
	public void setOUT(AnalysisName analysisName, FlowFact flowFact) {
		if (flowFactsOUT == null) {
			flowFactsOUT = new HashMap<>();
		}
		FlowFact oldOUT = flowFactsOUT.get(analysisName);
		if (oldOUT == flowFact) {
			return;
		}
		if (analysisName == AnalysisName.POINTSTO) {
			if (oldOUT == null || !oldOUT.isEqualTo(flowFact)) {
				this.invalidateAccessLists();
			}
		}
		flowFactsOUT.put(analysisName, flowFact);
		return;
	}

	public void removeAnalysisInformation(AnalysisName analysisName) {
		if (flowFactsIN != null) {
			flowFactsIN.remove(analysisName);
		}
		if (flowFactsOUT != null) {
			flowFactsOUT.remove(analysisName);
		}
	}

	public void removeAllAnalysisInformation() {
		if (flowFactsIN != null) {
			flowFactsIN.clear();
		}
		if (flowFactsOUT != null) {
			flowFactsOUT.clear();
		}
	}

	public NodeInfo(Node owner) {
		setNode(owner);
		setIdNumber(getNode().hashCode()); // May change later if this node has been
		// derived as a copy of some other node.
		if (Misc.isCFGLeafNode(owner)) {
			deprecated_flowFactsIN = new HashMap<>();
			deprecated_flowFactsOUT = new HashMap<>();
			deprecated_parallelFlowFactsIN = new HashMap<>();
			deprecated_parallelFlowFactsOUT = new HashMap<>();
		}
	}

	public void setCopySourceNode(Node copySourceNode) {
		this.copySourceNode = copySourceNode;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public IncompleteSemantics getIncompleteSemantics() {
		if (incomplete == null) {
			incomplete = new IncompleteSemantics(getNode());
		}
		return incomplete;
	}

	public CFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			if (copySourceNode != null) {
				this.cfgInfo = copySourceNode.getInfo().getCFGInfo().getCopy(this.getNode());
			} else {
				this.cfgInfo = new CFGInfo(getNode());
			}
		}
		return cfgInfo;
	}

	public NodePhaseInfo getNodePhaseInfo() {
		if (Misc.isCFGNonLeafNode(this.getNode())) {
			BeginNode beginNode = this.getCFGInfo().getNestedCFG().getBegin();
			return beginNode.getInfo().getNodePhaseInfo();
		}
		if (phaseInfo == null) {
			phaseInfo = new NodePhaseInfo(getNode());
		}
		return phaseInfo;
	}

	public NodePhaseInfo readNodePhaseInfo() {
		return phaseInfo;
	}

	public List<ParallelConstruct> getRegionInfo() {
		if (regionInfo == null) {
			regionInfo = new ArrayList<>();
		}
		return regionInfo;
	}

	public List<ParallelConstruct> readRegionInfo() {
		return regionInfo;
	}

	public boolean isRunnableInRegion(ParallelConstruct par) {
		if (regionInfo == null) {
			return false;
		}
		for (ParallelConstruct present : regionInfo) {
			if (present == par) {
				return true;
			}
		}
		return false;
	}

	/**
	 * For a given symbol, this method tells whether the corresponding block of
	 * storage is shared or private at this
	 * node.
	 *
	 * @param name
	 *
	 * @return
	 */
	public DataSharingAttribute getSharingAttribute(Cell cell) {
		assert (cell != null);
		if (cell instanceof HeapCell) {
			return DataSharingAttribute.SHARED;
		} else if (cell instanceof FreeVariable) {
			return DataSharingAttribute.SHARED;
		} else if (cell instanceof AddressCell) {
			return this.getSharingAttribute(((AddressCell) cell).getPointedElement());
		} else if (cell instanceof FieldCell) {
			return this.getSharingAttribute(((FieldCell) cell).getAggregateElement());
		} else if (!(cell instanceof Symbol)) {
			if (cell == Cell.getNullCell()) {
				return DataSharingAttribute.PRIVATE; // Or should it be UNDEFINED?
			}
			return DataSharingAttribute.SHARED;
		}
		Symbol sym = (Symbol) cell;
		if (sym == Cell.genericCell) {
			return DataSharingAttribute.SHARED;
		}

		if (Program.getRoot().getInfo().getThreadPrivateList().values().contains(sym)) {
			return DataSharingAttribute.THREADPRIVATE;
		}

		Node encloser = getNode();
		while (encloser != null) {
			if (encloser instanceof CompoundStatement) {
				CompoundStatementInfo compStmtInfo = ((CompoundStatementInfo) encloser.getInfo());
				HashMap<String, Symbol> symbolMap = compStmtInfo.getSymbolTable();
				// if (sym.getName().equals("x"))
				// System.out.println();
				// System.out.println(encloser);
				// for (String symName : symbolMap.keySet()) {
				// System.out.println(symName + " : " + symbolMap.get(symName).getType());
				// }
				// }
				if (symbolMap.containsKey(sym.getName())) {
					if (sym.isStatic()) {
						return DataSharingAttribute.SHARED;
					} else {
						return DataSharingAttribute.PRIVATE;
					}
				}
			} else if (encloser instanceof FunctionDefinition) {
				// if "sym" is present in the symbolTable of encloser,
				// then return Shared.
				FunctionDefinitionInfo funcDefInfo = ((FunctionDefinitionInfo) encloser.getInfo());
				HashMap<String, Symbol> symbolMap = funcDefInfo.getSymbolTable();

				// if (sym.getName() != null && (sym.getName().equals("r") ||
				// sym.getName().equals("k"))) {
				// System.err.println(symbolMap);
				// }

				if (symbolMap.containsKey(sym.getName())) {
					if (sym.isStatic()) {
						return DataSharingAttribute.SHARED;
					} else {
						return DataSharingAttribute.PRIVATE;
					}
				}
			} else if (encloser instanceof TranslationUnit) {
				RootInfo rootInfo = (RootInfo) encloser.getInfo();
				HashMap<String, Symbol> symbolMap = rootInfo.getSymbolTable();
				if (symbolMap.containsKey(sym.getName())) {
					return DataSharingAttribute.SHARED;
				} else {
					// Thread.dumpStack();
					// Misc.warnDueToLackOfFeature(
					// "Couldn't find an enclosing scope for the symbol " + sym.getName() + ".",
					// Program.root);
					return DataSharingAttribute.SHARED;
				}
			} else if (encloser instanceof OmpConstruct) {
				// if encloser is a for or parallel for construct, then if "s"
				// is same as the iteration variable, then return private.
				if (encloser instanceof ForConstruct) {
					NodeToken loopIteratorToken = ((ForConstruct) encloser).getF2().getF2().getF0();
					Cell iteratorCell = Misc.getSymbolOrFreeEntry(loopIteratorToken);
					if (!(iteratorCell instanceof Symbol)) {
						return DataSharingAttribute.SHARED;
					} else {
						Symbol iteratorSymbol = (Symbol) iteratorCell;
						if (iteratorSymbol == sym) {
							return DataSharingAttribute.PRIVATE;
						}
					}
				} else if (encloser instanceof ParallelForConstruct) {
					assert (false);
					// String loopIteratorName = ((ParallelForConstruct)
					// encloser).getF5().getF2().getF0().tokenImage;
					// Symbol iteratorSymbol = Misc.getSymbolEntry(loopIteratorName, encloser);
					// if (iteratorSymbol == sym) {
					// return DataSharingAttribute.PRIVATE;
					// }
				} else if (encloser instanceof ParallelConstruct || encloser instanceof TaskConstruct) {
					// if n is explicitly determined, return the explicit answer.
					List<OmpClause> ompClauseList = ((OmpConstructInfo) encloser.getInfo()).getOmpClauseList();
					for (OmpClause clause : ompClauseList) {
						if (clause instanceof OmpSharedClause) {
							OmpSharedClause sharedClause = (OmpSharedClause) clause;
							VariableList varList = sharedClause.getF2();
							// Symbol symVar = Misc.getSymbolEntry(varList.getF0().getTokenImage(),
							// encloser);
							Cell cellVar = Misc.getSymbolOrFreeEntry(varList.getF0());
							if (!(cellVar instanceof Symbol)) {
								return DataSharingAttribute.SHARED;
								// System.out.println("Found no declaration for " +
								// varList.getF0().getTokenImage());
								// System.exit(1);
							} else {
								Symbol symVar = (Symbol) cellVar;
								if (symVar == sym) {
									return DataSharingAttribute.SHARED;
								}
							}
							for (Node nodeChoice : varList.getF1().getNodes()) {
								// String varName = ((NodeToken) ((NodeSequence) nodeChoice).getNodes().get(1))
								// .getTokenImage();
								// Symbol symVar = Misc.getSymbolEntry(varName, encloser);
								cellVar = Misc.getSymbolOrFreeEntry(
										((NodeToken) ((NodeSequence) nodeChoice).getNodes().get(1)));
								if (!(cellVar instanceof Symbol)) {
									return DataSharingAttribute.SHARED;
								} else {
									Symbol symVar = (Symbol) cellVar;
									if (symVar == sym) {
										return DataSharingAttribute.SHARED;
									}
								}
							}
						} else if (clause instanceof OmpFirstPrivateClause) {
							OmpFirstPrivateClause privateClause = (OmpFirstPrivateClause) clause;
							VariableList varList = privateClause.getF2();
							// Symbol symVar = Misc.getSymbolEntry(varList.getF0().getTokenImage(),
							// encloser);
							Cell cellVar = Misc.getSymbolOrFreeEntry(varList.getF0());
							if (!(cellVar instanceof Symbol)) {
								return DataSharingAttribute.SHARED;
							} else {
								Symbol symVar = (Symbol) cellVar;
								if (symVar == sym) {
									return DataSharingAttribute.PRIVATE;
								}
							}
							for (Node nodeChoice : varList.getF1().getNodes()) {
								cellVar = Misc.getSymbolOrFreeEntry(
										(NodeToken) ((NodeSequence) nodeChoice).getNodes().get(1));
								if (!(cellVar instanceof Symbol)) {
									return DataSharingAttribute.SHARED;
								} else {
									Symbol symVar = (Symbol) cellVar;
									if (symVar == sym) {
										return DataSharingAttribute.PRIVATE;
									}
								}
							}
						} else if (clause instanceof OmpLastPrivateClause) {
							OmpLastPrivateClause privateClause = (OmpLastPrivateClause) clause;
							VariableList varList = privateClause.getF2();
							Cell cellVar = Misc.getSymbolOrFreeEntry(varList.getF0());
							if (!(cellVar instanceof Symbol)) {
								return DataSharingAttribute.SHARED;
							} else {
								Symbol symVar = (Symbol) cellVar;
								if (symVar == sym) {
									return DataSharingAttribute.PRIVATE;
								}
							}
							for (Node nodeChoice : varList.getF1().getNodes()) {
								cellVar = Misc.getSymbolOrFreeEntry(
										(NodeToken) ((NodeSequence) nodeChoice).getNodes().get(1));
								if (!(cellVar instanceof Symbol)) {
									return DataSharingAttribute.SHARED;
								} else {
									Symbol symVar = (Symbol) cellVar;
									if (symVar == sym) {
										return DataSharingAttribute.PRIVATE;
									}
								}
							}
						} else if (clause instanceof OmpPrivateClause) {
							OmpPrivateClause privateClause = (OmpPrivateClause) clause;
							VariableList varList = privateClause.getF2();
							Cell cellVar = Misc.getSymbolOrFreeEntry(varList.getF0());
							if (!(cellVar instanceof Symbol)) {
								return DataSharingAttribute.SHARED;
							} else {
								Symbol symVar = (Symbol) cellVar;
								if (symVar == sym) {
									return DataSharingAttribute.PRIVATE;
								}
							}
							for (Node nodeChoice : varList.getF1().getNodes()) {
								cellVar = Misc.getSymbolOrFreeEntry(
										(NodeToken) ((NodeSequence) nodeChoice).getNodes().get(1));
								if (!(cellVar instanceof Symbol)) {
									return DataSharingAttribute.SHARED;
								} else {
									Symbol symVar = (Symbol) cellVar;
									if (symVar == sym) {
										return DataSharingAttribute.PRIVATE;
									}
								}
							}
						}
					}
					// if n is defined with a default, return the default answer.
					for (OmpClause clause : ompClauseList) {
						if (clause instanceof OmpDfltSharedClause) {
							return DataSharingAttribute.SHARED;
						}
					}

					if (encloser instanceof ParallelConstruct) {
						return DataSharingAttribute.SHARED;
					}
				}
			}
			encloser = encloser.getParent();
		}

		return DataSharingAttribute.SHARED;
	}

	public boolean mayRelyOnPtsTo() {
		Node node = this.getNode();
		if (Misc.isCFGLeafNode(node)) {
			if (this.reliesOnPtsTo == TRISTATE.UNKNOWN) {
				this.reliesOnPtsTo = CellAccessGetter.mayRelyOnPointsTo(node) ? TRISTATE.YES : TRISTATE.NO;
			}
			return this.reliesOnPtsTo == TRISTATE.YES;
		} else {
			return CellAccessGetter.mayRelyOnPointsTo(node);
		}
	}

	private boolean mayRelyOnPtsToForSymbols() {
		Node node = this.getNode();
		if (Misc.isCFGLeafNode(node)) {
			if (this.reliesOnPtsTo == TRISTATE.UNKNOWN) {
				this.reliesOnPtsTo = CellAccessGetter.mayRelyOnPointsToForSymbols(node) ? TRISTATE.YES : TRISTATE.NO;
			}
			return this.reliesOnPtsTo == TRISTATE.YES;
		} else {
			return CellAccessGetter.mayRelyOnPointsToForSymbols(node);
		}
	}

	/**
	 * Returns a set of all the cells that may be read in the execution of this node
	 * (and the called methods, if any),
	 * excluding the nodes that are encountered after the control leaves this node
	 * from any exit-point.
	 *
	 * @return set of all those cells that may be read between the entry to and exit
	 *         from this node.
	 */
	public CellList getReads() {
		if (Misc.isCFGLeafNode(this.getNode())) {
			/*
			 * If the pointer analysis is invalid, stabilize it first; while
			 * running the pointer analysis, make sure that the memoization is
			 * turned off.
			 */
			if (this.readList == null) {
				readList = CellAccessGetter.getReads(this.getNode());
			} else {
				boolean mayRelyOnPointsTo = this.mayRelyOnPtsTo();
				if (mayRelyOnPointsTo) {
					if (pointsToMayBeStale() || Program.memoizeAccesses > 0) {
						readList = CellAccessGetter.getReads(this.getNode());
					}
				}
			}
			return this.readList;
		} else if (Misc.isCFGNonLeafNode(this.getNode())) {
			CellList nonLeafReadList = new CellList();
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				CellList leafReadList = leafContent.getInfo().getReads();
				nonLeafReadList.addAll(leafReadList);
			}
			return nonLeafReadList;
		} else {
			return CellAccessGetter.getReads(this.getNode());
		}
	}

	/**
	 * Returns a set of all the cells that may be written in the execution of this
	 * node (and the called methods, if
	 * any). Note that this method also considers those accesses that may happen in
	 * nodes that are not lexically inside
	 * the owner node, and those that may be reachable via some jumps (not just
	 * calls).
	 *
	 * <br>
	 * Note that this field is used only for CFG leaf nodes. Once we add
	 * {@link Predicate} to appropriate places in the
	 * production rules, we can create appropriate interfaces for CFG leaf and
	 * non-leaf nodes, and add this field in the
	 * corresponding CFG nodes.
	 *
	 * @return set of all those cells that may be written in the execution of this
	 *         node.
	 */
	public CellList getWrites() {
		if (Misc.isCFGLeafNode(this.getNode())) {
			/*
			 * If the pointer analysis is invalid, stabilize it first; while
			 * running the pointer analysis, make sure that the memoization is
			 * turned off.
			 */

			if (this.writeList == null) {
				writeList = CellAccessGetter.getWrites(this.getNode());
			} else {
				boolean mayRelyOnPointsTo = this.mayRelyOnPtsTo();
				if (mayRelyOnPointsTo) {
					if (pointsToMayBeStale() || Program.memoizeAccesses > 0) {
						writeList = CellAccessGetter.getWrites(this.getNode());
					}
				}
			}
			return this.writeList;
		} else if (Misc.isCFGNonLeafNode(this.getNode())) {
			CellList nonLeafWriteList = new CellList();
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				CellList leafWriteList = leafContent.getInfo().getWrites();
				nonLeafWriteList.addAll(leafWriteList);
			}
			return nonLeafWriteList;
		} else {
			return CellAccessGetter.getWrites(this.getNode());
		}
	}

	public boolean mayWrite() {
		return CellAccessGetter.mayWrite(this.getNode());
	}

	/**
	 * Checks whether this node (or its corresponding CFG node) may write to a cell
	 * of pointer-type.
	 *
	 * @return true, if this node (or its corresponding CFG node) may write to a
	 *         cell of pointer-type.
	 */
	public boolean mayUpdatePointsTo() {
		return CellAccessGetter.mayUpdatePointsTo(this.getNode());
	}

	/**
	 * Returns a set of all the shared cells that may be read in the execution of
	 * this node (and the called methods, if
	 * any). Note that this method also considers those accesses that may happen in
	 * nodes that are not lexically inside
	 * the owner node, and those that may be reachable via some jumps (not just
	 * calls).
	 * <br>
	 * Note that this field is used only for CFG leaf nodes. Once we add
	 * {@link Predicate} to appropriate places in the
	 * production rules, we can create appropriate interfaces for CFG leaf and
	 * non-leaf nodes, and add this field in the
	 * corresponding CFG nodes.
	 *
	 * @return set of all those shared cells that may be read in the execution of
	 *         this node.
	 */
	public CellSet getSharedReads() {
		if (Misc.isCFGLeafNode(this.getNode())) {
			/*
			 * If the pointer analysis is invalid, stabilize it first; while
			 * running the pointer analysis, make sure that the memoization is
			 * turned off.
			 */
			if (this.sharedReadSet == null) {
				sharedReadSet = CellAccessGetter.getSharedReads(this.getNode());
			} else {
				boolean mayRelyOnPointsTo = this.mayRelyOnPtsTo();
				if (mayRelyOnPointsTo) {
					if (pointsToMayBeStale() || Program.memoizeAccesses > 0) {
						sharedReadSet = CellAccessGetter.getSharedReads(this.getNode());
					}
				}
			}
			return this.sharedReadSet;
		} else if (Misc.isCFGNonLeafNode(this.getNode())) {
			CellSet nonLeafSharedReadList = new CellSet();
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				CellSet leafSharedReadList = leafContent.getInfo().getSharedReads();
				nonLeafSharedReadList.addAll(leafSharedReadList);
			}
			return nonLeafSharedReadList;
		} else {
			return CellAccessGetter.getSharedReads(this.getNode());
		}
		// Old Code:
		// CellSet symbolSet = new HashSet<>();
		// for (Cell tempSym : this.getReads()) {
		// if (this.getSharingAttribute(tempSym) == DataSharingAttribute.SHARED) {
		// symbolSet.add(tempSym);
		// }
		// }
		// return symbolSet;
	}

	/**
	 * Returns a set of all the shared cells that may be written in the execution of
	 * this node (and the called methods,
	 * if any). Note that this method also considers those accesses that may happen
	 * in nodes that are not lexically
	 * inside the owner node, and those that may be reachable via some jumps (not
	 * just calls).
	 * <br>
	 * Note that this field is used only for CFG leaf nodes. Once we add
	 * {@link Predicate} to appropriate places in the
	 * production rules, we can create appropriate interfaces for CFG leaf and
	 * non-leaf nodes, and add this field in the
	 * corresponding CFG nodes.
	 *
	 * @return set of all those shared cells that may be written in the execution of
	 *         this node.
	 */
	public CellSet getSharedWrites() {
		if (Misc.isCFGLeafNode(this.getNode())) {
			/*
			 * If the pointer analysis is invalid, stabilize it first; while
			 * running the pointer analysis, make sure that the memoization is
			 * turned off.
			 */
			if (this.sharedWriteSet == null) {
				sharedWriteSet = CellAccessGetter.getSharedWrites(this.getNode());
			} else {
				boolean mayRelyOnPointsTo = this.mayRelyOnPtsTo();
				if (mayRelyOnPointsTo) {
					if (pointsToMayBeStale() || Program.memoizeAccesses > 0) {
						sharedWriteSet = CellAccessGetter.getSharedWrites(this.getNode());
					}
				}
			}
			return this.sharedWriteSet;
		} else if (Misc.isCFGNonLeafNode(this.getNode())) {
			CellSet nonLeafSharedWriteList = new CellSet();
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				CellSet leafSharedWriteList = leafContent.getInfo().getSharedWrites();
				nonLeafSharedWriteList.addAll(leafSharedWriteList);
			}
			return nonLeafSharedWriteList;
		} else {
			return CellAccessGetter.getSharedWrites(this.getNode());
		}
	}

	/**
	 * Returns a set of all the symbols that may be read in the execution of this
	 * node (and the called methods, if any),
	 * excluding the nodes that are encountered after the control leaves this node
	 * from any exit-point.
	 *
	 * @return set of all those symbols that may be read between the entry to and
	 *         exit from this node.
	 */
	public CellSet getSymbolReads() {
		CellList cellList = this.readList;
		if (Misc.isCFGLeafNode(this.getNode())) {
			if (cellList == null) {
				cellList = CellAccessGetter.getSymbolReads(this.getNode());
			} else {
				boolean mayRelyOnPointsTo = this.mayRelyOnPtsToForSymbols();
				if (mayRelyOnPointsTo) {
					if (pointsToMayBeStale() || Program.memoizeAccesses > 0) {
						cellList = CellAccessGetter.getSymbolReads(this.getNode());
					}
				}
			}
		} else if (Misc.isCFGNonLeafNode(this.getNode())) {
			cellList = new CellList();
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				CellSet leafSymbolReadList = leafContent.getInfo().getSymbolReads();
				cellList.addAll(leafSymbolReadList);
			}
		} else {
			cellList = CellAccessGetter.getSymbolReads(this.getNode());
		}
		CellSet symbolSet = new CellSet();
		for (Cell cell : cellList) {
			if (cell instanceof Symbol) {
				symbolSet.add(cell);
			}
		}
		return symbolSet;
	}

	/**
	 * Returns a set of all the symbols that may be written in the execution of this
	 * node (and the called methods, if
	 * any), excluding the nodes that are encountered after the control leaves this
	 * node from any exit-point.
	 *
	 * @return set of all those symbols that may be written between the entry to and
	 *         exit from this node.
	 */
	public CellSet getSymbolWrites() {
		CellList cellList = this.writeList;
		if (Misc.isCFGLeafNode(this.getNode())) {
			if (cellList == null) {
				cellList = CellAccessGetter.getSymbolWrites(this.getNode());
			} else {
				boolean mayRelyOnPointsTo = this.mayRelyOnPtsToForSymbols();
				if (mayRelyOnPointsTo) {
					if (pointsToMayBeStale() || Program.memoizeAccesses > 0) {
						cellList = CellAccessGetter.getSymbolWrites(this.getNode());
					}
				}
			}
		} else if (Misc.isCFGNonLeafNode(this.getNode())) {
			cellList = new CellList();
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				CellSet leafSymbolWriteList = leafContent.getInfo().getSymbolWrites();
				cellList.addAll(leafSymbolWriteList);
			}
		} else {
			cellList = CellAccessGetter.getSymbolWrites(this.getNode());
		}
		CellSet symbolSet = new CellSet();
		for (Cell cell : cellList) {
			if (cell instanceof Symbol) {
				symbolSet.add(cell);
			}
		}
		return symbolSet;
	}

	public CellSet getSymbolAccesses() {
		CellSet retSet = new CellSet();
		retSet.addAll(this.getSymbolReads());
		retSet.addAll(this.getSymbolWrites());
		return retSet;
	}

	/**
	 * Returns a set of all the shared cells that may be read in the execution of
	 * this node (and the called methods, if
	 * any), excluding all the field cells. Note that this method also considers
	 * those accesses that may happen in nodes
	 * that are not lexically inside the owner node, and those that may be reachable
	 * via some jumps (not just calls).
	 * <br>
	 * Note that this field is used only for CFG leaf nodes. Once we add
	 * {@link Predicate} to appropriate places in the
	 * production rules, we can create appropriate interfaces for CFG leaf and
	 * non-leaf nodes, and add this field in the
	 * corresponding CFG nodes.
	 *
	 * @return set of all those shared cells that may be read in the execution of
	 *         this node, excluding all the field
	 *         cells.
	 */
	public CellSet getNonFieldSharedReads() {
		if (Misc.isCFGLeafNode(this.getNode())) {
			/*
			 * If the pointer analysis is invalid, stabilize it first; while
			 * running the pointer analysis, make sure that the memoization is
			 * turned off.
			 */
			boolean update = false;
			if (this.nonFieldReadSet == null) {
				update = true;
			} else {
				boolean mayRelyOnPointsTo = this.mayRelyOnPtsTo();
				if (mayRelyOnPointsTo) {
					if (pointsToMayBeStale() || Program.memoizeAccesses > 0) {
						update = true;
					}
				}
			}
			if (update) {
				CellSet reads = this.getSharedReads();
				if (reads.isUniversal()) {
					this.nonFieldReadSet = reads;
				} else if (!reads.getReadOnlyInternal().stream()
						.anyMatch(c -> c instanceof FieldCell || c instanceof HeapCell)) {
					this.nonFieldReadSet = reads;
				} else {
					this.nonFieldReadSet = new CellSet();
					for (Cell cell : reads) {
						if (cell instanceof FieldCell || cell instanceof HeapCell) {
							continue;
						}
						this.nonFieldReadSet.add(cell);
					}
				}

			}
			return this.nonFieldReadSet;
		} else {
			CellSet reads = this.getSharedReads();
			CellSet newSet;
			if (reads.isUniversal()) {
				newSet = reads;
			} else if (!reads.getReadOnlyInternal().stream()
					.anyMatch(c -> c instanceof FieldCell || c instanceof HeapCell)) {
				newSet = reads;
			} else {
				newSet = new CellSet();
				for (Cell cell : reads) {
					if (cell instanceof FieldCell || cell instanceof HeapCell) {
						continue;
					}
					newSet.add(cell);
				}
			}
			return newSet;
		}
	}

	/**
	 * Returns a set of all the shared cells that may be written in the execution of
	 * this node (and the called methods,
	 * if any), excluding all the field cells. Note that this method also considers
	 * those accesses that may happen in
	 * nodes that are not lexically inside the owner node, and those that may be
	 * reachable via some jumps (not just
	 * calls).
	 * <br>
	 * Note that this field is used only for CFG leaf nodes. Once we add
	 * {@link Predicate} to appropriate places in the
	 * production rules, we can create appropriate interfaces for CFG leaf and
	 * non-leaf nodes, and add this field in the
	 * corresponding CFG nodes.
	 *
	 * @return set of all those shared cells that may be written in the execution of
	 *         this node, excluding all the field
	 *         cells.
	 */
	public CellSet getNonFieldSharedWrites() {
		if (Misc.isCFGLeafNode(this.getNode())) {
			/*
			 * If the pointer analysis is invalid, stabilize it first; while
			 * running the pointer analysis, make sure that the memoization is
			 * turned off.
			 */
			boolean update = false;
			if (this.nonFieldWriteSet == null) {
				update = true;
			} else {
				boolean mayRelyOnPointsTo = this.mayRelyOnPtsTo();
				if (mayRelyOnPointsTo) {
					if (pointsToMayBeStale() || Program.memoizeAccesses > 0) {
						update = true;
					}
				}
			}
			if (update) {
				CellSet writes = this.getSharedWrites();
				if (writes.isUniversal()) {
					this.nonFieldWriteSet = writes;
				} else if (!writes.getReadOnlyInternal().stream()
						.anyMatch(c -> c instanceof FieldCell || c instanceof HeapCell)) {
					this.nonFieldWriteSet = writes;
				} else {
					this.nonFieldWriteSet = new CellSet();
					for (Cell cell : writes) {
						if (cell instanceof FieldCell || cell instanceof HeapCell) {
							continue;
						}
						this.nonFieldWriteSet.add(cell);
					}
				}
			}
			return this.nonFieldWriteSet;
		} else {
			CellSet writes = this.getSharedWrites();
			CellSet newSet;
			if (writes.isUniversal()) {
				newSet = writes;
			} else if (!writes.getReadOnlyInternal().stream()
					.anyMatch(c -> c instanceof FieldCell || c instanceof HeapCell)) {
				newSet = writes;
			} else {
				newSet = new CellSet();
				for (Cell cell : writes) {
					if (cell instanceof FieldCell || cell instanceof HeapCell) {
						continue;
					}
					newSet.add(cell);
				}
			}
			return newSet;
		}
	}

	public void invalidateAccessLists() {
		this.readList = null;
		this.writeList = null;
		this.sharedReadSet = null;
		this.sharedWriteSet = null;
		this.nonFieldReadSet = null;
		this.nonFieldWriteSet = null;
	}

	public CellSet getAccesses() {
		CellSet accessSet = new CellSet();
		accessSet.addAll(this.getReads());
		accessSet.addAll(this.getWrites());
		return accessSet;
	}

	/**
	 * Obtain the list of access-expressions (composed up of {@link BaseSyntax} and
	 * List{@literal <}{@link
	 * Tokenizable}{@literal >}) representing array dereferences that may be read
	 * anywhere in this node.
	 *
	 * @return list of access-expressions (composed up of {@link BaseSyntax} and
	 *         List{@literal <}{@link
	 *         Tokenizable}{@literal >}) representing array dereferences that may be
	 *         read anywhere in this node.
	 */
	public List<SyntacticAccessExpression> getBaseAccessExpressionReads() {
		Node node = this.getNode();
		if (node instanceof Expression || Misc.isCFGLeafNode(node)) {
			// Use the cached information for Expression and leaf nodes.
			if (baseAccessExpressionReads == null) {
				baseAccessExpressionReads = SyntacticAccessExpressionGetter.getBaseAccessExpressionReads(node);
			}
			return baseAccessExpressionReads;
		} else {
			return SyntacticAccessExpressionGetter.getBaseAccessExpressionReads(node);
		}
	}

	/**
	 * Obtain the list of access-expressions (composed up of {@link BaseSyntax} and
	 * List{@literal <}{@link
	 * Tokenizable}{@literal >}) representing array dereferences that may be written
	 * anywhere in this node.
	 *
	 * @return list of access-expressions (composed up of {@link BaseSyntax} and
	 *         List{@literal <}{@link
	 *         Tokenizable}{@literal >}) representing array dereferences that may be
	 *         written anywhere in this node.
	 */
	public List<SyntacticAccessExpression> getBaseAccessExpressionWrites() {
		Node node = this.getNode();
		if (node instanceof Expression || Misc.isCFGLeafNode(node)) {
			// Use the cached information for Expression and leaf nodes.
			if (baseAccessExpressionWrites == null) {
				baseAccessExpressionWrites = SyntacticAccessExpressionGetter.getBaseAccessExpressionWrites(node);
			}
			return baseAccessExpressionWrites;
		} else {
			return SyntacticAccessExpressionGetter.getBaseAccessExpressionWrites(node);
		}
	}

	/**
	 * Obtain a list of all the {@link CastExpression} expressions on which
	 * dereference may have been made using an
	 * asterisk ({@code *}), and whose dereferenced location may have been read
	 * within this node.
	 * <br>
	 * For example, in {@code ... = *(e1);}, this method returns a set containing
	 * {@code e1}.
	 *
	 * @return list of all the {@link CastExpression} expressions on which
	 *         dereference may have been made using an
	 *         asterisk ({@code *}), and whose dereferenced location may have been
	 *         read within this node.
	 */
	public List<CastExpression> getPointerDereferencedExpressionReads() {
		Node node = this.getNode();
		if (node instanceof Expression || Misc.isCFGLeafNode(node)) {
			if (pointerDereferencedExpressionReads == null) {
				pointerDereferencedExpressionReads = PointerDereferenceGetter
						.getPointerDereferencedCastExpressionReads(node);
			}
			return pointerDereferencedExpressionReads;
		} else {
			return PointerDereferenceGetter.getPointerDereferencedCastExpressionReads(node);
		}
	}

	/**
	 * Obtain a list of all the {@link CastExpression} expressions on which
	 * dereference may have been made using an
	 * asterisk ({@code *}), and whose dereferenced location may have been written
	 * within this node.
	 * <br>
	 * For example, in {@code *(e1) = ...;}, this method returns a set containing
	 * {@code e1}.
	 *
	 * @return list of all the {@link CastExpression} expressions on which
	 *         dereference may have been made using an
	 *         asterisk ({@code *}), and whose dereferenced location may have been
	 *         written within this node.
	 */
	public List<CastExpression> getPointerDereferencedExpressionWrites() {
		Node node = this.getNode();
		if (node instanceof Expression || Misc.isCFGLeafNode(node)) {
			if (pointerDereferencedExpressionWrites == null) {
				pointerDereferencedExpressionWrites = PointerDereferenceGetter
						.getPointerDereferencedCastExpressionWrites(node);
			}
			return pointerDereferencedExpressionWrites;
		} else {
			return PointerDereferenceGetter.getPointerDereferencedCastExpressionWrites(node);
		}
	}

	public CellSet getInternalAccesses() {
		CellSet internalAccessSet = new CellSet();
		internalAccessSet.addAll(this.getReads());
		internalAccessSet.addAll(this.getWrites());
		return internalAccessSet;
	}

	public CellSet getUsedCells() {
		return UsedCellsGetter.getUsedCells(this.getNode());
	}

	public Set<Type> getUsedTypes() {
		Node baseNode = this.getNode();
		Set<Type> usedTypes = new HashSet<>();
		for (Scopeable scopeNode : baseNode.getInfo().getLexicallyEnclosedScopesInclusive()) {
			if (scopeNode instanceof TranslationUnit) {
				TranslationUnit scope = (TranslationUnit) scopeNode;
				for (Symbol sym : scope.getInfo().getSymbolTable().values()) {
					Type symType = sym.getType();
					if (symType instanceof StructType || symType instanceof UnionType || symType instanceof EnumType) {
						usedTypes.addAll(symType.getAllTypes());
					}
				}
				for (Typedef tDef : scope.getInfo().getTypedefTable().values()) {
					Type tDefType = tDef.getType();
					if (tDefType instanceof StructType || tDefType instanceof UnionType
							|| tDefType instanceof EnumType) {
						usedTypes.addAll(tDefType.getAllTypes());
					}
					// OLD CODE: This would have ignored the types of members of user-defined types.
					// usedTypes.add(tDef.getType());
				}
			} else if (scopeNode instanceof FunctionDefinition) {
				FunctionDefinition scope = (FunctionDefinition) scopeNode;
				for (Symbol sym : scope.getInfo().getSymbolTable().values()) {
					Type symType = sym.getType();
					if (symType instanceof StructType || symType instanceof UnionType || symType instanceof EnumType) {
						usedTypes.addAll(symType.getAllTypes());
					}
				}
				usedTypes.addAll(scope.getInfo().getReturnType().getAllTypes());
			} else if (scopeNode instanceof CompoundStatement) {
				CompoundStatement scope = (CompoundStatement) scopeNode;
				for (Symbol sym : scope.getInfo().getSymbolTable().values()) {
					Type symType = sym.getType();
					if (symType instanceof StructType || symType instanceof UnionType || symType instanceof EnumType) {
						usedTypes.addAll(symType.getAllTypes());
					}
				}
				for (Typedef tDef : scope.getInfo().getTypedefTable().values()) {
					Type tDefType = tDef.getType();
					if (tDefType instanceof StructType || tDefType instanceof UnionType
							|| tDefType instanceof EnumType) {
						usedTypes.addAll(tDefType.getAllTypes());
					}
					// OLD CODE: This would have ignored the types of members of user-defined types.
					// usedTypes.add(tDef.getType());
				}
			}
		}
		UsedTypeGetter utg = new UsedTypeGetter();
		this.getNode().accept(utg);
		usedTypes.addAll(utg.usedTypes);
		return usedTypes;
	}

	public Set<Typedef> getUsedTypedefs() {
		UsedTypedefGetter utg = new UsedTypedefGetter();
		this.getNode().accept(utg);
		return utg.usedTypedefs;
	}

	/**
	 * Replaces all the occurrences of {@link Symbol} having names in
	 * {@code freeNames} by {@link FreeVariable}, for the
	 * {@link #readList} and {@link #writeList} of the owner leaf node.
	 *
	 * @param freeNames
	 *                  list of identifiers that refer to free variables in the
	 *                  owner leaf node.
	 */
	public void makeSymbolsFreeInRWList(Set<String> freeNames) {
		Node node = this.getNode();
		if (Misc.isCFGLeafNode(node)) {
			this.writeList.replaceAll((x) -> {
				if (x instanceof Symbol) {
					Symbol sym = (Symbol) x;
					if (freeNames.contains(sym.getName())) {
						return new FreeVariable(sym.getName());
					} else {
						return x;
					}
				} else {
					return x;
				}
			});
			this.readList.replaceAll((x) -> {
				if (x instanceof Symbol) {
					Symbol sym = (Symbol) x;
					if (freeNames.contains(sym.getName())) {
						return new FreeVariable(sym.getName());
					} else {
						return x;
					}
				} else {
					return x;
				}
			});

			// Old code: Commented out after changing the type of sharedReadSet and
			// sharedWriteSet.
			// this.sharedReadSet.replaceAll((x) -> {
			// if (x instanceof Symbol) {
			// Symbol sym = (Symbol) x;
			// if (freeNames.contains(sym.getName())) {
			// return new FreeVariable(sym.getName());
			// } else {
			// return x;
			// }
			// } else {
			// return x;
			// }
			// });
			// this.sharedWriteSet.replaceAll((x) -> {
			// if (x instanceof Symbol) {
			// Symbol sym = (Symbol) x;
			// if (freeNames.contains(sym.getName())) {
			// return new FreeVariable(sym.getName());
			// } else {
			// return x;
			// }
			// } else {
			// return x;
			// }
			// });
		} else {
			Misc.warnDueToLackOfFeature("Handling free variables in the read/write lists for non-leaf/non-CFG nodes.",
					this.getNode());
			return;
		}
	}

	public CellSet getSharedAccesses() {
		return Misc.setUnion(this.getSharedReads(), this.getSharedWrites());
	}

	/**
	 * Checks if this node is a self-update node -- one that may read and write to
	 * the same location.
	 *
	 * @return true, if this CFG node is a node that reads as well as writes to the
	 *         same cell.
	 */
	public boolean isUpdateNode() {
		return Misc.doIntersect(this.getReads(), this.getWrites());
	}

	/**
	 * Returns a set of all those cells that may be accessible at the given node,
	 * exclusively -- i.e., if this node is a
	 * {@link Scopeable} object, then we do not include the set of those cells that
	 * are created within this scope.
	 *
	 * @return a set of all those cells that may be accessible at the given node,
	 *         exclusively.
	 */
	public CellSet getAllCellsAtNodeExclusively() {
		if (this.getNode() instanceof Scopeable) {
			Node encloser = this.getNode().getParent();
			if (encloser == null) {
				return new CellSet();
			}
			return encloser.getInfo().getAllCellsAtNode();
		} else {
			return this.getAllCellsAtNode();
		}
	}

	/**
	 * Returns the set of symbol names that are accessible at this node, exclusively
	 * -- i.e., if this node is a {@link
	 * Scopeable} object, then the names from the symbol table of this object are
	 * not added to the return set.
	 *
	 * @return set of symbol names that are accessible at this node, exclusively.
	 */
	public Set<String> getAllSymbolNamesAtNodeExclusively() {
		Set<String> cellNames = new HashSet<>();
		CellSet cellSet = this.getAllCellsAtNodeExclusively();
		if (cellSet.isUniversal()) {
			return cellNames;
		} else {
			cellSet.applyAllExpanded(c -> {
				if (c instanceof Symbol) {
					Symbol sym = (Symbol) c;
					cellNames.add(sym.getName());
				}
			});
		}
		return cellNames;
	}

	/**
	 * Returns the set of symbols that are accessible at this node (inclusively).
	 * <p>
	 * TODO: Add points-to closure of the symbols to obtain all reachable cells.
	 *
	 * @param node
	 *
	 * @return
	 */
	public CellSet getAllCellsAtNode() {
		Node encloser = (Node) Misc.getEnclosingBlock(this.getNode());
		if (encloser == null) {
			return new CellSet();
		}
		return encloser.getInfo().getAllCellsAtNode();
		// Set<Symbol> symbolSet = new HashSet<>();
		// Node scopeChoice = getNode();
		// while (scopeChoice != null) {
		// if (scopeChoice instanceof TranslationUnit) {
		// for (Symbol sym : ((RootInfo)
		// scopeChoice.getInfo()).getSymbolTable().values()) {
		// if (sym.isAVariable()) {
		// symbolSet.add(sym);
		// }
		// }
		// } else if (scopeChoice instanceof FunctionDefinition) {
		// for (Symbol sym : ((FunctionDefinitionInfo)
		// scopeChoice.getInfo()).getSymbolTable().values()) {
		// if (sym.isAVariable()) {
		// symbolSet.add(sym);
		// }
		// }
		// } else if (scopeChoice instanceof CompoundStatement) {
		// for (Symbol sym : ((CompoundStatementInfo)
		// scopeChoice.getInfo()).getSymbolTable().values()) {
		// if (sym.isAVariable()) {
		// symbolSet.add(sym);
		// }
		// }
		// }
		// scopeChoice = (Node) Misc.getEnclosingBlock(scopeChoice);
		// }
		// return symbolSet;
	}

	public Set<String> getAllSymbolNamesAtNodes() {
		Set<String> cellNames = new HashSet<>();
		CellSet cellSet = this.getAllCellsAtNode();
		if (cellSet.isUniversal()) {
			return cellNames;
		} else {
			for (Cell cell : cellSet) {
				if (cell instanceof Symbol) {
					Symbol sym = (Symbol) cell;
					cellNames.add(sym.getName());
				}
			}
		}
		return cellNames;
	}

	public CellSet getSharedCellsAtNode() {
		Node encloser = (Node) Misc.getEnclosingBlock(this.getNode());
		if (encloser == null) {
			return new CellSet();
		}
		return encloser.getInfo().getSharedCellsAtNode();
		// CellSet retSet = new HashSet<>();
		// for (Symbol sym : this.getAllCellsAtNode()) {
		// if (this.getSharingAttribute(sym) == DataSharingAttribute.SHARED) {
		// retSet.add(sym);
		// }
		// }
		// return retSet;
	}

	public List<Typedef> getAllTypedefListAtNode() {
		Node encloser = (Node) Misc.getEnclosingBlock(this.getNode());
		if (encloser == null) {
			return new ArrayList<>();
		}
		return encloser.getInfo().getAllTypedefListAtNode();
	}

	public Set<String> getAllTypedefNameListAtNode() {
		Set<String> typedefNameList = new HashSet<>();
		for (Typedef td : this.getAllTypedefListAtNode()) {
			typedefNameList.add(td.getTypedefName());
		}
		return typedefNameList;
	}

	public List<Type> getAllTypeListAtNode() {
		Node encloser = (Node) Misc.getEnclosingBlock(this.getNode());
		if (encloser == null) {
			return new ArrayList<>();
		}
		return encloser.getInfo().getAllTypeListAtNode();
	}

	public Set<String> getAllTypeNameListAtNode() {
		List<Type> typeAtStmt = this.getAllTypeListAtNode();
		Set<String> typeNameAtStmt = new HashSet<>();
		for (Type t : typeAtStmt) {
			if (t instanceof StructType) {
				typeNameAtStmt.add(((StructType) t).getName());
			} else if (t instanceof UnionType) {
				typeNameAtStmt.add(((UnionType) t).getTag());
			} else if (t instanceof EnumType) {
				typeNameAtStmt.add(((EnumType) t).getTag());
			}
		}
		return typeNameAtStmt;
	}

	public boolean hasBarrierInAST() {
		BarrierGetter barGetter = new BarrierGetter();
		getNode().accept(barGetter);
		if (barGetter.barrierList.size() != 0) {
			return true;
		}
		return false;
	}

	public boolean hasBarrierInCFG() {
		if (this.hasBarrierInAST()) {
			return true;
		}
		for (CallStatement cS : this.getLexicallyEnclosedCallStatements()) {
			for (FunctionDefinition funcDef : cS.getInfo().getCalledDefinitions()) {
				Set<FunctionDefinition> inProcessFDSet = new HashSet<>();
				inProcessFDSet.add(funcDef);
				if (funcDef.getInfo().hasBarrierInCFGVisited(inProcessFDSet)) {
					return true;
				}
				// Old:
				// if (funcDef.getInfo().hasBarrierInCFG()) {
				// return true;
				// }
			}
		}
		return false;
		// for (CallSite cS : getCallSites()) {
		// if (cS.calleeDefinition == null) {
		// continue;
		// }
		// FunctionDefinitionInfo funcInfo = cS.calleeDefinition.getInfo();
		// if (funcInfo.hasBarrierInCFG()) {
		// return true;
		// }
		// }
	}

	protected boolean hasBarrierInCFGVisited(Set<FunctionDefinition> inProcessFDSet) {
		if (this.hasBarrierInAST()) {
			return true;
		}
		for (CallStatement cS : this.getLexicallyEnclosedCallStatements()) {
			for (FunctionDefinition funcDef : cS.getInfo().getCalledDefinitions()) {
				if (inProcessFDSet.contains(funcDef)) {
					continue;
				}
				inProcessFDSet.add(funcDef);
				if (funcDef.getInfo().hasBarrierInCFGVisited(inProcessFDSet)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean removeLexicallyEnclosedCallStatement(CallStatement callStmt) {
		Node node = this.getNode();
		if (!Misc.isCFGNode(node)) {
			return false;
		}
		if (callStatements == null) {
			return false;
		}
		return callStatements.removeIf(c -> c == callStmt);
	}

	/**
	 * Returns a list of call-statements within the given non-leaf/leaf node.
	 *
	 * @return
	 */
	public List<CallStatement> getLexicallyEnclosedCallStatements() {
		Node node = this.getNode();
		long thisTimer = System.nanoTime();
		if (Misc.isCFGNode(node)) {
			if (callStatements == null) {
				callStatements = new LinkedList<>();
				for (CallStatement expNode : Misc.getInheritedEnclosee(getNode(), CallStatement.class)) {
					callStatements.add(expNode);
				}
			}
			NodeInfo.callQueryTimer += (System.nanoTime() - thisTimer);
			return callStatements;
		} else if (node instanceof TranslationUnit) {
			List<CallStatement> nonCachedCalls = new LinkedList<>();
			RootInfo rootInfo = (RootInfo) node.getInfo();
			for (FunctionDefinition func : rootInfo.getAllFunctionDefinitions()) {
				nonCachedCalls.addAll(func.getInfo().getLexicallyEnclosedCallStatements());
			}
			NodeInfo.callQueryTimer += (System.nanoTime() - thisTimer);
			return nonCachedCalls;
		} else {
			List<CallStatement> nonCachedCalls = new LinkedList<>();
			for (Node cfgContent : node.getInfo().getCFGInfo().getLexicalCFGContents()) {
				if (cfgContent instanceof CallStatement) {
					nonCachedCalls.add((CallStatement) cfgContent);
				}
			}
			NodeInfo.callQueryTimer += (System.nanoTime() - thisTimer);
			return nonCachedCalls;
		}
	}

	public boolean hasLexicallyEnclosedCallStatements() {
		this.getLexicallyEnclosedCallStatements();
		if (callStatements.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the set of all those function-definitions (including the current node
	 * itself, if it's a
	 * function-definition), that may get executed during the processing of this
	 * node.
	 *
	 * @return set of all those function-definitions that may get executed during
	 *         the execution of this node (including
	 *         the node itself, if it's a function-definition).
	 */
	public Set<FunctionDefinition> getReachableCallGraphNodes() {
		Set<FunctionDefinition> returnSet = new HashSet<>();
		Node baseNode = this.getNode();
		if (baseNode instanceof FunctionDefinition) {
			returnSet.add((FunctionDefinition) baseNode);
		}
		Set<FunctionDefinition> endSet = new HashSet<>();
		Set<FunctionDefinition> startSet = this.getCallGraphSuccessors();
		returnSet.addAll(CollectorVisitor.collectNodeSetInGenericGraph(startSet, endSet, (f) -> false,
				(f) -> f.getInfo().getCallGraphSuccessors()));
		returnSet.addAll(startSet);
		returnSet.addAll(endSet);
		return returnSet;
	}

	/**
	 * Obtain a set of all those function-definitions that may be the target of any
	 * call-statement that is lexically
	 * enclosed within the owner node.
	 *
	 * @return set of all those function-definitions that may be the target of any
	 *         call-statement that is lexically
	 *         enclosed within the owner node.
	 */
	public Set<FunctionDefinition> getCallGraphSuccessors() {
		Set<FunctionDefinition> calledFDs = new HashSet<>();
		for (CallStatement cs : this.getLexicallyEnclosedCallStatements()) {
			calledFDs.addAll(cs.getInfo().getCalledDefinitions());
		}
		return calledFDs;
	}

	public Set<CallStatement> getReachableCallStatementsInclusive() {
		Set<CallStatement> callStmts = new HashSet<>();
		Set<CallStatement> startSet = new HashSet<>(this.getLexicallyEnclosedCallStatements());
		Set<CallStatement> endSet = new HashSet<>();
		callStmts.addAll(CollectorVisitor.collectNodeSetInGenericGraph(startSet, endSet, (cs) -> false, (cs) -> {
			Set<CallStatement> nextSet = new HashSet<>();
			for (FunctionDefinition fd : cs.getInfo().getCalledDefinitions()) {
				nextSet.addAll(fd.getInfo().getLexicallyEnclosedCallStatements());
			}
			return nextSet;
		}));
		callStmts.addAll(startSet);
		callStmts.addAll(endSet);
		return callStmts;
	}

	/**
	 * Returns the outermost enclosing node for the owner node.
	 *
	 * @return the outermost enclosing node for a given node.
	 */
	public Node getOuterMostEncloser() {
		List<Node> outerList = (CollectorVisitor.collectNodeListInGenericGraph(this.node, null, Objects::isNull,
				(n) -> {
					List<Node> neighbourSet = new LinkedList<>();
					neighbourSet.add(n.getParent());
					return neighbourSet;
				}));
		Node node = outerList.get(outerList.size() - 1);
		return node;
	}

	/**
	 * Returns the outermost enclosing non-leaf node for the owner node
	 * (exclusively).
	 *
	 * @return the outermost enclosing non-leaf node for a given node (exclusively).
	 */
	public Node getOuterMostNonLeafEncloser() {
		List<Node> nonLeafEnclosingPathExclusive = this.node.getInfo().getNonLeafNestingPathExclusive();
		if (nonLeafEnclosingPathExclusive.isEmpty()) {
			return null;
		} else {
			return nonLeafEnclosingPathExclusive.get(nonLeafEnclosingPathExclusive.size() - 1);
		}
	}

	public Scopeable getOuterMostScopeExclusive() {
		List<Node> nonLeafEnclosingPathExclusive = this.node.getInfo().getNonLeafNestingPathExclusive();
		nonLeafEnclosingPathExclusive = Misc.reverseList(nonLeafEnclosingPathExclusive);
		for (Node n : nonLeafEnclosingPathExclusive) {
			if (n instanceof Scopeable) {
				return (Scopeable) n;
			}
		}
		return null;
	}

	/**
	 * Obtain a set of all the non-leaf nodes that lexically enclose the node
	 * {@code node}.
	 *
	 * @param node
	 *             node of whose all non-leaf enclosers have to be found.
	 *
	 * @return a set of all those non-leaf nodes that lexically enclose the node
	 *         {@code node}
	 */
	public Set<Node> getAllLexicalNonLeafEnclosersExclusive() {
		Set<Node> allEnclosers = CollectorVisitor.collectNodeSetInGenericGraph(this.node, null, (n) -> (n == null),
				(n) -> {
					Set<Node> neighbourSet = new HashSet<>();
					Node encloser = Misc.getEnclosingCFGNonLeafNode(n);
					if (encloser != null) {
						neighbourSet.add(encloser);
					}
					return neighbourSet;
				});
		return allEnclosers;
	}

	/**
	 * Returns the list of all the non-leaf nodes that enclose the owner node,
	 * starting with the immediately enclosing
	 * non-leaf encloser (exclusively). Note that root of the tree would be the last
	 * element.
	 *
	 * @return the list of all the non-leaf nodes that enclose {@code node},
	 *         starting with the immediately enclosing
	 *         non-leaf encloser (exclusively).
	 */
	public List<Node> getNonLeafNestingPathExclusive() {
		List<Node> nonLeafEnclosingPathExclusive = new LinkedList<>();
		nonLeafEnclosingPathExclusive = CollectorVisitor.collectNodeListInGenericGraph(this.node, null,
				(n) -> (n == null), (n) -> {
					List<Node> neighbourSet = new LinkedList<>();
					neighbourSet.add(Misc.getEnclosingCFGNonLeafNode(n));
					return neighbourSet;
				});
		return nonLeafEnclosingPathExclusive;
	}

	/**
	 * Get a set of all those non-leaf nodes that may enclose the given statement,
	 * including those that may reside in
	 * the callers of the function in which the given node exists.
	 *
	 * @return a set of all those non-leaf nodes that may enclose the given
	 *         statement, including those that may reside
	 *         in the callers of the function in which the given node exists.
	 */
	public Set<Node> getCFGNestingNonLeafNodes() {
		Set<Node> allEnclosers = CollectorVisitor.collectNodeSetInGenericGraph(this.node, null, (n) -> (n == null),
				(n) -> {
					Set<Node> neighbourSet = new HashSet<>();
					if (n instanceof FunctionDefinition) {
						FunctionDefinition func = (FunctionDefinition) n;
						for (CallStatement callSt : func.getInfo().getCallersOfThis()) {
							neighbourSet.add(callSt);
							Node encloser = Misc.getEnclosingCFGNonLeafNode(callSt);
							if (encloser != null) {
								neighbourSet.add(encloser);
							}
						}
					} else {
						Node encloser = Misc.getEnclosingCFGNonLeafNode(n);
						if (encloser != null) {
							neighbourSet.add(encloser);
						}
					}
					return neighbourSet;
				});
		return allEnclosers;
	}

	/**
	 * Find whether the control can flow in and out of this AST node except from the
	 * beginning and end of it,
	 * respectively.
	 *
	 * @return
	 */
	public boolean isControlConfined() {
		Set<Node> cfgContents = this.getCFGInfo().getLexicalCFGContents();

		for (Object labeledObject : cfgContents.stream()
				.filter(n -> (n instanceof Statement && ((Statement) n).getInfo().hasLabelAnnotations())).toArray()) {
			Statement labeledStmt = (Statement) labeledObject;
			for (Node pred : labeledStmt.getInfo().getCFGInfo().getPredecessors()) {
				if (!cfgContents.contains(pred)) {
					return false;
				}
			}
			if (labeledStmt.getInfo().getIncompleteSemantics().hasIncompleteEdges()) {
				return false;
			}
		}

		for (Node constituent : cfgContents) {
			if (constituent instanceof JumpStatement) {
				for (Node succ : constituent.getInfo().getCFGInfo().getSuccessors()) {
					if (!cfgContents.contains(succ)) {
						return false;
					}
				}
				if (constituent.getInfo().getIncompleteSemantics().hasIncompleteEdges()) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Returns a set of those nodes which are sources of (intra-procedural) edges
	 * that enter this node from somewhere
	 * apart from BeginNode.
	 *
	 * @return a set of those nodes which are sources of (intra-procedural) edges
	 *         that enter this node from somewhere
	 *         apart from BeginNode.
	 */
	public Set<Node> getInJumpSources() {
		Set<Node> inJumpSources = new HashSet<>();
		Set<Node> cfgContents = this.getCFGInfo().getLexicalCFGContents();

		for (Object labeledObject : cfgContents.stream()
				.filter(n -> (n instanceof Statement && ((Statement) n).getInfo().hasLabelAnnotations())).toArray()) {
			Statement labeledStmt = (Statement) labeledObject;
			for (Node pred : labeledStmt.getInfo().getCFGInfo().getPredecessors()) {
				if (!cfgContents.contains(pred)) {
					inJumpSources.add(pred);
				}
			}
		}
		return inJumpSources;
	}

	/**
	 * Returns a set of those nodes which are destinations of (intra-procedural)
	 * edges that enter this node from
	 * somewhere apart from BeginNode.
	 *
	 * @return a set of those nodes which are destinations of (intra-procedural)
	 *         edges that enter this node from
	 *         somewhere apart from BeginNode.
	 */
	public Set<Node> getInJumpDestinations() {
		Set<Node> inJumpDestinations = new HashSet<>();
		Set<Node> cfgContents = this.getCFGInfo().getLexicalCFGContents();
		for (Node ijs : this.getInJumpSources()) {
			for (Node succ : ijs.getInfo().getCFGInfo().getInterProceduralLeafSuccessors()) {
				if (cfgContents.contains(succ)) {
					inJumpDestinations.add(succ);
				}
			}
		}
		return inJumpDestinations;

	}

	/**
	 * Returns a set of those nodes which are destinations of (intra-procedural)
	 * edges that leave this node from
	 * somewhere apart from EndNode.
	 *
	 * @return a set of those nodes which are destinations of (intra-procedural)
	 *         edges that leave this node from
	 *         somewhere apart from EndNode.
	 */
	public Set<Node> getOutJumpDestinations() {
		Set<Node> outJumpDestinations = new HashSet<>();
		Set<Node> cfgContents = this.getCFGInfo().getLexicalCFGContents();

		for (Node constituent : cfgContents) {
			if (constituent instanceof JumpStatement) {
				for (Node succ : constituent.getInfo().getCFGInfo().getSuccessors()) {
					if (!cfgContents.contains(succ)) {
						outJumpDestinations.add(succ);
					}
				}
			}
		}
		return outJumpDestinations;
	}

	public Set<Node> getOutJumpSources() {
		Set<Node> outJumpSources = new HashSet<>();
		Set<Node> cfgContents = this.getCFGInfo().getLexicalCFGContents();
		for (Node ojd : this.getOutJumpDestinations()) {
			for (Node pred : ojd.getInfo().getCFGInfo().getInterProceduralLeafPredecessors()) {
				if (cfgContents.contains(pred)) {
					outJumpSources.add(pred);
				}
			}
		}
		return outJumpSources;
	}

	/**
	 * Returns the set of nodes from which control may flow inside this node.
	 *
	 * @return set of nodes from which control may flow inside this node.
	 */
	public Set<Node> getEntryNodes() {
		Set<Node> entryNodes = new HashSet<>();
		if (Misc.isCFGLeafNode(this.getNode())) {
			entryNodes.add(this.getNode());
		} else if (Misc.isCFGNonLeafNode(this.getNode())) {
			entryNodes.add(this.getCFGInfo().getNestedCFG().getBegin());
			entryNodes.addAll(this.getInJumpDestinations());
		} else {
			// Do nothing.
		}
		return entryNodes;
	}

	/**
	 * Returns the set of nodes from which control may flow outside this node.
	 *
	 * @return set of nodes from which control may flow outside this node.
	 */
	public Set<Node> getExitNodes() {
		Set<Node> exitNodes = new HashSet<>();
		if (Misc.isCFGLeafNode(this.getNode())) {
			exitNodes.add(this.getNode());
		} else if (Misc.isCFGNonLeafNode(this.getNode())) {
			exitNodes.add(this.getCFGInfo().getNestedCFG().getEnd());
			exitNodes.addAll(this.getOutJumpSources());
		} else {
			// Do nothing.
		}
		return exitNodes;
	}

	/**
	 * Removes the unnecessary scoping of statements within this node, without
	 * resorting to renaming of the nested
	 * variables.
	 * <p>
	 * Note: The pair of braces across each single-statement body is preserved.
	 *
	 * @param in
	 *           input node which might contain more braces than it needs.
	 */
	public void removeExtraScopes() {
		CompoundStatementNormalizer.removeExtraScopes(this.getNode());
	}

	public void printNode() {
		System.out.println(this.getString());
	}

	public String getString(List<Commentor> commentorList) {
		return StringGetter.getString(this.getNode(), commentorList);
		// StringGetter str = new StringGetter(commentorList);
		// getNode().accept(str);
		// return new String(str.str); //replaceAll("(?m)^[ \t]*\r?\n", "");
	}

	public String getString() {
		// Temporary code starts below.
		// List<Commentor> commentors = new ArrayList<>();
		// commentors.add((n) -> {
		// FlowFact ff = this.getIN(AnalysisName.COPYPROPAGATION);
		// if (ff != null) {
		// String tempStr = "[";
		// tempStr += ff;
		// tempStr += "]";
		// return tempStr;
		// } else {
		// return "";
		// }
		// });
		// return this.getString(commentors);
		// Temporary code ends above.

		return this.getString(new ArrayList<>());
	}

	public String getDebugString() {
		List<Commentor> commentors = new ArrayList<>();
		// commentors.add((n) -> {
		// String tempStr = "";
		// FlowFact flow = n.getInfo().getCurrentIN(AnalysisName.PREDICATE_ANALYSIS);
		// if (flow != null) {
		// tempStr += "IN: " + flow.getString();
		// }
		// flow = n.getInfo().getCurrentOUT(AnalysisName.PREDICATE_ANALYSIS);
		// if (flow != null) {
		// tempStr += "OUT: " + flow.getString();
		// }
		// return tempStr;
		// });
		// commentors.add((n) -> {
		// String tempStr = "";
		// FlowFact flow;
		// // flow = n.getInfo().getIN(AnalysisName.POINTSTO);
		// // if (flow != null) {
		// // tempStr += "IN: " + flow.getString();
		// // }
		// // tempStr += "\n";
		// flow = n.getInfo().getCurrentOUT(AnalysisName.POINTSTO);
		// if (flow != null) {
		// tempStr += "OUT: " + flow.getString();
		// }
		// return tempStr;
		// });
		commentors.add((n) -> {
			String tempStr = "[";
			for (AbstractPhase<?, ?> ph : n.getInfo().getNodePhaseInfo().getStalePhaseSet()) {
				tempStr += ph.getPhaseId() + "; ";
			}
			tempStr += "]";
			if (Program.mhpUpdateCategory == UpdateCategory.LZINV) {
				if (AbstractPhase.globalMHPStale) {
					tempStr += "(Stale)";
				}
			} else if (!BeginPhasePoint.getStaleBeginPhasePoints().isEmpty()) {
				tempStr += "(Stale)";
			}
			return tempStr;
		});
		Node node = this.getNode();
		if (!Misc.isCFGNode(node)) {
			return this.getString(commentors);
		}
		Node outerMostNonLeafEncloser = node.getInfo().getOuterMostNonLeafEncloser();
		if (outerMostNonLeafEncloser == null) {
			return this.getString(commentors);
		} else {
			int size = this.getComments().size();
			this.getComments().add("################################################");
			String tempStr = outerMostNonLeafEncloser.getInfo().getString(commentors);
			this.getComments().remove(size);
			return tempStr;
		}
	}

	/**
	 * Obtain an internal CFG node in owner {@code node}, that contains the label
	 * {@code l}.
	 *
	 * @param l
	 *          label that has to be searched for.
	 *
	 * @return CFG node that has a label-annotation of {@code l}; null, otherwise.
	 */
	public Statement getStatementWithLabel(String l) {
		for (Node tempNode : this.getCFGInfo().getLexicalCFGContents()) {
			if (tempNode instanceof Statement) {
				// && ((Statement)tempNode).getInfo().hasLabelAnnotations()) {
				for (Label tempLabel : ((Statement) tempNode).getInfo().getLabelAnnotations()) {
					if (tempLabel instanceof SimpleLabel) {
						if (((SimpleLabel) tempLabel).getLabelName().equals(l)) {
							return (Statement) tempNode;
						}
					}
				}
			}
		}
		return null;
	}

	public CellMap<Cell> getCopyMap() {
		CellMap<Cell> returnSet = new CellMap<>();
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		if (mainFunc == null) {
			Misc.warnDueToLackOfFeature("Cannot run copy propagation pass on a program without main().", null);
			return returnSet;
		}
		if (!FlowAnalysis.getAllAnalyses().keySet().contains(AnalysisName.COPYPROPAGATION)) {
			System.err.println("Pass: Copy propagation analysis.");
			long timeStart = System.nanoTime();
			CopyPropagationAnalysis cpa = new CopyPropagationAnalysis();
			cpa.run(mainFunc);
			long timeTaken = System.nanoTime() - timeStart;
			System.err.println("\tNodes processed " + cpa.nodesProcessed + " times.");
			System.err.println("\tTime taken: " + timeTaken / 1000000000.0 + "s.");
		}

		CopyPropagationFlowMap cpf = (CopyPropagationFlowMap) this.getIN(AnalysisName.COPYPROPAGATION);
		if (cpf == null) {
			return returnSet;
		}
		returnSet = new ExtensibleCellMap<>(cpf.flowMap);
		return returnSet;
	}

	public CellMap<Cell> getRelevantCopyMap() {
		CellMap<Cell> retMap = new CellMap<>();
		CellMap<Cell> fullMap = this.getCopyMap();
		CellSet relevantSymbols = this.getAllCellsAtNode();
		for (Cell key : fullMap.nonGenericKeySet()) {
			if (!relevantSymbols.contains(key)) {
				continue;
			}
			Cell rhs = fullMap.get(key);
			if (key == rhs) {
				continue;
			}
			if (rhs == Cell.genericCell || !relevantSymbols.contains(rhs)) {
				continue;
			}
			retMap.put(key, rhs);
		}
		return retMap;
	}

	/**
	 * Obtain the set of nodes which may represent definitions that are reachable at
	 * the owner node, for cell {@code
	 * cell}.
	 *
	 * @param cell
	 *             whose reaching definition has to be found .
	 *
	 * @return set of nodes which may represent definitions that are reachable at
	 *         the owner node, for cell {@code cell}.
	 */
	public Set<Definition> getReachingDefinitions(Cell cell) {
		Set<Definition> returnSet = new HashSet<>();
		ReachingDefinitionFlowMap rdf = (ReachingDefinitionFlowMap) this.getIN(AnalysisName.REACHING_DEFINITION);
		if (rdf == null) {
			return returnSet;
		}
		return rdf.flowMap.get(cell);
		// OLD CODE:
		// for (Definition def : rdf.flowMap.get(cell).getInternalSetCopy()) {
		// Node defNode = def.getDefiningNode();
		// returnSet.add(defNode);
		// }
		// return returnSet;
	}

	/**
	 * Obtain the set of nodes which may represent definitions that are reachable at
	 * the owner node.
	 *
	 * @return set of nodes which may represent definitions that are reachable at
	 *         the owner node.
	 */
	public Set<Definition> getReachingDefinitions() {
		Set<Definition> reachingDefinitions = new HashSet<>();
		ReachingDefinitionFlowMap rdf = (ReachingDefinitionFlowMap) this.getIN(AnalysisName.REACHING_DEFINITION);
		CellSet cellsHere = this.getAllCellsAtNode();
		if (rdf == null) {
			return reachingDefinitions;
		}
		for (Cell key : rdf.flowMap.nonGenericKeySet()) {
			if (!cellsHere.contains(key)) {
				continue;
			}
			// ImmutableDefinitionSet tempSet = rdf.flowMap.get(key);
			reachingDefinitions.addAll(rdf.flowMap.get(key));
		}
		if (rdf.flowMap.isUniversal()) {
			reachingDefinitions = new HashSet<>(reachingDefinitions);
			reachingDefinitions.addAll(rdf.flowMap.get(Cell.genericCell));
			// ImmutableDefinitionSet tempSet = rdf.flowMap.get(Cell.genericCell);
			// for (Definition def : tempSet) {
			// reachingDefinitions.add(def.getDefiningNode());
			// }
		}
		// Old code: This method depends upon data-dependence forward approach. (Too
		// slow, though.)
		// DataDependenceForwardFF ff = (DataDependenceForwardFF)
		// this.getIN(AnalysisName.DATA_DEPENDENCE_FORWARD);
		// if (ff != null && ff.writeSources != null) {
		// for (Cell c : this.getAllCellsAtNode()) {
		// if (ff.writeSources.containsKey(c)) {
		// reachingDefinitions.addAll(ff.writeSources.get(c).getReadOnlySet());
		// }
		// }
		// }
		return reachingDefinitions;
	}

	private static boolean printedVal = false;

	public Set<Node> getDominators() {
		if (!printedVal) {
			printedVal = true;
			DumpSnapshot.dumpRoot("beforeDom");
		}
		DominatorFlowFact da = (DominatorFlowFact) this.getIN(AnalysisName.DOMINANCE);
		if (da == null) {
			return new HashSet<>();
		}
		if (da.dominators == null) {
			return new HashSet<>();
		}
		Set<Node> retSet = new HashSet<>(da.dominators);
		// assert (retSet.contains(this.getNode())) : "Could not find " +
		// this.getNode().toString() + ", a "
		// + this.getNode().getClass().getSimpleName() + " as its own dominator!";
		retSet.add(this.getNode());
		return retSet;
	}

	/**
	 * Obtain the set of nodes which may represent definitions that may be reachable
	 * to instances of the owner node in
	 * given phase {@code ph}.
	 *
	 * @param ph
	 *                                            phase in which the owner node
	 *                                            exists, and for which the
	 *                                            reaching-definitions have to be
	 *                                            found.
	 * @param phaseInsensitiveReachingDefinitions
	 *                                            set of nodes that are
	 *                                            reaching-definitions of this node
	 *                                            across all phases.
	 *
	 * @return
	 */
	public Set<Node> getPhaseSensitiveReachingDefinitions(Phase ph, Set<Node> phaseInsensitiveReachingDefinitions) {
		return phaseInsensitiveReachingDefinitions;
		// Old code: Incorrect! RDs need not belong to the same phase!
		// Set<Node> returnSet = new HashSet<>();
		// returnSet.addAll(phaseInsensitiveReachingDefinitions.parallelStream()
		// .filter(n -> n.getInfo().getNodePhaseInfo().getPhaseSetCopy().contains(ph))
		// .collect(Collectors.toCollection(HashSet::new)));
		// for (BeginPhasePoint bpp : ph.getBeginPointsCopy()) {
		// Node startingNode = bpp.getNode(); // this can be a BeginNode or a
		// BarrierDirective.
		// returnSet.addAll(Misc.setIntersection(startingNode.getInfo().getReachingDefinitions(),
		// phaseInsensitiveReachingDefinitions));
		// }
		// return returnSet;
	}

	public Set<Node> getFlowSources() {
		Set<Node> retSet = new HashSet<>();
		DataDependenceForwardFF ff = (DataDependenceForwardFF) this.getIN(AnalysisName.DATA_DEPENDENCE_FORWARD);
		if (ff == null || ff.writeSources == null) {
			return retSet;
		}
		this.getReads().applyAllExpanded((sym) -> {
			NodeSet nodeSet = ff.writeSources.get(sym);
			if (nodeSet != null) {
				Set<Node> nodesForSym = nodeSet.getReadOnlySet();
				if (nodesForSym != null) {
					retSet.addAll(nodesForSym);
				}
			}
		});
		return retSet;
	}

	public Set<Node> getFlowDestinations() {
		Set<Node> retSet = new HashSet<>();
		CellMap<NodeSet> fDMap = this.getReadDestinations();
		this.getWrites().applyAllExpanded(sym -> {
			NodeSet nodeSet = fDMap.get(sym);
			if (nodeSet != null) {
				Set<Node> nodesForSym = nodeSet.getReadOnlySet();
				if (nodesForSym != null) {
					retSet.addAll(nodesForSym);
				}
			}
		});
		// Old Code:
		// DataDependenceBackwardFF ff = (DataDependenceBackwardFF)
		// this.getOUT(AnalysisName.DATA_DEPENDENCE_BACKWARD);
		// if (ff == null || ff.readDestinations == null) {
		// return retSet;
		// }
		// this.getWrites().applyAllExpanded(sym -> {
		// NodeSet nodeSet = ff.readDestinations.get(sym);
		// if (nodeSet != null) {
		// Set<Node> nodesForSym = nodeSet.getReadOnlySet();
		// if (nodesForSym != null) {
		// retSet.addAll(nodesForSym);
		// }
		// }
		// });
		return retSet;
	}

	public Set<Node> getAntiSources() {
		Set<Node> retSet = new HashSet<>();
		if (Misc.doIntersect(this.getReads(), this.getWrites())) {
			retSet.add(this.getNode());
		}

		DataDependenceForwardFF ff = (DataDependenceForwardFF) this.getIN(AnalysisName.DATA_DEPENDENCE_FORWARD);
		if (ff == null || ff.readSources == null) {
			return retSet;
		}
		this.getWrites().applyAllExpanded(sym -> {
			NodeSet nodeSet = ff.readSources.get(sym);
			if (nodeSet != null) {
				Set<Node> nodesForSym = nodeSet.getReadOnlySet();
				if (nodesForSym != null) {
					retSet.addAll(nodesForSym);
				}
			}
		});

		return retSet;
	}

	public Set<Node> getAntiDestinations() {
		Set<Node> retSet = new HashSet<>();
		CellMap<NodeSet> aDMap = this.getWriteDestinations();
		this.getReads().applyAllExpanded(sym -> {
			NodeSet nodeSet = aDMap.get(sym);
			if (nodeSet != null) {
				Set<Node> nodesForSym = nodeSet.getReadOnlySet();
				if (nodesForSym != null) {
					retSet.addAll(nodesForSym);
				}
			}
		});
		// Old Code:
		// DataDependenceBackwardFF ff = (DataDependenceBackwardFF)
		// this.getOUT(AnalysisName.DATA_DEPENDENCE_BACKWARD);
		// if (ff == null || ff.writeDestinations == null) {
		// return retSet;
		// }
		// this.getReads().applyAllExpanded(sym -> {
		// if (nodeSet != null) {
		// Set<Node> nodesForSym = nodeSet.getReadOnlySet();
		// if (nodesForSym != null) {
		// retSet.addAll(nodesForSym);
		// }
		// }
		// });
		return retSet;
	}

	public Set<Node> getOutputSources() {
		Set<Node> retSet = new HashSet<>();
		DataDependenceForwardFF ff = (DataDependenceForwardFF) this.getIN(AnalysisName.DATA_DEPENDENCE_FORWARD);
		if (ff == null || ff.writeSources == null) {
			return retSet;
		}
		this.getWrites().applyAllExpanded(sym -> {
			NodeSet nodeSet = ff.writeSources.get(sym);
			if (nodeSet != null) {
				Set<Node> nodesForSym = nodeSet.getReadOnlySet();
				if (nodesForSym != null) {
					retSet.addAll(nodesForSym);
				}
			}
		});
		return retSet;
	}

	public Set<Node> getOutputDestinations() {
		Set<Node> retSet = new HashSet<>();
		CellMap<NodeSet> oDMap = this.getWriteDestinations();
		this.getWrites().applyAllExpanded(sym -> {
			NodeSet nodeSet = oDMap.get(sym);
			if (nodeSet != null) {
				Set<Node> nodesForSym = nodeSet.getReadOnlySet();
				if (nodesForSym != null) {
					retSet.addAll(nodesForSym);
				}
			}
		});
		// Old Code:
		// DataDependenceBackwardFF ff = (DataDependenceBackwardFF)
		// this.getOUT(AnalysisName.DATA_DEPENDENCE_BACKWARD);
		// if (ff == null || ff.writeDestinations == null) {
		// return retSet;
		// }
		// this.getWrites().applyAllExpanded(sym -> {
		// NodeSet nodeSet = ff.writeDestinations.get(sym);
		// if (nodeSet != null) {
		// Set<Node> nodesForSym = nodeSet.getReadOnlySet();
		// if (nodesForSym != null) {
		// retSet.addAll(nodesForSym);
		// }
		// }
		// });
		return retSet;
	}

	/**
	 * Obtain the set of all those nodes for which there exists a symbol {@code s}
	 * such that the value of {@code s} read
	 * by that node is same as the value visible at the owner node.
	 *
	 * @return set of all those nodes for which there exists a symbol {@code s} such
	 *         that the value of {@code s} read by
	 *         that node is same as the value visiable at the owner node.
	 */
	public CellMap<NodeSet> getReadDestinations() {
		if (!NodeInfo.ddfDone) {
			NodeInfo.ddfDone = true;
			performDDF();
		}
		if (!NodeInfo.readWriteDestinationsSet) {
			NodeInfo.setReadWriteDestinations();
		}
		if (this.readDestinations == null) {
			CellMap<NodeSet> newCellMap = new CellMap<>();
			// newCellMap.put(Cell.getGenericCell(), new NodeSet(new HashSet<>(2)));
			this.readDestinations = newCellMap;
		}
		return readDestinations;
	}

	/**
	 * Obtain the set of all those nodes for which there exists a symbol {@code s}
	 * such that the value of {@code s}
	 * written by that node is same as the value visible at owner node.
	 *
	 * @return set of all those nodes for which there exists a symbol {@code s} such
	 *         that the value of {@code s} written
	 *         by that node is same as the value visible at the owner node.
	 */
	public CellMap<NodeSet> getWriteDestinations() {
		if (!NodeInfo.ddfDone) {
			NodeInfo.ddfDone = true;
			performDDF();
		}
		if (!NodeInfo.readWriteDestinationsSet) {
			NodeInfo.setReadWriteDestinations();
		}
		if (this.writeDestinations == null) {
			CellMap<NodeSet> newCellMap = new CellMap<>();
			// newCellMap.put(Cell.getGenericCell(), new NodeSet(new HashSet<>(2)));
			this.writeDestinations = newCellMap;
		}
		return writeDestinations;
	}

	/**
	 * Populate the data structures {@link NodeInfo#readDestinations} and
	 * {@link NodeInfo#writeDestinations} using the
	 * forward data-dependence information, for all the nodes.
	 */
	public static void setReadWriteDestinations() {
		NodeInfo.readWriteDestinationsSet = true;
		for (Node n1 : Program.getRoot().getInfo().getAllLeafNodesInTheProgram()) {
			DataDependenceForwardFF n1FF = (DataDependenceForwardFF) n1.getInfo()
					.getIN(AnalysisName.DATA_DEPENDENCE_FORWARD);
			if (n1FF == null) {
				continue;
			}
			CellList n1Reads = n1.getInfo().getReads();
			if (n1Reads.isUniversal()) {
				// if (n1FF.readSources != null) {
				// if (n1FF.readSources.isUniversal()) {
				// for (Node n2 : n1FF.readSources.get(Cell.getGenericCell()).getReadOnlySet())
				// {
				// if (n2.getInfo().getReadDestinations().isUniversal()) {
				// NodeInfo.addNodeToMap(n2.getInfo().getReadDestinations(),
				// Cell.getGenericCell(), n1);
				// } else {
				// for (Cell cn2 : n2.getInfo().getReadDestinations().keySetExpanded()) {
				// NodeInfo.addNodeToMap(n2.getInfo().getReadDestinations(), cn2, n1);
				// }
				// }
				// }
				// } else {
				// for (Cell c : n1FF.readSources.keySetExpanded()) {
				// for (Node n2 : n1FF.readSources.get(c).getReadOnlySet()) {
				// NodeInfo.addNodeToMap(n2.getInfo().getReadDestinations(), c, n1);
				// }
				// }
				// }
				// }
				if (n1FF.writeSources != null) {
					if (n1FF.writeSources.isUniversal()) {
						for (Node n2 : n1FF.writeSources.get(Cell.genericCell).getReadOnlySet()) {
							if (n2.getInfo().getReadDestinations().isUniversal()) {
								NodeInfo.addNodeToMap(n2.getInfo().getReadDestinations(), Cell.genericCell, n1);
							} else {
								for (Cell cn2 : n2.getInfo().getReadDestinations().keySetExpanded()) {
									NodeInfo.addNodeToMap(n2.getInfo().getReadDestinations(), cn2, n1);
								}
							}
						}
					} else {
						for (Cell c : n1FF.writeSources.keySetExpanded()) {
							for (Node n2 : n1FF.writeSources.get(c).getReadOnlySet()) {
								NodeInfo.addNodeToMap(n2.getInfo().getReadDestinations(), c, n1);
							}
						}
					}
				}
			} else {
				for (Cell read : n1Reads) {
					// if (n1FF.readSources != null && n1FF.readSources.containsKey(read)) {
					// for (Node n2 : n1FF.readSources.get(read).getReadOnlySet()) {
					// NodeInfo.addNodeToMap(n2.getInfo().getReadDestinations(), read, n1);
					// }
					// }
					if (n1FF.writeSources != null && n1FF.writeSources.containsKey(read)) {
						for (Node n2 : n1FF.writeSources.get(read).getReadOnlySet()) {
							NodeInfo.addNodeToMap(n2.getInfo().getReadDestinations(), read, n1);
						}
					}
				}
			}
			CellList n1Writes = n1.getInfo().getWrites();
			if (n1Writes.isUniversal()) {
				if (n1FF.readSources != null) {
					if (n1FF.readSources.isUniversal()) {
						for (Node n2 : n1FF.readSources.get(Cell.genericCell).getReadOnlySet()) {
							if (n2.getInfo().getWriteDestinations().isUniversal()) {
								NodeInfo.addNodeToMap(n2.getInfo().getWriteDestinations(), Cell.genericCell, n1);
							} else {
								for (Cell cn2 : n2.getInfo().getWriteDestinations().keySetExpanded()) {
									NodeInfo.addNodeToMap(n2.getInfo().getWriteDestinations(), cn2, n1);
								}
							}
						}
					} else {
						for (Cell c : n1FF.readSources.keySetExpanded()) {
							for (Node n2 : n1FF.readSources.get(c).getReadOnlySet()) {
								NodeInfo.addNodeToMap(n2.getInfo().getWriteDestinations(), c, n1);
							}
						}
					}
				}
				if (n1FF.writeSources != null) {
					if (n1FF.writeSources.isUniversal()) {
						for (Node n2 : n1FF.writeSources.get(Cell.genericCell).getReadOnlySet()) {
							if (n2.getInfo().getWriteDestinations().isUniversal()) {
								NodeInfo.addNodeToMap(n2.getInfo().getWriteDestinations(), Cell.genericCell, n1);
							} else {
								for (Cell cn2 : n2.getInfo().getWriteDestinations().keySetExpanded()) {
									NodeInfo.addNodeToMap(n2.getInfo().getWriteDestinations(), cn2, n1);
								}
							}
						}
					} else {
						for (Cell c : n1FF.writeSources.keySetExpanded()) {
							for (Node n2 : n1FF.writeSources.get(c).getReadOnlySet()) {
								NodeInfo.addNodeToMap(n2.getInfo().getWriteDestinations(), c, n1);
							}
						}
					}
				}
			} else {
				for (Cell write : n1Writes) {
					if (n1FF.readSources != null && n1FF.readSources.containsKey(write)) {
						for (Node n2 : n1FF.readSources.get(write).getReadOnlySet()) {
							NodeInfo.addNodeToMap(n2.getInfo().getWriteDestinations(), write, n1);
						}
					}
					if (n1FF.writeSources != null && n1FF.writeSources.containsKey(write)) {
						for (Node n2 : n1FF.writeSources.get(write).getReadOnlySet()) {
							NodeInfo.addNodeToMap(n2.getInfo().getWriteDestinations(), write, n1);
						}
					}
				}
			}

			// Old Code:
			// if (n1FF.readSources != null) {
			// if (n1FF.readSources.isUniversal()) {
			// for (Node n2 : n1FF.readSources.get(Cell.getGenericCell()).getReadOnlySet())
			// {
			// NodeSet n2Set =
			// n2.getInfo().getReadDestinations().get(Cell.getGenericCell());
			// if (n2Set == null) {
			// n2Set = new NodeSet(new HashSet<>(4));
			// }
			// n2Set.addNode(n1);
			// n2.getInfo().getReadDestinations().put(Cell.getGenericCell(), n2Set);
			// }
			// } else {
			// for (Cell cell : n1FF.readSources.keySetExpanded()) {
			// for (Node n2 : n1FF.readSources.get(cell).getReadOnlySet()) {
			//
			// n2.getInfo().getReadDestinations().get(cell).addNode(n1);
			// }
			// }
			// }
			// }
			// if (n1FF.writeSources != null) {
			// if (n1FF.writeSources.isUniversal()) {
			// for (Node n2 : n1FF.writeSources.get(Cell.getGenericCell()).getReadOnlySet())
			// {
			// NodeSet n2Set =
			// n2.getInfo().getWriteDestinations().get(Cell.getGenericCell());
			// if (n2Set == null) {
			// n2Set = new NodeSet(new HashSet<>(4));
			// }
			// n2Set.addNode(n1);
			// n2.getInfo().getWriteDestinations().put(Cell.getGenericCell(), n2Set);
			// }
			// } else {
			// for (Cell cell : n1FF.writeSources.keySetExpanded()) {
			// for (Node n2 : n1FF.writeSources.get(cell).getReadOnlySet()) {
			// n2.getInfo().getWriteDestinations().get(cell).addNode(n1);
			// }
			// }
			// }
			//
			// }
		}
	}

	/**
	 * Collect first nodes reachable on all forward paths that may kill the value(s)
	 * for any cell in {@code atStack}, as
	 * visible at the exit to this node.
	 *
	 * @param atStake
	 *                a set of cells whose killers have to be found.
	 *
	 * @return set of all those first nodes reachable on all forward paths that may
	 *         kill the value(s) for any cell in
	 *         {@code atStack}, as visible at the exit to this node.
	 */
	public Set<Node> getFirstPossibleKillersForwardExclusively(CellSet atStake) {
		final Set<Node> nodeSet = new HashSet<>();
		Node leafNode = this.getNode();
		NodeWithStack startPoint = new NodeWithStack(leafNode, new CallStack());
		CollectorVisitor.collectNodeSetInGenericGraph(startPoint, null, (n) -> {
			CellSet writesOfN = new CellSet(n.getNode().getInfo().getWrites());
			if (Misc.doIntersect(writesOfN, atStake)) {
				nodeSet.add(n.getNode());
				return true;
			} else {
				return false;
			}
		}, (n) -> n.getNode().getInfo().getCFGInfo().getInterTaskLeafSuccessorNodesForVariables(n.getCallStack(),
				atStake, Program.sveSensitive));
		return nodeSet;
	}

	/**
	 * Collect all those forward reachable leaf nodes that may read same value for
	 * the cell {@code w} as is visible at
	 * the exit to this node.
	 * <br>
	 * Note that in this version, a node that both reads as well as writes to
	 * {@code w} is not added to the return set.
	 *
	 * @param w
	 *          a cell.
	 *
	 * @return set of all those forward reachable leaf nodes that may read same
	 *         value for the cell {@code w} as is
	 *         visible at the exit to this node.
	 */
	public Set<Node> getAllUsesForwardsExclusively(Cell w) {
		Node leafNode = this.getNode();
		assert (Misc.isCFGLeafNode(leafNode));
		CellSet focusSet = new CellSet();
		focusSet.add(w);
		NodeWithStack startPoint = new NodeWithStack(leafNode, new CallStack());
		Set<Node> readSet = new HashSet<>();

		CollectorVisitor.collectNodeSetInGenericGraph(startPoint, null, (n) -> {
			CellSet readsOfN = new CellSet(n.getNode().getInfo().getReads());
			if (Misc.doIntersect(readsOfN, focusSet)) {
				readSet.add(n.getNode());
			}
			/*
			 * Stop the processing only when write to "w" is not optional at
			 * this node.
			 * Even on the termination node, if there is any read to "w", it
			 * must be considered.
			 */
			CellSet writesOfN = new CellSet(n.getNode().getInfo().getWrites());
			if (writesOfN.size() == 1 && writesOfN.contains(w)) {
				return true;
			} else {
				return false;
			}
		}, n -> n.getNode().getInfo().getCFGInfo().getInterTaskLeafSuccessorNodesForVariables(n.getCallStack(),
				focusSet, Program.sveSensitive));
		return readSet;
	}

	/**
	 * A traversal based liveness information. This seems to be a much better
	 * alternative than a costly IDFA pass, for
	 * many scenarios.
	 *
	 * @param w
	 *          a cell.
	 *
	 * @return true, if there may be any use of {@code w} on any path without it
	 *         being overwritten, starting at the exit
	 *         of this node.
	 */
	public boolean isCellLiveOut(Cell w) {
		return this.hasUsesForwardsExclusively(w);
	}

	/**
	 * Checks if there is a forward reachable leaf node that may read same value for
	 * the cell {@code w} as is visible at
	 * the exit to this node.
	 * <br>
	 * Note that such node may even be a writer of {@code w}.
	 *
	 * @param w
	 *          a cell.
	 *
	 * @return true if there exists a forward reachable leaf node that may read same
	 *         value for the cell {@code w} as is
	 *         visible at the exit to this node.
	 */
	public boolean hasUsesForwardsExclusively(Cell w) {
		Node leafNode = this.getNode();
		assert (Misc.isCFGLeafNode(leafNode));
		CellSet focusSet = new CellSet();
		focusSet.add(w);
		NodeWithStack startPoint = new NodeWithStack(leafNode, new CallStack());
		Set<Node> readSet = new HashSet<>();

		CollectorVisitor.collectNodeSetInGenericGraph(startPoint, null, (n) -> {
			CellSet readsOfN = new CellSet(n.getNode().getInfo().getReads());
			if (Misc.doIntersect(readsOfN, focusSet)) {
				readSet.add(n.getNode());
			}
			/*
			 * Stop the processing only when write to "w" is not optional at
			 * this node.
			 * Even on the termination node, if there is any read to "w", it
			 * must be considered.
			 */
			CellSet writesOfN = new CellSet(n.getNode().getInfo().getWrites());
			if (writesOfN.size() == 1 && writesOfN.contains(w)) {
				return true;
			} else {
				return false;
			}
		}, n -> n.getNode().getInfo().getCFGInfo().getInterTaskLeafSuccessorNodesForVariables(n.getCallStack(),
				focusSet, Program.sveSensitive));

		if (readSet.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Collect all those backwards reachable leaf nodes that may read same value for
	 * the cell {@code w} as is visible at
	 * the entry to this node.
	 *
	 * @param w
	 *          a cell.
	 *
	 * @return set of all those backward reachable leaf nodes that may read same
	 *         value for the cell {@code w} as is
	 *         visible at the entry to this node.
	 */
	public Set<Node> getAllUsesBackwardsExclusively(Cell w) {
		Node leafNode = this.getNode();
		assert (Misc.isCFGLeafNode(leafNode));
		CellSet focusSet = new CellSet();
		focusSet.add(w);
		NodeWithStack startPoint = new NodeWithStack(leafNode, new CallStack());
		Set<Node> readSet = new HashSet<>();

		CollectorVisitor.collectNodeSetInGenericGraph(startPoint, null, (n) -> {
			/*
			 * Stop the processing only when write to "w" is not optional at
			 * this node.
			 * Note that since write is assumed to happen at the end, we do not
			 * consider the reads of n, if it already has a write to w.
			 */
			CellSet writesOfN = new CellSet(n.getNode().getInfo().getWrites());
			if (writesOfN.size() == 1 && writesOfN.contains(w)) {
				return true;
			} else {
				CellSet readsOfN = new CellSet(n.getNode().getInfo().getReads());
				if (Misc.doIntersect(readsOfN, focusSet)) {
					readSet.add(n.getNode());
				}
				return false;
			}
		}, n -> n.getNode().getInfo().getCFGInfo().getInterTaskLeafPredecessorNodesForVariables(n.getCallStack(),
				focusSet, Program.sveSensitive));
		return readSet;
	}

	/**
	 * Checks if there is a backward reachable leaf node that may read same value
	 * for the cell {@code w} as is visible
	 * at the entry to this node.
	 *
	 * @param w
	 *          a cell.
	 *
	 * @return true if there exists a backward reachable leaf node that may read
	 *         same value for the cell {@code w} as is
	 *         visible at the exit to this node.
	 */
	public boolean hasUsesBackwardsExclusively(Cell w) {
		Node leafNode = this.getNode();
		assert (Misc.isCFGLeafNode(leafNode));
		CellSet focusSet = new CellSet();
		focusSet.add(w);
		NodeWithStack startPoint = new NodeWithStack(leafNode, new CallStack());
		Set<Node> readSet = new HashSet<>();

		CollectorVisitor.collectNodeSetInGenericGraph(startPoint, null, (n) -> {
			/*
			 * Stop the processing only when write to "w" is not optional at
			 * this node.
			 * Note that since write is assumed to happen at the end, we do not
			 * consider the reads of n, if it already has a write to w.
			 */
			CellSet writesOfN = new CellSet(n.getNode().getInfo().getWrites());
			if (writesOfN.size() == 1 && writesOfN.contains(w)) {
				return true;
			} else {
				CellSet readsOfN = new CellSet(n.getNode().getInfo().getReads());
				if (Misc.doIntersect(readsOfN, focusSet)) {
					readSet.add(n.getNode());
				}
				return false;
			}
		}, n -> n.getNode().getInfo().getCFGInfo().getInterTaskLeafPredecessorNodesForVariables(n.getCallStack(),
				focusSet, Program.sveSensitive));
		return !readSet.isEmpty();
	}

	private static void addNodeToMap(CellMap<NodeSet> map, Cell cell, Node node) {
		NodeSet nS = map.get(cell);
		if (nS != null) {
			nS.addNode(node);
			return;
		} else {
			Set<Node> nodeSet = new HashSet<>();
			nodeSet.add(node);
			nS = new NodeSet(nodeSet);
			map.put(cell, nS);
		}
	}

	/**
	 * Obtain a list of {@code #pragma imop ....} annotations. Note that this field
	 * is applicable only for CFG nodes.
	 * For other nodes, we simply return null.
	 *
	 * @return list of pragma imop annotations, corresponding to the owner CFG node.
	 */
	public List<PragmaImop> getPragmaAnnotations() {
		if (!Misc.isCFGNode(this.node)) {
			return null;
		}
		if (this.pragmaAnnotations == null) {
			this.pragmaAnnotations = new ArrayList<>();
		}
		return this.pragmaAnnotations;
	}

	/**
	 * Given a set of nodes (possibly non-leaf), this method checks whether the
	 * owner expression is a predicate of a
	 * some non-leaf node that (i) is SCOPed, and (ii) does not contain any element
	 * from the given set.
	 *
	 * @param nodeSet
	 *
	 * @return
	 */
	public boolean isSCOPPed(Set<Node> nodeSet) {
		Node node = this.getNode();
		Node encloser = null;
		if (node instanceof Expression) {
			Expression exp = (Expression) this.getNode();
			if (!Misc.isAPredicate(exp)) {
				return false;
			} else {
				encloser = Misc.getEnclosingCFGNonLeafNode(exp);
			}
		} else if (node instanceof OmpForCondition) {
			encloser = Misc.getEnclosingCFGNonLeafNode(node);
		} else {
			assert (false);
			return false;
		}
		if (encloser == null) {
			return false;
		}

		if (!encloser.getInfo().isControlConfined()) {
			return false;
		}
		Set<Node> contents = encloser.getInfo().getCFGInfo().getIntraTaskCFGLeafContents();
		Set<Node> leafContents = new HashSet<>();
		for (Node n : nodeSet) {
			leafContents.addAll(n.getInfo().getCFGInfo().getIntraTaskCFGLeafContents());
		}
		if (Misc.doIntersect(contents, leafContents)) {
			return false;
		}
		return true;
	}

	/**
	 * Given a nodes, this method checks whether the owner expression is a predicate
	 * of some non-leaf node that (i) is
	 * SCOPed, and (ii) does not contain the given node.
	 *
	 * @param nodeSet
	 *
	 * @return
	 */
	public boolean isSCOPPed(Node tempNode) {
		Set<Node> tempSet = new HashSet<>();
		tempSet.add(tempNode);
		return this.isSCOPPed(tempSet);
	}

	public Set<Scopeable> getLexicallyEnclosedScopesInclusive() {
		// TODO: Optimize all call-sites of this method.
		// We can create the information required in a bottom-up manner, when it comes
		// to scopes.
		Set<Scopeable> retSet = new HashSet<>();
		Set<Class<? extends Node>> classSet = new HashSet<>();
		classSet.add(TranslationUnit.class);
		classSet.add(FunctionDefinition.class);
		classSet.add(CompoundStatement.class);
		for (Node scopeNode : Misc.getExactEnclosee(this.getNode(), classSet)) {
			retSet.add((Scopeable) scopeNode);
		}
		return retSet;
	}

	/**
	 * Removes unnecessary symbols, types, and typedefs from within this node.
	 */
	public void removeUnusedElements() {
		removeUnusedVariables();
		boolean changed;
		do {
			boolean internal;
			changed = false;
			do {
				internal = false;
				internal |= this.removeUnusedTypedefs();
				changed |= internal;
			} while (internal);

			do {
				internal = false;
				internal |= this.removeUnusedTypes();
				changed |= internal;
			} while (internal);
		} while (changed);
	}

	/**
	 * Removes unnecessary symbols from within this node.
	 * <p>
	 * Note: If you wish to remove types and typedefs as well, use
	 * {@link NodeInfo#removeUnusedElements()} instead.
	 */
	public void removeUnusedVariables() {
		Node baseNode = getNode();
		CellSet usedCells = baseNode.getInfo().getUsedCells();
		// for (Cell c : usedCells) {
		// System.err.println("Used a cell: " + c.toString());
		// }
		assert (!usedCells.isUniversal());

		System.err.print("\tDeleting the declarations for the following unused variables: ");
		boolean deletedAny = false;
		for (Scopeable scopeNode : baseNode.getInfo().getLexicallyEnclosedScopesInclusive()) {
			if (scopeNode instanceof TranslationUnit) {
				TranslationUnit scope = (TranslationUnit) scopeNode;
				Collection<Symbol> symbolSet = new HashSet<>(scope.getInfo().getSymbolTable().values());
				for (Symbol sym : symbolSet) {
					if (!usedCells.contains(sym)) {
						Node declaringNode = sym.getDeclaringNode();
						if (declaringNode instanceof Declaration) {
							// System.err.println("\tRemoving declaration from root for: " + sym.getName());
							ElementsOfTranslation elemOfTranslation = Misc.getEnclosingNode(declaringNode,
									ElementsOfTranslation.class);
							scope.getF0().removeNode(elemOfTranslation);
							// Note: The line below needs to be tested.
							scope.getInfo().removeDeclarationEffects((Declaration) declaringNode);
							System.err.print(sym.getName() + "; ");
						}
					}
				}
			} else if (scopeNode instanceof FunctionDefinition) {
				continue;
			} else if (scopeNode instanceof CompoundStatement) {
				CompoundStatement scope = (CompoundStatement) scopeNode;
				Collection<Symbol> symbolSet = new HashSet<>(scope.getInfo().getSymbolTable().values());
				for (Symbol sym : symbolSet) {
					if (!usedCells.contains(sym)) {
						Declaration decl = (Declaration) sym.getDeclaringNode();
						Initializer init = decl.getInfo().getInitializer();
						if (init != null) {
							Statement stmt = FrontEnd.parseAlone(init.toString() + ";", Statement.class);
							int index = scope.getInfo().getCFGInfo().getElementList().indexOf(decl);
							scope.getInfo().getCFGInfo().addStatement(index, stmt);
						}
						scope.getInfo().getCFGInfo().removeDeclaration(decl);
						System.err.print(sym.getName() + "; ");
					}
				}
			}
		}
		if (!deletedAny) {
			System.err.println("<none>");
		} else {
			System.err.println();
		}
	}

	/**
	 * Removes unnecessary types from within this node.
	 * <p>
	 * Note: If you wish to remove symbols and typedefs as well, use
	 * {@link NodeInfo#removeUnusedElements()} instead.
	 */
	public boolean removeUnusedTypes() {
		Node baseNode = getNode();
		boolean changed = false;
		Set<Type> usedTypes = baseNode.getInfo().getUsedTypes();
		// for (Type type : usedTypes) {
		// System.err.println("Used a type: " + type);
		// }

		System.err.print("\tDeleting the declarations for the following unused types: ");
		for (Scopeable scopeNode : baseNode.getInfo().getLexicallyEnclosedScopesInclusive()) {
			if (scopeNode instanceof TranslationUnit) {
				TranslationUnit scope = (TranslationUnit) scopeNode;
				Collection<Type> typeSet = new HashSet<>(scope.getInfo().getTypeTable().values());
				for (Type type : typeSet) {
					Declaration declaringNode = null;
					if (type instanceof StructType) {
						declaringNode = ((StructType) type).getDeclaringNode();
					} else if (type instanceof UnionType) {
						declaringNode = ((UnionType) type).getDeclaringNode();
					} else if (type instanceof EnumType) {
						declaringNode = ((EnumType) type).getDeclaringNode();
					}
					if (declaringNode != null) {
						if (type.isComplete()) {
							if (!usedTypes.contains(type)) {
								ElementsOfTranslation elemOfTranslation = Misc.getEnclosingNode(declaringNode,
										ElementsOfTranslation.class);
								scope.getF0().removeNode(elemOfTranslation);
								scope.getInfo().removeTypeDeclarationEffects(declaringNode);
								System.err.print(type);
								changed = true;
							}
						}
					}
				}
			} else if (scopeNode instanceof FunctionDefinition) {
				continue;
			} else if (scopeNode instanceof CompoundStatement) {
				CompoundStatement scope = (CompoundStatement) scopeNode;
				Collection<Type> typeSet = new HashSet<>(scope.getInfo().getTypeTable().values());
				for (Type type : typeSet) {
					Declaration declaringNode = null;
					if (type instanceof StructType) {
						declaringNode = ((StructType) type).getDeclaringNode();
					} else if (type instanceof UnionType) {
						declaringNode = ((UnionType) type).getDeclaringNode();
					} else if (type instanceof EnumType) {
						declaringNode = ((EnumType) type).getDeclaringNode();
					}
					if (declaringNode != null) {
						if (type.isComplete()) {
							if (!usedTypes.contains(type)) {
								scope.getInfo().getCFGInfo().removeDeclaration(declaringNode);
								System.err.print(type);
								changed = true;
							}
						}
					}
				}
			}
		}
		if (changed) {
			System.err.println();
		} else {
			System.err.println("<none>");
		}
		return changed;
	}

	/**
	 * Removes unnecessary typedefs from within this node.
	 * <p>
	 * Note: If you wish to remove symbols and types as well, use
	 * {@link NodeInfo#removeUnusedElements()} instead.
	 */
	public boolean removeUnusedTypedefs() {
		Node baseNode = getNode();
		boolean changed = false;
		Set<Typedef> usedTypedefs = baseNode.getInfo().getUsedTypedefs();

		System.err.print("\tDeleting the declarations for the following unused typedefs: ");
		for (Scopeable scopeNode : baseNode.getInfo().getLexicallyEnclosedScopesInclusive()) {
			if (scopeNode instanceof TranslationUnit) {
				TranslationUnit scope = (TranslationUnit) scopeNode;
				Collection<Typedef> typedefSet = new HashSet<>(scope.getInfo().getTypedefTable().values());
				for (Typedef tDef : typedefSet) {
					if (!usedTypedefs.contains(tDef)) {
						Declaration definingNode = tDef.getDefiningNode();
						ElementsOfTranslation elemOfTranslation = Misc.getEnclosingNode(definingNode,
								ElementsOfTranslation.class);
						scope.getF0().removeNode(elemOfTranslation);
						scope.getInfo().removeDeclarationEffects(definingNode);
						System.err.print(tDef.getTypedefName() + "; ");
						changed = true;
					}
				}
			} else if (scopeNode instanceof FunctionDefinition) {
				continue;
			} else if (scopeNode instanceof CompoundStatement) {
				CompoundStatement scope = (CompoundStatement) scopeNode;
				Collection<Typedef> typedefSet = new HashSet<>(scope.getInfo().getTypedefTable().values());
				for (Typedef tDef : typedefSet) {
					if (!usedTypedefs.contains(tDef)) {
						Declaration decl = tDef.getDefiningNode();
						scope.getInfo().getCFGInfo().removeDeclaration(decl);
						System.err.print(tDef.getTypedefName() + "; ");
						changed = true;
					}
				}
			}
		}
		if (changed) {
			System.err.println();
		} else {
			System.err.println("<none>");
		}
		return changed;
	}

	/**
	 * For the owner node, this method returns a list of {@link Assignment} that may
	 * possibly happen during the
	 * execution of the owner node, and are lexically within the node. Note that the
	 * assignment of arguments to
	 * parameters, and returned value to the returned receiver are not considered as
	 * lexically enclosed assignments.
	 * Hence, an assignment may represent any of the following forms:
	 * <ol>
	 * <li>NonConditionalExpressions, e.g., {@code x = y + 3}</li>
	 * <li>Initializations, e.g., {@code int x = y + 3}</li>
	 * <li>OmpForInitExpression, e.g., x = 0</li>
	 * </ol>
	 * <p>
	 * IMPORTANT: Note that currently we do not consider following operations as
	 * assignments:
	 * <ul>
	 * <li>prefix and postfix ++ and -- operators.</li>
	 * <li>any short-hand assignment, e.g., x += y, or x -= y.</li>
	 * <li>any assignment within OmpForReinitExpression.</li>
	 * </ul>
	 * These expression forms would be considered as assignments only after
	 * ExpressionTree has been created for all the parsed Expressions.
	 *
	 * @return a list of {@link Assignment}s that may get executed, and are
	 *         lexically present within the owner node.
	 */
	public List<Assignment> getLexicalAssignments() {
		return AssignmentGetter.getLexicalAssignments(this.getNode());
	}

	/**
	 * For the owner node, this method returns a list of {@link Assignment} that may
	 * possibly happen during the
	 * execution of the owner node. This list will also contain assignments that may
	 * happen in any of the called
	 * procedures, and also the assignments of arguments to parameters, and returned
	 * values to return receiver. Hence,
	 * Note an assignment may represent any of the following form:
	 * <ol>
	 * <li>NonConditionalExpressions, e.g., {@code x = y + 3}</li>
	 * <li>Initializations, e.g., {@code int x = y + 3}</li>
	 * <li>Argument passing, e.g., <code>foo(y+3)</code>, for a method
	 * <code>void foo(int x) {...}</code></li>
	 * <li>Returned values, e.g., <code>x = bar();</code>, for a method
	 * <code>int bar() {...; return y + 3;}</code></li>
	 * <li>OmpForInitExpression, e.g., x = 0</li>
	 * </ol>
	 * <p>
	 * IMPORTANT: Note that currently we do not consider following operations as
	 * assignments:
	 * <ul>
	 * <li>prefix and postfix ++ and -- operators.</li>
	 * <li>any short-hand assignment, e.g., x += y, or x -= y.</li>
	 * <li>any assignment within OmpForReinitExpression.</li>
	 * </ul>
	 * These expression forms would be considered as assignments, only after
	 * ExpressionTree has been created for all the parsed Expressions.
	 *
	 * @return a list of {@link Assignment}s that may get executed, and are
	 *         lexically present within the owner node.
	 */
	public List<Assignment> getInterProceduralAssignments() {
		return AssignmentGetter.getInterProceduralAssignments(this.getNode());
	}

	/**
	 * Obtain string equivalent of the owner node, with the string of
	 * {@code changeSource} replaced by the string {@code
	 * replacementString} (if the replacement would actually be possible in the
	 * AST).
	 * <br>
	 * Note that no resulting changes are done to the AST of the owner node, or the
	 * {@code changeSource} node. Also note
	 * that this method is very different from a simple substring replacement.
	 *
	 * @param changeSource
	 *                          node whose string equivalent should be replaced with
	 *                          {@code changeDestination}, in the return value.
	 * @param replacementString
	 *                          string that should replace the string of
	 *                          {@code changeSource} in the return value.
	 *
	 * @return string of the owner node, with the string of {@code changeSource},
	 *         replaced by {@code changeDestination}.
	 */
	public String getNodeReplacedString(Node changeSource, String replacementString) {
		return StringGetter.getNodeReplacedString(this.getNode(), changeSource, replacementString);
	}

	public List<String> getComments() {
		if (this.getNode() instanceof BeginNode || this.getNode() instanceof EndNode) {
			return this.getNode().getParent().getInfo().getComments();
		}
		if (comments == null) {
			comments = new ArrayList<>();
		}
		return comments;
	}

	public static Commentor getDefaultCommentor() {
		if (defaultCommentor == null) {
			defaultCommentor = ((n) -> {
				String comments = "";
				for (String comment : n.getInfo().getComments()) {
					comments += comment + "\n";
				}
				return comments;
			});
		}
		return defaultCommentor;
	}

	/**
	 * Checks whether the associated node is connected to the main AST (AST of the
	 * program under consideration).
	 *
	 * @return {@code true}, if the node is connected to the main AST; {@code false}
	 *         otherwise.
	 */
	public boolean isConnectedToProgram() {
		Node encloser = Misc.getEnclosingNode(this.getNode(), TranslationUnit.class);
		if (encloser == null || encloser != Program.getRoot()) {
			return false;
		}
		return true;
	}

	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * Used to obtain the cell, if any, that has been written cleanly in this node.
	 * <br>
	 * We assume that a leaf CFG node performs a clean write to a cell, if it does
	 * not perform any other write, and if
	 * it does not read from that cell.
	 *
	 * @param n
	 *          node that has to be tested.
	 *
	 * @return cell that has been cleanly written in this node, otherwise null.
	 */
	public Cell getCleanWrite() {
		Node n = getNode();
		n = Misc.getCFGNodeFor(n);
		if (Misc.isCFGNonLeafNode(n)) {
			return null;
		}
		CellList writeList = n.getInfo().getWrites();
		CellList readList = n.getInfo().getReads();
		if (writeList.size() != 1) {
			return null;
		}
		if (Misc.doIntersect(writeList, readList)) {
			return null;
		}
		return writeList.get(0);
	}

	/**
	 * Used to check if this node is a clean-write to any cell.
	 * <br>
	 * We assume that a leaf CFG node performs a clean write to a cell, if it does
	 * not perform any other write, and if
	 * it does not read from that cell.
	 *
	 * @param n
	 *          node that has to be tested.
	 *
	 * @return true, if this node is a clean-write to the node n.
	 */
	public boolean isCleanWrite() {
		Node n = getNode();
		n = Misc.getCFGNodeFor(n);
		if (Misc.isCFGNonLeafNode(n)) {
			return false;
		}
		CellList writeList = n.getInfo().getWrites();
		CellList readList = n.getInfo().getReads();
		if (writeList.size() != 1) {
			return false;
		}
		if (Misc.doIntersect(writeList, readList)) {
			return false;
		}
		return true;
	}

	public BTNode getBTNode() {
		if (this.btNode == null) {
			this.btNode = BarrierTreeConstructor.getBarrierTreeFor(this.getNode());
		}
		return this.btNode;
	}

	/**
	 * List of locks that are taken before execution of the current node. <br>
	 * Note that the list is maintained in the
	 * increasing order of closeness of locks to the node -- the
	 * immediately-enclosing lock should be present as the
	 * last element of this list.
	 */
	@Deprecated
	private List<OldLock> lockSet;

	/**
	 * A relatively faster version of clone. However, note that this method works on
	 * an existing object. This method
	 * should be called first from the overridden setInfo method in the subclasses
	 * of Info.
	 *
	 * @param oldInfo
	 */
	@Deprecated
	public void setInfo(NodeInfo oldInfo) {
		/*
		 * Nodes that are equal, should have same idNumber.
		 */
		this.setIdNumber(oldInfo.getIdNumber());

		/*
		 * For each analysis, the data flow-facts should be copied from the
		 * oldInfo,
		 * while making a duplicate of a CFG node.
		 */
		if (Misc.isCFGNode(oldInfo.getNode())) {
			if (oldInfo.deprecated_flowFactsIN != null) {
				for (AnalysisName analysis : oldInfo.deprecated_flowFactsIN.keySet()) {
					this.setFlowFactIN(analysis, oldInfo.getFlowFactIN(analysis, null).getCopy());
				}
			}
			if (oldInfo.deprecated_flowFactsOUT != null) {
				for (AnalysisName analysis : oldInfo.deprecated_flowFactsOUT.keySet()) {
					this.setFlowFactOUT(analysis, oldInfo.getFlowFactOUT(analysis, null).getCopy());
				}
			}
		}

		if (oldInfo.cfgInfo != null) {
			this.cfgInfo = oldInfo.cfgInfo.getCopy(this.getNode());
		}

		if (phaseInfo != null) {
			this.phaseInfo = oldInfo.phaseInfo.getCopy(this.getNode());
		}

		// if (this.node instanceof DummyFlushDirective) {
		// this.interTaskEdgeList = oldInfo.interTaskEdgeList;
		// }

		this.regionInfo = oldInfo.regionInfo;
		this.reachingDefinitions = oldInfo.reachingDefinitions;
		this.usesInDU = oldInfo.usesInDU;
		this.defsInUD = oldInfo.defsInUD;
		this.liveOut = oldInfo.liveOut;
		this.flowEdgeDestList = oldInfo.flowEdgeDestList;
		this.flowEdgeSrcList = oldInfo.flowEdgeSrcList;
		this.antiEdgeDestList = oldInfo.antiEdgeDestList;
		this.antiEdgeSrcList = oldInfo.antiEdgeSrcList;
		this.outputEdgeDestList = oldInfo.outputEdgeDestList;
		this.outputEdgeSrcList = oldInfo.outputEdgeSrcList;
		this.callStatements = oldInfo.callStatements;
		this.incomplete = oldInfo.incomplete;
		this.lockSet = oldInfo.lockSet;
	}

	@Deprecated
	public List<OldLock> getLockSet() {
		if (lockSet != null) {
			return lockSet;
		}
		lockSet = new ArrayList<>();
		return lockSet;
	}

	/**
	 * Find whether the control can flow in and out of this AST node except from the
	 * beginning and end of it,
	 * respectively.
	 *
	 * @return true, if the control can flow in and out of this AST node except from
	 *         the beginning and end of it,
	 *         respectively.
	 *
	 * @see NodeInfo#isControlConfined()
	 * @deprecated
	 */
	@Deprecated
	public boolean deprecated_isControlConfined() {
		Set<Node> cfgContents = this.getCFGInfo().getLexicalCFGContents();

		/*
		 * Return false if there is a jump outside this node.
		 */
		for (Node constituent : cfgContents) {
			if (constituent instanceof JumpStatement) {
				for (Node succ : constituent.getInfo().getCFGInfo().getSuccessors()) {
					if (!cfgContents.contains(succ)) {
						return false;
					}
				}
			}
		}

		/*
		 * Return false, if there is a jump from outside this node.
		 */
		Set<LabeledStatement> astContents = Misc.getExactEnclosee(getNode(), LabeledStatement.class);
		for (Node labeledStmt : astContents) {
			Node cfgNode = Misc.getInternalFirstCFGNode(labeledStmt);
			for (Node pred : cfgNode.getInfo().getCFGInfo().getPredBlocks()) {
				if (!cfgContents.contains(pred)) {
					return false;
				}
			}
		}
		return true;
	}

	@Deprecated
	public List<Definition> deprecated_getDefinitionList() {
		// Deprecated_AllDefinitionGetter defGetter = new
		// Deprecated_AllDefinitionGetter();
		// getNode().accept(defGetter);
		// return defGetter.definitionList;
		return null;
	}

	@Deprecated
	private HashMap<AnalysisName, Deprecated_FlowFact> deprecated_flowFactsIN;
	@Deprecated
	private HashMap<AnalysisName, Deprecated_FlowFact> deprecated_flowFactsOUT;
	@Deprecated
	private HashMap<AnalysisName, Deprecated_FlowFact> deprecated_parallelFlowFactsIN;
	@Deprecated
	private HashMap<AnalysisName, Deprecated_FlowFact> deprecated_parallelFlowFactsOUT;

	@Deprecated
	private Set<Definition> reachingDefinitions;
	@Deprecated
	private CellMap<Set<Node>> usesInDU;
	@Deprecated
	private CellMap<Set<Node>> defsInUD;
	@Deprecated
	private HashMap<Node, CellSet> liveOut;
	@Deprecated
	private CellMap<Set<Node>> flowEdgeDestList;
	@Deprecated
	private CellMap<Set<Node>> flowEdgeSrcList;
	@Deprecated
	private CellMap<Set<Node>> antiEdgeDestList;
	@Deprecated
	private CellMap<Set<Node>> antiEdgeSrcList;
	@Deprecated
	private CellMap<Set<Node>> outputEdgeDestList;
	@Deprecated
	private CellMap<Set<Node>> outputEdgeSrcList;
	@Deprecated
	private List<CallSite> calledFunctions_old = null;

	/**
	 * Returns true if a parallel-flow-fact IN object exists for the owner node,
	 * corresponding to the specified analysis
	 * name.
	 *
	 * @param analysisName
	 *                     enumeration constant corresponding to the analysis for
	 *                     which the object has to be checked.
	 *
	 * @return true, if the object for parallel-flow-fact IN, corresponding to
	 *         <code>analysisName</code> is present for the owner node.
	 */
	@Deprecated
	public boolean hasParallelFlowFactIN(AnalysisName analysisName) {
		if (deprecated_parallelFlowFactsIN.containsKey(analysisName)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if a parallel-flow-fact OUT object exists for the owner node,
	 * corresponding to the specified
	 * analysis name.
	 *
	 * @param analysisName
	 *                     enumeration constant corresponding to the analysis for
	 *                     which the object has to be checked.
	 *
	 * @return true, if the object for parallel-flow-fact OUT, corresponding to
	 *         <code>analysisName</code> is present for the owner node.
	 */
	@Deprecated
	public boolean hasParalleleFlowFactOUT(AnalysisName analysisName) {
		if (deprecated_parallelFlowFactsOUT.containsKey(analysisName)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if a flow-fact IN object exists for the owner node,
	 * corresponding to the specified analysis name.
	 *
	 * @param analysisName
	 *                     enumeration constant corresponding to the analysis for
	 *                     which the object has to be checked.
	 *
	 * @return true, if the object for flow-fact IN, corresponding to
	 *         <code>analysisName</code> is present for the owner node.
	 */
	@Deprecated
	public boolean hasFlowFactIN(AnalysisName analysisName) {
		if (deprecated_flowFactsIN.containsKey(analysisName)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if a flow-fact OUT object exists for the owner node,
	 * corresponding to the specified analysis name.
	 *
	 * @param analysisName
	 *                     enumeration constant corresponding to the analysis for
	 *                     which the object has to be checked.
	 *
	 * @return true, if the object for flow-fact OUT, corresponding to
	 *         <code>analysisName</code> is present for the owner node.
	 */
	@Deprecated
	public boolean hasFlowFactOUT(AnalysisName analysisName) {
		if (deprecated_flowFactsOUT.containsKey(analysisName)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the parallel-flow-fact IN object stored with the key
	 * {@code analysisName} in {@code parallelFlowFactsIN}
	 * map of the owner node.
	 *
	 * @param analysisName
	 *                     Enumerator constant that corresponds to the analysis for
	 *                     which the parallel flow-fact IN object is
	 *                     needed.
	 * @param pass
	 *                     An object of the analysis of type
	 *                     {@code analysisName</code>}.
	 *
	 * @return The parallel-flow-fact IN object corresponding to the analysis
	 *         specified with <code>analysisName</code>
	 */
	@Deprecated
	public Deprecated_FlowFact getParallelFlowFactIN(AnalysisName analysisName,
			Deprecated_InterProceduralCFGPass<? extends Deprecated_FlowFact> pass) {
		if (deprecated_parallelFlowFactsIN == null) {
			deprecated_parallelFlowFactsIN = new HashMap<>();
		}
		if (!deprecated_parallelFlowFactsIN.containsKey(analysisName)) {
			deprecated_parallelFlowFactsIN.put(analysisName, pass.getTop());
		}
		return deprecated_parallelFlowFactsIN.get(analysisName);
	}

	/**
	 * Returns the parallel-flow-fact OUT object stored with the key
	 * {@code analysisName} in {@code
	 * parallelFlowFactsOUT} map of the owner node.
	 *
	 * @param analysisName
	 *                     Enumerator constant that corresponds to the analysis for
	 *                     which the parallel flow-fact OUT object is
	 *                     needed.
	 * @param pass
	 *                     An object of the analysis of type
	 *                     {@code analysisName</code>}.
	 *
	 * @return The parallel-flow-fact OUT object corresponding to the analysis
	 *         specified with <code>analysisName</code>
	 */
	@Deprecated
	public Deprecated_FlowFact getParallelFlowFactOUT(AnalysisName analysisName,
			Deprecated_InterProceduralCFGPass<? extends Deprecated_FlowFact> pass) {
		if (deprecated_parallelFlowFactsOUT == null) {
			deprecated_parallelFlowFactsOUT = new HashMap<>();
		}
		if (!deprecated_parallelFlowFactsOUT.containsKey(analysisName)) {
			deprecated_parallelFlowFactsOUT.put(analysisName, pass.getTop());
		}
		return deprecated_parallelFlowFactsOUT.get(analysisName);
	}

	/**
	 * Returns the flow-fact IN object stored with the key
	 * <code>analysisName</code> in <code>flowFactsIN</code> map of the owner
	 * node.
	 * <p>
	 * If there is no entry for {@code analysisName}, this method initializes the
	 * entry with {@code Top} element
	 * corresponding to the specified {@code pass}.
	 *
	 * @param analysisName
	 *                     Enumerator constant that corresponds to the analysis for
	 *                     which the flow-fact IN object is needed.
	 * @param pass
	 *                     An object of the analysis of type
	 *                     <code>analysisName</code>.
	 *
	 * @return The flow-fact IN object corresponding to the specified {@code pass},
	 *         else <code>Top</code> corresponding
	 *         to the specified {@code pass}.
	 */
	@Deprecated
	public Deprecated_FlowFact getFlowFactIN(AnalysisName analysisName,
			Deprecated_InterProceduralCFGPass<? extends Deprecated_FlowFact> pass) {
		if (deprecated_flowFactsIN == null) {
			deprecated_flowFactsIN = new HashMap<>();
		}
		if (!deprecated_flowFactsIN.containsKey(analysisName)) {
			if (this.copySourceNode != null) {
				Deprecated_FlowFact copiedFlowFactIN = copySourceNode.getInfo().getFlowFactIN(analysisName, pass)
						.getCopy();
				this.deprecated_flowFactsIN.put(analysisName, copiedFlowFactIN);
			} else {
				deprecated_flowFactsIN.put(analysisName, pass.getTop());
			}
		}
		return deprecated_flowFactsIN.get(analysisName);
	}

	/**
	 * Returns the flow-fact IN object stored with the key
	 * <code>analysisName</code> in <code>flowFactsIN</code> map of the owner
	 * node.
	 * <p>
	 * If this method returns <code>null</code>, one should use the
	 * <code>setFlowFactOUT</code> to add the flow-fact entry to the map.
	 *
	 * @param analysisName
	 *                     Enumerator constant that corresponds to the analysis for
	 *                     which the flow-fact IN object is needed.
	 *
	 * @return The flow-fact IN object corresponding to the specified {@code pass},
	 *         else <code>null</code>.
	 */
	@Deprecated
	public Deprecated_FlowFact getFlowFactIN(AnalysisName analysisName) {
		if (deprecated_flowFactsIN == null) {
			deprecated_flowFactsIN = new HashMap<>();
		}
		if (!deprecated_flowFactsIN.containsKey(analysisName)) {
			if (this.copySourceNode != null) {
				Deprecated_FlowFact copiedFlowFactIN = copySourceNode.getInfo().getFlowFactIN(analysisName).getCopy();
				this.deprecated_flowFactsIN.put(analysisName, copiedFlowFactIN);
			}
		}
		return deprecated_flowFactsIN.get(analysisName);
	}

	/**
	 * Returns the flow-fact OUT object stored with the key
	 * <code>analysisName</code> in <code>flowFactsOUT</code> map of the owner
	 * node.
	 * <p>
	 *
	 * @param analysisName
	 *                     Enumerator constant that corresponds to the analysis for
	 *                     which the flow-fact OUT object is needed.
	 * @param pass
	 *                     An object of the analysis of type
	 *                     <code>analysisName</code>.
	 *
	 * @return The flow-fact OUT object corresponding to the specified {@code pass},
	 *         else <code>Top</code> corresponding
	 *         to the specified {@code pass}.
	 */
	@Deprecated
	public Deprecated_FlowFact getFlowFactOUT(AnalysisName analysisName,
			Deprecated_InterProceduralCFGPass<? extends Deprecated_FlowFact> pass) {
		if (deprecated_flowFactsOUT == null) {
			deprecated_flowFactsOUT = new HashMap<>();
		}
		if (!deprecated_flowFactsOUT.containsKey(analysisName)) {
			if (this.copySourceNode != null) {
				Deprecated_FlowFact copiedFlowFactOUT = copySourceNode.getInfo().getFlowFactOUT(analysisName, pass)
						.getCopy();
				this.deprecated_flowFactsOUT.put(analysisName, copiedFlowFactOUT);
			} else {
				deprecated_flowFactsOUT.put(analysisName, pass.getTop());
			}
		}
		return deprecated_flowFactsOUT.get(analysisName);
	}

	/**
	 * Returns the flow-fact OUT object stored with the key
	 * <code>analysisName</code> in <code>flowFactsOUT</code> map of the owner
	 * node.
	 * <p>
	 *
	 * @param analysisName
	 *                     Enumerator constant that corresponds to the analysis for
	 *                     which the flow-fact OUT object is needed.
	 *
	 * @return The flow-fact OUT object corresponding to the specified {@code pass},
	 *         else {@code null}.
	 */
	@Deprecated
	public Deprecated_FlowFact getFlowFactOUT(AnalysisName analysisName) {
		if (deprecated_flowFactsOUT == null) {
			deprecated_flowFactsOUT = new HashMap<>();
		}
		if (!deprecated_flowFactsOUT.containsKey(analysisName)) {
			if (this.copySourceNode != null) {
				Deprecated_FlowFact copiedFlowFactOUT = copySourceNode.getInfo().getFlowFactOUT(analysisName).getCopy();
				this.deprecated_flowFactsOUT.put(analysisName, copiedFlowFactOUT);
			}
		}
		return deprecated_flowFactsOUT.get(analysisName);
	}

	/**
	 * Stores the provided flow-fact object, <code>flowFact</code>, with the
	 * specified analysis
	 * <code>analysisName</code>, as a key in the {@code parallelFlowFactsIN} map.
	 *
	 * @param analysisName
	 *                     Enumerator constant that corresponds to the analysis for
	 *                     which the parallel-flow-fact IN object has to be
	 *                     stored.
	 * @param flowFact
	 *                     The parallel-flow-fact IN object corresponding to the
	 *                     analysis specified with <code>analysisName</code>.
	 */
	@Deprecated
	public <F extends Deprecated_FlowFact> void setParallelFlowFactIN(AnalysisName analysisName, F flowFact) {
		if (deprecated_parallelFlowFactsIN == null) {
			deprecated_parallelFlowFactsIN = new HashMap<>();
		}
		deprecated_parallelFlowFactsIN.put(analysisName, flowFact);
	}

	/**
	 * Stores the provided flow-fact object, <code>flowFact</code>, with the
	 * specified analysis
	 * <code>analysisName</code>, as a key in the
	 * <code>flowFactsOUT</code> map.
	 *
	 * @param analysisName
	 *                     Enumerator constant that corresponds to the analysis for
	 *                     which the flow-fact OUT object has to be
	 *                     stored.
	 * @param flowFact
	 *                     The flow-fact OUT object corresponding to the analysis
	 *                     specified with <code>analysisName</code>.
	 */
	@Deprecated
	public <F extends Deprecated_FlowFact> void setParallelFlowFactOUT(AnalysisName analysis, F flowFact) {
		if (deprecated_parallelFlowFactsOUT == null) {
			deprecated_parallelFlowFactsOUT = new HashMap<>();
		}
		deprecated_parallelFlowFactsOUT.put(analysis, flowFact);
	}

	/**
	 * Stores the provided flow-fact object, <code>flowFact</code>, with the
	 * specified analysis
	 * <code>analysisName</code>, as a key in the
	 * <code>flowFactsIN</code> map.
	 *
	 * @param analysisName
	 *                     Enumerator constant that corresponds to the analysis for
	 *                     which the flow-fact IN object has to be stored.
	 * @param flowFact
	 *                     The flow-fact IN object corresponding to the analysis
	 *                     specified with <code>analysisName</code>.
	 */
	@Deprecated
	public <F extends Deprecated_FlowFact> void setFlowFactIN(AnalysisName analysisName, F flowFact) {
		if (deprecated_flowFactsIN == null) {
			deprecated_flowFactsIN = new HashMap<>();
		}
		deprecated_flowFactsIN.put(analysisName, flowFact);
	}

	/**
	 * Stores the provided flow-fact object, <code>flowFact</code>, with the
	 * specified analysis
	 * <code>analysisName</code>, as a key in the
	 * <code>flowFactsOUT</code> map.
	 *
	 * @param analysisName
	 *                     Enumerator constant that corresponds to the analysis for
	 *                     which the flow-fact OUT object has to be
	 *                     stored.
	 * @param flowFact
	 *                     The flow-fact OUT object corresponding to the analysis
	 *                     specified with <code>analysisName</code>.
	 */
	@Deprecated
	public <F extends Deprecated_FlowFact> void setFlowFactOUT(AnalysisName analysis, F flowFact) {
		if (deprecated_flowFactsOUT == null) {
			deprecated_flowFactsOUT = new HashMap<>();
		}
		deprecated_flowFactsOUT.put(analysis, flowFact);
	}

	/**
	 * Loses all references to the information related to particular analysis,
	 * thereby helping the GC.
	 *
	 * @param type
	 *             Analysis related to which the complete information has to be
	 *             deleted.
	 */
	@Deprecated
	public void removeParallelFlowFact(AnalysisName type) {
		if (deprecated_parallelFlowFactsOUT == null) {
			return;
		}
		deprecated_parallelFlowFactsOUT.remove(type);
		if (deprecated_parallelFlowFactsIN == null) {
			return;
		}
		deprecated_parallelFlowFactsIN.remove(type);
	}

	/**
	 * Loses all references to the information related to particular analysis,
	 * thereby helping the GC.
	 *
	 * @param type
	 *             Analysis related to which the complete information has to be
	 *             deleted.
	 */
	@Deprecated
	public void removeFlowFact(AnalysisName type) {
		if (deprecated_flowFactsOUT == null) {
			return;
		}
		deprecated_flowFactsOUT.remove(type);
	}

	@Deprecated
	public Set<Definition> deprecated_getReachingDefinitions() {
		if (reachingDefinitions == null) {
			reachingDefinitions = new HashSet<>();
		}
		return reachingDefinitions;
	}

	@Deprecated
	public Set<Definition> deprecated_readReachingDefinitions() {
		return reachingDefinitions;
	}

	@Deprecated
	public CellMap<Set<Node>> getUsesInDU() {
		if (usesInDU == null) {
			usesInDU = new CellMap<>();
		}
		return usesInDU;
	}

	@Deprecated
	public CellMap<Set<Node>> readUsesInDU() {
		return usesInDU;
	}

	@Deprecated
	public CellMap<Set<Node>> getDefsInUD() {
		if (defsInUD == null) {
			defsInUD = new CellMap<>();
		}
		return defsInUD;
	}

	@Deprecated
	public CellMap<Set<Node>> readDefsInUD() {
		return defsInUD;
	}

	@Deprecated
	public HashMap<Node, CellSet> getLiveOut() {
		if (liveOut == null) {
			liveOut = new HashMap<>();
		}
		return liveOut;
	}

	@Deprecated
	public CellSet getLiveOutCells() {
		CellSet liveCells = new CellSet();
		for (Node keySucc : getLiveOut().keySet()) {
			liveCells = Misc.setUnion(liveCells, getLiveOut().get(keySucc));
		}
		return liveCells;
	}

	@Deprecated
	public CellMap<Set<Node>> getFlowEdgeDestList() {
		if (flowEdgeDestList == null) {
			flowEdgeDestList = new CellMap<>();
		}
		return flowEdgeDestList;
	}

	@Deprecated
	public CellMap<Set<Node>> readFlowEdgeDestList() {
		return flowEdgeDestList;
	}

	@Deprecated
	public CellMap<Set<Node>> getFlowEdgeSrcList() {
		if (flowEdgeSrcList == null) {
			flowEdgeSrcList = new CellMap<>();
		}
		return flowEdgeSrcList;
	}

	@Deprecated
	public CellMap<Set<Node>> readFlowEdgeSrcList() {
		return flowEdgeSrcList;
	}

	@Deprecated
	public CellMap<Set<Node>> getAntiEdgeDestList() {
		if (antiEdgeDestList == null) {
			antiEdgeDestList = new CellMap<>();
		}
		return antiEdgeDestList;
	}

	@Deprecated
	public CellMap<Set<Node>> readAntiEdgeDestList() {
		return antiEdgeDestList;
	}

	@Deprecated
	public CellMap<Set<Node>> getAntiEdgeSrcList() {
		if (antiEdgeSrcList == null) {
			antiEdgeSrcList = new CellMap<>();
		}
		return antiEdgeSrcList;
	}

	@Deprecated
	public CellMap<Set<Node>> readAntiEdgeSrcList() {
		return antiEdgeSrcList;
	}

	@Deprecated
	public CellMap<Set<Node>> getOutputEdgeDestList() {
		if (outputEdgeDestList == null) {
			outputEdgeDestList = new CellMap<>();
		}
		return outputEdgeDestList;
	}

	@Deprecated
	public CellMap<Set<Node>> readOutputEdgeDestList() {
		return outputEdgeDestList;
	}

	@Deprecated
	public CellMap<Set<Node>> getOutputEdgeSrcList() {
		if (outputEdgeSrcList == null) {
			outputEdgeSrcList = new CellMap<>();
		}
		return outputEdgeSrcList;
	}

	@Deprecated
	public CellMap<Set<Node>> readOutputEdgeSrcList() {
		return outputEdgeSrcList;
	}

	/**
	 * @return
	 *
	 * @deprecated
	 */
	@Deprecated
	public List<CallSite> getCallSites() {
		if (calledFunctions_old == null) {
			CallSiteGetter callGetter = new CallSiteGetter();
			getNode().accept(callGetter);
			calledFunctions_old = callGetter.callSiteList;
		}
		return calledFunctions_old;
	}

	/**
	 * @return
	 *
	 * @deprecated
	 */
	@Deprecated
	public boolean hasCallSites() {
		if (getCallSites().isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public void resetCallSites() {
		calledFunctions_old = null;
		getCallSites();
	}

}
