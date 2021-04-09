/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.deprecated;

import imop.baseVisitor.GJNoArguDepthFirstProcess;
import imop.lib.analysis.flowanalysis.Cell;

import java.util.List;

/**
 * This class populates sharedLvalueReadList and sharedLvalueWriteList with the
 * accesses made in the called node.
 * For each visit of the node n,
 * if node n is an lvalue
 * return the symbols to its parents.
 * else
 * return null;
 * 
 * if node n has some lvalues not representing the whole of n
 * add these lvalues to sharedLvalueReadList/sharedLvalueWriteList.
 */
@Deprecated
public class Deprecated_SharedLvalueAccessGetter extends GJNoArguDepthFirstProcess<List<Cell>> {
	// public Set<Cell> sharedLvalueReadList = new HashSet<>();
	// public Set<Cell> sharedLvalueWriteList = new HashSet<>();
	//
	// /**
	// * Adds a non-null symbol vector to sharedLvalueReadList
	// *
	// * @param sym
	// */
	// public void addReads(List<Cell> sym, Node n) {
	// if (sym != null) {
	// for (Cell s : sym) {
	// if (s != null && n.getInfo().getSharingAttribute(s) ==
	// DataSharingAttribute.SHARED) {
	// sharedLvalueReadList.add(s);
	// }
	// }
	// }
	// }
	//
	// /**
	// * Adds a non-null symbol vector to sharedLvalueWriteList
	// *
	// * @param sym
	// */
	// public void addWrites(List<Cell> sym, Node n) {
	// if (sym != null) {
	// for (Cell s : sym) {
	// if (s != null && n.getInfo().getSharingAttribute(s) ==
	// DataSharingAttribute.SHARED) {
	// sharedLvalueWriteList.add(s);
	// }
	// }
	// }
	// }
	//
	// /**
	// * @param pointerSet:
	// * List of pointer symbols.
	// * @param nodeSet
	// * List of nodes.
	// * @return a set of symbols pointed to by elements of {@code pointerSet} at
	// * corresponding elements of {@code nodeSet}.
	// */
	// public Set<Cell> getPointsToSet(List<Cell> pointerSet, List<Node> nodeSet) {
	// Set<Cell> pointsToSet = new HashSet<>();
	// if (pointerSet == null) {
	// return pointsToSet;
	// }
	// for (Cell pointer : pointerSet) {
	// int index = pointerSet.indexOf(pointer);
	// if (pointer != null) {
	// pointsToSet.addAll(pointer.getPointsTo(nodeSet.get(index)));
	// }
	// }
	// return pointsToSet;
	// }
	//
	// /**
	// * @param pointerSet:
	// * List of pointer symbols
	// * @param nodeSet
	// * List of nodes.
	// * @return a set of symbols pointed to by elements of pointerSet, and
	// * those pointed to by these symbols and so on...
	// */
	// public Set<Cell> getPointsToClosure(List<Cell> pointerSet, List<Node>
	// nodeSet) {
	// Set<Cell> pointsToClosure = new HashSet<>();
	// int oldSize = 0, newSize;
	// if (pointerSet == null) {
	// return pointsToClosure;
	// }
	// while (true) {
	// for (Cell cell : pointerSet) {
	// int index = pointerSet.indexOf(cell);
	// if (cell instanceof Cell) {
	// Cell sym = (Cell) cell;
	// if (sym != null) {
	// pointsToClosure.addAll(sym.getPointsTo(nodeSet.get(index)));
	// }
	// }
	// }
	// newSize = pointsToClosure.size();
	// if (newSize > oldSize) {
	// oldSize = newSize;
	// pointerSet = new ArrayList<>(pointsToClosure);
	// continue;
	// } else {
	// break;
	// }
	// }
	// return pointsToClosure;
	// }
	//
	// /**
	// * f0 ::= Declarator()
	// * f1 ::= ( "=" Initializer() )?
	// */
	// @Override
	// public List<Cell> visit(InitDeclarator n) {
	// if (n.getF1().getNode() != null) {
	// Cell sym = Misc.getSymbolEntry(Misc.getRootIdName(n.getF0()), n.getF0());
	// if (sym == null) {
	// System.out.println(
	// "Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::InitDeclarator");
	// System.exit(0);
	// }
	// sharedLvalueWriteList.add(sym);
	// addReads(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this),
	// n);
	// }
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * f1 ::= "="
	// * f2 ::= Expression()
	// */
	// @Override
	// public List<Cell> visit(OmpForInitExpression n) {
	// Cell sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n.getF0());
	// if (sym == null) {
	// System.out.println(
	// "Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::OmpForInitExpression");
	// System.exit(0);
	// }
	// sharedLvalueWriteList.add(sym);
	// addReads(n.getF2().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * f1 ::= "<"
	// * f2 ::= Expression()
	// */
	// @Override
	// public List<Cell> visit(OmpForLTCondition n) {
	// Cell sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n.getF0());
	// sharedLvalueReadList.add(sym);
	// addReads(n.getF2().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * f1 ::= "<="
	// * f2 ::= Expression()
	// */
	// @Override
	// public List<Cell> visit(OmpForLECondition n) {
	// Cell sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n.getF0());
	// sharedLvalueReadList.add(sym);
	// addReads(n.getF2().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * f1 ::= ">"
	// * f2 ::= Expression()
	// */
	// @Override
	// public List<Cell> visit(OmpForGTCondition n) {
	// Cell sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n.getF0());
	// sharedLvalueReadList.add(sym);
	// addReads(n.getF2().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * f1 ::= ">="
	// * f2 ::= Expression()
	// */
	// @Override
	// public List<Cell> visit(OmpForGECondition n) {
	// Cell sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n.getF0());
	// sharedLvalueReadList.add(sym);
	// addReads(n.getF2().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * f1 ::= "++"
	// */
	// @Override
	// public List<Cell> visit(PostIncrementId n) {
	// Cell sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n);
	// if (sym == null) {
	// System.out.println(
	// "Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::PostIncrementId");
	// System.exit(0);
	// }
	// sharedLvalueReadList.add(sym);
	// sharedLvalueWriteList.add(sym);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * f1 ::= "--"
	// */
	// @Override
	// public List<Cell> visit(PostDecrementId n) {
	// Cell sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n);
	// if (sym == null) {
	// System.out.println(
	// "Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::PostDecrementId");
	// System.exit(0);
	// }
	// sharedLvalueReadList.add(sym);
	// sharedLvalueWriteList.add(sym);
	// return null;
	// }
	//
	// /**
	// * f0 ::= "++"
	// * f1 ::= <IDENTIFIER>
	// */
	// @Override
	// public List<Cell> visit(PreIncrementId n) {
	// Cell sym = Misc.getSymbolEntry(n.getF1().getTokenImage(), n);
	// if (sym == null) {
	// System.out
	// .println("Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::PreIncrementId");
	// System.exit(0);
	// }
	// sharedLvalueReadList.add(sym);
	// sharedLvalueWriteList.add(sym);
	// return null;
	// }
	//
	// /**
	// * f0 ::= "--"
	// * f1 ::= <IDENTIFIER>
	// */
	// @Override
	// public List<Cell> visit(PreDecrementId n) {
	// Cell sym = Misc.getSymbolEntry(n.getF1().getTokenImage(), n);
	// if (sym == null) {
	// System.out
	// .println("Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::PreDecrementId");
	// System.exit(0);
	// }
	// sharedLvalueReadList.add(sym);
	// sharedLvalueWriteList.add(sym);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * f1 ::= "+="
	// * f2 ::= Expression()
	// */
	// @Override
	// public List<Cell> visit(ShortAssignPlus n) {
	// Cell sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n);
	// if (sym == null) {
	// System.out.println(
	// "Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::ShortAssignPlus");
	// System.exit(0);
	// }
	// sharedLvalueReadList.add(sym);
	// sharedLvalueWriteList.add(sym);
	// addReads(n.getF2().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * f1 ::= "-="
	// * f2 ::= Expression()
	// */
	// @Override
	// public List<Cell> visit(ShortAssignMinus n) {
	// Cell sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n);
	// if (sym == null) {
	// System.out.println(
	// "Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::ShortAssignMinus");
	// System.exit(0);
	// }
	// sharedLvalueReadList.add(sym);
	// sharedLvalueWriteList.add(sym);
	// addReads(n.getF2().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * f1 ::= "="
	// * f2 ::= <IDENTIFIER>
	// * f3 ::= "+"
	// * f4 ::= AdditiveExpression()
	// */
	// @Override
	// public List<Cell> visit(OmpForAdditive n) {
	// Cell sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n);
	// if (sym == null) {
	// System.out.println(
	// "Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::OmpForAdditive (1)");
	// System.exit(0);
	// }
	// sharedLvalueWriteList.add(sym);
	// sym = Misc.getSymbolEntry(n.getF2().getTokenImage(), n);
	// if (sym == null) {
	// System.out.println(
	// "Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::OmpForAdditive (2)");
	// System.exit(0);
	// }
	// sharedLvalueReadList.add(sym);
	// addReads(n.getF4().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * f1 ::= "="
	// * f2 ::= <IDENTIFIER>
	// * f3 ::= "-"
	// * f4 ::= AdditiveExpression()
	// */
	// @Override
	// public List<Cell> visit(OmpForSubtractive n) {
	// Cell sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n);
	// if (sym == null) {
	// System.out.println(
	// "Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::OmpForSubtractive (1)");
	// System.exit(0);
	// }
	// sharedLvalueWriteList.add(sym);
	// sym = Misc.getSymbolEntry(n.getF2().getTokenImage(), n);
	// if (sym == null) {
	// System.out.println(
	// "Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::OmpForSubtractive (2)");
	// System.exit(0);
	// }
	// sharedLvalueReadList.add(sym);
	// addReads(n.getF4().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * f1 ::= "="
	// * f2 ::= MultiplicativeExpression()
	// * f3 ::= "+"
	// * f4 ::= <IDENTIFIER>
	// */
	// @Override
	// public List<Cell> visit(OmpForMultiplicative n) {
	// Cell sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n);
	// if (sym == null) {
	// System.out.println(
	// "Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::OmpForMultiplicative (1)");
	// System.exit(0);
	// }
	// sharedLvalueWriteList.add(sym);
	// sym = Misc.getSymbolEntry(n.getF4().getTokenImage(), n);
	// if (sym == null) {
	// System.out.println(
	// "Some problem with symbol table updates! Exiting from
	// LvalueAccessGetter::OmpForMultiplicative (2)");
	// System.exit(0);
	// }
	// sharedLvalueReadList.add(sym);
	// addReads(n.getF2().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <RETURN>
	// * f1 ::= ( Expression() )?
	// * f2 ::= ";"
	// */
	// @Override
	// public List<Cell> visit(ReturnStatement n) {
	// addReads(n.getF1().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= AssignmentExpression()
	// * f1 ::= ( "," AssignmentExpression() )*
	// */
	// @Override
	// public List<Cell> visit(Expression n) {
	// if (n.getExpF1().getNodes().isEmpty()) {
	// return n.getExpF0().accept(this);
	// } else {
	// addReads(n.getExpF0().accept(this), n);
	// for (Node seq : n.getExpF1().getNodes()) {
	// assert seq instanceof NodeSequence;
	// AssignmentExpression aE = (AssignmentExpression) ((NodeSequence)
	// seq).getNodes().get(1);
	// addReads(aE.accept(this), n);
	// }
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= NonConditionalExpression()
	// * | ConditionalExpression()
	// */
	// @Override
	// public List<Cell> visit(AssignmentExpression n) {
	// return n.getF0().accept(this);
	// }
	//
	// /**
	// * f0 ::= UnaryExpression()
	// * f1 ::= AssignmentOperator()
	// * f2 ::= AssignmentExpression()
	// */
	// @Override
	// public List<Cell> visit(NonConditionalExpression n) {
	// // UnaryExpression may either be an lvalue, or not,
	// // but in this case, it must be an lvalue since it is on LHS of assignment
	// String operator = ((NodeToken)
	// n.getF1().getF0().getChoice()).getTokenImage();
	// if (operator.equals("=")) {
	// // UnaryExpression is only written to
	// addWrites(n.getF0().accept(this), n);
	// //addWrites(symList);
	// } else {
	// // UnaryExpression is both read and written
	// List<Cell> sym = (n.getF0().accept(this));
	// addReads(sym, n);
	// addWrites(sym, n);
	// }
	// addReads(n.getF2().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= LogicalORExpression()
	// * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
	// */
	// @Override
	// public List<Cell> visit(ConditionalExpression n) {
	// if (n.getF1().getNode() == null) {
	// return n.getF0().accept(this);
	// } else {
	// NodeSequence seq = (NodeSequence) n.getF1().getNode();
	// Expression exp = (Expression) seq.getNodes().get(1);
	// ConditionalExpression condExp = (ConditionalExpression)
	// seq.getNodes().get(3);
	//
	// addReads(n.getF0().accept(this), n);
	// addReads(exp.accept(this), n);
	// addReads(condExp.accept(this), n);
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= ConditionalExpression()
	// */
	// @Override
	// public List<Cell> visit(ConstantExpression n) {
	// addReads(n.getF0().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= LogicalANDExpression()
	// * f1 ::= ( "||" LogicalORExpression() )?
	// */
	// @Override
	// public List<Cell> visit(LogicalORExpression n) {
	// if (n.getF1().getNode() == null) {
	// return n.getF0().accept(this);
	// } else {
	// addReads(n.getF0().accept(this), n);
	// addReads(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this),
	// n);
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= InclusiveORExpression()
	// * f1 ::= ( "&&" LogicalANDExpression() )?
	// */
	// @Override
	// public List<Cell> visit(LogicalANDExpression n) {
	// if (n.getF1().getNode() == null) {
	// return n.getF0().accept(this);
	// } else {
	// addReads(n.getF0().accept(this), n);
	// addReads(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this),
	// n);
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= ExclusiveORExpression()
	// * f1 ::= ( "|" InclusiveORExpression() )?
	// */
	// @Override
	// public List<Cell> visit(InclusiveORExpression n) {
	// if (n.getF1().getNode() == null) {
	// return n.getF0().accept(this);
	// } else {
	// addReads(n.getF0().accept(this), n);
	// addReads(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this),
	// n);
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= ANDExpression()
	// * f1 ::= ( "^" ExclusiveORExpression() )?
	// */
	// @Override
	// public List<Cell> visit(ExclusiveORExpression n) {
	// if (n.getF1().getNode() == null) {
	// return n.getF0().accept(this);
	// } else {
	// addReads(n.getF0().accept(this), n);
	// addReads(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this),
	// n);
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= EqualityExpression()
	// * f1 ::= ( "&" ANDExpression() )?
	// */
	// @Override
	// public List<Cell> visit(ANDExpression n) {
	// if (n.getF1().getNode() == null) {
	// return n.getF0().accept(this);
	// } else {
	// addReads(n.getF0().accept(this), n);
	// addReads(((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this),
	// n);
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= RelationalExpression()
	// * f1 ::= ( EqualOptionalExpression() )?
	// */
	// @Override
	// public List<Cell> visit(EqualityExpression n) {
	// if (n.getF1().getNode() == null) {
	// return n.getF0().accept(this);
	// } else {
	// addReads(n.getF0().accept(this), n);
	// addReads(n.getF1().getNode().accept(this), n);
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= EqualExpression()
	// * | NonEqualExpression()
	// */
	// @Override
	// public List<Cell> visit(EqualOptionalExpression n) {
	// return n.getF0().accept(this);
	// }
	//
	// /**
	// * f0 ::= "=="
	// * f1 ::= EqualityExpression()
	// */
	// @Override
	// public List<Cell> visit(EqualExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= "!="
	// * f1 ::= EqualityExpression()
	// */
	// @Override
	// public List<Cell> visit(NonEqualExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= ShiftExpression()
	// * f1 ::= ( RelationalOptionalExpression() )?
	// */
	// @Override
	// public List<Cell> visit(RelationalExpression n) {
	// if (n.getRelExpF1().getNode() == null) {
	// return n.getRelExpF0().accept(this);
	// } else {
	// addReads(n.getRelExpF0().accept(this), n);
	// addReads(n.getRelExpF1().getNode().accept(this), n);
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= RelationalLTExpression()
	// * | RelationalGTExpression()
	// * | RelationalLEExpression()
	// * | RelationalGEExpression()
	// */
	// @Override
	// public List<Cell> visit(RelationalOptionalExpression n) {
	// return n.getF0().accept(this);
	// }
	//
	// /**
	// * f0 ::= "<"
	// * f1 ::= RelationalExpression()
	// */
	// @Override
	// public List<Cell> visit(RelationalLTExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= ">"
	// * f1 ::= RelationalExpression()
	// */
	// @Override
	// public List<Cell> visit(RelationalGTExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= "<="
	// * f1 ::= RelationalExpression()
	// */
	// @Override
	// public List<Cell> visit(RelationalLEExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= ">="
	// * f1 ::= RelationalExpression()
	// */
	// @Override
	// public List<Cell> visit(RelationalGEExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= AdditiveExpression()
	// * f1 ::= ( ShiftOptionalExpression() )?
	// */
	// @Override
	// public List<Cell> visit(ShiftExpression n) {
	// if (n.getF1().getNode() == null) {
	// return n.getF0().accept(this);
	// } else {
	// addReads(n.getF0().accept(this), n);
	// addReads(n.getF1().getNode().accept(this), n);
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= ShiftLeftExpression()
	// * | ShiftRightExpression()
	// */
	// @Override
	// public List<Cell> visit(ShiftOptionalExpression n) {
	// return n.getF0().accept(this);
	// }
	//
	// /**
	// * f0 ::= ">>"
	// * f1 ::= ShiftExpression()
	// */
	// @Override
	// public List<Cell> visit(ShiftLeftExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= "<<"
	// * f1 ::= ShiftExpression()
	// */
	// @Override
	// public List<Cell> visit(ShiftRightExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= MultiplicativeExpression()
	// * f1 ::= ( AdditiveOptionalExpression() )?
	// */
	// @Override
	// public List<Cell> visit(AdditiveExpression n) {
	// if (n.getF1().getNode() == null) {
	// return n.getF0().accept(this);
	// } else {
	// addReads(n.getF0().accept(this), n);
	// addReads(n.getF1().getNode().accept(this), n);
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= AdditivePlusExpression()
	// * | AdditiveMinusExpression()
	// */
	// @Override
	// public List<Cell> visit(AdditiveOptionalExpression n) {
	// return n.getF0().accept(this);
	// }
	//
	// /**
	// * f0 ::= "+"
	// * f1 ::= AdditiveExpression()
	// */
	// @Override
	// public List<Cell> visit(AdditivePlusExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= "-"
	// * f1 ::= AdditiveExpression()
	// */
	// @Override
	// public List<Cell> visit(AdditiveMinusExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= CastExpression()
	// * f1 ::= ( MultiplicativeOptionalExpression() )?
	// */
	// @Override
	// public List<Cell> visit(MultiplicativeExpression n) {
	// if (n.getF1().getNode() == null) {
	// return n.getF0().accept(this);
	// } else {
	// addReads(n.getF0().accept(this), n);
	// addReads(n.getF1().getNode().accept(this), n);
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= MultiplicativeMultiExpression()
	// * | MultiplicativeDivExpression()
	// * | MultiplicativeModExpression()
	// */
	// @Override
	// public List<Cell> visit(MultiplicativeOptionalExpression n) {
	// return n.getF0().accept(this);
	// }
	//
	// /**
	// * f0 ::= "*"
	// * f1 ::= MultiplicativeExpression()
	// */
	// @Override
	// public List<Cell> visit(MultiplicativeMultiExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= "/"
	// * f1 ::= MultiplicativeExpression()
	// */
	// @Override
	// public List<Cell> visit(MultiplicativeDivExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= "%"
	// * f1 ::= MultiplicativeExpression()
	// */
	// @Override
	// public List<Cell> visit(MultiplicativeModExpression n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= CastExpressionTyped()
	// * | UnaryExpression()
	// */
	// @Override
	// public List<Cell> visit(CastExpression n) {
	// return n.getF0().accept(this);
	// }
	//
	// /**
	// * f0 ::= "("
	// * f1 ::= TypeName()
	// * f2 ::= ")"
	// * f3 ::= CastExpression()
	// */
	// @Override
	// public List<Cell> visit(CastExpressionTyped n) {
	// addReads(n.getF3().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= UnaryExpressionPreIncrement()
	// * | UnaryExpressionPreDecrement()
	// * | UnarySizeofExpression()
	// * | UnaryCastExpression()
	// * | PostfixExpression()
	// */
	// @Override
	// public List<Cell> visit(UnaryExpression n) {
	// return n.getF0().accept(this);
	// }
	//
	// /**
	// * f0 ::= "++"
	// * f1 ::= UnaryExpression()
	// */
	// @Override
	// public List<Cell> visit(UnaryExpressionPreIncrement n) {
	// List<Cell> symList = n.getF1().accept(this);
	// addReads(symList, n);
	// addWrites(symList, n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= "--"
	// * f1 ::= UnaryExpression()
	// */
	// @Override
	// public List<Cell> visit(UnaryExpressionPreDecrement n) {
	// List<Cell> symList = n.getF1().accept(this);
	// addReads(symList, n);
	// addWrites(symList, n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= UnaryOperator()
	// * f1 ::= CastExpression()
	// */
	// @Override
	// public List<Cell> visit(UnaryCastExpression n) {
	// // Obtain the list of symbols referred by castExpression as a whole
	// List<Cell> symList = n.getF1().accept(this);
	// Node enclosingCFGNode = Misc.getCFGNodeFor(n);
	// List<Node> nodeList = new ArrayList<>();
	// nodeList.add(enclosingCFGNode);
	// String operator = ((NodeToken)
	// n.getF0().getF0().getChoice()).getTokenImage();
	// switch (operator) {
	// case "&":
	// // &x doesn't read or write to the value of x, so this is not an access to x.
	// return null;
	// case "*":
	// // *x reads/writes to all those symbols which are pointed to by x,
	// // and it also reads from x.
	// addReads(symList, n);
	// return (new ArrayList<>(getPointsToSet(symList, nodeList)));
	// case "+":
	// case "-":
	// case "~":
	// case "!":
	// addReads(symList, n);
	// return null;
	// default:
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= SizeofTypeName()
	// * | SizeofUnaryExpression()
	// */
	// @Override
	// public List<Cell> visit(UnarySizeofExpression n) {
	// return n.getF0().accept(this);
	// }
	//
	// /**
	// * f0 ::= <SIZEOF>
	// * f1 ::= UnaryExpression()
	// */
	// @Override
	// public List<Cell> visit(SizeofUnaryExpression n) {
	// // n.f1.accept(this);
	// // Expression is not evaluated when present as the argument to sizeof
	// expression
	// return null;
	// }
	//
	// /**
	// * f0 ::= <SIZEOF>
	// * f1 ::= "("
	// * f2 ::= TypeName()
	// * f3 ::= ")"
	// */
	// @Override
	// public List<Cell> visit(SizeofTypeName n) {
	// return null;
	// }
	//
	// /**
	// * f0 ::= PrimaryExpression()
	// * f1 ::= PostfixOperationsList()
	// */
	// @Override
	// public List<Cell> visit(PostfixExpression n) {
	//
	// List<Node> cfgNodeList = new ArrayList<>();
	// cfgNodeList.add(Misc.getCFGNodeFor(n));
	//
	// List<Cell> symList = n.getF0().accept(this);
	// NodeListOptional nodeList = n.getF1().getF0();
	// if (nodeList.getNodes().isEmpty()) {
	// return symList;
	// } else {
	// int size = nodeList.getNodes().size();
	// int i = 0;
	// while (i < size) {
	// Node opNode = ((APostfixOperation)
	// nodeList.getNodes().get(i)).getF0().getChoice();
	//
	// if (opNode instanceof BracketExpression) {
	// ((BracketExpression) opNode).accept(this);
	// } else if (opNode instanceof ArgumentList) {
	// ((ArgumentList) opNode).accept(this);
	// symList = new ArrayList<>();
	// } else if (opNode instanceof DotId) {
	// // ||p|.id| = |p|
	// } else if (opNode instanceof ArrowId) {
	// addReads(symList, n);
	// symList = new ArrayList<>(getPointsToSet(symList, cfgNodeList));
	// } else if (opNode instanceof PlusPlus) {
	// addReads(symList, n);
	// addWrites(symList, n);
	// symList = new ArrayList<>();
	// } else if (opNode instanceof MinusMinus) {
	// addReads(symList, n);
	// addWrites(symList, n);
	// symList = new ArrayList<>();
	// } else {
	// System.out.println("Some error in LvalueAccessGetter::PostfixExpression");
	// System.exit(0);
	// }
	// i++;
	// }
	// return symList;
	// }
	// }
	//
	// /**
	// * f0 ::= "["
	// * f1 ::= Expression()
	// * f2 ::= "]"
	// */
	// @Override
	// public List<Cell> visit(BracketExpression n) {
	// addReads(n.getF1().accept(this), n);
	// return null;
	// }
	//
	// /**
	// * f0 ::= "("
	// * f1 ::= ( ExpressionList() )?
	// * f2 ::= ")"
	// */
	// @Override
	// public List<Cell> visit(ArgumentList n) {
	// n.getF1().accept(this);
	// return null;
	// }
	//
	// /**
	// * f0 ::= <IDENTIFIER>
	// * | Constant()
	// * | ExpressionClosed()
	// */
	// @Override
	// public List<Cell> visit(PrimaryExpression n) {
	// if (n.getF0().getChoice() instanceof NodeToken) {
	// List<Cell> _ret = new ArrayList<>();
	// Cell sym = Misc.getSymbolEntry(((NodeToken)
	// n.getF0().getChoice()).getTokenImage(), n);
	// if (sym != null) {
	// _ret.add(sym);
	// }
	// return _ret;
	// } else if (n.getF0().getChoice() instanceof ExpressionClosed) {
	// return n.getF0().getChoice().accept(this);
	// } else if (n.getF0().getChoice() instanceof Constant) {
	// return null;
	// } else {
	// return null;
	// }
	// }
	//
	// /**
	// * f0 ::= AssignmentExpression()
	// * f1 ::= ( "," AssignmentExpression() )*
	// */
	// @Override
	// public List<Cell> visit(ExpressionList n) {
	// // Put the closure of expressions in read and write lists.
	// // Pure arguments, and non-array non-pointer arguments should
	// // not be taken a closure of, but assuming that points-to
	// // information will take care of that, we take closure of
	// // all the arguments.
	// Node enclosingCFGNode = Misc.getCFGNodeFor(n);
	// List<Cell> symList = n.getF0().accept(this);
	// addReads(symList, n);
	// List<Node> nodeList = new ArrayList<>();
	// nodeList.add(enclosingCFGNode);
	// Set<Cell> symSet = getPointsToClosure(symList, nodeList);
	// addReads(new ArrayList<>(symSet), n);
	// addWrites(new ArrayList<>(symSet), n);
	//
	// for (Node seq : n.getF1().getNodes()) {
	// symList = ((NodeSequence) seq).getNodes().get(1).accept(this);
	// addReads(symList, n);
	// symSet = getPointsToClosure(symList, nodeList);
	// addReads(new ArrayList<>(symSet), n);
	// addWrites(new ArrayList<>(symSet), n);
	// }
	// return null;
	// }
	//
	// /**
	// * f0 ::= "("
	// * f1 ::= Expression()
	// * f2 ::= ")"
	// */
	// @Override
	// public List<Cell> visit(ExpressionClosed n) {
	// return n.getF1().accept(this);
	// }
	//
	// /**
	// * f0 ::= <INTEGER_LITERAL>
	// * | <FLOATING_POINT_LITERAL>
	// * | <CHARACTER_LITERAL>
	// * | ( <STRING_LITERAL> )+
	// */
	// @Override
	// public List<Cell> visit(Constant n) {
	// return null;
	// }
}
