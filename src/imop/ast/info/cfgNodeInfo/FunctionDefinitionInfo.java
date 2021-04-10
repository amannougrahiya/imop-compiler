/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.annotation.Label;
import imop.ast.annotation.SimpleLabel;
import imop.ast.info.DataSharingAttribute;
import imop.ast.info.NodeInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.typeSystem.*;
import imop.lib.analysis.typeSystem.FunctionType.Parameter;
import imop.lib.builder.Builder;
import imop.lib.cfg.info.FunctionDefinitionCFGInfo;
import imop.lib.cg.CallSite;
import imop.lib.getter.InitFunctionName;
import imop.lib.util.CellSet;
import imop.lib.util.Misc;
import imop.lib.util.ProfileSS;
import imop.parser.Program;

import java.util.*;

/**
 * This class holds the information specific to a FunctionDefinition node.
 * 
 * @author aman
 *
 */
public class FunctionDefinitionInfo extends NodeInfo {

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
	 * Invalidate the dirt bit for all the sets that indicate accessible
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

		FunctionDefinition scopeChoice = (FunctionDefinition) this.getNode();
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
		Node encloser = (Node) Misc.getEnclosingBlock(scopeChoice);
		if (encloser != null) {
			this.allCellsInclusive.addAll(encloser.getInfo().getAllCellsAtNode());
			this.allSharedCellsInclusive.addAll(encloser.getInfo().getSharedCellsAtNode());
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

	@Override
	public List<Typedef> getAllTypedefListAtNode() {
		Node encloser = (Node) Misc.getEnclosingBlock(this.getNode());
		if (encloser != null) {
			return encloser.getInfo().getAllTypedefListAtNode();
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public List<Type> getAllTypeListAtNode() {
		Node encloser = (Node) Misc.getEnclosingBlock(this.getNode());
		if (encloser != null) {
			return encloser.getInfo().getAllTypeListAtNode();
		} else {
			return new ArrayList<>();
		}
	}

	public List<SimpleLabel> getAllSimpleLabelsOfFunction() {
		List<SimpleLabel> allLabels = new ArrayList<>();
		for (Node cfgContent : this.getCFGInfo().getLexicalCFGContents()) {
			if (cfgContent instanceof Statement) {
				Statement stmt = (Statement) cfgContent;
				for (Label label : stmt.getInfo().getLabelAnnotations()) {
					if (label instanceof SimpleLabel) {
						allLabels.add((SimpleLabel) label);
					}
				}

			}
		}
		return allLabels;
	}

	public Set<String> getAllSimpleLabelNamesOfFunction() {
		Set<String> allLabelNames = new HashSet<>();
		for (SimpleLabel label : this.getAllSimpleLabelsOfFunction()) {
			allLabelNames.add(label.getLabelName());
		}
		return allLabelNames;
	}

	/**
	 * Although rarely used, a function may define structs/unions in its
	 * parameter-list,
	 * and may use the created type in its definition.
	 * Not that a function can't define a typedef in its parameter-list.
	 * Hence we do not need a similar table for typedef's.
	 */
	@Deprecated
	private HashMap<String, Type> typeTable;

	private boolean runInParallel = false; // This flag is set for those function defs which may become a part of a
											// parallel region.
	private String functionName;
	private HashMap<String, Symbol> symbolTable;

	public FunctionDefinitionInfo(FunctionDefinition owner) {
		super(owner);
	}

	@Override
	public FunctionDefinitionCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new FunctionDefinitionCFGInfo(getNode());
		}
		return (FunctionDefinitionCFGInfo) cfgInfo;
	}

	@Deprecated
	public HashMap<String, Type> getTypeTable() {
		if (typeTable == null) {
			populateSymbolTable();
		}
		return typeTable;
	}

	/**
	 * @param onPath:
	 *                a vector of all those functions which have been traversed so
	 *                far.
	 * @return True if this function may be recursive in nature
	 */
	public boolean isRecursive() {
		CompoundStatement body = this.getCFGInfo().getBody();
		if (body.getInfo().getReachableCallGraphNodes().contains(this.getNode())) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unused")
	@Deprecated
	private boolean isRecursive(FunctionDefinition n, List<FunctionDefinition> visited) {
		if (visited.contains(n)) {
			if (n == getNode()) {
				return true;
			} else {
				return false;
			}
		} else {
			visited.add(n);
		}

		for (CallSite cS : n.getInfo().getCallSites()) {
			if (cS.calleeDefinition != null) {
				if (isRecursive(cS.calleeDefinition, visited)) {
					return true;
				}
			}
		}
		return false;
	}

	// /**
	// * Returns true if there is a barrier in this function (or in its called
	// * functions)
	// */
	// @Override
	// public boolean hasBarrierInCFG() {
	// Vector<FunctionDefinition> onPath = new Vector<>();
	// onPath.add((FunctionDefinition) node);
	// return hasBarrierInCFG(onPath);
	// }
	//
	// public boolean hasBarrierInCFG(Vector<FunctionDefinition> onPath) {
	// if (hasBarrierInAST()) {
	// return true;
	// }
	//
	// for (CallSite cS : getCallSites()) {
	// FunctionDefinition fD = cS.calleeDefinition;
	// if (fD == null) {
	// continue;
	// }
	//
	// if (onPath.contains(fD)) {
	// continue; // reached a recursive path
	// }
	//
	// onPath.add(fD);
	// if (fD.getInfo().hasBarrierInCFG(onPath)) {
	// return true;
	// }
	// onPath.remove(fD);
	// }
	// return false;
	// }

	/**
	 * Dumps a list of variables/functions declared in this
	 * CompoundStatementInfo
	 */
	public void dumpSymbolTable() {
		if (symbolTable.isEmpty()) {
			return;
		}
		System.out.println();
		System.out.println("List of symbols declared in the parameters of "
				+ ((FunctionDefinitionInfo) getNode().getInfo()).getFunctionName() + ": ");
		for (Symbol sym : symbolTable.values()) {
			System.out.print(sym.getName() + "\t");
			// sym.type.printType();
			System.out.println();
		}
		System.out.println();
	}

	public HashMap<String, Symbol> getSymbolTable() {
		ProfileSS.addRelevantChangePoint(ProfileSS.symSet);
		if (symbolTable == null) {
			populateSymbolTable();
		}
		return symbolTable;
	}

	/**
	 * This method populates the symbolTable field of
	 * the owner FunctionDefinition "node" of this info,
	 * with the names (and later types) of the various parameters.
	 */
	public void populateSymbolTable() {
		this.symbolTable = new HashMap<>();
		this.typeTable = new HashMap<>();
		assert (Program.getRoot() != null);
		HashMap<String, Symbol> rootSymbolTable = Program.getRoot().getInfo().getSymbolTable();
		Symbol functionSymbol = rootSymbolTable.get(this.getFunctionName());
		if (functionSymbol == null) {
			// TODO: Maybe this is a newly added function. We will handle this later.
			return;
		}
		FunctionType functionType = (FunctionType) functionSymbol.getType();
		for (Parameter param : functionType.getParameterList()) {
			// assert(param.parameterName != null);
			if (param.getParameterName() == null) {
				param.setParameterName(Builder.getNewTempName());
			}
			// System.out.println("Adding a parameter named " + param.parameterName + " in "
			// + functionName);
			symbolTable.put(param.getParameterName(), new Symbol(param.getParameterName(), param.getParameterType(),
					param.getParameterDeclaration(), (FunctionDefinition) this.getNode()));
		}

		// /*
		// * Extract names from Declarator (f1) first
		// */
		// Declarator decl = funcDef.f1;
		// ADeclaratorOp declOp =
		// ((ADeclaratorOp)decl.f1.f1.f0.nodes.get(decl.f1.f1.f0.nodes.size() - 1));
		// Node paramListChoice = declOp.f0.choice;
		// if (paramListChoice instanceof ParameterTypeListClosed){
		// // Case 1: Using new-style parameter list
		// ParameterTypeListClosed paramListClosed = (ParameterTypeListClosed)
		// paramListChoice;
		// if(paramListClosed.f1.node != null) {
		// ParameterList parList= ((ParameterTypeList)paramListClosed.f1.node).f0;
		//
		// // Obtain the name of first argument
		// Node paraDeclChoice = parList.f0.f1.f0.choice;
		// if(paraDeclChoice instanceof Declarator){
		// //System.out.println("Found a parameter: " +
		// (Misc.getRootIdName((Declarator)paraDeclChoice)));
		// String symbolTempName = Misc.getRootIdName((Declarator)paraDeclChoice);
		// symbolTable.put(symbolTempName, new Symbol(symbolTempName, parList.f0,
		// (FunctionDefinition) node));
		// }
		//
		// // Obtain the name of remaining arguments
		// for(Node seq: parList.f1.nodes){
		// assert seq instanceof NodeSequence;
		// paraDeclChoice =
		// ((ParameterDeclaration)((NodeSequence)seq).nodes.get(1)).f1.f0.choice;
		// if(paraDeclChoice instanceof Declarator){
		// //System.out.println("Found a parameter: " +
		// (Misc.getRootIdName((Declarator)paraDeclChoice)));
		// String symbolTempName = Misc.getRootIdName((Declarator)paraDeclChoice);
		// symbolTable.put(symbolTempName, new Symbol(symbolTempName,
		// ((NodeSequence)seq).nodes.get(1), (FunctionDefinition) node));
		// }
		// }
		//
		// }
		// }
		// dumpSymbolTable();
	}

	public String getFunctionName() {
		if (functionName == null) {
			getNode().accept(new InitFunctionName());
		}
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	// /**
	// * Returns a set of all those call-sites in the known part of the program
	// * which may call this function-definition.
	// *
	// * @return
	// * @deprecated Use {@link #getCallersOfThis()} instead
	// */
	// public Set<CallStatement> getCallers() {
	// return getCallersOfThis();
	// }

	/**
	 * Returns a set of all those call-sites in the known part of the program
	 * which may call this function-definition.
	 * 
	 * @return
	 */
	public Set<CallStatement> getCallersOfThis() {
		ProfileSS.addRelevantChangePoint(ProfileSS.cgSet);
		Set<CallStatement> possibleSites = new HashSet<>();
		for (CallStatement callSite : Program.getRoot().getInfo().getLexicallyEnclosedCallStatements()) {
			for (FunctionDefinition funcDef : callSite.getInfo().getCalledDefinitions()) {
				if (funcDef.equals(getNode())) {
					possibleSites.add(callSite);
					break;
				}
			}
		}
		return possibleSites;
	}

	/**
	 * Returns a set of all those call-sites in the known part of the program
	 * which may call this function-definition.
	 * 
	 * @return
	 * @deprecated
	 */
	@Deprecated
	public Set<CallSite> oldGetCallersOf() {
		Set<CallSite> possibleSites = new HashSet<>();
		for (CallSite callSite : Program.getRoot().getInfo().getCallSites()) {
			if (callSite.calleeDefinition != null && callSite.calleeDefinition.equals(getNode())) {
				possibleSites.add(callSite);
			}
		}
		return possibleSites;
	}

	public boolean isRunnableInParallel() {
		return runInParallel;
	}

	public void setRunInParallel(boolean runInParallel) {
		this.runInParallel = runInParallel;
	}

	public void addDeclaration(Declaration declaration) {
		assert (false);
	}

	public void removeDeclaration(Declaration declaration) {
		assert (false);
	}

	public boolean hasAnyRealParams() {
		List<ParameterDeclaration> paramDeclList = this.getCFGInfo().getParameterDeclarationList();
		if (paramDeclList.isEmpty()) {
			return false;
		} else if (paramDeclList.size() == 1) {
			ParameterDeclaration paramDecl = paramDeclList.get(0);
			if (paramDecl.toString().trim().equals("void")) {
				return false;
			}
		}
		return true;
	}

	public Type getReturnType() {
		for (Symbol s : Program.getRoot().getInfo().getSymbolTable().values()) {
			if (s.getName().equals(this.getFunctionName())) {
				if (s.getType() instanceof FunctionType) {
					FunctionType fT = (FunctionType) s.getType();
					return fT.getReturnType();
				}
			}
		}
		Scopeable scope = Misc.getEnclosingBlock(this.getNode());
		FunctionType fT = Type.getTypeTree((FunctionDefinition) this.getNode(), scope);
		if (fT == null) {
			return null;
		} else {
			return fT.getReturnType();
		}
	}

	public CellSet getReturnedLocations() {
		CellSet retSet = new CellSet();
		if (this.getReturnType() instanceof VoidType) {
			return retSet;
		}
		for (ReturnStatement retStmt : Misc.getInheritedEnclosee(this.getNode(), ReturnStatement.class)) {
			if (retStmt.getF1().present()) {
				Expression e = (Expression) retStmt.getF1().getNode();
				retSet.addAll(e.getInfo().getLocationsOf());
			}
		}
		return retSet;
	}

}
