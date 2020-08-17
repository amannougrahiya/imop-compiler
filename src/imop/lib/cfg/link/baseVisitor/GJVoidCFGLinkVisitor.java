/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.link.baseVisitor;

import imop.lib.cfg.link.node.*;

public class GJVoidCFGLinkVisitor<A> {
	public void initProcess(CFGLink link, A argu) {
	}

	public void endProcess(CFGLink link, A argu) {
	}

	public void visit(FunctionBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(FunctionParameterLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(FunctionBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(FunctionEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(ParallelBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(ParallelClauseLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(ParallelBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(ParallelEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(OmpForBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(OmpForInitLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(OmpForTermLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(OmpForStepLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(OmpForBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(OmpForEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(SectionsBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(SectionsSectionBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(SectionsEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(SingleBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(SingleBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(SingleEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(TaskBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(TaskClauseLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(TaskBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(TaskEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(MasterBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(MasterBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(MasterEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(CriticalBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(CriticalBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(CriticalEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(AtomicBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(AtomicStatementLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(AtomicEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(OrderedBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(OrderedBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(OrderedEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(CompoundBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(CompoundElementLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(CompoundEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(IfBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(IfPredicateLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(IfThenBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(IfElseBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(IfEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(SwitchBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(SwitchPredicateLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(SwitchBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(SwitchEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(WhileBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(WhilePredicateLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(WhileBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(WhileEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(DoBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(DoPredicateLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(DoBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(DoEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(ForBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(ForInitLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(ForTermLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(ForStepLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(ForBodyLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(ForEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(CallBeginLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(CallPreLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(CallPostLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(CallEndLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(GotoLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(BreakLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(ContinueLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}

	public void visit(ReturnLink link, A argu) {
		initProcess(link, argu);
		endProcess(link, argu);
	}
}
