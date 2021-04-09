/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis;

import imop.ast.info.cfgNodeInfo.ParameterDeclarationInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AssignmentGetter {
	/**
	 * Given a node {@code node}, this method returns a list of
	 * {@link Assignment} that may
	 * possibly happen during the execution of the {@code node},
	 * and are lexically within the node.
	 * Note that the assignment of arguments to parameters, and returned value
	 * to the returned receiver are not considered as lexically enclosed
	 * assignments.
	 * Hence, an assignment may represent any of the following forms:
	 * <ol>
	 * <li>NonConditionalExpressions, e.g., {@code x = y + 3}</li>
	 * <li>Initializations, e.g., {@code int x = y + 3}</li>
	 * <li>OmpForInitExpression, e.g., x = 0</li>
	 * </ol>
	 * 
	 * IMPORTANT: Note that currently we do not consider following operations as
	 * assignments:
	 * <ul>
	 * <li>prefix and postfix ++ and -- operators.</li>
	 * <li>any short-hand assignment, e.g., x += y, or x -= y.</li>
	 * <li>any assignment within OmpForReinitExpression.</li>
	 * </ul>
	 * These expression forms would be considered as assignments, only after
	 * ExpressionTree has been created for all the parsed Expressions.
	 * 
	 * @param node
	 *             node for which the possible {@link Assignment}s are required.
	 * @return
	 *         a list of {@link Assignment}s that may get executed, and are
	 *         lexically present within the node {@code node}.
	 */
	public static List<Assignment> getLexicalAssignments(Node node) {
		List<Assignment> assignmentList = new LinkedList<>();
		if (node == null) {
			return assignmentList;
		}
		for (Node leafContent : node.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
			if (node instanceof PreCallNode || node instanceof PostCallNode) {
				continue;
			}
			AssignmentExtractor assignmentExtractor = new AssignmentExtractor();
			leafContent.accept(assignmentExtractor);
			assignmentList.addAll(assignmentExtractor.asssignmentList);
		}
		return assignmentList;
	}

	/**
	 * Given a node {@code node}, this method returns a list of
	 * {@link Assignment} that may
	 * possibly happen during the execution of the {@code node}.
	 * This list will also contain assignments that may happen in any of the
	 * called procedures, and also the assignments of arguments to parameters,
	 * and returned values to return receiver.
	 * Hence an assignment may represent any of the following form:
	 * <ol>
	 * <li>NonConditionalExpressions, e.g., {@code x = y + 3}</li>
	 * <li>Initializations, e.g., {@code int x = y + 3}</li>
	 * <li>Argument passing, e.g., <code>foo(y+3)</code>, for a method
	 * <code>void foo(int x) {...}</code></li>
	 * <li>Returned values, e.g., <code>x = bar();</code>, for a method
	 * <code>int bar() {...; return y + 3;}</code></li>
	 * <li>OmpForInitExpression, e.g., x = 0</li>
	 * </ol>
	 * 
	 * IMPORTANT: Note that currently we do not consider following operations as
	 * assignments:
	 * <ul>
	 * <li>prefix and postfix ++ and -- operators.</li>
	 * <li>any short-hand assignment, e.g., x += y, or x -= y.</li>
	 * <li>any assignment within OmpForReinitExpression.</li>
	 * </ul>
	 * These expression forms would be considered as assignments, only after
	 * ExpressionTree has been created for all the parsed Expressions.
	 * 
	 * @param node
	 *             node for which the possible {@link Assignment}s are required.
	 * @return
	 *         a list of {@link Assignment}s that may get executed, and are
	 *         reachable within the node {@code node} (or its called functions).
	 */
	public static List<Assignment> getInterProceduralAssignments(Node node) {
		List<Assignment> assignmentList = new LinkedList<>();
		if (node == null) {
			return assignmentList;
		}
		for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			AssignmentExtractor assignmentExtractor = new AssignmentExtractor();
			leafContent.accept(assignmentExtractor);
			assignmentList.addAll(assignmentExtractor.asssignmentList);
		}
		return assignmentList;
	}

	private static class AssignmentExtractor extends DepthFirstProcess {
		public List<Assignment> asssignmentList = new LinkedList<>();

		/**
		 * f0 ::= Declarator()
		 * f1 ::= ( "=" Initializer() )?
		 */
		@Override
		public void visit(InitDeclarator n) {
			if (n.getF1().present()) {
				Initializer init = (Initializer) ((NodeSequence) n.getF1().getNode()).getNodes().get(1);
				this.asssignmentList.add(new Assignment(n.getF0(), init));
			}
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForInitExpression n) {
			this.asssignmentList.add(new Assignment(n.getF0(), n.getF2()));
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "+="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(ShortAssignPlus n) {
			// TODO: Code here after ExpressionTree is created.
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "-="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(ShortAssignMinus n) {
			// TODO: Code here after ExpressionTree is created.
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
			// TODO: Code here after ExpressionTree is created.
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
			// TODO: Code here after ExpressionTree is created.
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
			// TODO: Code here after ExpressionTree is created.
		}

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public void visit(NonConditionalExpression n) {
			String operator = ((NodeToken) n.getF1().getF0().getChoice()).getTokenImage();
			if (operator.equals("=")) {
				this.asssignmentList.add(new Assignment(n.getF0(), n.getF2()));
			} else {
				// TODO: Code here after ExpressionTree is created.
			}
		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public void visit(UnarySizeofExpression n) {
			// The children must not be visited here.
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(SizeofUnaryExpression n) {
			// The children must not be visited here.
		}

		@Override
		public void visit(PreCallNode n) {
			/*
			 * TODO: THIS IS UNCONVENTIONAL AND UNEXPECTED! Later, shift this
			 * code to the visit of a ParameterDeclaration instead.
			 * However, while doing that, try to be on valid-paths.
			 */
			CallStatement callStmt = n.getParent();
			for (FunctionDefinition funcDef : callStmt.getInfo().getCalledDefinitions()) {
				List<ParameterDeclaration> paramList = funcDef.getInfo().getCFGInfo().getParameterDeclarationList();
				if (paramList.size() == 1 && paramList.get(0).toString().trim().equals("void")) {
					paramList = new ArrayList<>();
				}
				assert (paramList.size() == n.getArgumentList().size())
						: "Parameter list " + paramList + " does not match with " + n.getArgumentList();
				int index = 0;
				for (SimplePrimaryExpression spe : n.getArgumentList()) {
					ParameterDeclaration param = paramList.get(index);
					if (spe.isAConstant()) {
						this.asssignmentList.add(new Assignment(ParameterDeclarationInfo.getRootParamNodeToken(param),
								spe.getConstant()));
					} else if (spe.isAnIdentifier()) {
						this.asssignmentList.add(new Assignment(ParameterDeclarationInfo.getRootParamNodeToken(param),
								spe.getIdentifier()));
					}
					index++;
				}
			}
		}

		@Override
		public void visit(PostCallNode n) {
			if (!n.hasReturnReceiver()) {
				return;
			}
			CallStatement callStmt = n.getParent();
			for (FunctionDefinition funcDef : callStmt.getInfo().getCalledDefinitions()) {
				for (ReturnStatement ret : Misc.getInheritedEnclosee(funcDef, ReturnStatement.class)) {
					Expression e = ret.getInfo().getReturnExpression();
					if (e == null) {
						continue;
					}
					this.asssignmentList.add(new Assignment(n.getReturnReceiver().getIdentifier(), e));
				}
			}
		}

	}
}
