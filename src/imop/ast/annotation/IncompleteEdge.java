/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.annotation;

import imop.ast.node.external.*;

/**
 * @author aman
 *         This edge represents an edge information for which either the source
 *         or the destination is not known.
 */
public class IncompleteEdge extends Annotation {
	private final TypeOfIncompleteness typeOfIncompleteness;

	private final Node incompleteNode;
	private final CaseLabel caseLabel;

	public static enum TypeOfIncompleteness {
		UNKNOWN_CASE_SOURCE, UNKNOWN_DEFAULT_SOURCE, UNKNOWN_GOTO_DESTINATION, UNKNOWN_BREAK_DESTINATION,
		UNKNOWN_CONTINUE_DESTINATION, UNKNOWN_RETURN_DESTINATION
	}

	public IncompleteEdge(TypeOfIncompleteness typeOfIncompleteness, Node incompleteNode) {
		assert (typeOfIncompleteness != TypeOfIncompleteness.UNKNOWN_CASE_SOURCE);
		this.typeOfIncompleteness = typeOfIncompleteness;
		this.incompleteNode = incompleteNode;
		this.caseLabel = null;
	}

	public IncompleteEdge(TypeOfIncompleteness typeOfIncompleteness, Node incompleteNode, CaseLabel caseLabel) {
		assert (caseLabel != null);
		this.typeOfIncompleteness = typeOfIncompleteness;
		this.incompleteNode = incompleteNode;
		this.caseLabel = caseLabel;
	}

	public TypeOfIncompleteness getTypeOfIncompleteness() {
		return typeOfIncompleteness;
	}

	public Node getIncompleteNode() {
		return incompleteNode;
	}

	public CaseLabel getCaseLabel() {
		return caseLabel;
	}
}
