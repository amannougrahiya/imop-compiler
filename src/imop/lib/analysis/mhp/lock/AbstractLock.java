/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp.lock;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractLock {
	protected static Set<AbstractLock> allLocks = new HashSet<>();

	public Set<AbstractLock> getAllLockSetCopy() {
		return new HashSet<>(allLocks);
	}

	public static void resetStaticFields() {
		AbstractLock.allLocks = new HashSet<>();
	}

	@Override
	public abstract boolean equals(Object other);

	@Override
	public abstract int hashCode();

}
