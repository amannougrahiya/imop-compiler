package imop.lib.analysis.mhp.incMHP;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import imop.ast.node.external.BarrierDirective;
import imop.ast.node.external.Node;
import imop.lib.analysis.CoExistenceChecker;
import imop.lib.util.Misc;

public class MemoizedPhaseInformation {

	private static final Map<BarrierPhaseNode, Boolean> mapAbove = new HashMap<>();
	private static final Map<BarrierPhaseNode, Boolean> mapBelow = new HashMap<>();

	public static class BarrierPhaseNode {
		public final BarrierDirective barr;
		public final Phase ph;
		public final Node node;
		private int hashVal = -1;

		public BarrierPhaseNode(BarrierDirective barr, Phase ph, Node node) {
			this.barr = barr;
			this.ph = ph;
			this.node = node;
		}

		@Override
		public int hashCode() {
			if (hashVal == -1) {
				final int prime = 31;
				int result = barr.getNodeId() + ph.hashCode() + node.getNodeId();
				hashVal = result;
				return result;
			} else {
				return hashVal;
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			BarrierPhaseNode other = (BarrierPhaseNode) obj;
			if (barr == null) {
				if (other.barr != null)
					return false;
			} else if (barr != other.barr)
				return false;
			if (ph == null) {
				if (other.ph != null)
					return false;
			} else if (!ph.equals(other.ph))
				return false;
			if (node == null) {
				if (other.node != null)
					return false;
			} else if (node != other.node)
				return false;
			return true;
		}
	}

	public static boolean canNodeCoExistAbove(BarrierDirective barr, Phase phAbove, Node node) {
		BarrierPhaseNode barrPh = new BarrierPhaseNode(barr, phAbove, node);
		Boolean value = mapAbove.get(barrPh);
		if (value == null) {
			value = CoExistenceChecker.canCoExistInPhase(node, barr, phAbove);
			mapAbove.put(barrPh, value);
		}
		return value;
	}

	public static boolean canNodeCoexistBelow(BarrierDirective barr, Phase phBelow, Node node) {
		BarrierPhaseNode barrPh = new BarrierPhaseNode(barr, phBelow, node);
		Boolean value = mapBelow.get(barrPh);
		if (value == null) {
			Node successor = Misc.getAnyElement(barr.getInfo().getCFGInfo().getInterProceduralLeafSuccessors());
			value = CoExistenceChecker.canCoExistInPhase(successor, node, phBelow);
			mapBelow.put(barrPh, value);
		}
		return value;
	}


	public static void flushMemoizedInformation() {
		mapAbove.clear();
		mapBelow.clear();
	}
}
