/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.baseVisitor.cfgTraversals;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.GJVoidDepthFirstProcess;

/**
 * Provides default methods for processing all the CFG nodes.
 * Traversal shall be defined as per the requirements.
 * Note: This visitor is just for convenience in handling all the CFGs.
 */
public class GJNoArguDepthFirstCFG<R> extends GJVoidDepthFirstProcess<R> {
	public R initProcess(Node n) {
		return null;
	}

	public R endProcess(Node n) {
		return null;
	}

	/**
	 * @deprecated
	 * @param n
	 * @return
	 */
	@Deprecated
	public R processCalls(Node n) {
		return null;
	}

	/**
	 * Special Node
	 */
	public R visit(BeginNode n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * Special Node
	 */
	public R visit(EndNode n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	public R visit(FunctionDefinition n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	public R visit(Declaration n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ParameterAbstraction()
	 */
	public R visit(ParameterDeclaration n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	public R visit(UnknownCpp n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	public R visit(ParallelConstruct n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	public R visit(UnknownPragma n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	public R visit(IfClause n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	public R visit(NumThreadsClause n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	public R visit(ForConstruct n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	public R visit(OmpForInitExpression n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	public R visit(OmpForCondition n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= PostIncrementId()
	 * | PostDecrementId()
	 * | PreIncrementId()
	 * | PreDecrementId()
	 * | ShortAssignPlus()
	 * | ShortAssignMinus()
	 * | OmpForAdditive()
	 * | OmpForSubtractive()
	 * | OmpForMultiplicative()
	 */
	public R visit(OmpForReinitExpression n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTIONS>
	 * f2 ::= NowaitDataClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= SectionsScope()
	 */
	public R visit(SectionsConstruct n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SINGLE>
	 * f2 ::= SingleClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	public R visit(SingleConstruct n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASK>
	 * f2 ::= ( TaskClause() )*
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	public R visit(TaskConstruct n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	public R visit(FinalClause n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
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
	public R visit(ParallelForConstruct n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <PARALLEL>
	 * f2 ::= <SECTIONS>
	 * f3 ::= UniqueParallelOrDataClauseList()
	 * f4 ::= OmpEol()
	 * f5 ::= SectionsScope()
	 */
	public R visit(ParallelSectionsConstruct n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	public R visit(MasterConstruct n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	public R visit(CriticalConstruct n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= ExpressionStatement()
	 */
	public R visit(AtomicConstruct n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	public R visit(FlushDirective n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	public R visit(OrderedConstruct n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	public R visit(BarrierDirective n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	public R visit(TaskwaitDirective n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	public R visit(TaskyieldDirective n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	public R visit(ExpressionStatement n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	public R visit(CompoundStatement n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 * f5 ::= ( <ELSE> Statement() )?
	 */
	public R visit(IfStatement n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	public R visit(SwitchStatement n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <WHILE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	public R visit(WhileStatement n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <DO>
	 * f1 ::= Statement()
	 * f2 ::= <WHILE>
	 * f3 ::= "("
	 * f4 ::= Expression()
	 * f5 ::= ")"
	 * f6 ::= ";"
	 */
	public R visit(DoStatement n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <FOR>
	 * f1 ::= "("
	 * f2 ::= ( Expression() )?
	 * f3 ::= ";"
	 * f4 ::= ( Expression() )?
	 * f5 ::= ";"
	 * f6 ::= ( Expression() )?
	 * f7 ::= ")"
	 * f8 ::= Statement()
	 */
	public R visit(ForStatement n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	public R visit(GotoStatement n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	public R visit(ContinueStatement n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	public R visit(BreakStatement n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	public R visit(ReturnStatement n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	public R visit(Expression n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	public R visit(DummyFlushDirective n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	public R visit(PreCallNode n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	public R visit(PostCallNode n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}

	public R visit(CallStatement n) {
		R _ret = null;
		initProcess(n);
		endProcess(n);
		return _ret;
	}
}
