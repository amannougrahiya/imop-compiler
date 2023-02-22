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
import java.util.List;
import java.util.Vector;

import imop.ast.node.ASection;
import imop.ast.node.AtomicConstruct;
import imop.ast.node.CallStatement;
import imop.ast.node.CaseLabeledStatement;
import imop.ast.node.CompoundStatement;
import imop.ast.node.CompoundStatementElement;
import imop.ast.node.CriticalConstruct;
import imop.ast.node.DefaultLabeledStatement;
import imop.ast.node.DoStatement;
import imop.ast.node.ExpressionStatement;
import imop.ast.node.ForConstruct;
import imop.ast.node.ForStatement;
import imop.ast.node.IfStatement;
import imop.ast.node.IterationStatement;
import imop.ast.node.JumpStatement;
import imop.ast.node.LabeledStatement;
import imop.ast.node.MasterConstruct;
import imop.ast.node.Node;
import imop.ast.node.NodeSequence;
import imop.ast.node.OmpConstruct;
import imop.ast.node.OmpDirective;
import imop.ast.node.OrderedConstruct;
import imop.ast.node.ParallelConstruct;
import imop.ast.node.ParallelForConstruct;
import imop.ast.node.ParallelSectionsConstruct;
import imop.ast.node.ReturnStatement;
import imop.ast.node.SectionsConstruct;
import imop.ast.node.SelectionStatement;
import imop.ast.node.SimpleLabeledStatement;
import imop.ast.node.SingleConstruct;
import imop.ast.node.Statement;
import imop.ast.node.SwitchStatement;
import imop.ast.node.TaskConstruct;
import imop.ast.node.UnknownCpp;
import imop.ast.node.UnknownPragma;
import imop.ast.node.WhileStatement;
import imop.baseVisitor.GJDepthFirst;
import imop.lib.getter.StringGetter;
import imop.parser.CParser;

/**
 * This visitor attaches the instrumentation print statements
 * to appropriate places.
 * These print statements print the log of the accesses made in the next
 * statement.
 */
public class Instrumentor extends GJDepthFirst<Object, Object> {
	public DRCommonUtils commUtil = new DRCommonUtils();

	/**
	 * This method is used to remove duplicates from the given accessList
	 * 
	 * @param accessList
	 * @return
	 */
	void removeDuplicateAccesses(Vector<Access> accessList) {
		// Can't use the for-each loop due to the fail-fast nature of Vector's iterators
		int index = 0;
		while (index != accessList.size()) {
			Access currAcc = accessList.get(index);
			int indexIn = index + 1;
			while (indexIn < accessList.size()) {
				Access loopAcc = accessList.get(indexIn);
				String parenthesesString = "( " + currAcc.location + ") ";
				if (loopAcc.location.equals(currAcc.location) || parenthesesString.equals(loopAcc.location)) {
					if (loopAcc.mode == currAcc.mode) {
						accessList.remove(loopAcc);
						continue;
					}
				}
				if ((loopAcc.location.equals(currAcc.location + " ")
						|| ((loopAcc.location + " ").equals(currAcc.location))
						|| loopAcc.location.equals(currAcc.location)) && (loopAcc.mode == 1 || currAcc.mode == 1)) {
					currAcc.mode = 1;
					accessList.remove(loopAcc);
					continue;
				}
				indexIn++;
			}
			index++;
		}
	}

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
		return n;
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
	 * f0 ::= BarrierDirective()
	 * | TaskwaitDirective()
	 * | TaskyieldDirective()
	 * | FlushDirective()
	 */
	@Override
	public Object visit(OmpDirective n, Object argu) {
		return n;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public Object visit(ParallelConstruct n, Object argu) {
		Node newNode = (Node) n.getParConsF2().accept(this, argu);
		n.setParConsF2((Statement) newNode); // Assuming statement returns its substitution, use it

		// 	// Read the accesses in parallelDirective and add it with this statement
		// 	// to create a block to be sent back.
		// 	Vector<Access> accessList = new Vector<Access>();
		// 	n.f1.accept(new AccessBuilder(), accessList);
		// 	removeDuplicateAccesses(accessList);
		// 	if(accessList.size() != 0) {
		// 		// Obtain string of sentence
		// 		StringGetter strGetter = new StringGetter();
		// 		n.f1.accept(strGetter);

		// 		// Obtain the final statement to be used
		// 		String log = commUtil.getPrintLog(accessList, strGetter.str);
		// 		strGetter = new StringGetter();
		// 		n.accept(strGetter);
		// 		log = "{" + log + strGetter.str + "}";
		// 		try {
		// 			CompoundStatement newCS = (CompoundStatement) new CParser(new ByteArrayInputStream(log.getBytes())).CompoundStatement();
		// 			return newCS;
		// 		}
		// 		catch (ParseException e) {
		// 			e.printStackTrace();
		// 		}
		// 	}
		return n;
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
		Statement newSt = (Statement) n.getF3().accept(this, argu);
		n.setF3(newSt);

		// Put accesses of OmpForHeader.OmpForCondition.choice.Expression at the end of statements in n.f3
		// AND Put accesses of OmpForHeader.OmpForReinitExpression.choice.Expression at the end of the statements in n.f3
		Vector<Access> accessList = new Vector<>();
		n.getF2().getF4().accept(new AccessBuilder(), accessList);
		n.getF2().getF6().accept(new AccessBuilder(), accessList);
		removeDuplicateAccesses(accessList);

		StringGetter strGetter;
		if (accessList.size() != 0) {
			// Obtain the CS to be put at the end of n.f3;
			strGetter = new StringGetter();

			String logTemp = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n.getF2()));
			String log = "{" + logTemp + "}";
			CompoundStatement newCSAtEnd = (CompoundStatement) CParser
					.createCrudeASTNode(new ByteArrayInputStream(log.getBytes()), CompoundStatement.class);

			if (n.getF3().getStmtF0().choice.getClass().getSimpleName().equals("CompoundStatement")) {
				// Add these new elements to the end of n.f3's CompoundStatement
				for (Node element : newCSAtEnd.getF1().nodes) {
					((CompoundStatement) n.getF3().getStmtF0().choice).getF1().nodes.add(element);
				}
			} else {
				// Create a new statement and replace n.f3
				strGetter = new StringGetter();
				n.getF3().accept(strGetter);
				String newStmtString = "{" + strGetter.str;
				newStmtString += logTemp + "}";
				Statement newStmt = (Statement) CParser
						.createCrudeASTNode(new ByteArrayInputStream(newStmtString.getBytes()), Statement.class);
				n.setF3(newStmt);
			}
		}

		// Put accesses of OmpForHeader.OmpForInitExpression.Expression at the beginning of this complete block
		// AND Put accesses of OmpForHeader.OmpForCondition.choice.Expression at the end of statements in n.f3
		accessList = new Vector<>();
		n.getF2().getF2().accept(new AccessBuilder(), accessList);
		n.getF2().getF4().accept(new AccessBuilder(), accessList);
		removeDuplicateAccesses(accessList);
		if (accessList.size() != 0) {
			strGetter = new StringGetter();
			String log = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n.getF2()));

			strGetter = new StringGetter();
			n.accept(strGetter);
			String newCSString = "{" + log + strGetter.str + "}";
			return (CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()),
					CompoundStatement.class));
		}
		return n;
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
		Statement newSt = (Statement) n.getF6().accept(this, argu);
		n.setF6(newSt);

		// Put accesses of OmpForHeader.OmpForCondition.choice.Expression at the end of statements in n.f6
		// AND Put accesses of OmpForHeader.OmpForReinitExpression.choice.Expression at the end of the statements in n.f6
		Vector<Access> accessList = new Vector<>();
		n.getF5().getF4().accept(new AccessBuilder(), accessList);
		n.getF5().getF6().accept(new AccessBuilder(), accessList);
		removeDuplicateAccesses(accessList);

		StringGetter strGetter;
		if (accessList.size() != 0) {
			// Obtain the CS to be put at the end of n.f6;
			strGetter = new StringGetter();

			String logTemp = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n.getF5()));
			String log = "{" + logTemp + "}";
			CompoundStatement newCSAtEnd = (CompoundStatement) CParser
					.createCrudeASTNode(new ByteArrayInputStream(log.getBytes()), CompoundStatement.class);

			if (n.getF6().getStmtF0().choice.getClass().getSimpleName().equals("CompoundStatement")) {
				// Add these new elements to the end of n.f6's CompoundStatement
				for (Node element : newCSAtEnd.getF1().nodes) {
					((CompoundStatement) n.getF6().getStmtF0().choice).getF1().nodes.add(element);
				}
			} else {
				// Create a new statement and replace n.f6
				strGetter = new StringGetter();
				n.getF6().accept(strGetter);
				String newStmtString = "{" + strGetter.str;
				newStmtString += logTemp + "}";
				Statement newStmt = (Statement) CParser
						.createCrudeASTNode(new ByteArrayInputStream(newStmtString.getBytes()), Statement.class);
				n.setF6(newStmt);
			}
		}

		// Put accesses of OmpForHeader.OmpForInitExpression.Expression at the beginning of this complete block
		// AND Put accesses of OmpForHeader.OmpForCondition.choice.Expression at the end of statements in n.f6
		accessList = new Vector<>();
		n.getF5().getF2().accept(new AccessBuilder(), accessList);
		n.getF5().getF4().accept(new AccessBuilder(), accessList);
		removeDuplicateAccesses(accessList);
		if (accessList.size() != 0) {
			strGetter = new StringGetter();
			String log = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n.getF5()));

			strGetter = new StringGetter();
			n.accept(strGetter);
			String newCSString = "{" + log + strGetter.str + "}";
			return (CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()),
					CompoundStatement.class));
		}
		return n;
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
		return n;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTION>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public Object visit(ASection n, Object argu) {
		Object _ret = null;
		n.setF3((Statement) n.getF3().accept(this, argu));
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
	public Object visit(SingleConstruct n, Object argu) {
		n.setF4((Statement) n.getF4().accept(this, argu));
		return n;
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
		n.setF4((Statement) n.getF4().accept(this, argu));
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
		return n;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public Object visit(MasterConstruct n, Object argu) {
		n.setF3((Statement) n.getF3().accept(this, argu));
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
		n.setF4((Statement) n.getF4().accept(this, argu));
		return n;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= ExpressionStatement()
	 */
	@Override
	public Object visit(AtomicConstruct n, Object argu) {
		Vector<Access> accessList = new Vector<>();
		n.getF4().accept(new AccessBuilder(), accessList);
		if (accessList.size() != 0) {
			removeDuplicateAccesses(accessList);
		}
		if (accessList.size() != 0) {
			StringGetter strGetter = new StringGetter();
			n.getF4().accept(strGetter);
			String log = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n.getF4()));

			strGetter = new StringGetter();
			n.accept(strGetter);
			String newCSString = "{" + log + strGetter.str + "}";
			return CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()),
					CompoundStatement.class);
		}
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
		n.setF3((Statement) n.getF3().accept(this, argu));
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
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public Object visit(SimpleLabeledStatement n, Object argu) {
		n.setF2((Statement) n.getF2().accept(this, argu));
		return null;
	}

	/**
	 * f0 ::= <CASE>
	 * f1 ::= ConstantExpression()
	 * f2 ::= ":"
	 * f3 ::= Statement()
	 */
	@Override
	public Object visit(CaseLabeledStatement n, Object argu) {
		n.setF3((Statement) n.getF3().accept(this, argu));
		return null;
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public Object visit(DefaultLabeledStatement n, Object argu) {
		n.setF2((Statement) n.getF2().accept(this, argu));
		return null;
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public Object visit(ExpressionStatement n, Object argu) {
		Vector<Access> accessList = new Vector<>();
		n.getF0().accept(new AccessBuilder(), accessList);
		removeDuplicateAccesses(accessList);
		if (accessList.size() != 0) {
			StringGetter strGetter = new StringGetter();
			n.accept(strGetter);
			String log = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n));
			String newCSString = "{" + log + strGetter.str + "}";
			return (CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()),
					CompoundStatement.class));
		}
		return n;
	}

	@Override
	public Object visit(CallStatement n, Object argu) {
		// Not yet implemented.
		return n;
	}
	
	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public Object visit(CompoundStatement n, Object argu) {
		Object _ret = null;
		// For all the declarations, add a new printStatement before it, if required.
		// Don't use iterators.
		List<Node> list = n.getF1().nodes;
		int i = 0;
		while (i != list.size()) {
			Node element = ((CompoundStatementElement) list.get(i)).getF0().choice;
			Node retElem = (Node) element.accept(this, argu);
			if (element.getClass().getSimpleName().equals("Statement")) {
				i++;
				continue;
			}

			Vector<Access> accessList = new Vector<>();
			element.accept(new AccessBuilder(), accessList);
			removeDuplicateAccesses(accessList);
			if (accessList.isEmpty()) { // If there are no logs to print
				i++;
				continue;
			}

			// Get string for the declaration
			StringGetter strGetter = new StringGetter();
			element.accept(strGetter);
			String log = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(element));

			// Obtain a block of statements to be added
			log = "{" + log + "}";
			CompoundStatementElement newBlock = (CompoundStatementElement) CParser
					.createCrudeASTNode(new ByteArrayInputStream(log.getBytes()), CompoundStatementElement.class);
			// Add this block to just before the declaration
			list.add(i, newBlock);
			i += 2;
			continue;
		}
		return n;
	}

	/**
	 * f0 ::= Declaration()
	 * | Statement()
	 */
	@Override
	public Object visit(CompoundStatementElement n, Object argu) {
		Node newNode = (Node) n.getF0().accept(this, argu);
		if (newNode.getClass().getSimpleName().equals("Statement")) {
			n.getF0().choice = newNode;
			return null;
		}
		return null;
	}

	/**
	 * f0 ::= IfStatement()
	 * | SwitchStatement()
	 */
	@Override
	public Object visit(SelectionStatement n, Object argu) {
		Node newNode = (Node) n.getSelStmtF0().accept(this, argu);
		if (newNode.getClass().getSimpleName().equals("CompoundStatement")) {
			return newNode;
		}
		return n;
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
	public Object visit(IfStatement n, Object argu) {
		n.setF4((Statement) n.getF4().accept(this, argu));
		if (n.getF5().present()) {
			List<Node> list = ((NodeSequence) n.getF5().node).nodes;
			Node retNode = (Node) list.get(1).accept(this, argu);
			list.remove(1);
			list.add(1, retNode);
		}

		Vector<Access> accessList = new Vector<>();
		n.getF2().accept(new AccessBuilder(), accessList);
		removeDuplicateAccesses(accessList);
		if (accessList.size() != 0) {
			StringGetter strGetter = new StringGetter();
			n.getF2().accept(strGetter);
			String log = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n.getF2()));

			strGetter = new StringGetter();
			n.accept(strGetter);
			String newCSString = "{" + log + strGetter.str + "}";
			return CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()),
					CompoundStatement.class);
		}
		return n;
	}

	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public Object visit(SwitchStatement n, Object argu) {
		n.setF4((Statement) n.getF4().accept(this, argu));

		Vector<Access> accessList = new Vector<>();
		n.getF2().accept(new AccessBuilder(), accessList);
		removeDuplicateAccesses(accessList);
		if (accessList.size() != 0) {
			StringGetter strGetter = new StringGetter();
			n.getF2().accept(strGetter);
			String log = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n.getF2()));

			strGetter = new StringGetter();
			n.accept(strGetter);
			String newCSString = "{" + log + strGetter.str + "}";
			return CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()),
					CompoundStatement.class);
		}
		return n;
	}

	/**
	 * f0 ::= WhileStatement()
	 * | DoStatement()
	 * | ForStatement()
	 */
	@Override
	public Object visit(IterationStatement n, Object argu) {
		Node newNode = (Node) n.getItStmtF0().accept(this, argu);
		if (newNode.getClass().getSimpleName().equals("CompoundStatement")) {
			return newNode;
		}
		return n;
	}

	/**
	 * f0 ::= <WHILE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public Object visit(WhileStatement n, Object argu) {
		n.setF4((Statement) n.getF4().accept(this, argu));

		Vector<Access> accessList = new Vector<>();
		n.getF2().accept(new AccessBuilder(), accessList);
		removeDuplicateAccesses(accessList);
		if (accessList.size() != 0) {
			StringGetter strGetter = new StringGetter();
			n.getF2().accept(strGetter);
			String logTemp = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n.getF2()));

			// Add Expression's accesses at the end of the while's body
			String log = "{" + logTemp + "}";
			CompoundStatement newCSAtEnd = (CompoundStatement) CParser
					.createCrudeASTNode(new ByteArrayInputStream(log.getBytes()), CompoundStatement.class);

			if (n.getF4().getStmtF0().choice.getClass().getSimpleName().equals("CompoundStatement")) {
				// Add these new elements to the end of n.f4's CompoundStatement
				for (Node element : newCSAtEnd.getF1().nodes) {
					((CompoundStatement) n.getF4().getStmtF0().choice).getF1().nodes.add(element);
				}
			} else {
				// Create a new statement and replace n.f4
				strGetter = new StringGetter();
				n.getF4().accept(strGetter);
				String newStmtString = "{" + strGetter.str;
				newStmtString += logTemp + "}";
				Statement newStmt = (Statement) CParser
						.createCrudeASTNode(new ByteArrayInputStream(newStmtString.getBytes()), Statement.class);
				n.setF4(newStmt);
			}

			// Add Expression's accesses to the beginning of while loop
			strGetter = new StringGetter();
			n.accept(strGetter);
			String newCSString = "{" + logTemp + strGetter.str + "}";
			return CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()),
					CompoundStatement.class);
		}
		return n;
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
	public Object visit(DoStatement n, Object argu) {
		n.setF1((Statement) n.getF1().accept(this, argu));

		Vector<Access> accessList = new Vector<>();
		n.getF4().accept(new AccessBuilder(), accessList);
		removeDuplicateAccesses(accessList);
		if (accessList.size() != 0) {
			StringGetter strGetter = new StringGetter();
			n.getF4().accept(strGetter);
			String logTemp = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n.getF4()));

			// Add Expression's accesses at the end of the while's body
			String log = "{" + logTemp + "}";
			CompoundStatement newCSAtEnd = (CompoundStatement) CParser
					.createCrudeASTNode(new ByteArrayInputStream(log.getBytes()), CompoundStatement.class);

			if (n.getF1().getStmtF0().choice.getClass().getSimpleName().equals("CompoundStatement")) {
				// Add these new elements to the end of n.f1's CompoundStatement
				for (Node element : newCSAtEnd.getF1().nodes) {
					((CompoundStatement) n.getF1().getStmtF0().choice).getF1().nodes.add(element);
				}
			} else {
				// Create a new statement and replace n.f1
				strGetter = new StringGetter();
				n.getF1().accept(strGetter);
				String newStmtString = "{" + strGetter.str;
				newStmtString += logTemp + "}";
				Statement newStmt = (Statement) CParser
						.createCrudeASTNode(new ByteArrayInputStream(newStmtString.getBytes()), Statement.class);
				n.setF1(newStmt);
			}
		}
		return n;
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
	public Object visit(ForStatement n, Object argu) {
		n.setF8((Statement) n.getF8().accept(this, argu));

		// Put accesses to n.f4.node and n.f6.node at the end of n.f8
		Vector<Access> accessList = new Vector<>();
		if (n.getF4().present()) {
			n.getF4().node.accept(new AccessBuilder(), accessList);
		}
		if (n.getF6().present()) {
			n.getF6().node.accept(new AccessBuilder(), accessList);
		}
		removeDuplicateAccesses(accessList);
		StringGetter strGetter = null;
		String log = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n.getF0()));
		String logCS = "{" + log + "}";

		if (accessList.size() != 0) {
			CompoundStatement newLogCS = (CompoundStatement) CParser
					.createCrudeASTNode(new ByteArrayInputStream(logCS.getBytes()), CompoundStatement.class);

			// Add elements to the elements of the original statement
			if (n.getF8().getStmtF0().choice.getClass().getSimpleName().equals("CompoundStatement")) {
				List<Node> newList = newLogCS.getF1().nodes;
				List<Node> oldList = ((CompoundStatement) n.getF8().getStmtF0().choice).getF1().nodes;
				for (Node element : newList) {
					oldList.add(element);
				}
			} else {
				// Create a new statement, and replace n.f8
				strGetter = new StringGetter();
				n.getF8().accept(strGetter);
				String newStmtString = "{" + log + strGetter.str + "}";
				Statement newStmt = (Statement) CParser
						.createCrudeASTNode(new ByteArrayInputStream(newStmtString.getBytes()), Statement.class);
				n.setF8(newStmt);
			}
		}

		// Put accesses to n.f4.node and n.f2.node at the beginning of n
		accessList = new Vector<>();
		if (n.getF4().present()) {
			n.getF4().node.accept(new AccessBuilder(), accessList);
		}
		if (n.getF2().present()) {
			n.getF2().node.accept(new AccessBuilder(), accessList);
		}
		removeDuplicateAccesses(accessList);
		if (accessList.size() != 0) {
			log = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n.getF0()));

			strGetter = new StringGetter();
			n.accept(strGetter);
			String newCSString = "{" + log + strGetter.str + "}";
			return (CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()),
					CompoundStatement.class));
		}
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
		Node newNode = (Node) n.getJumpStmtF0().accept(this, argu);
		if (newNode == null) {
			return n;
		}
		if (newNode.getClass().getSimpleName().equals("CompoundStatement")) {
			return newNode;
		}
		return n;
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public Object visit(ReturnStatement n, Object argu) {
		Vector<Access> accessList = new Vector<>();
		n.getF1().accept(new AccessBuilder(), accessList);
		removeDuplicateAccesses(accessList);
		if (accessList.size() != 0) {
			StringGetter strGetter = new StringGetter();
			n.accept(strGetter);
			String log = commUtil.getPrintLog(accessList, DRCommonUtils.getLineNum(n));
			String newCSString = "{" + log + strGetter.str + "}";
			return (CParser.createCrudeASTNode(new ByteArrayInputStream(newCSString.getBytes()),
					CompoundStatement.class));
		}
		return n;
	}

}
