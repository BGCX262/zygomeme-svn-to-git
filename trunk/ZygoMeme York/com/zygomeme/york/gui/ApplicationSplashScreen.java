package com.zygomeme.york.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JFrame;

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
* A splash screen for the application. The Java mechanism for splash screens
* doesn't not produce a satisfactory solution disappearing too quickly from the 
* screen. There appeared to be no way to extend the life span of the default
* splash screen. 
* 
*/ 

public class ApplicationSplashScreen extends JFrame {

	private static final long serialVersionUID = 1348706625102373141L;

	private Image splashImage;
	private int width;
	private int height;
	
	public ApplicationSplashScreen(){
		setUndecorated(true);
		setLocation(100, 100);
		setVisible(true);
	}
	
	public void setImage(Image image){
		this.splashImage = image;
		this.width = image.getWidth(null);
		this.height = image.getHeight(null);
		setPreferredSize(new Dimension(width, height));
		setMinimumSize(new Dimension(width, height));
	}
		
	/** 
	 * Simply draws the image to the graphics. I did experiment with repainting the image, but there were difficulties
	 * due to the transparency in the image. Repeated repaints meant that the image "builds up" on the Graphics rather 
	 * than being drawn afresh each time, efforts to clear the graphics layer (or image) failed - trying to overwrite
	 * a graphics with a transparency left the previous image(s) there and unaffected. 
	 */
	public void paint(Graphics g){

		((Graphics2D)g).drawImage(splashImage, 0, 0, null);
	}
}
