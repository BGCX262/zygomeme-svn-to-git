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
 * Panel for the properties dialog. This panel is used for Color Gradients. 
 * 
 */

public class ColorGradientPanel implements MappedPanelCreator, ActionListener{

	private String frameTitle = "";
	private String labelText = "";
	private String key = "";
	private MappedPanel panel = new MappedPanel();
	private JLabel topColorBox = new JLabel();
	private JLabel bottomColorBox = new JLabel();
	private int previewBoxSize = 200;
	private PreviewPanel previewBox = new PreviewPanel(previewBoxSize);
	private Color currentTopColor;
	private Color currentBottomColor;
	
	public final static String BOTTOM_COLOR = "bottom color";
	public final static String TOP_COLOR = "top color";
	
	public ColorGradientPanel(String frameTitle, String labelText, String key, Color previousTopColor, Color previousBottomColor){
		this.frameTitle = frameTitle;
		this.labelText = labelText;
		this.key = key;
		this.currentTopColor = previousTopColor;
		this.currentBottomColor = previousBottomColor;
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

		topColorBox.setText("      ");
		topColorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		topColorBox.setBackground(currentTopColor);
		topColorBox.setOpaque(true);
		topColorBox.setPreferredSize(new Dimension(128, 14));
		topColorBox.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		topLine.add(Box.createRigidArea(new Dimension(26,0)));
		topLine.add(topColorBox);

		JButton editButton = new JButton("Choose...");
		editButton.addActionListener(this);
		topLine.add(Box.createRigidArea(new Dimension(10,0)));
		topLine.add(editButton);

		Box bottomLine = Box.createHorizontalBox(); 
		bottomLine.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JLabel bottomLabel = new JLabel("Bottom Color:");
		bottomLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		bottomLine.add(bottomLabel);

		bottomColorBox.setText("      ");
		bottomColorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		bottomColorBox.setBackground(currentBottomColor);
		bottomColorBox.setOpaque(true);
		bottomColorBox.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		bottomColorBox.setPreferredSize(new Dimension(28, 14));
		bottomLine.add(Box.createRigidArea(new Dimension(10,0)));
		bottomLine.add(bottomColorBox);

		JButton bottomEditButton = new JButton("Choose...");
		bottomEditButton.setActionCommand(BOTTOM_COLOR);
		bottomEditButton.addActionListener(this);
		bottomLine.add(Box.createRigidArea(new Dimension(10,0)));
		bottomLine.add(bottomEditButton);
		
		Box previewLine = Box.createHorizontalBox(); 
		previewLine.setAlignmentX(JPanel.LEFT_ALIGNMENT);		
		previewBox.setCurrentTopColor(currentTopColor);
		previewBox.setCurrentBottomColor(currentBottomColor);
		previewBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		previewBox.setMinimumSize(new Dimension(previewBoxSize, previewBoxSize));
		previewBox.setPreferredSize(new Dimension(previewBoxSize, previewBoxSize));
		previewBox.setMaximumSize(new Dimension(previewBoxSize, previewBoxSize ));

		previewBox.setOpaque(true);
		previewLine.add(previewBox);
		previewLine.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		Box lines = Box.createVerticalBox();
		lines.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		lines.add(topLine);
		lines.add(Box.createRigidArea(new Dimension(0, 10)));
		lines.add(bottomLine);
		lines.add(Box.createRigidArea(new Dimension(0, 10)));
		lines.add(new JLabel("Preview:"));
		lines.add(Box.createRigidArea(new Dimension(0, 10)));
		lines.add(previewLine);
		panel.add(lines);

		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		panel.setPreferredSize(new Dimension(330, 200));
		return panel;
	}
	
	public void actionPerformed(ActionEvent e) { 
		
		Color newColor = JColorChooser.showDialog(panel, "Choose Background Color", Color.white);		

		if(BOTTOM_COLOR.equals(e.getActionCommand())){
			bottomColorBox.setBackground(newColor);
			currentBottomColor = newColor;
			previewBox.setCurrentBottomColor(currentBottomColor);
		}
		else{
			topColorBox.setBackground(newColor);
			currentTopColor = newColor;
			previewBox.setCurrentTopColor(currentTopColor);
		}
				
		previewBox.repaint();
    }

	public void setResults(){
		panel.setResult(BOTTOM_COLOR, currentBottomColor);
		panel.setResult(TOP_COLOR, currentTopColor);
	}

}
