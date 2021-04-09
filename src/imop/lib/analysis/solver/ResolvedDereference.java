/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.solver;

import imop.ast.node.external.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.solver.tokens.ExpressionTokenizer;
import imop.lib.analysis.solver.tokens.IdOrConstToken;
import imop.lib.analysis.solver.tokens.Tokenizable;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.analysis.typeSystem.PointerType;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.util.CellList;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * For an array dereference of the form {@code *(e1+e2)} or {@code e1[e2]}, this
 * class represents {@code e1} via its corresponding {@link Expression} object
 * in the former case, and as a {@link BaseSyntax} in the latter.
 * The expression {@code e2} is represented by a list of tokens
 * {@link Tokenizable} in the prefix form.
 * <br>
 * This class is used to unify the two abstractions of field accesses -- those
 * via {@code *} and those via {@code []} operators.
 * 
 * @author Aman Nougrahiya
 *
 */
public class ResolvedDereference extends AccessExpression {
	/**
	 * C expression that represents the base {@code e1} in {@code *(e1 + e2)}.
	 */
	private final Expression baseExpression;
	/**
	 * Base-syntax that represents the base {@code e1} in {@code e1[e2]}.
	 */
	private final BaseSyntax baseSyntax;

	private ResolvedDereference(Node leafNode, Expression baseExpression, List<Tokenizable> indexExpression) {
		super(leafNode, indexExpression);
		assert (leafNode != null);
		this.baseExpression = baseExpression;
		this.baseSyntax = null;
	}

	private ResolvedDereference(Node leafNode, BaseSyntax baseSyntax, List<Tokenizable> indexExpression) {
		super(leafNode, indexExpression);
		assert (leafNode != null);
		this.baseSyntax = baseSyntax;
		this.baseExpression = null;
	}

	/**
	 * Given a {@link CastExpression} object, this factory method returns the
	 * corresponding {@link ResolvedDereference} object.
	 * 
	 * @param castExpression
	 *                       given CastExpression that has been dereferenced using a
	 *                       pointer operator.
	 * @return
	 *         a {@link ResolvedDereference} object that logically
	 *         represents the dereferenced {@code castExpression} in the form
	 *         {@code e1[e2]}; {@code null}, if we were unable to find
	 *         {@code e1} or {@code e2}.
	 */
	public static ResolvedDereference getResolvedAccess(CastExpression castExpression) {
		ResolvedDereference returnExp;
		Node leafOfCast = Misc.getCFGNodeFor(castExpression);

		DereferenceGetter baseVisitor = new DereferenceGetter();
		castExpression.accept(baseVisitor);
		if (baseVisitor.baseExpression == null || baseVisitor.indexExpression == null
				|| baseVisitor.indexExpression.isEmpty()) {
			return null;
		}
		returnExp = new ResolvedDereference(leafOfCast, baseVisitor.baseExpression, baseVisitor.indexExpression);
		return returnExp;
	}

	/**
	 * Given a {@link SyntacticAccessExpression}, this factory method returns
	 * the corresponding {@link ResolvedDereference} object.
	 * 
	 * @param sae
	 *            given {@link SyntacticAccessExpression}.
	 * @return
	 *         a {@link ResolvedDereference} object that logically
	 *         represents the dereferenced {@code castExpression} in the form
	 *         {@code e1[e2]}, for multiple possible values of {@code e1}.
	 */
	public static ResolvedDereference getResolvedAccess(SyntacticAccessExpression sae) {
		ResolvedDereference returnExp;
		Node leafOdSAE = sae.getLeafNode();
		returnExp = new ResolvedDereference(leafOdSAE, sae.getBaseSyntax(), sae.getIndexExpression());
		return returnExp;
	}

	/**
	 * Obtain the base-expression e1 in *(e1 + e2).
	 * 
	 * @return
	 *         a C {@link Expression} representing the base on which
	 *         dereferencing is performed.
	 */
	public Expression getBaseExpression() {
		return this.baseExpression;
	}

	/**
	 * Obtain the base-syntax {@code e1} in {@code e1[e2]}.
	 * 
	 * @return
	 *         a {@link BaseSyntax} object that represents {@code e1} in
	 *         {@code e1[e2]}.
	 */
	public BaseSyntax getBaseSyntax() {
		return this.baseSyntax;
	}

	/**
	 * Obtain a set of cell locations that represent the base expression.
	 * 
	 * @return
	 *         a set of cell locations on which the dereference would be made.
	 */
	public CellSet getBaseLocations() {
		if (this.baseExpression != null) {
			return new CellSet(this.baseExpression.getInfo().getLocationsOf());
		} else {
			if (this.baseSyntax.postFixOps == null || this.baseSyntax.postFixOps.isEmpty()) {
				return new CellSet(this.baseSyntax.primExp.getInfo().getLocationsOf());
			} else {
				CellSet retSet = new CellSet(this.baseSyntax.primExp.getInfo().getLocationsOf());
				long count = this.baseSyntax.postFixOps.stream()
						.filter(op -> op.getF0().getChoice() instanceof BracketExpression).count();
				while (count > 0) {
					if (retSet.isUniversal()) {
						return retSet;
					}
					CellSet nextSet = new CellSet();
					for (Cell cell : retSet) {
						nextSet.addAll(cell.getPointsTo(this.getLeafNode()));
					}
					retSet = nextSet;
					count--;
				}
				return retSet;
			}
		}
	}

	/**
	 * Obtain the type information for baseExpression.
	 * 
	 * @return
	 */
	public Type getBaseType() {
		if (this.baseExpression != null) {
			return Type.getType(baseExpression);
		} else {
			if (this.baseSyntax.postFixOps == null || this.baseSyntax.postFixOps.isEmpty()) {
				return Type.getType(this.baseSyntax.primExp);
			} else {
				Type primType = Type.getType(this.baseSyntax.primExp);
				long count = this.baseSyntax.postFixOps.stream()
						.filter(op -> op.getF0().getChoice() instanceof BracketExpression).count();
				while (count > 0) {
					if (primType instanceof ArrayType) {
						primType = ((ArrayType) primType).getElementType();
					} else if (primType instanceof PointerType) {
						primType = ((PointerType) primType).getPointeeType();
					}
					count--;
				}
				return primType;
			}
		}
	}

	/**
	 * Obtain the dereference made at immediately lower level, if any. For
	 * example, if this object represents {@code e1[e2][e3]}, this method would
	 * return an object representing {@code e1[e2]}.
	 * <br>
	 * Note that this method doesn't check the reaching-definition of pointers
	 * to get lower level dereferences. For example, in
	 * {@code int *p = u[i]; L1: p[j];}, when this method is called on the
	 * object representing {@code p[j]}, it wouldn't return an object
	 * corresponding to {@code u[i]}, but {@code null} instead.
	 * <br>
	 * (If such precision is required in future, the return type of this method
	 * should become {@code Set<ResolvedDereference>}, and the method should use
	 * reaching-definition flow-facts to obtain the required information.)
	 * 
	 * @return
	 *         an object representing the lower level dereference made within
	 *         the base-expression, if any; else {@code null}, if the
	 *         dereference is not local to this node.
	 */
	public ResolvedDereference getLowerLevelDereference() {
		if (this.baseExpression != null) {
			List<CastExpression> baseDerefBaseCast = PointerDereferenceGetter
					.getPointerDereferencedCastExpressionLocationsOf(this.baseExpression);
			if (baseDerefBaseCast != null && !baseDerefBaseCast.isEmpty()) {
				// Pick the fist cast.
				return ResolvedDereference.getResolvedAccess(baseDerefBaseCast.get(0));
			} else {
				List<SyntacticAccessExpression> baseDerefBaseSynt = SyntacticAccessExpressionGetter
						.getBaseAccessExpressionLocationsOf(this.baseExpression);
				if (baseDerefBaseSynt != null && !baseDerefBaseSynt.isEmpty()) {
					// Pick the first deref.
					return ResolvedDereference.getResolvedAccess(baseDerefBaseSynt.get(0));
				} else {
					return null;
				}
			}
		} else {
			if (this.baseSyntax.postFixOps == null || this.baseSyntax.postFixOps.isEmpty()) {
				/*
				 * For now, we do not go up further to the reaching definitions
				 * of the primaryExpression.
				 */
				return null;
			} else {
				return this.baseSyntax.getLowerLevelDereference();
			}
		}
	}

	@Override
	public String toString() {
		if (this.baseExpression != null) {
			return "R-AE[" + baseExpression + "::" + getIndexExpression() + "]";
		} else {
			return "R-AE[" + baseSyntax + "::" + getIndexExpression() + "]";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseExpression == null) ? 0 : baseExpression.hashCode());
		result = prime * result + ((baseSyntax == null) ? 0 : baseSyntax.hashCode());
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
		ResolvedDereference other = (ResolvedDereference) obj;
		if (this.getLeafNode() != other.getLeafNode()) {
			return false;
		}
		if (!this.getIndexExpression().equals(other.getIndexExpression())) {
			return false;
		}
		if (baseExpression == null) {
			if (other.baseExpression != null) {
				return false;
			}
		} else if (!baseExpression.equals(other.baseExpression)) {
			return false;
		}
		if (baseSyntax == null) {
			if (other.baseSyntax != null) {
				return false;
			}
		} else if (!baseSyntax.equals(other.baseSyntax)) {
			return false;
		}
		return true;
	}

	private static class DereferenceGetter extends DepthFirstProcess {
		List<Tokenizable> indexExpression = null;
		Expression baseExpression = null;

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public void visit(Expression n) {
			if (n.getExpF1().present()) {
				return;
			} else {
				n.getExpF0().accept(this);
			}
		}

		/**
		 * f0 ::= NonConditionalExpression()
		 * | ConditionalExpression()
		 */
		@Override
		public void visit(AssignmentExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public void visit(NonConditionalExpression n) {
			return;
		}

		/**
		 * f0 ::= "="
		 * | "*="
		 * | "/="
		 * | "%="
		 * | "+="
		 * | "-="
		 * | "<<="
		 * | ">>="
		 * | "&="
		 * | "^="
		 * | "|="
		 */
		@Override
		public void visit(AssignmentOperator n) {
			return;
		}

		/**
		 * f0 ::= LogicalORExpression()
		 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
		 */
		@Override
		public void visit(ConditionalExpression n) {
			if (n.getF1().present()) {
				return;
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= ConditionalExpression()
		 */
		@Override
		public void visit(ConstantExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= LogicalANDExpression()
		 * f1 ::= ( "||" LogicalORExpression() )?
		 */
		@Override
		public void visit(LogicalORExpression n) {
			if (n.getF1().present()) {
				return;
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= InclusiveORExpression()
		 * f1 ::= ( "&&" LogicalANDExpression() )?
		 */
		@Override
		public void visit(LogicalANDExpression n) {
			if (n.getF1().present()) {
				return;
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= ExclusiveORExpression()
		 * f1 ::= ( "|" InclusiveORExpression() )?
		 */
		@Override
		public void visit(InclusiveORExpression n) {
			if (n.getF1().present()) {
				return;
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= ANDExpression()
		 * f1 ::= ( "^" ExclusiveORExpression() )?
		 */
		@Override
		public void visit(ExclusiveORExpression n) {
			if (n.getF1().present()) {
				return;
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= EqualityExpression()
		 * f1 ::= ( "&" ANDExpression() )?
		 */
		@Override
		public void visit(ANDExpression n) {
			if (n.getF1().present()) {
				return;
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= RelationalExpression()
		 * f1 ::= ( EqualOptionalExpression() )?
		 */
		@Override
		public void visit(EqualityExpression n) {
			if (n.getF1().present()) {
				return;
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= EqualExpression()
		 * | NonEqualExpression()
		 */
		@Override
		public void visit(EqualOptionalExpression n) {
			return;
		}

		/**
		 * f0 ::= "=="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public void visit(EqualExpression n) {
			return;
		}

		/**
		 * f0 ::= "!="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public void visit(NonEqualExpression n) {
			return;
		}

		/**
		 * f0 ::= ShiftExpression()
		 * f1 ::= ( RelationalOptionalExpression() )?
		 */
		@Override
		public void visit(RelationalExpression n) {
			if (n.getRelExpF1().present()) {
				return;
			} else {
				n.getRelExpF0().accept(this);
			}
		}

		/**
		 * f0 ::= RelationalLTExpression()
		 * | RelationalGTExpression()
		 * | RelationalLEExpression()
		 * | RelationalGEExpression()
		 */
		@Override
		public void visit(RelationalOptionalExpression n) {
			return;
		}

		/**
		 * f0 ::= "<"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public void visit(RelationalLTExpression n) {
			return;
		}

		/**
		 * f0 ::= ">"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public void visit(RelationalGTExpression n) {
			return;
		}

		/**
		 * f0 ::= "<="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public void visit(RelationalLEExpression n) {
			return;
		}

		/**
		 * f0 ::= ">="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public void visit(RelationalGEExpression n) {
			return;
		}

		/**
		 * f0 ::= AdditiveExpression()
		 * f1 ::= ( ShiftOptionalExpression() )?
		 */
		@Override
		public void visit(ShiftExpression n) {
			if (n.getF1().present()) {
				return;
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= ShiftLeftExpression()
		 * | ShiftRightExpression()
		 */
		@Override
		public void visit(ShiftOptionalExpression n) {
			return;
		}

		/**
		 * f0 ::= ">>"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public void visit(ShiftLeftExpression n) {
			return;
		}

		/**
		 * f0 ::= "<<"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public void visit(ShiftRightExpression n) {
			return;
		}

		/**
		 * f0 ::= MultiplicativeExpression()
		 * f1 ::= ( AdditiveOptionalExpression() )?
		 */
		@Override
		public void visit(AdditiveExpression n) {
			if (!n.getF1().present()) {
				n.getF0().accept(this);
			} else {
				Node rhs = ((AdditiveOptionalExpression) n.getF1().getNode()).getF0().getChoice();
				if (rhs instanceof AdditiveMinusExpression) {
					return;
				} else {
					AdditivePlusExpression ame = (AdditivePlusExpression) rhs;
					Expression left = n.getF0();
					Expression right = ame.getF1();
					// Find the pointer type and integer type.
					Type leftType = Type.getType(left);
					Type rightType = Type.getType(right);
					boolean leftIsBase = (leftType instanceof PointerType) || (leftType instanceof ArrayType);
					boolean rightIsBase = (rightType instanceof PointerType) || (rightType instanceof ArrayType);
					if (leftIsBase && rightIsBase) {
						return;
					} else if (leftIsBase) {
						CellList cells = left.getInfo().getLocationsOf();
						if (cells == null) {
							return;
						} else {
							indexExpression = ExpressionTokenizer.getPrefixTokens(right);
							baseExpression = left;
							return;
						}
					} else if (rightIsBase) {
						CellList cells = right.getInfo().getLocationsOf();
						if (cells == null) {
							return;
						} else {
							indexExpression = ExpressionTokenizer.getPrefixTokens(left);
							baseExpression = right;
							return;
						}
					}
				}
			}
		}

		/**
		 * f0 ::= AdditivePlusExpression()
		 * | AdditiveMinusExpression()
		 */
		@Override
		public void visit(AdditiveOptionalExpression n) {
			return;
		}

		/**
		 * f0 ::= "+"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public void visit(AdditivePlusExpression n) {
			return;
		}

		/**
		 * f0 ::= "-"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public void visit(AdditiveMinusExpression n) {
			return;
		}

		/**
		 * f0 ::= CastExpression()
		 * f1 ::= ( MultiplicativeOptionalExpression() )?
		 */
		@Override
		public void visit(MultiplicativeExpression n) {
			if (n.getF1().present()) {
				return;
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= MultiplicativeMultiExpression()
		 * | MultiplicativeDivExpression()
		 * | MultiplicativeModExpression()
		 */
		@Override
		public void visit(MultiplicativeOptionalExpression n) {
			return;
		}

		/**
		 * f0 ::= "*"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public void visit(MultiplicativeMultiExpression n) {
			return;
		}

		/**
		 * f0 ::= "/"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public void visit(MultiplicativeDivExpression n) {
			return;
		}

		/**
		 * f0 ::= "%"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public void visit(MultiplicativeModExpression n) {
			return;
		}

		/**
		 * f0 ::= CastExpressionTyped()
		 * | UnaryExpression()
		 */
		@Override
		public void visit(CastExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "("
		 * f1 ::= TypeName()
		 * f2 ::= ")"
		 * f3 ::= CastExpression()
		 */
		@Override
		public void visit(CastExpressionTyped n) {
			n.getF3().accept(this);
		}

		/**
		 * f0 ::= UnaryExpressionPreIncrement()
		 * | UnaryExpressionPreDecrement()
		 * | UnarySizeofExpression()
		 * | UnaryCastExpression()
		 * | PostfixExpression()
		 */
		@Override
		public void visit(UnaryExpression n) {
			n.getF0().accept(this);
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(UnaryExpressionPreIncrement n) {
			CellList cells = n.getF1().getInfo().getLocationsOf();
			if (cells == null) {
				return;
			} else {
				baseExpression = n.getF1();
				indexExpression = new ArrayList<>();
				indexExpression.add(IdOrConstToken.getNewConstantToken("1"));
				return;
			}
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(UnaryExpressionPreDecrement n) {
			return;
		}

		/**
		 * f0 ::= UnaryOperator()
		 * f1 ::= CastExpression()
		 */
		@Override
		public void visit(UnaryCastExpression n) {
			CellList cells = n.getInfo().getLocationsOf();
			if (cells == null) {
				return;
			} else {
				String operator = n.getF0().toString().trim();
				if (operator.equals("*") || operator.equals("+")) {
					baseExpression = n.getF1();
					indexExpression = new ArrayList<>();
					indexExpression.add(IdOrConstToken.getNewConstantToken("0"));
					return;
				} else {
					return;
				}
			}
		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public void visit(UnarySizeofExpression n) {
			return;
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(SizeofUnaryExpression n) {
			return;
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= "("
		 * f2 ::= TypeName()
		 * f3 ::= ")"
		 */
		@Override
		public void visit(SizeofTypeName n) {
			return;
		}

		/**
		 * f0 ::= PrimaryExpression()
		 * f1 ::= PostfixOperationsList()
		 */
		@Override
		public void visit(PostfixExpression n) {
			List<Node> postFixOpNodes = n.getF1().getF0().getNodes();
			if (postFixOpNodes.isEmpty()) {
				n.getF0().accept(this);
			} else {
				CellList cells = n.getInfo().getLocationsOf();
				if (cells == null) {
					return;
				} else {
					baseExpression = n;
					indexExpression = new ArrayList<>();
					indexExpression.add(IdOrConstToken.getNewConstantToken("0"));
				}
			}
		}

		/**
		 * f0 ::= ( APostfixOperation() )*
		 */
		@Override
		public void visit(PostfixOperationsList n) {
			return;
		}

		/**
		 * f0 ::= BracketExpression()
		 * | ArgumentList()
		 * | DotId()
		 * | ArrowId()
		 * | PlusPlus()
		 * | MinusMinus()
		 */
		@Override
		public void visit(APostfixOperation n) {
			return;
		}

		/**
		 * f0 ::= "++"
		 */
		@Override
		public void visit(PlusPlus n) {
			return;
		}

		/**
		 * f0 ::= "--"
		 */
		@Override
		public void visit(MinusMinus n) {
			return;
		}

		/**
		 * f0 ::= "["
		 * f1 ::= Expression()
		 * f2 ::= "]"
		 */
		@Override
		public void visit(BracketExpression n) {
			return;
		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( ExpressionList() )?
		 * f2 ::= ")"
		 */
		@Override
		public void visit(ArgumentList n) {
			return;
		}

		/**
		 * f0 ::= "."
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(DotId n) {
			return;
		}

		/**
		 * f0 ::= "->"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(ArrowId n) {
			return;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * | Constant()
		 * | ExpressionClosed()
		 */
		@Override
		public void visit(PrimaryExpression n) {
			Node internal = n.getF0().getChoice();
			if (internal instanceof NodeToken) {
				baseExpression = n;
				indexExpression = new ArrayList<>();
				indexExpression.add(IdOrConstToken.getNewConstantToken("0"));
				return;
			} else if (internal instanceof Constant) {
				assert (false) : "Found an expression of the form (*" + n + ")";
				return;
			} else {
				n.getF0().accept(this);
			}
		}

		/**
		 * f0 ::= "("
		 * f1 ::= Expression()
		 * f2 ::= ")"
		 */
		@Override
		public void visit(ExpressionClosed n) {
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public void visit(ExpressionList n) {
			return;
		}

		/**
		 * f0 ::= <INTEGER_LITERAL>
		 * | <FLOATING_POINT_LITERAL>
		 * | <CHARACTER_LITERAL>
		 * | ( <STRING_LITERAL> )+
		 */
		@Override
		public void visit(Constant n) {
			return;
		}
	}

}
