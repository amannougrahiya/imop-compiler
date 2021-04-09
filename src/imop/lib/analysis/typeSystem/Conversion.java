/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.typeSystem;

public class Conversion {
	private Conversion() {
	}

	/**
	 * Returns the <i>common real type</i> of given two types.
	 * 
	 * @param t1
	 *           an operand type.
	 * @param t2
	 *           an operand type.
	 * @return
	 *         type obtained after applying usual arithmetic conversions on the
	 *         operand types.
	 */
	public static Type getUsualArithmeticConvertedType(Type t1, Type t2) {
		if (t1 == null || t2 == null) {
			return null;
		}
		if (!(t1 instanceof ArithmeticType) || !(t2 instanceof ArithmeticType)) {
			// This might be some unstable state of the code.
			return SignedIntType.type();
		}
		Type t3 = null;
		Type realT1 = t1.getRealType();
		Type realT2 = t2.getRealType();

		if (realT1 instanceof LongDoubleType || realT2 instanceof LongDoubleType) {
			t3 = LongDoubleType.type();
		} else if (realT1 instanceof DoubleType || realT2 instanceof DoubleType) {
			t3 = DoubleType.type();
		} else if (realT1 instanceof FloatType || realT2 instanceof FloatType) {
			t3 = FloatType.type();
		} else {
			assert (realT1 instanceof IntegerType);
			assert (realT2 instanceof IntegerType);
			IntegerType integerT1 = (IntegerType) realT1.getIntegerPromotedType();
			IntegerType integerT2 = (IntegerType) realT2.getIntegerPromotedType();

			if (integerT1 == integerT2) {
				t3 = integerT1;
			} else if (integerT1 instanceof SignedIntegerType && integerT2 instanceof SignedIntegerType
					|| integerT1 instanceof UnsignedIntegerType && integerT2 instanceof UnsignedIntegerType) {
				if (integerT1.getIntegerConversionRank() > integerT2.getIntegerConversionRank()) {
					t3 = integerT1;
				} else {
					t3 = integerT2;
				}
			} else if (integerT1 instanceof UnsignedIntegerType) {
				if (integerT1.getIntegerConversionRank() >= integerT2.getIntegerConversionRank()) {
					t3 = integerT1;
				} else {
					if (Conversion.canT1holdT2(integerT2, integerT1)) {
						t3 = integerT2;
					} else {
						t3 = ((SignedIntegerType) integerT2).getUnsignedType();
					}
				}
			} else if (integerT2 instanceof UnsignedIntegerType) {
				if (integerT2.getIntegerConversionRank() >= integerT1.getIntegerConversionRank()) {
					t3 = integerT2;
				} else {
					if (Conversion.canT1holdT2(integerT1, integerT2)) {
						t3 = integerT1;
					} else {
						t3 = ((SignedIntegerType) integerT1).getUnsignedType();
					}
				}
			}

		}

		if (t1.getTypeDomain() == TypeDomain.COMPLEX || t2.getTypeDomain() == TypeDomain.COMPLEX) {
			return t3.getComplexType();
		} else {
			return t3.getRealType();
		}
	}

	public static boolean canT1holdT2(IntegerType t1, IntegerType t2) {
		// TODO: Check if this code should be implementation-defined
		if (t1.getIntegerConversionRank() >= t2.getIntegerConversionRank()) {
			return true;
		} else {
			return false;
		}
	}
}
