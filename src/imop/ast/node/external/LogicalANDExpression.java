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
 * f0 ::= InclusiveORExpression()
 * f1 ::= ( "&&" LogicalANDExpression() )?
 */
public class LogicalANDExpression extends Expression {
	{
		classId = 1642;
	}

	public LogicalANDExpression() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6457000833426961883L;
	private InclusiveORExpression f0;
	private NodeOptional f1;

	public LogicalANDExpression(InclusiveORExpression n0, NodeOptional n1) {
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

	public InclusiveORExpression getF0() {
		return f0;
	}

	public void setF0(InclusiveORExpression f0) {
		f0.setParent(this);
		this.f0 = f0;
	}

	public NodeOptional getF1() {
		return f1;
	}

	public void setF1(NodeOptional f1) {
		f1.setParent(this);
		this.f1 = f1;
	}
}
