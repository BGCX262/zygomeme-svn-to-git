package com.zygomeme.york.chart;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

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
 * Prepares the information for the ticks labels to be displayed on the chart. Should the increments
 * be in 10's or 20's etc., this class calculates which step size should be used. 
 * 
 * TODO Doesn't cope will very small numbers. The class would be better to calculate the steps on the
 * fly rather than compare against a map. The new algorithm should not have to loop or iterate to find 
 * the correct value. One (hacky?!) solution would be to parse the scientific version of the number to get
 * the step size. E.g. if the number is 0.8E3 then the step would be 100 that is (3 - 1) zeros.  
 * 
 */
public class TickUtil {

	private Map<Double, Double> tickMap = new LinkedHashMap<Double, Double>();
	private Logger logger = Logger.getLogger(TickUtil.class);	
	
	public TickUtil(){
		
		// Creates the step numbers for the axis. So if the maximum number is 67 then 
		// the axis should have ticks that increment in steps of ten. 
		double[] a = new double[]{1.0E-16, 2.0E-16, 5.0E-16};
		double[] b = new double[]{1.0E-17, 2.0E-17, 5.0E-17};
		for(int i = 0; i < 46; i++){
			for(int j = 0; j < 3; j++){
				tickMap.put(a[j], b[j]);
				// TODO possibility of accumulative error - revise algorithm ?! If you can think of a better way 
				a[j] = a[j] * 10;
				b[j] = b[j] * 10;
			}
		}
	}
	
    public double getAxisTickStepSize(double range, boolean fractionalTickAllowed){
    	
    	for(double threshold: tickMap.keySet()){
    		if(range <= threshold){
    			// For the X axis - discrete time points so fractional ticks are not allowed
    			if(!fractionalTickAllowed && tickMap.get(threshold) >= 1.0){
    				return tickMap.get(threshold);
    			}
    			
    			// The Y axis - fractional ticks are allowed
    			if(fractionalTickAllowed){
    				logger.info("Tick Y axis, range:" + range + " returning:" + tickMap.get(threshold));    				
    				return tickMap.get(threshold);    				
    			}
    		}
    	}
    			
    	// if not covered in the tickMap
		logger.info("Tick axis, range:" + range + " (NOT COVERED IN TICK MAP) returning:" + (range / 10.0));
    	return range / 10.0;
    }

}
