/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package demo.backup.demo4;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.util.Misc;
import imop.parser.Program;

public class Demo4 {

	public static void main(String[] args) {
		args = new String[] { "-f", "runner/cgo-eg/example.c", "-nru" };
		Program.parseNormalizeInput(args);

		for (FunctionDefinition func : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Symbol sym : func.getInfo().getSymbolTable().values()) {
				System.out.println(sym.getName() + " has type " + sym.getType());
			}
		}
		for (CallStatement callStmt : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (SimplePrimaryExpression spe : callStmt.getPreCallNode().getArgumentList()) {
				if (!spe.isAnIdentifier()) {
					continue;
				}
				System.out.println(Misc.getSymbolEntry(spe.toString(), callStmt).getDefiningScope());
			}
		}

		System.exit(0);

	}

}
