/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.typeSystem;

/**
 * Superclass of all the implementation-defined extended
 * unsigned integer types.
 * 
 * @author aman
 *
 */
public abstract class ExtendedUnsignedIntegerType extends UnsignedIntegerType {
	@Override
	public int getIntegerConversionRank() {
		return 0;
	}

	@Override
	public boolean isCompatibleWith(Type t) {
		if (t == this) {
			return true;
		}
		return false;
	}
}
