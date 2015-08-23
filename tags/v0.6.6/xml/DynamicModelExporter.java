package com.zygomeme.york.xml;

import java.io.File;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.zygomeme.york.Node;
import com.zygomeme.york.dynamicmodels.DynamicModel;
import com.zygomeme.york.gui.DynamicModelView;
import com.zygomeme.york.gui.YorkEntityView;

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
 * Class to export the Dynamic Model to an XML file. 
 * 
 */

public class DynamicModelExporter {

	private Logger logger = Logger.getLogger(DynamicModelExporter.class);
	
	public void save(DynamicModelView view, String filename, Map<Node, YorkEntityView> nodeViewMap){

		logger.info("Saving model");
		DynamicModel model = view.getModel();
		
		Document doc;
		try {

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = builder.newDocument();

			doc = getDoc(view, doc, model, nodeViewMap);
			Source source = new DOMSource(doc);

			// Prepare the output file
			Result result = new StreamResult(new File(filename));

			// Write the document to the file
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}		
	}

	public Document getDoc(DynamicModelView view, Document doc, DynamicModel model, Map<Node, YorkEntityView> nodeViewMap){

		// Prepare the DOM document for writing
		Element head = doc.createElement("dynamic_model");
		head.setAttribute("idString", model.getIdString());
		head.setAttribute("end_time", "" + model.getEndTime());

		// Save the start nodes
		Element startNodes = doc.createElement("start_nodes");
		Element startElement = doc.createElement("start_node");
		for(String startNode: model.getStartNodes()){
			startElement = doc.createElement("start_node");
			startElement.setAttribute("idString", startNode);
			startNodes.appendChild(startElement);
		}
		head.appendChild(startNodes);
		
		// Save the node info
		Element dynamicNodes = doc.createElement("dynamic_nodes");
		//DynamicModelNodeExporter dynamicNodeExporter = new DynamicModelNodeExporter();
		//SimpleGraphExporter simpleGraphExporter = new SimpleGraphExporter();
		
		for(String idString: model.getNodeIds()){

			// Include the nodes details - run the DynamicNodeExporter
			Node node = model.getNode(idString);
			StandardExporter exporter = ExporterFactory.getExporter(node);

			Element nodeElement = exporter.appendNode(doc, node);

			dynamicNodes.appendChild(nodeElement);

		}

		head.appendChild(dynamicNodes);
		
		// Now write the view part of the network. 
		// Ideally there would be a way to cleanly separate the view and the model into separate
		// artifacts, but using different files for each leads to a doubling of files, synch issues, 
		// naming problems, - lots of nastiness that I've had to work with for years. Hence the two 
		// are kept in one file. 
		
		// First the model
		Element layoutDetails = doc.createElement("layout");
		Element dModelElement = NuggetElementCreator.createElement(doc, view.getConfig(), "model_layout", model.getIdString());
		layoutDetails.appendChild(dModelElement);
		
		// Now the nodes
		for(String idString: model.getNodeIds()){
			Element dNodeElement = NuggetElementCreator.createElement(doc, nodeViewMap.get(model.getNode(idString)).getConfig(), "node_layout", idString);
			layoutDetails.appendChild(dNodeElement);
		}
		head.appendChild(layoutDetails);		
		
		doc.appendChild(head);
		return doc;
	}

}
