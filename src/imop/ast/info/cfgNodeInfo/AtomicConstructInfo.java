/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.OmpConstructInfo;
import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.cfg.info.AtomicConstructCFGInfo;
import imop.lib.util.CellList;

public class AtomicConstructInfo extends OmpConstructInfo {
	/**
	 * @author aman
	 *
	 */
	public enum AtomicClauseType {
		READ, WRITE, UPDATE, CAPTURE
	}

	public AtomicConstructInfo(Node owner) {
		super(owner);
	}

	@Override
	public AtomicConstructCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new AtomicConstructCFGInfo(getNode());
		}
		return (AtomicConstructCFGInfo) cfgInfo;
	}

	/**
	 * This method returns the symbol which will be read/written
	 * atomically by the virtue of this owner atomic construct.
	 * 
	 * @return
	 */
	public Symbol getTargetSymbol() {
		AtomicConstruct atom = (AtomicConstruct) this.getNode();
		switch (this.getAtomicClauseType()) {
		case READ:
			CellList reads = atom.getF4().getInfo().getReads();
			return (Symbol) reads.get(0);
		case WRITE:
		case UPDATE:
			CellList writes = atom.getF4().getInfo().getWrites();
			return (Symbol) writes.get(0);
		case CAPTURE:
			CellList capturedWrites = atom.getF4().getInfo().getWrites();
			return (Symbol) capturedWrites.get(0);
		}
		return null;
	}

	/**
	 * @return
	 */
	public AtomicClauseType getAtomicClauseType() {
		AtomicConstruct atom = (AtomicConstruct) getNode();
		if (atom.getF2().present()) {
			String atomClauseName = ((NodeToken) ((AtomicClause) atom.getF2().getNode()).getF0().getChoice())
					.getTokenImage();
			if (atomClauseName.equals("read")) {
				return AtomicClauseType.READ;
			} else if (atomClauseName.equals("write")) {
				return AtomicClauseType.WRITE;
			} else if (atomClauseName.equals("update")) {
				return AtomicClauseType.UPDATE;
			} else if (atomClauseName.equals("capture")) {
				return AtomicClauseType.CAPTURE;
			}
		} else {
			return AtomicClauseType.UPDATE;
		}
		return AtomicClauseType.UPDATE;
	}
}
