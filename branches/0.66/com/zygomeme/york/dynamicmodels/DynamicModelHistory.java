package com.zygomeme.york.dynamicmodels;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zygomeme.york.Identifiable;
import com.zygomeme.york.Node;
import com.zygomeme.york.util.StringUtil;

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
 * Stores the history of the model - the values and states of the model for each time point. 
 * This is then used as the data source for the chart, table, any other history GUI element, and
 * the history report - the HTML report dumped to file.  
 * Also includes links to the error reports, these can be for static "compile" type errors such 
 * as invalid expressions and run time errors - divide by zeros etc.. 
 * 
 */

public class DynamicModelHistory implements Identifiable{

	private Map<Integer, Map<String, DynamicNodeMemento>> history = new HashMap<Integer, Map<String, DynamicNodeMemento>>();
	private String idString;
	private ModelErrorReport errorReport = new ModelErrorReport();
	public enum StatusType{SUCCESS, ERROR, YET_TO_RUN};
	private List<String> startNodeIds;
	private Logger logger = Logger.getLogger(DynamicModelHistory.class);
	
	public void setupRecords(Map<String, Node> nodeMap, int timePoint){
			
		Map<String, DynamicNodeMemento> snapshot = new LinkedHashMap<String, DynamicNodeMemento>();
		for(Node node: nodeMap.values()){
			DynamicNodeMemento memento = new DynamicNodeMemento((DynamicModelNode)node);
			snapshot.put(node.getIdString(), memento);
		}
		
		// Add the snapshot to the history
		history.put(timePoint, snapshot);
	}
	
	public void recordState(Map<String, Node> nodeMap, int timePoint){
		
		// Build the snapshot
		// Assumes the snapshot has already been added for this timepoint (by recordInputs)
		Map<String, DynamicNodeMemento> snapshot = history.get(timePoint);
		for(Node node: nodeMap.values()){
			DynamicNodeMemento memento = snapshot.get(node.getIdString());
			memento.setOutputValue(node.getOutputValue());
			//snapshot.put(node.getIdString(), memento);
		}
	}

	public void reset(){
		errorReport.setStatus(StatusType.YET_TO_RUN);
		history = new HashMap<Integer, Map<String, DynamicNodeMemento>>();
		errorReport = new ModelErrorReport();
	}
	
	public void setStartNodeIds(List<String> nodeIds){
		this.startNodeIds = nodeIds;
	}
	
	public List<String> getStartNodeIds(){
		return startNodeIds;
	}
	
	public ModelErrorReport getErrorReport(){
		return errorReport;
	}
	
	public Map<Integer, Map<String, DynamicNodeMemento>> getHistory(){
		return history;
	}
	
	public void setStatus(StatusType newStatus){
		errorReport.setStatus(newStatus);
	}
	
	public StatusType getStatus(){
		return errorReport.getStatus();
	}
	
	public int getTimePointCount(){
		return history.size();
	}
	
	public Map<String, DynamicNodeMemento> getSnapshot(int timePoint){
		return history.get(timePoint);
	}
	
	public String getIdString(){
		return idString;
	}
	
	public void setIdString(String idStringIn){
		this.idString = idStringIn;
	}

	public void addToHistoryLog(String newMessage){
		logger.info(newMessage);
	}

	public String toString(){
		
		StringBuilder builder = new StringBuilder(300);
		
		// Headers
		builder.append(StringUtil.padRight("Item", 14));
		for(int i = 0; i < history.size(); i++){
			builder.append(StringUtil.padRight("t+" + i, 10));
		}
		builder.append("\n");
		
		
		String outputString;
		if(history != null && history.size() > 0 && history.get(0) != null){
			for (Iterator<String> it = history.get(0).keySet().iterator(); it.hasNext(); ){ 
				String name = it.next();
				builder.append(StringUtil.padRight(name, 14));
				// For all time points
				for(int t = 0; t < getTimePointCount(); t++){
					// Get the output value and pad it out 
					outputString = StringUtil.padRight("" + getSnapshot(t).get(name).getOutputValue(), 9);
					builder.append(outputString).append(" ");
				}
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	
}
