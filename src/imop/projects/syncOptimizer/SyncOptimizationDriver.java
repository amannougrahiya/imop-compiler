/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.projects.syncOptimizer;

import java.util.HashSet;
import java.util.Set;

import imop.Main;
import imop.ast.node.external.*;
import imop.lib.analysis.mhp.Phase;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;
import imop.parser.Program;

public class SyncOptimizationDriver {
	/**
	 * Performs code translation on the program so that the number of
	 * barriers and flushes encountered at runtime gets reduced. </br>
	 * 
	 */
	public static boolean optimizeProgram() {
		for (Node itStmtNode : Misc.getInheritedEnclosee(Program.getRoot(), IterationStatement.class)) {
			// Step 0: Process this loop, only if it has barriers in its CFG.
			if (!itStmtNode.getInfo().hasBarrierInCFG()) {
				continue;
			}

			// TODO: After removing unnecessary NodeChoice objects, kindly use method overriding below, instead of this if-else chain.
			if (itStmtNode instanceof ForStatement) {
				processForStatement((ForStatement) itStmtNode);
			} else if (itStmtNode instanceof WhileStatement) {
				if (processWhileStatement((WhileStatement) itStmtNode)) {
					return true;
				}
			} else if (itStmtNode instanceof DoStatement) {
				processDoStatement((DoStatement) itStmtNode);
			}
		}
		return false;
	}

	public static void processForStatement(ForStatement forStmt) {
		//		System.out.println("Processing the loop: " + forStmt);
	}

	/**
	 * Returns true if the processing should be done again.
	 * 
	 * @param whileStmt
	 * @return
	 */
	public static boolean processWhileStatement(WhileStatement whileStmt) {
		//		System.out.println("Processing the loop: " + whileStmt);
		// Step 1: Percolate code upwards in the loop.
		if (percolateUpwardsInLoop(whileStmt)) {
			return true;
		}

		// Step 2: Eliminate redundant barriers
		Set<BarrierDirective> barrierDirectiveNodeList = Misc.getInheritedEnclosee(whileStmt, BarrierDirective.class);
		for (Node barDirNode : barrierDirectiveNodeList) {
			if (eliminateBarriers((BarrierDirective) barDirNode)) {
				return true;
			}
		}
		// Step 3: Unroll the loop and process again.
		WhileStatement newStmt = unrollLoop(whileStmt);
		return true;
		//return false;
	}

	public static WhileStatement unrollLoop(WhileStatement whileStmt) {
		Node body = whileStmt.getInfo().getCFGInfo().getBody();
		Node predicate = whileStmt.getInfo().getCFGInfo().getPredicate();
		String newWhileLoop = "while(" + predicate + ")" + "{" + body;
		newWhileLoop += body + "}";
		WhileStatement newStmt = FrontEnd.parseAlone(newWhileLoop, WhileStatement.class);
		return newStmt;
	}

	public static boolean percolateUpwardsInLoop(IterationStatement itStmt) {
		Set<Phase> phaseList = new HashSet<>();
		for (Node barDirNode : Misc.getInheritedEnclosee(itStmt, BarrierDirective.class)) {
			phaseList.addAll(barDirNode.getInfo().getNodePhaseInfo().getPhaseSet());
		}
		boolean percolateUp;
		do {
			percolateUp = false;
			for (Phase phase : phaseList) {
				if (phase.deprecated_newPercolateCodeUpwards()) {
					return true;
				}
			}
		} while (percolateUp);
		return false;
	}

	public static boolean eliminateBarriers(BarrierDirective barr) {
		return false;
	}

	public static void processDoStatement(DoStatement doStmt) {
		//		System.out.println("Processing the loop: " + doStmt);
	}

}
