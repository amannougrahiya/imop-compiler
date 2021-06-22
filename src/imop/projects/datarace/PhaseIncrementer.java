/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.projects.datarace;

import java.io.ByteArrayInputStream;

import imop.ast.node.AtomicConstruct;
import imop.ast.node.BarrierDirective;
import imop.ast.node.CompoundStatement;
import imop.ast.node.CriticalConstruct;
import imop.ast.node.ExpressionStatement;
import imop.ast.node.ForConstruct;
import imop.ast.node.IterationStatement;
import imop.ast.node.JumpStatement;
import imop.ast.node.LabeledStatement;
import imop.ast.node.MasterConstruct;
import imop.ast.node.Node;
import imop.ast.node.OmpConstruct;
import imop.ast.node.OmpDirective;
import imop.ast.node.OrderedConstruct;
import imop.ast.node.ParallelConstruct;
import imop.ast.node.ParallelForConstruct;
import imop.ast.node.ParallelSectionsConstruct;
import imop.ast.node.SectionsConstruct;
import imop.ast.node.SelectionStatement;
import imop.ast.node.SingleConstruct;
import imop.ast.node.Statement;
import imop.ast.node.TaskConstruct;
import imop.ast.node.UnknownCpp;
import imop.ast.node.UnknownPragma;
import imop.baseVisitor.GJDepthFirst;
import imop.lib.getter.StringGetter;
import imop.parser.CParser;

/**
 * This visitor attaches the statements which increment the value of
 * phaseCounter at correct places.
 */
public class PhaseIncrementer extends GJDepthFirst<Object, Object> {
	public DRCommonUtils commUtil = new DRCommonUtils();

	/**
	 * f0 ::= ( LabeledStatement() | ExpressionStatement() | CompoundStatement()
	 * | SelectionStatement()
	 * | IterationStatement() | JumpStatement() | UnknownPragma() |
	 * OmpConstruct() | OmpDirective() | UnknownCpp() )
	 */
	@Override
	public Object visit(Statement n, Object argu) {
		Node newNode = (Node) n.getStmtF0().accept(this, argu);
		n.getStmtF0().choice = newNode;
		return null;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public Object visit(UnknownCpp n, Object argu) {
		return n;
	}

	/**
	 * f0 ::= BarrierDirective()
	 * | TaskwaitDirective()
	 * | TaskyieldDirective()
	 * | FlushDirective()
	 */
	@Override
	public Object visit(OmpDirective n, Object argu) {
		Node newNode = (Node) n.getOmpDirF0().accept(this, argu);
		if (newNode == null) {
			return n;
		} else if (newNode.getClass().getSimpleName().equals("CompoundStatement")) {
			return newNode;
		}
		return n;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public Object visit(BarrierDirective n, Object argu) {
		StringGetter strGetter = new StringGetter();
		n.accept(strGetter);
		String newCSString = "{" + strGetter.str + commUtil.getPhaseIncrementer() + "}";
		return CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()), CompoundStatement.class);
	}

	/**
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
	@Override
	public Object visit(OmpConstruct n, Object argu) {
		Node newNode = (Node) n.getOmpConsF0().accept(this, argu);
		// If this newNode is a CompoundStatement, then return this to the parent, else return n
		if (newNode.getClass().getSimpleName().equals("CompoundStatement")) {
			return newNode;
		}
		return n;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public Object visit(ParallelConstruct n, Object argu) {
		n.getParConsF2().accept(this, argu);

		StringGetter strGetter = new StringGetter();
		n.accept(strGetter);
		String newCSString = "{" + strGetter.str + commUtil.getPhaseIncrementer() + "}";
		return CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()), CompoundStatement.class);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public Object visit(UnknownPragma n, Object argu) {
		return n;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public Object visit(ForConstruct n, Object argu) {
		n.getF3().accept(this, argu);

		CrudeHasNowait crude = new CrudeHasNowait();
		n.getF1().accept(crude);

		if (!crude.foundNowait) {
			StringGetter strGetter = new StringGetter();
			n.accept(strGetter);
			String newCSString = "{" + strGetter.str + commUtil.getPhaseIncrementer() + "}";
			return CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()),
					CompoundStatement.class);
		}
		return null;
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
	public Object visit(ParallelForConstruct n, Object argu) {
		n.getF6().accept(this, argu);

		StringGetter strGetter = new StringGetter();
		n.accept(strGetter);
		String newCSString = "{" + strGetter.str + commUtil.getPhaseIncrementer() + "}";
		return CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()), CompoundStatement.class);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTIONS>
	 * f2 ::= NowaitDataClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= SectionsScope()
	 */
	@Override
	public Object visit(SectionsConstruct n, Object argu) {
		n.getF4().accept(this, argu);

		CrudeHasNowait crude = new CrudeHasNowait();
		n.getF2().accept(crude);

		if (!crude.foundNowait) {
			StringGetter strGetter = new StringGetter();
			n.accept(strGetter);
			String newCSString = "{" + strGetter.str + commUtil.getPhaseIncrementer() + "}";
			return CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()),
					CompoundStatement.class);
		}
		return null;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SINGLE>
	 * f2 ::= SingleClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public Object visit(SingleConstruct n, Object argu) {
		n.getF4().accept(this, argu);

		CrudeHasNowait crude = new CrudeHasNowait();
		n.getF2().accept(crude);

		if (!crude.foundNowait) {
			StringGetter strGetter = new StringGetter();
			n.accept(strGetter);
			String newCSString = "{" + strGetter.str + commUtil.getPhaseIncrementer() + "}";
			return CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()),
					CompoundStatement.class);
		}
		return null;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASK>
	 * f2 ::= ( TaskClause() )*
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public Object visit(TaskConstruct n, Object argu) {
		n.getF4().accept(this, argu);
		return n;
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
	public Object visit(ParallelSectionsConstruct n, Object argu) {
		n.getF5().accept(this, argu);

		StringGetter strGetter = new StringGetter();
		n.accept(strGetter);
		String newCSString = "{" + strGetter.str + commUtil.getPhaseIncrementer() + "}";
		return CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()), CompoundStatement.class);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public Object visit(MasterConstruct n, Object argu) {
		n.getF3().accept(this, argu);
		return n;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public Object visit(CriticalConstruct n, Object argu) {
		n.getF4().accept(this, argu);
		return n;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public Object visit(AtomicConstruct n, Object argu) {
		return n;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public Object visit(OrderedConstruct n, Object argu) {
		n.getF3().accept(this, argu);
		return n;
	}

	/**
	 * f0 ::= SimpleLabeledStatement()
	 * | CaseLabeledStatement()
	 * | DefaultLabeledStatement()
	 */
	@Override
	public Object visit(LabeledStatement n, Object argu) {
		n.getLabStmtF0().accept(this, argu);
		return n;
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public Object visit(ExpressionStatement n, Object argu) {
		return n;
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public Object visit(CompoundStatement n, Object argu) {
		n.getF1().accept(this, null);
		return n;
	}

	/**
	 * f0 ::= IfStatement()
	 * | SwitchStatement()
	 */
	@Override
	public Object visit(SelectionStatement n, Object argu) {
		n.getSelStmtF0().accept(this, null);
		return n;
	}

	/**
	 * f0 ::= WhileStatement()
	 * | DoStatement()
	 * | ForStatement()
	 */
	@Override
	public Object visit(IterationStatement n, Object argu) {
		n.getItStmtF0().accept(this, argu);
		return n;
	}

	/**
	 * f0 ::= GotoStatement()
	 * | ContinueStatement()
	 * | BreakStatement()
	 * | ReturnStatement()
	 */
	@Override
	public Object visit(JumpStatement n, Object argu) {
		return n;
	}
	
	@Override
	public Object visit(CallStatement n, Object argu) {
		return n;
	}
}
