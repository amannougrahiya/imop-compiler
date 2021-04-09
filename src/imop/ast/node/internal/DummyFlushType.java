/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.node.internal;

public enum DummyFlushType {
	FLUSH_START, BARRIER_START, BARRIER_END, CRITICAL_START, CRITICAL_END, ATOMIC_START, ATOMIC_END, ORDERED_START,
	ORDERED_END, LOCK_MODIFY_START, LOCK_MODIFY_END, LOCK_WRITE_END, TASK_START, TASK_END, TASKWAIT_START,
	TASKYIELD_START
}
