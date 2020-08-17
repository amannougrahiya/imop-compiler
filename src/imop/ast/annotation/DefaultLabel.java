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
import imop.lib.util.Misc;

public class DefaultLabel extends Label {
	public DefaultLabel(Statement labeledCFGNode) {
		super(labeledCFGNode);
	}

	public DefaultLabel(DefaultLabel other) {
		super(other.getLabeledCFGNode());
	}

	public SwitchStatement getParentSwitch() {
		return Misc.getEnclosingSwitch(this.getLabeledCFGNode());
	}

	@Override
	public String getString() {
		return "default: ";
	}

	@Override
	public int hashCode() {
		return this.getParentSwitch().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DefaultLabel)) {
			return false;
		}
		DefaultLabel other = (DefaultLabel) obj;
		if (this.getParentSwitch() == other.getParentSwitch()) {
			return true;
		} else {
			return false;
		}
	}

}
