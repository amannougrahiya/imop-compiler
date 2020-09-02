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

/**
 * Represents quantification node of a Barrier Tree.<br>
 * It is composed up of a barrier expression denoting the barrier tree within
 * its associated loop.
 * 
 * @author Aman Nougrahiya
 *
 */
public class BTQuantifyNode extends BTNode {
	public final BTNode loopTree;
	public final Expression predicate;
	private final CompoundStatement loopBody;

	public BTQuantifyNode(BTNode loopTree, Expression predicate, CompoundStatement loopBody) {
		this.loopTree = loopTree;
		this.loopTree.parent = this;
		this.predicate = predicate;
		this.loopBody = loopBody;
	}

	public Node getASTNode() {
		return this.predicate;
	}

	public boolean isConcurrent() {
		if (!SVEChecker.isSingleValuedPredicate(predicate)) {
			return false; // Testing..
		}
		// For loopBody.
		return false;
	}

	@Override
	public String getString(int tab) {
		String str = this.printTabs(tab);
		str += "* (" + (this.isConcurrent() ? "MVE" : "SVE") + ")\n";
		str += this.loopTree.getString(tab + 1);
		return str;
	}

	@Override
	public Integer getFixedLength() {
		return null;
	}

	@Override
	public boolean isWellMatched() {
		if (!this.loopTree.isWellMatched()) {
			return false;
		}
		return !this.isConcurrent();
	}

	@Override
	public void populateBTPairs(ParallelConstruct parCons) {
		this.loopTree.populateBTPairs(parCons);
	}

}
