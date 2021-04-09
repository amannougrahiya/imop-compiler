/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg;

import imop.ast.annotation.CaseLabel;
import imop.ast.annotation.DefaultLabel;
import imop.ast.annotation.IncompleteEdge;
import imop.ast.annotation.IncompleteEdge.TypeOfIncompleteness;
import imop.ast.annotation.Label;
import imop.ast.info.cfgNodeInfo.ExpressionInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstVisitor;
import imop.lib.analysis.typeSystem.IntegerType;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.cfg.info.ForConstructCFGInfo;
import imop.lib.cfg.link.node.*;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Populates getParent, getSuccBlock, getPredBlock and getNestedCFG entries.
 */
public class CFGGenerator {

	public static void createCFGEdgesIn(Node n) {
		CFGGenerationVisitor visitor = new CFGGenerationVisitor();
		n.accept(visitor);

		// OLD CODE: Now, we have deprecated incomplete edges.
		// /*
		// * If n does not have any nesting SwitchStatement, then the case/default
		// * labels in all the relevantCFGstmt of n should be added with an
		// * incomplete edge.
		// */
		// SwitchStatement enclosingSwitch = Misc.getEnclosingSwitch(n);
		// if (enclosingSwitch == null) {
		// SwitchRelevantStatementsGetter switchRelevantGetter = new
		// SwitchRelevantStatementsGetter();
		// n.accept(switchRelevantGetter);
		// for (Statement stmt : switchRelevantGetter.relevantCFGStmts) {
		// // Note that "stmt" (which can be "n" as well) is mostly not nested inside
		// any enclosing switch-statement.
		// CFGGenerator.handleIncompleteSwitchLabels(stmt);
		// }
		// }
	}

	@SuppressWarnings("unused")
	@Deprecated
	private static void handleIncompleteSwitchLabels(Statement n) {
		if (true) {
			return;
		}
		for (Label label : n.getInfo().getLabelAnnotations()) {
			if (label instanceof CaseLabel) {
				n.getInfo().getIncompleteSemantics().getIncompleteEdges()
						.add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_CASE_SOURCE, n, (CaseLabel) label));

			} else if (label instanceof DefaultLabel) {
				n.getInfo().getIncompleteSemantics().getIncompleteEdges()
						.add(new IncompleteEdge(TypeOfIncompleteness.UNKNOWN_DEFAULT_SOURCE, n));
			}
		}
	}

	/**
	 * Checks if the edge between {@code pred} and {@code succ} can ever be
	 * taken,
	 * if {@code pred} is a compile-time constant.
	 * <br>
	 * NOTE: This call also removes the stale edge from constant switch
	 * predicate, whenever required.
	 * 
	 * @param src
	 *             a predecessor node.
	 * @param dest
	 *             a successor node.
	 * @return
	 *         true, if the control-flow edge between {@code pred} and
	 *         {@code succ} may make sense.
	 */
	public static boolean verifyEdgePrecision(Node src, Node dest) {
		if (!(src instanceof Expression)) {
			return true;
		}
		if (!Misc.isAPredicate(src)) {
			return true;
		}
		if (!((ExpressionInfo) src.getInfo()).evaluatesToConstant()) {
			return true;
		}
		boolean isZero = ((Expression) src).getInfo().evaluatesToZero();
		CFGLink cfgLink = CFGLinkFinder.getCFGLinkFor(src);
		if (cfgLink instanceof WhilePredicateLink) {
			CFGLink succLink = CFGLinkFinder.getCFGLinkFor(dest);
			if (succLink instanceof WhileBodyLink) {
				return !isZero;
			} else if (succLink instanceof WhileEndLink) {
				return isZero;
			} else {
				assert (false);
				return true;
			}
		} else if (cfgLink instanceof IfPredicateLink) {
			CFGLink succLink = CFGLinkFinder.getCFGLinkFor(dest);
			if (succLink instanceof IfThenBodyLink) {
				return !isZero;
			} else if (succLink instanceof IfElseBodyLink || succLink instanceof IfEndLink) {
				return isZero;
			} else {
				assert (false);
				return true;
			}
		} else if (cfgLink instanceof DoPredicateLink) {
			CFGLink succLink = CFGLinkFinder.getCFGLinkFor(dest);
			if (succLink instanceof DoBodyLink) {
				return !isZero;
			} else if (succLink instanceof DoEndLink) {
				return isZero;
			} else {
				assert (false);
				return true;
			}
		} else if (cfgLink instanceof ForTermLink) {
			CFGLink succLink = CFGLinkFinder.getCFGLinkFor(dest);
			if (succLink instanceof ForBodyLink) {
				return !isZero;
			} else if (succLink instanceof ForEndLink) {
				return isZero;
			} else {
				assert (false);
				return true;
			}
		} else if (cfgLink instanceof SwitchPredicateLink) {
			Type expType = Type.getType((Expression) src);
			Statement boundStmt = null;
			SwitchStatement switchStmt = ((SwitchPredicateLink) cfgLink).enclosingNonLeafNode;
			if (expType.isCharacterType()) {
				Character predVal = Misc.evaluateCharacter(src);
				if (predVal == null) {
					return true;
				}
				boundStmt = switchStmt.getInfo().getDestinationStatement(predVal);
			} else if (expType instanceof IntegerType) {
				Integer predVal = Misc.evaluateInteger(src);
				// Note that predVal is not null here, as checked earlier. It may be zero or
				// something else.
				boundStmt = switchStmt.getInfo().getDestinationStatement(predVal);
			} else {
				assert (false);
				return true;
			}

			if (boundStmt == null) {
				/*
				 * Since the predicate is a constant, and there is no matching
				 * case in the switch-statement, as well as no default, the
				 * successor can only be the EndNode of the SwitchStatement.
				 */
				if (dest == switchStmt.getInfo().getCFGInfo().getNestedCFG().getEnd()) {
					return true;
				} else {
					return false;
				}
			} else {
				if (boundStmt == dest) {
					/*
					 * Here, we REMOVE the existing outgoing edges from the
					 * predicate,
					 * if they point to anything but succ, and return true.
					 */
					List<Node> originalSuccList = src.getInfo().getCFGInfo().getSuccBlocks();
					if (originalSuccList.isEmpty()) {
						// Do nothing.
					} else if (originalSuccList.size() == 1) {
						if (originalSuccList.get(0) == dest) {
							// Do nothing.
						} else {
							originalSuccList.clear();
						}
					} else {
						originalSuccList.clear();
					}
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	private static class CFGGenerationVisitor extends DepthFirstVisitor {
		/**
		 * This method creates forward and backward links between the given pair
		 * of
		 * nodes.
		 * <br>
		 * 
		 * @param pred
		 *             the predecessor node.
		 * @param succ
		 *             the successor node.
		 */
		public static void connect(Node pred, Node succ) {
			assert (pred != null && succ != null);
			assert (Misc.isCFGNode(pred) && Misc.isCFGNode(succ));

			if (!CFGGenerator.verifyEdgePrecision(pred, succ)) {
				return;
			}

			List<Node> edgesFromPred = pred.getInfo().getCFGInfo().getSuccBlocks();
			List<Node> edgesFromSucc = succ.getInfo().getCFGInfo().getPredBlocks();
			if (!edgesFromPred.contains(succ)) {
				edgesFromPred.add(succ);
			}
			if (!edgesFromSucc.contains(pred)) {
				edgesFromSucc.add(pred);
			}
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
			assert (!n.getF2().present());
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Create a nested CFG "ncfg".
			CompoundStatement body = n.getF3();
			Node first = ncfg.getBegin();
			List<ParameterDeclaration> paramList = Misc.getInheritedEncloseeList(n.getF1(), ParameterDeclaration.class);
			if (paramList.size() > 0) {
				connect(ncfg.getBegin(), paramList.get(0));
				for (Node paramNode : paramList) {
					if (paramNode == paramList.get(0)) {
						continue;
					} else {
						int index = paramList.indexOf(paramNode);
						Node predParam = paramList.get(index - 1);
						connect(predParam, paramNode);
					}
				}
				first = paramList.get(paramList.size() - 1);
			}

			// NEW CODE: Now, we do not remove compound-statement
			// of the FunctionDefinition from the CFG.
			connect(first, body);
			body.accept(this);
			boolean reach = body.getInfo().getCFGInfo().isEndReachable();
			if (reach) {
				connect(body, ncfg.getEnd());
			}

			// OLD CODE:
			// List<Node> stmtList = body.f1.nodes;
			// if (body.f1.present()) { // If the containing body is not empty
			// Node element = Misc.getInternalFirstCFGNode(stmtList.get(0)); // Obtain the
			// first statement element (CFG node) in the body.
			// connect(first, element); // Add element to the succ list of begin of ncfg.
			// for (Node tempNode : stmtList) { // For all the statements in the body
			// element = Misc.getInternalFirstCFGNode(tempNode); // Get the CFG node
			// "element"
			// element.accept(this);
			// reach = element.getInfo().getCFGInfo().isEndReachable();
			// if (tempNode == stmtList.get(stmtList.size() - 1)) { // For the last
			// statement of the body
			// if (reach) { // If the end is reachable
			// connect(element, ncfg.end); // Add element to the pred list of end of ncfg.
			// }
			// return;
			// }
			// if (reach) { // For all non-last statements, if the end is reachable
			// Node nextElement =
			// Misc.getInternalFirstCFGNode((stmtList.get(stmtList.indexOf(tempNode) + 1)));
			// // Get the next CFG element in the list.
			// connect(element, nextElement); // Add the next element in the succ list of
			// current element.
			// }
			// }
			// }
			return;
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= ParallelDirective()
		 * f2 ::= Statement()
		 */
		@Override
		public void visit(ParallelConstruct n) {
			boolean reach;
			/*
			 * Update on 24-Nov-2016 >>
			 */
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
			// Check if any clause that may have an expression is present.
			List<OmpClause> executableClauses = new ArrayList<>();
			for (OmpClause ompClause : Misc.getClauseList(n)) {
				if (ompClause instanceof IfClause || ompClause instanceof NumThreadsClause) {
					executableClauses.add(ompClause);
				}
			}
			Node firstInBody;
			if (!executableClauses.isEmpty()) {
				connect(ncfg.getBegin(), executableClauses.get(0));
				for (OmpClause ompClause : executableClauses) {
					int index = executableClauses.indexOf(ompClause);
					if (ompClause == executableClauses.get(0)) {
						continue;
					} else {
						connect(executableClauses.get(index - 1), ompClause);
					}
				}
				firstInBody = executableClauses.get(executableClauses.size() - 1);
			} else {
				firstInBody = ncfg.getBegin();
			}
			/*
			 * << Update.
			 */
			Node element = Misc.getInternalFirstCFGNode(n.getParConsF2()); // Obtain the internal CFG node "element"
			connect(firstInBody, element);
			element.accept(this);
			reach = element.getInfo().getCFGInfo().isEndReachable();
			if (reach) { // If the end is reachable
				connect(element, ncfg.getEnd()); // Add element in the pred list of ncfg's end
			}
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
			boolean reach;
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
			Node elementInit = n.getF2().getF2(); // Obtain the CFG elements from init Condition
			Node elementTerm = n.getF2().getF4(); // from termination condition
			Node elementStep = n.getF2().getF6(); // from step statement
			Node elementBody = Misc.getInternalFirstCFGNode(n.getF3()); // and from body of the for loop.

			connect(ncfg.getBegin(), elementInit); // Add elementInit to the succ of begin
			connect(elementInit, elementTerm); // Add elementTerm to the succ of elementInit
			connect(elementTerm, elementBody); // Add elementBody to the succ of elementTerm
			connect(elementTerm, ncfg.getEnd()); // Add end to the succ of elementTerm

			elementBody.accept(this);
			reach = elementBody.getInfo().getCFGInfo().isEndReachable();
			if (reach) {
				connect(elementBody, elementStep); // Add elementStep to the succ of elementBody
			}
			connect(elementStep, elementTerm); // Add elementTerm to the succ of elementStep
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
			List<Node> sectionList = n.getF4().getF2().getNodes();
			for (Node secNode : sectionList) { // For all the sections
				Node element = Misc.getInternalFirstCFGNode(((ASection) secNode).getF3()); // Obtain the CFG element of
																							// section's body
				element.accept(this);
				connect(ncfg.getBegin(), element); // Add the element to the succ of begin, and pred of end of the
													// NestedCFG
				boolean reach = element.getInfo().getCFGInfo().isEndReachable();
				if (reach) {
					connect(element, ncfg.getEnd());
				}
			}
			if (sectionList.isEmpty()) {
				connect(ncfg.getBegin(), ncfg.getEnd());
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
			Node element = Misc.getInternalFirstCFGNode(n.getF4()); // Obtain the CFG element from body of the Single
																	// construct.
			element.accept(this);
			connect(ncfg.getBegin(), element); // Add element to the succ of begin and pred of end
			boolean reach = element.getInfo().getCFGInfo().isEndReachable();
			if (reach) {
				connect(element, ncfg.getEnd());
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
			/*
			 * Update on 24-Nov-2016 >>
			 */
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
			// Check if any clause that may have an expression is present.
			List<OmpClause> executableClauses = new ArrayList<>();
			for (OmpClause ompClause : Misc.getClauseList(n)) {
				if (ompClause instanceof IfClause || ompClause instanceof FinalClause) {
					executableClauses.add(ompClause);
				}
			}
			Node firstInBody;
			if (!executableClauses.isEmpty()) {
				connect(ncfg.getBegin(), executableClauses.get(0));
				for (OmpClause ompClause : executableClauses) {
					int index = executableClauses.indexOf(ompClause);
					if (ompClause == executableClauses.get(0)) {
						continue;
					} else {
						connect(executableClauses.get(index - 1), ompClause);
					}
				}
				firstInBody = executableClauses.get(executableClauses.size() - 1);
			} else {
				firstInBody = ncfg.getBegin();
			}
			/*
			 * << Update.
			 */
			Node element = Misc.getInternalFirstCFGNode(n.getF4()); // Obtain the internal CFG node "element"
			connect(firstInBody, element);
			element.accept(this); // Create the CFG structure within
			boolean reach = element.getInfo().getCFGInfo().isEndReachable();
			if (reach) { // If the end is reachable
				connect(element, ncfg.getEnd()); // Add element in the pred list of ncfg's end
			}
			return;
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
			n.getF6().accept(this);
			Misc.warnDueToLackOfFeature(
					"Could not create CFG edges corresponding to the elements of a ParallelForConstruct. "
							+ "This would be done while adding this node to the program using any of the elementary "
							+ "transformations. Note that the CFG edges inside the body of this construct were created anyway.",
					n);
			// boolean reach;
			// NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the
			// NestedCFG "ncfg"
			// Node elementInit = Misc.getInternalFirstCFGNode(n.f5.f2); // Obtain the CFG
			// elements of init, term, step and body of the for.
			// Node elementTerm = Misc.getInternalFirstCFGNode(n.f5.f4);
			// Node elementStep = Misc.getInternalFirstCFGNode(n.f5.f6);
			// Node elementBody = Misc.getInternalFirstCFGNode(n.f6);
			//
			// connect(ncfg.begin, elementInit); // Add init to the succ of begin
			//
			// elementInit.accept(this);
			// reach = elementInit.getInfo().getCFGInfo().isEndReachable();
			// if (reach) {
			// connect(elementInit, elementTerm); // Add term to the succ of init
			// }
			//
			// elementTerm.accept(this);
			// reach = elementTerm.getInfo().getCFGInfo().isEndReachable();
			// if (reach) {
			// connect(elementTerm, elementBody); // Add body to the succ of term
			// connect(elementTerm, ncfg.end); // Add end to the succ of term
			// }
			//
			// elementBody.accept(this);
			// reach = elementBody.getInfo().getCFGInfo().isEndReachable();
			// if (reach) {
			// connect(elementBody, elementStep); // Add step to the succ of body
			// }
			//
			// elementStep.accept(this);
			// reach = elementStep.getInfo().getCFGInfo().isEndReachable();
			// if (reach) {
			// connect(elementStep, elementTerm); // Add term to the succ of step
			// }
			return;
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
			Misc.warnDueToLackOfFeature(
					"Could not create CFG edges corresponding to the elements of a ParallelSectionsConstruct. "
							+ "This would be done while adding this node to the program using any of the elementary "
							+ "transformations. Note that the CFG edges inside the body of this construct were created anyway.",
					n);
			// NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the
			// NestedCFG "ncfg"
			// for (Node secNode : n.f5.f2.nodes) { //For all the sections in the sections
			// construct
			// Node element = Misc.getInternalFirstCFGNode(((ASection) secNode).f3); //
			// Obtain the CFG element of the body of the section
			// element.accept(this);
			// connect(ncfg.begin, element); // Add element to the succ and pred of begin
			// and end respectively
			// boolean reach = element.getInfo().getCFGInfo().isEndReachable();
			// if (reach) {
			// connect(element, ncfg.end);
			// }
			// }
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
			Node element = Misc.getInternalFirstCFGNode(n.getF3()); // Obtain the CFG element of the body of the
																	// construct
			element.accept(this);
			connect(ncfg.getBegin(), element); // Add the element to the succ and pred of begin and end respectively
			boolean reach = element.getInfo().getCFGInfo().isEndReachable();
			if (reach) {
				connect(element, ncfg.getEnd());
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();
			Node element = Misc.getInternalFirstCFGNode(n.getF4()); // Obtain the CFG element of the body of the
																	// construct
			element.accept(this);
			connect(ncfg.getBegin(), element);
			boolean reach = element.getInfo().getCFGInfo().isEndReachable();
			if (reach) {
				connect(element, ncfg.getEnd());
			}
			return;
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <ATOMIC>
		 * f2 ::= ( AtomicClause() )?
		 * f3 ::= OmpEol()
		 * f4 ::= Statement()
		 */
		@Override
		public void visit(AtomicConstruct n) {
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();
			Node element = Misc.getInternalFirstCFGNode(n.getF4()); // Obtain the CFG element of the body of the
																	// construct
			element.accept(this);
			connect(ncfg.getBegin(), element);
			boolean reach = element.getInfo().getCFGInfo().isEndReachable();
			if (reach) {
				connect(element, ncfg.getEnd());
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();
			Node element = Misc.getInternalFirstCFGNode(n.getF3()); // Obtain the CFG element of the body of the
																	// construct
			element.accept(this);
			connect(ncfg.getBegin(), element);
			boolean reach = element.getInfo().getCFGInfo().isEndReachable();
			if (reach) {
				connect(element, ncfg.getEnd());
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
			boolean reach;
			if (n.getF1().present()) { // If the block isn't empty
				NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
				List<Node> stmtList = n.getF1().getNodes();
				Node element = Misc.getInternalFirstCFGNode(stmtList.get(0)); // Obtain the CFG element for the first
																				// statement in the block

				connect(ncfg.getBegin(), element); // Add the first statement to the succ of begin

				for (Node dS : stmtList) { // For all the statements
					element = Misc.getInternalFirstCFGNode(dS); // Obtain the CFG element of the statement
					element.accept(this);
					reach = element.getInfo().getCFGInfo().isEndReachable();
					if (reach) { // If the end is reachable
						if (dS == stmtList.get(stmtList.size() - 1)) { // If this is the last statement
							connect(element, ncfg.getEnd()); // Add end to the succ of this statement
							return;
						}
						Node nextElement = Misc.getInternalFirstCFGNode(stmtList.get(stmtList.indexOf(dS) + 1)); // Obtain
																													// the
																													// CFG
																													// element
																													// of
																													// the
																													// next
																													// statement
						// if (nextElement == null) {
						// System.out.println("Found a statement in CompoundStatement, which doesn't
						// have any CFG element within it!");
						// System.out.println("Here is the statement:");
						// ((CompoundStatementElement) stmtList.get(stmtList.indexOf(dS) +
						// 1)).f0.choice.accept(new PrintCodeVisitor(), 0);
						// continue;
						// }
						connect(element, nextElement); // Add next statement to the succ of the current statement
					}
				}
			} else {
				NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
				connect(ncfg.getBegin(), ncfg.getEnd()); // Connect the begin to end
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
			boolean reach;
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
			Node elementCond = n.getF2(); // Obtain the CFG element of condition and then part
			Node elementThen = Misc.getInternalFirstCFGNode(n.getF4());

			connect(ncfg.getBegin(), elementCond); // Add elementCond to the succ of begin
			connect(elementCond, elementThen); // Add elementThen to the succ of elementCond

			// No need to call accept on elementCond, as it is surely a leaf CFG element
			elementThen.accept(this);
			reach = elementThen.getInfo().getCFGInfo().isEndReachable();
			if (reach) {
				connect(elementThen, ncfg.getEnd()); // Add end to the succ of elementThen
			}
			if (n.getF5().present()) { // If "else" part is present
				Node elementElse = Misc.getInternalFirstCFGNode(((NodeSequence) n.getF5().getNode()).getNodes().get(1));
				connect(elementCond, elementElse); // Add elementElse to the succ of elementCond
				elementElse.accept(this);
				reach = elementElse.getInfo().getCFGInfo().isEndReachable();
				if (reach) { // If end of elementElse is reachable
					connect(elementElse, ncfg.getEnd()); // Add end to the succ of elementElse
				}
			} else { // If "else" isn't present
				connect(elementCond, ncfg.getEnd()); // Add end to the succ of elementCond
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
			boolean reach;
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG ncfg
			Node elementCond = n.getF2(); // Obtain the CFG element of cond and body
			Node elementBody = Misc.getInternalFirstCFGNode(n.getF4());

			connect(ncfg.getBegin(), elementCond); // Add elementCond to the succ of begin

			// No need to call accept on elementCond

			elementBody.accept(this);
			reach = elementBody.getInfo().getCFGInfo().isEndReachable();
			if (reach) { // If end of the body is reachable
				connect(elementBody, ncfg.getEnd()); // Add end to the succ of elementBody
			}

			Collection<Statement> caseStmts = n.getInfo().getRelevantCFGStatements(); // Old: Misc.getCaseStatements(n);
			// Obtain the vector of all the immediate cases and default within the current
			// switch block
			for (Node caseS : caseStmts) { // For all the case statements
				Node elementCase = Misc.getInternalFirstCFGNode(caseS); // Obtain the CFG element of case statement
				connect(elementCond, elementCase); // Add elementCase to the succ of elementCond
			}

			// If there was no DefaultLabeledStatement, add an edge from Switch's expression
			// to the endSwitchStatement
			if (!n.getInfo().hasDefaultLabel()) {
				connect(elementCond, ncfg.getEnd());
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
			boolean reach;
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG ncfg
			Node elementCond = n.getF2(); // Obtain the CFG element of cond and body
			Node elementBody = Misc.getInternalFirstCFGNode(n.getF4());

			// No need to call accept on elementCond

			connect(ncfg.getBegin(), elementCond); // Add elementCond to the succ of begin

			connect(elementCond, elementBody); // Add elementBody to the succ of elementCond
			connect(elementCond, ncfg.getEnd()); // Add end to the succ of elementCond

			elementBody.accept(this);
			reach = elementBody.getInfo().getCFGInfo().isEndReachable();
			if (reach) { // If end of body is reachable
				connect(elementBody, elementCond); // Add elementCond to the succ of elementBody
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
			boolean reach;
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG ncfg
			Node elementBody = Misc.getInternalFirstCFGNode(n.getF1()); // Obtain CFG element of body and cond
			Node elementCond = n.getF4();

			connect(ncfg.getBegin(), elementBody); // Add elementBody to the succ of begin

			elementBody.accept(this);
			reach = elementBody.getInfo().getCFGInfo().isEndReachable();
			if (reach) { // If end of elementBody is reachable
				connect(elementBody, elementCond); // Add elementCond to the succ of elementBody
			}

			connect(elementCond, elementBody); // Add elementBody and end to the succ of elementCond
			connect(elementCond, ncfg.getEnd());
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
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
			Node elementBody = Misc.getInternalFirstCFGNode(n.getF8()); // Obtain CFG element of the body
			elementBody.accept(this);
			boolean reachStmt = elementBody.getInfo().getCFGInfo().isEndReachable();
			Node elementInit, elementTerm, elementStep;
			if (n.getF2().present()) { // If the init is present
				elementInit = n.getF2().getNode(); // Obtain CFG element of the init

				connect(ncfg.getBegin(), elementInit); // Add elementInit to the succ of begin

				if (n.getF4().present()) { // If termination condition is present
					elementTerm = n.getF4().getNode(); // Obtain CFG element of the termination condition

					connect(elementInit, elementTerm); // Add elementTerm to the succ of elementInit
					connect(elementTerm, elementBody); // Add elementBody to the succ of elementTerm
					connect(elementTerm, ncfg.getEnd()); // Add end to the succ of elementTerm

					if (n.getF6().present()) { // If step is present
						elementStep = n.getF6().getNode();
						if (reachStmt) { // If the body's end is reachable
							connect(elementBody, elementStep); // Add elementStep to the succ of elementBody
						}
						connect(elementStep, elementTerm); // Add elementTerm to the succ of elementStep
					} else { // If step is not present
						if (reachStmt) { // If the body's end is reachable
							connect(elementBody, elementTerm); // Add elementTerm to the succ of elementBody
						}
					}
				} else { // If the termination condition is not present
					connect(elementInit, elementBody); // Add elementBody to the succ of elementInit
					if (reachStmt) { // If body's end is reachable
						if (n.getF6().present()) { // If step is present
							elementStep = n.getF6().getNode(); // Obtain CFG element of step

							connect(elementBody, elementStep); // Add elementStep to the succ of elementBody
							connect(elementStep, elementBody); // Add elementBody to the succ of elementStep
						} else { // If step is not present
							connect(elementBody, elementBody); // Add elementBody to its own succ
						}
					}
				}
			} else if (n.getF4().present()) { // if init is not present, but the termination condition is present
				elementTerm = n.getF4().getNode(); // Obtain CFG element of the termination condition

				connect(ncfg.getBegin(), elementTerm); // Add elementTerm to the succ of begin
				connect(elementTerm, elementBody); // Add elementBody to the succ of elementTerm
				connect(elementTerm, ncfg.getEnd()); // Add end to the succ of elementTerm
				if (reachStmt) { // If end of the body is reachable
					if (n.getF6().present()) { // If step is present
						elementStep = n.getF6().getNode(); // Obtain CFG element of step

						connect(elementBody, elementStep); // Add elementStep to the succ of elementBody
						connect(elementStep, elementTerm); // Add elementTerm to the succ of elementStep
					} else { // If step is not present
						connect(elementBody, elementTerm); // Add elementTerm to the succ of elementBody
					}
				} else if (n.getF6().present()) { // If the end of the body is not reachable and the step is present
					elementStep = n.getF6().getNode(); // Obtain CFG element of step

					connect(elementStep, elementTerm); // Add elementTerm to the succ of elementStep
				}

			} else { // if neither init nor term is present
				connect(ncfg.getBegin(), elementBody); // Add the body to the succ of begin
				if (reachStmt) { // If body's end is reachable
					if (n.getF6().present()) { // If elementStep is present
						elementStep = n.getF6().getNode(); // Obtain CFG element of step

						connect(elementBody, elementStep); // Add elementStep to the succ of elementBody
						connect(elementStep, elementBody); // Add elementBody to the succ of elementStep
					} else { // If elementStep is not present
						connect(elementBody, elementBody); // Add elementBody to its own succ
					}
				}
			}
			return;
		}

		@Override
		public void visit(CallStatement n) {
			NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();
			PreCallNode preCallNode = n.getPreCallNode();
			PostCallNode postCallNode = n.getPostCallNode();

			connect(ncfg.getBegin(), preCallNode);
			connect(preCallNode, postCallNode);
			connect(postCallNode, ncfg.getEnd());
		}

		/**
		 * f0 ::= <GOTO>
		 * f1 ::= <IDENTIFIER>
		 * f2 ::= ";"
		 * 
		 * CFG Type: LEAF
		 */
		@Override
		public void visit(GotoStatement n) {
			Node outerMostEncloser = n.getInfo().getOuterMostNonLeafEncloser();

			if (outerMostEncloser == null) {
				// OLD CODE: If there is no outer-most CFG encloser, add an incomplete edge and
				// return.
				// n.getInfo().getIncompleteSemantics().addToEdges(
				// new
				// IncompleteEdge(IncompleteEdge.TypeOfIncompleteness.UNKNOWN_GOTO_DESTINATION,
				// n));
				return;
			}

			Statement labeledNode = outerMostEncloser.getInfo().getStatementWithLabel(n.getF1().getTokenImage());
			// OLD Code:
			// Call a visitor which provides the target
			// SimpleLabeledStatementGetter labelVisitor = new
			// SimpleLabeledStatementGetter();
			// func.accept(labelVisitor, n.f1.tokenImage);

			if (labeledNode == null) {
				// OLD CODE: If label not found in the enclosing function, add to the
				// incompleteness and proceed.
				// n.getInfo().getIncompleteSemantics().addToEdges(
				// new
				// IncompleteEdge(IncompleteEdge.TypeOfIncompleteness.UNKNOWN_GOTO_DESTINATION,
				// n));
				return;
			}

			// Add target to the succ of n (the Goto statement)
			Node target = Misc.getInternalFirstCFGNode(labeledNode);
			connect(n, target);
			return;
		}

		/**
		 * f0 ::= <CONTINUE>
		 * f1 ::= ";"
		 * 
		 * CFG Type: LEAF
		 */
		@Override
		public void visit(ContinueStatement n) {
			Node targetOwner = Misc.getEnclosingLoopOrForConstruct(n); // Obtain the owner loop of this continue
																		// statement

			if (targetOwner == null) {
				// OLD CODE: If target-owner is not found, add to the incompleteness and
				// proceed.
				// n.getInfo().getIncompleteSemantics().addToEdges(
				// new
				// IncompleteEdge(IncompleteEdge.TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION,
				// n));
				return;
			}

			Node target = null; // target represents the target statement of this continue

			if (targetOwner instanceof WhileStatement) {
				target = Misc.getInternalFirstCFGNode(((WhileStatement) targetOwner).getF2()); // Target is the
																								// termination condition
			} else if (targetOwner instanceof DoStatement) {
				target = Misc.getInternalFirstCFGNode(((DoStatement) targetOwner).getF4()); // Target is the termination
																							// condition

			} else if (targetOwner instanceof ForStatement) {
				ForStatement forS = ((ForStatement) targetOwner);
				if (forS.getF6().present()) { // If step is present
					target = Misc.getInternalFirstCFGNode(forS.getF6());
				} else if (forS.getF4().present()) { // else if termination condition is present
					target = Misc.getInternalFirstCFGNode(forS.getF4());
				} else {
					target = Misc.getInternalFirstCFGNode(forS.getF8());
				}

			} else if (targetOwner instanceof ForConstruct) {
				ForConstructCFGInfo cfgInfo = (ForConstructCFGInfo) targetOwner.getInfo().getCFGInfo();
				OmpForReinitExpression ompForReinitExpression = cfgInfo.getReinitExpression();
				target = ompForReinitExpression;
			}
			connect(n, target); // Add the target to the succ of n (the Continue statement)
			return;
		}

		/**
		 * f0 ::= <BREAK>
		 * f1 ::= ";"
		 * 
		 * CFG Type: LEAF
		 */
		@Override
		public void visit(BreakStatement n) {
			Node targetOwn = Misc.getEnclosingLoopOrSwitch(n);

			if (targetOwn == null) {
				// OLD CODE: If target-owner not found, add to the incompleteness and return.
				// n.getInfo().getIncompleteSemantics().addToEdges(
				// new
				// IncompleteEdge(IncompleteEdge.TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION,
				// n));
				return;
			}

			Node target = targetOwn.getInfo().getCFGInfo().getNestedCFG().getEnd(); // get the endStatement of the
																					// associated loop/switch
			connect(n, target); // Add target to the succ of n (the Break Statement)
			return;
		}

		/**
		 * f0 ::= <RETURN>
		 * f1 ::= ( Expression() )?
		 * f2 ::= ";"
		 * <p>
		 * CFG Type: LEAF
		 */
		@Override
		public void visit(ReturnStatement n) {
			Node targetOwn = Misc.getEnclosingFunction(n);

			if (targetOwn == null) {
				// OLD CODE: If enclosing function is not found, add to the incompleteness and
				// return.
				// n.getInfo().getIncompleteSemantics().addToEdges(
				// new
				// IncompleteEdge(IncompleteEdge.TypeOfIncompleteness.UNKNOWN_RETURN_DESTINATION,
				// n));
				return;
			}

			Node target = targetOwn.getInfo().getCFGInfo().getNestedCFG().getEnd(); // get endNode of the enclosing
																					// function
			connect(n, target); // Add target to the succ of n (the Return statement)
			return;
		}

	}

}
