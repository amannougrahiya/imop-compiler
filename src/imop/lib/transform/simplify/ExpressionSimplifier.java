/*
g * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.simplify;

import imop.ast.annotation.Label;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.GJNoArguDepthFirstProcess;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.builder.Builder;
import imop.lib.getter.ExpressionTypeGetter;
import imop.lib.getter.StructUnionOrEnumInfoGetter;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simplifies expressions, by carrying out the following:
 * <ul>
 * <li>Remove all usages of the logical AND (&&) and logical OR(||)
 * operators.</li>
 * <li>Remove all usages of the conditional operator (?:).</li>
 * <li>Remove all usages of the comma operator (,).</li>
 * <li>TODO: Ensure that chained assignments are broken into statements with
 * single assignments.</li>
 * <li>Simplify function calls, to ensure that they are present in either of the
 * following forms:
 * <ul>
 * <li><code>t = foo(t1, t2, ..., tn);</code></li>
 * <li><code>foo(t1, t2, ..., tn);</code></li>
 * </ul>
 * where, <code>t, t1, t2, ... tn</code> are all either identifiers or
 * constants.
 * </li>
 * <li>Those parentheses that directly wrap up a simple-primary expression,
 * i.e., identifiers and constants, are removed.
 * </li>
 * <li>Splits all the ParallelFor constructs into Parallel and For
 * constructs.
 * </li>
 * <li>Splits all the ParallelSection constructs into Parallel and Sections
 * constructs.
 * </li>
 * <li>Splits declarators across multiple declarations (one declarator per
 * declaration).
 * <li>Ensures that typedefs do not contain any implicit definitions of
 * user-defined types.
 * </li>
 * <li>Ensures that user-defined types (structs/unions/enums) are declared
 * and used in different declarations.
 * </li>
 * <li>Replace {@code foo(void)} with {@code foo()} in all FunctionDefinition
 * nodes.</li>
 * </ul>
 * 
 * @author aman
 */
public class ExpressionSimplifier extends GJNoArguDepthFirstProcess<ExpressionSimplifier.SimplificationString> {

	public static final int INIT = 2;

	/**
	 * For creating objects that can be used to specify the simplification
	 * string. These objects are generally sent to a parent from a child,
	 * when the visit on the child returns.
	 * 
	 * @author aman
	 *
	 */
	public static class SimplificationString {
		// public static long counter = 0;

		/**
		 * StringBuilder that represents the modified code that replaces
		 * the children upon simplification.
		 */
		private StringBuilder replacementString;

		/**
		 * StringBuilder of statements that need to be executed immediately
		 * before
		 * the modified expression, in order to preserve the semantics.
		 * <p>
		 * Note that this StringBuilder is a pure concatenation of one or more
		 * statements, which might not be encapsulated into a
		 * single compound statement.
		 */
		private StringBuilder prelude;

		/**
		 * A list of declarations that are needed to be added globally,
		 * e.g., <code>int t1; float t2;</code>
		 */
		private List<Declaration> temporaryDeclarations;

		public SimplificationString() {
			// counter++;
		}

		public StringBuilder getPrelude() {
			if (prelude == null) {
				prelude = new StringBuilder(INIT);
			}
			return prelude;
		}

		public void setPrelude(StringBuilder prelude) {
			this.prelude = prelude;
		}

		public List<Declaration> getTemporaryDeclarations() {
			if (temporaryDeclarations == null) {
				temporaryDeclarations = new ArrayList<>();
			}
			return temporaryDeclarations;
		}

		public void setTemporaryDeclarations(List<Declaration> temporaryDeclarations) {
			this.temporaryDeclarations = temporaryDeclarations;
		}

		public StringBuilder getReplacementString() {
			if (replacementString == null) {
				replacementString = new StringBuilder(INIT);
			}
			return replacementString;
		}

		public void setReplacementString(StringBuilder replacementString) {
			this.replacementString = replacementString;
		}

		public boolean hasNoTempDeclarations() {
			if (temporaryDeclarations == null) {
				return true;
			}
			return temporaryDeclarations.isEmpty();
		}

		public int hasPrelude() {
			if (prelude == null) {
				return 0;
			}
			return prelude.length();
		}

	}

	Set<CompoundStatement> originalCS = new HashSet<>();

	public ExpressionSimplifier(Set<CompoundStatement> originalCS) {
		this.originalCS = originalCS;
	}

	/**
	 * Given a {@code SimplificationString}, and expression being processed,
	 * this method replaces the replacement string with a new temporary, if
	 * required.
	 * <br>
	 * Note that this method has not yet been used at all places within the
	 * visitor where it should be used.
	 * 
	 * @param ret
	 *               a {@code SimplificationString} from which the call-site, if
	 *               any, needs to be extracted out.
	 * @param retExp
	 *               expression being processed.
	 */
	private void extractFunctionCall(SimplificationString ret, Expression retExp) {
		if (!Misc.isACall(ret.getReplacementString())) {
			return;
		}
		String tempName = Builder.getNewTempName();
		Declaration decl = Type.getType(retExp).getDeclaration(tempName);
		ret.getTemporaryDeclarations().add(decl);
		ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
		ret.setReplacementString(new StringBuilder(" " + tempName + " "));
	}

	@Override
	public SimplificationString visit(NodeList n) {
		SimplificationString ret = new SimplificationString();
		for (Node node : n.getNodes()) {
			SimplificationString elementSS = node.accept(this);
			ret.getPrelude().append(elementSS.getPrelude());
			ret.getTemporaryDeclarations().addAll(elementSS.getTemporaryDeclarations());
			ret.getReplacementString().append(" " + elementSS.getReplacementString() + " ");
		}
		return ret;
	}

	@Override
	public SimplificationString visit(NodeListOptional n) {
		SimplificationString ret = new SimplificationString();
		if (n.present()) {
			for (Node node : n.getNodes()) {
				SimplificationString elementSS = node.accept(this);
				ret.getPrelude().append(elementSS.getPrelude());
				ret.getTemporaryDeclarations().addAll(elementSS.getTemporaryDeclarations());
				ret.getReplacementString().append(" " + elementSS.getReplacementString() + " ");
			}
		}
		return ret;
	}

	@Override
	public SimplificationString visit(NodeOptional n) {
		SimplificationString ret = new SimplificationString();
		if (n.present()) {
			ret = n.getNode().accept(this);
		}
		return ret;
	}

	@Override
	public SimplificationString visit(NodeToken n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(n.getTokenImage()));
		return ret;
	}

	@Override
	public SimplificationString visit(NodeSequence n) {
		SimplificationString ret = new SimplificationString();
		if (n.getNodes().isEmpty()) {
			return ret;
		}
		for (Node node : n.getNodes()) {
			SimplificationString tempSS = node.accept(this);
			if (tempSS == null) {
				System.err.println(node.getClass().getSimpleName());
				System.exit(0);
			}
			ret.getPrelude().append(tempSS.getPrelude());
			ret.getTemporaryDeclarations().addAll(tempSS.getTemporaryDeclarations());
			ret.getReplacementString().append(" " + tempSS.getReplacementString());
		}
		return ret;
	}

	/**
	 * f0 ::= ( ElementsOfTranslation() )+
	 */
	@Override
	public SimplificationString visit(TranslationUnit n) {
		SimplificationString ret = new SimplificationString();
		for (Node element : n.getF0().getNodes()) {
			SimplificationString elementSS = element.accept(this);
			for (Declaration tempDecl : elementSS.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
			}
			ret.getReplacementString().append(elementSS.getPrelude());
			ret.getReplacementString().append(" " + elementSS.getReplacementString() + " ");
		}
		return ret;
	}

	/**
	 * f0 ::= ExternalDeclaration()
	 * | UnknownCpp()
	 * | UnknownPragma()
	 */
	@Override
	public SimplificationString visit(ElementsOfTranslation n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= Declaration()
	 * | FunctionDefinition()
	 * | DeclareReductionDirective()
	 * | ThreadPrivateDirective()
	 */
	@Override
	public SimplificationString visit(ExternalDeclaration n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public SimplificationString visit(FunctionDefinition n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(n.getF0().getInfo().getString() + " "));
		ret.getReplacementString().append(this.getVoidFixedString(n.getF1()));
		ret.getReplacementString().append(n.getF2().getInfo().getString() + " ");
		SimplificationString compSS = n.getF3().accept(this);
		assert (compSS.hasNoTempDeclarations());
		assert (compSS.hasPrelude() == 0);
		ret.getReplacementString().append(compSS.getReplacementString());
		return ret;
	}

	/**
	 * Return the string-builder for this Declarator, while taking care of the
	 * special case "x(void)", which gets converted to "x()". This function
	 * should be updated later to handle the generic cases.
	 * 
	 * @param decl
	 * @return
	 */
	private String getVoidFixedString(Declarator decl) {
		String retStr = decl + " ";
		if (Misc.getInheritedEnclosee(decl, Declarator.class).size() != 1) {
			return retStr;
		}
		List<Node> decOpList = decl.getF1().getF1().getF0().getNodes();
		if (decOpList.size() != 1
				|| !(((ADeclaratorOp) decOpList.get(0)).getF0().getChoice() instanceof ParameterTypeListClosed)) {
			return retStr;
		}
		if (decOpList.get(0).toString().trim().equals("(void )")) {
			retStr = (decl.getF0().present() ? (decl.getF0() + " ") : "") + decl.getF1().getF0() + " ()" + " ";
		}
		return retStr;
	}

	/**
	 * Simplify a declaration, by ensuring that the initializer, if any, is
	 * simplified,
	 * and that each declarator is defined in its own line.
	 * <p>
	 * f0 ::= DeclarationSpecifiers()
	 * f1 ::= ( InitDeclaratorList() )?
	 * f2 ::= ";"
	 */
	@Override
	public SimplificationString visit(Declaration n) {
		SimplificationString ret = new SimplificationString();

		if (!n.getF1().present()) {
			ret.setReplacementString(new StringBuilder(n.getF0() + ";"));
			return ret;
		}
		/*
		 * Ensure that if this declaration defines a struct, union, or
		 * enum, then all declarations of objects of the declared type, are
		 * split away as different Declarations.
		 */
		StringBuilder declarationSpecifierString = new StringBuilder(n.getF0() + " ");
		StructUnionOrEnumInfoGetter aggregateInfoGetter = new StructUnionOrEnumInfoGetter();
		n.accept(aggregateInfoGetter);
		if (aggregateInfoGetter.isUserDefinedDefinitionOrAssociatedTypedef) {
			StringBuilder declarationSpecifierStringSimple = new StringBuilder(aggregateInfoGetter.simpleDeclaration);
			if (n.getInfo().isTypedef()) {
				StringBuilder str = new StringBuilder(INIT);
				for (Node aDeclSpecNode : n.getF0().getF0().getNodes()) {
					ADeclarationSpecifier aDeclSpec = (ADeclarationSpecifier) aDeclSpecNode;
					if (aDeclSpec.getF0().getChoice() instanceof StorageClassSpecifier) {
						StorageClassSpecifier stoClaSpec = (StorageClassSpecifier) aDeclSpec.getF0().getChoice();
						if (stoClaSpec.getF0().getChoice() instanceof NodeToken) {
							NodeToken nodeToken = (NodeToken) stoClaSpec.getF0().getChoice();
							if (nodeToken.getTokenImage().equals("typedef")) {
								// do nothing.
							} else {
								str.append(aDeclSpec + " ");
							}
						} else {
							assert (false);
						}
					} else {
						str.append(aDeclSpec + " ");
					}
				}
				ret.setPrelude(str.append(";"));
			} else {
				ret.setPrelude(new StringBuilder(declarationSpecifierString + ";"));
			}

			InitDeclaratorList initDeclList = (InitDeclaratorList) n.getF1().getNode();
			SimplificationString declSS = initDeclList.getF0().accept(this);
			ret.getTemporaryDeclarations().addAll(declSS.getTemporaryDeclarations());
			ret.getPrelude().append(declSS.getPrelude());
			if (!initDeclList.getF1().present()) {
				ret.setReplacementString(new StringBuilder(
						declarationSpecifierStringSimple + " " + declSS.getReplacementString() + ";"));
				return ret;
			} else {
				/*
				 * In <code> type a = i1, b = i2, c = i3;</code>, the
				 * replacement StringBuilder should be
				 * <code> type c= i3; </code> instead of <code> type a = i1;
				 * </code>.
				 * The latter should be the first definition in the prelude
				 * (preceded by its own prelude, if any).
				 */
				ret.getPrelude().append(declarationSpecifierStringSimple + " " + declSS.getReplacementString() + ";");

				Node lastElement = null;
				List<Node> allButFirst = initDeclList.getF1().getNodes();
				for (Node declNode : allButFirst) {
					if (declNode == allButFirst.get(allButFirst.size() - 1)) {
						declNode = ((NodeSequence) declNode).getNodes().get(1);
						lastElement = declNode;
						break;
					}
					declNode = ((NodeSequence) declNode).getNodes().get(1);
					declSS = declNode.accept(this);
					ret.getTemporaryDeclarations().addAll(declSS.getTemporaryDeclarations());
					ret.getPrelude().append(declSS.getPrelude());
					ret.getPrelude()
							.append(declarationSpecifierStringSimple + " " + declSS.getReplacementString() + ";");
				}
				declSS = lastElement.accept(this);
				ret.getTemporaryDeclarations().addAll(declSS.getTemporaryDeclarations());
				ret.getPrelude().append(declSS.getPrelude());
				ret.setReplacementString(new StringBuilder(
						declarationSpecifierStringSimple + " " + declSS.getReplacementString() + ";"));
				return ret;
			}
		} else {
			InitDeclaratorList initDeclList = (InitDeclaratorList) n.getF1().getNode();
			SimplificationString declSS = initDeclList.getF0().accept(this);
			ret.getTemporaryDeclarations().addAll(declSS.getTemporaryDeclarations());
			ret.setPrelude(declSS.getPrelude());
			if (!initDeclList.getF1().present()) {
				ret.getReplacementString()
						.append(declarationSpecifierString + " " + declSS.getReplacementString() + ";");
			} else {
				/*
				 * In <code> type a = i1, b = i2, c = i3;</code>, the
				 * replacement StringBuilder should be
				 * <code> type c= i3; </code> instead of <code> type a = i1;
				 * </code>.
				 * The latter should be the first definition in the prelude
				 * (preceded by its own prelude, if any).
				 */
				ret.getPrelude().append(declarationSpecifierString + " " + declSS.getReplacementString() + ";");

				Node lastElement = null;
				List<Node> allButFirst = initDeclList.getF1().getNodes();
				for (Node declNode : allButFirst) {
					if (declNode == allButFirst.get(allButFirst.size() - 1)) {
						declNode = ((NodeSequence) declNode).getNodes().get(1);
						lastElement = declNode;
						break;
					}
					declNode = ((NodeSequence) declNode).getNodes().get(1);
					declSS = declNode.accept(this);
					ret.getTemporaryDeclarations().addAll(declSS.getTemporaryDeclarations());
					ret.getPrelude().append(declSS.getPrelude());
					ret.getPrelude().append(declarationSpecifierString + " " + declSS.getReplacementString() + ";");
				}
				declSS = lastElement.accept(this);
				ret.getTemporaryDeclarations().addAll(declSS.getTemporaryDeclarations());
				ret.getPrelude().append(declSS.getPrelude());
				ret.setReplacementString(
						new StringBuilder(declarationSpecifierString + " " + declSS.getReplacementString() + ";"));
			}
		}

		// System.out.println("TT: " + ret.temporaryDeclarations);
		// System.out.println("PR: " + ret.prelude);
		// System.out.println("RR: " + ret.replacementString);
		// System.out.println("===");
		//
		return ret;
	}

	/**
	 * f0 ::= ( Declaration() )+
	 */
	@Override
	public SimplificationString visit(DeclarationList n) {
		SimplificationString ret = new SimplificationString();
		for (Node declNode : n.getF0().getNodes()) {
			SimplificationString elementSS = declNode.accept(this);
			for (Declaration tempDecl : elementSS.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
			}
			ret.getReplacementString().append(elementSS.getPrelude());
			ret.getReplacementString().append(elementSS.getReplacementString());
		}
		return ret;
	}

	/**
	 * f0 ::= ( ADeclarationSpecifier() )+
	 */
	@Override
	public SimplificationString visit(DeclarationSpecifiers n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= StorageClassSpecifier()
	 * | TypeSpecifier()
	 * | TypeQualifier()
	 */
	@Override
	public SimplificationString visit(ADeclarationSpecifier n) {
		SimplificationString ret = n.getF0().accept(this);
		ret.getReplacementString().append(" ");
		// ret.replacementString = new StringBuilder(n.getInfo().getString() + " ");
		return ret;
	}

	/**
	 * f0 ::= Declarator()
	 * f1 ::= ( "=" Initializer() )?
	 */
	@Override
	public SimplificationString visit(InitDeclarator n) {
		SimplificationString ret = new SimplificationString();
		if (n.getF1().present()) {
			Initializer initNode = (Initializer) ((NodeSequence) n.getF1().getNode()).getNodes().get(1);
			ret = initNode.accept(this);
			this.extractFunctionCall(ret, initNode);
			// OLD CODE:
			// if (Misc.isACall(ret.getReplacementString())) {
			// String tempName = Builder.getNewTempName();
			// Declaration decl = Type.getType(initNode).getDeclaration(tempName);
			// ret.getTemporaryDeclarations().add(decl);
			// ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			// ret.setReplacementString(new StringBuilder(" " + tempName + " "));
			// }
			ret.setReplacementString(
					new StringBuilder(n.getF0().getInfo().getString()).append(" = " + ret.getReplacementString()));
		} else {
			ret.setReplacementString(new StringBuilder(n.getF0().getInfo().getString() + " "));
		}
		return ret;
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * | ArrayInitializer()
	 */
	@Override
	public SimplificationString visit(Initializer n) {
		if (n.getF0().getChoice() instanceof AssignmentExpression) {
			SimplificationString ret = n.getF0().accept(this);
			if (Misc.isACall(ret.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType((AssignmentExpression) n.getF0().getChoice()).getDeclaration(tempName);
				ret.getTemporaryDeclarations().add(decl);
				ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
				ret.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
			return ret;
		}
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= InitializerList()
	 * f2 ::= ( "," )?
	 * f3 ::= "}"
	 */
	@Override
	public SimplificationString visit(ArrayInitializer n) {
		SimplificationString ret = n.getF1().accept(this);
		ret.setReplacementString(
				new StringBuilder("{" + ret.getReplacementString() + n.getF2().getInfo().getString() + " }"));
		return ret;
	}

	/**
	 * f0 ::= Initializer()
	 * f1 ::= ( "," Initializer() )*
	 */
	@Override
	public SimplificationString visit(InitializerList n) {
		SimplificationString ret = n.getF0().accept(this);
		for (Node initNodeSeq : n.getF1().getNodes()) {
			Node init = ((NodeSequence) initNodeSeq).getNodes().get(1);
			SimplificationString initSS = init.accept(this);
			ret.getTemporaryDeclarations().addAll(initSS.getTemporaryDeclarations());
			ret.getPrelude().append(initSS.getPrelude());
			ret.getReplacementString().append(", " + initSS.getReplacementString());
		}
		return ret;
	}

	/**
	 * f0 ::= SpecifierQualifierList()
	 * f1 ::= ( AbstractDeclarator() )?
	 */
	@Override
	public SimplificationString visit(TypeName n) {
		SimplificationString ret0 = n.getF0().accept(this);
		SimplificationString ret1 = n.getF1().accept(this);
		if (ret1.getReplacementString().toString().isEmpty()) {
			ret0.getReplacementString().append(" ");
		} else {
			ret0.getReplacementString().append(" " + ret1.getReplacementString().toString() + " ");
		}
		return ret0;
	}

	/**
	 * f0 ::= AbstractDeclaratorWithPointer()
	 * | DirectAbstractDeclarator()
	 */
	@Override
	public SimplificationString visit(AbstractDeclarator n) {
		SimplificationString ret = n.getF0().accept(this);
		ret.getReplacementString().append(" ");
		return ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 */
	@Override
	public SimplificationString visit(TypedefName n) {
		SimplificationString ret = new SimplificationString();
		ret.getReplacementString().append(" " + n.getF0().getTokenImage() + " ");
		return ret;
	}

	/**
	 * f0 ::= ( LabeledStatement() | ExpressionStatement() |
	 * CompoundStatement()
	 * | SelectionStatement() | IterationStatement() | JumpStatement() |
	 * UnknownPragma() | OmpConstruct() | OmpDirective() | UnknownCpp() )
	 */
	@Override
	public SimplificationString visit(Statement n) {
		SimplificationString ret = n.getStmtF0().accept(this);
		assert (ret.hasNoTempDeclarations());
		assert (ret.hasPrelude() == 0);
		return ret;
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <UNKNOWN_CPP>
	 */
	@Override
	public SimplificationString visit(UnknownCpp n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(INIT));
		List<Label> labels = n.getInfo().getLabelAnnotations();
		for (Label label : labels) {
			ret.getReplacementString().append(label.getString());
		}
		ret.getReplacementString().append("\n#" + n.getF1() + " \n");
		return ret;
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
	public SimplificationString visit(OmpConstruct n) {
		return n.getOmpConsF0().accept(this);
	}

	/**
	 * f0 ::= BarrierDirective()
	 * | TaskwaitDirective()
	 * | TaskyieldDirective()
	 * | FlushDirective()
	 */
	@Override
	public SimplificationString visit(OmpDirective n) {
		return n.getOmpDirF0().accept(this);
	}

	/**
	 * f0 ::= "#"
	 * f1 ::= <PRAGMA>
	 * f2 ::= <UNKNOWN_CPP>
	 */
	@Override
	public SimplificationString visit(UnknownPragma n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(INIT));
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		ret.getReplacementString().append("\n#pragma " + n.getF2() + "\n");
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public SimplificationString visit(ParallelConstruct n) {
		SimplificationString ret = new SimplificationString();
		SimplificationString stmtSS = n.getParConsF2().accept(this);
		assert (stmtSS.hasNoTempDeclarations());
		assert (stmtSS.hasPrelude() == 0);

		SimplificationString parDirSS = n.getParConsF1().accept(this);
		if (parDirSS.hasPrelude() == 0) {
			for (Label label : n.getInfo().getLabelAnnotations()) {
				ret.getReplacementString().append(label.getString());
			}
			ret.getReplacementString().append("\n#pragma omp ");
			ret.getReplacementString().append(parDirSS.getReplacementString());
			ret.getReplacementString().append(stmtSS.getReplacementString());
		} else {
			boolean needsEncapsulation = this.needsEncapsulation(n);
			if (needsEncapsulation) {
				ret.getReplacementString().append("{");
			}
			for (Declaration tempDecl : parDirSS.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
			}
			for (Label label : n.getInfo().getLabelAnnotations()) {
				ret.getReplacementString().append(label.getString());
			}
			ret.getReplacementString().append(parDirSS.getPrelude());
			ret.getReplacementString().append("\n#pragma omp ");
			ret.getReplacementString().append(parDirSS.getReplacementString());
			ret.getReplacementString().append(stmtSS.getReplacementString());
			if (needsEncapsulation) {
				ret.getReplacementString().append("}");
			}
		}
		return ret;
	}

	/**
	 * f0 ::= <PARALLEL>
	 * f1 ::= UniqueParallelOrDataClauseList()
	 * f2 ::= OmpEol()
	 */
	@Override
	public SimplificationString visit(ParallelDirective n) {
		SimplificationString ret = n.getF1().accept(this);
		ret.setReplacementString(
				new StringBuilder(" parallel " + ret.getReplacementString() + n.getF2().getInfo().getString() + " "));
		return ret;
	}

	/**
	 * f0 ::= ( AUniqueParallelOrDataClause() )*
	 */
	@Override
	public SimplificationString visit(UniqueParallelOrDataClauseList n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= UniqueParallelClause()
	 * | DataClause()
	 */
	@Override
	public SimplificationString visit(AUniqueParallelOrDataClause n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= IfClause()
	 * | NumThreadsClause()
	 */
	@Override
	public SimplificationString visit(UniqueParallelClause n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= <IF>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public SimplificationString visit(IfClause n) {
		SimplificationString ret = n.getF2().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder(" if (" + ret.getReplacementString() + ") "));
		return ret;
	}

	/**
	 * f0 ::= <NUM_THREADS>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public SimplificationString visit(NumThreadsClause n) {
		SimplificationString ret = n.getF2().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder(" num_threads (" + ret.getReplacementString() + ") "));
		return ret;
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
	public SimplificationString visit(DataClause n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(n.getF0().toString()));
		ret.getReplacementString().append(" ");
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ForDirective()
	 * f2 ::= OmpForHeader()
	 * f3 ::= Statement()
	 */
	@Override
	public SimplificationString visit(ForConstruct n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		SimplificationString forDirSS = n.getF1().accept(this);
		SimplificationString initExpSS = n.getF2().getF2().accept(this);
		SimplificationString forCondSS = n.getF2().getF4().accept(this);
		SimplificationString reinitExpSS = n.getF2().getF6().accept(this);
		SimplificationString stmtSS = n.getF3().accept(this);

		if (forCondSS.hasPrelude() == 0) {
			StringBuilder preString = new StringBuilder(INIT);
			for (Declaration tempDecl : forDirSS.getTemporaryDeclarations()) {
				preString.append(tempDecl.getInfo().getString() + " ");
			}
			for (Declaration tempDecl : initExpSS.getTemporaryDeclarations()) {
				preString.append(tempDecl.getInfo().getString() + " ");
			}
			preString.append(forDirSS.getPrelude());
			preString.append(initExpSS.getPrelude());
			if (preString.length() != 0) {
				boolean needsEncapsulation = this.needsEncapsulation(n);
				if (needsEncapsulation) {
					ret.getReplacementString().append("{");
				}
				ret.getReplacementString().append(preString);
			}
			ret.getReplacementString().append("\n#pragma omp ");
			ret.getReplacementString().append(forDirSS.getReplacementString() + " ");
			ret.getReplacementString().append(" for (" + initExpSS.getReplacementString() + "; "
					+ forCondSS.getReplacementString() + ";" + reinitExpSS.getReplacementString() + ")");
			if (reinitExpSS.hasPrelude() == 0) {
				ret.getReplacementString().append(stmtSS.getReplacementString());
			} else {
				ret.getReplacementString().append("{");
				for (Declaration tempDecl : reinitExpSS.getTemporaryDeclarations()) {
					ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
				}
				ret.getReplacementString().append(stmtSS.getReplacementString());
				ret.getReplacementString().append(reinitExpSS.getPrelude());
				ret.getReplacementString().append("}");

			}
			if (preString.length() != 0) {
				boolean needsEncapsulation = this.needsEncapsulation(n);
				if (needsEncapsulation) {
					ret.getReplacementString().append("}");
				}
			}
		} else {
			StringBuilder preString = new StringBuilder(INIT);
			for (Declaration tempDecl : forDirSS.getTemporaryDeclarations()) {
				preString.append(tempDecl.getInfo().getString() + " ");
			}
			for (Declaration tempDecl : initExpSS.getTemporaryDeclarations()) {
				preString.append(tempDecl.getInfo().getString() + " ");
			}
			for (Declaration tempDecl : forCondSS.getTemporaryDeclarations()) {
				preString.append(tempDecl.getInfo().getString() + " ");
			}
			preString.append(forDirSS.getPrelude());
			preString.append(initExpSS.getPrelude());
			preString.append(initExpSS.getReplacementString());
			preString.append(forCondSS.getPrelude());
			if (preString.length() != 0) {
				boolean needsEncapsulation = this.needsEncapsulation(n);
				if (needsEncapsulation) {
					ret.getReplacementString().append("{");
				}
				ret.getReplacementString().append(preString);
			}

			ret.getReplacementString().append("\n#pragma omp ");
			ret.getReplacementString().append(forDirSS.getReplacementString() + " ");
			ret.getReplacementString().append(" for ( ;" + forCondSS.getReplacementString() + ";)");
			ret.getReplacementString().append("{");
			for (Declaration tempDecl : reinitExpSS.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
			}
			ret.getReplacementString().append(" " + stmtSS.getReplacementString());
			ret.getReplacementString().append(" " + reinitExpSS.getPrelude());
			ret.getReplacementString().append(" " + reinitExpSS.getReplacementString());
			ret.getReplacementString().append(" " + forCondSS.getPrelude());
			ret.getReplacementString().append("}");
			if (preString.length() != 0) {
				boolean needsEncapsulation = this.needsEncapsulation(n);
				if (needsEncapsulation) {
					ret.getReplacementString().append("}");
				}
			}
		}
		return ret;
	}

	/**
	 * f0 ::= <FOR>
	 * f1 ::= UniqueForOrDataOrNowaitClauseList()
	 * f2 ::= OmpEol()
	 */
	@Override
	public SimplificationString visit(ForDirective n) {
		SimplificationString ret = n.getF1().accept(this);
		ret.setReplacementString(
				new StringBuilder(" for " + ret.getReplacementString() + " " + n.getF2().getInfo().getString() + " "));
		return ret;
	}

	/**
	 * f0 ::= ( AUniqueForOrDataOrNowaitClause() )*
	 */
	@Override
	public SimplificationString visit(UniqueForOrDataOrNowaitClauseList n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= UniqueForClause()
	 * | DataClause()
	 * | NowaitClause()
	 */
	@Override
	public SimplificationString visit(AUniqueForOrDataOrNowaitClause n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= <NOWAIT>
	 */
	@Override
	public SimplificationString visit(NowaitClause n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= <ORDERED>
	 * | UniqueForClauseSchedule()
	 * | UniqueForCollapse()
	 */
	@Override
	public SimplificationString visit(UniqueForClause n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= <COLLAPSE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public SimplificationString visit(UniqueForCollapse n) {
		SimplificationString ret = n.getF2().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder(" collapse(" + ret.getReplacementString() + ")"));
		return ret;
	}

	/**
	 * f0 ::= <SCHEDULE>
	 * f1 ::= "("
	 * f2 ::= ScheduleKind()
	 * f3 ::= ( "," Expression() )?
	 * f4 ::= ")"
	 */
	@Override
	public SimplificationString visit(UniqueForClauseSchedule n) {
		SimplificationString ret = new SimplificationString();
		if (n.getF3().present()) {
			Expression exp = (Expression) ((NodeSequence) n.getF3().getNode()).getNodes().get(1);
			ret = exp.accept(this);
			if (Misc.isACall(ret.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType(exp).getDeclaration(tempName);
				ret.getTemporaryDeclarations().add(decl);
				ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
				ret.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
			ret.setReplacementString(new StringBuilder(
					" schedule (" + n.getF2().getInfo().getString() + " , " + ret.getReplacementString() + ")"));
		} else {
			ret.setReplacementString(new StringBuilder(" schedule (" + n.getF2().getInfo().getString() + ")"));
		}
		return ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= Expression()
	 */
	@Override
	public SimplificationString visit(OmpForInitExpression n) {
		SimplificationString ret = n.getF2().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(
				new StringBuilder(n.getF0().getInfo().getString() + " = " + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= OmpForLTCondition() | OmpForLECondition() | OmpForGTCondition() |
	 * OmpForGECondition()
	 */
	@Override
	public SimplificationString visit(OmpForCondition n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<"
	 * f2 ::= Expression()
	 */
	@Override
	public SimplificationString visit(OmpForLTCondition n) {
		SimplificationString ret = n.getF2().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(
				new StringBuilder(n.getF0().getInfo().getString() + " < " + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<="
	 * f2 ::= Expression()
	 */
	@Override
	public SimplificationString visit(OmpForLECondition n) {
		SimplificationString ret = n.getF2().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(
				new StringBuilder(n.getF0().getInfo().getString() + " <= " + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">"
	 * f2 ::= Expression()
	 */
	@Override
	public SimplificationString visit(OmpForGTCondition n) {
		SimplificationString ret = n.getF2().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(
				new StringBuilder(n.getF0().getInfo().getString() + " > " + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">="
	 * f2 ::= Expression()
	 */
	@Override
	public SimplificationString visit(OmpForGECondition n) {
		SimplificationString ret = n.getF2().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(
				new StringBuilder(n.getF0().getInfo().getString() + " >= " + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= PostIncrementId() | PostDecrementId() | PreIncrementId() |
	 * PreDecrementId() | ShortAssignPlus() | ShortAssignMinus() |
	 * OmpForAdditive() | OmpForSubtractive() | OmpForMultiplicative()
	 */
	@Override
	public SimplificationString visit(OmpForReinitExpression n) {
		return n.getOmpForReinitF0().accept(this);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "++"
	 */
	@Override
	public SimplificationString visit(PostIncrementId n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(n.getF0().getTokenImage() + "++ "));
		return ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "--"
	 */
	@Override
	public SimplificationString visit(PostDecrementId n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(n.getF0().getTokenImage() + "-- "));
		return ret;
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public SimplificationString visit(PreIncrementId n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder("++" + n.getF1().getTokenImage() + " "));
		return ret;
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public SimplificationString visit(PreDecrementId n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder("--" + n.getF1().getTokenImage() + " "));
		return ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "+="
	 * f2 ::= Expression()
	 */
	@Override
	public SimplificationString visit(ShortAssignPlus n) {
		SimplificationString ret = n.getF2().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(
				new StringBuilder(n.getF0().getInfo().getString() + " += " + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "-="
	 * f2 ::= Expression()
	 */
	@Override
	public SimplificationString visit(ShortAssignMinus n) {
		SimplificationString ret = n.getF2().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(
				new StringBuilder(n.getF0().getInfo().getString() + " -= " + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= <IDENTIFIER>
	 * f3 ::= "+"
	 * f4 ::= AdditiveExpression()
	 */
	@Override
	public SimplificationString visit(OmpForAdditive n) {
		SimplificationString ret = n.getF4().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF4()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder(n.getF0().getInfo().getString() + " = "
				+ n.getF2().getInfo().getString() + " + " + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= <IDENTIFIER>
	 * f3 ::= "-"
	 * f4 ::=
	 * AdditiveExpression()
	 */
	@Override
	public SimplificationString visit(OmpForSubtractive n) {
		SimplificationString ret = n.getF4().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF4()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder(n.getF0().getInfo().getString() + " = "
				+ n.getF2().getInfo().getString() + " - " + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "="
	 * f2 ::= MultiplicativeExpression()
	 * f3 ::= "+"
	 * f4 ::= <IDENTIFIER>
	 */
	@Override
	public SimplificationString visit(OmpForMultiplicative n) {
		SimplificationString ret = n.getF2().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder(n.getF0().getInfo().getString() + " = " + ret.getReplacementString()
				+ " + " + n.getF4().getInfo().getString()));
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTIONS>
	 * f2 ::= NowaitDataClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= SectionsScope()
	 */
	@Override
	public SimplificationString visit(SectionsConstruct n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		SimplificationString clauseSS = n.getF2().accept(this);
		SimplificationString sectionsSS = n.getF4().accept(this);
		assert (sectionsSS.hasNoTempDeclarations());
		assert (sectionsSS.hasPrelude() == 0);

		if (clauseSS.hasPrelude() == 0) {
			ret.getReplacementString().append("\n#pragma omp sections ");
			ret.getReplacementString()
					.append(clauseSS.getReplacementString() + " " + n.getF3().getInfo().getString() + " ");
			ret.getReplacementString().append(sectionsSS.getReplacementString());
		} else {
			boolean needsEncapsulation = this.needsEncapsulation(n);
			if (needsEncapsulation) {
				ret.getReplacementString().append("{");
			}
			for (Declaration tempDecl : clauseSS.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
			}
			ret.getReplacementString().append(clauseSS.getPrelude() + "\n");
			ret.getReplacementString().append("\n#pragma omp sections ");
			ret.getReplacementString()
					.append(clauseSS.getReplacementString() + " " + n.getF3().getInfo().getString() + " ");
			ret.getReplacementString().append(sectionsSS.getReplacementString());
			if (needsEncapsulation) {
				ret.getReplacementString().append("}");
			}
		}
		return ret;
	}

	/**
	 * f0 ::= ( ANowaitDataClause() )*
	 */
	@Override
	public SimplificationString visit(NowaitDataClauseList n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= NowaitClause()
	 * | DataClause()
	 */
	@Override
	public SimplificationString visit(ANowaitDataClause n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( Statement() )?
	 * f2 ::= ( ASection() )*
	 * f3 ::= "}"
	 */
	@Override
	public SimplificationString visit(SectionsScope n) {
		SimplificationString ret = n.getF1().accept(this);
		SimplificationString sectionSS = n.getF2().accept(this);
		ret.setReplacementString(
				new StringBuilder("{" + ret.getReplacementString() + sectionSS.getReplacementString() + "}"));
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SECTION>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public SimplificationString visit(ASection n) {
		SimplificationString ret = n.getF3().accept(this);
		ret.setReplacementString(new StringBuilder(n.getF0().getInfo().getString() + " section "
				+ n.getF2().getInfo().getString() + " " + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <SINGLE>
	 * f2 ::= SingleClauseList()
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public SimplificationString visit(SingleConstruct n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		SimplificationString clauseSS = n.getF2().accept(this);
		SimplificationString stmtSS = n.getF4().accept(this);
		if (clauseSS.hasPrelude() == 0) {
			ret.getReplacementString().append("\n#pragma omp single " + clauseSS.getReplacementString()
					+ n.getF3().getInfo().getString() + " " + stmtSS.getReplacementString());
		} else {
			boolean needsEncapsulation = this.needsEncapsulation(n);
			if (needsEncapsulation) {
				ret.getReplacementString().append("{");
			}
			for (Declaration tempDecl : clauseSS.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
			}
			ret.getReplacementString().append(clauseSS.getPrelude());
			ret.getReplacementString().append("\n#pragma omp single " + clauseSS.getReplacementString()
					+ n.getF3().getInfo().getString() + " " + stmtSS.getReplacementString());
			if (needsEncapsulation) {
				ret.getReplacementString().append("}");
			}
		}
		return ret;
	}

	/**
	 * f0 ::= ( ASingleClause() )*
	 */
	@Override
	public SimplificationString visit(SingleClauseList n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= NowaitClause()
	 * | DataClause()
	 * | OmpCopyPrivateClause()
	 */
	@Override
	public SimplificationString visit(ASingleClause n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= <COPYPRIVATE>
	 * f1 ::= "("
	 * f2 ::= VariableList()
	 * f3 ::= ")"
	 */
	@Override
	public SimplificationString visit(OmpCopyPrivateClause n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder("copyprivate(" + n.getF2() + ") "));
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASK>
	 * f2 ::= ( TaskClause() )*
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public SimplificationString visit(TaskConstruct n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		SimplificationString clauseSS = n.getF2().accept(this);
		SimplificationString stmtSS = n.getF4().accept(this);
		if (clauseSS.hasPrelude() == 0) {
			ret.getReplacementString().append("\n#pragma omp task " + clauseSS.getReplacementString()
					+ n.getF3().getInfo().getString() + " " + stmtSS.getReplacementString());
		} else {
			boolean needsEncapsulation = this.needsEncapsulation(n);
			if (needsEncapsulation) {
				ret.getReplacementString().append("{");
			}
			for (Declaration tempDecl : clauseSS.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
			}
			ret.getReplacementString().append(clauseSS.getPrelude() + "\n");
			ret.getReplacementString().append("\n#pragma omp task " + clauseSS.getReplacementString()
					+ n.getF3().getInfo().getString() + " " + stmtSS.getReplacementString());
			if (needsEncapsulation) {
				ret.getReplacementString().append("}");
			}
		}
		return ret;
	}

	/**
	 * f0 ::= DataClause()
	 * | UniqueTaskClause()
	 */
	@Override
	public SimplificationString visit(TaskClause n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= IfClause()
	 * | FinalClause()
	 * | UntiedClause()
	 * | MergeableClause()
	 */
	@Override
	public SimplificationString visit(UniqueTaskClause n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= <FINAL>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 */
	@Override
	public SimplificationString visit(FinalClause n) {
		SimplificationString ret = n.getF2().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder(" final(" + ret.getReplacementString() + ")"));
		return ret;

	}

	/**
	 * f0 ::= <UNTIED>
	 */
	@Override
	public SimplificationString visit(UntiedClause n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= <MERGEABLE>
	 */
	@Override
	public SimplificationString visit(MergeableClause n) {
		return n.getF0().accept(this);
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
	public SimplificationString visit(ParallelForConstruct n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		SimplificationString forDirSS = n.getF3().accept(this);
		SimplificationString initExpSS = n.getF5().getF2().accept(this);
		SimplificationString forCondSS = n.getF5().getF4().accept(this);
		SimplificationString reinitExpSS = n.getF5().getF6().accept(this);
		SimplificationString stmtSS = n.getF6().accept(this);

		StringBuilder forSpecificClauses = new StringBuilder(INIT);
		StringBuilder parSpecificClauses = new StringBuilder(INIT);
		List<OmpClause> clauseList = Misc.getClauseList(n);
		for (OmpClause clause : clauseList) {
			if (clause instanceof IfClause || clause instanceof NumThreadsClause || clause instanceof OmpPrivateClause
					|| clause instanceof OmpFirstPrivateClause || clause instanceof OmpLastPrivateClause
					|| clause instanceof OmpSharedClause || clause instanceof OmpCopyinClause
					|| clause instanceof OmpDfltSharedClause || clause instanceof OmpDfltNoneClause
			// || clause instanceof OmpReductionClause) {}
			) {
				parSpecificClauses.append(" " + clause.getInfo().getString() + " ");
			} else {
				forSpecificClauses.append(" " + clause.getInfo().getString() + " ");
			}
		}

		if (forCondSS.hasPrelude() == 0) {
			StringBuilder preString = new StringBuilder(INIT);
			for (Declaration tempDecl : forDirSS.getTemporaryDeclarations()) {
				preString.append(tempDecl.getInfo().getString() + " ");
			}
			for (Declaration tempDecl : initExpSS.getTemporaryDeclarations()) {
				preString.append(tempDecl.getInfo().getString() + " ");
			}
			preString.append(forDirSS.getPrelude());
			preString.append(initExpSS.getPrelude());
			if (preString.length() != 0) {
				ret.getReplacementString().append("{"); // starting of an enclosing
				// compound statement.
				ret.getReplacementString().append(preString);
			}
			ret.getReplacementString().append("\n#pragma omp parallel " + parSpecificClauses);
			ret.getReplacementString().append("\n{"); // starting of a parallel region.
			ret.getReplacementString().append("\n#pragma omp for " + forSpecificClauses);
			ret.getReplacementString().append(n.getF4().getInfo().getString() + " ");
			ret.getReplacementString().append(" for (" + initExpSS.getReplacementString() + "; "
					+ forCondSS.getReplacementString() + ";" + reinitExpSS.getReplacementString() + ")");
			if (reinitExpSS.hasPrelude() == 0) {
				ret.getReplacementString().append(stmtSS.getReplacementString());
			} else {
				ret.getReplacementString().append("{");
				for (Declaration tempDecl : reinitExpSS.getTemporaryDeclarations()) {
					ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
				}
				ret.getReplacementString().append(stmtSS.getReplacementString());
				ret.getReplacementString().append(reinitExpSS.getPrelude());
				ret.getReplacementString().append("}");

			}
			ret.getReplacementString().append("\n}\n"); // end of the parallel region.
			if (preString.length() != 0) {
				ret.getReplacementString().append("}"); // end of the enclosing compound
				// statement
			}
		} else {
			StringBuilder preString = new StringBuilder(INIT);
			for (Declaration tempDecl : forDirSS.getTemporaryDeclarations()) {
				preString.append(tempDecl.getInfo().getString() + " ");
			}
			for (Declaration tempDecl : initExpSS.getTemporaryDeclarations()) {
				preString.append(tempDecl.getInfo().getString() + " ");
			}
			for (Declaration tempDecl : forCondSS.getTemporaryDeclarations()) {
				preString.append(tempDecl.getInfo().getString() + " ");
			}
			preString.append(forDirSS.getPrelude());
			preString.append(initExpSS.getPrelude());
			preString.append(initExpSS.getReplacementString());
			preString.append(forCondSS.getPrelude());
			if (preString.length() != 0) {
				ret.getReplacementString().append("{"); // starting of an enclosing
				// compound statement.
				ret.getReplacementString().append(preString);
			}

			ret.getReplacementString().append("\n#pragma omp parallel " + parSpecificClauses);
			ret.getReplacementString().append("\n{\n"); // starting of a parallel region.
			ret.getReplacementString().append("#pragma omp for " + forSpecificClauses);
			ret.getReplacementString().append(n.getF4().getInfo().getString() + " ");
			ret.getReplacementString().append(" for ( ;" + forCondSS.getReplacementString() + ";)");
			ret.getReplacementString().append("{"); // starting of the body of for.
			for (Declaration tempDecl : reinitExpSS.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
			}
			ret.getReplacementString().append(" " + stmtSS.getReplacementString());
			ret.getReplacementString().append(" " + reinitExpSS.getPrelude());
			ret.getReplacementString().append(" " + reinitExpSS.getReplacementString());
			ret.getReplacementString().append(" " + forCondSS.getPrelude());
			ret.getReplacementString().append("}"); // end of the body of for.
			ret.getReplacementString().append("\n}\n"); // end of the parallel region.
			if (preString.length() != 0) {
				ret.getReplacementString().append("}"); // end of the enclosing compound
				// statement.
			}
		}
		return ret;
	}

	/**
	 * f0 ::= ( AUniqueParallelOrUniqueForOrDataClause() )*
	 */
	@Override
	public SimplificationString visit(UniqueParallelOrUniqueForOrDataClauseList n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= UniqueParallelClause()
	 * | UniqueForClause()
	 * | DataClause()
	 */
	@Override
	public SimplificationString visit(AUniqueParallelOrUniqueForOrDataClause n) {
		return n.getF0().accept(this);
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
	public SimplificationString visit(ParallelSectionsConstruct n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		SimplificationString clauseSS = n.getF3().accept(this);
		SimplificationString sectionsSS = n.getF5().accept(this);

		StringBuilder parSpecificClauses = new StringBuilder(INIT);
		StringBuilder secSpecificClauses = clauseSS.getReplacementString();

		assert (sectionsSS.hasNoTempDeclarations());
		assert (sectionsSS.hasPrelude() == 0);

		if (clauseSS.hasPrelude() == 0) {
			ret.getReplacementString().append("\n#pragma omp parallel " + parSpecificClauses);
			ret.getReplacementString().append("\n{\n#pragma omp sections " + secSpecificClauses);
			ret.getReplacementString().append(" " + n.getF4().getInfo().getString() + " ");
			ret.getReplacementString().append(sectionsSS.getReplacementString());
			ret.getReplacementString().append("}");
		} else {
			ret.getReplacementString().append("{");
			for (Declaration tempDecl : clauseSS.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
			}
			ret.getReplacementString().append(clauseSS.getPrelude() + "\n");
			ret.getReplacementString().append("\n#pragma omp parallel " + parSpecificClauses);
			ret.getReplacementString().append("\n{\n#pragma omp sections " + secSpecificClauses);
			ret.getReplacementString().append(" " + n.getF4().getInfo().getString() + " ");
			ret.getReplacementString().append(sectionsSS.getReplacementString());
			ret.getReplacementString().append("}"); // end of parallel for
			ret.getReplacementString().append("}"); // end of the enclosing compound
			// statement.
		}
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <MASTER>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public SimplificationString visit(MasterConstruct n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		ret.getReplacementString().append("\n#pragma omp master \n");
		ret.getReplacementString().append(n.getF3().accept(this).getReplacementString());
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <CRITICAL>
	 * f2 ::= ( RegionPhrase() )?
	 * f3 ::= OmpEol()
	 * f4 ::= Statement()
	 */
	@Override
	public SimplificationString visit(CriticalConstruct n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		ret.getReplacementString().append("\n#pragma omp critical " + n.getF2().getInfo().getString() + " " + "\n");
		ret.getReplacementString().append(n.getF4().accept(this).getReplacementString());
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ATOMIC>
	 * f2 ::= ( AtomicClause() )?
	 * f3 ::= OmpEol()
	 * f4 ::= ExpressionStatement()
	 */
	@Override
	public SimplificationString visit(AtomicConstruct n) {
		// TODO: Should this visit also rely on needsEncapsulation()?
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		assert (n.getF4() instanceof ExpressionStatement);
		ExpressionStatement expStmt = (ExpressionStatement) n.getF4();
		assert (expStmt.getF0().present());
		Expression exp = (Expression) expStmt.getF0().getNode();
		SimplificationString retExp = exp.accept(this);
		if (!retExp.hasNoTempDeclarations()) {
			for (Declaration tempDecl : retExp.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.toString() + " ");
			}
		}
		if (retExp.hasPrelude() != 0) {
			ret.getReplacementString().append(retExp.getPrelude());
		}
		if (retExp.hasPrelude() == 0) {
			ret.getReplacementString().append("\n#pragma omp atomic " + n.getF2() + "\n" + n.getF4());
		} else {
			ret.getReplacementString()
					.append("\n#pragma omp atomic " + n.getF2() + "\n" + retExp.getReplacementString() + "; ");
		}
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <FLUSH>
	 * f2 ::= ( FlushVars() )?
	 * f3 ::= OmpEol()
	 */
	@Override
	public SimplificationString visit(FlushDirective n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(INIT));
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		ret.getReplacementString().append("\n#pragma omp flush " + n.getF2() + "\n");
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <ORDERED>
	 * f2 ::= OmpEol()
	 * f3 ::= Statement()
	 */
	@Override
	public SimplificationString visit(OrderedConstruct n) {
		SimplificationString retStmt = n.getF3().accept(this);
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		ret.getReplacementString().append("\n#pragma omp ordered\n" + retStmt.getReplacementString());
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <BARRIER>
	 * f2 ::= OmpEol()
	 */
	@Override
	public SimplificationString visit(BarrierDirective n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(INIT));
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		ret.getReplacementString().append("\n#pragma omp barrier\n");
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKWAIT>
	 * f2 ::= OmpEol()
	 */
	@Override
	public SimplificationString visit(TaskwaitDirective n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(INIT));
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		ret.getReplacementString().append("\n#pragma omp taskwait\n");
		return ret;
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <TASKYIELD>
	 * f2 ::= OmpEol()
	 */
	@Override
	public SimplificationString visit(TaskyieldDirective n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(INIT));
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		ret.getReplacementString().append("\n#pragma omp taskyield\n");
		return ret;
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
	public SimplificationString visit(ThreadPrivateDirective n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder("\n#pragma omp threadprivate(" + n.getF3() + ")\n"));
		return ret;
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
	public SimplificationString visit(DeclareReductionDirective n) {
		SimplificationString ret = new SimplificationString();
		SimplificationString combinerSS = n.getF8().accept(this);
		if (Misc.isACall(combinerSS.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF8()).getDeclaration(tempName);
			combinerSS.getTemporaryDeclarations().add(decl);
			combinerSS.getPrelude().append(tempName + " = " + combinerSS.getReplacementString() + ";");
			combinerSS.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		SimplificationString initClauseSS = n.getF10().accept(this);
		StringBuilder preString = new StringBuilder(INIT);
		for (Declaration tempDecl : combinerSS.getTemporaryDeclarations()) {
			preString.append(tempDecl.getInfo().getString() + " ");
		}
		for (Declaration tempDecl : initClauseSS.getTemporaryDeclarations()) {
			preString.append(tempDecl.getInfo().getString() + " ");
		}
		preString.append(combinerSS.getPrelude());
		preString.append(initClauseSS.getPrelude());
		if (preString.length() != 0) {
			ret.setReplacementString(new StringBuilder("{" + preString));
		}
		ret.getReplacementString()
				.append(n.getF0().getInfo().getString() + " " + " declare reduction (" + n.getF4().getInfo().getString()
						+ " " + ":" + n.getF6().getInfo().getString() + " " + ":" + combinerSS.getReplacementString()
						+ ")" + initClauseSS.getReplacementString() + "\n");
		if (preString.length() != 0) {
			ret.getReplacementString().append("}");
		}
		return ret;
	}

	/**
	 * f0 ::= ( TypeSpecifier() )*
	 */
	@Override
	public SimplificationString visit(ReductionTypeList n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(n.getF0() + " "));
		return ret;
	}

	/**
	 * f0 ::= AssignInitializerClause()
	 * | ArgumentInitializerClause()
	 */
	@Override
	public SimplificationString visit(InitializerClause n) {
		return n.getF0().accept(this);
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
	public SimplificationString visit(AssignInitializerClause n) {
		SimplificationString ret = n.getF4().accept(this);
		ret.setReplacementString(new StringBuilder(
				n.getF0().getTokenImage() + "(" + n.getF2().getTokenImage() + "=" + ret.getReplacementString() + ")"));
		return ret;
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
	public SimplificationString visit(ArgumentInitializerClause n) {
		SimplificationString ret = n.getF4().accept(this);
		ret.setReplacementString(new StringBuilder(
				n.getF0().getTokenImage() + "(" + n.getF2().getTokenImage() + "(" + ret.getReplacementString() + "))"));
		return ret;
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
	public SimplificationString visit(ReductionOp n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ( "," <IDENTIFIER> )*
	 */
	@Override
	public SimplificationString visit(VariableList n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(" " + n.getF0() + n.getF1() + " "));
		return ret;
	}

	/**
	 * f0 ::= SimpleLabeledStatement()
	 * | CaseLabeledStatement()
	 * | DefaultLabeledStatement()
	 */
	@Override
	public SimplificationString visit(LabeledStatement n) {
		return n.getLabStmtF0().accept(this);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public SimplificationString visit(SimpleLabeledStatement n) {
		SimplificationString ret = n.getF2().accept(this);
		ret.setReplacementString(new StringBuilder(n.getF0().getTokenImage() + ": " + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= <CASE>
	 * f1 ::= ConstantExpression()
	 * f2 ::= ":"
	 * f3 ::= Statement()
	 */
	@Override
	public SimplificationString visit(CaseLabeledStatement n) {
		SimplificationString ret = n.getF3().accept(this);
		assert (ret.hasNoTempDeclarations());
		assert (ret.hasPrelude() == 0);
		ret.setReplacementString(
				new StringBuilder("case " + n.getF1().getInfo().getString() + " " + ": " + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= <DFLT>
	 * f1 ::= ":"
	 * f2 ::= Statement()
	 */
	@Override
	public SimplificationString visit(DefaultLabeledStatement n) {
		SimplificationString ret = n.getF2().accept(this);
		assert (ret.hasNoTempDeclarations());
		assert (ret.hasPrelude() == 0);
		ret.setReplacementString(new StringBuilder("default :" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= ( Expression() )?
	 * f1 ::= ";"
	 */
	@Override
	public SimplificationString visit(ExpressionStatement n) {
		SimplificationString ret = n.getF0().accept(this);
		if (ret.hasPrelude() != 0) {
			StringBuilder expString = ret.getReplacementString();
			boolean needsEncapsulation = this.needsEncapsulation(n);
			if (needsEncapsulation) {
				ret.setReplacementString(new StringBuilder("{"));
			} else {
				ret.setReplacementString(new StringBuilder(INIT));
			}
			for (Declaration tempDecl : ret.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
			}

			List<Label> labels = n.getInfo().getLabelAnnotations();
			for (Label label : labels) {
				// System.out.println(label.getString());
				ret.getReplacementString().append(label.getString());
			}

			ret.getReplacementString().append(ret.getPrelude());
			ret.getReplacementString().append(expString + ";");
			if (needsEncapsulation) {
				ret.getReplacementString().append("}");
			}
		} else {
			StringBuilder expString = ret.getReplacementString();
			ret.setReplacementString(new StringBuilder(INIT));
			List<Label> labels = n.getInfo().getLabelAnnotations();
			for (Label label : labels) {
				// System.out.println(label.getString());
				ret.getReplacementString().append(label.getString());
			}
			ret.getReplacementString().append(expString);
			ret.getReplacementString().append(";");
		}
		ret.setPrelude(new StringBuilder(INIT));
		ret.getTemporaryDeclarations().clear();
		return ret;
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= ( CompoundStatementElement() )*
	 * f2 ::= "}"
	 */
	@Override
	public SimplificationString visit(CompoundStatement n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		boolean needsEncapsulation = this.needsEncapsulation(n);
		if (needsEncapsulation) {
			ret.getReplacementString().append("{");
		}
		for (Node element : n.getF1().getNodes()) {
			boolean needsBraces = this.originalCS.contains(Misc.getCFGNodeFor(element));
			if (needsBraces) {
				ret.getReplacementString().append("{");
			}
			SimplificationString elementSS = element.accept(this);
			for (Declaration tempDecl : elementSS.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
			}
			ret.getReplacementString().append(elementSS.getPrelude());
			ret.getReplacementString().append(elementSS.getReplacementString());
			if (needsBraces) {
				ret.getReplacementString().append("}");
			}
		}
		if (needsEncapsulation) {
			ret.getReplacementString().append("}");
		}
		return ret;
	}

	/**
	 * f0 ::= Declaration()
	 * | Statement()
	 */
	@Override
	public SimplificationString visit(CompoundStatementElement n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= IfStatement()
	 * | SwitchStatement()
	 */
	@Override
	public SimplificationString visit(SelectionStatement n) {
		return n.getSelStmtF0().accept(this);
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
	public SimplificationString visit(IfStatement n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}

		SimplificationString expSS = n.getF2().accept(this);
		if (Misc.isACall(expSS.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			expSS.getTemporaryDeclarations().add(decl);
			expSS.getPrelude().append(tempName + " = " + expSS.getReplacementString() + ";");
			expSS.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		SimplificationString thenSS = n.getF4().accept(this);
		SimplificationString elseSS = new SimplificationString();
		if (n.getF5().present()) {
			elseSS = ((NodeSequence) n.getF5().getNode()).getNodes().get(1).accept(this);
		}

		boolean needsEncapsulation = this.needsEncapsulation(n);
		if (needsEncapsulation) {
			ret.getReplacementString().append("{");
		}
		for (Declaration tempDecl : expSS.getTemporaryDeclarations()) {
			ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
		}
		ret.getReplacementString().append(expSS.getPrelude());
		ret.getReplacementString().append("if (" + expSS.getReplacementString() + ")");
		assert (thenSS.hasNoTempDeclarations());
		assert (thenSS.hasPrelude() == 0);
		ret.getReplacementString().append(thenSS.getReplacementString());

		if (elseSS.getReplacementString().length() != 0) {
			ret.getReplacementString().append("else ");
			assert (elseSS.hasNoTempDeclarations());
			assert (elseSS.hasPrelude() == 0);
			ret.getReplacementString().append(elseSS.getReplacementString());
		}
		if (needsEncapsulation) {
			ret.getReplacementString().append("}");
		}
		return ret;
	}

	/**
	 * f0 ::= <SWITCH>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public SimplificationString visit(SwitchStatement n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		SimplificationString expSS = n.getF2().accept(this);
		if (Misc.isACall(expSS.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			expSS.getTemporaryDeclarations().add(decl);
			expSS.getPrelude().append(tempName + " = " + expSS.getReplacementString() + ";");
			expSS.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		SimplificationString stmtSS = n.getF4().accept(this);

		boolean needsEncapsulation = this.needsEncapsulation(n);
		if (needsEncapsulation) {
			ret.getReplacementString().append("{");
		}
		for (Declaration tempDecl : expSS.getTemporaryDeclarations()) {
			ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
		}
		ret.getReplacementString().append(expSS.getPrelude());
		ret.getReplacementString().append(" switch (" + expSS.getReplacementString() + ") {");

		assert (stmtSS.hasNoTempDeclarations());
		assert (stmtSS.hasPrelude() == 0);
		ret.getReplacementString().append(stmtSS.getReplacementString() + "}");
		if (needsEncapsulation) {
			ret.getReplacementString().append("}");
		}

		return ret;
	}

	/**
	 * f0 ::= WhileStatement()
	 * | DoStatement()
	 * | ForStatement()
	 */
	@Override
	public SimplificationString visit(IterationStatement n) {
		return n.getItStmtF0().accept(this);
	}

	/**
	 * f0 ::= <WHILE>
	 * f1 ::= "("
	 * f2 ::= Expression()
	 * f3 ::= ")"
	 * f4 ::= Statement()
	 */
	@Override
	public SimplificationString visit(WhileStatement n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		SimplificationString expSS = n.getF2().accept(this);
		if (Misc.isACall(expSS.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
			expSS.getTemporaryDeclarations().add(decl);
			expSS.getPrelude().append(tempName + " = " + expSS.getReplacementString() + ";");
			expSS.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		SimplificationString stmtSS = n.getF4().accept(this);

		boolean needsEncapsulation = this.needsEncapsulation(n);
		if (needsEncapsulation) {
			ret.getReplacementString().append("{");
		}
		for (Declaration tempDecl : expSS.getTemporaryDeclarations()) {
			ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
		}
		ret.getReplacementString().append(expSS.getPrelude());
		ret.getReplacementString().append(" while (" + expSS.getReplacementString() + ") {");

		for (Declaration tempDecl : stmtSS.getTemporaryDeclarations()) {
			ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
		}

		ret.getReplacementString().append(stmtSS.getPrelude());
		ret.getReplacementString().append(stmtSS.getReplacementString());
		ret.getReplacementString().append(expSS.getPrelude() + "}");
		if (needsEncapsulation) {
			ret.getReplacementString().append("}");
		}

		return ret;
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
	public SimplificationString visit(DoStatement n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}

		SimplificationString stmtSS = n.getF1().accept(this);
		SimplificationString expSS = n.getF4().accept(this);
		if (Misc.isACall(expSS.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF4()).getDeclaration(tempName);
			expSS.getTemporaryDeclarations().add(decl);
			expSS.getPrelude().append(tempName + " = " + expSS.getReplacementString() + ";");
			expSS.setReplacementString(new StringBuilder(" " + tempName + " "));
		}

		boolean needsEncapsulation = this.needsEncapsulation(n);
		if (needsEncapsulation) {
			ret.getReplacementString().append("{");
		}
		for (Declaration tempDecl : expSS.getTemporaryDeclarations()) {
			ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
		}
		ret.getReplacementString().append("do {");

		assert (stmtSS.hasNoTempDeclarations());
		ret.getReplacementString().append(stmtSS.getPrelude());
		ret.getReplacementString().append(stmtSS.getReplacementString());
		ret.getReplacementString().append(expSS.getPrelude());
		ret.getReplacementString().append("} while (" + expSS.getReplacementString() + ");");
		if (needsEncapsulation) {
			ret.getReplacementString().append("}");
		}

		return ret;
	}

	/**
	 * Takes care of expression simplification of a loop of the form
	 * <code>for (e1; e2; e3) {}</code>
	 * <p>
	 * f0 ::= <FOR>
	 * f1 ::= "("
	 * f2 ::= (Expression() )?
	 * f3 ::= ";"
	 * f4 ::= (Expression() )?
	 * f5 ::= ";"
	 * f6 ::= (Expression() )?
	 * f7 ::= ")"
	 * f8 ::= Statement()
	 */
	@Override
	public SimplificationString visit(ForStatement n) {
		SimplificationString ret = new SimplificationString();
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		SimplificationString e1SS = n.getF2().accept(this);
		if (n.getF2().present()) {
			if (Misc.isACall(e1SS.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType((Expression) n.getF2().getNode()).getDeclaration(tempName);
				e1SS.getTemporaryDeclarations().add(decl);
				e1SS.getPrelude().append(tempName + " = " + e1SS.getReplacementString() + ";");
				e1SS.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
		}
		SimplificationString e2SS = n.getF4().accept(this);
		if (n.getF4().present()) {
			if (Misc.isACall(e2SS.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType((Expression) n.getF4().getNode()).getDeclaration(tempName);
				e2SS.getTemporaryDeclarations().add(decl);
				e2SS.getPrelude().append(tempName + " = " + e2SS.getReplacementString() + ";");
				e2SS.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
		}
		SimplificationString e3SS = n.getF6().accept(this);
		if (n.getF6().present()) {
			if (Misc.isACall(e3SS.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType((Expression) n.getF6().getNode()).getDeclaration(tempName);
				e3SS.getTemporaryDeclarations().add(decl);
				e3SS.getPrelude().append(tempName + " = " + e3SS.getReplacementString() + ";");
				e3SS.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
		}

		SimplificationString stmtSS = n.getF8().accept(this);

		boolean needsEncapsulation = this.needsEncapsulation(n);
		if (needsEncapsulation) {
			ret.getReplacementString().append("{");
		}
		for (Declaration tempDecl : e1SS.getTemporaryDeclarations()) {
			ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
		}
		for (Declaration tempDecl : e2SS.getTemporaryDeclarations()) {
			ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
		}
		ret.getReplacementString().append(e1SS.getPrelude());
		if (e2SS.hasPrelude() == 0) {
			ret.getReplacementString().append("for(" + e1SS.getReplacementString() + ";");
			ret.getReplacementString().append(e2SS.getReplacementString() + ";" + e3SS.getReplacementString() + ")");

			if (!e3SS.hasNoTempDeclarations() || e3SS.hasPrelude() != 0) {
				ret.getReplacementString().append("{");
				for (Declaration tempDeclaration : e3SS.getTemporaryDeclarations()) {
					ret.getReplacementString().append(tempDeclaration.getInfo().getString() + " ");
				}
			}

			assert (stmtSS.hasPrelude() == 0);
			assert (stmtSS.hasNoTempDeclarations());
			ret.getReplacementString().append(stmtSS.getReplacementString());
			if (!e3SS.hasNoTempDeclarations() || e3SS.hasPrelude() != 0) {
				ret.getReplacementString().append(e3SS.getPrelude());
				ret.getReplacementString().append("}");
			}
		} else {
			assert (!stmtSS.getReplacementString().toString().contains("continue"))
					: "Check this bug at ExpressionSimplifier:2408";
			ret.getReplacementString().append(e1SS.getReplacementString() + ";");
			ret.getReplacementString().append(e2SS.getPrelude());
			ret.getReplacementString().append("for (;" + e2SS.getReplacementString() + ";)");

			if (!e3SS.hasNoTempDeclarations() || e3SS.hasPrelude() != 0 || e2SS.hasPrelude() != 0) {
				ret.getReplacementString().append("{");
				for (Declaration tempDeclaration : e3SS.getTemporaryDeclarations()) {
					ret.getReplacementString().append(tempDeclaration.getInfo().getString() + " ");
				}
			}

			assert (stmtSS.hasPrelude() == 0);
			assert (stmtSS.hasNoTempDeclarations());
			ret.getReplacementString().append(stmtSS.getReplacementString());
			ret.getReplacementString().append(e3SS.getPrelude());
			ret.getReplacementString().append(e3SS.getReplacementString() + ";");
			if (!e3SS.hasNoTempDeclarations() || e3SS.hasPrelude() != 0 || e2SS.hasPrelude() != 0) {
				ret.getReplacementString().append(e2SS.getPrelude());
				ret.getReplacementString().append("}");
			}
		}
		if (needsEncapsulation) {
			ret.getReplacementString().append("}");
		}
		return ret;
	}

	/**
	 * f0 ::= GotoStatement()
	 * | ContinueStatement()
	 * | BreakStatement()
	 * | ReturnStatement()
	 */
	@Override
	public SimplificationString visit(JumpStatement n) {
		return n.getJumpStmtF0().accept(this);
	}

	/**
	 * f0 ::= <GOTO>
	 * f1 ::= <IDENTIFIER>
	 * f2 ::= ";"
	 */
	@Override
	public SimplificationString visit(GotoStatement n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(INIT));
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		ret.getReplacementString().append("goto " + n.getF1().getTokenImage() + "; ");
		return ret;
	}

	/**
	 * f0 ::= <CONTINUE>
	 * f1 ::= ";"
	 */
	@Override
	public SimplificationString visit(ContinueStatement n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(INIT));
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		ret.getReplacementString().append("continue; ");
		return ret;
	}

	/**
	 * f0 ::= <BREAK>
	 * f1 ::= ";"
	 */
	@Override
	public SimplificationString visit(BreakStatement n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(INIT));
		for (Label label : n.getInfo().getLabelAnnotations()) {
			ret.getReplacementString().append(label.getString());
		}
		ret.getReplacementString().append("break; ");
		return ret;
	}

	/**
	 * f0 ::= <RETURN>
	 * f1 ::= ( Expression() )?
	 * f2 ::= ";"
	 */
	@Override
	public SimplificationString visit(ReturnStatement n) {
		SimplificationString ret = new SimplificationString();
		SimplificationString expSS = n.getF1().accept(this);
		if (n.getF1().present()) {
			// System.out.println(expSS.replacementString);
			if (Misc.isACall(expSS.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType((Expression) n.getF1().getNode()).getDeclaration(tempName);
				expSS.getTemporaryDeclarations().add(decl);
				expSS.getPrelude().append(tempName + " = " + expSS.getReplacementString() + ";");
				expSS.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
		}
		if (expSS.hasPrelude() == 0) {
			for (Label label : n.getInfo().getLabelAnnotations()) {
				ret.getReplacementString().append(label.getString());
			}
			ret.getReplacementString().append("return " + expSS.getReplacementString() + ";");
		} else {
			boolean needsEncapsulation = this.needsEncapsulation(n);
			if (needsEncapsulation) {
				ret.setReplacementString(new StringBuilder("{"));
			} else {
				ret.setReplacementString(new StringBuilder(INIT));
			}
			for (Declaration tempDecl : expSS.getTemporaryDeclarations()) {
				ret.getReplacementString().append(tempDecl.getInfo().getString() + " ");
			}
			for (Label label : n.getInfo().getLabelAnnotations()) {
				ret.getReplacementString().append(label.getString());
			}
			ret.getReplacementString().append(expSS.getPrelude() + "return " + expSS.getReplacementString() + ";");
			if (needsEncapsulation) {
				ret.getReplacementString().append("}");
			}
		}
		return ret;
	}

	/**
	 * Splits expressions of the form <code>e1, e2</code>
	 * to <code>e2</code> with <code>e1;</code> as the prelude.
	 * <p>
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public SimplificationString visit(Expression n) {
		SimplificationString ret = n.getExpF0().accept(this);
		if (!n.getExpF1().present()) {
			return ret;
		}
		ret.getPrelude().append(ret.getReplacementString() + ";");
		SimplificationString elementSS = null;
		for (Node nodeSeq : n.getExpF1().getNodes()) {
			AssignmentExpression assignExp = (AssignmentExpression) ((NodeSequence) nodeSeq).getNodes().get(1);
			elementSS = assignExp.accept(this);
			ret.getTemporaryDeclarations().addAll(elementSS.getTemporaryDeclarations());
			ret.getPrelude().append(elementSS.getPrelude());
			if (nodeSeq != n.getExpF1().getNodes().get(n.getExpF1().getNodes().size() - 1)) {
				ret.getPrelude().append(elementSS.getReplacementString() + ";");
			}
		}
		ret.setReplacementString(elementSS.getReplacementString());
		return ret;
	}

	/**
	 * f0 ::= NonConditionalExpression() | ConditionalExpression()
	 */
	@Override
	public SimplificationString visit(AssignmentExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 *
	 * f0 ::= UnaryExpression()
	 * f1 ::= AssignmentOperator()
	 * f2 ::= AssignmentExpression()
	 */
	@Override
	public SimplificationString visit(NonConditionalExpression n) {
		// TODO: Ensure that chained assignments are broken into statements with single
		// assignments.
		SimplificationString rhsSS = n.getF2().accept(this);
		StringBuilder opSS = new StringBuilder(n.getF1().getInfo().getString() + " ");
		if (!opSS.toString().equals("= ")) {
			if (Misc.isACall(rhsSS.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType(n.getF2()).getDeclaration(tempName);
				rhsSS.getTemporaryDeclarations().add(decl);
				rhsSS.getPrelude().append(tempName + " = " + rhsSS.getReplacementString() + ";");
				rhsSS.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
		}
		SimplificationString lhsSS = n.getF0().accept(this);
		rhsSS.getPrelude().append(lhsSS.getPrelude());
		rhsSS.getTemporaryDeclarations().addAll(lhsSS.getTemporaryDeclarations());
		rhsSS.setReplacementString(
				new StringBuilder(lhsSS.getReplacementString() + " " + opSS + " " + rhsSS.getReplacementString()));
		return rhsSS;
	}

	/**
	 * f0 ::= "=" | "*=" | "/=" | "%=" | "+=" | "-=" | "<<=" | ">>=" | "&=" |
	 * "^=" | "|="
	 */
	@Override
	public SimplificationString visit(AssignmentOperator n) {
		return n.getF0().accept(this);
	}

	/**
	 * Splits <code>e1?e2:e3</code> to
	 * <code>int t1; int t2; t1 = e1; if (t1) {t2 = e2;} else {t2 = e3;} t2</code>
	 * <p>
	 * f0 ::= LogicalORExpression()
	 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
	 */
	@Override
	public SimplificationString visit(ConditionalExpression n) {
		SimplificationString ret = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return ret;
		}
		Expression e1 = n.getF0();
		Expression e2 = (Expression) (((NodeSequence) n.getF1().getNode()).getNodes().get(1));
		Expression e3 = (Expression) (((NodeSequence) n.getF1().getNode()).getNodes().get(3));

		Type typeOfE1 = Type.getType(e1);
		Declaration t1Decl = typeOfE1.getDeclaration(Builder.getNewTempName());
		Type typeOfE2 = Type.getType(n);
		Declaration t2Decl = typeOfE2.getDeclaration(Builder.getNewTempName());

		ret.getTemporaryDeclarations().add(t1Decl);
		ret.getTemporaryDeclarations().add(t2Decl);
		StringBuilder t1Str = new StringBuilder(t1Decl.getInfo().getIDNameList().get(0));
		StringBuilder t2Str = new StringBuilder(t2Decl.getInfo().getIDNameList().get(0));

		ret.getPrelude().append(t1Str + " = " + ret.getReplacementString() + ";");
		ret.getPrelude().append("if (" + t1Str + ") {");

		SimplificationString tempStr = e2.accept(this);
		ret.getTemporaryDeclarations().addAll(tempStr.getTemporaryDeclarations());
		ret.getPrelude().append(tempStr.getPrelude());
		ret.getPrelude().append(t2Str + " = " + tempStr.getReplacementString() + ";");

		ret.getPrelude().append("} else {");
		tempStr = e3.accept(this);
		ret.getTemporaryDeclarations().addAll(tempStr.getTemporaryDeclarations());
		ret.getPrelude().append(tempStr.getPrelude());
		ret.getPrelude().append(t2Str + " = " + tempStr.getReplacementString() + ";");

		ret.getPrelude().append("}");

		ret.setReplacementString(t2Str);
		return ret;
	}

	/**
	 * f0 ::= ConditionalExpression()
	 */
	@Override
	public SimplificationString visit(ConstantExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * Splits <code>e1 || e2</code> to
	 * <code>int t; t = e1; if (!t) {t=e2;} t</code>
	 * <p>
	 * f0 ::= LogicalANDExpression()
	 * f1 ::= ( "||" LogicalORExpression() )?
	 */
	@Override
	public SimplificationString visit(LogicalORExpression n) {
		SimplificationString ret = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return n.getF0().accept(this);
		}
		Type typeOfExp = Type.getType(n.getF0());
		Declaration tempDeclaration = typeOfExp.getDeclaration(Builder.getNewTempName());
		ret.getTemporaryDeclarations().add(tempDeclaration);
		StringBuilder tempName = new StringBuilder(tempDeclaration.getInfo().getIDNameList().get(0));

		// Prelude of e1 should precede prelude of (e1 || e2).
		ret.getPrelude().append(" " + tempName + " = " + ret.getReplacementString() + ";");
		ret.getPrelude().append("if (!" + tempName + ") {");

		// Prelude of e2 should be present within the if block, and not with
		// that of (e1 || e2)
		SimplificationString tempSS = ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this);
		ret.getPrelude().append(tempSS.getPrelude());
		ret.getPrelude().append(tempName + " = " + tempSS.getReplacementString() + ";");
		ret.getPrelude().append("}");
		ret.getTemporaryDeclarations().addAll(tempSS.getTemporaryDeclarations());

		ret.setReplacementString(tempName);
		return ret;
	}

	/**
	 * Splits <code>e1 && e2</code> to
	 * <code>int t; t = e1; if (t) {t = e2;} t</code>
	 * <p>
	 * f0 ::= InclusiveORExpression()
	 * f1 ::= ( "&&" LogicalANDExpression() )?
	 */
	@Override
	public SimplificationString visit(LogicalANDExpression n) {
		SimplificationString ret = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return ret;
		}
		Type typeOfExp = Type.getType(n.getF0());
		Declaration tempDeclaration = typeOfExp.getDeclaration(Builder.getNewTempName());
		ret.getTemporaryDeclarations().add(tempDeclaration);
		StringBuilder tempName = new StringBuilder(tempDeclaration.getInfo().getIDNameList().get(0));

		// Prelude of e1 should precede prelude of (e1 && e2).
		ret.getPrelude().append(" " + tempName + " = " + ret.getReplacementString() + ";");
		ret.getPrelude().append("if (" + tempName + ") {");

		// Prelude of e2 should be present within the if block, and not with
		// that of (e1 && e2)
		SimplificationString tempSS = ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this);
		ret.getPrelude().append(tempSS.getPrelude());
		ret.getPrelude().append(tempName + " = " + tempSS.getReplacementString() + ";");
		ret.getPrelude().append("}");
		ret.getTemporaryDeclarations().addAll(tempSS.getTemporaryDeclarations());

		ret.setReplacementString(tempName);
		return ret;
	}

	/**
	 * f0 ::= ExclusiveORExpression()
	 * f1 ::= ( "|" InclusiveORExpression() )?
	 */
	@Override
	public SimplificationString visit(InclusiveORExpression n) {
		SimplificationString ret = n.getF0().accept(this);
		if (n.getF1().present()) {
			if (Misc.isACall(ret.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType(n.getF0()).getDeclaration(tempName);
				ret.getTemporaryDeclarations().add(decl);
				ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
				ret.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
			SimplificationString childSS = ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this);
			if (Misc.isACall(childSS.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type
						.getType(((InclusiveORExpression) ((NodeSequence) n.getF1().getNode()).getNodes().get(1)))
						.getDeclaration(tempName);
				childSS.getTemporaryDeclarations().add(decl);
				childSS.getPrelude().append(tempName + " = " + childSS.getReplacementString() + ";");
				childSS.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
			ret.getPrelude().append(childSS.getPrelude());
			ret.getTemporaryDeclarations().addAll(childSS.getTemporaryDeclarations());
			ret.getReplacementString().append(" | " + childSS.getReplacementString());
		}
		return ret;
	}

	/**
	 * f0 ::= ANDExpression()
	 * f1 ::= ( "^" ExclusiveORExpression() )?
	 */
	@Override
	public SimplificationString visit(ExclusiveORExpression n) {
		SimplificationString ret = n.getF0().accept(this);
		if (n.getF1().present()) {
			if (Misc.isACall(ret.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType(n.getF0()).getDeclaration(tempName);
				ret.getTemporaryDeclarations().add(decl);
				ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
				ret.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
			SimplificationString childSS = ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this);
			if (Misc.isACall(childSS.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type
						.getType(((ExclusiveORExpression) ((NodeSequence) n.getF1().getNode()).getNodes().get(1)))
						.getDeclaration(tempName);
				childSS.getTemporaryDeclarations().add(decl);
				childSS.getPrelude().append(tempName + " = " + childSS.getReplacementString() + ";");
				childSS.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
			ret.getPrelude().append(childSS.getPrelude());
			ret.getTemporaryDeclarations().addAll(childSS.getTemporaryDeclarations());
			ret.getReplacementString().append(" ^ " + childSS.getReplacementString());
		}
		return ret;
	}

	/**
	 * f0 ::= EqualityExpression()
	 * f1 ::= ( "&" ANDExpression() )?
	 */
	@Override
	public SimplificationString visit(ANDExpression n) {
		SimplificationString ret = n.getF0().accept(this);
		if (n.getF1().present()) {
			if (Misc.isACall(ret.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType(n.getF0()).getDeclaration(tempName);
				ret.getTemporaryDeclarations().add(decl);
				ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
				ret.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
			SimplificationString childSS = ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this);
			if (Misc.isACall(childSS.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type
						.getType(((ANDExpression) ((NodeSequence) n.getF1().getNode()).getNodes().get(1)))
						.getDeclaration(tempName);
				childSS.getTemporaryDeclarations().add(decl);
				childSS.getPrelude().append(tempName + " = " + childSS.getReplacementString() + ";");
				childSS.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
			ret.getPrelude().append(childSS.getPrelude());
			ret.getTemporaryDeclarations().addAll(childSS.getTemporaryDeclarations());
			ret.getReplacementString().append(" & " + childSS.getReplacementString());
		}
		return ret;
	}

	/**
	 * f0 ::= RelationalExpression()
	 * f1 ::= ( EqualOptionalExpression() )?
	 */
	@Override
	public SimplificationString visit(EqualityExpression n) {
		SimplificationString ret = n.getF0().accept(this);
		if (n.getF1().present()) {
			if (Misc.isACall(ret.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType(n.getF0()).getDeclaration(tempName);
				ret.getTemporaryDeclarations().add(decl);
				ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
				ret.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
		}
		SimplificationString childSS = n.getF1().accept(this);
		ret.getPrelude().append(childSS.getPrelude());
		ret.getTemporaryDeclarations().addAll(childSS.getTemporaryDeclarations());
		ret.getReplacementString().append(childSS.getReplacementString());
		return ret;
	}

	/**
	 * f0 ::= EqualExpression() | NonEqualExpression()
	 */
	@Override
	public SimplificationString visit(EqualOptionalExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "=="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public SimplificationString visit(EqualExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder("==" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= "!="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public SimplificationString visit(NonEqualExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder("!=" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= ShiftExpression()
	 * f1 ::= ( RelationalOptionalExpression() )?
	 */
	@Override
	public SimplificationString visit(RelationalExpression n) {
		SimplificationString ret = n.getRelExpF0().accept(this);
		if (n.getRelExpF1().present()) {
			if (Misc.isACall(ret.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType(n.getRelExpF0()).getDeclaration(tempName);
				ret.getTemporaryDeclarations().add(decl);
				ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
				ret.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
		}
		SimplificationString childSS = n.getRelExpF1().accept(this);
		ret.getPrelude().append(childSS.getPrelude());
		ret.getTemporaryDeclarations().addAll(childSS.getTemporaryDeclarations());
		ret.getReplacementString().append(childSS.getReplacementString());
		return ret;
	}

	/**
	 * f0 ::= RelationalLTExpression() | RelationalGTExpression() |
	 * RelationalLEExpression() | RelationalGEExpression()
	 */
	@Override
	public SimplificationString visit(RelationalOptionalExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "<"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public SimplificationString visit(RelationalLTExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder("<" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= ">"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public SimplificationString visit(RelationalGTExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder(">" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= "<="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public SimplificationString visit(RelationalLEExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder("<=" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= ">="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public SimplificationString visit(RelationalGEExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder(">=" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= AdditiveExpression()
	 * f1 ::= ( ShiftOptionalExpression() )?
	 */
	@Override
	public SimplificationString visit(ShiftExpression n) {
		SimplificationString ret = n.getF0().accept(this);
		if (n.getF1().present()) {
			if (Misc.isACall(ret.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType(n.getF0()).getDeclaration(tempName);
				ret.getTemporaryDeclarations().add(decl);
				ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
				ret.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
		}
		SimplificationString childSS = n.getF1().accept(this);
		ret.getPrelude().append(childSS.getPrelude());
		ret.getTemporaryDeclarations().addAll(childSS.getTemporaryDeclarations());
		ret.getReplacementString().append(childSS.getReplacementString());
		return ret;
	}

	/**
	 * f0 ::= ShiftLeftExpression() | ShiftRightExpression()
	 */
	@Override
	public SimplificationString visit(ShiftOptionalExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= ">>"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public SimplificationString visit(ShiftLeftExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder(">>" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= "<<"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public SimplificationString visit(ShiftRightExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder("<<" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= MultiplicativeExpression()
	 * f1 ::= ( AdditiveOptionalExpression() )?
	 */
	@Override
	public SimplificationString visit(AdditiveExpression n) {
		SimplificationString ret = n.getF0().accept(this);
		if (n.getF1().present()) {
			if (Misc.isACall(ret.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType(n.getF0()).getDeclaration(tempName);
				ret.getTemporaryDeclarations().add(decl);
				ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
				ret.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
		}
		SimplificationString childSS = n.getF1().accept(this);
		ret.getPrelude().append(childSS.getPrelude());
		ret.getTemporaryDeclarations().addAll(childSS.getTemporaryDeclarations());
		ret.getReplacementString().append(childSS.getReplacementString());
		return ret;
	}

	/**
	 * f0 ::= AdditivePlusExpression() | AdditiveMinusExpression()
	 */
	@Override
	public SimplificationString visit(AdditiveOptionalExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "+"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public SimplificationString visit(AdditivePlusExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder("+" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= "-"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public SimplificationString visit(AdditiveMinusExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder("-" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= CastExpression()
	 * f1 ::= ( MultiplicativeOptionalExpression() )?
	 */
	@Override
	public SimplificationString visit(MultiplicativeExpression n) {
		SimplificationString ret = n.getF0().accept(this);
		if (n.getF1().present()) {
			if (Misc.isACall(ret.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType(n.getF0()).getDeclaration(tempName);
				ret.getTemporaryDeclarations().add(decl);
				ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
				ret.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
		}

		SimplificationString childSS = n.getF1().accept(this);
		ret.getPrelude().append(childSS.getPrelude());
		ret.getTemporaryDeclarations().addAll(childSS.getTemporaryDeclarations());
		ret.getReplacementString().append(childSS.getReplacementString());
		return ret;
	}

	/**
	 * f0 ::= MultiplicativeMultiExpression() | MultiplicativeDivExpression() |
	 * MultiplicativeModExpression()
	 */
	@Override
	public SimplificationString visit(MultiplicativeOptionalExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "*"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public SimplificationString visit(MultiplicativeMultiExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder("*" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= "/"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public SimplificationString visit(MultiplicativeDivExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder("/" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= "%"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public SimplificationString visit(MultiplicativeModExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder("%" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= CastExpressionTyped() | UnaryExpression()
	 */
	@Override
	public SimplificationString visit(CastExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= TypeName()
	 * f2 ::= ")"
	 * f3 ::= CastExpression()
	 */
	@Override
	public SimplificationString visit(CastExpressionTyped n) {
		SimplificationString ret = n.getF3().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF3()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(
				new StringBuilder("(" + n.getF1().getInfo().getString() + " " + ")" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= UnaryExpressionPreIncrement() | UnaryExpressionPreDecrement() |
	 * UnarySizeofExpression() | UnaryCastExpression() | PostfixExpression()
	 */
	@Override
	public SimplificationString visit(UnaryExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public SimplificationString visit(UnaryExpressionPreIncrement n) {
		SimplificationString ret = n.getF1().accept(this);
		ret.setReplacementString(new StringBuilder(" ++" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public SimplificationString visit(UnaryExpressionPreDecrement n) {
		SimplificationString ret = n.getF1().accept(this);
		ret.setReplacementString(new StringBuilder(" --" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= UnaryOperator()
	 * f1 ::= CastExpression()
	 */
	@Override
	public SimplificationString visit(UnaryCastExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(n.getF0().toString() + " "));
			ret.getReplacementString().append(tempName + " ");
		} else {
			ret.setReplacementString(
					new StringBuilder(n.getF0().getInfo().getString() + " " + ret.getReplacementString()));
		}
		return ret;
	}

	/**
	 * f0 ::= SizeofTypeName() | SizeofUnaryExpression()
	 */
	@Override
	public SimplificationString visit(UnarySizeofExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public SimplificationString visit(SizeofUnaryExpression n) {
		SimplificationString ret = new SimplificationString();
		// The next line has been commented since the expression will not be
		// evaluated. Instead, we just need to print the StringBuilder as it is.
		// ret.prelude.append(n.f1.accept(this).prelude);
		ret.setReplacementString(new StringBuilder("sizeof " + n.getF1() + " "));
		return ret;
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= "("
	 * f2 ::= TypeName()
	 * f3 ::= ")"
	 */
	@Override
	public SimplificationString visit(SizeofTypeName n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder("sizeof(" + n.getF2() + ") "));
		return ret;
	}

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public SimplificationString visit(PostfixExpression n) {
		SimplificationString ret = n.getF0().accept(this);
		List<Node> postFixOpList = n.getF1().getF0().getNodes();

		// if (postFixOpList.size() > 0) {
		// this.collapseCalls(ret, Type.getType(n.f0));
		// }
		//
		for (Node postFixOpNode : postFixOpList) {
			Node postFixOp = ((APostfixOperation) postFixOpNode).getF0().getChoice();
			if (postFixOp instanceof ArgumentList) {
				// Step 0: See whether e1 in e1(e2) should be simplified.
				Expression newE1 = FrontEnd.parseAlone(ret.getReplacementString().toString(), Expression.class);
				if (!Misc.isSimplePrimaryExpression(newE1)) {
					// Step 1: Find the type, say T, of e1 in e1(e2).
					Type e1Type = ExpressionTypeGetter.getHalfPostfixExpressionType(n,
							(APostfixOperation) postFixOpNode);
					// Step 2: Create a declaration for a temporary, say t1, with type T.
					// Add declaration to the list of temporary declarations.
					String newTempName = Builder.getNewTempName();
					Declaration e1Decl = e1Type.getDeclaration(newTempName);
					ret.getTemporaryDeclarations().add(e1Decl);

					// Step 3: Create a prelude "t1 = e1;"; append it to the existing prelude for
					// e1.
					ret.getPrelude().append(newTempName + " = " + ret.getReplacementString() + ";");

					// Step 4: Replace the text of e1 by "t1" in the replacement string.
					ret.setReplacementString(new StringBuilder(" " + newTempName + " "));

					// Step 5: Obtain the SimplificationString of e2.
					// Append the prelude to the existing prelude.
					// Add the declarations to the list of temporary declarations.
					// SimplificationString elementSS = postFixOp.accept(this);
					// ret.getPrelude().append(elementSS.getPrelude());
					// ret.getTemporaryDeclarations().addAll(elementSS.getTemporaryDeclarations());
					//
					// // Step 6: Create the appropriate replacement string.
					// ret.getReplacementString().append(elementSS.getReplacementString());
					// continue;
				}
			}
			SimplificationString elementSS = postFixOp.accept(this);
			ret.getPrelude().append(elementSS.getPrelude());
			ret.getTemporaryDeclarations().addAll(elementSS.getTemporaryDeclarations());
			ret.getReplacementString().append(elementSS.getReplacementString());

			// To remove function calls.
			if (postFixOpNode != postFixOpList.get(postFixOpList.size() - 1)) {
				if (Misc.isACall(ret.getReplacementString())) {
					APostfixOperation nextOperation = (APostfixOperation) postFixOpList
							.get(postFixOpList.indexOf(postFixOpNode) + 1);
					Type thisType = ExpressionTypeGetter.getHalfPostfixExpressionType(n, nextOperation);
					String tempName = Builder.getNewTempName();
					Declaration decl = thisType.getDeclaration(tempName);
					ret.getTemporaryDeclarations().add(decl);
					ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
					ret.setReplacementString(new StringBuilder(" " + tempName + " "));

					// OLD CODE: Incomplete..
					// Type prevType = null;
					// if (postFixOpList.indexOf(postFixOpNode) > 0) {
					// APostfixOperation prevOpNode = (APostfixOperation) postFixOpList
					// .get(postFixOpList.indexOf(postFixOpNode) - 1);
					// prevType = ExpressionTypeGetter.getHalfPostfixExpressionType(n, prevOpNode);
					// System.out.println("Type for " + n + " till" + prevOpNode + " is: " +
					// prevType);
					// } else {
					// prevType = Type.getType(n.getF0());
					// }
				}
			}
		}
		return ret;
	}

	/**
	 * f0 ::= ( APostfixOperation() )*
	 */
	@Override
	public SimplificationString visit(PostfixOperationsList n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= BracketExpression() | ArgumentList() | DotId() | ArrowId() |
	 * PlusPlus() | MinusMinus()
	 */
	@Override
	public SimplificationString visit(APostfixOperation n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "++"
	 */
	@Override
	public SimplificationString visit(PlusPlus n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder("++ "));
		return ret;
	}

	/**
	 * f0 ::= "--"
	 */
	@Override
	public SimplificationString visit(MinusMinus n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder("-- "));
		return ret;
	}

	/**
	 * f0 ::= "["
	 * f1 ::= Expression()
	 * f2 ::= "]"
	 */
	@Override
	public SimplificationString visit(BracketExpression n) {
		SimplificationString ret = n.getF1().accept(this);
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
		ret.setReplacementString(new StringBuilder("[" + ret.getReplacementString() + "]"));
		return ret;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ExpressionList() )?
	 * f2 ::= ")"
	 */
	@Override
	public SimplificationString visit(ArgumentList n) {
		SimplificationString ret = n.getF1().accept(this);
		ret.setReplacementString(new StringBuilder("(" + ret.getReplacementString() + ")"));
		return ret;
	}

	/**
	 * f0 ::= "."
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public SimplificationString visit(DotId n) {
		SimplificationString ret = n.getF1().accept(this);
		ret.setReplacementString(new StringBuilder("." + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= "->"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public SimplificationString visit(ArrowId n) {
		SimplificationString ret = n.getF1().accept(this);
		ret.setReplacementString(new StringBuilder("->" + ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= <IDENTIFIER> | Constant() | ExpressionClosed()
	 */
	@Override
	public SimplificationString visit(PrimaryExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= Expression()
	 * f2 ::= ")"
	 */
	@Override
	public SimplificationString visit(ExpressionClosed n) {
		SimplificationString ret = n.getF1().accept(this);

		Expression newExp = FrontEnd.parseAlone(ret.getReplacementString().toString(), Expression.class);
		if (Misc.isSimplePrimaryExpression(newExp)) {
			ret.setReplacementString(new StringBuilder(" " + ret.getReplacementString() + " "));
		} else {
			if (Misc.isACall(ret.getReplacementString())) {
				String tempName = Builder.getNewTempName();
				Declaration decl = Type.getType(n.getF1()).getDeclaration(tempName);
				ret.getTemporaryDeclarations().add(decl);
				ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
				ret.setReplacementString(new StringBuilder(" " + tempName + " "));
			}
			newExp = FrontEnd.parseAlone(ret.getReplacementString().toString(), Expression.class);
			if (Misc.isSimplePrimaryExpression(newExp)) {
				ret.setReplacementString(new StringBuilder(" " + ret.getReplacementString() + " "));
			} else {
				ret.setReplacementString(new StringBuilder("(" + ret.getReplacementString() + ")"));
			}
		}
		return ret;
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public SimplificationString visit(ExpressionList n) {
		SimplificationString ret = new SimplificationString();
		List<AssignmentExpression> reverseList = new ArrayList<>();
		reverseList.add(n.getF0());
		for (Node listElem : n.getF1().getNodes()) {
			AssignmentExpression assignmentExp = (AssignmentExpression) ((NodeSequence) listElem).getNodes().get(1);
			reverseList.add(0, assignmentExp);
		}

		StringBuilder[] idNameList = new StringBuilder[reverseList.size()];
		int i = 0;
		for (AssignmentExpression assignExp : reverseList) {
			SimplificationString childSS = assignExp.accept(this);
			ret.getTemporaryDeclarations().addAll(childSS.getTemporaryDeclarations());
			ret.getPrelude().append(childSS.getPrelude());

			AssignmentExpression newArguExp = FrontEnd.parseAlone(childSS.getReplacementString().toString(),
					AssignmentExpression.class);

			StringBuilder tempName;
			if (!Misc.isSimplePrimaryExpression(newArguExp)) {
				/*
				 * Add tempName = newArguExp; at the end of the prelude,
				 * along with the declaration for tempName.
				 */
				// Using assignExp for simplicity.
				Type argumentType = Type.getType(assignExp);
				Declaration declaration = argumentType.getDeclaration(Builder.getNewTempName());
				ret.getTemporaryDeclarations().add(declaration);
				tempName = new StringBuilder(declaration.getInfo().getIDNameList().get(0));

				StringBuilder assignStr = childSS.getReplacementString();
				ret.getPrelude().append(tempName + " = " + assignStr + ";");
			} else {
				tempName = new StringBuilder(newArguExp.getInfo().getString() + " ");
			}
			idNameList[i++] = tempName;
		}

		for (i = 0; i < idNameList.length - 1; i++) {
			ret.setReplacementString(new StringBuilder(", " + idNameList[i] + ret.getReplacementString()));
		}
		ret.setReplacementString(new StringBuilder(idNameList[i]).append(ret.getReplacementString()));
		return ret;
	}

	/**
	 * f0 ::= <INTEGER_LITERAL> | <FLOATING_POINT_LITERAL> .append(">
	 * | ( <STRING_LITERAL> )+
	 */
	@Override
	public SimplificationString visit(Constant n) {
		SimplificationString ret = new SimplificationString();
		ret.setReplacementString(new StringBuilder(" " + n.getF0() + " "));
		return ret;
	}

	@Override
	public SimplificationString visit(CallStatement n) {
		SimplificationString ret = new SimplificationString();
		// for (Label label : n.getInfo().getLabelAnnotations()) {
		// ret.getReplacementString().append(label.getString());
		// }
		ret.getReplacementString().append(" " + n.toString() + " ");
		return ret;
	}

	@Override
	public SimplificationString visit(PreCallNode n) {
		assert (false);
		return null;
	}

	@Override
	public SimplificationString visit(PostCallNode n) {
		assert (false);
		return null;
	}

	/**
	 * If the replacement string of ret is a function-call, this method
	 * collapses
	 * the call into a temporary of type t1.
	 * 
	 * @param ret
	 *            expression that needs to be collapsed if it is a function
	 *            call.
	 * @param t1
	 *            type of the expression represented by ret.
	 */
	@Deprecated
	public void collapseCalls(SimplificationString ret, Type t1) {
		if (Misc.isACall(ret.getReplacementString())) {
			String tempName = Builder.getNewTempName();
			Declaration decl = t1.getDeclaration(tempName);
			ret.getTemporaryDeclarations().add(decl);
			ret.getPrelude().append(tempName + " = " + ret.getReplacementString() + ";");
			ret.setReplacementString(new StringBuilder(" " + tempName + " "));
		}
	}

	/**
	 * Checks whether {@code s} needs to be encapsulated within a new set of
	 * braces,
	 * if it has been simplified down to more than one statements.
	 * 
	 * @param s
	 *          an object of proper super-type Statement.
	 * @return
	 *         true if {@code s} needs to be encapsulated within
	 *         CompoundStatement
	 *         if it has been simplified down to more than one statements.
	 */
	public boolean needsEncapsulation(Statement s) {
		s = (Statement) Misc.getCFGNodeFor(s);
		if (s.getInfo().hasLabelAnnotations()) {
			return true;
		}
		// if (s instanceof CompoundStatement) {
		// return true;
		// }
		Statement stmt = Misc.getEnclosingNode(s, Statement.class);
		if (stmt == null) {
			return true;
		}
		if (stmt.getParent() instanceof NodeChoice) {
			NodeChoice nodeChoice = (NodeChoice) stmt.getParent();
			if (nodeChoice.getParent() instanceof CompoundStatementElement) {
				return false;
			}
		}
		return true;
	}

}
