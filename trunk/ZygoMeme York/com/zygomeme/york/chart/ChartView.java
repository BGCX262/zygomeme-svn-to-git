/*
 * Created on 14-Dec-2008
 *
 */
package com.zygomeme.york.chart;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.zygomeme.york.Identifiable;
import com.zygomeme.york.gui.YorkBrowser;
import com.zygomeme.york.gui.YorkEntityView;
import com.zygomeme.york.gui.NumberFormatter;
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
 * The view for the chart. Renders the chart on GUI.  
 * 
 */
public class ChartView extends YorkEntityView implements MouseMotionListener, Identifiable
{
	final static long serialVersionUID = 1245134251435L;
	private Logger logger = Logger.getLogger(ChartView.class);
	
	private ChartModel model = null;
	private int originX = 50;
	private int originY = 500;
	private int xOffset = 60;
	private int yOffset = 30;
	private int yTopOffset = 30;
	private int xRightOffset = 160;
	private int bottomY = 530;
	private int boxSize = 2;
	private int columnWidth = 10;
	private double yScale = 1.0;
	private double xScale = 1.0;
	private Color[] lineColours = null;
	private int valuesBoxWidth = 160;
	private int yAxisHeight;
	private int xAxisLength;
	
	private EntityViewConfig parentConfig;
	private boolean relativeToParent = true; 
	
	private Color textColor = Color.black;
	private NumberFormatter formatter = new NumberFormatter();

	private TextNugget debugNugget;
	private TextNugget valuesNugget;

	private LegendNugget legendNugget;

	private int highlightIndex = -1;
	private int maxDataPoints = 0;
	private Hashtable<String, Color> colorMap = new Hashtable<String, Color>();
	private Hashtable<String, SeriesStyle> seriesStyles = new Hashtable<String, SeriesStyle>();
	private TickUtil tickUtil = new TickUtil();
	
	private String idString;

	public enum SeriesStyle{BAR, LINE};

	public ChartView(EntityViewConfig parentConfigIn, YorkBrowser browserIn) {
		super(browserIn);
		this.parentConfig = parentConfigIn;
		this.identifiable = this;
		debugNugget = new TextNugget("Debug:", browserIn);
		valuesNugget = new TextNugget("Selected Values:", browserIn);
	}

	public void init() {

		lineColours = new Color[11];
		// Set up colours for lines
		lineColours[0] = Color.blue;
		lineColours[1] = Color.green;
		lineColours[2] = Color.orange;
		lineColours[3] = Color.magenta;
		lineColours[4] = Color.red;
		lineColours[5] = Color.pink;
		lineColours[6] = Color.gray;
		lineColours[7] = Color.black;
		lineColours[8] = Color.darkGray;
		lineColours[9] = Color.cyan;
		lineColours[10] = Color.yellow;
		

		setConfigRelativeToParent();
		debugNugget.setPositionAndWidth(originX + 210, originY - yAxisHeight - 200, 220);
		
		valuesNugget.setPositionAndWidth(config.getX() + config.getWidth() - valuesBoxWidth, 
				   config.getY() + 100, valuesBoxWidth);

		legendNugget = new LegendNugget(this, colorMap);	
		
		formatter.setScientificFormat("##0.0E0");
		formatter.setSwitchThreshold(1000000);
	}

	public String getIdString(){
		return idString;
	}
	
	public void setIdString(String idStringIn){
		this.idString = idStringIn;
	}
	
	public void setSeriesStyle(String name, SeriesStyle newStyle){
		seriesStyles.put(name, newStyle);
	}
	
	public void setRelativeToParent(Boolean relativeFlag){
		this.relativeToParent = relativeFlag;
	}

	public void paint(Graphics g) 
	{		
		logger.info("ChartView.paint()");
		if (model == null) {
			logger.info("Model is null in ChartView");
			return;
		}
		
		if(lineColours == null){
			init();
		}

		if (g == null) {
			logger.info("Graphics object is null");
			return;
		}

		// Anti-alias
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Set the yScale 
		calcGraphSettings();

		// Draw background
		drawBackground(g2d);

		String dataSetName;
		maxDataPoints = model.getMaxLength();
		logger.info("DataPoint count:" + maxDataPoints);
		int j, x, colorCounter = 0;  
		double y;
		Color lineColor = lineColours[colorCounter];
		colorMap.clear();
		Iterator<String> it = model.getDataSetNamesIterator();

		int dataSetIndex = 0; // used for spacing bars
		while(it.hasNext()){
			dataSetName = it.next();
			if(colorCounter < (lineColours.length - 1)){
				colorCounter++;
			}
			else{
				colorCounter = 0;
			}
			//logger.info("DatasetName is " + dataSetName);
			colorMap.put(dataSetName, lineColours[colorCounter]);
			SeriesStyle style = seriesStyles.get(dataSetName);
			g.setColor(lineColor);

			for(j = 0; j < maxDataPoints; j++){
				y = model.get(dataSetName, j);
				x = j;

				drawOffsetDataPoint(style, dataSetName, x, y, j, maxDataPoints, g2d, dataSetIndex);
			}
			dataSetIndex++;
		}

		// Draw Axis
		drawAxis(g2d);

		// Draw the highlighted point
		drawHightlightLine((Graphics2D)g, highlightIndex);

		// Draw the legend
		legendNugget.draw(g);
		
		// Draw debug Nuggets
		//debugNugget.draw(g);
		if(highlightIndex != -1){
			// Set the values and draw values box
			setCurrentValues(highlightIndex);		
			valuesNugget.draw(g);
		}

		// Anti-alias
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);	
	}

	private void drawBackground(Graphics2D g){
		
		g.setColor(config.getBackgroundColor());
		g.fillRect(config.getX(), config.getY(), config.getWidth(), config.getHeight());
	}
	
	private void setCurrentValues(int index){

		if(highlightIndex == -1){
			return;
		}
		
		Iterator<String> it = model.getDataSetNamesIterator();
		while(it.hasNext()){
			String name = it.next();
			valuesNugget.setValue(name, model.get(name, index));
		}

	}
	
	private void drawHightlightLine(Graphics2D graphics, int x){

		if(highlightIndex != -1){
			graphics.setColor(Color.lightGray);
			int xPos = (int) (originX + (x * xScale));
			graphics.drawLine(xPos, bottomY, xPos, bottomY - yAxisHeight);
		}

	}
    
	private void drawAxis(Graphics2D g)
    {
		if(model.getMaxLength() == 0){
			return; // No values to draw
		}
		
        if (g != null) {
            g.setColor(textColor);
                                    
            int topY = bottomY - yAxisHeight;
            int rightX = originX + xAxisLength;
            g.drawLine(originX, bottomY, originX, topY); 	// The Y axis
            g.drawLine(originX, originY, rightX, originY);	// The X axis
            
            // What is the steps to marked on the axis
            double markStepX = tickUtil.getAxisTickStepSize(model.getMaxLength(), false);
            double minimumPoint; 
            if(model.getMinValue() > 0.0){
            	minimumPoint = 0.0;            	
            }
            else{
            	minimumPoint = model.getMinValue();
            }
            double markStepY = tickUtil.getAxisTickStepSize(model.getMaxValue() - minimumPoint, true);
            debugNugget.setValue("minimumPoint: ", minimumPoint);
            debugNugget.setValue("model.getMaxValue(): ", model.getMaxValue());
            debugNugget.setValue("markStepY: ", markStepY);
            
            double pixelStepY = (bottomY - topY) / ((model.getMaxValue() - minimumPoint) / markStepY); 
            double pixelStepX = (rightX - originX) / (model.getMaxLength() / markStepX); 
            debugNugget.setValue("pixelStepY: ", pixelStepY);
            int intPixelStep;
            if((pixelStepY <= 1) || (pixelStepX <= 1)){
            	logger.debug("pixelStepY too small, so not drawing axis");
            	return;
            }
            
        	double intLabel = 0;
        	String tickLabel = "";
        	FontMetrics fontMetrics = g.getFontMetrics(g.getFont());

        	// X axis
            // The axis is actually going to be a reasonable size then draw it...
            if((rightX - originX) > 4 && pixelStepX > 4){
            	int tickLength = 10;
            	for(double x = 0; x <= (rightX - originX); x+= pixelStepX){
            		intPixelStep = (int)x;
            		g.drawLine(originX + intPixelStep, originY, originX + intPixelStep, originY + tickLength);
            		// Draw the label 
            		tickLabel = formatter.format(intLabel) + " ";
            		g.drawString(tickLabel, (originX + intPixelStep) - (fontMetrics.stringWidth(tickLabel) / 2), 
            				(originY + fontMetrics.getHeight() + tickLength));
            		intLabel += markStepX;
            	}
            }

            // Positive Y axis
            // The axis is actually going to be a reasonable size then draw it...
            if((originY - topY) > 4 && pixelStepY > 1){
            	Font previousFont = g.getFont();
            	// Set font size
            	if(pixelStepY < fontMetrics.getHeight()){
            		logger.info("Resizing font to :" + pixelStepX);
            		Font font = new Font(previousFont.getFamily(), Font.PLAIN, (int) pixelStepY); 
            		g.setFont(font);
            	}
            	intLabel = 0;
            	for(double y = 0; y <= (originY - topY); y+= pixelStepY){
            		intPixelStep = (int)y;
            		g.drawLine(originX, originY - intPixelStep, originX - 10, originY - intPixelStep);
            		// Draw the label 
            		tickLabel = formatter.format(intLabel) + " ";
            		g.drawString(tickLabel, originX - (fontMetrics.stringWidth(tickLabel) + 10), (originY - intPixelStep) + 5);
            		intLabel += markStepY;
            	}
            	g.setFont(previousFont);
            }

            // Negative Y axis
            if((bottomY - originY) > 4 && pixelStepY > 1){
            	intLabel = 0;
            	for(double y = 0; y <= (bottomY - originY); y+= pixelStepY){
            		intPixelStep = (int)y;
            		g.drawLine(originX, originY + intPixelStep, originX - 10, originY + intPixelStep);
            		// Draw the label 
            		tickLabel = formatter.format(intLabel) + " ";
            		g.drawString(tickLabel, originX - (fontMetrics.stringWidth(tickLabel) + 10), (originY + intPixelStep) + 5);
            		intLabel -= markStepY;
            	}
            }

       }
    }
        
	/** 
	 * Some of these values only need to be calculated once, or when the data points change.
	 * An optimisation would be to factor out the "once only" calculations into a separate method 
	 */
	private void calcGraphSettings(){

		setConfigRelativeToParent();
		
		originX = config.getX() + xOffset;
		originY = (config.getY() + config.getHeight()) - yOffset;
		bottomY = originY;
		xRightOffset = Math.min(config.getWidth() / 3, 160);
		xAxisLength = config.getWidth() - xOffset - xRightOffset;
				
		// First the Y Scale
		double max = model.getMaxValue();
		double min = model.getMinValue();
		if(min < 0){
			yAxisHeight = config.getHeight() - yTopOffset - yOffset;
			yScale = ((double) yAxisHeight) / (max - min);
			originY = bottomY + (int)(yScale * min);
		}
		else{
			yAxisHeight = config.getHeight() - yTopOffset - yOffset;
			yScale = ((double) yAxisHeight) / max;
		}

		// Now the X Scale
		xScale = (double) xAxisLength / (double) model.getMaxLength();

		// Calculate the width of the columns - used when drawing a column graph/chart
		if(model.getDataSetCount() > 0 && model.getMaxLength() > 0){
			columnWidth = (xAxisLength / model.getMaxLength()) / model.getDataSetCount();	
		} 		 
		else{
			columnWidth = 2;
		}
		if(columnWidth < 2){
			columnWidth = 2;
		}

		// Position the nuggets
		int legendX = originX + xAxisLength + 10;
		legendNugget.setPosition(legendX, config.getY() + 20);
		legendNugget.setWidth(xRightOffset - 20);
		legendNugget.setHeight(config.getHeight() - 40);
		
		valuesNugget.setPosition(originX + xAxisLength + 10, originY - (yAxisHeight / 2) - 35);
		debugNugget.setPosition(originX + 410, config.getY() - 300);

		debugNugget.setValue("yScale: ", yScale);

	}
	
	private void setConfigRelativeToParent(){
		
		if(config == null){
			config = new EntityViewConfig(parentConfig.getX(), 
					parentConfig.getY(), 
					parentConfig.getWidth(), 
					parentConfig.getHeight());
		}

		if(relativeToParent){
			config.setX(parentConfig.getX() + 20);
			config.setY(parentConfig.getY() + 140); 
			config.setWidth(parentConfig.getWidth() - 40); 
			config.setHeight(parentConfig.getHeight() -160);

			config.setBackgroundColor(new Color(255, 255, 255, 190));
		}
		else{		
			// This config should be the same as the parent - used for drawing the image to be
			// saved to file.
			config.setX(parentConfig.getX());
			config.setY(parentConfig.getY()); 
			config.setWidth(parentConfig.getWidth()); 
			config.setHeight(parentConfig.getHeight());
		}
	}

	private void drawOffsetRect(double x, double y, int size, Graphics graphics){
		int halfSize = size / 2;
		graphics.drawRect((int) ((x * xScale) + originX) - halfSize, 
				(int) (originY - (y * yScale)) - halfSize, 
				size, size);
	}

	private void drawOffsetDataPoint(SeriesStyle style, String dataSetName, int x1, double y1, int j, 
			int count, Graphics2D graphics, int dataSetIndex)
	{
		graphics.setColor(colorMap.get(dataSetName));

		// Could split this out into another class so it has polymorphic response to the draw method
		if(style == SeriesStyle.LINE){
			if(j < (count - 1)){
				double y2 = model.get(dataSetName, j + 1);
				int x2 = (j + 1);
				drawOffsetRect(x1, y2, boxSize, graphics);                                       
				drawScaledLine(x1, y1, x2, y2, graphics);
			}
		}
		else{
			drawScaledBox(x1, 0, columnWidth, y1,  graphics, dataSetIndex);
		}
	}

	private void drawScaledLine(int x1, double y1, int x2, double y2, Graphics2D graphics){

		drawSingleScaledLine(x1, y1, x2, y2, graphics);
	}

	private void drawSingleScaledLine(int x1, double y1, int x2, double y2, Graphics2D graphics){
		
		graphics.drawLine((int) ((x1 * xScale) + originX), 
				(int) (originY - (y1 * yScale)), 
				(int) ((x2 * xScale) + originX), 
				(int) (originY - (y2 * yScale)));

	}

	private void drawScaledBox(int x, double y, int w, double h, Graphics2D graphics, int dataSetIndex){

		int barH = ((int)Math.abs(h * yScale));
		if(h < 0){
			// Negative
			graphics.fillRect((int) ((x * xScale) + originX + (dataSetIndex * columnWidth)), 
					(int) (originY - (y * yScale)), 
					w - 1, barH);
			//debugNugget.setValue("yScale", yScale);
		}
		else{
			// Positive values
			graphics.fillRect((int) ((x * xScale) + originX + (dataSetIndex * columnWidth)), 
					(int) (originY - (y * yScale) - barH), 
					w - 1, barH);    		
		}
	}

	public void setModel(ChartModel newModel) {
		this.model = newModel;
	}

	public ChartModel getModel() {
		return model;
	}

	public void mouseDragged(MouseEvent e){}

	public void mouseMoved(MouseEvent e){

		// Calculate point
		int index = e.getX() - originX;
		index = (int)(((double)index) / xScale);


		if(index < -1){
			highlightIndex = -1;
		}
		else{
			if(index >= maxDataPoints){
				highlightIndex = maxDataPoints - 1;
			}
			else{
				highlightIndex = index;
			}
		}
		browser.repaint();
	}	
}

