/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.NodeInfo;
import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.util.Misc;

public class ParameterDeclarationInfo extends NodeInfo {

	public ParameterDeclarationInfo(Node owner) {
		super(owner);
	}

	public Symbol getDeclaredSymbol() {
		NodeToken declaredToken = ParameterDeclarationInfo.getRootParamNodeToken((ParameterDeclaration) this.getNode());
		if (declaredToken == null) {
			return null;
		}
		return Misc.getSymbolEntry(declaredToken.toString(), this.getNode());
	}

	/**
	 * @param paramList
	 * @param paramName
	 * @return a ParameterDeclaration corresponding to paramName in paramList
	 */
	public static ParameterDeclaration getParameterDeclarationFromParameterList(ParameterList paramList,
			String paramName) {
		ParameterDeclaration paramDecl = paramList.getF0();
		if (paramName.equals(getRootParamName(paramDecl))) {
			return paramDecl;
		}
		for (Node seq : paramList.getF1().getNodes()) {
			paramDecl = (ParameterDeclaration) ((NodeSequence) seq).getNodes().get(1);
			if (paramName.equals(getRootParamName(paramDecl))) {
				return paramDecl;
			}
		}
		return null;
	}

	/**
	 * @param decl
	 * @return {@link NodeToken} being declared in this parameter-declaration.
	 * 
	 */
	public static NodeToken getRootParamNodeToken(ParameterDeclaration decl) {
		Node declChoice = decl.getF1().getF0().getChoice();
		if (declChoice instanceof Declarator) {
			return DeclarationInfo.getRootIdNodeToken((Declarator) declChoice);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param decl
	 * @return id Name being declared in this parameterDeclaration
	 * 
	 */
	public static String getRootParamName(ParameterDeclaration decl) {
		Node declChoice = decl.getF1().getF0().getChoice();
		if (declChoice instanceof Declarator) {
			return DeclarationInfo.getRootIdName((Declarator) declChoice);
		} else {
			return null;
		}
	}

}
