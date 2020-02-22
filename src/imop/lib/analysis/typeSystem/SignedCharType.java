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

public class SignedCharType extends StandardSignedIntegerType {
	private static SignedCharType singleton;

	private SignedCharType() {
	}

	public static SignedCharType type() {
		if (singleton == null) {
			singleton = new SignedCharType();
		}
		return singleton;
	}

	@Override
	public boolean isCharacterType() {
		return true;
	}

	@Override
	public int getIntegerConversionRank() {
		return 2;
	}

	@Override
	public UnsignedIntegerType getUnsignedType() {
		return UnsignedCharType.type();
	}

	@Override
	protected String preDeclarationString() {
		String string = "signed char ";
		return string;
	}

	@Override
	protected String preString() {
		return "signed char ";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(SignedCharType.type());
		return typeSet;
	}

	@Override
	public boolean isCompatibleWith(Type t) {
		if (t == this) {
			return true;
		}
		return false;
	}

}
