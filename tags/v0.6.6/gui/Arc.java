package com.zygomeme.york.gui;

import java.util.Set;

import com.zygomeme.york.Node;
import com.zygomeme.york.dynamicmodels.DynamicModelNode;
import com.zygomeme.york.gui.EntityViewConfig;

/**
 * **********************************************************************
 *   This file forms part of the ZygoMeme York project - an analysis and
 *   modelling platform.
 *  
 *   Copyright (c) 2009 ZygoMeme Ltd., 
 *   email: coda@zygomeme.com
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
 * The model used to record the arcs in the GUI.  
 * 
 */

public class Arc {
		
	private YorkEntityView fromView;
	private YorkEntityView toView;
	
	private int fx, fy, tx, ty;
	
	public Arc(YorkEntityView from, YorkEntityView to){
		this.fromView = from;
		this.toView = to;
	}
	
	public EntityViewConfig getFromConfig(){
		return fromView.getConfig();		
	}

	public EntityViewConfig getToConfig(){
		return toView.getConfig();		
	}

	public void setEndPoints(int nfx, int nfy, int ntx, int nty) {
		this.fx = nfx; this.fy = nfy; this.tx = ntx; this.ty = nty;
	}

	public int getFx() {
		return fx;
	}

	public int getFy() {
		return fy;
	}

	public int getTx() {
		return tx;
	}

	public int getTy() {
		return ty;
	}
	
	public boolean isModified(){

		String fromNodeId = fromView.getNode().getIdString();
		Node toNode = toView.getNode();
		
		Set<String> currentModifierIds = ((DynamicModelNode)toNode).getModifiers().keySet();
		if(currentModifierIds.contains(fromNodeId)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public String getFromId(){
		return fromView.getModelsIdString();
	}
	
	public String getToId(){
		return toView.getModelsIdString();
	}
	
	public String toString(){
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("FromView:").append(fromView.getModelsIdString());
		buffer.append(", toView:").append(toView.getModelsIdString());
		
		return buffer.toString();
	}

}
