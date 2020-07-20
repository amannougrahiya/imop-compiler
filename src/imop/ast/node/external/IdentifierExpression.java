/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.node.external;

public class IdentifierExpression extends Expression {
	{
		classId = 1658;
	}

	public IdentifierExpression() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6156917027464352559L;

	@Override
	public boolean isCFGNode() {
		return false;
	}

}
