package com.zygomeme.york.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
 * Creates an XML element of the given Nugget. 
 *
 */
public class NuggetElementCreator {

	public static Element createElement(Document doc, EntityViewConfig config, String elementName, String idString){

		// Create the Nugget XML
		Element element = doc.createElement(elementName);
		
		element.setAttribute("idString", idString);
		element.setAttribute("x", "" + config.getX());
		element.setAttribute("y", "" + config.getY());
		element.setAttribute("width", "" + config.getWidth());
		element.setAttribute("height", "" + config.getHeight());
		element.setAttribute("font_name", "" + config.getFont().getFamily());
		
		// Font color
		Element fontColourElement = doc.createElement("font_color");
		fontColourElement.setAttribute("red", config.getFontColor().getRed() + "");
		fontColourElement.setAttribute("green", config.getFontColor().getGreen() + "");
		fontColourElement.setAttribute("blue", config.getFontColor().getBlue() + "");
		element.appendChild(fontColourElement);			
		
		// Node color
		Element boarderColourElement = doc.createElement("border_color");
		boarderColourElement.setAttribute("red", config.getBorderColor().getRed() + "");
		boarderColourElement.setAttribute("green", config.getBorderColor().getGreen() + "");
		boarderColourElement.setAttribute("blue", config.getBorderColor().getBlue() + "");
		element.appendChild(boarderColourElement);			

		return element;
	}
}
