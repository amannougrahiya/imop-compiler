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
import imop.lib.util.Misc;

import java.util.Set;

/**
 * Provides default methods for processing all the CFG nodes.
 * Traversal shall be defined as per the requirements.
 * Note: This visitor is just for convenience in handling all the CFGs.
 */
public class LoopAndOmpProfiler extends DepthFirstProcess {
	int globalTab = 0;
	public StringBuilder str = new StringBuilder();

	private void printTabs() {
		for (int i = 0; i < globalTab; i++) {
			str.append("\t");
		}
	}

	private void printAccesses(Node n) {
		if (1 == 1) {
			return;
		}
		if (n.getInfo().getSharedAccesses().isEmpty()) {
			return;
		}
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("R(" + n.getInfo().getSharedReads() + "); W(" + n.getInfo().getSharedWrites() + ");\n");
	}

	private boolean shouldLeave(Node n) {
		Set<Node> lexicalContents = n.getInfo().getCFGInfo().getLexicalCFGContents();
		for (Node l : lexicalContents) {
			if (l instanceof OmpDirective || l instanceof OmpConstruct) {
				return false;
			}
			// if (!l.getInfo().getSharedAccesses().isEmpty()) {
			// return false;
			// }

		}
		return true;
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public void visit(FunctionDefinition n) {
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append(n.getInfo().getFunctionName() + "():\n");
		n.getF3().accept(this);
		endProcess(n);
		str.append("\n");
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(ParallelConstruct n) {
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("omp-parallel: \n");
		globalTab++;
		n.getParConsF2().accept(this);
		globalTab--;
		endProcess(n);
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(IfClause n) {
		initProcess(n);
		printAccesses(n);
		endProcess(n);
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(NumThreadsClause n) {
		initProcess(n);
		printAccesses(n);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(ForConstruct n) {
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("omp-for:\n");
		globalTab++;
		printAccesses(n.getInfo().getCFGInfo().getInitExpression());
		printAccesses(n.getInfo().getCFGInfo().getForConditionExpression());
		printAccesses(n.getInfo().getCFGInfo().getReinitExpression());
		n.getF3().accept(this);
		globalTab--;
		endProcess(n);
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
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("omp-sections:\n");
		globalTab++;
		for (Statement s : n.getInfo().getCFGInfo().getSectionList()) {
			str.append(Misc.getLineNum(n) + ": ");
			printTabs();
			str.append("section:\n");
			globalTab++;
			s.accept(this);
			globalTab--;

		}
		globalTab--;
		endProcess(n);
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
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("omp-single:\n");
		globalTab++;
		n.getInfo().getCFGInfo().getBody().accept(this);
		globalTab--;
		endProcess(n);
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
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("omp-task:\n");
		globalTab++;
		n.getInfo().getCFGInfo().getBody().accept(this);
		globalTab--;
		endProcess(n);
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(FinalClause n) {
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		printAccesses(n);
		endProcess(n);
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
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("omp-par-for:\n");
		globalTab++;
		n.getInfo().getCFGInfo().getBody().accept(this);
		globalTab--;
		endProcess(n);
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
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("omp-par-sections: \n");
		globalTab++;
		n.getInfo().getCFGInfo().getBody().accept(this);
		globalTab--;
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(MasterConstruct n) {
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("omp-master: \n");
		globalTab++;
		n.getInfo().getCFGInfo().getBody().accept(this);
		globalTab--;
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(CriticalConstruct n) {
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("omp-critical: \n");
		globalTab++;
		n.getInfo().getCFGInfo().getBody().accept(this);
		globalTab--;
		endProcess(n);
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
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("omp-atomic: \n");
		globalTab++;
		n.getInfo().getCFGInfo().getStatement();
		globalTab--;
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public void visit(FlushDirective n) {
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("-- FLUSH --\n");
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(OrderedConstruct n) {
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("omp-ordered: \n");
		globalTab++;
		n.getInfo().getCFGInfo().getBody().accept(this);
		globalTab--;
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(BarrierDirective n) {
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("-- BARRIER --\n");
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskwaitDirective n) {
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("taskwait;\n");
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskyieldDirective n) {
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("taskyield;\n");
		endProcess(n);
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ExpressionStatement n) {
		initProcess(n);
		printAccesses(n);
		endProcess(n);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public void visit(CompoundStatement n) {
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		// globalTab++;
		for (Node s : n.getInfo().getCFGInfo().getElementList()) {
			s.accept(this);
		}
		// globalTab--;
		endProcess(n);
	}

	// /**
	// * f0 ::= <IF>
	// * f1 ::= "("
	// * f2 ::= Expression()
	// * f3 ::= ")"
	// * f4 ::= Statement()
	// * f5 ::= ( <ELSE> Statement() )?
	// */
	// @Override
	// public void visit(IfStatement n) {
	// if (shouldLeave(n)) {
	// return;
	// }
	// initProcess(n);
	// str.append(Misc.getLineNum(n) + ": ");
	// printTabs();
	// str.append( "if: \n");
	// globalTab++;
	// printAccesses(n.getInfo().getCFGInfo().getPredicate());
	// str.append(Misc.getLineNum(n) + ": ");
	// printTabs();
	// str.append( "T:\n");
	// n.getInfo().getCFGInfo().getThenBody().accept(this);
	// globalTab--;
	// if (n.getInfo().getCFGInfo().hasElseBody()) {
	// globalTab++;
	// str.append( "F:\n");
	// n.getInfo().getCFGInfo().getElseBody().accept(this);
	// globalTab--;
	// }
	//
	// endProcess(n);
	// }
	//
	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SwitchStatement n) {
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("switch: \n");
		globalTab++;
		printAccesses(n.getInfo().getCFGInfo().getPredicate());
		n.getInfo().getCFGInfo().getBody().accept(this);
		globalTab--;
		endProcess(n);
	}

	/**
	 * f0 ::= <WHILE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(WhileStatement n) {
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("while-loop: \n");
		globalTab++;
		printAccesses(n.getInfo().getCFGInfo().getPredicate());
		n.getInfo().getCFGInfo().getBody().accept(this);
		globalTab--;
		endProcess(n);
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
	public void visit(DoStatement n) {
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("do-loop: \n");
		globalTab++;
		n.getInfo().getCFGInfo().getBody().accept(this);
		printAccesses(n.getInfo().getCFGInfo().getPredicate());
		globalTab--;
		endProcess(n);
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
	public void visit(ForStatement n) {
		if (shouldLeave(n)) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append("for-loop: \n");
		globalTab++;
		printAccesses(n.getInfo().getCFGInfo().getInitExpression());
		printAccesses(n.getInfo().getCFGInfo().getTerminationExpression());
		printAccesses(n.getInfo().getCFGInfo().getStepExpression());
		n.getInfo().getCFGInfo().getBody().accept(this);
		globalTab--;
		endProcess(n);
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public void visit(GotoStatement n) {
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append(n + "\n");
		endProcess(n);
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ContinueStatement n) {
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append(n + "\n");
		endProcess(n);
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(BreakStatement n) {
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append(n + "\n");
		endProcess(n);
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(ReturnStatement n) {
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append(n + "\n");
		endProcess(n);
	}

	@Override
	public void visit(CallStatement n) {
		if (1 == 1) {
			return;
		}
		initProcess(n);
		str.append(Misc.getLineNum(n) + ": ");
		printTabs();
		str.append(n + "\n");
		endProcess(n);
	}

}
