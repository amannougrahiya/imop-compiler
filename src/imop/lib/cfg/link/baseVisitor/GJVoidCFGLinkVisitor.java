/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cfg.link.baseVisitor;

import imop.lib.cfg.link.node.AtomicBeginLink;
import imop.lib.cfg.link.node.AtomicEndLink;
import imop.lib.cfg.link.node.AtomicStatementLink;
import imop.lib.cfg.link.node.BreakLink;
import imop.lib.cfg.link.node.CFGLink;
import imop.lib.cfg.link.node.CallBeginLink;
import imop.lib.cfg.link.node.CallEndLink;
import imop.lib.cfg.link.node.CallPostLink;
import imop.lib.cfg.link.node.CallPreLink;
import imop.lib.cfg.link.node.CompoundBeginLink;
import imop.lib.cfg.link.node.CompoundElementLink;
import imop.lib.cfg.link.node.CompoundEndLink;
import imop.lib.cfg.link.node.ContinueLink;
import imop.lib.cfg.link.node.CriticalBeginLink;
import imop.lib.cfg.link.node.CriticalBodyLink;
import imop.lib.cfg.link.node.CriticalEndLink;
import imop.lib.cfg.link.node.DoBeginLink;
import imop.lib.cfg.link.node.DoBodyLink;
import imop.lib.cfg.link.node.DoEndLink;
import imop.lib.cfg.link.node.DoPredicateLink;
import imop.lib.cfg.link.node.ForBeginLink;
import imop.lib.cfg.link.node.ForBodyLink;
import imop.lib.cfg.link.node.ForEndLink;
import imop.lib.cfg.link.node.ForInitLink;
import imop.lib.cfg.link.node.ForStepLink;
import imop.lib.cfg.link.node.ForTermLink;
import imop.lib.cfg.link.node.FunctionBeginLink;
import imop.lib.cfg.link.node.FunctionBodyLink;
import imop.lib.cfg.link.node.FunctionEndLink;
import imop.lib.cfg.link.node.FunctionParameterLink;
import imop.lib.cfg.link.node.GotoLink;
import imop.lib.cfg.link.node.IfBeginLink;
import imop.lib.cfg.link.node.IfElseBodyLink;
import imop.lib.cfg.link.node.IfEndLink;
import imop.lib.cfg.link.node.IfPredicateLink;
import imop.lib.cfg.link.node.IfThenBodyLink;
import imop.lib.cfg.link.node.MasterBeginLink;
import imop.lib.cfg.link.node.MasterBodyLink;
import imop.lib.cfg.link.node.MasterEndLink;
import imop.lib.cfg.link.node.OmpForBeginLink;
import imop.lib.cfg.link.node.OmpForBodyLink;
import imop.lib.cfg.link.node.OmpForEndLink;
import imop.lib.cfg.link.node.OmpForInitLink;
import imop.lib.cfg.link.node.OmpForStepLink;
import imop.lib.cfg.link.node.OmpForTermLink;
import imop.lib.cfg.link.node.OrderedBeginLink;
import imop.lib.cfg.link.node.OrderedBodyLink;
import imop.lib.cfg.link.node.OrderedEndLink;
import imop.lib.cfg.link.node.ParallelBeginLink;
import imop.lib.cfg.link.node.ParallelBodyLink;
import imop.lib.cfg.link.node.ParallelClauseLink;
import imop.lib.cfg.link.node.ParallelEndLink;
import imop.lib.cfg.link.node.ReturnLink;
import imop.lib.cfg.link.node.SectionsBeginLink;
import imop.lib.cfg.link.node.SectionsEndLink;
import imop.lib.cfg.link.node.SectionsSectionBodyLink;
import imop.lib.cfg.link.node.SingleBeginLink;
import imop.lib.cfg.link.node.SingleBodyLink;
import imop.lib.cfg.link.node.SingleEndLink;
import imop.lib.cfg.link.node.SwitchBeginLink;
import imop.lib.cfg.link.node.SwitchBodyLink;
import imop.lib.cfg.link.node.SwitchEndLink;
import imop.lib.cfg.link.node.SwitchPredicateLink;
import imop.lib.cfg.link.node.TaskBeginLink;
import imop.lib.cfg.link.node.TaskBodyLink;
import imop.lib.cfg.link.node.TaskClauseLink;
import imop.lib.cfg.link.node.TaskEndLink;
import imop.lib.cfg.link.node.WhileBeginLink;
import imop.lib.cfg.link.node.WhileBodyLink;
import imop.lib.cfg.link.node.WhileEndLink;
import imop.lib.cfg.link.node.WhilePredicateLink;

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
