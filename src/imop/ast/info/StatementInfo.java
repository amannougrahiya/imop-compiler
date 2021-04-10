/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info;

import imop.ast.annotation.CaseLabel;
import imop.ast.annotation.DefaultLabel;
import imop.ast.annotation.Label;
import imop.ast.annotation.SimpleLabel;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.getter.SwitchRelevantStatementsGetter;
import imop.lib.util.Misc;
import imop.lib.util.ProfileSS;
import imop.parser.Program;
import imop.parser.Program.UpdateCategory;

import java.util.*;

public class StatementInfo extends NodeInfo {
	/**
	 * List of labels with which the owner statement has been annotated.
	 */
	private List<Label> annotatedLabels = new ArrayList<>();

	public StatementInfo(Node owner) {
		super(owner);
	}

	/**
	 * Obtain an unmodifiable list of labels that annotate the owner node.
	 *
	 * @return list of labels that annotate the owner node directly.
	 */
	public List<Label> getLabelAnnotations() {
		ProfileSS.addRelevantChangePoint(ProfileSS.labSet);
		if (this.getNode() instanceof OmpConstruct || this.getNode() instanceof OmpDirective) {
			return new ArrayList<>();
		}
		Node cfgNode = Misc.getCFGNodeFor(this.getNode());
		if (cfgNode != this.getNode()) {
			return ((StatementInfo) cfgNode.getInfo()).getLabelAnnotations();
		}
		return this.annotatedLabels;
	}

	/**
	 * Adds a labeled annotation to the owner statement at the specified index. Adds
	 * incompleteness if the enclosing
	 * SwitchStatement is missing, for case and default labels. Performs no other
	 * updates. This method should be called
	 * only while removing the AST node LabeledStatmenet.
	 *
	 * @param index
	 * @param newLabel
	 */
	public void initAddLabelAnnotation(Label newLabel) {
		ProfileSS.addRelevantChangePoint(ProfileSS.labSet);
		if (this.getNode() instanceof OmpConstruct || this.getNode() instanceof OmpDirective) {
			Misc.exitDueToError("Cannot add the label " + newLabel + " to an OpenMP construct/directive.");
		}
		Node cfgNode = Misc.getCFGNodeFor(this.getNode());
		if (cfgNode != this.getNode()) {
			((StatementInfo) cfgNode.getInfo()).initAddLabelAnnotation(newLabel);
			return;
		}

		// Add the newLabel now.
		newLabel.setLabeledCFGNode((Statement) this.getNode());
		this.annotatedLabels.add(newLabel);

		// Old code. Incomplete edges are now added only during CFG generation.
		// /*
		// * Add incompleteness for missing switch parent.
		// * Note that this method is called only once upon each removal of
		// * LabeledStatement.
		// */
		// if (newLabel instanceof CaseLabel) {
		// assert (((CaseLabel) newLabel).getParentSwitch() == null);
		// this.getIncompleteSemantics().getIncompleteEdges().add(
		// new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_CASE_SOURCE, this.getNode(),
		// (CaseLabel) newLabel));
		// } else if (newLabel instanceof DefaultLabel) {
		// assert (((DefaultLabel) newLabel).getParentSwitch() == null);
		// this.getIncompleteSemantics().getIncompleteEdges()
		// .add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_DEFAULT_SOURCE,
		// this.getNode()));
		// }
	}

	/**
	 * Adds a labeled annotation to the owner statement.
	 *
	 * @param newLabel
	 */
	public void addLabelAnnotation(Label newLabel) {
		PointsToAnalysis.disableHeuristic();
		this.addLabelAnnotation(-1, newLabel);
	}

	/**
	 * Adds a labeled annotation to the owner statement at the specified index.
	 *
	 * @param index
	 * @param newLabel
	 */
	public void addLabelAnnotation(int index, Label newLabel) {
		PointsToAnalysis.disableHeuristic();
		ProfileSS.addRelevantChangePoint(ProfileSS.labSet);
		if (this.getNode() instanceof OmpConstruct || this.getNode() instanceof OmpDirective) {
			Misc.exitDueToError("Cannot add the label " + newLabel + " to an OpenMP construct/directive.");
		}
		Node cfgNode = Misc.getCFGNodeFor(this.getNode());
		if (cfgNode != this.getNode()) {
			((StatementInfo) cfgNode.getInfo()).addLabelAnnotation(index, newLabel);
			return;
		}

		// If the label is already present at the index, return.
		int currentIndex = this.annotatedLabels.indexOf(newLabel);
		if (index != -1 && currentIndex == index) {
			return;
		}

		// Remove the label from its old location, if any.
		if (newLabel.getLabeledCFGNode() != null) {
			newLabel.getLabeledCFGNode().getInfo().removeLabelAnnotation(newLabel);
		}

		// Remove similar labels from other statements in the relevant context.
		this.removeSimilarLabelsFromRelevantContext(newLabel);

		// Add the newLabel.
		newLabel.setLabeledCFGNode((Statement) this.getNode());
		if (index == -1) {
			this.annotatedLabels.add(newLabel);
		} else {
			this.annotatedLabels.add(index, newLabel);
		}

		// Update CFG, FlowFacts, and other data structures.
		this.updateUponLabelAddition(newLabel);
	}

	/**
	 * Removes all those labels from the relevant context (enclosing function or
	 * switch) which are similar to <b>but not
	 * same as</b> the given {@code newLabel}.
	 *
	 * @param newLabel
	 *                 label whose similar (not same) labels have to be removed from
	 *                 the relevant context.
	 */
	private void removeSimilarLabelsFromRelevantContext(Label newLabel) {
		if (newLabel instanceof SimpleLabel) {
			SimpleLabel simpleLabel = (SimpleLabel) newLabel;
			Node outerMostCFGEncloser = this.getOuterMostNonLeafEncloser();
			if (outerMostCFGEncloser == null) {
				return;
			}
			for (Node enclosedNode : outerMostCFGEncloser.getInfo().getCFGInfo().getLexicalCFGContents()) {
				if (enclosedNode == this.getNode()) {
					continue;
				}
				if (enclosedNode instanceof Statement) {
					((StatementInfo) enclosedNode.getInfo()).removeSimpleLabelAnnotation(simpleLabel.getLabelName());
				}
			}
			return;
		} else {
			Collection<Statement> relevantTargets;
			SwitchStatement enclosingSwitch = Misc.getEnclosingSwitch(this.getNode());
			if (enclosingSwitch != null) {
				relevantTargets = enclosingSwitch.getInfo().getRelevantCFGStatements();
			} else {
				Node outerMostCFGEncloser = this.getOuterMostNonLeafEncloser();
				if (outerMostCFGEncloser == null) {
					return;
				}
				SwitchRelevantStatementsGetter getter = new SwitchRelevantStatementsGetter();
				outerMostCFGEncloser.accept(getter);
				relevantTargets = getter.relevantCFGStmts;
			}
			if (newLabel instanceof CaseLabel) {
				CaseLabel caseL = (CaseLabel) newLabel;
				String caseString = caseL.getCaseExpression().toString();
				for (Statement enclosedNode : relevantTargets) {
					if (enclosedNode == this.getNode()) {
						continue;
					}
					enclosedNode.getInfo().removeCaseLabelAnnotation(caseString);
				}
			} else {
				for (Statement enclosedNode : relevantTargets) {
					if (enclosedNode == this.getNode()) {
						continue;
					}
					enclosedNode.getInfo().removeDefaultLabelAnnotation();
				}
			}
		}
	}

	/**
	 * Removes a simple-label annotation from the owner statement.
	 *
	 * @param string
	 *               name of a simple-label that has to be removed.
	 *
	 * @return true, if the label was removed.
	 */
	public boolean removeSimpleLabelAnnotation(String string) {
		PointsToAnalysis.disableHeuristic();
		ProfileSS.addRelevantChangePoint(ProfileSS.labSet);
		if (this.getNode() instanceof OmpConstruct || this.getNode() instanceof OmpDirective) {
			return false;
		}
		Node cfgNode = Misc.getCFGNodeFor(this.getNode());
		if (cfgNode != this.getNode()) {
			return ((StatementInfo) cfgNode.getInfo()).removeSimpleLabelAnnotation(string);
		}

		for (Label lab : new HashSet<>(this.annotatedLabels)) {
			if (lab instanceof SimpleLabel) {
				SimpleLabel simpleL = (SimpleLabel) lab;
				if (simpleL.getLabelName().equals(string)) {
					return this.removeLabelAnnotation(simpleL);
				}
			}
		}
		return false;
	}

	/**
	 * Removes a case-label annotation from the owner statement.
	 *
	 * @param caseString
	 *                   string of constant-expression of the case-label that has to
	 *                   be removed.
	 *
	 * @return true, if the label was removed.
	 */
	private boolean removeCaseLabelAnnotation(String caseString) {
		PointsToAnalysis.disableHeuristic();
		for (Label lab : new HashSet<>(this.annotatedLabels)) {
			if (lab instanceof CaseLabel) {
				CaseLabel caseL = (CaseLabel) lab;
				if (caseL.getCaseExpression().toString().equals(caseString)) {
					return this.removeLabelAnnotation(caseL);
				}
			}
		}
		return false;
	}

	private boolean removeDefaultLabelAnnotation() {
		PointsToAnalysis.disableHeuristic();
		for (Label lab : new HashSet<>(this.annotatedLabels)) {
			if (lab instanceof DefaultLabel) {
				return this.removeLabelAnnotation(lab);
			}
		}
		return false;
	}

	/**
	 * This method performs required updates to various data structures upon
	 * addition of label {@code newLabel} to a
	 * node. Note that this method does not itself add the label.
	 *
	 * @param newLabel
	 *                 a label of the owner node.
	 */
	private void updateUponLabelAddition(Label newLabel) {
		// OLD CODE:
		// // Remove this label from all other places.
		// if (newLabel instanceof SimpleLabel) {
		// FunctionDefinition enclosingFunction = (FunctionDefinition)
		// Misc.getEnclosingFunction(this.getNode());
		// if (enclosingFunction != null) {
		// StatementInfo.removeLabelFromAllInFunction(enclosingFunction, newLabel,
		// this.getNode());
		// }
		// } else {
		// SwitchStatement enclosingSwitch = Misc.getEnclosingSwitch(this.getNode()); //
		// can be null.
		// if (enclosingSwitch != null) {
		// StatementInfo.removeLabelFromAllInSwitch(enclosingSwitch, newLabel,
		// this.getNode());
		// }
		// }

		PointsToAnalysis.disableHeuristic();
		AutomatedUpdater.flushCaches();
		// Update CFG, Incompleteness, FlowFacts, and other data structures.
		Set<Node> newPredecessors = this.adjustSemanticsForLabelAddition(newLabel); // ,
																					// LabelUpdateMode.UPDATE_ONLY_THIS);
		AutomatedUpdater.adjustPhaseAndInterTaskEdgesUponLabelUpdate(newPredecessors);

		Set<Node> startNodes = new HashSet<>();

		startNodes.add(this.getNode());
		if (newLabel instanceof DefaultLabel) {
			SwitchStatement switchStmt = Misc.getEnclosingSwitch(this.getNode());
			if (switchStmt != null) {
				EndNode endNode = switchStmt.getInfo().getCFGInfo().getNestedCFG().getEnd();
				startNodes.add(endNode);
			}
		}
		if (Program.updateCategory == UpdateCategory.EGINV || Program.updateCategory == UpdateCategory.LZINV
				|| Program.updateCategory == UpdateCategory.CPINV) {
			AutomatedUpdater.updateFlowFactsForward(new HashSet<>());
			AutomatedUpdater.updateFlowFactsBackward(new HashSet<>());
		} else {
			AutomatedUpdater.updateFlowFactsForward(startNodes);
			AutomatedUpdater.updateFlowFactsBackward(newPredecessors);
		}
		AutomatedUpdater.invalidateWriteBeforeCellsInSuccessorDummyFlushesOf(startNodes);
		AutomatedUpdater.invalidateReadAfterCellsInPredecessorDummyFlushesOf(newPredecessors);
		AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForAddition(this.getNode());
	}

	/**
	 * Re-adjusts the edges upon addition of {@code label} to this statement. This
	 * method assumes that any pre-existing
	 * labels with the same name have been removed already. Currently, this method
	 * performs adjustment of semantics
	 * represented by the following data structures:
	 * <ol>
	 * <li>CFG edges:
	 * <ul>
	 * <li>When a simple-label is added, we process all the
	 * GotoStatement's in the enclosing function, and add CFG edges (removing
	 * the IncompleteEdges) from the GotoStatement to the owner node.
	 * </li>
	 * <li>When a case label is added, we should add an edge from the enclosing
	 * switch-statement's predicate to the owner node. If the enclosing
	 * switch-statement is not found, we should add incompleteness.</li>
	 * <li>When a default label is added, we should remove the edge from the
	 * predicate to the end of the enclosing switch-statement. If the enclosing
	 * switch-statement is not found, we should add incompleteness.</li>
	 * </ul>
	 * </li>
	 * <li>Incomplete edges: <i>already explained in last point.</i></li>
	 * </ol>
	 * (Note that the actual addition of the label to the metadata of the
	 * associated statement is done by the method {@code addLabelAnnotation},
	 * which in turn calls this method after the addition. Or this method might
	 * have been called upon addition of a labeled node to certain point in the
	 * CFG.)
	 *
	 * @param label
	 *                          a new label that has been added to the owner node.
	 * @param labelAdditionMode
	 *                          when equal to WILL_ALL_BE_UPDATED, exactly one edge
	 *                          is always ensured between the switch-predicate and
	 *                          this node, if it is a case/default statement.
	 *
	 * @return a set of predecessors that have been added to the current node as a
	 *         result of insertion of {@code label}.
	 */
	public Set<Node> adjustSemanticsForLabelAddition(Label label) {
		PointsToAnalysis.disableHeuristic();
		AutomatedUpdater.flushCaches();
		Set<Node> addedPredecessors = new HashSet<>();
		if (label instanceof SimpleLabel) {
			SimpleLabel simpleLabel = (SimpleLabel) label;
			Node outerMostEncloser = this.getNode().getInfo().getOuterMostNonLeafEncloser();
			// FunctionDefinition enclosingFunction = (FunctionDefinition)
			// Misc.getEnclosingFunction(this.getNode());
			// if (enclosingFunction == null) {
			// return;
			// } else {
			for (GotoStatement gotoStmt : Misc.getInheritedEnclosee(outerMostEncloser, GotoStatement.class)) {
				String labelName = gotoStmt.getInfo().getLabelName();
				if (!labelName.equals(simpleLabel.getLabelName())) {
					continue;
				}
				CFGInfo.connectAndAdjustEndReachability(gotoStmt, this.getNode());
				addedPredecessors.add(gotoStmt);
			}
			AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForAddition(this.getNode());
			return addedPredecessors;
		} else {
			if (label instanceof CaseLabel) {
				SwitchStatement switchStatement = Misc.getEnclosingSwitch(this.getNode());
				if (switchStatement == null) {
					// No new CFG edges are required; no incompleteness had to be removed.
					AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForAddition(this.getNode());
					return addedPredecessors;
				}
				// OLD CODE: Now, we do not need to explicitly check about the existence of a
				// CFG edge
				// between the source and destination.
				// /*
				// * Do not add any new edges if thisStmt already has other
				// * case/default statements.
				// */
				// Statement thisStmt = (Statement) this.getNode();
				// boolean hasMoreCaseOrDefaultLabels = false;
				// for (Label thisLab : thisStmt.getInfo().getLabelAnnotations()) {
				// if (thisLab == label) {
				// continue;
				// }
				// if (thisLab instanceof CaseLabel || thisLab instanceof DefaultLabel) {
				// hasMoreCaseOrDefaultLabels = true;
				// break;
				// }
				// }
				// if (hasMoreCaseOrDefaultLabels) {
				// // Do not add an edge between predicate and thisStmt.
				// AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForAddition(this.getNode());
				// return addedPredecessors;
				// } else {
				Expression predicate = switchStatement.getInfo().getCFGInfo().getPredicate();
				CFGInfo.connectAndAdjustEndReachability(predicate, this.getNode());
				addedPredecessors.add(predicate);
				AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForAddition(this.getNode());
				return addedPredecessors;
				// }
			} else {
				// label instanceof DefaultLabel
				SwitchStatement switchStatement = Misc.getEnclosingSwitch(this.getNode());
				if (switchStatement == null) {
					// No new CFG edges are required; no incompletness had to be removed.
					AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForAddition(this.getNode());
					return addedPredecessors;
				}

				// OLD CODE: as above.
				// Statement thisStmt = (Statement) this.getNode();
				// /*
				// * Do not add any new edges if thisStmt already has other
				// * case/default statements.
				// */
				// boolean hasMoreCaseOrDefaultLabels = false;
				// for (Label thisLab : thisStmt.getInfo().getLabelAnnotations()) {
				// if (thisLab == label) {
				// continue;
				// }
				// if (thisLab instanceof CaseLabel || thisLab instanceof DefaultLabel) {
				// hasMoreCaseOrDefaultLabels = true;
				// break;
				// }
				// }
				// if (hasMoreCaseOrDefaultLabels && labelAdditionMode !=
				// LabelUpdateMode.WILL_ALL_BE_UPDATED) {
				// // Do not add an edge between predicate and thisStmt.
				// } else {
				/*
				 * Now, we need to ensure that only one CFG edge is
				 * added from switch-predicate to this node.
				 */
				// if
				// (!predicate.getInfo().getCFGInfo().getSuccBlocks().contains(this.getNode()))
				// {
				Expression predicate = switchStatement.getInfo().getCFGInfo().getPredicate();
				CFGInfo.connectAndAdjustEndReachability(predicate, this.getNode());
				EndNode endNode = switchStatement.getInfo().getCFGInfo().getNestedCFG().getEnd();
				CFGInfo.disconnectAndAdjustEndReachability(predicate, endNode);
				addedPredecessors.add(predicate);
				// }
				// }
				AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForAddition(this.getNode());
				return addedPredecessors;
			}
		}
	}

	/**
	 * Removes a label annotation from the owner statement.
	 *
	 * @param oldLabel
	 *                 the label that has to be removed.
	 *
	 * @return true if the label was removed.
	 */
	public boolean removeLabelAnnotation(Label oldLabel) {
		PointsToAnalysis.disableHeuristic();
		ProfileSS.addRelevantChangePoint(ProfileSS.labSet);
		if (this.getNode() instanceof OmpConstruct || this.getNode() instanceof OmpDirective) {
			return false;
		}
		Node cfgNode = Misc.getCFGNodeFor(this.getNode());
		if (cfgNode != this.getNode()) {
			return ((StatementInfo) cfgNode.getInfo()).removeLabelAnnotation(oldLabel);
		}

		if (!this.annotatedLabels.contains(oldLabel)) {
			return false;
		}
		assert (oldLabel.getLabeledCFGNode() == this.getNode());
		this.updateUponLabelRemoval(oldLabel);
		oldLabel.setLabeledCFGNode(null);
		return this.annotatedLabels.remove(oldLabel);
	}

	/**
	 * This method performs required updates to various data structures upon removal
	 * of label {@code oldLabel} to a
	 * node. Note that this method does not itself remove the label.
	 *
	 * @param oldLabel
	 *                 a label of the owner node.
	 */
	private void updateUponLabelRemoval(Label oldLabel) {
		PointsToAnalysis.disableHeuristic();
		AutomatedUpdater.flushCaches();
		if (!this.getLabelAnnotations().contains(oldLabel)) {
			return;
		}
		Set<Node> noMorePredecessors = this.adjustSemanticsForLabelRemoval(oldLabel);
		AutomatedUpdater.adjustPhaseAndInterTaskEdgesUponLabelUpdate(noMorePredecessors);

		Set<Node> startNodes = new HashSet<>();

		startNodes.add(this.getNode());
		if (oldLabel instanceof DefaultLabel) {
			SwitchStatement switchStmt = Misc.getEnclosingSwitch(this.getNode());
			if (switchStmt != null) {
				EndNode endNode = switchStmt.getInfo().getCFGInfo().getNestedCFG().getEnd();
				startNodes.add(endNode);
			}
		}
		if (Program.updateCategory == UpdateCategory.EGINV || Program.updateCategory == UpdateCategory.LZINV
				|| Program.updateCategory == UpdateCategory.CPINV) {
			AutomatedUpdater.updateFlowFactsForward(new HashSet<>());
			AutomatedUpdater.updateFlowFactsBackward(new HashSet<>());
		} else {
			AutomatedUpdater.updateFlowFactsForward(startNodes);
			AutomatedUpdater.updateFlowFactsBackward(noMorePredecessors);
		}
		AutomatedUpdater.invalidateWriteBeforeCellsInSuccessorDummyFlushesOf(startNodes);
		AutomatedUpdater.invalidateReadAfterCellsInPredecessorDummyFlushesOf(noMorePredecessors);
		AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForRemoval(this.getNode());
	}

	/**
	 * Readjusts the edges upon removal of {@code label} from this statement.<br>
	 * Currently, this method performs
	 * adjustment of semantics to the following data structures:
	 * <ol>
	 * <li>CFG Edges.</li>
	 * <li>If a simple-label is being removed, Incompleteness needs to be added
	 * to the corresponding goto-statements in the enclosing function.
	 * If a case/default-label is being removed, then related Incompleteness
	 * should be removed from this statement, and the parentSwitch should be
	 * made null for these labels.
	 * We take care of both these updates in this method.</li>
	 *
	 * </ol>
	 * (Note that the actual removal of the label from the metadata of the
	 * associated statement
	 * is done by the method {@code removeLabelAnnotation}, which in turn calls
	 * this method.)
	 *
	 * @param label
	 *                         label that has to be removed from this statement.
	 *                         Note that this label should be removed only after the
	 *                         call to this method returns.
	 * @param labelRemovalMode
	 *                         when equal to WILL_ALL_BE_UPDATED, the CFG edge from
	 *                         a switch-predicate to a case/default statement is
	 *                         always removed. Otherwise, the edge is removed only
	 *                         if the statement has exactly one case/default label.
	 *
	 * @return a set of GotoStatements (for SimpleLabel), or Expression (for
	 *         Case/DefaultLabels) due to which ls) due to
	 *         which incompleteness has been added.
	 */
	private Set<Node> adjustSemanticsForLabelRemoval(Label label) {
		PointsToAnalysis.disableHeuristic();
		AutomatedUpdater.flushCaches();
		Set<Node> noMorePredecessors = new HashSet<>();
		if (label instanceof SimpleLabel) {
			// FunctionDefinition enclosingFunction = (FunctionDefinition)
			// Misc.getEnclosingFunction(this.getNode());
			// if (enclosingFunction == null) {
			List<Node> predList = this.getNode().getInfo().getCFGInfo().getPredBlocks();
			for (Node pred : new ArrayList<>(predList)) {
				if (pred instanceof GotoStatement) {
					GotoStatement gotoStmt = (GotoStatement) pred;
					assert (gotoStmt.getInfo().getLabelName().equals(((SimpleLabel) label).getLabelName()));
					CFGInfo.disconnectAndAdjustEndReachability(gotoStmt, this.getNode());
					// OLD CODE:
					// gotoStmt.getInfo().getIncompleteSemantics().getIncompleteEdges()
					// .add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_GOTO_DESTINATION,
					// gotoStmt));
					noMorePredecessors.add(gotoStmt);
				}
			}
			AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForRemoval(this.getNode());
			return noMorePredecessors;
		} else {
			// When label is a Case/Default label.
			Statement thisStmt = (Statement) this.getNode();
			SwitchStatement switchStmt = Misc.getEnclosingSwitch(thisStmt);

			if (switchStmt != null) {
				Expression predicate = switchStmt.getInfo().getCFGInfo().getPredicate();
				if (label instanceof DefaultLabel) {
					EndNode endNode = switchStmt.getInfo().getCFGInfo().getNestedCFG().getEnd();
					CFGInfo.connectAndAdjustEndReachability(predicate, endNode);
				}
				/*
				 * If thisStmt has more than one Case/Default label
				 * then do not remove the CFG edge from the predicate of
				 * switchStmt to thisStmt.
				 */
				boolean hasMoreCaseOrDefaultLabels = false;
				for (Label thisLab : thisStmt.getInfo().getLabelAnnotations()) {
					if (thisLab == label) {
						continue;
					}
					if (thisLab instanceof CaseLabel || thisLab instanceof DefaultLabel) {
						hasMoreCaseOrDefaultLabels = true;
						break;
					}
				}
				if (!hasMoreCaseOrDefaultLabels) {
					CFGInfo.disconnectAndAdjustEndReachability(predicate, this.getNode());
					noMorePredecessors.add(predicate);
				}
			}
		}
		AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForRemoval(this.getNode());
		return noMorePredecessors;
	}

	/**
	 * Performs various automated updates owing to labels of a newly added node.
	 * Note that this method does not itself
	 * add the label/node. The label might already be present in
	 * {@code annotatedLabels} of the owner node, but the node
	 * might have been newly added to its new position.
	 *
	 * @param newLabel
	 *                 a label of the owner node.
	 */
	public void updateUponLabeledNodeAddition(Label newLabel) {
		PointsToAnalysis.disableHeuristic();
		if (!Misc.isCFGNode(this.getNode()) && !(this.getNode() instanceof ParallelForConstruct)
				&& !(this.getNode() instanceof ParallelSectionsConstruct)) {
			// if (!Misc.isCFGNode(this.getNode())) {
			assert (false);
		}

		AutomatedUpdater.flushCaches();
		// Remove similar labels from other statements in the relevant context.
		this.removeSimilarLabelsFromRelevantContext(newLabel);

		// Update CFG, Incompleteness, FlowFacts, and other data structures.
		Set<Node> newPredecessors = this.adjustSemanticsForLabelAddition(newLabel); // ,
																					// LabelUpdateMode.WILL_ALL_BE_UPDATED);
		AutomatedUpdater.adjustPhaseAndInterTaskEdgesUponLabelUpdate(newPredecessors);

		Set<Node> startNodes = new HashSet<>();

		startNodes.add(this.getNode());
		if (newLabel instanceof DefaultLabel) {
			SwitchStatement switchStmt = Misc.getEnclosingSwitch(this.getNode());
			if (switchStmt != null) {
				EndNode endNode = switchStmt.getInfo().getCFGInfo().getNestedCFG().getEnd();
				startNodes.add(endNode);
			}
		}
		if (Program.updateCategory == UpdateCategory.EGINV || Program.updateCategory == UpdateCategory.LZINV
				|| Program.updateCategory == UpdateCategory.CPINV) {
			AutomatedUpdater.updateFlowFactsForward(new HashSet<>());
			AutomatedUpdater.updateFlowFactsBackward(new HashSet<>());
		} else {
			AutomatedUpdater.updateFlowFactsForward(startNodes);
			AutomatedUpdater.updateFlowFactsBackward(newPredecessors);
		}
		AutomatedUpdater.invalidateWriteBeforeCellsInSuccessorDummyFlushesOf(startNodes);
		AutomatedUpdater.invalidateReadAfterCellsInPredecessorDummyFlushesOf(newPredecessors);
		AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForAddition(this.getNode());
	}

	/**
	 * This method performs required updates to various data structures upon removal
	 * of the node annotated with label
	 * {@code oldLabel}. Note that this method does not itself remove the node. It
	 * helps in modeling the scenario where
	 * the annotated node has to be removed from its current position.
	 *
	 * @param oldLabel
	 *                 a label of the owner node.
	 */
	public void updateUponLabeledNodeRemoval(Label oldLabel) {
		// if (!Misc.isCFGNode(this.getNode())) {
		PointsToAnalysis.disableHeuristic();
		AutomatedUpdater.flushCaches();
		if (!Misc.isCFGNode(this.getNode()) && !(this.getNode() instanceof ParallelForConstruct)
				&& !(this.getNode() instanceof ParallelSectionsConstruct)) {
			assert (false);
		}
		if (!this.getLabelAnnotations().contains(oldLabel)) {
			return;
		}
		Set<Node> noMorePredecessors = this.adjustSemanticsForLabeledNodeRemoval(oldLabel);
		AutomatedUpdater.adjustPhaseAndInterTaskEdgesUponLabelUpdate(noMorePredecessors);

		Set<Node> startNodes = new HashSet<>();
		startNodes.add(this.getNode());
		if (oldLabel instanceof DefaultLabel) {
			SwitchStatement switchStmt = Misc.getEnclosingSwitch(this.getNode());
			if (switchStmt != null) {
				EndNode endNode = switchStmt.getInfo().getCFGInfo().getNestedCFG().getEnd();
				startNodes.add(endNode);
			}
		}
		if (Program.updateCategory == UpdateCategory.EGINV || Program.updateCategory == UpdateCategory.LZINV
				|| Program.updateCategory == UpdateCategory.CPINV) {
			AutomatedUpdater.updateFlowFactsForward(new HashSet<>());
			AutomatedUpdater.updateFlowFactsBackward(new HashSet<>());
		} else {
			AutomatedUpdater.updateFlowFactsForward(startNodes);
			AutomatedUpdater.updateFlowFactsBackward(noMorePredecessors);
		}
		AutomatedUpdater.invalidateWriteBeforeCellsInSuccessorDummyFlushesOf(startNodes);
		AutomatedUpdater.invalidateReadAfterCellsInPredecessorDummyFlushesOf(noMorePredecessors);
		AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForRemoval(this.getNode());
	}

	/**
	 * Readjusts the edges upon removal of {@code label} from this statement.<br>
	 * Currently, this method performs
	 * adjustment of semantics to the following data structures:
	 * <ol>
	 * <li>CFG Edges.</li>
	 * <li>If a simple-label is being removed, Incompleteness needs to be added
	 * to the corresponding goto-statements in the enclosing function.
	 * If a case/default-label is being removed, then related Incompleteness
	 * should be removed from this statement, and the parentSwitch should be
	 * made null for these labels.
	 * We take care of both these updates in this method.</li>
	 *
	 * </ol>
	 * (Note that the actual removal of the label from the metadata of the
	 * associated statement
	 * is done by the method {@code removeLabelAnnotation}, which in turn calls
	 * this method.)
	 *
	 * @param label
	 *                         label that has to be removed from this statement.
	 *                         Note that this label should be removed only after the
	 *                         call to this method returns.
	 * @param labelRemovalMode
	 *                         when equal to WILL_ALL_BE_UPDATED, the CFG edge from
	 *                         a switch-predicate to a case/default statement is
	 *                         always removed. Otherwise, the edge is removed only
	 *                         if the statement has exactly one case/default label.
	 *
	 * @return a set of GotoStatements (for SimpleLabel), or Expression (for
	 *         Case/DefaultLabels) due to which ls) due to
	 *         which incompleteness has been added.
	 */
	private Set<Node> adjustSemanticsForLabeledNodeRemoval(Label label) {
		PointsToAnalysis.disableHeuristic();
		AutomatedUpdater.flushCaches();
		Set<Node> noMorePredecessors = new HashSet<>();
		if (label instanceof SimpleLabel) {
			List<Node> predList = this.getNode().getInfo().getCFGInfo().getPredBlocks();
			for (Node pred : new ArrayList<>(predList)) {
				if (pred instanceof GotoStatement) {
					GotoStatement gotoStmt = (GotoStatement) pred;
					assert (gotoStmt.getInfo().getLabelName().equals(((SimpleLabel) label).getLabelName()));
					CFGInfo.disconnectAndAdjustEndReachability(gotoStmt, this.getNode());
					noMorePredecessors.add(gotoStmt);
				}
			}
			AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForRemoval(this.getNode());
			return noMorePredecessors;
		} else {
			// When label is a Case/Default label.
			Statement thisStmt = (Statement) this.getNode();
			SwitchStatement switchStmt = Misc.getEnclosingSwitch(thisStmt);

			if (switchStmt != null) {
				Expression predicate = switchStmt.getInfo().getCFGInfo().getPredicate();
				if (label instanceof DefaultLabel) {
					EndNode endNode = switchStmt.getInfo().getCFGInfo().getNestedCFG().getEnd();
					CFGInfo.connectAndAdjustEndReachability(predicate, endNode);
				}
				CFGInfo.disconnectAndAdjustEndReachability(predicate, this.getNode());
				noMorePredecessors.add(predicate);
			}
		}
		AutomatedUpdater.setPointsToInstabilityFlagIfRequiredForRemoval(this.getNode());
		return noMorePredecessors;
	}

	/**
	 * Clears all the label annotations from the owner statement.
	 */
	public void clearLabelAnnotations() {
		PointsToAnalysis.disableHeuristic();
		ProfileSS.addRelevantChangePoint(ProfileSS.labSet);
		if (this.getNode() instanceof OmpConstruct || this.getNode() instanceof OmpDirective) {
			return;
		}
		Node cfgNode = Misc.getCFGNodeFor(this.getNode());
		if (cfgNode != this.getNode()) {
			((StatementInfo) cfgNode.getInfo()).clearLabelAnnotations();
			return;
		}

		for (Label label : new ArrayList<>(this.annotatedLabels)) {
			this.adjustSemanticsForLabelRemoval(label);
			label.setLabeledCFGNode(null);
			this.annotatedLabels.remove(label);
		}
	}

	/**
	 * Checks whether the owner node has any label annotations.
	 *
	 * @return
	 */
	public boolean hasLabelAnnotations() {
		ProfileSS.addRelevantChangePoint(ProfileSS.labSet);
		if (this.getNode() instanceof OmpConstruct || this.getNode() instanceof OmpDirective) {
			return false;
		}
		Node cfgNode = Misc.getCFGNodeFor(this.getNode());
		if (cfgNode != this.getNode()) {
			return ((StatementInfo) cfgNode.getInfo()).hasLabelAnnotations();
		}
		if (annotatedLabels == null || annotatedLabels.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Tests if any label with specified string exists in this node.
	 *
	 * @param string
	 *
	 * @return
	 */
	public boolean containsLabel(String string) {
		// if (!Misc.isCFGNode(this.getNode()))
		ProfileSS.addRelevantChangePoint(ProfileSS.labSet);
		if (this.getNode() instanceof OmpConstruct || this.getNode() instanceof OmpDirective) {
			return false;
		}
		Node cfgNode = Misc.getCFGNodeFor(this.getNode());
		if (cfgNode != this.getNode()) {
			return ((StatementInfo) cfgNode.getInfo()).containsLabel(string);
		}

		if (!this.hasLabelAnnotations()) {
			return false;
		}
		for (Label label : this.annotatedLabels) {
			if (label instanceof SimpleLabel) {
				SimpleLabel simpleLabel = (SimpleLabel) label;
				if (simpleLabel.getLabelName().equals(string)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Flag used to ensure that the task of populating annotations is carried out
	 * exactly once before finishing the
	 * calls to {@code getLabeledAnnotations} or {@code hasLabeledAnnotations}.
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private boolean labelAnnotationDone = false;

	/**
	 * Obtain an List of all the labels that may be annotated to the owner statement
	 * {@code n} (or rather, that of the
	 * CFG node that it wraps).
	 *
	 * @return list of immediate labels that annotate {@code n}.
	 *
	 * @deprecated Use {@link #getLabelAnnotations()} instead.
	 */
	@Deprecated
	@SuppressWarnings("unused")
	private List<Label> getLabels() {
		Node cfgNode = Misc.getCFGNodeFor(this.getNode());
		return ((StatementInfo) cfgNode.getInfo()).getLabelAnnotations();
	}

	/**
	 * Check whether there are any immediate labels that annotate the owner, or the
	 * CFG node that it wraps.
	 *
	 * @return true if there exist any immediate labels that annotate {@code n}.
	 *
	 * @deprecated Use {@link #hasLabelAnnotations()} instead.
	 */
	@Deprecated
	@SuppressWarnings("unused")
	private boolean hasLabels() {
		Node cfgNode = Misc.getCFGNodeFor(this.getNode());
		return ((StatementInfo) cfgNode.getInfo()).hasLabelAnnotations();
	}

	@Deprecated
	@SuppressWarnings("unused")
	private enum LabelUpdateMode {
		WILL_ALL_BE_UPDATED, UPDATE_ONLY_THIS
	}
}
