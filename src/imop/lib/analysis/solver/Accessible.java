/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.solver;

import imop.lib.analysis.flowanalysis.Cell;

/**
 * This is an interface extended by {@link Cell} and
 * {@link AccessExpression} classes, denoting the various models in which we
 * abstract memory statically.
 * <br>
 * While a {@code Cell} object represents field-insensitive abstraction of
 * memory, an {@code AccessExpression} is used to model multiple locations
 * that {@code e1[e2]} may be an lvalue of, by expressing e1 and e2 as
 * uninterpreted variables (with parametric ranges), wherever possible.
 * 
 * @author Aman Nougrahiya
 *
 */
public interface Accessible {
}
