/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.percolate;

import imop.ast.info.cfgNodeInfo.BarrierDirectiveInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.analysis.mhp.incMHP.EndPhasePoint;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.analysis.typeSystem.FunctionType;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.info.CompoundStatementCFGInfo;
import imop.lib.cfg.link.node.CFGLink;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.transform.simplify.RedundantSynchronizationRemoval;
import imop.lib.transform.updater.InsertImmediatePredecessor;
import imop.lib.transform.updater.NodeRemover;
import imop.lib.transform.updater.NodeReplacer;
import imop.lib.transform.updater.sideeffect.AddedDFDPredecessor;
import imop.lib.transform.updater.sideeffect.RemovedDFDSuccessor;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.lib.util.*;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.util.*;
import java.util.stream.Collectors;

public class LoopInstructionsRescheduler {

	public static int counter = 0;
	public static int recCounter = 0;

	public static void resetStaticFields() {
		LoopInstructionsRescheduler.counter = LoopInstructionsRescheduler.recCounter = 0;
	}

	/**
	 * This method attempts to minimize the number of phases by
	 * rescheduling accesses to shared memory in different phases of the loop.
	 * <br>
	 * Note that unlike the basic percolation step, here we handle code
	 * motion of one shared variable at a time.
	 * <br>
	 * Furthermore, this module does not remove any dependence edges by memory
	 * renaming or copy propagation.
	 * That should have been done by the
	 * {{@link DriverModule#run(IterationStatement)}}
	 * method.
	 * 
	 * @param itStmt
	 *               loop that needs to be optimized.
	 */
	public static void tryDifferentSchedulings(IterationStatement itStmt, int minPhaseCount) {
		boolean changed;
		int finalPhaseCount;
		do {
			changed = false;
			DriverModule.performCopyElimination(itStmt);
			for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
				UpwardPercolater.initPercolate(parCons);
			}
			int initialPhaseCount = DriverModule.phaseCountPerExecution(itStmt);
			if (initialPhaseCount <= minPhaseCount) {
				System.out.println("New phase count in the main body: " + DriverModule.phaseCountPerExecution(itStmt));
				return;
			}
			if (!(itStmt instanceof WhileStatement)) {
				System.out.println("New phase count in the main body: " + DriverModule.phaseCountPerExecution(itStmt));
				return;
			}
			WhileStatement whileStmt = (WhileStatement) itStmt;

			/*
			 * Step A: Obtain a list of shared variables that are written in
			 * various
			 * phases of the loop.
			 */
			CellList readCellList = new CellList();
			CellList writtenCellList = new CellList();
			Set<Node> internalNodes = whileStmt.getInfo().getCFGInfo().getIntraTaskCFGLeafContents();
			internalNodes.stream().forEach(n -> {
				readCellList.addAll(n.getInfo().getSharedReads());
				writtenCellList.addAll(n.getInfo().getSharedWrites());
			});
			CellSet multiAccessedCells = new CellSet();

			/*
			 * Consider only those cells that are accessed in more than one
			 * instructions, such that at least one instruction writes to the
			 * cell.
			 * Note that the statement above will add a symbol to the list more
			 * than once only if it comes from more than one leaf node.
			 * Ignore all the function symbols, and arrays (not array fields).
			 */
			for (Cell c : writtenCellList) {
				if (c == Cell.getNullCell()) {
					continue;
				}
				if (c instanceof Symbol) {
					Symbol s = (Symbol) c;
					if (s.getType() instanceof FunctionType || s.getType() instanceof ArrayType) {
						continue;
					}
				}
				int freq = Collections.frequency(writtenCellList.getReadOnlyInternal(), c)
						+ Collections.frequency(readCellList.getReadOnlyInternal(), c);
				if (freq < 2) {
					continue;
				}
				multiAccessedCells.add(c);
			}

			/*
			 * Step B: For each accessed variable, try to achieve a state where
			 * each
			 * accessing instruction is present in consecutive phases, by moving
			 * the "free" instructions against the control-flow (crossing the
			 * back-edge, if required). Note that unlike the basic percolation
			 * step, here we handle code motion of one shared variable at a
			 * time.
			 * We should not change the phase of those instructions that cannot
			 * move freely on both sides of the phase.
			 */
			CellMap<List<Node>> freeInstMap = new CellMap<>();
			for (Cell c : new CellSet(multiAccessedCells)) {
				freeInstMap.put(c, getFreeInstructionsInIteration(itStmt, c, multiAccessedCells, internalNodes));
			}
			/*
			 * Step C: For all free instructions, move the instructions as high
			 * as
			 * possible, against the control flow within the loop, possibly
			 * crossing
			 * the loop header, if needed.
			 * Note that since there exists at least one constrained
			 * instruction, we
			 * will never cross the loop header more than once.
			 * It's important that we re-arrange the nodes in instructionNodes
			 * in
			 * such a way that those nodes that exist in early phases should get
			 * processed first.
			 */
			// /*
			// * Debug code starts.
			// */
			// List<Cell> deterministic = new ArrayList<>();
			// deterministic.add(multiAccessedCells.getReadOnlyInternal().stream().filter(c
			// -> c.toString().equals("diff_imopVarPre77"))
			// .findAny().get());
			// deterministic.add(multiAccessedCells.getReadOnlyInternal().stream()
			// .filter(c -> c.toString().equals("diff")).findAny().get());
			// deterministic.add(multiAccessedCells.getReadOnlyInternal().stream()
			// .filter(c -> c.toString().equals("diff_imopVarPre76")).findAny().get());
			// /*
			// * Debug code ends.
			// */
			//
			// for (Cell cell : deterministic) {
			// }
			for (Cell cell : multiAccessedCells) {
				List<Node> freeInstructionNodes = freeInstMap.get(cell);
				if (freeInstructionNodes.isEmpty()) {
					continue;
				}
				List<Node> orderedNodes = LoopInstructionsRescheduler.obtainOrderedNodes(freeInstructionNodes,
						whileStmt, cell);
				for (Node freeInst : orderedNodes) {
					System.err.println("\t\tMoving the following free instruction: "
							+ (Misc.isCFGLeafNode(freeInst) ? freeInst.toString()
									: freeInst.getClass().getSimpleName() + " for " + cell));
					if (moveFreeInstructionAgainstControlFlowInLoop(freeInst, cell, multiAccessedCells, itStmt,
							internalNodes)) {
						changed = true;
						RedundantSynchronizationRemoval.removeBarriers(itStmt);
					}
				}
			}
			finalPhaseCount = DriverModule.phaseCountPerExecution(itStmt);
			if (finalPhaseCount <= minPhaseCount) {
				break;
			}
			while (DriverModule.performCopyElimination(itStmt)) {
				changed = true;
			}
		} while (changed);
		System.out.println("New phase count in the main body: " + finalPhaseCount);
	}

	private static List<Node> obtainOrderedNodes(List<Node> freeInstructionNodes, WhileStatement whileStmt, Cell cell) {
		List<Node> orderedFreeNodes = new ArrayList<>();
		Set<Node> internalNodes = whileStmt.getInfo().getCFGInfo().getIntraTaskCFGLeafContents();
		if (freeInstructionNodes.isEmpty()) {
			return orderedFreeNodes;
		}

		/*
		 * Step 0: Prepare a map of leaf CFG nodes that may contain phase
		 * information about freeInstructionNodes. Also, create an inverse map.
		 */
		HashMap<Node, Node> freeCFGNodes = new HashMap<>();
		for (Node firstFree : freeInstructionNodes) {
			Node firstFreeLeaf;
			if (Misc.isCFGLeafNode(firstFree)) {
				firstFreeLeaf = firstFree;
			} else {
				assert (Misc.isCFGNode(firstFree));
				firstFreeLeaf = firstFree.getInfo().getCFGInfo().getNestedCFG().getBegin();
			}
			// assert
			// (!firstFreeLeaf.getInfo().getNodePhaseInfo().getStablePhaseSet().isEmpty());
			if (firstFreeLeaf.getInfo().getNodePhaseInfo().getPhaseSet().isEmpty()) {
				continue;
			}
			freeCFGNodes.put(firstFree, firstFreeLeaf);
		}

		/*
		 * Step A: Pick any free instruction inside the loop. Find its leaf CFG
		 * node.
		 */
		Node firstFree = null;
		for (Node first : freeInstructionNodes) {
			if (!first.getInfo().getNodePhaseInfo().getPhaseSet().isEmpty()) {
				firstFree = first;
				break;
			}
		}
		for (Node n : freeCFGNodes.keySet()) {
			if (internalNodes.contains(freeCFGNodes.get(n))) {
				firstFree = n;
				break;
			}
		}
		if (firstFree == null) {
			return orderedFreeNodes;
		}
		orderedFreeNodes.add(firstFree);

		/*
		 * Step B: Pick any phase that ends inside the loop and in which the
		 * first leaf exists.
		 */
		Set<Phase> firstPhases = (Set<Phase>) freeCFGNodes.get(firstFree).getInfo().getNodePhaseInfo().getPhaseSet();
		Phase insidePhase = null;
		outer: for (Phase ph : firstPhases) {
			for (EndPhasePoint epp : ph.getEndPoints()) {
				if (!(epp.getNode() instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective barrier = (BarrierDirective) epp.getNode();
				if (internalNodes.contains(barrier)) {
					insidePhase = ph;
					break outer;
				}
			}
		}
		assert (insidePhase != null);

		/*
		 * Step C: Now, we visit the phase-flow graph and collect relevant free
		 * instruction nodes.
		 */
		Phase currentPhase = insidePhase;
		Set<Phase> visitedPhases = new HashSet<>();
		boolean foundConstrained = false;
		int fixedIndex = 0;
		while (currentPhase != null) {
			/*
			 * For cyclic PFG.
			 */
			if (!visitedPhases.contains(currentPhase)) {
				visitedPhases.add(currentPhase);
			} else {
				break;
			}

			/*
			 * To stop the processing when all nodes have been added.
			 */
			if (!freeInstructionNodes.stream().anyMatch(n -> !orderedFreeNodes.contains(n))) {
				break;
			}

			/*
			 * If this is a constrained phase, set the corresponding flag.
			 * A phase is marked as "constrained", if it contains an access to
			 * this cell that is not in any freeInstruction.
			 */
			for (Node accessNode : currentPhase.getNodeSet().stream()
					.filter(n -> n.getInfo().getSharedAccesses().contains(cell)).collect(Collectors.toList())) {
				if (!freeInstructionNodes.stream().anyMatch(
						f -> f.getInfo().getCFGInfo().getReachableIntraTaskCFGLeafContents().contains(accessNode))) {
					foundConstrained = true;
				}
			}

			for (Node freeOuterNode : freeInstructionNodes) {
				if (orderedFreeNodes.contains(freeOuterNode)) {
					continue;
				}
				Node freeCFGNode = freeCFGNodes.get(freeOuterNode);
				if (freeCFGNode.getInfo().getNodePhaseInfo().getPhaseSet().contains(currentPhase)) {
					if (!foundConstrained) {
						orderedFreeNodes.add(freeOuterNode);
					} else {
						orderedFreeNodes.add(fixedIndex++, freeOuterNode);
					}
				}
			}
			currentPhase = currentPhase.getSuccPhase();
		}
		return orderedFreeNodes;
	}

	/**
	 * This method applies a heuristic to re-schedule various accesses of the
	 * given cell in such a way that accessing-instructions lie in consecutive
	 * phases, and all "free instructions" move maximum distance possible
	 * against the control-flow.
	 * Note that if a cell does not contain any "constrained instruction", then
	 * we do not cross the back-edge for any of its instructions.
	 * <br>
	 * Note that no re-scheduling of accesses to other multiAccessedCells is
	 * performed.
	 * <br>
	 * Furthermore, we do not re-schedule those accesses that cannot move
	 * freely on both sides of their current phase. We label such accesses as
	 * "constrained instructions".
	 * Any access that is not constrained is a "free instruction".
	 * <br>
	 * <b>UPDATE:</b> Now, this method does not move free instructions, but just
	 * collects them and hands them over to
	 * {{@link #tryDifferentSchedulings(IterationStatement)}}.
	 * 
	 * @param itStmt
	 *                           loop to be processed.
	 * @param cell
	 *                           cell whose accesses should be moved.
	 * @param multiAccessedCells
	 *                           set of all those cells that have more than one
	 *                           accesses in the
	 *                           loop, such that at least one access is write.
	 * @param internalNodes
	 *                           set of all the internal leaf nodes of itStmt.
	 * @return
	 *         set of all the free lead nodes of cell in itStmt.
	 */
	private static List<Node> getFreeInstructionsInIteration(IterationStatement itStmt, Cell cell,
			CellSet multiAccessedCells, Set<Node> internalNodes) {
		assert (multiAccessedCells.contains(cell));
		List<Node> instructionNodes = new ArrayList<>();
		List<Node> constrainedNodes = new ArrayList<>();
		if (!(itStmt instanceof WhileStatement)) {
			// For now, we process only while-statements.
			return instructionNodes;
		}
		WhileStatement whileStmt = (WhileStatement) itStmt;
		Expression predicate = whileStmt.getInfo().getCFGInfo().getPredicate();
		if (!predicate.getInfo().evaluatesToConstant()) {
			// The predicate has been replaced by a constant already.
			return instructionNodes;
		}
		CompoundStatement loopBody = (CompoundStatement) whileStmt.getInfo().getCFGInfo().getBody();

		/*
		 * Step A: Find all those instructions that may access this cell within
		 * the loop. Note that we should obtain only that enclosing node for
		 * an instruction which is a sibling element of some preceding
		 * barrier(s), so that the code percolation can be simplified.
		 * Also, prepare a list of those instructions (nodes) that are
		 * "constrained".
		 * Step B: If there are no constrained instructions, we should stop the
		 * processing of this cell.
		 * Otherwise, obtain all the free instructions (in instructionNodes).
		 */

		// Step A: Coming loop will collect all instructionNodes and constrainedNodes.
		List<Node> loopElements = loopBody.getInfo().getCFGInfo().getElementList();
		Set<Phase> visitedPhases = new HashSet<>();
		for (AbstractPhase<?, ?> absPh : predicate.getInfo().getNodePhaseInfo().getPhaseSet()) {
			Phase ph = (Phase) absPh;
			// if (visitedPhases.contains(ph)) {
			// continue;
			// } else {
			// visitedPhases.add(ph);
			// }
			/*
			 * GENERAL STEPS FOR ALL PHASES IN LOOP:
			 * Step A.1: Obtain the list of nodes in this phase, within the
			 * loop,
			 * at the level of the barriers.
			 * Step A.2: Perform checks on the obtained list to identify all
			 * constrainedNodes and instructionNodes.
			 */

			List<Node> thisPhaseNodeList;
			/*
			 * Step A.1.Special: For the special case of a phase in which
			 * predicate may execute,
			 * collect all nodes that lie on the other end of the back-edge
			 * within this loop.
			 * To each such "list" of nodes, append the remaining nodes within
			 * the phase, and process the lists independently.
			 */
			Set<BarrierDirective> visitedBarriers = new HashSet<>();
			for (BeginPhasePoint bpp : ph.getBeginPoints()) {
				if (!(bpp.getNode() instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective barrier = (BarrierDirective) bpp.getNode();
				if (visitedBarriers.contains(barrier)) {
					continue;
				} else {
					visitedBarriers.add(barrier);
				}
				if (!internalNodes.contains(barrier)) {
					continue;
				}
				thisPhaseNodeList = new ArrayList<>();

				/*
				 * Obtain the list below.
				 */
				CFGLink barrCFGLink = CFGLinkFinder.getCFGLinkFor(barrier);
				CompoundStatement barrCompStmt = (CompoundStatement) barrCFGLink.getEnclosingNode();
				List<Node> compList = barrCompStmt.getInfo().getCFGInfo().getElementList();
				int barrIndex = compList.indexOf(barrier);
				for (int i = barrIndex + 1; i < compList.size(); i++) {
					Node element = compList.get(i);
					if (element instanceof BarrierDirective) {
						break;
					}
					if (Misc.getInheritedEnclosee(element, ContinueStatement.class).stream()
							.anyMatch(c -> c.getInfo().getTargetPredicate() == predicate)) {
						thisPhaseNodeList.add(element);
						// Note that we conservatively stop at the first block that may contain a
						// continue statement.
						break;
					}
					thisPhaseNodeList.add(element);
				}

				/*
				 * Obtain the list above.
				 */
				for (int i = 0; i < loopElements.size(); i++) {
					Node element = loopElements.get(i);
					if (element instanceof BarrierDirective) {
						break;
					}
					if (element instanceof DummyFlushDirective) {
						DummyFlushDirective dfd = (DummyFlushDirective) element;
						if (dfd.getDummyFlushType() == DummyFlushType.BARRIER_START) {
							break;
						}
					}
					if (Misc.isCFGNode(element)) {
						Set<NodeWithStack> elementInternalNodes = element.getInfo().getCFGInfo()
								.getIntraTaskCFGLeafContentsOfSameParLevel(new CallStack());
						if (elementInternalNodes.stream().filter(ns -> ns.getNode() instanceof BarrierDirective)
								.count() > 0) {
							if (element instanceof IfStatement) {
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
										},
										(n) -> n.getNode().getInfo().getCFGInfo()
												.getParallelConstructFreeInterProceduralLeafPredecessors(
														n.getCallStack()));
								if (endingElements.stream().anyMatch(nS -> nS.getNode() instanceof BarrierDirective)) {
									break;
								}
							} else {
								break;
							}
						}
					}
					thisPhaseNodeList.add(element);
				}

				/*
				 * Step A.2.Special: Now, analyze the list to obtain
				 * instructionNodes and constrainedNodes.
				 */
				processPhaseNodeList(thisPhaseNodeList, instructionNodes, constrainedNodes, cell, multiAccessedCells);
				// System.out.println("For " + ph.getPhaseId() + ", and barrier #" +
				// Misc.getLineNum(barrier) + ": "+ thisPhaseNodeList.toString());
			}
			Phase thisPhase = ph;
			visitedPhases = new HashSet<>();
			visitedPhases.add(ph);
			while (true) {
				thisPhase = thisPhase.getSuccPhase();
				if (thisPhase == null) {
					break;
				}
				if (!thisPhase.getEndPoints().parallelStream().anyMatch(n -> internalNodes.contains(n.getNode()))) {
					// Elements of this phase are not part of the loop.
					break;
				}
				if (thisPhase.getNodeSet().parallelStream().anyMatch(n -> n == predicate)) {
					break;
				}
				if (visitedPhases.contains(thisPhase)) {
					break;
				} else {
					visitedPhases.add(thisPhase);
				}
				for (BeginPhasePoint bpp : thisPhase.getBeginPoints()) {

					/*
					 * Step A.1: Collect the list of nodes.
					 */
					thisPhaseNodeList = new ArrayList<>();
					if (!(bpp.getNode() instanceof BarrierDirective)) {
						continue;
					}
					BarrierDirective barrier = (BarrierDirective) bpp.getNode();
					if (!internalNodes.contains(barrier)) {
						continue;
					}
					CFGLink barrCFGLink = CFGLinkFinder.getCFGLinkFor(barrier);
					if (barrCFGLink == null) {
						continue;
					}
					CompoundStatement barrCompStmt = (CompoundStatement) barrCFGLink.getEnclosingNode();
					List<Node> compList = barrCompStmt.getInfo().getCFGInfo().getElementList();
					int barrIndex = compList.indexOf(barrier);
					for (int i = barrIndex + 1; i < loopElements.size(); i++) {
						Node element = loopElements.get(i);
						if (element instanceof BarrierDirective) {
							break;
						}
						if (element instanceof DummyFlushDirective) {
							DummyFlushDirective dfd = (DummyFlushDirective) element;
							if (dfd.getDummyFlushType() == DummyFlushType.BARRIER_START) {
								break;
							}
						}
						if (Misc.isCFGNode(element)) {
							Set<NodeWithStack> elementInternalNodes = element.getInfo().getCFGInfo()
									.getIntraTaskCFGLeafContentsOfSameParLevel(new CallStack());
							if (elementInternalNodes.stream().filter(ns -> ns.getNode() instanceof BarrierDirective)
									.count() > 0) {
								if (element instanceof IfStatement) {
									Set<NodeWithStack> endingElements = new HashSet<>();
									Node startingEndNode = element.getInfo().getCFGInfo().getNestedCFG().getEnd();
									Node endingBeginNode = element.getInfo().getCFGInfo().getNestedCFG().getBegin();
									Set<Node> outJumpSources = element.getInfo().getOutJumpSources();
									CollectorVisitor.collectNodeSetInGenericGraph(
											new NodeWithStack(startingEndNode, new CallStack()), endingElements,
											(n) -> {
												if (outJumpSources.stream().anyMatch(nS -> nS == n.getNode())
														|| n.getNode() == endingBeginNode) {
													return true;
												}
												if (n.getNode() instanceof BarrierDirective) {
													return true;
												}
												return false;
											},
											(n) -> n.getNode().getInfo().getCFGInfo()
													.getParallelConstructFreeInterProceduralLeafPredecessors(
															n.getCallStack()));
									if (endingElements.stream()
											.anyMatch(nS -> nS.getNode() instanceof BarrierDirective)) {
										break;
									}
								} else {
									break;
								}
							}
						}
						thisPhaseNodeList.add(element);
					}
					// System.out.println("For " + thisPhase.getPhaseId() + " cell " + cell + ", and
					// barrier #"
					// + Misc.getLineNum(barrier) + ": " + thisPhaseNodeList.toString());
					/*
					 * Step A.2: Process the list to obtain instructionNodes and
					 * constrainedNodes.
					 */
					processPhaseNodeList(thisPhaseNodeList, instructionNodes, constrainedNodes, cell,
							multiAccessedCells);
				}
			}
		}

		/*
		 * Step B: If there are no constrained instructions, we should stop the
		 * processing of this cell.
		 * Otherwise, obtain all the free instructions (in instructionNodes).
		 */
		if (constrainedNodes.isEmpty()) {
			multiAccessedCells.remove(cell);
			return new ArrayList<>();
		} else {
			instructionNodes.removeAll(constrainedNodes);
			if (instructionNodes.isEmpty()) {
				multiAccessedCells.remove(cell);
			}
			return instructionNodes;
		}
	}

	/**
	 * Given a list of executable nodes in a given phase, we populate the sets
	 * {@code instructioNodes} and {@code constrainedNodes} for a given
	 * {@code cell} among all {@code multiAccessedCells} of a certain loop.
	 * 
	 * @param thisPhaseNodeList
	 * @param instructionNodes
	 * @param constrainedNodes
	 */
	private static void processPhaseNodeList(List<Node> thisPhaseNodeList, List<Node> instructionNodes,
			List<Node> constrainedNodes, Cell cell, CellSet multiAccessedCells) {
		/*
		 * Step A: Obtain all instructionNodes as per this list. Return if no
		 * change detected.
		 */
		List<Node> thisInstructionNodes = new ArrayList<>();
		for (Node inst : thisPhaseNodeList) {
			if (inst.getInfo().getSharedAccesses().contains(cell)) {
				thisInstructionNodes.add(inst);
			}
		}
		if (thisInstructionNodes.isEmpty()) {
			// We remove this condition below, since for each phaseNodeList, we should
			// process the node once.
			// || thisInstructionNodes.stream().allMatch(i -> instructionNodes.contains(i)))
			// {}
			return;
		}
		thisInstructionNodes.removeAll(thisInstructionNodes.stream().filter(n -> !n.getInfo().isControlConfined())
				.collect(Collectors.toSet()));

		if (thisInstructionNodes.isEmpty()) {
			return;
		}
		if (thisInstructionNodes.size() > 1) {
			/*
			 * Since this is an access list, clearly there can't be any
			 * instruction which can move to both the ends.
			 * Hence, we add all the nodes to the constrainedNodes, and return.
			 * Note that multiple instruction nodes, reachable from different
			 * beginPhasePoints will not necessarily be constrainedNodes.
			 */
			constrainedNodes.addAll(thisInstructionNodes);
			return;
		}

		Node instructionNode = thisInstructionNodes.get(0);
		if (constrainedNodes.contains(instructionNode)) {
			// If the instructionNode has already been marked as constrained, then return.
			return;
		}

		/*
		 * Step B: Now, we will test if this instructionNode can move to the
		 * beginning and end of thisPhaseNodeList or not. If not, then we save
		 * it as a constrained node.
		 */
		for (Node otherNode : thisPhaseNodeList) {
			/*
			 * Check 1: If there exists any element with a label in this list,
			 * then
			 * we, conservatively, won't move the instructionNode across that
			 * element.
			 */
			if (otherNode instanceof Statement) {
				Statement otherStmt = (Statement) otherNode;
				if (!otherStmt.getInfo().getLabelAnnotations().isEmpty()) {
					if (!constrainedNodes.contains(instructionNode)) {
						constrainedNodes.add(instructionNode);
					}
					return;
				}
			}
			/*
			 * Check 2: If any non-leaf node in this list contains InJumps, then
			 * we assume the instructionNode to be constrained.
			 */
			if (Misc.isCFGNode(otherNode)) {
				if (!otherNode.getInfo().getInJumpDestinations().isEmpty()) {
					if (!constrainedNodes.contains(instructionNode)) {
						constrainedNodes.add(instructionNode);
					}
					return;
				}
			}
		}

		/*
		 * Check 3: Check for forward reachability.
		 */
		if (!LoopInstructionsRescheduler.testFrontReachability(thisPhaseNodeList, instructionNode, cell,
				multiAccessedCells)) {
			if (!constrainedNodes.contains(instructionNode)) {
				constrainedNodes.add(instructionNode);
			}
			return;
		}
		/*
		 * Check 4: Check for backward reachability.
		 */
		if (!LoopInstructionsRescheduler.testBackReachability(thisPhaseNodeList, instructionNode, cell,
				multiAccessedCells)) {
			if (!constrainedNodes.contains(instructionNode)) {
				constrainedNodes.add(instructionNode);
			}
			return;
		}

		instructionNodes.add(instructionNode);
	}

	private static boolean testFrontReachability(List<Node> nodeList, Node inst, Cell cell,
			CellSet multiAccessedCells) {
		if (!inst.getInfo().isControlConfined()) {
			return false;
		}
		int instIndex = nodeList.indexOf(inst);
		CellSet instReadSet = new CellSet(inst.getInfo().getReads());
		CellSet instWriteSet = new CellSet(inst.getInfo().getWrites());
		for (int i = 0; i < instIndex; i++) {
			Node ancestor = nodeList.get(i);
			boolean canJump = true;
			CellSet ancestorReadSet = null;
			CellSet ancestorWriteSet = null;
			boolean controlConfined = ancestor.getInfo().isControlConfined();
			if (controlConfined) {
				ancestorReadSet = new CellSet(ancestor.getInfo().getReads());
				ancestorWriteSet = new CellSet(ancestor.getInfo().getWrites());
				if (Misc.doIntersect(instReadSet, ancestorWriteSet) || Misc.doIntersect(instWriteSet, ancestorReadSet)
						|| Misc.doIntersect(instWriteSet, ancestorWriteSet)) {
					canJump = false;
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
				 * return false.
				 */
				for (Node bothWaysReachableLeaf : ancestor.getInfo().getCFGInfo()
						.getBothBeginAndEndReachableIntraTaskLeafNodes()) {
					ancestorReadSet.addAll(bothWaysReachableLeaf.getInfo().getReads());
					ancestorWriteSet.addAll(bothWaysReachableLeaf.getInfo().getWrites());
				}
				if (Misc.doIntersect(instReadSet, ancestorWriteSet) || Misc.doIntersect(instWriteSet, ancestorReadSet)
						|| Misc.doIntersect(instWriteSet, ancestorWriteSet)) {
					canJump = false;
				}
				if (canJump && BarrierDirectiveInfo.killedIfPushedUpwards(ancestor, inst)) {
					canJump = false;
				}
			}

			if (canJump) {
				continue;
			} else {
				if (ancestor.getInfo().getAccesses().getReadOnlyInternal().stream()
						.anyMatch(c -> c != cell && multiAccessedCells.contains(c))) {
					return false;
				}
				return testFrontReachability(nodeList, ancestor, cell, multiAccessedCells);
			}

		}
		return true;
	}

	private static boolean testBackReachability(List<Node> nodeList, Node inst, Cell cell, CellSet multiAccessedCells) {
		if (!inst.getInfo().isControlConfined()) {
			return false;
		}
		int instIndex = nodeList.indexOf(inst);
		CellSet instReadSet = new CellSet(inst.getInfo().getReads());
		CellSet instWriteSet = new CellSet(inst.getInfo().getWrites());
		for (int i = nodeList.size() - 1; i > instIndex; i--) {
			Node descendant = nodeList.get(i);
			boolean canJump = true;
			CellSet descendantReadSet = new CellSet(descendant.getInfo().getReads());
			CellSet descendantWriteSet = new CellSet(descendant.getInfo().getWrites());
			if (BarrierDirectiveInfo.killedIfPushedUpwards(inst, descendant)) {
				canJump = false;
			}
			if (Misc.doIntersect(instReadSet, descendantWriteSet) || Misc.doIntersect(instWriteSet, descendantReadSet)
					|| Misc.doIntersect(instWriteSet, descendantWriteSet)) {
				canJump = false;
			}

			if (canJump) {
				continue;
			} else {
				if (descendant.getInfo().getAccesses().getReadOnlyInternal().stream()
						.anyMatch(c -> c != cell && multiAccessedCells.contains(c))) {
					return false;
				}
				return testBackReachability(nodeList, descendant, cell, multiAccessedCells);
			}
		}
		return true;
	}

	/**
	 * 
	 * @param freeInst
	 * @param cell
	 * @param multiAccessedCells
	 * @param itStmt
	 * @param internalNodes
	 * @return
	 *         true if freeInst was moved at least one step up.
	 */
	private static boolean moveFreeInstructionAgainstControlFlowInLoop(final Node freeInst, Cell cell,
			CellSet multiAccessedCells, IterationStatement itStmt, Set<Node> internalNodes) {
		if (!(itStmt instanceof WhileStatement)) {
			return false;
		}
		boolean hasFreeInstMoved = false;
		CompoundStatementCFGInfo compStmtCFGInfo = ((CompoundStatement) Misc.getEnclosingBlock(freeInst)).getInfo()
				.getCFGInfo();
		List<Node> elemList = compStmtCFGInfo.getElementList();
		int instPointer = elemList.indexOf(freeInst);
		int indexOfBarrier = -1;
		int i;
		for (i = instPointer - 1; i >= 0; i--) {
			Node elem = elemList.get(i);
			if (elem instanceof BarrierDirective) {
				indexOfBarrier = i;
				break;
			}
			if (elementContainsBarrierForNext(elem)) {
				/*
				 * Until we find ways to move code outside all
				 * non-leaf nodes, we don't have a solution for this
				 * quagmire.
				 * Hence, we use "return" instead of "break".
				 */
				// indexOfBarrier = i;
				// break;
				return false;
			}
		}
		WhileStatement whileStmt = (WhileStatement) itStmt;
		if (indexOfBarrier == -1) {
			// Misc.printToFile("// " + freeInst + "\n" + itStmt, "tM" + (counter - 1) + "_"
			// + (recCounter++) + ".c");
			if (whileStmt.getInfo().getCFGInfo().getBody() == compStmtCFGInfo.getOwner()) {
				boolean couldMove = moveInstructionBeyondLoopHeader(freeInst, itStmt, internalNodes);
				/*
				 * Note that this code motion can happen only once per freeInst
				 * across the loop header, due to existence of
				 * constrainedInstructions.
				 * If we are successful in moving the node across the back-edge,
				 * we should keep moving it up.
				 */
				if (couldMove) {
					hasFreeInstMoved |= moveFreeInstructionAgainstControlFlowInLoop(freeInst, cell, multiAccessedCells,
							itStmt, internalNodes);
					/*
					 * Note that multiple copies of freeInst might have been
					 * created while inserting the freeInst above the
					 * while-predicate.
					 * We need to keep moving these copies against the control
					 * flow.
					 * Furthermore, we need to add this while-predicate in
					 * "getJumpedPredicate()" of these copies.
					 */
					Expression predicate = whileStmt.getInfo().getCFGInfo().getPredicate();
					// for (Node pred :
					// predicate.getInfo().getCFGInfo().getInterProceduralLeafPredecessors()) {
					// if (pred instanceof BeginNode) {
					// // We handle the copies in this direction below this for loop.
					// continue;
					// }
					//
					// Node freeInstCopy = null;
					// Node tempNode = pred;
					// while (!tempNode.toString().trim().equals(freeInst.toString().trim())) {
					// // Note that we would have inserted the freeInst in the same basic-block as
					// that of pred.
					// Set<Node> predecessors = tempNode.getInfo().getCFGInfo()
					// .getInterProceduralLeafPredecessors();
					// if (predecessors.isEmpty()) {
					// tempNode = null;
					// }
					// if (predecessors.size() != 1) {
					// tempNode = null;
					// break;
					// } else {
					// tempNode = Misc.getAnyElement(predecessors);
					// }
					// }
					// if (tempNode != null) {
					// freeInstCopy = tempNode;
					// } else {
					// System.err.println(
					// "Couldn't find the copy of the following instruciton after moving it beyond
					// the back-edge(s): "
					// + freeInst);
					// continue;
					// }
					// freeInstCopy.getInfo().getJumpedPredicates().add(predicate);
					// hasFreeInstMoved |= moveFreeInstructionAgainstControlFlowInLoop(freeInstCopy,
					// cell,
					// multiAccessedCells, itStmt, internalNodes);
					// }
					/*
					 * Assuming that whilePredicate is 1, we would have only one
					 * copy that should lie in the same basic-block as that of
					 * the whileStatement.
					 */
					if (predicate.toString().equals("1")) {
						Node outsideNode = null;
						outer: for (Node predecessor : predicate.getInfo().getCFGInfo()
								.getInterProceduralLeafPredecessors()) {
							if (!(predecessor instanceof BeginNode)) {
								continue;
							}
							final String elementStr = freeInst.toString();
							Set<Node> endNodes = new HashSet<>();
							CollectorVisitor.collectNodeSetInGenericGraph(predecessor, endNodes, n -> {
								if (n instanceof EndNode && n.getParent().toString().equals(elementStr)
										|| n.toString().equals(elementStr)) {
									return true;
								} else {
									return false;
								}
							}, n -> new HashSet<>(n.getInfo().getCFGInfo().getLeafPredecessors()));
							for (Node endNode : endNodes) {
								if (endNode.toString().equals(elementStr)) {
									if (endNode != freeInst) {
										outsideNode = endNode;
										break outer;
									}
								} else if (endNode instanceof EndNode
										&& endNode.getParent().toString().equals(elementStr)) {
									outsideNode = endNode.getParent();
								}
							}
						}
						boolean outsideNodeMoved = false;
						if (outsideNode != null) {
							outsideNodeMoved = moveInstructionAgainstControlFlow(outsideNode, cell, multiAccessedCells);
						}
						if (!outsideNodeMoved) {
							/*
							 * Note that we moved beyond the loop since we
							 * knew that freeInst could have freed this
							 * phase upon motion.
							 * However, the phase would still be in conflict
							 * now due to outsideCopy.
							 * In such a case, we should at least peel out
							 * the first barrier, if possible.
							 * This would give wider berth to other accesses
							 * of cell.
							 */
							// System.err.println(
							// "Could not move the following instruction that was copied outside the loop
							// during its peeling: "
							// + outsideNode + ". Hence, we peel the first barrier.");
							peelFirstBarrier(itStmt);
						}
					}
					return hasFreeInstMoved;
				} else {
					return false;
				}
			} else {
				/*
				 * Until we find ways to move code outside all non-leaf nodes,
				 * we don't have a solution for this quagmire.
				 */
				return false;
			}
		}

		int movingPointer = indexOfBarrier + 1;
		/*
		 * Note the subtle differences between usual percolation, and this one.
		 * We do not percolate any node that may have shared accesses to any
		 * multiAccessedCells (apart from cell).
		 */
		BarrierDirective barrier = (BarrierDirective) elemList.get(indexOfBarrier);
		outer: while (movingPointer <= instPointer) {
			Node element = elemList.get(movingPointer);
			if (element instanceof FlushDirective) {
				movingPointer++;
				continue outer;
			}
			if (!element.getInfo().isControlConfined()) {
				movingPointer++;
				continue outer;
			}
			if (element.getInfo().getAccesses().getReadOnlyInternal().stream()
					.anyMatch(c -> c != cell && multiAccessedCells.contains(c))) {
				movingPointer++;
				continue outer;
			}
			if (!Phase.isNodeAllowedInNewPhasesAbove(element, barrier)) {
				movingPointer++;
				continue outer;
			}
			if (!BarrierDirectiveInfo.checkPotentialToCrossUpwards(elemList, indexOfBarrier, movingPointer)) {
				movingPointer++;
				continue outer;
			}
			// Now, we will move the statement upwards, beyond the barrier.
			List<SideEffect> sideEffects = NodeRemover.removeNode(element);
			// List<UpdateSideEffects> sideEffects = compStmtCFGInfo.removeElement(element);
			if (!Misc.changePerformed(sideEffects)) {
				movingPointer++;
				continue outer;
			}
			for (SideEffect sideEffect : sideEffects) {
				if (sideEffect.getClass().getSimpleName().equals("IndexIncremented")
						|| sideEffect instanceof AddedDFDPredecessor) {
					// TODO: Verify this.
					// if (sideEffect == UpdateSideEffect.INDEX_INCREMENTED
					// || sideEffect == UpdateSideEffect.ADDED_DFD_PREDECESSOR) {
					movingPointer++;
				}
			}
			// Note that the elementList should be refreshed now.
			elemList = compStmtCFGInfo.getElementList();
			sideEffects = compStmtCFGInfo.addElement(indexOfBarrier, element);
			if (!Misc.changePerformed(sideEffects)) {
				Misc.exitDueToError("Cannot add a removed element: " + element);
				movingPointer++;
				continue outer;
			} else {
				if (element == freeInst) {
					hasFreeInstMoved = true;
				}
			}
			elemList = compStmtCFGInfo.getElementList();
			indexOfBarrier++;
			movingPointer++;
		}

		if (hasFreeInstMoved) {
			moveFreeInstructionAgainstControlFlowInLoop(freeInst, cell, multiAccessedCells, itStmt, internalNodes);
			return true; // Note that if we move even one phase up, we have moved up.
		} else {
			return false;
		}

	}

	public static void peelFirstBarrier(IterationStatement itStmt) {
		if (!(itStmt instanceof WhileStatement)) {
			return;
		}
		WhileStatement whileStmt = (WhileStatement) itStmt;
		Set<Node> internalNodes = whileStmt.getInfo().getCFGInfo().getIntraTaskCFGLeafContents();
		/*
		 * Note that we perform peeling only when the barrier is present at the
		 * level of the loop body.
		 */
		CompoundStatementCFGInfo whileBodyInfo = ((CompoundStatement) whileStmt.getInfo().getCFGInfo().getBody())
				.getInfo().getCFGInfo();
		List<Node> whileBodyElements = whileBodyInfo.getElementList();
		int indexOfFirstBarrier = -1;
		for (int i = 0; i < whileBodyElements.size(); i++) {
			Node element = whileBodyElements.get(i);
			if (element instanceof BarrierDirective) {
				indexOfFirstBarrier = i;
				break;
			} else if (LoopInstructionsRescheduler.elementContainsBarrierForNext(element)) {
				return;
			}
			if (!element.getInfo().isControlConfined()) {
				return;
			}
		}
		if (indexOfFirstBarrier == -1) {
			return;
		}
		BarrierDirective barrier = (BarrierDirective) whileBodyElements.get(indexOfFirstBarrier);
		/*
		 * Now, we should verify that shared accesses can indeed be moved down
		 * the barrier.
		 */
		for (int i = 0; i < indexOfFirstBarrier - 1; i++) {
			Node element = whileBodyElements.get(i);
			CellSet sharedAccesses = element.getInfo().getSharedAccesses();
			if (!sharedAccesses.isEmpty() && !Phase.isNodeAllowedInNewPhasesBelow(element, barrier)) {
				/*
				 * In this case, we should try and move this instruction above
				 * the loop header.
				 * That way, we can still push the barrier above.
				 */
				if (moveInstructionBeyondLoopHeader(element, itStmt, internalNodes)) {
					peelFirstBarrier(itStmt);
					return;
				} else {
					// System.err.println("Cannot move the first barrier due to the following
					// instruction: " + element);
					return;
				}
			}
		}
		System.err.println("\t\tPeeling off the top barrier.");
		Expression predicate = whileStmt.getInfo().getCFGInfo().getPredicate();
		if (!predicate.toString().trim().equals("1")) {
			System.err.println("Unable to peel barrier for loops that do not have their predicate as 1.");
			return;
		}
		List<SideEffect> sideEffects = whileBodyInfo.removeElement(barrier);
		if (!Misc.changePerformed(sideEffects)) {
			return;
		}
		boolean first = true;
		for (Node predecessor : predicate.getInfo().getCFGInfo().getInterProceduralLeafPredecessors()) {
			if (predecessor instanceof BeginNode) {
				// Note that we do not wish to move the internal node outside.
				BarrierDirective tempBarrier = FrontEnd.parseAndNormalize("#pragma omp barrier\n",
						BarrierDirective.class);
				sideEffects = InsertImmediatePredecessor.insert(predecessor, tempBarrier);
				if (!Misc.changePerformed(sideEffects)) {
					Misc.exitDueToError("Unable to insert a removed barrier!");
					return;
				}
				continue;
			} else {
				if (first) {
					first = false;
				} else {
					barrier = FrontEnd.parseAndNormalize("#pragma omp barrier\n", BarrierDirective.class);
				}
				sideEffects = InsertImmediatePredecessor.insert(predecessor, barrier);
				if (!Misc.changePerformed(sideEffects)) {
					Misc.exitDueToError("Unable to insert a removed barrier!");
					return;
				}
			}
		}
		return;
	}

	/**
	 * This method obtains a copy of the element that's present beyond the
	 * back-edges of the whileStmt, and swaps it with the provided element.
	 * 
	 * @param element
	 *                a node that should replace, in AST, its copy at the end of the
	 *                loop.
	 * @return
	 *         the copy which was swapped with element; null, if the swap didn't
	 *         happen.
	 */
	private static Node replaceCopyAtLoopEndWith(final Node element, WhileStatement whileStmt) {
		Expression predicate = whileStmt.getInfo().getCFGInfo().getPredicate();
		Node copiedNode = null;
		outer: for (Node predecessor : predicate.getInfo().getCFGInfo().getInterProceduralLeafPredecessors()) {
			if (predecessor instanceof BeginNode) {
				continue;
			}
			/*
			 * Obtain the copy.
			 */
			final String elementStr = element.toString();
			Set<Node> endNodes = new HashSet<>();
			CollectorVisitor.collectNodeSetInGenericGraph(predecessor, endNodes, n -> {
				if (n instanceof EndNode && n.getParent().toString().equals(elementStr)
						|| n.toString().equals(elementStr)) {
					return true;
				} else {
					return false;
				}
			}, n -> new HashSet<>(n.getInfo().getCFGInfo().getLeafPredecessors()));
			for (Node endNode : endNodes) {
				if (endNode.toString().equals(elementStr)) {
					if (endNode != element) {
						copiedNode = endNode;
						break outer;
					}
				} else if (endNode instanceof EndNode && endNode.getParent().toString().equals(elementStr)) {
					copiedNode = endNode.getParent();
				}
			}
		}
		if (copiedNode == null) {
			Misc.warnDueToLackOfFeature("Could not find the internal copy of the following element: " + element, null);
			return null;
		}
		Statement tempStmt = FrontEnd.parseAndNormalize(";", Statement.class);
		List<SideEffect> sideEffects = NodeReplacer.replaceNodes(element, tempStmt);
		sideEffects.addAll(NodeReplacer.replaceNodes(copiedNode, element));
		sideEffects.addAll(NodeReplacer.replaceNodes(tempStmt, copiedNode));
		if (!Misc.changePerformed(sideEffects)) {
			Misc.warnDueToLackOfFeature(
					"Could not replace the existing internal copy of the following element: " + element, null);
			return null;
		} else {
			return copiedNode;
		}
	}

	public static boolean elementContainsBarrierForNext(Node element) {
		element = Misc.getCFGNodeFor(element);
		if (element instanceof BarrierDirective) {
			return true;
		}
		if (Misc.isCFGNode(element)) {
			Set<NodeWithStack> elementInternalNodes = element.getInfo().getCFGInfo()
					.getIntraTaskCFGLeafContentsOfSameParLevel(new CallStack());
			if (elementInternalNodes.stream().filter(ns -> ns.getNode() instanceof BarrierDirective).count() > 0) {
				if (element instanceof IfStatement) {
					Set<NodeWithStack> endingElements = new HashSet<>();
					Node startingEndNode = element.getInfo().getCFGInfo().getNestedCFG().getEnd();
					Node endingBeginNode = element.getInfo().getCFGInfo().getNestedCFG().getBegin();
					Set<Node> outJumpSources = element.getInfo().getOutJumpSources();
					CollectorVisitor.collectNodeSetInGenericGraph(new NodeWithStack(startingEndNode, new CallStack()),
							endingElements, (n) -> {
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
						return true;
					} else {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// private static boolean moveInstructionBeyondLoopHeader(Node freeInst, Cell
	// cell, CellSet multiAccessedCells,
	// IterationStatement itStmt, Set<Node> internalNodes) {}
	private static boolean moveInstructionBeyondLoopHeader(final Node freeInst, IterationStatement itStmt,
			Set<Node> internalNodes) {
		if (!(itStmt instanceof WhileStatement)) {
			return false;
		}
		WhileStatement whileStmt = (WhileStatement) itStmt;
		CompoundStatementCFGInfo compStmtCFGInfo = ((CompoundStatement) Misc.getEnclosingBlock(freeInst)).getInfo()
				.getCFGInfo();
		if (whileStmt.getInfo().getCFGInfo().getBody() != compStmtCFGInfo.getOwner()) {
			return false;
		}

		Expression predicate = whileStmt.getInfo().getCFGInfo().getPredicate();
		Set<Phase> visitedPhases = new HashSet<>();
		/*
		 * Check if this code motion can result in movement across the preceding
		 * barrier.
		 * If not, then return false.
		 */
		Node leafFreeInst = Misc.getCFGNodeFor(freeInst);
		leafFreeInst = Misc.isCFGLeafNode(leafFreeInst) ? leafFreeInst
				: leafFreeInst.getInfo().getCFGInfo().getNestedCFG().getBegin();
		for (AbstractPhase<?, ?> absPh : predicate.getInfo().getNodePhaseInfo().getPhaseSet()) {
			Phase ph = (Phase) absPh;
			if (visitedPhases.contains(ph)) {
				continue;
			} else {
				visitedPhases.add(ph);
			}
			for (BeginPhasePoint bpp : ph.getBeginPoints()) {
				if (!(bpp.getNode() instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective barrier = (BarrierDirective) bpp.getNode();
				if (!bpp.getReachableNodes().contains(leafFreeInst) || !internalNodes.contains(barrier)) {
					continue;
				}

				/*
				 * Now, let's see if freeInst might conflict with phases above
				 * this barrier.
				 */
				if (!Phase.isNodeAllowedInNewPhasesAbove(freeInst, barrier)) {
					return false;
				}
			}
		}
		// System.out.println("Trying to move the following node beyond the loop header:
		// " + freeInst);

		/*
		 * Accumulate the list of statements that should be moved across the
		 * back-edges of whilePred.
		 * Let's move the maximum chunk at a time.
		 * Since we have already performed code percolation within the loop, we
		 * should try and move only that code across the back-edges which
		 * doesn't interfere with the previous phases of this barrier.
		 */
		// int moveUptil = obtainCodeIndexToBeMoved(freeInst, cell, multiAccessedCells,
		// whileStmt, internalNodes);
		int moveUptil = obtainCodeIndexToBeMoved(freeInst, whileStmt, internalNodes);
		if (moveUptil == -1) {
			/*
			 * This implies that there is no code which should be moved across
			 * the back-edge. Return.
			 */
			return false;
		}
		System.err.println("\t\tAttempting to move first " + (moveUptil + 1) + " statement(s) across the loop.");
		int ptr = 0;
		Set<Node> sharedNodesMoved = new HashSet<>();
		for (int i = 0; i <= moveUptil; i++) {
			Node elementToBeMoved = compStmtCFGInfo.getElementList().get(ptr);
			// System.out.println(elementToBeMoved);
			List<SideEffect> sideEffects = compStmtCFGInfo.removeElement(elementToBeMoved);
			if (elementToBeMoved instanceof DummyFlushDirective) {
				ptr++;
				continue;
			}
			if (sideEffects.stream().anyMatch(s -> s instanceof RemovedDFDSuccessor)) {
				// TODO: Test the next line.
				moveUptil--;
				ptr--;
			}
			if (!elementToBeMoved.getInfo().getSharedAccesses().isEmpty()) {
				sharedNodesMoved.add(elementToBeMoved);
			}
			sideEffects = InsertImmediatePredecessor.insert(predicate, elementToBeMoved);
			elementToBeMoved.getInfo().getJumpedPredicates().add(predicate);
			assert (Misc.changePerformed(sideEffects)) : sideEffects + " while attempting to move " + elementToBeMoved;
		}
		/*
		 * Now, let's ensure that all shared nodes within the loop use same AST
		 * node as the one that was above the loop. Furthermore, let's move all
		 * the copies as high as possible.
		 */
		for (Node shared : sharedNodesMoved) {
			Node copy = replaceCopyAtLoopEndWith(shared, whileStmt);
			if (copy != null) {
				moveAsHighAsPossible(copy, Cell.getNullCell(), new CellSet());
			}
		}
		for (Node elem : compStmtCFGInfo.getElementList()) {
			if (elem instanceof Statement && elem.toString().trim().equals(";")) {
				compStmtCFGInfo.removeElement(elem);
			}
		}

		return true;
	}

	/**
	 * Move a given instruction as high as possible.
	 * 
	 * @param inst
	 * @param cell
	 * @param multiAccessedCells
	 */
	private static void moveAsHighAsPossible(Node inst, Cell cell, CellSet multiAccessedCells) {
		Node cfgNode;
		if (Misc.isCFGLeafNode(inst)) {
			cfgNode = inst;
		} else {
			assert (Misc.isCFGNode(inst));
			cfgNode = inst.getInfo().getCFGInfo().getNestedCFG().getBegin();
		}
		Set<Phase> allPhases = (Set<Phase>) cfgNode.getInfo().getNodePhaseInfo().getPhaseSet();
		do {
			boolean moved = moveInstructionAgainstControlFlow(inst, cell, multiAccessedCells);
			if (!moved) {
				break;
			}

			Set<Phase> thisPhases = (Set<Phase>) cfgNode.getInfo().getNodePhaseInfo().getPhaseSet();
			if (Misc.doIntersect(allPhases, thisPhases)) {
				return;
			} else {
				allPhases.addAll(thisPhases);
				continue;
			}
		} while (true);
	}

	/**
	 * 
	 * @param inst
	 * @param cell
	 * @param multiAccessedCells
	 * @return
	 *         true if this instruction was moved at least one phase up.
	 */
	private static boolean moveInstructionAgainstControlFlow(Node inst, Cell cell, CellSet multiAccessedCells) {
		boolean isFreeInstMoved = false;
		CompoundStatementCFGInfo compStmtCFGInfo = ((CompoundStatement) Misc.getEnclosingBlock(inst)).getInfo()
				.getCFGInfo();
		List<Node> elemList = compStmtCFGInfo.getElementList();
		int instPointer = elemList.indexOf(inst);
		int indexOfBarrier = -1;
		int i;
		for (i = instPointer - 1; i >= 0; i--) {
			Node elem = elemList.get(i);
			if (elem instanceof BarrierDirective) {
				indexOfBarrier = i;
				break;
			}
			if (Misc.isCFGNode(elem)) {
				Set<NodeWithStack> elementInternalNodes = elem.getInfo().getCFGInfo()
						.getIntraTaskCFGLeafContentsOfSameParLevel(new CallStack());
				if (elementInternalNodes.stream().filter(ns -> ns.getNode() instanceof BarrierDirective).count() > 0) {
					if (elem instanceof IfStatement) {
						Set<NodeWithStack> endingElements = new HashSet<>();
						Node startingEndNode = elem.getInfo().getCFGInfo().getNestedCFG().getEnd();
						Node endingBeginNode = elem.getInfo().getCFGInfo().getNestedCFG().getBegin();
						Set<Node> outJumpSources = elem.getInfo().getOutJumpSources();
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
							indexOfBarrier = i;
							/*
							 * Until we find ways to move code outside all
							 * non-leaf nodes, we don't have a solution for this
							 * quagmire.
							 * Hence, we use "return" instead of "break".
							 */
							return false;
						}
					} else {
						indexOfBarrier = i;
						/*
						 * Until we find ways to move code outside all non-leaf
						 * nodes, we don't have a solution for this quagmire.
						 * Hence, we use "return" instead of "break".
						 */
						return false;
					}
				}
			}
		}
		if (indexOfBarrier == -1) {
			return false;
		}

		int movingPointer = indexOfBarrier + 1;

		/*
		 * Note the subtle differences between usual percolation, and this one.
		 * We do not percolate any node that may have shared accesses to any
		 * multiAccessedCells (apart from cell).
		 */
		BarrierDirective barrier = (BarrierDirective) elemList.get(indexOfBarrier);
		outer: while (movingPointer <= instPointer) {
			Node element = elemList.get(movingPointer);
			if (element instanceof FlushDirective) {
				movingPointer++;
				continue outer;
			}
			if (!element.getInfo().isControlConfined()) {
				movingPointer++;
				continue outer;
			}
			if (element.getInfo().getAccesses().getReadOnlyInternal().stream()
					.anyMatch(c -> c != cell && multiAccessedCells.contains(c))) {
				movingPointer++;
				continue outer;
			}
			if (!Phase.isNodeAllowedInNewPhasesAbove(element, barrier)) {
				movingPointer++;
				continue outer;
			}
			if (!BarrierDirectiveInfo.checkPotentialToCrossUpwards(elemList, indexOfBarrier, movingPointer)) {
				movingPointer++;
				continue outer;
			}
			// Now, we will move the statement upwards, beyond the barrier.

			List<SideEffect> sideEffects = NodeRemover.removeNode(element);
			// List<UpdateSideEffects> sideEffects = compStmtCFGInfo.removeElement(element);
			if (!Misc.changePerformed(sideEffects)) {
				movingPointer++;
				continue outer;
			}
			for (SideEffect sideEffect : sideEffects) {
				if (sideEffect.getClass().getSimpleName().equals("IndexIncremented")
						|| sideEffect instanceof AddedDFDPredecessor) {
					// TODO: Verify this.
					// if (sideEffect == UpdateSideEffect.INDEX_INCREMENTED
					// || sideEffect == UpdateSideEffect.ADDED_DFD_PREDECESSOR) {
					movingPointer++;
				}
			}
			// Note that the elementList should be refreshed now.
			elemList = compStmtCFGInfo.getElementList();
			sideEffects = compStmtCFGInfo.addElement(indexOfBarrier, element);
			if (!Misc.changePerformed(sideEffects)) {
				Misc.exitDueToError("Cannot add a removed element: " + element);
				movingPointer++;
				continue outer;
			} else {
				if (element == inst) {
					isFreeInstMoved = true;
				}
			}
			elemList = compStmtCFGInfo.getElementList();
			indexOfBarrier++;
			movingPointer++;
		}

		if (isFreeInstMoved) {
			moveInstructionAgainstControlFlow(inst, cell, multiAccessedCells);
			return true; // We return true if even one phase was cleared up.
		} else {
			return false;
		}

	}

	// private static int obtainCodeIndexToBeMoved(Node freeInst, Cell cell, CellSet
	// multiAccessedCells,
	// WhileStatement whileStmt, Set<Node> internalNodes) {}
	private static int obtainCodeIndexToBeMoved(Node freeInst, WhileStatement whileStmt, Set<Node> internalNodes) {
		/*
		 * Note subtle differences between this code,
		 * BarrierDirectiveInfo.obtainCodeIndexToBeMoved(), and
		 * percolateCodeUpwards(itStmt).
		 */
		CompoundStatement compStmt = (CompoundStatement) whileStmt.getInfo().getCFGInfo().getBody();
		CompoundStatementCFGInfo compStmtCFGInfo = compStmt.getInfo().getCFGInfo();
		List<Node> elemList = compStmtCFGInfo.getElementList();
		Expression predicate = whileStmt.getInfo().getCFGInfo().getPredicate();
		int instIndex = elemList.indexOf(freeInst);
		if (instIndex == -1) {
			// System.out.println(
			// "The following instruction doesn't appear directly at the level of the loop,
			// and hence can't be moved across the back-edge: "
			// + freeInst);
			return -1;
		}

		int stackPointer = -1; // Pointer to the last valid location (inclusive) in stack of statement that
								// should be moved.
		int movingPointer = 0;
		outer: while (movingPointer <= instIndex) {
			Node element = elemList.get(movingPointer);
			// Old code: Why would this be required!? This method doesn't move code across
			// barriers.
			// if (element.getInfo().getAccesses().getReadOnlyInternal().stream()
			// .anyMatch(c -> c != cell && multiAccessedCells.contains(c))) {
			// movingPointer++;
			// continue outer;
			// }
			if (element.getInfo().getJumpedPredicates().contains(predicate)) {
				movingPointer++;
				continue outer;
			}
			if (element instanceof BarrierDirective) {
				assert (false);
				return stackPointer;
			} else if (element instanceof Declaration) {
				movingPointer++;
				continue outer;
			} else if (element instanceof FlushDirective) {
				movingPointer++;
				continue outer;
			}
			if (!element.getInfo().isControlConfined()) {
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
				if (sideEffect.getClass().getSimpleName().equals("IndexIncremented")
						|| sideEffect instanceof AddedDFDPredecessor) {
					// TODO: Verify this.
					// if (sideEffect == UpdateSideEffect.INDEX_INCREMENTED
					// || sideEffect == UpdateSideEffect.ADDED_DFD_PREDECESSOR) {
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
		return stackPointer;
	}

	/**
	 * Checks if it is possible to move the given instruction to border of the
	 * enclosing phase on all sides, without having to move any other
	 * instruction that may access other cells from multiAccessedCells.
	 * 
	 * @param itStmt
	 *                           loop to be processed.
	 * @param inst
	 *                           instruction that needs to be tested for motion.
	 * @param cell
	 *                           cell accessed by {@code inst}.
	 * @param multiAccessedCells
	 *                           all cells whose instructions are candidates of
	 *                           motion.
	 * @param internalNodes
	 *                           set of all the internal leaf nodes of itStmt.
	 * @return
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private static boolean canInstructionMoveFreely(IterationStatement itStmt, final Node inst, Cell cell,
			CellSet multiAccessedCells, Set<Node> internalNodes) {
		assert (inst.getInfo().getSharedAccesses().contains(cell));

		Node compElement = null;
		if (inst instanceof JumpStatement) {
			return false;
		} else if (inst instanceof ParameterDeclaration) {
			return false;
		} else if (inst instanceof PreCallNode || inst instanceof PostCallNode) {
			compElement = inst.getParent();
			assert (compElement instanceof CallStatement);
		} else if (inst instanceof IfClause || inst instanceof NumThreadsClause || inst instanceof FinalClause
				|| inst instanceof OmpForReinitExpression || inst instanceof OmpForInitExpression
				|| inst instanceof OmpForCondition || inst instanceof Expression) {
			CFGLink link = CFGLinkFinder.getCFGLinkFor(inst);
			compElement = link.getEnclosingNode();
		} else {
			compElement = inst;
		}

		/*
		 * Now, we will move further up the enclosing non-leaf nodes if
		 * required, until we reach the level where previous and next barriers
		 * exist.
		 * NOTE: If these levels are not same, then we don't move the node.
		 */
		CompoundStatement barrLevelCompStmt = null;
		for (AbstractPhase<?, ?> absPh : compElement.getInfo().getNodePhaseInfo().getPhaseSet()) {
			Phase ph = (Phase) absPh;
			for (BeginPhasePoint bpp : ph.getBeginPoints()) {
				if (!(bpp.getNode() instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective barrier = (BarrierDirective) bpp.getNode();
				if (internalNodes.contains(barrier) && bpp.getReachableNodes().contains(inst)) {
					CompoundStatement bppEncloser = (CompoundStatement) Misc.getEnclosingBlock(barrier);
					if (barrLevelCompStmt == null) {
						barrLevelCompStmt = bppEncloser;
					} else {
						if (barrLevelCompStmt != bppEncloser) {
							return false;
						}
					}
				}
			}
			for (EndPhasePoint epp : ph.getEndPoints()) {
				if (!(epp.getNode() instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective barrier = (BarrierDirective) epp.getNode();
				if (internalNodes.contains(barrier) && ph.getBeginPoints().stream().anyMatch(
						bpp -> bpp.getReachableNodes().contains(inst) && bpp.getNextBarriers().contains(epp))) {
					CompoundStatement eppEncloser = (CompoundStatement) Misc.getEnclosingBlock(barrier);
					if (barrLevelCompStmt == null) {
						barrLevelCompStmt = eppEncloser;
					} else {
						if (barrLevelCompStmt != eppEncloser) {
							return false;
						}
					}
				}
			}
		}

		/*
		 * Now, we find that element of barrLevelCompStmt which contains the
		 * instruction "inst", and no barrier.
		 */
		Node barrLevelCompElement = null;
		for (Node tempElem : barrLevelCompStmt.getInfo().getCFGInfo().getElementList()) {
			if (tempElem.getInfo().getCFGInfo().getReachableIntraTaskCFGLeafContents().contains(inst)) {
				if (tempElem.getInfo().getCFGInfo().getIntraTaskCFGLeafContentsOfSameParLevel().stream()
						.anyMatch(n -> n.getNode() instanceof BarrierDirective)) {
					System.out.println(
							"The preceding barrier and succeeding barrier are not in the same level of compound statement for "
									+ inst);
					return false;
				} else {
					barrLevelCompElement = tempElem;
					break;
				}
			}
		}
		assert (barrLevelCompElement != null);

		if (!canInstructionMoveUpwards(itStmt, barrLevelCompElement, barrLevelCompStmt, cell, multiAccessedCells)) {
			return false;
		}

		if (!canInstructionMoveDownwards(itStmt, barrLevelCompElement, barrLevelCompStmt, cell, multiAccessedCells)) {
			return false;
		}
		return true;
	}

	@Deprecated
	private static boolean canInstructionMoveUpwards(IterationStatement itStmt, Node compElement,
			CompoundStatement compStmt, Cell cell, CellSet multiAccessedCells) {
		CompoundStatementCFGInfo compStmtInfo = compStmt.getInfo().getCFGInfo();
		assert (compStmtInfo.getElementList().contains(compElement));
		assert (compElement.getInfo().getSharedAccesses().contains(cell));
		// int instIndex = compStmtInfo.getElementList().indexOf(compElement);
		return false;
	}

	@Deprecated
	private static boolean canInstructionMoveDownwards(IterationStatement itStmt, Node compElement,
			CompoundStatement compStmt, Cell cell, CellSet multiAccessedCells) {
		CompoundStatementCFGInfo compStmtInfo = compStmt.getInfo().getCFGInfo();
		assert (compStmtInfo.getElementList().contains(compElement));
		assert (compElement.getInfo().getSharedAccesses().contains(cell));
		// int instIndex = compStmtInfo.getElementList().indexOf(compElement);
		return false;
	}

}
