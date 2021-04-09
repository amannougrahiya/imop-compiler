/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.builder;

import imop.ast.annotation.CaseLabel;
import imop.ast.annotation.DefaultLabel;
import imop.ast.annotation.Label;
import imop.ast.annotation.SimpleLabel;
import imop.ast.info.cfgNodeInfo.ParameterDeclarationInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.typeSystem.StructType;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.analysis.typeSystem.VoidType;
import imop.lib.getter.StringGetter;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Builder {
	public static int counterVar = 0;

	/**
	 * To obtain a new temporary name.
	 * 
	 * @return
	 *         a temporary name.
	 */
	public static String getNewTempName() {
		if (Program.isPrePassPhase) {
			return "_imopVarPre" + counterVar++;
		} else {
			return "_imopVar" + counterVar++;
		}
	}

	public static String getNewTempName(String buffer) {
		if (Program.isPrePassPhase) {
			return buffer + "_imopVarPre" + counterVar++;
		} else {
			return buffer + "_imopVar" + counterVar++;
		}
	}

	/**
	 * Adds the declaration decl to the global list of elements in root
	 * 
	 * @param decl
	 */
	public static void addDeclarationToGlobals(Declaration decl) {
		if (decl == null) {
			return;
		}
		// TODO: Make sure that we are not adding any conflicts in the global namespace.
		TranslationUnit root = Program.getRoot();
		ElementsOfTranslation elemTrans = FrontEnd.parseAlone(decl.getInfo().getString(), ElementsOfTranslation.class);
		((ExternalDeclaration) elemTrans.getF0().getChoice()).getF0().setChoice(decl);
		root.getF0().addNode(0, elemTrans);
		try {
			root.getInfo().addDeclarationEffects(decl); // TODO: This line needs to be tested.
		} catch (Exception e) {
			System.out.println("Found this error while performing automated updates for addition of a declaration "
					+ decl + " to the global scope: ");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Adds the declaration decl to the global list of elements in root
	 * 
	 * @param decl
	 */
	public static void addDeclarationToGlobals(int index, Declaration decl) {
		if (decl == null) {
			return;
		}
		// TODO: Make sure that we are not adding any conflicts in the global namespace.
		TranslationUnit root = Program.getRoot();
		ElementsOfTranslation elemTrans = FrontEnd.parseAlone(decl.getInfo().getString(), ElementsOfTranslation.class);
		((ExternalDeclaration) elemTrans.getF0().getChoice()).getF0().setChoice(decl);
		root.getF0().addNode(index, elemTrans);
		try {
			root.getInfo().addDeclarationEffects(decl); // TODO: This line needs to be tested.
		} catch (Exception e) {
			System.out.println("Found this error while performing automated updates for addition of a declaration "
					+ decl + " to the global scope: ");
			System.out.println(e.getMessage());
		}
	}

	public static void addFunctionToGlobal(FunctionDefinition funcDef) {
		addFunctionToGlobal(-1, funcDef);
	}

	public static void addFunctionToGlobal(int index, FunctionDefinition funcDef) {
		ExternalDeclaration extDecl = new ExternalDeclaration(new NodeChoice(funcDef));
		ElementsOfTranslation elemOfTranslation = new ElementsOfTranslation(new NodeChoice(extDecl));
		TranslationUnit root = Program.getRoot();
		if (index == -1) {
			// Add at the end.
			root.getF0().addNode(elemOfTranslation);
		} else {
			root.getF0().addNode(index, elemOfTranslation);
		}
		Program.getRoot().getInfo().addFunctionToList(funcDef);
	}

	public static void addDeclarationAfterType(Declaration declToBeAdded, StructType type) {
		Declaration typeDecl = type.getDeclaringNode();
		ElementsOfTranslation typeEOT = Misc.getEnclosingNode(typeDecl, ElementsOfTranslation.class);
		int indexOfTD = Program.getRoot().getF0().getNodes().indexOf(typeEOT);
		if (indexOfTD == -1) {
			Misc.warnDueToLackOfFeature(
					"No declaration found for structure " + type.getName() + " in the global scope.", null);
			return;
		}
		Builder.addDeclarationToGlobals(indexOfTD + 1, declToBeAdded);
	}

	public static void addDeclarationBeforeFirstFunction(Declaration declToBeAdded) {
		if (declToBeAdded == null) {
			return;
		}
		TranslationUnit root = Program.getRoot();
		ExternalDeclaration extDeclToAdd = new ExternalDeclaration(new NodeChoice(declToBeAdded));
		ElementsOfTranslation elemOfTranslationToAdd = new ElementsOfTranslation(new NodeChoice(extDeclToAdd));

		int i = -1;
		int index = i;
		for (Node elemOfTransNode : root.getF0().getNodes()) {
			i++;
			ElementsOfTranslation elemOfTrans = (ElementsOfTranslation) elemOfTransNode;
			if (elemOfTrans.getF0().getChoice() instanceof ExternalDeclaration) {
				ExternalDeclaration extDecl = (ExternalDeclaration) elemOfTrans.getF0().getChoice();
				if (extDecl.getF0().getChoice() instanceof FunctionDefinition) {
					index = i;
					break;
				}
			}
		}
		if (index == -1) {
			Misc.exitDueToLackOfFeature(
					"Could not find a function while attempting to insert the following declaration: " + declToBeAdded);
			return;
		}

		root.getF0().addNode(index, elemOfTranslationToAdd);
		try {
			root.getInfo().addDeclarationEffects(declToBeAdded); // TODO: This line needs to be tested.
		} catch (Exception e) {
			System.out.println("Found this error while performing automated updates for addition of a declaration "
					+ declToBeAdded + " to the global scope: ");
			System.out.println(e.getMessage());
		}
	}

	public static void removeDeclarationFromGlobals(Declaration decl) {
		if (decl == null) {
			return;
		}
		TranslationUnit root = Program.getRoot();

		ElementsOfTranslation elemOfTranslation = Misc.getEnclosingNode(decl, ElementsOfTranslation.class);
		root.getF0().removeNode(elemOfTranslation);
		root.getInfo().removeDeclarationEffects(decl); // TODO: This line needs to be tested.
	}

	/**
	 * @param exp
	 * @return A declaration for the expression exp, with some newName
	 */
	public static Declaration getDeclarationFromExpression(Expression exp) {
		String varName = getNewTempName();
		DeclarationStringFromExpressionGetter declStringGetter = new DeclarationStringFromExpressionGetter(varName);
		exp.accept(declStringGetter);
		return FrontEnd.parseAlone(declStringGetter.declCodeString, Declaration.class);
	}

	/**
	 * @param exp
	 * @return A declaration for the expression exp, with some newName
	 */
	public static Declaration getDeclarationFromType(Expression exp) {
		String varName = getNewTempName();
		DeclarationStringFromExpressionGetter declStringGetter = new DeclarationStringFromExpressionGetter(varName);
		exp.accept(declStringGetter);
		return FrontEnd.parseAlone(declStringGetter.declCodeString, Declaration.class);
	}

	/**
	 * @param baseSymbol
	 *                   Symbol from which type is taken to create a new symbol.
	 * @return a declaration for a new variable which is of the type same
	 *         as that of the baseSymbol.
	 *         IMPORTANT:
	 *         Before calling this function ensure consistency of following
	 *         function calls:
	 *         1.) InitParent
	 *         2.) InitSymbolTable
	 */
	public static Declaration getDeclarationFromSymbol(Symbol baseSymbol) {
		// Get the new name for new Declaration
		String varName = getNewTempName();

		/*
		 * There are two possibilities:
		 * A.) baseSymbol belongs to a Declaration.
		 * B.) baseSymbol belongs to a ParameterDeclaration
		 * Both these cases have been handled below
		 */
		if (baseSymbol.getDeclaringNode() == null || baseSymbol.getDeclaringNode() instanceof ParameterDeclaration) {
			/*
			 * CASE B:
			 * baseSymbol belongs to a ParameterDeclaration
			 * Two subcases arise:
			 * i.) baseSymbol has been defined in old-style parameter list.
			 * ii.) baseSymbol has been defined in the new-style parameter list.
			 */
			// Call getDeclarationFromParameter to solve this case
			// It requires the functionDefinition and parameter number
			// FunctionDefinition is baseSymbol.rootScope.
			// Now, we should find the parameter number using this and baseSymbol.name
			int num = -1;
			FunctionDefinition func = (FunctionDefinition) baseSymbol.getDefiningScope();

			Node listNode = ((ADeclaratorOp) func.getF1().getF1().getF1().getF0().getNodes().get(0)).getF0()
					.getChoice();
			if (listNode instanceof ParameterTypeListClosed) {
				ParameterList parList = ((ParameterTypeList) ((ParameterTypeListClosed) listNode).getF1().getNode())
						.getF0();
				// Check if name is present in the first declaration
				if (ParameterDeclarationInfo.getRootParamName(parList.getF0()).equals(baseSymbol.getName())) {
					num = 0;
				} else {
					// Else in the remaining declarations.
					for (Node seq : parList.getF1().getNodes()) {
						ParameterDeclaration paraDecl = (ParameterDeclaration) ((NodeSequence) seq).getNodes().get(1);
						if (ParameterDeclarationInfo.getRootParamName(paraDecl).equals(baseSymbol.getName())) {
							num = 1 + parList.getF1().getNodes().indexOf(seq);
							break;
						}
					}
				}
			} else if (listNode instanceof OldParameterListClosed) {
				OldParameterList paraList = (OldParameterList) ((OldParameterListClosed) listNode).getF1().getNode();
				// Check if name is present in the first declaration
				if (paraList.getF0().getTokenImage().equals(baseSymbol.getName())) {
					num = 0;
				} else {
					// Else in the remaining declarations.
					for (Node seq : paraList.getF1().getNodes()) {
						NodeToken token = (NodeToken) ((NodeSequence) seq).getNodes().get(1);
						if (token.getTokenImage().equals(baseSymbol.getName())) {
							num = 1 + paraList.getF1().getNodes().indexOf(seq);
							break;
						}
					}
				}
			}
			return getDeclarationFromParameter(func, num);
		} else if (baseSymbol.getDeclaringNode() instanceof Declaration) {
			/*
			 * CASE A:
			 * baseSymbol belongs to a Declaration
			 */
			Declaration baseDecl = (Declaration) baseSymbol.getDeclaringNode();
			DeclarationStringFromDeclarationGetter declStringGetter = new DeclarationStringFromDeclarationGetter();
			declStringGetter.varName = varName;
			declStringGetter.baseName = baseSymbol.getName();
			baseDecl.accept(declStringGetter);
			return FrontEnd.parseAlone(declStringGetter.declCodeString, Declaration.class);
		}
		return null;
	}

	/**
	 * @param func
	 *             : A function
	 * @param num
	 *             : A parameter number (starts from 0)
	 * @return a declaration for a new variable which is of the type same as
	 *         that
	 *         of the num-th parameter of given function.
	 */
	public static Declaration getDeclarationFromParameter(FunctionDefinition func, int num) {
		// Get num-th parameter
		Node paramListNode = ((ADeclaratorOp) func.getF1().getF1().getF1().getF0().getNodes().get(0)).getF0()
				.getChoice();
		if (paramListNode instanceof ParameterTypeListClosed) {
			/*
			 * Process new style parameter list
			 */
			ParameterTypeListClosed paraClosed = (ParameterTypeListClosed) paramListNode;
			if (paraClosed.getF1().getNode() != null) {
				ParameterList paraList = ((ParameterTypeList) paraClosed.getF1().getNode()).getF0();
				// Get the num-th parameter
				ParameterDeclaration paraDecl;
				if (num == 0) {
					paraDecl = paraList.getF0();
				} else {
					paraDecl = ((ParameterDeclaration) ((NodeSequence) paraList.getF1().getNodes().get(num - 1))
							.getNodes().get(1));
				}

				// Get the name of the variable to be created
				String varName = getNewTempName();

				// Obtain the string for Declaration
				DeclarationStringFromParameterGetter declStringGetter = new DeclarationStringFromParameterGetter();
				declStringGetter.varName = varName;
				paraDecl.accept(declStringGetter);

				// Return a new declaration, as required.
				return FrontEnd.parseAlone(declStringGetter.declCodeString, Declaration.class);
			}
		} else if (paramListNode instanceof OldParameterListClosed) {
			/*
			 * Process old-style parameter list
			 */
			OldParameterListClosed paraClosed = (OldParameterListClosed) paramListNode;
			// Get the name of num-th parameter
			if (paraClosed.getF1().getNode() != null) {
				OldParameterList paraList = (OldParameterList) paraClosed.getF1().getNode();
				String paramName;
				if (num == 0) {
					paramName = paraList.getF0().getTokenImage();
				} else {
					paramName = ((NodeToken) ((NodeSequence) paraList.getF1().getNodes().get(num - 1)).getNodes()
							.get(1)).getTokenImage();
				}

				// Get the appropriate declaration from DeclarationList of func
				Declaration foundDecl = null;
				if (func.getF2().getNode() != null) {
					outer: for (Node declElemNode : ((DeclarationList) func.getF2().getNode()).getF0().getNodes()) {
						Declaration declElem = (Declaration) declElemNode;
						for (String idName : declElem.getInfo().getIDNameList()) {
							if (idName.equals(paramName)) {
								foundDecl = declElem;
								break outer;
							}
						}
					}
				}
				if (foundDecl == null) {
					return FrontEnd.parseAlone("int " + getNewTempName() + ";", Declaration.class);
				} else { // Found the paramName's declaration in foundDecl

					// Get the name of the variable to be created
					String varName = getNewTempName();

					// Obtain the string for Declaration
					DeclarationStringFromDeclarationGetter declStringGetter = new DeclarationStringFromDeclarationGetter();
					declStringGetter.varName = varName;
					declStringGetter.baseName = paramName;
					foundDecl.accept(declStringGetter);

					// Obtain a new declaration, as required.
					Declaration newDecl = FrontEnd.parseAlone(declStringGetter.declCodeString, Declaration.class);
					// System.out.println("Declaration created is: " +
					// newDecl.getInfo().getString());
					return newDecl;
				}
			}
		}

		return null;
	}

	/**
	 * Returns a Declaration node with a new variable which holds the return
	 * value of func when it is inlined.
	 * 
	 * @param func
	 * @return
	 */
	public static Declaration getDeclarationFromReturn(FunctionDefinition func, Scopeable scope) {
		assert (func.getF0().present());
		Type returnType = Type.getTypeTree((DeclarationSpecifiers) func.getF0().getNode(), scope);

		if (returnType instanceof VoidType) {
			return null;
		}

		return returnType.getDeclaration(Builder.getNewTempName());
	}

	public static Node crudeDuplicateASTNode(Node original) {
		Node duplicate = null;
		duplicate = FrontEnd.parseAlone(original.getInfo().getString(), original.getClass());
		// TODO: Check if any deep copy is needed.
		duplicate.getInfo().setIdNumber(original.getInfo().getIdNumber());
		return duplicate;
	}

	/**
	 * Obtain a copy of {@code origBody} that can be repeated in the target
	 * loop.
	 * <i>NOTE: The returned String is a list of statements, and not enclosed
	 * with a CompoundStatement.</i>.
	 * The resulting string should not have re-declarations of the same
	 * variable, but should have initializations; all label sources and
	 * destinations must be renamed;
	 * 
	 * @param origBody
	 * @return
	 */
	public static String getRepeatableCopyForSameScope(Statement origBody) {
		String newCopy = "";
		CompoundStatement compStmt = (CompoundStatement) Misc.getCFGNodeFor(origBody);
		HashMap<String, String> labelMaps = new HashMap<>();
		for (Node element : compStmt.getInfo().getCFGInfo().getElementList()) {
			if (element instanceof Declaration) {
				// Do not add the declarations again.
				// However, we do not need to perform the explicit initialization again, if any.
				Declaration decl = (Declaration) element;
				for (String declaredName : decl.getInfo().getIDNameList()) {
					Initializer init = decl.getInfo().getInitializer(declaredName);
					if (init != null) {
						newCopy += declaredName + " = ";
						String updatedInit = init.toString();
						newCopy += updatedInit;
						newCopy += ";\n";
					}
				}
				continue;
			} else if (element instanceof Statement) {
				Statement stmt = (Statement) element;
				stmt = (Statement) Misc.getCFGNodeFor(stmt);
				if (stmt.getInfo().hasLabelAnnotations()) {
					List<Label> labelList = stmt.getInfo().getLabelAnnotations();
					List<String> newLabels = new ArrayList<>();
					for (Label l : labelList) {
						if (l instanceof DefaultLabel || l instanceof CaseLabel) {
							// Ignore.
							continue;
						} else {
							SimpleLabel simpleL = (SimpleLabel) l;
							String labelName = simpleL.getLabelName();
							String newName = "";
							if (labelMaps.containsKey(labelName)) {
								newName = labelMaps.get(labelName);
							} else {
								newName = getNewTempName(labelName);
								labelMaps.put(labelName, newName);
							}
							newLabels.add(newName);
						}
					}
					for (String newName : newLabels) {
						newCopy += newName + ": ";
					}
				}

				if (element instanceof GotoStatement) {
					// NOTE: Do not add labels again now!
					GotoStatement gs = (GotoStatement) element;
					String labelName = gs.getInfo().getLabelName();
					String newName = "";
					if (labelMaps.containsKey(labelName)) {
						newName = labelMaps.get(labelName);
					} else {
						newName = getNewTempName(labelName);
						labelMaps.put(labelName, newName);
					}
					newCopy += "goto " + newName + ";\n";
				} else if (element instanceof DummyFlushDirective) {
					newCopy += element.toString() + "\n";
				} else {
					// NOTE: Do not add labels again now!
					String noLabelString = StringGetter.getStringNoLabel(element);
					newCopy += noLabelString + "\n";
				}
			}
		}
		// newCopy += "}";
		return newCopy;
	}

	/**
	 * Creates and returns a new copy of the {@code targetNode}, if it is a
	 * {@link Statement} or a {@link Declaration}.
	 * 
	 * @return
	 *         a new copy of the {@code targetNode}, if it is a
	 *         {@link Statement} or a {@link Declaration};
	 *         {@code null} otherwise.
	 */
	public static Node getCopiedTarget(Node targetNode) {
		// TODO: Handle repetitions of labels.
		Node copiedTarget = null;
		String copyString = targetNode.toString();
		if (targetNode instanceof Statement) {
			copiedTarget = FrontEnd.parseAndNormalize(copyString, Statement.class);
		} else if (targetNode instanceof Declaration) {
			copiedTarget = FrontEnd.parseAndNormalize(copyString, Declaration.class);
		}
		return copiedTarget;
	}

}
