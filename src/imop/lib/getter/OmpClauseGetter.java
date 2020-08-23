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
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * When this visitor is called upon an OmpConstruct,
 * this populates the ompClauseList with the clauses in that OmpConstruct.
 */
public class OmpClauseGetter extends DepthFirstProcess {
	public List<OmpClause> ompClauseList = new ArrayList<>();

	@Override
	public void initProcess(Node n) {
		if (n instanceof OmpClause) {
			ompClauseList.add((OmpClause) n);
		}
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(ParallelConstruct n) {
		initProcess(n);
		n.getParConsF1().accept(this);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(ForConstruct n) {
		initProcess(n);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTIONS>
	 * f2 ::= NowaitDataClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= SectionsScope()
	 */
	@Override
	public void visit(SectionsConstruct n) {
		initProcess(n);
		n.getF2().accept(this);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SINGLE>
	 * f2 ::= SingleClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SingleConstruct n) {
		initProcess(n);
		n.getF2().accept(this);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASK>
	 * f2 ::= ( TaskClause() )*
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(TaskConstruct n) {
		initProcess(n);
		n.getF2().accept(this);
	}

	/**
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
		initProcess(n);
		n.getF3().accept(this);
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
		initProcess(n);
		n.getF3().accept(this);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= ExpressionStatement()
	 */
	@Override
	public void visit(AtomicConstruct n) {
		initProcess(n);
		n.getF2().accept(this);
	}

}
