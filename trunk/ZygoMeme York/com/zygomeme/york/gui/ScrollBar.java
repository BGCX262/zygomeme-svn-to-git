package com.zygomeme.york.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

import org.apache.log4j.Logger;

import com.zygomeme.york.chart.TextNugget;

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
 * Scroll bar code for the scroll bars of the table.     
 * 
 */

public class ScrollBar {

	private Logger logger = Logger.getLogger(ScrollBar.class);
	
	private int x = 10;
	private int y = 10;
	private int tableWidth = 0;
	private int barLength = 300;
	private int barWidth = 16;
	private int sliderLength = 15;
	private int sliderPosition = 0;
	private double logicalPosition = 0.0; 
	private int columnCount = 1;
	private YorkEntityView parentView; 
	public enum Orientation{VERTICAL, HORIZONTAL};
	private Orientation orientation = Orientation.VERTICAL;
	private boolean mouseBeingDragged = false;
	
	private GradientPaint gradient;
	private Stroke sliderStroke = new BasicStroke(barWidth - 2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
	
	private TextNugget debug;
		
	public ScrollBar(Orientation or, YorkBrowser browser, YorkEntityView parentIn){
		this.orientation = or;
		this.parentView = parentIn;
		debug = new TextNugget(browser);
		debug.setIdString("Debug");
		debug.setPositionAndWidth(20, 30, 190);
	}
	
	public void paint(Graphics g) {
		
		g.setColor(parentView.getConfig().getBorderColor());
		
		setBarLength();		
		
		if(orientation == Orientation.VERTICAL){
			drawVerticalScrollBar((Graphics2D)g);
		}
		else{
			drawHorizontalScrollBar((Graphics2D)g);
		}
		
		//debug.draw(g);
	}

	private void drawVerticalScrollBar(Graphics2D g){
		
		g.drawRect(x, y + barWidth, barWidth, barLength);
				
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
		// Draw the end controls
		g.drawRect(x, y + barLength + barWidth, barWidth, barWidth);
		int[] xPoints = new int[3];
		xPoints[0] = x + 4;
		xPoints[1] = x + (barWidth - 3);
		xPoints[2] = x + (barWidth / 2) + 1;
		int[] yPoints = new int[3];
		yPoints[0] = y + barLength + barWidth + 4;
		yPoints[1] = yPoints[0];
		yPoints[2] = y + barLength + (2 * barWidth) - 3;		
		g.fillPolygon(xPoints, yPoints, 3);

		g.drawRect(x, y, barWidth, barWidth);
		yPoints[0] = y + barWidth - 3;
		yPoints[1] = yPoints[0];
		yPoints[2] = y + 3;		
		g.fillPolygon(xPoints, yPoints, 3);
		
		// Flat-ended gradient bar	
		Stroke previousStroke = g.getStroke();
		gradient = new GradientPaint(x, 0, Color.gray, x + barWidth, 0, parentView.getConfig().getBorderColor());
		g.setPaint(gradient);
		g.setStroke(sliderStroke);
		int xPos = x + (barWidth / 2);
		g.drawLine(xPos, sliderPosition, xPos, sliderPosition + sliderLength);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		g.setStroke(previousStroke);
	}

	private void drawHorizontalScrollBar(Graphics2D g){
		
		g.drawRect(x + barWidth, y, barLength, barWidth);		
							
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Draw the end controls
		g.drawRect(x, y, barWidth, barWidth);
		int[] xPoints = new int[3];
		xPoints[0] = x + barWidth - 3;
		xPoints[1] = xPoints[0];
		xPoints[2] = x + 3;
		int[] yPoints = new int[3];
		yPoints[0] = y + 3;
		yPoints[1] = y + barWidth - 3;
		yPoints[2] = y + (barWidth / 2) + 1;
		g.fillPolygon(xPoints, yPoints, 3);

		
		g.drawRect(x + barLength + barWidth, y, barWidth, barWidth);
		xPoints[0] = x + barLength + barWidth + 4;
		xPoints[1] = xPoints[0];
		xPoints[2] = x + barLength + (2 * barWidth) - 3;			
		g.fillPolygon(xPoints, yPoints, 3);

		// Draw the slider
		// Simple rectangle
		//g.fillRect(sliderPosition, y, sliderLength, barWidth);
				
		// Flat-ended gradient bar	
		Stroke previousStroke = g.getStroke();
		gradient = new GradientPaint(0, y, Color.gray, 0, y + barWidth, parentView.getConfig().getBorderColor());
		g.setPaint(gradient);
		g.setStroke(sliderStroke);
		int yPos = y + (barWidth / 2);
		g.drawLine(sliderPosition, yPos, sliderPosition + sliderLength, yPos);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		g.setStroke(previousStroke);
		
	}

	public void mousePressed(MouseEvent e){
		
		logger.info("mousePressed() sliderPosition:" + sliderPosition);
		boolean hit = false;
		if(orientation == Orientation.HORIZONTAL){
			// Left box
			if(e.getX() > x && e.getX() < (x + barWidth) && e.getY() > y && e.getY() < (y + barWidth)){
				hit = true;
				debug.setValue("Before SliderPosition", sliderPosition);
				sliderPosition = sliderPosition - (barLength / columnCount);
				debug.setValue("columnCount", columnCount);
				debug.setValue("After SliderPosition", sliderPosition);
			}
			logger.info("left hit:" + hit + ", sliderPosition:" + sliderPosition);


			hit = false;
			if(e.getX() > (x + barWidth + barLength) && e.getX() < (x + barWidth + barWidth + barLength) && e.getY() > y && e.getY() < (y + barWidth)){
				hit = true;
				debug.setValue("Before SliderPosition", sliderPosition);
				sliderPosition = sliderPosition + (barLength / columnCount);
				debug.setValue("columnCount", columnCount);
				debug.setValue("After SliderPosition", sliderPosition);
			}
			logger.info("right hit:" + hit + ", sliderPosition:" + sliderPosition);
		}
		else{
			// Upper box
			if(e.getX() > x && e.getX() < (x + barWidth) && e.getY() > y && e.getY() < (y + barWidth)){
				hit = true;
				debug.setValue("Before SliderPosition", sliderPosition);
				sliderPosition = sliderPosition - (barLength / columnCount);
				debug.setValue("columnCount", columnCount);
				debug.setValue("After SliderPosition", sliderPosition);
			}
			logger.info("left hit:" + hit + ", sliderPosition:" + sliderPosition);

			// Lower box
			hit = false;
			if(e.getX() > x && e.getX() < (x + barWidth) && e.getY() > (y + barLength + barWidth) && e.getY() < (y + barWidth + barLength + barWidth)){
				hit = true;
				//debug.setValue("Before SliderPosition", sliderPosition);
				sliderPosition = sliderPosition + (barLength / columnCount);
				debug.setValue("columnCount", columnCount);
				//debug.setValue("After SliderPosition", sliderPosition);
			}
			logger.info("right hit:" + hit + ", sliderPosition:" + sliderPosition);			
		}
		imposeLimits();
	}
	
	public void mouseDragged(MouseEvent e){

		logger.info("mouseDragged(), mouseBeingDragged:" + mouseBeingDragged );
		if(mouseBeingDragged){
			if(orientation == Orientation.VERTICAL){
				sliderPosition = e.getY() - (sliderLength / 2);
			}
			else{
				sliderPosition = e.getX() - (sliderLength / 2);
			}
			imposeLimits();
		}
	}
	
	public double getLogicalPosition(){
		return logicalPosition;
	}
	
	private void imposeLimits(){
		
		// TODO The calculations are the same apart for x and y. Should be able to shorten.
		if(orientation == Orientation.VERTICAL){
			// Calculate the slider position
			if(sliderPosition > (y + barLength + barWidth - sliderLength)){
				sliderPosition = y + barLength + barWidth - sliderLength;
			}
			
			if(sliderPosition < y + barWidth){
				sliderPosition = y + barWidth;
			}
			logicalPosition = (((double)(sliderPosition - y - barWidth)) / ((double)(barLength - sliderLength))) * 100.0;			

		}
		else{
			// Calculate the slider position
			if(sliderPosition > (x + barLength + barWidth - sliderLength)){
				sliderPosition = x + barLength + barWidth - sliderLength;
			}
			
			if(sliderPosition < x + barWidth){
				sliderPosition = x + barWidth;
			}
			logicalPosition = (((double)(sliderPosition - x - barWidth)) / ((double)(barLength - sliderLength))) * 100.0;
						
			logger.info("(((double)(sliderPosition - x - barWidth)):" + ((double)(sliderPosition - x - barWidth)) + 
					"/ (barLength - sliderLength):" + (barLength - sliderLength));
			logger.info("barLength:" + barLength + ", barWidth:" + barWidth + ", sliderLength:" + sliderLength);
			logger.info("logical Horizontal Position:" + logicalPosition);
			logger.info("SliderPosition:" + sliderPosition);
			
			debug.setValue("logicalPosition", logicalPosition);
			debug.setValue("sliderPosition", sliderPosition);
			debug.setValue("barLength", barLength);
			debug.setValue("barWidth", barWidth);			
			debug.setValue("x", x);
		}		
		
	}
	
	public void setPositionAndWidth(int newX, int newY, int renderedWidth){

		if(orientation != Orientation.VERTICAL){
			logger.info("setPositionAndWidth(), mouseBeingDragged:" + mouseBeingDragged + 
					", logicalPosition:" + logicalPosition);
		}

		int oldX = x;
		x = newX;
		int oldY = y;
		y = newY;
		tableWidth = renderedWidth;
		if(!mouseBeingDragged){
			if(orientation == Orientation.HORIZONTAL){
				sliderPosition += (newX - oldX);
			}
			else{
				sliderPosition += (newY - oldY);
			}
		}
		imposeLimits();		
	}

	public void setSliderLength(double totalPossibleWidth, double renderedWidth, int columnCountIn){
		
		// columnCount is used by the buttons at either end to work out how big a step to 
		// make when the user hits them to scroll.
		columnCount = columnCountIn;
		tableWidth = (int)renderedWidth;
		setBarLength();
		debug.setValue("SetcolumnCount", columnCount);
		debug.setValue("barLength foo", barLength);
		sliderLength = (int)((renderedWidth / totalPossibleWidth) * barLength);
		sliderLength = Math.max(sliderLength, barWidth);
		debug.setValue("SliderLength", sliderLength);
	}
	
	public void setBarLength(){
		barLength = tableWidth - (2 * barWidth);
	}
	
	public int getBarWidth(){
		return barWidth;
	}
	
	public void setMouseBeingDragged(boolean mouseDragFlag){
		mouseBeingDragged = mouseDragFlag;
	}
	
	public boolean isMouseOver(MouseEvent e){
		
		if(orientation == Orientation.HORIZONTAL){
			if(e.getX() > (x + barWidth) && e.getX() < (x + barLength) &&
					e.getY() > y && e.getY() < y + barWidth){
				logger.info("Mouse over scrollbar:" + true);
				return true;
			}
			else{
				logger.info("Mouse over scrollbar:" + false);
				return false;
			}
		}
		else{
			logger.info("ex:" + e.getX() + ", x + barWidth:" + (x + barWidth));
			if(e.getX() > x && e.getX() < (x + barWidth) &&
					e.getY() > (y + barWidth) && e.getY() < (y + barLength + barWidth)){
				logger.info("V Mouse over scrollbar:" + true);
				return true;
			}
			else{
				logger.info("V Mouse over scrollbar:" + false);
				return false;
			}			
		}
	}
}
