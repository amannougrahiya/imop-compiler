/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import java.util.Set;

import imop.ast.info.OmpConstructInfo;
import imop.ast.node.external.AUniqueForOrDataOrNowaitClause;
import imop.ast.node.external.ForConstruct;
import imop.ast.node.external.Node;
import imop.ast.node.external.NodeChoice;
import imop.ast.node.external.NodeToken;
import imop.ast.node.external.NowaitClause;
import imop.ast.node.external.ScheduleKind;
import imop.ast.node.external.UniqueForClause;
import imop.ast.node.internal.OmpClause;
import imop.lib.cfg.info.ForConstructCFGInfo;
import imop.lib.util.Misc;
import imop.parser.CParserConstants;
import imop.parser.FrontEnd;

public class ForConstructInfo extends OmpConstructInfo {

	public static enum SchedulingType {
		STATIC, DYNAMIC, GUIDED, RUNTIME
	}

	public ForConstructInfo(Node owner) {
		super(owner);
	}

	public void addNowaitClause() {
		NowaitClause clause = FrontEnd.parseAndNormalize("nowait", NowaitClause.class);
		if (this.hasNowaitClause()) {
			return;
		}
		ForConstruct forCons = (ForConstruct) this.getNode();
		AUniqueForOrDataOrNowaitClause wrapper = new AUniqueForOrDataOrNowaitClause(new NodeChoice(clause));
		forCons.getF1().getF1().getF0().addNode(wrapper);
	}

	@Override
	public ForConstructCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new ForConstructCFGInfo(getNode());
		}
		return (ForConstructCFGInfo) cfgInfo;
	}

	public SchedulingType getSchedulingType() {
		for (OmpClause clause : this.getOmpClauseList()) {
			if (clause instanceof UniqueForClause) {
				UniqueForClause uniqueForClause = (UniqueForClause) clause;
				Set<ScheduleKind> schedSet = Misc.getInheritedEnclosee(uniqueForClause, ScheduleKind.class);
				if (schedSet == null || schedSet.size() != 1) {
					continue;
				}
				ScheduleKind sched = Misc.getAnyElement(schedSet);
				NodeToken nodeToken = (NodeToken) sched.getF0().getChoice();
				if (nodeToken.getKind() == CParserConstants.STATIC) {
					return SchedulingType.STATIC;
				} else if (nodeToken.getKind() == CParserConstants.DYNAMIC) {
					return SchedulingType.DYNAMIC;
				} else if (nodeToken.getKind() == CParserConstants.RUNTIME) {
					return SchedulingType.RUNTIME;
				} else if (nodeToken.getKind() == CParserConstants.GUIDED) {
					return SchedulingType.GUIDED;
				}
			}
		}
		return SchedulingType.STATIC;
	}

}
