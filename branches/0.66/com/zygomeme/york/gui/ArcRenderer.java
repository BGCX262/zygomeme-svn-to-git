package com.zygomeme.york.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.util.List;

import com.zygomeme.york.ArcMap;
import com.zygomeme.york.gui.EntityViewConfig;

/**
 * *********************************************************************
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
 * Draws the arcs (and arrows) - either as lines or as cubic curves.
 *
 */

public class ArcRenderer {

	private enum Side{TOP, BOTTOM, LEFT, RIGHT};
	public enum LineStyle{STRAIGHT, CUBIC};
	
	private int bfx = 0; int bfy = 0; int btx = 0; int bty = 0; // Best From X, Best From Y etc..
	private int cx1, cx2, cy1, cy2; // Control points;
	private int curveFactor = 20; // Curve factor - distance of the control point from the node
	private int shortestDist = Integer.MAX_VALUE;	
	private LineStyle lineStyle = LineStyle.CUBIC;
		
	
	public void renderArcs(ArcMap arcMap, Graphics2D g){
		
		List<Arc> arcs = arcMap.getArcs();		

		if(arcs.size() == 0){
			return;
		}
		
		g.setColor(Color.DARK_GRAY);	
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Draw each and every arc
		for(Arc arc: arcs){
			// Special case for nodes that point to themselves 
			if(arc.getFromId().equals(arc.getToId())){
				drawArcToSelf(g, arc);
			}
			else{
				drawArc(g, arc);
			}
		}	
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

	}
	
	private void checkShortestAndSetCoords(int fx, int fy, int tx, int ty, Side sideIn){

		int dist = (int)Math.hypot(fx - tx, fy - ty); 
		if(dist < shortestDist){
			shortestDist = dist;
			bfx = fx; bfy = fy; btx = tx; bty = ty;
			int minCurveFactor = 30;
			if((dist / 2) < minCurveFactor){
				minCurveFactor = dist / 2;
			}
			switch(sideIn){
			case TOP: cx2 = btx; cy2 = bty - Math.max(((bty - bfy) / 2), minCurveFactor); break;
			case BOTTOM: cx2 = btx; cy2 = bty + Math.max(((bfy - bty) / 2), minCurveFactor); break;
			case LEFT: cx2 = btx - Math.max(((btx - bfx) / 2), minCurveFactor); cy2 = bty; break;
			case RIGHT: cx2 = btx + Math.max(((bfx - btx) / 2), minCurveFactor); cy2 = bty; break;
			}
		}
	}

	private void drawArcToSelf(Graphics2D g, Arc arc){
		
		EntityViewConfig fromConfig = arc.getFromConfig();
		int stepIn = 15;
		int fx = fromConfig.getX() + fromConfig.getWidth();
		int fy = fromConfig.getY() + stepIn;
		int tx = fromConfig.getX() + fromConfig.getWidth() - stepIn;
		int ty = fromConfig.getY();
		int cx1 = fx + (stepIn * 3);
		int cy1 = fy - 8;
		int cx2 = tx;
		int cy2 = ty - (stepIn * 3);
		
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		CubicCurve2D.Float qcf = new CubicCurve2D.Float(fx, fy, cx1, cy1, cx2, cy2, tx, ty);
		g.draw((CubicCurve2D) qcf);
		g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		drawArrow(cx2 + 10, cy2, tx, ty, g);
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	
	private void drawArc(Graphics2D g, Arc arc){
	
		EntityViewConfig fromConfig = arc.getFromConfig();
		// ftx is From Top X
		int ftx = fromConfig.getX() + (fromConfig.getWidth() / 2); 
		int fty = fromConfig.getY();
		// From Left X and Y
		int flx = fromConfig.getX(); 
		int fly = fromConfig.getY() + (fromConfig.getHeight() / 2);
		// From Right 
		int frx = fromConfig.getX() + fromConfig.getWidth(); 
		int fry = fromConfig.getY() + (fromConfig.getHeight() / 2);
		// From Bottom 
		int fbx = fromConfig.getX() + (fromConfig.getWidth() / 2); 
		int fby = fromConfig.getY() + fromConfig.getHeight();

		EntityViewConfig toConfig = arc.getToConfig();
		// ttx To Top X
		int ttx = toConfig.getX() + (toConfig.getWidth() / 2); 
		int tty = toConfig.getY();
		// To Left X and Y
		int tlx = toConfig.getX(); 
		int tly = toConfig.getY() + (toConfig.getHeight() / 2);
		// To Right 
		int trx = toConfig.getX() + toConfig.getWidth(); 
		int tRy = toConfig.getY() + (toConfig.getHeight() / 2);
		// To Bottom 
		int tbx = toConfig.getX() + (toConfig.getWidth() / 2); 
		int tby = toConfig.getY() + toConfig.getHeight();
		
		// Now find the shortest route
		bfx = bfy = btx = bty = 0; // Best From X, Best From Y etc..
		shortestDist = Integer.MAX_VALUE;
		
		// Skip the ones that should not be possible (with straight lines), e.g. top to top. 
		//checkShortestAndSetCoords(ftx, fty, ttx, tty); // top to top -- SKIP
		checkShortestAndSetCoords(flx, fly, ttx, tty, Side.TOP); // left to top
		checkShortestAndSetCoords(fbx, fby, ttx, tty, Side.TOP); // bottom to top
		checkShortestAndSetCoords(frx, fry, ttx, tty, Side.TOP); // right to top 

		checkShortestAndSetCoords(ftx, fty, trx, tRy, Side.RIGHT); // top to right
		checkShortestAndSetCoords(flx, fly, trx, tRy, Side.RIGHT); // left to right
		checkShortestAndSetCoords(fbx, fby, trx, tRy, Side.RIGHT); // bottom to right
		//checkShortestAndSetCoords(frx, fry, trx, tRy); // right to right  -- SKIP

		checkShortestAndSetCoords(ftx, fty, tbx, tby, Side.BOTTOM); // top to bottom
		checkShortestAndSetCoords(flx, fly, tbx, tby, Side.BOTTOM); // left to bottom
		//checkShortestAndSetCoords(fbx, fby, tbx, tby); // bottom to bottom -- SKIP
		checkShortestAndSetCoords(frx, fry, tbx, tby, Side.BOTTOM); // right to bottom  

		checkShortestAndSetCoords(ftx, fty, tlx, tly, Side.LEFT); // top to left 
		//checkShortestAndSetCoords(flx, fly, tlx, tly); // left to left -- SKIP
		checkShortestAndSetCoords(fbx, fby, tlx, tly, Side.LEFT); // bottom to left 
		checkShortestAndSetCoords(frx, fry, tlx, tly, Side.LEFT); // right to left  
		
		// Update the arc
		arc.setEndPoints(bfx, bfy, btx, bty);
		
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		boolean modified = arc.isModified();
		if(modified){
			float[] dash = new float[]{10F, 6.0F, 3.0F, 6.0F};
			g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, dash, 5));
		}
		else{
			g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		}
		
		if(lineStyle == LineStyle.STRAIGHT){
			g.drawLine(bfx, bfy, btx, bty);
			g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

			drawArrow(bfx, bfy, btx, bty, g);
			//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		else{

			// Cubic curves
			// Calculate the control points for the from end. The control points for the "to" end 
			// have already been calculated in the checkShortestAndSetCoords method
			if(bfx > btx){
				cx1 = bfx - curveFactor;
			}
			else{
				cx1 = bfx + curveFactor;
			}

			if(bfy > bty){
				cy1 = bfy - curveFactor;
			}
			else{
				cy1 = bfy + curveFactor;
			}

			// Debug control points
			//g.drawRect(cx2, cy2, 4, 4);
			//g.drawRect(cx1, cy1, 4, 4);

			CubicCurve2D.Float qcf = new CubicCurve2D.Float(bfx, bfy, cx1, cy1, cx2, cy2, btx, bty);
			g.draw((CubicCurve2D) qcf);
			g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			drawArrow(cx2, cy2, btx, bty, g);
		}
	}
	
    private void drawArrow(int fx, int fy, int tx, int ty, Graphics g){

        int size = 10;
        // Calc angle
        double dx = tx - fx; 
        double dy = ty - fy;
        double r =  Math.atan2(dy, dx) - (Math.PI / 2.0);    
        double cosR = Math.cos(r);
        double sinR = Math.sin(r);
            
        // set points
        int px = tx; int py = ty;
        int halfSize = size / 2;
        int a1x = tx - halfSize; int a1y = ty - size;
        int a2x = tx + halfSize; int a2y = ty - size;
        
        // Rotate points
        int rpx = rotateXPoint(px, py, cosR, sinR, px, py);
        int rpy = rotateYPoint(px, py, cosR, sinR, px, py);
        int ra1x = rotateXPoint(a1x, a1y, cosR, sinR, px, py);
        int ra1y = rotateYPoint(a1x, a1y, cosR, sinR, px, py);
        int ra2x = rotateXPoint(a2x, a2y, cosR, sinR, px, py);
        int ra2y = rotateYPoint(a2x, a2y, cosR, sinR, px, py);
        
        // Draw filled arrow with a line around it
        int xPoints[] = new int[]{ra1x, rpx, ra2x};
        int yPoints[] = new int[]{ra1y, rpy, ra2y};
        g.fillPolygon(xPoints, yPoints, 3);
        //Color tmpColor = g.getColor();
        //g.setColor(Color.DARK_GRAY); 
        //g.drawPolygon(xPoints, yPoints, 3);
        //g.setColor(tmpColor);
    }

    private int rotateXPoint(double x, double y, double cosR, double sinR, double ox, double oy){
        
        // Adjust to origin
        double newX = x - ox;
        double newY = y - oy;
        return (int)(((newX * cosR) - (newY * sinR)) + ox);
    }

    // Rotate the y point x,y by r around an orgin ox,oy
    private int rotateYPoint(double x, double y, double cosR, double sinR, double ox, double oy){
        
        double newX = x - ox;
        double newY = y - oy;
        return (int)(((newY * cosR) + (newX * sinR))+ oy);
    }
    
    public void setLineStyle(LineStyle newLineStyle){
    	
    	lineStyle = newLineStyle;
    }

}
