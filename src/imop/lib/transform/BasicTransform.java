/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.transform;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.deprecated.Deprecated_ImmediatePredecessorInserter;
import imop.lib.analysis.typeSystem.Type;
import imop.lib.builder.Builder;
import imop.lib.cfg.CFGGenerator;
import imop.lib.cfg.info.CFGInfo;
import imop.lib.cfg.info.CompoundStatementCFGInfo;
import imop.lib.cfg.info.ForStatementCFGInfo;
import imop.lib.getter.StringGetter;
import imop.lib.transform.simplify.InsertDummyFlushDirectives;
import imop.lib.transform.updater.InsertImmediatePredecessor;
import imop.lib.transform.updater.InsertImmediateSuccessor;
import imop.lib.transform.updater.NodeRemover;
import imop.lib.transform.updater.NodeReplacer;
import imop.lib.transform.updater.sideeffect.MissingCFGParent;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.lib.transform.updater.sideeffect.SyntacticConstraint;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class BasicTransform {

	public static TranslationUnit root; // Reference to root TranslationUnit

	public static boolean crudeInsertNewBeforeBase(Node baseNode, Node newNode) {
		/*
		 * Find the container-list of the baseNode
		 */
		List<Node> containingList = Misc.getContainingList(baseNode);

		/*
		 * $newNode$ and $baseNode$ might not be directly present in
		 * containingList.
		 * We need to replace them with their appropriate wrappers.
		 */
		Node tempNewNode = Misc.getWrapperInList(newNode);
		baseNode = Misc.getWrapperInList(baseNode);

		if (tempNewNode == null) {
			// Create a wrapper node for newNode
			newNode = FrontEnd.parseAlone(newNode.getInfo().getString(), baseNode.getClass());
		} else {
			newNode = tempNewNode;
		}
		assert (newNode != null && baseNode != null);
		int indexOfM = containingList.indexOf(baseNode);
		containingList.add(indexOfM, newNode);
		return true;
	}

	public static boolean crudeInsertNewAfterBase(Node baseNode, Node newNode) {
		/*
		 * Find the container-list of the baseNode
		 */
		List<Node> containingList = Misc.getContainingList(baseNode);
		if (containingList == null) {
			return false;
		}

		/*
		 * $newNode$ and $baseNode$ might not be directly present in
		 * containingList.
		 * We need to replace them with their appropriate wrappers.
		 */
		Node tempNewNode = Misc.getWrapperInList(newNode);
		baseNode = Misc.getWrapperInList(baseNode);

		if (tempNewNode == null) {
			// Create a wrapper node for newNode, of the type same as that of the wrapper of
			// the baseNode
			newNode = FrontEnd.parseAlone(newNode.getInfo().getString(), baseNode.getClass());
		} else {
			newNode = tempNewNode;
		}

		assert (newNode != null && baseNode != null);

		int indexOfM = containingList.indexOf(baseNode);
		containingList.add(indexOfM + 1, newNode);
		return true;
	}

	public static boolean crudeReplaceBaseWithNewInList(Node baseNode, Node newNode) {
		/*
		 * Find the container-list of the baseNode
		 */
		List<Node> containingList = Misc.getContainingList(baseNode);
		if (containingList == null) {
			return false;
		}

		/*
		 * $newNode$ and $baseNode$ might not be directly present in
		 * containingList
		 * We need to replace them with their appropriate wrappers.
		 */
		Node tempNewNode = Misc.getWrapperInList(newNode);
		baseNode = Misc.getWrapperInList(baseNode);

		if (tempNewNode == null) {
			// Create a wrapper node for newNode, of the type same as that of the wrapper of
			// the baseNode
			newNode = FrontEnd.parseAlone(newNode.getInfo().getString(), baseNode.getClass());
		} else {
			newNode = tempNewNode;
		}

		assert (newNode != null && baseNode != null);

		int indexOfM = containingList.indexOf(baseNode);
		containingList.add(indexOfM, newNode);
		containingList.remove(baseNode);
		return true;
	}

	/**
	 * Assuming <code>m</code> occurs before (not necessarily immediately)
	 * <code>n</code> in <code>list</code>, this method swaps the nodes
	 * <code>m</code>
	 * and <code>n</code>.
	 *
	 * @param baseNode
	 * @param newNode
	 * @param containingList
	 *
	 * @return
	 */
	public static boolean crudeSwapBaseWithNew(Node baseNode, Node newNode) {
		// Make sure that we are working upon the CFG nodes.
		baseNode = Misc.getInternalFirstCFGNode(baseNode);
		newNode = Misc.getInternalFirstCFGNode(newNode);

		// Find the container-list of the baseNode
		List<Node> containingList = Misc.getContainingList(baseNode);
		if (containingList == null) {
			return false;
		}

		baseNode = Misc.getWrapperInList(baseNode);
		newNode = Misc.getWrapperInList(newNode);

		int indexOfM = containingList.indexOf(baseNode);
		int indexOfN = containingList.indexOf(newNode);

		// Make baseNode as left node and newNode as right node in a list.
		if (indexOfM > indexOfN) {
			Node tempNode = baseNode;
			baseNode = newNode;
			newNode = tempNode;
			int tempIndex = indexOfM;
			indexOfM = indexOfN;
			indexOfN = tempIndex;
		}

		assert (newNode != null && baseNode != null && baseNode != newNode);

		containingList.remove(newNode);
		containingList.add(indexOfM, newNode);
		containingList.remove(baseNode);
		containingList.add(indexOfN, baseNode);

		// TODO: Update the line numbers of these nodes.

		/*
		 * Now, we should update the CFG edges of these nodes.
		 * The following temporary code assumes that $base$ and $new$ are next
		 * to each other.
		 * "x1 base new x2" -::= "x1 new base x2"
		 * x1.succ = new;
		 * x2.pred = base;
		 * base.succ = x2;
		 * new.pred = x1;
		 * new.succ = base;
		 * base.pred = new;
		 */
		baseNode = Misc.getInternalFirstCFGNode(baseNode);
		newNode = Misc.getInternalFirstCFGNode(newNode);

		CFGInfo baseNodeCFG = baseNode.getInfo().getCFGInfo();
		CFGInfo newNodeCFG = newNode.getInfo().getCFGInfo();

		Vector<Node> x1List = new Vector<>(baseNodeCFG.getPredBlocks());
		Vector<Node> x2List = new Vector<>(newNodeCFG.getSuccBlocks());

		// Note: The following code works for the case when newNode is an immediate
		// successor of the baseNode.
		// TODO: Make the following code work for a general setting, when needed.
		// (The semantics can be difficult to define in a general setting.)
		newNodeCFG.getPredBlocks().remove(baseNode);
		baseNodeCFG.getSuccBlocks().remove(newNode);
		for (Node x1 : x1List) {
			CFGInfo x1CFG = x1.getInfo().getCFGInfo();
			x1CFG.getSuccBlocks().remove(baseNode);
			x1CFG.getSuccBlocks().add(newNode);
			newNodeCFG.getPredBlocks().add(x1);
		}

		for (Node x2 : x2List) {
			CFGInfo x2CFG = x2.getInfo().getCFGInfo();
			x2CFG.getPredBlocks().remove(newNode);
			x2CFG.getPredBlocks().add(baseNode);
			baseNodeCFG.getSuccBlocks().add(x2);
		}

		newNodeCFG.getSuccBlocks().add(baseNode);
		baseNodeCFG.getPredBlocks().add(newNode);

		return true;
	}

	/**
	 * Replaces a node in the AST with some other node, without changing anything
	 * else.
	 *
	 * @param baseNode
	 * @param newNode
	 *
	 * @return true, if the replacement is successful.
	 *
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static <T extends Node> boolean crudeReplaceOldWithNew(T baseNode, T newNode)
			throws IllegalArgumentException, IllegalAccessException {
		return CrudeReplaceNode.replace(baseNode, newNode);
	}

	/**
	 * Splits compound statement into finer pieces. e.g., {S1; S2; S3;} is split
	 * into {S1;} {S2;} {S3;}
	 *
	 * @param node
	 *
	 * @return true, if the transformation is successful.
	 */
	public static boolean splitCompoundStatement(CompoundStatement node) {

		// TODO: Handle declarations during splitting.

		Vector<CompoundStatementElement> newList = new Vector<>();
		if ((!node.getF1().present()) || node.getF1().getNodes().size() == 1) {
			return false;
		}

		// Create a vector of compound statement elements
		for (Node element : node.getF1().getNodes()) {
			CompoundStatementElement newCSE = FrontEnd.parseAlone("{" + element.getInfo().getString() + "}",
					CompoundStatementElement.class);
			newCSE.getF0().setChoice(element);
			newList.add(newCSE);
		}

		// Change the node $node$ with the newly created list of
		// compoundStatementElements.
		List<Node> containingList = Misc.getContainingList(node);
		int indexOfNode = Misc.getIndexInContainingList(node);

		if (containingList == null) {
			return false;
		}

		containingList.remove(Misc.getWrapperInList(node));
		containingList.addAll(indexOfNode, newList);

		// Now, update the analysis outputs for the newly created vector of compound
		// statements.
		// Node containingNode = Misc.getContainerListNode(node);
		// containingNode.accept(new ParentBuilder(), containingNode.getParent());

		// Updating CFG edges
		for (Node newNode : newList) {
			CFGInfo cfgInfo = Misc.getInternalFirstCFGNode(newNode).getInfo().getCFGInfo();
			cfgInfo.getPredBlocks().clear();
			cfgInfo.getSuccBlocks().clear();
		}

		Node firstNode = Misc.getInternalFirstCFGNode(newList.firstElement());
		for (Node pred : node.getInfo().getCFGInfo().getPredBlocks()) {
			CFGInfo predInfo = pred.getInfo().getCFGInfo();
			predInfo.getSuccBlocks().remove(node);
			predInfo.getSuccBlocks().add(firstNode);
			firstNode.getInfo().getCFGInfo().getPredBlocks().add(pred);
		}

		Node lastNode = Misc.getInternalFirstCFGNode(newList.lastElement());
		for (Node succ : node.getInfo().getCFGInfo().getSuccBlocks()) {
			CFGInfo succInfo = succ.getInfo().getCFGInfo();
			succInfo.getPredBlocks().remove(node);
			succInfo.getPredBlocks().add(lastNode);
			lastNode.getInfo().getCFGInfo().getSuccBlocks().add(succ);
		}

		for (Node tempNode : newList) {
			Node newNode = Misc.getInternalFirstCFGNode(tempNode);
			if (tempNode == newList.firstElement()) {
				continue;
			} else {
				Node prevTempNode = newList.get(newList.indexOf(tempNode) - 1);
				Node prevNode = Misc.getInternalFirstCFGNode(prevTempNode);
				prevNode.getInfo().getCFGInfo().getSuccBlocks().add(newNode);
				newNode.getInfo().getCFGInfo().getPredBlocks().add(prevNode);
			}
		}

		return true;
	}

	/**
	 * Returns a copy of the given node with its IDs renamed as per the
	 * {@code nameMap}.
	 * <br>
	 * DO NOT pass a dummy-flush directive to this node.
	 *
	 * @param node
	 *                old node on whose copy the replacement has to be done.
	 * @param nameMap
	 *                a map from old id names to new id names.
	 *
	 * @return new node with replacements done as per the nameMap.
	 */
	public static Node obtainRenamedNode(Node node, HashMap<String, String> nameMap) {
		Node replacementNode = node;
		if (node instanceof DummyFlushDirective) {
			assert (false);
			return null;
		} else if (node instanceof Statement) {
			// This needs to be done as a "Statement" since "inside" node may have labels,
			// due to which it might not get parsed as its CFG type.
			String replacementString = StringGetter.getRenamedString(node, nameMap);
			replacementNode = FrontEnd.parseAndNormalize(replacementString, Statement.class);
		} else if (node instanceof PreCallNode) {
			PreCallNode oldPre = (PreCallNode) node;

			// Obtain the new pre-call node with renamed variables.
			List<SimplePrimaryExpression> newSPEList = new ArrayList<>();
			for (SimplePrimaryExpression oldSPE : oldPre.getArgumentList()) {
				SimplePrimaryExpression newSPE = null;
				if (oldSPE.isAConstant()) {
					newSPE = new SimplePrimaryExpression(oldSPE.getConstant());
				} else {
					String oldIdStr = oldSPE.getIdentifier().getTokenImage();
					String newIdStr = nameMap.get(oldIdStr);
					NodeToken newId = null;
					if (newIdStr == null) {
						newId = oldSPE.getIdentifier();
					} else {
						newId = new NodeToken(newIdStr);
					}
					newSPE = new SimplePrimaryExpression(newId);
				}
				newSPEList.add(newSPE);
			}
			PreCallNode newPre = new PreCallNode(newSPEList);

			// Replace the enclosing call-statement with the new call-statement.
			// Make sure that same post-call node is transferred from old to new
			// call-statement,
			// so that the list in renamingOnNodes.keySet() remains valid.
			CallStatement oldCallStmt = oldPre.getParent();
			replacementNode = new CallStatement(oldCallStmt.getFunctionDesignatorNode(), newPre,
					oldCallStmt.getPostCallNode());
		} else if (node instanceof PostCallNode) {
			PostCallNode oldPost = (PostCallNode) node;

			// Obtain the new post-call node with renamed variable.
			SimplePrimaryExpression oldSPE = oldPost.getReturnReceiver();
			SimplePrimaryExpression newSPE = null;
			if (oldSPE.isAConstant()) {
				newSPE = new SimplePrimaryExpression(oldSPE.getConstant());
			} else {
				String oldIdStr = oldSPE.getIdentifier().getTokenImage();
				String newIdStr = nameMap.get(oldIdStr);
				NodeToken newId = null;
				if (newIdStr == null) {
					newId = oldSPE.getIdentifier();
				} else {
					newId = new NodeToken(newIdStr);
				}
				newSPE = new SimplePrimaryExpression(newId);
			}
			PostCallNode newPost = new PostCallNode(newSPE);

			// Replace the enclosing call-statement with the new call-statement.
			// Make sure that same pre-call node is transferred from old to new
			// call-statement,
			// so that the list in renamingOnNodes.keySet() remains valid.
			CallStatement oldCallStmt = oldPost.getParent();
			replacementNode = new CallStatement(oldCallStmt.getFunctionDesignatorNode(), oldCallStmt.getPreCallNode(),
					newPost);
		} else {
			String replacementString = StringGetter.getRenamedString(node, nameMap);
			replacementNode = FrontEnd.parseAndNormalize(replacementString, node.getClass());
		}
		return replacementNode;
	}

	/**
	 * Replaces {@code node} by a new node that has its IDs renamed as per the
	 * {@code nameMap}.
	 *
	 * @param node
	 *                old node in which replacements as per the nameMaps have to be
	 *                done.
	 * @param nameMap
	 *                a map from old id names to new id names.
	 *
	 * @return new node with replacements done as per the nameMap.
	 */
	public static Node renameIdsInNodes(Node node, HashMap<String, String> nameMap) {
		validateRenamingMap(nameMap);
		boolean found = false;
		String str = node.toString();
		for (String key : nameMap.keySet()) {
			if (str.contains(key)) {
				found = true;
				break;
			}
		}
		if (!found) {
			return node;
		}
		Node replacementNode = node;
		if (node instanceof Statement) {
			/*
			 * This needs to be done as a "Statement" since "inside" node may
			 * have labels, due to which it might not get parsed as its CFG
			 * type.
			 */
			String replacementString = StringGetter.getRenamedString(node, nameMap);
			replacementNode = FrontEnd.parseAndNormalize(replacementString, Statement.class);
			NodeReplacer.replaceNodes(node, replacementNode);
		} else if (node instanceof PreCallNode) {
			PreCallNode oldPre = (PreCallNode) node;

			// Obtain the new pre-call node with renamed variables.
			List<SimplePrimaryExpression> newSPEList = new ArrayList<>();
			for (SimplePrimaryExpression oldSPE : oldPre.getArgumentList()) {
				SimplePrimaryExpression newSPE = null;
				if (oldSPE.isAConstant()) {
					newSPE = new SimplePrimaryExpression(oldSPE.getConstant());
				} else {
					String oldIdStr = oldSPE.getIdentifier().getTokenImage();
					String newIdStr = nameMap.get(oldIdStr);
					NodeToken newId = null;
					if (newIdStr == null) {
						newId = oldSPE.getIdentifier();
					} else {
						newId = new NodeToken(newIdStr);
					}
					newSPE = new SimplePrimaryExpression(newId);
				}
				newSPEList.add(newSPE);
			}
			PreCallNode newPre = new PreCallNode(newSPEList);

			// Replace the enclosing call-statement with the new call-statement.
			// Make sure that same post-call node is transferred from old to new
			// call-statement,
			// so that the list in renamingOnNodes.keySet() remains valid.
			CallStatement oldCallStmt = oldPre.getParent();
			replacementNode = new CallStatement(oldCallStmt.getFunctionDesignatorNode(), newPre,
					oldCallStmt.getPostCallNode());
			NodeReplacer.replaceNodes(oldCallStmt, replacementNode);
		} else if (node instanceof PostCallNode) {
			PostCallNode oldPost = (PostCallNode) node;

			// Obtain the new post-call node with renamed variable.
			SimplePrimaryExpression oldSPE = oldPost.getReturnReceiver();
			SimplePrimaryExpression newSPE = null;
			if (oldSPE.isAConstant()) {
				newSPE = new SimplePrimaryExpression(oldSPE.getConstant());
			} else {
				String oldIdStr = oldSPE.getIdentifier().getTokenImage();
				String newIdStr = nameMap.get(oldIdStr);
				NodeToken newId = new NodeToken(newIdStr);
				newSPE = new SimplePrimaryExpression(newId);
			}
			PostCallNode newPost = new PostCallNode(newSPE);

			// Replace the enclosing call-statement with the new call-statement.
			// Make sure that same pre-call node is transferred from old to new
			// call-statement,
			// so that the list in renamingOnNodes.keySet() remains valid.
			CallStatement oldCallStmt = oldPost.getParent();
			replacementNode = new CallStatement(oldCallStmt.getFunctionDesignatorNode(), oldCallStmt.getPreCallNode(),
					newPost);
			NodeReplacer.replaceNodes(oldCallStmt, replacementNode);
		} else {
			String replacementString = StringGetter.getRenamedString(node, nameMap);
			replacementNode = FrontEnd.parseAndNormalize(replacementString, node.getClass());
			NodeReplacer.replaceNodes(node, replacementNode);
		}
		return replacementNode;
	}

	private static void validateRenamingMap(HashMap<String, String> nameMap) {
		for (String strSrc : nameMap.keySet()) {
			for (String strDest : nameMap.values()) {
				if (strSrc.equals(strDest)) {
					Thread.dumpStack();
					Misc.exitDueToError("Cannot perform conlifcting renaming for " + strSrc + " to "
							+ nameMap.get(strSrc) + " when another renaming has that name as the destination.");
				}
			}
		}
	}

	/**
	 * Pushes the declaration to the least possible index in the enclosing scope.
	 *
	 * @param decl
	 *             declaration that needs to be pushed up.
	 *
	 * @return
	 */
	public static List<SideEffect> pushDeclarationUp(Declaration decl) {
		List<SideEffect> sideEffects = new ArrayList<>();
		CompoundStatement scope = (CompoundStatement) Misc.getEnclosingBlock(decl);
		if (scope == null) {
			Misc.warnDueToLackOfFeature(
					"Could not find any enclosing block for the declaration that needs to be moved higher.", null);
			sideEffects.add(new MissingCFGParent(decl));
			return sideEffects;
		}
		CompoundStatementCFGInfo scopeInfo = scope.getInfo().getCFGInfo();
		List<Node> elementList = scopeInfo.getElementList();
		int index = elementList.indexOf(decl);
		if (index == 0) {
			return sideEffects;
		}
		/*
		 * Obtain the highest point at which this declaration can be pushed
		 * within the scope.
		 */
		int finalIndex = index;
		for (int i = index; i >= 1; i--) {
			Node prevNode = elementList.get(i - 1);
			if (decl.getInfo().hasInitializer()) {
				if (Misc.haveDataDependences(decl.getInfo().getInitializer(), prevNode)) {
					break;
				}
			}
			if (decl.getInfo().clashesSyntacticallyWith(prevNode)) {
				break;
			}
			finalIndex--;
		}

		if (finalIndex < index) {
			// Move the declaration.
			sideEffects.addAll(scopeInfo.removeDeclaration(decl));
			sideEffects.addAll(scopeInfo.addDeclaration(finalIndex, decl));
		} else {
			sideEffects.add(new SyntacticConstraint(decl, ""));
		}

		return sideEffects;
	}

	/**
	 * If the initializer contains any side-effects, this method updates the
	 * declaration such that the initializer has
	 * been simplified. This method returns true, if the simplification was
	 * performed.
	 *
	 * @param decl
	 *             declaration whose initializer needs to be simplified.
	 *
	 * @return
	 */
	public static boolean removeSideEffectsFromInitializer(Declaration decl) {
		Initializer init = decl.getInfo().getInitializer();
		if (init == null) {
			return false;
		}
		if (!init.getInfo().mayWrite()) {
			return false;
		}
		CompoundStatement scope = (CompoundStatement) Misc.getEnclosingBlock(decl);
		if (scope == null) {
			return false;
		}
		// Too bad! Now, we need to simplify the declaration, adjust the indices, and
		// notify.
		Type typeOfInit = Type.getType(init);
		String tempName = Builder.getNewTempName(decl.getInfo().getDeclaredName() + "Init");
		Declaration simplifyingTemp = typeOfInit.getDeclaration(tempName);
		InsertImmediatePredecessor.insert(decl, simplifyingTemp);

		Statement simplifyingExp = FrontEnd.parseAndNormalize(tempName + " = " + init.toString() + ";",
				Statement.class);
		InsertImmediatePredecessor.insert(decl, simplifyingExp);

		Declarator declarator = decl.getInfo().getDeclarator(decl.getInfo().getDeclaredName());
		String newDeclString = decl.getF0().toString() + " " + declarator + " = " + tempName + ";";
		Declaration newDecl = FrontEnd.parseAndNormalize(newDeclString, Declaration.class);
		NodeReplacer.replaceNodes(decl, newDecl);
		return true;
	}

	public static List<SideEffect> simplifyPredicate(IterationStatement itStmt) {
		List<SideEffect> sideEffectList = new ArrayList<>();
		if (itStmt instanceof WhileStatement) {
			WhileStatement whileStmt = (WhileStatement) itStmt;
			return whileStmt.getInfo().changePredicateToConstantTrue();
		} else if (itStmt instanceof DoStatement) {
			assert (false);
			// TODO: Code here.
		} else if (itStmt instanceof ForStatement) {
			assert (false);
			// TODO: Code here.
		}
		return sideEffectList;
	}

	/**
	 * Converts all do-while loops within {@code root} to while loop.
	 *
	 * @param root
	 *             the AST node below which all do-while loops need to be converted
	 *             into a while loop.
	 */
	public static DoStatement convertAllDoWhileToWhile(Node root) {
		for (DoStatement doStmt : Misc.getExactEnclosee(root, DoStatement.class)) {
			// TODO: Code here.
		}
		return null;
	}

	public static IterationStatement convertToWhile(IterationStatement itStmt) {
		if (itStmt instanceof WhileStatement) {
			return itStmt;
		} else if (itStmt instanceof DoStatement) {
			// TODO: Code here.
			return itStmt;
		} else {
			ForStatementCFGInfo cfgInfo = (ForStatementCFGInfo) itStmt.getInfo().getCFGInfo();
			if (cfgInfo.hasStepExpression()) {
				Expression stepExpr = cfgInfo.getStepExpression();
				for (Node pred : stepExpr.getInfo().getCFGInfo().getPredBlocks()) {
					Statement expStmt = FrontEnd.parseAndNormalize(stepExpr + ";", Statement.class);
					InsertImmediatePredecessor.insert(pred, expStmt);
				}
				cfgInfo.removeStepExpression();
			}

			StringBuilder whileStr = new StringBuilder();
			if (cfgInfo.hasInitExpression()) {
				Statement initStmt = FrontEnd.parseAndNormalize(cfgInfo.getInitExpression() + ";", Statement.class);
				InsertImmediatePredecessor.insert(itStmt, initStmt);
				cfgInfo.removeInitExpression();
			}
			whileStr.append("while (").append(cfgInfo.getTerminationExpression()).append(")").append(cfgInfo.getBody());
			WhileStatement whileStmt = FrontEnd.parseAndNormalize(whileStr.toString(), WhileStatement.class);
			NodeReplacer.replaceNodes(itStmt, whileStmt);
			return whileStmt;
		}
	}

	/**
	 * This method removes empty OpenMP constructs from the given {@code node}.
	 *
	 * @param node
	 *             node from within which empty OpenMP constructs have to be
	 *             removed.
	 */
	public static void removeEmptyConstructs(Node node) {
		for (Node openNode : Misc.getExactEnclosee(node, OmpConstruct.class)) {
			Statement stmt = openNode.getInfo().getCFGInfo().getBody();
			if (stmt == null || !(stmt instanceof CompoundStatement)) {
				continue;
			}
			CompoundStatement compStmt = (CompoundStatement) stmt;
			if (compStmt.getInfo().getCFGInfo().getElementList().isEmpty()) {
				boolean needsBarrier = false;
				boolean needsFlush = false;
				if (openNode instanceof SingleConstruct) {
					SingleConstruct singleCons = (SingleConstruct) openNode;
					if (!singleCons.getInfo().hasNowaitClause()) {
						needsBarrier = true;
					}
				} else if (openNode instanceof ForConstruct) {
					ForConstruct forCons = (ForConstruct) openNode;
					if (!forCons.getInfo().hasNowaitClause()) {
						needsBarrier = true;
					}
				} else if (openNode instanceof SectionsConstruct) {
					SectionsConstruct secCons = (SectionsConstruct) openNode;
					if (!secCons.getInfo().hasNowaitClause()) {
						needsBarrier = true;
					}
				}
				if (InsertDummyFlushDirectives.requiresRemovalOfDFDs(openNode)) {
					needsFlush = true;
				}

				/*
				 * Add a flush/barrier, if requited, before/after the node.
				 */
				if (needsBarrier) {
					BarrierDirective barrDir = FrontEnd.parseAndNormalize("#pragma omp barrier\n",
							BarrierDirective.class);
					InsertImmediateSuccessor.insert(openNode, barrDir);
				}
				if (needsFlush) {
					FlushDirective flushDir = FrontEnd.parseAndNormalize("#pragma omp flush\n", FlushDirective.class);
					InsertImmediatePredecessor.insert(openNode, flushDir);
				}

				/*
				 * Remove the openNode.
				 */
				NodeRemover.removeNode(openNode);
			}
		}
	}

	// /**
	// * Removes shared accesses from predicates to all loops and conditionals.
	// */
	// public static void removeSharedFromPredicates() {
	// TranslationUnit program = Program.root;
	// for (FunctionDefinition func : Misc.getInheritedEnclosee(program,
	// FunctionDefinition.class)) {
	// for (Node cfgNode : func.getInfo().getCFGInfo().getLexicalCFGLeafContents())
	// {
	// if (!(cfgNode instanceof Expression)) {
	// continue;
	// }
	// if (!Misc.isAPredicate(cfgNode)) {
	// continue;
	// }
	// if (cfgNode.getInfo().getSharedAccesses().isEmpty()) {
	// continue;
	// }
	// Expression predicate = (Expression) cfgNode;
	// CFGLink link = CFGLinkFinder.getCFGLinkFor(cfgNode);
	// String newTempName = Builder.getNewTempName("ifPred");
	// Type type = Type.getType(predicate);
	// Declaration decl = type.getDeclaration(newTempName);
	// Statement assign = FrontEnd.parseAndNormalize(newTempName + " = " + predicate
	// + ";", Statement.class);
	// // Note that assign might not be the best option always. Consider for's e1
	// and e3.
	// // Also, use a CFGLink visitor instead.
	//
	// if (link instanceof IfPredicateLink) {
	//
	// } else if (link instanceof SwitchPredicateLink) {
	//
	// } else if (link instanceof DoPredicateLink) {
	//
	// } else if (link instanceof WhilePredicateLink) {
	//
	// } else if (link instanceof ForInitLink) {
	//
	// } else if (link instanceof ForTermLink) {
	//
	// } else if (link instanceof ForStepLink) {
	//
	// }
	// }
	// }
	// }

	/**
	 * Adds {@code predecessor} as the only immediate predecessor of
	 * {@code baseNode} on all the paths.
	 *
	 * @param baseNode
	 *                    node before which a CFG node has to be inserted.
	 * @param predecessor
	 *                    node which has to be inserted immediately before
	 *                    {@code baseNode}
	 *
	 * @deprecated
	 */
	@Deprecated
	public static void deprecated_insertImmediatePredecessor(Node baseNode, Node predecessor) {
		baseNode = Misc.getCFGNodeFor(baseNode);
		Node parentNode = Misc.getEnclosingCFGNonLeafNode(baseNode);
		CFGGenerator.createCFGEdgesIn(predecessor);

		Deprecated_ImmediatePredecessorInserter updater = new Deprecated_ImmediatePredecessorInserter(baseNode,
				predecessor);
		parentNode.accept(updater);
	}

	@Deprecated
	public static void deprecated_insertImmediateSuccessor(Node baseNode, Node successor) {
		baseNode = Misc.getCFGNodeFor(baseNode);
		Node parentNode = Misc.getEnclosingCFGNonLeafNode(baseNode);
		Deprecated_ImmediatePredecessorInserter updater = new Deprecated_ImmediatePredecessorInserter(baseNode,
				successor);
		parentNode.accept(updater);
	}

}
