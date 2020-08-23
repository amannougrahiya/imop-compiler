/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.dataflow;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisDimension.SVEDimension;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.flowanalysis.generic.InterThreadForwardNonCellularAnalysis;
import imop.lib.analysis.mhp.lock.AbstractLock;
import imop.lib.analysis.mhp.lock.AtomicLock;
import imop.lib.analysis.mhp.lock.CriticalLock;
import imop.lib.analysis.mhp.lock.RoutineLock;
import imop.lib.util.CellList;
import imop.lib.util.CellSet;

import java.util.HashMap;

public class LockSetAnalysis extends InterThreadForwardNonCellularAnalysis<LockSetAnalysis.LockFlowFact> {

	/**
	 * This class ensures that <i>if</i> all entries in the map are entered
	 * using the {@link java.util.HashMap#put(Object, Object) HashMap::put}
	 * method, then the maximum integer value that an AbstractLock may map
	 * to would be {@code MAX_NESTING}.
	 * <br>
	 * Note: This class will not ensure the above guarantee, if changes to
	 * the map are made via other methods, like
	 * {@link java.util.HashMap#putAll(java.util.Map) HashMap::putAll}.
	 * 
	 * @author aman
	 *
	 */
	private static class LockMap extends HashMap<AbstractLock, Integer> {
		private static final long serialVersionUID = 1L;
		public static final int MAX_NESTING = 5;

		/**
		 * Obtain a copy of this map.
		 * <br>
		 * This method is not a constructor due to some unknown reasons.
		 * 
		 * @return
		 *         a copy of this map.
		 */
		public LockMap getCopy() {
			LockMap newMap = new LockMap();
			for (AbstractLock lock : this.keySet()) {
				newMap.put(lock, this.get(lock));
			}
			return newMap;
		}

		@Override
		public Integer put(AbstractLock abstractLock, Integer integer) {
			if (integer > MAX_NESTING) {
				return super.put(abstractLock, MAX_NESTING);
			} else if (integer < 0) {
				return super.put(abstractLock, 0);
			} else {
				return super.put(abstractLock, integer);
			}
		}
	} // End of LockMap

	/**
	 * Flow fact type for lock-set analysis.
	 * 
	 * @author aman
	 *
	 */
	public static class LockFlowFact extends FlowAnalysis.FlowFact {
		private LockMap lockMap;

		public LockFlowFact(LockMap lockMap) {
			this.lockMap = lockMap;
		}

		public HashMap<AbstractLock, Integer> getLockMapCopy() {
			return new HashMap<>(this.lockMap);
		}

		public int incrementLockCounter(AbstractLock lock) {
			if (this.lockMap == null) {
				this.lockMap = new LockMap();
			}
			if (!this.lockMap.containsKey(lock)) {
				this.lockMap.put(lock, 1);
				return 1;
			} else {
				int setSize = this.lockMap.get(lock);
				setSize = ((setSize + 1) < LockMap.MAX_NESTING) ? (setSize + 1) : LockMap.MAX_NESTING;
				this.lockMap.put(lock, setSize);
				return setSize;
			}
		}

		public int decrementLockCounter(AbstractLock lock) {
			if (this.lockMap == null) {
				this.lockMap = new LockMap();
			}
			if (!this.lockMap.containsKey(lock)) {
				this.lockMap.put(lock, 0);
				return 0;
			} else {
				int setSize = this.lockMap.get(lock);
				setSize = ((setSize - 1) > 0) ? (setSize - 1) : 0;
				return setSize;
			}
		}

		@Override
		// TODO: Optimize this method.
		public boolean isEqualTo(FlowFact other) {
			if (other == null) {
				return false;
			}
			if (!(other instanceof LockFlowFact)) {
				return false;
			}
			LockFlowFact that = (LockFlowFact) other;
			if (this == that) {
				return true;
			}
			if (this.lockMap == null && that.lockMap == null) {
				return true;
			}
			if (this.lockMap == null || that.lockMap == null) {
				return false;
			}
			for (AbstractLock thisLock : this.lockMap.keySet()) {
				if (!that.lockMap.containsKey(thisLock)) {
					if (this.lockMap.get(thisLock) != 0) {
						return false;
					} else {
						continue;
					}
				}
				int thisSize = this.lockMap.get(thisLock);
				int thatSize = that.lockMap.get(thisLock);
				if (thisSize != thatSize) {
					return false;
				}
			}
			for (AbstractLock thatLock : that.lockMap.keySet()) {
				if (!this.lockMap.containsKey(thatLock)) {
					if (that.lockMap.get(thatLock) != 0) {
						return false;
					} else {
						continue;
					}
				}
				int thatSize = that.lockMap.get(thatLock);
				int thisSize = this.lockMap.get(thatLock);
				if (thatSize != thisSize) {
					return false;
				}
			}
			return true;
		}

		@Override
		public String getString() {
			String retString = new String();
			retString += "Locks that must have been taken at this node are: ";
			for (AbstractLock abstractLock : this.lockMap.keySet()) {
				retString += abstractLock + "(" + this.lockMap.get(abstractLock) + "); ";
			}
			retString += "\n";
			return retString;
		}

		@Override
		public boolean merge(FlowFact other, CellSet cellSet) {
			boolean changed = false;
			assert (other instanceof LockFlowFact);
			LockFlowFact that = (LockFlowFact) other;
			if (this == that) {
				return false;
			}
			// Assuming that null implies universal set.
			if (this.lockMap == null && that.lockMap == null) {
				// Do nothing.
			} else if (this.lockMap == null) {
				this.lockMap = new LockMap();
				for (AbstractLock thatLock : that.lockMap.keySet()) {
					this.lockMap.put(thatLock, that.lockMap.get(thatLock));
				}
			} else if (that.lockMap == null) {
				changed = false;
			} else {
				for (AbstractLock thisLock : this.lockMap.keySet()) {
					if (!that.lockMap.containsKey(thisLock)) {
						continue;
					}
					int thisSize = this.lockMap.get(thisLock);
					int thatSize = that.lockMap.get(thisLock);
					int returnSize = (thisSize < thatSize) ? thisSize : thatSize;
					if (thisSize != returnSize) {
						changed = true;
					}
					this.lockMap.put(thisLock, returnSize);
				}
			}
			return changed;
		}

	}

	public LockSetAnalysis() {
		super(AnalysisName.LOCKSET_ANALYSIS, new AnalysisDimension(SVEDimension.SVE_INSENSITIVE));
	}

	@Override
	public LockFlowFact getTop() {
		return new LockFlowFact(new LockMap());
	}

	@Override
	public LockFlowFact getEntryFact() {
		return this.getTop();
	}

	@Override
	public LockFlowFact writeToParameter(ParameterDeclaration parameter, SimplePrimaryExpression argument,
			LockFlowFact flowFactIN) {
		return flowFactIN;
	}

	@Override
	public LockFlowFact visit(BeginNode n, LockFlowFact inFF) {
		Node parentNode = n.getParent();
		LockMap outLockMap = null;
		if (inFF.lockMap != null) {
			outLockMap = inFF.lockMap.getCopy();
		}
		LockFlowFact outFF = new LockFlowFact(outLockMap);
		if (parentNode instanceof AtomicConstruct) {
			AtomicConstruct atomicConstruct = (AtomicConstruct) parentNode;
			outFF.incrementLockCounter(AtomicLock.getAtomicLock(atomicConstruct));
		} else if (parentNode instanceof CriticalConstruct) {
			CriticalConstruct criticalConstruct = (CriticalConstruct) parentNode;
			outFF.incrementLockCounter(CriticalLock.getCriticalLock(criticalConstruct));
		}
		return outFF;
	}

	@Override
	public LockFlowFact visit(EndNode n, LockFlowFact inFF) {
		Node parentNode = n.getParent();
		LockMap outLockMap = null;
		if (inFF.lockMap != null) {
			outLockMap = inFF.lockMap.getCopy();
		}
		LockFlowFact outFF = new LockFlowFact(outLockMap);
		if (parentNode instanceof AtomicConstruct) {
			AtomicConstruct atomicConstruct = (AtomicConstruct) parentNode;
			outFF.decrementLockCounter(AtomicLock.getAtomicLock(atomicConstruct));
		} else if (parentNode instanceof CriticalConstruct) {
			CriticalConstruct criticalConstruct = (CriticalConstruct) parentNode;
			outFF.decrementLockCounter(CriticalLock.getCriticalLock(criticalConstruct));
		}
		return outFF;
	}

	@Override
	public LockFlowFact visit(PreCallNode n, LockFlowFact inFF) {
		LockMap outLockMap = null;
		if (inFF.lockMap != null) {
			outLockMap = inFF.lockMap.getCopy();
		}
		LockFlowFact outFF = new LockFlowFact(outLockMap);
		CallStatement callStmt = n.getParent();
		if (!callStmt.getPostCallNode().hasReturnReceiver()) {
			CellList calledRoutines = callStmt.getInfo().getCalledSymbols();
			if (calledRoutines.size() == 1) {
				Cell calledCell = calledRoutines.get(0);
				if (calledCell == Cell.genericCell) {
					// TODO: Code here.
				} else {
					Symbol calledSymbol = (Symbol) calledCell;
					if (RoutineLock.lockSettingRoutines.contains(calledSymbol.getName())) {
						outFF.incrementLockCounter(RoutineLock.getRoutineLock(callStmt));
					} else if (RoutineLock.lockReleasingRoutines.contains(calledSymbol.getName())) {
						outFF.decrementLockCounter(RoutineLock.getRoutineLock(callStmt));
					}
				}
			}
		}
		return outFF;
	}
}
