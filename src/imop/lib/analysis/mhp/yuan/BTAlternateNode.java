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
import imop.lib.analysis.SVEChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Represents an alternation node of a Barrier Tree.<br>
 * It contains a list of children, identified by their index in the list. <br>
 * Note that we use a list instead of pair so that we can also handle switch
 * statements. <br>
 * Also note that to denote an empty sub tree for an option, we add NULL
 * instead, so that index to branch mapping is not disturbed.
 * 
 * @author Aman Nougrahiya
 *
 */
public class BTAlternateNode extends BTNode {
	public final List<BTNode> options = new ArrayList<>();
	public final Expression predicate;

	public BTAlternateNode(List<BTNode> options, Expression predicate) {
		assert (options != null);
		this.options.addAll(options);
		for (BTNode btn : options) {
			if (btn != null) {
				btn.parent = this;
			}
		}
		this.predicate = predicate;
	}

	public Node getASTNode() {
		return this.predicate;
	}

	public boolean isConcurrent() {
		if (!SVEChecker.isSingleValuedPredicate(predicate)) {
			return true;
		}
		return false;
	}

	@Override
	public String getString(int tab) {
		String str = this.printTabs(tab);
		str += "| (" + (this.isConcurrent() ? "MVE" : "SVE") + ")\n";
		for (BTNode btn : this.options) {
			if (btn == null) {
				str += this.printTabs(tab + 1);
				str += "NO_OP\n";
			} else {
				str += btn.getString(tab + 1);
			}
		}
		return str;
	}

	@Override
	public Integer getFixedLength() {
		Integer count = options.get(0).getFixedLength();
		if (count == null) {
			return null;
		}
		for (BTNode child : options) {
			if (child == null) {
				if (count == 0) {
					continue;
				} else {
					return null;
				}
			}
			if (count != child.getFixedLength()) {
				return null;
			}
		}
		return count;
	}

	@Override
	public boolean isWellMatched() {
		for (BTNode child : options) {
			if (!child.isWellMatched()) {
				return false;
			}
		}
		if (this.isConcurrent()) {
			return this.getFixedLength() != null;
		} else {
			return true;
		}
	}

	@Override
	public void populateBTPairs(ParallelConstruct parCons) {
		if (!this.isConcurrent()) {
			// Do nothing.
			return;
		} else {
			Stack<BTNode> leftStack = new Stack<>();
			Stack<BTNode> rightStack = new Stack<>();
			for (BTNode c1 : options) {
				if (c1 == null) {
					continue;
				}
				for (BTNode c2 : options) {
					if (c2 == null) {
						continue;
					}
					leftStack.clear();
					rightStack.clear();
					BTNode.match(c1, c2, leftStack, rightStack, parCons, false);
				}
			}
		}
	}
}
