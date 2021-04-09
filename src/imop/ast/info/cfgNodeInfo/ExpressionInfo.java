/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info.cfgNodeInfo;

import imop.ast.info.NodeInfo;
import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.BranchEdge;
import imop.lib.analysis.typeSystem.*;
import imop.lib.getter.CellAccessGetter;
import imop.lib.util.CellList;
import imop.lib.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class ExpressionInfo extends NodeInfo {
	private boolean isSVEAnnotated = false;
	private List<BranchEdge> branchEdges = null;

	public ExpressionInfo(Node owner) {
		super(owner);
	}

	/**
	 * For the owner Expression node, this method returns a set of those cells
	 * that are accessed, but for which we do not know whether the access is a
	 * read or a write.
	 * (This situation may occur when the owner node is not yet connected to
	 * a leaf CFG node.)
	 * 
	 * @return
	 */
	public CellList getLocationsOf() {
		return CellAccessGetter.getLocationsOf((Expression) this.getNode());
	}

	public void reinitBranchEdges() {
		this.branchEdges = null;
	}

	public List<BranchEdge> getBranchEdges() {
		if (branchEdges == null || true) {
			branchEdges = new ArrayList<>();
			for (int i = 0; i < this.getCFGInfo().getLeafSuccessors().size(); i++) {
				branchEdges.add(new BranchEdge((Expression) this.getNode(), i));
			}
		}
		return branchEdges;
	}

	public BranchEdge getBranchEdge(int index) {
		List<BranchEdge> bedges = this.getBranchEdges();
		if (index >= bedges.size()) {
			return null;
		}
		return bedges.get(index);
	}

	public BranchEdge getBranchEdge(Node successor) {
		List<BranchEdge> bedges = this.getBranchEdges();
		int succId = -1;
		for (Node succ : this.getCFGInfo().getLeafSuccessors()) {
			succId++;
			if (succ.equals(successor)) {
				return bedges.get(succId);
			}
		}
		assert (false);
		return null;
	}

	public boolean isSVEAnnotated() {
		return isSVEAnnotated;
	}

	public void setSVEAnnotated() {
		this.isSVEAnnotated = true;
	}

	public void resetSVEAnnotated() {
		this.isSVEAnnotated = false;
	}

	public boolean evaluatesToZero() {
		Expression exp = (Expression) getNode();
		Type expType = Type.getType(exp);
		if (expType instanceof ArithmeticType) {
			if (expType instanceof FloatingType) {
				Float retVal = Misc.evaluateFloat(exp);
				if (retVal != null) {
					if (retVal == +0.0f || retVal == -0.0f) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else if (expType instanceof IntegerType) {
				Integer retVal = Misc.evaluateInteger(exp);
				if (retVal != null) {
					if (retVal == 0) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				assert (false);
				return false;
			}
		} else if (expType instanceof PointerType) {
			Integer retVal = Misc.evaluateInteger(exp);
			if (retVal != null) {
				if (retVal == 0) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean evaluatesToConstant() {
		Expression exp = (Expression) getNode();
		Type expType = Type.getType(exp);
		if (expType instanceof ArithmeticType) {
			if (expType instanceof FloatingType) {
				Float retVal = Misc.evaluateFloat(exp);
				if (retVal != null) {
					return true;
				} else {
					return false;
				}
			} else if (expType instanceof IntegerType) {
				Integer retVal = Misc.evaluateInteger(exp);
				if (retVal != null) {
					return true;
				} else {
					Character charValue = Misc.evaluateCharacter(exp);
					if (charValue == null) {
						return false;
					} else {
						return true;
					}
				}
			} else {
				assert (false);
				return false;
			}
			// OLD CODE:
			// } else if (expType instanceof PointerType) {
			// Integer retVal = Misc.evaluateInteger(exp);
			// if (retVal != null) {
			// return true;
			// } else {
			// return false;
			// }
		} else {
			return false;
		}
	}
}
