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
 * f0 ::= <FOR>
 * f1 ::= "("
 * f2 ::= OmpForInitExpression()
 * f3 ::= ";"
 * f4 ::= OmpForCondition()
 * f5 ::= ";"
 * f6 ::= OmpForReinitExpression()
 * f7 ::= ")"
 */
public class OmpForHeader extends Node {
	{
		classId = 311;
	}

	public OmpForHeader() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6180092732455124209L;
	private NodeToken f0;
	private NodeToken f1;
	private OmpForInitExpression f2;
	private NodeToken f3;
	private OmpForCondition f4;
	private NodeToken f5;
	private OmpForReinitExpression f6;
	private NodeToken f7;

	public OmpForHeader(NodeToken n0, NodeToken n1, OmpForInitExpression n2, NodeToken n3, OmpForCondition n4,
			NodeToken n5, OmpForReinitExpression n6, NodeToken n7) {
		n0.setParent(this);
		n1.setParent(this);
		n2.setParent(this);
		n3.setParent(this);
		n4.setParent(this);
		n5.setParent(this);
		n6.setParent(this);
		n7.setParent(this);
		setF0(n0);
		setF1(n1);
		setF2(n2);
		setF3(n3);
		setF4(n4);
		setF5(n5);
		setF6(n6);
		setF7(n7);
	}

	public OmpForHeader(OmpForInitExpression n0, OmpForCondition n1, OmpForReinitExpression n2) {
		n0.setParent(this);
		n1.setParent(this);
		n2.setParent(this);
		setF0(new NodeToken("for"));
		getF0().setParent(this);
		setF1(new NodeToken("("));
		getF1().setParent(this);
		setF2(n0);
		setF3(new NodeToken(";"));
		getF3().setParent(this);
		setF4(n1);
		setF5(new NodeToken(";"));
		getF5().setParent(this);
		setF6(n2);
		setF7(new NodeToken(")"));
		getF7().setParent(this);
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

	public OmpForInitExpression getF2() {
		return f2;
	}

	public void setF2(OmpForInitExpression f2) {
		f2.setParent(this);
		this.f2 = f2;
	}

	public NodeToken getF3() {
		return f3;
	}

	public void setF3(NodeToken f3) {
		f3.setParent(this);
		this.f3 = f3;
	}

	public OmpForCondition getF4() {
		return f4;
	}

	public void setF4(OmpForCondition f4) {
		f4.setParent(this);
		this.f4 = f4;
	}

	public NodeToken getF5() {
		return f5;
	}

	public void setF5(NodeToken f5) {
		f5.setParent(this);
		this.f5 = f5;
	}

	public OmpForReinitExpression getF6() {
		return f6;
	}

	public void setF6(OmpForReinitExpression f6) {
		f6.setParent(this);
		this.f6 = f6;
	}

	public NodeToken getF7() {
		return f7;
	}

	public void setF7(NodeToken f7) {
		f7.setParent(this);
		this.f7 = f7;
	}
}
