/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

import imop.ast.info.cfgNodeInfo.FunctionDefinitionInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.cfgTraversals.GJDepthFirstCFG;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.*;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis.FlowFact;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A generic class to implement an inter-thread interprocedural backward
 * iterative data-flow analysis.
 */
@Deprecated
public abstract class InterThreadBackwardIDFA<F extends FlowFact> extends GJDepthFirstCFG<F, F> {
	public static class NodeStatePair {
		private int hashCode = -1;

		@Override
		public int hashCode() {
			if (hashCode == -1) {
				final int prime = 31;
				hashCode = 1;
				hashCode = prime * hashCode + ((node == null) ? 0 : node.hashCode());
				hashCode = prime * hashCode + (stateINChanged ? 1231 : 1237);
				return hashCode;
			}
			return hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof NodeStatePair)) {
				return false;
			}
			NodeStatePair other = (NodeStatePair) obj;
			if (node == null) {
				if (other.node != null) {
					return false;
				}
			} else if (!node.equals(other.node)) {
				return false;
			}
			if (stateINChanged != other.stateINChanged) {
				return false;
			}
			return true;
		}

		public final Node node;
		public final boolean stateINChanged;

		public NodeStatePair(Node node, boolean stateINChanged) {
			this.node = node;
			this.stateINChanged = stateINChanged;
		}
	}

	/**
	 * A set of nodes with which we need to perform this analysis. <br>
	 * <i>Note that the processing of these nodes may recursively process the
	 * predecessor nodes as well.</i>
	 */
	Set<NodeStatePair> computeSet = new LinkedHashSet<>();

	public long nodesProcessed = 0;

	/**
	 * Name (unique-ID) to denote this analysis.
	 */
	private AnalysisName analysisName;

	private static boolean programHasBeenUpdated = false;

	/**
	 * A set of analyses that require backward IDFA.
	 */
	private static HashMap<AnalysisName, InterThreadBackwardIDFA<?>> analysisSet = new HashMap<>();

	public static void resetStaticFields() {
		InterThreadBackwardIDFA.analysisSet = new HashMap<>();
	}

	/**
	 * Set of analyses that require backward IDFA.
	 * 
	 * @return
	 *         set of analyses that require backward IDFA.
	 */
	public static HashMap<AnalysisName, InterThreadBackwardIDFA<?>> getBackwardAnalyses() {
		return analysisSet;
	}

	/**
	 * A set of nodes for which the IN is considered to be same as OUT. In
	 * essence, the flow-function of these nodes is considered to be an identity
	 * function.
	 */
	@Deprecated
	private Set<Node> fuseSet;

	/**
	 * Specifies whether this analysis is:
	 * <ul>
	 * <li>flow-sensitive/insensitive</li>
	 * <li>intraprocedural/interprocedural</li>
	 * <li>context-sensitive/insensitive</li>
	 * <li>field-sensitive/insensitive</li>
	 * <li>path-sensitive/insensitive</li>
	 * <ul>
	 */
	private AnalysisDimension analysisDimension;

	public HashMap<Node, Integer> tempMap = new HashMap<>();

	public InterThreadBackwardIDFA(AnalysisName analysisName, AnalysisDimension analysisDimension) {
		this.analysisName = analysisName;
		analysisSet.put(analysisName, this);

		if (analysisDimension == null) {
			analysisDimension = new AnalysisDimension(Program.sveSensitive);
		}

		this.analysisDimension = analysisDimension;

		if (analysisDimension.getFlowDimension() == FlowDimension.FLOW_INSENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for flow-insensitive analyses.", null);
		}
		if (analysisDimension.getContextDimension() == ContextDimension.CONTEXT_SENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for context-sensitive analyses.", null);
		}
		if (analysisDimension.getFieldDimension() == FieldDimension.FIELD_SENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for field-sensitive analysis.", null);
		}
		if (analysisDimension.getPathDimension() == PathDimension.PATH_SENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for path-sensitive analysis.", null);
		}
		if (analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for intra-procedural analysis.", null);
		}
	}

	/**
	 * Obtain the Top element of the associated lattice for this IDFA.
	 * 
	 * @return
	 *         Top element of the associated lattice for this IDFA.
	 */
	public abstract F getTop();

	/**
	 * Obtain flowFact to be used when a node has no successors.
	 * 
	 * @return
	 *         FlowFact to be used when a node has no successors.
	 */
	public abstract F getEntryFact();

	/**
	 * Perform the analysis, starting at the end node of the function
	 * {@code funcDef}.<br>
	 * <i>Note that this method does not clear the analysis information, if
	 * already present.</i>
	 * 
	 * @param funcDef
	 *                function-definition on which this analysis has to be
	 *                performed.
	 */
	public void run(FunctionDefinition funcDef) {
		this.fuseSet = new HashSet<>();
		EndNode endNode = funcDef.getInfo().getCFGInfo().getNestedCFG().getEnd();
		this.computeSet.clear();
		this.computeSet.add(new NodeStatePair(endNode, false));
		this.processBackwardIteratively();
		// this.processNodeBackward(endNode);
	}

	/**
	 * Start this analysis from all the nodes of {@code computeSet}.
	 * Computation of these nodes may trigger computations of other nodes as
	 * well.<br>
	 * <i>Note that this method does not clear the analysis information, if
	 * already present.</i>
	 * 
	 * @param computeSet
	 *                   a set of nodes which must be (re)analyzed.
	 */
	public void modularUpdateIDFA(Set<Node> computeSet) {
		InterThreadBackwardIDFA.programHasBeenUpdated = true;
		this.fuseSet = new HashSet<>();
		this.computeSet = new HashSet<>();
		for (Node n : computeSet) {
			if (Misc.isCFGLeafNode(n)) {
				this.computeSet.add(new NodeStatePair(n, false));
			} else {
				this.computeSet.add(new NodeStatePair(n.getInfo().getCFGInfo().getNestedCFG().getEnd(), false));
			}
		}
		this.processBackwardIteratively();
		InterThreadBackwardIDFA.programHasBeenUpdated = false;
	}

	/**
	 * Perform this analysis on all the functions.<br>
	 * <i>Note that this method does not clear the analysis information, if
	 * already present.</i>
	 */
	public void run() {
		if (this.analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL) {
			for (FunctionDefinition funcDefNode : Misc.getInheritedEnclosee(Program.getRoot(),
					FunctionDefinition.class)) {
				this.run(funcDefNode);
			}
		} else {
			this.computeSet.clear();
			FunctionDefinitionInfo mainInfo = Program.getRoot().getInfo().getMainFunction().getInfo();
			this.computeSet.add(new NodeStatePair(mainInfo.getCFGInfo().getNestedCFG().getEnd(), false));
			this.processBackwardIteratively();
			// this.run(computeSet);
		}
	}

	protected void processBackwardIteratively() {
		while (computeSet.size() != 0) {
			NodeStatePair nodeToBeAnalysed = Misc.getAnyElement(computeSet);
			computeSet.remove(nodeToBeAnalysed);
			this.processNodeBackward(nodeToBeAnalysed.node, nodeToBeAnalysed.stateINChanged);
		}
	}

	/**
	 * Perform the various steps involved in this backward IDFA analysis on the
	 * {@code node}.
	 * Process the predecessors, if needed.
	 * 
	 * @param node
	 *             node that has to be processed by this IDFA analysis.
	 */
	@SuppressWarnings("unchecked")
	protected void processNodeBackward(Node node, boolean stateINChanged) {
		stateINChanged = false;
		nodesProcessed++;
		Integer nodeProcessCount = tempMap.get(node);
		if (nodeProcessCount == null) {
			tempMap.put(node, 1);
		} else {
			tempMap.put(node, ++nodeProcessCount);
			if (nodeProcessCount > 70000) {
				System.out.println(node + "\t\t" + " exceeeded 70k iterations for IDFA analysis " + analysisName + ".");
			}
		}

		// Main code starts here.
		Set<IDFAEdge> successors = this.getInterTaskLeafSuccessors(node);
		// Step 2: Obtain the new state of OUT flow-fact.
		F newOUT = (F) node.getInfo().getOUT(analysisName);
		if (newOUT == null) {
			stateINChanged = true;
			if (successors.isEmpty()) {
				newOUT = this.getEntryFact();
			} else {
				newOUT = this.getTop();
			}
		}
		for (IDFAEdge idfaEdge : successors) {
			F succIN = (F) idfaEdge.getNode().getInfo().getIN(analysisName);
			if (succIN == null) {
				continue;
			}
			stateINChanged = stateINChanged | newOUT.merge(succIN, idfaEdge.getCells());
		}
		node.getInfo().setOUT(analysisName, newOUT);

		// Step 2: Apply the flow-function on OUT, to obtain the IN.
		F oldIN = (F) node.getInfo().getIN(analysisName);
		F newIN;
		newIN = node.accept(this, newOUT);
		node.getInfo().setIN(analysisName, newIN);

		// Step 3: Process the successors, if needed.
		if (((newOUT == newIN) && stateINChanged) || (oldIN == null || !newIN.isEqualTo(oldIN))) {
			for (IDFAEdge idfaEdge : this.getInterTaskLeafPredecessors(node)) {
				// Node predecessor = idfaEdge.getNode();
				// Set<Node> predSuccs =
				// predecessor.getInfo().getCFGInfo().getInterTaskLeafSuccessorNodes();
				// if (predSuccs.size() == 1) {
				// this.computeSet.add(new NodeStatePair(idfaEdge.getNode(), stateINChanged));
				// // Old code.
				// // F predOUT = (F) predecessor.getInfo().getOUT(analysisName);
				// // if (predOUT == newIN) {
				// // this.computeSet.add(new NodeStatePair(idfaEdge.getNode(), true));
				// // } else {
				// // // this.computeSet.add(new NodeStatePair(idfaEdge.getNode(), false));
				// // this.computeSet.add(new NodeStatePair(idfaEdge.getNode(),
				// stateINChanged));
				// // }
				// } else {
				this.computeSet.add(new NodeStatePair(idfaEdge.getNode(), false));
				// }
			}
		}
		// Old Code: Now this is done in the visit of BarrierDirective, if required.
		// /*
		// * Step 4: If node is a BarrierDirective, and if its IN has changed,
		// * re-evaluate equations for OUT of sibling barriers.
		// */
		//
		// if (node instanceof BarrierDirective) {
		// if ((oldOUT == null || !oldOUT.isEqualTo(newOUT)) &&
		// (!newOUT.isEqualTo(this.topElement))) {
		// for (Phase ph : node.getInfo().getNodePhaseInfo().getPhaseSetCopy()) {
		// for (PhasePoint endingPhasePoint : ph.getEndPointsCopy()) {
		// if (!(endingPhasePoint.getNode() instanceof BarrierDirective)) {
		// continue;
		// }
		// BarrierDirective siblingBarrier = (BarrierDirective)
		// endingPhasePoint.getNode();
		// if (node == siblingBarrier) {
		// continue;
		// }
		// this.computeSet.add(siblingBarrier);
		// }
		// }
		// }
		// }
	}

	/**
	 * Obtain a unique name that is used to denote this analysis.
	 * 
	 * @return
	 *         a unique name that is used to denote this analysis.
	 */
	public AnalysisName getAnalysisName() {
		return analysisName;
	}

	/////////////////////////////////////////////////
	// Default implementation of flow-functions below
	@Override
	public F initProcess(Node n, F flowFactOUT) {
		return flowFactOUT;
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public final F visit(FunctionDefinition n, F flowFactOUT) {
		assert (false);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public final F visit(ParallelConstruct n, F flowFactOUT) {
		assert (false);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public final F visit(ForConstruct n, F flowFactOUT) {
		assert (false);
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
	public final F visit(SectionsConstruct n, F flowFactOUT) {
		assert (false);
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
	public final F visit(SingleConstruct n, F flowFactOUT) {
		assert (false);
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
	public final F visit(TaskConstruct n, F flowFactOUT) {
		assert (false);
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
	public final F visit(ParallelForConstruct n, F flowFactOUT) {
		assert (false);
		return null;
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
	public final F visit(ParallelSectionsConstruct n, F flowFactOUT) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public final F visit(MasterConstruct n, F flowFactOUT) {
		assert (false);
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
	public final F visit(CriticalConstruct n, F flowFactOUT) {
		assert (false);
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
	public final F visit(AtomicConstruct n, F flowFactOUT) {
		assert (false);
		return flowFactOUT;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public final F visit(OrderedConstruct n, F flowFactOUT) {
		assert (false);
		return flowFactOUT;
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public final F visit(CompoundStatement n, F flowFactOUT) {
		assert (false);
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
	public final F visit(IfStatement n, F flowFactOUT) {
		assert (false);
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
	public final F visit(SwitchStatement n, F flowFactOUT) {
		assert (false);
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
	public final F visit(WhileStatement n, F flowFactOUT) {
		assert (false);
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
	public final F visit(DoStatement n, F flowFactOUT) {
		assert (false);
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
	public final F visit(ForStatement n, F flowFactOUT) {
		assert (false);
		return flowFactOUT;
	}

	/**
	 * Special Node
	 */
	@Override
	public final F visit(CallStatement n, F flowFactOUT) {
		assert (false);
		return flowFactOUT;
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public F visit(Declaration n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ParameterAbstraction()
	 */
	@Override
	public final F visit(ParameterDeclaration n, F flowFactOUT) {
		if (n.toString().equals("void ")) {
			return flowFactOUT;
		}
		F flowFactIN = this.getTop();
		if (this.analysisDimension.getContextDimension() == ContextDimension.CONTEXT_SENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available for context-sensitive analysis.", null);
		}
		FunctionDefinition calledFunction = Misc.getEnclosingFunction(n);
		int index = calledFunction.getInfo().getCFGInfo().getParameterDeclarationList().indexOf(n);
		for (CallStatement callStmt : calledFunction.getInfo().getCallersOfThis()) {
			SimplePrimaryExpression argExp;
			try {
				argExp = callStmt.getPreCallNode().getArgumentList().get(index);
			} catch (Exception e) {
				continue;
			}
			F tempFlowFact = writeToParameter(n, argExp, flowFactOUT);
			flowFactIN.merge(tempFlowFact, null);
		}
		return flowFactIN;
	}

	/**
	 * Given a parameter-declaration {@code parameter}, and a
	 * simple-primary-expression {@code argument} that represents the argument
	 * for this parameter from some call-site, this method should model the
	 * flow-function of the write to the parameter that happens implicitly.
	 * 
	 * @param parameter
	 *                    a {@code ParameterDeclaration} which needs to be assigned
	 *                    with
	 *                    the {@code argument}.
	 * @param argument
	 *                    a {@code SimplePrimaryExpression} which is assigned to the
	 *                    {@code parameter}.
	 * @param flowFactOUT
	 *                    the IN flow-fact for the implicit assignment of the
	 *                    {@code argument} to the {@code parameter}.
	 * @return
	 *         the OUT flow-fact, as a result of the implicit assignment of the
	 *         {@code argument} to the {@code parameter}.
	 */
	public abstract F writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument, F flowFactOUT);

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public F visit(UnknownCpp n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public F visit(UnknownPragma n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	@Override
	public F visit(OmpForInitExpression n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	@Override
	public F visit(OmpForCondition n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
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
	public F visit(OmpForReinitExpression n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public F visit(FlushDirective n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	@Override
	public F visit(DummyFlushDirective n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final F visit(BarrierDirective n, F flowFactOUT) {
		boolean changed = false;
		F flowFactIN = this.getTop();
		F oldFactIN = (F) n.getInfo().getIN(analysisName);
		if (oldFactIN != null) {
			flowFactIN.merge(oldFactIN, null);
		}
		for (AbstractPhase<?, ?> ph : new HashSet<>(n.getInfo().getNodePhaseInfo().getPhaseSet())) {
			for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
				if (!(endingPhasePoint.getNodeFromInterface() instanceof BarrierDirective)) {
					continue;
				}
				BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNodeFromInterface();
				F siblingFlowFactOUT = (F) siblingBarrier.getInfo().getOUT(analysisName);
				if (siblingFlowFactOUT == null) {
					continue;
				}
				if (siblingBarrier == n) {
					siblingFlowFactOUT = flowFactOUT;
					changed = changed | flowFactIN.merge(siblingFlowFactOUT, null);
				} else {
					changed = changed | flowFactIN.merge(siblingFlowFactOUT, n.getInfo().getSharedCellsAtNode());
				}
			}
		}
		if (changed) {
			for (AbstractPhase<?, ?> ph : new HashSet<>(n.getInfo().getNodePhaseInfo().getPhaseSet())) {
				for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
					if (!(endingPhasePoint.getNodeFromInterface() instanceof BarrierDirective)) {
						continue;
					}
					BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNodeFromInterface();
					if (siblingBarrier == n) {
						continue;
					}
					this.computeSet.add(new NodeStatePair(siblingBarrier, false));
				}
			}
		}
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public F visit(TaskwaitDirective n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public F visit(TaskyieldDirective n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public F visit(ExpressionStatement n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public F visit(GotoStatement n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public F visit(ContinueStatement n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public F visit(BreakStatement n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public F visit(ReturnStatement n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public F visit(Expression n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public F visit(IfClause n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public F visit(NumThreadsClause n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public F visit(FinalClause n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(BeginNode n, F flowFactOUT) {
		return flowFactOUT;
		// F flowFactIN;
		// flowFactIN = initProcess(n, flowFactOUT);
		// return flowFactIN;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(EndNode n, F flowFactOUT) {
		return flowFactOUT;
		// F flowFactIN;
		// flowFactIN = initProcess(n, flowFactOUT);
		// return flowFactIN;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(PreCallNode n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * Special Node
	 */
	@Override
	public F visit(PostCallNode n, F flowFactOUT) {
		F flowFactIN;
		flowFactIN = initProcess(n, flowFactOUT);
		return flowFactIN;
	}

	/**
	 * Obtain a set of successor nodes for this {@code node}, along with symbols
	 * via which information may flow on the edge connecting the successors to
	 * this {@code node}.
	 * 
	 * @param node
	 *             node whose successors have to be found.
	 * @return
	 *         a set of successor nodes for this {@code node}, along with
	 *         symbols via which information may flow on the edge connecting the
	 *         successors to this {@code node}.
	 */
	private Set<IDFAEdge> getInterTaskLeafSuccessors(Node node) {
		assert (Misc.isCFGLeafNode(node));
		if (this.analysisDimension.getFlowDimension() == FlowDimension.FLOW_INSENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for flow-insensitive analyses.", null);
			return null;
		}
		if (this.analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for intra-procedural analyses.", null);
			return null;
		} else {
			if (this.analysisDimension.getContextDimension() == ContextDimension.CONTEXT_SENSITIVE) {
				Misc.warnDueToLackOfFeature("No generic pass available yet for context-sensitive analyses.", null);
				return null;
			}
		}
		return node.getInfo().getCFGInfo().getInterTaskLeafSuccessorEdges(this.analysisDimension.getSVEDimension());
	}

	/**
	 * Obtain a set of predecessor nodes for this {@code node}, along with
	 * symbols
	 * via which information may flow on the edge connecting the predecessors to
	 * this {@code node}.
	 * 
	 * @param node
	 *             node whose predecessors have to be found.
	 * @return
	 *         a set of predecessor nodes for this {@code node}, along with
	 *         symbols via which information may flow on the edge connecting the
	 *         predecessors to this {@code node}.
	 */
	private Set<IDFAEdge> getInterTaskLeafPredecessors(Node node) {
		assert (Misc.isCFGLeafNode(node));
		if (this.analysisDimension.getFlowDimension() == FlowDimension.FLOW_INSENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for flow-insensitive analyses.", null);
			return null;
		}
		if (this.analysisDimension.getProceduralDimension() == ProceduralDimension.INTRA_PROCEDURAL) {
			Misc.warnDueToLackOfFeature("No generic pass available yet for intra-procedural analyses.", null);
			return null;
		} else {
			if (this.analysisDimension.getContextDimension() == ContextDimension.CONTEXT_SENSITIVE) {
				Misc.warnDueToLackOfFeature("No generic pass available yet for context-sensitive analyses.", null);
				return null;
			}
		}
		return node.getInfo().getCFGInfo().getInterTaskLeafPredecessorEdges(this.analysisDimension.getSVEDimension());
	}

	// /**
	// * Perform the analysis, starting at the end node of the function
	// * {@code funcDef}.<br>
	// * <i>Note that this method does not clear the analysis information, if
	// * already present.</i>
	// *
	// * @param funcDef
	// * function-definition on which this analysis has to be
	// * performed.
	// */
	// @Deprecated
	// public void runButFuse(FunctionDefinition funcDef, Set<Node> fuseSet) {
	// this.fuseSet = fuseSet;
	// EndNode endNode = funcDef.getInfo().getCFGInfo().getNestedCFG().getEnd();
	// this.computeSet.clear();
	// this.computeSet.add(endNode);
	// this.processBackwardIteratively();
	// // this.processNodeBackward(endNode);
	// }
	//
	// /**
	// * Start this analysis from all the nodes of {@code computeSet}.
	// * Computation of these nodes may trigger computations of other nodes as
	// * well.<br>
	// * <i>Note that this method does not clear the analysis information, if
	// * already present.</i>
	// *
	// * @param computeSet
	// * a set of nodes which must be (re)analyzed.
	// */
	// @Deprecated
	// public void runButFuse(Set<Node> computeSet, Set<Node> fuseSet) {
	// this.fuseSet = fuseSet;
	// this.computeSet = computeSet;
	// this.processBackwardIteratively();
	// // for (Node node : computeSet) {
	// // this.processNodeBackward(node);
	// // }
	// }
	//
	// /**
	// * Perform this analysis on all the functions.<br>
	// * <i>Note that this method does not clear the analysis information, if
	// * already present.</i>
	// */
	// @Deprecated
	// public void runButFuse(Set<Node> fuseSet) {
	// if (this.analysisDimension.getProceduralDimension() ==
	// ProceduralDimension.INTRA_PROCEDURAL) {
	// for (Node funcDefNode : Misc.getInheritedEnclosee(Main.root,
	// FunctionDefinition.class)) {
	// this.run((FunctionDefinition) funcDefNode);
	// }
	// } else {
	// this.computeSet.clear();
	// FunctionDefinitionInfo mainInfo =
	// Main.root.getInfo().getMainFunction().getInfo();
	// this.computeSet.add(mainInfo.getCFGInfo().getNestedCFG().getBegin());
	// this.runButFuse(computeSet, fuseSet);
	// }
	// }
}
