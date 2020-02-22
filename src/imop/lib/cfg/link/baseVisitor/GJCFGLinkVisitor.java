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

public class GJCFGLinkVisitor<R, A> {
	public R initProcess(CFGLink link, A argu) {
		R ret = null;
		return ret;
	}

	public R endProcess(CFGLink link, A argu) {
		R ret = null;
		return ret;
	}

	public R visit(FunctionBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(FunctionParameterLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(FunctionBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(FunctionEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(ParallelBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(ParallelClauseLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(ParallelBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(ParallelEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(OmpForBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(OmpForInitLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(OmpForTermLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(OmpForStepLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(OmpForBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(OmpForEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(SectionsBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(SectionsSectionBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(SectionsEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(SingleBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(SingleBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(SingleEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(TaskBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(TaskClauseLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(TaskBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(TaskEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(MasterBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(MasterBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(MasterEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(CriticalBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(CriticalBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(CriticalEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(AtomicBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(AtomicStatementLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(AtomicEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(OrderedBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(OrderedBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(OrderedEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(CompoundBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(CompoundElementLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(CompoundEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(IfBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(IfPredicateLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(IfThenBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(IfElseBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(IfEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(SwitchBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(SwitchPredicateLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(SwitchBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(SwitchEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(WhileBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(WhilePredicateLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(WhileBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(WhileEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(DoBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(DoPredicateLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(DoBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(DoEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(ForBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(ForInitLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(ForTermLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(ForStepLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(ForBodyLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(ForEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(CallBeginLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(CallPreLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(CallPostLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(CallEndLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(GotoLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(BreakLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(ContinueLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}

	public R visit(ReturnLink link, A argu) {
		R ret = null;
		initProcess(link, argu);
		endProcess(link, argu);
		return ret;
	}
}
