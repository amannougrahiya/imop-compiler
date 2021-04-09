/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package test;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.lib.cfg.CFGLinkFinder;
import imop.lib.cfg.link.node.*;
import imop.lib.transform.updater.InsertImmediatePredecessor;
import imop.lib.transform.updater.InsertImmediateSuccessor;
import imop.lib.transform.updater.InsertOnTheEdge;
import imop.lib.transform.updater.sideeffect.SideEffect;
import imop.lib.util.DumpSnapshot;
import imop.lib.util.Misc;
import imop.parser.FrontEnd;
import imop.parser.Program;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HigherLevelCFGTransformationTest {

	public static interface HigherLevel {
	}

	public static interface PredTransform extends HigherLevel {
	}

	public static interface SuccTransform extends HigherLevel {
	}

	public static interface IOTETransform extends HigherLevel {
	}

	public HigherLevelCFGTransformationTest() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testMethod() {
		System.out.println("Hi");
		assertEquals("Assert failed.", 0, 0);
	}

	// @Test
	// public void iis____() throws FileNotFoundException {
	// String[] args = { "-f", "src/imop/lib/testcases/higher/____.c" };
	// Program.parseNormalizeInput(args);
	// List<UpdateSideEffects> sideEffectList = new ArrayList<>();
	// String rootInput = Program.root.toString();
	// for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.root,
	// ForStatement.class)) {
	// for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
	// CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
	// if (link instanceof ____) {
	// InsertImmediateSuccessor.insertAggressive(element,
	// FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
	// }
	// // }
	// // }
	// Misc.dumpRootOrError("outS", sideEffectList);
	// assertEquals("Assert passed.", 0, 0);
	// }
	//
	// @Test
	// public void iip____() throws FileNotFoundException {
	// String[] args = { "-f", "src/imop/lib/testcases/higher/____.c" };
	// Program.parseNormalizeInput(args);
	// List<UpdateSideEffects> sideEffectList = new ArrayList<>();
	// String rootInput = Program.root.toString();
	// for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.root,
	// ForStatement.class)) {
	// for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
	// CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
	// if (link instanceof ____) {
	// InsertImmediatePredecessor.insertAggressive(element,
	// FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
	// }
	// // }
	// // }
	// Misc.dumpRootOrError("outP", sideEffectList);
	// assertEquals("Assert passed.", 0, 0);
	// }
	//
	// @Test
	// public void iote____() throws FileNotFoundException {
	// String[] args = { "-f", "src/imop/lib/testcases/higher/____.c" };
	// Program.parseNormalizeInput(args);
	// List<UpdateSideEffects> sideEffectList = new ArrayList<>();
	// String rootInput = Program.root.toString();
	// for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.root,
	// ForStatement.class)) {
	// for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
	// CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
	// if (link instanceof ____) {
	// if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0)
	// sideEffectList = InsertOnTheEdge.insertAggressive(element,
	// element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
	// FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
	// }
	// // }
	// // }
	// Misc.dumpRootOrError("outIOTE", sideEffectList);
	// assertEquals("Assert passed.", 0, 0);
	// }

	// Testcase to test insertion of an immediate predecessor in AtomicBeginLink
	@Test
	public void iisAtomicBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/AtomicBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (AtomicConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), AtomicConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof AtomicBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in AtomicBeginLink
	@Test
	public void iipAtomicBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/AtomicBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (AtomicConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), AtomicConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof AtomicBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for AtomicBeginLink
	@Test
	public void ioteAtomicBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/AtomicBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (AtomicConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), AtomicConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof AtomicBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in AtomicEndLink
	@Test
	public void iisAtomicEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/AtomicEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (AtomicConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), AtomicConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof AtomicEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in AtomicEndLink
	@Test
	public void iipAtomicEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/AtomicEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (AtomicConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), AtomicConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof AtomicEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for AtomicEndLink
	@Test
	public void ioteAtomicEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/AtomicEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (AtomicConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), AtomicConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof AtomicEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in AtomicStatementLink
	@Test
	public void iisAtomicStatementLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/AtomicStatementLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (AtomicConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), AtomicConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof AtomicStatementLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in AtomicStatementLink
	@Test
	public void iipAtomicStatementLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/AtomicStatementLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (AtomicConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), AtomicConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof AtomicStatementLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for AtomicStatementLink
	@Test
	public void ioteAtomicStatementLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/AtomicStatementLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (AtomicConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), AtomicConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof AtomicStatementLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in CallBeginLink
	@Test
	public void iisCallBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/CallBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CallStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CallBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in CallBeginLink
	@Test
	public void iipCallBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/CallBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CallStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CallBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for CallBeginLink
	@Test
	public void ioteCallBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/CallBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CallStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CallBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in CallEndLink
	@Test
	public void iisCallEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/CallEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CallStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CallEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in CallEndLink
	@Test
	public void iipCallEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/CallEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CallStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CallEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for CallEndLink
	@Test
	public void ioteCallEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/CallEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CallStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CallEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in CallPostLink
	@Test
	public void iisCallPostLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/CallPostLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CallStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CallPostLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in CallPostLink
	@Test
	public void iipCallPostLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/CallPostLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CallStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CallPostLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for CallPostLink
	@Test
	public void ioteCallPostLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/CallPostLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CallStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CallPostLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in CallPreLink
	@Test
	public void iisCallPreLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/CallPreLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CallStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CallPreLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in CallPreLink
	@Test
	public void iipCallPreLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/CallPreLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CallStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CallPreLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for CallPreLink
	@Test
	public void ioteCallPreLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/CallPreLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CallStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CallStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CallPreLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in CompoundBeginLink
	@Test
	public void iisCompoundBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/CompoundBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("int z;", Declaration.class));
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("int p1 = 100 + x;", Declaration.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in CompoundBeginLink
	@Test
	public void iipCompoundBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/CompoundBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("int z;", Declaration.class));
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("int p1 = 100 + x;", Declaration.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for CompoundBeginLink
	@Test
	public void ioteCompoundBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/CompoundBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in CompoundElementLink
	@Test
	public void iisCompoundElementLink1() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/CompoundElementLink1.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundElementLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("int z;", Declaration.class));
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("int p1 = 100 + x;", Declaration.class));
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("continue;", Statement.class));
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("break;", Statement.class));
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("return;", Statement.class));
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("goto l1;", Statement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in CompoundElementLink
	@Test
	public void iipCompoundElementLink1() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/CompoundElementLink1.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundElementLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("int z;", Declaration.class));
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("int p1 = 100 + x;", Declaration.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for CompoundElementLink
	@Test
	public void ioteCompoundElementLink1() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/CompoundElementLink1.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundElementLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("int z;", Declaration.class));
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("int p1 = 100 + x;", Declaration.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in CompoundElementLink
	@Test
	public void iisCompoundElementLink2() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/CompoundElementLink2.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundElementLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("int p1 = 100 + x;", Declaration.class));
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("int z;", Declaration.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in CompoundElementLink
	@Test
	public void iipCompoundElementLink2() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/CompoundElementLink2.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundElementLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("int p1 = 100 + x;", Declaration.class));
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("int z;", Declaration.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for CompoundElementLink
	@Test
	public void ioteCompoundElementLink2() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/CompoundElementLink2.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundElementLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("int p1 = 100 + x;", Declaration.class));
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("int z;", Declaration.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in CompoundElementLink
	@Test
	public void iisCompoundElementLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/CompoundElementLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundElementLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in CompoundElementLink
	@Test
	public void iipCompoundElementLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/CompoundElementLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundElementLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for CompoundElementLink
	@Test
	public void ioteCompoundElementLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/CompoundElementLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundElementLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in CompoundEndLink
	@Test
	public void iisCompoundEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/CompoundEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in CompoundEndLink
	@Test
	public void iipCompoundEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/CompoundEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for CompoundEndLink
	@Test
	public void ioteCompoundEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/CompoundEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CompoundStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CompoundStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CompoundEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in CriticalBeginLink
	@Test
	public void iisCriticalBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/CriticalBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CriticalConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CriticalConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CriticalBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in CriticalBeginLink
	@Test
	public void iipCriticalBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/CriticalBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CriticalConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CriticalConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CriticalBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for CriticalBeginLink
	@Test
	public void ioteCriticalBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/CriticalBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CriticalConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CriticalConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CriticalBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in CriticalBodyLink
	@Test
	public void iisCriticalBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/CriticalBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CriticalConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CriticalConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CriticalBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in CriticalBodyLink
	@Test
	public void iipCriticalBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/CriticalBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CriticalConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CriticalConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CriticalBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for CriticalBodyLink
	@Test
	public void ioteCriticalBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/CriticalBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CriticalConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CriticalConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CriticalBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in CriticalEndLink
	@Test
	public void iisCriticalEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/CriticalEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CriticalConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CriticalConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CriticalEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in CriticalEndLink
	@Test
	public void iipCriticalEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/CriticalEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CriticalConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CriticalConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CriticalEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for CriticalEndLink
	@Test
	public void ioteCriticalEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/CriticalEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (CriticalConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), CriticalConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof CriticalEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in DoBeginLink
	@Test
	public void iisDoBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/DoBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in DoBeginLink
	@Test
	public void iipDoBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/DoBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for DoBeginLink
	@Test
	public void ioteDoBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/DoBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in DoBodyLink
	@Test
	public void iisDoBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/DoBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in DoBodyLink
	@Test
	public void iipDoBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/DoBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for DoBodyLink
	@Test
	public void ioteDoBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/DoBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in DoEndLink
	@Test
	public void iisDoEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/DoEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in DoEndLink
	@Test
	public void iipDoEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/DoEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if(1) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10; break;}", SwitchStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for DoEndLink
	@Test
	public void ioteDoEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/DoEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in DoPredicateLink
	@Test
	public void iisDoPredicateLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/DoPredicateLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoPredicateLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in DoPredicateLink
	@Test
	public void iipDoPredicateLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/DoPredicateLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoPredicateLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoPredicateLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
				}
			}
		}
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoPredicateLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
				}
			}
		}
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoPredicateLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
				}
			}
		}
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoPredicateLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
				}
			}
		}
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoPredicateLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10; break;}", SwitchStatement.class)));
				}
			}
		}
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoPredicateLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoPredicateLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
				}
			}
		}
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoPredicateLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for DoPredicateLink
	@Test
	public void ioteDoPredicateLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/DoPredicateLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (DoStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), DoStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof DoPredicateLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in ForBeginLink
	@Test
	public void iisForBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/ForBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10; break;}", SwitchStatement.class)));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in ForBeginLink
	@Test
	public void iipForBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/ForBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for ForBeginLink
	@Test
	public void ioteForBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/ForBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in ForBodyLink
	@Test
	public void iisForBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/ForBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in ForBodyLink
	@Test
	public void iipForBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/ForBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for ForBodyLink
	@Test
	public void ioteForBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/ForBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in ForEndLink
	@Test
	public void iisForEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/ForEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in ForEndLink
	@Test
	public void iipForEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/ForEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForEndLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", Statement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForEndLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForEndLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForEndLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForEndLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForEndLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10; break;}", SwitchStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForEndLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForEndLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if(1) {continue;}", Statement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 54321;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for ForEndLink
	@Test
	public void ioteForEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/ForEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in ForInitLink
	@Test
	public void iisForInitLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/ForInitLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10; break;}", SwitchStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in ForInitLink
	@Test
	public void iipForInitLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/ForInitLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10; break;}", SwitchStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 54321;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for ForInitLink
	@Test
	public void ioteForInitLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/ForInitLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForInitLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in ForStepLink
	@Test
	public void iisForStepLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/ForStepLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForStepLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in ForStepLink
	@Test
	public void iipForStepLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/ForStepLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForStepLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForStepLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForStepLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForStepLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForStepLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForStepLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10; break;}", SwitchStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForStepLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForStepLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
				}
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForStepLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 54321;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for ForStepLink
	@Test
	public void ioteForStepLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/ForStepLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForStepLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in ForTermLink
	@Test
	public void iisForTermLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/ForTermLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForTermLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in ForTermLink
	@Test
	public void iipForTermLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/ForTermLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			if (ownerNode.getInfo().getCFGInfo().hasTerminationExpression()) {
				sideEffectList.addAll(
						InsertImmediatePredecessor.insert(ownerNode.getInfo().getCFGInfo().getTerminationExpression(),
								FrontEnd.parseAndNormalize("x + 12345;", Statement.class)));
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			if (ownerNode.getInfo().getCFGInfo().hasTerminationExpression()) {
				sideEffectList.addAll(
						InsertImmediatePredecessor.insert(ownerNode.getInfo().getCFGInfo().getTerminationExpression(),
								FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			if (ownerNode.getInfo().getCFGInfo().hasTerminationExpression()) {
				sideEffectList.addAll(
						InsertImmediatePredecessor.insert(ownerNode.getInfo().getCFGInfo().getTerminationExpression(),
								FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			if (ownerNode.getInfo().getCFGInfo().hasTerminationExpression()) {
				sideEffectList.addAll(
						InsertImmediatePredecessor.insert(ownerNode.getInfo().getCFGInfo().getTerminationExpression(),
								FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			if (ownerNode.getInfo().getCFGInfo().hasTerminationExpression()) {
				sideEffectList.addAll(
						InsertImmediatePredecessor.insert(ownerNode.getInfo().getCFGInfo().getTerminationExpression(),
								FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			if (ownerNode.getInfo().getCFGInfo().hasTerminationExpression()) {
				sideEffectList.addAll(
						InsertImmediatePredecessor.insert(ownerNode.getInfo().getCFGInfo().getTerminationExpression(),
								FrontEnd.parseAndNormalize("switch (x) {default: 10; break;}", SwitchStatement.class)));
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			if (ownerNode.getInfo().getCFGInfo().hasTerminationExpression()) {
				sideEffectList.addAll(InsertImmediatePredecessor
						.insert(ownerNode.getInfo().getCFGInfo().getTerminationExpression(), FrontEnd.parseAndNormalize(
								"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			if (ownerNode.getInfo().getCFGInfo().hasTerminationExpression()) {
				sideEffectList.addAll(
						InsertImmediatePredecessor.insert(ownerNode.getInfo().getCFGInfo().getTerminationExpression(),
								FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
			}
		}
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			if (ownerNode.getInfo().getCFGInfo().hasTerminationExpression()) {
				sideEffectList.addAll(
						InsertImmediatePredecessor.insert(ownerNode.getInfo().getCFGInfo().getTerminationExpression(),
								FrontEnd.parseAndNormalize("x + 54321;", ExpressionStatement.class)));
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for ForTermLink
	@Test
	public void ioteForTermLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/ForTermLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ForTermLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in FunctionBeginLink
	@Test
	public void iisFunctionBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/FunctionBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (FunctionDefinition ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof FunctionBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in FunctionBeginLink
	@Test
	public void iipFunctionBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/FunctionBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (FunctionDefinition ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof FunctionBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for FunctionBeginLink
	@Test
	public void ioteFunctionBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/FunctionBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (FunctionDefinition ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof FunctionBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in FunctionBodyLink
	@Test
	public void iisFunctionBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/FunctionBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (FunctionDefinition ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof FunctionBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in FunctionBodyLink
	@Test
	public void iipFunctionBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/FunctionBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (FunctionDefinition ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof FunctionBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for FunctionBodyLink
	@Test
	public void ioteFunctionBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/FunctionBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (FunctionDefinition ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof FunctionBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in FunctionEndLink
	@Test
	public void iisFunctionEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/FunctionEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (FunctionDefinition ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof FunctionEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in FunctionEndLink
	@Test
	public void iipFunctionEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/FunctionEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (FunctionDefinition ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof FunctionEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for FunctionEndLink
	@Test
	public void ioteFunctionEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/FunctionEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (FunctionDefinition ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof FunctionEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in
	// FunctionParameterLink
	@Test
	public void iisFunctionParameterLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/FunctionParameterLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (FunctionDefinition ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof FunctionParameterLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in FunctionParameterLink
	@Test
	public void iipFunctionParameterLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/FunctionParameterLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (FunctionDefinition ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof FunctionParameterLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for FunctionParameterLink
	@Test
	public void ioteFunctionParameterLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/FunctionParameterLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (FunctionDefinition ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), FunctionDefinition.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof FunctionParameterLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in IfBeginLink
	@Test
	public void iisIfBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/IfBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in IfBeginLink
	@Test
	public void iipIfBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/IfBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for IfBeginLink
	@Test
	public void ioteIfBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/IfBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in IfElseBodyLink
	@Test
	public void iisIfElseBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/IfElseBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfElseBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in IfElseBodyLink
	@Test
	public void iipIfElseBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/IfElseBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfElseBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for IfElseBodyLink
	@Test
	public void ioteIfElseBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/IfElseBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfElseBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in IfEndLink
	@Test
	public void iisIfEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/IfEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in IfEndLink
	@Test
	public void iipIfEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/IfEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for IfEndLink
	@Test
	public void ioteIfEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/IfEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in IfPredicateLink
	@Test
	public void iisIfPredicateLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/IfPredicateLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfPredicateLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in IfPredicateLink
	@Test
	public void iipIfPredicateLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/IfPredicateLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfPredicateLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for IfPredicateLink
	@Test
	public void ioteIfPredicateLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/IfPredicateLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfPredicateLink) {
					List<Node> predSuccessors = ((IfPredicateLink) link).childNode.getInfo().getCFGInfo()
							.getSuccBlocks();
					for (Node s : predSuccessors) {
						CFGLink sLink = CFGLinkFinder.getCFGLinkFor(s);
						if (sLink instanceof IfThenBodyLink) {
							sideEffectList = InsertOnTheEdge.insert(element, s,
									FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
						} else if (sLink instanceof IfElseBodyLink) {
							sideEffectList = InsertOnTheEdge.insert(element, s,
									FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
						} else if (sLink instanceof IfEndLink) {
							sideEffectList = InsertOnTheEdge.insert(element, s,
									FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
						}
					}

				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in IfThenBodyLink
	@Test
	public void iisIfThenBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/IfThenBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfThenBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in IfThenBodyLink
	@Test
	public void iipIfThenBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/IfThenBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfThenBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for IfThenBodyLink
	@Test
	public void ioteIfThenBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/IfThenBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (IfStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), IfStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof IfThenBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in MasterBeginLink
	@Test
	public void iisMasterBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/MasterBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof MasterBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in MasterBeginLink
	@Test
	public void iipMasterBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/MasterBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof MasterBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for MasterBeginLink
	@Test
	public void ioteMasterBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/MasterBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (MasterConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), MasterConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof MasterBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in MasterBodyLink
	@Test
	public void iisMasterBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/MasterBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (MasterConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), MasterConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof MasterBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in MasterBodyLink
	@Test
	public void iipMasterBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/MasterBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (MasterConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), MasterConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof MasterBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for MasterBodyLink
	@Test
	public void ioteMasterBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/MasterBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (MasterConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), MasterConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof MasterBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in MasterEndLink
	@Test
	public void iisMasterEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/MasterEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (MasterConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), MasterConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof MasterEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in MasterEndLink
	@Test
	public void iipMasterEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/MasterEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (MasterConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), MasterConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof MasterEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for MasterEndLink
	@Test
	public void ioteMasterEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/MasterEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (MasterConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), MasterConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof MasterEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in OmpForBeginLink
	@Test
	public void iisOmpForBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/OmpForBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in OmpForBeginLink
	@Test
	public void iipOmpForBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/OmpForBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for OmpForBeginLink
	@Test
	public void ioteOmpForBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/OmpForBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in OmpForBodyLink
	@Test
	public void iisOmpForBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/OmpForBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in OmpForBodyLink
	@Test
	public void iipOmpForBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/OmpForBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for OmpForBodyLink
	@Test
	public void ioteOmpForBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/OmpForBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in OmpForEndLink
	@Test
	public void iisOmpForEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/OmpForEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in OmpForEndLink
	@Test
	public void iipOmpForEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/OmpForEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for OmpForEndLink
	@Test
	public void ioteOmpForEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/OmpForEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in OmpForInitLink
	@Test
	public void iisOmpForInitLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/OmpForInitLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForInitLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in OmpForInitLink
	@Test
	public void iipOmpForInitLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/OmpForInitLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForInitLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {continue;}", WhileStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for OmpForInitLink
	@Test
	public void ioteOmpForInitLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/OmpForInitLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForInitLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in OmpForStepLink
	@Test
	public void iisOmpForStepLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/OmpForStepLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForStepLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in OmpForStepLink
	@Test
	public void iipOmpForStepLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/OmpForStepLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForStepLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for OmpForStepLink
	@Test
	public void ioteOmpForStepLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/OmpForStepLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForStepLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in OmpForTermLink
	@Test
	public void iisOmpForTermLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/OmpForTermLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForTermLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in OmpForTermLink
	@Test
	public void iipOmpForTermLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/OmpForTermLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForTermLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for OmpForTermLink
	@Test
	public void ioteOmpForTermLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/OmpForTermLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ForConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ForConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OmpForTermLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in OrderedBeginLink
	@Test
	public void iisOrderedBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/OrderedBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (OrderedConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), OrderedConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OrderedBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in OrderedBeginLink
	@Test
	public void iipOrderedBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/OrderedBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (OrderedConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), OrderedConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OrderedBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for OrderedBeginLink
	@Test
	public void ioteOrderedBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/OrderedBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (OrderedConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), OrderedConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OrderedBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in OrderedBodyLink
	@Test
	public void iisOrderedBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/OrderedBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (OrderedConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), OrderedConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OrderedBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in OrderedBodyLink
	@Test
	public void iipOrderedBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/OrderedBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (OrderedConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), OrderedConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OrderedBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for OrderedBodyLink
	@Test
	public void ioteOrderedBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/OrderedBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (OrderedConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), OrderedConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OrderedBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in OrderedEndLink
	@Test
	public void iisOrderedEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/OrderedEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (OrderedConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), OrderedConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OrderedEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in OrderedEndLink
	@Test
	public void iipOrderedEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/OrderedEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (OrderedConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), OrderedConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OrderedEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for OrderedEndLink
	@Test
	public void ioteOrderedEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/OrderedEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (OrderedConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), OrderedConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof OrderedEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in ParallelBeginLink
	@Test
	public void iisParallelBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/ParallelBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ParallelConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ParallelBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in ParallelBeginLink
	@Test
	public void iipParallelBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/ParallelBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ParallelConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ParallelBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for ParallelBeginLink
	@Test
	public void ioteParallelBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/ParallelBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ParallelConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ParallelBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in ParallelBodyLink
	@Test
	public void iisParallelBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/ParallelBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ParallelConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ParallelBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in ParallelBodyLink
	@Test
	public void iipParallelBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/ParallelBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ParallelConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ParallelBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for ParallelBodyLink
	@Test
	public void ioteParallelBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/ParallelBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ParallelConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ParallelBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in ParallelClauseLink
	@Test
	public void iisParallelClauseLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/ParallelClauseLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ParallelConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ParallelClauseLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in ParallelClauseLink
	@Test
	public void iipParallelClauseLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/ParallelClauseLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ParallelConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ParallelClauseLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for ParallelClauseLink
	@Test
	public void ioteParallelClauseLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/ParallelClauseLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ParallelConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ParallelClauseLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in ParallelEndLink
	@Test
	public void iisParallelEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/ParallelEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ParallelConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ParallelEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in ParallelEndLink
	@Test
	public void iipParallelEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/ParallelEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ParallelConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ParallelEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for ParallelEndLink
	@Test
	public void ioteParallelEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/ParallelEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (ParallelConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), ParallelConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof ParallelEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in SectionsBeginLink
	@Test
	public void iisSectionsBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/SectionsBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SectionsConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SectionsConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SectionsBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in SectionsBeginLink
	@Test
	public void iipSectionsBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/SectionsBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SectionsConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SectionsConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SectionsBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for SectionsBeginLink
	@Test
	public void ioteSectionsBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/SectionsBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SectionsConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SectionsConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SectionsBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in SectionsEndLink
	@Test
	public void iisSectionsEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/SectionsEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SectionsConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SectionsConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SectionsEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in SectionsEndLink
	@Test
	public void iipSectionsEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/SectionsEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SectionsConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SectionsConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SectionsEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for SectionsEndLink
	@Test
	public void ioteSectionsEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/SectionsEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SectionsConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SectionsConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SectionsEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in
	// SectionsSectionBodyLink
	@Test
	public void iisSectionsSectionBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/SectionsSectionBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SectionsConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SectionsConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SectionsSectionBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in
	// SectionsSectionBodyLink
	@Test
	public void iipSectionsSectionBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/SectionsSectionBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SectionsConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SectionsConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SectionsSectionBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for SectionsSectionBodyLink
	@Test
	public void ioteSectionsSectionBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/SectionsSectionBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SectionsConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SectionsConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SectionsSectionBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in SingleBeginLink
	@Test
	public void iisSingleBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/SingleBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SingleConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SingleConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SingleBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in SingleBeginLink
	@Test
	public void iipSingleBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/SingleBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SingleConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SingleConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SingleBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for SingleBeginLink
	@Test
	public void ioteSingleBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/SingleBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SingleConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SingleConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SingleBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in SingleBodyLink
	@Test
	public void iisSingleBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/SingleBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SingleConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SingleConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SingleBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in SingleBodyLink
	@Test
	public void iipSingleBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/SingleBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SingleConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SingleConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SingleBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for SingleBodyLink
	@Test
	public void ioteSingleBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/SingleBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SingleConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SingleConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SingleBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in SingleEndLink
	@Test
	public void iisSingleEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/SingleEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SingleConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SingleConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SingleEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in SingleEndLink
	@Test
	public void iipSingleEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/SingleEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SingleConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SingleConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SingleEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for SingleEndLink
	@Test
	public void ioteSingleEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/SingleEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SingleConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SingleConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SingleEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in SwitchBeginLink
	@Test
	public void iisSwitchBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/SwitchBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBeginLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBeginLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBeginLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBeginLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBeginLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBeginLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10;}", SwitchStatement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBeginLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in SwitchBeginLink
	@Test
	public void iipSwitchBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/SwitchBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for SwitchBeginLink
	@Test
	public void ioteSwitchBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/SwitchBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in SwitchBodyLink
	@Test
	public void iisSwitchBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/SwitchBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in SwitchBodyLink
	@Test
	public void iipSwitchBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/SwitchBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBodyLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBodyLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
				}
			}
		}
		DumpSnapshot.dumpNestedCFG(Program.getRoot(), Program.fileName + "temp");
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBodyLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBodyLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBodyLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBodyLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10;}", SwitchStatement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBodyLink) {
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for SwitchBodyLink
	@Test
	public void ioteSwitchBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/SwitchBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in SwitchEndLink
	@Test
	public void iisSwitchEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/SwitchEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in SwitchEndLink
	@Test
	public void iipSwitchEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/SwitchEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10;}", SwitchStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for SwitchEndLink
	@Test
	public void ioteSwitchEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/SwitchEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in SwitchPredicateLink
	@Test
	public void iisSwitchPredicateLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/SwitchPredicateLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		DumpSnapshot.dumpNestedCFG(Program.getRoot(), "zeroth");
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchPredicateLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchPredicateLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchPredicateLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchPredicateLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpNestedCFG(Program.getRoot(), "first");
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchPredicateLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchPredicateLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10;}", SwitchStatement.class)));
				}
			}
		}
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchPredicateLink) {
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (x) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpNestedCFG(Program.getRoot(), "second");
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in SwitchPredicateLink
	@Test
	public void iipSwitchPredicateLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/SwitchPredicateLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchPredicateLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10;}", SwitchStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for SwitchPredicateLink
	@Test
	public void ioteSwitchPredicateLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/SwitchPredicateLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (SwitchStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), SwitchStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof SwitchPredicateLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in TaskBeginLink
	@Test
	public void iisTaskBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/TaskBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (TaskConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), TaskConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof TaskBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in TaskBeginLink
	@Test
	public void iipTaskBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/TaskBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (TaskConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), TaskConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof TaskBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for TaskBeginLink
	@Test
	public void ioteTaskBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/TaskBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (TaskConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), TaskConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof TaskBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in TaskBodyLink
	@Test
	public void iisTaskBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/TaskBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (TaskConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), TaskConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof TaskBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in TaskBodyLink
	@Test
	public void iipTaskBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/TaskBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (TaskConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), TaskConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof TaskBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for TaskBodyLink
	@Test
	public void ioteTaskBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/TaskBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (TaskConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), TaskConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof TaskBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in TaskClauseLink
	@Test
	public void iisTaskClauseLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/TaskClauseLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (TaskConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), TaskConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof TaskClauseLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in TaskClauseLink
	@Test
	public void iipTaskClauseLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/TaskClauseLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (TaskConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), TaskConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof TaskClauseLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for TaskClauseLink
	@Test
	public void ioteTaskClauseLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/TaskClauseLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (TaskConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), TaskConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof TaskClauseLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in TaskEndLink
	@Test
	public void iisTaskEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/TaskEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (TaskConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), TaskConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof TaskEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in TaskEndLink
	@Test
	public void iipTaskEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/TaskEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (TaskConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), TaskConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof TaskEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for TaskEndLink
	@Test
	public void ioteTaskEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/TaskEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (TaskConstruct ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), TaskConstruct.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof TaskEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in WhileBeginLink
	@Test
	public void iisWhileBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/WhileBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (WhileStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof WhileBeginLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10; break;}", SwitchStatement.class)));
					sideEffectList.addAll(InsertImmediateSuccessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in WhileBeginLink
	@Test
	public void iipWhileBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/WhileBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (WhileStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof WhileBeginLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for WhileBeginLink
	@Test
	public void ioteWhileBeginLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/WhileBeginLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (WhileStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof WhileBeginLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in WhileBodyLink
	@Test
	public void iisWhileBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/WhileBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (WhileStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof WhileBodyLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in WhileBodyLink
	@Test
	public void iipWhileBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/WhileBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (WhileStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof WhileBodyLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for WhileBodyLink
	@Test
	public void ioteWhileBodyLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/WhileBodyLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (WhileStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof WhileBodyLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in WhileEndLink
	@Test
	public void iisWhileEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/WhileEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (WhileStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof WhileEndLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in WhileEndLink
	@Test
	public void iipWhileEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/WhileEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (WhileStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof WhileEndLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10; break;}", SwitchStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for WhileEndLink
	@Test
	public void ioteWhileEndLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/WhileEndLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (WhileStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof WhileEndLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate predecessor in WhilePredicateLink
	@Test
	public void iisWhilePredicateLink() throws FileNotFoundException, InterruptedException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/succ/WhilePredicateLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (WhileStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof WhilePredicateLink) {
					sideEffectList = InsertImmediateSuccessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outS", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion of an immediate successor in WhilePredicateLink
	@Test
	public void iipWhilePredicateLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/pred/WhilePredicateLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (WhileStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof WhilePredicateLink) {
					sideEffectList = InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("case 2: while (x) {continue;}", Statement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("while (x) {break;}", WhileStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {break;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("if (x) {default: 10;}", IfStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element,
							FrontEnd.parseAndNormalize("switch (x) {default: 10; break;}", SwitchStatement.class)));
					sideEffectList.addAll(InsertImmediatePredecessor.insert(element, FrontEnd.parseAndNormalize(
							"{int y; switch (y) {break;} if (1) {break;}}", CompoundStatement.class)));
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outP", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

	// Testcase to test insertion on an edge for WhilePredicateLink
	@Test
	public void ioteWhilePredicateLink() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/higher/iote/WhilePredicateLink.c" };
		Program.parseNormalizeInput(args);
		List<SideEffect> sideEffectList = new ArrayList<>();
		String rootInput = Program.getRoot().toString();
		for (WhileStatement ownerNode : Misc.getInheritedEnclosee(Program.getRoot(), WhileStatement.class)) {
			for (Node element : ownerNode.getInfo().getCFGInfo().getAllComponents()) {
				CFGLink link = CFGLinkFinder.getCFGLinkFor(element);
				if (link instanceof WhilePredicateLink) {
					if (element.getInfo().getCFGInfo().getLeafSuccessors().size() != 0) {
						sideEffectList = InsertOnTheEdge.insert(element,
								element.getInfo().getCFGInfo().getLeafSuccessors().get(0),
								FrontEnd.parseAndNormalize("x + 12345;", ExpressionStatement.class));
					}
				}
			}
		}
		DumpSnapshot.dumpRootOrError("outIOTE", sideEffectList);
		assertEquals("Assert passed.", 0, 0);
	}

}
