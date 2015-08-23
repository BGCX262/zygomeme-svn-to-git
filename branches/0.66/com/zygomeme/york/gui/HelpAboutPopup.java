package com.zygomeme.york.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
 * This dialog is shown when the user clicks on the "Help -> About" menu item
 * 
 */
public class HelpAboutPopup extends JDialog implements ActionListener{

	private static final long serialVersionUID = -8397969422529794631L;
	private int w = 240; 
	private int h = 280;
	private JFrame parent;
	private PropertiesMemento propertiesMemento;

	private String[] text;
		
	public HelpAboutPopup(JFrame parentIn, PropertiesMemento propertiesMementoIn){
		super(parentIn);
		this.parent = parentIn;
		this.propertiesMemento = propertiesMementoIn;
	}
	
	public void init(){
		
		text = new String[]{"ZygoMeme York", 
				"Version " + PropertiesMemento.APP_VERSION_STRING  + " " + PropertiesMemento.APP_STAGE, 
				"Copyright 2008-2009", ""};

		setSize(new Dimension(w, h));
		setTitle("About ZygoMeme York");
		text[text.length - 1] = "Build Date: Wed Jun 10 16:05:02";
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		JPanel imagePanel = new JPanel();

		JLabel image = new JLabel(new ImageIcon(propertiesMemento.getImageDir() + "help about.png"));		
		imagePanel.add(image);		
		mainPanel.add(Box.createRigidArea(new Dimension(w / 3, -5)));
		mainPanel.add(imagePanel);
		add(mainPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(w / 3, 0)));
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.PAGE_AXIS));
		for(String line: text){
			JLabel label = new JLabel(line);
			textPanel.add(label);
		}
		textPanel.setBackground(Color.WHITE);
		textPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(textPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(w / 3, 14)));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(220, 220, 255));
		JButton closeButton = new JButton("OK");
		closeButton.addActionListener(this);
		buttonPanel.add(closeButton);
		mainPanel.add(buttonPanel);		
		
		imagePanel.setBackground(Color.WHITE);
		mainPanel.setBackground(Color.WHITE);
		
		if(parent != null){
			setLocation(parent.getLocation().x + (parent.getWidth() / 2) - (w / 2), 
					parent.getLocation().y + (parent.getHeight() / 2) - (h / 2));
		}
	}
	
	public void showDialog() {

		init();
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
	}	
}
