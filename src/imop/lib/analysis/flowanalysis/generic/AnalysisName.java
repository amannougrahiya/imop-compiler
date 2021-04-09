/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

public enum AnalysisName {
	TEST // Dummy pass. Does nothing.
	, OLD_REACHING_DEFINITION // Inter-thread Interprocedural context-insensitive reaching-definition.
	, REACHING_DEFINITION // Optimized inter-thread Interprocedural context-insensitive
							// reaching-definition.
	, LIVENESS, CONSTANT_PROPAGATION // Constant Propagation.
	, DEPRECATED_REACHING_DEFINITION // Interprocedural context-insensitive reaching-definition.
	, LOCKSET_ANALYSIS // Interprocedural (and inter-task needlessly) analysis for obtaining must-lock
						// set.
	, DATA_DEPENDENCE_FORWARD // Sources for inter-task data-dependence edges (flow, anti, and output).
	, DATA_DEPENDENCE_BACKWARD // Destinations for inter-task data-dependence edges (flow, anti, and output).
	, DEPRECATED_POINTSTO // Deprecated inter-thread inter-procedural context-insensitive points-to
	, POINTSTO // Optimized inter-thread inter-procedural context-insensitive points-to.
	, DEPRECATED_COPYPROPAGATION // Copy propagation for symbols.
	, COPYPROPAGATION // Optimized copy propagation for symbols.
	, DOMINANCE // IDFA pass to obtain dominator information.
	, HEAP_VALIDITY // IDFA to check if a heap element is valid at a given location (i.e., not
					// freed).
	, INTRA_PREDICATE_ANALYSIS // Intra-procedural IDFA to obtain a set of paths in the current phase(s) in
								// terms of BranchEdges.
	, PREDICATE_ANALYSIS // Inter-procedural IDFA to obtain a set of paths in the current phase(s) in
							// terms of BranchEdges.
}
