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

public class UnsignedCharType extends StandardUnsignedIntegerType {
	private static UnsignedCharType singleton;

	private UnsignedCharType() {
	}

	public static UnsignedCharType type() {
		if (singleton == null) {
			singleton = new UnsignedCharType();
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
	public SignedIntegerType getSignedType() {
		return SignedCharType.type();
	}

	@Override
	protected String preDeclarationString() {
		String string = "unsigned char ";
		return string;
	}

	@Override
	protected String preString() {
		return "unsigned char ";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(UnsignedCharType.type());
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
