/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform.simplify;

import imop.ast.annotation.Label;
import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.analysis.flowanalysis.Symbol;
import imop.lib.analysis.typeSystem.ArrayType;
import imop.lib.analysis.typeSystem.FunctionType;
import imop.lib.builder.Builder;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.link.autoupdater.AutomatedUpdater;
import imop.lib.cfg.link.node.CompoundElementLink;
import imop.lib.transform.BasicTransform;
import imop.lib.transform.updater.InsertImmediateSuccessor;
import imop.lib.transform.updater.NodeRemover;
import imop.lib.transform.updater.NodeRemover.LabelShiftingMode;
import imop.lib.transform.updater.NodeReplacer;
import imop.lib.transform.updater.sideeffect.AddedDFDPredecessor;
import imop.lib.transform.updater.sideeffect.AddedDFDSuccessor;
import imop.lib.transform.updater.sideeffect.AddedExplicitBarrier;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;
import imop.lib.util.ProfileSS;
import imop.parser.FrontEnd;
import imop.parser.Program;

import java.util.*;

public class FunctionInliner {

	public static long inliningTimer = 0;
	private static int counter = 0;

	public static void resetStaticFields() {
		FunctionInliner.inliningTimer = 0;
		FunctionInliner.counter = 0;
	}

	/**
	 * Attempts to inline all those inlineable call-sites in the CFG (and CG) of
	 * {@code root} that contain a
	 * parallel-construct or a barrier within them.
	 *
	 * @param root
	 *             root node under which we will attempt to inline all call-sites
	 *             that may help further barrier removal.
	 */
	public static void inline(Node root) {
		long timer = System.nanoTime();
		FunctionInliner.inlineRecursive(root);
		ProfileSS.insertCP(); // RCP
		AutomatedUpdater.stabilizePTAInCPModes();
		timer = System.nanoTime() - timer;
		inliningTimer += timer;
	}

	private static int localCounter = 0;

	/**
	 * Attempts to inline all those inlineable call-sites in the CFG (and CG) of
	 * {@code root} that contain a
	 * parallel-construct or a barrier within them.
	 *
	 * @param root
	 *             root node under which we will attempt to inline all call-sites
	 *             that may help further barrier removal.
	 */
	private static void inlineRecursive(Node root) {
		for (CallStatement callStmt : new HashSet<>(root.getInfo().getReachableCallStatementsInclusive())) {
			if (!FunctionInliner.isInlineable(callStmt)) {
				continue;
			}
			if (!FunctionInliner.canInliningBeProfitable(callStmt)) {
				continue;
			}
			if (!FunctionInliner.handleConflictsAndStatics(callStmt)) {
				System.err.println("Unable to inline the call-stmt due to conflicts: " + callStmt);
				continue;
			}
			List<FunctionDefinition> calledDefs = callStmt.getInfo().getCalledDefinitions();
			FunctionDefinition funcDef = calledDefs.get(0);
			/*
			 * Inline internal call-statements first. Note that this won't
			 * affect the enclosing loop since no recursive functions are
			 * inlined.
			 */
			FunctionInliner.inlineRecursive(funcDef);
			ProfileSS.insertCP(); // RCP
			AutomatedUpdater.stabilizePTAInCPModes();
			/*
			 * After inlining the internal call-statements, inline this
			 * call-statement. Rest assured that this call-statement is still
			 * present in the program.
			 */
			assert (callStmt.getInfo().isConnectedToProgram());
			FunctionInliner.inlineFunctionDefinition(callStmt);
			ProfileSS.insertCP();
			if (Program.dumpIntermediateStates) {
				DumpSnapshot.dumpRoot("inlined" + Program.updateCategory + counter);
				DumpSnapshot.dumpPointsTo("inlined" + Program.updateCategory + counter);
				DumpSnapshot.dumpPhases("inlined" + Program.mhpUpdateCategory + counter++);
			}
			/*
			 * Note that some other call-statements might have been changed
			 * now due to renaming. Hence, we should rerun the enclosing
			 * loop.
			 */
			FunctionInliner.inlineRecursive(root);
			ProfileSS.insertCP();
			return;
		}
	}

	/**
	 * Checks whether the given call-statement can be inlined.
	 *
	 * @param callStmt
	 *                 a call-statement to be tested.
	 *
	 * @return true, if it is possible to inline the given call-statement.
	 */
	public static boolean isInlineable(CallStatement callStmt) {
		if (callStmt == null || callStmt.isPhantom()) {
			return false;
		}
		List<FunctionDefinition> calledDefs = callStmt.getInfo().getCalledDefinitions();
		if (calledDefs == null || calledDefs.size() != 1) {
			return false;
		}
		FunctionDefinition funcDef = calledDefs.get(0);
		if (funcDef.getInfo().isRecursive()) {
			return false;
		}
		return true;
	}

	/**
	 * Checks whether inlining might be profitable for barrier removal techniques.
	 * <br>
	 * For now, we consider a call-site to be profitable for inlining if it contains
	 * at least one barrier/parallel
	 * construct, and in case of former, if it contains at least one shared access.
	 *
	 * @param callStmt
	 *                 a call-statement to be inspected.
	 *
	 * @return true, if the call-statement may contain any barrier or parallel
	 *         construct, along with at least one shared
	 *         access (if it doesn't contain a parallel construct).
	 */
	private static boolean canInliningBeProfitable(CallStatement callStmt) {
		List<FunctionDefinition> calledDefs = callStmt.getInfo().getCalledDefinitions();
		FunctionDefinition funcDef = calledDefs.get(0);
		int found = -1;
		for (Node leaf : funcDef.getInfo().getCFGInfo().getIntraTaskCFGLeafContents()) {
			if (leaf instanceof BarrierDirective) {
				found = 0;
				break;
			} else if (leaf instanceof BeginNode) {
				BeginNode beginNode = (BeginNode) leaf;
				if (beginNode.getParent() instanceof ParallelConstruct) {
					found = 1;
					break;
				}
			}
		}
		switch (found) {
		case -1:
			return false;
		case 0:
		case 1:
			return true;
		default:
			assert (false);
			return false;
		}
	}

	private static boolean handleConflictsAndStatics(CallStatement callStmt) {
		List<FunctionDefinition> calledDefs = callStmt.getInfo().getCalledDefinitions();
		FunctionDefinition funcDef = calledDefs.get(0);
		if (!FunctionInliner.handleTypedefNameConflicts(callStmt, funcDef)
				|| !FunctionInliner.handleTypeNameConflicts(callStmt, funcDef)
				|| !FunctionInliner.handleLabelNameConflicts(callStmt, funcDef)) {
			return false;
		}
		if (!handleStaticDeclarations(funcDef)) {
			return false;
		}
		return true;
	}

	/**
	 * For now, this method does not suggest any renaming of the types, and returns
	 * false if there is any namespace
	 * collision for types of the given function definition with the type table
	 * visible at the call-statement.
	 * <br>
	 * Later, we should modify this function such that it removes all found
	 * conflicts by type renaming.
	 *
	 * @param funcDef
	 *                 target function definition of {@code callStmt}.
	 * @param callStmt
	 *                 a call-statement to be inlined.
	 *
	 * @return true, if there are no type namespace collisions; false, otherwise.
	 */
	private static boolean handleTypeNameConflicts(CallStatement callStmt, FunctionDefinition funcDef) {
		CompoundStatement body = funcDef.getInfo().getCFGInfo().getBody();
		Set<String> typeNameAtCallStmt = callStmt.getInfo().getAllTypeNameListAtNode();
		for (String typeName : body.getInfo().getTypeTable().keySet()) {
			if (typeNameAtCallStmt.contains(typeName)) {
				Misc.warnDueToLackOfFeature("Unable to inline a function due to collision of type named " + typeName,
						null);
				return false;
			}
		}
		return true;
	}

	/**
	 * For now, this method does not suggest any renaming of the typedef's, and
	 * returns false if there is any namespace
	 * collision for typedef of the given function definition with the typedef table
	 * visible at the call-statement.
	 * <br>
	 * Later, we should modify this function such that it removes all found
	 * conflicts by typedef renaming.
	 *
	 * @param funcDef
	 *                 target function definition of {@code callStmt}.
	 * @param callStmt
	 *                 a call-statement to be inlined.
	 *
	 * @return true, if there are no typedef namespace collisions; false, otherwise.
	 */
	private static boolean handleTypedefNameConflicts(CallStatement callStmt, FunctionDefinition funcDef) {
		CompoundStatement body = funcDef.getInfo().getCFGInfo().getBody();
		Set<String> typedefNameAtCallStmt = callStmt.getInfo().getAllTypedefNameListAtNode();
		for (String typedefName : body.getInfo().getTypedefTable().keySet()) {
			if (typedefNameAtCallStmt.contains(typedefName)) {
				Misc.warnDueToLackOfFeature(
						"Unable to inline a function due to collision of typedef named " + typedefName, null);
				return false;
			}
		}
		return true;
	}

	/**
	 * For now, this method does not suggest any renaming of the labels, and returns
	 * false if there is any namespace
	 * collision for labels of the given function definition with the labels at the
	 * caller.
	 * <br>
	 * Later, we should modify this function such that it removes all found
	 * conflicts by label renaming.
	 *
	 * @param funcDef
	 *                 target function definition of {@code callStmt}.
	 * @param callStmt
	 *                 a call-statement to be inlined.
	 *
	 * @return true, if there are no label namespace collisions; false, otherwise.
	 */
	private static boolean handleLabelNameConflicts(CallStatement callStmt, FunctionDefinition funcDef) {
		Node caller = Misc.getEnclosingFunction(callStmt);
		if (caller == null || !(caller instanceof FunctionDefinition)) {
			Misc.warnDueToLackOfFeature(
					"Cannot inline a callStatement that is not enclosed within a function-definition.", null);
			return false;
		}
		FunctionDefinition callerDef = (FunctionDefinition) caller;
		Set<String> callerLabels = callerDef.getInfo().getAllSimpleLabelNamesOfFunction();
		Set<String> calleeLabels = funcDef.getInfo().getAllSimpleLabelNamesOfFunction();
		if (Misc.doIntersect(callerLabels, calleeLabels)) {
			Misc.warnDueToLackOfFeature(
					"Unable to inline a function due to conflict of the labels at the caller and calle:" + callerLabels
							+ " conflicts with " + calleeLabels,
					null);
			return false;
		}
		return true;
	}

	/**
	 * Ensures that there are no static declarations in the given
	 * function-definition. Note that this function may
	 * update the function-definition.
	 *
	 * @param funcDef
	 *                a function-definition.
	 *
	 * @return true, if all static declarations, if any, have been pushed up to the
	 *         global scope, with proper renaming,
	 *         if required.
	 */
	public static boolean handleStaticDeclarations(FunctionDefinition funcDef) {
		for (Declaration decl : Misc.getInheritedEncloseeList(funcDef, Declaration.class)) {
			Symbol sym = decl.getInfo().getDeclaredSymbol();
			if (sym == null || !sym.isStatic()) {
				continue;
			}
			/*
			 * Check if there is a name-conflict with the global scope.
			 */
			TranslationUnit global = Misc.getEnclosingNode(funcDef, TranslationUnit.class);
			if (global == null) {
				Misc.exitDueToError("Cannot inline a function-definition that is not a part of the program.");
				return false;
			}
			if (global.getInfo().getSymbolTable().keySet().contains(sym.getName())) {
				// Rename the declaration, and all uses, while moving it to the global scope.
				String newName = Builder.getNewTempName(sym.getName());
				Declaration exactDecl = Misc.getRenamedDeclaration(decl, newName);
				Builder.addDeclarationBeforeFirstFunction(exactDecl);
				HashMap<String, String> renamingMap = new HashMap<>();
				renamingMap.put(sym.getName(), newName);
				CompoundStatement compStmt = funcDef.getInfo().getCFGInfo().getBody();
				for (Node element : compStmt.getInfo().getCFGInfo().getElementList()) {
					if (element == sym.getDeclaringNode()) {
						continue;
					} else {
						BasicTransform.renameIdsInNodes(element, renamingMap);
					}
				}
				NodeRemover.removeNode(sym.getDeclaringNode());
			} else {
				// Move the declaration to the global scope.
				Declaration exactDecl = Misc.getRenamedDeclaration(decl, sym.getName());
				NodeRemover.removeNode(decl);
				Builder.addDeclarationBeforeFirstFunction(exactDecl);
			}
		}
		return true;
	}

	/**
	 * Attempts to inline the function called at {@code callStmt}. If binding of the
	 * given call-statement cannot be
	 * determined unambiguously, then this method performs no change and returns
	 * {@code false}.
	 * <br>
	 * Note that labels attached to the {@code callStmt} are shifted to the first
	 * executable statement in the inline
	 * code.
	 *
	 * @param callStmt
	 *                 a call-statement to be inlined.
	 *
	 * @return {@code true}, if the inlining was successful; {@code false}
	 *         otherwise.
	 */
	private static boolean inlineFunctionDefinition(CallStatement callStmt) {
		System.err.println("\nAttempting to inline: " + callStmt);
		List<FunctionDefinition> calledDefs = callStmt.getInfo().getCalledDefinitions();
		FunctionDefinition funcDef = calledDefs.get(0);
		/*
		 * Obtain list of properly renamed declarations to be added from the
		 * scope of function's body, along with a renaming map to be applied on
		 * all the statements.
		 */
		List<Declaration> newDeclarationList = new ArrayList<>();
		HashMap<String, String> renamingMap = new HashMap<>();
		FunctionInliner.handleParameterNameConflicts(callStmt, funcDef, newDeclarationList, renamingMap);
		FunctionInliner.performInliningWithLocalVariableConflictRemoval(callStmt, funcDef, newDeclarationList,
				renamingMap);
		ProfileSS.insertCP();
		System.err.println("\tInlining successful.");
		return true;
	}

	/**
	 * Ensures that there are no name-conflicts of parameters with variables visible
	 * at the call-site.
	 *
	 * @param funcDef
	 * @param callStmt
	 * @param newDeclarationList
	 *
	 * @return true, if all parameter name-conflicts have been resolved in the
	 *         provided body.
	 */
	private static boolean handleParameterNameConflicts(CallStatement callStmt, FunctionDefinition funcDef,
			List<Declaration> newDeclarationList, HashMap<String, String> renamingMap) {
		CompoundStatement compStmt = funcDef.getInfo().getCFGInfo().getBody();
		List<SimplePrimaryExpression> argList = callStmt.getPreCallNode().getArgumentList();
		int index = -1;
		Set<String> allSymbolNamesAtCaller = callStmt.getInfo().getAllSymbolNamesAtNodes();
		for (ParameterDeclaration param : funcDef.getInfo().getCFGInfo().getParameterDeclarationList()) {
			index++;
			if (param.toString().trim().equals("void")) {
				return true;
			}
			/*
			 * A. If a parameter name matches with that of the argument
			 * identifier, and if the parameter has not been written anywhere in
			 * the body of the callee, then we can reuse the argument variable
			 * in place of the parameter.
			 */
			Symbol paramSym = param.getInfo().getDeclaredSymbol();
			String paramName = paramSym.toString().trim();
			if (paramName.equals(argList.get(index).toString().trim())) {
				if (!compStmt.getInfo().getWrites().contains(paramSym)) {
					// It is safe to replace parameter by the argument.
					continue;
				}
			}
			/*
			 * B. If there is a name conflict for the parameter at caller,
			 * rename the corresponding declaration for the paramter; otherwise,
			 * just obtain the pointer-converted type.
			 * Note that pointer conversion would take place only for an
			 * array-type and function-type. To be safe, let's rename in both
			 * these cases.
			 */
			if (allSymbolNamesAtCaller.contains(paramName) || paramSym.getType() instanceof ArrayType
					|| paramSym.getType() instanceof FunctionType) {
				// Build a new declaration from parameter, with new name.
				String newName = Builder.getNewTempName(paramName);
				Declaration newDecl = paramSym.getType().getDeclaration(newName);
				newDeclarationList.add(newDecl);
				renamingMap.put(paramName, newName);
			} else {
				// Build a declaration out of the parameter declaration, with same name.
				Declaration newDecl = paramSym.getType().getDeclaration(paramName);
				newDeclarationList.add(newDecl);
			}
		}
		return false;
	}

	private static Declaration obtainDeclarationForLocal(Declaration localDecl, CallStatement callStmt,
			FunctionDefinition funcDef, HashMap<String, String> renamingMap, Set<String> allSymbolNamesAtCaller) {
		if (localDecl.getInfo().isNonVariableDeclaration()) {
			// For declaration of typedef, and user-defined types.
			return FrontEnd.parseAndNormalize(localDecl.toString(), Declaration.class);
		}
		Declaration retDecl = localDecl;
		Symbol declaredLocalSymbol = localDecl.getInfo().getDeclaredSymbol();
		if (allSymbolNamesAtCaller.contains(declaredLocalSymbol.getName())) {
			String newTemp = Builder.getNewTempName(declaredLocalSymbol.getName());
			retDecl = Misc.getRenamedDeclarationWithRenamedInit(localDecl, newTemp, renamingMap);
			renamingMap.put(declaredLocalSymbol.getName(), newTemp);
			return retDecl;
		} else {
			return FrontEnd.parseAndNormalize(localDecl.toString(), Declaration.class);
		}
	}

	private static void performInliningWithLocalVariableConflictRemoval(CallStatement callStmt,
			FunctionDefinition funcDef, List<Declaration> parameterDeclarationList,
			HashMap<String, String> parameterRenamingMap) {
		CompoundElementLink link = (CompoundElementLink) CFGLinkFinder.getCFGLinkFor(callStmt);
		CompoundStatement callerScope = (CompoundStatement) link.getEnclosingNode();
		int callStmtIndex = link.getIndex();

		List<Label> labels = callStmt.getInfo().getLabelAnnotations();
		if (labels == null) {
			labels = new ArrayList<>();
		} else {
			labels = new ArrayList<>(labels);
		}
		Set<String> allSymbolNamesAtCaller = callStmt.getInfo().getAllSymbolNamesAtNodes();
		NodeRemover.removeNode(callStmt, LabelShiftingMode.LABELS_WITH_NODE);
		ProfileSS.insertCP();

		/*
		 * Step 1: Insert all the parameter declarations, that need to be
		 * added here.
		 */
		for (Declaration declForParam : parameterDeclarationList) {
			List<SideEffect> sideEffects = callerScope.getInfo().getCFGInfo().addDeclaration(callStmtIndex++,
					declForParam);
			ProfileSS.insertCP();
			for (SideEffect sideEffect : sideEffects) {
				if (sideEffect.getClass().getSimpleName().equals("IndexIncremented")
						|| sideEffect instanceof AddedDFDPredecessor || sideEffect instanceof AddedDFDSuccessor
						|| sideEffect instanceof AddedExplicitBarrier) {
					// TODO: Verify this.
					// if (sideEffect == UpdateSideEffect.INDEX_INCREMENTED
					// || sideEffect == UpdateSideEffect.ADDED_DFD_PREDECESSOR) {
					callStmtIndex++;
				}
			}
		}

		boolean labelsHandled = false;
		/*
		 * Step 2: Add assignment for all those parameters for which a
		 * declaration has been added.
		 * Note that the first assignment should also be annotated with the
		 * labels.
		 */
		int index = -1;
		List<SimplePrimaryExpression> argList = callStmt.getPreCallNode().getArgumentList();
		for (ParameterDeclaration paramDecl : funcDef.getInfo().getCFGInfo().getParameterDeclarationList()) {
			index++;
			if (paramDecl.toString().trim().equals("void")) {
				continue;
			}
			Symbol paramSym = paramDecl.getInfo().getDeclaredSymbol();
			boolean declarationDeclared = false;
			if (parameterRenamingMap.keySet().contains(paramSym.getName())) {
				declarationDeclared = true;
			}
			if (!declarationDeclared) {
				for (Declaration declAdded : parameterDeclarationList) {
					Symbol sym2 = declAdded.getInfo().getDeclaredSymbol();
					if (sym2.getName().equals(paramSym.getName())) {
						declarationDeclared = true;
						break;
					}
				}
			}
			if (!declarationDeclared) {
				continue;
			}
			/*
			 * Add the assignment of argument to parameter.
			 */
			String assignStr;
			if (parameterRenamingMap.containsKey(paramSym.getName())) {
				assignStr = parameterRenamingMap.get(paramSym.getName());
			} else {
				assignStr = paramSym.getName();
			}
			assignStr += " = " + argList.get(index) + ";";
			ExpressionStatement assignStmt = FrontEnd.parseAndNormalize(assignStr, ExpressionStatement.class);
			List<SideEffect> sideEffects = callerScope.getInfo().getCFGInfo().addElement(callStmtIndex++, assignStmt);
			ProfileSS.insertCP();
			for (SideEffect sideEffect : sideEffects) {
				if (sideEffect.getClass().getSimpleName().equals("IndexIncremented")
						|| sideEffect instanceof AddedDFDPredecessor || sideEffect instanceof AddedDFDSuccessor
						|| sideEffect instanceof AddedExplicitBarrier) {
					// TODO: Verify this.
					// if (sideEffect == UpdateSideEffect.INDEX_INCREMENTED
					// || sideEffect == UpdateSideEffect.ADDED_DFD_PREDECESSOR) {
					callStmtIndex++;
				}
			}
			if (!labelsHandled) {
				labelsHandled = true;
				for (Label lab : labels) {
					assignStmt.getInfo().addLabelAnnotation(lab);
				}
			}
		}
		/*
		 * Step 4: Create a jump point at the end of the inlined code, unless
		 * the function contains no explicit return statement,
		 * OR unless the function contains only one explicit return, as its last
		 * statement.
		 */
		CompoundStatement calleeBody = funcDef.getInfo().getCFGInfo().getBody();
		String jumpLabel = null;
		List<ReturnStatement> retStmt = Misc.getInheritedEncloseeList(funcDef, ReturnStatement.class);
		if (!retStmt.isEmpty()) {
			if (retStmt.size() == 1 && retStmt.get(0) == calleeBody.getInfo().getCFGInfo().getElementList()
					.get(calleeBody.getInfo().getCFGInfo().getElementList().size() - 1)) {
				// Do not add the jumpLabel.
			} else {
				jumpLabel = Builder.getNewTempName(callStmt.getFunctionDesignatorNode().toString());
				String jumpString = jumpLabel + ": ;";
				Statement jumpStmt = FrontEnd.parseAndNormalize(jumpString, Statement.class);
				callerScope.getInfo().getCFGInfo().addElement(callStmtIndex, jumpStmt); // index shouldn't be
																						// incremented here.
				ProfileSS.insertCP();
			}
		}

		/*
		 * Step 5: Now, add all local variables and statements, with proper
		 * renaming of local variables.
		 * Handle return statements carefully.
		 * Also, if there have been no argument assignments, we need to take
		 * care of the labels of the call-stmt as well.
		 */
		HashMap<String, String> allRenamingMap = new HashMap<>(parameterRenamingMap);
		for (Node element : calleeBody.getInfo().getCFGInfo().getElementList()) {
			if (element instanceof Declaration) {
				Declaration newDecl = FunctionInliner.obtainDeclarationForLocal((Declaration) element, callStmt,
						funcDef, allRenamingMap, allSymbolNamesAtCaller);
				callerScope.getInfo().getCFGInfo().addDeclaration(callStmtIndex++, newDecl);
				ProfileSS.insertCP();
			} else if (element instanceof Statement) {
				for (Statement newStmt : FunctionInliner.getStmtToInline((Statement) element, callStmt, allRenamingMap,
						jumpLabel)) {
					List<SideEffect> sideEffects = callerScope.getInfo().getCFGInfo().addElement(callStmtIndex++,
							newStmt);
					ProfileSS.insertCP();
					for (SideEffect sideEffect : sideEffects) {
						if (sideEffect.getClass().getSimpleName().equals("IndexIncremented")
								|| sideEffect instanceof AddedDFDPredecessor || sideEffect instanceof AddedDFDSuccessor
								|| sideEffect instanceof AddedExplicitBarrier) {
							// TODO: Verify this.
							// if (sideEffect == UpdateSideEffect.INDEX_INCREMENTED
							// || sideEffect == UpdateSideEffect.ADDED_DFD_PREDECESSOR) {
							callStmtIndex++;
						}
					}
					// Handle internal return statements of newStmt
					handleInternalReturnStatements(callStmt, jumpLabel, newStmt);
					ProfileSS.insertCP();
					if (!labelsHandled) {
						labelsHandled = true;
						assert (newStmt.getInfo().isConnectedToProgram());
						for (Label lab : labels) {
							newStmt.getInfo().addLabelAnnotation(lab);
						}
					}
				}
			}
		}
		if (!labelsHandled) {
			// Create a new null statement with labels.
			labelsHandled = true;
			String nullStr = "";
			for (Label lab : labels) {
				nullStr += lab.toString();
			}
			Statement nullStmt = FrontEnd.parseAndNormalize(nullStr + ";", Statement.class);
			callerScope.getInfo().getCFGInfo().addElement(callStmtIndex, nullStmt);
		}
	}

	/**
	 * Handles internal return statements, from connected nodes.
	 *
	 * @param callStmt
	 * @param jumpLabel
	 * @param newStmt
	 */
	private static void handleInternalReturnStatements(CallStatement callStmt, String jumpLabel, Statement newStmt) {
		for (ReturnStatement internalRetStmt : Misc.getInheritedEncloseeList(newStmt, ReturnStatement.class)) {
			Node currentNode = internalRetStmt;
			Expression internalRetExp = internalRetStmt.getInfo().getReturnExpression();
			if (internalRetExp != null) {
				SimplePrimaryExpression retHolder = callStmt.getPostCallNode().getReturnReceiver();
				if (retHolder == null) {
					currentNode = FrontEnd.parseAndNormalize(internalRetExp + ";", Statement.class);
				} else {
					String assignStr = retHolder.toString() + " = " + internalRetExp + ";";
					currentNode = FrontEnd.parseAndNormalize(assignStr, Statement.class);
				}
				// Renaming would already have been done in the return expression.
				NodeReplacer.replaceNodes(internalRetStmt, currentNode);
				ProfileSS.insertCP();
			}
			// Add jump statement as the successor of current node, if a jump exists.
			if (jumpLabel != null) {
				Statement jumpStmt = FrontEnd.parseAndNormalize("goto " + jumpLabel + ";", Statement.class);
				if (currentNode != internalRetStmt) {
					InsertImmediateSuccessor.insert(currentNode, jumpStmt);
					ProfileSS.insertCP();
				} else {
					NodeReplacer.replaceNodes(internalRetStmt, jumpStmt);
					ProfileSS.insertCP();
				}
			}
		}
	}

	/**
	 * Handles dummy-flushes, variable renaming, and external return statements on
	 * unconnected nodes, to obtain list of
	 * nodes to be added.
	 * <br>
	 * Note that later we should unify this method with
	 * {@link #handleInternalReturnStatements(CallStatement, String,
	 * Statement)}. There are subtle differences.
	 *
	 * @param element
	 * @param callStmt
	 * @param allRenamingMap
	 * @param jumpLabel
	 *
	 * @return
	 */
	private static List<Statement> getStmtToInline(Statement element, CallStatement callStmt,
			HashMap<String, String> allRenamingMap, String jumpLabel) {
		List<Statement> inlineList = new ArrayList<>();
		if (element instanceof DummyFlushDirective) {
			return inlineList;
		}
		if (element instanceof ReturnStatement) {
			ReturnStatement retStmt = (ReturnStatement) element;
			Expression retExp = retStmt.getInfo().getReturnExpression();
			Statement toBeAdded = null;
			if (retExp != null) {
				SimplePrimaryExpression retHolder = callStmt.getPostCallNode().getReturnReceiver();
				if (retHolder == null) {
					// Evaluate the return expression, anyway.
					toBeAdded = FrontEnd.parseAndNormalize(retExp + ";", Statement.class);
				} else {
					// Add assignment
					String assignStr = retHolder.toString() + " = " + retExp + ";";
					toBeAdded = FrontEnd.parseAndNormalize(assignStr, Statement.class);
				}
				toBeAdded = (Statement) BasicTransform.obtainRenamedNode(toBeAdded, allRenamingMap);
				inlineList.add(toBeAdded);
			}
			// Jump to the end label.
			if (jumpLabel != null) {
				toBeAdded = FrontEnd.parseAndNormalize("goto " + jumpLabel + ";", Statement.class);
				inlineList.add(toBeAdded);
			}
		} else {
			Statement renamedElem = (Statement) BasicTransform.obtainRenamedNode(element, allRenamingMap);
			inlineList.add(renamedElem);
		}
		return inlineList;
	}

}
