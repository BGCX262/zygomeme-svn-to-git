package com.zygomeme.york.propertiesdialog;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
 * Panel for the properties dialog. This panel is used of check box props.  
 * 
 */

public class CheckBoxPanel implements MappedPanelCreator{

	private String panelTitle;
	private String key = "";
	private MappedPanel mappedPanel;
	private Map<String, Boolean> setupMap;
	private Map<String, JCheckBox> checkBoxes = new HashMap<String, JCheckBox>(); 

	
	public CheckBoxPanel(String panelTitleIn, String key, Map<String, Boolean> newSetupMap){
		this.panelTitle = panelTitleIn;
		this.key = key;
		this.setupMap = newSetupMap;
	}

	public String getKey(){
		return key;
	}

	public MappedPanel getPanel(){

		// Titled border
		mappedPanel = new MappedPanel();

				
		JPanel checkBoxInnerPanel = new JPanel();
		checkBoxInnerPanel.setLayout(new BoxLayout(checkBoxInnerPanel, BoxLayout.Y_AXIS));
		checkBoxInnerPanel.setBackground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(checkBoxInnerPanel);		
		scrollPane.setPreferredSize(new Dimension(120, 145));
		
		for(String id: setupMap.keySet()){
			JCheckBox checkButton = new JCheckBox(id);
			checkButton.setBackground(Color.WHITE);
			//if(currentModifierIds.contains(id)){
			if(setupMap.get(id)){
				checkButton.setSelected(true);
			}
			else{
				checkButton.setSelected(false);	 
			}
			checkButton.setActionCommand(id);
			checkBoxInnerPanel.add(checkButton);
			checkBoxes.put(id, checkButton);
		}

		mappedPanel.add(scrollPane);
		
		// Titled border
		mappedPanel.setBorder(BorderFactory.createTitledBorder(panelTitle));
		mappedPanel.setPreferredSize(new Dimension(50, 200));
		return mappedPanel;
	}

	public void setResults() {

		Map<String, Boolean> resultsMap = new LinkedHashMap<String, Boolean>();
		for(String id: checkBoxes.keySet()){
			resultsMap.put(id, checkBoxes.get(id).isSelected());
		}
		mappedPanel.setResult(key, resultsMap);				
	}
}
