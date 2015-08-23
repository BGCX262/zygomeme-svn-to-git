package com.zygomeme.york.propertiesdialog;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import com.zygomeme.york.gui.ArcRenderer;
import com.zygomeme.york.gui.PropertiesMemento;
import com.zygomeme.york.gui.YorkBrowser;

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
 * Dialog for the Default properties (like default node color). 
 * 
 */
public class DefaultPropertiesDialog extends PropertiesDialog {

	
	public DefaultPropertiesDialog(YorkBrowser browser){
		super(browser);
	}
	
	protected void createNodes(DefaultMutableTreeNode top) {

		DefaultMutableTreeNode category = null;
	
		Color backgroundTopColor;
		if(propertiesMemento.get(ColorGradientPanel.TOP_COLOR) == null){
			backgroundTopColor = new Color(100, 100, 100);
			propertiesMemento.setProperty(ColorGradientPanel.TOP_COLOR, "" + backgroundTopColor.getRGB());
		}
		else{
			backgroundTopColor = new Color(Integer.parseInt(propertiesMemento.get(ColorGradientPanel.TOP_COLOR).toString()));
		}
		
		Color backgroundBottomColor;		
		if(propertiesMemento.get(ColorGradientPanel.BOTTOM_COLOR) == null){
			backgroundBottomColor = new Color(200, 200, 200);
			propertiesMemento.setProperty(ColorGradientPanel.BOTTOM_COLOR, "" + backgroundBottomColor.getRGB());
		}
		else{
			backgroundBottomColor = new Color(Integer.parseInt(propertiesMemento.get(ColorGradientPanel.BOTTOM_COLOR).toString()));
		}
		
		// Create the panels and place them in a map with a suitable, unique key
		panelMap.put(DIRECTORY, new InfoPanel());
		panelMap.put(TABS, new InfoPanel());
	    panelMap.put(DATA_DIR, new DirectorySelectionPanel(propertiesMemento.getProperty("dataDir"), 30, "Set the data directory", "Directory Name:", "dataDir"));
	    panelMap.put(DEFAULT_TAB_BACKGROUND_COLOR, new ColorGradientPanel("Set the default background color", "Top Color:", "dmNodeColour", backgroundTopColor, backgroundBottomColor));
	    
	    panelMap.put(DEFAULT_NODE_COLOR, new ColorPanel("Set the default node color", "Node Color:", "defaultNodeColor", propertiesMemento.getColor("defaultNodeColor")));
	    panelMap.put(DEFAULT_FONT_COLOR, new ColorPanel("Set the default font color", "Font Color:", PropertiesMemento.DEFAULT_FONT_COLOR, propertiesMemento.getColor(PropertiesMemento.DEFAULT_FONT_COLOR)));
	    panelMap.put(DEFAULT_FONT, new FontPanel("Set the default font", PropertiesMemento.DEFAULT_FONT, propertiesMemento.getProperty(PropertiesMemento.DEFAULT_FONT)));
	    panelMap.put(ARC_COLOR, new ColorPanel("Set the default arc color", "Arc Color:", "defaultArcColor", propertiesMemento.getColor("defaultArcColor")));
	    
	    HashMap<String, Object> buttonMap = new HashMap<String, Object>();
	    buttonMap.put("Curved", ArcRenderer.LineStyle.CUBIC);
	    buttonMap.put("Straight", ArcRenderer.LineStyle.STRAIGHT);
	    panelMap.put(ARC_STYLE, new RadioButtonPanel("Set the default arc color", PropertiesMemento.DEFAULT_ARC_STYLE, buttonMap));
	    		
	    // General
	    category = new DefaultMutableTreeNode(new PanelInfo(DIRECTORY, panelMap.get(DIRECTORY)));
	    top.add(category);
		category.add(new DefaultMutableTreeNode(new PanelInfo(DATA_DIR, panelMap.get(DATA_DIR))));		
	    
		// Tabs
	    category = new DefaultMutableTreeNode(new PanelInfo(TABS, panelMap.get(TABS)));
	    top.add(category);
	    category.add(new DefaultMutableTreeNode(new PanelInfo(DEFAULT_TAB_BACKGROUND_COLOR, panelMap.get(DEFAULT_TAB_BACKGROUND_COLOR))));	    
	    category.add(new DefaultMutableTreeNode(new PanelInfo(DEFAULT_NODE_COLOR, panelMap.get(DEFAULT_NODE_COLOR))));
	    category.add(new DefaultMutableTreeNode(new PanelInfo(DEFAULT_FONT_COLOR, panelMap.get(DEFAULT_FONT_COLOR))));
	    category.add(new DefaultMutableTreeNode(new PanelInfo(DEFAULT_FONT, panelMap.get(DEFAULT_FONT))));
	    category.add(new DefaultMutableTreeNode(new PanelInfo(ARC_COLOR, panelMap.get(ARC_COLOR))));
	    category.add(new DefaultMutableTreeNode(new PanelInfo(ARC_STYLE, panelMap.get(ARC_STYLE))));	    
	}
}
