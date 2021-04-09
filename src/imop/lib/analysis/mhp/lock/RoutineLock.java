/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp.lock;

import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.util.CellList;
import imop.lib.util.CellSet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoutineLock extends AbstractLock {
	public static Set<String> lockModifyRoutines;
	public static Set<String> lockWriteRoutines;
	public static Set<String> lockSettingRoutines;
	public static Set<String> lockReleasingRoutines;

	static {
		lockModifyRoutines = new HashSet<>();
		lockModifyRoutines.add("omp_set_lock");
		lockModifyRoutines.add("omp_test_lock");
		lockModifyRoutines.add("omp_set_nest_lock");
		lockModifyRoutines.add("omp_test_nest_lock");

		lockSettingRoutines = new HashSet<>();
		lockSettingRoutines.add("omp_set_lock");
		lockSettingRoutines.add("omp_test_lock");
		lockSettingRoutines.add("omp_set_nest_lock");
		lockSettingRoutines.add("omp_test_nest_lock");

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

		lockReleasingRoutines = new HashSet<>();
		lockReleasingRoutines.add("omp_unset_lock");
		lockReleasingRoutines.add("omp_unset_nest_lock");
	}

	private RoutineLock(CellSet lockedLocations) {
		this.lockedLocations = lockedLocations;
		AbstractLock.allLocks.add(this);
	}

	/**
	 * Set of cells that represent lock variables of type {@code omp_lock_t}
	 */
	private CellSet lockedLocations;

	public CellSet getLockedLocations() {
		return lockedLocations;
	}

	public static RoutineLock getRoutineLock(CellSet lockedLocations) {
		RoutineLock retLock = new RoutineLock(lockedLocations);
		if (!AbstractLock.allLocks.contains(retLock)) {
			AbstractLock.allLocks.add(retLock);
		}
		return retLock;
	}

	/**
	 * Obtain a routine lock object corresponding to the lock variables being
	 * locked or unlocked in the {@code lockingRoutine}.
	 * 
	 * @param lockingRoutine
	 * @return
	 */
	public static RoutineLock getRoutineLock(CallStatement lockingRoutine) {
		CellSet lockedLocations = new CellSet();
		CellList calledRoutines = lockingRoutine.getInfo().getCalledSymbols();
		if (calledRoutines.size() != 1) {
			return null;
		}
		Cell calledCell = calledRoutines.get(0);
		if (calledCell == Cell.genericCell) {
			return null;
		}
		Symbol calledSymbol = (Symbol) calledCell;
		if ((!lockSettingRoutines.contains(calledSymbol.getName()))
				&& (!lockReleasingRoutines.contains(calledSymbol.getName()))) {
			return null;
		}
		List<SimplePrimaryExpression> argumentList = lockingRoutine.getInfo().getArgumentList();
		if (argumentList.size() != 1) {
			return null;
		}
		SimplePrimaryExpression argument = argumentList.get(0);
		CellSet tempCellSet = new CellSet(argument.getInfo().getLocationsOf());
		if (tempCellSet.isUniversal()) {
			lockedLocations.add(Cell.genericCell);
		} else {
			tempCellSet.applyAllExpanded(lockingPointer -> {
				lockedLocations.addAll(lockingPointer.getPointsTo(lockingRoutine));
			});
		}
		return RoutineLock.getRoutineLock(lockedLocations);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof RoutineLock)) {
			return false;
		}
		RoutineLock that = (RoutineLock) other;
		if (that.lockedLocations.equals(that.lockedLocations)) {
			return true;
		} else {
			return false;
		}
		// if (that.lockedLocations.size() != this.lockedLocations.size()) {
		// return false;
		// }
		// for (Cell thisSymbol : this.lockedLocations) {
		// if (that.lockedLocations.contains(thisSymbol)) {
		// return false;
		// }
		// }
		// return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.lockedLocations == null) ? 0 : this.lockedLocations.size());
		return result;
		// return Arrays.hashCode(new Object[] {this.lockedLocations});
	}

	@Override
	public String toString() {
		return this.lockedLocations.toString();
	}

}
