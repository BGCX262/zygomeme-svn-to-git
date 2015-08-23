package com.zygomeme.york.propertiesdialog;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;
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
 * Panel for the properties dialog. 
 * Extended to allow a mapping of String to components so that client code has an easy
 * time extracting the values set by the user.   
 * 
 */


public class MappedPanel extends JPanel{

	private static final long serialVersionUID = -6752463056273912886L;
	private Map<String, JComponent> mappedComponents = new LinkedHashMap<String, JComponent>();
	
	private Map<String, Object> resultsMap = new LinkedHashMap<String, Object>();
	
	public void addMappedComponent(JComponent comp, String key){
		add(comp);
		mappedComponents.put(key, comp);
	}

	public void mapComponent(JComponent comp, String key){
		mappedComponents.put(key, comp);
	}

	public Map<String, JComponent> getMappedComponents(){
		return mappedComponents;
	}

	public Map<String, Object> getResultsMap(){
		return resultsMap;
	}
	
	public void setResult(String key, Object value){
		resultsMap.put(key, value);
	}
}
