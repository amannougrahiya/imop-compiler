/*
 *   Copyright (c) 2020 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 *   This file is a part of the project IMOP, licensed under the MIT license.
 *   See LICENSE.md for the full text of the license.
 *
 *   The above notice shall be included in all copies or substantial
 *   portions of this file.
 */
package imop.lib.analysis.mhp.incMHP;

import imop.ast.info.cfgNodeInfo.ParallelConstructInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.analysis.mhp.yuan.YuanConcurrencyAnalysis;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.*;

/**
 * This class represents the May Happen in Parallel analysis pass.
 *
 * @author aman
 */
public class MHPAnalyzer {

	public ParallelConstruct parConstruct;
	public ParallelConstructInfo parInfo;

	/**
	 * @param parConstruct:
	 *                      The Parallel Construct on which MHP Analysis has to be
	 *                      performed.
	 */
	public MHPAnalyzer(ParallelConstruct parConstruct) {
		this.parConstruct = parConstruct;
		this.parInfo = parConstruct.getInfo();
	}

	/**
	 * Flushes all the data structures that are related to the MHP information.
	 */
	public static void flushALLMHPData() {
		/*
		 * Step 1: For every BeginPhasePoint in allBeginPhasePoints of class
		 * BeginPhasePoint,
		 * clear all the three sets.
		 * Step 2: Clear the allBeginPhasePoints and staleBeginPhasePoints sets
		 * of BeginPhasePoint.
		 * Step 3: For every node in any phase in allPhaseList of every
		 * ParallelConstruct,
		 * (i) clear MHP related info from its NodePhaseInfo, and
		 * (ii) if the node is a DFD, clear its incoming/outgoingInterTaskEdges
		 * sets.
		 * Step 5: For every phase in allPhaseList of every ParallelConstruct,
		 * clear all MHP related info.
		 * Step 6: For every parallel construct, clear the allPhaseList.
		 */
		AbstractPhasePointable.flushALLMHPData();
		for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			parCons.getInfo().flushALLMHPData();
		}
	}

	/**
	 * This method performs MHP analysis over all the parallelConstructs in the
	 * specified {@code root}. At the end, the
	 * nodes which may run in parallel have their phaseInfo.phaseList populated.
	 * Field {@link
	 * ParallelConstructInfo#allPhaseList} will also get populated.
	 *
	 * @param root
	 *             root node of the AST of the input program.
	 */
	public static void performMHPAnalysis(TranslationUnit root) {
		for (ParallelConstruct parCons : Misc.getExactEnclosee(root, ParallelConstruct.class)) { // for all parCons
			MHPAnalyzer mhp = new MHPAnalyzer(parCons);
			mhp.initMHP(); // perform MHP Analysis
		}
		for (FunctionDefinition funcDef : root.getInfo().getAllFunctionDefinitions()) {
			for (Node cfgNode : funcDef.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
				cfgNode.getInfo().getNodePhaseInfo().rememberCurrentPhases();
			}
		}
	}

	/**
	 * This method starts the MHP Analysis on parConstruct, removing all the
	 * previously generated phases.
	 */
	public void initMHP() {
		/*
		 * Step 1:
		 * Create a new phase-list, after removing the traces of old phases, if
		 * any.
		 */
		for (Node leafContent : parInfo.getCFGInfo().getIntraTaskCFGLeafContents()) {
			for (AbstractPhase<?, ?> stalePhase : new HashSet<>(parInfo.getAllPhaseList())) {
				leafContent.getInfo().getNodePhaseInfo().removePhase(stalePhase);
			}
		}
		parInfo.flushMHPData();

		/*
		 * Step 2:
		 * Create and populate the phases one by one, unless some end scenario
		 * is reached.
		 */
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			while (shouldProceedWithMHP()) {
				Phase ph = new Phase(parConstruct.getInfo());
				processNextPhase(ph);
			}
		} else {
			YuanConcurrencyAnalysis.reinitAggregatePhases(parConstruct);
		}
	}

	/**
	 * This method calls the phase marker. It reads the last entry in allPhaseLists
	 * to get the start points and then
	 * applies the interprocedural reachability analysis (or some alternative) on
	 * these start points. Note that the new
	 * phase has already been added to allPhaseList before this method is called.
	 */
	private void processNextPhase(Phase ph) {
		Phase lastPhase = null;
		List<Phase> allPhaseList = (List<Phase>) parInfo.getAllPhaseList();
		if (allPhaseList.size() > 1) {
			lastPhase = allPhaseList.get(allPhaseList.indexOf(ph) - 1);
		}

		/*
		 * Set the successor and predecessor edges with the last
		 * phase, if any, for phase-flow graph.
		 */
		if (lastPhase != null) {
			Phase.connectPhases(lastPhase, ph);
		}

		/*
		 * Obtain the beginPoints and startPoints. Note that beginPoints are
		 * endPoints of the previous phase, if any. Whereas, startPoints are
		 * those points with which phase marking has to start for the phase
		 * {@code ph}. If there is no last phase, then the only beginPoint to be
		 * considered is the BeginNode of the parallelConstruct.
		 */
		List<BeginPhasePoint> beginPoints = new ArrayList<>();
		List<NodeWithStack> startPoints = new ArrayList<>();
		if (lastPhase == null) {
			beginPoints.add(BeginPhasePoint.getBeginPhasePoint(
					new NodeWithStack(parInfo.getCFGInfo().getNestedCFG().getBegin(), new CallStack()), ph));
			startPoints.addAll(beginPoints);
		} else {
			for (PhasePoint aPoint : lastPhase.getEndPoints()) {
				if (aPoint.getNode() instanceof EndNode) {
					assert (((EndNode) aPoint.getNode()).getParent() instanceof ParallelConstruct);
					continue;
				} else {
					beginPoints.add(BeginPhasePoint.getBeginPhasePoint(aPoint, ph));
					startPoints.addAll(aPoint.getNode().getInfo().getCFGInfo()
							.getInterProceduralLeafSuccessors(aPoint.getCallStack()));
				}
			}
		}
		ph.setBeginPointsNoUpdate(beginPoints);

		/*
		 * Mark the nodes, and obtain the EndPoints.
		 */
		if (!applyAlternative(ph)) {
			ParallelPhaseMarker phaseMarker = new ParallelPhaseMarker(ph);
			for (NodeWithStack startPointWithStack : startPoints) {
				startPointWithStack.getNode().accept(phaseMarker, new CallStack(startPointWithStack.getCallStack()));
			}
		}
	}

	/**
	 * This method checks the applicability of some more precise MHP analysis, and
	 * applies it if possible.
	 *
	 * @param phase
	 * @param startPoints
	 *
	 * @return true if the alternate approach for MHP is applicable and applied.
	 */
	public boolean applyAlternative(Phase phase) {
		return false;
	}

	/**
	 * This method checks whether we shall stop the MHP Analysis. It reads the
	 * allPhaseList to take this decision.
	 * Multiple heuristics can be added to this function over time. Note: "Heuristic
	 * 1" below has side-effects on
	 * allPhaseList. Read the related comment.
	 *
	 * @return
	 */
	@SuppressWarnings("unlikely-arg-type")
	private boolean shouldProceedWithMHP() {
		/*
		 * Every parallel-construct has at least one phase.
		 */
		if (parInfo.getAllPhaseList().size() == 0) {
			return true;
		}

		/*
		 * No analysis needs to start with the end of the parallel-construct,
		 * so let's remove all such end-points from a copy from the last phase.
		 */
		List<Phase> allPhaseList = (List<Phase>) parInfo.getAllPhaseList();
		Phase lastPhase = allPhaseList.get(parInfo.getAllPhaseList().size() - 1);
		List<EndPhasePoint> lastEndPoints = new ArrayList<>();
		for (EndPhasePoint lastEndPoint : lastPhase.getEndPoints()) {
			if (lastEndPoint.getNode() instanceof EndNode) {
				continue;
			}
			lastEndPoints.add(lastEndPoint);
		}

		/*
		 * If entries in the last phase are subset of the entries in the
		 * beginPoints of some phase (may be itself), then we should stop.
		 */
		Phase superPhase = null;
		allPhaseList = (List<Phase>) parInfo.getAllPhaseList();
		outer: for (Phase otherPhase : allPhaseList) {
			superPhase = otherPhase;
			for (EndPhasePoint endPhasePoint : lastEndPoints) {
				if (!otherPhase.getBeginPoints().contains(endPhasePoint)) {
					superPhase = null;
					continue outer;
				}
			}
			break;
		}
		if (superPhase != null) {
			Phase.connectPhases(lastPhase, superPhase);
			return false;
		}

		// /*
		// * If the lastPhase ends at a subset of phase-points at which we have
		// * already
		// * ended in some other phase, then we should stop.
		// */
		// List<Phase> penultimatePhaseListCopy = new
		// ArrayList<>(parInfo.getAllPhaseList());
		// penultimatePhaseListCopy.remove(penultimatePhaseListCopy.size() - 1);
		// for (Phase oldPhase : penultimatePhaseListCopy) {
		// List<PhasePoint> oldEndPoints = new ArrayList<>(oldPhase.getEndPoints());
		// boolean foundCopy = true;
		// for (PhasePoint phPointLast : lastEndPoints) {
		// if (!oldEndPoints.contains(phPointLast)) {
		// foundCopy = false;
		// break;
		// }
		// }
		// if (foundCopy) {
		// lastPhase.setSuccPhases(new ArrayList<>(oldPhase.getSuccPhases()));
		// for (Phase nextPhase : oldPhase.getSuccPhases()) {
		// nextPhase.getPredPhases().add(lastPhase);
		// }
		// return false;
		// }
		// }
		//
		/**
		 * TODO: Implement this heuristic, if required.
		 * Heuristic 1: MHPAnalysis should be stopped if number of phases being
		 * generated is very high.
		 * Some upper bound based on the number of total barriers in the
		 * "region" should be used to limit
		 * the number of phases generated.
		 * TODO: IMPORTANT: In case if this method returns true due to this
		 * reason, the last over-approximated phase
		 * generation (or rather, generation of the last context-insensitive
		 * phase) should be called from here itself.
		 */

		// At the end, if there is no reason to stop MHP analysis, let it proceed.
		return true;
	}

}
