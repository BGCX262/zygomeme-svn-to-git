package com.zygomeme.york.gui;

import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.zygomeme.york.util.io.GenericFileFilter;

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
 */

public class YorkGUIUtils {

	private boolean wasCancelled = false;
	
	public String getUserToDefineFilename(Frame frame, String dirName, String extensions, String title, int dialogType){
		
		JFileChooser fc = new JFileChooser(dirName);
		fc.setFileFilter(new GenericFileFilter(extensions, title));
		
		int returnVal;
		if(dialogType == JFileChooser.OPEN_DIALOG){
			returnVal = fc.showOpenDialog(frame);
		}
		else{
			returnVal = fc.showSaveDialog(frame);			
		}
		
		if(returnVal == JFileChooser.CANCEL_OPTION){
			wasCancelled = true;
			return null;
		}
		
		String selectedFilename;
		if(returnVal == JFileChooser.APPROVE_OPTION && fc.getSelectedFile() != null) {
			selectedFilename = fc.getSelectedFile().getAbsolutePath();
		}
		else{
			return null;
		}
		
		String preferedExtension = extensions.split(" ")[0];
		if(!selectedFilename.toLowerCase().endsWith("." + preferedExtension)){
			selectedFilename += "." + preferedExtension;
		}
		return selectedFilename;
	}
	
	public static void saveBufferedImage(BufferedImage bufferedImage, String filename){

        try {
            ImageIO.write(bufferedImage, "png", new File(filename));
        }
        catch (IOException e) {
            e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to save image to:" + filename, "Error", JOptionPane.ERROR_MESSAGE);
        }    
	}
	
	public boolean wasCancelled(){
		return wasCancelled;
	}
}
