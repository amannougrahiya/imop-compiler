/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.simplify;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.cfg.info.CompoundStatementCFGInfo;
import imop.lib.transform.updater.InsertImmediatePredecessor;
import imop.lib.transform.updater.InsertImmediateSuccessor;

public class InsertDummyFlushDirectives {

	/**
	 * @deprecated
	 *             Use
	 *             {@link CompoundStatementCFGInfo#initializeDummyFlushes()}
	 *             instead.
	 * @param root
	 */
	@Deprecated
	public static void insertDummyFlushDirective(Node root) {
		for (Node node : root.getInfo().getCFGInfo().getLexicalCFGContents()) {
			if (node instanceof FlushDirective && !(node instanceof DummyFlushDirective)) {
				if (!hasPredDFD(node, DummyFlushType.FLUSH_START)) {
					InsertImmediatePredecessor.insert(node, new DummyFlushDirective(DummyFlushType.FLUSH_START));
					// InsertImmediateSuccessor.insertImmediateSuccessor(node,
					// new DummyFlushDirective(DummyFlushType.FLUSH_END));
				}
			} else if (node instanceof BarrierDirective) {
				if (!hasPredDFD(node, DummyFlushType.BARRIER_START)) {
					InsertImmediatePredecessor.insert(node, new DummyFlushDirective(DummyFlushType.BARRIER_START));
					// InsertImmediateSuccessor.insertImmediateSuccessor(node,
					// new DummyFlushDirective(DummyFlushType.BARRIER_END));
				}
			} else if (node instanceof AtomicConstruct) {
				if (!hasPredDFD(node, DummyFlushType.ATOMIC_START)) {
					InsertImmediatePredecessor.insert(node, new DummyFlushDirective(DummyFlushType.ATOMIC_START));
				}
				if (!hasSuccDFD(node, DummyFlushType.ATOMIC_END)) {
					InsertImmediateSuccessor.insert(node, new DummyFlushDirective(DummyFlushType.ATOMIC_END));
				}
			} else if (node instanceof CriticalConstruct) {
				if (!hasPredDFD(node, DummyFlushType.CRITICAL_START)) {
					InsertImmediatePredecessor.insert(node, new DummyFlushDirective(DummyFlushType.CRITICAL_START));
				}
				if (!hasSuccDFD(node, DummyFlushType.CRITICAL_END)) {
					InsertImmediateSuccessor.insert(node, new DummyFlushDirective(DummyFlushType.CRITICAL_END));
				}
			} else if (node instanceof OrderedConstruct) {
				if (!hasPredDFD(node, DummyFlushType.ORDERED_START)) {
					InsertImmediatePredecessor.insert(node, new DummyFlushDirective(DummyFlushType.ORDERED_START));
				}
				if (!hasSuccDFD(node, DummyFlushType.ORDERED_END)) {
					InsertImmediateSuccessor.insert(node, new DummyFlushDirective(DummyFlushType.ORDERED_END));
				}
			} else if (node instanceof CallStatement) {
				CallStatement callStmt = (CallStatement) node;
				if (callStmt.getInfo().isALockModifyRoutine()) {
					if (!hasPredDFD(node, DummyFlushType.LOCK_MODIFY_START)) {
						InsertImmediatePredecessor.insert(node,
								new DummyFlushDirective(DummyFlushType.LOCK_MODIFY_START));
					}
					if (!hasSuccDFD(node, DummyFlushType.LOCK_MODIFY_END)) {
						InsertImmediateSuccessor.insert(node, new DummyFlushDirective(DummyFlushType.LOCK_MODIFY_END));
					}
				} else if (callStmt.getInfo().isALockWriteRoutine()) {
					if (!hasSuccDFD(node, DummyFlushType.LOCK_WRITE_END)) {
						InsertImmediateSuccessor.insert(node, new DummyFlushDirective(DummyFlushType.LOCK_WRITE_END));
					}
				}
			} else if (node instanceof TaskConstruct) {
				if (!hasPredDFD(node, DummyFlushType.TASK_START)) {
					InsertImmediatePredecessor.insert(node, new DummyFlushDirective(DummyFlushType.TASK_START));
				}
				if (!hasSuccDFD(node, DummyFlushType.TASK_END)) {
					InsertImmediateSuccessor.insert(node, new DummyFlushDirective(DummyFlushType.TASK_END));
				}
			} else if (node instanceof TaskyieldDirective) {
				if (!hasPredDFD(node, DummyFlushType.TASKYIELD_START)) {
					InsertImmediatePredecessor.insert(node, new DummyFlushDirective(DummyFlushType.TASKYIELD_START));
				}
			} else if (node instanceof TaskwaitDirective) {
				if (!hasPredDFD(node, DummyFlushType.TASKWAIT_START)) {
					InsertImmediatePredecessor.insert(node, new DummyFlushDirective(DummyFlushType.TASKWAIT_START));
				}
			}
		}
	}

	public static DummyFlushDirective getPredDFD(Node node, DummyFlushType dfType) {
		for (Node pred : node.getInfo().getCFGInfo().getPredecessors()) {
			if (pred instanceof DummyFlushDirective) {
				DummyFlushDirective dfd = (DummyFlushDirective) pred;
				if (dfd.getDummyFlushType() == dfType) {
					return dfd;
				}
			}
		}
		return null;
	}

	public static DummyFlushDirective getSuccDFD(Node node, DummyFlushType dfType) {
		for (Node succ : node.getInfo().getCFGInfo().getSuccessors()) {
			if (succ instanceof DummyFlushDirective) {
				DummyFlushDirective dfd = (DummyFlushDirective) succ;
				if (dfd.getDummyFlushType() == dfType) {
					return dfd;
				}
			}
		}
		return null;
	}

	public static boolean hasPredDFD(Node node, DummyFlushType dfType) {
		for (Node pred : node.getInfo().getCFGInfo().getPredecessors()) {
			if (pred instanceof DummyFlushDirective) {
				DummyFlushDirective dfd = (DummyFlushDirective) pred;
				if (dfd.getDummyFlushType() == dfType) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean hasSuccDFD(Node node, DummyFlushType dfType) {
		for (Node succ : node.getInfo().getCFGInfo().getSuccessors()) {
			if (succ instanceof DummyFlushDirective) {
				DummyFlushDirective dfd = (DummyFlushDirective) succ;
				if (dfd.getDummyFlushType() == dfType) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method looks at the {@code node} to see if it requires any addition
	 * of a DFD immediately due to itself.
	 * 
	 * @param node
	 * @return
	 */
	public static boolean requiresNewDFDBeforeAddition(Node node) {
		if (node instanceof FlushDirective && !(node instanceof DummyFlushDirective)) {
			if (!hasPredDFD(node, DummyFlushType.FLUSH_START)) {
				return true;
			}
		} else if (node instanceof BarrierDirective) {
			if (!hasPredDFD(node, DummyFlushType.BARRIER_START)) {
				return true;
			}
		} else if (node instanceof AtomicConstruct) {
			if (!hasPredDFD(node, DummyFlushType.ATOMIC_START)) {
				return true;
			}
			if (!hasSuccDFD(node, DummyFlushType.ATOMIC_END)) {
				return true;
			}
		} else if (node instanceof CriticalConstruct) {
			if (!hasPredDFD(node, DummyFlushType.CRITICAL_START)) {
				return true;
			}
			if (!hasSuccDFD(node, DummyFlushType.CRITICAL_END)) {
				return true;
			}
		} else if (node instanceof OrderedConstruct) {
			if (!hasPredDFD(node, DummyFlushType.ORDERED_START)) {
				return true;
			}
			if (!hasSuccDFD(node, DummyFlushType.ORDERED_END)) {
				return true;
			}
		} else if (node instanceof CallStatement) {
			CallStatement callStmt = (CallStatement) node;
			if (callStmt.getInfo().isALockModifyRoutine()) {
				if (!hasPredDFD(node, DummyFlushType.LOCK_MODIFY_START)) {
					return true;
				}
				if (!hasSuccDFD(node, DummyFlushType.LOCK_MODIFY_END)) {
					return true;
				}
			} else if (callStmt.getInfo().isALockWriteRoutine()) {
				if (!hasSuccDFD(node, DummyFlushType.LOCK_WRITE_END)) {
					return true;
				}
			}
		} else if (node instanceof TaskConstruct) {
			if (!hasPredDFD(node, DummyFlushType.TASK_START)) {
				return true;
			}
			if (!hasSuccDFD(node, DummyFlushType.TASK_END)) {
				return true;
			}
		} else if (node instanceof TaskyieldDirective) {
			if (!hasPredDFD(node, DummyFlushType.TASKYIELD_START)) {
				return true;
			}
		} else if (node instanceof TaskwaitDirective) {
			if (!hasPredDFD(node, DummyFlushType.TASKWAIT_START)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method looks at the {@code node} to see if we need removal of
	 * certain surrounding DFDs, when the {@code node} is expected to be
	 * removed.
	 * 
	 * @param node
	 * @return
	 */
	public static boolean requiresRemovalOfDFDs(Node node) {
		if (node instanceof FlushDirective && !(node instanceof DummyFlushDirective)) {
			if (hasPredDFD(node, DummyFlushType.FLUSH_START)) {
				return true;
			}
		} else if (node instanceof BarrierDirective) {
			if (hasPredDFD(node, DummyFlushType.BARRIER_START)) {
				return true;
			}
		} else if (node instanceof AtomicConstruct) {
			if (!hasPredDFD(node, DummyFlushType.ATOMIC_START)) {
				return true;
			}
			if (hasSuccDFD(node, DummyFlushType.ATOMIC_END)) {
				return true;
			}
		} else if (node instanceof CriticalConstruct) {
			if (hasPredDFD(node, DummyFlushType.CRITICAL_START)) {
				return true;
			}
			if (hasSuccDFD(node, DummyFlushType.CRITICAL_END)) {
				return true;
			}
		} else if (node instanceof OrderedConstruct) {
			if (hasPredDFD(node, DummyFlushType.ORDERED_START)) {
				return true;
			}
			if (hasSuccDFD(node, DummyFlushType.ORDERED_END)) {
				return true;
			}
		} else if (node instanceof CallStatement) {
			CallStatement callStmt = (CallStatement) node;
			if (callStmt.getInfo().isALockModifyRoutine()) {
				if (hasPredDFD(node, DummyFlushType.LOCK_MODIFY_START)) {
					return true;
				}
				if (hasSuccDFD(node, DummyFlushType.LOCK_MODIFY_END)) {
					return true;
				}
			} else if (callStmt.getInfo().isALockWriteRoutine()) {
				if (hasSuccDFD(node, DummyFlushType.LOCK_WRITE_END)) {
					return true;
				}
			}
		} else if (node instanceof TaskConstruct) {
			if (hasPredDFD(node, DummyFlushType.TASK_START)) {
				return true;
			}
			if (hasSuccDFD(node, DummyFlushType.TASK_END)) {
				return true;
			}
		} else if (node instanceof TaskyieldDirective) {
			if (hasPredDFD(node, DummyFlushType.TASKYIELD_START)) {
				return true;
			}
		} else if (node instanceof TaskwaitDirective) {
			if (hasPredDFD(node, DummyFlushType.TASKWAIT_START)) {
				return true;
			}
		}
		return false;
	}

}
