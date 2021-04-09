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

public class PostCallNode extends Node {
	{
		classId = 239;
	}

	private static final long serialVersionUID = 2043636790041815339L;

	private SimplePrimaryExpression returnReceiver;

	public PostCallNode() {
		this.returnReceiver = null;
	}

	public PostCallNode(SimplePrimaryExpression returnReceiver) {
		this.returnReceiver = returnReceiver;
		this.returnReceiver.setParent(this);
	}

	@Override
	public boolean isKnownCFGNode() {
		return true;
	}

	@Override
	public boolean isKnownCFGLeafNode() {
		return true;
	}

	public SimplePrimaryExpression getReturnReceiver() {
		return this.returnReceiver;
	}

	public void setReturnReceiver(SimplePrimaryExpression returnReceiver) {
		this.returnReceiver = returnReceiver;
		this.returnReceiver.setParent(this);
	}

	public boolean hasReturnReceiver() {
		return returnReceiver != null;
	}

	@Override
	public String toString() {
		String retString = "POST";
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
	// if (!(obj instanceof PostCallNode)) {
	// return false;
	// }
	// PostCallNode that = (PostCallNode) obj;
	// if (this.returnReceiver == null) {
	// if (that.returnReceiver == null) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	// if (that.returnReceiver == null) {
	// if (this.returnReceiver == null) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	// if (this.returnReceiver.equals(that.returnReceiver)) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	//
	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = prime * result + ((this.returnReceiver == null) ? 0 :
	// this.returnReceiver.hashCode());
	// return result;
	// // return Arrays.hashCode(new Object[]{this.returnReceiver});
	// }

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
