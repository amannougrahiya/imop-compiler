/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.flowanalysis.controlflow;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.BranchEdge;
import imop.lib.analysis.flowanalysis.generic.AnalysisName;
import imop.lib.analysis.mhp.AbstractPhase;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.util.ImmutableList;

import java.util.*;

public class ReversePath {
    public final Node startingNode;
    public final ImmutableList<BranchEdge> edgesOnPath;
    private static int maxSizeOfPath = 0;

    public ReversePath(Node startingBPP, ImmutableList<BranchEdge> edgesOnPath) {
        assert (startingBPP == null ||
                (startingBPP instanceof BarrierDirective || startingBPP instanceof PostCallNode ||
                        (startingBPP instanceof BeginNode && (startingBPP.getParent() instanceof ParallelConstruct ||
                                startingBPP.getParent() instanceof FunctionDefinition))));
        this.startingNode = startingBPP;
        this.edgesOnPath = edgesOnPath;
        if (edgesOnPath.size() > maxSizeOfPath) {
            maxSizeOfPath = edgesOnPath.size();
        }
    }

    List<BranchEdge> getNewList(BranchEdge be) {
        int predicateIndex = this.getPredicateIndexOf(be);
        List<BranchEdge> newList;
        newList = new ArrayList<>(this.edgesOnPath);
        if (predicateIndex != -1) {
            if (predicateIndex >= 0) {
                newList.subList(0, predicateIndex + 1).clear();
            }
        }
        newList.add(0, be);
        return newList;
    }

    public boolean pathStartsAtThePhase(AbstractPhase<?, ?> ph) {
        if (this.pathStartsAtABarrier()) {
            return ph.getBeginPoints().contains(this.startingNode);
        }
        return this.startingNode.getInfo().getNodePhaseInfo().getPhaseSet().contains(ph);
    }

    public boolean pathStartsAtAFunctionBeginNode() {
        return this.startingNode instanceof BeginNode && this.startingNode.getParent() instanceof FunctionDefinition;
    }

    public boolean pathStartsAtAPostCallNode() {
        return this.startingNode instanceof PostCallNode;
    }

    public boolean pathStartsAtABarrier() {
        return !this.pathStartsAtAFunctionBeginNode() && !this.pathStartsAtAPostCallNode();
    }

    private int getPredicateIndexOf(BranchEdge be) {
        int i = -1;
        for (BranchEdge b : this.edgesOnPath) {
            i++;
            if (b.predicate == be.predicate) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if the receiver path is subsumed in the argument path as a suffix.
     *
     * @param other
     *
     * @return
     */
    public boolean isStrictlySubsumedAsSuffixOf(ReversePath other) {
        if (!this.startingNode.equals(other.startingNode)) {
            return false;
        }
        if (this.edgesOnPath.size() >= other.edgesOnPath.size()) {
            return false;
        }
        ListIterator<BranchEdge> thisIterator = this.edgesOnPath.listIterator(this.edgesOnPath.size());
        ListIterator<BranchEdge> thatIterator = other.edgesOnPath.listIterator(other.edgesOnPath.size());
        while (thisIterator.hasPrevious()) {
            BranchEdge thisEdge = thisIterator.previous();
            BranchEdge thatEdge = thatIterator.previous();
            if (!thisEdge.equals(thatEdge)) {
                return false;
            }
        }
        //		System.err.println("The path " + this + " is subsumed by the bigger path " + other);
        return true;
    }

    public void getExtendedPaths(BeginPhasePoint bpp, Set<Set<BranchEdge>> setOfSetsOfEdges) {
        if (this.pathStartsAtABarrier()) {
            if (this.startingNode == bpp.getNode()) {
                Set<BranchEdge> set = new HashSet<>(this.edgesOnPath);
                setOfSetsOfEdges.add(set);
            }
            return;
        } else if (this.pathStartsAtAFunctionBeginNode()) {
            FunctionDefinition func = (FunctionDefinition) this.startingNode.getParent();
            for (CallStatement caller : func.getInfo().getCallersOfThis()) {
                PreCallNode pre = caller.getPreCallNode();
                if (!bpp.getReachableNodes().contains(pre)) {
                    continue;
                }

                PredicateAnalysis.PredicateFlowFact preFlowFact = (PredicateAnalysis.PredicateFlowFact) pre.getInfo().getIN(AnalysisName.PREDICATE_ANALYSIS);
                Set<Set<BranchEdge>> preSetOfSetOfEdges = new HashSet<>();
                for (ReversePath preRE : preFlowFact.controlPredicatePaths) {
                    preRE.getExtendedPaths(bpp, preSetOfSetOfEdges);
                }
                if (!preSetOfSetOfEdges.isEmpty()) {
                    for (Set<BranchEdge> setOfEdges : preSetOfSetOfEdges) {
                        setOfEdges.addAll(this.edgesOnPath);
                    }
                    setOfSetsOfEdges.addAll(preSetOfSetOfEdges);
                }
            }
        } else if (this.pathStartsAtAPostCallNode()) {
            for (FunctionDefinition func : ((PostCallNode) this.startingNode).getParent().getInfo().getCalledDefinitions()) {
                EndNode endNode = func.getInfo().getCFGInfo().getNestedCFG().getEnd();
                if (!bpp.getReachableNodes().contains(endNode)) {
                    continue;
                }

                PredicateAnalysis.PredicateFlowFact endFlowFact = (PredicateAnalysis.PredicateFlowFact) endNode.getInfo().getIN(AnalysisName.PREDICATE_ANALYSIS);
                Set<Set<BranchEdge>> endSetOfSetOfEdges = new HashSet<>();
                for (ReversePath preRE : endFlowFact.controlPredicatePaths) {
                    preRE.getExtendedPaths(bpp, endSetOfSetOfEdges);
                }
                if (!endSetOfSetOfEdges.isEmpty()) {
                    for (Set<BranchEdge> setOfEdges : endSetOfSetOfEdges) {
                        setOfEdges.addAll(this.edgesOnPath);
                    }
                    setOfSetsOfEdges.addAll(endSetOfSetOfEdges);
                }
            }
        } else {
            assert (false);
        }
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
        ReversePath other = (ReversePath) obj;
        if (!startingNode.equals(other.startingNode)) {
            return false;
        }
        if (edgesOnPath == null) {
            if (other.edgesOnPath != null) {
                return false;
            }
        } else if (!edgesOnPath.equals(other.edgesOnPath)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((edgesOnPath == null) ? 0 : edgesOnPath.hashCode());
        return result;
    }

    @Override
    public String toString() {
        String str = "\nReverse<";
        if (this.pathStartsAtABarrier()) {
            str += "B-" + startingNode.getNodeId() + ",";
        } else if (this.pathStartsAtAFunctionBeginNode()) {
            str += "F-" + startingNode.getNodeId() + ",";
        } else if (this.pathStartsAtAPostCallNode()) {
            str += "C-" + startingNode.getNodeId() + ",";
        } else {
            assert (false);
        }
        for (BranchEdge edge : edgesOnPath) {
            str += " " + edge.toString() + ";";
        }
        str += ">";
        return str;
    }
}
