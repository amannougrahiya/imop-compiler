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

public abstract class Label extends Annotation {
	protected Statement labeledCFGNode;

	public Label(Statement labeledCFGNode) {
		this.labeledCFGNode = labeledCFGNode;
		assert !(labeledCFGNode instanceof OmpDirective);
		assert !(labeledCFGNode instanceof OmpConstruct);
	}

	public static Label getCopy(Label other) {
		if (other instanceof SimpleLabel) {
			return new SimpleLabel((SimpleLabel) other);
		} else if (other instanceof CaseLabel) {
			return new CaseLabel((CaseLabel) other);
		} else if (other instanceof DefaultLabel) {
			return new DefaultLabel((DefaultLabel) other);
		}
		assert (false);
		return null;
	}

	public Statement getLabeledCFGNode() {
		return this.labeledCFGNode;
	}

	public void setLabeledCFGNode(Statement labeledCFGNode) {
		this.labeledCFGNode = labeledCFGNode;
		assert !(labeledCFGNode instanceof OmpDirective);
		assert !(labeledCFGNode instanceof OmpConstruct);
	}

	public abstract String getString();

	@Override
	public String toString() {
		return this.getString();
	}
}
