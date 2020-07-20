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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import imop.ast.info.NodeInfo;
import imop.lib.analysis.flowanalysis.DFable;
import imop.lib.util.Misc;

/**
 * The class which all syntax tree classes must extend.
 */

public abstract class Node implements java.io.Serializable, DFable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -58300488119959833L;
	public static final Set<Integer> superClassIds = new HashSet<>(
			Arrays.asList(new Integer[] { 2, 3, 541, 467, 281, 1466, 15, 21, 33, 39, 51, 57, 1479, 897 }));
	private Node parent;
	protected NodeInfo info;
	private int nodeId = -1;
	private static int counter = 0;

	private static List<Node> allLeafCFGNodes = new ArrayList<>();

	// Should throw a divide-by-zero error, if a certain class doesn't provide correct value for this field.
	public int classId = 0;

	public final int getClassId() {
		return classId;
	}

	protected Node() {
		if (Misc.isCFGLeafNode(this)) {
			allLeafCFGNodes.add(this);
			nodeId = counter++;
		}
	}

	public int getNodeId() {
		return this.nodeId;
	}

	/**
	 * Until we represent all predicate expression by their own class (which can
	 * allow us to separate out all CFG nodes using type information alone), we
	 * use this method to read the CFG status of certain classes all whose
	 * objects have the same status.
	 * <br>
	 * As per the current hierarchy (), this method should return false only for
	 * those objects which are of static type {@link OmpForReinitExpression},
	 * {@link ParallelConstruct}, or {@link Expression}.
	 * 
	 * @return
	 *         true, if either all objects of the class of the receiver are
	 *         known to be CFG nodes, or all are known to be non-CFG nodes.
	 */
	public boolean hasKnownCFGStatus() {
		return true;
	}

	/**
	 * Note: Should be called only when this node returns true for
	 * {@link #hasKnownCFGStatus()}.
	 * 
	 * @return
	 *         true, if this node is a CFG node.
	 */
	public boolean isKnownCFGNode() {
		assert (this.hasKnownCFGStatus());
		return false;
	}

	/**
	 * Note: Should be called only when this node returns true for
	 * {@link #hasKnownCFGStatus()}.
	 * 
	 * @return
	 *         true, if this node is a CFG leaf node.
	 */
	public boolean isKnownCFGLeafNode() {
		assert (this.hasKnownCFGStatus());
		return false;
	}

	public NodeInfo getInfo() {
		if (info == null) {
			info = new NodeInfo(this);
		}
		return info;
	}

	public void setInfo(NodeInfo newInfo) {
		this.info = newInfo;
	}

	public abstract void accept(imop.baseVisitor.Visitor v);

	public abstract <R, A> R accept(imop.baseVisitor.GJVisitor<R, A> v, A argu);

	public abstract <R> R accept(imop.baseVisitor.GJNoArguVisitor<R> v);

	public abstract <A> void accept(imop.baseVisitor.GJVoidVisitor<A> v, A argu);

	public NodeInfo getInfoCopied(Node oldNode) {
		NodeInfo info = this.getInfo();
		info.setCopySourceNode(oldNode);
		return info;
	}

	@Override
	public String toString() {
		return this.getInfo().getString();
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		assert (parent != this);
		this.parent = parent;
	}

	@Override
	public Set<DFable> getDataFlowPredecessors() {
		return this.getInfo().getCFGInfo().getDataFlowPredecessors();
	}

	@Override
	public Set<DFable> getDataFlowSuccessors() {
		return this.getInfo().getCFGInfo().getDataFlowSuccessors();
	}

	@Override
	public void setReversePostOrder(int i) {
		this.getInfo().setReversePostOrderId(i);
	}

	@Override
	public int getReversePostOrder() {
		return this.getInfo().getReversePostOrder();
	}
}
