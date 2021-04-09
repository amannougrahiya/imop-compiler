/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.deprecated;

import imop.ast.node.external.*;

@Deprecated
public class Deprecated_RemoveAnti {
	/**
	 * This method takes a parallel construct, and removes anti-dependences
	 * on shared variables across phases.
	 * TODO: Take care of atomic constructs. The read/write sets can be changed.
	 * 
	 * @param parCons
	 */
	@Deprecated
	public static void removeAntiDependence(ParallelConstruct parCons) {
		// outerMost: for (Node node :
		// parCons.getInfo().getCFGInfo().getLexicalIntraProceduralCFGContents()) {
		// if (!Misc.isCFGLeafNode(node) || node instanceof BeginNode || node instanceof
		// EndNode) {
		// // Note: Make sure that the notion of ElementaryRWNode has been
		// // removed.
		// continue;
		// }
		// /*
		// * TODO: This first version of the implementation works on the
		// * intra-thread dependences. Make sure that once inter-thread
		// * dependences have been populated, we make the appropriate changes
		// * in this function.
		// */
		// CellMap<Set<Node>> antiDepMap = node.getInfo().getAntiEdgeDestList();
		// nodeSymPair: for (Cell cell : antiDepMap.keySet()) {
		// if (!(cell instanceof Symbol)) {
		// continue;
		// }
		// Symbol sym = (Symbol) cell;
		// if (node.getInfo().getSharingAttribute(sym) != DataSharingAttribute.SHARED) {
		// continue;
		// }
		//
		// // TODO: Skip handling this symbol if it is of an array or
		// // pointer type.
		//
		// // Step 1: Find the set of definitions that need to be renamed.
		// Set<Node> defList = antiDepMap.get(sym);
		// defList.remove(node);
		// Set<Node> deleteList = new HashSet<>();
		// for (Node def : defList) {
		// // Remove def from defList, if it shares a phase with node.
		// if (Misc.setIntersection(new
		// Set<Object>(node.getInfo().getNodePhaseInfo().getPhaseSetCopy()),
		// new Set<Object>(def.getInfo().getNodePhaseInfo().getPhaseSetCopy())).size()
		// != 0) {
		// deleteList.add(def);
		// }
		// }
		// defList.removeAll(deleteList);
		// if (defList.isEmpty()) {
		// continue nodeSymPair; // Process next symbol.
		// }
		//
		// /*
		// * Step 2: Find the set of usages that need to be renamed. If
		// * $node$ is a part of $useList$, we won't be able to remove the
		// * anti-dependence edge, and hence we should skip this update.
		// */
		// Set<Node> useList = new HashSet<>();
		// for (Node def : defList) {
		// for (Node use : def.getInfo().getFlowEdgeDestList().get(sym)) {
		// if (use == node) {
		// continue nodeSymPair;
		// }
		// useList.add(use);
		// }
		// }
		//
		// /*
		// * Step 3: Create a new symbol in the same scope as that of sym,
		// * and of the same type as that of sym.
		// */
		// Declaration newSymbolDecl = Builder.getDeclarationFromSymbol(sym);
		// // Optional To-do: Change this addition of this declaration from
		// // global scope to the scope of sym.
		// Builder.addDeclarationToGlobals(newSymbolDecl);
		// String newSymbolName = Misc.getIdNameList(newSymbolDecl).get(0);
		// Symbol newSymbol = new Symbol(newSymbolName, sym.getType(), newSymbolDecl,
		// Main.root);
		// Main.root.getInfo().getSymbolTable().put(newSymbol.getName(), newSymbol);
		//
		// /*
		// * Step 4: Rename the occurrence of symbol sym with symbol
		// * newSymbol in the writes of DEFLIST.
		// */
		// for (Node def : defList) {
		// // TODO: Instead of calling this accept() on $def$, it
		// // should
		// // be called on the PrimaryExpressions() that constitute the
		// // reads(/writes) of def.
		// def.accept(new IdentifierRenamer(sym, newSymbol));
		// }
		//
		// /*
		// * Step 5: Rename the occurrence of symbol sym with symbol
		// * newSymbol in the reads of USELIST.
		// */
		// for (Node use : useList) {
		// // Step 1: Get a list of all the definitions which have not
		// // been renamed.
		// Set<Node> notRenamedDefSet =
		// Misc.setMinus(use.getInfo().getFlowEdgeSrcList().get(sym),
		// defList);
		// use.accept(new IdentifierRenamer(sym, newSymbol));
		// for (Node unrenamedDef : notRenamedDefSet) {
		// /*
		// * Here, we need to insert (newSymbol = sym;) at
		// * appropriate location for those reaching definitions
		// * which are not present in defList.
		// */
		// // Step A: Create the replacement statement
		// Statement replacementStmt = FrontEnd.parseAlone("{" +
		// unrenamedDef.getInfo().getString() + " "
		// + newSymbol.getName() + " = " + sym.getName() + ";}", Statement.class);
		//
		// // BasicTransform.crudeReplaceBaseWithNew(unrenamedDef,
		// // replacementStmt);
		// // Main.setupRoot();
		// // break outerMost;
		// }
		// // Simply rename sym to newSymbol in use.
		// // TODO: Instead of calling this accept() on $def$, it
		// // should be called on the PrimaryExpressions() that
		// // constitute the reads(/writes) of def.
		// }
		// }
		// }
		// // Remove extra braces, if needed
		// Misc.normalizeNode(parCons);
	}
}
