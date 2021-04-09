/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp.yuan;

import imop.ast.info.cfgNodeInfo.ParallelConstructInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.SVEChecker;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.YPhasePoint;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class YuanConcurrencyAnalysis {

	/**
	 * Construct all aggregate phases within the given parCons. Note that there is
	 * no reuse from the existing set of
	 * phases or phase-points.
	 *
	 * @param parConstruct
	 */
	public static void reinitAggregatePhases(ParallelConstruct parConstruct) {
		// Step 1: Construct the barrier trees.
		BTNode btn = BarrierTreeConstructor.getBarrierTreeFor(parConstruct);

		// Step 2: Within the generated barrier tree, match all barrier pairs and store
		// them in btPairs.
		btn.populateBTPairs(parConstruct);

		// Step 3: Collect all static phases.
		Set<YuanStaticPhase> allStaticPhases = getStaticPhases(parConstruct);

		// Step 4: Generate aggregate phases.
		Collection<YPhase> aggregatePhases = new HashSet<>();
		if (parConstruct.getInfo().getBTPairs().isEmpty()) {
			// Special case: Generate one aggregate phase per static phase.
			for (YuanStaticPhase sp : allStaticPhases) {
				YPhase newPhase = new YPhase(parConstruct);
				List<YPhasePoint> beginPoints = new ArrayList<>();
				beginPoints.add(YPhasePoint.getYPhasePoint(sp.startingBarrier, newPhase));
				newPhase.setBeginPointsNoUpdate(beginPoints);
				for (Node n : sp.stmts) {
					newPhase.addNode(n);
				}
				newPhase.addEndPointNoUdpate(new YPhasePoint(sp.endingBarrier));
				aggregatePhases.add(newPhase);
			}
		} else {
			Queue<Set<YuanStaticPhase>> queue = new LinkedList<>();
			for (YuanStaticPhase currSP : allStaticPhases) {
				int size = queue.size();
				boolean mergedCurrSP = false;
				for (int i = 0; i < size; i++) {
					Set<YuanStaticPhase> currSet = queue.remove();
					Set<YuanStaticPhase> matchingSet = currSet.stream().filter(s -> s.matchesWith(currSP))
							.collect(Collectors.toCollection(HashSet::new));
					if (matchingSet.isEmpty()) {
						queue.add(currSet); // Do nothing.
					} else if (matchingSet.size() == currSet.size()) {
						currSet.add(currSP);
						queue.add(currSet);
						mergedCurrSP = true;
					} else {
						queue.add(currSet);
						matchingSet.add(currSP);
						queue.add(matchingSet);
						mergedCurrSP = true;
					}
				}
				if (!mergedCurrSP) {
					// Create a new entry corresponding to the given static phase.
					queue.add(new HashSet<>(Arrays.asList(currSP)));
				}
			}
			// Create aggregate phases from the queue.
			for (Set<YuanStaticPhase> staticSet : queue) {
				YPhase newPhase = new YPhase(parConstruct);
				List<YPhasePoint> beginPoints = new ArrayList<>();
				for (YuanStaticPhase sp : staticSet) {
					beginPoints.add(YPhasePoint.getYPhasePoint(sp.startingBarrier, newPhase));
					for (Node n : sp.stmts) {
						newPhase.addNode(n);
					}
					newPhase.addEndPointNoUdpate(new YPhasePoint(sp.endingBarrier));
				}
				newPhase.setBeginPointsNoUpdate(beginPoints);
				aggregatePhases.add(newPhase);
			}
		}

		// Step 5: Generate PFG edges among the aggregate phases.
		for (YPhase yp1 : aggregatePhases) {
			Set<YPhasePoint> endPoints = yp1.getStaleEndPoints();
			int count = 0;
			for (YPhase yp2 : aggregatePhases) {
				// Check if begin points of yp2 are exactly same as endPoints of yp1
				if (yp2.getStaleBeginPoints().equals(endPoints)) {
					AbstractPhase.connectPhases(yp1, yp2);
					count++;
				}
			}
		}

	}

	public static class YuanStaticPhase {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((endingBarrier == null) ? 0 : endingBarrier.hashCode());
			result = prime * result + ((startingBarrier == null) ? 0 : startingBarrier.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			YuanStaticPhase other = (YuanStaticPhase) obj;
			if (this.endingBarrier != other.endingBarrier) {
				return false;
			}
			return this.startingBarrier == other.startingBarrier;
		}

		public final Node startingBarrier;
		public final Node endingBarrier;
		public final Set<Node> stmts = new HashSet<>();
		public final ParallelConstruct ownerConstruct;

		public YuanStaticPhase(Node startingBarrier, Node endingBarrier, ParallelConstruct owner) {
			this.startingBarrier = startingBarrier;
			this.endingBarrier = endingBarrier;
			stmts.addAll(Misc.setIntersection(startingBarrier.getInfo().getFWRNodes(),
					endingBarrier.getInfo().getBWRNodes()));
			stmts.add(endingBarrier);
			if (this.startingBarrier instanceof BeginNode) {
				stmts.add(this.startingBarrier);
			}
			this.ownerConstruct = owner;
		}

		public boolean matchesWith(YuanStaticPhase other) {
			if (other == null) {
				return false;
			}
			Set<ParallelConstructInfo.BTPair> btPairs = ownerConstruct.getInfo().getBTPairs();
			if (btPairs == null || btPairs.isEmpty()) {
				Thread.dumpStack();
				assert (false) : "Unexpected path";
				return false;
			}
			boolean foundBegin = false;
			boolean foundEnd = false;
			for (ParallelConstructInfo.BTPair btPair : btPairs) {
				if ((btPair.first.getASTNode() == this.startingBarrier
						&& btPair.second.getASTNode() == other.startingBarrier)
						|| (btPair.second.getASTNode() == this.startingBarrier
								&& btPair.first.getASTNode() == other.startingBarrier)) {
					foundBegin = true;
				}

				if ((btPair.first.getASTNode() == this.endingBarrier
						&& btPair.second.getASTNode() == other.endingBarrier)
						|| (btPair.second.getASTNode() == this.endingBarrier
								&& btPair.first.getASTNode() == other.endingBarrier)) {
					foundEnd = true;
				}
			}
			return foundBegin && foundEnd;
		}
	}

	public static void performMHPByYuan() {
		int aggregateSize = 0;
		List<Integer> setSizes = new ArrayList<>();
		long timer = System.nanoTime();
		for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			BTNode btn = BarrierTreeConstructor.getBarrierTreeFor(parCons);
			btn.populateBTPairs(parCons);
			Set<YuanStaticPhase> getAllStaticPhases = YuanConcurrencyAnalysis.getStaticPhases(parCons);
			aggregateSize += getAllStaticPhases.size();
			for (YuanStaticPhase ph : getAllStaticPhases) {
				setSizes.add(ph.stmts.size());
			}
		}
		System.out.println("Time: " + (System.nanoTime() - timer) / 1e9 + "s.");
		int tot = 0;
		for (Integer i : setSizes) {
			tot += i;
		}
		int numNodes = Program.getRoot().getInfo().getCFGInfo().getLexicalCFGLeafContents().size();
		int maxSetSize = Collections.max(setSizes);
		double relMaxSetSize = (maxSetSize * 100.0) / numNodes;
		double avgSetSize = (tot * 1.0) / setSizes.size();
		double relAvgSetSize = (avgSetSize * 100.0) / numNodes;
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println(aggregateSize + " " + maxSetSize + " " + df.format(relMaxSetSize) + " "
				+ df.format(avgSetSize) + " " + df.format(relAvgSetSize));
		System.exit(0);
	}

	public static Set<YuanStaticPhase> getStaticPhases(ParallelConstruct parCons) {
		Set<YuanStaticPhase> retSet = new HashSet<>();
		Set<Node> visited = new HashSet<>();
		Set<Node> workList = new HashSet<>();
		BeginNode bn = parCons.getInfo().getCFGInfo().getNestedCFG().getBegin();
		workList.add(bn);
		while (!workList.isEmpty()) {
			Node barr = Misc.getAnyElement(workList);
			workList.remove(barr);
			if (visited.contains(barr)) {
				continue;
			} else {
				visited.add(barr);
			}
			/*
			 * Create all static phases that start at barr.
			 */
			for (Node nextBarr : barr.getInfo().getFWRBarriers()) {
				retSet.add(new YuanStaticPhase(barr, nextBarr, parCons));
				workList.add(nextBarr);
			}
		}
		return retSet;
	}

	public static void printBarrierTrees() {
		if (Program.getRoot() == null) {
			Misc.exitDueToError("Could not find any program! Exiting..");
		}
		String barrStr = "";
		int i = 1;
		for (FunctionDefinition funcDef : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
			String funcName = "";
			BTNode funcBT = funcDef.getInfo().getBTNode();
			funcName += funcDef.getInfo().getFunctionName() + ": \n";
			String funcLoc = "";
			if (funcBT != null) {
				funcLoc += funcBT.getString(1) + "\n";
			}
			for (ParallelConstruct parCons : Misc.getExactEnclosee(funcDef, ParallelConstruct.class)) {
				BTNode btNode = parCons.getInfo().getBTNode();
				if (btNode != null) {
					funcLoc += "PC #" + i++ + ": \n";
					funcLoc += btNode.getString(1) + "\n";
				}
			}
			if (!funcLoc.isEmpty()) {
				barrStr += funcName + funcLoc;
			}
		}
		System.out.println(barrStr);
		System.exit(0);
	}

	public static void verifyForMVEs() {
		if (Program.getRoot() == null) {
			Misc.exitDueToError("Could not find any program! Exiting..");
		}
		Set<Node> leaves = Program.getRoot().getInfo().getCFGInfo().getLexicalCFGContents();
		System.err.println("Total number of leaf nodes: " + leaves.size());
		int i = 0, j = 0;
		for (Node leaf : leaves) {
			if (Misc.isAPredicate(leaf)) {
				i++;
				if (leaf instanceof Expression) {
					Expression predicate = (Expression) leaf;
					if (!SVEChecker.isSingleValuedPredicate(predicate)) {
						Node parent = Misc.getEnclosingCFGNonLeafNode(predicate);
						if (parent.getInfo().hasBarrierInCFG()) {
							if (parent instanceof IfStatement || parent instanceof SwitchStatement) {
								continue;
							} else {
								j++;
								System.err.println("The problematic loop is: " + parent);
							}
						}
					}
				}
			}
		}
		System.err.println("Total number of predicates: " + i);
		System.err.println("Total number of MVE predicates that contain barriers within them: " + j);
		System.exit(0);
	}

}
