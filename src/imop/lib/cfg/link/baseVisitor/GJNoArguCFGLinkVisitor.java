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

public class GJNoArguCFGLinkVisitor<R> {
	public R initProcess(CFGLink link) {
		R ret = null;
		return ret;
	}

	public R endProcess(CFGLink link) {
		R ret = null;
		return ret;
	}

	public R visit(FunctionBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(FunctionParameterLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(FunctionBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(FunctionEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(ParallelBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(ParallelClauseLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(ParallelBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(ParallelEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(OmpForBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(OmpForInitLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(OmpForTermLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(OmpForStepLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(OmpForBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(OmpForEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(SectionsBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(SectionsSectionBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(SectionsEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(SingleBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(SingleBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(SingleEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(TaskBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(TaskClauseLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(TaskBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(TaskEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(MasterBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(MasterBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(MasterEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(CriticalBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(CriticalBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(CriticalEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(AtomicBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(AtomicStatementLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(AtomicEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(OrderedBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(OrderedBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(OrderedEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(CompoundBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(CompoundElementLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(CompoundEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(IfBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(IfPredicateLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(IfThenBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(IfElseBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(IfEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(SwitchBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(SwitchPredicateLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(SwitchBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(SwitchEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(WhileBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(WhilePredicateLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(WhileBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(WhileEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(DoBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(DoPredicateLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(DoBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(DoEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(ForBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(ForInitLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(ForTermLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(ForStepLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(ForBodyLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(ForEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(CallBeginLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(CallPreLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(CallPostLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(CallEndLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(GotoLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(BreakLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(ContinueLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}

	public R visit(ReturnLink link) {
		R ret = null;
		initProcess(link);
		endProcess(link);
		return ret;
	}
}
