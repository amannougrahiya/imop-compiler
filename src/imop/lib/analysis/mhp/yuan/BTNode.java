/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp.yuan;

import imop.ast.node.external.*;

import java.util.Stack;

/**
 * Abstract class for nodes of a Barrier Tree.
 * 
 * @author Aman Nougrahiya
 *
 */
public abstract class BTNode {
	protected BTNode parent;

	public BTNode getParent() {
		return parent;
	}

	public abstract Node getASTNode();

	public abstract String getString(int tab);

	public abstract Integer getFixedLength();

	public abstract boolean isWellMatched();

	public abstract void populateBTPairs(ParallelConstruct parCons);

	protected static void match(BTNode t1, BTNode t2, Stack<BTNode> leftStack, Stack<BTNode> rightStack,
			ParallelConstruct parCons, boolean up) {
		if (t1 == null || t2 == null) {
			return;
		}
		if (up) {
			// Rule 3:
			if (t2.parent instanceof BTConcatNode) {
				BTNode.match(t1, t2.parent, leftStack, rightStack, parCons, up);
			}
			// Rule 4:
			if (t1.parent instanceof BTConcatNode && t2.parent instanceof BTConcatNode) {
				BTConcatNode t1p = (BTConcatNode) t1.parent;
				BTConcatNode t2p = (BTConcatNode) t2.parent;
				if (t1p.prev == t1 && t2p.prev == t2) {
					BTNode.match(t1p.next, t2p.next, leftStack, rightStack, parCons, !up);
				}
			}
			// Rule 6a:
			if (t2.parent instanceof BTAlternateNode) {
				BTNode.match(t1, t2.parent, leftStack, rightStack, parCons, up);
			}
			// Rule 6b:
			if (t1.parent instanceof BTAlternateNode) {
				BTNode.match(t2, t1.parent, leftStack, rightStack, parCons, up);
			}
			// Rule 8a.
			if (t1 instanceof BTFunctionNode) {
				BTFunctionNode t1f = (BTFunctionNode) t1;
				for (FunctionDefinition func : t1f.callStmt.getInfo().getCalledDefinitions()) {
					BTNode tP = BarrierTreeConstructor.getBarrierTreeFor(func);
					if (tP != null) {
						BTNode t3 = leftStack.pop();
						BTNode.match(t3, t2, leftStack, rightStack, parCons, up);
					}
				}
			}
			// Rule 8b.
			if (t2 instanceof BTFunctionNode) {
				BTFunctionNode t2f = (BTFunctionNode) t2;
				for (FunctionDefinition func : t2f.callStmt.getInfo().getCalledDefinitions()) {
					BTNode tP = BarrierTreeConstructor.getBarrierTreeFor(func);
					if (tP != null) {
						BTNode t3 = rightStack.pop();
						BTNode.match(t3, t1, leftStack, rightStack, parCons, up);
					}
				}
			}
		} else {
			// Rule 1:
			boolean down = up; // as up is false;
			if (t1 instanceof BTBarrierNode && t2 instanceof BTBarrierNode) {
				// Add (t1, t2) to matching set.
				parCons.getInfo().addBTPair((BTBarrierNode) t1, (BTBarrierNode) t2); // TODO: Check why is there a
																						// notion of direction here?
				return;
			}

			// Rule 2:
			if (t2 instanceof BTConcatNode) {
				BTConcatNode t2c = (BTConcatNode) t2;
				BTNode.match(t1, t2c.prev, leftStack, rightStack, parCons, down);
			}
			// Rule 5a:
			if (t2 instanceof BTAlternateNode) {
				BTAlternateNode t2a = (BTAlternateNode) t2;
				for (BTNode child : t2a.options) {
					if (child == null) {
						continue;
					}
					BTNode.match(t1, child, leftStack, rightStack, parCons, down);
				}
			}
			// Rule 5b:
			if (t1 instanceof BTAlternateNode) {
				BTAlternateNode t1a = (BTAlternateNode) t1;
				for (BTNode child : t1a.options) {
					if (child == null) {
						continue;
					}
					BTNode.match(t2, child, leftStack, rightStack, parCons, down);
				}
			}
			// Rule 7a.
			if (t1 instanceof BTFunctionNode) {
				BTFunctionNode t1f = (BTFunctionNode) t1;
				for (FunctionDefinition func : t1f.callStmt.getInfo().getCalledDefinitions()) {
					BTNode tP = BarrierTreeConstructor.getBarrierTreeFor(func);
					if (tP != null) {
						BTNode.match(tP, t2, leftStack, rightStack, parCons, down);
						leftStack.push(t1);
					}
				}
			}
			// Rule 7b.
			if (t2 instanceof BTFunctionNode) {
				BTFunctionNode t2f = (BTFunctionNode) t2;
				for (FunctionDefinition func : t2f.callStmt.getInfo().getCalledDefinitions()) {
					BTNode tP = BarrierTreeConstructor.getBarrierTreeFor(func);
					if (tP != null) {
						BTNode.match(tP, t1, leftStack, rightStack, parCons, down);
						rightStack.push(t2);
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return this.getString(0);
	}

	protected String printTabs(int num) {
		String str = "";
		for (int i = 0; i < num - 1; i++) {
			str += "    "; // 4 spaces
		}
		str += "----";
		return str;
	}
}
