/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import java.util.Vector;

import imop.ast.info.OmpConstructInfo;
import imop.ast.node.external.BarrierDirective;
import imop.ast.node.external.ForConstruct;
import imop.ast.node.external.Node;
import imop.ast.node.external.ParallelConstruct;
import imop.ast.node.external.ParallelForConstruct;
import imop.ast.node.external.ParallelSectionsConstruct;
import imop.ast.node.external.SectionsConstruct;
import imop.ast.node.external.SingleConstruct;
import imop.baseVisitor.DepthFirstProcess;

/**
 * This class populates barrierList with the list of barriers
 * that may be encountered by the current team of threads.
 * This skips all the barriers of inner parallel regions.
 */
public class BarrierGetter extends DepthFirstProcess {
	public Vector<Node> barrierList = new Vector<>();

	@Override
	public void initProcess(Node n) {
		boolean implicitBarrier = false;
		if (n instanceof BarrierDirective) {
			barrierList.add(n);
		} else if (n instanceof SingleConstruct) {
			implicitBarrier = true;
		} else if (n instanceof SectionsConstruct) {
			implicitBarrier = true;
		} else if (n instanceof ForConstruct) {
			implicitBarrier = true;
		}

		if (implicitBarrier) {
			if (!((OmpConstructInfo) n.getInfo()).hasNowaitClause()) {
				barrierList.add(n);
			}
		}
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(ParallelConstruct n) {
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <PARALLEL>
	 * f2 ::= <SECTIONS>
	 * f3 ::= UniqueParallelOrDataClauseList()
	 * f4 ::= OmpEol()
	 * f5 ::= SectionsScope()
	 */
	@Override
	public void visit(ParallelSectionsConstruct n) {
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= <PARALLEL>
	 * f2 ::= <FOR>
	 * f3 ::= UniqueParallelOrUniqueForOrDataClauseList()
	 * f4 ::= OmpEol()
	 * f5 ::= OmpForHeader()
	 * f6 ::= Statement()
	 */
	@Override
	public void visit(ParallelForConstruct n) {
	}

}
