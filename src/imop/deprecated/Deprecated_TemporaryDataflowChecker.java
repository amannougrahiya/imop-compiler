/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.deprecated;

import imop.baseVisitor.DepthFirstProcess;

/**
 * This temporary class dumps the line numbers of all the definitions reachable
 * at various leaf CFG nodes.
 */
@Deprecated
public class Deprecated_TemporaryDataflowChecker extends DepthFirstProcess {
	// @Override
	// public void initProcess(Node n) {
	// if (Misc.isCFGLeafNode(n)) {
	// System.out.print(Misc.getLineNum(n) + ": ");
	// n.getInfo().printNode();
	// System.out.print("{");
	// if (n.getInfo().readReachingDefinitions() != null) {
	// for (Definition def : n.getInfo().readReachingDefinitions()) {
	// System.out.print(Misc.getLineNum(def.getDefiningNode()) + ", ");
	// }
	// }
	// System.out.print("} usesInDU: {");
	// CellMap<Set<Node>> usesInDU = n.getInfo().readUsesInDU();
	// if (usesInDU != null) {
	// for (Cell key : usesInDU.keySet()) {
	// if (key instanceof Symbol) {
	// System.out.print("(" + ((Symbol) key).getName() + ", {");
	// for (Node useNode : usesInDU.get(key)) {
	// System.out.print(Misc.getLineNum(useNode) + ", ");
	// }
	// System.out.print("}), ");
	// }
	// }
	// }
	// System.out.print("} defsInUD: {");
	// CellMap<Set<Node>> defsInUD = n.getInfo().readDefsInUD();
	// if (defsInUD != null) {
	// for (Cell key : defsInUD.keySet()) {
	// if (key instanceof Symbol) {
	// System.out.print("(" + ((Symbol) key).getName() + ", {");
	// for (Node defNode : defsInUD.get(key)) {
	// System.out.print(Misc.getLineNum(defNode) + ", ");
	// }
	// System.out.print("}), ");
	// }
	// }
	// }
	// System.out.print("} liveSymbols: {");
	// for (Cell cell : n.getInfo().getLiveOutCells()) {
	// if (cell instanceof Symbol) {
	// System.out.print(((Symbol) cell).getName() + ", ");
	// }
	// }
	//
	// System.out.print("} flowsTo: {");
	// CellMap<Set<Node>> flowDestList = n.getInfo().readFlowEdgeDestList();
	// if (flowDestList != null) {
	// for (Cell key : flowDestList.keySet()) {
	// if (key instanceof Symbol) {
	// System.out.print("(" + ((Symbol) key).getName() + ", {");
	// for (Node defNode : flowDestList.get(key)) {
	// System.out.print(Misc.getLineNum(defNode) + ", ");
	// }
	// System.out.print("}), ");
	// }
	// }
	// }
	//
	// System.out.print("} outputsTo: {");
	// CellMap<Set<Node>> outputDestList = n.getInfo().readOutputEdgeDestList();
	// if (outputDestList != null) {
	// for (Cell key : outputDestList.keySet()) {
	// if (key instanceof Symbol) {
	// System.out.print("(" + ((Symbol) key).getName() + ", {");
	// for (Node defNode : outputDestList.get(key)) {
	// System.out.print(Misc.getLineNum(defNode) + ", ");
	// }
	// System.out.print("}), ");
	// }
	// }
	// }
	//
	// System.out.print("} isAntiTo: {");
	// CellMap<Set<Node>> antiDestList = n.getInfo().readAntiEdgeDestList();
	// if (antiDestList != null) {
	// for (Cell key : antiDestList.keySet()) {
	// if (key instanceof Symbol) {
	// System.out.print("(" + ((Symbol) key).getName() + ", {");
	// for (Node defNode : antiDestList.get(key)) {
	// System.out.print(Misc.getLineNum(defNode) + ", ");
	// }
	// System.out.print("}), ");
	// }
	// }
	// }
	// System.out.println("} \n\n");
	// }
	// return;
	// }
}
