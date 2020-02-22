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

public class UnsignedLongLongIntType extends StandardUnsignedIntegerType {
	private static UnsignedLongLongIntType singleton;

	private UnsignedLongLongIntType() {
	}

	public static UnsignedLongLongIntType type() {
		if (singleton == null) {
			singleton = new UnsignedLongLongIntType();
		}
		return singleton;
	}

	@Override
	public int getIntegerConversionRank() {
		return 6;
	}

	@Override
	public SignedIntegerType getSignedType() {
		return SignedLongLongIntType.type();
	}

	@Override
	protected String preDeclarationString() {
		String string = "unsigned long long int ";
		return string;
	}

	@Override
	protected String preString() {
		return "unsigned long long int ";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(UnsignedLongLongIntType.type());
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
