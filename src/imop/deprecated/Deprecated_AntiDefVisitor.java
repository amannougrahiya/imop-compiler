/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.deprecated;

import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.analysis.flowanalysis.Definition;
import imop.lib.analysis.flowanalysis.Usage;
import imop.lib.util.CellList;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * This class visits the CFG starting from all the uses, one-by-one and creates
 * antiEdges.
 */
@Deprecated
public class Deprecated_AntiDefVisitor extends DepthFirstProcess {
	public List<Definition> definitionList = new ArrayList<>();
	public Deprecated_AntiDependenceMarker antiMarker = new Deprecated_AntiDependenceMarker();

	@Override
	public void initProcess(Node n) {
		if (!Misc.isCFGNode(n)) {
			return;
		}

		CellList reads = null;
		if (Misc.isCFGLeafNode(n)) {
			reads = n.getInfo().getReads();

		} else {
			if (Misc.isElementaryRWNodeParent(n)) {
				Node part = Misc.getElementaryRWNode(n);
				reads = part.getInfo().getReads();
			}
		}
		if (reads != null && !reads.isEmpty()) {
			reads.applyAllExpanded(sym -> {
				antiMarker.srcUse = new Usage(n, sym);
				antiMarker.visitedNodes = new HashSet<>();

				if (Misc.isCFGLeafNode(n)) {
					for (Node startNode : n.getInfo().getCFGInfo().getSuccBlocks()) {
						startNode.accept(antiMarker);
					}
				} else { // def.definingNode is either of Parallel{For, Sections}Construct or
							// TaskConstruct
					n.getInfo().getCFGInfo().getNestedCFG().getBegin().accept(antiMarker);
				}
			});
		}
	} // end of init-process
}
