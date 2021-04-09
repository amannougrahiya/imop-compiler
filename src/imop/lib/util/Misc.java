/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import imop.ast.annotation.CaseLabel;
import imop.ast.annotation.DefaultLabel;
import imop.ast.annotation.IncompleteEdge;
import imop.ast.annotation.Label;
import imop.ast.info.NodeInfo;
import imop.ast.info.RootInfo;
import imop.ast.info.StatementInfo;
import imop.ast.info.cfgNodeInfo.*;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.deprecated.Deprecated_AntiDefVisitor;
import imop.deprecated.Deprecated_DefinitionReachabilityMarker;
import imop.deprecated.Deprecated_FlowAndOutputDependenceMarker;
import imop.deprecated.Deprecated_LivenessAnalysis;
import imop.lib.analysis.flowanalysis.*;
import imop.lib.analysis.mhp.*;
import imop.lib.analysis.mhp.incMHP.Phase;
import imop.lib.analysis.mhp.incMHP.PhasePoint;
import imop.lib.analysis.mhp.lock.AbstractLock;
import imop.lib.analysis.mhp.yuan.YPhase;
import imop.lib.analysis.typeSystem.*;
import imop.lib.cfg.info.ProgramElementExactCaches;
import imop.lib.cfg.parallel.DataFlowGraph;
import imop.lib.cg.CallSite;
import imop.lib.getter.*;
import imop.lib.transform.BasicTransform;
import imop.lib.transform.updater.InsertOnTheEdge.PivotPoint;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.lib.transform.updater.sideeffect.SyntacticConstraint;
import imop.parser.CParserConstants;
import imop.parser.FrontEnd;
import imop.parser.Program;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This class contains all those functions which can commonly be required across
 * multiple visitors.
 *
 * @author aman
 */
public class Misc {
	public static Set<String> uniqueTraces = new HashSet<>();

	Misc() {
	}

	private static Set<Node> cfgNodes = new HashSet<>();
	private static Set<Node> nonCFGNodes = new HashSet<>();

	public static void resetStaticFields() {
		Misc.cfgNodes.clear();
		Misc.nonCFGNodes.clear();
	}

	/**
	 * Checks if the call-stack contains a frame for the method with given name.
	 *
	 * @param methodName
	 *                   name of the method to be searched for in the current
	 *                   call-stack.
	 *
	 * @return {@code true}, if a method with the given method name is present in
	 *         the call-stack.
	 */
	public static boolean isCalledFromMethod(String methodName) {
		if (methodName == null) {
			return false;
		}
		Set<String> strSet = new HashSet<>();
		for (StackTraceElement st : Thread.currentThread().getStackTrace()) {
			strSet.add(st.getFileName() + "." + st.getMethodName() + ":" + st.getLineNumber() + ";");
			if (methodName.equals(st.getMethodName())) {
				Misc.uniqueTraces.addAll(strSet);
				return true;
			}
		}
		return false;
	}

	public static boolean changePerformed(List<SideEffect> sideEffects) {
		if (sideEffects == null) {
			assert (false);
		}
		if (sideEffects.stream().anyMatch(s -> s instanceof SyntacticConstraint)) {
			return false;
		}
		return true;
	}

	/**
	 * This method populates the Info.lockSet of various CFG nodes in the root, on
	 * the basis of syntactic enclosure of
	 * nodes with critical/atomic regions. Note: To remain conservative, any
	 * statement within a called function from a
	 * critical region, is not marked as locked with that region, since the function
	 * may be called from other contexts
	 * outside the region.
	 *
	 * @param root
	 */
	public static void performLocksetAnalysis(Node root) {
		root.accept(new OldLocksetMarker(), new ArrayList<>());

		// Testing:
		// System.err.println("List of locks in the code: ");
		// for (Lock lock: Lock.getAllLocks()) {
		// System.err.print(" " + lock.getName());
		// }
		// System.err.println();
	}

	/**
	 * This method populates various data dependence structures of elementary R/W
	 * CFG nodes.
	 *
	 * @param root
	 */
	public static void performDataDependenceAnalysis(Node root) {
		for (FunctionDefinition func : ((RootInfo) root.getInfo()).getAllFunctionDefinitions()) {
			/*
			 * Obtain Flow and Output Edges' Information
			 */
			Deprecated_FlowAndOutputDependenceMarker flowOutputMarker = new Deprecated_FlowAndOutputDependenceMarker();
			for (Definition def : func.getInfo().deprecated_getDefinitionList()) {
				// Initialize the marker
				flowOutputMarker.srcDef = def;
				flowOutputMarker.visitedNodes = new HashSet<>();

				if (isCFGLeafNode(def.getDefiningNode())) {
					for (Node startNode : def.getDefiningNode().getInfo().getCFGInfo().getSuccBlocks()) {
						startNode.accept(flowOutputMarker);
					}
				} else { // def.definingNode is either of Parallel{For,
					// Sections}Construct or TaskConstruct
					def.getDefiningNode().getInfo().getCFGInfo().getNestedCFG().getBegin().accept(flowOutputMarker);
				}
			}

			/*
			 * Obtain Anti-Dependence
			 */
			// For all the uses in all the nodes, flow backward and mark the
			// defs with anti-edges
			func.accept(new Deprecated_AntiDefVisitor());
		}
	}

	/**
	 * Performs intra-procedural liveness analysis for all the functions in the
	 * given root node.
	 *
	 * @param root
	 */
	public static void performLivenessAnalysis(Node root) {
		// For each function, call livenessAnalysis on its endNode
		for (FunctionDefinition func : ((RootInfo) root.getInfo()).getAllFunctionDefinitions()) {
			if (func.getInfo().getCFGInfo() != null) {
				func.getInfo().getCFGInfo().getNestedCFG().getEnd().accept(new Deprecated_LivenessAnalysis());
			}
		}
		// System.out.println("Liveness Analysis complete.");
	}

	/**
	 * For a given root node, this method calls the appropriate visitors to perform
	 * reaching definitions analysis in
	 * root.
	 *
	 * @param root
	 */
	public static void performReachingDefinitionsAnalysis(Node root) {
		for (FunctionDefinition func : ((RootInfo) root.getInfo()).getAllFunctionDefinitions()) {
			// System.out.println("Processing a function definition: " +
			// ((FunctionDefinitionInfo)func.getInfo()).functionName);
			Deprecated_DefinitionReachabilityMarker defReachMarker = new Deprecated_DefinitionReachabilityMarker();
			for (Definition def : func.getInfo().deprecated_getDefinitionList()) {
				defReachMarker.markerDef = def;

				List<Node> startNodeList = new ArrayList<>();
				if (isCFGLeafNode(def.getDefiningNode())) {
					if (def.getDefiningNode().getInfo().getWrites().size() > 1) {
						startNodeList.add(def.getDefiningNode());
					} else {
						// TODO: After understanding this if-else block, try and
						// verify it.
						for (Node succ : def.getDefiningNode().getInfo().getCFGInfo().getSuccBlocks()) {
							startNodeList.add(succ);
						}
					}
				} else { // def.definingNode is a Parallel{For,
					// Sections}Construct or TaskConstruct
					startNodeList.add(def.getDefiningNode().getInfo().getCFGInfo().getNestedCFG().getBegin());
				}
				for (Node startNode : startNodeList) {
					startNode.accept(defReachMarker);
				}
			}
		}
		// System.out.println("Intra-thread Reaching Definition analysis
		// complete.");
	}

	/**
	 * This method tells whether two given vectors of PhasePoints have subType
	 * relation or not. If newPoints are
	 * subTypes of oldPoints, then we can stop processing.
	 *
	 * @param newPoints
	 * @param oldPoints
	 *
	 * @return
	 */
	public static boolean hasSubTypeEndPoints(List<PhasePoint> newPoints, List<PhasePoint> oldPoints) {
		// If there exists a new sink in newPoints, then return false.
		for (PhasePoint newPoint : newPoints) {
			boolean found = false;
			for (PhasePoint oldPoint : oldPoints) {
				if (oldPoint.getNode() == newPoint.getNode()
						&& hasEqualCallSiteStacks(newPoint.getCallSiteStack(), oldPoint.getCallSiteStack())) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Creates the inter-task data-flow edges among DummyFlushDirective.
	 */
	public static void createDataFlowGraph(Node rootNode) {
		for (ParallelConstruct parConstruct : Misc.getExactEnclosee(rootNode, ParallelConstruct.class)) {
			for (AbstractPhase<?, ?> ph : parConstruct.getInfo().getConnectedPhases()) {
				DataFlowGraph.populateInterTaskEdges(rootNode, ph);
			}
		}
	}

	/**
	 * This method obtains the ParallelConstructs of n, and calls the
	 * ParallelRegionMarker on them.
	 *
	 * @param n
	 */
	public static void markParallelRegions(Node n) {
		// Get all the parallel constructs in n (top level)
		Set<ParallelConstruct> parConstructList = Misc.getParConstructs(n);

		// Forall parallel constructs, call parallelRegionMarker on them.
		for (ParallelConstruct parConstruct : parConstructList) {
			// Call ParallelRegionMarker on parConstruct's beginNode,
			// IFF this hasn't been done already (possible in case of recursion)
			Node beginNode = parConstruct.getInfo().getCFGInfo().getNestedCFG().getBegin();
			if (beginNode.getInfo().isRunnableInRegion(parConstruct)) {
				continue;
			}

			// If not already present, add parConstruct to the regionInfo field
			// of its statements.
			parConstruct.getInfo().getCFGInfo().getNestedCFG().getBegin().accept(new ParallelRegionMarker(),
					parConstruct);
		}
	}

	// NOTE: The function written below is quite too inefficient. Kindly search
	// for the efficient version elsewhere.
	// /**
	// * This method takes a compound statement and removes the extra blocks
	// * within it.
	// *
	// * @param n
	// */
	// public static void normalizeCS(CompoundStatement n) {
	// List<Node> list = n.f1.nodes;
	// int i = 0; // Can't use the for-each style loop here
	// while (i < list.size()) {
	// Node element = list.get(i);
	// Node stmt = ((CompoundStatementElement) element).f0.choice;
	// if (stmt.getClass().getSimpleName().equals("Statement")) {
	// if (((Statement)
	// stmt).f0.choice.getClass().getSimpleName().equals("CompoundStatement")) {
	// // Normalize the internal CompoundStatement
	// CompoundStatement internalCS = ((CompoundStatement) ((Statement)
	// stmt).f0.choice);
	//
	// // Add all the elements from internal CompoundStatement to n
	// for (Node internalElem : internalCS.f1.nodes) {
	// list.add(i++, internalElem);
	// internalElem.parent = n.f1;
	// }
	//
	// // Remove the internalCS's element from n
	// list.remove(element);
	// i--;
	// }
	// }
	// i++;
	// }
	// }

	/**
	 * This method is used to put escape characters in a string having " and '. This
	 * is required to be called on str of
	 * StringGetter
	 *
	 * @param str
	 *
	 * @return
	 */
	public static String putEscapes(String str) {
		String out = new String();
		char[] charArray = str.toCharArray();
		for (int i = 0; i < str.length(); i++) {
			if (charArray[i] == '\"') {
				out += "\\";
			}
			if (charArray[i] == '\'') {
				out += "\\";
			}
			out += charArray[i];
		}
		return out;
	}

	/**
	 * This method is used to put escape characters in a string having " and '. And,
	 * this method also removes the spaces
	 * from given string.
	 *
	 * @param str
	 *
	 * @return
	 */
	public static String putEscapesRemoveSpaces(String str) {
		String out = new String();
		char[] charArray = str.toCharArray();
		for (int i = 0; i < str.length(); i++) {
			if (charArray[i] == '\"') {
				out += "\\";
			}
			if (charArray[i] == '\'') {
				out += "\\";
			}
			if (charArray[i] == ' ') {
				continue;
			}
			out += charArray[i];
		}
		return out;
	}

	/**
	 * Checks whether the queried node is any of the jump statements - - return,
	 * continue, break, goto;
	 *
	 * @param n
	 *
	 * @return
	 */
	public static boolean isJumpNode(Node n) {
		if (n instanceof JumpStatement) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method returns true if n is a CFG node
	 *
	 * @param n
	 *
	 * @return
	 */
	public static boolean isCFGNode(Node n) {
		if (n == null) {
			return false;
		}
		if (n.hasKnownCFGStatus() && n.isKnownCFGNode()) {
			return true;
		} else {
			if (Misc.cfgNodes.contains(n)) {
				return true;
			} else if (Misc.nonCFGNodes.contains(n)) {
				return false;
			}
			if (n instanceof OmpForReinitExpression) {
				OmpForReinitExpression ompForReinit = (OmpForReinitExpression) n;
				if (ompForReinit.isCFGNode()) {
					Misc.cfgNodes.add(n);
					return true;
				}
			}
			if (n instanceof ParallelConstruct) {
				ParallelConstruct parConstruct = (ParallelConstruct) n;
				if (parConstruct.isCFGNode()) {
					Misc.cfgNodes.add(n);
					return true;
				}
			}
			if (n instanceof Expression) {
				Expression exp = (Expression) n;
				if (exp.isCFGNode()) {
					boolean isCFG = Misc.isACFGExpression(exp);
					if (isCFG) {
						Misc.cfgNodes.add(n);
						return true;
					} else {
						Misc.nonCFGNodes.add(n);
						return false;
					}
				}
			}
		}
		Misc.nonCFGNodes.add(n);
		return false;
		// String name = n.getClass().getSimpleName();
		// switch (name) {
		// case "Declaration":
		// case "ParameterDeclaration":
		// case "UnknownCpp":
		// case "UnknownPragma":
		// case "OmpForInitExpression":
		// case "OmpForCondition":
		// case "OmpForReinitExpression":
		// case "FlushDirective":
		// case "DummyFlushDirective":
		// case "BarrierDirective":
		// case "TaskwaitDirective":
		// case "TaskyieldDirective":
		// case "ExpressionStatement":
		// case "GotoStatement":
		// case "ContinueStatement":
		// case "BreakStatement":
		// case "ReturnStatement":
		// case "BeginNode":
		// case "EndNode":
		// case "FunctionDefinition":
		// case "ParallelConstruct":
		// case "ForConstruct":
		// case "SectionsConstruct":
		// case "SingleConstruct":
		// case "TaskConstruct":
		// case "ParallelForConstuct":
		// case "ParallelSectionsConstruct":
		// case "MasterConstruct":
		// case "CriticalConstruct":
		// case "AtomicConstruct":
		// case "OrderedConstruct":
		// case "CompoundStatement":
		// case "IfStatement":
		// case "SwitchStatement":
		// case "WhileStatement":
		// case "DoStatement":
		// case "ForStatement":
		// case "IfClause":
		// case "NumThreadsClause":
		// case "FinalClause":
		// case "CallStatement":
		// case "PreCallNode":
		// case "PostCallNode":
		// return true;
		// case "Expression":
		// // case "ParameterDeclaration": // Added later, on 17-11-2016.
		// return Misc.isAPredicate((Expression) n);
		// }
		// return false;
	}

	/**
	 * Returns true if node n is a type of leaf CFG node
	 *
	 * @param n
	 *
	 * @return
	 */
	public static boolean isCFGLeafNode(Node n) {
		if (n == null) {
			return false;
		}
		if (n.hasKnownCFGStatus() && n.isKnownCFGLeafNode()) {
			return true;
		} else {
			if (n instanceof OmpForReinitExpression) {
				OmpForReinitExpression ompForReinit = (OmpForReinitExpression) n;
				if (ompForReinit.isCFGNode()) {
					return true;
				}
			}
			if (n instanceof Expression) {
				Expression exp = (Expression) n;
				if (exp.isCFGNode()) {
					return Misc.isACFGExpression(exp);
				}
			}

		}
		return false;
		// String name = n.getClass().getSimpleName();
		// switch (name) {
		// case "Declaration":
		// case "ParameterDeclaration":
		// case "UnknownCpp":
		// case "UnknownPragma":
		// case "OmpForInitExpression":
		// case "OmpForCondition":
		// case "OmpForReinitExpression":
		// case "FlushDirective":
		// case "DummyFlushDirective":
		// case "BarrierDirective":
		// case "TaskwaitDirective":
		// case "TaskyieldDirective":
		// case "ExpressionStatement":
		// case "GotoStatement":
		// case "ContinueStatement":
		// case "BreakStatement":
		// case "ReturnStatement":
		// case "BeginNode":
		// case "EndNode":
		// case "IfClause":
		// case "NumThreadsClause":
		// case "FinalClause":
		// case "PreCallNode":
		// case "PostCallNode":
		// // case "ParameterDeclaration": // Added later, on 17-11-2016.
		// return true;
		// case "Expression":
		// return Misc.isAPredicate((Expression) n);
		// }
		// return false;
	}

	/**
	 * Returns true if node n is a type of non-leaf CFG node
	 *
	 * @param n
	 *
	 * @return
	 */
	public static boolean isCFGNonLeafNode(Node n) {
		if (Misc.isCFGNode(n)) {
			if (Misc.isCFGLeafNode(n)) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public static Declaration getRenamedDeclaration(Declaration inDecl, String newName) {
		if (inDecl == null) {
			return inDecl;
		}
		// Find the exact node that specifies the name, and then use
		// getNodeReplacedString.
		List<String> idList = inDecl.getInfo().getIDNameList();
		if (idList == null || idList.size() != 1) {
			return null;
		}
		String oldName = idList.get(0);
		Declarator declarator = inDecl.getInfo().getDeclarator(oldName);
		NodeToken oldToken = DeclarationInfo.getRootIdNodeToken(declarator);
		String newDeclString = StringGetter.getNodeReplacedString(inDecl, oldToken, newName);
		Declaration newDecl = FrontEnd.parseAndNormalize(newDeclString, Declaration.class);
		return newDecl;
	}

	/**
	 * Returns a declaration for given symbol name ({@code tempName}), as a copy of
	 * {@code baseDecl} with renaming
	 * performed as per {@code initRenamingMap}.
	 * <br>
	 *
	 * @param baseDecl
	 *                        a base declaration which is source of the copy that we
	 *                        wish to create.
	 * @param tempName
	 *                        name of the new symbol to be created with the return
	 *                        declaration.
	 * @param initRenamingMap
	 *                        renaming map to be followed in the initializer.
	 *
	 * @return a new declaration for symbol with name {@code tempName}, type as this
	 *         type object, and static specifier
	 *         and renaming initializer as present in {@code baseDecl}, if any.
	 */
	public static Declaration getRenamedDeclarationWithRenamedInit(Declaration baseDecl, String tempName,
			HashMap<String, String> initRenamingMap) {
		return (Declaration) BasicTransform.obtainRenamedNode(Misc.getRenamedDeclaration(baseDecl, tempName),
				initRenamingMap);
	}

	/**
	 * // * @param name // * @param node // * @return Union entry for the given id
	 * named "name", when used in given // *
	 * "node". Returns null, if entry not found in any of the outer // * nesting
	 * parents of "node". //
	 */
	// public static UnionType getUnionEntry(String name, Node node) {
	// if (node == null)
	// return null;
	//
	// Node scopeChoice = node;
	// while (scopeChoice != null) {
	// if (scopeChoice instanceof TranslationUnit) {
	// HashMap<String, UnionType> typeTable =
	// ((RootInfo)scopeChoice.getInfo()).getTypeTable();
	// if (typeTable.containsKey(name)) {
	// return typeTable.get(name);
	// }
	// } else if (scopeChoice instanceof FunctionDefinition) {
	// // Do nothing.
	// } else if (scopeChoice instanceof CompoundStatement) {
	// HashMap<String, UnionType> unionTable =
	// ((CompoundStatementInfo)scopeChoice.getInfo()).getTypeTable();
	// if (unionTable.containsKey(name)) {
	// return unionTable.get(name);
	// }
	// }
	// scopeChoice = Misc.getEnclosingBlock(scopeChoice);
	// }
	// return null;
	// }
	//
	// /**
	// * @param name
	// * @param node
	// * @return Enum entry for the given id named "name", when used in given
	// * "node". Returns null, if entry not found in any of the outer
	// * nesting parents of "node".
	// */
	// public static EnumType getEnumEntry(String name, Node node) {
	// if (node == null)
	// return null;
	//
	// Node scopeChoice = node;
	// while (scopeChoice != null) {
	// if (scopeChoice instanceof TranslationUnit) {
	// HashMap<String, EnumType> enumTable =
	// ((RootInfo)scopeChoice.getInfo()).getEnumTable();
	// if (enumTable.containsKey(name)) {
	// return enumTable.get(name);
	// }
	// } else if (scopeChoice instanceof FunctionDefinition) {
	// // Do nothing.
	// } else if (scopeChoice instanceof CompoundStatement) {
	// HashMap<String, EnumType> enumTable =
	// ((CompoundStatementInfo)scopeChoice.getInfo()).getEnumTable();
	// if (enumTable.containsKey(name)) {
	// return enumTable.get(name);
	// }
	// }
	// scopeChoice = Misc.getEnclosingBlock(scopeChoice);
	// }
	// return null;
	// }
	//
	// /**
	// * @param name
	// * @param node
	// * @return Struct entry for the given id named "name", when used in given
	// * "node". Returns null, if entry not found in any of the outer
	// * nesting parents of "node".
	// */
	// public static StructType getStructEntry(String name, Node node) {
	// if (node == null)
	// return null;
	//
	// Node scopeChoice = node;
	// while (scopeChoice != null) {
	// if (scopeChoice instanceof TranslationUnit) {
	// HashMap<String, StructType> structTable =
	// ((RootInfo)scopeChoice.getInfo()).getStructTable();
	// if (structTable.containsKey(name)) {
	// return structTable.get(name);
	// }
	// } else if (scopeChoice instanceof FunctionDefinition) {
	// // Do nothing.
	// } else if (scopeChoice instanceof CompoundStatement) {
	// HashMap<String, StructType> structTable =
	// ((CompoundStatementInfo)scopeChoice.getInfo()).getStructTable();
	// if (structTable.containsKey(name)) {
	// return structTable.get(name);
	// }
	// }
	// scopeChoice = Misc.getEnclosingBlock(scopeChoice);
	// }
	// return null;
	// }
	public static List<String> obtainVarNames(VariableList vL) {
		List<String> ids = new ArrayList<>();
		ids.add(vL.getF0().getTokenImage().trim());
		for (Node nodeSeqNode : vL.getF1().getNodes()) {
			NodeSequence nodeSeq = (NodeSequence) nodeSeqNode;
			NodeToken idNode = (NodeToken) nodeSeq.getNodes().get(1);
			ids.add(idNode.getTokenImage().trim());
		}
		return ids;
	}

	public static void addVarNames(VariableList vL, List<String> namesToBeAdded) {
		List<String> namesAlreadyPresent = Misc.obtainVarNames(vL);
		for (String id : namesToBeAdded) {
			if (namesAlreadyPresent.contains(id)) {
				continue;
			}
			NodeToken newIdToken = new NodeToken(id);
			newIdToken.setKind(CParserConstants.IDENTIFIER);
			NodeToken newComma = new NodeToken(",");
			newComma.setKind(CParserConstants.COMMA);
			List<Node> newNodes = new ArrayList<>();
			newNodes.add(newComma);
			newNodes.add(newIdToken);
			NodeSequence newNodeSeq = new NodeSequence(newNodes);
			vL.getF1().addNode(newNodeSeq);
		}
	}

	public static boolean isElementaryRWNodeParent(Node n) {
		if (isCFGLeafNode(n)) {
			return true;
		}
		if (n instanceof ParallelConstruct || n instanceof TaskConstruct) {
			return true;
		}
		return false;
	}

	/**
	 * An elementary node is the last level of nested node which we use for getting
	 * reads/writes, generally. If input is
	 * a ParallelConstruct, ParallelForConstruct, ParallelSectionsConstruct or
	 * TaskConstruct, then the corresponding
	 * non-CFG fields (f1,f3, f3, f2) are returned.
	 *
	 * @param n
	 *          A node whose elementary node has to be found.
	 *
	 * @return
	 */
	public static Node getElementaryRWNode(Node n) {
		if (!isCFGNode(n)) {
			return null;
		}
		if (isCFGLeafNode(n)) {
			return n;
		}
		if (n instanceof ParallelForConstruct) {
			return ((ParallelForConstruct) n).getF3();
		} else if (n instanceof ParallelSectionsConstruct) {
			return ((ParallelSectionsConstruct) n).getF3();
		} else if (n instanceof ParallelConstruct) {
			return ((ParallelConstruct) n).getParConsF1();
		} else if (n instanceof TaskConstruct) {
			return ((TaskConstruct) n).getF2();
		} else {
			// A non-Leaf CFG node. I don't know what to return.
			// Maybe I shall return null. This argument passed doesn't make
			// sense anyways.
			return null;
		}
	}

	public static NodePair getBlockNeighbours(Node sourceNode, Node destinationNode, PivotPoint pivotPoint) {
		// Remove the complexity of handling EndNode and BeginNode on one side of the
		// edge.
		if (sourceNode instanceof EndNode) {
			sourceNode = ((EndNode) sourceNode).getOwnerNestedCFG().getNode();
		}
		if (destinationNode instanceof BeginNode) {
			destinationNode = ((BeginNode) destinationNode).getOwnerNestedCFG().getNode();
		}

		List<Node> sourceSuccessors = sourceNode.getInfo().getCFGInfo().getSuccBlocks();
		if (sourceSuccessors.contains(destinationNode)) {
			return new NodePair(sourceNode, destinationNode);
		}

		if (pivotPoint == PivotPoint.DESTINATION) {
			// Try to move out the source.
			if (sourceSuccessors.size() != 1) {
				return new NodePair(null, null);
			}
			Node sourceSucc = sourceSuccessors.get(0);
			if (sourceSucc instanceof EndNode) {
				EndNode sourceSuccEndNode = (EndNode) sourceSucc;
				if (sourceSuccEndNode.getInfo().getCFGInfo().getPredBlocks().size() == 1) {
					return Misc.getBlockNeighbours(sourceSuccEndNode, destinationNode, pivotPoint);
				}
			}
			return new NodePair(null, null);
		} else {
			// Try to move out the destination.
			List<Node> destinationPredecessors = destinationNode.getInfo().getCFGInfo().getPredBlocks();
			if (destinationPredecessors.size() != 1) {
				return new NodePair(null, null);
			}
			Node destinationPred = destinationPredecessors.get(0);
			if (destinationPred instanceof BeginNode) {
				BeginNode destinationPredBeginNode = (BeginNode) destinationPred;
				if (destinationPredBeginNode.getInfo().getCFGInfo().getSuccBlocks().size() == 1) {
					return Misc.getBlockNeighbours(sourceNode, destinationPredBeginNode, pivotPoint);
				}
			}
			return new NodePair(null, null);
		}
	}

	public static <T> boolean areEqual(Set<T> A, Set<T> B) {
		if (A.size() != B.size()) {
			return false;
		}
		for (T elemA : A) {
			if (!B.contains(elemA)) {
				return false;
			}
		}
		return true;
	}

	public static <T> T removeOne(Set<T> A) {
		T ret = null;
		for (T obj : A) {
			ret = obj;
		}
		A.remove(ret);
		return ret;
	}

	/**
	 * Performs set union operation C = A U B;
	 */
	public static <T> Set<T> setUnion(Set<T> A, Set<T> B) {
		Set<T> C = new HashSet<>(A);
		C.addAll(B);
		// for (T obj : B) {
		// C.add(obj);
		// }
		return C;
	}

	/**
	 * Performs set union operation C = A - B;
	 */
	public static <T> Set<T> setMinus(Set<T> A, Set<T> B) {
		Set<T> C = new HashSet<>(A);
		for (T obj : B) {
			C.remove(obj);
		}
		return C;
	}

	public static <T> Set<T> setIntersection(Set<T> A, Set<T> B) {
		Set<T> C = new HashSet<>();
		for (T obj : A) {
			if (B.contains(obj)) {
				C.add(obj);
			}
		}
		return C;
	}

	public static <T> boolean doIntersect(Collection<T> A, Collection<T> B) {
		for (T elemA : A) {
			if (B.contains(elemA)) {
				return true;
			}
		}
		return false;
	}

	public static <T> boolean subsumes(Collection<T> A, Collection<T> B) {
		return A.containsAll(B);
		// for (T nodeInB : B) {
		// if (!A.contains(nodeInB)) {
		// return false;
		// }
		// }
		// return true;
	}

	/**
	 * This method returns true if newContext is same as the oldContext
	 *
	 * @param newCallSiteStack
	 * @param oldCallSiteStack
	 *
	 * @return
	 */
	public static boolean hasEqualCallSiteStacks(Stack<CallSite> newCallSiteStack, Stack<CallSite> oldCallSiteStack) {
		if (newCallSiteStack.size() != oldCallSiteStack.size()) {
			return false;
		}

		int index = oldCallSiteStack.size() - 1;
		while (index >= 0) {
			if (!newCallSiteStack.get(index).isEqual(oldCallSiteStack.get(index))) {
				return false;
			}
			index--;
		}

		return true;
	}

	/**
	 * @param decl
	 *             A declaration
	 * @param name
	 *             An identifier name
	 *
	 * @return true, if declaration "decl" declares an identifier named "name".
	 */
	public static boolean declaresIdName(Declaration decl, String name) {
		InitDeclaratorList initDeclList = (InitDeclaratorList) decl.getF1().getNode();
		if (initDeclList == null) {
			return false;
		}
		Declarator idDeclr = initDeclList.getF0().getF0();
		if (DeclarationInfo.getRootIdName(idDeclr).equals(name)) {
			return true;
		}
		for (Node seq : initDeclList.getF1().getNodes()) {
			assert seq instanceof NodeSequence;
			idDeclr = ((InitDeclarator) ((NodeSequence) seq).getNodes().get(1)).getF0();
			if (DeclarationInfo.getRootIdName(idDeclr).equals(name)) {
				return true;
			}
		}
		return false;
	}

	// /**
	// * @param declList
	// * A DeclarationSpecifiers list to be checked
	// * @return true, if declList contains no TypeSpecifier
	// */
	// public static boolean hasNoTypeSpecifier(Node declList) {
	// if (declList == null) {
	// return true;
	// }
	// HasTypeSpecifier hasTypeSpec = new HasTypeSpecifier();
	// declList.accept(hasTypeSpec);
	// return !(hasTypeSpec.foundTypeSpecifier);
	// }
	//
	// public static boolean hasVoidType(Node n) {
	// HasVoidType hasVoidType = new HasVoidType();
	// n.accept(hasVoidType);
	// if (hasVoidType.voidFound) {
	// return true;
	// }
	// return false;
	// }
	//
	// public static boolean hasLongType(Node n) {
	// HasLongType hasLongType = new HasLongType();
	// n.accept(hasLongType);
	// if (hasLongType.longFound) {
	// return true;
	// }
	// return false;
	// }
	//
	// /**
	// * @return true if the type contained is either char, short, int, etc
	// */
	// public static boolean hasIntType(Node n) {
	// HasIntType hasIntType = new HasIntType();
	// n.accept(hasIntType);
	// if (hasIntType.intFound) {
	// return true;
	// }
	// return false;
	// }
	//
	// public static boolean hasDoubleType(Node n) {
	// HasDoubleType hasDoubleType = new HasDoubleType();
	// n.accept(hasDoubleType);
	// if (hasDoubleType.doubleFound) {
	// return true;
	// }
	// return false;
	// }
	//
	// public static boolean hasFloatType(Node n) {
	// HasFloatType hasFloatType = new HasFloatType();
	// n.accept(hasFloatType);
	// if (hasFloatType.floatFound) {
	// return true;
	// }
	// return false;
	// }
	//
	// public static boolean hasUnsignedType(Node n) {
	// HasUnsignedType hasUnsignedType = new HasUnsignedType();
	// n.accept(hasUnsignedType);
	// if (hasUnsignedType.unsignedFound) {
	// return true;
	// }
	// return false;
	// }
	//
	// public static boolean hasStructOrUnionType(Node n) {
	// HasStructOrUnionType hasStructUnionOrEnumType = new HasStructOrUnionType();
	// n.accept(hasStructUnionOrEnumType);
	// if (hasStructUnionOrEnumType.structOrUnionFound) {
	// return true;
	// }
	// return false;
	// }
	//
	// public static boolean hasEnumType(Node n) {
	// HasEnumType hasEnumType = new HasEnumType();
	// n.accept(hasEnumType);
	// return hasEnumType.enumFound;
	// }

	// /**
	// * Returns true, if exp contains an operator associated with operatorClass
	// *
	// * @param exp
	// *
	// */
	// public static HashMap<Operator, Integer> countOperator(Expression exp) {
	// OperatorCounter opCounter = new OperatorCounter();
	// exp.accept(opCounter);
	// return opCounter.operatorCounter;
	// }
	//
	// /**
	// * Prints the map of operator to its count.
	// *
	// * @param countMap
	// */
	// public static void printOperatorCount(HashMap<Operator, Integer> countMap) {
	// for (Operator op : countMap.keySet()) {
	// System.out.println(op + "\t: " + countMap.get(op));
	// }
	// }

	/**
	 * Returns a buffered writer handle for the file with the specified
	 * <code>fileName</code> present in the current directory.
	 *
	 * @param fileName
	 *
	 * @return
	 */
	public static BufferedWriter getBufferedWriter(String fileName) {
		File file = new File(fileName);
		FileWriter fw = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fw = new FileWriter(file.getAbsoluteFile());
		} catch (IOException e) {
			return null;
		}
		BufferedWriter bw = new BufferedWriter(fw);
		return bw;
	}

	/**
	 * Returns an object of exact-type {@code Statement} that (inclusively) wraps
	 * {@code node}.<br>
	 * Note that this
	 * method doesn't create new AST ancestors for {@code node}, if they are already
	 * present.
	 *
	 * @param node
	 *             a node of a proper sub-type of Statement.
	 *
	 * @return a node of exact-type Statement, that wraps {@code node}.
	 */
	public static Statement getStatementWrapper(Statement node) {
		Statement stmt = Misc.getEnclosingNode(node, Statement.class);
		if (stmt == null) {
			if (node instanceof ExpressionStatement) {
				stmt = new Statement(new NodeChoice(node));
			} else if (node instanceof CallStatement) {
				stmt = new Statement(new NodeChoice(node));
			} else if (node instanceof CompoundStatement) {
				stmt = new Statement(new NodeChoice(node));
			} else if (node instanceof SelectionStatement) {
				SelectionStatement selStmt = Misc.getEnclosingNode(node, SelectionStatement.class);
				if (selStmt == null) {
					selStmt = new SelectionStatement(new NodeChoice(node));
				}
				stmt = new Statement(new NodeChoice(selStmt));
			} else if (node instanceof IterationStatement) {
				IterationStatement itStmt = Misc.getEnclosingNode(node, IterationStatement.class);
				if (itStmt == null) {
					itStmt = new IterationStatement(new NodeChoice(node));
				}
				stmt = new Statement(new NodeChoice(itStmt));
			} else if (node instanceof JumpStatement) {
				JumpStatement jmpStmt = Misc.getEnclosingNode(node, JumpStatement.class);
				if (jmpStmt == null) {
					jmpStmt = new JumpStatement(new NodeChoice(node));
				}
				stmt = new Statement(new NodeChoice(jmpStmt));
			} else if (node instanceof UnknownPragma) {
				stmt = new Statement(new NodeChoice(node));
			} else if (node instanceof OmpConstruct) {
				OmpConstruct ompConstruct = Misc.getEnclosingNode(node, OmpConstruct.class);
				if (ompConstruct == null) {
					ompConstruct = new OmpConstruct(new NodeChoice(node));
				}
				stmt = new Statement(new NodeChoice(ompConstruct));
			} else if (node instanceof OmpDirective) {
				OmpDirective ompDirective = Misc.getEnclosingNode(node, OmpDirective.class);
				if (ompDirective == null) {
					ompDirective = new OmpDirective(new NodeChoice(node));
				}
				stmt = new Statement(new NodeChoice(ompDirective));
			} else if (node instanceof UnknownCpp) {
				stmt = new Statement(new NodeChoice(node));
			} else {
				assert (false);
			}
		}
		return stmt;
	}

	/**
	 * Without changing any part of the tree above {@code node}, this method tries
	 * to return back the
	 * CompoundStatementElement object that wraps {@code node}.<br>
	 * If no such object exists, appropriate creation of
	 * missing nodes is performed.
	 *
	 * @param node
	 *             CFG node for which a wrapping CompoundStatementElement has to be
	 *             found.
	 *
	 * @return an existing or new CompoundStatementElement that wraps the
	 *         {@code node}.
	 */
	public static CompoundStatementElement getCompoundStatementElementWrapper(Node node) {
		// assert (Misc.isCFGNode(node));
		CompoundStatementElement wrappingCSE = Misc.getEnclosingNode(node, CompoundStatementElement.class);
		if (wrappingCSE != null) {
			// TODO: Make this efficient by considering a substitute of getEnclosingNode,
			// which should keep the string same before returning.
			if (wrappingCSE.toString().equals(node.toString())) {
				return wrappingCSE;
			}
		}
		if (node instanceof Statement) {
			Statement stmt = Misc.getEnclosingNode(node, Statement.class);
			if (stmt == null) {
				if (node instanceof ExpressionStatement) {
					stmt = new Statement(new NodeChoice(node));
				} else if (node instanceof CallStatement) {
					stmt = new Statement(new NodeChoice(node));
				} else if (node instanceof CompoundStatement) {
					stmt = new Statement(new NodeChoice(node));
				} else if (node instanceof SelectionStatement) {
					SelectionStatement selStmt = Misc.getEnclosingNode(node, SelectionStatement.class);
					if (selStmt == null) {
						selStmt = new SelectionStatement(new NodeChoice(node));
					}
					stmt = new Statement(new NodeChoice(selStmt));
				} else if (node instanceof IterationStatement) {
					IterationStatement itStmt = Misc.getEnclosingNode(node, IterationStatement.class);
					if (itStmt == null) {
						itStmt = new IterationStatement(new NodeChoice(node));
					}
					stmt = new Statement(new NodeChoice(itStmt));
				} else if (node instanceof JumpStatement) {
					JumpStatement jmpStmt = Misc.getEnclosingNode(node, JumpStatement.class);
					if (jmpStmt == null) {
						jmpStmt = new JumpStatement(new NodeChoice(node));
					}
					stmt = new Statement(new NodeChoice(jmpStmt));
				} else if (node instanceof UnknownPragma) {
					stmt = new Statement(new NodeChoice(node));
				} else if (node instanceof OmpConstruct) {
					OmpConstruct ompConstruct = Misc.getEnclosingNode(node, OmpConstruct.class);
					if (ompConstruct == null) {
						ompConstruct = new OmpConstruct(new NodeChoice(node));
					}
					stmt = new Statement(new NodeChoice(ompConstruct));
				} else if (node instanceof OmpDirective) {
					OmpDirective ompDirective = Misc.getEnclosingNode(node, OmpDirective.class);
					if (ompDirective == null) {
						ompDirective = new OmpDirective(new NodeChoice(node));
					}
					stmt = new Statement(new NodeChoice(ompDirective));
				} else if (node instanceof UnknownCpp) {
					stmt = new Statement(new NodeChoice(node));
				} else {
					assert (false);
				}
			}
			return new CompoundStatementElement(new NodeChoice(stmt));
		} else if (node instanceof Declaration) {
			return new CompoundStatementElement(new NodeChoice(node));
		} else {
			return null;
		}
	}

	/**
	 * This method takes a CFG node and returns the first leaf node encountered
	 * within it. Should be used to find the
	 * leaf successors (given n is a leaf/non-leaf successors). Note the behavior
	 * when n is a BeginNode or EndNode
	 *
	 * @param n
	 *
	 * @return
	 */
	public static List<Node> getFirstLeaves(Node n) {
		if (!isCFGNode(n)) {
			return null;
		}
		List<Node> leafList = new ArrayList<>();
		if (isCFGLeafNode(n)) {
			if (n instanceof BeginNode) {
				// Instead of BeginNode, return its successors.
				List<Node> beginSuccs = n.getInfo().getCFGInfo().getSuccBlocks();
				for (Node beginSucc : beginSuccs) {
					List<Node> retVal = getFirstLeaves(beginSucc);
					leafList.addAll(retVal);
				}
			} else if (n instanceof EndNode) {
				// Instead of EndNode, return its parent's successors.
				List<Node> endSuccs = n.getParent().getInfo().getCFGInfo().getSuccBlocks();
				for (Node endSucc : endSuccs) {
					List<Node> retVal = getFirstLeaves(endSucc);
					leafList.addAll(retVal);
				}
			} else {
				leafList.add(n);
			}
		} else if (isCFGNonLeafNode(n)) {
			Node beginNode = n.getInfo().getCFGInfo().getNestedCFG().getBegin();
			List<Node> beginSuccs = beginNode.getInfo().getCFGInfo().getSuccBlocks();
			for (Node beginSucc : beginSuccs) {
				List<Node> retVal = getFirstLeaves(beginSucc);
				leafList.addAll(retVal);
			}
		}
		return leafList;
	}

	/**
	 * This method returns the first CFG node encountered in the depth-first
	 * traversal of the AST rooted at
	 * <code>n</code> (inclusive).
	 *
	 * @param n
	 *          node in which the CFG node has to be found.
	 *
	 * @return the first CFG node encountered in the depth-first traversal of
	 *         <code>n</code>.
	 */
	public static Node getInternalFirstCFGNode(Node n) {
		if (n instanceof BeginNode || n instanceof EndNode) {
			return n;
		}
		InternalCFGNodeGetter internalVisitor = new InternalCFGNodeGetter();
		n.accept(internalVisitor);
		return internalVisitor.cfgNode;
	}

	public static enum TraverseExpressions {
		TRAVERSE_EXPRESSIONS, DONT_TRAVERSE_EXPRESSIONS
	}

	/**
	 * Returns the classId corresponding to the given class type.
	 *
	 * @param astType
	 *                any type of Node.
	 *
	 * @return classId corresponding to {@code astType}
	 */
	public static int getClassId(Class<? extends Node> astType) {
		int classId = -1;
		try {
			Method m = astType.getMethod("getClassId");
			classId = (int) m.invoke(astType.getConstructor(new Class<?>[] {}).newInstance(new Object[] {}));
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			if (astType == OmpClause.class) {
				classId = 467;
			} else if (astType == NodeListClass.class) {
				classId = 541;
			} else {
				e.printStackTrace();
				System.exit(0);
			}
		}
		assert (classId >= 0);
		return classId;
	}

	/**
	 * Returns the set of classIds for the given set of class types.
	 *
	 * @param astTypeSet
	 *                   set of any types of Node.
	 *
	 * @return set of classIds corresponding to the types in {@code astTypeSet}
	 */
	@SuppressWarnings("unchecked")
	public static Set<Integer> getClassIds(Set<Class<? extends Node>> astTypeSet) {
		Set<Integer> classIdSet = new HashSet<>();
		for (Class<?> astType : astTypeSet) {
			classIdSet.add(Misc.getClassId((Class<? extends Node>) astType));
		}
		return classIdSet;
	}

	/**
	 * This method returns a set of nodes of type $astType$, present within the AST
	 * of node $n$ (inclusive).
	 *
	 * @param n
	 *                node under which search needs to be performed
	 * @param astType
	 *                type of node whose assignable objects are to be searched for.
	 *
	 * @return set of nodes under {@code n} that are of type {@code astType}.
	 */
	public static <T extends Node> Set<T> getInheritedEnclosee(Node n, Class<T> astType) {
		return Misc.getInheritedEnclosee(n, astType, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static <T extends Node> Set<T> getInheritedEnclosee(Node n, Class<T> astType,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new HashSet<>();
		}
		return Misc.getInheritedEncloseeWithId(n, Misc.getClassId(astType), traverseExpressions);
	}

	/**
	 * This method returns a set of nodes of type with given {@code classId},
	 * present within the AST of node $n$
	 * (inclusive).
	 *
	 * @param n
	 *                node under which search needs to be performed
	 * @param classId
	 *                type of node whose assignable objects are to be searched for.
	 *
	 * @return set of nodes under {@code n} that are of type with given
	 *         {@code classId}.
	 */
	public static <T extends Node> Set<T> getInheritedEncloseeWithId(Node n, int classId) {
		return Misc.getInheritedEncloseeWithId(n, classId, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static <T extends Node> Set<T> getInheritedEncloseeWithId(Node n, int classId,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new HashSet<>();
		}
		// TODO: Enable this warning later.
		// if (classId == 897) {
		// Misc.warnDueToLackOfFeature("Handling of ParCons via
		// Misc.getInheritedEnclosee is not efficient. Use getExactEnclosee IF it is
		// correct to do so.", null);
		// Thread.dumpStack();
		// }
		if (classId % 3 == 0) {
			Collection<T> returnSet = new HashSet<>();
			if (Node.stmtSuperClassIds.contains(classId)) {
				InfiInheritedStatementGetter<T> nodeGetter = new InfiInheritedStatementGetter<>(classId, returnSet);
				n.accept(nodeGetter);
				return (Set<T>) returnSet;
			} else {
				InfiExactStatementGetter<T> nodeGetter = new InfiExactStatementGetter<>(classId, returnSet);
				n.accept(nodeGetter);
				return (Set<T>) returnSet;
			}
		}
		if (Node.superClassIds.contains(classId)) {
			InfiInheritedASTNodesGetter<T> nodeGetter = new InfiInheritedASTNodesGetter<>(classId, traverseExpressions);
			n.accept(nodeGetter);
			return nodeGetter.astContents;

		} else {
			return Misc.getExactEncloseeWithId(n, classId);
		}
	}

	/**
	 * This method returns a list of nodes of type $astType$, present within the AST
	 * of node $n$ (inclusive).
	 *
	 * @param n
	 *                node under which search needs to be performed
	 * @param astType
	 *                type of node whose assignable objects are to be searched for.
	 *
	 * @return list of nodes under {@code n} that are of type {@code astType}.
	 */
	public static <T extends Node> List<T> getInheritedEncloseeList(Node n, Class<T> astType) {
		return Misc.getInheritedEncloseeList(n, astType, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static <T extends Node> List<T> getInheritedEncloseeList(Node n, Class<T> astType,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new LinkedList<>();
		}
		return Misc.getInheritedEncloseeListWithId(n, Misc.getClassId(astType), traverseExpressions);
	}

	/**
	 * This method returns a list of nodes of type with given {@code classId},
	 * present within the AST of node $n$
	 * (inclusive).
	 *
	 * @param n
	 *                node under which search needs to be performed
	 * @param classId
	 *                type of node whose assignable objects are to be searched for.
	 *
	 * @return list of nodes under {@code n} that are of type with given
	 *         {@code classId}.
	 */
	public static <T extends Node> List<T> getInheritedEncloseeListWithId(Node n, int classId) {
		return Misc.getInheritedEncloseeListWithId(n, classId, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static <T extends Node> List<T> getInheritedEncloseeListWithId(Node n, int classId,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new LinkedList<>();
		}
		if (classId == 897) {
			Misc.warnDueToLackOfFeature(
					"Handling of ParCons via Misc.getInheritedEnclosee is not efficient. Use getExactEnclosee IF it is correct to do so.",
					null);
			Thread.dumpStack();
		}
		if (classId % 3 == 0) {
			Collection<T> returnList = new ArrayList<>();
			if (Node.stmtSuperClassIds.contains(classId)) {
				InfiInheritedStatementGetter<T> nodeGetter = new InfiInheritedStatementGetter<>(classId, returnList);
				n.accept(nodeGetter);
				return (List<T>) returnList;
			} else {
				InfiExactStatementGetter<T> nodeGetter = new InfiExactStatementGetter<>(classId, returnList);
				n.accept(nodeGetter);
				return (List<T>) returnList;
			}
		}
		if (Node.superClassIds.contains(classId)) {
			InfiInheritedASTNodeListGetter<T> nodeGetter = new InfiInheritedASTNodeListGetter<>(classId,
					traverseExpressions);
			n.accept(nodeGetter);
			return nodeGetter.astContents;
		} else {
			return Misc.getExactEncloseeListWithId(n, classId);
		}
	}

	/**
	 * This method returns a set of nodes of exact type $astType$, present within
	 * the AST of node $n$ (inclusive).
	 *
	 * @param n
	 *                node under which search needs to be performed
	 * @param astType
	 *                exact type of node whose assignable objects are to be searched
	 *                for.
	 *
	 * @return set of nodes under {@code n} that are of exact type {@code astType}.
	 */
	public static <T extends Node> Set<T> getExactEnclosee(Node n, Class<T> astType) {
		if (astType == CallStatement.class || astType == ParallelConstruct.class
				|| astType == DummyFlushDirective.class) {
			return ProgramElementExactCaches.getElements(astType);
		}

		return Misc.getExactEnclosee(n, astType, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static <T extends Node> Set<T> getExactEnclosee(Node n, Class<T> astType,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new HashSet<>();
		}
		return Misc.getExactEncloseeWithId(n, Misc.getClassId(astType), traverseExpressions);
	}

	/**
	 * This method returns a set of nodes of exact type with given {@code classId},
	 * present within the AST of node $n$
	 * (inclusive).
	 *
	 * @param n
	 *                node under which search needs to be performed
	 * @param classId
	 *                exact type of node whose assignable objects are to be searched
	 *                for.
	 *
	 * @return set of nodes under {@code n} that are of exact type with given
	 *         {@code classId}.
	 */
	public static <T extends Node> Set<T> getExactEncloseeWithId(Node n, int classId) {
		return Misc.getExactEncloseeWithId(n, classId, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static <T extends Node> Set<T> getExactEncloseeWithId(Node n, int classId,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new HashSet<>();
		}
		if (classId % 3 == 0) {
			Collection<T> returnSet = new HashSet<>();
			InfiExactStatementGetter<T> nodeGetter = new InfiExactStatementGetter<>(classId, returnSet);
			n.accept(nodeGetter);
			return (Set<T>) returnSet;
		}
		InfiExactASTNodesGetter<T> nodeGetter = new InfiExactASTNodesGetter<>(classId, traverseExpressions);
		n.accept(nodeGetter);
		return nodeGetter.astContents;
	}

	/**
	 * This method returns a list of nodes of exact type $astType$, present within
	 * the AST of node $n$ (inclusive).
	 *
	 * @param n
	 *                node under which search needs to be performed
	 * @param astType
	 *                exact type of node whose assignable objects are to be searched
	 *                for.
	 *
	 * @return list of nodes under {@code n} that are of exact type {@code astType}.
	 */
	public static <T extends Node> List<T> getExactEncloseeList(Node n, Class<T> astType) {
		return Misc.getExactEncloseeList(n, astType, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static <T extends Node> List<T> getExactEncloseeList(Node n, Class<T> astType,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new LinkedList<>();
		}
		return Misc.getExactEncloseeListWithId(n, Misc.getClassId(astType), traverseExpressions);
	}

	/**
	 * This method returns a list of nodes of exact type with given {@code classId},
	 * present within the AST of node $n$
	 * (inclusive).
	 *
	 * @param n
	 *                node under which search needs to be performed
	 * @param classId
	 *                exact type of node whose assignable objects are to be searched
	 *                for.
	 *
	 * @return list of nodes under {@code n} that are of exact type with given
	 *         {@code classId}.
	 */
	public static <T extends Node> List<T> getExactEncloseeListWithId(Node n, int classId) {
		return Misc.getExactEncloseeListWithId(n, classId, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static <T extends Node> List<T> getExactEncloseeListWithId(Node n, int classId,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new LinkedList<>();
		}
		if (classId % 3 == 0) {
			Collection<T> returnList = new ArrayList<>();
			InfiExactStatementGetter<T> nodeGetter = new InfiExactStatementGetter<>(classId, returnList);
			n.accept(nodeGetter);
			return (List<T>) returnList;
		}
		InfiExactASTNodeListGetter<T> nodeGetter = new InfiExactASTNodeListGetter<>(classId, traverseExpressions);
		n.accept(nodeGetter);
		return nodeGetter.astContents;
	}

	/**
	 * This method returns a set of nodes of any of the types in $astType$, present
	 * within the AST of node $n$
	 * (inclusive).
	 *
	 * @param n
	 *                   node under which search needs to be performed
	 * @param astTypeSet
	 *                   set of type of nodes whose assignable objects are to be
	 *                   searched for.
	 *
	 * @return set of nodes under {@code n} that are of any of the types in
	 *         {@code astTypeSet}.
	 */
	public static Set<Node> getInheritedEnclosee(Node n, Set<Class<? extends Node>> astTypeSet) {
		return Misc.getInheritedEnclosee(n, astTypeSet, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static Set<Node> getInheritedEnclosee(Node n, Set<Class<? extends Node>> astTypeSet,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new HashSet<>();
		}
		return Misc.getInheritedEncloseeWithId(n, Misc.getClassIds(astTypeSet), traverseExpressions);
	}

	/**
	 * This method returns a set of nodes of any of the types with classId in
	 * $classIds$, present within the AST of node
	 * $n$ (inclusive).
	 *
	 * @param n
	 *                 node under which search needs to be performed
	 * @param classIds
	 *                 set of classId of types whose assignable objects are to be
	 *                 searched for.
	 *
	 * @return set of nodes under {@code n} that are of any of the types with
	 *         classId in {@code classIds}.
	 */
	public static Set<Node> getInheritedEncloseeWithId(Node n, Set<Integer> classIds) {
		return Misc.getInheritedEncloseeWithId(n, classIds, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static Set<Node> getInheritedEncloseeWithId(Node n, Set<Integer> classIds,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new HashSet<>();
		}
		boolean notAStmtType = false;
		boolean hasASuperStmt = false;
		for (int i : classIds) {
			if (i % 3 != 0) {
				notAStmtType = true;
			}
			if (Node.stmtSuperClassIds.contains(i)) {
				hasASuperStmt = true;
			}
		}
		if (notAStmtType) {
			if (Misc.doIntersect(Node.superClassIds, classIds)) {
				InfiInheritedMultiASTNodesGetter nodeGetter = new InfiInheritedMultiASTNodesGetter(classIds,
						traverseExpressions);
				n.accept(nodeGetter);
				return nodeGetter.astContents;
			} else {
				return Misc.getExactEncloseeWithId(n, classIds);
			}
		} else {
			Collection<Node> returnSet = new HashSet<>();
			if (hasASuperStmt) {
				InfiInheritedMultiTypeStatementGetter<Node> nodeGetter = new InfiInheritedMultiTypeStatementGetter<>(
						classIds, returnSet);
				n.accept(nodeGetter);
				return (Set<Node>) returnSet;
			} else {
				InfiExactMultiTypeStatementGetter<Node> nodeGetter = new InfiExactMultiTypeStatementGetter<>(classIds,
						returnSet);
				n.accept(nodeGetter);
				return (Set<Node>) returnSet;

			}
		}
	}

	/**
	 * This method returns a list of nodes of any of the types in $astType$, present
	 * within the AST of node $n$
	 * (inclusive).
	 *
	 * @param n
	 *                   node under which search needs to be performed
	 * @param astTypeSet
	 *                   set of type of nodes whose assignable objects are to be
	 *                   searched for.
	 *
	 * @return list of nodes under {@code n} that are of any of the types in
	 *         {@code astTypeSet}.
	 */
	public static List<Node> getInheritedEncloseeList(Node n, Set<Class<? extends Node>> astTypeSet) {
		return Misc.getInheritedEncloseeList(n, astTypeSet, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static List<Node> getInheritedEncloseeList(Node n, Set<Class<? extends Node>> astTypeSet,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new LinkedList<>();
		}
		return Misc.getInheritedEncloseeListWithId(n, Misc.getClassIds(astTypeSet), traverseExpressions);
	}

	/**
	 * This method returns a list of nodes of any of the types with classId in
	 * $classIds$, present within the AST of
	 * node $n$ (inclusive).
	 *
	 * @param n
	 *                 node under which search needs to be performed
	 * @param classIds
	 *                 set of classId of types whose assignable objects are to be
	 *                 searched for.
	 *
	 * @return list of nodes under {@code n} that are of any of the types with
	 *         classId in {@code classIds}.
	 */
	public static List<Node> getInheritedEncloseeListWithId(Node n, Set<Integer> classIds) {
		return Misc.getInheritedEncloseeListWithId(n, classIds, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static List<Node> getInheritedEncloseeListWithId(Node n, Set<Integer> classIds,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new LinkedList<>();
		}
		boolean notAStmtType = false;
		boolean hasASuperStmt = false;
		for (int i : classIds) {
			if (i % 3 != 0) {
				notAStmtType = true;
			}
			if (Node.stmtSuperClassIds.contains(i)) {
				hasASuperStmt = true;
			}
		}
		if (notAStmtType) {
			if (Misc.doIntersect(Node.superClassIds, classIds)) {
				InfiInheritedMultiASTNodeListGetter nodeGetter = new InfiInheritedMultiASTNodeListGetter(classIds,
						traverseExpressions);
				n.accept(nodeGetter);
				return nodeGetter.astContents;
			} else {
				return Misc.getExactEncloseeListWithId(n, classIds);
			}
		} else {
			Collection<Node> returnList = new ArrayList<>();
			if (hasASuperStmt) {
				InfiInheritedMultiTypeStatementGetter<Node> nodeGetter = new InfiInheritedMultiTypeStatementGetter<>(
						classIds, returnList);
				n.accept(nodeGetter);
				return (List<Node>) returnList;
			} else {
				InfiExactMultiTypeStatementGetter<Node> nodeGetter = new InfiExactMultiTypeStatementGetter<>(classIds,
						returnList);
				n.accept(nodeGetter);
				return (List<Node>) returnList;

			}
		}
	}

	/**
	 * This method returns a set of nodes of any of the exact-types in $astType$,
	 * present within the AST of node $n$
	 * (inclusive).
	 *
	 * @param n
	 *                   node under which search needs to be performed
	 * @param astTypeSet
	 *                   set of exact-type of nodes whose assignable objects are to
	 *                   be searched for.
	 *
	 * @return set of nodes under {@code n} that are of any of the exact-types in
	 *         {@code astTypeSet}.
	 */
	public static Set<Node> getExactEnclosee(Node n, Set<Class<? extends Node>> astTypeSet) {
		return Misc.getExactEnclosee(n, astTypeSet, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static Set<Node> getExactEnclosee(Node n, Set<Class<? extends Node>> astTypeSet,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new HashSet<>();
		}
		return Misc.getExactEncloseeWithId(n, Misc.getClassIds(astTypeSet), traverseExpressions);
	}

	/**
	 * This method returns a set of nodes of any of the exact-types with classId in
	 * $classIds$, present within the AST
	 * of node $n$ (inclusive).
	 *
	 * @param n
	 *                 node under which search needs to be performed
	 * @param classIds
	 *                 set of classId of exact-types whose assignable objects are to
	 *                 be searched for.
	 *
	 * @return set of nodes under {@code n} that are of any of the exact-types with
	 *         classId in {@code classIds}.
	 */
	public static Set<Node> getExactEncloseeWithId(Node n, Set<Integer> classIds) {
		return Misc.getExactEncloseeWithId(n, classIds, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static Set<Node> getExactEncloseeWithId(Node n, Set<Integer> classIds,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new HashSet<>();
		}
		boolean notAStmtType = false;
		for (int i : classIds) {
			if (i % 3 != 0) {
				notAStmtType = true;
				break;
			}
		}
		if (notAStmtType) {
			InfiExactMultiTypeASTNodesGetter nodeGetter = new InfiExactMultiTypeASTNodesGetter(classIds,
					traverseExpressions);
			n.accept(nodeGetter);
			return nodeGetter.astContents;
		} else {
			Collection<Node> returnSet = new HashSet<>();
			InfiExactMultiTypeStatementGetter<Node> nodeGetter = new InfiExactMultiTypeStatementGetter<>(classIds,
					returnSet);
			n.accept(nodeGetter);
			return (Set<Node>) returnSet;

		}
	}

	/**
	 * This method returns a list of nodes of any of the exact-types in $astType$,
	 * present within the AST of node $n$
	 * (inclusive).
	 *
	 * @param n
	 *                   node under which search needs to be performed
	 * @param astTypeSet
	 *                   set of exact-type of nodes whose assignable objects are to
	 *                   be searched for.
	 *
	 * @return list of nodes under {@code n} that are of any of the exact-types in
	 *         {@code astTypeSet}.
	 */
	public static List<Node> getExactEncloseeList(Node n, Set<Class<? extends Node>> astTypeSet) {
		return Misc.getExactEncloseeList(n, astTypeSet, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static List<Node> getExactEncloseeList(Node n, Set<Class<? extends Node>> astTypeSet,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new LinkedList<>();
		}
		return Misc.getExactEncloseeListWithId(n, Misc.getClassIds(astTypeSet), traverseExpressions);
	}

	/**
	 * This method returns a list of nodes of any of the exact-types with classId in
	 * $classIds$, present within the AST
	 * of node $n$ (inclusive).
	 *
	 * @param n
	 *                 node under which search needs to be performed
	 * @param classIds
	 *                 set of classId of exact-types whose assignable objects are to
	 *                 be searched for.
	 *
	 * @return list of nodes under {@code n} that are of any of the exact-types with
	 *         classId in {@code classIds}.
	 */
	public static List<Node> getExactEncloseeListWithId(Node n, Set<Integer> classIds) {
		return Misc.getExactEncloseeListWithId(n, classIds, TraverseExpressions.TRAVERSE_EXPRESSIONS);
	}

	public static List<Node> getExactEncloseeListWithId(Node n, Set<Integer> classIds,
			TraverseExpressions traverseExpressions) {
		if (n == null) {
			return new LinkedList<>();
		}
		boolean notAStmtType = false;
		for (int i : classIds) {
			if (i % 3 != 0) {
				notAStmtType = true;
				break;
			}
		}
		if (notAStmtType) {
			InfiExactMultiTypeASTNodeListGetter nodeGetter = new InfiExactMultiTypeASTNodeListGetter(classIds,
					traverseExpressions);
			n.accept(nodeGetter);
			return nodeGetter.astContents;
		} else {
			Collection<Node> returnList = new ArrayList<>();
			InfiExactMultiTypeStatementGetter<Node> nodeGetter = new InfiExactMultiTypeStatementGetter<>(classIds,
					returnList);
			n.accept(nodeGetter);
			return (List<Node>) returnList;
		}
	}

	/**
	 * This method returns a pre-order list of nodes of type $astType$, present
	 * within the AST of node $n$ (inclusive).
	 *
	 * @param n
	 *                node under which search needs to be performed
	 * @param astType
	 *                type of node whose assignable objects are to be searched for.
	 *
	 * @return pre-order list of nodes under {@code n} that are of type
	 *         {@code astType}.
	 */
	public static <T extends Node> List<T> getInheritedPostOrderEnclosee(Node n, Class<T> astType) {
		return Misc.getInheritedEncloseeList(n, astType);
	}

	/**
	 * This method returns a pre-order list of nodes of any of the types in
	 * $astType$, present within the AST of node
	 * $n$ (inclusive).
	 *
	 * @param n
	 *                   node under which search needs to be performed
	 * @param astTypeSet
	 *                   set of type of nodes whose assignable objects are to be
	 *                   searched for.
	 *
	 * @return pre-order list of nodes under {@code n} that are of any of the types
	 *         in {@code astTypeSet}.
	 */
	public static List<Node> getInheritedPostOrderEnclosee(Node n, Set<Class<? extends Node>> astTypeSet) {
		return Misc.getInheritedEncloseeList(n, astTypeSet);
	}

	/**
	 * This method returns a pre-order list of nodes of exact-type $astType$,
	 * present within the AST of node $n$
	 * (inclusive).
	 *
	 * @param n
	 *                node under which search needs to be performed
	 * @param astType
	 *                exact-type of node whose assignable objects are to be searched
	 *                for.
	 *
	 * @return pre-order list of nodes under {@code n} that are of exact-type
	 *         {@code astType}.
	 */
	public static <T extends Node> List<T> getExactPostOrderEnclosee(Node n, Class<T> astType) {
		return Misc.getExactEncloseeList(n, astType);
	}

	/**
	 * This method returns a pre-order list of nodes of any of the exact-types in
	 * $astType$, present within the AST of
	 * node $n$ (inclusive).
	 *
	 * @param n
	 *                   node under which search needs to be performed
	 * @param astTypeSet
	 *                   set of exact-type of nodes whose assignable objects are to
	 *                   be searched for.
	 *
	 * @return pre-order list of nodes under {@code n} that are of any of the
	 *         exact-types in {@code astTypeSet}.
	 */
	public static List<Node> getExactPostOrderEnclosee(Node n, Set<Class<? extends Node>> astTypeSet) {
		return Misc.getExactEncloseeList(n, astTypeSet);
	}

	/**
	 * This method returns a vector of nodes representing case statements and
	 * default statements in the current level.
	 * In case of nested switches, the internal cases are not returned.
	 *
	 * @param n
	 *          switch statement for which the list of cases and default are to be
	 *          found.
	 *
	 * @return list of case/default statements for {@code switchStmt}
	 *
	 * @deprecated Use
	 *             {@link SwitchStatementInfo#getCaseDefaultLabelStatementList()}
	 *             instead.
	 */
	@Deprecated
	public static List<Node> getCaseStatements(SwitchStatement switchStmt) {
		List<Node> caseStatements = new ArrayList<>();
		for (Statement tempNode : switchStmt.getInfo().getRelevantCFGStatements()) {
			StatementInfo stmtInfo = tempNode.getInfo();
			if (!stmtInfo.hasLabelAnnotations()) {
				continue;
			}
			List<Label> labelList = stmtInfo.getLabelAnnotations();
			for (Label l : labelList) {
				if (l instanceof CaseLabel || l instanceof DefaultLabel) {
					caseStatements.add(tempNode);
					break;
				}
			}
		}
		return caseStatements;
	}

	/**
	 * This method returns the column number of the first token encountered in the
	 * node n.
	 *
	 * @param n
	 *
	 * @return
	 */
	public static int getColumnNum(Node n) {
		LineNumGetter lineGetter = new LineNumGetter();
		n.accept(lineGetter);
		return lineGetter.columnNum;
	}

	/**
	 * This method returns the line number of the first token encountered in the
	 * node n.
	 *
	 * @param n
	 *
	 * @return
	 */
	public static int getLineNum(Node n) {
		if (Program.disableLineNumbers) {
			return -1;
		}
		LineNumGetter lineGetter = new LineNumGetter();
		n.accept(lineGetter);
		return lineGetter.lineNum;
	}

	/**
	 * Returns all semantically enclosing beginNodes for the given node.
	 *
	 * @param n
	 *          a node.
	 *
	 * @return all semantically (CFG) enclosing {@link BeginNode} nodes.
	 */
	public static Set<BeginNode> getCFGEnclosingBeginNodes(Node n) {
		Set<BeginNode> retSet = new HashSet<>();
		if (n instanceof BeginNode) {
			return Misc.getCFGEnclosingBeginNodes(n.getParent());
		} else if (n instanceof FunctionDefinition) {
			FunctionDefinition func = (FunctionDefinition) n;
			for (CallStatement callStmt : func.getInfo().getCallersOfThis()) {
				retSet.add(callStmt.getInfo().getCFGInfo().getNestedCFG().getBegin());
			}
			return retSet;
		} else {
			Node parent = Misc.getEnclosingCFGNonLeafNode(n);
			assert (parent != null);
			retSet.add(parent.getInfo().getCFGInfo().getNestedCFG().getBegin());
			return retSet;
		}
	}

	/**
	 * This method returns the enclosing CFG node of <code>n</code>.
	 *
	 * @param n
	 *
	 * @return
	 */
	public static Node getEnclosingCFGNodeInclusive(Node n) {
		if (n == null) {
			return null;
		}
		if (n instanceof TranslationUnit) {
			// System.out.println("Some issue in getting the least upper CFG
			// node.. Exiting");
			return null;
		}
		if (Misc.isCFGNode(n)) {
			return n;
		} else {
			return getEnclosingCFGNodeInclusive(n.getParent());
		}
	}

	/**
	 * This method returns the enclosing non-leaf CFG node of n (exclusive).
	 *
	 * @param n
	 *
	 * @return
	 */
	public static Node getEnclosingCFGNonLeafNode(Node n) {
		if (n == null) {
			return null;
		}
		if (n instanceof TranslationUnit) {
			return null;
		}
		Node parent = n.getParent();
		if (Misc.isCFGNonLeafNode(parent)) {
			return parent;
		} else {
			return getEnclosingCFGNonLeafNode(parent);
		}
	}

	/**
	 * @param n
	 *                  an AST/CFG node whose enclosing node has to be found.
	 * @param classList
	 *                  set of possible class types of the enclosing node.
	 *
	 * @return null if there is no enclosing node of class type $c$ for node $n$.
	 */
	public static Node getEnclosingNode(Node n, Set<Class<?>> classList) {
		if (n == null) {
			return null;
		}
		if (classList.contains(n.getClass())) {
			return n;
		}
		/*
		 * Commented code below won't work due to inheritance hierarchy in the
		 * AST classes.
		 */
		// if(c.isInstance(n)) {
		// return n;
		// }
		if (n.getParent() == null) {
			// StringBuilder nameList = new StringBuilder();
			// for (Class c : classList) {
			// nameList.append(c.getName() + "; ");
			// }
			// System.out.println("No owner node of any of the types (" +
			// nameList.toString() + ") found for node "
			// + n.getClass().getSimpleName());
			return null;
		}
		return getEnclosingNode(n.getParent(), classList);
	}

	/**
	 * @param n
	 *          an AST/CFG node whose enclosing node (inclusive) has to be found.
	 * @param c
	 *          an exact class type for the enclosing node.
	 *
	 * @return {@code null} if there is no enclosing node of class type {@code c}
	 *         for node {@code n}.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getEnclosingNode(Node n, Class<T> c) {
		if (n == null) {
			return null;
		}
		if (n.getClass() == c) {
			return (T) n;
		}
		/*
		 * Commented code below won't work due to inheritance hierarchy in the
		 * AST classes.
		 */
		// if(c.isInstance(n)) {
		// return n;
		// }
		if (n.getParent() == null) {
			// System.out.println(
			// "No owner node of type " + c.getSimpleName() + " found for node " +
			// n.getClass().getSimpleName());
			return null;
		}
		return getEnclosingNode(n.getParent(), c);
	}

	/**
	 * This method returns the enclosing function for a given node (exclusively).
	 *
	 * @param n
	 *
	 * @return
	 */
	public static FunctionDefinition getEnclosingFunction(Node n) {
		if (n == null) {
			return null;
		}
		Node parent = n.getParent();

		while (parent != null) {
			if (parent instanceof FunctionDefinition) {
				return (FunctionDefinition) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}

	public static Node getEnclosingLoopOrForConstruct(Node n) {
		if (n == null) {
			return null;
		}
		Node parent = n.getParent();
		if (parent == null) {
			// System.out.println("No owner loop found for node " +
			// n.getClass().getSimpleName());
			return null;
		}
		if (parent instanceof IterationStatement || parent instanceof ForConstruct) {
			return parent;
		}

		return getEnclosingLoopOrForConstruct(parent);
	}

	/**
	 * This method returns the enclosing switch for given statement (exclusively).
	 *
	 * @param n
	 *
	 * @return
	 */
	public static SwitchStatement getEnclosingSwitch(Node n) {
		if (n == null) {
			return null;
		}
		Node parent = n.getParent();
		if (parent == null) {
			return null;
		}
		if (parent instanceof SwitchStatement) {
			return (SwitchStatement) parent;
		}

		return getEnclosingSwitch(parent);
	}

	/**
	 * This method returns the enclosing loop or switch for the given statement
	 * (exclusively).
	 *
	 * @param n
	 *
	 * @return
	 */
	public static Node getEnclosingLoopOrSwitch(Node n) {
		if (n == null) {
			return null;
		}
		Node parent = n.getParent();
		if (parent == null) {
			// System.out.println("No owner loop/switch found for node " +
			// n.getClass().getSimpleName());
			return null;
		}
		if (parent instanceof IterationStatement || parent instanceof SwitchStatement) {
			return parent;
		}

		return getEnclosingLoopOrSwitch(parent);
	}

	/**
	 * This method returns the enclosing CompoundStatement, FunctionDefinition or
	 * TranslationUnit of a given node
	 * (exclusively).
	 *
	 * @param n
	 *
	 * @return
	 */
	public static Scopeable getEnclosingBlock(Node n) {
		if (n == null) {
			return null;
		}
		Node parent = n.getParent();
		assert (parent != n);
		if (parent == null) {
			return null;
		}
		if (parent instanceof TranslationUnit || parent instanceof FunctionDefinition
				|| parent instanceof CompoundStatement) {
			return (Scopeable) parent;
		}
		return getEnclosingBlock(parent);
	}

	/**
	 * // * @param name
	 * // * @param node
	 * // * @return Union entry for the given id named "name", when used in
	 * given
	 * // * "node". Returns null, if entry not found in any of the outer
	 * // * nesting parents of "node".
	 * //
	 */
	// public static UnionType getUnionEntry(String name, Node node) {
	// if (node == null)
	// return null;
	//
	// Node scopeChoice = node;
	// while (scopeChoice != null) {
	// if (scopeChoice instanceof TranslationUnit) {
	// HashMap<String, UnionType> typeTable =
	// ((RootInfo)scopeChoice.getInfo()).getTypeTable();
	// if (typeTable.containsKey(name)) {
	// return typeTable.get(name);
	// }
	// } else if (scopeChoice instanceof FunctionDefinition) {
	// // Do nothing.
	// } else if (scopeChoice instanceof CompoundStatement) {
	// HashMap<String, UnionType> unionTable =
	// ((CompoundStatementInfo)scopeChoice.getInfo()).getTypeTable();
	// if (unionTable.containsKey(name)) {
	// return unionTable.get(name);
	// }
	// }
	// scopeChoice = Misc.getEnclosingBlock(scopeChoice);
	// }
	// return null;
	// }
	//
	// /**
	// * @param name
	// * @param node
	// * @return Enum entry for the given id named "name", when used in given
	// * "node". Returns null, if entry not found in any of the outer
	// * nesting parents of "node".
	// */
	// public static EnumType getEnumEntry(String name, Node node) {
	// if (node == null)
	// return null;
	//
	// Node scopeChoice = node;
	// while (scopeChoice != null) {
	// if (scopeChoice instanceof TranslationUnit) {
	// HashMap<String, EnumType> enumTable =
	// ((RootInfo)scopeChoice.getInfo()).getEnumTable();
	// if (enumTable.containsKey(name)) {
	// return enumTable.get(name);
	// }
	// } else if (scopeChoice instanceof FunctionDefinition) {
	// // Do nothing.
	// } else if (scopeChoice instanceof CompoundStatement) {
	// HashMap<String, EnumType> enumTable =
	// ((CompoundStatementInfo)scopeChoice.getInfo()).getEnumTable();
	// if (enumTable.containsKey(name)) {
	// return enumTable.get(name);
	// }
	// }
	// scopeChoice = Misc.getEnclosingBlock(scopeChoice);
	// }
	// return null;
	// }
	//
	// /**
	// * @param name
	// * @param node
	// * @return Struct entry for the given id named "name", when used in given
	// * "node". Returns null, if entry not found in any of the outer
	// * nesting parents of "node".
	// */
	// public static StructType getStructEntry(String name, Node node) {
	// if (node == null)
	// return null;
	//
	// Node scopeChoice = node;
	// while (scopeChoice != null) {
	// if (scopeChoice instanceof TranslationUnit) {
	// HashMap<String, StructType> structTable =
	// ((RootInfo)scopeChoice.getInfo()).getStructTable();
	// if (structTable.containsKey(name)) {
	// return structTable.get(name);
	// }
	// } else if (scopeChoice instanceof FunctionDefinition) {
	// // Do nothing.
	// } else if (scopeChoice instanceof CompoundStatement) {
	// HashMap<String, StructType> structTable =
	// ((CompoundStatementInfo)scopeChoice.getInfo()).getStructTable();
	// if (structTable.containsKey(name)) {
	// return structTable.get(name);
	// }
	// }
	// scopeChoice = Misc.getEnclosingBlock(scopeChoice);
	// }
	// return null;
	// }

	// /**
	// * Performs usual arithmetic conversion on type <code>one</code> and
	// * <code>two</code>,
	// * and returns the resulting type.
	// *
	// * @param one
	// * one of the types.
	// * @param two
	// * one of the types.
	// * @return
	// * resulting type upon performing usual arithmetic conversion on
	// * type <code>one</code> and <code>two</code>.
	// */
	// public static Type_obsolete performArithmeticConversion(Type_obsolete one,
	// Type_obsolete two) {
	// if (one == null) {
	// return two;
	// } else if (two == null) {
	// return one;
	// }
	// Type_obsolete returnType;
	// DeclarationSpecifiers declSpecOne = one.declarationSpecifiers;
	// DeclarationSpecifiers declSpecTwo = two.declarationSpecifiers;
	// DeclarationSpecifiers declSpecRet =
	// Misc.performArithmeticConversion(declSpecOne, declSpecTwo);
	// returnType = new Type_obsolete(declSpecRet, one.abstractOptionalDeclarator);
	// return returnType;
	// }

	// /**
	// * @param one
	// * A DeclarationSpecifier
	// * @param two
	// * A DeclarationSpecifier
	// * @return A DeclarationSpecifier which is the type promoted version of one
	// * and two.
	// */
	// public static DeclarationSpecifiers
	// performArithmeticConversion(DeclarationSpecifiers one,
	// DeclarationSpecifiers two) {
	// boolean isLong = false;
	// boolean isDouble = false;
	// boolean isFloat = false;
	// boolean isUnsigned = false;
	// boolean isInt = false;
	// boolean isStructUnionOrEnumType = false;
	// if ((hasLongType(one) && hasDoubleType(one)) || (hasLongType(two) &&
	// hasDoubleType(two))) {
	// isLong = true;
	// isDouble = true;
	// } else if (hasDoubleType(one) || hasDoubleType(two)) {
	// isDouble = true;
	// } else if (hasFloatType(one) || hasFloatType(two)) {
	// isFloat = true;
	// } else if ((hasLongType(one) && hasIntType(one) && hasUnsignedType(one))
	// || (hasLongType(two) && hasIntType(two) && hasUnsignedType(two))) {
	// isLong = true;
	// isInt = true;
	// isUnsigned = true;
	// } else if ((hasLongType(one) && hasIntType(one)) || (hasLongType(two) &&
	// hasIntType(two))) {
	// /*
	// * TODO: This is an approximate conversion, given that we are not
	// * considering the range of types.
	// * Later, we might want to perform conversions based on the range of
	// * values that a type may hold, with the help of limits.h.
	// */
	// isLong = true;
	// isInt = true;
	// } else if ((hasIntType(one) && hasUnsignedType(one)) || (hasIntType(two) &&
	// hasUnsignedType(two))) {
	// isInt = true;
	// isUnsigned = true;
	// } else if (hasStructOrUnionType(one) || hasEnumType(one) ||
	// hasStructOrUnionType(two) || hasEnumType(two)) {
	// isStructUnionOrEnumType = true;
	// } else {
	// isInt = true;
	// }
	// String typeSpecifierString = new String();
	// if (isUnsigned)
	// typeSpecifierString += "unsigned ";
	// if (isLong)
	// typeSpecifierString += "long ";
	// if (isDouble)
	// typeSpecifierString += "double ";
	// if (isFloat)
	// typeSpecifierString += "float ";
	// if (isInt)
	// typeSpecifierString += "int ";
	//
	// if (isStructUnionOrEnumType == false) {
	// return (DeclarationSpecifiers)
	// CParser.createCrudeASTNode(typeSpecifierString, DeclarationSpecifiers.class);
	// }
	// return null;
	// }

	// /**
	// * Ensures that the Type has no TypedefName's
	// *
	// * @param paraName
	// * @param funcDef
	// * @return an object of type Type, for the given parameter name in the
	// * function definition.
	// */
	// public static Type_obsolete getTypeFromParameter(FunctionDefinition funcDef,
	// String paramName) {
	// String declSpecString = new String();
	// String absOptDeclString = new String();
	// ExpandedTypeStringGetter typeStringGetter = new
	// ExpandedTypeStringGetter(paramName);
	//
	// // Check whether the parameter declarations are new-style or old
	// // UPDATE 25/08: They will be new-style always
	// ADeclaratorOp firstDeclOpChoice = (ADeclaratorOp)
	// funcDef.f1.f1.f1.f0.nodes.get(0);
	// Node firstDeclOp = firstDeclOpChoice.f0.choice;
	// if (firstDeclOp instanceof ParameterTypeListClosed) { // This is a
	// // new-style
	// // parameter
	// // list
	// // Obtain the parameterDeclaration for paramName
	// if (((ParameterTypeListClosed) firstDeclOp).f1.node == null)
	// return null;
	// ParameterTypeList paramTypeList = (ParameterTypeList)
	// ((ParameterTypeListClosed) firstDeclOp).f1.node;
	// ParameterDeclaration paraDecl =
	// getParameterDeclarationFromParameterList(paramTypeList.f0, paramName);
	//
	// // Obtain Type from ParameterDeclaration and return
	// paraDecl.accept(typeStringGetter);
	// declSpecString = typeStringGetter.declString;
	// absOptDeclString = typeStringGetter.absOptDeclPre + " " +
	// typeStringGetter.absOptDeclPost;
	// } else {
	// System.out.println(
	// "Exiting for " + paramName + ": " + ((FunctionDefinitionInfo)
	// funcDef.getInfo()).getFunctionName());
	// Thread.dumpStack();
	// System.exit(0);
	// }
	// DeclarationSpecifiers declSpec = (DeclarationSpecifiers)
	// CParser.createCrudeASTNode(declSpecString,
	// DeclarationSpecifiers.class);
	// AbstractOptionalDeclarator absOptDecl = (AbstractOptionalDeclarator) CParser
	// .createCrudeASTNode(absOptDeclString, AbstractOptionalDeclarator.class);
	// return new Type_obsolete(declSpec, absOptDecl);
	// }

	// public static void printOpCounts(Node root) {
	// for (Node stmt : Misc.getExactEnclosee(root, ExpressionStatement.class)) {
	// System.out.println("================================");
	// stmt.getInfo().printNode();
	// HashMap<Operator, Integer> opCountMap = new HashMap<>();
	// for (Operator op : Operator.values()) {
	// opCountMap.put(op, new Integer(0));
	// }
	//
	// for (Node exp : Misc.getExactEnclosee(stmt, Expression.class)) {
	// if (exp instanceof Expression) {
	// HashMap<Operator, Integer> thisOpCount = Misc.countOperator((Expression)
	// exp);
	// for (Operator op : thisOpCount.keySet()) {
	// opCountMap.put(op, opCountMap.get(op) + thisOpCount.get(op));
	// }
	// }
	// }
	// System.out.println();
	// Misc.printOperatorCount(opCountMap);
	//
	// }
	// }

	/**
	 * Given an element $elem$, this method returns that element ($wrapper$) of the
	 * $list$, which wraps $elem$ and for
	 * which $list.nodes.contains(wrapper)$ returns true.
	 *
	 * @param elem
	 *
	 * @return
	 */
	public static Node getWrapperInList(Node elem) {
		Node wrapper = elem;
		List<Node> list = Misc.getContainingList(elem);
		if (list == null) {
			return null;
		}
		while (!list.contains(wrapper)) {
			if (wrapper.getParent() != null) {
				wrapper = wrapper.getParent();
			} else {
				return null;
			}
		}
		return wrapper;
	}

	public static Node getContainerListNode(Node baseNode) {
		Node astForListOfNodes = baseNode;

		while ((astForListOfNodes != null)
				&& !(astForListOfNodes instanceof NodeList || astForListOfNodes instanceof NodeListOptional)) {
			astForListOfNodes = astForListOfNodes.getParent();
		}

		return astForListOfNodes;
	}

	public static List<Node> getContainingList(Node baseNode) {
		Node astForListOfNodes = baseNode;
		List<Node> containingList = null;

		while ((astForListOfNodes != null)
				&& !(astForListOfNodes instanceof NodeList || astForListOfNodes instanceof NodeListOptional)) {
			astForListOfNodes = astForListOfNodes.getParent();
		}

		if (astForListOfNodes == null) {
			return null;
		} else if (astForListOfNodes instanceof NodeList) {
			containingList = ((NodeList) astForListOfNodes).getNodes();
		} else {
			containingList = ((NodeListOptional) astForListOfNodes).getNodes();
		}

		return containingList;
	}

	public static int getIndexInContainingList(Node baseNode) {
		List<Node> containingList = getContainingList(baseNode);
		if (containingList == null) {
			return -1;
		} else {
			Node wrapper = Misc.getWrapperInList(baseNode);
			return containingList.indexOf(wrapper);
		}
	}

	/**
	 * Obtain the set of names associated with free variables in node.
	 *
	 * @param node
	 *
	 * @return
	 */
	public static Set<String> getFreeNames(Node node) {
		/*
		 * Obtain the set of all symbols declared in node.
		 */

		Set<Symbol> allInternalSymbols = new HashSet<>();
		for (Scopeable scope : node.getInfo().getLexicallyEnclosedScopesInclusive()) {
			if (scope instanceof CompoundStatement) {
				allInternalSymbols.addAll(((CompoundStatement) scope).getInfo().getSymbolTable().values());
			} else if (scope instanceof FunctionDefinition) {
				allInternalSymbols.addAll(((FunctionDefinition) scope).getInfo().getSymbolTable().values());
			} else if (scope instanceof TranslationUnit) {
				allInternalSymbols.addAll(((TranslationUnit) scope).getInfo().getSymbolTable().values());
			}
		}
		CellSet allUsedCells = node.getInfo().getUsedCells();

		Set<String> freeNames = new HashSet<>();
		if (allUsedCells.isUniversal()) {
			assert (false);
		} else {
			for (Cell c : allUsedCells) {
				if (c instanceof FreeVariable) {
					freeNames.add(((FreeVariable) c).getFreeVariableName());
				} else if (c instanceof Symbol) {
					Symbol sym = (Symbol) c;
					if (!allInternalSymbols.contains(sym)) {
						freeNames.add(sym.getName());
					}
				} else if (c instanceof AddressCell) {
					Cell sym = ((AddressCell) c).getPointedElement();
					if (sym instanceof Symbol && !allInternalSymbols.contains(sym)) {
						freeNames.add(((Symbol) sym).getName());
					}
				} else if (c instanceof FieldCell) {
					Symbol sym = ((FieldCell) c).getAggregateElement();
					if (!allInternalSymbols.contains(sym)) {
						freeNames.add(sym.getName());
					}
				}
			}
		}
		return freeNames;
	}

	/**
	 * Checks if there are any free identifiers in {@code n}, that have been
	 * declared in {@code scope}.
	 *
	 * @param n
	 *              a node that has to be inserted in the scope.
	 * @param scope
	 *              a scope in which node {@code n} has to be inserted.
	 *
	 * @return true, if {@code n} reads/writes to any free identifier that may be
	 *         declared within {@code scope}.
	 */
	public static boolean collidesFreelyWith(Node n, Scopeable scope) {
		/*
		 * Get a set of free identifiers accessed in n.
		 */
		Set<String> freeNames = Misc.getFreeNames(n);

		/*
		 * See if the list collides with the list of symbols declared in scope.
		 */
		Set<String> symbols = new HashSet<>();
		if (scope instanceof CompoundStatement) {
			symbols = ((CompoundStatement) scope).getInfo().getSymbolTable().keySet();
		} else if (scope instanceof FunctionDefinition) {
			symbols = ((FunctionDefinition) scope).getInfo().getSymbolTable().keySet();
		} else if (scope instanceof TranslationUnit) {
			symbols = ((TranslationUnit) scope).getInfo().getSymbolTable().keySet();
		}
		Set<String> declaredNames = new HashSet<>(symbols);
		if (doIntersect(freeNames, declaredNames)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if there are any free identifiers in {@code n}, that have been
	 * declared in any scope between {@code
	 * scope}, and the scope of {@code point}, inclusively.
	 *
	 * @param n
	 *              a node that has to be inserted in a child of scope.
	 * @param scope
	 *              a scope in whose child, a node {@code n} has to be inserted.
	 * @param point
	 *              program point in whose scope the node {@code n} has to be
	 *              inserted.
	 *
	 * @return true, if {@code n} reads/writes to any free identifier that may be
	 *         declared within {@code scope}.
	 */
	public static boolean collidesFreelyWithinScopesOf(Node n, Scopeable scope, Node point) {
		if (Misc.collidesFreelyWith(n, scope)) {
			return true;
		}
		for (Node nonLeaf : point.getInfo().getNonLeafNestingPathExclusive()) {
			if (nonLeaf instanceof CompoundStatement) {
				if (nonLeaf == scope) {
					return false;
				} else {
					CompoundStatement compStmt = (CompoundStatement) nonLeaf;
					if (Misc.collidesFreelyWith(n, compStmt)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Get a set of parallel construct that are lexically present inside node $n$.
	 *
	 * @return
	 */
	public static Set<ParallelConstruct> getParConstructs(Node n) {
		ParallelConstructGetter parConsGetter = new ParallelConstructGetter();
		n.accept(parConsGetter);
		return parConsGetter.parConstructs;
	}

	// /**
	// * This method returns the set of labels that apply directly to the given
	// * node.
	// *
	// * @param n
	// * should be a Statement.
	// * @return an empty list if there are no immediate labels attached to the
	// * statement $n$.
	// */
	// @Deprecated
	// public static Set<LabeledStatement> old_getLabels(Statement n) {
	// Set<LabeledStatement> labels = new Set<LabeledStatement>();
	//
	// Node tempNode = n;
	// assert (tempNode != null);
	// while (tempNode.getClass() != Statement.class) {
	// tempNode = tempNode.parent;
	// if (tempNode == null) {
	// return labels;
	// }
	// }
	//
	// if (tempNode.parent instanceof SimpleLabeledStatement) {
	// LabeledStatement labStmt = (LabeledStatement) Misc.getEnclosingNode(tempNode,
	// LabeledStatement.class);
	// labels.add(labStmt);
	// labels.addAll(Misc.old_getLabels(labStmt));
	// } else {
	// return labels;
	// }
	//
	// return labels;
	// }

	/**
	 * This method is used to query whether two nodes, n1 and n2 may happen in
	 * parallel. Note: If n1 and n2 may happen
	 * in parallel, it doesn't necessarily mean that they constitute a data race.
	 * Constraints put up by atomicity are
	 * ignored in this method due to the following reason: If a function call is
	 * made from the atomic region, then the
	 * reads and writes within the called function won't be atomic. So to be
	 * conservative, we assume that even when n1
	 * and n2 both are encapsulated in atomic regions, they may happen in parallel.
	 * (In fact, it is allowed to
	 * interleave the constituent operations of n1 and n2, as long as the read/write
	 * of the target variable appears to
	 * happen in isolation.)
	 *
	 * @param n1
	 *           This should be a CFG node. Otherwise, the function takes the
	 *           internal CFG node, if any. Otherwise, the
	 *           CFG encloser node.
	 * @param n2
	 *           // Same as n1.
	 *
	 * @return True, if n1 may happen in parallel with n2.
	 */
	public static boolean mayHappenInParallel(Node n1, Node n2) {
		if (!Misc.isCFGNode(n1)) {
			n1 = Misc.getInternalFirstCFGNode(n1);
			if (n1 == null) {
				n1 = Misc.getEnclosingCFGNodeInclusive(n1);
			}
		}
		if (!Misc.isCFGNode(n2)) {
			n2 = Misc.getInternalFirstCFGNode(n2);
			if (n2 == null) {
				n2 = Misc.getEnclosingCFGNodeInclusive(n2);
			}
		}
		if (n1 == null || n2 == null) {
			return true;
		}
		boolean ifVal;
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.YUANMHP) {
			ifVal = !Misc.doIntersect((Set<YPhase>) n1.getInfo().getNodePhaseInfo().getPhaseSet(),
					(Set<YPhase>) n2.getInfo().getNodePhaseInfo().getPhaseSet());
		} else {
			ifVal = !Misc.doIntersect((Set<Phase>) n1.getInfo().getNodePhaseInfo().getPhaseSet(),
					(Set<Phase>) n2.getInfo().getNodePhaseInfo().getPhaseSet());
		}
		if (ifVal) {
			return false; // n1 may not happen in parallel with n2 if they do
			// not share any common phase.
		}
		List<OldLock> n1Lockset = n1.getInfo().getLockSet();
		List<OldLock> n2Lockset = n2.getInfo().getLockSet();

		/*
		 * Check whether n1Lockset intersects with n2Lockset.
		 * If so, return false, else true.
		 */
		for (OldLock n1Lock : n1Lockset) {
			String n1LockName = n1Lock.getName();
			for (OldLock n2Lock : n2Lockset) {
				String n2LockName = n2Lock.getName();
				if (n1LockName.equals(n2LockName) && !(n1LockName.equals("__imop_atomic"))) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Returns a vector of call-sites that are present within $n$ after the
	 * call-site $callSite$.
	 *
	 * @param n
	 * @param callSite
	 *
	 * @return
	 */
	public static List<CallSite> getNextCallSites(Node n, CallSite callSite) {
		List<CallSite> callSites = n.getInfo().getCallSites();
		if (!callSites.contains(callSite)) {
			return null;
		}
		List<CallSite> returnSet = new ArrayList<>();
		boolean found = false;
		for (CallSite cs : callSites) {
			if (found) {
				returnSet.add(cs);
			}
			if (cs.equals(callSite)) {
				// TODO: Verify that the equals() of CallSite works as expected.
				found = true;
			}
		}
		return returnSet;
	}

	/**
	 * Returns a vector of call-sites that are present within $n$ before the
	 * call-site $callSite$ in a reverse order.
	 *
	 * @param n
	 * @param callSite
	 *
	 * @return
	 */
	public static List<CallSite> getPreviousCallSitesReversed(Node n, CallSite callSite) {
		List<CallSite> callSites = n.getInfo().getCallSites();
		if (!callSites.contains(callSite)) {
			return null;
		}
		List<CallSite> returnSet = new ArrayList<>();
		for (CallSite cs : callSites) {
			if (cs.equals(callSite)) {
				break;
			}
			returnSet.add(0, cs);
		}
		return returnSet;
	}

	/**
	 * Returns a new list with elements of <code>inList</code> stored in reverse
	 * order.
	 *
	 * @param inList
	 *               the input list.
	 *
	 * @return a new list with elements of <code>inList</code> stored in reverse
	 *         order.
	 */
	public static <T> List<T> reverseList(List<T> inList) {
		List<T> reverseList = new ArrayList<>();
		for (T elem : inList) {
			reverseList.add(0, elem);
		}
		return reverseList;
	}

	/**
	 * Throws a warning message that a certain feature has not been implemented,
	 *
	 * @param feature
	 *                     String explaining the feature that has not been
	 *                     implemented yet.
	 * @param sufferedNode
	 *                     node at/for which the warning has to be thrown.
	 */
	public static void warnDueToLackOfFeature(String feature, Node sufferedNode) {
		double line = -1;
		if (sufferedNode != null) {
			line = Misc.getLineNum(sufferedNode);
		}
		System.err.println("Feature not yet supported: ");
		System.err.println("warning @" + ((line == -1) ? "" : line) + " : " + feature);
	}

	/**
	 * Throws an error message and exits.
	 *
	 * @param error
	 *              string explaining the error message
	 */
	public static void exitDueToError(String error) {
		Thread.dumpStack();
		System.err.println("The pass had to exit upon encountering the following error: ");
		System.err.println("error: " + error);
		System.exit(0);
	}

	/**
	 * Throws a message that a certain feature has not been implemented, and exit.
	 *
	 * @param feature
	 *                String explaining the feature that has not been implemented
	 *                yet.
	 */
	public static void exitDueToLackOfFeature(String feature) {
		Thread.dumpStack();
		System.err.println(
				"The pass had to exit upon encountering the following feature that has not been implemented yet: ");
		System.err.println("error: " + feature);
		System.exit(0);
	}

	/**
	 * Returns the singleton element of a set. If the set doesn't contain exactly
	 * one element, then null is returned.
	 *
	 * @param workList
	 *
	 * @return
	 */
	public static <T> T getSingleton(Set<T> workList) {
		if (workList == null) {
			return null;
		}
		if (workList.size() != 1) {
			return null;
		}
		for (T element : workList) {
			return element;
		}
		return null;
	}

	/**
	 * Returns any element of an unordered set. If the set is empty, this method
	 * returns null.
	 *
	 * @param set
	 *
	 * @return
	 */
	public static <T> T getAnyElement(Set<T> set) {
		for (T element : set) {
			return element;
		}
		return null;
	}

	/**
	 * Checks whether <code>inExp</code> is an identifier/constant.
	 *
	 * @param inExp
	 *              input expression to be checked.
	 *
	 * @return true if inExp is an identifier or a constant
	 */
	public static boolean isSimplePrimaryExpression(Expression inExp) {
		IsSimplePrimaryExpression visitor = new IsSimplePrimaryExpression();
		return inExp.accept(visitor);
	}

	/**
	 * Returns the simple-primary expression represented by {@ code inExp}.
	 *
	 * @param inExp
	 *              an expression that may be an identifier or a constant.
	 *
	 * @return a reference to simple-primary expression if {@code inExp} is an
	 *         identifier or a constant; else null.
	 */
	public static SimplePrimaryExpression getSimplePrimaryExpression(Expression inExp) {
		IsSimplePrimaryExpression visitor = new IsSimplePrimaryExpression();
		if (inExp.accept(visitor)) {
			return visitor.aSimplePE;
		} else {
			return null;
		}
	}

	/**
	 * Checks whether <code>inExp</code> directly represents a function-call.
	 *
	 * @param inExp
	 *              expression to be checked.
	 *
	 * @return true if <code>inExp</code> directly wraps a function call.
	 */
	public static boolean isACall(StringBuilder inExpString) {
		Expression inExp = FrontEnd.parseACall(inExpString.toString());
		if (inExp == null) {
			return false;
		} else {
			return isACall(inExp);
		}
		// return isACall(inExp);
	}

	/**
	 * Checks whether <code>inExp</code> directly represents a function-call.
	 *
	 * @param inExp
	 *              expression to be checked.
	 *
	 * @return true if <code>inExp</code> directly wraps a function call.
	 */
	public static boolean isACall(Expression inExp) {
		IsACall callChecker = new IsACall();
		Boolean ret = inExp.accept(callChecker);
		if (ret == null) {
			ret = false;
		}
		return ret;
	}

	/**
	 * Returns a vector of clauses which are present in
	 * <code>ompConstruct</code>.
	 *
	 * @param ompConstruct
	 *                     An OpenMP construct from which the clauses need to be
	 *                     extracted.
	 *
	 * @return A list of clauses that are present in
	 *         <code>ompConstruct</code>.
	 */
	public static List<OmpClause> getClauseList(OmpConstruct ompConstruct) {
		OmpClauseGetter clauseGetter = new OmpClauseGetter();
		ompConstruct.accept(clauseGetter);
		return clauseGetter.ompClauseList;
	}

	public static void flushIfOnlySpaces(StringBuilder str) {
		boolean hasOther = false;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != ' ') {
				hasOther = true;
				break;
			}
		}
		if (!hasOther) {
			str.delete(0, str.length());
		}
	}

	/**
	 * Returns true if <code>exp</code> is an expression of a type that can hold an
	 * object type.
	 *
	 * @param exp
	 *
	 * @return true if the expression <code>exp</code> is of a type that can hold an
	 *         object type.
	 */
	public static boolean isLValue(Expression exp) {
		Type expType = Type.getType(exp);
		if (expType instanceof FunctionType) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Returns true if the expression's value can be modified.
	 *
	 * @param exp
	 *
	 * @return
	 */
	public static boolean isModifiableLValue(Expression exp) {
		if (!Misc.isLValue(exp)) {
			return false;
		}
		Type expType = Type.getType(exp);
		if (expType instanceof ArrayType) {
			return false;
		}
		if (!expType.isComplete()) {
			return false;
		}
		if (expType.hasConstQualifier()) {
			return false;
		}
		if (expType instanceof StructType || expType instanceof UnionType) {
			for (Type memberType : Misc.getAllMemberTypesRecursively(expType)) {
				if (memberType.hasConstQualifier()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns a hash-set of the type of all the members that are recursively
	 * present in the contained aggregates
	 * (arrays/structs) or unions. An empty set is returned when <code>type</code>
	 * is not an aggregate or a union type.
	 *
	 * @param type
	 *
	 * @return
	 */
	public static Set<Type> getAllMemberTypesRecursively(Type type) {
		Set<Type> returnSet = new HashSet<>();
		if (type instanceof StructType) {
			for (StructOrUnionMember member : ((StructType) type).getElementList()) {
				returnSet.add(member.getElementType());
				returnSet = Misc.setUnion(returnSet, Misc.getAllMemberTypesRecursively(member.getElementType()));
			}
		} else if (type instanceof ArrayType) {
			returnSet.add(((ArrayType) type).getElementType());
		} else if (type instanceof UnionType) {
			for (StructOrUnionMember member : ((UnionType) type).getElementList()) {
				returnSet.add(member.getElementType());
				returnSet = Misc.setUnion(returnSet, Misc.getAllMemberTypesRecursively(member.getElementType()));
			}
		}
		return returnSet;
	}

	public static boolean isFunctionDesignator(Expression exp) {
		if (Type.getType(exp) instanceof FunctionType) {
			return true;
		} else {
			return false;
		}
	}
	//
	// /**
	// * Dumps various call-statements obtained syntactically within {@code node}
	// *
	// * @param node
	// * AST node which needs to be checked for call-statements.
	// */
	// public static void dumpCallStatements(Node node) {
	// for (Node expNode : Misc.getInheritedEnclosee(node, CallStatement.class)) {
	// System.err.println("A Call Statement: ");
	// System.err.println("\t" + expNode);
	// }
	// }

	/**
	 * Obtain a CFG node corresponding to {@code node}
	 *
	 * @param node
	 *             CFG/AST node for which a corresponding CFG node is needed.
	 *
	 * @return {@code node} if it is a CFG node; else the internal-CFG node, or the
	 *         external-CFG node, in that order.
	 */
	public static Node getCFGNodeFor(Node node) {
		if (node == null) {
			assert (false);
			return null;
		}
		if (!Misc.isCFGNode(node)) {
			Node tempNode;
			tempNode = Misc.getInternalFirstCFGNode(node);
			if (tempNode == null) {
				node = Misc.getEnclosingCFGNodeInclusive(node);
			} else {
				node = tempNode;
			}
		}
		return node;
	}

	/**
	 * Obtain a set of all the incomplete edges within this node.
	 *
	 * @param root
	 *             a node.
	 *
	 * @return set of all the incomplete edges within the node {@code root}.
	 */
	public static Set<IncompleteEdge> getInternalIncompleteEdges(Node root) {
		Set<IncompleteEdge> edges = new HashSet<>();
		for (Node node : root.getInfo().getCFGInfo().getLexicalCFGContents()) {
			if (node.getInfo().getIncompleteSemantics().hasIncompleteEdges()) {
				edges.addAll(node.getInfo().getIncompleteSemantics().getIncompleteEdges());
			}
		}
		return edges;
	}

	/**
	 * Checks whether {@code exp} is a predicate (CFG expression), or not. Note that
	 * OmpForCondition also is considered
	 * as a predicate.
	 *
	 * @param node
	 *             node on which this check is requested.
	 *
	 * @return true, if {@code node} is a predicate CFG node.
	 */
	public static boolean isAPredicate(Node node) {
		if (node instanceof OmpForCondition) {
			return true;
		} else if (!(node instanceof Expression)) {
			return false;
		}

		Node parent = node.getParent();
		if (parent == null) {
			return true;
		} else if (parent instanceof IfClause || parent instanceof NumThreadsClause || parent instanceof FinalClause) {
			return false;
		} else if (parent instanceof IfStatement || parent instanceof SwitchStatement
				|| parent instanceof WhileStatement || parent instanceof DoStatement) {
			return true;
		} else if (parent instanceof NodeOptional) {
			Node grandParent = parent.getParent();
			if (grandParent instanceof ForStatement) {
				ForStatement forStmt = (ForStatement) grandParent;
				if (forStmt.getInfo().getCFGInfo().getTerminationExpression() == node) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks whether {@code exp} is a CFG expression, or not. Note that
	 * OmpForInitExpression, OmpForReinitExpression,
	 * and OmpForCondition are not considered as CFG expressions, for some unknown
	 * reasons. (Maybe we should consider
	 * them to be Expression nodes.)
	 *
	 * @param exp
	 *            expression that needs to be checked.
	 *
	 * @return true, if the expression {@code exp} is a CFG expression.
	 */
	public static boolean isACFGExpression(Expression exp) {
		Node parent = exp.getParent();
		if (parent == null) {
			return true;
		} else if (parent instanceof IfClause || parent instanceof NumThreadsClause || parent instanceof FinalClause) {
			return false;
		} else if (parent instanceof IfStatement || parent instanceof SwitchStatement
				|| parent instanceof WhileStatement || parent instanceof DoStatement) {
			return true;
		} else if (parent instanceof NodeOptional) {
			Node grandParent = parent.getParent();
			if (grandParent instanceof ForStatement) {
				return true;
			}
		}
		return false;
	}

	public static CellSet setUnion(CellSet setA, CellSet setB) {
		CellSet newSet = new CellSet(setA);
		newSet.addAll(setB);
		return newSet;
	}

	public static CellSet setMinus(CellSet setA, CellSet setB) {
		CellSet newSet = new CellSet(setA);
		newSet.removeAll(setB);
		return newSet;
	}

	public static CellSet setIntersection(CellSet setA, CellSet setB) {
		CellSet newSet = new CellSet(setA);
		newSet.retainAll(setB);
		return newSet;
	}

	public static boolean doIntersect(CellSet setA, CellSet setB) {
		return setA.overlapsWith(setB);
	}

	public static boolean doIntersect(CellList listA, CellList listB) {
		return listA.isOverlapping(listB);
	}

	/**
	 * Returns true if the string {@code str} can be evaluated to a constant value.
	 *
	 * @param str
	 *
	 * @return true if {@code str} represents a mathematical expression without
	 *         free-variables.
	 */
	public static boolean isConstant(String str) {
		return FrontEnd.isConstant(str);
		// ScriptEngineManager mgr = new ScriptEngineManager();
		// ScriptEngine engine = mgr.getEngineByName("JavaScript");
		// try {
		// engine.eval(str);
		// } catch (ScriptException e1) {
		// return false;
		// }
		// return true;
	}

	public static Integer evaluateInteger(Node node) {
		return Misc.evaluateInteger(node.toString().trim());
	}

	public static Integer evaluateInteger(String str) {
		try {
			Integer retInt = Integer.parseInt(str.trim());
			return retInt;
		} catch (NumberFormatException e1) {
			return null;
		}
	}

	public static Float evaluateFloat(Node node) {
		try {
			Float retFloat = Float.parseFloat(node.toString().trim());
			return retFloat;
		} catch (NumberFormatException e1) {
			return null;
		}
	}

	public static Character evaluateCharacter(Node node) {
		String str = node.toString().trim();
		if (str.startsWith("\'") && str.endsWith("\'") && str.length() == 3) {
			// This expression is a character constant.
			return str.charAt(1);
		}
		return null;
	}

	public static boolean evaluatesToZero(Expression exp) {
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

	public static Integer evaluateIntegerJS(Node node) {
		return Misc.evaluateInteger(node.toString());
	}

	public static Integer evaluateIntegerJS(String str) {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		Object retObj;
		try {
			retObj = engine.eval(str);
			Integer retInt = Integer.parseInt(retObj.toString());
			return retInt;
		} catch (ScriptException | NumberFormatException e1) {
			return null;
		}
	}

	/**
	 * A temporary method. Checks whether n1 and n2 conflict.
	 *
	 * @param n1
	 * @param n2
	 *
	 * @return
	 */
	public static boolean haveDataDependences(Node n1, Node n2) {
		CellList readCellsN1 = n1.getInfo().getReads();
		CellList writtenCellsN1 = n1.getInfo().getWrites();
		CellList readCellsN2 = n2.getInfo().getReads();
		CellList writtenCellsN2 = n2.getInfo().getWrites();
		if (doIntersect(readCellsN1, writtenCellsN2) || doIntersect(writtenCellsN1, writtenCellsN2)
				|| doIntersect(writtenCellsN1, readCellsN2)) {
			return true;
		}
		return false;
	}

	/**
	 * Obtain the least common ancestor in the nesting tree of scopes, for scopes of
	 * {@code one} and {@code two}.
	 *
	 * @param one
	 *            first node.
	 * @param two
	 *            second node.
	 *
	 * @return the least common ancestor in the nesting tree of scopes, for scopes
	 *         of {@code one} and {@code two}.
	 */
	public static Scopeable getLCAScope(Node one, Node two) {
		Scopeable scopeOne = Misc.getEnclosingBlock(one);
		if (scopeOne == null) {
			return null;
		}
		Scopeable scopeTwo = Misc.getEnclosingBlock(two);
		if (scopeTwo == null) {
			return null;
		}
		if (scopeOne == scopeTwo) {
			return scopeOne;
		}
		List<Node> enclosingNodesOfOne = one.getInfo().getNonLeafNestingPathExclusive();
		enclosingNodesOfOne.add(0, one);
		List<Node> enclosingNodesOfTwo = two.getInfo().getNonLeafNestingPathExclusive();
		enclosingNodesOfTwo.add(0, two);
		enclosingNodesOfOne = Misc.reverseList(enclosingNodesOfOne);
		enclosingNodesOfTwo = Misc.reverseList(enclosingNodesOfTwo);

		List<Scopeable> enclosingCSOfOne = new ArrayList<>();
		for (Object o : enclosingNodesOfOne.stream().filter((e) -> e instanceof Scopeable).toArray()) {
			enclosingCSOfOne.add((Scopeable) o);
		}
		List<Scopeable> enclosingCSOfTwo = new ArrayList<>();
		for (Object o : enclosingNodesOfTwo.stream().filter((e) -> e instanceof Scopeable).toArray()) {
			enclosingCSOfTwo.add((Scopeable) o);
		}

		int minSize = Math.min(enclosingCSOfOne.size(), enclosingCSOfTwo.size());

		int i = -1;
		while (++i < minSize) {
			Scopeable tempOne = enclosingCSOfOne.get(i);
			Scopeable tempTwo = enclosingCSOfTwo.get(i);
			if (tempOne != tempTwo) {
				// Pick the last one, if any, as lca and break.
				if (i != 0) {
					assert (enclosingCSOfOne.get(i - 1) == enclosingCSOfTwo.get(i - 1));
					return enclosingCSOfOne.get(i - 1);
				} else {
					return null;
				}
			}
		}
		if (enclosingCSOfOne.size() < enclosingCSOfTwo.size()) {
			return enclosingCSOfOne.get(enclosingCSOfOne.size() - 1);
		} else {
			return enclosingCSOfTwo.get(enclosingCSOfTwo.size() - 1);
		}
	}

	public static String getShortHand(String str) {
		String ret = "";
		boolean previousHigh = false;
		for (int i = 0; i < str.length(); i++) {
			if (Character.isUpperCase(str.charAt(i))) {
				ret += str.charAt(i);
				previousHigh = true;
				continue;
			}
			if (previousHigh) {
				ret += str.charAt(i);
				previousHigh = false;
				continue;
			}
		}
		return ret;
	}

	public static String getStringFromInputStream(InputStream is) {
		@SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static void changeFunctionReturnTypeToStruct(FunctionDefinition func, String structName) {
		if (func != null) {
			DeclarationSpecifiers declSpec = FrontEnd.parseAndNormalize("struct " + structName,
					DeclarationSpecifiers.class);
			NodeOptional opt = new NodeOptional(declSpec);
			FunctionType funcType = Type.getTypeTree(func, Program.getRoot());
			String declaratorStr = " " + func.getInfo().getFunctionName() + " (";
			if (funcType.getReturnType() instanceof PointerType) {
				boolean first = true;
				for (ParameterDeclaration param : func.getInfo().getCFGInfo().getParameterDeclarationList()) {
					if (first) {
						declaratorStr += param.toString();
						first = false;
						continue;
					}
					declaratorStr += ", " + param;
				}
			}
			declaratorStr += ")";
			Declarator declarator = FrontEnd.parseAndNormalize(declaratorStr, Declarator.class);

			func.setF0(opt);
			func.setF1(declarator);
		}
	}

	/**
	 * Obtain a string equivalent of a ByteArrayInputStream, while consuming the
	 * stream completely.
	 *
	 * @param in
	 *           a ByteArrayInputStream, whose string equivalent has to be obtained.
	 */
	public static String getString(ByteArrayInputStream in) {
		int n = in.available();
		byte[] bytes = new byte[n];
		in.read(bytes, 0, n);
		String s = new String(bytes, StandardCharsets.UTF_8);
		return s;
	}

	/**
	 * This method returns the enclosing loop for given continue statement.
	 *
	 * @param n:
	 *           Should generally be a continue statement.
	 *
	 * @return
	 */
	@Deprecated
	public static Node getEnclosingLoop(Node n) {
		if (n == null) {
			return null;
		}
		if (n instanceof IterationStatement) {
			return n;
		}

		if (n.getParent() == null) {
			// System.out.println("No owner loop found for node " +
			// n.getClass().getSimpleName());
			return null;
		}
		return getEnclosingLoop(n.getParent());
	}

	/**
	 * This method is used to query whether two nodes, n1 and n2, may result into an
	 * atomicity race condition.
	 *
	 * @param n1
	 *           This should be a CFG node. Otherwise, the function takes the
	 *           internal CFG node, if any. Otherwise, the
	 *           CFG encloser node.
	 * @param n2
	 *           // Same as n1.
	 *
	 * @return True, if n1 and n2 may form an atomicity race pair.
	 *
	 * @deprecated Needs to use {@link AbstractLock}s.
	 */
	@Deprecated
	public static boolean isAtomicityRacePair(Node n1, Node n2) {
		n1 = Misc.getCFGNodeFor(n1);
		n2 = Misc.getCFGNodeFor(n2);
		// if (!Misc.isCFGNode(n1)) {
		// n1 = Misc.getInternalFirstCFGNode(n1);
		// if (n1 == null) {
		// n1 = Misc.getEnclosingCFGNodeInclusive(n1);
		// }
		// }
		// if (!Misc.isCFGNode(n2)) {
		// n2 = Misc.getInternalFirstCFGNode(n2);
		// if (n2 == null) {
		// n2 = Misc.getEnclosingCFGNodeInclusive(n2);
		// }
		// }
		if (n1 == null || n2 == null) {
			return true;
		}
		if (n1 instanceof BeginNode || n1 instanceof EndNode || n2 instanceof BeginNode || n2 instanceof EndNode) {
			return false;
		}
		// System.err.println("Now, we will check: ");
		// System.err.println(n1.getInfo().getString());
		// System.err.println(n2.getInfo().getString());
		if (!Misc.mayHappenInParallel(n1, n2)) {
			return false;
		}

		CellSet readListN1 = n1.getInfo().getSharedReads();
		CellSet writeListN1 = n1.getInfo().getSharedWrites();
		CellSet readListN2 = n2.getInfo().getSharedReads();
		CellSet writeListN2 = n2.getInfo().getSharedWrites();

		if (!(Misc.doIntersect(readListN1, writeListN2) || Misc.doIntersect(readListN2, writeListN1)
				|| Misc.doIntersect(writeListN1, writeListN2))) {
			return false;
		}

		// System.err.println("Both have a conflict.");

		List<OldLock> lockSetN1 = n1.getInfo().getLockSet();
		List<OldLock> lockSetN2 = n2.getInfo().getLockSet();
		OldLock atomicLock = OldLock.getAtomicLock();
		if (!(lockSetN1.contains(atomicLock) && lockSetN2.contains(atomicLock))) {
			// System.err.println("These non-atomics!");
			return true;
		}

		AtomicConstructInfo atomN1 = Misc.getEnclosingNode(n1, AtomicConstruct.class).getInfo(); // can't return null
																									// since lockSet
																									// contains La
																									// already.
		AtomicConstructInfo atomN2 = Misc.getEnclosingNode(n2, AtomicConstruct.class).getInfo(); // can't return null
																									// since lockSet
																									// contains La
																									// already.

		if (atomN1.getTargetSymbol() != atomN2.getTargetSymbol()) {
			return true;
		}

		readListN1.remove(atomN1.getTargetSymbol());
		writeListN1.remove(atomN1.getTargetSymbol());
		readListN2.remove(atomN2.getTargetSymbol());
		writeListN2.remove(atomN1.getTargetSymbol());

		if (Misc.doIntersect(readListN1, writeListN2) || Misc.doIntersect(readListN2, writeListN1)
				|| Misc.doIntersect(writeListN1, writeListN2)) {
			return true;
		}

		return false;
	}

	/**
	 * This method returns the set of all the nodes on which $n$ depends, except the
	 * control dependees of $n$.
	 *
	 * @param n
	 *
	 * @return
	 */
	@Deprecated
	public static Set<Node> getAllDataDependees(Node n) {
		// Set<Node> dependeeNodes = new HashSet<>();
		//
		// if (!Misc.isCFGNode(n)) {
		// n = Misc.getInternalFirstCFGNode(n);
		// }
		//
		// if (Misc.isCFGLeafNode(n)) {
		// for (Set<Node> deps : n.getInfo().getFlowEdgeSrcList().values()) {
		// dependeeNodes.addAll(deps);
		// }
		// for (Set<Node> deps : n.getInfo().getAntiEdgeSrcList().values()) {
		// dependeeNodes.addAll(deps);
		// }
		//
		// for (Set<Node> deps : n.getInfo().getOutputEdgeSrcList().values()) {
		// dependeeNodes.addAll(deps);
		// }
		// } else {
		// for (Node internal :
		// n.getInfo().getCFGInfo().getLexicalIntraProceduralCFGContents()) {
		// if (Misc.isElementaryRWNodeParent(internal)) {
		// for (Set<Node> deps : internal.getInfo().getFlowEdgeSrcList().values()) {
		// dependeeNodes.addAll(deps);
		// }
		//
		// for (Set<Node> deps : internal.getInfo().getAntiEdgeSrcList().values()) {
		// dependeeNodes.addAll(deps);
		// }
		//
		// for (Set<Node> deps : internal.getInfo().getOutputEdgeSrcList().values()) {
		// dependeeNodes.addAll(deps);
		// }
		// }
		// }
		//
		// /*
		// * Remove those nodes from dependeeNodes which are already within
		// * $n$. From percolation's point of view: If we are able to move
		// * $n$, the internal dependences do not matter.
		// */
		// Set<Node> internalNodes =
		// n.getInfo().getCFGInfo().getLexicalIntraProceduralCFGContents();
		// dependeeNodes = Misc.setMinus(dependeeNodes, internalNodes);
		// }
		// return dependeeNodes;
		return null;
	}

	/**
	 * This method returns true if n contains a DefaultLabeledStatement
	 *
	 * @param n:
	 *           This refers to the BODY of Switch Statement.
	 *
	 * @return
	 */
	@Deprecated
	private static boolean old_hasDefaultLabel(Node n) {
		// OLD CODE:
		// SwitchRelevantStatementsGetter caseGetter = new
		// SwitchRelevantStatementsGetter();
		// n.accept(caseGetter);
		// return caseGetter.foundDefaultLabel;
		return false;
	}

	/**
	 * This method returns true if newContext is a subType of oldContext
	 *
	 * @param newCallSiteStack
	 * @param oldCallSiteStack
	 *
	 * @return
	 *
	 * @deprecated Use
	 *             {@link #hasEqualCallSiteStacks(Stack<CallSite>,Stack<CallSite>)}
	 *             instead
	 */
	@Deprecated
	public static boolean hasSubTypeCallSiteStacks(Stack<CallSite> newCallSiteStack, Stack<CallSite> oldCallSiteStack) {
		return hasEqualCallSiteStacks(newCallSiteStack, oldCallSiteStack);
	}

	/**
	 * Returns the set of symbols that are accessible at node $node$ (inclusively).
	 *
	 * @param node
	 *
	 * @return
	 *
	 * @see NodeInfo#getAllCellsAtNode()
	 */
	@Deprecated
	public static SymbolSet getVariableSymbolsAtNode(Node node) {
		// if (node == null) {
		// return null;
		// }
		// SymbolSet symbolSet = new SymbolSet();
		// Node scopeChoice = node;
		// while (scopeChoice != null) {
		// if (scopeChoice instanceof TranslationUnit) {
		// for (Symbol sym : ((RootInfo)
		// scopeChoice.getInfo()).getSymbolTable().values()) {
		// if (sym.isAVariable()) {
		// symbolSet.add(sym);
		// }
		// }
		// } else if (scopeChoice instanceof FunctionDefinition) {
		// for (Symbol sym : ((FunctionDefinitionInfo)
		// scopeChoice.getInfo()).getSymbolTable().values()) {
		// if (sym.isAVariable()) {
		// symbolSet.add(sym);
		// }
		// }
		// } else if (scopeChoice instanceof CompoundStatement) {
		// for (Symbol sym : ((CompoundStatementInfo)
		// scopeChoice.getInfo()).getSymbolTable().values()) {
		// if (sym.isAVariable()) {
		// symbolSet.add(sym);
		// }
		// }
		// }
		// scopeChoice = (Node) Misc.getEnclosingBlock(scopeChoice);
		// }
		// return symbolSet;
		return null;
	}

	/**
	 * @param nodeToken
	 *                  a {@link NodeToken} object for which we need to obtain the
	 *                  related symbol or free variable.
	 *
	 * @return reference to the symbol that is referred to by the {@code nodeToken}.
	 *         If nodeToken refers to a
	 *         free-variable, a corresponding {@link FreeVariable} object is
	 *         returned.
	 */
	public static Cell getSymbolOrFreeEntry(NodeToken nodeToken) {
		String name = nodeToken.getTokenImage();
		Cell symbolEntry = Misc.getSymbolEntry(name, nodeToken);
		if (symbolEntry != null) {
			return symbolEntry;
		}
		return new FreeVariable(nodeToken);
	}

	public static Typedef getTypedefEntry(String name, Node node) {
		if (node == null) {
			return null;
		}

		Node scopeChoice = node;
		while (scopeChoice != null) {
			if (scopeChoice instanceof TranslationUnit) {
				HashMap<String, Typedef> typedefTable = ((RootInfo) scopeChoice.getInfo()).getTypedefTable();
				if (typedefTable.containsKey(name)) {
					return typedefTable.get(name);
				}
			} else if (scopeChoice instanceof CompoundStatement) {
				HashMap<String, Typedef> typedefTable = ((CompoundStatementInfo) scopeChoice.getInfo())
						.getTypedefTable();
				if (typedefTable.containsKey(name)) {
					return typedefTable.get(name);
				}
			}
			scopeChoice = scopeChoice.getParent();
		}
		/*
		 * In case if node is disconnected from the AST, we should look into at
		 * least the TranslationUnit of the input.
		 */

		HashMap<String, Typedef> typedefTable = Program.getRoot().getInfo().getTypedefTable();
		if (typedefTable.containsKey(name)) {
			return typedefTable.get(name);
		}

		/*
		 * Search in the built-in libraries now.
		 */

		typedefTable = FrontEnd.getMacBuiltins().getInfo().getTypedefTable();
		if (typedefTable.containsKey(name)) {
			return typedefTable.get(name);
		}

		typedefTable = FrontEnd.getK2Builtins().getInfo().getTypedefTable();
		if (typedefTable.containsKey(name)) {
			return typedefTable.get(name);
		}

		typedefTable = FrontEnd.getOtherBuiltins().getInfo().getTypedefTable();
		if (typedefTable.containsKey(name)) {
			return typedefTable.get(name);
		}

		return null;
	}

	/**
	 * @param name
	 * @param node
	 *
	 * @return Symbol entry for the given id named "name", when used in given
	 *         "node". Returns null, if entry not found
	 *         in any of the outer nesting parents of "node". Note that if the
	 *         {@class node} itself is a scope, then its symbol
	 *         table too is searched for.
	 */
	public static Symbol getSymbolEntry(String name, Node node) {
		if (node == null) {
			return null;
		}

		Node scopeChoice = node;
		/*
		 * If this flag remains set after the succeeding while loop, then it
		 * would mean that the given node is not connected to the program.
		 */
		boolean disconnected = true;
		while (scopeChoice != null) {
			if (scopeChoice instanceof TranslationUnit) {
				HashMap<String, Symbol> symbolTable = ((RootInfo) scopeChoice.getInfo()).getSymbolTable();
				if (symbolTable.containsKey(name)) {
					return symbolTable.get(name);
				}
				disconnected = false;
			} else if (scopeChoice instanceof FunctionDefinition) {
				HashMap<String, Symbol> symbolTable = ((FunctionDefinitionInfo) scopeChoice.getInfo()).getSymbolTable();
				if (symbolTable.containsKey(name)) {
					return symbolTable.get(name);
				}
			} else if (scopeChoice instanceof CompoundStatement) {
				HashMap<String, Symbol> symbolTable = ((CompoundStatementInfo) scopeChoice.getInfo()).getSymbolTable();
				if (symbolTable.containsKey(name)) {
					return symbolTable.get(name);
				}
			}
			scopeChoice = scopeChoice.getParent();
			// scopeChoice = (Node) Misc.getEnclosingBlock(scopeChoice);
		}

		/*
		 * If we have reached this point, then it implies that the symbol wasn't
		 * found.
		 * There are two cases here -- either the given node is disconnected, or
		 * it isn't.
		 * In case of the former, we search into the global scope of the
		 * program, in case if that helps.
		 * Otherwise, or if the symbol is not found in the program's global
		 * scope, then we search
		 * in the built-in files.
		 */
		if (disconnected) {
			HashMap<String, Symbol> symbolTable = Program.getRoot().getInfo().getSymbolTable();
			if (symbolTable.containsKey(name)) {
				return symbolTable.get(name);
			}
		}

		/*
		 * Search in the built-in libraries now.
		 */
		Symbol sym = Misc.getSymbolFromLibrary(name, FrontEnd.getMacBuiltins());
		if (sym != null) {
			return sym;
		}
		sym = Misc.getSymbolFromLibrary(name, FrontEnd.getK2Builtins());
		if (sym != null) {
			return sym;
		}
		sym = Misc.getSymbolFromLibrary(name, FrontEnd.getOtherBuiltins());
		if (sym != null) {
			return sym;
		}
		// Node enclosingNode = Misc.getOuterMostNonLeafEncloser(node);
		// Misc.warnDueToLackOfFeature("Kindly ensure that symbol " + name + " has been
		// declared at line #" + Misc.getLineNum(node), node);
		// System.err.println(enclosingNode);
		return null;
	}

	/**
	 * Checks if the symbol with the given {@code name} is present in the provided
	 * {@code library}.
	 *
	 * @param name
	 *                name of the identifier corresponding to which a Symbol has to
	 *                be found.
	 * @param library
	 *                reference to any of the built-in libraries that contain
	 *                built-in symbols.
	 *
	 * @return a symbol corresponding to the given {@code name}, if found in the
	 *         {@code library}; {@code null}
	 *         otherwise.
	 */
	public static Symbol getSymbolFromLibrary(String name, TranslationUnit library) {
		Cell.dontSave = true;
		Symbol.dontSaveSymbol = true;
		HashMap<String, Symbol> symbolTable = library.getInfo().getSymbolTable();
		Symbol.dontSaveSymbol = false;
		Cell.dontSave = false;
		if (symbolTable.containsKey(name)) {
			return symbolTable.get(name);
		} else if (name.startsWith("__builtin_")) {
			String newName = name.replaceAll("__builtin_", "");
			if (symbolTable.containsKey(newName)) {
				return symbolTable.get(newName);
			}
		} else {
			String newName = "__builtin_" + name;
			if (symbolTable.containsKey(newName)) {
				return symbolTable.get(newName);
			}
		}
		return null;
	}

}
