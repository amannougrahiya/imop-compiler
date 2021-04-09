/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis;

import imop.ast.node.external.*;
import imop.lib.util.CellSet;
import imop.lib.util.ImmutableCellSet;

/**
 * Represents a free-variable.
 * 
 * @author Aman Nougrahiya
 *
 */
public class FreeVariable extends Cell {

	private final NodeToken nodeToken;
	public int cachedHashCode = -1;

	public FreeVariable(String freeVariableName) {
		this.nodeToken = new NodeToken(freeVariableName);
		// if (!dontSave) {
		// allCells.add(this);
		// }
	}

	public FreeVariable(NodeToken nodeToken) {
		this.nodeToken = nodeToken;
		// if (!dontSave) {
		// allCells.add(this);
		// }
	}

	public String getFreeVariableName() {
		return this.nodeToken.getTokenImage();
	}

	public NodeToken getNodeToken() {
		return nodeToken;
	}

	@Override
	public CellSet deprecated_getPointsTo(Node node) {
		CellSet pointsToSet = new CellSet();
		// TODO: Discuss this.
		// pointsToSet.add(Cell.getGenericCell());
		return pointsToSet;
	}

	@Override
	public ImmutableCellSet getPointsTo(Node node) {
		return new ImmutableCellSet(this.deprecated_getPointsTo(node));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FreeVariable)) {
			return false;
		}
		FreeVariable that = (FreeVariable) obj;
		if (!this.nodeToken.getTokenImage().equals(that.nodeToken.getTokenImage())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		if (cachedHashCode == -1) {
			if (this.nodeToken == null) {
				return 0;
			}
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((this.nodeToken.getTokenImage() == null) ? 0 : this.nodeToken.getTokenImage().hashCode());
			cachedHashCode = result;
		}
		return cachedHashCode;
	}

	@Override
	public String toString() {
		return "free_" + nodeToken.getTokenImage();
	}

}
