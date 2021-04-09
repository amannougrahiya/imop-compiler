/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.percolate;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.cfgTraversals.DepthFirstCFG;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

/**
 * This visitor traverses the CFG nodes, on the barrier-free paths,
 * and adds all those nodes which are movable above the root barrier
 * to the set $movableNodes$.
 */
public class MovableNodeMarker extends DepthFirstCFG {
	public BarrierDirective rootBarrier;
	public Set<Node> movableNodes; // set of reachable nodes that are movable
	public Set<Node> nonMovableNodes; // set of reachable nodes that are not movable
	public Set<Node> reachableNodes;
	public Set<Node> visitedNodes = new HashSet<>();

	public MovableNodeMarker(BarrierDirective rootBarrier, Set<Node> movableNodes, Set<Node> nonMovableNodes,
			Set<Node> reachableNodes) {
		this.rootBarrier = rootBarrier;
		this.movableNodes = movableNodes;
		this.nonMovableNodes = nonMovableNodes;
		this.reachableNodes = reachableNodes;
	}

	/**
	 * Adds nodes to movableNodes and nonMovableNodes.
	 * Makes sure that no node is processed more than once,
	 * for a given receiver.
	 */
	@Override
	public void initProcess(Node n) {
		/*
		 * If this process has already returned once for $n$,
		 * then we can return using check #1 below.
		 * If this process has been called again recursively on $n$,
		 * without finishing even once on $n$,
		 * check #2 makes sure we terminate.
		 * This case corresponds to a cyclic dependency of two statements that
		 * need to be moved across the barrier.
		 * In such a case, we should mark both as movable, if all other
		 * constraints are
		 * satisfied.
		 * In check #3, we do the actual marking of nodes as movable/nonMovable.
		 */

		// Check #1
		if (movableNodes.contains(n) || nonMovableNodes.contains(n)) {
			return;
		}

		// Check #2
		if (visitedNodes.contains(n)) {
			return;
		} else {
			visitedNodes.add(n);
		}

		// Check #3
		if (this.isNodeMovable(n)) {
			movableNodes.add(n);
			// Note that the constituents of the non-leaf nodes aren't marked if the
			// non-leaf node itself has been marked.
		} else {
			nonMovableNodes.add(n);
		}

		visitSuccessors(n);
	}

	/**
	 * Visits successors of a given node, if the node is not a jump statement
	 * -- return, break, continue, or goto.
	 */
	public void visitSuccessors(Node n) {
		if (Misc.isJumpNode(n)) {
			return;
		}
		for (Node succ : n.getInfo().getCFGInfo().getSuccessors()) {
			/*
			 * Note: getSuccessors() ensures that the successors of the parent
			 * of an EndNode are visited as well.
			 */
			succ.accept(this);
		}
	}

	/**
	 * Mark the node as movable, and return true, if the following hold true:
	 * 1. This node lies below the barrier in source code.
	 * 2. This node's reads and writes do not interfere with those in the phases
	 * above the barrier.
	 * 3. This node is reachable from the barrier.
	 * 4. All the nodes on which this node depend are also movable.
	 * 
	 * @param n
	 * @return
	 */
	public boolean isNodeMovable(Node n) {
		if (n instanceof BeginNode || n instanceof EndNode) {
			return true;
		}

		if (!this.isNodeBelowBarrier(n)) {
			return false;
		}

		if (!Phase.isNodeAllowedInNewPhasesAbove(n, this.rootBarrier)) {
			return false;
		}

		if (Misc.isCFGLeafNode(n)) {
			if (!this.isNodeReachable(n)) {
				return false;
			}
		} else {
			if (!this.isNodeReachable(n)) {
				// Next line will take care of internal markings.
				n.getInfo().getCFGInfo().getNestedCFG().getBegin().accept(this);
				return false;
			}
		}

		/*
		 * In the next line, we just check whether the dependees (those on which
		 * $n$ depends)
		 * are movable across this barrier or not. That decision will affect the
		 * movability of
		 * $n$, but we don't consider this fact here, since eventually at the
		 * time of code motion,
		 * $n$ won't be able to move past its dependees, due to the
		 * dependence(s).
		 */
		//
		Set<Node> nodesInTheChop;
		Set<Node> dependentNodes = Misc.getAllDataDependees(n);
		nodesInTheChop = Misc.setIntersection(dependentNodes, reachableNodes);

		for (Node dependent : nodesInTheChop) {
			dependent.accept(this);
		}

		return true;
	}

	public boolean isNodeReachable(Node n) {
		return reachableNodes.contains(n);
	}

	public boolean isNodeBelowBarrier(Node n) {
		if (Misc.getLineNum(n) < Misc.getLineNum(rootBarrier)) {
			return false;
		}
		return true;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(BarrierDirective n) {
		/*
		 * And here ends the barrier-free path.
		 * None shall pass to the successors of $n$.
		 */
	}

}
