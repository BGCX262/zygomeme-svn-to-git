package com.zygomeme.york.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

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
 * Creates the menu for the main view.    
 * 
 */

public class MenuCreator {

	public enum Command{OPEN, EXIT, SAVE_TAB, NEW_TAB, NEW_PROJECT_LIST, EDIT_TAB_NAME, NEW,  
						NEW_DYNAMIC_NODE, NEW_DYNAMIC_MODEL, NEW_GRAPH, WINDOW, PERFERENCES, 
						HELP, HELP_ABOUT, HELP_INFO, HELP_LICENCE, SAVE_AS_IMAGE, EXPORT_RESULTS_TO_FILE, 
						EDIT, TOOLS, NESTED_LOOPS, INSERT};
	private static Map<Command, String> menuLabelMap = new HashMap<Command, String>();
	private static Map<Command, JMenuItem> menuItemMap = new HashMap<Command, JMenuItem>();
	
	public JPanel createMenu(ActionListener actionListener, PropertiesMemento propertiesMemento){

		// Initialise the menu map
		menuLabelMap.put(Command.OPEN, "Open");
		menuLabelMap.put(Command.EDIT, "Edit");
		menuLabelMap.put(Command.NEW, "New");
		menuLabelMap.put(Command.NEW_TAB, "New Tab");
		menuLabelMap.put(Command.EXPORT_RESULTS_TO_FILE, "Export Results To File...");
		menuLabelMap.put(Command.EXIT, "Exit");
		menuLabelMap.put(Command.SAVE_TAB, "Save Model");
		menuLabelMap.put(Command.EDIT_TAB_NAME, "Edit Model Name...");
		menuLabelMap.put(Command.NEW_DYNAMIC_NODE, "New Node...");
		menuLabelMap.put(Command.NEW_DYNAMIC_MODEL, "New Dynamic Model...");
		menuLabelMap.put(Command.WINDOW, "Window");
		menuLabelMap.put(Command.PERFERENCES, "Edit Defaults...");
		menuLabelMap.put(Command.HELP, "Help");
		menuLabelMap.put(Command.HELP_ABOUT, "About ZygoMeme York...");
		menuLabelMap.put(Command.HELP_LICENCE, "View Licence...");
		menuLabelMap.put(Command.HELP_INFO, "Help Information...");
		menuLabelMap.put(Command.SAVE_AS_IMAGE, "Save As Image...");
		menuLabelMap.put(Command.TOOLS, "Tools");
		menuLabelMap.put(Command.NESTED_LOOPS, "Configure Nested Loops...");
		menuLabelMap.put(Command.INSERT, "Insert");
	
		JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        menu1.setMnemonic(KeyEvent.VK_F);
        menu1.getAccessibleContext().setAccessibleDescription("File commands");
        menuBar.add(menu1);
        
        // Add file items        
        JMenu newSubmenu = new JMenu(menuLabelMap.get(Command.NEW)); 
        menuItemMap.put(Command.NEW, newSubmenu);
        menu1.add(newSubmenu);
        menu1.addSeparator();
        
        // Add the items it the "New" side menu 
        addItemToNewSideMenu(Command.NEW_TAB, newSubmenu, actionListener, KeyEvent.VK_T);
        
        JMenuItem loadItem = new JMenuItem(menuLabelMap.get(Command.OPEN), KeyEvent.VK_O);
        loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        loadItem.addActionListener(actionListener);
        loadItem.setActionCommand(menuLabelMap.get(Command.OPEN));
        menuItemMap.put(Command.OPEN, loadItem);
        menu1.add(loadItem);

        JMenuItem saveItem = new JMenuItem(menuLabelMap.get(Command.SAVE_TAB), KeyEvent.VK_S);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveItem.addActionListener(actionListener);
        menuItemMap.put(Command.SAVE_TAB, saveItem);
        menu1.add(saveItem);

        JMenuItem exportResultsToFile = new JMenuItem(menuLabelMap.get(Command.EXPORT_RESULTS_TO_FILE));
        exportResultsToFile.addActionListener(actionListener);
        menuItemMap.put(Command.EXPORT_RESULTS_TO_FILE, exportResultsToFile);
        menu1.add(exportResultsToFile);

        JMenuItem saveAsImageItem = new JMenuItem(menuLabelMap.get(Command.SAVE_AS_IMAGE));
        saveAsImageItem.addActionListener(actionListener);
        menuItemMap.put(Command.SAVE_AS_IMAGE, saveAsImageItem);
        menu1.add(saveAsImageItem);

        JMenuItem exitItem = new JMenuItem(menuLabelMap.get(Command.EXIT), KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        exitItem.addActionListener(actionListener);
        menu1.add(exitItem);

        JMenu insertMenu = new JMenu(menuLabelMap.get(Command.INSERT));
        menuItemMap.put(Command.INSERT, insertMenu);
        insertMenu.setMnemonic(KeyEvent.VK_I);
        insertMenu.getAccessibleContext().setAccessibleDescription("Insert items");
        menuBar.add(insertMenu);

        JMenuItem addNodeItem = new JMenuItem(menuLabelMap.get(Command.NEW_DYNAMIC_NODE));
        addNodeItem.addActionListener(actionListener);
        menuItemMap.put(Command.NEW_DYNAMIC_NODE, addNodeItem);
        insertMenu.add(addNodeItem);

        JMenu menu2 = new JMenu(menuLabelMap.get(Command.EDIT));
        menu2.setMnemonic(KeyEvent.VK_E);
        menu2.getAccessibleContext().setAccessibleDescription("Edit commands");
        menuItemMap.put(Command.EDIT, menu2);
        menuBar.add(menu2);

        JMenuItem editTabNameItem = new JMenuItem(menuLabelMap.get(Command.EDIT_TAB_NAME));
        editTabNameItem.addActionListener(actionListener);
        menuItemMap.put(Command.EDIT_TAB_NAME, editTabNameItem);
        menu2.add(editTabNameItem);

	//        JMenu menuTools = new JMenu(menuMap.get(Command.TOOLS));
	//        menuTools.setMnemonic(KeyEvent.VK_T);
	//        menuTools.getAccessibleContext().setAccessibleDescription("Tools selection");
	//        menuBar.add(menuTools);

//        JMenuItem nestedLoopsItem = new JMenuItem(menuMap.get(Command.NESTED_LOOPS));
//        nestedLoopsItem.setMnemonic(KeyEvent.VK_N);
//        nestedLoopsItem.addActionListener(actionListener);
//        menuTools.add(nestedLoopsItem);

        JMenu menuWindow = new JMenu(menuLabelMap.get(Command.WINDOW));
        menuWindow.setMnemonic(KeyEvent.VK_W);
        menuWindow.getAccessibleContext().setAccessibleDescription("Window related controls");
        menuBar.add(menuWindow);

        JMenuItem editPerferencesItem = new JMenuItem(menuLabelMap.get(Command.PERFERENCES), KeyEvent.VK_F);
        editPerferencesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        editPerferencesItem.addActionListener(actionListener);
        menuWindow.add(editPerferencesItem);

        JMenu menuHelp = new JMenu(menuLabelMap.get(Command.HELP));
        menuHelp.setMnemonic(KeyEvent.VK_H);
        menuHelp.getAccessibleContext().setAccessibleDescription("Help Information");
        menuBar.add(menuHelp);

        JMenuItem helpInfoItem = new JMenuItem(menuLabelMap.get(Command.HELP_INFO));
        helpInfoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpInfoItem.addActionListener(actionListener);
        menuHelp.add(helpInfoItem);

        JMenuItem helpLicence = new JMenuItem(menuLabelMap.get(Command.HELP_LICENCE));
        helpLicence.addActionListener(actionListener);
        menuHelp.add(helpLicence);

        JMenuItem helpAboutItem = new JMenuItem(menuLabelMap.get(Command.HELP_ABOUT));
        helpAboutItem.addActionListener(actionListener);
        menuHelp.add(helpAboutItem);

        //  Add the menu to the top panel
        JPanel topPanel = new JPanel();        
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        menuBar.add(Box.createHorizontalGlue());
        menuPanel.add(menuBar);
        menuPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuBar.setPreferredSize(new Dimension(1900, 25));
        topPanel.add(menuPanel);

        return topPanel;
	}
	
	private static void addItemToNewSideMenu(Command command, JMenu menu, ActionListener actionListener, 
			            int keyEvent){

        JMenuItem newItem = new JMenuItem(menuLabelMap.get(command));
        if(keyEvent != -1){
        	newItem.setAccelerator(KeyStroke.getKeyStroke(keyEvent, ActionEvent.CTRL_MASK));
        }
        menu.add(newItem);
        newItem.addActionListener(actionListener);
	}
	
	public Map<Command, JMenuItem> getMenuItemMap(){
		return menuItemMap;
	}
	
	public static String getActionCommand(Command command){
		return menuLabelMap.get(command);
	}
}
