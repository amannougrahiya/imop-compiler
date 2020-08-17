/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.node.internal;

import imop.ast.info.cfgNodeInfo.EndNodeInfo;
import imop.ast.node.external.*;
import imop.baseVisitor.GJNoArguVisitor;
import imop.baseVisitor.GJVisitor;
import imop.baseVisitor.GJVoidVisitor;
import imop.baseVisitor.Visitor;
import imop.lib.cfg.NestedCFG;

public class EndNode extends Node {
	{
		classId = 1523;
	}

	public EndNode() {
		ownerNestedCFG = null;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2902007948010286413L;
	private final NestedCFG ownerNestedCFG;

	public EndNode(NestedCFG owner) {
		this.ownerNestedCFG = owner;
		setParent(owner.getNode());
	}

	@Override
	public EndNodeInfo getInfo() {
		if (info == null) {
			info = new EndNodeInfo(this);
		}
		return (EndNodeInfo) info;
	}

	@Override
	public boolean isKnownCFGNode() {
		return true;
	}

	@Override
	public boolean isKnownCFGLeafNode() {
		return true;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) {
		return v.visit(this, argu);
	}

	@Override
	public <R> R accept(GJNoArguVisitor<R> v) {
		return v.visit(this);
	}

	@Override
	public <A> void accept(GJVoidVisitor<A> v, A argu) {
		v.visit(this, argu);

	}

	public NestedCFG getOwnerNestedCFG() {
		return ownerNestedCFG;
	}

}
