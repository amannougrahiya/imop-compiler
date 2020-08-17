/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.node.internal;

import imop.lib.analysis.typeSystem.Type;

import java.util.HashMap;

/**
 * Nodes (or Types) that implement this interface, may start a new scope
 * for symbols (and types).
 * 
 * @author aman
 *
 */
public interface Scopeable {
	HashMap<String, Type> getTypeTable();
}
