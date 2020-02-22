/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.typeSystem;

import java.util.HashSet;
import java.util.Set;

public class VoidType extends Type {

	private static VoidType singleton;

	@Override
	public boolean isComplete() {
		return false;
	}

	public static VoidType type() {
		if (singleton == null) {
			singleton = new VoidType();
		}
		return singleton;
	}

	@Override
	protected String preDeclarationString() {
		String string = "void ";
		return string;
	}

	@Override
	protected String preString() {
		return "void ";
	}

	@Override
	protected String postDeclarationString() {
		return "";
	}

	@Override
	protected String postString() {
		return "";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		return typeSet;
	}

	@Override
	public boolean isKnownConstantSized() {
		return true;
	}

	@Override
	public boolean isCompatibleWith(Type t) {
		if (t == this) {
			return true;
		}
		return false;
	}
}
