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

import imop.ast.node.internal.OmpClause;

/**
 * Grammar production:
 * f0 ::= <NOWAIT>
 */
public class NowaitClause extends OmpClause {
	{
		classId *= 443;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4887394755402492028L;
	private NodeToken f0;

	public NowaitClause(NodeToken n0) {
		n0.setParent(this);
		setF0(n0);
	}

	public NowaitClause() {
		setF0(new NodeToken("nowait"));
		getF0().setParent(this);
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
}
