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

/**
 * Grammar production:
 * f0 ::= "=="
 * f1 ::= EqualityExpression()
 */
public class EqualExpression extends Expression {
	/**
	 * 
	 */
	private static final long serialVersionUID = 733004165219049998L;
	private NodeToken f0;
	private EqualityExpression f1;

	public EqualExpression(NodeToken n0, EqualityExpression n1) {
		n0.setParent(this);
		n1.setParent(this);
		setF0(n0);
		setF1(n1);
	}

	public EqualExpression(EqualityExpression n0) {
		n0.setParent(this);
		setF0(new NodeToken("=="));
		getF0().setParent(this);
		setF1(n0);
	}

	@Override
	public boolean isCFGNode() {
		return false;
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

	public EqualityExpression getF1() {
		return f1;
	}

	public void setF1(EqualityExpression f1) {
		f1.setParent(this);
		this.f1 = f1;
	}
}
