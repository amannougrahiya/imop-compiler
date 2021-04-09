/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.typeSystem;

import imop.ast.info.RootInfo;
import imop.ast.info.cfgNodeInfo.CompoundStatementInfo;
import imop.ast.info.cfgNodeInfo.DeclarationInfo;
import imop.ast.info.cfgNodeInfo.FunctionDefinitionInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.solver.BaseSyntax;
import imop.lib.getter.ExpressionTypeGetter;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.util.*;

/**
 * This is the superclass for all the various
 * Types supported by IMOP.
 * 
 * @author aman
 *
 */
public abstract class Type {

	public List<StorageClass> storageClasses;
	private static Set<Type> deletedTypes = new HashSet<>();

	public static void resetStaticFields() {
		Type.deletedTypes = new HashSet<>();
	}

	/**
	 * This method is overridden at appropriate subclasses
	 * to return true.
	 * 
	 * @return
	 */
	public boolean isComplete() {
		return true;
	}

	/**
	 * This method is overridden at appropriate subclasses
	 * to return true.
	 * 
	 * @return
	 */
	public boolean isCharacterType() {
		return false;
	}

	/**
	 * This method is overridden at appropriate subclasses
	 * to return true.
	 * 
	 * @return
	 */
	public boolean isBasicType() {
		return false;
	}

	/**
	 * This method is overridden at appropriate subclasses
	 * to return true.
	 * 
	 * @return
	 */
	public boolean isRealType() {
		return false;
	}

	public boolean isScalarType() {
		if (this instanceof ArithmeticType) {
			return true;
		} else if (this instanceof PointerType) {
			return true;
		}
		return false;
	}

	/**
	 * Note that unions are not aggregate types.
	 * 
	 * @return
	 */
	public boolean isAggregateType() {
		if (this instanceof ArrayType) {
			return true;
		} else if (this instanceof StructType) {
			return true;
		}
		return false;
	}

	public abstract boolean isKnownConstantSized();
	// public boolean isKnownConstantSized() {
	// if (this.isComplete()) {
	// if (this instanceof ArrayType && ((ArrayType) this).isVariableLengthed()) {
	// return false;
	// } else {
	// return true;
	// }
	// } else {
	// return false;
	// }
	// }

	public boolean isDerivedDeclaratorType() {
		if (this instanceof ArrayType || this instanceof FunctionType || this instanceof PointerType) {
			return true;
		} else {
			return false;
		}
	}

	public Type getIntegerPromotedType() {
		if (this instanceof IntegerType) {
			IntegerType integerType = (IntegerType) this;
			if (integerType.getIntegerConversionRank() <= SignedIntType.type().getIntegerConversionRank()) {
				// TODO: Ensure that later when we know the size of various types,
				// we should return the more appropriate type of int -- unsigned or signed.
				// Till then, we send SignedIntType for all.
				return SignedIntType.type();
			}
		}
		return this;
	}

	/**
	 * Returns the type domain of the types.
	 * Needs to be overridden by the complex types.
	 * 
	 * @return
	 */
	public TypeDomain getTypeDomain() {
		return TypeDomain.REAL;
	}

	/**
	 * Returns the corresponding real type for the current object.
	 * Should be overridden by complex types.
	 * 
	 * @return
	 */
	public Type getRealType() {
		return this;
	}

	/**
	 * Returns the corresponding complex type for the current object.
	 * Should be overridden by those real types which have a complex
	 * counterpart.
	 * 
	 * @return
	 */
	public Type getComplexType() {
		return this;
	}

	public boolean hasConstQualifier() {
		// TODO: Code here.
		return false;
	}

	protected abstract String preDeclarationString();

	// protected String getDeclarationPostString(String tempName, boolean
	// isFullDeclarator) {
	// String retString = " " + tempName + " ";
	// return retString;
	// }

	protected abstract String postDeclarationString();
	// protected String postDeclarationString(String tempName) {
	// return this.getDeclarationPostString(tempName, true);
	// }

	/**
	 * Returns a declaration for <code>tempName</code> with this type,
	 * such that {@code tempName} can be used to hold a value of any object of
	 * this type.
	 * 
	 * @param tempName
	 * @return
	 */
	public Declaration getDeclaration(String tempName) {
		Declaration decl = FrontEnd.parseAlone(
				this.preDeclarationString() + " " + tempName + " " + this.postDeclarationString() + ";",
				Declaration.class);
		return decl;
	}

	/**
	 * Returns a declaration for <code>tempName</code> with this type,
	 * without any pointer conversions. Do not use this method if you are
	 * creating temporaries that should be able to hold the value of any object
	 * of this type.
	 * 
	 * @param tempName
	 * @return
	 */
	public Declaration getExactDeclarationWithoutInit(String tempName) {
		Declaration decl = FrontEnd.parseAlone(this.preString() + " " + tempName + " " + this.postString() + ";",
				Declaration.class);
		return decl;
	}

	public abstract boolean isCompatibleWith(Type t);

	protected abstract String preString();

	protected abstract String postString();

	protected String preStringForCopy() {
		return this.preString();
	}

	protected String postStringForCopy() {
		return this.postString();
	}

	// protected String postDeclarationString(String tempName) {
	// return this.getDeclarationPostString(tempName, true);
	// }

	// protected String postDeclarationString(String tempName) {
	// return this.getDeclarationPostString(tempName, true);
	// }

	/**
	 * Returns a string of an abstract declaration of this type.
	 * 
	 * @return
	 */
	@Override
	public final String toString() {
		return this.preString() + " " + this.postString();
	}

	public final String toString(String idName) {
		return this.preString() + " " + idName + " " + this.postString();
	}

	public static FunctionType getTypeTree(FunctionDefinition funcDef, Scopeable scope) {
		// Ensure that all functions with unspecified return type have been
		// pre-processed to return int.
		if (!funcDef.getF0().present()) {
			Misc.exitDueToError("Could not find the return type (int) of : " + funcDef.getInfo().getFunctionName());
		}
		return (FunctionType) Type.getTypeTree((DeclarationSpecifiers) funcDef.getF0().getNode(), funcDef.getF1(),
				scope);
	}

	public static List<Type> getTypeTree(Declaration declaration, Scopeable scope, boolean deleteUserDefinedTypes) {
		// if (Misc.isTypedef(declaration)) {
		// return null;
		// }
		List<String> ids = declaration.getInfo().getIDNameList();
		List<Type> typeList = new ArrayList<>();
		for (String name : ids) {
			Type tempType = getTypeTree(declaration, name, scope, deleteUserDefinedTypes);
			if (tempType != null) {
				typeList.add(tempType);
			}
		}
		return typeList;
	}

	public static List<Type> getTypeTree(Declaration declaration, Scopeable scope) {
		// if (Misc.isTypedef(declaration)) {
		// return null;
		// }
		List<String> ids = declaration.getInfo().getIDNameList();
		List<Type> typeList = new ArrayList<>();
		if (ids.isEmpty()) {
			getTypeTree(declaration.getF0(), scope);
		}
		for (String name : ids) {
			Type tempType = getTypeTree(declaration, name, scope);
			if (tempType != null) {
				typeList.add(tempType);
			}
		}
		return typeList;
	}

	public static List<Type> getTypeTree(ParameterTypeList paramTypeList, Scopeable scope) {
		ParameterList paramList = paramTypeList.getF0();
		List<Type> typeList = new ArrayList<>();
		typeList.add(getTypeTree(paramList.getF0(), scope));
		for (Node nodeSeqNode : paramList.getF1().getNodes()) {
			NodeSequence nodeSeq = (NodeSequence) nodeSeqNode;
			typeList.add(getTypeTree((ParameterDeclaration) nodeSeq.getNodes().get(1), scope));
		}
		return typeList;
	}

	public static Type getTypeTree(Declaration declaration, String name, Scopeable scope,
			boolean deleteUserDefinedTypes) {
		// if (Misc.isTypedef(declaration)) {
		// return null;
		// }
		Declarator declarator = declaration.getInfo().getDeclarator(name);
		return getTypeTree(declaration.getF0(), declarator, scope, deleteUserDefinedTypes);
	}

	public static Type getTypeTree(Declaration declaration, String name, Scopeable scope) {
		// if (Misc.isTypedef(declaration)) {
		// return null;
		// }
		Declarator declarator = declaration.getInfo().getDeclarator(name);
		return getTypeTree(declaration.getF0(), declarator, scope);
	}

	public static Type getTypeTree(Declaration declaration, String name, Type inType, Scopeable scope) {
		// if (Misc.isTypedef(declaration)) {
		// return null;
		// }
		Declarator declarator = declaration.getInfo().getDeclarator(name);
		return getTypeTree(declaration.getF0(), declarator, inType, scope);
	}

	public static List<Type> getTypeTree(StructDeclaration structDeclaration, Scopeable scope) {
		List<String> ids = new ArrayList<>(DeclarationInfo.getIdNameList(structDeclaration));
		List<Type> typeList = new ArrayList<>();
		for (String name : ids) {
			typeList.add(getTypeTree(structDeclaration, name, scope));
		}

		return typeList;
	}

	public static Type getTypeTree(StructDeclaration structDeclaration, String name, Scopeable scope) {
		Declarator declarator = DeclarationInfo.getDeclarator(structDeclaration, name);
		return getTypeTree(structDeclaration.getF0(), declarator, scope);
	}

	public static Type getTypeTree(ParameterDeclaration paramDecl, Scopeable scope) {
		Type paramType = getTypeTree(paramDecl.getF0(), paramDecl.getF1(), scope);
		if (paramType instanceof ArrayType) {
			ArrayType arrType = (ArrayType) paramType;
			paramType = new PointerType(arrType.getElementType());
		}
		// System.out.println(paramDecl + " is a " + paramType + " now.");
		return paramType;
	}

	public static Type getTypeTree(DeclarationSpecifiers declSpec, ParameterAbstraction paramAbs, Scopeable scope) {
		Type baseType;
		ArithmeticTypeKeyCollector arithmeticTypeGetter = new ArithmeticTypeKeyCollector(scope);
		declSpec.accept(arithmeticTypeGetter);
		baseType = Type.getTypeFromArithmeticKeys(arithmeticTypeGetter.keywords);

		TypeTreeGetter typeTreeGetter = new TypeTreeGetter(scope);
		if (baseType == null) {
			// Here, the declaration specifiers use a struct, union, enum or typedef.
			baseType = declSpec.accept(typeTreeGetter, baseType);
		}

		// System.out.println("Found a " + baseType + " for " +
		// declSpec.getInfo().getString());
		// TODO: Add qualifiers here.

		Type typeFromDeclarator = paramAbs.accept(typeTreeGetter, baseType);
		return typeFromDeclarator;
	}

	public static Type getTypeTree(DeclarationSpecifiers declSpec, Scopeable scope, boolean deleteUserSpecifiedTypes) {
		// if (Misc.isTypedef(declSpec)) {
		// return null;
		// }
		Type baseType;
		ArithmeticTypeKeyCollector arithmeticTypeGetter = new ArithmeticTypeKeyCollector(scope);
		declSpec.accept(arithmeticTypeGetter);
		baseType = Type.getTypeFromArithmeticKeys(arithmeticTypeGetter.keywords);

		TypeTreeGetter typeTreeGetter = new TypeTreeGetter(scope, deleteUserSpecifiedTypes);
		if (baseType == null) {
			// Here, the declaration specifiers use a struct, union, enum or typedef.
			baseType = declSpec.accept(typeTreeGetter, baseType);
		}
		return baseType;
	}

	public static Type getTypeTree(DeclarationSpecifiers declSpec, Scopeable scope) {
		// if (Misc.isTypedef(declSpec)) {
		// return null;
		// }
		Type baseType;
		ArithmeticTypeKeyCollector arithmeticTypeGetter = new ArithmeticTypeKeyCollector(scope);
		declSpec.accept(arithmeticTypeGetter);
		baseType = Type.getTypeFromArithmeticKeys(arithmeticTypeGetter.keywords);

		if (baseType == null) {
			// Here, the declaration specifiers use a struct, union, enum or typedef.
			TypeTreeGetter typeTreeGetter = new TypeTreeGetter(scope);
			baseType = declSpec.accept(typeTreeGetter, baseType);
		}
		return baseType;
	}

	public static Type getTypeTree(DeclarationSpecifiers declSpec, Declarator declarator, Scopeable scope,
			boolean deleteUserDefinedType) {
		// if (Misc.isTypedef(declSpec)) {
		// return null;
		// }
		Type baseType;
		ArithmeticTypeKeyCollector arithmeticTypeGetter = new ArithmeticTypeKeyCollector(scope);
		declSpec.accept(arithmeticTypeGetter);
		baseType = Type.getTypeFromArithmeticKeys(arithmeticTypeGetter.keywords);

		TypeTreeGetter typeTreeGetter = new TypeTreeGetter(scope, deleteUserDefinedType);
		if (baseType == null) {
			// Here, the declaration specifiers use a struct, union, enum or typedef.
			baseType = declSpec.accept(typeTreeGetter, baseType);
		}

		// TODO: Add qualifiers here.

		Type typeFromDeclarator = declarator.accept(typeTreeGetter, baseType);
		return typeFromDeclarator;
	}

	public static Type getTypeTree(DeclarationSpecifiers declSpec, Declarator declarator, Scopeable scope) {
		// if (Misc.isTypedef(declSpec)) {
		// return null;
		// }
		Type baseType;
		ArithmeticTypeKeyCollector arithmeticTypeGetter = new ArithmeticTypeKeyCollector(scope);
		declSpec.accept(arithmeticTypeGetter);
		baseType = Type.getTypeFromArithmeticKeys(arithmeticTypeGetter.keywords);

		TypeTreeGetter typeTreeGetter = new TypeTreeGetter(scope);
		if (baseType == null) {
			// Here, the declaration specifiers use a struct, union, enum or typedef.
			baseType = declSpec.accept(typeTreeGetter, baseType);
		}

		// TODO: Add qualifiers here.

		Type typeFromDeclarator = declarator.accept(typeTreeGetter, baseType);
		return typeFromDeclarator;
	}

	public static Type getTypeTree(SpecifierQualifierList specQualList, Declarator declarator, Scopeable scope) {
		Type baseType;
		ArithmeticTypeKeyCollector arithmeticTypeGetter = new ArithmeticTypeKeyCollector(scope);
		specQualList.accept(arithmeticTypeGetter);
		baseType = Type.getTypeFromArithmeticKeys(arithmeticTypeGetter.keywords);

		TypeTreeGetter typeTreeGetter = new TypeTreeGetter(scope);
		if (baseType == null) {
			// Here, the specifier-qualifier list uses a struct, union, enum or typedef.
			baseType = specQualList.accept(typeTreeGetter, baseType);
		}

		// TODO: Add qualifiers here.

		Type typeFromDeclarator = declarator.accept(typeTreeGetter, baseType);
		return typeFromDeclarator;
	}

	public static Type getTypeTree(DeclarationSpecifiers declSpec, Declarator declarator, Type inType,
			Scopeable scope) {
		// if (Misc.isTypedef(declSpec)) {
		// return null;
		// }
		Type baseType;
		ArithmeticTypeKeyCollector arithmeticTypeGetter = new ArithmeticTypeKeyCollector(scope);
		declSpec.accept(arithmeticTypeGetter);
		baseType = Type.getTypeFromArithmeticKeys(arithmeticTypeGetter.keywords);

		TypeTreeGetter typeTreeGetter = new TypeTreeGetter(scope);
		if (baseType == null) {
			// Here, the declaration specifiers use a struct, union, enum or typedef.
			baseType = declSpec.accept(typeTreeGetter, inType);
		}

		// TODO: Add qualifiers here.

		Type typeFromDeclarator = declarator.accept(typeTreeGetter, baseType);
		return typeFromDeclarator;
	}

	public static Type getTypeTree(SpecifierQualifierList specQualList, Scopeable scope) {
		Type baseType;
		ArithmeticTypeKeyCollector arithmeticTypeGetter = new ArithmeticTypeKeyCollector(scope);
		specQualList.accept(arithmeticTypeGetter);
		baseType = Type.getTypeFromArithmeticKeys(arithmeticTypeGetter.keywords);

		TypeTreeGetter typeTreeGetter = new TypeTreeGetter(scope);
		if (baseType == null) {
			// Here, the specifier-qualifier list uses a struct, union, enum or typedef.
			baseType = specQualList.accept(typeTreeGetter, baseType);
		}
		return baseType;
	}

	public static Type getTypeTree(SpecifierQualifierList specQualList, StructDeclarator structDecl, Scopeable scope) {
		Type baseType = Type.getTypeTree(specQualList, scope);
		TypeTreeGetter typeTreeGetter = new TypeTreeGetter(scope);
		return structDecl.accept(typeTreeGetter, baseType);
	}

	public static Type getTypeTree(SpecifierQualifierList specQualList, AbstractDeclarator absDecl, Scopeable scope) {
		Type baseType = Type.getTypeTree(specQualList, scope);
		TypeTreeGetter typeTreeGetter = new TypeTreeGetter(scope);
		return absDecl.accept(typeTreeGetter, baseType);
	}

	public static Type getTypeTree(TypeName typeName, Scopeable scope) {
		if (!typeName.getF1().present()) {
			return Type.getTypeTree(typeName.getF0(), scope);
		} else {
			AbstractDeclarator absDecl = (AbstractDeclarator) typeName.getF1().getNode();
			return Type.getTypeTree(typeName.getF0(), absDecl, scope);
		}
	}

	public static Type getTypeFromArithmeticKeys(List<ArithmeticTypeKey> arithTypeKeyList) {
		// Void
		if (arithTypeKeyList.contains(ArithmeticTypeKey.VOID)) {
			return VoidType.type();
		}

		// Characters
		if (arithTypeKeyList.contains(ArithmeticTypeKey.CHAR)) {
			if (arithTypeKeyList.contains(ArithmeticTypeKey.SIGNED)) {
				return SignedCharType.type();
			} else if (arithTypeKeyList.contains(ArithmeticTypeKey.UNSIGNED)) {
				return UnsignedCharType.type();

			} else {
				return CharType.type();
			}
		}

		// Short
		if (arithTypeKeyList.contains(ArithmeticTypeKey.SHORT)) {
			if (arithTypeKeyList.contains(ArithmeticTypeKey.UNSIGNED)) {
				return UnsignedShortIntType.type();
			} else {
				return SignedShortIntType.type();
			}
		}

		// Long's
		if (arithTypeKeyList.contains(ArithmeticTypeKey.LONG)) {
			arithTypeKeyList.remove(ArithmeticTypeKey.LONG);
			if (arithTypeKeyList.contains(ArithmeticTypeKey.LONG)) {
				arithTypeKeyList.add(ArithmeticTypeKey.LONG);
				// Long Long
				if (arithTypeKeyList.contains(ArithmeticTypeKey.UNSIGNED)) {
					return UnsignedLongLongIntType.type();
					// return UnsignedLongIntType.type(); // Error.
				} else {
					return SignedLongLongIntType.type();
					// return SignedLongIntType.type(); // Error.
				}
			} else {
				arithTypeKeyList.add(ArithmeticTypeKey.LONG);
				// Long Double and Long Double Complex
				if (arithTypeKeyList.contains(ArithmeticTypeKey.DOUBLE)) {
					if (arithTypeKeyList.contains(ArithmeticTypeKey._COMPLEX)) {
						return LongDoubleComplexType.type();
					} else {
						return LongDoubleType.type();
					}
				}

				// Long
				if (arithTypeKeyList.contains(ArithmeticTypeKey.UNSIGNED)) {
					return UnsignedLongIntType.type();
				} else {
					return SignedLongIntType.type();
				}
			}
		}

		// Int's
		if (arithTypeKeyList.contains(ArithmeticTypeKey.INT)) {
			if (arithTypeKeyList.contains(ArithmeticTypeKey.UNSIGNED)) {
				return UnsignedIntType.type();
			} else {
				return SignedIntType.type();
			}
		}
		if (arithTypeKeyList.contains(ArithmeticTypeKey.UNSIGNED)) {
			return UnsignedIntType.type();
		}

		// _Bool
		if (arithTypeKeyList.contains(ArithmeticTypeKey._BOOL)) {
			return _BoolType.type();
		}

		// Float's
		if (arithTypeKeyList.contains(ArithmeticTypeKey.FLOAT)) {
			if (arithTypeKeyList.contains(ArithmeticTypeKey._COMPLEX)) {
				return FloatComplexType.type();
			} else {
				return FloatType.type();
			}
		}

		// Double's
		if (arithTypeKeyList.contains(ArithmeticTypeKey.DOUBLE)) {
			if (arithTypeKeyList.contains(ArithmeticTypeKey._COMPLEX)) {
				return DoubleComplexType.type();
			} else {
				return DoubleType.type();
			}
		}

		return null;
	}

	public static Type getType(NodeToken nodeToken) {
		Cell cell = Misc.getSymbolOrFreeEntry(nodeToken);
		if (cell == null) {
			return null;
		}
		if (cell instanceof Symbol) {
			return ((Symbol) cell).getType();
		} else {
			return null;
		}
	}

	/**************************************************************
	 * Following are some of the methods that should go into the Expression
	 * subclass of the Expression tree when we create it.
	 */

	/**
	 * Returns the type of the expression <code>exp</code>.
	 * 
	 * @param exp
	 * @return
	 *         type of <code>exp</code>
	 */
	public static Type getType(Expression exp) {
		ExpressionTypeGetter typeGetter = new ExpressionTypeGetter();
		Type retType = exp.accept(typeGetter);
		return retType;
	}

	/**
	 * Obtain the type of the given base syntax expression.
	 * 
	 * @param baseSyntax
	 * @return
	 */
	public static Type getType(BaseSyntax baseSyntax) {
		if (baseSyntax.primExp == null) {
			return null;
		}
		Type primType = Type.getType(baseSyntax.primExp);
		if (baseSyntax.postFixOps == null || baseSyntax.postFixOps.isEmpty()) {
			return primType;
		}
		int derefCount = baseSyntax.postFixOps.size();
		while (derefCount != 0) {
			if (primType instanceof PointerType) {
				PointerType ptrType = (PointerType) primType;
				primType = ptrType.getPointeeType();
			} else if (primType instanceof ArrayType) {
				ArrayType arrType = (ArrayType) primType;
				primType = arrType.getElementType();
			} else {
				assert (false) : "Incorrect type obtained for " + baseSyntax;
			}
			derefCount--;
		}
		return primType;
	}

	/**
	 * Returns the Type entry which defines the type named "tag",
	 * and is present in either an enclosing struct/union, or in an
	 * enclosing CompoundStatement, FunctionDefinition or TranslationUnit.
	 * 
	 * @param name
	 * @param scope
	 * @return
	 */
	public static Type getTypeEntry(String tag, Scopeable scope) {
		if (scope instanceof StructType || scope instanceof UnionType) {
			if (scope instanceof StructType) {
				StructType structType = (StructType) scope;
				if (structType.getTypeTable().containsKey(tag)) {
					return structType.getTypeTable().get(tag);
				}
				return Type.getTypeEntry(tag, structType.getDefiningScope());
			} else {
				UnionType unionType = (UnionType) scope;
				if (unionType.getTypeTable().containsKey(tag)) {
					return unionType.getTypeTable().get(tag);
				}
				return Type.getTypeEntry(tag, unionType.getDefiningScope());
			}

		} else if (scope instanceof Node) {
			return Type.getTypeEntry(tag, (Node) scope);
		}
		return null;
	}

	/**
	 * Returns reference to the Type object that represents a struct,
	 * union or an enum with the specified <code>tag</code> as visible in the
	 * node <code>node</code>.
	 * 
	 * @param tag
	 * @param node
	 * @return
	 */
	private static Type getTypeEntry(String tag, Node node) {
		if (node == null) {
			return null;
		}

		Node scopeChoice = node;
		while (scopeChoice != null) {
			if (scopeChoice instanceof TranslationUnit) {
				HashMap<String, Type> typeTable = ((RootInfo) scopeChoice.getInfo()).getTypeTable();
				if (typeTable.containsKey(tag)) {
					return typeTable.get(tag);
				}
			} else if (scopeChoice instanceof CompoundStatement) {
				HashMap<String, Type> typeTable = ((CompoundStatementInfo) scopeChoice.getInfo()).getTypeTable();
				if (typeTable.containsKey(tag)) {
					return typeTable.get(tag);
				}
			} else if (scopeChoice instanceof FunctionDefinition) {
				HashMap<String, Type> typeTable = ((FunctionDefinitionInfo) scopeChoice.getInfo()).getTypeTable();
				if (typeTable.containsKey(tag)) {
					return typeTable.get(tag);
				}
			}
			scopeChoice = (Node) Misc.getEnclosingBlock(scopeChoice);
		}
		// System.out.println("Could not find the type " + tag + " in " +
		// node.getInfo().getString() + " at line #" + Misc.getLineNum(node));
		return null;
	}

	/**
	 * Returns the type with specified tag, if present in the given scope,
	 * or any of the nested scope.
	 * 
	 * @param tag
	 * @param scope
	 * @return
	 */
	public static Type getTypeFromScope(String tag, Scopeable scope) {
		return Type.getTypeEntry(tag, scope);
	}

	/**
	 * Checks whether the given {@link CastExpressionTyped} node performs
	 * an incompatible type cast between pointers/arrays.
	 * <br>
	 * Note that this method <i>ignores</i> type casts that are done on type
	 * {@code (void *)}.
	 * (We cannot ignore type casts <i>to</i> {@code (void *)} as well, since
	 * these together can be then used to perform any desired incompatible type
	 * cast without getting caught by this method.)
	 * 
	 * @param castExp
	 *                a {@link CastExpressionTyped} expression to be tested.
	 * @return
	 *         true, if the given expression performs an incompatible type cast.
	 */
	public static boolean hasIncompatibleTypeCastOfPointers(CastExpressionTyped cet) {
		Scopeable scope = Misc.getEnclosingBlock(cet);
		if (scope == null) {
			return false;
		}
		Type castType = Type.getTypeTree(cet.getF1(), scope);
		Type expType = Type.getType(cet.getF3());
		if (!(castType instanceof ArrayType) && !(castType instanceof PointerType)) {
			return false;
		}
		if (!(expType instanceof ArrayType) && !(expType instanceof PointerType)) {
			return false;
		}
		if (expType instanceof PointerType) {
			PointerType pType = (PointerType) expType;
			if (pType.getPointeeType() == VoidType.type()) {
				return false;
			}
		}
		if (!castType.toString().equals(expType.toString())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Enters the provided tag and type key-value pair into the
	 * typeTable of the specified scope, if the type is not already
	 * present in the complete state.
	 * 
	 * @param scope
	 * @param tag
	 * @param newType
	 * @return
	 *         type which is finally present in the typeTable of scope, for the
	 *         given tag.
	 */
	public static Type putTypeToScope(Scopeable scope, String tag, Type newType) {
		assert (newType instanceof StructType || newType instanceof UnionType || newType instanceof EnumType);
		HashMap<String, Type> typeTable = scope.getTypeTable();

		// Future code. Can be thought about later.
		// if (!deletedTypes.isEmpty()) {
		// System.out.println(deletedTypes + " still " + tag + ".");
		// }
		//

		Type oldType = typeTable.get(tag);
		if (oldType == null) {
			typeTable.put(tag, newType);
			return newType;
		} else if (!oldType.isComplete()) {
			typeTable.put(tag, newType);
			return newType;
		} else {
			if (!newType.isComplete()) {
				return oldType;
			} else {
				if (newType instanceof StructType) {
					StructType newStructType = (StructType) newType;
					StructType oldStructType = (StructType) oldType;
					if (newStructType.getDeclaringNode() == oldStructType.getDeclaringNode()) {
						return oldStructType;
					}
				} else if (newType instanceof UnionType) {
					UnionType newUnionType = (UnionType) newType;
					UnionType oldUnionType = (UnionType) oldType;
					if (newUnionType.getDeclaringNode() == oldUnionType.getDeclaringNode()) {
						return oldUnionType;
					}
				} else if (newType instanceof EnumType) {
					EnumType newEnumType = (EnumType) newType;
					EnumType oldEnumType = (EnumType) oldType;
					if (newEnumType.getDeclaringNode() == oldEnumType.getDeclaringNode()) {
						return oldEnumType;
					}
				}
				Misc.warnDueToLackOfFeature(
						"Attempting to redefine a complete type. Please look into the declarations of type " + newType,
						Program.getRoot());
				// assert (false);
				// typeTable.put(tag, newType);
				return oldType;
			}
		}
	}

	public static Type removeTypeFromScope(Scopeable scope, String tag, Type type) {
		assert (type instanceof StructType || type instanceof UnionType || type instanceof EnumType);
		HashMap<String, Type> typeTable = scope.getTypeTable();
		Type oldType = typeTable.get(tag);
		if (oldType != null) {
			Type.deletedTypes.add(oldType);
		}
		typeTable.remove(tag);
		return type;
	}

	/**
	 * Obtain a set of all those types which have been used somewhere within the
	 * declaration of the receiver type.
	 * This set would contain the receiver type as well.
	 * 
	 * @return
	 *         set of all those types which have been used somewhere within the
	 *         declaration of the receiver type.
	 */
	public abstract Set<Type> getAllTypes();
}
