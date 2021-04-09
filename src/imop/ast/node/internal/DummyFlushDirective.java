/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.node.internal;

import imop.ast.info.cfgNodeInfo.DummyFlushDirectiveInfo;
import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.cg.CallStack;
import imop.lib.cg.NodeWithStack;
import imop.lib.util.CellSet;
import imop.lib.util.CollectorVisitor;

import java.util.*;

public class DummyFlushDirective extends FlushDirective {
	{
		classId = 1622463;
	}

	public DummyFlushDirective() {
		dummyFlushType = null;
	}

	private static final long serialVersionUID = 1L;
	private List<String> idSet;
	private final DummyFlushType dummyFlushType;
	private CellSet writtenCells;
	private CellSet readCells;
	private Set<Node> writtenBeforeNodes;
	private Set<Node> readAfterNodes;
	private Set<Node> uniLeavesAbove;
	private Set<Node> uniLeavesBelow;
	public boolean invalidWrittenCellSet = false;
	public boolean invalidReadCellSet = false;

	public Set<Node> getUniLeavesAbove() {
		if (uniLeavesAbove == null || this.invalidWrittenCellSet) {
			setWrittenBeforeFlush();
		}
		return Collections.unmodifiableSet(uniLeavesAbove);
	}

	public Set<Node> getUniLeavesBelow() {
		if (uniLeavesBelow == null || this.invalidReadCellSet) {
			setReadAfterFlush();
		}
		return Collections.unmodifiableSet(uniLeavesBelow);
	}

	public DummyFlushDirective(DummyFlushType dummyFlushType) {
		this.dummyFlushType = dummyFlushType;
		// idList points to null -- intentionally.
	}

	public DummyFlushDirective(DummyFlushType dummyFlushType, List<String> idList) {
		this.dummyFlushType = dummyFlushType;
		this.setIdList(idList);
	}

	@Override
	public DummyFlushDirectiveInfo getInfo() {
		if (info == null) {
			info = new DummyFlushDirectiveInfo(this);
		}
		return (DummyFlushDirectiveInfo) info;
	}

	@Override
	public boolean isKnownCFGNode() {
		return true;
	}

	@Override
	public boolean isKnownCFGLeafNode() {
		return true;
	}

	public DummyFlushType getDummyFlushType() {
		return this.dummyFlushType;
	}

	@Override
	public void accept(imop.baseVisitor.Visitor v) {
		v.visit(this);
	}

	@Override
	public <R, A> R accept(imop.baseVisitor.GJVisitor<R, A> v, A argu) {
		return v.visit(this, argu);
	}

	@Override
	public <R> R accept(imop.baseVisitor.GJNoArguVisitor<R> v) {
		return v.visit(this);
	}

	@Override
	public <A> void accept(imop.baseVisitor.GJVoidVisitor<A> v, A argu) {
		v.visit(this, argu);
	}

	public CellSet getWrittenBeforeCells() {
		if (writtenCells == null || this.invalidWrittenCellSet) {
			this.setWrittenBeforeFlush();
		}
		return writtenCells;
	}

	public CellSet getReadAfterCells() {
		if (readCells == null || this.invalidReadCellSet) {
			this.setReadAfterFlush();
		}
		return readCells;
	}

	public List<Cell> noUpdateGetWrittenBeforeCells() {
		if (writtenCells == null) {
			return new ArrayList<>();
		}
		List<Cell> retList = new ArrayList<>(writtenCells.getReadOnlyInternal());
		Collections.sort(retList);
		return retList;
		// return writtenCells;
	}

	public List<Cell> noUpdateGetReadAfterCells() {
		if (readCells == null) {
			return new ArrayList<>();
		}
		List<Cell> retList = new ArrayList<>(readCells.getReadOnlyInternal());
		Collections.sort(retList);
		return retList;
		// return readCells;
	}

	public Set<Node> getWrittenBeforeNodes() {
		if (this.writtenBeforeNodes == null || this.invalidWrittenCellSet) {
			this.setWrittenBeforeFlush();
		}
		return writtenBeforeNodes;
	}

	public Set<Node> getReadAfterNodes() {
		if (this.readAfterNodes == null || this.invalidReadCellSet) {
			this.setReadAfterFlush();
		}
		return readAfterNodes;
	}

	public void setWrittenBeforeFlush() {
		this.writtenCells = new CellSet();
		this.writtenBeforeNodes = new HashSet<>();
		this.uniLeavesAbove = new HashSet<>();
		Set<NodeWithStack> reachableNodeWithStacks = CollectorVisitor.collectNodeSetInGenericGraph(
				new NodeWithStack(this, new CallStack()), null, (n) -> (n.getNode() instanceof DummyFlushDirective),
				(n) -> n.getNode().getInfo().getCFGInfo().getInterProceduralLeafPredecessors(n.getCallStack()));

		for (NodeWithStack base : reachableNodeWithStacks) {
			Node baseNode = base.getNode();
			CellSet baseWrites = baseNode.getInfo().getSharedWrites();
			if (!baseWrites.isEmpty()) {
				writtenBeforeNodes.add(baseNode);
				writtenCells.addAll(baseWrites);
			}

			if (baseNode instanceof BeginNode) {
				BeginNode beginNode = (BeginNode) baseNode;
				Node parent = beginNode.getParent();
				if (parent instanceof SingleConstruct) {
					SingleConstruct single = (SingleConstruct) parent;
					if (!single.getInfo().isLoopedInPhase()) {
						uniLeavesAbove.addAll(single.getInfo().getCFGInfo().getIntraTaskCFGLeafContents());
					}
				} else if (parent instanceof MasterConstruct) {
					uniLeavesAbove.addAll(parent.getInfo().getCFGInfo().getIntraTaskCFGLeafContents());
				}
			}

		}
		invalidWrittenCellSet = false;
	}

	public void setReadAfterFlush() {
		this.readCells = new CellSet();
		this.readAfterNodes = new HashSet<>();
		this.uniLeavesBelow = new HashSet<>();
		Set<NodeWithStack> reachableNodeWithStacks = CollectorVisitor.collectNodeSetInGenericGraph(
				new NodeWithStack(this, new CallStack()), null, (n) -> (n.getNode() instanceof DummyFlushDirective),
				(n) -> n.getNode().getInfo().getCFGInfo().getInterProceduralLeafSuccessors(n.getCallStack()));

		for (NodeWithStack base : reachableNodeWithStacks) {
			Node baseNode = base.getNode();
			CellSet baseReads = baseNode.getInfo().getSharedReads();
			if (!baseReads.isEmpty()) {
				readAfterNodes.add(baseNode);
				readCells.addAll(baseReads);
			}

			if (baseNode instanceof BeginNode) {
				BeginNode beginNode = (BeginNode) baseNode;
				Node parent = beginNode.getParent();
				if (parent instanceof SingleConstruct) {
					SingleConstruct single = (SingleConstruct) parent;
					if (!single.getInfo().isLoopedInPhase()) {
						uniLeavesBelow.addAll(single.getInfo().getCFGInfo().getIntraTaskCFGLeafContents());
					}
				} else if (parent instanceof MasterConstruct) {
					uniLeavesBelow.addAll(parent.getInfo().getCFGInfo().getIntraTaskCFGLeafContents());
				}
			}
		}
		invalidReadCellSet = false;
	}

	@Deprecated
	List<String> getIdList() {
		return idSet;
	}

	@Deprecated
	void setIdList(List<String> idList) {
		this.idSet = idList;
	}
}
