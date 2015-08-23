package com.zygomeme.york.propertiesdialog;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

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
 * Shows a colour gradient in a panel.  
 * 
 */
public class PreviewPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private Color currentTopColor;
	private Color currentBottomColor;
	private int previewBoxSize = 200;
	
	public PreviewPanel(int boxSize){
		previewBoxSize = boxSize;
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
		GradientPaint gradient = new GradientPaint(0, 0, currentTopColor, 0, previewBoxSize, currentBottomColor);
		((Graphics2D)g).setPaint(gradient);
		g.fillRect(0, 0, previewBoxSize, previewBoxSize);
	}


	public void setCurrentBottomColor(Color currentBottomColor) {
		this.currentBottomColor = currentBottomColor;
	}

	public void setCurrentTopColor(Color currentTopColor) {
		this.currentTopColor = currentTopColor;
	}

}
