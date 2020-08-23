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
public class GJVoidDepthFirstCFG<A> extends GJVoidDepthFirstProcess<A> {
	@Override
	public void initProcess(Node n, A argu) {
		return;
	}

	@Override
	public void endProcess(Node n, A argu) {
		return;
	}

	/**
	 * @deprecated
	 * @param n
	 * @param argu
	 */
	@Deprecated
	public void processCalls(Node n, A argu) {

	}

	/**
	 * Special Node
	 */
	@Override
	public void visit(BeginNode n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * Special Node
	 */
	@Override
	public void visit(EndNode n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public void visit(FunctionDefinition n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(Declaration n, A argu) {
		initProcess(n, argu);
		processCalls(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ParameterAbstraction()
	 */
	@Override
	public void visit(ParameterDeclaration n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public void visit(UnknownCpp n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(ParallelConstruct n, A argu) {
		initProcess(n, argu);
		processCalls(n.getParConsF1(), argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public void visit(UnknownPragma n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(IfClause n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(NumThreadsClause n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(ForConstruct n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForInitExpression n, A argu) {
		initProcess(n, argu);
		processCalls(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	@Override
	public void visit(OmpForCondition n, A argu) {
		initProcess(n, argu);
		processCalls(n, argu);
		endProcess(n, argu);
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
	public void visit(OmpForReinitExpression n, A argu) {
		initProcess(n, argu);
		processCalls(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTIONS>
	 * f2 ::= NowaitDataClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= SectionsScope()
	 */
	@Override
	public void visit(SectionsConstruct n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SINGLE>
	 * f2 ::= SingleClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SingleConstruct n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASK>
	 * f2 ::= ( TaskClause() )*
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(TaskConstruct n, A argu) {
		initProcess(n, argu);
		processCalls(n.getF2(), argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(FinalClause n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
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
	public void visit(ParallelForConstruct n, A argu) {
		initProcess(n, argu);
		processCalls(n.getF3(), argu);
		endProcess(n, argu);
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
	public void visit(ParallelSectionsConstruct n, A argu) {
		initProcess(n, argu);
		processCalls(n.getF3(), argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(MasterConstruct n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(CriticalConstruct n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= ExpressionStatement()
	 */
	@Override
	public void visit(AtomicConstruct n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public void visit(FlushDirective n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(OrderedConstruct n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(BarrierDirective n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskwaitDirective n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskyieldDirective n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ExpressionStatement n, A argu) {
		initProcess(n, argu);
		processCalls(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public void visit(CompoundStatement n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
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
	public void visit(IfStatement n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SwitchStatement n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <WHILE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(WhileStatement n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
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
	public void visit(DoStatement n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
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
	public void visit(ForStatement n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public void visit(GotoStatement n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ContinueStatement n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(BreakStatement n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(ReturnStatement n, A argu) {
		initProcess(n, argu);
		processCalls(n, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(Expression n, A argu) {
		initProcess(n, argu);
		processCalls(n, argu);
		endProcess(n, argu);
	}

	@Override
	public void visit(DummyFlushDirective n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	@Override
	public void visit(PreCallNode n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	@Override
	public void visit(PostCallNode n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	@Override
	public void visit(CallStatement n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}
}
