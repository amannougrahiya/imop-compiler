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

public class CFGLinkVisitor {
	public void initProcess(CFGLink link) {
	}

	public void endProcess(CFGLink link) {
	}

	public void visit(FunctionBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(FunctionParameterLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(FunctionBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(FunctionEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(ParallelBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(ParallelClauseLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(ParallelBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(ParallelEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(OmpForBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(OmpForInitLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(OmpForTermLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(OmpForStepLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(OmpForBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(OmpForEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(SectionsBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(SectionsSectionBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(SectionsEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(SingleBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(SingleBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(SingleEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(TaskBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(TaskClauseLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(TaskBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(TaskEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(MasterBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(MasterBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(MasterEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(CriticalBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(CriticalBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(CriticalEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(AtomicBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(AtomicStatementLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(AtomicEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(OrderedBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(OrderedBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(OrderedEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(CompoundBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(CompoundElementLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(CompoundEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(IfBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(IfPredicateLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(IfThenBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(IfElseBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(IfEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(SwitchBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(SwitchPredicateLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(SwitchBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(SwitchEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(WhileBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(WhilePredicateLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(WhileBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(WhileEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(DoBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(DoPredicateLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(DoBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(DoEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(ForBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(ForInitLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(ForTermLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(ForStepLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(ForBodyLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(ForEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(CallBeginLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(CallPreLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(CallPostLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(CallEndLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(GotoLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(BreakLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(ContinueLink link) {
		initProcess(link);
		endProcess(link);
	}

	public void visit(ReturnLink link) {
		initProcess(link);
		endProcess(link);
	}
}
