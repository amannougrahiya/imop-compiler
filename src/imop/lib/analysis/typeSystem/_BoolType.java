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

public class _BoolType extends StandardUnsignedIntegerType {
	private static _BoolType singleton;

	private _BoolType() {
	}

	public static _BoolType type() {
		if (singleton == null) {
			singleton = new _BoolType();
		}
		return singleton;
	}

	@Override
	public int getIntegerConversionRank() {
		return 1;
	}

	@Override
	public SignedIntegerType getSignedType() {
		assert (false);
		return null;
	}

	@Override
	protected String preDeclarationString() {
		String string = "_Bool ";
		return string;
	}

	@Override
	protected String preString() {
		return "_Bool ";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(_BoolType.type());
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
