package com.zygomeme.york.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;

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
 * Renders the legend for the chart.  
 */
public class LegendNugget implements RComponent{

	private ChartView chartView;
	private Map<String, Color> colorMap; 
	private int xPadding = 5;
	private int yPadding = 5;
	private int x; 
	private int y;
	private int width = 120;
	private int height = 120;
	private Color outlineColor = Color.BLACK;
	private Color textColor = Color.BLACK;
	
	public LegendNugget(ChartView chartViewIn, Map<String, Color> colorMapIn){
		this.chartView = chartViewIn;
		this.colorMap = colorMapIn;
	}
	
	public void draw(Graphics graphics){

		if(chartView.getModel() == null){
			return;
		}		
		ChartModel model = chartView.getModel();
		if(model.getDataSetCount() == 0){
			return; // nothing to draw
		}
		
		// Drop the font size to the current font - 2
		Font existingFont = graphics.getFont();
		Font legendFont = new Font(existingFont.getFontName(), existingFont.getStyle(), existingFont.getSize() - 2);
		graphics.setFont(legendFont);
		
		
		FontMetrics fontMetrics = graphics.getFontMetrics(); 
		int fontHeight = graphics.getFontMetrics().getHeight();
		int lineCount = 1;
		
		// Draw the outline box
		graphics.setColor(outlineColor);
		int nuggetHeight = (model.getDataSetCount() * fontHeight) + yPadding;
		nuggetHeight = Math.min(height, nuggetHeight);
		graphics.drawRect(x, y, width, nuggetHeight);		
		
		Iterator<String> it = model.getDataSetNamesIterator();
		int boxSize = fontHeight / 2; // For the little coloured boxes
		String nameWithTruncation;
		int spaceForLabel = width - (fontHeight + xPadding);
		while(it.hasNext() && (lineCount * fontHeight) < nuggetHeight){
			String name = it.next();
			graphics.setColor(colorMap.get(name));
			
			// The coloured box
			graphics.fillRect(x + xPadding, y + ((lineCount - 1) * fontHeight) + boxSize, boxSize, boxSize);
			
			graphics.setColor(textColor);
			// Get the text with any truncation
			nameWithTruncation = StringUtil.trunc(fontMetrics, name, spaceForLabel, 0);
			graphics.drawString(nameWithTruncation, x + fontHeight + xPadding, y + (lineCount * fontHeight));
			lineCount++;
		}
		
		// Reset to the current font
		graphics.setFont(existingFont);
	}
	
	public void setPosition(int newX, int newY){
		x = newX; 
		y = newY;
	}
	
	public void setWidth(int newWidth){
		width = newWidth;
	}
	
	public void setHeight(int newHeight){
		height = newHeight;
	}

	// Could do with an adapter?
	public boolean isMouseOver(MouseEvent me){return false;}
	public void actionEvent(){}
	public void setCommand(String newCommand){}
	public String getCommand(){return null;}
	public void addListener(RComponentListener listener){}
}
