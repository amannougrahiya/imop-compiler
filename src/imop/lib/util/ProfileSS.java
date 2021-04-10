/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import java.util.HashSet;
import java.util.Set;

import imop.parser.Program;

/**
 * Class that contains profiling info done for self-stabilization ease-of-use
 * evaluations.
 */
public class ProfileSS {
	public static Set<Integer> ptaSet = new HashSet<>();
	public static Set<Integer> cfgSet = new HashSet<>();
	public static Set<Integer> cgSet = new HashSet<>();
	public static Set<Integer> iteSet = new HashSet<>();
	public static Set<Integer> symSet = new HashSet<>();
	public static Set<Integer> labSet = new HashSet<>();
	public static Set<Integer> phSet = new HashSet<>();
	public static int currentCP = -1;

	private static boolean flagRaised = false;
	public static int flagSwitchCount = 0;

	/**
	 * This method is placed immediately after a change-point, and is used to
	 * update currentCP.
	 */
	public static void insertCP() {
		if (!Program.addRelCPs) {
			return;
		}
		StackTraceElement st = Thread.currentThread().getStackTrace()[2];
		currentCP = st.getLineNumber() - 1;
		flagRaised = true;
	}

	/**
	 * This method adds the currentCP as an active CP to the given set.
	 *
	 * @param set
	 */
	public static void addRelevantChangePoint(Set<Integer> set) {
		if (!Program.addRelCPs) {
			return;
		}
		if (currentCP != -1) {
			set.add(currentCP);
		}
		if (set == ptaSet) {
			if (flagRaised) {
				flagRaised = false;
				flagSwitchCount++;
			}
		}
	}

}