package com.zygomeme.york.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.xml.XMLLayout;

import com.zygomeme.york.YorkUtils;
import com.zygomeme.york.dynamicmodels.DynamicModel;
import com.zygomeme.york.dynamicmodels.DynamicModelHistory;
import com.zygomeme.york.dynamicmodels.DynamicModelNode;
import com.zygomeme.york.dynamicmodels.ErrorItem;
import com.zygomeme.york.gui.MenuCreator.Command;
import com.zygomeme.york.gui.model.ViewModel;
import com.zygomeme.york.internet.HttpClient;
import com.zygomeme.york.propertiesdialog.DefaultPropertiesDialog;
import com.zygomeme.york.propertiesdialog.MappedPanel;
import com.zygomeme.york.propertiesdialog.PropertiesDialog;
import com.zygomeme.york.wizard.DynamicModelNodeWizard;
import com.zygomeme.york.wizard.DynamicModelWizard;
import com.zygomeme.york.xml.DynamicModelHandler;
import com.zygomeme.york.xml.HandlerFactory;
import com.zygomeme.york.xml.HeadHandler;
import com.zygomeme.york.xml.StandardHandler;
import com.zygomeme.york.xml.ViewModelHandler;

import com.zygomeme.york.util.io.FileLoader;

/**
 * **********************************************************************
 *   This file forms part of the ZygoMeme York project - an analysis and
 *   modelling platform.
 *  
 *   Copyright (C) 2009 ZygoMeme Ltd,  
 *   email: coda@zygomeme.com
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
 * This is the View part of the application, and has the main method for 
 * the application.  
 * 
 */

public class YorkBrowser extends JFrame 
	implements WindowListener, KeyListener, ActionListener, ChangeListener
{

	final static long serialVersionUID = 1245134233534L;
	
	//	Create the logger
	Logger  logger = Logger.getLogger(YorkBrowser.class);
	
	private int width = 1000;
    private int height = 700;
    private int xPos = 30; 
    private int yPos = 30;
        
    private JTabbedPane tabbedPane;
    private JLabel statusLabel = new JLabel("Starting up");
    private int newModelIndex = 1;
    
    private PropertiesMemento propertiesMemento;
    private KeyAndMenuEventHandler eventHandler = new KeyAndMenuEventHandler(this);
    private MenuCreator menuCreator = new MenuCreator();
    
    // The list of all the panels (in the tabs)
    private List<TabView> tabViews = new ArrayList<TabView>();
    private TabView currentView = null;	

    private final static String OPEN_COMMAND = "Open_Command";
    private final static String SAVE_COMMAND = "Save_Command";
    private final static String RUN_COMMAND = "Play_Command";
    public final static String CLOSE_TAB_COMMAND = "Close_Tab_Command";
    private final static String NEW_NODE = "New_Node";
    private final static String EDIT_PREFERENCES = "Edit_Preferences_Command";   
    
    private Map<String, JButton> buttonMap = new HashMap<String, JButton>();
        
    // From and to view are used when connecting view together with arcs
    private YorkEntityView fromView;
    private YorkEntityView toView;
    
    private ProgressMonitor progressMonitor;
    
    private boolean arcSelectionOn = false;
        
	public YorkBrowser(){
		super();
	}
		
	public void init(){
	
		// Load the saved position, width etc..
		try {
			propertiesMemento = new PropertiesMemento();
			propertiesMemento.load();
			propertiesMemento.setScreenLocationProperties(this);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Can't load the stored properties, carrying on.", "Warning", JOptionPane.WARNING_MESSAGE);
		}        
		
		// Logger set-up
		logger = Logger.getLogger(YorkBrowser.class);
		try {
			FileAppender appender;
			File logFile = new File(propertiesMemento.getAppDir() + "logfile.txt");
			logFile.delete();
			// "r" milliseconds, "t" thread, "c" category - the class name? etc., "p" priority - DEBUG etc., "m" message, "n" separator 
			FileAppender textAppender = new FileAppender(new PatternLayout("%r %-5p %c{1} - %m%n"),  logFile.getAbsolutePath());
			BasicConfigurator.configure(textAppender);

			File xmlLogFile = new File(propertiesMemento.getAppDir() + "logfile.xml");
			xmlLogFile.delete();
			appender = new FileAppender(new XMLLayout(),  xmlLogFile.getAbsolutePath());
			BasicConfigurator.configure(appender);

			BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%r %-5p %c{1}  \t - %m%n")));
			
		} catch (Exception e) {
			System.out.println("Cannot configure the logger");
		}
		
		if("on".equals(propertiesMemento.get("logging"))){
			logger.debug("Logging turned on from properties file");
			logger.setLevel(Level.DEBUG);			
		}
		else{
			logger.debug("turned logging off");
			// Turn off at the root level
			logger.getParent().setLevel(Level.OFF);
		}
		logger.info("Log file starts");
		
		logger.debug("OS:" + System.getProperty("os.name"));
		
        
        // Set the look and feel
		// Install the Windows look and feel if on windows
		if(System.getProperty("os.name").indexOf("Windows") != -1){
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} 
			catch (Exception e) {
				logger.warn("Exception loading the look and feel");
			} 
		}
		
		// Create directories structure if required
		try{
			YorkUtils.setupDirectoryStructure(propertiesMemento.getDataDir());
		}
		catch(Exception e){
			logger.error("Exception when checking/setting directories", e);
		}
		        
        // Add some listeners
        addWindowListener(this);
        
        setTitle("ZygoMeme York");
        
        // Set icon
        setIconImage(new ImageIcon(propertiesMemento.getImageDir() + "graphLogo32.png").getImage());
        
        // Set up the layout of the window
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        
        getContentPane().add(rootPanel);
        
        // Create the menu bar
        JPanel topPanel = menuCreator.createMenu(eventHandler, getProperties());
        
        // Create the toolbar
        topPanel.add(createToolBar());
        
        rootPanel.add(topPanel, BorderLayout.NORTH);      
        
        // Add the tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(this);
        addKeyListener(this);
        tabbedPane.addKeyListener(this);
        rootPanel.add(tabbedPane, BorderLayout.CENTER);
               
        // Add the status bar
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        rootPanel.add(statusBar, BorderLayout.SOUTH);
        setStatusBarText("Ready");                
        statusBar.add(statusLabel);
                        
        // Set up the size and position and make visible
		setSize(new Dimension(width, height));
		setLocation(xPos, yPos);
		
		// Open any saved tabs
		openSavedTabs();
				
		progressMonitor = new ProgressMonitor(this, "Running Model", "", 0, 1000);

        setVisible(true);
        
        // TODO Make this selectable in user preferences
        checkForUpdates();
	}
	
	private void checkForUpdates(){

		// Check to ensure that it has been at least a day since the last check for an update
		long now = System.currentTimeMillis();
		if(propertiesMemento.getProperty(PropertiesMemento.UPDATE_STRING) != null){
			long lastUpdateTime = Long.parseLong(propertiesMemento.getProperty(PropertiesMemento.UPDATE_STRING));
			long day = 86400000; // milli-seconds in a day
			if((now - lastUpdateTime) < day){
				return; // Don't need to check as a check has been made in the last 24hrs. 
			}
		}
		
		setStatusBarText("Checking for updates...");     
		
        HttpClient webConnection = new HttpClient();
   		Properties webSideProps; 
   		try{
   			webSideProps = webConnection.getProperties("http://www.zygomeme.com/version_prop.txt");
   		}
   		catch(SocketTimeoutException ste){
   			logger.debug("Can't connect to internet:" + ste);
   			setStatusBarText("Unable to connect to internet to check for updates");
   			return;
   		}
   		catch(UnknownHostException uhe){
   			logger.debug("Can't connect to internet:" + uhe);
   			setStatusBarText("Unable to connect to internet to check for updates");
   			return;
   		}
   		
   		if(webSideProps == null || webSideProps.isEmpty()){
   			return;
   		}
   		
   		int latestVersionAvailable = Integer.parseInt(webSideProps.get("version_number").toString());
   		if(latestVersionAvailable > PropertiesMemento.APP_VERSION_NUMBER){
   			setStatusBarText("A new version of ZygoMeme York is now available. You can now upgrade to Version " + webSideProps.getProperty("version_string") + " " + 
   					webSideProps.get("stage"));        
   		}
   		else{
   			setStatusBarText("Update check has been made - application is up to date.");     
   		}

   		// To get here the properties will have been updated
		propertiesMemento.setProperty(PropertiesMemento.UPDATE_STRING, "" + now);

		// Save the properties straight away so that the new last "check for update" time is recorded
		try {
			propertiesMemento.saveProperties(this);			
		} catch (Exception e) {
			logger.warn("Unable to save properties");
		}
	}
	
	private JToolBar createToolBar(){
	    
        JToolBar toolBar = new JToolBar();
        toolBar.setRollover(true);
        
        // Save button
        addButton(toolBar, propertiesMemento.getImageDir() + "save.gif", SAVE_COMMAND, "Save current model");
        addButton(toolBar, propertiesMemento.getImageDir() + "open_file.gif", OPEN_COMMAND, "Open a file");
        addButton(toolBar, propertiesMemento.getImageDir() + "new_node.png", NEW_NODE, "Create a new node");
        addButton(toolBar, propertiesMemento.getImageDir() + "play.png", RUN_COMMAND, "Run the model");
        addButton(toolBar, propertiesMemento.getImageDir() + "prefs.png", EDIT_PREFERENCES, "Edit defaults");        
        
        toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        setTabButtons(false);
        return toolBar;
	}

	private void addButton(JToolBar toolBar, String filename, String commandName, String tooltipText){

        JButton newButton = new JButton(new ImageIcon(filename));
        newButton.addActionListener(this);
        newButton.setActionCommand(commandName);
        newButton.setToolTipText(tooltipText);
        buttonMap.put(commandName, newButton);
        toolBar.add(newButton);        
    }
	
	public TabView getCurrentView(){
		return currentView;
	}

	public JTabbedPane getTabbedPane(){
		return tabbedPane;
	}
	
	public LayoutManager getCurrentLayoutManager(){
		return currentView.getLayoutManager();
	}
	
	public PropertiesMemento getProperties(){
		return propertiesMemento;
	}
	
	public String getAppBaseDir(){
		return propertiesMemento.getAppDir();
	}

	public String getBaseDir(){
		return propertiesMemento.getBaseDir();
	}
	
	public void repaintTab(){
		if(currentView != null){
			currentView.repaint();
		}
	}

	public void runModel(){

		// Run the analysis of the model in a separate thread
		currentView.getDynamicModelView().getModel().setProgressMonitor(progressMonitor);
		new Thread(currentView.getDynamicModelView().getModel()).start();

		setStatusBarText("Ready");
	}
	
	public void reportAnyErrors(DynamicModelHistory history){
		
		// If there was an error then report this to the user
		if(history.getStatus() == DynamicModelHistory.StatusType.ERROR && history.getErrorReport().getReport().size() > 0){
			ErrorItem error = history.getErrorReport().getReport().get(0);
			if(error.getNodeId() != null){
				JOptionPane.showMessageDialog(this, "Error on node \"" + error.getNodeId() + "\". " + error.getMessage(), "Run Aborted", JOptionPane.WARNING_MESSAGE);
			}
			else{
				JOptionPane.showMessageDialog(this, error.getMessage(), "Run Aborted", JOptionPane.WARNING_MESSAGE);
			}
			// Don't leave the progress monitor hanging around on the screen
			progressMonitor.close();
		}
	}

	// Actions such as Open File, Open Tab etc...
	public void openTab(String tabName){

		TabView selectedTabView = new TabView(this);
		tabViews.add(selectedTabView);
		
		
		if(tabName == null){ // If a new tab..
			// Ensure that there isn't already a model with that name
			while(propertiesMemento.getTabNames().contains(tabName) || tabName == null){
				tabName = "New Model " + newModelIndex;
				newModelIndex++;
			}
		}
        logger.info("Adding tab:" + tabName);

		// Just resizes and doesn't scroll - so don't need the scroll pane
		/*JScrollPane scrollPane = new JScrollPane(selectedTabView, 
                								 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                								 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        tabbedPane.addTab(tabName, scrollPane);*/

		tabbedPane.addTab(tabName, selectedTabView);

        currentView = selectedTabView;
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        setTabName(tabName);
				
        tabbedPane.setTabComponentAt(tabbedPane.getSelectedIndex(), 
				 new TabButtonComponent(propertiesMemento.getImageDir(), this, currentView));
        
        // Add Mouse motion Listener 
        currentView.addMouseMotionListener(currentView);
        currentView.addMouseListener(currentView);

        // Record the tab in the memento
        propertiesMemento.addTabRecord(tabName);
    
        setTabButtons(true);
        
        setStatusBarText("Ready");

        requestFocusInWindow();
	}
	
	private void setTabButtons(boolean enableFlag){
	
		// Grey out or ungrey the buttons
		buttonMap.get(SAVE_COMMAND).setEnabled(enableFlag);
		buttonMap.get(NEW_NODE).setEnabled(enableFlag);
		buttonMap.get(RUN_COMMAND).setEnabled(enableFlag);
		
		// Grey out or ungrey the menu items
		Map<Command, JMenuItem> menuItemMap = menuCreator.getMenuItemMap();
		menuItemMap.get(MenuCreator.Command.SAVE_TAB).setEnabled(enableFlag);
		menuItemMap.get(MenuCreator.Command.EDIT_TAB_NAME).setEnabled(enableFlag);
		menuItemMap.get(MenuCreator.Command.NEW_DYNAMIC_NODE).setEnabled(enableFlag);
		menuItemMap.get(MenuCreator.Command.SAVE_AS_IMAGE).setEnabled(enableFlag);
		menuItemMap.get(MenuCreator.Command.EXPORT_RESULTS_TO_FILE).setEnabled(enableFlag);
		menuItemMap.get(MenuCreator.Command.INSERT).setEnabled(enableFlag);
		menuItemMap.get(MenuCreator.Command.EDIT).setEnabled(enableFlag);		
	}
	
	private void setTabName(String newName){
        currentView.getViewModel().setName(newName);
		
		TabButtonComponent tbComp = (TabButtonComponent) tabbedPane.getTabComponentAt(tabbedPane.getSelectedIndex());

		if(tbComp != null){ // Null if not yet visible
			tbComp.setTitle(newName);
		}
	}
	
	private void openSavedTabs(){

		// Copy the tab filenames into a list to be used while loading as the opening tabs methods also
		// includes the step of recording the tabs in the propertiesMemento - thereby overwriting the 
		// loaded list. This would result in only one tab being opened. 
		List<String> filenames = new ArrayList<String>();
		int i = 0;		
		while(propertiesMemento.get("tab" + i) != null){
			filenames.add((String) propertiesMemento.get("tab" + i));
			i++;			
		}

		for(String filename: filenames){
			logger.info("Looking for:" + filename);
			File file = new File(filename);
			if(!file.exists()){
				//JOptionPane.showMessageDialog(null, "Cannot find " + filename, "Warning", JOptionPane.WARNING_MESSAGE);
				// Simply ignore KIS
				logger.info("Cannot find file " + filename + " so not load it as tab");
			}
			else{
				String content = FileLoader.loadFile(filename);
				// If the application can't open one of the recorded tabs then drop out trying to load them.
				if(!openSavedTab(content)){
					return;
				}
			}
		}
	}
	
	private boolean openSavedTab(String content){
		logger.info("Opening a saved tab");
		ViewModelHandler handler = new ViewModelHandler();
		try {
			handler.parse(content);
			if(!handler.parsedOK()){
				// TODO: Add some intelligence to the process of handling this exception
				if(handler.getException() instanceof IOException){
					JOptionPane.showMessageDialog(null, "Sorry, there was a problem loading one of the models", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else{
					JOptionPane.showMessageDialog(null, "Sorry. Unable to parse that file", "Error", JOptionPane.ERROR_MESSAGE);
				}
				return false;
			}
		} catch (Exception e) {
			// TODO: Add some intelligence to the process of handling this exception
			JOptionPane.showMessageDialog(null, "Sorry. Unable to parse that file", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		ViewModel model = handler.getViewModel();
		
		// Open a new tab and place the model on it
		openTab(model.getName());
		currentView.setViewModel(model);
        tabbedPane.setTabComponentAt(tabbedPane.getSelectedIndex(), 
        							 new TabButtonComponent(propertiesMemento.getImageDir(), this, currentView));
		
		// Load the models within the tab
		for(String filename: handler.getFilenames()){

			logger.info("Attempt to load and parse:\"" + filename + "\"");
			content = FileLoader.loadFile(filename);
			if(content != null){
				logger.info(filename + " content:" + content.substring(0, Math.min(180, content.length())));
			
				// Load the view and put it in the view
				loadViewModel(filename, content);
			}
			else{
				JOptionPane.showMessageDialog(null, "Sorry. File " + filename + " not found", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}		
		return true;
	}
			
	public void openFile(){
		logger.info("Open File");

		// Get the filename
		YorkGUIUtils guiUtils = new YorkGUIUtils();
		String selectedFile = guiUtils.getUserToDefineFilename(this, propertiesMemento.getTabDir(), "xml", "York Files", JFileChooser.OPEN_DIALOG);

		if(selectedFile == null || guiUtils.wasCancelled()){
			requestFocusInWindow();
			return;
		}			
		logger.info("Selected File:" + selectedFile);

		// Load the content
		String content = FileLoader.loadFile(selectedFile);
		if(content == null || content.length() == 0){
			JOptionPane.showMessageDialog(this, "Sorry, no content in that file.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Find out what type of file it is
		HeadHandler hHandler = new HeadHandler();
		try {
			hHandler.parse(content);
			if(hHandler.getFileType() == null){
				throw new RuntimeException("Unrecognised file type");
			}
		} catch (Exception e) {
			// TODO: Add some intelligence to the process of handling this exception
			JOptionPane.showMessageDialog(this, "Sorry, unrecognised file type. Content starts:" + hHandler.getHead() + "...", "Error", JOptionPane.ERROR_MESSAGE);
		}			
		
		// Load the view/tab 
		if(hHandler.getFileType() == HeadHandler.FileType.MODEL){
			openSavedTab(content);
			return;
		}
		
		// Load a dynamic model 
		if(hHandler.getFileType() == HeadHandler.FileType.DYNAMIC_MODEL){
			
			StandardHandler vHandler = HandlerFactory.getHandler(hHandler.getFileType());
			try {
				vHandler.parse(content);	
			} catch (Exception e) {
				// TODO: Add some intelligence to the process of handling this exception
				JOptionPane.showMessageDialog(null, "Sorry. Unable to parse the file " + selectedFile, "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			// Open a new tab for the newly loaded dynamic model
			openTab(((DynamicModelHandler)vHandler).getModel().getIdString());
			
			// Create a view for the loaded entity
			YorkEntityView newView = ViewFactory.getView(hHandler.getFileType(), this, vHandler, new EntityViewConfig(10, 30, 280, 100));

			// Add the new view to the tab view
			if(newView != null){
				currentView.addModelView(newView);
			}
		}
		
		// Repaint the current view
		if(currentView != null){
			currentView.repaint();
		}
		
		requestFocusInWindow();
	}

	private void loadViewModel(String configName, String content){

		// Get the file type
		HeadHandler hHandler = new HeadHandler();
		try {
			hHandler.parse(content);
			if(hHandler.getFileType() == null){
				throw new RuntimeException("Unrecognised file type");
			}
		} catch (Exception e) {
			// TODO: Add some intelligence to the process of handling this exception
			JOptionPane.showMessageDialog(this, "Sorry, unrecognised file type. Content starts:" + hHandler.getHead() + "...", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		StandardHandler vHandler = HandlerFactory.getHandler(hHandler.getFileType());
		logger.info("Found file of type:" + hHandler.getFileType());
		logger.info("vHandler:" + vHandler);
		try {
			vHandler.parse(content);	
		} catch (Exception e) {
			// TODO: Add some intelligence to the process of handling this exception
			JOptionPane.showMessageDialog(this, "Sorry. Unable to parse the file " + configName, "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		YorkEntityView newView = ViewFactory.getView(hHandler.getFileType(), this, vHandler, null);
		if(newView != null){
			currentView.addModelView(newView);
		}
		
		requestFocusInWindow();
	}
	
	public void editTabName(){

		if(tabbedPane.getSelectedIndex() == -1){
			JOptionPane.showMessageDialog(this, "No tab present that can be renamed", "Information", JOptionPane.INFORMATION_MESSAGE);			
			return;
		}
		boolean nameClash = false;
		//String previousName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		String previousName = ((TabButtonComponent)tabbedPane.getTabComponentAt(tabbedPane.getSelectedIndex())).getTabView().getViewModel().getName();
		String newName;
		DynamicModelWizard wiz = new DynamicModelWizard();
		
		List<String> modelsOnDisk = YorkUtils.getModelListFromDisk(PropertiesMemento.TAB_DIR);
		
		do{
			newName = wiz.getNewModelName(this, previousName);
			nameClash = false;
			
			if(!wiz.wasCancelled()){
				// Check that this name does not already exist
				for(int i = 0; i < tabbedPane.getTabCount(); i++){
					// TODO Should be an easier way to get the name
					String name = ((TabButtonComponent)tabbedPane.getTabComponentAt(i)).getTabView().getViewModel().getName();
					logger.info("Comparing new name " + newName + " against existing name: " + name);
					if(name != null && name.equals(newName)){
						nameClash = true;
						JOptionPane.showMessageDialog(this, "That name is already in use, please enter a different name", "Name already in use", JOptionPane.WARNING_MESSAGE);					
					}
				}

				// If the disk contains a model of the same name then we should warn then user.
				if(modelsOnDisk.contains(newName + ".xml") && nameClash == false){
					nameClash = true;
					int option = JOptionPane.showConfirmDialog(this, "That name is already in use, are you sure you want to use that name?", "Name already in use", JOptionPane.YES_NO_OPTION);
					if(option == JOptionPane.YES_OPTION){
						nameClash = false; // The user accepts that the name is being reused
					}
				}
			}
		}
		while(nameClash);

		if(!wiz.wasCancelled()){
			setTabName(newName);
			logger.info("Tab name:" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
			propertiesMemento.renameTab(previousName, newName); // Ensure the tabs are correctly recorded
			if(currentView.getDynamicModelView() != null){
				currentView.getDynamicModelView().getModel().setIdString(newName);
			}
			currentView.repaint();		
		}
		requestFocusInWindow();
	}
	
	public void createNewDynamicNode(){
		
		DynamicModelNodeWizard wiz = new DynamicModelNodeWizard();
		
		if(currentView == null){
			return;
		}
		
		if(currentView.getDynamicModelView() == null){
			createNewDynamicModel();
		}
		DynamicModel dModel = currentView.getDynamicModelView().getModel();

		while(true){

			DynamicModelNode dNode = new DynamicModelNode(wiz.getNewNamedNode(this));
			if(!wiz.wasCancelled()){
				String name = dNode.getIdString();
				// Check the name isn't a number (which is not allowed)
				// TODO Already does some of these validations in the wizard - repeated/spread code
				try {
					Double.parseDouble(name);
					JOptionPane.showMessageDialog(null, "Nodes cannot be given numbers as names", "Warning", JOptionPane.WARNING_MESSAGE);						
				} catch (NumberFormatException nfe) {
					
					// Check that the name isn't already in use
					if(dModel.getNodeIds().contains(name)){
						JOptionPane.showMessageDialog(null, "That name is already in use, please use another name", "Warning", JOptionPane.WARNING_MESSAGE);						
					}
					else{
						// SUCCESS - Add the new graph to the view
						logger.info("createNewDynamicNode() adding to node:" + name);
						currentView.addModelView( new DynamicModelNodeView(new EntityViewConfig(10, 30, 280, 100, EntityViewConfig.Type.DYNAMIC_NODE), dNode, this));
						currentView.repaint();
						requestFocusInWindow();					
						return; // The node was successfully named and created
					}
				}						
			}
			else{
				return; // The cancel button was pressed
			}
		}
	}
	
	private void createNewDynamicModel(){

		// By default the dynamic model has the same name as the tab
		DynamicModel dModel = new DynamicModel(currentView.getViewModel().getName());

		// Add the new graph to the view
		int modelWidth = DynamicModelView.minimumWidth; 
		int modelHeight = DynamicModelView.minimumHeight; 
		int viewHeight = currentView.getHeight();
		int pad = 8;
		currentView.addModelView(new DynamicModelView(new EntityViewConfig(pad, 
																	   viewHeight - (modelHeight + pad), 
																	   modelWidth, 
																	   modelHeight, 
																	   EntityViewConfig.Type.DYNAMIC_MODEL), 
													dModel, this, currentView));
		currentView.repaint();
	}
	
	public void editPerferences(){
		logger.info("Properties:" + propertiesMemento);
		DefaultPropertiesDialog pDialog = new DefaultPropertiesDialog(this);
		pDialog.showDialog();
		
		String previousDataDir = propertiesMemento.getProperty("dataDir");
		
		Map<String, JComponent> perferencesMap = pDialog.getMappedPanels(PropertiesDialog.DEFAULT_TAB_BACKGROUND_COLOR);
		for(String k: perferencesMap.keySet()){
			MappedPanel mPanel = (MappedPanel)perferencesMap.get(k);
			Map<String, Object> results = mPanel.getResultsMap();
			for(String key: results.keySet()){
				Object value = results.get(key);
				// Save colors as ints - properties will be saved to a text file so objects have to 
				// have some form of serialisation
				if(value instanceof Color){
					logger.info(key + " = " + value);
					propertiesMemento.setProperty(key, "" + ((Color)value).getRGB());
				}
				else{
					logger.info(key + " = " + results.get(key));
					propertiesMemento.setProperty(key, value.toString());
				}
			}
		}
		
		// Reset the directories in case any of them have changed
		if(!propertiesMemento.getDataDir().equals(previousDataDir)){
			propertiesMemento.resetDirectories();
		}

		// Ensure data directories are present
		YorkUtils.setupDirectoryStructure(propertiesMemento.getDataDir());
		
		logger.info("Properties:" + propertiesMemento);
	}
	
	public void showHelpInfo(){
	
		showURL("index.html", "Unable to launch help file in browser, use your Internet browser to view help files.");
	}

	public void showHelpAbout(){
		
		HelpAboutPopup aboutPanel = new HelpAboutPopup(this, getProperties());
		aboutPanel.showDialog();
	}
	
	public void showLicence(){
		
		String licenceUrl = "gpl-3.0-standalone.html";
		showURL(licenceUrl, "Unable to view licence. Please see " + PropertiesMemento.help_dir_url + licenceUrl);
	}
	
	private void showURL(String urlString, String errMessage){

		Desktop desktop;
		if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("file:///" + PropertiesMemento.help_dir_url + urlString));
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
                JOptionPane.showMessageDialog(this, "Unable to view the selected file", "Error", JOptionPane.ERROR_MESSAGE);				
            }
            catch(URISyntaxException use) {
                use.printStackTrace();
                JOptionPane.showMessageDialog(this, "Unable to view the selected file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
		else{
			JOptionPane.showMessageDialog(this, errMessage, "Warning", JOptionPane.WARNING_MESSAGE);			
		}				

	}

	/** 
	 * Exports the table results to a file.
	 */
	public void exportResultsToFile(){
				
		// This if clause shouldn't fire as the icon is grayed out if there is no tab open
		if(currentView == null 
				|| currentView.getDynamicModelView() == null 
				|| currentView.getDynamicModelView().getModel() == null){
			JOptionPane.showMessageDialog(this, "A tab must be open, and a model and table must be present before results can be exported", "Information", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		if(currentView.getDynamicModelView().getModel().getDefaultHistory() == null){
			JOptionPane.showMessageDialog(this, "A table must be present before results can be exported", "Information", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		DynamicModelHistory history = currentView.getDynamicModelView().getModel().getDefaultHistory();

		StringBuffer buffer = new StringBuffer(30000);

		if(history != null && history.getTimePointCount() > 0 && history.getSnapshot(0) != null){
		
			// Get the filename
			YorkGUIUtils guiUtils = new YorkGUIUtils();
			String selectedFile = guiUtils.getUserToDefineFilename(this, propertiesMemento.getBaseDir(), "txt csv", "Results files (.txt, .csv)", JFileChooser.SAVE_DIALOG);

			if(selectedFile == null || guiUtils.wasCancelled()){
				requestFocusInWindow();
				return;
			}			
			logger.info("Selected file for exporting results:" + selectedFile);
		
			buffer.append("time point").append("\t");
			for(int t = 0; t < history.getTimePointCount(); t++){
				buffer.append(t).append("\t");
			}
			buffer.append("\n");
			
			for(Iterator<String> it = history.getSnapshot(0).keySet().iterator(); it.hasNext();){
				String name = it.next();
				buffer.append(name).append("\t");
				// For all time points
				for(int t = 0; t < history.getTimePointCount(); t++){
					buffer.append(history.getSnapshot(t).get(name).getOutputValue()).append("\t");
				}
				buffer.append("\n");
			}
			FileLoader.save(selectedFile, buffer.toString());
		}
		else{
			JOptionPane.showMessageDialog(this, "No results available in the selected tab. Run the model first.", "Warning", JOptionPane.WARNING_MESSAGE);
			
		}
	}
	
	public void configureNestedLoops(){
		
		// Should not fire as the icon should be grayed out if there is not open tab or no dynamic model
		if(currentView == null || currentView.getDynamicModelView() == null){
			JOptionPane.showMessageDialog(this, "A tab must be open and a dynamic model present before nested loops can be configured", "Information", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		currentView.getDynamicModelView().configureNestedLoops();
	}
	
	public void saveAsImage(){

		// Should not fire as the menu item to select this feature is grayed out
		if(currentView == null){
			JOptionPane.showMessageDialog(this, "A tab must be open before an image can be saved", "Information", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		currentView.saveAsImage();
	}

	public void allowUserToQuit(){

        // Modal dialog with yes/no button
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit?", JOptionPane.YES_NO_OPTION);
        
        if (answer == JOptionPane.YES_OPTION) {
        	try {
        		propertiesMemento.saveProperties(this);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Unable to save properties. Ensure the file " + propertiesMemento.getAppDir() + "appProp.txt is not in use by another application.", 
						"Unable to save properties", JOptionPane.WARNING_MESSAGE);				
			}

			// If there are any tabs open then ask the user if she wants to save the tabs
			if(currentView != null){
				askIfTheUserWantsToSaveTabs();
			}

        	System.exit(0);
        }        
    }
	
	private void askIfTheUserWantsToSaveTabs(){
	
	    int answer = JOptionPane.showConfirmDialog(this, "Do you want to save all tabs?", "Save?", JOptionPane.YES_NO_OPTION);
        
	    // If the user chooses "no", then the tab details will be stored in the props file, but when the 
	    // tab is new and unsaved the XML file for the tab will not be saved. When the application starts
	    // again it will look for the tab view file and not find it. In such cases the application will
	    // silently ignore the missing file (it will log the issue). This is the KIS solution.
        if (answer == JOptionPane.YES_OPTION) {        	
        	for(TabView view: tabViews){
        		view.save(this, propertiesMemento);
        	}
        }
	}
	
	public void saveTab(){
		
		if(currentView != null && currentView.getViewModel() != null){
			logger.info("Filename is null, setting it to " + currentView.getViewModel().getName() + ".xml");
			currentView.save(this, propertiesMemento);
		}		
	}
	
	// Status Bar code
    public void setStatusBarText(String text){
        statusLabel.setText(text);
    }
    
    public boolean isArcSelectionOn(){
    	return arcSelectionOn;
    }

    public void setFromView(YorkEntityView view){

    	fromView = view;
    	logger.info("Set fromView:" + fromView);
    }
    
    public void setToView(YorkEntityView view){

    	toView = view;
    	logger.info("Set toView:" + toView.getModelsIdString());
    	// TODO Would be cute to have this work through listener style interfaces 
    	fromView.addAToView(toView);
    	toView.addAFromView(fromView);
    	//currentView.addArc(fromView, toView);
    	currentView.getDynamicModelView().getArcMap().add(fromView, toView);
    	repaint();

    	// Cleans up modes and pointers
    	fromView = null;
    	toView = null;
    }
    
    public void removeArc(YorkEntityView fromView, YorkEntityView toView){
    	currentView.getDynamicModelView().getArcMap().remove(fromView, toView);
    }
    
    //  Key Events
    public void keyTyped(KeyEvent e){
        eventHandler.keyTyped(e);
    }

    // TODO Not used!
    public void keyPressed(KeyEvent e){
        eventHandler.keyPressed(e);
        if(e.getKeyCode() == 37){
            return;
        }
        
        if(e.getKeyCode() == 39){
            return;
        }
    }
    
    public void keyReleased(KeyEvent e){
		eventHandler.keyReleased(e);
    }
    // End of Key Events
    
    
	// Getters and Setters for Properties
	public int getWidth(){
		width = super.getWidth();
		return width;
	}
	
	public void setWidth(int newWidth){
		width = newWidth;
	}
	
	public int getHeight(){
		height = super.getHeight();
		return height;
	}
	
	public void setHeight(int newHeight){
		height = newHeight;
	}
	
	public int getX(){
		xPos = super.getX();
		return xPos;		
	}
	
	public void setX(int newXPos){
		xPos = newXPos;
	}

	public int getY(){
		yPos = super.getY();
		return yPos;
	}
	
	public void setY(int newYPos){
		yPos = newYPos;
	}
	
	// Action Listener Events
	public void actionPerformed(ActionEvent e){

		logger.info("Is focused:" + isFocused() );
		logger.info("Focus comp:" + getFocusOwner());
        if(SAVE_COMMAND.equals(e.getActionCommand())){
        	logger.info("Save Tab");
		    saveTab();
            return;
        }
        if(OPEN_COMMAND.equals(e.getActionCommand())){
        	openFile();
        	return;
        }

        if(NEW_NODE.equals(e.getActionCommand())){
        	createNewDynamicNode();
        	return;
        }

        if(RUN_COMMAND.equals(e.getActionCommand())){
        	logger.info("Play command issued and received");
			if(currentView != null){
				runModel();
			}
        	return;
        }
                
        if(EDIT_PREFERENCES.equals(e.getActionCommand())){
        	editPerferences(); 	
        	repaint();
        	return;
        }
        
        if(e.getActionCommand().startsWith(CLOSE_TAB_COMMAND)){
        	
        	int indexOfDoomedTab = tabbedPane.indexOfTabComponent((Component)e.getSource());
        	
        	propertiesMemento.removeTabRecordAtIndex(indexOfDoomedTab);
        	tabbedPane.remove(indexOfDoomedTab);
        	tabViews.remove(indexOfDoomedTab);

        	if(tabbedPane.getSelectedIndex() > -1){
        		currentView = tabViews.get(tabbedPane.getSelectedIndex());
        	}
        	else{
        		currentView = null;
        		setTabButtons(false);
        	}        	
        	return;
        }
        logger.info("Event NOT HANDLED!:" + e);
	}
	
	public Logger getLogger(){
		return logger;
	}
	
	//	Window events
    public void windowActivated(WindowEvent we)
    {
    }
    public void windowClosed(WindowEvent we)
    { 
    }
    public void windowClosing(WindowEvent we)
    {
		// If there are any tabs open then ask the user if she wants to save the tabs
		if(currentView != null){
			askIfTheUserWantsToSaveTabs();
		}
		System.exit(0);
    }
    public void windowDeactivated(WindowEvent we)
    {
    }
    public void windowDeiconified(WindowEvent we)
    {
    }
    public void windowIconified(WindowEvent we)
    {
    }
    public void windowOpened(WindowEvent we)
    {
    }
    // End of Window events
	
    /** Listen for changes in the selection of the tabbed pane **/
    public void stateChanged(ChangeEvent e){
    	//logger.info("Change event " + e.getSource());
    	if(e.getSource() == tabbedPane){
    		logger.info("Index of the selected tab is " + tabbedPane.getSelectedIndex()); 
    		if(tabbedPane.getSelectedIndex() == -1){
    			return;
    		}
    		// Change to the current view
    		currentView = tabViews.get(tabbedPane.getSelectedIndex());
    	}
    }
    
	public static void main(String[] args) {
		
		ApplicationSplashScreen ms = new ApplicationSplashScreen();

		YorkBrowser nb = new YorkBrowser();				

		// Load the saved position, width etc..
		PropertiesMemento pm = null;
		try {
			pm = new PropertiesMemento();
			pm.load();
			pm.setScreenLocationProperties(nb);
		} catch (Exception e) { 
		}

		// Get Image
		Image image = new ImageIcon(pm.getImageDir() + "splash.png").getImage();
		
		// Get the size of the default screen
		if(image != null){
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			ms.setLocation((dim.width / 2) - (image.getWidth(null) / 2), (dim.height / 2) - (image.getHeight(null) / 2));
			ms.setImage(image);		
		}
		// Launch the application
        nb.init();        
        
        // Some messing about to try to have the splash screen in front of the application window and for the 
        // transparency to work correctly. Would be easier not to have transparency, but it wouldn't look as good, 
        // that said the transparency effect doesn't always succeed.
        // TODO revisit and produce something that looks 
        // good, but doesn't use transparency. 
        if(image != null){
        	try {
        		Thread.sleep(100);
        	}
        	catch(InterruptedException e) {
        	}
        	ms.toFront();

        	try {
        		Thread.sleep(500);
        	}
        	catch(InterruptedException e) {
        	}
        	ms.toFront();

        	try {
        		Thread.sleep(2000);
        	}
        	catch(InterruptedException e) {
        	}

        	ms.setVisible(false);
        	ms.dispose();
        }               
	}
}
