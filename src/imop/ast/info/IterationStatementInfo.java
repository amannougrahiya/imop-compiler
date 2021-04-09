/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 *
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.ast.info;

import imop.ast.node.external.*;
import imop.lib.util.Misc;

public class IterationStatementInfo extends StatementInfo {

	public IterationStatementInfo(Node owner) {
		super(owner);
	}

	// public int getCurrentUnrollFactor() {
	// return currentUnrollFactor;
	// }
	//
	// public void incrementUnrollFactor() {
	// currentUnrollFactor++;
	// }
	//
	// public void decremenetUnrollFactor() {
	// currentUnrollFactor--;
	// }
	//
	// public IterationStatement getOriginalLoopBody() {
	// return originalLoopBody;
	// }
	//
	// public void setOriginalLoopBody(IterationStatement originalLoopBody) {
	// this.originalLoopBody = originalLoopBody;
	// }
	//
	public void unrollLoop(int unrollingFactor) {
		Misc.exitDueToLackOfFeature("No implementation found to unroll the loop: " + this.getNode() + " of type "
				+ this.getNode().getClass().getSimpleName());
	}

	public Node getLoopEntryPoint() {
		assert (false)
				: "Attempted invocation of getLoopEntryPoint on a loop that is not a do-while-, while-, or for-loop.";
		return null;
	}

}
