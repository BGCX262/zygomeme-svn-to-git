package com.zygomeme.york.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.zygomeme.york.ArcMap;
import com.zygomeme.york.Node;
import com.zygomeme.york.chart.RComponent;
import com.zygomeme.york.chart.RComponentListener;
import com.zygomeme.york.dynamicmodels.DynamicModel;
import com.zygomeme.york.dynamicmodels.DynamicModelHistory;
import com.zygomeme.york.dynamicmodels.DynamicModelListener;
import com.zygomeme.york.dynamicmodels.LoopConfigurationBean;
import com.zygomeme.york.propertiesdialog.ConfigureNestedLoopUI;
import com.zygomeme.york.propertiesdialog.MappedPanel;
import com.zygomeme.york.propertiesdialog.ModelPropertiesDialog;
import com.zygomeme.york.propertiesdialog.PropertiesDialog;
import com.zygomeme.york.wizard.DynamicModelWizard;
import com.zygomeme.york.xml.DynamicModelExporter;
import com.zygomeme.york.gui.EntityViewConfig;

/**
 * *********************************************************************
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
 * The view for the Dynamic Model 
 * 
 */
public class DynamicModelView extends YorkEntityView implements ActionListener, RComponentListener, DynamicModelListener{

	private Logger logger = Logger.getLogger(DynamicModelView.class);
	private DynamicModel dModel;
	private Map<Node, YorkEntityView> nodeViewMap = new HashMap<Node, YorkEntityView>();
	private DynamicModelHistoryView historyView;
	// The dynamic model has its own arc renderer to render its own arcs.
	private ArcRenderer arcRenderer = new ArcRenderer();
	private ArcMap arcMap = null;
	private boolean horizontalScrollBarBeingDragged = false;
	private boolean verticalScrollBarBeingDragged = false;
	
	public final static int minimumWidth = 290;
	public final static int minimumHeight = 300;
	
	private TabView tabView;
	
	private GUIButton nestedGraphButton;
	private ButtonEventHandler buttonEventHandler;
	private final static String NESTED_COMMAND = "show nested graphs";
		
	private final static String SET_ITERATION_COUNT = "Set iteration count...";
	private final static String SET_START_NODES = "Set start nodes...";
	private final static String RUN_MODEL = "Run";
	private final static String EDIT_MODEL_NAME = "Edit model name...";
	private final static String SAVE_CHART_AS_IMAGE = "Save chart as image...";
	private final static String PREFEREENCES = "Set preferences...";
		
	public DynamicModelView(EntityViewConfig newConfig, 
							DynamicModel newDynamicModel, 
							YorkBrowser browserIn, TabView tabViewIn){
		super(newDynamicModel, browserIn, newConfig);
		this.config = newConfig;
		config.setHasSaveButton(false);
		config.setHasCloseButton(false);
		config.setMinWidth(minimumWidth); // The minimum width and height of the nugget under resize requests
		config.setMinHeight(minimumHeight);
		
		this.dModel = newDynamicModel;
		dModel.addModelListener(this);
		this.tabView = tabViewIn;
		this.historyView = new DynamicModelHistoryView(browserIn, config, tabView);
		
		this.rightClickLabels = new String[]{SET_ITERATION_COUNT, SET_START_NODES, RUN_MODEL, 
											 EDIT_MODEL_NAME, SAVE_CHART_AS_IMAGE, PREFEREENCES};
		this.rightClickListener = this;
		
		nestedGraphButton = new GUIButton(browserIn.getProperties().getImageDir() + "nested_graph16.png");
		this.buttonEventHandler = tabView;
		buttonEventHandler.addButton(nestedGraphButton, this);
		nestedGraphButton.setCommand(NESTED_COMMAND);
		nestedGraphButton.setParentConfig(config);
		
		// Set up the default values
		// Set the default arc style to that found in the properties memento using the "valueOf" method on Enums
		if(browser.getProperties().get(PropertiesMemento.DEFAULT_ARC_STYLE) != null){
			arcRenderer.setLineStyle((ArcRenderer.LineStyle.valueOf(browser.getProperties().get(PropertiesMemento.DEFAULT_ARC_STYLE).toString())));
		}
		else{
			arcRenderer.setLineStyle(ArcRenderer.LineStyle.CUBIC);
		}
		// TODO Need a better way for it to know if it is a new node - therefore takes default values
		// or whether it is a pre-existing node that has stored values/ 
		if(config.getFontColor() == null){
			config.setFontColor(browser.getProperties().getColor(PropertiesMemento.DEFAULT_FONT_COLOR));
		}
		if(config.getBorderColor() == null){
			config.setBorderColor(browser.getProperties().getColor(PropertiesMemento.DEFAULT_NODE_COLOR));
		}
	}
	
	/**
	 * The nodeView map in the model has the ids of the node, but the views have get to be created. In this method
	 * the configs loaded from the XML file are used to create the node views. 
	 * @param configMap
	 */
	public void createNodeViews(Map<String, EntityViewConfig> configMap){

		logger.debug("creating node views");
		for(Node dNode: dModel.getNodeMap().values()){
			YorkEntityView nodeView = nodeViewMap.get(dNode);
			if(nodeView == null){
				// The node has no view - so create one
				createView(dNode, configMap.get(dNode.getIdString()));				
			}			
		}
		
	}
	
	public void createView(Node dNode, EntityViewConfig config){

		// The node has no view - so create one
		if(config == null){
			config = new EntityViewConfig(0, 0, 200, 110, EntityViewConfig.Type.DYNAMIC_NODE);
		}

		logger.info("Creating view for:" + dNode.getIdString() + " at " + config.getX() + " " + config.getY());
		YorkEntityView nodeView = ViewFactory.getView(dNode, browser, config);
		nodeViewMap.put(dNode, nodeView);
		tabView.getLayoutManager().addView(nodeView); // Adds view and places it in the correct place
		
	}
	
	@Override
	public void paint(Graphics g){

		super.paint(g);
		logger.info("DynamicModelView.paint()");
		
		FontMetrics metrics = g.getFontMetrics(); 
		int fontHeight = metrics.getHeight();
		int lineCount = 0;

		// Draw text
		lineCount++;
		String headerMessage = "Dynamic Model: " + dModel.getIdString() + " (end time: t+" + dModel.getEndTime() + ")";
		g.setColor(config.getFontColor());
		g.drawString(headerMessage, config.getX() + widthPad, config.getY() + (lineCount * fontHeight));
		lineCount++;
		
		// Draw any history
		historyView.paint(g);
		
		// Have it draw the nodes within the network
		for(Node dNode: dModel.getNodeMap().values()){
			YorkEntityView nodeView = nodeViewMap.get(dNode);
			nodeView.paint(g);
		}
		
		// Set up the arc map if not already initialised
		if(arcMap == null){
			initialiseArcMap();
		}

		// Render the arcs
		arcRenderer.renderArcs(arcMap, (Graphics2D)g);
		
		// Draw the button
		if("enabled".equals(browser.getProperties().get("nested"))){
			nestedGraphButton.draw(g);			
		}
	}
	
	public DynamicModel getModel(){
		return dModel;
	}
	
	public void removeNodeView(YorkEntityView view){
		logger.info("Removing \"" + view.getModelsIdString() + "\" node view in the DynamicModelView");

		// Remove from the map of views in the class *and* from the map in the dModel
		if(nodeViewMap.remove(view.getNode()) != null){
			dModel.getNodeMap().remove(view.getModelsIdString());
		}
		
		// Remove the node from the list of starter nodes if it appears in that list
		if(dModel.getStartNodes().contains(view.getModelsIdString())){
			dModel.removeStartNode(view.getModelsIdString());
		}
		
		// Put the arcs to be removed into a list first before deleting to avoid 
		// a concurrent modification exception. 
		List<Arc> doomedArcs = new ArrayList<Arc>();
		for(Arc arc: arcMap.getArcs()){
			logger.info("removeNodeView(): arc:" + arc);
			if(arcMap.isConnectedTo(arc, view.getNode())){
				logger.info("Arc:" + arc + " is connected to " + view.getNode().getIdString());
				// This arc is connected to the removed node, so marked it for deletion
				doomedArcs.add(arc);
			}
		}
		// Remove those arc that have been found to be connected the node removed. 
		for(Arc arc: doomedArcs){
			arcMap.remove(arc);
		}
		
		// Remove from layout manager
		for(YorkEntityView nodeView: tabView.getLayoutManager().getYorkModelViews()){
			logger.info("removeNodeView() nodeView:" + nodeView); // Just for logging
		}
		
		// Remove the view from the layout manager
		tabView.getLayoutManager().removeView(view);
		
		// Remove the node in references to other nodes...
		logger.info("removeNodeView() About to remove the node to node references");
		Map<String, Node> nodeMap = dModel.getNodeMap();
		logger.info("removeNodeView() Before removal...");
		for(Node node: nodeMap.values()){
			logger.info(node.getIdString() + " = " + node);
		}
		Node removedNode = view.getNode();
		for(Node node: nodeMap.values()){
			logger.info("removeNodeView() Taking a look at " + node.getIdString() + " = " + node);
			List<Node> fromNodes = node.getFromNodes();
			for(Node fromNode: fromNodes){
				logger.info("removeNodeView() Looking at " + node.getIdString() + " connected to " + fromNode.getIdString());
				if(fromNode.equals(removedNode)){
					logger.info("removeNodeView() Remove refence in " + node.getIdString() + " to " + 
							fromNode.getIdString());
					node.removeInput(fromNode);
				}
			}	
			
			List<Node> toNodes = node.getToNodes();
			for(Node toNode: toNodes){
				logger.info("removeNodeView() Looking at " + node.getIdString() + " connected to " + toNode.getIdString());
				if(toNode.equals(removedNode)){
					logger.info("removeNodeView() Remove refence in " + node.getIdString() + " to " + 
							toNode.getIdString());
					node.removeOutput(toNode);
				}
			}			
		}
		
		// Log the result
		logger.info("removeNodeView() After removal...");
		for(Node node: nodeMap.values()){
			logger.info(node.getIdString() + " = " + node);
		}
	}
	
	public void renameNode(Node dNode, String oldName, String newName){
		arcMap.renameNode(oldName, newName);
	}
	
	public YorkEntityView getViewForNode(String nodeId){
		return nodeViewMap.get(dModel.getNode(nodeId));
	}
	
	private void initialiseArcMap(){
		
		logger.info("Initialising Arc Map");
		arcMap = new ArcMap();
		for(YorkEntityView view: nodeViewMap.values()){
			logger.info("view:" + view.getModelsIdString() + ", type:" + view.getClass().getName());
			List<Node> fromNodeIds = view.getNode().getFromNodes();
			for(Node fromNode: fromNodeIds){
				logger.info("Adding " + fromNode.getIdString() + " to " + view.getModelsIdString());
				arcMap.add(nodeViewMap.get(fromNode), view);
			}
		}
	}
	
	public ArcMap getArcMap(){
		return arcMap;
	}

	public void configureNestedLoops(){

		Map<String, Node> nodeMap = dModel.getNodeMap();
		// Get nodes without inputs - these are the nodes that can have ranges set on them
		// in nested loops
		List<String> candidateNodeIds = new LinkedList<String>();
		for(String id: nodeMap.keySet()){
			if(nodeMap.get(id).getInputCount() == 0){
				candidateNodeIds.add(id);
			}
		}
		
		ConfigureNestedLoopUI loopUI = new ConfigureNestedLoopUI();
		Map<String, LoopConfigurationBean> loopConfig = loopUI.getLoopConfiguration(browser, candidateNodeIds);
		if(loopConfig == null){
			return;
		}
		
		dModel.setLoopConfigurationMap(loopConfig);
		
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		super.mousePressed(e);
		logger.info("mousePressed() Button:" + e.getButton());
		historyView.mousePressed(e);
	}

	@Override
	public void mouseDragged(MouseEvent e){
		
		logger.info("horizontalScrollBarBeingDragged:" + horizontalScrollBarBeingDragged);
		if(historyView.isMouseOverHorizontalScrollBar(e) && !verticalScrollBarBeingDragged){
			horizontalScrollBarBeingDragged = true;
			historyView.setMouseBeingDraggedOnHorizontalScrollBar(true);
		}

		if(historyView.isMouseOverVerticalScrollBar(e) && !horizontalScrollBarBeingDragged){
			verticalScrollBarBeingDragged = true;
			historyView.setMouseBeingDraggedOnVerticalScrollBar(true);
		}
		
		if(!horizontalScrollBarBeingDragged && !verticalScrollBarBeingDragged){
			super.mouseDragged(e);
		}
		else{
			if(horizontalScrollBarBeingDragged){
				historyView.setMouseBeingDraggedOnHorizontalScrollBar(true);
			}
			if(verticalScrollBarBeingDragged){
				historyView.setMouseBeingDraggedOnVerticalScrollBar(true);
			}
			historyView.mouseDragged(e);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e){		
		historyView.setMouseBeingDraggedOnHorizontalScrollBar(false);
		historyView.setMouseBeingDraggedOnVerticalScrollBar(false);
		horizontalScrollBarBeingDragged = false;
		verticalScrollBarBeingDragged = false;
	}
	
	// Action Listener - for the right click menu
	public void actionPerformed(ActionEvent event){
		logger.info("ActionEvent:" + event);

		if(SET_ITERATION_COUNT.equals(event.getActionCommand())){
			DynamicModelWizard wiz = new DynamicModelWizard();
			int newEndTime = wiz.getIterationCount(browser);
			if(wiz.wasCancelled()){
				return;
			}
			dModel.setEndTime(newEndTime);
			browser.repaint(); // Ensure the browser is updated
			return;
		}
		
		if(SET_START_NODES.equals(event.getActionCommand())){
			DynamicModelWizard wiz = new DynamicModelWizard();
			Map<String, Boolean> resultsMap = wiz.getStartNodes(browser, dModel);
			if(!wiz.wasCancelled()){
				List<String> startNodeList = new ArrayList<String>();
				for(String node: resultsMap.keySet()){
					// If set to true then add to start list
					if(resultsMap.get(node)){
						startNodeList.add(node);
					}
				}
				dModel.setStartNodes(startNodeList);
			}
			browser.repaint(); // Ensure the browser is updated
			return;
		}

		if(RUN_MODEL.equals(event.getActionCommand())){
			logger.info("Run Model");
			browser.runModel();
			return;
		}
		
		if(EDIT_MODEL_NAME.equals(event.getActionCommand())){
			logger.info("Edit model name");
			browser.editTabName();
			browser.repaint(); // Ensure the browser is updated
			return;
		}	
		
		if(SAVE_CHART_AS_IMAGE.equals(event.getActionCommand())){
			historyView.saveChartAsImage();
			return;
		}	

		if(PREFEREENCES.equals(event.getActionCommand())){
			logger.info("Set preferences");
			ModelPropertiesDialog modelPropertiesDialog = new ModelPropertiesDialog(browser, config);
			modelPropertiesDialog.showDialog();

			Map<String, JComponent> preferencesMap = modelPropertiesDialog.getMappedPanels(PropertiesDialog.NODE_FONT);

			for(String k: preferencesMap.keySet()){
				MappedPanel mPanel = (MappedPanel)preferencesMap.get(k);
				Map<String, Object> results = mPanel.getResultsMap();
				for(String key: results.keySet()){
					Object value = results.get(key);
					if(PropertiesDialog.NODE_FONT.equals(key)){
						config.setFont((String)value);
					}
					if(PropertiesDialog.BORDER_COLOR.equals(key)){
						config.setBorderColor((Color)value);
					}
					if(PropertiesDialog.FONT_COLOR.equals(key)){
						config.setFontColor((Color)value);
					}
				}				
			}
			browser.repaint();
			return;
		}	

	}
		
	@Override
	public void save(){
		logger.info("DynamicModelView.save() for:" + dModel.getIdString());
		DynamicModelExporter exporter = new DynamicModelExporter();
		logger.info("Nodes:");
		for(Node node: nodeViewMap.keySet()){
			logger.info("Node:" + node.getIdString());
		}
		exporter.save(this, browser.getProperties().getDataDir() + "dynamic models" + File.separator + dModel.getIdString() + ".xml", nodeViewMap);
	}

	// GUIButton events
	public void actionEvent(RComponent component){
		logger.info("Button pressed: " + component.getCommand());
		if(NESTED_COMMAND.equals(component.getCommand())){
			logger.info("Requested made to show nested graphs");
			NestedGraphSelector nestedGraphSelector = new NestedGraphSelector(browser, dModel.getLoopConfig());
			nestedGraphSelector.showDialog();	
			Map<String, Integer> indexMap = nestedGraphSelector.getIndexMap();
			if(indexMap == null){
				JOptionPane.showMessageDialog(browser, "No selection made", "Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			DynamicModelHistory selectedHistory = dModel.selectHistory(indexMap);
			JOptionPane.showMessageDialog(browser, "Chosen:" + indexMap, "Info", JOptionPane.INFORMATION_MESSAGE);
			historyView.setHistory(selectedHistory);
			browser.repaint();
		}
	}
	
	// Dynamic Model Listener events
	public void setRunTerminatedStatus(boolean terminatedStatus){
		
		if(terminatedStatus == true){
			historyView.setHistory(dModel.getDefaultHistory());
			browser.repaint();
			browser.reportAnyErrors(dModel.getDefaultHistory());
		}
	}
	
	public void setProgress(int count){
		
	}
	public void setMinimum(int min){
		
	}
	public void setmMaximum(int max){
		
	}

}
