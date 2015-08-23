package com.zygomeme.york.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.zygomeme.york.Node;
import com.zygomeme.york.dynamicmodels.DynamicModelNode;

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
 * Class to export a Dynamic Model Node to an XML file. 
 * (This is done indirectly, with the node details stored in the
 * Dynamic Model XML file)
 * 
 */

public class DynamicModelNodeExporter extends StandardExporter{

	public Element appendNode(Document doc, Node node){

		DynamicModelNode dNode = (DynamicModelNode) node;
		
		Element dNodeElement = doc.createElement("dynamic_node");
		dNodeElement.setAttribute("idString", dNode.getIdString());
		
		Element inputs = doc.createElement("inputs");

		for(Node iNode: dNode.getInputNodes()){

			Element inputElement = doc.createElement("input");
			inputElement.setAttribute("idString", iNode.getIdString());

			inputs.appendChild(inputElement);
		}
		dNodeElement.appendChild(inputs);

		Element outputs = doc.createElement("outputs");

		for(Node oNode: dNode.getOutputNodes()){

			Element outputElement = doc.createElement("output");
			outputElement.setAttribute("idString", oNode.getIdString());

			outputs.appendChild(outputElement);
		}
		dNodeElement.appendChild(outputs);

		Element valuesElement = doc.createElement("values");
		valuesElement.setAttribute("initial_value", "" + dNode.getInitialValue());
		if(dNode.getExpression() != null){
			valuesElement.setAttribute("equation", "" + dNode.getExpression());
		}
		else{
			valuesElement.setAttribute("equation", "");
		}
		dNodeElement.appendChild(valuesElement);

		// Modifiers
		LinearPropagationModifierExporter modExporter = new LinearPropagationModifierExporter();
		dNodeElement.appendChild(modExporter.getModifierElement(doc, dNode.getModifiers().keySet()));
		
		return dNodeElement;
	}
}
