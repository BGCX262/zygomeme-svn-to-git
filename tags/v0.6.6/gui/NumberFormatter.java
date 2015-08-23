package com.zygomeme.york.gui;

import java.text.DecimalFormat;

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
 * Application wide number formatter.    
 * 
 */

public class NumberFormatter {
	
	private DecimalFormat standardFormat = new DecimalFormat("#,###,###,##0.##"); 
	private DecimalFormat scientificFormat = new DecimalFormat("##0.###E0"); 
	private double switchThreshold = 1000000000; // Threshold above which the number will be displayed in scientific notation
	
	public void setScientificFormat(String newFormat){
		scientificFormat = new DecimalFormat(newFormat);
		scientificFormat.setMinimumFractionDigits(0);  // To avoid the showing 5.0E-6 and 12E-6 
	}

	public void setStandardFormat(String newFormat){
		standardFormat = new DecimalFormat(newFormat);
	}

	public void setSwitchThreshold(double newThreshold){
		switchThreshold = newThreshold;
	}
	
	public String format(String string){
		
		// Format the label using the decimal formatter if it is a number
		try {
			string = format(Double.parseDouble(string));
		} catch (NumberFormatException e) {
			// It's OK to get an exception, the label will be returned 
			// without any changes because it doesn't parse as a number and
			// therefore shouldn't be formatted as a number
		}
		return string;
	}
	
	public String format(double d){

		if(d == 0.0){
			return "0.0";
		}
		
		if( Math.abs(d) > switchThreshold || Math.abs(d) < 0.01){
			return scientificFormat.format(d);
		}
		else{
			return standardFormat.format(d);
		}
	}

}
