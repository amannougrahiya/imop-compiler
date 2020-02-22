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
 * Represents the atomic types of C, which are defined
 * as <code>_Atomic(T)</code> for a given type <code>T</code>.
 * 
 * @author aman
 *
 */
public class AtomicType extends DerivedType {
	private Type elementType;

	public AtomicType(Type elementType) {
		this.elementType = elementType;
	}

	public Type getElementType() {
		return elementType;
	}

	@Override
	protected String preDeclarationString() {
		String retString = "_Atomic(";
		if (this.elementType instanceof StructType || this.elementType instanceof UnionType
				|| this.elementType instanceof EnumType) {
			retString += this.elementType.preDeclarationString();
		} else {
			retString += this.elementType.preStringForCopy();
		}
		retString += ")";
		return retString;
	}

	@Override
	protected String preStringForCopy() {
		String retString = "_Atomic(";
		retString += this.elementType.preStringForCopy();
		retString += ")";
		return retString;
	}

	@Override
	protected String preString() {
		String retString = "_Atomic(";
		retString += this.elementType.preString();
		retString += ")";
		return retString;
	}

	@Override
	protected String postDeclarationString() {
		return this.elementType.postDeclarationString();
	}

	@Override
	protected String postString() {
		return this.elementType.postString();
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.addAll(this.elementType.getAllTypes());
		return typeSet;
	}

	@Override
	public boolean isKnownConstantSized() {
		return this.elementType.isKnownConstantSized();
	}

	@Override
	public boolean isCompatibleWith(Type t) {
		if (t == this) {
			return true;
		}
		if (!(t instanceof AtomicType)) {
			return false;
		}
		AtomicType that = (AtomicType) t;
		if (!this.elementType.isCompatibleWith(that.elementType)) {
			return false;
		}
		return true;
	}
}
