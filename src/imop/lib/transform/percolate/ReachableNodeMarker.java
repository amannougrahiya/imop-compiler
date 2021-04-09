/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.percolate;

import imop.ast.info.cfgNodeInfo.FunctionDefinitionInfo;
import imop.ast.node.external.*;
import imop.baseVisitor.cfgTraversals.DepthFirstCFG;
import imop.lib.cg.CallSite;
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

/**
 * Given a rootBarrier, this visitor stores all the nodes that are reachable
 * on barrier-free paths from the rootBarrier.
 * If the node is a leaf node, we add it to the reachable set.
 * If the node is a non-leaf node, and it is completely within the barrier-free
 * path,
 * then we add it to the reachable set. Furthermore, we also add all its CFG
 * constituents to the reachable set.
 * However, if the node is a non-leaf node, and it is partially on the
 * barrier-free path,
 * we add only that portion of it to the reachable set which is on the
 * barrier-free path.
 * 
 */
public class ReachableNodeMarker extends DepthFirstCFG {
	public BarrierDirective rootBarrier; // The barrier, from which the reachability has to be marked.
	public ParallelConstruct ownerParallelConstruct; // The parallel construct that is under processing.
	public Set<Node> reachableNodes; // Set of reachable nodes. Persists across different visits from a barrier.
	public Set<Node> visitedNodes = new HashSet<>(); // Set of nodes that have been traversed already.

	public ReachableNodeMarker(BarrierDirective rootBarrier, ParallelConstruct ownerParallelConstruct,
			Set<Node> reachableNodes) {
		this.rootBarrier = rootBarrier;
		this.ownerParallelConstruct = ownerParallelConstruct;
		this.reachableNodes = reachableNodes;
	}

	@Override
	public void initProcess(Node n) {
		if (!Misc.isCFGNode(n)) {
			System.out.println("Check the code. We shouldn't be processing a non-CFG node at line #"
					+ Misc.getLineNum(n) + " in ReachableNodeMarker.");
			System.exit(1);
		}

		if (visitedNodes.contains(n)) {
			return;
		} else {
			visitedNodes.add(n);
		}

		/*
		 * If this node has already been added to the reachability set,
		 * we need to stop visiting this path further.
		 * Note that this is a different check from that of the visitedNodes,
		 * since the barrier-containing non-leaf nodes won't be added to this
		 * set ever.
		 */
		if (reachableNodes.contains(n)) {
			// Note we can't add this visited node as a reachable node straight away.
			return;
		}

		if (Misc.isCFGLeafNode(n)) {
			/*
			 * Get a list of call-sites, if any, in this node.
			 * If the called functions contain a barrier, we should stop marking
			 * at this node.
			 */
			for (CallSite callSite : n.getInfo().getCallSites()) {
				if (callSite.calleeDefinition != null) {
					FunctionDefinitionInfo calleeInfo = callSite.calleeDefinition.getInfo();
					if (calleeInfo.hasBarrierInCFG()) {
						return;
					}
				}
			}

			/*
			 * if we reach this point, then it implies that there are no
			 * barriers in or within this leaf node.
			 * so we mark and move.
			 */
			reachableNodes.add(n);
			visitSuccessors(n);
		} else {
			boolean hasBreakers = false;
			/*
			 * If the functions called in this node contain a barrier, we should
			 * stop marking at this node,
			 * and, instead, we should mark within this node.
			 * Similar case applies if this node contains a jump statement.
			 */
			for (CallSite callSite : n.getInfo().getCallSites()) {
				if (callSite.calleeDefinition != null) {
					FunctionDefinitionInfo calleeInfo = callSite.calleeDefinition.getInfo();
					if (calleeInfo.hasBarrierInCFG()) {
						hasBreakers = true;
						break;
					}
				}
			}

			/*
			 * Similar case, as above, applies if this node contains:
			 * 1. a jump statement with destination outside $n$.
			 * 2. a jump destination from outside $n$.
			 */
			if (!n.getInfo().deprecated_isControlConfined()) {
				hasBreakers = true;
			}

			if (hasBreakers) {
				// Do not add this node to the set of reachableNodes.
				// Instead, try marking its constituents.
				n.getInfo().getCFGInfo().getNestedCFG().getBegin().accept(this);
				return;
			} else {
				reachableNodes.add(n);
				// Now, without any checks for barriers, mark all the constituent CFG nodes in
				// $n$
				for (Node constituent : n.getInfo().getCFGInfo().getLexicalCFGContents()) {
					reachableNodes.add(constituent);
				}

				// Now, visit all the successors of this node.
				visitSuccessors(n);
			}
		}

		return;
	}

	/**
	 * Visits the CFG successors of the node.
	 * 
	 * @param n
	 */
	void visitSuccessors(Node n) {
		/*
		 * If this is a jump node, we will not be bringing its successors
		 * up towards this location. So we can assume that the successors
		 * cannot be reached from this barrier, and terminate traversal along
		 * such edges.
		 */
		if (Misc.isJumpNode(n)) {
			reachableNodes.remove(n);
			// TODO: Later, to be more precise, we can see how to handle motion of jump
			// nodes across barriers.
			// However, that might not be beneficial.
			return;
		}

		/*
		 * getSuccessors() makes sure that the successors of the parent node of
		 * an EndNode
		 * are visited as well, to mark them as reachable, if needed.
		 * (This eventually calls for code motion across the nested blocks.)
		 */
		for (Node succ : n.getInfo().getCFGInfo().getSuccessors()) {
			succ.accept(this);
		}
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(BarrierDirective n) {
		// The path ends here. This node is not reachable.
		// No further visits are needed.
	}

}
