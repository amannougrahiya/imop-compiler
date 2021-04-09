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
import imop.ast.node.internal.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a union type.
 * 
 * @author aman
 *
 */
public class UnionType extends DerivedType implements Scopeable {

	/**
	 * Defines the scope (a CompoundStatement, or a TranslationUnit) in which
	 * this structure has been declared.
	 */
	private Scopeable definingScope;
	private Declaration declaringNode;

	private String tag;
	private boolean complete;
	private List<StructOrUnionMember> elementList;
	private HashMap<String, Type> typeTable;
	private HashMap<String, Typedef> typedefTable;

	/**
	 * This method is generally called before diving into the definition, if
	 * any, of
	 * the related union.
	 */
	public UnionType(String tag, Scopeable definingScope, Declaration declaringNode) {
		this.setTag(tag);
		this.complete = false;
		this.setDefiningScope(definingScope);
		this.declaringNode = declaringNode;
	}

	/**
	 * This method is called at the end of the construction
	 * of a union.
	 * 
	 * @param elementList
	 */
	public void makeComplete(List<StructOrUnionMember> elementList) {
		this.elementList = elementList;
		this.complete = true;
	}

	public void setTypedefTable(HashMap<String, Typedef> typedefTable) {
		this.typedefTable = typedefTable;
	}

	public HashMap<String, Typedef> getTypedefTable() {
		if (typedefTable == null) {
			setTypedefTable(new HashMap<String, Typedef>());
		}
		return typedefTable;

	}
	// public void populateTypeTable() {
	// setTypeTable(new HashMap<String, Type>());
	// Type.getTypeTree((Declaration) Misc.getEnclosingNode(this.definingNode,
	// Declaration.class));
	// }

	@Override
	public HashMap<String, Type> getTypeTable() {
		if (typeTable == null) {
			setTypeTable(new HashMap<String, Type>());
		}
		return typeTable;
	}

	public void setTypeTable(HashMap<String, Type> typeTable) {
		this.typeTable = typeTable;
	}

	@Override
	public boolean isComplete() {
		return complete;
	}

	public List<StructOrUnionMember> getElementList() {
		return elementList;
	}

	public Type getElementType(String elementName) {
		if (elementList == null) {
			return null;
		}
		for (StructOrUnionMember elem : elementList) {
			if (elem.getElementName().equals(elementName)) {
				return elem.getElementType();
			}
		}
		return null;
	}

	@Override
	protected String preDeclarationString() {
		String retString = "union " + getTag() + " ";
		return retString;
	}

	@Override
	protected String preString() {
		String retString = "union " + getTag();
		if (!this.isComplete()) {
			retString += " ";
			return retString;
		} else {
			retString += " {";
			for (StructOrUnionMember mem : this.getElementList()) {
				retString += mem.getElementType().toString(mem.getElementName());
				if (mem.isBitField()) {
					retString += ":" + mem.getBitFieldSize();
				}
				retString += "; ";
			}
			retString += "}";
		}
		return retString;
	}

	@Override
	protected String preStringForCopy() {
		return this.preDeclarationString();
	}

	@Override
	protected String postDeclarationString() {
		return "";
	}

	@Override
	protected String postString() {
		return "";
	}

	public Scopeable getDefiningScope() {
		return definingScope;
	}

	public void setDefiningScope(Scopeable definingScope) {
		this.definingScope = definingScope;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Declaration getDeclaringNode() {
		return declaringNode;
	}

	public void setDeclaringNode(Declaration declaringNode) {
		this.declaringNode = declaringNode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((definingScope == null) ? 0 : definingScope.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UnionType other = (UnionType) obj;
		if (definingScope == null) {
			if (other.definingScope != null) {
				return false;
			}
		} else if (!definingScope.equals(other.definingScope)) {
			return false;
		}
		if (tag == null) {
			if (other.tag != null) {
				return false;
			}
		} else if (!tag.equals(other.tag)) {
			return false;
		}
		return true;
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.add(this);
		if (this.getElementList() != null) {
			for (StructOrUnionMember mem : this.getElementList()) {
				typeSet.addAll(mem.getElementType().getAllTypes());
			}
		}
		for (Typedef tDef : this.getTypedefTable().values()) {
			typeSet.addAll(tDef.getType().getAllTypes());
		}
		return typeSet;
	}

	@Override
	public boolean isKnownConstantSized() {
		for (StructOrUnionMember mem : this.getElementList()) {
			if (!mem.getElementType().isKnownConstantSized()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isCompatibleWith(Type t) {
		if (t == this) {
			return true;
		}
		if (!(t instanceof EnumType)) {
			return false;
		}
		UnionType that = (UnionType) t;
		if (!this.tag.equals(that.tag)) {
			return false;
		}
		if (this.isComplete() != that.isComplete()) {
			return false;
		}
		if (this.isComplete()) {
			if (this.elementList.size() != that.elementList.size()) {
				return false;
			}
			for (int i = 0; i < elementList.size(); i++) {
				StructOrUnionMember thisMember = this.elementList.get(i);
				StructOrUnionMember thatMember = that.elementList.get(i);
				if ((thisMember.getElementName() == null) != (thatMember.getElementName() == null)) {
					return false;
				} else if (thisMember.getElementName() != null) {
					if (!thisMember.getElementName().equals(thatMember.getElementName())) {
						return false;
					}
				}
				if (thisMember.isBitField() != thatMember.isBitField()) {
					return false;
				} else if (thisMember.isBitField()) {
					if (!thisMember.getBitFieldSize().toString().equals(thatMember.getBitFieldSize().toString())) {
						return false;
					}
				}
				if (!thisMember.getElementType().isCompatibleWith(thatMember.getElementType())) {
					return false;
				}
			}
		}
		return true;
	}
}
