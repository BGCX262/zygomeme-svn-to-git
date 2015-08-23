package com.zygomeme.york.dynamicmodelhistory;

import java.util.Collection;
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
 * 
 *
 * Class for the CRUD of N-Dimensional array, were N is declared at run
 * time. Within the class the "array" is stored in a Map, that is keyed
 * on an array of integers passed to the class in varargs (or int[])
 * 
 */
public class MultiDimensionalArray<T> {
	
	private Map<String, T> array = new HashMap<String, T>();
	private int[] dimensionSpec;
			
	public MultiDimensionalArray(){
	}

	public MultiDimensionalArray(int[] initialDimensions){
		this.dimensionSpec = initialDimensions;
	}
	
	public void addItem(T newItem, int... keys){
		array.put(hashCodeKeys(keys), newItem);
	}

	public void addItem(T newItem, String dottedString){
		array.put(dottedString, newItem);
	}

	public void addAll(MultiDimensionalArray<T> newItems){
		if(newItems != null){
			array.putAll(newItems.getArray());
		}
	}
	
	private Map<String, T> getArray(){
		return array;
	}

	public T getItem(int... keys){
		return array.get(hashCodeKeys(keys));
	}
	
	public T getItem(String key){
		return array.get(key);
	}

	public Collection<T> values(){
		return array.values();
	}

	public Collection<String> keySet(){
		return array.keySet();
	}

	public String hashCodeKeys(int... keys) {
		
		StringBuffer buffer = new StringBuffer();
		for(int key: keys){
			buffer.append(key).append(".");
		}
		return buffer.toString();
	}
	
	public void setDimensions(int[] initialDimensions){
		this.dimensionSpec = initialDimensions;
	}
	
	public int[] getDimensions(){
		return dimensionSpec;
	}	
}
