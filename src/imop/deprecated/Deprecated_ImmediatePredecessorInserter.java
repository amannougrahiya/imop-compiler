/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.deprecated;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstVisitor;
import imop.lib.cfg.CFGGenerator;
import imop.lib.cfg.NestedCFG;
import imop.lib.transform.BasicTransform;
import imop.lib.util.Misc;
import imop.parser.CParserConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Given a {@code baseNode} and a {@code targetNode}, the visits of this class,
 * when called on the CFG parent of {@code baseNode}, will insert
 * {@code targetNode} immediately before {@code baseNode} on all the CFG
 * paths.<br>
 * Note that the current implementation reflects CFG changes to only the AST,
 * and not to other abstractions of the program (like data-dependence graph,
 * call-graph, etc.)
 */
@Deprecated
public class Deprecated_ImmediatePredecessorInserter extends DepthFirstVisitor {

	public Node subRoot;
	private Node baseNode;
	private Node targetNode;

	public Deprecated_ImmediatePredecessorInserter(Node baseNode, Node targetNode) {
		this.baseNode = Misc.getCFGNodeFor(baseNode);
		this.targetNode = Misc.getCFGNodeFor(targetNode);
	}

	/**
	 * This method checks whether the given edge is the edge that we need to
	 * process,
	 * and performs the CFG-level processing of the same. <br>
	 * Note that the AST-level processing is done inline in the various visits.
	 * <br>
	 * 
	 * @param pred
	 *             the predecessor node.
	 * @param succ
	 *             the successor node.
	 */
	public void checkEdge(Node pred, Node succ) {
		assert (pred != null && succ != null);
		assert (Misc.isCFGNode(pred) && Misc.isCFGNode(succ));
		if (succ == baseNode) {
			this.disconnect(pred, succ);
			this.connect(pred, targetNode);
			if (this.isEndReachable(targetNode)) {
				this.connect(targetNode, baseNode);
			}
		}

	}

	/**
	 * This method creates forward and backward links between the given pair of
	 * nodes.
	 * <br>
	 * 
	 * @param pred
	 *             the predecessor node.
	 * @param succ
	 *             the successor node.
	 */
	public void connect(Node pred, Node succ) {
		assert (pred != null && succ != null);
		assert (Misc.isCFGNode(pred) && Misc.isCFGNode(succ));
		List<Node> edgesFromPred = pred.getInfo().getCFGInfo().getSuccBlocks();
		List<Node> edgesFromSucc = succ.getInfo().getCFGInfo().getPredBlocks();
		if (!edgesFromPred.contains(succ)) {
			edgesFromPred.add(succ);
		}
		if (!edgesFromSucc.contains(pred)) {
			edgesFromSucc.add(pred);
		}
	}

	/**
	 * Removes the CFG-edge between {@code pred} and {@code succ}.
	 * 
	 * @param pred
	 * @param succ
	 */
	public void disconnect(Node pred, Node succ) {
		assert (pred != null && succ != null);
		assert (Misc.isCFGNode(pred) && Misc.isCFGNode(succ));
		List<Node> edgesFromPred = pred.getInfo().getCFGInfo().getSuccBlocks();
		List<Node> edgesFromSucc = succ.getInfo().getCFGInfo().getPredBlocks();
		edgesFromPred.remove(succ);
		edgesFromSucc.remove(pred);
	}

	/**
	 * This method returns true if control entering to the node <code>n</code>
	 * "via any point within the block" may flow out of the end of the node.
	 * <br>
	 * This method should not be called until the CFG edges have been created
	 * within <code>n</code>
	 * 
	 * @param n
	 *          CFG node which needs to be checked for end-reachibility.
	 * @return
	 *         true, if starting the control-flow from the begin of
	 *         <code>n</code>,
	 *         the end of <code>n</code> can be reached.
	 */
	boolean isEndReachable(Node n) {
		assert (Misc.isCFGNode(n));
		if (Misc.isCFGLeafNode(n)) { // n is a leaf CFG node
			if (n instanceof JumpStatement) {
				return false;
			} else {
				return true;
			}
		} else {
			/*
			 * If the end node of the non-leaf node n has any predecessor, we
			 * assume that there must have
			 * been a path from the begin of n to the end.
			 */

			Node endNode = n.getInfo().getCFGInfo().getNestedCFG().getEnd();
			if (endNode.getInfo().getCFGInfo().getPredBlocks().isEmpty()) {
				return false;
			} else {
				return true;
			}
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
		boolean reach;
		NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Create a nested CFG "ncfg".
		CompoundStatement body = n.getF3();
		NodeListOptional stmtList = body.getF1();
		Node first = ncfg.getBegin();

		if (baseNode == ncfg.getBegin()) {
			Misc.exitDueToLackOfFeature("Cannot insert a statement before the begin-node of a function!");
		}

		List<ParameterDeclaration> paramList = Misc.getInheritedEncloseeList(n.getF1(), ParameterDeclaration.class);
		if (paramList.size() > 0) {
			if (paramList.get(0) == baseNode) {
				checkEdge(ncfg.getBegin(), paramList.get(0));
				assert (targetNode instanceof ParameterDeclaration);
				Set<ParameterList> paramListSet = Misc.getInheritedEnclosee(n.getF1(), ParameterList.class);
				ParameterList paramListNode = Misc.getSingleton(paramListSet);
				NodeSequence newNodeSeq = new NodeSequence(2);
				newNodeSeq.addNode(new NodeToken(","));
				newNodeSeq.addNode(paramListNode.getF0());
				paramListNode.getF1().addNode(0, newNodeSeq);
				paramListNode.setF0((ParameterDeclaration) targetNode);
				return;
			}
			for (Node paramNode : paramList) {
				if (paramNode == paramList.get(0)) {
					continue;
				} else {
					int index = paramList.indexOf(paramNode);
					Node predParam = paramList.get(index - 1);
					if (paramNode == baseNode) {
						checkEdge(predParam, paramNode);
						Set<ParameterList> paramListSet = Misc.getInheritedEnclosee(n.getF1(), ParameterList.class);
						ParameterList paramListNode = Misc.getSingleton(paramListSet);
						NodeSequence newNodeSeq = new NodeSequence(2);
						newNodeSeq.addNode(new NodeToken(","));
						newNodeSeq.addNode(targetNode);
						paramListNode.getF1().addNode(index - 1, newNodeSeq);
						return;
					}
				}
			}
			first = paramList.get(paramList.size() - 1);
		}

		if (body.getF1().present()) { // If the containing body is not empty
			Node element = Misc.getInternalFirstCFGNode(stmtList.getNodes().get(0)); // Obtain the first statement
																						// element (CFG node) in the
																						// body.
			if (element == baseNode) {
				checkEdge(first, element); // Add element to the succ list of begin of ncfg.
				if (targetNode instanceof ParameterDeclaration) {
					Set<ParameterList> paramListSet = Misc.getInheritedEnclosee(n.getF1(), ParameterList.class);
					ParameterList paramListNode = Misc.getSingleton(paramListSet);
					NodeSequence newNodeSeq = new NodeSequence(2);
					newNodeSeq.addNode(new NodeToken(","));
					newNodeSeq.addNode(targetNode);
					paramListNode.getF1().addNode(paramListNode.getF1().size() - 1, newNodeSeq);
					return;
				} else if (targetNode instanceof Statement || targetNode instanceof Declaration) {
					targetNode = Misc.getCompoundStatementElementWrapper(targetNode);
					assert (targetNode != null);
					stmtList.addNode(0, targetNode);
					return;
				}
			}
			for (Node tempNode : stmtList.getNodes()) { // For all the statements in the body
				element = Misc.getInternalFirstCFGNode(tempNode); // Get the CFG node "element"
				reach = isEndReachable(element);
				if (tempNode == stmtList.getNodes().get(stmtList.size() - 1)) { // For the last statement of the body
					if (reach) { // If the end is reachable
						if (ncfg.getEnd() == baseNode) {
							checkEdge(element, ncfg.getEnd()); // Add element to the pred list of end of ncfg.
							targetNode = Misc.getCompoundStatementElementWrapper(targetNode);
							assert (targetNode != null);
							stmtList.addNode(stmtList.size() - 1, targetNode);
							return;
						}
					}
					return;
				}
				if (reach) { // For all non-last statements, if the end is reachable
					Node nextElement = Misc.getInternalFirstCFGNode(
							(stmtList.getNodes().get(stmtList.getNodes().indexOf(tempNode) + 1))); // Get the next CFG
																									// element in the
																									// list.
					if (nextElement == baseNode) {
						checkEdge(element, nextElement); // Add the next element in the succ list of current element.
						targetNode = Misc.getCompoundStatementElementWrapper(targetNode);
						assert (targetNode != null);
						stmtList.addNode(stmtList.getNodes().indexOf(tempNode) + 1, targetNode);
						return;
					}
				}
			}
		}
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
		NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		// Check if any clause that may have an expression is present.
		List<OmpClause> executableClauses = new ArrayList<>();
		for (OmpClause ompClause : Misc.getClauseList(n)) {
			if (ompClause instanceof IfClause || ompClause instanceof NumThreadsClause) {
				executableClauses.add(ompClause);
			}
		}
		Node firstInBody;
		if (!executableClauses.isEmpty()) {
			if (executableClauses.get(0) == baseNode) {
				checkEdge(ncfg.getBegin(), executableClauses.get(0));
				if (targetNode instanceof IfClause || targetNode instanceof NumThreadsClause) {
					targetNode = this.getAUniqueParallelOrDataClause(targetNode);
					NodeListOptional clauseList = n.getParConsF1().getF1().getF0();
					int actualIndex = Misc.getClauseList(n).indexOf(executableClauses.get(0));
					clauseList.addNode(actualIndex, targetNode);
					return;
				} else if (targetNode instanceof Statement) {
					assert (false);
				}
			}
			for (OmpClause ompClause : executableClauses) {
				int index = executableClauses.indexOf(ompClause);
				if (ompClause == executableClauses.get(0)) {
					continue;
				} else {
					if (ompClause == baseNode) {
						checkEdge(executableClauses.get(index - 1), ompClause);
						if (targetNode instanceof IfClause || targetNode instanceof NumThreadsClause) {
							targetNode = this.getAUniqueParallelOrDataClause(targetNode);
							NodeListOptional clauseList = n.getParConsF1().getF1().getF0();
							int actualIndex = Misc.getClauseList(n).indexOf(ompClause);
							clauseList.addNode(actualIndex, targetNode);
							return;
						}
					}
				}
			}
			firstInBody = executableClauses.get(executableClauses.size() - 1);
		} else {

			firstInBody = ncfg.getBegin();
		}
		Node element = Misc.getInternalFirstCFGNode(n.getParConsF2()); // Obtain the internal CFG node "element"
		if (element == baseNode) {
			checkEdge(firstInBody, element);
			if (targetNode instanceof OmpClause) {
				targetNode = this.getAUniqueParallelOrDataClause(targetNode);
				NodeListOptional clauseList = n.getParConsF1().getF1().getF0();
				clauseList.addNode(clauseList.size() - 1, targetNode);
			} else if (targetNode instanceof Statement) {
				Statement oldStmt = n.getParConsF2();
				Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, firstInBody);
				if (oldStmt != newStmt) {
					n.setParConsF2(newStmt);
				}
				return;
			}
		}
		reach = isEndReachable(element);
		if (reach) { // If the end is reachable
			if (ncfg.getEnd() == baseNode) {
				checkEdge(element, ncfg.getEnd()); // Add element in the pred list of ncfg's end
				Statement newStmt = this.addAfterStatement(targetNode, n.getParConsF2(), ncfg.getEnd());
				if (newStmt != n.getParConsF2()) {
					n.setParConsF2(newStmt);
				}
				return;
			}
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

		if (baseNode == ncfg.getBegin()) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}

		if (elementInit == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		if (elementTerm == baseNode) {
			Misc.exitDueToLackOfFeature(
					"Cannot insert any predecessors for for-condition of the for-header of an omp-for-construct. Try inserting at a safer place.");
			return;

		}
		if (elementBody == baseNode) {
			checkEdge(elementTerm, elementBody); // Add elementBody to the succ of elementTerm
			Statement newStmt = this.addBeforeStatement(targetNode, n.getF3(), elementTerm);
			if (newStmt != n.getF3()) {
				n.setF3(newStmt);
			}
		}
		if (ncfg.getEnd() == baseNode) {
			BasicTransform.deprecated_insertImmediateSuccessor(n, targetNode);
		}

		reach = isEndReachable(elementBody);
		if (reach) {
			if (elementStep == baseNode) {
				checkEdge(elementBody, elementStep); // Add elementStep to the succ of elementBody
				Statement newStmt = this.addAfterStatement(targetNode, n.getF3(), elementStep);
				if (newStmt != n.getF3()) {
					n.setF3(newStmt);
				}
			}
		}
		if (elementTerm == baseNode) {
			Misc.exitDueToLackOfFeature(
					"Cannot insert any predecessors for for-condition of the for-header of an omp-for-construct. Try inserting at a safer place.");
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
		NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		for (Node secNode : n.getF4().getF2().getNodes()) { // For all the sections
			Node element = Misc.getInternalFirstCFGNode(((ASection) secNode).getF3()); // Obtain the CFG element of
																						// section's body
			if (element == baseNode) {
				checkEdge(ncfg.getBegin(), element); // Add the element to the succ of begin, and pred of end of the
														// NestedCFG
				Statement oldStmt = ((ASection) secNode).getF3();
				Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, ncfg.getBegin());
				if (newStmt != oldStmt) {
					((ASection) secNode).setF3(newStmt);
				}
			}
			boolean reach = isEndReachable(element);
			if (reach) {
				if (ncfg.getEnd() == baseNode) {
					checkEdge(element, ncfg.getEnd());
					Statement oldStmt = ((ASection) secNode).getF3();
					Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, ncfg.getEnd());
					if (newStmt != oldStmt) {
						((ASection) secNode).setF3(newStmt);
					}
				}
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
		NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		Node element = Misc.getInternalFirstCFGNode(n.getF4()); // Obtain the CFG element from body of the Single
																// construct.
		if (element == baseNode) {
			checkEdge(ncfg.getBegin(), element); // Add element to the succ of begin and pred of end
			Statement newStmt = this.addBeforeStatement(targetNode, n.getF4(), ncfg.getBegin());
			if (newStmt != n.getF4()) {
				n.setF4(newStmt);
			}
		}
		boolean reach = isEndReachable(element);
		if (reach) {
			if (ncfg.getEnd() == baseNode) {
				checkEdge(element, ncfg.getEnd());
				Statement newStmt = this.addAfterStatement(targetNode, n.getF4(), ncfg.getEnd());
				if (newStmt != n.getF4()) {
					n.setF4(newStmt);
				}
			}
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
		NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		// Check if any clause that may have an expression is present.
		List<OmpClause> executableClauses = new ArrayList<>();
		for (OmpClause ompClause : Misc.getClauseList(n)) {
			if (ompClause instanceof IfClause || ompClause instanceof FinalClause) {
				executableClauses.add(ompClause);
			}
		}
		Node firstInBody;
		if (!executableClauses.isEmpty()) {
			if (executableClauses.get(0) == baseNode) {
				checkEdge(ncfg.getBegin(), executableClauses.get(0));
				if (targetNode instanceof IfClause || targetNode instanceof FinalClause) {
					targetNode = this.getATaskClause(targetNode);
					assert (false); // Deprecated code below.
					NodeListOptional clauseList = n.getF2();
					int actualIndex = Misc.getClauseList(n).indexOf(executableClauses.get(0));
					clauseList.addNode(actualIndex, targetNode);
					return;
				} else if (targetNode instanceof Statement) {
					assert (false);
				}
			}
			for (OmpClause ompClause : executableClauses) {
				int index = executableClauses.indexOf(ompClause);
				if (ompClause == executableClauses.get(0)) {
					continue;
				} else {
					if (ompClause == baseNode) {
						checkEdge(executableClauses.get(index - 1), ompClause);
						if (targetNode instanceof IfClause || targetNode instanceof FinalClause) {
							targetNode = this.getATaskClause(targetNode);
							NodeListOptional clauseList = n.getF2();
							int actualIndex = Misc.getClauseList(n).indexOf(ompClause);
							clauseList.addNode(actualIndex, targetNode);
							return;
						}
					}
				}
			}
			firstInBody = executableClauses.get(executableClauses.size() - 1);
		} else {
			firstInBody = ncfg.getBegin();
		}
		Node element = Misc.getInternalFirstCFGNode(n.getF4()); // Obtain the internal CFG node "element"
		if (element == baseNode) {
			checkEdge(firstInBody, element);
			if (targetNode instanceof OmpClause) {
				targetNode = this.getATaskClause(targetNode);
				NodeListOptional clauseList = n.getF2();
				clauseList.addNode(clauseList.size() - 1, targetNode);
			} else if (targetNode instanceof Statement) {
				Statement oldStmt = n.getF4();
				Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, firstInBody);
				if (newStmt != oldStmt) {
					n.setF4(newStmt);
				}
			}
			return;
		}
		boolean reach = isEndReachable(element);
		if (reach) { // If the end is reachable
			if (ncfg.getEnd() == baseNode) {
				checkEdge(element, ncfg.getEnd()); // Add element in the pred list of ncfg's end
				Statement newStmt = this.addAfterStatement(targetNode, n.getF4(), ncfg.getEnd());
				if (newStmt != n.getF4()) {
					n.setF4(newStmt);
				}
				return;
			}
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
		assert (false);
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
		assert (false);
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
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		Node element = Misc.getInternalFirstCFGNode(n.getF3()); // Obtain the CFG element of the body of the construct
		if (element == baseNode) {
			checkEdge(ncfg.getBegin(), element); // Add the element to the succ and pred of begin and end respectively
			Statement newStmt = this.addBeforeStatement(targetNode, n.getF3(), ncfg.getBegin());
			if (newStmt != n.getF3()) {
				n.setF3(newStmt);
			}
		}
		boolean reach = isEndReachable(element);
		if (reach) {
			if (ncfg.getEnd() == baseNode) {
				checkEdge(element, ncfg.getEnd());
				Statement newStmt = this.addAfterStatement(targetNode, n.getF3(), ncfg.getEnd());
				if (newStmt != n.getF3()) {
					n.setF3(newStmt);
				}
			}
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
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		Node element = Misc.getInternalFirstCFGNode(n.getF4()); // Obtain the CFG element of the body of the construct
		if (element == baseNode) {
			checkEdge(ncfg.getBegin(), element);
			Statement newStmt = this.addBeforeStatement(targetNode, n.getF4(), ncfg.getBegin());
			if (newStmt != n.getF4()) {
				n.setF4(newStmt);
			}
		}
		boolean reach = isEndReachable(element);
		if (reach) {
			if (ncfg.getEnd() == baseNode) {
				checkEdge(element, ncfg.getEnd());
				Statement newStmt = this.addAfterStatement(targetNode, n.getF4(), ncfg.getEnd());
				if (newStmt != n.getF4()) {
					n.setF4(newStmt);
				}
			}
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
		NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG();
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		Node element = Misc.getInternalFirstCFGNode(n.getF4()); // Obtain the CFG element of the body of the construct
		if (element == baseNode) {
			Misc.warnDueToLackOfFeature("Note that a statement cannot be added"
					+ " within an Atomic construct. It is inserted immediately before the construct.", null);
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		boolean reach = isEndReachable(element);
		if (reach) {
			if (ncfg.getEnd() == baseNode) {
				Misc.warnDueToLackOfFeature(
						"Note that a statement cannot be added"
								+ " within an Atomic construct. It is inserted immediately before the construct.",
						null);
				BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
				return;
			}
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
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		Node element = Misc.getInternalFirstCFGNode(n.getF3()); // Obtain the CFG element of the body of the construct
		if (element == baseNode) {
			checkEdge(ncfg.getBegin(), element);
			Statement newStmt = this.addBeforeStatement(targetNode, n.getF3(), ncfg.getBegin());
			if (newStmt != n.getF3()) {
				n.setF3(newStmt);
			}
		}
		boolean reach = isEndReachable(element);
		if (reach) {
			if (ncfg.getEnd() == baseNode) {
				checkEdge(element, ncfg.getEnd());
				Statement newStmt = this.addAfterStatement(targetNode, n.getF3(), ncfg.getEnd());
				if (newStmt != n.getF3()) {
					n.setF3(newStmt);
				}
			}
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
		NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
		}
		NodeListOptional stmtList = n.getF1();
		if (n.getF1().present()) { // If the block isn't empty
			Node element = Misc.getInternalFirstCFGNode(stmtList.getNodes().get(0)); // Obtain the CFG element for the
																						// first statement in the block

			if (element == baseNode) {
				checkEdge(ncfg.getBegin(), element); // Add the first statement to the succ of begin
				targetNode = Misc.getCompoundStatementElementWrapper(targetNode);
				stmtList.addNode(0, targetNode);
				return;
			}

			for (Node dS : stmtList.getNodes()) { // For all the statements
				element = Misc.getInternalFirstCFGNode(dS); // Obtain the CFG element of the statement
				reach = isEndReachable(element);
				if (reach) { // If the end is reachable
					if (dS == stmtList.getNodes().get(stmtList.size() - 1)) { // If this is the last statement
						if (ncfg.getEnd() == baseNode) {
							checkEdge(element, ncfg.getEnd()); // Add end to the succ of this statement
							targetNode = Misc.getCompoundStatementElementWrapper(targetNode);
							stmtList.addNode(stmtList.size() - 1, targetNode);
							return;
						}
						return;
					}
					Node nextElement = Misc
							.getInternalFirstCFGNode(stmtList.getNodes().get(stmtList.getNodes().indexOf(dS) + 1)); // Obtain
																													// the
																													// CFG
																													// element
																													// of
																													// the
																													// next
																													// statement
					if (nextElement == baseNode) {
						checkEdge(element, nextElement); // Add next statement to the succ of the current statement
						targetNode = Misc.getCompoundStatementElementWrapper(targetNode);
						stmtList.addNode(stmtList.getNodes().indexOf(dS) + 1, targetNode);
						return;
					}
				}
			}
		} else {
			if (ncfg.getEnd() == baseNode) {
				checkEdge(ncfg.getBegin(), ncfg.getEnd()); // Connect the begin to end
				targetNode = Misc.getCompoundStatementElementWrapper(targetNode);
				stmtList.addNode(targetNode);
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
		boolean reach;
		NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		Node elementCond = n.getF2(); // Obtain the CFG element of condition and then part
		Node elementThen = Misc.getInternalFirstCFGNode(n.getF4());

		if (elementCond == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		if (elementThen == baseNode) {
			checkEdge(elementCond, elementThen); // Add elementThen to the succ of elementCond
			Statement oldStmt = n.getF4();
			Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, elementCond);
			if (oldStmt != newStmt) {
				n.setF4(newStmt);
			}
		}

		// No need to call accept on elementCond, as it is surely a leaf CFG element
		reach = isEndReachable(elementThen);
		if (reach) {
			if (ncfg.getEnd() == baseNode) {
				checkEdge(elementThen, ncfg.getEnd()); // Add end to the succ of elementThen
				Statement oldStmt = n.getF4();
				Statement newStmt = this.addAfterStatement(targetNode, oldStmt, ncfg.getEnd());
				if (newStmt != oldStmt) {
					n.setF4(newStmt);
				}
			}
		}
		if (n.getF5().present()) { // If "else" part is present
			Node elementElse = Misc.getInternalFirstCFGNode(((NodeSequence) n.getF5().getNode()).getNodes().get(1));
			NodeSequence nodes = ((NodeSequence) n.getF5().getNode());
			Statement elseStmt = (Statement) nodes.getNodes().get(1);
			if (elementElse == baseNode) {
				checkEdge(elementCond, elementElse); // Add elementElse to the succ of elementCond
				Statement oldStmt = elseStmt;
				Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, elementCond);
				if (newStmt != oldStmt) {
					nodes.removeNode(1);
					nodes.addNode(newStmt);
				}
			}
			reach = isEndReachable(elementElse);
			if (reach) { // If end of elementElse is reachable
				if (ncfg.getEnd() == baseNode) {
					checkEdge(elementElse, ncfg.getEnd()); // Add end to the succ of elementElse
					Statement oldStmt = elseStmt;
					Statement newStmt = this.addAfterStatement(targetNode, oldStmt, ncfg.getEnd());
					if (newStmt != oldStmt) {
						nodes.removeNode(1);
						nodes.addNode(newStmt);
					}
				}
			}
		} else { // If "else" isn't present
			if (ncfg.getEnd() == baseNode) {
				// checkEdge(elementCond, ncfg.end); // Add end to the succ of elementCond
				/*
				 * Here, we need to insert the else-clause separately.
				 */
				targetNode = Misc.getCompoundStatementElementWrapper(targetNode);
				NodeListOptional nodeListOptional = new NodeListOptional();
				nodeListOptional.addNode(targetNode);
				CompoundStatement newCompStmt = new CompoundStatement(new NodeToken("{"), nodeListOptional,
						new NodeToken("}"));
				CFGGenerator.createCFGEdgesIn(newCompStmt);
				Statement newStmt = new Statement(new NodeChoice(newCompStmt));
				NodeSequence newNodeSeq = new NodeSequence(2);
				newNodeSeq.addNode(new NodeToken("else", CParserConstants.ELSE, 0, 0, 0, 0));
				newNodeSeq.addNode(newStmt);
				n.setF5(new NodeOptional(newNodeSeq));

				// Taking care of the CFG edges now.
				this.disconnect(elementCond, ncfg.getEnd());
				this.connect(elementCond, newCompStmt);
				this.connect(newCompStmt, ncfg.getEnd());
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
		boolean reach;
		NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG ncfg
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		Node elementCond = n.getF2(); // Obtain the CFG element of cond and body
		Node elementBody = Misc.getInternalFirstCFGNode(n.getF4());

		if (elementCond == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}

		// No need to call accept on elementCond

		reach = isEndReachable(elementBody);
		if (reach) { // If end of the body is reachable
			if (ncfg.getEnd() == baseNode) {
				checkEdge(elementBody, ncfg.getEnd()); // Add end to the succ of elementBody
				Statement oldStmt = n.getF4();
				Statement newStmt = this.addAfterStatement(targetNode, oldStmt, ncfg.getEnd());
				if (newStmt != oldStmt) {
					n.setF4(newStmt);
				}
			}
		}

		Collection<Statement> caseStmts = n.getInfo().getRelevantCFGStatements(); // OLD: Misc.getCaseStatements(n); //
																					// Obtain the vector of all the
																					// immediate cases and default
																					// within the current switch block
		for (Node caseS : caseStmts) { // For all the case statements
			Node elementCase = Misc.getInternalFirstCFGNode(caseS); // Obtain the CFG element of case statement
			if (elementCase == baseNode) {
				checkEdge(elementCond, elementCase); // Add elementCase to the succ of elementCond
				Statement targetStmt = Misc.getStatementWrapper((Statement) targetNode);
				if (caseS instanceof CaseLabeledStatement) {
					Statement oldStmt = ((CaseLabeledStatement) caseS).getF3();
					((CaseLabeledStatement) caseS).setF3(targetStmt);
					BasicTransform.deprecated_insertImmediateSuccessor(targetNode,
							Misc.getInternalFirstCFGNode(oldStmt));
				} else if (caseS instanceof DefaultLabeledStatement) {
					Statement oldStmt = ((DefaultLabeledStatement) caseS).getF2();
					((DefaultLabeledStatement) caseS).setF2(targetStmt);
					BasicTransform.deprecated_insertImmediateSuccessor(targetNode,
							Misc.getInternalFirstCFGNode(oldStmt));
				}
			}
		}

		// If there was no DefaultLabeledStatement, add an edge from Switch's expression
		// to the endSwitchStatement
		if (!n.getInfo().hasDefaultLabel()) {
			if (ncfg.getEnd() == baseNode) {
				// Insert the new defaultLabaeledStatement in the AST.
				CompoundStatement cS = (CompoundStatement) Misc.getInternalFirstCFGNode(n.getF4());
				DefaultLabeledStatement newDefaultLabel = new DefaultLabeledStatement(
						Misc.getStatementWrapper((Statement) targetNode));
				LabeledStatement newLabel = new LabeledStatement(new NodeChoice(newDefaultLabel));
				Statement defaultStmt = new Statement(new NodeChoice(newLabel));
				cS.getF1().addNode(new CompoundStatementElement(new NodeChoice(defaultStmt)));

				// Update CFG edges.
				this.disconnect(elementCond, ncfg.getEnd());
				this.connect(elementCond, targetNode);
				EndNode endNode = cS.getInfo().getCFGInfo().getNestedCFG().getEnd();
				for (Node pred : endNode.getInfo().getCFGInfo().getPredBlocks()) {
					this.connect(pred, targetNode);
					pred.getInfo().getCFGInfo().getSuccBlocks().remove(endNode);
				}
				endNode.getInfo().getCFGInfo().getPredBlocks().clear();
				this.connect(targetNode, endNode);
			}
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
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		Node elementCond = n.getF2(); // Obtain the CFG element of cond and body
		Node elementBody = Misc.getInternalFirstCFGNode(n.getF4());

		// No need to call accept on elementCond

		if (elementCond == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			// Don't return here; we need to add targetNode on one other edge as well.
		}

		if (elementBody == baseNode) {
			checkEdge(elementCond, elementBody); // Add elementBody to the succ of elementCond
			Statement oldStmt = n.getF4();
			Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, elementCond);
			if (oldStmt != newStmt) {
				n.setF4(newStmt);
			}
		}
		if (ncfg.getEnd() == baseNode) {
			BasicTransform.deprecated_insertImmediateSuccessor(n, targetNode);
			return;
		}

		reach = isEndReachable(elementBody);
		if (reach) { // If end of body is reachable
			if (elementCond == baseNode) {
				checkEdge(elementBody, elementCond); // Add elementCond to the succ of elementBody
				Statement oldStmt = n.getF4();
				Statement newStmt = this.addAfterStatement(targetNode, oldStmt, elementCond);
				if (newStmt != oldStmt) {
					n.setF4(newStmt);
				}
			}
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
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		Node elementBody = Misc.getInternalFirstCFGNode(n.getF1()); // Obtain CFG element of body and cond
		Node elementCond = n.getF4();

		if (elementBody == baseNode) {
			checkEdge(ncfg.getBegin(), elementBody); // Add elementBody to the succ of begin

			Statement oldStmt = n.getF1();
			Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, ncfg.getBegin());
			if (oldStmt != newStmt) {
				n.setF1(newStmt);
			}
		}

		reach = isEndReachable(elementBody);
		if (reach) { // If end of elementBody is reachable
			if (elementCond == baseNode) {
				checkEdge(elementBody, elementCond); // Add elementCond to the succ of elementBody
				Statement oldStmt = n.getF1();
				Statement newStmt = this.addAfterStatement(targetNode, oldStmt, elementCond);
				if (newStmt != oldStmt) {
					n.setF1(newStmt);
				}
			}
		}

		if (elementBody == baseNode) {
			checkEdge(elementCond, elementBody); // Add elementBody and end to the succ of elementCond
			Statement oldStmt = n.getF1();
			Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, elementCond);
			if (oldStmt != newStmt) {
				n.setF1(newStmt);
			}
		}
		if (ncfg.getEnd() == baseNode) {
			BasicTransform.deprecated_insertImmediateSuccessor(n, targetNode);
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
		NestedCFG ncfg = n.getInfo().getCFGInfo().getNestedCFG(); // Obtain the NestedCFG "ncfg"
		if (ncfg.getBegin() == baseNode) {
			BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
			return;
		}
		Node elementBody = Misc.getInternalFirstCFGNode(n.getF8()); // Obtain CFG element of the body
		boolean reachStmt = isEndReachable(elementBody);
		Node elementInit, elementTerm, elementStep;
		if (n.getF2().present()) { // If the init is present
			elementInit = n.getF2().getNode(); // Obtain CFG element of the init
			if (elementInit == baseNode) {
				BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
				// Don't return here.
			}

			if (n.getF4().present()) { // If termination condition is present
				elementTerm = n.getF4().getNode(); // Obtain CFG element of the termination condition

				if (elementTerm == baseNode) {
					// CFG changes
					this.disconnect(ncfg.getBegin(), elementInit);
					this.disconnect(elementInit, elementTerm);
					this.connect(ncfg.getBegin(), elementTerm);

					// AST changes
					n.getF2().setNode(null);
					BasicTransform.deprecated_insertImmediatePredecessor(n, elementInit);
					BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
				}
				if (elementBody == baseNode) {
					checkEdge(elementTerm, elementBody); // Add elementBody to the succ of elementTerm
					Statement oldStmt = n.getF8();
					Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, elementTerm);
					if (newStmt != oldStmt) {
						n.setF8(newStmt);
						elementBody = Misc.getInternalFirstCFGNode(n.getF8());
						reachStmt = this.isEndReachable(elementBody);
					}
				}
				if (ncfg.getEnd() == baseNode) {
					BasicTransform.deprecated_insertImmediateSuccessor(n, targetNode);
					return;
				}

				if (n.getF6().present()) { // If step is present
					elementStep = n.getF6().getNode();
					if (reachStmt) { // If the body's end is reachable
						if (elementStep == baseNode) {
							checkEdge(elementBody, elementStep); // Add elementStep to the succ of elementBody
							Statement oldStmt = n.getF8();
							Statement newStmt = this.addAfterStatement(targetNode, oldStmt, elementStep);
							if (newStmt != oldStmt) {
								n.setF8(newStmt);
								elementBody = Misc.getInternalFirstCFGNode(n.getF8());
								reachStmt = this.isEndReachable(elementBody);
							}
						}
					}
					if (elementTerm == baseNode) {
						n.getF6().setNode(null);
						Statement oldStmt = n.getF8();
						Statement newStmt = this.addAfterStatement(elementStep, oldStmt, elementTerm);
						if (newStmt != oldStmt) {
							n.setF8(newStmt);
							elementBody = Misc.getInternalFirstCFGNode(n.getF8());
							reachStmt = this.isEndReachable(elementBody);
						}
						oldStmt = newStmt;
						newStmt = this.addAfterStatement(targetNode, oldStmt, elementTerm);
						if (newStmt != oldStmt) {
							n.setF8(newStmt);
						}
					}
				} else { // If step is not present
					if (reachStmt) { // If the body's end is reachable
						if (elementTerm == baseNode) {
							checkEdge(elementBody, elementTerm); // Add elementTerm to the succ of elementBody
							Statement oldStmt = n.getF8();
							Statement newStmt = this.addAfterStatement(targetNode, oldStmt, elementTerm);
							if (newStmt != oldStmt) {
								n.setF8(newStmt);
								elementBody = Misc.getInternalFirstCFGNode(n.getF8());
								reachStmt = this.isEndReachable(elementBody);
							}
						}
					}
				}
			} else { // If the termination condition is not present
				if (elementBody == baseNode) {
					checkEdge(elementInit, elementBody); // Add elementBody to the succ of elementInit
					Statement oldStmt = n.getF8();
					Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, elementInit);
					if (newStmt != oldStmt) {
						n.setF8(newStmt);
						elementBody = Misc.getInternalFirstCFGNode(n.getF8());
						reachStmt = this.isEndReachable(elementBody);
					}
				}
				if (reachStmt) { // If body's end is reachable
					if (n.getF6().present()) { // If step is present
						elementStep = n.getF6().getNode(); // Obtain CFG element of step

						if (elementStep == baseNode) {
							checkEdge(elementBody, elementStep); // Add elementStep to the succ of elementBody
							Statement oldStmt = n.getF8();
							Statement newStmt = this.addAfterStatement(targetNode, oldStmt, elementStep);
							if (newStmt != oldStmt) {
								n.setF8(newStmt);
								elementBody = Misc.getInternalFirstCFGNode(n.getF8());
								reachStmt = this.isEndReachable(elementBody);
							}
						}
						if (elementBody == baseNode) {
							checkEdge(elementStep, elementBody); // Add elementBody to the succ of elementStep
							Statement oldStmt = n.getF8();
							Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, elementStep);
							if (newStmt != oldStmt) {
								n.setF8(newStmt);
								elementBody = Misc.getInternalFirstCFGNode(n.getF8());
								reachStmt = this.isEndReachable(elementBody);
							}
						}
					} else { // If step is not present
						if (elementBody == baseNode) {
							checkEdge(elementBody, elementBody); // Add elementBody to its own succ
							Statement oldStmt = n.getF8();
							Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, elementBody);
							if (newStmt != oldStmt) {
								n.setF8(newStmt);
								elementBody = Misc.getInternalFirstCFGNode(n.getF8());
								reachStmt = this.isEndReachable(elementBody);
							}
						}
					}
				}
			}
		} else if (n.getF4().present()) { // if init is not present, but the termination condition is present
			elementTerm = n.getF4().getNode(); // Obtain CFG element of the termination condition

			if (elementTerm == baseNode) {
				BasicTransform.deprecated_insertImmediatePredecessor(n, targetNode);
				// Don't return here.
			}
			if (elementBody == baseNode) {
				checkEdge(elementTerm, elementBody); // Add elementBody to the succ of elementTerm
				Statement oldStmt = n.getF8();
				Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, elementTerm);
				if (newStmt != oldStmt) {
					n.setF8(newStmt);
					elementBody = Misc.getInternalFirstCFGNode(n.getF8());
					reachStmt = this.isEndReachable(elementBody);
				}
			}
			if (ncfg.getEnd() == baseNode) {
				BasicTransform.deprecated_insertImmediateSuccessor(n, targetNode);
				return;
			}
			reachStmt = this.isEndReachable(elementBody);
			if (reachStmt) { // If end of the body is reachable
				if (n.getF6().present()) { // If step is present
					elementStep = n.getF6().getNode(); // Obtain CFG element of step

					if (elementStep == baseNode) {
						checkEdge(elementBody, elementStep); // Add elementStep to the succ of elementBody
						Statement oldStmt = n.getF8();
						Statement newStmt = this.addAfterStatement(targetNode, oldStmt, elementStep);
						if (newStmt != oldStmt) {
							n.setF8(newStmt);
							elementBody = Misc.getInternalFirstCFGNode(n.getF8());
							reachStmt = this.isEndReachable(elementBody);
						}
					}
					if (elementTerm == baseNode) {
						checkEdge(elementStep, elementTerm); // Add elementTerm to the succ of elementStep
						n.getF6().setNode(null);
						Statement oldStmt = n.getF8();
						Statement newStmt = this.addAfterStatement(elementStep, oldStmt, elementTerm);
						if (newStmt != oldStmt) {
							n.setF8(newStmt);
							elementBody = Misc.getInternalFirstCFGNode(n.getF8());
							reachStmt = this.isEndReachable(elementBody);
						}
						oldStmt = newStmt;
						newStmt = this.addAfterStatement(targetNode, oldStmt, elementTerm);
						if (newStmt != oldStmt) {
							n.setF8(newStmt);
						}
					}
				} else { // If step is not present
					if (elementTerm == baseNode) {
						checkEdge(elementBody, elementTerm); // Add elementTerm to the succ of elementBody
						Statement oldStmt = n.getF8();
						Statement newStmt = this.addAfterStatement(targetNode, oldStmt, elementTerm);
						if (newStmt != oldStmt) {
							n.setF8(newStmt);
							elementBody = Misc.getInternalFirstCFGNode(n.getF8());
							reachStmt = this.isEndReachable(elementBody);
						}
					}
				}
			} else if (n.getF6().present()) { // If the end of the body is not reachable and the step is present
				elementStep = n.getF6().getNode(); // Obtain CFG element of step

				if (elementTerm == baseNode) {
					// checkEdge(elementStep, elementTerm); // Add elementTerm to the succ of
					// elementStep
					Misc.exitDueToLackOfFeature("Cannot insert any expression between e3 -::= e2 of a for-loop "
							+ "since the body of the loop doesn't reach e3.");
				}
			}

		} else { // if neither init nor term is present
			if (elementBody == baseNode) {
				checkEdge(ncfg.getBegin(), elementBody); // Add the body to the succ of begin
				Statement oldStmt = n.getF8();
				Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, ncfg.getBegin());
				if (newStmt != oldStmt) {
					n.setF8(newStmt);
					elementBody = Misc.getInternalFirstCFGNode(n.getF8());
					reachStmt = this.isEndReachable(elementBody);
				}
			}
			if (reachStmt) { // If body's end is reachable
				if (n.getF6().present()) { // If elementStep is present
					elementStep = n.getF6().getNode(); // Obtain CFG element of step

					if (elementStep == baseNode) {
						checkEdge(elementBody, elementStep); // Add elementStep to the succ of elementBody
						Statement oldStmt = n.getF8();
						Statement newStmt = this.addAfterStatement(targetNode, oldStmt, elementStep);
						if (newStmt != oldStmt) {
							n.setF8(newStmt);
							elementBody = Misc.getInternalFirstCFGNode(n.getF8());
							reachStmt = this.isEndReachable(elementBody);
						}
					}
					if (elementBody == baseNode) {
						checkEdge(elementStep, elementBody); // Add elementBody to the succ of elementStep
						Statement oldStmt = n.getF8();
						Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, elementStep);
						if (newStmt != oldStmt) {
							n.setF8(newStmt);
							elementBody = Misc.getInternalFirstCFGNode(n.getF8());
							reachStmt = this.isEndReachable(elementBody);
						}
					}
				} else { // If elementStep is not present
					if (elementBody == baseNode) {
						checkEdge(elementBody, elementBody); // Add elementBody to its own succ
						Statement oldStmt = n.getF8();
						Statement newStmt = this.addBeforeStatement(targetNode, oldStmt, elementBody);
						if (newStmt != oldStmt) {
							n.setF8(newStmt);
							elementBody = Misc.getInternalFirstCFGNode(n.getF8());
							reachStmt = this.isEndReachable(elementBody);
						}
					}
				}
			}
		}
		return;
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
		FunctionDefinition func = Misc.getEnclosingFunction(n); // Obtain the containing function

		if (func == null) {
			// OLD CODE: If enclosing function is not found, we should add this link later.
			// subRoot.getInfo().getIncompleteSemantics()
			// .addToEdges(new
			// IncompleteEdge(IncompleteEdge.TypeOfIncompleteness.UNKNOWN_GOTO_DESTINATION,
			// n));
			return;
		}

		// Call a visitor which provides the target
		Deprecated_SimpleLabeledStatementGetter labelVisitor = new Deprecated_SimpleLabeledStatementGetter();
		func.accept(labelVisitor, n.getF1().getTokenImage());

		if (labelVisitor.labelNode == null) {
			// OLD CODE: If label not found in the enclosing function, add to the
			// incompleteness and proceed.
			// subRoot.getInfo().getIncompleteSemantics()
			// .addToEdges(new
			// IncompleteEdge(IncompleteEdge.TypeOfIncompleteness.UNKNOWN_GOTO_DESTINATION,
			// n));
			return;
		}

		// Add target to the succ of n (the Goto statement)
		Node target = Misc.getInternalFirstCFGNode(labelVisitor.labelNode);
		if (target == baseNode) {
			// checkEdge(n, target);
		}
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
		Node targetOwn = Misc.getEnclosingLoopOrForConstruct(n); // Obtain the owner loop of this continue statement

		if (targetOwn == null) {
			// OLD CODE: If target-owner is not found, add to the incompleteness and
			// proceed.
			// subRoot.getInfo().getIncompleteSemantics().addToEdges(
			// new
			// IncompleteEdge(IncompleteEdge.TypeOfIncompleteness.UNKNOWN_CONTINUE_DESTINATION,
			// n));
			return;
		}

		Node target = null; // target represents the target statement of this continue

		if (targetOwn instanceof WhileStatement) {
			target = Misc.getInternalFirstCFGNode(((WhileStatement) targetOwn).getF2()); // Target is the termination
																							// condition
		} else if (targetOwn instanceof DoStatement) {
			target = Misc.getInternalFirstCFGNode(((DoStatement) targetOwn).getF4()); // Target is the termination
																						// condition

		} else if (targetOwn instanceof ForStatement) {
			ForStatement forS = ((ForStatement) targetOwn);
			if (forS.getF6().present()) { // If step is present
				target = Misc.getInternalFirstCFGNode(forS.getF6());
			} else if (forS.getF4().present()) { // else if termination condition is present
				target = Misc.getInternalFirstCFGNode(forS.getF4());
			} else {
				target = Misc.getInternalFirstCFGNode(forS.getF8());
			}

		}
		if (target == baseNode) {
			// checkEdge(n, target); // Add the target to the succ of n, i.e., the Continue
			// statement
		}
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
			// subRoot.getInfo().getIncompleteSemantics()
			// .addToEdges(new
			// IncompleteEdge(IncompleteEdge.TypeOfIncompleteness.UNKNOWN_BREAK_DESTINATION,
			// n));
			return;
		}

		Node target = targetOwn.getInfo().getCFGInfo().getNestedCFG().getEnd(); // get the endStatement of the
																				// associated loop/switch
		if (target == baseNode) {
			// checkEdge(n, target); // Add target to the succ of n, i.e., the Break
			// Statement
		}
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
			// OLD CODE:
			// If enclosing function is not found, add to the incompleteness and return.
			// subRoot.getInfo().getIncompleteSemantics()
			// .addToEdges(new
			// IncompleteEdge(IncompleteEdge.TypeOfIncompleteness.UNKNOWN_RETURN_DESTINATION,
			// n));
			return;
		}

		Node target = targetOwn.getInfo().getCFGInfo().getNestedCFG().getEnd(); // get endNode of the enclosing function
		if (target == baseNode) {
			// checkEdge(n, target); // Add target to the succ of n, i.e., the Return
			// statement
		}
		return;
	}

	/**
	 * @param node
	 * @return
	 */
	private AUniqueParallelOrDataClause getAUniqueParallelOrDataClause(Node node) {
		AUniqueParallelOrDataClause aUniqueParallelOrDataClause;
		UniqueParallelClause uniqueParClause;
		uniqueParClause = Misc.getEnclosingNode(node, UniqueParallelClause.class);
		if (uniqueParClause == null) {
			uniqueParClause = new UniqueParallelClause(new NodeChoice(node));
		}
		aUniqueParallelOrDataClause = Misc.getEnclosingNode(uniqueParClause, AUniqueParallelOrDataClause.class);
		if (aUniqueParallelOrDataClause == null) {
			aUniqueParallelOrDataClause = new AUniqueParallelOrDataClause(new NodeChoice(uniqueParClause));
		}
		return aUniqueParallelOrDataClause;
	}

	private Node getATaskClause(Node node) {
		TaskClause taskClause;
		UniqueTaskClause uniqueTaskClause;
		uniqueTaskClause = Misc.getEnclosingNode(node, UniqueTaskClause.class);
		if (uniqueTaskClause == null) {
			if (node instanceof IfClause || node instanceof FinalClause) {
				uniqueTaskClause = new UniqueTaskClause(new NodeChoice(node));
			} else {
				return null;
			}
		}
		taskClause = Misc.getEnclosingNode(uniqueTaskClause, TaskClause.class);
		if (taskClause == null) {
			taskClause = new TaskClause(new NodeChoice(uniqueTaskClause));
		}
		return taskClause;
	}

	/**
	 * This method adds {@code targetNode} as the lasr element of the statement
	 * {@code stmt}, if {@code stmt} is a CompoundStatement.
	 * Otherwise, {@code targetNode} gets added as the last element of a new
	 * CompoundStatment, which wraps {@code stmt} and {@code targetNode}.<br>
	 * Note that this method should be called at those places where the
	 * immediate non-leaf node of {@code stmt} is not a compound-statement
	 * or a function-definition.
	 * 
	 * @param targetNode
	 *                   node that has to be added just after {@code stmt}.
	 * @param stmt
	 *                   statement just after which {@code targetNode} has to be
	 *                   added.
	 * @param
	 * CFG
	 *                   successor of the statement {@code stmt}.
	 * 
	 * @return
	 */
	private Statement addAfterStatement(Node targetNode, Statement stmt, Node successor) {
		Node oldInternalNode = Misc.getInternalFirstCFGNode(stmt);
		boolean selfLoop = false;
		if (successor == oldInternalNode) {
			selfLoop = true;
		}
		CompoundStatement cs;
		this.disconnect(oldInternalNode, targetNode);
		this.disconnect(targetNode, successor);
		if (oldInternalNode instanceof CompoundStatement) {
			// Fix the CFG edges.
			this.connect(oldInternalNode, successor);

			cs = (CompoundStatement) oldInternalNode;
			EndNode endNode = cs.getInfo().getCFGInfo().getNestedCFG().getEnd();
			for (Node pred : endNode.getInfo().getCFGInfo().getPredBlocks()) {
				this.connect(pred, targetNode);
				pred.getInfo().getCFGInfo().getSuccBlocks().remove(endNode);
			}
			endNode.getInfo().getCFGInfo().getPredBlocks().clear();
			this.connect(targetNode, endNode);

			// Fix the AST edges.
			targetNode = Misc.getCompoundStatementElementWrapper(targetNode);
			cs.getF1().addNode(cs.getF1().getNodes().size() - 1, targetNode);
			return stmt;
		} else {
			// Create a new compound-statement; make it a CFG node.
			targetNode = Misc.getCompoundStatementElementWrapper(targetNode);
			NodeListOptional nodeListOptional = new NodeListOptional();
			nodeListOptional.addNode(Misc.getCompoundStatementElementWrapper(oldInternalNode));
			nodeListOptional.addNode(targetNode);
			cs = new CompoundStatement(new NodeToken("{"), nodeListOptional, new NodeToken("}"));
			Statement newStmt = new Statement(new NodeChoice(cs));

			// Fix the CFG edges.
			Node newInternalNode = Misc.getInternalFirstCFGNode(newStmt);
			CFGGenerator.createCFGEdgesIn(newInternalNode);
			if (selfLoop) {
				this.connect(newInternalNode, newInternalNode);
			} else {
				this.connect(newInternalNode, successor);
			}
			List<Node> predList = oldInternalNode.getInfo().getCFGInfo().getPredBlocks();
			for (Node pred : predList) {
				this.connect(pred, newInternalNode);
				pred.getInfo().getCFGInfo().getSuccBlocks().remove(oldInternalNode);
			}
			predList.clear();
			return newStmt;
		}
	}

	/**
	 * This method adds {@code targetNode} as the first element of the statement
	 * {@code stmt},
	 * if {@code stmt} is a CompoundStatement. Otherwise, {@code targetNode}
	 * gets
	 * added as the first element of a new CompoundStatment, which wraps
	 * {@code targetNode}
	 * and {@code stmt}.<br>
	 * Note that this method should be called at those places where the
	 * immediate non-leaf node
	 * of {@code stmt} is not a compound-statement or a function-definition.
	 * 
	 * @param targetNode
	 *                   node that has to be added immediately before {@code stmt}.
	 * @param stmt
	 *                   statement just before which {@code targetNode} has to be
	 *                   added.
	 * @param
	 * CFG
	 *                   predecessor of the statement {@code stmt}.
	 * 
	 * @return
	 */
	private Statement addBeforeStatement(Node targetNode, Statement stmt, Node predecessor) {
		Node oldInternalNode = Misc.getInternalFirstCFGNode(stmt);
		boolean selfLoop = false;
		if (predecessor == oldInternalNode) {
			selfLoop = true;
		}
		CompoundStatement cs;
		this.disconnect(predecessor, targetNode);
		this.disconnect(targetNode, oldInternalNode);
		if (oldInternalNode instanceof CompoundStatement) {
			// Fix the CFG edges.
			this.connect(predecessor, oldInternalNode);

			cs = (CompoundStatement) oldInternalNode;
			BeginNode beginNode = cs.getInfo().getCFGInfo().getNestedCFG().getBegin();
			for (Node succ : beginNode.getInfo().getCFGInfo().getSuccBlocks()) {
				this.connect(targetNode, succ);
				succ.getInfo().getCFGInfo().getPredBlocks().remove(beginNode);
			}
			beginNode.getInfo().getCFGInfo().getSuccBlocks().clear();
			this.connect(beginNode, targetNode);

			// Fix the AST edges.
			targetNode = Misc.getCompoundStatementElementWrapper(targetNode);
			cs.getF1().addNode(0, targetNode);
			return stmt;
		} else {
			// Create a new compound-statement; make it a CFG node.
			targetNode = Misc.getCompoundStatementElementWrapper(targetNode);
			NodeListOptional nodeListOptional = new NodeListOptional();
			nodeListOptional.addNode(targetNode);
			nodeListOptional.addNode(Misc.getCompoundStatementElementWrapper(oldInternalNode));
			cs = new CompoundStatement(new NodeToken("{"), nodeListOptional, new NodeToken("}"));
			Statement newStmt = new Statement(new NodeChoice(cs));

			// Fix the CFG edges.
			Node newInternalNode = Misc.getInternalFirstCFGNode(newStmt);
			CFGGenerator.createCFGEdgesIn(newInternalNode);
			if (selfLoop) {
				this.connect(newInternalNode, newInternalNode);
			} else {
				this.connect(predecessor, newInternalNode);
			}
			List<Node> succList = oldInternalNode.getInfo().getCFGInfo().getSuccBlocks();
			for (Node succ : succList) {
				this.connect(newInternalNode, succ);
				succ.getInfo().getCFGInfo().getPredBlocks().remove(oldInternalNode);
			}
			succList.clear();
			return newStmt;
		}
	}
}
