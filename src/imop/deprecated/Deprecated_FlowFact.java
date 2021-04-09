/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.deprecated;

import imop.ast.node.external.*;

@Deprecated
public abstract class Deprecated_FlowFact {
	/**
	 * Returns true if {@code other} is semantically
	 * same as this object.
	 * 
	 * <i>Note that we do not use equals() methods, since we want to enforce
	 * the subclasses of {@code FlowFact} to define this equality.</i>
	 * 
	 * @param other
	 *              the object to be checked for equality with this object.
	 * @return
	 *         true, if <code>other</code> is same as this object.
	 */
	public abstract boolean isEqualTo(Deprecated_FlowFact other);

	/**
	 * Prints the information represented by this object
	 * in a human-readable form.
	 * TODO: Make this method take a stream as an argument,
	 * where the information is dumped, instead of at stderr.
	 */
	public abstract void printFact();

	/**
	 * Given an instance of a FlowFact <code>other</code>, this method should be
	 * overridden to
	 * return the meet of the receiver with $other$.
	 * <i>Note: Make sure that this method returns a new object every time, if
	 * needed.</i>
	 * 
	 * @param other
	 *              one of the flowFacts. Can be null.
	 * @return
	 *         meet of the flowFact passed to this method, with the receiver.
	 */
	public abstract Deprecated_FlowFact meetWith(Deprecated_FlowFact other);

	/**
	 * Given an instance of a FlowFact <code>other</code>, this method should be
	 * overridden to
	 * return the meet of the receiver with $other$.
	 * Note: Make sure that this method returns a new object every time, if
	 * needed.
	 * 
	 * @param other
	 *              one of the flowFacts. Can be null.
	 * @param n
	 *              node at which the meet is being taken.
	 * @return
	 *         meet of the flowFact passed to this method, with the receiver.
	 */
	public Deprecated_FlowFact parallelMeetWith(Deprecated_FlowFact other, Node n) {
		return null;
	}

	/**
	 * Obtain a copy for this flow-fact.
	 * 
	 * @return
	 *         a copy of the this flow-fact.
	 */
	public abstract Deprecated_FlowFact getCopy();
}
