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
 * Superclass of all signed integer tyoes, which are:
 * <ol>
 * <li>Standard signed integer</li>
 * <li>Extended signed integer</li>
 * </ol>
 * 
 * @author aman
 *
 */
public abstract class SignedIntegerType extends IntegerType {

	@Override
	public boolean isBasicType() {
		return true;
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	/**
	 * Returns the corresponding unsigned integer type,
	 * for the current signed integer type.
	 * 
	 * @return
	 */
	public abstract UnsignedIntegerType getUnsignedType();
}
