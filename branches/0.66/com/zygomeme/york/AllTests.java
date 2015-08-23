package com.zygomeme.york;

import com.zygomeme.york.chart.TickUtilTest;
import com.zygomeme.york.gui.PropertiesMementoTest;
import com.zygomeme.york.gui.model.TableModelTest;
import com.zygomeme.york.kb.ConnectionTest;
import com.zygomeme.york.parser.ParseRuleTest;
import com.zygomeme.york.parser.ParserTest;
import com.zygomeme.york.tokeniser.TokeniserTest;
import com.zygomeme.york.util.LCSTest;
import com.zygomeme.york.util.StringUtilTest;
import com.zygomeme.york.xml.MiModelHandlerTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**  
* **********************************************************************
*   This file forms part of the Zygomeme York project - an analysis and
*   modelling platform.
*  
*   Copyright (C) 2008-2010 Zygomeme Ltd., email: coda@zygomeme.com
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
* AllTests
* Combined test suite for the Zygomeme York package.
* 
*/

public class AllTests {


	public static Test suite(){

		TestSuite suite = new TestSuite();

		suite.addTestSuite(TableModelTest.class);
		suite.addTestSuite(StringUtilTest.class);
		suite.addTestSuite(TickUtilTest.class);
		suite.addTestSuite(PropertiesMementoTest.class);
		suite.addTestSuite(ConnectionTest.class);
		suite.addTestSuite(ParserTest.class);
		suite.addTestSuite(ParseRuleTest.class);
		suite.addTestSuite(TokeniserTest.class);
		suite.addTestSuite(LCSTest.class);
		suite.addTestSuite(StringUtilTest.class);
		suite.addTestSuite(MiModelHandlerTestCase.class);	

		return suite;
	}

	public static void main(String[] args) {

		TestRunner.run(AllTests.suite());
	}
}
