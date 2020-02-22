/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
//
// Generated by JTB 1.3.2
//

package imop.ast.node.external;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an optional grammar list, e.g. ( A )*
 */
public class NodeListOptional extends NodeListClass {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1246160005166159612L;

	private List<Node> nodes;

	public NodeListOptional() {
		setNodes(new ArrayList<>());
	}

	public NodeListOptional(Node firstNode) {
		firstNode.setParent(this);
		setNodes(new ArrayList<>());
		addNode(firstNode);
	}

	public NodeListOptional(List<Node> nodes) {
		this.setNodes(new ArrayList<>(nodes));
		for (Node node : nodes) {
			node.setParent(this);
		}
	}

	public void addNode(int index, Node n) {
		n.setParent(this);
		this.nodes.add(index, n);
	}

	@Override
	public void addNode(Node n) {
		n.setParent(this);
		this.nodes.add(n);
	}

	@Override
	public List<Node> elements() {
		return this.nodes;
	}

	@Override
	public Node elementAt(int i) {
		return this.nodes.get(i);
	}

	public Node removeNode(int index) {
		Node element = this.nodes.get(index);
		element.setParent(null);
		return this.nodes.remove(index);
	}

	public boolean removeNode(Node toBeRemoved) {
		if (this.nodes.contains(toBeRemoved)) {
			toBeRemoved.setParent(null);
		}
		return this.nodes.remove(toBeRemoved);
	}

	@Override
	public int size() {
		return this.nodes.size();
	}

	public boolean present() {
		return this.nodes.size() != 0;
	}

	@Override
	public void accept(imop.baseVisitor.Visitor v) {
		v.visit(this);
	}

	@Override
	public <R, A> R accept(imop.baseVisitor.GJVisitor<R, A> v, A argu) {
		return v.visit(this, argu);
	}

	@Override
	public <R> R accept(imop.baseVisitor.GJNoArguVisitor<R> v) {
		return v.visit(this);
	}

	@Override
	public <A> void accept(imop.baseVisitor.GJVoidVisitor<A> v, A argu) {
		v.visit(this, argu);
	}

	public List<Node> getNodes() {
		return Collections.unmodifiableList(nodes);
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public void trimToSize() {
		((ArrayList<Node>) this.nodes).trimToSize();
	}

	public void clearNodes() {
		for (Node elem : this.nodes) {
			elem.setParent(null);
		}
		this.nodes.clear();
		;
	}

}
