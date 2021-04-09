/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.generic;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.DFable;
import imop.lib.analysis.flowanalysis.SCC;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.*;

public class ReversePostOrderWorkList {
	private static enum Stage {
		NONBARRIER, BARRIER
	}

	private Stage stage = Stage.NONBARRIER;
	public static int ignored = 0;

	private List<Node> nonBarrierList = new LinkedList<>();
	private Set<Node> barrierSet = new HashSet<>();

	public boolean isEmpty() {
		return this.barrierSet.isEmpty() && this.nonBarrierList.isEmpty();
	}

	public void recreate() {
		this.barrierSet = new HashSet<>();
		this.nonBarrierList = new LinkedList<>();
	}

	public void setStageToBarrier() {
		this.stage = Stage.BARRIER;
	}

	public boolean add(Node node) {
		if (node instanceof BarrierDirective
				|| node instanceof EndNode && ((EndNode) node).getParent() instanceof ParallelConstruct) {
			return barrierSet.add(node);
		}
		int ind = node.getReversePostOrder();
		if (ind < 0) {
			/*
			 * This does not seem to be a connected node. Is it, though? Let's
			 * check.
			 */
			// assert
			// (!Program.getRoot().getInfo().getCFGInfo().getLexicalCFGContents().contains(node))
			// : node
			// + " of type " + node.getClass().getSimpleName()
			// + " does not have a reverse postorder id, despite having the following
			// parent: "
			// + node.getParent().getInfo().getDebugString();
			return false; // This is not a connected node.
		}
		// assert (ind >= 0) : "The node " + node + " of type " +
		// node.getClass().getSimpleName() + " has no valid reverse postorder index!"
		// + " It is " + ind + ". "
		// + "The node is connected within " +
		// node.getParent().getInfo().getDebugString();
		int insertionIndex = Collections.binarySearch(this.nonBarrierList, node, new Comparator<Node>() {
			@Override
			public int compare(Node n1, Node n2) {
				CFGInfo n1Info = n1.getInfo().getCFGInfo();
				CFGInfo n2Info = n2.getInfo().getCFGInfo();
				assert (n1.getReversePostOrder() >= 0);
				assert (n2.getReversePostOrder() >= 0);
				if (n1Info.getSCC() == null) {
					if (n2Info.getSCC() == null) {
						return n1.getReversePostOrder() - n2.getReversePostOrder();
					} else {
						int n2SCCId = n2Info.getSCC().getReversePostOrder();
						return n1.getReversePostOrder() - n2SCCId;
					}
				} else {
					int n1SCCId = n1Info.getSCC().getReversePostOrder();
					if (n2Info.getSCC() == null) {
						return n1SCCId - n2.getReversePostOrder();
					} else {
						int n2SCCId = n2Info.getSCC().getReversePostOrder();
						if (n1SCCId != n2SCCId) {
							return n1SCCId - n2SCCId;
						} else {
							return n1.getReversePostOrder() - n2.getReversePostOrder();
						}
					}
				}
			}
		});
		if (insertionIndex >= 0) {
			assert (this.nonBarrierList.contains(node)) : "The node " + node + " of type "
					+ node.getClass().getSimpleName() + " present as " + node.getInfo().getDebugString()
					+ " has the insertion index of " + insertionIndex
					+ " but is not available in the worklist aleady! In case if this is a BeginNode or an EndNode, note that its parent is as follows. "
					+ node.getParent().getInfo().getDebugString() + foo(this.nonBarrierList, node, insertionIndex);
			return true; // The node is already present. NOTE: YES, THIS IS SUPPOSED TO BE TRUE!
		}
		insertionIndex = -(insertionIndex) - 1;
		this.nonBarrierList.add(insertionIndex, node);
		return true;
		// OLD CODE:
		// this.insertUsingBinarySearch(selectedList, node, nodeOrder, 0,
		// selectedList.size() - 1);

	}

	private String foo(List<Node> nonBarrList, Node node, int insertionIndex) {
		String ret = "The list IDs are: [\n";
		for (Node n : nonBarrList) {
			if (n.getInfo().getCFGInfo().getSCC() == null) {
				ret += n.getReversePostOrder() + "-" + n.hashCode() + "\n";
			} else {
				ret += n.getInfo().getCFGInfo().getSCC().getReversePostOrder() + "-" + n.getReversePostOrder() + "-"
						+ n.hashCode() + "\n";
			}
		}
		ret += "]\n ..., whereas the ID of this node is ";
		if (node.getInfo().getCFGInfo().getSCC() == null) {
			ret += node.getReversePostOrder() + "-" + node.hashCode() + ",";
		} else {
			ret += node.getInfo().getCFGInfo().getSCC().getReversePostOrder() + "-" + node.getReversePostOrder() + "-"
					+ node.hashCode() + ".";
		}
		ret += "\n Also note that the node with which the insertion index matched was as follows: "
				+ this.nonBarrierList.get(insertionIndex).getInfo().getDebugString() + ".";
		ret += "\n Finally, here are all the IDs: ";
		for (Node n : Program.getRoot().getInfo().getCFGInfo().getLexicalCFGContents()) {
			if (n.getInfo().getCFGInfo().getSCC() == null) {
				ret += n.getReversePostOrder() + "-" + n.hashCode() + "\n";
			} else {
				ret += n.getInfo().getCFGInfo().getSCC().getReversePostOrder() + "-" + n.getReversePostOrder() + "-"
						+ n.hashCode() + "\n";
			}
		}
		ret += ".";
		return ret;
	}
	//
	// private String foo(List<Node> list) {
	// String s = "[";
	// for (Node n : list) {
	// s += n.hashCode() + "; ";
	// }
	// s += "]";
	// return s;
	// }

	public void addAll(Collection<Node> collection) {
		for (Node n : collection) {
			this.add(n);
		}
	}

	public Node peekFirstElement() {
		if (this.stage == Stage.NONBARRIER) {
			// In NONBARRIER stage.
			if (this.nonBarrierList.isEmpty()) {
				if (this.barrierSet.isEmpty()) {
					return null;
				} else {
					return Misc.getAnyElement(this.barrierSet);
				}
			} else {
				return this.nonBarrierList.get(0);
			}
		} else {
			// In BARRIER stage.
			if (this.barrierSet.isEmpty()) {
				if (this.nonBarrierList.isEmpty()) {
					return null;
				} else {
					return this.nonBarrierList.get(0);
				}
			} else {
				return Misc.getAnyElement(this.barrierSet);
			}
		}
	}

	public Node removeFirstElement() {
		if (this.stage == Stage.NONBARRIER) {
			// In NONBARRIER stage.
			if (this.nonBarrierList.isEmpty()) {
				if (this.barrierSet.isEmpty()) {
					return null;
				} else {
					this.stage = Stage.BARRIER;
					return this.removeFirstElement();
				}
			} else {
				Node first = this.nonBarrierList.get(0);
				this.nonBarrierList.remove(first);
				return first;
			}
		} else {
			// In BARRIER stage.
			if (this.barrierSet.isEmpty()) {
				if (this.nonBarrierList.isEmpty()) {
					return null;
				} else {
					this.stage = Stage.NONBARRIER;
					return this.removeFirstElement();
				}
			} else {
				Node barr = Misc.getAnyElement(this.barrierSet);
				this.barrierSet.remove(barr);
				return barr;
			}
		}
	}

	public Node removeLastElement() {
		if (this.stage == Stage.NONBARRIER) {
			// In NONBARRIER stage.
			if (this.nonBarrierList.isEmpty()) {
				if (this.barrierSet.isEmpty()) {
					return null;
				} else {
					this.stage = Stage.BARRIER;
					return this.removeLastElement();
				}
			} else {
				Node last = this.nonBarrierList.get(this.nonBarrierList.size() - 1);
				this.nonBarrierList.remove(last);
				return last;
			}
		} else {
			// In BARRIER stage.
			if (this.barrierSet.isEmpty()) {
				if (this.nonBarrierList.isEmpty()) {
					return null;
				} else {
					this.stage = Stage.NONBARRIER;
					return this.removeLastElement();
				}
			} else {
				Node barr = Misc.getAnyElement(this.barrierSet);
				this.barrierSet.remove(barr);
				return barr;
			}
		}
	}

	public Node peekFirstElementOfSameSCC(int sccId) {
		if (this.stage == Stage.NONBARRIER) {
			// In NONBARRIER stage.
			if (this.hasEmptyNonBarrierListForId(sccId)) {
				if (this.hasEmptyBarrierSetForId(sccId)) {
					return null;
				} else {
					for (Node barr : this.barrierSet) {
						if (barr.getInfo().getCFGInfo().getSCCRPOIndex() == sccId) {
							return barr;
						}
					}
					return null;
				}
			} else {
				return this.nonBarrierList.get(0);
			}
		} else {
			// In BARRIER stage.
			if (this.hasEmptyBarrierSetForId(sccId)) {
				if (this.hasEmptyNonBarrierListForId(sccId)) {
					return null;
				} else {
					return this.nonBarrierList.get(0);
				}
			} else {
				for (Node barr : this.barrierSet) {
					if (barr.getInfo().getCFGInfo().getSCCRPOIndex() == sccId) {
						return barr;
					}
				}
				return null;
			}
		}
	}

	public Node removeFirstElementOfSameSCC(int sccId) {
		if (this.stage == Stage.NONBARRIER) {
			// In NONBARRIER stage.
			if (this.hasEmptyNonBarrierListForId(sccId)) {
				if (this.hasEmptyBarrierSetForId(sccId)) {
					return null;
				} else {
					this.stage = Stage.BARRIER;
					return this.removeFirstElementOfSameSCC(sccId);
				}
			} else {
				Node first = this.nonBarrierList.get(0);
				this.nonBarrierList.remove(first);
				return first;
			}
		} else {
			// In BARRIER stage.
			if (this.hasEmptyBarrierSetForId(sccId)) {
				if (this.hasEmptyNonBarrierListForId(sccId)) {
					return null;
				} else {
					this.stage = Stage.NONBARRIER;
					return this.removeFirstElementOfSameSCC(sccId);
				}
			} else {
				Node barr = null;
				for (Node barrTemp : this.barrierSet) {
					if (barrTemp.getInfo().getCFGInfo().getSCCRPOIndex() == sccId) {
						barr = barrTemp;
					}
				}
				this.barrierSet.remove(barr);
				return barr;
			}
		}
	}

	private boolean hasEmptyNonBarrierListForId(int id) {
		for (Node n : this.nonBarrierList) {
			if (n.getInfo().getCFGInfo().getSCCRPOIndex() == id) {
				return false;
			}
		}
		return true;
	}

	private boolean hasEmptyBarrierSetForId(int id) {
		for (Node n : this.barrierSet) {
			if (n.getInfo().getCFGInfo().getSCCRPOIndex() == id) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private void insertUsingBinarySearch(List<Node> list, final Node node, final int nodeWeight, final int startIndex,
			final int endIndex) {
		if (endIndex <= startIndex) {
			// Insert on the correct end of the startIndex, and return.
			Node baseNode = list.get(startIndex);
			int stIndex = baseNode.getInfo().getReversePostOrder();
			if (nodeWeight == stIndex) {
				if (baseNode != node) {
					String baseString = (baseNode instanceof BeginNode) ? "BeginNode"
							: (baseNode instanceof EndNode) ? "EndNode" : baseNode.toString();
					String nodeString = (node instanceof BeginNode) ? "BeginNode"
							: (node instanceof EndNode) ? "EndNode" : node.toString();
					Misc.warnDueToLackOfFeature(
							"Node " + baseString + " has same id as that of " + nodeString + " at index " + stIndex,
							null);
					list.add(startIndex, node);
					return;
				} else {
					// Node is already present.
					return;
				}
			} else if (nodeWeight < stIndex) {
				list.add(startIndex, node);
			} else {
				list.add(startIndex + 1, node);
			}
			return;
		}
		if (list.get(endIndex).getInfo().getReversePostOrder() == -1) {
			insertUsingBinarySearch(list, node, nodeWeight, startIndex, endIndex - 1);
			return;
		}
		int midIndex = (startIndex + endIndex) / 2;
		Node midNode = list.get(midIndex);
		int midWeight = midNode.getInfo().getReversePostOrder();
		if (nodeWeight == midWeight) {
			if (midNode != node) {
				String baseString = (midNode instanceof BeginNode) ? "BeginNode"
						: (midNode instanceof EndNode) ? "EndNode" : midNode.toString();
				String nodeString = (node instanceof BeginNode) ? "BeginNode"
						: (node instanceof EndNode) ? "EndNode" : node.toString();
				Misc.warnDueToLackOfFeature(
						"Node " + baseString + " has same id as that of " + nodeString + " at index " + midIndex, null);
				list.add(midIndex, node);
				return;
			} else {
				// Node is already present.
				return;
			}
		} else if (nodeWeight < midWeight) {
			insertUsingBinarySearch(list, node, nodeWeight, startIndex, midIndex - 1);
			return;
		} else {
			insertUsingBinarySearch(list, node, nodeWeight, midIndex + 1, endIndex);
			return;
		}
	}

	@Deprecated
	public static class SCCItem implements Comparable<SCCItem> {
		public final Node node;
		public final SCC scc;
		public final List<Node> sccElements = new ArrayList<>();

		public SCCItem(Node node) {
			assert (node.getInfo().getCFGInfo().getSCC() == null); // This must be an SCC node.
			this.node = node;
			this.scc = null;
		}

		public SCCItem(SCC scc) {
			this.node = null;
			this.scc = scc;
		}

		public boolean isNode() {
			return this.node != null;
		}

		public boolean isSCC() {
			return this.scc != null;
		}

		@Override
		public int compareTo(SCCItem otherItem) {
			DFable thatNode = otherItem.isNode() ? otherItem.node : otherItem.scc;
			DFable thisNode = this.isNode() ? this.node : this.scc;
			int thisId = thisNode.getReversePostOrder();
			int thatId = thatNode.getReversePostOrder();
			return thisId - thatId;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof SCCItem)) {
				return false;
			}
			SCCItem that = (SCCItem) obj;
			if (this.isNode()) {
				if (that.isNode()) {
					return this.node == that.node;
				} else {
					return false;
				}
			} else {
				if (that.isNode()) {
					return false;
				} else {
					return this.scc.id == that.scc.id;
				}
			}
		}

		@Override
		public int hashCode() {
			return this.isNode() ? this.node.hashCode() : this.scc.hashCode();
		}

	}
}
