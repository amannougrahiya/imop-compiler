/*
 *   Copyright (c) 2020 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 *   This file is a part of the project IMOP, licensed under the MIT license.
 *   See LICENSE.md for the full text of the license.
 *
 *   The above notice shall be included in all copies or substantial
 *   portions of this file.
 */

package imop.lib.analysis.mhp;

import imop.ast.node.external.*;
import imop.lib.analysis.mhp.incMHP.BeginPhasePoint;
import imop.lib.analysis.mhp.incMHP.PhasePoint;
import imop.parser.Program;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface AbstractPhasePointable {
	Set<AbstractPhasePointable> allBeginPhasePoints = new HashSet<>();

	static Set<AbstractPhasePointable> getAllBeginPhasePoints() {
		if (Program.enableUnmodifiability) {
			return Collections.unmodifiableSet(AbstractPhasePointable.allBeginPhasePoints);
		} else {
			return AbstractPhasePointable.allBeginPhasePoints;
		}
	}

	static void flushALLMHPData() {
		for (AbstractPhasePointable bppAbs : AbstractPhasePointable.allBeginPhasePoints) {
			bppAbs.flushData();
		}
		AbstractPhasePointable.allBeginPhasePoints.clear();
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			BeginPhasePoint.getStaleBeginPhasePoints().clear();
		}
	}

	static void resetStaticFields() {
		AbstractPhasePointable.allBeginPhasePoints.clear();
		if (Program.concurrencyAlgorithm == Program.ConcurrencyAlgorithm.ICON) {
			BeginPhasePoint.getStaleBeginPhasePoints().clear();
		}
	}

	Set<Node> getReachableNodes();

	Set<? extends AbstractPhasePointable> getNextBarriers();

	Set<? extends AbstractPhase<?, ?>> getPhaseSet();

	void flushMHPData(AbstractPhase<?, ?> phase);

	void flushData();

	default Node getNodeFromInterface() {
		if (this instanceof PhasePoint) {
			PhasePoint thisPoint = (PhasePoint) this;
			return thisPoint.getNode();
		} else {
			YPhasePoint thisPoint = (YPhasePoint) this;
			return thisPoint.getNode();
		}
	}

	<T extends AbstractPhase<?, ?>> void removePhase(T phaseToBeRemoved);
}
