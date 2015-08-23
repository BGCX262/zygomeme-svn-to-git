package com.zygomeme.york.propertiesdialog;


import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

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
 * Panel for the properties dialog. This panel is used for list props.  
 * 
 */ 

public class ListPanel implements MappedPanelCreator{

	private Logger logger = Logger.getLogger(ListPanel.class);
	private String panelTitle;
	private String key = "";
	private MappedPanel mappedPanel;
	private String[] list;
	private JList nodeList;
	private String id = "key";

	public ListPanel(String panelTitleIn, String key, String[] listIn){
		this.panelTitle = panelTitleIn;
		this.key = key;
		this.list = listIn;
	}

	public String getKey(){
		return key;
	}

	public MappedPanel getPanel(){

		// Titled border
		mappedPanel = new MappedPanel();		
		
		nodeList = new JList(list);
		nodeList.setFixedCellWidth(100);
		
		JScrollPane scrollPane = new JScrollPane(nodeList);
		scrollPane.setPreferredSize(new Dimension(120, 145));
		mappedPanel.addMappedComponent(scrollPane, id);		
		mappedPanel.addMappedComponent(new JLabel("Tip: Hold down the Ctrl key "), "tip1");		
		mappedPanel.addMappedComponent(new JLabel("to select mulitple nodes."), "tip2");		
		
		// Titled border
		mappedPanel.setBorder(BorderFactory.createTitledBorder(panelTitle));
		return mappedPanel;
	}

	/**
	 * This method is used to take the results from the UI component and place them in the 
	 * results map. 
	 */
	public void setResults() {

		logger.info("setResults()");

		Object[] selectedObjects = nodeList.getSelectedValues();
		// Can't cast so has to be taken out one at a time!
		String[] selectedStrings = new String[selectedObjects.length];
		int i = 0;
		for(Object s: selectedObjects){
			selectedStrings[i] = s.toString();
			i++;
		}
		mappedPanel.setResult(id, selectedStrings);
	}
}
