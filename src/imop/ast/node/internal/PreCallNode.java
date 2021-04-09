/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.node.internal;

import imop.ast.node.external.*;
import imop.baseVisitor.GJNoArguVisitor;
import imop.baseVisitor.GJVisitor;
import imop.baseVisitor.GJVoidVisitor;
import imop.baseVisitor.Visitor;

import java.util.List;

public class PreCallNode extends Node {
	{
		classId = 163;
	}

	public PreCallNode() {
		argumentList = null;
	}

	private static final long serialVersionUID = -7723480838912592935L;

	private final List<SimplePrimaryExpression> argumentList;

	@Override
	public boolean isKnownCFGNode() {
		return true;
	}

	@Override
	public boolean isKnownCFGLeafNode() {
		return true;
	}

	public PreCallNode(List<SimplePrimaryExpression> argumentList) {
		for (SimplePrimaryExpression spe : argumentList) {
			spe.setParent(this);
		}
		this.argumentList = argumentList;
	}

	public List<SimplePrimaryExpression> getArgumentList() {
		return argumentList;
	}

	@Override
	public String toString() {
		String retString = "PRE";
		if (this.getParent() != null) {
			retString += "(" + this.getParent() + ")";
		}
		return retString;
	}

	@Override
	public CallStatement getParent() {
		return (CallStatement) super.getParent();
	}

	// @Override
	// public boolean equals(Object obj) {
	// if (obj == null) {
	// return false;
	// }
	// if (!(obj instanceof PreCallNode)) {
	// return false;
	// }
	// PreCallNode that = (PreCallNode) obj;
	// if (this.argumentList.equals(that.argumentList)) {
	// return true;
	// } else {
	// return false;
	// }
	// // if (this.argumentList.size() != that.argumentList.size()) {
	// // return false;
	// // }
	// // for (SimplePrimaryExpression spe : this.argumentList) {
	// // int index = this.argumentList.indexOf(spe);
	// // if (!that.argumentList.get(index).equals(spe)) {
	// // return false;
	// // }
	// // }
	// // return true;
	// }
	//
	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = prime * result + ((this.argumentList == null) ? 0 :
	// this.argumentList.hashCode());
	// return result;
	// // return Arrays.hashCode(new Object[]{this.argumentList});
	// }
	//
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) {
		return v.visit(this, argu);
	}

	@Override
	public <R> R accept(GJNoArguVisitor<R> v) {
		return v.visit(this);
	}

	@Override
	public <A> void accept(GJVoidVisitor<A> v, A argu) {
		v.visit(this, argu);
	}

}
