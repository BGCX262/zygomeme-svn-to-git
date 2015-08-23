package com.zygomeme.york.gui.model;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.zygomeme.york.ArcMap;
import com.zygomeme.york.gui.Arc;
import com.zygomeme.york.gui.YorkEntityView;
import com.zygomeme.york.gui.PropertiesMemento;
import com.zygomeme.york.propertiesdialog.ColorGradientPanel;
import com.zygomeme.york.xml.ViewModelExporter;
import com.zygomeme.york.gui.EntityViewConfig;

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
 * This is the model part of a TabView  
 * 
 */

public class ViewModel {
	
	private Logger logger = Logger.getLogger(ViewModel.class);
	
	private String name;
	private Map<String, YorkEntityView> viewMap = new HashMap<String, YorkEntityView>();
	private int version = 0;
	private ArcMap arcMap = new ArcMap(); 
	private Color gradientColorTop = new Color(0, 0, 100);
	private Color gradientColorBottom = new Color(100, 100, 200);
	
	public ViewModel(){}
	
	public ViewModel(PropertiesMemento propertiesMementoIn){
		if(propertiesMementoIn.get(ColorGradientPanel.TOP_COLOR) != null){
			this.gradientColorTop = new Color(Integer.parseInt(propertiesMementoIn.get(ColorGradientPanel.TOP_COLOR).toString()));
		}
		if(propertiesMementoIn.get(ColorGradientPanel.BOTTOM_COLOR) != null){
			this.gradientColorBottom = new Color(Integer.parseInt(propertiesMementoIn.get(ColorGradientPanel.BOTTOM_COLOR).toString()));
		}
	}
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public void setName(String newName){
		this.name = newName;
	}
	public String getName(){
		return name;
	}
		
	public void addView(String name, YorkEntityView newView){
		logger.info("Adding " + name + " to viewModels");
		viewMap.put(name, newView);
	}

	public void addView(YorkEntityView newView){
		logger.info("Adding " + newView.getModelsIdString() + " to viewModels");
		viewMap.put(newView.getModelsIdString(), newView);
	}

	public void removeView(String name){
		logger.info("Removing " + name + " from viewModels");
		viewMap.remove(name);
	}

	public Set<String> getViewNames(){
		return viewMap.keySet();
	}
	
	public Collection<YorkEntityView> getViews(){
		return viewMap.values();
	}
	
	public YorkEntityView getView(String name){
		return viewMap.get(name);
	}
	
	public void setConfig(String name, EntityViewConfig newConfig){
		//configMap.put(name, newConfig);
		viewMap.get(name).setConfig(newConfig);
	}
	
	public EntityViewConfig getConfig(String name){
		return viewMap.get(name).getConfig();
	}
	
	public void save(PropertiesMemento propertiesMemento){
		ViewModelExporter exporter = new ViewModelExporter();
		exporter.save(this, propertiesMemento);
	}

	public void addArc(YorkEntityView fromView, YorkEntityView toView){
		arcMap.add(fromView, toView);
	}

	public void removeArc(YorkEntityView fromView, YorkEntityView toView){
		arcMap.remove(fromView, toView);
	}

	public ArcMap getArcMap(){
		return arcMap;
	}
	
	public List<Arc> getArcs(){
		return arcMap.getArcs();
	}
		
	public void setBackgroundGradientColorTop(Color newColor){
		gradientColorTop = newColor;
	}
	public void setBackgroundGradientColorBottom(Color newColor){
		gradientColorBottom = newColor;
	}
	
	public Color getBackgroundGradientColorTop(){
		return gradientColorTop;
	}

	public Color getBackgroundGradientColorBottom(){
		return gradientColorBottom;
	}
	
}
