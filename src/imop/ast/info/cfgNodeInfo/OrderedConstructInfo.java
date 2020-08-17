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
import imop.lib.cfg.info.OrderedConstructCFGInfo;

public class OrderedConstructInfo extends OmpConstructInfo {

	public OrderedConstructInfo(Node owner) {
		super(owner);
	}

	@Override
	public OrderedConstructCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new OrderedConstructCFGInfo(getNode());
		}
		return (OrderedConstructCFGInfo) cfgInfo;
	}

}
