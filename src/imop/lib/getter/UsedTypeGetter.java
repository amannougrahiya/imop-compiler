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
import imop.lib.analysis.typeSystem.Type;
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order. Your visitors may extend this class.
 */
public class UsedTypeGetter extends DepthFirstProcess {
	public Set<Type> usedTypes = new HashSet<>();

	/**
	 * f0 ::= "("
	 * f1 ::= TypeName()
	 * f2 ::= ")"
	 * f3 ::= CastExpression()
	 */
	@Override
	public void visit(CastExpressionTyped n) {
		usedTypes.addAll(Type.getTypeTree(n.getF1(), Misc.getEnclosingBlock(n)).getAllTypes());
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= "("
	 * f2 ::= TypeName()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(SizeofTypeName n) {
		usedTypes.addAll(Type.getTypeTree(n.getF2(), Misc.getEnclosingBlock(n)).getAllTypes());
	}
}
