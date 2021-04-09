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
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.typeSystem.*;
import imop.lib.util.Misc;
import imop.parser.CParserConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Returns the type of the expression being visited.
 * If any sub-expression has unknown type, then this method should gracefully
 * return {@code null}, and not throw any exception.
 * TODO: Handle type qualifiers as well.
 */
public class ExpressionTypeGetter extends GJNoArguDepthFirstProcess<Type> {

	/**
	 * This method performs pointer generation on {@code inputType}, as per the
	 * semantics of the C language.
	 * Note that the pointer-generation is performed for types of all the
	 * expressions and subexpressions, except when they are an operand of
	 * the unary {@code &}, {@code ++}, {@code --}, {@code sizeof}, or LHS
	 * operand of {@code =} or {@code .} operators.
	 * However, unless we encounter these operators, we don't assume that these
	 * operators exist on the operand under concern.
	 * Hence, this method <i>always</i> performs the pointer generation on all
	 * array and function types.
	 * 
	 * @param inputType
	 *                  type on which pointer generation has to be performed.
	 * @return
	 *         the pointer-generated type, regardless of where the corresponding
	 *         operand may exist.
	 * @see ExpressionTypeGetter#performPointerDegeneration(Type)
	 */
	public static Type performPointerGeneration(Type inputType) {
		if (inputType instanceof ArrayType) {
			return new PointerType(((ArrayType) inputType).getElementType(), inputType);
		} else if (inputType instanceof FunctionType) {
			return new PointerType(inputType, inputType);
		} else {
			return inputType;
		}
	}

	/**
	 * This method returns the type on which pointer generation might have been
	 * performed.
	 * This method is useful to undo the effects of pointer generation, at the
	 * following locations:
	 * the unary {@code &}, {@code ++}, {@code --}, {@code sizeof}, or LHS
	 * operand of {@code =} or {@code .} operators.
	 * 
	 * @param inputType
	 *                  the pointer-generated type, or a simple type.
	 * @return
	 *         type on which pointer-generation might have been applied.
	 */
	public static Type performPointerDegeneration(Type inputType) {
		if (inputType instanceof PointerType) {
			PointerType pointerType = (PointerType) inputType;
			if (pointerType.isFromPointer()) {
				return pointerType;
			} else {
				Type oldType = pointerType.getOldType();
				assert (oldType != null) : "A pointer type couldn't have been generated on top of a null type.";
				return oldType;
			}
		} else {
			return inputType;
		}
	}

	/**
	 * Returns the type of a postfix expression <code>n</code> of the form
	 * {@code n e1 e2 e3...em...en} up to and excluding {@code em},
	 * if <code>tillOpNode</code> is {@code em}, where all {@code e's} are
	 * postfix operators.
	 * Note that pointer-generation is performed on the returned type.
	 * 
	 * @param postFixExp
	 * @param tillOpNode
	 * @return
	 */
	public static Type getHalfPostfixExpressionType(PostfixExpression n, APostfixOperation tillOpNode) {
		List<Node> operators = n.getF1().getF0().getNodes();
		if (operators.isEmpty()) {
			return n.getF0().accept(new ExpressionTypeGetter());
		}
		Type e1Type = n.getF0().accept(new ExpressionTypeGetter());
		if (e1Type == null) {
			if (operators.size() > 0
					&& ((APostfixOperation) operators.get(0)).getF0().getChoice() instanceof ArgumentList) {
				return SignedIntType.type();
				/*
				 * OLD CODE: The expression denoting a function call will belong
				 * to the same type as is the return type of the function being
				 * called. This old code below erroneously assumes the type of
				 * such function-calls to be function-type.
				 */
				// e1Type = new FunctionType(SignedIntType.type(), null);
				// e1Type = new PointerType(e1Type, e1Type);
			} else {
				return null;
			}
		}
		int index = -1;
		for (Node opNode : operators) {
			index++;
			if (tillOpNode == opNode) {
				if (operators.size() > index + 1) {
					Node nextOp = operators.get(index + 1);
					if (nextOp instanceof PlusPlus || nextOp instanceof MinusMinus) {
						e1Type = ExpressionTypeGetter.performPointerDegeneration(e1Type);
					}
				}
				return e1Type;
			}

			Node operator = ((APostfixOperation) opNode).getF0().getChoice();
			if (e1Type == null && !(operator instanceof BracketExpression)) {
				return null;
			}
			if (operator instanceof BracketExpression) {
				Type e2Type = ((BracketExpression) operator).getF1().accept(new ExpressionTypeGetter());
				PointerType pType;
				// If the expression isn't connected to the program.
				if (e1Type == null) {
					pType = (PointerType) e2Type;
				} else if (e2Type == null) {
					pType = (PointerType) e1Type;
				} else {
					if (e1Type instanceof IntegerType) {
						assert (e2Type instanceof PointerType);
						pType = (PointerType) e2Type;

					} else if (e2Type instanceof IntegerType) {
						assert (e1Type instanceof PointerType) : "Type issues with " + n + "; found type: " + e1Type;
						pType = (PointerType) e1Type;
					} else {
						pType = null;
						return null;
					}
				}
				e1Type = ExpressionTypeGetter.performPointerGeneration(pType.getPointeeType());
			} else if (operator instanceof ArgumentList) {
				assert (e1Type instanceof PointerType);
				FunctionType fType = (FunctionType) ((PointerType) e1Type).getPointeeType();
				if (fType == null) {
					e1Type = SignedIntType.type();
				} else {
					if (!(fType.getReturnType() instanceof FunctionType)) {
						e1Type = ExpressionTypeGetter.performPointerGeneration(fType.getReturnType());
					} else {
						// Question: Why?!
						e1Type = VoidType.type();
					}
				}
			} else if (operator instanceof DotId) {
				e1Type = ExpressionTypeGetter.performPointerDegeneration(e1Type);
				Type mType = null;
				String memberName = ((DotId) operator).getF1().getTokenImage();
				if (e1Type instanceof StructType) {
					for (StructOrUnionMember member : ((StructType) e1Type).getElementList()) {
						if (member.getElementName() == null) {
							continue;
						}
						if (member.getElementName().equals(memberName)) {
							mType = ExpressionTypeGetter.performPointerGeneration(member.getElementType());
							break;
						}
					}
					// assert (mType != null) : "Could not find type information for " +
					// n.toString();
				} else if (e1Type instanceof UnionType) {
					for (StructOrUnionMember member : ((UnionType) e1Type).getElementList()) {
						if (member.getElementName() == null) {
							continue;
						}
						if (member.getElementName().equals(memberName)) {
							mType = ExpressionTypeGetter.performPointerGeneration(member.getElementType());
							break;
						}
					}
					// assert (mType != null);
				} else {
					mType = null;
					// assert (false);
				}
				// TODO: Take care of the qualifiers here.
				e1Type = mType;
			} else if (operator instanceof ArrowId) {
				Type mType = null;
				String memberName = ((ArrowId) operator).getF1().getTokenImage();
				e1Type = ((PointerType) e1Type).getPointeeType();
				if (e1Type instanceof StructType) {
					for (StructOrUnionMember member : ((StructType) e1Type).getElementList()) {
						if (member.getElementName() == null) {
							continue;
						}
						if (member.getElementName().equals(memberName)) {
							mType = ExpressionTypeGetter.performPointerGeneration(member.getElementType());
							break;
						}
					}
					// assert (mType != null);
				} else if (e1Type instanceof UnionType) {
					for (StructOrUnionMember member : ((UnionType) e1Type).getElementList()) {
						if (member.getElementName() == null) {
							continue;
						}
						if (member.getElementName().equals(memberName)) {
							mType = ExpressionTypeGetter.performPointerGeneration(member.getElementType());
							break;
						}
					}
					// assert (mType != null);
				} else {
					mType = null;
					// assert (false);
				}
				// TODO: Take care of the qualifiers here.
				e1Type = mType;
			} else if (operator instanceof PlusPlus || operator instanceof MinusMinus) {
				e1Type = ExpressionTypeGetter.performPointerDegeneration(e1Type);
				if (e1Type.isRealType()) {
					// Return the same type as that of the operand.
					// Hence, no change in the type information.
				} else if (e1Type instanceof PointerType) {
					// TODO: Refer to 6.5.6 and 6.5.16.2 of ISO C11 standard.
					// Return the same type as that of the operand.
					// Hence, no change in the type information.
				} else {
					e1Type = null;
					// assert (false);
				}
			}
		}
		return e1Type;
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * | ArrayInitializer()
	 */
	@Override
	public Type visit(Initializer n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "{"
	 * f1 ::= InitializerList()
	 * f2 ::= ( "," )?
	 * f3 ::= "}"
	 */
	@Override
	public Type visit(ArrayInitializer n) {
		return n.getF1().accept(this);
	}

	/**
	 * f0 ::= Initializer()
	 * f1 ::= ( "," Initializer() )*
	 */
	@Override
	public Type visit(InitializerList n) {
		/*
		 * Note that pointer-generation doesn't matter here. This expression has
		 * a specific usage. ISO C doesn't discuss about the type of the
		 * initializer list.
		 */
		return new ArrayType(n.getF0().accept(this));
	}

	/**
	 * f0 ::= OmpForLTCondition()
	 * | OmpForLECondition()
	 * | OmpForGTCondition()
	 * | OmpForGECondition()
	 */
	@Override
	public Type visit(OmpForCondition n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<"
	 * f2 ::= Expression()
	 */
	@Override
	public Type visit(OmpForLTCondition n) {
		return SignedIntType.type();
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "<="
	 * f2 ::= Expression()
	 */
	@Override
	public Type visit(OmpForLECondition n) {
		return SignedIntType.type();
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">"
	 * f2 ::= Expression()
	 */
	@Override
	public Type visit(OmpForGTCondition n) {
		return SignedIntType.type();
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= ">="
	 * f2 ::= Expression()
	 */
	@Override
	public Type visit(OmpForGECondition n) {
		return SignedIntType.type();
	}

	/**
	 * f0 ::= PostIncrementId()
	 * | PostDecrementId()
	 * | PreIncrementId()
	 * | PreDecrementId()
	 * | ShortAssignPlus()
	 * | ShortAssignMinus()
	 * | OmpForAdditive()
	 * | OmpForSubtractive()
	 * | OmpForMultiplicative()
	 */
	@Override
	public Type visit(OmpForReinitExpression n) {
		return n.getOmpForReinitF0().accept(this);
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "++"
	 */
	@Override
	public Type visit(PostIncrementId n) {
		Symbol sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n.getF0());
		if (sym == null) {
			return SignedIntType.type();
		}
		return sym.getType();
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * f1 ::= "--"
	 */
	@Override
	public Type visit(PostDecrementId n) {
		Symbol sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n.getF0());
		if (sym == null) {
			return SignedIntType.type();
		}
		return sym.getType();
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public Type visit(PreIncrementId n) {
		Symbol sym = Misc.getSymbolEntry(n.getF1().getTokenImage(), n.getF1());
		if (sym == null) {
			return SignedIntType.type();
		}
		return sym.getType();
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public Type visit(PreDecrementId n) {
		Symbol sym = Misc.getSymbolEntry(n.getF1().getTokenImage(), n.getF1());
		if (sym == null) {
			return SignedIntType.type();
		}
		return sym.getType();
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public Type visit(Expression n) {
		Type leftExpType = n.getExpF0().accept(this);
		if (!n.getExpF1().present()) {
			return leftExpType;
		}
		return ((NodeSequence) n.getExpF1().getNodes().get(n.getExpF1().getNodes().size() - 1)).getNodes().get(1)
				.accept(this);
	}

	/**
	 * f0 ::= NonConditionalExpression()
	 * | ConditionalExpression()
	 */
	@Override
	public Type visit(AssignmentExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= UnaryExpression()
	 * f1 ::= AssignmentOperator()
	 * f2 ::= AssignmentExpression()
	 */
	@Override
	public Type visit(NonConditionalExpression n) {
		Type type = n.getF0().accept(this);
		type = ExpressionTypeGetter.performPointerDegeneration(type);
		return type;
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
	public Type visit(AssignmentOperator n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= LogicalORExpression()
	 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
	 */
	@Override
	public Type visit(ConditionalExpression n) {
		Type leftExpType = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return leftExpType;
		}
		Type trueExpType = ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this);
		Type falseExpType = ((NodeSequence) n.getF1().getNode()).getNodes().get(3).accept(this);
		if (trueExpType instanceof ArithmeticType && falseExpType instanceof ArithmeticType) {
			return Conversion.getUsualArithmeticConvertedType(trueExpType, falseExpType);
		} else if (trueExpType instanceof StructType && falseExpType instanceof StructType) {
			return trueExpType;
		} else if (trueExpType instanceof UnionType && falseExpType instanceof UnionType) {
			return trueExpType;
		} else if (trueExpType instanceof VoidType && falseExpType instanceof VoidType) {
			return trueExpType;
		} else if (trueExpType instanceof PointerType) {
			if (falseExpType instanceof PointerType) {
				PointerType falsePointer = (PointerType) falseExpType;
				if (falsePointer.getPointeeType() == VoidType.type()) {
					return falsePointer;
				} else {
					return trueExpType;
				}
			} else {
				return trueExpType;
			}
		} else if (falseExpType instanceof PointerType) {
			if (trueExpType instanceof PointerType) {
				PointerType truePointer = (PointerType) trueExpType;
				if (truePointer.getPointeeType() == VoidType.type()) {
					return truePointer;
				} else {
					return falseExpType;
				}
			} else {
				return falseExpType;
			}
		} else {
			// assert (false);
			return trueExpType;
		}
	}

	/**
	 * f0 ::= ConditionalExpression()
	 */
	@Override
	public Type visit(ConstantExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= LogicalANDExpression()
	 * f1 ::= ( "||" LogicalORExpression() )?
	 */
	@Override
	public Type visit(LogicalORExpression n) {
		Type leftExpType = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return leftExpType;
		}
		return SignedIntType.type();
	}

	/**
	 * f0 ::= InclusiveORExpression()
	 * f1 ::= ( "&&" LogicalANDExpression() )?
	 */
	@Override
	public Type visit(LogicalANDExpression n) {
		Type leftExpType = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return leftExpType;
		}
		return SignedIntType.type();
	}

	/**
	 * f0 ::= ExclusiveORExpression()
	 * f1 ::= ( "|" InclusiveORExpression() )?
	 */
	@Override
	public Type visit(InclusiveORExpression n) {
		Type leftExpType = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return leftExpType;
		}
		Type rightExpType = ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this);
		return Conversion.getUsualArithmeticConvertedType(leftExpType, rightExpType);
	}

	/**
	 * f0 ::= ANDExpression()
	 * f1 ::= ( "^" ExclusiveORExpression() )?
	 */
	@Override
	public Type visit(ExclusiveORExpression n) {
		Type leftExpType = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return leftExpType;
		}
		Type rightExpType = ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this);
		return Conversion.getUsualArithmeticConvertedType(leftExpType, rightExpType);
	}

	/**
	 * f0 ::= EqualityExpression()
	 * f1 ::= ( "&" ANDExpression() )?
	 */
	@Override
	public Type visit(ANDExpression n) {
		Type leftExpType = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return leftExpType;
		}
		Type rightExpType = ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this);
		return Conversion.getUsualArithmeticConvertedType(leftExpType, rightExpType);
	}

	/**
	 * f0 ::= RelationalExpression()
	 * f1 ::= ( EqualOptionalExpression() )?
	 */
	@Override
	public Type visit(EqualityExpression n) {
		Type leftExpType = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return leftExpType;
		}
		return SignedIntType.type();
	}

	/**
	 * f0 ::= EqualExpression()
	 * | NonEqualExpression()
	 */
	@Override
	public Type visit(EqualOptionalExpression n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= "=="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public Type visit(EqualExpression n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= "!="
	 * f1 ::= EqualityExpression()
	 */
	@Override
	public Type visit(NonEqualExpression n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= ShiftExpression()
	 * f1 ::= ( RelationalOptionalExpression() )?
	 */
	@Override
	public Type visit(RelationalExpression n) {
		Type leftExpType = n.getRelExpF0().accept(this);
		if (!n.getRelExpF1().present()) {
			return leftExpType;
		}
		return SignedIntType.type();
	}

	/**
	 * f0 ::= RelationalLTExpression()
	 * | RelationalGTExpression()
	 * | RelationalLEExpression()
	 * | RelationalGEExpression()
	 */
	@Override
	public Type visit(RelationalOptionalExpression n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= "<"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public Type visit(RelationalLTExpression n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= ">"
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public Type visit(RelationalGTExpression n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= "<="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public Type visit(RelationalLEExpression n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= ">="
	 * f1 ::= RelationalExpression()
	 */
	@Override
	public Type visit(RelationalGEExpression n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= AdditiveExpression()
	 * f1 ::= ( ShiftOptionalExpression() )?
	 */
	@Override
	public Type visit(ShiftExpression n) {
		Type leftExpType = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return leftExpType;
		}
		return leftExpType.getIntegerPromotedType();
	}

	/**
	 * f0 ::= ShiftLeftExpression()
	 * | ShiftRightExpression()
	 */
	@Override
	public Type visit(ShiftOptionalExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= ">>"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public Type visit(ShiftLeftExpression n) {
		return n.getF1().accept(this);
	}

	/**
	 * f0 ::= "<<"
	 * f1 ::= ShiftExpression()
	 */
	@Override
	public Type visit(ShiftRightExpression n) {
		return n.getF1().accept(this);
	}

	/**
	 * f0 ::= MultiplicativeExpression()
	 * f1 ::= ( AdditiveOptionalExpression() )?
	 */
	@Override
	public Type visit(AdditiveExpression n) {
		Type leftExpType = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return leftExpType;
		}
		Type rightExpType = n.getF1().accept(this);
		if (leftExpType instanceof ArithmeticType && rightExpType instanceof ArithmeticType) {
			return Conversion.getUsualArithmeticConvertedType(leftExpType, rightExpType);
		}
		if (leftExpType instanceof PointerType && rightExpType instanceof IntegerType) {
			return leftExpType;
		} else if (rightExpType instanceof PointerType && leftExpType instanceof IntegerType) {
			return rightExpType;
		} else {
			// TODO: Return the value of ptrdiff_t instead, as defined in <stddef.h>.
			return SignedLongIntType.type();
		}
	}

	/**
	 * f0 ::= AdditivePlusExpression()
	 * | AdditiveMinusExpression()
	 */
	@Override
	public Type visit(AdditiveOptionalExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "+"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public Type visit(AdditivePlusExpression n) {
		return n.getF1().accept(this);
	}

	/**
	 * f0 ::= "-"
	 * f1 ::= AdditiveExpression()
	 */
	@Override
	public Type visit(AdditiveMinusExpression n) {
		return n.getF1().accept(this);
	}

	/**
	 * f0 ::= CastExpression()
	 * f1 ::= ( MultiplicativeOptionalExpression() )?
	 */
	@Override
	public Type visit(MultiplicativeExpression n) {
		Type leftExpType = n.getF0().accept(this);
		if (!n.getF1().present()) {
			return leftExpType;
		}
		Type rightExpType = n.getF1().accept(this);

		return Conversion.getUsualArithmeticConvertedType(leftExpType, rightExpType);
	}

	/**
	 * f0 ::= MultiplicativeMultiExpression()
	 * | MultiplicativeDivExpression()
	 * | MultiplicativeModExpression()
	 */
	@Override
	public Type visit(MultiplicativeOptionalExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "*"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public Type visit(MultiplicativeMultiExpression n) {
		return n.getF1().accept(this);
	}

	/**
	 * f0 ::= "/"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public Type visit(MultiplicativeDivExpression n) {
		return n.getF1().accept(this);
	}

	/**
	 * f0 ::= "%"
	 * f1 ::= MultiplicativeExpression()
	 */
	@Override
	public Type visit(MultiplicativeModExpression n) {
		return n.getF1().accept(this);
	}

	/**
	 * f0 ::= CastExpressionTyped()
	 * | UnaryExpression()
	 */
	@Override
	public Type visit(CastExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "("
	 * f1 ::= TypeName()
	 * f2 ::= ")"
	 * f3 ::= CastExpression()
	 */
	@Override
	public Type visit(CastExpressionTyped n) {
		return ExpressionTypeGetter.performPointerGeneration(Type.getTypeTree(n.getF1(), Misc.getEnclosingBlock(n)));
	}

	/**
	 * f0 ::= UnaryExpressionPreIncrement()
	 * | UnaryExpressionPreDecrement()
	 * | UnarySizeofExpression()
	 * | UnaryCastExpression()
	 * | PostfixExpression()
	 */
	@Override
	public Type visit(UnaryExpression n) {
		return n.getF0().accept(this);
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public Type visit(UnaryExpressionPreIncrement n) {
		Type type = n.getF1().accept(this);
		type = ExpressionTypeGetter.performPointerDegeneration(type);
		return type;
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public Type visit(UnaryExpressionPreDecrement n) {
		Type type = n.getF1().accept(this);
		type = ExpressionTypeGetter.performPointerDegeneration(type);
		return type;
	}

	/**
	 * f0 ::= UnaryOperator()
	 * f1 ::= CastExpression()
	 */
	@Override
	public Type visit(UnaryCastExpression n) {
		NodeToken operator = (NodeToken) n.getF0().getF0().getChoice();
		Type castExpType = n.getF1().accept(this);
		switch (operator.getTokenImage()) {
		case "&":
			castExpType = ExpressionTypeGetter.performPointerDegeneration(castExpType);
			return new PointerType(castExpType);
		case "*":
			if (castExpType instanceof PointerType) {
				Type pointeeType = ((PointerType) castExpType).getPointeeType();
				pointeeType = ExpressionTypeGetter.performPointerGeneration(pointeeType);
				return pointeeType;
			}
			// assert (false);
			return null;
		case "+":
		case "-":
		case "~":
			if (castExpType == null) {
				return SignedIntType.type();
			}
			return castExpType.getIntegerPromotedType();
		case "!":
			return SignedIntType.type();
		default:
			assert (false);
			return null;
		}
	}

	/**
	 * f0 ::= SizeofTypeName()
	 * | SizeofUnaryExpression()
	 */
	@Override
	public Type visit(UnarySizeofExpression n) {
		return UnsignedLongIntType.type();
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public Type visit(SizeofUnaryExpression n) {
		return UnsignedLongIntType.type();
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= "("
	 * f2 ::= TypeName()
	 * f3 ::= ")"
	 */
	@Override
	public Type visit(SizeofTypeName n) {
		return UnsignedLongIntType.type();
	}

	/**
	 * f0 ::= "&"
	 * | "*"
	 * | "+"
	 * | "-"
	 * | "~"
	 * | "!"
	 */
	@Override
	public Type visit(UnaryOperator n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public Type visit(PostfixExpression n) {
		return ExpressionTypeGetter.getHalfPostfixExpressionType(n, null);
	}

	/**
	 * f0 ::= ( APostfixOperation() )*
	 */
	@Override
	public Type visit(PostfixOperationsList n) {
		assert (false);
		return null;
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
	public Type visit(APostfixOperation n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= "++"
	 */
	@Override
	public Type visit(PlusPlus n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= "--"
	 */
	@Override
	public Type visit(MinusMinus n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= "["
	 * f1 ::= Expression()
	 * f2 ::= "]"
	 */
	@Override
	public Type visit(BracketExpression n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= "("
	 * f1 ::= ( ExpressionList() )?
	 * f2 ::= ")"
	 */
	@Override
	public Type visit(ArgumentList n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= "."
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public Type visit(DotId n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= ->"
	 * f1 ::= <IDENTIFIER>
	 */
	@Override
	public Type visit(ArrowId n) {
		assert (false);
		return null;
	}

	/**
	 * f0 ::= <IDENTIFIER>
	 * | Constant()
	 * | ExpressionClosed()
	 */
	@Override
	public Type visit(PrimaryExpression n) {
		if (n.getF0().getChoice() instanceof NodeToken) {
			String idName = ((NodeToken) n.getF0().getChoice()).getTokenImage();
			Symbol symbol = Misc.getSymbolEntry(idName, n);
			// Old Code commented below: This code has been shifted to getSymbolEntry.
			// if (idName.startsWith("__builtin_")) {
			// String newName;
			// newName = idName.replaceAll("__builtin_", "");
			// symbol = Misc.getSymbolEntry(newName, n);
			// if (symbol == null) {
			// symbol = Misc.getSymbolEntry(idName, n);
			// }
			// }
			if (symbol == null) {
				// Misc.warnDueToLackOfFeature("Could not find any declaration for " + n + ".",
				// n);
				return null; // This may be due to usage of a function that has not been declared yet,
								// or because this method has been called in a snippet rather than a code.
			}
			Type type = symbol.getType();
			type = ExpressionTypeGetter.performPointerGeneration(type);
			return type;
		} else {
			return n.getF0().getChoice().accept(this);
		}
	}

	/**
	 * f0 ::= "("
	 * f1 ::= Expression()
	 * f2 ::= ")"
	 */
	@Override
	public Type visit(ExpressionClosed n) {
		return n.getF1().accept(this);
	}

	/**
	 * f0 ::= AssignmentExpression()
	 * f1 ::= ( "," AssignmentExpression() )*
	 */
	@Override
	public Type visit(ExpressionList n) {
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
	public Type visit(Constant n) {
		Node choiceNode = n.getF0().getChoice();
		List<ArithmeticTypeKey> arithTypeKeyList = new ArrayList<>();
		if (choiceNode instanceof NodeToken) {
			String faceValue = "";
			faceValue = ((NodeToken) n.getF0().getChoice()).getTokenImage();
			NodeToken choice = (NodeToken) choiceNode;
			if (choice.getKind() == CParserConstants.INTEGER_LITERAL) {
				if (faceValue.endsWith("l") || faceValue.endsWith("L")) {
					arithTypeKeyList.add(ArithmeticTypeKey.LONG);
				} else if (faceValue.endsWith("u") || faceValue.endsWith("U")) {
					arithTypeKeyList.add(ArithmeticTypeKey.UNSIGNED);
				}
				/*
				 * To handle "lu" and "ll" -- note that, at the time of writing
				 * this comment parser doesn't handle these suffixes anyway.
				 */
				if (faceValue.length() > 3) {
					String subStr = faceValue.substring(0, faceValue.length() - 1);
					if (subStr.endsWith("l") || subStr.endsWith("L")) {
						arithTypeKeyList.add(ArithmeticTypeKey.LONG);
					} else if (subStr.endsWith("u") || subStr.endsWith("U")) {
						arithTypeKeyList.add(ArithmeticTypeKey.UNSIGNED);
					}
				}
				arithTypeKeyList.add(ArithmeticTypeKey.INT);
				return Type.getTypeFromArithmeticKeys(arithTypeKeyList);
			} else if (choice.getKind() == CParserConstants.FLOATING_POINT_LITERAL) {
				if (faceValue.endsWith("l") || faceValue.endsWith("L")) {
					arithTypeKeyList.add(ArithmeticTypeKey.LONG);
					arithTypeKeyList.add(ArithmeticTypeKey.DOUBLE);
				} else if (faceValue.endsWith("f") || faceValue.endsWith("F")) {
					arithTypeKeyList.add(ArithmeticTypeKey.FLOAT);
				} else {
					arithTypeKeyList.add(ArithmeticTypeKey.DOUBLE);
				}
				return Type.getTypeFromArithmeticKeys(arithTypeKeyList);
			} else if (choice.getKind() == CParserConstants.CHARACTER_LITERAL) {
				arithTypeKeyList.add(ArithmeticTypeKey.CHAR);
				return Type.getTypeFromArithmeticKeys(arithTypeKeyList);
			}
		} else if (choiceNode instanceof NodeList) {
			arithTypeKeyList.add(ArithmeticTypeKey.CHAR);
			Type elemType = Type.getTypeFromArithmeticKeys(arithTypeKeyList);
			PointerType pT = new PointerType(elemType);
			return pT;
		}
		assert (false);
		return null;
	}

	@Override
	public Type visit(SimplePrimaryExpression n) {
		if (n.isAConstant()) {
			return n.getConstant().accept(this);
		} else {
			String idName = n.getIdentifier().getTokenImage();
			Symbol symbol = Misc.getSymbolEntry(idName, n);
			if (symbol == null) {
				// Misc.warnDueToLackOfFeature("Could not find any declaration for " + n + ".",
				// n);
				return null; // This may be due to usage of a function that has not been declared yet,
								// or because this method has been called on a snippet rather than a code.
			}
			Type type = symbol.getType();
			type = ExpressionTypeGetter.performPointerGeneration(type);

			return type;
		}
	}

}
