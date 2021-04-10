/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis;

import imop.ast.info.cfgNodeInfo.CompoundStatementInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis;
import imop.lib.analysis.flowanalysis.dataflow.PointsToAnalysis.PointsToFlowMap;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis;
import imop.lib.analysis.typeSystem.*;
import imop.lib.builder.Builder;
import imop.lib.util.CellSet;
import imop.lib.util.ImmutableCellSet;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an abstraction of a variable (or stack-element) in C,
 * along with functions.
 *
 * @author Aman Nougrahiya
 */
public class Symbol extends Cell {
	//
	// /**
	// * Represents an abstraction over all the heap cells.
	// */
	// protected static Symbol genericCell = new Symbol();
	//
	// public static Symbol getGenericCell() {
	// return genericCell;
	// }

	public static List<Symbol> allSymbols = new LinkedList<>();
	public static boolean dontSaveSymbol = false;

	private final String name; // refers to the name of a variable for this Symbol
	private Node declaringNode; // Declaration, ParameterDeclaration or
	// FunctionDefinition for this Symbol
	private Scopeable definingScope; // Refers to the enclosing scope-level node for this Symbol
	private final Type type; // Type object, representing the expanded type of this
	// symbol. Can be null, in case of a typedef.

	private int cachedHashCode = -1; // Used to cache the hash value of this object.

	private AddressCell addressCell; // refers to the address-of this symbol.
	private FieldCell fieldCell; // refers to "aggregate.f" for any aggregate type.

	// private Symbol() {
	// this.name = "generic_stack";
	// // this.setName("generic_stack");
	// this.definingScope = null;
	// if (allSymbols == null) {
	// allSymbols = new ArrayList<>();
	// }
	// if (!Symbol.dontSaveSymbol) {
	// allSymbols.add(this);
	// }
	// if (!Cell.dontSave) {
	// allCells.add(this);
	// }
	// }
	//
	public Symbol(String name, Type type, Node declaringNode, Scopeable definingScope) {
		if (!Symbol.dontSaveSymbol && !(type.isComplete() || type instanceof ArrayType)) {
			System.err.println(name + " is not declared with a complete type. Its type is " + type);
			// System.err.println(declaringNode + ": " + definingScope);
			// System.exit(0);
		}
		if (declaringNode != null) {
			assert declaringNode instanceof Declaration || declaringNode instanceof ParameterDeclaration
					|| declaringNode instanceof FunctionDefinition;
		}
		if (definingScope != null) {
			assert definingScope instanceof CompoundStatement || definingScope instanceof FunctionDefinition
					|| definingScope instanceof TranslationUnit;
		}

		this.name = name;
		this.declaringNode = declaringNode;
		this.definingScope = definingScope;
		this.type = type;
		if (allSymbols == null) {
			allSymbols = new LinkedList<>();
		}
		if (!Symbol.dontSaveSymbol) {
			allSymbols.add(this);
		}
		if (!Cell.dontSave) {
			// if
			// (!FrontEnd.getK2Builtins().getInfo().getSymbolTable().containsKey(this.name))
			// {
			// if
			// (!FrontEnd.getMacBuiltins().getInfo().getSymbolTable().containsKey(this.name))
			// {
			// if
			// (!FrontEnd.getOtherBuiltins().getInfo().getSymbolTable().containsKey(this.name))
			// {
			// allCells.add(this);
			// }
			// }
			// }
			allCells.add(this);
			// if (this.isAFunction()) {
			// allFunctionCells.add(this);
			// } else {
			// allCells.add(this);
			// }
		}
	}

	public Cell getAddressCell() {
		if (this.getType() instanceof ArrayType) {
			return this;
		}
		if (addressCell == null) {
			addressCell = new AddressCell(this);
		}
		return addressCell;
	}

	public boolean hasAddressCell() {
		if (this.getType() instanceof ArrayType) {
			return false;
		}
		return addressCell != null;
	}

	public FieldCell getFieldCell() {
		if (this.type instanceof ArrayType) {
			if (fieldCell == null) {
				fieldCell = new FieldCell(this);
			}
			return fieldCell;
		} else {
			Misc.warnDueToLackOfFeature("Cannot refer to field-cell of a non-array type.", null);
			return null;
		}
	}

	public boolean hasFieldCell() {
		return fieldCell != null;
	}

	// private Symbol() {}
	// private static Set<Symbol> phantomSharedSet;
	//
	// /**
	// * Obtain a set with a singleton symbol object, which is used to denote all
	// the shared symbols in a given context.
	// * @return
	// * a set with a singleton symbol object, which is used to denote all the
	// shared symbols in a given context.
	// */
	// public static Set<Symbol> getPhantomSharedSet() {
	// if (phantomSharedSet == null) {
	// phantomSharedSet = new HashSet<>();
	// phantomSharedSet.add(new Symbol());
	// }
	// return phantomSharedSet;
	// }

	/**
	 * @return true if the declaration for this symbol has STATIC modifier
	 */
	public boolean isStatic() {
		if (getDeclaringNode() == null) {
			return false;
		}
		if (getDeclaringNode() instanceof Declaration) {
			Declaration decl = (Declaration) getDeclaringNode();
			HasStaticModifier hasStatic = new HasStaticModifier();
			decl.accept(hasStatic);
			return hasStatic.foundStatic;
		}
		return false;
	}

	public boolean isAFunction() {
		if (this.getType() != null && this.getType() instanceof FunctionType) {
			return true;
		}
		return false;
	}

	public boolean isAVariable() {
		if (!this.isAFunction()) {
			return true;
		} else {
			return false;
		}
	}

	public String getName() {
		return name;
	}

	/**
	 * Declaration, ParameterDeclaration, or FunctionDefinition, which declares this
	 * symbol.
	 *
	 * @return Declaration, ParameterDeclaration, or FunctionDefinition, which
	 *         declares this symbol.
	 */
	public Node getDeclaringNode() {
		return declaringNode;
	}

	/**
	 * Enclosing scope (CompoundStatement, FunctionDefinition, or TranslationUnit)
	 * in which this symbol was declared.
	 *
	 * @return enclosing scope (CompoundStatement, FunctionDefinition, or
	 *         TranslationUnit) in which this symbol was
	 *         declared.
	 */
	public Scopeable getDefiningScope() {
		return definingScope;
	}

	public Type getType() {
		return type;
	}

	/**
	 * Creates a new temporary with same assignable type as that of this symbol, and
	 * adds the declaration of the newly
	 * created temporary in the scope of this symbol.
	 *
	 * @return name of the newly created temporary.
	 */
	public String createAndInsertCopyDeclaration() {
		Symbol sym = this;
		String tempStr = Builder.getNewTempName(sym.toString());
		Declaration newSymbolDecl = sym.getType().getDeclaration(tempStr);
		Scopeable scope = sym.getDefiningScope();
		if (scope instanceof CompoundStatement) {
			CompoundStatement compStmt = (CompoundStatement) scope;
			compStmt.getInfo().getCFGInfo().addDeclaration(newSymbolDecl);
		} else if (scope instanceof FunctionDefinition) {
			FunctionDefinition funcDef = (FunctionDefinition) scope;
			CompoundStatement body = funcDef.getInfo().getCFGInfo().getBody();
			body.getInfo().getCFGInfo().addDeclaration(newSymbolDecl);
		} else if (scope instanceof TranslationUnit) {
			Builder.addDeclarationToGlobals(newSymbolDecl);
		}
		return tempStr;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Symbol)) {
			return false;
		}
		Symbol that = (Symbol) obj;
		if (this == that) {
			return true;
		}
		if (this.name == null) {
			if (that.name == null) {
				return this.definingScope == that.definingScope;
			} else {
				return false;
			}
		} else {
			if (that.name == null) {
				return false;
			}
		}
		if (this.name.equals(that.name) && this.definingScope == that.definingScope) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (cachedHashCode == -1) {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((definingScope == null) ? 0 : definingScope.hashCode());
			cachedHashCode = result;
		}
		return cachedHashCode;
	}

	@Override
	@Deprecated
	public CellSet deprecated_getPointsTo(Node node) {
		node = Misc.getCFGNodeFor(node);
		if (this.type instanceof ArrayType) {
			CellSet pointsToSet = new CellSet();
			pointsToSet.add(this.getFieldCell());
			return pointsToSet;
		}
		if (Program.basePointsTo) {
			return this.getDefaultPointsTo(node);
		} else {
			CellSet retSet = new CellSet();
			PointsToFlowMap pff = (PointsToFlowMap) node.getInfo().getIN(AnalysisName.POINTSTO);
			if (pff == null) {
				return retSet; // return an empty points-to set.
			} else {
				retSet = pff.flowMap.get(this);
				if (retSet == null) {
					retSet = new CellSet();
				}
				return retSet;
			}
		}
	}

	// public static Symbol tempNow;

	@Override
	public ImmutableCellSet getPointsTo(Node node) {
		// tempNow = this;
		node = Misc.getCFGNodeFor(node);
		if (this.type instanceof ArrayType) {
			CellSet pointsToSet = new CellSet();
			pointsToSet.add(this.getFieldCell());
			return new ImmutableCellSet(pointsToSet);
		}
		if (Program.basePointsTo) {
			return new ImmutableCellSet(this.getDefaultPointsTo(node));
		} else {
			if (this.type instanceof FunctionType) {
				CellSet pointsToSet = new CellSet();
				pointsToSet.add(this);
				return new ImmutableCellSet(pointsToSet);
			} else if (this.type instanceof ArithmeticType || this.type instanceof UnionType
					|| this.type instanceof StructType) {
				return new ImmutableCellSet();
			}
			ImmutableCellSet retSet;
			PointsToFlowMap opff = (PointsToFlowMap) node.getInfo().getIN(AnalysisName.POINTSTO, this);
			if (opff == null) {
				return new ImmutableCellSet(); // return an empty points-to set.
			} else {
				retSet = opff.flowMap.get(this);
				if (retSet == null) {
					// TODO: Uncomment this and test client pass on histo, lbm, and fft.
					// assert (Program.memoizeAccesses > 0 || (this.type instanceof PointerType
					// && ((PointerType) this.type).getPointeeType() instanceof CharType))
					// : "Is it correct to not have any points-to information for the symbol " +
					// this.getName()
					// + " at the node " + node + "\n. Note that the state of stale flag of PTA is "
					// + FlowAnalysis.getAllAnalyses().get(AnalysisName.POINTSTO).stateIsInvalid()
					// + ", and the nodes that need stabilization are as follows: "
					// + FlowAnalysis.getAllAnalyses().get(AnalysisName.POINTSTO).nodesToBeUpdated;
					retSet = new ImmutableCellSet();
				}
				return retSet;
			}
		}
	}

	/**
	 * Given a node, this method returns the default points-to set for this Symbol
	 * at that node, when no points-to
	 * analysis has been run.
	 *
	 * @param node
	 *             node at which the points-to information is required.
	 *
	 * @return the base points-to set, which should be returned until points-to
	 *         analysis has started.
	 */
	public CellSet getDefaultPointsTo(Node node) {
		CellSet pointsToSet = new CellSet();
		if (this.type instanceof ArrayType) {
			/*
			 * Note that an array name represents a pointer to its first element
			 * (except when it is an operand of &, ++ --, sizeof, or as the left
			 * operand of an assignment or the . operator).
			 * Since we assume field-insensitivity, we represent each element of
			 * the array by that array's field cell.
			 */
			pointsToSet.add(this.getFieldCell());
			return pointsToSet;
		} else if (this.type instanceof PointerType) {
			// For field-insensitivity.
			PointerType ptrType = (PointerType) this.type;
			if (!ptrType.isFromPointer()) {
				// I have no idea which branch is this. Putting an assert fail for now.
				assert (false);
				pointsToSet.add(this);
				return pointsToSet;
			} else {
				pointsToSet.add(Cell.genericCell);
			}
		} else if (this.type instanceof FunctionType) {
			pointsToSet.add(Cell.genericCell);
		} else {
			// Otherwise, we return an empty set.
		}

		return pointsToSet;
	}

	/**
	 * <i>Regardless of what this method does, it should be called only from
	 * {@link CompoundStatementInfo#addDeclarationToSymbolOrTypeTable(Declaration)}.</i>
	 *
	 * @param newCS
	 */
	public void setDefiningScope(CompoundStatement newCS) {
		this.definingScope = newCS;
	}

	/**
	 * <i>Regardless of what this method does, it should be called only from
	 * {@link CompoundStatementInfo#addDeclarationToSymbolOrTypeTable(Declaration)}.</i>
	 *
	 * @param declaration.
	 */
	public void setDeclaringNode(Declaration declaration) {
		this.declaringNode = declaration;
	}

}
