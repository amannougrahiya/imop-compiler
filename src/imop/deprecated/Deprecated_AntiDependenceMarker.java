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
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.Usage;
import imop.lib.util.CellMap;
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

/**
 */
@Deprecated
public class Deprecated_AntiDependenceMarker extends DepthFirstCFG {
	public Usage srcUse;
	public Set<Node> visitedNodes;

	public void addNodeForSymInMap(Node n, CellMap<Set<Node>> map) {
		if (!map.containsKey(srcUse.cell)) {
			map.put(srcUse.cell, new HashSet<>());
		}
		Set<Node> nodeSet = map.get(srcUse.cell);
		nodeSet.add(n);
	}

	@Override
	public void initProcess(Node n) {
		/*
		 * Sanity Check
		 */
		if (!Misc.isCFGNode(n)) {
			return;
		}

		/*
		 * Termination Check.
		 */
		if (visitedNodes.contains(n)) {
			return;
		} else {
			visitedNodes.add(n);
		}

		Cell useSym = srcUse.cell;
		Node useNode = srcUse.usageNode;

		if (Misc.isCFGLeafNode(n)) {
			/*
			 * Updates
			 */
			if (n.getInfo().getWrites().contains(useSym)) {
				addNodeForSymInMap(n, useNode.getInfo().getAntiEdgeDestList());
				addNodeForSymInMap(useNode, n.getInfo().getAntiEdgeSrcList());
			}

			/*
			 * Path Ends
			 */
			if (n.getInfo().getWrites().size() == 1 && n.getInfo().getWrites().contains(useSym)) {
				return;
			}

			/*
			 * Next Node
			 */
			if (n instanceof EndNode) {
				/*
				 * Scope
				 */
				if (useSym instanceof Symbol) {
					if (n.getParent() == ((Symbol) useSym).getDefiningScope()) {
						return;
					}
				} else {
					return; // TODO: Placeholder code here, while changing Symbol to Cell.
				}

				/*
				 * Next
				 */
				for (Node succ : n.getParent().getInfo().getCFGInfo().getSuccBlocks()) {
					succ.accept(this);
				}
			} else {
				/*
				 * Next
				 */
				for (Node succ : n.getInfo().getCFGInfo().getSuccBlocks()) {
					succ.accept(this);
				}
			}
		} else {
			if (Misc.isElementaryRWNodeParent(n)) {
				/*
				 * Updates
				 */
				Node part = Misc.getElementaryRWNode(n);
				if (part.getInfo().getWrites().contains(useSym)) {
					addNodeForSymInMap(n, useNode.getInfo().getAntiEdgeDestList());
					addNodeForSymInMap(useNode, n.getInfo().getAntiEdgeSrcList());
				}

				/*
				 * Path Ends
				 */
				if (part.getInfo().getWrites().size() == 1 && part.getInfo().getWrites().contains(useSym)) {
					return;
				}
			}
			/*
			 * Next Node
			 */
			n.getInfo().getCFGInfo().getNestedCFG().getBegin().accept(this);
		}
	}

}
