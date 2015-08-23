package com.zygomeme.york.propertiesdialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

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
 * Panel for the properties dialog. This panel shows information regarding
 * this part of the tree.
 * 
 */

public class InfoPanel implements MappedPanelCreator, ActionListener{

	private MappedPanel panel = new MappedPanel();
	
	public MappedPanel getPanel() {
		panel = new MappedPanel();

		JLabel label = new JLabel("Expand items on the tree and select your perferences.");
		
		panel.setPreferredSize(new Dimension(330, 200));
		panel.add(label);
		
		return panel;
	}

	public void setResults() {
		// Not applicable for a information panel
	}

	public void actionPerformed(ActionEvent arg0) {
		// Not applicable for a information panel
	}
	
	public String getKey() {
		// Not applicable for a information panel
		return null;
	}
}
