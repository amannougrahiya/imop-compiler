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

public class LongDoubleComplexType extends ComplexType {
	private static LongDoubleComplexType singleton;

	private LongDoubleComplexType() {
	}

	public static LongDoubleComplexType type() {
		if (singleton == null) {
			singleton = new LongDoubleComplexType();
		}
		return singleton;
	}

	@Override
	public Type getRealType() {
		return LongDoubleType.type();
	}

	@Override
	protected String preDeclarationString() {
		String string = " long double _Complex ";
		return string;
	}

	@Override
	protected String preString() {
		return "long double _Complex ";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(LongDoubleComplexType.type());
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
