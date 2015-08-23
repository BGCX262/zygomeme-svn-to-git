package com.zygomeme.york.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputListener;

import org.apache.log4j.Logger;

import com.zygomeme.york.chart.RComponentListener;
import com.zygomeme.york.dynamicmodels.DynamicModel;
import com.zygomeme.york.gui.model.ViewModel;
import com.zygomeme.york.propertiesdialog.ColorGradientPanel;
import com.zygomeme.york.propertiesdialog.IndividualPanelDialog;
import com.zygomeme.york.wizard.DynamicModelWizard;
import com.zygomeme.york.gui.EntityViewConfig;

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
 * The View of the Model - the object that paints the model in the tab.
 * 
 */
public class TabView extends JPanel implements MouseMotionListener, MouseInputListener, ActionListener, ButtonEventHandler{

	private static final long serialVersionUID = 80485098709709798L;

	private Logger logger = Logger.getLogger(TabView.class);
	private BackgroundRender backgroundRender;
	private ViewModel viewModel; 
	private LayoutManager layoutManager;
	private YorkBrowser browser;
	
	private List<YorkEntityView> stagedModelViews = new ArrayList<YorkEntityView>();
	private List<YorkEntityView> viewsToRemove = new ArrayList<YorkEntityView>();; 
	protected boolean mouseDragged = false;
	
	public enum ResizeCorner{TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT, NONE};
		
	// Dynamic Model View - for now only have one DMV per TabView
	// TODO this should be in the ViewModel
	private DynamicModelView dynamicModelView = null;
			    
    private final static String CREATE_DYNAMIC_NODE = "New node...";
    private final static String SAVE_AS_IMAGE = "Save as image...";
    private final static String EDIT_PROPERTIES = "Edit properties...";
    private String[] rightClickLabels = new String[]{CREATE_DYNAMIC_NODE, SAVE_AS_IMAGE, EDIT_PROPERTIES};
	
	private Graphics2D graphics = null;
    private Image myImage = null;   
    
    //private List<GUIButton> buttons = new LinkedList<GUIButton>();
    private Map<GUIButton, RComponentListener> buttonMap = new HashMap<GUIButton, RComponentListener>();
    	    
    public TabView(YorkBrowser browserIn){
    	this.browser = browserIn;
    	this.viewModel = new ViewModel(browser.getProperties());
    	this.backgroundRender = new BackgroundRender(viewModel);
    	this.layoutManager = new LayoutManager(this);
    }
    
	public void initGraphics(){
		myImage = createImage(getWidth(), getHeight());
		graphics = (Graphics2D)myImage.getGraphics();	
	}
	
	public void initialiseDefaultViews(){
	
		DynamicModelWizard wiz = new DynamicModelWizard();
		DynamicModel dModel = wiz.getNewNamedModel(browser);

		// Add the new model to the view
		logger.info("Initialising DynamicModelView");
		dynamicModelView = new DynamicModelView(new EntityViewConfig(getWidth() - 420, getHeight() - 320, 400, 300), dModel, browser, this);
		addModelView(dynamicModelView);
	}
	
	public LayoutManager getLayoutManager(){
		return layoutManager;
	}
	
	public DynamicModelView getDynamicModelView(){
		return dynamicModelView;
	}
	
	public void addModelView(YorkEntityView newView){

		logger.info("Adding new view:" + newView.getModelsIdString() + ", type:" + newView.getClass().getName());
		if(newView instanceof DynamicModelView){
			logger.info("This is a DynamicModelView");
			dynamicModelView = (DynamicModelView) newView;
		}

		// Creates the dynamicModel the first time anything is added to the view. 
		if(dynamicModelView == null){
			initialiseDefaultViews();
		}

		if(newView instanceof DynamicModelNodeView){
			dynamicModelView.getModel().addNode(newView.getNode());
			dynamicModelView.createView(newView.getNode(), null);
		}
		else{
			// Record the new view in the list of all the loaded model views
			viewModel.addView(newView.getModelsIdString(), newView);
			layoutManager.addView(newView);
		}
	}
	
	public void removeModelView(String modelName){

		List<YorkEntityView> doomedViews = new ArrayList<YorkEntityView>();
		for(YorkEntityView view: viewModel.getViews()){
			if(modelName.equals(view.getModelsIdString())){
				doomedViews.add(view);
			}				
		}

		for(YorkEntityView view: doomedViews){
			logger.info("Removed:" + view.getModelsIdString());
			removeModelView(view);
		}
	}
	
	public void removeModelView(YorkEntityView doomedView){
		viewModel.getViews().remove(doomedView);
		viewModel.removeView(doomedView.getModelsIdString());
		layoutManager.removeView(doomedView);
	}
	
	public void addModelViewToStage(YorkEntityView newView){
		stagedModelViews.add(newView);
	}
	
	public Collection<YorkEntityView> getModelViews(){
		return viewModel.getViews();
	}
	
	public void setViewModel(ViewModel newViewModel){
		this.viewModel = newViewModel;
		this.backgroundRender = new BackgroundRender(viewModel);
	}
	
	public ViewModel getViewModel(){
		return viewModel;
	}
	
	@SuppressWarnings("deprecation")
	public void paint(Graphics g) {
	
		// If myImage (the off-screen buffer) is null or the panel has been resized the (re)initialise it
		if(myImage == null || myImage.getWidth(null) != getWidth() || myImage.getHeight(null) != getHeight()){
			initGraphics();
		}
		
		backgroundRender.renderBackground(graphics, getWidth(), getHeight());
		
		for(YorkEntityView modelView: viewModel.getViews()){
			modelView.paint(graphics);
		}
				
		if(browser.getCurrentLayoutManager().hasResizeRequest()){
			browser.setCursor(LayoutManager.getResizeCursor(browser.getCurrentLayoutManager().getRequestCorner()));
		}
		else{
			browser.setCursor(Cursor.DEFAULT_CURSOR);
		}

		g.drawImage(myImage, 0, 0, null);
	}
	
	public Graphics getBufferedGraphics(){
		return graphics;
	}
	
	public void save(Frame frame, PropertiesMemento propertiesMemento){
		logger.info("TabView::save()");
		
		// Save the view information - names of models, positions, states etc..
		viewModel.save(propertiesMemento);
		logger.info("Saved ModelView config");
		
		// Save models in this view
		for(YorkEntityView modelView: viewModel.getViews()){
			logger.info("About to save model:" + modelView.getModelsIdString());
			modelView.save();
		}
	}
	
	public void setBackgroundGradientColors(Color newColorTop, Color newColorBottom){
		viewModel.setBackgroundGradientColorTop(newColorTop);
		viewModel.setBackgroundGradientColorBottom(newColorBottom);
	}
	
	public void saveAsImage(){
	
		// Get the file name 
		YorkGUIUtils guiUtils = new YorkGUIUtils();
		String selectedFilename = guiUtils.getUserToDefineFilename(browser, browser.getBaseDir() + "saved images\\", "png", "Images", JFileChooser.SAVE_DIALOG);
		
		if(selectedFilename == null || guiUtils.wasCancelled()){
			return;
		}			
		
		BufferedImage bufferedImage = new BufferedImage(myImage.getWidth(null), myImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
    
        // Paint the image onto the buffered image
        g.drawImage(myImage, 0, 0, null);
        g.dispose();

        try {
            ImageIO.write(bufferedImage, "png", new File(selectedFilename));
        }
        catch (IOException e) {
            e.printStackTrace();
			JOptionPane.showMessageDialog(browser, "Unable to save image to:" + selectedFilename, "Error", JOptionPane.ERROR_MESSAGE);
        }    
	}
	
	// Work out which ModelView is being hit and is on top
	private YorkEntityView getTopView(){

		YorkEntityView topView = null;
		int topZ = -1;
		int modelZ;
		for(YorkEntityView modelView: layoutManager.getYorkModelViews()){
			modelZ = layoutManager.getZValue(modelView);
			logger.info("Name:" + modelView.getModelsIdString() + ", modelZ:" + modelZ + ", inBounds:" + modelView.inBounds());
			if(modelView.inBounds() && modelZ > topZ){
				logger.info("Set topView \"" + modelView.getModelsIdString() + "\"");
				topZ = modelZ;
				topView = modelView;
			}
		}
		return topView;
	}
	
	// Button handler methods
	public void addButton(GUIButton button, RComponentListener listener){
		buttonMap.put(button, listener);
	}
	
	public boolean isMouseDragged(){
		return mouseDragged;
	}
	
	// Mouse motion events
	public void mouseDragged(MouseEvent e){
		
		logger.info("TabView.mouseDragged");
		mouseDragged = true;

		YorkEntityView topView = getTopView();		
		if(topView != null){
			logger.info("Top model is " + topView.getModelsIdString());
			topView.mouseDragged(e);
		}
		repaint();
	}
	
	@SuppressWarnings("deprecation")
	public void mouseMoved(MouseEvent e){
		
		for(YorkEntityView modelView: layoutManager.getYorkModelViews()){
			modelView.mouseMoved(e);
		}
		
		// If the mouse moves over a button then change the cursor
		boolean mouseOverButton = false;
		for(GUIButton button: buttonMap.keySet()){
			if(button.isMouseOver(e)){
				mouseOverButton = true;
			}
		}
		
		if(mouseOverButton){
			logger.debug("hand cursor");
			browser.setCursor(Cursor.HAND_CURSOR); 
		}
		
	}
	
	// MouseInputListener events
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		
		logger.info("mousePressed() Button:" + e.getButton());

		YorkEntityView topView = getTopView();		

		if(topView != null){
			// TODO Understand - why is it not just calling mousePressed on topView?
			for(YorkEntityView modelView: layoutManager.getYorkModelViews()){
				modelView.mousePressed(e);	
				repaint();
			}
		}
		else{
			// Show the right click menu
			if(e.getButton() == MouseEvent.BUTTON3){
				// Background pop-up
				// Create a right click menu from supplied config
				JPopupMenu popup = new JPopupMenu();
				for(String label: rightClickLabels){
					JMenuItem menuItem = new JMenuItem(label);
					menuItem.addActionListener(this);
					popup.add(menuItem);
				}
				popup.show(browser.getCurrentView(), e.getX(), e.getY());			
			}
		}

		if(topView != null && topView.closeButtonHit){
			logger.info("mousePressed() Include " + topView.getModelsIdString() + " as a view to remove");
			viewsToRemove.clear();
			viewsToRemove.add(topView);
		}		
		
		// Remove those views that are listed to remove
		if(viewsToRemove.size() != 0){
			// TODO SHould only be one view to remove!! not a list!?
			for(YorkEntityView view: viewsToRemove){
				if(view instanceof DynamicModelNodeView){
					dynamicModelView.removeNodeView(view);
				}
				else{
					removeModelView(view);
				}
			}
			viewsToRemove.clear();
			logger.info("viewsToRemove cleared, size now:" + viewsToRemove.size());
			repaint();
		}
	
		// Pass on the event to any button listeners
		for(GUIButton button: buttonMap.keySet()){
			if(button.isMouseOver(e)){
				buttonMap.get(button).actionEvent(button);
			}
		}

	}
	
	public void mouseReleased(MouseEvent e){
		mouseDragged = false;

		for(YorkEntityView modelView: layoutManager.getYorkModelViews()){
			modelView.mouseReleased(e);
		}
	
		if(stagedModelViews.size() != 0){
			for(YorkEntityView view: stagedModelViews){
				addModelView(view);
			}
			stagedModelViews.clear();
			logger.info("StageModelViews cleared, size now:" + stagedModelViews.size());
		}
	}
	
	// Action Listener - for the right click menu
	public void actionPerformed(ActionEvent event){
		logger.info("ActionEvent:" + event);
		
		if(CREATE_DYNAMIC_NODE.equals(event.getActionCommand())){
			browser.createNewDynamicNode();
			return;
		}
		
		if(SAVE_AS_IMAGE.equals(event.getActionCommand())){
			saveAsImage();
			return;
		}
		
		if(EDIT_PROPERTIES.equals(event.getActionCommand())){
			IndividualPanelDialog propsDialog = new IndividualPanelDialog(browser, 
					new ColorGradientPanel("Set the node color", "Top Color:", "dmNodeColour", 
							viewModel.getBackgroundGradientColorTop(), viewModel.getBackgroundGradientColorBottom()), 
							"Choose Color");
			propsDialog.setSize(new Dimension(330, 400));
			propsDialog.showDialog();
			
			Map<String, Object> colorMap = propsDialog.getResultsMap();
			if(colorMap.get(ColorGradientPanel.TOP_COLOR) != null){
				viewModel.setBackgroundGradientColorTop((Color)colorMap.get(ColorGradientPanel.TOP_COLOR));
			}

			if(colorMap.get(ColorGradientPanel.BOTTOM_COLOR) != null){
				viewModel.setBackgroundGradientColorBottom((Color)(colorMap.get(ColorGradientPanel.BOTTOM_COLOR)));
			}

			browser.repaint();
			return;
		}
	}	
}
