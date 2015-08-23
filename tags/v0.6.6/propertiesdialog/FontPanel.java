package com.zygomeme.york.propertiesdialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

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
 * Panel for the properties dialog. This panel is used for font selection. 
 * 
 */

public class FontPanel implements MappedPanelCreator, ActionListener{

	private String frameTitle = "";
	private String key = "";
	private MappedPanel panel = new MappedPanel();
	private String currentFont;
	private JLabel previewText = new JLabel("ZygoMeme York");
	private JComboBox fontList;
	private Color selectedBackgroundColor = new Color(10, 36, 106);

	public final static String COLOR = "color";

	public FontPanel(String frameTitle, String key, String previousFontName){
		this.frameTitle = frameTitle;
		this.key = key;
		this.currentFont = previousFontName;
		this.previewText.setBackground(Color.WHITE);
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

		JLabel label = new JLabel("Font:");
		label.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		topLine.add(label);

		topLine.add(Box.createRigidArea(new Dimension(12,0)));
		
		// Get all font family names
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    String fontNames[] = ge.getAvailableFontFamilyNames();
	    
	    // Filter those that don't show - 
	    List<String> filteredFonts = new LinkedList<String>();
	    for(String name: fontNames){
	    	if(name.indexOf("Wingdings") != -1 || name.indexOf("Webdings") != -1 || name.indexOf("Symbol") != -1
	    			|| name.indexOf("MV Boli") != -1 || name.indexOf("Marlett") != -1){
	    		// Want to filter out
	    	}
	    	else{
	    		filteredFonts.add(name);
	    	}
	    }
	    
		fontList = new JComboBox(filteredFonts.toArray());
		fontList.addActionListener(this);
		fontList.setPreferredSize(new Dimension(240, 20));
		ComboBoxRenderer renderer= new ComboBoxRenderer();
		fontList.setRenderer(renderer);		
		topLine.add(fontList);

		// Preview				
		Box previewLine = Box.createHorizontalBox(); 
		previewLine.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		previewText.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JLabel exampleLabel = new JLabel("Example:");
		exampleLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		previewLine.add(exampleLabel);
		previewText.setOpaque(true);
		previewText.setBackground(Color.WHITE);
		
		JPanel previewPanel = new JPanel();
		previewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		previewPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		previewPanel.setBackground(Color.WHITE);
		previewPanel.add(previewText);
		previewPanel.setMaximumSize(new Dimension(220, 30));
		previewPanel.setPreferredSize(new Dimension(220, 30));
		previewLine.add(previewPanel);

		Box lines = Box.createVerticalBox();
		lines.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		lines.add(topLine);
		lines.add(Box.createRigidArea(new Dimension(0, 30)));
		lines.add(previewLine);
		panel.add(lines);

		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		panel.setPreferredSize(new Dimension(330, 200));
		return panel;
	}

	/**
	 * Cell renderer used to demonstrate the font in the combo box
	 *
	 */
	private class ComboBoxRenderer extends JLabel implements ListCellRenderer {

		private static final long serialVersionUID = -6445825144480583836L;

		public ComboBoxRenderer() {
            setOpaque(true);
            setVerticalAlignment(CENTER);
        }

		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {

			//Set the icon and text.  If icon was null, say so.
			String fontName = (String) value;
			setText(fontName);
			setFont(new Font(fontName, Font.PLAIN, 14));
			
			// Not as neat as I'd like - there seems to be a problem with Java 
			// accepting requests to change the background colour
			if (isSelected) {
				setBackground(selectedBackgroundColor);
				setForeground(Color.WHITE);
			} else {
				setBackground(Color.WHITE);
				setForeground(Color.BLACK);
			}

			return this;
		}

	}

	public void actionPerformed(ActionEvent e) { 

		String fontName = (String)((JComboBox)e.getSource()).getSelectedItem();
		previewText.setFont(new Font(fontName, Font.PLAIN, 12));
		fontList.setSelectedItem(fontName);
		currentFont = fontName;
	}

	public void setResults(){
		panel.setResult(key, currentFont);
	}
}
