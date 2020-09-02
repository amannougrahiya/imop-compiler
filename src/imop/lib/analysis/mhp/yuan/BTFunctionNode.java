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
import imop.ast.node.internal.*;

import java.util.List;

/**
 * Represents a function node in a Barrier Tree.
 * Note that a function node may correspond to more than one
 * FunctionDefinitions; hence, the list.
 * 
 * @author Aman Nougrahiya
 *
 */
public class BTFunctionNode extends BTNode {
	public final CallStatement callStmt;

	public BTFunctionNode(CallStatement callStmt) {
		assert (!callStmt.getInfo().getCalledDefinitions().isEmpty());
		this.callStmt = callStmt;
	}

	public Node getASTNode() {
		return this.callStmt;
	}

	public boolean isConcurrent() {
		List<FunctionDefinition> calledFunctions = this.callStmt.getInfo().getCalledDefinitions();
		if (calledFunctions.isEmpty() || calledFunctions.size() == 1) {
			return false;
		} else {
			// Invoke SVEChecker.
			return true;
		}
	}

	@Override
	public String getString(int tab) {
		String str = this.printTabs(tab);
		str += callStmt + "(" + (this.isConcurrent() ? "MVE" : "SVE") + ")\n";
		return str;
	}

	@Override
	public Integer getFixedLength() {
		List<FunctionDefinition> calledFunctions = this.callStmt.getInfo().getCalledDefinitions();
		if (calledFunctions.isEmpty()) {
			return 0;
		}
		Integer count = BarrierTreeConstructor.getBarrierTreeFor(calledFunctions.get(0)).getFixedLength();
		if (count == null) {
			return null;
		}
		int i = 0;
		for (FunctionDefinition func : calledFunctions) {
			if (i == 0) {
				i++;
				continue;
			}
			if (func.getInfo().isRecursive()) {
				return null;
			}
			if (count != BarrierTreeConstructor.getBarrierTreeFor(func).getFixedLength()) {
				return null;
			}

		}
		return count;
	}

	@Override
	public boolean isWellMatched() {
		List<FunctionDefinition> calledFunctions = this.callStmt.getInfo().getCalledDefinitions();
		if (calledFunctions.isEmpty()) {
			return true;
		}
		for (FunctionDefinition func : calledFunctions) {
			if (!BarrierTreeConstructor.getBarrierTreeFor(func).isWellMatched()) {
				return false;
			} // ...
		}
		return true;
	}

	@Override
	public void populateBTPairs(ParallelConstruct parCons) {
		List<FunctionDefinition> calledFunctions = this.callStmt.getInfo().getCalledDefinitions();
		if (calledFunctions.isEmpty()) {
			return;
		}
		for (FunctionDefinition func : calledFunctions) {
			BarrierTreeConstructor.getBarrierTreeFor(func).populateBTPairs(parCons);
		}
	}
}
