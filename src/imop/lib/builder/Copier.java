/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.builder;

import imop.ast.annotation.Label;
import imop.ast.annotation.SimpleLabel;
import imop.ast.info.StatementInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.GJNoArguDepthFirstProcess;
import imop.lib.cfg.CFGGenerator;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Performs deep-copy of the visited node.
 */
public class Copier {
	private static DeepCopier deepCopier = new Copier.DeepCopier();

	/**
	 * After performing a deep copy of node {@code baseNode} to
	 * {@code targetNode},
	 * this method initializes the various fields accessible via Node and Info
	 * objects.
	 * 
	 * @param baseNode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Node> T getDeepCopy(T baseNode) {
		T targetNode;
		targetNode = (T) baseNode.accept(deepCopier);
		// // 3. Setting the parent pointer of the targetNode.
		// targetNode.parent = baseNode.parent;
		CFGGenerator.createCFGEdgesIn(targetNode);
		return targetNode;
	}

	private static class DeepCopier extends GJNoArguDepthFirstProcess<Node> {
		private String tempName;

		public DeepCopier() {
			tempName = Builder.getNewTempName();
		}

		/**
		 * Copy certain fields of Info from {@code oldNode} to
		 * {@code newNode}.<br>
		 * (Note that this method performs only shallow copy, i.e., copy of
		 * information
		 * from just oldNode to newNode, and not from their fields to fields.
		 * The latter part is automatically taken care of, recursively.)<br>
		 * 
		 * @param oldNode
		 *                old node, from which certain information has to be copied.
		 * @param newNode
		 *                new node, to which certain information has to be copied.
		 */
		public void copyInfo(Node oldNode, Node newNode) {
			assert (oldNode.getClass() == newNode.getClass());

			// 1. Renaming the destinations of GotoStatement.
			if (oldNode instanceof GotoStatement) {
				GotoStatement newGotoStmt = (GotoStatement) newNode;
				newGotoStmt.getF1().setTokenImage(newGotoStmt.getF1().getTokenImage() + tempName);
			}

			// 2. Copying the labels (renamed).
			if (oldNode instanceof Statement) {
				Statement oldStmt = (Statement) oldNode;
				if (oldStmt.getInfo().hasLabelAnnotations() && Misc.isCFGNode(oldStmt)) {
					for (Label oldLabel : oldStmt.getInfo().getLabelAnnotations()) {
						if (oldLabel instanceof SimpleLabel) {
							SimpleLabel simpleOldLabel = (SimpleLabel) oldLabel;
							SimpleLabel simpleNewLabel = new SimpleLabel((Statement) newNode,
									simpleOldLabel.getLabelName() + tempName);
							((StatementInfo) newNode.getInfo()).addLabelAnnotation(simpleNewLabel);
						}
					}
				}
			}

			// Info newInfo = newNode.getInfoCopied(oldNode);
			// Info oldInfo = oldNode.getInfo();
			// newNode.isCFGNode = oldNode.isCFGNode;
			// newInfo.idNumber = oldInfo.idNumber;
			// newNode.getInfo().getCFGInfo().clearAllEdges();
			// newNode.getInfo().setInfo(oldNode.getInfo());
		}

		@Override
		public Node visit(NodeList oldNode) {
			Node newNode = null;
			List<Node> nodesCopy = new ArrayList<>();
			for (Node e : oldNode.getNodes()) {
				nodesCopy.add(e.accept(this));
			}
			newNode = new NodeList(nodesCopy);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		@Override
		public Node visit(NodeListOptional oldNode) {
			Node newNode = null;
			List<Node> nodesCopy = new ArrayList<>();
			for (Node e : oldNode.getNodes()) {
				nodesCopy.add(e.accept(this));
			}
			newNode = new NodeListOptional(nodesCopy);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		@Override
		public Node visit(NodeOptional oldNode) {
			Node newNode = null;
			if (oldNode.present()) {
				newNode = oldNode.getNode().accept(this);
			}
			newNode = new NodeOptional(newNode);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		@Override
		public Node visit(NodeSequence oldNode) {
			Node newNode = null;
			List<Node> nodesCopy = new ArrayList<>();
			for (Node e : oldNode.getNodes()) {
				nodesCopy.add(e.accept(this));
			}
			newNode = new NodeSequence(nodesCopy);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		@Override
		public Node visit(NodeChoice oldNode) {
			Node newNode = null;
			// if (oldNode.getChoice() == null) {
			// System.out.println(oldNode.getParent().getInfo().getString());
			// }
			newNode = oldNode.getChoice().accept(this);
			newNode = new NodeChoice(newNode);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		@Override
		public Node visit(NodeToken oldNode) {
			Node newNode = new NodeToken(oldNode.getTokenImage());
			copyInfo(oldNode, newNode);
			return newNode;
		}

		//
		// User-generated visitor methods below
		//

		/**
		 * f0 ::= ( ElementsOfTranslation() )+
		 */
		@Override
		public Node visit(TranslationUnit oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new TranslationUnit((NodeList) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ExternalDeclaration()
		 * | UnknownCpp()
		 * | UnknownPragma()
		 */
		@Override
		public Node visit(ElementsOfTranslation oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ElementsOfTranslation((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= Declaration()
		 * | FunctionDefinition()
		 * | DeclareReductionDirective()
		 * | ThreadPrivateDirective()
		 */
		@Override
		public Node visit(ExternalDeclaration oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ExternalDeclaration((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( DeclarationSpecifiers() )?
		 * f1 ::= Declarator()
		 * f2 ::= ( DeclarationList() )?
		 * f3 ::= CompoundStatement()
		 */
		@Override
		public Node visit(FunctionDefinition oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new FunctionDefinition((NodeOptional) newF0, (Declarator) newF1, (NodeOptional) newF2,
					(CompoundStatement) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ( InitDeclaratorList() )?
		 * f2 ::= ";"
		 */
		@Override
		public Node visit(Declaration oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new Declaration((DeclarationSpecifiers) newF0, (NodeOptional) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( Declaration() )+
		 */
		@Override
		public Node visit(DeclarationList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new DeclarationList((NodeList) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( ADeclarationSpecifier() )+
		 */
		@Override
		public Node visit(DeclarationSpecifiers oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new DeclarationSpecifiers((NodeList) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= StorageClassSpecifier()
		 * | TypeSpecifier()
		 * | TypeQualifier()
		 */
		@Override
		public Node visit(ADeclarationSpecifier oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ADeclarationSpecifier((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <AUTO>
		 * | <REGISTER>
		 * | <STATIC>
		 * | <EXTERN>
		 * | <TYPEDEF>
		 */
		@Override
		public Node visit(StorageClassSpecifier oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new StorageClassSpecifier((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( <VOID> | <CHAR> | <SHORT> | <INT> | <LONG> | <FLOAT> |
		 * <DOUBLE> |
		 * <SIGNED> | <UNSIGNED> | StructOrUnionSpecifier() | EnumSpecifier() |
		 * TypedefName() )
		 */
		@Override
		public Node visit(TypeSpecifier oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new TypeSpecifier((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
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
		public Node visit(TypeQualifier oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new TypeQualifier((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * 
		 * f0 ::= ( StructOrUnionSpecifierWithList() |
		 * StructOrUnionSpecifierWithId()
		 * )
		 */
		@Override
		public Node visit(StructOrUnionSpecifier oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new StructOrUnionSpecifier((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= StructOrUnion()
		 * f1 ::= ( <IDENTIFIER> )?
		 * f2 ::= "{"
		 * f3 ::= StructDeclarationList()
		 * f4 ::= "}"
		 */
		@Override
		public Node visit(StructOrUnionSpecifierWithList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new StructOrUnionSpecifierWithList((StructOrUnion) newF0, (NodeOptional) newF1, (NodeToken) newF2,
					(StructDeclarationList) newF3, (NodeToken) newF4);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= StructOrUnion()
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public Node visit(StructOrUnionSpecifierWithId oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new StructOrUnionSpecifierWithId((StructOrUnion) newF0, (NodeToken) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <STRUCT>
		 * | <UNION>
		 */
		@Override
		public Node visit(StructOrUnion oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new StructOrUnion((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( StructDeclaration() )+
		 */
		@Override
		public Node visit(StructDeclarationList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new StructDeclarationList((NodeList) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= InitDeclarator()
		 * f1 ::= ( "," InitDeclarator() )*
		 */
		@Override
		public Node visit(InitDeclaratorList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new InitDeclaratorList((InitDeclarator) newF0, (NodeListOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= Declarator()
		 * f1 ::= ( "=" Initializer() )?
		 */
		@Override
		public Node visit(InitDeclarator oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new InitDeclarator((Declarator) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= SpecifierQualifierList()
		 * f1 ::= StructDeclaratorList()
		 * f2 ::= ";"
		 */
		@Override
		public Node visit(StructDeclaration oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new StructDeclaration((SpecifierQualifierList) newF0, (StructDeclaratorList) newF1,
					(NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( ASpecifierQualifier() )+
		 */
		@Override
		public Node visit(SpecifierQualifierList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new SpecifierQualifierList((NodeList) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= TypeSpecifier()
		 * | TypeQualifier()
		 */
		@Override
		public Node visit(ASpecifierQualifier oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ASpecifierQualifier((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= StructDeclarator()
		 * f1 ::= ( "," StructDeclarator() )*
		 */
		@Override
		public Node visit(StructDeclaratorList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new StructDeclaratorList((StructDeclarator) newF0, (NodeListOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= StructDeclaratorWithDeclarator()
		 * | StructDeclaratorWithBitField()
		 */
		@Override
		public Node visit(StructDeclarator oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new StructDeclarator((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= Declarator()
		 * f1 ::= ( ":" ConstantExpression() )?
		 */
		@Override
		public Node visit(StructDeclaratorWithDeclarator oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new StructDeclaratorWithDeclarator((Declarator) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ":"
		 * f1 ::= ConstantExpression()
		 */
		@Override
		public Node visit(StructDeclaratorWithBitField oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new StructDeclaratorWithBitField((NodeToken) newF0, (ConstantExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= EnumSpecifierWithList()
		 * | EnumSpecifierWithId()
		 */
		@Override
		public Node visit(EnumSpecifier oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new EnumSpecifier((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <ENUM>
		 * f1 ::= ( <IDENTIFIER> )?
		 * f2 ::= "{"
		 * f3 ::= EnumeratorList()
		 * f4 ::= "}"
		 */
		@Override
		public Node visit(EnumSpecifierWithList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new EnumSpecifierWithList((NodeToken) newF0, (NodeOptional) newF1, (NodeToken) newF2,
					(EnumeratorList) newF3, (NodeToken) newF4);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <ENUM>
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public Node visit(EnumSpecifierWithId oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new EnumSpecifierWithId((NodeToken) newF0, (NodeToken) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= Enumerator()
		 * f1 ::= ( "," Enumerator() )*
		 */
		@Override
		public Node visit(EnumeratorList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new EnumeratorList((Enumerator) newF0, (NodeListOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ( "=" ConstantExpression() )?
		 */
		@Override
		public Node visit(Enumerator oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new Enumerator((NodeToken) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( Pointer() )?
		 * f1 ::= DirectDeclarator()
		 */
		@Override
		public Node visit(Declarator oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new Declarator((NodeOptional) newF0, (DirectDeclarator) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= IdentifierOrDeclarator()
		 * f1 ::= DeclaratorOpList()
		 */
		@Override
		public Node visit(DirectDeclarator oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new DirectDeclarator((IdentifierOrDeclarator) newF0, (DeclaratorOpList) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( ADeclaratorOp() )*
		 */
		@Override
		public Node visit(DeclaratorOpList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new DeclaratorOpList((NodeListOptional) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= DimensionSize()
		 * | ParameterTypeListClosed()
		 * | OldParameterListClosed()
		 */
		@Override
		public Node visit(ADeclaratorOp oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ADeclaratorOp((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= "["
		 * f1 ::= ( ConstantExpression() )?
		 * f2 ::= "]"
		 */
		@Override
		public Node visit(DimensionSize oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new DimensionSize((NodeToken) newF0, (NodeOptional) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( ParameterTypeList() )?
		 * f2 ::= ")"
		 */
		@Override
		public Node visit(ParameterTypeListClosed oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new ParameterTypeListClosed((NodeToken) newF0, (NodeOptional) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( OldParameterList() )?
		 * f2 ::= ")"
		 */
		@Override
		public Node visit(OldParameterListClosed oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new OldParameterListClosed((NodeToken) newF0, (NodeOptional) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * | "(" Declarator() ")"
		 */
		@Override
		public Node visit(IdentifierOrDeclarator oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new IdentifierOrDeclarator((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( "*" | "^" )
		 * f1 ::= ( TypeQualifierList() )?
		 * f2 ::= ( Pointer() )?
		 */
		@Override
		public Node visit(Pointer oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new Pointer((NodeChoice) newF0, (NodeOptional) newF1, (NodeOptional) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( TypeQualifier() )+
		 */
		@Override
		public Node visit(TypeQualifierList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new TypeQualifierList((NodeList) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ParameterList()
		 * f1 ::= ( "," "..." )?
		 */
		@Override
		public Node visit(ParameterTypeList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new ParameterTypeList((ParameterList) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ParameterDeclaration()
		 * f1 ::= ( "," ParameterDeclaration() )*
		 */
		@Override
		public Node visit(ParameterList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new ParameterList((ParameterDeclaration) newF0, (NodeListOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ParameterAbstraction()
		 */
		@Override
		public Node visit(ParameterDeclaration oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new ParameterDeclaration((DeclarationSpecifiers) newF0, (ParameterAbstraction) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= Declarator()
		 * | AbstractOptionalDeclarator()
		 */
		@Override
		public Node visit(ParameterAbstraction oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ParameterAbstraction((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( AbstractDeclarator() )?
		 */
		@Override
		public Node visit(AbstractOptionalDeclarator oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new AbstractOptionalDeclarator((NodeOptional) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ( "," <IDENTIFIER> )*
		 */
		@Override
		public Node visit(OldParameterList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new OldParameterList((NodeToken) newF0, (NodeListOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * | ArrayInitializer()
		 */
		@Override
		public Node visit(Initializer oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new Initializer((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= "{"
		 * f1 ::= InitializerList()
		 * f2 ::= ( "," )?
		 * f3 ::= "}"
		 */
		@Override
		public Node visit(ArrayInitializer oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new ArrayInitializer((NodeToken) newF0, (InitializerList) newF1, (NodeOptional) newF2,
					(NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= Initializer()
		 * f1 ::= ( "," Initializer() )*
		 */
		@Override
		public Node visit(InitializerList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new InitializerList((Initializer) newF0, (NodeListOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= SpecifierQualifierList()
		 * f1 ::= ( AbstractDeclarator() )?
		 */
		@Override
		public Node visit(TypeName oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new TypeName((SpecifierQualifierList) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= AbstractDeclaratorWithPointer()
		 * | DirectAbstractDeclarator()
		 */
		@Override
		public Node visit(AbstractDeclarator oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new AbstractDeclarator((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= Pointer()
		 * f1 ::= ( DirectAbstractDeclarator() )?
		 */
		@Override
		public Node visit(AbstractDeclaratorWithPointer oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new AbstractDeclaratorWithPointer((Pointer) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= AbstractDimensionOrParameter()
		 * f1 ::= DimensionOrParameterList()
		 */
		@Override
		public Node visit(DirectAbstractDeclarator oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new DirectAbstractDeclarator((AbstractDimensionOrParameter) newF0,
					(DimensionOrParameterList) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= AbstractDeclaratorClosed()
		 * | DimensionSize()
		 * | ParameterTypeListClosed()
		 */
		@Override
		public Node visit(AbstractDimensionOrParameter oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new AbstractDimensionOrParameter((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= "("
		 * f1 ::= AbstractDeclarator()
		 * f2 ::= ")"
		 */
		@Override
		public Node visit(AbstractDeclaratorClosed oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new AbstractDeclaratorClosed((NodeToken) newF0, (AbstractDeclarator) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( ADimensionOrParameter() )*
		 */
		@Override
		public Node visit(DimensionOrParameterList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new DimensionOrParameterList((NodeListOptional) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= DimensionSize()
		 * | ParameterTypeListClosed()
		 */
		@Override
		public Node visit(ADimensionOrParameter oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ADimensionOrParameter((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 */
		@Override
		public Node visit(TypedefName oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new TypedefName((NodeToken) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( LabeledStatement() | ExpressionStatement() |
		 * CompoundStatement()
		 * | SelectionStatement() | IterationStatement() | JumpStatement() |
		 * UnknownPragma() | OmpConstruct() | OmpDirective() | UnknownCpp() )
		 */
		@Override
		public Node visit(Statement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getStmtF0().accept(this);
			newNode = new Statement((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= "#"
		 * f1 ::= <UNKNOWN_CPP>
		 */
		@Override
		public Node visit(UnknownCpp oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new UnknownCpp((NodeToken) newF0, (NodeToken) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <OMP_CR>
		 * | <OMP_NL>
		 */
		@Override
		public Node visit(OmpEol oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new OmpEol((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
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
		public Node visit(OmpConstruct oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getOmpConsF0().accept(this);
			newNode = new OmpConstruct((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= BarrierDirective()
		 * | TaskwaitDirective()
		 * | TaskyieldDirective()
		 * | FlushDirective()
		 */
		@Override
		public Node visit(OmpDirective oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getOmpDirF0().accept(this);
			newNode = new OmpDirective((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= ParallelDirective()
		 * f2 ::= Statement()
		 */
		@Override
		public Node visit(ParallelConstruct oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getParConsF0().accept(this);
			Node newF1 = oldNode.getParConsF1().accept(this);
			Node newF2 = oldNode.getParConsF2().accept(this);
			newNode = new ParallelConstruct((OmpPragma) newF0, (ParallelDirective) newF1, (Statement) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= "#"
		 * f1 ::= <PRAGMA>
		 * f2 ::= <OMP>
		 */
		@Override
		public Node visit(OmpPragma oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new OmpPragma((NodeToken) newF0, (NodeToken) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= "#"
		 * f1 ::= <PRAGMA>
		 * f2 ::= <UNKNOWN_CPP>
		 */
		@Override
		public Node visit(UnknownPragma oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new UnknownPragma((NodeToken) newF0, (NodeToken) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <PARALLEL>
		 * f1 ::= UniqueParallelOrDataClauseList()
		 * f2 ::= OmpEol()
		 */
		@Override
		public Node visit(ParallelDirective oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new ParallelDirective((NodeToken) newF0, (UniqueParallelOrDataClauseList) newF1, (OmpEol) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( AUniqueParallelOrDataClause() )*
		 */
		@Override
		public Node visit(UniqueParallelOrDataClauseList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new UniqueParallelOrDataClauseList((NodeListOptional) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= UniqueParallelClause()
		 * | DataClause()
		 */
		@Override
		public Node visit(AUniqueParallelOrDataClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new AUniqueParallelOrDataClause((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= IfClause()
		 * | NumThreadsClause()
		 */
		@Override
		public Node visit(UniqueParallelClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new UniqueParallelClause((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <IF>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(IfClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new IfClause((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2, (NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <NUM_THREADS>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(NumThreadsClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new NumThreadsClause((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2, (NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
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
		public Node visit(DataClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new DataClause((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <PRIVATE>
		 * f1 ::= "("
		 * f2 ::= VariableList()
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(OmpPrivateClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new OmpPrivateClause((NodeToken) newF0, (NodeToken) newF1, (VariableList) newF2,
					(NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <FIRSTPRIVATE>
		 * f1 ::= "("
		 * f2 ::= VariableList()
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(OmpFirstPrivateClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new OmpFirstPrivateClause((NodeToken) newF0, (NodeToken) newF1, (VariableList) newF2,
					(NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <LASTPRIVATE>
		 * f1 ::= "("
		 * f2 ::= VariableList()
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(OmpLastPrivateClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new OmpLastPrivateClause((NodeToken) newF0, (NodeToken) newF1, (VariableList) newF2,
					(NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <SHARED>
		 * f1 ::= "("
		 * f2 ::= VariableList()
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(OmpSharedClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new OmpSharedClause((NodeToken) newF0, (NodeToken) newF1, (VariableList) newF2,
					(NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <COPYIN>
		 * f1 ::= "("
		 * f2 ::= VariableList()
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(OmpCopyinClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new OmpCopyinClause((NodeToken) newF0, (NodeToken) newF1, (VariableList) newF2,
					(NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <DFLT>
		 * f1 ::= "("
		 * f2 ::= <SHARED>
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(OmpDfltSharedClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new OmpDfltSharedClause((NodeToken) newF0, (NodeToken) newF1, (NodeToken) newF2,
					(NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <DFLT>
		 * f1 ::= "("
		 * f2 ::= <NONE>
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(OmpDfltNoneClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new OmpDfltNoneClause((NodeToken) newF0, (NodeToken) newF1, (NodeToken) newF2, (NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
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
		public Node visit(OmpReductionClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			Node newF5 = oldNode.getF5().accept(this);
			newNode = new OmpReductionClause((NodeToken) newF0, (NodeToken) newF1, (ReductionOp) newF2,
					(NodeToken) newF3, (VariableList) newF4, (NodeToken) newF5);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= ForDirective()
		 * f2 ::= OmpForHeader()
		 * f3 ::= Statement()
		 */
		@Override
		public Node visit(ForConstruct oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new ForConstruct((OmpPragma) newF0, (ForDirective) newF1, (OmpForHeader) newF2,
					(Statement) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <FOR>
		 * f1 ::= UniqueForOrDataOrNowaitClauseList()
		 * f2 ::= OmpEol()
		 */
		@Override
		public Node visit(ForDirective oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new ForDirective((NodeToken) newF0, (UniqueForOrDataOrNowaitClauseList) newF1, (OmpEol) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= ( AUniqueForOrDataOrNowaitClause() )*
		 */
		@Override
		public Node visit(UniqueForOrDataOrNowaitClauseList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new UniqueForOrDataOrNowaitClauseList((NodeListOptional) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= UniqueForClause()
		 * | DataClause()
		 * | NowaitClause()
		 */
		@Override
		public Node visit(AUniqueForOrDataOrNowaitClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new AUniqueForOrDataOrNowaitClause((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <NOWAIT>
		 */
		@Override
		public Node visit(NowaitClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new NowaitClause((NodeToken) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <ORDERED>
		 * | UniqueForClauseSchedule()
		 * | UniqueForCollapse()
		 */
		@Override
		public Node visit(UniqueForClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new UniqueForClause((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <COLLAPSE>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(UniqueForCollapse oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new UniqueForCollapse((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2,
					(NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <SCHEDULE>
		 * f1 ::= "("
		 * f2 ::= ScheduleKind()
		 * f3 ::= ( "," Expression() )?
		 * f4 ::= ")"
		 */
		@Override
		public Node visit(UniqueForClauseSchedule oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new UniqueForClauseSchedule((NodeToken) newF0, (NodeToken) newF1, (ScheduleKind) newF2,
					(NodeOptional) newF3, (NodeToken) newF4);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <STATIC>
		 * | <DYNAMIC>
		 * | <GUIDED>
		 * | <RUNTIME>
		 */
		@Override
		public Node visit(ScheduleKind oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ScheduleKind((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
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
		public Node visit(OmpForHeader oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			Node newF5 = oldNode.getF5().accept(this);
			Node newF6 = oldNode.getF6().accept(this);
			Node newF7 = oldNode.getF7().accept(this);
			newNode = new OmpForHeader((NodeToken) newF0, (NodeToken) newF1, (OmpForInitExpression) newF2,
					(NodeToken) newF3, (OmpForCondition) newF4, (NodeToken) newF5, (OmpForReinitExpression) newF6,
					(NodeToken) newF7);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= Expression()
		 */
		@Override
		public Node visit(OmpForInitExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new OmpForInitExpression((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= OmpForLTCondition()
		 * | OmpForLECondition()
		 * | OmpForGTCondition()
		 * | OmpForGECondition()
		 */
		@Override
		public Node visit(OmpForCondition oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new OmpForCondition((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<"
		 * f2 ::= Expression()
		 */
		@Override
		public Node visit(OmpForLTCondition oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new OmpForLTCondition((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<="
		 * f2 ::= Expression()
		 */
		@Override
		public Node visit(OmpForLECondition oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new OmpForLECondition((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">"
		 * f2 ::= Expression()
		 */
		@Override
		public Node visit(OmpForGTCondition oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new OmpForGTCondition((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">="
		 * f2 ::= Expression()
		 */
		@Override
		public Node visit(OmpForGECondition oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new OmpForGECondition((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

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
		public Node visit(OmpForReinitExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getOmpForReinitF0().accept(this);
			newNode = new OmpForReinitExpression((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "++"
		 */
		@Override
		public Node visit(PostIncrementId oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new PostIncrementId((NodeToken) newF0, (NodeToken) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "--"
		 */
		@Override
		public Node visit(PostDecrementId oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new PostDecrementId((NodeToken) newF0, (NodeToken) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "++"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public Node visit(PreIncrementId oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new PreIncrementId((NodeToken) newF0, (NodeToken) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "--"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public Node visit(PreDecrementId oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new PreDecrementId((NodeToken) newF0, (NodeToken) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "+="
		 * f2 ::= Expression()
		 */
		@Override
		public Node visit(ShortAssignPlus oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new ShortAssignPlus((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "-="
		 * f2 ::= Expression()
		 */
		@Override
		public Node visit(ShortAssignMinus oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new ShortAssignMinus((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "+"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public Node visit(OmpForAdditive oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new OmpForAdditive((NodeToken) newF0, (NodeToken) newF1, (NodeToken) newF2, (NodeToken) newF3,
					(AdditiveExpression) newF4);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "-"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public Node visit(OmpForSubtractive oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new OmpForSubtractive((NodeToken) newF0, (NodeToken) newF1, (NodeToken) newF2, (NodeToken) newF3,
					(AdditiveExpression) newF4);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= MultiplicativeExpression()
		 * f3 ::= "+"
		 * f4 ::= <IDENTIFIER>
		 */
		@Override
		public Node visit(OmpForMultiplicative oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new OmpForMultiplicative((NodeToken) newF0, (NodeToken) newF1, (MultiplicativeExpression) newF2,
					(NodeToken) newF3, (NodeToken) newF4);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <SECTIONS>
		 * f2 ::= NowaitDataClauseList()
		 * f3 ::= OmpEol()
		 * f4 ::= SectionsScope()
		 */
		@Override
		public Node visit(SectionsConstruct oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new SectionsConstruct((OmpPragma) newF0, (NodeToken) newF1, (NowaitDataClauseList) newF2,
					(OmpEol) newF3, (SectionsScope) newF4);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ( ANowaitDataClause() )*
		 */
		@Override
		public Node visit(NowaitDataClauseList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new NowaitDataClauseList((NodeListOptional) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= NowaitClause()
		 * | DataClause()
		 */
		@Override
		public Node visit(ANowaitDataClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ANowaitDataClause((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "{"
		 * f1 ::= ( Statement() )?
		 * f2 ::= ( ASection() )*
		 * f3 ::= "}"
		 */
		@Override
		public Node visit(SectionsScope oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new SectionsScope((NodeToken) newF0, (NodeOptional) newF1, (NodeListOptional) newF2,
					(NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <SECTION>
		 * f2 ::= OmpEol()
		 * f3 ::= Statement()
		 */
		@Override
		public Node visit(ASection oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new ASection((OmpPragma) newF0, (NodeToken) newF1, (OmpEol) newF2, (Statement) newF3);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <SINGLE>
		 * f2 ::= SingleClauseList()
		 * f3 ::= OmpEol()
		 * f4 ::= Statement()
		 */
		@Override
		public Node visit(SingleConstruct oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new SingleConstruct((OmpPragma) newF0, (NodeToken) newF1, (SingleClauseList) newF2,
					(OmpEol) newF3, (Statement) newF4);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ( ASingleClause() )*
		 */
		@Override
		public Node visit(SingleClauseList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new SingleClauseList((NodeListOptional) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= NowaitClause()
		 * | DataClause()
		 * | OmpCopyPrivateClause()
		 */
		@Override
		public Node visit(ASingleClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ASingleClause((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <COPYPRIVATE>
		 * f1 ::= "("
		 * f2 ::= VariableList()
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(OmpCopyPrivateClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new OmpCopyPrivateClause((NodeToken) newF0, (NodeToken) newF1, (VariableList) newF2,
					(NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <TASK>
		 * f2 ::= ( TaskClause() )*
		 * f3 ::= OmpEol()
		 * f4 ::= Statement()
		 */
		@Override
		public Node visit(TaskConstruct oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new TaskConstruct((OmpPragma) newF0, (NodeToken) newF1, (NodeListOptional) newF2, (OmpEol) newF3,
					(Statement) newF4);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= DataClause()
		 * | UniqueTaskClause()
		 */
		@Override
		public Node visit(TaskClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new TaskClause((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= IfClause()
		 * | FinalClause()
		 * | UntiedClause()
		 * | MergeableClause()
		 */
		@Override
		public Node visit(UniqueTaskClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new UniqueTaskClause((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <FINAL>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(FinalClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new FinalClause((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2, (NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <UNTIED>
		 */
		@Override
		public Node visit(UntiedClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new UntiedClause((NodeToken) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <MERGEABLE>
		 */
		@Override
		public Node visit(MergeableClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new MergeableClause((NodeToken) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

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
		public Node visit(ParallelForConstruct oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			Node newF5 = oldNode.getF5().accept(this);
			Node newF6 = oldNode.getF6().accept(this);
			newNode = new ParallelForConstruct((OmpPragma) newF0, (NodeToken) newF1, (NodeToken) newF2,
					(UniqueParallelOrUniqueForOrDataClauseList) newF3, (OmpEol) newF4, (OmpForHeader) newF5,
					(Statement) newF6);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ( AUniqueParallelOrUniqueForOrDataClause() )*
		 */
		@Override
		public Node visit(UniqueParallelOrUniqueForOrDataClauseList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new UniqueParallelOrUniqueForOrDataClauseList((NodeListOptional) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= UniqueParallelClause()
		 * | UniqueForClause()
		 * | DataClause()
		 */
		@Override
		public Node visit(AUniqueParallelOrUniqueForOrDataClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new AUniqueParallelOrUniqueForOrDataClause((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

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
		public Node visit(ParallelSectionsConstruct oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			Node newF5 = oldNode.getF5().accept(this);
			newNode = new ParallelSectionsConstruct((OmpPragma) newF0, (NodeToken) newF1, (NodeToken) newF2,
					(UniqueParallelOrDataClauseList) newF3, (OmpEol) newF4, (SectionsScope) newF5);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <MASTER>
		 * f2 ::= OmpEol()
		 * f3 ::= Statement()
		 */
		@Override
		public Node visit(MasterConstruct oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new MasterConstruct((OmpPragma) newF0, (NodeToken) newF1, (OmpEol) newF2, (Statement) newF3);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <CRITICAL>
		 * f2 ::= ( RegionPhrase() )?
		 * f3 ::= OmpEol()
		 * f4 ::= Statement()
		 */
		@Override
		public Node visit(CriticalConstruct oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new CriticalConstruct((OmpPragma) newF0, (NodeToken) newF1, (NodeOptional) newF2, (OmpEol) newF3,
					(Statement) newF4);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "("
		 * f1 ::= <IDENTIFIER>
		 * f2 ::= ")"
		 */
		@Override
		public Node visit(RegionPhrase oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new RegionPhrase((NodeToken) newF0, (NodeToken) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <ATOMIC>
		 * f2 ::= ( AtomicClause() )?
		 * f3 ::= OmpEol()
		 * f4 ::= ExpressionStatement()
		 */
		@Override
		public Node visit(AtomicConstruct oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new AtomicConstruct((OmpPragma) newF0, (NodeToken) newF1, (NodeOptional) newF2, (OmpEol) newF3,
					(Statement) newF4);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <READ>
		 * | <WRITE>
		 * | <UPDATE>
		 * | <CAPTURE>
		 */
		@Override
		public Node visit(AtomicClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new AtomicClause((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <FLUSH>
		 * f2 ::= ( FlushVars() )?
		 * f3 ::= OmpEol()
		 */
		@Override
		public Node visit(FlushDirective oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new FlushDirective((OmpPragma) newF0, (NodeToken) newF1, (NodeOptional) newF2, (OmpEol) newF3);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "("
		 * f1 ::= VariableList()
		 * f2 ::= ")"
		 */
		@Override
		public Node visit(FlushVars oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new FlushVars((NodeToken) newF0, (VariableList) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <ORDERED>
		 * f2 ::= OmpEol()
		 * f3 ::= Statement()
		 */
		@Override
		public Node visit(OrderedConstruct oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new OrderedConstruct((OmpPragma) newF0, (NodeToken) newF1, (OmpEol) newF2, (Statement) newF3);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <BARRIER>
		 * f2 ::= OmpEol()
		 */
		@Override
		public Node visit(BarrierDirective oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new BarrierDirective((OmpPragma) newF0, (NodeToken) newF1, (OmpEol) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <TASKWAIT>
		 * f2 ::= OmpEol()
		 */
		@Override
		public Node visit(TaskwaitDirective oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new TaskwaitDirective((OmpPragma) newF0, (NodeToken) newF1, (OmpEol) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <TASKYIELD>
		 * f2 ::= OmpEol()
		 */
		@Override
		public Node visit(TaskyieldDirective oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new TaskyieldDirective((OmpPragma) newF0, (NodeToken) newF1, (OmpEol) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

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
		public Node visit(ThreadPrivateDirective oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			Node newF5 = oldNode.getF5().accept(this);
			newNode = new ThreadPrivateDirective((OmpPragma) newF0, (NodeToken) newF1, (NodeToken) newF2,
					(VariableList) newF3, (NodeToken) newF4, (OmpEol) newF5);
			copyInfo(oldNode, newNode);
			return newNode;

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
		public Node visit(DeclareReductionDirective oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			Node newF5 = oldNode.getF5().accept(this);
			Node newF6 = oldNode.getF6().accept(this);
			Node newF7 = oldNode.getF7().accept(this);
			Node newF8 = oldNode.getF8().accept(this);
			Node newF9 = oldNode.getF9().accept(this);
			Node newF10 = oldNode.getF10().accept(this);
			Node newF11 = oldNode.getF11().accept(this);
			newNode = new DeclareReductionDirective((OmpPragma) newF0, (NodeToken) newF1, (NodeToken) newF2,
					(NodeToken) newF3, (ReductionOp) newF4, (NodeToken) newF5, (ReductionTypeList) newF6,
					(NodeToken) newF7, (Expression) newF8, (NodeToken) newF9, (NodeOptional) newF10, (OmpEol) newF11);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ( TypeSpecifier() )*
		 */
		@Override
		public Node visit(ReductionTypeList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ReductionTypeList((NodeListOptional) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= AssignInitializerClause()
		 * | ArgumentInitializerClause()
		 */
		@Override
		public Node visit(InitializerClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new InitializerClause((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

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
		public Node visit(AssignInitializerClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			Node newF5 = oldNode.getF5().accept(this);
			newNode = new AssignInitializerClause((NodeToken) newF0, (NodeToken) newF1, (NodeToken) newF2,
					(NodeToken) newF3, (Initializer) newF4, (NodeToken) newF5);
			copyInfo(oldNode, newNode);
			return newNode;

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
		public Node visit(ArgumentInitializerClause oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			Node newF5 = oldNode.getF5().accept(this);
			Node newF6 = oldNode.getF6().accept(this);
			newNode = new ArgumentInitializerClause((NodeToken) newF0, (NodeToken) newF1, (NodeToken) newF2,
					(NodeToken) newF3, (ExpressionList) newF4, (NodeToken) newF5, (NodeToken) newF6);
			copyInfo(oldNode, newNode);
			return newNode;

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
		public Node visit(ReductionOp oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ReductionOp((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ( "," <IDENTIFIER> )*
		 */
		@Override
		public Node visit(VariableList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new VariableList((NodeToken) newF0, (NodeListOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= SimpleLabeledStatement()
		 * | CaseLabeledStatement()
		 * | DefaultLabeledStatement()
		 */
		@Override
		public Node visit(LabeledStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getLabStmtF0().accept(this);
			newNode = new LabeledStatement((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ":"
		 * f2 ::= Statement()
		 */
		@Override
		public Node visit(SimpleLabeledStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new SimpleLabeledStatement((NodeToken) newF0, (NodeToken) newF1, (Statement) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <CASE>
		 * f1 ::= ConstantExpression()
		 * f2 ::= ":"
		 * f3 ::= Statement()
		 */
		@Override
		public Node visit(CaseLabeledStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new CaseLabeledStatement((NodeToken) newF0, (ConstantExpression) newF1, (NodeToken) newF2,
					(Statement) newF3);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <DFLT>
		 * f1 ::= ":"
		 * f2 ::= Statement()
		 */
		@Override
		public Node visit(DefaultLabeledStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new DefaultLabeledStatement((NodeToken) newF0, (NodeToken) newF1, (Statement) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ( Expression() )?
		 * f1 ::= ";
		 * "
		 */
		@Override
		public Node visit(ExpressionStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = ExpressionStatement.getExpressionStatementOrCallStatement((NodeOptional) newF0,
					(NodeToken) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "{"
		 * f1 ::= ( CompoundStatementElement() )*
		 * f2 ::= "}"
		 */
		@Override
		public Node visit(CompoundStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new CompoundStatement((NodeToken) newF0, (NodeListOptional) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= Declaration()
		 * | Statement()
		 */
		@Override
		public Node visit(CompoundStatementElement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new CompoundStatementElement((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= IfStatement()
		 * | SwitchStatement()
		 */
		@Override
		public Node visit(SelectionStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getSelStmtF0().accept(this);
			newNode = new SelectionStatement((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

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
		public Node visit(IfStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			Node newF5 = oldNode.getF5().accept(this);
			newNode = new IfStatement((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2, (NodeToken) newF3,
					(Statement) newF4, (NodeOptional) newF5);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <SWITCH>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 * f4 ::= Statement()
		 */
		@Override
		public Node visit(SwitchStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new SwitchStatement((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2, (NodeToken) newF3,
					(Statement) newF4);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= WhileStatement()
		 * | DoStatement()
		 * | ForStatement()
		 */
		@Override
		public Node visit(IterationStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getItStmtF0().accept(this);
			newNode = new IterationStatement((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <WHILE>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 * f4 ::= Statement()
		 */
		@Override
		public Node visit(WhileStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			newNode = new WhileStatement((NodeToken) newF0, (NodeToken) newF1, (Expression) newF2, (NodeToken) newF3,
					(Statement) newF4);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <DO>
		 * f1 ::= Statement()
		 * f2 ::= <WHILE>
		 * f3 ::= "("
		 * f4 ::= Expression()
		 * f5 ::= ")"
		 * f6 ::= ";
		 * "
		 */
		@Override
		public Node visit(DoStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			Node newF5 = oldNode.getF5().accept(this);
			Node newF6 = oldNode.getF6().accept(this);
			newNode = new DoStatement((NodeToken) newF0, (Statement) newF1, (NodeToken) newF2, (NodeToken) newF3,
					(Expression) newF4, (NodeToken) newF5, (NodeToken) newF6);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <FOR>
		 * f1 ::= "("
		 * f2 ::= ( Expression() )?
		 * f3 ::= ";
		 * "
		 * f4 ::= ( Expression() )?
		 * f5 ::= ";
		 * "
		 * f6 ::= ( Expression() )?
		 * f7 ::= ")"
		 * f8 ::= Statement()
		 */
		@Override
		public Node visit(ForStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			Node newF4 = oldNode.getF4().accept(this);
			Node newF5 = oldNode.getF5().accept(this);
			Node newF6 = oldNode.getF6().accept(this);
			Node newF7 = oldNode.getF7().accept(this);
			Node newF8 = oldNode.getF8().accept(this);
			newNode = new ForStatement((NodeToken) newF0, (NodeToken) newF1, (NodeOptional) newF2, (NodeToken) newF3,
					(NodeOptional) newF4, (NodeToken) newF5, (NodeOptional) newF6, (NodeToken) newF7,
					(Statement) newF8);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= GotoStatement()
		 * | ContinueStatement()
		 * | BreakStatement()
		 * | ReturnStatement()
		 */
		@Override
		public Node visit(JumpStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getJumpStmtF0().accept(this);
			newNode = new JumpStatement((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <GOTO>
		 * f1 ::= <IDENTIFIER>
		 * f2 ::= ";
		 * "
		 */
		@Override
		public Node visit(GotoStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new GotoStatement((NodeToken) newF0, (NodeToken) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <CONTINUE>
		 * f1 ::= ";
		 * "
		 */
		@Override
		public Node visit(ContinueStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new ContinueStatement((NodeToken) newF0, (NodeToken) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <BREAK>
		 * f1 ::= ";
		 * "
		 */
		@Override
		public Node visit(BreakStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new BreakStatement((NodeToken) newF0, (NodeToken) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <RETURN>
		 * f1 ::= ( Expression() )?
		 * f2 ::= ";
		 * "
		 */
		@Override
		public Node visit(ReturnStatement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new ReturnStatement((NodeToken) newF0, (NodeOptional) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public Node visit(Expression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getExpF0().accept(this);
			Node newF1 = oldNode.getExpF1().accept(this);
			newNode = new Expression((AssignmentExpression) newF0, (NodeListOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= NonConditionalExpression()
		 * | ConditionalExpression()
		 */
		@Override
		public Node visit(AssignmentExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new AssignmentExpression((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public Node visit(NonConditionalExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new NonConditionalExpression((UnaryExpression) newF0, (AssignmentOperator) newF1,
					(AssignmentExpression) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

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
		public Node visit(AssignmentOperator oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new AssignmentOperator((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= LogicalORExpression()
		 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
		 */
		@Override
		public Node visit(ConditionalExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new ConditionalExpression((LogicalORExpression) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ConditionalExpression()
		 */
		@Override
		public Node visit(ConstantExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ConstantExpression((ConditionalExpression) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= LogicalANDExpression()
		 * f1 ::= ( "||" LogicalORExpression() )?
		 */
		@Override
		public Node visit(LogicalORExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new LogicalORExpression((LogicalANDExpression) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= InclusiveORExpression()
		 * f1 ::= ( "&&" LogicalANDExpression() )?
		 */
		@Override
		public Node visit(LogicalANDExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new LogicalANDExpression((InclusiveORExpression) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ExclusiveORExpression()
		 * f1 ::= ( "|" InclusiveORExpression() )?
		 */
		@Override
		public Node visit(InclusiveORExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new InclusiveORExpression((ExclusiveORExpression) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ANDExpression()
		 * f1 ::= ( "^" ExclusiveORExpression() )?
		 */
		@Override
		public Node visit(ExclusiveORExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new ExclusiveORExpression((ANDExpression) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= EqualityExpression()
		 * f1 ::= ( "&" ANDExpression() )?
		 */
		@Override
		public Node visit(ANDExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new ANDExpression((EqualityExpression) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= RelationalExpression()
		 * f1 ::= ( EqualOptionalExpression() )?
		 */
		@Override
		public Node visit(EqualityExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new EqualityExpression((RelationalExpression) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= EqualExpression()
		 * | NonEqualExpression()
		 */
		@Override
		public Node visit(EqualOptionalExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new EqualOptionalExpression((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "=="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public Node visit(EqualExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new EqualExpression((NodeToken) newF0, (EqualityExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "!="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public Node visit(NonEqualExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new NonEqualExpression((NodeToken) newF0, (EqualityExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ShiftExpression()
		 * f1 ::= ( RelationalOptionalExpression() )?
		 */
		@Override
		public Node visit(RelationalExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getRelExpF0().accept(this);
			Node newF1 = oldNode.getRelExpF1().accept(this);
			newNode = new RelationalExpression((ShiftExpression) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= RelationalLTExpression()
		 * | RelationalGTExpression()
		 * | RelationalLEExpression()
		 * | RelationalGEExpression()
		 */
		@Override
		public Node visit(RelationalOptionalExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new RelationalOptionalExpression((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "<"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public Node visit(RelationalLTExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new RelationalLTExpression((NodeToken) newF0, (RelationalExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ">"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public Node visit(RelationalGTExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new RelationalGTExpression((NodeToken) newF0, (RelationalExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "<="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public Node visit(RelationalLEExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new RelationalLEExpression((NodeToken) newF0, (RelationalExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ">="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public Node visit(RelationalGEExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new RelationalGEExpression((NodeToken) newF0, (RelationalExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= AdditiveExpression()
		 * f1 ::= ( ShiftOptionalExpression() )?
		 */
		@Override
		public Node visit(ShiftExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new ShiftExpression((AdditiveExpression) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ShiftLeftExpression()
		 * | ShiftRightExpression()
		 */
		@Override
		public Node visit(ShiftOptionalExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new ShiftOptionalExpression((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ">>"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public Node visit(ShiftLeftExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new ShiftLeftExpression((NodeToken) newF0, (ShiftExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "<<"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public Node visit(ShiftRightExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new ShiftRightExpression((NodeToken) newF0, (ShiftExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= MultiplicativeExpression()
		 * f1 ::= ( AdditiveOptionalExpression() )?
		 */
		@Override
		public Node visit(AdditiveExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new AdditiveExpression((MultiplicativeExpression) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= AdditivePlusExpression()
		 * | AdditiveMinusExpression()
		 */
		@Override
		public Node visit(AdditiveOptionalExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new AdditiveOptionalExpression((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "+"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public Node visit(AdditivePlusExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new AdditivePlusExpression((NodeToken) newF0, (AdditiveExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "-"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public Node visit(AdditiveMinusExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new AdditiveMinusExpression((NodeToken) newF0, (AdditiveExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= CastExpression()
		 * f1 ::= ( MultiplicativeOptionalExpression() )?
		 */
		@Override
		public Node visit(MultiplicativeExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new MultiplicativeExpression((CastExpression) newF0, (NodeOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= MultiplicativeMultiExpression()
		 * | MultiplicativeDivExpression()
		 * | MultiplicativeModExpression()
		 */
		@Override
		public Node visit(MultiplicativeOptionalExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new MultiplicativeOptionalExpression((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "*"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public Node visit(MultiplicativeMultiExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new MultiplicativeMultiExpression((NodeToken) newF0, (MultiplicativeExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "/"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public Node visit(MultiplicativeDivExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new MultiplicativeDivExpression((NodeToken) newF0, (MultiplicativeExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "%"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public Node visit(MultiplicativeModExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new MultiplicativeModExpression((NodeToken) newF0, (MultiplicativeExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= CastExpressionTyped()
		 * | UnaryExpression()
		 */
		@Override
		public Node visit(CastExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new CastExpression((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "("
		 * f1 ::= TypeName()
		 * f2 ::= ")"
		 * f3 ::= CastExpression()
		 */
		@Override
		public Node visit(CastExpressionTyped oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new CastExpressionTyped((NodeToken) newF0, (TypeName) newF1, (NodeToken) newF2,
					(CastExpression) newF3);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= UnaryExpressionPreIncrement()
		 * | UnaryExpressionPreDecrement()
		 * | UnarySizeofExpression()
		 * | UnaryCastExpression()
		 * | PostfixExpression()
		 */
		@Override
		public Node visit(UnaryExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new UnaryExpression((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "++"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public Node visit(UnaryExpressionPreIncrement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new UnaryExpressionPreIncrement((NodeToken) newF0, (UnaryExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "--"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public Node visit(UnaryExpressionPreDecrement oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new UnaryExpressionPreDecrement((NodeToken) newF0, (UnaryExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= UnaryOperator()
		 * f1 ::= CastExpression()
		 */
		@Override
		public Node visit(UnaryCastExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new UnaryCastExpression((UnaryOperator) newF0, (CastExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public Node visit(UnarySizeofExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new UnarySizeofExpression((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public Node visit(SizeofUnaryExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new SizeofUnaryExpression((NodeToken) newF0, (UnaryExpression) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= "("
		 * f2 ::= TypeName()
		 * f3 ::= ")"
		 */
		@Override
		public Node visit(SizeofTypeName oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			Node newF3 = oldNode.getF3().accept(this);
			newNode = new SizeofTypeName((NodeToken) newF0, (NodeToken) newF1, (TypeName) newF2, (NodeToken) newF3);
			copyInfo(oldNode, newNode);
			return newNode;

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
		public Node visit(UnaryOperator oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new UnaryOperator((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= PrimaryExpression()
		 * f1 ::= PostfixOperationsList()
		 */
		@Override
		public Node visit(PostfixExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new PostfixExpression((PrimaryExpression) newF0, (PostfixOperationsList) newF1);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= ( APostfixOperation() )*
		 */
		@Override
		public Node visit(PostfixOperationsList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new PostfixOperationsList((NodeListOptional) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

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
		public Node visit(APostfixOperation oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new APostfixOperation((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "++"
		 */
		@Override
		public Node visit(PlusPlus oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new PlusPlus((NodeToken) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "--"
		 */
		@Override
		public Node visit(MinusMinus oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new MinusMinus((NodeToken) newF0);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "["
		 * f1 ::= Expression()
		 * f2 ::= "]"
		 */
		@Override
		public Node visit(BracketExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new BracketExpression((NodeToken) newF0, (Expression) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;

		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( ExpressionList() )?
		 * f2 ::= ")"
		 */
		@Override
		public Node visit(ArgumentList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new ArgumentList((NodeToken) newF0, (NodeOptional) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= "."
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public Node visit(DotId oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new DotId((NodeToken) newF0, (NodeToken) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= "->"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public Node visit(ArrowId oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new ArrowId((NodeToken) newF0, (NodeToken) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * | Constant()
		 * | ExpressionClosed()
		 */
		@Override
		public Node visit(PrimaryExpression oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new PrimaryExpression((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= "("
		 * f1 ::= Expression()
		 * f2 ::= ")"
		 */
		@Override
		public Node visit(ExpressionClosed oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			Node newF2 = oldNode.getF2().accept(this);
			newNode = new ExpressionClosed((NodeToken) newF0, (Expression) newF1, (NodeToken) newF2);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public Node visit(ExpressionList oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			Node newF1 = oldNode.getF1().accept(this);
			newNode = new ExpressionList((AssignmentExpression) newF0, (NodeListOptional) newF1);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		/**
		 * f0 ::= <INTEGER_LITERAL>
		 * | <FLOATING_POINT_LITERAL>
		 * | <CHARACTER_LITERAL>
		 * | ( <STRING_LITERAL> )+
		 */
		@Override
		public Node visit(Constant oldNode) {
			Node newNode = null;
			Node newF0 = oldNode.getF0().accept(this);
			newNode = new Constant((NodeChoice) newF0);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		@Override
		public Node visit(CallStatement oldNode) {
			Node newNode = null;
			newNode = new CallStatement((NodeToken) oldNode.getFunctionDesignatorNode().accept(this),
					(PreCallNode) oldNode.getPreCallNode().accept(this),
					(PostCallNode) oldNode.getPostCallNode().accept(this));
			copyInfo(oldNode, newNode);
			return newNode;
		}

		@Override
		public Node visit(PreCallNode oldNode) {
			Node newNode = null;
			List<SimplePrimaryExpression> argumentList = new ArrayList<>(oldNode.getArgumentList());
			newNode = new PreCallNode(argumentList);
			copyInfo(oldNode, newNode);
			return newNode;
		}

		@Override
		public Node visit(PostCallNode oldNode) {
			Node newNode = null;
			newNode = new PostCallNode(oldNode.getReturnReceiver());
			copyInfo(oldNode, newNode);
			return newNode;
		}
	}
}
