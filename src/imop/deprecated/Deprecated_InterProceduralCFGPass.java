/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.deprecated;

import imop.ast.info.cfgNodeInfo.FunctionDefinitionInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.cfgTraversals.GJDepthFirstCFG;
import imop.lib.analysis.flowanalysis.InterProceduralNode;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.cg.CallSite;
import imop.lib.util.Misc;
import imop.lib.util.PrintStyle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * A generic class to implement an interprocedural iterative data-flow analysis.
 * TODO: Handle Context-Sensitive analysis.
 */
@Deprecated
public abstract class Deprecated_InterProceduralCFGPass<F extends Deprecated_FlowFact> extends GJDepthFirstCFG<F, F> {
	/**
	 * Node where the analysis has to be started.
	 */
	protected Node entryNode;

	/**
	 * Set of nodes which have to be processed with this analysis.
	 */
	protected Set<Node> targetNodes;

	/**
	 * Specifies whether the traversal is supposed to be context-sensitive
	 * or -insensitive in nature.
	 * This field is used by the getSuccessorsOf/getPredecessorsOf methods.
	 */
	protected Deprecated_ContextDimension contextDimension;

	/**
	 * Specifies whether this analysis is supposed to be forward or backward
	 * in nature.
	 */
	protected Deprecated_AnalysisDirection analysisDirection;

	/**
	 * Enumerated name of the analysis, which is used to hash into the
	 * flowFactMaps (IN and OUT),
	 * for this analysis.
	 */
	protected AnalysisName analysisName;

	/**
	 * Set of those nodes which have been analyzed at least once.
	 * (This field is used in context-insensitive analysis.)
	 */
	private Set<Node> visitedNodes;

	/**
	 * Used to handle context-sensitivity, if the analysis has been declared so.
	 * TODO: This field needs modification to handle recursive programs.
	 */
	private Stack<CallSite> contextStack;

	/**
	 * Constructs an analysis on which forward/backwardAnalyzeNode can be
	 * called,
	 * with any element of targetSet as a parameter, to perform
	 * a context-insensitive or -sensitive iterative data-flow analysis.
	 *
	 * @param targetNodes
	 *                          Set of nodes which may get analyzed with this
	 *                          analysis.
	 *                          IN/OUT of these nodes are supposed to be initialized
	 *                          in the
	 *                          overridden
	 *                          initFlowFactsForNodes() method.
	 * @param contextDimension
	 *                          Specifies whether the nature of this
	 *                          traversal/analysis is
	 *                          supposed
	 *                          to be context-sensitive or -insensitive.
	 * @param analysisDirection
	 *                          Specifies whether the analysis is forward or
	 *                          backward in
	 *                          nature.
	 * @param analysisName
	 *                          Enumerator value that is used to hash into the
	 *                          flowFactsMap of
	 *                          nodes
	 *                          for obtaining the flowFacts related to this
	 *                          analysis.
	 * @param contextStack
	 *                          An initial stack of call-sites call-stack (if ever
	 *                          needed).
	 */
	public Deprecated_InterProceduralCFGPass(Node entryNode, Set<Node> targetNodes,
			Deprecated_ContextDimension contextDimension, Deprecated_AnalysisDirection analysisDirection,
			AnalysisName analysisName, Stack<CallSite> contextStack) {
		// TODO: Remove the following check after implementing the context-sensitive
		// version of the analysis correctly.
		if (contextDimension == Deprecated_ContextDimension.CONTEXT_SENSITIVE) {
			Misc.exitDueToLackOfFeature("Context-sensitive inter-procedural analysis.");
		}
		if (analysisDirection == Deprecated_AnalysisDirection.BACKWARD) {
			Misc.exitDueToLackOfFeature("Backward iterative data-flow analysis.");
		}
		this.entryNode = entryNode;
		this.targetNodes = targetNodes;
		this.contextDimension = contextDimension;
		this.contextStack = contextStack; // This entry is insignificant if analysis is context-insensitive.
		this.analysisDirection = analysisDirection;
		this.analysisName = analysisName;
		// this.initFlowFactsForNodes();
		visitedNodes = new HashSet<>();
	}

	public Deprecated_InterProceduralCFGPass(Node entryNode, Set<Node> targetNodes,
			Deprecated_ContextDimension contextDimension, Deprecated_AnalysisDirection analysisDirection,
			AnalysisName analysisName) {
		// TODO: Remove the following check after implementing the context-sensitive
		// version of the analysis correctly.
		if (contextDimension == Deprecated_ContextDimension.CONTEXT_SENSITIVE) {
			Misc.exitDueToLackOfFeature("Context-sensitive inter-procedural analysis.");
		}
		if (analysisDirection == Deprecated_AnalysisDirection.BACKWARD) {
			Misc.exitDueToLackOfFeature("Backward iterative data-flow analysis.");
		}
		this.entryNode = entryNode;
		this.targetNodes = targetNodes;
		this.contextDimension = contextDimension;
		this.analysisDirection = analysisDirection;
		this.analysisName = analysisName;
		// this.initFlowFactsForNodes();
		visitedNodes = new HashSet<>();
	}

	// /**
	// * For each node (say <code>n</code>) in targetSet that has to analyzed with
	// this analysis,
	// * this method should create an object of the associated FlowFact type,
	// * and insert that object along with an appropriate enumeration key (of type
	// AnalysisName)
	// * in the map <code>n.getInfo().flowFactsIN</code> (and similarly for
	// <code>n.getInfo().flowFactsOUT</code>).
	// * Note: Unless this step is done, method imop.ast.info.Info#getFlowFact* will
	// return null.
	// */
	// public abstract void initFlowFactsForNodes();

	/**
	 * This method should be overridden to return an object of supertype
	 * FlowFact
	 * that represents the notion of a TOP element in the lattice.
	 * 
	 * @return
	 */
	public abstract F getTop();

	// /**
	// * This method should be overridden to return an object of supertype FlowFact
	// * that represents the notion of a BOTTOM element in the lattice.
	// *
	// * @return
	// */
	// public abstract F getBottom();

	/**
	 * This method should be overridden to specify the IN flow fact
	 * for the entry node.
	 * 
	 * @return
	 */
	public abstract F getEntryFact();

	public void printFacts() {
		this.printFacts(PrintStyle.LEAF);
	}

	/**
	 * Print flow-information related to this analysis for all the leaf nodes in
	 * targetNodes.
	 */
	public void printFacts(PrintStyle printStyle) {
		System.err.println("Flow information for " + this.analysisName);
		if (analysisDirection == Deprecated_AnalysisDirection.FORWARD) {
			for (Node target : targetNodes) {
				if ((printStyle != PrintStyle.ALL) && (!Misc.isCFGLeafNode(target))) {
					continue;
				}
				if ((printStyle != PrintStyle.ALL || printStyle != PrintStyle.LEAF_AND_DUMMY)
						&& (target instanceof BeginNode || target instanceof EndNode)) {
					continue;
				}
				if (!target.getInfo().hasFlowFactIN(analysisName)) {
					continue;
				}
				System.err.print("Node (" + target.getClass().getSimpleName() + ") -- ");
				target.getInfo().printNode();
				System.err.println();
				target.getInfo().getFlowFactIN(analysisName, this).printFact();
				System.err.println();
			}
		} else {
			for (Node target : targetNodes) {
				if ((printStyle != PrintStyle.ALL) && (!Misc.isCFGLeafNode(target))) {
					continue;
				}
				if ((printStyle != PrintStyle.ALL || printStyle != PrintStyle.LEAF_AND_DUMMY)
						&& (target instanceof BeginNode || target instanceof EndNode)) {
					continue;
				}
				if (!target.getInfo().hasFlowFactOUT(analysisName)) {
					continue;
				}
				System.err.print("Node (" + target.getClass().getSimpleName() + ") -- ");
				target.getInfo().printNode();
				System.err.println();
				target.getInfo().getFlowFactOUT(analysisName, this).printFact();
				System.err.println();
			}
		}
	}

	/**
	 * Starts the analysis on entryNode, after initializing the flow-facts
	 * for the OUT/IN of various nodes.
	 * 
	 * @param entryNode
	 *                  Node at which the analysis has to start.
	 */
	public final void run() {
		this.initAnalysis();
		if (analysisDirection == Deprecated_AnalysisDirection.FORWARD) {
			this.forwardAnalyzeNode(entryNode);
		} else {
			this.backwardAnalyzeNode(entryNode);
		}
	}

	private void initAnalysis() {
		if (analysisDirection == Deprecated_AnalysisDirection.FORWARD) {
			for (Node tempNode : this.targetNodes) {
				tempNode.getInfo().setFlowFactOUT(analysisName, this.getTop());
			}
		} else { // BACKWARD
			for (Node tempNode : this.targetNodes) {
				tempNode.getInfo().setFlowFactIN(analysisName, this.getTop());
			}
		}
	}

	// /**
	// * This method should be overridden to return an object of supertype FlowFact
	// * that represents the notion of a BOTTOM element in the lattice.
	// *
	// * @return
	// */
	// public abstract F getBottom();

	/**
	 * Stub-method for analyzing a node in a forward iterative data-flow
	 * analysis.
	 * TODO: Change this algorithm from recursive to iterative in nature.
	 * 
	 * @param n
	 */
	@SuppressWarnings("unchecked")
	private final void forwardAnalyzeNode(Node n) {
		if (!visitedNodes.contains(n)) {
			visitedNodes.add(n);
		}
		System.err.println("Analyzing node (" + n.getClass().getSimpleName() + ")");
		// n.getInfo().printNode();
		/*
		 * STEP I: IN of $n$ = meet of OUT of all the predecessors of $n$.
		 * Note: Make sure that the run method should initialize the OUT of all
		 * $n$.
		 */
		Set<Node> predecessorSet = this.getPredecessorsOf(n, contextStack);

		F newIN = null;
		if (predecessorSet.isEmpty()) {
			newIN = this.getEntryFact();
		} else {
			F cumulativeMeet = (F) Misc.getAnyElement(predecessorSet).getInfo().getFlowFactOUT(analysisName, this);
			for (Node pred : predecessorSet) {
				cumulativeMeet = (F) cumulativeMeet.meetWith(pred.getInfo().getFlowFactOUT(analysisName, this));
			}
			newIN = cumulativeMeet;
		}
		/*
		 * STEP II: Apply the flow-function on IN of $n$ to obtain OUT of $n$.
		 */
		F newOUT = n.accept(this, newIN);

		/*
		 * STEP III: For each successor $s$ of $n$, analyze $s$:
		 * if $s$ has not been visited yet.
		 * OR, if OUT of n changes from its previous value.
		 * Note: We may want to skip visiting a node with some stricter
		 * condition.
		 * Such conditions can be specified by overriding the method
		 * skipNode(Node n, Node succ).
		 */
		F oldOUT = (F) n.getInfo().getFlowFactOUT(analysisName, this);
		n.getInfo().setFlowFactOUT(analysisName, newOUT);
		boolean outChanged = ((oldOUT != newOUT) && !oldOUT.isEqualTo(newOUT)); // To make sure that all the subclasses
																				// of FlowFact must specify equality,
		// we need this abstract method, rather than equals.
		for (InterProceduralNode succ : this.getSuccessorsOf(n, contextStack)) {
			if (skipNode(n, succ)) {
				continue;
			}
			if (outChanged || (succ.isCallSite() || (succ.isNode() && !visitedNodes.contains(succ.getNode())))) {
				/* Temp Code Starts */
				if (outChanged) {
					System.err.println("Old OUT: ");
					oldOUT.printFact();
					System.err.println("New OUT: ");
					// newOUT = (F) n.getInfo().getFlowFactIN(analysisName);
					newOUT.printFact();
				}
				/* Temp Code Ends */

				/*
				 * In case of context-sensitive analysis, if $n$ is a
				 * returnNode,
				 * or an EndNode of a function definition, then surely the
				 * successor
				 * should be processed only after the update in contextStack.
				 */
				CallSite poppedCS;
				poppedCS = null;
				if (contextDimension == Deprecated_ContextDimension.CONTEXT_SENSITIVE && (n instanceof ReturnStatement
						|| (n instanceof EndNode && n.getParent() instanceof FunctionDefinition))) {
					if (!contextStack.isEmpty()) {
						poppedCS = contextStack.pop();
					}
				}

				if (succ.isCallSite()) {
					CallSite sourceCallSite = succ.getCallSite();
					assert (sourceCallSite.calleeDefinition != null);
					if (contextDimension == Deprecated_ContextDimension.CONTEXT_SENSITIVE) {
						contextStack.push(sourceCallSite);
					}
					this.forwardAnalyzeNode(
							sourceCallSite.calleeDefinition.getInfo().getCFGInfo().getNestedCFG().getBegin());
					if (contextDimension == Deprecated_ContextDimension.CONTEXT_SENSITIVE) {
						contextStack.pop();
					}
				} else {
					this.forwardAnalyzeNode(succ.getNode());
				}

				if (contextDimension == Deprecated_ContextDimension.CONTEXT_SENSITIVE && (n instanceof ReturnStatement
						|| (n instanceof EndNode && n.getParent() instanceof FunctionDefinition))) {
					if (poppedCS != null) {
						contextStack.push(poppedCS);
					}
				}
			}
		}
	}

	/**
	 * Override this method to stop the analysis of $node$,
	 * just after the analysis of $parent$ is done.
	 * 
	 * @param parent
	 *               Parent node that has just been analyzed.
	 * @param node
	 *               Successor node that might be analyzed next, if
	 *               this method return false.
	 * @return
	 *         true, if the analysis should not be performed on $node$.
	 */
	public boolean skipNode(Node parent, InterProceduralNode node) {
		return false;
	}

	public Set<Node> getPredecessorsOf(Node n, Stack<CallSite> contextStack) {
		if (!Misc.isCFGNode(n)) {
			n = Misc.getInternalFirstCFGNode(n);
		}
		Set<Node> predSet = new HashSet<>();
		if (n instanceof BeginNode && n.getParent() instanceof FunctionDefinition) {
			// Set of call-sites that may have called the related function-definition.
			Set<CallSite> possibleCallSites = new HashSet<>();
			if (contextDimension == Deprecated_ContextDimension.CONTEXT_INSENSITIVE) {
				// Obtain all the possible call-sites of the related function.
				possibleCallSites = ((FunctionDefinitionInfo) n.getParent().getInfo()).oldGetCallersOf();
			} else { // Context-sensitive.
				possibleCallSites.add(contextStack.peek());
			}
			outer: for (CallSite cs : possibleCallSites) {
				Node callerNode = cs.callerCFGNode;
				List<CallSite> previousListReversed = Misc.getPreviousCallSitesReversed(callerNode, cs);
				for (CallSite tempCS : previousListReversed) {
					if (tempCS.calleeDefinition != null) {
						predSet.add(tempCS.calleeDefinition.getInfo().getCFGInfo().getNestedCFG().getEnd());
						continue outer;
					}
				}
				predSet.add(callerNode);
			}
			return predSet;
		} else {
			// Check if the intra-procedural predecessors of $n$ have any call sites in
			// them.
			outer: for (Node pred : n.getInfo().getCFGInfo().getPredecessors()) {
				List<CallSite> predCallSites = pred.getInfo().getCallSites();
				if (!predCallSites.isEmpty()) {
					for (int i = predCallSites.size() - 1; i >= 0; i--) {
						CallSite tempCS = predCallSites.get(i);
						if (tempCS.calleeDefinition != null) {
							// Now, obtain the predecessors of the endNode of the called function
							predSet.addAll(tempCS.calleeDefinition.getInfo().getCFGInfo().getNestedCFG().getEnd()
									.getInfo().getCFGInfo().getPredecessors());
							continue outer;
						}
					}
					predSet.add(pred);
				}
				predSet.add(pred);
			}
			return predSet;
		}
	}

	public Set<InterProceduralNode> getSuccessorsOf(Node n, Stack<CallSite> contextStack) {
		if (!Misc.isCFGNode(n)) {
			n = Misc.getInternalFirstCFGNode(n);
		}

		Set<InterProceduralNode> succSet = new HashSet<>();
		if (n instanceof ReturnStatement || (n instanceof EndNode && n.getParent() instanceof FunctionDefinition)) {
			if (contextDimension == Deprecated_ContextDimension.CONTEXT_INSENSITIVE) {
				// Find the enclosing function for this return statement/endNode.
				FunctionDefinition enclosingFunction = null;
				if (n.getParent() instanceof FunctionDefinition) {
					enclosingFunction = (FunctionDefinition) n.getParent();
				} else {
					enclosingFunction = Misc.getEnclosingFunction(n);
				}

				Set<CallSite> possibleSites = enclosingFunction.getInfo().oldGetCallersOf();

				outer: for (CallSite callSite : possibleSites) {
					Node callerNode = callSite.callerCFGNode;
					List<CallSite> nextCallSites = Misc.getNextCallSites(callerNode, callSite);
					for (CallSite tempCS : nextCallSites) {
						if (tempCS.calleeDefinition != null) {
							succSet.add(new InterProceduralNode(tempCS));
							continue outer;
						}
					}
					for (Node tempNode : callerNode.getInfo().getCFGInfo().getSuccessors()) {
						succSet.add(new InterProceduralNode(tempNode));
					}
				}
				return succSet;
			} else { // CONTEXT_SENSITIVE
				if (contextStack.isEmpty()) {
					return succSet;
				} else {
					CallSite returnTo = contextStack.peek();
					Node callerNode = returnTo.callerCFGNode;

					// Check and process other call sites, if any.
					List<CallSite> nextCallSites = Misc.getNextCallSites(callerNode, returnTo);
					for (CallSite callSite : nextCallSites) {
						if (callSite.calleeDefinition != null) {
							succSet.add(new InterProceduralNode(callSite));
							return succSet;
						}
					}
					for (Node tempNode : callerNode.getInfo().getCFGInfo().getSuccessors()) {
						succSet.add(new InterProceduralNode(tempNode));
					}
					return succSet;
				}
			}
		}

		// Check if $n$ has call-sites within it.
		List<CallSite> callSites = n.getInfo().getCallSites();
		if (callSites.isEmpty()) {
			if (Misc.isCFGLeafNode(n)) {
				for (Node tempNode : n.getInfo().getCFGInfo().getSuccessors()) {
					succSet.add(new InterProceduralNode(tempNode));
				}
			} else { // n is a non-leaf node.
				succSet.add(new InterProceduralNode(n.getInfo().getCFGInfo().getNestedCFG().getBegin()));
			}
			return succSet;
		} else {
			for (CallSite callSite : callSites) {
				if (callSite.calleeDefinition != null) {
					succSet.add(new InterProceduralNode(callSite));
					return succSet;
				}
			}
			if (Misc.isCFGLeafNode(n)) {
				for (Node tempNode : n.getInfo().getCFGInfo().getSuccessors()) {
					succSet.add(new InterProceduralNode(tempNode));
				}
			} else { // n is a non-leaf node.
				succSet.add(new InterProceduralNode(n.getInfo().getCFGInfo().getNestedCFG().getBegin()));
			}
			return succSet;
		}
	}

	/**
	 * Stub-method for analyzing a node in a backward iterative data-flow
	 * analysis.
	 * 
	 * @param n
	 */
	public final void backwardAnalyzeNode(Node n) {
		// TODO: Fill this up.
	}

	@Override
	public F initProcess(Node n, F flowFactIN) {
		return flowFactIN;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(BeginNode n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(EndNode n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public F visit(FunctionDefinition n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public F visit(Declaration n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public F visit(UnknownCpp n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public F visit(ParallelConstruct n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public F visit(UnknownPragma n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public F visit(ForConstruct n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	@Override
	public F visit(OmpForInitExpression n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	@Override
	public F visit(OmpForCondition n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= PostIncrementId()
	 * | PostDecrementId()
	 * | PreIncrementId()
	 * | PreDecrementId()
	 * | ShortAssignPlus()
	 * | ShortAssignMinus()
	 * | OmpForAdditive()
	 * | OmpForSubtractive()
	 * | OmpForMultiplicative()
	 */
	@Override
	public F visit(OmpForReinitExpression n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTIONS>
	 * f2 ::= NowaitDataClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= SectionsScope()
	 */
	@Override
	public F visit(SectionsConstruct n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SINGLE>
	 * f2 ::= SingleClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public F visit(SingleConstruct n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASK>
	 * f2 ::= ( TaskClause() )*
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public F visit(TaskConstruct n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <PARALLEL>
	 * f2 ::= <FOR>
	 * f3 ::= UniqueParallelOrUniqueForOrDataClauseList()
	 * f4 ::= OmpEol()
	 * f5 ::= OmpForHeader()
	 * f6 ::= Statement()
	 */
	@Override
	public F visit(ParallelForConstruct n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <PARALLEL>
	 * f2 ::= <SECTIONS>
	 * f3 ::= UniqueParallelOrDataClauseList()
	 * f4 ::= OmpEol()
	 * f5 ::= SectionsScope()
	 */
	@Override
	public F visit(ParallelSectionsConstruct n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public F visit(MasterConstruct n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public F visit(CriticalConstruct n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= ExpressionStatement()
	 */
	@Override
	public F visit(AtomicConstruct n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public F visit(FlushDirective n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public F visit(OrderedConstruct n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public F visit(BarrierDirective n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public F visit(TaskwaitDirective n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public F visit(TaskyieldDirective n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public F visit(ExpressionStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public F visit(CompoundStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 * f5 ::= ( <ELSE> Statement() )?
	 */
	@Override
	public F visit(IfStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public F visit(SwitchStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <WHILE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public F visit(WhileStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <DO>
	 * f1 ::= Statement()
	 * f2 ::= <WHILE>
	 * f3 ::= "("
	 * f4 ::= Expression()
	 * f5 ::= ")"
	 * f6 ::= ";"
	 */
	@Override
	public F visit(DoStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <FOR>
	 * f1 ::= "("
	 * f2 ::= ( Expression() )?
	 * f3 ::= ";"
	 * f4 ::= ( Expression() )?
	 * f5 ::= ";"
	 * f6 ::= ( Expression() )?
	 * f7 ::= ")"
	 * f8 ::= Statement()
	 */
	@Override
	public F visit(ForStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public F visit(GotoStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public F visit(ContinueStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public F visit(BreakStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public F visit(ReturnStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public F visit(Expression n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	@Override
	public F visit(DummyFlushDirective n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	@Override
	public F visit(PreCallNode n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	@Override
	public F visit(PostCallNode n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

	@Override
	public F visit(CallStatement n, F flowFactIN) {
		F flowFactOUT;
		flowFactOUT = initProcess(n, flowFactIN);
		return flowFactOUT;
	}

}
