package com.zygomeme.york.chart;

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
 * Tests the functionality of the TickUtil class   
 * 
 */

public class TickUtilTest extends TestCase {

	
	public void testGetAxisTickStepSize(){
		
		TickUtil tickUtil = new TickUtil();
		
		double tickSize = tickUtil.getAxisTickStepSize(2.5641956337196968E-5, true);		
		assertTrue("Test 1: TickSize is wrong, is " + tickSize + " should be " + 5.0E-6, Math.abs(tickSize - 5.0E-6) < 0.00000001);

		tickSize = tickUtil.getAxisTickStepSize(2.5E-10, true);		
		assertTrue("Test 2: TickSize is wrong, is " + tickSize + " should be " + 5.0E-11, Math.abs(tickSize - 5.0E-11) < 0.0000000001);

	}
}
