/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.solver;

import imop.ast.info.cfgNodeInfo.ForConstructInfo.SchedulingType;
import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.solver.tokens.ExpressionTokenizer;
import imop.lib.analysis.solver.tokens.IdOrConstToken;
import imop.lib.analysis.solver.tokens.OperatorToken;
import imop.lib.analysis.solver.tokens.Tokenizable;
import imop.lib.builder.Builder;
import imop.lib.util.Misc;
import imop.parser.CParserConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents range of values that a variable may take.
 * <br>
 * Important: Note that same variable may correspond to different induction
 * ranges, when used in different loops.
 * 
 * @author Aman Nougrahiya
 *
 */
public class InductionRange {
	final public Symbol inductionVariable;
	final public Node loopHeader;
	final public Node loopTail;
	final public Node stepCFGNode;
	final public List<Tokenizable> initValue;
	final public OperatorToken stepOperator; // either "+" or "-"
	final public List<Tokenizable> stepValue;
	final public OperatorToken finalOperator; // either "<=", ">=", "<", ">", or "!-"
	final public List<Tokenizable> finalValue;

	/**
	 * Obtain an induction-range for a given basic induction variable
	 * {@code inductionVariable}, whose initial definition has
	 * {@code initialDefRHS} on its RHS, update definition has
	 * {@code loopUpdateDefRHS} on its RHS, and the enclosing loop's termination
	 * predicate is {@code terminationForm}. The basic induction variable
	 * belongs to a natural loop whose back-edge has {@code loopHeader} and
	 * {@code loopTail} as its head and tail, respectively.
	 * 
	 * @param inductionVariable
	 *                          a basic induction variable.
	 * @param initValue2
	 *                          RHS (in prefix form) of the initial definition of
	 *                          the
	 *                          induction variable.
	 * @param stepOperator
	 *                          operator used to perform the update.
	 * @param stepValue2
	 *                          RHS (in prefix form) of the update definition of the
	 *                          induction
	 *                          variable.
	 * @param finalOperator
	 *                          operator (<, >, <=, >=, or !=) which is used in the
	 *                          bound
	 *                          check.
	 * @param finalValue2
	 *                          final value of the basic induction variable from the
	 *                          termination predicate.
	 * @param loopHeader
	 *                          head of the back-edge.
	 * @param loopTail
	 *                          tail of the back-edge.
	 * @return
	 *         associated induction-range. (Note that this induction range
	 *         should be properly renamed, wherever required, while creating the
	 *         system of (in)equations.)
	 */
	public InductionRange(Symbol inductionVariable, List<Tokenizable> initValue2, OperatorToken stepOperator,
			List<Tokenizable> stepValue2, OperatorToken finalOperator, List<Tokenizable> finalValue2, Node loopHeader,
			Node loopTail, Node stepCFGNode) {
		this.inductionVariable = inductionVariable;
		this.loopHeader = loopHeader;
		this.loopTail = loopTail;
		this.stepCFGNode = stepCFGNode;
		this.initValue = initValue2;
		this.stepOperator = stepOperator;
		this.stepValue = stepValue2;
		this.finalOperator = finalOperator;
		this.finalValue = finalValue2;
	}

	/**
	 * Obtain a list of prefix form expressions that represent the symbolic
	 * value range constraints.
	 * 
	 * @param tid
	 *                   thread-id for which the range has to be obtained.
	 * @param numThreads
	 *                   maximum number of threads.
	 * @return
	 *         list of prefix form expressions that represent the symbolic value
	 *         range constraints.
	 */
	public Set<List<Tokenizable>> getRanges(IdOrConstToken tid, IdOrConstToken numThreads) {
		Set<List<Tokenizable>> returnSet = new HashSet<>();
		List<Tokenizable> initList = this.getInitRange(tid, numThreads);
		if (initList != null && !initList.isEmpty()) {
			returnSet.add(initList);
		}
		List<Tokenizable> termList = this.getTermRange(tid, numThreads);
		if (termList != null && !termList.isEmpty()) {
			returnSet.add(termList);
		}
		List<Tokenizable> stepList = this.getStepRange();
		if (stepList != null && !stepList.isEmpty()) {
			returnSet.add(stepList);
		}
		return returnSet;
	}

	/**
	 * Obtain the initialization expression, such as {@code p <= i} and
	 * {@code i < (size - 1)}, corresponding to this induction range, in prefix
	 * form.
	 * <br>
	 * Note that if the termination expression's comparison sign is {@code <} or
	 * {@code <=}, we consider the range to move from lower values to higher;
	 * it's the opposite for {@code >} and {@code >=}. In case of {@code !=} as
	 * the comparison sign in the termination expression of the loop, we try to
	 * find whether the variable is being incremented or decremented in each
	 * step-expression; if we cannot do that, we do not create any
	 * initialization constraint.
	 * 
	 * @return
	 *         initialization ranges in prefix form, if identifiable.
	 */
	public List<Tokenizable> getInitRange() {
		return this.getInitRange(IdOrConstToken.getNewConstantToken("0"), IdOrConstToken.getNewConstantToken("1"));
	}

	/**
	 * Obtain the initialization expression, such as {@code p <= i} and
	 * {@code i < (size - 1)}, corresponding to this induction range, in prefix
	 * form, for the given thread id.
	 * <br>
	 * Note that if the termination expression's comparison sign is {@code <} or
	 * {@code <=}, we consider the range to move from lower values to higher;
	 * it's the opposite for {@code >} and {@code >=}. In case of {@code !=} as
	 * the comparison sign in the termination expression of the loop, we try to
	 * find whether the variable is being incremented or decremented in each
	 * step-expression; if we cannot do that, we do not create any
	 * initialization constraint.
	 * 
	 * @param tid
	 *            thread id.
	 * @return
	 *         initialization ranges in prefix form, if identifiable.
	 */
	public List<Tokenizable> getInitRange(IdOrConstToken tid, IdOrConstToken numThreads) {
		if (this.hasStaticScheduling()) {
			return this.getParallelInitRange(tid, numThreads);
		}
		List<Tokenizable> assertion = new ArrayList<>();
		OperatorToken initOperator = this.loopMovesForward() ? OperatorToken.GE : null;
		if (initOperator == null) {
			initOperator = this.loopMovesBackwards() ? OperatorToken.LE : null;
		}
		if (initOperator == null) {
			return assertion;
		} else {
			Node cfgNode = ExpressionTokenizer.getCFGNode(initValue);
			assertion.add(initOperator);
			NodeToken newIdentifier = new NodeToken(this.inductionVariable.toString().trim());
			newIdentifier.setKind(CParserConstants.IDENTIFIER);
			IdOrConstToken idOrC = new IdOrConstToken(newIdentifier, cfgNode);
			assertion.add(idOrC);
			assertion.addAll(initValue);
			return assertion;
		}

	}

	/**
	 * Checks if the direction of the loop is statically known to be forward.
	 * 
	 * @return
	 *         true, if the loop moves forward.
	 */
	public boolean loopMovesForward() {
		if (finalOperator == OperatorToken.LE || finalOperator == OperatorToken.LT) {
			return true;
		} else if (finalOperator == OperatorToken.GE || finalOperator == OperatorToken.GT) {
			return false;
		} else if (finalOperator == OperatorToken.ISNEQ) {
			if (stepOperator == OperatorToken.PLUS) {
				if (ExpressionTokenizer.isKnownPositive(stepValue)) {
					return true;
				} else if (ExpressionTokenizer.isKnownNegative(stepValue)) {
					return false;
				} else {
					return false;
				}
			} else if (stepOperator == OperatorToken.MINUS) {
				if (ExpressionTokenizer.isKnownPositive(stepValue)) {
					return false;
				} else if (ExpressionTokenizer.isKnownNegative(stepValue)) {
					return true;
				} else {
					return false;
				}
			} else {
				assert (false);
				return false;
			}
		} else {
			assert (false);
			return false;
		}
	}

	/**
	 * Checks if the direction of the loop is statically known to be backwards.
	 * 
	 * @return
	 *         true, if the loop moves backwards.
	 */
	public boolean loopMovesBackwards() {
		if (finalOperator == OperatorToken.LE || finalOperator == OperatorToken.LT) {
			return false;
		} else if (finalOperator == OperatorToken.GE || finalOperator == OperatorToken.GT) {
			return true;
		} else if (finalOperator == OperatorToken.ISNEQ) {
			if (stepOperator == OperatorToken.PLUS) {
				if (ExpressionTokenizer.isKnownPositive(stepValue)) {
					return false;
				} else if (ExpressionTokenizer.isKnownNegative(stepValue)) {
					return true;
				} else {
					return false;
				}
			} else if (stepOperator == OperatorToken.MINUS) {
				if (ExpressionTokenizer.isKnownPositive(stepValue)) {
					return true;
				} else if (ExpressionTokenizer.isKnownNegative(stepValue)) {
					return false;
				} else {
					return false;
				}
			} else {
				assert (false);
				return false;
			}
		} else {
			assert (false);
			return false;
		}
	}

	/**
	 * For induction ranges where the variable gets incremented/decremented by
	 * more than one, this method returns a constraint (in prefix form) that
	 * asserts the divisibility of variable ({@code -} initial value) by the
	 * step value, for the given thread id.
	 * 
	 * @param tid
	 *            thread id.
	 * 
	 * @return
	 *         divisibility constraint on the induction variable, in prefix
	 *         form, if any.
	 */
	public List<Tokenizable> getStepRange() {
		List<Tokenizable> assertion = new ArrayList<>();
		Node cfgNode = ExpressionTokenizer.getCFGNode(this.stepValue);
		/*
		 * If stepValue is "1", then we do not need this constraint.
		 */
		if (stepValue.size() == 1) {
			Tokenizable step = stepValue.get(0);
			if (step.toString().equals("1")) {
				return assertion;
			}
		}
		assertion.add(OperatorToken.ASSIGN);
		assertion.add(OperatorToken.MOD);
		assertion.add(OperatorToken.MINUS);
		NodeToken newIdentifier = new NodeToken(this.inductionVariable.toString().trim());
		newIdentifier.setKind(CParserConstants.IDENTIFIER);
		IdOrConstToken idOrC = new IdOrConstToken(newIdentifier, cfgNode);
		assertion.add(idOrC);
		assertion.addAll(this.initValue);
		assertion.addAll(this.stepValue);
		IdOrConstToken zero = IdOrConstToken.getNewConstantToken("0");
		assertion.add(zero);
		return assertion;
	}

	/**
	 * This method returns the upper/lower bound termination constraint for the
	 * induction variable.
	 * 
	 * @return
	 *         the termination bound on the induction variable, in prefix form.
	 */
	public List<Tokenizable> getTermRange() {
		return this.getTermRange(IdOrConstToken.getNewConstantToken("0"), IdOrConstToken.getNewConstantToken("1"));
	}

	/**
	 * This method returns the upper/lower bound termination constraint for the
	 * induction variable, for the given thread.
	 * 
	 * @param tid
	 *            thread id.
	 * 
	 * @return
	 *         the termination bound on the induction variable, in prefix form.
	 */
	public List<Tokenizable> getTermRange(IdOrConstToken tid, IdOrConstToken numThreads) {
		if (this.hasStaticScheduling()) {
			return this.getParallelTermRange(tid, numThreads);
		}
		List<Tokenizable> assertion = new ArrayList<>();
		Node cfgNode = ExpressionTokenizer.getCFGNode(this.finalValue);
		assertion.add(this.finalOperator);
		NodeToken newIdentifier = new NodeToken(this.inductionVariable.toString().trim());
		newIdentifier.setKind(CParserConstants.IDENTIFIER);
		IdOrConstToken idOrC = new IdOrConstToken(newIdentifier, cfgNode);
		assertion.add(idOrC);
		assertion.addAll(this.finalValue);
		return assertion;
	}

	private List<Tokenizable> getParallelInitRange(IdOrConstToken tid, IdOrConstToken numThreads) {
		List<Tokenizable> assertion = new ArrayList<>();
		OperatorToken initOperator = null;
		OperatorToken directionOperator = null;
		if (this.loopMovesForward()) {
			initOperator = OperatorToken.GE;
			directionOperator = OperatorToken.PLUS;
		} else if (this.loopMovesBackwards()) {
			initOperator = OperatorToken.LE;
			directionOperator = OperatorToken.MINUS;
		} else {
			return assertion;
		}
		// Create the assertion here.
		Node cfgNode = ExpressionTokenizer.getCFGNode(initValue);
		assertion.add(initOperator);
		NodeToken newIdentifier = new NodeToken(this.inductionVariable.toString().trim());
		newIdentifier.setKind(CParserConstants.IDENTIFIER);
		IdOrConstToken idOrC = new IdOrConstToken(newIdentifier, cfgNode);
		assertion.add(idOrC);
		assertion.add(directionOperator);
		assertion.addAll(this.initValue);
		assertion.add(OperatorToken.MUL);
		assertion.add(tid);
		assertion.addAll(this.getStaticChunkSize(numThreads));
		return assertion;
	}

	private List<Tokenizable> getParallelTermRange(IdOrConstToken tid, IdOrConstToken numThreads) {
		List<Tokenizable> assertion = new ArrayList<>();
		OperatorToken lastOperator = null;
		OperatorToken directionOperator = null;
		if (this.loopMovesForward()) {
			lastOperator = OperatorToken.LT;
			directionOperator = OperatorToken.PLUS;
		} else if (this.loopMovesBackwards()) {
			lastOperator = OperatorToken.GT;
			directionOperator = OperatorToken.MINUS;
		} else {
			return assertion;
		}

		List<Tokenizable> nextChunk = new ArrayList<>();
		nextChunk.add(directionOperator);
		nextChunk.add(directionOperator);
		nextChunk.addAll(this.initValue);
		nextChunk.add(OperatorToken.MUL);
		nextChunk.add(tid);
		nextChunk.addAll(this.getStaticChunkSize(numThreads));
		nextChunk.addAll(this.getStaticChunkSize(numThreads));

		List<Tokenizable> borderValue = new ArrayList<>();
		if (this.finalOperator == OperatorToken.LE || this.finalOperator == OperatorToken.GE) {
			borderValue.add(directionOperator);
			borderValue.addAll(this.finalValue);
			borderValue.add(IdOrConstToken.getNewConstantToken("1"));
		} else {
			borderValue.addAll(this.finalValue);
		}

		List<Tokenizable> rhsTerm = null;
		if (directionOperator == OperatorToken.PLUS) {
			/*
			 * TODO: Think about these options below; first one is precise but
			 * inefficient (109s), whereas second option is (theoretically)
			 * imprecise, but efficient (17s).
			 */
			// rhsTerm = ExpressionTokenizer.getMin(nextChunk, borderValue);
			rhsTerm = nextChunk;
		} else if (directionOperator == OperatorToken.MINUS) {
			// rhsTerm = ExpressionTokenizer.getMax(nextChunk, borderValue);
			rhsTerm = nextChunk;
		} else {
			assert (false);
		}

		Node cfgNode = ExpressionTokenizer.getCFGNode(initValue);
		assertion.add(lastOperator);
		NodeToken newIdentifier = new NodeToken(this.inductionVariable.toString().trim());
		newIdentifier.setKind(CParserConstants.IDENTIFIER);
		IdOrConstToken idOrC = new IdOrConstToken(newIdentifier, cfgNode);
		assertion.add(idOrC);
		assertion.addAll(rhsTerm);
		return assertion;
	}

	/**
	 * Obtain the chunk size of this iteration, in the prefix form, for
	 * statically scheduled lists.
	 * 
	 * @param numThreads
	 *                   total number of threads (symbolically).
	 * @return
	 *         chunk size in prefix form, if this loop is statically scheduled;
	 *         else an empty list.
	 */
	public List<Tokenizable> getStaticChunkSize(IdOrConstToken numThreads) {
		List<Tokenizable> sizeList = new ArrayList<>();
		if (!this.hasStaticScheduling()) {
			sizeList.add(IdOrConstToken.getNewIdToken(Builder.getNewTempName("UNKNOWNSIZE")));
			return sizeList;
		}
		OperatorToken directionOperator = null;
		if (this.loopMovesForward()) {
			directionOperator = OperatorToken.PLUS;
		} else if (this.loopMovesBackwards()) {
			directionOperator = OperatorToken.MINUS;
		} else {
			sizeList.add(IdOrConstToken.getNewIdToken(Builder.getNewTempName("UNKNOWNSIZE")));
			return sizeList;
		}

		List<Tokenizable> rangeSize = new ArrayList<>();
		rangeSize.add(OperatorToken.MINUS);
		if (directionOperator == OperatorToken.PLUS) {
			rangeSize.addAll(finalValue);
			rangeSize.addAll(initValue);
		} else {
			rangeSize.addAll(initValue);
			rangeSize.addAll(finalValue);
		}
		if (finalOperator == OperatorToken.GE || finalOperator == OperatorToken.LE) {
			rangeSize.add(0, OperatorToken.PLUS);
			rangeSize.add(IdOrConstToken.getNewConstantToken("1"));
		}

		sizeList.add(OperatorToken.PLUS);
		sizeList.add(OperatorToken.DIV);
		sizeList.addAll(rangeSize);
		sizeList.add(numThreads);
		sizeList.add(OperatorToken.MOD);
		sizeList.addAll(rangeSize);
		sizeList.add(numThreads);

		return sizeList;
	}

	/**
	 * Checks whether this loop has static scheduling.
	 * 
	 * @return
	 *         {@code true}, if this loop has static scheduling; false,
	 *         otherwise (and also for serial loops).
	 */
	public boolean hasStaticScheduling() {
		if (!(this.stepCFGNode instanceof OmpForReinitExpression)) {
			return false;
		}
		ForConstruct encloser = Misc.getEnclosingNode(this.stepCFGNode, ForConstruct.class);
		if (encloser == null) {
			return false;
		}
		return encloser.getInfo().getSchedulingType() == SchedulingType.STATIC;
	}

	@Override
	public String toString() {
		return "InductionRange [initRange:" + getInitRange() + ", stepRange:" + getStepRange() + ", termRange:"
				+ getTermRange() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inductionVariable == null) ? 0 : inductionVariable.hashCode());
		result = prime * result + ((loopHeader == null) ? 0 : loopHeader.hashCode());
		result = prime * result + ((loopTail == null) ? 0 : loopTail.hashCode());
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
		InductionRange other = (InductionRange) obj;
		if (inductionVariable == null) {
			if (other.inductionVariable != null) {
				return false;
			}
		} else if (!inductionVariable.equals(other.inductionVariable)) {
			return false;
		}
		if (finalOperator != other.finalOperator) {
			return false;
		}
		if (finalValue == null) {
			if (other.finalValue != null) {
				return false;
			}
		} else if (!finalValue.equals(other.finalValue)) {
			return false;
		}
		if (initValue == null) {
			if (other.initValue != null) {
				return false;
			}
		} else if (!initValue.equals(other.initValue)) {
			return false;
		}
		if (loopHeader == null) {
			if (other.loopHeader != null) {
				return false;
			}
		} else if (!loopHeader.equals(other.loopHeader)) {
			return false;
		}
		if (loopTail == null) {
			if (other.loopTail != null) {
				return false;
			}
		} else if (!loopTail.equals(other.loopTail)) {
			return false;
		}
		if (stepOperator != other.stepOperator) {
			return false;
		}
		if (stepValue == null) {
			if (other.stepValue != null) {
				return false;
			}
		} else if (!stepValue.equals(other.stepValue)) {
			return false;
		}
		return true;
	}

}
