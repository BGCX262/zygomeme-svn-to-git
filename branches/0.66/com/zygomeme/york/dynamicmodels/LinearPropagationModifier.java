package com.zygomeme.york.dynamicmodels;

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
 * Modifiers are provided for models where the model required the effect of any change in the 
 * output of one node to be spread over a number of time points. Example if node2 take the output
 * of node1, and node1's output changes from 0 to 5. Then the effect of that change is spread 
 * over 10 (say) time points.  
 * 
 * !!!! NOTE !!!
 * Modifiers are no currently accessible to the user. There were enabled in an earlier version 
 * but were proving to be too much off a maintenance load, hence they were temporarily withdrawn
 * until user demand and programming resources justify their resurrection.
 * 
 */

public class LinearPropagationModifier implements PropagationSpeedModifier {

	private Logger logger = Logger.getLogger(LinearPropagationModifier.class);
	private int steps = 10;
	private int currentStep = 0;
	private double currentTarget = 0;
	private double currentValue = 0;
	private double delta = 0;
	private double startingValue = 0.0;
	
	public void setCurrentInput(double newCurrentValue){
		this.currentTarget = newCurrentValue;
	}
	
	public void reset(){
		currentStep = 0;
		currentValue = currentTarget = startingValue;
	}
	
	public double getStep(double input){
		
		if(currentStep == steps || delta == 0){
			delta = 0;
			currentStep = 0;
			return input;
		}
		currentStep++;
		currentValue = input - ((delta / steps) * ( steps - currentStep)); 
		logger.info("Modifier.getStep(" + input + ") modified to:" + currentValue);
		return currentValue;
	}
	
	// Record the current position and any delta
	public void recordInput(double input, int timePoint){
		
		if(input != currentTarget){
			delta = input - currentValue;
			currentStep = 0;
		}
		logger.info("input:" + input + ", delta:" + delta + 
				", currentValue:" + currentTarget + ", step:" + currentStep);
		currentTarget = input;
		
	}
}
