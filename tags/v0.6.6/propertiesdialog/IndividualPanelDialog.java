package com.zygomeme.york.propertiesdialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
 * Pop up for setting the props of a selected entity. 
 * This class is used by many others 
 * 
 */

public class IndividualPanelDialog extends JDialog implements ActionListener{

	private Logger logger = Logger.getLogger(IndividualPanelDialog.class);
	private static final long serialVersionUID = -3649106168882804203L;
	private boolean dialogCancelled = true;
	private MappedPanelCreator mappedPanelCreator;
	private MappedPanel mappedPanel;
	private String title = "ZygoMeme York";

	public IndividualPanelDialog(JFrame parentFrame, MappedPanelCreator mappedPanelCreatorIn, String newTitle){
		super(parentFrame, true);
		this.mappedPanelCreator = mappedPanelCreatorIn;
		this.title = newTitle;
	}

	public IndividualPanelDialog(JFrame parentFrame, MappedPanelCreator mappedPanelCreatorIn, String newTitle, Dimension minimumSize){
		this(parentFrame, mappedPanelCreatorIn, newTitle);
		setMinimumSize(minimumSize);
	}

	public MappedPanelCreator getMappedPanelCreator(){
		return mappedPanelCreator;
	}
		
	public void showDialog(){
		
		setTitle(title);
		mappedPanel = mappedPanelCreator.getPanel();
		logger.debug("showDialog() component map:\"" + mappedPanel.getMappedComponents() + "\"");
		add(mappedPanel);

		// Lay out buttons from left to right.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 10));
		buttonPane.add(Box.createHorizontalGlue());

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);

		JButton setButton = new JButton("Set");
		setButton.addActionListener(this);

		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(setButton);
		
		add(buttonPane, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
		super.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {

		setVisible(false);		

		if ("Set".equals(e.getActionCommand())) {
			dialogCancelled = false;
			mappedPanelCreator.setResults();
		}
		else{
			dialogCancelled = true;
		}	
		dispose();
	}
	
	public boolean getWasCancelled(){
		return dialogCancelled;
	}
	
	public Map<String, Object> getResultsMap(){
		return mappedPanel.getResultsMap();
	}	
}
