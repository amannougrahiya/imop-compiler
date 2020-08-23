/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp.yuan;

import imop.ast.annotation.Label;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.GJNoArguDepthFirstProcess;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides method to generate barrier tree corresponding to a
 * {@link ParallelConstruct}
 */
public class BarrierTreeConstructor {

	public static BTNode getBarrierTreeFor(Node node) {
		BarrierTreeConstructionVisitor consVis = new BarrierTreeConstructionVisitor();
		node = Misc.getCFGNodeFor(node);
		return node.accept(consVis);
	}

	private static class BarrierTreeConstructionVisitor extends GJNoArguDepthFirstProcess<BTNode> {

		/**
		 * f0 ::= ( DeclarationSpecifiers() )?
		 * f1 ::= Declarator()
		 * f2 ::= ( DeclarationList() )?
		 * f3 ::= CompoundStatement()
		 */
		@Override
		public BTNode visit(FunctionDefinition n) {
			return n.getInfo().getCFGInfo().getBody().accept(this);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= ParallelDirective()
		 * f2 ::= Statement()
		 */
		@Override
		public BTNode visit(ParallelConstruct n) {
			BTNode ret = null;
			BTNode beginBT = new BTBarrierNode(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
			ret = beginBT;
			BTNode bodyBT = n.getInfo().getCFGInfo().getBody().accept(this);
			if (bodyBT != null) {
				ret = new BTConcatNode(ret, bodyBT);
			}
			BTNode endBT = new BTBarrierNode(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
			ret = new BTConcatNode(ret, endBT);
			return ret;
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <BARRIER>
		 * f2 ::= OmpEol()
		 */
		@Override
		public BTNode visit(BarrierDirective n) {
			return new BTBarrierNode(n);
		}

		/**
		 * f0 ::= "{"
		 * f1 ::= ( CompoundStatementElement() )*
		 * f2 ::= "}"
		 */
		@Override
		public BTNode visit(CompoundStatement n) {
			BTNode ret = null;
			for (Node element : n.getInfo().getCFGInfo().getElementList()) {
				if (element instanceof ParallelConstruct) {
					continue;
				}
				BTNode elemBT = element.accept(this);
				if (elemBT == null) {
					continue;
				}
				if (ret == null) {
					ret = elemBT;
				} else {
					ret = new BTConcatNode(ret, elemBT);
				}
			}
			return ret;
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
		public BTNode visit(IfStatement n) {
			BTNode ret = null;
			List<BTNode> options = new ArrayList<>();
			BTNode thenBT = n.getInfo().getCFGInfo().getThenBody().accept(this);
			options.add(thenBT);
			if (n.getF5().present()) {
				options.add(n.getInfo().getCFGInfo().getElseBody().accept(this));
			} else {
				options.add(null);
			}
			ret = new BTAlternateNode(options, n.getInfo().getCFGInfo().getPredicate());
			return ret;
		}

		/**
		 * f0 ::= <SWITCH>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 * f4 ::= Statement()
		 */
		@Override
		public BTNode visit(SwitchStatement n) {
			BTNode _ret = null;
			List<BTNode> options = new ArrayList<>();
			CompoundStatement switchBody = (CompoundStatement) n.getInfo().getCFGInfo().getBody();
			List<Node> switchStmts = switchBody.getInfo().getCFGInfo().getElementList();
			for (Label l : n.getInfo().getCaseDefaultLabels()) {
				Node dest = l.getLabeledCFGNode();
				dest = Misc.getCFGNodeFor(dest);
				int index = switchStmts.indexOf(dest);
				BTNode btOfThisBranch = null;
				for (int i = index; i < switchStmts.size(); i++) {
					dest = switchStmts.get(i);
					if (dest instanceof Declaration) {
						continue;
					} else if (dest instanceof ParallelConstruct) {
						continue;
					} else if (dest instanceof BreakStatement) {
						break;
					} else {
						BTNode elemBT = dest.accept(this);
						if (elemBT == null) {
							continue;
						}
						if (btOfThisBranch == null) {
							btOfThisBranch = elemBT;
						} else {
							btOfThisBranch = new BTConcatNode(btOfThisBranch, elemBT);
						}
					}
				}
				options.add(btOfThisBranch); // It's fine if this is null.
			}
			_ret = new BTAlternateNode(options, n.getInfo().getCFGInfo().getPredicate());
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
		public BTNode visit(WhileStatement n) {
			BTNode ret = null;
			CompoundStatement body = (CompoundStatement) n.getInfo().getCFGInfo().getBody();
			BTNode bodyBT = body.accept(this);
			if (bodyBT != null) {
				ret = new BTQuantifyNode(bodyBT, n.getInfo().getCFGInfo().getPredicate(),
						(CompoundStatement) n.getInfo().getCFGInfo().getBody());
			}
			return ret;
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
		public BTNode visit(DoStatement n) {
			BTNode ret = null;
			CompoundStatement body = (CompoundStatement) n.getInfo().getCFGInfo().getBody();
			BTNode bodyBT = body.accept(this);
			if (bodyBT != null) {
				ret = new BTQuantifyNode(bodyBT, n.getInfo().getCFGInfo().getPredicate(),
						(CompoundStatement) n.getInfo().getCFGInfo().getBody());
			}
			return ret;
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
		public BTNode visit(ForStatement n) {
			BTNode ret = null;
			CompoundStatement body = (CompoundStatement) n.getInfo().getCFGInfo().getBody();
			BTNode bodyBT = body.accept(this);
			if (bodyBT != null) {
				ret = new BTQuantifyNode(bodyBT, n.getInfo().getCFGInfo().getTerminationExpression(),
						(CompoundStatement) n.getInfo().getCFGInfo().getBody());
			}
			return ret;
		}

		/**
		 * This method should return a {@link BTFunctionNode} that represents
		 * all the
		 * {@link FunctionDefinition}s that this call may correspond to; null,
		 * if there are none.
		 */
		@Override
		public BTNode visit(CallStatement n) {
			List<FunctionDefinition> calledFuncList = n.getInfo().getCalledDefinitions();
			if (calledFuncList.isEmpty()) {
				return null;
			}
			boolean found = false;
			for (FunctionDefinition func : calledFuncList) {
				if (func.getInfo().hasBarrierInCFG()) {
					found = true;
					break;
				}
			}
			if (found) {
				return new BTFunctionNode(n);
			} else {
				return null;
			}
		}
	}

}
