/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform;

import imop.ast.node.external.*;
import imop.lib.analysis.typeSystem.SignedLongLongIntType;
import imop.lib.builder.Builder;
import imop.lib.cg.NodeWithStack;
import imop.lib.transform.updater.InsertImmediatePredecessor;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.util.Set;

public class BarrierCounterInstrumentation {

	/**
	 * Inserts the code for barrier-counter initialization, counter increment,
	 * and counter prints, for each parallel construct.
	 */
	public static void insertBarrierCounters() {
		/**
		 * Insert a counter for barriers across all parallel constructs, in the
		 * global scope; initialize it to 0 in main.
		 */
		String totalCounterName = Builder.getNewTempName("totalBarrCounter");
		Declaration counterDeclaration = SignedLongLongIntType.type().getDeclaration(totalCounterName);
		Builder.addDeclarationToGlobals(counterDeclaration);
		FunctionDefinition mainFunction = Program.getRoot().getInfo().getMainFunction();
		ExpressionStatement totalInit = FrontEnd.parseAndNormalize(totalCounterName + " = 0;",
				ExpressionStatement.class);
		if (mainFunction == null) {
			Misc.exitDueToError("Cannot insert counters for a program with no main function.");
		}
		mainFunction.getInfo().getCFGInfo().getBody().getInfo().getCFGInfo().addElement(0, totalInit);

		for (ParallelConstruct parCons : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			/*
			 * Declare and initialize the counter. Note that the declaration
			 * must be global since barriers can be nested inside called
			 * functions.
			 */
			String counterName = Builder.getNewTempName("barrCounter");
			counterDeclaration = SignedLongLongIntType.type().getDeclaration(counterName);
			Builder.addDeclarationToGlobals(counterDeclaration);

			CompoundStatement parBody = (CompoundStatement) parCons.getInfo().getCFGInfo().getBody();
			// parBody.getInfo().getCFGInfo().addElement(0, counterDeclaration);
			Statement counterInit = FrontEnd.parseAndNormalize(
					"#pragma omp master\n{" + counterName + " = 2" + ";" + totalCounterName + " += 2;}",
					Statement.class);
			parBody.getInfo().getCFGInfo().addElement(0, counterInit);

			/*
			 * Increment the counter immediately before the execution of each
			 * barrier.
			 */
			for (NodeWithStack cfgNodeWithStack : parBody.getInfo().getCFGInfo()
					.getIntraTaskCFGLeafContentsOfSameParLevel()) {
				Node cfgNode = cfgNodeWithStack.getNode();
				if (cfgNode instanceof BarrierDirective) {
					BarrierDirective barrier = (BarrierDirective) cfgNode;
					Statement counterAdd = FrontEnd.parseAndNormalize(
							"#pragma omp master\n{" + counterName + "++;" + totalCounterName + "++;}", Statement.class);
					InsertImmediatePredecessor.insert(barrier, counterAdd);
				}
			}

			/*
			 * Print the counters at the end of each parallel construct.
			 */
			String printCode = "#pragma omp master\n{printf(\"The master thread encountered %d barriers.\\n\", "
					+ counterName + ");}";
			Statement counterPrint = FrontEnd.parseAndNormalize(printCode, Statement.class);
			parBody.getInfo().getCFGInfo().addAtLast(counterPrint);
		}

		/*
		 * Print the value for total counter as well.
		 */
		Set<ReturnStatement> allReturns = Misc.getExactEnclosee(mainFunction, ReturnStatement.class);
		if (allReturns == null || allReturns.isEmpty()) {
			Statement totalBarriers = FrontEnd.parseAndNormalize(
					"printf(\"TOTAL NUMBER OF BARRIERS ENCOUNTERED: %d.\\n\", " + totalCounterName + ");",
					Statement.class);
			mainFunction.getInfo().getCFGInfo().getBody().getInfo().getCFGInfo().addAtLast(totalBarriers);
		} else {
			for (ReturnStatement retStmt : allReturns) {
				Statement totalBarriers = FrontEnd.parseAndNormalize(
						"printf(\"TOTAL NUMBER OF BARRIERS ENCOUNTERED: %d.\\n\", " + totalCounterName + ");",
						Statement.class);
				InsertImmediatePredecessor.insert(retStmt, totalBarriers);
			}
		}
	}
}
