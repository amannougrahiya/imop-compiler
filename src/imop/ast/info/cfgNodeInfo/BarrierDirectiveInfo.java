/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.DataSharingAttribute;
import imop.ast.info.NodeInfo;
import imop.ast.info.OmpDirectiveInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.info.CompoundStatementCFGInfo;
import imop.lib.cfg.info.IfStatementCFGInfo;
import imop.lib.cfg.link.node.CFGLink;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.transform.BasicTransform;
import imop.lib.transform.percolate.MovableNodeMarker;
import imop.lib.transform.percolate.ReachableNodeMarker;
import imop.lib.transform.updater.InsertImmediatePredecessor;
import imop.lib.transform.updater.InsertImmediateSuccessor;
import imop.lib.transform.updater.NodeRemover;
import imop.lib.transform.updater.sideeffect.IndexIncremented;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.lib.transform.updater.sideeffect.SyntacticConstraint;
import imop.lib.util.CellSet;
import imop.lib.util.CollectorVisitor;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BarrierDirectiveInfo extends OmpDirectiveInfo {
	public BarrierDirectiveInfo(BarrierDirective owner) {
		super(owner);
	}

	private Set<Node> movableNodes = new HashSet<>(); // Populated by the visitor
	private Set<Node> nonMovableNodes = new HashSet<>();
	private Set<Node> reachableNodes = new HashSet<>(); // Populated by the method updatedReachableNodes()

	/**
	 * This method refills the $movableNodes$ with those statements (reachable from
	 * this barrier on a barrier-free path)
	 * which can be moved up this barrier.
	 *
	 * @param parConstruct
	 *                     Parallel construct which is being processed right now.
	 */
	public void updateMovableList(ParallelConstruct parConstruct) {
		/*
		 * Step I: Get a set of reachable nodes on barrier-free paths.
		 */
		setReachableNodes(new HashSet<>());
		ReachableNodeMarker reacher = new ReachableNodeMarker((BarrierDirective) getNode(), parConstruct,
				getReachableNodes());
		for (Node succ : this.getCFGInfo().getSuccBlocks()) {
			if (!getReachableNodes().contains(succ)) {
				succ.accept(reacher);
			}
		}

		/*
		 * Step II: Visit all the reachable nodes on barrier-free paths, and see
		 * if they can be moved across the barrier. If so, then mark them as
		 * movable. To be more efficient, we also keep track of those nodes
		 * which are guaranteed to be not movable.
		 */
		setMovableNodes(new HashSet<>());
		setNonMovableNodes(new HashSet<>());
		MovableNodeMarker movableMarker = new MovableNodeMarker((BarrierDirective) getNode(), getMovableNodes(),
				getNonMovableNodes(), getReachableNodes());
		for (Node succ : this.getCFGInfo().getSuccBlocks()) {
			if ((!getMovableNodes().contains(succ)) && (!getNonMovableNodes().contains(succ))) {
				succ.accept(movableMarker);
			}
		}

		// this.testMovableSet();
		/*
		 * For helping GC
		 */
		// movableNodes = null;
		// nonMovableNodes = null;

	}

	private void testMovableSet() {
		System.out.print("\nStatments movable across barrier at line #" + Misc.getLineNum(getNode()) + ": ");
		for (Node n : getMovableNodes()) {
			if (n instanceof BeginNode || n instanceof EndNode) {
				continue;
			}
			System.out.println("***********");
			n.getInfo().printNode();
			System.out.println("@" + Misc.getLineNum(n) + "; ");
		}
		System.out.println();

		// System.out.println("Statments reachable from barrier at line#" +
		// Misc.getLineNum(node) + ": ");
		// for(Node n : reachableNodes) {
		// System.out.print(Misc.getLineNum(n) + "; ");
		// }
		// System.out.println();
		System.out.print("Statements not movable across barrier at line #" + Misc.getLineNum(getNode()) + ": ");
		for (Node n : getNonMovableNodes()) {
			if (n instanceof BeginNode || n instanceof EndNode) {
				continue;
			}
			// if (Misc.isCFGNonLeafNode(n)) {
			System.out.println("***********");
			n.getInfo().printNode();
			System.out.println("@" + Misc.getLineNum(n) + "; ");
			// }
		}
		System.out.println();
	}

	/**
	 * Passes null for the required IterationStatement argument.
	 *
	 * @see {@link BarrierDirectiveInfo#percolateCodeUpwards(IterationStatement)}.
	 */
	public boolean percolateCodeUpwards() {
		return this.percolateCodeUpwards(null);
	}

	/**
	 * <br>
	 * CREATED/UPDATED: 09-Dec-2017 8:16:52 AM (Edited by: Aman) <br>
	 * Percolates code from below to above, while making
	 * sure that no code is moved upwards beyond the entry point of {@code itStmt}.
	 * <br>
	 * Note that this barrier might not even be present inside or reachable from
	 * within {@code itStmt} (in which case,
	 * it must share a synchronization set with some other barrier in
	 * {@code itStmt}).
	 *
	 * @param itStmt
	 *               loop above whose entry point the code should not be moved.
	 *
	 * @return true, if code involving read/write of any shared variable is moved
	 *         above this node.
	 */
	public boolean percolateCodeUpwards(IterationStatement itStmt) {
		boolean codeMoved = false;

		CompoundStatement compStmt = (CompoundStatement) Misc.getEnclosingBlock(this.getNode());
		if (compStmt == null) {
			// This implies that the barrier has been removed during split down.
			return false;
		}

		// Step I: Find the index of the barrier in its compound statement.
		CompoundStatementCFGInfo compStmtCFGInfo = compStmt.getInfo().getCFGInfo();
		List<Node> elemList = compStmtCFGInfo.getElementList();
		int indexOfBarrier = elemList.indexOf(getNode());
		int movingPointer = indexOfBarrier + 1;
		outer: while (movingPointer < elemList.size()) {
			Node element = elemList.get(movingPointer);
			if (element instanceof BarrierDirective) {
				codeMoved |= this.moveBarrierDownFork(itStmt);
				return codeMoved;
			}
			if (element instanceof FlushDirective) {
				movingPointer++;
				continue outer;
			}
			if (Misc.isCFGNonLeafNode(element)) {
				Set<NodeWithStack> internalNodes = element.getInfo().getCFGInfo()
						.getIntraTaskCFGLeafContentsOfSameParLevel(new CallStack());
				if (internalNodes.stream().filter(ns -> ns.getNode() instanceof BarrierDirective).count() > 0) {
					/*
					 * Stop percolation upon encountering a barrier.
					 * However, do notice that some syntactic barriers might not
					 * be reachable backwards from the EndNode of a non-leaf
					 * node. Allow percolation in that case, by skipping this
					 * element.
					 */
					if (element instanceof IfStatement) {
						/*
						 * Check if there exists any path upwards from EndNode
						 * that may:
						 * 1. exit the element except at the BeginNode, or
						 * 2. contain a barrier at the same par-level.
						 * If such a path exists, then return from this method,
						 * else skip this element.
						 */
						Set<NodeWithStack> endingElements = new HashSet<>();
						Node startingEndNode = element.getInfo().getCFGInfo().getNestedCFG().getEnd();
						Node endingBeginNode = element.getInfo().getCFGInfo().getNestedCFG().getBegin();
						Set<Node> outJumpSources = element.getInfo().getOutJumpSources();
						CollectorVisitor.collectNodeSetInGenericGraph(
								new NodeWithStack(startingEndNode, new CallStack()), endingElements, (n) -> {
									if (outJumpSources.stream().anyMatch(nS -> nS == n.getNode())
											|| n.getNode() == endingBeginNode) {
										return true;
									}
									if (n.getNode() instanceof BarrierDirective) {
										return true;
									}
									return false;
								}, (n) -> n.getNode().getInfo().getCFGInfo()
										.getParallelConstructFreeInterProceduralLeafPredecessors(n.getCallStack()));
						if (endingElements.stream().anyMatch(nS -> nS.getNode() instanceof BarrierDirective)) {
							codeMoved |= this.moveBarrierDownFork(itStmt);
							return codeMoved;
						} else {
							movingPointer++;
							continue outer;
						}
					}
					codeMoved |= this.moveBarrierDownFork(itStmt);
					return codeMoved;
				}
			}
			if (!Phase.isNodeAllowedInNewPhasesAbove(element, (BarrierDirective) this.getNode())) {
				movingPointer++;
				continue outer;
			}
			// Ensure that there won't be any control dependences that this node
			// may break while moving beyond the barrier.
			if (!element.getInfo().isControlConfined()) {
				movingPointer++;
				continue outer;
			}

			if (!BarrierDirectiveInfo.checkPotentialToCrossUpwards(elemList, indexOfBarrier, movingPointer)) {
				movingPointer++;
				continue outer;
			}

			// Ensure that there won't be any data dependences that this node may break
			// while moving beyond the barrier.

			// Put some checks for the case when we may have flushes in the ancestor.

			// Now, we will move the statement upwards, beyond the barrier.
			List<SideEffect> sideEffects = compStmtCFGInfo.removeElement(element);
			if (!Misc.changePerformed(sideEffects)) {
				movingPointer++;
				continue outer;
			}
			for (SideEffect sideEffect : sideEffects) {
				if (sideEffect instanceof IndexIncremented) {
					movingPointer++;
				}
			}
			elemList = compStmtCFGInfo.getElementList();
			sideEffects = compStmtCFGInfo.addElement(indexOfBarrier, element);
			for (SideEffect sideEffect : sideEffects) {
				if (sideEffect instanceof SyntacticConstraint) {
					Misc.exitDueToError("Cannot add a removed element: " + element);
					movingPointer++;
					continue outer;
				}
			}
			elemList = compStmtCFGInfo.getElementList();
			codeMoved = true;
			indexOfBarrier++;
			movingPointer++;
		}
		codeMoved |= this.moveBarrierDownFork(itStmt);
		return codeMoved;
	}

	/**
	 * Moves this barrier down the succeeding fork instruction, if any, if the
	 * forking predicate leaves the enclosing
	 * loop {@code itStmt}. This step is followed by calling
	 * {@link #percolateCodeUpwards(IterationStatement)}, on all
	 * the newly created copies of this barrier on the forked paths, if any.
	 *
	 * @param itStmt
	 *               iteration-statement beyond which the code should not move.
	 *
	 * @return true, if the barrier was forked down. Note that this implies code
	 *         motion of predicate across the barrier.
	 */
	public boolean moveBarrierDownFork(IterationStatement itStmt) {
		// if (1 < 2) {
		// return false;
		// }
		BarrierDirective barrier = (BarrierDirective) this.getNode();
		if (itStmt == null) {
			itStmt = (IterationStatement) Misc.getEnclosingLoopOrForConstruct(barrier);
			if (itStmt == null) {
				return false;
			}
		}
		final Node loop = Misc.getCFGNodeFor(itStmt);
		CFGLink link = CFGLinkFinder.getCFGLinkFor(barrier);
		CompoundStatement compStmt = (CompoundStatement) link.getEnclosingNode();
		CompoundStatementCFGInfo compInfo = compStmt.getInfo().getCFGInfo();
		List<Node> elementList = compInfo.getElementList();
		int indexOfBarrier = elementList.indexOf(barrier);
		if (indexOfBarrier == elementList.size() - 1) {
			return false;
		}
		Node nextElem = elementList.get(indexOfBarrier + 1);
		if (nextElem instanceof IterationStatement) {
			return false;
		}
		if (nextElem instanceof Statement) {
			Statement stmt = (Statement) nextElem;
			if (stmt.getInfo().hasLabelAnnotations()) {
				return false;
			}
		}
		if (Misc.isCFGLeafNode(nextElem) || nextElem.getInfo().isControlConfined()) {
			return false;
		}
		if (nextElem instanceof IfStatement) {
			IfStatement ifStmt = (IfStatement) nextElem;
			/*
			 * If the lexical contents of this ifStmt contain any break that
			 * corresponds to itStmt, then we should attempt to move this
			 * barrier below the construct, if possible.
			 */
			IfStatementCFGInfo ifInfo = ifStmt.getInfo().getCFGInfo();
			boolean found = false;
			for (Node leaf : ifInfo.getLexicalCFGContents()) {
				if (!(leaf instanceof BreakStatement)) {
					continue;
				}
				BreakStatement breakStmt = (BreakStatement) leaf;
				if (breakStmt.getInfo().getCorrespondingLoopOrSwitch() == loop) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
			/*
			 * Test whether the predicate can be moved to the previous
			 * phase.
			 */
			Expression ifPredicate = ifInfo.getPredicate();
			if (!Phase.isNodeAllowedInNewPhasesAbove(ifPredicate, barrier)) {
				return false;
			}
			System.err.println("Pushing a barrier down a forked path.");
			NodeRemover.removeNode(barrier);
			List<SideEffect> sideEffects = InsertImmediateSuccessor.insert(ifPredicate, barrier);
			// Phase.removeUnreachablePhases();
			if (!Misc.changePerformed(sideEffects)) {
				return false;
			} else {
				return true;
			}
		} else if (nextElem instanceof SwitchStatement) {
			return false;
		} else {
			return false;
		}
	}

	/**
	 * This method attempts to perform aggressive code motion by moving code around
	 * the loop. With the help of {@link
	 * NodeInfo#getJumpedPredicates()}, we ensure that each statement moves only
	 * once across a predicate across its
	 * lifetime.
	 *
	 * @param itStmt
	 *                 loop across which we are attempting to move the code.
	 * @param compStmt
	 *                 compound-statement enclosing the barrier node.
	 *
	 * @return if the code was moved.
	 */
	private boolean tryCodeMotionAroundWhileLoop(WhileStatement whileStmt) {
		Expression predicate = whileStmt.getInfo().getCFGInfo().getPredicate();
		/*
		 * Note that since the driver module only works with predicates which
		 * are "1", we simplify this translation accordingly.
		 */
		if (!predicate.toString().trim().equals("1")) {
			return false;
		}
		/*
		 * Accumulate the list of statements that should be moved across the
		 * back-edges of whilePred.
		 * Let's move the maximum chunk at a time.
		 * Since we have already performed code percolation within the loop, we
		 * should try and move only that code across the back-edges which
		 * doesn't interfere with the previous phases of this barrier.
		 */
		int moveUptil = obtainCodeIndexToBeMoved(whileStmt);
		if (moveUptil == -1) {
			/*
			 * This implies that there is no code which should be moved across
			 * the back-edge. Return.
			 */
			return false;
		}
		System.err.println("\t\tAttempting to move first " + (moveUptil + 1) + " statement(s) across the loop.");
		CompoundStatement compStmt = (CompoundStatement) whileStmt.getInfo().getCFGInfo().getBody();
		CompoundStatementCFGInfo compStmtCFGInfo = compStmt.getInfo().getCFGInfo();
		for (int i = 0; i <= moveUptil; i++) {
			Node elementToBeMoved = compStmtCFGInfo.getElementList().get(0);
			compStmtCFGInfo.removeElement(elementToBeMoved);
			List<SideEffect> sideEffects = InsertImmediatePredecessor.insert(predicate, elementToBeMoved);
			elementToBeMoved.getInfo().getJumpedPredicates().add(predicate);
			assert (Misc.changePerformed(sideEffects));
		}

		for (Node elem : compStmtCFGInfo.getElementList()) {
			if (elem instanceof Statement && elem.toString().trim().equals(";")) {
				compStmtCFGInfo.removeElement(elem);
			}
		}

		return true;
	}

	/**
	 * This function moves some of the statements within the body of a loop to the
	 * top of the loop, and returns the
	 * index of locations up till which (inclusively), the code should be moved
	 * across the back-edges. Note that this
	 * call may have side-effects of re-shuffling the code within first phase of the
	 * loop.
	 *
	 * @param whileStmt
	 *                  loop whose statements have to be moved across the
	 *                  back-edges.
	 *
	 * @return index up till which code should be moved across the back-edge of the
	 *         loop.
	 */
	public int obtainCodeIndexToBeMoved(WhileStatement whileStmt) {
		// Note subtle differences between this code and percolateCodeUpwards(itStmt).
		CompoundStatement compStmt = (CompoundStatement) whileStmt.getInfo().getCFGInfo().getBody();
		CompoundStatementCFGInfo compStmtCFGInfo = compStmt.getInfo().getCFGInfo();
		List<Node> elemList = compStmtCFGInfo.getElementList();
		Expression predicate = whileStmt.getInfo().getCFGInfo().getPredicate();

		int stackPointer = -1; // Pointer to the last valid location (inclusive) in stack of statement that
								// should be moved.
		int movingPointer = 0;
		outer: while (movingPointer < elemList.size()) {
			Node element = elemList.get(movingPointer);
			if (element.getInfo().getJumpedPredicates().contains(predicate)) {
				movingPointer++;
				continue outer;
			}
			if (element instanceof BarrierDirective) {
				return stackPointer;
			} else if (element instanceof Declaration || element instanceof FlushDirective) {
				movingPointer++;
				continue outer;
			}
			if (Misc.isCFGNonLeafNode(element)) {
				Set<NodeWithStack> internalNodes = element.getInfo().getCFGInfo()
						.getIntraTaskCFGLeafContentsOfSameParLevel(new CallStack());
				if (internalNodes.stream().filter(ns -> ns.getNode() instanceof BarrierDirective).count() > 0) {
					/*
					 * Stop percolation upon encountering a barrier.
					 * However, do notice that some syntactic barriers might not
					 * be reachable backwards from the EndNode of a non-leaf
					 * node. Allow percolation in that case, by skipping this
					 * element.
					 */
					if (element instanceof IfStatement) {
						/*
						 * Check if there exists any path upwards from EndNode
						 * that may:
						 * 1. exit the element except at the BeginNode, or
						 * 2. contain a barrier at the same par-level.
						 * If such a path exists, then return from this method,
						 * else skip this element.
						 */
						Set<NodeWithStack> endingElements = new HashSet<>();
						Node startingEndNode = element.getInfo().getCFGInfo().getNestedCFG().getEnd();
						Node endingBeginNode = element.getInfo().getCFGInfo().getNestedCFG().getBegin();
						Set<Node> outJumpSources = element.getInfo().getOutJumpSources();
						CollectorVisitor.collectNodeSetInGenericGraph(
								new NodeWithStack(startingEndNode, new CallStack()), endingElements, (n) -> {
									// if (!internalNodes.stream().anyMatch(nS -> nS.getNode() == n.getNode())) {
									if (outJumpSources.stream().anyMatch(nS -> nS == n.getNode())
											|| n.getNode() == endingBeginNode) {
										return true;
									}
									if (n.getNode() instanceof BarrierDirective) {
										return true;
									}
									return false;
								}, (n) -> n.getNode().getInfo().getCFGInfo()
										.getParallelConstructFreeInterProceduralLeafPredecessors(n.getCallStack()));
						if (endingElements.stream().anyMatch(nS -> nS.getNode() instanceof BarrierDirective)) {
							return stackPointer;
						} else {
							movingPointer++;
							continue outer;
						}
					}
					return stackPointer;
				}
			}
			if (!Phase.isNodeAllowedInNewPhasesAbove(element, (BarrierDirective) this.getNode())) {
				/*
				 * We attempt to move only that code which won't conflict with
				 * phases above "this" barrier.
				 */
				movingPointer++;
				continue outer;
			}
			/*
			 * Ensure that there won't be any control dependences that this node
			 * may break while moving beyond the barrier.
			 */
			if (!element.getInfo().isControlConfined()) {
				// After doing so, make sure that future checks do not assume a return here.
				// return codeMoved;
				movingPointer++;
				continue outer;
			}

			if (!BarrierDirectiveInfo.checkPotentialToCrossUpwards(elemList, stackPointer, movingPointer)) {
				movingPointer++;
				continue outer;
			}

			// Now, we will move the statement upwards in the movable stack.
			List<SideEffect> sideEffects = NodeRemover.removeNode(element);
			// List<UpdateSideEffects> sideEffects = compStmtCFGInfo.removeElement(element);
			if (!Misc.changePerformed(sideEffects)) {
				movingPointer++;
				continue outer;
			}
			for (SideEffect sideEffect : sideEffects) {
				if (sideEffect instanceof IndexIncremented) {
					movingPointer++;
				}
			}
			elemList = compStmtCFGInfo.getElementList();
			sideEffects = compStmtCFGInfo.addElement(stackPointer + 1, element);
			if (!Misc.changePerformed(sideEffects)) {
				movingPointer++;
				continue outer;
			}
			elemList = compStmtCFGInfo.getElementList();
			stackPointer = elemList.indexOf(element); // To be safe from cases when index gets changed unexpectedly due
														// to side-effects.
			movingPointer++;
		}
		return movingPointer;
	}

	/**
	 * This method tests whether a node at index {@code movingPointer} can be
	 * shifted uptil immediately below the
	 * barrier at index {@code indexOfBarrier}.
	 * <br>
	 * NOTE: This method assumes that there are no barriers in any of the
	 * intermediate nodes.
	 *
	 * @param elemList
	 * @param indexOfBarrier
	 * @param movingPointer
	 *
	 * @return
	 */
	public static boolean checkPotentialToCrossUpwards(List<Node> elemList, int indexOfBarrier, int movingPointer) {
		Node element = elemList.get(movingPointer);
		/*
		 * Conservatively, we do not move this node if it has any label
		 * annotations.
		 */
		if (element instanceof Statement) {
			if (!((Statement) element).getInfo().getLabelAnnotations().isEmpty()) {
				return false;
			}
		}

		CellSet movingReadSet = new CellSet(element.getInfo().getReads());
		CellSet movingWriteSet = new CellSet(element.getInfo().getWrites());
		for (int i = movingPointer - 1; i > indexOfBarrier; i--) {
			Node ancestor = elemList.get(i);
			if (ancestor instanceof Statement) {
				Statement stmt = (Statement) ancestor;
				if (!stmt.getInfo().getLabelAnnotations().isEmpty()) {
					/*
					 * We can make this check more precise later.
					 */
					return false;
				}
			}
			CellSet ancestorReadSet = null;
			CellSet ancestorWriteSet = null;
			boolean controlConfined = ancestor.getInfo().isControlConfined();
			if (controlConfined) {
				ancestorReadSet = new CellSet(ancestor.getInfo().getReads());
				ancestorWriteSet = new CellSet(ancestor.getInfo().getWrites());
				if (Misc.doIntersect(movingReadSet, ancestorWriteSet)
						|| Misc.doIntersect(movingWriteSet, ancestorReadSet)
						|| Misc.doIntersect(movingWriteSet, ancestorWriteSet)) {
					return false;
				}
			} else {
				ancestorReadSet = new CellSet();
				ancestorWriteSet = new CellSet();

				/*
				 * If there is any inJumpDestination in N2, then we
				 * conservatively return false.
				 */
				if (!ancestor.getInfo().getInJumpDestinations().isEmpty()) {
					return false;
				}

				/*
				 * If there is any r/w conflict of those nodes in N2 that are
				 * reachable from both sides of N2, with nodes in N1, then we
				 * return
				 * false.
				 */
				for (Node bothWaysReachableLeaf : ancestor.getInfo().getCFGInfo()
						.getBothBeginAndEndReachableIntraTaskLeafNodes()) {
					ancestorReadSet.addAll(bothWaysReachableLeaf.getInfo().getReads());
					ancestorWriteSet.addAll(bothWaysReachableLeaf.getInfo().getWrites());
				}
				if (Misc.doIntersect(movingReadSet, ancestorWriteSet)
						|| Misc.doIntersect(movingWriteSet, ancestorReadSet)
						|| Misc.doIntersect(movingWriteSet, ancestorWriteSet)) {
					return false;
				}

				/*
				 * Finally, we return false if writes in element can kill any
				 * value live-out of any of the node headers of paths that don't
				 * reach the EndNode.
				 */
				if (BarrierDirectiveInfo.killedIfPushedUpwards(ancestor, element)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean checkPotentialToCrossDownwards(List<Node> elemList, int movingPointer, int indexOfBarrier) {
		Node element = elemList.get(movingPointer);
		CellSet movingReadSet = new CellSet(element.getInfo().getReads());
		CellSet movingWriteSet = new CellSet(element.getInfo().getWrites());
		for (int i = movingPointer + 1; i < indexOfBarrier; i++) {
			Node descendant = elemList.get(i);
			if (descendant instanceof Statement) {
				Statement stmt = (Statement) descendant;
				if (!stmt.getInfo().getLabelAnnotations().isEmpty()) {
					/*
					 * We can make this check more precise later.
					 */
					return false;
				}
			}
			CellSet ancestorReadSet = new CellSet(descendant.getInfo().getReads());
			CellSet ancestorWriteSet = new CellSet(descendant.getInfo().getWrites());
			if (Misc.doIntersect(movingReadSet, ancestorWriteSet) || Misc.doIntersect(movingWriteSet, ancestorReadSet)
					|| Misc.doIntersect(movingWriteSet, ancestorWriteSet)) {
				return false;
			}
			if (BarrierDirectiveInfo.killedIfPushedUpwards(element, descendant)) {
				return false;
			}

		}
		return true;
	}

	/**
	 * Assuming that there is no data dependence between both nodes for nodes that
	 * are reachable via both ends of the
	 * {@code aboveNode}, this method informs whether upward motion of
	 * {@code belowNode} may kill some value on any path
	 * going out from {@code aboveNode}. Note that the {@code belowNode} is assumed
	 * to contain no barriers. Furthermore,
	 * conservatively, we assume that the {@code aboveNode} does not have any
	 * inJumpDestinations.
	 *
	 * @param aboveNode
	 * @param belowNode
	 *
	 * @return
	 */
	public static boolean killedIfPushedUpwards(Node aboveNode, Node belowNode) {
		aboveNode = Misc.getCFGNodeFor(aboveNode);
		belowNode = Misc.getCFGNodeFor(belowNode);

		if (aboveNode.getInfo().isControlConfined() && belowNode.getInfo().isControlConfined()) {
			return false;
		}
		if (!aboveNode.getInfo().getInJumpDestinations().isEmpty()) {
			// We can relax this condition later.
			return true;
		}
		if (!belowNode.getInfo().getInJumpDestinations().isEmpty()) {
			// We can relax this condition later.
			return true;
		}
		if (!belowNode.getInfo().getOutJumpSources().isEmpty()) {
			// We can relax this condition later.
			return true;
		}
		if (aboveNode.getInfo().getOutJumpSources().isEmpty()) {
			return false;
		}

		CellSet belowWrites = new CellSet(belowNode.getInfo().getWrites());
		Set<Node> unreachableHeaders = aboveNode.getInfo().getCFGInfo().getEndUnreachableLeafHeaders();
		if (unreachableHeaders.isEmpty()) {
			return false;
		}
		/*
		 * Conservatively, we do not perform this translation if there exists
		 * any other read to the variable being written in the common phase.
		 */
		CellSet belowSharedWrites = new CellSet(belowNode.getInfo().getSharedWrites());
		final Set<Node> belowSet = belowNode.getInfo().getCFGInfo().getIntraTaskCFGLeafContents();
		final Node belowFinalNode = belowNode;
		if (belowNode.getInfo().getNodePhaseInfo().getPhaseSet().stream()
				.anyMatch(p -> p.getNodeSet().stream()
						.anyMatch(n -> (!belowSet.contains(n)
								&& Misc.doIntersect(belowSharedWrites, n.getInfo().getSharedReads())
								&& CoExistenceChecker.canCoExistInPhase(belowFinalNode, n, p))))) {
			return true;
		}

		for (Node outJumpSrc : unreachableHeaders) {
			NodeWithStack startPoint = new NodeWithStack(outJumpSrc, new CallStack());
			Set<NodeWithStack> endPoints = new HashSet<>();
			for (Cell w : belowWrites) {
				final DataSharingAttribute dsa = aboveNode.getInfo().getSharingAttribute(w);
				CollectorVisitor.collectNodeSetInGenericGraph(startPoint, endPoints, (n) -> {
					CellSet readsOfN = new CellSet(n.getNode().getInfo().getReads());
					if (readsOfN.contains(w)) {
						return true;
					} else {
						return false;
					}
				}, (n) -> {
					Set<NodeWithStack> retSet = new HashSet<>();
					Set<NodeWithStack> successors = (dsa == DataSharingAttribute.SHARED)
							? n.getNode().getInfo().getCFGInfo().getInterTaskLeafSuccessorNodes(n.getCallStack())
							: n.getNode().getInfo().getCFGInfo().getInterProceduralLeafSuccessors(n.getCallStack());
					for (NodeWithStack succNode : successors) {
						CellSet writesOfN = new CellSet(n.getNode().getInfo().getWrites());
						if (writesOfN.size() == 1 && writesOfN.contains(w)) {
							// Skip.
						} else {
							retSet.add(succNode);
						}
					}
					return retSet;
				});
				if (!endPoints.isEmpty()) {
					return true;
				}
			}
		}

		return false;
	}

	public Set<Node> getMovableNodes() {
		return movableNodes;
	}

	public void setMovableNodes(Set<Node> movableNodes) {
		this.movableNodes = movableNodes;
	}

	public Set<Node> getNonMovableNodes() {
		return nonMovableNodes;
	}

	public void setNonMovableNodes(Set<Node> nonMovableNodes) {
		this.nonMovableNodes = nonMovableNodes;
	}

	public Set<Node> getReachableNodes() {
		return reachableNodes;
	}

	public void setReachableNodes(Set<Node> reachableNodes) {
		this.reachableNodes = reachableNodes;
	}

	public Set<Node> getExplicitSiblings() {
		Set<Node> siblings = new HashSet<>();
		BarrierDirective barrier = (BarrierDirective) this.getNode();
		for (AbstractPhase<?, ?> ph : new HashSet<>(barrier.getInfo().getNodePhaseInfo().getPhaseSet())) {
			boolean found = false;
			for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
				if (endingPhasePoint.getNodeFromInterface() == barrier) {
					found = true;
					break;
				}
			}
			if (!found) {
				continue;
			}
			for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
				if (!(endingPhasePoint.getNodeFromInterface() instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNodeFromInterface();
				if (siblingBarrier == barrier) {
					continue;
				}
				if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON
						&& Program.sveSensitive == SVEDimension.SVE_SENSITIVE
						&& !CoExistenceChecker.canCoExistInPhase(barrier, siblingBarrier, ph)) {
					continue;
				}
				siblings.add(siblingBarrier);
			}
		}
		return siblings;
	}

	/**
	 * This method attempts to perform aggressive code motion by moving code around
	 * the loop. With the help of {@link
	 * NodeInfo#getJumpedPredicates()}, we ensure that each statement moves only
	 * once across a predicate across its
	 * lifetime.
	 *
	 * @param itStmt
	 *                 loop across which we are attempting to move the code.
	 * @param compStmt
	 *                 compound-statement enclosing the barrier node.
	 *
	 * @return if the code was moved.
	 */
	@Deprecated
	private boolean tryCodeMotionAroundLoop(IterationStatement itStmt, CompoundStatement compStmt) {
		/*
		 * Verify if the arguments ensure that compStmt is the body of itStmt.
		 * Else, return.
		 */
		if (Misc.getEnclosingCFGNonLeafNode(compStmt) != itStmt) {
			return false;
		}

		/*
		 * Let's handle only while loop, to begin with. We can add similar code
		 * for do-while, and for later.
		 */
		if (itStmt instanceof WhileStatement) {
			return tryCodeMotionAroundWhileLoop((WhileStatement) itStmt);
		} else if (itStmt instanceof DoStatement) {
			// TODO: Handle this later.
			return false;
		} else if (itStmt instanceof ForStatement) {
			// TODO: Handle this later.
			return false;
		}

		// For all other types of loops.
		return false;
	}

	/**
	 * Assuming that there is no data dependence between both nodes, this method
	 * informs whether motion of one above the
	 * other may kill some value on any path. Note that the second node is assumed
	 * to be control-confined, and does not
	 * contain barriers or flushes.
	 *
	 * @param aboveNode
	 * @param belowNode
	 *
	 * @return
	 *
	 * @See {@link #killedIfPushedUpwards(Node, Node)} instead.
	 */
	@Deprecated
	public static boolean killedIfPushedBelow(Node aboveNode, Node belowNode) {
		aboveNode = Misc.getCFGNodeFor(aboveNode);
		belowNode = Misc.getCFGNodeFor(belowNode);

		if (aboveNode.getInfo().isControlConfined()) {
			return false;
		}
		if (aboveNode.getInfo().getInJumpDestinations().size() != 0) {
			// We can relax this condition later.
			return true; // This was false initially, which seems to be incorrect.
		}
		if (aboveNode.getInfo().getOutJumpSources().isEmpty()) {
			return false; // This was true initially, which seems to be incorrect.
		}
		CellSet belowWrites = new CellSet(belowNode.getInfo().getWrites());
		for (Node outJumpSrc : aboveNode.getInfo().getOutJumpSources()) {
			NodeWithStack startPoint = new NodeWithStack(outJumpSrc, new CallStack());
			Set<NodeWithStack> endPoints = new HashSet<>();
			for (Cell w : belowWrites) {
				CollectorVisitor.collectNodeSetInGenericGraph(startPoint, endPoints, (n) -> {
					CellSet readsOfN = new CellSet(n.getNode().getInfo().getReads());
					if (readsOfN.contains(w)) {
						return true;
					} else {
						return false;
					}
				}, (n) -> {
					Set<NodeWithStack> retSet = new HashSet<>();
					for (NodeWithStack succNode : n.getNode().getInfo().getCFGInfo()
							.getInterTaskLeafSuccessorNodes(n.getCallStack())) {
						CellSet writesOfN = new CellSet(n.getNode().getInfo().getWrites());
						if (writesOfN.size() == 1 && writesOfN.contains(w)) {
							// Skip.
						} else {
							retSet.add(succNode);
						}
					}
					return retSet;
				});
				if (endPoints.size() != 0) {
					return true;
				}
			}
		}

		return false;
		// Old code, from which this method has been extracted.
		// if (!ancestor.getInfo().isControlConfined()) {
		// if (ancestor.getInfo().getInJumpSources().size() != 0) {
		// return false;
		// }
		// for (Node outJumpSrc : ancestor.getInfo().getOutJumpSources()) {
		// // Note that there are no conditional jumps in C.
		// LivenessFlowFact lff = (LivenessFlowFact)
		// outJumpSrc.getInfo().getOUT(AnalysisName.LIVENESS);
		// assert (lff != null) : "Problem with liveness information of: " + outJumpSrc
		// + "@line#"
		// + Misc.getLineNum(outJumpSrc);
		// Set<Node> outJumpSucc =
		// outJumpSrc.getInfo().getCFGInfo().getInterProceduralLeafSuccessors();
		// for (Node succ : lff.livenessInfo.keySet()) {
		// // Ignore stale data.
		// if (!outJumpSucc.contains(succ)) {
		// continue;
		// }
		// // System.out.println("Checking with: " + succ + " edge of " + outJumpSrc);
		// if (Misc.doIntersect(movingWriteSet, lff.livenessInfo.get(succ))) {
		// continue;
		// }
		// }
		// }
		// }
	}

	/**
	 * Assuming that there is no data dependence between both nodes, this method
	 * informs whether motion of one below the
	 * other may kill some value on any path. Note that the {@code aboveNode} is
	 * assumed to be control-confined, and
	 * does not contain barriers or flushes.
	 *
	 * @param aboveNode
	 * @param belowNode
	 *
	 * @return
	 */
	@Deprecated
	public static boolean killedIfPushedAbove(Node aboveNode, Node belowNode) {
		aboveNode = Misc.getCFGNodeFor(aboveNode);
		belowNode = Misc.getCFGNodeFor(belowNode);

		if (belowNode.getInfo().isControlConfined()) {
			return false;
		}
		if (belowNode.getInfo().getInJumpDestinations().size() != 0) {
			// We can relax this condition later.
			return true; // This was false initially, which seems to be incorrect.
		}

		if (belowNode.getInfo().getOutJumpSources().isEmpty()) {
			return false; // This was true initially, which seems to be incorrect.
		}

		CellSet aboveWrites = new CellSet(aboveNode.getInfo().getWrites());
		for (Node outJumpSrc : belowNode.getInfo().getOutJumpSources()) {
			NodeWithStack startPoint = new NodeWithStack(outJumpSrc, new CallStack());
			Set<NodeWithStack> endPoints = new HashSet<>();
			for (Cell w : aboveWrites) {
				CollectorVisitor.collectNodeSetInGenericGraph(startPoint, endPoints, (n) -> {
					CellSet readsOfN = new CellSet(n.getNode().getInfo().getReads());
					if (readsOfN.contains(w)) {
						return true;
					} else {
						return false;
					}
				}, (n) -> {
					Set<NodeWithStack> retSet = new HashSet<>();
					for (NodeWithStack succNode : n.getNode().getInfo().getCFGInfo()
							.getInterTaskLeafPredecessorNodes(n.getCallStack())) {
						// TODO: Check if the method call above is correct.
						CellSet writesOfN = new CellSet(n.getNode().getInfo().getWrites());
						if (writesOfN.size() == 1 && writesOfN.contains(w)) {
							// Skip.
						} else {
							retSet.add(succNode);
						}
					}
					return retSet;
				});
				if (endPoints.size() != 0) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * This method percolates the movable statements above the barrier. It ensures
	 * that the code motion is accompanied
	 * with updates in the relevant data structures.
	 *
	 * @return
	 */
	@Deprecated
	public boolean old_PercolateCodeUpwards() {
		boolean codeMoved = false;
		// Step I: Find the index of the barrier in its compound statement.
		List<Node> containingList = Misc.getContainingList(getNode());
		int indexOfBarrier = Misc.getIndexInContainingList(getNode());

		/*
		 * Step II: Starting at the indexOfBarrier, process each node below it
		 * in the containing list, and see if it can be moved above the barrier.
		 */
		int currentStmtIndex = indexOfBarrier + 1;
		while (currentStmtIndex < containingList.size()) {
			/*
			 * Starting at the currentStmtIndex, given a statement at the
			 * newPosition,
			 * our aim is to move it to the least possible index in the
			 * containingList.
			 */
			int newPosition = currentStmtIndex;
			Node currentStmt = Misc.getInternalFirstCFGNode(containingList.get(newPosition));

			/*
			 * If the currentStmt contains a barrier, we should not move it.
			 */
			if (currentStmt.getInfo().hasBarrierInCFG()) {
				break;
			}

			/*
			 * If we reach a node with a function call in it, we can stop
			 * proceeding further.
			 * TODO: Remove this restriction by analyzing how heap/global data
			 * is read/written inside the functions.
			 */
			for (imop.lib.cg.CallSite cS : currentStmt.getInfo().getCallSites()) {
				if (cS.calleeDefinition != null) {
					break;
				}
			}

			/*
			 * Similarly, stop processing if the currentStmt is a labeled node
			 * unless we define how to handle such cases. (TODO)
			 */
			if (currentStmt instanceof Statement
					&& (((Statement) currentStmt).getInfo().getLabelAnnotations().size() > 0)) {
				break;
			}

			/*
			 * Stop processing if the currentStmt is a jump statement, or a
			 * non-confined block.
			 * TODO: Define how to process such cases, as and when needed.
			 */
			if (!currentStmt.getInfo().deprecated_isControlConfined()) {
				break;
			}

			/*
			 * If at the end the statement won't move above the barrier,
			 * then we don't need to process this statement.
			 */
			if (!this.getMovableNodes().contains(currentStmt)) {
				currentStmtIndex++;
				continue;
			}

			while (newPosition > indexOfBarrier) {
				if (newPosition > indexOfBarrier + 1) {
					// Here, we are trying to swap two non-barrier statements.
					Node baseStmt = Misc.getInternalFirstCFGNode(containingList.get(newPosition - 1));
					if (!Misc.getAllDataDependees(currentStmt).contains(baseStmt)) {
						BasicTransform.crudeSwapBaseWithNew(baseStmt, currentStmt);
					} else {
						// Found a dependence, above which we can't move!
						break;
					}
				} else {
					// Let's try and swap the current statement with the barrier.
					BasicTransform.crudeSwapBaseWithNew(getNode(), currentStmt);
					indexOfBarrier++;
					System.out.println("Called swap on the currentStmt: ");
					currentStmt.getInfo().printNode();
					System.out.println("***");
					codeMoved = true;

					/*
					 * In the following few lines, let's try to make sure that
					 * the phase information is updated correctly.
					 */
					// Updating the node-set of the phases.
					// Note that various constituents of the moved statement may be present
					// individually
					// in the phase list. In such a case, we should work with all the CFG nodes
					// present in the moved statement.

					Set<Node> movedNodes = currentStmt.getInfo().getCFGInfo().getLexicalCFGContents();
					for (AbstractPhase<?, ?> tempPh : currentStmt.getInfo().getNodePhaseInfo().getPhaseSet()) {
						for (Node movedNode : movedNodes) {
							tempPh.deprecated_crudeRemoveNode(movedNode);
						}
					}
					for (AbstractPhase<?, ?> tempPh : this.getNodePhaseInfo().getPhaseSet()) {
						for (Node movedNode : movedNodes) {
							tempPh.deprecated_crudeAddNode(movedNode);
						}
					}

					// Updating the phaseList of the currentStmts
					for (Node movedNode : movedNodes) {
						movedNode.getInfo().getNodePhaseInfo()
								.crudeSetPhaseSet(new HashSet<>(this.getNodePhaseInfo().getPhaseSet()));
					}

					break;
				}
				newPosition--;
			}

			currentStmtIndex++;
		}

		return codeMoved;
	}

}
