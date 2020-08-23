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
public class DepthFirstProcess extends DepthFirstVisitor {
	//
	// Auto class visitors--probably don't need to be overridden.
	//

	public void initProcess(Node n) {
	}

	public void endProcess(Node n) {
	}

	@Override
	public void visit(NodeList n) {
		initProcess(n);
		for (Node e : n.getNodes()) {
			e.accept(this);
		}
		endProcess(n);
	}

	@Override
	public void visit(NodeListOptional n) {
		initProcess(n);
		if (n.present()) {
			for (Node e : n.getNodes()) {
				e.accept(this);
			}
		}
		endProcess(n);
	}

	@Override
	public void visit(NodeOptional n) {
		initProcess(n);
		if (n.present()) {
			n.getNode().accept(this);
		}
		endProcess(n);

	}

	@Override
	public void visit(NodeSequence n) {
		initProcess(n);
		for (Node e : n.getNodes()) {
			e.accept(this);
		}
		endProcess(n);
	}

	@Override
	public void visit(NodeChoice n) {
		initProcess(n);
		n.getChoice().accept(this);
		endProcess(n);
	}

	@Override
	public void visit(NodeToken n) {
		initProcess(n);
		endProcess(n);
	}

	//
	// User-generated visitor methods below
	//

	/**
	 * f0 ::= ( ElementsOfTranslation() )+
	 */
	@Override
	public void visit(TranslationUnit n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ExternalDeclaration()
	 * | UnknownCpp()
	 * | UnknownPragma()
	 */
	@Override
	public void visit(ElementsOfTranslation n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= Declaration()
	 * | FunctionDefinition()
	 * | DeclareReductionDirective()
	 * | ThreadPrivateDirective()
	 */
	@Override
	public void visit(ExternalDeclaration n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public void visit(FunctionDefinition n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(Declaration n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( Declaration() )+
	 */
	@Override
	public void visit(DeclarationList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( ADeclarationSpecifier() )+
	 */
	@Override
	public void visit(DeclarationSpecifiers n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= StorageClassSpecifier()
	 * | TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public void visit(ADeclarationSpecifier n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <AUTO>
	 * | <REGISTER>
	 * | <STATIC>
	 * | <EXTERN>
	 * | <TYPEDEF>
	 */
	@Override
	public void visit(StorageClassSpecifier n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( <VOID> | <CHAR> | <SHORT> | <INT> | <LONG> | <FLOAT> | <DOUBLE>
	 * |
	 * <SIGNED> | <UNSIGNED> | StructOrUnionSpecifier() | EnumSpecifier() |
	 * TypedefName() )
	 */
	@Override
	public void visit(TypeSpecifier n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
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
	public void visit(TypeQualifier n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * 
	 * f0 ::= ( StructOrUnionSpecifierWithList() |
	 * StructOrUnionSpecifierWithId()
	 * )
	 */
	@Override
	public void visit(StructOrUnionSpecifier n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= ( <IDENTIFIER> )?
	 * f2 ::= "{"
	 * f3 ::= StructDeclarationList()
	 * f4 ::= "}"
	 */
	@Override
	public void visit(StructOrUnionSpecifierWithList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(StructOrUnionSpecifierWithId n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <STRUCT>
	 * | <UNION>
	 */
	@Override
	public void visit(StructOrUnion n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( StructDeclaration() )+
	 */
	@Override
	public void visit(StructDeclarationList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= InitDeclarator()
	 * f1 ::= ( "," InitDeclarator() )*
	 */
	@Override
	public void visit(InitDeclaratorList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( "=" Initializer() )?
	 */
	@Override
	public void visit(InitDeclarator n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= StructDeclaratorList()
	 * f2 ::= ";"
	 */
	@Override
	public void visit(StructDeclaration n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( ASpecifierQualifier() )+
	 */
	@Override
	public void visit(SpecifierQualifierList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public void visit(ASpecifierQualifier n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= StructDeclarator()
	 * f1 ::= ( "," StructDeclarator() )*
	 */
	@Override
	public void visit(StructDeclaratorList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= StructDeclaratorWithDeclarator()
	 * | StructDeclaratorWithBitField()
	 */
	@Override
	public void visit(StructDeclarator n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( ":" ConstantExpression() )?
	 */
	@Override
	public void visit(StructDeclaratorWithDeclarator n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ":"
	 * f1 ::= ConstantExpression()
	 */
	@Override
	public void visit(StructDeclaratorWithBitField n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= EnumSpecifierWithList()
	 * | EnumSpecifierWithId()
	 */
	@Override
	public void visit(EnumSpecifier n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= ( <IDENTIFIER> )?
	 * f2 ::= "{"
	 * f3 ::= EnumeratorList()
	 * f4 ::= "}"
	 */
	@Override
	public void visit(EnumSpecifierWithList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(EnumSpecifierWithId n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= Enumerator()
	 * f1 ::= ( "," Enumerator() )*
	 */
	@Override
	public void visit(EnumeratorList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "=" ConstantExpression() )?
	 */
	@Override
	public void visit(Enumerator n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( Pointer() )?
	 * f1 ::= DirectDeclarator()
	 */
	@Override
	public void visit(Declarator n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= IdentifierOrDeclarator()
	 * f1 ::= DeclaratorOpList()
	 */
	@Override
	public void visit(DirectDeclarator n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( ADeclaratorOp() )*
	 */
	@Override
	public void visit(DeclaratorOpList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= DimensionSize()
	 * | ParameterTypeListClosed()
	 * | OldParameterListClosed()
	 */
	@Override
	public void visit(ADeclaratorOp n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "["
	 * f1 ::= ( ConstantExpression() )?
	 * f2 ::= "]"
	 */
	@Override
	public void visit(DimensionSize n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ParameterTypeList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ParameterTypeListClosed n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( OldParameterList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(OldParameterListClosed n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | "(" Declarator() ")"
	 */
	@Override
	public void visit(IdentifierOrDeclarator n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( "*" | "^" )
	 * f1 ::= ( TypeQualifierList() )?
	 * f2 ::= ( Pointer() )?
	 */
	@Override
	public void visit(Pointer n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( TypeQualifier() )+
	 */
	@Override
	public void visit(TypeQualifierList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ParameterList()
	 * f1 ::= ( "," "..." )?
	 */
	@Override
	public void visit(ParameterTypeList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ParameterDeclaration()
	 * f1 ::= ( "," ParameterDeclaration() )*
	 */
	@Override
	public void visit(ParameterList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ParameterAbstraction()
	 */
	@Override
	public void visit(ParameterDeclaration n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= Declarator()
	 * | AbstractOptionalDeclarator()
	 */
	@Override
	public void visit(ParameterAbstraction n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public void visit(AbstractOptionalDeclarator n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "," <IDENTIFIER> )*
	 */
	@Override
	public void visit(OldParameterList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * | ArrayInitializer()
	 */
	@Override
	public void visit(Initializer n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= InitializerList()
	 * f2 ::= ( "," )?
	 * f3 ::= "}"
	 */
	@Override
	public void visit(ArrayInitializer n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= Initializer()
	 * f1 ::= ( "," Initializer() )*
	 */
	@Override
	public void visit(InitializerList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public void visit(TypeName n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= AbstractDeclaratorWithPointer()
	 * | DirectAbstractDeclarator()
	 */
	@Override
	public void visit(AbstractDeclarator n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= Pointer()
	 * f1 ::= ( DirectAbstractDeclarator() )?
	 */
	@Override
	public void visit(AbstractDeclaratorWithPointer n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= AbstractDimensionOrParameter()
	 * f1 ::= DimensionOrParameterList()
	 */
	@Override
	public void visit(DirectAbstractDeclarator n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= AbstractDeclaratorClosed()
	 * | DimensionSize()
	 * | ParameterTypeListClosed()
	 */
	@Override
	public void visit(AbstractDimensionOrParameter n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= AbstractDeclarator()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(AbstractDeclaratorClosed n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( ADimensionOrParameter() )*
	 */
	@Override
	public void visit(DimensionOrParameterList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= DimensionSize()
	 * | ParameterTypeListClosed()
	 */
	@Override
	public void visit(ADimensionOrParameter n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(TypedefName n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( LabeledStatement() | ExpressionStatement() | CompoundStatement()
	 * | SelectionStatement() | IterationStatement() | JumpStatement() |
	 * UnknownPragma() | OmpConstruct() | OmpDirective() | UnknownCpp() )
	 */
	@Override
	public void visit(Statement n) {
		initProcess(n);
		n.getStmtF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public void visit(UnknownCpp n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <OMP_CR>
	 * | <OMP_NL>
	 */
	@Override
	public void visit(OmpEol n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
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
	public void visit(OmpConstruct n) {
		initProcess(n);
		n.getOmpConsF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= BarrierDirective()
	 * | TaskwaitDirective()
	 * | TaskyieldDirective()
	 * | FlushDirective()
	 */
	@Override
	public void visit(OmpDirective n) {
		initProcess(n);
		n.getOmpDirF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(ParallelConstruct n) {
		initProcess(n);
		n.getParConsF0().accept(this);
		n.getParConsF1().accept(this);
		n.getParConsF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <OMP>
	 */
	@Override
	public void visit(OmpPragma n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public void visit(UnknownPragma n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <PARALLEL>
	 * f1 ::= UniqueParallelOrDataClauseList()
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(ParallelDirective n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( AUniqueParallelOrDataClause() )*
	 */
	@Override
	public void visit(UniqueParallelOrDataClauseList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= UniqueParallelClause()
	 * | DataClause()
	 */
	@Override
	public void visit(AUniqueParallelOrDataClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= IfClause()
	 * | NumThreadsClause()
	 */
	@Override
	public void visit(UniqueParallelClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(IfClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(NumThreadsClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
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
	public void visit(DataClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <PRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpPrivateClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <FIRSTPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpFirstPrivateClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <LASTPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpLastPrivateClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <SHARED>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpSharedClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <COPYIN>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpCopyinClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= "("
	 * f2 ::= <SHARED>
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpDfltSharedClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= "("
	 * f2 ::= <NONE>
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpDfltNoneClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
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
	public void visit(OmpReductionClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		n.getF5().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(ForConstruct n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <FOR>
	 * f1 ::= UniqueForOrDataOrNowaitClauseList()
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(ForDirective n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( AUniqueForOrDataOrNowaitClause() )*
	 */
	@Override
	public void visit(UniqueForOrDataOrNowaitClauseList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= UniqueForClause()
	 * | DataClause()
	 * | NowaitClause()
	 */
	@Override
	public void visit(AUniqueForOrDataOrNowaitClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <NOWAIT>
	 */
	@Override
	public void visit(NowaitClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <ORDERED>
	 * | UniqueForClauseSchedule()
	 * | UniqueForCollapse()
	 */
	@Override
	public void visit(UniqueForClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <COLLAPSE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(UniqueForCollapse n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <SCHEDULE>
	 * f1 ::= "("
	 * f2 ::= ScheduleKind()
	 * f3 ::= ( "," Expression() )?
	 * f4 ::= ")"
	 */
	@Override
	public void visit(UniqueForClauseSchedule n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <STATIC>
	 * | <DYNAMIC>
	 * | <GUIDED>
	 * | <RUNTIME>
	 */
	@Override
	public void visit(ScheduleKind n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
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
	public void visit(OmpForHeader n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		n.getF5().accept(this);
		n.getF6().accept(this);
		n.getF7().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForInitExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	@Override
	public void visit(OmpForCondition n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<"
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForLTCondition n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForLECondition n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">"
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForGTCondition n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForGECondition n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
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
	public void visit(OmpForReinitExpression n) {
		initProcess(n);
		n.getOmpForReinitF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "++"
	 */
	@Override
	public void visit(PostIncrementId n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "--"
	 */
	@Override
	public void visit(PostDecrementId n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(PreIncrementId n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(PreDecrementId n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "+="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(ShortAssignPlus n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "-="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(ShortAssignMinus n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= <IDENTIFIER>
	 * f3 ::= "+"
	 * f4 ::= AdditiveExpression()
	 */
	@Override
	public void visit(OmpForAdditive n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= <IDENTIFIER>
	 * f3 ::= "-"
	 * f4 ::= AdditiveExpression()
	 */
	@Override
	public void visit(OmpForSubtractive n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= MultiplicativeExpression()
	 * f3 ::= "+"
	 * f4 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(OmpForMultiplicative n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTIONS>
	 * f2 ::= NowaitDataClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= SectionsScope()
	 */
	@Override
	public void visit(SectionsConstruct n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( ANowaitDataClause() )*
	 */
	@Override
	public void visit(NowaitDataClauseList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= NowaitClause()
	 * | DataClause()
	 */
	@Override
	public void visit(ANowaitDataClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( Statement() )?
	 * f2 ::= ( ASection() )*
	 * f3 ::= "}"
	 */
	@Override
	public void visit(SectionsScope n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTION>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(ASection n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SINGLE>
	 * f2 ::= SingleClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SingleConstruct n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( ASingleClause() )*
	 */
	@Override
	public void visit(SingleClauseList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= NowaitClause()
	 * | DataClause()
	 * | OmpCopyPrivateClause()
	 */
	@Override
	public void visit(ASingleClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <COPYPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpCopyPrivateClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASK>
	 * f2 ::= ( TaskClause() )*
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(TaskConstruct n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= DataClause()
	 * | UniqueTaskClause()
	 */
	@Override
	public void visit(TaskClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= IfClause()
	 * | FinalClause()
	 * | UntiedClause()
	 * | MergeableClause()
	 */
	@Override
	public void visit(UniqueTaskClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(FinalClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <UNTIED>
	 */
	@Override
	public void visit(UntiedClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <MERGEABLE>
	 */
	@Override
	public void visit(MergeableClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
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
	public void visit(ParallelForConstruct n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		n.getF5().accept(this);
		n.getF6().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( AUniqueParallelOrUniqueForOrDataClause() )*
	 */
	@Override
	public void visit(UniqueParallelOrUniqueForOrDataClauseList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= UniqueParallelClause()
	 * | UniqueForClause()
	 * | DataClause()
	 */
	@Override
	public void visit(AUniqueParallelOrUniqueForOrDataClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
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
	public void visit(ParallelSectionsConstruct n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		n.getF5().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(MasterConstruct n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(CriticalConstruct n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ")"
	 */
	@Override
	public void visit(RegionPhrase n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= ExpressionStatement()
	 */
	@Override
	public void visit(AtomicConstruct n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <READ>
	 * | <WRITE>
	 * | <UPDATE>
	 * | <CAPTURE>
	 */
	@Override
	public void visit(AtomicClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public void visit(FlushDirective n) {
		initProcess(n);
		if (!(n instanceof DummyFlushDirective)) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
		}
		endProcess(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= VariableList()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(FlushVars n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(OrderedConstruct n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(BarrierDirective n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskwaitDirective n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskyieldDirective n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
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
	public void visit(ThreadPrivateDirective n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		n.getF5().accept(this);
		endProcess(n);
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
	public void visit(DeclareReductionDirective n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		n.getF5().accept(this);
		n.getF6().accept(this);
		n.getF7().accept(this);
		n.getF8().accept(this);
		n.getF9().accept(this);
		n.getF10().accept(this);
		n.getF11().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( TypeSpecifier() )*
	 */
	@Override
	public void visit(ReductionTypeList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= AssignInitializerClause()
	 * | ArgumentInitializerClause()
	 */
	@Override
	public void visit(InitializerClause n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
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
	public void visit(AssignInitializerClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		n.getF5().accept(this);
		endProcess(n);
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
	public void visit(ArgumentInitializerClause n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		n.getF5().accept(this);
		n.getF6().accept(this);
		endProcess(n);
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
	public void visit(ReductionOp n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "," <IDENTIFIER> )*
	 */
	@Override
	public void visit(VariableList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= SimpleLabeledStatement()
	 * | CaseLabeledStatement()
	 * | DefaultLabeledStatement()
	 */
	@Override
	public void visit(LabeledStatement n) {
		initProcess(n);
		n.getLabStmtF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(SimpleLabeledStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <CASE>
	 * f1 ::= ConstantExpression()
	 * f2 ::= ":"
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(CaseLabeledStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(DefaultLabeledStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ExpressionStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public void visit(CompoundStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= Declaration()
	 * | Statement()
	 */
	@Override
	public void visit(CompoundStatementElement n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= IfStatement()
	 * | SwitchStatement()
	 */
	@Override
	public void visit(SelectionStatement n) {
		initProcess(n);
		n.getSelStmtF0().accept(this);
		endProcess(n);
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
	public void visit(IfStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		n.getF5().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(SwitchStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= WhileStatement()
	 * | DoStatement()
	 * | ForStatement()
	 */
	@Override
	public void visit(IterationStatement n) {
		initProcess(n);
		n.getItStmtF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <WHILE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public void visit(WhileStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		endProcess(n);
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
	public void visit(DoStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		n.getF5().accept(this);
		n.getF6().accept(this);
		endProcess(n);
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
	public void visit(ForStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		n.getF4().accept(this);
		n.getF5().accept(this);
		n.getF6().accept(this);
		n.getF7().accept(this);
		n.getF8().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= GotoStatement()
	 * | ContinueStatement()
	 * | BreakStatement()
	 * | ReturnStatement()
	 */
	@Override
	public void visit(JumpStatement n) {
		initProcess(n);
		n.getJumpStmtF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public void visit(GotoStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ContinueStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(BreakStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(ReturnStatement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(Expression n) {
		initProcess(n);
		n.getExpF0().accept(this);
		n.getExpF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= NonConditionalExpression()
	 * | ConditionalExpression()
	 */
	@Override
	public void visit(AssignmentExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= UnaryExpression()
	 * f1 ::= AssignmentOperator()
	 * f2 ::= AssignmentExpression()
	 */
	@Override
	public void visit(NonConditionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
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
	public void visit(AssignmentOperator n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= LogicalORExpression()
	 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
	 */
	@Override
	public void visit(ConditionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ConditionalExpression()
	 */
	@Override
	public void visit(ConstantExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= LogicalANDExpression()
	 * f1 ::= ( "||" LogicalORExpression() )?
	 */
	@Override
	public void visit(LogicalORExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= InclusiveORExpression()
	 * f1 ::= ( "&&" LogicalANDExpression() )?
	 */
	@Override
	public void visit(LogicalANDExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ExclusiveORExpression()
	 * f1 ::= ( "|" InclusiveORExpression() )?
	 */
	@Override
	public void visit(InclusiveORExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ANDExpression()
	 * f1 ::= ( "^" ExclusiveORExpression() )?
	 */
	@Override
	public void visit(ExclusiveORExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= EqualityExpression()
	 * f1 ::= ( "&" ANDExpression() )?
	 */
	@Override
	public void visit(ANDExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= RelationalExpression()
	 * f1 ::= ( EqualOptionalExpression() )?
	 */
	@Override
	public void visit(EqualityExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= EqualExpression()
	 * | NonEqualExpression()
	 */
	@Override
	public void visit(EqualOptionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "=="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public void visit(EqualExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "!="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public void visit(NonEqualExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ShiftExpression()
	 * f1 ::= ( RelationalOptionalExpression() )?
	 */
	@Override
	public void visit(RelationalExpression n) {
		initProcess(n);
		n.getRelExpF0().accept(this);
		n.getRelExpF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= RelationalLTExpression()
	 * | RelationalGTExpression()
	 * | RelationalLEExpression()
	 * | RelationalGEExpression()
	 */
	@Override
	public void visit(RelationalOptionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "<"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalLTExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ">"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalGTExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "<="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalLEExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ">="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public void visit(RelationalGEExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= AdditiveExpression()
	 * f1 ::= ( ShiftOptionalExpression() )?
	 */
	@Override
	public void visit(ShiftExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ShiftLeftExpression()
	 * | ShiftRightExpression()
	 */
	@Override
	public void visit(ShiftOptionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ">>"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public void visit(ShiftLeftExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "<<"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public void visit(ShiftRightExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= MultiplicativeExpression()
	 * f1 ::= ( AdditiveOptionalExpression() )?
	 */
	@Override
	public void visit(AdditiveExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= AdditivePlusExpression()
	 * | AdditiveMinusExpression()
	 */
	@Override
	public void visit(AdditiveOptionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "+"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public void visit(AdditivePlusExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "-"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public void visit(AdditiveMinusExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= CastExpression()
	 * f1 ::= ( MultiplicativeOptionalExpression() )?
	 */
	@Override
	public void visit(MultiplicativeExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= MultiplicativeMultiExpression()
	 * | MultiplicativeDivExpression()
	 * | MultiplicativeModExpression()
	 */
	@Override
	public void visit(MultiplicativeOptionalExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "*"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeMultiExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "/"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeDivExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "%"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public void visit(MultiplicativeModExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= CastExpressionTyped()
	 * | UnaryExpression()
	 */
	@Override
	public void visit(CastExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= TypeName()
	 * f2 ::= ")"
	 * f3 ::= CastExpression()
	 */
	@Override
	public void visit(CastExpressionTyped n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= UnaryExpressionPreIncrement()
	 * | UnaryExpressionPreDecrement()
	 * | UnarySizeofExpression()
	 * | UnaryCastExpression()
	 * | PostfixExpression()
	 */
	@Override
	public void visit(UnaryExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(UnaryExpressionPreIncrement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(UnaryExpressionPreDecrement n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= UnaryOperator()
	 * f1 ::= CastExpression()
	 */
	@Override
	public void visit(UnaryCastExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= SizeofTypeName()
	 * | SizeofUnaryExpression()
	 */
	@Override
	public void visit(UnarySizeofExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public void visit(SizeofUnaryExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= "("
	 * f2 ::= TypeName()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(SizeofTypeName n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		endProcess(n);
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
	public void visit(UnaryOperator n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public void visit(PostfixExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= ( APostfixOperation() )*
	 */
	@Override
	public void visit(PostfixOperationsList n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
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
	public void visit(APostfixOperation n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "++"
	 */
	@Override
	public void visit(PlusPlus n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "--"
	 */
	@Override
	public void visit(MinusMinus n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "["
	 * f1 ::= Expression()
	 * f2 ::= "]"
	 */
	@Override
	public void visit(BracketExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ExpressionList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ArgumentList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "."
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(DotId n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "->"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(ArrowId n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | Constant()
	 * | ExpressionClosed()
	 */
	@Override
	public void visit(PrimaryExpression n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= Expression()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ExpressionClosed n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(ExpressionList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

	/**
	 * f0 ::= <INTEGER_LITERAL>
	 * | <FLOATING_POINT_LITERAL>
	 * | <CHARACTER_LITERAL>
	 * | ( <STRING_LITERAL> )+
	 */
	@Override
	public void visit(Constant n) {
		initProcess(n);
		n.getF0().accept(this);
		endProcess(n);
	}

	@Override
	public void visit(DummyFlushDirective n) {
		initProcess(n);
		endProcess(n);
	}

	@Override
	public void visit(CallStatement n) {
		initProcess(n);
		n.getPreCallNode().accept(this);
		n.getPostCallNode().accept(this);
		endProcess(n);
	}

	@Override
	public void visit(PreCallNode n) {
		initProcess(n);
		for (SimplePrimaryExpression arg : n.getArgumentList()) {
			arg.accept(this);
		}
		endProcess(n);
	}

	@Override
	public void visit(PostCallNode n) {
		initProcess(n);
		if (n.hasReturnReceiver()) {
			n.getReturnReceiver().accept(this);
		}
		endProcess(n);
	}

	@Override
	public void visit(SimplePrimaryExpression n) {
		initProcess(n);
		if (n.isAConstant()) {
			n.getConstant().accept(this);
		} else if (n.isAnIdentifier()) {
			n.getIdentifier().accept(this);
		}
		endProcess(n);
	}

	@Override
	public void visit(BeginNode n) {
		initProcess(n);
		endProcess(n);
	}

	@Override
	public void visit(EndNode n) {
		initProcess(n);
		endProcess(n);
	}
}
