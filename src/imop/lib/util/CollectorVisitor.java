/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class CollectorVisitor {

	@FunctionalInterface
	public static interface NeighbourSetGetter<T> {
		public Set<T> getImmediateNeighbours(T currentNode);
	}

	@FunctionalInterface
	public static interface NeighbourListGetter<T> {
		public List<T> getImmediateNeighbours(T currentNode);
	}

	/**
	 * A generic method that is used to traverse a graph until certain
	 * conditions are met, and collect the list of nodes that lie on the
	 * traversed path, as well as nodes where the paths terminate.
	 * 
	 * <p>
	 * Note that if a <i>list</i> of nodes is not required, then we should use
	 * the method
	 * {@link CollectorVisitor#collectNodeSetInGenericGraph(Object, Set, Predicate, NeighbourSetGetter)
	 * collectNodeSetInCustorGraph} instead.
	 * 
	 * @param startPoint
	 *                         a node of type {@code T}, starting whose successors
	 *                         the
	 *                         traversals have to start. Note that the
	 *                         {@terminationCheck} is
	 *                         not carried out on this node, unless any traversed
	 *                         paths
	 *                         pass through it.
	 * @param endPoints
	 *                         list of nodes where traversal ends. Note that these
	 *                         nodes
	 *                         satisfy the predicate {@code terminationCheck}. This
	 *                         field is
	 *                         used as one of the outputs.
	 * @param terminationCheck
	 *                         a predicate which determines if traversal should
	 *                         terminate --
	 *                         if the predicate returns true on a node, then the
	 *                         traversal
	 *                         terminates at that node.
	 * @param nextLayerFinder
	 *                         a functional interface that takes a node and returns
	 *                         a list of
	 *                         successors for the node.
	 * @return
	 *         a list of nodes which are encountered in the traversal of the
	 *         graph, starting with the successors of the node
	 *         {@code startPoint}, and ending on the nodes which do not satisfy
	 *         the {@code termintationCheck}.
	 *         Note that node {@code startPoint} and {@code endPoints} are
	 *         not part of the return, unless any traversed paths pass through
	 *         them.
	 */
	public static <T> List<T> collectNodeListInGenericGraph(T startPoint, List<T> endPoints,
			Predicate<T> terminationCheck, NeighbourListGetter<T> nextLayerFinder) {
		List<T> startPoints = new LinkedList<>();
		startPoints.add(startPoint);
		return collectNodeListInGenericGraph(startPoints, endPoints, terminationCheck, nextLayerFinder);
	}

	/**
	 * A generic method that is used to traverse a graph until certain
	 * conditions are met, and collect the list of nodes that lie on the
	 * traversed path, as well as nodes where the paths terminate.
	 * 
	 * <p>
	 * Note that if a <i>list</i> of nodes is not required, then we should use
	 * the method
	 * {@link CollectorVisitor#collectNodeSetInGenericGraph(Set, Set, Predicate, NeighbourSetGetter)
	 * collectNodeSetInCustorGraph} instead.
	 * 
	 * @param startPoints
	 *                         list of nodes of type {@code T}, starting whose
	 *                         successors the
	 *                         traversals have to start. Note that the
	 *                         {@terminationCheck} is
	 *                         not carried out on these nodes, unless any traversed
	 *                         paths
	 *                         pass through them.
	 * @param endPoints
	 *                         list of nodes where traversal ends. Note that these
	 *                         nodes
	 *                         satisfy the predicate {@code terminationCheck}. This
	 *                         field is
	 *                         used as one of the outputs.
	 * @param terminationCheck
	 *                         a predicate which determines if traversal should
	 *                         terminate --
	 *                         if the predicate returns true on a node, then the
	 *                         traversal
	 *                         terminates at that node.
	 * @param nextLayerFinder
	 *                         a functional interface that takes a node and returns
	 *                         a list of
	 *                         successors for the node.
	 * @return
	 *         a list of nodes which are encountered in the traversal of the
	 *         graph, starting with the successors of the nodes in
	 *         {@code startPoints},
	 *         and ending on the nodes which do not satisfy the
	 *         {@code termintationCheck}.
	 *         Note that nodes in {@code startPoints} and {@code endPoints} are
	 *         not part of the return, unless any traversed paths pass through
	 *         them.
	 */
	public static <T> List<T> collectNodeListInGenericGraph(List<T> startPoints, List<T> endPoints,
			Predicate<T> terminationCheck, NeighbourListGetter<T> nextLayerFinder) {
		List<T> collectedNodeList = new LinkedList<>();
		List<T> workList = new LinkedList<>(startPoints);

		while (!workList.isEmpty()) {
			T currentNode = workList.get(0);
			workList.remove(0);
			for (T succNode : nextLayerFinder.getImmediateNeighbours(currentNode)) {
				if (!terminationCheck.test(succNode)) {
					if (!collectedNodeList.contains(succNode)) {
						collectedNodeList.add(succNode);
						workList.add(succNode);
					}
				} else {
					if (endPoints != null) {
						endPoints.add(succNode);
					}
				}
			}
		}
		return collectedNodeList;
	}

	/**
	 * A generic method that is used to traverse a graph until certain
	 * conditions are met, and collect the set of nodes that lie on the
	 * traversed path, as well as nodes where the paths terminate.
	 * 
	 * @param startPoint
	 *                         a node of type {@code T}, starting whose successors
	 *                         the
	 *                         traversals have to start. Note that the
	 *                         {@code terminationCheck} is
	 *                         not carried out on this node, unless any traversed
	 *                         paths
	 *                         pass through it.
	 * @param endPoints
	 *                         set of nodes where the traversals end. Note that
	 *                         these nodes
	 *                         satisfy the predicate {@code terminationCheck}. This
	 *                         field is
	 *                         used as one of the outputs.
	 * @param terminationCheck
	 *                         a predicate which determines if traversal should
	 *                         terminate --
	 *                         if the predicate returns true on a node, then the
	 *                         traversal
	 *                         terminates at that node.
	 * @param nextLayerFinder
	 *                         a functional interface that takes a node and returns
	 *                         a set of
	 *                         successors for the node.
	 * @return
	 *         a set of nodes which are encountered in the traversal of the
	 *         graph, starting with the successors of the node
	 *         {@code startPoint},
	 *         and ending on the nodes which do not satisfy the
	 *         {@code termintationCheck}.
	 *         Note that node {@code startPoint} and {@code endPoints} are
	 *         not part of the return, unless any traversed paths pass through
	 *         them.
	 */
	public static <T> Set<T> collectNodeSetInGenericGraph(T startPoint, Set<T> endPoints, Predicate<T> terminationCheck,
			NeighbourSetGetter<T> nextLayerFinder) {
		Set<T> startPoints = new HashSet<>();
		startPoints.add(startPoint);
		return collectNodeSetInGenericGraph(startPoints, endPoints, terminationCheck, nextLayerFinder);
	}

	/**
	 * A generic method that is used to traverse a graph until certain
	 * conditions are met, and collect the set of nodes that lie on the
	 * traversed path, as well as nodes where the paths terminate.
	 * 
	 * @param startPoints
	 *                         set of nodes of type {@code T}, starting whose
	 *                         successors the
	 *                         traversals have to start. Note that the
	 *                         {@terminationCheck} is
	 *                         not carried out on these nodes, unless any traversed
	 *                         paths
	 *                         pass through them.
	 * @param endPoints
	 *                         set of nodes where traversals end. Note that these
	 *                         nodes
	 *                         satisfy the predicate {@code terminationCheck}. This
	 *                         field is
	 *                         used as one of the outputs.
	 * @param terminationCheck
	 *                         a predicate which determines if traversal should
	 *                         terminate --
	 *                         if the predicate returns true on a node, then the
	 *                         traversal
	 *                         terminates at that node.
	 * @param nextLayerFinder
	 *                         a functional interface that takes a node and returns
	 *                         a set of
	 *                         successors for the node.
	 * @return
	 *         a set of nodes which are encountered in the traversal of the
	 *         graph, starting with the successors of the nodes in
	 *         {@code startPoints},
	 *         and ending on the nodes which do not satisfy the
	 *         {@code termintationCheck}.
	 *         Note that nodes in {@code startPoints} and {@code endPoints} are
	 *         not part of the return, unless any traversed paths pass through
	 *         them.
	 */
	public static <T> Set<T> collectNodeSetInGenericGraph(Set<T> startPoints, Set<T> endPoints,
			Predicate<T> terminationCheck, NeighbourSetGetter<T> nextLayerFinder) {
		Set<T> collectedNodeSet = new HashSet<>();
		Set<T> workSet = new HashSet<>(startPoints);

		while (!workSet.isEmpty()) {
			T currentNode = Misc.getAnyElement(workSet);
			workSet.remove(currentNode);
			for (T succNode : nextLayerFinder.getImmediateNeighbours(currentNode)) {
				if (!terminationCheck.test(succNode)) {
					if (!collectedNodeSet.contains(succNode)) {
						collectedNodeSet.add(succNode);
						workSet.add(succNode);
					}
				} else {
					if (endPoints != null) {
						endPoints.add(succNode);
					}
				}
			}
		}
		return collectedNodeSet;
	}

	/**
	 * This visitor traverses forward on all the intra-task inter-procedural
	 * context-insensitive paths that start at {@code startPoint} and end at
	 * those nodes where the {@code terminationCheck} succeeds.
	 * <p>
	 * Note that when a traversal root is a non-leaf CFG node, the traversal
	 * would start with the successors of that non-leaf node, rather than those
	 * of its {@link BeginNode}.
	 * 
	 * @param startPoint
	 *                         node from which traversal has to start.
	 * @param endPoints
	 *                         set of nodes at which the traversal ends. This set is
	 *                         populated by this method call, and acts as one of the
	 *                         outputs.
	 * @param terminationCheck
	 *                         a functional interface, which is used to specify the
	 *                         condition
	 *                         for termination of the CFG traversal.
	 * @return
	 *         set of nodes that lie on any of the paths between
	 *         {@code startPoint} and {@code endPoints}, exclusively.
	 */
	public static Set<Node> collectNodesIntraTaskForward(Node startPoint, Set<Node> endPoints,
			Predicate<Node> terminationCheck) {
		Set<Node> startPoints = new HashSet<>();
		startPoints.add(startPoint);
		return collectNodesIntraTaskForward(startPoints, endPoints, terminationCheck);
	}

	/**
	 * This visitor traverses forwards on all the intra-task inter-procedural
	 * context-insensitive paths that start at any element in the
	 * {@code startPoint} and end at those nodes where the
	 * {@code terminationCheck} succeeds.
	 * <p>
	 * Note that when a traversal root is a non-leaf CFG node, the traversal
	 * would start with the successors of that non-leaf node, rather than those
	 * of its {@link BeginNode}.
	 * 
	 * @param startPoints
	 *                         set of nodes from which traversal has to start.
	 * @param endPoints
	 *                         set of nodes at which the traversal ends. This set is
	 *                         populated by this method call, and acts as one of the
	 *                         outputs.
	 * @param terminationCheck
	 *                         a functional interface, which is used to specify the
	 *                         condition
	 *                         for termination of the CFG traversal.
	 * @return
	 *         set of nodes that lie on any of the paths between any element of
	 *         {@code startPoint} and {@code endPoints}, exclusively.
	 */
	public static Set<Node> collectNodesIntraTaskForward(Set<Node> startPoints, Set<Node> endPoints,
			Predicate<Node> terminationCheck) {
		Set<NodeWithStack> stackedStartingPoints = new HashSet<>();
		Set<NodeWithStack> stackedEndingPoints = new HashSet<>();
		Predicate<NodeWithStack> stackedTerminationCheck = (n) -> terminationCheck.test(n.getNode());
		for (Node startPoint : startPoints) {
			stackedStartingPoints.add(new NodeWithStack(startPoint, new CallStack()));
		}
		Set<NodeWithStack> stackedCollectedNodes = collectNodeSetInGenericGraph(stackedStartingPoints,
				stackedEndingPoints, stackedTerminationCheck,
				(n) -> n.getNode().getInfo().getCFGInfo().getInterProceduralLeafSuccessors(n.getCallStack()));

		for (NodeWithStack nodeWithStack : stackedEndingPoints) {
			endPoints.add(nodeWithStack.getNode());
		}

		Set<Node> collectedNodes = new HashSet<>();
		for (NodeWithStack nodeWithStack : stackedCollectedNodes) {
			collectedNodes.add(nodeWithStack.getNode());
		}
		return collectedNodes;
		// return collectNodesInCustomGraph(startPoints, endPoints, terminationCheck,
		// (Node n) -> n.getInfo().getCFGInfo().getInterProceduralLeafSuccessors());
		// Set<Node> collectedNodeSet = new HashSet<>();
		// Set<Node> workSet = new HashSet<>(startPoints);
		//
		// while (workSet.size() != 0) {
		// Node currentNode = Misc.getAnyElement(workSet);
		// workSet.remove(currentNode);
		// for (Node succNode :
		// currentNode.getInfo().getCFGInfo().getInterProceduralLeafSuccessors()) {
		// if (!terminationCheck.test(succNode)) {
		// if (!collectedNodeSet.contains(succNode)) {
		// collectedNodeSet.add(succNode);
		// workSet.add(succNode);
		// }
		// } else {
		// endPoints.add(succNode);
		// }
		// }
		// }
		// return collectedNodeSet;
	}

	/**
	 * This visitor traverses backward on all the intra-task inter-procedural
	 * context-insensitive paths that start at {@code startPoint} and end at
	 * those nodes where the {@code terminationCheck} succeeds.
	 * <p>
	 * Note that when a traversal root is a non-leaf CFG node, the traversal
	 * would start with the predecessors of that non-leaf node, rather than
	 * those of its {@link EndNode}.
	 * 
	 * @param startPoint
	 *                         node from which traversal has to start.
	 * @param endPoints
	 *                         set of nodes at which the traversal ends. This set is
	 *                         populated by this method call, and acts as one of the
	 *                         outputs.
	 * @param terminationCheck
	 *                         a functional interface, which is used to specify the
	 *                         condition
	 *                         for termination of the CFG traversal.
	 * @return
	 *         set of nodes that lie on any of the paths between
	 *         {@code startPoint} and {@code endPoints}, exclusively.
	 */
	public static Set<Node> collectNodesIntraTaskBackward(Node startPoint, Set<Node> endPoints,
			Predicate<Node> terminationCheck) {
		Set<Node> startPoints = new HashSet<>();
		startPoints.add(startPoint);
		return collectNodesIntraTaskBackward(startPoints, endPoints, terminationCheck);
	}

	/**
	 * This visitor traverses backwards on all the intra-task inter-procedural
	 * context-insensitive paths that start at any element in the
	 * {@code startPoint} and end at those nodes where the
	 * {@code terminationCheck} succeeds.
	 * <p>
	 * Note that when a traversal root is a non-leaf CFG node, the traversal
	 * would start with the predecessors of that non-leaf node, rather than
	 * those of its {@link EndNode}.
	 * 
	 * @param startPoints
	 *                         set of nodes from which traversal has to start.
	 * @param endPoints
	 *                         set of nodes at which the traversal ends. This set is
	 *                         populated by this method call, and acts as one of the
	 *                         outputs.
	 * @param terminationCheck
	 *                         a functional interface, which is used to specify the
	 *                         condition
	 *                         for termination of the CFG traversal.
	 * @return
	 *         set of nodes that lie on any of the paths between any element of
	 *         {@code startPoint} and {@code endPoints}, exclusively.
	 */
	public static Set<Node> collectNodesIntraTaskBackward(Set<Node> startPoints, Set<Node> endPoints,
			Predicate<Node> terminationCheck) {
		Set<NodeWithStack> stackedStartingPoints = new HashSet<>();
		Set<NodeWithStack> stackedEndingPoints = new HashSet<>();
		Predicate<NodeWithStack> stackedTerminationCheck = (n) -> terminationCheck.test(n.getNode());
		for (Node startPoint : startPoints) {
			stackedStartingPoints.add(new NodeWithStack(startPoint, new CallStack()));
		}
		Set<NodeWithStack> stackedCollectedNodes = collectNodeSetInGenericGraph(stackedStartingPoints,
				stackedEndingPoints, stackedTerminationCheck,
				(n) -> n.getNode().getInfo().getCFGInfo().getInterProceduralLeafPredecessors(n.getCallStack()));

		for (NodeWithStack nodeWithStack : stackedEndingPoints) {
			endPoints.add(nodeWithStack.getNode());
		}

		Set<Node> collectedNodes = new HashSet<>();
		for (NodeWithStack nodeWithStack : stackedCollectedNodes) {
			collectedNodes.add(nodeWithStack.getNode());
		}
		return collectedNodes;
		// Set<Node> collectedNodeSet = new HashSet<>();
		// Set<Node> workSet = new HashSet<>(startPoints);
		//
		// while (workSet.size() != 0) {
		// Node currentNode = Misc.getAnyElement(workSet);
		// workSet.remove(currentNode);
		// for (Node succNode :
		// currentNode.getInfo().getCFGInfo().getInterProceduralLeafPredecessors()) {
		// if (!terminationCheck.test(succNode)) {
		// if (!collectedNodeSet.contains(succNode)) {
		// collectedNodeSet.add(succNode);
		// workSet.add(succNode);
		// }
		// } else {
		// endPoints.add(succNode);
		// }
		// }
		// }
		// return collectedNodeSet;
	}

	/**
	 * This visitor traverses forward on all the intra-task inter-procedural
	 * context-sensitive paths that start at {@code startPoint} and end at
	 * those nodes where the {@code terminationCheck} succeeds.
	 * <p>
	 * Note that when a traversal root is a non-leaf CFG node, the traversal
	 * would start with the successors of that non-leaf node, rather than those
	 * of its {@link BeginNode}.
	 * 
	 * @param startPoint
	 *                         node from which traversal has to start.
	 * @param endPoints
	 *                         set of nodes at which the traversal ends. This set is
	 *                         populated by this method call, and acts as one of the
	 *                         outputs.
	 * @param terminationCheck
	 *                         a functional interface, which is used to specify the
	 *                         condition
	 *                         for termination of the CFG traversal.
	 * @return
	 *         set of nodes that lie on any of the paths between
	 *         {@code startPoint} and {@code endPoints}, exclusively.
	 */
	public static Set<NodeWithStack> collectNodesIntraTaskForwardContextSensitive(NodeWithStack startPoint,
			Set<NodeWithStack> endPoints, Predicate<NodeWithStack> terminationCheck) {
		Set<NodeWithStack> startPoints = new HashSet<>();
		startPoints.add(startPoint);
		return collectNodeSetInGenericGraph(startPoints, endPoints, terminationCheck,
				(n) -> n.getNode().getInfo().getCFGInfo().getInterProceduralLeafSuccessors(n.getCallStack()));
	}

	/**
	 * This visitor traverses forwards on all the intra-task inter-procedural
	 * context-sensitive paths that start at any element in the
	 * {@code startPoint} and end at those nodes where the
	 * {@code terminationCheck} succeeds.
	 * <p>
	 * Note that when a traversal root is a non-leaf CFG node, the traversal
	 * would start with the successors of that non-leaf node, rather than those
	 * of its {@link BeginNode}.
	 * 
	 * @param startPoints
	 *                         set of nodes from which traversal has to start.
	 * @param endPoints
	 *                         set of nodes at which the traversal ends. This set is
	 *                         populated by this method call, and acts as one of the
	 *                         outputs.
	 * @param terminationCheck
	 *                         a functional interface, which is used to specify the
	 *                         condition
	 *                         for termination of the CFG traversal.
	 * @return
	 *         set of nodes that lie on any of the paths between any element of
	 *         {@code startPoint} and {@code endPoints}, exclusively.
	 */
	public static Set<NodeWithStack> collectNodesIntraTaskForwardContextSensitive(Set<NodeWithStack> startPoints,
			Set<NodeWithStack> endPoints, Predicate<NodeWithStack> terminationCheck) {
		return collectNodeSetInGenericGraph(startPoints, endPoints, terminationCheck,
				(n) -> n.getNode().getInfo().getCFGInfo().getInterProceduralLeafSuccessors(n.getCallStack()));
	}

	/**
	 * This visitor traverses backward on all the intra-task inter-procedural
	 * context-sensitive paths that start at {@code startPoint} and end at
	 * those nodes where the {@code terminationCheck} succeeds.
	 * <p>
	 * Note that when a traversal root is a non-leaf CFG node, the traversal
	 * would start with the successors of that non-leaf node, rather than those
	 * of its {@link BeginNode}.
	 * 
	 * @param startPoint
	 *                         node from which traversal has to start.
	 * @param endPoints
	 *                         set of nodes at which the traversal ends. This set is
	 *                         populated by this method call, and acts as one of the
	 *                         outputs.
	 * @param terminationCheck
	 *                         a functional interface, which is used to specify the
	 *                         condition
	 *                         for termination of the CFG traversal.
	 * @return
	 *         set of nodes that lie on any of the paths between
	 *         {@code startPoint} and {@code endPoints}, exclusively.
	 */
	public static Set<NodeWithStack> collectNodesIntraTaskBackwardContextSensitive(NodeWithStack startPoint,
			Set<NodeWithStack> endPoints, Predicate<NodeWithStack> terminationCheck) {
		Set<NodeWithStack> startPoints = new HashSet<>();
		startPoints.add(startPoint);
		return collectNodeSetInGenericGraph(startPoints, endPoints, terminationCheck,
				(n) -> n.getNode().getInfo().getCFGInfo().getInterProceduralLeafPredecessors(n.getCallStack()));
	}

	/**
	 * This visitor traverses backwards on all the intra-task inter-procedural
	 * context-insensitive paths that start at any element in the
	 * {@code startPoint} and end at those nodes where the
	 * {@code terminationCheck} succeeds.
	 * <p>
	 * Note that when a traversal root is a non-leaf CFG node, the traversal
	 * would start with the successors of that non-leaf node, rather than those
	 * of its {@link BeginNode}.
	 * 
	 * @param startPoints
	 *                         set of nodes from which traversal has to start.
	 * @param endPoints
	 *                         set of nodes at which the traversal ends. This set is
	 *                         populated by this method call, and acts as one of the
	 *                         outputs.
	 * @param terminationCheck
	 *                         a functional interface, which is used to specify the
	 *                         condition
	 *                         for termination of the CFG traversal.
	 * @return
	 *         set of nodes that lie on any of the paths between any element of
	 *         {@code startPoint} and {@code endPoints}, exclusively.
	 */
	public static Set<NodeWithStack> collectNodesIntraTaskBackwardContextSensitive(Set<NodeWithStack> startPoints,
			Set<NodeWithStack> endPoints, Predicate<NodeWithStack> terminationCheck) {
		return collectNodeSetInGenericGraph(startPoints, endPoints, terminationCheck,
				(n) -> n.getNode().getInfo().getCFGInfo().getInterProceduralLeafPredecessors(n.getCallStack()));
	}

	public static Set<NodeWithStack> collectNodesIntraTaskForwardBarrierFreePath(NodeWithStack startPoint,
			Set<NodeWithStack> endPoints) {
		Set<NodeWithStack> startPoints = new HashSet<>();
		startPoints.add(startPoint);
		Set<NodeWithStack> collectedNodes = collectNodeSetInGenericGraph(startPoints, endPoints,
				(n) -> (n.getNode() instanceof BarrierDirective || (n.getNode() instanceof EndNode
						&& ((EndNode) n.getNode()).getParent() instanceof ParallelConstruct)),
				(n) -> n.getNode().getInfo().getCFGInfo()
						.getParallelConstructFreeInterProceduralLeafSuccessors(n.getCallStack()));
		Set<NodeWithStack> newNodeStacks = new HashSet<>();
		for (NodeWithStack nodeWithStack : collectedNodes) {
			Node node = nodeWithStack.getNode();
			if (node instanceof BeginNode) {
				BeginNode beginNode = (BeginNode) node;
				if (beginNode.getParent() instanceof ParallelConstruct) {
					ParallelConstruct parConstruct = (ParallelConstruct) beginNode.getParent();
					newNodeStacks.addAll(parConstruct.getInfo().getCFGInfo()
							.getIntraTaskCFGLeafContents(nodeWithStack.getCallStack()));
				}
			}
		}
		collectedNodes.addAll(newNodeStacks);
		return collectedNodes;
	}

	public static Set<NodeWithStack> collectNodesIntraTaskBackwardBarrierFreePath(NodeWithStack startPoint,
			Set<NodeWithStack> endPoints) {
		Set<NodeWithStack> startPoints = new HashSet<>();
		startPoints.add(startPoint);
		Set<NodeWithStack> collectedNodes = collectNodeSetInGenericGraph(startPoints, endPoints,
				(n) -> (n.getNode() instanceof BarrierDirective || (n.getNode() instanceof BeginNode
						&& ((BeginNode) n.getNode()).getParent() instanceof ParallelConstruct)),
				(n) -> n.getNode().getInfo().getCFGInfo()
						.getParallelConstructFreeInterProceduralLeafPredecessors(n.getCallStack()));
		Set<NodeWithStack> newNodeStacks = new HashSet<>();
		for (NodeWithStack nodeWithStack : collectedNodes) {
			Node node = nodeWithStack.getNode();
			if (node instanceof EndNode) {
				EndNode endNode = (EndNode) node;
				if (endNode.getParent() instanceof ParallelConstruct) {
					ParallelConstruct parConstruct = (ParallelConstruct) endNode.getParent();
					newNodeStacks.addAll(parConstruct.getInfo().getCFGInfo()
							.getIntraTaskCFGLeafContents(nodeWithStack.getCallStack()));
				}
			}
		}
		collectedNodes.addAll(newNodeStacks);
		return collectedNodes;
	}

	/**
	 * TODO: Needs testing.
	 * 
	 * @param startPoint
	 * @param endPoints
	 * @param terminationCheck
	 * @return
	 */
	public static Set<NodeWithStack> collectNodesIntraTaskForwardOfSameParLevel(NodeWithStack startPoint,
			Set<NodeWithStack> endPoints, Predicate<NodeWithStack> terminationCheck) {
		Set<NodeWithStack> startPoints = new HashSet<>();
		startPoints.add(startPoint);
		return collectNodeSetInGenericGraph(startPoints, endPoints, terminationCheck, (n) -> n.getNode().getInfo()
				.getCFGInfo().getParallelConstructFreeInterProceduralLeafSuccessors(n.getCallStack()));
	}
}
