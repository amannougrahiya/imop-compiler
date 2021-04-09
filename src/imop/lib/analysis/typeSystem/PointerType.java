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

public class PointerType extends DerivedType {
	private Type pointeeType;
	/**
	 * This flag is reset if the type has been generated from
	 * an array or a function type.
	 */
	private Type oldType = null;

	public PointerType(Type pointeeType) {
		this.pointeeType = pointeeType;
	}

	/**
	 * Used when an "array of T", or a "function returning T"
	 * is automatically converted to a "pointer to T", or a "pointer to function
	 * returning T", respectively.
	 * 
	 * @param pointeeType
	 * @param oldType
	 *                    "array of T", or a "function returning T"
	 * 
	 */
	public PointerType(Type pointeeType, Type oldType) {
		this.pointeeType = pointeeType;
		this.oldType = oldType;
	}

	public boolean isFromPointer() {
		if (oldType == null) {
			return true;
		}
		return false;
	}

	public Type getOldType() {
		return oldType;
	}

	public Type getPointeeType() {
		return pointeeType;
	}

	// @Override
	// protected String preDeclarationString() {
	// StringBuilder stringBuilder = new StringBuilder();
	// stringBuilder.append(" ");
	// stringBuilder.append(pointeeType.preDeclarationString());
	// stringBuilder.append(" ");
	// return stringBuilder.toString();
	// }
	//
	// @Override
	// protected String postDeclarationString(String tempName) {
	// if (pointeeType instanceof FunctionType) {
	// return pointeeType.getDeclarationPostString(" (* " + tempName + ")", false);
	// } else if (pointeeType instanceof PointerType) {
	// return pointeeType.postDeclarationString(" * " + tempName + " ");
	// } else if (pointeeType instanceof ArrayType) {
	// return pointeeType.getDeclarationPostString("(* " + tempName + ")", false);
	// } else {
	// StringBuilder stringBuilder = new StringBuilder();
	// stringBuilder.append(" * ");
	// stringBuilder.append(tempName);
	// stringBuilder.append(" ");
	// return stringBuilder.toString();
	// }
	// }
	//
	// @Override
	// public String toString() {
	// String returnString = this.getDeclarationSpecifiersString();
	// if (pointeeType instanceof FunctionType) {
	// returnString += pointeeType.getDeclaratorString(" (* ) ", false);
	// } else if (pointeeType instanceof PointerType) {
	// returnString += pointeeType.getDeclaratorString(" * ");
	// } else if (pointeeType instanceof ArrayType) {
	// returnString += pointeeType.getDeclaratorString(" (* ) ", false);
	// } else {
	// returnString += " * ";
	// }
	// return returnString;
	// }

	@Override
	protected String preDeclarationString() {
		String retString = "";
		// retString += this.pointeeType.preStringForCopy();
		if (pointeeType instanceof StructType || pointeeType instanceof UnionType || pointeeType instanceof EnumType) {
			retString += this.pointeeType.preDeclarationString();
		} else {
			retString += this.pointeeType.preStringForCopy();
		}
		if (this.pointeeType instanceof FunctionType || this.pointeeType instanceof ArrayType) {
			retString += " (*";
		} else {
			retString += " *";
		}
		return retString;
	}

	@Override
	protected String preStringForCopy() {
		String retString = this.pointeeType.preStringForCopy();
		if (this.pointeeType instanceof FunctionType || this.pointeeType instanceof ArrayType) {
			retString += " (*";
		} else {
			retString += " *";
		}
		return retString;
	}

	@Override
	protected String preString() {
		String retString = this.pointeeType.preString();
		if (this.pointeeType instanceof FunctionType || this.pointeeType instanceof ArrayType) {
			retString += " (*";
		} else {
			retString += " *";
		}
		return retString;
	}

	@Override
	protected String postDeclarationString() {
		String retString = "";
		if (this.pointeeType instanceof FunctionType || this.pointeeType instanceof ArrayType) {
			retString += ")";
		}
		retString += this.pointeeType.postString();
		return retString;
	}

	@Override
	protected String postString() {
		String retString = "";
		if (this.pointeeType instanceof FunctionType || this.pointeeType instanceof ArrayType) {
			retString += ")";
		}
		retString += this.pointeeType.postString();
		return retString;
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.addAll(pointeeType.getAllTypes());
		return typeSet;
	}

	@Override
	public boolean isKnownConstantSized() {
		return this.pointeeType.isKnownConstantSized();
	}

	@Override
	public boolean isCompatibleWith(Type t) {
		if (t == this) {
			return true;
		}
		if (t instanceof PointerType) {
			PointerType that = (PointerType) t;
			if (this.pointeeType.isCompatibleWith(that.pointeeType)) {
				return true;
			}
		} else if (t instanceof ArrayType) {
			ArrayType that = (ArrayType) t;
			if (that.getElementType().isCompatibleWith(this.pointeeType)) {
				return true;
			}
		} else if (t instanceof FunctionType) {
			FunctionType that = (FunctionType) t;
			if (that.isCompatibleWith(this.pointeeType)) {
				return true;
			}
		}
		return false;
	}
}
