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
import imop.lib.cfg.info.SingleConstructCFGInfo;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.CollectorVisitor;
import imop.parser.FrontEnd;

import java.util.HashSet;
import java.util.Set;

public class SingleConstructInfo extends OmpConstructInfo {

	public SingleConstructInfo(Node owner) {
		super(owner);
	}

	@Override
	public SingleConstructCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new SingleConstructCFGInfo(getNode());
		}
		return (SingleConstructCFGInfo) cfgInfo;
	}

	public void addNowaitClause() {
		NowaitClause clause = FrontEnd.parseAndNormalize("nowait", NowaitClause.class);
		if (this.hasNowaitClause()) {
			return;
		}
		SingleConstruct singleCons = (SingleConstruct) this.getNode();
		ASingleClause wrapper = new ASingleClause(new NodeChoice(clause));
		singleCons.getF2().getF0().addNode(wrapper);
	}

	/**
	 * Checks if this single construct may have multiple instances in any of its
	 * phases.
	 * 
	 * @return
	 *         true, if the EndNode of this construct can reach its BeginNode,
	 *         on a barrier-free path.
	 */
	public boolean isLoopedInPhase() {
		BeginNode beginNode = this.getCFGInfo().getNestedCFG().getBegin();
		EndNode endNode = this.getCFGInfo().getNestedCFG().getEnd();
		NodeWithStack startPoint = new NodeWithStack(endNode, new CallStack());
		Set<NodeWithStack> endPoints = new HashSet<>();
		CollectorVisitor.collectNodeSetInGenericGraph(startPoint, endPoints, p -> {
			Node node = p.getNode();
			if (node == beginNode) {
				return true;
			} else if (node instanceof BarrierDirective) {
				return true;
			} else if (node instanceof EndNode && node.getParent() instanceof ParallelConstruct) {
				return true;
			} else {
				return false;
			}
		}, x -> x.getNode().getInfo().getCFGInfo()
				.getParallelConstructFreeInterProceduralLeafSuccessors(x.getCallStack()));
		if (endPoints.parallelStream().anyMatch(n -> n.getNode() == beginNode)) {
			return true;
		}
		return false;
	}

}
