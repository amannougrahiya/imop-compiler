/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.mhp.yuan;

import imop.ast.info.cfgNodeInfo.ParallelConstructInfo;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.SVEChecker;
import imop.lib.util.Misc;
import imop.parser.Program;

import java.text.DecimalFormat;
import java.util.*;

public class ConcurrencyAnalysis {

    public static class StaticPhase {
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((endingBarrier == null) ? 0 : endingBarrier.hashCode());
            result = prime * result + ((startingBarrier == null) ? 0 : startingBarrier.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            StaticPhase other = (StaticPhase) obj;
            if (this.endingBarrier != other.endingBarrier) {
                return false;
            }
            if (this.startingBarrier != other.startingBarrier) {
                return false;
            }
            return true;
        }

        public final Node startingBarrier;
        public final Node endingBarrier;
        public final Set<Node> stmts = new HashSet<>();

        public StaticPhase(Node startingBarrier, Node endingBarrier) {
            this.startingBarrier = startingBarrier;
            this.endingBarrier = endingBarrier;
            for (Node n : Misc.setIntersection(startingBarrier.getInfo().getFWRNodes(), endingBarrier.getInfo().getBWRNodes())) {
                stmts.add(n);
            }

		}

	}

    public static void performMHPByYuan() {
        int aggregateSize = 0;
        List<Integer> setSizes = new ArrayList<>();
        long timer = System.nanoTime();
        for (ParallelConstruct parCons : Misc.getExactEnclosee(Program.getRoot(), ParallelConstruct.class)) {
            BTNode btn = BarrierTreeConstructor.getBarrierTreeFor(parCons);
            btn.populateBTPairs(parCons);
            Set<StaticPhase> getAllStaticPhases = ConcurrencyAnalysis.getStaticPhases(parCons);
            aggregateSize += getAllStaticPhases.size();
            for (StaticPhase ph : getAllStaticPhases) {
                setSizes.add(ph.stmts.size());
            }
            finalizeConcurrencyRelations(parCons);
        }
        System.out.println("Time: " + (System.nanoTime() - timer) / 1e9 + "s.");
        int tot = 0;
        for (Integer i : setSizes) {
            tot += i;
        }
        int numNodes = Program.getRoot().getInfo().getCFGInfo().getLexicalCFGLeafContents().size();
        int maxSetSize = Collections.max(setSizes);
        double relMaxSetSize = (maxSetSize * 100.0) / numNodes;
        double avgSetSize = tot / setSizes.size();
        double relAvgSetSize = (avgSetSize * 100.0) / numNodes;
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println(
                aggregateSize + " " + maxSetSize + " " + df.format(relMaxSetSize) + " " + df.format(avgSetSize) + " " +
                        df.format(relAvgSetSize));
        System.exit(0);
    }

    private static Set<StaticPhase> getStaticPhases(ParallelConstruct parCons) {
        Set<StaticPhase> retSet = new HashSet<>();
        Set<Node> visited = new HashSet<>();
        Set<Node> workList = new HashSet<>();
        BeginNode bn = parCons.getInfo().getCFGInfo().getNestedCFG().getBegin();
        workList.add(bn);
        while (!workList.isEmpty()) {
            Node barr = Misc.getAnyElement(workList);
            workList.remove(barr);
            if (visited.contains(barr)) {
                continue;
            } else {
                visited.add(barr);
            }
            /*
             * Create all static phases that start at barr.
             */
            for (Node nextBarr : barr.getInfo().getFWRBarriers()) {
                retSet.add(new StaticPhase(barr, nextBarr));
                workList.add(nextBarr);
            }
        }
        return retSet;
    }

    public static void finalizeConcurrencyRelations(ParallelConstruct parCons) {
        // Step 1: With each Node that maybe a part of a BTPair corresponding to this parCons,
        // we should store the set of matching nodes.
        Set<ParallelConstructInfo.BTPair> btPairs = parCons.getInfo().getBTPairs();
        System.out.println(btPairs);


        //        Set<StaticPhase> allStaticPhases = ConcurrencyAnalysis.getStaticPhases(parCons);
        //        Map<StaticPhase, Set<StaticPhase>> matchingMap = new HashMap<>();
        //        // Step 2: With each static phase, we should store a set of static phases that match with it.
        //        for (StaticPhase sp1 : allStaticPhases) {
        //            for (StaticPhase sp2 : allStaticPhases) {
        //                if (sp1 == sp2) {
        //                    return;
        //                }
        //
        //            }
        //        }

        // Step 3: Now, we process each static phase one-by-one to populate the set of aggregate phases.
    }

    //    public static class AggregatePhase {
    //        Set<BT>
    //    }
    //
    //    private static Set<AggregatePhase> a;


    public static void printBarrierTrees() {
        if (Program.getRoot() == null) {
            Misc.exitDueToError("Could not find any program! Exiting..");
        }
        String barrStr = "";
        int i = 1;
        for (FunctionDefinition funcDef : Program.getRoot().getInfo().getAllFunctionDefinitions()) {
            String funcName = "";
            BTNode funcBT = funcDef.getInfo().getBTNode();
            funcName += funcDef.getInfo().getFunctionName() + ": \n";
            String funcLoc = "";
            if (funcBT != null) {
                funcLoc += funcBT.getString(1) + "\n";
            }
            for (ParallelConstruct parCons : Misc.getExactEnclosee(funcDef, ParallelConstruct.class)) {
                BTNode btNode = parCons.getInfo().getBTNode();
                if (btNode != null) {
                    funcLoc += "PC #" + i++ + ": \n";
                    funcLoc += btNode.getString(1) + "\n";
                }
            }
            if (!funcLoc.isEmpty()) {
                barrStr += funcName + funcLoc;
            }
        }
        System.out.println(barrStr);
        System.exit(0);
    }

    public static void verifyForMVEs() {
        if (Program.getRoot() == null) {
            Misc.exitDueToError("Could not find any program! Exiting..");
        }
        Set<Node> leaves = Program.getRoot().getInfo().getCFGInfo().getLexicalCFGContents();
        System.err.println("Total number of leaf nodes: " + leaves.size());
        int i = 0, j = 0;
        for (Node leaf : leaves) {
            if (Misc.isAPredicate(leaf)) {
                i++;
                if (leaf instanceof Expression) {
                    Expression predicate = (Expression) leaf;
                    if (!SVEChecker.isSingleValuedPredicate(predicate)) {
                        Node parent = Misc.getEnclosingCFGNonLeafNode(predicate);
                        if (parent.getInfo().hasBarrierInCFG()) {
                            if (parent instanceof IfStatement || parent instanceof SwitchStatement) {
                                continue;
                            } else {
                                j++;
                                System.err.println("The problematic loop is: " + parent);
                            }
                        }
                    }
                }
            }
        }
        System.err.println("Total number of predicates: " + i);
        System.err.println("Total number of MVE predicates that contain barriers within them: " + j);
        System.exit(0);
    }

}
