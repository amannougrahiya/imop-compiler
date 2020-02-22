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

import imop.ast.info.cfgNodeInfo.AtomicConstructInfo;
import imop.ast.node.internal.CallStatement;

/**
 * Grammar production:
 * f0 ::= OmpPragma()
 * f1 ::= <ATOMIC>
 * f2 ::= ( AtomicClause() )?
 * f3 ::= OmpEol()
 * f4 ::= Statement()
 */
public class AtomicConstruct extends OmpConstruct {
	/**
	 * 
	 */
	private static final long serialVersionUID = 617572517002031663L;
	private OmpPragma f0;
	private NodeToken f1;
	private NodeOptional f2;
	private OmpEol f3;
	private Statement f4;

	public AtomicConstruct(OmpPragma n0, NodeToken n1, NodeOptional n2, OmpEol n3, Statement n6) {
		assert (n6 instanceof ExpressionStatement || n6 instanceof CallStatement);
		n0.setParent(this);
		n1.setParent(this);
		n2.setParent(this);
		n3.setParent(this);
		n6.setParent(this);
		setF0(n0);
		setF1(n1);
		setF2(n2);
		setF3(n3);
		setF4(n6);
	}

	public AtomicConstruct(OmpPragma n0, NodeOptional n1, OmpEol n2, Statement n3) {
		assert (n3 instanceof ExpressionStatement || n3 instanceof CallStatement);
		n0.setParent(this);
		n1.setParent(this);
		n2.setParent(this);
		n3.setParent(this);
		setF0(n0);
		setF1(new NodeToken("atomic"));
		getF1().setParent(this);
		setF2(n1);
		setF3(n2);
		setF4(n3);
	}

	@Override
	public boolean isKnownCFGNode() {
		return true;
	}

	@Override
	public boolean isKnownCFGLeafNode() {
		return false;
	}

	@Override
	public AtomicConstructInfo getInfo() {
		if (info == null) {
			info = new AtomicConstructInfo(this);
		}
		return (AtomicConstructInfo) info;
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

	public NodeOptional getF2() {
		return f2;
	}

	public void setF2(NodeOptional f2) {
		f2.setParent(this);
		this.f2 = f2;
	}

	public OmpEol getF3() {
		return f3;
	}

	public void setF3(OmpEol f3) {
		f3.setParent(this);
		this.f3 = f3;
	}

	public Statement getF4() {
		return f4;
	}

	public void setF4(Statement f4) {
		//		assert (f4 instanceof ExpressionStatement || f4 instanceof CallStatement);
		f4.setParent(this);
		this.f4 = f4;
	}
}
