package com.zygomeme.york.gui.model;

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
 * Tests for the TableModel
 * 
 * 
 */


public class TableModelTest extends TestCase {

	public void testInitalModel(){
		
		TableModel tm = new TableModel();

		assertTrue("Wrong column count for empty table", tm.getColumnCount() == 0);
		assertTrue("Wrong row count for empty table", tm.getRowCount() == 0);

		tm.addColumn("c1");
		assertTrue("Wrong column count for one column table", tm.getColumnCount() == 1);
		assertTrue("Wrong row count for empty table", tm.getRowCount() == 0);

		tm.addColumn("c2");
		assertTrue("Wrong column count for one column table", tm.getColumnCount() == 2);
		assertTrue("Wrong row count for empty table", tm.getRowCount() == 0);

		tm.addRow("r1");
		assertTrue("Wrong column count for one column table", tm.getColumnCount() == 2);
		assertTrue("Wrong row count for empty table", tm.getRowCount() == 1);
		tm.addRow("r2");
		tm.addRow("r3");
		
		assertTrue("Wrong column count", tm.getColumnCount() == 2);
		assertTrue("Wrong row count", tm.getRowCount() == 3);
		
		
		// Test capacity numbers
		assertTrue("Default row capacity is wrong", tm.getRowCapacity() == TableModel.DEFAULT_ROW_CAPACITY);
		assertTrue("Default Column capacity is wrong", tm.getColumnCapacity() == TableModel.DEFAULT_COLUMN_CAPACITY);
		
		
		tm.setValue(0, 0, 32);
		assertTrue("Recovered value is incorrect, recovered value:" + tm.getValueAsString(0, 0), 
				"32.0".equals(tm.getValueAsString(0, 0)));
		
		// Add some new named rows
		for(int r = 4; r < 23; r++){
			tm.addRow("r" + r);
		}
		
		assertTrue("Row capacity is wrong, capacity now:" + tm.getRowCapacity(), tm.getRowCapacity() == 30);
		assertTrue("Column capacity is wrong, capacity now:" + tm.getColumnCapacity(), tm.getColumnCapacity() == TableModel.DEFAULT_COLUMN_CAPACITY);

		// Add some new named columns
		for(int c = 3; c < 23; c++){
			tm.addColumn("c" + c);
		}

		assertTrue("Row capacity is wrong, capacity now:" + tm.getRowCapacity(), tm.getRowCapacity() == 30);
		assertTrue("Column capacity is wrong, capacity now:" + tm.getColumnCapacity(), tm.getColumnCapacity() == 30);

		tm.setValue(32, 32, 64);
		assertTrue("Row capacity is wrong, capacity now:" + tm.getRowCapacity(), tm.getRowCapacity() == 42);
		assertTrue("Column capacity is wrong, capacity now:" + tm.getColumnCapacity(), tm.getColumnCapacity() == 42);
		
		assertTrue("Recovered value is incorrect, recovered value:" + tm.getValueAsString(32, 32), 
				"64.0".equals(tm.getValueAsString(32, 32)));
				
	}
	
	public void testTableModelWithNames(){
		
		TableModel tm = new TableModel();

		tm.setValue(4, 4, 16);
		assertTrue("Recovered value is incorrect, recovered value:" + tm.getValueAsString(4, 4), 
				"16.0".equals(tm.getValueAsString(4, 4)));		
	}
}
