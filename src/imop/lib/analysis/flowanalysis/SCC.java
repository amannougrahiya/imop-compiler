/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis;

import imop.ast.info.cfgNodeInfo.BarrierDirectiveInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.AbstractPhasePointable;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cfg.parallel.InterTaskEdge;
import imop.lib.util.TraversalOrderObtainer;
import imop.parser.Program;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class SCC implements DFable {
	private static int counter = 0;
	public final int id;

	private Set<Node> nodes = null;
	private Set<Node> entryNodes = null;
	private Set<Node> exitNodes = null;
	private Set<DFable> dataFlowPredecessors;
	private Set<DFable> dataFlowSuccessors;
	private int reversePostOrderId = -1;
	private boolean internalRPOStabilized = false;
	public static long SCCTimer = 0;
	public static int tarjanCount = 0;
	public static boolean processingTarjan = false; // Used to ensure that no IDFA stabilization are invoked during
													// application of Tarjan's algorithm.

	private static Stack<Node> internalStack;
	private static int tarjanI;

	private static Set<SCC> allSCCs = new HashSet<>();
	private static Set<Node> allSCCNodes = new HashSet<>();

	public SCC(Set<Node> nodes) {
		// We allow generation of only those SCCs that have at least two nodes within
		// them.
		assert (nodes != null && nodes.size() >= 2);
		id = counter++;
		this.nodes = nodes;
		for (Node n : nodes) {
			n.getInfo().getCFGInfo().setSCC(this);
		}
		allSCCs.add(this);
		// tempSum += this.nodes.size();
		// System.out.println("Creating an SCC of size " + this.nodes.size());
	}

	@Override
	public int getReversePostOrder() {
		// Program.stabilizeReversePostOrderOfLeaves();
		if (CFGInfo.isSCCStale) {
			SCC.initializeSCC();
		}
		return reversePostOrderId;
	}

	@Override
	public void setReversePostOrder(int reversePostOrderId) {
		this.reversePostOrderId = reversePostOrderId;
	}

	public Set<Node> getNodes() {
		return this.nodes;
	}

	public Set<Node> getEntryNodes() {
		if (entryNodes == null) {
			entryNodes = new HashSet<>();
			for (Node n : nodes) {
				for (Node predOfN : SCC.getInterTaskLeafPredecessorNodes(n, SVEDimension.SVE_INSENSITIVE)) {
					if (predOfN.getInfo().getCFGInfo().getSCC() != this) {
						entryNodes.add(n);
						break;
					}
				}
			}
		}
		return entryNodes;
	}

	public Set<Node> getExitNodes() {
		if (exitNodes == null) {
			exitNodes = new HashSet<>();
			for (Node n : nodes) {
				for (Node succOfN : SCC.getInterTaskLeafSuccessorNodes(n, SVEDimension.SVE_INSENSITIVE)) {
					if (succOfN.getInfo().getCFGInfo().getSCC() != this) {
						exitNodes.add(n);
						break;
					}
				}
			}
		}
		return exitNodes;
	}

	@Override
	public Set<DFable> getDataFlowPredecessors() {
		if (this.dataFlowPredecessors == null) {
			this.dataFlowPredecessors = new HashSet<>();
			for (Node entryNode : this.getEntryNodes()) {
				for (Node predOfEntry : SCC.getInterTaskLeafPredecessorNodes(entryNode, SVEDimension.SVE_INSENSITIVE)) {
					SCC predSCC = predOfEntry.getInfo().getCFGInfo().getSCC();
					if (predSCC == null) {
						this.dataFlowPredecessors.add(predOfEntry);
					} else {
						this.dataFlowPredecessors.add(predSCC);
					}
				}
			}
		}
		return this.dataFlowPredecessors;
	}

	@Override
	public Set<DFable> getDataFlowSuccessors() {
		if (this.dataFlowSuccessors == null) {
			this.dataFlowSuccessors = new HashSet<>();
			for (Node exitNode : this.getExitNodes()) {
				for (Node succOfExit : SCC.getInterTaskLeafSuccessorNodes(exitNode, SVEDimension.SVE_INSENSITIVE)) {
					SCC succSCC = succOfExit.getInfo().getCFGInfo().getSCC();
					if (succSCC == null) {
						this.dataFlowSuccessors.add(succOfExit);
					} else {
						this.dataFlowSuccessors.add(succSCC);
					}
				}
			}
		}
		return this.dataFlowSuccessors;
	}

	public void stabilizeInternalRPO() {
		if (this.internalRPOStabilized) {
			return;
		}
		this.internalRPOStabilized = true;
		Node startNode = null;
		for (Node entry : this.getEntryNodes()) {
			startNode = entry;
			break;
		}
		if (startNode == null) {
			assert (false) : "We have found an SCC with no entry points!";
		}
		Set<Node> exitNodes = this.getExitNodes();
		List<Node> rpoList = TraversalOrderObtainer.obtainReversePostOrder(startNode, n -> {
			if (!exitNodes.contains(n)) {
				return SCC.getInterTaskLeafSuccessorNodes(n, SVEDimension.SVE_INSENSITIVE);
			} else {
				Set<Node> succList = new HashSet<>();
				for (Node succ : SCC.getInterTaskLeafSuccessorNodes(n, SVEDimension.SVE_INSENSITIVE)) {
					if (succ.getInfo().getCFGInfo().getSCC() == this) {
						// Add only those successors which are within the same SCC.
						succList.add(succ);
					}
				}
				return succList;
			}
		});
		int i = 0;
		for (Node n : this.entryNodes) {
			n.setReversePostOrder(i++);
		}
		for (Node n : rpoList) {
			if (!this.entryNodes.contains(n)) {
				assert (rpoList.contains(n));
				n.setReversePostOrder(i++);
			}
		}
	}

	public static void initializeSCC() {
		// TODO: Check whether the next line should be here (risking incorrect recursive
		// radings until the end of this method),
		// or whether it should be at the end (risking infinite recursive calls of this
		// method from appropriate getters)?
		CFGInfo.isSCCStale = false;
		SCC.internalStack = new Stack<>();
		SCC.tarjanI = 0;
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main == null) {
			return;
		}
		long timer = System.nanoTime();
		// Before starting with the Tarjan's algorithm, reset the DFS index of each CFG
		// node in the program.
		for (SCC old : allSCCs) {
			flushSCC(old);
		}
		for (Node n : allSCCNodes) {
			n.setReversePostOrder(-1);
			n.getInfo().getCFGInfo().setDFSIndex(-1);
			n.getInfo().getCFGInfo().setLowLink(-1);
		}
		allSCCs.clear();
		allSCCNodes.clear();
		// for (Node leaf :
		// Program.getRoot().getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
		// leaf.getInfo().getCFGInfo().setDFSIndex(-1);
		// }
		// Now, invoke the Tarjan's algorithm.
		BeginNode begin = main.getInfo().getCFGInfo().getNestedCFG().getBegin();
		Set<Node> visitedSet = new HashSet<>();
		SCC.processingTarjan = true;
		tarjan(begin, visitedSet);
		SCC.processingTarjan = false;
		SCCTimer += System.nanoTime() - timer;
		SCC.tarjanCount++;
		assert (SCC.internalStack.isEmpty())
				: "The stack of nodes is not empty at the end of Tarjan's algorithm! Its contents are:\n"
						+ SCC.internalStack;
		List<DFable> reversePostOrder = TraversalOrderObtainer.obtainReversePostOrder(begin,
				DFable::getDataFlowSuccessors);
		int i = 0;
		for (DFable dfN : reversePostOrder) {
			dfN.setReversePostOrder(i++);
		}
		// System.out.println("Visited a total of " + visitedSet.size());
		// System.out.println("Cumulative size so far: " + tempSum);
		// System.out.println("We still have " + SCC.internalStack.size() + " nodes on
		// the stack!");
	}

	private static void flushSCC(SCC oldSCC) {
		for (Node n : oldSCC.nodes) {
			n.getInfo().getCFGInfo().setSCC(null);
			n.setReversePostOrder(-1);
			n.getInfo().getCFGInfo().setDFSIndex(-1);
		}
	}

	private static void tarjan(Node v, Set<Node> visited) {
		if (visited.contains(v)) {
			return;
		} else {
			visited.add(v);
		}
		CFGInfo vInfo = v.getInfo().getCFGInfo();
		vInfo.setDFSIndex(SCC.tarjanI);
		vInfo.setLowLink(SCC.tarjanI);
		SCC.tarjanI++;
		SCC.internalStack.push(v);
		vInfo.setOnInternalStack(true);

		Set<Node> neighbours;
		if (v instanceof BarrierDirective) {
			BarrierDirective barrier = (BarrierDirective) ((BarrierDirectiveInfo) v.getInfo()).getNode();
			neighbours = new HashSet<>(SCC.getInterTaskLeafSuccessorNodes(v, SVEDimension.SVE_INSENSITIVE));
			for (AbstractPhase<?, ?> ph : new HashSet<>(barrier.getInfo().getNodePhaseInfo().getPhaseSet())) {
				boolean found = false;
				for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
					if (endingPhasePoint.getNodeFromInterface() == barrier) {
						found = true;
						break;
					}
				}
				if (!found) {
					continue;
				}
				for (AbstractPhasePointable endingPhasePoint : ph.getEndPoints()) {
					if (!(endingPhasePoint.getNodeFromInterface() instanceof BarrierDirective)) {
						continue;
					}
					BarrierDirective siblingBarrier = (BarrierDirective) endingPhasePoint.getNodeFromInterface();
					if (siblingBarrier == barrier) {
						continue;
					}
					neighbours.add(siblingBarrier);
				}
			}
		} else {
			neighbours = SCC.getInterTaskLeafSuccessorNodes(v, SVEDimension.SVE_INSENSITIVE);
		}
		for (Node w : neighbours) {
			CFGInfo wInfo = w.getInfo().getCFGInfo();
			// if (wInfo.getDFSIndex() == -1) {
			if (!visited.contains(w)) {
				tarjan(w, visited);
				vInfo.setLowLink(Math.min(vInfo.getLowLink(), wInfo.getLowLink()));
				// } else if (SCC.internalStack.contains(w)) {
			} else if (wInfo.isOnInternalStack()) {
				vInfo.setLowLink(Math.min(vInfo.getLowLink(), wInfo.getDFSIndex()));
			}
		}
		if (vInfo.getDFSIndex() == vInfo.getLowLink()) {
			// Create a new SCC here, with v as its root.
			Set<Node> contentsOfNextSCC = new HashSet<>();
			while (true) {
				if (SCC.internalStack.isEmpty()) {
					break;
				}
				Node top = SCC.internalStack.pop();
				top.getInfo().getCFGInfo().setOnInternalStack(false);
				contentsOfNextSCC.add(top);
				if (top == v) {
					break;
				}
			}
			if (contentsOfNextSCC.size() != 1) {
				new SCC(contentsOfNextSCC);
			} else {
				vInfo.setSCC(null); // This explicit setting is needed, to remove any stale information.
				allSCCNodes.add(v);
			}
		}
	}

	public static Set<Node> getInterTaskLeafSuccessorNodes(Node node, SVEDimension sveDimension) {
		Set<Node> returnSet = new HashSet<>(node.getInfo().getCFGInfo().getInterProceduralLeafSuccessors());
		if (node instanceof DummyFlushDirective) {
			for (InterTaskEdge interTaskEdge : ((DummyFlushDirective) node).getInfo().getOutgoingInterTaskEdges()) {
				returnSet.add(interTaskEdge.getDestinationNode());
			}
		}
		return returnSet;
	}

	public static Set<Node> getInterTaskLeafPredecessorNodes(Node node, SVEDimension sveDimension) {
		Set<Node> returnSet = new HashSet<>(node.getInfo().getCFGInfo().getInterProceduralLeafPredecessors());
		if (node instanceof DummyFlushDirective) {
			for (InterTaskEdge interTaskEdge : ((DummyFlushDirective) node).getInfo().getIncomingInterTaskEdges()) {
				returnSet.add(interTaskEdge.getSourceNode());
			}
		}
		return returnSet;
	}
}
