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

public class SimpleLabel extends Label {
	private String labelName;

	public SimpleLabel(Statement labeledCFGNode, String labelName) {
		super(labeledCFGNode);
		this.labelName = labelName;
	}

	public SimpleLabel(SimpleLabel other) {
		super(other.getLabeledCFGNode());
		this.labelName = other.labelName;
	}

	public String getLabelName() {
		return labelName;
	}

	@Override
	public String getString() {
		return labelName + ": ";
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SimpleLabel)) {
			return false;
		}
		SimpleLabel other = (SimpleLabel) o;
		if (other.labelName.equals(this.labelName)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.labelName == null) ? 0 : this.labelName.hashCode());
		return result;
	}
}
