/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

import java.util.HashSet;
import java.util.Set;

public class OmpClauseInfo extends NodeInfo {

	public OmpClauseInfo(Node owner) {
		super(owner);
	}

	public static OmpClause getCopy(OmpClause ompClause) {
		OmpClause newClause;
		newClause = FrontEnd.parseAndNormalize(ompClause.toString(), ompClause.getClass());
		return newClause;
		// if (ompClause instanceof AtomicClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// AtomicClause.class);
		// } else if (ompClause instanceof FinalClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// FinalClause.class);
		// } else if (ompClause instanceof IfClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(), IfClause.class);
		// } else if (ompClause instanceof MergeableClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// MergeableClause.class);
		// } else if (ompClause instanceof NowaitClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// NowaitClause.class);
		// } else if (ompClause instanceof NumThreadsClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// NumThreadsClause.class);
		// } else if (ompClause instanceof OmpCopyinClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// OmpCopyinClause.class);
		// } else if (ompClause instanceof OmpDfltSharedClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// OmpDfltSharedClause.class);
		// } else if (ompClause instanceof OmpDfltNoneClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// OmpDfltNoneClause.class);
		// } else if (ompClause instanceof OmpFirstPrivateClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// OmpFirstPrivateClause.class);
		// } else if (ompClause instanceof OmpLastPrivateClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// OmpLastPrivateClause.class);
		// } else if (ompClause instanceof OmpPrivateClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// OmpPrivateClause.class);
		// } else if (ompClause instanceof OmpReductionClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// OmpReductionClause.class);
		// } else if (ompClause instanceof OmpSharedClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// OmpSharedClause.class);
		// } else if (ompClause instanceof UniqueForClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// UniqueForClause.class);
		// } else if (ompClause instanceof UntiedClause) {
		// newClause = FrontEnd.parseAndNormalize(ompClause.toString(),
		// UntiedClause.class);
		// } else {
		// assert (false);
		// newClause = null;
		// }
		// return newClause;
	}

	public Set<String> getListedNames() {
		return new HashSet<>();
	}

	public VariableList getVariableList() {
		return null;
	}

	public CellSet getListedSymbols() {
		CellSet privateCells = new CellSet();
		OmpConstruct enclosingOmp = Misc.getEnclosingNode(this.getNode(), OmpConstruct.class);
		enclosingOmp = (OmpConstruct) Misc.getCFGNodeFor(enclosingOmp);
		for (String st : this.getListedNames()) {
			privateCells.add(Misc.getSymbolEntry(st, enclosingOmp));
		}
		return privateCells;
	}

	/**
	 * Removes the list item with name {@code id}, from the list, if any.
	 * Returns true, if the the clause needs to be removed instead.
	 * 
	 * @param id
	 * @return
	 *         true, if the clause needs to be removed instead.
	 */
	public boolean removeListItem(String id) {
		if (!this.getListedNames().contains(id)) {
			return false;
		}
		VariableList vL = this.getVariableList();
		if (vL == null) {
			return false;
		}

		// TODO: Check the side-effects, if any, of this removal.
		if (id.equals(vL.getF0().getTokenImage())) {
			if (vL.getF1().getNodes().isEmpty()) {
				return true;
			} else {
				NodeSequence nodeSeq = (NodeSequence) vL.getF1().getNodes().get(0);
				vL.getF1().removeNode(0);
				NodeToken nextId = ((NodeToken) nodeSeq.getNodes().get(1));
				nodeSeq.removeNode(1);
				vL.setF0(nextId);
			}
		} else {
			Node nodeToBeRemoved = null;
			for (Node nodeSeqNode : vL.getF1().getNodes()) {
				NodeSequence nodeSeq = (NodeSequence) nodeSeqNode;
				NodeToken nextId = ((NodeToken) nodeSeq.getNodes().get(1));
				if (id.equals(nextId.getTokenImage())) {
					nodeToBeRemoved = nodeSeqNode;
				}
			}
			assert (nodeToBeRemoved != null);
			vL.getF1().removeNode(nodeToBeRemoved);
		}
		return false;
	}

}
