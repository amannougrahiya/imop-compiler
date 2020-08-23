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
import imop.lib.analysis.flowanalysis.Definition;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.util.CellMap;
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

/**
 */
@Deprecated
public class Deprecated_FlowAndOutputDependenceMarker extends DepthFirstCFG {
	public Definition srcDef;
	public Set<Node> visitedNodes;

	public void addNodeForSymInMap(Node m, CellMap<Set<Node>> hashMap) {
		if (!hashMap.containsKey(srcDef.getCell())) {
			hashMap.put(srcDef.getCell(), new HashSet<>());
		}
		Set<Node> nodeSet = hashMap.get(srcDef.getCell());
		nodeSet.add(m);
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

		Cell defCell = srcDef.getCell();
		Node defNode = srcDef.getDefiningNode();

		if (Misc.isCFGLeafNode(n)) {
			/*
			 * Updates
			 */
			if (n.getInfo().getReads().contains(defCell)) {
				addNodeForSymInMap(n, defNode.getInfo().getFlowEdgeDestList());
				addNodeForSymInMap(defNode, n.getInfo().getFlowEdgeSrcList());
			}
			if (n.getInfo().getWrites().contains(defCell)) {
				addNodeForSymInMap(n, defNode.getInfo().getOutputEdgeDestList());
				addNodeForSymInMap(defNode, n.getInfo().getOutputEdgeSrcList());
			}

			/*
			 * Path Ends
			 */
			if (n.getInfo().getWrites().size() == 1 && n.getInfo().getWrites().contains(defCell)) {
				return;
			}

			/*
			 * Next Node
			 */
			if (n instanceof EndNode) {
				/*
				 * Scope
				 */
				if (defCell instanceof Symbol) {
					if (n.getParent() == ((Symbol) defCell).getDefiningScope()) {
						return;
					}
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
				if (part.getInfo().getReads().contains(defCell)) {
					addNodeForSymInMap(n, defNode.getInfo().getFlowEdgeDestList());
					addNodeForSymInMap(defNode, n.getInfo().getFlowEdgeSrcList());
				}
				if (part.getInfo().getWrites().contains(defCell)) {
					addNodeForSymInMap(n, defNode.getInfo().getOutputEdgeDestList());
					addNodeForSymInMap(defNode, n.getInfo().getOutputEdgeSrcList());
				}

				/*
				 * Path Ends
				 */
				if (part.getInfo().getWrites().size() == 1 && part.getInfo().getWrites().contains(defCell)) {
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
