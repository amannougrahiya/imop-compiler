/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.cg;

import imop.ast.node.internal.*;
import imop.parser.Program;

import java.util.Stack;

public class CallStack {

	private Stack<CallStatement> callStack;

	public CallStack() {
		callStack = new Stack<>();
	}

	public CallStack(CallStack other) {
		this.callStack = new Stack<>();
		this.callStack.addAll(other.callStack);
	}

	public static CallStack pushUnique(CallStack otherStack, CallStatement callStmt) {
		CallStack retStack = new CallStack(otherStack);
		if (otherStack.callStack.isEmpty()) {
			// push, if empty.
			retStack.callStack.push(callStmt);
			return retStack;
		}
		if (otherStack.callStack.peek() == CallStatement.getPhantomCall()) {
			// do nothing.
			return retStack;
		}

		if (Program.oneEntryPerFDInCallStack) {
			if (otherStack.callStack.stream().anyMatch(cs -> cs.getFunctionDesignatorNode().toString()
					.equals(callStmt.getFunctionDesignatorNode().toString()))) {
				// NOTE: Here is the code that determines how to handle recursion.
				// push a phantom-call, instead.
				retStack.callStack.clear();
				retStack.callStack.push(CallStatement.getPhantomCall());
				return retStack;
			}
		} else {
			if (otherStack.callStack.contains(callStmt)) {
				// push a phantom-call, instead.
				// NOTE: Here is the code that determines how to handle recursion.
				retStack.callStack.clear();
				retStack.callStack.push(CallStatement.getPhantomCall());
				return retStack;
			}
		}

		if (callStmt == CallStatement.getPhantomCall()) {
			// push the phantom-call.
			retStack.callStack.clear();
			retStack.callStack.push(callStmt);
			return retStack;
		}
		retStack.callStack.push(callStmt);
		return retStack;
	}

	public static CallStack pop(CallStack otherStack) {
		CallStack retStack = new CallStack(otherStack);
		if (retStack.callStack.isEmpty()) {
			// push a phantom-call, if empty.
			retStack.callStack.push(CallStatement.getPhantomCall());
			// FIXED: Following return was missing.
			return retStack;
		}
		if (retStack.callStack.peek() == CallStatement.getPhantomCall()) {
			return retStack;
		}
		retStack.callStack.pop();
		return retStack;
	}

	public CallStatement peek() {
		if (callStack.isEmpty()) {
			// push a phantom-call, if empty.
			callStack.push(CallStatement.getPhantomCall());
		}
		return callStack.peek();
	}

	public int size() {
		return callStack.size();
	}

	@Override
	public String toString() {
		String ret = "[";
		if (callStack != null) {
			for (CallStatement cS : callStack) {
				ret += cS + " ";
			}
		}
		ret += "]";
		return ret;
	}

	public void print() {
		for (CallStatement cS : callStack) {
			System.err.print(cS + " -::= ");
		}
	}
	//
	// public Stack<CallStatement> getStackCopy() {
	// Stack<CallStatement> callStackCopy = new Stack<CallStatement>();
	// callStackCopy.addAll(callStack);
	// return callStackCopy;
	// }

	/**
	 * Informs whether this call stack object is context-sensitive in nature.
	 * 
	 * @return
	 *         {@code true} if this call stack is context-sensitive in nature.
	 */
	public boolean isContextSensitiveStack() {
		if (callStack == null) {
			return true;
		}
		if (callStack.isEmpty() || callStack.peek() == CallStatement.getPhantomCall()) {
			return false;
		}
		return true;
	}

	//
	// public Stack<CallStatement> getStackCopy() {
	// Stack<CallStatement> callStackCopy = new Stack<CallStatement>();
	// callStackCopy.addAll(callStack);
	// return callStackCopy;
	// }

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof CallStack)) {
			return false;
		}
		CallStack other = (CallStack) obj;
		if (this.callStack.equals(other.callStack)) {
			return true;
		} else {
			return false;
		}
		// if (this.callStack.size() != other.callStack.size()) {
		// return false;
		// }
		// for (CallStatement callStmtThis : callStack) {
		// int index = callStack.indexOf(callStmtThis);
		// CallStatement callStmtOther = other.callStack.get(index);
		// if (!callStmtThis.equals(callStmtOther)) {
		// return false;
		// }
		// }
		// return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((callStack == null) ? 0 : callStack.hashCode());
		return result;
		// return Arrays.hashCode(new Object[] {this.callStack});
	}
}
