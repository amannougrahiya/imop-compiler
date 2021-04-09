/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

public enum PrintStyle {
	LEAF, // Prints information about the leaf nodes (except BeginNode and EndNode).
	LEAF_AND_DUMMY, // Prints information about the leaf nodes, BeginNodes and EndNodes.
	NO_DUMMY, // Prints information about the leaf and non-leaf nodes.
	ALL // Prints information about all the CFG nodes.
}
