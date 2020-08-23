/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.builder;

import imop.ast.info.cfgNodeInfo.DeclarationInfo;
import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstProcess;

import java.util.List;

/**
 * This class sets the declCodeString of the declaration for a new variable
 * in String, as per the declaration of the given parameter declaration.
 * IMPORTANT: This should be called only over a Declaration.
 * Caller should populate varName with name of the new variable.
 * Caller should also put in the name of the symbol to be
 * used for type information in baseName.
 */
public class DeclarationStringFromDeclarationGetter extends DepthFirstProcess {
	public String declCodeString;
	public String varName;
	public String baseName;

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(Declaration n) {
		declCodeString = n.getF0().getInfo().getString();
		n.getF1().accept(this);
		declCodeString += ";";
	}

	/**
	 * f0 ::= InitDeclarator()
	 * f1 ::= ( "," InitDeclarator() )*
	 */
	@Override
	public void visit(InitDeclaratorList n) {
		Declarator decl = n.getF0().getF0();
		if (DeclarationInfo.getRootIdName(decl).equals(baseName)) {
			decl.accept(this);
			return;
		} else {
			for (Node seq : n.getF1().getNodes()) {
				decl = ((InitDeclarator) ((NodeSequence) seq).getNodes().get(1)).getF0();
				if (DeclarationInfo.getRootIdName(decl).equals(baseName)) {
					decl.accept(this);
					return;
				}
			}
		}
	}

	/**
	 * f0 ::= ( Pointer() )?
	 * f1 ::= DirectDeclarator()
	 */
	@Override
	public void visit(Declarator n) {
		declCodeString += n.getF0().getInfo().getString();
		// Add one more *, if DeclaratorOpList's first element is an array

		List<Node> opList = n.getF1().getF1().getF0().getNodes();
		if (opList.size() > 0) {
			if (((ADeclaratorOp) opList.get(0)).getF0().getChoice() instanceof DimensionSize) {
				declCodeString += "* ";
			}
		}

		n.getF1().accept(this);
	}

	/**
	 * f0 ::= IdentifierOrDeclarator()
	 * f1 ::= DeclaratorOpList()
	 */
	@Override
	public void visit(DirectDeclarator n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ( ADeclaratorOp() )*
	 */
	@Override
	public void visit(DeclaratorOpList n) {
		for (Node elem : n.getF0().getNodes()) {
			if (n.getF0().getNodes().indexOf(elem) == 0) {
				if (!(((ADeclaratorOp) elem).getF0().getChoice() instanceof DimensionSize)) {
					declCodeString += elem.getInfo().getString();
				}
			} else {
				declCodeString += elem.getInfo().getString();
			}
		}
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | "(" Declarator() ")"
	 */
	@Override
	public void visit(IdentifierOrDeclarator n) {
		if (n.getF0().getChoice() instanceof NodeToken) {
			declCodeString += varName + " ";
			return;
		}
		declCodeString += "(";
		n.getF0().accept(this);
		declCodeString += ")";
	}

}
