package com.zygomeme.york.dynamicmodels;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * **********************************************************************
 *   This file forms part of the ZygoMeme York project - an analysis and
 *   modelling platform.
 *  
 *   Copyright (c) 2009 ZygoMeme Ltd., email: coda@zygomeme.com
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * **********************************************************************
 * 
 * 
 * 
 */
public class ExpressionRunnerTest extends TestCase {

	public void testRunEquationStringArrayMapOfStringDouble() {

		Map<String, Double> valuesMap = new HashMap<String, Double>();
		valuesMap.put("skill", 76.0);
		valuesMap.put("knowledge", 90.0);
		valuesMap.put("self development", 82.0);
		
		String equation = "skill + knowledge + self development";
		
		//EquationRunner runner = new EquationRunner();
		double result = ExpressionRunner.runEquation(equation, valuesMap);
		System.out.println("Addition result:" + result);
		
		assertTrue("Addition failed", result == 248.0);
		
		equation = "knowledge - skill";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Subtraction failed", result == 14.0);

		equation = "knowledge * skill";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Multiplication failed", result == 6840.0);
		
		equation = "(knowledge * skill)";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Simple Brackets failed", result == 6840.0);

		equation = "((knowledge * skill))";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Simple Brackets failed", result == 6840.0);

		equation = "((knowledge + skill))";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Simple Brackets failed", result == 166.0);

		equation = "(knowledge + skill) - self development";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Simple Brackets failed", result == 84.0);

		equation = "(knowledge + skill) - (self development + skill)";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Simple Brackets failed", result == 8.0);

		equation = "((knowledge + skill) - (self development + skill))";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Simple Brackets failed", result == 8.0);

		equation = "(knowledge + skill + self development) - (self development + skill)";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Simple Brackets failed", result == 90.0);

		equation = "(knowledge + (skill - self development)) - (self development + skill)";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Simple Brackets failed", result == -74.0);

		equation = "12 * skill";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Simple Brackets failed", result == 912.0);

		equation = "(90 + (skill - self development)) - (82.0 + skill)";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Simple Brackets failed", result == -74.0);

		equation = "111 + 3";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Explicit numbers addition failed, result:" + result + " should be 114", result == 114);

		equation = "111 / 3";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Explicit numbers division failed, result:" + result + " should be 37", result == 37);

		equation = "(skill + knowledge + self development) / 3";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Explicit numbers division failed, result:" + result + " should be 82.66", Math.abs(result - 82.66666) < 0.001);

		equation = "skill * -10";		
		result = ExpressionRunner.runEquation(equation, valuesMap);
		assertTrue("Simple Multiplication of a negative number failed, result:" + result + " should be -760.0", Math.abs(result + 760.0) < 0.001);

	}

	public void testMoreEquations() {

		Map<String, Double> valuesMap = new HashMap<String, Double>();
		valuesMap.put("a", 2.0);
		valuesMap.put("b", 5.0);
		valuesMap.put("c", 9.0);
		
		String equation = "(a) + (b) + (c)";
		
		double result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("Addition failed", result == 16.0);

		equation = "((a) + (b)) + (c)";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("Addition failed", result == 16.0);

		equation = "(a) + ((b) + (c))";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("Addition failed", result == 16.0);

		equation = "((a) + ((b) + (c)))";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("Addition failed", result == 16.0);
	}
	
	public void testsThree() {

		Map<String, Double> valuesMap = new HashMap<String, Double>();
		valuesMap.put("a", 2.0);
		valuesMap.put("b", 5.0);
		valuesMap.put("c", 9.0);

		String equation = "a + b";
		double result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("Addition failed", result == 7.0);

		equation = "a+b";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("Addition failed", result == 7.0);

		equation = "a";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("Addition failed", result == 2.0);

		equation = "a +b";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("Addition failed", result == 7.0);

		equation = "a+ b";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("Addition failed", result == 7.0);

		equation = "a+ -b";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("Addition failed", result == -3.0);

		equation = "a + -b";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("Addition failed", result == -3.0);

		equation = "a + (-b)";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("Addition failed", result == -3.0);

	}
	
	public void testsFour() {

		Map<String, Double> valuesMap = new HashMap<String, Double>();
		valuesMap.put("pop", 2.0);
		valuesMap.put("b", 3.1);
		valuesMap.put("c", -4.2);

		String equation = "-2.9 * pop";
		double result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test 4.1 failed, expression produced:" + result, result == -5.8);

		equation = "-b * pop";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test 4.2 failed, expression produced:" + result, result == -6.2);

		equation = "c * pop";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test 4.3 failed, expression produced:" + result, result == -8.4);

	}

	public void testsFive() {

		Map<String, Double> valuesMap = new HashMap<String, Double>();
		valuesMap.put("pop", 2.0);
		valuesMap.put("b", 3.1);
		valuesMap.put("c", -4.2);

		String equation = "pop * -2.9";
		double result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test 4.1 failed, expression produced:" + result, result == -5.8);

		equation = "pop * -b";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test 4.2 failed, expression produced:" + result, result == -6.2);

		equation = "pop * c";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test 4.3 failed, expression produced:" + result, result == -8.4);

	}

	public void testSix() {

		Map<String, Double> valuesMap = new HashMap<String, Double>();
		valuesMap.put("one", 1.0);

		String equation = "one + 1.0";
		double result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test 6 failed, expression produced:" + result, result == 2.0);
	}


	public void testSpeed() {
		
		Map<String, Double> valuesMap = new HashMap<String, Double>();
		valuesMap.put("skill", 76.0);
		valuesMap.put("knowledge", 90.0);
		valuesMap.put("self development", 82.0);
		
		String equation = "((knowledge + skill) - (self development + skill))";		
		double result = 0.0;
		long before = System.currentTimeMillis();
		int endTime = 10000;
		for(int i = 0; i < endTime; i++){
			result = ExpressionRunner.runEquation(equation, valuesMap);
		}
		double elapsed = System.currentTimeMillis() - before;
		System.out.println("Elapsed: " + elapsed + "mSec, " + (((double)endTime) / elapsed) * 1000.0 + "loops/Sec");
		assertTrue("Simple Brackets failed", result == 8.0);
	}
	
	
	/**
	 * Test shortcuts
	 */
	public void testShortcutFunction() {

		Map<String, Double> valuesMap = new HashMap<String, Double>();
		valuesMap.put("pop", 200.0);
		valuesMap.put("fair", 30.0);

		String equation = "5% of 200";
		double result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 10.0);

		equation = "5% of  pop";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 10.0);

		equation = "fair% of  pop";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 60.0);
		
		equation = "fair% of  200";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 60.0);

		equation = "5% of  200";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 10.0);

		equation = "5%  of  200";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 10.0);

		equation = "5%    of  200";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 10.0);

		equation = "5%of  200";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 10.0);
	
		equation = "5% of200";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 10.0);

		equation = "5%of200";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 10.0);

		equation = " 5%of200 ";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 10.0);
	
		equation = "5% OF 200";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 10.0);
}
	
	/**
	 * Test functions
	 */
	public void testFunctions() {

		Map<String, Double> valuesMap = new HashMap<String, Double>();
		valuesMap.put("andy", 200.0);

		String equation = "sin(5.0)";
		double result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == -0.9589242746631385);

		equation = "SIN(5.0)";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == -0.9589242746631385);

		equation = "Sin(5.0)";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == -0.9589242746631385);

		equation = "SiN(5.0)";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == -0.9589242746631385);

		equation = "sin (5.0)";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == -0.9589242746631385);
		
		equation = "sin ( 5.0)";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == -0.9589242746631385);
		
		equation = "sin ( 5.0 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == -0.9589242746631385);

		equation = " sin ( 5.0 ) ";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == -0.9589242746631385);

		equation = " sin ( 0.0 ) ";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 0.0);

		equation = " sin ( 1000 ) ";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 0.826879540532002560);

		equation = " sin ( -9 ) ";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == -0.4121184852417566);		

		equation = "cos( 5.0 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 0.283662185463226265);

		equation = "abs( -5.0 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 5.0);

		equation = "abs( 5.0 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 5.0);

		equation = "acos( 1.0 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 0.0);
		
		equation = "acos( 1.0 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 0.0);

		equation = "asin( 0.0 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 0.0);

		equation = "tan( 1.0 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 1.55740772465490223);

		equation = "atan( 0.0 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 0.0);

		equation = "cubedroot( 125 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 5.0);

		equation = "sqrt( 25 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 5.0);

		equation = "sign( -10.0 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == -1.0);

		equation = "sign( 10.0 )";
		result = ExpressionRunner.runEquation(equation, valuesMap);		
		assertTrue("test shortcut function failed, expression produced:" + result, result == 1.0);

	}
}
