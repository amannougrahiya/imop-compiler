/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.simplify;

import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.builder.Builder;
import imop.parser.FrontEnd;

/**
 * Adds a tag to all the structs, unions and enums which are untagged.
 */
public class StructUnionEnumTagger extends DepthFirstProcess {
	/**
	 * f0 ::= ( StructOrUnionSpecifierWithList() |
	 * StructOrUnionSpecifierWithId()
	 * )
	 */
	@Override
	public void visit(StructOrUnionSpecifier n) {
		if (n.getF0().getChoice() instanceof StructOrUnionSpecifierWithList) {
			StructOrUnionSpecifierWithList node = (StructOrUnionSpecifierWithList) n.getF0().getChoice();
			if (!node.getF1().present()) {
				StringBuilder newNodeString = new StringBuilder(node.getF0().getInfo().getString());
				newNodeString.append(" " + Builder.getNewTempName("stUn"));
				newNodeString.append(" { " + node.getF3().getInfo().getString() + "} ");
				n.getF0()
						.setChoice(FrontEnd.parseAlone(newNodeString.toString(), StructOrUnionSpecifierWithList.class));
			}
		}
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= EnumSpecifierWithList()
	 * | EnumSpecifierWithId()
	 */
	@Override
	public void visit(EnumSpecifier n) {
		if (n.getF0().getChoice() instanceof EnumSpecifierWithList) {
			EnumSpecifierWithList node = (EnumSpecifierWithList) n.getF0().getChoice();
			if (!node.getF1().present()) {
				StringBuilder newNodeString = new StringBuilder("enum");
				newNodeString.append(" " + Builder.getNewTempName("enum"));
				newNodeString.append(" { " + node.getF3().getInfo().getString() + "} ");
				n.getF0().setChoice(FrontEnd.parseAlone(newNodeString.toString(), EnumSpecifierWithList.class));
			}
		}
		n.getF0().accept(this);
	}
}
