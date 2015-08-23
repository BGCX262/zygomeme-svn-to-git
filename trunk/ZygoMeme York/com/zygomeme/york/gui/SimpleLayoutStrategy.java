package com.zygomeme.york.gui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zygomeme.york.gui.EntityViewConfig;

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
 * Code to decide where in the GUI new nodes should be placed.     
 * 
 */

public class SimpleLayoutStrategy {
	
	private int pad = 14;
	
	public EntityViewConfig getPosition(YorkEntityView newView, Map<YorkEntityView, Integer> modelToZMap, 
			int tabViewWidth, int tabViewHeight){
		
		EntityViewConfig config = newView.getConfig();
		if(config == null){
			newView.setConfig(new EntityViewConfig(0, 0, 10, 10));
			config = newView.getConfig();
		}
		
		int w = config.getWidth();
		int h = config.getHeight();
		
		List<Rectangle> rectangles = new ArrayList<Rectangle>(); 
		for(YorkEntityView view: modelToZMap.keySet()){
			rectangles.add(new Rectangle(view.getConfig().getX() - pad, view.getConfig().getY() - pad, 
									       view.getConfig().getWidth() + (2 * pad), 
									       view.getConfig().getHeight() + (2 * pad)));
		}

		// Look at the all the x and y position in increments of 10 to see if there are any suitable spaces, 
		// this is N^3 but this is only done when the nugget is first added. 
		// An optimisation would be to add the width/height of the rectangle that it clashes with. So rather
		// than add a small fixed increment in the normal way it adds the width of the other rectangle 
		// to skip other it.
		Rectangle displayRect = new Rectangle(tabViewWidth, tabViewHeight);
		for(int y = 20; y < tabViewHeight; y += 15){
			for(int x = 20; x < tabViewWidth; x+= 15){
				Rectangle newPos = new Rectangle(x, y, w, h);
				boolean intersection = false;
				for(Rectangle rect: rectangles){
					if(rect.intersects(newPos)){
						intersection = true;
					}
				}
				
				// If the potential position doesn't intersect with any of the other nodes 
				// and is wholly within the display the use this position as the chosen one
				// an return with this value. 
				if(!intersection && displayRect.contains(newPos)){
					config.setX(newPos.x);
					config.setY(newPos.y);						
					return config;
				}
			}
		}
				
		// If all else fails (when there is no room according to the rules)...
		config.setX(100);
		config.setY(100);
		return config;
	}
}
