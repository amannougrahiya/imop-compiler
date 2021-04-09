/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp;

import imop.ast.node.external.*;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a static-time abstract lock.
 * This class internally maintains a static set of locks.
 * Access to the lock objects is through an iterator, or with the
 * help of the factory-method getLock.
 * 
 * @author aman
 * @deprecated
 *             this is old class that used to represent locks. This has been
 *             superseded with imop.lib.analysis.mhp.lock.AbstractLock.
 *
 */
@Deprecated
public class OldLock {
	public static Set<String> lockModifyRoutines;
	public static Set<String> lockWriteRoutines;
	protected static Set<OldLock> lockUniversalSet = new HashSet<>();

	static {
		lockModifyRoutines = new HashSet<>();
		lockModifyRoutines.add("omp_set_lock");
		lockModifyRoutines.add("omp_test_lock");
		lockModifyRoutines.add("omp_set_nest_lock");
		lockModifyRoutines.add("omp_test_nest_lock");

		lockWriteRoutines = new HashSet<>();
		lockWriteRoutines.add("omp_set_lock");
		lockWriteRoutines.add("omp_test_lock");
		lockWriteRoutines.add("omp_set_nest_lock");
		lockWriteRoutines.add("omp_test_nest_lock");
		lockWriteRoutines.add("omp_init_lock");
		lockWriteRoutines.add("omp_init_test_lock");
		lockWriteRoutines.add("omp_init_lock_with_hint");
		lockWriteRoutines.add("omp_init_test_lock_with_hint");
		lockWriteRoutines.add("omp_unset_lock");
		lockWriteRoutines.add("omp_unset_nest_lock");
	}

	private String name;
	private Node lockNode;

	protected OldLock(String name, Node lockNode) {
		this.name = name;
		this.lockNode = lockNode;
	}

	/**
	 * This factory-method implements singleton design pattern,
	 * so that only one lock exists with a given name.
	 * TODO: Check what happens when two AST nodes use the same lock,
	 * e.g., in case of two CriticalConstructs with same name. In case of
	 * issues, we can override the equals method.
	 * 
	 * @param name
	 * @return
	 */
	public static OldLock getLock(String name, Node lockNode) {
		OldLock retLock = null;
		for (OldLock lock : lockUniversalSet) {
			if (lock.name.equals(name)) {
				if (lockNode != null) {
					if (lock.lockNode == lockNode) {
						return lock;
					}
				}
				return lock;
			}
		}
		retLock = new OldLock(name, lockNode);
		lockUniversalSet.add(retLock);
		return retLock;
	}

	public static OldLock getAtomicLock() {
		return OldLock.getLock("__imop_atomic", null);
	}

	public String getName() {
		return name;
	}

	// /**
	// * If this lock is taken with the help of a critical-section, this method
	// * returns the reference to the corresponding AST node of type
	// * CriticalConstruct.
	// * <br>
	// * Note that two statements may get locked with different AST nodes and may
	// * yet
	// * share the same logical lock (which can be uniquely identified with the
	// * help of the lock's name).
	// * <p>
	// * If this lock is taken with the help of an atomic-region, this method
	// * returns
	// * the reference to the corresponding AtomicConstruct.
	// */
	// public Node getLockNode() {
	// return lockNode;
	// }
	//
	/**
	 * This method returns a copy of the set of all the locks.
	 * 
	 * @return
	 */
	public static Set<OldLock> getAllLocks() {
		return new HashSet<>(lockUniversalSet);
	}
}
