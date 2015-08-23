package com.zygomeme.york.dynamicmodelhistory;

import com.zygomeme.york.dynamicmodels.DynamicModelHistory;

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
 * Tests for the multi-dimensional array that is (will be) used in the nested 
 * loops feature of the application.  
 * 
 */
public class MultiDimensionalArrayTest extends TestCase {

	public void testOne(){

		MultiDimensionalArray<DynamicModelHistory> mda = new MultiDimensionalArray<DynamicModelHistory>(new int[]{1,2,3,4,9});

		DynamicModelHistory history1 = new DynamicModelHistory();
		history1.setIdString("harry");
		mda.addItem(history1, 1,2,3,4,9);

		assertTrue("Couldn't find the stored item", "harry".equals(mda.getItem(new int[]{1,2,3,4,9}).getIdString()));
		assertTrue("Couldn't find the stored item", "harry".equals(mda.getItem(1,2,3,4,9).getIdString()));

		// Hardly an exhaustive test!? but hey
		assertTrue("Hashcodes are not distinct", mda.hashCodeKeys(1,2) != mda.hashCodeKeys(2,1));
		
		DynamicModelHistory history2 = new DynamicModelHistory();
		history2.setIdString("tom");
		mda.addItem(history2, 0,1,2,3,3);
		assertTrue("Couldn't find the stored item - tom", "tom".equals(mda.getItem(0,1,2,3,3).getIdString()));
		assertTrue("Couldn't find the stored item - harry", "harry".equals(mda.getItem(1,2,3,4,9).getIdString()));

		int foo = 3;
		assertTrue("Couldn't find the stored item with foo - tom", "tom".equals(mda.getItem(0,1,2,foo,3).getIdString()));
		
		for(int i = 0; i < mda.getDimensions()[3]; i++){
			if(mda.getItem(0,1,2,i,3) == null){
				assertTrue("Found an item when I should have found null", mda.getItem(0,1,2,i,3) == null);
			}
			else{
				"tom".equals(mda.getItem(0,1,2,i,3).getIdString());
			}
		}
	}
}
