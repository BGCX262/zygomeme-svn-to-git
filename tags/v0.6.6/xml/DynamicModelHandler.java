package com.zygomeme.york.xml;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.zygomeme.york.Node;
import com.zygomeme.york.dynamicmodels.DynamicModel;
import com.zygomeme.york.dynamicmodels.DynamicModelNode;
import com.zygomeme.york.dynamicmodels.LinearPropagationModifier;
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
 * Class to import the Dynamic Model from an XML file. 
 * 
 */

public class DynamicModelHandler extends StandardHandler{

	private Logger logger = Logger.getLogger(DynamicModelHandler.class);
	private DynamicModel model;
	private Node dNode;
	private Map<String, EntityViewConfig> configMap = new HashMap<String, EntityViewConfig>();
	private EntityViewConfig config;
	private String currentNodeId;
	private boolean inModelConfig = false;

	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) 
	throws SAXException
	{

		if ("dynamic_model".equals(qName)) {
			model = new DynamicModel(atts.getValue("idString"));
			if(atts.getValue("end_time") != null){
				model.setEndTime(Integer.parseInt(atts.getValue("end_time")));
			}
			return;
		}

		if ("dynamic_node".equals(qName)) {
			dNode = new DynamicModelNode(atts.getValue("idString"));
			model.addNode(dNode);
			return;
		}
		
		if("modifier".equals(qName)){
			((DynamicModelNode)dNode).addModifier(atts.getValue("from"), new LinearPropagationModifier());
		}

		if ("start_node".equals(qName)) {
			model.addStartNode(atts.getValue("idString"));
			return;
		}

		if ("input".equals(qName)) {
			if(dNode instanceof DynamicModelNode){
				((DynamicModelNode)dNode).addInputPlaceholder(atts.getValue("idString"));
			}
			return;
		}

		if ("output".equals(qName)) {
			if(dNode instanceof DynamicModelNode){
				((DynamicModelNode)dNode).addOutputPlaceholder(atts.getValue("idString"));
			}
			return;
		}
		
		if ("values".equals(qName)) {
			
			if(dNode instanceof DynamicModelNode){
				((DynamicModelNode)dNode).setInitialValue(Double.parseDouble(atts.getValue("initial_value")));
				String equationString = atts.getValue("equation");
				if(equationString != null && equationString.length() > 0){
					((DynamicModelNode)dNode).setExpression(equationString);
				}
				else{
					((DynamicModelNode)dNode).setExpression(null);
				}
			}
			return;
		}
		
		if ("node_layout".equals(qName)) {
			int x = Integer.parseInt(atts.getValue("x"));
			int y = Integer.parseInt(atts.getValue("y"));
			int width = Integer.parseInt(atts.getValue("width"));
			int height = Integer.parseInt(atts.getValue("height"));
			currentNodeId = atts.getValue("idString");
			EntityViewConfig nodeConfig = new EntityViewConfig(x, y, width, height, EntityViewConfig.Type.DYNAMIC_NODE);
			
			// Font family
			String fontName = atts.getValue("font_name");
			nodeConfig.setFont(fontName);
			
			configMap.put(currentNodeId, nodeConfig);	
			return;
		}		

		if("model_layout".equals(qName)){
			inModelConfig = true;
		}
		
		// Font colour
		if("font_color".equals(qName)){
			if(currentNodeId != null){
				configMap.get(currentNodeId).setFontColor(parseColor(atts));
			}
			if(inModelConfig){
				config.setFontColor(parseColor(atts));
			}
		}
		
		// Border colour
		if("border_color".equals(qName)){
			if(currentNodeId != null){
				configMap.get(currentNodeId).setBorderColor(parseColor(atts));
			}
			if(inModelConfig){
				config.setBorderColor(parseColor(atts));
			}
		}

		if ("model_layout".equals(qName)) {
			int x = Integer.parseInt(atts.getValue("x"));
			int y = Integer.parseInt(atts.getValue("y"));
			int width = Integer.parseInt(atts.getValue("width"));
			int height = Integer.parseInt(atts.getValue("height"));
			config = new EntityViewConfig(x, y, width, height, EntityViewConfig.Type.DYNAMIC_MODEL);
			config.setType(EntityViewConfig.Type.DYNAMIC_MODEL);
			return;
		}		

	}
	
	private Color parseColor(Attributes atts){
		return new Color(Integer.parseInt(atts.getValue("red")), 
				Integer.parseInt(atts.getValue("green")), 
				Integer.parseInt(atts.getValue("blue")));
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
    throws SAXException 
	{
		if ("dynamic_model".equals(qName)) {
			
			// Replace the null place holders with real nodes
			for(String nodeId: model.getNodeIds()){
				Node node = model.getNode(nodeId);
				for(String fromNodeId: node.getFromNodeIds()){
					logger.info("Replacing placeholder with:" + model.getNode(fromNodeId));
					node.addInput(model.getNode(fromNodeId));
				}				
			}

			for(String nodeId: model.getNodeIds()){
				Node dyNode = model.getNode(nodeId);
				for(String toNodeId: dyNode.getToNodeIds()){
					logger.info("Replacing placeholder with:" + model.getNode(toNodeId));
					dyNode.addOutput(model.getNode(toNodeId));
				}				
			}
			
			return;
		}
		
		if("node_layout".equals(qName)) {
			currentNodeId = null;
		}
		if("model_layout".equals(qName)){
			inModelConfig = false;
		}
	}
	
	public DynamicModel getModel(){
		return model;
	}
	
	public Map<String, EntityViewConfig> getConfigMap(){
		return configMap;
	}

	public EntityViewConfig getConfig(){
		return config;
	}

}
