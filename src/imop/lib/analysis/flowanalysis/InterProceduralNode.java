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
import imop.lib.cg.CallSite;

@Deprecated
public class InterProceduralNode {
	private Node node;
	private CallSite callSite;

	public InterProceduralNode(Node node) {
		this.node = node;
	}

	public InterProceduralNode(CallSite callSite) {
		this.callSite = callSite;
	}

	public Node getNode() {
		return node;
	}

	public boolean isNode() {
		if (node == null) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isCallSite() {
		if (callSite == null) {
			return false;
		} else {
			return true;
		}
	}

	public CallSite getCallSite() {
		return callSite;
	}
}
