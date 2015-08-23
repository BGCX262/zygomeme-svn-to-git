package com.zygomeme.york.dynamicmodels;

import java.util.Map;

import com.zygomeme.york.Node;
import com.zygomeme.york.dynamicmodelhistory.IndexValuePair;
import com.zygomeme.york.dynamicmodelhistory.MultiDimensionalArray;

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
 * Part of the mechanism to run nested loops. This is the leaf class of the 
 * nests.  
 * 
 */
public class NestedLeaf implements NestedComposite{

	private DynamicModel model;
	
	public NestedLeaf(DynamicModel modelIn){
		this.model = modelIn;
	}
	
	public MultiDimensionalArray<DynamicModelHistory> run(Map<String, IndexValuePair> currentValues){

		Map<String, Node> nodeMap = model.getNodeMap();

		// Set the nodes detailed in the loop config to the values for the current
		// loop.
		StringBuffer buffer = new StringBuffer();
		for(String nodeId: currentValues.keySet()){
			((DynamicModelNode)nodeMap.get(nodeId)).setInitialValue(currentValues.get(nodeId).getValue());
			// Construct the key
			buffer.append(currentValues.get(nodeId).getIndex()).append(".");
		}
		
		MultiDimensionalArray<DynamicModelHistory> history = new MultiDimensionalArray<DynamicModelHistory>();
		history.addItem(model.runModel(), buffer.toString());
		return history;
	}
	
	public void addMethodRunner(NestedComposite child){ }
}