package com.zygomeme.york.chart;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zygomeme.york.Identifiable;
import com.zygomeme.york.gui.EntityViewConfig;
import com.zygomeme.york.gui.YorkBrowser;
import com.zygomeme.york.gui.YorkEntityView;

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
 * Provides a text box on the GUI that can be updated as the program runs. 
 * Useful for debugging, and has also been used to show the values for
 * the chart when the mouse hovers over data points in the chart.   
 * 
 */

public class TextNugget extends YorkEntityView implements Identifiable{

	private Map<String, String> map = new Hashtable<String, String>();
	private List<String> contentList = new ArrayList<String>();
	private int maxListLength = 20; // Maximum length for the content list 
	private int x = 0; 
	private int y = 0;
	private int width = 10;
	private int height = 10;
	private int widthPad = 3;
	private String command = "";
	private boolean isVisible = true;
	private String idString = "TextNugget";
	
	public enum Mode {MAP, LIST};
	private Mode mode = Mode.MAP;
	
	private DecimalFormat df = new DecimalFormat("###,###,##0.###");
	
	public TextNugget(YorkBrowser browserIn){
		super(browserIn);
		this.identifiable = this;
		this.config = new EntityViewConfig(20, 20, 200, 200);
		config.setHasSaveButton(false);
	}

	public TextNugget(String name, YorkBrowser browserIn){
		this(browserIn);
		this.idString = name;
	}

	public String getIdString(){
		return idString;
	}
	
	public void setIdString(String idStringIn){
		idString = idStringIn;
	}
		
	public void setCommand(String newCommand){
		command = newCommand;		
	}

	public String getCommand(){
		return command;		
	}
	
	public void setIsVisible(boolean visibilityFlag){
		isVisible = visibilityFlag;
	}

	public boolean isMouseOver(MouseEvent me){

		int topX = x + width;
		int topY = y + height;
		if(me.getX() > x && me.getX() < topX && me.getY() > y && me.getY() < topY){
			return true;
		}
		else{
			return false;
		}

	}
	
	public void setMode(Mode newMode){
		mode = newMode;
	}
	
	public void setValue(String key, Object ob){
		setValue(key, ob.toString());
	}
	
	public void setValue(String key, String value){
		map.put(key, value);
	}

	public void setValue(String key, double dValue){
		setValue(key, df.format(dValue));
	}

	public void setMaxLength(int newLimit){
		maxListLength = newLimit;
	}
	
	public void add(String newValue){
		
		contentList.add(newValue);
		if(contentList.size() > maxListLength){
			contentList.remove(0);
		}
	}
	
	public void clear(){
		contentList.clear();
		map.clear();
	}
	
	public void setPositionAndWidth(int x, int y, int w){
		this.x = x;
		this.y = y;
		this.width = w;
	}

	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void setWidth(int newWidth){this.width = newWidth;}
	
	private String trunc(FontMetrics fontMetrics, String message){

		// Truncate until it fits inside the box
		String truncMessage = message;
		int messageWidth = fontMetrics.stringWidth(truncMessage);
		if(messageWidth < (width - (widthPad * 2))){
			return message;
		}
		while(messageWidth >= (width - (widthPad * 2)) && truncMessage.length() > 0){
			truncMessage = truncMessage.substring(0, truncMessage.length() - 1);
			messageWidth = fontMetrics.stringWidth(truncMessage + "... ");
		}		
		return truncMessage + "... ";
	}
	
	public void draw(Graphics g){
		super.paint(g);
		
		if(!isVisible){
			return;
		}
		
		FontMetrics metrics = g.getFontMetrics(); 
		int fontHeight = metrics.getHeight();
        int lineCount = 0;
        
		int maxLineCount = config.getHeight() / fontHeight;

		// Draw text
		lineCount++;
		g.drawString(getIdString(), config.getX() + widthPad, config.getY() + (lineCount * fontHeight));
		lineCount++;

        String key;
        g.setColor(Color.green);
        if(mode == Mode.MAP){
        	Iterator<String> it = map.keySet().iterator();
        	while(it.hasNext()){
        		key = it.next();
        		lineCount = wrap(g, fontHeight, trunc(metrics, key + ": " + map.get(key)), lineCount, maxLineCount, metrics);
        	}
        }
        else{
    		List<String> stuffToDraw = Collections.synchronizedList(contentList);
    		synchronized (stuffToDraw) {
    			for(String content: contentList){
    				g.drawString(trunc(metrics, content), x, y + (lineCount * fontHeight));
    				lineCount++;
    			}
    		}
        }
	}
}
