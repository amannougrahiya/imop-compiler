/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.StatementInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.FreeVariable;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.mhp.lock.RoutineLock;
import imop.lib.analysis.typeSystem.FunctionType;
import imop.lib.analysis.typeSystem.PointerType;
import imop.lib.cfg.info.CallStatementCFGInfo;
import imop.lib.util.CellList;
import imop.lib.util.ImmutableCellSet;
import imop.lib.util.Misc;
import imop.lib.util.ProfileSS;

import java.util.ArrayList;
import java.util.List;

public class CallStatementInfo extends StatementInfo {
	/**
	 * Reference to the (constant) cell that represents the associated function
	 * designator.
	 */
	Cell functionDesignatorCell = null;
	List<FunctionDefinition> calledDefinitions = null;
	CellList calledFunctions = null;

	public CallStatementInfo(Node owner) {
		super(owner);
	}

	@Override
	public CallStatementCFGInfo getCFGInfo() {
		if (cfgInfo == null) {
			this.cfgInfo = new CallStatementCFGInfo(getNode());
		}
		return (CallStatementCFGInfo) cfgInfo;
	}

	public boolean isACallByFunctionPointer() {
		Cell functionDesignator = this.getFunctionDesignator();
		if (functionDesignator == Cell.genericCell) {
			return true;
		}
		Symbol functionDesignatorSymbol = (Symbol) functionDesignator;
		if (functionDesignatorSymbol == null) {
			return false;
		}
		if (functionDesignatorSymbol.getType() instanceof PointerType) {
			assert (((PointerType) functionDesignatorSymbol.getType()).getPointeeType() instanceof FunctionType);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the symbol for the function-designator at this
	 * call-statement.<br>
	 * Note that this symbol could be a direct call, or a function-pointer.
	 * 
	 * @return
	 */
	public Cell getFunctionDesignator() {
		if (this.functionDesignatorCell == null) {
			CallStatement callStmt = (CallStatement) this.getNode();
			Cell sym = Misc.getSymbolOrFreeEntry(callStmt.getFunctionDesignatorNode());
			if (sym instanceof FreeVariable) {
				Node encloser = callStmt.getFunctionDesignatorNode().getInfo().getOuterMostNonLeafEncloser();
				if (!(encloser instanceof FunctionDefinition)) {
					System.err.println("Cannot find any parent for " + callStmt.getFunctionDesignatorNode());
				} else {
					Misc.warnDueToLackOfFeature(
							"Could not find any declaration for " + callStmt.getFunctionDesignatorNode() + ".",
							this.getNode());
				}
				this.functionDesignatorCell = null;
			} else {
				this.functionDesignatorCell = sym;
			}
		}
		return this.functionDesignatorCell;
	}

	/**
	 * Returns true if we deterministically know the called function symbol at
	 * this
	 * call-statement.
	 * 
	 * @return
	 */
	public boolean hasKnownCalledFunctionSymbol() {
		if (this.getCalledSymbols().size() == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the set of FunctionDefinitions that
	 * this call-statement may potentially target.
	 * 
	 * @return
	 */
	public List<FunctionDefinition> getCalledDefinitions() {
		ProfileSS.addRelevantChangePoint(ProfileSS.cgSet);
		if (this.calledDefinitions == null) {
			this.calledDefinitions = new ArrayList<>();
			if (this.isACallByFunctionPointer()) {
				Cell funcDesig = this.getFunctionDesignator();
				if (funcDesig != null) {
					ImmutableCellSet set = funcDesig.getPointsTo(this.getNode());
					set.applyAllExpanded((cell) -> {
						if (cell instanceof Symbol) {
							Symbol sym = (Symbol) cell;
							if (sym.getDeclaringNode() instanceof FunctionDefinition) {
								calledDefinitions.add((FunctionDefinition) sym.getDeclaringNode());
							}
						}
					});
				}
			} else {
				Cell cell = this.getFunctionDesignator();
				if (cell != null) {
					if (cell == Cell.genericCell) {
						for (Cell c : Cell.allCells) {
							if (c instanceof Symbol) {
								Symbol sym = (Symbol) c;
								if (sym.getDeclaringNode() instanceof FunctionDefinition) {
									calledDefinitions.add((FunctionDefinition) sym.getDeclaringNode());
								}
							}
						}
					} else {
						Symbol sym = (Symbol) cell;
						if (sym.getDeclaringNode() instanceof FunctionDefinition) {
							calledDefinitions.add((FunctionDefinition) sym.getDeclaringNode());
						}
					}
				}
			}
		}
		return this.calledDefinitions;
	}

	/**
	 * Returns the set of function-designator symbols that may
	 * be the potential called functions of this call-statement.
	 * 
	 * @return
	 */
	public CellList getCalledSymbols() {
		ProfileSS.addRelevantChangePoint(ProfileSS.cgSet);
		if (this.calledFunctions == null) {
			this.calledFunctions = new CellList();
			Cell funcDesig = this.getFunctionDesignator();
			if (funcDesig != null) {
				if (!this.isACallByFunctionPointer()) {
					calledFunctions.add(funcDesig);
				} else {
					funcDesig.getPointsTo(this.getNode()).applyAllExpanded((cell) -> {
						if (cell instanceof Symbol && ((Symbol) cell).isAFunction()) {
							calledFunctions.add(cell);
						}
					});
				}
			}
		}
		return this.calledFunctions;
	}

	/**
	 * Returns a copy of the ordered list of simple-primary expressions,
	 * that represent the various arguments passed at this call-statement.
	 * 
	 * @return
	 */
	public List<SimplePrimaryExpression> getArgumentList() {
		return new ArrayList<>(((CallStatement) this.getNode()).getPreCallNode().getArgumentList());
	}

	public boolean isALockSettingRoutine() {
		CallStatement lockingRoutine = (CallStatement) this.getNode();
		CellList calledRoutines = lockingRoutine.getInfo().getCalledSymbols();
		if (calledRoutines.size() != 1) {
			return false;
		}
		Cell calledCell = calledRoutines.get(0);
		if (calledCell == Cell.genericCell) {
			return true;
		}
		Symbol calledSymbol = (Symbol) calledCell;
		if (calledSymbol == null) {
			return false;
		}
		if (RoutineLock.lockSettingRoutines.contains(calledSymbol.getName())) {
			return true;
		}
		return false;
	}

	public boolean isALockWriteRoutine() {
		CallStatement lockingRoutine = (CallStatement) this.getNode();
		CellList calledRoutines = lockingRoutine.getInfo().getCalledSymbols();
		if (calledRoutines.size() != 1) {
			return false;
		}
		Cell calledCell = calledRoutines.get(0);
		if (calledCell == Cell.genericCell) {
			return true;
		}
		Symbol calledSymbol = (Symbol) calledCell;
		if (calledSymbol == null) {
			return false;
		}
		if (RoutineLock.lockWriteRoutines.contains(calledSymbol.getName())) {
			return true;
		}
		return false;
	}

	public boolean isALockModifyRoutine() {
		CallStatement lockingRoutine = (CallStatement) this.getNode();
		CellList calledRoutines = lockingRoutine.getInfo().getCalledSymbols();
		if (calledRoutines.size() != 1) {
			return false;
		}
		Cell calledCell = calledRoutines.get(0);
		if (calledCell == Cell.genericCell) {
			return true;
		}
		Symbol calledSymbol = (Symbol) calledCell;
		if (calledSymbol == null) {
			return false;
		}
		if (RoutineLock.lockModifyRoutines.contains(calledSymbol.getName())) {
			return true;
		}
		return false;
	}

	public boolean isALockReleasingRoutine() {
		CallStatement lockingRoutine = (CallStatement) this.getNode();
		CellList calledRoutines = lockingRoutine.getInfo().getCalledSymbols();
		if (calledRoutines.size() != 1) {
			return false;
		}
		Cell calledCell = calledRoutines.get(0);
		if (calledCell == Cell.genericCell) {
			return true;
		}
		Symbol calledSymbol = (Symbol) calledCell;
		if (calledSymbol == null) {
			return false;
		}
		if (RoutineLock.lockReleasingRoutines.contains(calledSymbol.getName())) {
			return true;
		}
		return false;
	}

	public void printCode() {
		System.err.println(this);
	}

}
