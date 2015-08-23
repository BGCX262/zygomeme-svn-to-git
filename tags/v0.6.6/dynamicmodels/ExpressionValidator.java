package com.zygomeme.york.dynamicmodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.zygomeme.york.Node;
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
 * Given an expression this class will check it is valid, 
 * and give feedback describing any fault(s) found. 
 *
 */
public class ExpressionValidator {

	public List<ErrorItem> validateEquation(String equation, Set<String> valueIds, Node node){
		return validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valueIds, node);
	}
	
	public List<ErrorItem> validateEquation(String[] components, Set<String> values, Node node){
		
		List<ErrorItem> report = new ArrayList<ErrorItem>();
		
		// Check that the all the named variables are available in the values map
		int characterPosition = 0;
		for(String component: components){
			
			// Single character operator
			if(component.length() == 1 && 
			StringUtil.isSeparator(((char) component.charAt(0)), ExpressionRunner.OPERATORS)){
				 //System.out.println(component + " is an operator");
			}
			else{
				// Otherwise...
				if(!StringUtil.isNumber(component)){
					// ..it's not a number..
					if(!values.contains(component) && !ExpressionRunner.FUNCTION_NAMES.contains(component.toLowerCase())){
						// ...and it's not a variable
						// TODO See if any of the ids are close or differ only in case choice, then  
						// suggest the correct spelling - auto correct would be nice. 
						// TODO Also for consideration is real time highlighting at the input stage.
						report.add(new ExpressionErrorItem(characterPosition, 
								                         "\"" + component + "\" is not a recognised entity", 
								                         ModelErrorReport.ErrorType.INVALID_VARIABLE, 
								                         component, node.getIdString()));
					}
				}
			}
			characterPosition++;
		}
		
		// Test Brackets match
		characterPosition = 0;
		Stack<String> stack = new Stack<String>();
		for(String component: components){
		
			if("(".equals(component)){
				stack.push(component);
			}
			if(")".equals(component)){
				if(!stack.isEmpty() && stack.peek().equals("(")){
					stack.pop();
				}
				else{
					if(stack.isEmpty()){
						report.add(new ExpressionErrorItem(characterPosition, 
								"Unexpected closing bracket", 
								ModelErrorReport.ErrorType.UNEXPECTED_CLOSING_BRACKET, 
						")", node.getIdString()));
					}
					else{
						report.add(new ExpressionErrorItem(characterPosition, 
								"Bracket mismatch", 
								ModelErrorReport.ErrorType.BRACKET_MISMATCH, 
						"", node.getIdString()));
					}
				}
			}								
			characterPosition++;			
		}
		
		if(stack.size() > 0){
			report.add(new ExpressionErrorItem(characterPosition, 
					"Missing closing bracket", 
					ModelErrorReport.ErrorType.MISSING_CLOSING_BRACKET, 
			"(", node.getIdString()));			
		}
		
		
		// Disallow "(+..." sequences. Note "(-.." is allowed 
		characterPosition = 0;
		for(int i = 0; i < components.length - 1; i++){
		
			if("(".equals(components[i]) && 
					(("+".equals(components[i + 1])) ||
					 ("/".equals(components[i + 1])) ||
					 ("*".equals(components[i + 1]))
					 )){
				report.add(new ExpressionErrorItem(characterPosition, 
						"Unexpected operator \"" + components[i + 1] + "\"", 
						ModelErrorReport.ErrorType.UNEXPECTED_OPERATOR, components[i + 1], node.getIdString()));
			}
			characterPosition++;
		}
		
		return report;
	}
}
