/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

public class AnalysisDimension {

	/**
	 * Specifies whether this analysis is flow-sensitive or insensitive.<br>
	 * Default value: flow-sensitive.
	 */
	private final FlowDimension flowDimension;

	/**
	 * Specifies whether this analysis intra-procedural or inter-procedural.
	 */
	@Deprecated
	private final ProceduralDimension proceduralDimension;

	/**
	 * Specifies whether this analysis is context-sensitive or insensitive.<br>
	 * Default value: context-insensitive.
	 */
	private final ContextDimension contextDimension;

	/**
	 * Specifies whether this analysis is field-sensitive or insensitive.<br>
	 * Default value: field-insensitive.
	 */
	private final FieldDimension fieldDimension;

	/**
	 * Specifies whether this analysis path-sensitive or insensitive.<br>
	 * Default value: path-insensitive.
	 */
	private final PathDimension pathDimension;

	/**
	 * Specified whether this analysis is sve-sensitive or insensitive.<br>
	 * Default value: sve-insensitive.
	 */
	private final SVEDimension sveDimension;

	/**
	 * This constructor is used to set the following default values for
	 * different dimensions. <br>
	 * <ul>
	 * <li>flow-sensitive</li>
	 * <li>inter-procedural</li>
	 * <li>context-insensitive</li>
	 * <li>field-insensitive</li>
	 * <li>path-insensitive</li>
	 * </ul>
	 */
	public AnalysisDimension(SVEDimension sveDimension) {
		this.flowDimension = FlowDimension.FLOW_SENSITIVE;
		this.proceduralDimension = ProceduralDimension.INTER_PROCEDURAL;
		this.contextDimension = ContextDimension.CONTEXT_INSENSITIVE;
		this.fieldDimension = FieldDimension.FIELD_INSENSITIVE;
		this.pathDimension = PathDimension.PATH_INSENSITIVE;
		this.sveDimension = sveDimension;
	}

	public AnalysisDimension(SVEDimension sveDimension, ProceduralDimension intraProcedural) {
		this.flowDimension = FlowDimension.FLOW_SENSITIVE;
		this.proceduralDimension = intraProcedural;
		this.contextDimension = ContextDimension.CONTEXT_INSENSITIVE;
		this.fieldDimension = FieldDimension.FIELD_INSENSITIVE;
		this.pathDimension = PathDimension.PATH_INSENSITIVE;
		this.sveDimension = sveDimension;
	}

	public static enum FlowDimension {
		FLOW_SENSITIVE, FLOW_INSENSITIVE
	}

	public static enum ProceduralDimension {
		INTRA_PROCEDURAL, INTER_PROCEDURAL
	}

	public static enum ContextDimension {
		CONTEXT_SENSITIVE, CONTEXT_INSENSITIVE
	}

	public static enum FieldDimension {
		FIELD_SENSITIVE, FIELD_INSENSITIVE
	}

	public static enum PathDimension {
		PATH_SENSITIVE, PATH_INSENSITIVE
	}

	public static enum SVEDimension {
		SVE_SENSITIVE, SVE_INSENSITIVE
	}

	public FlowDimension getFlowDimension() {
		return flowDimension;
	}

	@Deprecated
	public ProceduralDimension getProceduralDimension() {
		return proceduralDimension;
	}

	public ContextDimension getContextDimension() {
		return contextDimension;
	}

	public FieldDimension getFieldDimension() {
		return fieldDimension;
	}

	public PathDimension getPathDimension() {
		return pathDimension;
	}

	public SVEDimension getSVEDimension() {
		return sveDimension;
	}

}
