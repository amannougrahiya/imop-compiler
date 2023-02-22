package imop.lib.analysis.flowanalysis.generic;

import java.util.Collections;
import java.util.Comparator;

import imop.ast.node.external.Node;
import imop.lib.cfg.info.CFGInfo;

public class SinglePhasedRPOWorklist extends ReversePostOrderWorkList {
	@Override
	public boolean add(Node node) {
		// if (node instanceof BarrierDirective
		// || node instanceof EndNode && ((EndNode) node).getParent() instanceof
		// ParallelConstruct) {
		// return barrierSet.add(node);
		// }
		int ind = node.getReversePostOrder();
		if (ind < 0) {
			return false; // This is not a connected node.
		}
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
			// assert (this.nonBarrierList.contains(node)) : "The node " + node + " of type
			// "
			// + node.getClass().getSimpleName() + " present as " +
			// node.getInfo().getDebugString()
			// + " has the insertion index of " + insertionIndex
			// + " but is not available in the worklist aleady! In case if this is a
			// BeginNode or an EndNode, note that its parent is as follows. "
			// + node.getParent().getInfo().getDebugString() + foo(this.nonBarrierList,
			// node, insertionIndex);
			return true; // The node is already present. NOTE: YES, THIS IS SUPPOSED TO BE TRUE!
		}
		insertionIndex = -(insertionIndex) - 1;
		this.nonBarrierList.add(insertionIndex, node);
		return true;
	}

}
