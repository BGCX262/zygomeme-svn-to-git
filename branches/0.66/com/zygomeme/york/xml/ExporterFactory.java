package com.zygomeme.york.xml;

import com.zygomeme.york.Node;
import com.zygomeme.york.dynamicmodels.DynamicModelNode;

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
 * Factory to create a specific Exporter for a given type of node 
 * 
 */

public class ExporterFactory {

	public static StandardExporter getExporter(Node node){

		if(node instanceof DynamicModelNode){
			return new DynamicModelNodeExporter();
		}

		throw new RuntimeException("No exporter for that file type:" + node.getClass().getName());
	}
}
