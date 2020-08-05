/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.info;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import imop.ast.node.external.Node;
import imop.lib.util.Misc;
import imop.parser.Program;

/**
 * This class is used to cache a list of a given exact-type of AST node in the
 * programs' root.
 * This would help in minimizing the cost of Misc.getInheritedEnclosee() method.
 * 
 * Note that this class employs the lazy-update mode of stabilization.
 * 
 * @author Aman Nougrahiya
 *
 * @param <T>
 *            type of the AST node whose elements a given object would hold.
 */
public class ProgramElementExactCaches<T extends Node> {
	private Class<T> classType;
	private Set<T> elements = new HashSet<>();
	private Set<Node> nodesAdded = new HashSet<>();
	private Set<Node> nodesRemoved = new HashSet<>();
	private static Map<Class<? extends Node>, ProgramElementExactCaches<? extends Node>> allCaches = new HashMap<>();
	private boolean staleFlag = false;

	private ProgramElementExactCaches(Class<T> classType) {
		allCaches.put(classType, this);
		this.classType = classType;
		this.elements.addAll(Misc.getExactEncloseeWithId(Program.getRoot(), Misc.getClassId(classType)));
	}

	public static <R extends Node> Set<R> getElements(Class<R> classType) {
		@SuppressWarnings("unchecked")
		ProgramElementExactCaches<R> cache = (ProgramElementExactCaches<R>) allCaches.get(classType);
		if (cache == null) {
			cache = new ProgramElementExactCaches<R>(classType);
		}
		if (cache.staleFlag) {
			cache.stabilize();
			cache.staleFlag = false;
		}
		return cache.elements;
	}

	private void stabilize() {
		for (Node n : nodesRemoved) {
			elements.removeAll(Misc.getExactEncloseeWithId(n, Misc.getClassId(classType)));
		}
		nodesRemoved.clear();
		for (Node n : nodesAdded) {
			elements.addAll(Misc.getExactEncloseeWithId(n, Misc.getClassId(classType)));
		}
		nodesAdded.clear();
	}

	/**
	 * Invoked during elementary transformations that add a node.
	 * 
	 * @param addedNode
	 */
	public static void considerNodeAddition(Node addedNode) {
		for (ProgramElementExactCaches<? extends Node> pec : ProgramElementExactCaches.allCaches.values()) {
			boolean changed = false;
			if (pec.nodesRemoved.contains(addedNode)) {
				changed |= pec.nodesRemoved.remove(addedNode);
			} else {
				changed |= pec.nodesAdded.add(addedNode);
			}
			if (changed) {
				pec.staleFlag = true;
			}
		}
	}

	/**
	 * Invoked during elementary transformations that remove a node.
	 * 
	 * @param removedNode
	 */
	public static void considerNodeRemoval(Node removedNode) {
		for (ProgramElementExactCaches<? extends Node> pec : ProgramElementExactCaches.allCaches.values()) {
			boolean changed = false;
			if (pec.nodesAdded.contains(removedNode)) {
				changed |= pec.nodesAdded.remove(removedNode);
			} else {
				changed |= pec.nodesRemoved.add(removedNode);
			}
			if (changed) {
				pec.staleFlag = true;
			}
		}
	}

}
