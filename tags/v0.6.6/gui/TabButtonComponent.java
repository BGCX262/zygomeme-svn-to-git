package com.zygomeme.york.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
 * Draws the title and button on the tabs in the view 
 * 
 */

public class TabButtonComponent extends JPanel implements ActionListener{

	private static final long serialVersionUID = -5786738361747162325L;
	private TabView tabView;
	private JLabel label = new JLabel();
	private ActionListener listener;

	public TabButtonComponent(String imageDir, ActionListener listenerIn, TabView tabViewIn){

		this.listener = listenerIn;
		this.tabView = tabViewIn;
		
		JButton newButton = new JButton(new ImageIcon(imageDir + "close_button.png"));
		int size = 16;
		newButton.setPreferredSize(new Dimension(size, size));
		newButton.setToolTipText("Close this tab");
		newButton.setRolloverEnabled(true);
		newButton.setFocusable(false);
		newButton.setBorder(BorderFactory.createEtchedBorder());
		newButton.setBorderPainted(false);

		newButton.addActionListener(this);
		newButton.setActionCommand(YorkBrowser.CLOSE_TAB_COMMAND);
		
		if(tabView != null){
			label = new JLabel(tabView.getViewModel().getName());
		}
		else{
			label = new JLabel("New Tab");
		}
		add(label);
		add(newButton);
		//	alter the space to the top of the component
		setBorder(BorderFactory.createEmptyBorder(-3, 0, -5, 0));
	}
	
	public void actionPerformed(ActionEvent e){
		e.setSource(this);
		listener.actionPerformed(e);
	}
	
	public void setTitle(String newTitle){
		tabView.getViewModel().setName(newTitle);
		label.setText(newTitle);
	}
	
	public String getTitle(){
		return tabView.getViewModel().getName();
	}
	
	public TabView getTabView(){
		return tabView;
	}

}
