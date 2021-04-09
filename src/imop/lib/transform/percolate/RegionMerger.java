/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.percolate;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.util.Misc;

import java.util.List;

@Deprecated
public class RegionMerger {
	/**
	 * Merges reasons that exist within the provided {@code node}.
	 * 
	 * @param node
	 *             node within which reasons need to be merged.
	 */
	public static void mergeRegionsWithin(TranslationUnit node) {
		for (ParallelConstruct parCons : Misc.getExactEnclosee(node, ParallelConstruct.class)) {
			Scopeable enclosingScope = Misc.getEnclosingBlock(parCons);
			if (enclosingScope == null || enclosingScope instanceof FunctionDefinition) {
				continue;
			}
			CompoundStatement enclosingCompStmt = (CompoundStatement) enclosingScope;
			if (RegionMerger.bringUpParCons(enclosingCompStmt, parCons)) {
				mergeRegionsWithin(node);
				return;
			}
		}
	}

	public static boolean bringUpParCons(CompoundStatement scope, ParallelConstruct parCons) {
		assert (scope != null);
		if (!scope.getInfo().isControlConfined()) {
			Misc.warnDueToLackOfFeature("Cannot merge regions within non-control-confined scope of type "
					+ scope.getClass().getSimpleName() + ".", scope);
			return false;
		}

		boolean parCalls = false;
		outer: for (CallStatement cs : scope.getInfo().getLexicallyEnclosedCallStatements()) {
			for (Node csContent : cs.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				if (csContent instanceof BeginNode) {
					BeginNode beginNode = (BeginNode) csContent;
					if (beginNode.getParent() instanceof ParallelConstruct) {
						parCalls = true;
						break outer;
					}
				}
			}
		}

		if (parCalls == true) {
			Misc.warnDueToLackOfFeature(
					"Cannot merge regions within a node since the regions are not lexically enclosed within the node.",
					null);
		}

		List<Node> elements = scope.getInfo().getCFGInfo().getElementList();
		for (Node elem : elements) {
			if (elem instanceof ParallelConstruct) {
			}
		}

		return false;
	}

}
