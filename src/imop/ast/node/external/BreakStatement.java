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

import imop.ast.info.cfgNodeInfo.BreakStatementInfo;

/**
 * Grammar production:
 * f0 ::= <BREAK>
 * f1 ::= ";"
 */
public class BreakStatement extends JumpStatement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6229969095866224526L;
	private NodeToken f0;
	private NodeToken f1;

	public BreakStatement(NodeToken n0, NodeToken n1) {
		n0.setParent(this);
		n1.setParent(this);
		setF0(n0);
		setF1(n1);
	}

	public BreakStatement() {
		setF0(new NodeToken("break"));
		getF0().setParent(this);
		setF1(new NodeToken(";"));
		getF1().setParent(this);
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
	public BreakStatementInfo getInfo() {
		if (info == null) {
			info = new BreakStatementInfo(this);
		}
		return (BreakStatementInfo) info;
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

	public NodeToken getF0() {
		return f0;
	}

	public void setF0(NodeToken f0) {
		f0.setParent(this);
		this.f0 = f0;
	}

	public NodeToken getF1() {
		return f1;
	}

	public void setF1(NodeToken f1) {
		f1.setParent(this);
		this.f1 = f1;
	}
}
