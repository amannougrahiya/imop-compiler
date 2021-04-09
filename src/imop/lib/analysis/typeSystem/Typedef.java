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
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.util.HashMap;

public class Typedef {
	private Type type; // Type that is represented by a typedef.
	private String tag; // Typedef name.
	private Declaration definingNode; // Refers to the declaration that defines this typedef.

	/**
	 * Defines where does this typedef definition occur.
	 * This can be CompounStatement, TranslationUnit, StructType, or UnionType.
	 */
	private Scopeable definingScope;

	public Typedef(String tag, Type type, Declaration definingNode, Scopeable definingScope) {
		this.type = type;
		this.tag = tag;
		this.definingNode = definingNode;
		this.setDefiningScope(definingScope);
	}

	public String getTypedefName() {
		return tag;
	}

	public Type getType() {
		return type;
	}

	public Declaration getDefiningNode() {
		assert (definingNode != null);
		// TODO: Replace the if-statement below with a better code.
		if (definingNode == null) {
			definingNode = FrontEnd.parseAlone(tag + " something " + ";", Declaration.class);
		}
		return definingNode;
	}

	// /**
	// * Returns the typedef with specified tag, if present in the given scope,
	// * or any of the nested scope.
	// * @param tag
	// * @param scope
	// * @return
	// */
	// public static Typedef getTypedefFromScope(String tag, Scopeable scope) {
	// return Typedef.getTypedefEntry(tag, scope);
	// }
	//
	// /**
	// * Enters the provided tag and typedef key-value pair into the
	// * typedefTable of the specified scope, if the typedef is not already
	// * present in the complete state.
	// * @param scope
	// * @param tag
	// * @param typedef
	// */
	// public static void putTypedefToScope(Scopeable scope, String tag, Typedef
	// typedef) {
	// HashMap<String, Typedef> typedefTable;
	// if (scope instanceof CompoundStatement) {
	// CompoundStatementInfo csInfo = (CompoundStatementInfo) ((CompoundStatement)
	// scope).getInfo();
	// typedefTable = csInfo.getTypedefTable();
	// } else if (scope instanceof TranslationUnit) {
	// RootInfo rootInfo = (RootInfo) ((TranslationUnit) scope).getInfo();
	// typedefTable = rootInfo.getTypedefTable();
	// } else if (scope instanceof FunctionDefinition) {
	// typedefTable = null;
	// assert(false);
	// } else if (scope instanceof StructType) {
	// StructType structTypedef = (StructType) scope;
	// typedefTable = structTypedef.getTypedefTable();
	// } else if (scope instanceof UnionType) {
	// UnionType unionTypedef = (UnionType) scope;
	// typedefTable = unionTypedef.getTypedefTable();
	// } else {
	// typedefTable = null;
	// assert(false);
	// }
	// typedefTable.put(tag, typedef);
	// }

	/**
	 * @return Typedef defining a typedef for the given TypedefName.
	 *         Returns null, if typedef not found in any of the outer nesting
	 *         AST scopes.
	 */
	public static Typedef getTypedefEntry(TypedefName typedefName, Scopeable scope) {
		if (typedefName.getF0().getTokenImage().equals("__builtin_va_list")) {
			return new Typedef("__builtin_va_list", SignedIntType.type(), null, Program.getRoot());
		}
		if (typedefName.getF0().getTokenImage().equals("bool")) {
			return new Typedef("bool", SignedIntType.type(), null, Program.getRoot());
		}
		if (typedefName.getF0().getTokenImage().equals("_Bool")) {
			return new Typedef("_Bool", SignedIntType.type(), null, Program.getRoot());
		}
		return Typedef.getTypedefEntry(typedefName.getF0().getTokenImage(), scope);
	}

	/**
	 * Returns the Typedef entry which defines the typedef named "tag",
	 * and is present in either an enclosing struct/union, or in an
	 * enclosing CompoundStatement or TranslationUnit.
	 * 
	 * @param name
	 * @param scope
	 * @return
	 */
	public static Typedef getTypedefEntry(String tag, Scopeable scope) {
		if (tag.equals("__builtin_va_list")) {
			return new Typedef("__builtin_va_list", SignedIntType.type(), null, Program.getRoot());
		}
		if (tag.equals("bool")) {
			return new Typedef("bool", SignedIntType.type(), null, Program.getRoot());
		}
		if (tag.equals("_Bool")) {
			return new Typedef("_Bool", SignedIntType.type(), null, Program.getRoot());
		}

		if (scope instanceof StructType || scope instanceof UnionType) {
			if (scope instanceof StructType) {
				StructType structType = (StructType) scope;
				if (structType.getTypedefTable().containsKey(tag)) {
					return structType.getTypedefTable().get(tag);
				}
				return Typedef.getTypedefEntry(tag, structType.getDefiningScope());
			} else {
				UnionType unionType = (UnionType) scope;
				if (unionType.getTypedefTable().containsKey(tag)) {
					return unionType.getTypedefTable().get(tag);
				}
				return Typedef.getTypedefEntry(tag, unionType.getDefiningScope());
			}

		} else if (scope instanceof Node) {
			return Typedef.getTypedefEntry(tag, (Node) scope);
		}
		return null;
	}

	private static Typedef getTypedefEntry(String name, Node node) {
		if (node == null) {
			return null;
		}

		Node scopeChoice = node;
		while (scopeChoice != null) {
			if (scopeChoice instanceof TranslationUnit) {
				HashMap<String, Typedef> typedefTable = ((RootInfo) scopeChoice.getInfo()).getTypedefTable();
				if (typedefTable.containsKey(name)) {
					return typedefTable.get(name);
				}
			} else if (scopeChoice instanceof FunctionDefinition) {
				// Do nothing.
			} else if (scopeChoice instanceof CompoundStatement) {
				HashMap<String, Typedef> typedefTable = ((CompoundStatementInfo) scopeChoice.getInfo())
						.getTypedefTable();
				if (typedefTable.containsKey(name)) {
					return typedefTable.get(name);
				}
			}
			scopeChoice = (Node) Misc.getEnclosingBlock(scopeChoice);
		}
		/*
		 * In case if node is disconnected from the AST, we should look into at
		 * least the TranslationUnit of the input.
		 */

		HashMap<String, Typedef> typedefTable = Program.getRoot().getInfo().getTypedefTable();
		if (typedefTable.containsKey(name)) {
			return typedefTable.get(name);
		}

		/*
		 * Search in the built-in libraries now.
		 */

		typedefTable = FrontEnd.getMacBuiltins().getInfo().getTypedefTable();
		if (typedefTable.containsKey(name)) {
			return typedefTable.get(name);
		}

		typedefTable = FrontEnd.getK2Builtins().getInfo().getTypedefTable();
		if (typedefTable.containsKey(name)) {
			return typedefTable.get(name);
		}

		typedefTable = FrontEnd.getOtherBuiltins().getInfo().getTypedefTable();
		if (typedefTable.containsKey(name)) {
			return typedefTable.get(name);
		}

		return null;
	}

	public Scopeable getDefiningScope() {
		return definingScope;
	}

	public void setDefiningScope(Scopeable definingScope) {
		this.definingScope = definingScope;
	}
}
