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
public class GJVoidDepthFirstProcess<A> extends GJVoidDepthFirst<A> {
	//
	// Auto class visitors--probably don't need to be overridden.
	//

	public void initProcess(Node n, A argu) {
	}

	public void endProcess(Node n, A argu) {
	}

	@Override
	public void visit(NodeList n, A argu) {
		initProcess(n, argu);
		for (Node e : n.getNodes()) {
			e.accept(this, argu);
		}
		endProcess(n, argu);
	}

	@Override
	public void visit(NodeListOptional n, A argu) {
		initProcess(n, argu);
		if (n.present()) {
			for (Node e : n.getNodes()) {
				e.accept(this, argu);
			}
		}
		endProcess(n, argu);
	}

	@Override
	public void visit(NodeOptional n, A argu) {
		initProcess(n, argu);
		if (n.present()) {
			n.getNode().accept(this, argu);
		}
		endProcess(n, argu);
	}

	@Override
	public void visit(NodeSequence n, A argu) {
		initProcess(n, argu);
		for (Node e : n.getNodes()) {
			e.accept(this, argu);
		}
		endProcess(n, argu);
	}

	@Override
	public void visit(NodeChoice n, A argu) {
		initProcess(n, argu);
		n.getChoice().accept(this, argu);
		endProcess(n, argu);
	}

	@Override
	public void visit(NodeToken n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	//
	// User-generated visitor methods below
	//

	/**
	 * f0 ::= ( ElementsOfTranslation() )+
	 */
	@Override
	public void visit(TranslationUnit n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ExternalDeclaration()
	 * | UnknownCpp()
	 * | UnknownPragma()
	 */
	@Override
	public void visit(ElementsOfTranslation n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= Declaration()
	 * | FunctionDefinition()
	 * | DeclareReductionDirective()
	 * | ThreadPrivateDirective()
	 */
	@Override
	public void visit(ExternalDeclaration n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public void visit(FunctionDefinition n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(Declaration n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( Declaration() )+
	 */
	@Override
	public void visit(DeclarationList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( ADeclarationSpecifier() )+
	 */
	@Override
	public void visit(DeclarationSpecifiers n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= StorageClassSpecifier()
	 * | TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public void visit(ADeclarationSpecifier n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <AUTO>
	 * | <REGISTER>
	 * | <STATIC>
	 * | <EXTERN>
	 * | <TYPEDEF>
	 */
	@Override
	public void visit(StorageClassSpecifier n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( <VOID> | <CHAR> | <SHORT> | <INT> | <LONG> | <FLOAT> | <DOUBLE>
	 * |
	 * <SIGNED> | <UNSIGNED> | StructOrUnionSpecifier() | EnumSpecifier() |
	 * TypedefName() )
	 */
	@Override
	public void visit(TypeSpecifier n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(TypeQualifier n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * 
	 * f0 ::= ( StructOrUnionSpecifierWithList() |
	 * StructOrUnionSpecifierWithId()
	 * )
	 */
	@Override
	public void visit(StructOrUnionSpecifier n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= ( <IDENTIFIER> )?
	 * f2 ::= "{"
	 * f3 ::= StructDeclarationList()
	 * f4 ::= "}"
	 */
	@Override
	public void visit(StructOrUnionSpecifierWithList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(StructOrUnionSpecifierWithId n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <STRUCT>
	 * | <UNION>
	 */
	@Override
	public void visit(StructOrUnion n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( StructDeclaration() )+
	 */
	@Override
	public void visit(StructDeclarationList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= InitDeclarator()
	 * f1 ::= ( "," InitDeclarator() )*
	 */
	@Override
	public void visit(InitDeclaratorList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( "=" Initializer() )?
	 */
	@Override
	public void visit(InitDeclarator n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= StructDeclaratorList()
	 * f2 ::= ";"
	 */
	@Override
	public void visit(StructDeclaration n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( ASpecifierQualifier() )+
	 */
	@Override
	public void visit(SpecifierQualifierList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public void visit(ASpecifierQualifier n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= StructDeclarator()
	 * f1 ::= ( "," StructDeclarator() )*
	 */
	@Override
	public void visit(StructDeclaratorList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= StructDeclaratorWithDeclarator()
	 * | StructDeclaratorWithBitField()
	 */
	@Override
	public void visit(StructDeclarator n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( ":" ConstantExpression() )?
	 */
	@Override
	public void visit(StructDeclaratorWithDeclarator n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ":"
	 * f1 ::= ConstantExpression()
	 */
	@Override
	public void visit(StructDeclaratorWithBitField n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= EnumSpecifierWithList()
	 * | EnumSpecifierWithId()
	 */
	@Override
	public void visit(EnumSpecifier n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= ( <IDENTIFIER> )?
	 * f2 ::= "{"
	 * f3 ::= EnumeratorList()
	 * f4 ::= "}"
	 */
	@Override
	public void visit(EnumSpecifierWithList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(EnumSpecifierWithId n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= Enumerator()
	 * f1 ::= ( "," Enumerator() )*
	 */
	@Override
	public void visit(EnumeratorList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "=" ConstantExpression() )?
	 */
	@Override
	public void visit(Enumerator n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( Pointer() )?
	 * f1 ::= DirectDeclarator()
	 */
	@Override
	public void visit(Declarator n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= IdentifierOrDeclarator()
	 * f1 ::= DeclaratorOpList()
	 */
	@Override
	public void visit(DirectDeclarator n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( ADeclaratorOp() )*
	 */
	@Override
	public void visit(DeclaratorOpList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= DimensionSize()
	 * | ParameterTypeListClosed()
	 * | OldParameterListClosed()
	 */
	@Override
	public void visit(ADeclaratorOp n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "["
	 * f1 ::= ( ConstantExpression() )?
	 * f2 ::= "]"
	 */
	@Override
	public void visit(DimensionSize n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ParameterTypeList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ParameterTypeListClosed n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( OldParameterList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(OldParameterListClosed n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | "(" Declarator() ")"
	 */
	@Override
	public void visit(IdentifierOrDeclarator n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( "*" | "^" )
	 * f1 ::= ( TypeQualifierList() )?
	 * f2 ::= ( Pointer() )?
	 */
	@Override
	public void visit(Pointer n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( TypeQualifier() )+
	 */
	@Override
	public void visit(TypeQualifierList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ParameterList()
	 * f1 ::= ( "," "..." )?
	 */
	@Override
	public void visit(ParameterTypeList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ParameterDeclaration()
	 * f1 ::= ( "," ParameterDeclaration() )*
	 */
	@Override
	public void visit(ParameterList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ParameterAbstraction()
	 */
	@Override
	public void visit(ParameterDeclaration n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= Declarator()
	 * | AbstractOptionalDeclarator()
	 */
	@Override
	public void visit(ParameterAbstraction n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public void visit(AbstractOptionalDeclarator n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "," <IDENTIFIER> )*
	 */
	@Override
	public void visit(OldParameterList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * | ArrayInitializer()
	 */
	@Override
	public void visit(Initializer n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= InitializerList()
	 * f2 ::= ( "," )?
	 * f3 ::= "}"
	 */
	@Override
	public void visit(ArrayInitializer n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= Initializer()
	 * f1 ::= ( "," Initializer() )*
	 */
	@Override
	public void visit(InitializerList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public void visit(TypeName n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= AbstractDeclaratorWithPointer()
	 * | DirectAbstractDeclarator()
	 */
	@Override
	public void visit(AbstractDeclarator n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= Pointer()
	 * f1 ::= ( DirectAbstractDeclarator() )?
	 */
	@Override
	public void visit(AbstractDeclaratorWithPointer n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= AbstractDimensionOrParameter()
	 * f1 ::= DimensionOrParameterList()
	 */
	@Override
	public void visit(DirectAbstractDeclarator n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= AbstractDeclaratorClosed()
	 * | DimensionSize()
	 * | ParameterTypeListClosed()
	 */
	@Override
	public void visit(AbstractDimensionOrParameter n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= AbstractDeclarator()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(AbstractDeclaratorClosed n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( ADimensionOrParameter() )*
	 */
	@Override
	public void visit(DimensionOrParameterList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= DimensionSize()
	 * | ParameterTypeListClosed()
	 */
	@Override
	public void visit(ADimensionOrParameter n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(TypedefName n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( LabeledStatement() | ExpressionStatement() | CompoundStatement()
	 * | SelectionStatement() | IterationStatement() | JumpStatement() |
	 * UnknownPragma() | OmpConstruct() | OmpDirective() | UnknownCpp() )
	 */
	@Override
	public void visit(Statement n, A argu) {
		initProcess(n, argu);
		n.getStmtF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public void visit(UnknownCpp n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <OMP_CR>
	 * | <OMP_NL>
	 */
	@Override
	public void visit(OmpEol n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(OmpConstruct n, A argu) {
		initProcess(n, argu);
		n.getOmpConsF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= BarrierDirective()
	 * | TaskwaitDirective()
	 * | TaskyieldDirective()
	 * | FlushDirective()
	 */
	@Override
	public void visit(OmpDirective n, A argu) {
		initProcess(n, argu);
		n.getOmpDirF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(ParallelConstruct n, A argu) {
		initProcess(n, argu);
		n.getParConsF0().accept(this, argu);
		n.getParConsF1().accept(this, argu);
		n.getParConsF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <OMP>
	 */
	@Override
	public void visit(OmpPragma n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public void visit(UnknownPragma n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <PARALLEL>
	 * f1 ::= UniqueParallelOrDataClauseList()
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(ParallelDirective n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( AUniqueParallelOrDataClause() )*
	 */
	@Override
	public void visit(UniqueParallelOrDataClauseList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= UniqueParallelClause()
	 * | DataClause()
	 */
	@Override
	public void visit(AUniqueParallelOrDataClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= IfClause()
	 * | NumThreadsClause()
	 */
	@Override
	public void visit(UniqueParallelClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(IfClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(NumThreadsClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(DataClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <PRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpPrivateClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <FIRSTPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpFirstPrivateClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <LASTPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpLastPrivateClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <SHARED>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpSharedClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <COPYIN>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpCopyinClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= "("
	 * f2 ::= <SHARED>
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpDfltSharedClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= "("
	 * f2 ::= <NONE>
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpDfltNoneClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(OmpReductionClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(ForConstruct n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <FOR>
	 * f1 ::= UniqueForOrDataOrNowaitClauseList()
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(ForDirective n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( AUniqueForOrDataOrNowaitClause() )*
	 */
	@Override
	public void visit(UniqueForOrDataOrNowaitClauseList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= UniqueForClause()
	 * | DataClause()
	 * | NowaitClause()
	 */
	@Override
	public void visit(AUniqueForOrDataOrNowaitClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <NOWAIT>
	 */
	@Override
	public void visit(NowaitClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <ORDERED>
	 * | UniqueForClauseSchedule()
	 * | UniqueForCollapse()
	 */
	@Override
	public void visit(UniqueForClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <COLLAPSE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(UniqueForCollapse n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <SCHEDULE>
	 * f1 ::= "("
	 * f2 ::= ScheduleKind()
	 * f3 ::= ( "," Expression() )?
	 * f4 ::= ")"
	 */
	@Override
	public void visit(UniqueForClauseSchedule n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <STATIC>
	 * | <DYNAMIC>
	 * | <GUIDED>
	 * | <RUNTIME>
	 */
	@Override
	public void visit(ScheduleKind n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(OmpForHeader n, A argu) {
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
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForInitExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	@Override
	public void visit(OmpForCondition n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<"
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForLTCondition n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForLECondition n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">"
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForGTCondition n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForGECondition n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(OmpForReinitExpression n, A argu) {
		initProcess(n, argu);
		n.getOmpForReinitF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "++"
	 */
	@Override
	public void visit(PostIncrementId n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "--"
	 */
	@Override
	public void visit(PostDecrementId n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(PreIncrementId n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(PreDecrementId n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "+="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(ShortAssignPlus n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "-="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(ShortAssignMinus n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= <IDENTIFIER>
	 * f3 ::= "+"
	 * f4 ::= AdditiveExpression()
	 */
	@Override
	public void visit(OmpForAdditive n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= <IDENTIFIER>
	 * f3 ::= "-"
	 * f4 ::= AdditiveExpression()
	 */
	@Override
	public void visit(OmpForSubtractive n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= MultiplicativeExpression()
	 * f3 ::= "+"
	 * f4 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(OmpForMultiplicative n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTIONS>
	 * f2 ::= NowaitDataClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= SectionsScope()
	 */
	@Override
	public void visit(SectionsConstruct n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( ANowaitDataClause() )*
	 */
	@Override
	public void visit(NowaitDataClauseList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= NowaitClause()
	 * | DataClause()
	 */
	@Override
	public void visit(ANowaitDataClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( Statement() )?
	 * f2 ::= ( ASection() )*
	 * f3 ::= "}"
	 */
	@Override
	public void visit(SectionsScope n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTION>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(ASection n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SINGLE>
	 * f2 ::= SingleClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SingleConstruct n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( ASingleClause() )*
	 */
	@Override
	public void visit(SingleClauseList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= NowaitClause()
	 * | DataClause()
	 * | OmpCopyPrivateClause()
	 */
	@Override
	public void visit(ASingleClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <COPYPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpCopyPrivateClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASK>
	 * f2 ::= ( TaskClause() )*
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(TaskConstruct n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= DataClause()
	 * | UniqueTaskClause()
	 */
	@Override
	public void visit(TaskClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= IfClause()
	 * | FinalClause()
	 * | UntiedClause()
	 * | MergeableClause()
	 */
	@Override
	public void visit(UniqueTaskClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(FinalClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <UNTIED>
	 */
	@Override
	public void visit(UntiedClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <MERGEABLE>
	 */
	@Override
	public void visit(MergeableClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(ParallelForConstruct n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		n.getF6().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( AUniqueParallelOrUniqueForOrDataClause() )*
	 */
	@Override
	public void visit(UniqueParallelOrUniqueForOrDataClauseList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= UniqueParallelClause()
	 * | UniqueForClause()
	 * | DataClause()
	 */
	@Override
	public void visit(AUniqueParallelOrUniqueForOrDataClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(ParallelSectionsConstruct n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(MasterConstruct n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(CriticalConstruct n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ")"
	 */
	@Override
	public void visit(RegionPhrase n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= ExpressionStatement()
	 */
	@Override
	public void visit(AtomicConstruct n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <READ>
	 * | <WRITE>
	 * | <UPDATE>
	 * | <CAPTURE>
	 */
	@Override
	public void visit(AtomicClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public void visit(FlushDirective n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= VariableList()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(FlushVars n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(OrderedConstruct n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(BarrierDirective n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskwaitDirective n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskyieldDirective n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(ThreadPrivateDirective n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(DeclareReductionDirective n, A argu) {
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
	}

	/**
	 * f0 ::= ( TypeSpecifier() )*
	 */
	@Override
	public void visit(ReductionTypeList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= AssignInitializerClause()
	 * | ArgumentInitializerClause()
	 */
	@Override
	public void visit(InitializerClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(AssignInitializerClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(ArgumentInitializerClause n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		n.getF6().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(ReductionOp n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "," <IDENTIFIER> )*
	 */
	@Override
	public void visit(VariableList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= SimpleLabeledStatement()
	 * | CaseLabeledStatement()
	 * | DefaultLabeledStatement()
	 */
	@Override
	public void visit(LabeledStatement n, A argu) {
		initProcess(n, argu);
		n.getLabStmtF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(SimpleLabeledStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <CASE>
	 * f1 ::= ConstantExpression()
	 * f2 ::= ":"
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(CaseLabeledStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(DefaultLabeledStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ExpressionStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public void visit(CompoundStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= Declaration()
	 * | Statement()
	 */
	@Override
	public void visit(CompoundStatementElement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= IfStatement()
	 * | SwitchStatement()
	 */
	@Override
	public void visit(SelectionStatement n, A argu) {
		initProcess(n, argu);
		n.getSelStmtF0().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(IfStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SwitchStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= WhileStatement()
	 * | DoStatement()
	 * | ForStatement()
	 */
	@Override
	public void visit(IterationStatement n, A argu) {
		initProcess(n, argu);
		n.getItStmtF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <WHILE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(WhileStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(DoStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		n.getF4().accept(this, argu);
		n.getF5().accept(this, argu);
		n.getF6().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(ForStatement n, A argu) {
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
	}

	/**
	 * f0 ::= GotoStatement()
	 * | ContinueStatement()
	 * | BreakStatement()
	 * | ReturnStatement()
	 */
	@Override
	public void visit(JumpStatement n, A argu) {
		initProcess(n, argu);
		n.getJumpStmtF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public void visit(GotoStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ContinueStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(BreakStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(ReturnStatement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(Expression n, A argu) {
		initProcess(n, argu);
		n.getExpF0().accept(this, argu);
		n.getExpF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= NonConditionalExpression()
	 * | ConditionalExpression()
	 */
	@Override
	public void visit(AssignmentExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= UnaryExpression()
	 * f1 ::= AssignmentOperator()
	 * f2 ::= AssignmentExpression()
	 */
	@Override
	public void visit(NonConditionalExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(AssignmentOperator n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= LogicalORExpression()
	 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
	 */
	@Override
	public void visit(ConditionalExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ConditionalExpression()
	 */
	@Override
	public void visit(ConstantExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= LogicalANDExpression()
	 * f1 ::= ( "||" LogicalORExpression() )?
	 */
	@Override
	public void visit(LogicalORExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= InclusiveORExpression()
	 * f1 ::= ( "&&" LogicalANDExpression() )?
	 */
	@Override
	public void visit(LogicalANDExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ExclusiveORExpression()
	 * f1 ::= ( "|" InclusiveORExpression() )?
	 */
	@Override
	public void visit(InclusiveORExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ANDExpression()
	 * f1 ::= ( "^" ExclusiveORExpression() )?
	 */
	@Override
	public void visit(ExclusiveORExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= EqualityExpression()
	 * f1 ::= ( "&" ANDExpression() )?
	 */
	@Override
	public void visit(ANDExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= RelationalExpression()
	 * f1 ::= ( EqualOptionalExpression() )?
	 */
	@Override
	public void visit(EqualityExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= EqualExpression()
	 * | NonEqualExpression()
	 */
	@Override
	public void visit(EqualOptionalExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "=="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public void visit(EqualExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "!="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public void visit(NonEqualExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ShiftExpression()
	 * f1 ::= ( RelationalOptionalExpression() )?
	 */
	@Override
	public void visit(RelationalExpression n, A argu) {
		initProcess(n, argu);
		n.getRelExpF0().accept(this, argu);
		n.getRelExpF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= RelationalLTExpression()
	 * | RelationalGTExpression()
	 * | RelationalLEExpression()
	 * | RelationalGEExpression()
	 */
	@Override
	public void visit(RelationalOptionalExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "<"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalLTExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ">"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalGTExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "<="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalLEExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ">="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalGEExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= AdditiveExpression()
	 * f1 ::= ( ShiftOptionalExpression() )?
	 */
	@Override
	public void visit(ShiftExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ShiftLeftExpression()
	 * | ShiftRightExpression()
	 */
	@Override
	public void visit(ShiftOptionalExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ">>"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public void visit(ShiftLeftExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "<<"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public void visit(ShiftRightExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= MultiplicativeExpression()
	 * f1 ::= ( AdditiveOptionalExpression() )?
	 */
	@Override
	public void visit(AdditiveExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= AdditivePlusExpression()
	 * | AdditiveMinusExpression()
	 */
	@Override
	public void visit(AdditiveOptionalExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "+"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public void visit(AdditivePlusExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "-"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public void visit(AdditiveMinusExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= CastExpression()
	 * f1 ::= ( MultiplicativeOptionalExpression() )?
	 */
	@Override
	public void visit(MultiplicativeExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= MultiplicativeMultiExpression()
	 * | MultiplicativeDivExpression()
	 * | MultiplicativeModExpression()
	 */
	@Override
	public void visit(MultiplicativeOptionalExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "*"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeMultiExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "/"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeDivExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "%"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeModExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= CastExpressionTyped()
	 * | UnaryExpression()
	 */
	@Override
	public void visit(CastExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= TypeName()
	 * f2 ::= ")"
	 * f3 ::= CastExpression()
	 */
	@Override
	public void visit(CastExpressionTyped n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= UnaryExpressionPreIncrement()
	 * | UnaryExpressionPreDecrement()
	 * | UnarySizeofExpression()
	 * | UnaryCastExpression()
	 * | PostfixExpression()
	 */
	@Override
	public void visit(UnaryExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(UnaryExpressionPreIncrement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(UnaryExpressionPreDecrement n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= UnaryOperator()
	 * f1 ::= CastExpression()
	 */
	@Override
	public void visit(UnaryCastExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= SizeofTypeName()
	 * | SizeofUnaryExpression()
	 */
	@Override
	public void visit(UnarySizeofExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(SizeofUnaryExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= "("
	 * f2 ::= TypeName()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(SizeofTypeName n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		n.getF3().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(UnaryOperator n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public void visit(PostfixExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= ( APostfixOperation() )*
	 */
	@Override
	public void visit(PostfixOperationsList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
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
	public void visit(APostfixOperation n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "++"
	 */
	@Override
	public void visit(PlusPlus n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "--"
	 */
	@Override
	public void visit(MinusMinus n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "["
	 * f1 ::= Expression()
	 * f2 ::= "]"
	 */
	@Override
	public void visit(BracketExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ExpressionList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ArgumentList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "."
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(DotId n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "->"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(ArrowId n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | Constant()
	 * | ExpressionClosed()
	 */
	@Override
	public void visit(PrimaryExpression n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= Expression()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ExpressionClosed n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		n.getF2().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(ExpressionList n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		n.getF1().accept(this, argu);
		endProcess(n, argu);
	}

	/**
	 * f0 ::= <INTEGER_LITERAL>
	 * | <FLOATING_POINT_LITERAL>
	 * | <CHARACTER_LITERAL>
	 * | ( <STRING_LITERAL> )+
	 */
	@Override
	public void visit(Constant n, A argu) {
		initProcess(n, argu);
		n.getF0().accept(this, argu);
		endProcess(n, argu);
	}

	@Override
	public void visit(DummyFlushDirective n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	@Override
	public void visit(CallStatement n, A argu) {
		initProcess(n, argu);
		n.getPreCallNode().accept(this, argu);
		n.getPostCallNode().accept(this, argu);
		endProcess(n, argu);
	}

	@Override
	public void visit(PreCallNode n, A argu) {
		initProcess(n, argu);
		for (SimplePrimaryExpression arg : n.getArgumentList()) {
			arg.accept(this, argu);
		}
		endProcess(n, argu);
	}

	@Override
	public void visit(PostCallNode n, A argu) {
		initProcess(n, argu);
		if (n.hasReturnReceiver()) {
			n.getReturnReceiver().accept(this, argu);
		}
		endProcess(n, argu);
	}

	@Override
	public void visit(SimplePrimaryExpression n, A argu) {
		initProcess(n, argu);
		if (n.isAConstant()) {
			n.getConstant().accept(this, argu);
		} else if (n.isAnIdentifier()) {
			n.getIdentifier().accept(this, argu);
		}
		endProcess(n, argu);
	}

	@Override
	public void visit(BeginNode n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}

	@Override
	public void visit(EndNode n, A argu) {
		initProcess(n, argu);
		endProcess(n, argu);
	}
}
