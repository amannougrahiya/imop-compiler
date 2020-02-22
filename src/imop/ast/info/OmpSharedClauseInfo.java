/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info;

import java.util.HashSet;
import java.util.Set;

import imop.ast.node.external.Node;
import imop.ast.node.external.OmpSharedClause;
import imop.ast.node.external.VariableList;
import imop.lib.util.Misc;

public class OmpSharedClauseInfo extends OmpClauseInfo {

	public OmpSharedClauseInfo(Node owner) {
		super(owner);
	}

	@Override
	public Set<String> getListedNames() {
		OmpSharedClause clause = (OmpSharedClause) this.getNode();
		return new HashSet<>(Misc.obtainVarNames(clause.getF2()));
	}

	@Override
	public VariableList getVariableList() {
		OmpSharedClause clause = (OmpSharedClause) this.getNode();
		return clause.getF2();
	}

}
