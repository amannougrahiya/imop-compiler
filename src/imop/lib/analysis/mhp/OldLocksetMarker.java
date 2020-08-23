/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp;

import imop.ast.node.external.*;
import imop.baseVisitor.GJVoidDepthFirstProcess;
import imop.lib.util.Misc;

import java.util.List;

/**
 * This visitor visits and marks all the CFG nodes
 * with their enclosing lock regions.
 * 
 * @deprecated
 *             this visitor marks nodes with locks of old type. Instead, use the
 *             IDFA based lock-set analysis.
 */
@Deprecated
public class OldLocksetMarker extends GJVoidDepthFirstProcess<List<OldLock>> {
	/**
	 * This overridden method adds all the enclosing lock regions
	 * to the set $lockSet$ of the traversed CFG node $n$.
	 */
	@Override
	public void initProcess(Node n, List<OldLock> argu) {
		if (Misc.isCFGNode(n)) {
			List<OldLock> lockSet = n.getInfo().getLockSet();
			lockSet.addAll(argu);
		}
		return;
	}

	/**
	 * To be conservative, we will not process the called functions,
	 * i.e., we won't mark the called statements with the enclosing lock region.
	 * 
	 * @param n
	 * @param argu
	 */
	public void processCalls(Node n, List<OldLock> argu) {
		// No body.
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(CriticalConstruct n, List<OldLock> argu) {
		initProcess(n, argu);
		OldLock newLock = null;
		if (n.getF2().present()) {
			newLock = OldLock.getLock(((RegionPhrase) n.getF2().getNode()).getF1().getTokenImage(), n);
		} else {
			newLock = OldLock.getLock("__imop_critical", n);
		}
		argu.add(newLock);
		n.getF4().accept(this, argu);
		argu.remove(argu.size() - 1);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(AtomicConstruct n, List<OldLock> argu) {
		initProcess(n, argu);
		OldLock newLock = null;
		newLock = OldLock.getLock("__imop_atomic", n);
		argu.add(newLock);
		n.getF4().accept(this, argu);
		argu.remove(argu.size() - 1);
	}

}
