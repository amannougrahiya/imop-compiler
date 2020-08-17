/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.OmpConstructInfo;
import imop.ast.node.external.*;
import imop.lib.cfg.info.SectionsConstructCFGInfo;
import imop.parser.FrontEnd;

public class SectionsConstructInfo extends OmpConstructInfo {

	public SectionsConstructInfo(Node owner) {
		super(owner);
	}

	public void addNowaitClause() {
		NowaitClause clause = FrontEnd.parseAndNormalize("nowait", NowaitClause.class);
		if (this.hasNowaitClause()) {
			return;
		}
		SectionsConstruct sectionsCons = (SectionsConstruct) this.getNode();
		ANowaitDataClause wrapper = new ANowaitDataClause(new NodeChoice(clause));
		sectionsCons.getF2().getF0().addNode(wrapper);
	}

	@Override
	public SectionsConstructCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new SectionsConstructCFGInfo(getNode());
		}
		return (SectionsConstructCFGInfo) cfgInfo;
	}

}
