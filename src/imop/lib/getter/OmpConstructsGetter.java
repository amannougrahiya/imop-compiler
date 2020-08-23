/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */

package imop.lib.getter;

import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstVisitor;

import java.util.Vector;

/**
 * Populates ompList with references to the outermost OmpConstructs
 */
public class OmpConstructsGetter extends DepthFirstVisitor {

	public Vector<OmpConstruct> ompList = new Vector<>();

	/**
	 * f0 ::= ParallelConstruct()
	 * | ForConstruct()
	 * | SectionsConstruct()
	 * | SingleConstruct()
	 * | ParallelForConstruct()
	 * | ParallelSectionsConstruct()
	 * | TaskConstruct()
	 * | MasterConstruct()
	 * | CriticalConstruct()
	 * | AtomicConstruct()
	 * | OrderedConstruct()
	 */
	@Override
	public void visit(OmpConstruct n) {
		ompList.add(n);
		// Not calling the visits on internal constructs.
	}

}
