package com.zygomeme.york.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

import com.zygomeme.york.chart.ChartModel;
import com.zygomeme.york.chart.ChartView;
import com.zygomeme.york.dynamicmodels.DynamicModelHistory;
import com.zygomeme.york.gui.model.TableModel;
import com.zygomeme.york.gui.EntityViewConfig;
import com.zygomeme.york.table.TableView;

/**
 * *********************************************************************
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
 * This is the model of the viewable part of the model history, calls the chart and table to paint
 * themselves. 
 * 
 */
public class DynamicModelHistoryView extends YorkEntityView{

	private Logger logger = Logger.getLogger(DynamicModelHistoryView.class);
	private DynamicModelHistory history = new DynamicModelHistory();

	// This is the model of the viewable part of the model history  
	private TableModel tableViewModel = new TableModel(); // This is the viewable window on the DynamicModelHistory
	
	private TableView tableView;
	private ChartView chartView = null;
	private ChartModel chartModel = null;
	private EntityViewConfig parentConfig;
		
	public DynamicModelHistoryView(YorkBrowser browserIn, EntityViewConfig configIn, ButtonEventHandler buttonEventHandlerIn){
		super(browserIn);
		this.config = configIn;
		this.parentConfig = configIn;
		this.browser = browserIn;
		this.tableView = new TableView(this, 200);		
	}
	
	public void setHistory(DynamicModelHistory newHistory){
		this.history = newHistory;
	}
	
	public void updateConfig(){
		config.setX(parentConfig.getX());
		config.setY(parentConfig.getY());
		config.setWidth(parentConfig.getWidth());
		config.setHeight(parentConfig.getHeight());
	}
	
	public TableModel getTableModel(){
    	return tableViewModel;
    }
	
	public void paint(Graphics g){

		logger.info("DynamicModelHistoryView.paint()");
		
		if(chartView == null){
			this.chartView = new ChartView(parentConfig, browser);
			chartView.init();
		}
		
		updateConfig();
		
		if(history == null){
			logger.info("History not ready to draw");
			return;
		}
		// Fill the table model
		tableViewModel = new TableModel();
		for(int t = 0; t < history.getTimePointCount(); t++){
			tableViewModel.addColumn("t+" + t);
		}

		chartModel = new ChartModel();
		if(history != null && history.getTimePointCount() > 0 && history.getSnapshot(0) != null){
			for (Iterator<String> it = history.getSnapshot(0).keySet().iterator(); it.hasNext(); ){
				String name = it.next();
				tableViewModel.addRow(name);
				double[] chartData = new double[history.getTimePointCount()];
				// For all time points
				for(int t = 0; t < history.getTimePointCount(); t++){
					tableViewModel.addValue(name, "t+" + t, history.getSnapshot(t).get(name).getOutputValue());
					chartData[t] = history.getSnapshot(t).get(name).getOutputValue();
				}
				chartModel.addDataSet(name, chartData);
				//chartView.setSeriesStyle(name, ChartView.SeriesStyle.LINE);
			}
		}
		tableView.setTopLeftLabel("time");
		
		// Now draw the table
		tableView.paint(g);
		
		// Set the model and paint
		chartView.setModel(chartModel);	
		chartView.paint(g);				
		
	}
	
	public void saveChartAsImage(){
				
		BufferedImage bufferedImage = new BufferedImage(500, 400, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        
        EntityViewConfig chartConfig = new EntityViewConfig(0, 0, 500, 400);
        chartConfig.setBackgroundColor(Color.WHITE);
        ChartView chartPrintView = new ChartView(chartConfig, browser);
        chartPrintView.setRelativeToParent(false);
        chartPrintView.init();

        if(chartModel != null){
        	chartPrintView.setModel(chartModel);
        }
		chartPrintView.paint(g);
		
		// Get the file name 
		YorkGUIUtils guiUtils = new YorkGUIUtils();
		String filename = guiUtils.getUserToDefineFilename(browser, browser.getBaseDir(), "png", "Images", JFileChooser.SAVE_DIALOG);
		
		if(filename != null && !guiUtils.wasCancelled()){
			// Save the image to the specified file 
			YorkGUIUtils.saveBufferedImage(bufferedImage, filename);
		}
	}	
	
	public void mousePressed(MouseEvent e){
		super.mousePressed(e);
		tableView.mousePressed(e);
	}
	
	public void mouseDragged(MouseEvent e){
		//super.mouseDragged(e);
		tableView.mouseDragged(e);
	}
	
	public boolean isMouseOverHorizontalScrollBar(MouseEvent e){
		return tableView.isMouseOverHorizontalScrollBar(e);
	}

	public boolean isMouseOverVerticalScrollBar(MouseEvent e){
		return tableView.isMouseOverVerticalScrollBar(e);
	}

	public void setMouseBeingDraggedOnHorizontalScrollBar(boolean mouseDragFlag){
		tableView.setMouseBeingDraggedOnHorizontalScrollBar(mouseDragFlag);
	}
	
	public void setMouseBeingDraggedOnVerticalScrollBar(boolean mouseDragFlag){
		tableView.setMouseBeingDraggedOnVerticalScrollBar(mouseDragFlag);
	}
}
