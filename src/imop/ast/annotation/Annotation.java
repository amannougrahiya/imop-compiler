/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.annotation;

/**
 * A collection of all the various types of annotations, that are attached
 * semantically to an AST node. Currently, there are following types of
 * annotations:
 * <ol>
 * <li>Comments -- {@code SingleLineComment} and {@code MultiLineComment}</li>
 * <li>{@code Label} -- applies to CFG Statement's.</li>
 * <li>Const-qualification -- applies to Declaration's.</li>
 * <li>IMOP pragma annotations -- applies to CFG statements.</li>
 * </ol>
 * 
 * @author aman
 *
 */
public abstract class Annotation {
}
