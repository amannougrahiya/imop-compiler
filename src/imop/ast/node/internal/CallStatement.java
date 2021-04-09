/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.node.internal;

import imop.ast.info.cfgNodeInfo.CallStatementInfo;
import imop.ast.node.external.*;
import imop.lib.cfg.CFGGenerator;
import imop.lib.getter.CallStatementGetter;
import imop.lib.util.Misc;

import java.util.ArrayList;

/**
 * This class represents either a FunctionCall or a MessageSend.
 * 
 * @author aman
 *
 */
public class CallStatement extends Statement {
	{
		classId = 2913;
	}

	public CallStatement() {
		functionDesignatorNode = null;
		preCallNode = null;
		postCallNode = null;
	}

	private static final long serialVersionUID = 4208276186550815164L;

	private final NodeToken functionDesignatorNode;
	private final PreCallNode preCallNode;
	private final PostCallNode postCallNode;

	/**
	 * Represents a context-insensitivity marker.
	 */
	private static CallStatement phantomCall;

	@Override
	public boolean isKnownCFGNode() {
		return true;
	}

	@Override
	public boolean isKnownCFGLeafNode() {
		return false;
	}

	public CallStatement(NodeToken functionDesignatorNode, PreCallNode preCallNode, PostCallNode postCallNode) {
		this.functionDesignatorNode = functionDesignatorNode;
		this.preCallNode = preCallNode;
		this.postCallNode = postCallNode;

		if (this.functionDesignatorNode != null) {
			this.functionDesignatorNode.setParent(this);
		}
		this.preCallNode.setParent(this);
		this.postCallNode.setParent(this);
		CFGGenerator.createCFGEdgesIn(this);
	}

	@Override
	public CallStatementInfo getInfo() {
		if (info == null) {
			info = new CallStatementInfo(this);
		}
		return (CallStatementInfo) info;
	}

	/**
	 * Used by {@link CallStatementGetter} to add a return receiver to this
	 * call-statement.
	 * <p>
	 * Note that this method should not be called to change the return receiver
	 * of an existing call-statement.
	 * 
	 * @param postCallNode
	 *                     a {@link PostCallNode} that represents the return
	 *                     receiver.
	 */
	public void addAReturnReceiver(PostCallNode postCallNode) {
		this.postCallNode.setReturnReceiver(postCallNode.getReturnReceiver());
		this.postCallNode.setParent(this);
	}

	public NodeToken getFunctionDesignatorNode() {
		return functionDesignatorNode;
	}

	public final PreCallNode getPreCallNode() {
		return preCallNode;
	}

	public final PostCallNode getPostCallNode() {
		return postCallNode;
	}

	public static CallStatement getPhantomCall() {
		if (phantomCall == null) {
			phantomCall = new CallStatement(null, new PreCallNode(new ArrayList<>()), new PostCallNode());
		}
		return phantomCall;
	}

	public boolean isPhantom() {
		if (functionDesignatorNode == null) {
			return true;
		} else {
			return false;
		}
	}

	public int getLineNum() {
		return Misc.getLineNum(this.functionDesignatorNode);
	}

	public int getColumnName() {
		return Misc.getColumnNum(this.functionDesignatorNode);
	}

	/**
	 * Returns true if {@code obj} connects to the same owner statement.
	 * 
	 * @param obj
	 *            Object against which this object has to be compared.
	 * @return
	 *         true, if {@code obj} is same as this object.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CallStatement)) {
			return false;
		}
		CallStatement other = (CallStatement) obj;
		if (this.isPhantom()) {
			if (other.isPhantom()) {
				return true;
			} else {
				return false;
			}
		}
		if (other.isPhantom()) {
			if (this.isPhantom()) {
				return true;
			} else {
				return false;
			}
		}
		if (this.functionDesignatorNode.getTokenImage().equals(other.functionDesignatorNode.getTokenImage())
				&& this.postCallNode.equals(other.postCallNode) && this.preCallNode.equals(other.preCallNode)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (this.functionDesignatorNode == null) {
			result = prime * result + ((this.postCallNode == null) ? 0 : this.postCallNode.hashCode());
			result = prime * result + ((this.preCallNode == null) ? 0 : this.preCallNode.hashCode());
			// return Arrays.hashCode(new Object[]{this.postCallNode, this.preCallNode});
		} else {
			String funcStr = this.functionDesignatorNode.getTokenImage();
			result = prime * result + ((funcStr == null) ? 0 : funcStr.hashCode());
			result = prime * result + ((this.postCallNode == null) ? 0 : this.postCallNode.hashCode());
			result = prime * result + ((this.preCallNode == null) ? 0 : this.preCallNode.hashCode());
			// return Arrays.hashCode(new
			// Object[]{this.functionDesignatorNode.getTokenImage(), this.postCallNode,
			// this.preCallNode});
		}
		return result;
	}

	@Override
	public void accept(imop.baseVisitor.Visitor v) {
		v.visit(this);
	}

	@Override
	public <R, A> R accept(imop.baseVisitor.GJVisitor<R, A> v, A argu) {
		return v.visit(this, argu);
	}

	@Override
	public <R> R accept(imop.baseVisitor.GJNoArguVisitor<R> v) {
		return v.visit(this);
	}

	@Override
	public <A> void accept(imop.baseVisitor.GJVoidVisitor<A> v, A argu) {
		v.visit(this, argu);
	}

}
