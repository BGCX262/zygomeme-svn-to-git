package com.zygomeme.york.propertiesdialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
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
 * Panel for the properties dialog. This panel is used for color selection. 
 * 
 */

public class ColorPanel implements MappedPanelCreator, ActionListener{

	private String frameTitle = "";
	private String labelText = "";
	private String key = "";
	private MappedPanel panel = new MappedPanel();
	private JLabel colorBox = new JLabel();
	private int previewBoxSize = 200;
	private PreviewPanel previewBox = new PreviewPanel(previewBoxSize);
	private Color currentColor;

	public final static String COLOR = "color";

	public ColorPanel(String frameTitle, String labelText, String key, Color previousColor){
		this.frameTitle = frameTitle;
		this.labelText = labelText;
		this.key = key;
		this.currentColor = previousColor;
	}

	public String getKey(){
		return key;
	}

	public MappedPanel getPanel(){						 

		panel = new MappedPanel();

		// Titled border
		panel.setBorder(BorderFactory.createTitledBorder(frameTitle));

		Box topLine = Box.createHorizontalBox(); 
		topLine.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JLabel label = new JLabel(labelText);
		label.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		topLine.add(label);

		colorBox.setText("      ");
		colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		colorBox.setBackground(currentColor);
		colorBox.setOpaque(true);
		colorBox.setPreferredSize(new Dimension(128, 14));
		colorBox.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		topLine.add(Box.createRigidArea(new Dimension(26,0)));
		topLine.add(colorBox);

		JButton editButton = new JButton("Choose...");
		editButton.addActionListener(this);
		topLine.add(Box.createRigidArea(new Dimension(10,0)));
		topLine.add(editButton);

		Box lines = Box.createVerticalBox();
		lines.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		lines.add(topLine);
		lines.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(lines);

		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		panel.setPreferredSize(new Dimension(330, 200));
		//panel.setBackground(Color.red);
		return panel;
	}

	public void actionPerformed(ActionEvent e) { 

		Color newColor = JColorChooser.showDialog(panel, "Choose Color", Color.white);		

		colorBox.setBackground(newColor);
		currentColor = newColor;
		previewBox.setCurrentTopColor(currentColor);

		previewBox.repaint();
	}

	public void setResults(){
		panel.setResult(key, currentColor);
	}
}
