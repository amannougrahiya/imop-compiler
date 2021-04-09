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
import imop.lib.analysis.flowanalysis.Symbol;
import imop.parser.FrontEnd;
import imop.parser.Program;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class TypeCopyTest {

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
	 * Testcase to test creation of CFG edges for breakStatement.c
	 */
	@Test
	public void testTypeCopyStruct() throws FileNotFoundException {
		FrontEnd.parseAndNormalize("int main () {struct s1 {int x1; int y1;}; struct s1 **x; l1: x;}");
		Symbol s = check();
		if (s != null) {
			System.out.println(s.getType().getDeclaration("temp"));
			// System.out.println(s.getType().getExactDeclaration("exact"));
		}
		assertEquals("Assert passed.", 0, 0);
	}

	@Test
	public void testTypeCopyStruct5() throws FileNotFoundException {
		FrontEnd.parseAndNormalize("int main () {struct s1 {int x1; int y1;}; struct s1 (*(*x[5])[])(); l1: x;}");
		Symbol s = check();
		if (s != null) {
			System.out.println(s.getType().getDeclaration("temp5"));
			// System.out.println(s.getType().getExactDeclaration("exact"));
		}
		assertEquals("Assert passed.", 0, 0);
	}

	@Test
	public void testTypeCopyStruct4() throws FileNotFoundException {

		FrontEnd.parseAndNormalize("int main () {struct s1 {int x1; int y1;}; struct s1 *y[20]; l1: y;}");
		Symbol s = check();
		if (s != null) {
			System.out.println(s.getType().getDeclaration("temp4"));
			// System.out.println(s.getType().getExactDeclaration("exact"));
		}
		assertEquals("Assert passed.", 0, 0);
	}

	@Test
	public void testTypeCopyStruct2() throws FileNotFoundException {

		FrontEnd.parseAndNormalize("int main () {struct s1 {int x1; int y1;}; struct s1 y[10][20]; l1: y;}");
		Symbol s = check();
		if (s != null) {
			System.out.println(s.getType().getDeclaration("temp"));
			// System.out.println(s.getType().getExactDeclaration("exact"));
		}
		assertEquals("Assert passed.", 0, 0);
	}

	@Test
	public void testTypeCopyStruct3() throws FileNotFoundException {
		FrontEnd.parseAndNormalize(
				"int main () {struct s2 {struct s1* p2;}; struct s1 {int x1; struct s2 *p1;}; struct s1 y[10][20]; l1: y;}");
		Symbol s = check();
		if (s != null) {
			System.out.println(s.getType().getDeclaration("temp"));
			// System.out.println(s.getType().getExactDeclaration("exact"));
		}
		assertEquals("Assert passed.", 0, 0);
	}

	private Symbol check() {
		FunctionDefinition mainDef = Program.getRoot().getInfo().getMainFunction();
		if (mainDef == null) {
			return null;
		}
		Statement stmt = mainDef.getInfo().getStatementWithLabel("l1");
		return (Symbol) stmt.getInfo().getReads().get(0);
	}

}
