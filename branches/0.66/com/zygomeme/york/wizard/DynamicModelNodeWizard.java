package com.zygomeme.york.wizard;

import java.awt.Dimension;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;


import com.zygomeme.york.dynamicmodels.DynamicModelNode;
import com.zygomeme.york.gui.YorkBrowser;
import com.zygomeme.york.propertiesdialog.IndividualPanelDialog;
import com.zygomeme.york.propertiesdialog.TextFieldPanel;

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
 * Wizard for getting Dynamic Model related input from the user. Relies heavily on IndividualPanelDialog.  
 * 
 */
public class DynamicModelNodeWizard {

	private IndividualPanelDialog propsDialog;
	private Logger logger = Logger.getLogger(DynamicModelWizard.class); 

	public String getNewNamedNode(YorkBrowser browser){

		return getNodeName(browser, "");
	}

	public String getNodeName(YorkBrowser browser, String idString){

		propsDialog = new IndividualPanelDialog(browser, 
										new TextFieldPanel(idString, 27, "Define the node name", "New name", "name"), 
										"Define name", new Dimension(320, 150));
		propsDialog.showDialog();		

		if(propsDialog.getWasCancelled()){
			return null;
		}
		else{
			// Validate the given name
			String newName = (String) propsDialog.getResultsMap().get("name");
			logger.debug("newName:" + newName);
			if(!DynamicModelWizard.validateWithDialog(browser, newName)){
				// It was invalid, so recurse
				return getNodeName(browser, idString);
			}
			return newName;
		}
	}

	public double getInitialValue(YorkBrowser browser){

		String title = "Set the initial value";
		String label = "New initial value: ";
		String frameTitle = "Edit initial value";

		propsDialog = new IndividualPanelDialog(browser, 
												new TextFieldPanel("", 14, title, label, "value"), 
												frameTitle, new Dimension(290, 150));
		propsDialog.showDialog();		

		if(propsDialog.getWasCancelled()){
			return 0.0;
		}
		else{
			// Validate input
			String input = (String)propsDialog.getResultsMap().get("value");
			try {
				return Double.parseDouble(input);				
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(browser, "\"" + input + "\" is not a valid number", "Invalid Number", JOptionPane.ERROR_MESSAGE);
				return getInitialValue(browser); //recurse
			} 
		}
	}

	/**
	 * Gets the expression as defined by the user. Validation is made in the client code. 
	 * 
	 * @param browser
	 * @param dNode
	 * @return
	 */
	public String getExpression(YorkBrowser browser, DynamicModelNode dNode){

		propsDialog = new IndividualPanelDialog(browser, 
												new TextFieldPanel(dNode.getExpression(), 27, "Define the expression", 
												dNode.getIdString() + " = ", "expression"), 
												"Define expression", new Dimension(390, 150));
		propsDialog.showDialog();		

		if(propsDialog.getWasCancelled()){
			return null;
		}
		else{
			return (String) propsDialog.getResultsMap().get("expression");
		}
	}

	public boolean wasCancelled(){
		if(propsDialog == null){
			return false;
		}
		else{
			return propsDialog.getWasCancelled();
		}
	}
}
