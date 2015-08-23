package com.zygomeme.york.gui;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.zygomeme.york.gui.model.ViewModel;

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
 * Renders the background of each tab view.
 */

public class BackgroundRender {

	private ViewModel viewModel;
	
	public BackgroundRender(ViewModel viewModelIn){
		this.viewModel = viewModelIn;
	}
			
	public void renderBackground(Graphics graphics, int width, int height){
			
		Graphics2D g2d = (Graphics2D)graphics;
	        
        // A non-cyclic gradient
		GradientPaint gradient = new GradientPaint(0, 0, viewModel.getBackgroundGradientColorTop(), 
												   0, height, viewModel.getBackgroundGradientColorBottom());
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
        
	}
}
