package com.zygomeme.york.dynamicmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zygomeme.york.Node;
import com.zygomeme.york.dynamicmodels.DynamicModelHistory.StatusType;

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
 */

public class ModelErrorReport {

	// Strictly speaking these errors fall into two types, expression errors and node/model errors. 
	// It would be nice to have enums that can inherit, but without that I've combined the two
	// error types into one - on the principle of KIS
	public static enum ErrorType{NO_ERROR, DEADLOCKED, INFINITY_ENCOUNTED, MISSING_TO_NODE, 
		INVALID_VARIABLE, BRACKET_MISMATCH, UNEXPECTED_CLOSING_BRACKET, 
		MISSING_CLOSING_BRACKET, UNEXPECTED_OPERATOR, NAN_ENCOUNTED};
		
	private static Map<ErrorType, String> reportMessage = new HashMap<ErrorType, String>();
	private StatusType status = StatusType.YET_TO_RUN;
	
	private List<ErrorItem> modelErrors = new ArrayList<ErrorItem>();
	
	public ModelErrorReport(){
		
		reportMessage.put(ErrorType.NO_ERROR, "No error");
		reportMessage.put(ErrorType.DEADLOCKED, "Model is deadlocked. Check that the start nodes are correctly set.");
		reportMessage.put(ErrorType.INFINITY_ENCOUNTED, "Infinity was encounted whilst running the model.");
		reportMessage.put(ErrorType.NAN_ENCOUNTED, "Maths error encounted. Invalid value generated.");
		reportMessage.put(ErrorType.MISSING_TO_NODE, "Node references a node that does not exist.");
	}
		
	public void addError(ErrorType newError, String nodeId){
		status = StatusType.ERROR;
		modelErrors.add(new ModelErrorItem(newError, nodeId));
	}
	
	public void addError(ErrorType newError, List<Node> nodes){
		status = StatusType.ERROR;
		for(Node node: nodes){
			modelErrors.add(new ModelErrorItem(newError, node.getIdString()));
		}
	}
	
	public void addErrors(List<ErrorItem> newItems){
		status = StatusType.ERROR;
		modelErrors.addAll(newItems);
	}
	
	public List<ErrorItem> getReport(){
		return modelErrors;
	}
	
	public static String getMessage(ErrorType reportType){
		return reportMessage.get(reportType);
	}
	
	public StatusType getStatus(){
		return status;
	}

	public void setStatus(StatusType newStatus){
		status = newStatus;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer("ModelErrorReport[");
		
		buffer.append("status:").append(status);
		for(ErrorItem item: modelErrors){
			buffer.append("[Item:").append("Node:").append(item.getNodeId()).append(" Message:");
			buffer.append(item.getMessage()).append("]");
		}
		buffer.append("]");
		
		return buffer.toString();
	}

}
