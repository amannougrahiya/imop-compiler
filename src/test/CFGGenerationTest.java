/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package test;

import imop.parser.Program;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class CFGGenerationTest {

	@BeforeClass
	public static void initSetup() {
		Program.isPrePassPhase = true;
		Program.proceedBeyond = true;
	}

	@AfterClass
	public static void exitSetdown() {
		Program.isPrePassPhase = false;
		Program.proceedBeyond = false;
	}

	/**
	 * Testcase to test creation of CFG edges for returnStatement.c
	 */
	@Test
	public void testreturnStatementCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/returnStatement.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for breakStatement.c
	 */
	@Test
	public void testbreakStatementCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/breakStatement.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for ifStatement.c
	 */
	@Test
	public void testifStatementCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/ifStatement.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for criticalConstruct.c
	 */
	@Test
	public void testcriticalConstructCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/criticalConstruct.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for functionCalls.c
	 */
	@Test
	public void testfunctionCallsCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/functionCalls.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for doStatement.c
	 */
	@Test
	public void testdoStatementCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/doStatement.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for switchStatement.c
	 */
	@Test
	public void testswitchStatementCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/switchStatement.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for compoundStatement.c
	 */
	@Test
	public void testcompoundStatementCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/compoundStatement.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for masterConstruct.c
	 */
	@Test
	public void testmasterConstructCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/masterConstruct.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for sectionsConstruct.c
	 */
	@Test
	public void testsectionsConstructCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/sectionsConstruct.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for allCFG.i
	 */
	@Test
	public void testallCFGCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/allCFG.i" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for orderedConstruct.c
	 */
	@Test
	public void testorderedConstructCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/orderedConstruct.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for atomicConstruct.c
	 */
	@Test
	public void testatomicConstructCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/atomicConstruct.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for parallelConstruct.c
	 */
	@Test
	public void testparallelConstructCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/parallelConstruct.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for small1.c
	 */
	@Test
	public void testsmall1CFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/small1.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for whileStatement.c
	 */
	@Test
	public void testwhileStatementCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/whileStatement.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for definitions.c
	 */
	@Test
	public void testdefinitionsCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/definitions.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for forConstruct.c
	 */
	@Test
	public void testforConstructCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/forConstruct.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for functionDef.c
	 */
	@Test
	public void testfunctionDefCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/functionDef.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for parallelForConstruct.c
	 */
	@Test
	public void testparallelForConstructCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/parallelForConstruct.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for proc.c
	 */
	@Test
	public void testprocCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/proc.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for
	 * flushBarrierTaskWaitTaskYield.c
	 */
	@Test
	public void testflushBarrierTaskWaitTaskYieldCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/flushBarrierTaskWaitTaskYield.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for livenessCheck.c
	 */
	@Test
	public void testlivenessCheckCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/livenessCheck.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for gotoStatement.c
	 */
	@Test
	public void testgotoStatementCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/gotoStatement.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for parallelSectionsConstruct.c
	 */
	@Test
	public void testparallelSectionsConstructCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/parallelSectionsConstruct.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for singleConstruct.c
	 */
	@Test
	public void testsingleConstructCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/singleConstruct.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for declaration.c
	 */
	@Test
	public void testdeclarationCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/declaration.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for taskConstruct.c
	 */
	@Test
	public void testtaskConstructCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/taskConstruct.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for forStatement.c
	 */
	@Test
	public void testforStatementCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/forStatement.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for simpleFlush.c
	 */
	@Test
	public void testsimpleFlushCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/simpleFlush.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for continueStatement.c
	 */
	@Test
	public void testcontinueStatementCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/continueStatement.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

	/**
	 * Testcase to test creation of CFG edges for atomicConstructSimple.c
	 */
	@Test
	public void testatomicConstructSimpleCFG() throws FileNotFoundException {
		String[] args = { "-f", "src/imop/lib/testcases/cfgTests/atomicConstructSimple.c" };
		Program.parseNormalizeInput(args);
		assertEquals("Assert passed.", 0, 0);
	}

}
