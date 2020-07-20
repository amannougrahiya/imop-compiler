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

import imop.ast.info.OmpConstructInfo;

/**
 * Grammar production:
 * f0 ::= ParallelConstruct()
 * | ForConstruct()
 * | SectionsConstruct()
 * | SingleConstruct()
 * | ParallelForConstruct()
 * | ParallelSectionsConstruct()
 * | TaskConstruct()
 * | MasterConstruct()
 * | CriticalConstruct()
 * | AtomicConstruct()
 * | OrderedConstruct()
 */
public class OmpConstruct extends Statement {
	{
		classId = 39;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8713640834160821946L;
	private NodeChoice ompConsF0;

	public OmpConstruct(NodeChoice n0) {
		n0 = splitParForAndSections(n0);
		n0.setParent(this);
		setOmpConsF0(n0);
	}

	public OmpConstruct() {
	}

	@Override
	public OmpConstructInfo getInfo() {
		if (info == null) {
			info = new OmpConstructInfo(this);
		}
		return (OmpConstructInfo) info;
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

	public NodeChoice getOmpConsF0() {
		return ompConsF0;
	}

	public void setOmpConsF0(NodeChoice ompConsF0) {
		ompConsF0 = this.splitParForAndSections(ompConsF0);
		ompConsF0.setParent(this);
		this.ompConsF0 = ompConsF0;
	}

	private NodeChoice splitParForAndSections(NodeChoice ompConsF0) {
		NodeChoice retNode = ompConsF0;
		Node choice = ompConsF0.getChoice();
		if (choice instanceof ParallelForConstruct) {
			// TODO: Import code from ExpressionSimplifier to here.
		} else if (choice instanceof ParallelSectionsConstruct) {
			// TODO: Import code from ExpressionSimplifier to here.
		}
		return retNode;
	}
}
