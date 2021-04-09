/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info;

import imop.ast.info.cfgNodeInfo.CompoundStatementInfo;
import imop.ast.info.cfgNodeInfo.FunctionDefinitionInfo;
import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstVisitor;

/**
 * Populates the symbol tables for TranslationUnit, FunctionDefinition and
 * CompoundStatements.
 */
@Deprecated
public class ReInitSymbolAndTypeTables extends DepthFirstVisitor {

	/**
	 * f0 ::= ( ElementsOfTranslation() )+
	 */
	@Override
	public void visit(TranslationUnit n) {
		RootInfo rootInfo = n.getInfo();
		rootInfo.populateSymbolTable(); // Must be called before calling populateThreadPrivateList.
		rootInfo.populateThreadPrivateList();
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public void visit(FunctionDefinition n) {
		FunctionDefinitionInfo funcDefInfo = n.getInfo();
		funcDefInfo.populateSymbolTable();
		n.getF3().accept(this);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public void visit(CompoundStatement n) {
		CompoundStatementInfo compStmtInfo = n.getInfo();
		// Old code below
		// compStmtInfo.populateSymbolTable();
		n.getF1().accept(this);
	}

}
