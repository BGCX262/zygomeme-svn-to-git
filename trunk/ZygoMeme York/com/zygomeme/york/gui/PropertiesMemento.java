package com.zygomeme.york.gui;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

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
 *
 * Used to store all application wide information on directories and the like. For example
 * where the models are stored and where the images are to saved.     
 * 
 */

public class PropertiesMemento extends Properties{

	final static long serialVersionUID = 124513423353466L;
	Logger logger = Logger.getLogger(PropertiesMemento.class);
	
	public final static int APP_VERSION_MAJOR = 0;
	public final static int APP_VERSION_MINOR = 5;
	public final static int APP_VERSION_BUILD = 3;
	public static int APP_VERSION_NUMBER;
	public final static String APP_VERSION_STRING = "" + APP_VERSION_MAJOR + "." + APP_VERSION_MINOR + "." + APP_VERSION_BUILD;
	public final static String APP_STAGE = "beta";
	
	public final static String DEFAULT_NODE_COLOR = "defaultNodeColor";
	public final static String DEFAULT_FONT_COLOR = "defaultFontColor";
	public final static String DEFAULT_ARC_STYLE = "defaultArcStyle";
	public final static String DEFAULT_FONT = "defaultFont";
	public final static String UPDATE_STRING = "time_of_last_check_for_updates";
		
	private static String baseDir = "C:/Program Files/ZygoMeme/";

	// TODO Check that this still works after the user has installed in another directory
	// has the simple appending in the constructor might cause problems as any spaces 
	// will not be escaped.
    public static String help_dir_url = "C:/Program%20Files/ZygoMeme/doc/";

	private static String dataDir = baseDir + "data" + File.separator; // Default setting

    private static String appDir = baseDir + "application_resources" + File.separator;
    private static String imageDir = appDir + "images" + File.separator;
    
    public static String TAB_DIR = dataDir + "tabs" + File.separator;
    public static String GRAPHS_DIR = dataDir + "simple graphs" + File.separator;
    public static String MODEL_PATTERNS_DIR = dataDir + "model patterns" + File.separator;
    public static String DYNAMIC_MODELS_DIR = dataDir + "dynamic models" + File.separator;
    public static String REPORT_DIR = dataDir + "reports" + File.separator;
	
	private List<String> tabNames = new ArrayList<String>();
	
	public PropertiesMemento(){
		
		// Uncommment the below line when not running in eclipse - Hacky is there a better way?!
		baseDir = System.getProperty("user.dir") + File.separator;
		System.out.println("baseDir:" + baseDir);
		
		resetDirectories();

		APP_VERSION_NUMBER = (APP_VERSION_MAJOR * 10000) + (APP_VERSION_MINOR * 100) + APP_VERSION_BUILD;
		appDir = baseDir + "application_resources" + File.separator;
	    imageDir = appDir + "images" + File.separator;

	    // help_dir_url is used by the default web browser to view the help files & licence
	    // so has to be escaped/encoded.
	    try{
		    URL url = new URL(baseDir + "doc" + File.separator);
	    	help_dir_url = url.toURI().toASCIIString();
	    }
	    catch(URISyntaxException e){
	    	// ?!
	    }
	    catch(MalformedURLException e){
	    	//?!
	    }
	}
	
	/**
	 * Reset directories when the root data directory has changed.
	 */
	public void resetDirectories(){
		dataDir = getProperty("dataDir");
		TAB_DIR = dataDir + "tabs" + File.separator;
		GRAPHS_DIR = dataDir + "simple graphs" + File.separator;
		MODEL_PATTERNS_DIR = dataDir + "model patterns" + File.separator;
		DYNAMIC_MODELS_DIR = dataDir + "dynamic models" + File.separator;
		REPORT_DIR = dataDir + "reports" + File.separator;
	}
	
	public String getDir(EntityViewConfig.Type type){
			
		switch(type){
		case DYNAMIC_MODEL: return DYNAMIC_MODELS_DIR;
		default: return baseDir;
		}
	}
	
	public void addTabRecord(String tabName){
		tabNames.add(tabName);
	}

	public void removeTabRecordAtIndex(int index){
		tabNames.remove(index);
	}	

	public void renameTab(String previousName, String newName){
	
		int index = 0;
		for(String tabName: tabNames){
			if(previousName.equals(tabName)){
				tabNames.set(index, newName);
			}
			index++;
		}
	}
	
	public List<String> getTabNames(){
		return tabNames;
	}
	
	public void load() throws Exception{
		
		try {
			load(new FileInputStream(appDir + "appProp.txt"));
			resetDirectories();
		} catch (Exception e) {
			System.out.println("Exception: Can't load the properties file \"" + appDir + "appProp.txt\"");
			throw e;
		}		
	}
	
	public int getInt(String name){
		return Integer.parseInt((String)get(name));
	}

	public Color getColor(String name){
		if(get(name) == null){
			logger.warn("Request for color that is not present in the PropertiesMemento", new RuntimeException());
			return Color.WHITE;
		}
		else{
			return new Color(Integer.parseInt(get(name).toString()));
		}
	}
	
	public String getDataDir(){
		
		if(getProperty("dataDir") == null){
			setProperty("dataDir", getBaseDir() + "data"  + File.separator);
			resetDirectories();
		}
		return getProperty("dataDir");
	}
	
	public String getBaseDir(){
		return baseDir;
	}	
	
	public String getTabDir(){
		return TAB_DIR;
	}

	public String getAppDir(){
		return appDir;
	}

	public String getImageDir(){
		return imageDir;
	}


	public void saveProperties(YorkBrowser browser) throws IOException{
				
		// Clear the recorded tab names
		int index = 0;
		boolean recordFound = true;
		do{
			if(get("tab" + index) != null){
				remove("tab" + index);
				index++;
			}
			else{
				recordFound = false;
			}
		}
		while(recordFound);
		
		// Add those present in the tab name list
		index = 0;
		for(String tabName: tabNames){
			put("tab" + index, TAB_DIR + tabName + ".xml");
			index++;
		}
					
		setProperty("width", browser.getWidth() + "");
        setProperty("height", browser.getHeight() + "");
        setProperty("x", browser.getX() + "");
        setProperty("y", browser.getY() + "");
        
        setProperty("dataDir", dataDir);
        try{
            store(new FileOutputStream(appDir + "appProp.txt"), "York Browser Properties");
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            throw ioe;
        }
	}
	
	public void setScreenLocationProperties(YorkBrowser browser){
		
		if(browser != null){
			browser.setWidth(getInt("width"));
			browser.setHeight(getInt("height"));
			browser.setX(getInt("x"));
			browser.setY(getInt("y"));
		}
	}
}
