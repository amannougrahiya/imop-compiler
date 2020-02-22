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

public class SignedIntType extends StandardSignedIntegerType {
	private static SignedIntType singleton;

	private SignedIntType() {
	}

	public static SignedIntType type() {
		if (singleton == null) {
			singleton = new SignedIntType();
		}
		return singleton;
	}

	@Override
	public int getIntegerConversionRank() {
		return 4;
	}

	@Override
	public UnsignedIntegerType getUnsignedType() {
		return UnsignedIntType.type();
	}

	@Override
	protected String preDeclarationString() {
		return "int ";
	}

	@Override
	protected String preString() {
		return "int ";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(SignedIntType.type());
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
