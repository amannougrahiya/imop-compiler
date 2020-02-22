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
 * f0 ::= "("
 * f1 ::= Expression()
 * f2 ::= ")"
 */
public class ExpressionClosed extends Expression {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7961223211380013045L;
	private NodeToken f0;
	private Expression f1;
	private NodeToken f2;

	public ExpressionClosed(NodeToken n0, Expression n1, NodeToken n2) {
		n0.setParent(this);
		n1.setParent(this);
		n2.setParent(this);
		setF0(n0);
		setF1(n1);
		setF2(n2);
	}

	@Override
	public boolean isCFGNode() {
		return false;
	}

	public ExpressionClosed(Expression n0) {
		n0.setParent(this);
		setF0(new NodeToken("("));
		getF0().setParent(this);
		setF1(n0);
		setF2(new NodeToken(")"));
		getF2().setParent(this);
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

	public Expression getF1() {
		return f1;
	}

	public void setF1(Expression f1) {
		f1.setParent(this);
		this.f1 = f1;
	}

	public NodeToken getF2() {
		return f2;
	}

	public void setF2(NodeToken f2) {
		f2.setParent(this);
		this.f2 = f2;
	}
}
