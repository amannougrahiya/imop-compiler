/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

public abstract class NonCellularDataFlowAnalysis<F extends FlowAnalysis.FlowFact> extends DataFlowAnalysis<F> {

	public NonCellularDataFlowAnalysis(AnalysisName analysisName, AnalysisDimension analysisDimension) {
		super(analysisName, analysisDimension);
	}

}
