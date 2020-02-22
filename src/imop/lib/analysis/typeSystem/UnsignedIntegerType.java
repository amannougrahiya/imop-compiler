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
 * Superclass of all unsigned integer types, which are:
 * <ol>
 * <li>Standard unsigned integer</li>
 * <li>Extended unsigned integer</li>
 * </ol>
 * 
 * @author aman
 *
 */
public abstract class UnsignedIntegerType extends IntegerType {

	@Override
	public boolean isBasicType() {
		return true;
	}

	@Override
	public final boolean isComplete() {
		return true;
	}

	/**
	 * Returns the corresponding signed integer type.
	 * 
	 * @return
	 */
	public abstract SignedIntegerType getSignedType();
}
