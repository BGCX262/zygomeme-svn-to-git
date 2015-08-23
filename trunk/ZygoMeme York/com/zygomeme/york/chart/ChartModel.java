package com.zygomeme.york.chart;

import java.util.Hashtable;
import java.util.Iterator;

/**
 *  * **********************************************************************
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
 * This is the model part of the MVC part of the chart. (BTW the model is a connected graph, so 
 * the avoid calling this a graph. The connected network is the graph).  
 * 
 */
public class ChartModel
{
	// Hashtable with name:dataSet mappings.
    private Hashtable<String, double[]> dataSets = new Hashtable<String, double[]>();
    private double minValue; 
    private double maxValue;
    
    public void setDataSet(Hashtable<String, double[]> newDataSet){
        dataSets = newDataSet;
        recalculateMaxAndMinValues();
    }
    
    public void addDataSet(String name, double[] values){
        dataSets.put(name, values);
        recalculateMaxAndMinValues();
    }
    
    public double get(String dataSetName, int index){
        return dataSets.get(dataSetName)[index];
    }
    
    public Iterator<String> getDataSetNamesIterator(){
        return dataSets.keySet().iterator();
    }

    public void recalculateMaxAndMinValues(){
    
        if(dataSets == null){
            return;
        }
        
        maxValue = Double.NEGATIVE_INFINITY;
        minValue = Double.POSITIVE_INFINITY;
        Iterator<double[]> it = dataSets.values().iterator();
        
        double[] dataset;
        while(it.hasNext()){
            dataset = it.next();
            for(int i = 0; i < dataset.length; i++){
                if(dataset[i] > maxValue){
                    maxValue = dataset[i];
                }
                if(dataset[i] < minValue){
                    minValue = dataset[i];
                }
            }
        }
    }
    
    /**
     * Returns the length of the longest data set. 
     * 
     * @return
     */
    public int getMaxLength(){

        if(dataSets == null){
            return 0;
        }
        
        int len = 0;
        Iterator<double[]> it = dataSets.values().iterator();
        
        double[] dataset;
        while(it.hasNext()){
            dataset = it.next();
            if(dataset.length > len){
                len = dataset.length;
            }
        }
        return len;
    }

    public double getMaxValue(){    	
    	return maxValue;
    }

    public double getMinValue(){
    	return minValue;
    }
    
    public int getDataSetCount(){
    	return dataSets.size();
    }

}
