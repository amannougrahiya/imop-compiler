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

import java.util.Vector;

/**
 * Obtains a list of parallel constructs in the same level of CFG.
 */
public class SiblingParallelConstructGetter extends DepthFirstVisitor {

	Vector<ParallelConstruct> siblingPar = new Vector<>();

	/**
	 * f0 ::= OmpPragma() f1 ::= ParallelDirective() f2 ::= Statement()
	 */
	@Override
	public void visit(ParallelConstruct n) {
		siblingPar.add(n);
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= ForDirective() f2 ::= OmpForHeader() f3 ::=
	 * Statement()
	 */
	@Override
	public void visit(ForConstruct n) {
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <SECTIONS> f2 ::= NowaitDataClauseList() f3 ::=
	 * OmpEol() f4 ::= SectionsScope()
	 */
	@Override
	public void visit(SectionsConstruct n) {
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <SINGLE> f2 ::= SingleClauseList() f3 ::=
	 * OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SingleConstruct n) {
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <TASK> f2 ::= ( TaskClause() )* f3 ::=
	 * OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(TaskConstruct n) {
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <PARALLEL> f2 ::= <FOR> f3 ::=
	 * UniqueParallelOrUniqueForOrDataClauseList() f4 ::= OmpEol() f5 ::=
	 * OmpForHeader() f6 ::= Statement()
	 */
	@Override
	public void visit(ParallelForConstruct n) {
		siblingPar.add(n);
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <PARALLEL> f2 ::= <SECTIONS> f3 ::=
	 * UniqueParallelOrDataClauseList() f4 ::= OmpEol() f5 ::= SectionsScope()
	 */
	@Override
	public void visit(ParallelSectionsConstruct n) {
		siblingPar.add(n);
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <MASTER> f2 ::= OmpEol() f3 ::= Statement()
	 */
	@Override
	public void visit(MasterConstruct n) {
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <CRITICAL> f2 ::= ( RegionPhrase() )? f3 ::=
	 * OmpEol() f4 ::= Statement()
	 */
	@Override
	public void visit(CriticalConstruct n) {
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <ATOMIC> f2 ::= ( AtomicClause() )? f3 ::=
	 * OmpEol()
	 * f4 ::= ExpressionStatement()
	 */
	@Override
	public void visit(AtomicConstruct n) {
	}

	/**
	 * f0 ::= OmpPragma() f1 ::= <ORDERED> f2 ::= OmpEol() f3 ::= Statement()
	 */
	@Override
	public void visit(OrderedConstruct n) {
	}

	/**
	 * f0 ::= <IF> f1 ::= "(" f2 ::= Expression() f3 ::= ")" f4 ::= Statement()
	 * f5 ::=
	 * ( <ELSE> Statement() )?
	 */
	@Override
	public void visit(IfStatement n) {
	}

	/**
	 * f0 ::= <SWITCH> f1 ::= "(" f2 ::= Expression() f3 ::= ")" f4 ::=
	 * Statement()
	 */
	@Override
	public void visit(SwitchStatement n) {
	}

	/**
	 * f0 ::= <WHILE> f1 ::= "(" f2 ::= Expression() f3 ::= ")" f4 ::=
	 * Statement()
	 */
	@Override
	public void visit(WhileStatement n) {
	}

	/**
	 * f0 ::= <DO> f1 ::= Statement() f2 ::= <WHILE> f3 ::= "(" f4 ::=
	 * Expression()
	 * f5 ::= ")" f6 ::= ";"
	 */
	@Override
	public void visit(DoStatement n) {
	}

	/**
	 * f0 ::= <FOR> f1 ::= "(" f2 ::= ( Expression() )? f3 ::= ";" f4 ::= (
	 * Expression() )? f5 ::= ";" f6 ::= ( Expression() )? f7 ::= ")" f8 ::=
	 * Statement()
	 */
	@Override
	public void visit(ForStatement n) {
	}

}
