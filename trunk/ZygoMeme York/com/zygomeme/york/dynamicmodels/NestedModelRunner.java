package com.zygomeme.york.dynamicmodels;

import java.util.HashMap;
import java.util.Map;

import com.zygomeme.york.dynamicmodelhistory.IndexValuePair;
import com.zygomeme.york.dynamicmodelhistory.MultiDimensionalArray;

/**
 * *********************************************************************
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
 * Runs models with nested loops. 
 * 
 */

public class NestedModelRunner {

	
	public MultiDimensionalArray<DynamicModelHistory> runModel(DynamicModel model){
		
		// Set up nested loops
		Map<String, LoopConfigurationBean> loopConfig = model.getLoopConfig();
		NestedComposite parentLoop = null;
		NestedComposite leafLoop = null;
		for(String nodeId: loopConfig.keySet()){
			NestedLoop loop = new NestedLoop();
			loop.setLoopParams(loopConfig.get(nodeId));
			// Add the top level loop
			if(parentLoop == null){
				parentLoop = loop;
			}
			else{
				parentLoop.addMethodRunner(loop);
			}
			leafLoop = loop;
		}
		leafLoop.addMethodRunner(new NestedLeaf(model));
				
		// Run
		return parentLoop.run(new HashMap<String, IndexValuePair>());
	}
}
