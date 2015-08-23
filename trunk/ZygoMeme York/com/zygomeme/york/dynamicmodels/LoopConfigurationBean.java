package com.zygomeme.york.dynamicmodels;


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
 * Stores the configuration information for the nested loops. For example
 * the value of node "rate" will be utilised in the range 1 to 7 in steps
 * of 0.5. For each node the bean will store the start value, the end value
 * and the step. Initially coded so that no restriction is placed on the numbers
 * (other then the step value must take the range from start to end). Hence
 * the start number can be greater than the end number (but the step value
 * must be negative). 
 * 
 */
public class LoopConfigurationBean {

	private String id;
	private double start; 
	private double stop; 
	private double step;
	
	public enum ParamType{START, STOP, STEP};
	
	public LoopConfigurationBean(String newId){	
		this.id = newId;
	}

	public void set(ParamType paramType, double value){
		
		switch(paramType){
			case START:{
				setStart(value);
				return;
			}
			case STOP:{
				setStop(value);
				return;
			}
			case STEP:{
				setStep(value);
				return;
			}
			default:throw new RuntimeException("No such paramType");
		}		
	}
	
	public void setStart(double start) {
		this.start = start;
	}

	public void setStop(double stop) {
		this.stop = stop;
	}

	public void setStep(double step) {
		this.step = step;
	}

	public double getStart() {
		return start;
	}

	public double getStop() {
		return stop;
	}

	public double getStep() {
		return step;
	}

	public String getId() {
		return id;
	}
	
}
