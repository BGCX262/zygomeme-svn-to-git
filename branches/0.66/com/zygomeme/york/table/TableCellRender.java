package com.zygomeme.york.table;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import com.zygomeme.york.gui.NumberFormatter;
import com.zygomeme.york.gui.model.TableModel;

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
 * Draw the text/numbers in the cells of the table    
 * 
 */

public class TableCellRender {

	private int cellPad = 4;
	public enum Justification{LEFT, RIGHT, CENTER};
	private NumberFormatter formatter = new NumberFormatter();
	
	public int render(Graphics g, int x, int y, String label, int fixedWidth, Justification justification, Color fontColor, Color borderColor){
	
		FontMetrics metrics = g.getFontMetrics(); 
		int fontHeight = metrics.getHeight();
		
		int width;
		label = formatter.format(label);
		int messageWidth = getWidth(metrics, label); 
		if(fixedWidth > 0){
			width = fixedWidth;
		}
		else{
			width = messageWidth;
		}
		
		g.setColor(borderColor);
		g.drawRect(x, y, width, fontHeight + cellPad);				
		
		g.setColor(fontColor);
		switch(justification){
		case LEFT: g.drawString(label, x + cellPad, (y + fontHeight) - (cellPad / 2)); break;
		case RIGHT: g.drawString(label, x + cellPad + (width - messageWidth), (y + fontHeight) - (cellPad / 2)); break;
		case CENTER: g.drawString(label, x + cellPad + ((width - messageWidth) / 2), (y + fontHeight) - (cellPad / 2));break;
		}
		return width;
	}
	
	public int getWidth(FontMetrics metrics, String label){
		label = formatter.format(label);
		return metrics.stringWidth(label) + (cellPad * 2);
	}
	
	public int getCellHeight(Graphics g){

		int fontHeight = g.getFontMetrics().getHeight();
		return fontHeight + cellPad;
	}
	
	public int getRowHeaderColumnWidth(Graphics g, TableModel tableModel, String topLeftLabel){

		FontMetrics metrics = g.getFontMetrics(); 

		// Get the maximum width of the strings that form the row headers and the table label
		int maxWidth = getWidth(metrics, topLeftLabel);
		for(int i = 0; i < tableModel.getRowCount(); i++){
			maxWidth = Math.max(getWidth(metrics, tableModel.getRowHeader(i)), maxWidth);
		}
				
		return maxWidth;
	}
	
	public int getColumnWidth(Graphics g, TableModel tableModel, int index){

		FontMetrics metrics = g.getFontMetrics(); 

		// Get the maximum width of the strings that form the column headers and the row contents
		int maxWidth = getWidth(metrics, tableModel.getColumnHeader(index));
		for(int i = 0; i < tableModel.getRowCount(); i++){
			maxWidth = Math.max(getWidth(metrics, tableModel.getValueAsString(i, index)), maxWidth);
		}
				
		return maxWidth;
	}

}
