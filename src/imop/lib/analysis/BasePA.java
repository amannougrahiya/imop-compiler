/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis;

import imop.ast.node.external.*;
import imop.lib.cfg.Edge;

import java.util.HashSet;
import java.util.Set;

public abstract class BasePA {
	public static Set<BasePA> allAbstractions = new HashSet<>();
	protected Set<Node> addedNodes = new HashSet<>();
	protected Set<Node> removedNodes = new HashSet<>();
	protected Set<Edge> addedEdges = new HashSet<>();
	protected Set<Edge> removedEdges = new HashSet<>();
	protected StableStatus stableStatus = StableStatus.INIT;
	protected final StabilizationMode stabilizationMode;

	public static enum StableStatus {
		STABLE, UNSTABLE, PROCESSING, INIT
	}

	public static enum StabilizationMode {
		EGINV, EGUPD, LZINV, // This is considered to be the default mode of stabilization.
		LZUPD
	}

	/**
	 * Constructs a program-abstraction with LZINV as its mode of stabilization.
	 * 
	 */
	public BasePA() {
		this.stabilizationMode = StabilizationMode.LZINV;
		BasePA.allAbstractions.add(this);
	}

	/**
	 * Constructs a program-abstraction with {@code stabilizationMode} as its
	 * mode of stabilization (or LZINV, if the argument is {@code null}).
	 * 
	 * @param stabilizationMode
	 *                          selected mode of stabilization for this
	 *                          program-abstraction;
	 *                          if {@code null}, LZINV mode is used by default.
	 */
	public BasePA(StabilizationMode stabilizationMode) {
		if (stabilizationMode == null) {
			this.stabilizationMode = StabilizationMode.LZINV;
		} else {
			this.stabilizationMode = stabilizationMode;
		}
		BasePA.allAbstractions.add(this);
	}

	/**
	 * INTERNAL method: This method is not supposed to be invoked from outside
	 * the context of an elementary transformation.
	 */
	public final static void modelNodeAddition(Node addedNode) {
		for (BasePA pa : BasePA.allAbstractions) {
			boolean changed = pa.removedNodes.remove(addedNode);
			if (!changed) {
				pa.stableStatus = pa.addedNodes.add(addedNode) ? StableStatus.UNSTABLE : pa.stableStatus;
			}
		}
	}

	/**
	 * INTERNAL method: This method is not supposed to be invoked from outside
	 * the context of an elementary transformation.
	 */
	public final static void modelNodeSetAddition(Set<Node> addedNodes) {
		for (BasePA pa : BasePA.allAbstractions) {
			for (Node n : addedNodes) {
				boolean changed = pa.removedNodes.remove(n);
				if (!changed) {
					pa.stableStatus = pa.addedNodes.add(n) ? StableStatus.UNSTABLE : pa.stableStatus;
				}
			}
		}
	}

	/**
	 * INTERNAL method: This method is not supposed to be invoked from outside
	 * the context of an elementary transformation.
	 */
	public final static void modelNodeRemoval(Node removedNode) {
		for (BasePA pa : BasePA.allAbstractions) {
			boolean changed = pa.addedNodes.remove(removedNode);
			if (!changed) {
				pa.stableStatus = pa.removedNodes.add(removedNode) ? StableStatus.UNSTABLE : pa.stableStatus;
			}
		}
	}

	/**
	 * INTERNAL method: This method is not supposed to be invoked from outside
	 * the context of an elementary transformation.
	 */
	public final static void modelNodeSetRemoval(Set<Node> removedNodes) {
		for (BasePA pa : BasePA.allAbstractions) {
			for (Node n : removedNodes) {
				boolean changed = pa.addedNodes.remove(n);
				if (!changed) {
					pa.stableStatus = pa.removedNodes.add(n) ? StableStatus.UNSTABLE : pa.stableStatus;
				}
			}
		}
	}

	/**
	 * INTERNAL method: This method is not supposed to be invoked from outside
	 * the context of an elementary transformation.
	 */
	public final static void modelEdgeAddition(Edge addedEdge) {
		for (BasePA pa : BasePA.allAbstractions) {
			boolean changed = pa.removedEdges.remove(addedEdge);
			if (!changed) {
				pa.stableStatus = pa.addedEdges.add(addedEdge) ? StableStatus.UNSTABLE : pa.stableStatus;
			}
		}
	}

	/**
	 * INTERNAL method: This method is not supposed to be invoked from outside
	 * the context of an elementary transformation.
	 */
	public final static void modelEdgeSetAddition(Set<Edge> addedEdges) {
		for (BasePA pa : BasePA.allAbstractions) {
			for (Edge n : addedEdges) {
				boolean changed = pa.removedEdges.remove(n);
				if (!changed) {
					pa.stableStatus = pa.addedEdges.add(n) ? StableStatus.UNSTABLE : pa.stableStatus;
				}
			}
		}
	}

	/**
	 * INTERNAL method: This method is not supposed to be invoked from outside
	 * the context of an elementary transformation.
	 */
	public final static void modelEdgeRemoval(Edge removedEdge) {
		for (BasePA pa : BasePA.allAbstractions) {
			boolean changed = pa.addedEdges.remove(removedEdge);
			if (!changed) {
				pa.stableStatus = pa.removedEdges.add(removedEdge) ? StableStatus.UNSTABLE : pa.stableStatus;
			}
		}
	}

	/**
	 * INTERNAL method: This method is not supposed to be invoked from outside
	 * the context of an elementary transformation.
	 */
	public final static void modelEdgeSetRemoval(Set<Edge> removedEdges) {
		for (BasePA pa : BasePA.allAbstractions) {
			for (Edge n : removedEdges) {
				boolean changed = pa.addedEdges.remove(n);
				if (!changed) {
					pa.stableStatus = pa.removedEdges.add(n) ? StableStatus.UNSTABLE : pa.stableStatus;
				}
			}
		}
	}

	/**
	 * Concrete program-abstractions should implement this method to perform any
	 * pre-processing that needs to be done before processing the impact of
	 * program changes on the internal state of this abstraction.
	 * 
	 */
	public abstract void commonPre();

	/**
	 * Concrete program-abstractions should implement this method to specify any
	 * common post-processing tasks after handling the impact of program changes
	 * to the internal state of this abstraction.
	 */
	public abstract void commonPost();

	/**
	 * Concrete program-abstractions should implement this method to specify how
	 * their internal state would change if a node {@code n} has been added
	 * to the super-graph of the program.
	 * 
	 * @param n
	 *          node that has been added to the program.
	 */
	public abstract void handleNodeAddition(Node n);

	/**
	 * Concrete program-abstractions should implement this method to
	 * 
	 */
	/**
	 * Concrete program-abstractions should implement this method to specify how
	 * their internal state would change if an edge {@code e} has been removed
	 * from the super-graph of the program.
	 * 
	 * @param n
	 *          node that has been removed from the program.
	 */
	public abstract void handleNodeRemoval(Node n);

	/**
	 * Concrete program-abstractions should implement this method to specify how
	 * their internal state would change if an edge {@code e} has been added
	 * to the super-graph of the program.
	 * 
	 * @param e
	 *          edge that has been added to the program.
	 */
	public abstract void handleEdgeAddition(Edge e);

	/**
	 * Concrete program-abstractions should implement this method to specify how
	 * their internal state would change if an edge {@code e} has been removed
	 * from the super-graph of the program.
	 * 
	 * @param e
	 *          edge that has been removed from the program.
	 */
	public abstract void handleEdgeRemoval(Edge e);

	/**
	 * Standard stabilizer defined by Homeostasis.
	 */
	public final void stabilizer() {
		if (this.stableStatus == StableStatus.STABLE) {
			return;
		}
		this.stableStatus = StableStatus.PROCESSING;
		this.commonPre();
		for (Node n : this.addedNodes) {
			this.handleNodeAddition(n);
		}
		this.addedNodes.clear();
		for (Node n : this.removedNodes) {
			this.handleNodeRemoval(n);
		}
		this.removedNodes.clear();
		for (Edge e : this.addedEdges) {
			this.handleEdgeAddition(e);
		}
		this.addedEdges.clear();
		for (Edge e : this.removedEdges) {
			this.handleEdgeRemoval(e);
		}
		this.removedEdges.clear();
		this.commonPost();
		this.stableStatus = StableStatus.STABLE;
	}

	/**
	 * This version of the stabilizer should be invoked when getters need to
	 * pass a context that can be used to determine, using
	 * {@code BasePA#isStabilizationNeeded(Object[])}, whether stabilization
	 * needs to be performed.
	 * 
	 * 
	 * @param arr
	 *            represents the <i>context</i> in which the getter of this
	 *            program
	 *            abstraction has been invoked.
	 */
	public void stabilizer(Object[] arr) {
		if (arr == null || arr.length == 0) {
			stabilizer();
		}
		if (this.isStabilizationNeeded(arr)) {
			stabilizer();
		}
		return;
	}

	/**
	 * This method can be overridden for a specific program abstraction to
	 * implement heuristics under which the stabilization can be delayed, under
	 * the given context in which the corresponding getter has been called.
	 * <p>
	 * Note that this method would be used only when a getter invokes
	 * {@code #stabilizer(Object[])} and not {@code #stabilizer()}.
	 * 
	 * @param arr
	 *            represents the <i>context</i> in which the getter of this
	 *            program
	 *            abstraction has been invoked.
	 * @return
	 *         {@code true} if stabilization needs to be performed in the given
	 *         context; {@code false} otherwise. The conservative value is
	 *         {@code true}.
	 *         When {@code false}, the getter would essentially see the
	 *         <i>old</i> internal state of this abstraction.
	 */
	public boolean isStabilizationNeeded(Object[] arr) {
		return true;
	}
}
