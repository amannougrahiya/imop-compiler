/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.link.autoupdater;

public enum UpdateSideEffect {
	ADDED_DFD_PREDECESSOR, ADDED_DFD_SUCCESSOR, REMOVED_DFD_SUCCESSOR, REMOVED_DFD_PREDECESSOR, UNAUTHORIZED_DFD_UPDATE,
	NO_UPDATE_DUE_TO_NAME_COLLISION, ADDED_EXPLICIT_BARRIER, ADDED_NOWAIT_CLAUSE, ADDED_ENCLOSING_BLOCK,
	MISSING_CFG_PARENT, SYNTACTIC_CONSTRAINT, INDEX_INCREMENTED, INDEX_DECREMENTED, NAMESPACE_COLLISION_ON_REMOVAL,
	NAMESPACE_COLLISION_ON_ADDITION, ADDED_COPY, INIT_SIMPLIFIED, JUMPEDGE_CONSTRAINT, REMOVED_DEAD_CODE,
}
