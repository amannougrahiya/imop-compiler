/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.annotation;

import imop.ast.annotation.IncompleteEdge.TypeOfIncompleteness;
import imop.ast.node.external.*;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cfg.link.node.SwitchPredicateLink;
import imop.lib.getter.SwitchRelevantStatementsGetter;
import imop.lib.util.Misc;

import java.util.*;

/**
 * This class represents the incomplete semantics associated with a newly
 * created AST node.
 * Initially, it only contains a set of incomplete edges, a.k.a those CFG edges
 * for which
 * either the source or the destination is not known.
 * 
 * @author aman
 *
 */
public class IncompleteSemantics extends Annotation {
	private Node owner;
	@SuppressWarnings("unused")
	@Deprecated
	private Set<IncompleteEdge> incompleteEdges;

	public IncompleteSemantics(Node owner) {
		this.setOwner(owner);
	}

	@Deprecated
	public void addToEdges(IncompleteEdge e) {
		this.getIncompleteEdges().add(e);
	}

	public Set<IncompleteEdge> getIncompleteEdges() {
		// OLD CODE:
		// if (incompleteEdges == null) {
		// incompleteEdges = new HashSet<>();
		// }
		// return incompleteEdges;
		Set<IncompleteEdge> retSet = new HashSet<>();
		if (this.owner instanceof GotoStatement) {
			if (this.owner.getInfo().getCFGInfo().getSuccBlocks().isEmpty()) {
				retSet.add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_GOTO_DESTINATION, this.owner));
			}
		} else if (this.owner instanceof BreakStatement) {
			if (this.owner.getInfo().getCFGInfo().getSuccBlocks().isEmpty()) {
				retSet.add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION, this.owner));
			}
		} else if (this.owner instanceof ContinueStatement) {
			if (this.owner.getInfo().getCFGInfo().getSuccBlocks().isEmpty()) {
				retSet.add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION, this.owner));
			}
		}
		// Note that this is not an else-if.
		if (this.owner instanceof Statement && !(this.owner instanceof OmpConstruct)
				&& !(this.owner instanceof OmpDirective)) {
			Statement stmt = (Statement) this.owner;
			for (Label l : stmt.getInfo().getLabelAnnotations()) {
				if (l instanceof CaseLabel) {
					if (!stmt.getInfo().getCFGInfo().getPredBlocks().stream()
							.anyMatch(p -> CFGLinkFinder.getCFGLinkFor(p) instanceof SwitchPredicateLink)) {
						retSet.add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_CASE_SOURCE, this.owner,
								(CaseLabel) l));
					}
				} else if (l instanceof DefaultLabel) {
					if (!stmt.getInfo().getCFGInfo().getPredBlocks().stream()
							.anyMatch(p -> CFGLinkFinder.getCFGLinkFor(p) instanceof SwitchPredicateLink)) {
						retSet.add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_DEFAULT_SOURCE, this.owner));
					}
				}
			}
		}
		return retSet;
	}

	/**
	 * CHecks whether this node has any incomplete edges.
	 * 
	 * @return
	 */
	public boolean hasIncompleteEdges() {
		if (this.owner instanceof GotoStatement || this.owner instanceof BreakStatement
				|| this.owner instanceof ContinueStatement) {
			if (this.owner.getInfo().getCFGInfo().getSuccBlocks().isEmpty()) {
				return true;
			}
		} else if (this.owner instanceof Statement && !(this.owner instanceof OmpConstruct)
				&& !(this.owner instanceof OmpDirective)) {
			Statement stmt = (Statement) this.owner;
			for (Label l : stmt.getInfo().getLabelAnnotations()) {
				if (l instanceof CaseLabel || l instanceof DefaultLabel) {
					if (!stmt.getInfo().getCFGInfo().getPredBlocks().stream()
							.anyMatch(p -> CFGLinkFinder.getCFGLinkFor(p) instanceof SwitchPredicateLink)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * When the owner node is a piece of code, this method ensures that the
	 * continue statements in that code, that connect to the immediately
	 * enclosing loop of the owner node, are added with an incomplete edge.
	 */
	public void adjustContinueSemanticsForLoopExpressionRemoval() {
		Statement outerLoop = (Statement) Misc.getEnclosingLoopOrForConstruct(this.getOwner());
		Set<Node> internalCFGNodes = this.getOwner().getInfo().getCFGInfo().getLexicalCFGContents();
		assert (outerLoop != null);
		for (Node internalStmtNode : internalCFGNodes) {
			if (!(internalStmtNode instanceof ContinueStatement)) {
				continue;
			}
			ContinueStatement contStmt = (ContinueStatement) internalStmtNode;
			Statement loopStmt = (Statement) Misc.getEnclosingLoopOrForConstruct(contStmt);
			if (loopStmt != outerLoop) {
				continue;
			}
			List<Node> succList = contStmt.getInfo().getCFGInfo().getSuccBlocks();
			assert (succList.size() == 1);
			CFGInfo.disconnectAndAdjustEndReachability(contStmt, succList.get(0));
			// OLD CODE:
			// contStmt.getInfo().getCFGInfo().clearSuccBlocks();
			// contStmt.getInfo().getIncompleteSemantics().getIncompleteEdges()
			// .add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION,
			// contStmt));
		}
	}

	/**
	 * When the owner node is a piece of code, this method ensures that the
	 * incomplete edges of the continue statements in that code, that connect to
	 * the immediately enclosing loop of the owner node, are removed, and
	 * replaced by the appropriate edges to the predicate.
	 */
	public void adjustContinueSemanticsForLoopExpressionAddition() {
		Statement outerLoop = (Statement) Misc.getEnclosingLoopOrForConstruct(this.getOwner());
		Set<Node> internalCFGNodes = this.getOwner().getInfo().getCFGInfo().getLexicalCFGContents();
		assert (outerLoop != null);
		for (Node internalNode : internalCFGNodes) {
			if (!(internalNode instanceof ContinueStatement)) {
				continue;
			}
			ContinueStatement contStmt = (ContinueStatement) internalNode;
			Statement loopStmt = (Statement) Misc.getEnclosingLoopOrForConstruct(contStmt);
			if (loopStmt != outerLoop) {
				continue;
			}

			CFGInfo.connectAndAdjustEndReachability(contStmt, contStmt.getInfo().getCFGInfo().getTarget());

			// OLD CODE:
			// /*
			// * Removing incompleteness from constituents of the added node.
			// */
			// Set<IncompleteEdge> incompleteEdgeList =
			// internalNode.getInfo().getIncompleteSemantics()
			// .getIncompleteEdges();
			// for (IncompleteEdge incompleteEdge : incompleteEdgeList) {
			// switch (incompleteEdge.getTypeOfIncompleteness()) {
			// case UNKNOWN_CONTINUE_DESTINATION:
			// ContinueStatement contStmt = (ContinueStatement) internalNode;
			// Statement continueDestination = (Statement)
			// Misc.getEnclosingLoopOrForConstruct(contStmt);
			//
			// if (continueDestination != outerLoop) {
			// continue;
			// }
			// Node continueSuccessor = null;
			// if (continueDestination instanceof ForStatement) {
			// ForStatement forStmt = (ForStatement) continueDestination;
			// if (forStmt.getInfo().getCFGInfo().hasStepExpression()) {
			// continueSuccessor = forStmt.getInfo().getCFGInfo().getStepExpression();
			// } else if (forStmt.getInfo().getCFGInfo().hasStepExpression()) {
			// continueSuccessor = ((ForStatementCFGInfo)
			// continueDestination.getInfo().getCFGInfo())
			// .getTerminationExpression();
			// } else {
			// continueSuccessor = Misc.getInternalFirstCFGNode(this.getOwner());
			// }
			// } else if (continueDestination instanceof DoStatement) {
			// continueSuccessor = ((DoStatementCFGInfo)
			// continueDestination.getInfo().getCFGInfo())
			// .getPredicate();
			// } else if (continueDestination instanceof WhileStatement) {
			// continueSuccessor = ((WhileStatementCFGInfo)
			// continueDestination.getInfo().getCFGInfo())
			// .getPredicate();
			// } else if (continueDestination instanceof ForConstruct) {
			// continueSuccessor = ((ForConstructCFGInfo)
			// continueDestination.getInfo().getCFGInfo())
			// .getReinitExpression();
			// }
			// CFGInfo.connectAndAdjustEndReachability(contStmt, continueSuccessor);
			// break;
			// default:
			// break;
			// }
			// }
		}
	}

	public void adjustSemanticsForSwitchPredicateRemoval() {
		SwitchRelevantStatementsGetter getter = new SwitchRelevantStatementsGetter();
		this.getOwner().accept(getter);
		Collection<Statement> relevantStmts = getter.relevantCFGStmts;
		SwitchStatement switchStmt = Misc.getEnclosingSwitch(this.getOwner());
		assert (switchStmt != null);
		Expression predicate = switchStmt.getInfo().getCFGInfo().getPredicate();
		for (Statement stmt : relevantStmts) {
			CFGInfo.disconnectAndAdjustEndReachability(predicate, stmt);
		}

		// OLD CODE:
		// Statement outerSwitch = Misc.getEnclosingSwitch(this.getOwner());
		// Set<Node> internalCFGNodes =
		// this.getOwner().getInfo().getCFGInfo().getLexicalCFGContents();
		// for (Node internalStmtNode : internalCFGNodes) {
		// if (!(internalStmtNode instanceof Statement)) {
		// continue;
		// }
		// Statement internalStmt = (Statement) internalStmtNode;
		// Statement switchStmt = Misc.getEnclosingSwitch(internalStmt);
		// if (switchStmt != outerSwitch) {
		// continue;
		// }
		//
		// for (Label internalLabel : internalStmt.getInfo().getLabelAnnotations()) {
		// if (internalLabel instanceof CaseLabel) {
		// List<Node> predecessorList =
		// internalStmt.getInfo().getCFGInfo().getPredBlocks();
		// Node toRemove = null;
		// for (Node pred : predecessorList) {
		// if (pred instanceof Expression) {
		// toRemove = pred;
		// break;
		// }
		// }
		// if (toRemove != null) {
		// predecessorList.remove(toRemove);
		// toRemove.getInfo().getCFGInfo().getSuccBlocks().remove(internalStmt);
		// }
		// //
		// internalStmt.getInfo().getIncompleteSemantics().getIncompleteEdges().add(new
		// IncompleteEdge(
		// // TypeOfIncompleteness.UNKNOWN_CASE_SOURCE, internalStmt, (CaseLabel)
		// internalLabel));
		// } else if (internalLabel instanceof DefaultLabel) {
		// List<Node> predecessorList =
		// internalStmt.getInfo().getCFGInfo().getPredBlocks();
		// Node toRemove = null;
		// for (Node pred : predecessorList) {
		// if (pred instanceof Expression) {
		// toRemove = pred;
		// break;
		// }
		// }
		// if (toRemove != null) {
		// predecessorList.remove(toRemove);
		// toRemove.getInfo().getCFGInfo().getSuccBlocks().remove(internalStmt);
		// }
		// // internalStmt.getInfo().getIncompleteSemantics().getIncompleteEdges()
		// // .add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_DEFAULT_SOURCE,
		// internalStmt));
		// }
		// }
		// }

	}

	public void adjustSemanticsForSwitchPredicateAddition() {
		SwitchRelevantStatementsGetter getter = new SwitchRelevantStatementsGetter();
		this.getOwner().accept(getter);
		Collection<Statement> relevantStmts = getter.relevantCFGStmts;
		SwitchStatement switchStmt = Misc.getEnclosingSwitch(this.getOwner());
		assert (switchStmt != null);
		Expression predicate = switchStmt.getInfo().getCFGInfo().getPredicate();
		for (Statement stmt : relevantStmts) {
			CFGInfo.connectAndAdjustEndReachability(predicate, stmt);
		}
		// OLD CODE:
		// Statement outerSwitch = Misc.getEnclosingSwitch(this.getOwner());
		// Expression predicate = ((SwitchStatementCFGInfo)
		// outerSwitch.getInfo().getCFGInfo()).getPredicate();
		// for (Node internalNode :
		// getOwner().getInfo().getCFGInfo().getLexicalCFGContents()) {
		// if (!(internalNode instanceof Statement)) {
		// continue;
		// }
		// SwitchStatement switchStmt = Misc.getEnclosingSwitch(internalNode);
		// if (switchStmt != outerSwitch) {
		// continue;
		// }
		//
		// /*
		// * Removing incompleteness from constituents of the owner node.
		// */
		// Set<IncompleteEdge> incompleteEdgeList =
		// internalNode.getInfo().getIncompleteSemantics()
		// .getIncompleteEdges();
		// for (IncompleteEdge incompleteEdge : incompleteEdgeList) {
		// switch (incompleteEdge.getTypeOfIncompleteness()) {
		// case UNKNOWN_CASE_SOURCE:
		// CFGInfo.connectAndAdjustEndReachability(predicate, internalNode);
		// break;
		// case UNKNOWN_DEFAULT_SOURCE:
		// CFGInfo.connectAndAdjustEndReachability(predicate, internalNode);
		// break;
		// default:
		// break;
		// }
		// }
		// }
	}

	/**
	 * Changes incomplete semantics when the owner node is removed from a CFG.
	 * <br>
	 * Note: We should call this method before removing the node from CFG.
	 */
	public void adjustSemanticsForOwnerRemoval() {
		Set<Node> removedCFGNodes = getOwner().getInfo().getCFGInfo().getLexicalCFGContents();
		for (Node internalStmtNode : removedCFGNodes) {
			if (!(internalStmtNode instanceof Statement)) {
				continue;
			}
			Statement internalStmt = (Statement) internalStmtNode;
			if (internalStmt instanceof GotoStatement) {
				GotoStatement gotoStmt = (GotoStatement) internalStmt;
				Statement labelStmt = gotoStmt.getInfo().getCFGInfo().getTarget();
				if (labelStmt == null || (!removedCFGNodes.contains(labelStmt))) {
					// This implies that the label resides outside the removed node.
					// Hence, incompleteness should be added.
					CFGInfo.disconnectAndAdjustEndReachability(gotoStmt, gotoStmt.getInfo().getCFGInfo().getTarget());
				}
			} else if (internalStmt instanceof BreakStatement) {
				BreakStatement breakStmt = (BreakStatement) internalStmt;
				Statement switchOrLoopStmt = (Statement) Misc.getEnclosingLoopOrSwitch(breakStmt);
				if (switchOrLoopStmt == null || (!removedCFGNodes.contains(switchOrLoopStmt))) {
					// This implies that the enclosing switch-statement/loop is not part of the
					// removed node.
					// Hence, incompleteness should be added.
					CFGInfo.disconnectAndAdjustEndReachability(breakStmt, breakStmt.getInfo().getCFGInfo().getTarget());
				}
			} else if (internalStmt instanceof ContinueStatement) {
				ContinueStatement contStmt = (ContinueStatement) internalStmt;
				Statement loopStmt = (Statement) Misc.getEnclosingLoopOrForConstruct(contStmt);
				if (loopStmt == null || (!removedCFGNodes.contains(loopStmt))) {
					// This implies that the enclosing loop statement is not part of the removed
					// node.
					// Hence, incompleteness should be added.
					CFGInfo.disconnectAndAdjustEndReachability(contStmt, contStmt.getInfo().getCFGInfo().getTarget());
				}
			} else if (internalStmt instanceof ReturnStatement) {
				ReturnStatement retStmt = (ReturnStatement) internalStmt;
				Node enclosingFunction = Misc.getEnclosingFunction(retStmt);
				if (enclosingFunction == null || !removedCFGNodes.contains(enclosingFunction)) {
					// This implies that the enclosing function is not a part of the removed node.
					// Hence, incompleteness.
					CFGInfo.disconnectAndAdjustEndReachability(retStmt, retStmt.getInfo().getCFGInfo().getTarget());
				}
			}

			/*
			 * Add incomplete edges for the missing source of case and default
			 * labels.
			 */
			for (Label label : internalStmt.getInfo().getLabelAnnotations()) {
				if (label instanceof SimpleLabel) {
					for (Node pred : new ArrayList<>(internalStmt.getInfo().getCFGInfo().getPredBlocks())) {
						if (pred instanceof GotoStatement) {
							GotoStatement gotoStmt = (GotoStatement) pred;
							if (gotoStmt.getInfo().getLabelName().equals(((SimpleLabel) label).getLabelName())) {
								if (!removedCFGNodes.contains(gotoStmt)) {
									// This implies that the source of this edge is not in the node to be removed.
									CFGInfo.disconnectAndAdjustEndReachability(gotoStmt, internalStmt);
								}
							}
						}
					}
				} else if (label instanceof CaseLabel) {
					SwitchStatement switchStmt = Misc.getEnclosingSwitch(internalStmt);
					if (switchStmt != null && !removedCFGNodes.contains(switchStmt)) {
						// This implies that the enclosing switch statement is not part of the removed
						// node.
						// Hence, incompleteness should be added.
						Expression switchPred = switchStmt.getInfo().getCFGInfo().getPredicate();
						CFGInfo.disconnectAndAdjustEndReachability(switchPred, internalStmt);
						// internalStmt.getInfo().getIncompleteSemantics().getIncompleteEdges().add(new
						// IncompleteEdge(
						// TypeOfIncompleteness.UNKNOWN_CASE_SOURCE, internalStmt, (CaseLabel) label));

					}
				} else if (label instanceof DefaultLabel) {
					SwitchStatement switchStmt = Misc.getEnclosingSwitch(internalStmt);
					if (switchStmt != null && !removedCFGNodes.contains(switchStmt)) {
						// This implies that the enclosing switch statement is not part of the removed
						// node.
						// Hence, incompleteness should be added.
						Expression switchPred = switchStmt.getInfo().getCFGInfo().getPredicate();
						CFGInfo.connectAndAdjustEndReachability(switchPred,
								switchStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());
						CFGInfo.disconnectAndAdjustEndReachability(switchPred, internalStmt);
						// internalStmt.getInfo().getIncompleteSemantics().getIncompleteEdges()
						// .add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_DEFAULT_SOURCE,
						// internalStmt));

					}
				}
			}

		}
		// CHECK: Removing the following line. Please check if it is needed.
		// internalStmt.getInfo().clearLabelAnnotations();
		// for (Label internalLabel : Misc.getLabels(internalStmt)) {
		// if (internalLabel instanceof SimpleLabel) {
		// if (sourceFunction == null) {
		// continue;
		// }
		// for (Node existingGotoStmtNode : existingCFGNodes) {
		// if (existingGotoStmtNode instanceof GotoStatement) {
		// GotoStatement existingGotoStmt = (GotoStatement) existingGotoStmtNode;
		// if (existingGotoStmt.getInfo().getLabelName()
		// .equals(((SimpleLabel) internalLabel).getLabelName())) {
		// // Adding incompleteness.
		// existingGotoStmt.getInfo().getCFGInfo().clearSuccessors();
		// existingGotoStmt.getInfo().getIncompleteSemantics().getIncompleteEdges()
		// .add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_GOTO_DESTINATION,
		// existingGotoStmt));
		// }
		// }
		// }
		// }
		// Statement switchStmt = (Statement) Misc.getEnclosingSwitch(internalStmt);
		// if (internalLabel instanceof CaseLabel) {
		// if (switchStmt == null || !(removedCFGNodes.contains(switchStmt))) {
		// // This implies that the enclosing switch statement is not part of the
		// removed node.
		// // Hence, incompleteness should be added.
		// List<Node> predecessorList =
		// internalStmt.getInfo().getCFGInfo().getPredBlocks();
		// Node toRemove = null;
		// for (Node pred : predecessorList) {
		// if (pred instanceof Expression) {
		// toRemove = pred;
		// break;
		// }
		// }
		// if (toRemove != null) {
		// predecessorList.remove(toRemove);
		// toRemove.getInfo().getCFGInfo().getSuccBlocks().remove(internalStmt);
		// }
		// internalStmt.getInfo().getIncompleteSemantics().getIncompleteEdges()
		// .add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_CASE_SOURCE,
		// internalStmt));
		// }
		// } else if (internalLabel instanceof DefaultLabel) {
		// if (switchStmt == null || !(removedCFGNodes.contains(switchStmt))) {
		// // This implies that the enclosing switch statement is not part of the
		// removed node.
		// // Hence, incompleteness should be added.
		// List<Node> predecessorList =
		// internalStmt.getInfo().getCFGInfo().getPredBlocks();
		// Node toRemove = null;
		// for (Node pred : predecessorList) {
		// if (pred instanceof Expression) {
		// toRemove = pred;
		// break;
		// }
		// }
		// if (toRemove != null) {
		// predecessorList.remove(toRemove);
		// toRemove.getInfo().getCFGInfo().getSuccBlocks().remove(internalStmt);
		// }
		// internalStmt.getInfo().getIncompleteSemantics().getIncompleteEdges()
		// .add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_DEFAULT_SOURCE,
		// internalStmt));
		// }
		//
		// }
		// }
	}

	/**
	 * Changes incomplete semantics when the owner node is added to a CFG. <br>
	 */
	public void adjustSemanticsForOwnerAddition() {
		// HashMap<SimpleLabel, Node> labelToNodeInAdded = new HashMap<>();
		Node outerMostEncloser = this.getOwner().getInfo().getOuterMostNonLeafEncloser();
		Set<Node> addedCFGNodes = getOwner().getInfo().getCFGInfo().getLexicalCFGContents();
		for (Node internalStmtNode : addedCFGNodes) {
			if (!(internalStmtNode instanceof Statement)) {
				continue;
			}
			Statement internalStmt = (Statement) internalStmtNode;
			if (internalStmt instanceof GotoStatement) {
				GotoStatement gotoStmt = (GotoStatement) internalStmt;
				Statement labelStmt = gotoStmt.getInfo().getCFGInfo().getTarget();
				if (labelStmt == null || (!addedCFGNodes.contains(labelStmt))) {
					// This implies that the label resides outside the added node.
					CFGInfo.connectAndAdjustEndReachability(gotoStmt, gotoStmt.getInfo().getCFGInfo().getTarget());
				}
			} else if (internalStmt instanceof BreakStatement) {
				BreakStatement breakStmt = (BreakStatement) internalStmt;
				Statement switchOrLoopStmt = (Statement) Misc.getEnclosingLoopOrSwitch(breakStmt);
				if (switchOrLoopStmt == null || (!addedCFGNodes.contains(switchOrLoopStmt))) {
					// This implies that the enclosing switch-statement/loop is not part of the
					// added node.
					CFGInfo.connectAndAdjustEndReachability(breakStmt, breakStmt.getInfo().getCFGInfo().getTarget());
				}
			} else if (internalStmt instanceof ContinueStatement) {
				ContinueStatement contStmt = (ContinueStatement) internalStmt;
				Statement loopStmt = (Statement) Misc.getEnclosingLoopOrForConstruct(contStmt);
				if (loopStmt == null || (!addedCFGNodes.contains(loopStmt))) {
					// This implies that the enclosing loop statement is not part of the added node.
					CFGInfo.connectAndAdjustEndReachability(contStmt, contStmt.getInfo().getCFGInfo().getTarget());
				}
			} else if (internalStmt instanceof ReturnStatement) {
				ReturnStatement retStmt = (ReturnStatement) internalStmt;
				Node enclosingFunction = Misc.getEnclosingFunction(retStmt);
				if (enclosingFunction == null || !addedCFGNodes.contains(enclosingFunction)) {
					// This implies that the enclosing function is not a part of the added node.
					CFGInfo.connectAndAdjustEndReachability(retStmt, retStmt.getInfo().getCFGInfo().getTarget());
				}
			}

			for (Label label : internalStmt.getInfo().getLabelAnnotations()) {
				if (label instanceof SimpleLabel) {
					SimpleLabel simpleLabel = (SimpleLabel) label;
					for (GotoStatement gotoStmt : Misc.getInheritedEnclosee(outerMostEncloser, GotoStatement.class)) {
						if (!addedCFGNodes.contains(gotoStmt)) {
							String labelName = gotoStmt.getInfo().getLabelName();
							if (!labelName.equals(simpleLabel.getLabelName())) {
								continue;
							}
							CFGInfo.connectAndAdjustEndReachability(gotoStmt, internalStmt);
						}
					}
				} else if (label instanceof CaseLabel) {
					SwitchStatement switchStmt = Misc.getEnclosingSwitch(internalStmt);
					if (switchStmt != null && !addedCFGNodes.contains(switchStmt)) {
						// This implies that the enclosing switch statement is not part of the added
						// node.
						Expression switchPred = switchStmt.getInfo().getCFGInfo().getPredicate();
						CFGInfo.connectAndAdjustEndReachability(switchPred, internalStmt);

					}
				} else if (label instanceof DefaultLabel) {
					SwitchStatement switchStmt = Misc.getEnclosingSwitch(internalStmt);
					if (switchStmt != null && !addedCFGNodes.contains(switchStmt)) {
						// This implies that the enclosing switch statement is not part of the added
						// node.
						Expression switchPred = switchStmt.getInfo().getCFGInfo().getPredicate();
						CFGInfo.connectAndAdjustEndReachability(switchPred, internalStmt);
						CFGInfo.disconnectAndAdjustEndReachability(switchPred,
								switchStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());

					}
				}
			}
			// OLD CODE:
			// /*
			// * Removing incompleteness from the added node (not from its
			// * constituents; check the outer loop for that.).
			// */
			// Set<IncompleteEdge> incompleteEdgeList =
			// internalNode.getInfo().getIncompleteSemantics()
			// .getIncompleteEdges();
			// for (IncompleteEdge incompleteEdge : incompleteEdgeList) {
			// switch (incompleteEdge.getTypeOfIncompleteness()) {
			// case UNKNOWN_GOTO_DESTINATION:
			// if (destinationFunction == null) {
			// continue;
			// }
			// GotoStatement gotoStmt = (GotoStatement) internalNode;
			// Statement labelDestination = destinationFunction.getInfo()
			// .getStatementWithLabel(gotoStmt.getInfo().getLabelName());
			// if (labelDestination != null) {
			// CFGInfo.connectAndAdjustEndReachability(gotoStmt, labelDestination);
			// }
			// break;
			// case UNKNOWN_BREAK_DESTINATION:
			// BreakStatement breakStmt = (BreakStatement) internalNode;
			// Statement breakDestination = (Statement)
			// Misc.getEnclosingLoopOrSwitch(breakStmt);
			// if (breakDestination != null) {
			// EndNode endNode =
			// breakDestination.getInfo().getCFGInfo().getNestedCFG().getEnd();
			// CFGInfo.connectAndAdjustEndReachability(breakStmt, endNode);
			// }
			// break;
			// case UNKNOWN_CONTINUE_DESTINATION:
			// ContinueStatement contStmt = (ContinueStatement) internalNode;
			// Statement continueDestination = (Statement)
			// Misc.getEnclosingLoopOrForConstruct(contStmt);
			// if (continueDestination != null) {
			// Node continueSuccessor = null;
			// if (continueDestination instanceof ForStatement) {
			// ForStatement forStmt = (ForStatement) continueDestination;
			// if (forStmt.getInfo().getCFGInfo().hasStepExpression()) {
			// continueSuccessor = forStmt.getInfo().getCFGInfo().getStepExpression();
			// } else if (forStmt.getInfo().getCFGInfo().hasStepExpression()) {
			// continueSuccessor = ((ForStatementCFGInfo)
			// continueDestination.getInfo().getCFGInfo())
			// .getTerminationExpression();
			// } else {
			// continueSuccessor = Misc.getInternalFirstCFGNode(this.getOwner());
			// }
			// } else if (continueDestination instanceof DoStatement) {
			// continueSuccessor = ((DoStatementCFGInfo)
			// continueDestination.getInfo().getCFGInfo())
			// .getPredicate();
			// } else if (continueDestination instanceof WhileStatement) {
			// continueSuccessor = ((WhileStatementCFGInfo)
			// continueDestination.getInfo().getCFGInfo())
			// .getPredicate();
			// } else if (continueDestination instanceof ForConstruct) {
			// continueSuccessor = ((ForConstructCFGInfo)
			// continueDestination.getInfo().getCFGInfo())
			// .getReinitExpression();
			// }
			// CFGInfo.connectAndAdjustEndReachability(contStmt, continueSuccessor);
			// }
			// break;
			// case UNKNOWN_RETURN_DESTINATION:
			// if (destinationFunction == null) {
			// break;
			// }
			// ReturnStatement retStmt = (ReturnStatement) internalNode;
			// CFGInfo.connectAndAdjustEndReachability(retStmt,
			// destinationFunction.getInfo().getCFGInfo().getNestedCFG().getEnd());
			// break;
			// case UNKNOWN_CASE_SOURCE:
			// SwitchStatement switchStmt = Misc.getEnclosingSwitch(internalNode);
			// if (switchStmt != null) {
			// CFGInfo.connectAndAdjustEndReachability(switchStmt.getInfo().getCFGInfo().getPredicate(),
			// internalNode);
			// }
			// break;
			// case UNKNOWN_DEFAULT_SOURCE:
			// switchStmt = Misc.getEnclosingSwitch(internalNode);
			// if (switchStmt != null) {
			// CFGInfo.connectAndAdjustEndReachability(switchStmt.getInfo().getCFGInfo().getPredicate(),
			// internalNode);
			// CFGInfo.disconnectAndAdjustEndReachability(switchStmt.getInfo().getCFGInfo().getPredicate(),
			// switchStmt.getInfo().getCFGInfo().getNestedCFG().getEnd());
			// }
			// break;
			// }
			// }
		}

		// for (Node gotoStmtNode : Misc.getInheritedEnclosee(destinationFunction,
		// GotoStatement.class)) {
		// GotoStatement gotoStmt = (GotoStatement) gotoStmtNode;
		// Set<IncompleteEdge> incompleteEdges =
		// gotoStmt.getInfo().getIncompleteSemantics().getIncompleteEdges();
		// if (incompleteEdges.size() != 0) {
		// String labelName = gotoStmt.getInfo().getLabelName();
		// for (SimpleLabel l : labelToNodeInAdded.keySet()) {
		// if (l.getLabelName().equals(labelName)) {
		// CFGInfo.connect(gotoStmt, labelToNodeInAdded.get(l));
		// incompleteEdges.clear();
		// break;
		// }
		// }
		// }
		// }
	}

	public Node getOwner() {
		return owner;
	}

	public void setOwner(Node owner) {
		this.owner = owner;
	}

}
