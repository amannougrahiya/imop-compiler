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

public class PragmaImop extends Annotation {
	private String pragmaString;
	private Node annotatedNode;

	public PragmaImop(String pragmaString) {
		this.pragmaString = pragmaString;
	}

	public PragmaImop(String pragmaString, Node annotatedNode) {
		assert (annotatedNode != null);
		this.pragmaString = pragmaString;
		this.annotatedNode = annotatedNode;
	}

	public String getPragmaString() {
		return pragmaString;
	}

	public Node getAnnotatedNode() {
		return annotatedNode;
	}

	@Override
	public String toString() {
		return "\n#pragma imop " + this.pragmaString + "\n";
	}

}
