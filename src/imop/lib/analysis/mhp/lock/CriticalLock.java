/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp.lock;

import imop.ast.node.external.*;

public class CriticalLock extends AbstractLock {
	private CriticalLock(String regionName) {
		this.regionName = regionName;
	}

	private String regionName;

	public static CriticalLock getCriticalLock(String regionName) {
		CriticalLock retLock = new CriticalLock(regionName);
		if (!AbstractLock.allLocks.contains(retLock)) {
			AbstractLock.allLocks.add(retLock);
		}
		return retLock;
	}

	public static CriticalLock getCriticalLock(CriticalConstruct criticalConstruct) {
		String regionName;
		if (criticalConstruct.getF2().present()) {
			regionName = ((RegionPhrase) criticalConstruct.getF2().getNode()).getF1().getTokenImage();
		} else {
			regionName = "__imop_critical";
		}
		return CriticalLock.getCriticalLock(regionName);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof CriticalLock)) {
			return false;
		}
		CriticalLock that = (CriticalLock) other;
		if (that.regionName.equals(this.regionName)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.regionName == null) ? 0 : this.regionName.hashCode());
		return result;
	}

}
