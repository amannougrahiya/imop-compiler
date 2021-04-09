/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.demo4;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo4 {

	/**
	 * Driver method for Demo #4.
	 * TODO OPTIONS:
	 * 1. System.out.println(Misc.getSymbolEntry(spe.toString(),
	 * callStmt).getDefiningScope());
	 * 2. !spe.isAnIdentifier()
	 * 3. System.out.println(sym.getName() + " has type " + sym.getType());
	 * 4. callStmt.getPreCallNode().getArgumentList()
	 * 5. func.getInfo().getSymbolTable().values()
	 */
	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.c", "-nru" };
		Program.parseNormalizeInput(args);

		// for (FunctionDefinition func : Misc.getInheritedEnclosee(Program.getRoot(),
		// FunctionDefinition.class)) {
		// for (Symbol sym :
		// TODO T1
		// ) {
		// TODO T2
		// }
		// }
		// for (CallStatement callStmt : Misc.getInheritedEnclosee(Program.getRoot(),
		// CallStatement.class)) {
		// for (SimplePrimaryExpression spe :
		// TODO T3
		// ) {
		// if (
		// TODO T4
		// ) {
		// continue;
		// }
		// TODO T5
		// }
		// }
	}

}
