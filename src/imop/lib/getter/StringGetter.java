/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import imop.ast.annotation.*;
import imop.ast.info.NodeInfo;
import imop.ast.info.StatementInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.transform.BasicTransform;
import imop.lib.util.Misc;
import imop.parser.CParserConstants;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Obtain String representation of the code corresponding to the current state
 * of AST.
 */
public class StringGetter {

	public static String getString(Node node) {
		return StringGetter.getString(node, false);
	}

	public static String getString(Node node, boolean withPragma) {
		List<Commentor> defaultCommentor = new ArrayList<>();
		defaultCommentor.add(NodeInfo.getDefaultCommentor());
		InternalStringGetter stringGetter = new InternalStringGetter(defaultCommentor, withPragma);
		node.accept(stringGetter);
		return stringGetter.str.toString().replaceAll("\n\\s*\n", "\n");
	}

	public static String getString(Node node, List<Commentor> commentorList) {
		return StringGetter.getString(node, commentorList, false);
	}

	public static String getString(Node node, List<Commentor> commentorList, boolean withPragma) {
		if (!commentorList.contains(NodeInfo.getDefaultCommentor())) {
			commentorList = new ArrayList<>(commentorList);
			commentorList.add(NodeInfo.getDefaultCommentor());
		}
		InternalStringGetter stringGetter = new InternalStringGetter(commentorList, withPragma);
		node.accept(stringGetter);
		return stringGetter.str.toString().replaceAll("\n\\s*\n", "\n");
	}

	public static String getStringNoLabel(Node node) {
		InternalStringNoLabel stringGetter = new InternalStringNoLabel();
		node.accept(stringGetter);
		return stringGetter.str.toString().replaceAll("\n\\s*\n", "\n");
	}

	public static String getRenamedString(Node node, HashMap<String, String> renamingMap) {
		RenamingStringGetter stringGetter = new RenamingStringGetter(renamingMap);
		node.accept(stringGetter);
		return stringGetter.str.toString().replaceAll("\n\\s*\n", "\n");
	}

	/**
	 * Obtain string equivalent of {@code baseNode}, with the string of
	 * {@code changeSource} replaced by the string
	 * {@code replacementString}
	 * (if the replacement would actually be possible in the AST).
	 * <br>
	 * Note that no resulting changes are done to the AST of {@code baseNode} or
	 * {@code changeSource}.
	 * Also note that this method is very different from a simple substring
	 * replacement.
	 * 
	 * @param baseNode
	 *                          node whose string equivalent is to be found, with
	 *                          requested
	 *                          replacements.
	 * @param changeSource
	 *                          node whose string equivalent should be replaced with
	 *                          {@code changeDestination}, in the return value.
	 * @param replacementString
	 *                          string that should replace the string of
	 *                          {@code changeSource}
	 *                          in the return value.
	 * @return
	 *         string of {@code baseNode}, with the string of
	 *         {@code changeSource}, replaced by {@code changeDestination}.
	 */
	public static String getNodeReplacedString(Node baseNode, Node changeSource, String replacementString) {
		try {
			Class<? extends Node> sourceClass = changeSource.getClass();
			Node changeDestination = FrontEnd.parseAndNormalize(replacementString, sourceClass);
			BasicTransform.crudeReplaceOldWithNew(changeSource, changeDestination);
			String str = baseNode.toString();
			BasicTransform.crudeReplaceOldWithNew(changeDestination, changeSource);
			return str;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getStringWithPragms(Node baseNode) {
		return StringGetter.getString(baseNode, true);
	}

	@FunctionalInterface
	public static interface Commentor {
		public String getString(Node node);
	}

	public static class Positioner extends InternalStringGetter {
		int lineCounter;

		public Positioner() {
			this.lineCounter = 1;
		}

		public Positioner(int lineCounter) {
			this.lineCounter = lineCounter;
		}

		@Override
		protected void startNewlineNoTab() {
			lineCounter++;
			super.startNewlineNoTab();
		}

		@Override
		protected void startNewline() {
			lineCounter++;
			super.startNewline();
		}

		@Override
		public void visit(NodeToken n) {
			String res = n.getTokenImage();
			if (res.equals("\t")) {
				return;
			}
			n.setLineNum(lineCounter);
			int columnNum;
			if (str.lastIndexOf("\n") != -1) {
				columnNum = str.substring(str.lastIndexOf("\n")).length() + 1;
			} else {
				columnNum = 0;
			}
			n.setColumnNum(columnNum);
			str.append(res);
			return;
		}

		@Override
		public void visit(CallStatement n) {
			printCommentorsAndPragmas(n.getPreCallNode());
			printCommentorsAndPragmas(n.getPostCallNode());
			NodeToken fdn = n.getFunctionDesignatorNode();
			if (fdn != null) {
				fdn.setLineNum(lineCounter);
				fdn.setColumnNum(0);
			}
		}

		@Override
		protected void printCommentorsAndPragmas(Node n) {
			if (n == null) {
				return;
			}
			n = Misc.getCFGNodeFor(n);
			/*
			 * Print all imop pragma annotations before the comments.
			 */
			if (this.withPragma) {
				for (PragmaImop annotation : n.getInfo().getPragmaAnnotations()) {
					str.append(annotation.toString());
					lineCounter += 2;
				}
			}

			/*
			 * Print comments.
			 */
			for (Commentor commentor : this.commentorList) {
				String content = commentor.getString(n);
				if (content.isEmpty()) {
					continue;
				}

				StringBuilder fancy = new StringBuilder();
				fancy.append(System.getProperty("line.separator"));
				for (int tabCount = 0; tabCount < tabs; tabCount++) {
					for (int i = 0; i < 4; i++) {
						fancy.append(' ');
					}
				}
				for (int i = 0; i < content.length(); i++) {
					if (content.charAt(i) == '\n') {
						lineCounter++;
					}
				}
				content = content.replaceAll("\n", fancy.toString());
				content = content.trim();
				content = content.replace("/*", "//");
				content = content.replace("*/", "//");
				this.startNewline();
				str.append("/*");
				str.append(content);
				str.append("*/");
				this.startNewline();
			}
		}
	}

	private static class RenamingStringGetter extends InternalStringGetter {
		HashMap<String, String> renamingMap;

		public RenamingStringGetter(HashMap<String, String> renamingMap) {
			this.renamingMap = renamingMap;
		}

		/**
		 * f0 ::= ( DeclarationSpecifiers() )?
		 * f1 ::= Declarator()
		 * f2 ::= ( DeclarationList() )?
		 * f3 ::= CompoundStatement()
		 */
		@Override
		public void visit(FunctionDefinition n) {
			for (ParameterDeclaration param : n.getInfo().getCFGInfo().getParameterDeclarationList()) {
				printCommentorsAndPragmas(param);
			}
			printCommentorsAndPragmas(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			if (n.getF2().present()) {
				n.getF2().getNode().accept(this);
				this.startNewlineNoTab();
			}
			/*
			 * Now, remove all those mappings that may get redefined in the
			 * symbol table of this scope,
			 * before entering the scope.
			 */
			HashMap<String, String> hiddenMap = new HashMap<>();
			for (String internalSymbolName : n.getInfo().getSymbolTable().keySet()) {
				if (renamingMap.containsKey(internalSymbolName)) {
					hiddenMap.put(internalSymbolName, renamingMap.get(internalSymbolName));
					renamingMap.remove(internalSymbolName);
				}
			}
			n.getF3().accept(this);
			/*
			 * Restore all mappings back now.
			 */
			for (String addMapping : hiddenMap.keySet()) {
				renamingMap.put(addMapping, hiddenMap.get(addMapping));
			}
		}

		/**
		 * f0 ::= "{"
		 * f1 ::= ( CompoundStatementElement() )*
		 * f2 ::= "}"
		 */
		@Override
		public void visit(CompoundStatement n) {
			printLabels(n);
			n.getF0().accept(this);
			// TODO Check: Shouldn't the pragmas be printed outside the body?
			printCommentorsAndPragmas(n); // Note that the comments of a CompoundStatement are always printed inside its
											// body.
			if (n.getF1().present()) {
				tabs++;
				/*
				 * Now, remove all those mappings that may get redefined in the
				 * symbol table of this scope,
				 * before entering the scope.
				 */
				HashMap<String, String> hiddenMap = new HashMap<>();
				for (String removeMapping : hiddenMap.keySet()) {
					renamingMap.remove(removeMapping);
				}
				for (Node element : n.getF1().getNodes()) {
					Node choice = ((CompoundStatementElement) element).getF0().getChoice();
					if (choice instanceof Statement) {
						this.newLineForStatement((Statement) choice);
					} else {
						List<String> idList = ((Declaration) choice).getInfo().getIDNameList();
						for (String internalSymbolName : idList) {
							if (renamingMap.containsKey(internalSymbolName)) {
								hiddenMap.put(internalSymbolName, renamingMap.get(internalSymbolName));
								renamingMap.remove(internalSymbolName);
							}
						}
						this.startNewline();
					}
					element.accept(this);
				}
				/*
				 * Restore all mappings back now.
				 */
				for (String addMapping : hiddenMap.keySet()) {
					renamingMap.put(addMapping, hiddenMap.get(addMapping));
				}
				tabs--;
			}
			this.startNewline();
			n.getF2().accept(this);
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
			str.append(" ");
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			n.getF1().accept(this);
			origName = n.getF2().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF2().accept(this);
			}
			str.append(" ");
			n.getF3().accept(this);
			str.append(" ");
			n.getF4().accept(this);
			n.getF5().accept(this);
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
			str.append(" ");
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			n.getF1().accept(this);
			origName = n.getF2().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF2().accept(this);
			}
			n.getF3().accept(this);
			n.getF4().accept(this);
			n.getF5().accept(this);
			n.getF6().accept(this);
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
			if (n.getF0().getChoice() instanceof NodeToken) {
				NodeToken nodeToken = (NodeToken) n.getF0().getChoice();
				if (nodeToken.getKind() == CParserConstants.IDENTIFIER) {
					String origName = nodeToken.getTokenImage();
					if (renamingMap.containsKey(origName)) {
						str.append(renamingMap.get(origName));
					} else {
						n.getF0().accept(this);
					}
				} else {
					n.getF0().accept(this);
				}
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ( "," <IDENTIFIER> )*
		 */
		@Override
		public void visit(VariableList n) {
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			for (Node nodeSeqNode : n.getF1().getNodes()) {
				NodeSequence nodeSeq = (NodeSequence) nodeSeqNode;
				str.append(", ");
				origName = ((NodeToken) nodeSeq.getNodes().get(1)).getTokenImage();
				if (renamingMap.containsKey(origName)) {
					str.append(renamingMap.get(origName));
				} else {
					nodeSeq.getNodes().get(1).accept(this);
				}
			}
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForInitExpression n) {
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<"
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForLTCondition n) {
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForLECondition n) {
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">"
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForGTCondition n) {
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForGECondition n) {
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "++"
		 */
		@Override
		public void visit(PostIncrementId n) {
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "--"
		 */
		@Override
		public void visit(PostDecrementId n) {
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(PreIncrementId n) {
			n.getF0().accept(this);
			String origName = n.getF1().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF1().accept(this);
			}
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(PreDecrementId n) {
			n.getF0().accept(this);
			String origName = n.getF1().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF1().accept(this);
			}
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "+="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(ShortAssignPlus n) {
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "-="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(ShortAssignMinus n) {
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
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
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			origName = n.getF2().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF2().accept(this);
			}
			str.append(" ");
			n.getF3().accept(this);
			str.append(" ");
			n.getF4().accept(this);
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
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			origName = n.getF2().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF2().accept(this);
			}
			str.append(" ");
			n.getF3().accept(this);
			str.append(" ");
			n.getF4().accept(this);
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
			String origName = n.getF0().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF0().accept(this);
			}
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
			str.append(" ");
			n.getF3().accept(this);
			str.append(" ");
			origName = n.getF4().getTokenImage();
			if (renamingMap.containsKey(origName)) {
				str.append(renamingMap.get(origName));
			} else {
				n.getF4().accept(this);
			}
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * | Constant()
		 * | ExpressionClosed()
		 */
		@Override
		public void visit(PrimaryExpression n) {
			if (n.getF0().getChoice() instanceof NodeToken) {
				String id = ((NodeToken) n.getF0().getChoice()).getTokenImage();
				if (renamingMap.containsKey(id)) {
					str.append(renamingMap.get(id));
				} else {
					str.append(id);
				}
			} else {
				n.getF0().accept(this);
			}
		}

		@Override
		public void visit(CallStatement n) {
			printCommentorsAndPragmas(n.getPreCallNode());
			printLabels(n);
			CallStatement node = n;
			if (node.getPostCallNode().hasReturnReceiver()) {
				node.getPostCallNode().getReturnReceiver().accept(this);
				str.append(" = ");
			}
			if (node.getFunctionDesignatorNode() == null) {
				str.append("__context-insensitive();");
			} else {
				str.append(node.getFunctionDesignatorNode().getTokenImage() + "(");
				List<SimplePrimaryExpression> argumentList = node.getPreCallNode().getArgumentList();
				for (SimplePrimaryExpression spe : argumentList) {
					if (spe == argumentList.get(argumentList.size() - 1)) {
						break;
					}
					spe.accept(this);
					str.append(", ");
				}
				if (argumentList.size() != 0) {
					argumentList.get(argumentList.size() - 1).accept(this);
				}
				str.append(");");
			}
			printCommentorsAndPragmas(n.getPostCallNode());
		}

		@Override
		public void visit(SimplePrimaryExpression n) {
			if (n.isAConstant()) {
				str.append(n.getString());
			} else {
				NodeToken idToken = n.getIdentifier();
				String id = idToken.getTokenImage();
				if (renamingMap.containsKey(id)) {
					str.append(renamingMap.get(id));
				} else {
					str.append(id);
				}
			}
		}

	}

	private static class InternalStringNoLabel extends InternalStringGetter {
		@Override
		protected void printLabels(Node n) {
			// Do nothing.
		}
	}

	private static class InternalStringGetter extends DepthFirstProcess {
		protected List<Commentor> commentorList = new ArrayList<>();

		public StringBuilder str = new StringBuilder();
		protected int tabs = 0;
		protected final boolean withPragma;

		protected InternalStringGetter() {
			this.withPragma = false;
		}

		public InternalStringGetter(List<Commentor> commentorList, boolean withPragma) {
			this.commentorList = commentorList;
			this.withPragma = withPragma;
		}

		/**
		 * @param stmt
		 */
		protected void newLineForStatement(Statement stmt) {
			Node stmtChoice = stmt.getStmtF0().getChoice();
			if (stmtChoice instanceof UnknownPragma || stmtChoice instanceof OmpConstruct
					|| stmtChoice instanceof OmpDirective || stmtChoice instanceof UnknownCpp) {
				this.startNewlineNoTab();
			} else {
				this.startNewline();
			}
		}

		protected void newSpaceForStatement(Statement stmt) {
			Node stmtChoice = stmt.getStmtF0().getChoice();
			if (stmtChoice instanceof UnknownPragma || stmtChoice instanceof OmpConstruct
					|| stmtChoice instanceof OmpDirective || stmtChoice instanceof UnknownCpp) {
				this.startNewlineNoTab();
			} else {
				if (stmtChoice instanceof CompoundStatement) {
					str.append(" ");
				} else {
					tabs++;
					this.startNewline();
					tabs--;
				}
			}
		}

		protected void startNewlineNoTab() {
			str.append(System.getProperty("line.separator"));
		}

		protected void startNewline() {
			str.append(System.getProperty("line.separator"));
			for (int tabCount = 0; tabCount < tabs; tabCount++) {
				for (int i = 0; i < 4; i++) {
					str.append(' ');
				}
			}
		}

		public void visit(Node n) {
			return;
		}

		@Override
		public void visit(NodeOptional n) {
			if (n.present()) {
				n.getNode().accept(this);
			} else {
				// Do nothing.
			}
		}

		@Override
		public void visit(NodeList n) {
			List<Node> nodeList = n.getNodes();
			for (Node node : nodeList) {
				node.accept(this);
				if (node != nodeList.get(nodeList.size() - 1)) {
					str.append(" ");
				}
			}
		}

		@Override
		public void visit(NodeListOptional n) {
			List<Node> nodeList = n.getNodes();
			for (Node node : nodeList) {
				node.accept(this);
				if (node != nodeList.get(nodeList.size() - 1)) {
					str.append(" ");
				}
			}
		}

		@Override
		public void visit(NodeSequence n) {
			List<Node> nodeList = n.getNodes();
			for (Node node : nodeList) {
				node.accept(this);
				if (node != nodeList.get(nodeList.size() - 1)) {
					str.append(" ");
				}
			}
		}

		@Override
		public void visit(NodeToken n) {
			String res = n.getTokenImage();

			// Note: No idea why do we need the next if statement.
			if (res.equals("\t")) {
				System.out.println("Found it!");
				return;
			}

			str.append(res);
			return;
		}

		//
		// User-generated visitor methods below
		//

		/**
		 * f0 ::= ( ElementsOfTranslation() )+
		 */
		@Override
		public void visit(TranslationUnit n) {
			for (Node elements : n.getF0().getNodes()) {
				elements.accept(this);
				if (elements != n.getF0().getNodes().get(n.getF0().getNodes().size() - 1)) {
					this.startNewlineNoTab();
				}
			}
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
			for (ParameterDeclaration param : n.getInfo().getCFGInfo().getParameterDeclarationList()) {
				printCommentorsAndPragmas(param);
			}
			printCommentorsAndPragmas(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			if (n.getF2().present()) {
				n.getF2().getNode().accept(this);
				this.startNewlineNoTab();
			}
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ( InitDeclaratorList() )?
		 * f2 ::= ";"
		 */
		@Override
		public void visit(Declaration n) {
			printCommentorsAndPragmas(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= ( Declaration() )+
		 */
		@Override
		public void visit(DeclarationList n) {
			for (Node decl : n.getF0().getNodes()) {
				this.startNewlineNoTab();
				decl.accept(this);
			}
		}

		/**
		 * f0 ::= ( ADeclarationSpecifier() )+
		 */
		@Override
		public void visit(DeclarationSpecifiers n) {
			for (Node declSpec : n.getF0().getNodes()) {
				declSpec.accept(this);
				if (declSpec != n.getF0().getNodes().get(n.getF0().getNodes().size() - 1)) {
					str.append(" ");
				}
			}
		}

		/**
		 * f0 ::= StorageClassSpecifier()
		 * | TypeSpecifier()
		 * | TypeQualifier()
		 */
		@Override
		public void visit(ADeclarationSpecifier n) {
			n.getF0().accept(this);
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
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= ( <VOID> | <CHAR> | <SHORT> | <INT> | <LONG> | <FLOAT> |
		 * <DOUBLE> |
		 * <SIGNED> | <UNSIGNED> | StructOrUnionSpecifier() | EnumSpecifier() |
		 * TypedefName() )
		 */
		@Override
		public void visit(TypeSpecifier n) {
			n.getF0().accept(this);
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
			n.getF0().accept(this);
		}

		/**
		 * 
		 * f0 ::= ( StructOrUnionSpecifierWithList() |
		 * StructOrUnionSpecifierWithId()
		 * )
		 */
		@Override
		public void visit(StructOrUnionSpecifier n) {
			n.getF0().accept(this);
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
			n.getF0().accept(this);
			str.append(" ");
			if (n.getF1().present()) {
				n.getF1().accept(this);
				str.append(" ");
			}
			n.getF2().accept(this);
			tabs++;
			for (Node listNode : n.getF3().getF0().getNodes()) {
				this.startNewline();
				listNode.accept(this);
			}
			tabs--;
			this.startNewline();
			n.getF4().accept(this);
		}

		/**
		 * f0 ::= StructOrUnion()
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(StructOrUnionSpecifierWithId n) {
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= <STRUCT>
		 * | <UNION>
		 */
		@Override
		public void visit(StructOrUnion n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= ( StructDeclaration() )+
		 */
		@Override
		public void visit(StructDeclarationList n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= InitDeclarator()
		 * f1 ::= ( "," InitDeclarator() )*
		 */
		@Override
		public void visit(InitDeclaratorList n) {
			n.getF0().accept(this);
			for (Node nodeSeq : n.getF1().getNodes()) {
				str.append(", ");
				((NodeSequence) nodeSeq).getNodes().get(1).accept(this);
			}
		}

		/**
		 * f0 ::= Declarator()
		 * f1 ::= ( "=" Initializer() )?
		 */
		@Override
		public void visit(InitDeclarator n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				str.append(" ");
				n.getF1().getNode().accept(this);
			}
		}

		/**
		 * f0 ::= SpecifierQualifierList()
		 * f1 ::= StructDeclaratorList()
		 * f2 ::= ";"
		 */
		@Override
		public void visit(StructDeclaration n) {
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= ( ASpecifierQualifier() )+
		 */
		@Override
		public void visit(SpecifierQualifierList n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= TypeSpecifier()
		 * | TypeQualifier()
		 */
		@Override
		public void visit(ASpecifierQualifier n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= StructDeclarator()
		 * f1 ::= ( "," StructDeclarator() )*
		 */
		@Override
		public void visit(StructDeclaratorList n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= StructDeclaratorWithDeclarator()
		 * | StructDeclaratorWithBitField()
		 */
		@Override
		public void visit(StructDeclarator n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= Declarator()
		 * f1 ::= ( ":" ConstantExpression() )?
		 */
		@Override
		public void visit(StructDeclaratorWithDeclarator n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= ":"
		 * f1 ::= ConstantExpression()
		 */
		@Override
		public void visit(StructDeclaratorWithBitField n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= EnumSpecifierWithList()
		 * | EnumSpecifierWithId()
		 */
		@Override
		public void visit(EnumSpecifier n) {
			n.getF0().accept(this);
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
			n.getF0().accept(this);
			str.append(" ");
			if (n.getF1().present()) {
				n.getF1().getNode().accept(this);
				str.append(" ");
			}
			n.getF2().accept(this);
			tabs++;
			this.startNewline();
			n.getF3().accept(this);
			tabs--;
			this.startNewline();
			n.getF4().accept(this);
		}

		/**
		 * f0 ::= <ENUM>
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(EnumSpecifierWithId n) {
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= Enumerator()
		 * f1 ::= ( "," Enumerator() )*
		 */
		@Override
		public void visit(EnumeratorList n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ( "=" ConstantExpression() )?
		 */
		@Override
		public void visit(Enumerator n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				str.append(" ");
			}
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= ( Pointer() )?
		 * f1 ::= DirectDeclarator()
		 */
		@Override
		public void visit(Declarator n) {
			n.getF0().accept(this);
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
			for (Node node : n.getF0().getNodes()) {
				node.accept(this);
			}
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
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( ParameterTypeList() )?
		 * f2 ::= ")"
		 */
		@Override
		public void visit(ParameterTypeListClosed n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( OldParameterList() )?
		 * f2 ::= ")"
		 */
		@Override
		public void visit(OldParameterListClosed n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
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
			n.getF0().accept(this);
			n.getF1().accept(this);
			if (n.getF1().present()) {
				str.append(" ");
			}
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= ( TypeQualifier() )+
		 */
		@Override
		public void visit(TypeQualifierList n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= ParameterList()
		 * f1 ::= ( "," "..." )?
		 */
		@Override
		public void visit(ParameterTypeList n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
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
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
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
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * | ArrayInitializer()
		 */
		@Override
		public void visit(Initializer n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "{"
		 * f1 ::= InitializerList()
		 * f2 ::= ( "," )?
		 * f3 ::= "}"
		 */
		@Override
		public void visit(ArrayInitializer n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= Initializer()
		 * f1 ::= ( "," Initializer() )*
		 */
		@Override
		public void visit(InitializerList n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= SpecifierQualifierList()
		 * f1 ::= ( AbstractDeclarator() )?
		 */
		@Override
		public void visit(TypeName n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				str.append(" ");
			}
			n.getF1().accept(this);
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
			n.getF0().accept(this);
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
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= ( ADimensionOrParameter() )*
		 */
		@Override
		public void visit(DimensionOrParameterList n) {
			for (Node node : n.getF0().getNodes()) {
				node.accept(this);
			}
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
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= ( LabeledStatement() | ExpressionStatement() |
		 * CompoundStatement()
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
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= <OMP_CR>
		 * | <OMP_NL>
		 */
		@Override
		public void visit(OmpEol n) {
			this.startNewline();
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
			for (OmpClause clause : Misc.getClauseList(n)) {
				if (clause instanceof IfClause) {
					printCommentorsAndPragmas(clause);
				} else if (clause instanceof NumThreadsClause) {
					printCommentorsAndPragmas(clause);
				}
			}
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getParConsF0().accept(this);
			str.append(" ");
			n.getParConsF1().accept(this);
			this.newLineForStatement(n.getParConsF2());
			n.getParConsF2().accept(this);
		}

		/**
		 * f0 ::= "#"
		 * f1 ::= <PRAGMA>
		 * f2 ::= <OMP>
		 */
		@Override
		public void visit(OmpPragma n) {
			str.append("\n");
			n.getF0().accept(this);
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= "#"
		 * f1 ::= <PRAGMA>
		 * f2 ::= <UNKNOWN_CPP>
		 */
		@Override
		public void visit(UnknownPragma n) {
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <PARALLEL>
		 * f1 ::= UniqueParallelOrDataClauseList()
		 * f2 ::= OmpEol()
		 */
		@Override
		public void visit(ParallelDirective n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= ( AUniqueParallelOrDataClause() )*
		 */
		@Override
		public void visit(UniqueParallelOrDataClauseList n) {
			if (n.getF0().present()) {
				for (Node aUniqueParallelOrDataClause : n.getF0().getNodes()) {
					str.append(" ");
					aUniqueParallelOrDataClause.accept(this);
				}
			}
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
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= <NUM_THREADS>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public void visit(NumThreadsClause n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
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
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= <PRIVATE>
		 * f1 ::= "("
		 * f2 ::= VariableList()
		 * f3 ::= ")"
		 */
		@Override
		public void visit(OmpPrivateClause n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= <FIRSTPRIVATE>
		 * f1 ::= "("
		 * f2 ::= VariableList()
		 * f3 ::= ")"
		 */
		@Override
		public void visit(OmpFirstPrivateClause n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= <LASTPRIVATE>
		 * f1 ::= "("
		 * f2 ::= VariableList()
		 * f3 ::= ")"
		 */
		@Override
		public void visit(OmpLastPrivateClause n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= <SHARED>
		 * f1 ::= "("
		 * f2 ::= VariableList()
		 * f3 ::= ")"
		 */
		@Override
		public void visit(OmpSharedClause n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= <COPYIN>
		 * f1 ::= "("
		 * f2 ::= VariableList()
		 * f3 ::= ")"
		 */
		@Override
		public void visit(OmpCopyinClause n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= <DFLT>
		 * f1 ::= "("
		 * f2 ::= <SHARED>
		 * f3 ::= ")"
		 */
		@Override
		public void visit(OmpDfltSharedClause n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= <DFLT>
		 * f1 ::= "("
		 * f2 ::= <NONE>
		 * f3 ::= ")"
		 */
		@Override
		public void visit(OmpDfltNoneClause n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
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
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
			n.getF4().accept(this);
			n.getF5().accept(this);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= ForDirective()
		 * f2 ::= OmpForHeader()
		 * f3 ::= Statement()
		 */
		@Override
		public void visit(ForConstruct n) {
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
			this.newSpaceForStatement(n.getF3());
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= <FOR>
		 * f1 ::= UniqueForOrDataOrNowaitClauseList()
		 * f2 ::= OmpEol()
		 */
		@Override
		public void visit(ForDirective n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			this.startNewline();
		}

		/**
		 * f0 ::= ( AUniqueForOrDataOrNowaitClause() )*
		 */
		@Override
		public void visit(UniqueForOrDataOrNowaitClauseList n) {
			if (n.getF0().present()) {
				for (Node aUniqueForOrDataOrNowaitClause : n.getF0().getNodes()) {
					str.append(" ");
					aUniqueForOrDataOrNowaitClause.accept(this);
				}
			}
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
			n.getF0().accept(this);
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
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
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
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			if (n.getF3().present()) {
				NodeSequence nodeSeq = (NodeSequence) n.getF3().getNode();
				str.append(", ");
				nodeSeq.getNodes().get(1).accept(this);
			}
			n.getF4().accept(this);
		}

		/**
		 * f0 ::= <STATIC>
		 * | <DYNAMIC>
		 * | <GUIDED>
		 * | <RUNTIME>
		 */
		@Override
		public void visit(ScheduleKind n) {
			n.getF0().accept(this);
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
			printCommentorsAndPragmas(n.getF2());
			printCommentorsAndPragmas(n.getF4());
			printCommentorsAndPragmas(n.getF6());
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
			str.append(" ");
			n.getF4().accept(this);
			n.getF5().accept(this);
			str.append(" ");
			n.getF6().accept(this);
			n.getF7().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForInitExpression n) {
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= OmpForLTCondition()
		 * | OmpForLECondition()
		 * | OmpForGTCondition()
		 * | OmpForGECondition()
		 */
		@Override
		public void visit(OmpForCondition n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<"
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForLTCondition n) {
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForLECondition n) {
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">"
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForGTCondition n) {
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForGECondition n) {
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
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
			n.getOmpForReinitF0().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "++"
		 */
		@Override
		public void visit(PostIncrementId n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "--"
		 */
		@Override
		public void visit(PostDecrementId n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(PreIncrementId n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(PreDecrementId n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "+="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(ShortAssignPlus n) {
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "-="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(ShortAssignMinus n) {
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
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
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
			str.append(" ");
			n.getF3().accept(this);
			str.append(" ");
			n.getF4().accept(this);
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
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
			str.append(" ");
			n.getF3().accept(this);
			str.append(" ");
			n.getF4().accept(this);
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
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
			str.append(" ");
			n.getF3().accept(this);
			str.append(" ");
			n.getF4().accept(this);
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
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
			this.startNewline();
			n.getF4().accept(this);
		}

		/**
		 * f0 ::= ( ANowaitDataClause() )*
		 */
		@Override
		public void visit(NowaitDataClauseList n) {
			if (n.getF0().present()) {
				for (Node aNowaitDataClause : n.getF0().getNodes()) {
					str.append(" ");
					aNowaitDataClause.accept(this);
				}
			}
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
			tabs++;
			this.startNewlineNoTab();
			n.getF1().accept(this);
			for (Node aSectionNode : n.getF2().getNodes()) {
				this.startNewlineNoTab();
				aSectionNode.accept(this);
			}
			tabs--;
			this.startNewline();
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
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			this.startNewline();
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
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
			this.startNewline();
			n.getF4().accept(this);
		}

		/**
		 * f0 ::= ( ASingleClause() )*
		 */
		@Override
		public void visit(SingleClauseList n) {
			if (n.getF0().present()) {
				for (Node singleClause : n.getF0().getNodes()) {
					str.append(" ");
					singleClause.accept(this);
				}
			}
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
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
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
			for (OmpClause clause : Misc.getClauseList(n)) {
				if (clause instanceof IfClause) {
					printCommentorsAndPragmas(clause);
				} else if (clause instanceof FinalClause) {
					printCommentorsAndPragmas(clause);
				}
			}
			printCommentorsAndPragmas(n);

			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			if (n.getF2().present()) {
				for (Node tCList : n.getF2().getNodes()) {
					str.append(" ");
					tCList.accept(this);
				}
			}
			this.startNewline();
			n.getF4().accept(this);
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
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= <UNTIED>
		 */
		@Override
		public void visit(UntiedClause n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= <MERGEABLE>
		 */
		@Override
		public void visit(MergeableClause n) {
			n.getF0().accept(this);
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
			for (OmpClause clause : Misc.getClauseList(n)) {
				if (clause instanceof IfClause) {
					printCommentorsAndPragmas(clause);
				} else if (clause instanceof NumThreadsClause) {
					printCommentorsAndPragmas(clause);
				}
			}
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
			str.append(" ");
			n.getF3().accept(this);
			this.startNewline();
			n.getF5().accept(this);
			str.append(" ");
			this.newSpaceForStatement(n.getF6());
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
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
			str.append(" ");
			n.getF3().accept(this);
			this.startNewline();
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
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			this.startNewline();
			n.getF3().accept(this);
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
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
			this.startNewline();
			n.getF4().accept(this);
		}

		/**
		 * f0 ::= "("
		 * f1 ::= <IDENTIFIER>
		 * f2 ::= ")"
		 */
		@Override
		public void visit(RegionPhrase n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
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
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			if (n.getF2().present()) {
				str.append(" ");
				n.getF2().accept(this);
			}
			this.startNewline();
			n.getF4().accept(this);
		}

		/**
		 * f0 ::= <READ>
		 * | <WRITE>
		 * | <UPDATE>
		 * | <CAPTURE>
		 */
		@Override
		public void visit(AtomicClause n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <FLUSH>
		 * f2 ::= ( FlushVars() )?
		 * f3 ::= OmpEol()
		 */
		@Override
		public void visit(FlushDirective n) {
			printCommentorsAndPragmas(n);
			if (n instanceof DummyFlushDirective) {
				this.visit((DummyFlushDirective) n);
				return;
			}
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
			this.startNewline();
		}

		/**
		 * f0 ::= "("
		 * f1 ::= VariableList()
		 * f2 ::= ")"
		 */
		@Override
		public void visit(FlushVars n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <ORDERED>
		 * f2 ::= OmpEol()
		 * f3 ::= Statement()
		 */
		@Override
		public void visit(OrderedConstruct n) {
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			this.startNewline();
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <BARRIER>
		 * f2 ::= OmpEol()
		 */
		@Override
		public void visit(BarrierDirective n) {
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			this.startNewline();
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <TASKWAIT>
		 * f2 ::= OmpEol()
		 */
		@Override
		public void visit(TaskwaitDirective n) {
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			this.startNewline();
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <TASKYIELD>
		 * f2 ::= OmpEol()
		 */
		@Override
		public void visit(TaskyieldDirective n) {
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			this.startNewline();
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
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
			n.getF4().accept(this);
			this.startNewline();
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
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			str.append(" ");
			n.getF2().accept(this);
			n.getF3().accept(this);
			n.getF4().accept(this);
			n.getF5().accept(this);
			n.getF6().accept(this);
			n.getF7().accept(this);
			n.getF8().accept(this);
			n.getF9().accept(this);
			n.getF10().accept(this);
			this.startNewline();
		}

		/**
		 * f0 ::= ( TypeSpecifier() )*
		 */
		@Override
		public void visit(ReductionTypeList n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= AssignInitializerClause()
		 * | ArgumentInitializerClause()
		 */
		@Override
		public void visit(InitializerClause n) {
			n.getF0().accept(this);
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
			str.append(" ");
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			str.append(" ");
			n.getF3().accept(this);
			str.append(" ");
			n.getF4().accept(this);
			n.getF5().accept(this);
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
			str.append(" ");
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
			n.getF4().accept(this);
			n.getF5().accept(this);
			n.getF6().accept(this);
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
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ( "," <IDENTIFIER> )*
		 */
		@Override
		public void visit(VariableList n) {
			n.getF0().accept(this);
			for (Node nodeSeqNode : n.getF1().getNodes()) {
				NodeSequence nodeSeq = (NodeSequence) nodeSeqNode;
				str.append(", ");
				nodeSeq.getNodes().get(1).accept(this);
			}
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
			n.getF0().accept(this);
			n.getF1().accept(this);
			this.newSpaceForStatement(n.getF2());
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
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
			this.newSpaceForStatement(n.getF3());
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= <DFLT>
		 * f1 ::= ":"
		 * f2 ::= Statement()
		 */
		@Override
		public void visit(DefaultLabeledStatement n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			this.newSpaceForStatement(n.getF2());
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= ( Expression() )?
		 * f1 ::= ";"
		 */
		@Override
		public void visit(ExpressionStatement n) {
			printCommentorsAndPragmas(n);
			printLabels(n);

			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "{"
		 * f1 ::= ( CompoundStatementElement() )*
		 * f2 ::= "}"
		 */
		@Override
		public void visit(CompoundStatement n) {
			printLabels(n);
			n.getF0().accept(this);
			// TODO Check: Shouldn't the pragmas be printed outside the body?
			printCommentorsAndPragmas(n); // Note that the comments of a CompoundStatement are always printed inside its
											// body.
			if (n.getF1().present()) {
				tabs++;
				for (Node element : n.getF1().getNodes()) {
					Node choice = ((CompoundStatementElement) element).getF0().getChoice();
					if (choice instanceof Statement) {
						this.newLineForStatement((Statement) choice);
					} else {
						this.startNewline();
					}
					element.accept(this);
				}
				tabs--;
			}
			this.startNewline();
			n.getF2().accept(this);
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
			Expression predicate = n.getInfo().getCFGInfo().getPredicate();
			printCommentorsAndPragmas(predicate);
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
			this.newSpaceForStatement(n.getF4());
			n.getF4().accept(this);
			if (n.getF5().present()) {
				NodeSequence nodeSeq = (NodeSequence) n.getF5().getNode();
				if (n.getF4().getStmtF0().getChoice() instanceof CompoundStatement) {
					str.append(" ");
				} else {
					this.startNewline();
				}
				str.append("else");
				this.newSpaceForStatement((Statement) nodeSeq.getNodes().get(1));
				nodeSeq.getNodes().get(1).accept(this);

			}
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
			Expression predicate = n.getInfo().getCFGInfo().getPredicate();
			printCommentorsAndPragmas(predicate);
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
			this.newSpaceForStatement(n.getF4());
			n.getF4().accept(this);
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
			Expression predicate = n.getInfo().getCFGInfo().getPredicate();
			printCommentorsAndPragmas(predicate);
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
			this.newSpaceForStatement(n.getF4());
			n.getF4().accept(this);
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
			Expression predicate = n.getInfo().getCFGInfo().getPredicate();
			printCommentorsAndPragmas(predicate);
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			this.newSpaceForStatement(n.getF1());
			n.getF1().accept(this);
			if (n.getF1().getStmtF0().getChoice() instanceof CompoundStatement) {
				this.newSpaceForStatement(n.getF1()); // Intentional.
				n.getF2().accept(this);
			} else {
				this.startNewline();
				n.getF2().accept(this);
			}
			str.append(" ");
			n.getF3().accept(this);
			n.getF4().accept(this);
			n.getF5().accept(this);
			n.getF6().accept(this);
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
			if (n.getF2().present()) {
				printCommentorsAndPragmas(n.getF2().getNode());
			}
			if (n.getF4().present()) {
				printCommentorsAndPragmas(n.getF4().getNode());
			}
			if (n.getF6().present()) {
				printCommentorsAndPragmas(n.getF6().getNode());
			}
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
			str.append(" ");
			n.getF4().accept(this);
			n.getF5().accept(this);
			str.append(" ");
			n.getF6().accept(this);
			n.getF7().accept(this);
			this.newSpaceForStatement(n.getF8());
			n.getF8().accept(this);
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
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <CONTINUE>
		 * f1 ::= ";"
		 */
		@Override
		public void visit(ContinueStatement n) {
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= <BREAK>
		 * f1 ::= ";"
		 */
		@Override
		public void visit(BreakStatement n) {
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= <RETURN>
		 * f1 ::= ( Expression() )?
		 * f2 ::= ";"
		 */
		@Override
		public void visit(ReturnStatement n) {
			printCommentorsAndPragmas(n);
			printLabels(n);
			n.getF0().accept(this);
			if (n.getF1().present()) {
				str.append(" ");
			}
			n.getF1().accept(this);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public void visit(Expression n) {
			n.getExpF0().accept(this);
			for (Node assignExpNode : n.getExpF1().getNodes()) {
				NodeSequence assignExpSeq = (NodeSequence) assignExpNode;
				str.append(", ");
				assignExpSeq.getNodes().get(1).accept(this);
			}
		}

		/**
		 * f0 ::= NonConditionalExpression()
		 * | ConditionalExpression()
		 */
		@Override
		public void visit(AssignmentExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public void visit(NonConditionalExpression n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
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
			str.append(" ");
			((NodeToken) n.getF0().getChoice()).accept(this);
			str.append(" ");
		}

		/**
		 * f0 ::= LogicalORExpression()
		 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
		 */
		@Override
		public void visit(ConditionalExpression n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				NodeSequence nodeSeq = (NodeSequence) n.getF1().getNode();
				str.append("? ");
				nodeSeq.getNodes().get(1).accept(this);
				str.append(" : ");
				nodeSeq.getNodes().get(3).accept(this);
			}
		}

		/**
		 * f0 ::= ConditionalExpression()
		 */
		@Override
		public void visit(ConstantExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= LogicalANDExpression()
		 * f1 ::= ( "||" LogicalORExpression() )?
		 */
		@Override
		public void visit(LogicalORExpression n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				NodeSequence nodeSeq = (NodeSequence) n.getF1().getNode();
				str.append(" || ");
				nodeSeq.getNodes().get(1).accept(this);
			}
		}

		/**
		 * f0 ::= InclusiveORExpression()
		 * f1 ::= ( "&&" LogicalANDExpression() )?
		 */
		@Override
		public void visit(LogicalANDExpression n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				NodeSequence nodeSeq = (NodeSequence) n.getF1().getNode();
				str.append(" && ");
				nodeSeq.getNodes().get(1).accept(this);
			}
		}

		/**
		 * f0 ::= ExclusiveORExpression()
		 * f1 ::= ( "|" InclusiveORExpression() )?
		 */
		@Override
		public void visit(InclusiveORExpression n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				NodeSequence nodeSeq = (NodeSequence) n.getF1().getNode();
				str.append(" | ");
				nodeSeq.getNodes().get(1).accept(this);
			}
		}

		/**
		 * f0 ::= ANDExpression()
		 * f1 ::= ( "^" ExclusiveORExpression() )?
		 */
		@Override
		public void visit(ExclusiveORExpression n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				NodeSequence nodeSeq = (NodeSequence) n.getF1().getNode();
				str.append(" ^ ");
				nodeSeq.getNodes().get(1).accept(this);
			}
		}

		/**
		 * f0 ::= EqualityExpression()
		 * f1 ::= ( "&" ANDExpression() )?
		 */
		@Override
		public void visit(ANDExpression n) {
			n.getF0().accept(this);
			if (n.getF1().present()) {
				NodeSequence nodeSeq = (NodeSequence) n.getF1().getNode();
				str.append(" & ");
				nodeSeq.getNodes().get(1).accept(this);
			}
		}

		/**
		 * f0 ::= RelationalExpression()
		 * f1 ::= ( EqualOptionalExpression() )?
		 */
		@Override
		public void visit(EqualityExpression n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= EqualExpression()
		 * | NonEqualExpression()
		 */
		@Override
		public void visit(EqualOptionalExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "=="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public void visit(EqualExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "!="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public void visit(NonEqualExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");

			n.getF1().accept(this);
		}

		/**
		 * f0 ::= ShiftExpression()
		 * f1 ::= ( RelationalOptionalExpression() )?
		 */
		@Override
		public void visit(RelationalExpression n) {
			n.getRelExpF0().accept(this);
			n.getRelExpF1().accept(this);
		}

		/**
		 * f0 ::= RelationalLTExpression()
		 * | RelationalGTExpression()
		 * | RelationalLEExpression()
		 * | RelationalGEExpression()
		 */
		@Override
		public void visit(RelationalOptionalExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "<"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public void visit(RelationalLTExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= ">"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public void visit(RelationalGTExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "<="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public void visit(RelationalLEExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= ">="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public void visit(RelationalGEExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= AdditiveExpression()
		 * f1 ::= ( ShiftOptionalExpression() )?
		 */
		@Override
		public void visit(ShiftExpression n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= ShiftLeftExpression()
		 * | ShiftRightExpression()
		 */
		@Override
		public void visit(ShiftOptionalExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= ">>"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public void visit(ShiftLeftExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "<<"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public void visit(ShiftRightExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= MultiplicativeExpression()
		 * f1 ::= ( AdditiveOptionalExpression() )?
		 */
		@Override
		public void visit(AdditiveExpression n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= AdditivePlusExpression()
		 * | AdditiveMinusExpression()
		 */
		@Override
		public void visit(AdditiveOptionalExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "+"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public void visit(AdditivePlusExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "-"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public void visit(AdditiveMinusExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= CastExpression()
		 * f1 ::= ( MultiplicativeOptionalExpression() )?
		 */
		@Override
		public void visit(MultiplicativeExpression n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= MultiplicativeMultiExpression()
		 * | MultiplicativeDivExpression()
		 * | MultiplicativeModExpression()
		 */
		@Override
		public void visit(MultiplicativeOptionalExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "*"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public void visit(MultiplicativeMultiExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "/"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public void visit(MultiplicativeDivExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "%"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public void visit(MultiplicativeModExpression n) {
			str.append(" ");
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= CastExpressionTyped()
		 * | UnaryExpression()
		 */
		@Override
		public void visit(CastExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "("
		 * f1 ::= TypeName()
		 * f2 ::= ")"
		 * f3 ::= CastExpression()
		 */
		@Override
		public void visit(CastExpressionTyped n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			str.append(" ");
			n.getF3().accept(this);
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
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(UnaryExpressionPreIncrement n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(UnaryExpressionPreDecrement n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= UnaryOperator()
		 * f1 ::= CastExpression()
		 */
		@Override
		public void visit(UnaryCastExpression n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public void visit(UnarySizeofExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(SizeofUnaryExpression n) {
			n.getF0().accept(this);
			str.append(" ");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= "("
		 * f2 ::= TypeName()
		 * f3 ::= ")"
		 */
		@Override
		public void visit(SizeofTypeName n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
			n.getF2().accept(this);
			n.getF3().accept(this);
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
			((NodeToken) n.getF0().getChoice()).accept(this);
		}

		/**
		 * f0 ::= PrimaryExpression()
		 * f1 ::= PostfixOperationsList()
		 */
		@Override
		public void visit(PostfixExpression n) {
			n.getF0().accept(this);
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= ( APostfixOperation() )*
		 */
		@Override
		public void visit(PostfixOperationsList n) {
			for (Node node : n.getF0().getNodes()) {
				node.accept(this);
			}
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
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "++"
		 */
		@Override
		public void visit(PlusPlus n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "--"
		 */
		@Override
		public void visit(MinusMinus n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "["
		 * f1 ::= Expression()
		 * f2 ::= "]"
		 */
		@Override
		public void visit(BracketExpression n) {
			str.append("[");
			n.getF1().accept(this);
			str.append("]");
		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( ExpressionList() )?
		 * f2 ::= ")"
		 */
		@Override
		public void visit(ArgumentList n) {
			str.append("(");
			n.getF1().accept(this);
			str.append(")");
		}

		/**
		 * f0 ::= "."
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(DotId n) {
			str.append(".");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "->"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(ArrowId n) {
			str.append("->");
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * | Constant()
		 * | ExpressionClosed()
		 */
		@Override
		public void visit(PrimaryExpression n) {
			if (n.getF0().getChoice() instanceof NodeToken) {
				((NodeToken) n.getF0().getChoice()).accept(this);
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= "("
		 * f1 ::= Expression()
		 * f2 ::= ")"
		 */
		@Override
		public void visit(ExpressionClosed n) {
			str.append("(");
			n.getF1().accept(this);
			str.append(")");
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public void visit(ExpressionList n) {
			n.getF0().accept(this);
			for (Node assignExpNode : n.getF1().getNodes()) {
				NodeSequence assignExpSeq = (NodeSequence) assignExpNode;
				str.append(", ");
				assignExpSeq.getNodes().get(1).accept(this);
			}
		}

		/**
		 * f0 ::= <INTEGER_LITERAL>
		 * | <FLOATING_POINT_LITERAL>
		 * | <CHARACTER_LITERAL>
		 * | ( <STRING_LITERAL> )+
		 */
		@Override
		public void visit(Constant n) {
			if (n.getF0().getChoice() instanceof NodeToken) {
				((NodeToken) n.getF0().getChoice()).accept(this);
			} else {
				List<Node> strList = ((NodeList) n.getF0().getChoice()).getNodes();
				for (Node strNode : strList) {
					((NodeToken) strNode).accept(this);
					if (strNode != strList.get(strList.size() - 1)) {
						str.append(' ');
					}
				}
			}
		}

		@Override
		public void visit(DummyFlushDirective n) {
			printCommentorsAndPragmas(n);
			printLabels(n);
			String writeCells = "";
			String readCells = "";
			if (n.noUpdateGetWrittenBeforeCells() != null) {
				writeCells += n.noUpdateGetWrittenBeforeCells();
			}
			if (n.noUpdateGetReadAfterCells() != null) {
				readCells += n.noUpdateGetReadAfterCells();
			}
			if (Program.printDFDs) {
				if (Program.printRWinDFDs) {
					str.append("// #pragma omp dummyFlush " + n.getDummyFlushType() + " written(" + writeCells
							+ ") read(" + readCells + ")\n");
				} else {
					str.append("// #pragma omp dummyFlush " + n.getDummyFlushType());// + " written(" + writeCells + ")
																						// read("
					// + readCells + ")\n");
				}
			}
		}

		@Override
		public void visit(CallStatement n) {
			printCommentorsAndPragmas(n.getPreCallNode());
			printLabels(n);
			String ret = new String();
			CallStatement node = n;
			if (node.getPostCallNode().hasReturnReceiver()) {
				ret += node.getPostCallNode().getReturnReceiver().getString();
				ret += " = ";
			}
			if (node.getFunctionDesignatorNode() == null) {
				ret += "__context-insensitive();";
			} else {
				ret += node.getFunctionDesignatorNode().getTokenImage() + "(";
				List<SimplePrimaryExpression> argumentList = node.getPreCallNode().getArgumentList();
				for (SimplePrimaryExpression spe : argumentList) {
					if (spe == argumentList.get(argumentList.size() - 1)) {
						break;
					}
					ret += spe.getString() + ", ";
				}
				if (argumentList.size() != 0) {
					ret += argumentList.get(argumentList.size() - 1).getString();
				}
				ret += ");";
			}
			str.append(ret);
			printCommentorsAndPragmas(n.getPostCallNode());
		}

		@Override
		public void visit(SimplePrimaryExpression n) {
			if (n.isAnIdentifier()) {
				n.getIdentifier().accept(this);
			} else {
				n.getConstant().accept(this);
			}
		}

		protected void printLabels(Node n) {
			Node cfgNode = Misc.getInternalFirstCFGNode(n);
			if (cfgNode instanceof Statement) {
				StatementInfo stmtInfo = (StatementInfo) cfgNode.getInfo();
				if (stmtInfo.hasLabelAnnotations()) {
					for (Label l : stmtInfo.getLabelAnnotations()) {
						if (l instanceof SimpleLabel) {
							str.append(((SimpleLabel) l).getLabelName() + ": ");
						} else if (l instanceof DefaultLabel) {
							str.append("default: ");
						} else if (l instanceof CaseLabel) {
							str.append("case " + ((CaseLabel) l).getCaseExpression().getInfo().getString() + ": ");
						} else {
							Misc.exitDueToError("Some problem with the labels of " + cfgNode.getInfo().getString());
						}
					}
				}
			}
		}

		protected void printCommentorsAndPragmas(Node n) {
			if (n == null) {
				return;
			}
			n = Misc.getCFGNodeFor(n);
			/*
			 * Print all imop pragma annotations before the comments.
			 */
			if (this.withPragma) {
				for (PragmaImop annotation : n.getInfo().getPragmaAnnotations()) {
					str.append(annotation.toString());
				}
			}

			/*
			 * Print comments.
			 */
			for (Commentor commentor : this.commentorList) {
				String content = commentor.getString(n);
				if (content.isEmpty()) {
					continue;
				}

				StringBuilder fancy = new StringBuilder();
				fancy.append(System.getProperty("line.separator"));
				for (int tabCount = 0; tabCount < tabs; tabCount++) {
					for (int i = 0; i < 4; i++) {
						fancy.append(' ');
					}
				}
				content = content.replaceAll("\n", fancy.toString());
				content = content.trim();
				content = content.replace("/*", "//");
				content = content.replace("*/", "//");
				this.startNewline();
				str.append("/*");
				str.append(content);
				str.append("*/");
				this.startNewline();
			}
		}
	}
}
