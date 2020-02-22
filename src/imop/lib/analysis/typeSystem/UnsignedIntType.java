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

public class UnsignedIntType extends StandardUnsignedIntegerType {
	private static UnsignedIntType singleton;

	private UnsignedIntType() {
	}

	public static UnsignedIntType type() {
		if (singleton == null) {
			singleton = new UnsignedIntType();
		}
		return singleton;
	}

	@Override
	public int getIntegerConversionRank() {
		return 4;
	}

	@Override
	public SignedIntegerType getSignedType() {
		return SignedIntType.type();
	}

	@Override
	protected String preDeclarationString() {
		String string = "unsigned int ";
		return string;
	}

	@Override
	protected String preString() {
		return "unsigned int ";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(UnsignedIntType.type());
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
