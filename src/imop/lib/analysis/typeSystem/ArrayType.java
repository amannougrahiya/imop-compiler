/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.typeSystem;

import imop.ast.node.external.*;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the array type.
 * 
 * @author aman
 *
 */
public class ArrayType extends DerivedType {
	private Type elementType;

	/**
	 * Represents the number of elements that this array can hold.
	 * Note that value 0 implies that this array is an incomplete type,
	 * and value -1 implies that this array has a variable length.
	 * TODO: Later, we might want to change the type from ConstantExpression to
	 * "int".
	 */
	private ConstantExpression size;
	private Length sizeType;
	private boolean complete;

	public static enum Length {
		VARIABLE_LENGTH, FIXED_LENGTH
	}

	public ArrayType(Type elementType, ConstantExpression size) {
		// assert (elementType.isComplete());
		try {
			Integer.parseInt(size.toString().trim());
			this.sizeType = Length.FIXED_LENGTH;
		} catch (NumberFormatException e) {
			if (Misc.isConstant(size.toString())) {
				this.sizeType = Length.FIXED_LENGTH;
			} else {
				this.sizeType = Length.VARIABLE_LENGTH;
			}
		}
		this.elementType = elementType;
		this.size = size;
		this.complete = true;
	}

	public ArrayType(Type elementType, Length sizeType) {
		assert (elementType.isComplete());
		assert (sizeType == Length.VARIABLE_LENGTH);

		this.elementType = elementType;
		this.size = FrontEnd.parseAlone("-1", ConstantExpression.class);
		this.sizeType = Length.VARIABLE_LENGTH;
		this.complete = true;
	}

	/**
	 * Constructor for creating array of unknown size.
	 * 
	 * @param elementType
	 */
	public ArrayType(Type elementType) {
		assert (elementType.isComplete());
		this.elementType = elementType;
		this.size = FrontEnd.parseAlone("0", ConstantExpression.class);
		this.sizeType = Length.FIXED_LENGTH;
		this.complete = false;
	}

	@Override
	public boolean isComplete() {
		return complete;
	}

	public Type getElementType() {
		return elementType;
	}

	/**
	 * Returns the maximum number of elements that the array of this type
	 * may hold.
	 * <p>
	 * Value is 0, if the type is incomplete.
	 * It is -1, if the corresponding array has a variable length.
	 * 
	 * @return
	 */
	public ConstantExpression getArraySize() {
		return size;
	}

	public boolean isVariableLengthed() {
		if (sizeType == Length.VARIABLE_LENGTH) {
			return true;
		} else {
			return false;
		}
	}

	// @Override
	// protected String getDeclarationPostString(String tempName, boolean
	// isFullDeclarator) {
	// if (isFullDeclarator) {
	// this.getDeclaration(tempName);
	// } else {
	// if (elementType instanceof ArrayType || elementType instanceof PointerType
	// || elementType instanceof FunctionType) {
	// String sizeStr = size.getInfo().getString().trim();
	// if (sizeStr.equals("0") || sizeStr.equals("-1")) {
	// return elementType.getDeclarationPostString(" " + tempName + "[" + "]",
	// false);
	// } else {
	// return elementType.getDeclarationPostString(" " + tempName + "[" + sizeStr +
	// "]", false);
	// }
	//
	// } else {
	// String sizeStr = size.getInfo().getString().trim();
	// if (sizeStr.equals("0") || sizeStr.equals("-1")) {
	// return " " + tempName + "[" + "]";
	// } else {
	// return " " + tempName + "[" + sizeStr + "]";
	// }
	// }
	// }
	// return null;
	// }

	// @Override
	// protected String postDeclarationString(String tempName) {
	// if (elementType instanceof ArrayType || elementType instanceof PointerType
	// || elementType instanceof FunctionType) {
	// return elementType.getDeclarationPostString(" (*" + tempName + ")", false);
	// } else {
	// return " (*" + tempName + ")";
	// }
	// }

	// @Override
	// public String toString() {
	// String returnString = this.getDeclarationSpecifiersString();
	// returnString += this.getDeclaratorString("zzz", false).replace("zzz", " ");
	// return returnString;
	// }

	// @Override
	// protected String preDeclarationString() {
	// StringBuilder stringBuilder = new StringBuilder();
	// stringBuilder.append(" ");
	// stringBuilder.append(elementType.preDeclarationString());
	// stringBuilder.append(" ");
	// return stringBuilder.toString();
	// }

	@Override
	protected String preDeclarationString() {
		String retString = "";
		// retString += this.elementType.preStringForCopy();
		if (elementType instanceof StructType || elementType instanceof UnionType || elementType instanceof EnumType) {
			retString += this.elementType.preDeclarationString();
		} else {
			retString += this.elementType.preStringForCopy();
		}
		retString += " (*";
		return retString;
	}

	@Override
	protected String preStringForCopy() {
		String retString = this.elementType.preStringForCopy() + " ";
		return retString;
	}

	@Override
	protected String preString() {
		String retString = this.elementType.preString() + " ";
		return retString;
	}

	@Override
	protected String postDeclarationString() {
		return ")" + this.elementType.postString();
	}

	@Override
	protected String postString() {
		String retString = "[";
		if (this.isComplete()) {
			if (this.sizeType == Length.VARIABLE_LENGTH) {
				retString += this.size;
			} else {
				retString += this.size;
			}
		}
		retString += "]" + this.elementType.postString();
		return retString;
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.addAll(elementType.getAllTypes());
		return typeSet;
	}

	@Override
	public boolean isKnownConstantSized() {
		return !this.isVariableLengthed();
	}

	@Override
	public boolean isCompatibleWith(Type t) {
		if (t == this) {
			return true;
		}
		if (t instanceof PointerType) {
			PointerType that = (PointerType) t;
			if (this.elementType.isCompatibleWith(that.getPointeeType())) {
				return true;
			}
		} else if (t instanceof ArrayType) {
			ArrayType that = (ArrayType) t;
			if (!that.getElementType().isCompatibleWith(this.getElementType())) {
				return false;
			}
			if (!that.size.toString().equals(this.size.toString())) {
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean containsPointers() {
		Type elemType = this.elementType;
		if (elemType instanceof PointerType) {
			return true;
		} else if (elemType instanceof ArrayType) {
			return ((ArrayType) elemType).containsPointers();
		} else if (elemType instanceof FunctionType) {
			return true;
		} else if (elemType instanceof StructType || elemType instanceof UnionType) {
			// TODO: Make this more precise.
			return true;
		} else if (elemType instanceof EnumType) {
			return false;
		} else if (elemType instanceof ArithmeticType) {
			return false;
		}
		return true;
	}
}
