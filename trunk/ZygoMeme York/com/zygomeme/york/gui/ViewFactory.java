package com.zygomeme.york.gui;

import javax.swing.JOptionPane;

import com.zygomeme.york.Node;
import com.zygomeme.york.dynamicmodels.DynamicModel;
import com.zygomeme.york.dynamicmodels.DynamicModelNode;
import com.zygomeme.york.xml.DynamicModelHandler;
import com.zygomeme.york.xml.HeadHandler;
import com.zygomeme.york.xml.StandardHandler;
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
 * Creates a view for a item loaded from an XML file. 
 * Useful when there were (when there will be) other file types. 
 * 
 */

public class ViewFactory {

	
    public static YorkEntityView getView(HeadHandler.FileType fileType, YorkBrowser browser, 
									StandardHandler handler, EntityViewConfig config){
		
		switch(fileType){
			case DYNAMIC_MODEL: DynamicModel dModel = (DynamicModel) ((DynamicModelHandler)handler).getModel();
								DynamicModelView modelView = new DynamicModelView(((DynamicModelHandler)handler).getConfig(), 
										dModel, 
										browser, 
										browser.getCurrentView());
								modelView.createNodeViews(((DynamicModelHandler)handler).getConfigMap());
								return modelView;
		}
		JOptionPane.showMessageDialog(browser, "Not able to load this type of file.", "File type not recognised", JOptionPane.WARNING_MESSAGE);
		return null;
		
	}

    public static YorkEntityView getView(Node node, YorkBrowser browser, EntityViewConfig config){

    	if(node instanceof DynamicModelNode){
    		return new DynamicModelNodeView(config, (DynamicModelNode)node, browser);
    	}
    	    	
    	throw new RuntimeException("No view for file type:\"" + node + "\"");
    }
    
}
