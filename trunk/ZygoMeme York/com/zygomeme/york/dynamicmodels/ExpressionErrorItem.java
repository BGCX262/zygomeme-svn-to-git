package com.zygomeme.york.dynamicmodels;

import com.zygomeme.york.dynamicmodels.ModelErrorReport.ErrorType;

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
 * Error record for invalid models or errors during the run. 
 *
 */
public class ExpressionErrorItem implements ErrorItem{

	
	private String message; // Informal message describing the error
	private int position; 	// The position in the expression where the error was spotted. 
	private ErrorType type;// The type of the error
	private String badItem; // The string associated with the error, for example a ")" if that was unexpected
	private String nodeId;
	       	
	public ExpressionErrorItem(int positionIn, String messageIn, ErrorType typeIn, String badItemIn, String nodeIdIn){
		this.message = messageIn;
		this.position = positionIn;
		this.type = typeIn;
		this.badItem = badItemIn;
		this.nodeId = nodeIdIn;
	}
	
	public String getMessage() {
		return message;
	}
	public int getPosition() {
		return position;
	}
	public ErrorType getType() {
		return type;
	}
	public String getBadItem(){
		return badItem;
	}
	public String getNodeId(){
		return nodeId;
	}
	
	
}
