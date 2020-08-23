/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */

package imop.lib.getter;

import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstVisitor;
import imop.lib.util.Misc;

import java.util.Vector;

/**
 * This class adds to the list of parallel construct all the parallel
 * constructs,
 * including the nested ones.
 * Note that a call to this visitor would be cheaper than a call to
 * {@link Misc#getInheritedEncloseeList(imop.ast.node.external.Node, java.util.HashSet)}
 * since latter involves {@code instanceof} checks.
 */
public class InfiParallelConstructGetter extends DepthFirstVisitor {

	public Vector<ParallelConstruct> parallelConstructList = new Vector<>();

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(ParallelConstruct n) {
		n.getParConsF2().accept(this);
		parallelConstructList.add(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * 
	 * /**
	 * f0 ::= OmpPragma()
	 * f1 ::= <PARALLEL>
	 * f2 ::= <FOR>
	 * f3 ::= UniqueParallelOrUniqueForOrDataClauseList()
	 * f4 ::= OmpEol()
	 * f5 ::= OmpForHeader()
	 * f6 ::= Statement()
	 */
	@Override
	public void visit(ParallelForConstruct n) {
		n.getF6().accept(this);
		parallelConstructList.add(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <PARALLEL>
	 * f2 ::= <SECTIONS>
	 * f3 ::= UniqueParallelOrDataClauseList()
	 * f4 ::= OmpEol()
	 * f5 ::= SectionsScope()
	 */
	@Override
	public void visit(ParallelSectionsConstruct n) {
		n.getF5().accept(this);
		parallelConstructList.add(n);
	}

	@Override
	public void visit(Expression n) {
		// Do nothing; no parallel-construct can reside within an Expression.
	}

}
