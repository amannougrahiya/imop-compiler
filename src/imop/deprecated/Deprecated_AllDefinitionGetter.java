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
 * This class populates definitionList with all the definitions present in the
 * given node
 */
@Deprecated
public class Deprecated_AllDefinitionGetter extends DepthFirstProcess {
	// public List<Definition> definitionList = new ArrayList<>();
	//
	// /**
	// * This overridden version puts n in the definitionList,
	// * if
	// * a.) n is a leaf CFG node with non-empty list of writes.
	// * b.) n is one of the four non-leaf CFG nodes which may have writes
	// * invisible in their nested components.
	// */
	// @Override
	// public void initProcess(Node n) {
	// if (Misc.isCFGNode(n)) {
	// if (Misc.isCFGLeafNode(n)) {
	// List<Cell> writes = n.getInfo().getWrites();
	// if (!writes.isEmpty()) {
	// for (Cell sym : writes) {
	// if (sym instanceof Symbol) {
	// definitionList.add(new Definition(n, sym));
	// }
	// }
	// }
	// } else {
	// if (n instanceof ParallelForConstruct) {
	// List<Cell> writes = ((ParallelForConstruct) n).getF3().getInfo().getWrites();
	// if (!writes.isEmpty()) {
	// for (Cell sym : writes) {
	// if (sym instanceof Symbol) {
	// definitionList.add(new Definition(n, sym));
	// }
	// }
	// }
	// } else if (n instanceof ParallelSectionsConstruct) {
	// List<Cell> writes = ((ParallelSectionsConstruct)
	// n).getF3().getInfo().getWrites();
	// if (!writes.isEmpty()) {
	// for (Cell sym : writes) {
	// if (sym instanceof Symbol) {
	// definitionList.add(new Definition(n, sym));
	// }
	// }
	// }
	// } else if (n instanceof ParallelConstruct) {
	// List<Cell> writes = ((ParallelConstruct)
	// n).getParConsF1().getInfo().getWrites();
	// if (!writes.isEmpty()) {
	// for (Cell sym : writes) {
	// if (sym instanceof Symbol) {
	// definitionList.add(new Definition(n, sym));
	// }
	// }
	// }
	// } else if (n instanceof TaskConstruct) {
	// List<Cell> writes = ((TaskConstruct) n).getF2().getInfo().getWrites();
	// if (!writes.isEmpty()) {
	// for (Cell sym : writes) {
	// if (sym instanceof Symbol) {
	// definitionList.add(new Definition(n, sym));
	// }
	// }
	// }
	// }
	// }
	// }
	// }
}
