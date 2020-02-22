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

public class DoubleComplexType extends ComplexType {
	private static DoubleComplexType singleton;

	private DoubleComplexType() {
	}

	public static DoubleComplexType type() {
		if (singleton == null) {
			singleton = new DoubleComplexType();
		}
		return singleton;
	}

	@Override
	public Type getRealType() {
		return DoubleType.type();
	}

	@Override
	protected String preDeclarationString() {
		return " double _Complex ";
	}

	@Override
	protected String preString() {
		return "double _Complex ";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(DoubleComplexType.type());
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
