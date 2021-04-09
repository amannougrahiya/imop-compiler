/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.deprecated;

import imop.ast.node.external.*;
import imop.parser.FrontEnd;

/**
 * This class encapsulates the module to unroll the loops.
 * 
 * @author aman
 *
 */
public class Deprecated_LoopTransformations {
	// Old code:
	// public static void unrollLoop(IterationStatement loop, int unrollingFactor) {
	// IterationStatementInfo itInfo = (IterationStatementInfo) loop.getInfo();
	// /*
	// * Step I: Obtain the original loop body for this loop.
	// */
	// IterationStatement originalLoop = itInfo.getOriginalLoopBody();
	//
	// /*
	// * Step II: Now we will process all three loops separately. Our aim is
	// * to attach the original loop body to the end of $loop$, and make
	// * appropriate changes in the updates of the iteration variable.
	// */
	// if (loop instanceof ForStatement) {
	// ForStatement originalForLoop = (ForStatement) originalLoop;
	// ForStatement currentForLoop = (ForStatement) loop;
	//
	// Statement originalBody = originalForLoop.getF8();
	// Statement currentBody = currentForLoop.getF8();
	// NodeOptional originalUpdateExpression = originalForLoop.getF6();
	// NodeOptional originalTerminationCheck = originalForLoop.getF4();
	// String originalUpdateExpressionString, originalTerminationCheckString;
	// if (originalUpdateExpression.present()) {
	// originalUpdateExpressionString =
	// originalUpdateExpression.getNode().getInfo().getString() + " ";
	// } else {
	// originalUpdateExpressionString = "";
	// }
	//
	// if (originalTerminationCheck.present()) {
	// originalTerminationCheckString =
	// originalTerminationCheck.getNode().getInfo().getString() + " ";
	// } else {
	// originalTerminationCheckString = "";
	// }
	//
	// String newBodyString = "{" + currentBody.getInfo().getString() + " " +
	// originalUpdateExpressionString
	// + "; if (" + originalTerminationCheckString + ")" +
	// originalBody.getInfo().getString()
	// + " else {break;}" + "}";
	// Statement newBody = FrontEnd.parseAlone(newBodyString, Statement.class);
	// Misc.normalizeNode(newBody);
	// currentForLoop.setF8(newBody);
	//
	// } else if (loop instanceof WhileStatement) {
	//
	// } else if (loop instanceof DoStatement) {
	//
	// }
	// }

	/**
	 * This is a temporary unrolling method that takes a while loop,
	 * <code>loop</code>, and unrolls it <code>unrollFactor - 1</code> times. A
	 * new AST node is returned back which represents the unrolled while loop.
	 * 
	 * @param loop
	 *                     the while loop which has to be unrolled.
	 * @param unrollFactor
	 *                     the unrolling factor. Note that the unrolling factor of a
	 *                     loop
	 *                     that has not yet been unrolled is 0.
	 * @return A new AST that represents the unrolled loop.
	 */
	@Deprecated
	public static WhileStatement unrollWhile(WhileStatement loop, int unrollFactor) {
		WhileStatement newWhile = null;
		String newWhileString = new String();
		newWhileString = "while(";
		newWhileString += loop.getF2().getInfo().getString();
		newWhileString += ") {";
		newWhileString += loop.getF4().getInfo().getString();
		for (int i = 0; i < unrollFactor; i++) {
			newWhileString += "if (!";
			newWhileString += loop.getF2().getInfo().getString();
			newWhileString += ") {break;}";
			newWhileString += loop.getF4().getInfo().getString();
		}
		newWhileString += "}";
		newWhile = FrontEnd.parseAlone(newWhileString, WhileStatement.class);
		return newWhile;
	}
}
