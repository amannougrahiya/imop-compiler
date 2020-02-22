/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info;

import java.util.List;

import imop.ast.node.external.Node;
import imop.ast.node.external.NowaitClause;
import imop.ast.node.internal.OmpClause;
import imop.lib.getter.OmpClauseGetter;

public class OmpConstructInfo extends StatementInfo {
	public OmpConstructInfo(Node owner) {
		super(owner);
	}

	public List<OmpClause> getOmpClauseList() {
		OmpClauseGetter clauseGetter = new OmpClauseGetter();
		getNode().accept(clauseGetter);
		return clauseGetter.ompClauseList;
	}

	public boolean hasNowaitClause() {
		for (OmpClause clause : this.getOmpClauseList()) {
			if (clause instanceof NowaitClause) {
				return true;
			}
		}
		return false;
	}

}
