/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.node.internal;

import imop.ast.info.OmpClauseInfo;
import imop.ast.node.external.*;
import imop.baseVisitor.GJNoArguVisitor;
import imop.baseVisitor.GJVisitor;
import imop.baseVisitor.GJVoidVisitor;
import imop.baseVisitor.Visitor;

/**
 * This is a non-AST class, to be used as a superType of different OmpClauses
 * 
 * @author aman
 *
 */
public abstract class OmpClause extends Node {
	{
		classId = 467;
	}

	public OmpClause() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1319109674485299589L;

	@Override
	public OmpClauseInfo getInfo() {
		if (info == null) {
			this.info = new OmpClauseInfo(this);
		}
		return (OmpClauseInfo) info;
	}

	@Override
	public abstract void accept(Visitor v);

	@Override
	public abstract <R, A> R accept(GJVisitor<R, A> v, A argu);

	@Override
	public abstract <R> R accept(GJNoArguVisitor<R> v);

	@Override
	public abstract <A> void accept(GJVoidVisitor<A> v, A argu);
}
