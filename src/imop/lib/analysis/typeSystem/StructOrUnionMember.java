/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.typeSystem;

import imop.ast.node.external.*;

public class StructOrUnionMember {
	private Type elementType;
	private String elementName;
	private ConstantExpression bitFieldSize;

	public StructOrUnionMember(Type elementType, String elementName) {
		this.setElementType(elementType);
		this.setElementName(elementName);
	}

	public StructOrUnionMember(Type elementType, String elementName, ConstantExpression bitFieldSize) {
		this.setElementType(elementType);
		this.setElementName(elementName);
		this.bitFieldSize = bitFieldSize;
	}

	public StructOrUnionMember(Type elementType) {
		this.setElementType(elementType);
	}

	public StructOrUnionMember(Type elementType, ConstantExpression bitFieldSize) {
		this.setElementType(elementType);
		this.bitFieldSize = bitFieldSize;
	}

	public ConstantExpression getBitFieldSize() {
		return bitFieldSize;
	}

	public boolean isBitField() {
		if (this.bitFieldSize == null) {
			return false;
		} else {
			return true;
		}
	}

	public Type getElementType() {
		return elementType;
	}

	public void setElementType(Type elementType) {
		this.elementType = elementType;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
}
