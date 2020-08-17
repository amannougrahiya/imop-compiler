/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.OmpDirectiveInfo;
import imop.ast.node.external.*;

public class TaskyieldDirectiveInfo extends OmpDirectiveInfo {

	public TaskyieldDirectiveInfo(Node owner) {
		super(owner);
	}

}
