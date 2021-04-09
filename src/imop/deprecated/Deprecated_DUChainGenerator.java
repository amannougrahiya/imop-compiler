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
import imop.lib.util.CellList;
import imop.lib.util.CellMap;
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

/**
 * This class traverses all the elementary read/write nodes,
 * and populates their defsInUD and usesInDU fields.
 */
@Deprecated
public class Deprecated_DUChainGenerator extends DepthFirstProcess {
	@Override
	public void initProcess(Node n) {
		// Skip non-CFG nodes
		if (!Misc.isCFGNode(n)) {
			return;
		}

		CellList readSymbols = null; // A vector of symbols being read

		if (Misc.isCFGLeafNode(n)) {
			readSymbols = n.getInfo().getReads();
		} else {
			if (n instanceof ParallelForConstruct) {
				readSymbols = ((ParallelForConstruct) n).getF3().getInfo().getReads();
			} else if (n instanceof ParallelSectionsConstruct) {
				readSymbols = ((ParallelSectionsConstruct) n).getF3().getInfo().getReads();
			} else if (n instanceof ParallelConstruct) {
				readSymbols = ((ParallelConstruct) n).getParConsF1().getInfo().getReads();
			} else if (n instanceof TaskConstruct) {
				readSymbols = ((TaskConstruct) n).getF2().getInfo().getReads();
			}
		}

		// Return if this is not an elementary read/write node or if it reads nothing
		if (readSymbols == null || readSymbols.isEmpty()) {
			return;
		}

		// Create DU and UD chains using all the definitions reachable at this node
		// It may seem to happen that no definition reaches this use.
		// This is not true, the use, in fact, gets the definition from arguments of the
		// caller.
		if (n.getInfo().deprecated_readReachingDefinitions() == null) {
			return;
		}
		for (Definition def : n.getInfo().deprecated_readReachingDefinitions()) {
			if (readSymbols.contains(def.getCell())) {
				// System.out.println("Adding " + def.symbol.name + " for def at " +
				// Misc.getLineNum(def.definingNode) + " with use at " + Misc.getLineNum(n));
				// Populate the defsInUDSet<E> HashMap<Symbol, Set<Node>> defsInDU =
				// n.getInfo().getDefsInDU();
				CellMap<Set<Node>> defsInUD = n.getInfo().getDefsInUD();
				if (!defsInUD.containsKey(def.getCell())) {
					defsInUD.put(def.getCell(), new HashSet<>());
				}
				Set<Node> defNodeSet = defsInUD.get(def.getCell());
				defNodeSet.add(def.getDefiningNode());

				// Populate the usesInDU of def.definingNode
				CellMap<Set<Node>> usesInDU = def.getDefiningNode().getInfo().getUsesInDU();
				if (!usesInDU.containsKey(def.getCell())) {
					usesInDU.put(def.getCell(), new HashSet<>());
				}
				Set<Node> useNodeSet = usesInDU.get(def.getCell());
				useNodeSet.add(n);
			}
		}
	}
}
