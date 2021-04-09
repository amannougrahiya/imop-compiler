/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.simplify;

import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.getter.StructUnionOrEnumInfoGetter;
import imop.lib.transform.updater.InsertImmediatePredecessor;
import imop.lib.transform.updater.NodeReplacer;
import imop.lib.transform.updater.sideeffect.NodeUpdated;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

import java.util.HashSet;
import java.util.List;

public class Normalization {

	/**
	 * Given a node, this method traverses over all its
	 * constituent CFG leaf nodes in their postorder traversal,
	 * and transforms code such that each CFG leaf node respects the following
	 * constraints:
	 * <ol>
	 * <li>Each Declaration should contain declaration of only one variable.
	 * </li>
	 * <li>A typedef should not contain implicit declaration of any user-defined
	 * type.</li>
	 * <li>Declarations of user-defined types (structures, unions, and
	 * enumerators) should not also contain their use (i.e., declaration of
	 * their objects).</li>
	 * <li>The code should not contain any of the following operators : logical
	 * AND ( &&), logical OR ( ||), comma (,), and conditional (?:) operators.
	 * </li>
	 * <li>There should not be any chained assignment operators in the program
	 * (such as x = y = z;).</li>
	 * <li>No Expression should contain a function call; all function calls
	 * should be simplified such that they are denoted by independent statements
	 * (CallStatement), with arguments that can either be a Constant or an
	 * Identifier.</li>
	 * </ol>
	 * 
	 * @param node
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Node> T normalizeLeafNodes(T node, List<SideEffect> sideEffects) {
		T retNode = node;
		ExpressionSimplifier simplifier = new ExpressionSimplifier(new HashSet<>());
		for (Node leaf : node.getInfo().getCFGInfo().getLexicalCFGLeafContentsInPostOrder()) {
			CompoundStatement enclosingBlock = (CompoundStatement) Misc.getEnclosingBlock(leaf);
			if (enclosingBlock == null) {
				continue; // We cannot perform the normalization yet; when the parent would be connected,
							// we will.
			}
			if (!Normalization.needsNormalization(leaf)) {
				continue;
			}
			ExpressionSimplifier.SimplificationString simplifiedString = leaf.accept(simplifier);
			for (Declaration d : simplifiedString.getTemporaryDeclarations()) {
				enclosingBlock.getInfo().getCFGInfo().addDeclaration(d); // Ignore the side effects returned by this
																			// invocation.
			}

			Node replacementLeaf = FrontEnd.parseAndNormalize(simplifiedString.getReplacementString().toString(),
					leaf.getClass());
			if (leaf == node) {
				sideEffects.addAll(NodeReplacer.replaceNodes(leaf, replacementLeaf));
				sideEffects.add(new NodeUpdated(replacementLeaf,
						"This node was obtained as a result of normalization of some other node."));
				retNode = (T) replacementLeaf;
			} else {
				NodeReplacer.replaceNodes(leaf, replacementLeaf);
				new NodeUpdated(replacementLeaf,
						"This node was obtained as a result of normalization of some other node."); // Don't add this to
																									// sideEffects.
			}

			CompoundStatement preludeBlock = FrontEnd
					.parseAndNormalize("{" + simplifiedString.getPrelude().toString() + "}", CompoundStatement.class);
			for (Node elem : preludeBlock.getInfo().getCFGInfo().getElementList()) {
				InsertImmediatePredecessor.insert(replacementLeaf, elem); // Handle these side effects at call sites of
																			// this method, if required.
			}
		}
		return retNode;
	}

	/**
	 * Checks whether there exists any leaf CFG node within the given
	 * {@code node} which needs normalization of its expression or declarations.
	 * 
	 * @param node
	 *             node to be checked for normalizations.
	 * @return
	 *         <i>true</i> if there exists any leaf node in the given
	 *         {@code node} which needs normalization of expression or
	 *         declaration; <i>false</i> otherwise.
	 */
	private static boolean needsNormalization(Node node) {
		ExpressionNormalizationChecker checker = new ExpressionNormalizationChecker();
		node.accept(checker);
		return checker.needsSimplification;
	}

	/**
	 * This visitor is used to check whether the visited node required any
	 * normalization of expressions or declarations.
	 * 
	 * @author Aman Nougrahiya
	 *
	 */
	private static class ExpressionNormalizationChecker extends DepthFirstProcess {

		public boolean needsSimplification = false;

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ( InitDeclaratorList() )?
		 * f2 ::= ";"
		 */
		@Override
		public void visit(Declaration n) {
			if (!n.getF1().present()) {
				return;
			}

			StructUnionOrEnumInfoGetter aggregateInfoGetter = new StructUnionOrEnumInfoGetter();
			n.accept(aggregateInfoGetter);
			if (aggregateInfoGetter.isUserDefinedDefinitionOrAssociatedTypedef) {
				// Simplification would be required to separate out the definition of
				// user-defined types.
				// No further checks (such as multiple declarators) are required in this
				// context.
				needsSimplification = true;
				return;
			} else {
				// This accept would look into the simplification requirements, if any, of the
				// initializer(s).
				n.getF1().accept(this);
				if (!needsSimplification) {
					// If the flag isn't set yet, check if simplification is required due to
					// multiple declarators.
					InitDeclaratorList initDeclList = (InitDeclaratorList) n.getF1().getNode();
					if (initDeclList.getF1().present()) {
						needsSimplification = true;
					}
				}
			}
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public void visit(Expression n) {
			if (n.getExpF1().present()) {
				needsSimplification = true;
			}
			n.getExpF0().accept(this);
		}

		/**
		 * f0 ::= LogicalORExpression()
		 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
		 */
		@Override
		public void visit(ConditionalExpression n) {
			if (n.getF1().present()) {
				needsSimplification = true;
			}
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= LogicalANDExpression()
		 * f1 ::= ( "||" LogicalORExpression() )?
		 */
		@Override
		public void visit(LogicalORExpression n) {
			if (n.getF1().present()) {
				needsSimplification = true;
			}
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= InclusiveORExpression()
		 * f1 ::= ( "&&" LogicalANDExpression() )?
		 */
		@Override
		public void visit(LogicalANDExpression n) {
			if (n.getF1().present()) {
				needsSimplification = true;
			}
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(SizeofUnaryExpression n) {
			// No visit in this subtree.
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= "("
		 * f2 ::= TypeName()
		 * f3 ::= ")"
		 */
		@Override
		public void visit(SizeofTypeName n) {
			// No visit in this subtree.
		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( ExpressionList() )?
		 * f2 ::= ")"
		 */
		@Override
		public void visit(ArgumentList n) {
			needsSimplification = true;
		}

		/**
		 * f0 ::= "("
		 * f1 ::= Expression()
		 * f2 ::= ")"
		 */
		@Override
		public void visit(ExpressionClosed n) {
			if (Misc.isSimplePrimaryExpression(n.getF1())) {
				needsSimplification = true;
			}
		}
	}
}
