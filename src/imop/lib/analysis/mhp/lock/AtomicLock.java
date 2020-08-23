/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp.lock;

import imop.ast.info.cfgNodeInfo.AtomicConstructInfo.AtomicClauseType;
import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Symbol;

public class AtomicLock extends AbstractLock {
	private final Symbol targetSymbol;
	private final AtomicClauseType atomicClauseType;

	private AtomicLock(Symbol targetSymbol, AtomicClauseType atomicClauseType) {
		this.targetSymbol = targetSymbol;
		this.atomicClauseType = atomicClauseType;
	}

	public static AtomicLock getAtomicLock(Symbol targetSymbol, AtomicClauseType atomicClauseType) {
		AtomicLock retLock = new AtomicLock(targetSymbol, atomicClauseType);
		if (!AbstractLock.allLocks.contains(retLock)) {
			AbstractLock.allLocks.add(retLock);
		}
		return retLock;
	}

	public static AtomicLock getAtomicLock(AtomicConstruct atomicConstruct) {
		return getAtomicLock(atomicConstruct.getInfo().getTargetSymbol(),
				atomicConstruct.getInfo().getAtomicClauseType());
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof AtomicLock)) {
			return false;
		}
		AtomicLock that = (AtomicLock) other;
		if (that.targetSymbol == this.targetSymbol && that.atomicClauseType == this.atomicClauseType) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.targetSymbol == null) ? 0 : this.targetSymbol.hashCode());
		result = prime * result + ((this.atomicClauseType == null) ? 0 : this.atomicClauseType.hashCode());
		return result;
	}

}
