package com.zygomeme.york.xml;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.zygomeme.york.gui.model.ViewModel;

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
 * Loads and parses the viewModel from a XML file. 
 * 
 */

public class ViewModelHandler extends StandardHandler{
	
	private ViewModel viewModel = new ViewModel();
	private List<String> filenames = new ArrayList<String>(); 
	// TODO parsedOK is not set anywhere !! 
	private boolean parsedOK = true;
	private Exception exception = null;

	public Exception getException(){
		return exception;
	}
	
	public boolean parsedOK(){
		return parsedOK;
	}

	public ViewModel getViewModel(){
		return viewModel;
	}
	
	public List<String> getFilenames(){
		return filenames;
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) 
		throws SAXException
	{
		
		if ("model_view".equals(qName)) {
			viewModel.setName(atts.getValue("name"));
			viewModel.setVersion(Integer.parseInt(atts.getValue("version")));
		}

		// In future the layout is saved with the model. Mixes the view and the model in one file
		// but in some ways better than having information about a single view in two separate files.
		// Otherwise you have to load the config before the view from the tab file, then load the 
		// view (only then finding out what type of view it is) and then set the config in the view. 
		if ("view".equals(qName)) {
			filenames.add(atts.getValue("filename"));
			viewModel.addView(atts.getValue("name"), null);
			return;
		}		
		
		if ("background_color_top".equals(qName)) {
			Color color = new Color(Integer.parseInt(atts.getValue("red")), 
									Integer.parseInt(atts.getValue("green")), 
									Integer.parseInt(atts.getValue("blue")));
			viewModel.setBackgroundGradientColorTop(color);
			return;
		}

		if ("background_color_bottom".equals(qName)) {
			Color color = new Color(Integer.parseInt(atts.getValue("red")), 
									Integer.parseInt(atts.getValue("green")), 
									Integer.parseInt(atts.getValue("blue")));
			viewModel.setBackgroundGradientColorBottom(color);
			return;
		}

	}
}
