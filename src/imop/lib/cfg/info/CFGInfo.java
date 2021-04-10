/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.info;

import imop.ast.info.NodeInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.DFable;
import imop.lib.analysis.flowanalysis.SCC;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.flowanalysis.generic.IDFAEdge;
import imop.lib.analysis.flowanalysis.generic.IDFAEdgeWithStack;
import imop.lib.cfg.CFGGenerator;
import imop.lib.cfg.NestedCFG;
import imop.lib.cfg.link.autoupdater.EndReachabilityAdjuster;
import imop.lib.cfg.parallel.InterTaskEdge;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.getter.AllCFGNodeGetter;
import imop.lib.util.CellSet;
import imop.lib.util.CollectorVisitor;
import imop.lib.util.Misc;
import imop.lib.util.ProfileSS;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CFGInfo {
	public static long uniPrecisionTimer = 0;

	public static void resetStaticFields() {
		CFGInfo.uniPrecisionTimer = 0;
	}

	private Node owner;

	private List<Node> succBlocks;
	private List<Node> predBlocks;
	private NestedCFG nestedCFG;

	public static boolean isSCCStale = true;
	private SCC scc;
	private Set<DFable> dataFlowPredecessors;
	private Set<DFable> dataFlowSuccessors;
	private int dfsIndex = -1;
	private int lowLink = -1;
	private boolean onInternalStack = false;

	public CFGInfo(Node node) {
		this.setOwner(node);
	}

	public SCC getSCC() {
		if (isSCCStale) {
			SCC.initializeSCC();
		}
		return scc;
	}

	public void setSCC(SCC scc) {
		this.dataFlowPredecessors = this.dataFlowSuccessors = null;
		this.scc = scc;
	}

	public Set<DFable> getDataFlowPredecessors() {
		if (isSCCStale) {
			SCC.initializeSCC();
		}
		if (this.dataFlowPredecessors == null) {
			this.dataFlowPredecessors = new HashSet<>();
			Node node = this.owner;
			for (Node pred : SCC.getInterTaskLeafPredecessorNodes(node, SVEDimension.SVE_INSENSITIVE)) {
				SCC predSCC = pred.getInfo().getCFGInfo().getSCC();
				if (predSCC == null) {
					this.dataFlowPredecessors.add(pred);
				} else {
					this.dataFlowPredecessors.add(predSCC);
				}
			}
		}
		return this.dataFlowPredecessors;
	}

	public Set<DFable> getDataFlowSuccessors() {
		if (isSCCStale) {
			SCC.initializeSCC();
		}
		if (this.dataFlowSuccessors == null) {
			this.dataFlowSuccessors = new HashSet<>();
			Node node = this.owner;
			for (Node succ : SCC.getInterTaskLeafSuccessorNodes(node, SVEDimension.SVE_INSENSITIVE)) {
				SCC succSCC = succ.getInfo().getCFGInfo().getSCC();
				if (succSCC == null) {
					this.dataFlowSuccessors.add(succ);
				} else {
					this.dataFlowSuccessors.add(succSCC);
				}
			}
		}
		return this.dataFlowSuccessors;
	}

	public int getDFSIndex() {
		return dfsIndex;
	}

	public void setDFSIndex(int dfsIndex) {
		this.dfsIndex = dfsIndex;
	}

	public int getLowLink() {
		return lowLink;
	}

	public void setLowLink(int lowLink) {
		this.lowLink = lowLink;
	}

	public boolean isOnInternalStack() {
		return onInternalStack;
	}

	public void setOnInternalStack(boolean onInternalStack) {
		this.onInternalStack = onInternalStack;
	}

	/**
	 * Returns the RPO index of the SCC corresponding to this node.
	 *
	 * @return the RPO index of the SCC corresponding to this node.
	 */
	public int getSCCRPOIndex() {
		if (this.getSCC() == null) {
			return this.getOwner().getReversePostOrder();
		} else {
			return this.getSCC().getReversePostOrder();
		}
	}

	/**
	 * Clears the list of successors of the node node. Furthermore, removes the node
	 * node as a predecessor from its
	 * successors.
	 */
	public void clearSuccBlocks() {
		for (Node succ : new HashSet<>(getSuccBlocks())) {
			CFGInfo.disconnectAndAdjustEndReachability(this.owner, succ);
		}
	}

	/**
	 * Clears the list of predecessor of the node node. Furthermore, removes the
	 * node node as a successor from its
	 * predecessors.
	 */
	public void clearPredBlocks() {
		for (Node pred : new HashSet<>(this.getPredBlocks())) {
			CFGInfo.disconnectAndAdjustEndReachability(pred, this.owner);
		}
	}

	public void clearAllEdges() {
		this.clearPredBlocks();
		this.clearSuccBlocks();
	}

	/**
	 * Obtain intra-procedural intra-task successors at the same nesting level of a
	 * CFG node. Note that for an {@link
	 * EndNode}, this method returns an empty list. Each {@link CallStatement}, and
	 * {@link DummyFlushDirective}, is
	 * treated as any other node.
	 *
	 * @return intra-procedural intra-task successors at same nesting level. (Note:
	 *         an empty list is returned for {@link
	 *         EndNode}.)
	 */
	public List<Node> getSuccBlocks() {
		ProfileSS.addRelevantChangePoint(ProfileSS.cfgSet);
		if (succBlocks == null) {
			succBlocks = new ArrayList<>();
		}
		return succBlocks;
	}

	/**
	 * Obtain intra-procedural intra-task successors at the same nesting level of a
	 * CFG node. Note that for an {@link
	 * BeginNode}, this method returns an empty list. Each {@link CallStatement},
	 * and {@link DummyFlushDirective}, is
	 * treated as any other node.
	 *
	 * @return intra-procedural intra-task successors at same nesting level. (Note:
	 *         an empty list is returned for {@link
	 *         BeginNode}.)
	 */
	public List<Node> getPredBlocks() {
		ProfileSS.addRelevantChangePoint(ProfileSS.cfgSet);
		if (predBlocks == null) {
			predBlocks = new ArrayList<>();
		}
		return predBlocks;
	}

	/**
	 * Obtain CFG successors of a node, such that for an {@link EndNode}, we obtain
	 * the successors of the parent node.
	 *
	 * @return CFG successors of a node, such that for an {@link EndNode}, we obtain
	 *         the successors of the parent node.
	 */
	public List<Node> getSuccessors() {
		if (this.owner instanceof EndNode) {
			return this.owner.getParent().getInfo().getCFGInfo().getSuccBlocks();
		} else {
			return this.getSuccBlocks();
		}
	}

	/**
	 * Obtain CFG predecessors of a node, such that for a {@link BeginNode}, we
	 * obtain the predecessors of the parent
	 * node.
	 *
	 * @return CFG predecessors of a node, such that for a {@link BeginNode}, we
	 *         obtain the predecessors of the parent
	 *         node.
	 */
	public List<Node> getPredecessors() {
		if (this.owner instanceof BeginNode) {
			return this.owner.getParent().getInfo().getCFGInfo().getPredBlocks();
		} else {
			return this.getPredBlocks();
		}
	}

	public List<Node> getLeafSuccessors() {
		List<Node> leafSuccessors = new ArrayList<>();
		List<Node> successors = this.getSuccessors();
		for (Node s : successors) {
			if (Misc.isCFGLeafNode(s)) {
				leafSuccessors.add(s);
			} else if (Misc.isCFGNonLeafNode(s)) {
				leafSuccessors.add(s.getInfo().getCFGInfo().getNestedCFG().getBegin());
			}
		}
		return leafSuccessors;
	}

	public List<NodeWithStack> getLeafSuccessors(CallStack callStack) {
		List<NodeWithStack> retList = new ArrayList<>();
		for (Node n : this.getLeafSuccessors()) {
			retList.add(new NodeWithStack(n, callStack));
		}
		return retList;
	}

	public List<Node> getLeafPredecessors() {
		List<Node> leafPredecessors = new ArrayList<>();
		List<Node> predecessors = this.getPredecessors();
		for (Node p : predecessors) {
			if (Misc.isCFGLeafNode(p)) {
				leafPredecessors.add(p);
			} else if (Misc.isCFGNonLeafNode(p)) {
				leafPredecessors.add(p.getInfo().getCFGInfo().getNestedCFG().getEnd());
			}
		}
		return leafPredecessors;
	}

	public List<NodeWithStack> getLeafPredecessors(CallStack callStack) {
		List<NodeWithStack> retList = new ArrayList<>();
		for (Node n : this.getLeafPredecessors()) {
			retList.add(new NodeWithStack(n, callStack));
		}
		return retList;
	}

	/**
	 * Obtain a set of leaf nodes that are immediate successors of the owner node in
	 * a context-insensitive path on the
	 * super graph.
	 *
	 * @return a set of leaf nodes that are immediate successors of the owner node
	 *         in a context-insensitive path on the
	 *         super graph.
	 */
	public Set<Node> getInterProceduralLeafSuccessors() {
		Node baseNode = this.getOwner();
		Set<Node> interProcSuccNodes = new HashSet<>();

		if (baseNode instanceof EndNode) {
			EndNode endNode = (EndNode) baseNode;
			if (endNode.getParent() instanceof FunctionDefinition) {
				FunctionDefinition calledFunction = (FunctionDefinition) endNode.getParent();
				Set<CallStatement> callSites = new HashSet<>(calledFunction.getInfo().getCallersOfThis());

				for (CallStatement callSite : callSites) {
					interProcSuccNodes.add(callSite.getPostCallNode());
				}
				return interProcSuccNodes;
			}
		}
		if (baseNode instanceof PreCallNode) {
			CallStatement callStmt = (CallStatement) baseNode.getParent();
			List<FunctionDefinition> calledDefinitions = callStmt.getInfo().getCalledDefinitions();

			// TODO: Verify the semantics of this snippet of code.
			if (calledDefinitions.isEmpty()) {
				interProcSuccNodes.add(callStmt.getPostCallNode());
			}
			////////////////////////////////////////////////////

			for (FunctionDefinition funcDef : calledDefinitions) {
				interProcSuccNodes.add(funcDef.getInfo().getCFGInfo().getNestedCFG().getBegin());
			}
			return interProcSuccNodes;
		}
		for (Node succNode : baseNode.getInfo().getCFGInfo().getSuccessors()) {
			if (Misc.isCFGNonLeafNode(succNode)) {
				interProcSuccNodes.add(succNode.getInfo().getCFGInfo().getNestedCFG().getBegin());
			} else {
				interProcSuccNodes.add(succNode);
			}
		}
		return interProcSuccNodes;
	}

	/**
	 * Obtain a set of leaf nodes that are immediate successors of the owner node in
	 * a context-sensitive path on the
	 * super graph.
	 *
	 * @param callStack
	 *                  call stack used for context-sensitive traversal.
	 *
	 * @return a set of leaf nodes that are immediate successors of the owner node
	 *         in a context-sensitive path on the
	 *         super graph.
	 */
	public Set<NodeWithStack> getInterProceduralLeafSuccessors(CallStack callStack) {
		Node baseNode = this.getOwner();
		Set<NodeWithStack> interProcSuccNodeStacks = new HashSet<>();

		if (baseNode instanceof EndNode) {
			EndNode endNode = (EndNode) baseNode;
			if (endNode.getParent() instanceof FunctionDefinition) {
				Set<CallStatement> callSites = new HashSet<>();
				if (callStack.isContextSensitiveStack()) {
					CallStatement caller = callStack.peek();
					callSites.add(caller);
				} else {
					FunctionDefinition calledFunction = (FunctionDefinition) endNode.getParent();
					callSites.addAll(calledFunction.getInfo().getCallersOfThis());
				}

				for (CallStatement callSite : callSites) {
					CallStack newCallStack = CallStack.pop(callStack);
					interProcSuccNodeStacks.add(new NodeWithStack(callSite.getPostCallNode(), newCallStack));
				}
				return interProcSuccNodeStacks;
			}
		}

		if (baseNode instanceof PreCallNode) {
			CallStatement callStmt = (CallStatement) baseNode.getParent();

			List<FunctionDefinition> calledDefinitions = callStmt.getInfo().getCalledDefinitions();

			// TODO: Verify the semantics of this snippet of code.
			if (calledDefinitions.isEmpty()) {
				interProcSuccNodeStacks.add(new NodeWithStack(callStmt.getPostCallNode(), callStack));
			}
			////////////////////////////////////////////////////

			for (FunctionDefinition funcDef : calledDefinitions) {
				CallStack newStack = CallStack.pushUnique(callStack, callStmt);
				interProcSuccNodeStacks
						.add(new NodeWithStack(funcDef.getInfo().getCFGInfo().getNestedCFG().getBegin(), newStack));
			}
			return interProcSuccNodeStacks;
		}

		for (Node successor : baseNode.getInfo().getCFGInfo().getSuccessors()) {
			if (Misc.isCFGNonLeafNode(successor)) {
				interProcSuccNodeStacks
						.add(new NodeWithStack(successor.getInfo().getCFGInfo().getNestedCFG().getBegin(), callStack));
			} else {
				interProcSuccNodeStacks.add(new NodeWithStack(successor, callStack));
			}
		}
		return interProcSuccNodeStacks;
	}

	/**
	 * Obtain a set of leaf nodes that are immediate predecessors of the owner node
	 * in a context-insensitive path on the
	 * super graph.
	 *
	 * @return a set of leaf nodes that are immediate predecessors of the owner node
	 *         in a context-insensitive path on
	 *         the super graph.
	 */
	public Set<Node> getInterProceduralLeafPredecessors() {
		Node baseNode = this.getOwner();
		Set<Node> interProcPredNodes = new HashSet<>();
		if (baseNode instanceof BeginNode) {
			BeginNode beginNode = (BeginNode) baseNode;
			if (beginNode.getParent() instanceof FunctionDefinition) {
				FunctionDefinition calledFunction = (FunctionDefinition) beginNode.getParent();
				List<CallStatement> callSites = new ArrayList<>();
				callSites.addAll(calledFunction.getInfo().getCallersOfThis());
				for (CallStatement callSite : callSites) {
					interProcPredNodes.add(callSite.getPreCallNode());
				}
				return interProcPredNodes;
			}
		}
		if (baseNode instanceof PostCallNode) {
			CallStatement callStmt = (CallStatement) baseNode.getParent();
			List<FunctionDefinition> calledDefinitions = callStmt.getInfo().getCalledDefinitions();

			// TODO: Verify the semantics of this snippet of code.
			if (calledDefinitions.isEmpty()) {
				interProcPredNodes.add(callStmt.getPreCallNode());
			}
			////////////////////////////////////////////////////

			for (FunctionDefinition calledFunc : calledDefinitions) {
				EndNode endNode = calledFunc.getInfo().getCFGInfo().getNestedCFG().getEnd();
				interProcPredNodes.add(endNode);
			}
			return interProcPredNodes;
		}

		for (Node predecessor : baseNode.getInfo().getCFGInfo().getPredecessors()) {
			if (Misc.isCFGNonLeafNode(predecessor)) {
				interProcPredNodes.add(predecessor.getInfo().getCFGInfo().getNestedCFG().getEnd());
			} else {
				interProcPredNodes.add(predecessor);
			}
		}
		return interProcPredNodes;
	}

	/**
	 * Obtain a set of leaf nodes that are immediate predecessors of the owner node
	 * in a context-sensitive path on the
	 * super graph.
	 *
	 * @param callStack
	 *                  call stack used for context-sensitive traversal.
	 *
	 * @return a set of leaf nodes that are immediate predecessors of the owner node
	 *         in a context-sensitive path on the
	 *         super graph.
	 */
	public Set<NodeWithStack> getInterProceduralLeafPredecessors(CallStack callStack) {
		Node baseNode = this.getOwner();
		Set<NodeWithStack> interProcPredNodeStacks = new HashSet<>();
		if (baseNode instanceof BeginNode) {
			BeginNode beginNode = (BeginNode) baseNode;
			if (beginNode.getParent() instanceof FunctionDefinition) {
				List<CallStatement> callSites = new ArrayList<>();
				if (callStack.isContextSensitiveStack()) {
					CallStatement caller = callStack.peek();
					callSites.add(caller);
				} else {
					FunctionDefinition calledFunction = (FunctionDefinition) beginNode.getParent();
					callSites.addAll(calledFunction.getInfo().getCallersOfThis());
				}

				for (CallStatement callSite : callSites) {
					CallStack newCallStack = CallStack.pop(callStack);
					interProcPredNodeStacks.add(new NodeWithStack(callSite.getPreCallNode(), newCallStack));
				}
				return interProcPredNodeStacks;
			}
		}
		if (baseNode instanceof PostCallNode) {
			CallStatement callStmt = (CallStatement) baseNode.getParent();
			List<FunctionDefinition> calledDefinitions = callStmt.getInfo().getCalledDefinitions();

			// TODO: Verify the semantics of this snippet of code.
			if (calledDefinitions.isEmpty()) {
				interProcPredNodeStacks.add(new NodeWithStack(callStmt.getPreCallNode(), callStack));
			}
			////////////////////////////////////////////////////

			for (FunctionDefinition calledFunc : calledDefinitions) {
				EndNode endNode = calledFunc.getInfo().getCFGInfo().getNestedCFG().getEnd();
				CallStack newStack = CallStack.pushUnique(callStack, callStmt);
				interProcPredNodeStacks.add(new NodeWithStack(endNode, newStack));
			}
			return interProcPredNodeStacks;
		}

		for (Node predecessor : baseNode.getInfo().getCFGInfo().getPredecessors()) {
			if (Misc.isCFGNonLeafNode(predecessor)) {
				interProcPredNodeStacks
						.add(new NodeWithStack(predecessor.getInfo().getCFGInfo().getNestedCFG().getEnd(), callStack));
			} else {
				interProcPredNodeStacks.add(new NodeWithStack(predecessor, callStack));
			}
		}
		return interProcPredNodeStacks;
	}

	/**
	 * Obtain a list of successor leaf nodes, while preserving the order present in
	 * block-level successors (if they are
	 * present in that list).
	 *
	 * @return list of successor leaf nodes, in order in which they are present in
	 *         the block-level successors.
	 */
	public List<NodeWithStack> getInterTaskLeafSuccessorList(CallStack callStack) {
		Node node = this.getOwner();
		if (node instanceof DummyFlushDirective) {
			return new ArrayList<>(this.getInterTaskLeafSuccessorNodes(callStack));
		} else if (node instanceof PreCallNode
				|| (node instanceof EndNode && ((EndNode) node).getParent() instanceof FunctionDefinition)) {
			return new ArrayList<>(this.getInterProceduralLeafSuccessors(callStack));
		} else {
			return this.getLeafSuccessors(callStack);
		}
	}

	/**
	 * Obtain a list of predecessor leaf nodes, while preserving the order present
	 * in block-level predecessors (if they
	 * are present in that list).
	 *
	 * @return list of predecessor leaf nodes, in order in which they are present in
	 *         the block-level predecessors.
	 */
	public List<Node> getInterTaskLeafPredecessorList() {
		Node node = this.getOwner();
		if (node instanceof DummyFlushDirective) {
			return new ArrayList<>(this.getInterTaskLeafPredecessorNodes());
		} else if (node instanceof PostCallNode
				|| (node instanceof BeginNode && ((BeginNode) node).getParent() instanceof FunctionDefinition)) {
			return new ArrayList<>(this.getInterProceduralLeafPredecessors());
		} else {
			return this.getLeafPredecessors();
		}
	}

	/**
	 * Returns the inter-procedural leaf successors of the owner, except when it is
	 * a BeginNode of a ParallelConstruct.
	 * When owner is a BeginNode of a ParallelConstruct, the immediate
	 * inter-procedural leaf successors of the enclosing
	 * parallel construct are returned.
	 *
	 * @param phasePoint
	 *
	 * @return
	 */
	public Set<NodeWithStack> getParallelConstructFreeInterProceduralLeafSuccessors(CallStack callStack) {
		Node baseNode = this.getOwner();
		if (baseNode instanceof BeginNode) {
			BeginNode beginNode = (BeginNode) baseNode;
			if (beginNode.getParent() instanceof ParallelConstruct) {
				ParallelConstruct parConstruct = (ParallelConstruct) beginNode.getParent();
				return parConstruct.getInfo().getCFGInfo().getInterProceduralLeafSuccessors(callStack);
			}
		}
		if (baseNode instanceof EndNode) {
			EndNode endNode = (EndNode) baseNode;
			if (endNode.getParent() instanceof ParallelConstruct) {
				return new HashSet<>();
			}
		}
		return this.getInterProceduralLeafSuccessors(callStack);
	}

	/**
	 * Returns the inter-procedural leaf predecessors of the owner, except when it
	 * is an EndNode of a ParallelConstruct.
	 * When owner is an EndNode of a ParallelConstruct, the immediate
	 * inter-procedural leaf predecessors of the
	 * enclosing parallel construct are returned.
	 *
	 * @param phasePoint
	 *
	 * @return
	 */
	public Set<NodeWithStack> getParallelConstructFreeInterProceduralLeafPredecessors(CallStack callStack) {
		Node baseNode = this.getOwner();
		if (baseNode instanceof EndNode) {
			EndNode endNode = (EndNode) baseNode;
			if (endNode.getParent() instanceof ParallelConstruct) {
				ParallelConstruct parConstruct = (ParallelConstruct) endNode.getParent();
				return parConstruct.getInfo().getCFGInfo().getInterProceduralLeafPredecessors(callStack);
			}
		}
		// Do not move outside the enclosing ParallelConstruct.
		if (baseNode instanceof BeginNode) {
			BeginNode beginNode = (BeginNode) baseNode;
			if (beginNode.getParent() instanceof ParallelConstruct) {
				return new HashSet<>();
			}
		}
		return this.getInterProceduralLeafPredecessors(callStack);
	}

	/**
	 * Returns a set of all the CFG nodes that are present within the owner node,
	 * lexically.
	 *
	 * @return a set of all the CFG nodes that are present within the owner node,
	 *         lexically.
	 */
	public Set<Node> getLexicalCFGContents() {
		AllCFGNodeGetter collector = new AllCFGNodeGetter();
		this.owner.accept(collector);
		return new HashSet<>(collector.cfgNodeList);
	}

	/**
	 * Returns a set of all the leaf CFG nodes that are present within the owner
	 * node, lexically.
	 *
	 * @return a set of all the leaf CFG nodes that are present within the owner
	 *         node, lexically.
	 */
	public Set<Node> getLexicalCFGLeafContents() {
		Set<Node> returnSet = new HashSet<>();
		for (Node n : this.getLexicalCFGContents()) {
			if (Misc.isCFGLeafNode(n)) {
				returnSet.add(n);
			}
		}
		return returnSet;
	}

	public List<Node> getLexicalCFGContentsInPostOrder() {
		AllCFGNodeGetter collector = new AllCFGNodeGetter();
		this.owner.accept(collector);
		return collector.cfgNodeList;
	}

	public List<Node> getLexicalCFGLeafContentsInPostOrder() {
		List<Node> returnList = new ArrayList<>();
		for (Node n : this.getLexicalCFGContentsInPostOrder()) {
			if (Misc.isCFGLeafNode(n)) {
				returnList.add(n);
			}
		}
		return returnList;

	}

	/**
	 * Returns a set of all the leaf CFG nodes that are present on valid
	 * inter-procedural context-insensitive paths
	 * between the BeginNode and EndNode of the owner node, if owner node is a
	 * non-leaf node. Otherwise, this method
	 * returns a set containing just the owner node.
	 * <br>
	 * NOTE: This method DOES NOT traverse on reachable paths that originate from
	 * internal jumps and move outside the
	 * node.
	 *
	 * @return set of all the leaf CFG nodes that are present in the valid
	 *         inter-procedural paths.
	 */
	public Set<Node> getIntraTaskCFGLeafContents() {
		Set<Node> returnSet = new HashSet<>();
		if (Misc.isCFGLeafNode(this.getOwner())) {
			returnSet.add(this.getOwner());
			return returnSet;
		}
		for (Node n : this.getLexicalCFGContents()) {
			if (!Misc.isCFGLeafNode(n)) {
				continue; // Note that internal leaves are already present in iterator.
			} else {
				returnSet.add(n);
				if (n instanceof PreCallNode) {
					CallStatement callStmt = (CallStatement) n.getParent();
					BeginNode beginNode = callStmt.getInfo().getCFGInfo().getNestedCFG().getBegin();
					EndNode endNode = callStmt.getInfo().getCFGInfo().getNestedCFG().getEnd();
					returnSet.add(beginNode);
					Set<Node> endPoints = new HashSet<>();
					returnSet.addAll(CollectorVisitor.collectNodesIntraTaskForward(beginNode, endPoints,
							node -> node == endNode));
					returnSet.addAll(endPoints);
				}
			}

		}
		return returnSet;
	}

	/**
	 * Obtain a list of successor leaf nodes, while preserving the order present in
	 * block-level successors (if they are
	 * present in that list).
	 *
	 * @return list of successor leaf nodes, in order in which they are present in
	 *         the block-level successors.
	 */
	public List<Node> getInterTaskLeafSuccessorList() {
		Node node = this.getOwner();
		if (node instanceof DummyFlushDirective) {
			return new ArrayList<>(this.getInterTaskLeafSuccessorNodes(SVEDimension.SVE_INSENSITIVE));
		} else if (node instanceof PreCallNode
				|| (node instanceof EndNode && ((EndNode) node).getParent() instanceof FunctionDefinition)) {
			return new ArrayList<>(this.getInterProceduralLeafSuccessors());
		} else {
			return this.getLeafSuccessors();
		}
	}

	/**
	 * Obtain a list of predecessor leaf nodes, while preserving the order present
	 * in block-level predecessors (if they
	 * are present in that list).
	 *
	 * @return list of predecessor leaf nodes, in order in which they are present in
	 *         the block-level predecessors.
	 */
	public List<NodeWithStack> getInterTaskLeafPredecessorList(CallStack callStack) {
		Node node = this.getOwner();
		if (node instanceof DummyFlushDirective) {
			return new ArrayList<>(this.getInterTaskLeafPredecessorNodes(callStack));
		} else if (node instanceof PostCallNode
				|| (node instanceof BeginNode && ((BeginNode) node).getParent() instanceof FunctionDefinition)) {
			return new ArrayList<>(this.getInterProceduralLeafPredecessors(callStack));
		} else {
			return this.getLeafPredecessors(callStack);
		}
	}

	/**
	 * Returns a set of all the leaf CFG nodes that are present on valid
	 * inter-procedural context-insensitive paths
	 * between the BeginNode and EndNode of the owner node, if owner node is a
	 * non-leaf node. Otherwise, this method
	 * returns a set containing just the owner node.
	 * <br>
	 * NOTE: This method traverses on all reachable paths (even those that originate
	 * from internal jumps and move
	 * outside the node).
	 *
	 * @return set of all the leaf CFG nodes that are present in the valid
	 *         inter-procedural paths.
	 *
	 * @See {@link #getIntraTaskCFGLeafContents()}
	 */
	public Set<Node> getReachableIntraTaskCFGLeafContents() {
		Set<Node> returnSet = new HashSet<>();
		// Old code: Commented to remain consistent with the semantics of a
		// CallStatement.
		// if (Misc.isACallStatement(this.node)) {
		// returnSet.add(node);
		// CallStatement callStmt = Misc.getCallStatement(this.node);
		// for (FunctionDefinition target : callStmt.getCalleeDefinitions()) {
		// returnSet.addAll(target.getInfo().getIntraTaskContextInsensitiveCFGLeafContents());
		// }
		// } else
		if (Misc.isCFGLeafNode(this.owner)) {
			returnSet.add(owner);
			return returnSet;
		} else {
			// New Code, which is yet incomplete.
			// returnSet.addAll(this.owner.getInfo().getCFGInfo().getLexicalIntraProceduralCFGLeafContents());
			// Set<Node> others = new HashSet<>();
			// for (CallStatement callStmt : this.owner.getInfo().getCallStatements()) {
			//
			// }
			// returnSet.addAll(others);
			// others.clear();
			// for (Node inJumps : this.owner.getInfo().getInJumpSources()) {
			//
			// }
			// returnSet.addAll(others);
			// others.clear();
			// for (Node outJumps : this.owner.getInfo().getOutJumpDestinations()) {
			//
			// }
			// returnSet.addAll(others);
			// others.clear();

			/*
			 * Old code: (Version 2) Commented since this reachability based one
			 * provides with very imprecise sets in case of recursion.
			 */
			BeginNode beginNode = this.getNestedCFG().getBegin();
			EndNode endNode = this.getNestedCFG().getEnd();
			returnSet.add(beginNode);
			Set<Node> endPoints = new HashSet<>();
			returnSet.addAll(CollectorVisitor.collectNodesIntraTaskForward(beginNode, endPoints, n -> n == endNode));
			returnSet.addAll(endPoints);
			returnSet.add(endNode);
			return returnSet;
		}
	}

	/**
	 * Returns a set of all the leaf CFG nodes that are present on valid
	 * inter-procedural context-sensitive paths
	 * between the BeginNode and EndNode of the owner node, if owner node is a
	 * non-leaf node. Otherwise, this method
	 * returns a set containing just the owner node.
	 *
	 * @return set of all the leaf CFG nodes that are present in the valid
	 *         inter-procedural paths.
	 */
	public Set<NodeWithStack> getIntraTaskCFGLeafContents(CallStack callStack) {
		Set<NodeWithStack> returnSet = new HashSet<>();
		if (Misc.isCFGLeafNode(this.owner)) {
			returnSet.add(new NodeWithStack(this.owner, callStack));
		} else {
			NodeWithStack beginNodeWithStack = new NodeWithStack(
					this.owner.getInfo().getCFGInfo().getNestedCFG().getBegin(), callStack);
			Set<NodeWithStack> endNodeWithStackSet = new HashSet<>();
			Node endNode = this.owner.getInfo().getCFGInfo().getNestedCFG().getEnd();
			returnSet.add(beginNodeWithStack);
			returnSet.addAll(CollectorVisitor.collectNodesIntraTaskForwardContextSensitive(beginNodeWithStack,
					endNodeWithStackSet, n -> n.getNode() == endNode));
			returnSet.addAll(endNodeWithStackSet);
		}
		return returnSet;
	}

	public Set<NodeWithStack> getIntraTaskCFGLeafContentsOfSameParLevel() {
		return this.getIntraTaskCFGLeafContentsOfSameParLevel(new CallStack());
	}

	public Set<NodeWithStack> getIntraTaskCFGLeafContentsOfSameParLevel(CallStack callStack) {
		Set<NodeWithStack> returnSet = new HashSet<>();
		if (Misc.isCFGLeafNode(this.owner)) {
			returnSet.add(new NodeWithStack(this.owner, callStack));
		} else {
			NodeWithStack beginNodeWithStack = new NodeWithStack(
					this.owner.getInfo().getCFGInfo().getNestedCFG().getBegin(), callStack);
			Set<NodeWithStack> endNodeWithStackSet = new HashSet<>();
			Node endNode = this.owner.getInfo().getCFGInfo().getNestedCFG().getEnd();
			returnSet.add(beginNodeWithStack);
			returnSet.addAll(CollectorVisitor.collectNodesIntraTaskForwardOfSameParLevel(beginNodeWithStack,
					endNodeWithStackSet, n -> n.getNode() == endNode));
			returnSet.addAll(endNodeWithStackSet);
		}
		return returnSet;
	}

	/**
	 * From among the lexical leaf contents of this node, this method returns a
	 * subset of those leaf CFG nodes from
	 * which BeginNode of this node is not reachable, while moving backwards.
	 * <br>
	 * Note: To obtain the complete set, we should perform a simple backward
	 * traversal from the returned set, within the
	 * given node.
	 * <br>
	 * While this method's counterpart {@link #getEndUnreachableLeafHeaders()}} is
	 * useful, one might wish to use {@link
	 * NodeInfo#getInJumpDestinations()} instead of this method.
	 *
	 * @return subset of those leaf CFG nodes from which BeginNode of this node is
	 *         unreachable.
	 */
	public Set<Node> getBeginUnreachableLeafHeaders() {
		Set<Node> returnSet = new HashSet<>();
		Set<Node> lexicalContents = this.getLexicalCFGLeafContents();
		BeginNode beginNode = this.getNestedCFG().getBegin();
		Set<Node> beginReachables = CollectorVisitor.collectNodeSetInGenericGraph(beginNode, new HashSet<>(),
				n -> false, n -> n.getInfo().getCFGInfo().getLeafSuccessors().stream().filter(succ -> {
					return lexicalContents.contains(succ);
				}).collect(Collectors.toCollection(HashSet::new)));
		for (Node b : beginReachables) {
			for (Node bPred : b.getInfo().getCFGInfo().getLeafPredecessors()) {
				if (bPred == beginNode) {
					continue;
				}
				if (!lexicalContents.contains(bPred)) {
					continue;
				} else if (!beginReachables.contains(bPred)) {
					returnSet.add(bPred);
				}
			}
		}
		return returnSet;
	}

	/**
	 * From among the lexical leaf contents of this node, this method returns a
	 * subset (headers of the paths) of those
	 * leaf CFG nodes from which EndNode of this node is not reachable.
	 * <br>
	 * Note: To obtain the complete set, we should perform a simple forward
	 * traversal from the returned set, within the
	 * given node.
	 *
	 * @return subset of those leaf CFG nodes from which EndNode of this node is
	 *         unreachable.
	 */
	public Set<Node> getEndUnreachableLeafHeaders() {
		Set<Node> returnSet = new HashSet<>();
		Set<Node> lexicalContents = this.getLexicalCFGLeafContents();
		EndNode endNode = this.getNestedCFG().getEnd();
		Set<Node> endReachables = CollectorVisitor.collectNodeSetInGenericGraph(endNode, new HashSet<>(), n -> false,
				n -> n.getInfo().getCFGInfo().getLeafPredecessors().stream().filter(pred -> {
					return lexicalContents.contains(pred);
				}).collect(Collectors.toCollection(HashSet::new)));
		for (Node e : endReachables) {
			for (Node eSucc : e.getInfo().getCFGInfo().getLeafSuccessors()) {
				if (eSucc == endNode) {
					continue;
				}
				if (!lexicalContents.contains(eSucc)) {
					continue;
				} else if (!endReachables.contains(eSucc)) {
					returnSet.add(eSucc);
				}
			}
		}
		return returnSet;
	}

	public Set<Node> getBothBeginAndEndReachableIntraTaskLeafNodes() {
		Set<Node> returnSet = new HashSet<>();
		Set<Node> lexicalContents = this.getLexicalCFGLeafContents();
		EndNode endNode = this.getNestedCFG().getEnd();
		Set<Node> endReachables = CollectorVisitor.collectNodeSetInGenericGraph(endNode, new HashSet<>(), n -> false,
				n -> n.getInfo().getCFGInfo().getLeafPredecessors().stream().filter(pred -> {
					return lexicalContents.contains(pred);
				}).collect(Collectors.toCollection(HashSet::new)));
		BeginNode beginNode = this.getNestedCFG().getBegin();
		Set<Node> beginReachables = CollectorVisitor.collectNodeSetInGenericGraph(beginNode, new HashSet<>(),
				n -> false, n -> n.getInfo().getCFGInfo().getLeafSuccessors().stream().filter(succ -> {
					return lexicalContents.contains(succ);
				}).collect(Collectors.toCollection(HashSet::new)));
		/*
		 * Add all nodes lexically inside this node, reachable backwards from
		 * EndNode and forward from BeginNode, to the returnSet.
		 */
		returnSet.addAll(Misc.setIntersection(endReachables, beginReachables));
		/*
		 * Now, add all nodes from call-statements in reachable nodes.
		 */
		for (Node r : new HashSet<>(returnSet)) {
			if (r instanceof PreCallNode) {
				CallStatement callStmt = (CallStatement) r.getParent();
				returnSet.addAll(callStmt.getInfo().getCFGInfo().getIntraTaskCFGLeafContents());
			}
		}
		return returnSet;
	}

	/**
	 * Returns a set of all the CFG leaf nodes that are reachable starting the
	 * immediate successors of this node, on
	 * valid paths.
	 * <p>
	 * Note that when called on a non-leaf node, this traversal will not traverse
	 * inside the non-leaf node, but would
	 * start with its same nesting level successors.
	 *
	 * @return a set of all the CFG leaf nodes that are reachable starting the
	 *         immediate successors of this node, on
	 *         valid paths.
	 */
	public Set<Node> getIntraTaskCFGLeafReachablesForward() {
		return CollectorVisitor.collectNodesIntraTaskForward(this.owner, new HashSet<>(), n -> false);
	}

	/**
	 * Returns a set of all the CFG leaf nodes that are reachable backwards starting
	 * with the immediate predecessors of
	 * this node, on valid paths.
	 * <p>
	 * Note that when called on a non-leaf node, this traversal will not traverse
	 * inside the non-leaf node, but would
	 * start with its same nesting level successors.
	 *
	 * @return a set of all the CFG leaf nodes that are reachable backwards starting
	 *         with the immediate predecessors of
	 *         this node, on valid paths.
	 */
	public Set<Node> getIntraTaskCFGLeafReachablesBackward() {
		return CollectorVisitor.collectNodesIntraTaskBackward(this.owner, new HashSet<>(), n -> false);
	}

	/**
	 * Obtain a set of predecessor node edges for this {@code node}, along with
	 * symbols via which information may flow
	 * on the edge connecting the predecessors to this {@code node}.
	 *
	 * @param node
	 *             node whose predecessors have to be found.
	 *
	 * @return a set of predecessor node edges for this {@code node}, along with
	 *         symbols via which information may flow
	 *         on the edge connecting the predecessors to this {@code node}.
	 */
	public Set<IDFAEdge> getInterTaskLeafPredecessorEdges() {
		return this.getInterTaskLeafPredecessorEdgesForVariables(null, Program.sveSensitive);
	}

	public Set<IDFAEdge> getInterTaskLeafPredecessorEdges(SVEDimension sveDimension) {
		return this.getInterTaskLeafPredecessorEdgesForVariables(null, sveDimension);
	}

	/**
	 * Obtain a set of predecessor node edges for this {@code node}, along with
	 * symbols via which information may flow
	 * on the edge connecting the predecessors to this {@code node}; for variables
	 * in {@code set}.
	 *
	 * @param node
	 *             node whose predecessors have to be found.
	 * @param set
	 *             set of cells for which communication edges have to be considered;
	 *             if this set is {@code null}, we assume
	 *             it to be a global set.
	 *
	 * @return a set of predecessor node edges for this {@code node}, along with
	 *         symbols via which information may flow
	 *         on the edge connecting the predecessors to this {@code node}.
	 */
	public Set<IDFAEdge> getInterTaskLeafPredecessorEdgesForVariables(CellSet set, SVEDimension sveDimension) {
		Node node = this.owner;
		Set<IDFAEdge> predecessors;
		predecessors = new HashSet<>();
		for (Node predNode : node.getInfo().getCFGInfo().getInterProceduralLeafPredecessors()) {
			predecessors.add(new IDFAEdge(predNode, null));
		}
		if (node instanceof DummyFlushDirective) {
			DummyFlushDirective headFlush = (DummyFlushDirective) node;
			for (InterTaskEdge interTaskEdge : headFlush.getInfo().getIncomingInterTaskEdges()) {
				DummyFlushDirective tailFlush = interTaskEdge.getSourceNode();
				CellSet communicationCells = CFGInfo.getCommunicationVariables(tailFlush, headFlush);
				if (communicationCells.isEmpty()) {
					continue;
				} else if (set != null && !Misc.doIntersect(set, communicationCells)) {
					continue;
				} else {
					if (Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YUANMHP
							&& sveDimension == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInAnyPhase(tailFlush, headFlush)) {
						continue;
					}
					predecessors.add(new IDFAEdge(tailFlush, communicationCells));
				}
			}
		}
		return predecessors;
	}

	/**
	 * Obtain a set of predecessor node edges for this {@code node}, along with
	 * symbols via which information may flow
	 * on the edge connecting the predecessors to this {@code node}.
	 *
	 * @param node
	 *             node whose predecessors have to be found.
	 *
	 * @return a set of predecessor node edges for this {@code node}, along with
	 *         symbols via which information may flow
	 *         on the edge connecting the predecessors to this {@code node}.
	 */
	public Set<Node> getInterTaskLeafPredecessorNodes() {
		return this.getInterTaskLeafPredecessorNodesForVariables(null, Program.sveSensitive);
	}

	public Set<Node> getInterTaskLeafPredecessorNodes(SVEDimension sveDimension) {
		return this.getInterTaskLeafPredecessorNodesForVariables(null, sveDimension);
	}

	/**
	 * Obtain a set of predecessor node edges for this {@code node}, along with
	 * symbols via which information may flow
	 * on the edge connecting the predecessors to this {@code node}; for variables
	 * in {@code set}.
	 *
	 * @param node
	 *             node whose predecessors have to be found.
	 * @param set
	 *             set of cells for which communication edges have to be considered;
	 *             if this set is {@code null}, we assume
	 *             it to be a global set.
	 *
	 * @return a set of predecessor node edges for this {@code node}, along with
	 *         symbols via which information may flow
	 *         on the edge connecting the predecessors to this {@code node}.
	 */
	public Set<Node> getInterTaskLeafPredecessorNodesForVariables(CellSet set, SVEDimension sveDimension) {
		Set<Node> returnSet = new HashSet<>();
		for (IDFAEdge edge : this.getInterTaskLeafPredecessorEdgesForVariables(set, sveDimension)) {
			returnSet.add(edge.getNode());
		}
		return returnSet;
	}

	/**
	 * Obtain a set of successor node edges for this {@code node}, along with
	 * symbols via which information may flow on
	 * the edge connecting the successors to this {@code node}.
	 *
	 * @param node
	 *             node whose successors have to be found.
	 *
	 * @return a set of successor node edges for this {@code node}, along with
	 *         symbols via which information may flow on
	 *         the edge connecting the successors to this {@code node}.
	 */
	public Set<IDFAEdge> getInterTaskLeafSuccessorEdges() {
		return this.getInterTaskLeafSuccessorEdgesForVariables(null, Program.sveSensitive);
	}

	public Set<IDFAEdge> getInterTaskLeafSuccessorEdges(SVEDimension sveDimension) {
		return this.getInterTaskLeafSuccessorEdgesForVariables(null, sveDimension);
	}

	/**
	 * Obtain a set of successor node edges for this {@code node}, along with
	 * symbols via which information may flow on
	 * the edge connecting the successors to this {@code node}; for variables in
	 * {@code set}.
	 *
	 * @param node
	 *             node whose successors have to be found.
	 * @param set
	 *             set of cells for which communication edges have to be considered;
	 *             if this set is {@code null}, we assume
	 *             it to be a global set.
	 *
	 * @return a set of successor node edges for this {@code node}, along with
	 *         symbols via which information may flow on
	 *         the edge connecting the successors to this {@code node}.
	 */
	public Set<IDFAEdge> getInterTaskLeafSuccessorEdgesForVariables(CellSet set, SVEDimension sveDimension) {
		Node node = this.owner;
		Set<IDFAEdge> successors;
		successors = new HashSet<>();
		for (Node succNode : node.getInfo().getCFGInfo().getInterProceduralLeafSuccessors()) {
			successors.add(new IDFAEdge(succNode, null));
		}
		if (node instanceof DummyFlushDirective) {
			DummyFlushDirective tailFlush = (DummyFlushDirective) node;
			for (InterTaskEdge interTaskEdge : tailFlush.getInfo().getOutgoingInterTaskEdges()) {
				DummyFlushDirective headFlush = interTaskEdge.getDestinationNode();
				CellSet communicationCells = CFGInfo.getCommunicationVariables(tailFlush, headFlush);
				if (communicationCells.isEmpty()) {
					continue;
				} else if (set != null && !Misc.doIntersect(set, communicationCells)) {
					continue;
				} else {
					if (Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YUANMHP
							&& sveDimension == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInAnyPhase(tailFlush, headFlush)) {
						continue;
					}
					successors.add(new IDFAEdge(headFlush, communicationCells));
				}
			}
		}
		return successors;
	}

	/**
	 * Obtain a set of successor node edges for this {@code node}, along with
	 * symbols via which information may flow on
	 * the edge connecting the successors to this {@code node}.
	 *
	 * @param node
	 *             node whose successors have to be found.
	 *
	 * @return a set of successor node edges for this {@code node}, along with
	 *         symbols via which information may flow on
	 *         the edge connecting the successors to this {@code node}.
	 */
	public Set<Node> getInterTaskLeafSuccessorNodes() {
		return this.getInterTaskLeafSuccessorNodesForVariables(null, Program.sveSensitive);
	}

	public Set<Node> getInterTaskLeafSuccessorNodes(SVEDimension sveDimension) {
		return this.getInterTaskLeafSuccessorNodesForVariables(null, sveDimension);
	}

	/**
	 * Obtain a set of successor node edges for this {@code node}, along with
	 * symbols via which information may flow on
	 * the edge connecting the successors to this {@code node}; for variables in
	 * {@code set}.
	 *
	 * @param node
	 *             node whose successors have to be found.
	 * @param set
	 *             set of cells for which communication edges have to be considered;
	 *             if this set is {@code null}, we assume
	 *             it to be a global set.
	 *
	 * @return a set of successor node edges for this {@code node}, along with
	 *         symbols via which information may flow on
	 *         the edge connecting the successors to this {@code node}.
	 */
	public Set<Node> getInterTaskLeafSuccessorNodesForVariables(CellSet set, SVEDimension sveDimension) {
		Set<Node> returnSet = new HashSet<>();
		for (IDFAEdge edge : this.getInterTaskLeafSuccessorEdgesForVariables(set, sveDimension)) {
			returnSet.add(edge.getNode());
		}
		return returnSet;
	}

	/**
	 * Obtain a set of predecessor node edges for this {@code node}, along with
	 * symbols via which information may flow
	 * on the edge connecting the predecessors to this {@code node}.
	 *
	 * @param node
	 *             node whose predecessors have to be found.
	 *
	 * @return a set of predecessor node edges for this {@code node}, along with
	 *         symbols via which information may flow
	 *         on the edge connecting the predecessors to this {@code node}.
	 */
	public Set<IDFAEdgeWithStack> getInterTaskLeafPredecessorEdges(CallStack callStack) {
		return this.getInterTaskLeafPredecessorEdgesForVariables(callStack, null, Program.sveSensitive);
	}

	public Set<IDFAEdgeWithStack> getInterTaskLeafPredecessorEdges(CallStack callStack, SVEDimension sveDimension) {
		return this.getInterTaskLeafPredecessorEdgesForVariables(callStack, null, sveDimension);
	}

	/**
	 * Obtain a set of predecessor node edges for this {@code node}, along with
	 * symbols via which information may flow
	 * on the edge connecting the predecessors to this {@code node}; for variables
	 * in {@code set}.
	 *
	 * @param node
	 *             node whosae predecessors have to be found.
	 *
	 * @return a set of predecessor node edges for this {@code node}, along with
	 *         symbols via which information may flow
	 *         on the edge connecting the predecessors to this {@code node}.
	 */
	public Set<IDFAEdgeWithStack> getInterTaskLeafPredecessorEdgesForVariables(CallStack callStack, CellSet set,
			SVEDimension sveDimension) {
		Node node = this.owner;
		Set<IDFAEdgeWithStack> predecessors;
		predecessors = new HashSet<>();
		for (NodeWithStack predNode : node.getInfo().getCFGInfo().getInterProceduralLeafPredecessors(callStack)) {
			predecessors.add(new IDFAEdgeWithStack(predNode, null));
		}
		if (node instanceof DummyFlushDirective) {
			DummyFlushDirective headFlush = (DummyFlushDirective) node;
			for (InterTaskEdge interTaskEdge : headFlush.getInfo().getIncomingInterTaskEdges()) {
				DummyFlushDirective tailFlush = interTaskEdge.getSourceNode();
				CellSet communicationCells = CFGInfo.getCommunicationVariables(tailFlush, headFlush);
				if (communicationCells.isEmpty()) {
					continue;
				} else if (set != null && !Misc.doIntersect(set, communicationCells)) {
					continue;
				} else {
					if (Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YUANMHP
							&& sveDimension == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInAnyPhase(tailFlush, headFlush)) {
						continue;
					}
					predecessors.add(
							new IDFAEdgeWithStack(new NodeWithStack(tailFlush, new CallStack()), communicationCells));
				}
			}
		}
		return predecessors;
	}

	/**
	 * Obtain a set of predecessor node edges for this {@code node}, along with
	 * symbols via which information may flow
	 * on the edge connecting the predecessors to this {@code node}.
	 *
	 * @param node
	 *             node whose predecessors have to be found.
	 *
	 * @return a set of predecessor node edges for this {@code node}, along with
	 *         symbols via which information may flow
	 *         on the edge connecting the predecessors to this {@code node}.
	 */
	public Set<NodeWithStack> getInterTaskLeafPredecessorNodes(CallStack callStack) {
		return this.getInterTaskLeafPredecessorNodesForVariables(callStack, null, Program.sveSensitive);
	}

	public Set<NodeWithStack> getInterTaskLeafPredecessorNodes(CallStack callStack, SVEDimension sveDimension) {
		return this.getInterTaskLeafPredecessorNodesForVariables(callStack, null, sveDimension);
	}

	/**
	 * Obtain a set of predecessor node edges for this {@code node}, along with
	 * symbols via which information may flow
	 * on the edge connecting the predecessors to this {@code node}; for variables
	 * in {@code set}.
	 *
	 * @param node
	 *             node whose predecessors h
	 * @param set
	 *             set of cells for which communication edges have to be considered;
	 *             if this set is {@code null}, we assume
	 *             it to be a global set. ve to be found.
	 *
	 * @return a set of predecessor node edges for this {@code node}, along with
	 *         symbols via which information may flow
	 *         on the edge connecting the predecessors to this {@code node}.
	 */
	public Set<NodeWithStack> getInterTaskLeafPredecessorNodesForVariables(CallStack callStack, CellSet set,
			SVEDimension sveDimension) {
		Set<NodeWithStack> returnSet = new HashSet<>();
		for (IDFAEdgeWithStack edgeWithStack : this.getInterTaskLeafPredecessorEdgesForVariables(callStack, set,
				sveDimension)) {
			returnSet.add(edgeWithStack.getNodeWithStack());
		}
		return returnSet;
	}

	/**
	 * Obtain a set of successor node edges for this {@code node}, along with
	 * symbols via which information may flow on
	 * the edge connecting the successors to this {@code node}.
	 *
	 * @param node
	 *             node whose successors have to be found.
	 *
	 * @return a set of successor node edges for this {@code node}, along with
	 *         symbols via which information may flow on
	 *         the edge connecting the successors to this {@code node}.
	 */
	public Set<IDFAEdgeWithStack> getInterTaskLeafSuccessorEdges(CallStack callStack) {
		return this.getInterTaskLeafSuccessorEdgesForVariables(callStack, null, Program.sveSensitive);
	}

	public Set<IDFAEdgeWithStack> getInterTaskLeafSuccessorEdges(CallStack callStack, SVEDimension sveDimension) {
		return this.getInterTaskLeafSuccessorEdgesForVariables(callStack, null, sveDimension);
	}

	/**
	 * Obtain a set of successor node edges for this {@code node}, along with
	 * symbols via which information may flow on
	 * the edge connecting the successors to this {@code node}; for variables in
	 * {@code set}.
	 *
	 * @param node
	 *             node whose successors have to be found.
	 * @param set
	 *             set of cells for which communication edges have to be considered;
	 *             if this set is {@code null}, we assume
	 *             it to be a global set.
	 *
	 * @return a set of successor node edges for this {@code node}, along with
	 *         symbols via which information may flow on
	 *         the edge connecting the successors to this {@code node}.
	 */
	public Set<IDFAEdgeWithStack> getInterTaskLeafSuccessorEdgesForVariables(CallStack callStack, CellSet set,
			SVEDimension sveDimension) {
		Node node = this.owner;
		Set<IDFAEdgeWithStack> successors;
		successors = new HashSet<>();
		for (NodeWithStack succNode : node.getInfo().getCFGInfo().getInterProceduralLeafSuccessors(callStack)) {
			successors.add(new IDFAEdgeWithStack(succNode, null));
		}
		if (node instanceof DummyFlushDirective) {
			DummyFlushDirective tailFlush = (DummyFlushDirective) node;
			for (InterTaskEdge interTaskEdge : tailFlush.getInfo().getOutgoingInterTaskEdges()) {
				DummyFlushDirective headFlush = interTaskEdge.getDestinationNode();
				CellSet communicationCells = CFGInfo.getCommunicationVariables(tailFlush, headFlush);
				if (communicationCells.isEmpty()) {
					continue;
				} else if (set != null && !Misc.doIntersect(set, communicationCells)) {
					continue;
				} else {
					if (Program.concurrencyAlgorithm != Program.ConcurrencyAlgorithm.YUANMHP
							&& sveDimension == SVEDimension.SVE_SENSITIVE
							&& !CoExistenceChecker.canCoExistInAnyPhase(tailFlush, headFlush)) {
						continue;
					}
					successors.add(
							new IDFAEdgeWithStack(new NodeWithStack(headFlush, new CallStack()), communicationCells));
				}
			}
		}
		return successors;
	}

	/**
	 * Obtain a set of successor node edges for this {@code node}, along with
	 * symbols via which information may flow on
	 * the edge connecting the successors to this {@code node}.
	 *
	 * @param node
	 *             node whose successors have to be found.
	 *
	 * @return a set of successor node edges for this {@code node}, along with
	 *         symbols via which information may flow on
	 *         the edge connecting the successors to this {@code node}.
	 */
	public Set<NodeWithStack> getInterTaskLeafSuccessorNodes(CallStack callStack) {
		return this.getInterTaskLeafSuccessorNodesForVariables(callStack, null, Program.sveSensitive);
	}

	public Set<NodeWithStack> getInterTaskLeafSuccessorNodes(CallStack callStack, SVEDimension sveDimension) {
		return this.getInterTaskLeafSuccessorNodesForVariables(callStack, null, sveDimension);
	}

	/**
	 * Obtain a set of successor node edges for this {@code node}, along with
	 * symbols via which information may flow on
	 * the edge connecting the successors to this {@code node}; for variables in
	 * {@code set}.
	 *
	 * @param node
	 *             node whose successors have to be found.
	 * @param set
	 *             set of cells for which communication edges have to be considered;
	 *             if this set is {@code null}, we assume
	 *             it to be a global set.
	 *
	 * @return a set of successor node edges for this {@code node}, along with
	 *         symbols via which information may flow on
	 *         the edge connecting the successors to this {@code node}.
	 */
	public Set<NodeWithStack> getInterTaskLeafSuccessorNodesForVariables(CallStack callStack, CellSet set,
			SVEDimension sveDimension) {
		Set<NodeWithStack> returnSet = new HashSet<>();
		for (IDFAEdgeWithStack edge : this.getInterTaskLeafSuccessorEdgesForVariables(callStack, set, sveDimension)) {
			returnSet.add(edge.getNodeWithStack());
		}
		return returnSet;
	}

	public static CellSet getCommunicationVariables(DummyFlushDirective tailFlush, DummyFlushDirective headFlush) {
		CellSet commVars = new CellSet();

		if (Program.memoizeAccesses > 0) {
			/*
			 * This scenario implies that during the processing of PTA information,
			 * there was an attempt to obtain precise inter-task edges.
			 * Note that such precision, in turn, may require points-to analysis.
			 * Do handle such recursive issue, we simply return a set with global cell in
			 * it.
			 * This would give us a conservatively correct set of communication variables.
			 * NOTE: Extra inter-task edges would not negatively impact the precision,
			 * but efficiency, of the PTA.
			 */
			commVars.add(Cell.genericCell);
			return commVars;
		}
		// Backup code: For when things get too slow.
		// if (1 < 2) {
		// commVars.addAll(Misc.setIntersection(tailFlush.getWrittenBeforeCells(),
		// headFlush.getReadAfterCells()));
		// return commVars;
		// }

		/*
		 * Heuristic for an early return.
		 */
		if (!Misc.doIntersect(tailFlush.getWrittenBeforeCells(), headFlush.getReadAfterCells())) {
			return commVars;
		}

		/*
		 * The actual check.
		 */
		Set<Node> aboveNodes = tailFlush.getWrittenBeforeNodes();
		Set<Node> belowNodes = headFlush.getReadAfterNodes();
		Set<Node> aboveUni = tailFlush.getUniLeavesAbove();
		Set<Node> belowUni = headFlush.getUniLeavesBelow();
		Set<Node> commonUniNodes = new HashSet<>();
		for (Node n : aboveUni) {
			if (belowUni.contains(n)) {
				commonUniNodes.add(n);
			}
		}

		if (commonUniNodes.isEmpty()) {
			CellSet writtenAbove = tailFlush.getWrittenBeforeCells();
			CellSet readBelow = headFlush.getReadAfterCells();
			CellSet common = Misc.setIntersection(writtenAbove, readBelow);
			return common;
		}

		long thisUniPrecisionTimer = System.nanoTime();

		CellSet writtenInCommon = new CellSet();
		CellSet readInCommon = new CellSet();
		for (Node n : commonUniNodes) {
			writtenInCommon.addAll(n.getInfo().getSharedWrites());
			readInCommon.addAll(n.getInfo().getSharedReads());
		}
		CellSet writtenInAboveDiff = new CellSet();
		for (Node n : aboveNodes) {
			if (commonUniNodes.contains(n)) {
				continue;
			}
			writtenInAboveDiff.addAll(n.getInfo().getSharedWrites());
		}

		CellSet readInBelowDiff = new CellSet();
		for (Node n : belowNodes) {
			if (commonUniNodes.contains(n)) {
				continue;
			}
			readInBelowDiff.addAll(n.getInfo().getSharedReads());
		}

		// A-U with B-U
		commVars.addAll(Misc.setIntersection(writtenInAboveDiff, readInBelowDiff));

		// U with B-U
		commVars.addAll(Misc.setIntersection(writtenInCommon, readInBelowDiff));

		// A-U with U
		commVars.addAll(Misc.setIntersection(writtenInAboveDiff, readInCommon));

		// For all pairs (x, y) in U, such that x.binder != y.binder
		Set<Node> beginNodes = commonUniNodes.stream()
				.filter(n -> n instanceof BeginNode
						&& (n.getParent() instanceof MasterConstruct || n.getParent() instanceof SingleConstruct))
				.collect(Collectors.toSet());
		for (Node x : beginNodes) {
			for (Node y : beginNodes) {
				if (x == y) {
					continue;
				}
				commVars.addAll(Misc.setIntersection(x.getParent().getInfo().getSharedWrites(),
						y.getParent().getInfo().getSharedReads()));
				commVars.addAll(Misc.setIntersection(y.getParent().getInfo().getSharedWrites(),
						x.getParent().getInfo().getSharedReads()));
			}
		}

		CFGInfo.uniPrecisionTimer += System.nanoTime() - thisUniPrecisionTimer;

		return commVars;
	}

	public List<Node> getExecutableLeafSuccessors() {
		Set<Node> visitedNodes = new HashSet<>();
		visitedNodes.add(this.getOwner());
		return this.getExecutableLeafSuccessors(visitedNodes);
	}

	public List<Node> getExecutableLeafSuccessors(Set<Node> visited) {
		Node node = this.getOwner();
		List<Node> executableLeafSuccessors = new ArrayList<>();
		for (Node n : node.getInfo().getCFGInfo().getLeafSuccessors()) {
			if (visited.contains(n)) {
				continue;
			} else {
				visited.add(n);
			}
			if (n instanceof BeginNode || n instanceof EndNode) {
				executableLeafSuccessors.addAll(n.getInfo().getCFGInfo().getExecutableLeafSuccessors(visited));
			} else {
				executableLeafSuccessors.add(n);
			}
		}

		return executableLeafSuccessors;
	}

	public List<Node> getExecutableLeafPredecessors() {
		Set<Node> visitedNodes = new HashSet<>();
		visitedNodes.add(this.getOwner());
		return this.getExecutableLeafPredecessors(visitedNodes);
	}

	public List<Node> getExecutableLeafPredecessors(Set<Node> visited) {
		Node node = this.getOwner();
		List<Node> executableLeafPredecessors = new ArrayList<>();
		for (Node n : node.getInfo().getCFGInfo().getLeafPredecessors()) {
			if (visited.contains(n)) {
				continue;
			} else {
				visited.add(n);
			}
			if (n instanceof BeginNode || n instanceof EndNode) {
				executableLeafPredecessors.addAll(n.getInfo().getCFGInfo().getExecutableLeafPredecessors(visited));
			} else {
				executableLeafPredecessors.add(n);
			}
		}
		return executableLeafPredecessors;
	}

	/**
	 * This method returns true if control entering to the node "via any point
	 * within the block" may flow out of the end
	 * of the node.
	 * <br>
	 * This method should not be called until the CFG edges have been created within
	 * <code>n</code>
	 *
	 * @return true, if starting the control-flow from the begin of the node node,
	 *         the end of the node node can be
	 *         reached.
	 */
	public boolean isEndReachable() {
		assert (Misc.isCFGNode(this.getOwner()))
				: "Note that " + this.getOwner().getClass().getSimpleName() + " is not a CFG node.";
		if (Misc.isCFGLeafNode(getOwner())) { // n is a leaf CFG node
			if (getOwner() instanceof JumpStatement) {
				return false;
			} else {
				return true;
			}
		} else {
			/*
			 * If the end node of the non-leaf node n has any predecessor, we
			 * assume that there must have
			 * been a path from the begin of n to the end.
			 */

			Node endNode = this.getNestedCFG().getEnd();
			if (endNode.getInfo().getCFGInfo().getPredBlocks().isEmpty()) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * This method should be overridden by all the subclasses of CFGInfo.<br>
	 * It should return the various CFG
	 * components of the {@code owner} node.<br>
	 * Note: This method should be made {@code abstract} in CFGInfo, after we
	 * ensure that each CFG element has its own CFGInfo subclass.
	 *
	 * @return CFG components of the {@code owner} node.
	 */
	public List<Node> getAllComponents() {
		return null;
	}

	public Node getOwner() {
		return owner;
	}

	public void setOwner(Node owner) {
		this.owner = owner;
	}

	public NestedCFG getNestedCFG() {
		ProfileSS.addRelevantChangePoint(ProfileSS.cfgSet);
		if (nestedCFG == null) {
			nestedCFG = new NestedCFG(getOwner());
		}
		return nestedCFG;
	}

	public void setNestedCFG(NestedCFG nestedCFG) {
		this.nestedCFG = nestedCFG;
	}

	public CFGInfo getCopy(Node newNode) {
		CFGInfo returnCFGInfo = new CFGInfo(newNode);
		if (!this.getSuccBlocks().isEmpty()) {
			for (Node successor : this.getSuccBlocks()) {
				returnCFGInfo.getSuccBlocks().add(successor);
				successor.getInfo().getCFGInfo().getPredBlocks().add(returnCFGInfo.getOwner());
			}
			for (Node predecessor : this.getPredBlocks()) {
				returnCFGInfo.getPredBlocks().add(predecessor);
				predecessor.getInfo().getCFGInfo().getSuccBlocks().add(returnCFGInfo.getOwner());
			}
			returnCFGInfo.setNestedCFG(getNestedCFG().getCopy(newNode));
		}
		return returnCFGInfo;
	}

	/**
	 * Obtain the body, if any, of this node, if it is a non-leaf CFG node that
	 * contains a body.
	 *
	 * @return body, if any, of this node; otherwise {@code null}.
	 */
	public Statement getBody() {
		/*
		 * Note that all specific subclasses of CFGInfo that contain a body
		 * should override this method.
		 */
		return null;
	}

	/**
	 * Adds CFG edges from and to source and destination, respectively, if not
	 * already present.
	 *
	 * @param source
	 *                    source node, for the CFG edge.
	 * @param destination
	 *                    destination node, for the CFG edge.
	 */
	public static void connectAndAdjustEndReachability(Node source, Node destination) {
		if (source == null || destination == null) {
			return;
		}
		source = Misc.getCFGNodeFor(source);
		destination = Misc.getCFGNodeFor(destination);
		if (source == null || destination == null) {
			return;
		}

		if (!CFGGenerator.verifyEdgePrecision(source, destination)) {
			return;
		}

		List<Node> fromList = source.getInfo().getCFGInfo().getSuccBlocks();
		List<Node> toList = destination.getInfo().getCFGInfo().getPredBlocks();
		if (!fromList.contains(destination)) {
			fromList.add(destination);
			assert (!toList.contains(source));
			toList.add(source);
		} else {
			return;
		}

		if (toList.size() == 1) {
			if (destination instanceof EndNode) {
				// This implies that the enclosing node will become end-reachable due
				// to the addition of this edge. We should adjust end-reachability now.
				Node parent = destination.getParent();
				EndReachabilityAdjuster.updateEndReachabilityAddition(parent);
				// } else {
				/*
				 * OLD CODE: Now, since we allow edges among disconnected
				 * snippets for efficiency purposes, this method call below
				 * isn't required.
				 */
				// NextNodeJoiner.joinToNextNode(destination);
			}
		}

	}

	/**
	 * Removes CFG edges from and to source and destination, respectively. Note that
	 * this also triggers adjustment of
	 * EndReachability, if required.
	 *
	 * @param source
	 *                    source node, for the CFG edge.
	 * @param destination
	 *                    destination node, for the CFG edge.
	 */
	public static void disconnectAndAdjustEndReachability(Node source, Node destination) {
		if (source == null || destination == null) {
			return;
		}
		source = Misc.getCFGNodeFor(source);
		destination = Misc.getCFGNodeFor(destination);
		if (source == null || destination == null) {
			return;
		}

		List<Node> fromList = source.getInfo().getCFGInfo().getSuccBlocks();
		List<Node> toList = destination.getInfo().getCFGInfo().getPredBlocks();
		if (fromList.contains(destination)) {
			fromList.remove(destination);
			assert (toList.contains(source));
			toList.remove(source);
		} else {
			return;
		}

		/*
		 * Adjust end-reachability.
		 */
		if (toList.isEmpty()) {
			if (destination instanceof EndNode) {
				// This implies that the last-edge to an EndNode has been removed.
				// Now, we should call adjustment of the EndNode edges.
				Node parent = destination.getParent();
				EndReachabilityAdjuster.updateEndReachabilityRemoval(parent);
				// } else {
				/*
				 * OLD CODE: Now, since we allow edges among disconnected
				 * snippets for efficiency purposes, this method call below
				 * isn't required.
				 */
				// NextNodeDisjoiner.disjoinFromNextNode(destination);
			}
		}
	}

	@Deprecated
	private List<Node> succ = new ArrayList<>();

	@Deprecated
	private List<Node> pred = new ArrayList<>();

	@Deprecated
	public List<Node> deprecated_getSucc() {
		return succ;
	}

	@Deprecated
	public List<Node> deprecated_getPred() {
		return pred;
	}

}
