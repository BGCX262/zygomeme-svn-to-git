package com.zygomeme.york.gui;

import java.awt.Cursor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.zygomeme.york.gui.TabView.ResizeCorner;

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
 * Used in the layout of nodes in the GUI. 
 * Decides where new nodes should be placed (using a strategy object), records the 
 * Z value and helps with resize requests.    
 * 
 */

public class LayoutManager {

	private Logger logger = Logger.getLogger(LayoutManager.class); 
	private int z = 0; 
	private SimpleLayoutStrategy layoutStrategy = new SimpleLayoutStrategy();
	private TabView tabView;
	
	private Map<YorkEntityView, Integer> modelToZMap = new HashMap<YorkEntityView, Integer>();
	
	// A count of the number of views that are requesting the cursor be a resize icon
	// TODO Shouldn't this be at most one item!!?
	private Map<YorkEntityView, ResizeCorner> resizeCursorMap = new HashMap<YorkEntityView, ResizeCorner>();
	
	public LayoutManager(TabView tabView){
		this.tabView = tabView;
	}
	
	public void addView(YorkEntityView view){
		
		// If the view doesn't already have config values, then set a position.
		if(view.getConfig() == null || view.getConfig().getX() == 0){
			logger.info("Requesting layout from the SimpleLayoutStrategy object");
			layoutStrategy.getPosition(view, modelToZMap, tabView.getWidth(), tabView.getHeight());
			logger.info("Config set to:" + view.getConfig());
		}
		
		// Store the z value for this view
		modelToZMap.put(view, z);
		z++;
	}
	
	public Set<YorkEntityView> getYorkModelViews(){
		return modelToZMap.keySet();
	}
	
	public void removeView(YorkEntityView view){
		modelToZMap.remove(view);
	}
	
	public int getZValue(YorkEntityView view){
		return modelToZMap.get(view);
	}
	
	public void addResizeRequest(YorkEntityView view, ResizeCorner resizeCorner){
		resizeCursorMap.put(view, resizeCorner);
	}
	
	public void removeResizeRequest(YorkEntityView view){
		resizeCursorMap.remove(view);
	}
	
	public boolean hasResizeRequest(){
		if(resizeCursorMap.size() > 0){
			return true;
		}
		else{
			return false;
		}
	}	

	public ResizeCorner getRequestCorner(){
		if(resizeCursorMap.size() > 0){
			// Convoluted to get the first item
			return resizeCursorMap.get(resizeCursorMap.keySet().iterator().next());
		}
		else{
			return null;
		}
	}	

	public boolean hasResizeCursorRequest(YorkEntityView view){
		return resizeCursorMap.containsKey(view);
	}
	
	public static int getResizeCursor(ResizeCorner resizeCorner){
		
		if(resizeCorner == ResizeCorner.TOP_LEFT || resizeCorner == ResizeCorner.BOTTOM_RIGHT){
			return Cursor.SE_RESIZE_CURSOR;
		}
		if(resizeCorner == ResizeCorner.TOP_RIGHT || resizeCorner == ResizeCorner.BOTTOM_LEFT){
			return Cursor.NE_RESIZE_CURSOR;
		}
		return Cursor.DEFAULT_CURSOR; 
	}

}
