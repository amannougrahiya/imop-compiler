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
import imop.baseVisitor.GJDepthFirstProcess;

/**
 * Provides default methods for processing all the CFG nodes.
 * Traversal shall be defined as per the requirements.
 * Note: This visitor is just for convenience in handling all the CFGs.
 */
public abstract class GJDepthFirstCFG<R, A> extends GJDepthFirstProcess<R, A> {
	@Override
	public R initProcess(Node n, A argu) {
		return null;
	}

	@Override
	public R endProcess(Node n, A argu) {
		return null;
	}

	/**
	 * @deprecated
	 * @param n
	 * @param argu
	 * @return
	 */
	@Deprecated
	public R processCalls(Node n, A argu) {
		return null;
	}

	/**
	 * Special Node
	 */
	@Override
	abstract public R visit(BeginNode n, A argu);
	// {
	// R _ret=null;
	// initProcess(n, argu);
	// endProcess(n, argu);
	// return _ret;
	// }

	/**
	 * Special Node
	 */
	@Override
	public R visit(EndNode n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public R visit(FunctionDefinition n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public R visit(Declaration n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ParameterAbstraction()
	 */
	@Override
	public R visit(ParameterDeclaration n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public R visit(UnknownCpp n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public R visit(ParallelConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public R visit(UnknownPragma n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(IfClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(NumThreadsClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public R visit(ForConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	@Override
	public R visit(OmpForInitExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	@Override
	public R visit(OmpForCondition n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
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
	@Override
	public R visit(OmpForReinitExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTIONS>
	 * f2 ::= NowaitDataClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= SectionsScope()
	 */
	@Override
	public R visit(SectionsConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SINGLE>
	 * f2 ::= SingleClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public R visit(SingleConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASK>
	 * f2 ::= ( TaskClause() )*
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public R visit(TaskConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(FinalClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
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
	@Override
	public R visit(ParallelForConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
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
	@Override
	public R visit(ParallelSectionsConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public R visit(MasterConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public R visit(CriticalConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= ExpressionStatement()
	 */
	@Override
	public R visit(AtomicConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public R visit(FlushDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public R visit(OrderedConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public R visit(BarrierDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public R visit(TaskwaitDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public R visit(TaskyieldDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public R visit(ExpressionStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public R visit(CompoundStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
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
	@Override
	public R visit(IfStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public R visit(SwitchStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <WHILE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public R visit(WhileStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
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
	@Override
	public R visit(DoStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
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
	@Override
	public R visit(ForStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public R visit(GotoStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public R visit(ContinueStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public R visit(BreakStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public R visit(ReturnStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public R visit(Expression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(DummyFlushDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(PreCallNode n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(PostCallNode n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(CallStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}
}
