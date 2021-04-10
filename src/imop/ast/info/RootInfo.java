/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.HeapCell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.typeSystem.*;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.getter.AllFunctionDefinitionGetter;
import imop.lib.util.*;
import imop.parser.Program;

import java.util.*;

public class RootInfo extends NodeInfo {

	private List<FunctionDefinition> allFunctionDefinitions;

	/**
	 * Set of all the symbols that can be accessed within this
	 * translation-unit (after corresponding declarations).
	 */
	private CellSet allCellsInclusive;

	/**
	 * Set of all the shared cells that can be accessed anywhere within this
	 * translation-unit (after corresponding declarations).
	 */
	private CellSet allSharedCellsInclusive;

	/**
	 * Set of all the heap cells that are present anywhere within the program.
	 */
	private CellSet allHeapCellsHere;

	/**
	 * Dirty bit for both sets that indicate accessible symbols/cells.
	 */
	private boolean invalidCellSets;

	/**
	 * Invalidate the dirt bit for all the sets that indicate accessible
	 * symbols/cells.
	 */
	public void invalidateCellSets() {
		this.invalidCellSets = true;
		this.invalidateHeapCellSet();
	}

	public void invalidateHeapCellSet() {
		this.allHeapCellsHere = null; // processed slightly differently here.
	}

	public CellSet getAllHeapCells() {
		if (this.allHeapCellsHere == null) {
			this.allHeapCellsHere = new CellSet();
			for (CallStatement callStmt : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
				for (Cell c : HeapCell.getHeapCells(callStmt)) {
					this.allHeapCellsHere.add(c);
					this.allHeapCellsHere.add(((HeapCell) c).getAddressCell());
				}
			}

		}
		return this.allHeapCellsHere;
	}

	private void populateSetsWithHeapCells() {
		this.allCellsInclusive.addAll(this.getAllHeapCells());
		this.allSharedCellsInclusive.addAll(this.getAllHeapCells());
	}

	/**
	 * Set all sets that indicate symbols/cells.
	 */
	private void setAllCellSets() {
		this.allCellsInclusive = new CellSet();
		this.allSharedCellsInclusive = new CellSet();

		TranslationUnit scopeChoice = (TranslationUnit) this.getNode();
		for (Symbol sym : scopeChoice.getInfo().getSymbolTable().values()) {
			if (sym.isAVariable()) {
				allCellsInclusive.add(sym);
				allCellsInclusive.add(sym.getAddressCell());
				if (sym.getType() instanceof ArrayType) {
					allCellsInclusive.add(sym.getFieldCell());
				}
				if (this.getSharingAttribute(sym) == DataSharingAttribute.SHARED) {
					allSharedCellsInclusive.add(sym);
					allSharedCellsInclusive.add(sym.getAddressCell());
					if (sym.getType() instanceof ArrayType) {
						allSharedCellsInclusive.add(sym.getFieldCell());
					}
				}
			}
		}
		this.populateSetsWithHeapCells();
		invalidCellSets = false;
	}

	/**
	 * Obtain a set of all those symbols that are accessible at this node
	 * (inclusively)
	 * (after their declarations).
	 */
	@Override
	public CellSet getAllCellsAtNode() {
		if (allCellsInclusive == null || invalidCellSets) {
			this.setAllCellSets();
		}
		return this.allCellsInclusive;

	}

	/**
	 * Obtain a set of all those symbols that are accessible at this node
	 * (inclusively)
	 * (after their declarations).
	 */
	@Override
	public CellSet getSharedCellsAtNode() {
		if (allSharedCellsInclusive == null || invalidCellSets) {
			this.setAllCellSets();
		}
		return this.allSharedCellsInclusive;
	}

	@Override
	public List<Typedef> getAllTypedefListAtNode() {
		return new ArrayList<>(this.getTypedefTable().values());
	}

	@Override
	public List<Type> getAllTypeListAtNode() {
		return new ArrayList<>(this.getTypeTable().values());
	}

	public RootInfo(Node owner) {
		super(owner);
		// this.populateSymbolTable();
		// this.populateThreadPrivateList();
	}

	private HashMap<String, Symbol> symbolTable;
	private HashMap<String, Typedef> typedefTable;
	private HashMap<String, Symbol> threadPrivateList;
	private HashMap<String, Type> typeTable;

	public static long functionListQueryTimer = 0;

	public List<FunctionDefinition> getAllFunctionDefinitions() {
		long thisTimer = System.nanoTime();
		if (this.allFunctionDefinitions == null) {
			AllFunctionDefinitionGetter funcGetter = new AllFunctionDefinitionGetter();
			getNode().accept(funcGetter);
			this.allFunctionDefinitions = funcGetter.funcList;
		}
		functionListQueryTimer += (System.nanoTime() - thisTimer);
		return this.allFunctionDefinitions;
	}

	public FunctionDefinition getFunctionWithName(String functionName) {
		for (FunctionDefinition funcDef : this.getAllFunctionDefinitions()) {
			if (funcDef.getInfo().getFunctionName().equals(functionName)) {
				return funcDef;
			}
		}
		return null;
	}

	public FunctionDefinition getMainFunction() {
		for (FunctionDefinition func : getAllFunctionDefinitions()) {
			if (func.getInfo().getFunctionName().equals("main")) {
				return func;
			}
		}
		return null;
	}

	/**
	 * This method ensures that whenever a function definition is present
	 * for a particular function, we remove all the duplicate declarations
	 * of that function from the symbol table.
	 */
	// public void removeDuplicateSymbols() {
	// // Get a list of function names whose definitions are present in symbolTable
	// Vector<Symbol> funcWithDef = new Vector<Symbol>();
	// for(Symbol sym : getSymbolTable()) {
	// if(sym.declaringNode instanceof FunctionDefinition) {
	// funcWithDef.add(sym);
	// }
	// }
	//
	// // Remove the other occurrences of these functions from symbolTable
	// for(Symbol defSym : funcWithDef) {
	// int index = 0;
	// while(index < getSymbolTable().size()) {
	// Symbol elem = getSymbolTable().elementAt(index);
	// if(elem.name.equals(defSym.name)) {
	// if(!(elem.declaringNode instanceof FunctionDefinition)) {
	// getSymbolTable().remove(index);
	// continue;
	// }
	// }
	// index++;
	// }
	// }
	//
	// }

	public void populateThreadPrivateList() {
		this.threadPrivateList = new HashMap<>();

		TranslationUnit n = ((TranslationUnit) getNode());
		for (Node elementChoice : n.getF0().getNodes()) {
			ElementsOfTranslation elemTransChoice = ((ElementsOfTranslation) elementChoice);
			Node extDeclChoice = elemTransChoice.getF0().getChoice();
			if (extDeclChoice instanceof ExternalDeclaration) {
				ExternalDeclaration extDecl = ((ExternalDeclaration) extDeclChoice);
				Node threadPrivChoice = extDecl.getF0().getChoice();
				if (threadPrivChoice instanceof ThreadPrivateDirective) {
					ThreadPrivateDirective threadPriv = (ThreadPrivateDirective) threadPrivChoice;
					VariableList varList = threadPriv.getF3();
					Symbol sym = Misc.getSymbolEntry(varList.getF0().getTokenImage(), n);
					if (sym == null) {
						System.out.println("Found no declaration for " + varList.getF0().getTokenImage());
						assert false;
					}
					getThreadPrivateList().put(varList.getF0().getTokenImage(), sym);
					for (Node nodeChoice : varList.getF1().getNodes()) {
						String varName = ((NodeToken) ((NodeSequence) nodeChoice).getNodes().get(1)).getTokenImage();
						sym = Misc.getSymbolEntry(varName, n);
						if (sym == null) {
							System.out.println("Found no declaration for " + varName);
							assert false;
						}
						getThreadPrivateList().put(varName, sym);
					}

				}
			}
		}

	}

	/**
	 * This method populates all the symbol and type tables of
	 * the owner CompoundStatement "node" of this info.
	 * <p>
	 * Various structures that get populated are:
	 * <ol>
	 * <li>Symbol Table.</li>
	 * <li>Typedef Table.</li>
	 * <li>Type Table.</li>
	 * </ol>
	 */
	public void populateSymbolTable() {
		this.symbolTable = new HashMap<>();
		this.typedefTable = new HashMap<>();
		this.typeTable = new HashMap<>();

		// Find a list of Declarations made at the global level
		TranslationUnit n = ((TranslationUnit) getNode());
		for (Node elementChoice : n.getF0().getNodes()) {
			ElementsOfTranslation elemTransChoice = ((ElementsOfTranslation) elementChoice);
			Node extDeclChoice = elemTransChoice.getF0().getChoice();
			if (!(extDeclChoice instanceof ExternalDeclaration)) {
				continue;
			}
			ExternalDeclaration extDecl = ((ExternalDeclaration) extDeclChoice);
			Node declChoice = extDecl.getF0().getChoice();
			if (declChoice instanceof Declaration) {
				// Note: This declaration may be that of a Function or a Variable
				Declaration declaration = (Declaration) declChoice;
				List<String> declaratorNameList = declaration.getInfo().getIDNameList();
				assert (declaratorNameList != null);
				if (declaratorNameList.isEmpty()) {
					// This may just be a definition of certain type, without use of that type.
					// In this case, we simply visit the declaration, so as to collect the defined
					// type.
					Type.getTypeTree(declaration.getF0(), (TranslationUnit) this.getNode());
				}

				for (String declaratorName : declaratorNameList) {
					Type declaratorType = Type.getTypeTree(declaration, declaratorName,
							(TranslationUnit) this.getNode());
					if (declaration.getInfo().isTypedef()) {
						/*
						 * If this declaration is a Typedef.
						 */
						if (typedefTable.containsKey(declaratorName)) {
							continue;
						}
						Typedef newTypedef = new Typedef(declaratorName, declaratorType, declaration,
								(TranslationUnit) this.getNode());
						// System.out.println("Adding a typedef for " + declaratorName
						// + " with type " + newTypedef.getType());
						this.typedefTable.put(declaratorName, newTypedef);
					} else {
						/*
						 * Note that if this symbol is a function, we should
						 * ensure that
						 * there are no entries for prototypes unless one
						 * without the body
						 * is indeed present.
						 * The following line (and the last one of the
						 * outer-most if-else) ensures
						 * that a function declaration is added (and
						 * persisted)
						 * only if we do not encounter the definition for
						 * this function anywhere.
						 */
						if (symbolTable.containsKey(declaratorName)) {
							continue;
						}
						Symbol newSymbol = new Symbol(declaratorName, declaratorType, declaration,
								(TranslationUnit) this.getNode());
						this.symbolTable.put(declaratorName, newSymbol);
					}
				}
			} else if (declChoice instanceof FunctionDefinition) {
				// Note: A definition is a declaration as well.
				// Ensure that only one entry for any given function is present in the symbol
				// table.
				// Furthermore, this entry shall be replaced with the definition, if that indeed
				// is the case.
				FunctionDefinition funcDef = (FunctionDefinition) declChoice;

				// Obtain the name of the function
				String functionName = funcDef.getInfo().getFunctionName();
				Type functionType = Type.getTypeTree(funcDef, (TranslationUnit) this.getNode());
				// Check if the function name is already present in the symbol table
				// continue if so.
				// else, add this function to the symbol table
				getSymbolTable().put(functionName,
						new Symbol(functionName, functionType, funcDef, (TranslationUnit) getNode()));

			}
		}
	}

	public HashMap<String, Symbol> getSymbolTable() {
		ProfileSS.addRelevantChangePoint(ProfileSS.symSet);
		if (symbolTable == null) {
			populateSymbolTable();
		}
		return symbolTable;
	}

	public HashMap<String, Typedef> getTypedefTable() {
		ProfileSS.addRelevantChangePoint(ProfileSS.symSet);
		if (typedefTable == null) {
			populateSymbolTable();
		}
		return typedefTable;
	}

	public HashMap<String, Symbol> getThreadPrivateList() {
		ProfileSS.addRelevantChangePoint(ProfileSS.symSet);
		if (threadPrivateList == null) {
			populateThreadPrivateList();
		}
		return threadPrivateList;
	}

	public void setThreadPrivateList(HashMap<String, Symbol> threadPrivateList) {
		this.threadPrivateList = threadPrivateList;
	}

	public HashMap<String, Type> getTypeTable() {
		if (typeTable == null) {
			populateSymbolTable();
		}
		return typeTable;
	}

	public void addDeclarationEffects(Declaration declaration) {
		// Note: This declaration may be that of a Function or a Variable
		List<String> declaratorNameList = declaration.getInfo().getIDNameList();
		assert (declaratorNameList != null);
		if (declaratorNameList.isEmpty()) {
			// This may just be a definition of certain type, without use of that type.
			// In this case, we simply visit the declaration, so as to collect the defined
			// type.
			Type.getTypeTree(declaration.getF0(), (TranslationUnit) this.getNode());
		}

		for (String declaratorName : declaratorNameList) {
			Type declaratorType = Type.getTypeTree(declaration, declaratorName, (TranslationUnit) this.getNode());
			if (declaration.getInfo().isTypedef()) {
				/*
				 * If this declaration is a Typedef.
				 */
				if (typedefTable.containsKey(declaratorName)) {
					continue;
				}
				Typedef newTypedef = new Typedef(declaratorName, declaratorType, declaration,
						(TranslationUnit) this.getNode());
				// System.out.println("Adding a typedef for " + declaratorName
				// + " with type " + newTypedef.getType());
				this.typedefTable.put(declaratorName, newTypedef);
			} else {
				/*
				 * Note that if this symbol is a function, we should
				 * ensure that
				 * there are no entries for prototypes unless one
				 * without the body
				 * in indeed present.
				 * The following line (and the last one of the
				 * outer-most if-else) ensures
				 * that a function declaration is added (and
				 * persisted)
				 * only if we do not encounter the definition for
				 * this function anywhere.
				 */

				if (symbolTable.containsKey(declaratorName)) {
					continue;
				}
				Symbol newSymbol = new Symbol(declaratorName, declaratorType, declaration,
						(TranslationUnit) this.getNode());
				this.symbolTable.put(declaratorName, newSymbol);
			}
		}
	}

	public void removeDeclarationEffects(Declaration declaration) {
		AutomatedUpdater.invalidateEnclosedSymbolSets(this.getNode());

		// Note: This declaration may be that of a Function or a Variable
		List<String> declaratorNameList = declaration.getInfo().getIDNameList();
		assert (declaratorNameList != null);

		for (String declaratorName : declaratorNameList) {
			if (declaration.getInfo().isTypedef()) {
				/*
				 * If this declaration is a Typedef.
				 */
				if (typedefTable.containsKey(declaratorName)) {
					typedefTable.remove(declaratorName);
				}
			} else {
				Symbol sym = this.getSymbolTable().get(declaratorName);
				if (sym != null) {
					CellCollection.removeCellFromDerivedCollections(sym);
					this.symbolTable.remove(declaratorName);
				} else {
					this.getTypeTable().remove(declaratorName);
				}
				continue;
			}
		}
		Program.invalidColumnNum = Program.invalidLineNum = true;
	}

	public void removeTypeDeclarationEffects(Declaration declaration) {
		// TODO: Uncomment these two lines below, after removing the declaration from
		// use, for user-defined types,
		// in ExpressionSimplifier.
		// List<String> declaratorNameList = Misc.getIdNameList(declaration);
		// assert(declaratorNameList.isEmpty());
		Type t = Type.getTypeTree(declaration.getF0(), (TranslationUnit) this.getNode());
		String typeName = null;
		if (t instanceof StructType) {
			typeName = ((StructType) t).getName();
		} else if (t instanceof UnionType) {
			typeName = ((UnionType) t).getTag();
		} else if (t instanceof EnumType) {
			typeName = ((EnumType) t).getTag();
		}
		if (typeName == null) {
			return;
		}
		assert (typeTable.containsKey(typeName));
		typeTable.remove(typeName);
		Program.invalidColumnNum = Program.invalidLineNum = true;
	}

	public void removeFunctionDefinitionEffects(FunctionDefinition funcDef) {
		String name = funcDef.getInfo().getFunctionName();
		Symbol sym = this.getSymbolTable().get(name);
		if (sym != null) {
			CellCollection.removeCellFromDerivedCollections(sym);
			this.symbolTable.remove(name);
		}
		HashMap<String, Symbol> funcSymTable = funcDef.getInfo().getSymbolTable();
		for (Symbol funcSym : funcSymTable.values()) {
			CellCollection.removeCellFromDerivedCollections(funcSym);
			funcSymTable.remove(name);
		}
		for (CompoundStatement compStmt : Misc.getInheritedEnclosee(funcDef, CompoundStatement.class)) {
			HashMap<String, Symbol> compSymTable = compStmt.getInfo().getSymbolTable();
			for (Symbol compSym : compSymTable.values()) {
				CellCollection.removeCellFromDerivedCollections(compSym);
				funcSymTable.remove(name);
			}

		}
	}

	/**
	 * Removes unused functions from this {@link TranslationUnit}.
	 * Then, this methods performs the removal of other elements (symbols,
	 * types, etc.) by calling the super-class
	 * {@link NodeInfo#removeUnusedElements()} method.
	 */
	@Override
	public void removeUnusedElements() {
		removeUnusedFunctions();
		super.removeUnusedElements();
	}

	/**
	 * Removes the function definition and declaration for all those functions
	 * (except {@code main}) that have not been called anywhere in the program.
	 * 
	 * @param baseNode
	 *                 a {@link TranslationUnit} from within which all unused
	 *                 function definitions and declarations have to be removed.
	 */
	public void removeUnusedFunctions() {
		TranslationUnit baseNode = (TranslationUnit) getNode();
		/**
		 * Step 1: Obtain the set of function names that have been called
		 * somewhere.
		 */
		FunctionDefinition mainFunc = baseNode.getInfo().getMainFunction();
		if (mainFunc == null) {
			return;
		}
		Set<String> usedFuncNames = new HashSet<>();
		usedFuncNames.add("main");
		for (CallStatement cs : mainFunc.getInfo().getReachableCallStatementsInclusive()) {
			CellList calledSymbols = cs.getInfo().getCalledSymbols();
			if (calledSymbols.isUniversal()) {
				System.err.println(
						"Does this program use function pointers? If not, check this line in RootInfo::removeUnusedFunctions.");
				continue;
			}
			calledSymbols.getReadOnlyInternal().stream().forEach(sym -> {
				if (sym instanceof Symbol) {
					usedFuncNames.add(((Symbol) sym).getName());
				}
			});
			// OLD CODE:
			// for (Cell cell : calledSymbols) {
			// if (!(cell instanceof Symbol)) {
			// continue;
			// }
			// usedFuncNames.add(((Symbol) cell).getName());
			// }
		}

		/*
		 * Step 2: In a no-update manner, remove all those FunctionDefinition
		 * and Declaration which define or declare a function that has not been
		 * used anywhere.
		 */
		System.err.print("\tDeleting the definitions/declarations for the following unused functions: ");
		boolean deletedAny = false;
		for (ExternalDeclaration extDecl : Misc.getInheritedEnclosee(baseNode, ExternalDeclaration.class)) {
			Node choice = extDecl.getF0().getChoice();
			if (choice instanceof Declaration) {
				Declaration decl = (Declaration) choice;
				List<String> idNames = decl.getInfo().getIDNameList();
				if (idNames.size() == 1) {
					String idName = idNames.get(0);
					Type declType = Type.getTypeTree(decl, idName, baseNode);
					if (declType instanceof FunctionType) {
						if (!usedFuncNames.contains(idName)) {
							ElementsOfTranslation elemOfTranslation = Misc.getEnclosingNode(extDecl,
									ElementsOfTranslation.class);
							System.err.print(idName + "(); ");
							baseNode.getF0().removeNode(elemOfTranslation);
							deletedAny = true;
						}
					}
				}
			} else if (choice instanceof FunctionDefinition) {
				FunctionDefinition fd = (FunctionDefinition) choice;
				String fName = fd.getInfo().getFunctionName();
				if (!usedFuncNames.contains(fName)) {
					System.err.print("definition of " + fName + "(); ");
					ElementsOfTranslation elemOfTranslation = Misc.getEnclosingNode(extDecl,
							ElementsOfTranslation.class);
					baseNode.getF0().removeNode(elemOfTranslation);
					Program.getRoot().getInfo().removeFunctionFromList(fd);
					deletedAny = true;
				}
			}
		}
		if (!deletedAny) {
			System.err.println("<none>");
		} else {
			System.err.println();
		}

		// Old Code:
		// CellList calledSymbols = new CellList();
		// for (CallStatement cS :
		// mainFunc.getInfo().getLexicallyEnclosedCallStatements()) {
		// calledSymbols.addAll(cS.getInfo().getCalledSymbols());
		// }
		// if (calledSymbols.isUniversal()) {
		// System.err.println(
		// "Does this program use function pointers? If not, check this line in
		// BasicTransform::removeUnusedFunctions.");
		// }
		// Collection<Symbol> symbolSet = new
		// HashSet<>(baseNode.getInfo().getSymbolTable().values());
		// Set<String> removedSymNames = new HashSet<>();
		// for (Symbol sym : symbolSet) {
		// if (sym.getName().equals("main")) {
		// continue;
		// }
		// if (sym.getDeclaringNode() instanceof FunctionDefinition) {
		// FunctionDefinition funcDef = (FunctionDefinition) sym.getDeclaringNode();
		// if (!calledSymbols.contains(sym)) {
		// // System.err.println("\t Removing function from root: " + sym.getName());
		// ElementsOfTranslation elemOfTranslation = (ElementsOfTranslation)
		// Misc.getEnclosingNode(funcDef,
		// ElementsOfTranslation.class);
		// baseNode.getF0().getNodes().remove(elemOfTranslation);
		// baseNode.getInfo().removeFunctionDefinitionEffects(funcDef);
		// removedSymNames.add(sym.getName());
		// }
		// }
		// }
		//
		//
	}

	/**
	 * Returns a set of all those CFG leaf nodes in the program that are
	 * accessible from {@code main}, if any.
	 * 
	 * @return:
	 *          a set of all the CFG leaf nodes in the program, that are
	 *          accessible from {@code main}, if any. If program contains no
	 *          {@code main()} method, then an empty set is returned.
	 */
	public Set<Node> getAllLeafNodesInTheProgram() {
		FunctionDefinition mainFunc = Program.getRoot().getInfo().getMainFunction();
		if (mainFunc == null) {
			NodeInfo.readWriteDestinationsSet = true;
			return new HashSet<>();
		}
		Set<Node> allNodes = mainFunc.getInfo().getCFGInfo().getIntraTaskCFGLeafContents();
		// System.out.println("Total reachable CFG leaf nodes in the program: " +
		// allNodes.size());
		return allNodes;
	}

	/**
	 * Adds newFunc to the cached list of functions.
	 * 
	 * @param newFunc
	 *                a newly added function.
	 */
	public void addFunctionToList(FunctionDefinition newFunc) {
		this.allFunctionDefinitions.add(newFunc);
	}

	/**
	 * Removes a function from the cached list of functions.
	 * 
	 * @param oldFunc
	 *                an old function to be removed.
	 */
	public void removeFunctionFromList(FunctionDefinition oldFunc) {
		this.allFunctionDefinitions.remove(oldFunc);
	}
}
