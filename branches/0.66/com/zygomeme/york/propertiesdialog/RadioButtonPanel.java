package com.zygomeme.york.propertiesdialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.GroupLayout.ParallelGroup;

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
 * Panel for showing Radio buttons for property dialogs
 * 
 */
public class RadioButtonPanel implements MappedPanelCreator, ActionListener{

	private String panelTitle;
	private String key = "";
	private MappedPanel mappedPanel;
	private String[] list;
	private ButtonGroup group;
	private Map<String, Object> buttonMap;

	public RadioButtonPanel(String panelTitleIn, String key, Map<String, Object> buttonMapIn){
		this.panelTitle = panelTitleIn;
		this.key = key;
		this.buttonMap = buttonMapIn;
		this.list = buttonMap.keySet().toArray(new String[]{});
	}

	public String getKey() {
		return key;
	}

	public MappedPanel getPanel() {
				
		JPanel panel = new JPanel();
		
		mappedPanel = new MappedPanel();
		mappedPanel.setBorder(BorderFactory.createTitledBorder(panelTitle));
		
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		group = new ButtonGroup();
		JLabel[] labels = new JLabel[list.length];
		JRadioButton[] buttons = new JRadioButton[list.length];

		// Create the buttons and the labels
		int i = 0;
		for(String itemName: list){
			labels[i] = new JLabel(itemName);			
			buttons[i] = new JRadioButton();
		    group.add(buttons[i]);
		    buttons[i].addActionListener(this);
		    buttons[i].setActionCommand(itemName);
		    i++;
		}
		
		// GroupLayout works by adding the components to horizontal groups and vertical groups
		// and placing the components accordingly. 
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

		// The labels are part of a parallel group that are stacked at the same X position 
		ParallelGroup hpGroup = layout.createParallelGroup(); 		
		for(JLabel label: labels){
			hpGroup.addComponent(label);
		}		
		hGroup.addGroup(hpGroup);
		
		// The buttons are part of a parallel group that are stacked at the same X position
		ParallelGroup hpButtonGroup = layout.createParallelGroup(); 		
		for(JRadioButton button: buttons){
			hpButtonGroup.addComponent(button);
		}		
		hGroup.addGroup(hpButtonGroup);
		layout.setHorizontalGroup(hGroup);
		
		// The label button parallel group is layout in N parallel groups
		for(i = 0; i < list.length; i++){
			ParallelGroup vpGroup = layout.createParallelGroup(); 		
			vpGroup.addComponent(labels[i]);
			vpGroup.addComponent(buttons[i]);
			vGroup.addGroup(vpGroup);
		}		
		layout.setVerticalGroup(vGroup);

		panel.setPreferredSize(new Dimension(300, 325));
		mappedPanel.setPreferredSize(new Dimension(330, 200));
		mappedPanel.add(panel);
		return mappedPanel;
	}

	public void setResults() {
		// empty method as actionPerformed method already sets the results
	}

	public void actionPerformed(ActionEvent e) {
		mappedPanel.setResult(key, buttonMap.get(e.getActionCommand()));
	}
}
