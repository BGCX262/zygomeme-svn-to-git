package com.zygomeme.york.propertiesdialog;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
 * Panel for the input of Model Pattern information - used by the Model 
 * Pattern Wizard.  
 * 
 */
public class ModelPatternPanel implements MappedPanelCreator{

	private int columnWidth = 38;
	private MappedPanel mappedPanel;
	private String frameTitle;
	private String key = "key1";
	
	public ModelPatternPanel(String frameTitleIn){
		this.frameTitle = frameTitleIn;
	}

	public String getKey() {
		return key;
	}

	public MappedPanel getPanel() {

		mappedPanel = new MappedPanel();
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2,1));
		
		JPanel inputPanel = new JPanel();

		// Inputs
		JTextArea inputArea = new JTextArea();
		inputArea.setColumns(columnWidth);
		inputArea.setRows(5);
		inputArea.setName(key);
		mappedPanel.mapComponent(inputArea, "input");
		
		JScrollPane inputScrollPane = new JScrollPane(inputArea);
		// Notice the extra spaces at the start of the label to make it line up with 
		// the "Outputs: " label !!
		JLabel inputLabel = new JLabel("   Inputs: ");
		
		inputPanel.add(inputLabel);
		inputPanel.add(inputScrollPane);
		mainPanel.add(inputPanel);

		// Outputs
		JPanel outputPanel = new JPanel();
		JTextArea outputArea = new JTextArea();
		outputArea.setColumns(columnWidth);
		outputArea.setRows(5);
		outputArea.setName(key);
		mappedPanel.mapComponent(outputArea, "output");
		
		JScrollPane outputScrollPane = new JScrollPane(outputArea);
		JLabel outputLabel = new JLabel("Outputs: ");
		outputPanel.add(outputLabel);
		outputPanel.add(outputScrollPane);
		mainPanel.add(outputPanel);
				
		// Titled border
		mappedPanel.setBorder(BorderFactory.createTitledBorder(frameTitle));
		
		//mappedPanel.add(inputLabel);
		mappedPanel.addMappedComponent(mainPanel, key);
		mappedPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		return mappedPanel;
	}

	public void setResults() {
		
		JComponent component;
		for(String id: mappedPanel.getMappedComponents().keySet()){
			component = mappedPanel.getMappedComponents().get(id);

			if(component instanceof JTextArea){
				mappedPanel.setResult(id, ((JTextArea)component).getText());
			}
		}
	}
	
}
