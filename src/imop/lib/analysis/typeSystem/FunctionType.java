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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a function type.
 * 
 * @author aman
 *
 */
public class FunctionType extends DerivedType {
	private Type returnType;
	private List<Parameter> parameterList = new ArrayList<>();

	public static class Parameter {
		private Type parameterType;
		private String parameterName;
		private ParameterDeclaration parameterDeclaration;

		public Parameter(Type parameterType, ParameterDeclaration parameterDeclaration) {
			this.setParameterType(parameterType);
			this.setParameterDeclaration(parameterDeclaration);
		}

		public Parameter(Type parameterType, String parameterName, ParameterDeclaration parameterDeclaration) {
			this.setParameterType(parameterType);
			this.setParameterName(parameterName);
			this.setParameterDeclaration(parameterDeclaration);
		}

		public Type getParameterType() {
			return parameterType;
		}

		public void setParameterType(Type parameterType) {
			this.parameterType = parameterType;
		}

		public String getParameterName() {
			return parameterName;
		}

		public void setParameterName(String parameterName) {
			this.parameterName = parameterName;
		}

		public ParameterDeclaration getParameterDeclaration() {
			return parameterDeclaration;
		}

		public void setParameterDeclaration(ParameterDeclaration parameterDeclaration) {
			this.parameterDeclaration = parameterDeclaration;
		}

		@Override
		public String toString() {
			return parameterDeclaration.toString();
		}
	}

	public FunctionType(Type returnType, List<Parameter> parameterList) {
		this.returnType = returnType;
		if (parameterList != null) {
			this.parameterList = parameterList;
		}
	}

	public Type getReturnType() {
		return returnType;
	}

	public List<Parameter> getParameterList() {
		return parameterList;
	}

	public Type getParameterType(int index) {
		assert (index > 0 && index < parameterList.size());
		return parameterList.get(index).parameterType;
	}

	public int getParameterCount() {
		return parameterList.size();
	}

	// @Override
	// protected String preDeclarationString() {
	// StringBuilder stringBuilder = new StringBuilder();
	// stringBuilder.append(" ");
	// stringBuilder.append(returnType.preDeclarationString());
	// stringBuilder.append(" ");
	// return stringBuilder.toString();
	// }
	//
	// @Override
	// protected String getDeclarationPostString(String tempName, boolean
	// isFullDeclarator) {
	// if (isFullDeclarator) {
	// return this.postDeclarationString(tempName);
	// } else {
	// return tempName + " " + this.getParameterString();
	// }
	// }
	//
	// @Override
	// protected String postDeclarationString(String tempName) {
	// return " (* " + tempName + ") " + this.getParameterString();
	// }
	//
	// private String getParameterString() {
	// StringBuilder stringBuilder = new StringBuilder();
	// stringBuilder.append("(");
	// if (!parameterList.isEmpty()) {
	// Parameter lastElement = parameterList.get(parameterList.size() - 1);
	// for (Parameter param : parameterList) {
	// if (param == lastElement) {
	// break;
	// }
	// stringBuilder.append(param.getParameterType().preDeclarationString() + " "
	// + param.getParameterType().getDeclarationPostString("", false) + ", ");
	// }
	// // Add the last element (without comma)
	// stringBuilder.append(lastElement.getParameterType().preDeclarationString() +
	// " "
	// + lastElement.getParameterType().getDeclarationPostString("", false));
	// }
	// stringBuilder.append(")");
	// return stringBuilder.toString();
	// }
	//
	// @Override
	// public String toString() {
	// String returnString = this.getDeclarationSpecifiersString();
	// returnString += this.getParameterString();
	// return returnString;
	// }

	@Override
	protected String preDeclarationString() {
		String retString = "";
		// retString += this.returnType.preStringForCopy();
		if (returnType instanceof StructType || returnType instanceof UnionType || returnType instanceof EnumType) {
			retString += this.returnType.preDeclarationString();
		} else {
			retString += this.returnType.preStringForCopy();
		}
		retString += " (*";
		return retString;
	}

	@Override
	protected String preStringForCopy() {
		String retString = this.returnType.preStringForCopy() + " ";
		return retString;
	}

	@Override
	protected String preString() {
		String retString = this.returnType.preString() + " ";
		return retString;
	}

	@Override
	protected String postDeclarationString() {
		String retString = ")(";
		for (Parameter param : parameterList) {
			retString += param.getParameterDeclaration();
			if (param != parameterList.get(parameterList.size() - 1)) {
				retString += ", ";
			}
		}
		retString += ")" + this.returnType.postString();
		return retString;
	}

	@Override
	protected String postString() {
		String retString = "(";
		for (Parameter param : parameterList) {
			retString += param.getParameterDeclaration();
			if (param != parameterList.get(parameterList.size() - 1)) {
				retString += ", ";
			}
		}
		retString += ")" + this.returnType.postString();
		return retString;
	}

	@Override
	public Set<Type> getAllTypes() {
		Set<Type> typeSet = new HashSet<>();
		typeSet.addAll(returnType.getAllTypes());
		if (this.parameterList != null) {
			for (Parameter param : this.parameterList) {
				typeSet.addAll(param.getParameterType().getAllTypes());
			}
		}
		return typeSet;
	}

	@Override
	public boolean isKnownConstantSized() {
		for (Parameter par : this.parameterList) {
			if (!par.getParameterType().isKnownConstantSized()) {
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
		if (t instanceof PointerType) {
			PointerType that = (PointerType) t;
			return that.getPointeeType().isCompatibleWith(this);
		} else if (t instanceof FunctionType) {
			FunctionType that = (FunctionType) t;
			if (!this.returnType.isCompatibleWith(that.returnType)) {
				return false;
			}
			if ((this.parameterList == null) != (that.parameterList == null)) {
				return false;
			} else if (this.parameterList != null) {
				if (this.parameterList.size() != that.parameterList.size()) {
					return false;
				}
				for (int i = 0; i < this.parameterList.size(); i++) {
					Parameter thisParam = this.parameterList.get(i);
					Parameter thatParam = that.parameterList.get(i);
					if (!thisParam.getParameterType().isCompatibleWith(thatParam.getParameterType())) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
}
