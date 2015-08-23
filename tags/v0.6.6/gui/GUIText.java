package com.zygomeme.york.gui;

import java.util.ArrayList;
import java.util.List;

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
 * GUIText provides functionality for text strings in the GUI to be in different colors.   
 * 
 */
public class GUIText {

	private String message = "";
	private List<Integer[]> startAndEndPoints = new ArrayList<Integer[]>();
	
	
	public void addStartAndEndPoint(int start, int end){
		startAndEndPoints.add(new Integer[]{start, end});
	}
	
	public List<Integer[]> getStartAndEndPoints(){
		return startAndEndPoints;
	}
	
	public void setStartStopPoints(List<Integer[]> newPoints){
		startAndEndPoints = newPoints;
	}
	
	public void setMessage(String newMessage){
		this.message = newMessage;
	}
	
	public String getMessage(){
		return message;
	}
}
