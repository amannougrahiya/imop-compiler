/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.link.autoupdater;

import imop.ast.annotation.Label;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis.PointsToGlobalState;
import imop.lib.analysis.flowanalysis.generic.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.analysis.mhp.incMHP.EndPhasePoint;
import imop.lib.analysis.mhp.incMHP.MHPAnalyzer;
import imop.lib.analysis.mhp.incMHP.NodePhaseInfo;
import imop.lib.analysis.solver.ConstraintsGenerator;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cfg.info.ProgramElementExactCaches;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.CollectorVisitor;
import imop.lib.util.Misc;
import imop.parser.Program;
import imop.parser.Program.UpdateCategory;

import java.util.*;

public class AutomatedUpdater {

	public static int reinitMHPCounter = 0;

	public static enum AutoUpdateFlag {
		ENABLED, DISABLED
	}

	;

	public static Stack<AutoUpdateFlag> autoUpdateFlagStack = new Stack<>();

	/**
	 * This updater is used as a target for various automated updater for addition
	 * and removal of node {@code n} from
	 * its CFG.
	 *
	 * @author aman
	 */
	@FunctionalInterface
	public static interface Updater {
		public void apply(Node n);
	}

	/**
	 * Flushes various cached data that we cannot rely upon due to program update.
	 * <br>
	 * Note that this is the most naive
	 * (and inefficient) solution to maintain consistency, for any given caching.
	 */
	public static void flushCaches() {
		Program.rememberInitialPhasesIfRequired();
		CoExistenceChecker.flushCoExistenceCaches();
		Program.invalidateReversePostorder();
		CFGInfo.isSCCStale = true;
		ConstraintsGenerator.reinitInductionSet();
		// Misc.isCalledFromMethod("clientAutoUpdateHomeostasis");
	}

	public static int hasBeenOtimized = 0;
	private static boolean localFlagForDebugging = true;

	/**
	 * List of updates that should be performed to make various representations of
	 * the program consistent, upon removal
	 * of a node. Note that these updates need to be called before the CFG and AST
	 * links have actually been removed for
	 * the old node.
	 */
	protected static List<Updater> updateSetForRemoval;

	/**
	 * List of updates that should be performed to make various representations of
	 * the program consistent, upon addition
	 * of a node. Note that these updates need to be called after the CFG and AST
	 * links have actually been created for
	 * the new node.
	 */
	protected static List<Updater> updateSetForAddition;

	/**
	 * Initializes the set of those updates which are called upon insertion and
	 * removal of various types of CFG component nodes.
	 */
	static {
		updateSetForRemoval = new ArrayList<>();
		updateSetForAddition = new ArrayList<>();

		// Update of flow-facts for removal isn't done here, since we need the actual
		// removal done before rerunning any analysis.
		// updateSetForRemoval.add(AutomatedUpdator::updateFlowFactsForRemoval);

		updateSetForRemoval.add(ProgramElementExactCaches::considerNodeRemoval);
		updateSetForRemoval.add(AutomatedUpdater::performResetOfMHPUponRemoval);
		updateSetForRemoval.add(AutomatedUpdater::updateCallStatementsUponRemoval);
		updateSetForRemoval.add(AutomatedUpdater::updateSymbolsAtDummyFlushesUponRemoval);
		updateSetForRemoval.add(AutomatedUpdater::setPointsToInstabilityFlagIfRequiredForRemoval);
		// updateSetForRemoval.add(AutomatedUpdater::updateSymbolTableOnNodeRemoval);
		updateSetForRemoval.add(AutomatedUpdater::modelRemovalOfLabels);
		// updateSetForRemoval.add(AutomatedUpdater::invalidateEnclosingSymbolSets);

		updateSetForAddition.add(ProgramElementExactCaches::considerNodeAddition);
		// updateSetForAddition.add(AutomatedUpdater::invalidateEnclosingSymbolSets);
		updateSetForAddition.add(AutomatedUpdater::modelAdditionOfLabels);
		// updateSetForAddition.add(AutomatedUpdater::updateSymbolTableOnNodeAddition);
		updateSetForAddition.add(AutomatedUpdater::updateSymbolsAtDummyFlushesUponAddition);
		updateSetForAddition.add(AutomatedUpdater::updateCallStatementsUponAddition);
		updateSetForAddition.add(AutomatedUpdater::performInitOfMHPUponAddition); // TODO: Verify this order for various
																					// cases.
		updateSetForAddition.add(AutomatedUpdater::updatePhaseAndInterTaskEdgesUponAddition);
		updateSetForAddition.add(AutomatedUpdater::setPointsToInstabilityFlagIfRequiredForAddition);

		/*
		 * The following method, updateFlowFactsForAddition, must be called
		 * after all the inter- and intra-procedural/task edges have been
		 * updated.
		 */
		updateSetForAddition.add(AutomatedUpdater::updateFlowFactsForAddition);
	}

	public static void stabilizePTAInCPModes() {
		if (Program.updateCategory != UpdateCategory.CPINV && Program.updateCategory != UpdateCategory.CPUPD) {
			return;
		}
		for (FlowAnalysis<?> analysisHandle : FlowAnalysis.getAllAnalyses().values()) {
			if (Program.updateCategory == UpdateCategory.CPINV) {
				BeginPhasePoint.stabilizeStaleBeginPhasePoints();
				if (analysisHandle.stateIsInvalid()) {
					analysisHandle.markStateToBeValid();
					AutomatedUpdater.reinitIDFA(analysisHandle);
				}
			} else {
				assert (Program.updateCategory == UpdateCategory.CPUPD);
				// if (analysisHandle.stateIsInvalid()) {
				if (analysisHandle.stateIsInvalid() && (analysisHandle.getAnalysisName() != AnalysisName.POINTSTO
						|| !PointsToAnalysis.isHeuristicEnabled)) {
					BeginPhasePoint.stabilizeStaleBeginPhasePoints();
					analysisHandle.markStateToBeValid();
					if (analysisHandle.getAnalysisName() == AnalysisName.POINTSTO) {
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

	/**
	 * Given a node that has been added to the program, this method sets the
	 * points-to instability flag, if
	 * required.<br>
	 * Furthermore, if the instability flag is not set, this method also sets the
	 * points-to IN and OUT
	 * flow-facts of all the leaf nodes within {@code affectedNode} to be same as
	 * that of the meet of OUT of
	 * predecessors of the entry point of the node.
	 *
	 * @param affectedNode
	 *                     a node that has been added, to the program.
	 */
	public static void setPointsToInstabilityFlagIfRequiredForAddition(Node affectedNode) {
		if (AutomatedUpdater.localFlagForDebugging) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.STALE;
			return;
		}

		/*
		 * If the node is not control-confined (ignoring all the call-sites),
		 * then mark points-to information as unstable and return.
		 */
		if (!affectedNode.getInfo().isControlConfined()) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.STALE;
			return;
		}

		/*
		 * If the node may write to any cell of pointer-type, then we should
		 * mark points-to as unstable. (In this check, we should consider only
		 * those CFG leaf nodes of affectedNode which are either present
		 * lexically inside, or are present in the called-procedures.)
		 */
		if (affectedNode.getInfo().mayUpdatePointsTo()) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.STALE;
			return;
		}

		for (Node leaf : affectedNode.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			if (leaf instanceof BarrierDirective || leaf instanceof FlushDirective) {
				PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.STALE;
				return;
			}
		}

		/*
		 * Now, we should ensure that IN and OUT of all the nodes in
		 * affectedNode should be set to the meet of OUT of all predecessors of
		 * the entry point of affectedNode.
		 * Since none of the nodes can affected points-to, we should use same
		 * object for IN as well as OUT.
		 */
		FlowAnalysis<?> analysisHandle = FlowAnalysis.getAllAnalyses().get(AnalysisName.POINTSTO);
		if (analysisHandle != null) {
			analysisHandle.cloneFlowFactsToAllWithin(affectedNode);
		}

		return;
	}

	/**
	 * Given a node that has to be removed from the program, this method sets the
	 * points-to instability flag, if
	 * required.
	 *
	 * @param affectedNode
	 *                     a node that has to be removed from the program.
	 */
	public static void setPointsToInstabilityFlagIfRequiredForRemoval(Node affectedNode) {
		if (AutomatedUpdater.localFlagForDebugging) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.STALE;
			return;
		}

		/*
		 * If the node is not control-confined (ignoring all the call-sites),
		 * then mark points-to information as unstable and return.
		 */
		if (!affectedNode.getInfo().isControlConfined()) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.STALE;
			return;
		}

		/*
		 * If the node may write to any cell of pointer-type, then we should
		 * mark points-to as unstable. (In this check, we should consider only
		 * those CFG leaf nodes of affectedNode which are either present
		 * lexically inside, or are present in the called-procedures.)
		 */
		if (affectedNode.getInfo().mayUpdatePointsTo()) {
			PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.STALE;
			return;
		}

		for (Node leaf : affectedNode.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			if (leaf instanceof BarrierDirective || leaf instanceof FlushDirective) {
				PointsToAnalysis.stateOfPointsTo = PointsToGlobalState.STALE;
				return;
			}
		}

		// There is no point of wasting time in flushing of IN and OUT of removed nodes.
		return;
	}

	/**
	 * Invokes the driver method that performs automated updates for insertion of
	 * the node {@code insertedNode}.
	 *
	 * @param insertedNode
	 *                     a newly inserted node.
	 */
	public static void updateInformationForAddition(Node insertedNode) {
		insertedNode = Misc.getCFGNodeFor(insertedNode);
		updateInformation(insertedNode, updateSetForAddition);
	}

	/**
	 * Invokes the driver module that performs automated updates for removal of the
	 * node {@code removedNode}.
	 *
	 * @param removedNode
	 *                    node that has to be removed.
	 */
	public static void updateInformationForRemoval(Node removedNode) {
		removedNode = Misc.getCFGNodeFor(removedNode);
		updateInformation(removedNode, updateSetForRemoval);
	}

	/**
	 * Driver function that performs call-backs of various update methods specified
	 * in the {@code interSet}, after a
	 * node {@code node} has been inserted in or removed from the CFG.
	 *
	 * @param node
	 *                 newly inserted, or to be removed, node.
	 * @param interSet
	 *                 a set of lambda expressions (rather, method references),
	 *                 which need to be called on the {@code node}.
	 */
	public static void updateInformation(Node node, List<Updater> interSet) {
		node = Misc.getCFGNodeFor(node);
		for (Updater updater : interSet) {
			updater.apply(node);
		}
	}

	/**
	 * This methods sets the invalidation flags for the appropriate read/write sets
	 * of appropriate dummy-flushes when
	 * {@code oldNode} has been removed from a certain position in the CFG.
	 *
	 * @param oldNode
	 *                a node that has to be removed from a certain point in the CFG.
	 *                Note that this method should be called
	 *                before the node has actually been removed.
	 */
	public static void updateSymbolsAtDummyFlushesUponRemoval(Node oldNode) {
		oldNode = Misc.getCFGNodeFor(oldNode);
		Node backwardStartPoint;
		Node forwardStartPoint;
		if (Misc.isCFGLeafNode(oldNode)) {
			if (oldNode instanceof DummyFlushDirective) {
				DummyFlushDirective dummy = (DummyFlushDirective) oldNode;
				dummy.invalidReadCellSet = true;
				dummy.invalidWrittenCellSet = true;
			} else {
				// if (oldNode.getInfo().getSharedAccesses().isEmpty()) {
				// return;
				// }
			}
			backwardStartPoint = forwardStartPoint = oldNode;
		} else {
			// Invalidate the written-before and read-after symbol sets of all the dummy
			// flushes
			// in the region of oldNode.
			BeginNode beginNode = oldNode.getInfo().getCFGInfo().getNestedCFG().getBegin();
			EndNode endNode = oldNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			for (Node successorInNewArea : CollectorVisitor.collectNodesIntraTaskForward(beginNode, new HashSet<>(),
					(n) -> (n == endNode))) {
				if (successorInNewArea instanceof DummyFlushDirective) {
					DummyFlushDirective dummy = (DummyFlushDirective) successorInNewArea;
					dummy.invalidReadCellSet = true;
					dummy.invalidWrittenCellSet = true;
				}
			}
			backwardStartPoint = beginNode;
			forwardStartPoint = endNode;
		}

		// Invalidate the read-after symbol sets of all the dummy flushes
		// that lie before the newNode in a flush-free path.
		Set<Node> previousDummies = new HashSet<>();
		CollectorVisitor.collectNodesIntraTaskBackward(backwardStartPoint, previousDummies,
				(n) -> (n instanceof DummyFlushDirective));
		for (Node previousDummy : previousDummies) {
			DummyFlushDirective dummy = (DummyFlushDirective) previousDummy;
			dummy.invalidReadCellSet = true;
		}

		// Invalidate the written-before symbol sets of all the dummy flushes
		// that lie after the newNode in a flush-free path.
		Set<Node> futureDummies = new HashSet<>();
		CollectorVisitor.collectNodesIntraTaskForward(forwardStartPoint, futureDummies,
				(n) -> (n instanceof DummyFlushDirective));
		for (Node futureDummy : futureDummies) {
			DummyFlushDirective dummy = (DummyFlushDirective) futureDummy;
			dummy.invalidWrittenCellSet = true;
		}
	}

	/**
	 * This method sets the invalidation flags for the appropriate read/write sets
	 * of appropriate dummy-flushes when
	 * {@code newNode} has been inserted at certain position in the CFG.
	 *
	 * @param newNode
	 *                a node that has been inserted at a certain point in the CFG.
	 */
	public static void updateSymbolsAtDummyFlushesUponAddition(Node newNode) {
		newNode = Misc.getCFGNodeFor(newNode);
		Node backwardStartPoint;
		Node forwardStartPoint;
		if (Misc.isCFGLeafNode(newNode)) {
			if (newNode instanceof DummyFlushDirective) {
				DummyFlushDirective dummy = (DummyFlushDirective) newNode;
				dummy.invalidReadCellSet = true;
				dummy.invalidWrittenCellSet = true;
				backwardStartPoint = forwardStartPoint = newNode;
			} else {
				backwardStartPoint = forwardStartPoint = newNode;
				// CellSet newSharedReads = newNode.getInfo().getSharedReads();
				// CellSet newSharedWrites = newNode.getInfo().getSharedWrites();
				// if (!newSharedReads.isEmpty()) {
				// // Update the read-after symbol sets of all the dummy flushes
				// // that lie before the newNode in a flush-free path.
				// Set<Node> previousDummies = new HashSet<>();
				// CollectorVisitor.collectNodesIntraTaskBackward(newNode, previousDummies,
				// (n) -> (n instanceof DummyFlushDirective));
				// for (Node previousDummy : previousDummies) {
				// DummyFlushDirective dummy = (DummyFlushDirective) previousDummy;
				// dummy.getReadAfterCells().addAll(newSharedReads);
				// }
				// }
				// if (!newSharedWrites.isEmpty()) {
				// // Update the written-before symbol sets of all the dummy flushes
				// // that lie after the newNode in a flush-free path.
				// Set<Node> futureDummies = new HashSet<>();
				// CollectorVisitor.collectNodesIntraTaskForward(newNode, futureDummies,
				// (n) -> (n instanceof DummyFlushDirective));
				// for (Node futureDummy : futureDummies) {
				// DummyFlushDirective dummy = (DummyFlushDirective) futureDummy;
				// dummy.getWrittenBeforeCells().addAll(newSharedWrites);
				// }
				// }
				// return;
			}
		} else {
			// Invalidate the written-before and read-after symbol sets of all the dummy
			// flushes
			// in the region of newNode.
			BeginNode beginNode = newNode.getInfo().getCFGInfo().getNestedCFG().getBegin();
			EndNode endNode = newNode.getInfo().getCFGInfo().getNestedCFG().getEnd();
			for (Node successorInNewArea : CollectorVisitor.collectNodesIntraTaskForward(beginNode, new HashSet<>(),
					(n) -> (n == endNode))) {
				if (successorInNewArea instanceof DummyFlushDirective) {
					DummyFlushDirective dummy = (DummyFlushDirective) successorInNewArea;
					dummy.invalidReadCellSet = true;
					dummy.invalidWrittenCellSet = true;
				}
			}
			backwardStartPoint = beginNode;
			forwardStartPoint = endNode;
		}

		// Invalidate the read-after symbol sets of all the dummy flushes
		// that lie before the newNode in a flush-free path.
		Set<Node> previousDummies = new HashSet<>();
		CollectorVisitor.collectNodesIntraTaskBackward(backwardStartPoint, previousDummies,
				(n) -> (n instanceof DummyFlushDirective));
		for (Node previousDummy : previousDummies) {
			DummyFlushDirective dummy = (DummyFlushDirective) previousDummy;
			dummy.invalidReadCellSet = true;
		}

		// Invalidate the written-before symbol sets of all the dummy flushes
		// that lie after the newNode in a flush-free path.
		Set<Node> futureDummies = new HashSet<>();
		CollectorVisitor.collectNodesIntraTaskForward(forwardStartPoint, futureDummies,
				(n) -> (n instanceof DummyFlushDirective));
		for (Node futureDummy : futureDummies) {
			DummyFlushDirective dummy = (DummyFlushDirective) futureDummy;
			dummy.invalidWrittenCellSet = true;
		}
	}

	/**
	 * This method traverses forward in the inter-procedural CFG, and invalidates
	 * the written-before symbol set of first
	 * encountered DummyFlushes on all paths.
	 *
	 * @param nodeSet
	 *                nodes starting which the invalidation traversal has to begin.
	 */
	public static void invalidateWriteBeforeCellsInSuccessorDummyFlushesOf(Set<Node> nodeSet) {
		for (Node node : nodeSet) {
			node = Misc.getCFGNodeFor(node);
			if (node instanceof DummyFlushDirective) {
				DummyFlushDirective dummy = (DummyFlushDirective) node;
				dummy.invalidReadCellSet = true;
				dummy.invalidWrittenCellSet = true;
				continue;
			}

			// Invalidate the written-before symbol sets of all the dummy flushes
			// that lie after the node in a flush-free path.
			Set<Node> futureDummies = new HashSet<>();
			CollectorVisitor.collectNodesIntraTaskForward(node, futureDummies,
					(n) -> (n instanceof DummyFlushDirective));
			for (Node futureDummy : futureDummies) {
				DummyFlushDirective dummy = (DummyFlushDirective) futureDummy;
				dummy.invalidWrittenCellSet = true;
			}
		}
	}

	/**
	 * This method traverses backward in the inter-procedural CFG, and invalidates
	 * the read-after symbol set of first
	 * encountered DummyFlushes on all paths.
	 *
	 * @param nodeSet
	 *                nodes starting which the invalidation traversal has to begin.
	 */
	public static void invalidateReadAfterCellsInPredecessorDummyFlushesOf(Set<Node> nodeSet) {
		for (Node node : nodeSet) {
			node = Misc.getCFGNodeFor(node);
			if (node instanceof DummyFlushDirective) {
				DummyFlushDirective dummy = (DummyFlushDirective) node;
				dummy.invalidReadCellSet = true;
				dummy.invalidWrittenCellSet = true;
				continue;
			}

			// Invalidate the read-after symbol sets of all the dummy flushes
			// that lie before the node in a flush-free path.
			Set<Node> previousDummies = new HashSet<>();
			CollectorVisitor.collectNodesIntraTaskBackward(node, previousDummies,
					(n) -> (n instanceof DummyFlushDirective));
			for (Node previousDummy : previousDummies) {
				DummyFlushDirective dummy = (DummyFlushDirective) previousDummy;
				dummy.invalidReadCellSet = true;
			}
		}
	}

	/**
	 * This methods sets the invalidation flags for the appropriate read/write sets
	 * of appropriate dummy-flushes when
	 * label list has been updated for {@code node}.
	 * <br>
	 * Note that this method should be called while the label to be removed or added
	 * are present in the {@code node}.
	 *
	 * @param node
	 *             node to be processed.
	 */
	@Deprecated
	public static void updateSymbolsAtDummyFlushesUponLabelUpdate(Node node) {
		node = Misc.getCFGNodeFor(node);
		Node backwardStartPoint;
		Node forwardStartPoint;
		if (Misc.isCFGLeafNode(node)) {
			if (node instanceof DummyFlushDirective) {
				DummyFlushDirective dummy = (DummyFlushDirective) node;
				dummy.invalidReadCellSet = true;
				dummy.invalidWrittenCellSet = true;
			}
			backwardStartPoint = forwardStartPoint = node;
		} else {
			// Invalidate the written-before and read-after symbol sets of all the dummy
			// flushes
			// in the region of node.
			BeginNode beginNode = node.getInfo().getCFGInfo().getNestedCFG().getBegin();
			EndNode endNode = node.getInfo().getCFGInfo().getNestedCFG().getEnd();
			for (Node successorInNewArea : CollectorVisitor.collectNodesIntraTaskForward(beginNode, new HashSet<>(),
					(n) -> (n == endNode))) {
				if (successorInNewArea instanceof DummyFlushDirective) {
					DummyFlushDirective dummy = (DummyFlushDirective) successorInNewArea;
					dummy.invalidReadCellSet = true;
					dummy.invalidWrittenCellSet = true;
				}
			}
			backwardStartPoint = beginNode;
			forwardStartPoint = endNode;
		}

		// Invalidate the read-after symbol sets of all the dummy flushes
		// that lie before the node in a flush-free path.
		Set<Node> previousDummies = new HashSet<>();
		CollectorVisitor.collectNodesIntraTaskBackward(backwardStartPoint, previousDummies,
				(n) -> (n instanceof DummyFlushDirective));
		for (Node previousDummy : previousDummies) {
			DummyFlushDirective dummy = (DummyFlushDirective) previousDummy;
			dummy.invalidReadCellSet = true;
		}

		// Invalidate the written-before symbol sets of all the dummy flushes
		// that lie after the newNode in a flush-free path.
		Set<Node> futureDummies = new HashSet<>();
		CollectorVisitor.collectNodesIntraTaskForward(forwardStartPoint, futureDummies,
				(n) -> (n instanceof DummyFlushDirective));
		for (Node futureDummy : futureDummies) {
			DummyFlushDirective dummy = (DummyFlushDirective) futureDummy;
			dummy.invalidWrittenCellSet = true;
		}
	}

	/**
	 * Updates the set of call statements present in various non-leaf nodes that
	 * would lexically enclose {@code node}
	 * after its insertion in the CFG.
	 *
	 * @param node
	 *             newly inserted node.
	 */
	public static void updateCallStatementsUponAddition(Node node) {
		node = Misc.getCFGNodeFor(node);
		if (Misc.isCFGLeafNode(node)) {
			return;
		}
		// Old code:
		// if (node instanceof CallStatement) {
		// CallStatement insertedCallStmt = (CallStatement) node;
		// for (Node encloserNonLeaf : node.getInfo().getAllNonLeafEnclosersExclusive())
		// {
		// assert (encloserNonLeaf != null);
		// encloserNonLeaf.getInfo().getCallStatements().add(insertedCallStmt);
		// }
		// } else {
		// return;
		// }
		Set<Node> enclosingNonLeafNodes = node.getInfo().getAllLexicalNonLeafEnclosersExclusive();
		for (Node encloserNonLeaf : enclosingNonLeafNodes) {
			for (CallStatement insertedCallStmt : node.getInfo().getLexicallyEnclosedCallStatements()) {
				encloserNonLeaf.getInfo().getLexicallyEnclosedCallStatements().add(insertedCallStmt);
			}
		}
	}

	/**
	 * Updates the accessible symbol sets for all lexically enclosing scopes.
	 *
	 * @param node
	 *             node that has to be removed.
	 */
	public static void invalidateEnclosedSymbolSets(Node node) {
		for (Scopeable internalScope : node.getInfo().getLexicallyEnclosedScopesInclusive()) {
			if (internalScope instanceof CompoundStatement) {
				((CompoundStatement) internalScope).getInfo().invalidateCellSets();
			} else if (internalScope instanceof FunctionDefinition) {
				((FunctionDefinition) internalScope).getInfo().invalidateCellSets();
			} else if (internalScope instanceof TranslationUnit) {
				((TranslationUnit) internalScope).getInfo().invalidateCellSets();
			}
		}
	}

	/**
	 * Updates the set of call statements present in various non-leaf nodes that
	 * enclose {@code node} before it gets
	 * removed from the CFG.
	 *
	 * @param node
	 *             node that has to be removed.
	 */
	public static void updateCallStatementsUponRemoval(Node node) {
		node = Misc.getCFGNodeFor(node);
		if (Misc.isCFGLeafNode(node)) {
			return;
		}
		// Old code:
		// if (node instanceof CallStatement) {
		// CallStatement removedCallStmt = (CallStatement) node;
		// for (Node encloserNonLeaf : node.getInfo().getAllNonLeafEnclosersExclusive())
		// {
		// encloserNonLeaf.getInfo().getCallStatements().remove(removedCallStmt);
		// }
		// } else {
		// return;
		// }
		Set<Node> enclosingNonLeafNodes = node.getInfo().getAllLexicalNonLeafEnclosersExclusive();
		for (Node encloserNonLeaf : enclosingNonLeafNodes) {
			for (CallStatement removedCallStmt : node.getInfo().getLexicallyEnclosedCallStatements()) {
				encloserNonLeaf.getInfo().removeLexicallyEnclosedCallStatement(removedCallStmt);
			}
		}
	}

	/**
	 * Updates flow-fact information for various analyses, by rerunning the
	 * analyses, starting with {@code newNode}.
	 * Note that this method is called after {code newNode} has been inserted in the
	 * CFG, and after all the inter- and
	 * intra-procedural edge updates have been taken care of.
	 *
	 * @param newNode
	 *                newly inserted node, starting which various analyses have to
	 *                rerun.
	 */
	public static void updateFlowFactsForAddition(Node newNode) {
		Node node = Misc.getCFGNodeFor(newNode);
		if (Program.updateCategory == UpdateCategory.EGINV || Program.updateCategory == UpdateCategory.LZINV
				|| Program.updateCategory == UpdateCategory.CPINV) {
			AutomatedUpdater.updateFlowFactsForward(null);
			AutomatedUpdater.updateFlowFactsBackward(null);
			return;
		}
		Set<Node> newNodeSetForward = new HashSet<>();
		Set<Node> newNodeSetBackward = new HashSet<>();
		// Step 1: Obtain the set of nodes starting which we need to rerun the analyses.
		if (Misc.isCFGLeafNode(node)) {
			node.getInfo().removeAllAnalysisInformation();
			newNodeSetForward.add(node);
			newNodeSetBackward.add(node);
		} else {
			for (Node tempNode : node.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
				tempNode.getInfo().removeAllAnalysisInformation();
			}
			newNodeSetForward.addAll(node.getInfo().getEntryNodes());
			newNodeSetBackward.addAll(node.getInfo().getExitNodes());
		}

		// Step 2: Rerun the forward analyses.
		AutomatedUpdater.updateFlowFactsForward(newNodeSetForward);
		// HashMap<AnalysisName, InterThreadForwardIDFA<?>> analysisMapForward =
		// InterThreadForwardIDFA
		// .getForwardAnalyses();
		// for (InterThreadForwardIDFA<?> analysis : analysisMapForward.values()) {
		// analysis.run(newNodeSet);
		// }

		// Step 3: Rerun the backward analyses.
		AutomatedUpdater.updateFlowFactsBackward(newNodeSetBackward);
		// HashMap<AnalysisName, InterThreadBackwardIDFA<?>> analysisMapBackward =
		// InterThreadBackwardIDFA
		// .getBackwardAnalyses();
		// for (InterThreadBackwardIDFA<?> analysis : analysisMapBackward.values()) {
		// analysis.run(newNodeSet);
		// }
	}

	/**
	 * Updates flow-fact information for various forward analyses, by rerunning the
	 * analyses on the set of nodes in
	 * {@code nodeSet}.
	 *
	 * @param nodeSet
	 *                set of nodes starting which forward analyses have to be rerun.
	 */
	public static void updateFlowFactsForward(Set<Node> nodeSet) {
		if (Program.updateCategory == UpdateCategory.EGINV) {
			assert (nodeSet == null || nodeSet.isEmpty());
			for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
				if (analysis instanceof InterThreadForwardCellularAnalysis
						|| analysis instanceof InterThreadForwardNonCellularAnalysis) {
					AutomatedUpdater.reinitIDFA(analysis);
				}
			}
		} else if (Program.updateCategory == UpdateCategory.LZINV || Program.updateCategory == UpdateCategory.CPINV) {
			assert (nodeSet == null || nodeSet.isEmpty());
			for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
				if (analysis
						.getAnalysisName() == (Program.useInterProceduralPredicateAnalysis
								? AnalysisName.PREDICATE_ANALYSIS
								: AnalysisName.INTRA_PREDICATE_ANALYSIS)
						|| analysis instanceof InterThreadForwardCellularAnalysis
						|| analysis instanceof InterThreadForwardNonCellularAnalysis) {
					analysis.storeNodesToBeUpdated(new HashSet<>()); // This will just mark the IDFA as invalid.
				}
			}

		} else if (Program.updateCategory == UpdateCategory.EGUPD) {
			for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
				if (analysis instanceof InterThreadForwardCellularAnalysis
						|| analysis instanceof InterThreadForwardNonCellularAnalysis) {
					analysis.storeNodesToBeUpdated(nodeSet);
				}
			}
			for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
				if (analysis instanceof InterThreadForwardCellularAnalysis
						|| analysis instanceof InterThreadForwardNonCellularAnalysis) {
					if (analysis.getAnalysisName() == AnalysisName.POINTSTO) {
						analysis.markStateToBeValid();
						Program.memoizeAccesses++;
						analysis.restartAnalysisFromStoredNodes();
						Program.memoizeAccesses--;
					} else {
						analysis.markStateToBeValid();
						analysis.restartAnalysisFromStoredNodes();
					}
				}
			}
		} else if (Program.updateCategory == UpdateCategory.LZUPD || Program.updateCategory == UpdateCategory.CPUPD) {
			for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
				if (analysis
						.getAnalysisName() == (Program.useInterProceduralPredicateAnalysis
								? AnalysisName.PREDICATE_ANALYSIS
								: AnalysisName.INTRA_PREDICATE_ANALYSIS)
						|| analysis instanceof InterThreadForwardCellularAnalysis
						|| analysis instanceof InterThreadForwardNonCellularAnalysis) {
					analysis.storeNodesToBeUpdated(nodeSet);
				}
			}
		}
		// Old code:
		// /*
		// * NOTE: We must always call points-to analysis first, so that
		// * memoized values of all access lists stabilize.
		// */
		// if (analysisMapForward.values().stream().anyMatch((a) -> a instanceof
		// PointsToAnalysis)) {
		// Optional<InterThreadForwardIDFA<?>> opt =
		// analysisMapForward.values().stream()
		// .filter((a) -> a instanceof PointsToAnalysis).findFirst();
		// if (opt.isPresent()) {
		// Program.memoizeAccesses++;
		// opt.get().modularUpdateIDFA(nodeSet);
		// Program.memoizeAccesses--;
		// }
		// }
		// for (InterThreadForwardIDFA<?> analysis : analysisMapForward.values()) {
		// if (analysis instanceof PointsToAnalysis) {
		// continue;
		// }
		// analysis.modularUpdateIDFA(nodeSet);
		// }
	}

	/**
	 * Updates flow-fact information for various backward analyses, by rerunning the
	 * analyses on the set of nodes in
	 * {@code nodeSet}.
	 *
	 * @param nodeSet
	 *                set of nodes starting which backward analyses have to be
	 *                rerun.
	 */
	public static void updateFlowFactsBackward(Set<Node> nodeSet) {
		if (Program.updateCategory == UpdateCategory.EGINV) {
			assert (nodeSet == null || nodeSet.isEmpty());
			for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
				if (analysis instanceof InterThreadBackwardCellularAnalysis
						|| analysis instanceof InterThreadBackwardNonCellularAnalysis) {
					AutomatedUpdater.reinitIDFA(analysis);
				}
			}
		} else if (Program.updateCategory == UpdateCategory.LZINV || Program.updateCategory == UpdateCategory.CPINV) {
			assert (nodeSet == null || nodeSet.isEmpty());
			for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
				if (analysis instanceof InterThreadBackwardCellularAnalysis
						|| analysis instanceof InterThreadBackwardNonCellularAnalysis) {
					analysis.storeNodesToBeUpdated(new HashSet<>());
				}
			}
		} else if (Program.updateCategory == UpdateCategory.EGUPD) {
			for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
				if (analysis instanceof InterThreadBackwardCellularAnalysis
						|| analysis instanceof InterThreadBackwardNonCellularAnalysis) {
					analysis.storeNodesToBeUpdated(nodeSet);
				}
			}
			for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
				if (analysis instanceof InterThreadBackwardCellularAnalysis
						|| analysis instanceof InterThreadBackwardNonCellularAnalysis) {
					analysis.markStateToBeValid();
					BeginPhasePoint.stabilizeStaleBeginPhasePoints();
					analysis.restartAnalysisFromStoredNodes();
				}
			}
		} else {
			assert (Program.updateCategory == UpdateCategory.LZUPD || Program.updateCategory == UpdateCategory.CPUPD);
			for (FlowAnalysis<?> analysis : FlowAnalysis.getAllAnalyses().values()) {
				if (analysis instanceof InterThreadBackwardCellularAnalysis
						|| analysis instanceof InterThreadBackwardNonCellularAnalysis) {
					analysis.storeNodesToBeUpdated(nodeSet);
				}
			}
		}
	}

	/**
	 * Returns a set of those nodes starting which we need to rerun various forward
	 * iterative data-flow analyses, to
	 * make the related flow-facts consistent, upon removal of {@code oldNode}.
	 * <br>
	 * <i>Note that this method should be called before {@code oldNode}
	 * has already been removed. However, the reruns should start after the actual
	 * removal.</i>
	 *
	 * @param oldNode
	 *                node that has to be removed from the CFG (while the node is
	 *                still connected to the CFG).
	 *
	 * @return a set of those nodes starting which we need to rerun forward
	 *         data-flow analyses.
	 */
	public static Set<Node> nodesForForwardRerunOnRemoval(Node oldNode) {
		if (Program.updateCategory == UpdateCategory.EGINV || Program.updateCategory == UpdateCategory.LZINV
				|| Program.updateCategory == UpdateCategory.CPINV) {
			return null;
		}
		Node node = Misc.getCFGNodeFor(oldNode);
		Set<Node> returnSet = new HashSet<>();
		if (node instanceof CallStatement) {
			CallStatement callStmt = (CallStatement) node;
			for (FunctionDefinition callee : callStmt.getInfo().getCalledDefinitions()) {
				returnSet.add(callee.getInfo().getCFGInfo().getNestedCFG().getBegin());
			}
			returnSet.addAll(node.getInfo().getCFGInfo().getInterProceduralLeafSuccessors());
			return returnSet;
		} else if (Misc.isCFGLeafNode(node)) {
			if (node instanceof DummyFlushDirective) {
				DummyFlushDirective dfd = (DummyFlushDirective) node;
				returnSet.addAll(dfd.getInfo().getInterTaskDummySuccessors());
			}
			returnSet.addAll(node.getInfo().getCFGInfo().getInterProceduralLeafSuccessors());
			return returnSet;
		} else if (Misc.isCFGNonLeafNode(node)) {
			for (CallStatement callStmt : node.getInfo().getLexicallyEnclosedCallStatements()) {
				for (FunctionDefinition callee : callStmt.getInfo().getCalledDefinitions()) {
					returnSet.add(callee.getInfo().getCFGInfo().getNestedCFG().getBegin());
				}
			}
			for (DummyFlushDirective sourceDummyFlush : Misc.getInheritedEnclosee(node, DummyFlushDirective.class)) {
				returnSet.addAll(sourceDummyFlush.getInfo().getInterTaskDummySuccessors());
			}
			returnSet.addAll(node.getInfo().getCFGInfo().getInterProceduralLeafSuccessors());
			returnSet.addAll(node.getInfo().getOutJumpDestinations());
			return returnSet;
		} else {
			assert (false);
			return null;
		}
	}

	/**
	 * Returns a set of nodes that lie in those paths which will not be reachable
	 * anymore after removal of the {@code
	 * oldNode}, when traversing in the forward direction.
	 *
	 * @param oldNode
	 *                node of interest.
	 *
	 * @return a set of nodes that lie in those paths which will not be reachable
	 *         anymore after removal of the {@code
	 * oldNode}, when traversing in the forward direction.
	 */
	public static Set<Node> unreachableAfterRemovalForward(Node oldNode) {
		if (Program.updateCategory == UpdateCategory.EGINV || Program.updateCategory == UpdateCategory.LZINV
				|| Program.updateCategory == UpdateCategory.CPINV) {
			return null;
		}
		Node node = Misc.getCFGNodeFor(oldNode);
		Set<Node> returnSet = new HashSet<>();
		if (node instanceof CallStatement) {
			CallStatement callStmt = (CallStatement) node;
			for (FunctionDefinition callee : callStmt.getInfo().getCalledDefinitions()) {
				returnSet.add(callee.getInfo().getCFGInfo().getNestedCFG().getBegin());
			}
			return returnSet;
		} else if (Misc.isCFGLeafNode(node)) {
			if (node instanceof DummyFlushDirective) {
				DummyFlushDirective sourceDummyFlush = (DummyFlushDirective) node;
				returnSet.addAll(sourceDummyFlush.getInfo().getInterTaskDummySuccessors());
				return returnSet;
			} else {
				if (Misc.isJumpNode(node)) {
					returnSet.addAll(node.getInfo().getCFGInfo().getInterTaskLeafSuccessorNodes());
				}
				return returnSet;
			}
		} else if (Misc.isCFGNonLeafNode(node)) {
			for (CallStatement callStmt : node.getInfo().getLexicallyEnclosedCallStatements()) {
				// System.out.println(callStmt);
				for (FunctionDefinition callee : callStmt.getInfo().getCalledDefinitions()) {
					returnSet.add(callee.getInfo().getCFGInfo().getNestedCFG().getBegin());
				}
			}
			for (DummyFlushDirective sourceDummyFlush : Misc.getInheritedEnclosee(node, DummyFlushDirective.class)) {
				returnSet.addAll(sourceDummyFlush.getInfo().getInterTaskDummySuccessors());
			}
			returnSet.addAll(node.getInfo().getOutJumpDestinations());
			return returnSet;
		} else {
			assert (false);
			return null;
		}
	}

	/**
	 * Returns a set of those nodes starting which we need to rerun various backward
	 * iterative data-flow analyses, to
	 * make the related flow-facts consistent, upon removal of {@code oldNode}.
	 * <br>
	 * <i>Note that this method should be called before {@code oldNode}
	 * has already been removed. However, the reruns should start after the actual
	 * removal.</i>
	 *
	 * @param oldNode
	 *                node that has to be removed from the CFG. (However, which is
	 *                still connected.)
	 *
	 * @return a set of those nodes starting which we need to rerun backward
	 *         data-flow analyses.
	 */
	public static Set<Node> nodesForBackwardRerunOnRemoval(Node oldNode) {
		if (Program.updateCategory == UpdateCategory.EGINV || Program.updateCategory == UpdateCategory.LZINV
				|| Program.updateCategory == UpdateCategory.CPINV) {
			return null;
		}
		Node node = Misc.getCFGNodeFor(oldNode);
		Set<Node> returnSet = new HashSet<>();
		if (node instanceof CallStatement) {
			CallStatement callStmt = (CallStatement) node;
			for (FunctionDefinition callee : callStmt.getInfo().getCalledDefinitions()) {
				returnSet.add(callee.getInfo().getCFGInfo().getNestedCFG().getEnd());
			}
			returnSet.addAll(node.getInfo().getCFGInfo().getInterProceduralLeafPredecessors());
			return returnSet;
		} else if (Misc.isCFGLeafNode(node)) {
			if (node instanceof DummyFlushDirective) {
				DummyFlushDirective dfd = (DummyFlushDirective) node;
				returnSet.addAll(dfd.getInfo().getInterTaskDummyPredecessors());
			}
			returnSet.addAll(node.getInfo().getCFGInfo().getInterProceduralLeafPredecessors());
			return returnSet;
		} else if (Misc.isCFGNonLeafNode(node)) {
			for (CallStatement callStmt : node.getInfo().getLexicallyEnclosedCallStatements()) {
				for (FunctionDefinition callee : callStmt.getInfo().getCalledDefinitions()) {
					returnSet.add(callee.getInfo().getCFGInfo().getNestedCFG().getEnd());
				}
			}
			for (DummyFlushDirective sourceDummyFlush : Misc.getInheritedEnclosee(node, DummyFlushDirective.class)) {
				returnSet.addAll(sourceDummyFlush.getInfo().getInterTaskDummyPredecessors());
			}
			returnSet.addAll(node.getInfo().getCFGInfo().getInterProceduralLeafPredecessors());
			returnSet.addAll(node.getInfo().getInJumpSources());
			return returnSet;
		} else {
			assert (false);
			return null;
		}
	}

	/**
	 * Returns a set of nodes that lie in those paths which will not be reachable
	 * anymore after removal of the {@code
	 * oldNode}, when traversing in the backward direction.
	 *
	 * @param oldNode
	 *                node of interest.
	 *
	 * @return a set of nodes that lie in those paths which will not be reachable
	 *         anymore after removal of the {@code
	 * oldNode}, when traversing in the backward direction.
	 */
	public static Set<Node> unreachableAfterRemovalBackward(Node oldNode) {
		if (Program.updateCategory == UpdateCategory.EGINV || Program.updateCategory == UpdateCategory.LZINV
				|| Program.updateCategory == UpdateCategory.CPINV) {
			return null;
		}
		Node node = Misc.getCFGNodeFor(oldNode);
		Set<Node> returnSet = new HashSet<>();
		if (node instanceof CallStatement) {
			CallStatement callStmt = (CallStatement) node;
			for (FunctionDefinition callee : callStmt.getInfo().getCalledDefinitions()) {
				returnSet.add(callee.getInfo().getCFGInfo().getNestedCFG().getEnd());
			}
			return returnSet;
		} else if (Misc.isCFGLeafNode(node)) {
			if (node instanceof DummyFlushDirective) {
				DummyFlushDirective destinationDummyFlush = (DummyFlushDirective) node;
				returnSet.addAll(destinationDummyFlush.getInfo().getInterTaskDummyPredecessors());
				return returnSet;
			} else {
				if (node instanceof Statement) {
					Statement stmt = (Statement) node;
					if (stmt.getInfo().hasLabelAnnotations()) {
						returnSet.addAll(node.getInfo().getCFGInfo().getInterTaskLeafPredecessorNodes());
					}
				}
				return returnSet;
			}
		} else if (Misc.isCFGNonLeafNode(node)) {
			for (CallStatement callStmt : node.getInfo().getLexicallyEnclosedCallStatements()) {
				for (FunctionDefinition callee : callStmt.getInfo().getCalledDefinitions()) {
					returnSet.add(callee.getInfo().getCFGInfo().getNestedCFG().getEnd());
				}
			}
			for (DummyFlushDirective destinationDummyFlush : Misc.getInheritedEnclosee(node,
					DummyFlushDirective.class)) {
				returnSet.addAll(destinationDummyFlush.getInfo().getInterTaskDummyPredecessors());
			}
			returnSet.addAll(node.getInfo().getInJumpSources());
			return returnSet;
		} else {
			assert (false);
			return null;
		}
	}

	/**
	 * This method models the affect of addition of an already existing label in
	 * {@code node} to {@code node}. This is
	 * used while inserting {@code node} or any of its ancestors at a new place in
	 * CFG.
	 *
	 * @param node
	 */
	public static void modelAdditionOfLabels(Node node) {
		PointsToAnalysis.disableHeuristic();
		node = Misc.getCFGNodeFor(node);
		for (Node cfgNode : node.getInfo().getCFGInfo().getLexicalCFGContents()) {
			if (cfgNode instanceof Statement) {
				Statement stmtNode = (Statement) cfgNode;
				for (Label label : stmtNode.getInfo().getLabelAnnotations()) {
					stmtNode.getInfo().updateUponLabeledNodeAddition(label);
				}
			}
		}
	}

	/**
	 * This method models the affect of removal of a label (without actually
	 * removing the label) from {@code node}, and
	 * its nested components. It is used before removing {@code node} from its
	 * current position in the CFG.
	 *
	 * @param node
	 */
	public static void modelRemovalOfLabels(Node node) {
		PointsToAnalysis.disableHeuristic();
		node = Misc.getCFGNodeFor(node);
		for (Node cfgNode : node.getInfo().getCFGInfo().getLexicalCFGContents()) {
			if (cfgNode instanceof Statement) {
				Statement stmtNode = (Statement) cfgNode;
				for (Label label : stmtNode.getInfo().getLabelAnnotations()) {
					stmtNode.getInfo().updateUponLabeledNodeRemoval(label);
				}
			}
		}
	}

	/**
	 * This method automatically updates the symbol table, typedef table, and type
	 * tables upon addition of the node
	 * {@code node}.
	 *
	 * @param node
	 *             CFG node that has to been added to a new location.
	 */
	public static void updateSymbolTableOnNodeAddition(Node node) {
		node = Misc.getCFGNodeFor(node);
		if (Misc.isCFGLeafNode(node)) {
			if (node instanceof Declaration) {
				Scopeable nestingScope = Misc.getEnclosingBlock(node);
				if (nestingScope instanceof CompoundStatement) {
					((CompoundStatement) nestingScope).getInfo().addDeclarationToSymbolOrTypeTable((Declaration) node);
				}
			}
		}
	}

	/**
	 * This method automatically updates the symbol table, typedef table, and type
	 * tables upon removal of the node
	 * {@code node}.
	 *
	 * @param node
	 *             CFG node that has to be removed.
	 */
	public static void updateSymbolTableOnNodeRemoval(Node node) {
		node = Misc.getCFGNodeFor(node);
		if (Misc.isCFGLeafNode(node)) {
			if (node instanceof Declaration) {
				Scopeable nestingScope = Misc.getEnclosingBlock(node);
				if (nestingScope instanceof CompoundStatement) {
					((CompoundStatement) nestingScope).getInfo()
							.removeDeclarationFromSymbolOrTypeTable((Declaration) node);
				}
			}
		}
	}

	/**
	 * Given a node that has been added to the program, this method invokes
	 * {@link MHPAnalyzer#initMHP()} on each {@link
	 * ParallelConstruct} present in the node. Furthermore, if the set of input
	 * phases for any leaf CFG node lexically
	 * enclosed within the given node is empty, then this method invokes
	 * {@link NodePhaseInfo#rememberCurrentPhases()}
	 * on such leaf nodes.
	 *
	 * @param addedNode
	 */
	public static void performInitOfMHPUponAddition(Node addedNode) {
		if (!addedNode.getInfo().isConnectedToProgram()) {
			return;
		}
		for (ParallelConstruct parCons : Misc.getExactPostOrderEnclosee(addedNode, ParallelConstruct.class)) {
			MHPAnalyzer mhp = new MHPAnalyzer(parCons);
			mhp.initMHP();
		}
		for (Node cfgNode : addedNode.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
			if (cfgNode.getInfo().getNodePhaseInfo().getInputPhasesOfOldTimes().isEmpty()) {
				cfgNode.getInfo().getNodePhaseInfo().rememberCurrentPhases();
			}
		}
	}

	public static void performResetOfMHPUponRemoval(Node removedNode) {
		for (ParallelConstruct parCons : Misc.getExactPostOrderEnclosee(removedNode, ParallelConstruct.class)) {
			parCons.getInfo().flushMHPData();
		}
	}

	/**
	 * This method attempts to adjust MHP information locally, if that can be done,
	 * to model the effect of addition of
	 * {@code node} If successful, it returns {@code true}.
	 *
	 * @param node
	 *             the node that has been added to the program, for which MHP
	 *             information needs to be stabilized.
	 *
	 * @return {@code true}, if the MHP stabilization was successful.
	 */
	public static boolean stabilizeMHPLocallyUponAddition(Node node) {
		node = Misc.getCFGNodeFor(node);
		assert (!(node instanceof BeginNode));
		if (node instanceof BarrierDirective || !node.getInfo().isControlConfined()
				|| node.getInfo().getCFGInfo().getIntraTaskCFGLeafContentsOfSameParLevel().stream()
						.anyMatch(n -> n.getNode() instanceof BarrierDirective)) {
			return false;
		}
		Set<Node> predSet = node.getInfo().getCFGInfo().getInterProceduralLeafPredecessors();
		for (Node pred : predSet) {
			if (pred instanceof BarrierDirective
					|| (pred instanceof BeginNode && pred.getParent() instanceof ParallelConstruct)
					|| (pred instanceof EndNode && pred.getParent() instanceof ParallelConstruct)) {
				return false;
			}
		}
		// Take MHP information from all the predecessors.
		hasBeenOtimized++;
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			boolean oldVal = BeginPhasePoint.stabilizationInProgress;
			BeginPhasePoint.stabilizationInProgress = true;
			Set<Node> internalContents = node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents();
			for (Node pred : predSet) {
				Set<BeginPhasePoint> bppsOfPred = BeginPhasePoint.getRelatedBPPsNoStaleRemoval(pred);
				for (BeginPhasePoint bpp : bppsOfPred) {
					for (Node n : internalContents) {
						bpp.getInternalReachables().add(n);
					}
				}
				try {
					for (AbstractPhase<?, ?> ph : pred.getInfo().getNodePhaseInfo().getStalePhaseSet()) {
						for (Node n : internalContents) {
							ph.addNode(n);
						}
					}
				} catch (ConcurrentModificationException e) {
					System.err.println("While attempting to add " + node + " a" + node.getClass().getSimpleName()
							+ " we found an issue while dealing with its predecessor " + pred + " a "
							+ pred.getClass().getSimpleName()
							+ ". It's worth noting that phaseSet of the predecessor is object "
							+ pred.getInfo().getNodePhaseInfo().getStalePhaseSet().hashCode()
							+ ", whereas that of the node is "
							+ node.getInfo().getNodePhaseInfo().getStalePhaseSet().hashCode());
					System.exit(0);
				}
			}
			BeginPhasePoint.stabilizationInProgress = oldVal;
		} else {
			Set<Node> internalContents = node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents();
			for (Node pred : predSet) {
				try {
					for (AbstractPhase<?, ?> ph : pred.getInfo().getNodePhaseInfo().getStalePhaseSet()) {
						for (Node n : internalContents) {
							ph.addNode(n);
						}
					}
				} catch (ConcurrentModificationException e) {
					System.err.println("While attempting to add " + node + " a" + node.getClass().getSimpleName()
							+ " we found an issue while dealing with its predecessor " + pred + " a "
							+ pred.getClass().getSimpleName()
							+ ". It's worth noting that phaseSet of the predecessor is object "
							+ pred.getInfo().getNodePhaseInfo().getStalePhaseSet().hashCode()
							+ ", whereas that of the node is "
							+ node.getInfo().getNodePhaseInfo().getStalePhaseSet().hashCode());
					System.exit(0);
				}
			}

		}
		return true;
	}

	/**
	 * This method attempts to adjust MHP information locally, if that can be done,
	 * to model the effect of addition of
	 * {@code node} If successful, it returns {@code true}.
	 *
	 * @param node
	 *             the node that has been added to the program, for which MHP
	 *             information needs to be stabilized.
	 *
	 * @return {@code true}, if the MHP stabilization was successful.
	 */
	public static boolean stabilizeMHPLocallyUponRemoval(Node node) {
		node = Misc.getCFGNodeFor(node);
		assert (!(node instanceof EndNode));
		if (node instanceof BarrierDirective || !node.getInfo().isControlConfined()
				|| node.getInfo().getCFGInfo().getIntraTaskCFGLeafContentsOfSameParLevel().stream()
						.anyMatch(n -> n.getNode() instanceof BarrierDirective)) {
			return false;
		}
		Set<Node> predSet = node.getInfo().getCFGInfo().getInterProceduralLeafPredecessors();
		for (Node pred : predSet) {
			if (pred instanceof BarrierDirective
					|| (pred instanceof BeginNode && pred.getParent() instanceof ParallelConstruct)
					|| (pred instanceof EndNode && pred.getParent() instanceof ParallelConstruct)) {
				return false;
			}
		}
		// Delete all MHP info for this node.
		hasBeenOtimized++;
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			boolean oldVal = BeginPhasePoint.stabilizationInProgress;
			BeginPhasePoint.stabilizationInProgress = true;
			Set<Node> internalContents = node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents();
			for (Node n : internalContents) {
				for (AbstractPhase<?, ?> ph : new HashSet<>(node.getInfo().getNodePhaseInfo().getStalePhaseSet())) {
					ph.removeNode(n);
					for (AbstractPhasePointable bppAbs : new HashSet<>(ph.getStaleBeginPoints())) {
						BeginPhasePoint bpp = (BeginPhasePoint) bppAbs;
						bpp.getInternalReachables().remove(n);
					}
				}
			}
			BeginPhasePoint.stabilizationInProgress = oldVal;
		} else {
			Set<Node> internalContents = node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents();
			for (Node n : internalContents) {
				for (AbstractPhase<?, ?> ph : new HashSet<>(node.getInfo().getNodePhaseInfo().getStalePhaseSet())) {
					ph.removeNode(n);
				}
			}

		}
		return true;
	}

	/**
	 * Performs automated updates to phase information, and information about
	 * inter-task edges, upon insertion of the
	 * node {@code node}.
	 * <br>
	 * Note that {@code node} has already been inserted in its specific location in
	 * the CFG.
	 *
	 * @param node
	 *             node that has been inserted at a location in the CFG.
	 */
	public static void updatePhaseAndInterTaskEdgesUponAddition(Node node) {
		if (Program.mhpUpdateCategory == UpdateCategory.EGINV) {
			AutomatedUpdater.reinitMHP();
			return;
		}
		node = Misc.getCFGNodeFor(node);
		long timer = System.nanoTime();
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (Program.useHeuristicWithYuan) {
				if (AutomatedUpdater.stabilizeMHPLocallyUponAddition(node)) {
					BeginPhasePoint.stabilizationTime += (System.nanoTime() - timer);
					return;
				}
			}
			AbstractPhase.globalMHPStale = true;
			return;
		}
		if (Program.useHeuristicWithIcon && AutomatedUpdater.stabilizeMHPLocallyUponAddition(node)) {
			BeginPhasePoint.stabilizationTime += (System.nanoTime() - timer);
			return;
		}
		if (Program.mhpUpdateCategory == UpdateCategory.LZINV) {
			AbstractPhase.globalMHPStale = true;
			return;
		}
		Set<BeginPhasePoint> affectedBPPs = new HashSet<>();
		for (NodeWithStack predWithStack : node.getInfo().getCFGInfo()
				.getParallelConstructFreeInterProceduralLeafPredecessors(new CallStack())) {
			Node predNode = predWithStack.getNode();
			affectedBPPs.addAll(BeginPhasePoint.getRelatedBPPs(predNode));
		}
		if (Misc.isCFGLeafNode(node)) {
			if (node instanceof BarrierDirective) {
				for (AbstractPhasePointable bppAbs : AbstractPhasePointable.getAllBeginPhasePoints()) {
					BeginPhasePoint bpp = (BeginPhasePoint) bppAbs;
					if (bpp.getNode() == node) {
						affectedBPPs.add(bpp);
					}
				}
			}
		} else {
			// For non-leaf nodes.
			for (Node predNode : node.getInfo().getInJumpSources()) {
				affectedBPPs.addAll(BeginPhasePoint.getRelatedBPPs(predNode));
			}
			for (Node predNode : node.getInfo().getOutJumpSources()) {
				affectedBPPs.addAll(BeginPhasePoint.getRelatedBPPs(predNode));
			}
			affectedBPPs.addAll(BeginPhasePoint.getRelatedBPPs(node.getInfo().getCFGInfo().getNestedCFG().getEnd()));
		}
		BeginPhasePoint.addStaleBeginPhasePoints(affectedBPPs);
		BeginPhasePoint.stabilizationTime += (System.nanoTime() - timer);
		if (Program.mhpUpdateCategory == UpdateCategory.EGUPD) {
			BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		}
	}

	/**
	 * In case of update to {@link BeginPhasePoint} done via
	 * {@link BeginPhasePoint#removeNode(Node)}, {@link
	 * BeginPhasePoint#deprecated_removeNextBarrier(EndPhasePoint)}, etc., this
	 * method just performs the updates, and
	 * returns an empty set.
	 * <br>
	 * Otherwise, this method is used to obtain a set of BeginPhasePoints on which
	 * we need to call {@link
	 * BeginPhasePoint#populateReachablesAndBarriers()}, after CFG updates have been
	 * done. Note that {@code
	 * BeginPhasePoint#setInvalidFlag()} is already called by this method, and need
	 * not be called again by the callee.
	 *
	 * @param node
	 *             node that has to be removed from the CFG.
	 *
	 * @return a set of BeginPhasePoints whose sets need to be regenerated upon
	 *         removal of {@code node}, using {@link
	 *         BeginPhasePoint#populateReachablesAndBarriers()}.
	 */
	public static Set<BeginPhasePoint> updateBPPOrGetAffectedBPPSetUponRemoval(Node node) {
		if (Program.mhpUpdateCategory == UpdateCategory.EGINV) {
			return null;
		}
		node = Misc.getCFGNodeFor(node);
		long timer = System.nanoTime();
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (Program.useHeuristicWithYuan) {
				if (AutomatedUpdater.stabilizeMHPLocallyUponRemoval(node)) {
					BeginPhasePoint.stabilizationTime += (System.nanoTime() - timer);
					return new HashSet<>();
				}
			}
			AbstractPhase.globalMHPStale = true;
			return null;
		}
		if (Program.useHeuristicWithIcon && AutomatedUpdater.stabilizeMHPLocallyUponRemoval(node)) {
			BeginPhasePoint.stabilizationTime += (System.nanoTime() - timer);
			return new HashSet<>();
		}
		if (Program.mhpUpdateCategory == UpdateCategory.LZINV) {
			AbstractPhase.globalMHPStale = true;
			return null;
		}
		Set<BeginPhasePoint> affectedBPPs = new HashSet<>();
		for (NodeWithStack predWithStack : node.getInfo().getCFGInfo()
				.getParallelConstructFreeInterProceduralLeafPredecessors(new CallStack())) {
			Node predNode = predWithStack.getNode();
			affectedBPPs.addAll(BeginPhasePoint.getRelatedBPPs(predNode));
		}
		if (Misc.isCFGLeafNode(node)) {
			if (node instanceof BarrierDirective) {
				for (AbstractPhasePointable bppAbs : AbstractPhasePointable.getAllBeginPhasePoints()) {
					BeginPhasePoint bpp = (BeginPhasePoint) bppAbs;
					if (bpp.getNode() == node) {
						affectedBPPs.add(bpp);
					}
				}
			}
		} else {
			for (Node predNode : node.getInfo().getInJumpSources()) {
				affectedBPPs.addAll(BeginPhasePoint.getRelatedBPPs(predNode));
			}
			for (Node predNode : node.getInfo().getOutJumpSources()) {
				affectedBPPs.addAll(BeginPhasePoint.getRelatedBPPs(predNode));
			}
			affectedBPPs.addAll(BeginPhasePoint.getRelatedBPPs(node.getInfo().getCFGInfo().getNestedCFG().getEnd()));
		}
		BeginPhasePoint.stabilizationTime += (System.nanoTime() - timer);
		return affectedBPPs;
	}

	/**
	 * Update the phase and inter-task edges information upon removal of certain
	 * nodes that have resulted in changes to
	 * the sets of various begin-phase points in {@code bppSet}.
	 * <br>
	 * Note that this method is called on the modified state of the CFG.
	 *
	 * @param bppSet
	 *               set of begin-phase points whose sets have been changed due to
	 *               removal of certain nodes.
	 */
	public static void updatePhaseAndInterTaskEdgesUponRemoval(Set<BeginPhasePoint> bppSet) {
		if (Program.mhpUpdateCategory == UpdateCategory.EGINV) {
			AutomatedUpdater.reinitMHP();
			// The method above already calculates the time taken in stabilization.
			return;
		} else if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			if (!Program.useHeuristicWithYuan) {
				AbstractPhase.globalMHPStale = true;
			}
			return;
		} else if (Program.mhpUpdateCategory == UpdateCategory.LZINV) {
			AbstractPhase.globalMHPStale = true;
			return;
		} else if (Program.mhpUpdateCategory == UpdateCategory.EGUPD) {
			long timer = System.nanoTime();
			BeginPhasePoint.addStaleBeginPhasePoints(bppSet);
			BeginPhasePoint.stabilizationTime += (System.nanoTime() - timer);
			BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		} else {
			long timer = System.nanoTime();
			BeginPhasePoint.addStaleBeginPhasePoints(bppSet);
			BeginPhasePoint.stabilizationTime += (System.nanoTime() - timer);
		}
	}

	/**
	 * Given a set of nodes, starting which the CFG paths may change, this method
	 * calls changes to phase and inter-task
	 * edge information for appropriate BeginPhasePoints.
	 *
	 * @param nodeSet
	 *                set of nodes starting which the CFG paths may change.
	 */
	public static void adjustPhaseAndInterTaskEdgesUponLabelUpdate(Set<Node> nodeSet) {
		if (Program.mhpUpdateCategory == UpdateCategory.EGINV) {
			AutomatedUpdater.reinitMHP();
			// The method above already calculates the time taken for stabilization.
			return;
		} else if (Program.mhpUpdateCategory == UpdateCategory.LZINV
				|| Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			AbstractPhase.globalMHPStale = true;
			return;
		}
		long timer = System.nanoTime();
		Set<BeginPhasePoint> affectedBPPs = new HashSet<>();
		for (Node n : nodeSet) {
			affectedBPPs.addAll(BeginPhasePoint.getRelatedBPPs(n));
		}
		BeginPhasePoint.addStaleBeginPhasePoints(affectedBPPs);
		BeginPhasePoint.stabilizationTime += (System.nanoTime() - timer);

		if (Program.mhpUpdateCategory == UpdateCategory.EGUPD) {
			BeginPhasePoint.stabilizeStaleBeginPhasePoints();
		}
	}

	public static void invalidateSymbolsInNode(Node node) {
		for (Node leafNodes : node.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
			leafNodes.getInfo().invalidateAccessLists();
		}
	}

	private static final boolean localCoExistenceCheck = false;

	/**
	 * Remove any caching of co-existence data for {@code element} node (and its
	 * successors), if {@code element} is a
	 * {@link BarrierDirective}.
	 *
	 * @param element
	 *                a node that has to be removed from its position in the AST.
	 */
	public static void removeCachingFromSVEChecks(Node element) {
		Node cfgNode = Misc.getCFGNodeFor(element);
		if (cfgNode instanceof BarrierDirective) {
			BarrierDirective barrier = (BarrierDirective) cfgNode;
			if (AutomatedUpdater.localCoExistenceCheck) {
				CoExistenceChecker.flushCoExistenceCaches();
			} else {
				CoExistenceChecker.removeNodeCache(barrier);
				for (Node succ : barrier.getInfo().getCFGInfo().getLeafSuccessors()) {
					CoExistenceChecker.removeNodeCache(succ);
				}
			}
		}
	}

	public static void reinitMHP() {
		AutomatedUpdater.reinitMHPCounter++;
		long timer = System.nanoTime();
		MHPAnalyzer.flushALLMHPData();
		MHPAnalyzer.performMHPAnalysis(Program.getRoot());
		BeginPhasePoint.stabilizationTime += (System.nanoTime() - timer);
		// DumpSnapshot.dumpPhases("stable" + Program.mhpUpdateCategory +
		// AutomatedUpdater.reinitMHPCounter);
	}

	public static void reinitIDFA(FlowAnalysis<?> f) {
		f.autoUpdateTriggerCounter++;
		long timer = System.nanoTime();
		for (Node node : Program.getRoot().getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
			node.getInfo().removeAnalysisInformation(f.getAnalysisName());
		}
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		if (f instanceof PointsToAnalysis) {
			Program.basePointsTo = false; // Unneeded for second and further runs.
			Program.memoizeAccesses++;
			f.run(mainFunc);
			Program.memoizeAccesses--;
		} else {
			f.run(mainFunc);
		}
		f.flowAnalysisUpdateTimer += (System.nanoTime() - timer);
		// if (f instanceof PointsToAnalysis) {
		// DumpSnapshot.dumpPointsTo("stable" + Program.updateCategory +
		// f.autoUpdateTriggerCounter);
		// }
	}

}
