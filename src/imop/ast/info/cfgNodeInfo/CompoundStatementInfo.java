/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.annotation.CaseLabel;
import imop.ast.annotation.DefaultLabel;
import imop.ast.annotation.Label;
import imop.ast.annotation.SimpleLabel;
import imop.ast.info.DataSharingAttribute;
import imop.ast.info.StatementInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.typeSystem.*;
import imop.lib.builder.Builder;
import imop.lib.cfg.info.CompoundStatementCFGInfo;
import imop.lib.getter.UsedCellsGetter;
import imop.lib.transform.updater.NodeReplacer;
import imop.lib.util.CellCollection;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.lib.util.ProfileSS;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompoundStatementInfo extends StatementInfo {

	/**
	 * Set of all the symbols that can be accessed within this
	 * compound-statement.
	 */
	private CellSet allCellsInclusive;

	/**
	 * Set of all the shared cells that can be accessed within this
	 * compound-statement.
	 */
	private CellSet allSharedCellsInclusive;

	/**
	 * Dirty bit for both sets that indicate accessible symbols/cells.
	 */
	private boolean invalidCellSets;

	/**
	 * Invalidate the dirty bit for all the sets that indicate accessible
	 * symbols/cells.
	 */
	public void invalidateCellSets() {
		this.invalidCellSets = true;
		Program.getRoot().getInfo().invalidateHeapCellSet();
	}

	private void populateSetsWithHeapCells() {
		this.allCellsInclusive.addAll(Program.getRoot().getInfo().getAllHeapCells());
		this.allSharedCellsInclusive.addAll(Program.getRoot().getInfo().getAllHeapCells());
	}

	/**
	 * Set all sets that indicate symbols/cells.
	 */
	private void setAllCellSets() {
		this.allCellsInclusive = new CellSet();
		this.allSharedCellsInclusive = new CellSet();

		CompoundStatement thisScope = (CompoundStatement) this.getNode();
		for (Symbol sym : thisScope.getInfo().getSymbolTable().values()) {
			if (sym.isAVariable()) {
				this.allCellsInclusive.add(sym);
				this.allCellsInclusive.add(sym.getAddressCell());
				if (sym.getType() instanceof ArrayType) {
					this.allCellsInclusive.add(sym.getFieldCell());
				}
				if (this.getSharingAttribute(sym) == DataSharingAttribute.SHARED) {
					this.allSharedCellsInclusive.add(sym);
					this.allSharedCellsInclusive.add(sym.getAddressCell());
					if (sym.getType() instanceof ArrayType) {
						this.allSharedCellsInclusive.add(sym.getFieldCell());
					}
				}
			}
		}

		Node encloser = (Node) Misc.getEnclosingBlock(thisScope);
		if (encloser != null) {
			CellSet allCellsInEncloser = encloser.getInfo().getAllCellsAtNode();
			this.allCellsInclusive.addAll(allCellsInEncloser);
			allCellsInEncloser.applyAllExpanded(cell -> {
				if (cell instanceof Symbol) {
					Symbol sym = (Symbol) cell;
					if (sym.isAVariable()) {
						if (this.getSharingAttribute(sym) == DataSharingAttribute.SHARED) {
							this.allSharedCellsInclusive.add(sym);
							this.allSharedCellsInclusive.add(sym.getAddressCell());
							if (sym.getType() instanceof ArrayType) {
								this.allSharedCellsInclusive.add(sym.getFieldCell());
							}
						}
					}
				}
			});
			// this.allSharedCellsInclusive.addAll(encloser.getInfo().getSharedCellsAtNode());
		}
		this.populateSetsWithHeapCells();
		invalidCellSets = false;

		// Node scopeChoice = getNode();
		// while (scopeChoice != null) {
		// if (scopeChoice instanceof TranslationUnit) {
		// for (Symbol sym : ((RootInfo)
		// scopeChoice.getInfo()).getSymbolTable().values()) {
		// if (sym.isAVariable()) {
		// allCellsInclusive.add(sym);
		// if (this.getSharingAttribute(sym) == DataSharingAttribute.SHARED) {
		// allSharedCellsInclusive.add(sym);
		// }
		// }
		// }
		// } else if (scopeChoice instanceof FunctionDefinition) {
		// for (Symbol sym : ((FunctionDefinitionInfo)
		// scopeChoice.getInfo()).getSymbolTable().values()) {
		// if (sym.isAVariable()) {
		// allCellsInclusive.add(sym);
		// if (this.getSharingAttribute(sym) == DataSharingAttribute.SHARED) {
		// allSharedCellsInclusive.add(sym);
		// }
		// }
		// }
		// } else if (scopeChoice instanceof CompoundStatement) {
		// for (Symbol sym : ((CompoundStatementInfo)
		// scopeChoice.getInfo()).getSymbolTable().values()) {
		// if (sym.isAVariable()) {
		// allCellsInclusive.add(sym);
		// if (this.getSharingAttribute(sym) == DataSharingAttribute.SHARED) {
		// allSharedCellsInclusive.add(sym);
		// }
		// }
		// }
		// }
		// scopeChoice = (Node) Misc.getEnclosingBlock(scopeChoice);
		// }

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
	 * Obtain a set of all those cells that can be accessed everywhere within
	 * this node
	 * (after their declarations).
	 */
	@Override
	public CellSet getSharedCellsAtNode() {
		if (allSharedCellsInclusive == null || invalidCellSets) {
			this.setAllCellSets();
		}
		return this.allSharedCellsInclusive;
	}

	public CompoundStatementInfo(CompoundStatement owner) {
		super(owner);
		// this.populateSymbolTable();
	}

	@Override
	public List<Typedef> getAllTypedefListAtNode() {
		List<Typedef> typedefList = new ArrayList<>();
		typedefList.addAll(this.getTypedefTable().values());
		Node encloser = (Node) Misc.getEnclosingBlock(this.getNode());
		if (encloser != null) {
			typedefList.addAll(encloser.getInfo().getAllTypedefListAtNode());
		}
		return typedefList;
	}

	@Override
	public List<Type> getAllTypeListAtNode() {
		List<Type> typeList = new ArrayList<>();
		typeList.addAll(this.getTypeTable().values());
		Node encloser = (Node) Misc.getEnclosingBlock(this.getNode());
		if (encloser != null) {
			typeList.addAll(encloser.getInfo().getAllTypeListAtNode());
		}
		return typeList;
	}

	@Override
	public CompoundStatementCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new CompoundStatementCFGInfo(getNode());
		}
		return (CompoundStatementCFGInfo) cfgInfo;
	}

	private HashMap<String, Symbol> symbolTable;
	private HashMap<String, Typedef> typedefTable;
	private HashMap<String, Type> typeTable;

	/**
	 * This method populates all the symbol and type tables of
	 * the owner CompoundStatement "node" of this info.
	 * <p>
	 * Various structures that get reinitialized and populated are:
	 * <ol>
	 * <li>Symbol Table.</li>
	 * <li>Typedef Table.</li>
	 * <li>Type Table.</li>
	 * </ol>
	 */
	private void populateSymbolTable() {
		// Recreate all the maps/sets.
		this.symbolTable = new HashMap<>();
		this.typedefTable = new HashMap<>();
		this.typeTable = new HashMap<>();

		// Find a list of Declarations made at the level of this CompoundStatement
		CompoundStatement cS = (CompoundStatement) getNode();
		for (Node elementChoice : cS.getF1().getNodes()) {
			Node declChoice = ((CompoundStatementElement) elementChoice).getF0().getChoice();
			if (declChoice instanceof Declaration) {
				Declaration declaration = (Declaration) declChoice;
				addDeclarationToSymbolOrTypeTable(declaration);
			}
		}
	}

	public void addDeclarationToSymbolOrTypeTable(Declaration declaration) {
		List<String> declaratorNameList = declaration.getInfo().getIDNameList();
		assert (declaratorNameList != null);
		if (declaratorNameList.isEmpty()) {
			// This may just be a definition of certain type, without use of that type.
			// In this case, we simply visit the declaration, so as to collect the defined
			// type.
			Type.getTypeTree(declaration, (CompoundStatement) this.getNode());
			return;
		}

		for (String declaratorName : declaratorNameList) {
			Type declaratorType = Type.getTypeTree(declaration, declaratorName, (CompoundStatement) this.getNode());
			if (declaration.getInfo().isTypedef()) {
				/*
				 * If this declaration is a Typedef.
				 */
				if (typedefTable.containsKey(declaratorName)) {
					continue;
				}
				Typedef newTypedef = new Typedef(declaratorName, declaratorType, declaration,
						(CompoundStatement) this.getNode());
				this.typedefTable.put(declaratorName, newTypedef);
			} else {
				/*
				 * If this declaration defines certain objects
				 * (symbols).
				 */
				if (symbolTable.containsKey(declaratorName)) {
					continue;
				}
				Symbol newSymbol = null;
				for (Cell c : Cell.deletedCells) {
					if (c instanceof Symbol) {
						Symbol sym = (Symbol) c;
						String oldName = sym.getName();
						Node oldDeclarationdNode = sym.getDeclaringNode();
						if (oldDeclarationdNode instanceof Declaration) {
							String newName = declaration.getInfo().getDeclaredName();
							// If the name of some deleted symbol matches the one declared in this
							// declaration, ...
							if (oldName.equals(newName)) {
								Scopeable oldScope = sym.getDefiningScope();
								if (oldScope instanceof CompoundStatement) {
									CompoundStatement oldCS = (CompoundStatement) oldScope;
									CompoundStatement newCS = ((CompoundStatement) this.getNode());
									// .. and if the scope of one is the ancestor of the other in the scope tree,
									// ...
									if (newCS.getInfo().getLexicallyEnclosedScopesInclusive().contains(oldCS)
											|| oldCS.getInfo().getLexicallyEnclosedScopesInclusive().contains(newCS)) {
										// .. then, reuse the old symbol, by changing its Declaration and Scope.
										sym.setDeclaringNode(declaration);
										sym.setDefiningScope(newCS);
										newSymbol = sym;
									}
								}
							}
						}
					}
				}
				// If the required symbol is not present in deletedCells, we should obtain a new
				// symbol and proceed.
				if (newSymbol == null) {
					newSymbol = new Symbol(declaratorName, declaratorType, declaration,
							(CompoundStatement) this.getNode());
				}
				this.symbolTable.put(declaratorName, newSymbol);
			}
		}
	}

	public void removeDeclarationFromSymbolOrTypeTable(Declaration declaration) {
		List<String> declaratorNameList = declaration.getInfo().getIDNameList();
		assert (declaratorNameList != null);
		if (declaratorNameList.isEmpty()) {
			// This may just be a definition of certain type, without use of that type.
			// In this case, we simply visit the declaration, so as to collect the defined
			// type.
			Type t = Type.getTypeTree(declaration.getF0(), (CompoundStatement) this.getNode());
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
		}

		for (String declaratorName : declaratorNameList) {
			if (declaration.getInfo().isTypedef()) {
				this.typedefTable.remove(declaratorName);
			} else {
				Symbol sym = this.symbolTable.get(declaratorName);
				if (sym != null) {
					CellCollection.removeCellFromDerivedCollections(sym);
				}
				this.symbolTable.remove(declaratorName);
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

	public HashMap<String, Type> getTypeTable() {
		ProfileSS.addRelevantChangePoint(ProfileSS.symSet);
		if (typeTable == null) {
			populateSymbolTable();
		}
		return typeTable;
	}

	/**
	 * Obtain a copy of the owner compound statement which can be used during
	 * loop unrolling. In this copy,
	 * <ul>
	 * <li>no declarations are included, and</li>
	 * <li>all internal labels are renamed.</li>
	 * <li></li>
	 * </ul>
	 * 
	 * @return
	 */
	public Statement getCopyForUnrolling() {
		// Step 1: Obtain a copy that does not contain any declarations at the top-most
		// level.
		Object[] stmtList = this.getCFGInfo().getElementList().stream().filter((n) -> (!(n instanceof Declaration)))
				.toArray();
		String newBodyStr = "{";
		for (Object stmtObj : stmtList) {
			Statement stmt = (Statement) stmtObj;
			newBodyStr += stmt;
		}
		newBodyStr += "}";
		Statement retCopyStmt = FrontEnd.parseAndNormalize(newBodyStr, Statement.class);

		// Step 2: Obtain a mapping from old label names to new label names, for all
		// nodes
		// inside the newly created copy.
		HashMap<String, String> labelMap = new HashMap<>();
		for (Node cfgNode : retCopyStmt.getInfo().getCFGInfo().getLexicalCFGContents()) {
			if (!(cfgNode instanceof Statement)) {
				continue;
			}
			Statement internalStmt = (Statement) cfgNode;
			for (Label l : internalStmt.getInfo().getLabelAnnotations()) {
				if (l instanceof DefaultLabel || l instanceof CaseLabel) {
					continue;
				}
				labelMap.put(((SimpleLabel) l).getLabelName(), Builder.getNewTempName() + "_label");
			}
		}

		// Step 3: Make sure that all the GotoStatement's now refer to the new label
		// names instead of old.
		for (GotoStatement gotoStmt : Misc.getInheritedEnclosee(retCopyStmt, GotoStatement.class)) {
			if (labelMap.containsKey(gotoStmt.getInfo().getLabelName())) {
				GotoStatement newGotoStmt = FrontEnd.parseAndNormalize(
						"goto " + labelMap.get(gotoStmt.getInfo().getLabelName()) + "; ", GotoStatement.class);
				NodeReplacer.replaceNodes(gotoStmt, newGotoStmt);
			}
		}

		// Step 4: Ensure that all the statements are not annotated with new label names
		// instead of old.
		for (Node element : retCopyStmt.getInfo().getCFGInfo().getLexicalCFGContents()) {
			if (!(element instanceof Statement)) {
				continue;
			}
			Statement internalStmt = (Statement) element;
			List<Label> labelsCopy = new ArrayList<>(internalStmt.getInfo().getLabelAnnotations());
			for (Label l : labelsCopy) {
				if (l instanceof DefaultLabel || l instanceof CaseLabel) {
					continue;
				}
				int labelIndex = internalStmt.getInfo().getLabelAnnotations().indexOf(l);
				internalStmt.getInfo().removeLabelAnnotation(l);
				internalStmt.getInfo().addLabelAnnotation(labelIndex,
						new SimpleLabel(internalStmt, labelMap.get(((SimpleLabel) l).getLabelName())));
			}
		}

		return retCopyStmt;
	}

	public CellSet getUsedCellsEnding(int i) {
		return getUsedCellsBetween(0, i);
	}

	public CellSet getUsedCellsStarting(int i) {
		int size = this.getCFGInfo().getElementList().size();
		if (i >= size) {
			return new CellSet();
		}
		return getUsedCellsBetween(i, size - 1);
	}

	public CellSet getUsedCellsBetween(int startIndex, int endIndex) {
		if (!(startIndex > 0 && endIndex >= startIndex)) {
			return new CellSet();
		}
		List<Node> elements = this.getCFGInfo().getElementList();
		int size = elements.size();
		if (startIndex >= size || endIndex >= size) {
			return new CellSet();
		}
		CellSet accesses = new CellSet();
		for (int i = startIndex; i <= endIndex; i++) {
			Node element = elements.get(i);
			accesses.addAll(UsedCellsGetter.getUsedCells(element));
		}
		return accesses;
	}

	public CellSet getAccessesEnding(int i) {
		return getAccessesBetween(0, i);
	}

	public CellSet getSymbolAccessesEnding(int i) {
		return getSymbolAccessesBetween(0, i);
	}

	public CellSet getAccessesStarting(int i) {
		int size = this.getCFGInfo().getElementList().size();
		if (i >= size) {
			return new CellSet();
		}
		return getAccessesBetween(i, size - 1);
	}

	public CellSet getSymbolAccessesStarting(int i) {
		int size = this.getCFGInfo().getElementList().size();
		if (i >= size) {
			return new CellSet();
		}
		return getSymbolAccessesBetween(i, size - 1);
	}

	public CellSet getSymbolAccessesBetween(int startIndex, int endIndex) {
		if (!(startIndex > 0 && endIndex >= startIndex)) {
			return new CellSet();
		}
		List<Node> elements = this.getCFGInfo().getElementList();
		int size = elements.size();
		if (startIndex >= size || endIndex >= size) {
			return new CellSet();
		}
		CellSet accesses = new CellSet();
		for (int i = startIndex; i <= endIndex; i++) {
			Node element = elements.get(i);
			accesses.addAll(element.getInfo().getSymbolAccesses());
		}
		return accesses;
	}

	public CellSet getAccessesBetween(int startIndex, int endIndex) {
		if (!(startIndex > 0 && endIndex >= startIndex)) {
			return new CellSet();
		}
		List<Node> elements = this.getCFGInfo().getElementList();
		int size = elements.size();
		if (startIndex >= size || endIndex >= size) {
			return new CellSet();
		}
		CellSet accesses = new CellSet();
		for (int i = startIndex; i <= endIndex; i++) {
			Node element = elements.get(i);
			accesses.addAll(element.getInfo().getAccesses());
		}
		return accesses;
	}

	public CellSet getWritesEnding(int i) {
		return getWritesBetween(0, i);
	}

	public CellSet getWritesStarting(int i) {
		int size = this.getCFGInfo().getElementList().size();
		if (i >= size) {
			return new CellSet();
		}
		return getWritesBetween(i, size - 1);
	}

	public CellSet getWritesBetween(int startIndex, int endIndex) {
		if (!(startIndex > 0 && endIndex >= startIndex)) {
			return new CellSet();
		}
		List<Node> elements = this.getCFGInfo().getElementList();
		int size = elements.size();
		if (startIndex >= size || endIndex >= size) {
			return new CellSet();
		}
		CellSet writes = new CellSet();
		for (int i = startIndex; i <= endIndex; i++) {
			Node element = elements.get(i);
			writes.addAll(element.getInfo().getWrites());
		}
		return writes;
	}

	public CellSet getReadsEnding(int i) {
		return getReadsBetween(0, i);
	}

	public CellSet getReadsStarting(int i) {
		int size = this.getCFGInfo().getElementList().size();
		if (i >= size) {
			return new CellSet();
		}
		return getReadsBetween(i, size - 1);
	}

	public CellSet getReadsBetween(int startIndex, int endIndex) {
		if (!(startIndex > 0 && endIndex >= startIndex)) {
			return new CellSet();
		}
		List<Node> elements = this.getCFGInfo().getElementList();
		int size = elements.size();
		if (startIndex >= size || endIndex >= size) {
			return new CellSet();
		}
		CellSet reads = new CellSet();
		for (int i = startIndex; i <= endIndex; i++) {
			Node element = elements.get(i);
			reads.addAll(element.getInfo().getReads());
		}
		return reads;
	}

	@Deprecated
	private void addDeclaration(Declaration declaration) {
		List<String> declaratorNameList = declaration.getInfo().getIDNameList();
		assert (declaratorNameList != null);
		if (declaratorNameList.isEmpty()) {
			// This may just be a definition of certain type, without use of that type.
			// In this case, we simply visit the declaration, so as to collect the defined
			// type.
			Type.getTypeTree(declaration, (CompoundStatement) this.getNode());
		}

		for (String declaratorName : declaratorNameList) {
			Type declaratorType = Type.getTypeTree(declaration, declaratorName, (CompoundStatement) this.getNode());
			if (declaration.getInfo().isTypedef()) {
				/*
				 * If this declaration is a Typedef.
				 */
				if (typedefTable.containsKey(declaratorName)) {
					continue;
				}
				Typedef newTypedef = new Typedef(declaratorName, declaratorType, declaration,
						(CompoundStatement) this.getNode());
				this.typedefTable.put(declaratorName, newTypedef);
			} else {
				/*
				 * If this declaration defines certain objects
				 * (symbols).
				 */
				if (symbolTable.containsKey(declaratorName)) {
					continue;
				}
				Symbol newSymbol = new Symbol(declaratorName, declaratorType, declaration,
						(CompoundStatement) this.getNode());
				this.symbolTable.put(declaratorName, newSymbol);
			}
		}
	}

	@Deprecated
	private void removeDeclaration(Declaration declaration) {
		List<String> declaratorNameList = declaration.getInfo().getIDNameList();
		assert (declaratorNameList != null);
		if (declaratorNameList.isEmpty()) {
			// This may just be a definition of certain type, without use of that type.
			// In this case, we simply visit the declaration, so as to collect the defined
			// type.
			Type.getTypeTree(declaration, (CompoundStatement) this.getNode(), true);
		}

		for (String declaratorName : declaratorNameList) {
			if (declaration.getInfo().isTypedef()) {
				/*
				 * If this declaration is a Typedef.
				 */
				if (typedefTable.containsKey(declaratorName)) {
					typedefTable.remove(declaratorName);
				}
			} else {
				/*
				 * If this declaration defines certain objects
				 * (symbols).
				 */
				if (symbolTable.containsKey(declaratorName)) {
					symbolTable.remove(declaratorName);
				}
			}
		}
	}

}
