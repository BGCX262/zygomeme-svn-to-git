package com.zygomeme.york.propertiesdialog;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import com.zygomeme.york.gui.YorkBrowser;
import com.zygomeme.york.gui.PropertiesMemento;

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
 * Dialog that presents the properties to the user so that they can set their preferences.  
 * 
 */
public abstract class PropertiesDialog implements TreeSelectionListener{

	private Logger logger = Logger.getLogger(PropertiesDialog.class);
	protected PropertiesMemento propertiesMemento = null;
	private JTree tree;
	protected Map<String, MappedPanelCreator> panelMap = new LinkedHashMap<String, MappedPanelCreator>();
	private EditPane editPane;
	private YorkBrowser browser;
	private boolean initialised = false;

	public final static String DATA_DIR = "Data Directory";
	public final static String DEFAULT_TAB_BACKGROUND_COLOR = "Default Background Gradient";
	public final static String NODE_FONT = "Font";
	public final static String FONT_COLOR = "Font Color";
	public final static String BORDER_COLOR = "Border Color";
	public final static String INFO = "Node Preferences";
	public final static String MODEL_PREFERENCES = "Model Preferences";
	public final static String DIRECTORY = "Directory Preferences";
	public final static String TABS = "Tab Views";
	public final static String DEFAULT_NODE_COLOR = "Default node color";
	public final static String DEFAULT_FONT_COLOR = "Default font color";
	public final static String DEFAULT_FONT = "Default font";
	public final static String ARC_COLOR = "Default arc color";
	public final static String ARC_STYLE = "Arc style";
	
	private ImageIcon closedImage;
	private ImageIcon openImage;

	public PropertiesDialog(YorkBrowser browserIn){
		this.browser = browserIn;
		if(browser != null){
			propertiesMemento = browser.getProperties();
		}
	}
	
	public void init(){
		
		// Load the saved properties
		// TODO Get Properties from a singleton or factory
		if(propertiesMemento == null){
			try {
				propertiesMemento = new PropertiesMemento();
				propertiesMemento.load();
			} catch (Exception e) {
				System.out.println("Can't load the stored properties");
				e.printStackTrace();
				System.exit(-1);
			}        
		}
		
        // Set the look and feel
		// Install a different look and feel; specifically, the Windows look and feel
		if(browser == null){
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
        closedImage = new ImageIcon(propertiesMemento.getImageDir() + "closed_tree_node.gif");
        openImage = new ImageIcon(propertiesMemento.getImageDir() + "open_tree_node.gif");
        
        editPane = new EditPane(browser, "Edit Properties");
        initialised = true;
	}
	
	public void createTree(){
		DefaultMutableTreeNode top = new DefaultMutableTreeNode();
	    createNodes(top);
	    tree = new JTree(top);
	    tree.setRootVisible(false);
	    tree.setSelectionPath(new TreePath(top));

	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

	    // Change the icons for the branch nodes and replace the default leaf icon with null
	    if (closedImage != null) {
	        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
	        renderer.setClosedIcon(closedImage);
	        renderer.setOpenIcon(openImage);
	        renderer.setLeafIcon(null);
	        tree.setCellRenderer(renderer);
	    }

	    tree.addTreeSelectionListener(this);
	}

	protected abstract void createNodes(DefaultMutableTreeNode top);
	
	protected class PanelInfo {
        public String name;
        private MappedPanelCreator panelCreator;

        public PanelInfo(String newName, MappedPanelCreator panelIn) {
            name = newName;
            panelCreator = panelIn;
        }

        public MappedPanelCreator getPanelCreator(){
        	return panelCreator;
        }
        
        public String toString() {
            return name;
        }
    }

	public void showDialog(){

		if(!initialised){
			init();
		}

		createTree();
						
		editPane.setTree(tree);
		InfoPanel infoPanel = new InfoPanel();
		editPane.addEditPane(infoPanel.getPanel(), "info", infoPanel);
		editPane.getEditPane().validate();
		editPane.validate();

		editPane.showDialog();

		Map<String, String> map = editPane.getMap();
		for(String key: map.keySet()){
			logger.info(key + " = " + map.get(key));
		}

	}
	
	public Map<String, String> getMap(){
		return editPane.getMap();
	}
	
	public Map<String, JComponent> getMappedPanels(String id){
		return editPane.getMappedPanels();
	}
	
	public void valueChanged(TreeSelectionEvent e) {

		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		if (treeNode == null){
			return;
		}
		
		Object nodeInfo = treeNode.getUserObject();

		if(editPane.getEditPane().getComponentCount() > 0){
			editPane.getEditPane().remove(0);
		}

		PanelInfo panelInfo = (PanelInfo)nodeInfo;
		MappedPanel mappedPanel = panelInfo.getPanelCreator().getPanel();	
		editPane.addEditPane(mappedPanel, panelInfo.getPanelCreator().getKey(), panelInfo.getPanelCreator());
		editPane.validate();
	}
}
