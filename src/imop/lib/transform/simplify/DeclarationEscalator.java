/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.simplify;

import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.info.CompoundStatementCFGInfo;
import imop.lib.cfg.link.baseVisitor.CFGLinkVisitor;
import imop.lib.cfg.link.node.*;
import imop.lib.transform.updater.InsertImmediatePredecessor;
import imop.lib.transform.updater.InsertImmediateSuccessor;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DeclarationEscalator {
	/**
	 * Given a compound-statement, this method pushes all declarations from the
	 * level of this body to enclosing scope, if any.
	 * NOTE: This class is not yet fully implemented. Currently, it supports
	 * escalation only for the body of a while-statement.
	 * 
	 * @param body
	 *             a compound-statement from which all top level declarations
	 *             should be moved outside.
	 */
	public static void pushAllDeclarationsUpFromLevel(CompoundStatement body) {
		CFGLink bodyLink = CFGLinkFinder.getCFGLinkFor(body);
		bodyLink.accept(new DeclarationEscalatorVisitor());
	}

	private static class DeclarationEscalatorVisitor extends CFGLinkVisitor {

		@Override
		public void visit(FunctionBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(ParallelBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(OmpForBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(SectionsSectionBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(SingleBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(TaskBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(MasterBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(CriticalBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(AtomicStatementLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(OrderedBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(CompoundElementLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(IfThenBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(IfElseBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(SwitchBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(WhileBodyLink link) {
			WhileStatement whileStmt = link.enclosingNonLeafNode;
			CompoundStatement body = (CompoundStatement) link.childNode;
			CompoundStatementCFGInfo compInfo = body.getInfo().getCFGInfo();

			/*
			 * Step A: Obtain the list of all top-level declarations in the
			 * body.
			 */
			List<Node> elementList = compInfo.getElementList();
			List<Node> topLevelDeclList = elementList.stream().filter(d -> d instanceof Declaration)
					.collect(Collectors.toCollection(ArrayList::new));

			/*
			 * Step B: Collect the list of declarations that cannot be moved
			 * outside due to their nature (array-inits, static, more than one
			 * declarator, etc.).
			 */
			List<Node> disallowedDeclList = new ArrayList<>();
			for (Node declNode : topLevelDeclList) {
				Declaration decl = (Declaration) declNode;
				if (decl.toString().contains("static")) {
					// This is a fairly conservative check, since we do not model the type
					// qualifiers semantically.
					disallowedDeclList.add(decl);
					continue;
				}
				if (decl.getInfo().getIDNameList().size() > 1) {
					System.err.println("Cannot move declaration that declares more than one symbol: " + decl);
					disallowedDeclList.add(decl);
					continue;
				}
				if (decl.getInfo().hasInitializer()) {
					Initializer init = decl.getInfo().getInitializer();
					if (!Misc.getInheritedEnclosee(init, ArrayInitializer.class).isEmpty()) {
						System.err.println("Cannot move declaration that uses an array initializer: " + decl);
						disallowedDeclList.add(decl);
						continue;
					}
				}
			}

			/*
			 * Step C: Mark all those declarations as disallowed that can create
			 * namespace collision if pushed outside.
			 */
			CompoundStatement enclosingCS = (CompoundStatement) Misc.getEnclosingBlock(whileStmt);
			List<Node> enclosingList = enclosingCS.getInfo().getCFGInfo().getElementList();
			Set<String> usedCellNames = new HashSet<>();
			for (int i = 0; i < enclosingList.size(); i++) {
				Node outerElement = enclosingList.get(i);
				if (outerElement == whileStmt) {
					continue;
				} else if (outerElement instanceof Declaration) {
					Declaration outerDecl = (Declaration) outerElement;
					usedCellNames.add(outerDecl.getInfo().getDeclaredName());
				}
				usedCellNames.addAll(outerElement.getInfo().getUsedCells().getReadOnlyInternal().stream()
						.filter(c -> c instanceof Symbol).map(s -> {
							return ((Symbol) s).getName();
						}).collect(Collectors.toSet()));
			}
			for (Node delcNode : topLevelDeclList) {
				Declaration decl = (Declaration) delcNode;
				if (disallowedDeclList.contains(decl)) {
					continue;
				}
				String declaredName = decl.getInfo().getDeclaredName();
				if (usedCellNames.contains(declaredName)) {
					disallowedDeclList.add(decl);
				}
			}

			/*
			 * Step D: For each allowed declaration, add an assignment for each
			 * initializer, if any.
			 * Create the new declaration and insert it immediately before the
			 * whileStmt.
			 */
			topLevelDeclList.removeAll(disallowedDeclList);
			for (Node declNode : topLevelDeclList) {
				Declaration decl = (Declaration) declNode;
				/*
				 * For each initializer, add an assignment statement immediately
				 * after the declaration.
				 */
				if (decl.getInfo().hasInitializer()) {
					String assignStmtString = "";
					assignStmtString += decl.getInfo().getDeclaredName() + " = " + decl.getInfo().getInitializer()
							+ ";";
					Statement assignStmt = FrontEnd.parseAndNormalize(assignStmtString, Statement.class);
					List<SideEffect> sideEffects = InsertImmediateSuccessor.insert(decl, assignStmt);
					if (!Misc.changePerformed(sideEffects)) {
						System.err.println("Could not delegate the initialization for " + decl);
						continue;
					}
				}
				/*
				 * Obtain the new declaration and move it immediately above the
				 * whileStmt.
				 */
				Declaration newDecl;
				if (decl.getInfo().hasInitializer()) {
					String newDeclString = "";
					newDeclString += decl.getF0() + " ";
					String declaredName = decl.getInfo().getDeclaredName();
					Declarator declarator = decl.getInfo().getDeclarator(declaredName);
					newDeclString += declarator + ";";
					newDecl = FrontEnd.parseAndNormalize(newDeclString, Declaration.class);
				} else {
					newDecl = decl; // We will reuse the same declaration.
				}
				compInfo.removeElement(decl);
				List<SideEffect> sideEffects = InsertImmediatePredecessor.insert(whileStmt, newDecl);
				if (!Misc.changePerformed(sideEffects)) {
					Misc.exitDueToError("Unable to add a removed declaration: " + newDecl);
					return;
				}
			}
		}

		@Override
		public void visit(DoBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

		@Override
		public void visit(ForBodyLink link) {
			initProcess(link);
			endProcess(link);
		}

	}

}
