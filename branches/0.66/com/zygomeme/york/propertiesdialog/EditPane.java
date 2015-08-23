package com.zygomeme.york.propertiesdialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.text.JTextComponent;

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
 * Used for the configuration and set-properties dialogs.    
 * 
 */

public class EditPane extends JDialog implements ActionListener
{
	private final static long serialVersionUID = 91498124984L;
	private Logger logger = Logger.getLogger(EditPane.class);
	
	private Map<String, JTextComponent> textComponents = new LinkedHashMap<String, JTextComponent>();
	private Map<String, String> panes = new HashMap<String, String>();
	private Map<String, JComponent> components = new LinkedHashMap<String, JComponent>(); 
	private Map<String, MappedPanelCreator> mappedPanelCreators = new LinkedHashMap<String, MappedPanelCreator>(); 
	private String introText = null;
	private boolean dialogCancelled = false;
	private JTree tree = null;
	private JPanel editPane = new JPanel();

	public EditPane(Frame frame) {
		super(frame, "Edit object", true);
	}

	public EditPane(Frame frame, String title) {
		super(frame, title, true);
	}
	
	public void setMappedPanelCreator(String key, MappedPanelCreator creator){
		mappedPanelCreators.put(key, creator);
	}

	public void setTree(JTree newSelectionTree){
		tree = newSelectionTree;
	}
	
	public void addComponent(String id, JComponent newComponent){
		components.put(id, newComponent);		
	}
	
	public Map<String, JComponent> getMappedPanels(){
		return components;
	}
	
	public void setIntroText(String newIntroText){
		introText = newIntroText;
	}
		
	public void init() {

		//Create and initialise the buttons.
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		final JButton setButton = new JButton("Set");
		setButton.setActionCommand("Set");
		setButton.addActionListener(this);
		getRootPane().setDefaultButton(setButton);
		
		// Lay out buttons from left to right.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(cancelButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(setButton);

		editPane.setLayout(new BoxLayout(editPane, BoxLayout.Y_AXIS));

		// Add the introduction text if available
		if(introText != null){
			JLabel label = new JLabel(introText);
			label.setFont(new Font(label.getFont().getFamily(), Font.BOLD, label.getFont().getSize()));
			label.setAlignmentX(LEFT_ALIGNMENT);
			editPane.add(label);
		}
	
		// Add the given components
		for(JComponent component: components.values()){
			component.setAlignmentX(LEFT_ALIGNMENT);
			editPane.add(component);
		}
		
		Container contentPane = getContentPane();
		if(tree != null){
			JPanel treeAndEditPane = new JPanel();
			treeAndEditPane.setLayout(new BoxLayout(treeAndEditPane, BoxLayout.X_AXIS));

			JScrollPane scrollPane = new JScrollPane(tree);
			Dimension minimumSize = new Dimension(100, 50);
			scrollPane.setMinimumSize(minimumSize);
			scrollPane.setPreferredSize(new Dimension(210, 300));
			treeAndEditPane.add(scrollPane);
			treeAndEditPane.add(editPane);
			contentPane.add(treeAndEditPane, BorderLayout.CENTER);
			setMinimumSize(new Dimension(610, 440));
		}
		else{
			contentPane.add(editPane, BorderLayout.CENTER);
		}
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
	}

	public void actionPerformed(ActionEvent e) {

		if ("Set".equals(e.getActionCommand())) {
			dialogCancelled = false;

			// Put the new text into the map that will be sampled by the client code
			for(String textAreaName: textComponents.keySet()){
				panes.put(textAreaName, textComponents.get(textAreaName).getText());
			}

			// Do the same for the components
			JComponent component;
			for(String id: components.keySet()){
				component = components.get(id);
				logger.info("actionPerformed() wiz component:" + component.getClass().getName());

				if(component instanceof MappedPanel){
					logger.info("MappedPanel found");
					// Get creator 
					MappedPanelCreator creator = mappedPanelCreators.get(id);
					creator.setResults();
				}
			}
		}
		else{
			dialogCancelled = true;
		}
		setVisible(false);		
	}

	public void addEditPane(MappedPanel mappedPanel, String key, MappedPanelCreator creator){
		editPane.add(mappedPanel);
		addComponent(key, mappedPanel);
		mappedPanelCreators.put(key, creator);
	}

	public void showDialog() {
		init();
		setVisible(true);
	}

	public boolean getWasCancelled(){
		return dialogCancelled;
	}
	
	public void addTextPane(String key, String value){
		panes.put(key, value);
		textComponents.put(key, new JTextArea());
	}

	public void addTextPane(String key, String value, JTextComponent textComponent){
		panes.put(key, value);
		textComponents.put(key, textComponent);
	}

	public JPanel getEditPane(){
		return editPane;
	}
	
	public Map<String, String> getMap(){
		return panes;
	}	
}
