/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import imop.ast.node.external.*;
import imop.lib.analysis.flowanalysis.Cell;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.flowanalysis.dataflow.DataDependenceForward.DataDependenceForwardFF;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.flowanalysis.generic.FlowAnalysis.FlowFact;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.ParallelPhaseDumper;
import imop.lib.analysis.mhp.incMHP.ParallelRegionDumper;
import imop.lib.analysis.solver.InductionRange;
import imop.lib.analysis.solver.NaturalLoopAnalysis;
import imop.lib.analysis.solver.tokens.IdOrConstToken;
import imop.lib.cfg.DotGraphGenerator;
import imop.lib.cfg.TypeOfCFG;
import imop.lib.cg.CallSite;
import imop.lib.getter.AllFunctionDefinitionGetter;
import imop.lib.getter.StringGetter.Commentor;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.parser.Program;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DumpSnapshot {

	public static void dumpAccessibleCells(String str) {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			tempStr += n.getInfo().getAllCellsAtNodeExclusively();
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-accessibleCells" + str + ".i", commetors);
	}

	public static void dumpCopyInfo(String str) {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			CellMap<Cell> copyMap = n.getInfo().getCopyMap();
			CellSet set = n.getInfo().getAllCellsAtNode();
			for (Cell w : copyMap.nonGenericKeySet()) {
				if (!set.contains(w)) {
					continue;
				}
				Cell rhs = copyMap.get(w);
				if (rhs == Cell.genericCell || !set.contains(rhs)) {
					continue;
				}
				tempStr += w + ":" + copyMap.get(w) + "; ";
			}
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-copyMap" + str + ".i", commetors);
	}

	public static void dumpDataDependence() {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commentors = new ArrayList<>();
		commentors.add((n) -> {
			String tempStr = "";
			FlowFact flow = n.getInfo().getIN(AnalysisName.DATA_DEPENDENCE_FORWARD);
			if (flow != null) {
				tempStr += "IN_FORWARD: " + flow.getString();
			}
			flow = n.getInfo().getOUT(AnalysisName.DATA_DEPENDENCE_BACKWARD);
			if (flow != null) {
				tempStr += "OUT_BACKWARD: " + flow.getString();
			}
			// if (n instanceof DummyFlushDirective) {
			// for (Node pred : n.getInfo().getCFGInfo().getInterTaskLeafPredecessorNodes())
			// {
			// tempStr += "\nPRED: " + pred;
			// }
			// for (Node succ : n.getInfo().getCFGInfo().getInterTaskLeafSuccessorNodes()) {
			// tempStr += "\nSUCC: " + succ;
			// }
			// }
			return tempStr;
		});
		commentors.add((n) -> {
			String tempStr = n.getClass().getSimpleName() + ": ";
			for (Node flowDest : n.getInfo().getFlowDestinations()) {
				// assert(flowDest.getInfo().getFlowSources().contains(n));
				if (!flowDest.getInfo().getFlowSources().contains(n)) {
					System.err.println("Couldn't find flow source " + n + " in " + flowDest);
				}
			}
			for (Node flowDest : n.getInfo().getAntiDestinations()) {
				// assert(flowDest.getInfo().getAntiSources().contains(n));
				if (!flowDest.getInfo().getAntiSources().contains(n)) {
					System.err.println("Couldn't find anti source " + n + " in " + flowDest);
				}
			}
			for (Node flowDest : n.getInfo().getOutputDestinations()) {
				// assert(flowDest.getInfo().getOutputSources().contains(n));
				if (!flowDest.getInfo().getOutputSources().contains(n)) {
					System.err.println("Couldn't find output source " + n + " in " + flowDest);
				}
			}
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-datadep.i", commentors);
	}

	public static void dumpDominatorInfo(String string) {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			Set<Node> dominators = n.getInfo().getDominators();
			String dominatorStr;
			if (dominators == null) {
				dominatorStr = "UNIVERSAL";
			} else {
				dominatorStr = n.getInfo().getDominators().toString();
			}
			if (!dominatorStr.isEmpty()) {
				tempStr += n.getClass().getSimpleName() + ": " + dominatorStr;
			}
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-dominators" + string + ".i", commetors);
	}

	public static void dumpHeapValidity() {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			FlowFact flow = n.getInfo().getOUT(AnalysisName.HEAP_VALIDITY);
			if (flow != null) {
				tempStr += "OUT: " + flow.getString();
			}
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-heapValidity.i", commetors);
	}

	public static void dumpLiveness() {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			FlowFact flow = n.getInfo().getOUT(AnalysisName.LIVENESS);
			if (flow != null) {
				tempStr += "OUT: " + flow.getString();
			}
			// if (n instanceof DummyFlushDirective) {
			// for (Node pred : n.getInfo().getCFGInfo().getInterTaskLeafPredecessorNodes())
			// {
			// tempStr += "PRED: " + pred;
			// }
			// }
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-liveness.i", commetors);
	}

	public static void dumpLockSet() {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			FlowFact flow = n.getInfo().getIN(AnalysisName.LOCKSET_ANALYSIS);
			if (flow != null) {
				tempStr += "[";
				tempStr += n.getInfo().getIN(AnalysisName.LOCKSET_ANALYSIS).getString();
				tempStr += "]";
			}
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-locks.i", commetors);
	}

	public static void dumpLoopHeadersOnSTDOUT() {
		if (Program.printNoFiles) {
			return;
		}
		FunctionDefinition main = Program.getRoot().getInfo().getMainFunction();
		if (main == null) {
			System.exit(0);
		}
		Set<Node> loopHeaders = NaturalLoopAnalysis.getLoopHeadersFromCFG(main);
		System.out.println("Distinct loop-headers: " + loopHeaders.size());
		for (Node loopHeader : loopHeaders) {
			System.out.println("Loop header: " + loopHeader);
			for (Node loopTail : NaturalLoopAnalysis.getLoopTails(loopHeader)) {
				Set<Node> loopContents = NaturalLoopAnalysis.getLoopContentsOfThisLoopHeader(loopHeader, loopTail);
				HashMap<Symbol, InductionRange> map = NaturalLoopAnalysis.getBasicInductionValueRange(loopHeader,
						loopTail, loopContents);
				for (Symbol sym : map.keySet()) {
					InductionRange idr = map.get(sym);
					System.out.println("\t Symbol " + sym + " has following constraints: ");
					System.out.println(idr.getRanges(IdOrConstToken.getNewConstantToken("0"),
							IdOrConstToken.getNewIdToken("numTh")));
				}
			}
		}
	}

	public static void dumpPhaseAndCopyInfo(String str) {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			CellMap<Cell> copyMap = n.getInfo().getCopyMap();
			CellSet set = n.getInfo().getAllCellsAtNode();
			for (Cell w : copyMap.nonGenericKeySet()) {
				if (!set.contains(w)) {
					continue;
				}
				Cell rhs = copyMap.get(w);
				if (rhs == Cell.genericCell || !set.contains(rhs)) {
					continue;
				}
				tempStr += w + ":" + copyMap.get(w) + "; ";
			}
			return tempStr;
		});
		commetors.add((n) -> {
			String tempStr = "[";
			for (AbstractPhase<?, ?> ph : n.getInfo().getNodePhaseInfo().getPhaseSet()) {
				tempStr += ph.getPhaseId() + "; ";
			}
			tempStr += "]";
			return tempStr;
		});
		// commetors.add((n) -> {
		// String tempStr = "";
		// if (n instanceof DummyFlushDirective) {
		// for (Node interSucc :
		// n.getInfo().getCFGInfo().getInterTaskLeafSuccessorNodes()) {
		// if (interSucc instanceof DummyFlushDirective) {
		// DummyFlushDirective succDFD = (DummyFlushDirective) interSucc;
		// if (succDFD.getInfo().hasLabelAnnotations()) {
		// tempStr += succDFD.getInfo().getLabelAnnotations();
		// }
		// }
		// }
		// }
		// return tempStr;
		// });
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-copyAndPhase" + str + ".i", commetors);
	}

	public static void dumpPhaseListOfAllParCons(String string) {
		if (Program.printNoFiles) {
			return;
		}
		String text = "";
		for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			text += Misc.getLineNum(parCons) + ": " + parCons.getInfo().getConnectedPhases() + "\n";
		}
		DumpSnapshot.printToFile(text, Program.fileName + "-allPhList" + string + ".txt");
	}

	public static void dumpPhases(String string) {
		if (Program.printNoFiles) {
			return;
		}
		List<Integer> newId = new ArrayList<>();
		for (Node n : Program.getRoot().getInfo().getCFGInfo().getLexicalCFGContentsInPostOrder()) {
			for (AbstractPhase<?, ?> ph : n.getInfo().getNodePhaseInfo().getPhaseSet()) {
				int thisPH = ph.getPhaseId();
				if (!newId.contains(thisPH)) {
					newId.add(thisPH);
				}
			}
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			List<Integer> arr = new ArrayList<>();
			for (AbstractPhase<?, ?> ph : n.getInfo().getNodePhaseInfo().getPhaseSet()) {
				arr.add(newId.indexOf(ph.getPhaseId()) + 1);
			}
			Collections.sort(arr);
			String tempStr = arr.toString();
			return tempStr;
		});
		// commetors.add((n) -> {
		// String tempStr = "";
		// if (n instanceof DummyFlushDirective) {
		// for (Node interSucc :
		// n.getInfo().getCFGInfo().getInterTaskLeafSuccessorNodes()) {
		// if (interSucc instanceof DummyFlushDirective) {
		// DummyFlushDirective succDFD = (DummyFlushDirective) interSucc;
		// if (succDFD.getInfo().hasLabelAnnotations()) {
		// tempStr += succDFD.getInfo().getLabelAnnotations();
		// }
		// }
		// }
		// }
		// return tempStr;
		// });
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + string + "-phases.i", commetors);
	}

	public static void dumpPointsTo(String string) {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			FlowFact flow;
			// flow = n.getInfo().getIN(AnalysisName.POINTSTO);
			// if (flow != null) {
			// tempStr += "IN: " + flow.getString();
			// }
			// tempStr += "\n";
			flow = n.getInfo().getOUT(AnalysisName.POINTSTO);
			if (flow != null) {
				tempStr += "OUT: " + flow.getString();
			}
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + string + "-pointsTo.i", commetors);

	}

	public static void dumpPredicates(String string) {
		if (Program.printNoFiles) {
			return;
		}
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			FlowFact flow = n.getInfo()
					.getOUT(Program.useInterProceduralPredicateAnalysis ? AnalysisName.PREDICATE_ANALYSIS
							: AnalysisName.INTRA_PREDICATE_ANALYSIS);
			if (flow != null) {
				tempStr += "OUT: " + flow.getString();
			}
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + string + "-predicates.i", commetors);
	}

	public static void dumpReachingDefinitions(String string) {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			String reachingDefStr = n.getInfo().getReachingDefinitions().toString();
			if (!reachingDefStr.isEmpty()) {
				tempStr += n.getClass().getSimpleName() + ": " + reachingDefStr;
			}
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + string + "-reachingDefs.i", commetors);
	}

	public static void dumpReadDestinations() {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			tempStr += "\nRead_Dest: ";
			tempStr += n.getInfo().getReadDestinations();
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-readDest.i", commetors);
	}

	public static void dumpReadSources() {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			DataDependenceForwardFF ff = (DataDependenceForwardFF) n.getInfo()
					.getIN(AnalysisName.DATA_DEPENDENCE_FORWARD);
			if (ff != null && ff.readSources != null) {
				tempStr += "\nRead_Src: " + ff.readSources;
			}
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-readSrc.i", commetors);
	}

	public static void dumpVisibleSharedReadWrittenCells(String string) {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			// tempStr += "ALL: ";
			// tempStr += n.getInfo().getAllCellsAtNode();
			// tempStr += "\nSHARED:";
			// tempStr += n.getInfo().getSharedCellsAtNode();
			tempStr += "\nREAD_S: ";
			tempStr += n.getInfo().getSharedReads();
			tempStr += "\nWRITE_S: ";
			tempStr += n.getInfo().getSharedWrites();
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + string + "-allAndShared.i", commetors);
	}

	private static void dumpWriteDestinations() {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			tempStr += "\nWrite_Dest: ";
			tempStr += n.getInfo().getWriteDestinations();
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-writeDest.i", commetors);
	}

	public static void dumpWriteSources() {
		if (Program.printNoFiles) {
			return;
		}
		List<Commentor> commetors = new ArrayList<>();
		commetors.add((n) -> {
			String tempStr = "";
			DataDependenceForwardFF ff = (DataDependenceForwardFF) n.getInfo()
					.getIN(AnalysisName.DATA_DEPENDENCE_FORWARD);
			DataDependenceForwardFF ffOUT = (DataDependenceForwardFF) n.getInfo()
					.getOUT(AnalysisName.DATA_DEPENDENCE_FORWARD);
			if (ff != null && ff.writeSources != null) {
				tempStr += "\nWrite_Src: [" + ff.writeSources.hashCode() + ", " + ffOUT.writeSources.hashCode() + ", "
						+ n.getClass().getSimpleName() + "]" + ff.writeSources;
			}
			return tempStr;
		});
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-writeSrc.i", commetors);
	}

	/**
	 * This method dumps the callSiteStack to stdout
	 *
	 * @param callSiteStack
	 */
	public static void dumpContextStack(Stack<CallSite> contextStack) {
		if (Program.printNoFiles) {
			return;
		}
		System.out.print("\n CallSiteStack: [");
		for (CallSite callS : contextStack) {
			System.out.print(callS.calleeName + "; ");
		}
		System.out.println("]");
	}

	/**
	 * For debugging purposes This method dumps the mhp information obtained so far
	 * for all the leaf CFG nodes
	 *
	 * @param n
	 */
	public static void dumpMHPInformation(Node n) {
		if (Program.printNoFiles) {
			return;
		}
		n.accept(new ParallelPhaseDumper());
	}

	/**
	 * For debugging purposes This method dumps the parallelRegion hashCodes for all
	 * the leaf CFG nodes
	 *
	 * @param n
	 */
	public static void dumpParallelRegion(Node n) {
		if (Program.printNoFiles) {
			return;
		}
		n.accept(new ParallelRegionDumper());
	}

	/**
	 * This method dumps the Call Graph of the functions in the given rootScope
	 *
	 * @param rootScope
	 */
	public static void dumpCallGraph(Node root) {
		if (Program.printNoFiles) {
			return;
		}
		// Get all the functionDefinitions.
		AllFunctionDefinitionGetter funcGetter = new AllFunctionDefinitionGetter();
		root.accept(funcGetter);
		for (FunctionDefinition func : funcGetter.funcList) {
			if (func.getInfo().getCallSites().isEmpty()) {
				continue;
			}
			System.out.println("Call Graph for " + func.getInfo().getFunctionName() + " ("
					+ (func.getInfo().hasBarrierInCFG() ? "has barrier, " : "barrier-free, ")
					+ (func.getInfo().isRecursive() ? "recursive" : "non-recursive") + ") :");
			for (CallSite cs : func.getInfo().getCallSites()) {
				System.out.println("\t" + cs.calleeName);
			}
		}
	}

	public static void dumpRoot(String identifier) {
		if (Program.printNoFiles) {
			return;
		}
		DumpSnapshot.printToFile(Program.getRoot(), Program.fileName + "-" + identifier + ".i");
	}

	public static void dumpRootOrError(String string, List<SideEffect> sideEffectList) {
		if (Program.printNoFiles) {
			return;
		}
		// Old code: Now, we have annotated each constraint as a comment at the node
		// where the constraint occurred.
		// if (sideEffectList != null && sideEffectList.size() != 0) {
		// if (!Misc.changePerformed(sideEffectList)) {
		// StringBuilder str = new StringBuilder();
		// for (UpdateSideEffects se : sideEffectList) {
		// str.append(se.toString());
		// }
		// Misc.printToFile(str.toString(), Program.fileName + "-" + string + ".i");
		// return;
		// }
		// }
		dumpRoot(string);
	}

	/**
	 * This method returns a BufferedWriter object which wraps within it a stream to
	 * write to a file for dot graphs
	 * generation.
	 *
	 * @param fileName
	 *
	 * @return
	 */
	public static BufferedWriter getGraphFileWriter(TypeOfCFG nested, String fileName) {
		try {
			String name = "";
			switch (nested) {
			case NESTED:
				if (fileName == null) {
					name = "nestedDotGraph.gv";
				} else {
					name = fileName + "nestedDotGraph.gv";
				}
				break;
			case SIMPLE:
				name = "simpleDotGraph.gv";
				break;
			}

			String fs = System.getProperty("file.separator");
			String filePath = Program.class.getProtectionDomain().getCodeSource().getLocation().getFile();
			fileName = filePath + ".." + fs + "output-dump" + fs + name;

			System.err.println("\tCreating the dot file at " + fileName);
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			return bw;
		} catch (Exception e) {
			return null;
		}
	}

	public static void printToFile(Node root, String fileName, List<Commentor> commetors) {
		if (Program.printNoFiles) {
			return;
		}
		String fs = System.getProperty("file.separator");
		String filePath = Program.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		fileName = filePath + ".." + fs + "output-dump" + fs + fileName;
		BufferedWriter bw = Misc.getBufferedWriter(fileName);
		try {
			bw.write(root.getInfo().getString(commetors));
			bw.append(System.getProperty("line.separator"));
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printToFile(Node root, String fileName) {
		if (Program.printNoFiles) {
			return;
		}
		DumpSnapshot.printToFile(root, fileName, new ArrayList<>());
		// BufferedWriter bw = Misc.getBufferedWriter(fileName);
		// try {
		// bw.write(root.getInfo().getString());
		// bw.append(System.getProperty("line.separator"));
		// bw.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	public static void printToFile(String str, String fileName) {
		if (Program.printNoFiles) {
			return;
		}
		String fs = System.getProperty("file.separator");
		String filePath = Program.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		fileName = filePath + ".." + fs + "output-dump" + fs + fileName;
		BufferedWriter bw = Misc.getBufferedWriter(fileName);
		try {
			bw.write(str);
			bw.append(System.getProperty("line.separator"));
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printToFile(String str, String fileName, boolean b) {
		String fs = System.getProperty("file.separator");
		String filePath = Program.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		fileName = filePath + ".." + fs + "output-dump" + fs + fileName;
		BufferedWriter bw = Misc.getBufferedWriter(fileName);
		try {
			bw.write(str);
			bw.append(System.getProperty("line.separator"));
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void dumpNestedCFG(Node root, String fileName) {
		if (Program.printNoFiles) {
			return;
		}
		DotGraphGenerator graphGen = new DotGraphGenerator(root, fileName);
		graphGen.create(TypeOfCFG.NESTED);
	}

}
