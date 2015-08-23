package com.zygomeme.york.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

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
 * Designed to load the head of a York XML file so that it is possible to work out
 * what the file type is. 
 * 
 *
 */
public class HeadHandler extends StandardHandler{

	public enum FileType{DYNAMIC_MODEL, DYNAMIC_NODE, MODEL};
	private FileType fileType = null;
	private String content;
		
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) 
		throws SAXException
	{
		// If the fileType has already been set then the handler has read the file type 
		// - so skip everything else. Sadly this means that the handler will continue
		// to read through the rest to of the file, but at least this is a simple implementation  
		if(fileType!= null){
			return;
		}

		if("dynamic_model".equals(qName)){
			fileType = FileType.DYNAMIC_MODEL;
			return;
		}	
		
		if("model_view".equals(qName)){
			fileType = FileType.MODEL;
			return;
		}	
	}
	
	public FileType getFileType(){
		return fileType;
	}
	
	/*
	 * Returns the head of the file. This can then be shown to the user if/when an error occurs.
	 */
	public String getHead(){
		if(content != null){
			return content.substring(0, Math.min(80, content.length() - 1));
		}
		else{
			return "empty";			
		}
	}
}
