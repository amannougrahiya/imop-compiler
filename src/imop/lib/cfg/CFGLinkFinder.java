/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg;

import java.util.ArrayList;
import java.util.List;

import imop.ast.node.external.ASection;
import imop.ast.node.external.AtomicConstruct;
import imop.ast.node.external.CompoundStatement;
import imop.ast.node.external.CriticalConstruct;
import imop.ast.node.external.DoStatement;
import imop.ast.node.external.Expression;
import imop.ast.node.external.ExpressionStatement;
import imop.ast.node.external.FinalClause;
import imop.ast.node.external.ForConstruct;
import imop.ast.node.external.ForStatement;
import imop.ast.node.external.FunctionDefinition;
import imop.ast.node.external.IfClause;
import imop.ast.node.external.IfStatement;
import imop.ast.node.external.MasterConstruct;
import imop.ast.node.external.Node;
import imop.ast.node.external.NodeSequence;
import imop.ast.node.external.NumThreadsClause;
import imop.ast.node.external.OmpForCondition;
import imop.ast.node.external.OmpForInitExpression;
import imop.ast.node.external.OmpForReinitExpression;
import imop.ast.node.external.OrderedConstruct;
import imop.ast.node.external.ParallelConstruct;
import imop.ast.node.external.ParameterDeclaration;
import imop.ast.node.external.SectionsConstruct;
import imop.ast.node.external.SingleConstruct;
import imop.ast.node.external.Statement;
import imop.ast.node.external.SwitchStatement;
import imop.ast.node.external.TaskConstruct;
import imop.ast.node.external.WhileStatement;
import imop.ast.node.internal.BeginNode;
import imop.ast.node.internal.CallStatement;
import imop.ast.node.internal.EndNode;
import imop.ast.node.internal.OmpClause;
import imop.ast.node.internal.PostCallNode;
import imop.ast.node.internal.PreCallNode;
import imop.baseVisitor.DepthFirstVisitor;
import imop.lib.cfg.link.node.AtomicBeginLink;
import imop.lib.cfg.link.node.AtomicEndLink;
import imop.lib.cfg.link.node.AtomicStatementLink;
import imop.lib.cfg.link.node.CFGLink;
import imop.lib.cfg.link.node.CallBeginLink;
import imop.lib.cfg.link.node.CallEndLink;
import imop.lib.cfg.link.node.CallPostLink;
import imop.lib.cfg.link.node.CallPreLink;
import imop.lib.cfg.link.node.CompoundBeginLink;
import imop.lib.cfg.link.node.CompoundElementLink;
import imop.lib.cfg.link.node.CompoundEndLink;
import imop.lib.cfg.link.node.CriticalBeginLink;
import imop.lib.cfg.link.node.CriticalBodyLink;
import imop.lib.cfg.link.node.CriticalEndLink;
import imop.lib.cfg.link.node.DoBeginLink;
import imop.lib.cfg.link.node.DoBodyLink;
import imop.lib.cfg.link.node.DoEndLink;
import imop.lib.cfg.link.node.DoPredicateLink;
import imop.lib.cfg.link.node.ForBeginLink;
import imop.lib.cfg.link.node.ForBodyLink;
import imop.lib.cfg.link.node.ForEndLink;
import imop.lib.cfg.link.node.ForInitLink;
import imop.lib.cfg.link.node.ForStepLink;
import imop.lib.cfg.link.node.ForTermLink;
import imop.lib.cfg.link.node.FunctionBeginLink;
import imop.lib.cfg.link.node.FunctionBodyLink;
import imop.lib.cfg.link.node.FunctionEndLink;
import imop.lib.cfg.link.node.FunctionParameterLink;
import imop.lib.cfg.link.node.IfBeginLink;
import imop.lib.cfg.link.node.IfElseBodyLink;
import imop.lib.cfg.link.node.IfEndLink;
import imop.lib.cfg.link.node.IfPredicateLink;
import imop.lib.cfg.link.node.IfThenBodyLink;
import imop.lib.cfg.link.node.MasterBeginLink;
import imop.lib.cfg.link.node.MasterBodyLink;
import imop.lib.cfg.link.node.MasterEndLink;
import imop.lib.cfg.link.node.OmpForBeginLink;
import imop.lib.cfg.link.node.OmpForBodyLink;
import imop.lib.cfg.link.node.OmpForEndLink;
import imop.lib.cfg.link.node.OmpForInitLink;
import imop.lib.cfg.link.node.OmpForStepLink;
import imop.lib.cfg.link.node.OmpForTermLink;
import imop.lib.cfg.link.node.OrderedBeginLink;
import imop.lib.cfg.link.node.OrderedBodyLink;
import imop.lib.cfg.link.node.OrderedEndLink;
import imop.lib.cfg.link.node.ParallelBeginLink;
import imop.lib.cfg.link.node.ParallelBodyLink;
import imop.lib.cfg.link.node.ParallelClauseLink;
import imop.lib.cfg.link.node.ParallelEndLink;
import imop.lib.cfg.link.node.SectionsBeginLink;
import imop.lib.cfg.link.node.SectionsEndLink;
import imop.lib.cfg.link.node.SectionsSectionBodyLink;
import imop.lib.cfg.link.node.SingleBeginLink;
import imop.lib.cfg.link.node.SingleBodyLink;
import imop.lib.cfg.link.node.SingleEndLink;
import imop.lib.cfg.link.node.SwitchBeginLink;
import imop.lib.cfg.link.node.SwitchBodyLink;
import imop.lib.cfg.link.node.SwitchEndLink;
import imop.lib.cfg.link.node.SwitchPredicateLink;
import imop.lib.cfg.link.node.TaskBeginLink;
import imop.lib.cfg.link.node.TaskBodyLink;
import imop.lib.cfg.link.node.TaskClauseLink;
import imop.lib.cfg.link.node.TaskEndLink;
import imop.lib.cfg.link.node.WhileBeginLink;
import imop.lib.cfg.link.node.WhileBodyLink;
import imop.lib.cfg.link.node.WhileEndLink;
import imop.lib.cfg.link.node.WhilePredicateLink;
import imop.lib.util.Misc;

public class CFGLinkFinder {
	/**
	 * For a given {@code childNode}, when a visit is called on the parent
	 * of the {@code childNode}, this method populates {@code cfgLink}
	 * with appropriate CFGLink object.<br>
	 * Note that {@code childNode} should not be a FunctionDefinition.
	 * 
	 * @param childNode
	 *            node whose place in its parent CFG node has to be found with
	 *            the help of an appropriate CFG link.
	 * @return
	 *         an appropriate CFG link object that represents the position of
	 *         the {@code childNode} in its CFG parent node.
	 */
	public static CFGLink getCFGLinkFor(Node childNode) {
		CFGLink returnCFGLink;
		childNode = Misc.getCFGNodeFor(childNode);
		assert (childNode != null) : "Cannot provide CFG link node for a non-CFG node.";
		if (childNode instanceof FunctionDefinition) {
			return null;
		}
		Node parentNode = Misc.getEnclosingCFGNonLeafNode(childNode);
		if (parentNode == null) {
			// Misc.warnDueToLackOfFeature("Cannot provide CFG link node for a node with no CFG parent.", childNode);
			return null;
		}

		CFGLinkFinder.CFGLinkGetter linkGetter = new CFGLinkGetter(childNode);
		parentNode.accept(linkGetter);
		returnCFGLink = linkGetter.cfgLink;
		return returnCFGLink;
	}

	/**
	 * For a given {@code childNode}, when a visit is called on the parent
	 * of the {@code childNode}, this visitor populates {@code cfgLink}
	 * with appropriate CFGLink object.
	 */
	private static class CFGLinkGetter extends DepthFirstVisitor {

		public CFGLink cfgLink = null;
		private Node childNode = null;

		public CFGLinkGetter(Node childNode) {
			this.childNode = childNode;
		}

		//
		// User-generated visitor methods below
		//

		/**
		 * f0 ::= ( DeclarationSpecifiers() )?
		 * f1 ::= Declarator()
		 * f2 ::= ( DeclarationList() )?
		 * f3 ::= CompoundStatement()
		 */
		@Override
		public void visit(FunctionDefinition n) {
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new FunctionBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new FunctionEndLink(n, (EndNode) childNode);
				return;
			}
			if (childNode == n.getF3()) {
				cfgLink = new FunctionBodyLink(n, (CompoundStatement) childNode);
				return;
			}
			List<ParameterDeclaration> paramList = Misc.getInheritedEncloseeList(n.getF1(), ParameterDeclaration.class);
			for (Node paramNode : paramList) {
				if (childNode == paramNode) {
					int index = paramList.indexOf(paramNode);
					cfgLink = new FunctionParameterLink(n, (ParameterDeclaration) childNode, index);
					return;
				}
			}
			// OLD CODE:
			//			List<Node> stmtList = body.f1.nodes;
			//			if (body.f1.present()) {	// If the containing body is not empty
			//				for (Node tempNode : stmtList) { // For all the statements in the body
			//					Node element = Misc.getInternalFirstCFGNode(tempNode); // Get the CFG node "element"
			//					if (childNode == element) {
			//						int index = stmtList.indexOf(tempNode);
			//						cfgLink = new FunctionBodyLink(n, childNode, index);
			//						return;
			//					}
			//				}
			//			}
			return;
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= ParallelDirective()
		 * f2 ::= Statement()
		 */
		@Override
		public void visit(ParallelConstruct n) {
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new ParallelBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new ParallelEndLink(n, (EndNode) childNode);
				return;
			}
			for (OmpClause ompClause : n.getInfo().getCFGInfo().getCFGClauseList()) {
				if (childNode == ompClause) {
					cfgLink = new ParallelClauseLink(n, ompClause);
				}
			}
			if (childNode == n.getInfo().getCFGInfo().getBody()) {
				cfgLink = new ParallelBodyLink(n, (Statement) childNode);
			}

			// Old Code
			//			List<OmpClause> executableClauses = new ArrayList<>();
			//			for (OmpClause ompClause : Misc.getClauseList(n)) {
			//				if (ompClause instanceof IfClause || ompClause instanceof NumThreadsClause
			//						|| ompClause instanceof FinalClause) {
			//					executableClauses.add(ompClause);
			//				}
			//			}
			//			if (!executableClauses.isEmpty()) {
			//				for (OmpClause ompClause : executableClauses) {
			//					if (childNode == ompClause) {
			//						int index = executableClauses.indexOf(ompClause);
			//						cfgLink = new ParallelClauseLink(n, (OmpClause) childNode, index);
			//						return;
			//					}
			//				}
			//			}
			//			Node element = Misc.getInternalFirstCFGNode(n.f2); // Obtain the internal CFG node "element"
			//			if (childNode == element) {
			//				cfgLink = new ParallelBodyLink(n, (Statement) childNode);
			//			}
			return;
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= ForDirective()
		 * f2 ::= OmpForHeader()
		 * f3 ::= Statement()
		 */
		@Override
		public void visit(ForConstruct n) {
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new OmpForBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new OmpForEndLink(n, (EndNode) childNode);
				return;
			}
			Node elementInit = n.getF2().getF2(); // Obtain the CFG elements from init Condition
			if (childNode == elementInit) {
				cfgLink = new OmpForInitLink(n, (OmpForInitExpression) childNode);
				return;
			}

			Node elementTerm = n.getF2().getF4(); // from termination condition
			if (childNode == elementTerm) {
				cfgLink = new OmpForTermLink(n, (OmpForCondition) childNode);
				return;
			}
			Node elementStep = n.getF2().getF6(); // from step statement
			if (childNode == elementStep) {
				cfgLink = new OmpForStepLink(n, (OmpForReinitExpression) childNode);
				return;
			}
			Node elementBody = Misc.getInternalFirstCFGNode(n.getF3()); // and from body of the for loop.
			if (childNode == elementBody) {
				cfgLink = new OmpForBodyLink(n, (Statement) childNode);
				return;
			}
			return;
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new SectionsBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new SectionsEndLink(n, (EndNode) childNode);
				return;
			}

			for (Node secNode : n.getF4().getF2().getNodes()) { // For all the sections
				Node element = Misc.getInternalFirstCFGNode(((ASection) secNode).getF3()); // Obtain the CFG element of section's body
				if (childNode == element) {
					int index = n.getF4().getF2().getNodes().indexOf(secNode);
					cfgLink = new SectionsSectionBodyLink(n, (Statement) childNode, index);
					return;
				}
			}
			return;
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new SingleBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new SingleEndLink(n, (EndNode) childNode);
				return;
			}
			Node element = Misc.getInternalFirstCFGNode(n.getF4()); // Obtain the CFG element from body of the Single construct.
			if (childNode == element) {
				cfgLink = new SingleBodyLink(n, (Statement) childNode);
			}
			return;
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new TaskBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new TaskEndLink(n, (EndNode) childNode);
				return;
			}

			List<OmpClause> executableClauses = new ArrayList<>();
			for (OmpClause ompClause : Misc.getClauseList(n)) {
				if (ompClause instanceof IfClause || ompClause instanceof NumThreadsClause
						|| ompClause instanceof FinalClause) {
					executableClauses.add(ompClause);
				}
			}
			if (!executableClauses.isEmpty()) {
				for (OmpClause ompClause : executableClauses) {
					if (childNode == ompClause) {
						int index = executableClauses.indexOf(ompClause);
						cfgLink = new TaskClauseLink(n, (OmpClause) childNode, index);
						return;
					}
				}
			}

			Node element = Misc.getInternalFirstCFGNode(n.getF4()); // Obtain the internal CFG node "element"
			if (childNode == element) {
				cfgLink = new TaskBodyLink(n, (Statement) childNode);
			}
			return;
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <MASTER>
		 * f2 ::= OmpEol()
		 * f3 ::= Statement()
		 */
		@Override
		public void visit(MasterConstruct n) {
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new MasterBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new MasterEndLink(n, (EndNode) childNode);
				return;
			}
			Node element = Misc.getInternalFirstCFGNode(n.getF3()); // Obtain the CFG element of the body of the construct
			if (childNode == element) {
				cfgLink = new MasterBodyLink(n, (Statement) childNode);
			}
			return;
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new CriticalBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new CriticalEndLink(n, (EndNode) childNode);
				return;
			}
			Node element = Misc.getInternalFirstCFGNode(n.getF4()); // Obtain the CFG element of the body of the construct
			if (childNode == element) {
				cfgLink = new CriticalBodyLink(n, (Statement) childNode);
			}
			return;
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new AtomicBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new AtomicEndLink(n, (EndNode) childNode);
				return;
			}
			Node element = Misc.getInternalFirstCFGNode(n.getF4()); // Obtain the CFG element of the body of the construct
			if (childNode == element) {
				cfgLink = new AtomicStatementLink(n, (ExpressionStatement) childNode);
			}
			return;
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <ORDERED>
		 * f2 ::= OmpEol()
		 * f3 ::= Statement()
		 */
		@Override
		public void visit(OrderedConstruct n) {
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new OrderedBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new OrderedEndLink(n, (EndNode) childNode);
				return;
			}
			Node element = Misc.getInternalFirstCFGNode(n.getF3()); // Obtain the CFG element of the body of the construct
			if (childNode == element) {
				cfgLink = new OrderedBodyLink(n, (Statement) childNode);
			}
			return;
		}

		/**
		 * f0 ::= "{"
		 * f1 ::= ( CompoundStatementElement() )*
		 * f2 ::= "}"
		 */
		@Override
		public void visit(CompoundStatement n) {
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new CompoundBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new CompoundEndLink(n, (EndNode) childNode);
				return;
			}
			if (n.getF1().present()) { // If the block isn't empty
				List<Node> stmtList = n.getF1().getNodes();
				for (Node dS : stmtList) { // For all the statements
					Node element = Misc.getInternalFirstCFGNode(dS); // Obtain the CFG element of the statement 
					if (childNode == element) {
						int index = stmtList.indexOf(dS);
						cfgLink = new CompoundElementLink(n, childNode, index);
						return;
					}
				}
			}
			return;
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
		public void visit(IfStatement n) {
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new IfBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new IfEndLink(n, (EndNode) childNode);
				return;
			}
			Node elementCond = n.getF2(); // Obtain the CFG element of condition and then part
			if (childNode == elementCond) {
				cfgLink = new IfPredicateLink(n, (Expression) childNode);
				return;
			}

			Node elementThen = Misc.getInternalFirstCFGNode(n.getF4());
			if (childNode == elementThen) {
				cfgLink = new IfThenBodyLink(n, (Statement) childNode);
				return;
			}

			if (n.getF5().present()) {	// If "else" part is present
				Node elementElse = Misc.getInternalFirstCFGNode(((NodeSequence) n.getF5().getNode()).getNodes().get(1));
				if (childNode == elementElse) {
					cfgLink = new IfElseBodyLink(n, (Statement) childNode);
					return;
				}
			}
			return;
		}

		/**
		 * f0 ::= <SWITCH>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 * f4 ::= Statement()
		 */
		@Override
		public void visit(SwitchStatement n) {
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new SwitchBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new SwitchEndLink(n, (EndNode) childNode);
				return;
			}
			Node elementCond = n.getF2(); // Obtain the CFG element of cond and body
			if (childNode == elementCond) {
				cfgLink = new SwitchPredicateLink(n, (Expression) childNode);
				return;
			}

			Node elementBody = Misc.getInternalFirstCFGNode(n.getF4());
			if (childNode == elementBody) {
				cfgLink = new SwitchBodyLink(n, (Statement) childNode);
				return;
			}

			return;
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new WhileBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new WhileEndLink(n, (EndNode) childNode);
				return;
			}
			Node elementCond = n.getF2(); // Obtain the CFG element of cond and body
			if (childNode == elementCond) {
				cfgLink = new WhilePredicateLink(n, (Expression) childNode);
				return;
			}

			Node elementBody = Misc.getInternalFirstCFGNode(n.getF4());
			if (childNode == elementBody) {
				cfgLink = new WhileBodyLink(n, (Statement) childNode);
				return;
			}
			return;
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new DoBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new DoEndLink(n, (EndNode) childNode);
				return;
			}
			Node elementBody = Misc.getInternalFirstCFGNode(n.getF1()); // Obtain CFG element of body and cond
			if (childNode == elementBody) {
				cfgLink = new DoBodyLink(n, (Statement) childNode);
				return;
			}
			Node elementCond = n.getF4();
			if (childNode == elementCond) {
				cfgLink = new DoPredicateLink(n, (Expression) childNode);
				return;
			}
			return;
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();	// Create a nested CFG "ncfg".
			if (childNode == ncfg.getBegin()) {
				cfgLink = new ForBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new ForEndLink(n, (EndNode) childNode);
				return;
			}
			Node elementBody = Misc.getInternalFirstCFGNode(n.getF8()); // Obtain CFG element of the body
			if (childNode == elementBody) {
				cfgLink = new ForBodyLink(n, (Statement) childNode);
				return;
			}
			Node elementInit = n.getF2().getNode();
			if (childNode == elementInit) {
				cfgLink = new ForInitLink(n, (Expression) childNode);
				return;
			}
			Node elementTerm = n.getF4().getNode();
			if (childNode == elementTerm) {
				cfgLink = new ForTermLink(n, (Expression) childNode);
				return;
			}
			Node elementStep = n.getF6().getNode();
			if (childNode == elementStep) {
				cfgLink = new ForStepLink(n, (Expression) childNode);
				return;
			}
		}

		@Override
		public void visit(CallStatement n) {
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();
			if (childNode == ncfg.getBegin()) {
				cfgLink = new CallBeginLink(n, (BeginNode) childNode);
				return;
			}
			if (childNode == ncfg.getEnd()) {
				cfgLink = new CallEndLink(n, (EndNode) childNode);
				return;
			}
			if (childNode == n.getPreCallNode()) {
				cfgLink = new CallPreLink(n, (PreCallNode) childNode);
				return;
			}
			if (childNode == n.getPostCallNode()) {
				cfgLink = new CallPostLink(n, (PostCallNode) childNode);
				return;
			}
		}
	}
}
