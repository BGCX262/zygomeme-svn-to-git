package com.zygomeme.york.propertiesdialog;

import javax.swing.tree.DefaultMutableTreeNode;

import com.zygomeme.york.gui.EntityViewConfig;
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
 * Dialog that presents the Node properties to the user so that they can 
 * set their preferences.  
 * 
 */

public class NodePropertiesDialog extends PropertiesDialog {

	private EntityViewConfig config;
	
	public NodePropertiesDialog(YorkBrowser browser, EntityViewConfig configIn){
		super(browser);
		this.config = configIn;
	}
		
	protected void createNodes(DefaultMutableTreeNode top) {

		DefaultMutableTreeNode category = null;		
		
		// Create the panels and place them in a map with a suitable, unique key
	    panelMap.put(INFO, new InfoPanel());
		panelMap.put(BORDER_COLOR, new ColorPanel("Set border color", "Border Color:", PropertiesDialog.BORDER_COLOR, config.getBorderColor()));
		panelMap.put(NODE_FONT, new FontPanel("Set the font", PropertiesDialog.NODE_FONT, config.getFont().getFamily()));
		panelMap.put(FONT_COLOR, new ColorPanel("Set the font color", "Font Color:", PropertiesDialog.FONT_COLOR, config.getFontColor()));
	    		
		// Node config
	    category = new DefaultMutableTreeNode(new PanelInfo(INFO, panelMap.get(INFO)));
	    top.add(category);

	    category.add(new DefaultMutableTreeNode(new PanelInfo(BORDER_COLOR, panelMap.get(BORDER_COLOR))));
	    category.add(new DefaultMutableTreeNode(new PanelInfo(NODE_FONT, panelMap.get(NODE_FONT))));
	    category.add(new DefaultMutableTreeNode(new PanelInfo(FONT_COLOR, panelMap.get(FONT_COLOR))));
	    
	}	
}
