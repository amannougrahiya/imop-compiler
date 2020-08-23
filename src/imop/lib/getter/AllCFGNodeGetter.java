/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * A visitor that accumulates ALL the CFG nodes encountered in the depth-first
 * visit, along with beginNode and endNode of the non-leaf CFG nodes.
 */
public class AllCFGNodeGetter extends DepthFirstProcess {
	public List<Node> cfgNodeList = new ArrayList<>();

	@Override
	public void visit(NodeList n) {
		for (Node e : n.getNodes()) {
			e.accept(this);
		}
	}

	@Override
	public void visit(NodeListOptional n) {
		if (n.present()) {
			for (Node e : n.getNodes()) {
				e.accept(this);
			}
		}
	}

	@Override
	public void visit(NodeOptional n) {
		if (n.present()) {
			n.getNode().accept(this);
		}
	}

	@Override
	public void visit(NodeSequence n) {
		for (Node e : n.getNodes()) {
			e.accept(this);
		}
	}

	@Override
	public void visit(NodeChoice n) {
		n.getChoice().accept(this);
	}

	@Override
	public void visit(NodeToken n) {
	}

	//
	// User-generated visitor methods below
	//

	/**
	 * f0 ::= ( ElementsOfTranslation() )+
	 */
	@Override
	public void visit(TranslationUnit n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= ExternalDeclaration()
	 * | UnknownCpp()
	 * | UnknownPragma()
	 */
	@Override
	public void visit(ElementsOfTranslation n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= Declaration()
	 * | FunctionDefinition()
	 * | DeclareReductionDirective()
	 * | ThreadPrivateDirective()
	 */
	@Override
	public void visit(ExternalDeclaration n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public void visit(FunctionDefinition n) {
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		n.getF1().accept(this);
		n.getF3().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(Declaration n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= ( Declaration() )+
	 */
	@Override
	public void visit(DeclarationList n) {
	}

	/**
	 * f0 ::= ( ADeclarationSpecifier() )+
	 */
	@Override
	public void visit(DeclarationSpecifiers n) {
	}

	/**
	 * f0 ::= StorageClassSpecifier()
	 * | TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public void visit(ADeclarationSpecifier n) {
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
	}

	/**
	 * f0 ::= ( <VOID> | <CHAR> | <SHORT> | <INT> | <LONG> | <FLOAT> | <DOUBLE>
	 * |
	 * <SIGNED> | <UNSIGNED> | StructOrUnionSpecifier() | EnumSpecifier() |
	 * TypedefName() )
	 */
	@Override
	public void visit(TypeSpecifier n) {
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
	}

	/**
	 * 
	 * f0 ::= ( StructOrUnionSpecifierWithList() |
	 * StructOrUnionSpecifierWithId()
	 * )
	 */
	@Override
	public void visit(StructOrUnionSpecifier n) {
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
	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(StructOrUnionSpecifierWithId n) {
	}

	/**
	 * f0 ::= <STRUCT>
	 * | <UNION>
	 */
	@Override
	public void visit(StructOrUnion n) {
	}

	/**
	 * f0 ::= ( StructDeclaration() )+
	 */
	@Override
	public void visit(StructDeclarationList n) {
	}

	/**
	 * f0 ::= InitDeclarator()
	 * f1 ::= ( "," InitDeclarator() )*
	 */
	@Override
	public void visit(InitDeclaratorList n) {
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( "=" Initializer() )?
	 */
	@Override
	public void visit(InitDeclarator n) {
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= StructDeclaratorList()
	 * f2 ::= ";"
	 */
	@Override
	public void visit(StructDeclaration n) {
	}

	/**
	 * f0 ::= ( ASpecifierQualifier() )+
	 */
	@Override
	public void visit(SpecifierQualifierList n) {
	}

	/**
	 * f0 ::= TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public void visit(ASpecifierQualifier n) {
	}

	/**
	 * f0 ::= StructDeclarator()
	 * f1 ::= ( "," StructDeclarator() )*
	 */
	@Override
	public void visit(StructDeclaratorList n) {
	}

	/**
	 * f0 ::= StructDeclaratorWithDeclarator()
	 * | StructDeclaratorWithBitField()
	 */
	@Override
	public void visit(StructDeclarator n) {
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( ":" ConstantExpression() )?
	 */
	@Override
	public void visit(StructDeclaratorWithDeclarator n) {
	}

	/**
	 * f0 ::= ":"
	 * f1 ::= ConstantExpression()
	 */
	@Override
	public void visit(StructDeclaratorWithBitField n) {
	}

	/**
	 * f0 ::= EnumSpecifierWithList()
	 * | EnumSpecifierWithId()
	 */
	@Override
	public void visit(EnumSpecifier n) {
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
	}

	/**
	 * f0 ::= <ENUM>
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(EnumSpecifierWithId n) {
	}

	/**
	 * f0 ::= Enumerator()
	 * f1 ::= ( "," Enumerator() )*
	 */
	@Override
	public void visit(EnumeratorList n) {
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "=" ConstantExpression() )?
	 */
	@Override
	public void visit(Enumerator n) {
	}

	/**
	 * f0 ::= ( Pointer() )?
	 * f1 ::= DirectDeclarator()
	 */
	@Override
	public void visit(Declarator n) {
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= IdentifierOrDeclarator()
	 * f1 ::= DeclaratorOpList()
	 */
	@Override
	public void visit(DirectDeclarator n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ( ADeclaratorOp() )*
	 */
	@Override
	public void visit(DeclaratorOpList n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= DimensionSize()
	 * | ParameterTypeListClosed()
	 * | OldParameterListClosed()
	 */
	@Override
	public void visit(ADeclaratorOp n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "["
	 * f1 ::= ( ConstantExpression() )?
	 * f2 ::= "]"
	 */
	@Override
	public void visit(DimensionSize n) {
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ParameterTypeList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(ParameterTypeListClosed n) {
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( OldParameterList() )?
	 * f2 ::= ")"
	 */
	@Override
	public void visit(OldParameterListClosed n) {
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | "(" Declarator() ")"
	 */
	@Override
	public void visit(IdentifierOrDeclarator n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= ( "*" | "^" )
	 * f1 ::= ( TypeQualifierList() )?
	 * f2 ::= ( Pointer() )?
	 */
	@Override
	public void visit(Pointer n) {
	}

	/**
	 * f0 ::= ( TypeQualifier() )+
	 */
	@Override
	public void visit(TypeQualifierList n) {
	}

	/**
	 * f0 ::= ParameterList()
	 * f1 ::= ( "," "..." )?
	 */
	@Override
	public void visit(ParameterTypeList n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= ParameterDeclaration()
	 * f1 ::= ( "," ParameterDeclaration() )*
	 */
	@Override
	public void visit(ParameterList n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ParameterAbstraction()
	 */
	@Override
	public void visit(ParameterDeclaration n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= Declarator()
	 * | AbstractOptionalDeclarator()
	 */
	@Override
	public void visit(ParameterAbstraction n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public void visit(AbstractOptionalDeclarator n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "," <IDENTIFIER> )*
	 */
	@Override
	public void visit(OldParameterList n) {
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * | ArrayInitializer()
	 */
	@Override
	public void visit(Initializer n) {
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= InitializerList()
	 * f2 ::= ( "," )?
	 * f3 ::= "}"
	 */
	@Override
	public void visit(ArrayInitializer n) {
	}

	/**
	 * f0 ::= Initializer()
	 * f1 ::= ( "," Initializer() )*
	 */
	@Override
	public void visit(InitializerList n) {
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public void visit(TypeName n) {
	}

	/**
	 * f0 ::= AbstractDeclaratorWithPointer()
	 * | DirectAbstractDeclarator()
	 */
	@Override
	public void visit(AbstractDeclarator n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= Pointer()
	 * f1 ::= ( DirectAbstractDeclarator() )?
	 */
	@Override
	public void visit(AbstractDeclaratorWithPointer n) {
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= AbstractDimensionOrParameter()
	 * f1 ::= DimensionOrParameterList()
	 */
	@Override
	public void visit(DirectAbstractDeclarator n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= AbstractDeclaratorClosed()
	 * | DimensionSize()
	 * | ParameterTypeListClosed()
	 */
	@Override
	public void visit(AbstractDimensionOrParameter n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= AbstractDeclarator()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(AbstractDeclaratorClosed n) {
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ( ADimensionOrParameter() )*
	 */
	@Override
	public void visit(DimensionOrParameterList n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= DimensionSize()
	 * | ParameterTypeListClosed()
	 */
	@Override
	public void visit(ADimensionOrParameter n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(TypedefName n) {
	}

	/**
	 * f0 ::= ( LabeledStatement() | ExpressionStatement() | CompoundStatement()
	 * | SelectionStatement() | IterationStatement() | JumpStatement() |
	 * UnknownPragma() | OmpConstruct() | OmpDirective() | UnknownCpp() )
	 */
	@Override
	public void visit(Statement n) {
		n.getStmtF0().accept(this);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public void visit(UnknownCpp n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= <OMP_CR>
	 * | <OMP_NL>
	 */
	@Override
	public void visit(OmpEol n) {
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
		n.getOmpConsF0().accept(this);
	}

	/**
	 * f0 ::= BarrierDirective()
	 * | TaskwaitDirective()
	 * | TaskyieldDirective()
	 * | FlushDirective()
	 */
	@Override
	public void visit(OmpDirective n) {
		n.getOmpDirF0().accept(this);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(ParallelConstruct n) {
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		n.getParConsF1().accept(this);
		n.getParConsF2().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <OMP>
	 */
	@Override
	public void visit(OmpPragma n) {
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public void visit(UnknownPragma n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= <PARALLEL>
	 * f1 ::= UniqueParallelOrDataClauseList()
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(ParallelDirective n) {
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ( AUniqueParallelOrDataClause() )*
	 */
	@Override
	public void visit(UniqueParallelOrDataClauseList n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= UniqueParallelClause()
	 * | DataClause()
	 */
	@Override
	public void visit(AUniqueParallelOrDataClause n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= IfClause()
	 * | NumThreadsClause()
	 */
	@Override
	public void visit(UniqueParallelClause n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(IfClause n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(NumThreadsClause n) {
		cfgNodeList.add(n);
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
	}

	/**
	 * f0 ::= <PRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpPrivateClause n) {
	}

	/**
	 * f0 ::= <FIRSTPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpFirstPrivateClause n) {
	}

	/**
	 * f0 ::= <LASTPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpLastPrivateClause n) {
	}

	/**
	 * f0 ::= <SHARED>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpSharedClause n) {
	}

	/**
	 * f0 ::= <COPYIN>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpCopyinClause n) {
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= "("
	 * f2 ::= <SHARED>
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpDfltSharedClause n) {
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= "("
	 * f2 ::= <NONE>
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpDfltNoneClause n) {
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
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(ForConstruct n) {
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= <FOR>
	 * f1 ::= UniqueForOrDataOrNowaitClauseList()
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(ForDirective n) {
		n.getF1().accept(this);
	}

	/**
	 * f0 ::= ( AUniqueForOrDataOrNowaitClause() )*
	 */
	@Override
	public void visit(UniqueForOrDataOrNowaitClauseList n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= UniqueForClause()
	 * | DataClause()
	 * | NowaitClause()
	 */
	@Override
	public void visit(AUniqueForOrDataOrNowaitClause n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= <NOWAIT>
	 */
	@Override
	public void visit(NowaitClause n) {
	}

	/**
	 * f0 ::= <ORDERED>
	 * | UniqueForClauseSchedule()
	 * | UniqueForCollapse()
	 */
	@Override
	public void visit(UniqueForClause n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= <COLLAPSE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(UniqueForCollapse n) {
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
	}

	/**
	 * f0 ::= <STATIC>
	 * | <DYNAMIC>
	 * | <GUIDED>
	 * | <RUNTIME>
	 */
	@Override
	public void visit(ScheduleKind n) {
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
		n.getF2().accept(this);
		n.getF4().accept(this);
		n.getF6().accept(this);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForInitExpression n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	@Override
	public void visit(OmpForCondition n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<"
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForLTCondition n) {
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForLECondition n) {
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">"
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForGTCondition n) {
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(OmpForGECondition n) {
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
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "++"
	 */
	@Override
	public void visit(PostIncrementId n) {
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "--"
	 */
	@Override
	public void visit(PostDecrementId n) {
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(PreIncrementId n) {
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public void visit(PreDecrementId n) {
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "+="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(ShortAssignPlus n) {
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "-="
	 * f2 ::= Expression()
	 */
	@Override
	public void visit(ShortAssignMinus n) {
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
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		n.getF2().accept(this);
		n.getF4().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= ( ANowaitDataClause() )*
	 */
	@Override
	public void visit(NowaitDataClauseList n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= NowaitClause()
	 * | DataClause()
	 */
	@Override
	public void visit(ANowaitDataClause n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( Statement() )?
	 * f2 ::= ( ASection() )*
	 * f3 ::= "}"
	 */
	@Override
	public void visit(SectionsScope n) {
		n.getF0().accept(this);
		n.getF1().accept(this);
		n.getF2().accept(this);
		n.getF3().accept(this);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTION>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(ASection n) {
		n.getF3().accept(this);
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
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		n.getF2().accept(this);
		n.getF4().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= ( ASingleClause() )*
	 */
	@Override
	public void visit(SingleClauseList n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= NowaitClause()
	 * | DataClause()
	 * | OmpCopyPrivateClause()
	 */
	@Override
	public void visit(ASingleClause n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= <COPYPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(OmpCopyPrivateClause n) {
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
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		n.getF2().accept(this);
		n.getF4().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= DataClause()
	 * | UniqueTaskClause()
	 */
	@Override
	public void visit(TaskClause n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= IfClause()
	 * | FinalClause()
	 * | UntiedClause()
	 * | MergeableClause()
	 */
	@Override
	public void visit(UniqueTaskClause n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public void visit(FinalClause n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= <UNTIED>
	 */
	@Override
	public void visit(UntiedClause n) {
	}

	/**
	 * f0 ::= <MERGEABLE>
	 */
	@Override
	public void visit(MergeableClause n) {
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
		n.getF3().accept(this);
		n.getF5().accept(this);
		n.getF6().accept(this);
	}

	/**
	 * f0 ::= ( AUniqueParallelOrUniqueForOrDataClause() )*
	 */
	@Override
	public void visit(UniqueParallelOrUniqueForOrDataClauseList n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= UniqueParallelClause()
	 * | UniqueForClause()
	 * | DataClause()
	 */
	@Override
	public void visit(AUniqueParallelOrUniqueForOrDataClause n) {
		n.getF0().accept(this);
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
		n.getF3().accept(this);
		n.getF5().accept(this);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(MasterConstruct n) {
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		n.getF3().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
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
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		n.getF4().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ")"
	 */
	@Override
	public void visit(RegionPhrase n) {
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
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		n.getF4().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= <READ>
	 * | <WRITE>
	 * | <UPDATE>
	 * | <CAPTURE>
	 */
	@Override
	public void visit(AtomicClause n) {
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public void visit(FlushDirective n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= VariableList()
	 * f2 ::= ")"
	 */
	@Override
	public void visit(FlushVars n) {
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(OrderedConstruct n) {
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		n.getF3().accept(this);
		cfgNodeList.add(n);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(BarrierDirective n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskwaitDirective n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public void visit(TaskyieldDirective n) {
		cfgNodeList.add(n);
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
	}

	/**
	 * f0 ::= ( TypeSpecifier() )*
	 */
	@Override
	public void visit(ReductionTypeList n) {
	}

	/**
	 * f0 ::= AssignInitializerClause()
	 * | ArgumentInitializerClause()
	 */
	@Override
	public void visit(InitializerClause n) {
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
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "," <IDENTIFIER> )*
	 */
	@Override
	public void visit(VariableList n) {
	}

	/**
	 * f0 ::= SimpleLabeledStatement()
	 * | CaseLabeledStatement()
	 * | DefaultLabeledStatement()
	 */
	@Override
	public void visit(LabeledStatement n) {
		n.getLabStmtF0().accept(this);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(SimpleLabeledStatement n) {
		n.getF2().accept(this);
	}

	/**
	 * f0 ::= <CASE>
	 * f1 ::= ConstantExpression()
	 * f2 ::= ":"
	 * f3 ::= Statement()
	 */
	@Override
	public void visit(CaseLabeledStatement n) {
		n.getF3().accept(this);
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(DefaultLabeledStatement n) {
		n.getF2().accept(this);
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ExpressionStatement n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public void visit(CompoundStatement n) {
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		n.getF1().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= Declaration()
	 * | Statement()
	 */
	@Override
	public void visit(CompoundStatementElement n) {
		n.getF0().accept(this);
	}

	/**
	 * f0 ::= IfStatement()
	 * | SwitchStatement()
	 */
	@Override
	public void visit(SelectionStatement n) {
		n.getSelStmtF0().accept(this);
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
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		cfgNodeList.add(n.getF2());
		n.getF4().accept(this);
		n.getF5().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
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
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		cfgNodeList.add(n.getF2());
		n.getF4().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= WhileStatement()
	 * | DoStatement()
	 * | ForStatement()
	 */
	@Override
	public void visit(IterationStatement n) {
		n.getItStmtF0().accept(this);
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
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		cfgNodeList.add(n.getF2());
		n.getF4().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
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
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		n.getF1().accept(this);
		cfgNodeList.add(n.getF4());
		n.getF5().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
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
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		if (n.getF4().present()) {
			cfgNodeList.add(n.getF4().getNode());
		}
		if (n.getF2().present()) {
			cfgNodeList.add(n.getF2().getNode());
		}
		if (n.getF6().present()) {
			cfgNodeList.add(n.getF6().getNode());
		}
		n.getF8().accept(this);
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= GotoStatement()
	 * | ContinueStatement()
	 * | BreakStatement()
	 * | ReturnStatement()
	 */
	@Override
	public void visit(JumpStatement n) {
		n.getJumpStmtF0().accept(this);
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public void visit(GotoStatement n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(ContinueStatement n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public void visit(BreakStatement n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public void visit(ReturnStatement n) {
		cfgNodeList.add(n);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public void visit(Expression n) {
		if (Misc.isACFGExpression(n)) {
			cfgNodeList.add(n);
		}
	}

	@Override
	public void visit(DummyFlushDirective n) {
		cfgNodeList.add(n);
	}

	@Override
	public void visit(CallStatement n) {
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getBegin());
		cfgNodeList.add(n.getPreCallNode());
		cfgNodeList.add(n.getPostCallNode());
		cfgNodeList.add(n.getInfo().getCFGInfo().getNestedCFG().getEnd());
		cfgNodeList.add(n);
	}

	@Override
	public void visit(PreCallNode n) {
		cfgNodeList.add(n);
	}

	@Override
	public void visit(PostCallNode n) {
		cfgNodeList.add(n);
	}

	@Override
	public void visit(SimplePrimaryExpression n) {
	}

	@Override
	public void visit(BeginNode n) {
		cfgNodeList.add(n);
	}

	@Override
	public void visit(EndNode n) {
		cfgNodeList.add(n);
	}

}
