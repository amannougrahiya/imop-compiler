/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.solver;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.solver.tokens.IdOrConstToken;
import imop.lib.analysis.solver.tokens.OperatorToken;
import imop.lib.util.Misc;
import imop.lib.util.NodePair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class contains various methods that are specific to field-sensitive
 * queries.
 * 
 * @author Aman Nougrahiya
 *
 */
public class FieldSensitivity {

	public static Set<NodePair> cannotConflit = new HashSet<>();
	public static Set<NodePair> canConflict = new HashSet<>();
	public static long timer = 0;
	public static long counter = 0;
	private static boolean debugMode = true;
	public static boolean neededAtLeastOnce = false;

	/**
	 * Given two CFG nodes, this method informs whether the nodes may conflict
	 * with each other.
	 * 
	 * @param s1
	 *           a CFG node.
	 * @param s2
	 *           a CFG node.
	 * @return
	 *         true, if the nodes may access same memory location, when run from
	 *         two different threads.
	 */
	public static boolean canConflictWithTwoThreads(Node s1, Node s2) {
		if (s1 instanceof BeginNode || s1 instanceof EndNode || s2 instanceof BeginNode || s2 instanceof EndNode) {
			return false;
		}
		s1 = Misc.getCFGNodeFor(s1);
		s2 = Misc.getCFGNodeFor(s2);
		long localTimer = System.nanoTime();
		NodePair nodePair = new NodePair(s1, s2);
		if (FieldSensitivity.cannotConflit.contains(nodePair)) {
			timer += (System.nanoTime() - localTimer);
			return false;
		}
		if (FieldSensitivity.canConflict.contains(nodePair)) {
			timer += (System.nanoTime() - localTimer);
			return true;
		}
		List<SyntacticAccessExpression> readSS1 = s1.getInfo().getBaseAccessExpressionReads();
		List<SyntacticAccessExpression> writeSS1 = s1.getInfo().getBaseAccessExpressionWrites();
		List<CastExpression> readPS1 = s1.getInfo().getPointerDereferencedExpressionReads();
		List<CastExpression> writePS1 = s1.getInfo().getPointerDereferencedExpressionWrites();
		if (readSS1.isEmpty() && writeSS1.isEmpty() && readPS1.isEmpty() && writePS1.isEmpty()) {
			cannotConflit.add(new NodePair(s1, s2));
			timer += (System.nanoTime() - localTimer);
			return false;
		}
		List<SyntacticAccessExpression> readSS2 = s2.getInfo().getBaseAccessExpressionReads();
		List<SyntacticAccessExpression> writeSS2 = s2.getInfo().getBaseAccessExpressionWrites();
		List<CastExpression> readPS2 = s2.getInfo().getPointerDereferencedExpressionReads();
		List<CastExpression> writePS2 = s2.getInfo().getPointerDereferencedExpressionWrites();
		if (readSS2.isEmpty() && writeSS2.isEmpty() && readPS2.isEmpty() && writePS2.isEmpty()) {
			cannotConflit.add(new NodePair(s1, s2));
			timer += (System.nanoTime() - localTimer);
			return false;
		}

		neededAtLeastOnce = true;
		// ConstraintsGenerator.reinitInductionSet(); // Shifted to flush in
		// AutomatedUpdater.

		if (FieldSensitivity.doConflict(writeSS1, readSS2) || FieldSensitivity.doConflict(writeSS1, writeSS2)
				|| FieldSensitivity.doConflict(writeSS1, readPS2) || FieldSensitivity.doConflict(writeSS1, writePS2)
				|| FieldSensitivity.doConflict(writePS1, readSS2) || FieldSensitivity.doConflict(writePS1, writeSS2)
				|| FieldSensitivity.doConflict(writePS1, readPS2) || FieldSensitivity.doConflict(writePS1, writePS2)
				|| FieldSensitivity.doConflict(writeSS2, readSS1) || FieldSensitivity.doConflict(writeSS2, readPS1)
				|| FieldSensitivity.doConflict(writePS2, readSS1) || FieldSensitivity.doConflict(writePS2, readPS1)) {
			canConflict.add(new NodePair(s1, s2));
			timer += (System.nanoTime() - localTimer);
			return true;
		}
		timer += (System.nanoTime() - localTimer);
		cannotConflit.add(new NodePair(s1, s2));
		return false;
	}

	private static <F, S> boolean doConflict(List<F> list1, List<S> list2) {
		if (list1.isEmpty() || list2.isEmpty()) {
			return false;
		}
		for (F o1 : list1) {
			for (S o2 : list2) {
				if (o1 == o2) {
					continue;
				}
				if (o1 instanceof SyntacticAccessExpression) {
					if (o2 instanceof SyntacticAccessExpression) {
						if (FieldSensitivity.doConflict((SyntacticAccessExpression) o1,
								(SyntacticAccessExpression) o2)) {
							return true;
						}
					} else {
						if (FieldSensitivity.doConflict((SyntacticAccessExpression) o1, (CastExpression) o2)) {
							return true;
						}
					}
				} else {
					if (o2 instanceof SyntacticAccessExpression) {
						if (FieldSensitivity.doConflict((SyntacticAccessExpression) o2, (CastExpression) o1)) {
							return true;
						}
					} else {
						if (FieldSensitivity.doConflict((CastExpression) o1, (CastExpression) o2)) {
							return true;
						}
					}

				}
			}
		}
		return false;
	}

	/**
	 * Generates and solves equations that assert equality of the index
	 * expressions used to index the locations accessed via dereferences
	 * on {@code sae1} and {@code sae2}, and also for lower level of
	 * indirections.
	 * 
	 * @param sae1
	 *             an expression that has been dereferenced using {@code []}.
	 * @param sae2
	 *             an expression that has been dereferenced using {@code []}.
	 * @return
	 *         true, if two different threads may access the same location by
	 *         dereferencing the given arguments.
	 */
	private static boolean doConflict(SyntacticAccessExpression sae1, SyntacticAccessExpression sae2) {
		ResolvedDereference rae1 = ResolvedDereference.getResolvedAccess(sae1);
		ResolvedDereference rae2 = ResolvedDereference.getResolvedAccess(sae2);
		Set<SeedConstraint> allEqualityConstraints = new HashSet<>();
		return FieldSensitivity.conflictExistsAtAnyLevel(rae1, rae2, allEqualityConstraints);
	}

	/**
	 * Generates and solves equations that assert equality of the index
	 * expressions used to index the locations accessed via dereferences
	 * on {@code sae1} and {@code ce2}, and also for lower level of
	 * indirections.
	 * 
	 * @param sae1
	 *             an expression that has been dereferenced using {@code []}
	 * @param ce2
	 *             an expression that has been dereferences using a pointer.
	 * @return
	 *         true, if two different threads may access the same location by
	 *         dereferencing the given arguments.
	 */
	private static boolean doConflict(SyntacticAccessExpression sae1, CastExpression ce2) {
		ResolvedDereference rae1 = ResolvedDereference.getResolvedAccess(sae1);
		ResolvedDereference rae2 = ResolvedDereference.getResolvedAccess(ce2);
		Set<SeedConstraint> allEqualityConstraints = new HashSet<>();
		return FieldSensitivity.conflictExistsAtAnyLevel(rae1, rae2, allEqualityConstraints);
	}

	/**
	 * Generates and solves equations that assert equality of the index
	 * expressions used to index the locations accessed via dereferences
	 * on {@code sae1} and {@code ce2}, and also for lower level of
	 * indirections.
	 * 
	 * @param ce1
	 *            an expression that has been dereferenced using a pointer.
	 * @param ce2
	 *            an expression that has been dereferences using a pointer.
	 * @return
	 * @return
	 *         true, if two different threads may access the same location by
	 *         dereferencing the given arguments.
	 */
	private static boolean doConflict(CastExpression ce1, CastExpression ce2) {
		ResolvedDereference rae1 = ResolvedDereference.getResolvedAccess(ce1);
		ResolvedDereference rae2 = ResolvedDereference.getResolvedAccess(ce2);
		Set<SeedConstraint> allEqualityConstraints = new HashSet<>();
		return FieldSensitivity.conflictExistsAtAnyLevel(rae1, rae2, allEqualityConstraints);
	}

	private static boolean conflictExistsAtAnyLevel(ResolvedDereference rae1, ResolvedDereference rae2,
			Set<SeedConstraint> allEqualityConstraints) {
		if (rae1 == null || rae2 == null) {
			// We were not able to determine the dereference form e1[e2]
			boolean isConflicting = ConstraintsGenerator.mayBeSatisfiable(allEqualityConstraints);
			if (debugMode) {
				if (isConflicting) {
					System.err.print("MAYBE_SATISFIABLE: ");
				} else {
					System.err.print("UNSATISFIABLE: ");
				}
				System.err.println(allEqualityConstraints);
			}
			return isConflicting;
		}
		if (!rae1.getBaseLocations().overlapsWith(rae2.getBaseLocations())) {
			// The base locations do not overlap; there cannot be any conflicts.
			return false;
		}
		if (!rae1.getBaseType().isCompatibleWith(rae2.getBaseType())) {
			/*
			 * These accesses are of incompatible types; we do not allow such
			 * accesses in program; refer to
			 * Type.hasIncompatibleTypeCastOfPointers.
			 */
			return false;
		}
		if (rae1.hasUnknownRange() || rae2.hasUnknownRange()) {
			// The access spans over the complete iteration domain, statically.
			boolean isConflicting = ConstraintsGenerator.mayBeSatisfiable(allEqualityConstraints);
			if (debugMode) {
				if (isConflicting) {
					System.err.print("MAYBE_SATISFIABLE: ");
				} else {
					System.err.print("UNSATISFIABLE: ");
				}
				System.err.println(allEqualityConstraints);
			}
			return isConflicting;
		}
		/*
		 * Add the constraints at this level.
		 */
		SeedConstraint eqConstraint = new SeedConstraint(rae1.getIndexExpression(), rae1.getLeafNode(),
				IdOrConstToken.getNewIdToken("tid1"), rae2.getIndexExpression(), rae2.getLeafNode(),
				IdOrConstToken.getNewIdToken("tid2"), OperatorToken.ASSIGN);
		allEqualityConstraints.add(eqConstraint);

		/*
		 * Process deeper levels recursively. Note that if none exists, the null
		 * check at the top of this method would invoke the solver.
		 */
		ResolvedDereference lower1 = rae1.getLowerLevelDereference();
		ResolvedDereference lower2 = rae2.getLowerLevelDereference();
		boolean isConflicting = FieldSensitivity.conflictExistsAtAnyLevel(lower1, lower2, allEqualityConstraints);
		allEqualityConstraints.remove(eqConstraint);
		return isConflicting;
	}

}
