/*
 *   Copyright (c) 2020 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 *   This file is a part of the project IMOP, licensed under the MIT license.
 *   See LICENSE.md for the full text of the license.
 *
 *   The above notice shall be included in all copies or substantial
 *   portions of this file.
 */

package imop.lib.analysis.mhp.yuan;

import imop.ast.info.cfgNodeInfo.ParallelConstructInfo;
import imop.ast.node.external.*;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.YPhasePoint;
import imop.parser.Program;

import java.util.Collection;
import java.util.HashSet;

public class YPhase extends AbstractPhase<YPhasePoint, YPhasePoint> {
	public YPhase(ParallelConstruct owner) {
		this(owner.getInfo());
	}

	public YPhase(ParallelConstructInfo ownerInfo) {
		super((ParallelConstruct) ownerInfo.getNode());
		assert (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP);
		Collection<YPhase> allPhaseList = (Collection<YPhase>) ownerInfo.getAllPhaseList();
		this.succPhases = new HashSet<YPhase>();
		this.predPhases = new HashSet<YPhase>();
		allPhaseList.add(this);
	}
}
