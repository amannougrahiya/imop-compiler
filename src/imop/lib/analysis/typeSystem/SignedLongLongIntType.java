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

public class SignedLongLongIntType extends StandardSignedIntegerType {
	private static SignedLongLongIntType singleton;

	private SignedLongLongIntType() {
	}

	public static SignedLongLongIntType type() {
		if (singleton == null) {
			singleton = new SignedLongLongIntType();
		}
		return singleton;
	}

	@Override
	public int getIntegerConversionRank() {
		return 6;
	}

	@Override
	public UnsignedIntegerType getUnsignedType() {
		return UnsignedLongLongIntType.type();
	}

	@Override
	protected String preDeclarationString() {
		String string = "signed long long int ";
		return string;
	}

	@Override
	protected String preString() {
		return "signed long long int ";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(SignedLongLongIntType.type());
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
