/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.percolate;

import imop.ast.info.cfgNodeInfo.ParallelConstructInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.transform.simplify.RedundantSynchronizationRemoval;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.List;

public class UpwardPercolater {
	/**
	 * This method percolates the nodes in parConstruct to as high a phase as
	 * possible.
	 * 
	 * @param parConstuct
	 */
	public static void initPercolate(ParallelConstruct parConstuct) {
		/*
		 * Forget about efficient implementation of the algorithm for now.
		 * First, let's try and make the output as efficient as possible. Take a
		 * phase. Process it. Process all the other phases. If there is a change
		 * in even a single phase, process all the phases again.
		 */

		ParallelConstructInfo parConsInfo = parConstuct.getInfo();

		// Percolate code upwards, as high (lexically) as possible.
		boolean percolateUp;
		do {
			percolateUp = false;
			for (AbstractPhase<?, ?> absPhase : new ArrayList<>(parConsInfo.getConnectedPhases())) {
				Phase phase = (Phase) absPhase;
				// System.err.println("Trying to percolate code upwards in phase #" +
				// phase.getPhaseId());
				boolean tempRet = phase.percolateCodeUpwardsInLoop(null);
				if (tempRet) {
					percolateUp = true;
					System.err.println("\tCode percolated upwards in phase #" + phase.getPhaseId());
				}
			}
		} while (percolateUp);

		// Dump the percolated code
		// System.out.println("\t\t********Percolated Code********");
		// Main.root.getInfo().printNode();
	}

	/**
	 * Performs simple upward percolation of code (against the control-flow,
	 * without crossing loop headers), and removes barriers.
	 */
	public static void percolateCodeAndRemoveBarriers() {
		for (ParallelConstruct parConstruct : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			UpwardPercolater.initPercolate(parConstruct);
		}
		RedundantSynchronizationRemoval.removeBarriers(Program.getRoot());
	}

	/**
	 * This method moves a node $movee$ up into its predecessor $pred$.
	 * 
	 * @param movee
	 *              : The node to be moved up.
	 * @param pred
	 *              : The predecessor into which the $movee$ has to be moved.
	 * @return true if the $movee$ could successfully be moved inside $pred$.
	 */
	public static boolean moveUpANode(Node movee, Node pred) {
		if (movee instanceof BeginNode || movee instanceof EndNode) {
			return false;
		}
		List<Node> predList = movee.getInfo().getCFGInfo().getPredBlocks();
		assert (predList.contains(pred));

		/*
		 * If $movee$ has more than one predecessors, we should put a copy of it
		 * on
		 * all the paths except on the path that joins $movee$ and $pred$.
		 */
		if (predList.size() > 1) {
			// TODO: Code here
			return false;
		}

		/*
		 * If we haven't returned yet, then we can assume that we have put a
		 * copy of
		 * $movee$ on all the paths between it and its other predecessors.
		 */
		if (pred instanceof CompoundStatement) {
			return moveUpACompoundStatement(movee, (CompoundStatement) pred);
		} else if (pred instanceof IfStatement) {
			return moveUpAnIfStatement(movee, (IfStatement) pred);
		} else if (pred instanceof SwitchStatement) {
			return moveUpASwitchStatement(movee, (SwitchStatement) pred);
		} else if (pred instanceof WhileStatement) {
			return moveUpAWhileStatement(movee, (WhileStatement) pred);
		} else if (pred instanceof DoStatement) {
			return moveUpADoStatement(movee, (DoStatement) pred);
		} else if (pred instanceof ForStatement) {
			return moveUpAForStatement(movee, (ForStatement) pred);
		}
		// TODO: Allow percolation into other type of statements, as and when needed.
		return false;
	}

	/**
	 * This method moves $movee$ up into the compound-statement $pred$.
	 * 
	 * @param movee
	 * @param pred
	 * @return
	 */
	private static boolean moveUpACompoundStatement(Node movee, CompoundStatement pred) {
		// TODO 1: Stop code motion, if there exists a name collision in the scopes.
		// TODO 2: If movee is a declaration, make sure that we do not break the code!

		/*
		 * $movee$ and $pred$, both are elements of certain enclosing
		 * compound-statement, say encCom.
		 * We should remove $movee$ from encCom, and make appropriate changes in
		 * the nested-CFG of encCom.
		 */
		List<Node> containingList = Misc.getContainingList(movee);
		if (containingList == null) {
			return false;
		}
		Node wrapperOfMovee = Misc.getWrapperInList(movee);
		containingList.remove(wrapperOfMovee);

		movee.getInfo().getCFGInfo().getPredBlocks().clear();
		for (Node newSucc : movee.getInfo().getCFGInfo().getSuccBlocks()) {
			pred.getInfo().getCFGInfo().getSuccBlocks().add(newSucc);
			pred.getInfo().getCFGInfo().getSuccBlocks().remove(movee);

			newSucc.getInfo().getCFGInfo().getPredBlocks().add(pred);
			newSucc.getInfo().getCFGInfo().getPredBlocks().remove(movee);
		}
		movee.getInfo().getCFGInfo().getSuccBlocks().clear();

		wrapperOfMovee.setParent(pred.getF1());
		pred.getF1().addNode(wrapperOfMovee);
		pred.getInfo().resetCallSites();

		// TODO: Code here.. Make proper CFG edges to and from the movee and endNode.

		return false;
	}

	/**
	 * This method moves $movee$ up into the if-statement $pred$.
	 * 
	 * @param movee
	 * @param pred
	 * @return
	 */
	private static boolean moveUpAnIfStatement(Node movee, IfStatement pred) {
		// TODO: Code here
		return false;
	}

	/**
	 * This method moves $movee$ up into the switch-statement $pred$.
	 * 
	 * @param movee
	 * @param pred
	 * @return
	 */
	private static boolean moveUpASwitchStatement(Node movee, SwitchStatement pred) {
		// TODO: Code here
		return false;
	}

	/**
	 * This method moves $movee$ up into the while-statement $pred$.
	 * 
	 * @param movee
	 * @param pred
	 * @return
	 */
	private static boolean moveUpAWhileStatement(Node movee, WhileStatement pred) {
		// TODO: Code here
		return false;
	}

	/**
	 * This method moves $movee$ up into the do-statement $pred$.
	 * 
	 * @param movee
	 * @param pred
	 * @return
	 */
	private static boolean moveUpADoStatement(Node movee, DoStatement pred) {
		// TODO: Code here
		return false;
	}

	/**
	 * This method moves $movee$ up into the for-statement $pred$.
	 * 
	 * @param movee
	 * @param pred
	 * @return
	 */
	private static boolean moveUpAForStatement(Node movee, ForStatement pred) {
		// TODO: Code here
		return false;
	}

}
