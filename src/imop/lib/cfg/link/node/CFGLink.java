/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.link.node;

import imop.ast.node.external.*;
import imop.lib.cfg.link.baseVisitor.CFGLinkVisitor;
import imop.lib.cfg.link.baseVisitor.GJCFGLinkVisitor;
import imop.lib.cfg.link.baseVisitor.GJNoArguCFGLinkVisitor;
import imop.lib.cfg.link.baseVisitor.GJVoidCFGLinkVisitor;

public abstract class CFGLink {
	public abstract Node getEnclosingNode();

	public abstract void accept(CFGLinkVisitor visitor);

	public abstract <R, A> R accept(GJCFGLinkVisitor<R, A> visitor, A argu);

	public abstract <R> R accept(GJNoArguCFGLinkVisitor<R> visitor);

	public abstract <A> void accept(GJVoidCFGLinkVisitor<A> visitor, A argu);
}
