package com.zygomeme.york.gui;

import java.awt.Color;
import java.awt.Font;

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
 * Use to store the config for entity view on the GUI.
 * 
 * Includes the configuration information:- x, and y position, width, height...   
 * 
 */
public class EntityViewConfig {

	private int x = 10;
	private int y = 10;
	private int width = 140;
	private int height = 100;
	
	private int minWidth = 140; 
	private int minHeight = 70;
	
	private Color textColor = null;
	private Color backgroundColor = new Color(0, 60, 0, 170);
	private Color borderColor = null;
	private boolean hasSaveButton = false;
	private boolean hasCloseButton = true;
	private Font font = new Font("Arial", Font.PLAIN, 12);
	
	public enum Type{UNKNOWN, DYNAMIC_NODE, DYNAMIC_MODEL};
	private Type type = Type.UNKNOWN;

	public EntityViewConfig(int newX, int newY, int newWidth, int newHeight, Type newType){
		this(newX, newY, newWidth, newHeight);
		setType(newType);
	}
	
	public EntityViewConfig(int newX, int newY, int newWidth, int newHeight){
		this.x = newX;
		this.y = newY;
		this.width = newWidth;
		this.height = newHeight;
		this.type = Type.UNKNOWN; 
	}
	
	public Type getType(){
		return type;
	}
	public void setType(Type newType){
		this.type = newType;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public int getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(int minWidth) {
		this.minWidth = minWidth;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		this.backgroundColor = new Color(borderColor.getRed() / 10, borderColor.getGreen() / 10, borderColor.getBlue() / 10, 170);
	}

	public Color getFontColor() {
		return textColor;
	}

	public void setFontColor(Color textColor) {
		this.textColor = textColor;
	}	
	
	public boolean hasSaveButton(){
		return hasSaveButton;
	}
	
	public void setHasSaveButton(boolean newValue){
		hasSaveButton = newValue;
	}
	
	public boolean hasCloseButton(){
		return hasCloseButton;
	}
	
	public void setHasCloseButton(boolean newValue){
		hasCloseButton = newValue;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(String fontName) {
		this.font = new Font(fontName, Font.PLAIN, 12);
	}	
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();

		buffer.append("[x:").append(x).append(", ");
		buffer.append("y:").append(y).append(", ");
		buffer.append("width:").append(width).append(", ");
		buffer.append("height:").append(height).append(", ");
		buffer.append("minWidth:").append(minWidth).append(", ");
		buffer.append("minHeight:").append(minHeight).append(", ");
		buffer.append("textColor:").append(textColor).append(", ");
		buffer.append("backgroundColor:").append(backgroundColor).append(", ");
		buffer.append("borderColor:").append(borderColor).append(", ");
		buffer.append("hasCloseButton:").append(hasCloseButton).append(", ");
		buffer.append("hasSaveButton:").append(hasSaveButton).append(", ");
		buffer.append("type:").append(type).append("]");
		
		return buffer.toString();
	}

}
