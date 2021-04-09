/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import imop.lib.util.CollectorVisitor.NeighbourSetGetter;

import java.util.*;

public class TraversalOrderObtainer {
	public static long orderGenerationTime = 0;

	public static void resetStaticFields() {
		TraversalOrderObtainer.orderGenerationTime = 0;
	}

	/**
	 * Starting with {@code entryNode}, using {@code neighbourGetter} as a
	 * lambda to obtain the list of successors, this method returns a list that
	 * contains all reachable nodes in postorder assuming {@code entryNode} as
	 * the root. Note that the cycles are ignored.
	 * 
	 * @param entryNode
	 *                        starting node, from which postorder traversal has to
	 *                        be
	 *                        performed.
	 * @param neighbourGetter
	 *                        a lambda used to obtain the list of nodes that are
	 *                        successors
	 *                        of the given node.
	 * @return
	 *         a list that contains reachable nodes in postorder.
	 */
	public static <T> List<T> obtainPostOrder(T entryNode, NeighbourSetGetter<T> neighbourGetter) {
		List<T> printList = new ArrayList<>();
		Set<T> graySet = new HashSet<>();
		Set<T> blackSet = new HashSet<>();
		TraversalOrderObtainer.performPostOrder(entryNode, neighbourGetter, graySet, blackSet, printList);
		return printList;
	}

	/**
	 * Starting with {@code entryNode}, using {@code neighbourGetter} as a
	 * lambda to obtain the list of successors, this method returns a list that
	 * contains all reachable nodes in reverse postorder assuming
	 * {@code entryNode} as the root. Note that the cycles are ignored.
	 * 
	 * @param entryNode
	 *                        starting node, from which reverse postorder traversal
	 *                        has to
	 *                        be performed.
	 * @param neighbourGetter
	 *                        a lambda used to obtain the list of nodes that are
	 *                        successors
	 *                        of the given node.
	 * @return
	 *         a list that contains reachable nodes in reverse postorder.
	 */
	public static <T> List<T> obtainReversePostOrder(T entryNode, NeighbourSetGetter<T> neighbourGetter) {
		long timer = System.nanoTime();
		List<T> retListInverted = TraversalOrderObtainer.obtainPostOrder(entryNode, neighbourGetter);
		Collections.reverse(retListInverted);
		orderGenerationTime += (System.nanoTime() - timer);
		return retListInverted;
	}

	private static <T> void performPostOrder(T currentNode, NeighbourSetGetter<T> neighbourGetter, final Set<T> graySet,
			final Set<T> blackSet, final List<T> printList) {
		if (graySet.contains(currentNode)) {
			return;
		} else {
			graySet.add(currentNode);
		}
		Set<T> neighbours = neighbourGetter.getImmediateNeighbours(currentNode);
		for (T neighbour : neighbours) {
			// If a neighbour is black skip it.
			if (blackSet.contains(neighbour)) {
				continue;
			} else {
				/*
				 * Otherwise, visit the neighbour. (Note that the gray
				 * neighbours would be ignored by the first branch above.).
				 */
				performPostOrder(neighbour, neighbourGetter, graySet, blackSet, printList);
			}
		}
		graySet.remove(currentNode);
		blackSet.add(currentNode);
		printList.add(currentNode);
	}
}
