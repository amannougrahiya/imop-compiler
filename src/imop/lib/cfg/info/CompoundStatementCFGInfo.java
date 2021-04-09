/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.info;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.*;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.cfg.NestedCFG;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.transform.simplify.ImplicitBarrierRemover;
import imop.lib.transform.simplify.InsertDummyFlushDirectives;
import imop.lib.transform.simplify.Normalization;
import imop.lib.transform.simplify.SplitCombinedConstructs;
import imop.lib.transform.updater.NodeRemover;
import imop.lib.transform.updater.sideeffect.*;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompoundStatementCFGInfo extends CFGInfo {

	public CompoundStatementCFGInfo(Node owner) {
		super(owner);
	}

	public void setElementList(List<Node> elementList) {
		this.clearElementList();
		for (Node element : elementList) {
			this.addElement(element);
		}
	}

	public List<Node> getElementList() {
		CompoundStatement node = (CompoundStatement) this.getOwner();
		List<Node> elementList = new ArrayList<>();
		for (Node compStmtElement : node.getF1().getNodes()) {
			elementList.add(Misc.getInternalFirstCFGNode(compStmtElement));
		}
		return elementList;
	}

	public void clearElementList() {
		CompoundStatement node = (CompoundStatement) this.getOwner();
		NodeListOptional elementList = node.getF1();
		// TODO: It might be optimal to remove all the statements first, and then remove
		// all the declarations.
		while (!elementList.getNodes().isEmpty()) {
			List<SideEffect> sideEffectList = this.removeElement(elementList.getNodes().get(0));
			for (SideEffect nse : sideEffectList) {
				if (nse instanceof RemovedDFDPredecessor) {
					assert (false);
				} else if (nse instanceof RemovedDFDSuccessor) {
					// Do nothing.
				} else if (nse instanceof UnauthorizedDFDUpdate) {
					this.removeElement(elementList.getNodes().get(1));
				} else {
					Misc.exitDueToError("Cannot handle the side-effect " + nse
							+ " while removing elements of a CompoundStatement. (Or wrong side-effects sent to this place.)");
				}
			}
		}
	}

	/**
	 * Elementary transformation to remove the element at index {@code i} from
	 * this compound-statement.
	 * <br>
	 * Note that this would remove the associated labels as well (i.e., would
	 * keep them attached to the element).
	 * If this is not expected, then use {@link NodeRemover#removeNode(Node)}
	 * instead.
	 * 
	 * @param i
	 *          an index.
	 * @return
	 *         list of side-effects generated while removing the element from
	 *         index {@code i}.
	 */
	public List<SideEffect> removeElement(int i) {
		Node element = this.getElementList().get(i);
		return this.removeElement(element);
	}

	/**
	 * Elementary transformation to remove {@code element} from this
	 * compound-statement.
	 * <br>
	 * Note that this would remove the associated labels as well (i.e., would
	 * keep them attached to the element).
	 * If this is not expected, then use {@link NodeRemover#removeNode(Node)}
	 * instead.
	 * 
	 * @param element
	 *                element to be removed.
	 * @return
	 *         list of side-effects generated while removing the element from
	 *         index {@code i}.
	 */
	public List<SideEffect> removeElement(Node element) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		Node cfgNode = Misc.getCFGNodeFor(element);
		if (cfgNode instanceof Declaration) {
			return this.removeDeclaration((Declaration) cfgNode);
		} else if (cfgNode instanceof Statement) {
			return this.removeStatement((Statement) cfgNode);
		} else {
			assert (false);
			return sideEffectList;
		}
	}

	private List<SideEffect> removeStatement(Statement element) {
		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(element);
		AutomatedUpdater.flushCaches();
		List<SideEffect> sideEffectList = new ArrayList<>();
		element = Misc.getStatementWrapper(element);
		Node cfgNode = Misc.getCFGNodeFor(element);
		if (cfgNode instanceof DummyFlushDirective) {
			// Not allowed. Express back the inability to do so.
			sideEffectList.add(new UnauthorizedDFDUpdate(cfgNode));
			return sideEffectList;
		}
		if (InsertDummyFlushDirectives.requiresRemovalOfDFDs(cfgNode)) {
			return this.removeWithDFDs(cfgNode);
		} else {
			this.commonNodeRemovalModule(cfgNode);
			return sideEffectList;
		}
	}

	public List<SideEffect> removeDeclaration(Declaration declaration) {
		PointsToAnalysis.handleNodeAdditionOrRemovalForHeuristic(declaration);
		AutomatedUpdater.flushCaches();
		List<SideEffect> sideEffectList = new ArrayList<>();
		/*
		 * Verify that this removal doesn't lead to collision of names.
		 */
		List<String> declaredIds = declaration.getInfo().getIDNameList();

		for (String idToBeRemoved : declaredIds) {
			Scopeable enclosingScope = Misc.getEnclosingBlock(this.getOwner());
			if (enclosingScope != null) {
				CellSet allSymbols = ((Node) enclosingScope).getInfo().getAllCellsAtNode();
				if (allSymbols.isUniversal()) {
					for (Symbol visibleVar : Symbol.allSymbols) {
						if (visibleVar.getName().equals(idToBeRemoved)) {
							sideEffectList.add(new NamespaceCollisionOnRemoval(declaration));
							// Old Code:
							// sideEffectList.add(UpdateSideEffects.NO_UPDATE_DUE_TO_NAME_COLLISION);
							// return sideEffectList;
						}
					}
				} else {
					for (Cell visibleCell : allSymbols) {
						if (visibleCell instanceof Symbol) {
							Symbol visibleVar = (Symbol) visibleCell;
							if (visibleVar.getName().equals(idToBeRemoved)) {
								sideEffectList.add(new NamespaceCollisionOnRemoval(declaration));
								// Old Code:
								// sideEffectList.add(UpdateSideEffects.NO_UPDATE_DUE_TO_NAME_COLLISION);
								// return sideEffectList;
							}
						}
					}
				}
			}
		}

		/*
		 * If no collision would occur, proceed with the removal of the
		 * declaration.
		 * Update: Either way, proceed.
		 */
		Symbol sym = declaration.getInfo().getDeclaredSymbol();

		CompoundStatement node = (CompoundStatement) this.getOwner();
		CompoundStatementElement compStmtElementTarget = Misc.getCompoundStatementElementWrapper(declaration);
		if (!node.getF1().getNodes().contains(compStmtElementTarget)) {
			// This implies that the declaration was not present in the element list.
			return sideEffectList;
		}
		Set<Node> rerunNodesForward = AutomatedUpdater.nodesForForwardRerunOnRemoval(declaration);
		Set<Node> rerunNodesBackward = AutomatedUpdater.nodesForBackwardRerunOnRemoval(declaration);
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(declaration);
		AutomatedUpdater.updateInformationForRemoval(declaration);
		AutomatedUpdater.invalidateEnclosedSymbolSets(this.getOwner());

		this.updateCFGForElementRemoval(declaration);

		boolean removed = node.getF1().removeNode(compStmtElementTarget);
		if (removed) {
			node.getInfo().removeDeclarationFromSymbolOrTypeTable(declaration);
			AutomatedUpdater.invalidateSymbolsInNode(compStmtElementTarget);
			Program.invalidColumnNum = Program.invalidLineNum = true;
			AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
			AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after removal is successful.
			// AutomatedUpdater.invalidateSymbolsInNode(compStmtElementTarget);// Added, so
			// that any changes from points-to may be reflected here.
			AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
			Cell.removeCell(sym);
		}

		return sideEffectList;
	}

	private boolean commonNodeRemovalModule(Node element) {
		CompoundStatement node = (CompoundStatement) this.getOwner();
		AutomatedUpdater.removeCachingFromSVEChecks(element);
		CompoundStatementElement compStmtElementTarget = Misc.getCompoundStatementElementWrapper(element);
		if (!node.getF1().getNodes().contains(compStmtElementTarget)) {
			return false;
		}
		Set<Node> rerunNodesForward = AutomatedUpdater.nodesForForwardRerunOnRemoval(element);
		Set<Node> rerunNodesBackward = AutomatedUpdater.nodesForBackwardRerunOnRemoval(element);
		Set<BeginPhasePoint> affectedBeginPhasePoints = AutomatedUpdater
				.updateBPPOrGetAffectedBPPSetUponRemoval(element);
		AutomatedUpdater.updateInformationForRemoval(element);
		this.updateCFGForElementRemoval(element);
		boolean removed = node.getF1().removeNode(compStmtElementTarget);
		if (removed) {
			AutomatedUpdater.invalidateSymbolsInNode(compStmtElementTarget);
			Program.invalidColumnNum = Program.invalidLineNum = true;
			AutomatedUpdater.updatePhaseAndInterTaskEdgesUponRemoval(affectedBeginPhasePoints);
			AutomatedUpdater.updateFlowFactsForward(rerunNodesForward); // Called here after removal is successful.
			// AutomatedUpdater.invalidateSymbolsInNode(compStmtElementTarget); // Added, so
			// that any changes from points-to may be reflected here.
			AutomatedUpdater.updateFlowFactsBackward(rerunNodesBackward);
		}
		return removed;

	}

	private List<SideEffect> removeWithDFDs(Node node) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		if (node instanceof FlushDirective && !(node instanceof DummyFlushDirective)) {
			if (InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.FLUSH_START)) {
				DummyFlushDirective dfd = InsertDummyFlushDirectives.getPredDFD(node, DummyFlushType.FLUSH_START);
				sideEffectList.add(new RemovedDFDPredecessor(dfd, node));
				this.commonNodeRemovalModule(dfd);
			}
			this.commonNodeRemovalModule(node);
		} else if (node instanceof BarrierDirective) {
			if (InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.BARRIER_START)) {
				DummyFlushDirective dfd = InsertDummyFlushDirectives.getPredDFD(node, DummyFlushType.BARRIER_START);
				sideEffectList.add(new RemovedDFDPredecessor(dfd, node));
				this.commonNodeRemovalModule(dfd);
			}
			this.commonNodeRemovalModule(node);
		} else if (node instanceof AtomicConstruct) {
			if (InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.ATOMIC_START)) {
				DummyFlushDirective dfd = InsertDummyFlushDirectives.getPredDFD(node, DummyFlushType.ATOMIC_START);
				sideEffectList.add(new RemovedDFDPredecessor(dfd, node));
				this.commonNodeRemovalModule(dfd);
			}
			if (InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.ATOMIC_END)) {
				DummyFlushDirective dfd = InsertDummyFlushDirectives.getSuccDFD(node, DummyFlushType.ATOMIC_END);
				sideEffectList.add(new RemovedDFDSuccessor(dfd));
				this.commonNodeRemovalModule(dfd);
			}
			this.commonNodeRemovalModule(node);
		} else if (node instanceof CriticalConstruct) {
			if (InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.CRITICAL_START)) {
				DummyFlushDirective dfd = InsertDummyFlushDirectives.getPredDFD(node, DummyFlushType.CRITICAL_START);
				sideEffectList.add(new RemovedDFDPredecessor(dfd, node));
				this.commonNodeRemovalModule(dfd);
			}
			if (InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.CRITICAL_END)) {
				DummyFlushDirective dfd = InsertDummyFlushDirectives.getSuccDFD(node, DummyFlushType.CRITICAL_END);
				sideEffectList.add(new RemovedDFDSuccessor(dfd));
				this.commonNodeRemovalModule(dfd);
			}
			this.commonNodeRemovalModule(node);
		} else if (node instanceof OrderedConstruct) {
			if (InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.ORDERED_START)) {
				DummyFlushDirective dfd = InsertDummyFlushDirectives.getPredDFD(node, DummyFlushType.ORDERED_START);
				sideEffectList.add(new RemovedDFDPredecessor(dfd, node));
				this.commonNodeRemovalModule(dfd);
			}
			if (InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.ORDERED_END)) {
				DummyFlushDirective dfd = InsertDummyFlushDirectives.getSuccDFD(node, DummyFlushType.ORDERED_END);
				sideEffectList.add(new RemovedDFDSuccessor(dfd));
				this.commonNodeRemovalModule(dfd);
			}
			this.commonNodeRemovalModule(node);
		} else if (node instanceof CallStatement) {
			CallStatement callStmt = (CallStatement) node;
			if (callStmt.getInfo().isALockModifyRoutine()) {
				if (InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.LOCK_MODIFY_START)) {
					DummyFlushDirective dfd = InsertDummyFlushDirectives.getPredDFD(node,
							DummyFlushType.LOCK_MODIFY_START);
					sideEffectList.add(new RemovedDFDPredecessor(dfd, node));
					this.commonNodeRemovalModule(dfd);
				}
				if (InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.LOCK_MODIFY_END)) {
					DummyFlushDirective dfd = InsertDummyFlushDirectives.getSuccDFD(node,
							DummyFlushType.LOCK_MODIFY_END);
					sideEffectList.add(new RemovedDFDSuccessor(dfd));
					this.commonNodeRemovalModule(dfd);
				}
				this.commonNodeRemovalModule(node);
			} else if (callStmt.getInfo().isALockWriteRoutine()) {
				if (InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.LOCK_WRITE_END)) {
					DummyFlushDirective dfd = InsertDummyFlushDirectives.getSuccDFD(node,
							DummyFlushType.LOCK_WRITE_END);
					sideEffectList.add(new RemovedDFDSuccessor(dfd));
					this.commonNodeRemovalModule(dfd);
				}
				this.commonNodeRemovalModule(node);
			}
		} else if (node instanceof TaskConstruct) {
			if (InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.TASK_START)) {
				DummyFlushDirective dfd = InsertDummyFlushDirectives.getPredDFD(node, DummyFlushType.TASK_START);
				sideEffectList.add(new RemovedDFDPredecessor(dfd, node));
				this.commonNodeRemovalModule(dfd);
			}
			if (InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.TASK_END)) {
				DummyFlushDirective dfd = InsertDummyFlushDirectives.getSuccDFD(node, DummyFlushType.TASK_END);
				sideEffectList.add(new RemovedDFDSuccessor(dfd));
				this.commonNodeRemovalModule(dfd);
			}
			this.commonNodeRemovalModule(node);
		} else if (node instanceof TaskyieldDirective) {
			if (InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.TASKYIELD_START)) {
				DummyFlushDirective dfd = InsertDummyFlushDirectives.getPredDFD(node, DummyFlushType.TASKYIELD_START);
				sideEffectList.add(new RemovedDFDPredecessor(dfd, node));
				this.commonNodeRemovalModule(dfd);
			}
			this.commonNodeRemovalModule(node);
		} else if (node instanceof TaskwaitDirective) {
			if (InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.TASKWAIT_START)) {
				DummyFlushDirective dfd = InsertDummyFlushDirectives.getPredDFD(node, DummyFlushType.TASKWAIT_START);
				sideEffectList.add(new RemovedDFDPredecessor(dfd, node));
				this.commonNodeRemovalModule(dfd);
			}
			this.commonNodeRemovalModule(node);
		} else {
			assert (false);
		}
		return sideEffectList;
	}

	public List<SideEffect> addAtLast(Node element) {
		int index = this.getElementList().size();
		return this.addElement(index, element);
	}

	public List<SideEffect> addElement(Node element) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		Node cfgNode = Misc.getCFGNodeFor(element);
		if (cfgNode instanceof Declaration) {
			return this.addDeclaration((Declaration) cfgNode);
		} else if (cfgNode instanceof Statement) {
			return this.addStatement((Statement) cfgNode);
		} else {
			assert (false);
			return sideEffectList;
		}
	}

	public List<SideEffect> addElement(int index, Node element) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		Node cfgNode = Misc.getCFGNodeFor(element);
		if (cfgNode instanceof Declaration) {
			return this.addDeclaration(index, (Declaration) cfgNode);
		} else if (cfgNode instanceof Statement) {
			return this.addStatement(index, (Statement) cfgNode);
		} else {
			assert (false);
			return sideEffectList;
		}
	}

	public List<SideEffect> addStatement(Statement element) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		Node cfgNode = Misc.getCFGNodeFor(element);
		if (cfgNode instanceof DummyFlushDirective) {
			// Not allowed!
			sideEffectList.add(new UnauthorizedDFDUpdate(cfgNode));
			return sideEffectList;
		} else {
			int size = this.getElementList().size();
			return this.addStatement(size, element);
		}
	}

	public List<SideEffect> addStatement(int index, Statement stmt) {
		AutomatedUpdater.flushCaches();
		List<SideEffect> sideEffectList = new ArrayList<>();
		stmt = Misc.getStatementWrapper(stmt);

		List<SideEffect> splitSE = SplitCombinedConstructs.splitCombinedConstructForTheStatement(stmt);
		if (!splitSE.isEmpty()) {
			NodeUpdated nodeUpdatedSE = (NodeUpdated) splitSE.get(0);
			// Note: Here we reparse the parallel construct so that we can perform other
			// normalizations within it.
			ParallelConstruct splitParCons = FrontEnd.parseAndNormalize(nodeUpdatedSE.affectedNode.toString(),
					ParallelConstruct.class);
			sideEffectList.add(new NodeUpdated(splitParCons, nodeUpdatedSE.getUpdateMessage()));
			sideEffectList.addAll(this.addStatement(index, splitParCons));
			return sideEffectList;
		}

		NodeRemover.removeNodeIfConnected(stmt);
		Statement newStmt = ImplicitBarrierRemover.makeBarrierExplicitForNode(stmt, sideEffectList);
		if (newStmt != stmt) {
			// OLD CODE: sideEffectList.addAll(this.addElement(index, newStmt));
			/*
			 * Update code: Now, we do not add any extra encapsulation of braces
			 * here.
			 */
			CompoundStatement compNewStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
			for (Node elem : compNewStmt.getInfo().getCFGInfo().getElementList()) {
				if (elem instanceof DummyFlushDirective) {
					continue;
				}
				sideEffectList.addAll(this.addElement(index, elem));
				index++;
			}
			return sideEffectList;
		}
		Node cfgNode = Misc.getCFGNodeFor(stmt);
		if (cfgNode instanceof DummyFlushDirective) {
			// Not allowed!
			sideEffectList.add(new UnauthorizedDFDUpdate(cfgNode));
			return sideEffectList;
		}
		index = this.shiftedIndex(index, sideEffectList);
		if (InsertDummyFlushDirectives.requiresNewDFDBeforeAddition(cfgNode)) {
			sideEffectList.addAll(this.insertNewDFDsWithNode(index, cfgNode));
			return sideEffectList;
		} else {
			this.commonNodeAdditionModule(index, stmt, sideEffectList);
			return sideEffectList;
		}
	}

	public List<SideEffect> addDeclaration(Declaration declaration) {
		return this.addDeclaration(0, declaration);
	}

	public List<SideEffect> addDeclaration(int index, Declaration declaration) {
		AutomatedUpdater.flushCaches();
		List<SideEffect> sideEffectList = new ArrayList<>();
		index = this.shiftedIndex(index, sideEffectList);
		/*
		 * Verify that this addition doesn't lead to collision of names.
		 */
		List<String> declaredIds = declaration.getInfo().getIDNameList();

		CellSet existingCells = this.getOwner().getInfo().getAllCellsAtNode();

		Set<String> accessedNames = new HashSet<>();
		// if (accessedCells.isUniversal()) {
		// System.out.println("No update due to name collision (inefficient points-to):
		// " + declaredIds + ", " + accessedNames);
		// Thread.dumpStack();
		// System.exit(0);
		// sideEffectList.add(UpdateSideEffects.NO_UPDATE_DUE_TO_NAME_COLLISION);
		// return sideEffectList;
		// } else {
		existingCells.applyAllExpanded(cell -> {
			if (cell instanceof Symbol) {
				accessedNames.add(((Symbol) cell).getName());
			} else if (cell instanceof AddressCell) {
				Cell pointee = ((AddressCell) cell).getPointedElement();
				if (pointee instanceof Symbol) {
					accessedNames.add(((Symbol) pointee).getName());
				}
			} else if (cell instanceof FieldCell) {
				accessedNames.add(((FieldCell) cell).getAggregateElement().getName());
			} else if (cell instanceof FreeVariable) {
				accessedNames.add(((FreeVariable) cell).getFreeVariableName());
			}
		});
		// }

		if (Misc.doIntersect(declaredIds, accessedNames)) {
			sideEffectList.add(new NamespaceCollisionOnAddition(declaration));
			// Old Code:
			// System.out.println("No update due to name collision: " + declaredIds + ", " +
			// accessedNames);
			// Thread.dumpStack();
			// System.exit(0);
			// sideEffectList.add(UpdateSideEffects.NO_UPDATE_DUE_TO_NAME_COLLISION);
			// return sideEffectList;
		}

		NodeRemover.removeNodeIfConnected(declaration);
		CompoundStatement node = (CompoundStatement) this.getOwner();
		CompoundStatementElement compStmtElementTarget = Misc.getCompoundStatementElementWrapper(declaration);
		NodeListOptional list = node.getF1();
		compStmtElementTarget.setParent(node.getF1()); // Not needed; harmless, though.
		list.addNode(index, compStmtElementTarget);
		AutomatedUpdater.invalidateEnclosedSymbolSets(this.getOwner());
		AutomatedUpdater.invalidateSymbolsInNode(compStmtElementTarget);
		node.getInfo().addDeclarationToSymbolOrTypeTable(declaration);
		this.updateCFGForElementAddition(declaration); // Ignore the side effects returned by this invocation.

		Declaration newDeclaration = Normalization.normalizeLeafNodes(declaration, sideEffectList);
		int newIndex = list.getNodes().indexOf(Misc.getCompoundStatementElementWrapper(newDeclaration));
		if (newIndex != -1) {
			for (int i = index; i < newIndex; i++) {
				sideEffectList.add(new InitializationSimplified(newDeclaration, newDeclaration));
			}
		}
		declaration = newDeclaration;

		Program.invalidColumnNum = Program.invalidLineNum = true;
		Symbol sym = declaration.getInfo().getDeclaredSymbol();
		Cell.addCell(sym);
		AutomatedUpdater.updateInformationForAddition(declaration);
		// AutomatedUpdater.invalidateSymbolsInNode(compStmtElementTarget); // Added, so
		// that any changes from points-to may be reflected here.
		// Old code: These lines from above were here previously.
		// Symbol sym = declaration.getInfo().getDeclaredSymbol();
		// Cell.addCell(sym);
		return sideEffectList;
	}

	/**
	 * Shifts the increment forward or backward if a node cannot be inserted at
	 * the current location due to DFD constraints.
	 * 
	 * @param index
	 *              index at which we wish to insert a node.
	 * @return
	 *         index at which a new node can be inserted, if intention was to
	 *         insert at {@code index}.
	 */
	private int shiftedIndex(int index, List<SideEffect> sideEffects) {
		if (index == this.getElementList().size() || index == 0) {
			return index;
		}
		Node node = this.getElementList().get(index);
		Node prevNode = this.getElementList().get(index - 1);
		if ((node instanceof FlushDirective && !(node instanceof DummyFlushDirective))
				|| node instanceof BarrierDirective || node instanceof AtomicConstruct
				|| node instanceof CriticalConstruct || node instanceof OrderedConstruct
				|| node instanceof TaskConstruct || node instanceof TaskyieldDirective
				|| node instanceof TaskwaitDirective) {
			if (prevNode instanceof DummyFlushDirective) {
				index--;
			}
		} else if (node instanceof CallStatement) {
			CallStatement callStmt = (CallStatement) node;
			if (callStmt.getInfo().isALockModifyRoutine()) {
				if (prevNode instanceof DummyFlushDirective) {
					index--;
				}
			}
		} else if (node instanceof DummyFlushDirective) {
			DummyFlushDirective dfd = (DummyFlushDirective) node;
			DummyFlushType dfType = dfd.getDummyFlushType();
			switch (dfType) {
			case ATOMIC_END:
			case CRITICAL_END:
			case ORDERED_END:
			case LOCK_MODIFY_END:
			case LOCK_WRITE_END:
			case TASK_END:
				index++;
			default:
				// Do nothing.
			}
		}
		return index;
	}

	/**
	 * Inserts an element, without caring for any normalizations.
	 * 
	 * @param index
	 *                location at which the node has to be added.
	 * @param element
	 *                a CFG Node that needs to be added.
	 */
	private void commonNodeAdditionModule(int index, Node element, List<SideEffect> sideEffectList) {
		CompoundStatement node = (CompoundStatement) this.getOwner();
		// OLD CODE: This check has already been performed in addStatement().
		// if (element instanceof Statement) {
		// Statement stmtWrapper = Misc.getStatementWrapper((Statement) element);
		// Statement newElement =
		// ImplicitBarrierRemover.makeBarrierExplicitForNode(stmtWrapper,
		// sideEffectList);
		// if (newElement != stmtWrapper) {
		// sideEffectList.addAll(this.addElement(index, newElement));
		// return;
		// }
		// }

		CompoundStatementElement compStmtElementTarget = Misc.getCompoundStatementElementWrapper(element);
		NodeListOptional list = node.getF1();
		compStmtElementTarget.setParent(node.getF1());
		list.addNode(index, compStmtElementTarget);
		AutomatedUpdater.invalidateSymbolsInNode(compStmtElementTarget);
		this.updateCFGForElementAddition(element);

		Node newElement = Normalization.normalizeLeafNodes(element, sideEffectList);
		int newIndex = list.getNodes().indexOf(Misc.getCompoundStatementElementWrapper(newElement));
		if (newIndex != -1) {
			for (int i = index; i < newIndex; i++) {
				sideEffectList.add(new InitializationSimplified(newElement, newElement));
			}
		}
		element = newElement;

		Program.invalidColumnNum = Program.invalidLineNum = true;
		AutomatedUpdater.updateInformationForAddition(element);
		// AutomatedUpdater.invalidateSymbolsInNode(compStmtElementTarget);// Added, so
		// that any changes from points-to may be reflected here.
	}

	/**
	 * Used to insert the {@code node}, and its corresponding methods, at
	 * specified location.
	 * 
	 * @param node
	 * @return
	 */
	private List<SideEffect> insertNewDFDsWithNode(int index, Node node) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		if (node instanceof FlushDirective && !(node instanceof DummyFlushDirective)) {
			this.commonNodeAdditionModule(index, node, sideEffectList);
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.FLUSH_START)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.FLUSH_START);
				sideEffectList.add(new AddedDFDPredecessor(dfd, node));
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
			}
		} else if (node instanceof BarrierDirective) {
			this.commonNodeAdditionModule(index, node, sideEffectList);
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.BARRIER_START)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.BARRIER_START);
				sideEffectList.add(new AddedDFDPredecessor(dfd, node));
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
			}
		} else if (node instanceof AtomicConstruct) {
			int last = index + 1;
			this.commonNodeAdditionModule(index, node, sideEffectList);
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.ATOMIC_START)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.ATOMIC_START);
				sideEffectList.add(new AddedDFDPredecessor(dfd, node));
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
				last++;
			}
			if (!InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.ATOMIC_END)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.ATOMIC_END);
				sideEffectList.add(new AddedDFDSuccessor(dfd));
				this.commonNodeAdditionModule(last, dfd, sideEffectList);
			}
		} else if (node instanceof CriticalConstruct) {
			int last = index + 1;
			this.commonNodeAdditionModule(index, node, sideEffectList);
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.CRITICAL_START)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.CRITICAL_START);
				sideEffectList.add(new AddedDFDPredecessor(dfd, node));
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
				last++;
			}
			if (!InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.CRITICAL_END)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.CRITICAL_END);
				sideEffectList.add(new AddedDFDSuccessor(dfd));
				this.commonNodeAdditionModule(last, dfd, sideEffectList);
			}
		} else if (node instanceof OrderedConstruct) {
			int last = index + 1;
			this.commonNodeAdditionModule(index, node, sideEffectList);
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.ORDERED_START)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.ORDERED_START);
				sideEffectList.add(new AddedDFDPredecessor(dfd, node));
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
				last++;
			}
			if (!InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.ORDERED_END)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.ORDERED_END);
				sideEffectList.add(new AddedDFDSuccessor(dfd));
				this.commonNodeAdditionModule(last, dfd, sideEffectList);
			}
		} else if (node instanceof CallStatement) {
			CallStatement callStmt = (CallStatement) node;
			if (callStmt.getInfo().isALockModifyRoutine()) {
				int last = index + 1;
				this.commonNodeAdditionModule(index, node, sideEffectList);
				if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.LOCK_MODIFY_START)) {
					DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.LOCK_MODIFY_START);
					sideEffectList.add(new AddedDFDPredecessor(dfd, node));
					this.commonNodeAdditionModule(index, dfd, sideEffectList);
					last++;
				}
				if (!InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.LOCK_MODIFY_END)) {
					DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.LOCK_MODIFY_END);
					sideEffectList.add(new AddedDFDSuccessor(dfd));
					this.commonNodeAdditionModule(last, dfd, sideEffectList);
				}
			} else if (callStmt.getInfo().isALockWriteRoutine()) {
				this.commonNodeAdditionModule(index, node, sideEffectList);
				if (!InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.LOCK_WRITE_END)) {
					DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.LOCK_WRITE_END);
					sideEffectList.add(new AddedDFDSuccessor(dfd));
					this.commonNodeAdditionModule(index + 1, dfd, sideEffectList);
				}
			}
		} else if (node instanceof TaskConstruct) {
			int last = index + 1;
			this.commonNodeAdditionModule(index, node, sideEffectList);
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.TASK_START)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.TASK_START);
				sideEffectList.add(new AddedDFDPredecessor(dfd, node));
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
				last++;
			}
			if (!InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.TASK_END)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.TASK_END);
				sideEffectList.add(new AddedDFDSuccessor(dfd));
				this.commonNodeAdditionModule(last, dfd, sideEffectList);
			}
		} else if (node instanceof TaskyieldDirective) {
			this.commonNodeAdditionModule(index, node, sideEffectList);
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.TASKYIELD_START)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.TASKYIELD_START);
				sideEffectList.add(new AddedDFDPredecessor(dfd, node));
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
			}
		} else if (node instanceof TaskwaitDirective) {
			this.commonNodeAdditionModule(index, node, sideEffectList);
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.TASKWAIT_START)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.TASKWAIT_START);
				sideEffectList.add(new AddedDFDPredecessor(dfd, node));
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
			}
		} else {
			assert (false);
		}
		return sideEffectList;
	}

	private void updateCFGForElementRemoval(Node removed) {
		removed = Misc.getInternalFirstCFGNode(removed);
		// 1. Adjust incompleteness.
		removed.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerRemoval();

		// 2. Remove stale edges.
		// OLD CODE: Readable version follows.
		// NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// List<Node> stmtList = this.getElementList();
		// int index = stmtList.indexOf(removed);
		// assert (index != -1);
		// Node prevNode;
		// Node nextNode;
		// if (index == 0) {
		// prevNode = ncfg.getBegin();
		// if (stmtList.size() > 1) {
		// nextNode = stmtList.get(1);
		// } else {
		// nextNode = ncfg.getEnd();
		// }
		// } else if (index < stmtList.size() - 1) {
		// prevNode = stmtList.get(index - 1);
		// nextNode = stmtList.get(index + 1);
		// } else if (index == stmtList.size() - 1) {
		// prevNode = stmtList.get(index - 1);
		// nextNode = ncfg.getEnd();
		// } else {
		// prevNode = nextNode = null;
		// assert (false);
		// }
		// if (prevNode.getInfo().getCFGInfo().isEndReachable()) {
		// connectAndAdjustEndReachability(prevNode, nextNode);
		// }
		for (Node prevCFGNode : removed.getInfo().getCFGInfo().getPredBlocks()) {
			if (prevCFGNode instanceof GotoStatement) {
				continue;
			}
			for (Node succCFGNode : removed.getInfo().getCFGInfo().getSuccBlocks()) {
				connectAndAdjustEndReachability(prevCFGNode, succCFGNode);
			}
		}
		removed.getInfo().getCFGInfo().clearAllEdges();
		// disconnectAndAdjustEndReachability(prevNode, removed);
		// disconnectAndAdjustEndReachability(removed, nextNode);
	}

	private void updateCFGForElementAddition(Node added) {
		added = Misc.getInternalFirstCFGNode(added);
		NestedCFG ncfg = getOwner().getInfo().getCFGInfo().getNestedCFG();
		// 1. Add new edges.
		List<Node> stmtList = this.getElementList();
		int index = stmtList.indexOf(added);
		if (index == -1) {
			System.out.println("Couldn't find " + added.toString());
			Thread.dumpStack();
			System.exit(1);
		}
		Node prevNode;
		Node nextNode;
		if (index == 0) {
			prevNode = ncfg.getBegin();
			if (stmtList.size() > 1) {
				nextNode = stmtList.get(1);
			} else {
				nextNode = ncfg.getEnd();
			}
		} else if (index < stmtList.size() - 1) {
			prevNode = stmtList.get(index - 1);
			nextNode = stmtList.get(index + 1);
		} else if (index == stmtList.size() - 1) {
			prevNode = stmtList.get(index - 1);
			nextNode = ncfg.getEnd();
		} else {
			prevNode = nextNode = null;
			assert (false);
		}
		if (prevNode.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(prevNode, added);
		}
		if (added.getInfo().getCFGInfo().isEndReachable()) {
			connectAndAdjustEndReachability(added, nextNode);
		}
		if (prevNode instanceof GotoStatement) {
			GotoStatement gotoStmt = (GotoStatement) prevNode;
			if (gotoStmt.getInfo().getCFGInfo().getTarget() == nextNode) {
				return;
			}
		}
		disconnectAndAdjustEndReachability(prevNode, nextNode);

		// 2. Adjust incompleteness
		added.getInfo().getIncompleteSemantics().adjustSemanticsForOwnerAddition();
	}

	/**
	 * Used to insert the missing dummy-flushes around {@code node}, where
	 * {@code node} is an element of this CompoundStatement.
	 * <br>
	 * Note that this method is normally called only on newly parsed (enclosed)
	 * nodes,
	 * (given that all other translations are performed by update-safe methods).
	 * 
	 * @param node
	 *             {@code node} for which missing dummy-flushes have to be added.
	 * @return
	 */
	private void insertNewDFDsWithoutNode(Node node) {
		node = Misc.getCFGNodeFor(node);
		int index = this.getElementList().indexOf(node);
		assert (index != -1);
		List<SideEffect> sideEffectList = new ArrayList<>();
		if (node instanceof FlushDirective && !(node instanceof DummyFlushDirective)) {
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.FLUSH_START)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.FLUSH_START);
				// OLD CODE: Apparently, we had forgotten that pragmas cannot have labels!
				// StatementInfo stmtInfo = (StatementInfo) node.getInfo();
				// List<Label> labelAnnotations = null;
				// if (stmtInfo.hasLabelAnnotations()) {
				// labelAnnotations = new ArrayList<>(stmtInfo.getLabelAnnotations());
				// stmtInfo.clearLabelAnnotations();
				// }
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
				// if (labelAnnotations != null) {
				// for (Label label : labelAnnotations) {
				// dfd.getInfo().addLabelAnnotation(label);
				// }
				// }
			}
		} else if (node instanceof BarrierDirective) {
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.BARRIER_START)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.BARRIER_START);
				// StatementInfo stmtInfo = (StatementInfo) node.getInfo();
				// List<Label> labelAnnotations = null;
				// if (stmtInfo.hasLabelAnnotations()) {
				// labelAnnotations = new ArrayList<>(stmtInfo.getLabelAnnotations());
				// stmtInfo.clearLabelAnnotations();
				// }
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
				// if (labelAnnotations != null) {
				// for (Label label : labelAnnotations) {
				// dfd.getInfo().addLabelAnnotation(label);
				// }
				// }
			}
		} else if (node instanceof AtomicConstruct) {
			boolean addedStart = false;
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.ATOMIC_START)) {
				addedStart = true;
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.ATOMIC_START);
				// StatementInfo stmtInfo = (StatementInfo) node.getInfo();
				// List<Label> labelAnnotations = null;
				// if (stmtInfo.hasLabelAnnotations()) {
				// labelAnnotations = new ArrayList<>(stmtInfo.getLabelAnnotations());
				// stmtInfo.clearLabelAnnotations();
				// }
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
				// if (labelAnnotations != null) {
				// for (Label label : labelAnnotations) {
				// dfd.getInfo().addLabelAnnotation(label);
				// }
				// }
			}
			if (!InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.ATOMIC_END)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.ATOMIC_END);
				if (addedStart) {
					this.commonNodeAdditionModule(index + 2, dfd, sideEffectList);
				} else {
					this.commonNodeAdditionModule(index + 1, dfd, sideEffectList);
				}

			}
		} else if (node instanceof CriticalConstruct) {
			boolean addedStart = false;
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.CRITICAL_START)) {
				addedStart = true;
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.CRITICAL_START);
				// StatementInfo stmtInfo = (StatementInfo) node.getInfo();
				// List<Label> labelAnnotations = null;
				// if (stmtInfo.hasLabelAnnotations()) {
				// labelAnnotations = new ArrayList<>(stmtInfo.getLabelAnnotations());
				// stmtInfo.clearLabelAnnotations();
				// }
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
				// if (labelAnnotations != null) {
				// for (Label label : labelAnnotations) {
				// dfd.getInfo().addLabelAnnotation(label);
				// }
				// }
			}
			if (!InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.CRITICAL_END)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.CRITICAL_END);
				if (addedStart) {
					this.commonNodeAdditionModule(index + 2, dfd, sideEffectList);
				} else {
					this.commonNodeAdditionModule(index + 1, dfd, sideEffectList);
				}
			}
		} else if (node instanceof OrderedConstruct) {
			boolean addedStart = false;
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.ORDERED_START)) {
				addedStart = true;
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.ORDERED_START);
				// StatementInfo stmtInfo = (StatementInfo) node.getInfo();
				// List<Label> labelAnnotations = null;
				// if (stmtInfo.hasLabelAnnotations()) {
				// labelAnnotations = new ArrayList<>(stmtInfo.getLabelAnnotations());
				// stmtInfo.clearLabelAnnotations();
				// }
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
				// if (labelAnnotations != null) {
				// for (Label label : labelAnnotations) {
				// dfd.getInfo().addLabelAnnotation(label);
				// }
				// }
			}
			if (!InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.ORDERED_END)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.ORDERED_END);
				if (addedStart) {
					this.commonNodeAdditionModule(index + 2, dfd, sideEffectList);
				} else {
					this.commonNodeAdditionModule(index + 1, dfd, sideEffectList);
				}
			}
		} else if (node instanceof CallStatement) {
			CallStatement callStmt = (CallStatement) node;
			if (callStmt.getInfo().isALockModifyRoutine()) {
				boolean addedStart = false;
				if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.LOCK_MODIFY_START)) {
					addedStart = true;
					DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.LOCK_MODIFY_START);
					// StatementInfo stmtInfo = (StatementInfo) node.getInfo();
					// List<Label> labelAnnotations = null;
					// if (stmtInfo.hasLabelAnnotations()) {
					// labelAnnotations = new ArrayList<>(stmtInfo.getLabelAnnotations());
					// stmtInfo.clearLabelAnnotations();
					// }
					this.commonNodeAdditionModule(index, dfd, sideEffectList);
					// if (labelAnnotations != null) {
					// for (Label label : labelAnnotations) {
					// dfd.getInfo().addLabelAnnotation(label);
					// }
					// }
				}
				if (!InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.LOCK_MODIFY_END)) {
					DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.LOCK_MODIFY_END);
					if (addedStart) {
						this.commonNodeAdditionModule(index + 2, dfd, sideEffectList);
					} else {
						this.commonNodeAdditionModule(index + 1, dfd, sideEffectList);
					}
				}
			} else if (callStmt.getInfo().isALockWriteRoutine()) {
				if (!InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.LOCK_WRITE_END)) {
					DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.LOCK_WRITE_END);
					this.commonNodeAdditionModule(index + 1, dfd, sideEffectList);
				}
			}
		} else if (node instanceof TaskConstruct) {
			boolean addedStart = false;
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.TASK_START)) {
				addedStart = true;
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.TASK_START);
				// StatementInfo stmtInfo = (StatementInfo) node.getInfo();
				// List<Label> labelAnnotations = null;
				// if (stmtInfo.hasLabelAnnotations()) {
				// labelAnnotations = new ArrayList<>(stmtInfo.getLabelAnnotations());
				// stmtInfo.clearLabelAnnotations();
				// }
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
				// if (labelAnnotations != null) {
				// for (Label label : labelAnnotations) {
				// dfd.getInfo().addLabelAnnotation(label);
				// }
				// }
			}
			if (!InsertDummyFlushDirectives.hasSuccDFD(node, DummyFlushType.TASK_END)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.TASK_END);
				if (addedStart) {
					this.commonNodeAdditionModule(index + 2, dfd, sideEffectList);
				} else {
					this.commonNodeAdditionModule(index + 1, dfd, sideEffectList);
				}
			}
		} else if (node instanceof TaskyieldDirective) {
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.TASKYIELD_START)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.TASKYIELD_START);
				// StatementInfo stmtInfo = (StatementInfo) node.getInfo();
				// List<Label> labelAnnotations = null;
				// if (stmtInfo.hasLabelAnnotations()) {
				// labelAnnotations = new ArrayList<>(stmtInfo.getLabelAnnotations());
				// stmtInfo.clearLabelAnnotations();
				// }
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
				// if (labelAnnotations != null) {
				// for (Label label : labelAnnotations) {
				// dfd.getInfo().addLabelAnnotation(label);
				// }
				// }
			}
		} else if (node instanceof TaskwaitDirective) {
			if (!InsertDummyFlushDirectives.hasPredDFD(node, DummyFlushType.TASKWAIT_START)) {
				DummyFlushDirective dfd = new DummyFlushDirective(DummyFlushType.TASKWAIT_START);
				this.commonNodeAdditionModule(index, dfd, sideEffectList);
			}
		}
	}

	/**
	 * Takes each element of this CompoundStatement, one-by-one, and ensures
	 * that
	 * missing dummy-flushes are inserted for all.
	 */
	public void initializeDummyFlushes() {
		for (Node element : this.getElementList()) {
			this.insertNewDFDsWithoutNode(element);
		}
	}

	/**
	 * Obtain the various CFG components of the {@code owner} node.
	 * 
	 * @return
	 *         CFG components of the {@code owner} node.
	 */
	@Override
	public List<Node> getAllComponents() {
		List<Node> retList = new ArrayList<>();
		retList.add(this.getNestedCFG().getBegin());
		retList.addAll(this.getElementList());
		retList.add(this.getNestedCFG().getEnd());
		return retList;
	}
}
