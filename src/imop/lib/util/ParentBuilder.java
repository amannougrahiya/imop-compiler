/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.GJVoidDepthFirst;

/**
 * Creates parents of each node.
 * Note: This functionality has been shifted to the constructors, now.
 * Still, we may need this visitor sometime (quite improbable).
 */
public class ParentBuilder extends GJVoidDepthFirst<Node> {
	//
	// Auto class visitors--probably don't need to be overridden.
	//
	@Override
	public void visit(NodeOptional n, Node argu) {
		if (n.present()) {
			n.setParent(argu);
			n.getNode().accept(this, n);
		}
	}

	@Override
	public void visit(NodeChoice n, Node argu) {
		n.setParent(argu);
		n.getChoice().accept(this, n);
	}

	@Override
	public void visit(NodeToken n, Node argu) {
		n.setParent(argu);
	}

	//
	// User-generated visitor methods below
	//

	/**
	 * f0 ::= ( ElementsOfTranslation() )+
	 */
	@Override
	public void visit(TranslationUnit n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= ExternalDeclaration()
	 * | UnknownCpp()
	 * | UnknownPragma()
	 */
	@Override
	public void visit(ElementsOfTranslation n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= Declaration()
	 * | FunctionDefinition()
	 * | DeclareReductionDirective()
	 * | ThreadPrivateDirective()
	 */
	@Override
	public void visit(ExternalDeclaration n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public void visit(FunctionDefinition n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(Declaration n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= ( Declaration() )+
	 */
	@Override
	public void visit(DeclarationList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= ( ADeclarationSpecifier() )+
	 */
	@Override
	public void visit(DeclarationSpecifiers n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= StorageClassSpecifier()
	 * | TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public void visit(ADeclarationSpecifier n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <AUTO>
	 * | <REGISTER>
	 * | <STATIC>
	 * | <EXTERN>
	 * | <TYPEDEF>
	 */
	@Override
	public void visit(StorageClassSpecifier n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= ( <VOID> | <CHAR> | <SHORT> | <INT> | <LONG> | <FLOAT> | <DOUBLE>
	 * |
	 * <SIGNED> | <UNSIGNED> | StructOrUnionSpecifier() | EnumSpecifier() |
	 * TypedefName() )
	 */
	@Override
	public void visit(TypeSpecifier n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
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
	public void visit(TypeQualifier n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * 
	 * f0 ::= ( StructOrUnionSpecifierWithList() |
	 * StructOrUnionSpecifierWithId()
	 * )
	 */
	@Override
	public void visit(StructOrUnionSpecifier n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= ( <IDENTIFIER> )?
	 * f2 ::= "{"
	 * f3 ::= StructDeclarationList()
	 * f4 ::= "}"
	 */
	@Override
	public void visit(StructOrUnionSpecifierWithList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(StructOrUnionSpecifierWithId n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= <STRUCT>
	 * | <UNION>
	 */
	@Override
	public void visit(StructOrUnion n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= ( StructDeclaration() )+
	 */
	@Override
	public void visit(StructDeclarationList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= InitDeclarator()
	 * f1 ::= ( "," InitDeclarator() )*
	 */
	@Override
	public void visit(InitDeclaratorList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( "=" Initializer() )?
	 */
	@Override
	public void visit(InitDeclarator n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= StructDeclaratorList()
	 * f2 ::= ";"
	 */
	@Override
	public void visit(StructDeclaration n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= ( ASpecifierQualifier() )+
	 */
	@Override
	public void visit(SpecifierQualifierList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public void visit(ASpecifierQualifier n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= StructDeclarator()
	 * f1 ::= ( "," StructDeclarator() )*
	 */
	@Override
	public void visit(StructDeclaratorList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= StructDeclaratorWithDeclarator()
	 * | StructDeclaratorWithBitField()
	 */
	@Override
	public void visit(StructDeclarator n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( ":" ConstantExpression() )?
	 */
	@Override
	public void visit(StructDeclaratorWithDeclarator n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= ":"
	 * f1 ::= ConstantExpression()
	 */
	@Override
	public void visit(StructDeclaratorWithBitField n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= EnumSpecifierWithList()
	 * | EnumSpecifierWithId()
	 */
	@Override
	public void visit(EnumSpecifier n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= ( <IDENTIFIER> )?
	 * f2 ::= "{"
	 * f3 ::= EnumeratorList()
	 * f4 ::= "}"
	 */
	@Override
	public void visit(EnumSpecifierWithList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(EnumSpecifierWithId n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= Enumerator()
	 * f1 ::= ( "," Enumerator() )*
	 */
	@Override
	public void visit(EnumeratorList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "=" ConstantExpression() )?
	 */
	@Override
	public void visit(Enumerator n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= ( Pointer() )?
	 * f1 ::= DirectDeclarator()
	 */
	@Override
	public void visit(Declarator n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= IdentifierOrDeclarator()
	 * f1 ::= DeclaratorOpList()
	 */
	@Override
	public void visit(DirectDeclarator n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= ( ADeclaratorOp() )*
	 */
	@Override
	public void visit(DeclaratorOpList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= DimensionSize()
	 * | ParameterTypeListClosed()
	 * | OldParameterListClosed()
	 */
	@Override
	public void visit(ADeclaratorOp n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "["
	 * f1 ::= ( ConstantExpression() )?
	 * f2 ::= "]"
	 */
	@Override
	public void visit(DimensionSize n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ParameterTypeList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ParameterTypeListClosed n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( OldParameterList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(OldParameterListClosed n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | "(" Declarator() ")"
	 */
	@Override
	public void visit(IdentifierOrDeclarator n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= ( "*" | "^" )
	 * f1 ::= ( TypeQualifierList() )?
	 * f2 ::= ( Pointer() )?
	 */
	@Override
	public void visit(Pointer n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= ( TypeQualifier() )+
	 */
	@Override
	public void visit(TypeQualifierList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= ParameterList()
	 * f1 ::= ( "," "..." )?
	 */
	@Override
	public void visit(ParameterTypeList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= ParameterDeclaration()
	 * f1 ::= ( "," ParameterDeclaration() )*
	 */
	@Override
	public void visit(ParameterList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ParameterAbstraction()
	 */
	@Override
	public void visit(ParameterDeclaration n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= Declarator()
	 * | AbstractOptionalDeclarator()
	 */
	@Override
	public void visit(ParameterAbstraction n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public void visit(AbstractOptionalDeclarator n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "," <IDENTIFIER> )*
	 */
	@Override
	public void visit(OldParameterList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * | ArrayInitializer()
	 */
	@Override
	public void visit(Initializer n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= InitializerList()
	 * f2 ::= ( "," )?
	 * f3 ::= "}"
	 */
	@Override
	public void visit(ArrayInitializer n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= Initializer()
	 * f1 ::= ( "," Initializer() )*
	 */
	@Override
	public void visit(InitializerList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public void visit(TypeName n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= AbstractDeclaratorWithPointer()
	 * | DirectAbstractDeclarator()
	 */
	@Override
	public void visit(AbstractDeclarator n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= Pointer()
	 * f1 ::= ( DirectAbstractDeclarator() )?
	 */
	@Override
	public void visit(AbstractDeclaratorWithPointer n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= AbstractDimensionOrParameter()
	 * f1 ::= DimensionOrParameterList()
	 */
	@Override
	public void visit(DirectAbstractDeclarator n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= AbstractDeclaratorClosed()
	 * | DimensionSize()
	 * | ParameterTypeListClosed()
	 */
	@Override
	public void visit(AbstractDimensionOrParameter n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= AbstractDeclarator()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(AbstractDeclaratorClosed n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= ( ADimensionOrParameter() )*
	 */
	@Override
	public void visit(DimensionOrParameterList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= DimensionSize()
	 * | ParameterTypeListClosed()
	 */
	@Override
	public void visit(ADimensionOrParameter n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(TypedefName n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= ( LabeledStatement() | ExpressionStatement() | CompoundStatement()
	 * | SelectionStatement() | IterationStatement() | JumpStatement() |
	 * UnknownPragma() | OmpConstruct() | OmpDirective() | UnknownCpp() )
	 */
	@Override
	public void visit(Statement n, Node argu) {
		n.setParent(argu);
		n.getStmtF0().accept(this, n);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public void visit(UnknownCpp n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= <OMP_CR>
	 * | <OMP_NL>
	 */
	@Override
	public void visit(OmpEol n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
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
	public void visit(OmpConstruct n, Node argu) {
		n.setParent(argu);
		n.getOmpConsF0().accept(this, n);
	}

	/**
	 * f0 ::= BarrierDirective()
	 * | TaskwaitDirective()
	 * | TaskyieldDirective()
	 * | FlushDirective()
	 */
	@Override
	public void visit(OmpDirective n, Node argu) {
		n.setParent(argu);
		n.getOmpDirF0().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(ParallelConstruct n, Node argu) {
		n.setParent(argu);
		n.getParConsF0().accept(this, n);
		n.getParConsF1().accept(this, n);
		n.getParConsF2().accept(this, n);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <OMP>
	 */
	@Override
	public void visit(OmpPragma n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public void visit(UnknownPragma n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= <PARALLEL>
	 * f1 ::= UniqueParallelOrDataClauseList()
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(ParallelDirective n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= ( AUniqueParallelOrDataClause() )*
	 */
	@Override
	public void visit(UniqueParallelOrDataClauseList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= UniqueParallelClause()
	 * | DataClause()
	 */
	@Override
	public void visit(AUniqueParallelOrDataClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= IfClause()
	 * | NumThreadsClause()
	 */
	@Override
	public void visit(UniqueParallelClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(IfClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(NumThreadsClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
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
	public void visit(DataClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <PRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpPrivateClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= <FIRSTPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpFirstPrivateClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= <LASTPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpLastPrivateClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= <SHARED>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpSharedClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= <COPYIN>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpCopyinClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= "("
	 * f2 ::= <SHARED>
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpDfltSharedClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= "("
	 * f2 ::= <NONE>
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpDfltNoneClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
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
	public void visit(OmpReductionClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
		n.getF5().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(ForConstruct n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= <FOR>
	 * f1 ::= UniqueForOrDataOrNowaitClauseList()
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(ForDirective n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= ( AUniqueForOrDataOrNowaitClause() )*
	 */
	@Override
	public void visit(UniqueForOrDataOrNowaitClauseList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= UniqueForClause()
	 * | DataClause()
	 * | NowaitClause()
	 */
	@Override
	public void visit(AUniqueForOrDataOrNowaitClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <NOWAIT>
	 */
	@Override
	public void visit(NowaitClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <ORDERED>
	 * | UniqueForClauseSchedule()
	 * | UniqueForCollapse()
	 */
	@Override
	public void visit(UniqueForClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <COLLAPSE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(UniqueForCollapse n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= <SCHEDULE>
	 * f1 ::= "("
	 * f2 ::= ScheduleKind()
	 * f3 ::= ( "," Expression() )?
	 * f4 ::= ")"
	 */
	@Override
	public void visit(UniqueForClauseSchedule n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
	}

	/**
	 * f0 ::= <STATIC>
	 * | <DYNAMIC>
	 * | <GUIDED>
	 * | <RUNTIME>
	 */
	@Override
	public void visit(ScheduleKind n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
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
	public void visit(OmpForHeader n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
		n.getF5().accept(this, n);
		n.getF6().accept(this, n);
		n.getF7().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForInitExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	@Override
	public void visit(OmpForCondition n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<"
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForLTCondition n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForLECondition n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">"
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForGTCondition n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForGECondition n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
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
	public void visit(OmpForReinitExpression n, Node argu) {
		n.setParent(argu);
		n.getOmpForReinitF0().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "++"
	 */
	@Override
	public void visit(PostIncrementId n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "--"
	 */
	@Override
	public void visit(PostDecrementId n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(PreIncrementId n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(PreDecrementId n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "+="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(ShortAssignPlus n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "-="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(ShortAssignMinus n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= <IDENTIFIER>
	 * f3 ::= "+"
	 * f4 ::= AdditiveExpression()
	 */
	@Override
	public void visit(OmpForAdditive n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= <IDENTIFIER>
	 * f3 ::= "-"
	 * f4 ::= AdditiveExpression()
	 */
	@Override
	public void visit(OmpForSubtractive n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= MultiplicativeExpression()
	 * f3 ::= "+"
	 * f4 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(OmpForMultiplicative n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTIONS>
	 * f2 ::= NowaitDataClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= SectionsScope()
	 */
	@Override
	public void visit(SectionsConstruct n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
	}

	/**
	 * f0 ::= ( ANowaitDataClause() )*
	 */
	@Override
	public void visit(NowaitDataClauseList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= NowaitClause()
	 * | DataClause()
	 */
	@Override
	public void visit(ANowaitDataClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( Statement() )?
	 * f2 ::= ( ASection() )*
	 * f3 ::= "}"
	 */
	@Override
	public void visit(SectionsScope n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTION>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(ASection n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SINGLE>
	 * f2 ::= SingleClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SingleConstruct n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
	}

	/**
	 * f0 ::= ( ASingleClause() )*
	 */
	@Override
	public void visit(SingleClauseList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= NowaitClause()
	 * | DataClause()
	 * | OmpCopyPrivateClause()
	 */
	@Override
	public void visit(ASingleClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <COPYPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpCopyPrivateClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASK>
	 * f2 ::= ( TaskClause() )*
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(TaskConstruct n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
	}

	/**
	 * f0 ::= DataClause()
	 * | UniqueTaskClause()
	 */
	@Override
	public void visit(TaskClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= IfClause()
	 * | FinalClause()
	 * | UntiedClause()
	 * | MergeableClause()
	 */
	@Override
	public void visit(UniqueTaskClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(FinalClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= <UNTIED>
	 */
	@Override
	public void visit(UntiedClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <MERGEABLE>
	 */
	@Override
	public void visit(MergeableClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
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
	public void visit(ParallelForConstruct n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
		n.getF5().accept(this, n);
		n.getF6().accept(this, n);
	}

	/**
	 * f0 ::= ( AUniqueParallelOrUniqueForOrDataClause() )*
	 */
	@Override
	public void visit(UniqueParallelOrUniqueForOrDataClauseList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= UniqueParallelClause()
	 * | UniqueForClause()
	 * | DataClause()
	 */
	@Override
	public void visit(AUniqueParallelOrUniqueForOrDataClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
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
	public void visit(ParallelSectionsConstruct n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
		n.getF5().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(MasterConstruct n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(CriticalConstruct n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ")"
	 */
	@Override
	public void visit(RegionPhrase n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(AtomicConstruct n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
	}

	/**
	 * f0 ::= <READ>
	 * | <WRITE>
	 * | <UPDATE>
	 * | <CAPTURE>
	 */
	@Override
	public void visit(AtomicClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public void visit(FlushDirective n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= VariableList()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(FlushVars n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(OrderedConstruct n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(BarrierDirective n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskwaitDirective n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskyieldDirective n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
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
	public void visit(ThreadPrivateDirective n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
		n.getF5().accept(this, n);
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
	public void visit(DeclareReductionDirective n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
		n.getF5().accept(this, n);
		n.getF6().accept(this, n);
		n.getF7().accept(this, n);
		n.getF8().accept(this, n);
		n.getF9().accept(this, n);
		n.getF10().accept(this, n);
		n.getF11().accept(this, n);
	}

	/**
	 * f0 ::= ( TypeSpecifier() )*
	 */
	@Override
	public void visit(ReductionTypeList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= AssignInitializerClause()
	 * | ArgumentInitializerClause()
	 */
	@Override
	public void visit(InitializerClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
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
	public void visit(AssignInitializerClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
		n.getF5().accept(this, n);
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
	public void visit(ArgumentInitializerClause n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
		n.getF5().accept(this, n);
		n.getF6().accept(this, n);
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
	public void visit(ReductionOp n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "," <IDENTIFIER> )*
	 */
	@Override
	public void visit(VariableList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= SimpleLabeledStatement()
	 * | CaseLabeledStatement()
	 * | DefaultLabeledStatement()
	 */
	@Override
	public void visit(LabeledStatement n, Node argu) {
		n.setParent(argu);
		n.getLabStmtF0().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(SimpleLabeledStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= <CASE>
	 * f1 ::= ConstantExpression()
	 * f2 ::= ":"
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(CaseLabeledStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(DefaultLabeledStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ExpressionStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public void visit(CompoundStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= Declaration()
	 * | Statement()
	 */
	@Override
	public void visit(CompoundStatementElement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= IfStatement()
	 * | SwitchStatement()
	 */
	@Override
	public void visit(SelectionStatement n, Node argu) {
		n.setParent(argu);
		n.getSelStmtF0().accept(this, n);
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
	public void visit(IfStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
		n.getF5().accept(this, n);
	}

	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SwitchStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
	}

	/**
	 * f0 ::= WhileStatement()
	 * | DoStatement()
	 * | ForStatement()
	 */
	@Override
	public void visit(IterationStatement n, Node argu) {
		n.setParent(argu);
		n.getItStmtF0().accept(this, n);
	}

	/**
	 * f0 ::= <WHILE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(WhileStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
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
	public void visit(DoStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
		n.getF5().accept(this, n);
		n.getF6().accept(this, n);
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
	public void visit(ForStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
		n.getF4().accept(this, n);
		n.getF5().accept(this, n);
		n.getF6().accept(this, n);
		n.getF7().accept(this, n);
		n.getF8().accept(this, n);
	}

	/**
	 * f0 ::= GotoStatement()
	 * | ContinueStatement()
	 * | BreakStatement()
	 * | ReturnStatement()
	 */
	@Override
	public void visit(JumpStatement n, Node argu) {
		n.setParent(argu);
		n.getJumpStmtF0().accept(this, n);
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public void visit(GotoStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ContinueStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(BreakStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(ReturnStatement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(Expression n, Node argu) {
		n.setParent(argu);
		n.getExpF0().accept(this, n);
		n.getExpF1().accept(this, n);
	}

	/**
	 * f0 ::= NonConditionalExpression()
	 * | ConditionalExpression()
	 */
	@Override
	public void visit(AssignmentExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= UnaryExpression()
	 * f1 ::= AssignmentOperator()
	 * f2 ::= AssignmentExpression()
	 */
	@Override
	public void visit(NonConditionalExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
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
	public void visit(AssignmentOperator n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= LogicalORExpression()
	 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
	 */
	@Override
	public void visit(ConditionalExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= ConditionalExpression()
	 */
	@Override
	public void visit(ConstantExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= LogicalANDExpression()
	 * f1 ::= ( "||" LogicalORExpression() )?
	 */
	@Override
	public void visit(LogicalORExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= InclusiveORExpression()
	 * f1 ::= ( "&&" LogicalANDExpression() )?
	 */
	@Override
	public void visit(LogicalANDExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= ExclusiveORExpression()
	 * f1 ::= ( "|" InclusiveORExpression() )?
	 */
	@Override
	public void visit(InclusiveORExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= ANDExpression()
	 * f1 ::= ( "^" ExclusiveORExpression() )?
	 */
	@Override
	public void visit(ExclusiveORExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= EqualityExpression()
	 * f1 ::= ( "&" ANDExpression() )?
	 */
	@Override
	public void visit(ANDExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= RelationalExpression()
	 * f1 ::= ( EqualOptionalExpression() )?
	 */
	@Override
	public void visit(EqualityExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= EqualExpression()
	 * | NonEqualExpression()
	 */
	@Override
	public void visit(EqualOptionalExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "=="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public void visit(EqualExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= "!="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public void visit(NonEqualExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= ShiftExpression()
	 * f1 ::= ( RelationalOptionalExpression() )?
	 */
	@Override
	public void visit(RelationalExpression n, Node argu) {
		n.setParent(argu);
		n.getRelExpF0().accept(this, n);
		n.getRelExpF1().accept(this, n);
	}

	/**
	 * f0 ::= RelationalLTExpression()
	 * | RelationalGTExpression()
	 * | RelationalLEExpression()
	 * | RelationalGEExpression()
	 */
	@Override
	public void visit(RelationalOptionalExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "<"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalLTExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= ">"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalGTExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= "<="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalLEExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= ">="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalGEExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= AdditiveExpression()
	 * f1 ::= ( ShiftOptionalExpression() )?
	 */
	@Override
	public void visit(ShiftExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= ShiftLeftExpression()
	 * | ShiftRightExpression()
	 */
	@Override
	public void visit(ShiftOptionalExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= ">>"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public void visit(ShiftLeftExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= "<<"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public void visit(ShiftRightExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= MultiplicativeExpression()
	 * f1 ::= ( AdditiveOptionalExpression() )?
	 */
	@Override
	public void visit(AdditiveExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= AdditivePlusExpression()
	 * | AdditiveMinusExpression()
	 */
	@Override
	public void visit(AdditiveOptionalExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "+"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public void visit(AdditivePlusExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= "-"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public void visit(AdditiveMinusExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= CastExpression()
	 * f1 ::= ( MultiplicativeOptionalExpression() )?
	 */
	@Override
	public void visit(MultiplicativeExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= MultiplicativeMultiExpression()
	 * | MultiplicativeDivExpression()
	 * | MultiplicativeModExpression()
	 */
	@Override
	public void visit(MultiplicativeOptionalExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "*"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeMultiExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= "/"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeDivExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= "%"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeModExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= CastExpressionTyped()
	 * | UnaryExpression()
	 */
	@Override
	public void visit(CastExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= TypeName()
	 * f2 ::= ")"
	 * f3 ::= CastExpression()
	 */
	@Override
	public void visit(CastExpressionTyped n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
	}

	/**
	 * f0 ::= UnaryExpressionPreIncrement()
	 * | UnaryExpressionPreDecrement()
	 * | UnarySizeofExpression()
	 * | UnaryCastExpression()
	 * | PostfixExpression()
	 */
	@Override
	public void visit(UnaryExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(UnaryExpressionPreIncrement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(UnaryExpressionPreDecrement n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= UnaryOperator()
	 * f1 ::= CastExpression()
	 */
	@Override
	public void visit(UnaryCastExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= SizeofTypeName()
	 * | SizeofUnaryExpression()
	 */
	@Override
	public void visit(UnarySizeofExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(SizeofUnaryExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= "("
	 * f2 ::= TypeName()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(SizeofTypeName n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
		n.getF3().accept(this, n);
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
	public void visit(UnaryOperator n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public void visit(PostfixExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= ( APostfixOperation() )*
	 */
	@Override
	public void visit(PostfixOperationsList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
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
	public void visit(APostfixOperation n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "++"
	 */
	@Override
	public void visit(PlusPlus n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "--"
	 */
	@Override
	public void visit(MinusMinus n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "["
	 * f1 ::= Expression()
	 * f2 ::= "]"
	 */
	@Override
	public void visit(BracketExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ExpressionList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ArgumentList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= "."
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(DotId n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= "->"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(ArrowId n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | Constant()
	 * | ExpressionClosed()
	 */
	@Override
	public void visit(PrimaryExpression n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= Expression()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ExpressionClosed n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
		n.getF2().accept(this, n);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(ExpressionList n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
		n.getF1().accept(this, n);
	}

	/**
	 * f0 ::= <INTEGER_LITERAL>
	 * | <FLOATING_POINT_LITERAL>
	 * | <CHARACTER_LITERAL>
	 * | ( <STRING_LITERAL> )+
	 */
	@Override
	public void visit(Constant n, Node argu) {
		n.setParent(argu);
		n.getF0().accept(this, n);
	}

	@Override
	public void visit(DummyFlushDirective n, Node argu) {
		n.setParent(argu);
	}

	@Override
	public void visit(CallStatement n, Node argu) {
		n.setParent(argu);
		n.getPreCallNode().accept(this, n);
		n.getPostCallNode().accept(this, n);
	}

	@Override
	public void visit(PreCallNode n, Node argu) {
		n.setParent(argu);
		for (SimplePrimaryExpression arg : n.getArgumentList()) {
			arg.accept(this, n);
		}
	}

	@Override
	public void visit(PostCallNode n, Node argu) {
		n.setParent(argu);
		if (n.hasReturnReceiver()) {
			n.getReturnReceiver().accept(this, n);
		}
	}

	@Override
	public void visit(SimplePrimaryExpression n, Node argu) {
		n.setParent(argu);
	}
}
