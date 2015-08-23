package com.zygomeme.york.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.zygomeme.york.Node;
import com.zygomeme.york.dynamicmodels.DynamicModel;
import com.zygomeme.york.dynamicmodels.DynamicModelNode;
import com.zygomeme.york.dynamicmodels.ExpressionRunner;
import com.zygomeme.york.dynamicmodels.ExpressionValidator;
import com.zygomeme.york.dynamicmodels.ErrorItem;
import com.zygomeme.york.wizard.DynamicModelNodeWizard;
import com.zygomeme.york.propertiesdialog.MappedPanel;
import com.zygomeme.york.propertiesdialog.NodePropertiesDialog;
import com.zygomeme.york.propertiesdialog.PropertiesDialog;
import com.zygomeme.york.util.StringUtil;
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
 * This is the view part for the Dynamic Nodes. Most of the functionality is from the superclass. 
 * 
 */

public class DynamicModelNodeView extends YorkEntityView implements ActionListener{
		
	private DynamicModelNode dNode;
	private final static String SET_INITIAL_VALUE = "Set initial value...";
	private final static String SET_FREE_FORM_EQUATION = "Set expression...";
	// Modifiers not currently used. These will/may be re-introduced once there is enough time to 
	// set up a config wizard and have the modifier actions detailed in the HTML report.
	private final static String SET_NAME = "Change name...";
	private final static String SET_PREFERENCES = "Set Preferences...";
	
	private NumberFormatter formatter = new NumberFormatter();

	public DynamicModelNodeView(EntityViewConfig newConfig, DynamicModelNode newDynamicNode, YorkBrowser browserIn){
		super(newDynamicNode, browserIn, newConfig);
		this.config = newConfig;
		this.dNode = newDynamicNode;
		config.setHasSaveButton(false);
		
		this.rightClickLabels = new String[]{SET_INITIAL_VALUE, SET_FREE_FORM_EQUATION, SET_NAME, SET_PREFERENCES};
		this.rightClickListener = this;
		this.logger = Logger.getLogger(DynamicModelNodeView.class);
		
		// Set the default values
		// TODO Need a better way for it to know if it is a new node and therefore takes default values
		// or whether it is a pre-existing node that has stored values
		if(config.getFontColor() == null){
			config.setFontColor(browser.getProperties().getColor(PropertiesMemento.DEFAULT_FONT_COLOR));
		}
		if(config.getBorderColor() == null){
			config.setBorderColor(browser.getProperties().getColor(PropertiesMemento.DEFAULT_NODE_COLOR));
		}
	}
	
	public Node getNode(){
		return dNode;
	}
	
	public void paint(Graphics g){

		super.paint(g);
		logger.info("DynamicModelNodeView.paint()");

		Font previousFont = g.getFont();
		g.setFont(config.getFont());
		FontMetrics metrics = g.getFontMetrics(); 
		int fontHeight = metrics.getHeight();
		int lineCount = 0;
		int maxLineCount = config.getHeight() / fontHeight;

		// TODO the title drawing could go in the parent class
		// Draw text
		lineCount++;
		g.setColor(config.getFontColor());
		g.drawString(dNode.getIdString(), config.getX() + widthPad, config.getY() + (lineCount * fontHeight));
		lineCount++;

		lineCount = wrap(g, fontHeight, "Initial Value: " + formatter.format(dNode.getInitialValue()), lineCount, maxLineCount, metrics);
		//lineCount = wrap(g, fontHeight, "Given Value: " + dNode.getGivenValue(), lineCount, maxLineCount, metrics);
		String outputString = "Output: " + formatter.format(dNode.getOutputValue()); 
		if(Double.isNaN(dNode.getOutputValue())){
			outputString = "Output: Invalid" ;
		}
		lineCount = wrap(g, fontHeight, outputString, lineCount, maxLineCount, metrics);
		
		if(dNode.getExpression() != null && dNode.getExpression().length() > 0){
			lineCount = wrap(g, fontHeight, "Expression: " + dNode.getIdString() + " = " + dNode.getExpression(), lineCount, maxLineCount, metrics);
		}
		
		g.setFont(previousFont);
	}
	
	public void addAToView(YorkEntityView toView){ 

		if(toView instanceof DynamicModelNodeView){
			dNode.addOutput((DynamicModelNode)toView.getNode());
		}
		else{
			JOptionPane.showMessageDialog(browser, "Cannot connect that type of input to this node", "Error", JOptionPane.ERROR_MESSAGE);
		}		 
	}
	
	public void addAFromView(YorkEntityView fromView){
		if(fromView instanceof DynamicModelNodeView){
			dNode.addInput((DynamicModelNode)fromView.getNode());
		}
		else{
			JOptionPane.showMessageDialog(browser, "Cannot connect this type of node", "Error", JOptionPane.ERROR_MESSAGE);			
		}		
	}
		
	// Action Listener - for the right click menu
	public void actionPerformed(ActionEvent event){
		logger.info("ActionEvent:" + event);
		
		if(SET_PREFERENCES.equals(event.getActionCommand())){
			logger.info("Set preferences");
			NodePropertiesDialog nodePropertiesDialog = new NodePropertiesDialog(browser, config);
			nodePropertiesDialog.showDialog();

			Map<String, JComponent> preferencesMap = nodePropertiesDialog.getMappedPanels(PropertiesDialog.NODE_FONT);

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
		}
		
		if(SET_INITIAL_VALUE.equals(event.getActionCommand())){
			logger.info("Set initial value");
			DynamicModelNodeWizard wiz = new DynamicModelNodeWizard();
			double initialValue = wiz.getInitialValue(browser);
			dNode.setInitialValue(initialValue);
			browser.repaint();
		}

		if(SET_FREE_FORM_EQUATION.equals(event.getActionCommand())){
			logger.info("Set Free Form Equation");
			DynamicModelNodeWizard wiz = new DynamicModelNodeWizard();
			ExpressionValidator validator = new ExpressionValidator();
			boolean functionSet = false;
			while(!functionSet && !wiz.wasCancelled()){
				String newEquation = wiz.getExpression(browser, dNode);
				logger.info("actionPerformed() new expression:" + newEquation);
				if(wiz.wasCancelled()){
					return;
				}

				List<ErrorItem> report = validator.validateEquation(newEquation, browser.getCurrentView().getDynamicModelView().getModel().getNodeMap().keySet(), dNode);

				// If there are errors then report them
				if(report.size() > 0){
					JOptionPane.showMessageDialog(browser, report.get(0).getMessage(), "Invalid expression", JOptionPane.ERROR_MESSAGE);					
				}
				else{
					String[] extractedIdArray = StringUtil.retainingSplit(newEquation, ExpressionRunner.OPERATORS);
					List<String> extractedIds = StringUtil.getFilteredArray(extractedIdArray, ExpressionRunner.OPERATORS);
					//logger.info("Extracted ids are:" + List.toString(extractedIds));
					logger.info("Extracted ids are:" + extractedIds);
					List<String> needToAdd = new ArrayList<String>();
					for(String nodeId: extractedIds){
						// if it is not in the list of from nodes for this node and it is not already in the 
						// list of nodes to add and it is in the list of nodes recorded by the dynamic model
						// then add it to the list of nodes to add
						// TODO Nodes should have a link to the model rather than this long line
						if(!dNode.getFromNodeIds().contains(nodeId) && !needToAdd.contains(nodeId) &&
								browser.getCurrentView().getDynamicModelView().getModel().getNodeMap().containsKey(nodeId)){
							needToAdd.add(nodeId);
						}
					}
					
					// Add links to nodes included in the expression
					logger.info("Need to add:" + needToAdd);
					for(String nodeId: needToAdd){
						// TODO Nodes should have a link to the model rather than this long line
						browser.setFromView(browser.getCurrentView().getDynamicModelView().getViewForNode(nodeId));
						browser.setToView(this);
					}

					// Removal of unnecessary nodes 
					for(String fromNodeId: dNode.getFromNodeIds()){
						if(!extractedIds.contains(fromNodeId)){
							logger.info("Need to remove: " + fromNodeId);
							dNode.removeInput(fromNodeId);
							browser.removeArc(browser.getCurrentView().getDynamicModelView().getViewForNode(fromNodeId), this);
						}
					}

					// Set the new function
					dNode.setExpression(newEquation);
					functionSet = true;
					browser.repaint();
				}
			}
			return;
		}
				
		if(SET_NAME.equals(event.getActionCommand())){
			logger.info("Set Name");

			String previousName = dNode.getIdString();
			DynamicModelNodeWizard wiz = new DynamicModelNodeWizard();
			DynamicModel dModel = browser.getCurrentView().getDynamicModelView().getModel();
			Set<String> nodeIds = dModel.getNodeIds();
			boolean nameSet = false;
			String newName = dNode.getIdString();
			while(!wiz.wasCancelled() && !nameSet){
				newName = wiz.getNodeName(browser, dNode.getIdString());
				if((!previousName.equals(newName) && !nodeIds.contains(newName)) ||
						previousName.equals(newName)){
					nameSet = true;
				}
				else{
					if(!previousName.equals(newName) && nodeIds.contains(newName)){
						JOptionPane.showMessageDialog(browser, "That name is already in use, please choose another", "Duplicate Name", JOptionPane.WARNING_MESSAGE);						
					}
				}
			}

			if(previousName.equals(newName)){
				return;
			}

			if(wiz.wasCancelled()){
				return;
			}
			else{
				dNode.setIdString(newName);
				dModel.renameNode(previousName, newName);
				browser.getCurrentView().getDynamicModelView().renameNode(dNode, previousName, newName);
				browser.repaint();
			}
		}

	}		
}
