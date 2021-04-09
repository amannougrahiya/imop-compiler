/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.cfgTraversals.GJVoidDepthFirstCFG;
import imop.lib.cg.CallSite;
import imop.lib.getter.CallSiteGetter;
import imop.lib.util.Misc;

import java.util.List;

/**
 * When called on the beginNode of a ParallelConstruct, this visitor
 * traverses the construct's region and marks the enclosed CFG nodes with the
 * argument $region$.
 */
@Deprecated
public class ParallelRegionMarker extends GJVoidDepthFirstCFG<ParallelConstruct> {

	/**
	 * This overridden method marks n with "region".
	 * Not called in ParallelConstructs
	 */
	@Override
	public void initProcess(Node n, ParallelConstruct region) {

		// If n is not a CFG node, terminate the program.
		if (!Misc.isCFGNode(n)) {
			System.out.println("Error: Regions are not applicable to Non-CFG nodes");
			System.exit(1);
		}

		if (!n.getInfo().isRunnableInRegion(region)) { // If n isn't already marked,
			n.getInfo().getRegionInfo().add(region); // mark it
			if (Misc.isCFGNonLeafNode(n)) { // and if n is a non-leaf CFG node, traverse inside it
				n.getInfo().getCFGInfo().getNestedCFG().getBegin().accept(this, region);
			}
		} else {
			System.out.println("Region-marking visit called on an already marked Node!");
			System.exit(1);
		}
		return;
	}

	/**
	 * This method is used to call ParallelRegionMarker on internal regions.
	 * 
	 * @param n
	 */
	public void initParallelRegion(ParallelConstruct parConstruct) {
		// Call ParallelRegionMarker on parConstruct's beginNode,
		// IFF this hasn't been done already (possible in case of recursion)
		Node beginNode = parConstruct.getInfo().getCFGInfo().getNestedCFG().getBegin();
		if (beginNode.getInfo().isRunnableInRegion(parConstruct)) {
			return;
		}

		// If not already present, add parConstruct to the regionInfo field of its
		// statements.
		beginNode.accept(new ParallelRegionMarker(), parConstruct);
	}

	/**
	 * This method calls the processing on successors of n.
	 */
	@Override
	public void endProcess(Node n, ParallelConstruct region) {
		for (Node succ : n.getInfo().getCFGInfo().getSuccBlocks()) {
			if (succ.getInfo().isRunnableInRegion(region)) {
				continue;
			}
			succ.accept(this, region); // This may be a BeginNode, EndNode, other Leaf node or a Non-Leaf node.
		}
		return;
	}

	/**
	 * Mark called functions as well.
	 * 
	 * @Override
	 */
	@Override
	public void processCalls(Node n, ParallelConstruct region) {
		// Find all the Function Calls in n, and mark the callee's bodies with region.
		CallSiteGetter callSiteGetter = new CallSiteGetter();
		n.accept(callSiteGetter);
		List<CallSite> callSiteList = callSiteGetter.callSiteList;
		for (CallSite cs : callSiteList) {
			// If cs has a body, call region-marking on that, if not already marked.
			if (cs.calleeDefinition != null) {
				Node beginNode = cs.calleeDefinition.getInfo().getCFGInfo().getNestedCFG().getBegin();
				if (beginNode.getInfo().isRunnableInRegion(region)) {
					continue;
				}
				beginNode.accept(this, region);
			}
		}
	}

	@Override
	public void visit(EndNode n, ParallelConstruct region) {
		initProcess(n, region);
		// No endProcess needed for EndNode as it has no successors.
	}

	/**
	 * f0 ::= OmpPragma()
	 * f1 ::= ParallelDirective()
	 * f2 ::= Statement()
	 */
	@Override
	public void visit(ParallelConstruct n, ParallelConstruct region) {
		initParallelRegion(n);
		processCalls(n.getParConsF1(), region);
		endProcess(n, region);
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
	public void visit(ParallelForConstruct n, ParallelConstruct region) {
		initParallelRegion(n);
		processCalls(n.getF3(), region);
		endProcess(n, region);
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
	public void visit(ParallelSectionsConstruct n, ParallelConstruct region) {
		initParallelRegion(n);
		processCalls(n.getF3(), region);
		endProcess(n, region);
	}

}
