/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis;

import imop.ast.info.cfgNodeInfo.DeclarationInfo;
import imop.ast.info.cfgNodeInfo.ExpressionInfo;
import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;

/**
 * Represents an abstraction of various types of assignments
 * from expressions to lvalues (of the form lhs = rhs).
 * Some common forms include:
 * <ol>
 * <li>NonConditionalExpressions, e.g., {@code x = y + 3}</li>
 * <li>Initializations, e.g., {@code int x = y + 3}</li>
 * <li>Argument passing, e.g., <code>foo(y+3)</code>, for a method
 * <code>void foo(int x) {...}</code></li>
 * <li>Returned values, e.g., <code>x = bar();</code>, for a method
 * <code>int bar() {...; return y + 3;}</code></li>
 * <li>OmpForInitExpression, e.g., x = 0</li>
 * </ol>
 * IMPORTANT: Note that currently we do not consider following operations as
 * assignments:
 * <ul>
 * <li>prefix and postfix ++ and -- operators.</li>
 * <li>any short-hand assignment, e.g., x += y, or x -= y.</li>
 * <li>any assignment within OmpForReinitExpression.</li>
 * </ul>
 * These expression forms would be considered as assignments, only after
 * ExpressionTree has been created for all the parsed Expressions.
 * 
 * @author Aman Nougrahiya
 *
 */
public class Assignment {
	/**
	 * This could refer to a Declarator, UnaryExpression, or a NodeToken
	 * (&ltIDENTIFIER&gt).
	 */
	public final Node lhs;
	/**
	 * This could refer to an Initializer, AssignmentExpression,
	 * ConstantExpression, Expression, or &ltIDENTIFIER&gt.
	 */
	public final Node rhs;

	public Assignment(Node lhs, Node rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public CellSet getLHSLocations() {
		if (lhs instanceof Declarator) {
			NodeToken rootId = DeclarationInfo.getRootIdNodeToken((Declarator) lhs);
			Symbol sym = Misc.getSymbolEntry(rootId.getTokenImage(), lhs);
			CellSet retSet = new CellSet();
			if (sym != null) {
				retSet.add(sym);
			}
			return retSet;
		} else if (lhs instanceof UnaryExpression) {
			return new CellSet(((ExpressionInfo) lhs.getInfo()).getLocationsOf());
		} else if (lhs instanceof NodeToken) {
			Symbol sym = Misc.getSymbolEntry(((NodeToken) lhs).getTokenImage(), lhs);
			CellSet retSet = new CellSet();
			if (sym != null) {
				retSet.add(sym);
			}
			return retSet;
		} else {
			assert (false);
			return null;
		}
	}

	public CellSet getRHSLocations() {
		if (rhs instanceof NodeToken) {
			Symbol sym = Misc.getSymbolEntry(((NodeToken) rhs).getTokenImage(), rhs);
			CellSet retSet = new CellSet();
			if (sym != null) {
				retSet.add(sym);
			}
			return retSet;
		} else if (rhs instanceof Expression) {
			return new CellSet(((ExpressionInfo) rhs.getInfo()).getLocationsOf());
		} else {
			assert (false);
			return null;
		}

	}

	/**
	 * Checks if this assignment has an effect of transferring the value of one
	 * symbol to the other. (For example, {@code a = b} is a copy instruction.)
	 * 
	 * @return
	 *         true, if this assignment is a copy instruction.
	 */
	public boolean isCopyInstruction() {
		if (!Misc.getInheritedEnclosee(this.rhs, CastExpressionTyped.class).isEmpty()) {
			return false;
		}
		CellSet lhsLocations = this.getLHSLocations();
		CellSet rhsLocations = this.getRHSLocations();
		if (lhsLocations.size() != 1 || rhsLocations.size() != 1) {
			return false;
		}
		Cell lhsCell = lhsLocations.getAnyElement();
		Cell rhsCell = rhsLocations.getAnyElement();
		if (!(lhsCell instanceof Symbol) || !(rhsCell instanceof Symbol)) {
			return false;
		}
		if (lhsCell.isSummaryNode()) {
			return false;
		}
		if (rhsCell.isSummaryNode()) {
			return false;
		}
		Symbol rhsSym = (Symbol) rhsCell;
		if (rhsSym.getType() instanceof ArrayType) {
			if (!Misc.getInheritedEnclosee(this.rhs, BracketExpression.class).isEmpty()) {
				return false;
			}
			if (!Misc.getInheritedEnclosee(this.rhs, AdditiveOptionalExpression.class).isEmpty()) {
				return false;
			}
		}
		return true;

	}

	@Override
	public String toString() {
		return lhs + " = " + rhs;
	}

	@Override
	public int hashCode() {
		final int prime = 179;
		int result = 1;
		result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
		result = prime * result + ((rhs == null) ? 0 : rhs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Assignment other = (Assignment) obj;
		if (lhs == null) {
			if (other.lhs != null) {
				return false;
			}
		} else if (!lhs.equals(other.lhs)) {
			return false;
		}
		if (rhs == null) {
			if (other.rhs != null) {
				return false;
			}
		} else if (!rhs.equals(other.rhs)) {
			return false;
		}
		return true;
	}
}
