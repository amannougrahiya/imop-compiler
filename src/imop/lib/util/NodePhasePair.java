/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import imop.ast.node.external.*;
import imop.lib.analysis.mhp.AbstractPhase;

public class NodePhasePair {
	public final Node one;
	public final Node two;
	public final AbstractPhase<?, ?> ph;

	public NodePhasePair(Node one, Node two, AbstractPhase<?, ?> ph) {
		this.one = one;
		this.two = two;
		this.ph = ph;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		NodePhasePair that = (NodePhasePair) obj;
		if (this.ph != that.ph) {
			return false;
		}
		if ((this.one == that.one && this.two == that.two) || (this.one == that.two && this.two == that.one)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((one == null) ? 0 : one.hashCode());
		result = prime * result + ((ph == null) ? 0 : ph.hashCode());
		result = prime * result + ((two == null) ? 0 : two.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return one + " and " + two + " in phase #" + ph.getPhaseId();
	}

}
