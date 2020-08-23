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
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Definition;
import imop.lib.util.CellList;
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

/**
 * This class populates definitionList with all the definitions (of
 * stack-elements) present in the given node.
 */
public class AllDefinitionGetter extends DepthFirstProcess {
	public Set<Definition> definitionList = new HashSet<>();

	/**
	 * This overridden version puts n in the definitionList,
	 * if n is a leaf CFG node with non-empty list of writes.
	 */
	@Override
	public void initProcess(Node n) {
		if (Misc.isCFGLeafNode(n)) {
			CellList writes = n.getInfo().getWrites();
			if (!writes.isEmpty()) {
				if (writes.isUniversal()) {
					definitionList.add(new Definition(n, Cell.genericCell));
				} else {
					writes.applyAllExpanded(sym -> {
						definitionList.add(new Definition(n, sym));
					});
				}
			}
		}
	}
}
