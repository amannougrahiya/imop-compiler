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

public class SignedShortIntType extends StandardSignedIntegerType {
	private static SignedShortIntType singleton;

	private SignedShortIntType() {
	}

	public static SignedShortIntType type() {
		if (singleton == null) {
			singleton = new SignedShortIntType();
		}
		return singleton;
	}

	@Override
	public int getIntegerConversionRank() {
		return 3;
	}

	@Override
	public UnsignedIntegerType getUnsignedType() {
		return UnsignedShortIntType.type();
	}

	@Override
	protected String preDeclarationString() {
		String string = "signed short int ";
		return string;
	}

	@Override
	protected String preString() {
		return "signed short int ";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(SignedShortIntType.type());
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
