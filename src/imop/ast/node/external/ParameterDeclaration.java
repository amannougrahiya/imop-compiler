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

import imop.ast.info.cfgNodeInfo.ParameterDeclarationInfo;

/**
 * Grammar production:
 * f0 ::= DeclarationSpecifiers()
 * f1 ::= ParameterAbstraction()
 */
public class ParameterDeclaration extends Node {
	{
		classId = 251;
	}

	public ParameterDeclaration() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3380491291809713173L;
	private DeclarationSpecifiers f0;
	private ParameterAbstraction f1;

	public ParameterDeclaration(DeclarationSpecifiers n0, ParameterAbstraction n1) {
		n0.setParent(this);
		n1.setParent(this);
		setF0(n0);
		setF1(n1);
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
	public ParameterDeclarationInfo getInfo() {
		if (info == null) {
			info = new ParameterDeclarationInfo(this);
		}
		return (ParameterDeclarationInfo) info;
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

	public DeclarationSpecifiers getF0() {
		return f0;
	}

	public void setF0(DeclarationSpecifiers f0) {
		f0.setParent(this);
		this.f0 = f0;
	}

	public ParameterAbstraction getF1() {
		return f1;
	}

	public void setF1(ParameterAbstraction f1) {
		f1.setParent(this);
		this.f1 = f1;
	}
}
