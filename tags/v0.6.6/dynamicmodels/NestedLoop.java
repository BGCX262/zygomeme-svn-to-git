package com.zygomeme.york.dynamicmodels;

import java.util.Map;

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
 * Part of the nested loop mechanism. This is the nest loop part.
 * 
 */
public class NestedLoop implements NestedComposite{

	private NestedComposite child;
	private LoopConfigurationBean config;
	
	public MultiDimensionalArray<DynamicModelHistory> run(Map<String, IndexValuePair> currentValues){

		MultiDimensionalArray<DynamicModelHistory> histories = new MultiDimensionalArray<DynamicModelHistory>();
		int index = 0;
		for(double d = config.getStart(); d <= config.getStop(); d += config.getStep()){
			currentValues.put(config.getId(), new IndexValuePair(index, d));
			histories.addAll(child.run(currentValues));
			index++;
		}
		return histories;
	}
	
	public void addMethodRunner(NestedComposite newChild){this.child = newChild;}
	
	public void setLoopParams(LoopConfigurationBean initialConfig){
		this.config = initialConfig;
	}
}
