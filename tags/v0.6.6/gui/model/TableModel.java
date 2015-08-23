package com.zygomeme.york.gui.model;

import java.util.LinkedHashMap;
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
 * TableModel for the table that appears in the GUI. Stored as a 2D array
 * of objects for speed and robustness of code. The array is dynamically 
 * resized if the the array limits are exceeded. 
 * 
 * 
 */

public class TableModel {

	private Map<String, Integer> columnNameMap = new LinkedHashMap<String, Integer>(); 
	private Map<Integer, String> columnIndexMap = new LinkedHashMap<Integer, String>(); 
	private Map<String, Integer> rowNameMap = new LinkedHashMap<String, Integer>();
	private Map<Integer, String> rowIndexMap = new LinkedHashMap<Integer, String>(); 

	public final static int DEFAULT_ROW_CAPACITY = 20;
	public final static int DEFAULT_COLUMN_CAPACITY = 20;
	private int currentRowCount = DEFAULT_ROW_CAPACITY;
	private int currentColumnCount = DEFAULT_COLUMN_CAPACITY;
	private Object[][] table = new Object[currentRowCount][currentColumnCount];
		
	
	private void resize(int requestedI, int requestedJ){

		// Resize logic
		int newRowCount = currentRowCount; int newColumnCount = currentColumnCount;
		if(requestedI >= currentRowCount){
			newRowCount = Math.max(requestedI + (requestedI / 10), requestedI + 10); 
		}
		
		if(requestedJ >= currentColumnCount){
			newColumnCount = Math.max(requestedJ + (requestedJ / 10), requestedJ + 10);
		}

		// Create new table of that size and copy in the values
		Object[][] newTable = new Object[newRowCount][newColumnCount];
		for(int i = 0; i < currentRowCount; i++){
			for(int j = 0; j < currentColumnCount; j++){
				newTable[i][j] = table[i][j];
			}
		}
		table = newTable;
		currentColumnCount = table[0].length;
		currentRowCount = table.length;
	}
	
	public int getRowCapacity(){
		return currentRowCount;
	}

	public int getColumnCapacity(){
		return currentColumnCount;
	}

	public void addColumn(String newColumnName){
		if(columnNameMap.size() >= currentColumnCount){
			resize(currentRowCount - 1, columnNameMap.size());
		}
		int index = columnNameMap.size();
		columnNameMap.put(newColumnName, index);
		columnIndexMap.put(index, newColumnName);
	}
	
	public int getColumnCount(){
		return columnNameMap.size();
	}

	public int getRowCount(){
		return rowNameMap.size();
	}

	public void addRow(String newRowName){
		if(rowNameMap.size() >= currentRowCount){
			resize(rowNameMap.size(), currentColumnCount - 1);
		}
		int index = rowNameMap.size();
		rowNameMap.put(newRowName, index);
		rowIndexMap.put(index, newRowName );
	}

	public void addValue(String row, String column, Integer value){
		addValue(row, column, value.doubleValue());
	}
	
	public void addValue(String row, String column, Double value){
		table[rowNameMap.get(row)][columnNameMap.get(column)] = value;
	}

	public void setValue(int i, int j, double value){
		if(i >= currentRowCount || j >= currentColumnCount){
			resize(i, j);
		}
		table[i][j] = value;
	}

	public String getValueAsString(int i, int j){
		if(table[i][j] != null){
			return table[i][j].toString();
		}
		else{
			return "";
		}
	}

	public String getValueAsString(String rowName, String columnName){
		return table[rowNameMap.get(rowName)][columnNameMap.get(columnName)].toString();
	}

	public String getColumnHeader(int index){
		return columnIndexMap.get(index);
	}
	
	public String getRowHeader(int index){
		return rowIndexMap.get(index);
	}

}
