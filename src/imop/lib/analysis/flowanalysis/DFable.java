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

import java.util.Set;

/**
 * Common interface for SCC components, such as {@link Node} and {@link SCC}.
 * 
 * @author Aman Nougrahiya
 *
 */
public interface DFable {
	public Set<DFable> getDataFlowPredecessors();

	public Set<DFable> getDataFlowSuccessors();

	public void setReversePostOrder(int i);

	public int getReversePostOrder();
}
