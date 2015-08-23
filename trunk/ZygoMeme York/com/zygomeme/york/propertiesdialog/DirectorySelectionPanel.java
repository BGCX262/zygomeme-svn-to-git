package com.zygomeme.york.propertiesdialog;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


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
 * Panel for the properties dialog. This panel is used for text based input - typically names.  
 * 
 */

public class DirectorySelectionPanel implements MappedPanelCreator, ActionListener{

	private String initialText = "";
	private int columnWidth = 30;
	private String frameTitle = "";
	private String labelText = "";
	private String key = "";
	private MappedPanel mappedPanel;

	public DirectorySelectionPanel(String initialText, int columnWidth, String frameTitle, String labelText, String key){
		this.initialText = initialText;
		this.columnWidth = columnWidth;
		this.frameTitle = frameTitle;
		this.labelText = labelText;
		this.key = key;
	}

	public String getKey(){
		return key;
	}

	public MappedPanel getPanel(){

		JTextField nameArea = new JTextField(initialText);
		nameArea.setColumns(columnWidth);
		nameArea.setName(key);

		// Titled border
		mappedPanel = new MappedPanel();
		mappedPanel.setBorder(BorderFactory.createTitledBorder(frameTitle));
		JLabel label = new JLabel(labelText);
		label.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		mappedPanel.add(label);
		mappedPanel.addMappedComponent(nameArea, key);

		JButton browseButton = new JButton("Browse...");
		browseButton.addActionListener(this);
		mappedPanel.add(browseButton);
		
		mappedPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		mappedPanel.setPreferredSize(new Dimension(400, 200));
		return mappedPanel;
	}

	public void setResults() {

		JComponent component;
		for(String id: mappedPanel.getMappedComponents().keySet()){
			component = mappedPanel.getMappedComponents().get(id);

			if(component instanceof JTextField){
	
				String dirName = ((JTextField)component).getText();
				
			    // Check that it exists
			    File chosenDirectory = new File(dirName);
			    if(!chosenDirectory.exists()){
			    	JOptionPane.showMessageDialog(null, "\"" + dirName + "\" does not exist, would you like to create it?", "Information", JOptionPane.INFORMATION_MESSAGE);
			    	try{
			    		chosenDirectory.mkdir();
			    	}
			    	catch(SecurityException se){
			    		JOptionPane.showMessageDialog(null, "Unable to create that directory", "Error", JOptionPane.ERROR_MESSAGE);
			    		return;
			    	}
			    }
			    
			    // Ensure it ends with a slash
			    if(!dirName.endsWith("\\")){
			    	dirName = dirName + "\\";
			    }
				mappedPanel.setResult(id, dirName);
			}
		}				
	}
	
	public void actionPerformed(ActionEvent arg0) {

		JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Choose Directory");
		chooser.setApproveButtonText("Select");
	    int returnVal = chooser.showOpenDialog(null);
	    String filename = null;

	    if(returnVal == JFileChooser.APPROVE_OPTION) {

	    	filename = chooser.getSelectedFile().getAbsolutePath();
			if(filename != null){
				JComponent component = mappedPanel.getMappedComponents().get(key);
				((JTextField)component).setText(filename);
			}		
	    }
	    else{
	    	return;
	    }
		
	}
}
