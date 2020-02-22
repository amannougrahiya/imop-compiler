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
 * <i>Note: This type is optional.</i>
 * Superclass of all the complex types, which are three in number:
 * <ol>
 * <li>FloatComplex</li>
 * <li>DoubleComplex</li>
 * <li>LongDoubleComplex</li>
 * </ol>
 * 
 * @author aman
 *
 */
public abstract class ComplexType extends FloatingType {
	@Override
	public TypeDomain getTypeDomain() {
		return TypeDomain.COMPLEX;
	}
}
