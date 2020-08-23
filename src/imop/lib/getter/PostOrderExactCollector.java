/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstProcess;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Performs a post-order traversal in the visited node to collect the nodes
 * (inclusively) of classes specified in {@code classSet}.
 * <p>
 * Note that this method relies on Reflections, and hence objects of type
 * subclasses of entried in {@code classSet} will not be collected.
 * 
 * @author Aman Nougrahiya
 *
 */
public class PostOrderExactCollector extends DepthFirstProcess {
	private Set<Class<? extends Node>> classSet;

	public List<Node> nodesInPostOrder = new LinkedList<>();

	public PostOrderExactCollector(Set<Class<? extends Node>> classSet) {
		this.classSet = classSet;
	}

	@Override
	public void endProcess(Node n) {
		if (classSet.contains(n.getClass())) {
			nodesInPostOrder.add(n);
		}
	}

}
