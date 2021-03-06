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
 * f0 ::= OmpPragma()
 * f1 ::= <DECLARE>
 * f2 ::= <REDUCTION>
 * f3 ::= "("
 * f4 ::= ReductionOp()
 * f5 ::= ":"
 * f6 ::= ReductionTypeList()
 * f7 ::= ":"
 * f8 ::= Expression()
 * f9 ::= ")"
 * f10 ::= ( InitializerClause() )?
 * f11 ::= OmpEol()
 */
public class DeclareReductionDirective extends Node {
	{
		classId = 1039;
	}

	public DeclareReductionDirective() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2075836272624213118L;
	private OmpPragma f0;
	private NodeToken f1;
	private NodeToken f2;
	private NodeToken f3;
	private ReductionOp f4;
	private NodeToken f5;
	private ReductionTypeList f6;
	private NodeToken f7;
	private Expression f8;
	private NodeToken f9;
	private NodeOptional f10;
	private OmpEol f11;

	public DeclareReductionDirective(OmpPragma n0, NodeToken n1, NodeToken n2, NodeToken n3, ReductionOp n4,
			NodeToken n5, ReductionTypeList n6, NodeToken n7, Expression n8, NodeToken n9, NodeOptional n10,
			OmpEol n11) {
		n0.setParent(this);
		n1.setParent(this);
		n2.setParent(this);
		n3.setParent(this);
		n4.setParent(this);
		n5.setParent(this);
		n6.setParent(this);
		n7.setParent(this);
		n8.setParent(this);
		n9.setParent(this);
		n10.setParent(this);
		n11.setParent(this);
		setF0(n0);
		setF1(n1);
		setF2(n2);
		setF3(n3);
		setF4(n4);
		setF5(n5);
		setF6(n6);
		setF7(n7);
		setF8(n8);
		setF9(n9);
		setF10(n10);
		setF11(n11);
	}

	public DeclareReductionDirective(OmpPragma n0, ReductionOp n1, ReductionTypeList n2, Expression n3, NodeOptional n4,
			OmpEol n5) {
		n0.setParent(this);
		n1.setParent(this);
		n2.setParent(this);
		n3.setParent(this);
		n4.setParent(this);
		n5.setParent(this);
		setF0(n0);
		setF1(new NodeToken("declare"));
		getF1().setParent(this);
		setF2(new NodeToken("reduction"));
		getF2().setParent(this);
		setF3(new NodeToken("("));
		getF3().setParent(this);
		setF4(n1);
		setF5(new NodeToken(":"));
		getF5().setParent(this);
		setF6(n2);
		setF7(new NodeToken(":"));
		getF7().setParent(this);
		setF8(n3);
		setF9(new NodeToken(")"));
		getF9().setParent(this);
		setF10(n4);
		setF11(n5);
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

	public OmpPragma getF0() {
		return f0;
	}

	public void setF0(OmpPragma f0) {
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

	public NodeToken getF2() {
		return f2;
	}

	public void setF2(NodeToken f2) {
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

	public ReductionOp getF4() {
		return f4;
	}

	public void setF4(ReductionOp f4) {
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

	public ReductionTypeList getF6() {
		return f6;
	}

	public void setF6(ReductionTypeList f6) {
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

	public Expression getF8() {
		return f8;
	}

	public void setF8(Expression f8) {
		f8.setParent(this);
		this.f8 = f8;
	}

	public NodeToken getF9() {
		return f9;
	}

	public void setF9(NodeToken f9) {
		f9.setParent(this);
		this.f9 = f9;
	}

	public NodeOptional getF10() {
		return f10;
	}

	public void setF10(NodeOptional f10) {
		f10.setParent(this);
		this.f10 = f10;
	}

	public OmpEol getF11() {
		return f11;
	}

	public void setF11(OmpEol f11) {
		f11.setParent(this);
		this.f11 = f11;
	}
}
