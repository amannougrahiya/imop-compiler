/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
/**
 * This package contains classes that are used to perform various
 * simplification/normalization passes on the input program (or on newly created
 * nodes).
 * <p>
 * These passes are used to ensure certain invariants on the structure
 * of the AST, which an analysis/transformation can rely upon, for the sake of
 * simplicity.
 * 
 * @author Aman Nougrahiya
 *
 */
package imop.lib.transform.simplify;
