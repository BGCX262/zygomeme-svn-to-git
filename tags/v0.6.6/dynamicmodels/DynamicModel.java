package com.zygomeme.york.dynamicmodels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import org.apache.log4j.Logger;

import com.zygomeme.york.Identifiable;
import com.zygomeme.york.Node;
import com.zygomeme.york.dynamicmodelhistory.MultiDimensionalArray;
import com.zygomeme.york.gui.PropertiesMemento;
import com.zygomeme.york.reporter.DynamicModelHistoryReporter;
import com.zygomeme.york.util.StringUtil;

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
 * DynamicModel is the Model and Control of the Dynamic model. Stores and runs the model. 
 * Future work sure ensure that this class can run independent of any GUI elements.  
 * 
 */
public class DynamicModel implements Identifiable, Runnable{

	private Logger logger = Logger.getLogger(DynamicModel.class);
	private Map<String, Node> nodeMap = new LinkedHashMap<String, Node>();
	private MultiDimensionalArray<DynamicModelHistory> histories;
	private ModelValidator validator = new ModelValidator();
	private DynamicModelHistoryReporter reporter;
	private List<String> startNodes = new ArrayList<String>();
	private int endTime = 1; // Default
	private String idString;
	private DynamicModelListener listener = null;
	// TODO Have the progress monitor update via the DynamicModelListener so that the 
	// DM is decoupled from any GUI code.
	private ProgressMonitor progressMonitor;
	
	private Map<String, LoopConfigurationBean> loopConfig;
	
	public DynamicModel(String initialIdString){
		idString = initialIdString;
		logger = Logger.getLogger(DynamicModel.class);
		reporter = new DynamicModelHistoryReporter();
	}
	
	public void setLoopConfigurationMap(Map<String, LoopConfigurationBean> newMap){
		this.loopConfig = newMap;
	}
	
	public void addModelListener(DynamicModelListener listenerIn){
		listener = listenerIn;
	}
	
	public void setProgressMonitor(ProgressMonitor newPM){
		this.progressMonitor = newPM;
		progressMonitor.setMinimum(0);
	}
	
	public Map<String, LoopConfigurationBean> getLoopConfig(){
		return loopConfig;
	}
		
	public DynamicModelHistory getDefaultHistory(){
		if(histories == null){
			return null;
		}
		else{
			return histories.getItem("default");
		}
	}

	public DynamicModelHistory selectHistory(Map<String, Integer> selectionKey){
		
		// Construct the key
		logger.info("selectionKey:" + selectionKey);
		int[] key = new int[selectionKey.size()];
		int i = 0;
		for(String param: selectionKey.keySet()){
			key[i] = selectionKey.get(param);
			logger.info("key element:" + selectionKey.get(param));
			i++;
		}
		
		logger.info("history count:" + histories.values().size());
		
		// Show histories
		for(String hKey: histories.keySet()){
			logger.info("hKey:" + hKey);
			logger.info("History:key:" + hKey + " history:" + histories.getItem(hKey));
		}
		// Get the history
		return histories.getItem(key);
	}

	public Map<String, Node> getNodeMap(){
		return nodeMap;
	}
	
	public void setStartNodes(List<String> newStartNodes){
		startNodes.clear();
		startNodes.addAll(newStartNodes);
		logger.info("startNodes:" + startNodes + ", length:" + startNodes.size());
	}

	public void addStartNode(String newStartNode){
		startNodes.add(newStartNode);
	}

	public List<String> getStartNodes(){
		return startNodes;
	}
	
	public void removeStartNode(String nodeId){
		startNodes.remove(nodeId);
	}
	
	public void setEndTime(int newEndTime){
		this.endTime = newEndTime;
	}
	
	public long getEndTime(){
		return endTime;
	}
		
	public void addNode(Node newNode){
		nodeMap.put(newNode.getIdString(), newNode);
	}
	
	public Set<String> getNodeIds(){
		return nodeMap.keySet();
	}
	
	public Node getNode(String name){
		return nodeMap.get(name);
	}
	
	public String getIdString(){
		return idString;
	}

	public void setIdString(String newId){
		logger.info("Setting idString to " + newId);
		idString = newId;
	}

	public void renameNode(String oldName, String newName){

		Node renamedNode = nodeMap.get(oldName);
		nodeMap.remove(oldName);
		nodeMap.put(newName, renamedNode);
		
		if(startNodes.contains(oldName)){
			startNodes.remove(oldName);
			startNodes.add(newName);
		}
		
		// Go through the node and change the references
		for(Node node: nodeMap.values()){
			if(node.getFromNodes().contains(renamedNode)){
				logger.info("renameNode() Renaming input node from " + oldName + " to " + newName);
				((DynamicModelNode)node).renameInput(newName, oldName);
			}

			if(node.getToNodes().contains(renamedNode)){
				logger.info("renameNode() Renaming output node to from " + oldName + " to " + newName);
				((DynamicModelNode)node).renameOutput(newName, oldName);
			}
		}
		
		// Rename any references in the nodes expressions
		logger.debug("Looking to rename elements in expressions in " + nodeMap.size() + " other nodes");
		for(Node node: nodeMap.values()){
			String expression = ((DynamicModelNode)node).getExpression();
			if(expression != null && expression.length() > 0){
				String[] splitExpression = StringUtil.retainingSplit(expression, ExpressionRunner.OPERATORS);
				StringBuffer newExpression = new StringBuffer();
				boolean changeMade = false;
				for(String element: splitExpression){
					if(oldName.equals(element)){
						newExpression.append(newName).append(" ");
						changeMade = true;
					}
					else{
						newExpression.append(element).append(" ");					
					}
				}
				if(changeMade){
					((DynamicModelNode)node).setExpression(newExpression.toString().trim());
				}
			}
		}
	}
	
	private void tellListenersTerminatedStatus(boolean flag){
		if(listener != null){
			listener.setRunTerminatedStatus(flag);		
		}
	}

	/**
	 * Runnable interface method implementation
	 */
	public void run()
	{
		runModels();
	}
	
	/** 
	 * Convenience method for when the the start nodes have already been defined
	 */
	public MultiDimensionalArray<DynamicModelHistory> runModels()
	{
		tellListenersTerminatedStatus(false);

		long before = System.currentTimeMillis();
		
		if(loopConfig != null){
			NestedModelRunner nestedModelRunner = new NestedModelRunner();
			histories = nestedModelRunner.runModel(this);
			logger.info("Created " + histories.values().size() + " histories");
		}
		else{
			histories = new MultiDimensionalArray<DynamicModelHistory>();
			if(progressMonitor != null){
				progressMonitor.setMaximum(endTime);
			}
			DynamicModelHistory history = runModel(); 
			histories.addItem(history, "default");
		}
		long elapsed = System.currentTimeMillis() - before;
		logger.info("Elapsed time for run: " + elapsed + "mSec");
		
		tellListenersTerminatedStatus(true);
		return histories;
	}

	/** 
	 * Convenience method for when there is only one startNode
	 * 
	 * @param startNode - the single starting node
	 * @param endTime - the end time for the run
	 */ 
	public DynamicModelHistory run(String startNode, int newEndTime){
		startNodes = new ArrayList<String>();
		startNodes.add(startNode);
		this.endTime = newEndTime;
		return runModel();
	}
	
	public DynamicModelHistory runModel(){

		int t = 0; // First time point
		DynamicModelHistory history = new DynamicModelHistory();
		history.setStartNodeIds(startNodes);
		resetNodes();
		
		// Validate the model - run some tests to see if the model is reasonable. 
		// This will check things like do any of the nodes refer to nodes that 
		// have been deleted or are otherwise not there. 
		validator.validate(this, history.getErrorReport());		
		if(history.getStatus() == DynamicModelHistory.StatusType.ERROR){
			writeHistoryReport(history);
			return history;
		}
		
		// Set nodes that point to themselves as start nodes
		setAutoStartNodes();
				
		List<Node> nodesToUpdate = new ArrayList<Node>();
		nodesToUpdate.addAll(nodeMap.values());
		boolean deadlock = false;
		// For this number of timePoints
		// To account for the fact that the number is zero based for the application but this is confusing
		// for the user we have "<=" rather than the normal "<". Hence setting an end time of 10 results 
		// in the last iteration being t+10. 
		for(t = 0; t <= endTime; t++){

			// Set up the history record so that the inputs can be saved below
			history.setupRecords(nodeMap, t);
			Map<String, DynamicNodeMemento> timePointRecord = history.getHistory().get(t);
			
			// Initiate the start nodes
			// The first time around the model should just have the initial values and no
			// computation should have occurred
			if(t > 0){
				for(String startNodeName: startNodes){
					Node node = nodeMap.get(startNodeName);
					history.addToHistoryLog("Start node name:>" + startNodeName + "<");
					if(node.getInputCount() > 0){
						history.addToHistoryLog("input count > 0");
						node.updateStarterNode(t - 1);
						node.setTimePoint(t);
					}
					else{
						node.setTimePoint(t);
					}
				}
			}			
			
			// Update the nodes
			// Loop around until all nodes have been updated, or the number of nodes to update is unchanged
			// between two loops.
			int previousNodesToUpdateCount;
			if(t > 0){
				do{
					previousNodesToUpdateCount = nodesToUpdate.size();
					List<Node> nodesNotYetUpdated = new ArrayList<Node>();
					for(Node node: nodesToUpdate){
						// Record the inputs
						DynamicNodeMemento memento = timePointRecord.get(node.getIdString());
						for(Node inputNode: node.getFromNodes()){
							memento.setInput((DynamicModelNode)inputNode);
						}
						// Get the node to update itself
						history.addToHistoryLog("Updating " + node.getIdString());
						node.update(t);
						
						// Check for infinity 					
						if(Double.isInfinite(node.getOutputValue())){
							history.setStatus(DynamicModelHistory.StatusType.ERROR);
							history.getErrorReport().addError(ModelErrorReport.ErrorType.INFINITY_ENCOUNTED, node.getIdString());
							writeHistoryReport(history);
							return history;						
						}

						// Check for NaN 					
						if(Double.isNaN(node.getOutputValue())){
							history.setStatus(DynamicModelHistory.StatusType.ERROR);
							history.getErrorReport().addError(ModelErrorReport.ErrorType.NAN_ENCOUNTED, node.getIdString());
							writeHistoryReport(history);
							return history;						
						}

						if(node.getTimePoint() != t){
							nodesNotYetUpdated.add(node);
						}
					}
					nodesToUpdate.clear();
					nodesToUpdate.addAll(nodesNotYetUpdated);
					logger.info("Number of nodes to update in this inner iteration:" + nodesToUpdate.size());
					if(nodesToUpdate.size() == previousNodesToUpdateCount){
						deadlock = true;
					}
				}
				while(nodesToUpdate.size() > 0 && nodesToUpdate.size() < previousNodesToUpdateCount);
			}
			
			if(deadlock){
				history.setStatus(DynamicModelHistory.StatusType.ERROR);
				history.getErrorReport().addError(ModelErrorReport.ErrorType.DEADLOCKED, nodesToUpdate);
				writeHistoryReport(history);
				return history;
			}

			// Refresh the list for the next timePoint
			nodesToUpdate.clear();
			nodesToUpdate.addAll(nodeMap.values());
			
			// Record into the history object the state of the model at this time point
			history.recordState(nodeMap, t);
			logger.info("Time point:" + t);
	
			if(listener != null){
				listener.setProgress(t);
			}

			if(progressMonitor != null){
				progressMonitor.setProgress(t);
				// If the number of iterations if very high then there is no need to 
				// display each and every "t", - this will slow down the processing, so 
				// only update in 10's, 100's etc.
				if(endTime <= 100){
					progressMonitor.setNote("Iteration:" + t);					
				}
				else{
					if(endTime > 1000){
						if((t % 100) == 0){
							progressMonitor.setNote("Iteration:" + t);
						}
					}
					else{
						if(endTime > 100){
							if((t % 10) == 0){
								progressMonitor.setNote("Iteration:" + t);
							}
						}
					}
				}
				if(progressMonitor.isCanceled()){
					progressMonitor.setNote("Iteration:" + t);
					break;
				}
			}
		}

		if(progressMonitor != null){
			progressMonitor.setProgress(progressMonitor.getMaximum());
		}

		history.setStatus(DynamicModelHistory.StatusType.SUCCESS);
		writeHistoryReport(history);	
		return history;
	}
		
	private void writeHistoryReport(DynamicModelHistory history){
		
		// Rewrite history :)
		String filename = PropertiesMemento.REPORT_DIR + idString + ".html";
		try {
			reporter.writeReport(history, filename);	
		} catch (Exception e) {			
			JOptionPane.showMessageDialog(null, new String[]{"Unable to save the history report. " +  
															 "Ensure that file is not in use by any other program.", 
															 "Tried to save to " + filename}, 
															 "Failure to write History Report", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void resetNodes(){
		
		for(Node node: nodeMap.values()){
			node.reset();
		}
	}	
	
	public void setAutoStartNodes(){
		
		for(Node node: nodeMap.values()){
			if(node.getFromNodeIds().contains(node.getIdString())){
				logger.info("Setting node " + node.getIdString() + " to start node as it points to itself");
				if(!startNodes.contains(node.getIdString())){
					startNodes.add(node.getIdString());
				}
			}
		}
	}	

}
