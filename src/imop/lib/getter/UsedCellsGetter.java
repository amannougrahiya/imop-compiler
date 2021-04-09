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
import imop.ast.node.internal.*;
import imop.baseVisitor.GJNoArguDepthFirstProcess;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to obtain the set of l-values (symbols/locations/cells)
 * that may have been accessed within a node.
 * <br>
 * Note that occurrences of a cell within {@code sizeof} operator is also
 * considered as a use.
 * 
 * @author Aman Nougrahiya
 *
 */
public class UsedCellsGetter {
	/**
	 * Obtain the list of cells that may be used lexically within {@code node}.
	 * 
	 * @param node
	 *             node for which the used set of cells needs to be obtained.
	 * @return
	 *         set of cells that may be used lexically within the node
	 *         {@code node}.
	 */
	public static CellSet getUsedCells(Node node) {
		CellSet accessSet = new CellSet();
		Set<Cell> tempAllCells = new HashSet<>(Cell.allCells); // This would not be a CellSet.
		Cell.allCells.clear();
		AccessGetter accessGetter = new AccessGetter();
		for (Node leafContent : node.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
			accessGetter = new AccessGetter();
			CellSet unknownList = leafContent.accept(accessGetter);
			if (unknownList != null) {
				accessSet.addAll(unknownList);
			}
			accessSet.addAll(accessGetter.cellAccessSet);
		}
		Cell.allCells.addAll(tempAllCells);
		return accessSet;
	}

	/**
	 * This visitor is used to obtain the list of symbols or free variables that
	 * are used in the visited expression.
	 * Each visit may add to the list of cells that are used in the expression
	 * being visited, and return the list of symbols/free-variables that the
	 * visited expression itself may represent.
	 * 
	 * @author Aman Nougrahiya
	 *
	 */
	private static class AccessGetter extends GJNoArguDepthFirstProcess<CellSet> {
		public CellSet cellAccessSet = new CellSet();

		/**
		 * Adds non-null symbols and free variables from {@code cellSet} to
		 * {@code cellReadList}.
		 * 
		 * @param cellSet
		 *                list of symbols and free variables that need to be added
		 *                to the list of cells that may have been read in the
		 *                visits.
		 * 
		 */
		public void addAccesses(CellSet cellSet) {
			if (cellSet != null) {
				assert (!cellSet.isUniversal());
				for (Cell cell : cellSet) {
					if (cell != null) {
						cellAccessSet.add(cell);
					}
				}
			}
		}

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ( InitDeclaratorList() )?
		 * f2 ::= ";"
		 */
		@Override
		public CellSet visit(Declaration n) {
			if (n.getInfo().isTypedef()) {
				return null;
			}
			n.getF1().accept(this);
			return null;
		}

		/**
		 * f0 ::= Declarator()
		 * f1 ::= ( "=" Initializer() )?
		 */
		@Override
		public CellSet visit(InitDeclarator n) {
			if (n.getF1().getNode() != null) {
				addAccesses(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
			}
			return null;
		}

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ParameterAbstraction()
		 */
		@Override
		public CellSet visit(ParameterDeclaration n) {
			return null;
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * | ArrayInitializer()
		 */
		@Override
		public CellSet visit(Initializer n) {
			if (n.getF0().getChoice() instanceof AssignmentExpression) {
				AssignmentExpression assignExp = (AssignmentExpression) n.getF0().getChoice();
				if (assignExp.getF0().getChoice() instanceof ConditionalExpression) {
					ConditionalExpression condExp = (ConditionalExpression) assignExp.getF0().getChoice();
					addAccesses(condExp.accept(this));
				}
			} else {
				n.getF0().getChoice().accept(this);
			}
			return null;
		}

		/**
		 * f0 ::= <IF>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public CellSet visit(IfClause n) {
			addAccesses(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <NUM_THREADS>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public CellSet visit(NumThreadsClause n) {
			addAccesses(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= Expression()
		 */
		@Override
		public CellSet visit(OmpForInitExpression n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			cellAccessSet.add(sym);
			addAccesses(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<"
		 * f2 ::= Expression()
		 */
		@Override
		public CellSet visit(OmpForLTCondition n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			cellAccessSet.add(sym);
			addAccesses(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<="
		 * f2 ::= Expression()
		 */
		@Override
		public CellSet visit(OmpForLECondition n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			cellAccessSet.add(sym);
			addAccesses(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">"
		 * f2 ::= Expression()
		 */
		@Override
		public CellSet visit(OmpForGTCondition n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			cellAccessSet.add(sym);
			addAccesses(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">="
		 * f2 ::= Expression()
		 */
		@Override
		public CellSet visit(OmpForGECondition n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			cellAccessSet.add(sym);
			addAccesses(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "++"
		 */
		@Override
		public CellSet visit(PostIncrementId n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			cellAccessSet.add(sym);
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "--"
		 */
		@Override
		public CellSet visit(PostDecrementId n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			cellAccessSet.add(sym);
			return null;
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public CellSet visit(PreIncrementId n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF1());
			cellAccessSet.add(sym);
			return null;
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public CellSet visit(PreDecrementId n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF1());
			cellAccessSet.add(sym);
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "+="
		 * f2 ::= Expression()
		 */
		@Override
		public CellSet visit(ShortAssignPlus n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			cellAccessSet.add(sym);
			addAccesses(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "-="
		 * f2 ::= Expression()
		 */
		@Override
		public CellSet visit(ShortAssignMinus n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			cellAccessSet.add(sym);
			addAccesses(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "+"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public CellSet visit(OmpForAdditive n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			cellAccessSet.add(sym);
			sym = Misc.getSymbolOrFreeEntry(n.getF2());
			cellAccessSet.add(sym);
			addAccesses(n.getF4().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "-"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public CellSet visit(OmpForSubtractive n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			cellAccessSet.add(sym);
			sym = Misc.getSymbolOrFreeEntry(n.getF2());
			cellAccessSet.add(sym);
			addAccesses(n.getF4().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= MultiplicativeExpression()
		 * f3 ::= "+"
		 * f4 ::= <IDENTIFIER>
		 */
		@Override
		public CellSet visit(OmpForMultiplicative n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			cellAccessSet.add(sym);
			sym = Misc.getSymbolOrFreeEntry(n.getF4());
			cellAccessSet.add(sym);
			addAccesses(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <FINAL>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public CellSet visit(FinalClause n) {
			addAccesses(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= ( Expression() )?
		 * f1 ::= ";"
		 */
		@Override
		public CellSet visit(ExpressionStatement n) {
			if (n.getF0().present()) {
				addAccesses(n.getF0().getNode().accept(this));
			}
			return null;
		}

		/**
		 * f0 ::= <RETURN>
		 * f1 ::= ( Expression() )?
		 * f2 ::= ";"
		 */
		@Override
		public CellSet visit(ReturnStatement n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public CellSet visit(Expression n) {
			if (n.getExpF1().getNodes().isEmpty()) {
				return n.getExpF0().accept(this);
			} else {
				assert (false); // ExpressionSimplification should have removed the comma operator.
				addAccesses(n.getExpF0().accept(this));
				for (Node seq : n.getExpF1().getNodes()) {
					assert seq instanceof NodeSequence;
					AssignmentExpression aE = (AssignmentExpression) ((NodeSequence) seq).getNodes().get(1);
					addAccesses(aE.accept(this));
				}
				return null;
			}
		}

		/**
		 * f0 ::= NonConditionalExpression()
		 * | ConditionalExpression()
		 */
		@Override
		public CellSet visit(AssignmentExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public CellSet visit(NonConditionalExpression n) {
			// UnaryExpression may either be an lvalue, or not,
			// but in this case, it must be an lvalue since it is on LHS of assignment
			addAccesses(n.getF0().accept(this));
			addAccesses(n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= LogicalORExpression()
		 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
		 */
		@Override
		public CellSet visit(ConditionalExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				NodeSequence seq = (NodeSequence) n.getF1().getNode();
				Expression exp = (Expression) seq.getNodes().get(1);
				ConditionalExpression condExp = (ConditionalExpression) seq.getNodes().get(3);

				addAccesses(n.getF0().accept(this));
				addAccesses(exp.accept(this));
				addAccesses(condExp.accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= LogicalANDExpression()
		 * f1 ::= ( "||" LogicalORExpression() )?
		 */
		@Override
		public CellSet visit(LogicalORExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addAccesses(n.getF0().accept(this));
				addAccesses(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= InclusiveORExpression()
		 * f1 ::= ( "&&" LogicalANDExpression() )?
		 */
		@Override
		public CellSet visit(LogicalANDExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addAccesses(n.getF0().accept(this));
				addAccesses(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= ExclusiveORExpression()
		 * f1 ::= ( "|" InclusiveORExpression() )?
		 */
		@Override
		public CellSet visit(InclusiveORExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addAccesses(n.getF0().accept(this));
				addAccesses(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= ANDExpression()
		 * f1 ::= ( "^" ExclusiveORExpression() )?
		 */
		@Override
		public CellSet visit(ExclusiveORExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addAccesses(n.getF0().accept(this));
				addAccesses(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= EqualityExpression()
		 * f1 ::= ( "&" ANDExpression() )?
		 */
		@Override
		public CellSet visit(ANDExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addAccesses(n.getF0().accept(this));
				addAccesses(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= RelationalExpression()
		 * f1 ::= ( EqualOptionalExpression() )?
		 */
		@Override
		public CellSet visit(EqualityExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addAccesses(n.getF0().accept(this));
				n.getF1().getNode().accept(this);
				return null;
			}
		}

		/**
		 * f0 ::= EqualExpression()
		 * | NonEqualExpression()
		 */
		@Override
		public CellSet visit(EqualOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "=="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public CellSet visit(EqualExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "!="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public CellSet visit(NonEqualExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= ShiftExpression()
		 * f1 ::= ( RelationalOptionalExpression() )?
		 */
		@Override
		public CellSet visit(RelationalExpression n) {
			if (n.getRelExpF1().getNode() == null) {
				return n.getRelExpF0().accept(this);
			} else {
				addAccesses(n.getRelExpF0().accept(this));
				n.getRelExpF1().getNode().accept(this);
				return null;
			}
		}

		/**
		 * f0 ::= RelationalLTExpression()
		 * | RelationalGTExpression()
		 * | RelationalLEExpression()
		 * | RelationalGEExpression()
		 */
		@Override
		public CellSet visit(RelationalOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "<"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public CellSet visit(RelationalLTExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= ">"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public CellSet visit(RelationalGTExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "<="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public CellSet visit(RelationalLEExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= ">="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public CellSet visit(RelationalGEExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= AdditiveExpression()
		 * f1 ::= ( ShiftOptionalExpression() )?
		 */
		@Override
		public CellSet visit(ShiftExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addAccesses(n.getF0().accept(this));
				n.getF1().getNode().accept(this);
				return null;
			}
		}

		/**
		 * f0 ::= ShiftLeftExpression()
		 * | ShiftRightExpression()
		 */
		@Override
		public CellSet visit(ShiftOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= ">>"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public CellSet visit(ShiftLeftExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "<<"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public CellSet visit(ShiftRightExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= MultiplicativeExpression()
		 * f1 ::= ( AdditiveOptionalExpression() )?
		 */
		@Override
		public CellSet visit(AdditiveExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addAccesses(n.getF0().accept(this));
				n.getF1().getNode().accept(this);
				return null;
			}
		}

		/**
		 * f0 ::= AdditivePlusExpression()
		 * | AdditiveMinusExpression()
		 */
		@Override
		public CellSet visit(AdditiveOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "+"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public CellSet visit(AdditivePlusExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "-"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public CellSet visit(AdditiveMinusExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= CastExpression()
		 * f1 ::= ( MultiplicativeOptionalExpression() )?
		 */
		@Override
		public CellSet visit(MultiplicativeExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addAccesses(n.getF0().accept(this));
				n.getF1().getNode().accept(this);
				return null;
			}
		}

		/**
		 * f0 ::= MultiplicativeMultiExpression()
		 * | MultiplicativeDivExpression()
		 * | MultiplicativeModExpression()
		 */
		@Override
		public CellSet visit(MultiplicativeOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "*"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public CellSet visit(MultiplicativeMultiExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "/"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public CellSet visit(MultiplicativeDivExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "%"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public CellSet visit(MultiplicativeModExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= CastExpressionTyped()
		 * | UnaryExpression()
		 */
		@Override
		public CellSet visit(CastExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "("
		 * f1 ::= TypeName()
		 * f2 ::= ")"
		 * f3 ::= CastExpression()
		 */
		@Override
		public CellSet visit(CastExpressionTyped n) {
			addAccesses(n.getF3().accept(this));
			return null;
		}

		/**
		 * f0 ::= UnaryExpressionPreIncrement()
		 * | UnaryExpressionPreDecrement()
		 * | UnarySizeofExpression()
		 * | UnaryCastExpression()
		 * | PostfixExpression()
		 */
		@Override
		public CellSet visit(UnaryExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public CellSet visit(UnaryExpressionPreIncrement n) {
			CellSet symList = n.getF1().accept(this);
			addAccesses(symList);
			return null;
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public CellSet visit(UnaryExpressionPreDecrement n) {
			CellSet symList = n.getF1().accept(this);
			addAccesses(symList);
			return null;
		}

		/**
		 * f0 ::= UnaryOperator()
		 * f1 ::= CastExpression()
		 */
		@Override
		public CellSet visit(UnaryCastExpression n) {
			// Obtain the list of symbols referred by castExpression as a whole
			CellSet symList = n.getF1().accept(this);
			if (symList == null) {
				return null;
			}
			String operator = ((NodeToken) n.getF0().getF0().getChoice()).getTokenImage();
			switch (operator) {
			case "&":
				symList.applyAllExpanded(sym -> {
					if (sym instanceof Symbol) {
						cellAccessSet.add(sym);
					}
				});
				return null;
			case "*":
				addAccesses(symList);
				return null;
			case "+":
			case "-":
			case "~":
			case "!":
				addAccesses(symList);
				return null;
			default:
				return null;
			}
		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public CellSet visit(UnarySizeofExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public CellSet visit(SizeofUnaryExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= "("
		 * f2 ::= TypeName()
		 * f3 ::= ")"
		 */
		@Override
		public CellSet visit(SizeofTypeName n) {
			return null;
		}

		/**
		 * f0 ::= PrimaryExpression()
		 * f1 ::= PostfixOperationsList()
		 */
		@Override
		public CellSet visit(PostfixExpression n) {
			CellSet symList = n.getF0().accept(this);
			NodeListOptional nodeList = n.getF1().getF0();
			if (nodeList.getNodes().isEmpty()) {
				return symList;
			} else {
				for (Node opNodeWrapper : nodeList.getNodes()) {
					Node opNode = ((APostfixOperation) opNodeWrapper).getF0().getChoice();
					if (opNode instanceof BracketExpression) {
						BracketExpression brackExp = (BracketExpression) opNode;
						CellSet e1List = symList;
						CellSet e2List = brackExp.getF1().accept(this);
						addAccesses(e1List);
						addAccesses(e2List);
					} else if (opNode instanceof ArgumentList) {
						assert (false);
						return null;
					} else if (opNode instanceof DotId) {
						// ||p|.id| = |p|
						addAccesses(symList);
						// Now, the same set of symbols represents symbol.id,
						// hence we do not update symList.
					} else if (opNode instanceof ArrowId) {
						addAccesses(symList);
					} else if (opNode instanceof PlusPlus) {
						addAccesses(symList);
						symList = new CellSet();
					} else if (opNode instanceof MinusMinus) {
						addAccesses(symList);
						symList = new CellSet();
					} else {
						assert (false);
					}
				}
				return symList;
			}
		}

		/**
		 * f0 ::= "["
		 * f1 ::= Expression()
		 * f2 ::= "]"
		 */
		@Override
		public CellSet visit(BracketExpression n) {
			addAccesses(n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( ExpressionList() )?
		 * f2 ::= ")"
		 */
		@Override
		public CellSet visit(ArgumentList n) {
			// Since we have performed expression simplification,
			// we won't enter this operator.
			assert (false);
			// n.getF1().accept(this);
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * | Constant()
		 * | ExpressionClosed()
		 */
		@Override
		public CellSet visit(PrimaryExpression n) {
			if (n.getF0().getChoice() instanceof NodeToken) {
				CellSet _ret = new CellSet();
				Cell sym = Misc.getSymbolOrFreeEntry((NodeToken) n.getF0().getChoice());
				_ret.add(sym);
				return _ret;
			} else if (n.getF0().getChoice() instanceof Constant) {
				return null;
			} else if (n.getF0().getChoice() instanceof ExpressionClosed) {
				return n.getF0().getChoice().accept(this);
			} else {
				assert (false);
				return null;
			}
		}

		/**
		 * f0 ::= "("
		 * f1 ::= Expression()
		 * f2 ::= ")"
		 */
		@Override
		public CellSet visit(ExpressionClosed n) {
			return n.getF1().accept(this);
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public CellSet visit(ExpressionList n) {
			// Since we have performed expression simplification,
			// we won't enter this operator.
			assert (false);
			return null;
		}

		/**
		 * f0 ::= <INTEGER_LITERAL>
		 * | <FLOATING_POINT_LITERAL>
		 * | <CHARACTER_LITERAL>
		 * | ( <STRING_LITERAL> )+
		 */
		@Override
		public CellSet visit(Constant n) {
			return null;
		}

		@Override
		public CellSet visit(CallStatement n) {
			// AccessGetter should be called only on leaf nodes or their parts.
			// CallStatement is NOT a leaf node now.
			assert (false);
			return null;
		}

		@Override
		public CellSet visit(PreCallNode n) {
			CallStatement callStmt = n.getParent();
			addAccesses(new CellSet(callStmt.getInfo().getCalledSymbols()));
			for (SimplePrimaryExpression argument : n.getArgumentList()) {
				addAccesses(argument.accept(this));
			}
			return null;
		}

		@Override
		public CellSet visit(PostCallNode n) {
			if (n.hasReturnReceiver()) {
				addAccesses(n.getReturnReceiver().accept(this));
			}
			return null;
		}

		@Override
		public CellSet visit(SimplePrimaryExpression n) {
			CellSet symList = new CellSet();
			if (n.isAConstant()) {
				return symList;
			} else {
				// This SPE is an identifier.
				Cell sym = Misc.getSymbolOrFreeEntry(n.getIdentifier());
				if (sym != null) {
					symList.add(sym);
				}
				return symList;
			}
		}
	}
}
