package com.zygomeme.york;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zygomeme.york.gui.Arc;
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
 *
 * ArcMap used for keeping track of the arc in the models. Used mostly by the GUI 
 * elements. 
 * 
 */
public class ArcMap {

	private Logger logger = Logger.getLogger(ArcMap.class);
	
	private Map<String, List<Node>> fromMap = new HashMap<String, List<Node>>();
	
	// The arcs list is used for the rendering of the arcs in the GUI
	private List<Arc> arcs = new ArrayList<Arc>();
	private Map<Arc, Node> arcToNodeMap = new HashMap<Arc, Node>();
	private Map<Arc, Node> arcFromNodeMap = new HashMap<Arc, Node>();
		
	public void renameNode(String oldName, String newName){
	
		if(fromMap.get(oldName) != null){
			List<Node> nodeList = fromMap.get(oldName); 
			fromMap.remove(oldName);
			fromMap.put(newName, nodeList);
		}
	}
	
	public void remove(Arc arc){
		arcs.remove(arc);
		Node toNode = arcToNodeMap.get(arc);
		Node fromNode = arcFromNodeMap.get(arc);

		logger.info("Remove arc from: " + fromNode.getIdString() + " to:" + toNode.getIdString());

		fromMap.remove(arcFromNodeMap.get(arc).getIdString());
		arcToNodeMap.remove(arc);
		arcFromNodeMap.remove(arc);
	}
	
	public void add(YorkEntityView fromView, YorkEntityView toView){

		Node fromNode = fromView.getNode();
		Node toNode = toView.getNode();

		// Add the from Node mappings
		if(!fromMap.keySet().contains(toNode.getIdString())){
			fromMap.put(toNode.getIdString(), new ArrayList<Node>());
		}

		List<Node> currentFromNodes = fromMap.get(toNode.getIdString());
		currentFromNodes.add(fromNode);

		// Add the view aspect of the arc
		Arc arc = new Arc(fromView, toView);
		arcs.add(arc);
		arcToNodeMap.put(arc, toNode);
		arcFromNodeMap.put(arc, fromNode);
	}

	public void remove(YorkEntityView fromView, YorkEntityView toView){

		Node fromNode = fromView.getNode();
		Node toNode = toView.getNode();

		// Remove the from Node mappings
		fromMap.remove(fromNode.getIdString());
		logger.info("Remove arc from:" + fromNode.getIdString() + ", to:" + toNode.getIdString());

		List<Node> currentFromNodes = fromMap.get(toNode.getIdString());
		currentFromNodes.remove(fromNode);

		// Add the view aspect of the arc
		Arc doomedArc = new Arc(fromView, toView);
		for(Arc arc: arcs){
			if(arc.getFromConfig() == fromView.getConfig() && 
			   arc.getToConfig() == toView.getConfig()){
				doomedArc = arc;
			}
		}		
		logger.info("Arc found to remove:" + doomedArc);
		logger.info("Arc.get(0):" + arcs.get(0));
		if(doomedArc != null){
			arcs.remove(doomedArc);
			arcToNodeMap.remove(doomedArc);
			arcFromNodeMap.remove(doomedArc);
		}
	}

	public Map<String, List<Node>> getFromMap(){
		return fromMap;
	}
	
	public List<Arc> getArcs(){
		return arcs;
	}

	public Node getFromNode(Arc arc){
		return arcFromNodeMap.get(arc);
	}
	
	/**
	 * Returns true if the given arc is connected at either end, to the 
	 * given node. 
	 * 
	 * @param arc
	 * @param node
	 * @return true if connected, false otherwise
	 */
	public boolean isConnectedTo(Arc arc, Node node){
		
		if(arc.getToId().equals(node.getIdString())){
			return true;
		}

		if(arc.getFromId().equals(node.getIdString())){
			return true;
		}
		
		return false;
	}
}
