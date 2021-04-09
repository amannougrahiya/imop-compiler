/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.typeSystem;

import imop.ast.info.cfgNodeInfo.DeclarationInfo;
import imop.ast.info.cfgNodeInfo.ParameterDeclarationInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.GJDepthFirstProcess;
import imop.lib.analysis.typeSystem.EnumType.EnumMember;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

import java.util.ArrayList;
import java.util.List;

/**
 * Returns the root of the type-tree obtained by traversing the
 * DeclarationSpecifiers, ParameterAbstraction, and Declarator, as needed.
 * <p>
 * Note 1: This class may freely read/write to the associated scope's
 * typeTable.
 * <p>
 * Note 2: An scope can be a CompoundStatement, a FunctionDefinition,
 * a TranslationUnit, a StructType, or a UnionType.
 */
public class TypeTreeGetter extends GJDepthFirstProcess<Type, Type> {

	/**
	 * Refers to the immediately enclosing CompoundStatement,
	 * FunctionDefinition, TranslationUnit,
	 * StructType, or UnionType, in which the user-defined structs/unions/enums
	 * and typedefs
	 * should be visible, which might be needed to obtain the type information.
	 */
	private Scopeable definingScope;

	private boolean deleteUserDefinedTypes = false;

	public TypeTreeGetter(Scopeable definingScope) {
		this.definingScope = definingScope;
	}

	public TypeTreeGetter(Scopeable definingScope, boolean deleteUserDefinedTypes) {
		this.deleteUserDefinedTypes = deleteUserDefinedTypes;
		this.definingScope = definingScope;
	}

	@Override
	public Type visit(NodeList n, Type argu) {
		Type _ret = argu;
		for (Node e : n.getNodes()) {
			_ret = e.accept(this, argu);
		}
		return _ret;
	}

	@Override
	public Type visit(NodeListOptional n, Type argu) {
		Type _ret = argu;
		if (n.present()) {
			for (Node e : n.getNodes()) {
				_ret = e.accept(this, _ret);
			}
		}
		return _ret;
	}

	@Override
	public Type visit(NodeOptional n, Type argu) {
		Type _ret = argu;
		if (n.present()) {
			_ret = n.getNode().accept(this, _ret);
		}
		return _ret;
	}

	@Override
	public Type visit(NodeSequence n, Type argu) {
		Type _ret = argu;
		for (Node e : n.getNodes()) {
			_ret = e.accept(this, _ret);
		}
		return _ret;
	}

	@Override
	public Type visit(NodeChoice n, Type argu) {
		return n.getChoice().accept(this, argu);
	}

	@Override
	public Type visit(NodeToken n, Type argu) {
		return argu;
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public Type visit(Declaration n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= ( Declaration() )+
	 */
	@Override
	public Type visit(DeclarationList n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= ( ADeclarationSpecifier() )+
	 */
	@Override
	public Type visit(DeclarationSpecifiers n, Type argu) {
		Type retType = argu;

		Type baseType;
		ArithmeticTypeKeyCollector arithmeticTypeGetter = new ArithmeticTypeKeyCollector(definingScope);
		n.accept(arithmeticTypeGetter);
		baseType = Type.getTypeFromArithmeticKeys(arithmeticTypeGetter.keywords);

		if (baseType == null) {
			for (Node node : n.getF0().getNodes()) {
				ADeclarationSpecifier aDeclSpec = (ADeclarationSpecifier) node;
				retType = aDeclSpec.accept(this, retType);
			}
		} else {
			retType = baseType;
		}
		return retType;
	}

	/**
	 * f0 ::= StorageClassSpecifier()
	 * | TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public Type visit(ADeclarationSpecifier n, Type argu) {
		return n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= <AUTO>
	 * | <REGISTER>
	 * | <STATIC>
	 * | <EXTERN>
	 * | <TYPEDEF>
	 */
	@Override
	public Type visit(StorageClassSpecifier n, Type argu) {
		return argu;
	}

	/**
	 * f0 ::= ( <VOID> | <CHAR> | <SHORT> | <INT> | <LONG> | <FLOAT> | <DOUBLE>
	 * |
	 * <SIGNED> | <UNSIGNED> | StructOrUnionSpecifier() | EnumSpecifier() |
	 * TypedefName() )
	 */
	@Override
	public Type visit(TypeSpecifier n, Type argu) {
		Node choice = n.getF0().getChoice();
		assert (!(choice instanceof NodeToken));
		return choice.accept(this, argu);
	}

	/**
	 * f0 ::= <RESTRICT>
	 * | <CONST>
	 * | <VOLATILE>
	 * | <INLINE>
	 * | <CCONST>
	 * | <CINLINED>
	 * | <CINLINED2>
	 * | <CSIGNED>
	 * | <CSIGNED2>
	 */
	@Override
	public Type visit(TypeQualifier n, Type argu) {
		return argu;
	}

	/**
	 * 
	 * f0 ::= ( StructOrUnionSpecifierWithList() |
	 * StructOrUnionSpecifierWithId()
	 * )
	 */
	@Override
	public Type visit(StructOrUnionSpecifier n, Type argu) {
		return n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= StructOrUnion()
	 * <br>
	 * f1 ::= ( <IDENTIFIER> )?
	 * <br>
	 * f2 ::= "{"
	 * <br>
	 * f3 ::= StructDeclarationList()
	 * <br>
	 * f4 ::= "}"
	 * <br>
	 * If we encounter this visit, then a corresponding struct/union type
	 * must be created and inserted in the typeTable of the definingScope.
	 * If the type already exists, we override the type.
	 * 
	 * @return
	 *         the created struct/union type, that is implicitly added to the
	 *         typeTable of the definingScope.
	 */
	@Override
	public Type visit(StructOrUnionSpecifierWithList n, Type argu) {
		/*
		 * Step 1: Create and insert the incomplete type into the typeTable of
		 * the definingScope.
		 */
		Type retType = null;
		String typeStr = ((NodeToken) n.getF0().getF0().getChoice()).getTokenImage().equals("struct") ? "struct"
				: "union";
		String tag = null;
		if (n.getF1().present()) {
			tag = ((NodeToken) n.getF1().getNode()).getTokenImage();
		}
		assert (tag != null); // In a pre-pass, we have given names to all the unnamed structures, and unions.

		Declaration decl = Misc.getEnclosingNode(n, Declaration.class);
		if (typeStr.equals("struct")) {
			retType = new StructType(tag, definingScope, decl);
		} else {
			retType = new UnionType(tag, definingScope, decl);
		}

		/*
		 * Step 2: Visit the structure/union to obtain the list of member
		 * elements.
		 */

		List<StructOrUnionMember> elementList = new ArrayList<>();
		for (Node node : n.getF3().getF0().getNodes()) {
			StructDeclaration structDeclaration = (StructDeclaration) node;
			for (StructOrUnionMember member : getElementList(structDeclaration, (Scopeable) retType)) {
				elementList.add(member);
			}
		}

		/*
		 * Stop 3: Make the type complete, by adding information about the
		 * members of the type.
		 */
		if (retType instanceof StructType) {
			((StructType) retType).makeComplete(elementList);
		} else if (retType instanceof UnionType) {
			((UnionType) retType).makeComplete(elementList);
		}

		if (!deleteUserDefinedTypes) {
			retType = Type.putTypeToScope(definingScope, tag, retType);
		} else {
			retType = Type.removeTypeFromScope(definingScope, tag, retType);
		}
		return retType;
	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public Type visit(StructOrUnionSpecifierWithId n, Type argu) {
		Type retType = null;
		String typeStr = ((NodeToken) n.getF0().getF0().getChoice()).getTokenImage().equals("struct") ? "struct"
				: "union";
		String tag = n.getF1().getTokenImage();
		retType = Type.getTypeEntry(tag, definingScope);
		if (retType == null) {
			Declaration decl = Misc.getEnclosingNode(n, Declaration.class);
			if (typeStr.equals("struct")) {
				retType = new StructType(tag, definingScope, decl);
			} else {
				retType = new UnionType(tag, definingScope, decl);
			}
			if (!deleteUserDefinedTypes) {
				retType = Type.putTypeToScope(definingScope, tag, retType);
			} else {
				retType = Type.removeTypeFromScope(definingScope, tag, retType);
			}
		}
		return retType;
	}

	private static List<StructOrUnionMember> getElementList(StructDeclaration decl, Scopeable scope) {
		List<StructOrUnionMember> members = new ArrayList<>();
		SpecifierQualifierList specQualList = decl.getF0();

		for (StructDeclarator structDecl : Misc.getInheritedEnclosee(decl.getF1(), StructDeclarator.class)) {
			StructOrUnionMember structOrUnionMember = null;
			if (structDecl.getF0().getChoice() instanceof StructDeclaratorWithDeclarator) {
				StructDeclaratorWithDeclarator structDeclWithDecl = (StructDeclaratorWithDeclarator) structDecl.getF0()
						.getChoice();
				if (structDeclWithDecl.getF1().present()) {
					ConstantExpression constExp = (ConstantExpression) ((NodeSequence) structDeclWithDecl.getF1()
							.getNode()).getNodes().get(1);
					structOrUnionMember = new StructOrUnionMember(Type.getTypeTree(specQualList, structDecl, scope),
							DeclarationInfo.getRootIdName(structDeclWithDecl), constExp);
				} else {
					structOrUnionMember = new StructOrUnionMember(Type.getTypeTree(specQualList, structDecl, scope),
							DeclarationInfo.getRootIdName(structDeclWithDecl));

				}
			} else {
				StructDeclaratorWithBitField structDeclWithBF = (StructDeclaratorWithBitField) structDecl.getF0()
						.getChoice();
				ConstantExpression constExp = structDeclWithBF.getF1();
				structOrUnionMember = new StructOrUnionMember(Type.getTypeTree(specQualList, structDecl, scope),
						constExp);
			}
			assert (structOrUnionMember != null);
			members.add(structOrUnionMember);

		}
		return members;
	}

	/**
	 * f0 ::= <STRUCT>
	 * | <UNION>
	 */
	@Override
	public Type visit(StructOrUnion n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= ( StructDeclaration() )+
	 */
	@Override
	public Type visit(StructDeclarationList n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= InitDeclarator()
	 * f1 ::= ( "," InitDeclarator() )*
	 */
	@Override
	public Type visit(InitDeclaratorList n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( "=" Initializer() )?
	 */
	@Override
	public Type visit(InitDeclarator n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= StructDeclaratorList()
	 * f2 ::= ";"
	 */
	@Override
	public Type visit(StructDeclaration n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= ( ASpecifierQualifier() )+
	 */
	@Override
	public Type visit(SpecifierQualifierList n, Type argu) {
		Type retType = argu;

		Type baseType;
		ArithmeticTypeKeyCollector arithmeticTypeGetter = new ArithmeticTypeKeyCollector(definingScope);
		n.accept(arithmeticTypeGetter);
		baseType = Type.getTypeFromArithmeticKeys(arithmeticTypeGetter.keywords);

		if (baseType == null) {
			for (Node node : n.getF0().getNodes()) {
				ASpecifierQualifier aDeclSpec = (ASpecifierQualifier) node;
				retType = aDeclSpec.accept(this, retType);
			}
		} else {
			retType = baseType;
		}
		return retType;
	}

	/**
	 * f0 ::= TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public Type visit(ASpecifierQualifier n, Type argu) {
		return n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= StructDeclarator()
	 * f1 ::= ( "," StructDeclarator() )*
	 */
	@Override
	public Type visit(StructDeclaratorList n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= StructDeclaratorWithDeclarator()
	 * | StructDeclaratorWithBitField()
	 */
	@Override
	public Type visit(StructDeclarator n, Type argu) {
		return n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( ":" ConstantExpression() )?
	 */
	@Override
	public Type visit(StructDeclaratorWithDeclarator n, Type argu) {
		return n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= ":"
	 * f1 ::= ConstantExpression()
	 */
	@Override
	public Type visit(StructDeclaratorWithBitField n, Type argu) {
		return argu;
	}

	/**
	 * f0 ::= EnumSpecifierWithList()
	 * | EnumSpecifierWithId()
	 */
	@Override
	public Type visit(EnumSpecifier n, Type argu) {
		return n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= ( <IDENTIFIER> )?
	 * f2 ::= "{"
	 * f3 ::= EnumeratorList()
	 * f4 ::= "}"
	 */
	@Override
	public Type visit(EnumSpecifierWithList n, Type argu) {
		/*
		 * Step 1: Create the incomplete type.
		 */
		Type retType = null;
		String tag = null;
		if (n.getF1().present()) {
			tag = ((NodeToken) n.getF1().getNode()).getTokenImage();
		}
		assert (tag != null); // In a pre-pass, we have given names to all the unnamed enums.
		Declaration decl = Misc.getEnclosingNode(n, Declaration.class);
		retType = new EnumType(tag, definingScope, decl);

		/*
		 * Step 2: Visit the enumerator to obtain the list of member elements.
		 */

		List<EnumMember> members = new ArrayList<>();
		Enumerator enumerator = n.getF3().getF0();
		members.add(this.getEnumMember(enumerator));
		for (Node nodeSeqNode : n.getF3().getF1().getNodes()) {
			NodeSequence nodeSeq = (NodeSequence) nodeSeqNode;
			enumerator = (Enumerator) nodeSeq.getNodes().get(1);
			members.add(this.getEnumMember(enumerator));
		}

		/*
		 * Stop 3: Make the type complete, by adding information about the
		 * members of the type.
		 */
		((EnumType) retType).makeComplete(members);
		if (!deleteUserDefinedTypes) {
			retType = Type.putTypeToScope(definingScope, tag, retType);
		} else {
			retType = Type.removeTypeFromScope(definingScope, tag, retType);
		}
		return retType;
	}

	private EnumMember getEnumMember(Enumerator enumerator) {
		String memberName = enumerator.getF0().getTokenImage();
		if (enumerator.getF1().present()) {
			ConstantExpression constExp = (ConstantExpression) ((NodeSequence) enumerator.getF1().getNode()).getNodes()
					.get(1);
			return new EnumMember(memberName, constExp);
		} else {
			return new EnumMember(memberName);
		}
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public Type visit(EnumSpecifierWithId n, Type argu) {
		Type retType = null;
		String tag = n.getF1().getTokenImage();
		retType = Type.getTypeEntry(tag, definingScope);
		if (retType == null) {
			Declaration decl = Misc.getEnclosingNode(n, Declaration.class);
			retType = new EnumType(tag, definingScope, decl);
			if (!deleteUserDefinedTypes) {
				retType = Type.putTypeToScope(definingScope, tag, retType);
			} else {
				retType = Type.removeTypeFromScope(definingScope, tag, retType);
			}
		}
		return retType;
	}

	/**
	 * f0 ::= Enumerator()
	 * f1 ::= ( "," Enumerator() )*
	 */
	@Override
	public Type visit(EnumeratorList n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "=" ConstantExpression() )?
	 */
	@Override
	public Type visit(Enumerator n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= ( Pointer() )?
	 * f1 ::= DirectDeclarator()
	 */
	@Override
	public Type visit(Declarator n, Type argu) {
		Type base1 = null;
		if (n.getF0().present()) {
			base1 = n.getF0().getNode().accept(this, argu);
		} else {
			base1 = argu;
		}
		return n.getF1().accept(this, base1);
	}

	/**
	 * f0 ::= IdentifierOrDeclarator()
	 * f1 ::= DeclaratorOpList()
	 */
	@Override
	public Type visit(DirectDeclarator n, Type argu) {
		Type base1 = n.getF1().accept(this, argu);
		return n.getF0().accept(this, base1);
	}

	/**
	 * f0 ::= ( ADeclaratorOp() )*
	 */
	@Override
	public Type visit(DeclaratorOpList n, Type argu) {
		Type base1 = argu;
		List<Node> nodeList = n.getF0().getNodes();
		for (Node node : Misc.reverseList(nodeList)) {
			base1 = node.accept(this, base1);
		}
		return base1;
	}

	/**
	 * f0 ::= DimensionSize()
	 * | ParameterTypeListClosed()
	 * | OldParameterListClosed()
	 */
	@Override
	public Type visit(ADeclaratorOp n, Type argu) {
		return n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= "["
	 * f1 ::= ( ConstantExpression() )?
	 * f2 ::= "]"
	 */
	@Override
	public Type visit(DimensionSize n, Type argu) {
		if (n.getF1().present()) {
			ConstantExpression constExpCopy = FrontEnd.parseAlone(n.getF1().getNode().getInfo().getString(),
					ConstantExpression.class);
			return new ArrayType(argu, constExpCopy);
		} else {
			return new ArrayType(argu);
		}
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ParameterTypeList() )?
	 * f2 ::= ")"
	 */
	@Override
	public Type visit(ParameterTypeListClosed n, Type argu) {
		List<FunctionType.Parameter> parameters = new ArrayList<>();
		if (n.getF1().present()) {
			// Note that here we are using the same definingScope in which this part of a
			// declaration is present.
			parameters = this.getParameters((ParameterTypeList) n.getF1().getNode(), definingScope);
		}
		return new FunctionType(argu, parameters);
	}

	private List<FunctionType.Parameter> getParameters(ParameterTypeList paramList, Scopeable scope) {
		List<FunctionType.Parameter> parameters = new ArrayList<>();
		for (ParameterDeclaration paramDeclNode : Misc.getInheritedEnclosee(paramList, ParameterDeclaration.class)) {
			// System.out.println("Processing the following parameter-declaration now: " +
			// paramDeclNode.getInfo().getString());
			if (paramDeclNode.toString().equals("void ")) {
				continue;
			}
			Type paramType = Type.getTypeTree(paramDeclNode, definingScope);
			assert (paramType != null);
			String paramName = ParameterDeclarationInfo.getRootParamName(paramDeclNode);
			parameters.add(new FunctionType.Parameter(paramType, paramName, paramDeclNode));
		}
		return parameters;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( OldParameterList() )?
	 * f2 ::= ")"
	 */
	@Override
	public Type visit(OldParameterListClosed n, Type argu) {
		assert (false); // Old-style function prototypes should have been removed by a pre-pass already.
		return null;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | "(" Declarator() ")"
	 */
	@Override
	public Type visit(IdentifierOrDeclarator n, Type argu) {
		Node choice = n.getF0().getChoice();
		if (choice instanceof NodeToken) {
			return argu;
		} else {
			NodeSequence nodeSeq = (NodeSequence) choice;
			return nodeSeq.getNodes().get(1).accept(this, argu);
		}
	}

	/**
	 * f0 ::= ( "*" | "^" )
	 * f1 ::= ( TypeQualifierList() )?
	 * f2 ::= ( Pointer() )?
	 */
	@Override
	public Type visit(Pointer n, Type argu) {
		Type retType = null;

		Type base1 = new PointerType(argu);
		if (n.getF2().present()) {
			retType = n.getF2().getNode().accept(this, base1);
		} else {
			retType = base1;
		}
		return retType;
	}

	/**
	 * f0 ::= ( TypeQualifier() )+
	 */
	@Override
	public Type visit(TypeQualifierList n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= ParameterList()
	 * f1 ::= ( "," "..." )?
	 */
	@Override
	public Type visit(ParameterTypeList n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= ParameterDeclaration()
	 * f1 ::= ( "," ParameterDeclaration() )*
	 */
	@Override
	public Type visit(ParameterList n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ParameterAbstraction()
	 */
	@Override
	public Type visit(ParameterDeclaration n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= Declarator()
	 * | AbstractOptionalDeclarator()
	 */
	@Override
	public Type visit(ParameterAbstraction n, Type argu) {
		return n.getF0().getChoice().accept(this, argu);
	}

	/**
	 * f0 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public Type visit(AbstractOptionalDeclarator n, Type argu) {
		return n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public Type visit(TypeName n, Type argu) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= AbstractDeclaratorWithPointer()
	 * | DirectAbstractDeclarator()
	 */
	@Override
	public Type visit(AbstractDeclarator n, Type argu) {
		return n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= Pointer()
	 * f1 ::= ( DirectAbstractDeclarator() )?
	 */
	@Override
	public Type visit(AbstractDeclaratorWithPointer n, Type argu) {
		Type retType = null;
		Type base1 = n.getF0().accept(this, argu);
		if (n.getF1().present()) {
			retType = n.getF1().accept(this, base1);
		} else {
			retType = base1;
		}
		return retType;
	}

	/**
	 * f0 ::= AbstractDimensionOrParameter()
	 * f1 ::= DimensionOrParameterList()
	 */
	@Override
	public Type visit(DirectAbstractDeclarator n, Type argu) {
		Type retType = null;
		Type base1 = n.getF0().accept(this, argu);
		retType = n.getF1().accept(this, base1);
		return retType;
	}

	/**
	 * f0 ::= AbstractDeclaratorClosed()
	 * | DimensionSize()
	 * | ParameterTypeListClosed()
	 */
	@Override
	public Type visit(AbstractDimensionOrParameter n, Type argu) {
		return n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= AbstractDeclarator()
	 * f2 ::= ")"
	 */
	@Override
	public Type visit(AbstractDeclaratorClosed n, Type argu) {
		return n.getF1().accept(this, argu);
	}

	/**
	 * f0 ::= ( ADimensionOrParameter() )*
	 */
	@Override
	public Type visit(DimensionOrParameterList n, Type argu) {
		Type retType = argu;
		for (Node node : n.getF0().getNodes()) {
			retType = node.accept(this, retType);
		}
		return retType;
	}

	/**
	 * f0 ::= DimensionSize()
	 * | ParameterTypeListClosed()
	 */
	@Override
	public Type visit(ADimensionOrParameter n, Type argu) {
		return n.getF0().accept(this, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 */
	@Override
	public Type visit(TypedefName n, Type argu) {
		Typedef tDef = Typedef.getTypedefEntry(n.getF0().getTokenImage(), definingScope);
		if (tDef == null) {
			Misc.warnDueToLackOfFeature("Assuming the unknown type " + n + " to be of type SignedInt.", null);
			return SignedIntType.type();
		}
		assert (tDef != null);
		return tDef.getType();
	}
}
