/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.projects.datarace;

import java.util.Vector;

import imop.ast.node.external.*;
import imop.baseVisitor.GJDepthFirst;
import imop.lib.getter.StringGetter;

/**
 * This visitor when called on a node with argu having a Vector<Access>,
 * will populate the vector with accesses to variables, rooted under the node.
 */
public class AccessBuilder extends GJDepthFirst<Object, Object> {
	public DRCommonUtils commUtil = new DRCommonUtils();

	/**
	 * f0 ::= UnaryExpression()
	 * f1 ::= AssignmentOperator()
	 * f2 ::= AssignmentExpression()
	 */
	@Override
	public Object visit(NonConditionalExpression n, Object argu) {
		Object _ret = null;
		n.getF0().accept(this, argu); // Let UE accumulate reads/writes

		// Now, the complete string of UE, that was initially assumed to be read, is actually being written
		StringGetter strGetter = new StringGetter();
		n.getF0().accept(strGetter);
		boolean found = false;		// For checking if read access is indeed found
		for (Access acc : (Vector<Access>) argu) {
			if (strGetter.str.equals(acc.location)) {
				acc.mode = 1;
				found = true;
				break;
			}
		}
		if (!found) {
			((Vector<Access>) argu).add(new Access(new String(strGetter.str), 1));
		}

		n.getF2().accept(this, argu); // Let AE accumulate reads/writes
		return _ret;
	}

	/**
	 * f0 ::= <SIZEOF>
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public Object visit(SizeofUnaryExpression n, Object argu) {
		Object _ret = null;
		// Don't let UE accumulate any reads/writes
		return _ret;
	}

	/**
	 * f0 ::= "++"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public Object visit(UnaryExpressionPreIncrement n, Object argu) {
		Object _ret = null;
		n.getF1().accept(this, argu); // Let UE accumulate reads/writes

		//Now, the complete string of UE is being written to as well, due to ++
		StringGetter strGetter = new StringGetter();
		n.getF1().accept(strGetter);
		((Vector<Access>) argu).add(new Access(new String(strGetter.str), 1));
		return _ret;
	}

	/**
	 * f0 ::= "--"
	 * f1 ::= UnaryExpression()
	 */
	@Override
	public Object visit(UnaryExpressionPreDecrement n, Object argu) {
		Object _ret = null;
		n.getF1().accept(this, argu); // Let UE accumulate reads/writes

		//Now, the complete string of UE is being written to as well, due to --
		StringGetter strGetter = new StringGetter();
		n.getF1().accept(strGetter);
		((Vector<Access>) argu).add(new Access(new String(strGetter.str), 1));
		return _ret;
	}

	/**
	 * f0 ::= UnaryOperator()
	 * f1 ::= CastExpression()
	 */
	@Override
	public Object visit(UnaryCastExpression n, Object argu) {
		Object _ret = null;
		n.getF1().accept(this, argu); // Let CE accumulate reads/writes

		StringGetter strGetter;
		// Now depending on the operator being used, add other accesses
		switch (n.getF0().getF0().choice.toString()) {
		case "&":
			// Remove the complete string of CE from the accesses.
			strGetter = new StringGetter();
			n.getF1().accept(strGetter);
			int i = 0;	// This shall contain the index of the access to be removed from argu. 
			for (Access acc : (Vector<Access>) argu) {
				if (acc.location.equals(strGetter.str)) {
					break;
				}
				i++;
			}
			if (i < ((Vector<Access>) argu).size()) {		// Remove the element (index i) matching the complete string of CE
				((Vector<Access>) argu).remove(i);
			} else {
				//System.err.println("Didn't add " + strGetter.str);
			}
			break;

		case "*":
			// For "*CE", we need to add a read access of "*CE" 
			strGetter = new StringGetter();
			n.getF1().accept(strGetter);
			String addThis = "*" + strGetter.str; // New read location (access) to be added
			((Vector<Access>) argu).add(new Access(addThis, 0)); // Adding in read mode
			break;

		// For +, -, ~ and !, we don't need to add/remove any extra accesses than those by CE
		case "+":
			break;
		case "-":
			break;
		case "~":
			break;
		case "!":
			break;

		// Code shouldn't reach here
		default:
			System.out.println("Some issue in DataAccessBuilder::UnaryCastExpression");
			System.exit(1);
		}
		return _ret;
	}

	/**
	 * f0 ::= PrimaryExpression()
	 * f1 ::= PostfixOperationsList()
	 */
	@Override
	public Object visit(PostfixExpression n, Object argu) {
		Object _ret = null;
		n.getF0().accept(this, argu); // Let PE accumulate reads/writes to argu

		// If there are no postFix operations, add the read access to ID, if PE is ID
		if (n.getF1().getF0().nodes.isEmpty()) {
			if (n.getF0().getF0().choice.getClass().getSimpleName().equals("NodeToken")) {
				((Vector<Access>) argu).add(new Access(((NodeToken) n.getF0().getF0().choice).tokenImage, 0));
			}
			return null;
		}

		// For all postFix operations, add appropriate reads/writes
		for (Node nodeTemp : n.getF1().getF0().nodes) {
			nodeTemp.accept(this, argu); // Call accept on the arguments

			// For "___ Op", add access to "___"
			int index = n.getF1().getF0().nodes.indexOf(nodeTemp);
			((Vector<Access>) argu).add(new Access(getPreceedingString(n.getF0(), n.getF1().getF0(), index), 0));

			// For "___ ++" or "___ --" add write access to "___"
			switch (((APostfixOperation) nodeTemp).getF0().choice.getClass().getSimpleName()) {
			case "PlusPlus":
				((Vector<Access>) argu).add(new Access(getPreceedingString(n.getF0(), n.getF1().getF0(), index), 1));
				break;
			case "MinusMinus":
				((Vector<Access>) argu).add(new Access(getPreceedingString(n.getF0(), n.getF1().getF0(), index), 1));
				break;
			}
			// If this is the last operator in this PostfixExpression, add the whole expression as a read, if operator permits
			if (nodeTemp == n.getF1().getF0().nodes.get(n.getF1().getF0().nodes.size() - 1)) {
				switch (((APostfixOperation) nodeTemp).getF0().choice.getClass().getSimpleName()) {
				case "BracketExpression":
					((Vector<Access>) argu)
							.add(new Access(getPreceedingString(n.getF0(), n.getF1().getF0(), n.getF1().getF0().nodes.size()), 0));
					break;
				case "ArgumentList":
					break;
				case "DotId":
					((Vector<Access>) argu)
							.add(new Access(getPreceedingString(n.getF0(), n.getF1().getF0(), n.getF1().getF0().nodes.size()), 0));
					break;
				case "ArrowId":
					((Vector<Access>) argu)
							.add(new Access(getPreceedingString(n.getF0(), n.getF1().getF0(), n.getF1().getF0().nodes.size()), 0));
					break;
				case "PlusPlus":
					break;
				case "MinusMinus":
					break;
				default:
					System.out.println("Something wrong in PostfixExpression!");
					assert false;
				}
			}
		}

		return _ret;
	}

	/**
	 * This method returns the string obtained by combining the pe
	 * with all the elements in list, from index 0 to (before-1).
	 * 
	 * @param pe
	 * @param list
	 * @param before
	 * @return
	 */
	String getPreceedingString(PrimaryExpression pe, NodeListOptional list, int before) {
		// Add pe to the result
		String result = new String();
		StringGetter strGetter = new StringGetter();
		pe.accept(strGetter);
		result += strGetter.str;

		// Add all the elements from index 0 to (before-1) from list
		for (Node element : list.nodes) {
			if (list.nodes.indexOf(element) == before) {
				break;
			}
			strGetter = new StringGetter();
			element.accept(strGetter);
			result += strGetter.str;
		}
		return result;
	}
}
