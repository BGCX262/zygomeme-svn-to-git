package com.zygomeme.york.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

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
 * Handles the events generated when the user clicks on items in the menu, and events from 
 * using keyboard short cuts.    
 * 
 */

public class KeyAndMenuEventHandler implements ActionListener, KeyListener
{
	private YorkBrowser browser;
	private Logger logger = Logger.getLogger(KeyAndMenuEventHandler.class);

	public KeyAndMenuEventHandler(YorkBrowser browser){
		this.browser = browser;
	}
	
	public void actionPerformed(ActionEvent event){
		logger.info("Event:" + event.getActionCommand());
		
		// (Long winded way of avoiding having strings all over the code base)
		if(MenuCreator.getActionCommand(MenuCreator.Command.OPEN).equals(event.getActionCommand())){
			browser.openFile();
			return;
		}

		if(MenuCreator.getActionCommand(MenuCreator.Command.EXIT).equals(event.getActionCommand())){
			browser.allowUserToQuit();
			return;
		}
		
		if(MenuCreator.getActionCommand(MenuCreator.Command.SAVE_TAB).equals(event.getActionCommand())){
			browser.saveTab();
			return;
		}
		
		if(MenuCreator.getActionCommand(MenuCreator.Command.NEW_TAB).equals(event.getActionCommand())){
			browser.openTab(null);
			return;
		}

		if(MenuCreator.getActionCommand(MenuCreator.Command.EDIT_TAB_NAME).equals(event.getActionCommand())){
			browser.editTabName();
			return;
		}
		
		if(MenuCreator.getActionCommand(MenuCreator.Command.NEW_DYNAMIC_NODE).equals(event.getActionCommand())){
			browser.createNewDynamicNode();
			return;
		}
		
		if(MenuCreator.getActionCommand(MenuCreator.Command.PERFERENCES).equals(event.getActionCommand())){
			browser.editPerferences();
			return;
		}

		if(MenuCreator.getActionCommand(MenuCreator.Command.HELP_INFO).equals(event.getActionCommand())){
			browser.showHelpInfo();
			return;
		}

		if(MenuCreator.getActionCommand(MenuCreator.Command.HELP_ABOUT).equals(event.getActionCommand())){
			browser.showHelpAbout();
			return;
		}

		if(MenuCreator.getActionCommand(MenuCreator.Command.HELP_LICENCE).equals(event.getActionCommand())){
			browser.showLicence();
			return;
		}

		if(MenuCreator.getActionCommand(MenuCreator.Command.EXPORT_RESULTS_TO_FILE).equals(event.getActionCommand())){
			browser.exportResultsToFile();
			return;
		}

		if(MenuCreator.getActionCommand(MenuCreator.Command.NESTED_LOOPS).equals(event.getActionCommand())){
			browser.configureNestedLoops();
			return;
		}

		if(MenuCreator.getActionCommand(MenuCreator.Command.SAVE_AS_IMAGE).equals(event.getActionCommand())){
			browser.saveAsImage();
			return;
		}

		JOptionPane.showMessageDialog(null, "Sorry. No action for that event", "Warning", JOptionPane.WARNING_MESSAGE);
	}
	
    // Key Events	
	// TODO - Not used!??
	public void keyTyped(KeyEvent e){
        logger.info("Typed " + ((int)e.getKeyChar()) + ", code " + e.getKeyCode());
        int modifiers = e.getModifiersEx();
        String modString = "modifiers = " + modifiers;
        String tmpString = KeyEvent.getModifiersExText(modifiers);
        
        if (tmpString.length() > 0) {
            modString += " (" + tmpString + ")";
        } else {
            modString += " (no modifiers)";
        }
	}
	
	// TODO - Not used! Use it or loose it. 
	public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == 37){
            return;
        }
        
        if(e.getKeyCode() == 39){
            return;
        }
    }
    
    public void keyReleased(KeyEvent e){
    }
    // End of Key Events
	
}
