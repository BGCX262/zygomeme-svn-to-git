package com.zygomeme.york.xml;

import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
 * Creates an XML element of a given modifier so that it can be included
 * in XML for the Dynamic Model file.  
 * 
 */
public class LinearPropagationModifierExporter extends StandardExporter{

	public Element getModifierElement(Document doc, Set<String> nodeIds){

		Element mods = doc.createElement("modifiers");
		for(String fromNodeId: nodeIds){
			Element modElement = doc.createElement("modifier");
			modElement.setAttribute("from", fromNodeId);
			mods.appendChild(modElement);
		}

		return mods;
	}

}
