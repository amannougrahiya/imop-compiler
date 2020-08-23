/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.parser.CParserConstants;

/**
 * Populates the <code>name</code> field with that of struct/union/enum's name,
 * and also populates the field <code>structDeclarationList</code>.
 */
public class StructUnionOrEnumInfoGetter extends DepthFirstProcess {
	public boolean isStruct = false;
	public boolean isUnion = false;
	public boolean isEnum = false;
	public boolean isUserDefinedDefinitionOrAssociatedTypedef = false;
	public String name = null;
	public String simpleDeclaration = new String();
	public StructDeclarationList structDeclarationList;
	public EnumeratorList enumeratorList;

	@Override
	public void visit(ADeclarationSpecifier n) {
		if (n.getF0().getChoice() instanceof TypeSpecifier) {
			TypeSpecifier typeSpec = (TypeSpecifier) n.getF0().getChoice();
			if (typeSpec.getF0().getChoice() instanceof StructOrUnionSpecifier
					|| typeSpec.getF0().getChoice() instanceof EnumSpecifier) {
				n.getF0().accept(this);
			} else {
				simpleDeclaration += n.getF0() + " ";
			}
		} else {
			simpleDeclaration += n.getF0() + " ";
		}
	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= ( <IDENTIFIER> )?
	 * f2 ::= "{"
	 * f3 ::= StructDeclarationList()
	 * f4 ::= "}"
	 */
	@Override
	public void visit(StructOrUnionSpecifierWithList n) {
		n.getF0().accept(this);
		name = n.getF1().getInfo().getString();
		structDeclarationList = n.getF3();
		isUserDefinedDefinitionOrAssociatedTypedef = true;
		simpleDeclaration += n.getF0() + " " + name + " ";
	}

	/**
	 * f0 ::= <STRUCT>
	 * | <UNION>
	 */
	@Override
	public void visit(StructOrUnion n) {
		int type = ((NodeToken) n.getF0().getChoice()).getKind();
		if (type == CParserConstants.STRUCT) {
			isStruct = true;
		} else {
			isUnion = true;
		}
	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(StructOrUnionSpecifierWithId n) {
		n.getF0().accept(this);
		name = n.getF1().getInfo().getString();
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= ( <IDENTIFIER> )?
	 * f2 ::= "{"
	 * f3 ::= EnumeratorList()
	 * f4 ::= "}"
	 */
	@Override
	public void visit(EnumSpecifierWithList n) {
		isEnum = true;
		name = n.getF1().getInfo().getString();
		enumeratorList = n.getF3();
		isUserDefinedDefinitionOrAssociatedTypedef = true;
		simpleDeclaration += n.getF0() + " " + name + " ";
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(EnumSpecifierWithId n) {
		isEnum = true;
		name = n.getF1().getInfo().getString();
	}
}
