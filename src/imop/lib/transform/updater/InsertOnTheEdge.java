/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.updater;

import imop.ast.annotation.IncompleteEdge;
import imop.ast.annotation.IncompleteEdge.TypeOfIncompleteness;
import imop.ast.info.cfgNodeInfo.DoStatementInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.info.DoStatementCFGInfo;
import imop.lib.cfg.link.baseVisitor.GJNoArguCFGLinkVisitor;
import imop.lib.cfg.link.node.*;
import imop.lib.transform.updater.sideeffect.*;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Inserts a {@code targetNode} on the edge between {@code sourceNode} and
 * {@code destinationNode}.
 * 
 * @author aman
 *
 */
public class InsertOnTheEdge {

	public static enum PivotPoint {
		SOURCE, DESTINATION
	}

	public static List<SideEffect> insert(Node sourceNode, Node destinationNode, Node targetNode) {
		return InsertOnTheEdge.insertAggressive(sourceNode, destinationNode, targetNode, PivotPoint.SOURCE);
	}

	@SuppressWarnings("unlikely-arg-type")
	private static List<SideEffect> tryImmediates(Node sourceNode, Node destinationNode, Node targetNode,
			PivotPoint pivot) {
		List<Node> sourceSuccessors = sourceNode.getInfo().getCFGInfo().getSuccessors();
		List<Node> destinationPredecessors = destinationNode.getInfo().getCFGInfo().getPredecessors();
		List<SideEffect> sideEffectList = new ArrayList<>();
		Node requestedPivot = null;
		Node chosenPivot = null;
		if (sourceSuccessors.size() == 1) {
			if (pivot == PivotPoint.DESTINATION) {
				requestedPivot = destinationNode;
				chosenPivot = sourceNode;
			} else {
				/*
				 * Perform insertion since the pivot point has not changed.
				 */
				return InsertImmediateSuccessor.insert(sourceNode, targetNode);
			}
		} else if (destinationPredecessors.size() == 1) {
			if (pivot == PivotPoint.SOURCE) {
				requestedPivot = sourceNode;
				chosenPivot = destinationNode;
			} else {
				/*
				 * Perform insertion since the pivot point has not changed.
				 */
				return InsertImmediatePredecessor.insert(destinationNode, targetNode);
			}
		} else {
			/*
			 * Return, since neither source node has a single outgoing edge, nor
			 * destination node has a single incoming edge.
			 */
			return null;
		}

		Scopeable scopeRequested = Misc.getEnclosingBlock(requestedPivot);
		if (scopeRequested == null) {
			return null;
		}
		Scopeable scopeChosen = Misc.getEnclosingBlock(chosenPivot);
		if (scopeChosen == null) {
			return null;
		}

		/*
		 * Test name collision possibility.
		 */
		if (scopeChosen == scopeRequested) {
			// No issues.
		} else {
			Scopeable lca = Misc.getLCAScope(chosenPivot, requestedPivot);
			Scopeable outerMost = null;
			if (lca == null) {
				outerMost = chosenPivot.getInfo().getOuterMostScopeExclusive();
			} else {
				if (lca == scopeChosen) {
					List<Node> nodeChain = requestedPivot.getInfo().getNonLeafNestingPathExclusive();
					assert (nodeChain.contains(scopeChosen));
					int indexOfS = nodeChain.indexOf(scopeChosen);
					for (int i = indexOfS - 1; i >= 0; i--) {
						if (nodeChain.get(i) instanceof Scopeable) {
							outerMost = (Scopeable) nodeChain.get(i);
							break;
						}
					}
				} else if (lca == scopeRequested) {
					List<Node> nodeChain = chosenPivot.getInfo().getNonLeafNestingPathExclusive();
					assert (nodeChain.contains(scopeRequested));
					int indexOfD = nodeChain.indexOf(scopeRequested);
					for (int i = indexOfD - 1; i >= 0; i--) {
						if (nodeChain.get(i) instanceof Scopeable) {
							outerMost = (Scopeable) nodeChain.get(i);
							break;
						}
					}
				} else {
					List<Node> nodeChain = chosenPivot.getInfo().getNonLeafNestingPathExclusive();
					assert (nodeChain.contains(lca));
					int indexOfLCA = nodeChain.indexOf(lca);
					for (int i = indexOfLCA - 1; i >= 0; i--) {
						if (nodeChain.get(i) instanceof Scopeable) {
							outerMost = (Scopeable) nodeChain.get(i);
							break;
						}
					}
				}
			}
			if (outerMost != null) {
				if (Misc.collidesFreelyWithinScopesOf(targetNode, outerMost, chosenPivot)) {
					sideEffectList.add(new NoUpdateDueToNameCollision(targetNode));
					return sideEffectList;
				}
			}
		}

		/*
		 * Test possible issues with incomplete edges.
		 */
		Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);
		/*
		 * Handle break: ensure that enclosing targets are same for source
		 * and destination.
		 */
		if (edges.stream()
				.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION))) {
			Node srcTarget = Misc.getEnclosingLoopOrSwitch(sourceNode);
			Node destTarget = Misc.getEnclosingLoopOrSwitch(destinationNode);
			if (srcTarget != destTarget) {
				sideEffectList.add(new JumpEdgeConstraint(targetNode));
				return sideEffectList;
			}
		}

		/*
		 * Handle continue: ensure that enclosing targets are same for
		 * source and destination.
		 */
		if (edges.stream()
				.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
			Node srcTarget = Misc.getEnclosingLoopOrForConstruct(sourceNode);
			Node destTarget = Misc.getEnclosingLoopOrForConstruct(destinationNode);
			if (srcTarget != destTarget) {
				sideEffectList.add(new JumpEdgeConstraint(sourceNode));
				return sideEffectList;
			}
		}
		/*
		 * Handle case and default: ensure that enclosing targets are same
		 * for source and destination.
		 */
		if (edges.stream().anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CASE_SOURCE
				|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_DEFAULT_SOURCE))) {
			Node srcTarget = Misc.getEnclosingSwitch(sourceNode);
			Node destTarget = Misc.getEnclosingSwitch(destinationNode);
			if (srcTarget != destTarget) {
				sideEffectList.add(new JumpEdgeConstraint(sourceNode));
				return sideEffectList;
			}
		}

		/*
		 * If we haven't returned so far, then the insertion is safe to perform.
		 */
		if (sourceSuccessors.size() == 1) {
			return InsertImmediateSuccessor.insert(sourceNode, targetNode);
		} else if (destinationPredecessors.size() == 1) {
			return InsertImmediatePredecessor.insert(destinationNode, targetNode);
		} else {
			return null;
		}

	}

	public static List<SideEffect> insertAggressive(Node sourceNode, Node destinationNode, Node targetNode,
			PivotPoint pivot) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		sourceNode = Misc.getCFGNodeFor(sourceNode);
		destinationNode = Misc.getCFGNodeFor(destinationNode);
		targetNode = Misc.getCFGNodeFor(targetNode);

		if (sourceNode instanceof EndNode) {
			sourceNode = sourceNode.getParent();
		}
		if (destinationNode instanceof BeginNode) {
			destinationNode = destinationNode.getParent();
		}
		List<Node> sourceSuccessors = sourceNode.getInfo().getCFGInfo().getSuccessors();
		List<Node> destinationPredecessors = destinationNode.getInfo().getCFGInfo().getPredecessors();

		assert (sourceSuccessors.contains(destinationNode));
		assert (destinationPredecessors.contains(sourceNode));

		CFGLink sourceLink = CFGLinkFinder.getCFGLinkFor(sourceNode);
		CFGLink destinationLink = CFGLinkFinder.getCFGLinkFor(destinationNode);
		sideEffectList = tryImmediates(sourceNode, destinationNode, targetNode, pivot);
		if (sideEffectList != null && Misc.changePerformed(sideEffectList)) {
			return sideEffectList;
		}

		// Future code. To be completed when we wish to handle executableLeafPred/Succ
		// as well.
		// /*
		// * Note that this method supports insertion using both nested as well as
		// * plain CFG.
		// * We convert insertion requests on plain CFG to nested CFG below.
		// */
		// NodePair nodePair = Misc.getBlockNeighbours(sourceNode, destinationNode,
		// pivotPoint);
		// if (nodePair.one == null || nodePair.two == null) {
		// Misc.warnDueToLackOfFeature("Could not find a proper link between the source
		// and destination nodes.", null);
		// sideEffectList.add(UpdateSideEffects.SYNTACTIC_CONSTRAINT);
		// return sideEffectList;
		// } else {
		// sourceNode = nodePair.one;
		// destinationNode = nodePair.two;
		// }
		if (sideEffectList != null && (sourceSuccessors.size() == 1 || destinationPredecessors.size() == 1)) {
			return sideEffectList;
		}

		if (sourceLink == null || destinationLink == null) {
			Misc.warnDueToLackOfFeature(
					"Cannot insert a node immediately before some other node, due to lack of an appropriate enclosing node.",
					sourceNode);
			if (sideEffectList == null) {
				sideEffectList = new ArrayList<>();
			}
			if (sourceLink == null) {
				sideEffectList.add(new MissingCFGParent(sourceNode));
			} else {
				sideEffectList.add(new MissingCFGParent(destinationNode));
			}
			return sideEffectList;
		}
		sideEffectList = sourceLink.accept(new AggressiveInsertOnTheEdge(destinationLink, targetNode, pivot));
		return sideEffectList;
	}

	private static class AggressiveInsertOnTheEdge extends GJNoArguCFGLinkVisitor<List<SideEffect>> {
		protected CFGLink destinationLink;
		protected Node targetNode;
		protected PivotPoint pivotPoint;

		private AggressiveInsertOnTheEdge(CFGLink destinationNode, Node targetNode, PivotPoint pivotPoint) {
			this.destinationLink = destinationNode;
			this.targetNode = targetNode;
			this.pivotPoint = pivotPoint;
		}

		@Override
		public List<SideEffect> visit(FunctionBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(FunctionParameterLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(FunctionBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(FunctionEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ParallelBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ParallelClauseLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ParallelBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ParallelEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OmpForBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OmpForInitLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OmpForTermLink link) {
			if (destinationLink instanceof OmpForBodyLink) {
				assert (false);
				return null;
			} else if (destinationLink instanceof OmpForEndLink) {
				assert (false);
				return null;
			} else {
				assert (false);
				return null;
			}
		}

		@Override
		public List<SideEffect> visit(OmpForStepLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OmpForBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OmpForEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SectionsBeginLink link) {
			if (destinationLink instanceof SectionsSectionBodyLink) {
				assert (false);
				return null;
			} else {
				assert (false);
				return null;
			}
		}

		@Override
		public List<SideEffect> visit(SectionsSectionBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SectionsEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SingleBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SingleBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SingleEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(TaskBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(TaskClauseLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(TaskBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(TaskEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(MasterBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(MasterBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(MasterEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CriticalBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CriticalBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CriticalEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(AtomicBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(AtomicStatementLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(AtomicEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OrderedBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OrderedBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(OrderedEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CompoundBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CompoundElementLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CompoundEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(IfBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(IfPredicateLink link) {
			if (destinationLink instanceof IfThenBodyLink) {
				assert (false);
				return null;
			} else if (destinationLink instanceof IfElseBodyLink) {
				assert (false);
				return null;
			} else if (destinationLink instanceof IfEndLink) {
				// Note that this link exists since there is no else body.
				List<SideEffect> sideEffectList = new ArrayList<>();
				IfStatement ifStmt = ((IfEndLink) destinationLink).enclosingNonLeafNode;
				assert (!ifStmt.getInfo().getCFGInfo().hasElseBody());

				if (targetNode instanceof CompoundStatement) {
					sideEffectList.addAll(ifStmt.getInfo().getCFGInfo().setElseBody((Statement) targetNode));
					if (!Misc.changePerformed(sideEffectList)) {
						ifStmt.getInfo().getCFGInfo().removeElseBody();
						return sideEffectList;
					}
					return sideEffectList;
				} else {
					assert (targetNode instanceof Statement || targetNode instanceof Declaration);
					CompoundStatement compStmt = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
					sideEffectList.addAll(ifStmt.getInfo().getCFGInfo().setElseBody(compStmt));
					sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
					if (!Misc.changePerformed(sideEffectList)) {
						ifStmt.getInfo().getCFGInfo().removeElseBody();
						return sideEffectList;
					}
				}
				return sideEffectList;
			} else {
				assert (false);
				return null;
			}
		}

		@Override
		public List<SideEffect> visit(IfThenBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(IfElseBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(IfEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SwitchBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SwitchPredicateLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (destinationLink instanceof SwitchEndLink) {
				// This implies that there is no default case in this switch-statement.
				// We will handle this case by adding a default statement.
				CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
				if (body.getInfo().getCFGInfo().isEndReachable()
						&& !body.getInfo().getCFGInfo().getElementList().isEmpty()) {
					BreakStatement breakStmt = FrontEnd.parseAndNormalize("break;", BreakStatement.class);
					sideEffectList.addAll(body.getInfo().getCFGInfo().addAtLast(breakStmt));
				}
				Statement defaultStmt = FrontEnd.parseAndNormalize("default: ;", Statement.class);
				sideEffectList.addAll(body.getInfo().getCFGInfo().addAtLast(defaultStmt));
				sideEffectList.addAll(InsertImmediateSuccessor.insert(defaultStmt, targetNode));
				return sideEffectList;

				// Old Code:
				// Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);
				// if (edges.stream()
				// .anyMatch((e) -> (e.getTypeOfIncompleteness() ==
				// TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
				// || e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CASE_SOURCE
				// || e.getTypeOfIncompleteness() ==
				// TypeOfIncompleteness.UNKNOWN_DEFAULT_SOURCE))) {
				// sideEffectList.add(UpdateSideEffects.JUMPEDGE_CONSTRAINT);
				// sideEffectList.add(UpdateSideEffects.SYNTACTIC_CONSTRAINT);
				// return sideEffectList;
				// }
				// SwitchStatement switchStmt = ((SwitchEndLink)
				// destinationLink).enclosingNonLeafNode;
				// return InsertImmediateSuccessor.insertAggressive(switchStmt, targetNode);
			} else if (destinationLink instanceof CompoundElementLink) {
				CompoundStatement body = (CompoundStatement) link.enclosingNonLeafNode.getInfo().getCFGInfo().getBody();
				if (pivotPoint == PivotPoint.SOURCE) {
					if (Misc.collidesFreelyWith(targetNode, body)) {
						sideEffectList.add(new NoUpdateDueToNameCollision(targetNode));
						return sideEffectList;
					}
				}

				Node destination = ((CompoundElementLink) destinationLink).childNode;
				Statement destStmt = (Statement) destination;
				sideEffectList.addAll(InsertImmediatePredecessor.insert(destStmt, targetNode));
				return sideEffectList;
			} else {
				assert (false);
				return null;
			}
		}

		@Override
		public List<SideEffect> visit(SwitchBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(SwitchEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(WhileBeginLink link) {
			// TODO: Test this!
			return InsertImmediatePredecessor.insert(link.childNode, targetNode);
		}

		@Override
		public List<SideEffect> visit(WhilePredicateLink link) {
			if (destinationLink instanceof WhileBodyLink) {
				assert (false);
				return null;
			} else if (destinationLink instanceof WhileEndLink) {
				List<SideEffect> sideEffectList = new ArrayList<>();
				Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);

				if (edges.stream()
						.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
								|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
					sideEffectList.add(new JumpEdgeConstraint(targetNode));
					return sideEffectList;
				}
				WhileStatement whileStmt = ((WhileEndLink) destinationLink).enclosingNonLeafNode;
				return InsertImmediateSuccessor.insert(whileStmt, targetNode);
			} else {
				assert (false);
				return null;
			}
		}

		@Override
		public List<SideEffect> visit(WhileBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(WhileEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(DoBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(DoPredicateLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			DoStatementInfo doInfo = link.enclosingNonLeafNode.getInfo();
			DoStatementCFGInfo doCFGInfo = doInfo.getCFGInfo();
			Expression oldPred = doCFGInfo.getPredicate();
			if (destinationLink instanceof DoBodyLink) {
				if (doInfo.hasConstantPredicate()) {
					if (doInfo.predicateIsConstantFalse()) {
						String str = "The edge from predicate " + oldPred
								+ " to body of a certain do-while loop does not exist. "
								+ "Cannot perform insertion on that edge.";
						Misc.warnDueToLackOfFeature(str, null);
						sideEffectList.add(new SyntacticConstraint(doInfo.getCFGInfo().getBody(), str));
						return sideEffectList;
					} else {
						return InsertImmediatePredecessor.insert(oldPred, targetNode);
					}
				} else {
					IfStatement breakingStmt = FrontEnd.parseAndNormalize("if (!(" + oldPred + ")) {break;}",
							IfStatement.class);
					if (targetNode instanceof CompoundStatement) {
						breakingStmt.getInfo().getCFGInfo().setElseBody((Statement) targetNode);
					} else {
						CompoundStatement elseBody = FrontEnd.parseAndNormalize("{}", CompoundStatement.class);
						elseBody.getInfo().getCFGInfo().addElement(targetNode);
						breakingStmt.getInfo().getCFGInfo().setElseBody(elseBody);
					}
					sideEffectList.addAll(InsertImmediatePredecessor.insert(doCFGInfo.getPredicate(), breakingStmt));

					Expression newPred = FrontEnd.parseAndNormalize("1", Expression.class);
					doCFGInfo.setPredicate(newPred);
					return sideEffectList;
				}
			} else if (destinationLink instanceof DoEndLink) {
				if (doInfo.hasConstantPredicate()) {
					if (doInfo.predicateIsConstantTrue()) {
						String str = "The edge from predicate " + oldPred
								+ " to EndNode of a certain do-while loop does not exist."
								+ "Cannot perform insertion on that edge.";
						Misc.warnDueToLackOfFeature(str, null);
						sideEffectList.add(new SyntacticConstraint(doInfo.getCFGInfo().getNestedCFG().getEnd(), str));
						return sideEffectList;
					} else {
						return InsertImmediatePredecessor.insert(oldPred, targetNode);
					}
				} else {
					IfStatement breakingStmt = FrontEnd.parseAndNormalize("if (!(" + oldPred + ")) {break;}",
							IfStatement.class);
					CompoundStatement thenBody = (CompoundStatement) breakingStmt.getInfo().getCFGInfo().getThenBody();
					thenBody.getInfo().getCFGInfo().addElement(0, targetNode);
					sideEffectList.addAll(InsertImmediatePredecessor.insert(doCFGInfo.getPredicate(), breakingStmt));

					Expression newPred = FrontEnd.parseAndNormalize("1", Expression.class);
					doCFGInfo.setPredicate(newPred);
					return sideEffectList;
				}
			} else {
				assert (false);
				return null;
			}
		}

		@Override
		public List<SideEffect> visit(DoBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(DoEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ForBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ForInitLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ForTermLink link) {
			List<SideEffect> sideEffectList = new ArrayList<>();
			if (destinationLink instanceof ForBodyLink) {
				CompoundStatement body = (CompoundStatement) ((ForBodyLink) destinationLink).childNode;
				if (Misc.collidesFreelyWith(targetNode, body)) {
					Statement newStmt = FrontEnd.parseAndNormalize("{}", Statement.class);
					CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(newStmt);
					sideEffectList.addAll(link.enclosingNonLeafNode.getInfo().getCFGInfo().setBody(compStmt));
					sideEffectList.addAll(compStmt.getInfo().getCFGInfo().addElement(targetNode));
					sideEffectList.add(new AddedEnclosingBlock(compStmt));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(targetNode, body));
					return sideEffectList;
				} else {
					sideEffectList.addAll(InsertImmediateSuccessor
							.insert(body.getInfo().getCFGInfo().getNestedCFG().getBegin(), targetNode));
					return sideEffectList;
				}
			} else if (destinationLink instanceof ForEndLink) {
				Set<IncompleteEdge> edges = Misc.getInternalIncompleteEdges(targetNode);
				if (edges.stream()
						.anyMatch((e) -> (e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION
								|| e.getTypeOfIncompleteness() == TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION))) {
					sideEffectList.add(new JumpEdgeConstraint(targetNode));
					return sideEffectList;
				}
				ForStatement forStmt = ((ForEndLink) destinationLink).enclosingNonLeafNode;
				return InsertImmediateSuccessor.insert(forStmt, targetNode);
			} else {
				assert (false);
				return null;
			}
		}

		@Override
		public List<SideEffect> visit(ForStepLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ForBodyLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(ForEndLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CallBeginLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CallPreLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CallPostLink link) {
			assert (false);
			return null;
		}

		@Override
		public List<SideEffect> visit(CallEndLink link) {
			assert (false);
			return null;
		}
	}
}
