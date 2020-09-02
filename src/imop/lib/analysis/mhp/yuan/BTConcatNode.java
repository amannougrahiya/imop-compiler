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

/**
 * Represents a concatenation node in Barrier Tree.
 * It contains two children -- prev and next.
 * 
 * @author Aman Nougrahiya
 *
 */
public class BTConcatNode extends BTNode {
	public final BTNode prev;
	public final BTNode next;

	public BTConcatNode(BTNode prev, BTNode next) {
		this.prev = prev;
		this.next = next;
		this.prev.parent = this;
		this.next.parent = this;
	}

	public Node getASTNode() {
		return null;
	}

	@Override
	public String getString(int tab) {
		String str = this.printTabs(tab);
		str += "o\n";
		str += this.prev.getString(tab + 1);
		str += this.next.getString(tab + 1);
		return str;
	}

	@Override
	public Integer getFixedLength() {
		Integer prevCount = prev.getFixedLength();
		Integer nextCount = next.getFixedLength();
		if (prevCount == null || nextCount == null) {
			return null;
		}
		return prevCount + nextCount;
	}

	@Override
	public boolean isWellMatched() {
		if (!this.prev.isWellMatched() || !this.next.isWellMatched()) {
			return false;
		}
		return true;
	}

	@Override
	public void populateBTPairs(ParallelConstruct parCons) {
		this.prev.populateBTPairs(parCons);
		this.next.populateBTPairs(parCons);
	}
}
