package com.zygomeme.york.dynamicmodels;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zygomeme.york.util.StringUtil;

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
 * Runs any given expression. 
 * Checking the validity of the expression is done elsewhere, so here the expression is assumed to be valid. 
 *
 */
public class ExpressionRunner {

	private static Logger logger = Logger.getLogger(ExpressionRunner.class);
	public final static char[] OPERATORS = new char[]{'*', '+', '-', ')', '(', '/'};
	public final static List<String> FUNCTION_NAMES = Arrays.asList(new String[]{"sin", "cos", "abs", "acos", 
			"asin", "atan", "tan", "cubedroot", "exp", "log", "sign", "sqrt", "todegrees", "toradians"});
	
	public static double runEquation(String equation, Map<String, Double> valuesMap){
		if(equation.indexOf("%") != -1){
			return tryShortCut(equation.toLowerCase(), valuesMap);
		}
		else{
			return runEquation(StringUtil.retainingSplit(equation, OPERATORS), valuesMap);
		}
	}
	
	public static double runEquation(String[] components, Map<String, Double> values){
		
		double currentValue;
		int start = 1;
			
		logger.debug("Components are:" + Arrays.toString(components));
		// Algorithm works on theory of "currentValue operator operand" tuples - 
		// so the method needs to get the "currentValue" first 
		// Work out what the first part of the equation is...
		if(components.length > 0 && "(".equals(components[0])){
			// ..if is a opening bracket then find the matching closing bracket and 
			// then recurse on that equation
			int closingB = getClosingBracket(components, 0);
			// Find the sub expression and recurse on that
			String[] subexpression = Arrays.copyOfRange(components, 1, closingB);
			logger.debug("Recursing at start on:" + StringUtil.arrayToString(subexpression));
			currentValue = runEquation(subexpression, values);
			logger.debug("CurrentValue from return of recursive call:" + currentValue);
			start = closingB + 1;
			// If that's the entire equation, the return
			if(start == components.length){
				return currentValue;
			}
		}
		else{
			// Check to see if it's a number
			if(components.length > 0 && StringUtil.isNumber(components[0])){
				currentValue = Double.parseDouble(components[0]);
			}
			else{
				if(FUNCTION_NAMES.contains(components[0].toLowerCase())){
					// It's a function
					currentValue = evaluateFunction(components[0].toLowerCase(), 0, components, values);
					logger.debug("Set the currentValue to " + currentValue + " from function at the start");
					int closingB = getClosingBracket(components, 1);
					start = closingB + 1;
				}
				else{
					// Must be a variable - get its value from the map
					if("-".equals(components[0])){ //e.g "-b" or "-135"  as in "-b + c"
						start++;
						currentValue = getOperand(components[1], values) * -1.0;
					}
					else{
						// Doesn't have a minus sign prefix
						currentValue = getOperand(components[0], values);
					}
				}
			}
		}
		
		// Algorithm works on theory of "currentValue operator operand"  e.g. "134 + 35" which then become the
		// new current value, and then moves onto the next operator and operand
		logger.debug("Components are...");
		logger.debug("starting at pos:" + start);
//		logger.debug("starting at:" + components[start] + " (pos:" + start + ")");
		String operator;
		double operand = 0.0;
		for(int i = start; i < components.length - 1; i+=2){
			logger.debug("component[i]:\"" + components[i] + "\", ");
			operator = components[i];

			// Get the operand
			if("(".equals(components[i+1])){

				// Find the closing ')'
				int closingB = getClosingBracket(components, i + 1);
				String[] subexpression = Arrays.copyOfRange(components, i + 2, closingB);
				logger.debug("A Recursing on component:" + StringUtil.arrayToString(subexpression));
				operand = runEquation(subexpression, values);
				i = closingB - 1;
			}
			else{
				logger.debug("component:" + components[i+1]);
				if(FUNCTION_NAMES.contains(components[i+1].toLowerCase())){
					// It's a function
					int closingB = getClosingBracket(components, i + 2);
					logger.debug("closingB:" + closingB);
					String[] subexpression = Arrays.copyOfRange(components, i + 1, closingB + 1);
					logger.debug("subexpression:" + StringUtil.arrayToString(subexpression));
					operand = runEquation(subexpression, values);
					i = closingB + 1;
				}
				else{
					// Expecting a Variable e.g. "Price", or a explicit string number e.g. "54.98" but get a 
					// "-" in which case we are looking at a negative number "- Price", "- 54.98". These will
					// have been split out in the tokenising routine, so they need to be recombined. 
					if("-".equals(components[i+1])){
						i++;
						operand = getOperand(components[i + 1], values) * -1.0;
					}
					else{
						// Doesn't have a minus sign prefix
						operand = getOperand(components[i + 1], values);
					}	
				}
			}
			logger.debug("CurrentValue, Operator, Operand:" + currentValue + ", " + operator + ", " + operand);
			if("+".equals(operator)){
				currentValue = currentValue + operand;
			}
			if("-".equals(operator)){
				currentValue = currentValue - operand;
			}
			if("*".equals(operator)){
				currentValue = currentValue * operand;
			}
			if("/".equals(operator)){
				currentValue = currentValue / operand;
			}
			logger.debug("CurrentValue:" + currentValue);
		}
		logger.debug("Current Value:" + currentValue + ", returning from recursive call");
		
		return currentValue;
	}
		
	private static double getOperand(String operandString, Map<String, Double> values){

		// Check to see if it's a number
		if(StringUtil.isNumber(operandString)){
			return Double.parseDouble(operandString);
		}
		else{
			// Otherwise return the double that this string maps to in the values map
			logger.debug("Looking for the value of:>" + operandString + "<");
			logger.debug(operandString + " = " + values.get(operandString));
			return values.get(operandString);
		}							
	}	
	
	private static boolean isValidOperand(String operandString, Map<String, Double> values){

		// Check to see if it's a number
		if(StringUtil.isNumber(operandString)){
			return true;
		}
		else{
			// Otherwise return the double that this string maps to in the values map
			logger.debug("Looking for the value of:>" + operandString + "<");
			logger.debug(operandString + " = " + values.get(operandString));
			return values.containsKey(operandString);
		}							
	}	
	
	private static int getClosingBracket(String[] components, int i){
	
		logger.debug("Looking for closing bracket for:" + StringUtil.arrayToString(components) + " starting at position " + i );
		int openCount = 0;
		while(i < components.length){
			if("(".equals(components[i])){
				openCount++;
			}
			if(")".equals(components[i])){
				openCount--;
			}
			if(openCount == 0){
				logger.debug("Found at " + i);
				return i;
			}
			i++;
		}
		throw new RuntimeException("No closing bracket found: " + StringUtil.arrayToString(components));
	}	
	
	private static double tryShortCut(String equation, Map<String, Double> valuesMap){
		
		// ONLY ONE TYPE OF SHORTCUT JUST NOW "X% of Y" e.g. "5% of 200"
		String firstOperandString = equation.substring(0, equation.indexOf("%")).trim();
		String operator = equation.substring(equation.indexOf("%"), equation.indexOf("of") + 2).trim();

		// Clean the operator
		operator = StringUtil.minimiseSpaces(operator);
		if("%of".equals(operator)){
			operator = "% of";
		}
		String secondOperandString = equation.substring(equation.indexOf("of") + 2).trim();
		
		double firstOperand = getOperand(firstOperandString, valuesMap);
		double secondOperand = getOperand(secondOperandString, valuesMap);
		
		// Check it matches the template
		if(isValidOperand(firstOperandString, valuesMap) && 
		   "% of".equals(operator) &&
		   isValidOperand(secondOperandString, valuesMap)){
			return (secondOperand * firstOperand) / 100.0;
		}
		else{
			// Does not match so run the equation as normal
			return runEquation(StringUtil.retainingSplit(equation, OPERATORS), valuesMap);
		}
	}

	private static double evaluateFunction(String functionName, int i, String[] components, Map<String, Double> values){
		
		int closingB = getClosingBracket(components, i + 1);
		String[] subexpression = Arrays.copyOfRange(components, i + 2, closingB);
		double operand = runEquation(subexpression, values);

		// TODO would like a neater way to find the right function. Could have a factory method
		// that creates a function runner dependent on the function name, but this would require
		// many tiny classes. Don't want to use reflection as I test show that it is around 8 or
		// 9 times slower, even if we just compare Math.sqrt() versus a "method.invoke" call 
		
		if("sqrt".equals(functionName)){
			return Math.sqrt(operand);
		}
		if("sin".equals(functionName)){
			return Math.sin(operand);
		}		
		if("cos".equals(functionName)){
			return Math.cos(operand);
		}		
		if("abs".equals(functionName)){
			return Math.abs(operand);
		}
		if("acos".equals(functionName)){
			return Math.acos(operand);
		}
		if("asin".equals(functionName)){
			return Math.asin(operand);
		}
		if("atan".equals(functionName)){
			return Math.atan(operand);
		}
		if("tan".equals(functionName)){
			return Math.tan(operand);
		}		
		if("cubedroot".equals(functionName)){
			return Math.cbrt(operand);
		}
		if("exp".equals(functionName)){
			return Math.exp(operand);
		}
		if("log".equals(functionName)){
			return Math.log(operand);
		}
		if("sign".equals(functionName)){
			return Math.signum(operand);
		}
		if("todegrees".equals(functionName)){
			return Math.toDegrees(operand);
		}
		if("toradians".equals(functionName)){
			return Math.toRadians(operand);
		}

		// TODO say where the unrecognised bit is and what it is
		throw new RuntimeException("Unrecognised Function: " + StringUtil.arrayToString(components));
	}
}
