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

public class FloatType extends RealFloatingType {
	private static FloatType singleton;

	private FloatType() {
	}

	public static FloatType type() {
		if (singleton == null) {
			singleton = new FloatType();
		}
		return singleton;
	}

	@Override
	public Type getComplexType() {
		return FloatComplexType.type();
	}

	@Override
	protected String preDeclarationString() {
		return "float ";
	}

	@Override
	protected String preString() {
		return "float ";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(FloatType.type());
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
