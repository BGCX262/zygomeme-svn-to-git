package com.zygomeme.york.dynamicmodels;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zygomeme.york.util.StringUtil;

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
 */

public class ExpressionValidatorTest extends TestCase {

	
	public void testValidateEquation(){
		
		Set<String> valuesMap = new HashSet<String>();
		valuesMap.add("skill");
		valuesMap.add("knowledge");
		valuesMap.add("self development");
		
		String equation = "skill + knowledge + self development";
		
		ExpressionValidator ev = new ExpressionValidator();
		
		DynamicModelNode node = new DynamicModelNode("Nody the node");
		List<ErrorItem> report = ev.validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valuesMap, node);
		
		assertTrue("Spurious error found", report.size() == 0);
		
		equation = "skill + knowledge + slef development"; // "self" is misspelt
		
		report = ev.validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valuesMap, node);

		ExpressionErrorItem expressionError = (ExpressionErrorItem) report.get(0);
		assertTrue("Error missed", report.size() == 1);	
		assertTrue("Error type is incorrect", expressionError.getType() == ModelErrorReport.ErrorType.INVALID_VARIABLE);
		assertTrue("Position is incorrect, is " + expressionError.getPosition() + " should be 4", expressionError.getPosition() == 4);
		assertTrue("Bad Item is incorrect, is \"" + expressionError.getBadItem() + "\" should be \"slef development\"", "slef development".equals(expressionError.getBadItem()));
		
	}
	
	public void testBrackets(){
		
		Set<String> valuesMap = new HashSet<String>();
		valuesMap.add("skill");
		valuesMap.add("knowledge");
		valuesMap.add("self development");
		
		String equation = "(skill + knowledge) + self development";
		
		ExpressionValidator ev = new ExpressionValidator();
		
		DynamicModelNode node = new DynamicModelNode("Nody the node");
		List<ErrorItem> report = ev.validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valuesMap, node);
		
		assertTrue("Spurious error found", report.size() == 0);
		
		equation = "(skill + knowledge)) + self development"; 
		
		report = ev.validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valuesMap, node);
		
		ExpressionErrorItem expressionError = (ExpressionErrorItem) report.get(0);
		assertTrue("Error missed", report.size() == 1);	
		assertTrue("Error type is incorrect, is " + expressionError.getType(), expressionError.getType() == ModelErrorReport.ErrorType.UNEXPECTED_CLOSING_BRACKET);
		assertTrue("Position is incorrect, is " + expressionError.getPosition() + " should be 5", expressionError.getPosition() == 5);
		assertTrue("Bad Item is incorrect, is \"" + expressionError.getBadItem() + "\" should be \")\"", ")".equals(expressionError.getBadItem()));		
		
		equation = "(skill + knowledge)( self development"; 
		report = ev.validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valuesMap, node);
		
		assertTrue("Error missed", report.size() == 1);	
		if(report.size() > 0){
			expressionError = (ExpressionErrorItem) report.get(0);
			assertTrue("Error type is incorrect, is " + expressionError.getType(), expressionError.getType() == ModelErrorReport.ErrorType.MISSING_CLOSING_BRACKET);
			assertTrue("Position is incorrect, is " + expressionError.getPosition() + " should be 7", expressionError.getPosition() == 7);
			assertTrue("Bad Item is incorrect, is \"" + expressionError.getBadItem() + "\" should be \"(\"", "(".equals(expressionError.getBadItem()));
		}

		equation = "(skill + knowledge) + self development("; 
		report = ev.validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valuesMap, node);

		assertTrue("Error missed", report.size() == 1);	
		if(report.size() > 0){
			expressionError = (ExpressionErrorItem) report.get(0);
			assertTrue("Error type is incorrect, is " + expressionError.getType(), expressionError.getType() == ModelErrorReport.ErrorType.MISSING_CLOSING_BRACKET);
			assertTrue("Position is incorrect, is " + expressionError.getPosition() + " should be 8", expressionError.getPosition() == 8);
			assertTrue("Bad Item is incorrect, is \"" + expressionError.getBadItem() + "\" should be \"(\"", "(".equals(expressionError.getBadItem()));
		}
		
		equation = "(skill + knowledge) + self development)"; 
		report = ev.validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valuesMap, node);
		
		assertTrue("Error missed", report.size() == 1);	
		if(report.size() > 0){
			expressionError = (ExpressionErrorItem) report.get(0);		
			assertTrue("Error type is incorrect, is " + expressionError.getType(), expressionError.getType() == ModelErrorReport.ErrorType.UNEXPECTED_CLOSING_BRACKET);
			assertTrue("Position is incorrect, is " + expressionError.getPosition() + " should be 7", expressionError.getPosition() == 7);
			assertTrue("Bad Item is incorrect, is \"" + expressionError.getBadItem() + "\" should be \")\"", ")".equals(expressionError.getBadItem()));
		}

		equation = ")(skill + knowledge) + self development"; 
		report = ev.validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valuesMap, node);
		
		assertTrue("Error missed", report.size() == 1);	
		if(report.size() > 0){
			expressionError = (ExpressionErrorItem) report.get(0);

			assertTrue("Error type is incorrect, is " + expressionError.getType(), expressionError.getType() == ModelErrorReport.ErrorType.UNEXPECTED_CLOSING_BRACKET);
			assertTrue("Position is incorrect, is " + expressionError.getPosition() + " should be 0", expressionError.getPosition() == 0);
			assertTrue("Bad Item is incorrect, is \"" + expressionError.getBadItem() + "\" should be \")\"", ")".equals(expressionError.getBadItem()));
		}
		
		equation = ")("; 
		report = ev.validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valuesMap, node);
		
		if(report.size() > 0){
			expressionError = (ExpressionErrorItem) report.get(0);

			assertTrue("Error missed, report size:" + report.size(), report.size() == 2);	
			assertTrue("Error type is incorrect, is " + expressionError.getType(), expressionError.getType() == ModelErrorReport.ErrorType.UNEXPECTED_CLOSING_BRACKET);
			assertTrue("Position is incorrect, is " + expressionError.getPosition() + " should be 0", expressionError.getPosition() == 0);
			assertTrue("Bad Item is incorrect, is \"" + expressionError.getBadItem() + "\" should be \")\"", ")".equals(expressionError.getBadItem()));
		}

		equation = "(skill (+ knowledge))"; 
		report = ev.validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valuesMap, node);

		if(report.size() > 0){
			expressionError = (ExpressionErrorItem) report.get(0);

			assertTrue("Error missed, report size:" + report.size(), report.size() == 1);	
			assertTrue("Error type is incorrect, is " + expressionError.getType(), expressionError.getType() == ModelErrorReport.ErrorType.UNEXPECTED_OPERATOR);
			assertTrue("Position is incorrect, is " + expressionError.getPosition() + " should be 2", expressionError.getPosition() == 2);
			assertTrue("Bad Item is incorrect, is \"" + expressionError.getBadItem() + "\" should be \"+\"", "+".equals(expressionError.getBadItem()));
		}

		equation = "(skill (/ knowledge))"; 
		report = ev.validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valuesMap, node);
		
		if(report.size() > 0){
			expressionError = (ExpressionErrorItem) report.get(0);

			assertTrue("Error missed, report size:" + report.size(), report.size() == 1);	
			assertTrue("Error type is incorrect, is " + expressionError.getType(), expressionError.getType() == ModelErrorReport.ErrorType.UNEXPECTED_OPERATOR);
			assertTrue("Position is incorrect, is " + expressionError.getPosition() + " should be 2", expressionError.getPosition() == 2);
			assertTrue("Bad Item is incorrect, is \"" + expressionError.getBadItem() + "\" should be \"/\"", "/".equals(expressionError.getBadItem()));
		}

		equation = "(skill (* knowledge))"; 
		report = ev.validateEquation(StringUtil.retainingSplit(equation, ExpressionRunner.OPERATORS), valuesMap, node);
		
		if(report.size() > 0){
			expressionError = (ExpressionErrorItem) report.get(0);

			assertTrue("Error missed, report size:" + report.size(), report.size() == 1);	
			assertTrue("Error type is incorrect, is " + expressionError.getType(), expressionError.getType() == ModelErrorReport.ErrorType.UNEXPECTED_OPERATOR);
			assertTrue("Position is incorrect, is " + expressionError.getPosition() + " should be 2", expressionError.getPosition() == 2);
			assertTrue("Bad Item is incorrect, is \"" + expressionError.getBadItem() + "\" should be \"*\"", "*".equals(expressionError.getBadItem()));
		}
	}

}
