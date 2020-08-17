/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info;

import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.*;
import imop.lib.analysis.typeSystem.PointerType;
import imop.lib.util.CellSet;

public class PTAStabilizationHeuristic {
	public static CellSet getAffectedCells(Node node) {
		if (!node.getInfo().isControlConfined()) {
			return null;
		}
		CellSet pointerSet = new CellSet();
		for (Cell c : node.getInfo().getAccesses()) {
			if (c instanceof HeapCell) {
				pointerSet.add(c);
			} else if (c instanceof FreeVariable) {
				// Ignore for now.
				return null;
			} else if (c instanceof Symbol) {
				if (((Symbol) c).getType() instanceof PointerType) {
					pointerSet.add(c);
				}
			} else if (c instanceof AddressCell) {
				pointerSet.addAll(c.getPointsTo(node));
			} else if (c instanceof FieldCell) {
				pointerSet.add(c);
			}
		}
		CellSet pointsToClosure = new CellSet();
		int oldSize = 0, newSize;
		while (true) {
			if (pointerSet.isUniversal()) {
				return null;
			} else {
				for (Cell sym : pointerSet) {
					if (sym != null) {
						pointsToClosure.addAll(sym.getPointsTo(node));
					}
				}
			}
			newSize = pointsToClosure.size();
			if (newSize > oldSize) {
				oldSize = newSize;
				pointerSet = new CellSet(pointsToClosure);
				continue;
			} else {
				break;
			}
		}
		pointsToClosure.addAll(pointerSet);
		return pointsToClosure;
	}

}
