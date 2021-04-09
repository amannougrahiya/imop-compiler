/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.util;

import com.microsoft.z3.*;

import java.util.HashMap;

public class Z3Solver {

	public static void test() {
		HashMap<String, String> cfg = new HashMap<>();
		cfg.put("model", "true");
		Context c = new Context(cfg);
		Solver solver = c.mkSimpleSolver();
		solver.push();
		IntExpr a = c.mkIntConst("a");

		Status st = solver.check();
		System.out.println(st);
		if (st != Status.SATISFIABLE) {
			c.close();
			return;
		}
		Model m = solver.getModel();
		System.out.println(m.getConstInterp(a));
		c.close();
	}

	public static void test3() {
		HashMap<String, String> cfg = new HashMap<>();
		cfg.put("model_validate", "true");
		Context c = new Context(cfg);
		Solver solver = c.mkSimpleSolver();
		IntExpr x = c.mkIntConst("x");
		IntExpr y = c.mkIntConst("y");
		solver.push();

		solver.add(c.mkEq(x, c.mkITE(c.mkEq(c.mkInt(1), c.mkInt(1)), c.mkInt(0), c.mkMod(c.mkInt(5), c.mkInt(2)))));
		// solver.add(c.mkOr(c.mkEq(x, c.mkInt(3)), c.mkEq(x, c.mkInt(2))));
		// solver.add(c.mkEq(c.mkSub(y, c.mkInt(7)), c.mkInt(0)));
		// solver.add(c.mkEq(c.mkAdd(x, y), c.mkInt(10)));

		Status st = solver.check();
		System.out.println(st);
		if (st != Status.SATISFIABLE) {
			c.close();
			return;
		}
		Model m = solver.getModel();
		System.out.println(m.getConstInterp(x));
		// System.out.println(m.getConstInterp(y));
		c.close();
	}

	public static void test2() {
		long timer = System.nanoTime();
		HashMap<String, String> cfg = new HashMap<>();
		cfg.put("model", "true");
		Context c = new Context(cfg);
		Solver solver = c.mkSimpleSolver();
		solver.push();
		IntExpr nT = c.mkIntConst("nT");
		IntExpr tid1 = c.mkIntConst("tid1");
		IntExpr tid2 = c.mkIntConst("tid2");
		IntExpr i1 = c.mkIntConst("i1");
		IntExpr j1 = c.mkIntConst("j1");
		IntExpr i2 = c.mkIntConst("i2");
		IntExpr j2 = c.mkIntConst("j2");
		BoolExpr unknown = c.mkBoolConst("unknown");
		solver.add(c.mkEq(unknown, c.mkBool(true)));
		// solver.add(c.mkEq(nT, c.mkInt(16)));
		solver.add(c.mkGe(tid1, c.mkInt(0)));
		solver.add(c.mkLt(tid1, nT));
		solver.add(c.mkGe(tid2, c.mkInt(0)));
		solver.add(c.mkLt(tid2, nT));
		// solver.add(c.mkNot(c.mkEq(tid1, tid2)));
		ArithExpr itNum1 = c.mkSub(c.mkSub(c.mkInt(500), c.mkInt(1)), c.mkInt(1));
		ArithExpr itNum2 = c.mkSub(c.mkSub(c.mkInt(500), c.mkInt(1)), c.mkInt(1));
		ArithExpr itPerT1 = c.mkDiv(itNum1, nT);
		ArithExpr itPerT2 = c.mkDiv(itNum2, nT);
		ArithExpr startingOfT1 = c.mkAdd(c.mkInt(1), c.mkMul(tid1, itPerT1));
		ArithExpr startingOfT2 = c.mkAdd(c.mkInt(1), c.mkMul(tid2, itPerT2));

		solver.add(c.mkGe(i1, startingOfT1));
		solver.add(c.mkLt(i1, c.mkAdd(startingOfT1, itPerT1)));
		solver.add(c.mkGe(i2, startingOfT2));
		solver.add(c.mkLt(i2, c.mkAdd(startingOfT2, itPerT2)));
		solver.add(c.mkGe(j1, c.mkInt(1)));
		solver.add(c.mkLt(j1, c.mkSub(c.mkInt(500), c.mkInt(1))));
		solver.add(c.mkGe(j2, c.mkInt(1)));
		solver.add(c.mkLt(j2, c.mkSub(c.mkInt(500), c.mkInt(1))));
		solver.add(c.mkEq(c.mkMul(c.mkInt(2), i1), c.mkMul(c.mkInt(3), i2)));
		solver.add(c.mkEq(j1, j2));

		Status st = solver.check();
		System.out.println(st);
		if (st != Status.SATISFIABLE) {
			c.close();
			return;
		}
		Model m = solver.getModel();
		System.out.println("Time taken: " + (System.nanoTime() - timer) / (1e9 * 1.0) + "s.");
		System.out.println(m.getConstInterp(nT));
		System.out.println(m.getConstInterp(tid1));
		System.out.println(m.getConstInterp(tid2));
		System.out.println(m.getConstInterp(unknown));
		System.out.println(m.getConstInterp(i1));
		System.out.println(m.getConstInterp(j1));
		System.out.println(m.getConstInterp(i2));
		System.out.println(m.getConstInterp(j2));
		c.close();
	}

}
