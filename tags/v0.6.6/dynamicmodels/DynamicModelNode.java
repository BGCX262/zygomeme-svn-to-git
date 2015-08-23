package com.zygomeme.york.dynamicmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zygomeme.york.Node;

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
 * DynamicModelNode the nodes of the Dynamic Model. 
 * Keeps track of the other nodes it is connected to, its attributes and values, the list
 * of modifiers. 
 * This class should function independent of the GUI.  
 * 
 */
public class DynamicModelNode implements Node {

	private Logger logger = Logger.getLogger(DynamicModelNode.class);
	private String idString = "";
	private Map<String, Node> inputs = new HashMap<String, Node>();
	private Map<String, Node> outputs = new HashMap<String, Node>(); 
	private double initialValue;
	private double outputValue; // Maybe later this could be extended to an object so as to convey more information
							    // and multiple outputs values corresponding to different attributes of the item the
								// node is modelling. 
	
	private String equationString = null;

	// Speed modifiers
	private Map<String, PropagationSpeedModifier> modifiers = new HashMap<String, PropagationSpeedModifier>();
	
	private int timePoint = 0;
	
	public DynamicModelNode(String newName){
		this.idString = newName;
	}
	
	public void reset(){
		timePoint = 0;
		outputValue = initialValue;
		for(PropagationSpeedModifier modifier: modifiers.values()){
			modifier.reset();
		}
	}
	
	public void updateStarterNode(int timePointFromPerviousLoop){
		update(timePointFromPerviousLoop, true);
	}
	
	public void update(int newTimePoint){
		update(newTimePoint, false);
	}
	
	public void update(int newTimePoint, boolean isStarterNode){
	
		// Don't process if already updated
		if(timePoint == newTimePoint && !isStarterNode){
			return;
		}
		
		// No inputs 
		if(getFromNodeIds().size() == 0){
			// ..with an expression
			if(equationString != null){
				outputValue = runEquationUpdate(newTimePoint);
			}
			timePoint = newTimePoint;
			return;
		}
		
		// With expression and where (all inputs are ready or it's a starter node) 
		if(equationString != null && (checkInputsAreReady(newTimePoint) || isStarterNode)){
			outputValue = runEquationUpdate(newTimePoint);
		}
		else{
			return;
		}
		
		if(!isStarterNode){
			timePoint = newTimePoint;
		}
	}
	
	private boolean checkInputsAreReady(int newTimePoint){
		
		logger.info("Checking the inputs of " + getIdString() + " at time point " + timePoint);
		for(Node iNode: getInputNodes()){
			logger.info("Time point of input: " + iNode.getIdString() + "=" + iNode.getTimePoint());
			if(iNode.getTimePoint() <= timePoint){
				logger.info("Inputs of " + getIdString() + " are NOT ready");
				return false;
			}
		}
		logger.info("Inputs of " + getIdString() + " *are* ready");
		return true;
	}
	
	private double runEquationUpdate(int newTimePoint){

		Map<String, Double>valuesMap = new HashMap<String, Double>();
		double input; 
		// Construct the valuesMap
		for(Node iNode: getInputNodes()){

			// Get the speed modifier
			PropagationSpeedModifier modifier = modifiers.get(iNode.getIdString());
			
			// If there is a valid modifiers for that input, then use it to modify the 
			// input value
			if(modifier != null){
				logger.info("Found a modifier for node " + iNode.getIdString());
				modifier.recordInput(iNode.getOutputValue(), timePoint);
				input = modifier.getStep(iNode.getOutputValue());
			}
			else{
				input = iNode.getOutputValue();
			}
			logger.info("Timepoint:" + newTimePoint + ". Taking the output of " + iNode.getIdString() + " to be " + input);
			valuesMap.put(iNode.getIdString(), new Double(input));
		}

		// Run the equation and return the result
		return ExpressionRunner.runEquation(equationString, valuesMap);
	}
	
	public void addModifier(DynamicModelNode node, PropagationSpeedModifier modifier){
		modifiers.put(node.getIdString(), modifier);
	}

	public void addModifier(String nodeId, PropagationSpeedModifier modifier){
		modifiers.put(nodeId, modifier);
	}

	public void removeModifier(DynamicModelNode node){
		logger.info("removeModifier() removing " + node.getIdString());
		modifiers.remove(node.getIdString());
	}

	public Map<String, PropagationSpeedModifier> getModifiers(){
		return modifiers;
	}
	
	// Adds an input 
	public void addInput(Node newInput){
		inputs.put(newInput.getIdString(), newInput);
	}

	public void removeInput(Node inputNode){
		logger.info("removeInputNode() removing: " + inputNode);
		inputs.remove(inputNode.getIdString());
	}

	public void removeOutput(Node node){
		outputs.remove(node.getIdString());
	}
	
	public void removeInput(String inputNodeId){
		inputs.remove(inputNodeId);
	}

	// Adds an input name without the actually node
	public void addInputPlaceholder(String newInputName){
		inputs.put(newInputName, null);
	}
	
	public int getInputCount(){
		return inputs.size();
	}
	
	public Node getInput(String nodeName){
		return inputs.get(nodeName);
	}
	
	public void renameInput(String newName, String oldName){

		if(inputs.containsKey(oldName) && !newName.equals(oldName)){
			inputs.put(newName, inputs.get(oldName));
			inputs.remove(oldName);
		}
		else{
			// Shouldn't happen
			logger.warn("Trying to rename a node that does not exist! Old Name:" + oldName + " new name:" + newName);
		}
	}
	
	public void renameOutput(String newName, String oldName){

		if(outputs.containsKey(oldName) && !newName.equals(oldName)){
			outputs.put(newName, outputs.get(oldName));
			outputs.remove(oldName);
		}
		else{
			// Shouldn't happen
			logger.warn("Trying to rename a node that does not exist! Old Name:" + oldName + " new name:" + newName);
		}
	}

	public void addOutput(Node newNode){
		outputs.put(newNode.getIdString(), newNode);
	}
	
	public void addOutputPlaceholder(String nodeId){
		outputs.put(nodeId, null);
	}

	public List<Node> getOutputNodes(){
		List<Node> outputNodes = new ArrayList<Node>();
		outputNodes.addAll(outputs.values());
		return outputNodes;
	}

	public List<Node> getInputNodes(){
		List<Node> inputNodes = new ArrayList<Node>();
		inputNodes.addAll(inputs.values());
		return inputNodes;
	}
	
	public void setInitialValue(double newInitialValue){
		this.initialValue = newInitialValue;
		this.outputValue = initialValue;
	}
	
	public double getInitialValue(){
		return initialValue;
	}
	
	public double getOutputValue() {
		return outputValue;
	}

	public void setOutputValue(double outputValue) {
		this.outputValue = outputValue;
	}

	public int getTimePoint() {
		return timePoint;
	}

	public void setTimePoint(int timePoint) {
		this.timePoint = timePoint;
	}

	public String getIdString() {
		return idString;
	}
	
	public void setIdString(String newIdString){
		this.idString = newIdString;
	}

	public List<Node> getFromNodes(){
		List<Node> nodes = new ArrayList<Node>();
		nodes.addAll(inputs.values());
		return nodes;
	}
	
	public List<String> getFromNodeIds(){
		List<String> fromNodeIds = new ArrayList<String>();
		fromNodeIds.addAll(inputs.keySet());
		return fromNodeIds;
	}
	
	public List<Node> getToNodes(){
		List<Node> nodes = new ArrayList<Node>();
		nodes.addAll(outputs.values());
		return nodes;
	}

	public List<String> getToNodeIds(){
		List<String> toNodeIds = new ArrayList<String>();
		toNodeIds.addAll(outputs.keySet());
		return toNodeIds;
	}	
	
	public void setNodeName(String nodeName) {
		this.idString = nodeName;
	}
	
	public void setExpression(String newEquationString){
		equationString = newEquationString;
	}
	
	public String getExpression(){
		return equationString;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder(80);
		
		builder.append("[DynamicModelNode: [idString:\"").append(idString).append("\"] ");
		
		builder.append("[inputs:");
		for(String inputName: inputs.keySet()){
			builder.append(inputName).append(", ");
		}
		
		builder.append("] [outputs:");
		for(String outputName: outputs.keySet()){
			builder.append(outputName).append(", ");
		}

		builder.append("]]");
		
		return builder.toString();
	}
}
