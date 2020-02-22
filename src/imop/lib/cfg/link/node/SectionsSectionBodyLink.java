/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.link.node;

import imop.ast.node.external.Node;
import imop.ast.node.external.SectionsConstruct;
import imop.ast.node.external.Statement;
import imop.lib.cfg.link.baseVisitor.CFGLinkVisitor;
import imop.lib.cfg.link.baseVisitor.GJCFGLinkVisitor;
import imop.lib.cfg.link.baseVisitor.GJNoArguCFGLinkVisitor;
import imop.lib.cfg.link.baseVisitor.GJVoidCFGLinkVisitor;

public class SectionsSectionBodyLink extends CFGLink {
	public SectionsConstruct enclosingNonLeafNode;
	public Statement childNode;
	public int index;

	public SectionsSectionBodyLink(SectionsConstruct enclosingNonLeafNode, Statement childNode, int index) {
		this.enclosingNonLeafNode = enclosingNonLeafNode;
		this.childNode = childNode;
		this.index = index;
	}

	@Override
	public void accept(CFGLinkVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public <R, A> R accept(GJCFGLinkVisitor<R, A> visitor, A argu) {
		return visitor.visit(this, argu);
	}

	@Override
	public <R> R accept(GJNoArguCFGLinkVisitor<R> visitor) {
		return visitor.visit(this);
	}

	@Override
	public <A> void accept(GJVoidCFGLinkVisitor<A> visitor, A argu) {
		visitor.visit(this, argu);
	}

	@Override
	public Node getEnclosingNode() {
		return this.enclosingNonLeafNode;
	}
}
