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

/**
 * Represents the <code>char</code> type of C.
 * Note that as per the ISO C11 standard, this type
 * is distinct from its signed or unsigned versions.
 * 
 * @author aman
 *
 */
public class CharType extends IntegerType {
	private static CharType singleton;

	public static CharType type() {
		if (singleton == null) {
			singleton = new CharType();
		}
		return singleton;
	}

	@Override
	public boolean isCharacterType() {
		return true;
	}

	@Override
	public boolean isBasicType() {
		return true;
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	@Override
	public int getIntegerConversionRank() {
		return 2;
	}

	@Override
	protected String preDeclarationString() {
		return "char ";
	}

	@Override
	protected String preString() {
		return "char ";
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(CharType.type());
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
