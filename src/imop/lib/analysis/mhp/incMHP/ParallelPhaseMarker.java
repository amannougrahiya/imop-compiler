/*
 *   Copyright (c) 2020 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 *   This file is a part of the project IMOP, licensed under the MIT license.
 *   See LICENSE.md for the full text of the license.
 *
 *   The above notice shall be included in all copies or substantial
 *   portions of this file.
 */
package imop.lib.analysis.mhp.incMHP;

import imop.ast.info.cfgNodeInfo.FunctionDefinitionInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.cfgTraversals.GJVoidDepthFirstCFG;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.Misc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Marks the visited node with provided phase.
 * 
 * @author Aman Nougrahiya
 *
 */
public class ParallelPhaseMarker extends GJVoidDepthFirstCFG<CallStack> {

	private HashMap<Node, Set<CallStack>> visitedMap;

	public Phase ph;

	public ParallelPhaseMarker(Phase ph) {
		this.ph = ph;
		visitedMap = new HashMap<>();
	}

	/**
	 * This method adds phase {@code ph} to the {@code phaseList} of
	 * {@code node},
	 * and {@code node} to {@code nodeSet} of {@code ph}.
	 * It also adds the current callStack to the {@link #visitedMap} for the key
	 * {@code node}.
	 * 
	 * @param node
	 * @return returns true if the {@code node} wasn't already visited with the
	 *         same phase (and callStack).
	 */
	public boolean addPhase(Node node, CallStack callStack) {
		Set<CallStack> visitedCallStacks = visitedMap.get(node);
		if (visitedCallStacks == null) {
			visitedCallStacks = new HashSet<>();
			visitedCallStacks.add(callStack);
			visitedMap.put(node, visitedCallStacks);
		} else {
			if (visitedCallStacks.contains(callStack)) {
				return false;
			} else {
				visitedCallStacks.add(callStack);
			}
		}
		ph.addNode(node);
		return true;
	}

	/**
	 * Processes (non-interesting) leaf nodes -- add phase, visit successors;
	 * <br>
	 * Processes non-leaf nodes == visit beginNode.
	 */
	@Override
	public void initProcess(Node n, CallStack callStack) {
		// assert (!(n instanceof BarrierDirective));
		// assert (!(n instanceof EndNode));
		// assert (!(n instanceof ParallelConstruct));
		if (Misc.isCFGLeafNode(n)) {
			if (addPhase(n, callStack)) {
				for (NodeWithStack successor : n.getInfo().getCFGInfo().getInterProceduralLeafSuccessors(callStack)) {
					successor.getNode().accept(this, successor.getCallStack());
				}
			}
		} else if (Misc.isCFGNonLeafNode(n)) {
			n.getInfo().getCFGInfo().getNestedCFG().getBegin().accept(this, callStack);
		}
		return;
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public void visit(FunctionDefinition n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(Declaration n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ParameterAbstraction()
	 */
	@Override
	public void visit(ParameterDeclaration n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public void visit(UnknownCpp n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(ParallelConstruct n, CallStack callStack) {
		for (NodeWithStack temp : n.getInfo().getCFGInfo().getIntraTaskCFGLeafContents(callStack)) {
			addPhase(temp.getNode(), temp.getCallStack());
		}
		for (NodeWithStack successor : n.getInfo().getCFGInfo().getInterProceduralLeafSuccessors(callStack)) {
			successor.getNode().accept(this, successor.getCallStack());
		}
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public void visit(UnknownPragma n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(IfClause n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(NumThreadsClause n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(ForConstruct n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForInitExpression n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	@Override
	public void visit(OmpForCondition n, CallStack callStack) {
		initProcess(n, callStack);
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
	public void visit(OmpForReinitExpression n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTIONS>
	 * f2 ::= NowaitDataClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= SectionsScope()
	 */
	@Override
	public void visit(SectionsConstruct n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SINGLE>
	 * f2 ::= SingleClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SingleConstruct n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASK>
	 * f2 ::= ( TaskClause() )*
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(TaskConstruct n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(FinalClause n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(MasterConstruct n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(CriticalConstruct n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= ExpressionStatement()
	 */
	@Override
	public void visit(AtomicConstruct n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public void visit(FlushDirective n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(OrderedConstruct n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(BarrierDirective n, CallStack callStack) {
		addPhase(n, callStack);
		ph.addEndPointNoUdpate(new EndPhasePoint(n, callStack));
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskwaitDirective n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskyieldDirective n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(ExpressionStatement n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public void visit(CallStatement n, CallStack callStack) {
		initProcess(n, callStack);
		// Old code:
		// /*
		// * Note that in case of recursion, when we enter a
		// * function-definition for the third time
		// * with the same callSite (callStatement), then the BeginNode will
		// * not be visiting
		// * the successors due to the code written in initProcess() for
		// * BeginNode.
		// */
		//
		// CallStatement callSt = n;
		// if (callSt.getInfo().getCalleeDefinitions().isEmpty()) {
		// /*
		// * In this case, the function definition is not present.
		// * We should move ahead with the marking of the phases.
		// */
		// initProcess(n, callStack);
		// } else {
		// addPhase(n, callStack);
		// for (FunctionDefinition callee : callSt.getInfo().getCalleeDefinitions()) {
		// CallStack windCallStack = CallStack.pushUnique(callStack, callSt);
		// callee.accept(this, windCallStack);
		// }
		// }
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public void visit(CompoundStatement n, CallStack callStack) {
		initProcess(n, callStack);
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
	public void visit(IfStatement n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SwitchStatement n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= <WHILE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(WhileStatement n, CallStack callStack) {
		initProcess(n, callStack);
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
	public void visit(DoStatement n, CallStack callStack) {
		initProcess(n, callStack);
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
	public void visit(ForStatement n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public void visit(GotoStatement n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ContinueStatement n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(BreakStatement n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(ReturnStatement n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(Expression n, CallStack callStack) {
		initProcess(n, callStack);
	}

	@Override
	public void visit(DummyFlushDirective n, CallStack callStack) {
		initProcess(n, callStack);
	}

	@Override
	public void visit(PreCallNode n, CallStack callStack) {
		CallStatement callSt = n.getParent();
		if (callSt.getInfo().getCalledDefinitions().isEmpty()) {
			/*
			 * In this case, the function definition is not present.
			 * We should move ahead with the marking of the phases,
			 * assuming that there are no barriers in the called function.
			 */
			initProcess(n, callStack);
		} else {
			if (addPhase(n, callStack)) {
				for (FunctionDefinition callee : callSt.getInfo().getCalledDefinitions()) {
					CallStack windCallStack = CallStack.pushUnique(callStack, callSt);
					callee.accept(this, windCallStack);
				}
			}
		}
	}

	@Override
	public void visit(PostCallNode n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * Special Node
	 */
	@Override
	public void visit(BeginNode n, CallStack callStack) {
		initProcess(n, callStack);
	}

	/**
	 * Special Node
	 */
	@Override
	public void visit(EndNode n, CallStack callStack) {
		boolean proceed = addPhase(n, callStack);
		if (!proceed) {
			return;
		}
		if (n.getParent() instanceof FunctionDefinition) {
			FunctionDefinitionInfo funcDefInfo = (FunctionDefinitionInfo) n.getParent().getInfo();
			if (callStack.peek() == CallStatement.getPhantomCall()) {
				/*
				 * Context-insensitive traversal.
				 * Visit the successors of all the possible callStatements.
				 * Note that the call-statements are added with both, incoming
				 * and outgoing phases.
				 */
				for (CallStatement cS : funcDefInfo.getCallersOfThis()) {
					Node callerCFGNode = cS.getPostCallNode();
					// TODO: We could have simply called this visitor on callerCFGNode, instead of
					// the code below.
					if (this.addPhase(callerCFGNode, callStack)) {
						for (NodeWithStack successor : callerCFGNode.getInfo().getCFGInfo()
								.getInterProceduralLeafSuccessors(callStack)) {
							successor.getNode().accept(this, successor.getCallStack());
						}
					}
				}
			} else {
				/*
				 * Context-sensitive traversal.
				 */
				CallStack unwindCallStack = CallStack.pop(callStack);
				CallStatement cS = callStack.peek();
				Node callerCFGNode = cS.getPostCallNode();
				// TODO: We could have simply called this visitor on callerCFGNode, instead of
				// the code below.
				if (this.addPhase(callerCFGNode, unwindCallStack)) {
					for (NodeWithStack successor : callerCFGNode.getInfo().getCFGInfo()
							.getInterProceduralLeafSuccessors(unwindCallStack)) {
						successor.getNode().accept(this, successor.getCallStack());
					}
				}
			}
			return;
		} else if (n.getParent() instanceof ParallelConstruct && n.getParent() == ph.getParConstruct()) {
			// assert (callStack.isEmpty());
			addPhase(ph.getParConstruct().getInfo().getCFGInfo().getNestedCFG().getEnd(), callStack);
			ph.addEndPointNoUdpate(
					new EndPhasePoint(ph.getParConstruct().getInfo().getCFGInfo().getNestedCFG().getEnd(), callStack));
			return;
		} else {
			for (NodeWithStack successor : n.getParent().getInfo().getCFGInfo()
					.getInterProceduralLeafSuccessors(callStack)) {
				successor.getNode().accept(this, successor.getCallStack());
			}
		}
	}

}
/// **
// * Given a phase ph, this visitor marks with ph all the nodes
// * reachable from n on a barrier-free path. <br>
// * Note that the marking is done for only the leaf nodes. Non-leaf nodes
// * are not marked with phases since they may contain a barrier.
// * Furthermore, a barrier connects to the phase which ends at it.
// */
// public class ParallelPhaseMarker extends GJVoidDepthFirstCFG<CallStack> {
//
// public static int k = 3;
// private HashMap<Node, Set<CallStack>> visitedMap;
//
// public Phase ph;
//
// public ParallelPhaseMarker(Phase ph) {
// this.ph = ph;
// visitedMap = new HashMap<>();
// }
//
// /**
// * This method adds phase {@code ph} to the {@code phaseList} of
// * {@code node},
// * and {@code node} to {@code nodeSet} of {@code ph}.
// * It also adds the current callStack to the visitedMap for the key
// * {@code n}
// *
// * @param node
// * @return returns true if the {@code node} wasn't already visited with the
// * same phase (and callStack).
// */
// public boolean addPhase(Node node, CallStack callStack) {
// Set<CallStack> visitedCallStacks = visitedMap.get(node);
// if (visitedCallStacks == null) {
// visitedCallStacks = new Set<CallStack>();
// visitedCallStacks.add(callStack);
// visitedMap.put(node, visitedCallStacks);
// } else {
// if (visitedCallStacks.contains(callStack)) {
// return false;
// } else {
// visitedCallStacks.add(callStack);
// }
// }
// ph.nodeSet.add(node);
// node.getInfo().getPhaseInfo().phaseList.add(ph);
// return true;
// }
//
// /**
// * This method marks the node n with ph, if not already marked (with same
// * callStack).
// * If already marked (with same callStack), this returns false;
// * If n is a BarrierDirective, it adds n to the endPoints of ph, and returns
// * false;
// *
// * @return Returns true if customProcessCalls/endProcess should be called
// * after this.
// */
// public boolean customInitProcess(Node n, CallStack callStack) {
// if (!Misc.isCFGNode(n)) {
// System.out.println(
// "Some error in customInitProcess. Node " + n.getClass().getSimpleName() + "
/// isn't a CFG node!");
// Thread.dumpStack();
// System.exit(0);
// }
//
// // Don't mark any non-leaf CFG node with the phase Information
// if (Misc.isCFGNonLeafNode(n) && !(n instanceof ParallelConstruct)) {
// return true;
// }
//
// // Add the phase information to n, and return false if information is already
/// present there.
// boolean tempRet = addPhase(n, callStack);
// if (!tempRet) {
// return false;
// }
//
// // If n is a BarrierDirective, add n with callStack to the phase's endPoints
// if (n instanceof BarrierDirective) {
// ph.endPoints.add(new PhasePoint(n, new CallStack(callStack)));
// return false;
// }
// return true;
// }
//
// /**
// * This method passes control to the first function call encountered in
// * expressionInN.
// * If no method with definition is present, it returns true to endProcess,
// * for proceeding with analysis.
// * This also implements the limit on the context-sensitivity with k depth.
// * k is set as a static int for this class.
// *
// * @param expressionInN
// * This node belongs to the (may be non-)CFG node within n which
// * has the Expression.
// * @param callStack
// * @return Returns true if endProcess should be called after this.
// */
// public boolean customProcessCalls(Node expressionInN, CallStack callStack) {
// // Get the call sites
// CallSiteGetter callGetter = new CallSiteGetter();
// expressionInN.accept(callGetter);
// Vector<CallSite> callSiteList = callGetter.callSiteList;
//
// // Process the first call-site available
// CallStack newCallSiteStack = (CallStack) callStack.clone();
// for (CallSite callSite : callSiteList) {
// if (callSite.calleeDefinition == null)
// continue;
// else {
// if (callStack.isEmpty()) {
// // System.out.println("Pushing the function " + callSite.calleeName);
// newCallSiteStack.push(callSite);
// } else {
// if (callStack.peek().isEmptyCallSite()) {
// // push nothing
// } else {
// if (callSite.presentKTimesIn(callStack, k)) {
// // System.out.println("Pushing the empty function!");
// newCallSiteStack.push(new CallSite("", null, null)); // push an emptyCallSite
// } else {
// // System.out.println("Pushing the function " + callSite.calleeName);
// newCallSiteStack.push(callSite);
// }
// }
// }
// callSite.calleeDefinition.accept(this, newCallSiteStack);
// return false;
// }
// } // end of for
// return true;
// }
//
// /**
// * If called, this method makes the phase information flow to the nestedCFG
// * and succBlock()
// * in case of non-leaf and leaf CFG nodes respectively.
// * Not called for EndNode.
// */
// @Override
// public void endProcess(Node n, CallStack callStack) {
//
// /**
// * If n is a leaf CFG node OR a ParallelConstruct, make the phase
// * Information flow
// * to all its successors.
// */
// if (Misc.isCFGLeafNode(n) || n instanceof ParallelConstruct) {
// if (n.getInfo().getCFGInfo().getSuccBlocks().size() == 1) {
// Node successor = n.getInfo().getCFGInfo().getSuccBlocks().get(0);
// //System.out.println("Accepting from endProcess, the node " +
/// successor.getClass().getSimpleName()
// // + " of " + successor.parent.getClass().getSimpleName());
// successor.accept(this, callStack);
// } else {
// for (Node successor : n.getInfo().getCFGInfo().getSuccBlocks()) {
// successor.accept(this, (CallStack) callStack.clone());
// }
// }
// } else if (Misc.isCFGNonLeafNode(n)) {
// // Call processing on the beginNode (and not on any successors right now)
// BeginNode beginNode = n.getInfo().getCFGInfo().getNestedCFG().begin;
// beginNode.accept(this, callStack);
// }
// }
//
// /**
// * Special Node
// */
// @Override
// public void visit(EndNode n, CallStack callStack) {
// if (n.parent instanceof FunctionDefinition) {
// FunctionDefinitionInfo funcDefInfo = (FunctionDefinitionInfo)
/// n.parent.getInfo();
// assert (callStack.isEmpty());
// if (callStack.peek() == CallStatement.getPhantomCall()) {
// /*
// * Context-insensitive traversal.
// * Visit the successors of all the possible callStatements.
// * Note that the call-statements are added with both, incoming
// * and outgoing
// * phases.
// */
// for (CallStatement cS : funcDefInfo.getCallers()) {
// Node callerCFGNode = cS.getOwnerStatement();
// this.addPhase(callerCFGNode, callStack);
// for (Node successor : callerCFGNode.getInfo().getCFGInfo().getSuccBlocks()) {
// successor.accept(this, callStack);
// }
// }
// } else {
// /*
// * Context-sensitive traversal.
// */
// CallStack unwindCallStack = new CallStack(callStack);
// CallStatement cS = unwindCallStack.pop();
// Node callerCFGNode = cS.getOwnerStatement();
// this.addPhase(callerCFGNode, unwindCallStack);
// for (Node successor : callerCFGNode.getInfo().getCFGInfo().getSuccBlocks()) {
// successor.accept(this, unwindCallStack);
// }
// }
// return;
// } else if (n.parent instanceof ParallelConstruct) {
// assert (callStack.isEmpty());
// ph.endPoints.add(new PhasePoint(ph.parConstruct, callStack));
// return;
// } else {
// /*
// * For:
// * CompoundStatement, IfStatement, SwitchStatement,
// * WhileStatement, DoStatement, ForStatement,
// * TaskConstruct, MasterConstrcut, CriticalConstruct,
// * AtomicConstruct
// * SectionsConstruct, ForConstruct, and SingleConstruct.
// * In these cases, we shall visit the parent's succBlock.
// * Note that there are no implicit barriers in the code now.
// */
// for (Node successor : n.parent.getInfo().getCFGInfo().getSuccBlocks()) {
// successor.accept(this, callStack);
// }
// }
// }
//
// /**
// * Special Node
// */
// @Override
// public void visit(BeginNode n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= ( DeclarationSpecifiers() )?
// * f1 ::= Declarator()
// * f2 ::= ( DeclarationList() )?
// * f3 ::= CompoundStatement()
// */
// @Override
// public void visit(FunctionDefinition n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= DeclarationSpecifiers()
// * f1 ::= ( InitDeclaratorList() )?
// * f2 ::= ";"
// */
// @Override
// public void visit(Declaration n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// ret = customProcessCalls(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= "#"
// * f1 ::= <UNKNOWN_CPP>
// */
// @Override
// public void visit(UnknownCpp n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= ParallelDirective()
// * f2 ::= Statement()
// */
// @Override
// public void visit(ParallelConstruct n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// ret = customProcessCalls(n.f1, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= "#"
// * f1 ::= <PRAGMA>
// * f2 ::= <UNKNOWN_CPP>
// */
// @Override
// public void visit(UnknownPragma n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= ForDirective()
// * f2 ::= OmpForHeader()
// * f3 ::= Statement()
// */
// @Override
// public void visit(ForConstruct n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= <IDENTIFIER>
// * f1 ::= "="
// * f2 ::= Expression()
// */
// @Override
// public void visit(OmpForInitExpression n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// ret = customProcessCalls(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpForLTCondition()
// * | OmpForLECondition()
// * | OmpForGTCondition()
// * | OmpForGECondition()
// */
// @Override
// public void visit(OmpForCondition n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// ret = customProcessCalls(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= PostIncrementId()
// * | PostDecrementId()
// * | PreIncrementId()
// * | PreDecrementId()
// * | ShortAssignPlus()
// * | ShortAssignMinus()
// * | OmpForAdditive()
// * | OmpForSubtractive()
// * | OmpForMultiplicative()
// */
// @Override
// public void visit(OmpForReinitExpression n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// ret = customProcessCalls(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <SECTIONS>
// * f2 ::= NowaitDataClauseList()
// * f3 ::= OmpEol()
// * f4 ::= SectionsScope()
// */
// @Override
// public void visit(SectionsConstruct n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <SINGLE>
// * f2 ::= SingleClauseList()
// * f3 ::= OmpEol()
// * f4 ::= Statement()
// */
// @Override
// public void visit(SingleConstruct n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <TASK>
// * f2 ::= ( TaskClause() )*
// * f3 ::= OmpEol()
// * f4 ::= Statement()
// */
// @Override
// public void visit(TaskConstruct n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// ret = customProcessCalls(n.f2, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <PARALLEL>
// * f2 ::= <FOR>
// * f3 ::= UniqueParallelOrUniqueForOrDataClauseList()
// * f4 ::= OmpEol()
// * f5 ::= OmpForHeader()
// * f6 ::= Statement()
// */
// @Override
// public void visit(ParallelForConstruct n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// ret = customProcessCalls(n.f3, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <PARALLEL>
// * f2 ::= <SECTIONS>
// * f3 ::= UniqueParallelOrDataClauseList()
// * f4 ::= OmpEol()
// * f5 ::= SectionsScope()
// */
// @Override
// public void visit(ParallelSectionsConstruct n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// ret = customProcessCalls(n.f3, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <MASTER>
// * f2 ::= OmpEol()
// * f3 ::= Statement()
// */
// @Override
// public void visit(MasterConstruct n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <CRITICAL>
// * f2 ::= ( RegionPhrase() )?
// * f3 ::= OmpEol()
// * f4 ::= Statement()
// */
// @Override
// public void visit(CriticalConstruct n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <ATOMIC>
// * f2 ::= ( AtomicClause() )?
// * f3 ::= OmpEol()
// * f4 ::= ExpressionStatement()
// */
// @Override
// public void visit(AtomicConstruct n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <FLUSH>
// * f2 ::= ( FlushVars() )?
// * f3 ::= OmpEol()
// */
// @Override
// public void visit(FlushDirective n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <ORDERED>
// * f2 ::= OmpEol()
// * f3 ::= Statement()
// */
// @Override
// public void visit(OrderedConstruct n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <BARRIER>
// * f2 ::= OmpEol()
// */
// @Override
// public void visit(BarrierDirective n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <TASKWAIT>
// * f2 ::= OmpEol()
// */
// @Override
// public void visit(TaskwaitDirective n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= OmpPragma()
// * f1 ::= <TASKYIELD>
// * f2 ::= OmpEol()
// */
// @Override
// public void visit(TaskyieldDirective n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= ( Expression() )?
// * f1 ::= ";"
// */
// @Override
// public void visit(ExpressionStatement n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// ret = customProcessCalls(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= "{"
// * f1 ::= ( CompoundStatementElement() )*
// * f2 ::= "}"
// */
// @Override
// public void visit(CompoundStatement n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= <IF>
// * f1 ::= "("
// * f2 ::= Expression()
// * f3 ::= ")"
// * f4 ::= Statement()
// * f5 ::= ( <ELSE> Statement() )?
// */
// @Override
// public void visit(IfStatement n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= <SWITCH>
// * f1 ::= "("
// * f2 ::= Expression()
// * f3 ::= ")"
// * f4 ::= Statement()
// */
// @Override
// public void visit(SwitchStatement n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= <WHILE>
// * f1 ::= "("
// * f2 ::= Expression()
// * f3 ::= ")"
// * f4 ::= Statement()
// */
// @Override
// public void visit(WhileStatement n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= <DO>
// * f1 ::= Statement()
// * f2 ::= <WHILE>
// * f3 ::= "("
// * f4 ::= Expression()
// * f5 ::= ")"
// * f6 ::= ";"
// */
// @Override
// public void visit(DoStatement n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= <FOR>
// * f1 ::= "("
// * f2 ::= ( Expression() )?
// * f3 ::= ";"
// * f4 ::= ( Expression() )?
// * f5 ::= ";"
// * f6 ::= ( Expression() )?
// * f7 ::= ")"
// * f8 ::= Statement()
// */
// @Override
// public void visit(ForStatement n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= <GOTO>
// * f1 ::= <IDENTIFIER>
// * f2 ::= ";"
// */
// @Override
// public void visit(GotoStatement n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= <CONTINUE>
// * f1 ::= ";"
// */
// @Override
// public void visit(ContinueStatement n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= <BREAK>
// * f1 ::= ";"
// */
// @Override
// public void visit(BreakStatement n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= <RETURN>
// * f1 ::= ( Expression() )?
// * f2 ::= ";"
// */
// @Override
// public void visit(ReturnStatement n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// ret = customProcessCalls(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
// /**
// * f0 ::= AssignmentExpression()
// * f1 ::= ( "," AssignmentExpression() )*
// */
// @Override
// public void visit(Expression n, CallStack callStack) {
// boolean ret = customInitProcess(n, callStack);
// if (ret)
// ret = customProcessCalls(n, callStack);
// if (ret)
// endProcess(n, callStack);
// }
//
