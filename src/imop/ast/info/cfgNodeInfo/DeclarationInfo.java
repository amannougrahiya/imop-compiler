/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.NodeInfo;
import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class DeclarationInfo extends NodeInfo {

	public DeclarationInfo(Node owner) {
		super(owner);
	}

	private static enum STATES {
		UNKNOWN, YES, NO
	}

	private STATES stTypeDef = STATES.UNKNOWN;

	/**
	 * @return true if this declaration is a typedef
	 */
	public boolean isTypedef() {
		if (this.stTypeDef != STATES.UNKNOWN) {
			return (this.stTypeDef == STATES.YES) ? true : false;
		}
		for (StorageClassSpecifier stor : Misc.getInheritedEnclosee(((Declaration) this.getNode()).getF0(),
				StorageClassSpecifier.class)) {
			if (((NodeToken) stor.getF0().getChoice()).getTokenImage().equals("typedef")) {
				this.stTypeDef = STATES.YES;
				return true;
			}
		}
		this.stTypeDef = STATES.NO;
		return false;
	}

	public boolean hasInitializer() {
		Declaration declaration = (Declaration) this.getNode();
		List<String> idList = declaration.getInfo().getIDNameList();
		assert (idList.size() < 2);
		for (String name : idList) {
			Initializer init = declaration.getInfo().getInitializer(name);
			if (init == null) {
				return false;
			}
			break;
		}
		return true;

	}

	public Initializer getInitializer() {
		Declaration declaration = (Declaration) this.getNode();
		List<String> idList = declaration.getInfo().getIDNameList();
		assert (idList.size() < 2);
		for (String name : idList) {
			return declaration.getInfo().getInitializer(name);
		}
		return null;
	}

	/**
	 * @param name
	 *             A symbol name
	 * @return Declarator node which contains the symbol with name "name" in
	 *         this declaration.
	 */
	public Initializer getInitializer(String name) {
		Declaration decl = (Declaration) this.getNode();
		InitDeclaratorList initDeclList = (InitDeclaratorList) decl.getF1().getNode();
		if (initDeclList == null) {
			return null;
		}
		InitDeclarator initDecl = initDeclList.getF0();
		Declarator idDeclr = initDecl.getF0();
		if (getRootIdName(idDeclr).equals(name)) {
			if (initDecl.getF1().present()) {
				return (Initializer) ((NodeSequence) initDecl.getF1().getNode()).getNodes().get(1);
			}
		}
		for (Node seq : initDeclList.getF1().getNodes()) {
			assert seq instanceof NodeSequence;
			initDecl = (InitDeclarator) ((NodeSequence) seq).getNodes().get(1);
			idDeclr = initDecl.getF0();
			if (getRootIdName(idDeclr).equals(name)) {
				if (initDecl.getF1().present()) {
					return (Initializer) ((NodeSequence) initDecl.getF1().getNode()).getNodes().get(1);
				}
			}
		}
		return null;
	}

	/**
	 * @param name
	 *             A symbol name
	 * @return Declarator node which contains the symbol with name "name" in
	 *         decl
	 */
	public Declarator getDeclarator(String name) {
		Declaration decl = (Declaration) this.getNode();
		InitDeclaratorList initDeclList = (InitDeclaratorList) decl.getF1().getNode();
		if (initDeclList == null) {
			return null;
		}
		Declarator idDeclr = initDeclList.getF0().getF0();
		if (getRootIdName(idDeclr).equals(name)) {
			return idDeclr;
		}
		for (Node seq : initDeclList.getF1().getNodes()) {
			assert seq instanceof NodeSequence;
			idDeclr = ((InitDeclarator) ((NodeSequence) seq).getNodes().get(1)).getF0();
			if (getRootIdName(idDeclr).equals(name)) {
				return idDeclr;
			}
		}
		return null;
	}

	/**
	 * Check if any symbol with same name as this declaration is accessed in
	 * {@code other}.
	 * 
	 * @param other
	 * @return
	 */
	public boolean clashesSyntacticallyWith(Node other) {
		other = Misc.getCFGNodeFor(other);
		Declaration decl = (Declaration) this.getNode();
		List<String> idNames = decl.getInfo().getIDNameList();
		assert (idNames.size() == 1);
		String idName = idNames.get(0);

		for (Cell c : other.getInfo().getUsedCells()) {
			if (c instanceof Symbol) {
				Symbol sym = (Symbol) c;
				if (sym.getName().equals(idName)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns the first declared symbol, if any. Note that this declaration may
	 * that be of a type or typedef, instead of a symbol (variable).
	 * 
	 * @return
	 *         the first declared symbol, if any, else {@code null}.
	 */
	public Symbol getDeclaredSymbol() {
		Declaration declaration = (Declaration) this.getNode();
		List<String> idList = declaration.getInfo().getIDNameList();
		assert (idList.size() < 2) : "Found declaration of multiple declarators in the same line: " + declaration;
		for (String name : idList) {
			return Misc.getSymbolEntry(name, this.getNode());
		}
		return null;
	}

	/**
	 * Checks whether the declared symbol has a static storage class specifier.
	 * 
	 * @return
	 *         true, if this declaration declared a symbol with static storage
	 *         class specifier.
	 */
	public boolean isStatic() {
		Symbol sym = this.getDeclaredSymbol();
		if (sym == null) {
			return false;
		} else {
			return sym.isStatic();
		}
	}

	public String getDeclaredName() {
		Declaration declaration = (Declaration) this.getNode();
		List<String> idList = declaration.getInfo().getIDNameList();
		// assert (idList.size() < 2);
		for (String name : idList) {
			return name;
		}
		return null;

	}

	/**
	 * Checks whether this declaration declares a typedef, struct, union, or
	 * enum.
	 * 
	 * @return
	 *         <i>true</i> if this declaration declares a typedef, struct,
	 *         union, or enum; <i>false</i> otherwise.
	 */
	public boolean isNonVariableDeclaration() {
		if (this.isTypedef()) {
			return true;
		}
		if (this.getDeclaredName() == null) {
			return true;
		}
		return false;
	}

	public List<String> getIDNameList() {
		Declaration decl = (Declaration) this.getNode();
		List<String> list = new ArrayList<>();
		InitDeclaratorList initDeclList = (InitDeclaratorList) decl.getF1().getNode();
		if (initDeclList == null) {
			return list;
		}
		Declarator idDeclr = initDeclList.getF0().getF0();
		list.add(getRootIdName(idDeclr));
		for (Node seq : initDeclList.getF1().getNodes()) {
			assert seq instanceof NodeSequence;
			idDeclr = ((InitDeclarator) ((NodeSequence) seq).getNodes().get(1)).getF0();
			list.add(getRootIdName(idDeclr));
		}
		return list;
	}

	public StructDeclarationList getStructOrUnionDeclarationList() {
		Declaration decl = (Declaration) this.getNode();
		for (Node declSpecChoice : decl.getF0().getF0().getNodes()) {
			ADeclarationSpecifier declSpec = (ADeclarationSpecifier) declSpecChoice;
			if (declSpec.getF0().getChoice() instanceof TypeSpecifier) {
				TypeSpecifier typeSpec = (TypeSpecifier) declSpec.getF0().getChoice();
				if (typeSpec.getF0().getChoice() instanceof StructOrUnionSpecifier) {
					StructOrUnionSpecifier structOrUnionSpec = (StructOrUnionSpecifier) typeSpec.getF0().getChoice();
					if (structOrUnionSpec.getF0().getChoice() instanceof StructOrUnionSpecifierWithList) {
						StructOrUnionSpecifierWithList structOrUnionList = (StructOrUnionSpecifierWithList) structOrUnionSpec
								.getF0().getChoice();
						return structOrUnionList.getF3();
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}

	public EnumeratorList getEnumeratorList() {
		Declaration decl = (Declaration) this.getNode();
		for (Node declSpecChoice : decl.getF0().getF0().getNodes()) {
			ADeclarationSpecifier declSpec = (ADeclarationSpecifier) declSpecChoice;
			if (declSpec.getF0().getChoice() instanceof TypeSpecifier) {
				TypeSpecifier typeSpec = (TypeSpecifier) declSpec.getF0().getChoice();
				if (typeSpec.getF0().getChoice() instanceof EnumSpecifier) {
					EnumSpecifier enumSpec = (EnumSpecifier) typeSpec.getF0().getChoice();
					if (enumSpec.getF0().getChoice() instanceof EnumSpecifierWithList) {
						EnumSpecifierWithList enumSpecList = (EnumSpecifierWithList) enumSpec.getF0().getChoice();
						return enumSpecList.getF3();
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @return Returns the name of struct type "defined" (not just declared) in
	 *         this declaration, if any.
	 */
	public String getDefinedStructName() {
		Declaration decl = (Declaration) this.getNode();
		for (Node declSpecChoice : decl.getF0().getF0().getNodes()) {
			ADeclarationSpecifier declSpec = (ADeclarationSpecifier) declSpecChoice;
			if (declSpec.getF0().getChoice() instanceof TypeSpecifier) {
				TypeSpecifier typeSpec = (TypeSpecifier) declSpec.getF0().getChoice();
				if (typeSpec.getF0().getChoice() instanceof StructOrUnionSpecifier) {
					StructOrUnionSpecifier structOrUnionSpec = (StructOrUnionSpecifier) typeSpec.getF0().getChoice();
					if (structOrUnionSpec.getF0().getChoice() instanceof StructOrUnionSpecifierWithList) {
						StructOrUnionSpecifierWithList structOrUnionList = (StructOrUnionSpecifierWithList) structOrUnionSpec
								.getF0().getChoice();
						if (((NodeToken) structOrUnionList.getF0().getF0().getChoice()).getTokenImage()
								.equals("struct")) {
							if (structOrUnionList.getF1().getNode() == null) {
								// System.out.println("Found a query on an
								// unnamed Struct");
								return null;
							} else {
								return ((NodeToken) structOrUnionList.getF1().getNode()).getTokenImage();
							}
						} else {
							return null;
						}
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param decl
	 *             A Declaration Node
	 * @return Returns the name of union type "defined" (not just declared) in
	 *         this declaration, if any.
	 */
	public String getDefinedUnionName() {
		Declaration decl = (Declaration) this.getNode();
		for (Node declSpecChoice : decl.getF0().getF0().getNodes()) {
			ADeclarationSpecifier declSpec = (ADeclarationSpecifier) declSpecChoice;
			if (declSpec.getF0().getChoice() instanceof TypeSpecifier) {
				TypeSpecifier typeSpec = (TypeSpecifier) declSpec.getF0().getChoice();
				if (typeSpec.getF0().getChoice() instanceof StructOrUnionSpecifier) {
					StructOrUnionSpecifier structOrUnionSpec = (StructOrUnionSpecifier) typeSpec.getF0().getChoice();
					if (structOrUnionSpec.getF0().getChoice() instanceof StructOrUnionSpecifierWithList) {
						StructOrUnionSpecifierWithList structOrUnionList = (StructOrUnionSpecifierWithList) structOrUnionSpec
								.getF0().getChoice();
						if (((NodeToken) structOrUnionList.getF0().getF0().getChoice()).getTokenImage()
								.equals("union")) {
							if (structOrUnionList.getF1().getNode() == null) {
								// System.out.println("Found a query on an
								// unnamed Union");
								return null;
							} else {
								return ((NodeToken) structOrUnionList.getF1().getNode()).getTokenImage();
							}
						} else {
							return null;
						}
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param decl
	 *             A Declaration Node
	 * @return Returns the name of enumerator type "defined" (not just declared)
	 *         in this declaration, if any.
	 */
	public String getDefinedEnumName() {
		Declaration decl = (Declaration) this.getNode();
		for (Node declSpecChoice : decl.getF0().getF0().getNodes()) {
			ADeclarationSpecifier declSpec = (ADeclarationSpecifier) declSpecChoice;
			if (declSpec.getF0().getChoice() instanceof TypeSpecifier) {
				TypeSpecifier typeSpec = (TypeSpecifier) declSpec.getF0().getChoice();
				if (typeSpec.getF0().getChoice() instanceof EnumSpecifier) {
					EnumSpecifier enumSpec = (EnumSpecifier) typeSpec.getF0().getChoice();
					if (enumSpec.getF0().getChoice() instanceof EnumSpecifierWithList) {
						EnumSpecifierWithList enumSpecList = (EnumSpecifierWithList) enumSpec.getF0().getChoice();
						if (enumSpecList.getF1().getNode() == null) {
							// System.out.println("Found a query on an unnamed
							// Enumerator");
							return null;
						} else {
							return ((NodeToken) enumSpecList.getF1().getNode()).getTokenImage();
						}
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Given a struct-declarator, this method gives the name of the basic
	 * identifier in
	 * it (and not the parameter names).
	 * 
	 * @param decl
	 * @return
	 */
	public static String getRootIdName(StructDeclaratorWithDeclarator decl) {
		return getRootIdName(decl.getF0());
	}

	/**
	 * Given a declarator, this method gives the name of the basic identifier in
	 * it (and not the parameter names)
	 * 
	 * @param decl
	 * @return
	 */
	public static NodeToken getRootIdNodeToken(Declarator decl) {
		Node chosen = decl.getF1().getF0().getF0().getChoice();
		if (chosen instanceof NodeToken) {
			return ((NodeToken) chosen);
		}
		assert chosen instanceof NodeSequence;
		return getRootIdNodeToken(((Declarator) ((NodeSequence) chosen).getNodes().get(1)));
	}

	/**
	 * Given a declarator, this method gives the name of the basic identifier in
	 * it (and not the parameter names)
	 * 
	 * @param decl
	 * @return
	 */
	public static String getRootIdName(Declarator decl) {
		Node chosen = decl.getF1().getF0().getF0().getChoice();
		if (chosen instanceof NodeToken) {
			return ((NodeToken) chosen).getTokenImage();
		}
		assert chosen instanceof NodeSequence;
		return getRootIdName(((Declarator) ((NodeSequence) chosen).getNodes().get(1)));
	}

	/**
	 * 
	 * @param decl:
	 *              A StructDeclaration
	 * @return a string of idNames declared at this struct-declaration
	 */
	public static List<String> getIdNameList(StructDeclaration decl) {
		List<String> list = new ArrayList<>();
		StructDeclaratorList structDeclList = decl.getF1();

		StructDeclarator structDecl = structDeclList.getF0();
		if (structDecl.getF0().getChoice() instanceof StructDeclaratorWithDeclarator) {
			StructDeclaratorWithDeclarator strDeclWithDecl = (StructDeclaratorWithDeclarator) structDecl.getF0()
					.getChoice();
			list.add(DeclarationInfo.getRootIdName(strDeclWithDecl));
		}

		for (Node seq : structDeclList.getF1().getNodes()) {
			assert (seq instanceof NodeSequence);
			StructDeclarator strIn = (StructDeclarator) ((NodeSequence) seq).getNodes().get(1);
			if (strIn.getF0().getChoice() instanceof StructDeclaratorWithDeclarator) {
				list.add(getRootIdName((StructDeclaratorWithDeclarator) strIn.getF0().getChoice()));
			}
		}

		return list;
	}

	public static Declarator getDeclarator(StructDeclaration structDecl, String name) {
		StructDeclarator structDeclarator = DeclarationInfo.getStructDeclarator(structDecl, name);
		if (structDeclarator == null) {
			return null;
		}
		return ((StructDeclaratorWithDeclarator) structDeclarator.getF0().getChoice()).getF0();
	}

	public static StructDeclarator getStructDeclarator(StructDeclaration structDecl, String name) {
		StructDeclaratorList structDeclList = structDecl.getF1();
		StructDeclarator structDeclarator = structDeclList.getF0();
		Node choice = structDeclarator.getF0().getChoice();
		if (choice instanceof StructDeclaratorWithDeclarator) {
			if (DeclarationInfo.getRootIdName((StructDeclaratorWithDeclarator) choice).equals(name)) {
				return structDeclarator;
			}
		}
		for (Node nodeSeqNode : structDeclList.getF1().getNodes()) {
			NodeSequence nodeSeq = (NodeSequence) nodeSeqNode;
			structDeclarator = (StructDeclarator) nodeSeq.getNodes().get(1);
			choice = structDeclarator.getF0().getChoice();
			if (choice instanceof StructDeclaratorWithDeclarator) {
				if (DeclarationInfo.getRootIdName((StructDeclaratorWithDeclarator) choice).equals(name)) {
					return structDeclarator;
				}
			}
		}
		return null;
	}

}
