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
import imop.parser.FrontEnd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents <code>enum</code>erators.
 * 
 * @author aman
 *
 */
public class EnumType extends IntegerType {

	/**
	 * Defines the scope (a CompoundStatement, or a TranslationUnit) in which
	 * this structure has been declared.
	 */
	private Scopeable definingScope;
	private Declaration declaringNode;

	private String tag;
	private List<EnumMember> enumMembers = new ArrayList<>();
	private boolean complete;

	public static class EnumMember {
		private String name;
		private ConstantExpression value;

		public EnumMember(String name) {
			this.setName(name);
		}

		public EnumMember(String name, ConstantExpression value) {
			this.setName(name);
			this.setValue(value);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ConstantExpression getValue() {
			return value;
		}

		public void setValue(ConstantExpression value) {
			this.value = value;
		}
	}

	/**
	 * This method is generally called before diving into the definition, if
	 * any, of
	 * the related enumeration.
	 * 
	 * @param tag
	 */
	public EnumType(String tag, Scopeable definingScope, Declaration declaringNode) {
		this.setTag(tag);
		this.complete = false;
		this.setDefiningScope(definingScope);
		this.setDeclaringNode(declaringNode);
	}

	/**
	 * This method is called at the end of the construction
	 * of an enumerator.
	 * 
	 * @param enumMembers
	 */
	public void makeComplete(List<EnumMember> enumMembers) {
		this.setEnumMembers(enumMembers);
		this.complete = true;
	}

	@Override
	public boolean isComplete() {
		return complete;
	}

	public ConstantExpression getMemberValue(EnumMember enumMember) {
		if (getEnumMembers() == null) {
			return FrontEnd.parseAlone("-1", ConstantExpression.class);
		}
		for (EnumMember eM : getEnumMembers()) {
			if (eM.getName().equals(enumMember.getName())) {
				return eM.getValue();
			}
		}
		return FrontEnd.parseAlone("-1", ConstantExpression.class);
	}

	@Override
	public int getIntegerConversionRank() {
		return 4; // TODO: Need to make it equal to the compatible integer type, instead.
	}

	@Override
	protected String preDeclarationString() {
		String retString = "enum " + getTag() + " ";
		return retString;
	}

	@Override
	protected String preString() {
		String retString = "enum " + getTag();
		if (!this.isComplete()) {
			retString += " ";
			return retString;
		}
		retString += " {";
		for (EnumMember mem : this.getEnumMembers()) {
			retString += mem.getName();
			if (mem.getValue() != null) {
				retString += " = " + mem.getValue();
			}
			if (mem != this.getEnumMembers().get(this.getEnumMembers().size() - 1)) {
				retString += ", ";
			}
		}
		retString += "} ";
		return retString;
	}

	@Override
	protected String preStringForCopy() {
		return this.preDeclarationString();
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

	public List<EnumMember> getEnumMembers() {
		return enumMembers;
	}

	public void setEnumMembers(List<EnumMember> enumMembers) {
		this.enumMembers = enumMembers;
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
		EnumType other = (EnumType) obj;
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
		return typeSet;
	}

	@Override
	public boolean isCompatibleWith(Type t) {
		if (t == this) {
			return true;
		}
		if (!(t instanceof EnumType)) {
			return false;
		}
		EnumType that = (EnumType) t;
		if (!this.tag.equals(that.tag)) {
			return false;
		}
		if (this.isComplete() != that.isComplete()) {
			return false;
		}
		if (this.isComplete()) {
			if (this.enumMembers.size() != that.enumMembers.size()) {
				return false;
			}
			for (int i = 0; i < enumMembers.size(); i++) {
				EnumMember thisMember = this.enumMembers.get(i);
				EnumMember thatMember = that.enumMembers.get(i);
				if ((thisMember.name == null) != (thatMember.name == null)) {
					return false;
				} else if (thisMember.name != null) {
					if (!thisMember.name.equals(thatMember.name)) {
						return false;
					}
				}
				if ((thisMember.value == null) != (thatMember.value == null)) {
					return false;
				} else if (thisMember.value != null) {
					if (!thisMember.value.toString().equals(thatMember.value.toString())) {
						return false;
					}
				}
			}

		}
		return true;
	}
}
