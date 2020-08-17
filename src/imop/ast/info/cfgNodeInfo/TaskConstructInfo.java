/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.OmpConstructInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.cfg.info.TaskConstructCFGInfo;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.CollectorVisitor;

import java.util.HashSet;
import java.util.Set;

public class TaskConstructInfo extends OmpConstructInfo {

	public TaskConstructInfo(Node owner) {
		super(owner);
	}

	@Override
	public TaskConstructCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new TaskConstructCFGInfo(getNode());
		}
		return (TaskConstructCFGInfo) cfgInfo;
	}

	boolean createsRecursiveTasks() {
		TaskConstruct tc = (TaskConstruct) this.getNode();
		BeginNode beginN = tc.getInfo().getCFGInfo().getNestedCFG().getBegin();
		EndNode endN = tc.getInfo().getCFGInfo().getNestedCFG().getEnd();
		NodeWithStack beginNS = new NodeWithStack(beginN, new CallStack());
		Set<NodeWithStack> endPoints = new HashSet<>();
		CollectorVisitor.collectNodeSetInGenericGraph(beginNS, endPoints, nS -> {
			Node n = nS.getNode();
			if (n == endN) {
				return true;
			} else if (n == beginN) {
				return true;
			} else {
				return false;
			}
		}, nS -> nS.getNode().getInfo().getCFGInfo()
				.getParallelConstructFreeInterProceduralLeafSuccessors(nS.getCallStack()));
		return endPoints.stream().anyMatch(nS -> nS.getNode() == beginN);
	}

}
