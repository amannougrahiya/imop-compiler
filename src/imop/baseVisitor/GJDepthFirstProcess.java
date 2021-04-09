/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.baseVisitor;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order. Your visitors may extend this class.
 */
public class GJDepthFirstProcess<R, A> extends GJDepthFirst<R, A> {
	//
	// Auto class visitors--probably don't need to be overridden.
	//

	public R initProcess(Node n, A argu) {
		return null;
	}

	public R endProcess(Node n, A argu) {
		return null;
	}

	@Override
	public R visit(NodeList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		for (Node e : n.getNodes()) {
			_ret = e.accept(this, argu);
		}
		return _ret;
	}

	@Override
	public R visit(NodeListOptional n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		if (n.present()) {
			for (Node e : n.getNodes()) {
				_ret = e.accept(this, argu);
			}
		}
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(NodeOptional n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		if (n.present()) {
			_ret = n.getNode().accept(this, argu);
		}
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(NodeSequence n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		for (Node e : n.getNodes()) {
			_ret = e.accept(this, argu);
		}
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(NodeChoice n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		_ret = n.getChoice().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(NodeToken n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
		return null;
	}
	//
	// User-generated visitor methods below
	//

	/**
	 * f0 ::= ( ElementsOfTranslation() )+
	 */
	@Override
	public R visit(TranslationUnit n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ExternalDeclaration()
	 * | UnknownCpp()
	 * | UnknownPragma()
	 */
	@Override
	public R visit(ElementsOfTranslation n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= Declaration()
	 * | FunctionDefinition()
	 * | DeclareReductionDirective()
	 * | ThreadPrivateDirective()
	 */
	@Override
	public R visit(ExternalDeclaration n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public R visit(FunctionDefinition n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public R visit(Declaration n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( Declaration() )+
	 */
	@Override
	public R visit(DeclarationList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( ADeclarationSpecifier() )+
	 */
	@Override
	public R visit(DeclarationSpecifiers n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= StorageClassSpecifier()
	 * | TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public R visit(ADeclarationSpecifier n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <AUTO>
	 * | <REGISTER>
	 * | <STATIC>
	 * | <EXTERN>
	 * | <TYPEDEF>
	 */
	@Override
	public R visit(StorageClassSpecifier n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( <VOID> | <CHAR> | <SHORT> | <INT> | <LONG> | <FLOAT> | <DOUBLE>
	 * |
	 * <SIGNED> | <UNSIGNED> | StructOrUnionSpecifier() | EnumSpecifier() |
	 * TypedefName() )
	 */
	@Override
	public R visit(TypeSpecifier n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
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
	public R visit(TypeQualifier n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * 
	 * f0 ::= ( StructOrUnionSpecifierWithList() |
	 * StructOrUnionSpecifierWithId()
	 * )
	 */
	@Override
	public R visit(StructOrUnionSpecifier n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= ( <IDENTIFIER> )?
	 * f2 ::= "{"
	 * f3 ::= StructDeclarationList()
	 * f4 ::= "}"
	 */
	@Override
	public R visit(StructOrUnionSpecifierWithList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public R visit(StructOrUnionSpecifierWithId n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <STRUCT>
	 * | <UNION>
	 */
	@Override
	public R visit(StructOrUnion n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( StructDeclaration() )+
	 */
	@Override
	public R visit(StructDeclarationList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= InitDeclarator()
	 * f1 ::= ( "," InitDeclarator() )*
	 */
	@Override
	public R visit(InitDeclaratorList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( "=" Initializer() )?
	 */
	@Override
	public R visit(InitDeclarator n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= StructDeclaratorList()
	 * f2 ::= ";"
	 */
	@Override
	public R visit(StructDeclaration n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( ASpecifierQualifier() )+
	 */
	@Override
	public R visit(SpecifierQualifierList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public R visit(ASpecifierQualifier n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= StructDeclarator()
	 * f1 ::= ( "," StructDeclarator() )*
	 */
	@Override
	public R visit(StructDeclaratorList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= StructDeclaratorWithDeclarator()
	 * | StructDeclaratorWithBitField()
	 */
	@Override
	public R visit(StructDeclarator n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( ":" ConstantExpression() )?
	 */
	@Override
	public R visit(StructDeclaratorWithDeclarator n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ":"
	 * f1 ::= ConstantExpression()
	 */
	@Override
	public R visit(StructDeclaratorWithBitField n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= EnumSpecifierWithList()
	 * | EnumSpecifierWithId()
	 */
	@Override
	public R visit(EnumSpecifier n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= ( <IDENTIFIER> )?
	 * f2 ::= "{"
	 * f3 ::= EnumeratorList()
	 * f4 ::= "}"
	 */
	@Override
	public R visit(EnumSpecifierWithList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public R visit(EnumSpecifierWithId n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= Enumerator()
	 * f1 ::= ( "," Enumerator() )*
	 */
	@Override
	public R visit(EnumeratorList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "=" ConstantExpression() )?
	 */
	@Override
	public R visit(Enumerator n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( Pointer() )?
	 * f1 ::= DirectDeclarator()
	 */
	@Override
	public R visit(Declarator n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= IdentifierOrDeclarator()
	 * f1 ::= DeclaratorOpList()
	 */
	@Override
	public R visit(DirectDeclarator n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( ADeclaratorOp() )*
	 */
	@Override
	public R visit(DeclaratorOpList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= DimensionSize()
	 * | ParameterTypeListClosed()
	 * | OldParameterListClosed()
	 */
	@Override
	public R visit(ADeclaratorOp n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "["
	 * f1 ::= ( ConstantExpression() )?
	 * f2 ::= "]"
	 */
	@Override
	public R visit(DimensionSize n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ParameterTypeList() )?
	 * f2 ::= ")"
	 */
	@Override
	public R visit(ParameterTypeListClosed n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( OldParameterList() )?
	 * f2 ::= ")"
	 */
	@Override
	public R visit(OldParameterListClosed n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | "(" Declarator() ")"
	 */
	@Override
	public R visit(IdentifierOrDeclarator n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( "*" | "^" )
	 * f1 ::= ( TypeQualifierList() )?
	 * f2 ::= ( Pointer() )?
	 */
	@Override
	public R visit(Pointer n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( TypeQualifier() )+
	 */
	@Override
	public R visit(TypeQualifierList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ParameterList()
	 * f1 ::= ( "," "..." )?
	 */
	@Override
	public R visit(ParameterTypeList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ParameterDeclaration()
	 * f1 ::= ( "," ParameterDeclaration() )*
	 */
	@Override
	public R visit(ParameterList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ParameterAbstraction()
	 */
	@Override
	public R visit(ParameterDeclaration n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= Declarator()
	 * | AbstractOptionalDeclarator()
	 */
	@Override
	public R visit(ParameterAbstraction n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public R visit(AbstractOptionalDeclarator n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "," <IDENTIFIER> )*
	 */
	@Override
	public R visit(OldParameterList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * | ArrayInitializer()
	 */
	@Override
	public R visit(Initializer n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= InitializerList()
	 * f2 ::= ( "," )?
	 * f3 ::= "}"
	 */
	@Override
	public R visit(ArrayInitializer n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= Initializer()
	 * f1 ::= ( "," Initializer() )*
	 */
	@Override
	public R visit(InitializerList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public R visit(TypeName n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= AbstractDeclaratorWithPointer()
	 * | DirectAbstractDeclarator()
	 */
	@Override
	public R visit(AbstractDeclarator n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= Pointer()
	 * f1 ::= ( DirectAbstractDeclarator() )?
	 */
	@Override
	public R visit(AbstractDeclaratorWithPointer n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= AbstractDimensionOrParameter()
	 * f1 ::= DimensionOrParameterList()
	 */
	@Override
	public R visit(DirectAbstractDeclarator n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= AbstractDeclaratorClosed()
	 * | DimensionSize()
	 * | ParameterTypeListClosed()
	 */
	@Override
	public R visit(AbstractDimensionOrParameter n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= AbstractDeclarator()
	 * f2 ::= ")"
	 */
	@Override
	public R visit(AbstractDeclaratorClosed n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( ADimensionOrParameter() )*
	 */
	@Override
	public R visit(DimensionOrParameterList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= DimensionSize()
	 * | ParameterTypeListClosed()
	 */
	@Override
	public R visit(ADimensionOrParameter n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 */
	@Override
	public R visit(TypedefName n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( LabeledStatement() | ExpressionStatement() | CompoundStatement()
	 * | SelectionStatement() | IterationStatement() | JumpStatement() |
	 * UnknownPragma() | OmpConstruct() | OmpDirective() | UnknownCpp() )
	 */
	@Override
	public R visit(Statement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getStmtF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public R visit(UnknownCpp n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <OMP_CR>
	 * | <OMP_NL>
	 */
	@Override
	public R visit(OmpEol n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ParallelConstruct()
	 * | ForConstruct()
	 * | SectionsConstruct()
	 * | SingleConstruct()
	 * | ParallelForConstruct()
	 * | ParallelSectionsConstruct()
	 * | TaskConstruct()
	 * | MasterConstruct()
	 * | CriticalConstruct()
	 * | AtomicConstruct()
	 * | OrderedConstruct()
	 */
	@Override
	public R visit(OmpConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getOmpConsF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= BarrierDirective()
	 * | TaskwaitDirective()
	 * | TaskyieldDirective()
	 * | FlushDirective()
	 */
	@Override
	public R visit(OmpDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getOmpDirF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public R visit(ParallelConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getParConsF0().accept(this, argu);
		n.getParConsF1().accept(this, argu);
		n.getParConsF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <OMP>
	 */
	@Override
	public R visit(OmpPragma n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public R visit(UnknownPragma n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <PARALLEL>
	 * f1 ::= UniqueParallelOrDataClauseList()
	 * f2 ::= OmpEol()
	 */
	@Override
	public R visit(ParallelDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( AUniqueParallelOrDataClause() )*
	 */
	@Override
	public R visit(UniqueParallelOrDataClauseList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= UniqueParallelClause()
	 * | DataClause()
	 */
	@Override
	public R visit(AUniqueParallelOrDataClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= IfClause()
	 * | NumThreadsClause()
	 */
	@Override
	public R visit(UniqueParallelClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(IfClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(NumThreadsClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPrivateClause()
	 * | OmpFirstPrivateClause()
	 * | OmpLastPrivateClause()
	 * | OmpSharedClause()
	 * | OmpCopyinClause()
	 * | OmpDfltSharedClause()
	 * | OmpDfltNoneClause()
	 * | OmpReductionClause()
	 */
	@Override
	public R visit(DataClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <PRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(OmpPrivateClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <FIRSTPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(OmpFirstPrivateClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <LASTPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(OmpLastPrivateClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <SHARED>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(OmpSharedClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <COPYIN>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(OmpCopyinClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= "("
	 * f2 ::= <SHARED>
	 * f3 ::= ")"
	 */
	@Override
	public R visit(OmpDfltSharedClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= "("
	 * f2 ::= <NONE>
	 * f3 ::= ")"
	 */
	@Override
	public R visit(OmpDfltNoneClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <REDUCTION>
	 * f1 ::= "("
	 * f2 ::= ReductionOp()
	 * f3 ::= ":"
	 * f4 ::= VariableList()
	 * f5 ::= ")"
	 */
	@Override
	public R visit(OmpReductionClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public R visit(ForConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <FOR>
	 * f1 ::= UniqueForOrDataOrNowaitClauseList()
	 * f2 ::= OmpEol()
	 */
	@Override
	public R visit(ForDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( AUniqueForOrDataOrNowaitClause() )*
	 */
	@Override
	public R visit(UniqueForOrDataOrNowaitClauseList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= UniqueForClause()
	 * | DataClause()
	 * | NowaitClause()
	 */
	@Override
	public R visit(AUniqueForOrDataOrNowaitClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <NOWAIT>
	 */
	@Override
	public R visit(NowaitClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <ORDERED>
	 * | UniqueForClauseSchedule()
	 * | UniqueForCollapse()
	 */
	@Override
	public R visit(UniqueForClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <COLLAPSE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(UniqueForCollapse n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <SCHEDULE>
	 * f1 ::= "("
	 * f2 ::= ScheduleKind()
	 * f3 ::= ( "," Expression() )?
	 * f4 ::= ")"
	 */
	@Override
	public R visit(UniqueForClauseSchedule n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <STATIC>
	 * | <DYNAMIC>
	 * | <GUIDED>
	 * | <RUNTIME>
	 */
	@Override
	public R visit(ScheduleKind n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <FOR>
	 * f1 ::= "("
	 * f2 ::= OmpForInitExpression()
	 * f3 ::= ";"
	 * f4 ::= OmpForCondition()
	 * f5 ::= ";"
	 * f6 ::= OmpForReinitExpression()
	 * f7 ::= ")"
	 */
	@Override
	public R visit(OmpForHeader n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		n.getF6().accept(this, argu);
		n.getF7().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	@Override
	public R visit(OmpForInitExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	@Override
	public R visit(OmpForCondition n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<"
	 * f2 ::= Expression()
	 */
	@Override
	public R visit(OmpForLTCondition n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<="
	 * f2 ::= Expression()
	 */
	@Override
	public R visit(OmpForLECondition n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">"
	 * f2 ::= Expression()
	 */
	@Override
	public R visit(OmpForGTCondition n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">="
	 * f2 ::= Expression()
	 */
	@Override
	public R visit(OmpForGECondition n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= PostIncrementId()
	 * | PostDecrementId()
	 * | PreIncrementId()
	 * | PreDecrementId()
	 * | ShortAssignPlus()
	 * | ShortAssignMinus()
	 * | OmpForAdditive()
	 * | OmpForSubtractive()
	 * | OmpForMultiplicative()
	 */
	@Override
	public R visit(OmpForReinitExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getOmpForReinitF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "++"
	 */
	@Override
	public R visit(PostIncrementId n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "--"
	 */
	@Override
	public R visit(PostDecrementId n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public R visit(PreIncrementId n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public R visit(PreDecrementId n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "+="
	 * f2 ::= Expression()
	 */
	@Override
	public R visit(ShortAssignPlus n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "-="
	 * f2 ::= Expression()
	 */
	@Override
	public R visit(ShortAssignMinus n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= <IDENTIFIER>
	 * f3 ::= "+"
	 * f4 ::= AdditiveExpression()
	 */
	@Override
	public R visit(OmpForAdditive n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= <IDENTIFIER>
	 * f3 ::= "-"
	 * f4 ::= AdditiveExpression()
	 */
	@Override
	public R visit(OmpForSubtractive n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= MultiplicativeExpression()
	 * f3 ::= "+"
	 * f4 ::= <IDENTIFIER>
	 */
	@Override
	public R visit(OmpForMultiplicative n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTIONS>
	 * f2 ::= NowaitDataClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= SectionsScope()
	 */
	@Override
	public R visit(SectionsConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( ANowaitDataClause() )*
	 */
	@Override
	public R visit(NowaitDataClauseList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= NowaitClause()
	 * | DataClause()
	 */
	@Override
	public R visit(ANowaitDataClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( Statement() )?
	 * f2 ::= ( ASection() )*
	 * f3 ::= "}"
	 */
	@Override
	public R visit(SectionsScope n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTION>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public R visit(ASection n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SINGLE>
	 * f2 ::= SingleClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public R visit(SingleConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( ASingleClause() )*
	 */
	@Override
	public R visit(SingleClauseList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= NowaitClause()
	 * | DataClause()
	 * | OmpCopyPrivateClause()
	 */
	@Override
	public R visit(ASingleClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <COPYPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(OmpCopyPrivateClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASK>
	 * f2 ::= ( TaskClause() )*
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public R visit(TaskConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= DataClause()
	 * | UniqueTaskClause()
	 */
	@Override
	public R visit(TaskClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= IfClause()
	 * | FinalClause()
	 * | UntiedClause()
	 * | MergeableClause()
	 */
	@Override
	public R visit(UniqueTaskClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(FinalClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <UNTIED>
	 */
	@Override
	public R visit(UntiedClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <MERGEABLE>
	 */
	@Override
	public R visit(MergeableClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <PARALLEL>
	 * f2 ::= <FOR>
	 * f3 ::= UniqueParallelOrUniqueForOrDataClauseList()
	 * f4 ::= OmpEol()
	 * f5 ::= OmpForHeader()
	 * f6 ::= Statement()
	 */
	@Override
	public R visit(ParallelForConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		n.getF6().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( AUniqueParallelOrUniqueForOrDataClause() )*
	 */
	@Override
	public R visit(UniqueParallelOrUniqueForOrDataClauseList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= UniqueParallelClause()
	 * | UniqueForClause()
	 * | DataClause()
	 */
	@Override
	public R visit(AUniqueParallelOrUniqueForOrDataClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <PARALLEL>
	 * f2 ::= <SECTIONS>
	 * f3 ::= UniqueParallelOrDataClauseList()
	 * f4 ::= OmpEol()
	 * f5 ::= SectionsScope()
	 */
	@Override
	public R visit(ParallelSectionsConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public R visit(MasterConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public R visit(CriticalConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ")"
	 */
	@Override
	public R visit(RegionPhrase n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= ExpressionStatement()
	 */
	@Override
	public R visit(AtomicConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <READ>
	 * | <WRITE>
	 * | <UPDATE>
	 * | <CAPTURE>
	 */
	@Override
	public R visit(AtomicClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public R visit(FlushDirective n, A argu) {
		R _ret = null;
		if (n instanceof DummyFlushDirective) {
			assert (false);
			return null;
			// System.exit(0);
		}
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= VariableList()
	 * f2 ::= ")"
	 */
	@Override
	public R visit(FlushVars n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public R visit(OrderedConstruct n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public R visit(BarrierDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public R visit(TaskwaitDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public R visit(TaskyieldDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <THREADPRIVATE>
	 * f2 ::= "("
	 * f3 ::= VariableList()
	 * f4 ::= ")"
	 * f5 ::= OmpEol()
	 */
	@Override
	public R visit(ThreadPrivateDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <DECLARE>
	 * f2 ::= <REDUCTION>
	 * f3 ::= "("
	 * f4 ::= ReductionOp()
	 * f5 ::= ":"
	 * f6 ::= ReductionTypeList()
	 * f7 ::= ":"
	 * f8 ::= Expression()
	 * f9 ::= ")"
	 * f10 ::= ( InitializerClause() )?
	 * f11 ::= OmpEol()
	 */
	@Override
	public R visit(DeclareReductionDirective n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		n.getF6().accept(this, argu);
		n.getF7().accept(this, argu);
		n.getF8().accept(this, argu);
		n.getF9().accept(this, argu);
		n.getF10().accept(this, argu);
		n.getF11().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( TypeSpecifier() )*
	 */
	@Override
	public R visit(ReductionTypeList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= AssignInitializerClause()
	 * | ArgumentInitializerClause()
	 */
	@Override
	public R visit(InitializerClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <INITIALIZER>
	 * f1 ::= "("
	 * f2 ::= <IDENTIFIER>
	 * f3 ::= "="
	 * f4 ::= Initializer()
	 * f5 ::= ")"
	 */
	@Override
	public R visit(AssignInitializerClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <INITIALIZER>
	 * f1 ::= "("
	 * f2 ::= <IDENTIFIER>
	 * f3 ::= "("
	 * f4 ::= ExpressionList()
	 * f5 ::= ")"
	 * f6 ::= ")"
	 */
	@Override
	public R visit(ArgumentInitializerClause n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		n.getF6().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | "+"
	 * | "*"
	 * | "-"
	 * | "&"
	 * | "^"
	 * | "|"
	 * | "||"
	 * | "&&"
	 */
	@Override
	public R visit(ReductionOp n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "," <IDENTIFIER> )*
	 */
	@Override
	public R visit(VariableList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= SimpleLabeledStatement()
	 * | CaseLabeledStatement()
	 * | DefaultLabeledStatement()
	 */
	@Override
	public R visit(LabeledStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getLabStmtF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public R visit(SimpleLabeledStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <CASE>
	 * f1 ::= ConstantExpression()
	 * f2 ::= ":"
	 * f3 ::= Statement()
	 */
	@Override
	public R visit(CaseLabeledStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public R visit(DefaultLabeledStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public R visit(ExpressionStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public R visit(CompoundStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= Declaration()
	 * | Statement()
	 */
	@Override
	public R visit(CompoundStatementElement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= IfStatement()
	 * | SwitchStatement()
	 */
	@Override
	public R visit(SelectionStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getSelStmtF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 * f5 ::= ( <ELSE> Statement() )?
	 */
	@Override
	public R visit(IfStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public R visit(SwitchStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= WhileStatement()
	 * | DoStatement()
	 * | ForStatement()
	 */
	@Override
	public R visit(IterationStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getItStmtF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <WHILE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public R visit(WhileStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <DO>
	 * f1 ::= Statement()
	 * f2 ::= <WHILE>
	 * f3 ::= "("
	 * f4 ::= Expression()
	 * f5 ::= ")"
	 * f6 ::= ";"
	 */
	@Override
	public R visit(DoStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		n.getF6().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <FOR>
	 * f1 ::= "("
	 * f2 ::= ( Expression() )?
	 * f3 ::= ";"
	 * f4 ::= ( Expression() )?
	 * f5 ::= ";"
	 * f6 ::= ( Expression() )?
	 * f7 ::= ")"
	 * f8 ::= Statement()
	 */
	@Override
	public R visit(ForStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		n.getF6().accept(this, argu);
		n.getF7().accept(this, argu);
		n.getF8().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= GotoStatement()
	 * | ContinueStatement()
	 * | BreakStatement()
	 * | ReturnStatement()
	 */
	@Override
	public R visit(JumpStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getJumpStmtF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public R visit(GotoStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public R visit(ContinueStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public R visit(BreakStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public R visit(ReturnStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public R visit(Expression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getExpF0().accept(this, argu);
		n.getExpF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= NonConditionalExpression()
	 * | ConditionalExpression()
	 */
	@Override
	public R visit(AssignmentExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= UnaryExpression()
	 * f1 ::= AssignmentOperator()
	 * f2 ::= AssignmentExpression()
	 */
	@Override
	public R visit(NonConditionalExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "="
	 * | "*="
	 * | "/="
	 * | "%="
	 * | "+="
	 * | "-="
	 * | "<<="
	 * | ">>="
	 * | "&="
	 * | "^="
	 * | "|="
	 */
	@Override
	public R visit(AssignmentOperator n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= LogicalORExpression()
	 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
	 */
	@Override
	public R visit(ConditionalExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ConditionalExpression()
	 */
	@Override
	public R visit(ConstantExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= LogicalANDExpression()
	 * f1 ::= ( "||" LogicalORExpression() )?
	 */
	@Override
	public R visit(LogicalORExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= InclusiveORExpression()
	 * f1 ::= ( "&&" LogicalANDExpression() )?
	 */
	@Override
	public R visit(LogicalANDExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ExclusiveORExpression()
	 * f1 ::= ( "|" InclusiveORExpression() )?
	 */
	@Override
	public R visit(InclusiveORExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ANDExpression()
	 * f1 ::= ( "^" ExclusiveORExpression() )?
	 */
	@Override
	public R visit(ExclusiveORExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= EqualityExpression()
	 * f1 ::= ( "&" ANDExpression() )?
	 */
	@Override
	public R visit(ANDExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= RelationalExpression()
	 * f1 ::= ( EqualOptionalExpression() )?
	 */
	@Override
	public R visit(EqualityExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= EqualExpression()
	 * | NonEqualExpression()
	 */
	@Override
	public R visit(EqualOptionalExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "=="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public R visit(EqualExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "!="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public R visit(NonEqualExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ShiftExpression()
	 * f1 ::= ( RelationalOptionalExpression() )?
	 */
	@Override
	public R visit(RelationalExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getRelExpF0().accept(this, argu);
		n.getRelExpF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= RelationalLTExpression()
	 * | RelationalGTExpression()
	 * | RelationalLEExpression()
	 * | RelationalGEExpression()
	 */
	@Override
	public R visit(RelationalOptionalExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "<"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public R visit(RelationalLTExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ">"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public R visit(RelationalGTExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "<="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public R visit(RelationalLEExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ">="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public R visit(RelationalGEExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= AdditiveExpression()
	 * f1 ::= ( ShiftOptionalExpression() )?
	 */
	@Override
	public R visit(ShiftExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ShiftLeftExpression()
	 * | ShiftRightExpression()
	 */
	@Override
	public R visit(ShiftOptionalExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ">>"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public R visit(ShiftLeftExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "<<"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public R visit(ShiftRightExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= MultiplicativeExpression()
	 * f1 ::= ( AdditiveOptionalExpression() )?
	 */
	@Override
	public R visit(AdditiveExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= AdditivePlusExpression()
	 * | AdditiveMinusExpression()
	 */
	@Override
	public R visit(AdditiveOptionalExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "+"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public R visit(AdditivePlusExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "-"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public R visit(AdditiveMinusExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= CastExpression()
	 * f1 ::= ( MultiplicativeOptionalExpression() )?
	 */
	@Override
	public R visit(MultiplicativeExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= MultiplicativeMultiExpression()
	 * | MultiplicativeDivExpression()
	 * | MultiplicativeModExpression()
	 */
	@Override
	public R visit(MultiplicativeOptionalExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "*"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public R visit(MultiplicativeMultiExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "/"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public R visit(MultiplicativeDivExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "%"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public R visit(MultiplicativeModExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= CastExpressionTyped()
	 * | UnaryExpression()
	 */
	@Override
	public R visit(CastExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= TypeName()
	 * f2 ::= ")"
	 * f3 ::= CastExpression()
	 */
	@Override
	public R visit(CastExpressionTyped n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= UnaryExpressionPreIncrement()
	 * | UnaryExpressionPreDecrement()
	 * | UnarySizeofExpression()
	 * | UnaryCastExpression()
	 * | PostfixExpression()
	 */
	@Override
	public R visit(UnaryExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public R visit(UnaryExpressionPreIncrement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public R visit(UnaryExpressionPreDecrement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= UnaryOperator()
	 * f1 ::= CastExpression()
	 */
	@Override
	public R visit(UnaryCastExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= SizeofTypeName()
	 * | SizeofUnaryExpression()
	 */
	@Override
	public R visit(UnarySizeofExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public R visit(SizeofUnaryExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= "("
	 * f2 ::= TypeName()
	 * f3 ::= ")"
	 */
	@Override
	public R visit(SizeofTypeName n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "&"
	 * | "*"
	 * | "+"
	 * | "-"
	 * | "~"
	 * | "!"
	 */
	@Override
	public R visit(UnaryOperator n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public R visit(PostfixExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= ( APostfixOperation() )*
	 */
	@Override
	public R visit(PostfixOperationsList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= BracketExpression()
	 * | ArgumentList()
	 * | DotId()
	 * | ArrowId()
	 * | PlusPlus()
	 * | MinusMinus()
	 */
	@Override
	public R visit(APostfixOperation n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "++"
	 */
	@Override
	public R visit(PlusPlus n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "--"
	 */
	@Override
	public R visit(MinusMinus n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "["
	 * f1 ::= Expression()
	 * f2 ::= "]"
	 */
	@Override
	public R visit(BracketExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ExpressionList() )?
	 * f2 ::= ")"
	 */
	@Override
	public R visit(ArgumentList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "."
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public R visit(DotId n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "->"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public R visit(ArrowId n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | Constant()
	 * | ExpressionClosed()
	 */
	@Override
	public R visit(PrimaryExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= Expression()
	 * f2 ::= ")"
	 */
	@Override
	public R visit(ExpressionClosed n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public R visit(ExpressionList n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	/**
	 * f0 ::= <INTEGER_LITERAL>
	 * | <FLOATING_POINT_LITERAL>
	 * | <CHARACTER_LITERAL>
	 * | ( <STRING_LITERAL> )+
	 */
	@Override
	public R visit(Constant n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(DummyFlushDirective n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
		return null;
	}

	@Override
	public R visit(CallStatement n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		n.getPreCallNode().accept(this, argu);
		n.getPostCallNode().accept(this, argu);
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(PreCallNode n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		for (SimplePrimaryExpression arg : n.getArgumentList()) {
			arg.accept(this, argu);
		}
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(PostCallNode n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		if (n.hasReturnReceiver()) {
			n.getReturnReceiver().accept(this, argu);
		}
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(SimplePrimaryExpression n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		if (n.isAConstant()) {
			n.getConstant().accept(this, argu);
		} else if (n.isAnIdentifier()) {
			n.getIdentifier().accept(this, argu);
		}
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(BeginNode n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}

	@Override
	public R visit(EndNode n, A argu) {
		R _ret = null;
		initProcess(n, argu);
		endProcess(n, argu);
		return _ret;
	}
}
