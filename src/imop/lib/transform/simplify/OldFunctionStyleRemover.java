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
import imop.parser.FrontEnd;

/**
 * This class takes all the FunctionDefintions and replaces old-style parameter
 * declarations with the new style ones.
 */
public class OldFunctionStyleRemover extends DepthFirstProcess {

	/**
	 * f0 ::= ( DeclarationSpecifiers() )?
	 * f1 ::= Declarator()
	 * f2 ::= ( DeclarationList() )?
	 * f3 ::= CompoundStatement()
	 */
	@Override
	public void visit(FunctionDefinition n) {
		// If f0's node is null, then replace it with "int"
		if (n.getF0().getNode() == null) {
			DeclarationSpecifiers declSpec = FrontEnd.parseAlone("int", DeclarationSpecifiers.class);
			declSpec.setParent(n.getF0());
			n.getF0().setNode(declSpec);
		}

		ADeclaratorOp firstDeclOpChoice = (ADeclaratorOp) n.getF1().getF1().getF1().getF0().getNodes().get(0);
		Node firstDeclOp = firstDeclOpChoice.getF0().getChoice();

		if (firstDeclOp instanceof ParameterTypeListClosed) {
			// This is a new-style parameter list
			// Do nothing. No visit to parts required as C doesn't support nested function
			// definitions.
			return;
		} else if (firstDeclOp instanceof OldParameterListClosed) {
			// This is an old-style parameter list.
			// Change this to a new-style parameter list

			if (((OldParameterListClosed) firstDeclOp).getF1().getNode() == null) {
				// Do nothing and return
				return;
			}
			OldParameterList oldParaList = (OldParameterList) ((OldParameterListClosed) firstDeclOp).getF1().getNode();
			/*
			 * Algorithm:
			 * For all the identifiers in the old-style parameter list,
			 * obtain the corresponding declaration and declarator.
			 * If no such information is found, use int for declaration.
			 * else, create the new parameter's string.
			 * Complete the string and add it to the FunctionDefinition
			 * at the appropriate place.
			 */
			String newDeclaratorString = new String();
			newDeclaratorString = n.getF1().getF0().getInfo().getString(); // Add (pointer)?
			newDeclaratorString += n.getF1().getF1().getF0().getInfo().getString(); // From DirectDeclarator, add
																					// IdentifierOrDeclarator
			newDeclaratorString += "("; // Start the new-style parameter list
			/*
			 * From firstDeclOpChoice, get all the identifiers one-by-one and
			 * after finding their appropriate declarations add them
			 */
			// Process the first identifier
			String idName = oldParaList.getF0().getTokenImage();
			Declaration declForParam = getDeclarationFromList(n.getF2(), idName);
			if (declForParam == null) {
				// This parameter is of type "int"
				newDeclaratorString += "int " + idName;
			} else {
				// This parameter is defined in declForParam
				// Get the appropriate string in newDeclarator
				Declarator declaratorForParam = declForParam.getInfo().getDeclarator(idName);
				if (declaratorForParam == null) {
					// Some error
					System.out.println(
							"Can't find declarator for " + idName + " in " + declForParam.getInfo().getString());
					Thread.dumpStack();
					System.exit(0);
				}
				newDeclaratorString += declForParam.getF0().getInfo().getString() + " "
						+ declaratorForParam.getInfo().getString();
			}
			// Process the remaining identifier
			for (Node seq : oldParaList.getF1().getNodes()) {
				newDeclaratorString += ", ";
				assert seq instanceof NodeSequence;
				idName = ((NodeToken) ((NodeSequence) seq).getNodes().get(1)).getTokenImage();
				declForParam = getDeclarationFromList(n.getF2(), idName);
				if (declForParam == null) {
					// This parameter is of type "int"
					newDeclaratorString += "int " + idName;
				} else {
					// This parameter is defined in declForParam
					// Get the appropriate string in newDeclarator
					Declarator declaratorForParam = declForParam.getInfo().getDeclarator(idName);
					if (declaratorForParam == null) {
						// Some error
						System.out.println(
								"Can't find declarator for " + idName + " in " + declForParam.getInfo().getString());
						Thread.dumpStack();
						System.exit(0);
					}
					newDeclaratorString += declForParam.getF0().getInfo().getString() + " "
							+ declaratorForParam.getInfo().getString();
				}
			}
			newDeclaratorString += ")"; // End the new-style parameter list

			// Create the new declarator and make proper adjustments to replace
			// the old-style parameter list with the new style parameter list

			// 1.) Removing (DeclarationList)?
			n.getF2().setNode(null);

			// 2.) Replacing Declarator with newDeclarator obtained from newDeclaratorString
			n.setF1(FrontEnd.parseAlone(newDeclaratorString, Declarator.class));
			n.getF1().setParent(n);
		}
	}

	/**
	 * @param declList:
	 *                  A NodeOptional of DeclarationList to be checked
	 * @param idName:
	 *                  An identifier name
	 * @return The declaration which contains idName
	 */
	public Declaration getDeclarationFromList(NodeOptional declListOption, String idName) {
		if (declListOption.getNode() == null) {
			return null;
		}
		DeclarationList declList = (DeclarationList) declListOption.getNode();
		for (Node seq : declList.getF0().getNodes()) {
			Declaration decl = (Declaration) seq;
			for (String idElem : decl.getInfo().getIDNameList()) {
				if (idElem.equals(idName)) {
					return decl;
				}
			}
		}
		return null;
	}

}
