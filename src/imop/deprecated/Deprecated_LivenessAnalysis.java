/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.deprecated;

import imop.baseVisitor.cfgTraversals.DepthFirstCFG;

/**
 * This class populates liveOut information for all the CFG nodes
 */
@Deprecated
public class Deprecated_LivenessAnalysis extends DepthFirstCFG {
	// @Override
	// public void initProcess(Node n) {
	// // Skip if a non-CFG node
	// if (!Misc.isCFGNode(n)) {
	// return;
	// }
	//
	// // Skip if this is not an elementary read/write node
	// if (!Misc.isElementaryRWNodeParent(n)) {
	// System.out.println("!");
	// return;
	// }
	//
	// /*
	// * Calculate liveIn information for n
	// * liveIn = (liveOut - def) U uses
	// */
	// // Get "def"
	// Set<Cell> defSet = new HashSet<>();
	// Node elemNode = Misc.getElementaryRWNode(n);
	// List<Cell> defList = elemNode.getInfo().getWrites();
	// if (defList.size() == 1) {
	// defSet = new HashSet<>(defList);
	// }
	//
	// // Get "use"
	// Set<Cell> useSet = new HashSet<>(n.getInfo().getReads());
	//
	// // Get "liveOut"
	// Set<Cell> liveOutSet = new HashSet<>();
	// HashMap<Node, Set<Cell>> liveOutMap = n.getInfo().getLiveOut();
	// for (Node keySucc : liveOutMap.keySet()) {
	// liveOutSet = Misc.setUnion(liveOutSet, liveOutMap.get(keySucc));
	// }
	//
	// // Finally, obtain "liveIn"
	// Set<Cell> liveInSet = Misc.setUnion(Misc.setMinus(liveOutSet, defSet),
	// useSet);
	//
	// /*
	// * Obtain a list of predecessors
	// */
	// Set<Node> predecessors = new HashSet<>();
	// boolean getParentNotPred = false;
	// Node parent = n.getParent();
	// if (n instanceof BeginNode) {
	// if (Misc.isElementaryRWNodeParent(parent)) {
	// getParentNotPred = true;
	// }
	// }
	//
	// if (getParentNotPred) {
	// predecessors.add(parent);
	// } else {
	// Node toBeUsed = n;
	// if (n instanceof BeginNode) {
	// toBeUsed = parent;
	// }
	// for (Node pred : toBeUsed.getInfo().getCFGInfo().getPredBlocks()) {
	// if (Misc.isCFGLeafNode(pred)) {
	// predecessors.add(pred);
	// } else {
	// EndNode endNode = pred.getInfo().getCFGInfo().getNestedCFG().getEnd();
	// predecessors.add(endNode);
	// }
	// }
	// }
	//
	// /*
	// * Visit the predecessors
	// */
	// for (Node pred : predecessors) {
	// /*
	// * Check if this is firstVisit, by checking if n is present in the
	// * key of pred.liveOut
	// * If this is the firstVisit, create an entry for n in pred.liveOut
	// */
	// HashMap<Node, Set<Symbol>> predLiveOut = pred.getInfo().getLiveOut();
	// boolean firstVisit = false;
	// if (!predLiveOut.containsKey(n)) {
	// firstVisit = true;
	// predLiveOut.put(n, new HashSet<>());
	// }
	//
	// /*
	// * Check if we need to process pred or not.
	// * We need not process pred if:
	// * a.) If no new information is sent from n.liveIn to
	// * pred.liveOut[n]
	// * AND b.) firstVisit is false.
	// * If we need not process pred, then continue;
	// */
	// // Check if n.liveIn has some element not in pred.liveOut[n]
	// Set<Cell> thisLivenessOfPred = predLiveOut.get(n);
	// Set<Cell> newInformation = Misc.setMinus(liveInSet, thisLivenessOfPred);
	// if (newInformation.isEmpty() && firstVisit == false) {
	// continue;
	// }
	//
	// /*
	// * Process pred:
	// * pred.liveOut[n] U= n.liveIn
	// * visit pred;
	// */
	// predLiveOut.put(n, Misc.setUnion(thisLivenessOfPred, liveInSet));
	// pred.accept(this);
	// } // end of for
	//
	// return;
	// }
}
