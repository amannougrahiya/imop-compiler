/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis.FlowFact;
import imop.parser.Program;

import java.util.Set;

public class FlowInsensitive {

	/**
	 * Performs flow-insensitive analysis, given a flow-sensitive analysis tag.
	 * <br>
	 * Note that if the calculation of transfer functions relies on flow-facts
	 * (e.g., access lists may rely on points-to flow facts), those flow-facts
	 * would still be field-sensitive, rendering the output of this method
	 * incorrect.
	 * 
	 * @param analysisName
	 * @return
	 */
	public static <F extends FlowFact> F getFlowInsensitiveForwardFact(AnalysisName analysisName) {
		F ff = null;
		@SuppressWarnings("unchecked")
		FlowAnalysis<F> analysisHandle = (FlowAnalysis<F>) FlowAnalysis.getAllAnalyses().get(analysisName);
		if (analysisHandle == null) {
			return ff;
		}
		TranslationUnit root = Program.getRoot();
		FunctionDefinition mainFunc = root.getInfo().getMainFunction();
		if (mainFunc == null) {
			return ff;
		}
		ff = analysisHandle.getEntryFact();
		Set<Node> nodeSet = mainFunc.getInfo().getCFGInfo().getIntraTaskCFGLeafContents();
		boolean changed;
		do {
			changed = false;
			for (Node node : nodeSet) {
				if (node instanceof BeginNode || node instanceof EndNode) {
					continue;
				}
				F temp = node.accept(analysisHandle, ff);
				changed |= ff.merge(temp, null);

			}
		} while (changed);
		return ff;
	}

}
