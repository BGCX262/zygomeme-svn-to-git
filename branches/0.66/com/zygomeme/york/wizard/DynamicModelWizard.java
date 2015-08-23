package com.zygomeme.york.wizard;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.zygomeme.york.dynamicmodels.DynamicModel;
import com.zygomeme.york.dynamicmodels.ExpressionRunner;
import com.zygomeme.york.gui.YorkBrowser;
import com.zygomeme.york.propertiesdialog.CheckBoxPanel;
import com.zygomeme.york.propertiesdialog.IndividualPanelDialog;
import com.zygomeme.york.propertiesdialog.TextFieldPanel;
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
 * Wizard for getting Dynamic Node related input from the user. Relies heavily on IndividualPanelDialog.  
 * 
 */

public class DynamicModelWizard {

	private IndividualPanelDialog propsDialog;
	private DecimalFormat nf = new DecimalFormat();
	private Logger logger = Logger.getLogger(DynamicModelWizard.class);
	
	public String getNewModelName(YorkBrowser browser, String oldName){
		
		logger.debug("getNewModelName()");
		
		propsDialog = new IndividualPanelDialog(browser, 
				new TextFieldPanel(oldName, 23, "Model Name", 
						"New name:", "newName"), "Define model name", new Dimension(320, 150));
		
		propsDialog.showDialog();		
		
		if(wasCancelled()){
			return null;
		}
		else{
			// Validate the given name
			String newName = (String) propsDialog.getResultsMap().get("newName");
			if(!validateWithDialog(browser, newName)){
				// It was invalid, so recurse
				return getNewModelName(browser, oldName);
			}
			return newName;
		}
	}
	/**
	 * Ensure that the chosen name is permissible. The new name cannot be null, be a
	 * number, have one of the recognised operators (+, - etc). cannot have ":" (this
	 * is used in the construction of keys in the SpinnerListPanel. 
	 *   
	 * @param browser
	 * @param newName
	 * @return
	 */
	public static boolean validateWithDialog(YorkBrowser browser, String newName){

		// Validate the given name
		if(newName.length() == 0){
			JOptionPane.showMessageDialog(browser, "Blank names are not allowed", "Invalid Name", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if(newName.indexOf(":") != -1){
			JOptionPane.showMessageDialog(browser, "The character \":\" is not allowed in a name", "Invalid Name", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		// Tokenise the name as the EquationRunner would, if it splits it then the new name
		// must contain one of the operator strings - making the name invalid.
		String[] tokenized  = StringUtil.retainingSplit(newName, ExpressionRunner.OPERATORS);
		if(tokenized.length > 1){
			String message[] = new String[3];
			message[0] = "New name is not valid.";
			message[1] = "Name must not contain any of these characters:";
			StringBuffer buffer = new StringBuffer();
			for(int i = 0; i < ExpressionRunner.OPERATORS.length; i++){
				buffer.append(ExpressionRunner.OPERATORS[i]).append(" ");
			}
			message[2] = buffer.toString();
			JOptionPane.showMessageDialog(browser, message, "Invalid Name", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		// Check it's not a number
		try{
			Double.parseDouble(newName);
			JOptionPane.showMessageDialog(browser, "New name must not be a number", "Error", JOptionPane.ERROR_MESSAGE);	
			return false;
		}
		catch(NumberFormatException nfe){
			// It's not a number - therefore OK
		}
		
		// Check that none of the words are the same as a function name
		String[] words = newName.split(" ");
		for(String word: words){
			if(ExpressionRunner.FUNCTION_NAMES.contains(word)){
				JOptionPane.showMessageDialog(browser, "\"" + word + "\"  is not allowed as part of a node's name", 
						"Invalid Name", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}		
		
		return true;
	}
	
	public DynamicModel getNewNamedModel(YorkBrowser browser){
		
		String idString = getNewModelName(browser, "");

		if(wasCancelled()){
			return null;
		}
		else{
			DynamicModel dModel = new DynamicModel(idString);
			//dModel.setPropertiesMemento(browser.getProperties());
			return dModel;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Boolean> getStartNodes(YorkBrowser browser, DynamicModel dModel){

		List<String> startNodes = dModel.getStartNodes();
		Map<String, Boolean> setupMap = new LinkedHashMap<String, Boolean>();
		for(String nodeId: dModel.getNodeIds()){
			if(startNodes.contains(nodeId)){
				setupMap.put(nodeId, true);
			}
			else{
				setupMap.put(nodeId, false);
			}
		}
		propsDialog = new IndividualPanelDialog(browser, 
				new CheckBoxPanel("Start Nodes", "key", setupMap), 
				"Select start nodes", new Dimension(240, 270));

		propsDialog.showDialog();	
		
		if(wasCancelled()){
			return null;
		}
		else{				
			return (Map<String, Boolean>) propsDialog.getResultsMap().get("key");
		}
	}
		
	public int getIterationCount(YorkBrowser browser){

		propsDialog = new IndividualPanelDialog(browser, 
				new TextFieldPanel("", 23, "End time", 
						"New end time:", "time"),"Define end time", new Dimension(320, 150));
		
		propsDialog.showDialog();		

		if(wasCancelled()){
			return 0;
		}
		else{

			String endTimeString = (String) propsDialog.getResultsMap().get("time");				

			// Some simple validation
			int iterationCount = 0;
			long maxAllowed = 1000000;
			try {
				iterationCount = Integer.parseInt(endTimeString);	
				if(iterationCount > maxAllowed){
					nf.setGroupingSize(3);
					JOptionPane.showMessageDialog(browser, "Given number is too large. Please enter a positive whole number (max value " + nf.format(maxAllowed) + ")", "Invalid Input", JOptionPane.ERROR_MESSAGE);
					return getIterationCount(browser);				
				}
			} catch (Exception e) {
				// Get's here if the user has entered a non-Integer
				JOptionPane.showMessageDialog(browser, "\"" + endTimeString + "\" is not a positive whole number. Please enter a positive whole number (max value " + nf.format(maxAllowed) + ")", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				return getIterationCount(browser);
			}
			// Ensure is it positive
			if(iterationCount < 1){
				JOptionPane.showMessageDialog(browser, "The number of iterations should be greater than zero", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				return getIterationCount(browser);
			}

			return iterationCount;
		}
	}
	
	public boolean wasCancelled(){
		return propsDialog.getWasCancelled();
	}

}
