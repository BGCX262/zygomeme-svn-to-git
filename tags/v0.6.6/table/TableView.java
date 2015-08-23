package com.zygomeme.york.table;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import org.apache.log4j.Logger;

import com.zygomeme.york.chart.TextNugget;
import com.zygomeme.york.gui.model.TableModel;
import com.zygomeme.york.gui.DynamicModelHistoryView;
import com.zygomeme.york.gui.EntityViewConfig;
import com.zygomeme.york.gui.ScrollBar;

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
 * View for the table object - paints the table from the values in the table model
 * 
 */

public class TableView {

	private Logger logger = Logger.getLogger(TableView.class);
	
	private DynamicModelHistoryView parentView;
	private int yOffset = 25;
	private int xOffset = 5;
	private String topLeftLabel;
	private ScrollBar scrollBarHorizontal;
	private ScrollBar scrollBarVertical;
	private int maxAllowedTableWidth = 200;
	private TextNugget debug;

	private TableCellRender cRender = new TableCellRender();
		
	public TableView(DynamicModelHistoryView parentIn, int initialTableWidth){
		this.parentView = parentIn;		
		this.maxAllowedTableWidth = initialTableWidth;
		debug = new TextNugget(parentIn.getBrowser());
		debug.setPositionAndWidth(20, 30, 230);
		scrollBarHorizontal = new ScrollBar(ScrollBar.Orientation.HORIZONTAL, parentIn.getBrowser(), parentView);
		scrollBarVertical = new ScrollBar(ScrollBar.Orientation.VERTICAL, parentIn.getBrowser(), parentView);
	}
	
	public void paint(Graphics g){
						
		EntityViewConfig parentConfig = parentView.getConfig();		
		TableModel tableModel = parentView.getTableModel();
	
		if(tableModel.getColumnCount() == 0){
			return;
		}

		int tlx = parentConfig.getX() + xOffset;
		int tly = parentConfig.getY() + yOffset;
		maxAllowedTableWidth = parentConfig.getWidth() - (2 * xOffset) - scrollBarVertical.getBarWidth();
		
		// Calculate column widths
		String tableLabel;
		if(topLeftLabel == null){
			tableLabel = "time point";
		}
		else{
			tableLabel = topLeftLabel;
		}

		int totalPossibleWidth = 0;
		int[] columnWidths = new int[tableModel.getColumnCount() + 1];
		columnWidths[0] = cRender.getRowHeaderColumnWidth(g, tableModel, tableLabel);
		for(int i = 0; i < tableModel.getColumnCount(); i++){
			columnWidths[i + 1] = cRender.getColumnWidth(g, tableModel, i);
			totalPossibleWidth += columnWidths[i];
		}
		totalPossibleWidth += columnWidths[columnWidths.length - 1];

		// Get the position of the scroll bars
		double horizontalScrollbarPosition = scrollBarHorizontal.getLogicalPosition();
		logger.info("Logical Column:" + horizontalScrollbarPosition);
		
		int startingIndex = getTheStartingColumnIndex(columnWidths, tableModel);
		startingIndex = (int)(startingIndex / (100 / horizontalScrollbarPosition));
		
		double verticalScrollbarPosition = scrollBarVertical.getLogicalPosition();
		debug.setValue("verticalScrollbarPosition", verticalScrollbarPosition);
		int visibleRowCount = 3;
		double foo = (100.0 / (tableModel.getRowCount() - visibleRowCount));
		debug.setValue("tableModel.getRowCount()", tableModel.getRowCount());
		debug.setValue("foo", foo);
		int startingRow = (int)(verticalScrollbarPosition / foo);
		debug.setValue("startingRow", startingRow);

		// Render the t+n column headers
		int x = tlx;
		Color fontColor = parentView.getConfig().getFontColor();
		Color borderColor = parentView.getConfig().getBorderColor();
		x += cRender.render(g, x, tly, tableLabel, columnWidths[0], TableCellRender.Justification.LEFT, fontColor, borderColor);
		int lastColumnRendered = startingIndex;
		boolean spaceRemains = true;
		for(int i = startingIndex; i < tableModel.getColumnCount() && spaceRemains; i++){

			int gap = maxAllowedTableWidth - (x - tlx);
			if(gap >= columnWidths[i + 1]){
				x += cRender.render(g, x, tly, tableModel.getColumnHeader(i), columnWidths[i + 1], TableCellRender.Justification.CENTER, fontColor, borderColor);
				lastColumnRendered++;
			}
			else{
				spaceRemains = false;
			}
		}
		
		boolean allColumnsShown = false;
		if(startingIndex == 0 && lastColumnRendered == tableModel.getColumnCount()){
			allColumnsShown = true;
		}
		
		// Render the partial box at the right side
		int tableWidth = x - tlx; 
		int gap = maxAllowedTableWidth - tableWidth;

		// If the last column has been rendered then don't put "..." in the partial column 
		String gapString = "...";
		if(lastColumnRendered == tableModel.getColumnCount()){
			gapString = "";
		}

		if(!allColumnsShown){
			//logger.info("gap:" + gap);
			if(gap > 17){
				cRender.render(g, x, tly, gapString, gap, TableCellRender.Justification.CENTER, fontColor, borderColor);
			}
			else{
				if(gap > 0){
					cRender.render(g, x, tly, "", gap, TableCellRender.Justification.RIGHT, fontColor, borderColor);
				}
			}
		}
		
		// Render rows
		int cellHeight = cRender.getCellHeight(g);
		int y = tly;
		int columnsRendered = 0;
		int rowsRendered = Math.min(visibleRowCount, tableModel.getRowCount());
				
		for(int i = startingRow; i < (startingRow + rowsRendered); i++){
			x = tlx;
			y += cellHeight;
			// Render row header
			String rowHeader = tableModel.getRowHeader(i);
			x += cRender.render(g, x, y, rowHeader, columnWidths[0], TableCellRender.Justification.LEFT, fontColor, borderColor);
			columnsRendered = 0;
			int columnIndex; 
			// TODO this should not be so complicated as the rendering of the header has calculated the starting index
			// the end index etc.. 
			spaceRemains = true;
			for(columnIndex = startingIndex; columnIndex < tableModel.getColumnCount() && spaceRemains; columnIndex++){

				gap = maxAllowedTableWidth - (x - tlx);
				//logger.debug("ColumnWidth:" + columnWidths[columnIndex+ 1] + " gap:" + gap + " row:" + i);
				if(gap >= columnWidths[columnIndex + 1]){
					x += cRender.render(g, x, y, tableModel.getValueAsString(i,columnIndex), 
							columnWidths[columnIndex + 1], TableCellRender.Justification.RIGHT, 
							fontColor, borderColor);
					columnsRendered++;
				}
				else{
					spaceRemains = false;
				}
			}
			
			if(!allColumnsShown){
				// Render the partial box at the right side
				gap = maxAllowedTableWidth - (x - tlx);
				
				//logger.info("gap:" + gap);
				if(gap > 17){
					cRender.render(g, x, y, gapString, gap, TableCellRender.Justification.CENTER, fontColor, borderColor);
				}
				else{
					if(gap > 0){
						cRender.render(g, x, y, "", gap, TableCellRender.Justification.CENTER, fontColor, borderColor);
					}
				}
			}
		}
		
		// Work out how wide the table should actually be
		int paddedTableWidth = maxAllowedTableWidth;
		if(!allColumnsShown){		
			scrollBarHorizontal.setSliderLength(totalPossibleWidth, paddedTableWidth, tableModel.getColumnCount());
			scrollBarHorizontal.setPositionAndWidth(tlx, y + cellHeight, paddedTableWidth);
			scrollBarHorizontal.paint(g);	
			
			if(visibleRowCount < tableModel.getRowCount()){
				scrollBarVertical.setSliderLength(cellHeight * tableModel.getRowCount(), cellHeight * visibleRowCount, tableModel.getRowCount());
				scrollBarVertical.setPositionAndWidth(tlx + maxAllowedTableWidth, tly + cellHeight, cellHeight * visibleRowCount);
				scrollBarVertical.paint(g);
			}
		}
		else{
			if(visibleRowCount < tableModel.getRowCount()){
				scrollBarVertical.setSliderLength(cellHeight * tableModel.getRowCount(), cellHeight * visibleRowCount, tableModel.getRowCount());
				scrollBarVertical.setPositionAndWidth(tlx + tableWidth, tly + cellHeight, cellHeight * visibleRowCount);
				scrollBarVertical.paint(g);
			}
		}

		//debug.draw(g);
	}
	
	private int getTheStartingColumnIndex(int[] columnWidths, TableModel tableModel){
		
		int widthIncludingNextColumn = columnWidths[0]; // Width of the table if the next column is also rendered
		widthIncludingNextColumn += columnWidths[columnWidths.length - 1];
		int startingIndex = columnWidths.length - 1;

		// TODO best not to use object level variable (maxAllowedTableWidth) - confusing
		for(int i = columnWidths.length - 2; i >= 0 && widthIncludingNextColumn < maxAllowedTableWidth; i--){			
			widthIncludingNextColumn += columnWidths[i];
			startingIndex--;
		}
		
		return startingIndex;
	}
	
	public void mousePressed(MouseEvent e){
		scrollBarHorizontal.mousePressed(e);		
		scrollBarVertical.mousePressed(e);		
	}
	
	public void mouseDragged(MouseEvent e){		
		scrollBarHorizontal.mouseDragged(e);
		scrollBarVertical.mouseDragged(e);
	}
	
	public boolean isMouseOverHorizontalScrollBar(MouseEvent e){
		return scrollBarHorizontal.isMouseOver(e);
	}

	public boolean isMouseOverVerticalScrollBar(MouseEvent e){
		return scrollBarVertical.isMouseOver(e);
	}

	public void setTopLeftLabel(String newLabel){
		topLeftLabel = newLabel;
	}
	
	public String getTopLeftLabel(){
		return topLeftLabel;
	}	
	
	public void setMouseBeingDraggedOnHorizontalScrollBar(boolean mouseDragFlag){
		scrollBarHorizontal.setMouseBeingDragged(mouseDragFlag);
	}

	public void setMouseBeingDraggedOnVerticalScrollBar(boolean mouseDragFlag){
		scrollBarVertical.setMouseBeingDragged(mouseDragFlag);
	}
}
