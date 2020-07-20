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

import imop.ast.info.cfgNodeInfo.ParallelConstructInfo;
import imop.ast.node.internal.OmpClause;
import imop.lib.cfg.info.ParallelConstructCFGInfo;

/**
 * Grammar production:
 * f0 ::= OmpPragma()
 * f1 ::= ParallelDirective()
 * f2 ::= Statement()
 */
public class ParallelConstruct extends OmpConstruct {
	{
		classId = 897;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -700077472654320239L;
	private OmpPragma parConsF0;
	private ParallelDirective parConsF1;
	private Statement parConsF2;

	/**
	 * This constructor should be called directly only during instantiation of
	 * subclasses of {@link ParallelConstruct}.
	 */
	public ParallelConstruct() {
		/*
		 * Note: Do NOT remove the call to getInfo
		 * below. This method would result in
		 * initialization of the entry phase and
		 * begin-phase-point.
		 */
		this.getInfo();
	}

	public ParallelConstruct(OmpPragma n0, ParallelDirective n1, Statement n2) {
		this();
		n0.setParent(this);
		n1.setParent(this);
		n2.setParent(this);
		setParConsF0(n0);
		setParConsF1(n1);
		setParConsF2(n2);
	}

	@Override
	public boolean hasKnownCFGStatus() {
		return false;
	}

	public boolean isCFGNode() {
		return true;
	}

	@Override
	public ParallelConstructInfo getInfo() {
		if (info == null) {
			info = ParallelConstructInfo.createParallelConstructInfo(this);
		}
		return (ParallelConstructInfo) info;
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

	public void addOmpClause(OmpClause targetNode) {
		this.addOmpClause(targetNode, -1);
	}

	public void addOmpClause(OmpClause targetNode, int index) {
		UniqueParallelOrDataClauseList clauseList = this.getParConsF1().getF1();
		AUniqueParallelOrDataClause targetWrapper = ParallelConstructCFGInfo
				.getAUniqueParallelOrDataClauseWrapper(targetNode);
		if (index == -1) {
			clauseList.getF0().addNode(targetWrapper);
		} else {
			clauseList.getF0().addNode(index, targetWrapper);
		}
		return;
	}

	public OmpPragma getParConsF0() {
		return parConsF0;
	}

	public void setParConsF0(OmpPragma parConsF0) {
		parConsF0.setParent(this);
		this.parConsF0 = parConsF0;
	}

	public ParallelDirective getParConsF1() {
		return parConsF1;
	}

	public void setParConsF1(ParallelDirective parConsF1) {
		parConsF1.setParent(this);
		this.parConsF1 = parConsF1;
	}

	public Statement getParConsF2() {
		return parConsF2;
	}

	public void setParConsF2(Statement parConsF2) {
		parConsF2.setParent(this);
		this.parConsF2 = parConsF2;
	}
}
