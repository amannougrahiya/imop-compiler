/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.analysis.typeSystem.Typedef;
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

/**
 * Collects all usages of Typedef names in the visited AST.
 */
public class UsedTypedefGetter extends DepthFirstProcess {
	public Set<Typedef> usedTypedefs = new HashSet<>();

	/**
	 * f0 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(TypedefName n) {
		Typedef tDef = Misc.getTypedefEntry(n.getF0().getTokenImage(), n);
		if (tDef != null) {
			usedTypedefs.add(tDef);
		}
	}
}
