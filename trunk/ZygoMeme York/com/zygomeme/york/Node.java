package com.zygomeme.york;

import java.util.List;

/**
 *  * **********************************************************************
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
 * Node is the interface of node like classes that appear in models.  
 * 
 */

public interface Node extends Identifiable{
	
	public List<Node> getFromNodes(); // TODO Perhaps these should be Set to allow for the classes
									// having Map<String, node> and returning keySets, and values()
	public List<Node> getToNodes(); // TODO Perhaps these should be Set to allow for the classes
	// having Map<String, node> and returning keySets, and values()
	public List<String> getFromNodeIds();
	public List<String> getToNodeIds();
	public int getInputCount();
	public void reset();
	public double getOutputValue();
	public void updateStarterNode(int t);
	public void setTimePoint(int t);
	public void update(int t);
	public int getTimePoint();
	public void addInput(Node newNode);
	public void removeInput(Node node);
	public void removeOutput(Node node);
	public void addOutput(Node newNode);
	
}
