package com.zygomeme.york.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;

import com.zygomeme.york.Identifiable;
import com.zygomeme.york.Node;
import com.zygomeme.york.util.StringUtil;
import com.zygomeme.york.gui.EntityViewConfig;
import com.zygomeme.york.gui.TabView.ResizeCorner;

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
 * The super class of model views - the viewable aspect of models that appear in the GUI. 
 * Has a lot of reused functionality making it easy to add new, pretty views. 
 * 
 *
 */
public class YorkEntityView{
	
	private static final long serialVersionUID = 8166300233361917039L;

	protected Logger logger = Logger.getLogger(YorkEntityView.class);
	protected EntityViewConfig config;
	protected Identifiable identifiable;
	protected String filename = null;
	protected boolean inBounds = false;
	protected boolean inHeader = false;
	private int headerHeight = 12;
	protected boolean closeButtonHit = false;
	protected boolean saveButtonHit = false;
	protected int widthPad = 3; // The width of the padding
	private   int imagePad = 3;
	protected int mdx, mdy;
	protected Image closeImage;
	protected Image saveImage;	
	
	protected String[] rightClickLabels = new String[0];
	protected ActionListener rightClickListener;
	
	protected int closeImageXPos;
	protected int saveImageXPos;
	
	protected YorkBrowser browser;

	public YorkEntityView(){}

	protected YorkEntityView(YorkBrowser browserIn){
		this.browser = browserIn;
		closeImage = new ImageIcon(browser.getProperties().getImageDir() + "close_button.png").getImage();	
		saveImage = new ImageIcon(browser.getProperties().getImageDir() + "save.gif").getImage();
	}

	protected YorkEntityView(Identifiable identifiableIn, YorkBrowser browserIn, EntityViewConfig configIn){
		this(browserIn);
		this.identifiable = identifiableIn;
		this.config = configIn;
	}
		
	public void addAToView(YorkEntityView toView){
		logger.info("Default **NULL** addAToView");
	}
	
	public void addAFromView(YorkEntityView toView){
		logger.info("Default **NULL** addAFromView");		
	}

	public String getModelsIdString(){
		return identifiable.getIdString();
	}
	
	public void setModelsIdString(String newId){
		identifiable.setIdString(newId);
	}
	
	public void setFilename(String newFilename){
		this.filename = newFilename;
	}

	public String getFilename(){
		return filename;
	}
	
	/**
	 * Returns the model that this view represents
	 * @return
	 */
	public Node getNode(){
		logger.info("Child class **may** have to implement this method \"getNode()\"");
		return null;
	}
	
	/** 
	 * Designed to be called from the child object to perform the standard drawing features
	 */
	public void paint(Graphics g){
		
		logger.info("YorkModelView.paint()" + getModelsIdString());

		closeImageXPos = config.getX() + config.getWidth() - closeImage.getWidth(null);
		saveImageXPos = config.getX() + config.getWidth() - ((saveImage.getWidth(null)  + imagePad) * 2);

		FontMetrics metrics = g.getFontMetrics(); 
		int fontHeight = metrics.getHeight();

		// Draw translucent box
		g.setColor(config.getBackgroundColor());
		g.fillRect(config.getX(), config.getY(), config.getWidth(), config.getHeight());

		g.setColor(config.getBorderColor());
		g.drawRect(config.getX(), config.getY(), config.getWidth(), config.getHeight());
		headerHeight = fontHeight + 2;
		Color headerColor = new Color(config.getBorderColor().getRed(), 
									  config.getBorderColor().getGreen(),
									  config.getBorderColor().getBlue(), 
									  config.getBorderColor().getAlpha() - 210); 
		g.setColor(headerColor);
		g.fillRect(config.getX(), config.getY(), config.getWidth(), headerHeight);
		g.setColor(config.getBorderColor());
		g.drawLine(config.getX(), config.getY() + headerHeight, config.getX() + config.getWidth(), config.getY() + headerHeight);

		// Draw the close button
		if(config.hasCloseButton()){
			g.drawImage(closeImage, closeImageXPos, config.getY(), null);
		}
		if(config.hasSaveButton()){
			g.drawImage(saveImage, saveImageXPos, config.getY(), null);
		}
				
		// Grab the focus back when ever there's a paint - not so good if the app auto paints??
		browser.requestFocus();
	}
	
	public void save(){
		logger.info("Default **NULL** save");
	}
	
	public EntityViewConfig getConfig(){
		return config;
	}

	public void setConfig(EntityViewConfig newConfig){
		this.config = newConfig;
	}

	public boolean inBounds(){
		return inBounds;
	}

	protected int wrapCollectionStringArray(Graphics g, int fontHeight, Collection<String[]> content, int lineCount, int maxLineCount, FontMetrics metrics){

		for(String[] stringArray: content){
			lineCount = wrap(g, fontHeight, StringUtil.arrayToString(stringArray), lineCount, maxLineCount, metrics);
		}
		return lineCount;
	}
	
	protected int wrap(Graphics g, int fontHeight, String content, int lineCount, int maxLineCount, FontMetrics metrics){
		return wrap(g, fontHeight, content, lineCount, maxLineCount, metrics, null);
	}

	protected int wrap(Graphics g, int fontHeight, int lineCount, int maxLineCount, FontMetrics metrics, GUIText guiText){
		return wrap(g, fontHeight, guiText.getMessage(), lineCount, maxLineCount, metrics, guiText);
	}
	
	protected int wrap(Graphics g, int fontHeight, String content, int lineCount, int maxLineCount, FontMetrics metrics, GUIText guiText){

		// Don't write below the bottom of the box
		if(lineCount > maxLineCount){
			return lineCount;
		}

		int widthToBreak;
		int start = 0;
		while(start < content.length()){
			widthToBreak = findWidthToSpace(content, start, metrics);
			
			drawFormattedString(g, content.substring(start, Math.min(content.length(), start + widthToBreak)), 
					config.getX() + widthPad, 
					config.getY() + (lineCount * fontHeight), start, widthToBreak, guiText);
			
			lineCount++;

			// Don't write below the bottom of the box
			if(lineCount > maxLineCount){
				return lineCount;
			}
			start += widthToBreak;
		}
		return lineCount;
	}

	private void drawFormattedString(Graphics g, String string, int x, int y, int start, int width, GUIText guiText){
		
		if(guiText == null){
			g.drawString(string, x, y);
			return;
		}
		
		FontMetrics metrics = g.getFontMetrics();
		//logger.info("Draw String>" + string + "< start:" + start + ", width:" + width);
		
		int lastPoint = start;
		int cx = x; String greenText; String redText;
		List<Integer[]> pairs = guiText.getStartAndEndPoints();
		Iterator<Integer[]> it = pairs.iterator();  
		Integer[] pair;
		while(it.hasNext() && !((lastPoint - start) > string.length())){
			pair = it.next();
			//logger.info("(pair[0]" + pair[0] + ", pair[1]:" + pair[1] + ", lastPoint:" + lastPoint);
			
			if(start < pair[0]){
				g.setColor(Color.GREEN);
				greenText = string.substring(lastPoint - start, Math.min(pair[0] - start, string.length()));
				//logger.info("Green text:" + greenText);
				g.drawString(greenText, cx, y);
				cx += metrics.stringWidth(greenText);
				lastPoint = pair[0];
			}

			if(pair[0] <= (start + width) && pair[1] >= start){
				g.setColor(Color.RED);
				//logger.info("String from and to:" + Math.max(pair[0] - start, 0) + ", " + Math.min(pair[1] - start, string.length()));
				redText = string.substring(Math.max(pair[0] - start, 0), Math.min(pair[1] - start, string.length()));
				//logger.info("Red text:" + redText);
				g.drawString(redText, cx, y);
				cx += metrics.stringWidth(redText);				
				lastPoint = pair[1];
			}
		}
		
		//logger.info("lastPoint:" + lastPoint + ", start:" + start + ", stringlength:" + string.length());
		if(lastPoint - start <= string.length()){
			g.setColor(Color.GREEN);
			greenText = string.substring(Math.max(lastPoint - start, 0));
			//logger.info("Final text:" + greenText);
			g.drawString(greenText, cx, y);
		}
		
	}
	
	/**
	 * Return the position of the last space in the content so that the string fills the width of
	 * the nugget where the string for that line is from "start" to the calculated "lastBest". 
	 * 
	 * @param content
	 * @param start
	 * @param metrics
	 * @return
	 */
	private int findWidthToSpace(String content, int start, FontMetrics metrics){

		int lastBest = -1;

		// For the case where the whole string fits in the width
		if(metrics.stringWidth(content.substring(start, content.length())) < (config.getWidth() - (2 * widthPad))){
			lastBest = (content.length() - start) + 1 ;
			return lastBest;
		}

		int breakPoint = start + 1;
		for(int i = start + 1; i < content.length(); i++){
			// Find the last space on a line so that is shorter than the width
			if(content.charAt(i) == ' ' && metrics.stringWidth(content.substring(start, i + 1)) < (config.getWidth() - (2 * widthPad))){
				lastBest = (i - start) + 1;
			}			
			
			// Calculate the point at which to break a word or number that cannot fit by itself on the line
			if(metrics.stringWidth(content.substring(start, i + 1)) < (config.getWidth() - (2 * widthPad))){
				breakPoint = (i - start) + 1;
			}
		}
		
		if(lastBest == -1){
			// no good position yet found because the word/number is wider than the nugget
			lastBest = breakPoint;
		}
		return lastBest;
	}

	public YorkBrowser getBrowser(){
		return browser;
	}
	
	public void mouseMoved(MouseEvent e){

		setBoundsFlags(e);

		ResizeCorner resizeCorner = getResizeCorner(e);
		
		// If the distance is less than the threshold then record a request to 
		// set the cursor to the resize cursor (the mouse/pointer icon). 
		// If the number of requests is greater than zero then the cursor will be
		// set to the resize icon, otherwise it will be the default icon. The
		// icon is managed by the layout manager to avoid the individual views from
		// switching the icon on and off only WRT themselves and overwriting the 
		// requests from the other views. 
		if(resizeCorner != ResizeCorner.NONE && inBounds){
			if(!browser.getCurrentLayoutManager().hasResizeRequest()){
				browser.getCurrentLayoutManager().addResizeRequest(this, resizeCorner);
				browser.repaint();
			}
		}
		else{
			if(browser.getCurrentLayoutManager().hasResizeRequest() && !browser.getCurrentView().isMouseDragged()){
				browser.getCurrentLayoutManager().removeResizeRequest(this);
				browser.repaint();
			}
		}
	}
	
	// Default drag behaviour
	public void mouseDragged(MouseEvent e){
		
		logger.debug("Dragged");
		ResizeCorner resizeCorner = getResizeCorner(e);
		logger.debug("Resize corner:" + resizeCorner);
		// If this view (or subclass) has requested a resize 
		if(browser.getCurrentLayoutManager().hasResizeCursorRequest(this)){

			if(resizeCorner == ResizeCorner.TOP_LEFT){
				if(e.getX() < config.getX()){
					config.setWidth(config.getWidth() + (config.getX() - e.getX()));
					config.setX(e.getX());
				}
				else{
					config.setWidth(config.getWidth() - (e.getX() - config.getX()));
					config.setX(e.getX());					
				}

				if(e.getY() < config.getY()){
					config.setHeight(config.getHeight() + (config.getY() - e.getY()));
					config.setY(e.getY());
				}
				else{
					config.setHeight(config.getHeight() - (e.getY() - config.getY()));
					config.setY(e.getY());					
				}
			}

			if(resizeCorner == ResizeCorner.TOP_RIGHT){
				config.setWidth(e.getX() - config.getX());
				
				if(e.getY() < config.getY()){
					config.setHeight(config.getHeight() + (config.getY() - e.getY()));
					config.setY(e.getY());
				}
				else{
					config.setHeight(config.getHeight() - (e.getY() - config.getY()));
					config.setY(e.getY());					
				}
			}

			if(resizeCorner == ResizeCorner.BOTTOM_LEFT){
				if(e.getX() < config.getX()){
					config.setWidth(config.getWidth() + (config.getX() - e.getX()));
					config.setX(e.getX());
				}
				else{
					config.setWidth(config.getWidth() - (e.getX() - config.getX()));
					config.setX(e.getX());					
				}
				
				if(e.getY() > config.getY()){
					config.setHeight(e.getY() - config.getY());
				}
				else{
					config.setHeight(config.getHeight() - (e.getY() - config.getY()));
					config.setY(e.getY());					
				}
			}

			if(resizeCorner == ResizeCorner.BOTTOM_RIGHT){
				logger.debug("Bottom right resize point");
				config.setWidth(Math.max(config.getMinWidth(), e.getX() - config.getX()));
				config.setHeight(Math.max(config.getMinHeight(), e.getY() - config.getY()));
			}

			browser.getCurrentView().repaint();			
			return;
		}
		else{
			// Drag if in bounds
			if(inHeader){
				config.setX(e.getX() - mdx);
				config.setY(e.getY() - mdy);
				browser.getCurrentView().repaint();
			}
		}
	}
	
	public void mousePressed(MouseEvent e){

		mdx = e.getX() - config.getX();
		mdy = e.getY() - config.getY();

		setBoundsFlags(e);
		if(!inBounds){
			return;
		}
		
		if(config.hasCloseButton() && closeImage != null && e.getX() > closeImageXPos && 
				e.getX() < (config.getX() + config.getWidth()) && 
				e.getY() > config.getY() && 
				e.getY() < (config.getY() + closeImage.getHeight(null))){
			closeButtonHit = true;
		}

		if(config.hasSaveButton() && saveImage != null && e.getX() > saveImageXPos && 
				e.getX() < saveImageXPos + saveImage.getWidth(null) && 
				e.getY() > config.getY() && 
				e.getY() < (config.getY() + saveImage.getHeight(null))){
			logger.info("Save button hit");
			saveButtonHit = true;
		}
		else{
			saveButtonHit = false;
		}
		
		// Set up a right-click menu
		if(e.getButton() == MouseEvent.BUTTON3 && inBounds && rightClickLabels.length > 0){			

			// Create a right click menu from supplied config
			JPopupMenu popup = new JPopupMenu();
			for(String label: rightClickLabels){
				JMenuItem menuItem = new JMenuItem(label);
				menuItem.addActionListener(rightClickListener);
				popup.add(menuItem);
			}

			logger.info("popup.isDisplayable()" + popup.isDisplayable());
			logger.info("popup.isVisible" + popup.isVisible());
			logger.info("popup.isEnabled" + popup.isEnabled());
			logger.info("Showing:" + browser.getCurrentView().isShowing());
			logger.info("Current View:" + browser.getCurrentView().getViewModel().getName());
			
			popup.show(browser.getCurrentView(), e.getX(), e.getY());
		}	

	}
	
	public void mouseReleased(MouseEvent e){
		setBoundsFlags(e);
	}
	
	private ResizeCorner getResizeCorner(MouseEvent e){
		
		if(browser.getCurrentView().isMouseDragged()){
			return browser.getCurrentLayoutManager().getRequestCorner();
		}
		
		int rx = config.getX() + config.getWidth();
		int lx = config.getX();
		int ty = config.getY();
		int by = config.getY() + config.getHeight();
		
		// Simple block distance is fine and computational inexpensive
		// Work out the distances from each four corners
		int distTL = Math.abs(e.getX() - lx) + Math.abs(e.getY() - ty);
		int distTR = Math.abs(e.getX() - rx) + Math.abs(e.getY() - ty);
		int distBL = Math.abs(e.getX() - lx) + Math.abs(e.getY() - by);
		int distBR = Math.abs(e.getX() - rx) + Math.abs(e.getY() - by);
		// Get the minimum of all these
		int dist = Math.min(Math.min(distTL, distTR), Math.min(distBL, distBR));
		
		if(dist > 20){
			return ResizeCorner.NONE;
		}
		
		if(distTL == dist){
			return ResizeCorner.TOP_LEFT;
		}
		// No resize on the top right for dynamic model nodes as this is where the 
		// close button is. If the user tries to resize from that point they *will*
		// hit the close button instead - causing much annoyance. 
		// Not the perfect user experience as the user will wonder
		// why they can't resize on top right. 
		if(!(this instanceof DynamicModelNodeView) && distTR == dist){
			return ResizeCorner.TOP_RIGHT;
		}
		if(distBR == dist){
			return ResizeCorner.BOTTOM_RIGHT;
		}
		if(distBL == dist){
			return ResizeCorner.BOTTOM_LEFT;
		}
		return ResizeCorner.NONE;
	}
	
	private void setBoundsFlags(MouseEvent e){

		// Set whether in the bounds of the box
		if(e.getX() > config.getX() && e.getX() < (config.getX() + config.getWidth()) && 
				e.getY() > config.getY() && e.getY() < (config.getY() + config.getHeight())){
			inBounds = true;
		}
		else{
			inBounds = false;
		}
		
		// Set whether in the bounds of the header of the box
		if(e.getX() > config.getX() && e.getX() < (config.getX() + config.getWidth()) && 
				e.getY() > config.getY() && e.getY() < (config.getY() + headerHeight)){
			inHeader = true;
		}
		else{
			inHeader = false;
		}
		
	}
}
