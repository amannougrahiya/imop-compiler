/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.getter;

import imop.ast.info.DataSharingAttribute;
import imop.ast.info.cfgNodeInfo.DeclarationInfo;
import imop.ast.info.cfgNodeInfo.ParameterDeclarationInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstProcess;
import imop.baseVisitor.GJNoArguDepthFirstProcess;
import imop.lib.analysis.flowanalysis.*;
import imop.lib.analysis.typeSystem.ArithmeticType;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.analysis.typeSystem.PointerType;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.util.CellList;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.parser.CParserConstants;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to obtain the set of l-values (symbols/locations/cells)
 * that may have been read/written within a node.
 * <br>
 * Note that if the methods {@link #getReads(Node)} and {@link #getWrites(Node)}
 * are called on expressions that themselves represent l-values, then we look
 * into the AST placement of these expressions to categorize them as
 * read/write/both.
 * If no proper placement is present, we add the l-values to reads/writes for
 * {@link #getReads(Node)}/{@link #getWrites(Node)}, respectively.
 * 
 * @author Aman Nougrahiya
 *
 */
public class CellAccessGetter {
	/**
	 * Returns a set of all the cells that may be read in the execution of this
	 * node (and the called methods, if any), excluding the nodes that are
	 * encountered after the control leaves this node from any exit-point.
	 * 
	 * @return
	 *         set of all those cells that may be read between the entry to and
	 *         exit from this node.
	 */
	public static CellList getReads(Node node) {
		CellList readList = new CellList();
		if (node instanceof Expression) {
			node = Misc.getCFGNodeFor(node);
			AccessGetter accessGetter = new AccessGetter(false, node);
			CellList unknownList = node.accept(accessGetter);
			if (unknownList != null) {
				readList.addAll(unknownList);
			}
			readList.addAll(accessGetter.cellReadList);
		} else {
			node = Misc.getCFGNodeFor(node);
			if (node == null) {
				Misc.warnDueToLackOfFeature("Cannot obtain reads for non-executable statements.", node);
				return null;
			}
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				AccessGetter accessGetter = new AccessGetter(false, leafContent);
				CellList unknownList = leafContent.accept(accessGetter);
				if (unknownList != null) {
					readList.addAll(unknownList);
				}
				readList.addAll(accessGetter.cellReadList);
			}
		}
		return readList;
	}

	public static CellList getSymbolReads(Node node) {
		CellList readList = new CellList();
		if (node instanceof Expression) {
			node = Misc.getCFGNodeFor(node);
			AccessGetter accessGetter = new SymbolAccessGetter(false, node);
			CellList unknownList = node.accept(accessGetter);
			if (unknownList != null) {
				readList.addAll(unknownList);
			}
			readList.addAll(accessGetter.cellReadList);
		} else {
			node = Misc.getCFGNodeFor(node);
			if (node == null) {
				Misc.warnDueToLackOfFeature("Cannot obtain reads for non-executable statements.", node);
				return null;
			}
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				AccessGetter accessGetter = new SymbolAccessGetter(false, leafContent);
				CellList unknownList = leafContent.accept(accessGetter);
				if (unknownList != null) {
					readList.addAll(unknownList);
				}
				readList.addAll(accessGetter.cellReadList);
			}
		}
		return readList;
	}

	/**
	 * Returns a set of all the cells that may be written in the execution of
	 * this node (and the called methods, if any), excluding the nodes that
	 * are encountered after the control leaves this node from any exit-point.
	 * 
	 * @return
	 *         set of all those cells that may be written between the entry to
	 *         and exit from this node.
	 */
	public static CellList getWrites(Node node) {
		CellList writeList = new CellList();
		AccessGetter accessGetter;
		if (node instanceof Expression) {
			node = Misc.getCFGNodeFor(node);
			accessGetter = new AccessGetter(false, node);
			node.accept(accessGetter);
			writeList.addAll(accessGetter.cellWriteList);
		} else {
			node = Misc.getCFGNodeFor(node);
			if (node == null) {
				Misc.warnDueToLackOfFeature("Cannot obtain writes for non-executable statements.", node);
				return null;
			}
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				accessGetter = new AccessGetter(false, leafContent);
				leafContent.accept(accessGetter);
				writeList.addAll(accessGetter.cellWriteList);
			}
		}
		return writeList;
	}

	public static boolean mayWrite(Node node) {
		MayWriteChecker accessGetter;
		if (node instanceof Expression) {
			accessGetter = new MayWriteChecker();
			node.accept(accessGetter);
			return accessGetter.mayWrite;
		} else {
			node = Misc.getCFGNodeFor(node);
			if (node == null) {
				Misc.warnDueToLackOfFeature("Cannot obtain writes for non-executable statements.", node);
				return false;
			}
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				accessGetter = new MayWriteChecker();
				leafContent.accept(accessGetter);
				if (accessGetter.mayWrite) {
					return true;
				}
			}
		}
		return false;
	}

	public static CellList getSymbolWrites(Node node) {
		CellList writeList = new CellList();
		AccessGetter accessGetter;
		if (node instanceof Expression) {
			node = Misc.getCFGNodeFor(node);
			accessGetter = new SymbolAccessGetter(false, node);
			node.accept(accessGetter);
			writeList.addAll(accessGetter.cellWriteList);
		} else {
			node = Misc.getCFGNodeFor(node);
			if (node == null) {
				Misc.warnDueToLackOfFeature("Cannot obtain writes for non-executable statements.", node);
				return null;
			}
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				accessGetter = new SymbolAccessGetter(false, leafContent);
				leafContent.accept(accessGetter);
				writeList.addAll(accessGetter.cellWriteList);
			}
		}
		return writeList;
	}

	/**
	 * Returns a set of all the shared cells that may be read in the execution
	 * of this node (and the called methods, if any), excluding the nodes that
	 * are encountered after the control leaves this node from any exit-point.
	 * 
	 * @return
	 *         set of all those shared cells that may be read between the entry
	 *         to and exit from this node.
	 */
	public static CellSet getSharedReads(Node node) {
		CellSet sharedReadSet = new CellSet();
		AccessGetter accessGetter;
		if (node instanceof Expression) {
			node = Misc.getCFGNodeFor(node);
			accessGetter = new AccessGetter(true, node);
			CellList unknownList = node.accept(accessGetter);
			if (unknownList != null) {
				sharedReadSet.addAll(unknownList);
			}
			sharedReadSet.addAll(accessGetter.cellReadList);
		} else {
			node = Misc.getCFGNodeFor(node);
			if (node == null) {
				Misc.warnDueToLackOfFeature("Cannot obtain reads for non-executable statements.", node);
				return null;
			}
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				accessGetter = new AccessGetter(true, leafContent);
				CellList unknownList = leafContent.accept(accessGetter);
				if (unknownList != null) {
					sharedReadSet.addAll(unknownList);
				}
				sharedReadSet.addAll(accessGetter.cellReadList);
			}
		}
		return sharedReadSet;
	}

	/**
	 * Returns a set of all the shared cells that may be written in the
	 * execution of this node (and the called methods, if any), excluding the
	 * nodes that are encountered after the control leaves this node from any
	 * exit-point.
	 * 
	 * @return
	 *         set of all those shared cells that may be written between the
	 *         entry to and exit from this node.
	 */
	public static CellSet getSharedWrites(Node node) {
		CellSet sharedWriteSet = new CellSet();
		AccessGetter accessGetter;
		if (node instanceof Expression) {
			node = Misc.getCFGNodeFor(node);
			accessGetter = new AccessGetter(true, node);
			node.accept(accessGetter);
			sharedWriteSet.addAll(accessGetter.cellWriteList);
		} else {
			node = Misc.getCFGNodeFor(node);
			if (node == null) {
				Misc.warnDueToLackOfFeature("Cannot obtain writes for non-executable statements.", node);
				return null;
			}
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				accessGetter = new AccessGetter(true, leafContent);
				leafContent.accept(accessGetter);
				sharedWriteSet.addAll(accessGetter.cellWriteList);
			}
		}
		return sharedWriteSet;
	}

	/**
	 * This method is useful for obtaining memory locations referred by a
	 * sub-expression. Note that this method will return an empty list when
	 * {@code exp} is an expression that does not refer to one or more cells.
	 * 
	 * @param exp
	 *            expression whose represented cells have to be determined.
	 * @return
	 *         locations that {@exp} may represent.
	 */
	public static CellList getLocationsOf(Expression exp) {
		if (exp.toString().equals("((void *) 0)") || exp.toString().equals("0")) {
			CellList list = new CellList();
			list.add(Cell.getNullCell());
			return list;
		}
		Node leaf = Misc.getCFGNodeFor(exp);
		AccessGetter unknownAccessGetter = new AccessGetter(false, leaf);
		CellList unknownAccesses = exp.accept(unknownAccessGetter);
		if (unknownAccesses == null) {
			unknownAccesses = new CellList();
		}
		return unknownAccesses;
	}

	/**
	 * Checks whether the read/write lists of this node may rely on stable
	 * points-to information or not. If not, then we shouldn't be too eager to
	 * dump the memoization of this node just because points-to analysis might
	 * be stale.
	 * 
	 * @param node
	 * @return
	 */
	public static boolean mayRelyOnPointsTo(Node node) {
		PointsToRelianceGetter relianceGetter = new PointsToRelianceGetter();
		if (node instanceof Expression) {
			node.accept(relianceGetter);
			return relianceGetter.reliesOnPointsTo;
		} else {
			node = Misc.getCFGNodeFor(node);
			if (node == null) {
				Misc.warnDueToLackOfFeature("Cannot process non-executable statements.", node);
				return false;
			}
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				relianceGetter = new PointsToRelianceGetter();
				leafContent.accept(relianceGetter);
				if (relianceGetter.reliesOnPointsTo) {
					return true;
				}
			}
			return false;
		}
	}

	public static boolean mayRelyOnPointsToForSymbols(Node node) {
		PointsToForSymbolsRelianceGetter relianceGetter;
		if (node instanceof Expression) {
			node = Misc.getCFGNodeFor(node);
			relianceGetter = new PointsToForSymbolsRelianceGetter(node);
			node.accept(relianceGetter);
			return relianceGetter.reliesOnPointsTo;
		} else {
			node = Misc.getCFGNodeFor(node);
			if (node == null) {
				Misc.warnDueToLackOfFeature("Cannot process non-executable statements.", node);
				return false;
			}
			for (Node leafContent : node.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
				relianceGetter = new PointsToForSymbolsRelianceGetter(leafContent);
				leafContent.accept(relianceGetter);
				if (relianceGetter.reliesOnPointsTo) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Checks whether {@code node} (or its corresponding CFG node) may write to
	 * a cell of pointer-type.<br>
	 * Note that this method inspects all lexically enclosed components of this
	 * node, and all reachable function definitions.
	 * 
	 * @return
	 *         true, if this node (or its corresponding CFG node) may write to a
	 *         cell of pointer-type.
	 */
	public static boolean mayUpdatePointsTo(Node node) {
		MayUpdatePointsToGetter writeGetter = new MayUpdatePointsToGetter();
		if (node instanceof Expression) {
			node.accept(writeGetter);
			return writeGetter.mayUpdatePointsTo;
		} else {
			node = Misc.getCFGNodeFor(node);
			if (node == null) {
				Misc.warnDueToLackOfFeature("Cannot process non-executable statements.", node);
				return false;
			}
			for (Node leafContent : node.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
				writeGetter = new MayUpdatePointsToGetter();
				leafContent.accept(writeGetter);
				if (writeGetter.mayUpdatePointsTo) {
					return true;
				}
			}
			if (node.getInfo().hasLexicallyEnclosedCallStatements()) {
				for (FunctionDefinition reachableFunction : node.getInfo().getReachableCallGraphNodes()) {
					for (Node leafContent : reachableFunction.getInfo().getCFGInfo().getLexicalCFGLeafContents()) {
						writeGetter = new MayUpdatePointsToGetter();
						leafContent.accept(writeGetter);
						if (writeGetter.mayUpdatePointsTo) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}

	private static class PointsToRelianceGetter extends DepthFirstProcess {
		public boolean reliesOnPointsTo = false;

		/**
		 * f0 ::= UnaryOperator()
		 * f1 ::= CastExpression()
		 */
		@Override
		public void visit(UnaryCastExpression n) {
			String operator = ((NodeToken) n.getF0().getF0().getChoice()).getTokenImage();
			if (operator.equals("*")) {
				reliesOnPointsTo = true;
				return;
			}
		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public void visit(UnarySizeofExpression n) {
			// Expression is not evaluated when present as the argument
			// to sizeof operator.
			return;
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(SizeofUnaryExpression n) {
			// Expression is not evaluated when present as the argument to sizeof expression
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
			NodeListOptional nodeList = n.getF1().getF0();
			if (nodeList.getNodes().isEmpty()) {
				return;
			} else {
				int size = nodeList.getNodes().size();
				int i = -1;
				Type type = Type.getType(n.getF0());
				if (type == null) {
					reliesOnPointsTo = true;
					return;
				}
				if (type instanceof PointerType) {
					PointerType ptrType = (PointerType) type;
					if (ptrType.getOldType() instanceof ArrayType) {
						type = ptrType.getOldType();
					}
				}

				while (i < size - 1) {
					i++;
					Node opNode = ((APostfixOperation) nodeList.getNodes().get(i)).getF0().getChoice();

					if (opNode instanceof BracketExpression) {
						if (type instanceof ArrayType) {
							if (((ArrayType) type).containsPointers()) {
								reliesOnPointsTo = true;
								return;
							}
						} else if (type instanceof PointerType) {
							reliesOnPointsTo = true;
							return;
						}
					} else if (opNode instanceof ArgumentList) {
						assert (false);
						return;
					} else if (opNode instanceof DotId) {
					} else if (opNode instanceof ArrowId) {
						reliesOnPointsTo = true;
						return;
					} else if (opNode instanceof PlusPlus) {
					} else if (opNode instanceof MinusMinus) {
					} else {
						assert (false);
					}
				}
				return;
			}
		}

		@Override
		public void visit(PreCallNode n) {
			CallStatement callStmt = n.getParent();
			/*
			 * If the corresponding CallStatement does not have any known
			 * targets, then conservatively we should assume that this
			 * PreCallNode can read/write to all the locations that are
			 * accessible via the argument.
			 */
			if (callStmt.getInfo().getCalledDefinitions().isEmpty()) {
				for (SimplePrimaryExpression argument : n.getArgumentList()) {
					if (argument.isAConstant()) {
						continue;
					}
					Cell argCell = Misc.getSymbolOrFreeEntry(argument.getIdentifier());
					if (argCell == null || argCell instanceof FreeVariable) {
						continue;
					}
					if (argCell instanceof Symbol) {
						Symbol sym = (Symbol) argCell;
						Type symType = sym.getType();
						if (symType instanceof ArithmeticType) {
							continue;
						} else {
							this.reliesOnPointsTo = true;
							return;
						}
					}
				}
			}
		}
	}

	private static class PointsToForSymbolsRelianceGetter extends DepthFirstProcess {
		public boolean reliesOnPointsTo = false;
		public final Node cfgLeaf;

		public PointsToForSymbolsRelianceGetter(Node cfgLeaf) {
			this.cfgLeaf = cfgLeaf;
		}

		/**
		 * f0 ::= UnaryOperator()
		 * f1 ::= CastExpression()
		 */
		@Override
		public void visit(UnaryCastExpression n) {
			String operator = ((NodeToken) n.getF0().getF0().getChoice()).getTokenImage();
			if (operator.equals("*")) {
				CellList operands = n.getF1().getInfo().getLocationsOf();
				for (Cell operand : operands) {
					if (Program.getCellsThatMayPointToSymbols().contains(operand)) {
						reliesOnPointsTo = true;
					}
				}
				return;
			}
		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public void visit(UnarySizeofExpression n) {
			// Expression is not evaluated when present as the argument
			// to sizeof operator.
			return;
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(SizeofUnaryExpression n) {
			// Expression is not evaluated when present as the argument to sizeof expression
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
			Node cfgNode = this.cfgLeaf;
			NodeListOptional nodeList = n.getF1().getF0();
			if (nodeList.getNodes().isEmpty()) {
				return;
			} else {
				int size = nodeList.getNodes().size();
				int i = -1;
				Type type = Type.getType(n.getF0());
				if (type == null) {
					reliesOnPointsTo = true;
					return;
				}
				if (type instanceof PointerType) {
					PointerType ptrType = (PointerType) type;
					if (ptrType.getOldType() instanceof ArrayType) {
						type = ptrType.getOldType();
					}
				}

				CellList primList = n.getF0().getInfo().getLocationsOf();
				while (i < size - 1) {
					i++;
					Node opNode = ((APostfixOperation) nodeList.getNodes().get(i)).getF0().getChoice();

					if (opNode instanceof BracketExpression) {
						if (type instanceof ArrayType) {
							if (((ArrayType) type).containsPointers()) {
								for (Cell prim : primList) {
									if (Program.getCellsThatMayPointToSymbols().contains(prim)) {
										reliesOnPointsTo = true;
										return;
									}
								}
							}
						}
						CellList newPrimList = new CellList(primList);
						for (Cell prim : primList) {
							newPrimList.addAll(prim.getPointsTo(cfgNode));
						}
						primList = newPrimList;
					} else if (opNode instanceof ArgumentList) {
						assert (false);
						return;
					} else if (opNode instanceof DotId) {
					} else if (opNode instanceof ArrowId) {
						reliesOnPointsTo = true;
						return;
					} else if (opNode instanceof PlusPlus) {
					} else if (opNode instanceof MinusMinus) {
					} else {
						assert (false);
					}
				}
				return;
			}
		}

		@Override
		public void visit(PreCallNode n) {
			CallStatement callStmt = n.getParent();
			/*
			 * If the corresponding CallStatement does not have any known
			 * targets, then conservatively we should assume that this
			 * PreCallNode can read/write to all the locations that are
			 * accessible via the argument.
			 */
			if (callStmt.getInfo().getCalledDefinitions().isEmpty()) {
				for (SimplePrimaryExpression argument : n.getArgumentList()) {
					if (argument.isAConstant()) {
						continue;
					}
					Cell argCell = Misc.getSymbolOrFreeEntry(argument.getIdentifier());
					if (argCell == null || argCell instanceof FreeVariable) {
						continue;
					}
					if (argCell instanceof Symbol) {
						Symbol sym = (Symbol) argCell;
						Type symType = sym.getType();
						if (symType instanceof ArithmeticType) {
							continue;
						} else {
							this.reliesOnPointsTo = true;
							return;
						}
					}
				}
			}
		}
	}

	public static class SymbolAccessGetter extends AccessGetter {

		public SymbolAccessGetter(boolean isForShared, Node leaf) {
			super(isForShared, leaf);
		}

		@Override
		public CellList visit(PostfixExpression n) {
			Node cfgNode = this.cfgLeaf;

			CellList symList = n.getF0().accept(this);
			NodeListOptional nodeList = n.getF1().getF0();
			if (nodeList.getNodes().isEmpty()) {
				return symList;
			} else {
				int size = nodeList.getNodes().size();
				int i = 0;
				while (i < size) {
					Node opNode = ((APostfixOperation) nodeList.getNodes().get(i)).getF0().getChoice();

					if (opNode instanceof BracketExpression) {
						BracketExpression brackExp = (BracketExpression) opNode;
						CellList e1List = symList;
						CellList e2List = brackExp.getF1().accept(this);

						addReads(n, e1List);
						addReads(n, e2List);

						/*
						 * Find the list with integer type.
						 */
						CellList tempList = new CellList();
						if (e1List != null) {
							if (e1List.isUniversal()) {
								tempList.add(Cell.genericCell);
							} else {
								e1List.applyAllExpanded(cell -> {
									if (Program.getCellsThatMayPointToSymbols().contains(cell)) {
										// if (cell instanceof Symbol) {
										// Symbol sym = (Symbol) cell;
										// if (sym.getType() instanceof PointerType) {
										// System.err.println("Following pointer at " + cfgNode + ": " + sym);
										// }
										// }
										tempList.addAll(cell.getPointsTo(cfgNode));
									}
								});
							}
						}
						if (e2List != null) {
							if (e2List.isUniversal()) {
								tempList.add(Cell.genericCell);
							} else {
								e2List.applyAllExpanded(cell -> {
									if (Program.getCellsThatMayPointToSymbols().contains(cell)) {
										// if (cell instanceof Symbol) {
										// Symbol sym = (Symbol) cell;
										// if (sym.getType() instanceof PointerType) {
										// System.err.println("Following pointer at " + cfgNode + ": " + sym);
										// }
										// }
										tempList.addAll(cell.getPointsTo(cfgNode));
									}
								});
							}
						}

						symList = tempList;
					} else if (opNode instanceof ArgumentList) {
						// assert (false); // Cannot have this operator here after expression
						// simplification.
						return null;
					} else if (opNode instanceof DotId) {
						// ||p|.id| = |p|
						addReads(n, symList);
						// Now, the same set of symbols represents symbol.id,
						// hence we do not update symList.
					} else if (opNode instanceof ArrowId) {
						addReads(n, symList);

						CellList tempList = new CellList();
						if (symList.isUniversal()) {
							tempList.add(Cell.genericCell);
						} else {
							symList.applyAllExpanded(cell -> {
								tempList.addAll(cell.getPointsTo(cfgNode));
							});
						}
						symList = tempList;
						addReads(n, symList);
						// Now, the same set of symbols as (*sym) represents (*sym).id,
						// hence we do not update symList.
					} else if (opNode instanceof PlusPlus) {
						addReads(n, symList);
						addWrites(n, symList);
						symList = new CellList();
					} else if (opNode instanceof MinusMinus) {
						addReads(n, symList);
						addWrites(n, symList);
						symList = new CellList();
					} else {
						assert (false);
					}
					i++;
				}
				return symList;
			}
		}

		@Override
		public CellList visit(UnaryCastExpression n) {
			// Obtain the list of symbols referred by castExpression as a whole
			CellList symList = n.getF1().accept(this);
			if (symList == null) {
				return new CellList();
			}
			Node node = this.cfgLeaf;

			CellList retList = new CellList();
			String operator = ((NodeToken) n.getF0().getF0().getChoice()).getTokenImage();
			switch (operator) {
			case "&":
				if (symList.isUniversal()) {
					addRead(n, Cell.genericCell);
					retList.add(Cell.genericCell);
				} else {
					symList.applyAllExpanded(sym -> {
						if (sym instanceof Symbol) {
							Symbol s = (Symbol) sym;
							addRead(n, s.getAddressCell());
							retList.add(s.getAddressCell());
						} else if (sym instanceof FieldCell) {
							retList.add(((FieldCell) sym).getAggregateElement());
						} else if (sym instanceof HeapCell) {
							retList.add(((HeapCell) sym).getAddressCell());
						}
					});
				}
				return retList;
			case "*":
				addReads(n, symList);
				if (symList.isUniversal()) {
					retList.add(Cell.genericCell);
				} else {
					symList.applyAllExpanded(sym -> {
						if (Program.getCellsThatMayPointToSymbols().contains(sym)) {
							retList.addAll(sym.getPointsTo(node));
						}
					});
				}
				return retList;
			case "+":
			case "-":
			case "~":
			case "!":
				addReads(n, symList);
				return null;
			default:
				return null;
			}
		}

		@Override
		public CellList visit(PreCallNode n) {
			CallStatement callStmt = n.getParent();
			addReads(n, callStmt.getInfo().getCalledSymbols());
			for (SimplePrimaryExpression argument : n.getArgumentList()) {
				addReads(n, argument.accept(this));
			}
			/*
			 * Update: 27-Apr-18
			 * If the corresponding CallStatement does not have any known
			 * targets,
			 * then conservatively we should assume that this PreCallNode can
			 * read/write
			 * to all the locations that are accessible via the arguments.
			 */
			if (callStmt.getInfo().getCalledDefinitions().isEmpty()) {
				CellList escapingHeads = new CellList();
				for (SimplePrimaryExpression argument : n.getArgumentList()) {
					if (argument.isAConstant()) {
						continue;
					}
					escapingHeads.add(Misc.getSymbolOrFreeEntry(argument.getIdentifier()));
				}

				/*
				 * Add reads and writes of only global variables now.
				 */
				if (Program.getRoot() != null) {
					for (Symbol globalSym : Program.getRoot().getInfo().getSymbolTable().values()) {
						addWrite(n, globalSym);
						addRead(n, globalSym);
					}
				}
			}

			return null;
		}

	}

	/**
	 * This visitor is used to obtain the list of symbols or free variables that
	 * are read/written in the visited expression. Each visit may add to the
	 * list of nodes that are read/written in the expression being visited, and
	 * return the list of symbols/free-variables that the visited expression
	 * itself represents.
	 * 
	 * @author Aman Nougrahiya
	 *
	 */
	private static class AccessGetter extends GJNoArguDepthFirstProcess<CellList> {
		public CellList cellReadList = new CellList();
		public CellList cellWriteList = new CellList();
		private final boolean isForShared;
		protected final Node cfgLeaf;

		public AccessGetter(boolean isForShared, Node leaf) {
			this.isForShared = isForShared;
			this.cfgLeaf = leaf;
		}

		/**
		 * Adds non-null symbols and free variables from {@code cellList} to
		 * {@code cellReadList}.
		 *
		 * @param n
		 *                 node at which the access has been made.
		 * @param cellList
		 *                 list of symbols and free variables that need to be added
		 *                 to the list of cells that may have been read in the
		 *                 visits.
		 * 
		 */
		public void addReads(Node n, CellList cellList) {
			n = this.cfgLeaf;
			if (cellList != null) {
				if (cellList.isUniversal()) {
					cellReadList.add(Cell.genericCell);
					// CellSet allCellsHere = n.getInfo().getAllCellsAtNode();
					// if (allCellsHere.isUniversal()) {
					// for (Cell cell : Cell.allCells) {
					// if (cell != null) {
					// if (isForShared == false
					// || n.getInfo().getSharingAttribute(cell) == DataSharingAttribute.SHARED) {
					// cellReadList.add(cell);
					// }
					// }
					// }
					// } else {
					// for (Cell cell : n.getInfo().getAllCellsAtNode()) {
					// if (cell != null) {
					// if (isForShared == false
					// || n.getInfo().getSharingAttribute(cell) == DataSharingAttribute.SHARED) {
					// cellReadList.add(cell);
					// }
					// }
					// }
					// }
				} else {
					for (Cell cell : cellList) {
						if (cell != null) {
							if (isForShared == false
									|| n.getInfo().getSharingAttribute(cell) == DataSharingAttribute.SHARED) {
								// if (cell instanceof Symbol) {
								// Symbol sym = (Symbol) cell;
								// if (sym.getType() instanceof ArrayType) {
								// cellReadList.add(sym.getAddressCell());
								// } else {
								// cellReadList.add(cell);
								// }
								// } else {
								cellReadList.add(cell);
								// }
							}
						}
					}
				}
			}
		}

		/**
		 * Adds non-null symbols and free variables from {@code cellList} to
		 * {@code cellWriteList}.
		 * 
		 * @param n
		 *                 node at which the access has been made.
		 * @param cellList
		 *                 list of symbols and free variables that need to be added
		 *                 to the list of cells that may have been written in the
		 *                 visits.
		 * 
		 */
		public void addWrites(Node n, CellList cellList) {
			n = this.cfgLeaf;
			if (cellList != null) {
				if (cellList.isUniversal()) {
					boolean morePrecise = false;
					if (morePrecise) {
						// CellSet allCellsHere = n.getInfo().getAllCellsAtNode();
						// if (allCellsHere.isUniversal()) {
						// for (Cell cell : Cell.allCells) {
						// if (cell != null) {
						// if (isForShared == false
						// || n.getInfo().getSharingAttribute(cell) == DataSharingAttribute.SHARED) {
						// cellWriteList.add(cell);
						// }
						// }
						// }
						// } else {
						// for (Cell cell : n.getInfo().getAllCellsAtNode()) {
						// if (cell != null) {
						// if (isForShared == false
						// || n.getInfo().getSharingAttribute(cell) == DataSharingAttribute.SHARED) {
						// cellWriteList.add(cell);
						// }
						// }
						// }
						// }
					} else {
						cellWriteList.add(Cell.genericCell);
					}
				} else {
					for (Cell cell : cellList) {
						if (cell != null) {
							if (isForShared == false
									|| n.getInfo().getSharingAttribute(cell) == DataSharingAttribute.SHARED) {
								cellWriteList.add(cell);
							}
						}
					}
				}
			}
		}

		public void addWrite(Node n, Cell c) {
			CellList cellList = new CellList();
			cellList.add(c);
			addWrites(n, cellList);
		}

		public void addRead(Node n, Cell c) {
			CellList cellList = new CellList();
			cellList.add(c);
			addReads(n, cellList);
		}

		/**
		 * @param pointerSet:
		 *                    list of pointer cells.
		 * @param nodeList
		 *                    list of nodes.
		 * @return a set of symbols pointed to by elements of {@code pointerSet}
		 */
		@Deprecated
		public CellSet getOptimizedPointsToSet(CellList pointerSet, List<Node> nodeList) {
			CellSet pointsToSet = new CellSet();
			if (pointerSet == null) {
				return pointsToSet;
			}
			if (pointerSet.isUniversal()) {
				pointsToSet.add(Cell.genericCell);
			} else {
				pointerSet.applyAllExpanded(pointer -> {
					int index = pointerSet.indexOf(pointer);
					if (!(index < 0)) {
						Node tempNode = nodeList.get(0);
						if (nodeList.size() > index) {
							tempNode = nodeList.get(index);
						}

						if (pointer != null) {
							pointsToSet.addAll(pointer.getPointsTo(tempNode));
						}
					}
				});
			}
			return pointsToSet;
		}

		/**
		 * @param pointerSet:
		 *                    List of pointer symbols
		 * @param nodeList
		 *                    list of nodes.
		 * @return a set of symbols pointed to by elements of (@code
		 *         pointerSet}, and
		 *         those pointed to by these symbols and so on...
		 */
		public CellSet getOptimizedPointsToClosure(CellList pointerList, Node node) {
			CellSet pointsToClosure = new CellSet();
			int oldSize = 0, newSize;
			if (pointerList == null) {
				return pointsToClosure;
			}
			while (true) {
				if (pointerList.isUniversal()) {
					pointsToClosure.add(Cell.genericCell);
					return pointsToClosure;
				} else {
					for (Cell sym : pointerList) {
						if (sym != null) {
							pointsToClosure.addAll(sym.getPointsTo(node));
						}
					}
				}
				newSize = pointsToClosure.size();
				if (newSize > oldSize) {
					oldSize = newSize;
					pointerList = new CellList(pointsToClosure);
					continue;
				} else {
					break;
				}
			}
			return pointsToClosure;
		}

		/**
		 * @param pointerSet:
		 *                    List of pointer symbols
		 * @param nodeList
		 *                    list of nodes.
		 * @return a set of symbols pointed to by elements of (@code
		 *         pointerSet}, and
		 *         those pointed to by these symbols and so on...
		 */
		@Deprecated
		public CellSet getOptimizedPointsToClosureObsolete(CellList pointerList, List<Node> nodeList) {
			CellSet pointsToClosure = new CellSet();
			int oldSize = 0, newSize;
			if (pointerList == null) {
				return pointsToClosure;
			}
			while (true) {
				if (pointerList.isUniversal()) {
					pointsToClosure.add(Cell.genericCell);
					return pointsToClosure;
				} else {
					for (Cell sym : pointerList) {
						int index = pointerList.indexOf(sym);
						if (index >= 0) {
							Node tempNode = nodeList.get(0);
							if (nodeList.size() > index) {
								tempNode = nodeList.get(index);
							}

							if (sym != null) {
								pointsToClosure.addAll(sym.getPointsTo(tempNode));
							}
						}
					}
				}
				newSize = pointsToClosure.size();
				if (newSize > oldSize) {
					oldSize = newSize;
					pointerList = new CellList(pointsToClosure);
					continue;
				} else {
					break;
				}
			}
			return pointsToClosure;
		}

		@Override
		public CellList visit(NodeToken n) {
			if (n.getKind() == CParserConstants.IDENTIFIER) {
				CellList locations = new CellList();
				locations.add(Misc.getSymbolOrFreeEntry(n));
				return locations;
			}
			return null;
		}

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ( InitDeclaratorList() )?
		 * f2 ::= ";"
		 */
		@Override
		public CellList visit(Declaration n) {
			CellList _ret = null;
			if (n.getInfo().isTypedef()) {
				return _ret;
			}
			n.getF1().accept(this);
			return _ret;
		}

		/**
		 * f0 ::= Declarator()
		 * f1 ::= ( "=" Initializer() )?
		 */
		@Override
		public CellList visit(InitDeclarator n) {
			Cell sym = Misc.getSymbolOrFreeEntry(DeclarationInfo.getRootIdNodeToken(n.getF0()));
			if (sym instanceof Symbol && ((Symbol) sym).getType() instanceof ArrayType) {
				addWrite(n, ((Symbol) sym).getFieldCell());
			}
			addWrite(n, sym);
			if (n.getF1().getNode() != null) {
				addReads(n, ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
			}
			// This return null is correct -- the expression does not represent LHS
			// location.
			return null;
		}

		/**
		 * f0 ::= ( Pointer() )?
		 * f1 ::= DirectDeclarator()
		 */
		@Override
		public CellList visit(Declarator n) {
			CellList locations = new CellList();
			Cell sym = Misc.getSymbolOrFreeEntry(DeclarationInfo.getRootIdNodeToken(n));
			locations.add(sym);
			return locations;
		}

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ParameterAbstraction()
		 */
		@Override
		public CellList visit(ParameterDeclaration n) {
			NodeToken parameterToken = ParameterDeclarationInfo.getRootParamNodeToken(n);
			if (parameterToken != null) {
				Cell sym = Misc.getSymbolOrFreeEntry(ParameterDeclarationInfo.getRootParamNodeToken(n));
				addWrite(n, sym);
			}
			return null;
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * | ArrayInitializer()
		 */
		@Override
		public CellList visit(Initializer n) {
			if (n.getF0().getChoice() instanceof AssignmentExpression) {
				AssignmentExpression assignExp = (AssignmentExpression) n.getF0().getChoice();
				CellList retList = new CellList();
				if (assignExp.getF0().getChoice() instanceof ConditionalExpression) {
					ConditionalExpression condExp = (ConditionalExpression) assignExp.getF0().getChoice();
					CellList condRetSet = condExp.accept(this);
					if (condRetSet != null) {
						retList.addAll(condRetSet);
					}
				} else {
					// in case of a NonConditionalExpression.
					NonConditionalExpression condExp = (NonConditionalExpression) assignExp.getF0().getChoice();
					CellList condRetSet = condExp.accept(this);
					if (condRetSet != null) {
						retList.addAll(condRetSet);
					}
				}
				return retList;
			} else {
				return n.getF0().getChoice().accept(this);
			}
		}

		/**
		 * f0 ::= <IF>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public CellList visit(IfClause n) {
			addReads(n, n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <NUM_THREADS>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public CellList visit(NumThreadsClause n) {
			addReads(n, n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= Expression()
		 */
		@Override
		public CellList visit(OmpForInitExpression n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			addWrite(n, sym);
			addReads(n, n.getF2().accept(this));
			CellList retList = new CellList();
			retList.add(sym);
			return retList;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<"
		 * f2 ::= Expression()
		 */
		@Override
		public CellList visit(OmpForLTCondition n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			addRead(n, sym);
			addReads(n, n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "<="
		 * f2 ::= Expression()
		 */
		@Override
		public CellList visit(OmpForLECondition n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			addRead(n, sym);
			addReads(n, n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">"
		 * f2 ::= Expression()
		 */
		@Override
		public CellList visit(OmpForGTCondition n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			addRead(n, sym);
			addReads(n, n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= ">="
		 * f2 ::= Expression()
		 */
		@Override
		public CellList visit(OmpForGECondition n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			addRead(n, sym);
			addReads(n, n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "++"
		 */
		@Override
		public CellList visit(PostIncrementId n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			addRead(n, sym);
			addWrite(n, sym);
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "--"
		 */
		@Override
		public CellList visit(PostDecrementId n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			addRead(n, sym);
			addWrite(n, sym);
			return null;
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public CellList visit(PreIncrementId n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF1());
			addRead(n, sym);
			addWrite(n, sym);
			return null;
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public CellList visit(PreDecrementId n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF1());
			addRead(n, sym);
			addWrite(n, sym);
			return null;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "+="
		 * f2 ::= Expression()
		 */
		@Override
		public CellList visit(ShortAssignPlus n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			addRead(n, sym);
			addWrite(n, sym);
			addReads(n, n.getF2().accept(this));
			CellList retList = new CellList();
			retList.add(sym);
			return retList;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "-="
		 * f2 ::= Expression()
		 */
		@Override
		public CellList visit(ShortAssignMinus n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			addRead(n, sym);
			addWrite(n, sym);
			addReads(n, n.getF2().accept(this));
			CellList retList = new CellList();
			retList.add(sym);
			return retList;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "+"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public CellList visit(OmpForAdditive n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			addWrite(n, sym);
			sym = Misc.getSymbolOrFreeEntry(n.getF2());
			addRead(n, sym);
			addReads(n, n.getF4().accept(this));
			CellList retList = new CellList();
			retList.add(sym);
			return retList;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "-"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public CellList visit(OmpForSubtractive n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			addWrite(n, sym);
			sym = Misc.getSymbolOrFreeEntry(n.getF2());
			addRead(n, sym);
			addReads(n, n.getF4().accept(this));
			CellList retList = new CellList();
			retList.add(sym);
			return retList;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= MultiplicativeExpression()
		 * f3 ::= "+"
		 * f4 ::= <IDENTIFIER>
		 */
		@Override
		public CellList visit(OmpForMultiplicative n) {
			Cell sym = Misc.getSymbolOrFreeEntry(n.getF0());
			addWrite(n, sym);
			sym = Misc.getSymbolOrFreeEntry(n.getF4());
			addRead(n, sym);
			addReads(n, n.getF2().accept(this));
			CellList retList = new CellList();
			retList.add(sym);
			return retList;
		}

		/**
		 * f0 ::= <FINAL>
		 * f1 ::= "("
		 * f2 ::= Expression()
		 * f3 ::= ")"
		 */
		@Override
		public CellList visit(FinalClause n) {
			addReads(n, n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= ( Expression() )?
		 * f1 ::= ";"
		 */
		@Override
		public CellList visit(ExpressionStatement n) {
			if (n.getF0().present()) {
				addReads(n, n.getF0().getNode().accept(this));
			}
			return null;
		}

		/**
		 * f0 ::= <RETURN>
		 * f1 ::= ( Expression() )?
		 * f2 ::= ";"
		 */
		@Override
		public CellList visit(ReturnStatement n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public CellList visit(Expression n) {
			if (n.getExpF1().getNodes().isEmpty()) {
				// if (Misc.isAPredicate(n)) {
				// addReads(n, n.getExpF0().accept(this));
				// return null;
				// } else {
				return n.getExpF0().accept(this);
				// }
			} else {
				assert (false); // ExpressionSimplification should have removed the comma operator.
				addReads(n, n.getExpF0().accept(this));
				for (Node seq : n.getExpF1().getNodes()) {
					assert seq instanceof NodeSequence;
					AssignmentExpression aE = (AssignmentExpression) ((NodeSequence) seq).getNodes().get(1);
					addReads(n, aE.accept(this));
				}
				return null;
			}
		}

		/**
		 * f0 ::= NonConditionalExpression()
		 * | ConditionalExpression()
		 */
		@Override
		public CellList visit(AssignmentExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public CellList visit(NonConditionalExpression n) {
			// UnaryExpression may either be an lvalue, or not,
			// but in this case, it must be an lvalue since it is on LHS of assignment
			CellList sym = n.getF0().accept(this);
			String operator = ((NodeToken) n.getF1().getF0().getChoice()).getTokenImage();
			if (operator.equals("=")) {
				// UnaryExpression is only written to
				addWrites(n, sym);
				// addWrites(n, symList);
			} else {
				// UnaryExpression is both read and written
				addReads(n, sym);
				addWrites(n, sym);
			}
			addReads(n, n.getF2().accept(this));
			return null;
		}

		/**
		 * f0 ::= LogicalORExpression()
		 * f1 ::= ( "?" Expression() ":" ConditionalExpression() )?
		 */
		@Override
		public CellList visit(ConditionalExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				NodeSequence seq = (NodeSequence) n.getF1().getNode();
				Expression exp = (Expression) seq.getNodes().get(1);
				ConditionalExpression condExp = (ConditionalExpression) seq.getNodes().get(3);

				CellList retList = new CellList();

				addReads(n, n.getF0().accept(this));
				retList.addAll(exp.accept(this));
				retList.addAll(condExp.accept(this));
				return retList;
			}
		}

		/**
		 * f0 ::= LogicalANDExpression()
		 * f1 ::= ( "||" LogicalORExpression() )?
		 */
		@Override
		public CellList visit(LogicalORExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n, n.getF0().accept(this));
				addReads(n, ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= InclusiveORExpression()
		 * f1 ::= ( "&&" LogicalANDExpression() )?
		 */
		@Override
		public CellList visit(LogicalANDExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n, n.getF0().accept(this));
				addReads(n, ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= ExclusiveORExpression()
		 * f1 ::= ( "|" InclusiveORExpression() )?
		 */
		@Override
		public CellList visit(InclusiveORExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n, n.getF0().accept(this));
				addReads(n, ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= ANDExpression()
		 * f1 ::= ( "^" ExclusiveORExpression() )?
		 */
		@Override
		public CellList visit(ExclusiveORExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n, n.getF0().accept(this));
				addReads(n, ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= EqualityExpression()
		 * f1 ::= ( "&" ANDExpression() )?
		 */
		@Override
		public CellList visit(ANDExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n, n.getF0().accept(this));
				addReads(n, ((NodeSequence) n.getF1().getNode()).getNodes().get(1).accept(this));
				return null;
			}
		}

		/**
		 * f0 ::= RelationalExpression()
		 * f1 ::= ( EqualOptionalExpression() )?
		 */
		@Override
		public CellList visit(EqualityExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n, n.getF0().accept(this));
				n.getF1().getNode().accept(this);
				return null;
			}
		}

		/**
		 * f0 ::= EqualExpression()
		 * | NonEqualExpression()
		 */
		@Override
		public CellList visit(EqualOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "=="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public CellList visit(EqualExpression n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "!="
		 * f1 ::= EqualityExpression()
		 */
		@Override
		public CellList visit(NonEqualExpression n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= ShiftExpression()
		 * f1 ::= ( RelationalOptionalExpression() )?
		 */
		@Override
		public CellList visit(RelationalExpression n) {
			if (n.getRelExpF1().getNode() == null) {
				return n.getRelExpF0().accept(this);
			} else {
				addReads(n, n.getRelExpF0().accept(this));
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
		public CellList visit(RelationalOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "<"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public CellList visit(RelationalLTExpression n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= ">"
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public CellList visit(RelationalGTExpression n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "<="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public CellList visit(RelationalLEExpression n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= ">="
		 * f1 ::= RelationalExpression()
		 */
		@Override
		public CellList visit(RelationalGEExpression n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= AdditiveExpression()
		 * f1 ::= ( ShiftOptionalExpression() )?
		 */
		@Override
		public CellList visit(ShiftExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n, n.getF0().accept(this));
				n.getF1().getNode().accept(this);
				return null;
			}
		}

		/**
		 * f0 ::= ShiftLeftExpression()
		 * | ShiftRightExpression()
		 */
		@Override
		public CellList visit(ShiftOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= ">>"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public CellList visit(ShiftLeftExpression n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "<<"
		 * f1 ::= ShiftExpression()
		 */
		@Override
		public CellList visit(ShiftRightExpression n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= MultiplicativeExpression()
		 * f1 ::= ( AdditiveOptionalExpression() )?
		 */
		@Override
		public CellList visit(AdditiveExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				CellList lhOp = n.getF0().accept(this);
				addReads(n, lhOp);
				Type lhType = Type.getType(n.getF0());
				CellList rhOp = n.getF1().getNode().accept(this);
				boolean isLeftBase = lhType instanceof PointerType || lhType instanceof ArrayType;
				if (rhOp != null) {
					if (isLeftBase) {
						return null; // Pointer +/- Pointer is assumed to be int here. Check.
					} else {
						return rhOp;
					}
				} else {
					if (isLeftBase) {
						return lhOp;
					} else {
						return null;
					}
				}
			}
		}

		/**
		 * f0 ::= AdditivePlusExpression()
		 * | AdditiveMinusExpression()
		 */
		@Override
		public CellList visit(AdditiveOptionalExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "+"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public CellList visit(AdditivePlusExpression n) {
			CellList rhOp = n.getF1().accept(this);
			addReads(n, rhOp);
			Type rhType = Type.getType(n.getF1());
			if (rhType instanceof PointerType) {
				return rhOp;
			} else if (rhType instanceof ArrayType) {
				return rhOp;
			} else {
				return null;
			}
		}

		/**
		 * f0 ::= "-"
		 * f1 ::= AdditiveExpression()
		 */
		@Override
		public CellList visit(AdditiveMinusExpression n) {
			CellList rhOp = n.getF1().accept(this);
			addReads(n, rhOp);
			Type rhType = Type.getType(n.getF1());
			if (rhType instanceof PointerType) {
				return rhOp;
			} else if (rhType instanceof ArrayType) {
				return rhOp;
			} else {
				return null;
			}
		}

		/**
		 * f0 ::= CastExpression()
		 * f1 ::= ( MultiplicativeOptionalExpression() )?
		 */
		@Override
		public CellList visit(MultiplicativeExpression n) {
			if (n.getF1().getNode() == null) {
				return n.getF0().accept(this);
			} else {
				addReads(n, n.getF0().accept(this));
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
		public CellList visit(MultiplicativeOptionalExpression n) {
			n.getF0().accept(this);
			return null;
		}

		/**
		 * f0 ::= "*"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public CellList visit(MultiplicativeMultiExpression n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "/"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public CellList visit(MultiplicativeDivExpression n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "%"
		 * f1 ::= MultiplicativeExpression()
		 */
		@Override
		public CellList visit(MultiplicativeModExpression n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= CastExpressionTypedCastExpressionTyped()
		 * | UnaryExpression()
		 */
		@Override
		public CellList visit(CastExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "("
		 * f1 ::= TypeName()
		 * f2 ::= ")"
		 * f3 ::= CastExpression()
		 */
		@Override
		public CellList visit(CastExpressionTyped n) {
			return n.getF3().accept(this);
		}

		/**
		 * f0 ::= UnaryExpressionPreIncrement()
		 * | UnaryExpressionPreDecrement()
		 * | UnarySizeofExpression()
		 * | UnaryCastExpression()
		 * | PostfixExpression()
		 */
		@Override
		public CellList visit(UnaryExpression n) {
			return n.getF0().accept(this);
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public CellList visit(UnaryExpressionPreIncrement n) {
			CellList symList = n.getF1().accept(this);
			addReads(n, symList);
			addWrites(n, symList);
			return null;
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public CellList visit(UnaryExpressionPreDecrement n) {
			CellList symList = n.getF1().accept(this);
			addReads(n, symList);
			addWrites(n, symList);
			return null;
		}

		/**
		 * f0 ::= UnaryOperator()
		 * f1 ::= CastExpression()
		 */
		@Override
		public CellList visit(UnaryCastExpression n) {
			// Obtain the list of symbols referred by castExpression as a whole
			CellList symList = n.getF1().accept(this);
			if (symList == null) {
				return new CellList();
			}
			Node node = this.cfgLeaf;

			CellList retList = new CellList();
			String operator = ((NodeToken) n.getF0().getF0().getChoice()).getTokenImage();
			switch (operator) {
			case "&":
				if (symList.isUniversal()) {
					addRead(n, Cell.genericCell);
					retList.add(Cell.genericCell);
					// Set<Cell> tempAllCells = Cell.allCells;
					// Cell.allCells = new HashSet<>();
					// for (Cell sym : tempAllCells) {
					// if (sym instanceof Symbol) {
					// cellReadList.add(((Symbol) sym).getAddressCell());
					// }
					// }
					// tempAllCells.addAll(Cell.allCells);
					// Cell.allCells = tempAllCells;
				} else {
					symList.applyAllExpanded(sym -> {
						if (sym instanceof Symbol) {
							Symbol s = (Symbol) sym;
							addRead(n, s.getAddressCell());
							retList.add(s.getAddressCell());
							// Old code:
							// if (s.getType() instanceof ArrayType) {
							// addRead(n, s);
							// // Old Code:
							// // } else if (s.getType() instanceof PointerType) {
							// // // Field-insensitivity and pointer generation.
							// // PointerType ptrType = (PointerType) s.getType();
							// // if (!ptrType.isFromPointer()) {
							// // addRead(n, s);
							// // assert(false);
							// // }
							// retList.add(s);
							// } else {
							// addRead(n, s.getAddressCell());
							// retList.add(s.getAddressCell());
							// }
						} else if (sym instanceof FieldCell) {
							retList.add(((FieldCell) sym).getAggregateElement());
						} else if (sym instanceof HeapCell) {
							retList.add(((HeapCell) sym).getAddressCell());
						}
					});
				}
				return retList;
			case "*":
				addReads(n, symList);
				if (symList.isUniversal()) {
					retList.add(Cell.genericCell);
				} else {
					symList.applyAllExpanded(sym -> {
						retList.addAll(sym.getPointsTo(node));
					});
				}
				return retList;
			// // OLD CODE:
			// // *x refers to all those symbols which are pointed to by x,
			// // and it also reads from x.
			// addReads(n, symList);
			// return (new CellList(getOptimizedPointsToSet(symList, nodeList)));
			case "+":
			case "-":
			case "~":
			case "!":
				addReads(n, symList);
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
		public CellList visit(UnarySizeofExpression n) {
			// Expression is not evaluated when present as the argument
			// to sizeof operator.
			return null;
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public CellList visit(SizeofUnaryExpression n) {
			// Expression is not evaluated when present as the argument to sizeof expression
			return null;
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= "("
		 * f2 ::= TypeName()
		 * f3 ::= ")"
		 */
		@Override
		public CellList visit(SizeofTypeName n) {
			return null;
		}

		/**
		 * f0 ::= PrimaryExpression()
		 * f1 ::= PostfixOperationsList()
		 */
		@Override
		public CellList visit(PostfixExpression n) {
			Node cfgNode = this.cfgLeaf;

			CellList symList = n.getF0().accept(this);
			NodeListOptional nodeList = n.getF1().getF0();
			if (nodeList.getNodes().isEmpty()) {
				return symList;
			} else {
				int size = nodeList.getNodes().size();
				int i = 0;
				while (i < size) {
					Node opNode = ((APostfixOperation) nodeList.getNodes().get(i)).getF0().getChoice();

					if (opNode instanceof BracketExpression) {
						BracketExpression brackExp = (BracketExpression) opNode;
						CellList e1List = symList;
						CellList e2List = brackExp.getF1().accept(this);

						addReads(n, e1List);
						addReads(n, e2List);

						/*
						 * Find the list with integer type.
						 */
						CellList tempList = new CellList();
						if (e1List != null) {
							if (e1List.isUniversal()) {
								tempList.add(Cell.genericCell);
							} else {
								e1List.applyAllExpanded(cell -> {
									tempList.addAll(cell.getPointsTo(cfgNode));
								});
							}
						}
						if (e2List != null) {
							if (e2List.isUniversal()) {
								tempList.add(Cell.genericCell);
							} else {
								e2List.applyAllExpanded(cell -> {
									tempList.addAll(cell.getPointsTo(cfgNode));
								});
							}
						}

						symList = tempList;
					} else if (opNode instanceof ArgumentList) {
						// assert (false); // Cannot have this operator here after expression
						// simplification.
						return null;
					} else if (opNode instanceof DotId) {
						// ||p|.id| = |p|
						addReads(n, symList);
						// Now, the same set of symbols represents symbol.id,
						// hence we do not update symList.
					} else if (opNode instanceof ArrowId) {
						addReads(n, symList);

						CellList tempList = new CellList();
						if (symList.isUniversal()) {
							tempList.add(Cell.genericCell);
						} else {
							symList.applyAllExpanded(cell -> {
								tempList.addAll(cell.getPointsTo(cfgNode));
							});
						}
						symList = tempList;
						addReads(n, symList);
						// Now, the same set of symbols as (*sym) represents (*sym).id,
						// hence we do not update symList.
					} else if (opNode instanceof PlusPlus) {
						addReads(n, symList);
						addWrites(n, symList);
						symList = new CellList();
					} else if (opNode instanceof MinusMinus) {
						addReads(n, symList);
						addWrites(n, symList);
						symList = new CellList();
					} else {
						assert (false);
					}
					i++;
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
		public CellList visit(BracketExpression n) {
			addReads(n, n.getF1().accept(this));
			return null;
		}

		/**
		 * f0 ::= "("
		 * f1 ::= ( ExpressionList() )?
		 * f2 ::= ")"
		 */
		@Override
		public CellList visit(ArgumentList n) {
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
		public CellList visit(PrimaryExpression n) {
			if (n.getF0().getChoice() instanceof NodeToken) {
				CellList _ret = new CellList();
				Cell sym = Misc.getSymbolOrFreeEntry((NodeToken) n.getF0().getChoice());
				if (sym != null) {
					_ret.add(sym);
				}
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
		public CellList visit(ExpressionClosed n) {
			return n.getF1().accept(this);
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public CellList visit(ExpressionList n) {
			// Since we have performed expression simplification,
			// we won't enter this operator.
			assert (false);

			// Put the closure of expressions in read and write lists.
			// Pure arguments, and non-array non-pointer arguments should
			// not be taken a closure of, but assuming that points-to
			// information will take care of that, we take closure of
			// all the arguments.
			List<Node> nodeList = new ArrayList<>();
			nodeList.add(this.cfgLeaf);

			CellList symList = n.getF0().accept(this);
			addReads(n, symList);
			CellSet symSet = getOptimizedPointsToClosureObsolete(symList, nodeList);
			addReads(n, new CellList(symSet));
			addWrites(n, new CellList(symSet));

			for (Node seq : n.getF1().getNodes()) {
				symList = ((NodeSequence) seq).getNodes().get(1).accept(this);
				addReads(n, symList);
				symSet = getOptimizedPointsToClosureObsolete(symList, nodeList);
				addReads(n, new CellList(symSet));
				addWrites(n, new CellList(symSet));
			}
			return null;
		}

		/**
		 * f0 ::= <INTEGER_LITERAL>
		 * | <FLOATING_POINT_LITERAL>
		 * | <CHARACTER_LITERAL>
		 * | ( <STRING_LITERAL> )+
		 */
		@Override
		public CellList visit(Constant n) {
			return null;
		}

		@Override
		public CellList visit(CallStatement n) {
			// AccessGetter should be called only on leaf nodes or their parts.
			// CallStatement is NOT a leaf node now.
			assert (false);
			return null;
		}

		@Override
		public CellList visit(PreCallNode n) {
			CallStatement callStmt = n.getParent();
			addReads(n, callStmt.getInfo().getCalledSymbols());
			for (SimplePrimaryExpression argument : n.getArgumentList()) {
				addReads(n, argument.accept(this));
			}
			/*
			 * Update: 27-Apr-18
			 * If the corresponding CallStatement does not have any known
			 * targets,
			 * then conservatively we should assume that this PreCallNode can
			 * read/write
			 * to all the locations that are accessible via the arguments.
			 */
			if (callStmt.getInfo().getCalledDefinitions().isEmpty()) {
				CellList escapingHeads = new CellList();
				for (SimplePrimaryExpression argument : n.getArgumentList()) {
					if (argument.isAConstant()) {
						continue;
					}
					escapingHeads.add(Misc.getSymbolOrFreeEntry(argument.getIdentifier()));
				}
				addWrites(n, new CellList(this.getOptimizedPointsToClosure(escapingHeads, n)));
				addReads(n, new CellList(this.getOptimizedPointsToClosure(escapingHeads, n)));
			}

			return null;
		}

		@Override
		public CellList visit(PostCallNode n) {
			if (n.hasReturnReceiver()) {
				addWrites(n, n.getReturnReceiver().accept(this));
			}
			// CellList retList = new CellList();
			// CallStatement callStmt = (CallStatement) n.getParent();
			// retList.addAll(HeapCell.getHeapCells(callStmt));
			// return retList;
			return null;
		}

		@Override
		public CellList visit(SimplePrimaryExpression n) {
			CellList symList = new CellList();
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

	private static class MayWriteChecker extends DepthFirstProcess {
		public boolean mayWrite = false;

		/**
		 * f0 ::= Declarator()
		 * f1 ::= ( "=" Initializer() )?
		 */
		@Override
		public void visit(InitDeclarator n) {
			if (n.getF1().present()) {
				mayWrite = true;
			}
		}

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ParameterAbstraction()
		 */
		@Override
		public void visit(ParameterDeclaration n) {
			NodeToken parameterToken = ParameterDeclarationInfo.getRootParamNodeToken(n);
			if (parameterToken != null) {
				mayWrite = true;
			}
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForInitExpression n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "++"
		 */
		@Override
		public void visit(PostIncrementId n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "--"
		 */
		@Override
		public void visit(PostDecrementId n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(PreIncrementId n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(PreDecrementId n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "+="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(ShortAssignPlus n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "-="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(ShortAssignMinus n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "+"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public void visit(OmpForAdditive n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "-"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public void visit(OmpForSubtractive n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= MultiplicativeExpression()
		 * f3 ::= "+"
		 * f4 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(OmpForMultiplicative n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public void visit(NonConditionalExpression n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(UnaryExpressionPreIncrement n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(UnaryExpressionPreDecrement n) {
			mayWrite = true;
		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public void visit(UnarySizeofExpression n) {
			// Expression is not evaluated when present as the argument to sizeof expression
			return;
		}

		/**
		 * f0 ::= <SIZEOF>
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(SizeofUnaryExpression n) {
			// Expression is not evaluated when present as the argument to sizeof expression
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
			NodeListOptional nodeList = n.getF1().getF0();
			if (nodeList.getNodes().isEmpty()) {
				return;
			} else {
				int size = nodeList.getNodes().size();
				int i = 0;
				while (i < size) {
					Node opNode = ((APostfixOperation) nodeList.getNodes().get(i)).getF0().getChoice();

					if (opNode instanceof BracketExpression) {
					} else if (opNode instanceof ArgumentList) {
						return;
					} else if (opNode instanceof DotId) {
					} else if (opNode instanceof ArrowId) {
					} else if (opNode instanceof PlusPlus) {
						mayWrite = true;
						return;
					} else if (opNode instanceof MinusMinus) {
						mayWrite = true;
						return;
					} else {
						assert (false);
					}
					i++;
				}
				return;
			}
		}

		/**
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public void visit(ExpressionList n) {
			assert (false);
		}

		@Override
		public void visit(PreCallNode n) {
			CallStatement callStmt = n.getParent();
			if (callStmt.getInfo().getCalledDefinitions().isEmpty()) {
				mayWrite = true;
			}
		}

		@Override
		public void visit(PostCallNode n) {
			if (n.hasReturnReceiver()) {
				mayWrite = true;
			}
		}
	}

	private static class MayUpdatePointsToGetter extends DepthFirstProcess {
		public boolean mayUpdatePointsTo = false;

		/**
		 * f0 ::= Declarator()
		 * f1 ::= ( "=" Initializer() )?
		 */
		@Override
		public void visit(InitDeclarator n) {
			if (n.getF1().present()) {
				String symName = DeclarationInfo.getRootIdName(n.getF0());
				Symbol sym = Misc.getSymbolEntry(symName, n);
				if (sym == null) {
					Misc.warnDueToLackOfFeature("Could not find symbol for " + symName, n);
				} else if (sym.getType() instanceof PointerType) {
					mayUpdatePointsTo = true;
					return;
				}
			}
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= DeclarationSpecifiers()
		 * f1 ::= ParameterAbstraction()
		 */
		@Override
		public void visit(ParameterDeclaration n) {
			NodeToken parameterToken = ParameterDeclarationInfo.getRootParamNodeToken(n);
			if (parameterToken != null) {
				Symbol param = Misc.getSymbolEntry(parameterToken.getTokenImage(), n);
				if (param == null) {
					Misc.warnDueToLackOfFeature("Could not find symbol for " + parameterToken, n);
				} else if (param.getType() instanceof PointerType) {
					mayUpdatePointsTo = true;
				}
			}
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(OmpForInitExpression n) {
			Symbol sym = Misc.getSymbolEntry(n.getF0().getTokenImage(), n);
			if (sym == null) {
				Misc.warnDueToLackOfFeature("Could not find a symbol for " + n.getF0().getTokenImage(), n);
			} else if (sym.getType() instanceof PointerType) {
				mayUpdatePointsTo = true;
				return;
			}
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "++"
		 */
		@Override
		public void visit(PostIncrementId n) {
			/*
			 * Note that our points-to analysis does not get affected by pointer
			 * arithmetic.
			 */
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "--"
		 */
		@Override
		public void visit(PostDecrementId n) {
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(PreIncrementId n) {
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(PreDecrementId n) {
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "+="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(ShortAssignPlus n) {
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "-="
		 * f2 ::= Expression()
		 */
		@Override
		public void visit(ShortAssignMinus n) {
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "+"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public void visit(OmpForAdditive n) {
			n.getF4().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= <IDENTIFIER>
		 * f3 ::= "-"
		 * f4 ::= AdditiveExpression()
		 */
		@Override
		public void visit(OmpForSubtractive n) {
			n.getF4().accept(this);
		}

		/**
		 * f0 ::= <IDENTIFIER>
		 * f1 ::= "="
		 * f2 ::= MultiplicativeExpression()
		 * f3 ::= "+"
		 * f4 ::= <IDENTIFIER>
		 */
		@Override
		public void visit(OmpForMultiplicative n) {
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= UnaryExpression()
		 * f1 ::= AssignmentOperator()
		 * f2 ::= AssignmentExpression()
		 */
		@Override
		public void visit(NonConditionalExpression n) {
			String operator = ((NodeToken) n.getF1().getF0().getChoice()).getTokenImage();
			if (operator.equals("=")) {
				Type lhsType = Type.getType(n.getF0());
				if (lhsType instanceof PointerType) {
					if (((PointerType) lhsType).isFromPointer()) {
						mayUpdatePointsTo = true;
						return;
					}
				}
			}
			n.getF0().accept(this);
			n.getF2().accept(this);
		}

		/**
		 * f0 ::= "++"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(UnaryExpressionPreIncrement n) {
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= "--"
		 * f1 ::= UnaryExpression()
		 */
		@Override
		public void visit(UnaryExpressionPreDecrement n) {
			n.getF1().accept(this);
		}

		/**
		 * f0 ::= SizeofTypeName()
		 * | SizeofUnaryExpression()
		 */
		@Override
		public void visit(UnarySizeofExpression n) {
			// Expression is not evaluated when present as the argument to sizeof expression
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
		 * f0 ::= AssignmentExpression()
		 * f1 ::= ( "," AssignmentExpression() )*
		 */
		@Override
		public void visit(ExpressionList n) {
			assert (false);
		}

		@Override
		public void visit(PreCallNode n) {
			CallStatement callStmt = n.getParent();
			if (callStmt.getInfo().getCalledDefinitions().isEmpty()) {
				mayUpdatePointsTo = true;
			}
		}

		@Override
		public void visit(PostCallNode n) {
			if (n.hasReturnReceiver()) {
				String symName = n.getReturnReceiver().getIdentifier().getTokenImage();
				Symbol sym = Misc.getSymbolEntry(symName, n);
				if (sym == null) {
					Misc.warnDueToLackOfFeature("Could not find symbol for " + sym, n);
				} else if (sym.getType() instanceof PointerType) {
					mayUpdatePointsTo = true;
				}
			}
		}
	}
}
