/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform;

import imop.ast.info.DataSharingAttribute;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.Assignment;
import imop.lib.analysis.AssignmentGetter;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Definition;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.transform.updater.NodeRemover;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.lib.util.CellList;
import imop.lib.util.CellMap;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CopyEliminator {

	private static int debugCounter = 0;

	/**
	 * Using the copy propagation analysis info, this method performs copy
	 * replacement, which would eventually help in removal of returned copy
	 * definitions, if they become dead.
	 * 
	 * @param root
	 *             node under which copy propagation has to be performed.
	 * @return
	 *         set of all those nodes which were source of replaced copies.
	 */
	public static Set<Node> replaceCopiesIn(Node root) {
		Set<Node> copySources = new HashSet<>();
		root = Misc.getCFGNodeFor(root);
		for (Node leafNode : root.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			CellMap<Cell> map = leafNode.getInfo().getRelevantCopyMap();
			if (map.isEmpty()) {
				continue;
			}
			CellSet reads = new CellSet(leafNode.getInfo().getReads());
			if (reads.isEmpty()) {
				continue;
			}
			if (!reads.getReadOnlyInternal().stream().anyMatch(r -> map.containsKey(r))) {
				continue;
			}
			CellSet writes = new CellSet(leafNode.getInfo().getWrites());
			CellSet replacedSymbols = new CellSet();

			/*
			 * Note that we should not add reads to any shared variable
			 * if it is already written anywhere else in any of its
			 * current phases (without a flush, specially, due to which
			 * we wouldn't have been able to observe that write at this
			 * leafNode). Otherwise, we will add an atomicity race!
			 * So next, we obtain a list of shared variables that may have been
			 * written to anywhere in the phases in which leafNode may exist
			 * (including the leafNode itself).
			 */
			CellSet conflictingWrites = new CellSet();
			/*
			 * For a given phase, this map contains a set of BeginNodes of
			 * uni-task Single and Master nodes in the phase.
			 */
			HashMap<Phase, Set<BeginNode>> uniNodeBegins = new HashMap<>();
			for (AbstractPhase<?, ?> absPh : leafNode.getInfo().getNodePhaseInfo().getPhaseSet()) {
				Phase ph = (Phase) absPh;
				ph.getNodeSet().stream().forEach(n -> conflictingWrites.addAll(n.getInfo().getSharedWrites()));
				Set<BeginNode> uniNodes = new HashSet<>();
				for (Node internal : ph.getNodeSet()) {
					if (internal instanceof BeginNode) {
						BeginNode begin = (BeginNode) internal;
						Node parent = begin.getParent();
						if (parent instanceof SingleConstruct) {
							SingleConstruct singleCons = (SingleConstruct) parent;
							if (!singleCons.getInfo().isLoopedInPhase()) {
								uniNodes.add(begin);
							}
						} else if (parent instanceof MasterConstruct) {
							uniNodes.add(begin);
						}
					}
				}
				uniNodeBegins.put(ph, uniNodes);
			}

			HashMap<String, String> replaceMap = new HashMap<>();
			CellSet allCells = leafNode.getInfo().getAllCellsAtNode();
			rLoop: for (Cell r : reads) {
				if (!map.nonGenericKeySet().contains(r)) {
					continue;
				}
				if (writes.contains(r)) {
					/*
					 * Conservatively, we ignore replacement of r with
					 * target in this update node.
					 */
					continue;
				}
				Cell target = map.get(r);
				if (!allCells.contains(target)) {
					/*
					 * A reachable copy, of the form A=B, might not be
					 * replaceable if B is not in the scope at the leafNode.
					 */
					continue;
				}
				// Perform uni-task checks, if any conflicting write found.
				if (conflictingWrites.contains(target)) {
					/*
					 * We should not add read to a variable that may be
					 * written anywhere in the phase(s) of the leafNode.
					 * However, we should ignore the conflicting node if any
					 * of the following conditions is true:
					 * 1. leafNode shares a uniTask single with the
					 * conflictingNode.
					 * 2. leafNode and conflictingNode both belong to master
					 * construct(s).
					 */
					for (AbstractPhase<?, ?> absPh : leafNode.getInfo().getNodePhaseInfo().getPhaseSet()) {
						Phase ph = (Phase) absPh;
						/*
						 * Step 1: Ensure that no other node, apart from
						 * those in uni-tasks write to the target.
						 */
						Set<Node> otherNodes = new HashSet<>();
						otherNodes.addAll(ph.getNodeSet());
						for (BeginNode uniNodeBegin : uniNodeBegins.get(ph)) {
							Node parent = uniNodeBegin.getParent();
							otherNodes.removeAll(parent.getInfo().getCFGInfo().getIntraTaskCFGLeafContents());
						}
						for (Node otherNode : otherNodes) {
							if (otherNode.getInfo().getWrites().contains(target)) {
								// Unsafe to perform the translation; skip.
								continue rLoop;
							}
						}

						/*
						 * Step 2: Check if leafNode is a part of a master
						 * or uni-task single node. If not, then no
						 * conflicts can be benign.
						 */
						boolean leafBelongsToMaster = false;
						SingleConstruct enclosingSingleOfLeaf = null;
						for (BeginNode uniNodeBegin : uniNodeBegins.get(ph)) {
							Node parent = uniNodeBegin.getParent();
							if (parent instanceof SingleConstruct) {
								if (parent.getInfo().getCFGInfo().getIntraTaskCFGLeafContents().contains(leafNode)) {
									enclosingSingleOfLeaf = (SingleConstruct) parent;
								}
							} else if (parent instanceof MasterConstruct) {
								if (parent.getInfo().getCFGInfo().getIntraTaskCFGLeafContents().contains(leafNode)) {
									leafBelongsToMaster = true;
								}
							}
						}

						if (!leafBelongsToMaster && enclosingSingleOfLeaf == null) {
							for (BeginNode uniNodeBegin : uniNodeBegins.get(ph)) {
								Node parent = uniNodeBegin.getParent();
								if (parent.getInfo().getCFGInfo().getIntraTaskCFGLeafContents().stream()
										.anyMatch(c -> c.getInfo().getWrites().contains(target))) {
									continue rLoop;
								}
							}
						} else if (leafBelongsToMaster) {
							// If any conflicts appear in single-cons, continue;
							for (BeginNode uniNodeBegin : uniNodeBegins.get(ph)) {
								Node parent = uniNodeBegin.getParent();
								if (parent instanceof SingleConstruct
										&& parent.getInfo().getCFGInfo().getIntraTaskCFGLeafContents().stream()
												.anyMatch(c -> c.getInfo().getWrites().contains(target))) {
									continue rLoop;
								}
							}
						} else if (enclosingSingleOfLeaf != null) {
							// If any conflict appears in any other single or master, continue;
							for (BeginNode uniNodeBegin : uniNodeBegins.get(ph)) {
								Node parent = uniNodeBegin.getParent();
								if (parent == enclosingSingleOfLeaf) {
									continue;
								}
								if (parent.getInfo().getCFGInfo().getIntraTaskCFGLeafContents().stream()
										.anyMatch(c -> c.getInfo().getWrites().contains(target))) {
									continue rLoop;
								}
							}
						}
					}
				}
				if (leafNode.getInfo().getSharingAttribute(target) != leafNode.getInfo().getSharingAttribute(r)) {
					/*
					 * We should not replace a private variable with shared,
					 * or vice-versa.
					 */
					continue;
				}
				replaceMap.put(r.toString(), target.toString());
				replacedSymbols.add(r);
			}
			if (replaceMap.isEmpty()) {
				continue;
			}
			/*
			 * Collect all possible copy sources that are leading to this
			 * replacement.
			 */
			for (Definition rdDef : leafNode.getInfo().getReachingDefinitions()) {
				Node rd = rdDef.getDefiningNode();
				if (!(rd instanceof ExpressionStatement)) {
					continue;
				}
				ExpressionStatement copy = (ExpressionStatement) rd;
				if (!copy.getInfo().isCopyInstruction()) {
					continue;
				}
				for (Cell r : replacedSymbols) {
					if (copy.getInfo().getWrites().contains(r)) {
						copySources.add(copy);
						break;
					}
				}
			}
			System.err.println("\tPerforming copy replacement in " + leafNode + " with " + replaceMap);
			BasicTransform.renameIdsInNodes(leafNode, replaceMap);
		}
		return copySources;
	}

	/**
	 * Given a baseNode, we attempt to remove killers of all copy statements
	 * within it.
	 * <br>
	 * This may eventually help in more aggressive copy replacement, that may
	 * finally render the copy source(s) dead.
	 * 
	 * @param baseNode
	 *                 when {@code inCopySources} is null, we assume it to be same
	 *                 as
	 *                 the set of all copy nodes in baseNode.
	 * @return
	 *         true, if any renaming was performed.
	 */
	public static boolean removeAllCopyKillers(Node baseNode) {
		Set<Node> copySources = new HashSet<>();
		for (ExpressionStatement expStmt : Misc.getInheritedEnclosee(baseNode, ExpressionStatement.class)) {
			if (expStmt.getInfo().isCopyInstruction()) {
				copySources.add(expStmt);
			}
		}
		return removeAllCopyKillers(copySources);
	}

	/**
	 * Given a set of nodes that are sources of copy-replacement, we attempt to
	 * remove all those anti- and output-dependence edges for which any node in
	 * this set may be the source node, with the help of memory renaming.
	 * Note that we do not remove those nodes for a given copy {@code X = Y},
	 * through
	 * which the value of X written at the copy is not live-out.
	 * This method may also result in removal of some dead definitions.
	 * <br>
	 * This may eventually help in more aggressive copy replacement, that may
	 * finally render the copy source(s) dead.
	 * 
	 * @param inCopySources
	 *                      set of copy nodes for which all anti- and
	 *                      output-dependences
	 *                      that originate at the node, have to be removed with the
	 *                      help
	 *                      of memory renaming, if possible; if this field is null,
	 *                      we
	 *                      handle all the copies in program.
	 * @return
	 *         true, if any renaming was performed.
	 */
	public static boolean removeAllCopyKillers(Set<Node> inCopySources) {
		if (inCopySources == null) {
			return removeAllCopyKillers(Program.getRoot());
		}
		if (inCopySources.isEmpty()) {
			return false;
		}
		boolean changed = false;
		Set<Node> copySources = new HashSet<>(inCopySources);
		while (!copySources.isEmpty()) {
			Node copy = Misc.getAnyElement(copySources);
			copySources.remove(copy);
			if (!(copy instanceof ExpressionStatement)) {
				continue;
			}
			ExpressionStatement copyExpStmt = (ExpressionStatement) copy;
			assert (copyExpStmt.getInfo().isCopyInstruction()) : "\n" + copyExpStmt + " is not a copy instruction!";

			/*
			 * Step A: Collect all clean writes that may be destinations of
			 * anti- and output-dependence edges originating out of this copy.
			 * AND through which the writeCell may be live-out.
			 * (Note that is X (assuming X:Y) is read at such node, the
			 * dependence will not prohibit copy replacement.)
			 */
			Set<Node> copyDependenceDestinations = new HashSet<>();
			Cell readCell = copyExpStmt.getInfo().getReads().get(0);
			Cell writeCell = copyExpStmt.getInfo().getWrites().get(0);
			CellSet atStake = new CellSet();
			atStake.add(readCell);
			atStake.add(writeCell); // Just so that we do not move past any writes of X (assuming that the copy is
									// X:Y)
			for (Node depEdgeDestination : copyExpStmt.getInfo().getFirstPossibleKillersForwardExclusively(atStake)) {
				Cell depEdgeDestWrite = depEdgeDestination.getInfo().getCleanWrite();
				if (depEdgeDestWrite == null || depEdgeDestWrite != readCell) {
					continue;
				}
				if (!depEdgeDestination.getInfo().hasUsesForwardsExclusively(writeCell)) {
					continue;
				}
				copyDependenceDestinations.add(depEdgeDestination);
			}
			// Renaming code below:
			inner: while (!copyDependenceDestinations.isEmpty()) {
				Node copyDepDestWriteStmt = Misc.getAnyElement(copyDependenceDestinations);
				copyDependenceDestinations.remove(copyDepDestWriteStmt);

				/*
				 * Heuristic 1: If copyDepDestWriteStmt is of the form "A=A",
				 * simple remove it.
				 * Heuristic 2: If copyDepDestWriteStmt is of the form "A=B",
				 * with flow-fact "B:A", remove it.
				 */
				if (copyDepDestWriteStmt instanceof ExpressionStatement) {
					ExpressionStatement expStmt = (ExpressionStatement) copyDepDestWriteStmt;
					if (expStmt.getInfo().isCopyInstruction()) {
						Cell a = expStmt.getInfo().getWrites().get(0);
						Cell b = expStmt.getInfo().getReads().get(0);
						if (b == a) {
							List<SideEffect> sideEffects = NodeRemover.removeNode(copyDepDestWriteStmt);
							if (Misc.changePerformed(sideEffects)) {
								copySources.remove(copyDepDestWriteStmt);
								changed = true;
								System.err.println("\tRemoved the useless swap definition (1) " + copyDepDestWriteStmt);
								continue inner;
							}
						}
					}

					if (expStmt.getInfo().isCopyInstruction()) {
						Cell a = expStmt.getInfo().getWrites().get(0);
						Cell b = expStmt.getInfo().getReads().get(0);
						CellMap<Cell> map = expStmt.getInfo().getRelevantCopyMap();
						if (map.containsKey(b) && map.get(b) == a) {
							List<SideEffect> sideEffects = NodeRemover.removeNode(copyDepDestWriteStmt);
							if (Misc.changePerformed(sideEffects)) {
								copySources.remove(copyDepDestWriteStmt);
								changed = true;
								System.err.println("\tRemoved the useless swap definition (2) " + copyDepDestWriteStmt);
								// Main.validateRDs();
								continue inner;
							}
						}
					}
				}

				/*
				 * Step B: From among the collected nodes that should be
				 * renamed, obtain a set which could be renamed.
				 * Conservatively, we consider a node to be an invalid candidate
				 * for removal if any of the following are true:
				 * __1. The node reads and writes to the same variable. (Already
				 * _____checked.)
				 * __2. The node's possible uses contain a write as well.
				 * __3. The node's possible uses contain the copyExpStmt as
				 * _____well.
				 * Also, we should collect all those nodes that are
				 * "dead/useless definitions".
				 */
				Set<Node> uses = copyDepDestWriteStmt.getInfo().getAllUsesForwardsExclusively(readCell);
				if (uses.isEmpty()) {
					List<SideEffect> sideEffects = NodeRemover.removeNode(copyDepDestWriteStmt);
					if (Misc.changePerformed(sideEffects)) {
						copySources.remove(copyDepDestWriteStmt);
						changed = true;
						System.err.println("\tRemoved the dead swap definition " + copyDepDestWriteStmt);
						continue inner;
					}
				}

				/*
				 * If there exists any use that may see value from some other
				 * write, we must skip renaming of the associated definition.
				 * Furthermore, if any use is also a write to "sym", we skip the
				 * renaming.
				 * Finally, we also skip when the uses may contain the
				 * copySource for which this writeStmt is a
				 * copyDependenceDestination.
				 */
				if (uses.contains(copyExpStmt)) {
					continue inner;
				}
				for (Node use : uses) {
					if (use.getInfo().getWrites().contains(readCell)) {
						continue inner;
					}
					Set<Definition> rds = use.getInfo().getReachingDefinitions(readCell);
					if (rds.size() != 1) {
						continue inner;
					}
				}

				/*
				 * Step C: Now, "writeStmt" and all its "uses" can be safely
				 * renamed to a new variable.
				 * Make sure that all renamed nodes are removed from
				 * copySources, as well as copyDependenceDestinations.
				 */
				Symbol sym = (Symbol) readCell;
				String tempStr = sym.createAndInsertCopyDeclaration();
				changed = true;
				System.err.println("\tRenaming " + sym + " with " + tempStr + " in " + copyDepDestWriteStmt
						+ " and in its uses:" + uses);

				HashMap<String, String> defMap = new HashMap<>();
				defMap.put(sym.toString(), tempStr);
				Node newWriteStmt = BasicTransform.renameIdsInNodes(copyDepDestWriteStmt, defMap);
				if (copySources.contains(copyDepDestWriteStmt)) {
					copySources.remove(copyDepDestWriteStmt);
					copySources.add(newWriteStmt);
				}
				for (Node use : uses) {
					Node newUse = BasicTransform.renameIdsInNodes(use, defMap);
					if (copySources.contains(use)) {
						copySources.remove(use);
						copySources.add(newUse);
					}
					if (use instanceof ExpressionStatement && copyDependenceDestinations.contains(use)) {
						copyDependenceDestinations.remove(use);
						copyDependenceDestinations.add(newUse);
					}
				}
			}
		}
		return changed;
	}

	/**
	 * Removes all dead copy instructions in {@code root}.
	 * 
	 * @param root
	 *             code under which dead-code elimination of copy instructions
	 *             has to be performed.
	 * @return
	 *         true, if any dead-code elimination was performed.
	 */
	public static boolean eliminateDeadCopies(Node root) {
		boolean changed = false;
		for (Node leafNode : root.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			if (leafNode instanceof Declaration) {
				continue;
			}
			CellList writes = leafNode.getInfo().getWrites();
			if (writes.size() != 1) {
				continue;
			}
			Cell w = writes.get(0);
			/*
			 * If leafNode is a copy-statement of the form "A=A", then we can
			 * safely remove the leafNode.
			 */
			if (leafNode instanceof ExpressionStatement) {
				ExpressionStatement expStmt = (ExpressionStatement) leafNode;
				if (expStmt.getInfo().isCopyInstruction()) {
					Cell a = expStmt.getInfo().getWrites().get(0);
					Cell b = expStmt.getInfo().getReads().get(0);
					if (b == a) {
						List<SideEffect> sideEffects = NodeRemover.removeNode(leafNode);
						if (Misc.changePerformed(sideEffects)) {
							changed = true;
							System.err.println("\tRemoved the useless swap definition (3) " + leafNode);
							continue;
						}
					}
					/*
					 * If leafNode is a copy-statement of the form "A=B", such
					 * that its
					 * IN copy flow-fact contains "B:A", then we can safely
					 * remove the
					 * leafNode.
					 * TODO: Verify the above heuristic as per the OpenMP
					 * semantics.
					 */
					CellMap<Cell> map = expStmt.getInfo().getRelevantCopyMap();
					if (map.containsKey(b) && map.get(b) == a) {
						List<SideEffect> sideEffects = NodeRemover.removeNode(leafNode);
						if (Misc.changePerformed(sideEffects)) {
							changed = true;
							System.err.println("\tRemoved the useless swap definition (4) " + leafNode);
							continue;
						}
					}
					/*
					 * If the value written in this node is not live-out, then
					 * we can delete this instruction.
					 * NOTE: Instead of triggering a costly full-fledged
					 * liveness analysis, we simply perform an inter-task
					 * traversal to
					 * see if this cell has been read anywhere before being
					 * overwritten.
					 */
					if (!expStmt.getInfo().hasUsesForwardsExclusively(w)) {
						// This implies that w is not live-out of this leafNode.
						List<SideEffect> sideEffects = NodeRemover.removeNode(leafNode);
						if (Misc.changePerformed(sideEffects)) {
							changed = true;
							System.err.println("\tRemoved the dead definition " + leafNode);
						}
					}
				}
			}

		}
		return changed;
	}

	public static void detectSwapCode(IterationStatement itStmt) {
		if (!(itStmt instanceof WhileStatement)) {
			return;
		}
		WhileStatement whileStmt = (WhileStatement) itStmt;
		CompoundStatement loopBody = (CompoundStatement) whileStmt.getInfo().getCFGInfo().getBody();
		/*
		 * Step A: Detect all the swap instructions within the body of this
		 * loop.
		 * Note that in this SPMD block, swaps would usually be performed only
		 * by any one thread.
		 * Hence, conservatively, we consider only those instructions that lie
		 * within a single/master construct.
		 * (Another common usage would be conditional execution using
		 * omp_get_thread_num(). We can handle this scenario later, if
		 * required.)
		 */
		Set<CompoundStatement> masterAndSingleBodies = new HashSet<>();
		for (Node begin : loopBody.getInfo().getCFGInfo().getIntraTaskCFGLeafContents().stream()
				.filter(n -> n instanceof BeginNode).collect(Collectors.toSet())) {
			if (begin.getParent() instanceof SingleConstruct) {
				SingleConstruct single = (SingleConstruct) begin.getParent();
				masterAndSingleBodies.add((CompoundStatement) single.getInfo().getCFGInfo().getBody());
			} else if (begin.getParent() instanceof MasterConstruct) {
				MasterConstruct master = (MasterConstruct) begin.getParent();
				masterAndSingleBodies.add((CompoundStatement) master.getInfo().getCFGInfo().getBody());
			}
		}

		/*
		 * Step B: Detect swap instructions within the collected bodies.
		 */
		for (CompoundStatement compStmt : masterAndSingleBodies) {
			List<Node> elementList = compStmt.getInfo().getCFGInfo().getElementList();
			int index = -1;
			elemLoop: for (Node elem : elementList) {
				index++;
				if (elem instanceof ExpressionStatement) {
					ExpressionStatement firstExpStmt = (ExpressionStatement) elem;
					if (firstExpStmt.getInfo().isCopyInstruction()) {
						Assignment firstAssign = AssignmentGetter.getLexicalAssignments(firstExpStmt).get(0);
						Cell tempCell = firstAssign.getLHSLocations().getAnyElement();
						Cell firstCell = firstAssign.getRHSLocations().getAnyElement();
						if (!(firstExpStmt.getInfo().getSharingAttribute(tempCell) == DataSharingAttribute.SHARED)
								|| !(firstExpStmt.getInfo()
										.getSharingAttribute(firstCell) == DataSharingAttribute.SHARED)) {
							continue elemLoop;
						}

						/*
						 * A heuristic that would almost always succeed:
						 * Next two instructions would be copy instructions too,
						 * of the form:
						 * firstCell = secondCell;
						 * secondCell = tempCell;
						 * Let's search for this pattern now.
						 */
						if (index + 2 >= elementList.size()) {
							break elemLoop;
						}
						Node secondNode = elementList.get(index + 1);
						if (!(secondNode instanceof ExpressionStatement)) {
							continue elemLoop;
						}
						ExpressionStatement secondExpStmt = (ExpressionStatement) secondNode;
						if (!secondExpStmt.getInfo().isCopyInstruction()) {
							continue elemLoop;
						}
						Assignment secondAssign = AssignmentGetter.getLexicalAssignments(secondExpStmt).get(0);
						if (secondAssign.getLHSLocations().getAnyElement() != firstCell) {
							continue elemLoop;
						}
						Cell secondCell = secondAssign.getRHSLocations().getAnyElement();

						Node thirdNode = elementList.get(index + 2);
						if (!(thirdNode instanceof ExpressionStatement)) {
							continue elemLoop;
						}
						ExpressionStatement thirdExpStmt = (ExpressionStatement) thirdNode;
						if (!thirdExpStmt.getInfo().isCopyInstruction()) {
							continue elemLoop;
						}
						Assignment thirdAssign = AssignmentGetter.getLexicalAssignments(thirdExpStmt).get(0);
						if (thirdAssign.getLHSLocations().getAnyElement() != secondCell
								|| thirdAssign.getRHSLocations().getAnyElement() != tempCell) {
							continue elemLoop;
						}
						System.out.println("Found the following swapping of " + firstCell + " and " + secondCell + ": {"
								+ firstExpStmt + " " + secondExpStmt + " " + thirdExpStmt + "}");
					}
				}
			}
		}
	}

	/**
	 * Obtain the length of the swap chain, if any, starting with the given
	 * {@code stmt}. For example, {@code C=A, A=B, B=C} has a swap-length of 2.
	 * 
	 * @param stmt
	 *                 statement starting which a swap-chain starts.
	 * @param encloser
	 *                 enclosing compound-statement for the given statement.
	 * @return
	 *         length of the swap-chain. (Note that this can never be 1.)
	 */
	public static int swapLength(ExpressionStatement stmt, CompoundStatement encloser) {
		if (!stmt.getInfo().isCopyInstruction()) {
			return 0;
		}

		int length = 0;
		Cell root = stmt.getInfo().getWrites().get(0);
		Cell first = stmt.getInfo().getReads().get(0);
		if (stmt.getInfo().getSharingAttribute(first) != DataSharingAttribute.SHARED) {
			return 0;
		}
		List<Node> elemList = encloser.getInfo().getCFGInfo().getElementList();
		int nextIndex = elemList.indexOf(stmt);
		if (nextIndex == -1) {
			assert (false);
			return 0;
		}
		nextIndex++;
		while (nextIndex < elemList.size()) {
			Node next = elemList.get(nextIndex++);
			if (!(next instanceof ExpressionStatement)) {
				return 0;
			}
			ExpressionStatement nextStmt = (ExpressionStatement) next;
			if (!nextStmt.getInfo().isCopyInstruction()) {
				return 0;
			}
			Cell temp = nextStmt.getInfo().getWrites().get(0);
			if (first != temp) {
				return 0;
			}
			Cell second = nextStmt.getInfo().getReads().get(0);
			length++;
			/*
			 * Now, the cycle either completes or expands.
			 */
			if (second == root) {
				if (length == 1) {
					return 0;
				} else {
					return length;
				}
			} else {
				first = second;
			}
		}
		return 0;
	}

	/**
	 * Obtain a set of shared clean-writes, given a set of nodes.
	 * 
	 * @param inSet
	 *              set of nodes.
	 * @return
	 *         set of clean write nodes in {@code inSet}.
	 * @deprecate
	 */
	@Deprecated
	public static Set<ExpressionStatement> getSharedCleanWrites(Set<Node> inSet) {
		Set<ExpressionStatement> outSet = new HashSet<>();
		for (Node leafNode : inSet) {
			if (!(leafNode instanceof ExpressionStatement)) {
				continue;
			}
			ExpressionStatement writeStmt = (ExpressionStatement) leafNode;
			Cell cleanCell = writeStmt.getInfo().getCleanWrite();
			if ((cleanCell == null) || !(cleanCell instanceof Symbol)) {
				continue;
			}
			// Consider clean writes to only shared variables.
			if (writeStmt.getInfo().getSharingAttribute(cleanCell) != DataSharingAttribute.SHARED) {
				continue;
			}
			// Ignore arrays.
			Symbol sym = (Symbol) cleanCell;
			if (sym.getType() instanceof ArrayType) {
				continue;
			}
			/*
			 * Step B: Ignore all those writes that do not have
			 * anti-dependence on any previous read.
			 */
			if (!writeStmt.getInfo().hasUsesBackwardsExclusively(cleanCell)) {
				continue;
			}
			outSet.add(writeStmt);
		}
		return outSet;
	}

	/**
	 * Using the copy propagation analysis info, this method performs copy
	 * replacement, which would eventually help in removal of returned copy
	 * definitions, if they become dead.
	 * 
	 * @param root
	 *             node under which copy propagation has to be performed.
	 * @return
	 *         set of all those symbols which were replaced by some other symbol
	 *         that they were a copy of, at any program point.
	 * @deprecated
	 * @see #removeAllCopyKillers(HashSet)
	 */
	@Deprecated
	public static CellSet replaceAllCopies(Node root) {
		CellSet replacedSymbols = new CellSet();
		root = Misc.getCFGNodeFor(root);
		for (Node leafNode : root.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			CellMap<Cell> map = leafNode.getInfo().getRelevantCopyMap();
			if (map.isEmpty()) {
				continue;
			}

			/*
			 * Note that we should not add reads to any shared variable
			 * if it is already written anywhere else in any of its
			 * current phases (without a flush, specially, due to which
			 * we wouldn't have been able to observe that write at this
			 * leafNode). Otherwise, we will add an atomicity race!
			 * So next, we obtain a list of shared variables that may have been
			 * written to anywhere in the phases in which leafNode may exist.
			 */
			CellSet conflictingWrites = new CellSet();
			for (AbstractPhase<?, ?> ph : leafNode.getInfo().getNodePhaseInfo().getPhaseSet()) {
				ph.getNodeSet().stream().forEach(n -> conflictingWrites.addAll(n.getInfo().getSharedWrites()));
			}

			CellSet reads = new CellSet(leafNode.getInfo().getReads());
			CellSet writes = new CellSet(leafNode.getInfo().getWrites());
			HashMap<String, String> replaceMap = new HashMap<>();
			for (Cell r : reads) {
				if (map.nonGenericKeySet().contains(r) && !writes.contains(r)) {
					Cell target = map.get(r);
					if (target.toString().equals(r.toString())) {
						continue;
					}
					if (conflictingWrites.contains(target)) {
						continue;
					}
					replaceMap.put(r.toString(), target.toString());
					replacedSymbols.add(r);
				}
			}
			if (replaceMap.isEmpty()) {
				continue;
			}
			System.out.println("\tPerforming copy replacement in " + leafNode + " with " + replaceMap + ".");
			BasicTransform.renameIdsInNodes(leafNode, replaceMap);
		}
		return replacedSymbols;
	}

}
