/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.projects.criticalSection;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.builder.Builder;
import imop.lib.transform.BasicTransform;
import imop.lib.transform.simplify.ImplicitBarrierRemover;
import imop.lib.util.CellSet;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

public class CriticalSectionOptimizer {
	private static class Data {
		private static int id = 0;
		public int myId;
		public CriticalConstruct node;
		public CellSet readSet;
		public CellSet writeSet;

		Data(CriticalConstruct node, CellSet readSet, CellSet writeSet) {
			myId = id++;
			this.node = node;
			this.readSet = readSet;
			this.writeSet = writeSet;
		}

		public void printReads() {
			System.err.println("Reads: ");
			for (Cell sym : readSet) {
				if (sym instanceof Symbol) {
					System.err.print(((Symbol) sym).getName() + "; ");
				}
			}
			System.err.println();
		}

		public void printWrites() {
			System.err.println("Writes: ");
			for (Cell sym : writeSet) {
				if (sym instanceof Symbol)
				System.err.print(((Symbol) sym).getName() + "; ");
			}
			System.err.println();
		}
	}

	public static void start() throws IllegalArgumentException, IllegalAccessException {
		TranslationUnit root = FrontEnd.parseAlone(System.in, TranslationUnit.class);

//		root.accept(new ImplicitBarrierRemover());
		root.getInfo().removeExtraScopes();

		root = FrontEnd.parseAlone(root.getInfo().getString(), TranslationUnit.class);

		List<Data> nodeList = new ArrayList<>();
		for (Node crConsNode : Misc.getInheritedEnclosee(root, CriticalConstruct.class)) {
			nodeList.add(new Data((CriticalConstruct) crConsNode, crConsNode.getInfo().getSharedReads(),
					crConsNode.getInfo().getSharedWrites()));
		}

		for (Data data : nodeList) {
			System.err.println("Critical Construct #" + data.myId);
			data.node.getInfo().printNode();
			data.printReads();
			data.printWrites();
			System.err.println();
		}

		HashMap<Data, String> nameMap = new HashMap<>();
		for (Data data1 : nodeList) {
			/*
			 * Step 1: Find first key with which there is no conflict,
			 * and obtain the name.
			 */
			Data sibling = null;
			for (Data data2 : nameMap.keySet()) {
				if (!(Misc.doIntersect(data1.readSet, data2.writeSet) || Misc.doIntersect(data1.writeSet, data2.readSet)
						|| Misc.doIntersect(data1.writeSet, data2.writeSet))) {
					sibling = data2;
					break;
				}
			}
			if (sibling == null) {
				nameMap.put(data1, Builder.getNewTempName());
			} else {
				nameMap.put(data1, nameMap.get(sibling));
			}
		}

		for (Data data : nodeList) {
			System.err.println("Critical construct #" + data.myId + " belongs to cluster: " + nameMap.get(data));
		}

		for (Data data : nodeList) {
			CriticalConstruct node = data.node;
			String newConstruct = node.getF0().getInfo().getString() + " " + node.getF1().getInfo().getString() + " ("
					+ nameMap.get(data) + ")" + node.getF2().getInfo().getString() + "\n"
					+ node.getF4().getInfo().getString();
			CriticalConstruct newNode = FrontEnd.parseAlone(newConstruct, CriticalConstruct.class);
			BasicTransform.crudeReplaceOldWithNew(node, newNode);
		}

		DumpSnapshot.printToFile(root, "output.s");
	}
}
