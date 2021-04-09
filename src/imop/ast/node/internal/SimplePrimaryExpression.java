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

public class SimplePrimaryExpression extends Expression {
	{
		classId = 1318;
	}

	public SimplePrimaryExpression() {
		anIdentifier = null;
		aConstant = null;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4425674770641887330L;
	private final NodeToken anIdentifier;
	private final Constant aConstant;

	public SimplePrimaryExpression(NodeToken anIdentifier) {
		this.anIdentifier = anIdentifier;
		this.aConstant = null;
		this.anIdentifier.setParent(this);
	}

	public SimplePrimaryExpression(Constant aConstant) {
		this.aConstant = aConstant;
		this.anIdentifier = null;
		this.aConstant.setParent(this);
	}

	@Override
	public boolean isCFGNode() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SimplePrimaryExpression)) {
			return false;
		}
		SimplePrimaryExpression that = (SimplePrimaryExpression) obj;
		if (this.isAConstant()) {
			if (that.isAnIdentifier()) {
				return false;
			} else {
				if (that.aConstant.toString().equals(this.aConstant.toString())) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			if (that.isAConstant()) {
				return false;
			} else {
				if (that.anIdentifier.toString().equals(this.anIdentifier.toString())) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (this.isAConstant()) {
			result = prime * result + ((this.aConstant.toString() == null) ? 0 : this.aConstant.toString().hashCode());
			return result;
			// return Arrays.hashCode(new Object[]{this.aConstant.toString()});
		} else {
			result = prime * result
					+ ((this.anIdentifier.toString() == null) ? 0 : this.anIdentifier.toString().hashCode());
			return result;
			// return Arrays.hashCode(new Object[]{this.anIdentifier.toString()});
		}
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

	public boolean isAnIdentifier() {
		return anIdentifier != null;
	}

	public boolean isAConstant() {
		return aConstant != null;
	}

	public NodeToken getIdentifier() {
		return anIdentifier;
	}

	public Constant getConstant() {
		return aConstant;
	}

	public String getString() {
		if (this.isAnIdentifier()) {
			return this.anIdentifier.getTokenImage();
		} else {
			return this.aConstant.toString();
		}
	}
}
