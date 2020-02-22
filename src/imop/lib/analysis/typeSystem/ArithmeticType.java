/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.typeSystem;

public abstract class ArithmeticType extends Type {

	@Override
	protected String postString() {
		return "";
	}

	@Override
	protected String postDeclarationString() {
		return "";
	}

	@Override
	public boolean isKnownConstantSized() {
		return true;
	}
}
