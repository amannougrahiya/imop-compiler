/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.ContextDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.mhp.YPhasePoint;
import imop.lib.analysis.mhp.incMHP.PhasePoint;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.HashSet;
import java.util.Set;

/**
 * Superclass of all fixed-point based data-flow analyses.
 *
 * @author Aman Nougrahiya
 */
public abstract class DataFlowAnalysis<F extends FlowAnalysis.FlowFact> extends FlowAnalysis<F> {

	public DataFlowAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
		super(analysisName, analysisDimension);
	}

	protected final void addAllSiblingBarriersToWorkList(BarrierDirective barrier) {
		for (AbstractPhase<?, ?> ph : new HashSet<>(barrier.getInfo().getNodePhaseInfo().getPhaseSet())) {
			for (AbstractPhasePointable abstractEndingPhasePoint : ph.getEndPoints()) {
				if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
					YPhasePoint endingPhasePoint = (YPhasePoint) abstractEndingPhasePoint;
					if (!(endingPhasePoint.getNode() instanceof BarrierDirective)) {
						continue;
					}
					BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNode();
					if (siblingBarrier == barrier) {
						continue;
					}
					if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON
							&& this.analysisDimension.getSVEDimension() == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInPhase(barrier, siblingBarrier, ph)) {
						continue;
					}
					this.workList.add(siblingBarrier);
				} else {
					PhasePoint endingPhasePoint = (PhasePoint) abstractEndingPhasePoint;
					if (!(endingPhasePoint.getNode() instanceof BarrierDirective)) {
						continue;
					}
					BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNode();
					if (siblingBarrier == barrier) {
						continue;
					}
					if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON
							&& this.analysisDimension.getSVEDimension() == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInPhase(barrier, siblingBarrier, ph)) {
						continue;
					}
					this.workList.add(siblingBarrier);
				}
			}
		}
	}

	/**
	 * f0 ::= DeclarationSpecifiers() f1 ::= ParameterAbstraction()
	 */
	@Override
	public final F visit(ParameterDeclaration n, F flowFactOne) {
		if (this.analysisDimension.getContextDimension() == ContextDimension.CONTEXT_SENSITIVE) {
			Misc.warnDueToLackOfFeature("No generic pass available for context-sensitive analysis.", null);
		}
		if (n.toString().equals("void ")) {
			return flowFactOne;
		}
		FunctionDefinition calledFunction = Misc.getEnclosingFunction(n);
		if (calledFunction.getInfo().getFunctionName().equals("main")) {
			return flowFactOne;
		}
		int index = calledFunction.getInfo().getCFGInfo().getParameterDeclarationList().indexOf(n);
		Set<CallStatement> callers = calledFunction.getInfo().getCallersOfThis();
		if (callers.isEmpty()) {
			return flowFactOne;
		}
		F flowFactTwo = this.getTop();
		for (CallStatement callStmt : callers) {
			SimplePrimaryExpression argExp;
			try {
				argExp = callStmt.getPreCallNode().getArgumentList().get(index);
			} catch (Exception e) {
				continue;
			}
			F tempFlowFact = writeToParameter(n, argExp, flowFactOne);
			flowFactTwo.merge(tempFlowFact, null);
		}
		return flowFactTwo;
	}

	/**
	 * Given a parameter-declaration {@code parameter}, and a
	 * simple-primary-expression {@code argument} that represents
	 * the argument for this parameter from some call-site, this method should model
	 * the flow-function of the write to
	 * the parameter that happens implicitly.
	 *
	 * @param parameter
	 *                    a {@code ParameterDeclaration} which needs to be assigned
	 *                    with the {@code argument}.
	 * @param argument
	 *                    a {@code SimplePrimaryExpression} which is assigned to the
	 *                    {@code parameter}.
	 * @param flowFactOne
	 *                    the IN/OUT flow-fact for the implicit assignment of the
	 *                    {@code argument} to the {@code parameter}.
	 *
	 * @return the OUT/IN flow-fact, as a result of the implicit assignment of the
	 *         {@code argument} to the {@code
	 * parameter}.
	 */
	public abstract F writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument, F flowFactOne);

}
