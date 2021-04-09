/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.parallel;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

public class DataFlowGraph {
	/**
	 * Populate the leaf nodes of parallel construct, <code>parConstruct</code>
	 * with the inter-task edges.
	 * 
	 * @param parConstruct
	 */
	public static void populateInterTaskEdges(Node rootNode, AbstractPhase<?, ?> phase) {
		/*
		 * Step 1: Obtain the set of all the DummyFlushDirectives.
		 */
		Set<DummyFlushDirective> nodeSet = Misc.getInheritedEnclosee(rootNode, DummyFlushDirective.class);
		Set<Node> B = phase.getNodeSet();
		Set<DummyFlushDirective> C = new HashSet<>();
		for (DummyFlushDirective obj : nodeSet) {
			if (B.contains(obj)) {
				C.add(obj);
			}
		}
		nodeSet = C;

		/*
		 * Step 2: Check and add inter-task edges for each pair in the
		 * obtained set, if needed.
		 */
		for (Node n1Node : nodeSet) {
			for (Node n2Node : nodeSet) {
				DataFlowGraph.createEdgeBetween((DummyFlushDirective) n1Node, (DummyFlushDirective) n2Node);
			}
		}
	}

	public static void createEdgeBetween(DummyFlushDirective n1Node, DummyFlushDirective n2Node) {
		// OLD CODE: Now, we allow self-loops via inter-task edges.
		// if (n1Node == n2Node) {
		// return;
		// }
		DummyFlushDirective n1 = n1Node;
		DummyFlushDirective n2 = n2Node;

		// Set<Lock> n1LockSet = new HashSet<>(n1.getInfo().getLockSet());
		// Set<Lock> n2LockSet = new HashSet<>(n2.getInfo().getLockSet());
		// Set<Lock> intersection = Misc.setIntersection(n1LockSet, n2LockSet);
		// if (intersection.isEmpty()) {
		// TODO: Use must-lock sets here.
		if (true) {
			DataFlowGraph.createInterTaskEdgeBetween(n1, n2);
		}
	}

	// /**
	// * Returns true, if the data-flow facts may flow between node n1 and n2.
	// * In such a case, we should create an inter-task data-flow edge between n1
	// * and n2.
	// * <p>
	// * As a side-effect, this node also creates edges between the locking nodes,
	// * if any, if needed.
	// *
	// * @param n1
	// * a DummyFlushDirective
	// * @param n2
	// * a DummyFlushDirective
	// * @return
	// * true if n1 and n2 should have an inter-task data-flow fact edge
	// * between them.
	// */
	// private static boolean dataFactsMayFlowBetween(DummyFlushDirective n1,
	// DummyFlushDirective n2) {
	// /**
	// * Step 1: Check if n1 and n2 are bound by a same uni-task.
	// */
	// Set<Class> classList = new HashSet<>();
	// classList.add(SingleConstruct.class);
	// classList.add(MasterConstruct.class);
	// classList.add(ASection.class);
	// classList.add(TaskConstruct.class);
	//
	// if (n1 == n2) {
	// Node n1EnclosingNode = Misc.getEnclosingNode(n1, classList);
	// if (n1EnclosingNode != null) {
	// return false;
	// }
	//
	// } else {
	// Node n1EnclosingNode = Misc.getEnclosingNode(n1, classList);
	// Node n2EnclosingNode = Misc.getEnclosingNode(n2, classList);
	//
	// if ((n1EnclosingNode != null) && (n2EnclosingNode != null) &&
	// (n1EnclosingNode == n2EnclosingNode)) {
	// return false;
	// }
	// }
	//
	// // /**
	// // * Step 2: Check if n1 and n2 share the same lock, and change the edges
	// // * accordingly..
	// // * Note that the locks are stored in the increasing order of closeness
	// // * from the locked nodes.
	// // * Hence, if there are any common lock nodes, we will create the edges
	// // * between the outermost pair.
	// // */
	// // List<Lock> n1LockSet = n1.getInfo().getLockSet();
	// // List<Lock> n2LockSet = n2.getInfo().getLockSet();
	// // for (Lock n1Lock : n1LockSet) {
	// // for (Lock n2Lock : n2LockSet) {
	// // if (n1Lock.getName().equals(n2Lock.getName())) {
	// // Node n1LockNode = n1Lock.getLockNode();
	// // Node n2LockNode = n2Lock.getLockNode();
	// // Deprecated_DataFlowGraph.createInterTaskEdgeBetween(n1LockNode,
	// n2LockNode);
	// // return false; // We do not need edges between n1 and n2 now.
	// // }
	// // }
	// // }
	//
	// return true;
	// }

	/**
	 * Creates a pair of inter-task edges between n1 and n2.
	 * This edge represents that the data-flow facts may flow in any direction
	 * between
	 * n1 and n2.
	 * 
	 * @param n1
	 * @param n2
	 */
	private static void createInterTaskEdgeBetween(DummyFlushDirective n1, DummyFlushDirective n2) {
		InterTaskEdge edgeFromN1ToN2 = new InterTaskEdge(n1, n2);
		InterTaskEdge edgeFromN2ToN1 = new InterTaskEdge(n2, n1);
		n1.getInfo().addIncomingInterTaskEdge(edgeFromN2ToN1);
		n1.getInfo().addOutgoingInterTaskEdge(edgeFromN1ToN2);

		n2.getInfo().addIncomingInterTaskEdge(edgeFromN1ToN2);
		n2.getInfo().addOutgoingInterTaskEdge(edgeFromN2ToN1);
	}
}
