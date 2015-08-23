package com.zygomeme.york.gui;

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
 * Test for the PropertiesMemento    
 * 
 */

public class PropertiesMementoTest extends TestCase {

	public void testRenameTab() {
		
		PropertiesMemento pm = new PropertiesMemento();
		
		pm.addTabRecord("fooAlpha");
		
		assertTrue("Wrong length", pm.getTabNames().size() == 1);
				
		pm.addTabRecord("fooBeta");

		assertTrue("Wrong length", pm.getTabNames().size() == 2);
		
		pm.renameTab("fooAlpha", "foo1");

		assertTrue("Wrong length", pm.getTabNames().size() == 2);
		
		assertTrue("Wrong name in position 0", "foo1".equals(pm.getTabNames().get(0)));
		assertTrue("Wrong name in position 1", "fooBeta".equals(pm.getTabNames().get(1)));

		pm.renameTab("fooBeta", "foo2");

		assertTrue("Wrong name in position 0", "foo1".equals(pm.getTabNames().get(0)));
		assertTrue("Wrong name in position 1", "foo2".equals(pm.getTabNames().get(1)));

	}

}
