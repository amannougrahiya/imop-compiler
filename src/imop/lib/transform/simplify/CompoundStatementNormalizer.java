/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.simplify;

import imop.ast.annotation.Label;
import imop.ast.node.external.*;
import imop.baseVisitor.GJVoidDepthFirstProcess;
import imop.lib.cfg.info.CompoundStatementCFGInfo;
import imop.lib.transform.updater.sideeffect.*;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides a method to remove extra scoping of nodes in
 * {@code CompoundStatement}s.
 */
public class CompoundStatementNormalizer {

	/**
	 * 
	 * Removes the unnecessary scoping of statements within {@code node},
	 * without resorting to renaming of the nested variables.
	 * <p>
	 * Note: The pair of braces across each single-statement body is preserved.
	 * 
	 * @param node
	 *             input node which might contain more braces than it needs.
	 */
	public static void removeExtraScopes(Node node) {
		if (!node.getInfo().isConnectedToProgram()) {
			/*
			 * For now, we do not handle this case.
			 */
			Thread.dumpStack();
			Misc.warnDueToLackOfFeature(
					"Currently, we do not handle removal of scoping from within disconnected snippets of code.", null);
			return;
		}
		CompoundStatementNormalizationVisitor compNormalizer = new CompoundStatementNormalizationVisitor();
		node.accept(compNormalizer, node.getInfo().getAllSymbolNamesAtNodeExclusively());
	}

	private static class CompoundStatementNormalizationVisitor extends GJVoidDepthFirstProcess<Set<String>> {
		/**
		 * f0 ::= ( ElementsOfTranslation() )+
		 */
		@Override
		public void visit(TranslationUnit n, Set<String> argu) {
			assert (argu.isEmpty());
			Set<String> nameSet = new HashSet<>(n.getInfo().getSymbolTable().keySet());
			n.getF0().accept(this, nameSet);
		}

		/**
		 * f0 ::= ( DeclarationSpecifiers() )?
		 * f1 ::= Declarator()
		 * f2 ::= ( DeclarationList() )?
		 * f3 ::= CompoundStatement()
		 */
		@Override
		public void visit(FunctionDefinition n, Set<String> argu) {
			Set<String> nameSet = new HashSet<>(argu);
			nameSet.addAll(n.getInfo().getSymbolTable().keySet());
			n.getF3().accept(this, nameSet);
		}

		/**
		 * f0 ::= "{"
		 * f1 ::= ( CompoundStatementElement() )*
		 * f2 ::= "}"
		 */
		@Override
		public void visit(CompoundStatement enclosingCS, final Set<String> argu) {
			Set<String> nameSet = new HashSet<>(argu);
			nameSet.addAll(enclosingCS.getInfo().getSymbolTable().keySet());
			enclosingCS.getF1().accept(this, nameSet);

			CompoundStatementCFGInfo enclosingCSInfo = enclosingCS.getInfo().getCFGInfo();
			List<Node> elementList = enclosingCSInfo.getElementList();
			int incrementFromInsertion = 0;
			for (Node element : elementList) {
				if (!(element instanceof CompoundStatement)) {
					continue;
				}
				int traversalIndex = elementList.indexOf(element);
				CompoundStatement nestedCS = (CompoundStatement) element;
				/*
				 * Step 1: Check if it is correct to bring up the elements
				 * of the nestedCS into the current scope.
				 */
				Set<String> nestedCSNameSet = new HashSet<>(nestedCS.getInfo().getSymbolTable().keySet());
				boolean collision = Misc.doIntersect(nestedCSNameSet, nameSet);
				if (collision) {
					continue;
				}
				CompoundStatementCFGInfo nestedCFGInfo = nestedCS.getInfo().getCFGInfo();
				int insertionIndex = traversalIndex + incrementFromInsertion;
				boolean labelsShifted;
				List<Label> labelsOfNestedCS = new ArrayList<>(nestedCS.getInfo().getLabelAnnotations());
				if (labelsOfNestedCS.isEmpty()) {
					labelsShifted = true;
				} else {
					labelsShifted = false;
				}
				for (Node nestedElem : nestedCFGInfo.getElementList()) {
					if (!labelsShifted) {
						if (nestedElem instanceof Statement) {
							int tempInd = 0;
							for (Label label : labelsOfNestedCS) {
								((Statement) nestedElem).getInfo().addLabelAnnotation(tempInd++, label);
							}
							labelsShifted = true;
						} else {
							Statement newStmt = FrontEnd.parseAndNormalize(";", Statement.class);
							newStmt = (Statement) Misc.getCFGNodeFor(newStmt);
							enclosingCSInfo.addElement(insertionIndex++, newStmt);
							incrementFromInsertion++;
							int tempInd = 0;
							for (Label label : labelsOfNestedCS) {
								newStmt.getInfo().addLabelAnnotation(tempInd++, label);
							}
							labelsShifted = true;
						}
					}

					List<SideEffect> sideEffectsOfRemoval = nestedCFGInfo.removeElement(nestedElem);
					if (!Misc.changePerformed(sideEffectsOfRemoval)) {
						/*
						 * What shall we do in this case?
						 * If the side-effect is an UNAUTHORIZED_DFD_UPDATE, we
						 * can ignore the removal of the element.
						 * There won't be any other consequential side-effects
						 * of this removal request.
						 */
						// Do nothing.
					}

					List<SideEffect> sideEffectsOfAddition = enclosingCSInfo.addElement(insertionIndex, nestedElem);
					if (!Misc.changePerformed(sideEffectsOfAddition)) {
						// Do nothing.
					} else {
						insertionIndex++;
						incrementFromInsertion++;
						for (SideEffect nse : sideEffectsOfAddition) {
							if (nse instanceof AddedDFDPredecessor || nse instanceof AddedDFDSuccessor
									|| nse instanceof InitializationSimplified) {
								incrementFromInsertion++;
								insertionIndex++;
							} else if (nse instanceof AddedEnclosingBlock || nse instanceof AddedExplicitBarrier
									|| nse instanceof AddedNowaitClause || nse instanceof RemovedDFDSuccessor
									|| nse instanceof RemovedDFDPredecessor) {
								assert (false);
							} else {
								Misc.exitDueToError("Cannot handle side-effect " + nse.getClass().getSimpleName()
										+ " while normalizing compound-statement.");
							}
						}
					}
				}
				/*
				 * Update the set of names accessible in the enclosing CS,
				 * since after bringing up the symbols from the nested CS.
				 */
				for (String str : nestedCSNameSet) {
					nameSet.add(str);
				}
				enclosingCSInfo.removeElement(nestedCS);
				incrementFromInsertion--;
			}
			// n.getInfo().populateSymbolTable();
			// OLD CODE:::
			// /*
			// * Assuming that the nested elements have been normalized,
			// * now we need to pick each element, and see whether its contents
			// * can be brought into the scope of the current CS.
			// */
			// List<CompoundStatementElement> newList = new ArrayList<>();
			// for (Node elemNode : n.f1.nodes) {
			// CompoundStatementElement elemCSE = (CompoundStatementElement) elemNode;
			// Node element = elemCSE.f0.choice;
			//
			// if (element instanceof Declaration) {
			// newList.add(elemCSE);
			// } else {
			// // In this case, element is of type Statement
			// Node stmtElement = ((Statement) element).f0.choice;
			// if (stmtElement instanceof CompoundStatement) {
			// CompoundStatement csElem = (CompoundStatement) stmtElement;
			//
			// /*
			// * Step 1: Check if it is correct to bring up the elements
			// * of csElem into the current scope.
			// */
			// Set<String> internalNameSet = new
			// HashSet<>(csElem.getInfo().getSymbolTable().keySet());
			// boolean collision = Misc.doIntersect(internalNameSet, nameSet);
			//
			// /*
			// * Step 2: Bring up the elements, if possible.
			// */
			// if (collision) {
			// newList.add(elemCSE);
			// } else {
			// for (Node internalCSENode : csElem.f1.nodes) {
			// newList.add((CompoundStatementElement) internalCSENode);
			// }
			// for (String str : internalNameSet) {
			// nameSet.add(str);
			// }
			// }
			// } else {
			// /*
			// * In this case, stmtElement is of some other type which we
			// * do not wish to disintegrate.
			// */
			// newList.add(elemCSE);
			// }
			// }
			// }
			//
			// List<Node> nodeList = new ArrayList<>();
			// for (CompoundStatementElement cse : newList) {
			// nodeList.add(cse);
			// }
			// n.f1.nodes = nodeList;
			// /*
			// * Re-populate the symbol table of this child CS, so that
			// * the parent CS should read the correct values.
			// */
			// n.getInfo().populateSymbolTable();
		}

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ( InitDeclaratorList() )?
		 * f2 ::= ";"
		 */
		@Override
		public void visit(Declaration n, Set<String> argu) {
		}

		/**
		 * f0 ::= ( Declaration() )+
		 */
		@Override
		public void visit(DeclarationList n, Set<String> argu) {
		}

		/**
		 * f0 ::= "#"
		 * f1 ::= <UNKNOWN_CPP>
		 */
		@Override
		public void visit(UnknownCpp n, Set<String> argu) {
		}

		/**
		 * f0 ::= <OMP_CR>
		 * | <OMP_NL>
		 */
		@Override
		public void visit(OmpEol n, Set<String> argu) {
		}

		/**
		 * f0 ::= BarrierDirective()
		 * | TaskwaitDirective()
		 * | TaskyieldDirective()
		 * | FlushDirective()
		 */
		@Override
		public void visit(OmpDirective n, Set<String> argu) {
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= ParallelDirective()
		 * f2 ::= Statement()
		 */
		@Override
		public void visit(ParallelConstruct n, Set<String> argu) {
			n.getParConsF2().accept(this, argu);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= ForDirective()
		 * f2 ::= OmpForHeader()
		 * f3 ::= Statement()
		 */
		@Override
		public void visit(ForConstruct n, Set<String> argu) {
			n.getF3().accept(this, argu);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <SECTIONS>
		 * f2 ::= NowaitDataClauseList()
		 * f3 ::= OmpEol()
		 * f4 ::= SectionsScope()
		 */
		@Override
		public void visit(SectionsConstruct n, Set<String> argu) {
			n.getF4().accept(this, argu);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <SECTION>
		 * f2 ::= OmpEol()
		 * f3 ::= Statement()
		 */
		@Override
		public void visit(ASection n, Set<String> argu) {
			n.getF3().accept(this, argu);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <SINGLE>
		 * f2 ::= SingleClauseList()
		 * f3 ::= OmpEol()
		 * f4 ::= Statement()
		 */
		@Override
		public void visit(SingleConstruct n, Set<String> argu) {
			n.getF4().accept(this, argu);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <TASK>
		 * f2 ::= ( TaskClause() )*
		 * f3 ::= OmpEol()
		 * f4 ::= Statement()
		 */
		@Override
		public void visit(TaskConstruct n, Set<String> argu) {
			n.getF4().accept(this, argu);
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
		public void visit(ParallelForConstruct n, Set<String> argu) {
			n.getF6().accept(this, argu);
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
		public void visit(ParallelSectionsConstruct n, Set<String> argu) {
			n.getF5().accept(this, argu);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <MASTER>
		 * f2 ::= OmpEol()
		 * f3 ::= Statement()
		 */
		@Override
		public void visit(MasterConstruct n, Set<String> argu) {
			n.getF3().accept(this, argu);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <CRITICAL>
		 * f2 ::= ( RegionPhrase() )?
		 * f3 ::= OmpEol()
		 * f4 ::= Statement()
		 */
		@Override
		public void visit(CriticalConstruct n, Set<String> argu) {
			n.getF4().accept(this, argu);
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <ATOMIC>
		 * f2 ::= ( AtomicClause() )?
		 * f3 ::= OmpEol()
		 * f4 ::= Statement()
		 */
		@Override
		public void visit(AtomicConstruct n, Set<String> argu) {
		}

		/**
		 * f0 ::= OmpPragma()
		 * f1 ::= <ORDERED>
		 * f2 ::= OmpEol()
		 * f3 ::= Statement()
		 */
		@Override
		public void visit(OrderedConstruct n, Set<String> argu) {
			n.getF3().accept(this, argu);
		}

		/**
		 * f0 ::= GotoStatement()
		 * | ContinueStatement()
		 * | BreakStatement()
		 * | ReturnStatement()
		 */
		@Override
		public void visit(JumpStatement n, Set<String> argu) {
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public void visit(Expression n, Set<String> argu) {
		}

	}
}
