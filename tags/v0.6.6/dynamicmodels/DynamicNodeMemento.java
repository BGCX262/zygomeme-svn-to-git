package com.zygomeme.york.dynamicmodels;

import java.util.HashMap;
import java.util.Map;

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
 * Records a snapshot of the node and any given time. 
 *
 */
public class DynamicNodeMemento {

	private double outputValue;
	private Map<String, Double> inputs = new HashMap<String, Double>(); 
	private DynamicModelNode node;
	
	public DynamicNodeMemento(DynamicModelNode clientNode){
		this.node = clientNode;
	}

	public double getOutputValue() {
		return outputValue;
	}

	public void setOutputValue(double outputValue) {
		this.outputValue = outputValue;
	}
	
	public void setInput(DynamicModelNode inputNode){
		inputs.put(inputNode.getIdString(), inputNode.getOutputValue());
	}
	
	public Map<String, Double> getInputs(){
		return inputs;
	}
	
	public DynamicModelNode getNode(){
		return node;
	}
}
