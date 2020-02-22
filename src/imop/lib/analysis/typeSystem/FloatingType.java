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
 * Superclass of all the floating types, which are of two types:
 * <ol>
 * <li>Real floating</li>
 * <li>Complex</li>
 * </ol>
 *
 */
public abstract class FloatingType extends ArithmeticType {
	@Override
	public boolean isBasicType() {
		return true;
	}

	@Override
	public final boolean isComplete() {
		return true;
	}
}
