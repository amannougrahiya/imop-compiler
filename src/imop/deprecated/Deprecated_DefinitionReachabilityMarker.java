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
import imop.ast.node.internal.*;
import imop.baseVisitor.cfgTraversals.DepthFirstCFG;
import imop.lib.analysis.flowanalysis.Definition;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.util.CellList;
import imop.lib.util.Misc;

import java.util.List;
import java.util.Set;

/**
 * Given a definition markerDef, this class marks all the nodes where this
 * definition can be reachable.
 */
@Deprecated
public class Deprecated_DefinitionReachabilityMarker extends DepthFirstCFG {
	public Definition markerDef;

	@Override
	public void initProcess(Node n) {
		// Process only CFG nodes.
		if (!Misc.isCFGNode(n)) {
			return;
		}

		// If this node has already been marked with markerDef, then return
		// else mark this node with markerDef
		Set<Definition> markedDefSet = n.getInfo().deprecated_getReachingDefinitions();
		if (markedDefSet.contains(markerDef)) {
			return;
		} else {
			markedDefSet.add(markerDef);
		}

		if (Misc.isCFGLeafNode(n)) {
			// If n is the end of definitions's scope, then stop
			if (n instanceof EndNode) {
				if (markerDef.getCell() instanceof Symbol) {
					if (n.getParent() == ((Symbol) markerDef.getCell()).getDefiningScope()) {
						return;
					}
				}
			}

			if (n != markerDef.getDefiningNode()) {
				// If n has a singleton set containing the symbol being marked, then return;
				CellList writes = n.getInfo().getWrites();
				if (writes.size() == 1 && writes.contains(markerDef.getCell())) {
					return;
				}
			}

			// Visit the successors
			List<Node> successors = n.getInfo().getCFGInfo().getSuccessors();
			for (Node succ : successors) {
				succ.accept(this);
			}
		} else { // if n is a non-leaf CFG node
			CellList nonNestedWrites = null;
			if (n instanceof ParallelForConstruct) {
				nonNestedWrites = ((ParallelForConstruct) n).getF3().getInfo().getWrites();
			} else if (n instanceof ParallelSectionsConstruct) {
				nonNestedWrites = ((ParallelSectionsConstruct) n).getF3().getInfo().getWrites();
			} else if (n instanceof ParallelConstruct) {
				nonNestedWrites = ((ParallelConstruct) n).getParConsF1().getInfo().getWrites();
			} else if (n instanceof TaskConstruct) {
				nonNestedWrites = ((TaskConstruct) n).getF2().getInfo().getWrites();
			}

			if (n != markerDef.getDefiningNode()) {
				// If non-nested writes is a singleton set with the symbol being marked, then
				// return
				if (nonNestedWrites != null && nonNestedWrites.size() == 1
						&& nonNestedWrites.contains(markerDef.getCell())) {
					return;
				}
			}

			// Visit the BeginNode
			n.getInfo().getCFGInfo().getNestedCFG().getBegin().accept(this);
		}
		return;
	}

}
