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
 * f0 ::= UnaryOperator()
 * f1 ::= CastExpression()
 */
public class UnaryCastExpression extends Expression {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4529066302046607857L;
	private UnaryOperator f0;
	private CastExpression f1;

	public UnaryCastExpression(UnaryOperator n0, CastExpression n1) {
		n0.setParent(this);
		n1.setParent(this);
		setF0(n0);
		setF1(n1);
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

	public UnaryOperator getF0() {
		return f0;
	}

	public void setF0(UnaryOperator f0) {
		f0.setParent(this);
		this.f0 = f0;
	}

	public CastExpression getF1() {
		return f1;
	}

	public void setF1(CastExpression f1) {
		f1.setParent(this);
		this.f1 = f1;
	}
}
