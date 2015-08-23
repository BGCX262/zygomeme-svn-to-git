package com.zygomeme.york.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import com.zygomeme.york.chart.RComponent;
import com.zygomeme.york.chart.RComponentListener;

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
 * Buttons that are used in the GUI.  
 * 
 */
public class GUIButton implements RComponent{

	private int x = 0; 
	private int y = 0;
	private boolean mouseOverFlag = false;

	private String text = "Button";
	private Image nestedGraphImage;

	private List<RComponentListener> listeners = new LinkedList<RComponentListener>();
	private String command = "";
	private EntityViewConfig parentConfig = null;

	public GUIButton(){}

	public GUIButton(String newText, int x, int y, int w, int h){
		this.text = newText;
		this.x = x; 
		this.y = y;
	}	

	public GUIButton(String imageFilename){
		this.nestedGraphImage = new ImageIcon(imageFilename).getImage();	
	}	

	public void setParentConfig(EntityViewConfig newParentConfig){
		this.parentConfig = newParentConfig;
	}
	
	public String getText(){
		return text;
	}

	public void draw(Graphics g){

		// Draw the nested graph button
		x = parentConfig.getX() + parentConfig.getWidth() - nestedGraphImage.getWidth(null);
		y = parentConfig.getY();
		g.drawImage(nestedGraphImage, x, y, null);
	}

	public void addListener(RComponentListener newListener){
		listeners.add(newListener);
	}

	public void actionEvent(){
		for(RComponentListener listener: listeners){
			listener.actionEvent(this);
		}
	}

	public void setCommand(String newCommand){
		this.command = newCommand;
	}

	public String getCommand(){
		return command;
	}

	public boolean isMouseOver(MouseEvent me){

		int topX = x + nestedGraphImage.getWidth(null);
		int topY = y + nestedGraphImage.getHeight(null);
		if(me.getX() > x && me.getX() < topX && me.getY() > y && me.getY() < topY){
			mouseOverFlag = true;
		}
		else{
			mouseOverFlag = false;
		}
		return mouseOverFlag;
	}
}
