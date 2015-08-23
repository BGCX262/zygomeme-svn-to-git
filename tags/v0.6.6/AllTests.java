package com.zygomeme.york;

import com.zygomeme.york.chart.TickUtilTest;
import com.zygomeme.york.dynamicmodelhistory.MultiDimensionalArrayTest;
import com.zygomeme.york.dynamicmodels.DynamicModelTest;
import com.zygomeme.york.dynamicmodels.ExpressionRunnerTest;
import com.zygomeme.york.dynamicmodels.ExpressionValidatorTest;
import com.zygomeme.york.gui.PropertiesMementoTest;
import com.zygomeme.york.gui.model.TableModelTest;
import com.zygomeme.york.util.StringUtilTest;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

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
* AllTests
* Combined test suite for the ZygoMeme York package.
* 
*/

public class AllTests {


	public static Test suite(){

		TestSuite suite = new TestSuite();

		suite.addTestSuite(DynamicModelTest.class);
		suite.addTestSuite(ExpressionRunnerTest.class);
		suite.addTestSuite(ExpressionValidatorTest.class);
		suite.addTestSuite(TableModelTest.class);
		suite.addTestSuite(StringUtilTest.class);
		suite.addTestSuite(TickUtilTest.class);
		suite.addTestSuite(MultiDimensionalArrayTest.class);
		suite.addTestSuite(PropertiesMementoTest.class);

		return suite;
	}

	public static void main(String[] args) {

		TestRunner.run(AllTests.suite());
	}
}
