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

public class Usage {
	public Node usageNode;
	public Cell cell;

	public Usage(Node node, Cell sym) {
		this.usageNode = node;
		this.cell = sym;
	}
}
