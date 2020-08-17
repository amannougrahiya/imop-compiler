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
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

public class OmpCopyinClauseInfo extends OmpClauseInfo {

	public OmpCopyinClauseInfo(Node owner) {
		super(owner);
	}

	@Override
	public Set<String> getListedNames() {
		OmpCopyinClause clause = (OmpCopyinClause) this.getNode();
		return new HashSet<>(Misc.obtainVarNames(clause.getF2()));
	}

	@Override
	public VariableList getVariableList() {
		OmpCopyinClause clause = (OmpCopyinClause) this.getNode();
		return clause.getF2();
	}
}
